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
/*    */ 
/*    */ 
/*    */ public abstract class TransducerDecorator
/*    */   implements Transducer
/*    */ {
/*    */   protected final Transducer core;
/*    */   
/*    */   protected TransducerDecorator(Transducer _core) {
/* 31 */     this.core = _core;
/*    */   }
/*    */   
/*    */   public JType getReturnType() {
/* 35 */     return this.core.getReturnType();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBuiltin() {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public void populate(AnnotatedGrammar grammar, GeneratorContext context) {
/* 44 */     this.core.populate(grammar, context);
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 48 */     return this.core.generateSerializer(value, context);
/*    */   }
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 52 */     this.core.declareNamespace(body, value, context);
/*    */   }
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 56 */     return this.core.generateDeserializer(literal, context);
/*    */   }
/*    */   
/*    */   public boolean needsDelayedDeserialization() {
/* 60 */     return this.core.needsDelayedDeserialization();
/*    */   }
/*    */   
/*    */   public boolean isID() {
/* 64 */     return this.core.isID();
/*    */   }
/*    */   public SymbolSpace getIDSymbolSpace() {
/* 67 */     return this.core.getIDSymbolSpace();
/*    */   }
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 71 */     return this.core.generateConstant(exp);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\TransducerDecorator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */