/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.SetCookie;
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
/*     */ @NotThreadSafe
/*     */ public class BasicClientCookie
/*     */   implements SetCookie, ClientCookie, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3869795591041535538L;
/*     */   private final String name;
/*     */   private Map<String, String> attribs;
/*     */   private String value;
/*     */   private String cookieComment;
/*     */   private String cookieDomain;
/*     */   private Date cookieExpiryDate;
/*     */   private String cookiePath;
/*     */   private boolean isSecure;
/*     */   private int cookieVersion;
/*     */   
/*     */   public BasicClientCookie(String name, String value) {
/*  59 */     if (name == null) {
/*  60 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  62 */     this.name = name;
/*  63 */     this.attribs = new HashMap<String, String>();
/*  64 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  82 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  91 */     this.value = value;
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
/*     */   public String getComment() {
/* 103 */     return this.cookieComment;
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
/*     */   public void setComment(String comment) {
/* 115 */     this.cookieComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentURL() {
/* 123 */     return null;
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
/*     */   public Date getExpiryDate() {
/* 139 */     return this.cookieExpiryDate;
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
/*     */   public void setExpiryDate(Date expiryDate) {
/* 154 */     this.cookieExpiryDate = expiryDate;
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
/*     */   public boolean isPersistent() {
/* 166 */     return (null != this.cookieExpiryDate);
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
/*     */   public String getDomain() {
/* 178 */     return this.cookieDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDomain(String domain) {
/* 189 */     if (domain != null) {
/* 190 */       this.cookieDomain = domain.toLowerCase(Locale.ENGLISH);
/*     */     } else {
/* 192 */       this.cookieDomain = null;
/*     */     } 
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
/*     */   public String getPath() {
/* 205 */     return this.cookiePath;
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
/*     */   public void setPath(String path) {
/* 217 */     this.cookiePath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 225 */     return this.isSecure;
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
/*     */   public void setSecure(boolean secure) {
/* 241 */     this.isSecure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getPorts() {
/* 249 */     return null;
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
/*     */   public int getVersion() {
/* 263 */     return this.cookieVersion;
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
/*     */   public void setVersion(int version) {
/* 275 */     this.cookieVersion = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired(Date date) {
/* 285 */     if (date == null) {
/* 286 */       throw new IllegalArgumentException("Date may not be null");
/*     */     }
/* 288 */     return (this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= date.getTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, String value) {
/* 293 */     this.attribs.put(name, value);
/*     */   }
/*     */   
/*     */   public String getAttribute(String name) {
/* 297 */     return this.attribs.get(name);
/*     */   }
/*     */   
/*     */   public boolean containsAttribute(String name) {
/* 301 */     return (this.attribs.get(name) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 306 */     BasicClientCookie clone = (BasicClientCookie)super.clone();
/* 307 */     clone.attribs = new HashMap<String, String>(this.attribs);
/* 308 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 313 */     StringBuilder buffer = new StringBuilder();
/* 314 */     buffer.append("[version: ");
/* 315 */     buffer.append(Integer.toString(this.cookieVersion));
/* 316 */     buffer.append("]");
/* 317 */     buffer.append("[name: ");
/* 318 */     buffer.append(this.name);
/* 319 */     buffer.append("]");
/* 320 */     buffer.append("[value: ");
/* 321 */     buffer.append(this.value);
/* 322 */     buffer.append("]");
/* 323 */     buffer.append("[domain: ");
/* 324 */     buffer.append(this.cookieDomain);
/* 325 */     buffer.append("]");
/* 326 */     buffer.append("[path: ");
/* 327 */     buffer.append(this.cookiePath);
/* 328 */     buffer.append("]");
/* 329 */     buffer.append("[expiry: ");
/* 330 */     buffer.append(this.cookieExpiryDate);
/* 331 */     buffer.append("]");
/* 332 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicClientCookie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */