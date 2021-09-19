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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class JCast
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression object;
/*    */   
/*    */   JCast(JType type, JExpression object) {
/* 50 */     this.type = type;
/* 51 */     this.object = object;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 55 */     f.p("((").g(this.type).p(')').g(this.object).p(')');
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JCast.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */