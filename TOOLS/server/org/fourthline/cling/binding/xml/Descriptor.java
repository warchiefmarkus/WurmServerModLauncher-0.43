/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import org.w3c.dom.Node;
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
/*     */ public abstract class Descriptor
/*     */ {
/*     */   public static interface Device
/*     */   {
/*     */     public static final String NAMESPACE_URI = "urn:schemas-upnp-org:device-1-0";
/*     */     public static final String DLNA_NAMESPACE_URI = "urn:schemas-dlna-org:device-1-0";
/*     */     public static final String DLNA_PREFIX = "dlna";
/*     */     public static final String SEC_NAMESPACE_URI = "http://www.sec.co.kr/dlna";
/*     */     public static final String SEC_PREFIX = "sec";
/*     */     
/*     */     public enum ELEMENT
/*     */     {
/*  36 */       root,
/*  37 */       specVersion, major, minor,
/*  38 */       URLBase,
/*  39 */       device,
/*  40 */       UDN,
/*  41 */       X_DLNADOC,
/*  42 */       X_DLNACAP,
/*  43 */       ProductCap,
/*  44 */       X_ProductCap,
/*  45 */       deviceType,
/*  46 */       friendlyName,
/*  47 */       manufacturer,
/*  48 */       manufacturerURL,
/*  49 */       modelDescription,
/*  50 */       modelName,
/*  51 */       modelNumber,
/*  52 */       modelURL,
/*  53 */       presentationURL,
/*  54 */       UPC,
/*  55 */       serialNumber,
/*  56 */       iconList, icon, width, height, depth, url, mimetype,
/*  57 */       serviceList, service, serviceType, serviceId, SCPDURL, controlURL, eventSubURL,
/*  58 */       deviceList;
/*     */       
/*     */       public static ELEMENT valueOrNullOf(String s) {
/*     */         try {
/*  62 */           return valueOf(s);
/*  63 */         } catch (IllegalArgumentException ex) {
/*  64 */           return null;
/*     */         } 
/*     */       }
/*     */       
/*     */       public boolean equals(Node node) {
/*  69 */         return toString().equals(node.getLocalName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Service
/*     */   {
/*     */     public static final String NAMESPACE_URI = "urn:schemas-upnp-org:service-1-0";
/*     */     
/*     */     public enum ELEMENT {
/*  79 */       scpd,
/*  80 */       specVersion, major, minor,
/*  81 */       actionList, action, name,
/*  82 */       argumentList, argument, direction, relatedStateVariable, retval,
/*  83 */       serviceStateTable, stateVariable, dataType, defaultValue,
/*  84 */       allowedValueList, allowedValue, allowedValueRange, minimum, maximum, step;
/*     */       
/*     */       public static ELEMENT valueOrNullOf(String s) {
/*     */         try {
/*  88 */           return valueOf(s);
/*  89 */         } catch (IllegalArgumentException ex) {
/*  90 */           return null;
/*     */         } 
/*     */       }
/*     */       
/*     */       public boolean equals(Node node) {
/*  95 */         return toString().equals(node.getLocalName());
/*     */       }
/*     */     }
/*     */     
/*     */     public enum ATTRIBUTE
/*     */     {
/* 101 */       sendEvents;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\Descriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */