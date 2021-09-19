/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.mesh.Tiles;
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.questions.SummonSoulQuestion;
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
/*    */ public class SummonSoul
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 40;
/*    */   
/*    */   public SummonSoul() {
/* 35 */     super("Summon Soul", 934, 30, 100, 10, 80, 0L);
/* 36 */     this.targetCreature = true;
/* 37 */     this.targetItem = true;
/* 38 */     this.targetTile = true;
/* 39 */     this.description = "summons a willing player to your location";
/* 40 */     this.type = 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean mayCastSummonSoul(Creature performer) {
/* 45 */     if (Servers.isThisAPvpServer() && performer.getEnemyPresense() > 0) {
/*    */       
/* 47 */       performer.getCommunicator().sendNormalServerMessage("Enemies are nearby, you cannot cast Summon Soul right now.");
/* 48 */       return false;
/*    */     } 
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/* 56 */     return mayCastSummonSoul(performer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer, int heightOffset, Tiles.TileBorderDirection dir) {
/* 62 */     return mayCastSummonSoul(performer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 68 */     return mayCastSummonSoul(performer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/* 74 */     return mayCastSummonSoul(performer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 80 */     SummonSoulQuestion ssq = new SummonSoulQuestion(performer, "Summon Soul", "Which soul do you wish to summon?", performer.getWurmId());
/* 81 */     ssq.sendQuestion();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 87 */     SummonSoulQuestion ssq = new SummonSoulQuestion(performer, "Summon Soul", "Which soul do you wish to summon?", performer.getWurmId());
/* 88 */     ssq.sendQuestion();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 94 */     SummonSoulQuestion ssq = new SummonSoulQuestion(performer, "Summon Soul", "Which soul do you wish to summon?", performer.getWurmId());
/* 95 */     ssq.sendQuestion();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SummonSoul.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */