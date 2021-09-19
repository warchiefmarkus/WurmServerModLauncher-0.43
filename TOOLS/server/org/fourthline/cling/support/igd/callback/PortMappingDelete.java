/*    */ package org.fourthline.cling.support.igd.callback;
/*    */ 
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.support.model.PortMapping;
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
/*    */ public abstract class PortMappingDelete
/*    */   extends ActionCallback
/*    */ {
/*    */   protected final PortMapping portMapping;
/*    */   
/*    */   public PortMappingDelete(Service service, PortMapping portMapping) {
/* 32 */     this(service, null, portMapping);
/*    */   }
/*    */   
/*    */   protected PortMappingDelete(Service service, ControlPoint controlPoint, PortMapping portMapping) {
/* 36 */     super(new ActionInvocation(service.getAction("DeletePortMapping")), controlPoint);
/*    */     
/* 38 */     this.portMapping = portMapping;
/*    */     
/* 40 */     getActionInvocation().setInput("NewExternalPort", portMapping.getExternalPort());
/* 41 */     getActionInvocation().setInput("NewProtocol", portMapping.getProtocol());
/* 42 */     if (portMapping.hasRemoteHost())
/* 43 */       getActionInvocation().setInput("NewRemoteHost", portMapping.getRemoteHost()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\callback\PortMappingDelete.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */