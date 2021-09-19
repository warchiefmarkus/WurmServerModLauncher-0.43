/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpServerConnection;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.EntityDeserializer;
/*     */ import org.apache.http.impl.entity.EntitySerializer;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.DefaultHttpRequestParser;
/*     */ import org.apache.http.impl.io.HttpResponseWriter;
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
/*     */ public abstract class AbstractHttpServerConnection
/*     */   implements HttpServerConnection
/*     */ {
/*     */   private final EntitySerializer entityserializer;
/*     */   private final EntityDeserializer entitydeserializer;
/*  80 */   private SessionInputBuffer inbuffer = null;
/*  81 */   private SessionOutputBuffer outbuffer = null;
/*  82 */   private EofSensor eofSensor = null;
/*  83 */   private HttpMessageParser<HttpRequest> requestParser = null;
/*  84 */   private HttpMessageWriter<HttpResponse> responseWriter = null;
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
/*     */   public AbstractHttpServerConnection() {
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
/* 120 */     return new EntityDeserializer((ContentLengthStrategy)new DisallowIdentityContentLengthStrategy((ContentLengthStrategy)new LaxContentLengthStrategy(0)));
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
/*     */   protected EntitySerializer createEntitySerializer() {
/* 136 */     return new EntitySerializer((ContentLengthStrategy)new StrictContentLengthStrategy());
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
/*     */   protected HttpRequestFactory createHttpRequestFactory() {
/* 150 */     return new DefaultHttpRequestFactory();
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
/*     */   protected HttpMessageParser<HttpRequest> createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
/* 171 */     return (HttpMessageParser<HttpRequest>)new DefaultHttpRequestParser(buffer, null, requestFactory, params);
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
/*     */   protected HttpMessageWriter<HttpResponse> createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 190 */     return (HttpMessageWriter<HttpResponse>)new HttpResponseWriter(buffer, null, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/* 199 */     return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
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
/* 222 */     if (inbuffer == null) {
/* 223 */       throw new IllegalArgumentException("Input session buffer may not be null");
/*     */     }
/* 225 */     if (outbuffer == null) {
/* 226 */       throw new IllegalArgumentException("Output session buffer may not be null");
/*     */     }
/* 228 */     this.inbuffer = inbuffer;
/* 229 */     this.outbuffer = outbuffer;
/* 230 */     if (inbuffer instanceof EofSensor) {
/* 231 */       this.eofSensor = (EofSensor)inbuffer;
/*     */     }
/* 233 */     this.requestParser = createRequestParser(inbuffer, createHttpRequestFactory(), params);
/*     */ 
/*     */ 
/*     */     
/* 237 */     this.responseWriter = createResponseWriter(outbuffer, params);
/*     */     
/* 239 */     this.metrics = createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest receiveRequestHeader() throws HttpException, IOException {
/* 246 */     assertOpen();
/* 247 */     HttpRequest request = (HttpRequest)this.requestParser.parse();
/* 248 */     this.metrics.incrementRequestCount();
/* 249 */     return request;
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 254 */     if (request == null) {
/* 255 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 257 */     assertOpen();
/* 258 */     HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, (HttpMessage)request);
/* 259 */     request.setEntity(entity);
/*     */   }
/*     */   
/*     */   protected void doFlush() throws IOException {
/* 263 */     this.outbuffer.flush();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 267 */     assertOpen();
/* 268 */     doFlush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendResponseHeader(HttpResponse response) throws HttpException, IOException {
/* 273 */     if (response == null) {
/* 274 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 276 */     assertOpen();
/* 277 */     this.responseWriter.write((HttpMessage)response);
/* 278 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 279 */       this.metrics.incrementResponseCount();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 285 */     if (response.getEntity() == null) {
/*     */       return;
/*     */     }
/* 288 */     this.entityserializer.serialize(this.outbuffer, (HttpMessage)response, response.getEntity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEof() {
/* 295 */     return (this.eofSensor != null && this.eofSensor.isEof());
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 299 */     if (!isOpen()) {
/* 300 */       return true;
/*     */     }
/* 302 */     if (isEof()) {
/* 303 */       return true;
/*     */     }
/*     */     try {
/* 306 */       this.inbuffer.isDataAvailable(1);
/* 307 */       return isEof();
/* 308 */     } catch (IOException ex) {
/* 309 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 314 */     return this.metrics;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\AbstractHttpServerConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */