/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.MalformedCookieException;
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
/*     */ @Immutable
/*     */ public class ResponseProcessCookies
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  59 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*  67 */     if (response == null) {
/*  68 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  70 */     if (context == null) {
/*  71 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */ 
/*     */     
/*  75 */     CookieSpec cookieSpec = (CookieSpec)context.getAttribute("http.cookie-spec");
/*     */     
/*  77 */     if (cookieSpec == null) {
/*  78 */       this.log.debug("Cookie spec not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*  82 */     CookieStore cookieStore = (CookieStore)context.getAttribute("http.cookie-store");
/*     */     
/*  84 */     if (cookieStore == null) {
/*  85 */       this.log.debug("Cookie store not specified in HTTP context");
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     CookieOrigin cookieOrigin = (CookieOrigin)context.getAttribute("http.cookie-origin");
/*     */     
/*  91 */     if (cookieOrigin == null) {
/*  92 */       this.log.debug("Cookie origin not specified in HTTP context");
/*     */       return;
/*     */     } 
/*  95 */     HeaderIterator it = response.headerIterator("Set-Cookie");
/*  96 */     processCookies(it, cookieSpec, cookieOrigin, cookieStore);
/*     */ 
/*     */     
/*  99 */     if (cookieSpec.getVersion() > 0) {
/*     */ 
/*     */       
/* 102 */       it = response.headerIterator("Set-Cookie2");
/* 103 */       processCookies(it, cookieSpec, cookieOrigin, cookieStore);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCookies(HeaderIterator iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
/* 112 */     while (iterator.hasNext()) {
/* 113 */       Header header = iterator.nextHeader();
/*     */       try {
/* 115 */         List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
/* 116 */         for (Cookie cookie : cookies) {
/*     */           try {
/* 118 */             cookieSpec.validate(cookie, cookieOrigin);
/* 119 */             cookieStore.addCookie(cookie);
/*     */             
/* 121 */             if (this.log.isDebugEnabled()) {
/* 122 */               this.log.debug("Cookie accepted: \"" + cookie + "\". ");
/*     */             }
/*     */           }
/* 125 */           catch (MalformedCookieException ex) {
/* 126 */             if (this.log.isWarnEnabled()) {
/* 127 */               this.log.warn("Cookie rejected: \"" + cookie + "\". " + ex.getMessage());
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 132 */       } catch (MalformedCookieException ex) {
/* 133 */         if (this.log.isWarnEnabled())
/* 134 */           this.log.warn("Invalid cookie header: \"" + header + "\". " + ex.getMessage()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\ResponseProcessCookies.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */