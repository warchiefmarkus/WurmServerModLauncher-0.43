/*    */ package com.sun.codemodel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JConditional
/*    */   implements JStatement
/*    */ {
/* 32 */   private JExpression test = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   private JBlock _then = new JBlock();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   private JBlock _else = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JConditional(JExpression test) {
/* 51 */     this.test = test;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock _then() {
/* 60 */     return this._then;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock _else() {
/* 69 */     if (this._else == null) this._else = new JBlock(); 
/* 70 */     return this._else;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JConditional _elseif(JExpression boolExp) {
/* 77 */     return _else()._if(boolExp);
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 81 */     if (this.test == JExpr.TRUE) {
/* 82 */       this._then.generateBody(f);
/*    */       return;
/*    */     } 
/* 85 */     if (this.test == JExpr.FALSE) {
/* 86 */       this._else.generateBody(f);
/*    */       
/*    */       return;
/*    */     } 
/* 90 */     if (JOp.hasTopOp(this.test)) {
/* 91 */       f.p("if ").g(this.test);
/*    */     } else {
/* 93 */       f.p("if (").g(this.test).p(')');
/*    */     } 
/* 95 */     f.g(this._then);
/* 96 */     if (this._else != null)
/* 97 */       f.p("else").g(this._else); 
/* 98 */     f.nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JConditional.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */