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
/*    */ public class ItemEnchantment
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   ItemEnchantment(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long aCooldown) {
/* 32 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, aCooldown);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 44 */     if (!mayBeEnchanted(target)) {
/*    */       
/* 46 */       EnchantUtil.sendCannotBeEnchantedMessage(performer);
/* 47 */       return false;
/*    */     } 
/*    */ 
/*    */     
/* 51 */     SpellEffect negatingEffect = EnchantUtil.hasNegatingEffect(target, getEnchantment());
/* 52 */     if (negatingEffect != null) {
/*    */       
/* 54 */       EnchantUtil.sendNegatingEffectMessage(getName(), performer, target, negatingEffect);
/* 55 */       return false;
/*    */     } 
/* 57 */     return true;
/*    */   }
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
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 72 */     return false;
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
/* 83 */     enchantItem(performer, target, getEnchantment(), (float)power);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doNegativeEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 94 */     checkDestroyItem(power, performer, target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ItemEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */