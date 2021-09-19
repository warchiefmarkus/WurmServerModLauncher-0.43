/*      */ package com.wurmonline.server.villages;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Groups;
/*      */ import com.wurmonline.server.HistoryEvent;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.MapAnnotation;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.LinkedList;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class DbVillage
/*      */   extends Village
/*      */ {
/*   51 */   private static final Logger logger = Logger.getLogger(DbVillage.class.getName());
/*      */   
/*      */   private static final String CREATE_VILLAGE = "INSERT INTO VILLAGES (NAME,FOUNDER ,MAYOR ,CREATIONDATE, STARTX ,ENDX,STARTY ,ENDY, DEEDID, SURFACED, DEMOCRACY, DEVISE, HOMESTEAD, TOKEN,LASTLOGIN, KINGDOM,UPKEEP,ACCEPTSHOMESTEADS,PERMANENT,MERCHANTS,SPAWNKINGDOM,PERIMETER,MOTD) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String GET_CITIZENS = "SELECT * FROM CITIZENS WHERE VILLAGEID=?";
/*      */   
/*      */   private static final String GET_ROLES = "SELECT * FROM VILLAGEROLE WHERE VILLAGEID=?";
/*      */   
/*      */   private static final String SET_MAYOR = "UPDATE VILLAGES SET MAYOR=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DEVISE = "UPDATE VILLAGES SET DEVISE=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_STARTX = "UPDATE VILLAGES SET STARTX=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_STARTY = "UPDATE VILLAGES SET STARTY=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_ENDX = "UPDATE VILLAGES SET ENDX=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_ENDY = "UPDATE VILLAGES SET ENDY=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DEEDID = "UPDATE VILLAGES SET DEEDID=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_NAME = "UPDATE VILLAGES SET NAME=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DISBAND = "UPDATE VILLAGES SET DISBAND=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DISBANDER = "UPDATE VILLAGES SET DISBANDER=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_LASTLOGIN = "UPDATE VILLAGES SET LASTLOGIN=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_DEMOCRACY = "UPDATE VILLAGES SET DEMOCRACY=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_TOKEN = "UPDATE VILLAGES SET TOKEN=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAXCITIZENS = "UPDATE VILLAGES SET MAXCITIZENS=? WHERE ID=?";
/*      */   
/*      */   private static final String DELETE = "UPDATE VILLAGES SET DISBANDED=1 WHERE ID=?";
/*      */   
/*      */   private static final String SET_PERIMETER = "UPDATE VILLAGES SET PERIMETER=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_UPKEEP = "UPDATE VILLAGES SET UPKEEP=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_MAYPICKUP = "UPDATE VILLAGES SET MAYPICKUP=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_UNLIMITEDCITIZENS = "UPDATE VILLAGES SET ACCEPTSHOMESTEADS=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_ACCEPTSMERCHANTS = "UPDATE VILLAGES SET MERCHANTS=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_ALLOWSAGGRO = "UPDATE VILLAGES SET AGGROS=? WHERE ID=?";
/*      */   
/*      */   private static final String GET_REPUTATIONS = "SELECT REPUTATION,PERMANENT, WURMID FROM REPUTATION WHERE VILLAGEID=?";
/*      */   
/*      */   private static final String GET_GUARDS = "SELECT * FROM GUARDS WHERE VILLAGEID=?";
/*      */   
/*      */   private static final String ADD_HISTORY = "INSERT INTO HISTORY(EVENTDATE,VILLAGEID,PERFORMER,EVENT) VALUES (?,?,?,?)";
/*      */   
/*      */   private static final String GET_HISTORY = "SELECT EVENTDATE, VILLAGEID, PERFORMER, EVENT FROM HISTORY WHERE VILLAGEID=? ORDER BY EVENTDATE DESC";
/*      */   
/*      */   private static final String SET_KINGDOM = "UPDATE VILLAGES SET KINGDOM=? WHERE ID=?";
/*      */   
/*      */   private static final String SET_TWITTER = "UPDATE VILLAGES SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=?,TWITCHAT=?,TWITENABLE=? WHERE ID=?";
/*      */   
/*      */   private static final String UPDATE_FAITHWAR = "UPDATE VILLAGES SET FAITHWAR=? WHERE ID=?";
/*      */   private static final String UPDATE_FAITHHEAL = "UPDATE VILLAGES SET FAITHHEAL=? WHERE ID=?";
/*      */   private static final String UPDATE_FAITHCREATE = "UPDATE VILLAGES SET FAITHCREATE=? WHERE ID=?";
/*      */   private static final String SET_SPAWNSITUATION = "UPDATE VILLAGES SET SPAWNSITUATION=? WHERE ID=?";
/*      */   private static final String SET_ALLIANCENUMBER = "UPDATE VILLAGES SET ALLIANCENUMBER=? WHERE ID=?";
/*      */   private static final String SET_HOTAWINS = "UPDATE VILLAGES SET HOTAWINS=? WHERE ID=?";
/*      */   private static final String SET_LASTCHANGENAMED = "UPDATE VILLAGES SET NAMECHANGED=? WHERE ID=?";
/*      */   private static final String SET_MOTD = "UPDATE VILLAGES SET MOTD=? WHERE ID=?";
/*      */   private static final String SET_VILLAGEREP = "UPDATE VILLAGES SET VILLAGEREP=? WHERE ID=?";
/*      */   private static final String GET_VILLAGE_MAP_POI = "SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=1 AND OWNERID=?";
/*      */   private static final String DELETE_VILLAGE_MAP_POIS = "DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=1;";
/*      */   private static final String GET_RECRUITEES = "SELECT RECRUITEEID, RECRUITEENAME FROM VILLAGERECRUITEES WHERE VILLAGEID =?;";
/*      */   private static final String INSERT_RECRUITEE = "INSERT INTO VILLAGERECRUITEES(VILLAGEID, RECRUITEEID, RECRUITEENAME) VALUES (?,?,?)";
/*      */   private static final String DELETE_RECRUITEE = "DELETE FROM VILLAGERECRUITEES WHERE(VILLAGEID=? AND RECRUITEEID=?);";
/*      */   
/*      */   DbVillage(int aStartX, int aEndX, int aStartY, int aEndY, String aName, Creature aFounder, long aDeedid, boolean aSurfaced, boolean aDemocracy, String aDevise, boolean aPermanent, byte aSpawnKingdom, int initialPerimeter) throws NoSuchCreatureException, NoSuchPlayerException, IOException {
/*  129 */     super(aStartX, aEndX, aStartY, aEndY, aName, aFounder, aDeedid, aSurfaced, aDemocracy, aDevise, aPermanent, aSpawnKingdom, initialPerimeter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DbVillage(int aId, int aStartX, int aEndX, int aStartY, int aEndY, String aName, String aFounderName, String aMayor, long aDeedid, boolean aSurfaced, boolean aDemocracy, String aDevise, long aCreationDate, boolean aHomestead, long aTokenid, long aDisband, long aDisbander, long aLastlogin, byte aKingdom, long aUpkeep, byte aSettings, boolean aAcceptsHomesteads, boolean aAcceptsMerchants, int aMaxcitiz, boolean aPermanent, byte aSpawnKingdom, int perimetert, boolean aggros, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean _twitChat, boolean _twitEnabled, float _faithWar, float _faithHeal, float _faithCreate, byte _spawnSituation, int _allianceNumber, short _hotaWins, long lastChangeName, String _motd) {
/*  145 */     super(aId, aStartX, aEndX, aStartY, aEndY, aName, aFounderName, aMayor, aDeedid, aSurfaced, aDemocracy, aDevise, aCreationDate, aHomestead, aTokenid, aDisband, aDisbander, aLastlogin, aKingdom, aUpkeep, aSettings, aAcceptsHomesteads, aAcceptsMerchants, aMaxcitiz, aPermanent, aSpawnKingdom, perimetert, aggros, _consumerKeyToUse, _consumerSecretToUse, _applicationToken, _applicationSecret, _twitChat, _twitEnabled, _faithWar, _faithHeal, _faithCreate, _spawnSituation, _allianceNumber, _hotaWins, lastChangeName, _motd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int create() throws IOException {
/*  160 */     Connection dbcon = null;
/*  161 */     PreparedStatement ps = null;
/*  162 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  165 */       dbcon = DbConnector.getZonesDbCon();
/*  166 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGES (NAME,FOUNDER ,MAYOR ,CREATIONDATE, STARTX ,ENDX,STARTY ,ENDY, DEEDID, SURFACED, DEMOCRACY, DEVISE, HOMESTEAD, TOKEN,LASTLOGIN, KINGDOM,UPKEEP,ACCEPTSHOMESTEADS,PERMANENT,MERCHANTS,SPAWNKINGDOM,PERIMETER,MOTD) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/*  167 */       ps.setString(1, this.name);
/*  168 */       ps.setString(2, this.founderName);
/*  169 */       ps.setString(3, this.mayorName);
/*  170 */       ps.setLong(4, this.creationDate);
/*  171 */       ps.setInt(5, this.startx);
/*  172 */       ps.setInt(6, this.endx);
/*  173 */       ps.setInt(7, this.starty);
/*  174 */       ps.setInt(8, this.endy);
/*  175 */       ps.setLong(9, this.deedid);
/*  176 */       ps.setBoolean(10, this.surfaced);
/*  177 */       ps.setBoolean(11, this.democracy);
/*  178 */       ps.setString(12, this.motto);
/*  179 */       ps.setBoolean(13, false);
/*  180 */       ps.setLong(14, this.tokenId);
/*  181 */       ps.setLong(15, this.lastLogin);
/*  182 */       ps.setByte(16, this.kingdom);
/*  183 */       ps.setLong(17, this.upkeep);
/*  184 */       ps.setBoolean(18, this.unlimitedCitizens);
/*  185 */       ps.setBoolean(19, this.isPermanent);
/*  186 */       ps.setBoolean(20, this.acceptsMerchants);
/*      */       
/*  188 */       ps.setByte(21, this.spawnKingdom);
/*  189 */       ps.setInt(22, this.perimeterTiles);
/*  190 */       ps.setString(23, this.motd);
/*  191 */       ps.executeUpdate();
/*  192 */       rs = ps.getGeneratedKeys();
/*  193 */       if (rs.next()) {
/*  194 */         this.id = rs.getInt(1);
/*      */       }
/*  196 */     } catch (SQLException sqx) {
/*      */       
/*  198 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  202 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  203 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  205 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void loadVillageRecruitees() {
/*  211 */     Connection dbcon = null;
/*  212 */     PreparedStatement ps = null;
/*  213 */     ResultSet rs = null;
/*      */ 
/*      */     
/*      */     try {
/*  217 */       dbcon = DbConnector.getZonesDbCon();
/*  218 */       ps = dbcon.prepareStatement("SELECT RECRUITEEID, RECRUITEENAME FROM VILLAGERECRUITEES WHERE VILLAGEID =?;");
/*  219 */       ps.setInt(1, getId());
/*  220 */       rs = ps.executeQuery();
/*  221 */       while (rs.next())
/*      */       {
/*  223 */         long pid = rs.getLong("RECRUITEEID");
/*  224 */         String sName = rs.getString("RECRUITEENAME");
/*      */         
/*  226 */         addVillageRecruitee(new VillageRecruitee(getId(), pid, sName));
/*      */       }
/*      */     
/*  229 */     } catch (SQLException sqx) {
/*      */       
/*  231 */       logger.log(Level.WARNING, "Problem loading all village recruitees for village: " + getId() + " - " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  236 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  237 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void deleteRecruitee(VillageRecruitee vr) {
/*  244 */     Connection dbcon = null;
/*  245 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  248 */       dbcon = DbConnector.getZonesDbCon();
/*  249 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGERECRUITEES WHERE(VILLAGEID=? AND RECRUITEEID=?);");
/*  250 */       ps.setInt(1, vr.getVillageId());
/*  251 */       ps.setLong(2, vr.getRecruiteeId());
/*      */       
/*  253 */       ps.executeUpdate();
/*      */     
/*      */     }
/*  256 */     catch (SQLException sqx) {
/*      */       
/*  258 */       logger.log(Level.WARNING, "Problem deleting from recruitees: " + getId() + " - " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  263 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  264 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void loadVillageMapAnnotations() {
/*  272 */     Connection dbcon = null;
/*  273 */     PreparedStatement ps = null;
/*  274 */     ResultSet rs = null;
/*      */ 
/*      */     
/*      */     try {
/*  278 */       dbcon = DbConnector.getPlayerDbCon();
/*  279 */       ps = dbcon.prepareStatement("SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=1 AND OWNERID=?");
/*  280 */       ps.setLong(1, getId());
/*  281 */       rs = ps.executeQuery();
/*  282 */       while (rs.next())
/*      */       {
/*  284 */         long wid = rs.getLong("ID");
/*  285 */         String sName = rs.getString("NAME");
/*  286 */         long position = rs.getLong("POSITION");
/*  287 */         byte type = rs.getByte("POITYPE");
/*  288 */         long ownerId = rs.getLong("OWNERID");
/*  289 */         String server = rs.getString("SERVER");
/*  290 */         byte icon = rs.getByte("ICON");
/*  291 */         addVillageMapAnnotation(new MapAnnotation(wid, sName, type, position, ownerId, server, icon), false);
/*      */       }
/*      */     
/*  294 */     } catch (SQLException sqx) {
/*      */       
/*  296 */       logger.log(Level.WARNING, "Problem loading all village POI's for village: " + getId() + " - " + sqx.getMessage(), sqx);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  301 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  302 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void loadCitizens() {
/*  314 */     Connection dbcon = null;
/*  315 */     PreparedStatement ps = null;
/*  316 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  319 */       dbcon = DbConnector.getZonesDbCon();
/*  320 */       ps = dbcon.prepareStatement("SELECT * FROM CITIZENS WHERE VILLAGEID=?");
/*  321 */       ps.setInt(1, this.id);
/*  322 */       rs = ps.executeQuery();
/*  323 */       while (rs.next()) {
/*      */         
/*  325 */         long wurmid = rs.getLong("WURMID");
/*  326 */         int roleid = rs.getInt("ROLEID");
/*  327 */         long votedate = rs.getLong("VOTEDATE");
/*  328 */         if (votedate == 0L)
/*  329 */           votedate = -10L; 
/*  330 */         long votedfor = rs.getLong("VOTEDFOR");
/*  331 */         Citizen citizen = null;
/*  332 */         if (WurmId.getType(wurmid) == 0) {
/*      */ 
/*      */           
/*      */           try {
/*  336 */             String n = Players.getInstance().getNameFor(wurmid);
/*  337 */             citizen = new DbCitizen(wurmid, n, getRole(roleid), votedate, votedfor);
/*      */           }
/*  339 */           catch (NoSuchPlayerException nsp) {
/*      */             
/*      */             try
/*      */             {
/*      */ 
/*      */               
/*  345 */               Citizen.delete(wurmid);
/*      */             }
/*  347 */             catch (IOException iox2)
/*      */             {
/*  349 */               logger.log(Level.WARNING, "Failed to remove citiz " + wurmid, iox2);
/*      */             }
/*      */           
/*  352 */           } catch (IOException iox) {
/*      */             
/*  354 */             logger.log(Level.INFO, iox.getMessage(), iox);
/*      */           }
/*  356 */           catch (NoSuchRoleException nsr) {
/*      */             
/*  358 */             logger.log(Level.WARNING, nsr.getMessage(), (Throwable)nsr);
/*      */           } 
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/*  365 */             Creature c = Creatures.getInstance().getCreature(wurmid);
/*  366 */             citizen = new DbCitizen(wurmid, c.getName(), getRole(roleid), votedate, votedfor);
/*  367 */             c.setCitizenVillage(this);
/*      */           }
/*  369 */           catch (NoSuchCreatureException nsp) {
/*      */             
/*  371 */             logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*      */             
/*      */             try {
/*  374 */               Citizen.delete(wurmid);
/*      */             }
/*  376 */             catch (IOException iox2) {
/*      */               
/*  378 */               logger.log(Level.WARNING, "Failed to remove citiz " + wurmid, iox2);
/*      */             }
/*      */           
/*  381 */           } catch (NoSuchRoleException nsr) {
/*      */             
/*  383 */             logger.log(Level.WARNING, nsr.getMessage(), (Throwable)nsr);
/*      */           } 
/*      */         } 
/*  386 */         if (citizen != null) {
/*  387 */           this.citizens.put(new Long(citizen.getId()), citizen);
/*      */         }
/*      */       } 
/*  390 */     } catch (SQLException sqx) {
/*      */       
/*  392 */       logger.log(Level.WARNING, "Failed to load citizens for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  396 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  397 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayor(String aName) throws IOException {
/*  409 */     if (!this.mayorName.equals(aName)) {
/*      */       
/*  411 */       this.mayorName = aName;
/*  412 */       Connection dbcon = null;
/*  413 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  416 */         dbcon = DbConnector.getZonesDbCon();
/*  417 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET MAYOR=? WHERE ID=?");
/*  418 */         ps.setString(1, this.mayorName);
/*  419 */         ps.setInt(2, this.id);
/*  420 */         ps.executeUpdate();
/*      */       }
/*  422 */       catch (SQLException sqx) {
/*      */         
/*  424 */         logger.log(Level.WARNING, "Failed to set mayor name for village with id " + this.id, sqx);
/*  425 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  429 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  430 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */       
/*      */       try {
/*  434 */         addHistory(this.mayorName, "new " + getRoleForStatus((byte)2).getName());
/*      */       }
/*  436 */       catch (NoSuchRoleException nsr) {
/*      */         
/*  438 */         logger.log(Level.WARNING, getName() + " this village doesn't have the correct roles: " + nsr.getMessage(), (Throwable)nsr);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMotto(String devise) throws IOException {
/*  451 */     if (!this.motto.equals(devise)) {
/*      */       
/*  453 */       this.motto = devise;
/*  454 */       Connection dbcon = null;
/*  455 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  458 */         dbcon = DbConnector.getZonesDbCon();
/*  459 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET DEVISE=? WHERE ID=?");
/*  460 */         ps.setString(1, devise);
/*  461 */         ps.setInt(2, this.id);
/*  462 */         ps.executeUpdate();
/*      */       }
/*  464 */       catch (SQLException sqx) {
/*      */         
/*  466 */         logger.log(Level.WARNING, "Failed to set devise for village with id " + this.id, sqx);
/*  467 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  471 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  472 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVillageRep(int newRep) {
/*  485 */     if (Servers.localServer.HOMESERVER && Servers.localServer.EPIC)
/*      */     {
/*  487 */       if (newRep != this.villageReputation && newRep <= 150) {
/*      */         
/*  489 */         this.villageReputation = newRep;
/*  490 */         Connection dbcon = null;
/*  491 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/*  494 */           dbcon = DbConnector.getZonesDbCon();
/*  495 */           ps = dbcon.prepareStatement("UPDATE VILLAGES SET VILLAGEREP=? WHERE ID=?");
/*  496 */           ps.setInt(1, this.villageReputation);
/*  497 */           ps.setInt(2, this.id);
/*  498 */           ps.executeUpdate();
/*      */         }
/*  500 */         catch (SQLException sqx) {
/*      */           
/*  502 */           logger.log(Level.WARNING, "Failed to set reputation for village with id " + this.id, sqx);
/*      */         }
/*      */         finally {
/*      */           
/*  506 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  507 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*  509 */         boolean mayDeclare = (this.villageReputation > 100);
/*  510 */         String toSend = "Settlement reputation now at " + this.villageReputation + (mayDeclare ? "Other settlements may now declare war." : "");
/*      */         
/*  512 */         this.group.sendMessage(getRepMessage(toSend));
/*  513 */         if (twitChat()) {
/*  514 */           twit(toSend);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMotd(String newMotd) throws IOException {
/*  527 */     if (!this.motd.equals(newMotd)) {
/*      */       
/*  529 */       this.motd = newMotd;
/*  530 */       Connection dbcon = null;
/*  531 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  534 */         dbcon = DbConnector.getZonesDbCon();
/*  535 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET MOTD=? WHERE ID=?");
/*  536 */         ps.setString(1, newMotd);
/*  537 */         ps.setInt(2, this.id);
/*  538 */         ps.executeUpdate();
/*      */       }
/*  540 */       catch (SQLException sqx) {
/*      */         
/*  542 */         logger.log(Level.WARNING, "Failed to set motd for village with id " + this.id, sqx);
/*  543 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  547 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  548 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*  550 */       if (newMotd != null && newMotd.length() > 0) {
/*      */         
/*  552 */         this.group.sendMessage(getMotdMessage());
/*  553 */         if (twitChat()) {
/*  554 */           twit("MOTD:" + this.motd);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setStartX(int aStartx) throws IOException {
/*  567 */     if (this.startx != aStartx) {
/*      */       
/*  569 */       this.startx = aStartx;
/*  570 */       Connection dbcon = null;
/*  571 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  574 */         dbcon = DbConnector.getZonesDbCon();
/*  575 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET STARTX=? WHERE ID=?");
/*  576 */         ps.setInt(1, aStartx);
/*  577 */         ps.setInt(2, this.id);
/*  578 */         ps.executeUpdate();
/*      */       }
/*  580 */       catch (SQLException sqx) {
/*      */         
/*  582 */         logger.log(Level.WARNING, "Failed to set startx for village with id " + this.id, sqx);
/*  583 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  587 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  588 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String aName) throws IOException {
/*  601 */     if (!this.name.equals(aName)) {
/*      */       
/*  603 */       Groups.renameGroup(this.name, aName);
/*  604 */       this.name = aName;
/*  605 */       Connection dbcon = null;
/*  606 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  609 */         dbcon = DbConnector.getZonesDbCon();
/*  610 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET NAME=? WHERE ID=?");
/*  611 */         ps.setString(1, aName);
/*  612 */         ps.setInt(2, this.id);
/*  613 */         ps.executeUpdate();
/*      */       }
/*  615 */       catch (SQLException sqx) {
/*      */         
/*  617 */         logger.log(Level.WARNING, "Failed to set name for village with id " + this.id, sqx);
/*  618 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  622 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  623 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setEndX(int aEndx) throws IOException {
/*  636 */     if (this.endx != aEndx) {
/*      */       
/*  638 */       this.endx = aEndx;
/*  639 */       Connection dbcon = null;
/*  640 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  643 */         dbcon = DbConnector.getZonesDbCon();
/*  644 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET ENDX=? WHERE ID=?");
/*  645 */         ps.setInt(1, aEndx);
/*  646 */         ps.setInt(2, this.id);
/*  647 */         ps.executeUpdate();
/*      */       }
/*  649 */       catch (SQLException sqx) {
/*      */         
/*  651 */         logger.log(Level.WARNING, "Failed to set endx for village with id " + this.id, sqx);
/*  652 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  656 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  657 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setStartY(int aStarty) throws IOException {
/*  670 */     if (this.starty != aStarty) {
/*      */       
/*  672 */       this.starty = aStarty;
/*  673 */       Connection dbcon = null;
/*  674 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  677 */         dbcon = DbConnector.getZonesDbCon();
/*  678 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET STARTY=? WHERE ID=?");
/*  679 */         ps.setInt(1, aStarty);
/*  680 */         ps.setInt(2, this.id);
/*  681 */         ps.executeUpdate();
/*      */       }
/*  683 */       catch (SQLException sqx) {
/*      */         
/*  685 */         logger.log(Level.WARNING, "Failed to set starty for village with id " + this.id, sqx);
/*  686 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  690 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  691 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setEndY(int aEndy) throws IOException {
/*  704 */     if (this.endy != aEndy) {
/*      */       
/*  706 */       this.endy = aEndy;
/*  707 */       Connection dbcon = null;
/*  708 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  711 */         dbcon = DbConnector.getZonesDbCon();
/*  712 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET ENDY=? WHERE ID=?");
/*  713 */         ps.setInt(1, aEndy);
/*  714 */         ps.setInt(2, this.id);
/*  715 */         ps.executeUpdate();
/*      */       }
/*  717 */       catch (SQLException sqx) {
/*      */         
/*  719 */         logger.log(Level.WARNING, "Failed to set endy for village with id " + this.id, sqx);
/*  720 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  724 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  725 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setDeedId(long aDeedid) throws IOException {
/*  738 */     if (this.deedid != aDeedid) {
/*      */       
/*  740 */       this.deedid = aDeedid;
/*  741 */       Connection dbcon = null;
/*  742 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  745 */         dbcon = DbConnector.getZonesDbCon();
/*  746 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET DEEDID=? WHERE ID=?");
/*  747 */         ps.setLong(1, aDeedid);
/*  748 */         ps.setInt(2, this.id);
/*  749 */         ps.executeUpdate();
/*      */       }
/*  751 */       catch (SQLException sqx) {
/*      */         
/*  753 */         logger.log(Level.WARNING, "Failed to set deedid=" + aDeedid + " for village with id " + this.id, sqx);
/*  754 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  758 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  759 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDemocracy(boolean aDemocracy) throws IOException {
/*  772 */     if (this.democracy != aDemocracy) {
/*      */       
/*  774 */       this.democracy = aDemocracy;
/*  775 */       Connection dbcon = null;
/*  776 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  779 */         dbcon = DbConnector.getZonesDbCon();
/*  780 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET DEMOCRACY=? WHERE ID=?");
/*  781 */         ps.setBoolean(1, aDemocracy);
/*  782 */         ps.setInt(2, this.id);
/*  783 */         ps.executeUpdate();
/*      */       }
/*  785 */       catch (SQLException sqx) {
/*      */         
/*  787 */         logger.log(Level.WARNING, "Failed to set democracy=" + aDemocracy + " for village with id " + this.id, sqx);
/*  788 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  792 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  793 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*  795 */       if (aDemocracy) {
/*  796 */         addHistory(getName(), "is now a democracy");
/*      */       } else {
/*  798 */         addHistory(getName(), "is now a dictatorship");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void loadRoles() {
/*  810 */     Connection dbcon = null;
/*  811 */     PreparedStatement ps = null;
/*  812 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  815 */       dbcon = DbConnector.getZonesDbCon();
/*  816 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGEROLE WHERE VILLAGEID=?");
/*  817 */       ps.setInt(1, this.id);
/*  818 */       rs = ps.executeQuery();
/*  819 */       while (rs.next()) {
/*      */         
/*  821 */         int roleid = rs.getInt("ID");
/*  822 */         String roleName = rs.getString("NAME");
/*  823 */         boolean mayTerraform = rs.getBoolean("MAYTERRAFORM");
/*  824 */         boolean mayCuttrees = rs.getBoolean("MAYCUTTREE");
/*  825 */         boolean mayMine = rs.getBoolean("MAYMINE");
/*  826 */         boolean mayFarm = rs.getBoolean("MAYFARM");
/*  827 */         boolean mayBuild = rs.getBoolean("MAYBUILD");
/*  828 */         boolean mayHire = rs.getBoolean("MAYHIRE");
/*  829 */         boolean mayInvite = rs.getBoolean("MAYINVITE");
/*  830 */         boolean mayDestroy = rs.getBoolean("MAYDESTROY");
/*  831 */         boolean mayManageRoles = rs.getBoolean("MAYMANAGEROLES");
/*  832 */         boolean mayExpand = rs.getBoolean("MAYEXPAND");
/*  833 */         boolean mayPassAllFences = rs.getBoolean("MAYPASSFENCES");
/*  834 */         boolean mayLockFences = rs.getBoolean("MAYLOCKFENCES");
/*  835 */         boolean mayAttackCitizens = rs.getBoolean("MAYATTACKCITIZ");
/*  836 */         boolean mayAttackNonCitizens = rs.getBoolean("MAYATTACKNONCITIZ");
/*  837 */         boolean mayFish = rs.getBoolean("MAYFISH");
/*  838 */         boolean mayCutOldTrees = rs.getBoolean("MAYCUTOLD");
/*  839 */         boolean mayPushPullTurn = rs.getBoolean("MAYPUSHPULLTURN");
/*  840 */         boolean diplomat = rs.getBoolean("DIPLOMAT");
/*  841 */         byte status = rs.getByte("STATUS");
/*  842 */         int villageAppliedTo = rs.getInt("VILLAGEAPPLIEDTO");
/*  843 */         boolean mayUpdateMap = rs.getBoolean("MAYUPDATEMAP");
/*  844 */         boolean mayLead = rs.getBoolean("MAYLEAD");
/*  845 */         boolean mayPickup = rs.getBoolean("MAYPICKUP");
/*  846 */         boolean mayTame = rs.getBoolean("MAYTAME");
/*  847 */         boolean mayLoad = rs.getBoolean("MAYLOAD");
/*  848 */         boolean mayButcher = rs.getBoolean("MAYBUTCHER");
/*  849 */         boolean mayAttachLock = rs.getBoolean("MAYATTACHLOCK");
/*  850 */         boolean mayPickLocks = rs.getBoolean("MAYPICKLOCKS");
/*  851 */         long playerAppliedTo = rs.getLong("PLAYERAPPLIEDTO");
/*  852 */         int settings = rs.getInt("SETTINGS");
/*  853 */         int moreSettings = rs.getInt("MORESETTINGS");
/*  854 */         int extraSettings = rs.getInt("EXTRASETTINGS");
/*  855 */         VillageRole role = new DbVillageRole(roleid, this.id, roleName, mayTerraform, mayCuttrees, mayMine, mayFarm, mayBuild, mayHire, mayInvite, mayDestroy, mayManageRoles, mayExpand, mayPassAllFences, mayLockFences, mayAttackCitizens, mayAttackNonCitizens, mayFish, mayCutOldTrees, mayPushPullTurn, diplomat, status, villageAppliedTo, mayUpdateMap, mayLead, mayPickup, mayTame, mayLoad, mayButcher, mayAttachLock, mayPickLocks, playerAppliedTo, settings, moreSettings, extraSettings);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  861 */         if (role.getStatus() == 1 && (role.mayDestroy || role.mayDestroyAnyBuilding())) {
/*      */           
/*  863 */           logger.warning("Loading RoleID " + this.id + ": mayDestroy/mayDestroyAnyBuilding set on ROLE_EVERYBODY, defaulting to false");
/*      */           
/*      */           try {
/*  866 */             role.setMayDestroy(false);
/*  867 */             role.setCanDestroyAnyBuilding(false);
/*      */           }
/*  869 */           catch (IOException e) {
/*      */             
/*  871 */             e.printStackTrace();
/*      */           } 
/*      */         } 
/*      */         
/*  875 */         boolean insert = true;
/*  876 */         if (role.getStatus() == 1) {
/*      */           
/*      */           try {
/*      */             
/*  880 */             if (getRoleForStatus((byte)1) != null)
/*      */             {
/*  882 */               role.delete();
/*  883 */               logger.log(Level.INFO, "Deleted everybody role for " + getName());
/*  884 */               insert = false;
/*      */             }
/*      */           
/*  887 */           } catch (Exception exception) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  892 */         if (insert) {
/*  893 */           this.roles.put(Integer.valueOf(roleid), role);
/*      */         }
/*      */       } 
/*      */       try {
/*  897 */         this.everybody = getRoleForStatus((byte)1);
/*      */       }
/*  899 */       catch (Exception ex) {
/*      */         
/*  901 */         logger.log(Level.WARNING, getName() + " - role everybody doesn't exist. Creating.");
/*  902 */         createRoleEverybody();
/*      */       }
/*      */     
/*  905 */     } catch (SQLException sqx) {
/*      */       
/*  907 */       logger.log(Level.WARNING, "Failed to load roles for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  911 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  912 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTokenId(long tokenid) throws IOException {
/*  924 */     if (this.tokenId != tokenid) {
/*      */       
/*  926 */       this.tokenId = tokenid;
/*  927 */       Connection dbcon = null;
/*  928 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  931 */         dbcon = DbConnector.getZonesDbCon();
/*  932 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET TOKEN=? WHERE ID=?");
/*  933 */         ps.setLong(1, this.tokenId);
/*  934 */         ps.setInt(2, this.id);
/*  935 */         ps.executeUpdate();
/*      */       }
/*  937 */       catch (SQLException sqx) {
/*      */         
/*  939 */         logger.log(Level.WARNING, "Failed to set tokenid=" + this.tokenId + " for village with id " + this.id, sqx);
/*  940 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  944 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  945 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setMaxcitizens(int maxcitizens) {
/*  958 */     if (maxcitizens != this.maxCitizens) {
/*      */       
/*  960 */       this.maxCitizens = maxcitizens;
/*  961 */       Connection dbcon = null;
/*  962 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  965 */         dbcon = DbConnector.getZonesDbCon();
/*  966 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET MAXCITIZENS=? WHERE ID=?");
/*  967 */         ps.setInt(1, this.maxCitizens);
/*  968 */         ps.setInt(2, this.id);
/*  969 */         ps.executeUpdate();
/*      */       }
/*  971 */       catch (SQLException sqx) {
/*      */         
/*  973 */         logger.log(Level.WARNING, "Failed to set max citizens=" + this.maxCitizens + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/*  977 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  978 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisbandTime(long disbandTime) throws IOException {
/*  991 */     if (this.disband != disbandTime) {
/*      */       
/*  993 */       this.disband = disbandTime;
/*  994 */       Connection dbcon = null;
/*  995 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  998 */         dbcon = DbConnector.getZonesDbCon();
/*  999 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET DISBAND=? WHERE ID=?");
/* 1000 */         ps.setLong(1, this.disband);
/* 1001 */         ps.setInt(2, this.id);
/* 1002 */         ps.executeUpdate();
/*      */       }
/* 1004 */       catch (SQLException sqx) {
/*      */         
/* 1006 */         logger.log(Level.WARNING, "Failed to set disbanding=" + disbandTime + " for village with id " + this.id, sqx);
/* 1007 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1011 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1012 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisbander(long disb) throws IOException {
/* 1025 */     if (this.disbander != disb) {
/*      */       
/* 1027 */       this.disbander = disb;
/* 1028 */       Connection dbcon = null;
/* 1029 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1032 */         dbcon = DbConnector.getZonesDbCon();
/* 1033 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET DISBANDER=? WHERE ID=?");
/* 1034 */         ps.setLong(1, this.disbander);
/* 1035 */         ps.setInt(2, this.id);
/* 1036 */         ps.executeUpdate();
/*      */       }
/* 1038 */       catch (SQLException sqx) {
/*      */         
/* 1040 */         logger.log(Level.WARNING, "Failed to set disbander=" + this.disbander + " for village with id " + this.id, sqx);
/* 1041 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1045 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1046 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveSettings() throws IOException {
/* 1059 */     Connection dbcon = null;
/* 1060 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1063 */       dbcon = DbConnector.getZonesDbCon();
/* 1064 */       ps = dbcon.prepareStatement("UPDATE VILLAGES SET MAYPICKUP=? WHERE ID=?");
/* 1065 */       ps.setByte(1, (byte)this.settings.getPermissions());
/* 1066 */       ps.setInt(2, this.id);
/* 1067 */       ps.executeUpdate();
/*      */     }
/* 1069 */     catch (SQLException sqx) {
/*      */       
/* 1071 */       logger.log(Level.WARNING, "Failed to seve settings: for village with id " + this.id, sqx);
/* 1072 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1076 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1077 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPerimeter(int newPerimeter) throws IOException {
/* 1089 */     if (this.perimeterTiles != newPerimeter) {
/*      */       
/* 1091 */       this.perimeterTiles = newPerimeter;
/* 1092 */       Connection dbcon = null;
/* 1093 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1096 */         dbcon = DbConnector.getZonesDbCon();
/* 1097 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET PERIMETER=? WHERE ID=?");
/* 1098 */         ps.setInt(1, this.perimeterTiles);
/* 1099 */         ps.setInt(2, this.id);
/* 1100 */         ps.executeUpdate();
/*      */       }
/* 1102 */       catch (SQLException sqx) {
/*      */         
/* 1104 */         logger.log(Level.WARNING, "Failed to set perimeter=" + newPerimeter + " for village with id " + this.id, sqx);
/* 1105 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1109 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1110 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcceptsMerchants(boolean accepts) throws IOException {
/* 1123 */     if (this.acceptsMerchants != accepts) {
/*      */       
/* 1125 */       this.acceptsMerchants = accepts;
/* 1126 */       Connection dbcon = null;
/* 1127 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1130 */         dbcon = DbConnector.getZonesDbCon();
/* 1131 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET MERCHANTS=? WHERE ID=?");
/* 1132 */         ps.setBoolean(1, this.acceptsMerchants);
/* 1133 */         ps.setInt(2, this.id);
/* 1134 */         ps.executeUpdate();
/*      */       }
/* 1136 */       catch (SQLException sqx) {
/*      */         
/* 1138 */         logger.log(Level.WARNING, "Failed to set acceptsMerchants=" + this.acceptsMerchants + " for village with id " + this.id, sqx);
/*      */         
/* 1140 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1144 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1145 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowsAggroCreatures(boolean allows) throws IOException {
/* 1158 */     if (this.allowsAggCreatures != allows) {
/*      */       
/* 1160 */       this.allowsAggCreatures = allows;
/* 1161 */       Connection dbcon = null;
/* 1162 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1165 */         dbcon = DbConnector.getZonesDbCon();
/* 1166 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET AGGROS=? WHERE ID=?");
/* 1167 */         ps.setBoolean(1, this.allowsAggCreatures);
/* 1168 */         ps.setInt(2, this.id);
/* 1169 */         ps.executeUpdate();
/*      */       }
/* 1171 */       catch (SQLException sqx) {
/*      */         
/* 1173 */         logger.log(Level.WARNING, "Failed to set acceptsMerchants=" + this.acceptsMerchants + " for village with id " + this.id, sqx);
/*      */         
/* 1175 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1179 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1180 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUnlimitedCitizens(boolean accepts) throws IOException {
/* 1195 */     if (this.unlimitedCitizens != accepts) {
/*      */       
/* 1197 */       this.unlimitedCitizens = accepts;
/* 1198 */       Connection dbcon = null;
/* 1199 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1202 */         dbcon = DbConnector.getZonesDbCon();
/* 1203 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET ACCEPTSHOMESTEADS=? WHERE ID=?");
/* 1204 */         ps.setBoolean(1, this.unlimitedCitizens);
/* 1205 */         ps.setInt(2, this.id);
/* 1206 */         ps.executeUpdate();
/*      */       }
/* 1208 */       catch (SQLException sqx) {
/*      */         
/* 1210 */         logger.log(Level.WARNING, "Failed to set unlimitedcitizens=" + this.unlimitedCitizens + " for village with id " + this.id, sqx);
/*      */         
/* 1212 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1216 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1217 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogin() {
/* 1230 */     this.lastLogin = System.currentTimeMillis();
/* 1231 */     Connection dbcon = null;
/* 1232 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1235 */       dbcon = DbConnector.getZonesDbCon();
/* 1236 */       ps = dbcon.prepareStatement("UPDATE VILLAGES SET LASTLOGIN=? WHERE ID=?");
/* 1237 */       ps.setLong(1, this.lastLogin);
/* 1238 */       ps.setInt(2, this.id);
/* 1239 */       ps.executeUpdate();
/*      */     }
/* 1241 */     catch (SQLException sqx) {
/*      */       
/* 1243 */       logger.log(Level.WARNING, "Failed to set last login=now for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1247 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1248 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1250 */     if (!isCitizen(this.disbander)) {
/* 1251 */       stopDisbanding();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void delete() throws IOException {
/* 1262 */     Connection dbcon = null;
/* 1263 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1266 */       dbcon = DbConnector.getZonesDbCon();
/* 1267 */       ps = dbcon.prepareStatement("UPDATE VILLAGES SET DISBANDED=1 WHERE ID=?");
/* 1268 */       ps.setInt(1, this.id);
/* 1269 */       ps.executeUpdate();
/*      */     }
/* 1271 */     catch (SQLException sqx) {
/*      */       
/* 1273 */       logger.log(Level.WARNING, "Failed to delete village with id=" + this.id, sqx);
/* 1274 */       throw new IOException(sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1278 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1279 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void deleteVillageMapAnnotations() {
/* 1286 */     Connection dbcon = null;
/* 1287 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1290 */       dbcon = DbConnector.getPlayerDbCon();
/* 1291 */       ps = dbcon.prepareStatement("DELETE FROM MAP_ANNOTATIONS WHERE OWNERID=? AND POITYPE=1;");
/* 1292 */       ps.setLong(1, this.id);
/* 1293 */       ps.executeUpdate();
/*      */     }
/* 1295 */     catch (SQLException sqx) {
/*      */       
/* 1297 */       logger.log(Level.WARNING, "Failed to delete village map annotations with id=" + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1301 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1302 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void save() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void loadReputations() {
/* 1326 */     Connection dbcon = null;
/* 1327 */     PreparedStatement ps = null;
/* 1328 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1331 */       dbcon = DbConnector.getZonesDbCon();
/* 1332 */       ps = dbcon.prepareStatement("SELECT REPUTATION,PERMANENT, WURMID FROM REPUTATION WHERE VILLAGEID=?");
/* 1333 */       ps.setInt(1, this.id);
/* 1334 */       rs = ps.executeQuery();
/* 1335 */       while (rs.next())
/*      */       {
/* 1337 */         long wurmId = rs.getLong("WURMID");
/* 1338 */         int reputation = rs.getByte("REPUTATION");
/* 1339 */         boolean perma = rs.getBoolean("PERMANENT");
/* 1340 */         Reputation rep = new Reputation(wurmId, this.id, perma, reputation, false, true);
/*      */         
/* 1342 */         this.reputations.put(new Long(wurmId), rep);
/*      */       }
/*      */     
/* 1345 */     } catch (SQLException sqx) {
/*      */       
/* 1347 */       logger.log(Level.WARNING, "Failed to load reputations for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1351 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1352 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setUpkeep(long aUpk) throws IOException {
/* 1364 */     long upk = Math.max(0L, aUpk);
/* 1365 */     if (upk != this.upkeep) {
/*      */       
/* 1367 */       this.upkeep = upk;
/* 1368 */       Connection dbcon = null;
/* 1369 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1372 */         dbcon = DbConnector.getZonesDbCon();
/* 1373 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET UPKEEP=? WHERE ID=?");
/* 1374 */         ps.setLong(1, this.upkeep);
/* 1375 */         ps.setInt(2, this.id);
/* 1376 */         ps.executeUpdate();
/*      */       }
/* 1378 */       catch (SQLException sqx) {
/*      */         
/* 1380 */         logger.log(Level.WARNING, "Failed to set upkeep=" + this.upkeep + " for village with id " + this.id, sqx);
/* 1381 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1385 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1386 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadGuards() {
/* 1399 */     Connection dbcon = null;
/* 1400 */     PreparedStatement ps = null;
/* 1401 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1404 */       dbcon = DbConnector.getZonesDbCon();
/* 1405 */       ps = dbcon.prepareStatement("SELECT * FROM GUARDS WHERE VILLAGEID=?");
/* 1406 */       ps.setInt(1, this.id);
/* 1407 */       rs = ps.executeQuery();
/* 1408 */       while (rs.next())
/*      */       {
/* 1410 */         long wurmId = rs.getLong("WURMID");
/* 1411 */         long expireDate = rs.getLong("EXPIREDATE");
/*      */         
/*      */         try {
/* 1414 */           Creature creature = Creatures.getInstance().getCreature(wurmId);
/* 1415 */           Guard guard = new DbGuard(this.id, creature, expireDate);
/* 1416 */           creature.setCitizenVillage(this);
/* 1417 */           this.guards.put(new Long(wurmId), guard);
/*      */         }
/* 1419 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 1421 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1422 */           ps = dbcon.prepareStatement("DELETE FROM GUARDS WHERE WURMID=?");
/* 1423 */           ps.setLong(1, wurmId);
/* 1424 */           ps.executeUpdate();
/* 1425 */           logger.log(Level.WARNING, "Deleted guard with id " + wurmId + ". These messages should disappear over time since 040707.");
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 1430 */     } catch (SQLException sqx) {
/*      */       
/* 1432 */       logger.log(Level.WARNING, "Failed to load guards for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1436 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1437 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void loadHistory() {
/* 1449 */     this.history = new LinkedList<>();
/* 1450 */     Connection dbcon = null;
/* 1451 */     PreparedStatement ps = null;
/* 1452 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1455 */       dbcon = DbConnector.getZonesDbCon();
/* 1456 */       ps = dbcon.prepareStatement("SELECT EVENTDATE, VILLAGEID, PERFORMER, EVENT FROM HISTORY WHERE VILLAGEID=? ORDER BY EVENTDATE DESC");
/* 1457 */       ps.setInt(1, this.id);
/* 1458 */       rs = ps.executeQuery();
/* 1459 */       while (rs.next())
/*      */       {
/* 1461 */         this.history.add(new HistoryEvent(rs.getLong("EVENTDATE"), rs.getString("PERFORMER"), rs.getString("EVENT"), this.id));
/*      */       }
/*      */     }
/* 1464 */     catch (SQLException sqx) {
/*      */       
/* 1466 */       logger.log(Level.WARNING, "Failed to load history for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1470 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1471 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addHistory(String performerName, String event) {
/* 1483 */     HistoryEvent h = new HistoryEvent(System.currentTimeMillis(), performerName, event, this.id);
/* 1484 */     this.history.addFirst(h);
/* 1485 */     Connection dbcon = null;
/* 1486 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1489 */       dbcon = DbConnector.getZonesDbCon();
/* 1490 */       ps = dbcon.prepareStatement("INSERT INTO HISTORY(EVENTDATE,VILLAGEID,PERFORMER,EVENT) VALUES (?,?,?,?)");
/* 1491 */       ps.setLong(1, h.time);
/* 1492 */       ps.setInt(2, h.identifier);
/* 1493 */       ps.setString(3, h.performer);
/* 1494 */       ps.setString(4, h.event);
/* 1495 */       ps.executeUpdate();
/*      */     }
/* 1497 */     catch (SQLException sqx) {
/*      */       
/* 1499 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1503 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1504 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void saveRecruitee(VillageRecruitee vr) {
/* 1511 */     Connection dbcon = null;
/* 1512 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1515 */       dbcon = DbConnector.getZonesDbCon();
/* 1516 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGERECRUITEES(VILLAGEID, RECRUITEEID, RECRUITEENAME) VALUES (?,?,?)");
/* 1517 */       ps.setInt(1, vr.getVillageId());
/* 1518 */       ps.setLong(2, vr.getRecruiteeId());
/* 1519 */       ps.setString(3, vr.getRecruiteeName());
/* 1520 */       ps.executeUpdate();
/*      */     }
/* 1522 */     catch (SQLException sqx) {
/*      */       
/* 1524 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1528 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1529 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKingdom(byte newkingdom) throws IOException {
/* 1541 */     setKingdom(newkingdom, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKingdom(byte newkingdom, boolean updateTimeStamp) throws IOException {
/* 1552 */     if (this.kingdom != newkingdom) {
/*      */       
/* 1554 */       this.kingdom = newkingdom;
/* 1555 */       Kingdom k = Kingdoms.getKingdom(this.kingdom);
/* 1556 */       if (k != null)
/* 1557 */         k.setExistsHere(true); 
/* 1558 */       convertOfflineCitizensToKingdom(newkingdom, updateTimeStamp);
/* 1559 */       Connection dbcon = null;
/* 1560 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1563 */         dbcon = DbConnector.getZonesDbCon();
/* 1564 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET KINGDOM=? WHERE ID=?");
/* 1565 */         ps.setByte(1, this.kingdom);
/* 1566 */         ps.setInt(2, this.id);
/* 1567 */         ps.executeUpdate();
/*      */       }
/* 1569 */       catch (SQLException sqx) {
/*      */         
/* 1571 */         logger.log(Level.WARNING, "Failed to set kingdom=" + newkingdom + " for village with id " + this.id, sqx);
/* 1572 */         throw new IOException(sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1576 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1577 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFaithCreate(float newFaith) {
/* 1590 */     if (this.faithCreate != newFaith) {
/*      */       
/* 1592 */       this.faithCreate = newFaith;
/* 1593 */       Connection dbcon = null;
/* 1594 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1597 */         dbcon = DbConnector.getZonesDbCon();
/* 1598 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET FAITHCREATE=? WHERE ID=?");
/* 1599 */         ps.setFloat(1, this.faithCreate);
/* 1600 */         ps.setInt(2, this.id);
/* 1601 */         ps.executeUpdate();
/*      */       }
/* 1603 */       catch (SQLException sqx) {
/*      */         
/* 1605 */         logger.log(Level.WARNING, "Failed to set faithCreate=" + this.faithCreate + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1609 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1610 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFaithWar(float newFaith) {
/* 1623 */     if (this.faithWar != newFaith) {
/*      */       
/* 1625 */       this.faithWar = newFaith;
/* 1626 */       Connection dbcon = null;
/* 1627 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1630 */         dbcon = DbConnector.getZonesDbCon();
/* 1631 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET FAITHWAR=? WHERE ID=?");
/* 1632 */         ps.setFloat(1, this.faithWar);
/* 1633 */         ps.setInt(2, this.id);
/* 1634 */         ps.executeUpdate();
/*      */       }
/* 1636 */       catch (SQLException sqx) {
/*      */         
/* 1638 */         logger.log(Level.WARNING, "Failed to set faithCreate=" + this.faithWar + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1642 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1643 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFaithHeal(float newFaith) {
/* 1656 */     if (this.faithHeal != newFaith) {
/*      */       
/* 1658 */       this.faithHeal = newFaith;
/* 1659 */       Connection dbcon = null;
/* 1660 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1663 */         dbcon = DbConnector.getZonesDbCon();
/* 1664 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET FAITHHEAL=? WHERE ID=?");
/* 1665 */         ps.setFloat(1, this.faithHeal);
/* 1666 */         ps.setInt(2, this.id);
/* 1667 */         ps.executeUpdate();
/*      */       }
/* 1669 */       catch (SQLException sqx) {
/*      */         
/* 1671 */         logger.log(Level.WARNING, "Failed to set faithHeal=" + this.faithHeal + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1675 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1676 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTwitCredentials(String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean _twitChat, boolean enabled) {
/* 1691 */     this.consumerKeyToUse = _consumerKeyToUse;
/* 1692 */     this.consumerSecretToUse = _consumerSecretToUse;
/* 1693 */     this.applicationToken = _applicationToken;
/* 1694 */     this.applicationSecret = _applicationSecret;
/* 1695 */     this.twitChat = _twitChat;
/* 1696 */     this.twitEnabled = enabled;
/* 1697 */     Connection dbcon = null;
/* 1698 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1701 */       dbcon = DbConnector.getZonesDbCon();
/* 1702 */       ps = dbcon.prepareStatement("UPDATE VILLAGES SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=?,TWITCHAT=?,TWITENABLE=? WHERE ID=?");
/* 1703 */       ps.setString(1, this.consumerKeyToUse);
/* 1704 */       ps.setString(2, this.consumerSecretToUse);
/* 1705 */       ps.setString(3, this.applicationToken);
/* 1706 */       ps.setString(4, this.applicationSecret);
/* 1707 */       ps.setBoolean(5, this.twitChat);
/* 1708 */       ps.setBoolean(6, this.twitEnabled);
/* 1709 */       ps.setInt(7, this.id);
/* 1710 */       ps.executeUpdate();
/*      */     }
/* 1712 */     catch (SQLException sqx) {
/*      */       
/* 1714 */       logger.log(Level.WARNING, "Failed to set twitter info for village with id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1718 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1719 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1721 */     canTwit();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setHotaWins(short newWins) {
/* 1727 */     if (this.hotaWins != newWins) {
/*      */       
/* 1729 */       this.hotaWins = newWins;
/* 1730 */       Connection dbcon = null;
/* 1731 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1734 */         dbcon = DbConnector.getZonesDbCon();
/* 1735 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET HOTAWINS=? WHERE ID=?");
/* 1736 */         ps.setShort(1, this.hotaWins);
/* 1737 */         ps.setInt(2, this.id);
/* 1738 */         ps.executeUpdate();
/*      */       }
/* 1740 */       catch (SQLException sqx) {
/*      */         
/* 1742 */         logger.log(Level.WARNING, "Failed to set hotaWins=" + newWins + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1746 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1747 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLastChangedName(long newDate) {
/* 1755 */     if (this.lastChangedName != newDate) {
/*      */       
/* 1757 */       this.lastChangedName = newDate;
/* 1758 */       Connection dbcon = null;
/* 1759 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1762 */         dbcon = DbConnector.getZonesDbCon();
/* 1763 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET NAMECHANGED=? WHERE ID=?");
/* 1764 */         ps.setLong(1, this.lastChangedName);
/* 1765 */         ps.setInt(2, this.id);
/* 1766 */         ps.executeUpdate();
/*      */       }
/* 1768 */       catch (SQLException sqx) {
/*      */         
/* 1770 */         logger.log(Level.WARNING, "Failed to set lastChangedName=" + newDate + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1774 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1775 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSpawnSituation(byte newSpawnSituation) {
/* 1783 */     if (this.spawnSituation != newSpawnSituation) {
/*      */       
/* 1785 */       this.spawnSituation = newSpawnSituation;
/* 1786 */       Connection dbcon = null;
/* 1787 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1790 */         dbcon = DbConnector.getZonesDbCon();
/* 1791 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET SPAWNSITUATION=? WHERE ID=?");
/* 1792 */         ps.setByte(1, this.spawnSituation);
/* 1793 */         ps.setInt(2, this.id);
/* 1794 */         ps.executeUpdate();
/*      */       }
/* 1796 */       catch (SQLException sqx) {
/*      */         
/* 1798 */         logger.log(Level.WARNING, "Failed to set spawnSituation=" + this.spawnSituation + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1802 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1803 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAllianceNumber(int newAllianceNumber) {
/* 1811 */     if (this.allianceNumber != newAllianceNumber) {
/*      */       
/* 1813 */       this.allianceNumber = newAllianceNumber;
/*      */       
/* 1815 */       Connection dbcon = null;
/* 1816 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1819 */         dbcon = DbConnector.getZonesDbCon();
/* 1820 */         ps = dbcon.prepareStatement("UPDATE VILLAGES SET ALLIANCENUMBER=? WHERE ID=?");
/* 1821 */         ps.setInt(1, this.allianceNumber);
/* 1822 */         ps.setInt(2, this.id);
/* 1823 */         ps.executeUpdate();
/*      */       }
/* 1825 */       catch (SQLException sqx) {
/*      */         
/* 1827 */         logger.log(Level.WARNING, "Failed to set allianceNumber=" + this.allianceNumber + " for village with id " + this.id, sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1831 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1832 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1834 */       if (this.allianceNumber > 0)
/*      */         
/*      */         try {
/*      */           
/* 1838 */           getRoleForStatus((byte)5);
/*      */         }
/* 1840 */         catch (NoSuchRoleException nsr) {
/*      */           
/* 1842 */           createRoleAlly();
/*      */         }  
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */