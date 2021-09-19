/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*     */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.BasicHttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecompressingHttpClient
/*     */   implements HttpClient
/*     */ {
/*     */   private HttpClient backend;
/*     */   private HttpRequestInterceptor acceptEncodingInterceptor;
/*     */   private HttpResponseInterceptor contentEncodingInterceptor;
/*     */   
/*     */   public DecompressingHttpClient(HttpClient backend) {
/*  87 */     this(backend, (HttpRequestInterceptor)new RequestAcceptEncoding(), (HttpResponseInterceptor)new ResponseContentEncoding());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DecompressingHttpClient(HttpClient backend, HttpRequestInterceptor requestInterceptor, HttpResponseInterceptor responseInterceptor) {
/*  93 */     this.backend = backend;
/*  94 */     this.acceptEncodingInterceptor = requestInterceptor;
/*  95 */     this.contentEncodingInterceptor = responseInterceptor;
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/*  99 */     return this.backend.getParams();
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 103 */     return this.backend.getConnectionManager();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
/* 108 */     return execute(getHttpHost(request), (HttpRequest)request, (HttpContext)null);
/*     */   }
/*     */   
/*     */   HttpHost getHttpHost(HttpUriRequest request) {
/* 112 */     URI uri = request.getURI();
/* 113 */     return URIUtils.extractHost(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
/* 118 */     return execute(getHttpHost(request), (HttpRequest)request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
/* 123 */     return execute(target, request, (HttpContext)null);
/*     */   }
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*     */     try {
/*     */       BasicHttpContext basicHttpContext;
/*     */       RequestWrapper requestWrapper;
/* 129 */       if (context == null) basicHttpContext = new BasicHttpContext();
/*     */       
/* 131 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 132 */         requestWrapper = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*     */       } else {
/* 134 */         requestWrapper = new RequestWrapper(request);
/*     */       } 
/* 136 */       this.acceptEncodingInterceptor.process((HttpRequest)requestWrapper, (HttpContext)basicHttpContext);
/* 137 */       HttpResponse response = this.backend.execute(target, (HttpRequest)requestWrapper, (HttpContext)basicHttpContext);
/*     */       try {
/* 139 */         this.contentEncodingInterceptor.process(response, (HttpContext)basicHttpContext);
/* 140 */         if (Boolean.TRUE.equals(basicHttpContext.getAttribute("http.client.response.uncompressed"))) {
/* 141 */           response.removeHeaders("Content-Length");
/* 142 */           response.removeHeaders("Content-Encoding");
/* 143 */           response.removeHeaders("Content-MD5");
/*     */         } 
/* 145 */         return response;
/* 146 */       } catch (HttpException ex) {
/* 147 */         EntityUtils.consume(response.getEntity());
/* 148 */         throw ex;
/* 149 */       } catch (IOException ex) {
/* 150 */         EntityUtils.consume(response.getEntity());
/* 151 */         throw ex;
/* 152 */       } catch (RuntimeException ex) {
/* 153 */         EntityUtils.consume(response.getEntity());
/* 154 */         throw ex;
/*     */       } 
/* 156 */     } catch (HttpException e) {
/* 157 */       throw new ClientProtocolException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 164 */     return execute(getHttpHost(request), (HttpRequest)request, responseHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/* 170 */     return execute(getHttpHost(request), (HttpRequest)request, responseHandler, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 176 */     return execute(target, request, responseHandler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/* 182 */     HttpResponse response = execute(target, request, context);
/*     */     try {
/* 184 */       return (T)responseHandler.handleResponse(response);
/*     */     } finally {
/* 186 */       HttpEntity entity = response.getEntity();
/* 187 */       if (entity != null) EntityUtils.consume(entity); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DecompressingHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */