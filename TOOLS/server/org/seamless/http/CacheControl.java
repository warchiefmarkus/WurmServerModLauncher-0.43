/*     */ package org.seamless.http;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class CacheControl
/*     */ {
/*  27 */   private int maxAge = -1;
/*  28 */   private int sharedMaxAge = -1;
/*     */   
/*     */   private boolean noCache = false;
/*  31 */   private List<String> noCacheFields = new ArrayList<String>();
/*     */   
/*     */   private boolean privateFlag = false;
/*  34 */   private List<String> privateFields = new ArrayList<String>();
/*     */   
/*     */   private boolean noStore = false;
/*     */   
/*     */   private boolean noTransform = true;
/*     */   private boolean mustRevalidate = false;
/*     */   private boolean proxyRevalidate = false;
/*  41 */   private Map<String, String> cacheExtensions = new HashMap<String, String>();
/*     */   
/*     */   public int getMaxAge() {
/*  44 */     return this.maxAge;
/*     */   }
/*     */   
/*     */   public void setMaxAge(int maxAge) {
/*  48 */     this.maxAge = maxAge;
/*     */   }
/*     */   
/*     */   public int getSharedMaxAge() {
/*  52 */     return this.sharedMaxAge;
/*     */   }
/*     */   
/*     */   public void setSharedMaxAge(int sharedMaxAge) {
/*  56 */     this.sharedMaxAge = sharedMaxAge;
/*     */   }
/*     */   
/*     */   public boolean isNoCache() {
/*  60 */     return this.noCache;
/*     */   }
/*     */   
/*     */   public void setNoCache(boolean noCache) {
/*  64 */     this.noCache = noCache;
/*     */   }
/*     */   
/*     */   public List<String> getNoCacheFields() {
/*  68 */     return this.noCacheFields;
/*     */   }
/*     */   
/*     */   public void setNoCacheFields(List<String> noCacheFields) {
/*  72 */     this.noCacheFields = noCacheFields;
/*     */   }
/*     */   
/*     */   public boolean isPrivateFlag() {
/*  76 */     return this.privateFlag;
/*     */   }
/*     */   
/*     */   public void setPrivateFlag(boolean privateFlag) {
/*  80 */     this.privateFlag = privateFlag;
/*     */   }
/*     */   
/*     */   public List<String> getPrivateFields() {
/*  84 */     return this.privateFields;
/*     */   }
/*     */   
/*     */   public void setPrivateFields(List<String> privateFields) {
/*  88 */     this.privateFields = privateFields;
/*     */   }
/*     */   
/*     */   public boolean isNoStore() {
/*  92 */     return this.noStore;
/*     */   }
/*     */   
/*     */   public void setNoStore(boolean noStore) {
/*  96 */     this.noStore = noStore;
/*     */   }
/*     */   
/*     */   public boolean isNoTransform() {
/* 100 */     return this.noTransform;
/*     */   }
/*     */   
/*     */   public void setNoTransform(boolean noTransform) {
/* 104 */     this.noTransform = noTransform;
/*     */   }
/*     */   
/*     */   public boolean isMustRevalidate() {
/* 108 */     return this.mustRevalidate;
/*     */   }
/*     */   
/*     */   public void setMustRevalidate(boolean mustRevalidate) {
/* 112 */     this.mustRevalidate = mustRevalidate;
/*     */   }
/*     */   
/*     */   public boolean isProxyRevalidate() {
/* 116 */     return this.proxyRevalidate;
/*     */   }
/*     */   
/*     */   public void setProxyRevalidate(boolean proxyRevalidate) {
/* 120 */     this.proxyRevalidate = proxyRevalidate;
/*     */   }
/*     */   
/*     */   public Map<String, String> getCacheExtensions() {
/* 124 */     return this.cacheExtensions;
/*     */   }
/*     */   
/*     */   public void setCacheExtensions(Map<String, String> cacheExtensions) {
/* 128 */     this.cacheExtensions = cacheExtensions;
/*     */   }
/*     */   
/*     */   public static CacheControl valueOf(String s) throws IllegalArgumentException {
/* 132 */     if (s == null) return null; 
/* 133 */     CacheControl result = new CacheControl();
/*     */     
/* 135 */     String[] directives = s.split(",");
/* 136 */     for (String directive : directives) {
/* 137 */       directive = directive.trim();
/*     */       
/* 139 */       String[] nameValue = directive.split("=");
/* 140 */       String name = nameValue[0].trim();
/* 141 */       String value = null;
/* 142 */       if (nameValue.length > 1) {
/* 143 */         value = nameValue[1].trim();
/* 144 */         if (value.startsWith("\"")) value = value.substring(1); 
/* 145 */         if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
/*     */       
/*     */       } 
/* 148 */       String lowercase = name.toLowerCase();
/* 149 */       if ("no-cache".equals(lowercase)) {
/* 150 */         result.setNoCache(true);
/* 151 */         if (value != null && !"".equals(value)) {
/* 152 */           result.getNoCacheFields().add(value);
/*     */         }
/* 154 */       } else if ("private".equals(lowercase)) {
/* 155 */         result.setPrivateFlag(true);
/* 156 */         if (value != null && !"".equals(value)) {
/* 157 */           result.getPrivateFields().add(value);
/*     */         }
/* 159 */       } else if ("no-store".equals(lowercase)) {
/* 160 */         result.setNoStore(true);
/* 161 */       } else if ("max-age".equals(lowercase)) {
/* 162 */         if (value == null)
/* 163 */           throw new IllegalArgumentException("CacheControl max-age header does not have a value: " + value); 
/* 164 */         result.setMaxAge(Integer.valueOf(value).intValue());
/* 165 */       } else if ("s-maxage".equals(lowercase)) {
/* 166 */         if (value == null)
/* 167 */           throw new IllegalArgumentException("CacheControl s-maxage header does not have a value: " + value); 
/* 168 */         result.setSharedMaxAge(Integer.valueOf(value).intValue());
/* 169 */       } else if ("no-transform".equals(lowercase)) {
/* 170 */         result.setNoTransform(true);
/* 171 */       } else if ("must-revalidate".equals(lowercase)) {
/* 172 */         result.setMustRevalidate(true);
/* 173 */       } else if ("proxy-revalidate".equals(lowercase)) {
/* 174 */         result.setProxyRevalidate(true);
/* 175 */       } else if (!"public".equals(lowercase)) {
/*     */ 
/*     */         
/* 178 */         if (value == null) value = ""; 
/* 179 */         result.getCacheExtensions().put(name, value);
/*     */       } 
/*     */     } 
/* 182 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 187 */     StringBuilder sb = new StringBuilder();
/* 188 */     if (!isPrivateFlag()) sb.append("public"); 
/* 189 */     if (isMustRevalidate()) append("must-revalidate", sb); 
/* 190 */     if (isNoTransform()) append("no-transform", sb); 
/* 191 */     if (isNoStore()) append("no-store", sb); 
/* 192 */     if (isProxyRevalidate()) append("proxy-revalidate", sb); 
/* 193 */     if (getSharedMaxAge() > -1) append("s-maxage", sb).append("=").append(getSharedMaxAge()); 
/* 194 */     if (getMaxAge() > -1) append("max-age", sb).append("=").append(getMaxAge()); 
/* 195 */     if (isNoCache()) {
/* 196 */       List<String> fields = getNoCacheFields();
/* 197 */       if (fields.size() < 1) { append("no-cache", sb); }
/*     */       else
/* 199 */       { for (String field : getNoCacheFields()) {
/* 200 */           append("no-cache", sb).append("=\"").append(field).append("\"");
/*     */         } }
/*     */     
/*     */     } 
/* 204 */     if (isPrivateFlag()) {
/* 205 */       List<String> fields = getPrivateFields();
/* 206 */       if (fields.size() < 1) { append("private", sb); }
/*     */       else
/* 208 */       { for (String field : getPrivateFields()) {
/* 209 */           append("private", sb).append("=\"").append(field).append("\"");
/*     */         } }
/*     */     
/*     */     } 
/* 213 */     for (String key : getCacheExtensions().keySet()) {
/* 214 */       String val = getCacheExtensions().get(key);
/* 215 */       append(key, sb);
/* 216 */       if (val != null && !"".equals(val)) {
/* 217 */         sb.append("=\"").append(val).append("\"");
/*     */       }
/*     */     } 
/* 220 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private StringBuilder append(String s, StringBuilder sb) {
/* 224 */     if (sb.length() > 0) sb.append(", "); 
/* 225 */     sb.append(s);
/* 226 */     return sb;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 231 */     if (this == o) return true; 
/* 232 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 234 */     CacheControl that = (CacheControl)o;
/*     */     
/* 236 */     if (this.maxAge != that.maxAge) return false; 
/* 237 */     if (this.mustRevalidate != that.mustRevalidate) return false; 
/* 238 */     if (this.noCache != that.noCache) return false; 
/* 239 */     if (this.noStore != that.noStore) return false; 
/* 240 */     if (this.noTransform != that.noTransform) return false; 
/* 241 */     if (this.privateFlag != that.privateFlag) return false; 
/* 242 */     if (this.proxyRevalidate != that.proxyRevalidate) return false; 
/* 243 */     if (this.sharedMaxAge != that.sharedMaxAge) return false; 
/* 244 */     if (!this.cacheExtensions.equals(that.cacheExtensions)) return false; 
/* 245 */     if (!this.noCacheFields.equals(that.noCacheFields)) return false; 
/* 246 */     if (!this.privateFields.equals(that.privateFields)) return false;
/*     */     
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 253 */     int result = this.maxAge;
/* 254 */     result = 31 * result + this.sharedMaxAge;
/* 255 */     result = 31 * result + (this.noCache ? 1 : 0);
/* 256 */     result = 31 * result + this.noCacheFields.hashCode();
/* 257 */     result = 31 * result + (this.privateFlag ? 1 : 0);
/* 258 */     result = 31 * result + this.privateFields.hashCode();
/* 259 */     result = 31 * result + (this.noStore ? 1 : 0);
/* 260 */     result = 31 * result + (this.noTransform ? 1 : 0);
/* 261 */     result = 31 * result + (this.mustRevalidate ? 1 : 0);
/* 262 */     result = 31 * result + (this.proxyRevalidate ? 1 : 0);
/* 263 */     result = 31 * result + this.cacheExtensions.hashCode();
/* 264 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\CacheControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */