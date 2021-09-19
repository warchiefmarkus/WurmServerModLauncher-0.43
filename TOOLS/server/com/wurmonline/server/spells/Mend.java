/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
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
/*    */ public final class Mend
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Mend() {
/* 36 */     super("Mend", 251, 20, 29, 20, 29, 0L);
/* 37 */     this.targetItem = true;
/* 38 */     this.description = "removes damage from an item at the cost of some quality";
/* 39 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 45 */     if (!mayBeEnchanted(target) || target.isFood()) {
/*    */       
/* 47 */       performer.getCommunicator().sendNormalServerMessage("You cannot mend that.", (byte)3);
/* 48 */       return false;
/*    */     } 
/* 50 */     if (target.getDamage() <= 0.0F) {
/*    */       
/* 52 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " is not damaged.", (byte)3);
/*    */       
/* 54 */       return false;
/*    */     } 
/* 56 */     if (target.getQualityLevel() <= 2.0F) {
/*    */       
/* 58 */       performer.getCommunicator().sendNormalServerMessage("The " + target.getName() + " would break under the power of the spell.", (byte)3);
/*    */       
/* 60 */       return false;
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 68 */     float oldDamage = target.getDamage();
/* 69 */     float qlReduction = 2.0F;
/* 70 */     if (oldDamage < 20.0F)
/* 71 */       qlReduction *= oldDamage / 20.0F; 
/* 72 */     target.setDamage(Math.max(oldDamage - 20.0F, 0.0F));
/* 73 */     target.setQualityLevel(Math.max(target.getQualityLevel() - qlReduction, 0.0F));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 79 */     if (power < -80.0D) {
/*    */       
/* 81 */       performer.getCommunicator().sendNormalServerMessage("You fail miserably and the spell has the opposite effect.", (byte)3);
/*    */       
/* 83 */       target.setDamage(target.getDamage() + 1.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Mend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */