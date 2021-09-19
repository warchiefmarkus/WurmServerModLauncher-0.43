/*    */ package com.sun.xml.bind.v2;
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
/* 46 */   ILLEGAL_ENTRY,
/* 47 */   ERROR_LOADING_CLASS,
/* 48 */   INVALID_PROPERTY_VALUE,
/* 49 */   UNSUPPORTED_PROPERTY,
/* 50 */   BROKEN_CONTEXTPATH,
/* 51 */   NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS,
/* 52 */   INVALID_TYPE_IN_MAP;
/*    */   
/*    */   static {
/* 55 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */   }
/*    */   public String toString() {
/* 58 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 62 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */