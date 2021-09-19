/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.Messages;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
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
/*    */ final class VersionGenerator
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   private final GeneratorContext context;
/*    */   private final JPackage targetPackage;
/*    */   public final JDefinedClass versionClass;
/*    */   
/*    */   VersionGenerator(GeneratorContext _context, AnnotatedGrammar _grammar, JPackage _pkg) {
/* 36 */     this.context = _context;
/* 37 */     this.codeModel = _grammar.codeModel;
/* 38 */     this.targetPackage = _pkg;
/*    */ 
/*    */     
/* 41 */     this.versionClass = this.context.getClassFactory().createClass((JClassContainer)this.targetPackage, "JAXBVersion", null);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     generate();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void generate() {
/* 56 */     this.versionClass.field(25, (JType)this.codeModel.ref(String.class), "version", JExpr.lit(Messages.format("VersionGenerator.versionField")));
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
/*    */   void generateVersionReference(JDefinedClass impl) {
/* 72 */     impl.field(25, (JType)this.codeModel.ref(Class.class), "version", this.versionClass.dotclass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void generateVersionReference(ClassContext cc) {
/* 80 */     generateVersionReference(cc.implClass);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\VersionGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */