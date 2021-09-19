/*    */ package 1.0.com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class Messages {
/*    */   public static final String ERR_UNDEFINED_SIMPLETYPE = "UndefinedSimpleType";
/*    */   public static final String ERR_UNDEFINED_COMPLEXTYPE = "UndefinedCompplexType";
/*    */   public static final String ERR_UNDEFINED_TYPE = "UndefinedType";
/*    */   public static final String ERR_UNDEFINED_ELEMENT = "UndefinedElement";
/*    */   public static final String ERR_UNDEFINED_MODELGROUP = "UndefinedModelGroup";
/*    */   public static final String ERR_UNDEFINED_ATTRIBUTE = "UndefinedAttribute";
/*    */   public static final String ERR_UNDEFINED_ATTRIBUTEGROUP = "UndefinedAttributeGroup";
/*    */   public static final String ERR_UNDEFINED_PREFIX = "UndefinedPrefix";
/*    */   public static final String ERR_DOUBLE_DEFINITION = "DoubleDefinition";
/*    */   public static final String ERR_DOUBLE_DEFINITION_ORIGINAL = "DoubleDefinition.Original";
/*    */   public static final String ERR_MISSING_SCHEMALOCATION = "MissingSchemaLocation";
/*    */   public static final String ERR_ENTITY_RESOLUTION_FAILURE = "EntityResolutionFailure";
/*    */   
/*    */   public static String format(String property) {
/* 21 */     return format(property, null);
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1) {
/* 25 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1, Object arg2) {
/* 29 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */   
/*    */   public static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 33 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String format(String property, Object[] args) {
/* 40 */     String text = ResourceBundle.getBundle(com.sun.xml.xsom.impl.parser.Messages.class.getName()).getString(property);
/*    */     
/* 42 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */