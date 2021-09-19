/*    */ package org.fourthline.cling.support.igd.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.Connection;
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
/*    */ public abstract class GetStatusInfo
/*    */   extends ActionCallback
/*    */ {
/*    */   public GetStatusInfo(Service service) {
/* 32 */     super(new ActionInvocation(service.getAction("GetStatusInfo")));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/*    */     try {
/* 40 */       Connection.Status status = Connection.Status.valueOf(invocation.getOutput("NewConnectionStatus").getValue().toString());
/*    */ 
/*    */       
/* 43 */       Connection.Error lastError = Connection.Error.valueOf(invocation.getOutput("NewLastConnectionError").getValue().toString());
/*    */       
/* 45 */       success(new Connection.StatusInfo(status, (UnsignedIntegerFourBytes)invocation.getOutput("NewUptime").getValue(), lastError));
/*    */     }
/* 47 */     catch (Exception ex) {
/* 48 */       invocation.setFailure(new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Invalid status or last error string: " + ex, ex));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 55 */       failure(invocation, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void success(Connection.StatusInfo paramStatusInfo);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\callback\GetStatusInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */