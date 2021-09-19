/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class PoolingClientConnectionManager
/*     */   implements ClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/*  73 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */   
/*     */   private final HttpConnPool pool;
/*     */   
/*     */   private final ClientConnectionOperator operator;
/*     */   
/*     */   private final DnsResolver dnsResolver;
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg) {
/*  85 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schreg, DnsResolver dnsResolver) {
/*  89 */     this(schreg, -1L, TimeUnit.MILLISECONDS, dnsResolver);
/*     */   }
/*     */   
/*     */   public PoolingClientConnectionManager() {
/*  93 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit) {
/*  99 */     this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit, DnsResolver dnsResolver) {
/* 106 */     if (schemeRegistry == null) {
/* 107 */       throw new IllegalArgumentException("Scheme registry may not be null");
/*     */     }
/* 109 */     if (dnsResolver == null) {
/* 110 */       throw new IllegalArgumentException("DNS resolver may not be null");
/*     */     }
/* 112 */     this.schemeRegistry = schemeRegistry;
/* 113 */     this.dnsResolver = dnsResolver;
/* 114 */     this.operator = createConnectionOperator(schemeRegistry);
/* 115 */     this.pool = new HttpConnPool(this.log, this.operator, 2, 20, timeToLive, tunit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 121 */       shutdown();
/*     */     } finally {
/* 123 */       super.finalize();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 140 */     return new DefaultClientConnectionOperator(schreg, this.dnsResolver);
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 144 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 148 */     StringBuilder buf = new StringBuilder();
/* 149 */     buf.append("[route: ").append(route).append("]");
/* 150 */     if (state != null) {
/* 151 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 153 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 157 */     StringBuilder buf = new StringBuilder();
/* 158 */     PoolStats totals = this.pool.getTotalStats();
/* 159 */     PoolStats stats = this.pool.getStats(route);
/* 160 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 161 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 162 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 163 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 164 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 165 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(HttpPoolEntry entry) {
/* 169 */     StringBuilder buf = new StringBuilder();
/* 170 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 171 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 172 */     Object state = entry.getState();
/* 173 */     if (state != null) {
/* 174 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 176 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 182 */     if (route == null) {
/* 183 */       throw new IllegalArgumentException("HTTP route may not be null");
/*     */     }
/* 185 */     if (this.log.isDebugEnabled()) {
/* 186 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 188 */     final Future<HttpPoolEntry> future = this.pool.lease(route, state);
/*     */     
/* 190 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 193 */           future.cancel(true);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 199 */           return PoolingClientConnectionManager.this.leaseConnection(future, timeout, tunit);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/*     */     try {
/* 212 */       HttpPoolEntry entry = future.get(timeout, tunit);
/* 213 */       if (entry == null || future.isCancelled()) {
/* 214 */         throw new InterruptedException();
/*     */       }
/* 216 */       if (entry.getConnection() == null) {
/* 217 */         throw new IllegalStateException("Pool entry with no connection");
/*     */       }
/* 219 */       if (this.log.isDebugEnabled()) {
/* 220 */         this.log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 222 */       return new ManagedClientConnectionImpl(this, this.operator, entry);
/* 223 */     } catch (ExecutionException ex) {
/* 224 */       Throwable cause = ex.getCause();
/* 225 */       if (cause == null) {
/* 226 */         cause = ex;
/*     */       }
/* 228 */       this.log.error("Unexpected exception leasing connection from pool", cause);
/*     */       
/* 230 */       throw new InterruptedException();
/* 231 */     } catch (TimeoutException ex) {
/* 232 */       throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 239 */     if (!(conn instanceof ManagedClientConnectionImpl)) {
/* 240 */       throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
/*     */     }
/*     */ 
/*     */     
/* 244 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 245 */     if (managedConn.getManager() != this) {
/* 246 */       throw new IllegalStateException("Connection not obtained from this manager.");
/*     */     }
/*     */     
/* 249 */     synchronized (managedConn) {
/* 250 */       HttpPoolEntry entry = managedConn.detach();
/* 251 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 255 */         if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/*     */           try {
/* 257 */             managedConn.shutdown();
/* 258 */           } catch (IOException iox) {
/* 259 */             if (this.log.isDebugEnabled()) {
/* 260 */               this.log.debug("I/O exception shutting down released connection", iox);
/*     */             }
/*     */           } 
/*     */         }
/*     */         
/* 265 */         if (managedConn.isMarkedReusable()) {
/* 266 */           entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 267 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 269 */             if (keepalive > 0L) {
/* 270 */               s = "for " + keepalive + " " + tunit;
/*     */             } else {
/* 272 */               s = "indefinitely";
/*     */             } 
/* 274 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 278 */         this.pool.release(entry, managedConn.isMarkedReusable());
/*     */       } 
/* 280 */       if (this.log.isDebugEnabled()) {
/* 281 */         this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 287 */     this.log.debug("Connection manager is shutting down");
/*     */     try {
/* 289 */       this.pool.shutdown();
/* 290 */     } catch (IOException ex) {
/* 291 */       this.log.debug("I/O exception shutting down connection manager", ex);
/*     */     } 
/* 293 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 297 */     if (this.log.isDebugEnabled()) {
/* 298 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 300 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 304 */     this.log.debug("Closing expired connections");
/* 305 */     this.pool.closeExpired();
/*     */   }
/*     */   
/*     */   public int getMaxTotal() {
/* 309 */     return this.pool.getMaxTotal();
/*     */   }
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 313 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 317 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 321 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 325 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 329 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 333 */     return this.pool.getTotalStats();
/*     */   }
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 337 */     return this.pool.getStats(route);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\PoolingClientConnectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */