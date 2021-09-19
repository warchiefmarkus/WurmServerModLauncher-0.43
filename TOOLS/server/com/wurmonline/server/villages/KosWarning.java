/*    */ package com.wurmonline.server.villages;
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
/*    */ public class KosWarning
/*    */ {
/*    */   public final long playerId;
/*    */   public final int newReputation;
/* 28 */   private int ticks = 0;
/*    */ 
/*    */   
/*    */   public final Village village;
/*    */   
/*    */   public final boolean permanent;
/*    */ 
/*    */   
/*    */   public KosWarning(long pid, int newRep, Village vill, boolean perma) {
/* 37 */     this.playerId = pid;
/* 38 */     this.newReputation = newRep;
/* 39 */     this.village = vill;
/* 40 */     this.permanent = perma;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getTick() {
/* 45 */     return this.ticks;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int tick() {
/* 50 */     return ++this.ticks;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\KosWarning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */