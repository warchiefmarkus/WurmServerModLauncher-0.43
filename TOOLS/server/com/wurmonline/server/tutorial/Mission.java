/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class Mission
/*     */   implements Comparable<Mission>
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(Mission.class.getName());
/*     */   
/*     */   private static final String UPDATE_MISSION = "UPDATE MISSIONS SET NAME=?,INSTRUCTION=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,MAXTIMESECS=?,SECONDCHANCE=?,MAYBERESTARTED=?,FAILONDEATH=?,GROUP_NAME=?,HIDDEN=? WHERE ID=?";
/*     */   
/*     */   private static final String CREATE_MISSION = "INSERT INTO MISSIONS (NAME,INSTRUCTION,INACTIVE,CREATOR,CREATEDDATE,LASTMODIFIER,MAXTIMESECS,SECONDCHANCE,MAYBERESTARTED,CREATORID,CREATORTYPE,FAILONDEATH,GROUP_NAME,HIDDEN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String DELETE_MISSION = "DELETE FROM MISSIONS WHERE ID=?";
/*     */   
/*     */   private int id;
/*     */   
/*     */   private String name;
/*     */   
/*  61 */   private String groupName = "";
/*     */   
/*  63 */   private String instruction = "unknown";
/*     */ 
/*     */   
/*     */   private boolean inActive = false;
/*     */   
/*     */   private boolean isHidden = false;
/*     */   
/*     */   private String missionCreatorName;
/*     */   
/*     */   private String createdDate;
/*     */   
/*     */   private String lastModifierName;
/*     */   
/*     */   private Timestamp lastModifiedDate;
/*     */   
/*  78 */   private int maxTimeSeconds = 0;
/*     */   
/*     */   private boolean hasSecondChance = false;
/*     */   
/*     */   private boolean mayBeRestarted = false;
/*     */   
/*  84 */   private long ownerId = 0L;
/*     */   
/*  86 */   private byte creatorType = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnDeath = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mission(String aMissionCreatorName, String aLastModifierName) {
/*  97 */     this.missionCreatorName = aMissionCreatorName;
/*  98 */     this.lastModifierName = aLastModifierName;
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
/*     */   Mission(int aId, String aName, String aInstruction, boolean aInActive, String aMissionCreatorName, String aCreatedDate, String aLastModifierName, Timestamp aLastModifiedDate, int aMaxTimeSeconds, boolean aMayBeRestarted) {
/* 128 */     this.id = aId;
/* 129 */     this.name = aName;
/* 130 */     this.instruction = aInstruction;
/* 131 */     this.inActive = aInActive;
/* 132 */     this.missionCreatorName = aMissionCreatorName;
/* 133 */     this.createdDate = aCreatedDate;
/* 134 */     this.lastModifierName = aLastModifierName;
/* 135 */     this.lastModifiedDate = aLastModifiedDate;
/* 136 */     this.maxTimeSeconds = aMaxTimeSeconds;
/* 137 */     this.mayBeRestarted = aMayBeRestarted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setId(int aId) {
/* 146 */     this.id = aId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 151 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInstruction() {
/* 156 */     return this.instruction;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 161 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMissionCreatorName() {
/* 166 */     return this.missionCreatorName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOwnerName() {
/* 171 */     if (getCreatorType() == 2)
/* 172 */       return "System"; 
/* 173 */     return PlayerInfoFactory.getPlayerName(getOwnerId());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastModifiedString() {
/* 178 */     return getLastModifierName() + ", " + getLastModifiedDate();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastModifierName() {
/* 183 */     return this.lastModifierName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSecondChance() {
/* 188 */     return this.hasSecondChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSecondChance(boolean restart) {
/* 193 */     this.hasSecondChance = restart;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInactive() {
/* 198 */     return this.inActive;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInactive(boolean inactive) {
/* 203 */     this.inActive = inactive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHidden() {
/* 208 */     return this.isHidden;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsHidden(boolean ishidden) {
/* 213 */     this.isHidden = ishidden;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTimeSeconds(int seconds) {
/* 218 */     this.maxTimeSeconds = seconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTimeSeconds() {
/* 223 */     return this.maxTimeSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreatorType(byte aCreatorType) {
/* 228 */     this.creatorType = aCreatorType;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getCreatorType() {
/* 233 */     return this.creatorType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 238 */     return this.groupName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwnerId(long aWurmId) {
/* 243 */     this.ownerId = aWurmId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMayBeRestarted(boolean restarted) {
/* 248 */     this.mayBeRestarted = restarted;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mayBeRestarted() {
/* 253 */     return this.mayBeRestarted;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFailOnDeath(boolean fail) {
/* 258 */     this.failOnDeath = fail;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFailOnDeath() {
/* 263 */     return this.failOnDeath;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getOwnerId() {
/* 268 */     return this.ownerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInstruction(String n) {
/* 273 */     this.instruction = n;
/* 274 */     this.instruction = this.instruction.substring(0, Math.min(this.instruction.length(), 400));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String n) {
/* 279 */     this.name = n;
/* 280 */     this.name = this.name.substring(0, Math.min(this.name.length(), 100));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public void setGroupName(String gn) {
/* 286 */     if (gn == null) {
/* 287 */       this.groupName = "";
/*     */     } else {
/* 289 */       this.groupName = gn.substring(0, Math.min(gn.length(), 20));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setMissionCreatorName(String cn) {
/* 294 */     this.missionCreatorName = cn;
/* 295 */     this.missionCreatorName = this.missionCreatorName.substring(0, Math.min(this.missionCreatorName.length(), 40));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastModifierName(String mn) {
/* 300 */     this.lastModifierName = mn;
/* 301 */     this.lastModifierName = this.lastModifierName.substring(0, Math.min(this.lastModifierName.length(), 40));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCreatedDate(String aCreatedDate) {
/* 310 */     this.createdDate = aCreatedDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCreatedDate() {
/* 315 */     DateFormat formatter = DateFormat.getDateInstance(2);
/*     */     
/*     */     try {
/* 318 */       Date date = formatter.parse(this.createdDate);
/* 319 */       return date.getTime();
/*     */     }
/* 321 */     catch (ParseException e) {
/*     */       
/* 323 */       logger.log(Level.WARNING, e.getMessage(), e);
/*     */ 
/*     */       
/* 326 */       return this.lastModifiedDate.getTime();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setLastModifiedDate(Timestamp aLastModifiedDate) {
/* 335 */     this.lastModifiedDate = aLastModifiedDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastModifiedAsLong() {
/* 340 */     return this.lastModifiedDate.getTime();
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
/*     */   public String getLastModifiedDate() {
/* 353 */     if (this.lastModifiedDate != null)
/*     */     {
/* 355 */       return DateFormat.getDateInstance(2).format(this.lastModifiedDate);
/*     */     }
/*     */ 
/*     */     
/* 359 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasTargetOf(long currentTargetId, Creature performer) {
/* 365 */     MissionTrigger[] triggers = MissionTriggers.getAllTriggers();
/* 366 */     if (getCreatorType() != 2 || performer.getPower() >= 5)
/*     */     {
/* 368 */       if (performer.getPower() > 0 || getMissionCreatorName().equals(performer.getName()))
/*     */       {
/* 370 */         for (MissionTrigger mt : triggers) {
/*     */           
/* 372 */           if (mt.getMissionRequired() == this.id && mt.getTarget() == currentTargetId)
/*     */           {
/* 374 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     }
/* 379 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void update() {
/* 384 */     Connection dbcon = null;
/* 385 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 388 */       dbcon = DbConnector.getPlayerDbCon();
/* 389 */       ps = dbcon.prepareStatement("UPDATE MISSIONS SET NAME=?,INSTRUCTION=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,MAXTIMESECS=?,SECONDCHANCE=?,MAYBERESTARTED=?,FAILONDEATH=?,GROUP_NAME=?,HIDDEN=? WHERE ID=?");
/* 390 */       ps.setString(1, this.name);
/* 391 */       ps.setString(2, this.instruction);
/* 392 */       ps.setBoolean(3, this.inActive);
/* 393 */       ps.setString(4, this.missionCreatorName);
/* 394 */       ps.setString(5, this.createdDate);
/* 395 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 396 */       ps.setString(6, this.lastModifierName);
/* 397 */       ps.setInt(7, this.maxTimeSeconds);
/* 398 */       ps.setBoolean(8, this.hasSecondChance);
/* 399 */       ps.setBoolean(9, this.mayBeRestarted);
/* 400 */       ps.setBoolean(10, this.failOnDeath);
/* 401 */       ps.setString(11, this.groupName);
/* 402 */       ps.setBoolean(12, this.isHidden);
/*     */       
/* 404 */       ps.setInt(13, this.id);
/* 405 */       ps.executeUpdate();
/*     */     }
/* 407 */     catch (SQLException sqx) {
/*     */       
/* 409 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 413 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 414 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void create() {
/* 420 */     Connection dbcon = null;
/* 421 */     PreparedStatement ps = null;
/* 422 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 425 */       dbcon = DbConnector.getPlayerDbCon();
/* 426 */       ps = dbcon.prepareStatement("INSERT INTO MISSIONS (NAME,INSTRUCTION,INACTIVE,CREATOR,CREATEDDATE,LASTMODIFIER,MAXTIMESECS,SECONDCHANCE,MAYBERESTARTED,CREATORID,CREATORTYPE,FAILONDEATH,GROUP_NAME,HIDDEN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 427 */       ps.setString(1, this.name);
/* 428 */       ps.setString(2, this.instruction);
/* 429 */       ps.setBoolean(3, this.inActive);
/* 430 */       ps.setString(4, this.missionCreatorName);
/* 431 */       this.createdDate = DateFormat.getDateInstance(2).format(new Timestamp(System.currentTimeMillis()));
/* 432 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 433 */       ps.setString(5, this.createdDate);
/* 434 */       ps.setString(6, this.lastModifierName);
/* 435 */       ps.setInt(7, this.maxTimeSeconds);
/* 436 */       ps.setBoolean(8, this.hasSecondChance);
/* 437 */       ps.setBoolean(9, this.mayBeRestarted);
/* 438 */       ps.setLong(10, this.ownerId);
/* 439 */       ps.setByte(11, this.creatorType);
/* 440 */       ps.setBoolean(12, this.failOnDeath);
/* 441 */       ps.setString(13, this.groupName);
/* 442 */       ps.setBoolean(14, this.isHidden);
/*     */       
/* 444 */       ps.executeUpdate();
/* 445 */       rs = ps.getGeneratedKeys();
/* 446 */       if (rs.next())
/* 447 */         this.id = rs.getInt(1); 
/* 448 */       logger.log(Level.INFO, "Mission " + this.name + " (" + this.id + ") created at " + this.createdDate);
/*     */     }
/* 450 */     catch (SQLException sqx) {
/*     */       
/* 452 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 456 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 457 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void destroy() {
/* 465 */     Missions.removeMission(this.id);
/* 466 */     Connection dbcon = null;
/* 467 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 470 */       dbcon = DbConnector.getPlayerDbCon();
/* 471 */       ps = dbcon.prepareStatement("DELETE FROM MISSIONS WHERE ID=?");
/* 472 */       ps.setInt(1, this.id);
/* 473 */       ps.executeUpdate();
/*     */     }
/* 475 */     catch (SQLException sqx) {
/*     */       
/* 477 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 481 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 482 */       DbConnector.returnConnection(dbcon);
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
/*     */   public int compareTo(Mission aMission) {
/* 496 */     return getName().compareTo(aMission.getName());
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
/* 507 */     return "Mission [createdDate=" + this.createdDate + ", creatorType=" + this.creatorType + ", hasSecondChance=" + this.hasSecondChance + ", id=" + this.id + ", inActive=" + this.inActive + ", instruction=" + this.instruction + ", lastModifiedDate=" + this.lastModifiedDate + ", lastModifierName=" + this.lastModifierName + ", maxTimeSeconds=" + this.maxTimeSeconds + ", mayBeRestarted=" + this.mayBeRestarted + ", missionCreatorName=" + this.missionCreatorName + ", name=" + this.name + ", ownerId=" + this.ownerId + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRewards() {
/* 516 */     EpicMission em = EpicServerStatus.getEpicMissionForMission(this.id);
/* 517 */     if (em != null) {
/* 518 */       return em.getRewards();
/*     */     }
/* 520 */     return "Unknown";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getDifficulty() {
/* 525 */     EpicMission em = EpicServerStatus.getEpicMissionForMission(this.id);
/* 526 */     if (em != null) {
/* 527 */       return (byte)em.getDifficulty();
/*     */     }
/* 529 */     return -10;
/*     */   }
/*     */ 
/*     */   
/*     */   public MissionTrigger[] getTriggers() {
/* 534 */     return MissionTriggers.getMissionTriggers(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTriggers() {
/* 539 */     return MissionTriggers.hasMissionTriggers(this.id);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\Mission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */