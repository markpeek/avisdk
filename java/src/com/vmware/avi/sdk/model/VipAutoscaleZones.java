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
 * VipAutoscaleZones
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class VipAutoscaleZones {
  @JsonProperty("availability_zone")
  private String availabilityZone = null;

  @JsonProperty("fip_capable")
  private Boolean fipCapable = null;

  @JsonProperty("subnet_uuid")
  private String subnetUuid = null;

   /**
   * Availability zone associated with the subnet. Field introduced in 17.2.12, 18.1.2.
   * @return availabilityZone
  **/
  @Schema(description = "Availability zone associated with the subnet. Field introduced in 17.2.12, 18.1.2.")
  public String getAvailabilityZone() {
    return availabilityZone;
  }

   /**
   * Determines if the subnet is capable of hosting publicly accessible IP. Field introduced in 17.2.12, 18.1.2.
   * @return fipCapable
  **/
  @Schema(description = "Determines if the subnet is capable of hosting publicly accessible IP. Field introduced in 17.2.12, 18.1.2.")
  public Boolean isFipCapable() {
    return fipCapable;
  }

  public VipAutoscaleZones subnetUuid(String subnetUuid) {
    this.subnetUuid = subnetUuid;
    return this;
  }

   /**
   * UUID of the subnet for new IP address allocation. Field introduced in 17.2.12, 18.1.2.
   * @return subnetUuid
  **/
  @Schema(description = "UUID of the subnet for new IP address allocation. Field introduced in 17.2.12, 18.1.2.")
  public String getSubnetUuid() {
    return subnetUuid;
  }

  public void setSubnetUuid(String subnetUuid) {
    this.subnetUuid = subnetUuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VipAutoscaleZones vipAutoscaleZones = (VipAutoscaleZones) o;
    return Objects.equals(this.availabilityZone, vipAutoscaleZones.availabilityZone) &&
        Objects.equals(this.fipCapable, vipAutoscaleZones.fipCapable) &&
        Objects.equals(this.subnetUuid, vipAutoscaleZones.subnetUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availabilityZone, fipCapable, subnetUuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VipAutoscaleZones {\n");
    
    sb.append("    availabilityZone: ").append(toIndentedString(availabilityZone)).append("\n");
    sb.append("    fipCapable: ").append(toIndentedString(fipCapable)).append("\n");
    sb.append("    subnetUuid: ").append(toIndentedString(subnetUuid)).append("\n");
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