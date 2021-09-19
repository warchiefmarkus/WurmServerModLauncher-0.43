/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ServiceReference;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.types.Datatype;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Service<D extends Device, S extends Service>
/*     */ {
/*  38 */   private static final Logger log = Logger.getLogger(Service.class.getName());
/*     */   
/*     */   private final ServiceType serviceType;
/*     */   
/*     */   private final ServiceId serviceId;
/*     */   
/*  44 */   private final Map<String, Action> actions = new HashMap<>();
/*  45 */   private final Map<String, StateVariable> stateVariables = new HashMap<>();
/*     */   
/*     */   private D device;
/*     */ 
/*     */   
/*     */   public Service(ServiceType serviceType, ServiceId serviceId) throws ValidationException {
/*  51 */     this(serviceType, serviceId, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Service(ServiceType serviceType, ServiceId serviceId, Action<S>[] actions, StateVariable<S>[] stateVariables) throws ValidationException {
/*  57 */     this.serviceType = serviceType;
/*  58 */     this.serviceId = serviceId;
/*     */     
/*  60 */     if (actions != null) {
/*  61 */       for (Action<S> action : actions) {
/*  62 */         this.actions.put(action.getName(), action);
/*  63 */         action.setService((S)this);
/*     */       } 
/*     */     }
/*     */     
/*  67 */     if (stateVariables != null) {
/*  68 */       for (StateVariable<S> stateVariable : stateVariables) {
/*  69 */         this.stateVariables.put(stateVariable.getName(), stateVariable);
/*  70 */         stateVariable.setService((S)this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ServiceType getServiceType() {
/*  77 */     return this.serviceType;
/*     */   }
/*     */   
/*     */   public ServiceId getServiceId() {
/*  81 */     return this.serviceId;
/*     */   }
/*     */   
/*     */   public boolean hasActions() {
/*  85 */     return (getActions() != null && (getActions()).length > 0);
/*     */   }
/*     */   
/*     */   public Action<S>[] getActions() {
/*  89 */     return (this.actions == null) ? null : (Action<S>[])this.actions.values().toArray((Object[])new Action[this.actions.values().size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasStateVariables() {
/*  94 */     return (getStateVariables() != null && (getStateVariables()).length > 0);
/*     */   }
/*     */   
/*     */   public StateVariable<S>[] getStateVariables() {
/*  98 */     return (this.stateVariables == null) ? null : (StateVariable<S>[])this.stateVariables.values().toArray((Object[])new StateVariable[this.stateVariables.values().size()]);
/*     */   }
/*     */   
/*     */   public D getDevice() {
/* 102 */     return this.device;
/*     */   }
/*     */   
/*     */   void setDevice(D device) {
/* 106 */     if (this.device != null)
/* 107 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/* 108 */     this.device = device;
/*     */   }
/*     */   
/*     */   public Action<S> getAction(String name) {
/* 112 */     return (this.actions == null) ? null : this.actions.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public StateVariable<S> getStateVariable(String name) {
/* 117 */     if ("VirtualQueryActionInput".equals(name)) {
/* 118 */       return new StateVariable<>("VirtualQueryActionInput", new StateVariableTypeDetails(Datatype.Builtin.STRING
/*     */             
/* 120 */             .getDatatype()));
/*     */     }
/*     */     
/* 123 */     if ("VirtualQueryActionOutput".equals(name)) {
/* 124 */       return new StateVariable<>("VirtualQueryActionOutput", new StateVariableTypeDetails(Datatype.Builtin.STRING
/*     */             
/* 126 */             .getDatatype()));
/*     */     }
/*     */     
/* 129 */     return (this.stateVariables == null) ? null : this.stateVariables.get(name);
/*     */   }
/*     */   
/*     */   public StateVariable<S> getRelatedStateVariable(ActionArgument argument) {
/* 133 */     return getStateVariable(argument.getRelatedStateVariableName());
/*     */   }
/*     */   
/*     */   public Datatype<S> getDatatype(ActionArgument argument) {
/* 137 */     return getRelatedStateVariable(argument).getTypeDetails().getDatatype();
/*     */   }
/*     */   
/*     */   public ServiceReference getReference() {
/* 141 */     return new ServiceReference(getDevice().getIdentity().getUdn(), getServiceId());
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 145 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 147 */     if (getServiceType() == null) {
/* 148 */       errors.add(new ValidationError(
/* 149 */             getClass(), "serviceType", "Service type/info is required"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (getServiceId() == null) {
/* 156 */       errors.add(new ValidationError(
/* 157 */             getClass(), "serviceId", "Service ID is required"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     if (hasStateVariables()) {
/* 177 */       for (StateVariable<S> stateVariable : getStateVariables()) {
/* 178 */         errors.addAll(stateVariable.validate());
/*     */       }
/*     */     }
/*     */     
/* 182 */     if (hasActions()) {
/* 183 */       for (Action<S> action : getActions()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 188 */         List<ValidationError> actionErrors = action.validate();
/* 189 */         if (actionErrors.size() > 0) {
/* 190 */           this.actions.remove(action.getName());
/* 191 */           log.warning("Discarding invalid action of service '" + getServiceId() + "': " + action.getName());
/* 192 */           for (ValidationError actionError : actionErrors) {
/* 193 */             log.warning("Invalid action '" + action.getName() + "': " + actionError);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 199 */     return errors;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Action getQueryStateVariableAction();
/*     */   
/*     */   public String toString() {
/* 206 */     return "(" + getClass().getSimpleName() + ") ServiceId: " + getServiceId();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\Service.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */