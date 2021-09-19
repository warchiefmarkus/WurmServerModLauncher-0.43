/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JClassAlreadyExistsException;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.MethodWriter;
/*    */ import com.sun.tools.xjc.generator.PackageContext;
/*    */ import com.sun.tools.xjc.generator.cls.ImplStructureStrategy;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassContext
/*    */ {
/*    */   public final GeneratorContext parent;
/*    */   public final PackageContext _package;
/*    */   public final ClassItem target;
/*    */   public final JClass implRef;
/*    */   public final JDefinedClass implClass;
/*    */   public final JDefinedClass ref;
/*    */   private final ImplStructureStrategy strategy;
/*    */   
/*    */   public MethodWriter createMethodWriter() {
/* 71 */     return this.strategy.createMethodWriter(this);
/*    */   }
/*    */   
/*    */   protected ClassContext(GeneratorContext _parent, ImplStructureStrategy _strategy, ClassItem _target) {
/* 75 */     this.parent = _parent;
/* 76 */     this.target = _target;
/* 77 */     this.strategy = _strategy;
/* 78 */     this.ref = _target.getTypeAsDefined();
/* 79 */     this._package = this.parent.getPackageContext(this.ref._package());
/* 80 */     this.implClass = _strategy.createImplClass(_target);
/*    */     
/* 82 */     if (this.target.getUserSpecifiedImplClass() != null) {
/*    */       JDefinedClass usr;
/*    */       
/*    */       try {
/* 86 */         usr = this.parent.getCodeModel()._class(this.target.getUserSpecifiedImplClass());
/*    */         
/* 88 */         usr.hide();
/* 89 */       } catch (JClassAlreadyExistsException e) {
/*    */         
/* 91 */         usr = e.getExistingClass();
/*    */       } 
/* 93 */       usr._extends((JClass)this.implClass);
/* 94 */       this.implRef = (JClass)usr;
/*    */     } else {
/* 96 */       this.implRef = (JClass)this.implClass;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\ClassContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */