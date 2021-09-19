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
/*    */ public class LurkerDeep
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public LurkerDeep() {
/* 41 */     super("Lurker in the Deep", 457, 20, 30, 60, 31, 0L);
/* 42 */     this.targetPendulum = true;
/* 43 */     this.enchantment = 48;
/* 44 */     this.effectdesc = "will locate rare fish.";
/* 45 */     this.description = "locates rare fish";
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
/* 56 */     if (target.getTemplateId() != 233) {
/*    */       
/* 58 */       performer.getCommunicator().sendNormalServerMessage("This would work well on a pendulum.", (byte)3);
/* 59 */       return false;
/*    */     } 
/* 61 */     return mayBeEnchanted(target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LurkerDeep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */