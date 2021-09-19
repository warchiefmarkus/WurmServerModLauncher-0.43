/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
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
/*    */ final class SymbolicLink
/*    */   extends File
/*    */ {
/*    */   private final JimfsPath target;
/*    */   
/*    */   public static SymbolicLink create(int id, JimfsPath target) {
/* 34 */     return new SymbolicLink(id, target);
/*    */   }
/*    */   
/*    */   private SymbolicLink(int id, JimfsPath target) {
/* 38 */     super(id);
/* 39 */     this.target = (JimfsPath)Preconditions.checkNotNull(target);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JimfsPath target() {
/* 46 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   File copyWithoutContent(int id) {
/* 51 */     return create(id, this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\SymbolicLink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */