package session

import (
	"bytes"
	"crypto/tls"
	"encoding/json"
	"errors"
	"flag"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"net/http/httputil"
	"reflect"
	"time"
	"os"
	"mime/multipart"
	"github.com/golang/glog"
	"log"
	"strings"
)

type aviResult struct {
	// Code should match the HTTP status code.
	Code int `json:"code"`

	// Message should contain a short description of the result of the requested
	// operation.
	Message *string `json:"message"`
}

// AviError represents an error resulting from a request to the Avi Controller
type AviError struct {
	// aviresult holds the standard header (code and message) that is included in
	// responses from Avi.
	aviResult

	// verb is the HTTP verb (GET, POST, PUT, PATCH, or DELETE) that was
	// used in the request that resulted in the error.
	verb string

	// url is the URL that was used in the request that resulted in the error.
	url string

	// httpStatusCode is the HTTP response status code (e.g., 200, 404, etc.).
	httpStatusCode int

	// err contains a descriptive error object for error cases other than HTTP
	// errors (i.e., non-2xx responses), such as socket errors or malformed JSON.
	err error
}

// Error implements the error interface.
func (err AviError) Error() string {
	var msg string

	if err.err != nil {
		msg = fmt.Sprintf("error: %v", err.err)
	} else if err.Message != nil {
		msg = fmt.Sprintf("HTTP code: %d; error from Avi: %s",
			err.httpStatusCode, *err.Message)
	} else {
		msg = fmt.Sprintf("HTTP code: %d.", err.httpStatusCode)
	}

	return fmt.Sprintf("Encountered an error on %s request to URL %s: %s",
		err.verb, err.url, msg)
}

//AviSession maintains a session to the specified Avi Controller
type AviSession struct {
	// host specifies the hostname or IP address of the Avi Controller
	host string

	// username specifies the username with which we should authenticate with the
	// Avi Controller.
	username string

	// password specifies the password with which we should authenticate with the
	// Avi Controller.
	password string

	// auth token generated by Django, for use in token mode
	authToken string

	// optional callback function passed in by the client which generates django auth token
	refreshAuthToken func() string

	// insecure specifies whether we should perform strict certificate validation
	// for connections to the Avi Controller.
	insecure bool

	// optional tenant string to use for API request
	tenant string

	// optional version string to use for API request
	version string

	// internal: session id for this session
	sessionid string

	// internal: csrfToken for this session
	csrfToken string

	// internal: referer field string to use in requests
	prefix string
}

const DEFAULT_AVI_VERSION = "17.1.2"

//NewAviSession initiates a session to AviController and returns it
func NewAviSession(host string, username string, options ...func(*AviSession) error) (*AviSession, error) {
	if flag.Parsed() == false {
		flag.Parse()
	}
	avisess := &AviSession{
		host:     host,
		username: username,
	}
	avisess.sessionid = ""
	avisess.csrfToken = ""
	avisess.prefix = "https://" + avisess.host + "/"
	avisess.tenant = ""
	avisess.insecure = false

	for _, option := range options {
		err := option(avisess)
		if err != nil {
			return avisess, err
		}
	}

	if avisess.version == "" {
		avisess.version = DEFAULT_AVI_VERSION
	}

	err := avisess.initiateSession()
	return avisess, err
}

func (avisess *AviSession) initiateSession() error {
	if avisess.insecure == true {
		glog.Warning("Strict certificate verification is *DISABLED*")
	}

	// If refresh auth token is provided, use callback function provided
	if avisess.isTokenAuth() {
		if avisess.refreshAuthToken != nil {
			avisess.setAuthToken(avisess.refreshAuthToken())
		}
	}

	// initiate http session here
	// first set the csrf token
	var res interface{}
	rerror := avisess.Get("", res)

	// now login to get session_id, csrfToken
	cred := make(map[string]string)
	cred["username"] = avisess.username

	if avisess.isTokenAuth() {
		cred["token"] = avisess.authToken
	} else {
		cred["password"] = avisess.password
	}

	rerror = avisess.Post("login", cred, res)
	if rerror != nil {
		return rerror
	}

	glog.Infof("response: %v", res)
	if res != nil && reflect.TypeOf(res).Kind() != reflect.String {
		glog.Infof("results: %v error %v", res.(map[string]interface{}), rerror)
	}

	return nil
}

// SetPassword - Use this for NewAviSession option argument for setting password
func SetPassword(password string) func(*AviSession) error {
	return func(sess *AviSession) error {
		return sess.setPassword(password)
	}
}

func (avisess *AviSession) setPassword(password string) error {
	avisess.password = password
	return nil
}

// SetVersion - Use this for NewAviSession option argument for setting version
func SetVersion(version string) func(*AviSession) error {
	return func(sess *AviSession) error {
		return sess.setVersion(version)
	}
}

func (avisess *AviSession) setVersion(version string) error {
	avisess.version = version
	return nil
}

// SetAuthToken - Use this for NewAviSession option argument for setting authToken
func SetAuthToken(authToken string) func(*AviSession) error {
	return func(sess *AviSession) error {
		return sess.setAuthToken(authToken)
	}
}

func (avisess *AviSession) setAuthToken(authToken string) error {
	avisess.authToken = authToken
	return nil
}

// SetAuthToken - Use this for NewAviSession option argument for setting authToken
func SetRefreshAuthTokenCallback(f func() string) func(*AviSession) error {
	return func(sess *AviSession) error {
		return sess.setRefreshAuthTokenCallback(f)
	}
}

func (avisess *AviSession) setRefreshAuthTokenCallback(f func() string) error {
	avisess.refreshAuthToken = f
	return nil
}

// SetTenant - Use this for NewAviSession option argument for setting tenant
func SetTenant(tenant string) func(*AviSession) error {
	return func(sess *AviSession) error {
		return sess.setTenant(tenant)
	}
}

func (avisess *AviSession) setTenant(tenant string) error {
	avisess.tenant = tenant
	return nil
}

// SetInsecure - Use this for NewAviSession option argument for allowing insecure connection to AviController
func SetInsecure(avisess *AviSession) error {
	avisess.insecure = true
	return nil
}

func (avisess *AviSession) isTokenAuth() bool {
	return avisess.authToken != "" || avisess.refreshAuthToken != nil
}

//
// Helper routines for REST calls.
//

//Open given file as a pointer
func mustOpen(f string) *os.File {
	r, err := os.Open(f)
	if err != nil {
		log.Printf("[ERROR] mustOpen Error while opening  file %v", f)
		panic(err)
	}
	return r
}
// restRequest makes a REST request to the Avi Controller's REST API.
// Returns a byte[] if successful
func (avisess *AviSession) restRequest(verb string, uri string, payload interface{}, retryNum ...int) ([]byte, error) {

	var result []byte
	url := avisess.prefix + uri
	log.Printf("[INFO] restRequest url: %v", url)
	// If optional retryNum arg is provided, then count which retry number this is
	retry := 0
	if len(retryNum) > 0 {
		retry = retryNum[0]
	}

	// On subsequent retries, wait a bit before retrying.
	// If not our first 3 tries, stop trying
	if retry == 1 {
		time.Sleep(100 * time.Millisecond)
	}
	if retry == 2 {
		time.Sleep(500 * time.Millisecond)
	}
	if retry == 3 {
		time.Sleep(1 * time.Second)
	}
	if retry > 3 {
		errorResult := AviError{verb: verb, url: url}
		errorResult.err = fmt.Errorf("tried 3 times and failed")
		return nil, errorResult
	}

	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: avisess.insecure},
	}

	errorResult := AviError{verb: verb, url: url}

	var payloadIO io.Reader
	if payload != nil {
		jsonStr, err := json.Marshal(payload)
		if err != nil {
			return result, AviError{verb: verb, url: url, err: err}
		}
		payloadIO = bytes.NewBuffer(jsonStr)
	}

	req, err := http.NewRequest(verb, url, payloadIO)
	if err != nil {
		errorResult.err = fmt.Errorf("http.NewRequest failed: %v", err)
		return result, errorResult
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Accept", "application/json")
	req.Header.Set("X-Avi-Version", avisess.version)

	if avisess.csrfToken != "" {
		req.Header["X-CSRFToken"] = []string{avisess.csrfToken}
		req.AddCookie(&http.Cookie{Name: "csrftoken", Value: avisess.csrfToken})
	}
	if avisess.prefix != "" {
		req.Header.Set("Referer", avisess.prefix)
	}
	if avisess.tenant != "" {
		req.Header.Set("X-Avi-Tenant", avisess.tenant)
	}
	if avisess.sessionid != "" {
		req.AddCookie(&http.Cookie{Name: "sessionid", Value: avisess.sessionid})
	}

	// glog.Infof("Request headers: %v", req.Header)
	dump, err := httputil.DumpRequestOut(req, true)
	debug(dump, err)

	client := &http.Client{Transport: tr}

	resp, err := client.Do(req)
	if err != nil {
		errorResult.err = fmt.Errorf("client.Do failed: %v", err)
		return result, errorResult
	}

	defer resp.Body.Close()

	errorResult.httpStatusCode = resp.StatusCode

	// collect cookies from the resp
	for _, cookie := range resp.Cookies() {
		glog.Infof("cookie: %v", cookie)
		if cookie.Name == "csrftoken" {
			avisess.csrfToken = cookie.Value
			glog.Infof("Set the csrf token to %v", avisess.csrfToken)
		}
		if cookie.Name == "sessionid" {
			avisess.sessionid = cookie.Value
		}
	}
	glog.Infof("Response code: %v", resp.StatusCode)

	if resp.StatusCode == 419 {
		// session got reset; try again
		return avisess.restRequest(verb, uri, payload, retry+1)
	}

	// session expired; initiate session and then retry the request
	if resp.StatusCode == 401 && len(avisess.sessionid) != 0 && uri != "login" {
		err := avisess.initiateSession()
		if err != nil {
			return nil, err
		}
		return avisess.restRequest(verb, uri, payload, retry+1)
	}

	if resp.StatusCode < 200 || resp.StatusCode > 299 {
		glog.Errorf("Error: %v", resp)
		bres, berr := ioutil.ReadAll(resp.Body)
		if berr == nil {
			mres, _ := convertAviResponseToMapInterface(bres)
			glog.Infof("Error resp: %v", mres)
			emsg := fmt.Sprintf("%v", mres)
			errorResult.Message = &emsg
		}
		return result, errorResult
	}

	if resp.StatusCode == 204 {
		// no content in the response
		return result, nil
	}

	result, err = ioutil.ReadAll(resp.Body)

	if err != nil {
		errmsg := fmt.Sprintf("Response body read failed: %v", err)
		errorResult.Message = &errmsg
		return nil, errorResult
	}

	return result, nil
}

// restMultipartUploadRequest makes a REST request to the Avi Controller's REST API using POST to upload a file.
// Return status of multipart upload.
func (avisess *AviSession) restMultipartUploadRequest(verb string, uri string, file_path string, retryNum ...int) (error) {

	url := avisess.prefix + "/api/fileservice/" +uri
	log.Printf("[INFO] restMultipartUploadRequest restRequest url: %v", url)
	// If optional retryNum arg is provided, then count which retry number this is
	retry := 0
	if len(retryNum) > 0 {
		retry = retryNum[0]
	}
	// On subsequent retries, wait a bit before retrying.
	// If not our first 3 tries, stop trying
	if retry == 1 {
		time.Sleep(100 * time.Millisecond)
	}
	if retry == 2 {
		time.Sleep(500 * time.Millisecond)
	}
	if retry == 3 {
		time.Sleep(1 * time.Second)
	}
	if retry > 3 {
		errorResult := AviError{verb: verb, url: url}
		errorResult.err = fmt.Errorf("[ERROR] restMultipartDownloadRequest tried 3 times and failed")
		return errorResult
	}
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: avisess.insecure},
	}
	//
	errorResult := AviError{verb: verb, url: url}

	//Prepare a file that you will submit to an URL.
	values := map[string]io.Reader{
		"file":  mustOpen(file_path),
	}

	var b bytes.Buffer
	w := multipart.NewWriter(&b)
	for key, r := range values {
		var fw io.Writer
		var err error
		if x, ok := r.(io.Closer); ok {
			defer x.Close()
		}
		// Add an file
		if x, ok := r.(*os.File); ok {
			if fw, err = w.CreateFormFile(key, x.Name()); err != nil {
				if err != nil {
					log.Printf("[ERROR] restMultipartUploadRequest Error in adding file: %v ", err)
					return err
				}
			}
		}
		if _, err := io.Copy(fw, r); err != nil {
			if err != nil {
				log.Printf("[ERROR] restMultipartUploadRequest Error io.Copy %v ", err)
				return err
			}
		}

	}
	// Closing the multipart writer.
	// If you don't close it, your request will be missing the terminating boundary.
	w.Close()
	uri_temp := "controller://" + strings.Split(uri,"?")[0]
	err := w.WriteField("uri", uri_temp)

	if err != nil {
		errorResult.err = fmt.Errorf("[ERROR] restMultipartUploadRequest Adding URI field failed: %v", err)
		return errorResult
	}
	req, err := http.NewRequest(verb, url, &b)
	if err != nil {
		errorResult.err = fmt.Errorf("[ERROR] restMultipartUploadRequest http.NewRequest failed: %v", err)
		return errorResult
	}
	req.Header.Set("Content-Type", w.FormDataContentType())
	req.Header.Set("X-Avi-Version", avisess.version)
	//
	if avisess.csrfToken != "" {
		req.Header["X-CSRFToken"] = []string{avisess.csrfToken}
		req.AddCookie(&http.Cookie{Name: "csrftoken", Value: avisess.csrfToken})
	}
	if avisess.prefix != "" {
		req.Header.Set("Referer", avisess.prefix)
	}
	if avisess.tenant != "" {
		req.Header.Set("X-Avi-Tenant", avisess.tenant)
	}
	if avisess.sessionid != "" {
		req.AddCookie(&http.Cookie{Name: "sessionid", Value: avisess.sessionid})
		req.AddCookie(&http.Cookie{Name: "avi-sessionid", Value: avisess.sessionid})
	}

	client := &http.Client{Transport: tr}

	dump, err := httputil.DumpRequestOut(req, true)
	debug(dump, err)

	resp, err := client.Do(req)
	if err != nil {
		log.Printf("[ERROR] restMultipartUploadRequest Error during client request: %v ", err)
		return err
	}

	defer resp.Body.Close()

	errorResult.httpStatusCode = resp.StatusCode

	// collect cookies from the resp
	for _, cookie := range resp.Cookies() {
		glog.Infof("cookie: %v", cookie)
		if cookie.Name == "csrftoken" {
			avisess.csrfToken = cookie.Value
			glog.Infof("Set the csrf token to %v", avisess.csrfToken)
		}
		if cookie.Name == "sessionid" {
			avisess.sessionid = cookie.Value
		}
	}
	glog.Infof("Response code: %v", resp.StatusCode)

	if resp.StatusCode == 419 {
		// session got reset; try again
		return avisess.restMultipartUploadRequest(verb, uri, file_path, retry+1)
	}

	// session expired; initiate session and then retry the request
	if resp.StatusCode == 401 && len(avisess.sessionid) != 0 && uri != "login" {
		err := avisess.initiateSession()
		if err != nil {
			return err
		}
		return avisess.restMultipartUploadRequest(verb, uri, file_path, retry+1)
	}

	if resp.StatusCode < 200 || resp.StatusCode > 299 {
		glog.Errorf("Error: %v", resp)
		bres, berr := ioutil.ReadAll(resp.Body)
		if berr == nil {
			mres, _ := convertAviResponseToMapInterface(bres)
			glog.Infof("Error resp: %v", mres)
			emsg := fmt.Sprintf("%v", mres)
			errorResult.Message = &emsg
		}
		return errorResult
	}

	if resp.StatusCode == 201 {
		// File Created and upload to server
		fmt.Printf("[INFO] restMultipartUploadRequest Response: %v", resp.Status)
		return nil
	}

	return err
}

// restRequest makes a REST request to the Avi Controller's REST API.
// Returns multipart download and write data to file
func (avisess *AviSession) restMultipartDownloadRequest(verb string, uri string, file_path string, retryNum ...int) (error) {

	url := avisess.prefix + "/api/fileservice/" + uri
	log.Printf("[INFO] restMultipartDownloadRequest url: %v", url)
	// If optional retryNum arg is provided, then count which retry number this is
	retry := 0
	if len(retryNum) > 0 {
		retry = retryNum[0]
	}

	// On subsequent retries, wait a bit before retrying.
	// If not our first 3 tries, stop trying
	if retry == 1 {
		time.Sleep(100 * time.Millisecond)
	}
	if retry == 2 {
		time.Sleep(500 * time.Millisecond)
	}
	if retry == 3 {
		time.Sleep(1 * time.Second)
	}
	if retry > 3 {
		errorResult := AviError{verb: verb, url: url}
		errorResult.err = fmt.Errorf("[ERROR] restMultipartDownloadRequest tried 3 times and failed")
		return errorResult
	}

	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: avisess.insecure},
	}

	errorResult := AviError{verb: verb, url: url}

	req, err := http.NewRequest(verb, url, nil)
	if err != nil {
		errorResult.err = fmt.Errorf("[ERROR] restMultipartDownloadRequest http.NewRequest failed: %v", err)
		return errorResult
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Accept", "application/json")
	req.Header.Set("X-Avi-Version", avisess.version)

	if avisess.csrfToken != "" {
		req.Header["X-CSRFToken"] = []string{avisess.csrfToken}
		req.AddCookie(&http.Cookie{Name: "csrftoken", Value: avisess.csrfToken})
	}
	if avisess.prefix != "" {
		req.Header.Set("Referer", avisess.prefix)
	}
	if avisess.tenant != "" {
		req.Header.Set("X-Avi-Tenant", avisess.tenant)
	}
	if avisess.sessionid != "" {
		req.AddCookie(&http.Cookie{Name: "sessionid", Value: avisess.sessionid})
	}

	// glog.Infof("Request headers: %v", req.Header)
	dump, err := httputil.DumpRequestOut(req, true)
	debug(dump, err)

	client := &http.Client{Transport: tr}

	resp, err := client.Do(req)
	if err != nil {
		errorResult.err = fmt.Errorf("client.Do failed: %v", err)
		return errorResult
	}

	defer resp.Body.Close()

	errorResult.httpStatusCode = resp.StatusCode

	// collect cookies from the resp
	for _, cookie := range resp.Cookies() {
		glog.Infof("cookie: %v", cookie)
		if cookie.Name == "csrftoken" {
			avisess.csrfToken = cookie.Value
			glog.Infof("Set the csrf token to %v", avisess.csrfToken)
		}
		if cookie.Name == "sessionid" {
			avisess.sessionid = cookie.Value
		}
	}
	glog.Infof("Response code: %v", resp.StatusCode)

	if resp.StatusCode == 419 {
		// session got reset; try again
		return avisess.restMultipartDownloadRequest(verb, uri, file_path, retry+1)
	}

	// session expired; initiate session and then retry the request
	if resp.StatusCode == 401 && len(avisess.sessionid) != 0 && uri != "login" {
		err := avisess.initiateSession()
		if err != nil {
			return err
		}
		return avisess.restMultipartDownloadRequest(verb, uri, file_path, retry+1)
	}

	if resp.StatusCode < 200 || resp.StatusCode > 299 {
		glog.Errorf("Error: %v", resp)
		bres, berr := ioutil.ReadAll(resp.Body)
		if berr == nil {
			mres, _ := convertAviResponseToMapInterface(bres)
			glog.Infof("Error resp: %v", mres)
			emsg := fmt.Sprintf("%v", mres)
			errorResult.Message = &emsg
		}
		return errorResult
	}

	if resp.StatusCode == 204 {
		// no content in the response
		return nil
	}

	//File creation
	out, err := os.Create(file_path)
	if err != nil {
		log.Printf("[ERROR] Error for creation of file %v", file_path)
	}

	_, err = io.Copy(out, resp.Body)

	defer out.Close()
	defer resp.Body.Close()

	if err != nil {
		log.Printf("[ERROR] Error while downloading %v", err)
	}
	//Set 1KB as buffer size
	//buff := make([]byte, 1024)
	//n, err := io.CopyBuffer(out, resp.Body, buff)
	//fmt.Printf("%v", n)
	return err
}

func convertAviResponseToMapInterface(resbytes []byte) (interface{}, error) {
	var result interface{}
	err := json.Unmarshal(resbytes, &result)
	return result, err
}

// AviCollectionResult for representing the collection type results from Avi
type AviCollectionResult struct {
	Count   int
	Results json.RawMessage
}

func debug(data []byte, err error) {
	if err == nil {
		glog.Infof("%s\n\n", data)
	} else {
		glog.Fatalf("%s\n\n", err)
	}
}

func (avisess *AviSession) restRequestInterfaceResponse(verb string, url string,
	payload interface{}, response interface{}) error {
	res, rerror := avisess.restRequest(verb, url, payload)
	if rerror != nil || res == nil {
		return rerror
	}
	return json.Unmarshal(res, &response)
}

// Get issues a GET request against the avisess REST API.
func (avisess *AviSession) Get(uri string, response interface{}) error {
	return avisess.restRequestInterfaceResponse("GET", uri, nil, response)
}

// Post issues a POST request against the avisess REST API.
func (avisess *AviSession) Post(uri string, payload interface{}, response interface{}) error {
	return avisess.restRequestInterfaceResponse("POST", uri, payload, response)
}

// Put issues a PUT request against the avisess REST API.
func (avisess *AviSession) Put(uri string, payload interface{}, response interface{}) error {
	return avisess.restRequestInterfaceResponse("PUT", uri, payload, response)
}

// Delete issues a DELETE request against the avisess REST API.
func (avisess *AviSession) Delete(uri string) error {
	return avisess.restRequestInterfaceResponse("DELETE", uri, nil, nil)
}

// GetCollectionRaw issues a GET request and returns a AviCollectionResult with unmarshaled (raw) results section.
func (avisess *AviSession) GetCollectionRaw(uri string) (AviCollectionResult, error) {
	var result AviCollectionResult
	res, rerror := avisess.restRequest("GET", uri, nil)
	if rerror != nil || res == nil {
		return result, rerror
	}
	err := json.Unmarshal(res, &result)
	return result, err
}

// GetCollection performs a collection API call and unmarshals the results into objList, which should be an array type
func (avisess *AviSession) GetCollection(uri string, objList interface{}) error {
	result, err := avisess.GetCollectionRaw(uri)
	if err != nil {
		return err
	}
	if result.Count == 0 {
		return nil
	}
	return json.Unmarshal(result.Results, &objList)
}

// GetRaw performs a GET API call and returns raw data
func (avisess *AviSession) GetRaw(uri string) ([]byte, error) {
	return avisess.restRequest("GET", uri, nil)
}

// PostRaw performs a POST API call and returns raw data
func (avisess *AviSession) PostRaw(uri string, payload interface{}) ([]byte, error) {
	return avisess.restRequest("POST", uri, payload)
}

// GetMultipartRaw performs a GET API call and returns multipart raw data (File Download)
func (avisess *AviSession) GetMultipartRaw(verv string, uri string, file_loc string) (error) {
	return avisess.restMultipartDownloadRequest("GET", uri, file_loc)
}

// PostMultipartRequest performs a POST API call and uploads multipart data
func (avisess *AviSession) PostMultipartRequest(verb string, uri string, file_loc string) (error) {
	return avisess.restMultipartUploadRequest("POST", uri, file_loc)
}

type ApiOptions struct {
	name        string
	cloud       string
	cloudUUID   string
	skipDefault bool
	includeName bool
	result      interface{}
}

func SetName(name string) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setName(name)
	}
}

func (opts *ApiOptions) setName(name string) error {
	opts.name = name
	return nil
}

func SetCloud(cloud string) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setCloud(cloud)
	}
}

func (opts *ApiOptions) setCloud(cloud string) error {
	opts.cloud = cloud
	return nil
}

func SetCloudUUID(cloudUUID string) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setCloudUUID(cloudUUID)
	}
}

func (opts *ApiOptions) setCloudUUID(cloudUUID string) error {
	opts.cloudUUID = cloudUUID
	return nil
}

func SetSkipDefault(skipDefault bool) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setSkipDefault(skipDefault)
	}
}

func (opts *ApiOptions) setSkipDefault(skipDefault bool) error {
	opts.skipDefault = skipDefault
	return nil
}

func SetIncludeName(includeName bool) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setIncludeName(includeName)
	}
}

func (opts *ApiOptions) setIncludeName(includeName bool) error {
	opts.includeName = includeName
	return nil
}

func SetResult(result interface{}) func(*ApiOptions) error {
	return func(opts *ApiOptions) error {
		return opts.setResult(result)
	}
}

func (opts *ApiOptions) setResult(result interface{}) error {
	opts.result = result
	return nil
}

type ApiOptionsParams func(*ApiOptions) error

func (avisess *AviSession) GetObject(obj string, options ...ApiOptionsParams) error {
	opts := &ApiOptions{}
	for _, opt := range options {
		err := opt(opts)
		if err != nil {
			return err
		}
	}
	if opts.result == nil {
		return errors.New("reference to result provided")
	}

	if opts.name == "" {
		return errors.New("Name not specified")
	}

	uri := "api/" + obj + "?name=" + opts.name
	if opts.cloud != "" {
		uri = uri + "&cloud=" + opts.cloud
	} else if opts.cloudUUID != "" {
		uri = uri + "&cloud_ref.uuid=" + opts.cloudUUID
	}
	if opts.skipDefault {
		uri = uri + "&skip_default=true"
	}
	if opts.includeName {
		uri = uri + "&include_name=true"
	}
	res, err := avisess.GetCollectionRaw(uri)
	if err != nil {
		return err
	}
	if res.Count == 0 {
		return errors.New("No object of type " + obj + " with name " + opts.name + "is found")
	} else if res.Count > 1 {
		return errors.New("More than one object of type " + obj + " with name " + opts.name + "is found")
	}
	elems := make([]json.RawMessage, 1)
	err = json.Unmarshal(res.Results, &elems)
	if err != nil {
		return err
	}
	return json.Unmarshal(elems[0], &opts.result)

}

// GetObjectByName performs GET with name filter
func (avisess *AviSession) GetObjectByName(obj string, name string, result interface{}) error {
	return avisess.GetObject(obj, SetName(name), SetResult(result))
}

// Utility functions

// GetControllerVersion gets the version number from the Avi Controller
func (avisess *AviSession) GetControllerVersion() (string, error) {
	var resp interface{}

	err := avisess.Get("/api/initial-data", &resp)
	if err != nil {
		return "", err
	}
	version := resp.(map[string]interface{})["version"].(map[string]interface{})["Version"].(string)
	return version, nil
}
