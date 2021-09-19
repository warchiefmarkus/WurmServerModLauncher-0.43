/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import com.wurmonline.server.steam.SteamId;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SteamIdBan
/*    */   implements Ban
/*    */ {
/*    */   private SteamId identifier;
/*    */   private String reason;
/*    */   private long expiry;
/*    */   private static final String ADD_BANNED_STEAMID = "insert into BANNED_STEAM_IDS (STEAM_ID,BANREASON,BANEXPIRY) values(?,?,?)";
/*    */   private static final String UPDATE_BANNED_STEAMID = "UPDATE BANNED_STEAM_IDS SET BANREASON=?,BANEXPIRY=? WHERE STEAM_ID=?";
/*    */   private static final String GET_BANNED_STEAMIDS = "select * from BANNED_STEAM_IDS";
/*    */   private static final String REMOVE_BANNED_STEAMID = "delete from BANNED_STEAM_IDS where STEAM_ID=?";
/*    */   
/*    */   public SteamIdBan(SteamId identifier, String reason, long expiry) {
/* 20 */     this.identifier = identifier;
/* 21 */     this.reason = reason;
/* 22 */     this.expiry = expiry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isExpired() {
/* 28 */     return (System.currentTimeMillis() > getExpiry());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getIdentifier() {
/* 34 */     return this.identifier.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getReason() {
/* 40 */     return this.reason;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReason(String reason) {
/* 46 */     this.reason = reason;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getExpiry() {
/* 52 */     return this.expiry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpiry(long expiry) {
/* 58 */     this.expiry = expiry;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdateSql() {
/* 63 */     return "UPDATE BANNED_STEAM_IDS SET BANREASON=?,BANEXPIRY=? WHERE STEAM_ID=?";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 68 */     return "insert into BANNED_STEAM_IDS (STEAM_ID,BANREASON,BANEXPIRY) values(?,?,?)";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDeleteSql() {
/* 73 */     return "delete from BANNED_STEAM_IDS where STEAM_ID=?";
/*    */   }
/*    */   
/*    */   public static String getSelectSql() {
/* 77 */     return "select * from BANNED_STEAM_IDS";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\SteamIdBan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */