package com.vmware.avi.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class creates a session with controller and facilitates CRUD operations.
 * 
 * @author: Chaitanya Deshpande
 *
 */
public class AviApi {

	/**
	 * Sets the logger for get all logs.
	 */
	static final Logger LOGGER = Logger.getLogger(AviApi.class.getName());

	/**
	 * Constructor for AviApi Class.
	 * 
	 * @param aviCredentials an AviCredentials Object containing controller's
	 *                       details to create session.
	 */
	public AviApi(AviCredentials aviCredentials) {
		this.aviCredentials = aviCredentials;
		this.sessionKey = aviCredentials.getController() + ":" + aviCredentials.getUsername() + ":"
				+ aviCredentials.getPort();
	}

	/**
	 * The Session pool containing session objects in runtime to avoid duplicates.
	 */
	private static ConcurrentHashMap<String, AviApi> sessionPool = new ConcurrentHashMap<String, AviApi>();
	/**
	 * Maintains count of execution at the time of retries.
	 */
	int numApiExecCount = 0;
	/**
	 * AviCredentials object for this session.
	 */
	private AviCredentials aviCredentials;
	/**
	 * The session key of this session.
	 */
	private String sessionKey;

	/**
	 * This static factory method to create session if not present in the pool if
	 * presents in the session pool returns existing session.
	 * 
	 * @param aviCredentials Containing the credentials of the controller.
	 * @return A session representing the session for the controller.
	 * @throws Exception
	 */
	public static AviApi getSession(AviCredentials aviCredentials) throws Exception {
		String sessionKey = aviCredentials.getController() + ":" + aviCredentials.getUsername() + ":"
				+ aviCredentials.getPort();
		synchronized (AviApi.class) {
			if (sessionPool.containsKey(sessionKey)) {
				LOGGER.info("Using existing session");
				return sessionPool.get(sessionKey);
			} else {
				AviApi session = new AviApi(aviCredentials);
				if (!aviCredentials.getLazyAuthentication()) {
					LOGGER.info("Using new session");
					session.authenticateSession();
				}
				sessionPool.put(session.sessionKey, session);
				return session;
			}
		}
	}

	/**
	 * This method returns the controller URL based on controller IP and controller
	 * port.
	 * 
	 * @return A String representing the controller URL.
	 */
	private String getControllerURL() {
		if (this.aviCredentials.getController().startsWith("http")) {
			if (Arrays.asList(80, 443).contains(this.aviCredentials.getPort())) {
				return this.aviCredentials.getController();
			} else {
				return this.aviCredentials.getController() + ":" + this.aviCredentials.getPort();
			}
		} else {
			if (this.aviCredentials.getPort() == 443) {
				return "https://" + this.aviCredentials.getController();
			} else if (this.aviCredentials.getPort() == 80) {
				return "http://" + this.aviCredentials.getController();
			} else {
				return "https://" + this.aviCredentials.getController() + ":" + this.aviCredentials.getPort();
			}
		}
	}

	/**
	 * This method authenticates user based on the credentials and update the
	 * csrftoken and session id for this session.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void authenticateSession() throws Exception {
		JSONObject body = new JSONObject();
		body.put("username", this.aviCredentials.getUsername());
		if (this.aviCredentials.getPassword() != "" || this.aviCredentials.getPassword() != null) {
			body.put("password", this.aviCredentials.getPassword());
		} else if (this.aviCredentials.getToken() != "" || this.aviCredentials.getToken() != null) {
			body.put("token", this.aviCredentials.getToken());
		}
		LOGGER.info("Authentication session for " + this.aviCredentials.getUsername());
		CloseableHttpClient httpClient = this.buildHttpClient();
		try {
			String postUrl = this.getControllerURL() + "/login";

			HttpPost postRequest = new HttpPost(postUrl);
			StringEntity input = new StringEntity(body.toString());
			input.setContentType("application/json");
			postRequest.addHeader("X-Avi-Version", this.aviCredentials.getVersion());
			postRequest.addHeader("X-Avi-Tenant", this.aviCredentials.getTenant());
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode > 299) {
				LOGGER.severe("Login faild with status code " + statusCode);
				throw new IOException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			String output = EntityUtils.toString(response.getEntity());
			JSONParser parser = new JSONParser();
			JSONObject result = (JSONObject) parser.parse(output);
			String sessionCookieName = result.get("session_cookie_name").toString();
			String csrftoken = null;
			String sessionCookie = null;
			for (Header header : response.getHeaders("Set-Cookie")) {
				List<HttpCookie> httpCookies = HttpCookie.parse(header.getValue());
				for (HttpCookie cookie : httpCookies) {
					if (cookie.getName().equals("csrftoken")) {
						csrftoken = cookie.getValue();
					} else if (cookie.getName().equals(sessionCookieName)) {
						sessionCookie = cookie.getValue();
					}
				}
			}
			this.aviCredentials.setCsrftoken(csrftoken);
			this.aviCredentials.setSessionID(sessionCookie);
			synchronized (AviApi.class) {
				AviApi.sessionPool.put(this.sessionKey, this);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method calls the GET REST API.
	 * 
	 * @param path   A String containing the objectType to be get from controller.
	 * @param params A Map which can contain URL params if any.
	 * 
	 * @return A JSONObeject representing response of the GET REST call.
	 * @throws Exception
	 */
	public JSONObject get(String path, Map<String, String> params) throws Exception {
		return this.get(path, params, null);
	}

	/**
	 * This method calls the GET REST API.
	 * 
	 * @param path   A String containing the objectType to be get from controller.
	 * @param params A Map which can contain URL params if any.
	 * @propertyMap propertyMap A map hich can contains extra properties that user
	 *              want to set in header
	 * 
	 * @return A JSONObeject representing response of the GET REST call.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject get(String path, Map<String, String> params, HashMap<String, String> propertyMap)
			throws Exception {
		CloseableHttpClient httpClient = null;
		try {
			String getUrl = this.getControllerURL() + "/api/" + path;
			URIBuilder uriBuilder = new URIBuilder(getUrl);

			if (null != params && !params.isEmpty()) {
				List<NameValuePair> urlParameters = new ArrayList<>();
				for (String key : params.keySet()) {
					urlParameters.add(new BasicNameValuePair(key, params.get(key)));
				}
				uriBuilder.addParameters(urlParameters);
			}

			httpClient = this.buildHttpClient();
			HttpGet request = new HttpGet(uriBuilder.build());
			this.buildHeaders(request, propertyMap);
			HttpResponse response = httpClient.execute(request);

			Object[] args = new Object[] { path, params };
			Class[] mParams = new Class[] { String.class, Map.class };
			Method m = AviApi.class.getMethod("get", mParams);
			JSONObject result = this.parseResponse(response, m, args);
			return result;

		} catch (IOException | URISyntaxException | NoSuchMethodException | SecurityException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in GET : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * This method calls the PUT REST API.
	 * 
	 * @param path A String containing object type which want to PUT on the
	 *             controller.
	 * @param body A JSONObject containing object details.
	 * 
	 * @return A JSONObject representing response of the PUT REST call.
	 * 
	 * @throws AviApiException if there any error in executing PUT request
	 */
	public JSONObject put(String path, JSONObject body) throws AviApiException {
		return this.put(path, body, null);
	}

	/**
	 * This method calls the PUT REST API.
	 * 
	 * @param path A String containing object type which want to PUT on the
	 *             controller.
	 * @param body A JSONObject containing object details.
	 * @propertyMap propertyMap A map hich can contains extra properties that user
	 *              want to set in header
	 * 
	 * @return A JSONObject representing response of the PUT REST call.
	 * 
	 * @throws AviApiException if there any error in executing PUT request
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject put(String path, JSONObject body, HashMap<String, String> propertyMap) throws AviApiException {
		CloseableHttpClient httpClient = null;
		try {
			httpClient = this.buildHttpClient();
			String putUrl = this.getControllerURL() + "/api/" + path + "/" + body.get("uuid");
			HttpPut request = new HttpPut(putUrl);
			StringEntity input = new StringEntity(body.toString());
			request.setEntity(input);
			this.buildHeaders(request, propertyMap);
			HttpResponse response = httpClient.execute(request);

			Object[] args = new Object[] { path, body };
			Class[] mParams = new Class[] { String.class, JSONObject.class };
			Method m = AviApi.class.getMethod("put", mParams);
			JSONObject result = this.parseResponse(response, m, args);
			return result;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in PUT : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * This method calls the POST REST API
	 * 
	 * @param path A String containing object type which want to create on the
	 *             controller.
	 * @param body A JSONObject containing the object body.
	 * 
	 * @return A JSONObject representing the created response of the POST REST call.
	 * 
	 * @throws AviApiException if there any error in executing POST request.
	 */
	public JSONObject post(String path, JSONObject body) throws AviApiException {
		return this.post(path, body, null);
	}

	/**
	 * This method calls the POST REST API
	 * 
	 * @param path A String containing object type which want to create on the
	 *             controller.
	 * @param body A JSONObject containing the object body.
	 * @propertyMap propertyMap A map hich can contains extra properties that user
	 *              want to set in header
	 * 
	 * @return A JSONObject representing the created response of the POST REST call.
	 * 
	 * @throws AviApiException if there any error in executing POST request.
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject post(String path, JSONObject body, HashMap<String, String> propertyMap) throws AviApiException {
		CloseableHttpClient httpClient = null;
		try {
			httpClient = this.buildHttpClient();
			String postUrl = this.getControllerURL() + "/api/" + path;
			HttpPost request = new HttpPost(postUrl);
			StringEntity input = new StringEntity(body.toString());
			request.setEntity(input);
			this.buildHeaders(request, propertyMap);
			HttpResponse response = httpClient.execute(request);

			Object[] args = new Object[] { path, body };
			Class[] mParams = new Class[] { String.class, JSONObject.class };
			Method m = AviApi.class.getMethod("post", mParams);
			JSONObject result = this.parseResponse(response, m, args);
			return result;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in POST : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * This method calls the DELETE REST API.
	 * 
	 * @param path A String containing object type which want to delete from the
	 *             controller.
	 * @param uuid A String containing id of the object which want to DELETE.
	 * 
	 * @return A JSONObject representing response of the DELETE REST call.
	 * 
	 * @throws AviApiException if any error executing DELETE request.
	 */
	public JSONObject delete(String path, String uuid) throws AviApiException {
		return this.delete(path, uuid, null);
	}

	/**
	 * This method calls the DELETE REST API.
	 * 
	 * @param path A String containing object type which want to delete from the
	 *             controller.
	 * @param uuid A String containing id of the object which want to DELETE.
	 * @propertyMap propertyMap A map hich can contains extra properties that user
	 *              want to set in header
	 * 
	 * @return A JSONObject representing response of the DELETE REST call.
	 * 
	 * @throws AviApiException if any error executing DELETE request.
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject delete(String path, String uuid, HashMap<String, String> propertyMap) throws AviApiException {
		CloseableHttpClient httpClient = null;
		try {
			String deleteUrl = this.getControllerURL() + "/api/" + path + "/" + uuid;
			httpClient = this.buildHttpClient();
			HttpDelete request = new HttpDelete(deleteUrl);
			this.buildHeaders(request, propertyMap);
			HttpResponse response = httpClient.execute(request);
			Object[] args = new Object[] { path, uuid };
			Class[] mParams = new Class[] { String.class, String.class };
			Method m = AviApi.class.getMethod("delete", mParams);
			JSONObject result = this.parseResponse(response, m, args);
			return result;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in DELETE : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 
	 * @param uri           is the api which upload a file
	 * @param filePath      is file which we want to upload
	 * @param fileUploadUri is uri where we have to upload file
	 * @throws Exception
	 */
	public void postFileUpload(String uri, String filePath, String fileUploadUri) throws Exception {
		CloseableHttpClient httpClient = null;
		try {
			httpClient = this.buildHttpClient();
			String postUrl = this.getControllerURL() + "/api/" + uri;
			HttpPost request = new HttpPost(postUrl);
			this.buildHeaders(request, null);
			request.removeHeaders("Content-Type");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("uri", fileUploadUri, ContentType.TEXT_PLAIN);

			// This attaches the file to the POST:
			File f = new File(filePath);
			builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());
			HttpEntity multipart = builder.build();
			request.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode > 299) {
				StringBuffer errMessage = new StringBuffer();
				errMessage.append("Failed : HTTP error code : ");
				errMessage.append(responseCode);
				if (null != response.getEntity()) {
					errMessage.append(" Error Message :");
					errMessage.append(EntityUtils.toString(response.getEntity()));
				}
				throw new AviApiException(errMessage.toString());
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in postFileUpload : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 
	 * @param exportType is a export type
	 * @param passphrase is a key that used for gain the access
	 * @return JSONObeject representing response.
	 * @throws AviApiException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public JSONObject exportConfiguration(String exportType, String passphrase) throws AviApiException, IOException {
		CloseableHttpClient httpClient = null;
		HttpResponse response = null;
		JSONObject jsonObject = null;
		try {
			httpClient = this.buildHttpClient();
			String path = "/configuration/export";
			JSONObject json_data = new JSONObject();
			json_data.put("passphrase", passphrase);
			if (("full".equals(exportType)) || ("passphrase-full".equals(exportType))) {
				path = path + "?full_system=true";
			}
			String postUrl = this.getControllerURL() + "/api" + path;
			HttpPost request = new HttpPost(postUrl);
			StringEntity json_input = new StringEntity(json_data.toString());
			request.setEntity(json_input);
			this.buildHeaders(request, null);
			response = httpClient.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode > 299) {
				StringBuffer errMessage = new StringBuffer();
				errMessage.append("Failed : HTTP error code : ");
				errMessage.append(responseCode);
				if (null != response.getEntity()) {
					errMessage.append(" Error Message :");
					errMessage.append(EntityUtils.toString(response.getEntity()));
				}
				throw new AviApiException(errMessage.toString());
			}
			if (null != response.getEntity()) {
				String output = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				jsonObject = (JSONObject) parser.parse(output);
			}
			return jsonObject;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in POST : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {

			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}

		}

	}

	/**
	 * 
	 * @param exportType is a export type
	 * @param passphrase is a key that used for gain the access
	 * @return name of the file.
	 * @throws AviApiException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String exportConfigurationFile(String exportType, String passphrase) throws AviApiException, IOException {

		FileWriter fileWriter = null;
		File tempFile = null;
		try {
			JSONObject result = this.exportConfiguration(exportType, passphrase);
			tempFile = File.createTempFile("temp", ".json");
			fileWriter = new FileWriter(tempFile);
			fileWriter.write(result.toJSONString());
			LOGGER.info("Path of file :" + tempFile.getAbsolutePath());
			return tempFile.getName();
		} catch (

		Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in POST : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		} finally {
			fileWriter.close();
		}

	}

	/**
	 * This method gets the response and convert it into JSONObject.
	 * 
	 * @param response A HttpResponse from the REST call.
	 * @param m        The method type.
	 * @param args     Object which have headers and post body into it.
	 * 
	 * @return A JSONObject representing converted response of the REST call.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private JSONObject parseResponse(HttpResponse response, Method m, Object[] args) throws Exception {
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			if (Arrays.asList(419, 401).contains(responseCode)) {
				this.numApiExecCount++;
				if (numApiExecCount < this.aviCredentials.getNumApiRetries()) {
					this.authenticateSession();
					JSONObject result = (JSONObject) m.invoke(this, args);
					this.numApiExecCount = 0;
					return result;
				}
			} else if (responseCode > 299) {
				StringBuffer errMessage = new StringBuffer();
				errMessage.append("Failed : HTTP error code : ");
				errMessage.append(responseCode);
				if (null != response.getEntity()) {
					errMessage.append(" Error Message :");
					errMessage.append(EntityUtils.toString(response.getEntity()));
				}
				throw new AviApiException(errMessage.toString());
			}
			if (null != response.getEntity()) {
				String output = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject) parser.parse(output);
				return result;
			} else {
				if (m.getName().equals("delete")) {
					JSONObject result = new JSONObject();
					result.put("Message", "Object deleted successfully");
					return result;
				} else {
					return null;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ParseException
				| IOException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.severe("Exception in parsing response : " + e.getMessage() + sw.toString());
			throw new AviApiException(e);
		}
	}

	/**
	 * This method sets a custom HttpRequestRetryHandler in order to enable a custom
	 * exception recovery mechanism.
	 * 
	 * @return A HttpRequestRetryHandler representing handling of the retryHandler.
	 */
	private HttpRequestRetryHandler retryHandler() {
		return (exception, executionCount, context) -> {

			if (executionCount >= this.aviCredentials.getNumApiRetries()) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				// Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) {
				// Unknown host
				return false;
			}
			if (exception instanceof SSLException) {
				// SSL handshake exception
				return false;
			}
			if (exception instanceof HttpHostConnectException) {
				return true;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		};
	}

	/**
	 * This method sets all HTTP request headers.
	 * 
	 * @param request A HttpRequestBase containing all require headers.
	 * @throws Exception
	 */
	private void buildHeaders(HttpRequestBase request, HashMap<String, String> propertyMap) throws Exception {
		if (null == this.aviCredentials.getSessionID() || this.aviCredentials.getSessionID().isEmpty()) {
			this.authenticateSession();
		}
		request.addHeader("Content-Type", "application/json");
		request.addHeader("X-Avi-Version", this.aviCredentials.getVersion());
		request.addHeader("X-Avi-Tenant", this.aviCredentials.getTenant());
		request.addHeader("X-CSRFToken", this.aviCredentials.getCsrftoken());
		request.addHeader("Referer", this.getControllerURL());

		request.addHeader("Cookie", "csrftoken=" + this.aviCredentials.getCsrftoken() + "; " + "avi-sessionid="
				+ this.aviCredentials.getSessionID());

		if ((null != propertyMap) && (!propertyMap.isEmpty())) {
			for (String key : propertyMap.keySet()) {
				request.addHeader(key, propertyMap.get(key));
			}
		}

	}

	/**
	 * This method build custom CloseableHttpClient with SSL socket and
	 * retryHandler.
	 * 
	 * @return The CloseableHttpClient representing HttpClient.
	 */
	private CloseableHttpClient buildHttpClient() {
		CloseableHttpClient httpClient = null;
		if (!this.aviCredentials.getVerify()) {
			SSLContext sslcontext = null;
			try {
				sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
			} catch (Exception e) {
				e.printStackTrace();
			}

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext,
					(s, sslSession) -> true);

			httpClient = HttpClients.custom().setRetryHandler(this.retryHandler())
					.setSSLSocketFactory(sslConnectionSocketFactory)
					.setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy(
							this.aviCredentials.getNumApiRetries(), this.aviCredentials.getRetryWaitTime()))
					.build();
		} else {
			httpClient = HttpClients.custom().setRetryHandler(this.retryHandler())
					.setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy(
							this.aviCredentials.getNumApiRetries(), this.aviCredentials.getRetryWaitTime()))
					.build();
		}
		return httpClient;
	}
}