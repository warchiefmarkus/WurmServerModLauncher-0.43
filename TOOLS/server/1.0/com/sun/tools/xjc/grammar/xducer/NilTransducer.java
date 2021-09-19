/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.msv.grammar.ValueExp;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NilTransducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   
/*    */   public NilTransducer(JCodeModel _codeModel) {
/* 23 */     this.codeModel = _codeModel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JType getReturnType() {
/* 29 */     return (JType)this.codeModel.NULL;
/*    */   }
/*    */ 
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 34 */     return JExpr.lit("true");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 45 */     return JExpr._null();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 51 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\NilTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */