/*
 * Avi avi_global_spec Object API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 20.1.1
 * Contact: support@avinetworks.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.vmware.avi.sdk.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * ErrorPageBody
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class ErrorPageBody {
  @JsonProperty("_last_modified")
  private String _lastModified = null;

  @JsonProperty("error_page_body")
  private String errorPageBody = null;

  @JsonProperty("format")
  private String format = "ERROR_PAGE_FORMAT_HTML";

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("tenant_ref")
  private String tenantRef = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("uuid")
  private String uuid = null;

   /**
   * UNIX time since epoch in microseconds. Units(MICROSECONDS).
   * @return _lastModified
  **/
  @Schema(description = "UNIX time since epoch in microseconds. Units(MICROSECONDS).")
  public String getLastModified() {
    return _lastModified;
  }

  public ErrorPageBody errorPageBody(String errorPageBody) {
    this.errorPageBody = errorPageBody;
    return this;
  }

   /**
   * Error page body sent to client when match. Field introduced in 17.2.4.
   * @return errorPageBody
  **/
  @Schema(description = "Error page body sent to client when match. Field introduced in 17.2.4.")
  public String getErrorPageBody() {
    return errorPageBody;
  }

  public void setErrorPageBody(String errorPageBody) {
    this.errorPageBody = errorPageBody;
  }

  public ErrorPageBody format(String format) {
    this.format = format;
    return this;
  }

   /**
   * Format of an error page body HTML or JSON. Enum options - ERROR_PAGE_FORMAT_HTML, ERROR_PAGE_FORMAT_JSON. Field introduced in 18.2.3.
   * @return format
  **/
  @Schema(description = "Format of an error page body HTML or JSON. Enum options - ERROR_PAGE_FORMAT_HTML, ERROR_PAGE_FORMAT_JSON. Field introduced in 18.2.3.")
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public ErrorPageBody name(String name) {
    this.name = name;
    return this;
  }

   /**
   *  Field introduced in 17.2.4.
   * @return name
  **/
  @Schema(required = true, description = " Field introduced in 17.2.4.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ErrorPageBody tenantRef(String tenantRef) {
    this.tenantRef = tenantRef;
    return this;
  }

   /**
   *  It is a reference to an object of type Tenant. Field introduced in 17.2.4.
   * @return tenantRef
  **/
  @Schema(description = " It is a reference to an object of type Tenant. Field introduced in 17.2.4.")
  public String getTenantRef() {
    return tenantRef;
  }

  public void setTenantRef(String tenantRef) {
    this.tenantRef = tenantRef;
  }

   /**
   * url
   * @return url
  **/
  @Schema(description = "url")
  public String getUrl() {
    return url;
  }

  public ErrorPageBody uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   *  Field introduced in 17.2.4.
   * @return uuid
  **/
  @Schema(description = " Field introduced in 17.2.4.")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorPageBody errorPageBody = (ErrorPageBody) o;
    return Objects.equals(this._lastModified, errorPageBody._lastModified) &&
        Objects.equals(this.errorPageBody, errorPageBody.errorPageBody) &&
        Objects.equals(this.format, errorPageBody.format) &&
        Objects.equals(this.name, errorPageBody.name) &&
        Objects.equals(this.tenantRef, errorPageBody.tenantRef) &&
        Objects.equals(this.url, errorPageBody.url) &&
        Objects.equals(this.uuid, errorPageBody.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_lastModified, errorPageBody, format, name, tenantRef, url, uuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorPageBody {\n");
    
    sb.append("    _lastModified: ").append(toIndentedString(_lastModified)).append("\n");
    sb.append("    errorPageBody: ").append(toIndentedString(errorPageBody)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tenantRef: ").append(toIndentedString(tenantRef)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
