/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.tools.xjc.reader.xmlschema.JClassFactory;
/*    */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
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
/*    */ class JClassFactoryImpl
/*    */   implements JClassFactory
/*    */ {
/*    */   private final ClassSelector owner;
/*    */   private final JClassFactory parent;
/*    */   private final JClassContainer container;
/*    */   
/*    */   JClassFactoryImpl(ClassSelector owner, JClassContainer _cont) {
/* 25 */     this.parent = owner.getClassFactory();
/* 26 */     this.container = _cont;
/* 27 */     this.owner = owner;
/*    */   }
/*    */   
/*    */   public JDefinedClass create(String name, Locator sourceLocation) {
/* 31 */     return this.owner.codeModelClassFactory.createInterface(this.container, name, sourceLocation);
/*    */   }
/*    */ 
/*    */   
/*    */   public JClassFactory getParentFactory() {
/* 36 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\JClassFactoryImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */