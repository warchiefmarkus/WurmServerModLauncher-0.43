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
/*    */ public abstract class SetMute
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(SetMute.class.getName());
/*    */   
/*    */   public SetMute(Service service, boolean desiredMute) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service, desiredMute);
/*    */   }
/*    */   
/*    */   public SetMute(UnsignedIntegerFourBytes instanceId, Service service, boolean desiredMute) {
/* 39 */     super(new ActionInvocation(service.getAction("SetMute")));
/* 40 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 41 */     getActionInvocation().setInput("Channel", Channel.Master.toString());
/* 42 */     getActionInvocation().setInput("DesiredMute", Boolean.valueOf(desiredMute));
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 47 */     log.fine("Executed successfully");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\callback\SetMute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */