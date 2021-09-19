/*    */ package com.sun.tools.xjc.generator.bean.field;
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
/*    */ enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 46 */   DEFAULT_GETTER_JAVADOC,
/* 47 */   DEFAULT_SETTER_JAVADOC;
/*    */   
/*    */   static {
/* 50 */     rb = ResourceBundle.getBundle(Messages.class.getName().substring(0, Messages.class.getName().lastIndexOf('.')) + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 53 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 57 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */