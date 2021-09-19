/*    */ package org.fourthline.cling.support.connectionmanager.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.ServiceReference;
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
/*    */ import org.fourthline.cling.support.model.ConnectionInfo;
/*    */ import org.fourthline.cling.support.model.ProtocolInfo;
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
/*    */ public abstract class GetCurrentConnectionInfo
/*    */   extends ActionCallback
/*    */ {
/*    */   public GetCurrentConnectionInfo(Service service, int connectionID) {
/* 35 */     this(service, (ControlPoint)null, connectionID);
/*    */   }
/*    */   
/*    */   protected GetCurrentConnectionInfo(Service service, ControlPoint controlPoint, int connectionID) {
/* 39 */     super(new ActionInvocation(service.getAction("GetCurrentConnectionInfo")), controlPoint);
/* 40 */     getActionInvocation().setInput("ConnectionID", Integer.valueOf(connectionID));
/*    */   }
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
/*    */   public void success(ActionInvocation invocation) {
/*    */     try {
/* 55 */       ConnectionInfo info = new ConnectionInfo(((Integer)invocation.getInput("ConnectionID").getValue()).intValue(), ((Integer)invocation.getOutput("RcsID").getValue()).intValue(), ((Integer)invocation.getOutput("AVTransportID").getValue()).intValue(), new ProtocolInfo(invocation.getOutput("ProtocolInfo").toString()), new ServiceReference(invocation.getOutput("PeerConnectionManager").toString()), ((Integer)invocation.getOutput("PeerConnectionID").getValue()).intValue(), ConnectionInfo.Direction.valueOf(invocation.getOutput("Direction").toString()), ConnectionInfo.Status.valueOf(invocation.getOutput("Status").toString()));
/*    */ 
/*    */       
/* 58 */       received(invocation, info);
/*    */     }
/* 60 */     catch (Exception ex) {
/* 61 */       invocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse ConnectionInfo response: " + ex, ex));
/*    */ 
/*    */       
/* 64 */       failure(invocation, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, ConnectionInfo paramConnectionInfo);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\callback\GetCurrentConnectionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */