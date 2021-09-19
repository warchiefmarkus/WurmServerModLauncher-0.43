/*    */ package org.fourthline.cling.support.avtransport;
/*    */ 
/*    */ import org.fourthline.cling.model.action.ActionException;
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
/*    */ public class AVTransportException
/*    */   extends ActionException
/*    */ {
/*    */   public AVTransportException(int errorCode, String message) {
/* 27 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public AVTransportException(int errorCode, String message, Throwable cause) {
/* 31 */     super(errorCode, message, cause);
/*    */   }
/*    */   
/*    */   public AVTransportException(ErrorCode errorCode, String message) {
/* 35 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public AVTransportException(ErrorCode errorCode) {
/* 39 */     super(errorCode);
/*    */   }
/*    */   
/*    */   public AVTransportException(AVTransportErrorCode errorCode, String message) {
/* 43 */     super(errorCode.getCode(), errorCode.getDescription() + ". " + message + ".");
/*    */   }
/*    */   
/*    */   public AVTransportException(AVTransportErrorCode errorCode) {
/* 47 */     super(errorCode.getCode(), errorCode.getDescription());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\AVTransportException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */