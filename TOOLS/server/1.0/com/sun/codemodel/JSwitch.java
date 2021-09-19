/*    */ package 1.0.com.sun.codemodel;
/*    */ 
/*    */ import com.sun.codemodel.JCase;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JFormatter;
/*    */ import com.sun.codemodel.JGenerable;
/*    */ import com.sun.codemodel.JOp;
/*    */ import com.sun.codemodel.JStatement;
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JSwitch
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 23 */   private Vector cases = new Vector();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   private JCase defaultCase = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JSwitch(JExpression test) {
/* 34 */     this.test = test;
/*    */   }
/*    */   public JExpression test() {
/* 37 */     return this.test;
/*    */   } public Iterator cases() {
/* 39 */     return this.cases.iterator();
/*    */   }
/*    */   public JCase _case(JExpression label) {
/* 42 */     JCase c = new JCase(label);
/* 43 */     this.cases.add(c);
/* 44 */     return c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JCase _default() {
/* 51 */     this.defaultCase = new JCase(null, true);
/* 52 */     return this.defaultCase;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 56 */     if (JOp.hasTopOp(this.test)) {
/* 57 */       f.p("switch ").g((JGenerable)this.test).p(" {").nl();
/*    */     } else {
/* 59 */       f.p("switch (").g((JGenerable)this.test).p(')').p(" {").nl();
/*    */     } 
/* 61 */     Iterator itr = cases();
/* 62 */     while (itr.hasNext())
/* 63 */       f.s((JStatement)itr.next()); 
/* 64 */     if (this.defaultCase != null)
/* 65 */       f.s((JStatement)this.defaultCase); 
/* 66 */     f.p('}').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JSwitch.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */