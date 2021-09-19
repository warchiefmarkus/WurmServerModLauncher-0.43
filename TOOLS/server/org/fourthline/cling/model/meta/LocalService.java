/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.ServiceManager;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.action.ActionExecutor;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalService<T>
/*     */   extends Service<LocalDevice, LocalService>
/*     */ {
/*     */   protected final Map<Action, ActionExecutor> actionExecutors;
/*     */   protected final Map<StateVariable, StateVariableAccessor> stateVariableAccessors;
/*     */   protected final Set<Class> stringConvertibleTypes;
/*     */   protected final boolean supportsQueryStateVariables;
/*     */   protected ServiceManager manager;
/*     */   
/*     */   public LocalService(ServiceType serviceType, ServiceId serviceId, Action[] actions, StateVariable[] stateVariables) throws ValidationException {
/*  51 */     super(serviceType, serviceId, (Action<LocalService>[])actions, (StateVariable<LocalService>[])stateVariables);
/*  52 */     this.manager = null;
/*  53 */     this.actionExecutors = new HashMap<>();
/*  54 */     this.stateVariableAccessors = new HashMap<>();
/*  55 */     this.stringConvertibleTypes = (Set)new HashSet<>();
/*  56 */     this.supportsQueryStateVariables = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalService(ServiceType serviceType, ServiceId serviceId, Map<Action, ActionExecutor> actionExecutors, Map<StateVariable, StateVariableAccessor> stateVariableAccessors, Set<Class<?>> stringConvertibleTypes, boolean supportsQueryStateVariables) throws ValidationException {
/*  65 */     super(serviceType, serviceId, (Action<LocalService>[])actionExecutors
/*  66 */         .keySet().toArray((Object[])new Action[actionExecutors.size()]), (StateVariable<LocalService>[])stateVariableAccessors
/*  67 */         .keySet().toArray((Object[])new StateVariable[stateVariableAccessors.size()]));
/*     */ 
/*     */     
/*  70 */     this.supportsQueryStateVariables = supportsQueryStateVariables;
/*  71 */     this.stringConvertibleTypes = stringConvertibleTypes;
/*  72 */     this.stateVariableAccessors = stateVariableAccessors;
/*  73 */     this.actionExecutors = actionExecutors;
/*     */   }
/*     */   
/*     */   public synchronized void setManager(ServiceManager<T> manager) {
/*  77 */     if (this.manager != null) {
/*  78 */       throw new IllegalStateException("Manager is final");
/*     */     }
/*  80 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   public synchronized ServiceManager<T> getManager() {
/*  84 */     if (this.manager == null) {
/*  85 */       throw new IllegalStateException("Unmanaged service, no implementation instance available");
/*     */     }
/*  87 */     return this.manager;
/*     */   }
/*     */   
/*     */   public boolean isSupportsQueryStateVariables() {
/*  91 */     return this.supportsQueryStateVariables;
/*     */   }
/*     */   
/*     */   public Set<Class> getStringConvertibleTypes() {
/*  95 */     return this.stringConvertibleTypes;
/*     */   }
/*     */   
/*     */   public boolean isStringConvertibleType(Object o) {
/*  99 */     return (o != null && isStringConvertibleType(o.getClass()));
/*     */   }
/*     */   
/*     */   public boolean isStringConvertibleType(Class clazz) {
/* 103 */     return ModelUtil.isStringConvertibleType(getStringConvertibleTypes(), clazz);
/*     */   }
/*     */   
/*     */   public StateVariableAccessor getAccessor(String stateVariableName) {
/*     */     StateVariable<LocalService> sv;
/* 108 */     return ((sv = getStateVariable(stateVariableName)) != null) ? getAccessor(sv) : null;
/*     */   }
/*     */   
/*     */   public StateVariableAccessor getAccessor(StateVariable stateVariable) {
/* 112 */     return this.stateVariableAccessors.get(stateVariable);
/*     */   }
/*     */   
/*     */   public ActionExecutor getExecutor(String actionName) {
/*     */     Action<LocalService> action;
/* 117 */     return ((action = getAction(actionName)) != null) ? getExecutor(action) : null;
/*     */   }
/*     */   
/*     */   public ActionExecutor getExecutor(Action action) {
/* 121 */     return this.actionExecutors.get(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getQueryStateVariableAction() {
/* 126 */     return getAction("QueryStateVariable");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return super.toString() + ", Manager: " + this.manager;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\LocalService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */