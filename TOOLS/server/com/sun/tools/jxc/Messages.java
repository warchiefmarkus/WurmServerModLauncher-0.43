/*    */ package com.sun.tools.jxc;
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
/* 49 */   UNEXPECTED_NGCC_TOKEN,
/* 50 */   BASEDIR_DOESNT_EXIST,
/* 51 */   USAGE,
/* 52 */   VERSION;
/*    */   
/*    */   static {
/* 55 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 58 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 62 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */