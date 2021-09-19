/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Logger;
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
/*    */ final class OwnershipPaperBehaviour
/*    */   extends ItemBehaviour
/*    */ {
/* 37 */   private static final Logger logger = Logger.getLogger(OwnershipPaperBehaviour.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   OwnershipPaperBehaviour() {
/* 44 */     super((short)52);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/* 50 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/* 51 */     toReturn.addAll(getBehavioursForPaper());
/* 52 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/* 58 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/* 59 */     toReturn.addAll(getBehavioursForPaper());
/* 60 */     return toReturn;
/*    */   }
/*    */ 
/*    */   
/*    */   List<ActionEntry> getBehavioursForPaper() {
/* 65 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 66 */     toReturn.add(new ActionEntry((short)17, "Read paper", "Reading"));
/* 67 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 73 */     boolean done = true;
/* 74 */     if (action == 1 && target.getTemplateId() == 1000) {
/*    */ 
/*    */ 
/*    */       
/* 78 */       performer.getCommunicator().sendNormalServerMessage("This is the writ of ownership. It can be traded with another player to transfer ownership.");
/*    */ 
/*    */     
/*    */     }
/* 82 */     else if (action != 17) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 91 */       done = super.action(act, performer, target, action, counter);
/* 92 */     }  return done;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\OwnershipPaperBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */