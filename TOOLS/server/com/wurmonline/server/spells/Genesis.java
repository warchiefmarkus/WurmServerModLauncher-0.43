/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
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
/*    */ 
/*    */ public class Genesis
/*    */   extends DamageSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   public static final double BASE_DAMAGE = 32767.5D;
/*    */   public static final double DAMAGE_PER_POWER = 491.5125D;
/*    */   
/*    */   public Genesis() {
/* 40 */     super("Genesis", 408, 10, 30, 40, 70, 30000L);
/* 41 */     this.targetCreature = true;
/* 42 */     this.description = "cleanses a creature of a single negative trait";
/* 43 */     this.type = 1;
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
/* 54 */     if (target != performer)
/*    */     {
/* 56 */       return (performer.getDeity() != null);
/*    */     }
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 70 */     if (target.isReborn()) {
/*    */       
/* 72 */       performer.getCommunicator().sendNormalServerMessage("You rid the " + target.getName() + " of its evil spirit and it collapses.");
/* 73 */       double damage = calculateDamage(target, power, 32767.5D, 491.5125D);
/* 74 */       target.addWoundOfType(performer, (byte)9, 2, false, 1.0F, false, damage, 0.0F, 0.0F, false, true);
/*    */ 
/*    */     
/*    */     }
/* 78 */     else if (target.removeRandomNegativeTrait()) {
/* 79 */       performer.getCommunicator().sendNormalServerMessage("You rid the " + target.getName() + " of an evil spirit. It will now produce healthier offspring.");
/*    */     } else {
/* 81 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " was not possessed by any evil spirit.", (byte)3);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Genesis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */