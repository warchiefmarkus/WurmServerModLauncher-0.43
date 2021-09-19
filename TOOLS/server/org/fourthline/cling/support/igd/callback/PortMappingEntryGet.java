/*    */ package org.fourthline.cling.support.igd.callback;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
/*    */ import org.fourthline.cling.support.model.PortMapping;
/*    */ 
/*    */ public abstract class PortMappingEntryGet
/*    */   extends ActionCallback
/*    */ {
/*    */   public PortMappingEntryGet(Service service, long index) {
/* 16 */     this(service, null, index);
/*    */   }
/*    */   
/*    */   protected PortMappingEntryGet(Service service, ControlPoint controlPoint, long index) {
/* 20 */     super(new ActionInvocation(service.getAction("GetGenericPortMappingEntry")), controlPoint);
/*    */     
/* 22 */     getActionInvocation().setInput("NewPortMappingIndex", new UnsignedIntegerTwoBytes(index));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 28 */     Map<String, ActionArgumentValue<Service>> outputMap = invocation.getOutputMap();
/* 29 */     success(new PortMapping(outputMap));
/*    */   }
/*    */   
/*    */   protected abstract void success(PortMapping paramPortMapping);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\callback\PortMappingEntryGet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */