/*    */ package org.fourthline.cling.support.renderingcontrol.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
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
/*    */ public abstract class SetVolume
/*    */   extends ActionCallback
/*    */ {
/* 33 */   private static Logger log = Logger.getLogger(SetVolume.class.getName());
/*    */   
/*    */   public SetVolume(Service service, long newVolume) {
/* 36 */     this(new UnsignedIntegerFourBytes(0L), service, newVolume);
/*    */   }
/*    */   
/*    */   public SetVolume(UnsignedIntegerFourBytes instanceId, Service service, long newVolume) {
/* 40 */     super(new ActionInvocation(service.getAction("SetVolume")));
/* 41 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 42 */     getActionInvocation().setInput("Channel", Channel.Master.toString());
/* 43 */     getActionInvocation().setInput("DesiredVolume", new UnsignedIntegerTwoBytes(newVolume));
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 48 */     log.fine("Executed successfully");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\callback\SetVolume.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */