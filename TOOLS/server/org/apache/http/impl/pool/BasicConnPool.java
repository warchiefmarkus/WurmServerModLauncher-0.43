/*    */ package org.apache.http.impl.pool;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.http.HttpClientConnection;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @ThreadSafe
/*    */ public class BasicConnPool
/*    */   extends AbstractConnPool<HttpHost, HttpClientConnection, BasicPoolEntry>
/*    */ {
/* 63 */   private static AtomicLong COUNTER = new AtomicLong();
/*    */   
/*    */   public BasicConnPool(ConnFactory<HttpHost, HttpClientConnection> connFactory) {
/* 66 */     super(connFactory, 2, 20);
/*    */   }
/*    */   
/*    */   public BasicConnPool(HttpParams params) {
/* 70 */     super(new BasicConnFactory(params), 2, 20);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BasicPoolEntry createEntry(HttpHost host, HttpClientConnection conn) {
/* 77 */     return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\pool\BasicConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */