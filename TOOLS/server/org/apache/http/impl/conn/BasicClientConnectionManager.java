/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicClientConnectionManager
/*     */   implements ClientConnectionManager
/*     */ {
/*  66 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  68 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
/*     */ 
/*     */ 
/*     */   
/*     */   private final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClientConnectionOperator connOperator;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private HttpPoolEntry poolEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private ManagedClientConnectionImpl conn;
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private volatile boolean shutdown;
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientConnectionManager(SchemeRegistry schreg) {
/*  99 */     if (schreg == null) {
/* 100 */       throw new IllegalArgumentException("Scheme registry may not be null");
/*     */     }
/* 102 */     this.schemeRegistry = schreg;
/* 103 */     this.connOperator = createConnectionOperator(schreg);
/*     */   }
/*     */   
/*     */   public BasicClientConnectionManager() {
/* 107 */     this(SchemeRegistryFactory.createDefault());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 113 */       shutdown();
/*     */     } finally {
/* 115 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SchemeRegistry getSchemeRegistry() {
/* 120 */     return this.schemeRegistry;
/*     */   }
/*     */   
/*     */   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
/* 124 */     return new DefaultClientConnectionOperator(schreg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
/* 131 */     return new ClientConnectionRequest()
/*     */       {
/*     */         public void abortRequest() {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
/* 139 */           return BasicClientConnectionManager.this.getConnection(route, state);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertNotShutdown() {
/* 147 */     if (this.shutdown) {
/* 148 */       throw new IllegalStateException("Connection manager has been shut down");
/*     */     }
/*     */   }
/*     */   
/*     */   ManagedClientConnection getConnection(HttpRoute route, Object state) {
/* 153 */     if (route == null) {
/* 154 */       throw new IllegalArgumentException("Route may not be null.");
/*     */     }
/* 156 */     synchronized (this) {
/* 157 */       assertNotShutdown();
/* 158 */       if (this.log.isDebugEnabled()) {
/* 159 */         this.log.debug("Get connection for route " + route);
/*     */       }
/* 161 */       if (this.conn != null) {
/* 162 */         throw new IllegalStateException("Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
/*     */       }
/* 164 */       if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(route)) {
/* 165 */         this.poolEntry.close();
/* 166 */         this.poolEntry = null;
/*     */       } 
/* 168 */       if (this.poolEntry == null) {
/* 169 */         String id = Long.toString(COUNTER.getAndIncrement());
/* 170 */         OperatedClientConnection conn = this.connOperator.createConnection();
/* 171 */         this.poolEntry = new HttpPoolEntry(this.log, id, route, conn, 0L, TimeUnit.MILLISECONDS);
/*     */       } 
/* 173 */       long now = System.currentTimeMillis();
/* 174 */       if (this.poolEntry.isExpired(now)) {
/* 175 */         this.poolEntry.close();
/* 176 */         this.poolEntry.getTracker().reset();
/*     */       } 
/* 178 */       this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
/* 179 */       return this.conn;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownConnection(HttpClientConnection conn) {
/*     */     try {
/* 185 */       conn.shutdown();
/* 186 */     } catch (IOException iox) {
/* 187 */       if (this.log.isDebugEnabled()) {
/* 188 */         this.log.debug("I/O exception shutting down connection", iox);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
/* 194 */     if (!(conn instanceof ManagedClientConnectionImpl)) {
/* 195 */       throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager");
/*     */     }
/*     */     
/* 198 */     ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
/* 199 */     synchronized (managedConn) {
/* 200 */       if (this.log.isDebugEnabled()) {
/* 201 */         this.log.debug("Releasing connection " + conn);
/*     */       }
/* 203 */       if (managedConn.getPoolEntry() == null) {
/*     */         return;
/*     */       }
/* 206 */       ClientConnectionManager manager = managedConn.getManager();
/* 207 */       if (manager != null && manager != this) {
/* 208 */         throw new IllegalStateException("Connection not obtained from this manager");
/*     */       }
/* 210 */       synchronized (this) {
/* 211 */         if (this.shutdown) {
/* 212 */           shutdownConnection((HttpClientConnection)managedConn);
/*     */           return;
/*     */         } 
/*     */         try {
/* 216 */           if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
/* 217 */             shutdownConnection((HttpClientConnection)managedConn);
/*     */           }
/* 219 */           if (managedConn.isMarkedReusable()) {
/* 220 */             this.poolEntry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 221 */             if (this.log.isDebugEnabled()) {
/*     */               String s;
/* 223 */               if (keepalive > 0L) {
/* 224 */                 s = "for " + keepalive + " " + tunit;
/*     */               } else {
/* 226 */                 s = "indefinitely";
/*     */               } 
/* 228 */               this.log.debug("Connection can be kept alive " + s);
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 232 */           managedConn.detach();
/* 233 */           this.conn = null;
/* 234 */           if (this.poolEntry.isClosed()) {
/* 235 */             this.poolEntry = null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 243 */     synchronized (this) {
/* 244 */       assertNotShutdown();
/* 245 */       long now = System.currentTimeMillis();
/* 246 */       if (this.poolEntry != null && this.poolEntry.isExpired(now)) {
/* 247 */         this.poolEntry.close();
/* 248 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 254 */     if (tunit == null) {
/* 255 */       throw new IllegalArgumentException("Time unit must not be null.");
/*     */     }
/* 257 */     synchronized (this) {
/* 258 */       assertNotShutdown();
/* 259 */       long time = tunit.toMillis(idletime);
/* 260 */       if (time < 0L) {
/* 261 */         time = 0L;
/*     */       }
/* 263 */       long deadline = System.currentTimeMillis() - time;
/* 264 */       if (this.poolEntry != null && this.poolEntry.getUpdated() <= deadline) {
/* 265 */         this.poolEntry.close();
/* 266 */         this.poolEntry.getTracker().reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 272 */     synchronized (this) {
/* 273 */       this.shutdown = true;
/*     */       try {
/* 275 */         if (this.poolEntry != null) {
/* 276 */           this.poolEntry.close();
/*     */         }
/*     */       } finally {
/* 279 */         this.poolEntry = null;
/* 280 */         this.conn = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\BasicClientConnectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */