/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*    */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassBinder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.XSSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractBinderImpl
/*    */   implements ClassBinder
/*    */ {
/*    */   protected final BGMBuilder builder;
/*    */   protected final ClassSelector owner;
/*    */   
/*    */   protected AbstractBinderImpl(ClassSelector _owner) {
/* 30 */     this.owner = _owner;
/* 31 */     this.builder = this.owner.builder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final ClassItem wrapByClassItem(XSComponent sc, JDefinedClass cls) {
/* 38 */     return this.owner.builder.grammar.createClassItem(cls, Expression.epsilon, sc.getLocator());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final String deriveName(XSDeclaration comp) {
/* 46 */     return deriveName(comp.getName(), (XSComponent)comp);
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
/*    */   protected final String deriveName(String name, XSComponent comp) {
/* 63 */     XSSchema owner = comp.getOwnerSchema();
/*    */     
/* 65 */     if (owner != null) {
/* 66 */       BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)owner).get(BISchemaBinding.NAME);
/*    */ 
/*    */       
/* 69 */       if (sb != null) name = sb.mangleClassName(name, comp);
/*    */     
/*    */     } 
/* 72 */     name = this.builder.getNameConverter().toClassName(name);
/*    */     
/* 74 */     return name;
/*    */   }
/*    */   
/*    */   protected static void _assert(boolean b) {
/* 78 */     if (!b)
/* 79 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\AbstractBinderImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */