/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDeclaration;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JNestedClass
/*    */   extends JDefinedClass
/*    */   implements JDeclaration
/*    */ {
/* 19 */   private JDefinedClass outer = null;
/*    */   
/*    */   public JPackage _package() {
/* 22 */     return this.outer._package();
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
/*    */   JNestedClass(JDefinedClass outer, int mods, String name) {
/* 37 */     this(outer, mods, name, false);
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
/*    */ 
/*    */   
/*    */   JNestedClass(JDefinedClass outer, int mods, String name, boolean isInterface) {
/* 58 */     super(mods, name, isInterface, outer.owner());
/* 59 */     this.outer = outer;
/*    */   }
/*    */ 
/*    */   
/*    */   public String fullName() {
/* 64 */     return this.outer.fullName() + '.' + name();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JClass outer() {
/* 72 */     return (JClass)this.outer;
/*    */   }
/*    */   
/*    */   public JClassContainer parentContainer() {
/* 76 */     return (JClassContainer)this.outer;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JNestedClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */