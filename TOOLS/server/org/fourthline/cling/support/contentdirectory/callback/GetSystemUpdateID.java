/*    */ package org.fourthline.cling.support.contentdirectory.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
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
/*    */ public abstract class GetSystemUpdateID
/*    */   extends ActionCallback
/*    */ {
/*    */   public GetSystemUpdateID(Service service) {
/* 30 */     super(new ActionInvocation(service.getAction("GetSystemUpdateID")));
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 34 */     boolean ok = true;
/* 35 */     long id = 0L;
/*    */     try {
/* 37 */       id = Long.valueOf(invocation.getOutput("Id").getValue().toString()).longValue();
/* 38 */     } catch (Exception ex) {
/* 39 */       invocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse GetSystemUpdateID response: " + ex, ex));
/* 40 */       failure(invocation, null);
/* 41 */       ok = false;
/*    */     } 
/* 43 */     if (ok) received(invocation, id); 
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, long paramLong);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\callback\GetSystemUpdateID.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */