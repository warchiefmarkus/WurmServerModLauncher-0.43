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
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractClientConnAdapter
/*     */   implements ManagedClientConnection, HttpContext
/*     */ {
/*     */   private final ClientConnectionManager connManager;
/*     */   private volatile OperatedClientConnection wrappedConnection;
/*     */   private volatile boolean markedReusable;
/*     */   private volatile boolean released;
/*     */   private volatile long duration;
/*     */   
/*     */   protected AbstractClientConnAdapter(ClientConnectionManager mgr, OperatedClientConnection conn) {
/* 104 */     this.connManager = mgr;
/* 105 */     this.wrappedConnection = conn;
/* 106 */     this.markedReusable = false;
/* 107 */     this.released = false;
/* 108 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void detach() {
/* 116 */     this.wrappedConnection = null;
/* 117 */     this.duration = Long.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected OperatedClientConnection getWrappedConnection() {
/* 121 */     return this.wrappedConnection;
/*     */   }
/*     */   
/*     */   protected ClientConnectionManager getManager() {
/* 125 */     return this.connManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertNotAborted() throws InterruptedIOException {
/* 132 */     if (isReleased()) {
/* 133 */       throw new InterruptedIOException("Connection has been shut down");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReleased() {
/* 142 */     return this.released;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertValid(OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
/* 153 */     if (isReleased() || wrappedConn == null) {
/* 154 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 159 */     OperatedClientConnection conn = getWrappedConnection();
/* 160 */     if (conn == null) {
/* 161 */       return false;
/*     */     }
/* 163 */     return conn.isOpen();
/*     */   }
/*     */   
/*     */   public boolean isStale() {
/* 167 */     if (isReleased())
/* 168 */       return true; 
/* 169 */     OperatedClientConnection conn = getWrappedConnection();
/* 170 */     if (conn == null) {
/* 171 */       return true;
/*     */     }
/* 173 */     return conn.isStale();
/*     */   }
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 177 */     OperatedClientConnection conn = getWrappedConnection();
/* 178 */     assertValid(conn);
/* 179 */     conn.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   public int getSocketTimeout() {
/* 183 */     OperatedClientConnection conn = getWrappedConnection();
/* 184 */     assertValid(conn);
/* 185 */     return conn.getSocketTimeout();
/*     */   }
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 189 */     OperatedClientConnection conn = getWrappedConnection();
/* 190 */     assertValid(conn);
/* 191 */     return conn.getMetrics();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 195 */     OperatedClientConnection conn = getWrappedConnection();
/* 196 */     assertValid(conn);
/* 197 */     conn.flush();
/*     */   }
/*     */   
/*     */   public boolean isResponseAvailable(int timeout) throws IOException {
/* 201 */     OperatedClientConnection conn = getWrappedConnection();
/* 202 */     assertValid(conn);
/* 203 */     return conn.isResponseAvailable(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
/* 208 */     OperatedClientConnection conn = getWrappedConnection();
/* 209 */     assertValid(conn);
/* 210 */     unmarkReusable();
/* 211 */     conn.receiveResponseEntity(response);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 216 */     OperatedClientConnection conn = getWrappedConnection();
/* 217 */     assertValid(conn);
/* 218 */     unmarkReusable();
/* 219 */     return conn.receiveResponseHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
/* 224 */     OperatedClientConnection conn = getWrappedConnection();
/* 225 */     assertValid(conn);
/* 226 */     unmarkReusable();
/* 227 */     conn.sendRequestEntity(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
/* 232 */     OperatedClientConnection conn = getWrappedConnection();
/* 233 */     assertValid(conn);
/* 234 */     unmarkReusable();
/* 235 */     conn.sendRequestHeader(request);
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 239 */     OperatedClientConnection conn = getWrappedConnection();
/* 240 */     assertValid(conn);
/* 241 */     return conn.getLocalAddress();
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 245 */     OperatedClientConnection conn = getWrappedConnection();
/* 246 */     assertValid(conn);
/* 247 */     return conn.getLocalPort();
/*     */   }
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 251 */     OperatedClientConnection conn = getWrappedConnection();
/* 252 */     assertValid(conn);
/* 253 */     return conn.getRemoteAddress();
/*     */   }
/*     */   
/*     */   public int getRemotePort() {
/* 257 */     OperatedClientConnection conn = getWrappedConnection();
/* 258 */     assertValid(conn);
/* 259 */     return conn.getRemotePort();
/*     */   }
/*     */   
/*     */   public boolean isSecure() {
/* 263 */     OperatedClientConnection conn = getWrappedConnection();
/* 264 */     assertValid(conn);
/* 265 */     return conn.isSecure();
/*     */   }
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 269 */     OperatedClientConnection conn = getWrappedConnection();
/* 270 */     assertValid(conn);
/* 271 */     if (!isOpen()) {
/* 272 */       return null;
/*     */     }
/* 274 */     SSLSession result = null;
/* 275 */     Socket sock = conn.getSocket();
/* 276 */     if (sock instanceof SSLSocket) {
/* 277 */       result = ((SSLSocket)sock).getSession();
/*     */     }
/* 279 */     return result;
/*     */   }
/*     */   
/*     */   public void markReusable() {
/* 283 */     this.markedReusable = true;
/*     */   }
/*     */   
/*     */   public void unmarkReusable() {
/* 287 */     this.markedReusable = false;
/*     */   }
/*     */   
/*     */   public boolean isMarkedReusable() {
/* 291 */     return this.markedReusable;
/*     */   }
/*     */   
/*     */   public void setIdleDuration(long duration, TimeUnit unit) {
/* 295 */     if (duration > 0L) {
/* 296 */       this.duration = unit.toMillis(duration);
/*     */     } else {
/* 298 */       this.duration = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void releaseConnection() {
/* 303 */     if (this.released) {
/*     */       return;
/*     */     }
/* 306 */     this.released = true;
/* 307 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public synchronized void abortConnection() {
/* 311 */     if (this.released) {
/*     */       return;
/*     */     }
/* 314 */     this.released = true;
/* 315 */     unmarkReusable();
/*     */     try {
/* 317 */       shutdown();
/* 318 */     } catch (IOException ignore) {}
/*     */     
/* 320 */     this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/* 324 */     OperatedClientConnection conn = getWrappedConnection();
/* 325 */     assertValid(conn);
/* 326 */     if (conn instanceof HttpContext) {
/* 327 */       return ((HttpContext)conn).getAttribute(id);
/*     */     }
/* 329 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 334 */     OperatedClientConnection conn = getWrappedConnection();
/* 335 */     assertValid(conn);
/* 336 */     if (conn instanceof HttpContext) {
/* 337 */       return ((HttpContext)conn).removeAttribute(id);
/*     */     }
/* 339 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/* 344 */     OperatedClientConnection conn = getWrappedConnection();
/* 345 */     assertValid(conn);
/* 346 */     if (conn instanceof HttpContext)
/* 347 */       ((HttpContext)conn).setAttribute(id, obj); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\AbstractClientConnAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */