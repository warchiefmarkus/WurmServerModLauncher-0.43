/*    */ package org.apache.http.pool;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class PoolStats
/*    */ {
/*    */   private final int leased;
/*    */   private final int pending;
/*    */   private final int available;
/*    */   private final int max;
/*    */   
/*    */   public PoolStats(int leased, int pending, int free, int max) {
/* 47 */     this.leased = leased;
/* 48 */     this.pending = pending;
/* 49 */     this.available = free;
/* 50 */     this.max = max;
/*    */   }
/*    */   
/*    */   public int getLeased() {
/* 54 */     return this.leased;
/*    */   }
/*    */   
/*    */   public int getPending() {
/* 58 */     return this.pending;
/*    */   }
/*    */   
/*    */   public int getAvailable() {
/* 62 */     return this.available;
/*    */   }
/*    */   
/*    */   public int getMax() {
/* 66 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     StringBuilder buffer = new StringBuilder();
/* 72 */     buffer.append("[leased: ");
/* 73 */     buffer.append(this.leased);
/* 74 */     buffer.append("; pending: ");
/* 75 */     buffer.append(this.pending);
/* 76 */     buffer.append("; available: ");
/* 77 */     buffer.append(this.available);
/* 78 */     buffer.append("; max: ");
/* 79 */     buffer.append(this.max);
/* 80 */     buffer.append("]");
/* 81 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\PoolStats.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */