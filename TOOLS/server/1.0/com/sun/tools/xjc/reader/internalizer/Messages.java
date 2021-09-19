/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_INCORRECT_SCHEMA_REFERENCE = "Internalizer.IncorrectSchemaReference";
/*    */   static final String ERR_XPATH_EVAL = "Internalizer.XPathEvaluationError";
/*    */   static final String NO_XPATH_EVAL_TO_NO_TARGET = "Internalizer.XPathEvaluatesToNoTarget";
/*    */   static final String NO_XPATH_EVAL_TOO_MANY_TARGETS = "Internalizer.XPathEvaulatesToTooManyTargets";
/*    */   static final String NO_XPATH_EVAL_TO_NON_ELEMENT = "Internalizer.XPathEvaluatesToNonElement";
/*    */   static final String XPATH_EVAL_TO_NON_SCHEMA_ELEMENT = "Internalizer.XPathEvaluatesToNonSchemaElement";
/*    */   
/*    */   static String format(String property) {
/* 17 */     return format(property, null);
/*    */   }
/*    */   static final String CONTEXT_NODE_IS_NOT_ELEMENT = "Internalizer.ContextNodeIsNotElement"; static final String ERR_INCORRECT_VERSION = "Internalizer.IncorrectVersion"; static final String ERR_VERSION_NOT_FOUND = "Internalizer.VersionNotPresent"; static final String TWO_VERSION_ATTRIBUTES = "Internalizer.TwoVersionAttributes"; static final String ORPHANED_CUSTOMIZATION = "Internalizer.OrphanedCustomization"; static final String ERR_UNABLE_TO_PARSE = "AbstractReferenceFinderImpl.UnableToParse";
/*    */   static String format(String property, Object arg1) {
/* 21 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */   
/*    */   static String format(String property, Object arg1, Object arg2) {
/* 25 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */   
/*    */   static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 29 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String format(String property, Object[] args) {
/* 36 */     String text = ResourceBundle.getBundle(com.sun.tools.xjc.reader.internalizer.Messages.class.getName()).getString(property);
/* 37 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */