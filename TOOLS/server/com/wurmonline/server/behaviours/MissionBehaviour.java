/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.tutorial.MissionPerformed;
/*    */ import com.wurmonline.server.tutorial.MissionPerformer;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class MissionBehaviour
/*    */   extends Behaviour
/*    */ {
/*    */   public MissionBehaviour() {
/* 37 */     super((short)43);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object, int missionId) {
/* 48 */     return getBehavioursFor(performer, missionId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, int missionId) {
/* 59 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 60 */     toReturn.add(Actions.actionEntrys[1]);
/* 61 */     toReturn.add(Actions.actionEntrys[16]);
/* 62 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, int missionId, short action, float counter) {
/* 73 */     if (action == 1)
/*    */     {
/* 75 */       performer.getCommunicator().sendNormalServerMessage("This displays the state of a mission.");
/*    */     }
/* 77 */     if (action == 16) {
/*    */       
/* 79 */       MissionPerformer mp = MissionPerformed.getMissionPerformer(performer
/* 80 */           .getWurmId());
/* 81 */       MissionPerformed mpf = mp.getMission(missionId);
/* 82 */       mpf.setInactive(true);
/*    */     } 
/* 84 */     return true;
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
/*    */   public boolean action(Action act, Creature performer, Item source, int missionId, short action, float counter) {
/* 96 */     return action(act, performer, missionId, action, counter);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MissionBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */