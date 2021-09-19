/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.impl.SocketHttpClientConnection;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
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
/*     */ @NotThreadSafe
/*     */ public class DefaultClientConnection
/*     */   extends SocketHttpClientConnection
/*     */   implements OperatedClientConnection, HttpContext
/*     */ {
/*  75 */   private final Log log = LogFactory.getLog(getClass());
/*  76 */   private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
/*  77 */   private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
/*     */ 
/*     */   
/*     */   private volatile Socket socket;
/*     */ 
/*     */   
/*     */   private HttpHost targetHost;
/*     */ 
/*     */   
/*     */   private boolean connSecure;
/*     */ 
/*     */   
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */   
/*     */   private final Map<String, Object> attributes;
/*     */ 
/*     */   
/*     */   public DefaultClientConnection() {
/*  96 */     this.attributes = new HashMap<String, Object>();
/*     */   }
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 100 */     return this.targetHost;
/*     */   }
/*     */   
/*     */   public final boolean isSecure() {
/* 104 */     return this.connSecure;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Socket getSocket() {
/* 109 */     return this.socket;
/*     */   }
/*     */   
/*     */   public void opening(Socket sock, HttpHost target) throws IOException {
/* 113 */     assertNotOpen();
/* 114 */     this.socket = sock;
/* 115 */     this.targetHost = target;
/*     */ 
/*     */     
/* 118 */     if (this.shutdown) {
/* 119 */       sock.close();
/*     */       
/* 121 */       throw new InterruptedIOException("Connection already shutdown");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openCompleted(boolean secure, HttpParams params) throws IOException {
/* 126 */     assertNotOpen();
/* 127 */     if (params == null) {
/* 128 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */     
/* 131 */     this.connSecure = secure;
/* 132 */     bind(this.socket, params);
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
/*     */   public void shutdown() throws IOException {
/* 150 */     this.shutdown = true;
/*     */     try {
/* 152 */       super.shutdown();
/* 153 */       if (this.log.isDebugEnabled()) {
/* 154 */         this.log.debug("Connection " + this + " shut down");
/*     */       }
/* 156 */       Socket sock = this.socket;
/* 157 */       if (sock != null)
/* 158 */         sock.close(); 
/* 159 */     } catch (IOException ex) {
/* 160 */       this.log.debug("I/O error shutting down connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 167 */       super.close();
/* 168 */       if (this.log.isDebugEnabled()) {
/* 169 */         this.log.debug("Connection " + this + " closed");
/*     */       }
/* 171 */     } catch (IOException ex) {
/* 172 */       this.log.debug("I/O error closing connection", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 181 */     if (buffersize == -1) {
/* 182 */       buffersize = 8192;
/*     */     }
/* 184 */     SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, buffersize, params);
/*     */ 
/*     */ 
/*     */     
/* 188 */     if (this.wireLog.isDebugEnabled()) {
/* 189 */       inbuffer = new LoggingSessionInputBuffer(inbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 194 */     return inbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 202 */     if (buffersize == -1) {
/* 203 */       buffersize = 8192;
/*     */     }
/* 205 */     SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, buffersize, params);
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (this.wireLog.isDebugEnabled()) {
/* 210 */       outbuffer = new LoggingSessionOutputBuffer(outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 215 */     return outbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 224 */     return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
/* 232 */     assertOpen();
/* 233 */     if (target == null) {
/* 234 */       throw new IllegalArgumentException("Target host must not be null.");
/*     */     }
/*     */     
/* 237 */     if (params == null) {
/* 238 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */ 
/*     */     
/* 242 */     if (sock != null) {
/* 243 */       this.socket = sock;
/* 244 */       bind(sock, params);
/*     */     } 
/* 246 */     this.targetHost = target;
/* 247 */     this.connSecure = secure;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 252 */     HttpResponse response = super.receiveResponseHeader();
/* 253 */     if (this.log.isDebugEnabled()) {
/* 254 */       this.log.debug("Receiving response: " + response.getStatusLine());
/*     */     }
/* 256 */     if (this.headerLog.isDebugEnabled()) {
/* 257 */       this.headerLog.debug("<< " + response.getStatusLine().toString());
/* 258 */       Header[] headers = response.getAllHeaders();
/* 259 */       for (Header header : headers) {
/* 260 */         this.headerLog.debug("<< " + header.toString());
/*     */       }
/*     */     } 
/* 263 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 268 */     if (this.log.isDebugEnabled()) {
/* 269 */       this.log.debug("Sending request: " + request.getRequestLine());
/*     */     }
/* 271 */     super.sendRequestHeader(request);
/* 272 */     if (this.headerLog.isDebugEnabled()) {
/* 273 */       this.headerLog.debug(">> " + request.getRequestLine().toString());
/* 274 */       Header[] headers = request.getAllHeaders();
/* 275 */       for (Header header : headers) {
/* 276 */         this.headerLog.debug(">> " + header.toString());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 282 */     return this.attributes.get(id);
/*     */   }
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 286 */     return this.attributes.remove(id);
/*     */   }
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 290 */     this.attributes.put(id, obj);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\DefaultClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */