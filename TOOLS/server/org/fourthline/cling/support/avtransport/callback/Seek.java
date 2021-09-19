/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.SeekMode;
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
/*    */ public abstract class Seek
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(Seek.class.getName());
/*    */   
/*    */   public Seek(Service service, String relativeTimeTarget) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service, SeekMode.REL_TIME, relativeTimeTarget);
/*    */   }
/*    */   
/*    */   public Seek(UnsignedIntegerFourBytes instanceId, Service service, String relativeTimeTarget) {
/* 39 */     this(instanceId, service, SeekMode.REL_TIME, relativeTimeTarget);
/*    */   }
/*    */   
/*    */   public Seek(Service service, SeekMode mode, String target) {
/* 43 */     this(new UnsignedIntegerFourBytes(0L), service, mode, target);
/*    */   }
/*    */   
/*    */   public Seek(UnsignedIntegerFourBytes instanceId, Service service, SeekMode mode, String target) {
/* 47 */     super(new ActionInvocation(service.getAction("Seek")));
/* 48 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 49 */     getActionInvocation().setInput("Unit", mode.name());
/* 50 */     getActionInvocation().setInput("Target", target);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 55 */     log.fine("Execution successful");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\Seek.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */