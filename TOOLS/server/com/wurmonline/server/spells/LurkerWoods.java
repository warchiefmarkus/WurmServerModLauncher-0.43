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
/*    */ public class LurkerWoods
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public LurkerWoods() {
/* 36 */     super("Lurker in the Woods", 458, 20, 30, 60, 31, 0L);
/* 37 */     this.targetPendulum = true;
/* 38 */     this.enchantment = 49;
/* 39 */     this.effectdesc = "will locate rare creatures.";
/* 40 */     this.description = "locates rare creatures";
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
/* 51 */     if (target.getTemplateId() != 233) {
/*    */       
/* 53 */       performer.getCommunicator().sendNormalServerMessage("This would work well on a pendulum.", (byte)3);
/* 54 */       return false;
/*    */     } 
/* 56 */     return mayBeEnchanted(target);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LurkerWoods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */