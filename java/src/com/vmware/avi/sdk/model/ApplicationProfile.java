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
import com.vmware.avi.sdk.model.DnsServiceApplicationProfile;
import com.vmware.avi.sdk.model.DosRateLimitProfile;
import com.vmware.avi.sdk.model.HTTPApplicationProfile;
import com.vmware.avi.sdk.model.SipServiceApplicationProfile;
import com.vmware.avi.sdk.model.TCPApplicationProfile;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * ApplicationProfile
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-03-12T12:27:26.755+05:30[Asia/Kolkata]")
public class ApplicationProfile {
  @JsonProperty("_last_modified")
  private String _lastModified = null;

  @JsonProperty("cloud_config_cksum")
  private String cloudConfigCksum = null;

  @JsonProperty("created_by")
  private String createdBy = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("dns_service_profile")
  private DnsServiceApplicationProfile dnsServiceProfile = null;

  @JsonProperty("dos_rl_profile")
  private DosRateLimitProfile dosRlProfile = null;

  @JsonProperty("http_profile")
  private HTTPApplicationProfile httpProfile = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("preserve_client_ip")
  private Boolean preserveClientIp = null;

  @JsonProperty("preserve_client_port")
  private Boolean preserveClientPort = null;

  @JsonProperty("sip_service_profile")
  private SipServiceApplicationProfile sipServiceProfile = null;

  @JsonProperty("tcp_app_profile")
  private TCPApplicationProfile tcpAppProfile = null;

  @JsonProperty("tenant_ref")
  private String tenantRef = null;

  @JsonProperty("type")
  private String type = null;

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

  public ApplicationProfile cloudConfigCksum(String cloudConfigCksum) {
    this.cloudConfigCksum = cloudConfigCksum;
    return this;
  }

   /**
   * Checksum of application profiles. Internally set by cloud connector. Field introduced in 17.2.14, 18.1.5, 18.2.1.
   * @return cloudConfigCksum
  **/
  @Schema(description = "Checksum of application profiles. Internally set by cloud connector. Field introduced in 17.2.14, 18.1.5, 18.2.1.")
  public String getCloudConfigCksum() {
    return cloudConfigCksum;
  }

  public void setCloudConfigCksum(String cloudConfigCksum) {
    this.cloudConfigCksum = cloudConfigCksum;
  }

  public ApplicationProfile createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Name of the application profile creator. Field introduced in 17.2.14, 18.1.5, 18.2.1.
   * @return createdBy
  **/
  @Schema(description = "Name of the application profile creator. Field introduced in 17.2.14, 18.1.5, 18.2.1.")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public ApplicationProfile description(String description) {
    this.description = description;
    return this;
  }

   /**
   * User defined description for the object.
   * @return description
  **/
  @Schema(description = "User defined description for the object.")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ApplicationProfile dnsServiceProfile(DnsServiceApplicationProfile dnsServiceProfile) {
    this.dnsServiceProfile = dnsServiceProfile;
    return this;
  }

   /**
   * Get dnsServiceProfile
   * @return dnsServiceProfile
  **/
  @Schema(description = "")
  public DnsServiceApplicationProfile getDnsServiceProfile() {
    return dnsServiceProfile;
  }

  public void setDnsServiceProfile(DnsServiceApplicationProfile dnsServiceProfile) {
    this.dnsServiceProfile = dnsServiceProfile;
  }

  public ApplicationProfile dosRlProfile(DosRateLimitProfile dosRlProfile) {
    this.dosRlProfile = dosRlProfile;
    return this;
  }

   /**
   * Get dosRlProfile
   * @return dosRlProfile
  **/
  @Schema(description = "")
  public DosRateLimitProfile getDosRlProfile() {
    return dosRlProfile;
  }

  public void setDosRlProfile(DosRateLimitProfile dosRlProfile) {
    this.dosRlProfile = dosRlProfile;
  }

  public ApplicationProfile httpProfile(HTTPApplicationProfile httpProfile) {
    this.httpProfile = httpProfile;
    return this;
  }

   /**
   * Get httpProfile
   * @return httpProfile
  **/
  @Schema(description = "")
  public HTTPApplicationProfile getHttpProfile() {
    return httpProfile;
  }

  public void setHttpProfile(HTTPApplicationProfile httpProfile) {
    this.httpProfile = httpProfile;
  }

  public ApplicationProfile name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the application profile.
   * @return name
  **/
  @Schema(required = true, description = "The name of the application profile.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ApplicationProfile preserveClientIp(Boolean preserveClientIp) {
    this.preserveClientIp = preserveClientIp;
    return this;
  }

   /**
   * Specifies if client IP needs to be preserved for backend connection. Not compatible with Connection Multiplexing.
   * @return preserveClientIp
  **/
  @Schema(description = "Specifies if client IP needs to be preserved for backend connection. Not compatible with Connection Multiplexing.")
  public Boolean isPreserveClientIp() {
    return preserveClientIp;
  }

  public void setPreserveClientIp(Boolean preserveClientIp) {
    this.preserveClientIp = preserveClientIp;
  }

  public ApplicationProfile preserveClientPort(Boolean preserveClientPort) {
    this.preserveClientPort = preserveClientPort;
    return this;
  }

   /**
   * Specifies if we need to preserve client port while preserving client IP for backend connections. Field introduced in 17.2.7.
   * @return preserveClientPort
  **/
  @Schema(description = "Specifies if we need to preserve client port while preserving client IP for backend connections. Field introduced in 17.2.7.")
  public Boolean isPreserveClientPort() {
    return preserveClientPort;
  }

  public void setPreserveClientPort(Boolean preserveClientPort) {
    this.preserveClientPort = preserveClientPort;
  }

  public ApplicationProfile sipServiceProfile(SipServiceApplicationProfile sipServiceProfile) {
    this.sipServiceProfile = sipServiceProfile;
    return this;
  }

   /**
   * Get sipServiceProfile
   * @return sipServiceProfile
  **/
  @Schema(description = "")
  public SipServiceApplicationProfile getSipServiceProfile() {
    return sipServiceProfile;
  }

  public void setSipServiceProfile(SipServiceApplicationProfile sipServiceProfile) {
    this.sipServiceProfile = sipServiceProfile;
  }

  public ApplicationProfile tcpAppProfile(TCPApplicationProfile tcpAppProfile) {
    this.tcpAppProfile = tcpAppProfile;
    return this;
  }

   /**
   * Get tcpAppProfile
   * @return tcpAppProfile
  **/
  @Schema(description = "")
  public TCPApplicationProfile getTcpAppProfile() {
    return tcpAppProfile;
  }

  public void setTcpAppProfile(TCPApplicationProfile tcpAppProfile) {
    this.tcpAppProfile = tcpAppProfile;
  }

  public ApplicationProfile tenantRef(String tenantRef) {
    this.tenantRef = tenantRef;
    return this;
  }

   /**
   *  It is a reference to an object of type Tenant.
   * @return tenantRef
  **/
  @Schema(description = " It is a reference to an object of type Tenant.")
  public String getTenantRef() {
    return tenantRef;
  }

  public void setTenantRef(String tenantRef) {
    this.tenantRef = tenantRef;
  }

  public ApplicationProfile type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Specifies which application layer proxy is enabled for the virtual service. Enum options - APPLICATION_PROFILE_TYPE_L4, APPLICATION_PROFILE_TYPE_HTTP, APPLICATION_PROFILE_TYPE_SYSLOG, APPLICATION_PROFILE_TYPE_DNS, APPLICATION_PROFILE_TYPE_SSL, APPLICATION_PROFILE_TYPE_SIP.
   * @return type
  **/
  @Schema(required = true, description = "Specifies which application layer proxy is enabled for the virtual service. Enum options - APPLICATION_PROFILE_TYPE_L4, APPLICATION_PROFILE_TYPE_HTTP, APPLICATION_PROFILE_TYPE_SYSLOG, APPLICATION_PROFILE_TYPE_DNS, APPLICATION_PROFILE_TYPE_SSL, APPLICATION_PROFILE_TYPE_SIP.")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

   /**
   * url
   * @return url
  **/
  @Schema(description = "url")
  public String getUrl() {
    return url;
  }

  public ApplicationProfile uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * UUID of the application profile.
   * @return uuid
  **/
  @Schema(description = "UUID of the application profile.")
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
    ApplicationProfile applicationProfile = (ApplicationProfile) o;
    return Objects.equals(this._lastModified, applicationProfile._lastModified) &&
        Objects.equals(this.cloudConfigCksum, applicationProfile.cloudConfigCksum) &&
        Objects.equals(this.createdBy, applicationProfile.createdBy) &&
        Objects.equals(this.description, applicationProfile.description) &&
        Objects.equals(this.dnsServiceProfile, applicationProfile.dnsServiceProfile) &&
        Objects.equals(this.dosRlProfile, applicationProfile.dosRlProfile) &&
        Objects.equals(this.httpProfile, applicationProfile.httpProfile) &&
        Objects.equals(this.name, applicationProfile.name) &&
        Objects.equals(this.preserveClientIp, applicationProfile.preserveClientIp) &&
        Objects.equals(this.preserveClientPort, applicationProfile.preserveClientPort) &&
        Objects.equals(this.sipServiceProfile, applicationProfile.sipServiceProfile) &&
        Objects.equals(this.tcpAppProfile, applicationProfile.tcpAppProfile) &&
        Objects.equals(this.tenantRef, applicationProfile.tenantRef) &&
        Objects.equals(this.type, applicationProfile.type) &&
        Objects.equals(this.url, applicationProfile.url) &&
        Objects.equals(this.uuid, applicationProfile.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_lastModified, cloudConfigCksum, createdBy, description, dnsServiceProfile, dosRlProfile, httpProfile, name, preserveClientIp, preserveClientPort, sipServiceProfile, tcpAppProfile, tenantRef, type, url, uuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationProfile {\n");
    
    sb.append("    _lastModified: ").append(toIndentedString(_lastModified)).append("\n");
    sb.append("    cloudConfigCksum: ").append(toIndentedString(cloudConfigCksum)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dnsServiceProfile: ").append(toIndentedString(dnsServiceProfile)).append("\n");
    sb.append("    dosRlProfile: ").append(toIndentedString(dosRlProfile)).append("\n");
    sb.append("    httpProfile: ").append(toIndentedString(httpProfile)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    preserveClientIp: ").append(toIndentedString(preserveClientIp)).append("\n");
    sb.append("    preserveClientPort: ").append(toIndentedString(preserveClientPort)).append("\n");
    sb.append("    sipServiceProfile: ").append(toIndentedString(sipServiceProfile)).append("\n");
    sb.append("    tcpAppProfile: ").append(toIndentedString(tcpAppProfile)).append("\n");
    sb.append("    tenantRef: ").append(toIndentedString(tenantRef)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
