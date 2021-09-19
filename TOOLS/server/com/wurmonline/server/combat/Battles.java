/*     */ package com.wurmonline.server.combat;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Battles
/*     */   implements TimeConstants
/*     */ {
/*  32 */   private static final List<Battle> battles = new LinkedList<>();
/*     */   
/*  34 */   private static final Logger logger = Logger.getLogger(Battles.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Battle getBattleFor(Creature creature) {
/*  45 */     for (Battle battle : battles) {
/*     */       
/*  47 */       if (battle.containsCreature(creature))
/*  48 */         return battle; 
/*     */     } 
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Battle getBattleFor(Creature attacker, Creature defender) {
/*  55 */     Battle bone = getBattleFor(attacker);
/*  56 */     Battle btwo = getBattleFor(defender);
/*  57 */     Battle toReturn = null;
/*  58 */     if (bone == null && btwo == null) {
/*     */       
/*  60 */       toReturn = new Battle(attacker, defender);
/*  61 */       battles.add(toReturn);
/*     */     }
/*  63 */     else if (bone == null && btwo != null) {
/*     */       
/*  65 */       btwo.addCreature(attacker);
/*  66 */       toReturn = btwo;
/*     */     }
/*  68 */     else if (btwo == null && bone != null) {
/*     */       
/*  70 */       bone.addCreature(defender);
/*  71 */       toReturn = bone;
/*     */     } else {
/*     */       
/*  74 */       toReturn = mergeBattles(bone, btwo);
/*  75 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Battle mergeBattles(Battle battleOne, Battle battleTwo) {
/*  80 */     if (battleTwo != null && battleOne != null) {
/*     */       
/*  82 */       Creature[] bonec = battleTwo.getCreatures();
/*  83 */       for (Creature lElement : bonec)
/*     */       {
/*  85 */         battleOne.addCreature(lElement);
/*     */       }
/*  87 */       battles.remove(battleTwo);
/*     */     }
/*     */     else {
/*     */       
/*  91 */       logger.warning("Cannot merge null battles: battleOne: " + battleOne + ", battleTwo: " + battleTwo);
/*     */     } 
/*  93 */     return battleOne;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void poll(boolean shutdown) {
/*  98 */     long now = System.currentTimeMillis();
/*  99 */     for (ListIterator<Battle> it = battles.listIterator(); it.hasNext(); ) {
/*     */ 
/*     */       
/* 102 */       Battle battle = it.next();
/* 103 */       if ((battle.getCreatures()).length <= 1 || now - battle.getEndTime() > 300000L || shutdown) {
/*     */         
/* 105 */         battle.save();
/* 106 */         battle.clearCreatures();
/* 107 */         it.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\Battles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */