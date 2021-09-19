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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JWhileLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 38 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JWhileLoop(JExpression test) {
/* 44 */     this.test = test;
/*    */   }
/*    */   
/*    */   public JExpression test() {
/* 48 */     return this.test;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 52 */     if (this.body == null) this.body = new JBlock(); 
/* 53 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 57 */     if (JOp.hasTopOp(this.test)) {
/* 58 */       f.p("while ").g(this.test);
/*    */     } else {
/* 60 */       f.p("while (").g(this.test).p(')');
/*    */     } 
/* 62 */     if (this.body != null) {
/* 63 */       f.s(this.body);
/*    */     } else {
/* 65 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JWhileLoop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */