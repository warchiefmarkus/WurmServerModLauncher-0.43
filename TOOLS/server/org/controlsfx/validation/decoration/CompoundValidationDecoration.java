/*    */ package org.controlsfx.validation.decoration;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javafx.scene.control.Control;
/*    */ import org.controlsfx.control.decoration.Decoration;
/*    */ import org.controlsfx.validation.ValidationMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompoundValidationDecoration
/*    */   extends AbstractValidationDecoration
/*    */ {
/* 50 */   private final Set<ValidationDecoration> decorators = new HashSet<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompoundValidationDecoration(Collection<ValidationDecoration> decorators) {
/* 57 */     this.decorators.addAll(decorators);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompoundValidationDecoration(ValidationDecoration... decorators) {
/* 65 */     this(Arrays.asList(decorators));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void applyRequiredDecoration(Control target) {
/* 73 */     this.decorators.stream().forEach(d -> d.applyRequiredDecoration(target));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void applyValidationDecoration(ValidationMessage message) {
/* 81 */     this.decorators.stream().forEach(d -> d.applyValidationDecoration(message));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
/* 86 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<Decoration> createRequiredDecorations(Control target) {
/* 91 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\decoration\CompoundValidationDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */