/*    */ package com.sun.tools.xjc.reader.dtd.bindinfo;
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
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_UNDEFINED_FIELD = "BIConstructor.UndefinedField";
/*    */   
/*    */   static String format(String property, Object... args) {
/* 49 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 50 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */