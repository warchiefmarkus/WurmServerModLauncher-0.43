/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
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
/*     */ public final class MissionPerformed
/*     */   implements CounterTypes
/*     */ {
/*  41 */   private static Logger logger = Logger.getLogger(MissionPerformed.class.getName());
/*  42 */   private static final Map<Long, MissionPerformer> missionsPerformers = new HashMap<>();
/*     */   
/*     */   private static final String LOADALLMISSIONSPERFORMER = "SELECT * FROM MISSIONSPERFORMED";
/*     */   
/*     */   private static final String ADDMISSIONSPERFORMED = "INSERT INTO MISSIONSPERFORMED (PERFORMER,MISSION,STATE,STARTTIME) VALUES(?,?,?,?)";
/*     */   
/*     */   private static final String DELETEALLMISSIONSPERFORMER = "DELETE FROM MISSIONSPERFORMED WHERE PERFORMER=?";
/*     */   
/*     */   private static final String UPDATESTATE = "UPDATE MISSIONSPERFORMED SET STATE=? WHERE MISSION=? AND PERFORMER=?";
/*     */   
/*     */   private static final String SETINACTIVATED = "UPDATE MISSIONSPERFORMED SET INACTIVE=? WHERE MISSION=? AND PERFORMER=?";
/*     */   
/*     */   private static final String RESTARTMISSION = "UPDATE MISSIONSPERFORMED SET STARTTIME=?,FINISHEDDATE=? WHERE MISSION=? AND PERFORMER=?";
/*     */   
/*     */   private static final String UPDATEFINISHEDDATE = "UPDATE MISSIONSPERFORMED SET FINISHEDDATE=?, ENDTIME=? WHERE MISSION=? AND PERFORMER=?";
/*     */   
/*     */   public static final float FINISHED = 100.0F;
/*     */   
/*     */   public static final float NOTSTARTED = 0.0F;
/*     */   
/*     */   public static final float STARTED = 1.0F;
/*     */   
/*     */   public static final float FAILED = -1.0F;
/*     */   
/*     */   public static final float SOME_COMPLETED = 33.0F;
/*     */   
/*     */   private final int mission;
/*     */   
/*  70 */   private float state = 0.0F;
/*  71 */   private long startTime = 0L;
/*  72 */   private long endTime = 0L;
/*  73 */   private String endDate = "";
/*     */ 
/*     */   
/*     */   private boolean inactive = false;
/*     */ 
/*     */   
/*     */   private final long wurmid;
/*     */   
/*     */   private final MissionPerformer performer;
/*     */   
/*  83 */   private static long tempMissionPerformedCounter = 0L;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  88 */       loadAllMissionsPerformed();
/*     */     }
/*  90 */     catch (Exception ex) {
/*     */       
/*  92 */       logger.log(Level.WARNING, "Problems loading Missions Performed", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionPerformed(int missionId, MissionPerformer perf) {
/*  98 */     this.mission = missionId;
/*  99 */     this.wurmid = generateWurmId(this.mission);
/* 100 */     this.performer = perf;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long generateWurmId(int mission) {
/* 105 */     tempMissionPerformedCounter++;
/* 106 */     return BigInteger.valueOf(tempMissionPerformedCounter).shiftLeft(24).longValue() + (mission << 8) + 22L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int decodeMissionId(long wurmId) {
/* 112 */     return (int)(wurmId >> 8L & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/* 117 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMissionId() {
/* 122 */     return this.mission;
/*     */   }
/*     */ 
/*     */   
/*     */   public Mission getMission() {
/* 127 */     return Missions.getMissionWithId(this.mission);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getState() {
/* 132 */     return this.state;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInactivated() {
/* 137 */     return this.inactive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/* 142 */     return (this.state == 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFailed() {
/* 147 */     return (this.state == -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 152 */     return (this.state >= 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getStartTimeMillis() {
/* 157 */     return this.startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getStartDate() {
/* 162 */     return DateFormat.getDateInstance(1).format(new Timestamp(this.startTime));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLastTimeToFinish(int maxSecondsToFinish) {
/* 167 */     return DateFormat.getDateInstance(1).format(new Timestamp(this.startTime + (maxSecondsToFinish * 1000)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getFinishTimeAsLong(int maxSecondsToFinish) {
/* 172 */     return this.startTime + (maxSecondsToFinish * 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getStartTime() {
/* 180 */     return this.startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   String getEndDate() {
/* 185 */     return this.endDate;
/*     */   }
/*     */ 
/*     */   
/*     */   long getEndTime() {
/* 190 */     return this.endTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionPerformer getMissionPerformer(long id) {
/* 195 */     return missionsPerformers.get(Long.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionPerformer[] getAllPerformers() {
/* 200 */     return (MissionPerformer[])missionsPerformers.values().toArray((Object[])new MissionPerformer[missionsPerformers.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInactive(boolean inactivate) {
/* 208 */     if (this.inactive != inactivate) {
/*     */       
/* 210 */       this.inactive = inactivate;
/*     */       
/* 212 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 215 */         Connection dbcon = DbConnector.getPlayerDbCon();
/* 216 */         ps = dbcon.prepareStatement("UPDATE MISSIONSPERFORMED SET INACTIVE=? WHERE MISSION=? AND PERFORMER=?");
/* 217 */         ps.setBoolean(1, this.inactive);
/* 218 */         ps.setLong(2, this.performer.getWurmId());
/* 219 */         ps.executeUpdate();
/*     */       }
/* 221 */       catch (SQLException sqx) {
/*     */         
/* 223 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 227 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/* 229 */       sendUpdate();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendUpdate() {
/* 235 */     if (this.performer != null)
/*     */     {
/* 237 */       this.performer.sendUpdatePerformer(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteMissionPerformer(long id) {
/* 246 */     missionsPerformers.remove(Long.valueOf(id));
/* 247 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 250 */       Connection dbcon = DbConnector.getPlayerDbCon();
/* 251 */       ps = dbcon.prepareStatement("DELETE FROM MISSIONSPERFORMED WHERE PERFORMER=?");
/* 252 */       ps.setLong(1, id);
/* 253 */       ps.executeUpdate();
/*     */     }
/* 255 */     catch (SQLException sqx) {
/*     */       
/* 257 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 261 */       DbUtilities.closeDatabaseObjects(ps, null);
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
/*     */   public boolean setState(float newState, long aPerformer) {
/* 274 */     if (this.state != newState) {
/*     */       
/* 276 */       this.state = newState;
/* 277 */       if (this.state > 100.0F)
/* 278 */         this.state = 100.0F; 
/* 279 */       if (this.state < -1.0F)
/* 280 */         this.state = -1.0F; 
/* 281 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 284 */         Connection dbcon = DbConnector.getPlayerDbCon();
/* 285 */         ps = dbcon.prepareStatement("UPDATE MISSIONSPERFORMED SET STATE=? WHERE MISSION=? AND PERFORMER=?");
/* 286 */         ps.setFloat(1, this.state);
/* 287 */         ps.setInt(2, this.mission);
/* 288 */         ps.setLong(3, aPerformer);
/*     */         
/* 290 */         ps.executeUpdate();
/*     */       }
/* 292 */       catch (SQLException sqx) {
/*     */         
/* 294 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 298 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/* 300 */       if (this.state >= 100.0F || this.state <= -1.0F) {
/* 301 */         setFinishDate(DateFormat.getDateInstance(1).format(new Timestamp(System.currentTimeMillis())), aPerformer);
/*     */       }
/* 303 */       if (this.state == 1.0F)
/* 304 */         restartMission(this.performer.getWurmId()); 
/* 305 */       sendUpdate();
/*     */     } 
/* 307 */     return (this.state >= 100.0F || this.state <= -1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFinishDate(String date, long aPerformer) {
/* 315 */     this.endDate = date;
/*     */     
/* 317 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 320 */       Connection dbcon = DbConnector.getPlayerDbCon();
/* 321 */       ps = dbcon.prepareStatement("UPDATE MISSIONSPERFORMED SET FINISHEDDATE=?, ENDTIME=? WHERE MISSION=? AND PERFORMER=?");
/* 322 */       ps.setString(1, date);
/* 323 */       this.endTime = System.currentTimeMillis();
/* 324 */       ps.setLong(2, this.endTime);
/*     */       
/* 326 */       ps.setInt(3, this.mission);
/* 327 */       ps.setLong(4, aPerformer);
/* 328 */       ps.executeUpdate();
/*     */     }
/* 330 */     catch (SQLException sqx) {
/*     */       
/* 332 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 336 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionPerformer startNewMission(int mission, long performerId, float state) {
/* 342 */     MissionPerformer mp = missionsPerformers.get(Long.valueOf(performerId));
/* 343 */     if (mp == null) {
/*     */       
/* 345 */       mp = new MissionPerformer(performerId);
/* 346 */       missionsPerformers.put(Long.valueOf(performerId), mp);
/*     */     } 
/* 348 */     MissionPerformed mpf = new MissionPerformed(mission, mp);
/*     */     
/* 350 */     mpf.state = state;
/* 351 */     mpf.startTime = System.currentTimeMillis();
/* 352 */     mp.addMissionPerformed(mpf);
/* 353 */     Connection dbcon = null;
/* 354 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 357 */       dbcon = DbConnector.getPlayerDbCon();
/* 358 */       ps = dbcon.prepareStatement("INSERT INTO MISSIONSPERFORMED (PERFORMER,MISSION,STATE,STARTTIME) VALUES(?,?,?,?)");
/* 359 */       ps.setLong(1, performerId);
/* 360 */       ps.setInt(2, mission);
/* 361 */       ps.setFloat(3, state);
/* 362 */       ps.setLong(4, mpf.startTime);
/* 363 */       ps.executeUpdate();
/*     */     }
/* 365 */     catch (SQLException sqx) {
/*     */       
/* 367 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 371 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 372 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 374 */     if (state == 100.0F)
/*     */     {
/* 376 */       mpf.setFinishDate(DateFormat.getDateInstance(1).format(new Timestamp(System.currentTimeMillis())), performerId);
/*     */     }
/*     */     
/* 379 */     mpf.sendUpdate();
/* 380 */     return mp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void restartMission(long aPerformer) {
/* 388 */     Connection dbcon = null;
/* 389 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 392 */       this.startTime = System.currentTimeMillis();
/* 393 */       dbcon = DbConnector.getPlayerDbCon();
/* 394 */       ps = dbcon.prepareStatement("UPDATE MISSIONSPERFORMED SET STARTTIME=?,FINISHEDDATE=? WHERE MISSION=? AND PERFORMER=?");
/* 395 */       ps.setLong(1, this.startTime);
/* 396 */       ps.setLong(2, this.endTime);
/*     */       
/* 398 */       ps.setInt(3, this.mission);
/* 399 */       ps.setLong(4, aPerformer);
/* 400 */       ps.executeUpdate();
/*     */     }
/* 402 */     catch (SQLException sqx) {
/*     */       
/* 404 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 408 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 409 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadAllMissionsPerformed() {
/* 415 */     Connection dbcon = null;
/* 416 */     PreparedStatement ps = null;
/* 417 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 420 */       dbcon = DbConnector.getPlayerDbCon();
/* 421 */       ps = dbcon.prepareStatement("SELECT * FROM MISSIONSPERFORMED");
/* 422 */       rs = ps.executeQuery();
/* 423 */       while (rs.next())
/*     */       {
/* 425 */         long performer = rs.getLong("PERFORMER");
/* 426 */         MissionPerformer mp = missionsPerformers.get(Long.valueOf(performer));
/* 427 */         if (mp == null) {
/*     */           
/* 429 */           mp = new MissionPerformer(performer);
/* 430 */           missionsPerformers.put(Long.valueOf(performer), mp);
/*     */         } 
/* 432 */         MissionPerformed mpf = new MissionPerformed(rs.getInt("MISSION"), mp);
/*     */         
/* 434 */         mpf.state = rs.getInt("STATE");
/* 435 */         mpf.startTime = rs.getLong("STARTTIME");
/* 436 */         mpf.endDate = rs.getString("FINISHEDDATE");
/* 437 */         mpf.endTime = rs.getLong("ENDTIME");
/* 438 */         mp.addMissionPerformed(mpf);
/*     */       }
/*     */     
/* 441 */     } catch (SQLException sqx) {
/*     */       
/* 443 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 447 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 448 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionPerformed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */