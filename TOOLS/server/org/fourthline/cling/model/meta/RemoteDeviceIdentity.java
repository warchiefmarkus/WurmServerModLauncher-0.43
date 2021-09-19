/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.message.discovery.IncomingNotificationRequest;
/*     */ import org.fourthline.cling.model.message.discovery.IncomingSearchResponse;
/*     */ import org.fourthline.cling.model.types.UDN;
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
/*     */ public class RemoteDeviceIdentity
/*     */   extends DeviceIdentity
/*     */ {
/*     */   private final URL descriptorURL;
/*     */   private final byte[] interfaceMacAddress;
/*     */   private final InetAddress discoveredOnLocalAddress;
/*     */   
/*     */   public RemoteDeviceIdentity(UDN udn, RemoteDeviceIdentity template) {
/*  50 */     this(udn, template.getMaxAgeSeconds(), template.getDescriptorURL(), template.getInterfaceMacAddress(), template.getDiscoveredOnLocalAddress());
/*     */   }
/*     */   
/*     */   public RemoteDeviceIdentity(UDN udn, Integer maxAgeSeconds, URL descriptorURL, byte[] interfaceMacAddress, InetAddress discoveredOnLocalAddress) {
/*  54 */     super(udn, maxAgeSeconds);
/*  55 */     this.descriptorURL = descriptorURL;
/*  56 */     this.interfaceMacAddress = interfaceMacAddress;
/*  57 */     this.discoveredOnLocalAddress = discoveredOnLocalAddress;
/*     */   }
/*     */   
/*     */   public RemoteDeviceIdentity(IncomingNotificationRequest notificationRequest) {
/*  61 */     this(notificationRequest.getUDN(), notificationRequest
/*  62 */         .getMaxAge(), notificationRequest
/*  63 */         .getLocationURL(), notificationRequest
/*  64 */         .getInterfaceMacHeader(), notificationRequest
/*  65 */         .getLocalAddress());
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteDeviceIdentity(IncomingSearchResponse searchResponse) {
/*  70 */     this(searchResponse.getRootDeviceUDN(), searchResponse
/*  71 */         .getMaxAge(), searchResponse
/*  72 */         .getLocationURL(), searchResponse
/*  73 */         .getInterfaceMacHeader(), searchResponse
/*  74 */         .getLocalAddress());
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getDescriptorURL() {
/*  79 */     return this.descriptorURL;
/*     */   }
/*     */   
/*     */   public byte[] getInterfaceMacAddress() {
/*  83 */     return this.interfaceMacAddress;
/*     */   }
/*     */   
/*     */   public InetAddress getDiscoveredOnLocalAddress() {
/*  87 */     return this.discoveredOnLocalAddress;
/*     */   }
/*     */   
/*     */   public byte[] getWakeOnLANBytes() {
/*  91 */     if (getInterfaceMacAddress() == null) return null; 
/*  92 */     byte[] bytes = new byte[6 + 16 * (getInterfaceMacAddress()).length]; int i;
/*  93 */     for (i = 0; i < 6; i++) {
/*  94 */       bytes[i] = -1;
/*     */     }
/*  96 */     for (i = 6; i < bytes.length; i += (getInterfaceMacAddress()).length) {
/*  97 */       System.arraycopy(getInterfaceMacAddress(), 0, bytes, i, (getInterfaceMacAddress()).length);
/*     */     }
/*  99 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     if (ModelUtil.ANDROID_RUNTIME) {
/* 106 */       return "(RemoteDeviceIdentity) UDN: " + getUdn() + ", Descriptor: " + getDescriptorURL();
/*     */     }
/* 108 */     return "(" + getClass().getSimpleName() + ") UDN: " + getUdn() + ", Descriptor: " + getDescriptorURL();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\RemoteDeviceIdentity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */