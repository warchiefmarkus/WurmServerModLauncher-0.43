/*    */ package com.sun.tools.xjc.generator.bean;
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
/*    */ enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 47 */   METHOD_COLLISION,
/* 48 */   ERR_UNUSABLE_NAME,
/* 49 */   ERR_NAME_COLLISION,
/* 50 */   ILLEGAL_CONSTRUCTOR_PARAM,
/* 51 */   OBJECT_FACTORY_CONFLICT,
/* 52 */   OBJECT_FACTORY_CONFLICT_RELATED;
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */