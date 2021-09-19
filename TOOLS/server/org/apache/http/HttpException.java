/*    */ package org.apache.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -5437299376222011036L;
/*    */   
/*    */   public HttpException() {}
/*    */   
/*    */   public HttpException(String message) {
/* 52 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpException(String message, Throwable cause) {
/* 63 */     super(message);
/* 64 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\HttpException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */