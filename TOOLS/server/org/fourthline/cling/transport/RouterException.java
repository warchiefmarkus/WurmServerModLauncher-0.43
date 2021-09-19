/*    */ package org.fourthline.cling.transport;
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
/*    */ public class RouterException
/*    */   extends Exception
/*    */ {
/*    */   public RouterException() {}
/*    */   
/*    */   public RouterException(String s) {
/* 37 */     super(s);
/*    */   }
/*    */   
/*    */   public RouterException(String s, Throwable throwable) {
/* 41 */     super(s, throwable);
/*    */   }
/*    */   
/*    */   public RouterException(Throwable throwable) {
/* 45 */     super(throwable);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\RouterException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */