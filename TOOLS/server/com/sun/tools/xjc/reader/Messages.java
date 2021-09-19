/*    */ package com.sun.tools.xjc.reader;
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
/*    */ public enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 46 */   DUPLICATE_PROPERTY,
/* 47 */   DUPLICATE_ELEMENT,
/*    */   
/* 49 */   ERR_UNDECLARED_PREFIX,
/* 50 */   ERR_UNEXPECTED_EXTENSION_BINDING_PREFIXES,
/* 51 */   ERR_UNSUPPORTED_EXTENSION,
/* 52 */   ERR_SUPPORTED_EXTENSION_IGNORED,
/* 53 */   ERR_RELEVANT_LOCATION,
/* 54 */   ERR_CLASS_NOT_FOUND,
/* 55 */   PROPERTY_CLASS_IS_RESERVED,
/* 56 */   ERR_VENDOR_EXTENSION_DISALLOWED_IN_STRICT_MODE,
/* 57 */   ERR_ILLEGAL_CUSTOMIZATION_TAGNAME,
/* 58 */   ERR_PLUGIN_NOT_ENABLED;
/*    */   
/*    */   static {
/* 61 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 64 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 68 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */