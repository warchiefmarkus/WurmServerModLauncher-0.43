/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ResponseConnControl
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*  60 */     if (response == null) {
/*  61 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/*  63 */     if (context == null) {
/*  64 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  67 */     int status = response.getStatusLine().getStatusCode();
/*  68 */     if (status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 503 || status == 501) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       response.setHeader("Connection", "Close");
/*     */       return;
/*     */     } 
/*  78 */     Header explicit = response.getFirstHeader("Connection");
/*  79 */     if (explicit != null && "Close".equalsIgnoreCase(explicit.getValue())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     HttpEntity entity = response.getEntity();
/*  86 */     if (entity != null) {
/*  87 */       ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  88 */       if (entity.getContentLength() < 0L && (!entity.isChunked() || ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0))) {
/*     */         
/*  90 */         response.setHeader("Connection", "Close");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  95 */     HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */     
/*  97 */     if (request != null) {
/*  98 */       Header header = request.getFirstHeader("Connection");
/*  99 */       if (header != null) {
/* 100 */         response.setHeader("Connection", header.getValue());
/* 101 */       } else if (request.getProtocolVersion().lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 102 */         response.setHeader("Connection", "Close");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\ResponseConnControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */