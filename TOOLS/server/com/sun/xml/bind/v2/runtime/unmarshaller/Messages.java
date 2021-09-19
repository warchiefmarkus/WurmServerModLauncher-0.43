/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
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
/* 46 */   UNRESOLVED_IDREF,
/* 47 */   UNEXPECTED_ELEMENT,
/* 48 */   UNEXPECTED_TEXT,
/* 49 */   NOT_A_QNAME,
/* 50 */   UNRECOGNIZED_TYPE_NAME,
/* 51 */   UNRECOGNIZED_TYPE_NAME_MAYBE,
/* 52 */   UNABLE_TO_CREATE_MAP,
/* 53 */   UNINTERNED_STRINGS;
/*    */   
/*    */   static {
/* 56 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */   }
/*    */   public String toString() {
/* 59 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 63 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */