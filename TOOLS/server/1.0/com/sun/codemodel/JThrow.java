/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JStatement;
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
/* 27 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 31 */     f.p("throw");
/* 32 */     f.g((JGenerable)this.expr);
/* 33 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JThrow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */