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
/*    */ public class ActionCancelledException
/*    */   extends ActionException
/*    */ {
/*    */   public ActionCancelledException(InterruptedException cause) {
/* 25 */     super(ErrorCode.ACTION_FAILED, "Action execution interrupted", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\ActionCancelledException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */