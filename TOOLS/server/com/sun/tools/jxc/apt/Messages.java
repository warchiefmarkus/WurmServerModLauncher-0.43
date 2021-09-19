/*    */ package com.sun.tools.jxc.apt;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 49 */   NON_EXISTENT_FILE,
/* 50 */   UNRECOGNIZED_PARAMETER,
/* 51 */   OPERAND_MISSING;
/*    */   
/*    */   static {
/* 54 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 57 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 61 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */