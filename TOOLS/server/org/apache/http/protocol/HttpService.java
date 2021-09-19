/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.HttpServerConnection;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.params.DefaultedHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.EncodingUtils;
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
/*     */ @Immutable
/*     */ public class HttpService
/*     */ {
/*  77 */   private volatile HttpParams params = null;
/*  78 */   private volatile HttpProcessor processor = null;
/*  79 */   private volatile HttpRequestHandlerResolver handlerResolver = null;
/*  80 */   private volatile ConnectionReuseStrategy connStrategy = null;
/*  81 */   private volatile HttpResponseFactory responseFactory = null;
/*  82 */   private volatile HttpExpectationVerifier expectationVerifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpExpectationVerifier expectationVerifier, HttpParams params) {
/* 104 */     if (processor == null) {
/* 105 */       throw new IllegalArgumentException("HTTP processor may not be null");
/*     */     }
/* 107 */     if (connStrategy == null) {
/* 108 */       throw new IllegalArgumentException("Connection reuse strategy may not be null");
/*     */     }
/* 110 */     if (responseFactory == null) {
/* 111 */       throw new IllegalArgumentException("Response factory may not be null");
/*     */     }
/* 113 */     if (params == null) {
/* 114 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 116 */     this.processor = processor;
/* 117 */     this.connStrategy = connStrategy;
/* 118 */     this.responseFactory = responseFactory;
/* 119 */     this.handlerResolver = handlerResolver;
/* 120 */     this.expectationVerifier = expectationVerifier;
/* 121 */     this.params = params;
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
/*     */ 
/*     */   
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpParams params) {
/* 141 */     this(processor, connStrategy, responseFactory, handlerResolver, null, params);
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
/*     */   @Deprecated
/*     */   public HttpService(HttpProcessor proc, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory) {
/* 160 */     setHttpProcessor(proc);
/* 161 */     setConnReuseStrategy(connStrategy);
/* 162 */     setResponseFactory(responseFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHttpProcessor(HttpProcessor processor) {
/* 170 */     if (processor == null) {
/* 171 */       throw new IllegalArgumentException("HTTP processor may not be null");
/*     */     }
/* 173 */     this.processor = processor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setConnReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 181 */     if (connStrategy == null) {
/* 182 */       throw new IllegalArgumentException("Connection reuse strategy may not be null");
/*     */     }
/* 184 */     this.connStrategy = connStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setResponseFactory(HttpResponseFactory responseFactory) {
/* 192 */     if (responseFactory == null) {
/* 193 */       throw new IllegalArgumentException("Response factory may not be null");
/*     */     }
/* 195 */     this.responseFactory = responseFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setParams(HttpParams params) {
/* 203 */     this.params = params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
/* 211 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 219 */     this.expectationVerifier = expectationVerifier;
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/* 223 */     return this.params;
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
/*     */   public void handleRequest(HttpServerConnection conn, HttpContext context) throws IOException, HttpException {
/* 240 */     context.setAttribute("http.connection", conn);
/*     */     
/* 242 */     HttpResponse response = null;
/*     */ 
/*     */     
/*     */     try {
/* 246 */       HttpRequest request = conn.receiveRequestHeader();
/* 247 */       request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));
/*     */ 
/*     */       
/* 250 */       if (request instanceof HttpEntityEnclosingRequest)
/*     */       {
/* 252 */         if (((HttpEntityEnclosingRequest)request).expectContinue()) {
/* 253 */           response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 100, context);
/*     */           
/* 255 */           response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */           
/* 258 */           if (this.expectationVerifier != null) {
/*     */             try {
/* 260 */               this.expectationVerifier.verify(request, response, context);
/* 261 */             } catch (HttpException ex) {
/* 262 */               response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */               
/* 264 */               response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */               
/* 266 */               handleException(ex, response);
/*     */             } 
/*     */           }
/* 269 */           if (response.getStatusLine().getStatusCode() < 200) {
/*     */ 
/*     */             
/* 272 */             conn.sendResponseHeader(response);
/* 273 */             conn.flush();
/* 274 */             response = null;
/* 275 */             conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
/*     */           } 
/*     */         } else {
/* 278 */           conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
/*     */         } 
/*     */       }
/*     */       
/* 282 */       context.setAttribute("http.request", request);
/*     */       
/* 284 */       if (response == null) {
/* 285 */         response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 200, context);
/*     */         
/* 287 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */         
/* 289 */         this.processor.process(request, context);
/* 290 */         doService(request, response, context);
/*     */       } 
/*     */ 
/*     */       
/* 294 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 295 */         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 296 */         EntityUtils.consume(entity);
/*     */       }
/*     */     
/* 299 */     } catch (HttpException ex) {
/* 300 */       response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */ 
/*     */       
/* 303 */       response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */       
/* 305 */       handleException(ex, response);
/*     */     } 
/*     */     
/* 308 */     context.setAttribute("http.response", response);
/*     */     
/* 310 */     this.processor.process(response, context);
/* 311 */     conn.sendResponseHeader(response);
/* 312 */     conn.sendResponseEntity(response);
/* 313 */     conn.flush();
/*     */     
/* 315 */     if (!this.connStrategy.keepAlive(response, context)) {
/* 316 */       conn.close();
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
/*     */   protected void handleException(HttpException ex, HttpResponse response) {
/* 329 */     if (ex instanceof org.apache.http.MethodNotSupportedException) {
/* 330 */       response.setStatusCode(501);
/* 331 */     } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
/* 332 */       response.setStatusCode(505);
/* 333 */     } else if (ex instanceof org.apache.http.ProtocolException) {
/* 334 */       response.setStatusCode(400);
/*     */     } else {
/* 336 */       response.setStatusCode(500);
/*     */     } 
/* 338 */     String message = ex.getMessage();
/* 339 */     if (message == null) {
/* 340 */       message = ex.toString();
/*     */     }
/* 342 */     byte[] msg = EncodingUtils.getAsciiBytes(message);
/* 343 */     ByteArrayEntity entity = new ByteArrayEntity(msg);
/* 344 */     entity.setContentType("text/plain; charset=US-ASCII");
/* 345 */     response.setEntity((HttpEntity)entity);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doService(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 369 */     HttpRequestHandler handler = null;
/* 370 */     if (this.handlerResolver != null) {
/* 371 */       String requestURI = request.getRequestLine().getUri();
/* 372 */       handler = this.handlerResolver.lookup(requestURI);
/*     */     } 
/* 374 */     if (handler != null) {
/* 375 */       handler.handle(request, response, context);
/*     */     } else {
/* 377 */       response.setStatusCode(501);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\HttpService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */