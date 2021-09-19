/*     */ package com.wurmonline.server.kingdom;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.webinterface.WcExpelMember;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public final class Kingdom
/*     */   implements MiscConstants, TimeConstants
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(Kingdom.class.getName());
/*     */   final byte kingdomId;
/*     */   private final byte template;
/*     */   private final String name;
/*     */   private final String chatname;
/*     */   private final String suffix;
/*     */   private final String password;
/*  57 */   private String firstMotto = "";
/*  58 */   private String secondMotto = "";
/*     */   
/*     */   private boolean existsHere = false;
/*     */   private boolean shouldBeDeleted = false;
/*     */   private final byte red;
/*     */   private final byte blue;
/*     */   private final byte green;
/*     */   public static final byte ALLIANCE_TYPE_NONE = 0;
/*     */   public static final byte ALLIANCE_TYPE_ALLIANCE = 1;
/*     */   public static final byte ALLIANCE_TYPE_SENT_REQUEST = 2;
/*  68 */   private static int winPoints = 0;
/*     */   
/*  70 */   private long startedDisbandWarning = 0L;
/*  71 */   public int activePremiums = 0;
/*     */   
/*     */   public boolean countedAtleastOnce = false;
/*     */   private boolean acceptsTransfers = true;
/*     */   private static final String LOAD_ALL_KINGDOMS = "SELECT * FROM KINGDOMS";
/*     */   private static final String LOAD_ALLIANCES = "SELECT * FROM KALLIANCES";
/*     */   private static final String INSERT_KINGDOM = "INSERT INTO KINGDOMS (KINGDOM, KINGDOMNAME,PASSWORD, TEMPLATE, SUFFIX, CHATNAME, FIRSTMOTTO,SECONDMOTTO,ACCEPTSTRANSFERS) VALUES (?,?,?,?,?,?,?,?,?)";
/*     */   private static final String UPDATE_KINGDOM = "UPDATE KINGDOMS SET KINGDOMNAME=?, PASSWORD=?,TEMPLATE=?, SUFFIX=?, CHATNAME=?, FIRSTMOTTO=?,SECONDMOTTO=?,ACCEPTSTRANSFERS=? WHERE KINGDOM=?";
/*     */   private static final String INSERT_ALLIANCE = "INSERT INTO KALLIANCES (ALLIANCETYPE,KINGDOMONE, KINGDOMTWO) VALUES (?,?,?)";
/*     */   private static final String UPDATE_ALLIANCE = "UPDATE KALLIANCES SET ALLIANCETYPE=? WHERE KINGDOMONE=? AND KINGDOMTWO=?";
/*     */   private static final String DELETE_ALLIANCE = "DELETE FROM KALLIANCES WHERE KINGDOMONE=? AND KINGDOMTWO=?";
/*     */   private static final String DELETE_ALL_ALLIANCE = "DELETE FROM KALLIANCES WHERE KINGDOMONE=? OR KINGDOMTWO=?";
/*     */   private static final String SET_ERA_NONE = "UPDATE KING_ERA SET KINGDOM=0 WHERE KINGDOM=?";
/*     */   private static final String SET_WINPOINTS = "UPDATE KINGDOMS SET WINPOINTS=? WHERE KINGDOM=?";
/*     */   private static final String GET_MEMBERS = "SELECT WURMID FROM PLAYERS WHERE KINGDOM=?";
/*  86 */   private static final Random colorRand = new Random();
/*     */   
/*  88 */   private Map<Byte, Byte> alliances = new HashMap<>();
/*  89 */   private List<Long> members = new LinkedList<>();
/*  90 */   private ArrayList<KingdomBuff> kingdomBuffs = new ArrayList<>();
/*     */   
/*     */   public int lastConfrontationTileX;
/*     */   
/*     */   public int lastConfrontationTileY;
/*  95 */   private long lastMemberLoad = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Kingdom(byte id, byte templateKingdom, String _name, String _password, String _chatName, String _suffix, String mottoOne, String mottoTwo, boolean acceptsPortals) {
/* 101 */     this.kingdomId = id;
/* 102 */     this.template = templateKingdom;
/* 103 */     this.name = _name;
/* 104 */     this.password = _password;
/* 105 */     this.chatname = _chatName;
/* 106 */     this.suffix = _suffix;
/* 107 */     this.firstMotto = mottoOne;
/* 108 */     this.secondMotto = mottoTwo;
/* 109 */     this.acceptsTransfers = acceptsPortals;
/* 110 */     colorRand.setSeed(this.name.hashCode());
/* 111 */     this.red = (byte)colorRand.nextInt(255);
/* 112 */     this.blue = (byte)colorRand.nextInt(255);
/* 113 */     this.green = (byte)colorRand.nextInt(255);
/* 114 */     loadAllMembers();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/* 119 */     return this.kingdomId;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getTemplate() {
/* 124 */     return this.template;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Byte, Byte> getAllianceMap() {
/* 129 */     return this.alliances;
/*     */   }
/*     */ 
/*     */   
/*     */   void setAlliances(Map<Byte, Byte> newAlliances) {
/* 134 */     this.alliances = newAlliances;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean existsHere() {
/* 139 */     return this.existsHere;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExistsHere(boolean exists) {
/* 144 */     if (this.existsHere != exists)
/* 145 */       Servers.loginServer.shouldResendKingdoms = true; 
/* 146 */     this.existsHere = exists;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 151 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 156 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getChatName() {
/* 161 */     return this.chatname;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSuffix() {
/* 166 */     return this.suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptsTransfers(boolean accepts) {
/* 172 */     if (!isCustomKingdom())
/*     */       return; 
/* 174 */     this.acceptsTransfers = accepts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptsTransfers() {
/* 183 */     return this.acceptsTransfers;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFirstMotto(String motto) {
/* 188 */     this.firstMotto = motto;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSecondMotto(String motto) {
/* 193 */     this.secondMotto = motto;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFirstMotto() {
/* 198 */     return this.firstMotto;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSecondMotto() {
/* 203 */     return this.secondMotto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isShouldBeDeleted() {
/* 213 */     return this.shouldBeDeleted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setShouldBeDeleted(boolean aShouldBeDeleted) {
/* 224 */     this.shouldBeDeleted = aShouldBeDeleted;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getColorRed() {
/* 229 */     return this.red;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getColorBlue() {
/* 234 */     return this.blue;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getColorGreen() {
/* 239 */     return this.green;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCustomKingdom() {
/* 244 */     return (this.kingdomId < 0 || this.kingdomId > 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disband() {
/* 251 */     Kingdoms.destroyTowersWithKingdom(this.kingdomId);
/* 252 */     Kingdom[] kingdomArr = Kingdoms.getAllKingdoms();
/* 253 */     for (Kingdom k2 : kingdomArr) {
/*     */       
/* 255 */       if (k2.getId() != getId())
/* 256 */         k2.removeKingdomFromAllianceMap(getId()); 
/*     */     } 
/* 258 */     King k = King.getKing(this.kingdomId);
/*     */ 
/*     */ 
/*     */     
/* 262 */     byte newKingdomId = (getTemplate() == 3) ? 3 : (Servers.localServer.isChallengeOrEpicServer() ? getTemplate() : 4);
/*     */ 
/*     */     
/* 265 */     if (k != null) {
/*     */       
/* 267 */       k.abdicate(true, true);
/* 268 */       Players.getInstance().convertFromKingdomToKingdom(this.kingdomId, newKingdomId);
/*     */     } 
/* 270 */     this.existsHere = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     PlayerInfo[] pinfs = PlayerInfoFactory.getPlayerInfos();
/* 276 */     for (PlayerInfo pinf : pinfs) {
/*     */       
/* 278 */       if (pinf.epicKingdom == this.kingdomId)
/* 279 */         pinf.setEpicLocation(newKingdomId, pinf.epicServerId); 
/* 280 */       if (pinf.getChaosKingdom() == this.kingdomId) {
/* 281 */         pinf.setChaosKingdom(newKingdomId);
/*     */       }
/*     */     } 
/* 284 */     delete();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllied(byte otherKingdom) {
/* 289 */     Byte b = this.alliances.get(Byte.valueOf(otherKingdom));
/* 290 */     if (b == null)
/* 291 */       return false; 
/* 292 */     return (b.byteValue() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSentRequestingAlliance(byte otherKingdom) {
/* 297 */     Byte b = this.alliances.get(Byte.valueOf(otherKingdom));
/* 298 */     if (b == null)
/* 299 */       return false; 
/* 300 */     return (b.byteValue() == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlliance(byte kingdId, byte allianceType) {
/* 309 */     Connection dbcon = null;
/* 310 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 313 */       dbcon = DbConnector.getZonesDbCon();
/* 314 */       if (allianceType == 0)
/*     */       {
/* 316 */         this.alliances.remove(Byte.valueOf(kingdId));
/* 317 */         ps = dbcon.prepareStatement("DELETE FROM KALLIANCES WHERE KINGDOMONE=? AND KINGDOMTWO=?");
/* 318 */         ps.setByte(1, this.kingdomId);
/* 319 */         ps.setByte(2, kingdId);
/* 320 */         ps.executeUpdate();
/*     */       }
/*     */       else
/*     */       {
/* 324 */         if (this.alliances.containsKey(Byte.valueOf(kingdId))) {
/*     */           
/* 326 */           ps = dbcon.prepareStatement("UPDATE KALLIANCES SET ALLIANCETYPE=? WHERE KINGDOMONE=? AND KINGDOMTWO=?");
/*     */         }
/*     */         else {
/*     */           
/* 330 */           ps = dbcon.prepareStatement("INSERT INTO KALLIANCES (ALLIANCETYPE,KINGDOMONE, KINGDOMTWO) VALUES (?,?,?)");
/*     */         } 
/* 332 */         ps.setByte(1, allianceType);
/* 333 */         ps.setByte(2, this.kingdomId);
/* 334 */         ps.setByte(3, kingdId);
/* 335 */         ps.executeUpdate();
/* 336 */         this.alliances.put(Byte.valueOf(kingdId), Byte.valueOf(allianceType));
/*     */       }
/*     */     
/* 339 */     } catch (SQLException sqex) {
/*     */       
/* 341 */       logger.log(Level.WARNING, "Failed to load kingdom: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 345 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 346 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void removeKingdomFromAllianceMap(byte kingdom) {
/* 352 */     this.alliances.remove(Byte.valueOf(kingdom));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllKingdoms() {
/* 357 */     logger.log(Level.INFO, "Loading all kingdoms.");
/* 358 */     long start = System.nanoTime();
/* 359 */     Connection dbcon = null;
/* 360 */     PreparedStatement ps = null;
/* 361 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 364 */       dbcon = DbConnector.getZonesDbCon();
/* 365 */       ps = dbcon.prepareStatement("SELECT * FROM KINGDOMS");
/* 366 */       rs = ps.executeQuery();
/* 367 */       while (rs.next())
/*     */       {
/*     */ 
/*     */         
/* 371 */         Kingdom k = new Kingdom(rs.getByte("KINGDOM"), rs.getByte("TEMPLATE"), rs.getString("KINGDOMNAME"), rs.getString("PASSWORD"), rs.getString("CHATNAME"), rs.getString("SUFFIX"), rs.getString("FIRSTMOTTO"), rs.getString("SECONDMOTTO"), rs.getBoolean("ACCEPTSTRANSFERS"));
/* 372 */         k.setWinpoints(rs.getInt("WINPOINTS"));
/* 373 */         Kingdoms.loadKingdom(k);
/*     */       }
/*     */     
/* 376 */     } catch (SQLException sqex) {
/*     */       
/* 378 */       logger.log(Level.WARNING, "Failed to load kingdom: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 382 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 383 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 385 */       long end = System.nanoTime();
/* 386 */       logger.info("Loaded kingdoms from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/* 388 */     loadAlliances();
/* 389 */     if (Kingdoms.numKingdoms() == 0) {
/* 390 */       Kingdoms.createBasicKingdoms();
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadAllMembers() {
/* 395 */     if (System.currentTimeMillis() - this.lastMemberLoad < 900000L) {
/*     */       return;
/*     */     }
/* 398 */     this.lastMemberLoad = System.currentTimeMillis();
/*     */ 
/*     */     
/* 401 */     if (!Servers.localServer.PVPSERVER || getId() == 4) {
/*     */       return;
/*     */     }
/* 404 */     logger.log(Level.INFO, "Loading all members for " + getName() + ".");
/* 405 */     long start = System.nanoTime();
/* 406 */     Connection dbcon = null;
/* 407 */     PreparedStatement ps = null;
/* 408 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 411 */       dbcon = DbConnector.getPlayerDbCon();
/* 412 */       ps = dbcon.prepareStatement("SELECT WURMID FROM PLAYERS WHERE KINGDOM=?");
/* 413 */       ps.setByte(1, this.kingdomId);
/* 414 */       rs = ps.executeQuery();
/* 415 */       while (rs.next())
/*     */       {
/* 417 */         long wurmId = rs.getLong("WURMID");
/* 418 */         if (wurmId == -10L)
/*     */           continue; 
/* 420 */         addMember(wurmId);
/*     */       }
/*     */     
/* 423 */     } catch (SQLException sqex) {
/*     */       
/* 425 */       logger.log(Level.WARNING, "Failed to load kingdom members: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 429 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 430 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 432 */       long end = System.nanoTime();
/* 433 */       logger.info("Loaded " + this.members.size() + " kingdom members from database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final void loadAlliance(byte otherKingdom, byte allianceType) {
/* 439 */     logger.log(Level.INFO, "Alliance between " + getId() + " and " + otherKingdom + ":" + allianceType);
/* 440 */     this.alliances.put(Byte.valueOf(otherKingdom), Byte.valueOf(allianceType));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void loadAlliances() {
/* 445 */     Connection dbcon = null;
/* 446 */     PreparedStatement ps = null;
/* 447 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 450 */       dbcon = DbConnector.getZonesDbCon();
/* 451 */       ps = dbcon.prepareStatement("SELECT * FROM KALLIANCES");
/* 452 */       rs = ps.executeQuery();
/* 453 */       while (rs.next())
/*     */       {
/* 455 */         Kingdom k = Kingdoms.getKingdom(rs.getByte("KINGDOMONE"));
/* 456 */         if (k != null)
/*     */         {
/* 458 */           k.loadAlliance(rs.getByte("KINGDOMTWO"), rs.getByte("ALLIANCETYPE"));
/*     */         }
/*     */       }
/*     */     
/* 462 */     } catch (SQLException sqex) {
/*     */       
/* 464 */       logger.log(Level.WARNING, "Failed to load alliances: " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 468 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 469 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete() {
/* 475 */     logger.log(Level.INFO, "Deleting " + this.kingdomId + ", " + this.name);
/* 476 */     Connection dbcon = null;
/* 477 */     PreparedStatement ps = null;
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
/*     */     try {
/* 496 */       dbcon = DbConnector.getZonesDbCon();
/* 497 */       ps = dbcon.prepareStatement("DELETE FROM KALLIANCES WHERE KINGDOMONE=? OR KINGDOMTWO=?");
/* 498 */       ps.setByte(1, this.kingdomId);
/* 499 */       ps.setByte(2, this.kingdomId);
/* 500 */       ps.executeUpdate();
/*     */     }
/* 502 */     catch (SQLException sqex) {
/*     */       
/* 504 */       logger.log(Level.WARNING, "Failed to delete all alliances for " + this.kingdomId + ", " + this.name + " : " + sqex
/* 505 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 509 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 510 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */     
/*     */     try {
/* 514 */       dbcon = DbConnector.getZonesDbCon();
/* 515 */       ps = dbcon.prepareStatement("UPDATE KING_ERA SET KINGDOM=0 WHERE KINGDOM=?");
/* 516 */       ps.setByte(1, this.kingdomId);
/* 517 */       ps.executeUpdate();
/*     */     }
/* 519 */     catch (SQLException sqex) {
/*     */       
/* 521 */       logger.log(Level.WARNING, "Failed to update king era set to none for " + this.kingdomId + ", " + this.name + " : " + sqex
/* 522 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 526 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 527 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 529 */     King.setToNoKingdom(this.kingdomId);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updatePointsDB() {
/* 534 */     Connection dbcon = null;
/* 535 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 538 */       dbcon = DbConnector.getZonesDbCon();
/* 539 */       ps = dbcon.prepareStatement("UPDATE KINGDOMS SET WINPOINTS=? WHERE KINGDOM=?");
/* 540 */       ps.setInt(1, winPoints);
/* 541 */       ps.setByte(2, this.kingdomId);
/* 542 */       ps.executeUpdate();
/*     */     }
/* 544 */     catch (SQLException sqex) {
/*     */       
/* 546 */       logger.log(Level.WARNING, "Failed to update king era set to none for " + this.kingdomId + ", " + this.name + " : " + sqex
/* 547 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 551 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 552 */       DbConnector.returnConnection(dbcon);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 592 */     Connection dbcon = null;
/* 593 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 596 */       dbcon = DbConnector.getZonesDbCon();
/* 597 */       ps = dbcon.prepareStatement("UPDATE KINGDOMS SET KINGDOMNAME=?, PASSWORD=?,TEMPLATE=?, SUFFIX=?, CHATNAME=?, FIRSTMOTTO=?,SECONDMOTTO=?,ACCEPTSTRANSFERS=? WHERE KINGDOM=?");
/* 598 */       ps.setString(1, this.name);
/* 599 */       ps.setString(2, this.password);
/* 600 */       ps.setByte(3, this.template);
/* 601 */       ps.setString(4, this.suffix);
/* 602 */       ps.setString(5, this.chatname);
/* 603 */       ps.setString(6, this.firstMotto);
/* 604 */       ps.setString(7, this.secondMotto);
/* 605 */       ps.setBoolean(8, this.acceptsTransfers);
/* 606 */       ps.setByte(9, this.kingdomId);
/* 607 */       ps.executeUpdate();
/*     */     }
/* 609 */     catch (SQLException sqex) {
/*     */       
/* 611 */       logger.log(Level.WARNING, "Failed to delete kingdom " + this.kingdomId + ", " + this.name + " : " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 615 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 616 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void saveToDisk() {
/* 622 */     Connection dbcon = null;
/* 623 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 626 */       logger.log(Level.INFO, "Saving " + this.name + " id=" + this.kingdomId);
/* 627 */       dbcon = DbConnector.getZonesDbCon();
/* 628 */       ps = dbcon.prepareStatement("INSERT INTO KINGDOMS (KINGDOM, KINGDOMNAME,PASSWORD, TEMPLATE, SUFFIX, CHATNAME, FIRSTMOTTO,SECONDMOTTO,ACCEPTSTRANSFERS) VALUES (?,?,?,?,?,?,?,?,?)");
/* 629 */       ps.setByte(1, this.kingdomId);
/* 630 */       ps.setString(2, this.name);
/* 631 */       ps.setString(3, this.password);
/* 632 */       ps.setByte(4, this.template);
/* 633 */       ps.setString(5, this.suffix);
/* 634 */       ps.setString(6, this.chatname);
/* 635 */       ps.setString(7, this.firstMotto);
/* 636 */       ps.setString(8, this.secondMotto);
/* 637 */       ps.setBoolean(9, this.acceptsTransfers);
/* 638 */       ps.executeUpdate();
/*     */     }
/* 640 */     catch (SQLException sqex) {
/*     */       
/* 642 */       logger.log(Level.WARNING, "Failed to save kingdom " + this.kingdomId + ", " + this.name + " : " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 646 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 647 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendDisbandTick() {
/* 653 */     if (getStartedDisbandWarning() == 0L) {
/* 654 */       setStartedDisbandWarning(System.currentTimeMillis());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 661 */     logger.log(Level.INFO, "The appointments of " + getName() + " does not work because of low population.");
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
/*     */   public int getWinpoints() {
/* 673 */     return winPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWinpoints(int newpoints) {
/* 684 */     winPoints = newpoints;
/* 685 */     updatePointsDB();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addWinpoints(int pointsAdded) {
/* 696 */     winPoints += pointsAdded;
/* 697 */     updatePointsDB();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMember(long wurmId) {
/* 702 */     PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/*     */ 
/*     */     
/* 705 */     if (p == null) {
/*     */       return;
/*     */     }
/*     */     
/* 709 */     if (p.getPower() != 0) {
/*     */       return;
/*     */     }
/* 712 */     if (this.members.contains(new Long(wurmId)))
/*     */       return; 
/* 714 */     this.members.add(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeMember(long wurmId) {
/* 719 */     if (!this.members.contains(new Long(wurmId)))
/*     */       return; 
/* 721 */     this.members.remove(new Long(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public final PlayerInfo getMember(long wurmId) {
/* 726 */     if (!this.members.contains(new Long(wurmId))) {
/* 727 */       return null;
/*     */     }
/* 729 */     return PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/*     */   }
/*     */ 
/*     */   
/*     */   public final PlayerInfo[] getAllMembers() {
/* 734 */     if (this.members.size() == 0) {
/*     */       
/* 736 */       logger.log(Level.WARNING, "No members to return for kingdom id " + getId() + "!");
/* 737 */       return new PlayerInfo[0];
/*     */     } 
/*     */     
/* 740 */     List<PlayerInfo> m = new LinkedList<>();
/* 741 */     for (Iterator<Long> iterator = this.members.iterator(); iterator.hasNext(); ) { long w = ((Long)iterator.next()).longValue();
/*     */       
/* 743 */       PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(w);
/* 744 */       if (p != null) {
/* 745 */         m.add(p); continue;
/*     */       } 
/* 747 */       logger.log(Level.WARNING, w + " returns null player info!"); }
/*     */     
/* 749 */     return m.<PlayerInfo>toArray(new PlayerInfo[m.size()]);
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
/*     */   public void expelMember(Creature performer, String ostra) {
/* 767 */     boolean isOnline = true;
/* 768 */     Player p = Players.getInstance().getPlayerOrNull(LoginHandler.raiseFirstLetter(ostra));
/* 769 */     byte toKingdom = (Servers.localServer.EPIC || getTemplate() == 3) ? getTemplate() : 4;
/* 770 */     PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithName(LoginHandler.raiseFirstLetter(ostra));
/* 771 */     if (pInfo == null) {
/*     */       
/* 773 */       performer.getCommunicator().sendNormalServerMessage("That player does not exist.", (byte)3);
/*     */       return;
/*     */     } 
/* 776 */     if (pInfo.realdeath > 0) {
/*     */       
/* 778 */       performer.getCommunicator().sendNormalServerMessage("You cannot expel a champion of your kingdom.", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/* 782 */     if (p == null) {
/*     */       
/* 784 */       PlayerState ps = PlayerInfoFactory.getPlayerState(pInfo.wurmId);
/* 785 */       if (ps == null || ps.getServerId() != Servers.localServer.getId()) {
/* 786 */         WcExpelMember wcx = new WcExpelMember(pInfo.wurmId, getId(), toKingdom, Servers.localServer.getId());
/* 787 */         if (!Servers.isThisLoginServer()) {
/* 788 */           wcx.sendToLoginServer();
/*     */         } else {
/* 790 */           wcx.sendFromLoginServer();
/*     */         } 
/* 792 */       }  isOnline = false;
/*     */       
/*     */       try {
/* 795 */         pInfo.load();
/* 796 */         p = new Player(pInfo);
/*     */       }
/* 798 */       catch (Exception ex) {
/*     */         
/* 800 */         performer.getCommunicator().sendNormalServerMessage("Failed to load '" + ostra + "' to expel, please /support.", (byte)3);
/*     */         
/* 802 */         ex.printStackTrace();
/*     */         return;
/*     */       } 
/*     */     } 
/* 806 */     if (p.getWurmId() == performer.getWurmId()) {
/*     */       
/* 808 */       performer.getCommunicator().sendNormalServerMessage("You cannot expel yourself!", (byte)3);
/*     */       return;
/*     */     } 
/* 811 */     if (p.getKingdomId() != getId()) {
/*     */       
/* 813 */       performer.getCommunicator().sendNormalServerMessage("Only " + p.getName() + "'s king may expel them.", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/* 817 */     Village village = p.getCitizenVillage();
/* 818 */     if (village != null && village.isMayor((Creature)p)) {
/*     */       
/* 820 */       performer.getCommunicator().sendNormalServerMessage("You cannot expel " + p.getName() + " as they are mayor of " + p
/* 821 */           .getVillageName() + ".", (byte)3);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 827 */       if (!p.setKingdomId(toKingdom, false, false, isOnline)) {
/*     */         
/* 829 */         performer.getCommunicator().sendNormalServerMessage("Unable to expel " + p.getName() + ", please /support.", (byte)3);
/*     */         
/*     */         return;
/*     */       } 
/* 833 */       if (isOnline)
/*     */       {
/* 835 */         p.getCommunicator().sendAlertServerMessage("You have been expelled from " + 
/* 836 */             getName() + "!");
/* 837 */         p.getCommunicator().sendAlertServerMessage("You better leave the kingdom immediately!");
/*     */       }
/*     */     
/*     */     }
/* 841 */     catch (IOException iox) {
/*     */       
/* 843 */       performer.getCommunicator().sendNormalServerMessage("Failed to expel '" + p.getName() + "', please /support.", (byte)3);
/*     */       
/* 845 */       iox.printStackTrace();
/*     */       return;
/*     */     } 
/* 848 */     performer.getCommunicator().sendSafeServerMessage("You successfully expel " + p.getName() + ". Let the dog run!");
/*     */     
/* 850 */     Message mess = new Message(performer, (byte)10, getChatName(), "<" + performer.getName() + "> expelled " + p.getName());
/* 851 */     Server.getInstance().addMessage(mess);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPremiumMemberCount() {
/* 857 */     this.activePremiums = 0;
/* 858 */     this.members.forEach(w -> {
/*     */           PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(w.longValue());
/*     */           if (p != null && p.isPaying())
/*     */             this.activePremiums++; 
/*     */         });
/* 863 */     return this.activePremiums;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getStartedDisbandWarning() {
/* 868 */     return this.startedDisbandWarning;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStartedDisbandWarning(long startedDisbandWarning) {
/* 873 */     this.startedDisbandWarning = startedDisbandWarning;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\Kingdom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */