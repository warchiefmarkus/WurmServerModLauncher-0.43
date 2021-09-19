/*    */ package 1.0.com.sun.tools.xjc.grammar.id;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*    */ import com.sun.tools.xjc.grammar.id.Messages;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*    */ import com.sun.xml.bind.marshaller.IdentifiableObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IDREFTransducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   private final SymbolSpace symbolSpace;
/*    */   private final boolean whitespaceNormalization;
/*    */   
/*    */   public IDREFTransducer(JCodeModel _codeModel, SymbolSpace _symbolSpace, boolean _whitespaceNormalization) {
/* 27 */     this.codeModel = _codeModel;
/* 28 */     this.symbolSpace = _symbolSpace;
/* 29 */     this.whitespaceNormalization = _whitespaceNormalization;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SymbolSpace getIDSymbolSpace() {
/* 38 */     return this.symbolSpace;
/*    */   }
/*    */   
/*    */   public JType getReturnType() {
/* 42 */     return this.symbolSpace.getType();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateSerializer(JExpression literal, SerializerContext context) {
/* 48 */     return context.onIDREF((JExpression)JExpr.cast((JType)this.codeModel.ref(IdentifiableObject.class), literal));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 54 */     return (JExpression)JExpr.cast(this.symbolSpace.getType(), context.getObjectFromId(this.whitespaceNormalization ? WhitespaceNormalizer.COLLAPSE.generate(this.codeModel, literal) : literal));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean needsDelayedDeserialization() {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     return "IDREFTransducer:" + this.symbolSpace.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 74 */     throw new UnsupportedOperationException(Messages.format("IDREFTransducer.ConstantIDREFError"));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\id\IDREFTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */