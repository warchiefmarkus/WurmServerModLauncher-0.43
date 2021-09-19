/*    */ package com.sun.xml.bind.api;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AccessorException
/*    */   extends Exception
/*    */ {
/*    */   public AccessorException() {}
/*    */   
/*    */   public AccessorException(String message) {
/* 63 */     super(message);
/*    */   }
/*    */   
/*    */   public AccessorException(String message, Throwable cause) {
/* 67 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public AccessorException(Throwable cause) {
/* 71 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\AccessorException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */