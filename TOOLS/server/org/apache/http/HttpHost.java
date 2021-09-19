/*     */ package org.apache.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HttpHost
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7529410654042457626L;
/*     */   public static final String DEFAULT_SCHEME_NAME = "http";
/*     */   protected final String hostname;
/*     */   protected final String lcHostname;
/*     */   protected final int port;
/*     */   protected final String schemeName;
/*     */   
/*     */   public HttpHost(String hostname, int port, String scheme) {
/*  78 */     if (hostname == null) {
/*  79 */       throw new IllegalArgumentException("Host name may not be null");
/*     */     }
/*  81 */     this.hostname = hostname;
/*  82 */     this.lcHostname = hostname.toLowerCase(Locale.ENGLISH);
/*  83 */     if (scheme != null) {
/*  84 */       this.schemeName = scheme.toLowerCase(Locale.ENGLISH);
/*     */     } else {
/*  86 */       this.schemeName = "http";
/*     */     } 
/*  88 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost(String hostname, int port) {
/*  99 */     this(hostname, port, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost(String hostname) {
/* 108 */     this(hostname, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost(HttpHost httphost) {
/* 117 */     this(httphost.hostname, httphost.port, httphost.schemeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHostName() {
/* 126 */     return this.hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 135 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/* 144 */     return this.schemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toURI() {
/* 153 */     StringBuilder buffer = new StringBuilder();
/* 154 */     buffer.append(this.schemeName);
/* 155 */     buffer.append("://");
/* 156 */     buffer.append(this.hostname);
/* 157 */     if (this.port != -1) {
/* 158 */       buffer.append(':');
/* 159 */       buffer.append(Integer.toString(this.port));
/*     */     } 
/* 161 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toHostString() {
/* 171 */     if (this.port != -1) {
/*     */       
/* 173 */       StringBuilder buffer = new StringBuilder(this.hostname.length() + 6);
/* 174 */       buffer.append(this.hostname);
/* 175 */       buffer.append(":");
/* 176 */       buffer.append(Integer.toString(this.port));
/* 177 */       return buffer.toString();
/*     */     } 
/* 179 */     return this.hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     return toURI();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 192 */     if (this == obj) return true; 
/* 193 */     if (obj instanceof HttpHost) {
/* 194 */       HttpHost that = (HttpHost)obj;
/* 195 */       return (this.lcHostname.equals(that.lcHostname) && this.port == that.port && this.schemeName.equals(that.schemeName));
/*     */     } 
/*     */ 
/*     */     
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 208 */     int hash = 17;
/* 209 */     hash = LangUtils.hashCode(hash, this.lcHostname);
/* 210 */     hash = LangUtils.hashCode(hash, this.port);
/* 211 */     hash = LangUtils.hashCode(hash, this.schemeName);
/* 212 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 217 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\HttpHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */