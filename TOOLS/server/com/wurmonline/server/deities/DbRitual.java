/*     */ package com.wurmonline.server.deities;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.spells.RiteEvent;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ 
/*     */ public class DbRitual
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(DbRitual.class.getName());
/*     */ 
/*     */   
/*     */   private static final String CREATE_RITE_EVENT = "INSERT INTO RITUALCASTS (ID,CASTERID,SPELLID,DEITYID,CASTTIME,DURATION) VALUES(?,?,?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String LOAD_RITE_EVENTS = "SELECT * FROM RITUALCASTS";
/*     */   
/*     */   private static final String CREATE_RITE_CLAIM = "INSERT INTO RITUALCLAIMS (ID,PLAYERID,RITUALCASTSID,CLAIMTIME) VALUES(?,?,?,?)";
/*     */   
/*     */   private static final String LOAD_RITE_CLAIMS = "SELECT * FROM RITUALCLAIMS";
/*     */ 
/*     */   
/*     */   public static void createRiteEvent(RiteEvent event) {
/*  54 */     Connection dbcon = null;
/*  55 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  58 */       dbcon = DbConnector.getDeityDbCon();
/*     */       
/*  60 */       ps = dbcon.prepareStatement("INSERT INTO RITUALCASTS (ID,CASTERID,SPELLID,DEITYID,CASTTIME,DURATION) VALUES(?,?,?,?,?,?)");
/*  61 */       ps.setInt(1, event.getId());
/*  62 */       ps.setLong(2, event.getCasterId());
/*  63 */       ps.setInt(3, event.getSpellId());
/*  64 */       ps.setInt(4, event.getDeityNum());
/*  65 */       ps.setLong(5, event.getCastTime());
/*  66 */       ps.setLong(6, event.getDuration());
/*  67 */       ps.executeUpdate();
/*     */     }
/*  69 */     catch (SQLException sqex) {
/*     */       
/*  71 */       logger.log(Level.WARNING, "Failed to create RiteEvent " + event.getId(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/*  75 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  76 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadRiteEvents() throws IOException {
/*  86 */     long lStart = System.nanoTime();
/*  87 */     Connection dbcon = null;
/*  88 */     PreparedStatement ps = null;
/*  89 */     ResultSet rs = null;
/*  90 */     int found = 0;
/*     */     
/*     */     try {
/*  93 */       dbcon = DbConnector.getDeityDbCon();
/*  94 */       ps = dbcon.prepareStatement("SELECT * FROM RITUALCASTS");
/*  95 */       rs = ps.executeQuery();
/*  96 */       while (rs.next())
/*     */       {
/*  98 */         int id = rs.getInt("ID");
/*  99 */         long casterId = rs.getLong("CASTERID");
/* 100 */         int spellId = rs.getInt("SPELLID");
/* 101 */         int deityNum = rs.getInt("DEITYID");
/* 102 */         long castTime = rs.getLong("CASTTIME");
/* 103 */         long duration = rs.getLong("DURATION");
/*     */         
/* 105 */         RiteEvent.createGenericRiteEvent(id, casterId, spellId, deityNum, castTime, duration);
/*     */         
/* 107 */         found++;
/*     */       }
/*     */     
/* 110 */     } catch (SQLException sqx) {
/*     */       
/* 112 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/* 113 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 117 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 118 */       DbConnector.returnConnection(dbcon);
/* 119 */       logger.info("Finished loading " + found + " RiteEvents, which took " + ((float)(System.nanoTime() - lStart) / 1000000.0F) + " millis.");
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
/*     */ 
/*     */   
/*     */   public static void createRiteClaim(int id, long playerId, int ritualCastsId, long claimTime) {
/* 133 */     Connection dbcon = null;
/* 134 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 137 */       dbcon = DbConnector.getDeityDbCon();
/*     */       
/* 139 */       ps = dbcon.prepareStatement("INSERT INTO RITUALCLAIMS (ID,PLAYERID,RITUALCASTSID,CLAIMTIME) VALUES(?,?,?,?)");
/* 140 */       ps.setInt(1, id);
/* 141 */       ps.setLong(2, playerId);
/* 142 */       ps.setInt(3, ritualCastsId);
/* 143 */       ps.setLong(4, claimTime);
/* 144 */       ps.executeUpdate();
/*     */     }
/* 146 */     catch (SQLException sqex) {
/*     */       
/* 148 */       logger.log(Level.WARNING, "Failed to create Rite claim for player " + playerId + " claiming rite " + id, sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 152 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 153 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadRiteClaims() throws IOException {
/* 163 */     long lStart = System.nanoTime();
/* 164 */     Connection dbcon = null;
/* 165 */     PreparedStatement ps = null;
/* 166 */     ResultSet rs = null;
/* 167 */     int found = 0;
/*     */     
/*     */     try {
/* 170 */       dbcon = DbConnector.getDeityDbCon();
/* 171 */       ps = dbcon.prepareStatement("SELECT * FROM RITUALCLAIMS");
/* 172 */       rs = ps.executeQuery();
/* 173 */       while (rs.next())
/*     */       {
/* 175 */         int id = rs.getInt("ID");
/* 176 */         long playerId = rs.getLong("PLAYERID");
/* 177 */         int ritualCastsId = rs.getInt("RITUALCASTSID");
/* 178 */         long claimTime = rs.getLong("CLAIMTIME");
/*     */         
/* 180 */         RiteEvent.addRitualClaim(id, playerId, ritualCastsId, claimTime);
/*     */         
/* 182 */         found++;
/*     */       }
/*     */     
/* 185 */     } catch (SQLException sqx) {
/*     */       
/* 187 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/* 188 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 192 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 193 */       DbConnector.returnConnection(dbcon);
/* 194 */       logger.info("Finished loading " + found + " Ritual claims, which took " + ((float)(System.nanoTime() - lStart) / 1000000.0F) + " millis.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\deities\DbRitual.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */