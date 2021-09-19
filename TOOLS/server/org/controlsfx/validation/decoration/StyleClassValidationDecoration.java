/*    */ package org.controlsfx.validation.decoration;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import javafx.scene.control.Control;
/*    */ import org.controlsfx.control.decoration.Decoration;
/*    */ import org.controlsfx.control.decoration.StyleClassDecoration;
/*    */ import org.controlsfx.validation.Severity;
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
/*    */ 
/*    */ 
/*    */ public class StyleClassValidationDecoration
/*    */   extends AbstractValidationDecoration
/*    */ {
/*    */   private final String errorClass;
/*    */   private final String warningClass;
/*    */   
/*    */   public StyleClassValidationDecoration() {
/* 56 */     this(null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StyleClassValidationDecoration(String errorClass, String warningClass) {
/* 65 */     this.errorClass = (errorClass != null) ? errorClass : "error";
/* 66 */     this.warningClass = (warningClass != null) ? warningClass : "warning";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
/* 72 */     return Arrays.asList(new Decoration[] { (Decoration)new StyleClassDecoration(new String[] { (Severity.ERROR == message.getSeverity()) ? this.errorClass : this.warningClass }) });
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<Decoration> createRequiredDecorations(Control target) {
/* 77 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\decoration\StyleClassValidationDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */