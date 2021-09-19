/*     */ package org.fourthline.cling.model;
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
/*     */ public class ServerClientTokens
/*     */ {
/*     */   public static final String UNKNOWN_PLACEHOLDER = "UNKNOWN";
/*  32 */   private int majorVersion = 1;
/*  33 */   private int minorVersion = 0;
/*     */   
/*  35 */   private String osName = System.getProperty("os.name").replaceAll("[^a-zA-Z0-9\\.\\-_]", "");
/*  36 */   private String osVersion = System.getProperty("os.version").replaceAll("[^a-zA-Z0-9\\.\\-_]", "");
/*  37 */   private String productName = "Cling";
/*  38 */   private String productVersion = "2.0";
/*     */ 
/*     */   
/*     */   public ServerClientTokens() {}
/*     */   
/*     */   public ServerClientTokens(int majorVersion, int minorVersion) {
/*  44 */     this.majorVersion = majorVersion;
/*  45 */     this.minorVersion = minorVersion;
/*     */   }
/*     */   
/*     */   public ServerClientTokens(String productName, String productVersion) {
/*  49 */     this.productName = productName;
/*  50 */     this.productVersion = productVersion;
/*     */   }
/*     */   
/*     */   public ServerClientTokens(int majorVersion, int minorVersion, String osName, String osVersion, String productName, String productVersion) {
/*  54 */     this.majorVersion = majorVersion;
/*  55 */     this.minorVersion = minorVersion;
/*  56 */     this.osName = osName;
/*  57 */     this.osVersion = osVersion;
/*  58 */     this.productName = productName;
/*  59 */     this.productVersion = productVersion;
/*     */   }
/*     */   
/*     */   public int getMajorVersion() {
/*  63 */     return this.majorVersion;
/*     */   }
/*     */   
/*     */   public void setMajorVersion(int majorVersion) {
/*  67 */     this.majorVersion = majorVersion;
/*     */   }
/*     */   
/*     */   public int getMinorVersion() {
/*  71 */     return this.minorVersion;
/*     */   }
/*     */   
/*     */   public void setMinorVersion(int minorVersion) {
/*  75 */     this.minorVersion = minorVersion;
/*     */   }
/*     */   
/*     */   public String getOsName() {
/*  79 */     return this.osName;
/*     */   }
/*     */   
/*     */   public void setOsName(String osName) {
/*  83 */     this.osName = osName;
/*     */   }
/*     */   
/*     */   public String getOsVersion() {
/*  87 */     return this.osVersion;
/*     */   }
/*     */   
/*     */   public void setOsVersion(String osVersion) {
/*  91 */     this.osVersion = osVersion;
/*     */   }
/*     */   
/*     */   public String getProductName() {
/*  95 */     return this.productName;
/*     */   }
/*     */   
/*     */   public void setProductName(String productName) {
/*  99 */     this.productName = productName;
/*     */   }
/*     */   
/*     */   public String getProductVersion() {
/* 103 */     return this.productVersion;
/*     */   }
/*     */   
/*     */   public void setProductVersion(String productVersion) {
/* 107 */     this.productVersion = productVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return getOsName() + "/" + getOsVersion() + " UPnP/" + 
/* 113 */       getMajorVersion() + "." + getMinorVersion() + " " + 
/* 114 */       getProductName() + "/" + getProductVersion();
/*     */   }
/*     */   
/*     */   public String getHttpToken() {
/* 118 */     StringBuilder sb = new StringBuilder(256);
/* 119 */     sb.append((this.osName.indexOf(' ') != -1) ? this.osName.replace(' ', '_') : this.osName);
/* 120 */     sb.append('/');
/* 121 */     sb.append((this.osVersion.indexOf(' ') != -1) ? this.osVersion.replace(' ', '_') : this.osVersion);
/* 122 */     sb.append(" UPnP/");
/* 123 */     sb.append(this.majorVersion);
/* 124 */     sb.append('.');
/* 125 */     sb.append(this.minorVersion);
/* 126 */     sb.append(' ');
/* 127 */     sb.append((this.productName.indexOf(' ') != -1) ? this.productName.replace(' ', '_') : this.productName);
/* 128 */     sb.append('/');
/* 129 */     sb.append((this.productVersion.indexOf(' ') != -1) ? this.productVersion.replace(' ', '_') : this.productVersion);
/*     */     
/* 131 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getOsToken() {
/* 135 */     return getOsName().replaceAll(" ", "_") + "/" + getOsVersion().replaceAll(" ", "_");
/*     */   }
/*     */   
/*     */   public String getProductToken() {
/* 139 */     return getProductName().replaceAll(" ", "_") + "/" + getProductVersion().replaceAll(" ", "_");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 144 */     if (this == o) return true; 
/* 145 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 147 */     ServerClientTokens that = (ServerClientTokens)o;
/*     */     
/* 149 */     if (this.majorVersion != that.majorVersion) return false; 
/* 150 */     if (this.minorVersion != that.minorVersion) return false; 
/* 151 */     if (!this.osName.equals(that.osName)) return false; 
/* 152 */     if (!this.osVersion.equals(that.osVersion)) return false; 
/* 153 */     if (!this.productName.equals(that.productName)) return false; 
/* 154 */     if (!this.productVersion.equals(that.productVersion)) return false;
/*     */     
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 161 */     int result = this.majorVersion;
/* 162 */     result = 31 * result + this.minorVersion;
/* 163 */     result = 31 * result + this.osName.hashCode();
/* 164 */     result = 31 * result + this.osVersion.hashCode();
/* 165 */     result = 31 * result + this.productName.hashCode();
/* 166 */     result = 31 * result + this.productVersion.hashCode();
/* 167 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ServerClientTokens.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */