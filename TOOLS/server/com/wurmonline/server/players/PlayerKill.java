/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ public final class PlayerKill
/*     */   implements TimeConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(PlayerKill.class.getName());
/*     */   
/*     */   private static final String ADD_KILL = "INSERT INTO KILLS(WURMID,VICTIM, VICTIMNAME,KILLTIME) VALUES(?,?,?,?)";
/*     */   private final long victimId;
/*  44 */   private long lastKilled = -10L;
/*  45 */   private int timesKilled = 0;
/*  46 */   private int timesKilledSinceRestart = 0;
/*  47 */   private String name = "";
/*     */ 
/*     */   
/*     */   PlayerKill(long _victimId, long _lastKilled, String _name, int kills) {
/*  51 */     this.victimId = _victimId;
/*  52 */     this.name = _name;
/*  53 */     this.lastKilled = _lastKilled;
/*  54 */     this.timesKilled = kills;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addKill(long time, String victimname, boolean loading) {
/*  62 */     this.timesKilled++;
/*  63 */     if (!loading)
/*  64 */       this.timesKilledSinceRestart++; 
/*  65 */     if (time > this.lastKilled) {
/*     */       
/*  67 */       this.lastKilled = time;
/*  68 */       this.name = victimname;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void kill(long killerId, String victimname) {
/*  77 */     Connection dbcon = null;
/*  78 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  81 */       dbcon = DbConnector.getPlayerDbCon();
/*  82 */       ps = dbcon.prepareStatement("INSERT INTO KILLS(WURMID,VICTIM, VICTIMNAME,KILLTIME) VALUES(?,?,?,?)");
/*  83 */       ps.setLong(1, killerId);
/*  84 */       ps.setLong(2, this.victimId);
/*  85 */       ps.setString(3, victimname);
/*  86 */       ps.setLong(4, System.currentTimeMillis());
/*  87 */       ps.executeUpdate();
/*     */     }
/*  89 */     catch (SQLException ex) {
/*     */       
/*  91 */       logger.log(Level.WARNING, "Failed to add kill for  " + killerId, ex);
/*     */     }
/*     */     finally {
/*     */       
/*  95 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  96 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*  98 */     addKill(System.currentTimeMillis(), victimname, false);
/*  99 */     if (isOverkilling()) {
/*     */       
/* 101 */       String kname = String.valueOf(killerId);
/*     */       
/*     */       try {
/* 104 */         kname = Players.getInstance().getNameFor(killerId);
/*     */       }
/* 106 */       catch (NoSuchPlayerException nsp) {
/*     */         
/* 108 */         logger.log(Level.INFO, "weird " + kname + " not online while killing " + killerId);
/*     */       }
/* 110 */       catch (IOException iox) {
/*     */         
/* 112 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */       } 
/* 114 */       logger.log(Level.INFO, kname + " overkilling " + this.name + " since restart: " + this.timesKilledSinceRestart + " overall: " + this.timesKilled);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long getLastKill() {
/* 121 */     return this.lastKilled;
/*     */   }
/*     */ 
/*     */   
/*     */   int getNumKills() {
/* 126 */     return this.timesKilled;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isOverkilling() {
/* 131 */     return ((this.lastKilled > System.currentTimeMillis() - 21600000L && this.timesKilledSinceRestart > 3) || this.timesKilled > 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerKill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */