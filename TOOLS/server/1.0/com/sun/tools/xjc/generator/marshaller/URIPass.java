/*    */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*    */ 
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JStringLiteral;
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
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class URIPass
/*    */   extends AbstractPassImpl
/*    */ {
/*    */   URIPass(Context _context) {
/* 28 */     super(_context, "URIs");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onElement(ElementExp exp) {
/* 33 */     if (this.context.isInside()) {
/* 34 */       this.context.skipPass.build(exp.contentModel);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onExternal(ExternalItem item) {
/* 40 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/* 41 */     fmg.increment(this.context.getCurrentBlock());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onAttribute(AttributeExp exp) {
/* 46 */     XmlNameStoreAlgorithm algorithm = XmlNameStoreAlgorithm.get((NameClassAndExpression)exp);
/* 47 */     JExpression namespaceURI = algorithm.getNamespaceURI();
/*    */     
/* 49 */     if (!(namespaceURI instanceof JStringLiteral) || !((JStringLiteral)namespaceURI).str.equals(""))
/*    */     {
/*    */ 
/*    */       
/* 53 */       getBlock(true).invoke((JExpression)this.context.$serializer.invoke("getNamespaceContext"), "declareNamespace").arg(namespaceURI).arg(JExpr._null()).arg(JExpr.TRUE);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     this.context.uriPass.build(exp.exp);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPrimitive(PrimitiveItem exp) {
/* 67 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*    */ 
/*    */     
/* 70 */     Transducer xducer = TypeAdaptedTransducer.adapt(exp.xducer, (fmg.owner().getFieldUse()).type);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 75 */     xducer.declareNamespace(this.context.getCurrentBlock(), (JExpression)JExpr.cast(xducer.getReturnType(), fmg.peek(false)), (SerializerContext)this.context);
/*    */ 
/*    */     
/* 78 */     fmg.increment(this.context.getCurrentBlock());
/*    */   }
/*    */   
/*    */   public void onValue(ValueExp exp) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\URIPass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */