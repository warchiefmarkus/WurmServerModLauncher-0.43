/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IPBan
/*     */   implements Ban
/*     */ {
/*     */   private final String identifier;
/*     */   private String reason;
/*     */   private long expiry;
/*     */   private static final String ADD_BANNED_IP = "insert into BANNEDIPS (IPADDRESS,BANREASON,BANEXPIRY) values(?,?,?)";
/*     */   private static final String UPDATE_BANNED_IP = "UPDATE BANNEDIPS SET BANREASON=?,BANEXPIRY=? WHERE IPADDRESS=?";
/*     */   private static final String GET_BANNED_IPS = "select * from BANNEDIPS";
/*     */   private static final String REMOVE_BANNED_IP = "delete from BANNEDIPS where IPADDRESS=?";
/*     */   
/*     */   public IPBan(String _identifier, String _reason, long _expiry) {
/*  46 */     this.identifier = _identifier;
/*  47 */     setReason(_reason);
/*  48 */     setExpiry(_expiry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/*  59 */     return (System.currentTimeMillis() > getExpiry());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentifier() {
/*  65 */     return this.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReason() {
/*  71 */     return this.reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReason(String reason) {
/*  77 */     this.reason = reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpiry() {
/*  83 */     return this.expiry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpiry(long expiry) {
/*  89 */     this.expiry = expiry;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateSql() {
/*  94 */     return "UPDATE BANNEDIPS SET BANREASON=?,BANEXPIRY=? WHERE IPADDRESS=?";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInsertSql() {
/*  99 */     return "insert into BANNEDIPS (IPADDRESS,BANREASON,BANEXPIRY) values(?,?,?)";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeleteSql() {
/* 104 */     return "delete from BANNEDIPS where IPADDRESS=?";
/*     */   }
/*     */   
/*     */   public static String getSelectSql() {
/* 108 */     return "select * from BANNEDIPS";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\IPBan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */