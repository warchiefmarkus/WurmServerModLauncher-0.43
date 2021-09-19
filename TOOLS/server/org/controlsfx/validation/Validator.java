/*     */ package org.controlsfx.validation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Validator<T>
/*     */   extends BiFunction<Control, T, ValidationResult>
/*     */ {
/*     */   @SafeVarargs
/*     */   static <T> Validator<T> combine(Validator<T>... validators) {
/*  54 */     return (control, value) -> (ValidationResult)Stream.<Validator>of(validators).map(()).collect(Collectors.reducing(new ValidationResult(), ValidationResult::combine));
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
/*     */   static <T> Validator<T> createEmptyValidator(String message, Severity severity) {
/*  66 */     return (c, value) -> {
/*     */         boolean condition = (value instanceof String) ? value.toString().trim().isEmpty() : ((value == null));
/*     */         return ValidationResult.fromMessageIf(c, message, severity, condition);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Validator<T> createEmptyValidator(String message) {
/*  79 */     return createEmptyValidator(message, Severity.ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Validator<T> createEqualsValidator(String message, Severity severity, Collection<T> values) {
/*  89 */     return (c, value) -> ValidationResult.fromMessageIf(c, message, severity, !values.contains(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Validator<T> createEqualsValidator(String message, Collection<T> values) {
/* 100 */     return createEqualsValidator(message, Severity.ERROR, values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Validator<T> createPredicateValidator(Predicate<T> predicate, String message) {
/* 111 */     return createPredicateValidator(predicate, message, Severity.ERROR);
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
/*     */   static <T> Validator<T> createPredicateValidator(Predicate<T> predicate, String message, Severity severity) {
/* 123 */     return (control, value) -> ValidationResult.fromMessageIf(control, message, severity, !predicate.test(value));
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
/*     */ 
/*     */ 
/*     */   
/*     */   static Validator<String> createRegexValidator(String message, String regex, Severity severity) {
/* 138 */     return (c, value) -> {
/*     */         boolean condition = (value == null) ? true : (!Pattern.matches(regex, value));
/*     */         return ValidationResult.fromMessageIf(c, message, severity, condition);
/*     */       };
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
/*     */   static Validator<String> createRegexValidator(String message, Pattern regex, Severity severity) {
/* 153 */     return (c, value) -> {
/*     */         boolean condition = (value == null) ? true : (!regex.matcher(value).matches());
/*     */         return ValidationResult.fromMessageIf(c, message, severity, condition);
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\Validator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */