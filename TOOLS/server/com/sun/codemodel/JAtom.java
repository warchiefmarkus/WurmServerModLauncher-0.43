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
/*    */ final class JAtom
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final String what;
/*    */   
/*    */   JAtom(String what) {
/* 32 */     this.what = what;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 36 */     f.p(this.what);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAtom.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */