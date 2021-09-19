/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Vessle
/*     */   extends ReligiousSpell
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(Vessle.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   Vessle() {
/*  48 */     super("Vessel", 272, 30, 5, 70, 31, 0L);
/*  49 */     this.targetItem = true;
/*  50 */     this.description = "stores favor in a gem";
/*  51 */     this.type = 0;
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
/*  62 */     if (target.isGem()) {
/*     */       
/*  64 */       if (target.isSource()) {
/*     */         
/*  66 */         performer.getCommunicator().sendNormalServerMessage("This gem can hold no more power.", (byte)3);
/*  67 */         return false;
/*     */       } 
/*  69 */       if (target.getData1() > 0) {
/*     */         
/*  71 */         performer.getCommunicator().sendNormalServerMessage("The gem can hold no more power right now.", (byte)3);
/*     */         
/*  73 */         return false;
/*     */       } 
/*  75 */       if (performer.isRoyalPriest()) {
/*     */         
/*  77 */         if (performer.getFavor() - this.cost / 2.0F < 5.0F)
/*     */         {
/*  79 */           performer.getCommunicator().sendNormalServerMessage("You have too little favor left to store.", (byte)3);
/*     */           
/*  81 */           return false;
/*     */         }
/*     */       
/*  84 */       } else if ((performer.getFavor() - this.cost) * 2.0F < 5.0F) {
/*     */         
/*  86 */         performer.getCommunicator().sendNormalServerMessage("You have too little favor left to store.", (byte)3);
/*     */         
/*  88 */         return false;
/*     */       } 
/*  90 */       return true;
/*     */     } 
/*  92 */     performer.getCommunicator().sendNormalServerMessage("You need something pure and beautiful.", (byte)3);
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 114 */     if (target.isGem())
/*     */     {
/* 116 */       if (target.getQualityLevel() > 10.0F) {
/*     */         
/* 118 */         int favorStored = (int)Math.min(performer.getFavor() * 2.0F, target.getQualityLevel() * 2.0F);
/* 119 */         if (favorStored > 5)
/*     */         {
/* 121 */           float qlMod = target.getRarity() + 1.0F;
/* 122 */           target.setData1((int)(favorStored * qlMod));
/*     */ 
/*     */           
/*     */           try {
/* 126 */             performer.setFavor(performer.getFavor() - favorStored / 2.0F);
/*     */           }
/* 128 */           catch (IOException iox) {
/*     */             
/* 130 */             logger.log(Level.WARNING, performer.getName() + ":" + iox.getMessage(), iox);
/*     */           } 
/* 132 */           if (performer.getDeity().getNumber() == 4) {
/* 133 */             performer.getCommunicator().sendNormalServerMessage("You fill the gem with the power of your hate.", (byte)2);
/*     */           }
/* 135 */           else if (performer.getDeity().getNumber() == 3) {
/* 136 */             performer.getCommunicator().sendNormalServerMessage("You fill the gem with the power of your determination.", (byte)2);
/*     */           }
/* 138 */           else if (performer.getDeity().getNumber() == 2) {
/* 139 */             performer.getCommunicator().sendNormalServerMessage("You fill the gem with the power of your rage.", (byte)2);
/*     */           }
/* 141 */           else if (performer.getDeity().getNumber() == 1) {
/* 142 */             performer.getCommunicator().sendNormalServerMessage("You fill the gem with the power of your love.", (byte)2);
/*     */           } else {
/*     */             
/* 145 */             performer.getCommunicator()
/* 146 */               .sendNormalServerMessage("You fill the gem with the power of your devotion.", (byte)2);
/*     */           } 
/*     */           
/* 149 */           performer.achievement(614);
/*     */         }
/*     */         else
/*     */         {
/* 153 */           target.setQualityLevel(target.getQualityLevel() - 3.0F + target.getRarity());
/* 154 */           performer.getCommunicator().sendNormalServerMessage("You fail to store any favor but the " + target
/* 155 */               .getName() + " is damaged in the process.", (byte)3);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 161 */         target.setQualityLevel(target.getQualityLevel() - 3.0F);
/* 162 */         performer.getCommunicator().sendNormalServerMessage("The gem is of too low quality to store any power and is damaged a bit.", (byte)3);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Vessle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */