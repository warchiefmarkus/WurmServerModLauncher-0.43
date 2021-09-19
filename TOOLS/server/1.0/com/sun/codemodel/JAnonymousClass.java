/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JAnonymousClass
/*    */   extends JDefinedClass
/*    */ {
/*    */   private final JClass base;
/*    */   
/*    */   JAnonymousClass(JClass _base, JCodeModel owner) {
/* 21 */     super(0, _base.name(), false, owner);
/* 22 */     this.base = _base;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JPackage _package() {
/* 28 */     return this.base._package();
/*    */   }
/*    */ 
/*    */   
/*    */   public JClassContainer parentContainer() {
/* 33 */     throw new InternalError();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JAnonymousClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */