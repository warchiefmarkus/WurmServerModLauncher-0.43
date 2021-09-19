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
/*    */ public final class JForEach
/*    */   implements JStatement
/*    */ {
/*    */   private final JType type;
/*    */   private final String var;
/* 33 */   private JBlock body = null;
/*    */   
/*    */   private final JExpression collection;
/*    */   private final JVar loopVar;
/*    */   
/*    */   public JForEach(JType vartype, String variable, JExpression collection) {
/* 39 */     this.type = vartype;
/* 40 */     this.var = variable;
/* 41 */     this.collection = collection;
/* 42 */     this.loopVar = new JVar(JMods.forVar(0), this.type, this.var, collection);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JVar var() {
/* 50 */     return this.loopVar;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 54 */     if (this.body == null)
/* 55 */       this.body = new JBlock(); 
/* 56 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 60 */     f.p("for (");
/* 61 */     f.g(this.type).id(this.var).p(": ").g(this.collection);
/* 62 */     f.p(')');
/* 63 */     if (this.body != null) {
/* 64 */       f.g(this.body).nl();
/*    */     } else {
/* 66 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JForEach.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */