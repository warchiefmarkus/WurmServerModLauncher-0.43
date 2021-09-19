/*    */ package org.fourthline.cling.support.connectionmanager.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
/*    */ import org.fourthline.cling.support.model.ProtocolInfos;
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
/*    */ public abstract class GetProtocolInfo
/*    */   extends ActionCallback
/*    */ {
/*    */   public GetProtocolInfo(Service service) {
/* 33 */     this(service, null);
/*    */   }
/*    */   
/*    */   protected GetProtocolInfo(Service service, ControlPoint controlPoint) {
/* 37 */     super(new ActionInvocation(service.getAction("GetProtocolInfo")), controlPoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/*    */     try {
/* 43 */       ActionArgumentValue sink = invocation.getOutput("Sink");
/* 44 */       ActionArgumentValue source = invocation.getOutput("Source");
/*    */       
/* 46 */       received(invocation, (sink != null) ? new ProtocolInfos(sink
/*    */             
/* 48 */             .toString()) : null, (source != null) ? new ProtocolInfos(source
/* 49 */             .toString()) : null);
/*    */     
/*    */     }
/* 52 */     catch (Exception ex) {
/* 53 */       invocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse ProtocolInfo response: " + ex, ex));
/*    */ 
/*    */       
/* 56 */       failure(invocation, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, ProtocolInfos paramProtocolInfos1, ProtocolInfos paramProtocolInfos2);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\callback\GetProtocolInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */