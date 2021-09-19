/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
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
/*     */ public final class CharmAnimal
/*     */   extends ReligiousSpell
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(CharmAnimal.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 6;
/*     */ 
/*     */   
/*     */   CharmAnimal() {
/*  42 */     super("Charm Animal", 275, 20, 40, 35, 30, 0L);
/*  43 */     this.targetCreature = true;
/*  44 */     this.offensive = true;
/*  45 */     this.description = "makes a tamable creature become your loyal companion";
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
/*  57 */     if (performer.getPet() != null)
/*     */     {
/*  59 */       if (DbCreatureStatus.getIsLoaded(performer.getPet().getWurmId()) == 1) {
/*     */         
/*  61 */         performer.getCommunicator().sendNormalServerMessage("You have a pet in a cage, remove it first, to charm this one.", (byte)3);
/*     */         
/*  63 */         return false;
/*     */       } 
/*     */     }
/*  66 */     if (target.isDominatable(performer) && target.isAnimal() && !target.isUnique() && 
/*  67 */       !target.isReborn())
/*     */     {
/*  69 */       return true;
/*     */     }
/*     */     
/*  72 */     performer.getCommunicator().sendNormalServerMessage("You fail to connect with the " + target.getName() + ".", (byte)3);
/*     */     
/*  74 */     return false;
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
/*  85 */     if (power > 0.0D) {
/*     */       
/*  87 */       if (performer.getPet() != null && performer.getPet() != target) {
/*     */         
/*  89 */         performer.getCommunicator().sendNormalServerMessage(performer.getPet().getNameWithGenus() + " stops following you.", (byte)2);
/*     */         
/*  91 */         if (performer.getPet().getLeader() == performer)
/*  92 */           performer.getPet().setLeader(null); 
/*  93 */         performer.getPet().setDominator(-10L);
/*  94 */         performer.setPet(-10L);
/*     */       } 
/*  96 */       boolean newpet = false;
/*  97 */       if (target.dominator != performer.getWurmId())
/*  98 */         newpet = true; 
/*  99 */       target.setTarget(-10L, true);
/* 100 */       target.stopFighting();
/* 101 */       if (performer.getTarget() == target) {
/*     */         
/* 103 */         performer.stopFighting();
/* 104 */         performer.setTarget(-10L, true);
/*     */       } 
/* 106 */       if (target.opponent == performer)
/* 107 */         target.setOpponent(null); 
/* 108 */       if (performer.opponent == target) {
/* 109 */         performer.setOpponent(null);
/*     */       }
/*     */       try {
/* 112 */         target.setKingdomId(performer.getKingdomId());
/*     */       }
/* 114 */       catch (IOException iox) {
/*     */         
/* 116 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */       } 
/* 118 */       target.setDominator(performer.getWurmId());
/*     */       
/* 120 */       if (newpet) {
/*     */         
/* 122 */         target.setLoyalty((float)Math.max(10.0D, power));
/* 123 */         performer.setPet(target.getWurmId());
/* 124 */         target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " charms you!");
/* 125 */         performer.getCommunicator().sendNormalServerMessage("You overwhelm " + target
/* 126 */             .getNameWithGenus() + " with love and trust.", (byte)2);
/* 127 */         if (target.isUnique())
/*     */         {
/* 129 */           HistoryManager.addHistory(performer.getName(), "charms " + target.getName());
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 134 */         target.setLoyalty((float)Math.min(99.0D, target.getLoyalty() + power / 10.0D));
/* 135 */         target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " charms you!");
/* 136 */         performer.getCommunicator().sendNormalServerMessage("You strengthen " + target.getNameWithGenus() + "'s love in you.", (byte)2);
/*     */       } 
/*     */       
/* 139 */       target.getStatus().setLastPolledLoyalty();
/*     */     } else {
/*     */       
/* 142 */       performer.getCommunicator().sendNormalServerMessage("You fail to convey your love and trust to " + target
/* 143 */           .getNameWithGenus() + ".", (byte)3);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\CharmAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */