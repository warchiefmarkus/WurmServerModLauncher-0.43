/*     */ package org.fourthline.cling.binding.annotations;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.binding.AllowedValueProvider;
/*     */ import org.fourthline.cling.binding.AllowedValueRangeProvider;
/*     */ import org.fourthline.cling.binding.LocalServiceBindingException;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.meta.StateVariableAllowedValueRange;
/*     */ import org.fourthline.cling.model.meta.StateVariableEventDetails;
/*     */ import org.fourthline.cling.model.meta.StateVariableTypeDetails;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.types.Datatype;
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
/*     */ public class AnnotationStateVariableBinder
/*     */ {
/*  37 */   private static Logger log = Logger.getLogger(AnnotationLocalServiceBinder.class.getName());
/*     */   
/*     */   protected UpnpStateVariable annotation;
/*     */   
/*     */   protected String name;
/*     */   protected StateVariableAccessor accessor;
/*     */   protected Set<Class> stringConvertibleTypes;
/*     */   
/*     */   public AnnotationStateVariableBinder(UpnpStateVariable annotation, String name, StateVariableAccessor accessor, Set<Class<?>> stringConvertibleTypes) {
/*  46 */     this.annotation = annotation;
/*  47 */     this.name = name;
/*  48 */     this.accessor = accessor;
/*  49 */     this.stringConvertibleTypes = stringConvertibleTypes;
/*     */   }
/*     */   
/*     */   public UpnpStateVariable getAnnotation() {
/*  53 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public StateVariableAccessor getAccessor() {
/*  61 */     return this.accessor;
/*     */   }
/*     */   
/*     */   public Set<Class> getStringConvertibleTypes() {
/*  65 */     return this.stringConvertibleTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected StateVariable createStateVariable() throws LocalServiceBindingException {
/*  70 */     log.fine("Creating state variable '" + getName() + "' with accessor: " + getAccessor());
/*     */ 
/*     */     
/*  73 */     Datatype datatype = createDatatype();
/*     */ 
/*     */     
/*  76 */     String defaultValue = createDefaultValue(datatype);
/*     */ 
/*     */     
/*  79 */     String[] allowedValues = null;
/*  80 */     if (Datatype.Builtin.STRING.equals(datatype.getBuiltin())) {
/*     */       
/*  82 */       if (getAnnotation().allowedValueProvider() != void.class) {
/*  83 */         allowedValues = getAllowedValuesFromProvider();
/*  84 */       } else if ((getAnnotation().allowedValues()).length > 0) {
/*  85 */         allowedValues = getAnnotation().allowedValues();
/*  86 */       } else if (getAnnotation().allowedValuesEnum() != void.class) {
/*  87 */         allowedValues = getAllowedValues(getAnnotation().allowedValuesEnum());
/*  88 */       } else if (getAccessor() != null && getAccessor().getReturnType().isEnum()) {
/*  89 */         allowedValues = getAllowedValues(getAccessor().getReturnType());
/*     */       } else {
/*  91 */         log.finer("Not restricting allowed values (of string typed state var): " + getName());
/*     */       } 
/*     */       
/*  94 */       if (allowedValues != null && defaultValue != null) {
/*     */ 
/*     */         
/*  97 */         boolean foundValue = false;
/*  98 */         for (String s : allowedValues) {
/*  99 */           if (s.equals(defaultValue)) {
/* 100 */             foundValue = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 104 */         if (!foundValue) {
/* 105 */           throw new LocalServiceBindingException("Default value '" + defaultValue + "' is not in allowed values of: " + 
/* 106 */               getName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 113 */     StateVariableAllowedValueRange allowedValueRange = null;
/* 114 */     if (Datatype.Builtin.isNumeric(datatype.getBuiltin())) {
/*     */       
/* 116 */       if (getAnnotation().allowedValueRangeProvider() != void.class) {
/* 117 */         allowedValueRange = getAllowedRangeFromProvider();
/* 118 */       } else if (getAnnotation().allowedValueMinimum() > 0L || getAnnotation().allowedValueMaximum() > 0L) {
/* 119 */         allowedValueRange = getAllowedValueRange(
/* 120 */             getAnnotation().allowedValueMinimum(), 
/* 121 */             getAnnotation().allowedValueMaximum(), 
/* 122 */             getAnnotation().allowedValueStep());
/*     */       } else {
/*     */         
/* 125 */         log.finer("Not restricting allowed value range (of numeric typed state var): " + getName());
/*     */       } 
/*     */ 
/*     */       
/* 129 */       if (defaultValue != null && allowedValueRange != null) {
/*     */         long v;
/*     */         
/*     */         try {
/* 133 */           v = Long.valueOf(defaultValue).longValue();
/* 134 */         } catch (Exception ex) {
/* 135 */           throw new LocalServiceBindingException("Default value '" + defaultValue + "' is not numeric (for range checking) of: " + 
/* 136 */               getName());
/*     */         } 
/*     */ 
/*     */         
/* 140 */         if (!allowedValueRange.isInRange(v)) {
/* 141 */           throw new LocalServiceBindingException("Default value '" + defaultValue + "' is not in allowed range of: " + 
/* 142 */               getName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 149 */     boolean sendEvents = getAnnotation().sendEvents();
/* 150 */     if (sendEvents && getAccessor() == null) {
/* 151 */       throw new LocalServiceBindingException("State variable sends events but has no accessor for field or getter: " + 
/* 152 */           getName());
/*     */     }
/*     */ 
/*     */     
/* 156 */     int eventMaximumRateMillis = 0;
/* 157 */     int eventMinimumDelta = 0;
/* 158 */     if (sendEvents) {
/* 159 */       if (getAnnotation().eventMaximumRateMilliseconds() > 0) {
/* 160 */         log.finer("Moderating state variable events using maximum rate (milliseconds): " + getAnnotation().eventMaximumRateMilliseconds());
/* 161 */         eventMaximumRateMillis = getAnnotation().eventMaximumRateMilliseconds();
/*     */       } 
/*     */       
/* 164 */       if (getAnnotation().eventMinimumDelta() > 0 && Datatype.Builtin.isNumeric(datatype.getBuiltin())) {
/*     */         
/* 166 */         log.finer("Moderating state variable events using minimum delta: " + getAnnotation().eventMinimumDelta());
/* 167 */         eventMinimumDelta = getAnnotation().eventMinimumDelta();
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     StateVariableTypeDetails typeDetails = new StateVariableTypeDetails(datatype, defaultValue, allowedValues, allowedValueRange);
/*     */ 
/*     */     
/* 174 */     StateVariableEventDetails eventDetails = new StateVariableEventDetails(sendEvents, eventMaximumRateMillis, eventMinimumDelta);
/*     */ 
/*     */     
/* 177 */     return new StateVariable(getName(), typeDetails, eventDetails);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Datatype createDatatype() throws LocalServiceBindingException {
/* 182 */     String declaredDatatype = getAnnotation().datatype();
/*     */     
/* 184 */     if (declaredDatatype.length() == 0 && getAccessor() != null) {
/* 185 */       Class returnType = getAccessor().getReturnType();
/* 186 */       log.finer("Using accessor return type as state variable type: " + returnType);
/*     */       
/* 188 */       if (ModelUtil.isStringConvertibleType(getStringConvertibleTypes(), returnType)) {
/*     */         
/* 190 */         log.finer("Return type is string-convertible, using string datatype");
/* 191 */         return Datatype.Default.STRING.getBuiltinType().getDatatype();
/*     */       } 
/* 193 */       Datatype.Default defaultDatatype = Datatype.Default.getByJavaType(returnType);
/* 194 */       if (defaultDatatype != null) {
/* 195 */         log.finer("Return type has default UPnP datatype: " + defaultDatatype);
/* 196 */         return defaultDatatype.getBuiltinType().getDatatype();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 202 */     if ((declaredDatatype == null || declaredDatatype.length() == 0) && ((
/* 203 */       getAnnotation().allowedValues()).length > 0 || getAnnotation().allowedValuesEnum() != void.class)) {
/* 204 */       log.finer("State variable has restricted allowed values, hence using 'string' datatype");
/* 205 */       declaredDatatype = "string";
/*     */     } 
/*     */ 
/*     */     
/* 209 */     if (declaredDatatype == null || declaredDatatype.length() == 0) {
/* 210 */       throw new LocalServiceBindingException("Could not detect datatype of state variable: " + getName());
/*     */     }
/*     */     
/* 213 */     log.finer("Trying to find built-in UPnP datatype for detected name: " + declaredDatatype);
/*     */ 
/*     */     
/* 216 */     Datatype.Builtin builtin = Datatype.Builtin.getByDescriptorName(declaredDatatype);
/* 217 */     if (builtin != null) {
/* 218 */       log.finer("Found built-in UPnP datatype: " + builtin);
/* 219 */       return builtin.getDatatype();
/*     */     } 
/*     */     
/* 222 */     throw new LocalServiceBindingException("No built-in UPnP datatype found, using CustomDataType (TODO: NOT IMPLEMENTED)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String createDefaultValue(Datatype datatype) throws LocalServiceBindingException {
/* 229 */     if (getAnnotation().defaultValue().length() != 0) {
/*     */       
/*     */       try {
/* 232 */         datatype.valueOf(getAnnotation().defaultValue());
/* 233 */         log.finer("Found state variable default value: " + getAnnotation().defaultValue());
/* 234 */         return getAnnotation().defaultValue();
/* 235 */       } catch (Exception ex) {
/* 236 */         throw new LocalServiceBindingException("Default value doesn't match datatype of state variable '" + 
/* 237 */             getName() + "': " + ex.getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] getAllowedValues(Class enumType) throws LocalServiceBindingException {
/* 247 */     if (!enumType.isEnum()) {
/* 248 */       throw new LocalServiceBindingException("Allowed values type is not an Enum: " + enumType);
/*     */     }
/*     */     
/* 251 */     log.finer("Restricting allowed values of state variable to Enum: " + getName());
/* 252 */     String[] allowedValueStrings = new String[(enumType.getEnumConstants()).length];
/* 253 */     for (int i = 0; i < (enumType.getEnumConstants()).length; i++) {
/* 254 */       Object o = enumType.getEnumConstants()[i];
/* 255 */       if (o.toString().length() > 32) {
/* 256 */         throw new LocalServiceBindingException("Allowed value string (that is, Enum constant name) is longer than 32 characters: " + o
/* 257 */             .toString());
/*     */       }
/*     */       
/* 260 */       log.finer("Adding allowed value (converted to string): " + o.toString());
/* 261 */       allowedValueStrings[i] = o.toString();
/*     */     } 
/*     */     
/* 264 */     return allowedValueStrings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected StateVariableAllowedValueRange getAllowedValueRange(long min, long max, long step) throws LocalServiceBindingException {
/* 270 */     if (max < min) {
/* 271 */       throw new LocalServiceBindingException("Allowed value range maximum is smaller than minimum: " + 
/* 272 */           getName());
/*     */     }
/*     */ 
/*     */     
/* 276 */     return new StateVariableAllowedValueRange(min, max, step);
/*     */   }
/*     */   
/*     */   protected String[] getAllowedValuesFromProvider() throws LocalServiceBindingException {
/* 280 */     Class<?> provider = getAnnotation().allowedValueProvider();
/* 281 */     if (!AllowedValueProvider.class.isAssignableFrom(provider)) {
/* 282 */       throw new LocalServiceBindingException("Allowed value provider is not of type " + AllowedValueProvider.class + ": " + 
/* 283 */           getName());
/*     */     }
/*     */     try {
/* 286 */       return ((AllowedValueProvider)provider.newInstance()).getValues();
/* 287 */     } catch (Exception ex) {
/* 288 */       throw new LocalServiceBindingException("Allowed value provider can't be instantiated: " + 
/* 289 */           getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected StateVariableAllowedValueRange getAllowedRangeFromProvider() throws LocalServiceBindingException {
/* 295 */     Class<?> provider = getAnnotation().allowedValueRangeProvider();
/* 296 */     if (!AllowedValueRangeProvider.class.isAssignableFrom(provider)) {
/* 297 */       throw new LocalServiceBindingException("Allowed value range provider is not of type " + AllowedValueRangeProvider.class + ": " + 
/* 298 */           getName());
/*     */     }
/*     */     
/*     */     try {
/* 302 */       AllowedValueRangeProvider providerInstance = (AllowedValueRangeProvider)provider.newInstance();
/* 303 */       return getAllowedValueRange(providerInstance
/* 304 */           .getMinimum(), providerInstance
/* 305 */           .getMaximum(), providerInstance
/* 306 */           .getStep());
/*     */     }
/* 308 */     catch (Exception ex) {
/* 309 */       throw new LocalServiceBindingException("Allowed value range provider can't be instantiated: " + 
/* 310 */           getName(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\AnnotationStateVariableBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */