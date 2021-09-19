/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.items.ItemFactory;
/*    */ import com.wurmonline.server.items.NoSuchTemplateException;
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
/*    */ public class LightToken
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   public LightToken() {
/* 37 */     super("Light Token", 421, 10, 5, 10, 20, 0L);
/* 38 */     this.targetItem = true;
/* 39 */     this.targetTile = true;
/* 40 */     this.targetCreature = true;
/* 41 */     this.description = "creates a bright light item";
/* 42 */     this.type = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 49 */     createToken(performer, power);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 55 */     createToken(performer, power);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 61 */     createToken(performer, power);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void createToken(Creature performer, double power) {
/*    */     try {
/* 68 */       Item token = ItemFactory.createItem(649, (float)Math.max(50.0D, power), performer.getName());
/* 69 */       performer.getInventory().insertItem(token);
/* 70 */       performer.getCommunicator().sendNormalServerMessage("Something starts shining in your pocket.", (byte)2);
/*    */     
/*    */     }
/* 73 */     catch (NoSuchTemplateException|com.wurmonline.server.FailedException noSuchTemplateException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LightToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */