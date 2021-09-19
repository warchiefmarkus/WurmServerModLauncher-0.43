/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import com.wurmonline.server.steam.SteamId;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
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
/*    */ public class SteamIdHistory
/*    */ {
/*    */   private int id;
/*    */   private long playerId;
/*    */   private SteamId steamId;
/*    */   private long firstUsed;
/*    */   private long lastUsed;
/*    */   
/*    */   public SteamIdHistory(long playerId, SteamId steamId, long firstUsed, long lastUsed) {
/* 25 */     this.playerId = playerId;
/* 26 */     this.steamId = steamId;
/* 27 */     this.firstUsed = firstUsed;
/* 28 */     this.lastUsed = lastUsed;
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
/*    */   public SteamIdHistory(ResultSet rs) throws SQLException {
/* 42 */     this.id = rs.getInt("ID");
/* 43 */     this.playerId = rs.getLong("PLAYER_ID");
/* 44 */     this.steamId = SteamId.fromSteamID64(Long.valueOf(rs.getLong("STEAM_ID")).longValue());
/* 45 */     this.firstUsed = rs.getLong("FIRST_USED");
/* 46 */     this.lastUsed = rs.getLong("LAST_USED");
/*    */   }
/*    */   
/*    */   public int getId() {
/* 50 */     return this.id;
/*    */   }
/*    */   
/*    */   public long getPlayerId() {
/* 54 */     return this.playerId;
/*    */   }
/*    */   
/*    */   public SteamId getSteamId() {
/* 58 */     return this.steamId;
/*    */   }
/*    */   
/*    */   public long getFirstUsed() {
/* 62 */     return this.firstUsed;
/*    */   }
/*    */   
/*    */   public long getLastUsed() {
/* 66 */     return this.lastUsed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLastUsed(long lastUsed) {
/* 71 */     this.lastUsed = lastUsed;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\SteamIdHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */