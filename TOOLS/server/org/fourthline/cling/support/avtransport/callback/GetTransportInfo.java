/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.TransportInfo;
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
/*    */ public abstract class GetTransportInfo
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(GetTransportInfo.class.getName());
/*    */   
/*    */   public GetTransportInfo(Service service) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public GetTransportInfo(UnsignedIntegerFourBytes instanceId, Service service) {
/* 39 */     super(new ActionInvocation(service.getAction("GetTransportInfo")));
/* 40 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 44 */     TransportInfo transportInfo = new TransportInfo(invocation.getOutputMap());
/* 45 */     received(invocation, transportInfo);
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, TransportInfo paramTransportInfo);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\GetTransportInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */