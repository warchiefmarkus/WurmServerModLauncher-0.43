/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.params.ConnPerRouteBean;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.conn.DefaultClientConnectionOperator;
/*     */ import org.apache.http.impl.conn.SchemeRegistryFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ThreadSafe
/*     */ public class ThreadSafeClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*     */   private final Log log;
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   protected final AbstractConnPool connectionPool;
/*     */   protected final ConnPoolByRoute pool;
/*     */   protected final ClientConnectionOperator connOperator;
/*     */   protected final ConnPerRouteBean connPerRoute;
/*     */   
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg) {
/*  94 */     this(schreg, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadSafeClientConnManager() {
/* 101 */     this(SchemeRegistryFactory.createDefault());
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
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit) {
/* 115 */     this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
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
/*     */   public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit, ConnPerRouteBean connPerRoute) {
/* 133 */     if (schreg == null) {
/* 134 */       throw new IllegalArgumentException("Scheme registry may not be null");
/*     */     }
/* 136 */     this.log = LogFactory.getLog(getClass());
/* 137 */     this.schemeRegistry = schreg;
/* 138 */     this.connPerRoute = connPerRoute;
/* 139 */     this.connOperator = createConnectionOperator(schreg);
/* 140 */     this.pool = createConnectionPool(connTTL, connTTLTimeUnit);
/* 141 */     this.connectionPool = this.pool;
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
/*     */   public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 154 */     if (schreg == null) {
/* 155 */       throw new IllegalArgumentException("Scheme registry may not be null");
/*     */     }
/* 157 */     this.log = LogFactory.getLog(getClass());
/* 158 */     this.schemeRegistry = schreg;
/* 159 */     this.connPerRoute = new ConnPerRouteBean();
/* 160 */     this.connOperator = createConnectionOperator(schreg);
/* 161 */     this.pool = (ConnPoolByRoute)createConnectionPool(params);
/* 162 */     this.connectionPool = this.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 168 */       shutdown();
/*     */     } finally {
/* 170 */       super.finalize();
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
/*     */   protected AbstractConnPool createConnectionPool(HttpParams params) {
/* 182 */     return new ConnPoolByRoute(this.connOperator, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnPoolByRoute createConnectionPool(long connTTL, TimeUnit connTTLTimeUnit) {
/* 193 */     return new ConnPoolByRoute(this.connOperator, (ConnPerRoute)this.connPerRoute, 20, connTTL, connTTLTimeUnit);
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
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 211 */     return (ClientConnectionOperator)new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 215 */     return this.schemeRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
/* 222 */     final PoolEntryRequest poolRequest = this.pool.requestPoolEntry(route, state);
/*     */ 
/*     */     
/* 225 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 228 */           poolRequest.abortRequest();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 234 */           if (route == null) {
/* 235 */             throw new IllegalArgumentException("Route may not be null.");
/*     */           }
/*     */           
/* 238 */           if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
/* 239 */             ThreadSafeClientConnManager.this.log.debug("Get connection: " + route + ", timeout = " + timeout);
/*     */           }
/*     */           
/* 242 */           BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
/* 243 */           return (ManagedClientConnection)new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 252 */     if (!(conn instanceof BasicPooledConnAdapter)) {
/* 253 */       throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
/*     */     }
/*     */ 
/*     */     
/* 257 */     BasicPooledConnAdapter hca = (BasicPooledConnAdapter)conn;
/* 258 */     if (hca.getPoolEntry() != null && hca.getManager() != this) {
/* 259 */       throw new IllegalArgumentException("Connection not obtained from this manager.");
/*     */     }
/*     */     
/* 262 */     synchronized (hca) {
/* 263 */       BasicPoolEntry entry = (BasicPoolEntry)hca.getPoolEntry();
/* 264 */       if (entry == null) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 269 */         if (hca.isOpen() && !hca.isMarkedReusable())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 278 */           hca.shutdown();
/*     */         }
/* 280 */       } catch (IOException iox) {
/* 281 */         if (this.log.isDebugEnabled()) {
/* 282 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/* 285 */         boolean reusable = hca.isMarkedReusable();
/* 286 */         if (this.log.isDebugEnabled()) {
/* 287 */           if (reusable) {
/* 288 */             this.log.debug("Released connection is reusable.");
/*     */           } else {
/* 290 */             this.log.debug("Released connection is not reusable.");
/*     */           } 
/*     */         }
/* 293 */         hca.detach();
/* 294 */         this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 300 */     this.log.debug("Shutting down");
/* 301 */     this.pool.shutdown();
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
/*     */   public int getConnectionsInPool(HttpRoute route) {
/* 315 */     return this.pool.getConnectionsInPool(route);
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
/*     */   public int getConnectionsInPool() {
/* 327 */     return this.pool.getConnectionsInPool();
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 331 */     if (this.log.isDebugEnabled()) {
/* 332 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 334 */     this.pool.closeIdleConnections(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 338 */     this.log.debug("Closing expired connections");
/* 339 */     this.pool.closeExpiredConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 346 */     return this.pool.getMaxTotalConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 353 */     this.pool.setMaxTotalConnections(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 360 */     return this.connPerRoute.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 367 */     this.connPerRoute.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxForRoute(HttpRoute route) {
/* 374 */     return this.connPerRoute.getMaxForRoute(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxForRoute(HttpRoute route, int max) {
/* 381 */     this.connPerRoute.setMaxForRoute(route, max);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\ThreadSafeClientConnManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */