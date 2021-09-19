/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.MiscConstants;
/*    */ import com.wurmonline.server.Players;
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WurmPermissions
/*    */   implements MiscConstants
/*    */ {
/*    */   public static boolean mayCreateItems(Creature performer) {
/* 42 */     return (Servers.isThisATestServer() || performer
/* 43 */       .getPower() >= 2 || Players.isArtist(performer.getWurmId(), false, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean mayChangeTile(Creature performer) {
/* 48 */     return (performer.getPower() >= 3 || Players.isArtist(performer.getWurmId(), false, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean mayUseDeityWand(Creature performer) {
/* 53 */     return (performer.getPower() >= 2 || Players.isArtist(performer.getWurmId(), false, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean mayUseGMWand(Creature performer) {
/* 58 */     return (performer.getPower() >= 2 || Players.isArtist(performer.getWurmId(), false, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean maySetFaith(Creature performer) {
/* 63 */     return (performer.getPower() >= 3 || (
/* 64 */       Servers.isThisATestServer() && Players.isArtist(performer.getWurmId(), false, false)));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WurmPermissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */