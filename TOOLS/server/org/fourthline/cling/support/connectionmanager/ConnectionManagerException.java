/*    */ package org.fourthline.cling.support.connectionmanager;
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
/*    */ public class ConnectionManagerException
/*    */   extends ActionException
/*    */ {
/*    */   public ConnectionManagerException(int errorCode, String message) {
/* 27 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public ConnectionManagerException(int errorCode, String message, Throwable cause) {
/* 31 */     super(errorCode, message, cause);
/*    */   }
/*    */   
/*    */   public ConnectionManagerException(ErrorCode errorCode, String message) {
/* 35 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public ConnectionManagerException(ErrorCode errorCode) {
/* 39 */     super(errorCode);
/*    */   }
/*    */   
/*    */   public ConnectionManagerException(ConnectionManagerErrorCode errorCode, String message) {
/* 43 */     super(errorCode.getCode(), errorCode.getDescription() + ". " + message + ".");
/*    */   }
/*    */   
/*    */   public ConnectionManagerException(ConnectionManagerErrorCode errorCode) {
/* 47 */     super(errorCode.getCode(), errorCode.getDescription());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\ConnectionManagerException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */