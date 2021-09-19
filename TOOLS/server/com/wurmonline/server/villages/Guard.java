/*    */ package com.wurmonline.server.villages;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ public abstract class Guard
/*    */ {
/*    */   final int villageId;
/*    */   final Creature creature;
/*    */   long expireDate;
/*    */   
/*    */   Guard(int aVillageId, Creature aCreature, long aExpireDate) {
/* 40 */     this.villageId = aVillageId;
/* 41 */     this.creature = aCreature;
/* 42 */     this.expireDate = aExpireDate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long getExpireDate() {
/* 51 */     return this.expireDate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Creature getCreature() {
/* 60 */     return this.creature;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int getVillageId() {
/* 69 */     return this.villageId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   abstract void save();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   abstract void setExpireDate(long paramLong);
/*    */ 
/*    */ 
/*    */   
/*    */   abstract void delete();
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return "Guard [villageId=" + this.villageId + ", expireDate=" + this.expireDate + ", creature=" + this.creature + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Guard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */