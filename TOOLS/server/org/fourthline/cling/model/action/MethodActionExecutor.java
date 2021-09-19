/*     */ package org.fourthline.cling.model.action;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.profile.RemoteClientInfo;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.seamless.util.Reflections;
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
/*     */ 
/*     */ public class MethodActionExecutor
/*     */   extends AbstractActionExecutor
/*     */ {
/*  46 */   private static Logger log = Logger.getLogger(MethodActionExecutor.class.getName());
/*     */   
/*     */   protected Method method;
/*     */   
/*     */   public MethodActionExecutor(Method method) {
/*  51 */     this.method = method;
/*     */   }
/*     */   
/*     */   public MethodActionExecutor(Map<ActionArgument<LocalService>, StateVariableAccessor> outputArgumentAccessors, Method method) {
/*  55 */     super(outputArgumentAccessors);
/*  56 */     this.method = method;
/*     */   }
/*     */   
/*     */   public Method getMethod() {
/*  60 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void execute(ActionInvocation<LocalService> actionInvocation, Object serviceImpl) throws Exception {
/*  67 */     Object result, inputArgumentValues[] = createInputArgumentValues(actionInvocation, this.method);
/*     */ 
/*     */     
/*  70 */     if (!actionInvocation.getAction().hasOutputArguments()) {
/*  71 */       log.fine("Calling local service method with no output arguments: " + this.method);
/*  72 */       Reflections.invoke(this.method, serviceImpl, inputArgumentValues);
/*     */       
/*     */       return;
/*     */     } 
/*  76 */     boolean isVoid = this.method.getReturnType().equals(void.class);
/*     */     
/*  78 */     log.fine("Calling local service method with output arguments: " + this.method);
/*     */     
/*  80 */     boolean isArrayResultProcessed = true;
/*  81 */     if (isVoid) {
/*     */       
/*  83 */       log.fine("Action method is void, calling declared accessors(s) on service instance to retrieve ouput argument(s)");
/*  84 */       Reflections.invoke(this.method, serviceImpl, inputArgumentValues);
/*  85 */       result = readOutputArgumentValues(actionInvocation.getAction(), serviceImpl);
/*     */     }
/*  87 */     else if (isUseOutputArgumentAccessors(actionInvocation)) {
/*     */       
/*  89 */       log.fine("Action method is not void, calling declared accessor(s) on returned instance to retrieve ouput argument(s)");
/*  90 */       Object returnedInstance = Reflections.invoke(this.method, serviceImpl, inputArgumentValues);
/*  91 */       result = readOutputArgumentValues(actionInvocation.getAction(), returnedInstance);
/*     */     }
/*     */     else {
/*     */       
/*  95 */       log.fine("Action method is not void, using returned value as (single) output argument");
/*  96 */       result = Reflections.invoke(this.method, serviceImpl, inputArgumentValues);
/*  97 */       isArrayResultProcessed = false;
/*     */     } 
/*     */     
/* 100 */     ActionArgument[] arrayOfActionArgument = actionInvocation.getAction().getOutputArguments();
/*     */     
/* 102 */     if (isArrayResultProcessed && result instanceof Object[]) {
/* 103 */       Object[] results = (Object[])result;
/* 104 */       log.fine("Accessors returned Object[], setting output argument values: " + results.length);
/* 105 */       for (int i = 0; i < arrayOfActionArgument.length; i++) {
/* 106 */         setOutputArgumentValue(actionInvocation, arrayOfActionArgument[i], results[i]);
/*     */       }
/* 108 */     } else if (arrayOfActionArgument.length == 1) {
/* 109 */       setOutputArgumentValue(actionInvocation, arrayOfActionArgument[0], result);
/*     */     } else {
/* 111 */       throw new ActionException(ErrorCode.ACTION_FAILED, "Method return does not match required number of output arguments: " + arrayOfActionArgument.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUseOutputArgumentAccessors(ActionInvocation<LocalService> actionInvocation) {
/* 120 */     for (ActionArgument argument : actionInvocation.getAction().getOutputArguments()) {
/*     */       
/* 122 */       if (getOutputArgumentAccessors().get(argument) != null) {
/* 123 */         return true;
/*     */       }
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object[] createInputArgumentValues(ActionInvocation<LocalService> actionInvocation, Method method) throws ActionException {
/* 131 */     LocalService service = (LocalService)actionInvocation.getAction().getService();
/*     */     
/* 133 */     List<Object> values = new ArrayList();
/* 134 */     int i = 0;
/* 135 */     for (ActionArgument<LocalService> argument : actionInvocation.getAction().getInputArguments()) {
/*     */       
/* 137 */       Class<?> methodParameterType = method.getParameterTypes()[i];
/*     */       
/* 139 */       ActionArgumentValue<LocalService> inputValue = actionInvocation.getInput(argument);
/*     */ 
/*     */       
/* 142 */       if (methodParameterType.isPrimitive() && (inputValue == null || inputValue.toString().length() == 0)) {
/* 143 */         throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Primitive action method argument '" + argument
/*     */             
/* 145 */             .getName() + "' requires input value, can't be null or empty string");
/*     */       }
/*     */ 
/*     */       
/* 149 */       if (inputValue == null) {
/* 150 */         values.add(i++, null);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 155 */         String inputCallValueString = inputValue.toString();
/*     */         
/* 157 */         if (inputCallValueString.length() > 0 && service.isStringConvertibleType(methodParameterType) && !methodParameterType.isEnum()) {
/*     */           try {
/* 159 */             Constructor<String> ctor = (Constructor)methodParameterType.getConstructor(new Class[] { String.class });
/* 160 */             log.finer("Creating new input argument value instance with String.class constructor of type: " + methodParameterType);
/* 161 */             Object o = ctor.newInstance(new Object[] { inputCallValueString });
/* 162 */             values.add(i++, o);
/* 163 */           } catch (Exception ex) {
/* 164 */             log.warning("Error preparing action method call: " + method);
/* 165 */             log.warning("Can't convert input argument string to desired type of '" + argument.getName() + "': " + ex);
/* 166 */             throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Can't convert input argument string to desired type of '" + argument
/* 167 */                 .getName() + "': " + ex);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 172 */           values.add(i++, inputValue.getValue());
/*     */         } 
/*     */       } 
/*     */     } 
/* 176 */     if ((method.getParameterTypes()).length > 0 && RemoteClientInfo.class
/* 177 */       .isAssignableFrom(method.getParameterTypes()[(method.getParameterTypes()).length - 1])) {
/* 178 */       if (actionInvocation instanceof RemoteActionInvocation && ((RemoteActionInvocation)actionInvocation)
/* 179 */         .getRemoteClientInfo() != null) {
/* 180 */         log.finer("Providing remote client info as last action method input argument: " + method);
/* 181 */         values.add(i, ((RemoteActionInvocation)actionInvocation).getRemoteClientInfo());
/*     */       } else {
/*     */         
/* 184 */         values.add(i, null);
/*     */       } 
/*     */     }
/*     */     
/* 188 */     return values.toArray(new Object[values.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\MethodActionExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */