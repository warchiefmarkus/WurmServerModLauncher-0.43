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
/*    */ class JAnonymousClass
/*    */   extends JDefinedClass
/*    */ {
/*    */   private final JClass base;
/*    */   
/*    */   JAnonymousClass(JClass _base) {
/* 36 */     super(_base.owner(), 0, null);
/* 37 */     this.base = _base;
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 41 */     return this.base.fullName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnonymousClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */