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
 * InternalGatewayMonitor
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class InternalGatewayMonitor {
  @JsonProperty("disable_gateway_monitor")
  private Boolean disableGatewayMonitor = null;

  @JsonProperty("gateway_monitor_failure_threshold")
  private Integer gatewayMonitorFailureThreshold = 10;

  @JsonProperty("gateway_monitor_interval")
  private Integer gatewayMonitorInterval = 1000;

  @JsonProperty("gateway_monitor_success_threshold")
  private Integer gatewayMonitorSuccessThreshold = 15;

  public InternalGatewayMonitor disableGatewayMonitor(Boolean disableGatewayMonitor) {
    this.disableGatewayMonitor = disableGatewayMonitor;
    return this;
  }

   /**
   * Disable the gateway monitor for default gateway. They are monitored by default. Field introduced in 17.1.1.
   * @return disableGatewayMonitor
  **/
  @Schema(description = "Disable the gateway monitor for default gateway. They are monitored by default. Field introduced in 17.1.1.")
  public Boolean isDisableGatewayMonitor() {
    return disableGatewayMonitor;
  }

  public void setDisableGatewayMonitor(Boolean disableGatewayMonitor) {
    this.disableGatewayMonitor = disableGatewayMonitor;
  }

  public InternalGatewayMonitor gatewayMonitorFailureThreshold(Integer gatewayMonitorFailureThreshold) {
    this.gatewayMonitorFailureThreshold = gatewayMonitorFailureThreshold;
    return this;
  }

   /**
   * The number of consecutive failed gateway health checks before a gateway is marked down. Allowed values are 3-50. Field introduced in 17.1.1.
   * @return gatewayMonitorFailureThreshold
  **/
  @Schema(description = "The number of consecutive failed gateway health checks before a gateway is marked down. Allowed values are 3-50. Field introduced in 17.1.1.")
  public Integer getGatewayMonitorFailureThreshold() {
    return gatewayMonitorFailureThreshold;
  }

  public void setGatewayMonitorFailureThreshold(Integer gatewayMonitorFailureThreshold) {
    this.gatewayMonitorFailureThreshold = gatewayMonitorFailureThreshold;
  }

  public InternalGatewayMonitor gatewayMonitorInterval(Integer gatewayMonitorInterval) {
    this.gatewayMonitorInterval = gatewayMonitorInterval;
    return this;
  }

   /**
   * The interval between two ping requests sent by the gateway monitor in milliseconds. If a value is not specified, requests are sent every second. Allowed values are 100-60000. Field introduced in 17.1.1.
   * @return gatewayMonitorInterval
  **/
  @Schema(description = "The interval between two ping requests sent by the gateway monitor in milliseconds. If a value is not specified, requests are sent every second. Allowed values are 100-60000. Field introduced in 17.1.1.")
  public Integer getGatewayMonitorInterval() {
    return gatewayMonitorInterval;
  }

  public void setGatewayMonitorInterval(Integer gatewayMonitorInterval) {
    this.gatewayMonitorInterval = gatewayMonitorInterval;
  }

  public InternalGatewayMonitor gatewayMonitorSuccessThreshold(Integer gatewayMonitorSuccessThreshold) {
    this.gatewayMonitorSuccessThreshold = gatewayMonitorSuccessThreshold;
    return this;
  }

   /**
   * The number of consecutive successful gateway health checks before a gateway that was marked down by the gateway monitor is marked up. Allowed values are 3-50. Field introduced in 17.1.1.
   * @return gatewayMonitorSuccessThreshold
  **/
  @Schema(description = "The number of consecutive successful gateway health checks before a gateway that was marked down by the gateway monitor is marked up. Allowed values are 3-50. Field introduced in 17.1.1.")
  public Integer getGatewayMonitorSuccessThreshold() {
    return gatewayMonitorSuccessThreshold;
  }

  public void setGatewayMonitorSuccessThreshold(Integer gatewayMonitorSuccessThreshold) {
    this.gatewayMonitorSuccessThreshold = gatewayMonitorSuccessThreshold;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InternalGatewayMonitor internalGatewayMonitor = (InternalGatewayMonitor) o;
    return Objects.equals(this.disableGatewayMonitor, internalGatewayMonitor.disableGatewayMonitor) &&
        Objects.equals(this.gatewayMonitorFailureThreshold, internalGatewayMonitor.gatewayMonitorFailureThreshold) &&
        Objects.equals(this.gatewayMonitorInterval, internalGatewayMonitor.gatewayMonitorInterval) &&
        Objects.equals(this.gatewayMonitorSuccessThreshold, internalGatewayMonitor.gatewayMonitorSuccessThreshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(disableGatewayMonitor, gatewayMonitorFailureThreshold, gatewayMonitorInterval, gatewayMonitorSuccessThreshold);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InternalGatewayMonitor {\n");
    
    sb.append("    disableGatewayMonitor: ").append(toIndentedString(disableGatewayMonitor)).append("\n");
    sb.append("    gatewayMonitorFailureThreshold: ").append(toIndentedString(gatewayMonitorFailureThreshold)).append("\n");
    sb.append("    gatewayMonitorInterval: ").append(toIndentedString(gatewayMonitorInterval)).append("\n");
    sb.append("    gatewayMonitorSuccessThreshold: ").append(toIndentedString(gatewayMonitorSuccessThreshold)).append("\n");
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
