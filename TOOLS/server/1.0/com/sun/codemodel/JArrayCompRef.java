/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JAssignmentTarget;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JExpressionImpl;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
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
/*    */ public class JArrayCompRef
/*    */   extends JExpressionImpl
/*    */   implements JAssignmentTarget
/*    */ {
/*    */   private JExpression array;
/*    */   private JExpression index;
/*    */   
/*    */   JArrayCompRef(JExpression array, JExpression index) {
/* 34 */     if (array == null || index == null) {
/* 35 */       throw new NullPointerException();
/*    */     }
/*    */     
/* 38 */     this.array = array;
/* 39 */     this.index = index;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 43 */     f.g((JGenerable)this.array).p('[').g((JGenerable)this.index).p(']');
/*    */   }
/*    */   
/*    */   public JExpression assign(JExpression rhs) {
/* 47 */     return JExpr.assign(this, rhs);
/*    */   }
/*    */   public JExpression assignPlus(JExpression rhs) {
/* 50 */     return JExpr.assignPlus(this, rhs);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JArrayCompRef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */