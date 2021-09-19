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
/*    */ public final class DemiseAnimal
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   DemiseAnimal() {
/* 43 */     super("Animal Demise", 269, 30, 40, 50, 43, 0L);
/* 44 */     this.targetWeapon = true;
/* 45 */     this.enchantment = 11;
/* 46 */     this.effectdesc = "will deal increased damage to animals.";
/* 47 */     this.description = "increases damage dealt to animals";
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
/* 81 */     target.enchant((byte)11);
/* 82 */     performer.getCommunicator().sendNormalServerMessage("The " + target
/* 83 */         .getName() + " will now be effective against animals.", (byte)2);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DemiseAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */