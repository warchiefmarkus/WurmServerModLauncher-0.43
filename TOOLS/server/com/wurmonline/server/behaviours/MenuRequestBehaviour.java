/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ final class MenuRequestBehaviour
/*    */   extends Behaviour
/*    */ {
/* 35 */   private static final Logger logger = Logger.getLogger(MenuRequestBehaviour.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   MenuRequestBehaviour() {
/* 42 */     super((short)53);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, int menuId) {
/* 53 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 54 */     if (menuId == 0)
/* 55 */       toReturn.addAll(ManageMenu.getBehavioursFor(performer)); 
/* 56 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, int menuId, short action, float counter) {
/* 67 */     boolean done = true;
/* 68 */     if (menuId == 0 && ManageMenu.isManageAction(performer, action))
/* 69 */       return ManageMenu.action(act, performer, action, counter); 
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\MenuRequestBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */