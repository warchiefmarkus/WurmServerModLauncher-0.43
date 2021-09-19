/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.shared.constants.AttitudeConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RottingGut
/*    */   extends DamageSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   public static final double BASE_DAMAGE = 7000.0D;
/*    */   public static final double DAMAGE_PER_POWER = 100.0D;
/*    */   
/*    */   public RottingGut() {
/* 39 */     super("Rotting Gut", 428, 7, 10, 10, 35, 30000L);
/* 40 */     this.targetCreature = true;
/* 41 */     this.offensive = true;
/* 42 */     this.description = "damages the targets stomach with rotting acid";
/* 43 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 54 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 56 */       if (performer.faithful)
/*    */       {
/* 58 */         if (!performer.isDuelOrSpar(target)) {
/*    */           
/* 60 */           performer.getCommunicator().sendNormalServerMessage(performer
/* 61 */               .getDeity().getName() + " would never accept your attack on " + target.getName() + ".", (byte)3);
/*    */           
/* 63 */           return false;
/*    */         } 
/*    */       }
/*    */     }
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 73 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 75 */       if (!performer.isDuelOrSpar(target))
/*    */       {
/* 77 */         performer.modifyFaith(-5.0F);
/*    */       }
/*    */     }
/*    */     
/* 81 */     double damage = calculateDamage(target, power, 7000.0D, 100.0D);
/*    */     
/* 83 */     target.addWoundOfType(performer, (byte)10, 23, false, 1.0F, false, damage, (float)power, 0.0F, false, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RottingGut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */