/*    */ package 1.0.com.sun.tools.xjc.grammar.id;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IDTransducer
/*    */   extends IdentityTransducer
/*    */ {
/*    */   private final SymbolSpace symbolSpace;
/*    */   
/*    */   public IDTransducer(JCodeModel _codeModel, SymbolSpace _symbolSpace) {
/* 23 */     super(_codeModel);
/* 24 */     this.symbolSpace = _symbolSpace;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isID() {
/* 29 */     return true; } public SymbolSpace getIDSymbolSpace() {
/* 30 */     return this.symbolSpace;
/*    */   }
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 33 */     return context.addToIdTable(literal);
/*    */   }
/*    */   
/*    */   public JExpression generateSerializer(JExpression value, SerializerContext context) {
/* 37 */     return context.onID(JExpr._this(), value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\id\IDTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */