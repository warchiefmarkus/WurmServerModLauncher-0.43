/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.TransportAction;
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
/*    */ public abstract class GetCurrentTransportActions
/*    */   extends ActionCallback
/*    */ {
/* 31 */   private static Logger log = Logger.getLogger(GetCurrentTransportActions.class.getName());
/*    */   
/*    */   public GetCurrentTransportActions(Service service) {
/* 34 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public GetCurrentTransportActions(UnsignedIntegerFourBytes instanceId, Service service) {
/* 38 */     super(new ActionInvocation(service.getAction("GetCurrentTransportActions")));
/* 39 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 43 */     String actionsString = (String)invocation.getOutput("Actions").getValue();
/* 44 */     received(invocation, TransportAction.valueOfCommaSeparatedList(actionsString));
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, TransportAction[] paramArrayOfTransportAction);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\GetCurrentTransportActions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */