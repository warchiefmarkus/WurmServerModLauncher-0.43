/*     */ package org.fourthline.cling.model.message.header;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.seamless.util.Exceptions;
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
/*     */ public abstract class UpnpHeader<T>
/*     */ {
/*  37 */   private static final Logger log = Logger.getLogger(UpnpHeader.class.getName());
/*     */   
/*     */   private T value;
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  44 */     USN("USN", new Class[] { USNRootDeviceHeader.class, DeviceUSNHeader.class, ServiceUSNHeader.class, UDNHeader.class
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/*  50 */     NT("NT", new Class[] { RootDeviceHeader.class, UDADeviceTypeHeader.class, UDAServiceTypeHeader.class, DeviceTypeHeader.class, ServiceTypeHeader.class, UDNHeader.class, NTEventHeader.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/*  59 */     NTS("NTS", new Class[] { NTSHeader.class }),
/*  60 */     HOST("HOST", new Class[] { HostHeader.class }),
/*  61 */     SERVER("SERVER", new Class[] { ServerHeader.class }),
/*  62 */     LOCATION("LOCATION", new Class[] { LocationHeader.class }),
/*  63 */     MAX_AGE("CACHE-CONTROL", new Class[] { MaxAgeHeader.class }),
/*  64 */     USER_AGENT("USER-AGENT", new Class[] { UserAgentHeader.class }),
/*  65 */     CONTENT_TYPE("CONTENT-TYPE", new Class[] { ContentTypeHeader.class }),
/*  66 */     MAN("MAN", new Class[] { MANHeader.class }),
/*  67 */     MX("MX", new Class[] { MXHeader.class }),
/*  68 */     ST("ST", new Class[] { STAllHeader.class, RootDeviceHeader.class, UDADeviceTypeHeader.class, UDAServiceTypeHeader.class, DeviceTypeHeader.class, ServiceTypeHeader.class, UDNHeader.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/*  77 */     EXT("EXT", new Class[] { EXTHeader.class }),
/*  78 */     SOAPACTION("SOAPACTION", new Class[] { SoapActionHeader.class }),
/*  79 */     TIMEOUT("TIMEOUT", new Class[] { TimeoutHeader.class }),
/*  80 */     CALLBACK("CALLBACK", new Class[] { CallbackHeader.class }),
/*  81 */     SID("SID", new Class[] { SubscriptionIdHeader.class }),
/*  82 */     SEQ("SEQ", new Class[] { EventSequenceHeader.class }),
/*  83 */     RANGE("RANGE", new Class[] { RangeHeader.class }),
/*  84 */     CONTENT_RANGE("CONTENT-RANGE", new Class[] { ContentRangeHeader.class }),
/*  85 */     PRAGMA("PRAGMA", new Class[] { PragmaHeader.class
/*     */       }),
/*  87 */     EXT_IFACE_MAC("X-CLING-IFACE-MAC", new Class[] { InterfaceMacHeader.class }),
/*  88 */     EXT_AV_CLIENT_INFO("X-AV-CLIENT-INFO", new Class[] { AVClientInfoHeader.class });
/*     */     
/*  90 */     private static Map<String, Type> byName = new HashMap<String, Type>()
/*     */       {
/*     */       
/*     */       };
/*     */ 
/*     */     
/*     */     private String httpName;
/*     */ 
/*     */     
/*     */     @SafeVarargs
/*     */     Type(String httpName, Class<? extends UpnpHeader>... headerClass) {
/* 101 */       this.httpName = httpName;
/* 102 */       this.headerTypes = headerClass;
/*     */     } private Class<? extends UpnpHeader>[] headerTypes; static {
/*     */     
/*     */     } public String getHttpName() {
/* 106 */       return this.httpName;
/*     */     }
/*     */     
/*     */     public Class<? extends UpnpHeader>[] getHeaderTypes() {
/* 110 */       return this.headerTypes;
/*     */     }
/*     */     
/*     */     public boolean isValidHeaderType(Class<? extends UpnpHeader> clazz) {
/* 114 */       for (Class<? extends UpnpHeader> permissibleType : getHeaderTypes()) {
/* 115 */         if (permissibleType.isAssignableFrom(clazz)) {
/* 116 */           return true;
/*     */         }
/*     */       } 
/* 119 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Type getByHttpName(String httpName) {
/* 126 */       if (httpName == null) return null; 
/* 127 */       return byName.get(httpName.toUpperCase(Locale.ROOT));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(T value) {
/* 134 */     this.value = value;
/*     */   }
/*     */   
/*     */   public T getValue() {
/* 138 */     return this.value;
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
/*     */   public abstract void setString(String paramString) throws InvalidHeaderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UpnpHeader newInstance(Type type, String headerValue) {
/* 168 */     UpnpHeader upnpHeader = null;
/* 169 */     for (int i = 0; i < (type.getHeaderTypes()).length && upnpHeader == null; i++) {
/* 170 */       Class<? extends UpnpHeader> headerClass = type.getHeaderTypes()[i];
/*     */       try {
/* 172 */         log.finest("Trying to parse '" + type + "' with class: " + headerClass.getSimpleName());
/* 173 */         upnpHeader = headerClass.newInstance();
/* 174 */         if (headerValue != null) {
/* 175 */           upnpHeader.setString(headerValue);
/*     */         }
/* 177 */       } catch (InvalidHeaderException ex) {
/* 178 */         log.finest("Invalid header value for tested type: " + headerClass.getSimpleName() + " - " + ex.getMessage());
/* 179 */         upnpHeader = null;
/* 180 */       } catch (Exception ex) {
/* 181 */         log.severe("Error instantiating header of type '" + type + "' with value: " + headerValue);
/* 182 */         log.log(Level.SEVERE, "Exception root cause: ", Exceptions.unwrap(ex));
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     return upnpHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 191 */     return "(" + getClass().getSimpleName() + ") '" + getValue() + "'";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\UpnpHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */