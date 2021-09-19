/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortMapping
/*     */ {
/*     */   private boolean enabled;
/*     */   private UnsignedIntegerFourBytes leaseDurationSeconds;
/*     */   private String remoteHost;
/*     */   private UnsignedIntegerTwoBytes externalPort;
/*     */   private UnsignedIntegerTwoBytes internalPort;
/*     */   private String internalClient;
/*     */   private Protocol protocol;
/*     */   private String description;
/*     */   
/*     */   public enum Protocol
/*     */   {
/*  31 */     UDP,
/*  32 */     TCP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PortMapping() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PortMapping(Map<String, ActionArgumentValue<Service>> map) {
/*  49 */     this(((Boolean)((ActionArgumentValue)map
/*  50 */         .get("NewEnabled")).getValue()).booleanValue(), (UnsignedIntegerFourBytes)((ActionArgumentValue)map
/*  51 */         .get("NewLeaseDuration")).getValue(), (String)((ActionArgumentValue)map
/*  52 */         .get("NewRemoteHost")).getValue(), (UnsignedIntegerTwoBytes)((ActionArgumentValue)map
/*  53 */         .get("NewExternalPort")).getValue(), (UnsignedIntegerTwoBytes)((ActionArgumentValue)map
/*  54 */         .get("NewInternalPort")).getValue(), (String)((ActionArgumentValue)map
/*  55 */         .get("NewInternalClient")).getValue(), 
/*  56 */         Protocol.valueOf(((ActionArgumentValue)map.get("NewProtocol")).toString()), (String)((ActionArgumentValue)map
/*  57 */         .get("NewPortMappingDescription")).getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public PortMapping(int port, String internalClient, Protocol protocol) {
/*  62 */     this(true, new UnsignedIntegerFourBytes(0L), null, new UnsignedIntegerTwoBytes(port), new UnsignedIntegerTwoBytes(port), internalClient, protocol, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PortMapping(int port, String internalClient, Protocol protocol, String description) {
/*  75 */     this(true, new UnsignedIntegerFourBytes(0L), null, new UnsignedIntegerTwoBytes(port), new UnsignedIntegerTwoBytes(port), internalClient, protocol, description);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PortMapping(String remoteHost, UnsignedIntegerTwoBytes externalPort, Protocol protocol) {
/*  88 */     this(true, new UnsignedIntegerFourBytes(0L), remoteHost, externalPort, null, null, protocol, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PortMapping(boolean enabled, UnsignedIntegerFourBytes leaseDurationSeconds, String remoteHost, UnsignedIntegerTwoBytes externalPort, UnsignedIntegerTwoBytes internalPort, String internalClient, Protocol protocol, String description) {
/* 102 */     this.enabled = enabled;
/* 103 */     this.leaseDurationSeconds = leaseDurationSeconds;
/* 104 */     this.remoteHost = remoteHost;
/* 105 */     this.externalPort = externalPort;
/* 106 */     this.internalPort = internalPort;
/* 107 */     this.internalClient = internalClient;
/* 108 */     this.protocol = protocol;
/* 109 */     this.description = description;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 113 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 117 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public UnsignedIntegerFourBytes getLeaseDurationSeconds() {
/* 121 */     return this.leaseDurationSeconds;
/*     */   }
/*     */   
/*     */   public void setLeaseDurationSeconds(UnsignedIntegerFourBytes leaseDurationSeconds) {
/* 125 */     this.leaseDurationSeconds = leaseDurationSeconds;
/*     */   }
/*     */   
/*     */   public boolean hasRemoteHost() {
/* 129 */     return (this.remoteHost != null && this.remoteHost.length() > 0);
/*     */   }
/*     */   
/*     */   public String getRemoteHost() {
/* 133 */     return (this.remoteHost == null) ? "-" : this.remoteHost;
/*     */   }
/*     */   
/*     */   public void setRemoteHost(String remoteHost) {
/* 137 */     this.remoteHost = (remoteHost == null || remoteHost.equals("-") || remoteHost.length() == 0) ? null : remoteHost;
/*     */   }
/*     */   
/*     */   public UnsignedIntegerTwoBytes getExternalPort() {
/* 141 */     return this.externalPort;
/*     */   }
/*     */   
/*     */   public void setExternalPort(UnsignedIntegerTwoBytes externalPort) {
/* 145 */     this.externalPort = externalPort;
/*     */   }
/*     */   
/*     */   public UnsignedIntegerTwoBytes getInternalPort() {
/* 149 */     return this.internalPort;
/*     */   }
/*     */   
/*     */   public void setInternalPort(UnsignedIntegerTwoBytes internalPort) {
/* 153 */     this.internalPort = internalPort;
/*     */   }
/*     */   
/*     */   public String getInternalClient() {
/* 157 */     return this.internalClient;
/*     */   }
/*     */   
/*     */   public void setInternalClient(String internalClient) {
/* 161 */     this.internalClient = internalClient;
/*     */   }
/*     */   
/*     */   public Protocol getProtocol() {
/* 165 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public void setProtocol(Protocol protocol) {
/* 169 */     this.protocol = protocol;
/*     */   }
/*     */   
/*     */   public boolean hasDescription() {
/* 173 */     return (this.description != null);
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 177 */     return (this.description == null) ? "-" : this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 181 */     this.description = (description == null || description.equals("-") || description.length() == 0) ? null : description;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     return "(" + getClass().getSimpleName() + ") Protocol: " + getProtocol() + ", " + getExternalPort() + " => " + getInternalClient();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\PortMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */