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
/*    */ public abstract class Stop
/*    */   extends ActionCallback
/*    */ {
/* 31 */   private static Logger log = Logger.getLogger(Stop.class.getName());
/*    */   
/*    */   public Stop(Service service) {
/* 34 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public Stop(UnsignedIntegerFourBytes instanceId, Service service) {
/* 38 */     super(new ActionInvocation(service.getAction("Stop")));
/* 39 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 44 */     log.fine("Execution successful");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\Stop.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */