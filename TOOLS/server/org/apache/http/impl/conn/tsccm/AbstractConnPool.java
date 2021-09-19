/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.impl.conn.IdleConnectionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractConnPool
/*     */ {
/*  91 */   private final Log log = LogFactory.getLog(getClass()); @GuardedBy("poolLock")
/*  92 */   protected Set<BasicPoolEntry> leasedConnections = new HashSet<BasicPoolEntry>();
/*  93 */   protected IdleConnectionHandler idleConnHandler = new IdleConnectionHandler();
/*  94 */   protected final Lock poolLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("poolLock")
/*     */   protected int numConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean isShutDown;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<BasicPoolEntryRef> issuedConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ReferenceQueue<Object> refQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableConnectionGC() throws IllegalStateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BasicPoolEntry getEntry(HttpRoute route, Object state, long timeout, TimeUnit tunit) throws ConnectionPoolTimeoutException, InterruptedException {
/* 123 */     return requestPoolEntry(route, state).getPoolEntry(timeout, tunit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PoolEntryRequest requestPoolEntry(HttpRoute paramHttpRoute, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void freeEntry(BasicPoolEntry paramBasicPoolEntry, boolean paramBoolean, long paramLong, TimeUnit paramTimeUnit);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReference(Reference<?> ref) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleLostEntry(HttpRoute paramHttpRoute);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 163 */     if (tunit == null) {
/* 164 */       throw new IllegalArgumentException("Time unit must not be null.");
/*     */     }
/*     */     
/* 167 */     this.poolLock.lock();
/*     */     try {
/* 169 */       this.idleConnHandler.closeIdleConnections(tunit.toMillis(idletime));
/*     */     } finally {
/* 171 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 176 */     this.poolLock.lock();
/*     */     try {
/* 178 */       this.idleConnHandler.closeExpiredConnections();
/*     */     } finally {
/* 180 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void deleteClosedConnections();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 196 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 199 */       if (this.isShutDown) {
/*     */         return;
/*     */       }
/*     */       
/* 203 */       Iterator<BasicPoolEntry> iter = this.leasedConnections.iterator();
/* 204 */       while (iter.hasNext()) {
/* 205 */         BasicPoolEntry entry = iter.next();
/* 206 */         iter.remove();
/* 207 */         closeConnection(entry.getConnection());
/*     */       } 
/* 209 */       this.idleConnHandler.removeAll();
/*     */       
/* 211 */       this.isShutDown = true;
/*     */     } finally {
/*     */       
/* 214 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeConnection(OperatedClientConnection conn) {
/* 225 */     if (conn != null)
/*     */       try {
/* 227 */         conn.close();
/* 228 */       } catch (IOException ex) {
/* 229 */         this.log.debug("I/O error closing connection", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\AbstractConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */