/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.PositionInfo;
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
/*    */ public abstract class GetPositionInfo
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(GetPositionInfo.class.getName());
/*    */   
/*    */   public GetPositionInfo(Service service) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public GetPositionInfo(UnsignedIntegerFourBytes instanceId, Service service) {
/* 39 */     super(new ActionInvocation(service.getAction("GetPositionInfo")));
/* 40 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 44 */     PositionInfo positionInfo = new PositionInfo(invocation.getOutputMap());
/* 45 */     received(invocation, positionInfo);
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, PositionInfo paramPositionInfo);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\GetPositionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */