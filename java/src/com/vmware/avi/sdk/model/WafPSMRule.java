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
import com.vmware.avi.sdk.model.WafPSMMatchElement;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * WafPSMRule
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class WafPSMRule {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("enable")
  private Boolean enable = true;

  @JsonProperty("index")
  private Integer index = null;

  @JsonProperty("match_case")
  private String matchCase = "INSENSITIVE";

  @JsonProperty("match_elements")
  private List<WafPSMMatchElement> matchElements = null;

  @JsonProperty("match_value_max_length")
  private Integer matchValueMaxLength = null;

  @JsonProperty("match_value_pattern")
  private String matchValuePattern = null;

  @JsonProperty("mode")
  private String mode = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("paranoia_level")
  private String paranoiaLevel = "WAF_PARANOIA_LEVEL_LOW";

  @JsonProperty("rule_id")
  private String ruleId = null;

  public WafPSMRule description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Free-text comment about this rule. Field introduced in 18.2.3.
   * @return description
  **/
  @Schema(description = "Free-text comment about this rule. Field introduced in 18.2.3.")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WafPSMRule enable(Boolean enable) {
    this.enable = enable;
    return this;
  }

   /**
   * Enable or disable this rule. Field introduced in 18.2.3.
   * @return enable
  **/
  @Schema(description = "Enable or disable this rule. Field introduced in 18.2.3.")
  public Boolean isEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  public WafPSMRule index(Integer index) {
    this.index = index;
    return this;
  }

   /**
   * Rule index, this is used to determine the order of the rules. Field introduced in 18.2.3.
   * @return index
  **/
  @Schema(required = true, description = "Rule index, this is used to determine the order of the rules. Field introduced in 18.2.3.")
  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public WafPSMRule matchCase(String matchCase) {
    this.matchCase = matchCase;
    return this;
  }

   /**
   * The field match_value_pattern regular expression is case sensitive. Enum options - SENSITIVE, INSENSITIVE. Field introduced in 18.2.3.
   * @return matchCase
  **/
  @Schema(description = "The field match_value_pattern regular expression is case sensitive. Enum options - SENSITIVE, INSENSITIVE. Field introduced in 18.2.3.")
  public String getMatchCase() {
    return matchCase;
  }

  public void setMatchCase(String matchCase) {
    this.matchCase = matchCase;
  }

  public WafPSMRule matchElements(List<WafPSMMatchElement> matchElements) {
    this.matchElements = matchElements;
    return this;
  }

  public WafPSMRule addMatchElementsItem(WafPSMMatchElement matchElementsItem) {
    if (this.matchElements == null) {
      this.matchElements = new ArrayList<WafPSMMatchElement>();
    }
    this.matchElements.add(matchElementsItem);
    return this;
  }

   /**
   * The match elements, for example ARGS id or ARGS|!ARGS password. Field introduced in 18.2.3.
   * @return matchElements
  **/
  @Schema(description = "The match elements, for example ARGS id or ARGS|!ARGS password. Field introduced in 18.2.3.")
  public List<WafPSMMatchElement> getMatchElements() {
    return matchElements;
  }

  public void setMatchElements(List<WafPSMMatchElement> matchElements) {
    this.matchElements = matchElements;
  }

  public WafPSMRule matchValueMaxLength(Integer matchValueMaxLength) {
    this.matchValueMaxLength = matchValueMaxLength;
    return this;
  }

   /**
   * The maximum allowed length of the match_value. If this is not set, the length will not be checked. Field introduced in 18.2.3.
   * @return matchValueMaxLength
  **/
  @Schema(description = "The maximum allowed length of the match_value. If this is not set, the length will not be checked. Field introduced in 18.2.3.")
  public Integer getMatchValueMaxLength() {
    return matchValueMaxLength;
  }

  public void setMatchValueMaxLength(Integer matchValueMaxLength) {
    this.matchValueMaxLength = matchValueMaxLength;
  }

  public WafPSMRule matchValuePattern(String matchValuePattern) {
    this.matchValuePattern = matchValuePattern;
    return this;
  }

   /**
   * A regular expression which describes the expected value. Field introduced in 18.2.3.
   * @return matchValuePattern
  **/
  @Schema(required = true, description = "A regular expression which describes the expected value. Field introduced in 18.2.3.")
  public String getMatchValuePattern() {
    return matchValuePattern;
  }

  public void setMatchValuePattern(String matchValuePattern) {
    this.matchValuePattern = matchValuePattern;
  }

  public WafPSMRule mode(String mode) {
    this.mode = mode;
    return this;
  }

   /**
   * WAF Rule mode. This can be detection or enforcement. If this is not set, the Policy mode is used. This only takes effect if the policy allows delegation. Enum options - WAF_MODE_DETECTION_ONLY, WAF_MODE_ENFORCEMENT. Field introduced in 18.2.3.
   * @return mode
  **/
  @Schema(description = "WAF Rule mode. This can be detection or enforcement. If this is not set, the Policy mode is used. This only takes effect if the policy allows delegation. Enum options - WAF_MODE_DETECTION_ONLY, WAF_MODE_ENFORCEMENT. Field introduced in 18.2.3.")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public WafPSMRule name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the rule. Field introduced in 18.2.3.
   * @return name
  **/
  @Schema(required = true, description = "Name of the rule. Field introduced in 18.2.3.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public WafPSMRule paranoiaLevel(String paranoiaLevel) {
    this.paranoiaLevel = paranoiaLevel;
    return this;
  }

   /**
   * WAF Ruleset paranoia mode. This is used to select Rules based on the paranoia-level. Enum options - WAF_PARANOIA_LEVEL_LOW, WAF_PARANOIA_LEVEL_MEDIUM, WAF_PARANOIA_LEVEL_HIGH, WAF_PARANOIA_LEVEL_EXTREME. Field introduced in 18.2.3.
   * @return paranoiaLevel
  **/
  @Schema(description = "WAF Ruleset paranoia mode. This is used to select Rules based on the paranoia-level. Enum options - WAF_PARANOIA_LEVEL_LOW, WAF_PARANOIA_LEVEL_MEDIUM, WAF_PARANOIA_LEVEL_HIGH, WAF_PARANOIA_LEVEL_EXTREME. Field introduced in 18.2.3.")
  public String getParanoiaLevel() {
    return paranoiaLevel;
  }

  public void setParanoiaLevel(String paranoiaLevel) {
    this.paranoiaLevel = paranoiaLevel;
  }

  public WafPSMRule ruleId(String ruleId) {
    this.ruleId = ruleId;
    return this;
  }

   /**
   * Id field which is used for log and metric generation. This id must be unique for all rules in this group. Field introduced in 18.2.3.
   * @return ruleId
  **/
  @Schema(required = true, description = "Id field which is used for log and metric generation. This id must be unique for all rules in this group. Field introduced in 18.2.3.")
  public String getRuleId() {
    return ruleId;
  }

  public void setRuleId(String ruleId) {
    this.ruleId = ruleId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WafPSMRule wafPSMRule = (WafPSMRule) o;
    return Objects.equals(this.description, wafPSMRule.description) &&
        Objects.equals(this.enable, wafPSMRule.enable) &&
        Objects.equals(this.index, wafPSMRule.index) &&
        Objects.equals(this.matchCase, wafPSMRule.matchCase) &&
        Objects.equals(this.matchElements, wafPSMRule.matchElements) &&
        Objects.equals(this.matchValueMaxLength, wafPSMRule.matchValueMaxLength) &&
        Objects.equals(this.matchValuePattern, wafPSMRule.matchValuePattern) &&
        Objects.equals(this.mode, wafPSMRule.mode) &&
        Objects.equals(this.name, wafPSMRule.name) &&
        Objects.equals(this.paranoiaLevel, wafPSMRule.paranoiaLevel) &&
        Objects.equals(this.ruleId, wafPSMRule.ruleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, enable, index, matchCase, matchElements, matchValueMaxLength, matchValuePattern, mode, name, paranoiaLevel, ruleId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WafPSMRule {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    enable: ").append(toIndentedString(enable)).append("\n");
    sb.append("    index: ").append(toIndentedString(index)).append("\n");
    sb.append("    matchCase: ").append(toIndentedString(matchCase)).append("\n");
    sb.append("    matchElements: ").append(toIndentedString(matchElements)).append("\n");
    sb.append("    matchValueMaxLength: ").append(toIndentedString(matchValueMaxLength)).append("\n");
    sb.append("    matchValuePattern: ").append(toIndentedString(matchValuePattern)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    paranoiaLevel: ").append(toIndentedString(paranoiaLevel)).append("\n");
    sb.append("    ruleId: ").append(toIndentedString(ruleId)).append("\n");
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
