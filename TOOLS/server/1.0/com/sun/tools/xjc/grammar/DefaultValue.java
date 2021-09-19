/*    */ package 1.0.com.sun.tools.xjc.grammar;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.msv.grammar.ValueExp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DefaultValue
/*    */ {
/*    */   public final Transducer xducer;
/*    */   public final ValueExp value;
/*    */   
/*    */   public DefaultValue(Transducer _xducer, ValueExp _value) {
/* 28 */     this.xducer = _xducer;
/* 29 */     this.value = _value;
/*    */   }
/*    */   
/*    */   public JExpression generateConstant() {
/* 33 */     return this.xducer.generateConstant(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\DefaultValue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */