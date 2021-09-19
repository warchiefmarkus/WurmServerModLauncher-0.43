/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
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
/*    */ public abstract class DelayedTransducer
/*    */   implements Transducer
/*    */ {
/* 27 */   private Transducer core = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Transducer create();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void update() {
/* 45 */     if (this.core == null) this.core = create(); 
/*    */   }
/*    */   
/*    */   public JType getReturnType() {
/* 49 */     update();
/* 50 */     return this.core.getReturnType();
/*    */   }
/*    */   
/*    */   public boolean isBuiltin() {
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 58 */     update();
/* 59 */     return this.core.generateSerializer(value, context);
/*    */   }
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 63 */     update();
/* 64 */     return this.core.generateDeserializer(literal, context);
/*    */   }
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 68 */     update();
/* 69 */     return this.core.generateConstant(exp);
/*    */   }
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {
/* 73 */     update();
/* 74 */     this.core.declareNamespace(body, value, context);
/*    */   }
/*    */   
/*    */   public boolean needsDelayedDeserialization() {
/* 78 */     update();
/* 79 */     return this.core.needsDelayedDeserialization();
/*    */   }
/*    */   
/*    */   public void populate(AnnotatedGrammar grammar, GeneratorContext context) {
/* 83 */     update();
/* 84 */     this.core.populate(grammar, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\DelayedTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */