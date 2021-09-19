/*     */ package org.fourthline.cling.binding.annotations;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.LocalServiceBindingException;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.action.ActionExecutor;
/*     */ import org.fourthline.cling.model.action.MethodActionExecutor;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.profile.RemoteClientInfo;
/*     */ import org.fourthline.cling.model.state.GetterStateVariableAccessor;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.types.Datatype;
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
/*     */ public class AnnotationActionBinder
/*     */ {
/*  47 */   private static Logger log = Logger.getLogger(AnnotationLocalServiceBinder.class.getName());
/*     */   
/*     */   protected UpnpAction annotation;
/*     */   protected Method method;
/*     */   protected Map<StateVariable, StateVariableAccessor> stateVariables;
/*     */   protected Set<Class> stringConvertibleTypes;
/*     */   
/*     */   public AnnotationActionBinder(Method method, Map<StateVariable, StateVariableAccessor> stateVariables, Set<Class<?>> stringConvertibleTypes) {
/*  55 */     this.annotation = method.<UpnpAction>getAnnotation(UpnpAction.class);
/*  56 */     this.stateVariables = stateVariables;
/*  57 */     this.method = method;
/*  58 */     this.stringConvertibleTypes = stringConvertibleTypes;
/*     */   }
/*     */   
/*     */   public UpnpAction getAnnotation() {
/*  62 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public Map<StateVariable, StateVariableAccessor> getStateVariables() {
/*  66 */     return this.stateVariables;
/*     */   }
/*     */   
/*     */   public Method getMethod() {
/*  70 */     return this.method;
/*     */   }
/*     */   
/*     */   public Set<Class> getStringConvertibleTypes() {
/*  74 */     return this.stringConvertibleTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action appendAction(Map<Action, ActionExecutor> actions) throws LocalServiceBindingException {
/*     */     String name;
/*  80 */     if (getAnnotation().name().length() != 0) {
/*  81 */       name = getAnnotation().name();
/*     */     } else {
/*  83 */       name = AnnotationLocalServiceBinder.toUpnpActionName(getMethod().getName());
/*     */     } 
/*     */     
/*  86 */     log.fine("Creating action and executor: " + name);
/*     */     
/*  88 */     List<ActionArgument> inputArguments = createInputArguments();
/*  89 */     Map<ActionArgument<LocalService>, StateVariableAccessor> outputArguments = createOutputArguments();
/*     */     
/*  91 */     inputArguments.addAll((Collection)outputArguments.keySet());
/*     */     
/*  93 */     ActionArgument[] arrayOfActionArgument = inputArguments.<ActionArgument>toArray(new ActionArgument[inputArguments.size()]);
/*     */     
/*  95 */     Action action = new Action(name, arrayOfActionArgument);
/*  96 */     ActionExecutor executor = createExecutor(outputArguments);
/*     */     
/*  98 */     actions.put(action, executor);
/*  99 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActionExecutor createExecutor(Map<ActionArgument<LocalService>, StateVariableAccessor> outputArguments) {
/* 104 */     return (ActionExecutor)new MethodActionExecutor(outputArguments, getMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ActionArgument> createInputArguments() throws LocalServiceBindingException {
/* 109 */     List<ActionArgument> list = new ArrayList<>();
/*     */ 
/*     */     
/* 112 */     int annotatedParams = 0;
/* 113 */     Annotation[][] params = getMethod().getParameterAnnotations();
/* 114 */     for (int i = 0; i < params.length; i++) {
/* 115 */       Annotation[] param = params[i];
/* 116 */       for (Annotation paramAnnotation : param) {
/* 117 */         if (paramAnnotation instanceof UpnpInputArgument) {
/* 118 */           UpnpInputArgument inputArgumentAnnotation = (UpnpInputArgument)paramAnnotation;
/* 119 */           annotatedParams++;
/*     */ 
/*     */           
/* 122 */           String argumentName = inputArgumentAnnotation.name();
/*     */ 
/*     */           
/* 125 */           StateVariable stateVariable = findRelatedStateVariable(inputArgumentAnnotation
/* 126 */               .stateVariable(), argumentName, 
/*     */               
/* 128 */               getMethod().getName());
/*     */ 
/*     */           
/* 131 */           if (stateVariable == null) {
/* 132 */             throw new LocalServiceBindingException("Could not detected related state variable of argument: " + argumentName);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 137 */           validateType(stateVariable, getMethod().getParameterTypes()[i]);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           ActionArgument inputArgument = new ActionArgument(argumentName, inputArgumentAnnotation.aliases(), stateVariable.getName(), ActionArgument.Direction.IN);
/*     */ 
/*     */ 
/*     */           
/* 146 */           list.add(inputArgument);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 152 */     if (annotatedParams < (getMethod().getParameterTypes()).length && 
/* 153 */       !RemoteClientInfo.class.isAssignableFrom(this.method.getParameterTypes()[(this.method.getParameterTypes()).length - 1])) {
/* 154 */       throw new LocalServiceBindingException("Method has parameters that are not input arguments: " + getMethod().getName());
/*     */     }
/*     */     
/* 157 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<ActionArgument<LocalService>, StateVariableAccessor> createOutputArguments() throws LocalServiceBindingException {
/* 162 */     Map<ActionArgument<LocalService>, StateVariableAccessor> map = new LinkedHashMap<>();
/*     */     
/* 164 */     UpnpAction actionAnnotation = getMethod().<UpnpAction>getAnnotation(UpnpAction.class);
/* 165 */     if ((actionAnnotation.out()).length == 0) return map;
/*     */     
/* 167 */     boolean hasMultipleOutputArguments = ((actionAnnotation.out()).length > 1);
/*     */     
/* 169 */     for (UpnpOutputArgument outputArgumentAnnotation : actionAnnotation.out()) {
/*     */       
/* 171 */       String argumentName = outputArgumentAnnotation.name();
/*     */       
/* 173 */       StateVariable stateVariable = findRelatedStateVariable(outputArgumentAnnotation
/* 174 */           .stateVariable(), argumentName, 
/*     */           
/* 176 */           getMethod().getName());
/*     */ 
/*     */ 
/*     */       
/* 180 */       if (stateVariable == null && outputArgumentAnnotation.getterName().length() > 0) {
/* 181 */         stateVariable = findRelatedStateVariable(null, null, outputArgumentAnnotation.getterName());
/*     */       }
/*     */       
/* 184 */       if (stateVariable == null) {
/* 185 */         throw new LocalServiceBindingException("Related state variable not found for output argument: " + argumentName);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 190 */       StateVariableAccessor accessor = findOutputArgumentAccessor(stateVariable, outputArgumentAnnotation
/*     */           
/* 192 */           .getterName(), hasMultipleOutputArguments);
/*     */ 
/*     */ 
/*     */       
/* 196 */       log.finer("Found related state variable for output argument '" + argumentName + "': " + stateVariable);
/*     */ 
/*     */ 
/*     */       
/* 200 */       ActionArgument<LocalService> outputArgument = new ActionArgument(argumentName, stateVariable.getName(), ActionArgument.Direction.OUT, !hasMultipleOutputArguments);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 205 */       map.put(outputArgument, accessor);
/*     */     } 
/*     */     
/* 208 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected StateVariableAccessor findOutputArgumentAccessor(StateVariable stateVariable, String getterName, boolean multipleArguments) throws LocalServiceBindingException {
/* 214 */     boolean isVoid = getMethod().getReturnType().equals(void.class);
/*     */     
/* 216 */     if (isVoid) {
/*     */       
/* 218 */       if (getterName != null && getterName.length() > 0) {
/* 219 */         log.finer("Action method is void, will use getter method named: " + getterName);
/*     */ 
/*     */         
/* 222 */         Method getter = Reflections.getMethod(getMethod().getDeclaringClass(), getterName);
/* 223 */         if (getter == null) {
/* 224 */           throw new LocalServiceBindingException("Declared getter method '" + getterName + "' not found on: " + 
/* 225 */               getMethod().getDeclaringClass());
/*     */         }
/*     */         
/* 228 */         validateType(stateVariable, getter.getReturnType());
/*     */         
/* 230 */         return (StateVariableAccessor)new GetterStateVariableAccessor(getter);
/*     */       } 
/*     */       
/* 233 */       log.finer("Action method is void, trying to find existing accessor of related: " + stateVariable);
/* 234 */       return getStateVariables().get(stateVariable);
/*     */     } 
/*     */ 
/*     */     
/* 238 */     if (getterName != null && getterName.length() > 0) {
/* 239 */       log.finer("Action method is not void, will use getter method on returned instance: " + getterName);
/*     */ 
/*     */       
/* 242 */       Method getter = Reflections.getMethod(getMethod().getReturnType(), getterName);
/* 243 */       if (getter == null) {
/* 244 */         throw new LocalServiceBindingException("Declared getter method '" + getterName + "' not found on return type: " + 
/* 245 */             getMethod().getReturnType());
/*     */       }
/*     */       
/* 248 */       validateType(stateVariable, getter.getReturnType());
/*     */       
/* 250 */       return (StateVariableAccessor)new GetterStateVariableAccessor(getter);
/*     */     } 
/* 252 */     if (!multipleArguments) {
/* 253 */       log.finer("Action method is not void, will use the returned instance: " + getMethod().getReturnType());
/* 254 */       validateType(stateVariable, getMethod().getReturnType());
/*     */     } 
/*     */     
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected StateVariable findRelatedStateVariable(String declaredName, String argumentName, String methodName) throws LocalServiceBindingException {
/* 263 */     StateVariable relatedStateVariable = null;
/*     */     
/* 265 */     if (declaredName != null && declaredName.length() > 0) {
/* 266 */       relatedStateVariable = getStateVariable(declaredName);
/*     */     }
/*     */     
/* 269 */     if (relatedStateVariable == null && argumentName != null && argumentName.length() > 0) {
/* 270 */       String actualName = AnnotationLocalServiceBinder.toUpnpStateVariableName(argumentName);
/* 271 */       log.finer("Finding related state variable with argument name (converted to UPnP name): " + actualName);
/* 272 */       relatedStateVariable = getStateVariable(argumentName);
/*     */     } 
/*     */     
/* 275 */     if (relatedStateVariable == null && argumentName != null && argumentName.length() > 0) {
/*     */       
/* 277 */       String actualName = AnnotationLocalServiceBinder.toUpnpStateVariableName(argumentName);
/* 278 */       actualName = "A_ARG_TYPE_" + actualName;
/* 279 */       log.finer("Finding related state variable with prefixed argument name (converted to UPnP name): " + actualName);
/* 280 */       relatedStateVariable = getStateVariable(actualName);
/*     */     } 
/*     */     
/* 283 */     if (relatedStateVariable == null && methodName != null && methodName.length() > 0) {
/*     */       
/* 285 */       String methodPropertyName = Reflections.getMethodPropertyName(methodName);
/* 286 */       if (methodPropertyName != null) {
/* 287 */         log.finer("Finding related state variable with method property name: " + methodPropertyName);
/*     */         
/* 289 */         relatedStateVariable = getStateVariable(
/* 290 */             AnnotationLocalServiceBinder.toUpnpStateVariableName(methodPropertyName));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 295 */     return relatedStateVariable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateType(StateVariable stateVariable, Class type) throws LocalServiceBindingException {
/* 306 */     Datatype.Default expectedDefaultMapping = ModelUtil.isStringConvertibleType(getStringConvertibleTypes(), type) ? Datatype.Default.STRING : Datatype.Default.getByJavaType(type);
/*     */     
/* 308 */     log.finer("Expecting '" + stateVariable + "' to match default mapping: " + expectedDefaultMapping);
/*     */     
/* 310 */     if (expectedDefaultMapping != null && 
/* 311 */       !stateVariable.getTypeDetails().getDatatype().isHandlingJavaType(expectedDefaultMapping.getJavaType()))
/*     */     {
/*     */       
/* 314 */       throw new LocalServiceBindingException("State variable '" + stateVariable + "' datatype can't handle action " + "argument's Java type (change one): " + expectedDefaultMapping
/*     */           
/* 316 */           .getJavaType());
/*     */     }
/*     */     
/* 319 */     if (expectedDefaultMapping == null && stateVariable.getTypeDetails().getDatatype().getBuiltin() != null) {
/* 320 */       throw new LocalServiceBindingException("State variable '" + stateVariable + "' should be custom datatype " + "(action argument type is unknown Java type): " + type
/*     */           
/* 322 */           .getSimpleName());
/*     */     }
/*     */ 
/*     */     
/* 326 */     log.finer("State variable matches required argument datatype (or can't be validated because it is custom)");
/*     */   }
/*     */   
/*     */   protected StateVariable getStateVariable(String name) {
/* 330 */     for (StateVariable stateVariable : getStateVariables().keySet()) {
/* 331 */       if (stateVariable.getName().equals(name)) {
/* 332 */         return stateVariable;
/*     */       }
/*     */     } 
/* 335 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\AnnotationActionBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */