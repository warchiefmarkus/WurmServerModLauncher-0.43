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
/*    */ 
/*    */ class JContinue
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */   
/*    */   JContinue(JLabel _label) {
/* 38 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 42 */     if (this.label == null) {
/* 43 */       f.p("continue;").nl();
/*    */     } else {
/* 45 */       f.p("continue").p(this.label.label).p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JContinue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */