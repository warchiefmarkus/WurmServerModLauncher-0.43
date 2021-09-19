/*    */ package com.sun.xml.bind.v2.schemagen;
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
/* 46 */   ANONYMOUS_TYPE_CYCLE;
/*    */   
/*    */   static {
/* 49 */     rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */   }
/*    */   public String toString() {
/* 52 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 56 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */