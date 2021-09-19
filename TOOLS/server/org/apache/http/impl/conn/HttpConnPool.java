/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.conn.ClientConnectionOperator;
/*    */ import org.apache.http.conn.OperatedClientConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.pool.AbstractConnPool;
/*    */ import org.apache.http.pool.ConnFactory;
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
/*    */ class HttpConnPool
/*    */   extends AbstractConnPool<HttpRoute, OperatedClientConnection, HttpPoolEntry>
/*    */ {
/* 45 */   private static AtomicLong COUNTER = new AtomicLong();
/*    */   
/*    */   private final Log log;
/*    */   
/*    */   private final long timeToLive;
/*    */   
/*    */   private final TimeUnit tunit;
/*    */ 
/*    */   
/*    */   public HttpConnPool(Log log, ClientConnectionOperator connOperator, int defaultMaxPerRoute, int maxTotal, long timeToLive, TimeUnit tunit) {
/* 55 */     super(new InternalConnFactory(connOperator), defaultMaxPerRoute, maxTotal);
/* 56 */     this.log = log;
/* 57 */     this.timeToLive = timeToLive;
/* 58 */     this.tunit = tunit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpPoolEntry createEntry(HttpRoute route, OperatedClientConnection conn) {
/* 63 */     String id = Long.toString(COUNTER.getAndIncrement());
/* 64 */     return new HttpPoolEntry(this.log, id, route, conn, this.timeToLive, this.tunit);
/*    */   }
/*    */   
/*    */   static class InternalConnFactory
/*    */     implements ConnFactory<HttpRoute, OperatedClientConnection> {
/*    */     private final ClientConnectionOperator connOperator;
/*    */     
/*    */     InternalConnFactory(ClientConnectionOperator connOperator) {
/* 72 */       this.connOperator = connOperator;
/*    */     }
/*    */     
/*    */     public OperatedClientConnection create(HttpRoute route) throws IOException {
/* 76 */       return this.connOperator.createConnection();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\HttpConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */