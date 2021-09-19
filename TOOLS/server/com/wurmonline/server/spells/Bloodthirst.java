/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.items.ItemSpellEffects;
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
/*    */ public class Bloodthirst
/*    */   extends ItemEnchantment
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public Bloodthirst() {
/* 42 */     super("Bloodthirst", 454, 20, 50, 60, 31, 0L);
/* 43 */     this.targetWeapon = true;
/* 44 */     this.enchantment = 45;
/* 45 */     this.effectdesc = "will do more damage the more you hit creatures or players.";
/* 46 */     this.description = "increases damage dealt with weapons, improves power with usage";
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
/* 57 */     ItemSpellEffects effs = target.getSpellEffects();
/* 58 */     if (effs == null)
/* 59 */       effs = new ItemSpellEffects(target.getWurmId()); 
/* 60 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/* 61 */     if (eff == null) {
/*    */       
/* 63 */       performer.getCommunicator().sendNormalServerMessage("The " + target
/* 64 */           .getName() + " will now do more and more damage when you hit creatures or other players.", (byte)2);
/*    */       
/* 66 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, 20000000);
/* 67 */       effs.addSpellEffect(eff);
/* 68 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */ 
/*    */     
/*    */     }
/* 72 */     else if ((eff.getPower() / 10.0F) > power) {
/*    */       
/* 74 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/*    */       
/* 76 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*    */     }
/*    */     else {
/*    */       
/* 80 */       performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*    */       
/* 82 */       eff.setPower(eff.getPower() + (float)Math.min((float)power, (10000.0F - eff.getPower()) * 0.1D));
/*    */       
/* 84 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Bloodthirst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */