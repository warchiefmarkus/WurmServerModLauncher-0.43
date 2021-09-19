/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class SingleClientConnManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  67 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean alwaysShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile PoolEntry uniquePoolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile ConnAdapter managedConn;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long lastReleaseTime;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   protected volatile long connectionExpiresTime;
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean isShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager(HttpParams params, SchemeRegistry schreg) {
/* 112 */     this(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager(SchemeRegistry schreg) {
/* 120 */     if (schreg == null) {
/* 121 */       throw new IllegalArgumentException("Scheme registry must not be null.");
/*     */     }
/*     */     
/* 124 */     this.schemeRegistry = schreg;
/* 125 */     this.connOperator = createConnectionOperator(schreg);
/* 126 */     this.uniquePoolEntry = new PoolEntry();
/* 127 */     this.managedConn = null;
/* 128 */     this.lastReleaseTime = -1L;
/* 129 */     this.alwaysShutDown = false;
/* 130 */     this.isShutDown = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleClientConnManager() {
/* 137 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 143 */       shutdown();
/*     */     } finally {
/* 145 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 150 */     return this.schemeRegistry;
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
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 167 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void assertStillUp() throws IllegalStateException {
/* 176 */     if (this.isShutDown) {
/* 177 */       throw new IllegalStateException("Manager is shut down.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 184 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 192 */           return SingleClientConnManager.this.getConnection(route, state);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 208 */     if (route == null) {
/* 209 */       throw new IllegalArgumentException("Route may not be null.");
/*     */     }
/* 211 */     assertStillUp();
/*     */     
/* 213 */     if (this.log.isDebugEnabled()) {
/* 214 */       this.log.debug("Get connection for route " + route);
/*     */     }
/*     */     
/* 217 */     synchronized (this) {
/* 218 */       if (this.managedConn != null) {
/* 219 */         throw new IllegalStateException("Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/*     */       }
/*     */       
/* 222 */       boolean recreate = false;
/* 223 */       boolean shutdown = false;
/*     */ 
/*     */       
/* 226 */       closeExpiredConnections();
/*     */       
/* 228 */       if (this.uniquePoolEntry.connection.isOpen()) {
/* 229 */         RouteTracker tracker = this.uniquePoolEntry.tracker;
/* 230 */         shutdown = (tracker == null || !tracker.toRoute().equals(route));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 238 */         recreate = true;
/*     */       } 
/*     */       
/* 241 */       if (shutdown) {
/* 242 */         recreate = true;
/*     */         try {
/* 244 */           this.uniquePoolEntry.shutdown();
/* 245 */         } catch (IOException iox) {
/* 246 */           this.log.debug("Problem shutting down connection.", iox);
/*     */         } 
/*     */       } 
/*     */       
/* 250 */       if (recreate) {
/* 251 */         this.uniquePoolEntry = new PoolEntry();
/*     */       }
/* 253 */       this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
/*     */       
/* 255 */       return this.managedConn;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 262 */     assertStillUp();
/*     */     
/* 264 */     if (!(conn instanceof ConnAdapter)) {
/* 265 */       throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 270 */     if (this.log.isDebugEnabled()) {
/* 271 */       this.log.debug("Releasing connection " + conn);
/*     */     }
/*     */     
/* 274 */     ConnAdapter sca = (ConnAdapter)conn;
/* 275 */     synchronized (sca) {
/* 276 */       if (sca.poolEntry == null)
/*     */         return; 
/* 278 */       ClientConnectionManager manager = sca.getManager();
/* 279 */       if (manager != null && manager != this) {
/* 280 */         throw new IllegalArgumentException("Connection not obtained from this manager.");
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 285 */         if (sca.isOpen() && (this.alwaysShutDown || !sca.isMarkedReusable())) {
/*     */ 
/*     */           
/* 288 */           if (this.log.isDebugEnabled()) {
/* 289 */             this.log.debug("Released connection open but not reusable.");
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 296 */           sca.shutdown();
/*     */         } 
/* 298 */       } catch (IOException iox) {
/* 299 */         if (this.log.isDebugEnabled()) {
/* 300 */           this.log.debug("Exception shutting down released connection.", iox);
/*     */         }
/*     */       } finally {
/* 303 */         sca.detach();
/* 304 */         synchronized (this) {
/* 305 */           this.managedConn = null;
/* 306 */           this.lastReleaseTime = System.currentTimeMillis();
/* 307 */           if (validDuration > 0L) {
/* 308 */             this.connectionExpiresTime = timeUnit.toMillis(validDuration) + this.lastReleaseTime;
/*     */           } else {
/* 310 */             this.connectionExpiresTime = Long.MAX_VALUE;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void closeExpiredConnections() {
/* 317 */     long time = this.connectionExpiresTime;
/* 318 */     if (System.currentTimeMillis() >= time) {
/* 319 */       closeIdleConnections(0L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 324 */     assertStillUp();
/*     */ 
/*     */     
/* 327 */     if (tunit == null) {
/* 328 */       throw new IllegalArgumentException("Time unit must not be null.");
/*     */     }
/*     */     
/* 331 */     synchronized (this) {
/* 332 */       if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
/* 333 */         long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
/*     */         
/* 335 */         if (this.lastReleaseTime <= cutoff) {
/*     */           try {
/* 337 */             this.uniquePoolEntry.close();
/* 338 */           } catch (IOException iox) {
/*     */             
/* 340 */             this.log.debug("Problem closing idle connection.", iox);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 348 */     this.isShutDown = true;
/* 349 */     synchronized (this) {
/*     */       try {
/* 351 */         if (this.uniquePoolEntry != null)
/* 352 */           this.uniquePoolEntry.shutdown(); 
/* 353 */       } catch (IOException iox) {
/*     */         
/* 355 */         this.log.debug("Problem while shutting down manager.", iox);
/*     */       } finally {
/* 357 */         this.uniquePoolEntry = null;
/* 358 */         this.managedConn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void revokeConnection() {
/* 364 */     ConnAdapter conn = this.managedConn;
/* 365 */     if (conn == null)
/*     */       return; 
/* 367 */     conn.detach();
/*     */     
/* 369 */     synchronized (this) {
/*     */       try {
/* 371 */         this.uniquePoolEntry.shutdown();
/* 372 */       } catch (IOException iox) {
/*     */         
/* 374 */         this.log.debug("Problem while shutting down connection.", iox);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class PoolEntry
/*     */     extends AbstractPoolEntry
/*     */   {
/*     */     protected PoolEntry() {
/* 389 */       super(SingleClientConnManager.this.connOperator, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void close() throws IOException {
/* 396 */       shutdownEntry();
/* 397 */       if (this.connection.isOpen()) {
/* 398 */         this.connection.close();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void shutdown() throws IOException {
/* 405 */       shutdownEntry();
/* 406 */       if (this.connection.isOpen()) {
/* 407 */         this.connection.shutdown();
/*     */       }
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
/*     */   protected class ConnAdapter
/*     */     extends AbstractPooledConnAdapter
/*     */   {
/*     */     protected ConnAdapter(SingleClientConnManager.PoolEntry entry, HttpRoute route) {
/* 424 */       super(SingleClientConnManager.this, entry);
/* 425 */       markReusable();
/* 426 */       entry.route = route;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\SingleClientConnManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */