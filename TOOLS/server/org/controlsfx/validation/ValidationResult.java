/*     */ package org.controlsfx.validation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.scene.control.Control;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidationResult
/*     */ {
/*  44 */   private List<ValidationMessage> errors = new ArrayList<>();
/*  45 */   private List<ValidationMessage> warnings = new ArrayList<>();
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
/*     */   public static final ValidationResult fromMessageIf(Control target, String text, Severity severity, boolean condition) {
/*  61 */     return (new ValidationResult()).addMessageIf(target, text, severity, condition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromErrorIf(Control target, String text, boolean condition) {
/*  72 */     return (new ValidationResult()).addErrorIf(target, text, condition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromWarningIf(Control target, String text, boolean condition) {
/*  83 */     return (new ValidationResult()).addWarningIf(target, text, condition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromError(Control target, String text) {
/*  93 */     return fromMessages(new ValidationMessage[] { ValidationMessage.error(target, text) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromWarning(Control target, String text) {
/* 103 */     return fromMessages(new ValidationMessage[] { ValidationMessage.warning(target, text) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromMessages(ValidationMessage... messages) {
/* 112 */     return (new ValidationResult()).addAll(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromMessages(Collection<? extends ValidationMessage> messages) {
/* 121 */     return (new ValidationResult()).addAll(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromResults(ValidationResult... results) {
/* 130 */     return (new ValidationResult()).combineAll(results);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ValidationResult fromResults(Collection<ValidationResult> results) {
/* 139 */     return (new ValidationResult()).combineAll(results);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult copy() {
/* 147 */     return fromMessages(getMessages());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult add(ValidationMessage message) {
/* 157 */     if (message != null) {
/* 158 */       switch (message.getSeverity()) { case ERROR:
/* 159 */           this.errors.add(message); break;
/* 160 */         case WARNING: this.warnings.add(message);
/*     */           break; }
/*     */     
/*     */     }
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult addMessageIf(Control target, String text, Severity severity, boolean condition) {
/* 176 */     return condition ? add(new SimpleValidationMessage(target, text, severity)) : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult addErrorIf(Control target, String text, boolean condition) {
/* 187 */     return addMessageIf(target, text, Severity.ERROR, condition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult addWarningIf(Control target, String text, boolean condition) {
/* 198 */     return addMessageIf(target, text, Severity.WARNING, condition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult addAll(Collection<? extends ValidationMessage> messages) {
/* 207 */     messages.stream().forEach(msg -> add(msg));
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult addAll(ValidationMessage... messages) {
/* 217 */     return addAll(Arrays.asList(messages));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult combine(ValidationResult validationResult) {
/* 226 */     return (validationResult == null) ? copy() : copy().addAll(validationResult.getMessages());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult combineAll(Collection<ValidationResult> validationResults) {
/* 235 */     return validationResults.stream().reduce(copy(), (x, r) -> (r == null) ? x : x.addAll(r.getMessages()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValidationResult combineAll(ValidationResult... validationResults) {
/* 246 */     return combineAll(Arrays.asList(validationResults));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ValidationMessage> getErrors() {
/* 254 */     return Collections.unmodifiableList(this.errors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ValidationMessage> getWarnings() {
/* 262 */     return Collections.unmodifiableList(this.warnings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ValidationMessage> getMessages() {
/* 270 */     List<ValidationMessage> messages = new ArrayList<>();
/* 271 */     messages.addAll(this.errors);
/* 272 */     messages.addAll(this.warnings);
/* 273 */     return Collections.unmodifiableList(messages);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\ValidationResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */