/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JOp;
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
/*    */ public class JWhileLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 23 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JWhileLoop(JExpression test) {
/* 29 */     this.test = test;
/*    */   }
/*    */   
/*    */   public JExpression test() {
/* 33 */     return this.test;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 37 */     if (this.body == null) this.body = new JBlock(); 
/* 38 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 42 */     if (JOp.hasTopOp(this.test)) {
/* 43 */       f.p("while ").g((JGenerable)this.test);
/*    */     } else {
/* 45 */       f.p("while (").g((JGenerable)this.test).p(')');
/*    */     } 
/* 47 */     if (this.body != null) {
/* 48 */       f.s((JStatement)this.body);
/*    */     } else {
/* 50 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JWhileLoop.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */