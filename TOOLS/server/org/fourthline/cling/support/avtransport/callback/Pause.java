/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
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
/*    */ public abstract class Pause
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(Pause.class.getName());
/*    */   
/*    */   protected Pause(ActionInvocation actionInvocation, ControlPoint controlPoint) {
/* 35 */     super(actionInvocation, controlPoint);
/*    */   }
/*    */   
/*    */   protected Pause(ActionInvocation actionInvocation) {
/* 39 */     super(actionInvocation);
/*    */   }
/*    */   
/*    */   public Pause(Service service) {
/* 43 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public Pause(UnsignedIntegerFourBytes instanceId, Service service) {
/* 47 */     super(new ActionInvocation(service.getAction("Pause")));
/* 48 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 53 */     log.fine("Execution successful");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\Pause.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */