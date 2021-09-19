/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ public class WisdomVynora
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 12;
/*     */   
/*     */   public WisdomVynora() {
/*  35 */     super("Wisdom of Vynora", 445, 30, 30, 50, 30, 1800000L);
/*  36 */     this.targetCreature = true;
/*  37 */     this.description = "transfers fatigue to sleep bonus";
/*  38 */     this.type = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/*  44 */     if (target.isReborn())
/*  45 */       return true; 
/*  46 */     if (target.getFatigueLeft() < 100) {
/*     */ 
/*     */       
/*  49 */       performer.getCommunicator().sendNormalServerMessage(target.getName() + " has almost no fatigue left.", (byte)3);
/*     */       
/*  51 */       return false;
/*     */     } 
/*  53 */     if (!target.equals(performer)) {
/*     */       
/*  55 */       if (performer.getDeity() != null) {
/*     */         
/*  57 */         if (target.getDeity() != null) {
/*     */           
/*  59 */           if (target.getDeity().isHateGod()) {
/*     */ 
/*     */             
/*  62 */             if (performer.isFaithful()) {
/*     */ 
/*     */               
/*  65 */               performer.getCommunicator().sendNormalServerMessage(performer.getDeity().getName() + " would never help the infidel " + target
/*  66 */                   .getName() + "!", (byte)3);
/*     */               
/*  68 */               return false;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/*  73 */             return true;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  79 */           return true;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  85 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  91 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 104 */     double toconvert = power;
/* 105 */     toconvert = Math.max(20.0D, power);
/* 106 */     toconvert = Math.min(99.0D, toconvert + (performer.getNumLinks() * 10));
/*     */     
/* 108 */     toconvert /= 100.0D;
/*     */ 
/*     */     
/* 111 */     int numsecondsToMove = Math.min((int)((target.getFatigueLeft() / 12.0F) * toconvert), 3600);
/* 112 */     target.setFatigue(-numsecondsToMove);
/*     */ 
/*     */     
/* 115 */     numsecondsToMove = (int)(numsecondsToMove * 0.2F);
/* 116 */     if (target.isPlayer())
/*     */     {
/* 118 */       ((Player)target).getSaveFile().addToSleep(numsecondsToMove);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WisdomVynora.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */