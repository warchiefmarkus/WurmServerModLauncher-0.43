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
/*     */ public class ServiceType
/*     */ {
/*  35 */   private static final Logger log = Logger.getLogger(ServiceType.class.getName());
/*     */ 
/*     */   
/*  38 */   public static final Pattern PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):service:([a-zA-Z_0-9\\-]{1,64}):([0-9]+).*");
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final Pattern BROKEN_PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):serviceId:([a-zA-Z_0-9\\-]{1,64}):([0-9]+).*");
/*     */   
/*     */   private String namespace;
/*     */   private String type;
/*  46 */   private int version = 1;
/*     */   
/*     */   public ServiceType(String namespace, String type) {
/*  49 */     this(namespace, type, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServiceType(String namespace, String type, int version) {
/*  54 */     if (namespace != null && !namespace.matches("[a-zA-Z0-9\\-\\.]+")) {
/*  55 */       throw new IllegalArgumentException("Service type namespace contains illegal characters");
/*     */     }
/*  57 */     this.namespace = namespace;
/*     */     
/*  59 */     if (type != null && !type.matches("[a-zA-Z_0-9\\-]{1,64}")) {
/*  60 */       throw new IllegalArgumentException("Service type suffix too long (64) or contains illegal characters");
/*     */     }
/*  62 */     this.type = type;
/*     */     
/*  64 */     this.version = version;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/*  68 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  72 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  76 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServiceType valueOf(String s) throws InvalidValueException {
/*  84 */     if (s == null) {
/*  85 */       throw new InvalidValueException("Can't parse null string");
/*     */     }
/*  87 */     ServiceType serviceType = null;
/*     */ 
/*     */     
/*  90 */     s = s.replaceAll("\\s", "");
/*     */ 
/*     */     
/*     */     try {
/*  94 */       serviceType = UDAServiceType.valueOf(s);
/*  95 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (serviceType != null) {
/* 100 */       return serviceType;
/*     */     }
/*     */     
/*     */     try {
/* 104 */       Matcher matcher = PATTERN.matcher(s);
/* 105 */       if (matcher.matches() && matcher.groupCount() >= 3) {
/* 106 */         return new ServiceType(matcher.group(1), matcher.group(2), Integer.valueOf(matcher.group(3)).intValue());
/*     */       }
/*     */       
/* 109 */       matcher = BROKEN_PATTERN.matcher(s);
/* 110 */       if (matcher.matches() && matcher.groupCount() >= 3) {
/* 111 */         return new ServiceType(matcher.group(1), matcher.group(2), Integer.valueOf(matcher.group(3)).intValue());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 116 */       matcher = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):service:(.+?):([0-9]+).*").matcher(s);
/* 117 */       if (matcher.matches() && matcher.groupCount() >= 3) {
/* 118 */         String cleanToken = matcher.group(2).replaceAll("[^a-zA-Z_0-9\\-]", "-");
/* 119 */         log.warning("UPnP specification violation, replacing invalid service type token '" + matcher
/*     */             
/* 121 */             .group(2) + "' with: " + cleanToken);
/*     */ 
/*     */ 
/*     */         
/* 125 */         return new ServiceType(matcher.group(1), cleanToken, Integer.valueOf(matcher.group(3)).intValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 130 */       matcher = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):serviceId:(.+?):([0-9]+).*").matcher(s);
/* 131 */       if (matcher.matches() && matcher.groupCount() >= 3) {
/* 132 */         String cleanToken = matcher.group(2).replaceAll("[^a-zA-Z_0-9\\-]", "-");
/* 133 */         log.warning("UPnP specification violation, replacing invalid service type token '" + matcher
/*     */             
/* 135 */             .group(2) + "' with: " + cleanToken);
/*     */ 
/*     */ 
/*     */         
/* 139 */         return new ServiceType(matcher.group(1), cleanToken, Integer.valueOf(matcher.group(3)).intValue());
/*     */       } 
/* 141 */     } catch (RuntimeException e) {
/* 142 */       throw new InvalidValueException(String.format("Can't parse service type string (namespace/type/version) '%s': %s", new Object[] { s, e
/* 143 */               .toString() }));
/*     */     } 
/*     */ 
/*     */     
/* 147 */     throw new InvalidValueException("Can't parse service type string (namespace/type/version): " + s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implementsVersion(ServiceType that) {
/* 155 */     if (that == null) return false; 
/* 156 */     if (!this.namespace.equals(that.namespace)) return false; 
/* 157 */     if (!this.type.equals(that.type)) return false; 
/* 158 */     if (this.version < that.version) return false; 
/* 159 */     return true;
/*     */   }
/*     */   
/*     */   public String toFriendlyString() {
/* 163 */     return getNamespace() + ":" + getType() + ":" + getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return "urn:" + getNamespace() + ":service:" + getType() + ":" + getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 173 */     if (this == o) return true; 
/* 174 */     if (o == null || !(o instanceof ServiceType)) return false;
/*     */     
/* 176 */     ServiceType that = (ServiceType)o;
/*     */     
/* 178 */     if (this.version != that.version) return false; 
/* 179 */     if (!this.namespace.equals(that.namespace)) return false; 
/* 180 */     if (!this.type.equals(that.type)) return false;
/*     */     
/* 182 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 187 */     int result = this.namespace.hashCode();
/* 188 */     result = 31 * result + this.type.hashCode();
/* 189 */     result = 31 * result + this.version;
/* 190 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\ServiceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */