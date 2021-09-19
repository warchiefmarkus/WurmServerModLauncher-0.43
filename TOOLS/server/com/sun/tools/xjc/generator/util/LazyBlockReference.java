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
/*    */ 
/*    */ 
/*    */ public abstract class LazyBlockReference
/*    */   implements BlockReference
/*    */ {
/* 48 */   private JBlock block = null;
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract JBlock create();
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock get(boolean create) {
/* 57 */     if (!create) return this.block; 
/* 58 */     if (this.block == null)
/* 59 */       this.block = create(); 
/* 60 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generato\\util\LazyBlockReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */