/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
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
/*    */ enum Messages
/*    */ {
/* 47 */   ERR_CANNOT_BE_BOUND_TO_SIMPLETYPE,
/* 48 */   ERR_UNDEFINED_SIMPLE_TYPE,
/* 49 */   ERR_ILLEGAL_FIXEDATTR;
/*    */ 
/*    */ 
/*    */   
/*    */   String format(Object... args) {
/* 54 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(name());
/* 55 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */