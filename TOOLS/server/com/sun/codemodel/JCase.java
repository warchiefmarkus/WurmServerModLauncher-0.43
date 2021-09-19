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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JCase
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression label;
/* 35 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isDefaultCase = false;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label) {
/* 46 */     this(label, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label, boolean isDefaultCase) {
/* 54 */     this.label = label;
/* 55 */     this.isDefaultCase = isDefaultCase;
/*    */   }
/*    */   
/*    */   public JExpression label() {
/* 59 */     return this.label;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 63 */     if (this.body == null) this.body = new JBlock(false, true); 
/* 64 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 68 */     f.i();
/* 69 */     if (!this.isDefaultCase) {
/* 70 */       f.p("case ").g(this.label).p(':').nl();
/*    */     } else {
/* 72 */       f.p("default:").nl();
/*    */     } 
/* 74 */     if (this.body != null)
/* 75 */       f.s(this.body); 
/* 76 */     f.o();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JCase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */