/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.items.CreationEntry;
/*     */ import com.wurmonline.server.items.CreationMatrix;
/*     */ import com.wurmonline.server.tutorial.MissionTrigger;
/*     */ import com.wurmonline.server.tutorial.MissionTriggers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EpicMission
/*     */ {
/*  51 */   private static final Logger logger = Logger.getLogger(EpicMission.class.getName());
/*     */   
/*     */   private final int epicEntityId;
/*     */   
/*     */   private final int epicScenarioId;
/*     */   
/*     */   private final String epicEntityName;
/*     */   
/*     */   private final String epicScenarioName;
/*     */   
/*     */   private final int missionId;
/*     */   
/*     */   private final int serverId;
/*     */   private final byte missionType;
/*     */   private final int difficulty;
/*     */   private long expireTime;
/*     */   private final String rewards;
/*  68 */   private long endTime = 0L;
/*  69 */   private float missionProgress = 0.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean current = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SAVE_EPIC_MISSION_ENTITY = "INSERT INTO EPICMISSIONS(NAME,SCENARIONAME,MISSION,SCENARIO,TSTAMP,ENTITY,SERVERID,PROGRESS,CURRENT,MISSIONTYPE,DIFFICULTY) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UPDATE_EPIC_MISSION_ENTITY = "UPDATE EPICMISSIONS SET TSTAMP=?,PROGRESS=?,CURRENT=?,ENDTIME=?,MISSIONTYPE=?,DIFFICULTY=? WHERE MISSION=? AND ENTITY=? AND SERVERID=?";
/*     */ 
/*     */   
/*     */   private static final String DELETE_EPIC_MISSION = "DELETE FROM EPICMISSIONS WHERE MISSION=? AND ENTITY=?";
/*     */ 
/*     */ 
/*     */   
/*     */   public EpicMission(int entityid, int scenarioId, String name, String scenarioName, int mission, byte missionType, int difficulty, float progress, int server, long time, boolean load, boolean isCurrent) {
/*  89 */     this.epicEntityId = entityid;
/*  90 */     this.epicScenarioId = scenarioId;
/*  91 */     this.epicScenarioName = scenarioName;
/*  92 */     this.missionId = mission;
/*  93 */     this.missionType = missionType;
/*  94 */     this.difficulty = difficulty;
/*  95 */     this.rewards = generateRewardsString();
/*  96 */     this.serverId = server;
/*  97 */     this.expireTime = time;
/*  98 */     this.epicEntityName = name;
/*  99 */     this.missionProgress = progress;
/*     */     
/* 101 */     this.current = isCurrent;
/* 102 */     if (!load)
/*     */     {
/* 104 */       save();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private String generateRewardsString() {
/* 110 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 112 */     EpicMissionEnum missionEnum = EpicMissionEnum.getMissionForType(getMissionType());
/* 113 */     if (missionEnum == null) {
/* 114 */       return "Unknown";
/*     */     }
/* 116 */     int karmaPerPart = missionEnum.getBaseKarma();
/* 117 */     int karmaSplit = missionEnum.getKarmaBonusDiffMult() * getDifficulty();
/* 118 */     int sleepPerPart = missionEnum.getBaseSleep();
/* 119 */     int sleepSplit = missionEnum.getSleepBonusDiffMult() * getDifficulty();
/* 120 */     int numReq = EpicServerStatus.getNumberRequired(getDifficulty(), missionEnum);
/*     */     
/* 122 */     if (karmaPerPart > 0)
/* 123 */       sb.append(karmaPerPart + " karma " + ((sleepPerPart > 0) ? "and " : "to")); 
/* 124 */     if (sleepPerPart > 0)
/* 125 */       sb.append(sleepPerPart + "m sleep bonus for"); 
/* 126 */     if (numReq > 1) {
/* 127 */       sb.append(" each participant upon completion of this mission");
/*     */     } else {
/* 129 */       sb.append(" the person who completes this mission");
/*     */     } 
/* 131 */     boolean creature = EpicMissionEnum.isMissionCreature(missionEnum);
/* 132 */     boolean item = EpicMissionEnum.isMissionItem(missionEnum);
/*     */     
/* 134 */     MissionTrigger[] triggers = MissionTriggers.getMissionTriggers(getMissionId());
/* 135 */     if (EpicMissionEnum.isNumReqItemEffected(missionEnum) && triggers.length > 0) {
/*     */       
/* 137 */       int templateId = triggers[0].getItemUsedId();
/* 138 */       CreationEntry entry = CreationMatrix.getInstance().getCreationEntry(templateId);
/* 139 */       if (entry != null)
/* 140 */         numReq /= Math.min(100, entry.getTotalNumberOfItems()); 
/*     */     } 
/* 142 */     numReq = Math.max(1, numReq);
/* 143 */     if (karmaSplit > 0)
/*     */     {
/* 145 */       if (missionEnum.isKarmaMultProgress()) {
/* 146 */         sb.append(" as well as " + (int)Math.ceil((karmaSplit / numReq)) + " karma per " + (creature ? "creature" : (item ? "item" : "action")));
/* 147 */       } else if (!EpicMissionEnum.isKarmaSplitNearby(missionEnum)) {
/* 148 */         sb.append(" as well as " + karmaSplit + " karma split between participants");
/*     */       } else {
/* 150 */         sb.append(" as well as " + karmaSplit + " karma split between nearby players");
/*     */       }  } 
/* 152 */     if (sleepSplit > 0)
/*     */     {
/* 154 */       if (missionEnum.isSleepMultNearby()) {
/* 155 */         sb.append(" with " + sleepSplit + "m extra sleep bonus split between nearby players upon completion (30m max each)");
/*     */       } else {
/* 157 */         sb.append(" with " + sleepSplit + "m extra sleep bonus split between participants upon completion (30m max each)");
/*     */       } 
/*     */     }
/* 160 */     if (Servers.isThisAnEpicServer()) {
/* 161 */       sb.append(". The movement timer for " + Deities.getEntityName(this.epicEntityId) + " will also be reduced by " + (
/* 162 */           EpicMissionEnum.getTimeReductionForMission(getMissionType(), getDifficulty()) / 3600000L) + " hours.");
/*     */     }
/* 164 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private final void save() {
/* 169 */     Connection dbcon = null;
/* 170 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 173 */       dbcon = DbConnector.getDeityDbCon();
/* 174 */       ps = dbcon.prepareStatement("INSERT INTO EPICMISSIONS(NAME,SCENARIONAME,MISSION,SCENARIO,TSTAMP,ENTITY,SERVERID,PROGRESS,CURRENT,MISSIONTYPE,DIFFICULTY) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
/* 175 */       ps.setString(1, this.epicEntityName);
/* 176 */       ps.setString(2, this.epicScenarioName);
/* 177 */       ps.setInt(3, this.missionId);
/* 178 */       ps.setInt(4, this.epicScenarioId);
/* 179 */       ps.setLong(5, this.expireTime);
/* 180 */       ps.setInt(6, this.epicEntityId);
/* 181 */       ps.setInt(7, this.serverId);
/* 182 */       ps.setFloat(8, this.missionProgress);
/* 183 */       ps.setBoolean(9, this.current);
/* 184 */       ps.setByte(10, this.missionType);
/* 185 */       ps.setInt(11, this.difficulty);
/* 186 */       ps.executeUpdate();
/*     */     }
/* 188 */     catch (SQLException sqx) {
/*     */       
/* 190 */       logger.log(Level.WARNING, "Failed to save epic mission status.", sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 194 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 195 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final void update() {
/* 201 */     Connection dbcon = null;
/* 202 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 205 */       dbcon = DbConnector.getDeityDbCon();
/* 206 */       ps = dbcon.prepareStatement("UPDATE EPICMISSIONS SET TSTAMP=?,PROGRESS=?,CURRENT=?,ENDTIME=?,MISSIONTYPE=?,DIFFICULTY=? WHERE MISSION=? AND ENTITY=? AND SERVERID=?");
/* 207 */       ps.setLong(1, this.expireTime);
/* 208 */       ps.setFloat(2, this.missionProgress);
/* 209 */       ps.setBoolean(3, this.current);
/* 210 */       ps.setLong(4, this.endTime);
/* 211 */       ps.setByte(5, this.missionType);
/* 212 */       ps.setInt(6, this.difficulty);
/* 213 */       ps.setInt(7, this.missionId);
/* 214 */       ps.setInt(8, this.epicEntityId);
/* 215 */       ps.setInt(9, this.serverId);
/* 216 */       ps.executeUpdate();
/*     */     }
/* 218 */     catch (SQLException sqx) {
/*     */       
/* 220 */       logger.log(Level.WARNING, "Failed to update epic mission progress.", sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 224 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 225 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final void delete() {
/* 231 */     Connection dbcon = null;
/* 232 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 235 */       dbcon = DbConnector.getDeityDbCon();
/* 236 */       ps = dbcon.prepareStatement("DELETE FROM EPICMISSIONS WHERE MISSION=? AND ENTITY=?");
/* 237 */       ps.setInt(1, this.missionId);
/* 238 */       ps.setInt(2, this.epicEntityId);
/* 239 */       ps.executeUpdate();
/*     */     }
/* 241 */     catch (SQLException sqx) {
/*     */       
/* 243 */       logger.log(Level.WARNING, "Failed to delete epic mission.", sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 247 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 248 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateProgress(float newProgress) {
/* 254 */     this.missionProgress = newProgress;
/* 255 */     if (this.missionProgress >= 100.0F) {
/* 256 */       this.endTime = System.currentTimeMillis();
/* 257 */     } else if (Servers.localServer.LOGINSERVER && this.missionProgress >= getServerId()) {
/* 258 */       this.endTime = System.currentTimeMillis();
/*     */     } 
/* 260 */     update();
/*     */     
/* 262 */     Players.getInstance().sendUpdateEpicMission(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCompleted() {
/* 267 */     return (this.missionProgress >= 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getEpicEntityId() {
/* 272 */     return this.epicEntityId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getMissionId() {
/* 277 */     return this.missionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getServerId() {
/* 282 */     return this.serverId;
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
/*     */   public final long getExpireTime() {
/* 295 */     return this.expireTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setExpireTime(long newExpireTime) {
/* 300 */     if (this.expireTime != newExpireTime) {
/*     */       
/* 302 */       this.expireTime = newExpireTime;
/* 303 */       update();
/*     */       
/* 305 */       if (!Servers.localServer.LOGINSERVER) {
/* 306 */         Players.getInstance().sendUpdateEpicMission(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final long getEndTime() {
/* 312 */     return this.endTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getMissionProgress() {
/* 317 */     return this.missionProgress;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getEntityName() {
/* 322 */     return this.epicEntityName;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getScenarioName() {
/* 327 */     return this.epicScenarioName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrent(boolean isCurrent) {
/* 332 */     this.current = isCurrent;
/* 333 */     update();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCurrent() {
/* 338 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 349 */     int prime = 31;
/* 350 */     int result = 1;
/* 351 */     result = 31 * result + this.epicEntityId;
/* 352 */     result = 31 * result + this.missionId;
/* 353 */     result = 31 * result + this.serverId;
/* 354 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 365 */     if (this == obj)
/*     */     {
/* 367 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 371 */     if (!(obj instanceof EpicMission))
/*     */     {
/* 373 */       return false;
/*     */     }
/* 375 */     EpicMission other = (EpicMission)obj;
/* 376 */     if (this.epicEntityId != other.epicEntityId || this.missionId != other.missionId || this.serverId != other.serverId)
/*     */     {
/* 378 */       return false;
/*     */     }
/* 380 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 391 */     StringBuilder lBuilder = new StringBuilder();
/* 392 */     lBuilder.append("EpicMission [epicEntityId=").append(this.epicEntityId);
/* 393 */     lBuilder.append(", epicScenarioId=").append(this.epicScenarioId);
/* 394 */     lBuilder.append(", epicEntityName=").append(this.epicEntityName);
/* 395 */     lBuilder.append(", epicScenarioName=").append(this.epicScenarioName);
/* 396 */     lBuilder.append(", missionId=").append(this.missionId);
/* 397 */     lBuilder.append(", serverId=").append(this.serverId);
/* 398 */     lBuilder.append(", timeStamp=").append(this.expireTime);
/* 399 */     lBuilder.append(", endTime=").append(this.endTime);
/* 400 */     lBuilder.append(", missionProgress=").append(this.missionProgress);
/* 401 */     lBuilder.append(", difficulty=").append(this.difficulty);
/* 402 */     lBuilder.append(", current=").append(this.current);
/* 403 */     lBuilder.append(", missionType=").append(this.missionType);
/* 404 */     lBuilder.append(']');
/* 405 */     return lBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMissionType() {
/* 410 */     return this.missionType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDifficulty() {
/* 415 */     return this.difficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRewards() {
/* 420 */     return this.rewards;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicMission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */