/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class StopWatch
/*    */ {
/*    */   private long start;
/*    */   private long stop;
/*    */   
/*    */   public void start() {
/* 38 */     this.start = System.nanoTime();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 45 */     this.stop = System.nanoTime();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getTotalTimeMillis() {
/* 52 */     return TimeUnit.NANOSECONDS.toMillis(this.stop - this.start);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\StopWatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */