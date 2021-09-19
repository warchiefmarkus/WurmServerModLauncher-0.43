/*    */ package 1.0.com.sun.tools.xjc.util;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_CLASSNAME_COLLISION = "CodeModelClassFactory.ClassNameCollision";
/*    */   static final String ERR_CLASSNAME_COLLISION_SOURCE = "CodeModelClassFactory.ClassNameCollision.Source";
/*    */   static final String ERR_CASE_SENSITIVITY_COLLISION = "CodeModelClassFactory.CaseSensitivityCollision";
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
/* 36 */     String text = ResourceBundle.getBundle(com.sun.tools.xjc.util.Messages.class.getName()).getString(property);
/* 37 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */