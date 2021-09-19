/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.MapAnnotation;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*     */ 
/*     */ public class PvPAlliance
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(PvPAlliance.class.getName());
/*     */   
/*     */   private static final String LOAD_ENTRIES = "SELECT * FROM PVPALLIANCE";
/*     */   
/*     */   private static final String CREATE_ENTRY = "INSERT INTO PVPALLIANCE (ALLIANCENUMBER,NAME) VALUES(?,?)";
/*     */   
/*     */   private static final String DELETE_ENTRY = "DELETE FROM PVPALLIANCE WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_NAME = "UPDATE PVPALLIANCE SET NAME=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_WINS = "UPDATE PVPALLIANCE SET WINS=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_DEITYONE = "UPDATE PVPALLIANCE SET DEITYONE=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_DEITYTWO = "UPDATE PVPALLIANCE SET DEITYTWO=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_MOTD = "UPDATE PVPALLIANCE SET MOTD=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String UPDATE_ALLIANCENUMBER = "UPDATE PVPALLIANCE SET ALLIANCENUMBER=? WHERE ALLIANCENUMBER=?";
/*     */   private static final String GET_ALLIANCE_MAP_POI = "SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=2 AND OWNERID=?";
/*     */   private static final String DELETE_ALLIANCE_MAP_POIS = "DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=2;";
/*  64 */   private String name = "unknown";
/*     */ 
/*     */   
/*  67 */   private int idNumber = 0;
/*     */ 
/*     */   
/*  70 */   private int wins = 0;
/*     */ 
/*     */   
/*  73 */   private byte deityOneId = 0;
/*     */ 
/*     */   
/*  76 */   private byte deityTwoId = 0;
/*     */   
/*  78 */   private static final ConcurrentHashMap<Integer, PvPAlliance> alliances = new ConcurrentHashMap<>();
/*     */   
/*  80 */   private final CopyOnWriteArraySet<AllianceWar> wars = new CopyOnWriteArraySet<>();
/*  81 */   private static final PvPAlliance[] emptyAlliances = new PvPAlliance[0];
/*  82 */   private static final AllianceWar[] emptyWars = new AllianceWar[0];
/*  83 */   private String motd = "";
/*     */   
/*  85 */   private final Set<MapAnnotation> mapAnnotations = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PvPAlliance(int id, String allianceName) {
/*  91 */     this.idNumber = id;
/*  92 */     this.name = allianceName.substring(0, Math.min(allianceName.length(), 20));
/*  93 */     create();
/*  94 */     addAlliance(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addHotaWin() {
/*  99 */     setWins(this.wins + 1);
/* 100 */     for (Village vill : getVillages()) {
/*     */       
/* 102 */       vill.addHotaWin();
/* 103 */       if (vill.getAllianceNumber() == vill.getId()) {
/* 104 */         vill.createHotaPrize(getNumberOfWins());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAllianceWar(AllianceWar toadd) {
/* 110 */     this.wars.add(toadd);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeWar(AllianceWar war) {
/* 115 */     this.wars.remove(war);
/*     */   }
/*     */ 
/*     */   
/*     */   public final AllianceWar getWarWith(int allianceNum) {
/* 120 */     for (AllianceWar war : this.wars) {
/*     */       
/* 122 */       if (war.getAggressor() == allianceNum || war.getDefender() == allianceNum)
/* 123 */         return war; 
/*     */     } 
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final AllianceWar[] getWars() {
/* 130 */     if (this.wars.isEmpty())
/* 131 */       return emptyWars; 
/* 132 */     return this.wars.<AllianceWar>toArray(new AllianceWar[this.wars.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PvPAlliance[] getAllAlliances() {
/* 137 */     if (alliances.size() > 0)
/* 138 */       return (PvPAlliance[])alliances.values().toArray((Object[])new PvPAlliance[alliances.size()]); 
/* 139 */     return emptyAlliances;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAtWarWith(int allianceNum) {
/* 144 */     if (allianceNum > 0)
/* 145 */       for (AllianceWar war : this.wars) {
/*     */         
/* 147 */         if (war.getAggressor() == allianceNum || war.getDefender() == allianceNum)
/*     */         {
/* 149 */           return war.isActive();
/*     */         }
/*     */       }  
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public PvPAlliance(int id, String allianceName, byte deityOne, byte deityTwo, int _wins, String _motd) {
/* 157 */     this.idNumber = id;
/* 158 */     this.name = allianceName.substring(0, Math.min(allianceName.length(), 20));
/* 159 */     this.deityOneId = deityOne;
/* 160 */     this.deityTwoId = deityTwo;
/* 161 */     this.wins = _wins;
/* 162 */     this.motd = _motd;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void addAlliance(PvPAlliance alliance) {
/* 167 */     alliances.put(Integer.valueOf(alliance.getId()), alliance);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PvPAlliance getPvPAlliance(int allianceId) {
/* 172 */     return alliances.get(Integer.valueOf(allianceId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadPvPAlliances() {
/* 177 */     logger.log(Level.INFO, "Loading all PVP Alliances.");
/* 178 */     long start = System.nanoTime();
/* 179 */     Connection dbcon = null;
/* 180 */     PreparedStatement ps = null;
/* 181 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 184 */       dbcon = DbConnector.getZonesDbCon();
/* 185 */       ps = dbcon.prepareStatement("SELECT * FROM PVPALLIANCE");
/* 186 */       rs = ps.executeQuery();
/* 187 */       while (rs.next())
/*     */       {
/* 189 */         int allianceId = rs.getInt("ALLIANCENUMBER");
/* 190 */         String name = rs.getString("NAME");
/* 191 */         byte deityOne = rs.getByte("DEITYONE");
/* 192 */         byte deityTwo = rs.getByte("DEITYTWO");
/* 193 */         int wins = rs.getInt("WINS");
/* 194 */         String motd = rs.getString("MOTD");
/* 195 */         PvPAlliance toAdd = new PvPAlliance(allianceId, name, deityOne, deityTwo, wins, motd);
/* 196 */         toAdd.loadAllianceMapAnnotations();
/* 197 */         addAlliance(toAdd);
/*     */       }
/*     */     
/* 200 */     } catch (SQLException sqx) {
/*     */       
/* 202 */       logger.log(Level.WARNING, "Failed to load pvp alliances " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 206 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 207 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 209 */       long end = System.nanoTime();
/* 210 */       logger.info("Loaded PVP Alliances from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void loadAllianceMapAnnotations() {
/* 217 */     Connection dbcon = null;
/* 218 */     PreparedStatement ps = null;
/* 219 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 222 */       dbcon = DbConnector.getPlayerDbCon();
/* 223 */       ps = dbcon.prepareStatement("SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=2 AND OWNERID=?");
/* 224 */       ps.setLong(1, getId());
/* 225 */       rs = ps.executeQuery();
/* 226 */       while (rs.next())
/*     */       {
/* 228 */         long wid = rs.getLong("ID");
/* 229 */         String poiName = rs.getString("NAME");
/* 230 */         long position = rs.getLong("POSITION");
/* 231 */         byte type = rs.getByte("POITYPE");
/* 232 */         long ownerId = rs.getLong("OWNERID");
/* 233 */         String server = rs.getString("SERVER");
/* 234 */         byte icon = rs.getByte("ICON");
/* 235 */         addAllianceMapAnnotation(new MapAnnotation(wid, poiName, type, position, ownerId, server, icon), false);
/*     */       }
/*     */     
/* 238 */     } catch (SQLException sqx) {
/*     */       
/* 240 */       logger.log(Level.WARNING, "Problem loading all alliance POI's for alliance nr: " + 
/* 241 */           getId() + " - " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 245 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 246 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 252 */     return this.idNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 257 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumberOfWins() {
/* 262 */     return this.wins;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getFavoredDeityOne() {
/* 267 */     return this.deityOneId;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getFavoredDeityTwo() {
/* 272 */     return this.deityTwoId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMotd() {
/* 277 */     return this.motd;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Message getMotdMessage() {
/* 282 */     return new Message(null, (byte)15, "Alliance", "MOTD: " + this.motd, 250, 150, 250);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void create() {
/* 287 */     Connection dbcon = null;
/* 288 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 291 */       dbcon = DbConnector.getZonesDbCon();
/* 292 */       ps = dbcon.prepareStatement("INSERT INTO PVPALLIANCE (ALLIANCENUMBER,NAME) VALUES(?,?)");
/* 293 */       ps.setInt(1, this.idNumber);
/* 294 */       ps.setString(2, this.name);
/* 295 */       ps.executeUpdate();
/*     */     }
/* 297 */     catch (SQLException sqx) {
/*     */       
/* 299 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 303 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 304 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean exists() {
/* 310 */     for (Village v : Villages.getVillages()) {
/*     */       
/* 312 */       if (v.getAllianceNumber() == getId() && v.getAllianceNumber() != v.getId())
/* 313 */         return true; 
/*     */     } 
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Village[] getVillages() {
/* 320 */     Set<Village> toReturn = new HashSet<>();
/* 321 */     for (Village v : Villages.getVillages()) {
/*     */       
/* 323 */       if (v.getAllianceNumber() == getId())
/* 324 */         toReturn.add(v); 
/*     */     } 
/* 326 */     return toReturn.<Village>toArray(new Village[toReturn.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Village getAllianceCapital() {
/* 331 */     Village[] villages = getVillages();
/* 332 */     for (Village village : villages) {
/*     */       
/* 334 */       if (village.getId() == village.getAllianceNumber())
/*     */       {
/* 336 */         return village;
/*     */       }
/*     */     } 
/*     */     
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void disband() {
/* 345 */     for (Village v : Villages.getVillages()) {
/*     */       
/* 347 */       if (v.getAllianceNumber() == getId()) {
/*     */         
/* 349 */         v.setAllianceNumber(0);
/* 350 */         v.sendClearMapAnnotationsOfType((byte)2);
/*     */       } 
/*     */     } 
/* 353 */     delete();
/* 354 */     deleteAllianceMapAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void deleteAllianceMapAnnotations() {
/* 359 */     Connection dbcon = null;
/* 360 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 363 */       dbcon = DbConnector.getPlayerDbCon();
/* 364 */       ps = dbcon.prepareStatement("DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=2;");
/* 365 */       ps.setLong(1, this.idNumber);
/* 366 */       ps.executeUpdate();
/*     */     }
/* 368 */     catch (SQLException sqx) {
/*     */       
/* 370 */       logger.log(Level.WARNING, "Failed to delete alliance map annotations with alliance id=" + this.idNumber, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 374 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 375 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void delete() {
/* 381 */     alliances.remove(Integer.valueOf(this.idNumber));
/* 382 */     Connection dbcon = null;
/* 383 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 386 */       dbcon = DbConnector.getZonesDbCon();
/* 387 */       ps = dbcon.prepareStatement("DELETE FROM PVPALLIANCE WHERE ALLIANCENUMBER=?");
/* 388 */       ps.setInt(1, this.idNumber);
/* 389 */       ps.executeUpdate();
/*     */     }
/* 391 */     catch (SQLException sqx) {
/*     */       
/* 393 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 397 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 398 */       DbConnector.returnConnection(dbcon);
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
/*     */   public void setName(@Nullable Creature changer, String aName) {
/* 410 */     if (!this.name.equals(aName)) {
/*     */       
/* 412 */       this.name = aName.substring(0, Math.min(aName.length(), 20));
/* 413 */       Connection dbcon = null;
/* 414 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 417 */         dbcon = DbConnector.getZonesDbCon();
/* 418 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET NAME=? WHERE ALLIANCENUMBER=?");
/* 419 */         ps.setString(1, aName);
/* 420 */         ps.setInt(2, this.idNumber);
/* 421 */         ps.executeUpdate();
/*     */       }
/* 423 */       catch (SQLException sqx) {
/*     */         
/* 425 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 429 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 430 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/* 432 */       if (changer != null) {
/* 433 */         broadCastAllianceMessage(changer, " changed the name of the alliance to " + getName() + ".");
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
/*     */   public void setIdNumber(int aIdNumber) {
/* 445 */     if (this.idNumber != aIdNumber) {
/*     */       
/* 447 */       Connection dbcon = null;
/* 448 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 451 */         dbcon = DbConnector.getZonesDbCon();
/* 452 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET ALLIANCENUMBER=? WHERE ALLIANCENUMBER=?");
/* 453 */         ps.setInt(1, aIdNumber);
/* 454 */         ps.setInt(2, this.idNumber);
/* 455 */         ps.executeUpdate();
/* 456 */         alliances.remove(Integer.valueOf(this.idNumber));
/* 457 */         this.idNumber = aIdNumber;
/* 458 */         addAlliance(this);
/*     */       }
/* 460 */       catch (SQLException sqx) {
/*     */         
/* 462 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 466 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 467 */         DbConnector.returnConnection(dbcon);
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
/*     */   public void setWins(int aWins) {
/* 480 */     if (this.wins != aWins) {
/*     */       
/* 482 */       this.wins = aWins;
/* 483 */       Connection dbcon = null;
/* 484 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 487 */         dbcon = DbConnector.getZonesDbCon();
/* 488 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET WINS=? WHERE ALLIANCENUMBER=?");
/* 489 */         ps.setInt(1, aWins);
/* 490 */         ps.setInt(2, this.idNumber);
/* 491 */         ps.executeUpdate();
/*     */       }
/* 493 */       catch (SQLException sqx) {
/*     */         
/* 495 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 499 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 500 */         DbConnector.returnConnection(dbcon);
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
/*     */   public void setDeityOneId(byte aDeityOneId) {
/* 513 */     if (this.deityOneId != aDeityOneId) {
/*     */       
/* 515 */       this.deityOneId = aDeityOneId;
/* 516 */       Connection dbcon = null;
/* 517 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 520 */         dbcon = DbConnector.getZonesDbCon();
/* 521 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET DEITYONE=? WHERE ALLIANCENUMBER=?");
/* 522 */         ps.setByte(1, aDeityOneId);
/* 523 */         ps.setInt(2, this.idNumber);
/* 524 */         ps.executeUpdate();
/*     */       }
/* 526 */       catch (SQLException sqx) {
/*     */         
/* 528 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 532 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 533 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void broadCastMessage(Message message) {
/* 540 */     for (Village v : getVillages())
/*     */     {
/* 542 */       v.broadCastMessage(message, false);
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
/*     */   public void setDeityTwoId(byte aDeityTwoId) {
/* 554 */     if (this.deityTwoId != aDeityTwoId) {
/*     */       
/* 556 */       this.deityTwoId = aDeityTwoId;
/* 557 */       Connection dbcon = null;
/* 558 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 561 */         dbcon = DbConnector.getZonesDbCon();
/* 562 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET DEITYTWO=? WHERE ALLIANCENUMBER=?");
/* 563 */         ps.setByte(1, aDeityTwoId);
/* 564 */         ps.setInt(2, this.idNumber);
/* 565 */         ps.executeUpdate();
/*     */       }
/* 567 */       catch (SQLException sqx) {
/*     */         
/* 569 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 573 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 574 */         DbConnector.returnConnection(dbcon);
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
/*     */   public void setMotd(String aMotd) {
/* 586 */     if (this.motd != null && !this.motd.equals(aMotd)) {
/*     */       
/* 588 */       this.motd = aMotd;
/* 589 */       Connection dbcon = null;
/* 590 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 593 */         dbcon = DbConnector.getZonesDbCon();
/* 594 */         ps = dbcon.prepareStatement("UPDATE PVPALLIANCE SET MOTD=? WHERE ALLIANCENUMBER=?");
/* 595 */         ps.setString(1, aMotd);
/* 596 */         ps.setInt(2, this.idNumber);
/* 597 */         ps.executeUpdate();
/*     */       }
/* 599 */       catch (SQLException sqx) {
/*     */         
/* 601 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 605 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 606 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/* 608 */       if (aMotd != null && aMotd.length() > 0)
/*     */       {
/* 610 */         broadCastMessage(getMotdMessage());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void broadCastAllianceMessage(Creature sender, String toSend) {
/* 617 */     broadCastMessage(createAllianceMessage(sender, toSend));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Message createAllianceMessage(Creature sender, String toSend) {
/* 622 */     return new Message(sender, (byte)15, "Alliance", "<" + sender.getName() + "> " + toSend);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Message createAllianceEventMessage(Creature sender, String toSend) {
/* 628 */     return new Message(sender, (byte)1, ":Event", "<" + sender.getName() + "> " + toSend);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void disband(Creature disbander) {
/* 634 */     for (Village v : getVillages()) {
/*     */       
/* 636 */       v.broadCastMessage(createAllianceEventMessage(disbander, "disbanded " + getName() + "."));
/* 637 */       v.setAllianceNumber(0);
/* 638 */       v.sendClearMapAnnotationsOfType((byte)2);
/*     */     } 
/* 640 */     delete();
/* 641 */     deleteAllianceMapAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void transferControl(Creature disbander, int newCapital) {
/*     */     try {
/* 648 */       Village vill = Villages.getVillage(newCapital);
/* 649 */       for (Village v : getVillages()) {
/*     */         
/* 651 */         v.broadCastMessage(createAllianceEventMessage(disbander, "transfers control of " + 
/* 652 */               getName() + " to " + vill.getName() + "."));
/* 653 */         v.setAllianceNumber(newCapital);
/*     */       } 
/* 655 */       setIdNumber(newCapital);
/*     */     
/*     */     }
/* 658 */     catch (NoSuchVillageException noSuchVillageException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean addAllianceMapAnnotation(MapAnnotation annotation, boolean send) {
/* 666 */     if (this.mapAnnotations.size() < 500) {
/*     */       
/* 668 */       this.mapAnnotations.add(annotation);
/* 669 */       if (send)
/*     */       {
/* 671 */         sendAddAnnotations(new MapAnnotation[] { annotation });
/*     */       }
/*     */       
/* 674 */       return true;
/*     */     } 
/* 676 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllianceMapAnnotation(MapAnnotation annotation) {
/* 681 */     if (this.mapAnnotations.contains(annotation)) {
/*     */       
/*     */       try {
/*     */         
/* 685 */         this.mapAnnotations.remove(annotation);
/* 686 */         MapAnnotation.deleteAnnotation(annotation.getId());
/* 687 */         sendRemoveAnnotation(annotation);
/*     */       }
/* 689 */       catch (IOException iex) {
/*     */         
/* 691 */         logger.log(Level.WARNING, "Error deleting alliance map annotation: " + annotation
/* 692 */             .getId() + " " + iex.getMessage(), iex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final void sendAddAnnotations(MapAnnotation[] annotations) {
/* 699 */     for (Village v : getVillages())
/*     */     {
/* 701 */       v.sendMapAnnotationsToVillagers(annotations);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final void sendRemoveAnnotation(MapAnnotation annotation) {
/* 707 */     for (Village v : getVillages())
/*     */     {
/* 709 */       v.sendRemoveMapAnnotationToVillagers(annotation);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendClearAllianceAnnotations() {
/* 715 */     for (Village village : getVillages())
/*     */     {
/* 717 */       village.sendClearMapAnnotationsOfType((byte)2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final Set<MapAnnotation> getAllianceMapAnnotations() {
/* 723 */     return this.mapAnnotations;
/*     */   }
/*     */ 
/*     */   
/*     */   public final MapAnnotation[] getAllianceMapAnnotationsArray() {
/* 728 */     if (this.mapAnnotations == null || this.mapAnnotations.size() == 0)
/* 729 */       return null; 
/* 730 */     MapAnnotation[] annos = new MapAnnotation[this.mapAnnotations.size()];
/* 731 */     this.mapAnnotations.toArray(annos);
/* 732 */     return annos;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\PvPAlliance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */