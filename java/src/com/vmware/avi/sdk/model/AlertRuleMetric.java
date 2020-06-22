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
import com.vmware.avi.sdk.model.AlertMetricThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * AlertRuleMetric
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class AlertRuleMetric {
  @JsonProperty("duration")
  private Integer duration = null;

  @JsonProperty("metric_id")
  private String metricId = null;

  @JsonProperty("metric_threshold")
  private AlertMetricThreshold metricThreshold = null;

  public AlertRuleMetric duration(Integer duration) {
    this.duration = duration;
    return this;
  }

   /**
   * Evaluation window for the Metrics.
   * @return duration
  **/
  @Schema(description = "Evaluation window for the Metrics.")
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public AlertRuleMetric metricId(String metricId) {
    this.metricId = metricId;
    return this;
  }

   /**
   * Metric Id for the Alert. Eg. l4_client.avg_complete_conns.
   * @return metricId
  **/
  @Schema(description = "Metric Id for the Alert. Eg. l4_client.avg_complete_conns.")
  public String getMetricId() {
    return metricId;
  }

  public void setMetricId(String metricId) {
    this.metricId = metricId;
  }

  public AlertRuleMetric metricThreshold(AlertMetricThreshold metricThreshold) {
    this.metricThreshold = metricThreshold;
    return this;
  }

   /**
   * Get metricThreshold
   * @return metricThreshold
  **/
  @Schema(required = true, description = "")
  public AlertMetricThreshold getMetricThreshold() {
    return metricThreshold;
  }

  public void setMetricThreshold(AlertMetricThreshold metricThreshold) {
    this.metricThreshold = metricThreshold;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertRuleMetric alertRuleMetric = (AlertRuleMetric) o;
    return Objects.equals(this.duration, alertRuleMetric.duration) &&
        Objects.equals(this.metricId, alertRuleMetric.metricId) &&
        Objects.equals(this.metricThreshold, alertRuleMetric.metricThreshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(duration, metricId, metricThreshold);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertRuleMetric {\n");
    
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    metricId: ").append(toIndentedString(metricId)).append("\n");
    sb.append("    metricThreshold: ").append(toIndentedString(metricThreshold)).append("\n");
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