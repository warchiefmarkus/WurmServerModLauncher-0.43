/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.URI;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class AutoRetryHttpClient
/*     */   implements HttpClient
/*     */ {
/*     */   private final HttpClient backend;
/*     */   private final ServiceUnavailableRetryStrategy retryStrategy;
/*  62 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy) {
/*  67 */     if (client == null) {
/*  68 */       throw new IllegalArgumentException("HttpClient may not be null");
/*     */     }
/*  70 */     if (retryStrategy == null) {
/*  71 */       throw new IllegalArgumentException("ServiceUnavailableRetryStrategy may not be null");
/*     */     }
/*     */     
/*  74 */     this.backend = client;
/*  75 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient() {
/*  84 */     this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
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
/*     */   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config) {
/*  96 */     this(new DefaultHttpClient(), config);
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
/*     */   public AutoRetryHttpClient(HttpClient client) {
/* 108 */     this(client, new DefaultServiceUnavailableRetryStrategy());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
/* 113 */     HttpContext defaultContext = null;
/* 114 */     return execute(target, request, defaultContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 119 */     return execute(target, request, responseHandler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 125 */     HttpResponse resp = execute(target, request, context);
/* 126 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request) throws IOException {
/* 130 */     HttpContext context = null;
/* 131 */     return execute(request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
/* 136 */     URI uri = request.getURI();
/* 137 */     HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */     
/* 139 */     return execute(httpHost, (HttpRequest)request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 144 */     return execute(request, responseHandler, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 150 */     HttpResponse resp = execute(request, context);
/* 151 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
/* 156 */     for (int c = 1;; c++) {
/* 157 */       HttpResponse response = this.backend.execute(target, request, context);
/*     */       try {
/* 159 */         if (this.retryStrategy.retryRequest(response, c, context)) {
/* 160 */           EntityUtils.consume(response.getEntity());
/* 161 */           long nextInterval = this.retryStrategy.getRetryInterval();
/*     */           try {
/* 163 */             this.log.trace("Wait for " + nextInterval);
/* 164 */             Thread.sleep(nextInterval);
/* 165 */           } catch (InterruptedException e) {
/* 166 */             Thread.currentThread().interrupt();
/* 167 */             throw new InterruptedIOException();
/*     */           } 
/*     */         } else {
/* 170 */           return response;
/*     */         } 
/* 172 */       } catch (RuntimeException ex) {
/*     */         try {
/* 174 */           EntityUtils.consume(response.getEntity());
/* 175 */         } catch (IOException ioex) {
/* 176 */           this.log.warn("I/O error consuming response content", ioex);
/*     */         } 
/* 178 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 184 */     return this.backend.getConnectionManager();
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/* 188 */     return this.backend.getParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AutoRetryHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */