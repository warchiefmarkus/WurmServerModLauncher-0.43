/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.datatype.DatabindableDatatype;
/*    */ import com.sun.msv.datatype.xsd.StringType;
/*    */ import com.sun.msv.datatype.xsd.XSDatatype;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*    */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ class MixedComplexTypeBuilder
/*    */   extends AbstractCTBuilder
/*    */ {
/*    */   public MixedComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/* 25 */     super(_builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 30 */     return (ct.getBaseType() == this.bgmBuilder.schemas.getAnyType() && ct.isMixed());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression build(XSComplexType ct) {
/* 36 */     BIProperty prop = BIProperty.getCustomization(this.bgmBuilder, (XSComponent)ct);
/*    */ 
/*    */ 
/*    */     
/* 40 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/*    */     
/* 42 */     FieldItem fi = prop.createFieldItem("Content", false, this.pool.createInterleave(this.pool.createZeroOrMore((Expression)this.bgmBuilder.grammar.createPrimitiveItem((Transducer)new IdentityTransducer(this.bgmBuilder.grammar.codeModel), (DatabindableDatatype)StringType.theInstance, this.pool.createData((XSDatatype)StringType.theInstance), ct.getLocator())), this.bgmBuilder.typeBuilder.build((XSComponent)ct.getContentType())), (XSComponent)ct);
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
/* 56 */     fi.multiplicity = Multiplicity.star;
/* 57 */     fi.collisionExpected = true;
/*    */ 
/*    */     
/* 60 */     return this.pool.createSequence(this.bgmBuilder.fieldBuilder.attributeContainer((XSAttContainer)ct), (Expression)fi);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\MixedComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */