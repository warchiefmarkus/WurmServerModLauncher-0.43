/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.tools.xjc.generator.XmlNameStoreAlgorithm;
/*     */ import com.sun.tools.xjc.generator.marshaller.AbstractPassImpl;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BodyPass
/*     */   extends AbstractPassImpl
/*     */ {
/*     */   BodyPass(Context _context, String name) {
/*  41 */     super(_context, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onElement(ElementExp exp) {
/* 168 */     _onElement((NameClassAndExpression)exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void _onElement(NameClassAndExpression exp) {
/* 195 */     Expression contentModel = exp.getContentModel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     JBlock block = getBlock(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     XmlNameStoreAlgorithm algorithm = XmlNameStoreAlgorithm.get(exp.getNameClass());
/* 212 */     block.invoke((JExpression)this.context.$serializer, "startElement").arg(algorithm.getNamespaceURI()).arg(algorithm.getLocalPart());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     FieldCloner fc = new FieldCloner(this, contentModel, true);
/* 218 */     fc.push();
/* 219 */     this.context.uriPass.build(contentModel);
/* 220 */     block.invoke((JExpression)this.context.$serializer, "endNamespaceDecls");
/* 221 */     fc.pop();
/*     */ 
/*     */     
/* 224 */     fc = new FieldCloner(this, contentModel, false);
/* 225 */     fc.push();
/* 226 */     this.context.attPass.build(contentModel);
/* 227 */     block.invoke((JExpression)this.context.$serializer, "endAttributes");
/* 228 */     fc.pop();
/*     */ 
/*     */     
/* 231 */     this.context.bodyPass.build(contentModel);
/*     */     
/* 233 */     block.invoke((JExpression)this.context.$serializer, "endElement");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onExternal(ExternalItem item) {
/* 239 */     item.generateMarshaller(this.context.genContext, getBlock(true), this.context.getCurrentFieldMarshaller(), (JExpression)this.context.$serializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAttribute(AttributeExp exp) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPrimitive(PrimitiveItem exp) {
/* 256 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*     */ 
/*     */     
/* 259 */     Transducer xducer = TypeAdaptedTransducer.adapt(exp.xducer, (fmg.owner().getFieldUse()).type);
/*     */ 
/*     */ 
/*     */     
/* 263 */     getBlock(true).invoke((JExpression)this.context.$serializer, "text").arg(xducer.generateSerializer((JExpression)JExpr.cast(xducer.getReturnType(), fmg.peek(true)), (SerializerContext)this.context)).arg(JExpr.lit((fmg.owner().getFieldUse()).name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onValue(ValueExp exp) {
/* 275 */     marshalValue(exp);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\BodyPass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */