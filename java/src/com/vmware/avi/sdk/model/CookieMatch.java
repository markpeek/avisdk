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
 * CookieMatch
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class CookieMatch {
  @JsonProperty("match_case")
  private String matchCase = "INSENSITIVE";

  @JsonProperty("match_criteria")
  private String matchCriteria = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("value")
  private String value = null;

  public CookieMatch matchCase(String matchCase) {
    this.matchCase = matchCase;
    return this;
  }

   /**
   * Case sensitivity to use for the match. Enum options - SENSITIVE, INSENSITIVE.
   * @return matchCase
  **/
  @Schema(description = "Case sensitivity to use for the match. Enum options - SENSITIVE, INSENSITIVE.")
  public String getMatchCase() {
    return matchCase;
  }

  public void setMatchCase(String matchCase) {
    this.matchCase = matchCase;
  }

  public CookieMatch matchCriteria(String matchCriteria) {
    this.matchCriteria = matchCriteria;
    return this;
  }

   /**
   * Criterion to use for matching the cookie in the HTTP request. Enum options - HDR_EXISTS, HDR_DOES_NOT_EXIST, HDR_BEGINS_WITH, HDR_DOES_NOT_BEGIN_WITH, HDR_CONTAINS, HDR_DOES_NOT_CONTAIN, HDR_ENDS_WITH, HDR_DOES_NOT_END_WITH, HDR_EQUALS, HDR_DOES_NOT_EQUAL.
   * @return matchCriteria
  **/
  @Schema(required = true, description = "Criterion to use for matching the cookie in the HTTP request. Enum options - HDR_EXISTS, HDR_DOES_NOT_EXIST, HDR_BEGINS_WITH, HDR_DOES_NOT_BEGIN_WITH, HDR_CONTAINS, HDR_DOES_NOT_CONTAIN, HDR_ENDS_WITH, HDR_DOES_NOT_END_WITH, HDR_EQUALS, HDR_DOES_NOT_EQUAL.")
  public String getMatchCriteria() {
    return matchCriteria;
  }

  public void setMatchCriteria(String matchCriteria) {
    this.matchCriteria = matchCriteria;
  }

  public CookieMatch name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the cookie.
   * @return name
  **/
  @Schema(required = true, description = "Name of the cookie.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CookieMatch value(String value) {
    this.value = value;
    return this;
  }

   /**
   * String value in the cookie.
   * @return value
  **/
  @Schema(description = "String value in the cookie.")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CookieMatch cookieMatch = (CookieMatch) o;
    return Objects.equals(this.matchCase, cookieMatch.matchCase) &&
        Objects.equals(this.matchCriteria, cookieMatch.matchCriteria) &&
        Objects.equals(this.name, cookieMatch.name) &&
        Objects.equals(this.value, cookieMatch.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchCase, matchCriteria, name, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CookieMatch {\n");
    
    sb.append("    matchCase: ").append(toIndentedString(matchCase)).append("\n");
    sb.append("    matchCriteria: ").append(toIndentedString(matchCriteria)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
