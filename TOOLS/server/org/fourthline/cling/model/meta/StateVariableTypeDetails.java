/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StateVariableTypeDetails
/*     */   implements Validatable
/*     */ {
/*  36 */   private static final Logger log = Logger.getLogger(StateVariableTypeDetails.class.getName());
/*     */   
/*     */   private final Datatype datatype;
/*     */   private final String defaultValue;
/*     */   private final String[] allowedValues;
/*     */   private final StateVariableAllowedValueRange allowedValueRange;
/*     */   
/*     */   public StateVariableTypeDetails(Datatype datatype) {
/*  44 */     this(datatype, null, null, null);
/*     */   }
/*     */   
/*     */   public StateVariableTypeDetails(Datatype datatype, String defaultValue) {
/*  48 */     this(datatype, defaultValue, null, null);
/*     */   }
/*     */   
/*     */   public StateVariableTypeDetails(Datatype datatype, String defaultValue, String[] allowedValues, StateVariableAllowedValueRange allowedValueRange) {
/*  52 */     this.datatype = datatype;
/*  53 */     this.defaultValue = defaultValue;
/*  54 */     this.allowedValues = allowedValues;
/*  55 */     this.allowedValueRange = allowedValueRange;
/*     */   }
/*     */   
/*     */   public Datatype getDatatype() {
/*  59 */     return this.datatype;
/*     */   }
/*     */   
/*     */   public String getDefaultValue() {
/*  63 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAllowedValues() {
/*  68 */     if (!foundDefaultInAllowedValues(this.defaultValue, this.allowedValues)) {
/*  69 */       List<String> list = new ArrayList<>(Arrays.asList(this.allowedValues));
/*  70 */       list.add(getDefaultValue());
/*  71 */       return list.<String>toArray(new String[list.size()]);
/*     */     } 
/*  73 */     return this.allowedValues;
/*     */   }
/*     */   
/*     */   public StateVariableAllowedValueRange getAllowedValueRange() {
/*  77 */     return this.allowedValueRange;
/*     */   }
/*     */   
/*     */   protected boolean foundDefaultInAllowedValues(String defaultValue, String[] allowedValues) {
/*  81 */     if (defaultValue == null || allowedValues == null) return true; 
/*  82 */     for (String s : allowedValues) {
/*  83 */       if (s.equals(defaultValue)) return true; 
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/*  89 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/*  91 */     if (getDatatype() == null) {
/*  92 */       errors.add(new ValidationError(
/*  93 */             getClass(), "datatype", "Service state variable has no datatype"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (getAllowedValues() != null) {
/*     */       
/* 101 */       if (getAllowedValueRange() != null) {
/* 102 */         errors.add(new ValidationError(
/* 103 */               getClass(), "allowedValues", "Allowed value list of state variable can not also be restricted with allowed value range"));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       if (!Datatype.Builtin.STRING.equals(getDatatype().getBuiltin())) {
/* 110 */         errors.add(new ValidationError(
/* 111 */               getClass(), "allowedValues", "Allowed value list of state variable only available for string datatype, not: " + 
/*     */               
/* 113 */               getDatatype()));
/*     */       }
/*     */ 
/*     */       
/* 117 */       for (String s : getAllowedValues()) {
/* 118 */         if (s.length() > 31) {
/* 119 */           log.warning("UPnP specification violation, allowed value string must be less than 32 chars: " + s);
/*     */         }
/*     */       } 
/*     */       
/* 123 */       if (!foundDefaultInAllowedValues(this.defaultValue, this.allowedValues)) {
/* 124 */         log.warning("UPnP specification violation, allowed string values don't contain default value: " + this.defaultValue);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (getAllowedValueRange() != null) {
/* 131 */       errors.addAll(getAllowedValueRange().validate());
/*     */     }
/*     */     
/* 134 */     return errors;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\StateVariableTypeDetails.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */