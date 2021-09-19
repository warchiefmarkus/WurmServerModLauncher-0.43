/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ public class Disease
/*     */   extends KarmaSpell
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public Disease() {
/*  39 */     super("Disease", 547, 10, 1000, 10, 10, 300000L);
/*  40 */     this.targetCreature = true;
/*  41 */     this.offensive = true;
/*  42 */     this.description = "diseases creatures and players";
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
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  54 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  55 */       !performer.getDeity().isLibila())
/*     */     {
/*  57 */       if (performer.faithful)
/*     */       {
/*  59 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  61 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  62 */               .getDeity().getName() + " would never accept your attack on " + target.getNameWithGenus() + ".", (byte)3);
/*     */           
/*  64 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/*  79 */     int baseDamage = 12000;
/*  80 */     int damage = (int)(12000.0D + 8000.0D * power / 100.0D);
/*  81 */     int diseaseGained = (int)Math.max(power / 2.0D, 10.0D);
/*     */     
/*  83 */     if (!target.isUnique() || power > 99.0D) {
/*     */       
/*  85 */       if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  86 */         !performer.getDeity().isLibila())
/*     */       {
/*  88 */         if (!performer.isDuelOrSpar(target))
/*     */         {
/*  90 */           performer.modifyFaith(-(100.0F - performer.getFaith()) / 50.0F);
/*     */         }
/*     */       }
/*  93 */       target.setDisease((byte)Math.min(90, target.getDisease() + diseaseGained));
/*  94 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " diseases you.", (byte)4);
/*     */ 
/*     */       
/*  97 */       if (target.isPlayer()) {
/*     */         
/*  99 */         double defensiveRoll = target.getSoulStrength().skillCheck(power, 0.0D, false, 10.0F);
/* 100 */         if (defensiveRoll <= 50.0D) {
/*     */           
/* 102 */           if (defensiveRoll > 0.0D) {
/* 103 */             damage = (int)(damage * 0.5F);
/*     */           }
/* 105 */           if (performer.getPower() > 1 && Servers.isThisATestServer()) {
/* 106 */             performer.getCommunicator().sendNormalServerMessage("Damage done: " + damage, (byte)2);
/*     */           }
/*     */           
/* 109 */           target.addAttacker(performer);
/* 110 */           target.addWoundOfType(performer, (byte)6, 1, true, 1.0F, false, damage, diseaseGained / 2.5F, 0.0F, false, true);
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 116 */           String tMessage = "You resisted the '" + this.name + "' spell.";
/* 117 */           String pMessage = target.getNameWithGenus() + " resisted your '" + this.name + "' spell.";
/* 118 */           target.getCommunicator().sendCombatNormalMessage(tMessage, (byte)4);
/* 119 */           performer.getCommunicator().sendCombatNormalMessage(pMessage, (byte)4);
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       VolaTile targetVolaTile = Zones.getTileOrNull(target.getTileX(), target
/* 124 */           .getTileY(), target.isOnSurface());
/* 125 */       if (targetVolaTile != null)
/*     */       {
/* 127 */         targetVolaTile.sendAttachCreatureEffect(target, (byte)12, (byte)0, (byte)0, (byte)0, (byte)0);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 133 */       performer.getCommunicator().sendNormalServerMessage("You try to disease " + target
/* 134 */           .getNameWithGenus() + " but fail.", (byte)3);
/* 135 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to disease you but fails.", (byte)4);
/*     */     } 
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
/*     */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 148 */     performer.getCommunicator().sendNormalServerMessage("You try to cast karma disease on " + target
/* 149 */         .getNameWithGenus() + " but fail.", (byte)3);
/*     */     
/* 151 */     target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to cast karma disease on you but fails.", (byte)4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Disease.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */