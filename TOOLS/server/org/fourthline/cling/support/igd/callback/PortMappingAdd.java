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
/*    */ public abstract class PortMappingAdd
/*    */   extends ActionCallback
/*    */ {
/*    */   protected final PortMapping portMapping;
/*    */   
/*    */   public PortMappingAdd(Service service, PortMapping portMapping) {
/* 32 */     this(service, null, portMapping);
/*    */   }
/*    */   
/*    */   protected PortMappingAdd(Service service, ControlPoint controlPoint, PortMapping portMapping) {
/* 36 */     super(new ActionInvocation(service.getAction("AddPortMapping")), controlPoint);
/*    */     
/* 38 */     this.portMapping = portMapping;
/*    */     
/* 40 */     getActionInvocation().setInput("NewExternalPort", portMapping.getExternalPort());
/* 41 */     getActionInvocation().setInput("NewProtocol", portMapping.getProtocol());
/* 42 */     getActionInvocation().setInput("NewInternalClient", portMapping.getInternalClient());
/* 43 */     getActionInvocation().setInput("NewInternalPort", portMapping.getInternalPort());
/* 44 */     getActionInvocation().setInput("NewLeaseDuration", portMapping.getLeaseDurationSeconds());
/* 45 */     getActionInvocation().setInput("NewEnabled", Boolean.valueOf(portMapping.isEnabled()));
/* 46 */     if (portMapping.hasRemoteHost())
/* 47 */       getActionInvocation().setInput("NewRemoteHost", portMapping.getRemoteHost()); 
/* 48 */     if (portMapping.hasDescription())
/* 49 */       getActionInvocation().setInput("NewPortMappingDescription", portMapping.getDescription()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\igd\callback\PortMappingAdd.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */