/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSType;
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
/*    */ class RestrictedComplexTypeBuilder
/*    */   extends AbstractCTBuilder
/*    */ {
/*    */   public RestrictedComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/* 24 */     super(_builder);
/*    */   }
/*    */   
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 28 */     XSType baseType = ct.getBaseType();
/* 29 */     return (baseType != this.bgmBuilder.schemas.getAnyType() && baseType.isComplexType() && ct.getDerivationMethod() == 2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression build(XSComplexType ct) {
/* 35 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/*    */     
/* 38 */     ClassItem baseClass = this.bgmBuilder.selector.bindToType(baseType);
/* 39 */     _assert((baseClass != null));
/*    */ 
/*    */     
/* 42 */     this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */     
/* 44 */     return (Expression)new SuperClassItem((Expression)baseClass, ct.getLocator());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\RestrictedComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */