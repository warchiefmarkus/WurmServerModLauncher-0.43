/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JStatement;
/*    */ 
/*    */ public class JAssignment
/*    */   extends JExpressionImpl
/*    */   implements JStatement
/*    */ {
/*    */   JAssignmentTarget lhs;
/*    */   JExpression rhs;
/* 16 */   String op = "";
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs) {
/* 19 */     this.lhs = lhs;
/* 20 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs, String op) {
/* 24 */     this.lhs = lhs;
/* 25 */     this.rhs = rhs;
/* 26 */     this.op = op;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 30 */     f.g((JGenerable)this.lhs).p(this.op + '=').g((JGenerable)this.rhs);
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 34 */     f.g((JGenerable)this).p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JAssignment.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */