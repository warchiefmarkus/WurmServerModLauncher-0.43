/*    */ package org.fourthline.cling.support.renderingcontrol.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionException;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.ErrorCode;
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
/*    */ public abstract class GetVolume
/*    */   extends ActionCallback
/*    */ {
/* 34 */   private static Logger log = Logger.getLogger(GetVolume.class.getName());
/*    */   
/*    */   public GetVolume(Service service) {
/* 37 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public GetVolume(UnsignedIntegerFourBytes instanceId, Service service) {
/* 41 */     super(new ActionInvocation(service.getAction("GetVolume")));
/* 42 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 43 */     getActionInvocation().setInput("Channel", Channel.Master.toString());
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 47 */     boolean ok = true;
/* 48 */     int currentVolume = 0;
/*    */     try {
/* 50 */       currentVolume = Integer.valueOf(invocation.getOutput("CurrentVolume").getValue().toString()).intValue();
/* 51 */     } catch (Exception ex) {
/* 52 */       invocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse ProtocolInfo response: " + ex, ex));
/*    */ 
/*    */       
/* 55 */       failure(invocation, null);
/* 56 */       ok = false;
/*    */     } 
/* 58 */     if (ok) received(invocation, currentVolume); 
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, int paramInt);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\callback\GetVolume.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */