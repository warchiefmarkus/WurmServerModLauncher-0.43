/*    */ package org.controlsfx.validation;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import javafx.scene.control.Control;
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
/*    */ public interface ValidationMessage
/*    */   extends Comparable<ValidationMessage>
/*    */ {
/* 38 */   public static final Comparator<ValidationMessage> COMPARATOR = new Comparator<ValidationMessage>()
/*    */     {
/*    */       public int compare(ValidationMessage vm1, ValidationMessage vm2)
/*    */       {
/* 42 */         if (vm1 == vm2) return 0; 
/* 43 */         if (vm1 == null) return 1; 
/* 44 */         if (vm2 == null) return -1; 
/* 45 */         return vm1.compareTo(vm2);
/*    */       }
/*    */     };
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
/*    */   static ValidationMessage error(Control target, String text) {
/* 75 */     return new SimpleValidationMessage(target, text, Severity.ERROR);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static ValidationMessage warning(Control target, String text) {
/* 85 */     return new SimpleValidationMessage(target, text, Severity.WARNING);
/*    */   }
/*    */   
/*    */   default int compareTo(ValidationMessage msg) {
/* 89 */     return (msg == null || getTarget() != msg.getTarget()) ? -1 : getSeverity().compareTo(msg.getSeverity());
/*    */   }
/*    */   
/*    */   String getText();
/*    */   
/*    */   Severity getSeverity();
/*    */   
/*    */   Control getTarget();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\ValidationMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */