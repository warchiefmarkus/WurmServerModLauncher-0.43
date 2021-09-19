/*    */ package com.wurmonline.server.support;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JSONException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public JSONException(String message) {
/* 39 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JSONException(Throwable cause) {
/* 47 */     super(cause.getMessage(), cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JSONException(String message, Throwable cause) {
/* 60 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\JSONException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */