/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JPrimitiveType;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerDecorator;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
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
/*    */ public class CastTranducer
/*    */   extends TransducerDecorator
/*    */ {
/*    */   private final JPrimitiveType type;
/*    */   
/*    */   public CastTranducer(JPrimitiveType _type, Transducer _core) {
/* 34 */     super(_core);
/* 35 */     this.type = _type;
/* 36 */     if (!super.getReturnType().isPrimitive())
/* 37 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */   
/*    */   public JType getReturnType() {
/* 41 */     return (JType)this.type;
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 45 */     return super.generateSerializer((JExpression)JExpr.cast(super.getReturnType(), value), context);
/*    */   }
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 49 */     return (JExpression)JExpr.cast((JType)this.type, super.generateDeserializer(literal, context));
/*    */   }
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 53 */     super.declareNamespace(body, (JExpression)JExpr.cast(super.getReturnType(), value), context);
/*    */   }
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 57 */     return (JExpression)JExpr.cast((JType)this.type, super.generateConstant(exp));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\CastTranducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */