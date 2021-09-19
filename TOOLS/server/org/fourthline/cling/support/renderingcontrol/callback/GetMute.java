/*    */ package org.fourthline.cling.support.renderingcontrol.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.Channel;
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
/*    */ public abstract class GetMute
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(GetMute.class.getName());
/*    */   
/*    */   public GetMute(Service service) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   public GetMute(UnsignedIntegerFourBytes instanceId, Service service) {
/* 38 */     super(new ActionInvocation(service.getAction("GetMute")));
/* 39 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 40 */     getActionInvocation().setInput("Channel", Channel.Master.toString());
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 44 */     boolean currentMute = ((Boolean)invocation.getOutput("CurrentMute").getValue()).booleanValue();
/* 45 */     received(invocation, currentMute);
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\callback\GetMute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */