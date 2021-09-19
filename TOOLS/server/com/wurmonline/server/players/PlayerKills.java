/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*     */ import com.wurmonline.server.statistics.ChallengeSummary;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class PlayerKills
/*     */ {
/*     */   private final long wurmid;
/*  44 */   private static final Logger logger = Logger.getLogger(PlayerKills.class.getName());
/*     */   private static final String GET_KILLS = "SELECT * FROM KILLS WHERE WURMID=?";
/*  46 */   private final Map<Long, PlayerKill> kills = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerKills(long _wurmId) {
/*  53 */     this.wurmid = _wurmId;
/*  54 */     load();
/*     */   }
/*     */ 
/*     */   
/*     */   private void load() {
/*  59 */     Connection dbcon = null;
/*  60 */     PreparedStatement ps = null;
/*  61 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  64 */       dbcon = DbConnector.getPlayerDbCon();
/*  65 */       ps = dbcon.prepareStatement("SELECT * FROM KILLS WHERE WURMID=?");
/*  66 */       ps.setLong(1, this.wurmid);
/*  67 */       rs = ps.executeQuery();
/*  68 */       while (rs.next())
/*     */       {
/*  70 */         Long vid = new Long(rs.getLong("VICTIM"));
/*  71 */         PlayerKill pk = this.kills.get(vid);
/*  72 */         if (pk != null) {
/*  73 */           pk.addKill(rs.getLong("KILLTIME"), rs.getString("VICTIMNAME"), true); continue;
/*     */         } 
/*  75 */         this.kills.put(vid, new PlayerKill(vid.longValue(), rs.getLong("KILLTIME"), rs.getString("VICTIMNAME"), 1));
/*     */       }
/*     */     
/*  78 */     } catch (SQLException ex) {
/*     */       
/*  80 */       logger.log(Level.INFO, "Failed to load kills for " + this.wurmid, ex);
/*     */     }
/*  82 */     catch (Exception ex) {
/*     */       
/*  84 */       logger.log(Level.INFO, "Failed to load kills for " + this.wurmid, ex);
/*     */     }
/*     */     finally {
/*     */       
/*  88 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  89 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastKill(long victimId) {
/*  95 */     PlayerKill pk = this.kills.get(Long.valueOf(victimId));
/*  96 */     if (pk != null)
/*     */     {
/*  98 */       return pk.getLastKill();
/*     */     }
/* 100 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNumKills(long victimId) {
/* 105 */     PlayerKill pk = this.kills.get(Long.valueOf(victimId));
/* 106 */     if (pk != null)
/*     */     {
/* 108 */       return pk.getNumKills();
/*     */     }
/* 110 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addKill(long victimId, String victimName) {
/* 115 */     Long vid = new Long(victimId);
/* 116 */     PlayerKill pk = this.kills.get(vid);
/* 117 */     if (pk != null) {
/* 118 */       pk.kill(this.wurmid, victimName);
/*     */     } else {
/*     */       
/* 121 */       pk = new PlayerKill(victimId, System.currentTimeMillis(), victimName, 0);
/* 122 */       if (Servers.localServer.isChallengeServer()) {
/*     */         
/* 124 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(this.wurmid);
/* 125 */         if (pinf != null) {
/*     */           
/* 127 */           Achievements ach = Achievements.getAchievementObject(this.wurmid);
/* 128 */           if (ach != null && ach.getAchievement(369) != null) {
/*     */             
/* 130 */             ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.PLAYERKILLS.getEnumtype(), 1.0F);
/* 131 */             ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 10.0F);
/*     */           } 
/*     */         } 
/*     */       } 
/* 135 */       pk.kill(this.wurmid, victimName);
/* 136 */       this.kills.put(vid, pk);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverKilling(long victimId) {
/* 142 */     Long vid = new Long(victimId);
/* 143 */     PlayerKill pk = this.kills.get(vid);
/* 144 */     if (pk != null)
/* 145 */       return pk.isOverkilling(); 
/* 146 */     return false;
/*     */   }
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
/*     */   public int getNumberOfKills() {
/* 160 */     return this.kills.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerKills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */