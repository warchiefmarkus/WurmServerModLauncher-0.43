/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.conn.OperatedClientConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.conn.routing.RouteTracker;
/*    */ import org.apache.http.pool.PoolEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class HttpPoolEntry
/*    */   extends PoolEntry<HttpRoute, OperatedClientConnection>
/*    */ {
/*    */   private final Log log;
/*    */   private final RouteTracker tracker;
/*    */   
/*    */   public HttpPoolEntry(Log log, String id, HttpRoute route, OperatedClientConnection conn, long timeToLive, TimeUnit tunit) {
/* 53 */     super(id, route, conn, timeToLive, tunit);
/* 54 */     this.log = log;
/* 55 */     this.tracker = new RouteTracker(route);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired(long now) {
/* 60 */     boolean expired = super.isExpired(now);
/* 61 */     if (expired && this.log.isDebugEnabled()) {
/* 62 */       this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
/*    */     }
/* 64 */     return expired;
/*    */   }
/*    */   
/*    */   RouteTracker getTracker() {
/* 68 */     return this.tracker;
/*    */   }
/*    */   
/*    */   HttpRoute getPlannedRoute() {
/* 72 */     return (HttpRoute)getRoute();
/*    */   }
/*    */   
/*    */   HttpRoute getEffectiveRoute() {
/* 76 */     return this.tracker.toRoute();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 81 */     OperatedClientConnection conn = (OperatedClientConnection)getConnection();
/* 82 */     return !conn.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 87 */     OperatedClientConnection conn = (OperatedClientConnection)getConnection();
/*    */     try {
/* 89 */       conn.close();
/* 90 */     } catch (IOException ex) {
/* 91 */       this.log.debug("I/O error closing connection", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\HttpPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */