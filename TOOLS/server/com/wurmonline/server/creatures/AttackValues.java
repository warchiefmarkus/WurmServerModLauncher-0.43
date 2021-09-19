/*    */ package com.wurmonline.server.creatures;
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
/*    */ public final class AttackValues
/*    */ {
/*    */   private final float baseDamage;
/*    */   private final int attackReach;
/*    */   private final int weightGroup;
/*    */   private final float criticalChance;
/*    */   private final float baseSpeed;
/*    */   private final byte damageType;
/*    */   private final boolean usesWeapon;
/*    */   private final int rounds;
/*    */   private final float waitUntilNextAttack;
/*    */   
/*    */   public AttackValues(float baseDamage, float criticalChance, float baseSpeed, int attackReach, int weightGroup, byte damageType, boolean usesWeapon, int rounds, float waitUntilNextAttack) {
/* 40 */     this.baseDamage = baseDamage;
/* 41 */     this.criticalChance = criticalChance;
/* 42 */     this.baseSpeed = baseSpeed;
/* 43 */     this.attackReach = attackReach;
/* 44 */     this.weightGroup = weightGroup;
/* 45 */     this.damageType = damageType;
/* 46 */     this.usesWeapon = usesWeapon;
/* 47 */     this.rounds = rounds;
/* 48 */     this.waitUntilNextAttack = waitUntilNextAttack;
/*    */   }
/*    */ 
/*    */   
/*    */   public final float getBaseDamage() {
/* 53 */     return this.baseDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public final float getCriticalChance() {
/* 58 */     return this.criticalChance;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getRounds() {
/* 63 */     return this.rounds;
/*    */   }
/*    */ 
/*    */   
/*    */   public final float getBaseSpeed() {
/* 68 */     return this.baseSpeed;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getAttackReach() {
/* 73 */     return this.attackReach;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getWeightGroup() {
/* 78 */     return this.weightGroup;
/*    */   }
/*    */ 
/*    */   
/*    */   public final byte getDamageType() {
/* 83 */     return this.damageType;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isUsingWeapon() {
/* 88 */     return this.usesWeapon;
/*    */   }
/*    */ 
/*    */   
/*    */   public final float getWaitTime() {
/* 93 */     return this.waitUntilNextAttack;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\AttackValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */