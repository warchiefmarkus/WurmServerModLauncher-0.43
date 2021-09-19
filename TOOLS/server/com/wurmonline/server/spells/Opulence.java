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
/*    */ public final class Opulence
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Opulence() {
/* 32 */     super("Opulence", 280, 20, 10, 10, 15, 0L);
/* 33 */     this.targetItem = true;
/* 34 */     this.enchantment = 15;
/* 35 */     this.effectdesc = "will feed you more.";
/* 36 */     this.description = "causes a food item to be more effective at filling you up";
/* 37 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 43 */     if (!mayBeEnchanted(target) || !target.isFood()) {
/*    */       
/* 45 */       performer.getCommunicator().sendNormalServerMessage("The spell will not work on that.", (byte)3);
/*    */       
/* 47 */       return false;
/*    */     } 
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Opulence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */