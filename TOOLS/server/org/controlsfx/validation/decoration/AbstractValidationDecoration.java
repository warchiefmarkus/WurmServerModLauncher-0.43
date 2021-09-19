/*     */ package org.controlsfx.validation.decoration;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import org.controlsfx.control.decoration.Decoration;
/*     */ import org.controlsfx.control.decoration.Decorator;
/*     */ import org.controlsfx.validation.ValidationMessage;
/*     */ import org.controlsfx.validation.ValidationSupport;
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
/*     */ public abstract class AbstractValidationDecoration
/*     */   implements ValidationDecoration
/*     */ {
/*     */   private static final String VALIDATION_DECORATION = "$org.controlsfx.decoration.vaidation$";
/*     */   
/*     */   private static boolean isValidationDecoration(Decoration decoration) {
/*  53 */     return (decoration != null && decoration.getProperties().get("$org.controlsfx.decoration.vaidation$") == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   private static void setValidationDecoration(Decoration decoration) {
/*  57 */     if (decoration != null) {
/*  58 */       decoration.getProperties().put("$org.controlsfx.decoration.vaidation$", Boolean.TRUE);
/*     */     }
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
/*     */   public void removeDecorations(Control target) {
/*  71 */     ObservableList observableList = Decorator.getDecorations((Node)target);
/*  72 */     if (observableList != null)
/*     */     {
/*  74 */       for (Decoration d : (Decoration[])Decorator.getDecorations((Node)target).toArray((Object[])new Decoration[0])) {
/*  75 */         if (isValidationDecoration(d)) Decorator.removeDecoration((Node)target, d);
/*     */       
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyValidationDecoration(ValidationMessage message) {
/*  86 */     createValidationDecorations(message).stream().forEach(d -> decorate(message.getTarget(), d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRequiredDecoration(Control target) {
/*  95 */     if (ValidationSupport.isRequired(target)) {
/*  96 */       createRequiredDecorations(target).stream().forEach(d -> decorate(target, d));
/*     */     }
/*     */   }
/*     */   
/*     */   private void decorate(Control target, Decoration d) {
/* 101 */     setValidationDecoration(d);
/* 102 */     Decorator.addDecoration((Node)target, d);
/*     */   }
/*     */   
/*     */   protected abstract Collection<Decoration> createValidationDecorations(ValidationMessage paramValidationMessage);
/*     */   
/*     */   protected abstract Collection<Decoration> createRequiredDecorations(Control paramControl);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\decoration\AbstractValidationDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */