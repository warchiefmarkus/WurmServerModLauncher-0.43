/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.msv.datatype.xsd.XSDatatype;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.util.BlockReference;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
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
/*    */ public abstract class TransducerImpl
/*    */   implements Transducer
/*    */ {
/*    */   public void populate(AnnotatedGrammar grammar, GeneratorContext context) {}
/*    */   
/*    */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext context) {}
/*    */   
/*    */   public boolean needsDelayedDeserialization() {
/* 28 */     return false;
/* 29 */   } public boolean isID() { return false; }
/* 30 */   public SymbolSpace getIDSymbolSpace() { return null; } public boolean isBuiltin() {
/* 31 */     return false;
/*    */   }
/*    */   public String toString() {
/* 34 */     String className = getClass().getName();
/* 35 */     int idx = className.lastIndexOf('.');
/* 36 */     if (idx >= 0) className = className.substring(idx + 1); 
/* 37 */     return className + ":" + getReturnType().name();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final String obtainString(ValueExp exp) {
/* 48 */     return ((XSDatatype)exp.dt).convertToLexicalValue(exp.value, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\TransducerImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */