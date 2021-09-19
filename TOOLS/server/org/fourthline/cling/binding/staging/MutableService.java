/*    */ package org.fourthline.cling.binding.staging;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.ValidationException;
/*    */ import org.fourthline.cling.model.meta.Action;
/*    */ import org.fourthline.cling.model.meta.Device;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.meta.StateVariable;
/*    */ import org.fourthline.cling.model.types.ServiceId;
/*    */ import org.fourthline.cling.model.types.ServiceType;
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
/*    */ 
/*    */ public class MutableService
/*    */ {
/*    */   public ServiceType serviceType;
/*    */   public ServiceId serviceId;
/*    */   public URI descriptorURI;
/*    */   public URI controlURI;
/*    */   public URI eventSubscriptionURI;
/* 41 */   public List<MutableAction> actions = new ArrayList<>();
/* 42 */   public List<MutableStateVariable> stateVariables = new ArrayList<>();
/*    */   
/*    */   public Service build(Device prototype) throws ValidationException {
/* 45 */     return prototype.newInstance(this.serviceType, this.serviceId, this.descriptorURI, this.controlURI, this.eventSubscriptionURI, 
/*    */ 
/*    */         
/* 48 */         createActions(), 
/* 49 */         createStateVariables());
/*    */   }
/*    */ 
/*    */   
/*    */   public Action[] createActions() {
/* 54 */     Action[] array = new Action[this.actions.size()];
/* 55 */     int i = 0;
/* 56 */     for (MutableAction action : this.actions) {
/* 57 */       array[i++] = action.build();
/*    */     }
/* 59 */     return array;
/*    */   }
/*    */   
/*    */   public StateVariable[] createStateVariables() {
/* 63 */     StateVariable[] array = new StateVariable[this.stateVariables.size()];
/* 64 */     int i = 0;
/* 65 */     for (MutableStateVariable stateVariable : this.stateVariables) {
/* 66 */       array[i++] = stateVariable.build();
/*    */     }
/* 68 */     return array;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */