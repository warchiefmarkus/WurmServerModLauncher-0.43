/*    */ package org.fourthline.cling.support.connectionmanager.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.ServiceReference;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
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
/*    */ 
/*    */ 
/*    */ public abstract class PrepareForConnection
/*    */   extends ActionCallback
/*    */ {
/*    */   public PrepareForConnection(Service service, ProtocolInfo remoteProtocolInfo, ServiceReference peerConnectionManager, int peerConnectionID, ConnectionInfo.Direction direction) {
/* 35 */     this(service, null, remoteProtocolInfo, peerConnectionManager, peerConnectionID, direction);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PrepareForConnection(Service service, ControlPoint controlPoint, ProtocolInfo remoteProtocolInfo, ServiceReference peerConnectionManager, int peerConnectionID, ConnectionInfo.Direction direction) {
/* 41 */     super(new ActionInvocation(service.getAction("PrepareForConnection")), controlPoint);
/*    */     
/* 43 */     getActionInvocation().setInput("RemoteProtocolInfo", remoteProtocolInfo.toString());
/* 44 */     getActionInvocation().setInput("PeerConnectionManager", peerConnectionManager.toString());
/* 45 */     getActionInvocation().setInput("PeerConnectionID", Integer.valueOf(peerConnectionID));
/* 46 */     getActionInvocation().setInput("Direction", direction.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 51 */     received(invocation, ((Integer)invocation
/*    */         
/* 53 */         .getOutput("ConnectionID").getValue()).intValue(), ((Integer)invocation
/* 54 */         .getOutput("RcsID").getValue()).intValue(), ((Integer)invocation
/* 55 */         .getOutput("AVTransportID").getValue()).intValue());
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, int paramInt1, int paramInt2, int paramInt3);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\callback\PrepareForConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */