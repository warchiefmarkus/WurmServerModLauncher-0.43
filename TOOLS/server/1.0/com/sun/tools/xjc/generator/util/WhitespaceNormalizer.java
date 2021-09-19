/*    */ package 1.0.com.sun.tools.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpression;
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
/*    */ public abstract class WhitespaceNormalizer
/*    */ {
/*    */   public abstract JExpression generate(JCodeModel paramJCodeModel, JExpression paramJExpression);
/*    */   
/*    */   public static com.sun.tools.xjc.generator.util.WhitespaceNormalizer parse(String method) {
/* 39 */     if (method.equals("preserve")) {
/* 40 */       return PRESERVE;
/*    */     }
/* 42 */     if (method.equals("replace")) {
/* 43 */       return REPLACE;
/*    */     }
/* 45 */     if (method.equals("collapse")) {
/* 46 */       return COLLAPSE;
/*    */     }
/* 48 */     throw new IllegalArgumentException(method);
/*    */   }
/*    */   
/* 51 */   public static com.sun.tools.xjc.generator.util.WhitespaceNormalizer PRESERVE = (com.sun.tools.xjc.generator.util.WhitespaceNormalizer)new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public static com.sun.tools.xjc.generator.util.WhitespaceNormalizer REPLACE = (com.sun.tools.xjc.generator.util.WhitespaceNormalizer)new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Class class$(String x0) {
/*    */     
/* 64 */     try { return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError(x1.getMessage()); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/* 69 */   public static com.sun.tools.xjc.generator.util.WhitespaceNormalizer COLLAPSE = (com.sun.tools.xjc.generator.util.WhitespaceNormalizer)new Object();
/*    */   static Class class$com$sun$xml$bind$WhiteSpaceProcessor;
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\util\WhitespaceNormalizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */