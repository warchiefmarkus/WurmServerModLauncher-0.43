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
/*    */ 
/*    */ 
/*    */ class JThrow
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression expr;
/*    */   
/*    */   JThrow(JExpression expr) {
/* 42 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 46 */     f.p("throw");
/* 47 */     f.g(this.expr);
/* 48 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JThrow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */