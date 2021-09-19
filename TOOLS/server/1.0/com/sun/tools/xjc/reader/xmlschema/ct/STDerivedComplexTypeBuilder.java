/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class STDerivedComplexTypeBuilder
/*    */   extends AbstractCTBuilder
/*    */ {
/*    */   public STDerivedComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/* 23 */     super(_builder);
/*    */   }
/*    */   
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 27 */     return ct.getBaseType().isSimpleType();
/*    */   }
/*    */   
/*    */   public Expression build(XSComplexType ct) {
/* 31 */     _assert((ct.getDerivationMethod() == 1));
/*    */ 
/*    */     
/* 34 */     XSSimpleType baseType = ct.getBaseType().asSimpleType();
/*    */ 
/*    */     
/* 37 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */     
/* 39 */     Expression att = this.bgmBuilder.fieldBuilder.attributeContainer((XSAttContainer)ct);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     this.bgmBuilder.simpleTypeBuilder.refererStack.push(ct);
/*    */ 
/*    */     
/* 49 */     Expression exp = this.pool.createSequence(att, this.bgmBuilder.fieldBuilder.simpleType(baseType, (XSComponent)ct));
/*    */ 
/*    */     
/* 52 */     this.bgmBuilder.simpleTypeBuilder.refererStack.pop();
/*    */     
/* 54 */     return exp;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\STDerivedComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */