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
/*    */ public class Hypothermia
/*    */   extends DamageSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   public static final double BASE_DAMAGE = 10000.0D;
/*    */   public static final double DAMAGE_PER_POWER = 250.0D;
/*    */   
/*    */   public Hypothermia() {
/* 40 */     super("Hypothermia", 932, 15, 50, 30, 70, 60000L);
/* 41 */     this.targetCreature = true;
/* 42 */     this.offensive = true;
/* 43 */     this.description = "damages the target with extreme frost";
/* 44 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 50 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 52 */       if (performer.faithful) {
/*    */         
/* 54 */         performer.getCommunicator().sendNormalServerMessage(performer
/* 55 */             .getDeity().getName() + " would never accept your spell on " + target.getName() + ".", (byte)3);
/*    */         
/* 57 */         return false;
/*    */       } 
/*    */     }
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 66 */     if ((target.isHuman() || target.isDominated()) && target.getAttitude(performer) != 2)
/*    */     {
/* 68 */       performer.modifyFaith(-5.0F);
/*    */     }
/*    */     
/* 71 */     double damage = calculateDamage(target, power, 10000.0D, 250.0D);
/*    */     
/* 73 */     target.addWoundOfType(performer, (byte)8, 1, true, 1.0F, false, damage, 0.0F, 0.0F, false, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Hypothermia.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */