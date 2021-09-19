/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ public class VersionInfo
/*     */ {
/*     */   public static final String UNAVAILABLE = "UNAVAILABLE";
/*     */   public static final String VERSION_PROPERTY_FILE = "version.properties";
/*     */   public static final String PROPERTY_MODULE = "info.module";
/*     */   public static final String PROPERTY_RELEASE = "info.release";
/*     */   public static final String PROPERTY_TIMESTAMP = "info.timestamp";
/*     */   private final String infoPackage;
/*     */   private final String infoModule;
/*     */   private final String infoRelease;
/*     */   private final String infoTimestamp;
/*     */   private final String infoClassloader;
/*     */   
/*     */   protected VersionInfo(String pckg, String module, String release, String time, String clsldr) {
/*  91 */     if (pckg == null) {
/*  92 */       throw new IllegalArgumentException("Package identifier must not be null.");
/*     */     }
/*     */ 
/*     */     
/*  96 */     this.infoPackage = pckg;
/*  97 */     this.infoModule = (module != null) ? module : "UNAVAILABLE";
/*  98 */     this.infoRelease = (release != null) ? release : "UNAVAILABLE";
/*  99 */     this.infoTimestamp = (time != null) ? time : "UNAVAILABLE";
/* 100 */     this.infoClassloader = (clsldr != null) ? clsldr : "UNAVAILABLE";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPackage() {
/* 111 */     return this.infoPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getModule() {
/* 121 */     return this.infoModule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getRelease() {
/* 131 */     return this.infoRelease;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getTimestamp() {
/* 141 */     return this.infoTimestamp;
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
/*     */   public final String getClassloader() {
/* 153 */     return this.infoClassloader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     StringBuilder sb = new StringBuilder(20 + this.infoPackage.length() + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     sb.append("VersionInfo(").append(this.infoPackage).append(':').append(this.infoModule);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (!"UNAVAILABLE".equals(this.infoRelease))
/* 175 */       sb.append(':').append(this.infoRelease); 
/* 176 */     if (!"UNAVAILABLE".equals(this.infoTimestamp)) {
/* 177 */       sb.append(':').append(this.infoTimestamp);
/*     */     }
/* 179 */     sb.append(')');
/*     */     
/* 181 */     if (!"UNAVAILABLE".equals(this.infoClassloader)) {
/* 182 */       sb.append('@').append(this.infoClassloader);
/*     */     }
/* 184 */     return sb.toString();
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
/*     */ 
/*     */   
/*     */   public static final VersionInfo[] loadVersionInfo(String[] pckgs, ClassLoader clsldr) {
/* 200 */     if (pckgs == null) {
/* 201 */       throw new IllegalArgumentException("Package identifier list must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 205 */     List<VersionInfo> vil = new ArrayList<VersionInfo>(pckgs.length);
/* 206 */     for (int i = 0; i < pckgs.length; i++) {
/* 207 */       VersionInfo vi = loadVersionInfo(pckgs[i], clsldr);
/* 208 */       if (vi != null) {
/* 209 */         vil.add(vi);
/*     */       }
/*     */     } 
/* 212 */     return vil.<VersionInfo>toArray(new VersionInfo[vil.size()]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final VersionInfo loadVersionInfo(String pckg, ClassLoader clsldr) {
/* 230 */     if (pckg == null) {
/* 231 */       throw new IllegalArgumentException("Package identifier must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 235 */     if (clsldr == null) {
/* 236 */       clsldr = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 238 */     Properties vip = null;
/*     */ 
/*     */     
/*     */     try {
/* 242 */       InputStream is = clsldr.getResourceAsStream(pckg.replace('.', '/') + "/" + "version.properties");
/*     */       
/* 244 */       if (is != null) {
/*     */         try {
/* 246 */           Properties props = new Properties();
/* 247 */           props.load(is);
/* 248 */           vip = props;
/*     */         } finally {
/* 250 */           is.close();
/*     */         } 
/*     */       }
/* 253 */     } catch (IOException ex) {}
/*     */ 
/*     */ 
/*     */     
/* 257 */     VersionInfo result = null;
/* 258 */     if (vip != null) {
/* 259 */       result = fromMap(pckg, vip, clsldr);
/*     */     }
/* 261 */     return result;
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
/*     */ 
/*     */   
/*     */   protected static final VersionInfo fromMap(String pckg, Map<?, ?> info, ClassLoader clsldr) {
/* 277 */     if (pckg == null) {
/* 278 */       throw new IllegalArgumentException("Package identifier must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 282 */     String module = null;
/* 283 */     String release = null;
/* 284 */     String timestamp = null;
/*     */     
/* 286 */     if (info != null) {
/* 287 */       module = (String)info.get("info.module");
/* 288 */       if (module != null && module.length() < 1) {
/* 289 */         module = null;
/*     */       }
/* 291 */       release = (String)info.get("info.release");
/* 292 */       if (release != null && (release.length() < 1 || release.equals("${pom.version}")))
/*     */       {
/* 294 */         release = null;
/*     */       }
/* 296 */       timestamp = (String)info.get("info.timestamp");
/* 297 */       if (timestamp != null && (timestamp.length() < 1 || timestamp.equals("${mvn.timestamp}")))
/*     */       {
/*     */ 
/*     */         
/* 301 */         timestamp = null;
/*     */       }
/*     */     } 
/* 304 */     String clsldrstr = null;
/* 305 */     if (clsldr != null) {
/* 306 */       clsldrstr = clsldr.toString();
/*     */     }
/* 308 */     return new VersionInfo(pckg, module, release, timestamp, clsldrstr);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\VersionInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */