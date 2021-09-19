/*     */ package com.wurmonline.server.creatures.ai.scripts;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAIData;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BartenderAI
/*     */   extends CreatureAI
/*     */ {
/*     */   private static final long MIN_TIME_TALK = 120000L;
/*     */   private static final long MIN_TIME_NEWPATH = 30000L;
/*     */   private static final int TIMER_SPECTALK = 0;
/*     */   private static final int TIMER_NEWPATH = 1;
/*     */   
/*     */   public void creatureCreated(Creature c) {
/*  20 */     if (c.getCurrentTile().getVillage() != null) {
/*  21 */       ((BartenderAIData)c.getCreatureAIData()).setHomeVillage(c.getCurrentTile().getVillage());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean pollSpecialFinal(Creature c, long delta) {
/*  27 */     increaseTimer(c, delta, new int[] { 0 });
/*  28 */     if (!isTimerReady(c, 0, 120000L)) {
/*  29 */       return false;
/*     */     }
/*  31 */     c.say("Come and get some tasty treats!");
/*  32 */     resetTimer(c, new int[] { 0 });
/*     */     
/*  34 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollMovement(Creature c, long delta) {
/*  40 */     BartenderAIData aiData = (BartenderAIData)c.getCreatureAIData();
/*  41 */     if (aiData.getFoodTarget() != null)
/*     */     {
/*  43 */       if (aiData.getFoodTarget().getTileX() == c.getTileX() && aiData.getFoodTarget().getTileY() == c.getTileY()) {
/*     */         
/*  45 */         c.say("Hey " + aiData.getFoodTarget().getName() + " you look hungry, come and get some food!");
/*  46 */         aiData.setFoodTarget(null);
/*     */       } 
/*     */     }
/*     */     
/*  50 */     if (c.getStatus().getPath() == null) {
/*     */       
/*  52 */       if (aiData.getHomeVillage() != null)
/*     */       {
/*  54 */         if (c.getCurrentTile().getVillage() != aiData.getHomeVillage()) {
/*     */           
/*  56 */           c.startPathingToTile(getMovementTarget(c, aiData.getHomeVillage().getTokenX(), aiData.getHomeVillage().getTokenY()));
/*  57 */           return false;
/*     */         } 
/*     */       }
/*     */       
/*  61 */       increaseTimer(c, delta, new int[] { 1 });
/*  62 */       if (isTimerReady(c, 1, 30000L))
/*     */       {
/*  64 */         if (Server.rand.nextInt(100) < 10) {
/*     */           
/*  66 */           Creature[] nearbyCreatures = c.getCurrentTile().getZone().getAllCreatures();
/*  67 */           for (Creature otherC : nearbyCreatures) {
/*     */             
/*  69 */             if (otherC != c)
/*     */             {
/*     */               
/*  72 */               if (otherC.isPlayer())
/*     */               {
/*     */                 
/*  75 */                 if (otherC.getStatus().isHungry())
/*     */                 {
/*  77 */                   if (otherC.getCurrentTile().getVillage() == aiData.getHomeVillage())
/*     */                   {
/*     */                     
/*  80 */                     if (otherC.getTileX() != c.getTileX() || otherC.getTileY() != c.getTileY()) {
/*     */                       
/*  82 */                       c.startPathingToTile(getMovementTarget(c, otherC.getTileX(), otherC.getTileY()));
/*  83 */                       aiData.setFoodTarget(otherC);
/*     */                     }  }  }  } 
/*     */             }
/*     */           } 
/*     */         } 
/*  88 */         resetTimer(c, new int[] { 1 });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  93 */       pathedMovementTick(c);
/*     */       
/*  95 */       if (c.getStatus().getPath().isEmpty()) {
/*     */         
/*  97 */         c.getStatus().setPath(null);
/*  98 */         c.getStatus().setMoving(false);
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollAttack(Creature c, long delta) {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollBreeding(Creature c, long delta) {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureAIData createCreatureAIData() {
/* 122 */     return new BartenderAIData();
/*     */   }
/*     */   
/*     */   class BartenderAIData
/*     */     extends CreatureAIData {
/* 127 */     private Creature currentFoodTarget = null;
/*     */     
/* 129 */     private Village homeVillage = null;
/*     */ 
/*     */     
/*     */     void setHomeVillage(Village homeVillage) {
/* 133 */       this.homeVillage = homeVillage;
/*     */     }
/*     */ 
/*     */     
/*     */     Village getHomeVillage() {
/* 138 */       return this.homeVillage;
/*     */     }
/*     */ 
/*     */     
/*     */     void setFoodTarget(Creature newFoodTarget) {
/* 143 */       this.currentFoodTarget = newFoodTarget;
/*     */     }
/*     */ 
/*     */     
/*     */     Creature getFoodTarget() {
/* 148 */       return this.currentFoodTarget;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\BartenderAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */