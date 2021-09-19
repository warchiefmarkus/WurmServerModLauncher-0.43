/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ import com.wurmonline.server.steam.SteamId;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Ban
/*    */ {
/*    */   boolean isExpired();
/*    */   
/*    */   String getIdentifier();
/*    */   
/*    */   String getReason();
/*    */   
/*    */   void setReason(String paramString);
/*    */   
/*    */   long getExpiry();
/*    */   
/*    */   void setExpiry(long paramLong);
/*    */   
/*    */   default String getUpdateSql() {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   default String getInsertSql() {
/* 30 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   default String getDeleteSql() {
/* 35 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   static Ban fromString(String identifier) {
/* 40 */     return fromString(identifier, "", 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   static Ban fromString(String identifier, String reason, long expiry) {
/* 45 */     SteamId id = SteamId.fromAnyString(identifier);
/* 46 */     if (id != null) return new SteamIdBan(id, reason, expiry); 
/* 47 */     return new IPBan(identifier, reason, expiry);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Ban.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */