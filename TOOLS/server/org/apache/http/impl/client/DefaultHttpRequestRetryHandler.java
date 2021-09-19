/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.HttpRequestRetryHandler;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultHttpRequestRetryHandler
/*     */   implements HttpRequestRetryHandler
/*     */ {
/*     */   private final int retryCount;
/*     */   private final boolean requestSentRetryEnabled;
/*     */   
/*     */   public DefaultHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
/*  66 */     this.retryCount = retryCount;
/*  67 */     this.requestSentRetryEnabled = requestSentRetryEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestRetryHandler() {
/*  74 */     this(3, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
/*  84 */     if (exception == null) {
/*  85 */       throw new IllegalArgumentException("Exception parameter may not be null");
/*     */     }
/*  87 */     if (context == null) {
/*  88 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*  90 */     if (executionCount > this.retryCount)
/*     */     {
/*  92 */       return false;
/*     */     }
/*  94 */     if (exception instanceof java.io.InterruptedIOException)
/*     */     {
/*  96 */       return false;
/*     */     }
/*  98 */     if (exception instanceof java.net.UnknownHostException)
/*     */     {
/* 100 */       return false;
/*     */     }
/* 102 */     if (exception instanceof java.net.ConnectException)
/*     */     {
/* 104 */       return false;
/*     */     }
/* 106 */     if (exception instanceof javax.net.ssl.SSLException)
/*     */     {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */ 
/*     */     
/* 114 */     if (requestIsAborted(request)) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (handleAsIdempotent(request))
/*     */     {
/* 120 */       return true;
/*     */     }
/*     */     
/* 123 */     Boolean b = (Boolean)context.getAttribute("http.request_sent");
/*     */     
/* 125 */     boolean sent = (b != null && b.booleanValue());
/*     */     
/* 127 */     if (!sent || this.requestSentRetryEnabled)
/*     */     {
/*     */       
/* 130 */       return true;
/*     */     }
/*     */     
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequestSentRetryEnabled() {
/* 141 */     return this.requestSentRetryEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRetryCount() {
/* 148 */     return this.retryCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleAsIdempotent(HttpRequest request) {
/* 155 */     return !(request instanceof org.apache.http.HttpEntityEnclosingRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean requestIsAborted(HttpRequest request) {
/* 162 */     HttpRequest req = request;
/* 163 */     if (request instanceof RequestWrapper) {
/* 164 */       req = ((RequestWrapper)request).getOriginal();
/*     */     }
/* 166 */     return (req instanceof HttpUriRequest && ((HttpUriRequest)req).isAborted());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultHttpRequestRetryHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */