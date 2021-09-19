/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
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
/*    */ public class BIXSuperClass
/*    */ {
/*    */   private final JDefinedClass cls;
/*    */   
/*    */   public BIXSuperClass(JDefinedClass _cls) {
/* 22 */     this.cls = _cls;
/* 23 */     _cls.hide();
/*    */   }
/*    */   public JDefinedClass getRootClass() {
/* 26 */     return this.cls;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIXSuperClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */