/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.questions.SimplePopup;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MissionHelper
/*     */   implements MiscConstants
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(MissionHelper.class.getName());
/*     */   
/*     */   private static final String LOAD_ALL_MISSION_HELPERS = "SELECT * FROM MISSIONHELPERS";
/*     */   
/*  56 */   private static final String INSERT_MISSION_HELPER = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO MISSIONHELPERS (NUMS, MISSIONID, PLAYERID) VALUES(?,?,?)" : "INSERT IGNORE INTO MISSIONHELPERS (NUMS, MISSIONID, PLAYERID) VALUES(?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String MOVE_MISSION_HELPER = "UPDATE MISSIONHELPERS SET MISSIONID=? WHERE MISSIONID=?";
/*     */ 
/*     */   
/*     */   private static final String DELETE_MISSION_HELPER = "DELETE FROM MISSIONHELPERS WHERE MISSIONID=?";
/*     */ 
/*     */   
/*     */   private static final String UPDATE_MISSION_HELPER = "UPDATE MISSIONHELPERS SET NUMS=? WHERE MISSIONID=? AND PLAYERID=?";
/*     */   
/*  67 */   private static final Map<Long, MissionHelper> MISSION_HELPERS = new ConcurrentHashMap<>();
/*     */   private static boolean INITIALIZED = false;
/*  69 */   private final Map<Long, Integer> missionsHelped = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final long playerId;
/*     */ 
/*     */ 
/*     */   
/*     */   public MissionHelper(long playerid) {
/*  78 */     this.playerId = playerid;
/*  79 */     addHelper(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void increaseHelps(long missionId) {
/*  84 */     setHelps(missionId, getHelps(missionId) + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void increaseHelps(long missionId, int nums) {
/*  89 */     setHelps(missionId, getHelps(missionId) + nums);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void addHelper(MissionHelper helper) {
/*  94 */     MISSION_HELPERS.put(Long.valueOf(helper.getPlayerId()), helper);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void setHelpsAtLoad(long missionId, int nums) {
/*  99 */     this.missionsHelped.put(Long.valueOf(missionId), Integer.valueOf(nums));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Map<Long, MissionHelper> getHelpers() {
/* 104 */     return MISSION_HELPERS;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAll() {
/* 109 */     if (!INITIALIZED) {
/*     */       
/* 111 */       Connection dbcon = null;
/* 112 */       PreparedStatement ps = null;
/* 113 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 116 */         dbcon = DbConnector.getZonesDbCon();
/* 117 */         ps = dbcon.prepareStatement("SELECT * FROM MISSIONHELPERS");
/* 118 */         rs = ps.executeQuery();
/* 119 */         while (rs.next()) {
/*     */           
/* 121 */           long helperId = rs.getLong("PLAYERID");
/* 122 */           MissionHelper helper = MISSION_HELPERS.get(Long.valueOf(helperId));
/* 123 */           if (helper == null)
/* 124 */             helper = new MissionHelper(helperId); 
/* 125 */           helper.setHelpsAtLoad(rs.getLong("MISSIONID"), rs.getInt("NUMS"));
/*     */         } 
/* 127 */         INITIALIZED = true;
/*     */       }
/* 129 */       catch (SQLException sqx) {
/*     */         
/* 131 */         logger.log(Level.WARNING, "Failed to load epic item helpers.", sqx);
/* 132 */         INITIALIZED = false;
/*     */       }
/*     */       finally {
/*     */         
/* 136 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 137 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void printHelpForMission(long missionId, String missionName, Creature performer) {
/* 144 */     float total = 0.0F;
/*     */     
/* 146 */     if (!INITIALIZED) {
/* 147 */       loadAll();
/*     */     }
/* 149 */     for (MissionHelper helper : MISSION_HELPERS.values())
/*     */     {
/* 151 */       total += helper.getHelps(missionId);
/*     */     }
/* 153 */     if (total > 0.0F) {
/*     */       
/* 155 */       SimplePopup sp = new SimplePopup(performer, "Plaque on " + missionName, "These helped:", missionId, total);
/* 156 */       sp.sendQuestion();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void addKarmaForItem(long itemId) {
/* 165 */     for (MissionHelper helper : MISSION_HELPERS.values()) {
/*     */       
/* 167 */       int i = helper.getHelps(itemId);
/*     */       
/* 169 */       if (i > 10) {
/*     */         
/*     */         try {
/*     */           
/* 173 */           Player p = Players.getInstance().getPlayer(helper.getPlayerId());
/* 174 */           p.modifyKarma(i / 10);
/*     */         }
/* 176 */         catch (NoSuchPlayerException nsp) {
/*     */           
/* 178 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(helper.getPlayerId());
/* 179 */           pinf.setKarma(pinf.getKarma() + i / 10);
/*     */         } 
/*     */       }
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
/*     */   public static final MissionHelper getOrCreateHelper(long playerId) {
/* 218 */     MissionHelper helper = MISSION_HELPERS.get(Long.valueOf(playerId));
/* 219 */     if (helper == null)
/* 220 */       helper = new MissionHelper(playerId); 
/* 221 */     return helper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getPlayerId() {
/* 231 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getHelps(long missionId) {
/* 236 */     Integer nums = this.missionsHelped.get(Long.valueOf(missionId));
/* 237 */     if (nums == null)
/* 238 */       return 0; 
/* 239 */     return nums.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private final void moveLocalMissionId(long oldMissionId, long newMissionId) {
/* 244 */     int oldHelps = getHelps(oldMissionId);
/* 245 */     if (oldHelps > 0) {
/*     */       
/* 247 */       this.missionsHelped.remove(Long.valueOf(oldMissionId));
/* 248 */       this.missionsHelped.put(Long.valueOf(newMissionId), Integer.valueOf(oldHelps));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final void removeMissionId(long missionId) {
/* 254 */     this.missionsHelped.remove(Long.valueOf(missionId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void moveGlobalMissionId(long oldmissionId, long newMissionId) {
/* 259 */     Connection dbcon = null;
/* 260 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 263 */       dbcon = DbConnector.getZonesDbCon();
/* 264 */       ps = dbcon.prepareStatement("UPDATE MISSIONHELPERS SET MISSIONID=? WHERE MISSIONID=?");
/* 265 */       ps.setLong(1, newMissionId);
/* 266 */       ps.setLong(2, oldmissionId);
/* 267 */       ps.executeUpdate();
/* 268 */       for (MissionHelper h : MISSION_HELPERS.values())
/*     */       {
/* 270 */         h.moveLocalMissionId(oldmissionId, newMissionId);
/*     */       }
/*     */     }
/* 273 */     catch (SQLException sqx) {
/*     */       
/* 275 */       logger.log(Level.WARNING, "Failed to move epic mission helps from mission " + oldmissionId + ", to" + newMissionId, sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 280 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 281 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteMissionId(long missionId) {
/* 287 */     Connection dbcon = null;
/* 288 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 291 */       dbcon = DbConnector.getZonesDbCon();
/* 292 */       ps = dbcon.prepareStatement("DELETE FROM MISSIONHELPERS WHERE MISSIONID=?");
/* 293 */       ps.setLong(1, missionId);
/* 294 */       ps.executeUpdate();
/* 295 */       for (MissionHelper h : MISSION_HELPERS.values())
/*     */       {
/* 297 */         h.removeMissionId(missionId);
/*     */       }
/*     */     }
/* 300 */     catch (SQLException sqx) {
/*     */       
/* 302 */       logger.log(Level.WARNING, "Failed to delete epic mission helps for mission " + missionId, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 306 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 307 */       DbConnector.returnConnection(dbcon);
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
/*     */   public final void setHelps(long missionId, int helps) {
/* 319 */     int oldHelps = getHelps(missionId);
/* 320 */     if (oldHelps != helps) {
/*     */       
/* 322 */       this.missionsHelped.put(Long.valueOf(missionId), Integer.valueOf(helps));
/* 323 */       Connection dbcon = null;
/* 324 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 327 */         dbcon = DbConnector.getZonesDbCon();
/* 328 */         if (oldHelps == 0) {
/* 329 */           ps = dbcon.prepareStatement(INSERT_MISSION_HELPER);
/*     */         } else {
/* 331 */           ps = dbcon.prepareStatement("UPDATE MISSIONHELPERS SET NUMS=? WHERE MISSIONID=? AND PLAYERID=?");
/* 332 */         }  ps.setInt(1, helps);
/* 333 */         ps.setLong(2, missionId);
/* 334 */         ps.setLong(3, this.playerId);
/* 335 */         ps.executeUpdate();
/*     */       }
/* 337 */       catch (SQLException sqx) {
/*     */         
/* 339 */         logger.log(Level.WARNING, "Failed to save epic item helps " + helps + " for mission " + missionId + ", pid=" + this.playerId, sqx);
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 344 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 345 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\MissionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */