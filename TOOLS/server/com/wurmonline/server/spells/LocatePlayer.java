/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.questions.LocatePlayerQuestion;
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
/*    */ public class LocatePlayer
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 40;
/*    */   
/*    */   public LocatePlayer() {
/* 33 */     super("Locate Soul", 419, 10, 20, 10, 20, 120000L);
/* 34 */     this.targetCreature = true;
/* 35 */     this.targetItem = true;
/* 36 */     this.targetTile = true;
/* 37 */     this.description = "locates a player and corpses";
/* 38 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 46 */     LocatePlayerQuestion lpq = new LocatePlayerQuestion(performer, "Locate a soul", "Which soul do you wish to locate?", performer.getWurmId(), false, power);
/* 47 */     lpq.sendQuestion();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/* 54 */     LocatePlayerQuestion lpq = new LocatePlayerQuestion(performer, "Locate a soul", "Which soul do you wish to locate?", performer.getWurmId(), false, power);
/* 55 */     lpq.sendQuestion();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 62 */     LocatePlayerQuestion lpq = new LocatePlayerQuestion(performer, "Locate a soul", "Which soul do you wish to locate?", performer.getWurmId(), false, power);
/* 63 */     lpq.sendQuestion();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LocatePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */