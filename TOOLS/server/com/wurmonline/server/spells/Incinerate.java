/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
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
/*     */ public class Incinerate
/*     */   extends KarmaEnchantment
/*     */ {
/*     */   public static final int RANGE = 24;
/*     */   
/*     */   public Incinerate() {
/*  39 */     super("Incinerate", 686, 15, 750, 10, 1, 60000L);
/*  40 */     this.targetCreature = true;
/*  41 */     this.offensive = true;
/*  42 */     this.enchantment = 94;
/*     */     
/*  44 */     this.description = "creates a heat wave around the target";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  55 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  56 */       !performer.getDeity().isHateGod())
/*     */     {
/*  58 */       if (performer.faithful)
/*     */       {
/*  60 */         if (!performer.isDuelOrSpar(target)) {
/*     */           
/*  62 */           performer.getCommunicator().sendNormalServerMessage(performer
/*  63 */               .getDeity().getName() + " would never accept your attack on " + target.getName() + ".", (byte)3);
/*     */           
/*  65 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*  69 */     return true;
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
/*  80 */     if (!target.isUnique() || power > 99.0D) {
/*     */       
/*  82 */       if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2 && 
/*  83 */         !performer.getDeity().isHateGod())
/*     */       {
/*  85 */         if (!performer.isDuelOrSpar(target))
/*     */         {
/*  87 */           performer.modifyFaith(-(100.0F - performer.getFaith()) / 50.0F);
/*     */         }
/*     */       }
/*  90 */       SpellEffects effs = target.getSpellEffects();
/*  91 */       if (effs == null)
/*     */       {
/*  93 */         effs = target.createSpellEffects();
/*     */       }
/*  95 */       SpellEffect eff = effs.getSpellEffect(this.enchantment);
/*  96 */       if (eff == null)
/*     */       {
/*  98 */         if (target != performer)
/*  99 */           performer.getCommunicator().sendNormalServerMessage(target
/* 100 */               .getNameWithGenus() + " is engulfed in a wave of extreme heat.", (byte)4); 
/* 101 */         target.getCommunicator().sendAlertServerMessage("You are engulfed in a wave of extreme heat!", (byte)4);
/*     */         
/* 103 */         eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, 60, (byte)9, (byte)1, true);
/*     */         
/* 105 */         effs.addSpellEffect(eff);
/* 106 */         Server.getInstance().broadCastAction(performer
/* 107 */             .getNameWithGenus() + " looks pleased as " + performer.getHeSheItString() + " engulfs " + target
/* 108 */             .getNameWithGenus() + " in a wave of heat.", performer, 5);
/*     */ 
/*     */       
/*     */       }
/* 112 */       else if (eff.getPower() > power)
/*     */       {
/* 114 */         performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the heat.", (byte)3);
/*     */         
/* 116 */         Server.getInstance().broadCastAction(performer.getNameWithGenus() + " frowns.", performer, 5);
/*     */       }
/*     */       else
/*     */       {
/* 120 */         if (target != performer) {
/* 121 */           performer.getCommunicator().sendNormalServerMessage("You succeed in improving the heat around " + target
/* 122 */               .getNameWithGenus() + ".", (byte)2);
/*     */         }
/* 124 */         target.getCommunicator()
/* 125 */           .sendAlertServerMessage("The heat around you increases. The pain is excruciating!", (byte)4);
/*     */         
/* 127 */         eff.setPower((float)power);
/* 128 */         eff.setTimeleft(60);
/* 129 */         target.sendUpdateSpellEffect(eff);
/* 130 */         Server.getInstance().broadCastAction(performer
/* 131 */             .getNameWithGenus() + " looks pleased as " + performer.getHeSheItString() + " increases the heat around " + target
/* 132 */             .getNameWithGenus() + ".", performer, 5);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 138 */       performer.getCommunicator().sendNormalServerMessage("You try to incinerate " + target
/* 139 */           .getNameWithGenus() + " but fail.", (byte)3);
/*     */       
/* 141 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to incinerate you but fails.", (byte)4);
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
/* 154 */     performer.getCommunicator().sendNormalServerMessage("You try to incinerate " + target
/* 155 */         .getNameWithGenus() + " but fail.", (byte)3);
/*     */     
/* 157 */     target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " tries to incinerate you but fails.", (byte)4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Incinerate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */