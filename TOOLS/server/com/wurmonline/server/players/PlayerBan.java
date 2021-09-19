/*    */ package com.wurmonline.server.players;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerBan
/*    */   implements Ban
/*    */ {
/*    */   private String playerName;
/*    */   private String reason;
/*    */   private long expiry;
/*    */   
/*    */   public PlayerBan(String playerName, String reason, long expiry) {
/* 14 */     this.playerName = playerName;
/* 15 */     this.reason = reason;
/* 16 */     this.expiry = expiry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isExpired() {
/* 23 */     return (System.currentTimeMillis() > getExpiry());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getIdentifier() {
/* 29 */     return this.playerName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getReason() {
/* 35 */     return this.reason;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReason(String reason) {
/* 41 */     this.reason = reason;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getExpiry() {
/* 47 */     return this.expiry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpiry(long expiry) {
/* 53 */     this.expiry = expiry;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerBan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */