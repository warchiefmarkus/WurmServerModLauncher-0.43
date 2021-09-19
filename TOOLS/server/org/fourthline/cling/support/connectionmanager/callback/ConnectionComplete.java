/*    */ package org.fourthline.cling.support.connectionmanager.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
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
/*    */ public abstract class ConnectionComplete
/*    */   extends ActionCallback
/*    */ {
/*    */   public ConnectionComplete(Service service, int connectionID) {
/* 29 */     this(service, null, connectionID);
/*    */   }
/*    */   
/*    */   protected ConnectionComplete(Service service, ControlPoint controlPoint, int connectionID) {
/* 33 */     super(new ActionInvocation(service.getAction("ConnectionComplete")), controlPoint);
/* 34 */     getActionInvocation().setInput("ConnectionID", Integer.valueOf(connectionID));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\connectionmanager\callback\ConnectionComplete.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */