/*    */ package com.sun.tools.xjc.reader.xmlschema.parser;
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
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_UNACKNOWLEDGED_CUSTOMIZATION = "CustomizationContextChecker.UnacknolwedgedCustomization";
/*    */   static final String WARN_INCORRECT_URI = "IncorrectNamespaceURIChecker.WarnIncorrectURI";
/*    */   static final String WARN_UNABLE_TO_CHECK_CORRECTNESS = "SchemaConstraintChecker.UnableToCheckCorrectness";
/*    */   
/*    */   static String format(String property, Object... args) {
/* 49 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 50 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\parser\Messages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */