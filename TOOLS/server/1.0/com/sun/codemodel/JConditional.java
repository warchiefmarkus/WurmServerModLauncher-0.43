/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JOp;
/*    */ import com.sun.codemodel.JStatement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JConditional
/*    */   implements JStatement
/*    */ {
/* 17 */   private JExpression test = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   private JBlock _then = new JBlock();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private JBlock _else = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JConditional(JExpression test) {
/* 36 */     this.test = test;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock _then() {
/* 45 */     return this._then;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock _else() {
/* 54 */     if (this._else == null) this._else = new JBlock(); 
/* 55 */     return this._else;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 59 */     if (JOp.hasTopOp(this.test)) {
/* 60 */       f.p("if ").g((JGenerable)this.test);
/*    */     } else {
/* 62 */       f.p("if (").g((JGenerable)this.test).p(')');
/*    */     } 
/* 64 */     f.g((JGenerable)this._then);
/* 65 */     if (this._else != null)
/* 66 */       f.p("else").g((JGenerable)this._else); 
/* 67 */     f.nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JConditional.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */