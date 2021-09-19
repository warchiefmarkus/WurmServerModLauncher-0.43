/*    */ package org.controlsfx.validation;
/*    */ 
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
/*    */ class SimpleValidationMessage
/*    */   implements ValidationMessage
/*    */ {
/*    */   private final String text;
/*    */   private final Severity severity;
/*    */   private final Control target;
/*    */   
/*    */   public SimpleValidationMessage(Control target, String text, Severity severity) {
/* 41 */     this.text = text;
/* 42 */     this.severity = (severity == null) ? Severity.ERROR : severity;
/* 43 */     this.target = target;
/*    */   }
/*    */   
/*    */   public Control getTarget() {
/* 47 */     return this.target;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 51 */     return this.text;
/*    */   }
/*    */   
/*    */   public Severity getSeverity() {
/* 55 */     return this.severity;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 59 */     int prime = 31;
/* 60 */     int result = 1;
/*    */     
/* 62 */     result = 31 * result + ((this.severity == null) ? 0 : this.severity.hashCode());
/* 63 */     result = 31 * result + ((this.target == null) ? 0 : this.target.hashCode());
/* 64 */     result = 31 * result + ((this.text == null) ? 0 : this.text.hashCode());
/* 65 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 69 */     if (this == obj)
/* 70 */       return true; 
/* 71 */     if (obj == null)
/* 72 */       return false; 
/* 73 */     if (getClass() != obj.getClass())
/* 74 */       return false; 
/* 75 */     SimpleValidationMessage other = (SimpleValidationMessage)obj;
/* 76 */     if (this.severity != other.severity)
/* 77 */       return false; 
/* 78 */     if (this.target == null) {
/* 79 */       if (other.target != null)
/* 80 */         return false; 
/* 81 */     } else if (!this.target.equals(other.target)) {
/* 82 */       return false;
/* 83 */     }  if (this.text == null) {
/* 84 */       if (other.text != null)
/* 85 */         return false; 
/* 86 */     } else if (!this.text.equals(other.text)) {
/* 87 */       return false;
/* 88 */     }  return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 92 */     return String.format("%s(%s)", new Object[] { this.severity, this.text });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\SimpleValidationMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */