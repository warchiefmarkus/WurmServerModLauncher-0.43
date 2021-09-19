/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.ObjectFactoryGenerator;
/*    */ import com.sun.tools.xjc.generator.VersionGenerator;
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
/*    */ public final class PackageContext
/*    */ {
/*    */   public final JPackage _package;
/*    */   public final JDefinedClass objectFactory;
/*    */   public final JVar rootTagMap;
/*    */   protected final VersionGenerator versionGenerator;
/*    */   protected final ObjectFactoryGenerator objectFactoryGenerator;
/*    */   
/*    */   protected PackageContext(GeneratorContext _context, AnnotatedGrammar _grammar, Options _opt, JPackage _pkg) {
/* 63 */     this._package = _pkg;
/*    */     
/* 65 */     this.versionGenerator = new VersionGenerator(_context, _grammar, _pkg.subPackage("impl"));
/*    */     
/* 67 */     this.objectFactoryGenerator = new ObjectFactoryGenerator(_context, _grammar, _opt, _pkg);
/*    */     
/* 69 */     this.objectFactory = this.objectFactoryGenerator.getObjectFactory();
/* 70 */     this.rootTagMap = this.objectFactoryGenerator.getRootTagMap();
/*    */ 
/*    */     
/* 73 */     this.versionGenerator.generateVersionReference(this.objectFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\PackageContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */