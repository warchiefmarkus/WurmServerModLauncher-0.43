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
/*    */ public class JAssignment
/*    */   extends JExpressionImpl
/*    */   implements JStatement
/*    */ {
/*    */   JAssignmentTarget lhs;
/*    */   JExpression rhs;
/* 31 */   String op = "";
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs) {
/* 34 */     this.lhs = lhs;
/* 35 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs, String op) {
/* 39 */     this.lhs = lhs;
/* 40 */     this.rhs = rhs;
/* 41 */     this.op = op;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 45 */     f.g(this.lhs).p(this.op + '=').g(this.rhs);
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 49 */     f.g(this).p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAssignment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */