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
/*    */ final class JBreak
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */   
/*    */   JBreak(JLabel _label) {
/* 38 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 42 */     if (this.label == null) {
/* 43 */       f.p("break;").nl();
/*    */     } else {
/* 45 */       f.p("break").p(this.label.label).p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JBreak.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */