/*    */ package org.fourthline.cling.support.renderingcontrol;
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
/*    */ public class RenderingControlException
/*    */   extends ActionException
/*    */ {
/*    */   public RenderingControlException(int errorCode, String message) {
/* 27 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public RenderingControlException(int errorCode, String message, Throwable cause) {
/* 31 */     super(errorCode, message, cause);
/*    */   }
/*    */   
/*    */   public RenderingControlException(ErrorCode errorCode, String message) {
/* 35 */     super(errorCode, message);
/*    */   }
/*    */   
/*    */   public RenderingControlException(ErrorCode errorCode) {
/* 39 */     super(errorCode);
/*    */   }
/*    */   
/*    */   public RenderingControlException(RenderingControlErrorCode errorCode, String message) {
/* 43 */     super(errorCode.getCode(), errorCode.getDescription() + ". " + message + ".");
/*    */   }
/*    */   
/*    */   public RenderingControlException(RenderingControlErrorCode errorCode) {
/* 47 */     super(errorCode.getCode(), errorCode.getDescription());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\RenderingControlException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */