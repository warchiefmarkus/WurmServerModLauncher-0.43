/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.SpellEffects;
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
/*    */ public class RustMonster
/*    */   extends KarmaEnchantment
/*    */ {
/*    */   public static final int RANGE = 24;
/*    */   
/*    */   public RustMonster() {
/* 39 */     super("Rust Monster", 548, 20, 500, 20, 1, 180000L);
/* 40 */     this.targetCreature = true;
/* 41 */     this.enchantment = 70;
/* 42 */     this.effectdesc = "rust effect to enemy weapons when hit.";
/* 43 */     this.description = "damages enemy weapons";
/* 44 */     this.durationModifier = 4.0F;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 51 */     SpellEffects effs = target.getSpellEffects();
/* 52 */     if (effs == null)
/*    */     {
/* 54 */       effs = target.createSpellEffects();
/*    */     }
/* 56 */     SpellEffect eff = effs.getSpellEffect(this.enchantment);
/*    */     
/* 58 */     int duration = (int)(600.0D + 600.0D * power / 100.0D);
/* 59 */     if (eff == null) {
/*    */ 
/*    */       
/* 62 */       if (target != performer) {
/* 63 */         performer.getCommunicator().sendNormalServerMessage(target.getName() + " now has " + this.effectdesc, (byte)2);
/*    */       }
/* 65 */       target.getCommunicator().sendNormalServerMessage("You now have " + this.effectdesc, (byte)2);
/* 66 */       eff = new SpellEffect(target.getWurmId(), this.enchantment, (float)power, duration, (byte)9, (byte)0, true);
/*    */ 
/*    */       
/* 69 */       effs.addSpellEffect(eff);
/* 70 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 76 */     else if (eff.getPower() > power) {
/*    */       
/* 78 */       performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte)3);
/* 79 */       Server.getInstance().broadCastAction(performer.getName() + " frowns.", performer, 5);
/*    */     }
/*    */     else {
/*    */       
/* 83 */       if (target != performer) {
/* 84 */         performer.getCommunicator().sendNormalServerMessage("You succeed in improving the power of the " + this.name + ".", (byte)2);
/*    */       }
/* 86 */       target.getCommunicator().sendNormalServerMessage("You will now receive improved " + this.effectdesc, (byte)2);
/*    */       
/* 88 */       eff.setPower((float)power);
/* 89 */       eff.setTimeleft(Math.max(eff.timeleft, duration));
/* 90 */       target.sendUpdateSpellEffect(eff);
/* 91 */       Server.getInstance().broadCastAction(performer.getName() + " looks pleased.", performer, 5);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RustMonster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */