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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeviceType
/*     */ {
/*  35 */   private static final Logger log = Logger.getLogger(DeviceType.class.getName());
/*     */ 
/*     */   
/*     */   public static final String UNKNOWN = "UNKNOWN";
/*     */   
/*  40 */   public static final Pattern PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):device:([a-zA-Z_0-9\\-]{1,64}):([0-9]+).*");
/*     */   
/*     */   private String namespace;
/*     */   private String type;
/*  44 */   private int version = 1;
/*     */   
/*     */   public DeviceType(String namespace, String type) {
/*  47 */     this(namespace, type, 1);
/*     */   }
/*     */   
/*     */   public DeviceType(String namespace, String type, int version) {
/*  51 */     if (namespace != null && !namespace.matches("[a-zA-Z0-9\\-\\.]+")) {
/*  52 */       throw new IllegalArgumentException("Device type namespace contains illegal characters");
/*     */     }
/*  54 */     this.namespace = namespace;
/*     */     
/*  56 */     if (type != null && !type.matches("[a-zA-Z_0-9\\-]{1,64}")) {
/*  57 */       throw new IllegalArgumentException("Device type suffix too long (64) or contains illegal characters");
/*     */     }
/*  59 */     this.type = type;
/*     */     
/*  61 */     this.version = version;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/*  65 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  69 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  73 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DeviceType valueOf(String s) throws InvalidValueException {
/*  81 */     DeviceType deviceType = null;
/*     */ 
/*     */     
/*  84 */     s = s.replaceAll("\\s", "");
/*     */ 
/*     */     
/*     */     try {
/*  88 */       deviceType = UDADeviceType.valueOf(s);
/*  89 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (deviceType != null) {
/*  94 */       return deviceType;
/*     */     }
/*     */     
/*     */     try {
/*  98 */       Matcher matcher = PATTERN.matcher(s);
/*  99 */       if (matcher.matches()) {
/* 100 */         return new DeviceType(matcher.group(1), matcher.group(2), Integer.valueOf(matcher.group(3)).intValue());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 105 */       matcher = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):device::([0-9]+).*").matcher(s);
/* 106 */       if (matcher.matches() && matcher.groupCount() >= 2) {
/* 107 */         log.warning("UPnP specification violation, no device type token, defaulting to UNKNOWN: " + s);
/* 108 */         return new DeviceType(matcher.group(1), "UNKNOWN", Integer.valueOf(matcher.group(2)).intValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 113 */       matcher = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):device:(.+?):([0-9]+).*").matcher(s);
/* 114 */       if (matcher.matches() && matcher.groupCount() >= 3) {
/* 115 */         String cleanToken = matcher.group(2).replaceAll("[^a-zA-Z_0-9\\-]", "-");
/* 116 */         log.warning("UPnP specification violation, replacing invalid device type token '" + matcher
/*     */             
/* 118 */             .group(2) + "' with: " + cleanToken);
/*     */ 
/*     */ 
/*     */         
/* 122 */         return new DeviceType(matcher.group(1), cleanToken, Integer.valueOf(matcher.group(3)).intValue());
/*     */       } 
/* 124 */     } catch (RuntimeException e) {
/* 125 */       throw new InvalidValueException(String.format("Can't parse device type string (namespace/type/version) '%s': %s", new Object[] { s, e
/* 126 */               .toString() }));
/*     */     } 
/*     */ 
/*     */     
/* 130 */     throw new InvalidValueException("Can't parse device type string (namespace/type/version): " + s);
/*     */   }
/*     */   
/*     */   public boolean implementsVersion(DeviceType that) {
/* 134 */     if (!this.namespace.equals(that.namespace)) return false; 
/* 135 */     if (!this.type.equals(that.type)) return false; 
/* 136 */     if (this.version < that.version) return false; 
/* 137 */     return true;
/*     */   }
/*     */   
/*     */   public String getDisplayString() {
/* 141 */     return getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     return "urn:" + getNamespace() + ":device:" + getType() + ":" + getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 151 */     if (this == o) return true; 
/* 152 */     if (o == null || !(o instanceof DeviceType)) return false;
/*     */     
/* 154 */     DeviceType that = (DeviceType)o;
/*     */     
/* 156 */     if (this.version != that.version) return false; 
/* 157 */     if (!this.namespace.equals(that.namespace)) return false; 
/* 158 */     if (!this.type.equals(that.type)) return false;
/*     */     
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 165 */     int result = this.namespace.hashCode();
/* 166 */     result = 31 * result + this.type.hashCode();
/* 167 */     result = 31 * result + this.version;
/* 168 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\DeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */