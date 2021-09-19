/*    */ package com.wurmonline.server.utils;
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
/*    */ public class ItemParentDatabaseUpdatable
/*    */   implements WurmDbUpdatable
/*    */ {
/*    */   private final long id;
/*    */   private final long owner;
/*    */   private final String updateStatement;
/*    */   
/*    */   public ItemParentDatabaseUpdatable(long aId, long aOwner, String aUpdateStatement) {
/* 24 */     this.id = aId;
/* 25 */     this.owner = aOwner;
/* 26 */     this.updateStatement = aUpdateStatement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDatabaseUpdateStatement() {
/* 37 */     return this.updateStatement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   long getId() {
/* 47 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getOwner() {
/* 57 */     return this.owner;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "ItemParentDatabaseUpdatable [id=" + this.id + ", owner=" + this.owner + ", updateStatement=" + this.updateStatement + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\ItemParentDatabaseUpdatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */