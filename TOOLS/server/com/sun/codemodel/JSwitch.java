/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public final class JSwitch
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 39 */   private List<JCase> cases = new ArrayList<JCase>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   private JCase defaultCase = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JSwitch(JExpression test) {
/* 50 */     this.test = test;
/*    */   }
/*    */   public JExpression test() {
/* 53 */     return this.test;
/*    */   } public Iterator cases() {
/* 55 */     return this.cases.iterator();
/*    */   }
/*    */   public JCase _case(JExpression label) {
/* 58 */     JCase c = new JCase(label);
/* 59 */     this.cases.add(c);
/* 60 */     return c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JCase _default() {
/* 67 */     this.defaultCase = new JCase(null, true);
/* 68 */     return this.defaultCase;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 72 */     if (JOp.hasTopOp(this.test)) {
/* 73 */       f.p("switch ").g(this.test).p(" {").nl();
/*    */     } else {
/* 75 */       f.p("switch (").g(this.test).p(')').p(" {").nl();
/*    */     } 
/* 77 */     for (JCase c : this.cases)
/* 78 */       f.s(c); 
/* 79 */     if (this.defaultCase != null)
/* 80 */       f.s(this.defaultCase); 
/* 81 */     f.p('}').nl();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JSwitch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */