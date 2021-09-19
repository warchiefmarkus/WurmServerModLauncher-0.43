/*    */ package org.fourthline.cling.support.contentdirectory;
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
/*    */ public class ContentDirectoryException
/*    */   extends ActionException
/*    */ {
/*    */   public ContentDirectoryException(int errorCode, String message) {
/* 27 */     super(errorCode, message);
/*    */   }
/*    */ 
/*    */   
/*    */   public ContentDirectoryException(int errorCode, String message, Throwable cause) {
/* 32 */     super(errorCode, message, cause);
/*    */   }
/*    */   
/*    */   public ContentDirectoryException(ErrorCode errorCode, String message) {
/* 36 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public ContentDirectoryException(ErrorCode errorCode) {
/* 40 */     super(errorCode);
/*    */   }
/*    */   
/*    */   public ContentDirectoryException(ContentDirectoryErrorCode errorCode, String message) {
/* 44 */     super(errorCode.getCode(), errorCode.getDescription() + ". " + message + ".");
/*    */   }
/*    */   
/*    */   public ContentDirectoryException(ContentDirectoryErrorCode errorCode) {
/* 48 */     super(errorCode.getCode(), errorCode.getDescription());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\ContentDirectoryException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */