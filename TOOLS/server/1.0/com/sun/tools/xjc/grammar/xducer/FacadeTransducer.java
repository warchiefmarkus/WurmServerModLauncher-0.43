/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
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
/*    */ public class FacadeTransducer
/*    */   implements Transducer
/*    */ {
/*    */   private final Transducer marshaller;
/*    */   private final Transducer unmarshaller;
/*    */   
/*    */   public FacadeTransducer(Transducer _marshaller, Transducer _unmarshaller) {
/* 30 */     this.marshaller = _marshaller;
/* 31 */     this.unmarshaller = _unmarshaller;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JType getReturnType() {
/* 38 */     return this.marshaller.getReturnType();
/*    */   }
/*    */   
/*    */   public boolean isID() {
/* 42 */     return false; } public SymbolSpace getIDSymbolSpace() {
/* 43 */     return null;
/*    */   }
/*    */   public boolean isBuiltin() {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   public void populate(AnnotatedGrammar grammar, GeneratorContext context) {
/* 50 */     this.marshaller.populate(grammar, context);
/* 51 */     this.unmarshaller.populate(grammar, context);
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 55 */     return this.marshaller.generateSerializer(value, context);
/*    */   }
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 59 */     this.marshaller.declareNamespace(body, value, context);
/*    */   }
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 63 */     return this.unmarshaller.generateDeserializer(literal, context);
/*    */   }
/*    */   
/*    */   public boolean needsDelayedDeserialization() {
/* 67 */     return this.unmarshaller.needsDelayedDeserialization();
/*    */   }
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 71 */     return this.unmarshaller.generateConstant(exp);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\FacadeTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */