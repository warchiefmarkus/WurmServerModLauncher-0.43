/*    */ package org.apache.http.impl.client;
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
/*    */ class SystemClock
/*    */   implements Clock
/*    */ {
/*    */   public long getCurrentTime() {
/* 36 */     return System.currentTimeMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\SystemClock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */