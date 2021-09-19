/*    */ package com.sun.tools.xjc.generator.util;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExistingBlockReference
/*    */   implements BlockReference
/*    */ {
/*    */   private final JBlock block;
/*    */   
/*    */   public ExistingBlockReference(JBlock _block) {
/* 49 */     this.block = _block;
/*    */   }
/*    */   
/*    */   public JBlock get(boolean create) {
/* 53 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generato\\util\ExistingBlockReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */