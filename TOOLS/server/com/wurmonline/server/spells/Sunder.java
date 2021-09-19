/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.behaviours.Methods;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.NoSuchCreatureException;
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
/*    */ public final class Sunder
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   Sunder() {
/* 41 */     super("Sunder", 253, 30, 50, 30, 60, 0L);
/* 42 */     this.targetItem = true;
/* 43 */     this.description = "deal damage to item";
/* 44 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 51 */     if (performer.mayDestroy(target) && Methods.isActionAllowed(performer, (short)83, target))
/*    */     {
/* 53 */       return true;
/*    */     }
/* 55 */     if (!mayBeEnchanted(target)) {
/*    */       
/* 57 */       performer.getCommunicator().sendNormalServerMessage("Your spell will not work on that.", (byte)3);
/*    */       
/* 59 */       return false;
/*    */     } 
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*    */     try {
/* 69 */       if (target.getOwner() != -10L)
/*    */       {
/* 71 */         Creature owner = Server.getInstance().getCreature(target.getOwner());
/* 72 */         if (!performer.equals(owner)) {
/* 73 */           owner.getCommunicator().sendNormalServerMessage(performer.getName() + " damages your " + target.getName() + ".", (byte)4);
/*    */         }
/*    */       }
/*    */     
/* 77 */     } catch (NoSuchCreatureException|com.wurmonline.server.NoSuchPlayerException|com.wurmonline.server.items.NotOwnedException noSuchCreatureException) {}
/*    */ 
/*    */ 
/*    */     
/* 81 */     float qlMod = 1.0F - target.getQualityLevel() / 200.0F;
/* 82 */     float damMod = 1.0F - target.getDamage() / 100.0F;
/*    */ 
/*    */     
/* 85 */     float weightMod = Math.min(1.0F, 5000.0F / target.getWeightGrams());
/* 86 */     float sunderDamage = (float)(power / 5.0D) * qlMod * damMod * weightMod;
/* 87 */     target.setDamage(target.getDamage() + sunderDamage);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Sunder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */