/*    */ package 1.0.com.sun.tools.xjc.generator.field;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String DEFAULT_GETTER_JAVADOC = "SingleFieldRenderer.DefaultGetterJavadoc";
/*    */   static final String DEFAULT_SETTER_JAVADOC = "SingleFieldRenderer.DefaultSetterJavadoc";
/*    */   
/*    */   static String format(String property) {
/* 17 */     return format(property, null);
/*    */   }
/*    */   
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
/* 36 */     String text = ResourceBundle.getBundle(com.sun.tools.xjc.generator.field.Messages.class.getName()).getString(property);
/* 37 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\field\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */