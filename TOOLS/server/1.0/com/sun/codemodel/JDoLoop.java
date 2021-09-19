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
/*    */ public class JDoLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 23 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JDoLoop(JExpression test) {
/* 29 */     this.test = test;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 33 */     if (this.body == null) this.body = new JBlock(); 
/* 34 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 38 */     f.p("do");
/* 39 */     if (this.body != null) {
/* 40 */       f.g((JGenerable)this.body);
/*    */     } else {
/* 42 */       f.p("{ }");
/*    */     } 
/* 44 */     if (JOp.hasTopOp(this.test)) {
/* 45 */       f.p("while ").g((JGenerable)this.test);
/*    */     } else {
/* 47 */       f.p("while (").g((JGenerable)this.test).p(')');
/*    */     } 
/* 49 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JDoLoop.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */