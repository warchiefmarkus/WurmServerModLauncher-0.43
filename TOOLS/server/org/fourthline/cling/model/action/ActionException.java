/*    */ package org.fourthline.cling.model.action;
/*    */ 
/*    */ import org.fourthline.cling.model.types.ErrorCode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActionException
/*    */   extends Exception
/*    */ {
/*    */   private int errorCode;
/*    */   
/*    */   public ActionException(int errorCode, String message) {
/* 30 */     super(message);
/* 31 */     this.errorCode = errorCode;
/*    */   }
/*    */   
/*    */   public ActionException(int errorCode, String message, Throwable cause) {
/* 35 */     super(message, cause);
/* 36 */     this.errorCode = errorCode;
/*    */   }
/*    */   
/*    */   public ActionException(ErrorCode errorCode) {
/* 40 */     this(errorCode.getCode(), errorCode.getDescription());
/*    */   }
/*    */   
/*    */   public ActionException(ErrorCode errorCode, String message) {
/* 44 */     this(errorCode, message, true);
/*    */   }
/*    */   
/*    */   public ActionException(ErrorCode errorCode, String message, boolean concatMessages) {
/* 48 */     this(errorCode.getCode(), concatMessages ? (errorCode.getDescription() + ". " + message + ".") : message);
/*    */   }
/*    */   
/*    */   public ActionException(ErrorCode errorCode, String message, Throwable cause) {
/* 52 */     this(errorCode.getCode(), errorCode.getDescription() + ". " + message + ".", cause);
/*    */   }
/*    */   
/*    */   public int getErrorCode() {
/* 56 */     return this.errorCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\ActionException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */