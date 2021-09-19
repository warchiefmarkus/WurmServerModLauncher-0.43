/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fourthline.cling.model.ModelUtil;
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
/*     */ public class SoapActionType
/*     */ {
/*     */   public static final String MAGIC_CONTROL_NS = "schemas-upnp-org";
/*     */   public static final String MAGIC_CONTROL_TYPE = "control-1-0";
/*  35 */   public static final Pattern PATTERN_MAGIC_CONTROL = Pattern.compile("urn:schemas-upnp-org:control-1-0#([a-zA-Z0-9^-_\\p{L}\\p{N}]{1}[a-zA-Z0-9^-_\\.\\\\p{L}\\\\p{N}\\p{Mc}\\p{Sk}]*)");
/*     */ 
/*     */   
/*  38 */   public static final Pattern PATTERN = Pattern.compile("urn:([a-zA-Z0-9\\-\\.]+):service:([a-zA-Z_0-9\\-]{1,64}):([0-9]+)#([a-zA-Z0-9^-_\\p{L}\\p{N}]{1}[a-zA-Z0-9^-_\\.\\\\p{L}\\\\p{N}\\p{Mc}\\p{Sk}]*)");
/*     */   
/*     */   private String namespace;
/*     */   private String type;
/*     */   private String actionName;
/*     */   private Integer version;
/*     */   
/*     */   public SoapActionType(ServiceType serviceType, String actionName) {
/*  46 */     this(serviceType.getNamespace(), serviceType.getType(), Integer.valueOf(serviceType.getVersion()), actionName);
/*     */   }
/*     */   
/*     */   public SoapActionType(String namespace, String type, Integer version, String actionName) {
/*  50 */     this.namespace = namespace;
/*  51 */     this.type = type;
/*  52 */     this.version = version;
/*  53 */     this.actionName = actionName;
/*     */     
/*  55 */     if (actionName != null && !ModelUtil.isValidUDAName(actionName)) {
/*  56 */       throw new IllegalArgumentException("Action name contains illegal characters: " + actionName);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getActionName() {
/*  61 */     return this.actionName;
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
/*     */   public Integer getVersion() {
/*  73 */     return this.version;
/*     */   }
/*     */   
/*     */   public static SoapActionType valueOf(String s) throws InvalidValueException {
/*  77 */     Matcher magicControlMatcher = PATTERN_MAGIC_CONTROL.matcher(s);
/*     */     
/*     */     try {
/*  80 */       if (magicControlMatcher.matches()) {
/*  81 */         return new SoapActionType("schemas-upnp-org", "control-1-0", null, magicControlMatcher.group(1));
/*     */       }
/*     */       
/*  84 */       Matcher matcher = PATTERN.matcher(s);
/*  85 */       if (matcher.matches()) {
/*  86 */         return new SoapActionType(matcher.group(1), matcher.group(2), Integer.valueOf(matcher.group(3)), matcher.group(4));
/*     */       }
/*  88 */     } catch (RuntimeException e) {
/*  89 */       throw new InvalidValueException(String.format("Can't parse action type string (namespace/type/version#actionName) '%s': %s", new Object[] { s, e
/*  90 */               .toString() }));
/*     */     } 
/*     */     
/*  93 */     throw new InvalidValueException("Can't parse action type string (namespace/type/version#actionName): " + s);
/*     */   }
/*     */   
/*     */   public ServiceType getServiceType() {
/*  97 */     if (this.version == null) return null; 
/*  98 */     return new ServiceType(this.namespace, this.type, this.version.intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     return getTypeString() + "#" + getActionName();
/*     */   }
/*     */   
/*     */   public String getTypeString() {
/* 107 */     if (this.version == null) {
/* 108 */       return "urn:" + getNamespace() + ":" + getType();
/*     */     }
/* 110 */     return "urn:" + getNamespace() + ":service:" + getType() + ":" + getVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 116 */     if (this == o) return true; 
/* 117 */     if (o == null || !(o instanceof SoapActionType)) return false;
/*     */     
/* 119 */     SoapActionType that = (SoapActionType)o;
/*     */     
/* 121 */     if (!this.actionName.equals(that.actionName)) return false; 
/* 122 */     if (!this.namespace.equals(that.namespace)) return false; 
/* 123 */     if (!this.type.equals(that.type)) return false; 
/* 124 */     if ((this.version != null) ? !this.version.equals(that.version) : (that.version != null)) return false;
/*     */     
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 131 */     int result = this.namespace.hashCode();
/* 132 */     result = 31 * result + this.type.hashCode();
/* 133 */     result = 31 * result + this.actionName.hashCode();
/* 134 */     result = 31 * result + ((this.version != null) ? this.version.hashCode() : 0);
/* 135 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\SoapActionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */