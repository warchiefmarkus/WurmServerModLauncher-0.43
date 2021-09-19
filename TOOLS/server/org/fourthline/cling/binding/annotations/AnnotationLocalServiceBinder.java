/*     */ package org.fourthline.cling.binding.annotations;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.LocalServiceBinder;
/*     */ import org.fourthline.cling.binding.LocalServiceBindingException;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.action.ActionExecutor;
/*     */ import org.fourthline.cling.model.action.QueryStateVariableExecutor;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.QueryStateVariableAction;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.state.FieldStateVariableAccessor;
/*     */ import org.fourthline.cling.model.state.GetterStateVariableAccessor;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
/*     */ import org.fourthline.cling.model.types.UDAServiceId;
/*     */ import org.fourthline.cling.model.types.UDAServiceType;
/*     */ import org.fourthline.cling.model.types.csv.CSV;
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
/*     */ public class AnnotationLocalServiceBinder
/*     */   implements LocalServiceBinder
/*     */ {
/*  58 */   private static Logger log = Logger.getLogger(AnnotationLocalServiceBinder.class.getName());
/*     */   
/*     */   public LocalService read(Class<?> clazz) throws LocalServiceBindingException {
/*  61 */     log.fine("Reading and binding annotations of service implementation class: " + clazz);
/*     */ 
/*     */     
/*  64 */     if (clazz.isAnnotationPresent((Class)UpnpService.class)) {
/*     */       
/*  66 */       UpnpService annotation = clazz.<UpnpService>getAnnotation(UpnpService.class);
/*  67 */       UpnpServiceId idAnnotation = annotation.serviceId();
/*  68 */       UpnpServiceType typeAnnotation = annotation.serviceType();
/*     */ 
/*     */ 
/*     */       
/*  72 */       ServiceId serviceId = idAnnotation.namespace().equals("upnp-org") ? (ServiceId)new UDAServiceId(idAnnotation.value()) : new ServiceId(idAnnotation.namespace(), idAnnotation.value());
/*     */ 
/*     */ 
/*     */       
/*  76 */       ServiceType serviceType = typeAnnotation.namespace().equals("schemas-upnp-org") ? (ServiceType)new UDAServiceType(typeAnnotation.value(), typeAnnotation.version()) : new ServiceType(typeAnnotation.namespace(), typeAnnotation.value(), typeAnnotation.version());
/*     */       
/*  78 */       boolean supportsQueryStateVariables = annotation.supportsQueryStateVariables();
/*     */       
/*  80 */       Set<Class<?>> stringConvertibleTypes = readStringConvertibleTypes(annotation.stringConvertibleTypes());
/*     */       
/*  82 */       return read(clazz, serviceId, serviceType, supportsQueryStateVariables, stringConvertibleTypes);
/*     */     } 
/*  84 */     throw new LocalServiceBindingException("Given class is not an @UpnpService");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalService read(Class<?> clazz, ServiceId id, ServiceType type, boolean supportsQueryStateVariables, Class[] stringConvertibleTypes) throws LocalServiceBindingException {
/*  90 */     return read(clazz, id, type, supportsQueryStateVariables, (Set)new HashSet<>(Arrays.asList(stringConvertibleTypes)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalService read(Class<?> clazz, ServiceId id, ServiceType type, boolean supportsQueryStateVariables, Set<Class<?>> stringConvertibleTypes) throws LocalServiceBindingException {
/*  97 */     Map<StateVariable, StateVariableAccessor> stateVariables = readStateVariables(clazz, stringConvertibleTypes);
/*  98 */     Map<Action, ActionExecutor> actions = readActions(clazz, stateVariables, stringConvertibleTypes);
/*     */ 
/*     */     
/* 101 */     if (supportsQueryStateVariables) {
/* 102 */       actions.put(new QueryStateVariableAction(), new QueryStateVariableExecutor());
/*     */     }
/*     */     
/*     */     try {
/* 106 */       return new LocalService(type, id, actions, stateVariables, stringConvertibleTypes, supportsQueryStateVariables);
/*     */     }
/* 108 */     catch (ValidationException ex) {
/* 109 */       log.severe("Could not validate device model: " + ex.toString());
/* 110 */       for (ValidationError validationError : ex.getErrors()) {
/* 111 */         log.severe(validationError.toString());
/*     */       }
/* 113 */       throw new LocalServiceBindingException("Validation of model failed, check the log");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Class> readStringConvertibleTypes(Class[] declaredTypes) throws LocalServiceBindingException {
/* 119 */     for (Class stringConvertibleType : declaredTypes) {
/* 120 */       if (!Modifier.isPublic(stringConvertibleType.getModifiers())) {
/* 121 */         throw new LocalServiceBindingException("Declared string-convertible type must be public: " + stringConvertibleType);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 126 */         stringConvertibleType.getConstructor(new Class[] { String.class });
/* 127 */       } catch (NoSuchMethodException ex) {
/* 128 */         throw new LocalServiceBindingException("Declared string-convertible type needs a public single-argument String constructor: " + stringConvertibleType);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 133 */     Set<Class<?>> stringConvertibleTypes = new HashSet<>(Arrays.asList(declaredTypes));
/*     */ 
/*     */     
/* 136 */     stringConvertibleTypes.add(URI.class);
/* 137 */     stringConvertibleTypes.add(URL.class);
/* 138 */     stringConvertibleTypes.add(CSV.class);
/*     */     
/* 140 */     return stringConvertibleTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<StateVariable, StateVariableAccessor> readStateVariables(Class<?> clazz, Set<Class<?>> stringConvertibleTypes) throws LocalServiceBindingException {
/* 146 */     Map<StateVariable, StateVariableAccessor> map = new HashMap<>();
/*     */ 
/*     */     
/* 149 */     if (clazz.isAnnotationPresent((Class)UpnpStateVariables.class)) {
/* 150 */       UpnpStateVariables variables = clazz.<UpnpStateVariables>getAnnotation(UpnpStateVariables.class);
/* 151 */       for (UpnpStateVariable v : variables.value()) {
/*     */         GetterStateVariableAccessor getterStateVariableAccessor;
/* 153 */         if (v.name().length() == 0) {
/* 154 */           throw new LocalServiceBindingException("Class-level @UpnpStateVariable name attribute value required");
/*     */         }
/* 156 */         String javaPropertyName = toJavaStateVariableName(v.name());
/*     */         
/* 158 */         Method getter = Reflections.getGetterMethod(clazz, javaPropertyName);
/* 159 */         Field field = Reflections.getField(clazz, javaPropertyName);
/*     */         
/* 161 */         StateVariableAccessor accessor = null;
/* 162 */         if (getter != null && field != null) {
/* 163 */           accessor = variables.preferFields() ? (StateVariableAccessor)new FieldStateVariableAccessor(field) : (StateVariableAccessor)new GetterStateVariableAccessor(getter);
/*     */         
/*     */         }
/* 166 */         else if (field != null) {
/* 167 */           FieldStateVariableAccessor fieldStateVariableAccessor = new FieldStateVariableAccessor(field);
/* 168 */         } else if (getter != null) {
/* 169 */           getterStateVariableAccessor = new GetterStateVariableAccessor(getter);
/*     */         } else {
/* 171 */           log.finer("No field or getter found for state variable, skipping accessor: " + v.name());
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 176 */         StateVariable stateVar = (new AnnotationStateVariableBinder(v, v.name(), (StateVariableAccessor)getterStateVariableAccessor, stringConvertibleTypes)).createStateVariable();
/*     */         
/* 178 */         map.put(stateVar, getterStateVariableAccessor);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 183 */     for (Field field : Reflections.getFields(clazz, UpnpStateVariable.class)) {
/*     */       
/* 185 */       UpnpStateVariable svAnnotation = field.<UpnpStateVariable>getAnnotation(UpnpStateVariable.class);
/*     */       
/* 187 */       FieldStateVariableAccessor fieldStateVariableAccessor = new FieldStateVariableAccessor(field);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       StateVariable stateVar = (new AnnotationStateVariableBinder(svAnnotation, (svAnnotation.name().length() == 0) ? toUpnpStateVariableName(field.getName()) : svAnnotation.name(), (StateVariableAccessor)fieldStateVariableAccessor, stringConvertibleTypes)).createStateVariable();
/*     */       
/* 198 */       map.put(stateVar, fieldStateVariableAccessor);
/*     */     } 
/*     */ 
/*     */     
/* 202 */     for (Method getter : Reflections.getMethods(clazz, UpnpStateVariable.class)) {
/*     */       
/* 204 */       String propertyName = Reflections.getMethodPropertyName(getter.getName());
/* 205 */       if (propertyName == null) {
/* 206 */         throw new LocalServiceBindingException("Annotated method is not a getter method (: " + getter);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 211 */       if ((getter.getParameterTypes()).length > 0) {
/* 212 */         throw new LocalServiceBindingException("Getter method defined as @UpnpStateVariable can not have parameters: " + getter);
/*     */       }
/*     */ 
/*     */       
/* 216 */       UpnpStateVariable svAnnotation = getter.<UpnpStateVariable>getAnnotation(UpnpStateVariable.class);
/*     */       
/* 218 */       GetterStateVariableAccessor getterStateVariableAccessor = new GetterStateVariableAccessor(getter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       StateVariable stateVar = (new AnnotationStateVariableBinder(svAnnotation, (svAnnotation.name().length() == 0) ? toUpnpStateVariableName(propertyName) : svAnnotation.name(), (StateVariableAccessor)getterStateVariableAccessor, stringConvertibleTypes)).createStateVariable();
/*     */       
/* 230 */       map.put(stateVar, getterStateVariableAccessor);
/*     */     } 
/*     */     
/* 233 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Action, ActionExecutor> readActions(Class<?> clazz, Map<StateVariable, StateVariableAccessor> stateVariables, Set<Class<?>> stringConvertibleTypes) throws LocalServiceBindingException {
/* 241 */     Map<Action, ActionExecutor> map = new HashMap<>();
/*     */     
/* 243 */     for (Method method : Reflections.getMethods(clazz, UpnpAction.class)) {
/* 244 */       AnnotationActionBinder actionBinder = new AnnotationActionBinder(method, stateVariables, stringConvertibleTypes);
/*     */       
/* 246 */       Action action = actionBinder.appendAction(map);
/* 247 */       if (isActionExcluded(action)) {
/* 248 */         map.remove(action);
/*     */       }
/*     */     } 
/*     */     
/* 252 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isActionExcluded(Action action) {
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String toUpnpStateVariableName(String javaName) {
/* 265 */     if (javaName.length() < 1) {
/* 266 */       throw new IllegalArgumentException("Variable name must be at least 1 character long");
/*     */     }
/* 268 */     return javaName.substring(0, 1).toUpperCase(Locale.ROOT) + javaName.substring(1);
/*     */   }
/*     */   
/*     */   static String toJavaStateVariableName(String upnpName) {
/* 272 */     if (upnpName.length() < 1) {
/* 273 */       throw new IllegalArgumentException("Variable name must be at least 1 character long");
/*     */     }
/* 275 */     return upnpName.substring(0, 1).toLowerCase(Locale.ROOT) + upnpName.substring(1);
/*     */   }
/*     */ 
/*     */   
/*     */   static String toUpnpActionName(String javaName) {
/* 280 */     if (javaName.length() < 1) {
/* 281 */       throw new IllegalArgumentException("Action name must be at least 1 character long");
/*     */     }
/* 283 */     return javaName.substring(0, 1).toUpperCase(Locale.ROOT) + javaName.substring(1);
/*     */   }
/*     */   
/*     */   static String toJavaActionName(String upnpName) {
/* 287 */     if (upnpName.length() < 1) {
/* 288 */       throw new IllegalArgumentException("Variable name must be at least 1 character long");
/*     */     }
/* 290 */     return upnpName.substring(0, 1).toLowerCase(Locale.ROOT) + upnpName.substring(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\AnnotationLocalServiceBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */