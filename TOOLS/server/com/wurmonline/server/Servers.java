/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.utils.SimpleArgumentParser;
/*      */ import com.wurmonline.server.webinterface.WebCommand;
/*      */ import com.wurmonline.server.zones.TilePoller;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public final class Servers
/*      */   implements MiscConstants
/*      */ {
/*   51 */   public static SimpleArgumentParser arguments = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   56 */   private static Map<String, ServerEntry> neighbours = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   62 */   private static Map<Integer, ServerEntry> allServers = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry localServer;
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry loginServer;
/*      */ 
/*      */   
/*   73 */   private static final Logger logger = Logger.getLogger(Servers.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String GET_ALL_SERVERS = "SELECT * FROM SERVERS";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String INSERT_SERVER = "INSERT INTO SERVERS(SERVER,NAME,HOMESERVER,SPAWNPOINTJENNX,SPAWNPOINTJENNY,SPAWNPOINTLIBX,SPAWNPOINTLIBY,SPAWNPOINTMOLX,SPAWNPOINTMOLY,INTRASERVERADDRESS,INTRASERVERPORT,INTRASERVERPASSWORD,EXTERNALIP, EXTERNALPORT,LOGINSERVER, KINGDOM,ISPAYMENT,TWITKEY,TWITSECRET,TWITAPP,TWITAPPSECRET, LOCAL,ISTEST,RANDOMSPAWNS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String GET_NEIGHBOURS = "SELECT * FROM SERVERNEIGHBOURS WHERE SERVER=?";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String ADD_NEIGHBOUR = "INSERT INTO SERVERNEIGHBOURS(SERVER,NEIGHBOUR,DIRECTION) VALUES(?,?,?)";
/*      */ 
/*      */   
/*      */   private static final String DELETE_NEIGHBOUR = "DELETE FROM SERVERNEIGHBOURS WHERE SERVER=? AND NEIGHBOUR=?";
/*      */ 
/*      */   
/*      */   private static final String DELETE_SERVER = "DELETE FROM SERVERS WHERE SERVER=?";
/*      */ 
/*      */   
/*      */   private static final String DELETE_SERVER2 = "DELETE FROM SERVERNEIGHBOURS WHERE SERVER=? OR NEIGHBOUR=?";
/*      */ 
/*      */   
/*      */   private static final String SET_TWITTER = "UPDATE SERVERS SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=? WHERE SERVER=?";
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry getServer(String direction) {
/*  106 */     return neighbours.get(direction);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean addServerNeighbour(int serverid, String direction) {
/*  111 */     boolean ok = false;
/*  112 */     if (loadNeighbour(serverid, direction)) {
/*      */       
/*  114 */       Connection dbcon = null;
/*  115 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  118 */         dbcon = DbConnector.getLoginDbCon();
/*  119 */         ps = dbcon.prepareStatement("INSERT INTO SERVERNEIGHBOURS(SERVER,NEIGHBOUR,DIRECTION) VALUES(?,?,?)");
/*  120 */         ps.setInt(1, localServer.id);
/*  121 */         ps.setInt(2, serverid);
/*  122 */         ps.setString(3, direction);
/*  123 */         ps.executeUpdate();
/*  124 */         ok = true;
/*      */       }
/*  126 */       catch (SQLException sqex) {
/*      */         
/*  128 */         logger.log(Level.WARNING, "Failed to insert neighbour " + serverid + "," + direction + " into logindb!" + sqex
/*  129 */             .getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  133 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  134 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*  137 */     return ok;
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
/*      */   public static boolean isThisLoginServer() {
/*  152 */     return (loginServer == null || localServer.id == loginServer.id);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRealLoginServer() {
/*  157 */     return (localServer.id == loginServer.id);
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
/*      */   public static boolean isThisAChaosServer() {
/*  170 */     return (localServer != null && localServer.isChaosServer());
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
/*      */   public static boolean isThisAnEpicServer() {
/*  183 */     return (localServer != null && localServer.EPIC);
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
/*      */   public static boolean isThisAnEpicOrChallengeServer() {
/*  196 */     return (localServer != null && (localServer.EPIC || localServer.isChallengeServer()));
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
/*      */   public static boolean isThisAHomeServer() {
/*  209 */     return (localServer != null && localServer.HOMESERVER);
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
/*      */   public static boolean isThisAPvpServer() {
/*  222 */     return (localServer != null && localServer.PVPSERVER);
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
/*      */   public static boolean isThisATestServer() {
/*  236 */     return (localServer != null && localServer.testServer);
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
/*      */   public static byte getLocalKingdom() {
/*      */     byte localKingdom;
/*  249 */     if (localServer != null) {
/*      */       
/*  251 */       localKingdom = localServer.getKingdom();
/*      */     }
/*      */     else {
/*      */       
/*  255 */       localKingdom = -10;
/*      */     } 
/*  257 */     return localKingdom;
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
/*      */   public static int getLocalServerId() {
/*      */     int localServerId;
/*  270 */     if (localServer != null) {
/*      */       
/*  272 */       localServerId = localServer.getId();
/*      */     }
/*      */     else {
/*      */       
/*  276 */       localServerId = 0;
/*      */     } 
/*  278 */     return localServerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry getLoginServer() {
/*  288 */     return loginServer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getLoginServerId() {
/*  298 */     return loginServer.id;
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
/*      */   public static String getLocalServerName() {
/*      */     String localServerName;
/*  311 */     if (localServer != null) {
/*      */       
/*  313 */       localServerName = localServer.getName();
/*      */     }
/*      */     else {
/*      */       
/*  317 */       localServerName = "Unknown";
/*      */     } 
/*  319 */     return localServerName;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean deleteServerNeighbour(String dir) {
/*  324 */     boolean ok = false;
/*  325 */     ServerEntry entry = neighbours.get(dir);
/*  326 */     if (entry != null)
/*  327 */       ok = deleteServerNeighbour(entry.id); 
/*  328 */     neighbours.remove(dir);
/*  329 */     if (dir.equals("NORTH")) {
/*  330 */       localServer.serverNorth = null;
/*  331 */     } else if (dir.equals("WEST")) {
/*  332 */       localServer.serverWest = null;
/*  333 */     } else if (dir.equals("SOUTH")) {
/*  334 */       localServer.serverSouth = null;
/*  335 */     } else if (dir.equals("EAST")) {
/*  336 */       localServer.serverEast = null;
/*  337 */     }  return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean deleteServerNeighbour(int id) {
/*  342 */     boolean ok = false;
/*  343 */     Connection dbcon = null;
/*  344 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  347 */       dbcon = DbConnector.getLoginDbCon();
/*  348 */       ps = dbcon.prepareStatement("DELETE FROM SERVERNEIGHBOURS WHERE SERVER=? AND NEIGHBOUR=?");
/*  349 */       ps.setInt(1, localServer.id);
/*  350 */       ps.setInt(2, id);
/*  351 */       ps.executeUpdate();
/*  352 */       ok = true;
/*      */     }
/*  354 */     catch (SQLException sqex) {
/*      */       
/*  356 */       logger.log(Level.WARNING, "Failed to delete neighbour " + id + " from logindb!" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  360 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  361 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  363 */     return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean deleteServerEntry(int id) {
/*  368 */     boolean ok = false;
/*  369 */     Connection dbcon = null;
/*  370 */     PreparedStatement ps = null;
/*  371 */     PreparedStatement ps2 = null;
/*      */ 
/*      */     
/*      */     try {
/*  375 */       dbcon = DbConnector.getLoginDbCon();
/*  376 */       ps2 = dbcon.prepareStatement("DELETE FROM SERVERNEIGHBOURS WHERE SERVER=? OR NEIGHBOUR=?");
/*  377 */       ps2.setInt(1, id);
/*  378 */       ps2.setInt(2, id);
/*  379 */       ps2.executeUpdate();
/*  380 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*      */       
/*  382 */       ps = dbcon.prepareStatement("DELETE FROM SERVERS WHERE SERVER=?");
/*  383 */       ps.setInt(1, id);
/*  384 */       ps.executeUpdate();
/*  385 */       ok = true;
/*      */     }
/*  387 */     catch (SQLException sqex) {
/*      */       
/*  389 */       logger.log(Level.WARNING, "Failed to delete neighbour " + id + " from logindb!" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  393 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  394 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*  395 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  397 */     removeServer(id);
/*  398 */     return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void removeServer(int id) {
/*  403 */     allServers.remove(Integer.valueOf(id));
/*  404 */     neighbours.remove(Integer.valueOf(id));
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
/*      */   public static final void registerServer(int id, String name, boolean homeServer, int fox, int foy, int libx, int liby, int molx, int moly, String intraip, String intraport, String password, String externalip, String externalport, boolean loginserver, byte kingdom, boolean isPayment, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean isLocalServer, boolean isTestServer, boolean randomSpawns) {
/*  464 */     Connection dbcon = null;
/*  465 */     PreparedStatement ps2 = null;
/*      */     
/*      */     try {
/*  468 */       logger.log(Level.INFO, "Registering server id: " + id + ", external IP: " + externalip + ", name: " + name);
/*  469 */       dbcon = DbConnector.getLoginDbCon();
/*  470 */       ps2 = dbcon.prepareStatement("INSERT INTO SERVERS(SERVER,NAME,HOMESERVER,SPAWNPOINTJENNX,SPAWNPOINTJENNY,SPAWNPOINTLIBX,SPAWNPOINTLIBY,SPAWNPOINTMOLX,SPAWNPOINTMOLY,INTRASERVERADDRESS,INTRASERVERPORT,INTRASERVERPASSWORD,EXTERNALIP, EXTERNALPORT,LOGINSERVER, KINGDOM,ISPAYMENT,TWITKEY,TWITSECRET,TWITAPP,TWITAPPSECRET, LOCAL,ISTEST,RANDOMSPAWNS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/*  471 */       ps2.setInt(1, id);
/*  472 */       ps2.setString(2, name);
/*  473 */       ps2.setBoolean(3, homeServer);
/*  474 */       ps2.setInt(4, fox);
/*  475 */       ps2.setInt(5, foy);
/*  476 */       ps2.setInt(6, libx);
/*  477 */       ps2.setInt(7, liby);
/*  478 */       ps2.setInt(8, molx);
/*  479 */       ps2.setInt(9, moly);
/*  480 */       ps2.setString(10, intraip);
/*  481 */       ps2.setString(11, intraport);
/*  482 */       ps2.setString(12, password);
/*  483 */       ps2.setString(13, externalip);
/*  484 */       ps2.setString(14, externalport);
/*  485 */       ps2.setBoolean(15, loginserver);
/*  486 */       ps2.setByte(16, kingdom);
/*  487 */       ps2.setBoolean(17, isPayment);
/*  488 */       ps2.setString(18, _consumerKeyToUse);
/*  489 */       ps2.setString(19, _consumerSecretToUse);
/*  490 */       ps2.setString(20, _applicationToken);
/*  491 */       ps2.setString(21, _applicationSecret);
/*  492 */       ps2.setBoolean(22, isLocalServer);
/*  493 */       ps2.setBoolean(23, isTestServer);
/*  494 */       ps2.setBoolean(24, randomSpawns);
/*  495 */       ps2.executeUpdate();
/*  496 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*  497 */       if (loginserver)
/*  498 */         loadLoginServer(); 
/*  499 */       loadAllServers(true);
/*      */     }
/*  501 */     catch (SQLException sqex) {
/*      */       
/*  503 */       logger.log(Level.WARNING, "Failed to load or insert server into logindb!" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  507 */       DbUtilities.closeDatabaseObjects(ps2, null);
/*  508 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void setTwitCredentials(int serverId, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret) {
/*  515 */     ServerEntry entry = getServerWithId(serverId);
/*      */     
/*  517 */     if (entry != null) {
/*      */       
/*  519 */       entry.consumerKeyToUse = _consumerKeyToUse;
/*  520 */       entry.consumerSecretToUse = _consumerSecretToUse;
/*  521 */       entry.applicationToken = _applicationToken;
/*  522 */       entry.applicationSecret = _applicationSecret;
/*  523 */       entry.canTwit();
/*      */     } 
/*      */ 
/*      */     
/*  527 */     if (localServer.id == serverId) {
/*      */       
/*  529 */       localServer.consumerKeyToUse = _consumerKeyToUse;
/*  530 */       localServer.consumerSecretToUse = _consumerSecretToUse;
/*  531 */       localServer.applicationToken = _applicationToken;
/*  532 */       localServer.applicationSecret = _applicationSecret;
/*  533 */       localServer.canTwit();
/*      */     } 
/*  535 */     Connection dbcon = null;
/*  536 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  539 */       dbcon = DbConnector.getLoginDbCon();
/*  540 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=? WHERE SERVER=?");
/*  541 */       ps.setString(1, _consumerKeyToUse);
/*  542 */       ps.setString(2, _consumerSecretToUse);
/*  543 */       ps.setString(3, _applicationToken);
/*  544 */       ps.setString(4, _applicationSecret);
/*  545 */       ps.setInt(5, serverId);
/*  546 */       ps.executeUpdate();
/*      */     }
/*  548 */     catch (SQLException sqx) {
/*      */       
/*  550 */       logger.log(Level.WARNING, "Failed to set twitter info for server with id " + serverId, sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  554 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  555 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void loadNeighbours() {
/*  562 */     neighbours = new HashMap<>();
/*  563 */     if (localServer != null) {
/*      */       
/*  565 */       localServer.serverNorth = null;
/*  566 */       localServer.serverEast = null;
/*  567 */       localServer.serverSouth = null;
/*  568 */       localServer.serverWest = null;
/*  569 */       Connection dbcon = null;
/*  570 */       PreparedStatement ps = null;
/*  571 */       ResultSet rs = null;
/*      */       
/*      */       try {
/*  574 */         dbcon = DbConnector.getLoginDbCon();
/*  575 */         ps = dbcon.prepareStatement("SELECT * FROM SERVERNEIGHBOURS WHERE SERVER=?");
/*  576 */         ps.setInt(1, localServer.id);
/*  577 */         rs = ps.executeQuery();
/*  578 */         while (rs.next())
/*      */         {
/*  580 */           int serverid = rs.getInt("NEIGHBOUR");
/*  581 */           String direction = rs.getString("DIRECTION");
/*  582 */           loadNeighbour(serverid, direction);
/*      */         }
/*      */       
/*  585 */       } catch (SQLException sqex) {
/*      */         
/*  587 */         logger.log(Level.WARNING, "Failed to load all neighbours!" + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  591 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  592 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean loadNeighbour(int serverid, String dir) {
/*  599 */     boolean ok = false;
/*  600 */     ServerEntry entry = getServerWithId(serverid);
/*  601 */     if (entry != null) {
/*      */       
/*  603 */       logger.log(Level.INFO, "found neighbour " + entry.name + " " + dir);
/*  604 */       neighbours.put(dir, entry);
/*  605 */       if (dir.equals("NORTH")) {
/*      */         
/*  607 */         logger.log(Level.INFO, "NORTH neighbour " + entry.name + " " + dir);
/*  608 */         localServer.serverNorth = entry;
/*      */       }
/*  610 */       else if (dir.equals("WEST")) {
/*      */         
/*  612 */         logger.log(Level.INFO, "WEST neighbour " + entry.name + " " + dir);
/*  613 */         localServer.serverWest = entry;
/*      */       }
/*  615 */       else if (dir.equals("SOUTH")) {
/*      */         
/*  617 */         logger.log(Level.INFO, "SOUTH neighbour " + entry.name + " " + dir);
/*  618 */         localServer.serverSouth = entry;
/*      */       }
/*  620 */       else if (dir.equals("EAST")) {
/*      */         
/*  622 */         logger.log(Level.INFO, "EAST neighbour " + entry.name + " " + dir);
/*  623 */         localServer.serverEast = entry;
/*      */       } 
/*  625 */       ok = true;
/*      */     } 
/*  627 */     return ok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadAllServers(boolean reload) {
/*  638 */     System.out.println("Loading servers");
/*  639 */     Connection dbcon = null;
/*  640 */     PreparedStatement ps = null;
/*  641 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  644 */       if (!reload) {
/*      */         
/*  646 */         allServers = new ConcurrentHashMap<>();
/*  647 */         localServer = null;
/*      */       }
/*      */       else {
/*      */         
/*  651 */         for (ServerEntry server : allServers.values())
/*      */         {
/*      */ 
/*      */           
/*  655 */           server.reloading = true;
/*      */         }
/*      */       } 
/*  658 */       logger.log(Level.INFO, "Loading all servers.");
/*  659 */       dbcon = DbConnector.getLoginDbCon();
/*  660 */       ps = dbcon.prepareStatement("SELECT * FROM SERVERS");
/*  661 */       rs = ps.executeQuery();
/*  662 */       while (rs.next()) {
/*      */         
/*  664 */         int loadedId = rs.getInt("SERVER");
/*  665 */         ServerEntry entry = null;
/*  666 */         if (reload)
/*  667 */           entry = getServerWithId(loadedId); 
/*  668 */         if (entry == null) {
/*      */ 
/*      */ 
/*      */           
/*  672 */           entry = new ServerEntry();
/*  673 */           entry.id = loadedId;
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/*  680 */           entry.reloading = false;
/*      */         } 
/*  682 */         entry.HOMESERVER = rs.getBoolean("HOMESERVER");
/*  683 */         entry.PVPSERVER = rs.getBoolean("PVP");
/*  684 */         entry.name = rs.getString("NAME");
/*      */         
/*  686 */         logger.log(Level.INFO, "Loading " + entry.name + " - " + entry.id);
/*  687 */         entry.isLocal = rs.getBoolean("LOCAL");
/*  688 */         if (entry.isLocal) {
/*      */           
/*  690 */           localServer = entry;
/*  691 */           entry.isAvailable = true;
/*  692 */           entry.setNextEpicPoll(rs.getLong("NEXTEPICPOLL"));
/*  693 */           entry.setSkillDaySwitch(rs.getLong("SKILLDAYSWITCH"));
/*  694 */           entry.setSkillWeekSwitch(rs.getLong("SKILLWEEKSWITCH"));
/*  695 */           entry.setNextHota(rs.getLong("NEXTHOTA"));
/*  696 */           entry.setFatigueSwitch(rs.getLong("FATIGUESWITCH"));
/*      */         } 
/*      */         
/*  699 */         entry.SPAWNPOINTJENNX = rs.getInt("SPAWNPOINTJENNX");
/*  700 */         entry.SPAWNPOINTJENNY = rs.getInt("SPAWNPOINTJENNY");
/*  701 */         entry.SPAWNPOINTLIBX = rs.getInt("SPAWNPOINTLIBX");
/*  702 */         entry.SPAWNPOINTLIBY = rs.getInt("SPAWNPOINTLIBY");
/*  703 */         entry.SPAWNPOINTMOLX = rs.getInt("SPAWNPOINTMOLX");
/*  704 */         entry.SPAWNPOINTMOLY = rs.getInt("SPAWNPOINTMOLY");
/*  705 */         entry.INTRASERVERADDRESS = rs.getString("INTRASERVERADDRESS");
/*  706 */         entry.INTRASERVERPORT = rs.getString("INTRASERVERPORT");
/*  707 */         entry.INTRASERVERPASSWORD = rs.getString("INTRASERVERPASSWORD");
/*  708 */         entry.EXTERNALIP = rs.getString("EXTERNALIP");
/*  709 */         entry.EXTERNALPORT = rs.getString("EXTERNALPORT");
/*  710 */         entry.LOGINSERVER = rs.getBoolean("LOGINSERVER");
/*  711 */         if (entry.LOGINSERVER)
/*      */         {
/*  713 */           loginServer = entry;
/*      */         }
/*  715 */         entry.KINGDOM = rs.getByte("KINGDOM");
/*  716 */         entry.ISPAYMENT = rs.getBoolean("ISPAYMENT");
/*  717 */         entry.entryServer = rs.getBoolean("ENTRYSERVER");
/*      */         
/*  719 */         entry.testServer = rs.getBoolean("ISTEST");
/*  720 */         entry.challengeServer = rs.getBoolean("CHALLENGE");
/*  721 */         if (entry.challengeServer) {
/*      */           
/*  723 */           entry.setChallengeStarted(rs.getLong("CHALLENGESTARTED"));
/*  724 */           entry.setChallengeEnds(rs.getLong("CHALLENGEEND"));
/*  725 */           if (entry.getChallengeStarted() == 0L && entry.getChallengeEnds() == 0L)
/*  726 */             entry.challengeServer = false; 
/*      */         } 
/*  728 */         entry.lastDecreasedChampionPoints = rs.getLong("LASTRESETCHAMPS");
/*  729 */         entry.consumerKeyToUse = rs.getString("TWITKEY");
/*  730 */         entry.consumerSecretToUse = rs.getString("TWITSECRET");
/*  731 */         entry.applicationToken = rs.getString("TWITAPP");
/*  732 */         entry.applicationSecret = rs.getString("TWITAPPSECRET");
/*  733 */         entry.champConsumerKeyToUse = rs.getString("CHAMPTWITKEY");
/*  734 */         entry.champConsumerSecretToUse = rs.getString("CHAMPTWITSECRET");
/*  735 */         entry.champApplicationToken = rs.getString("CHAMPTWITAPP");
/*  736 */         entry.champApplicationSecret = rs.getString("CHAMPTWITAPPSECRET");
/*  737 */         long movedArtifacts = rs.getLong("MOVEDARTIS");
/*  738 */         if (movedArtifacts > 0L) {
/*  739 */           entry.setMovedArtifacts(movedArtifacts);
/*      */         } else {
/*  741 */           entry.movedArtifacts();
/*  742 */         }  long lastSpawnedUnique = rs.getLong("SPAWNEDUNIQUE");
/*      */         
/*  744 */         entry.setLastSpawnedUnique(lastSpawnedUnique);
/*      */         
/*  746 */         entry.canTwit();
/*      */         
/*  748 */         String rmiPort = rs.getString("RMIPORT");
/*  749 */         if (rmiPort != null && rmiPort.length() > 0) {
/*      */           
/*      */           try {
/*      */             
/*  753 */             entry.RMI_PORT = Integer.parseInt(rmiPort);
/*      */           }
/*  755 */           catch (NullPointerException npe) {
/*      */             
/*  757 */             logger.log(Level.WARNING, "rmiPort for server " + loadedId + " was not a number " + rmiPort, npe);
/*      */           } 
/*      */         }
/*  760 */         String regPort = rs.getString("REGISTRATIONPORT");
/*  761 */         if (regPort != null && regPort.length() > 0) {
/*      */           
/*      */           try {
/*      */             
/*  765 */             entry.REGISTRATION_PORT = Integer.parseInt(regPort);
/*      */           }
/*  767 */           catch (NullPointerException npe) {
/*      */             
/*  769 */             logger.log(Level.WARNING, "regPort for server " + loadedId + " was not a number " + regPort, npe);
/*      */           } 
/*      */         }
/*      */         
/*      */         try {
/*  774 */           entry.pLimit = rs.getInt("MAXPLAYERS");
/*  775 */           entry.maxCreatures = rs.getInt("MAXCREATURES");
/*  776 */           entry.maxTypedCreatures = entry.maxCreatures / 8;
/*      */           
/*  778 */           entry.percentAggCreatures = rs.getFloat("PERCENT_AGG_CREATURES");
/*  779 */           entry.treeGrowth = rs.getInt("TREEGROWTH");
/*  780 */           TilePoller.treeGrowth = entry.treeGrowth;
/*  781 */           entry.setSkillGainRate(rs.getFloat("SKILLGAINRATE"));
/*  782 */           entry.setActionTimer(rs.getFloat("ACTIONTIMER"));
/*  783 */           entry.setHotaDelay(rs.getInt("HOTADELAY"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  790 */         catch (Exception ex) {
/*      */ 
/*      */           
/*  793 */           logger.log(Level.WARNING, "Please run USE WURMLOGIN;    ALTER TABLE SERVERS ADD COLUMN MAXPLAYERS INT NOT NULL DEFAULT 1000;    ALTER TABLE SERVERS ADD COLUMN MAXCREATURES INT NOT NULL DEFAULT 1000;    ALTER TABLE SERVERS ADD COLUMN PERCENT_AGG_CREATURES FLOAT NOT NULL DEFAULT 30;    ALTER TABLE SERVERS ADD COLUMN TREEGROWTH INT NOT NULL DEFAULT 20;    ALTER TABLE SERVERS ADD COLUMN SKILLGAINRATE FLOAT NOT NULL DEFAULT 100;    ALTER TABLE SERVERS ADD COLUMN ACTIONTIMER FLOAT NOT NULL DEFAULT 100;    ALTER TABLE SERVERS ADD COLUMN HOTADELAY INT NOT NULL DEFAULT 2160; ALTER TABLE SERVERS ADD COLUMN MESHSIZE INT NOT NULL DEFAULT 2048;");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  799 */           entry.mapname = rs.getString("MAPNAME");
/*      */         }
/*  801 */         catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  807 */           entry.randomSpawns = rs.getBoolean("RANDOMSPAWNS");
/*  808 */           entry.setSkillbasicval(rs.getFloat("SKILLBASICSTART"));
/*  809 */           entry.setSkillfightval(rs.getFloat("SKILLFIGHTINGSTART"));
/*  810 */           entry.setSkillmindval(rs.getFloat("SKILLMINDLOGICSTART"));
/*  811 */           entry.setSkilloverallval(rs.getFloat("SKILLOVERALLSTART"));
/*  812 */           entry.EPIC = rs.getBoolean("EPIC");
/*  813 */           entry.setCombatRatingModifier(rs.getFloat("CRMOD"));
/*  814 */           entry.setSteamServerPassword(rs.getString("STEAMPW"));
/*  815 */           entry.setUpkeep(rs.getBoolean("UPKEEP"));
/*  816 */           entry.setMaxDeedSize(rs.getInt("MAXDEED"));
/*  817 */           entry.setFreeDeeds(rs.getBoolean("FREEDEEDS"));
/*  818 */           entry.setTraderMaxIrons(rs.getInt("TRADERMAX"));
/*  819 */           entry.setInitialTraderIrons(rs.getInt("TRADERINIT"));
/*  820 */           entry.setTunnelingHits(rs.getInt("TUNNELING"));
/*  821 */           entry.setBreedingTimer(rs.getLong("BREEDING"));
/*  822 */           entry.setFieldGrowthTime(rs.getLong("FIELDGROWTH"));
/*  823 */           entry.setKingsmoneyAtRestart(rs.getInt("KINGSMONEY"));
/*  824 */           entry.setMotd(rs.getString("MOTD"));
/*  825 */           entry.setSkillbcval(rs.getFloat("SKILLBODYCONTROLSTART"));
/*      */         }
/*  827 */         catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */         
/*  831 */         byte caHelpGroup = rs.getByte("CAHELPGROUP");
/*  832 */         entry.setCAHelpGroup(caHelpGroup);
/*      */         
/*  834 */         allServers.put(Integer.valueOf(entry.id), entry);
/*  835 */         if (logger.isLoggable(Level.FINE))
/*      */         {
/*  837 */           logger.fine("Loaded server " + entry);
/*      */         }
/*  839 */         if (entry.isLocal) {
/*      */           
/*  841 */           long time = rs.getLong("WORLDTIME");
/*  842 */           if (time > 0L)
/*      */           {
/*  844 */             logger.log(Level.INFO, "Using database entry for time " + time);
/*  845 */             WurmCalendar.setTime(time);
/*  846 */             TilePoller.currentPollTile = rs.getInt("POLLTILE");
/*  847 */             TilePoller.rest = rs.getInt("TILEREST");
/*  848 */             TilePoller.pollModifier = rs.getInt("POLLMOD");
/*  849 */             TilePoller.pollround = rs.getInt("POLLROUND");
/*      */             
/*  851 */             WurmCalendar.checkSpring();
/*  852 */             TilePoller.calcRest();
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*  857 */     } catch (SQLException sqex) {
/*      */       
/*  859 */       logger.log(Level.WARNING, "Failed to load all servers!" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  863 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  864 */       DbConnector.returnConnection(dbcon);
/*  865 */       logger.info("Loaded " + allServers.size() + " servers from the database");
/*      */     } 
/*  867 */     Set<Integer> toDelete = new HashSet<>();
/*  868 */     for (ServerEntry server : allServers.values()) {
/*      */       
/*  870 */       if (server.reloading)
/*  871 */         toDelete.add(Integer.valueOf(server.id)); 
/*  872 */       server.reloading = false;
/*      */     } 
/*  874 */     for (Integer id : toDelete)
/*      */     {
/*  876 */       allServers.remove(id);
/*      */     }
/*  878 */     loadNeighbours();
/*  879 */     ServerProperties.loadProperties();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void moveServerId(ServerEntry entry, int oldId) {
/*  884 */     allServers.remove(Integer.valueOf(oldId));
/*  885 */     allServers.put(Integer.valueOf(entry.getId()), entry);
/*  886 */     if (entry.isLocal) {
/*  887 */       localServer = entry;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void loadLoginServer() {
/*  892 */     for (ServerEntry server : allServers.values()) {
/*      */       
/*  894 */       if (server.LOGINSERVER) {
/*      */         
/*  896 */         loginServer = server;
/*      */         
/*  898 */         logger.log(Level.INFO, "Loaded loginserver " + loginServer.id);
/*      */         return;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String rename(String oldName, String newName, String newPass, int power) {
/*  944 */     String toReturn = "";
/*  945 */     if (!localServer.testServer)
/*      */     {
/*  947 */       for (Iterator<ServerEntry> iterator = allServers.values().iterator(); iterator.hasNext(); ) {
/*      */         
/*  949 */         ServerEntry s = iterator.next();
/*  950 */         if (!s.isConnected())
/*  951 */           return "Not all servers are connected (" + s.getName() + "). Try later. This is an Error."; 
/*      */       } 
/*      */     }
/*  954 */     for (Iterator<ServerEntry> it = allServers.values().iterator(); it.hasNext(); ) {
/*      */       
/*  956 */       ServerEntry s = it.next();
/*  957 */       if (s.id != localServer.id) {
/*      */         
/*  959 */         LoginServerWebConnection lsw = new LoginServerWebConnection(s.id);
/*  960 */         toReturn = toReturn + lsw.renamePlayer(oldName, newName, newPass, power);
/*      */       } 
/*      */     } 
/*  963 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String sendChangePass(String changerName, String name, String newPass, int power) {
/*  968 */     String toReturn = "";
/*  969 */     for (Iterator<ServerEntry> it = allServers.values().iterator(); it.hasNext(); ) {
/*      */       
/*  971 */       ServerEntry s = it.next();
/*  972 */       if (s.id != localServer.id) {
/*      */         
/*  974 */         LoginServerWebConnection lsw = new LoginServerWebConnection(s.id);
/*  975 */         toReturn = toReturn + lsw.changePassword(changerName, name, newPass, power);
/*      */       } 
/*      */     } 
/*  978 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String requestDemigod(byte existingDeity, String deityName) {
/*  983 */     String toReturn = "";
/*  984 */     int max = 0; Iterator<ServerEntry> it;
/*  985 */     for (it = allServers.values().iterator(); it.hasNext(); ) {
/*      */       
/*  987 */       ServerEntry s = it.next();
/*  988 */       if (s.id != localServer.id && s.EPIC)
/*      */       {
/*  990 */         max++;
/*      */       }
/*      */     } 
/*  993 */     if (max > 0)
/*      */     {
/*  995 */       if (localServer.testServer) {
/*      */         
/*  997 */         LoginServerWebConnection lsw = new LoginServerWebConnection(63500);
/*  998 */         lsw.requestDemigod(existingDeity, deityName);
/*      */       } else {
/*      */         
/* 1001 */         for (it = allServers.values().iterator(); it.hasNext(); ) { ServerEntry s = it.next();
/*      */           
/* 1003 */           if (s.id != localServer.id && s.EPIC)
/*      */           {
/*      */             
/* 1006 */             if (Server.rand.nextInt(max) == 0) {
/*      */               
/* 1008 */               LoginServerWebConnection lsw = new LoginServerWebConnection(s.id);
/* 1009 */               lsw.requestDemigod(existingDeity, deityName);
/*      */             }  }  }
/*      */       
/*      */       } 
/*      */     }
/* 1014 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String ascend(int nextDeityId, String deityname, long wurmid, byte existingDeity, byte gender, byte newPower, float initialBStr, float initialBSta, float initialBCon, float initialML, float initialMS, float initialSS, float initialSD) {
/* 1021 */     String toReturn = "";
/* 1022 */     for (Iterator<ServerEntry> it = allServers.values().iterator(); it.hasNext(); ) {
/*      */       
/* 1024 */       ServerEntry s = it.next();
/* 1025 */       if (s.id != localServer.id) {
/*      */         
/* 1027 */         LoginServerWebConnection lsw = new LoginServerWebConnection(s.id);
/* 1028 */         lsw.ascend(nextDeityId, deityname, wurmid, existingDeity, gender, newPower, initialBStr, initialBSta, initialBCon, initialML, initialMS, initialSS, initialSD);
/*      */       } 
/*      */     } 
/*      */     
/* 1032 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String changeEmail(String changerName, String name, String newEmail, String password, int power, String pwQuestion, String pwAnswer) {
/* 1038 */     String toReturn = "";
/* 1039 */     for (Iterator<ServerEntry> it = allServers.values().iterator(); it.hasNext(); ) {
/*      */       
/* 1041 */       ServerEntry s = it.next();
/* 1042 */       if (s.id != localServer.id) {
/*      */         
/* 1044 */         if (s.isAvailable(5, true)) {
/*      */           
/* 1046 */           LoginServerWebConnection lsw = new LoginServerWebConnection(s.id);
/* 1047 */           toReturn = toReturn + lsw.changeEmail(changerName, name, newEmail, password, power, pwQuestion, pwAnswer);
/*      */           continue;
/*      */         } 
/* 1050 */         toReturn = toReturn + s.name + " was unavailable. ";
/*      */       } 
/*      */     } 
/* 1053 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry[] getAllServers() {
/* 1062 */     return (ServerEntry[])allServers.values().toArray((Object[])new ServerEntry[allServers.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry[] getAllNeighbours() {
/* 1071 */     return (ServerEntry[])neighbours.values().toArray((Object[])new ServerEntry[neighbours.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ServerEntry getEntryServer() {
/* 1080 */     ServerEntry[] alls = getAllServers();
/* 1081 */     for (ServerEntry lAll : alls) {
/* 1082 */       if (lAll.entryServer)
/* 1083 */         return lAll; 
/* 1084 */     }  return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<ServerEntry> getServerList(int desiredNum) {
/* 1108 */     boolean getEpicServers = (desiredNum == 100001);
/* 1109 */     boolean getFreedomServers = (desiredNum == 100000);
/* 1110 */     boolean getChallengeServers = (desiredNum == 100002);
/* 1111 */     List<ServerEntry> lAskedServers = new ArrayList<>();
/* 1112 */     for (ServerEntry lServerEntry : allServers.values()) {
/*      */       
/* 1114 */       if (desiredNum == lServerEntry.id || (getEpicServers && lServerEntry.EPIC != localServer.EPIC) || (getChallengeServers && lServerEntry
/*      */         
/* 1116 */         .isChallengeServer()) || (getFreedomServers && (!lServerEntry.PVPSERVER || lServerEntry.id == 3)))
/*      */       {
/*      */         
/* 1119 */         lAskedServers.add(lServerEntry);
/*      */       }
/*      */     } 
/* 1122 */     return lAskedServers;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ServerEntry getServerWithId(int id) {
/* 1127 */     if (loginServer != null && loginServer.id == id)
/* 1128 */       return loginServer; 
/* 1129 */     if (localServer != null) {
/*      */       
/* 1131 */       if (localServer.serverNorth != null && localServer.serverNorth.id == id)
/* 1132 */         return localServer.serverNorth; 
/* 1133 */       if (localServer.serverSouth != null && localServer.serverSouth.id == id)
/* 1134 */         return localServer.serverSouth; 
/* 1135 */       if (localServer.serverWest != null && localServer.serverWest.id == id)
/* 1136 */         return localServer.serverWest; 
/* 1137 */       if (localServer.serverEast != null && localServer.serverEast.id == id)
/* 1138 */         return localServer.serverEast; 
/*      */     } 
/* 1140 */     ServerEntry[] alls = getAllServers();
/* 1141 */     for (ServerEntry entry : alls) {
/*      */       
/* 1143 */       if (entry.id == id)
/* 1144 */         return entry; 
/*      */     } 
/* 1146 */     return null;
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
/*      */   public static ServerEntry getClosestSpawnServer(byte aKingdom) {
/* 1162 */     if (localServer.serverNorth != null && (localServer.serverNorth
/* 1163 */       .kingdomExists(aKingdom) || localServer.serverNorth.KINGDOM == aKingdom))
/* 1164 */       return localServer.serverNorth; 
/* 1165 */     if (localServer.serverSouth != null && (localServer.serverSouth
/* 1166 */       .kingdomExists(aKingdom) || localServer.serverSouth.KINGDOM == aKingdom))
/* 1167 */       return localServer.serverSouth; 
/* 1168 */     if (localServer.serverWest != null && (localServer.serverWest
/* 1169 */       .kingdomExists(aKingdom) || localServer.serverWest.KINGDOM == aKingdom))
/* 1170 */       return localServer.serverWest; 
/* 1171 */     if (localServer.serverEast != null && (localServer.serverEast
/* 1172 */       .kingdomExists(aKingdom) || localServer.serverEast.KINGDOM == aKingdom)) {
/* 1173 */       return localServer.serverEast;
/*      */     }
/*      */     
/* 1176 */     if (localServer.serverNorth != null && !localServer.serverNorth.HOMESERVER)
/* 1177 */       return localServer.serverNorth; 
/* 1178 */     if (localServer.serverSouth != null && !localServer.serverSouth.HOMESERVER)
/* 1179 */       return localServer.serverSouth; 
/* 1180 */     if (localServer.serverWest != null && !localServer.serverWest.HOMESERVER)
/* 1181 */       return localServer.serverWest; 
/* 1182 */     if (localServer.serverEast != null && !localServer.serverEast.HOMESERVER) {
/* 1183 */       return localServer.serverEast;
/*      */     }
/* 1185 */     ServerEntry[] alls = getAllNeighbours();
/* 1186 */     for (ServerEntry entry : alls) {
/*      */ 
/*      */       
/* 1189 */       if (entry.EPIC == localServer.EPIC && entry.isChallengeServer() == localServer.isChallengeServer())
/*      */       {
/* 1191 */         if (entry.kingdomExists(aKingdom) || entry.KINGDOM == aKingdom)
/* 1192 */           return entry; 
/*      */       }
/*      */     } 
/* 1195 */     return null;
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
/*      */   
/*      */   public static ServerEntry getClosestJennHomeServer() {
/* 1212 */     if (localServer.serverNorth != null && localServer.serverNorth.HOMESERVER && localServer.serverNorth.KINGDOM == 1)
/*      */     {
/* 1214 */       return localServer.serverNorth; } 
/* 1215 */     if (localServer.serverSouth != null && localServer.serverSouth.HOMESERVER && localServer.serverSouth.KINGDOM == 1)
/*      */     {
/* 1217 */       return localServer.serverSouth; } 
/* 1218 */     if (localServer.serverWest != null && localServer.serverWest.HOMESERVER && localServer.serverWest.KINGDOM == 1)
/*      */     {
/* 1220 */       return localServer.serverWest; } 
/* 1221 */     if (localServer.serverEast != null && localServer.serverEast.HOMESERVER && localServer.serverEast.KINGDOM == 1)
/*      */     {
/* 1223 */       return localServer.serverEast; } 
/* 1224 */     ServerEntry[] alls = getAllNeighbours();
/* 1225 */     for (ServerEntry entry : alls) {
/*      */       
/* 1227 */       if (entry.HOMESERVER && entry.KINGDOM == 1)
/* 1228 */         return entry; 
/*      */     } 
/* 1230 */     if (localServer.serverNorth != null && !localServer.serverNorth.HOMESERVER)
/* 1231 */       return localServer.serverNorth; 
/* 1232 */     if (localServer.serverSouth != null && !localServer.serverNorth.HOMESERVER)
/* 1233 */       return localServer.serverSouth; 
/* 1234 */     if (localServer.serverWest != null && !localServer.serverNorth.HOMESERVER)
/* 1235 */       return localServer.serverWest; 
/* 1236 */     if (localServer.serverEast != null && !localServer.serverNorth.HOMESERVER)
/* 1237 */       return localServer.serverEast; 
/* 1238 */     for (ServerEntry entry : alls) {
/*      */       
/* 1240 */       if (!entry.HOMESERVER)
/* 1241 */         return entry; 
/*      */     } 
/* 1243 */     return null;
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
/*      */   
/*      */   public static ServerEntry getClosestMolRehanHomeServer() {
/* 1260 */     if (localServer.serverNorth != null && localServer.serverNorth.HOMESERVER && localServer.serverNorth.KINGDOM == 2)
/*      */     {
/* 1262 */       return localServer.serverNorth; } 
/* 1263 */     if (localServer.serverSouth != null && localServer.serverSouth.HOMESERVER && localServer.serverSouth.KINGDOM == 2)
/*      */     {
/* 1265 */       return localServer.serverSouth; } 
/* 1266 */     if (localServer.serverWest != null && localServer.serverWest.HOMESERVER && localServer.serverWest.KINGDOM == 2)
/*      */     {
/* 1268 */       return localServer.serverWest; } 
/* 1269 */     if (localServer.serverEast != null && localServer.serverEast.HOMESERVER && localServer.serverEast.KINGDOM == 2)
/*      */     {
/* 1271 */       return localServer.serverEast; } 
/* 1272 */     ServerEntry[] alls = getAllNeighbours();
/* 1273 */     for (ServerEntry entry : alls) {
/*      */       
/* 1275 */       if (entry.HOMESERVER && entry.KINGDOM == 2)
/* 1276 */         return entry; 
/*      */     } 
/* 1278 */     if (localServer.serverNorth != null && !localServer.serverNorth.HOMESERVER)
/* 1279 */       return localServer.serverNorth; 
/* 1280 */     if (localServer.serverSouth != null && !localServer.serverNorth.HOMESERVER)
/* 1281 */       return localServer.serverSouth; 
/* 1282 */     if (localServer.serverWest != null && !localServer.serverNorth.HOMESERVER)
/* 1283 */       return localServer.serverWest; 
/* 1284 */     if (localServer.serverEast != null && !localServer.serverNorth.HOMESERVER)
/* 1285 */       return localServer.serverEast; 
/* 1286 */     for (ServerEntry entry : alls) {
/*      */       
/* 1288 */       if (!entry.HOMESERVER)
/* 1289 */         return entry; 
/*      */     } 
/* 1291 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sendWeather(float windRotation, float windpower, float windDir) {
/* 1296 */     ServerEntry[] alls = getAllServers();
/* 1297 */     for (ServerEntry entry : alls) {
/*      */       
/* 1299 */       if (entry.id != localServer.id) {
/*      */         
/* 1301 */         LoginServerWebConnection lsw = new LoginServerWebConnection(entry.id);
/* 1302 */         lsw.setWeather(windRotation, windpower, windDir);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pingServers() {
/* 1309 */     if (localServer.serverNorth != null) {
/*      */       
/* 1311 */       boolean pollResult = localServer.serverNorth.poll();
/* 1312 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1314 */         logger.finer("Polling north server result: " + pollResult + ", " + localServer.serverNorth);
/*      */       }
/*      */     } 
/* 1317 */     if (localServer.serverEast != null) {
/*      */       
/* 1319 */       boolean pollResult = localServer.serverEast.poll();
/* 1320 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1322 */         logger.finer("Polling east server result: " + pollResult + ", " + localServer.serverEast);
/*      */       }
/*      */     } 
/* 1325 */     if (localServer.serverSouth != null) {
/*      */       
/* 1327 */       boolean pollResult = localServer.serverSouth.poll();
/* 1328 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1330 */         logger.finer("Polling south server result: " + pollResult + ", " + localServer.serverSouth);
/*      */       }
/*      */     } 
/* 1333 */     if (localServer.serverWest != null) {
/*      */       
/* 1335 */       boolean pollResult = localServer.serverWest.poll();
/* 1336 */       if (logger.isLoggable(Level.FINER))
/*      */       {
/* 1338 */         logger.finer("Polling west server result: " + pollResult + ", " + localServer.serverWest);
/*      */       }
/*      */     } 
/* 1341 */     if (!localServer.LOGINSERVER) {
/*      */       
/* 1343 */       if (loginServer != null) {
/*      */         
/* 1345 */         boolean pollResult = loginServer.poll();
/* 1346 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1348 */           logger.finer("Polling login server result: " + pollResult + ", " + loginServer);
/*      */         }
/*      */       } 
/* 1351 */       for (ServerEntry portal : neighbours.values())
/*      */       {
/* 1353 */         if (portal != localServer.serverEast && 
/* 1354 */           portal != localServer.serverWest && 
/* 1355 */           portal != localServer.serverNorth && 
/* 1356 */           portal != localServer.serverSouth) {
/* 1357 */           portal.poll();
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1364 */       for (ServerEntry entry : allServers.values()) {
/*      */         
/* 1366 */         if (!entry.LOGINSERVER) {
/*      */           
/* 1368 */           boolean pollResult = entry.poll();
/* 1369 */           if (logger.isLoggable(Level.FINER))
/*      */           {
/* 1371 */             logger.finer("Polling server id " + entry.id + " result: " + pollResult + ", " + entry);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendKingdomExistsToAllServers(int serverId, byte kingdomId, boolean exists) {
/* 1380 */     for (ServerEntry entry : allServers.values()) {
/*      */       
/* 1382 */       if (entry.isAvailable(5, true) && entry.id != localServer.id)
/*      */       {
/*      */         
/* 1385 */         if (entry.id != serverId) {
/*      */           
/* 1387 */           LoginServerWebConnection lsw = new LoginServerWebConnection(entry.id);
/* 1388 */           lsw.kingdomExists(serverId, kingdomId, exists);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendWebCommandToAllServers(final short type, final WebCommand command, final boolean restrictEpic) {
/* 1396 */     (new Thread()
/*      */       {
/*      */         
/*      */         public void run()
/*      */         {
/* 1401 */           for (ServerEntry entry : Servers.allServers.values()) {
/*      */ 
/*      */             
/* 1404 */             if (entry.isAvailable(5, true) && entry.id != Servers.localServer.id)
/*      */             {
/*      */               
/* 1407 */               if (entry.id != WurmId.getOrigin(command.getWurmId()))
/*      */               {
/*      */                 
/* 1410 */                 if (entry.EPIC || !restrictEpic) {
/*      */                   
/* 1412 */                   LoginServerWebConnection lsw = new LoginServerWebConnection(entry.id);
/* 1413 */                   lsw.sendWebCommand(type, command);
/*      */                 } 
/*      */               }
/*      */             }
/*      */           } 
/*      */         }
/* 1419 */       }).start();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean kingdomExists(int serverId, byte kingdomId, boolean exists) {
/* 1424 */     if (!exists) {
/*      */       
/* 1426 */       if (localServer.serverEast != null && localServer.serverEast.id == serverId)
/* 1427 */         localServer.serverEast.removeKingdom(kingdomId); 
/* 1428 */       if (localServer.serverWest != null && localServer.serverWest.id == serverId)
/* 1429 */         localServer.serverWest.removeKingdom(kingdomId); 
/* 1430 */       if (localServer.serverNorth != null && localServer.serverNorth.id == serverId)
/* 1431 */         localServer.serverNorth.removeKingdom(kingdomId); 
/* 1432 */       if (localServer.serverSouth != null && localServer.serverSouth.id == serverId) {
/* 1433 */         localServer.serverSouth.removeKingdom(kingdomId);
/*      */       }
/* 1435 */       for (ServerEntry portal : neighbours.values()) {
/*      */         
/* 1437 */         if (portal != localServer.serverEast && 
/* 1438 */           portal != localServer.serverWest && 
/* 1439 */           portal != localServer.serverNorth && 
/* 1440 */           portal != localServer.serverSouth && 
/* 1441 */           portal.id == serverId)
/* 1442 */           portal.removeKingdom(kingdomId); 
/*      */       } 
/* 1444 */       ServerEntry serverEntry = allServers.get(Integer.valueOf(serverId));
/* 1445 */       if (serverEntry != null)
/* 1446 */         serverEntry.removeKingdom(kingdomId); 
/* 1447 */       if (localServer.id == serverId)
/* 1448 */         localServer.removeKingdom(kingdomId); 
/* 1449 */       if (loginServer.id == serverId) {
/* 1450 */         loginServer.removeKingdom(kingdomId);
/*      */       }
/* 1452 */       for (ServerEntry se : allServers.values()) {
/*      */         
/* 1454 */         if (se.kingdomExists(kingdomId) && se.id != serverId)
/* 1455 */           return true; 
/*      */       } 
/* 1457 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1461 */     if (localServer.serverEast != null && localServer.serverEast.id == serverId)
/* 1462 */       localServer.serverEast.addExistingKingdom(kingdomId); 
/* 1463 */     if (localServer.serverWest != null && localServer.serverWest.id == serverId)
/* 1464 */       localServer.serverWest.addExistingKingdom(kingdomId); 
/* 1465 */     if (localServer.serverNorth != null && localServer.serverNorth.id == serverId)
/* 1466 */       localServer.serverNorth.addExistingKingdom(kingdomId); 
/* 1467 */     if (localServer.serverSouth != null && localServer.serverSouth.id == serverId)
/* 1468 */       localServer.serverSouth.addExistingKingdom(kingdomId); 
/* 1469 */     if (neighbours != null)
/*      */     {
/* 1471 */       for (ServerEntry portal : neighbours.values()) {
/*      */         
/* 1473 */         if (portal != localServer.serverEast && 
/* 1474 */           portal != localServer.serverWest && 
/* 1475 */           portal != localServer.serverNorth && 
/* 1476 */           portal != localServer.serverSouth && 
/* 1477 */           portal.id == serverId)
/* 1478 */           portal.addExistingKingdom(kingdomId); 
/*      */       } 
/*      */     }
/* 1481 */     if (localServer.id == serverId)
/* 1482 */       localServer.addExistingKingdom(kingdomId); 
/* 1483 */     if (loginServer.id == serverId) {
/* 1484 */       loginServer.addExistingKingdom(kingdomId);
/*      */     }
/* 1486 */     ServerEntry e = allServers.get(Integer.valueOf(serverId));
/* 1487 */     if (e != null) {
/*      */       
/* 1489 */       e.addExistingKingdom(kingdomId);
/*      */     } else {
/*      */       
/* 1492 */       logger.log(Level.WARNING, "No such server - " + serverId + ", kingdom=" + kingdomId + ", exists: " + exists);
/*      */     } 
/*      */     
/* 1495 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void removeKingdomInfo(byte kingdomId) {
/* 1506 */     if (localServer.serverEast != null)
/* 1507 */       localServer.serverEast.removeKingdom(kingdomId); 
/* 1508 */     if (localServer.serverWest != null)
/* 1509 */       localServer.serverWest.removeKingdom(kingdomId); 
/* 1510 */     if (localServer.serverNorth != null)
/* 1511 */       localServer.serverNorth.removeKingdom(kingdomId); 
/* 1512 */     if (localServer.serverSouth != null)
/* 1513 */       localServer.serverSouth.removeKingdom(kingdomId); 
/* 1514 */     for (ServerEntry portal : neighbours.values()) {
/*      */       
/* 1516 */       if (portal != localServer.serverEast && 
/* 1517 */         portal != localServer.serverWest && 
/* 1518 */         portal != localServer.serverNorth && 
/* 1519 */         portal != localServer.serverSouth) {
/* 1520 */         portal.removeKingdom(kingdomId);
/*      */       }
/*      */     } 
/* 1523 */     for (ServerEntry entry : allServers.values())
/*      */     {
/* 1525 */       entry.removeKingdom(kingdomId);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberOfLoyalServers(int deityId) {
/* 1531 */     int kingdomTemplate = Deities.getFavoredKingdom(deityId);
/* 1532 */     int toReturn = 0;
/* 1533 */     for (ServerEntry entry : allServers.values()) {
/*      */       
/* 1535 */       if (!entry.LOGINSERVER)
/*      */       {
/* 1537 */         if (entry.EPIC) {
/*      */           
/* 1539 */           if (!entry.HOMESERVER) {
/* 1540 */             toReturn++; continue;
/* 1541 */           }  if (entry.KINGDOM == kingdomTemplate)
/* 1542 */             toReturn++; 
/*      */         } 
/*      */       }
/*      */     } 
/* 1546 */     return toReturn;
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
/*      */   public static void startShutdown(String instigator, int seconds, String reason) {
/* 1559 */     if (isThisLoginServer()) {
/*      */ 
/*      */       
/* 1562 */       for (ServerEntry server : getAllServers())
/*      */       {
/* 1564 */         if (server.id != getLocalServerId())
/*      */         {
/* 1566 */           LoginServerWebConnection lsw = new LoginServerWebConnection(server.id);
/* 1567 */           lsw.startShutdown(instigator, seconds, reason);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1574 */       LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 1575 */       lsw.startShutdown(instigator, seconds, reason);
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
/*      */   public static final boolean mayEnterServer(Creature _player, ServerEntry entry) {
/* 1588 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAvailableDestination(Creature performer, ServerEntry entry) {
/* 1599 */     if (entry.isAvailable(5, true))
/* 1600 */       return true; 
/* 1601 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ServerEntry[] getDestinations(Creature performer) {
/* 1606 */     ServerEntry[] allServers = getAllServers();
/* 1607 */     List<ServerEntry> servers = new ArrayList<>();
/*      */     
/* 1609 */     Arrays.sort((Object[])allServers);
/* 1610 */     for (ServerEntry entry : allServers) {
/* 1611 */       if (isAvailableDestination(performer, entry)) {
/* 1612 */         servers.add(entry);
/*      */       }
/*      */     } 
/* 1615 */     servers.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
/* 1616 */     return servers.<ServerEntry>toArray(new ServerEntry[servers.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static ServerEntry getDestinationFor(Creature performer) {
/* 1621 */     if (performer.isVehicleCommander() && performer.getVehicle() != -10L) {
/*      */       
/* 1623 */       Vehicle vehicle = Vehicles.getVehicleForId(performer.getVehicle());
/*      */       
/* 1625 */       if (vehicle.hasDestinationSet() && mayEnterServer(performer, vehicle.getDestinationServer())) {
/* 1626 */         return vehicle.getDestinationServer();
/*      */       }
/* 1628 */     } else if (performer.getDestination() != null) {
/* 1629 */       return performer.getDestination();
/*      */     } 
/* 1631 */     return localServer;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Servers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */