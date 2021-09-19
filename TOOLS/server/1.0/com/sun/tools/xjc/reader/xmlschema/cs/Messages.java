/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_ABSTRACT_COMPLEX_TYPE = "ClassSelector.AbstractComplexType";
/*    */   static final String ERR_ABSTRACT_COMPLEX_TYPE_SOURCE = "ClassSelector.AbstractComplexType.SourceLocation";
/*    */   static final String JAVADOC_HEADING = "ClassSelector.JavadocHeading";
/*    */   static final String JAVADOC_LINE_UNKNOWN = "ClassSelector.JavadocLineUnknown";
/*    */   
/*    */   static String format(String property) {
/* 17 */     return format(property, null);
/*    */   }
/*    */   static final String ERR_RESERVED_CLASS_NAME = "ClassSelector.ReservedClassName"; static final String ERR_CLASS_NAME_IS_REQUIRED = "ClassSelector.ClassNameIsRequired"; static final String ERR_INCORRECT_CLASS_NAME = "ClassSelector.IncorrectClassName"; static final String ERR_INCORRECT_PACKAGE_NAME = "ClassSelector.IncorrectPackageName"; static final String ERR_UNABLE_TO_GENERATE_NAME_FROM_MODELGROUP = "DefaultParticleBinder.UnableToGenerateNameFromModelGroup";
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
/* 36 */     String text = ResourceBundle.getBundle(com.sun.tools.xjc.reader.xmlschema.cs.Messages.class.getName()).getString(property);
/* 37 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */