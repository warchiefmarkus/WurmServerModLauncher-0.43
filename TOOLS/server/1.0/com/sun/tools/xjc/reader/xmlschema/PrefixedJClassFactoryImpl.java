/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*    */ import com.sun.xml.xsom.XSModelGroupDecl;
/*    */ import org.xml.sax.Locator;
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
/*    */ public class PrefixedJClassFactoryImpl
/*    */   implements JClassFactory
/*    */ {
/*    */   private final JClassFactory parent;
/*    */   private final String prefix;
/*    */   private final JClassContainer pkg;
/*    */   private final BGMBuilder builder;
/*    */   
/*    */   public PrefixedJClassFactoryImpl(BGMBuilder builder, JDefinedClass parentClass) {
/* 37 */     this.builder = builder;
/* 38 */     this.parent = builder.selector.getClassFactory();
/* 39 */     this.prefix = parentClass.name();
/*    */     
/* 41 */     this.pkg = parentClass.parentContainer();
/*    */   }
/*    */   
/*    */   public PrefixedJClassFactoryImpl(BGMBuilder builder, XSModelGroupDecl decl) {
/* 45 */     if (decl.isLocal()) {
/* 46 */       throw new IllegalArgumentException();
/*    */     }
/* 48 */     this.builder = builder;
/* 49 */     this.parent = builder.selector.getClassFactory();
/* 50 */     this.prefix = builder.getNameConverter().toClassName(decl.getName());
/*    */     
/* 52 */     this.pkg = (JClassContainer)builder.selector.getPackage(decl.getTargetNamespace());
/*    */   }
/*    */   
/*    */   public JDefinedClass create(String name, Locator sourceLocation) {
/* 56 */     return this.builder.selector.codeModelClassFactory.createInterface(this.pkg, this.prefix + name, sourceLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JClassFactory getParentFactory() {
/* 63 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\PrefixedJClassFactoryImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */