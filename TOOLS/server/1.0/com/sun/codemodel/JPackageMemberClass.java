/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDeclaration;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JPackage;
/*    */ 
/*    */ class JPackageMemberClass
/*    */   extends JDefinedClass
/*    */   implements JDeclaration
/*    */ {
/*    */   private JPackage pkg;
/*    */   
/*    */   public final JPackage _package() {
/* 16 */     return this.pkg;
/*    */   }
/*    */   public JClassContainer parentContainer() {
/* 19 */     return (JClassContainer)this.pkg;
/*    */   }
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
/*    */   JPackageMemberClass(JPackage pkg, int mods, String name) {
/* 35 */     this(pkg, mods, name, false);
/*    */   }
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
/*    */   JPackageMemberClass(JPackage pkg, int mods, String name, boolean isInterface) {
/* 54 */     super(mods, name, isInterface, pkg.owner());
/* 55 */     this.pkg = pkg;
/*    */   }
/*    */   
/*    */   public void declare(JFormatter f) {
/* 59 */     if (!this.pkg.isUnnamed()) {
/* 60 */       f.nl().d((JDeclaration)this.pkg);
/* 61 */       f.nl();
/*    */     } 
/* 63 */     super.declare(f);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JPackageMemberClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */