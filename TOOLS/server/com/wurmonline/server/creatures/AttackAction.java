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
/*    */ public final class AttackAction
/*    */ {
/*    */   private final String name;
/*    */   private final AttackIdentifier identifier;
/*    */   private final AttackValues attackValues;
/*    */   
/*    */   public AttackAction(String name, AttackIdentifier identifier, AttackValues attackValues) {
/* 34 */     this.name = name;
/* 35 */     this.identifier = identifier;
/* 36 */     this.attackValues = attackValues;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public final AttackIdentifier getAttackIdentifier() {
/* 46 */     return this.identifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isUsingWeapon() {
/* 51 */     return this.attackValues.isUsingWeapon();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final AttackValues getAttackValues() {
/* 62 */     return this.attackValues;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\AttackAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */