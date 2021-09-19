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
/*    */ 
/*    */ 
/*    */ public class WormBrains
/*    */   extends DamageSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   public static final double BASE_DAMAGE = 17500.0D;
/*    */   public static final double DAMAGE_PER_POWER = 120.0D;
/*    */   
/*    */   public WormBrains() {
/* 41 */     super("Worm Brains", 430, 15, 40, 40, 51, 60000L);
/* 42 */     this.targetCreature = true;
/* 43 */     this.offensive = true;
/* 44 */     this.description = "damages the target severely with internal damage";
/* 45 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 51 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 53 */       if (performer.faithful) {
/*    */         
/* 55 */         performer.getCommunicator().sendNormalServerMessage(performer
/* 56 */             .getDeity().getName() + " would never accept your spell on " + target.getName() + ".", (byte)3);
/*    */         
/* 58 */         return false;
/*    */       } 
/*    */     }
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 67 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 69 */       performer.modifyFaith(-5.0F);
/*    */     }
/*    */     
/* 72 */     double damage = calculateDamage(target, power, 17500.0D, 120.0D);
/*    */     
/* 74 */     target.addWoundOfType(performer, (byte)9, 1, false, 1.0F, false, damage, 0.0F, 0.0F, false, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\WormBrains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */