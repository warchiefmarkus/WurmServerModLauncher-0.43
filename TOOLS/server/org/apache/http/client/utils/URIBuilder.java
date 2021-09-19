/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class URIBuilder
/*     */ {
/*     */   private String scheme;
/*     */   private String encodedSchemeSpecificPart;
/*     */   private String encodedAuthority;
/*     */   private String userInfo;
/*     */   private String encodedUserInfo;
/*     */   private String host;
/*     */   private int port;
/*     */   private String path;
/*     */   private String encodedPath;
/*     */   private String encodedQuery;
/*     */   private List<NameValuePair> queryParams;
/*     */   private String fragment;
/*     */   private String encodedFragment;
/*     */   
/*     */   public URIBuilder() {
/*  69 */     this.port = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(String string) throws URISyntaxException {
/*  80 */     digestURI(new URI(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder(URI uri) {
/*  89 */     digestURI(uri);
/*     */   }
/*     */   
/*     */   private List<NameValuePair> parseQuery(String query, Charset charset) {
/*  93 */     if (query != null && query.length() > 0) {
/*  94 */       return URLEncodedUtils.parse(query, charset);
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI build() throws URISyntaxException {
/* 103 */     return new URI(buildString());
/*     */   }
/*     */   
/*     */   private String buildString() {
/* 107 */     StringBuilder sb = new StringBuilder();
/* 108 */     if (this.scheme != null) {
/* 109 */       sb.append(this.scheme).append(':');
/*     */     }
/* 111 */     if (this.encodedSchemeSpecificPart != null) {
/* 112 */       sb.append(this.encodedSchemeSpecificPart);
/*     */     } else {
/* 114 */       if (this.encodedAuthority != null) {
/* 115 */         sb.append("//").append(this.encodedAuthority);
/* 116 */       } else if (this.host != null) {
/* 117 */         sb.append("//");
/* 118 */         if (this.encodedUserInfo != null) {
/* 119 */           sb.append(this.encodedUserInfo).append("@");
/* 120 */         } else if (this.userInfo != null) {
/* 121 */           sb.append(encodeUserInfo(this.userInfo)).append("@");
/*     */         } 
/* 123 */         if (InetAddressUtils.isIPv6Address(this.host)) {
/* 124 */           sb.append("[").append(this.host).append("]");
/*     */         } else {
/* 126 */           sb.append(this.host);
/*     */         } 
/* 128 */         if (this.port >= 0) {
/* 129 */           sb.append(":").append(this.port);
/*     */         }
/*     */       } 
/* 132 */       if (this.encodedPath != null) {
/* 133 */         sb.append(normalizePath(this.encodedPath));
/* 134 */       } else if (this.path != null) {
/* 135 */         sb.append(encodePath(normalizePath(this.path)));
/*     */       } 
/* 137 */       if (this.encodedQuery != null) {
/* 138 */         sb.append("?").append(this.encodedQuery);
/* 139 */       } else if (this.queryParams != null) {
/* 140 */         sb.append("?").append(encodeQuery(this.queryParams));
/*     */       } 
/*     */     } 
/* 143 */     if (this.encodedFragment != null) {
/* 144 */       sb.append("#").append(this.encodedFragment);
/* 145 */     } else if (this.fragment != null) {
/* 146 */       sb.append("#").append(encodeFragment(this.fragment));
/*     */     } 
/* 148 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void digestURI(URI uri) {
/* 152 */     this.scheme = uri.getScheme();
/* 153 */     this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
/* 154 */     this.encodedAuthority = uri.getRawAuthority();
/* 155 */     this.host = uri.getHost();
/* 156 */     this.port = uri.getPort();
/* 157 */     this.encodedUserInfo = uri.getRawUserInfo();
/* 158 */     this.userInfo = uri.getUserInfo();
/* 159 */     this.encodedPath = uri.getRawPath();
/* 160 */     this.path = uri.getPath();
/* 161 */     this.encodedQuery = uri.getRawQuery();
/* 162 */     this.queryParams = parseQuery(uri.getRawQuery(), Consts.UTF_8);
/* 163 */     this.encodedFragment = uri.getRawFragment();
/* 164 */     this.fragment = uri.getFragment();
/*     */   }
/*     */   
/*     */   private String encodeUserInfo(String userInfo) {
/* 168 */     return URLEncodedUtils.encUserInfo(userInfo, Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodePath(String path) {
/* 172 */     return URLEncodedUtils.encPath(path, Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeQuery(List<NameValuePair> params) {
/* 176 */     return URLEncodedUtils.format(params, Consts.UTF_8);
/*     */   }
/*     */   
/*     */   private String encodeFragment(String fragment) {
/* 180 */     return URLEncodedUtils.encFragment(fragment, Consts.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setScheme(String scheme) {
/* 187 */     this.scheme = scheme;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String userInfo) {
/* 196 */     this.userInfo = userInfo;
/* 197 */     this.encodedSchemeSpecificPart = null;
/* 198 */     this.encodedAuthority = null;
/* 199 */     this.encodedUserInfo = null;
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setUserInfo(String username, String password) {
/* 208 */     return setUserInfo(username + ':' + password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setHost(String host) {
/* 215 */     this.host = host;
/* 216 */     this.encodedSchemeSpecificPart = null;
/* 217 */     this.encodedAuthority = null;
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPort(int port) {
/* 225 */     this.port = (port < 0) ? -1 : port;
/* 226 */     this.encodedSchemeSpecificPart = null;
/* 227 */     this.encodedAuthority = null;
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setPath(String path) {
/* 235 */     this.path = path;
/* 236 */     this.encodedSchemeSpecificPart = null;
/* 237 */     this.encodedPath = null;
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder removeQuery() {
/* 245 */     this.queryParams = null;
/* 246 */     this.encodedQuery = null;
/* 247 */     this.encodedSchemeSpecificPart = null;
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setQuery(String query) {
/* 257 */     this.queryParams = parseQuery(query, Consts.UTF_8);
/* 258 */     this.encodedQuery = null;
/* 259 */     this.encodedSchemeSpecificPart = null;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder addParameter(String param, String value) {
/* 268 */     if (this.queryParams == null) {
/* 269 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 271 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 272 */     this.encodedQuery = null;
/* 273 */     this.encodedSchemeSpecificPart = null;
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setParameter(String param, String value) {
/* 282 */     if (this.queryParams == null) {
/* 283 */       this.queryParams = new ArrayList<NameValuePair>();
/*     */     }
/* 285 */     if (!this.queryParams.isEmpty()) {
/* 286 */       for (Iterator<NameValuePair> it = this.queryParams.iterator(); it.hasNext(); ) {
/* 287 */         NameValuePair nvp = it.next();
/* 288 */         if (nvp.getName().equals(param)) {
/* 289 */           it.remove();
/*     */         }
/*     */       } 
/*     */     }
/* 293 */     this.queryParams.add(new BasicNameValuePair(param, value));
/* 294 */     this.encodedQuery = null;
/* 295 */     this.encodedSchemeSpecificPart = null;
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIBuilder setFragment(String fragment) {
/* 304 */     this.fragment = fragment;
/* 305 */     this.encodedFragment = null;
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   public String getScheme() {
/* 310 */     return this.scheme;
/*     */   }
/*     */   
/*     */   public String getUserInfo() {
/* 314 */     return this.userInfo;
/*     */   }
/*     */   
/*     */   public String getHost() {
/* 318 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 322 */     return this.port;
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 326 */     return this.path;
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getQueryParams() {
/* 330 */     if (this.queryParams != null) {
/* 331 */       return new ArrayList<NameValuePair>(this.queryParams);
/*     */     }
/* 333 */     return new ArrayList<NameValuePair>();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFragment() {
/* 338 */     return this.fragment;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 343 */     return buildString();
/*     */   }
/*     */   
/*     */   private static String normalizePath(String path) {
/* 347 */     if (path == null) {
/* 348 */       return null;
/*     */     }
/* 350 */     int n = 0;
/* 351 */     for (; n < path.length() && 
/* 352 */       path.charAt(n) == '/'; n++);
/*     */ 
/*     */ 
/*     */     
/* 356 */     if (n > 1) {
/* 357 */       path = path.substring(n - 1);
/*     */     }
/* 359 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\URIBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */