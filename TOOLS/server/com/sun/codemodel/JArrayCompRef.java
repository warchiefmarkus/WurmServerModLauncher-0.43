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
/*    */ final class JArrayCompRef
/*    */   extends JExpressionImpl
/*    */   implements JAssignmentTarget
/*    */ {
/*    */   private final JExpression array;
/*    */   private final JExpression index;
/*    */   
/*    */   JArrayCompRef(JExpression array, JExpression index) {
/* 49 */     if (array == null || index == null) {
/* 50 */       throw new NullPointerException();
/*    */     }
/* 52 */     this.array = array;
/* 53 */     this.index = index;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 57 */     f.g(this.array).p('[').g(this.index).p(']');
/*    */   }
/*    */   
/*    */   public JExpression assign(JExpression rhs) {
/* 61 */     return JExpr.assign(this, rhs);
/*    */   }
/*    */   public JExpression assignPlus(JExpression rhs) {
/* 64 */     return JExpr.assignPlus(this, rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JArrayCompRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */