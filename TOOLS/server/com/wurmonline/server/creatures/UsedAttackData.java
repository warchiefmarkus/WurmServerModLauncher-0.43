/*    */ package com.wurmonline.server.creatures;
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
/*    */ public class UsedAttackData
/*    */ {
/*    */   private float time;
/*    */   private int rounds;
/*    */   
/*    */   public UsedAttackData(float swingTime, int round) {
/* 30 */     this.time = swingTime;
/* 31 */     this.rounds = round;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getRounds() {
/* 36 */     return this.rounds;
/*    */   }
/*    */ 
/*    */   
/*    */   public final float getTime() {
/* 41 */     return this.time;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRounds(int numberOfRounds) {
/* 46 */     this.rounds = numberOfRounds;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTime(float newTime) {
/* 51 */     this.time = newTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(float newTime) {
/* 56 */     this.time = Math.max(0.0F, newTime);
/* 57 */     this.rounds = Math.max(this.rounds - 1, 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\UsedAttackData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */