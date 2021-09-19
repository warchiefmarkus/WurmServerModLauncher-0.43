/*    */ package com.wurmonline.server.creatures;
/*    */ 
/*    */ import java.util.logging.Level;
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
/*    */ final class CreatureStatusFactory
/*    */ {
/* 28 */   private static final Logger logger = Logger.getLogger(CreatureStatusFactory.class.getName());
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
/*    */   static CreatureStatus createCreatureStatus(Creature creature, float posx, float posy, float rot, int layer) throws Exception {
/* 40 */     CreatureStatus toReturn = null;
/*    */ 
/*    */     
/* 43 */     toReturn = new DbCreatureStatus(creature, posx, posy, rot, layer);
/*    */ 
/*    */     
/* 46 */     if (logger.isLoggable(Level.FINEST))
/*    */     {
/* 48 */       logger.finest("Created new CreatureStatus: " + toReturn);
/*    */     }
/* 50 */     return toReturn;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureStatusFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */