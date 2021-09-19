/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ public class DamageSpell
/*    */   extends ReligiousSpell
/*    */ {
/*    */   DamageSpell(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long aCooldown) {
/* 27 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, aCooldown);
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
/*    */   public double calculateDamage(Creature target, double power, double baseDamage, double damagePerPower) {
/* 42 */     double damage = power * damagePerPower;
/* 43 */     damage += baseDamage;
/* 44 */     double resistance = SpellResist.getSpellResistance(target, getNumber());
/* 45 */     damage *= resistance;
/*    */     
/* 47 */     SpellResist.addSpellResistance(target, getNumber(), damage);
/*    */     
/* 49 */     return Spell.modifyDamage(target, damage);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\DamageSpell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */