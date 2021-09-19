/*    */ package com.wurmonline.server.creatures.ai;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CreatureAIData
/*    */ {
/*    */   private Creature creature;
/* 11 */   private long lastPollTime = 0L;
/*    */   
/*    */   private boolean dropsCorpse = true;
/*    */   
/* 15 */   private float movementSpeedModifier = 1.0F;
/*    */   
/* 17 */   private float sizeModifier = 1.0F;
/*    */   
/* 19 */   private HashMap<Integer, Long> aiTimerMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public void setTimer(int timer, long time) {
/* 23 */     if (!this.aiTimerMap.containsKey(Integer.valueOf(timer))) {
/* 24 */       this.aiTimerMap.put(Integer.valueOf(timer), Long.valueOf(time));
/*    */     } else {
/* 26 */       this.aiTimerMap.replace(Integer.valueOf(timer), Long.valueOf(time));
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getTimer(int timer) {
/* 31 */     if (!this.aiTimerMap.containsKey(Integer.valueOf(timer))) {
/* 32 */       setTimer(timer, 0L);
/*    */     }
/* 34 */     return ((Long)this.aiTimerMap.get(Integer.valueOf(timer))).longValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCreature(Creature c) {
/* 39 */     this.creature = c;
/*    */   }
/*    */ 
/*    */   
/*    */   public Creature getCreature() {
/* 44 */     return this.creature;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastPollTime() {
/* 49 */     return this.lastPollTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLastPollTime(long lastPollTime) {
/* 54 */     this.lastPollTime = lastPollTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesDropCorpse() {
/* 59 */     return this.dropsCorpse;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDropsCorpse(boolean dropsCorpse) {
/* 64 */     this.dropsCorpse = dropsCorpse;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMovementSpeedModifier() {
/* 69 */     return this.movementSpeedModifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMovementSpeedModifier(float movementModifier) {
/* 74 */     this.movementSpeedModifier = movementModifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getSpeed() {
/* 79 */     return this.creature.getTemplate().getSpeed();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getSizeModifier() {
/* 84 */     return this.sizeModifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSizeModifier(float sizeModifier) {
/* 89 */     this.sizeModifier = sizeModifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\CreatureAIData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */