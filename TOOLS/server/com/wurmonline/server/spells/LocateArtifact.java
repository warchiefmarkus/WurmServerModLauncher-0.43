/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.endgames.EndGameItems;
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
/*    */ public final class LocateArtifact
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 4;
/*    */   
/*    */   LocateArtifact() {
/* 35 */     super("Locate Artifact", 271, 30, 70, 70, 80, 1800000L);
/* 36 */     this.targetTile = true;
/* 37 */     this.description = "locates hidden artifacts";
/* 38 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/* 44 */     if (performer.getPower() > 0 && performer.getPower() < 5) {
/*    */       
/* 46 */       performer.getCommunicator().sendNormalServerMessage("You may not cast this spell.", (byte)3);
/* 47 */       return false;
/*    */     } 
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 56 */     performer.getCommunicator().sendNormalServerMessage(EndGameItems.locateRandomEndGameItem(performer));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\LocateArtifact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */