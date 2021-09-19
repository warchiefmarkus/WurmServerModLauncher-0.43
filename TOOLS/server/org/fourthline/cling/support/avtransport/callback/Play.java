/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*    */ public abstract class Play
/*    */   extends ActionCallback
/*    */ {
/* 31 */   private static Logger log = Logger.getLogger(Play.class.getName());
/*    */   
/*    */   public Play(Service service) {
/* 34 */     this(new UnsignedIntegerFourBytes(0L), service, "1");
/*    */   }
/*    */   
/*    */   public Play(Service service, String speed) {
/* 38 */     this(new UnsignedIntegerFourBytes(0L), service, speed);
/*    */   }
/*    */   
/*    */   public Play(UnsignedIntegerFourBytes instanceId, Service service) {
/* 42 */     this(instanceId, service, "1");
/*    */   }
/*    */   
/*    */   public Play(UnsignedIntegerFourBytes instanceId, Service service, String speed) {
/* 46 */     super(new ActionInvocation(service.getAction("Play")));
/* 47 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 48 */     getActionInvocation().setInput("Speed", speed);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 53 */     log.fine("Execution successful");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\Play.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */