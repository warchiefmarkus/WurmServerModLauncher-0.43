/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ public class OakShell
/*    */   extends CreatureEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public OakShell() {
/* 32 */     super("Oakshell", 404, 10, 20, 19, 35, 30000L);
/* 33 */     this.enchantment = 22;
/* 34 */     this.effectdesc = "increased natural armour.";
/* 35 */     this.description = "increases the natural armour of a creature or player, does not stack with armour";
/* 36 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 42 */     if (super.precondition(castSkill, performer, target)) {
/*    */       
/* 44 */       if (Servers.isThisAPvpServer() && !target.isPlayer()) {
/*    */         
/* 46 */         performer.getCommunicator().sendNormalServerMessage("You cannot cast " + getName() + " on " + target.getNameWithGenus());
/* 47 */         return false;
/*    */       } 
/*    */     } else {
/*    */       
/* 51 */       return false;
/*    */     } 
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\OakShell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */