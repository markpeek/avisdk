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
import com.vmware.avi.sdk.model.IpAddr;
import com.vmware.avi.sdk.model.IpAddrPrefix;
import com.vmware.avi.sdk.model.IpAddrRange;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * DebugIpAddr
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class DebugIpAddr {
  @JsonProperty("addrs")
  private List<IpAddr> addrs = null;

  @JsonProperty("prefixes")
  private List<IpAddrPrefix> prefixes = null;

  @JsonProperty("ranges")
  private List<IpAddrRange> ranges = null;

  public DebugIpAddr addrs(List<IpAddr> addrs) {
    this.addrs = addrs;
    return this;
  }

  public DebugIpAddr addAddrsItem(IpAddr addrsItem) {
    if (this.addrs == null) {
      this.addrs = new ArrayList<IpAddr>();
    }
    this.addrs.add(addrsItem);
    return this;
  }

   /**
   * Placeholder for description of property addrs of obj type DebugIpAddr field type str  type object
   * @return addrs
  **/
  @Schema(description = "Placeholder for description of property addrs of obj type DebugIpAddr field type str  type object")
  public List<IpAddr> getAddrs() {
    return addrs;
  }

  public void setAddrs(List<IpAddr> addrs) {
    this.addrs = addrs;
  }

  public DebugIpAddr prefixes(List<IpAddrPrefix> prefixes) {
    this.prefixes = prefixes;
    return this;
  }

  public DebugIpAddr addPrefixesItem(IpAddrPrefix prefixesItem) {
    if (this.prefixes == null) {
      this.prefixes = new ArrayList<IpAddrPrefix>();
    }
    this.prefixes.add(prefixesItem);
    return this;
  }

   /**
   * Placeholder for description of property prefixes of obj type DebugIpAddr field type str  type object
   * @return prefixes
  **/
  @Schema(description = "Placeholder for description of property prefixes of obj type DebugIpAddr field type str  type object")
  public List<IpAddrPrefix> getPrefixes() {
    return prefixes;
  }

  public void setPrefixes(List<IpAddrPrefix> prefixes) {
    this.prefixes = prefixes;
  }

  public DebugIpAddr ranges(List<IpAddrRange> ranges) {
    this.ranges = ranges;
    return this;
  }

  public DebugIpAddr addRangesItem(IpAddrRange rangesItem) {
    if (this.ranges == null) {
      this.ranges = new ArrayList<IpAddrRange>();
    }
    this.ranges.add(rangesItem);
    return this;
  }

   /**
   * Placeholder for description of property ranges of obj type DebugIpAddr field type str  type object
   * @return ranges
  **/
  @Schema(description = "Placeholder for description of property ranges of obj type DebugIpAddr field type str  type object")
  public List<IpAddrRange> getRanges() {
    return ranges;
  }

  public void setRanges(List<IpAddrRange> ranges) {
    this.ranges = ranges;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DebugIpAddr debugIpAddr = (DebugIpAddr) o;
    return Objects.equals(this.addrs, debugIpAddr.addrs) &&
        Objects.equals(this.prefixes, debugIpAddr.prefixes) &&
        Objects.equals(this.ranges, debugIpAddr.ranges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addrs, prefixes, ranges);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DebugIpAddr {\n");
    
    sb.append("    addrs: ").append(toIndentedString(addrs)).append("\n");
    sb.append("    prefixes: ").append(toIndentedString(prefixes)).append("\n");
    sb.append("    ranges: ").append(toIndentedString(ranges)).append("\n");
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