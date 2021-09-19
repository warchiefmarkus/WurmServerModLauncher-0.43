/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FreshComplexTypeBuilder
/*    */   extends AbstractCTBuilder
/*    */ {
/*    */   public FreshComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/* 23 */     super(_builder);
/*    */   }
/*    */   
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 27 */     return (ct.getBaseType() == this.bgmBuilder.schemas.getAnyType() && !ct.isMixed());
/*    */   }
/*    */ 
/*    */   
/*    */   public Expression build(XSComplexType ct) {
/* 32 */     XSContentType contentType = ct.getContentType();
/*    */     
/* 34 */     Expression exp = (Expression)contentType.apply((XSContentTypeFunction)new Object(this, ct));
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
/* 57 */     return this.pool.createSequence(this.bgmBuilder.fieldBuilder.attributeContainer((XSAttContainer)ct), exp);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\FreshComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */