/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.shared.constants.AttitudeConstants;
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
/*    */ public final class Refresh
/*    */   extends ReligiousSpell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public static final int RANGE = 12;
/*    */   
/*    */   Refresh() {
/* 39 */     super("Refresh", 250, 9, 15, 20, 20, 180000L);
/* 40 */     this.targetCreature = true;
/* 41 */     this.description = "refreshes stamina of a single creature";
/* 42 */     this.type = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, Creature target) {
/* 53 */     if (target.getAttitude(performer) != 2)
/*    */     {
/* 55 */       return true;
/*    */     }
/* 57 */     performer.getCommunicator().sendNormalServerMessage(performer
/* 58 */         .getDeity().getName() + " would never help the infidel " + target.getName() + ".", (byte)3);
/*    */     
/* 60 */     return false;
/*    */   }
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
/*    */   void doEffect(Skill castSkill, double power, Creature performer, Creature target) {
/* 86 */     target.getStatus().modifyStamina2(100.0F);
/* 87 */     if (performer != target) {
/*    */       
/* 89 */       performer.getCommunicator().sendNormalServerMessage("You refresh " + target.getName() + ".", (byte)2);
/*    */ 
/*    */       
/* 92 */       if (performer != target) {
/* 93 */         target.getCommunicator().sendNormalServerMessage(performer.getName() + " refreshens you.", (byte)2);
/*    */       }
/*    */     } else {
/*    */       
/* 97 */       performer.getCommunicator().sendNormalServerMessage("You refresh yourself.", (byte)2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Refresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */