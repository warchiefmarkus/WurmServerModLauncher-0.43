/*     */ package com.wurmonline.server.creatures.ai.scripts;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericCreatureAI
/*     */   extends CreatureAI
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final int T_NEWMOVEMENT = 0;
/*     */   private static final long TD_NEWMOVEMENT = 30000L;
/*     */   
/*     */   protected boolean pollMovement(final Creature c, long delta) {
/*  23 */     final GenericCreatureAIData aiData = (GenericCreatureAIData)c.getCreatureAIData();
/*     */     
/*  25 */     if (aiData.isMovementFrozen()) {
/*  26 */       return false;
/*     */     }
/*  28 */     if (c.getStatus().getPath() == null) {
/*     */ 
/*     */ 
/*     */       
/*  32 */       if (c.getTarget() != null) {
/*     */ 
/*     */         
/*  35 */         if (!c.getTarget().isWithinDistanceTo(c, 6.0F))
/*     */         {
/*  37 */           c.startPathingToTile(getMovementTarget(c, c.getTarget().getTileX(), c.getTarget().getTileY()));
/*     */         }
/*     */         else
/*     */         {
/*  41 */           c.setOpponent(c.getTarget());
/*     */         }
/*     */       
/*  44 */       } else if ((c.getLatestAttackers()).length > 0) {
/*     */ 
/*     */         
/*  47 */         long[] attackers = c.getLatestAttackers();
/*  48 */         ArrayList<Creature> attackerList = new ArrayList<>();
/*  49 */         for (long a : attackers) {
/*     */ 
/*     */           
/*     */           try {
/*  53 */             attackerList.add(Creatures.getInstance().getCreature(a));
/*     */           }
/*  55 */           catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         Collections.sort(attackerList, new Comparator<Creature>()
/*     */             {
/*     */               
/*     */               public int compare(Creature creature1, Creature creature2)
/*     */               {
/*  66 */                 float distance1 = creature1.getPos2f().distance(c.getPos2f());
/*  67 */                 float distance2 = creature2.getPos2f().distance(c.getPos2f());
/*     */                 
/*  69 */                 if (aiData.doesPreferPlayers()) {
/*     */                   
/*  71 */                   if (creature1.isPlayer() && !creature2.isPlayer()) {
/*  72 */                     distance1 *= aiData.getPrefersPlayersModifier();
/*     */                   }
/*  74 */                   if (!creature1.isPlayer() && creature2.isPlayer()) {
/*  75 */                     distance2 *= aiData.getPrefersPlayersModifier();
/*     */                   }
/*     */                 } 
/*  78 */                 if (distance1 < distance2) return -1; 
/*  79 */                 if (distance2 > distance1) return 1; 
/*  80 */                 return 0;
/*     */               }
/*     */             });
/*     */ 
/*     */         
/*  85 */         boolean gotTarget = false;
/*  86 */         while (!gotTarget) {
/*     */           
/*  88 */           if (attackerList.isEmpty()) {
/*     */             break;
/*     */           }
/*  91 */           Creature newTarget = attackerList.remove(0);
/*  92 */           if (newTarget.isWithinDistanceTo(c, c.getMaxHuntDistance()))
/*     */           {
/*     */             
/*  95 */             c.setTarget(newTarget.getWurmId(), true);
/*  96 */             if (!c.getTarget().isWithinDistanceTo(c, 6.0F))
/*     */             {
/*  98 */               c.startPathingToTile(getMovementTarget(c, c.getTarget().getTileX(), c.getTarget().getTileY()));
/*     */             }
/* 100 */             gotTarget = true;
/*     */           }
/*     */         
/*     */         } 
/* 104 */       } else if (aiData.hasTether() && !c.isWithinTileDistanceTo((int)(aiData.getTetherPos()).x, (int)(aiData.getTetherPos()).y, 0, aiData.getTetherDistance())) {
/*     */ 
/*     */ 
/*     */         
/* 108 */         c.startPathingToTile(getMovementTarget(c, (int)(aiData.getTetherPos()).x, (int)(aiData.getTetherPos()).y));
/*     */       }
/* 110 */       else if (!addPathToInteresting(c, delta)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         increaseTimer(c, delta, new int[] { 0 });
/* 118 */         if (isTimerReady(c, 0, 30000L))
/*     */         {
/*     */ 
/*     */           
/* 122 */           if (Server.rand.nextFloat() < aiData.getRandomMovementChance())
/*     */           {
/* 124 */             simpleMovementTick(c);
/* 125 */             resetTimer(c, new int[] { 0 });
/*     */           }
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 133 */       pathedMovementTick(c);
/* 134 */       if (c.getStatus().getPath().isEmpty()) {
/*     */         
/* 136 */         c.getStatus().setPath(null);
/* 137 */         c.getStatus().setMoving(false);
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void creatureCreated(Creature c) {
/* 147 */     GenericCreatureAIData aiData = (GenericCreatureAIData)c.getCreatureAIData();
/*     */     
/* 149 */     if (aiData.hasTether())
/* 150 */       aiData.setTether(c.getTileX(), c.getTileY()); 
/*     */   }
/*     */   
/*     */   protected abstract boolean addPathToInteresting(Creature paramCreature, long paramLong);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\GenericCreatureAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */