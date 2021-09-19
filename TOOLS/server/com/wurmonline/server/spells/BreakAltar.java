/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.endgames.EndGameItems;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BreakAltar
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 10;
/*     */   private static final float MAX_DAM = 99.9F;
/*     */   
/*     */   BreakAltar() {
/*  45 */     super("Break Altar", 258, 30, 20, 50, 40, 300000L);
/*  46 */     this.targetItem = true;
/*  47 */     this.description = "damages altars and huge altars for an increased favor cost";
/*  48 */     this.type = 0;
/*  49 */     this.hasDynamicCost = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCost(Item target) {
/*  56 */     if (target.isHugeAltar())
/*  57 */       return getCost() * 4; 
/*  58 */     return getCost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  69 */     if (!target.isDomainItem()) {
/*     */       
/*  71 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*  72 */       return false;
/*     */     } 
/*  74 */     if (target.isHugeAltar() && !Deities.mayDestroyAltars()) {
/*     */       
/*  76 */       performer.getCommunicator().sendNormalServerMessage("The time is not right. The moons make the " + target
/*  77 */           .getName() + " vulnerable on Wrath day and the day of Awakening in the first and third week of a Starfall.");
/*     */       
/*  79 */       return false;
/*     */     } 
/*  81 */     if (target.isHugeAltar() && performer.getStrengthSkill() < 23.0D) {
/*     */       
/*  83 */       performer.getCommunicator().sendNormalServerMessage("The altar resists your attempt to break it due to your physical weakness. You need at least 23 body strength in order to affect it.");
/*     */       
/*  85 */       return false;
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*     */     try {
/* 100 */       if (target.getOwner() != -10L) {
/*     */         
/* 102 */         Creature owner = Server.getInstance().getCreature(target.getOwner());
/* 103 */         if (!owner.equals(performer)) {
/* 104 */           owner.getCommunicator().sendNormalServerMessage(performer
/* 105 */               .getName() + " damages the " + target.getName() + ".", (byte)4);
/*     */         }
/*     */       } 
/* 108 */     } catch (NoSuchCreatureException|com.wurmonline.server.NoSuchPlayerException|com.wurmonline.server.items.NotOwnedException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (target.getDamage() >= 99.9F) {
/*     */       
/* 114 */       performer.getCommunicator().sendSafeServerMessage("You destroy the " + target.getName() + "!", (byte)3);
/*     */       
/* 116 */       EndGameItems.destroyHugeAltar(target, performer);
/*     */     }
/*     */     else {
/*     */       
/* 120 */       target.setDamage(Math.min(99.9F, target.getDamage() + 1.0F));
/*     */     } 
/* 122 */     if (target.getDamage() < 10.0F) {
/* 123 */       performer.getCommunicator().sendNormalServerMessage("You feel the power of the altar wane for a second.", (byte)2);
/*     */     }
/* 125 */     else if (target.getDamage() < 30.0F) {
/* 126 */       performer.getCommunicator().sendNormalServerMessage("The spell has good effect on the altar.", (byte)2);
/*     */     }
/* 128 */     else if (target.getDamage() < 50.0F) {
/* 129 */       performer.getCommunicator().sendNormalServerMessage("The altar takes good damage from the spell.", (byte)2);
/*     */     }
/* 131 */     else if (target.getDamage() < 70.0F) {
/* 132 */       performer.getCommunicator().sendNormalServerMessage("The altar is starting to look pretty bad.", (byte)2);
/*     */     }
/* 134 */     else if (target.getDamage() < 95.0F) {
/* 135 */       performer.getCommunicator().sendNormalServerMessage("Not much is left of the altar's power now.", (byte)2);
/*     */     } else {
/*     */       
/* 138 */       performer.getCommunicator().sendNormalServerMessage("The altar is shuddering from your spell, and your inner eye can see it collapse.", (byte)2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\BreakAltar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */