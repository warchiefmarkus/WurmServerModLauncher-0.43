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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DemiseMonster
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   DemiseMonster() {
/* 43 */     super("Monster Demise", 268, 30, 40, 40, 41, 0L);
/* 44 */     this.targetWeapon = true;
/* 45 */     this.enchantment = 10;
/* 46 */     this.effectdesc = "will deal increased damage to monstrous creatures.";
/* 47 */     this.description = "increases damage dealt to monstrous creatures";
/* 48 */     this.singleItemEnchant = true;
/* 49 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 60 */     return EnchantUtil.canEnchantDemise(performer, target);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 81 */     target.enchant((byte)10);
/* 82 */     performer.getCommunicator().sendNormalServerMessage("The " + target
/* 83 */         .getName() + " will now be effective against monsters.", (byte)2);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DemiseMonster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */