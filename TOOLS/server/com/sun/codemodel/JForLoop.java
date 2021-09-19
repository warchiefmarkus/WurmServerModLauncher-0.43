/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public class JForLoop
/*    */   implements JStatement
/*    */ {
/* 33 */   private List<Object> inits = new ArrayList();
/* 34 */   private JExpression test = null;
/* 35 */   private List<JExpression> updates = new ArrayList<JExpression>();
/* 36 */   private JBlock body = null;
/*    */   
/*    */   public JVar init(int mods, JType type, String var, JExpression e) {
/* 39 */     JVar v = new JVar(JMods.forVar(mods), type, var, e);
/* 40 */     this.inits.add(v);
/* 41 */     return v;
/*    */   }
/*    */   
/*    */   public JVar init(JType type, String var, JExpression e) {
/* 45 */     return init(0, type, var, e);
/*    */   }
/*    */   
/*    */   public void init(JVar v, JExpression e) {
/* 49 */     this.inits.add(JExpr.assign(v, e));
/*    */   }
/*    */   
/*    */   public void test(JExpression e) {
/* 53 */     this.test = e;
/*    */   }
/*    */   
/*    */   public void update(JExpression e) {
/* 57 */     this.updates.add(e);
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 61 */     if (this.body == null) this.body = new JBlock(); 
/* 62 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 66 */     f.p("for (");
/* 67 */     boolean first = true;
/* 68 */     for (Object o : this.inits) {
/* 69 */       if (!first) f.p(','); 
/* 70 */       if (o instanceof JVar) {
/* 71 */         f.b((JVar)o);
/*    */       } else {
/* 73 */         f.g((JExpression)o);
/* 74 */       }  first = false;
/*    */     } 
/* 76 */     f.p(';').g(this.test).p(';').g((Collection)this.updates).p(')');
/* 77 */     if (this.body != null) {
/* 78 */       f.g(this.body).nl();
/*    */     } else {
/* 80 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JForLoop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */