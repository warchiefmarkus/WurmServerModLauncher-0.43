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
/*    */ public class LurkerDark
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public LurkerDark() {
/* 41 */     super("Lurker in the Dark", 459, 20, 30, 60, 31, 0L);
/* 42 */     this.targetPendulum = true;
/* 43 */     this.enchantment = 50;
/* 44 */     this.effectdesc = "will locate enemies.";
/* 45 */     this.description = "locates enemies";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 51 */     if (target.getTemplateId() != 233) {
/*    */       
/* 53 */       performer.getCommunicator().sendNormalServerMessage("This would work well on a pendulum.", (byte)3);
/* 54 */       return false;
/*    */     } 
/* 56 */     return mayBeEnchanted(target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LurkerDark.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */