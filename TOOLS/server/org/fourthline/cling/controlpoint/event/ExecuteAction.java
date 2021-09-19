/*    */ package org.fourthline.cling.controlpoint.event;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
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
/*    */ public class ExecuteAction
/*    */ {
/*    */   protected ActionCallback callback;
/*    */   
/*    */   public ExecuteAction(ActionCallback callback) {
/* 28 */     this.callback = callback;
/*    */   }
/*    */   
/*    */   public ActionCallback getCallback() {
/* 32 */     return this.callback;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\event\ExecuteAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */