/*    */ package 1.0.com.sun.tools.xjc.generator.cls;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.tools.xjc.generator.ClassContext;
/*    */ import com.sun.tools.xjc.generator.MethodWriter;
/*    */ import com.sun.tools.xjc.generator.cls.ImplStructureStrategy;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ public final class PararellStructureStrategy
/*    */   implements ImplStructureStrategy
/*    */ {
/* 38 */   private final Map intf2impl = new HashMap();
/*    */   
/*    */   private final CodeModelClassFactory codeModelClassFactory;
/*    */   
/*    */   public PararellStructureStrategy(CodeModelClassFactory _codeModelClassFactory) {
/* 43 */     this.codeModelClassFactory = _codeModelClassFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private JDefinedClass determineImplClass(JDefinedClass intf) {
/*    */     JPackage jPackage;
/* 51 */     JDefinedClass jDefinedClass1, d = (JDefinedClass)this.intf2impl.get(intf);
/* 52 */     if (d != null) return d;
/*    */     
/* 54 */     JClassContainer parent = intf.parentContainer();
/* 55 */     int mod = 1;
/*    */     
/* 57 */     if (parent instanceof JPackage) {
/* 58 */       jPackage = ((JPackage)parent).subPackage("impl");
/*    */     } else {
/* 60 */       jDefinedClass1 = determineImplClass((JDefinedClass)jPackage);
/* 61 */       mod |= 0x10;
/*    */     } 
/*    */     
/* 64 */     d = this.codeModelClassFactory.createClass((JClassContainer)jDefinedClass1, mod, intf.name() + "Impl", (Locator)intf.metadata);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     this.intf2impl.put(intf, d);
/* 70 */     return d;
/*    */   }
/*    */   
/*    */   public JDefinedClass createImplClass(ClassItem ci) {
/* 74 */     JDefinedClass impl = determineImplClass(ci.getTypeAsDefined());
/*    */     
/* 76 */     impl._implements((JClass)ci.getTypeAsDefined());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     impl.method(28, Class.class, "PRIMARY_INTERFACE_CLASS").body()._return(ci.getTypeAsDefined().dotclass());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 89 */     return impl;
/*    */   }
/*    */   
/*    */   public MethodWriter createMethodWriter(ClassContext target) {
/* 93 */     return (MethodWriter)new Object(this, target, target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\cls\PararellStructureStrategy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */