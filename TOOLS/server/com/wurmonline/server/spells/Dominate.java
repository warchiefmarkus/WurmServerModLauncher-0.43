/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
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
/*     */ public class Dominate
/*     */   extends ReligiousSpell
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(Dominate.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 6;
/*     */ 
/*     */   
/*     */   Dominate() {
/*  43 */     super("Dominate", 274, 20, 40, 35, 39, 0L);
/*  44 */     this.targetCreature = true;
/*  45 */     this.offensive = true;
/*  46 */     this.dominate = true;
/*  47 */     this.description = "forces a creature or monster to become a loyal companion of yours";
/*  48 */     this.type = 2;
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
/*  59 */     return mayDominate(performer, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean mayDominate(Creature performer, Creature target) {
/*  64 */     if (performer.getDeity() != null) {
/*     */       
/*  66 */       if (target.isDominatable(performer) && target.isMonster()) {
/*     */ 
/*     */         
/*  69 */         if (performer.getPet() != null)
/*     */         {
/*  71 */           if (DbCreatureStatus.getIsLoaded(performer.getPet().getWurmId()) == 1) {
/*     */             
/*  73 */             performer.getCommunicator().sendNormalServerMessage("You have a pet in a cage, remove it first, to dominate this one.", (byte)3);
/*     */             
/*  75 */             return false;
/*     */           } 
/*     */         }
/*  78 */         if (target.isReborn() || (target.isDominated() && target.getDominator() != performer)) {
/*     */           
/*  80 */           performer.getCommunicator().sendNormalServerMessage("You fail to connect with the " + target
/*  81 */               .getName() + ".", (byte)3);
/*     */           
/*  83 */           return false;
/*     */         } 
/*  85 */         return true;
/*     */       } 
/*     */       
/*  88 */       performer.getCommunicator().sendNormalServerMessage("You fail to connect with the " + target.getName() + ".", (byte)3);
/*     */     } 
/*     */     
/*  91 */     return false;
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
/* 102 */     if (power > 0.0D) {
/*     */       
/* 104 */       dominate(power, performer, target);
/*     */     } else {
/*     */       
/* 107 */       performer.getCommunicator().sendNormalServerMessage("You fail to bind " + target.getNameWithGenus() + ".", (byte)3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void dominate(double power, Creature performer, Creature target) {
/* 113 */     if (performer.getPet() != null && performer.getPet() != target) {
/*     */       
/* 115 */       performer.getCommunicator().sendNormalServerMessage(performer.getPet().getNameWithGenus() + " stops following you.", (byte)2);
/*     */       
/* 117 */       if (performer.getPet().getLeader() == performer)
/* 118 */         performer.getPet().setLeader(null); 
/* 119 */       performer.getPet().setDominator(-10L);
/* 120 */       performer.setPet(-10L);
/*     */     } 
/* 122 */     boolean newpet = false;
/* 123 */     if (target.dominator != performer.getWurmId())
/* 124 */       newpet = true; 
/* 125 */     target.setTarget(-10L, true);
/*     */     
/* 127 */     target.stopFighting();
/* 128 */     if (performer.getTarget() == target) {
/*     */       
/* 130 */       performer.setTarget(-10L, true);
/* 131 */       performer.stopFighting();
/*     */     } 
/* 133 */     if (target.opponent == performer)
/* 134 */       target.setOpponent(null); 
/* 135 */     if (performer.opponent == target) {
/* 136 */       performer.setOpponent(null);
/*     */     }
/*     */     try {
/* 139 */       target.setKingdomId(performer.getKingdomId());
/*     */     }
/* 141 */     catch (IOException iox) {
/*     */       
/* 143 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */     } 
/* 145 */     target.setDominator(performer.getWurmId());
/* 146 */     if (newpet) {
/*     */       
/* 148 */       target.setLoyalty((float)Math.max(10.0D, power));
/* 149 */       performer.setPet(target.getWurmId());
/* 150 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " dominates you!");
/* 151 */       performer.getCommunicator().sendNormalServerMessage("You bind " + target.getNameWithGenus() + " with fear for your power.", (byte)2);
/*     */       
/* 153 */       if (target.isUnique())
/*     */       {
/* 155 */         HistoryManager.addHistory(performer.getName(), "dominated " + target.getName());
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 160 */       target.setLoyalty((float)Math.min(99.0D, target.getLoyalty() + power / 10.0D));
/* 161 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " dominates you!");
/* 162 */       performer.getCommunicator().sendNormalServerMessage("You strengthen " + target
/* 163 */           .getNameWithGenus() + "'s fear for your power.", (byte)2);
/*     */     } 
/*     */     
/* 166 */     if (Constants.devmode)
/* 167 */       performer.getCommunicator().sendNormalServerMessage("New loyalty=" + target.getLoyalty()); 
/* 168 */     target.getStatus().setLastPolledLoyalty();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Dominate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */