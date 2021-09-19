/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.EntityDeserializer;
/*     */ import org.apache.http.impl.entity.EntitySerializer;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpResponseParser;
/*     */ import org.apache.http.impl.io.HttpRequestWriter;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.HttpMessageWriter;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractHttpClientConnection
/*     */   implements HttpClientConnection
/*     */ {
/*     */   private final EntitySerializer entityserializer;
/*     */   private final EntityDeserializer entitydeserializer;
/*  80 */   private SessionInputBuffer inbuffer = null;
/*  81 */   private SessionOutputBuffer outbuffer = null;
/*  82 */   private EofSensor eofSensor = null;
/*  83 */   private HttpMessageParser<HttpResponse> responseParser = null;
/*  84 */   private HttpMessageWriter<HttpRequest> requestWriter = null;
/*  85 */   private HttpConnectionMetricsImpl metrics = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractHttpClientConnection() {
/*  97 */     this.entityserializer = createEntitySerializer();
/*  98 */     this.entitydeserializer = createEntityDeserializer();
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
/*     */   protected abstract void assertOpen() throws IllegalStateException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityDeserializer createEntityDeserializer() {
/* 120 */     return new EntityDeserializer((ContentLengthStrategy)new LaxContentLengthStrategy());
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
/*     */   protected EntitySerializer createEntitySerializer() {
/* 135 */     return new EntitySerializer((ContentLengthStrategy)new StrictContentLengthStrategy());
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
/*     */   protected HttpResponseFactory createHttpResponseFactory() {
/* 149 */     return new DefaultHttpResponseFactory();
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
/*     */   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 170 */     return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
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
/*     */   protected HttpMessageWriter<HttpRequest> createRequestWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 189 */     return (HttpMessageWriter<HttpRequest>)new HttpRequestWriter(buffer, null, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/* 198 */     return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
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
/*     */   protected void init(SessionInputBuffer inbuffer, SessionOutputBuffer outbuffer, HttpParams params) {
/* 221 */     if (inbuffer == null) {
/* 222 */       throw new IllegalArgumentException("Input session buffer may not be null");
/*     */     }
/* 224 */     if (outbuffer == null) {
/* 225 */       throw new IllegalArgumentException("Output session buffer may not be null");
/*     */     }
/* 227 */     this.inbuffer = inbuffer;
/* 228 */     this.outbuffer = outbuffer;
/* 229 */     if (inbuffer instanceof EofSensor) {
/* 230 */       this.eofSensor = (EofSensor)inbuffer;
/*     */     }
/* 232 */     this.responseParser = createResponseParser(inbuffer, createHttpResponseFactory(), params);
/*     */ 
/*     */ 
/*     */     
/* 236 */     this.requestWriter = createRequestWriter(outbuffer, params);
/*     */     
/* 238 */     this.metrics = createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 244 */     assertOpen();
/*     */     try {
/* 246 */       return this.inbuffer.isDataAvailable(timeout);
/* 247 */     } catch (SocketTimeoutException ex) {
/* 248 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 254 */     if (request == null) {
/* 255 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 257 */     assertOpen();
/* 258 */     this.requestWriter.write((HttpMessage)request);
/* 259 */     this.metrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 264 */     if (request == null) {
/* 265 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 267 */     assertOpen();
/* 268 */     if (request.getEntity() == null) {
/*     */       return;
/*     */     }
/* 271 */     this.entityserializer.serialize(this.outbuffer, (HttpMessage)request, request.getEntity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 278 */     this.outbuffer.flush();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 282 */     assertOpen();
/* 283 */     doFlush();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 288 */     assertOpen();
/* 289 */     HttpResponse response = (HttpResponse)this.responseParser.parse();
/* 290 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 291 */       this.metrics.incrementResponseCount();
/*     */     }
/* 293 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 298 */     if (response == null) {
/* 299 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 301 */     assertOpen();
/* 302 */     HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, (HttpMessage)response);
/* 303 */     response.setEntity(entity);
/*     */   }
/*     */   
/*     */   protected boolean isEof() {
/* 307 */     return (this.eofSensor != null && this.eofSensor.isEof());
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 311 */     if (!isOpen()) {
/* 312 */       return true;
/*     */     }
/* 314 */     if (isEof()) {
/* 315 */       return true;
/*     */     }
/*     */     try {
/* 318 */       this.inbuffer.isDataAvailable(1);
/* 319 */       return isEof();
/* 320 */     } catch (SocketTimeoutException ex) {
/* 321 */       return false;
/* 322 */     } catch (IOException ex) {
/* 323 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 328 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\AbstractHttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */