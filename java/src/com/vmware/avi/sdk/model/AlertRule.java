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
import com.vmware.avi.sdk.model.AlertFilter;
import com.vmware.avi.sdk.model.AlertRuleEvent;
import com.vmware.avi.sdk.model.AlertRuleMetric;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * AlertRule
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class AlertRule {
  @JsonProperty("conn_app_log_rule")
  private AlertFilter connAppLogRule = null;

  @JsonProperty("event_match_filter")
  private String eventMatchFilter = null;

  @JsonProperty("metrics_rule")
  private List<AlertRuleMetric> metricsRule = null;

  @JsonProperty("operator")
  private String operator = "OPERATOR_AND";

  @JsonProperty("sys_event_rule")
  private List<AlertRuleEvent> sysEventRule = null;

  public AlertRule connAppLogRule(AlertFilter connAppLogRule) {
    this.connAppLogRule = connAppLogRule;
    return this;
  }

   /**
   * Get connAppLogRule
   * @return connAppLogRule
  **/
  @Schema(description = "")
  public AlertFilter getConnAppLogRule() {
    return connAppLogRule;
  }

  public void setConnAppLogRule(AlertFilter connAppLogRule) {
    this.connAppLogRule = connAppLogRule;
  }

  public AlertRule eventMatchFilter(String eventMatchFilter) {
    this.eventMatchFilter = eventMatchFilter;
    return this;
  }

   /**
   * event_match_filter of AlertRule.
   * @return eventMatchFilter
  **/
  @Schema(description = "event_match_filter of AlertRule.")
  public String getEventMatchFilter() {
    return eventMatchFilter;
  }

  public void setEventMatchFilter(String eventMatchFilter) {
    this.eventMatchFilter = eventMatchFilter;
  }

  public AlertRule metricsRule(List<AlertRuleMetric> metricsRule) {
    this.metricsRule = metricsRule;
    return this;
  }

  public AlertRule addMetricsRuleItem(AlertRuleMetric metricsRuleItem) {
    if (this.metricsRule == null) {
      this.metricsRule = new ArrayList<AlertRuleMetric>();
    }
    this.metricsRule.add(metricsRuleItem);
    return this;
  }

   /**
   * Placeholder for description of property metrics_rule of obj type AlertRule field type str  type object
   * @return metricsRule
  **/
  @Schema(description = "Placeholder for description of property metrics_rule of obj type AlertRule field type str  type object")
  public List<AlertRuleMetric> getMetricsRule() {
    return metricsRule;
  }

  public void setMetricsRule(List<AlertRuleMetric> metricsRule) {
    this.metricsRule = metricsRule;
  }

  public AlertRule operator(String operator) {
    this.operator = operator;
    return this;
  }

   /**
   *  Enum options - OPERATOR_AND, OPERATOR_OR.
   * @return operator
  **/
  @Schema(description = " Enum options - OPERATOR_AND, OPERATOR_OR.")
  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public AlertRule sysEventRule(List<AlertRuleEvent> sysEventRule) {
    this.sysEventRule = sysEventRule;
    return this;
  }

  public AlertRule addSysEventRuleItem(AlertRuleEvent sysEventRuleItem) {
    if (this.sysEventRule == null) {
      this.sysEventRule = new ArrayList<AlertRuleEvent>();
    }
    this.sysEventRule.add(sysEventRuleItem);
    return this;
  }

   /**
   * Placeholder for description of property sys_event_rule of obj type AlertRule field type str  type object
   * @return sysEventRule
  **/
  @Schema(description = "Placeholder for description of property sys_event_rule of obj type AlertRule field type str  type object")
  public List<AlertRuleEvent> getSysEventRule() {
    return sysEventRule;
  }

  public void setSysEventRule(List<AlertRuleEvent> sysEventRule) {
    this.sysEventRule = sysEventRule;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertRule alertRule = (AlertRule) o;
    return Objects.equals(this.connAppLogRule, alertRule.connAppLogRule) &&
        Objects.equals(this.eventMatchFilter, alertRule.eventMatchFilter) &&
        Objects.equals(this.metricsRule, alertRule.metricsRule) &&
        Objects.equals(this.operator, alertRule.operator) &&
        Objects.equals(this.sysEventRule, alertRule.sysEventRule);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connAppLogRule, eventMatchFilter, metricsRule, operator, sysEventRule);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertRule {\n");
    
    sb.append("    connAppLogRule: ").append(toIndentedString(connAppLogRule)).append("\n");
    sb.append("    eventMatchFilter: ").append(toIndentedString(eventMatchFilter)).append("\n");
    sb.append("    metricsRule: ").append(toIndentedString(metricsRule)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    sysEventRule: ").append(toIndentedString(sysEventRule)).append("\n");
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
