/*    */ package com.wurmonline.server.highways;
/*    */ 
/*    */ import com.wurmonline.server.Items;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.players.Player;
/*    */ import com.wurmonline.server.zones.VirtualZone;
/*    */ import com.wurmonline.server.zones.VolaTile;
/*    */ import com.wurmonline.server.zones.Zones;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerMessageToSend
/*    */ {
/* 40 */   private static final Logger logger = Logger.getLogger(PlayerMessageToSend.class.getName());
/*    */   
/*    */   private final Player player;
/*    */   
/*    */   private final String text;
/*    */   
/*    */   PlayerMessageToSend(Player player, String text) {
/* 47 */     this.player = player;
/* 48 */     this.text = text;
/*    */   }
/*    */ 
/*    */   
/*    */   void send() {
/* 53 */     this.player.getCommunicator().sendNormalServerMessage(this.text);
/*    */ 
/*    */     
/* 56 */     for (Item waystone : Items.getWaystones()) {
/*    */       
/* 58 */       VolaTile vt = Zones.getTileOrNull(waystone.getTileX(), waystone.getTileY(), waystone.isOnSurface());
/*    */       
/* 60 */       if (vt != null)
/*    */       {
/* 62 */         for (VirtualZone vz : vt.getWatchers()) {
/*    */ 
/*    */           
/*    */           try {
/* 66 */             if (vz.getWatcher().getWurmId() == this.player.getWurmId()) {
/*    */               
/* 68 */               this.player.getCommunicator().sendWaystoneData(waystone);
/*    */               
/*    */               break;
/*    */             } 
/* 72 */           } catch (Exception e) {
/*    */             
/* 74 */             logger.log(Level.WARNING, e.getMessage(), e);
/*    */           } 
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\PlayerMessageToSend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */