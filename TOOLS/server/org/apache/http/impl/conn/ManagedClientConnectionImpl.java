/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @NotThreadSafe
/*     */ class ManagedClientConnectionImpl
/*     */   implements ManagedClientConnection
/*     */ {
/*     */   private final ClientConnectionManager manager;
/*     */   private final ClientConnectionOperator operator;
/*     */   private volatile HttpPoolEntry poolEntry;
/*     */   private volatile boolean reusable;
/*     */   private volatile long duration;
/*     */   
/*     */   ManagedClientConnectionImpl(ClientConnectionManager manager, ClientConnectionOperator operator, HttpPoolEntry entry) {
/*  68 */     if (manager == null) {
/*  69 */       throw new IllegalArgumentException("Connection manager may not be null");
/*     */     }
/*  71 */     if (operator == null) {
/*  72 */       throw new IllegalArgumentException("Connection operator may not be null");
/*     */     }
/*  74 */     if (entry == null) {
/*  75 */       throw new IllegalArgumentException("HTTP pool entry may not be null");
/*     */     }
/*  77 */     this.manager = manager;
/*  78 */     this.operator = operator;
/*  79 */     this.poolEntry = entry;
/*  80 */     this.reusable = false;
/*  81 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   HttpPoolEntry getPoolEntry() {
/*  85 */     return this.poolEntry;
/*     */   }
/*     */   
/*     */   HttpPoolEntry detach() {
/*  89 */     HttpPoolEntry local = this.poolEntry;
/*  90 */     this.poolEntry = null;
/*  91 */     return local;
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getManager() {
/*  95 */     return this.manager;
/*     */   }
/*     */   
/*     */   private OperatedClientConnection getConnection() {
/*  99 */     HttpPoolEntry local = this.poolEntry;
/* 100 */     if (local == null) {
/* 101 */       return null;
/*     */     }
/* 103 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private OperatedClientConnection ensureConnection() {
/* 107 */     HttpPoolEntry local = this.poolEntry;
/* 108 */     if (local == null) {
/* 109 */       throw new ConnectionShutdownException();
/*     */     }
/* 111 */     return (OperatedClientConnection)local.getConnection();
/*     */   }
/*     */   
/*     */   private HttpPoolEntry ensurePoolEntry() {
/* 115 */     HttpPoolEntry local = this.poolEntry;
/* 116 */     if (local == null) {
/* 117 */       throw new ConnectionShutdownException();
/*     */     }
/* 119 */     return local;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 123 */     HttpPoolEntry local = this.poolEntry;
/* 124 */     if (local != null) {
/* 125 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 126 */       local.getTracker().reset();
/* 127 */       conn.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 132 */     HttpPoolEntry local = this.poolEntry;
/* 133 */     if (local != null) {
/* 134 */       OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
/* 135 */       local.getTracker().reset();
/* 136 */       conn.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 141 */     OperatedClientConnection conn = getConnection();
/* 142 */     if (conn != null) {
/* 143 */       return conn.isOpen();
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 150 */     OperatedClientConnection conn = getConnection();
/* 151 */     if (conn != null) {
/* 152 */       return conn.isStale();
/*     */     }
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 159 */     OperatedClientConnection conn = ensureConnection();
/* 160 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 164 */     OperatedClientConnection conn = ensureConnection();
/* 165 */     return conn.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 169 */     OperatedClientConnection conn = ensureConnection();
/* 170 */     return conn.getMetrics();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 174 */     OperatedClientConnection conn = ensureConnection();
/* 175 */     conn.flush();
/*     */   }
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 179 */     OperatedClientConnection conn = ensureConnection();
/* 180 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 185 */     OperatedClientConnection conn = ensureConnection();
/* 186 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 190 */     OperatedClientConnection conn = ensureConnection();
/* 191 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 196 */     OperatedClientConnection conn = ensureConnection();
/* 197 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 202 */     OperatedClientConnection conn = ensureConnection();
/* 203 */     conn.sendRequestHeader(request);
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 207 */     OperatedClientConnection conn = ensureConnection();
/* 208 */     return conn.getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 212 */     OperatedClientConnection conn = ensureConnection();
/* 213 */     return conn.getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 217 */     OperatedClientConnection conn = ensureConnection();
/* 218 */     return conn.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 222 */     OperatedClientConnection conn = ensureConnection();
/* 223 */     return conn.getRemotePort();
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 227 */     OperatedClientConnection conn = ensureConnection();
/* 228 */     return conn.isSecure();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 232 */     OperatedClientConnection conn = ensureConnection();
/* 233 */     SSLSession result = null;
/* 234 */     Socket sock = conn.getSocket();
/* 235 */     if (sock instanceof SSLSocket) {
/* 236 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 238 */     return result;
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 242 */     OperatedClientConnection conn = ensureConnection();
/* 243 */     if (conn instanceof HttpContext) {
/* 244 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 251 */     OperatedClientConnection conn = ensureConnection();
/* 252 */     if (conn instanceof HttpContext) {
/* 253 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 260 */     OperatedClientConnection conn = ensureConnection();
/* 261 */     if (conn instanceof HttpContext) {
/* 262 */       ((HttpContext)conn).setAttribute(id, obj);
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 267 */     HttpPoolEntry local = ensurePoolEntry();
/* 268 */     return local.getEffectiveRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 275 */     if (route == null) {
/* 276 */       throw new IllegalArgumentException("Route may not be null");
/*     */     }
/* 278 */     if (params == null) {
/* 279 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*     */     
/* 282 */     synchronized (this) {
/* 283 */       if (this.poolEntry == null) {
/* 284 */         throw new ConnectionShutdownException();
/*     */       }
/* 286 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 287 */       if (tracker.isConnected()) {
/* 288 */         throw new IllegalStateException("Connection already open");
/*     */       }
/* 290 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 293 */     HttpHost proxy = route.getProxyHost();
/* 294 */     this.operator.openConnection(conn, (proxy != null) ? proxy : route.getTargetHost(), route.getLocalAddress(), context, params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     synchronized (this) {
/* 301 */       if (this.poolEntry == null) {
/* 302 */         throw new InterruptedIOException();
/*     */       }
/* 304 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 305 */       if (proxy == null) {
/* 306 */         tracker.connectTarget(conn.isSecure());
/*     */       } else {
/* 308 */         tracker.connectProxy(proxy, conn.isSecure());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 315 */     if (params == null) {
/* 316 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*     */ 
/*     */     
/* 320 */     synchronized (this) {
/* 321 */       if (this.poolEntry == null) {
/* 322 */         throw new ConnectionShutdownException();
/*     */       }
/* 324 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 325 */       if (!tracker.isConnected()) {
/* 326 */         throw new IllegalStateException("Connection not open");
/*     */       }
/* 328 */       if (tracker.isTunnelled()) {
/* 329 */         throw new IllegalStateException("Connection is already tunnelled");
/*     */       }
/* 331 */       target = tracker.getTargetHost();
/* 332 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 335 */     conn.update(null, target, secure, params);
/*     */     
/* 337 */     synchronized (this) {
/* 338 */       if (this.poolEntry == null) {
/* 339 */         throw new InterruptedIOException();
/*     */       }
/* 341 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 342 */       tracker.tunnelTarget(secure);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/*     */     OperatedClientConnection conn;
/* 348 */     if (next == null) {
/* 349 */       throw new IllegalArgumentException("Next proxy amy not be null");
/*     */     }
/* 351 */     if (params == null) {
/* 352 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*     */     
/* 355 */     synchronized (this) {
/* 356 */       if (this.poolEntry == null) {
/* 357 */         throw new ConnectionShutdownException();
/*     */       }
/* 359 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 360 */       if (!tracker.isConnected()) {
/* 361 */         throw new IllegalStateException("Connection not open");
/*     */       }
/* 363 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/*     */     
/* 366 */     conn.update(null, next, secure, params);
/*     */     
/* 368 */     synchronized (this) {
/* 369 */       if (this.poolEntry == null) {
/* 370 */         throw new InterruptedIOException();
/*     */       }
/* 372 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 373 */       tracker.tunnelProxy(next, secure);
/*     */     } 
/*     */   }
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/*     */     HttpHost target;
/*     */     OperatedClientConnection conn;
/* 379 */     if (params == null) {
/* 380 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*     */ 
/*     */     
/* 384 */     synchronized (this) {
/* 385 */       if (this.poolEntry == null) {
/* 386 */         throw new ConnectionShutdownException();
/*     */       }
/* 388 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 389 */       if (!tracker.isConnected()) {
/* 390 */         throw new IllegalStateException("Connection not open");
/*     */       }
/* 392 */       if (!tracker.isTunnelled()) {
/* 393 */         throw new IllegalStateException("Protocol layering without a tunnel not supported");
/*     */       }
/* 395 */       if (tracker.isLayered()) {
/* 396 */         throw new IllegalStateException("Multiple protocol layering not supported");
/*     */       }
/* 398 */       target = tracker.getTargetHost();
/* 399 */       conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */     } 
/* 401 */     this.operator.updateSecureConnection(conn, target, context, params);
/*     */     
/* 403 */     synchronized (this) {
/* 404 */       if (this.poolEntry == null) {
/* 405 */         throw new InterruptedIOException();
/*     */       }
/* 407 */       RouteTracker tracker = this.poolEntry.getTracker();
/* 408 */       tracker.layerProtocol(conn.isSecure());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 413 */     HttpPoolEntry local = ensurePoolEntry();
/* 414 */     return local.getState();
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 418 */     HttpPoolEntry local = ensurePoolEntry();
/* 419 */     local.setState(state);
/*     */   }
/*     */   
/*     */   public void markReusable() {
/* 423 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void unmarkReusable() {
/* 427 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 431 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 435 */     if (duration > 0L) {
/* 436 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 438 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseConnection() {
/* 443 */     synchronized (this) {
/* 444 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 447 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 448 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void abortConnection() {
/* 453 */     synchronized (this) {
/* 454 */       if (this.poolEntry == null) {
/*     */         return;
/*     */       }
/* 457 */       this.reusable = false;
/* 458 */       OperatedClientConnection conn = (OperatedClientConnection)this.poolEntry.getConnection();
/*     */       try {
/* 460 */         conn.shutdown();
/* 461 */       } catch (IOException ignore) {}
/*     */       
/* 463 */       this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/* 464 */       this.poolEntry = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\ManagedClientConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */