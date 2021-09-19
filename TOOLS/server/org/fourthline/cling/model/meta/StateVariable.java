/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ModelUtil;
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
/*     */ public class StateVariable<S extends Service>
/*     */   implements Validatable
/*     */ {
/*  35 */   private static final Logger log = Logger.getLogger(StateVariable.class.getName());
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final StateVariableTypeDetails type;
/*     */   
/*     */   private final StateVariableEventDetails eventDetails;
/*     */   private S service;
/*     */   
/*     */   public StateVariable(String name, StateVariableTypeDetails type) {
/*  45 */     this(name, type, new StateVariableEventDetails());
/*     */   }
/*     */   
/*     */   public StateVariable(String name, StateVariableTypeDetails type, StateVariableEventDetails eventDetails) {
/*  49 */     this.name = name;
/*  50 */     this.type = type;
/*  51 */     this.eventDetails = eventDetails;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */   public StateVariableTypeDetails getTypeDetails() {
/*  59 */     return this.type;
/*     */   }
/*     */   
/*     */   public StateVariableEventDetails getEventDetails() {
/*  63 */     return this.eventDetails;
/*     */   }
/*     */   
/*     */   public S getService() {
/*  67 */     return this.service;
/*     */   }
/*     */   
/*     */   void setService(S service) {
/*  71 */     if (this.service != null)
/*  72 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/*  73 */     this.service = service;
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/*  77 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/*  79 */     if (getName() == null || getName().length() == 0) {
/*  80 */       errors.add(new ValidationError(
/*  81 */             getClass(), "name", "StateVariable without name of: " + 
/*     */             
/*  83 */             getService()));
/*     */     }
/*  85 */     else if (!ModelUtil.isValidUDAName(getName())) {
/*  86 */       log.warning("UPnP specification violation of: " + getService().getDevice());
/*  87 */       log.warning("Invalid state variable name: " + this);
/*     */     } 
/*     */     
/*  90 */     errors.addAll(getTypeDetails().validate());
/*     */     
/*  92 */     return errors;
/*     */   }
/*     */   
/*     */   public boolean isModeratedNumericType() {
/*  96 */     return (Datatype.Builtin.isNumeric(
/*  97 */         getTypeDetails().getDatatype().getBuiltin()) && 
/*  98 */       getEventDetails().getEventMinimumDelta() > 0);
/*     */   }
/*     */   
/*     */   public StateVariable<S> deepCopy() {
/* 102 */     return new StateVariable(
/* 103 */         getName(), 
/* 104 */         getTypeDetails(), 
/* 105 */         getEventDetails());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     StringBuilder sb = new StringBuilder();
/* 112 */     sb.append("(").append(getClass().getSimpleName());
/* 113 */     sb.append(", Name: ").append(getName());
/* 114 */     sb.append(", Type: ").append(getTypeDetails().getDatatype().getDisplayString()).append(")");
/* 115 */     if (!getEventDetails().isSendEvents()) {
/* 116 */       sb.append(" (No Events)");
/*     */     }
/* 118 */     if (getTypeDetails().getDefaultValue() != null) {
/* 119 */       sb.append(" Default Value: ").append("'").append(getTypeDetails().getDefaultValue()).append("'");
/*     */     }
/* 121 */     if (getTypeDetails().getAllowedValues() != null) {
/* 122 */       sb.append(" Allowed Values: ");
/* 123 */       for (String s : getTypeDetails().getAllowedValues()) {
/* 124 */         sb.append(s).append("|");
/*     */       }
/*     */     } 
/* 127 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\StateVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */