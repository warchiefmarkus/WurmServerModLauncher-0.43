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
/*    */ public class JCatchBlock
/*    */   implements JGenerable
/*    */ {
/*    */   JClass exception;
/* 31 */   private JVar var = null;
/* 32 */   private JBlock body = new JBlock();
/*    */   
/*    */   JCatchBlock(JClass exception) {
/* 35 */     this.exception = exception;
/*    */   }
/*    */   
/*    */   public JVar param(String name) {
/* 39 */     if (this.var != null) throw new IllegalStateException(); 
/* 40 */     this.var = new JVar(JMods.forVar(0), this.exception, name, null);
/* 41 */     return this.var;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 45 */     return this.body;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 49 */     if (this.var == null) {
/* 50 */       this.var = new JVar(JMods.forVar(0), this.exception, "_x", null);
/*    */     }
/* 52 */     f.p("catch (").b(this.var).p(')').g(this.body);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JCatchBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */