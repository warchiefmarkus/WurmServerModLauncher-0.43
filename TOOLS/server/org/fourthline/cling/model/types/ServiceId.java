/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ServiceId
/*     */ {
/*  31 */   private static final Logger log = Logger.getLogger(ServiceId.class.getName());
/*     */ 
/*     */   
/*     */   public static final String UNKNOWN = "UNKNOWN";
/*     */   
/*  36 */   public static final Pattern PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):serviceId:([a-zA-Z_0-9\\-:\\.]{1,64})");
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final Pattern BROKEN_PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):service:([a-zA-Z_0-9\\-:\\.]{1,64})");
/*     */   
/*     */   private String namespace;
/*     */   private String id;
/*     */   
/*     */   public ServiceId(String namespace, String id) {
/*  46 */     if (namespace != null && !namespace.matches("[a-zA-Z0-9\\-\\.]+")) {
/*  47 */       throw new IllegalArgumentException("Service ID namespace contains illegal characters");
/*     */     }
/*  49 */     this.namespace = namespace;
/*     */     
/*  51 */     if (id != null && !id.matches("[a-zA-Z_0-9\\-:\\.]{1,64}")) {
/*  52 */       throw new IllegalArgumentException("Service ID suffix too long (64) or contains illegal characters");
/*     */     }
/*  54 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/*  58 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  62 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ServiceId valueOf(String s) throws InvalidValueException {
/*  67 */     ServiceId serviceId = null;
/*     */ 
/*     */     
/*     */     try {
/*  71 */       serviceId = UDAServiceId.valueOf(s);
/*  72 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  76 */     if (serviceId != null) {
/*  77 */       return serviceId;
/*     */     }
/*     */     
/*  80 */     Matcher matcher = PATTERN.matcher(s);
/*  81 */     if (matcher.matches() && matcher.groupCount() >= 2) {
/*  82 */       return new ServiceId(matcher.group(1), matcher.group(2));
/*     */     }
/*     */     
/*  85 */     matcher = BROKEN_PATTERN.matcher(s);
/*  86 */     if (matcher.matches() && matcher.groupCount() >= 2) {
/*  87 */       return new ServiceId(matcher.group(1), matcher.group(2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  92 */     matcher = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):serviceId:").matcher(s);
/*  93 */     if (matcher.matches() && matcher.groupCount() >= 1) {
/*  94 */       log.warning("UPnP specification violation, no service ID token, defaulting to UNKNOWN: " + s);
/*  95 */       return new ServiceId(matcher.group(1), "UNKNOWN");
/*     */     } 
/*     */ 
/*     */     
/*  99 */     String[] tokens = s.split("[:]");
/* 100 */     if (tokens.length == 4) {
/* 101 */       log.warning("UPnP specification violation, trying a simple colon-split of: " + s);
/* 102 */       return new ServiceId(tokens[1], tokens[3]);
/*     */     } 
/*     */     
/* 105 */     throw new InvalidValueException("Can't parse service ID string (namespace/id): " + s);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return "urn:" + getNamespace() + ":serviceId:" + getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 115 */     if (this == o) return true; 
/* 116 */     if (o == null || !(o instanceof ServiceId)) return false;
/*     */     
/* 118 */     ServiceId serviceId = (ServiceId)o;
/*     */     
/* 120 */     if (!this.id.equals(serviceId.id)) return false; 
/* 121 */     if (!this.namespace.equals(serviceId.namespace)) return false;
/*     */     
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     int result = this.namespace.hashCode();
/* 129 */     result = 31 * result + this.id.hashCode();
/* 130 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\ServiceId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */