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
 * HealthMonitorAuthInfo
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class HealthMonitorAuthInfo {
  @JsonProperty("password")
  private String password = null;

  @JsonProperty("username")
  private String username = null;

  public HealthMonitorAuthInfo password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Password for server authentication. Field introduced in 20.1.1.
   * @return password
  **/
  @Schema(required = true, description = "Password for server authentication. Field introduced in 20.1.1.")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public HealthMonitorAuthInfo username(String username) {
    this.username = username;
    return this;
  }

   /**
   * Username for server authentication. Field introduced in 20.1.1.
   * @return username
  **/
  @Schema(required = true, description = "Username for server authentication. Field introduced in 20.1.1.")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HealthMonitorAuthInfo healthMonitorAuthInfo = (HealthMonitorAuthInfo) o;
    return Objects.equals(this.password, healthMonitorAuthInfo.password) &&
        Objects.equals(this.username, healthMonitorAuthInfo.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, username);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HealthMonitorAuthInfo {\n");
    
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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
