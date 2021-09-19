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
/*    */ final class JAnnotationStringValue
/*    */   extends JAnnotationValue
/*    */ {
/*    */   private final JExpression value;
/*    */   
/*    */   JAnnotationStringValue(JExpression value) {
/* 38 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 42 */     f.g(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnnotationStringValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */