/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
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
/*    */ final class ToyBehaviour
/*    */   extends ItemBehaviour
/*    */ {
/*    */   ToyBehaviour() {
/* 35 */     super((short)26);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
/* 41 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 42 */     if (object.getTemplateId() == 271)
/*    */     {
/* 44 */       toReturn.add(Actions.actionEntrys[190]);
/*    */     }
/* 46 */     toReturn.addAll(super.getBehavioursFor(performer, object));
/* 47 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
/* 53 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 54 */     if (object.getTemplateId() == 271)
/*    */     {
/* 56 */       toReturn.add(Actions.actionEntrys[190]);
/*    */     }
/* 58 */     toReturn.addAll(super.getBehavioursFor(performer, source, object));
/* 59 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 65 */     boolean toReturn = true;
/* 66 */     if (action == 190) {
/*    */       
/* 68 */       if (target.getTemplateId() == 271)
/*    */       {
/* 70 */         toReturn = MethodsItems.yoyo(performer, target, counter, act);
/*    */       }
/*    */     } else {
/*    */       
/* 74 */       toReturn = super.action(act, performer, target, action, counter);
/* 75 */     }  return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 82 */     boolean toReturn = false;
/* 83 */     if (target.getTemplateId() == 271 && action == 190) {
/* 84 */       toReturn = action(act, performer, target, action, counter);
/*    */     } else {
/* 86 */       toReturn = super.action(act, performer, source, target, action, counter);
/* 87 */     }  return toReturn;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ToyBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */