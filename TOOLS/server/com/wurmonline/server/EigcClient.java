/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class EigcClient
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(EigcClient.class.getName());
/*     */   
/*     */   private final String eigcUserName;
/*     */   
/*     */   private String eigcUserPassword;
/*     */   
/*  41 */   private String currentPlayerName = "";
/*     */   
/*  43 */   private String serviceBundle = "";
/*     */   
/*  45 */   private String playerAccount = "";
/*     */   
/*  47 */   private long serviceExpirationTime = Long.MAX_VALUE;
/*     */ 
/*     */   
/*     */   private static final String UPDATE_EIGC_ACCOUNT = "UPDATE EIGC SET PASSWORD=?,SERVICEBUNDLE=?,EXPIRATION=?,EMAIL=? WHERE USERNAME=?";
/*     */   
/*  52 */   private long lastUsed = 0L;
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
/*     */   public EigcClient(String eigcUserId, String clientPass, String services, long expirationTime, String accountName) {
/*  70 */     this.eigcUserName = eigcUserId;
/*  71 */     this.eigcUserPassword = clientPass;
/*  72 */     this.serviceBundle = services;
/*  73 */     this.serviceExpirationTime = expirationTime;
/*  74 */     this.playerAccount = accountName;
/*  75 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/*  77 */       logger.fine("Created EIGC Client for user ID: " + eigcUserId);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpiration(long expirationDate) {
/*  89 */     this.serviceExpirationTime = expirationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpiration() {
/*  99 */     return this.serviceExpirationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/* 109 */     return (System.currentTimeMillis() > this.serviceExpirationTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlayerName(String newPlayerName, String reason) {
/* 120 */     logger.log(Level.INFO, "Setting client " + getClientId() + " to player name " + newPlayerName + " reason=" + reason);
/*     */     
/* 122 */     this.currentPlayerName = newPlayerName;
/* 123 */     if (this.currentPlayerName == null || this.currentPlayerName.length() == 0) {
/* 124 */       setLastUsed(System.currentTimeMillis());
/*     */     } else {
/*     */       
/* 127 */       setLastUsed(Long.MAX_VALUE);
/* 128 */     }  if (System.currentTimeMillis() > getExpiration())
/*     */     {
/*     */       
/* 131 */       Eigc.modifyUser(getClientId(), "proximity", Long.MAX_VALUE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPlayerName() {
/* 142 */     return this.currentPlayerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAccountName() {
/* 152 */     return this.playerAccount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccountName(String newAccountName) {
/* 163 */     this.playerAccount = newAccountName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPermanent() {
/* 173 */     return (this.playerAccount != null && this.playerAccount.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceBundle(String newServices) {
/* 184 */     this.serviceBundle = newServices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServiceBundle() {
/* 194 */     return this.serviceBundle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String newPassword) {
/* 205 */     this.eigcUserPassword = newPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 215 */     return this.eigcUserPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/* 225 */     return this.eigcUserName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAccount() {
/* 230 */     Connection dbcon = null;
/* 231 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 234 */       dbcon = DbConnector.getLoginDbCon();
/* 235 */       ps = dbcon.prepareStatement("UPDATE EIGC SET PASSWORD=?,SERVICEBUNDLE=?,EXPIRATION=?,EMAIL=? WHERE USERNAME=?");
/* 236 */       ps.setString(1, this.eigcUserPassword);
/* 237 */       ps.setString(2, this.serviceBundle);
/* 238 */       ps.setLong(3, this.serviceExpirationTime);
/* 239 */       ps.setString(4, this.playerAccount);
/* 240 */       ps.setString(5, this.eigcUserName);
/* 241 */       ps.executeUpdate();
/*     */     }
/* 243 */     catch (SQLException sqx) {
/*     */       
/* 245 */       logger.log(Level.WARNING, "Problem updating EIGC for username " + this.eigcUserName + " due to " + sqx, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 249 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 250 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final long getLastUsed() {
/* 256 */     return this.lastUsed;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void setLastUsed(long time) {
/* 261 */     this.lastUsed = time;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean isUsed() {
/* 266 */     return (this.lastUsed > System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected final String timeSinceLastUse() {
/* 271 */     return Server.getTimeFor(System.currentTimeMillis() - this.lastUsed);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\EigcClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */