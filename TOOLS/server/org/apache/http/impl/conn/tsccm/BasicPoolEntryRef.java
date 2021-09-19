/*    */ package org.apache.http.impl.conn.tsccm;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ import org.apache.http.conn.routing.HttpRoute;
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
/*    */ @Deprecated
/*    */ public class BasicPoolEntryRef
/*    */   extends WeakReference<BasicPoolEntry>
/*    */ {
/*    */   private final HttpRoute route;
/*    */   
/*    */   public BasicPoolEntryRef(BasicPoolEntry entry, ReferenceQueue<Object> queue) {
/* 58 */     super(entry, queue);
/* 59 */     if (entry == null) {
/* 60 */       throw new IllegalArgumentException("Pool entry must not be null.");
/*    */     }
/*    */     
/* 63 */     this.route = entry.getPlannedRoute();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final HttpRoute getRoute() {
/* 74 */     return this.route;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\tsccm\BasicPoolEntryRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */