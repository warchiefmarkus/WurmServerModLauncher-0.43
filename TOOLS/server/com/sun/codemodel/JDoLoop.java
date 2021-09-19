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
/*    */ public class JDoLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 38 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JDoLoop(JExpression test) {
/* 44 */     this.test = test;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 48 */     if (this.body == null) this.body = new JBlock(); 
/* 49 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 53 */     f.p("do");
/* 54 */     if (this.body != null) {
/* 55 */       f.g(this.body);
/*    */     } else {
/* 57 */       f.p("{ }");
/*    */     } 
/* 59 */     if (JOp.hasTopOp(this.test)) {
/* 60 */       f.p("while ").g(this.test);
/*    */     } else {
/* 62 */       f.p("while (").g(this.test).p(')');
/*    */     } 
/* 64 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JDoLoop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */