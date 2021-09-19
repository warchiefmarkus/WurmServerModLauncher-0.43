/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.client.ConnectionBackoffStrategy;
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
/*    */ public class NullBackoffStrategy
/*    */   implements ConnectionBackoffStrategy
/*    */ {
/*    */   public boolean shouldBackoff(Throwable t) {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public boolean shouldBackoff(HttpResponse resp) {
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\NullBackoffStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */