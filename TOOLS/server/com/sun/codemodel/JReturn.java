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
/*    */ class JReturn
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression expr;
/*    */   
/*    */   JReturn(JExpression expr) {
/* 41 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 45 */     f.p("return ");
/* 46 */     if (this.expr != null) f.g(this.expr); 
/* 47 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JReturn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */