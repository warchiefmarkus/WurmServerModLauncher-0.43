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
/*    */ public class JClassAlreadyExistsException
/*    */   extends Exception
/*    */ {
/*    */   private final JDefinedClass existing;
/*    */   
/*    */   public JClassAlreadyExistsException(JDefinedClass _existing) {
/* 32 */     this.existing = _existing;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDefinedClass getExistingClass() {
/* 42 */     return this.existing;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JClassAlreadyExistsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */