/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.params.HttpClientParams;
/*     */ import org.apache.http.conn.HttpRoutedConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.CookieSpecRegistry;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RequestAddCookies
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  78 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/*     */     URI requestURI;
/*  86 */     if (request == null) {
/*  87 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  89 */     if (context == null) {
/*  90 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  93 */     String method = request.getRequestLine().getMethod();
/*  94 */     if (method.equalsIgnoreCase("CONNECT")) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  99 */     CookieStore cookieStore = (CookieStore)context.getAttribute("http.cookie-store");
/*     */     
/* 101 */     if (cookieStore == null) {
/* 102 */       this.log.debug("Cookie store not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 107 */     CookieSpecRegistry registry = (CookieSpecRegistry)context.getAttribute("http.cookiespec-registry");
/*     */     
/* 109 */     if (registry == null) {
/* 110 */       this.log.debug("CookieSpec registry not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 115 */     HttpHost targetHost = (HttpHost)context.getAttribute("http.target_host");
/*     */     
/* 117 */     if (targetHost == null) {
/* 118 */       this.log.debug("Target host not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 123 */     HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
/*     */     
/* 125 */     if (conn == null) {
/* 126 */       this.log.debug("HTTP connection not set in the context");
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     String policy = HttpClientParams.getCookiePolicy(request.getParams());
/* 131 */     if (this.log.isDebugEnabled()) {
/* 132 */       this.log.debug("CookieSpec selected: " + policy);
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (request instanceof HttpUriRequest) {
/* 137 */       requestURI = ((HttpUriRequest)request).getURI();
/*     */     } else {
/*     */       try {
/* 140 */         requestURI = new URI(request.getRequestLine().getUri());
/* 141 */       } catch (URISyntaxException ex) {
/* 142 */         throw new ProtocolException("Invalid request URI: " + request.getRequestLine().getUri(), ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 147 */     String hostName = targetHost.getHostName();
/* 148 */     int port = targetHost.getPort();
/* 149 */     if (port < 0) {
/* 150 */       HttpRoute route = conn.getRoute();
/* 151 */       if (route.getHopCount() == 1) {
/* 152 */         port = conn.getRemotePort();
/*     */       }
/*     */       else {
/*     */         
/* 156 */         String scheme = targetHost.getSchemeName();
/* 157 */         if (scheme.equalsIgnoreCase("http")) {
/* 158 */           port = 80;
/* 159 */         } else if (scheme.equalsIgnoreCase("https")) {
/* 160 */           port = 443;
/*     */         } else {
/* 162 */           port = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     CookieOrigin cookieOrigin = new CookieOrigin(hostName, port, requestURI.getPath(), conn.isSecure());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     CookieSpec cookieSpec = registry.getCookieSpec(policy, request.getParams());
/*     */     
/* 176 */     List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
/*     */     
/* 178 */     List<Cookie> matchedCookies = new ArrayList<Cookie>();
/* 179 */     Date now = new Date();
/* 180 */     for (Cookie cookie : cookies) {
/* 181 */       if (!cookie.isExpired(now)) {
/* 182 */         if (cookieSpec.match(cookie, cookieOrigin)) {
/* 183 */           if (this.log.isDebugEnabled()) {
/* 184 */             this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
/*     */           }
/* 186 */           matchedCookies.add(cookie);
/*     */         }  continue;
/*     */       } 
/* 189 */       if (this.log.isDebugEnabled()) {
/* 190 */         this.log.debug("Cookie " + cookie + " expired");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 195 */     if (!matchedCookies.isEmpty()) {
/* 196 */       List<Header> headers = cookieSpec.formatCookies(matchedCookies);
/* 197 */       for (Header header : headers) {
/* 198 */         request.addHeader(header);
/*     */       }
/*     */     } 
/*     */     
/* 202 */     int ver = cookieSpec.getVersion();
/* 203 */     if (ver > 0) {
/* 204 */       boolean needVersionHeader = false;
/* 205 */       for (Cookie cookie : matchedCookies) {
/* 206 */         if (ver != cookie.getVersion() || !(cookie instanceof org.apache.http.cookie.SetCookie2)) {
/* 207 */           needVersionHeader = true;
/*     */         }
/*     */       } 
/*     */       
/* 211 */       if (needVersionHeader) {
/* 212 */         Header header = cookieSpec.getVersionHeader();
/* 213 */         if (header != null)
/*     */         {
/* 215 */           request.addHeader(header);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 222 */     context.setAttribute("http.cookie-spec", cookieSpec);
/* 223 */     context.setAttribute("http.cookie-origin", cookieOrigin);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\RequestAddCookies.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */