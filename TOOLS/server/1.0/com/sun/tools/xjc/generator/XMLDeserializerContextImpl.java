/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class XMLDeserializerContextImpl
/*    */   implements DeserializerContext
/*    */ {
/*    */   private final JExpression $context;
/*    */   
/*    */   public XMLDeserializerContextImpl(JExpression _$context) {
/* 29 */     this.$context = _$context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression ref() {
/* 36 */     return this.$context;
/*    */   }
/*    */   
/*    */   public JExpression addToIdTable(JExpression literal) {
/* 40 */     return (JExpression)this.$context.invoke("addToIdTable").arg(literal);
/*    */   }
/*    */   
/*    */   public JExpression getObjectFromId(JExpression literal) {
/* 44 */     return (JExpression)this.$context.invoke("getObjectFromId").arg(literal);
/*    */   }
/*    */   
/*    */   public JExpression getNamespaceContext() {
/* 48 */     return this.$context;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\XMLDeserializerContextImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */