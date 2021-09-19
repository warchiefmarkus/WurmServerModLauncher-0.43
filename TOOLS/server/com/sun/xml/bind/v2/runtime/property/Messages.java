/*    */ package com.sun.xml.bind.v2.runtime.property;
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
/* 46 */   UNSUBSTITUTABLE_TYPE,
/* 47 */   UNEXPECTED_JAVA_TYPE;
/*    */   
/*    */   static {
/* 50 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */   }
/*    */   public String toString() {
/* 53 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 57 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */