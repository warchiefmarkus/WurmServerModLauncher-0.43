/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.LangUtils;
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
/*     */ @Immutable
/*     */ public class AuthScope
/*     */ {
/*  51 */   public static final String ANY_HOST = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ANY_PORT = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final String ANY_REALM = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final String ANY_SCHEME = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final AuthScope ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String scheme;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String realm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String host;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int port;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(String host, int port, String realm, String scheme) {
/* 107 */     this.host = (host == null) ? ANY_HOST : host.toLowerCase(Locale.ENGLISH);
/* 108 */     this.port = (port < 0) ? -1 : port;
/* 109 */     this.realm = (realm == null) ? ANY_REALM : realm;
/* 110 */     this.scheme = (scheme == null) ? ANY_SCHEME : scheme.toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(HttpHost host, String realm, String schemeName) {
/* 117 */     this(host.getHostName(), host.getPort(), realm, schemeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(HttpHost host) {
/* 124 */     this(host, ANY_REALM, ANY_SCHEME);
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
/*     */   public AuthScope(String host, int port, String realm) {
/* 142 */     this(host, port, realm, ANY_SCHEME);
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
/*     */   public AuthScope(String host, int port) {
/* 157 */     this(host, port, ANY_REALM, ANY_SCHEME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(AuthScope authscope) {
/* 165 */     if (authscope == null) {
/* 166 */       throw new IllegalArgumentException("Scope may not be null");
/*     */     }
/* 168 */     this.host = authscope.getHost();
/* 169 */     this.port = authscope.getPort();
/* 170 */     this.realm = authscope.getRealm();
/* 171 */     this.scheme = authscope.getScheme();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 178 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 185 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 192 */     return this.realm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 199 */     return this.scheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int match(AuthScope that) {
/* 210 */     int factor = 0;
/* 211 */     if (LangUtils.equals(this.scheme, that.scheme)) {
/* 212 */       factor++;
/*     */     }
/* 214 */     else if (this.scheme != ANY_SCHEME && that.scheme != ANY_SCHEME) {
/* 215 */       return -1;
/*     */     } 
/*     */     
/* 218 */     if (LangUtils.equals(this.realm, that.realm)) {
/* 219 */       factor += 2;
/*     */     }
/* 221 */     else if (this.realm != ANY_REALM && that.realm != ANY_REALM) {
/* 222 */       return -1;
/*     */     } 
/*     */     
/* 225 */     if (this.port == that.port) {
/* 226 */       factor += 4;
/*     */     }
/* 228 */     else if (this.port != -1 && that.port != -1) {
/* 229 */       return -1;
/*     */     } 
/*     */     
/* 232 */     if (LangUtils.equals(this.host, that.host)) {
/* 233 */       factor += 8;
/*     */     }
/* 235 */     else if (this.host != ANY_HOST && that.host != ANY_HOST) {
/* 236 */       return -1;
/*     */     } 
/*     */     
/* 239 */     return factor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 247 */     if (o == null) {
/* 248 */       return false;
/*     */     }
/* 250 */     if (o == this) {
/* 251 */       return true;
/*     */     }
/* 253 */     if (!(o instanceof AuthScope)) {
/* 254 */       return super.equals(o);
/*     */     }
/* 256 */     AuthScope that = (AuthScope)o;
/* 257 */     return (LangUtils.equals(this.host, that.host) && this.port == that.port && LangUtils.equals(this.realm, that.realm) && LangUtils.equals(this.scheme, that.scheme));
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
/*     */   public String toString() {
/* 269 */     StringBuilder buffer = new StringBuilder();
/* 270 */     if (this.scheme != null) {
/* 271 */       buffer.append(this.scheme.toUpperCase(Locale.ENGLISH));
/* 272 */       buffer.append(' ');
/*     */     } 
/* 274 */     if (this.realm != null) {
/* 275 */       buffer.append('\'');
/* 276 */       buffer.append(this.realm);
/* 277 */       buffer.append('\'');
/*     */     } else {
/* 279 */       buffer.append("<any realm>");
/*     */     } 
/* 281 */     if (this.host != null) {
/* 282 */       buffer.append('@');
/* 283 */       buffer.append(this.host);
/* 284 */       if (this.port >= 0) {
/* 285 */         buffer.append(':');
/* 286 */         buffer.append(this.port);
/*     */       } 
/*     */     } 
/* 289 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 297 */     int hash = 17;
/* 298 */     hash = LangUtils.hashCode(hash, this.host);
/* 299 */     hash = LangUtils.hashCode(hash, this.port);
/* 300 */     hash = LangUtils.hashCode(hash, this.realm);
/* 301 */     hash = LangUtils.hashCode(hash, this.scheme);
/* 302 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\AuthScope.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */