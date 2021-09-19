/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.msv.grammar.AttributeExp;
/*    */ import com.sun.msv.grammar.ElementExp;
/*    */ import com.sun.msv.grammar.NameClassAndExpression;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.XmlNameStoreAlgorithm;
/*    */ import com.sun.tools.xjc.generator.marshaller.AbstractPassImpl;
/*    */ import com.sun.tools.xjc.generator.marshaller.Context;
/*    */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*    */ import com.sun.tools.xjc.grammar.ExternalItem;
/*    */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AttributePass
/*    */   extends AbstractPassImpl
/*    */ {
/*    */   AttributePass(Context _context) {
/* 24 */     super(_context, "Attributes");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onElement(ElementExp exp) {
/* 30 */     if (this.context.isInside()) {
/* 31 */       this.context.skipPass.build(exp.contentModel);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onExternal(ExternalItem item) {
/* 36 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/* 37 */     fmg.increment(this.context.getCurrentBlock());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onAttribute(AttributeExp exp) {
/* 46 */     JBlock block = getBlock(true);
/*    */ 
/*    */     
/* 49 */     XmlNameStoreAlgorithm algorithm = XmlNameStoreAlgorithm.get((NameClassAndExpression)exp);
/* 50 */     block.invoke((JExpression)this.context.$serializer, "startAttribute").arg(algorithm.getNamespaceURI()).arg(algorithm.getLocalPart());
/*    */ 
/*    */ 
/*    */     
/* 54 */     this.context.bodyPass.build(exp.exp);
/*    */     
/* 56 */     block.invoke((JExpression)this.context.$serializer, "endAttribute");
/*    */   }
/*    */   
/*    */   public void onPrimitive(PrimitiveItem exp) {
/* 60 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/* 61 */     fmg.increment(this.context.getCurrentBlock());
/*    */   }
/*    */   
/*    */   public void onValue(ValueExp exp) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\AttributePass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */