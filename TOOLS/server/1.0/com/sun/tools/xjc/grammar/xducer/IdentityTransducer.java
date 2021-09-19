/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
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
/*    */ public class IdentityTransducer
/*    */   extends TransducerImpl
/*    */ {
/*    */   private final JClass stringType;
/*    */   
/*    */   public IdentityTransducer(JCodeModel codeModel) {
/* 23 */     this.stringType = codeModel.ref(String.class);
/*    */   }
/*    */   public JType getReturnType() {
/* 26 */     return (JType)this.stringType;
/*    */   }
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 29 */     return value;
/*    */   }
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 33 */     return literal;
/*    */   }
/*    */   
/*    */   public JExpression generateConstant(ValueExp exp) {
/* 37 */     return JExpr.lit(obtainString(exp));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\IdentityTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */