/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.endgames.EndGameItem;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Artist;
/*      */ import com.wurmonline.server.players.Ban;
/*      */ import com.wurmonline.server.players.DbSearcher;
/*      */ import com.wurmonline.server.players.IPBan;
/*      */ import com.wurmonline.server.players.KingdomIp;
/*      */ import com.wurmonline.server.players.MapAnnotation;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.PlayerKills;
/*      */ import com.wurmonline.server.players.PlayerState;
/*      */ import com.wurmonline.server.players.SteamIdBan;
/*      */ import com.wurmonline.server.players.TabData;
/*      */ import com.wurmonline.server.players.WurmRecord;
/*      */ import com.wurmonline.server.questions.KosWarningInfo;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.steam.SteamId;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.support.Ticket;
/*      */ import com.wurmonline.server.support.TicketAction;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.support.VoteQuestion;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.MissionPerformer;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.KosWarning;
/*      */ import com.wurmonline.server.villages.PvPAlliance;
/*      */ import com.wurmonline.server.villages.Reputation;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WCGmMessage;
/*      */ import com.wurmonline.server.webinterface.WcDemotion;
/*      */ import com.wurmonline.server.webinterface.WcTabLists;
/*      */ import com.wurmonline.server.zones.Trap;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.EffectConstants;
/*      */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*      */ import com.wurmonline.website.StatsXMLWriter;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.logging.FileHandler;
/*      */ import java.util.logging.Handler;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.logging.SimpleFormatter;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ public final class Players
/*      */   implements MiscConstants, CreatureTemplateIds, EffectConstants, MonetaryConstants, TimeConstants
/*      */ {
/*   95 */   private static Map<String, Player> players = new ConcurrentHashMap<>();
/*      */   
/*   97 */   private static Map<Long, Player> playersById = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*  100 */   private final Map<Long, Byte> pkingdoms = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   private static final ConcurrentHashMap<String, TabData> tabListGM = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */   
/*  109 */   private static final ConcurrentHashMap<String, TabData> tabListMGMT = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  116 */   private static Players instance = null;
/*      */ 
/*      */   
/*  119 */   private static Logger logger = Logger.getLogger(Players.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DOES_PLAYER_NAME_EXIST = "SELECT WURMID FROM PLAYERS WHERE NAME=?";
/*      */ 
/*      */ 
/*      */   
/*  127 */   private static final Map<Long, Artist> artists = new HashMap<>();
/*      */   
/*      */   private static final String GET_ARTISTS = "SELECT * FROM ARTISTS";
/*      */   
/*      */   private static final String SET_ARTISTS = "INSERT INTO ARTISTS (WURMID,SOUND,GRAPHICS) VALUES(?,?,?)";
/*      */   
/*      */   private static final String DELETE_ARTIST = "DELETE FROM ARTISTS WHERE WURMID=?";
/*      */   
/*      */   private static final String GET_PLAYERS_BANNED = "SELECT NAME,BANREASON,BANEXPIRY FROM PLAYERS WHERE BANNED=1";
/*      */   
/*      */   private static final String SET_NOSTRUCTURE = "update PLAYERS set BUILDINGID=-10 WHERE BUILDINGID=?";
/*      */   
/*      */   private static final String GET_LASTLOGOUT = "SELECT LASTLOGOUT FROM PLAYERS WHERE WURMID=?";
/*      */   
/*      */   private static final String GET_KINGDOM = "SELECT KINGDOM FROM PLAYERS WHERE WURMID=?";
/*      */   
/*      */   private static final String GET_KINGDOM_PLAYERS = "SELECT NAME,WURMID FROM PLAYERS WHERE KINGDOM=? AND CURRENTSERVER=? AND POWER=0";
/*      */   
/*      */   private static final String GET_PREMIUM_KINGDOM_PLAYERS = "SELECT NAME,WURMID FROM PLAYERS WHERE KINGDOM=? AND PAYMENTEXPIRE>? AND POWER=0";
/*      */   
/*      */   private static final String GET_CHAMPION_KINGDOM_PLAYERS = "SELECT NAME,WURMID,REALDEATH,LASTLOSTCHAMPION FROM PLAYERS WHERE KINGDOM=? AND REALDEATH>0 AND REALDEATH<4 AND POWER=0";
/*      */   
/*      */   private static final String RESET_FAITHGAIN = "UPDATE PLAYERS SET LASTFAITH=0,NUMFAITH=0";
/*      */   
/*      */   private static final String GM_SALARY = "UPDATE PLAYERS SET MONEY=MONEY+250000 WHERE POWER>1";
/*      */   
/*      */   private static final String RESET_PLAYER_SKILLS = "UPDATE SKILLS SET VALUE=20, MINVALUE=20 WHERE VALUE>20 AND OWNER=?";
/*      */   
/*      */   private static final String RESET_PLAYER_FAITH = "UPDATE PLAYERS SET FAITH=20 WHERE FAITH>20 AND WURMID=?";
/*      */   
/*      */   private static final String ADD_GM_MESSAGE = "INSERT INTO GMMESSAGES(TIME,SENDER,MESSAGE) VALUES(?,?,?)";
/*      */   
/*      */   private static final String ADD_MGMT_MESSAGE = "INSERT INTO MGMTMESSAGES(TIME,SENDER,MESSAGE) VALUES(?,?,?)";
/*      */   
/*      */   private static final String GET_GM_MESSAGES = "SELECT TIME,SENDER,MESSAGE FROM GMMESSAGES ORDER BY TIME";
/*      */   
/*      */   private static final String GET_MGMT_MESSAGES = "SELECT TIME,SENDER,MESSAGE FROM MGMTMESSAGES ORDER BY TIME";
/*      */   
/*      */   private static final String PRUNE_GM_MESSAGES = "DELETE FROM GMMESSAGES WHERE TIME<?";
/*      */   
/*      */   private static final String PRUNE_MGMT_MESSAGES = "DELETE FROM MGMTMESSAGES WHERE TIME<?";
/*      */   
/*      */   private static final String GET_BATTLE_RANKS = "select RANK, NAME from PLAYERS ORDER BY RANK DESC LIMIT ?";
/*      */   
/*      */   private static final String GET_MAXBATTLE_RANKS = "select MAXRANK,RANK, NAME from PLAYERS ORDER BY MAXRANK DESC LIMIT ?";
/*      */   
/*      */   private static final String GET_FRIENDS = "select p.NAME,p.WURMID from PLAYERS p INNER JOIN FRIENDS f ON f.FRIEND=p.WURMID WHERE f.WURMID=? ORDER BY NAME";
/*      */   
/*      */   private static final String GET_PLAYERID_BY_NAME = "SELECT WURMID FROM PLAYERS WHERE NAME=?";
/*      */   
/*      */   private static final String GET_PLAYERS_MUTED = "SELECT NAME,MUTEREASON,MUTEEXPIRY FROM PLAYERS WHERE MUTED=1";
/*      */   
/*      */   private static final String GET_MUTERS = "SELECT NAME FROM PLAYERS WHERE MAYMUTE=1";
/*      */   
/*      */   private static final String GET_DEVTALKERS = "SELECT NAME FROM PLAYERS WHERE DEVTALK=1";
/*      */   
/*      */   private static final String GET_CAS = "SELECT NAME FROM PLAYERS WHERE PA=1";
/*      */   
/*      */   private static final String GET_HEROS = "SELECT NAME FROM PLAYERS WHERE POWER=? AND CURRENTSERVER=?";
/*      */   
/*      */   private static final String GET_PRIVATE_MAP_POI = "SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=0 AND OWNERID=?";
/*      */   
/*      */   private static final String CHANGE_KINGDOM = "UPDATE PLAYERS SET KINGDOM=? WHERE KINGDOM=?";
/*      */   
/*      */   private static final String CHANGE_KINGDOM_FOR_PLAYER = "UPDATE PLAYERS SET KINGDOM=? WHERE WURMID=?";
/*      */   
/*      */   public static final String CACHAN = "CA HELP";
/*      */   
/*      */   public static final String GVCHAN = "GV HELP";
/*      */   
/*      */   public static final String JKCHAN = "JK HELP";
/*      */   
/*      */   public static final String MRCHAN = "MR HELP";
/*      */   
/*      */   public static final String HOTSCHAN = "HOTS HELP";
/*      */   private static final String CAPREFIX = " CA ";
/*  203 */   private static final Map<String, Logger> loggers = new HashMap<>();
/*      */ 
/*      */   
/*  206 */   private static Set<Ban> bans = new HashSet<>();
/*      */   
/*  208 */   private static final Map<Long, PlayerKills> playerKills = new ConcurrentHashMap<>();
/*      */   
/*  210 */   private final Map<Byte, Float> crBonuses = new HashMap<>();
/*      */   
/*      */   private boolean shouldSendWeather = false;
/*      */   
/*  214 */   private final long timeBetweenChampDecreases = 604800000L;
/*      */ 
/*      */ 
/*      */   
/*  218 */   private static ConcurrentLinkedQueue<KosWarning> kosList = new ConcurrentLinkedQueue<>();
/*      */   
/*  220 */   private static String header = "<HTML> <HEAD><TITLE>Wurm battle ranks</TITLE></HEAD><BODY><BR><BR>";
/*      */   
/*  222 */   private long lastPoll = System.currentTimeMillis();
/*      */   
/*      */   private static final float minDelta = 0.095F;
/*      */   
/*      */   private static boolean pollCheckClients = false;
/*  227 */   private long lastCheckClients = System.currentTimeMillis();
/*  228 */   private static int challengeStep = 0;
/*      */   
/*  230 */   private static HashMap<Long, Short> deathCount = new HashMap<>();
/*      */   
/*  232 */   private static final Logger caHelpLogger = Logger.getLogger("ca-help");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Players getInstance() {
/*  240 */     if (instance == null)
/*  241 */       instance = new Players(); 
/*  242 */     return instance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static synchronized Players getInstanceForUnitTestingWithoutDatabase() {
/*  252 */     if (instance == null)
/*  253 */       instance = new Players(true); 
/*  254 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCreatureDead(Creature dead) {
/*  259 */     long deadid = dead.getWurmId();
/*      */     
/*  261 */     Player[] plays = getInstance().getPlayers();
/*  262 */     for (Player player : plays) {
/*      */       
/*  264 */       if (player.opponent == dead)
/*      */       {
/*  266 */         player.setOpponent(null);
/*      */       }
/*  268 */       if (player.target == deadid)
/*  269 */         player.setTarget(-10L, true); 
/*  270 */       player.removeTarget(deadid);
/*      */     } 
/*      */     
/*  273 */     Vehicles.removeDragger(dead);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Players() {
/*  281 */     loadBannedIps();
/*  282 */     loadBannedSteamIds();
/*  283 */     header = getBattleRanksHtmlHeader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Players(boolean forUnitTestingWithoutDatabase) {
/*  291 */     if (forUnitTestingWithoutDatabase) {
/*      */       
/*  293 */       logger.warning("Instantiating Players for Unit Test without a database");
/*      */     }
/*      */     else {
/*      */       
/*  297 */       loadBannedIps();
/*  298 */       loadBannedSteamIds();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getBattleRanksHtmlHeader() {
/*  307 */     return "<HTML> <HEAD><TITLE>Wurm battle ranks on " + Servers.getLocalServerName() + "</TITLE></HEAD><BODY><BR><BR>";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int numberOfPlayers() {
/*  318 */     return players.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int numberOfPremiumPlayers() {
/*  328 */     int x = 0;
/*  329 */     for (Player lPlayer : getInstance().getPlayers()) {
/*      */       
/*  331 */       if (lPlayer.isPaying() && lPlayer.getPower() == 0)
/*  332 */         x++; 
/*      */     } 
/*  334 */     return x;
/*      */   }
/*      */ 
/*      */   
/*      */   public void weatherFlash(int tilex, int tiley, float height) {
/*  339 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/*  341 */       if (p != null) {
/*      */         
/*  343 */         Communicator lPlayerCommunicator = p.getCommunicator();
/*  344 */         if (lPlayerCommunicator != null) {
/*  345 */           lPlayerCommunicator.sendAddEffect(9223372036854775707L, (short)1, (tilex << 2), (tiley << 2), height, (byte)0);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGlobalNonPersistantComplexEffect(long target, short effect, int tilex, int tiley, float height, float radiusMeters, float lengthMeters, int direction, byte kingdomTemplateId, byte epicEntityId) {
/*  355 */     long effectId = Long.MAX_VALUE - Server.rand.nextInt(1000);
/*  356 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/*  358 */       if (p != null) {
/*      */         
/*  360 */         Communicator lPlayerCommunicator = p.getCommunicator();
/*  361 */         if (lPlayerCommunicator != null) {
/*  362 */           lPlayerCommunicator.sendAddComplexEffect(effectId, target, effect, (tilex << 2), (tiley << 2), height, (byte)0, radiusMeters, lengthMeters, direction, kingdomTemplateId, epicEntityId);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGlobalNonPersistantEffect(long id, short effect, int tilex, int tiley, float height) {
/*  371 */     long effectId = Long.MAX_VALUE - Server.rand.nextInt(1000);
/*  372 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/*  374 */       if (p != null) {
/*      */         
/*  376 */         Communicator lPlayerCommunicator = p.getCommunicator();
/*  377 */         if (lPlayerCommunicator != null)
/*      */         {
/*  379 */           lPlayerCommunicator.sendAddEffect((id <= 0L) ? effectId : id, effect, (tilex << 2), (tiley << 2), height, (byte)0);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGlobalNonPersistantTimedEffect(long id, short effect, int tilex, int tiley, float height, long expireTime) {
/*  388 */     long effectId = (id <= 0L) ? (Long.MAX_VALUE - Server.rand.nextInt(10000)) : id;
/*  389 */     Server.getInstance().addGlobalTempEffect(effectId, expireTime);
/*  390 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/*  392 */       if (p != null) {
/*      */         
/*  394 */         Communicator lPlayerCommunicator = p.getCommunicator();
/*  395 */         if (lPlayerCommunicator != null)
/*      */         {
/*  397 */           lPlayerCommunicator.sendAddEffect(effectId, effect, (tilex << 2), (tiley << 2), height, (byte)0);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getChallengeStep() {
/*  405 */     return challengeStep;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setChallengeStep(int step) {
/*  410 */     if (Servers.localServer.isChallengeServer() || Servers.localServer.testServer) {
/*      */       
/*  412 */       challengeStep = step;
/*  413 */       byte toSend = 0;
/*  414 */       switch (challengeStep) {
/*      */         
/*      */         case 1:
/*  417 */           toSend = 20;
/*      */           break;
/*      */         case 2:
/*  420 */           toSend = 21;
/*      */           break;
/*      */         case 3:
/*  423 */           toSend = 22;
/*      */           break;
/*      */         case 4:
/*  426 */           toSend = 23;
/*      */           break;
/*      */         default:
/*  429 */           toSend = 0;
/*      */           break;
/*      */       } 
/*      */       
/*  433 */       if (toSend > 0) {
/*  434 */         sendGlobalNonPersistantEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), (short)toSend, 0, 0, 0.0F);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendPlayerStatus(Player player) {
/*  440 */     for (Player p : getInstance().getPlayers())
/*      */     {
/*  442 */       player.getCommunicator().sendNormalServerMessage(p
/*  443 */           .getName() + ", secstolog=" + p.getSecondsToLogout() + ", logged off=" + p.loggedout);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getOnlinePlayersFromKingdom(byte kingdomId) {
/*  449 */     int nums = 0;
/*  450 */     for (Player lPlayer : getInstance().getPlayers()) {
/*      */       
/*  452 */       if (lPlayer.getKingdomId() == kingdomId)
/*  453 */         nums++; 
/*      */     } 
/*  455 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public Player[] getPlayersByIp() {
/*  460 */     Player[] playerArr = getPlayers();
/*  461 */     Arrays.sort(playerArr, new Comparator<Player>()
/*      */         {
/*      */ 
/*      */           
/*      */           public int compare(Player o1, Player o2)
/*      */           {
/*  467 */             Player i1 = o1;
/*  468 */             Player i2 = o2;
/*  469 */             return i1.getSaveFile().getIpaddress().compareTo(i2.getSaveFile().getIpaddress());
/*      */           }
/*      */         });
/*  472 */     return playerArr;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendIpsToPlayer(Player player) {
/*  477 */     Player[] playerArr = getPlayersByIp();
/*      */     
/*  479 */     for (Player lPlayer : playerArr) {
/*      */       
/*  481 */       if (lPlayer.getPower() <= player.getPower() && player.getPower() > 1)
/*      */       {
/*  483 */         player.getCommunicator().sendNormalServerMessage(lPlayer
/*  484 */             .getName() + " IP: " + lPlayer.getSaveFile().getIpaddress());
/*      */       }
/*      */     } 
/*  487 */     player.getCommunicator().sendNormalServerMessage(playerArr.length + " players logged on.");
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendIpsToPlayer(Player player, String playername) {
/*  492 */     PlayerInfo pinfo = null;
/*      */     
/*      */     try {
/*  495 */       pinfo = getPlayer(playername).getSaveFile();
/*      */     
/*      */     }
/*  498 */     catch (NoSuchPlayerException nsp) {
/*      */       
/*  500 */       pinfo = PlayerInfoFactory.createPlayerInfo(playername);
/*      */       
/*      */       try {
/*  503 */         pinfo.load();
/*      */       }
/*  505 */       catch (IOException iox) {
/*      */         
/*  507 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/*  510 */     if (pinfo != null) {
/*      */       
/*  512 */       if (pinfo.getPower() <= player.getPower() && player.getPower() > 1) {
/*      */         
/*  514 */         Player[] playerArr = getPlayersByIp();
/*  515 */         Map<String, String> ps = new HashMap<>();
/*  516 */         boolean error = false;
/*  517 */         ps.put(playername, pinfo.getIpaddress());
/*  518 */         for (Player lPlayer : playerArr) {
/*      */           
/*  520 */           if (lPlayer.getSaveFile().getIpaddress().equals(pinfo.getIpaddress()))
/*  521 */             ps.put(lPlayer.getName(), lPlayer.getSaveFile().getIpaddress()); 
/*      */         } 
/*  523 */         for (String name : ps.keySet())
/*      */         {
/*  525 */           String ip = ps.get(name);
/*  526 */           player.getCommunicator().sendNormalServerMessage(name + ", " + ip);
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  532 */         player.getCommunicator().sendNormalServerMessage("You may not check that player's ip.");
/*      */       } 
/*      */     } else {
/*  535 */       player.getCommunicator().sendNormalServerMessage(playername + " - not found!");
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void stopLoggers() {
/*  540 */     for (Logger logger : loggers.values()) {
/*      */       
/*  542 */       if (logger != null) {
/*  543 */         for (Handler h : logger.getHandlers())
/*      */         {
/*  545 */           h.close();
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Logger getLogger(Player player) {
/*  552 */     if (player.getPower() > 0 || player.isLogged() || isArtist(player.getWurmId(), false, false)) {
/*      */       
/*  554 */       String name = player.getName();
/*  555 */       Logger personalLogger = loggers.get(name);
/*  556 */       if (personalLogger == null) {
/*      */         
/*  558 */         personalLogger = Logger.getLogger(name);
/*  559 */         personalLogger.setUseParentHandlers(false);
/*  560 */         Handler[] h = logger.getHandlers();
/*  561 */         for (int i = 0; i != h.length; i++)
/*      */         {
/*  563 */           personalLogger.removeHandler(h[i]);
/*      */         }
/*      */         
/*      */         try {
/*  567 */           FileHandler fh = new FileHandler(name + ".log", 0, 1, true);
/*  568 */           fh.setFormatter(new SimpleFormatter());
/*  569 */           personalLogger.addHandler(fh);
/*      */         }
/*  571 */         catch (IOException ie) {
/*      */           
/*  573 */           Logger.getLogger(name).log(Level.WARNING, name + ":no redirection possible!");
/*      */         } 
/*  575 */         loggers.put(name, personalLogger);
/*      */       } 
/*  577 */       return personalLogger;
/*      */     } 
/*  579 */     return null;
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
/*      */   public Player getPlayer(String name) throws NoSuchPlayerException {
/*  595 */     Player p = getPlayerByName(LoginHandler.raiseFirstLetter(name));
/*  596 */     if (p == null)
/*  597 */       throw new NoSuchPlayerException(name); 
/*  598 */     return p;
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
/*      */   private Player getPlayerByName(String aName) {
/*  612 */     return players.get(aName);
/*      */   }
/*      */ 
/*      */   
/*      */   public Player getPlayerOrNull(String aName) {
/*  617 */     return getPlayerByName(aName);
/*      */   }
/*      */ 
/*      */   
/*      */   public Optional<Player> getPlayerOptional(String aName) {
/*  622 */     return Optional.ofNullable(getPlayerByName(aName));
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
/*      */   public Player getPlayer(long id) throws NoSuchPlayerException {
/*  638 */     Player p = getPlayerById(id);
/*  639 */     if (p != null)
/*  640 */       return p; 
/*  641 */     throw new NoSuchPlayerException("Player with id " + id + " could not be found.");
/*      */   }
/*      */ 
/*      */   
/*      */   public Player getPlayerOrNull(long id) {
/*  646 */     return getPlayerById(id);
/*      */   }
/*      */ 
/*      */   
/*      */   public Optional<Player> getPlayerOptional(long id) {
/*  651 */     return Optional.ofNullable(getPlayerById(id));
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
/*      */   public Player getPlayer(Long id) throws NoSuchPlayerException {
/*  667 */     Player p = getPlayerById(id);
/*  668 */     if (p != null)
/*  669 */       return p; 
/*  670 */     throw new NoSuchPlayerException("Player with id " + id + " could not be found.");
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
/*      */   private Player getPlayerById(long aWurmID) {
/*  688 */     return getPlayerById(new Long(aWurmID));
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
/*      */   private Player getPlayerById(Long aWurmID) {
/*  703 */     return playersById.get(aWurmID);
/*      */   }
/*      */ 
/*      */   
/*      */   public Player getPlayer(SocketConnection serverConnection) throws NoSuchPlayerException {
/*  708 */     Player[] playarr = getPlayers();
/*  709 */     for (Player lPlayer : playarr) {
/*      */ 
/*      */       
/*      */       try {
/*  713 */         if (serverConnection == lPlayer.getCommunicator().getConnection()) {
/*  714 */           return lPlayer;
/*      */         }
/*  716 */       } catch (NullPointerException ex) {
/*      */         
/*  718 */         if (lPlayer == null) {
/*  719 */           logger.log(Level.WARNING, "A player in the Players list is null. this shouldn't happen.");
/*  720 */         } else if (lPlayer.getCommunicator() == null) {
/*  721 */           logger.log(Level.WARNING, lPlayer + "'s communicator is null.");
/*      */         } else {
/*  723 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */         } 
/*      */       } 
/*      */     } 
/*  727 */     throw new NoSuchPlayerException("Player could not be found.");
/*      */   }
/*      */ 
/*      */   
/*      */   void sendReconnect(Player player) {
/*  732 */     if (!player.isUndead()) {
/*      */       
/*  734 */       player.getCommunicator().sendClearFriendsList();
/*  735 */       sendConnectInfo(player, " reconnected.", player.getLastLogin(), PlayerOnlineStatus.ONLINE);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void sendAddPlayer(Player player) {
/*  741 */     if (!player.isUndead()) {
/*  742 */       sendConnectInfo(player, " joined.", player.getLastLogin(), PlayerOnlineStatus.ONLINE);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendConnectAlert(String message) {
/*  747 */     Player[] playerArr = getPlayers();
/*  748 */     for (Player lPlayer : playerArr) {
/*      */       
/*  750 */       if (lPlayer.getPower() > 0)
/*      */       {
/*  752 */         lPlayer.getCommunicator().sendAlertServerMessage(message);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendConnectInfo(Player player, String message, long whenStateChanged, PlayerOnlineStatus loginstatus) {
/*  760 */     sendConnectInfo(player, message, whenStateChanged, loginstatus, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendConnectInfo(Player player, String message, long whenStateChanged, PlayerOnlineStatus loginstatus, boolean loggedin) {
/*  767 */     PlayerInfoFactory.updatePlayerState(player, whenStateChanged, loginstatus);
/*      */     
/*  769 */     if (!player.isUndead()) {
/*      */       
/*  771 */       Player[] playerArr = getPlayers();
/*  772 */       int tilex = player.getTileX();
/*  773 */       int tiley = player.getTileY();
/*  774 */       int tilez = (int)(player.getPositionZ() + player.getAltOffZ()) >> 2;
/*  775 */       int vision = 80;
/*      */ 
/*      */       
/*      */       try {
/*  779 */         vision = CreatureTemplateFactory.getInstance().getTemplate(1).getVision();
/*      */       }
/*  781 */       catch (NoSuchCreatureTemplateException nst) {
/*      */         
/*  783 */         logger.log(Level.WARNING, "Failed to find HUMAN_CID. Vision set to " + vision);
/*      */       } 
/*  785 */       Village village = player.getCitizenVillage();
/*      */       
/*  787 */       for (Player lPlayer : playerArr) {
/*      */         
/*  789 */         if (player != lPlayer) {
/*      */           
/*  791 */           if (lPlayer.getPower() > 1) {
/*      */             
/*  793 */             if (player.getCommunicator() != null && player.getCommunicator().getConnection() != null && lPlayer
/*  794 */               .getPower() > player.getPower()) {
/*      */               try
/*      */               {
/*      */                 
/*  798 */                 lPlayer.getCommunicator().sendSystemMessage(player
/*  799 */                     .getName() + "[" + player.getCommunicator().getConnection().getIp() + "] " + message);
/*      */               
/*      */               }
/*  802 */               catch (Exception ex)
/*      */               {
/*  804 */                 lPlayer.getCommunicator().sendSystemMessage(player.getName() + message);
/*      */               }
/*      */             
/*      */             }
/*  808 */           } else if (player.isVisibleTo((Creature)lPlayer) && (!loggedin || player.getPower() <= 1)) {
/*      */ 
/*      */             
/*  811 */             if (!lPlayer.isFriend(player.getWurmId()))
/*      */             {
/*      */ 
/*      */               
/*  815 */               if (village != null && lPlayer.getCitizenVillage() == village) {
/*      */                 
/*  817 */                 lPlayer.getCommunicator().sendSafeServerMessage(player.getName() + message);
/*      */               }
/*  819 */               else if (lPlayer.isOnSurface() == player.isOnSurface() && lPlayer
/*  820 */                 .isWithinTileDistanceTo(tilex, tiley, tilez, vision)) {
/*      */                 
/*  822 */                 lPlayer.getCommunicator().sendSafeServerMessage(player.getName() + message);
/*      */               } 
/*      */             }
/*      */           } 
/*  826 */           if (lPlayer.seesPlayerAssistantWindow() && player.seesPlayerAssistantWindow()) {
/*      */             
/*  828 */             if (player.isVisibleTo((Creature)lPlayer))
/*      */             {
/*  830 */               if (player.isPlayerAssistant()) {
/*  831 */                 lPlayer.getCommunicator().sendAddPa(" CA " + player.getName(), player.getWurmId());
/*      */               }
/*  833 */               else if (shouldReceivePlayerList(lPlayer)) {
/*  834 */                 lPlayer.getCommunicator().sendAddPa(player.getName(), player.getWurmId());
/*      */               }  } 
/*  836 */             if (lPlayer.isVisibleTo((Creature)player))
/*      */             {
/*  838 */               if (lPlayer.isPlayerAssistant()) {
/*  839 */                 player.getCommunicator().sendAddPa(" CA " + lPlayer.getName(), lPlayer.getWurmId());
/*  840 */               } else if (shouldReceivePlayerList(player)) {
/*  841 */                 player.getCommunicator().sendAddPa(lPlayer.getName(), lPlayer.getWurmId());
/*      */               } 
/*      */             }
/*      */           } 
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
/*      */   public void combatRound() {
/*  857 */     for (Player lPlayer : getInstance().getPlayers())
/*      */     {
/*  859 */       lPlayer.getCombatHandler().clearRound();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void pollKosWarnings() {
/*  865 */     for (KosWarning kos : kosList) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  871 */         Player p = getPlayer(kos.playerId);
/*  872 */         if (p.isFullyLoaded() && p.getVisionArea() != null && p.getVisionArea().isInitialized() && p.hasLink()) {
/*      */           
/*  874 */           if (kos.getTick() < 10) {
/*      */             
/*  876 */             if (kos.tick() == 10)
/*      */             {
/*  878 */               if (p.acceptsKosPopups(kos.village.getId()))
/*      */               {
/*  880 */                 KosWarningInfo kwi = new KosWarningInfo((Creature)p, kos.playerId, kos.village);
/*  881 */                 kwi.sendQuestion();
/*      */               }
/*      */               else
/*      */               {
/*  885 */                 p.getCommunicator().sendAlertServerMessage("You are being put on the KOS list of " + kos.village
/*  886 */                     .getName() + " again.", (byte)4);
/*      */               }
/*      */             
/*      */             }
/*  890 */           } else if (kos.getTick() % 30 == 0) {
/*      */             
/*  892 */             if (p.acceptsKosPopups(kos.village.getId()))
/*      */             {
/*  894 */               if (p.getCurrentVillage() == kos.village) {
/*  895 */                 p.getCommunicator().sendAlertServerMessage("You must leave the settlement of " + kos.village
/*  896 */                     .getName() + " immediately or you will be attacked by the guards!", (byte)4);
/*      */               } else {
/*      */                 
/*  899 */                 p.getCommunicator().sendAlertServerMessage("Make sure to stay out of " + kos.village
/*  900 */                     .getName() + " since you soon will be killed on sight there!", (byte)4);
/*      */               } 
/*      */             }
/*      */           } 
/*  904 */           if (kos.getTick() >= 130)
/*      */           {
/*  906 */             if (p.acceptsKosPopups(kos.village.getId())) {
/*  907 */               p.getCommunicator().sendAlertServerMessage("You will now be killed on sight in " + kos.village
/*  908 */                   .getName() + "!", (byte)4);
/*      */             }
/*      */           }
/*      */         } 
/*  912 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  919 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(kos.playerId);
/*  920 */       if (pinf != null) {
/*      */         
/*  922 */         if (kos.getTick() >= 10) {
/*      */ 
/*      */ 
/*      */           
/*  926 */           kos.tick();
/*  927 */           if (kos.getTick() >= 130) {
/*      */             
/*  929 */             Reputation r = kos.village.setReputation(kos.playerId, kos.newReputation, false, true);
/*  930 */             r.setPermanent(kos.permanent);
/*  931 */             kos.village.addHistory(pinf.getName(), "will now be killed on sight.");
/*  932 */             kosList.remove(kos);
/*      */           } 
/*      */         } 
/*      */         continue;
/*      */       } 
/*  937 */       kosList.remove(kos);
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
/*      */   public final boolean addKosWarning(KosWarning newkos) {
/*  952 */     for (KosWarning kosw : kosList) {
/*      */       
/*  954 */       if (kosw.playerId == newkos.playerId)
/*      */       {
/*  956 */         return false;
/*      */       }
/*      */     } 
/*  959 */     kosList.add(newkos);
/*  960 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean removeKosFor(long wurmId) {
/*  965 */     for (KosWarning kos : kosList) {
/*      */       
/*  967 */       if (kos.playerId == wurmId) {
/*      */         
/*  969 */         kosList.remove(kos);
/*  970 */         return true;
/*      */       } 
/*      */     } 
/*  973 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void pollDeadPlayers() {
/*  978 */     Player[] playerarr = getPlayers();
/*  979 */     for (Player lPlayer : playerarr) {
/*      */       
/*  981 */       if (lPlayer != null)
/*      */       {
/*  983 */         if (lPlayer.getSaveFile() != null && 
/*  984 */           lPlayer.pollDead()) {
/*      */           
/*  986 */           logger.log(Level.INFO, "Removing from players " + lPlayer.getName() + ".");
/*  987 */           players.remove(lPlayer.getName());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastMissionInfo(String missionInfo, int missionRelated) {
/*  995 */     Player[] playarr = getPlayers();
/*  996 */     for (int x = 0; x < playarr.length; x++) {
/*      */       
/*  998 */       MissionPerformer mp = MissionPerformed.getMissionPerformer(playarr[x].getWurmId());
/*  999 */       if (mp != null) {
/*      */         
/* 1001 */         MissionPerformed m = mp.getMission(missionRelated);
/* 1002 */         if (m != null)
/*      */         {
/* 1004 */           playarr[x].getCommunicator().sendSafeServerMessage(missionInfo);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastConquerInfo(Creature conquerer, String info) {
/* 1012 */     Player[] playarr = getPlayers();
/* 1013 */     for (int x = 0; x < playarr.length; x++) {
/*      */       
/* 1015 */       int r = 200;
/* 1016 */       int g = 200;
/* 1017 */       int b = 25;
/* 1018 */       if (conquerer.isFriendlyKingdom(playarr[x].getKingdomId())) {
/* 1019 */         r = 25;
/*      */       } else {
/* 1021 */         g = 25;
/* 1022 */       }  playarr[x].getCommunicator().sendDeathServerMessage(info, (byte)r, (byte)g, (byte)25);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void broadCastDestroyInfo(Creature performer, String info) {
/* 1033 */     Player[] playarr = getInstance().getPlayers();
/* 1034 */     for (int x = 0; x < playarr.length; x++) {
/*      */       
/* 1036 */       int r = 200;
/* 1037 */       int g = 200;
/* 1038 */       int b = 25;
/* 1039 */       if (performer.getKingdomId() == playarr[x].getKingdomId()) {
/* 1040 */         r = 25;
/*      */       } else {
/* 1042 */         g = 25;
/* 1043 */       }  playarr[x].getCommunicator().sendDeathServerMessage(info, (byte)r, (byte)g, (byte)25);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void broadCastBashInfo(Item target, String info) {
/* 1053 */     Player[] playarr = getInstance().getPlayers();
/* 1054 */     for (int x = 0; x < playarr.length; x++) {
/*      */       
/* 1056 */       if (target.getKingdom() == playarr[x].getKingdomId()) {
/*      */         
/* 1058 */         int r = 200;
/* 1059 */         int g = 25;
/* 1060 */         int b = 25;
/* 1061 */         playarr[x].getCommunicator().sendDeathServerMessage(info, (byte)r, (byte)g, (byte)b);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void broadCastDeathInfo(Player player, String slayers) {
/* 1069 */     if (Servers.isThisAPvpServer()) {
/*      */       
/* 1071 */       String toSend = player.getName() + " slain by " + slayers;
/* 1072 */       Player[] playarr = getPlayers();
/* 1073 */       for (int x = 0; x < playarr.length; x++)
/*      */       {
/* 1075 */         int r = 200;
/* 1076 */         int g = 200;
/* 1077 */         int b = 25;
/* 1078 */         if (player.isFriendlyKingdom(playarr[x].getKingdomId())) {
/* 1079 */           r = 25;
/*      */         } else {
/* 1081 */           g = 25;
/* 1082 */         }  playarr[x].getCommunicator().sendDeathServerMessage(toSend, (byte)r, (byte)g, (byte)25);
/*      */       }
/*      */     
/* 1085 */     } else if (Features.Feature.PVE_DEATHTABS.isEnabled()) {
/*      */       
/* 1087 */       String toSend = player.getName() + " slain by " + slayers;
/* 1088 */       for (Player p : getInstance().getPlayers()) {
/*      */         
/* 1090 */         if (!p.hasFlag(60)) {
/* 1091 */           p.getCommunicator().sendDeathServerMessage(toSend, (byte)25, (byte)-56, (byte)25);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void sendAddToAlliance(Creature player, Village village) {
/* 1098 */     if (village != null) {
/*      */       
/* 1100 */       Player[] playerArr = getPlayers();
/* 1101 */       for (Player lPlayer : playerArr) {
/*      */         
/* 1103 */         if (player != lPlayer)
/*      */         {
/* 1105 */           if (player.isVisibleTo((Creature)lPlayer))
/*      */           {
/* 1107 */             if (lPlayer.getCitizenVillage() != null && village.getAllianceNumber() > 0 && village
/* 1108 */               .getAllianceNumber() == lPlayer.getCitizenVillage().getAllianceNumber()) {
/*      */               
/* 1110 */               lPlayer.getCommunicator().sendAddAlly(player.getName(), player.getWurmId());
/* 1111 */               player.getCommunicator().sendAddAlly(lPlayer.getName(), lPlayer.getWurmId());
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendRemoveFromAlliance(Creature player, Village village) {
/* 1121 */     if (village != null) {
/*      */       
/* 1123 */       Player[] playerArr = getPlayers();
/* 1124 */       for (Player lPlayer : playerArr) {
/*      */         
/* 1126 */         if (player != lPlayer)
/*      */         {
/* 1128 */           if (lPlayer.getCitizenVillage() != null && village.getAllianceNumber() > 0 && village
/* 1129 */             .getAllianceNumber() == lPlayer.getCitizenVillage().getAllianceNumber()) {
/*      */             
/* 1131 */             lPlayer.getCommunicator().sendRemoveAlly(player.getName());
/* 1132 */             player.getCommunicator().sendRemoveAlly(lPlayer.getName());
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addToGroups(Player player) {
/* 1141 */     if (!player.isUndead()) {
/*      */ 
/*      */       
/*      */       try {
/* 1145 */         Groups.getGroup("wurm").addMember(player.getName(), (Creature)player);
/*      */       }
/* 1147 */       catch (NoSuchGroupException ex) {
/*      */         
/* 1149 */         logger.log(Level.WARNING, "Could not get group for Group 'wurm', Player: " + player + " due to " + ex
/* 1150 */             .getMessage(), (Throwable)ex);
/*      */       } 
/*      */ 
/*      */       
/* 1154 */       Village citvil = Villages.getVillageForCreature((Creature)player);
/* 1155 */       player.setCitizenVillage(citvil);
/* 1156 */       player.sendSkills();
/* 1157 */       if (citvil != null) {
/*      */         
/*      */         try {
/*      */           
/* 1161 */           citvil.setLogin();
/* 1162 */           Groups.getGroup(citvil.getName()).addMember(player.getName(), (Creature)player);
/* 1163 */           if (citvil.getAllianceNumber() > 0) {
/*      */             
/* 1165 */             PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(citvil.getAllianceNumber());
/* 1166 */             if (pvpAll != null && !pvpAll.getMotd().isEmpty())
/*      */             {
/* 1168 */               Message mess = pvpAll.getMotdMessage();
/* 1169 */               player.getCommunicator().sendMessage(mess);
/*      */             }
/*      */             else
/*      */             {
/* 1173 */               Message mess = new Message((Creature)player, (byte)15, "Alliance", "");
/* 1174 */               player.getCommunicator().sendMessage(mess);
/*      */             }
/*      */           
/*      */           } 
/* 1178 */         } catch (NoSuchGroupException ex) {
/*      */           
/* 1180 */           logger.log(Level.WARNING, "Could not get group for Village: " + citvil + ", Player: " + player + " due to " + ex
/* 1181 */               .getMessage(), (Throwable)ex);
/*      */         } 
/*      */       }
/* 1184 */       Player[] playerArr = getPlayers();
/*      */ 
/*      */ 
/*      */       
/* 1188 */       Village village = player.getCitizenVillage();
/* 1189 */       if (village != null)
/*      */       {
/* 1191 */         for (Player lPlayer : playerArr)
/*      */         {
/* 1193 */           if (player != lPlayer)
/*      */           {
/* 1195 */             if (player.isVisibleTo((Creature)lPlayer, true))
/*      */             {
/* 1197 */               if (lPlayer.getCitizenVillage() == village) {
/* 1198 */                 lPlayer.getCommunicator().sendAddVillager(player.getName(), player.getWurmId());
/*      */               }
/* 1200 */               if (lPlayer.getCitizenVillage() != null && village.getAllianceNumber() > 0 && village
/* 1201 */                 .getAllianceNumber() == lPlayer.getCitizenVillage().getAllianceNumber())
/*      */               {
/* 1203 */                 lPlayer.getCommunicator().sendAddAlly(player.getName(), player.getWurmId());
/* 1204 */                 player.getCommunicator().sendAddAlly(lPlayer.getName(), lPlayer.getWurmId());
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1229 */       player.sendSkills();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeFromGroups(Player player) {
/*      */     try {
/* 1236 */       Groups.getGroup("wurm").dropMember(player.getName());
/* 1237 */       if (player.getCitizenVillage() != null)
/*      */       {
/* 1239 */         Groups.getGroup(player.getCitizenVillage().getName()).dropMember(player.getName());
/*      */       }
/*      */     }
/* 1242 */     catch (NoSuchGroupException nsg) {
/*      */       
/* 1244 */       logger.log(Level.WARNING, "Could not get group for Village: " + player.getCitizenVillage() + ", Player: " + player + " due to " + nsg
/* 1245 */           .getMessage(), (Throwable)nsg);
/*      */     } 
/*      */     
/* 1248 */     if (player.mayHearDevTalk() || player.mayHearMgmtTalk()) {
/*      */       
/* 1250 */       removeFromTabs(player.getWurmId(), player.getName());
/* 1251 */       sendRemoveFromTabs(player.getWurmId(), player.getName());
/*      */     } 
/*      */     
/* 1254 */     Village village = player.getCitizenVillage();
/* 1255 */     Player[] playerArr = getPlayers();
/* 1256 */     for (Player lPlayer : playerArr) {
/*      */       
/* 1258 */       if (player != lPlayer) {
/*      */         
/* 1260 */         if (village != null) {
/*      */           
/* 1262 */           if (lPlayer.getCitizenVillage() == village) {
/* 1263 */             lPlayer.getCommunicator().sendRemoveVillager(player.getName());
/*      */           }
/* 1265 */           if (lPlayer.getCitizenVillage() != null && village.getAllianceNumber() > 0 && village
/* 1266 */             .getAllianceNumber() == lPlayer.getCitizenVillage().getAllianceNumber())
/*      */           {
/* 1268 */             lPlayer.getCommunicator().sendRemoveAlly(player.getName());
/*      */           }
/*      */         } 
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
/* 1285 */         if (player.seesPlayerAssistantWindow() && lPlayer.seesPlayerAssistantWindow())
/*      */         {
/* 1287 */           if (player.isPlayerAssistant()) {
/* 1288 */             lPlayer.getCommunicator().sendRemovePa(" CA " + player.getName());
/* 1289 */           } else if (shouldReceivePlayerList(lPlayer)) {
/* 1290 */             lPlayer.getCommunicator().sendRemovePa(player.getName());
/*      */           } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShouldSendWeather(boolean shouldSend) {
/* 1312 */     this.shouldSendWeather = shouldSend;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean shouldSendWeather() {
/* 1317 */     return this.shouldSendWeather;
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkSendWeather() {
/* 1322 */     if (shouldSendWeather()) {
/*      */       
/* 1324 */       sendWeather();
/* 1325 */       setShouldSendWeather(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendWeather() {
/* 1334 */     Player[] playerArr = getPlayers();
/* 1335 */     for (Player lPlayer : playerArr) {
/*      */       
/* 1337 */       if (lPlayer != null && lPlayer.getCommunicator() != null)
/*      */       {
/* 1339 */         lPlayer.getCommunicator().sendWeather();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Player logout(SocketConnection serverConnection) {
/* 1347 */     Player player = null;
/* 1348 */     String ip = "";
/* 1349 */     if (serverConnection != null) {
/*      */ 
/*      */       
/*      */       try {
/* 1353 */         ip = serverConnection.getIp();
/*      */       }
/* 1355 */       catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1361 */         serverConnection.disconnect();
/*      */       }
/* 1363 */       catch (NullPointerException nullPointerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1370 */       player = getPlayer(serverConnection);
/* 1371 */       logoutPlayer(player);
/*      */     }
/* 1373 */     catch (NoSuchPlayerException ex) {
/*      */ 
/*      */       
/*      */       try {
/* 1377 */         player = getPlayer(serverConnection);
/* 1378 */         if (player != null) {
/*      */           
/* 1380 */           if (ip.equals(""))
/*      */           {
/* 1382 */             ip = player.getSaveFile().getIpaddress();
/*      */           }
/* 1384 */           removeFromGroups(player);
/* 1385 */           players.remove(player.getName());
/* 1386 */           playersById.remove(Long.valueOf(player.getWurmId()));
/* 1387 */           logger.log(Level.INFO, "Logout - " + ex.getMessage() + " please verify that player " + player.getName() + " is logged out.", (Throwable)ex);
/*      */         }
/*      */         else {
/*      */           
/* 1391 */           logger.log(Level.INFO, "Logout - " + ex.getMessage(), (Throwable)ex);
/*      */         } 
/* 1393 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1400 */     if (Servers.localServer.PVPSERVER && !Servers.isThisATestServer())
/*      */     {
/* 1402 */       if (!ip.isEmpty()) {
/*      */ 
/*      */         
/* 1405 */         KingdomIp kip = KingdomIp.getKIP(ip, (byte)0);
/* 1406 */         if (kip != null)
/* 1407 */           kip.logoff(); 
/*      */       } 
/*      */     }
/* 1410 */     return player;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendPAWindow(Player player) {
/* 1415 */     String chan = getKingdomHelpChannelName(player.getKingdomId());
/* 1416 */     if (chan.length() == 0)
/*      */       return; 
/* 1418 */     Message mess = new Message((Creature)player, (byte)12, chan, "<System> This is the Community Assistance window. Just type your questions here. To stop receiving these messages, manage your profile.");
/*      */     
/* 1420 */     player.getCommunicator().sendMessage(mess);
/* 1421 */     joinPAChannel(player);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getKingdomHelpChannelName(byte kingdomId) {
/* 1426 */     String chan = "";
/* 1427 */     if (kingdomId == 4) {
/* 1428 */       chan = "CA HELP";
/* 1429 */     } else if (kingdomId == 1) {
/* 1430 */       chan = "JK HELP";
/* 1431 */     } else if (kingdomId == 2) {
/* 1432 */       chan = "MR HELP";
/* 1433 */     } else if (kingdomId == 3) {
/* 1434 */       chan = "HOTS HELP";
/* 1435 */     }  return chan;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendGVHelpWindow(Player player) {
/* 1440 */     Message mess = new Message((Creature)player, (byte)12, "GV HELP", "<System> This is the GV Help window. just reply to questions here. To stop receiving these messages, manage your profile.");
/*      */     
/* 1442 */     player.getCommunicator().sendMessage(mess);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void sendGmsToPlayer(Player player) {
/* 1448 */     Message mess = new Message((Creature)player, (byte)9, "MGMT", "");
/* 1449 */     player.getCommunicator().sendMessage(mess);
/*      */     
/* 1451 */     if (player.mayHearMgmtTalk() || player.mayHearDevTalk()) {
/* 1452 */       sendToTabs(player, (player.getPower() < 2), (player.getPower() >= 2));
/*      */     }
/* 1454 */     if (player.mayHearMgmtTalk())
/*      */     {
/* 1456 */       for (TabData tabData : tabListMGMT.values()) {
/*      */         
/* 1458 */         if (tabData.isVisible() || tabData.getPower() < 2) {
/* 1459 */           player.getCommunicator().sendAddMgmt(tabData.getName(), tabData.getWurmId());
/*      */         }
/*      */       } 
/*      */     }
/* 1463 */     if (player.mayMute()) {
/*      */       
/* 1465 */       Message mess2 = new Message((Creature)player, (byte)11, "GM", "");
/* 1466 */       player.getCommunicator().sendMessage(mess2);
/*      */       
/* 1468 */       if (player.mayHearDevTalk())
/*      */       {
/* 1470 */         for (TabData tabData : tabListGM.values()) {
/*      */           
/* 1472 */           if (tabData.isVisible() || tabData.getPower() <= player.getPower()) {
/* 1473 */             player.getCommunicator().sendAddGm(tabData.getName(), tabData.getWurmId());
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   void sendTicketsToPlayer(Player player) {
/* 1481 */     Ticket[] tickets = Tickets.getTickets(player);
/*      */ 
/*      */     
/* 1484 */     for (Ticket t : tickets)
/*      */     {
/* 1486 */       player.getCommunicator().sendTicket(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeGlobalEffect(long id) {
/* 1492 */     for (Player player : getPlayers())
/*      */     {
/* 1494 */       player.getCommunicator().sendRemoveEffect(id);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void sendAltarsToPlayer(Player player) {
/* 1500 */     for (EndGameItem eg : EndGameItems.altars.values()) {
/*      */       
/* 1502 */       if (eg.isHoly()) {
/*      */         
/* 1504 */         player.getCommunicator().sendAddEffect(eg.getWurmid(), (short)2, eg.getItem().getPosX(), eg
/* 1505 */             .getItem().getPosY(), eg.getItem().getPosZ(), (byte)0);
/* 1506 */         if (WurmCalendar.isChristmas()) {
/*      */           
/* 1508 */           if (Zones.santaMolRehan != null) {
/* 1509 */             player.getCommunicator().sendAddEffect(Zones.santaMolRehan.getWurmId(), (short)4, Zones.santaMolRehan
/* 1510 */                 .getPosX(), Zones.santaMolRehan.getPosY(), Zones.santaMolRehan.getPositionZ(), (byte)0);
/*      */           }
/* 1512 */           if (Zones.santa != null)
/* 1513 */             player.getCommunicator().sendAddEffect(Zones.santa.getWurmId(), (short)4, Zones.santa.getPosX(), Zones.santa
/* 1514 */                 .getPosY(), Zones.santa.getPositionZ(), (byte)0); 
/* 1515 */           if (Zones.santas != null && !Zones.santas.isEmpty())
/*      */           {
/* 1517 */             for (Creature santa : Zones.santas.values())
/*      */             {
/* 1519 */               player.getCommunicator().sendAddEffect(santa.getWurmId(), (short)4, santa.getPosX(), santa
/* 1520 */                   .getPosY(), santa.getPositionZ(), (byte)0);
/*      */             }
/*      */           }
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/* 1527 */       player.getCommunicator().sendAddEffect(eg.getWurmid(), (short)3, eg.getItem().getPosX(), eg
/* 1528 */           .getItem().getPosY(), eg.getItem().getPosZ(), (byte)0);
/* 1529 */       if (WurmCalendar.isChristmas() && Zones.evilsanta != null) {
/* 1530 */         player.getCommunicator().sendAddEffect(Zones.evilsanta.getWurmId(), (short)4, Zones.evilsanta
/* 1531 */             .getPosX(), Zones.evilsanta.getPosY(), Zones.evilsanta.getPositionZ(), (byte)0);
/*      */       }
/*      */     } 
/* 1534 */     if (EndGameItems.altars == null || EndGameItems.altars.isEmpty())
/*      */     {
/* 1536 */       if (WurmCalendar.isChristmas()) {
/*      */         
/* 1538 */         if (Zones.santa != null)
/* 1539 */           player.getCommunicator().sendAddEffect(Zones.santa.getWurmId(), (short)4, Zones.santa.getPosX(), Zones.santa
/* 1540 */               .getPosY(), Zones.santa.getPositionZ(), (byte)0); 
/* 1541 */         if (Zones.santaMolRehan != null) {
/* 1542 */           player.getCommunicator().sendAddEffect(Zones.santaMolRehan.getWurmId(), (short)4, Zones.santaMolRehan
/* 1543 */               .getPosX(), Zones.santaMolRehan.getPosY(), Zones.santaMolRehan.getPositionZ(), (byte)0);
/*      */         }
/* 1545 */         if (Zones.evilsanta != null)
/* 1546 */           player.getCommunicator().sendAddEffect(Zones.evilsanta.getWurmId(), (short)4, Zones.evilsanta
/* 1547 */               .getPosX(), Zones.evilsanta.getPosY(), Zones.evilsanta.getPositionZ(), (byte)0); 
/* 1548 */         if (Zones.santas != null && !Zones.santas.isEmpty())
/*      */         {
/* 1550 */           for (Creature santa : Zones.santas.values())
/*      */           {
/* 1552 */             player.getCommunicator().sendAddEffect(santa.getWurmId(), (short)4, santa.getPosX(), santa
/* 1553 */                 .getPosY(), santa.getPositionZ(), (byte)0);
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/* 1558 */     if (Servers.localServer.isChallengeServer())
/*      */     {
/* 1560 */       if (challengeStep > 0) {
/*      */         
/* 1562 */         player.getCommunicator().sendAddEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), (short)20, 0.0F, 0.0F, 0.0F, (byte)0);
/*      */ 
/*      */         
/* 1565 */         if (challengeStep > 1) {
/* 1566 */           player.getCommunicator().sendAddEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), (short)21, 0.0F, 0.0F, 0.0F, (byte)0);
/*      */         }
/*      */         
/* 1569 */         if (challengeStep > 2) {
/* 1570 */           player.getCommunicator().sendAddEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), (short)22, 0.0F, 0.0F, 0.0F, (byte)0);
/*      */         }
/*      */         
/* 1573 */         if (challengeStep > 3) {
/* 1574 */           player.getCommunicator().sendAddEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), (short)23, 0.0F, 0.0F, 0.0F, (byte)0);
/*      */         }
/*      */       } 
/*      */     }
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
/* 1592 */     Effect[] effs = EffectFactory.getInstance().getAllEffects();
/* 1593 */     for (Effect effect : effs) {
/*      */       
/* 1595 */       if (effect.isGlobal()) {
/*      */         
/* 1597 */         if (logger.isLoggable(Level.FINER))
/*      */         {
/* 1599 */           logger.finer(player.getName() + " Sending effect type " + effect.getType() + " at position (x,y,z) " + effect
/* 1600 */               .getPosX() + ',' + effect.getPosY() + ',' + effect.getPosZ());
/*      */         }
/* 1602 */         player.getCommunicator().sendAddEffect(effect.getOwner(), effect.getType(), effect
/* 1603 */             .getPosX(), effect.getPosY(), effect.getPosZ(), (byte)0);
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
/*      */   public void logoutPlayer(Player player) {
/* 1617 */     if (player.hasLink()) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 1622 */         player.getCommunicator().sendShutDown("You were logged out by the server.", false);
/*      */       }
/* 1624 */       catch (Exception e) {
/*      */ 
/*      */         
/* 1627 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 1629 */           logger.log(Level.FINEST, "Could not send shutdown to " + player + " due to " + e.getMessage(), e);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1636 */         player.getCommunicator().disconnect();
/*      */       }
/* 1638 */       catch (Exception e) {
/*      */ 
/*      */         
/* 1641 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/* 1643 */           logger.log(Level.FINEST, "Could not send disconnect to " + player + " due to " + e.getMessage(), e);
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 1648 */       player.getCommunicator().disconnect();
/* 1649 */     }  sendConnectInfo(player, " left the world.", System.currentTimeMillis(), PlayerOnlineStatus.OFFLINE);
/* 1650 */     removeFromGroups(player);
/* 1651 */     setCreatureDead((Creature)player);
/* 1652 */     Creatures.getInstance().setCreatureDead((Creature)player);
/* 1653 */     player.logout();
/* 1654 */     players.remove(player.getName());
/* 1655 */     playersById.remove(Long.valueOf(player.getWurmId()));
/*      */ 
/*      */     
/* 1658 */     (Server.getInstance()).steamHandler.EndAuthSession(player.getSteamId().toString());
/*      */     
/* 1660 */     if (Servers.localServer.PVPSERVER && !Servers.isThisATestServer())
/*      */     {
/* 1662 */       if (player.getPower() < 1) {
/*      */ 
/*      */         
/* 1665 */         KingdomIp kip = KingdomIp.getKIP(player.getSaveFile().getIpaddress(), (byte)0);
/* 1666 */         if (kip != null) {
/* 1667 */           kip.logoff();
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   final void removePlayer(Player player) {
/* 1674 */     players.remove(player.getName());
/* 1675 */     playersById.remove(Long.valueOf(player.getWurmId()));
/*      */   }
/*      */ 
/*      */   
/*      */   final void addPlayer(Player player) {
/* 1680 */     players.put(player.getName(), player);
/* 1681 */     playersById.put(Long.valueOf(player.getWurmId()), player);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Player[] getPlayers() {
/* 1691 */     return (Player[])players.values().toArray((Object[])new Player[players.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Player> getPlayerMap() {
/* 1701 */     return players;
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
/*      */   String[] getPlayerNames() {
/* 1731 */     String[] lReturn = (String[])players.keySet().toArray((Object[])new String[players.size()]);
/* 1732 */     return lReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendEffect(short effType, float posx, float posy, float posz, boolean surfaced, float maxDistMeters) {
/* 1738 */     Player[] playarr = getPlayers();
/* 1739 */     for (Player lPlayer : playarr) {
/*      */ 
/*      */       
/*      */       try {
/* 1743 */         if (lPlayer.getVisionArea() != null && lPlayer.isOnSurface() == surfaced)
/*      */         {
/* 1745 */           if (lPlayer.isWithinDistanceTo(posx, posy, posz, maxDistMeters))
/*      */           {
/* 1747 */             lPlayer.getCommunicator().sendAddEffect(WurmId.getNextTempItemId(), effType, posx, posy, posz, (byte)(surfaced ? 0 : -1));
/*      */           
/*      */           }
/*      */         }
/*      */       }
/* 1752 */       catch (NullPointerException npe) {
/*      */         
/* 1754 */         logger.log(Level.WARNING, "Null visionArea or communicator for player " + lPlayer.getName() + ", disconnecting.");
/*      */         
/* 1756 */         lPlayer.setLink(false);
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
/*      */   public void sendChangedTile(@Nonnull TilePos tilePos, boolean surfaced, boolean destroyTrap) {
/* 1768 */     sendChangedTile(tilePos.x, tilePos.y, surfaced, destroyTrap);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendChangedTile(int tilex, int tiley, boolean surfaced, boolean destroyTrap) {
/* 1773 */     Player[] playarr = getPlayers();
/* 1774 */     if (destroyTrap) {
/*      */       
/* 1776 */       Trap t = Trap.getTrap(tilex, tiley, surfaced ? 0 : -1);
/* 1777 */       if (t != null) {
/*      */         
/* 1779 */         byte tiletype = Tiles.decodeType(Zones.getMesh(surfaced).getTile(tilex, tiley));
/* 1780 */         if (!t.mayTrapRemainOnTile(tiletype)) {
/*      */           
/*      */           try {
/*      */             
/* 1784 */             t.delete();
/*      */           }
/* 1786 */           catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1795 */     boolean nearRoad = isNearRoad(surfaced, tilex, tiley);
/* 1796 */     if (surfaced) {
/*      */       
/* 1798 */       for (Player lPlayer : playarr) {
/*      */ 
/*      */         
/*      */         try {
/* 1802 */           if (lPlayer.getVisionArea() != null && lPlayer.getVisionArea().contains(tilex, tiley))
/*      */           {
/* 1804 */             if (lPlayer.getCommunicator() != null) {
/*      */               
/*      */               try {
/* 1807 */                 lPlayer.getMovementScheme().touchFreeMoveCounter();
/* 1808 */                 if (nearRoad) {
/* 1809 */                   lPlayer.getCommunicator().sendTileStrip((short)(tilex - 1), (short)(tiley - 1), 3, 3);
/*      */                 } else {
/* 1811 */                   lPlayer.getCommunicator().sendTileStrip((short)tilex, (short)tiley, 1, 1);
/*      */                 } 
/* 1813 */               } catch (IOException iOException) {}
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         }
/* 1819 */         catch (NullPointerException npe) {
/*      */           
/* 1821 */           if (lPlayer == null)
/*      */           {
/* 1823 */             logger.log(Level.INFO, "Null player detected. Ignoring for now.");
/*      */           }
/*      */           else
/*      */           {
/* 1827 */             logger.log(Level.WARNING, "Null visionArea or communicator for player " + lPlayer.getName() + ", disconnecting.");
/*      */             
/* 1829 */             lPlayer.setLink(false);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1836 */       for (Player lPlayer : playarr) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1845 */           if (lPlayer.getVisionArea() != null && lPlayer.getVisionArea().containsCave(tilex, tiley))
/*      */           {
/*      */ 
/*      */             
/* 1849 */             lPlayer.getMovementScheme().touchFreeMoveCounter();
/* 1850 */             lPlayer.getCommunicator().sendCaveStrip((short)(tilex - 1), (short)(tiley - 1), 3, 3);
/*      */           }
/*      */         
/* 1853 */         } catch (NullPointerException npe) {
/*      */           
/* 1855 */           logger.log(Level.WARNING, "Null visionArea or communicator for player " + lPlayer.getName() + ", disconnecting.");
/*      */           
/* 1857 */           lPlayer.setLink(false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendChangedTiles(int startX, int startY, int sizeX, int sizeY, boolean surfaced, boolean destroyTrap) {
/* 1866 */     if (destroyTrap)
/*      */     {
/* 1868 */       for (int x = 0; x < sizeX; x++) {
/*      */         
/* 1870 */         for (int y = 0; y < sizeY; y++) {
/*      */           
/* 1872 */           int tempTileX = startX + x;
/* 1873 */           int tempTileY = startY + y;
/* 1874 */           if (GeneralUtilities.isValidTileLocation(tempTileX, tempTileY)) {
/*      */             
/* 1876 */             Trap t = Trap.getTrap(tempTileX, tempTileY, surfaced ? 0 : -1);
/* 1877 */             if (t != null) {
/*      */               
/* 1879 */               byte tiletype = Tiles.decodeType(Zones.getMesh(surfaced).getTile(tempTileX, tempTileY));
/* 1880 */               if (!t.mayTrapRemainOnTile(tiletype)) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1884 */                   t.delete();
/*      */                 }
/* 1886 */                 catch (IOException iOException) {}
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1896 */     boolean nearRoad = (sizeX == 1 && sizeY == 1 && isNearRoad(surfaced, startX, startY));
/* 1897 */     Player[] playarr = getPlayers();
/* 1898 */     for (Player lPlayer : playarr) {
/*      */ 
/*      */       
/*      */       try {
/* 1902 */         if (surfaced) {
/*      */           
/* 1904 */           if (nearRoad)
/*      */           {
/* 1906 */             if (lPlayer.getVisionArea() != null && (lPlayer
/* 1907 */               .getVisionArea().contains(startX, startY) || lPlayer
/* 1908 */               .getVisionArea().contains(startX, startY + sizeY) || lPlayer
/* 1909 */               .getVisionArea().contains(startX + sizeX, startY + sizeY) || lPlayer
/* 1910 */               .getVisionArea().contains(startX + sizeX, startY))) {
/*      */               
/*      */               try {
/*      */                 
/* 1914 */                 lPlayer.getCommunicator().sendTileStrip((short)(startX - 1), (short)(startY - 1), 3, 3);
/*      */               }
/* 1916 */               catch (IOException iOException) {}
/*      */ 
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1922 */           else if (lPlayer.getVisionArea() != null && (lPlayer
/* 1923 */             .getVisionArea().contains(startX, startY) || lPlayer
/* 1924 */             .getVisionArea().contains(startX, startY + sizeY) || lPlayer
/* 1925 */             .getVisionArea().contains(startX + sizeX, startY + sizeY) || lPlayer
/* 1926 */             .getVisionArea().contains(startX + sizeX, startY)))
/*      */           {
/*      */             
/*      */             try {
/* 1930 */               lPlayer.getCommunicator().sendTileStrip((short)startX, (short)startY, sizeX, sizeY);
/*      */             }
/* 1932 */             catch (IOException iOException) {}
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */         
/*      */         }
/* 1940 */         else if (lPlayer.isNearCave()) {
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
/* 1957 */           for (int xx = startX; xx < startX + sizeX; xx++)
/*      */           {
/* 1959 */             for (int yy = startY; yy < startY + sizeY; yy++)
/*      */             {
/* 1961 */               if (lPlayer.getVisionArea() != null && lPlayer.getVisionArea().containsCave(xx, yy))
/*      */               {
/* 1963 */                 lPlayer.getCommunicator().sendCaveStrip((short)xx, (short)yy, 1, 1);
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         } 
/* 1970 */       } catch (NullPointerException npe) {
/*      */         
/* 1972 */         logger.log(Level.WARNING, "Null visionArea or communicator for player " + lPlayer.getName() + ", disconnecting.");
/*      */         
/* 1974 */         lPlayer.setLink(false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isNearRoad(boolean surfaced, int tilex, int tiley) {
/*      */     try {
/* 1985 */       if (surfaced)
/*      */       {
/* 1987 */         for (int x = -1; x <= 1; x++) {
/*      */           
/* 1989 */           for (int y = -1; y <= 1; y++)
/*      */           {
/* 1991 */             if (GeneralUtilities.isValidTileLocation(tilex + x, tiley + y) && 
/* 1992 */               Tiles.isRoadType(Server.surfaceMesh.getTile(tilex + x, tiley + y)))
/*      */             {
/* 1994 */               return true;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       }
/* 2000 */     } catch (Exception ex) {
/*      */       
/* 2002 */       logger.log(Level.WARNING, "****** Oops invalid x,y " + tilex + "," + tiley + ".");
/*      */     } 
/*      */     
/* 2005 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   void savePlayersAtShutdown() {
/* 2010 */     logger.info("Saving Players");
/* 2011 */     Player[] playarr = getPlayers();
/* 2012 */     for (Player lPlayer : playarr) {
/*      */       
/* 2014 */       if (lPlayer.getDraggedItem() != null) {
/* 2015 */         Items.stopDragging(lPlayer.getDraggedItem());
/*      */       }
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
/*      */       try {
/* 2031 */         lPlayer.sleep();
/*      */       }
/* 2033 */       catch (Exception ex) {
/*      */         
/* 2035 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*      */     } 
/*      */     
/* 2039 */     logger.info("Finished saving Players");
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNameFor(long playerId) throws NoSuchPlayerException, IOException {
/* 2044 */     Long pid = Long.valueOf(playerId);
/* 2045 */     Player p = getPlayerById(pid);
/* 2046 */     if (p != null) {
/* 2047 */       return p.getName();
/*      */     }
/*      */     
/* 2050 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(playerId);
/* 2051 */     if (info != null)
/* 2052 */       return info.getName(); 
/* 2053 */     PlayerState pState = PlayerInfoFactory.getPlayerState(playerId);
/* 2054 */     if (pState != null)
/* 2055 */       return pState.getPlayerName(); 
/* 2056 */     return DbSearcher.getNameForPlayer(playerId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getWurmIdFor(String name) throws NoSuchPlayerException, IOException {
/* 2062 */     PlayerInfo info = PlayerInfoFactory.createPlayerInfo(name);
/* 2063 */     if (info.loaded)
/* 2064 */       return info.wurmId; 
/* 2065 */     return DbSearcher.getWurmIdForPlayer(name);
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
/*      */   private void loadBannedIps() {
/* 2100 */     Connection dbcon = null;
/* 2101 */     PreparedStatement ps = null;
/* 2102 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2105 */       dbcon = DbConnector.getPlayerDbCon();
/* 2106 */       ps = dbcon.prepareStatement(IPBan.getSelectSql());
/* 2107 */       rs = ps.executeQuery();
/* 2108 */       while (rs.next()) {
/*      */         
/* 2110 */         String ip = rs.getString("IPADDRESS");
/* 2111 */         String reason = rs.getString("BANREASON");
/* 2112 */         long expiry = rs.getLong("BANEXPIRY");
/* 2113 */         IPBan iPBan = new IPBan(ip, reason, expiry);
/* 2114 */         if (!iPBan.isExpired()) { bans.add(iPBan); continue; }
/* 2115 */          removeBan((Ban)iPBan);
/*      */       } 
/* 2117 */       logger.info("Loaded " + bans.size() + " banned IPs");
/*      */     }
/* 2119 */     catch (SQLException sqex) {
/*      */       
/* 2121 */       logger.log(Level.WARNING, "Failed to load banned ips.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2125 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2126 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadBannedSteamIds() {
/* 2133 */     Connection dbcon = null;
/* 2134 */     PreparedStatement ps = null;
/* 2135 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2138 */       dbcon = DbConnector.getPlayerDbCon();
/* 2139 */       ps = dbcon.prepareStatement(SteamIdBan.getSelectSql());
/* 2140 */       rs = ps.executeQuery();
/* 2141 */       while (rs.next()) {
/*      */         
/* 2143 */         String identifier = rs.getString("STEAM_ID");
/* 2144 */         String reason = rs.getString("BANREASON");
/* 2145 */         long expiry = rs.getLong("BANEXPIRY");
/* 2146 */         SteamIdBan steamIdBan = new SteamIdBan(SteamId.fromSteamID64(Long.valueOf(identifier).longValue()), reason, expiry);
/* 2147 */         if (!steamIdBan.isExpired()) { bans.add(steamIdBan); continue; }
/* 2148 */          removeBan((Ban)steamIdBan);
/*      */       } 
/* 2150 */       logger.info("Loaded " + bans.size() + " more bans from steamids");
/*      */     }
/* 2152 */     catch (SQLException sqex) {
/*      */       
/* 2154 */       logger.log(Level.WARNING, "Failed to load banned steamids.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2158 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2159 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumberOfPlayers() {
/* 2165 */     return players.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBannedIp(String ip, String reason, long expiry) {
/* 2170 */     IPBan iPBan = new IPBan(ip, reason, expiry);
/* 2171 */     addBan((Ban)iPBan);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBan(Ban ban) {
/* 2176 */     if (ban == null || ban.getIdentifier() == null || ban.getIdentifier().isEmpty()) {
/*      */       
/* 2178 */       logger.warning("Cannot add a null ban");
/*      */       return;
/*      */     } 
/* 2181 */     Ban bip = getBannedIp(ban.getIdentifier());
/* 2182 */     if (bip == null) {
/*      */       
/* 2184 */       bans.add(ban);
/*      */ 
/*      */       
/* 2187 */       Connection dbcon = null;
/* 2188 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2191 */         dbcon = DbConnector.getPlayerDbCon();
/* 2192 */         ps = dbcon.prepareStatement(ban.getInsertSql());
/* 2193 */         ps.setString(1, ban.getIdentifier());
/* 2194 */         ps.setString(2, ban.getReason());
/* 2195 */         ps.setLong(3, ban.getExpiry());
/* 2196 */         ps.executeUpdate();
/*      */       }
/* 2198 */       catch (SQLException sqex) {
/*      */         
/* 2200 */         logger.log(Level.WARNING, "Failed to add ban " + ban.getIdentifier(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 2204 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2205 */         DbConnector.returnConnection(dbcon);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2211 */       bip.setReason(ban.getReason());
/* 2212 */       bip.setExpiry(ban.getExpiry());
/*      */ 
/*      */       
/* 2215 */       Connection dbcon = null;
/* 2216 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2219 */         dbcon = DbConnector.getPlayerDbCon();
/* 2220 */         ps = dbcon.prepareStatement(bip.getUpdateSql());
/* 2221 */         ps.setString(1, bip.getReason());
/* 2222 */         ps.setLong(2, bip.getExpiry());
/* 2223 */         ps.setString(3, bip.getIdentifier());
/* 2224 */         ps.executeUpdate();
/*      */       }
/* 2226 */       catch (SQLException sqex) {
/*      */         
/* 2228 */         logger.log(Level.WARNING, "Failed to update ban " + bip.getIdentifier(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 2232 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2233 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeBan(String identifier) {
/* 2241 */     Ban existing = null;
/* 2242 */     for (Ban lBip : bans) {
/*      */       
/* 2244 */       if (lBip.getIdentifier().equals(identifier)) {
/* 2245 */         existing = lBip; continue;
/* 2246 */       }  if (identifier.contains("*") && lBip.getIdentifier().startsWith(identifier))
/* 2247 */         existing = lBip; 
/*      */     } 
/* 2249 */     if (existing == null) existing = Ban.fromString(identifier); 
/* 2250 */     return removeBan(existing);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeBan(Ban ban) {
/* 2255 */     bans.remove(ban);
/*      */     
/* 2257 */     Connection dbcon = null;
/* 2258 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2261 */       dbcon = DbConnector.getPlayerDbCon();
/* 2262 */       ps = dbcon.prepareStatement(ban.getDeleteSql());
/* 2263 */       ps.setString(1, ban.getIdentifier());
/* 2264 */       ps.executeUpdate();
/* 2265 */       return true;
/*      */     }
/* 2267 */     catch (SQLException sqex) {
/*      */       
/* 2269 */       logger.log(Level.WARNING, "Failed to remove ban " + ban.getIdentifier(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2273 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2274 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 2276 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void tickSecond() {
/* 2281 */     for (Player p : players.values()) {
/*      */       
/* 2283 */       if (p.getSaveFile() != null && (p.getSaveFile()).sleep > 0 && !(p.getSaveFile()).frozenSleep) {
/*      */         
/* 2285 */         float chance = p.getStatus().getFats() / 3.0F;
/* 2286 */         if (Server.rand.nextFloat() < chance) {
/*      */           continue;
/*      */         }
/* 2289 */         (p.getSaveFile()).sleep--;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban[] getPlayersBanned() {
/* 2296 */     Set<Ban> banned = new HashSet<>();
/*      */ 
/*      */     
/* 2299 */     Connection dbcon = null;
/* 2300 */     PreparedStatement ps = null;
/* 2301 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2304 */       dbcon = DbConnector.getPlayerDbCon();
/* 2305 */       ps = dbcon.prepareStatement("SELECT NAME,BANREASON,BANEXPIRY FROM PLAYERS WHERE BANNED=1");
/* 2306 */       rs = ps.executeQuery();
/* 2307 */       while (rs.next()) {
/*      */         
/* 2309 */         String ip = rs.getString("NAME");
/* 2310 */         String reason = rs.getString("BANREASON");
/* 2311 */         long expiry = rs.getLong("BANEXPIRY");
/* 2312 */         if (expiry > System.currentTimeMillis()) {
/* 2313 */           banned.add(new IPBan(ip, reason, expiry));
/*      */         }
/*      */       } 
/* 2316 */     } catch (SQLException sqex) {
/*      */       
/* 2318 */       logger.log(Level.WARNING, "Failed to get players banned.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2322 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2323 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2326 */     return banned.<Ban>toArray(new Ban[banned.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGmMessage(Creature sender, String playerName, String message, boolean emote, int red, int green, int blue) {
/* 2332 */     Message mess = null;
/* 2333 */     if (emote) {
/* 2334 */       mess = new Message(sender, (byte)6, "GM", message);
/*      */     } else {
/*      */       
/* 2337 */       mess = new Message(sender, (byte)11, "GM", "<" + playerName + "> " + message, red, green, blue);
/*      */     } 
/* 2339 */     addGmMessage(playerName, message);
/*      */     
/* 2341 */     Player[] playerArr = getInstance().getPlayers();
/* 2342 */     for (Player lPlayer : playerArr) {
/*      */       
/* 2344 */       if (lPlayer.mayHearDevTalk()) {
/*      */ 
/*      */         
/* 2347 */         if (sender == null)
/* 2348 */           mess.setSender((Creature)lPlayer); 
/* 2349 */         lPlayer.getCommunicator().sendMessage(mess);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendGmMessage(Creature sender, String playerName, String message, boolean emote) {
/* 2356 */     sendGmMessage(sender, playerName, message, emote, -1, -1, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendGlobalKingdomMessage(Creature sender, long senderId, String playerName, String message, boolean emote, byte kingdom, int r, int g, int b) {
/* 2363 */     Message mess = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2371 */     mess = new Message(sender, (byte)16, "GL-" + Kingdoms.getChatNameFor(kingdom), "<" + playerName + "> " + message);
/*      */     
/* 2373 */     mess.setSenderKingdom(kingdom);
/* 2374 */     mess.setSenderId(senderId);
/* 2375 */     mess.setColorR(r);
/* 2376 */     mess.setColorG(g);
/* 2377 */     mess.setColorB(b);
/*      */     
/* 2379 */     Server.getInstance().addMessage(mess);
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
/*      */   public void sendGlobalTradeMessage(Creature sender, long senderId, String playerName, String message, byte kingdom, int r, int g, int b) {
/* 2402 */     Message mess = null;
/* 2403 */     mess = new Message(sender, (byte)18, "Trade", "<" + playerName + "> " + message);
/*      */     
/* 2405 */     mess.setSenderKingdom(kingdom);
/* 2406 */     mess.setSenderId(senderId);
/* 2407 */     mess.setColorR(r);
/* 2408 */     mess.setColorG(g);
/* 2409 */     mess.setColorB(b);
/* 2410 */     Server.getInstance().addMessage(mess);
/*      */   }
/*      */ 
/*      */   
/*      */   public void partPAChannel(Player player) {
/* 2415 */     if (!player.seesPlayerAssistantWindow()) {
/*      */       
/* 2417 */       Player[] playerArr = getInstance().getPlayers();
/* 2418 */       for (Player lPlayer : playerArr) {
/*      */         
/* 2420 */         if (lPlayer.getSaveFile() != null && lPlayer.seesPlayerAssistantWindow())
/*      */         {
/* 2422 */           lPlayer.getCommunicator().sendRemovePa(player.getName());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void joinPAChannel(Player player) {
/* 2430 */     Player[] playerArr = getInstance().getPlayers();
/* 2431 */     for (Player lPlayer : playerArr) {
/*      */       
/* 2433 */       if (lPlayer.getSaveFile() != null && lPlayer.seesPlayerAssistantWindow() && player.isVisibleTo((Creature)lPlayer))
/*      */       {
/* 2435 */         if (player.isPlayerAssistant()) {
/* 2436 */           lPlayer.getCommunicator().sendAddPa(" CA " + player.getName(), player.getWurmId());
/* 2437 */         } else if (shouldReceivePlayerList(lPlayer) && player.getPower() < 2) {
/* 2438 */           lPlayer.getCommunicator().sendAddPa(player.getName(), player.getWurmId());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void partChannels(Player player) {
/* 2445 */     boolean mayDev = player.mayHearDevTalk();
/* 2446 */     boolean mayMgmt = player.mayHearMgmtTalk();
/* 2447 */     boolean mayHelp = player.seesPlayerAssistantWindow();
/*      */ 
/*      */     
/* 2450 */     if (!mayDev && !mayMgmt && !mayHelp) {
/*      */       return;
/*      */     }
/*      */     
/* 2454 */     String playerName = player.getName();
/*      */     
/* 2456 */     if (mayDev || mayMgmt) {
/*      */       
/* 2458 */       removeFromTabs(player.getWurmId(), playerName);
/* 2459 */       sendRemoveFromTabs(player.getWurmId(), playerName);
/*      */     } 
/*      */     
/* 2462 */     for (Player otherPlayer : getInstance().getPlayers()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2467 */       if (!player.isVisibleTo((Creature)otherPlayer))
/*      */       {
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
/* 2480 */         if (mayHelp && otherPlayer.seesPlayerAssistantWindow())
/*      */         {
/*      */           
/* 2483 */           if (player.isPlayerAssistant()) {
/* 2484 */             otherPlayer.getCommunicator().sendRemovePa(" CA " + playerName);
/*      */           }
/* 2486 */           else if (shouldReceivePlayerList(otherPlayer)) {
/* 2487 */             otherPlayer.getCommunicator().sendRemovePa(playerName);
/*      */           }  } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void joinChannels(Player player) {
/* 2494 */     boolean mayDev = player.mayHearDevTalk();
/* 2495 */     boolean mayMgmt = player.mayHearMgmtTalk();
/* 2496 */     boolean mayHelp = player.seesPlayerAssistantWindow();
/*      */ 
/*      */     
/* 2499 */     if (!mayDev && !mayMgmt && !mayHelp) {
/*      */       return;
/*      */     }
/*      */     
/* 2503 */     long playerId = player.getWurmId();
/* 2504 */     String playerName = player.getName();
/*      */     
/* 2506 */     if (mayDev || mayMgmt) {
/* 2507 */       sendToTabs(player, (player.getPower() < 2), (player.getPower() >= 2));
/*      */     }
/* 2509 */     for (Player otherPlayer : getInstance().getPlayers()) {
/*      */ 
/*      */       
/* 2512 */       if (player.isVisibleTo((Creature)otherPlayer) && player != otherPlayer)
/*      */       {
/*      */ 
/*      */         
/* 2516 */         if (mayHelp && otherPlayer.seesPlayerAssistantWindow())
/*      */         {
/*      */           
/* 2519 */           if (player.isPlayerAssistant()) {
/* 2520 */             otherPlayer.getCommunicator().sendAddPa(" CA " + playerName, playerId);
/*      */           }
/* 2522 */           else if (shouldReceivePlayerList(otherPlayer) && player.getPower() < 2) {
/* 2523 */             otherPlayer.getCommunicator().sendAddPa(playerName, playerId);
/*      */           }  } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendPaMessage(Message mes) {
/* 2530 */     caHelpLogger.info(mes.getMessage());
/* 2531 */     Player[] playerArr = getInstance().getPlayers();
/* 2532 */     for (Player lPlayer : playerArr) {
/*      */       
/* 2534 */       if (lPlayer.seesPlayerAssistantWindow())
/*      */       {
/* 2536 */         lPlayer.getCommunicator().sendMessage(mes);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendGVMessage(Message mes) {
/* 2543 */     caHelpLogger.info(mes.getMessage());
/* 2544 */     Player[] playerArr = getInstance().getPlayers();
/* 2545 */     for (Player lPlayer : playerArr) {
/*      */       
/* 2547 */       if (lPlayer.seesGVHelpWindow())
/*      */       {
/* 2549 */         lPlayer.getCommunicator().sendMessage(mes);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendCaMessage(byte kingdom, Message mes) {
/* 2556 */     caHelpLogger.info(mes.getMessage());
/* 2557 */     Player[] playerArr = getInstance().getPlayers();
/* 2558 */     for (Player lPlayer : playerArr) {
/*      */       
/* 2560 */       if (lPlayer.seesPlayerAssistantWindow() && (lPlayer.getKingdomId() == kingdom || lPlayer.getPower() >= 2))
/*      */       {
/* 2562 */         lPlayer.getCommunicator().sendMessage(mes);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getMuters() {
/* 2569 */     Set<String> muted = new HashSet<>();
/*      */ 
/*      */     
/* 2572 */     Connection dbcon = null;
/* 2573 */     PreparedStatement ps = null;
/* 2574 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2577 */       dbcon = DbConnector.getPlayerDbCon();
/* 2578 */       ps = dbcon.prepareStatement("SELECT NAME FROM PLAYERS WHERE MAYMUTE=1");
/* 2579 */       rs = ps.executeQuery();
/* 2580 */       while (rs.next())
/*      */       {
/* 2582 */         String name = rs.getString("NAME");
/* 2583 */         muted.add(name);
/*      */       }
/*      */     
/* 2586 */     } catch (SQLException sqex) {
/*      */       
/* 2588 */       logger.log(Level.WARNING, "Failed to get muters.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2592 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2593 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2596 */     return muted.<String>toArray(new String[muted.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getDevTalkers() {
/* 2601 */     Set<String> devTalkers = new HashSet<>();
/*      */ 
/*      */     
/* 2604 */     Connection dbcon = null;
/* 2605 */     PreparedStatement ps = null;
/* 2606 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2609 */       dbcon = DbConnector.getPlayerDbCon();
/* 2610 */       ps = dbcon.prepareStatement("SELECT NAME FROM PLAYERS WHERE DEVTALK=1");
/* 2611 */       rs = ps.executeQuery();
/* 2612 */       while (rs.next())
/*      */       {
/* 2614 */         String name = rs.getString("NAME");
/* 2615 */         devTalkers.add(name);
/*      */       }
/*      */     
/* 2618 */     } catch (SQLException sqex) {
/*      */       
/* 2620 */       logger.log(Level.WARNING, "Failed to get dev talkers.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2624 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2625 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2628 */     return devTalkers.<String>toArray(new String[devTalkers.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getCAs() {
/* 2633 */     Set<String> pas = new HashSet<>();
/*      */ 
/*      */     
/* 2636 */     Connection dbcon = null;
/* 2637 */     PreparedStatement ps = null;
/* 2638 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2641 */       dbcon = DbConnector.getPlayerDbCon();
/* 2642 */       ps = dbcon.prepareStatement("SELECT NAME FROM PLAYERS WHERE PA=1");
/* 2643 */       rs = ps.executeQuery();
/* 2644 */       while (rs.next())
/*      */       {
/* 2646 */         String name = rs.getString("NAME");
/* 2647 */         pas.add(name);
/*      */       }
/*      */     
/* 2650 */     } catch (SQLException sqex) {
/*      */       
/* 2652 */       logger.log(Level.WARNING, "Failed to get pas.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2656 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2657 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2660 */     return pas.<String>toArray(new String[pas.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getHeros(byte checkPower) {
/* 2665 */     Set<String> heros = new HashSet<>();
/*      */ 
/*      */     
/* 2668 */     Connection dbcon = null;
/* 2669 */     PreparedStatement ps = null;
/* 2670 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2673 */       dbcon = DbConnector.getPlayerDbCon();
/* 2674 */       ps = dbcon.prepareStatement("SELECT NAME FROM PLAYERS WHERE POWER=? AND CURRENTSERVER=?");
/* 2675 */       ps.setByte(1, checkPower);
/* 2676 */       ps.setInt(2, Servers.localServer.getId());
/* 2677 */       rs = ps.executeQuery();
/* 2678 */       while (rs.next())
/*      */       {
/* 2680 */         String name = rs.getString("NAME");
/* 2681 */         heros.add(name);
/*      */       }
/*      */     
/* 2684 */     } catch (SQLException sqex) {
/*      */       
/* 2686 */       logger.log(Level.WARNING, "Failed to get heros.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2690 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2691 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2694 */     return heros.<String>toArray(new String[heros.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban[] getPlayersMuted() {
/* 2699 */     Set<Ban> muted = new HashSet<>();
/*      */ 
/*      */     
/* 2702 */     Connection dbcon = null;
/* 2703 */     PreparedStatement ps = null;
/* 2704 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2707 */       dbcon = DbConnector.getPlayerDbCon();
/* 2708 */       ps = dbcon.prepareStatement("SELECT NAME,MUTEREASON,MUTEEXPIRY FROM PLAYERS WHERE MUTED=1");
/* 2709 */       rs = ps.executeQuery();
/* 2710 */       while (rs.next())
/*      */       {
/* 2712 */         String ip = rs.getString("NAME");
/* 2713 */         String reason = rs.getString("MUTEREASON");
/* 2714 */         long expiry = rs.getLong("MUTEEXPIRY");
/* 2715 */         muted.add(new IPBan(ip, reason, expiry));
/*      */       }
/*      */     
/* 2718 */     } catch (SQLException sqex) {
/*      */       
/* 2720 */       logger.log(Level.WARNING, "Failed to get players muted.", sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2724 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2725 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2728 */     return muted.<Ban>toArray(new Ban[muted.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban getAnyBan(String ip, Player player, String steamId) {
/* 2733 */     Ban ban = player.getBan();
/* 2734 */     if (ban == null)
/* 2735 */       ban = getBannedIp(ip); 
/* 2736 */     if (ban == null)
/* 2737 */       ban = getBannedSteamId(steamId); 
/* 2738 */     return ban;
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban getBannedSteamId(String steamId) {
/* 2743 */     if (steamId.isEmpty()) return null; 
/* 2744 */     Ban[] banArr = bans.<Ban>toArray(new Ban[0]);
/* 2745 */     for (Ban ban : banArr) {
/*      */       
/* 2747 */       if (ban != null && 
/* 2748 */         ban.getIdentifier().equals(steamId))
/* 2749 */         if (ban.isExpired()) { removeBan(ban); }
/* 2750 */         else { return ban; }
/*      */          
/* 2752 */     }  return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban getBannedIp(String ip) {
/* 2757 */     if (ip.isEmpty()) return null; 
/* 2758 */     Ban[] bips = bans.<Ban>toArray(new Ban[0]);
/* 2759 */     int dots = 0;
/* 2760 */     for (Ban lBip : bips) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2766 */       if (lBip == null) {
/*      */         
/* 2768 */         logger.warning("BannedIPs includes a null");
/* 2769 */         return null;
/*      */       } 
/* 2771 */       dots = lBip.getIdentifier().indexOf("*");
/* 2772 */       if (dots > 0)
/*      */       {
/* 2774 */         if (lBip.isExpired()) {
/* 2775 */           removeBan(lBip.getIdentifier());
/* 2776 */         } else if (lBip.getIdentifier().substring(0, dots).equals(ip.substring(0, dots))) {
/* 2777 */           return lBip;
/*      */         }  } 
/* 2779 */       if (lBip.getIdentifier().equals(ip))
/*      */       {
/* 2781 */         if (lBip.isExpired()) {
/* 2782 */           removeBan(lBip.getIdentifier());
/*      */         } else {
/* 2784 */           return lBip;
/*      */         }  } 
/*      */     } 
/* 2787 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban[] getBans() {
/* 2792 */     Ban[] bips = bans.<Ban>toArray(new Ban[bans.size()]);
/* 2793 */     Arrays.sort(bips, new Comparator<Ban>()
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public int compare(Ban o1, Ban o2)
/*      */           {
/* 2801 */             Ban i1 = o1;
/* 2802 */             Ban i2 = o2;
/* 2803 */             return i1.getIdentifier().compareTo(i2.getIdentifier());
/*      */           }
/*      */         });
/* 2806 */     return bips;
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertFromKingdomToKingdom(byte oldKingdom, byte newKingdom) {
/* 2811 */     Player[] playerArr = getPlayers();
/* 2812 */     for (Player play : playerArr) {
/*      */       
/* 2814 */       if (play.getKingdomId() == oldKingdom) {
/*      */         
/*      */         try {
/*      */           
/* 2818 */           play.setKingdomId(newKingdom, true);
/*      */         }
/* 2820 */         catch (IOException iox) {
/*      */           
/* 2822 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2828 */     Connection dbcon = null;
/* 2829 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2832 */       dbcon = DbConnector.getPlayerDbCon();
/* 2833 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET KINGDOM=? WHERE KINGDOM=?");
/* 2834 */       ps.setByte(1, newKingdom);
/* 2835 */       ps.setByte(2, oldKingdom);
/* 2836 */       ps.executeUpdate();
/*      */     }
/* 2838 */     catch (SQLException sqex) {
/*      */       
/* 2840 */       logger.log(Level.WARNING, "Failed to change kingdom to " + newKingdom, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2844 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2845 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void convertPlayerToKingdom(long wurmId, byte newKingdom) {
/* 2854 */     Connection dbcon = null;
/* 2855 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2858 */       dbcon = DbConnector.getPlayerDbCon();
/* 2859 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET KINGDOM=? WHERE WURMID=?");
/* 2860 */       ps.setByte(1, newKingdom);
/* 2861 */       ps.setLong(2, wurmId);
/* 2862 */       ps.executeUpdate();
/*      */     }
/* 2864 */     catch (SQLException sqex) {
/*      */       
/* 2866 */       logger.log(Level.WARNING, "Failed to change kingdom to " + newKingdom + " for " + wurmId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2870 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2871 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastLogoutForPlayer(long wurmid) {
/* 2878 */     long toReturn = 0L;
/* 2879 */     if (getPlayerById(wurmid) != null) {
/* 2880 */       toReturn = System.currentTimeMillis();
/*      */     } else {
/*      */       
/* 2883 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 2884 */       if (pinf != null) {
/* 2885 */         return pinf.lastLogout;
/*      */       }
/*      */     } 
/*      */     
/* 2889 */     Connection dbcon = null;
/* 2890 */     PreparedStatement ps = null;
/* 2891 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2894 */       dbcon = DbConnector.getPlayerDbCon();
/* 2895 */       ps = dbcon.prepareStatement("SELECT LASTLOGOUT FROM PLAYERS WHERE WURMID=?");
/* 2896 */       ps.setLong(1, wurmid);
/* 2897 */       rs = ps.executeQuery();
/* 2898 */       if (rs.next())
/*      */       {
/* 2900 */         toReturn = rs.getLong("LASTLOGOUT");
/*      */       }
/*      */     }
/* 2903 */     catch (SQLException sqex) {
/*      */       
/* 2905 */       logger.log(Level.WARNING, "Failed to retrieve lastlogout for " + wurmid, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2909 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2910 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2913 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doesPlayerNameExist(String name) {
/* 2918 */     if (getPlayerByName(name) != null) {
/* 2919 */       return true;
/*      */     }
/*      */     
/* 2922 */     Connection dbcon = null;
/* 2923 */     PreparedStatement ps = null;
/* 2924 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2927 */       dbcon = DbConnector.getPlayerDbCon();
/* 2928 */       ps = dbcon.prepareStatement("SELECT WURMID FROM PLAYERS WHERE NAME=?");
/* 2929 */       ps.setString(1, name);
/* 2930 */       rs = ps.executeQuery();
/* 2931 */       if (rs.next())
/*      */       {
/* 2933 */         return true;
/*      */       }
/*      */     }
/* 2936 */     catch (SQLException sqex) {
/*      */       
/* 2938 */       logger.log(Level.WARNING, "Failed to check if " + name + " exists:" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2942 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2943 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2946 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getWurmIdByPlayerName(String name) {
/* 2951 */     String lName = LoginHandler.raiseFirstLetter(name);
/* 2952 */     if (getPlayerByName(lName) != null) {
/* 2953 */       return getPlayerByName(lName).getWurmId();
/*      */     }
/*      */     
/* 2956 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(lName);
/* 2957 */     if (pinf.wurmId > 0L) {
/* 2958 */       return pinf.wurmId;
/*      */     }
/*      */ 
/*      */     
/* 2962 */     Connection dbcon = null;
/* 2963 */     PreparedStatement ps = null;
/* 2964 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2967 */       dbcon = DbConnector.getPlayerDbCon();
/* 2968 */       ps = dbcon.prepareStatement("SELECT WURMID FROM PLAYERS WHERE NAME=?");
/* 2969 */       ps.setString(1, lName);
/* 2970 */       rs = ps.executeQuery();
/* 2971 */       if (rs.next())
/*      */       {
/* 2973 */         return rs.getLong("WURMID");
/*      */       }
/*      */     }
/* 2976 */     catch (SQLException sqex) {
/*      */       
/* 2978 */       logger.log(Level.WARNING, "Failed to retrieve wurmid for " + name + " exists:" + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2982 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2983 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 2986 */     return -1L;
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
/*      */   public void registerNewKingdom(Creature registered) {
/* 2999 */     registerNewKingdom(registered.getWurmId(), registered.getKingdomId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void pollChamps() {
/* 3005 */     if (System.currentTimeMillis() - Servers.localServer.lastDecreasedChampionPoints > 604800000L) {
/*      */       
/* 3007 */       Servers.localServer.setChampStamp();
/* 3008 */       PlayerInfo[] playinfos = PlayerInfoFactory.getPlayerInfos();
/* 3009 */       for (int p = 0; p < playinfos.length; p++) {
/*      */         
/* 3011 */         if ((playinfos[p]).realdeath > 0 && (playinfos[p]).realdeath < 5) {
/*      */           
/*      */           try {
/*      */ 
/*      */ 
/*      */             
/* 3017 */             Player play = getInstance().getPlayer((playinfos[p]).wurmId);
/* 3018 */             play.sendAddChampionPoints();
/*      */           }
/* 3020 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3029 */     printChampStats();
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
/*      */   public void registerNewKingdom(long aWurmId, byte aKingdom) {
/* 3061 */     this.pkingdoms.put(Long.valueOf(aWurmId), Byte.valueOf(aKingdom));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getKingdomForPlayer(long wurmid) {
/* 3066 */     Byte b = this.pkingdoms.get(Long.valueOf(wurmid));
/* 3067 */     if (b != null)
/* 3068 */       return b.byteValue(); 
/* 3069 */     Player lPlayerById = getPlayerById(wurmid);
/* 3070 */     if (lPlayerById != null) {
/*      */       
/* 3072 */       registerNewKingdom(wurmid, lPlayerById.getKingdomId());
/* 3073 */       return lPlayerById.getKingdomId();
/*      */     } 
/*      */ 
/*      */     
/* 3077 */     Connection dbcon = null;
/* 3078 */     PreparedStatement ps = null;
/* 3079 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3082 */       dbcon = DbConnector.getPlayerDbCon();
/* 3083 */       ps = dbcon.prepareStatement("SELECT KINGDOM FROM PLAYERS WHERE WURMID=?");
/* 3084 */       ps.setLong(1, wurmid);
/* 3085 */       rs = ps.executeQuery();
/* 3086 */       if (rs.next())
/*      */       {
/* 3088 */         byte toret = rs.getByte("KINGDOM");
/* 3089 */         this.pkingdoms.put(Long.valueOf(wurmid), Byte.valueOf(toret));
/* 3090 */         return toret;
/*      */       }
/*      */     
/* 3093 */     } catch (SQLException sqex) {
/*      */       
/* 3095 */       logger.log(Level.WARNING, "Failed to retrieve kingdom for " + wurmid, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3099 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3100 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 3103 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPlayersFromKingdom(byte kingdomId) {
/* 3108 */     int nums = 0;
/* 3109 */     Connection dbcon = null;
/* 3110 */     PreparedStatement ps = null;
/* 3111 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3114 */       dbcon = DbConnector.getPlayerDbCon();
/* 3115 */       ps = dbcon.prepareStatement("SELECT NAME,WURMID FROM PLAYERS WHERE KINGDOM=? AND CURRENTSERVER=? AND POWER=0");
/* 3116 */       ps.setByte(1, kingdomId);
/* 3117 */       ps.setInt(2, Servers.localServer.id);
/* 3118 */       rs = ps.executeQuery();
/* 3119 */       rs.last();
/* 3120 */       return rs.getRow();
/*      */     }
/* 3122 */     catch (SQLException sqex) {
/*      */       
/* 3124 */       logger.log(Level.WARNING, "Failed to retrieve nums kingdom for " + kingdomId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3128 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3129 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/* 3132 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getChampionsFromKingdom(byte kingdomId) {
/* 3137 */     int nums = 0;
/* 3138 */     Connection dbcon = null;
/* 3139 */     PreparedStatement ps = null;
/* 3140 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3143 */       dbcon = DbConnector.getPlayerDbCon();
/* 3144 */       ps = dbcon.prepareStatement("SELECT NAME,WURMID,REALDEATH,LASTLOSTCHAMPION FROM PLAYERS WHERE KINGDOM=? AND REALDEATH>0 AND REALDEATH<4 AND POWER=0");
/* 3145 */       ps.setByte(1, kingdomId);
/* 3146 */       rs = ps.executeQuery();
/* 3147 */       while (rs.next()) {
/*      */         
/* 3149 */         long wid = rs.getLong("WURMID");
/*      */         
/* 3151 */         String name = rs.getString("NAME");
/* 3152 */         long lastChamped = rs.getLong("LASTLOSTCHAMPION");
/* 3153 */         int realDeath = rs.getInt("REALDEATH");
/* 3154 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wid);
/* 3155 */         if (pinf.getCurrentServer() == Servers.localServer.id)
/*      */         {
/* 3157 */           if (System.currentTimeMillis() - pinf.championTimeStamp < 14515200000L)
/* 3158 */             nums++; 
/*      */         }
/*      */       } 
/* 3161 */       return nums;
/*      */     }
/* 3163 */     catch (SQLException sqex) {
/*      */       
/* 3165 */       logger.log(Level.WARNING, "Failed to retrieve nums kingdom for " + kingdomId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3169 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3170 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3172 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getChampionsFromKingdom(byte kingdomId, int deity) {
/* 3177 */     int nums = 0;
/* 3178 */     Connection dbcon = null;
/* 3179 */     PreparedStatement ps = null;
/* 3180 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3183 */       dbcon = DbConnector.getPlayerDbCon();
/* 3184 */       ps = dbcon.prepareStatement("SELECT NAME,WURMID,REALDEATH,LASTLOSTCHAMPION FROM PLAYERS WHERE KINGDOM=? AND REALDEATH>0 AND REALDEATH<4 AND POWER=0");
/* 3185 */       ps.setByte(1, kingdomId);
/* 3186 */       rs = ps.executeQuery();
/* 3187 */       while (rs.next()) {
/*      */         
/* 3189 */         long wid = rs.getLong("WURMID");
/*      */         
/* 3191 */         String name = rs.getString("NAME");
/* 3192 */         long lastChamped = rs.getLong("LASTLOSTCHAMPION");
/* 3193 */         int realDeath = rs.getInt("REALDEATH");
/* 3194 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wid);
/* 3195 */         if (pinf.getCurrentServer() == Servers.localServer.id)
/*      */         {
/* 3197 */           if (System.currentTimeMillis() - pinf.championTimeStamp < 14515200000L)
/*      */           {
/* 3199 */             if (pinf.getDeity() != null && pinf.getDeity().getNumber() == deity)
/* 3200 */               nums++; 
/*      */           }
/*      */         }
/*      */       } 
/* 3204 */       logger.log(Level.INFO, "Found " + nums + " champs for kingdom =" + kingdomId);
/* 3205 */       return nums;
/*      */     }
/* 3207 */     catch (SQLException sqex) {
/*      */       
/* 3209 */       logger.log(Level.WARNING, "Failed to retrieve nums kingdom for " + kingdomId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3213 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3214 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3216 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getPremiumPlayersFromKingdom(byte kingdomId) {
/* 3221 */     int nums = 0;
/* 3222 */     Connection dbcon = null;
/* 3223 */     PreparedStatement ps = null;
/* 3224 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3227 */       dbcon = DbConnector.getPlayerDbCon();
/* 3228 */       ps = dbcon.prepareStatement("SELECT NAME,WURMID FROM PLAYERS WHERE KINGDOM=? AND PAYMENTEXPIRE>? AND POWER=0");
/* 3229 */       ps.setByte(1, kingdomId);
/* 3230 */       ps.setLong(2, System.currentTimeMillis());
/* 3231 */       rs = ps.executeQuery();
/* 3232 */       while (rs.next()) {
/*      */         
/* 3234 */         long wid = rs.getLong("WURMID");
/* 3235 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wid);
/* 3236 */         if (pinf.getCurrentServer() == Servers.localServer.id || 
/* 3237 */           System.currentTimeMillis() - pinf.getLastLogout() < 259200000L)
/* 3238 */           nums++; 
/*      */       } 
/* 3240 */       return nums;
/*      */     }
/* 3242 */     catch (SQLException sqex) {
/*      */       
/* 3244 */       logger.log(Level.WARNING, "Failed to retrieve nums kingdom for " + kingdomId, sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 3248 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3249 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3251 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStructureFinished(long structureid) {
/* 3256 */     Player[] playarr = getPlayers();
/* 3257 */     boolean found = false;
/* 3258 */     for (Player lPlayer : playarr) {
/*      */ 
/*      */       
/*      */       try {
/* 3262 */         if (lPlayer.getStructure().getWurmId() == structureid) {
/*      */           
/*      */           try {
/*      */             
/* 3266 */             lPlayer.setStructure(null);
/* 3267 */             lPlayer.save();
/* 3268 */             found = true;
/*      */             
/*      */             break;
/* 3271 */           } catch (Exception ex) {
/*      */             
/* 3273 */             logger.log(Level.WARNING, "Failed to set structure finished for " + lPlayer, ex);
/*      */           }
/*      */         
/*      */         }
/* 3277 */       } catch (NoSuchStructureException noSuchStructureException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3282 */     if (!found) {
/*      */       
/* 3284 */       Connection dbcon = null;
/* 3285 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3288 */         dbcon = DbConnector.getPlayerDbCon();
/* 3289 */         ps = dbcon.prepareStatement("update PLAYERS set BUILDINGID=-10 WHERE BUILDINGID=?");
/* 3290 */         ps.setLong(1, structureid);
/* 3291 */         ps.executeUpdate();
/*      */       }
/* 3293 */       catch (SQLException sqex) {
/*      */         
/* 3295 */         logger.log(Level.WARNING, "Failed to set buidlingid to -10 for " + structureid, sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 3299 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3300 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resetFaithGain() {
/* 3307 */     PlayerInfoFactory.resetFaithGain();
/*      */     
/* 3309 */     Connection dbcon = null;
/* 3310 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3313 */       dbcon = DbConnector.getPlayerDbCon();
/* 3314 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET LASTFAITH=0,NUMFAITH=0");
/* 3315 */       ps.executeUpdate();
/*      */     }
/* 3317 */     catch (SQLException sqx) {
/*      */       
/* 3319 */       logger.log(Level.WARNING, "Problem resetting faith gain - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3323 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3324 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void payGms() {
/* 3330 */     Connection dbcon = null;
/* 3331 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3334 */       dbcon = DbConnector.getPlayerDbCon();
/* 3335 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET MONEY=MONEY+250000 WHERE POWER>1");
/*      */       
/* 3337 */       ps.executeUpdate();
/*      */     }
/* 3339 */     catch (SQLException sqx) {
/*      */       
/* 3341 */       logger.log(Level.WARNING, "Problem processing GM Salary - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3345 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3346 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3348 */     Player[] playarr = getPlayers();
/* 3349 */     for (Player lPlayer : playarr) {
/*      */       
/* 3351 */       if (lPlayer.getPower() > 0)
/*      */       {
/* 3353 */         lPlayer.getCommunicator().sendSafeServerMessage("You have now received salary.");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetPlayer(long wurmid) {
/* 3360 */     Connection dbcon = null;
/* 3361 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3364 */       dbcon = DbConnector.getPlayerDbCon();
/* 3365 */       ps = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=20, MINVALUE=20 WHERE VALUE>20 AND OWNER=?");
/* 3366 */       ps.setLong(1, wurmid);
/* 3367 */       ps.executeUpdate();
/*      */     }
/* 3369 */     catch (SQLException sqx) {
/*      */       
/* 3371 */       logger.log(Level.WARNING, "Problem resetting player skills - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3375 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3376 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 3380 */       dbcon = DbConnector.getPlayerDbCon();
/* 3381 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET FAITH=20 WHERE FAITH>20 AND WURMID=?");
/* 3382 */       ps.setLong(1, wurmid);
/* 3383 */       ps.executeUpdate();
/*      */     }
/* 3385 */     catch (SQLException sqx) {
/*      */       
/* 3387 */       logger.log(Level.WARNING, "Problem resetting player faith - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3391 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3392 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 3396 */       Player p = getPlayer(wurmid);
/*      */       
/*      */       try {
/* 3399 */         if (p.isChampion()) {
/*      */           
/* 3401 */           p.revertChamp();
/* 3402 */           if (p.getFaith() > 20.0F) {
/* 3403 */             p.setFaith(20.0F);
/*      */           }
/*      */         } 
/* 3406 */       } catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */       
/* 3410 */       Skills sk = p.getSkills();
/* 3411 */       Skill[] skills = sk.getSkills();
/* 3412 */       for (int x = 0; x < skills.length; x++) {
/*      */         
/* 3414 */         if (skills[x].getKnowledge() > 20.0D)
/*      */         {
/* 3416 */           (skills[x]).minimum = 20.0D;
/* 3417 */           skills[x].setKnowledge(20.0D, true);
/*      */         }
/*      */       
/*      */       } 
/* 3421 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/* 3423 */       PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 3424 */       if (p != null) {
/*      */         
/*      */         try {
/*      */           
/* 3428 */           p.setRealDeath((byte)0);
/*      */         }
/* 3430 */         catch (IOException iOException) {}
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
/*      */   public static void sendGmMessages(Player player) {
/* 3443 */     Connection dbcon = null;
/* 3444 */     PreparedStatement ps = null;
/* 3445 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3448 */       dbcon = DbConnector.getPlayerDbCon();
/* 3449 */       ps = dbcon.prepareStatement("SELECT TIME,SENDER,MESSAGE FROM GMMESSAGES ORDER BY TIME");
/* 3450 */       rs = ps.executeQuery();
/* 3451 */       while (rs.next())
/*      */       {
/* 3453 */         player.getCommunicator().sendGmMessage(rs.getLong("TIME"), rs.getString("SENDER"), rs.getString("MESSAGE"));
/*      */       }
/*      */     }
/* 3456 */     catch (SQLException sqx) {
/*      */       
/* 3458 */       logger.log(Level.WARNING, "Problem getting GM messages - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3462 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3463 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3465 */     pruneMessages();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sendMgmtMessages(Player player) {
/* 3472 */     Connection dbcon = null;
/* 3473 */     PreparedStatement ps = null;
/* 3474 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3477 */       dbcon = DbConnector.getPlayerDbCon();
/* 3478 */       ps = dbcon.prepareStatement("SELECT TIME,SENDER,MESSAGE FROM MGMTMESSAGES ORDER BY TIME");
/* 3479 */       rs = ps.executeQuery();
/* 3480 */       while (rs.next())
/*      */       {
/* 3482 */         player.getCommunicator().sendMgmtMessage(rs.getLong("TIME"), rs.getString("SENDER"), rs.getString("MESSAGE"));
/*      */       }
/*      */     }
/* 3485 */     catch (SQLException sqx) {
/*      */       
/* 3487 */       logger.log(Level.WARNING, "Problem getting management messages - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3491 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3492 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3494 */     pruneMessages();
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStartKingdomChat(Player player) {
/* 3499 */     if (player.showKingdomStartMessage()) {
/*      */ 
/*      */       
/* 3502 */       Message mess = new Message((Creature)player, (byte)10, Kingdoms.getChatNameFor(player.getKingdomId()), "<System> This is the Kingdom Chat for your current server. ", 250, 150, 250);
/*      */ 
/*      */       
/* 3505 */       player.getCommunicator().sendMessage(mess);
/*      */       
/* 3507 */       Message mess1 = new Message((Creature)player, (byte)10, Kingdoms.getChatNameFor(player.getKingdomId()), "<System> You can disable receiving these messages, by a setting in your profile.", 250, 150, 250);
/*      */ 
/*      */       
/* 3510 */       player.getCommunicator().sendMessage(mess1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStartGlobalKingdomChat(Player player) {
/* 3516 */     if (player.showGlobalKingdomStartMessage()) {
/*      */ 
/*      */       
/* 3519 */       Message mess = new Message((Creature)player, (byte)16, "GL-" + Kingdoms.getChatNameFor(player.getKingdomId()), "<System> This is your Global Kingdom Chat. ", 250, 150, 250);
/*      */ 
/*      */       
/* 3522 */       player.getCommunicator().sendMessage(mess);
/*      */       
/* 3524 */       Message mess1 = new Message((Creature)player, (byte)16, "GL-" + Kingdoms.getChatNameFor(player.getKingdomId()), "<System> You can disable receiving these messages, by a setting in your profile.", 250, 150, 250);
/*      */ 
/*      */       
/* 3527 */       player.getCommunicator().sendMessage(mess1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendStartGlobalTradeChannel(Player player) {
/* 3533 */     if (player.showTradeStartMessage()) {
/*      */       
/* 3535 */       Message mess = new Message((Creature)player, (byte)18, "Trade", "<System> This is the Trade channel. ", 250, 150, 250);
/*      */ 
/*      */       
/* 3538 */       player.getCommunicator().sendMessage(mess);
/* 3539 */       Message mess1 = new Message((Creature)player, (byte)18, "Trade", "<System> Only messages starting with WTB, WTS, WTT, PC or @ are allowed. ", 250, 150, 250);
/*      */ 
/*      */       
/* 3542 */       player.getCommunicator().sendMessage(mess1);
/* 3543 */       Message mess2 = new Message((Creature)player, (byte)18, "Trade", "<System> Please PM the person if you are interested in the Item.", 250, 150, 250);
/*      */ 
/*      */       
/* 3546 */       player.getCommunicator().sendMessage(mess2);
/* 3547 */       Message mess3 = new Message((Creature)player, (byte)18, "Trade", "<System> You can also use @<name> to send a reply in this channel to <name>.", 250, 150, 250);
/*      */ 
/*      */       
/* 3550 */       player.getCommunicator().sendMessage(mess3);
/* 3551 */       Message mess4 = new Message((Creature)player, (byte)18, "Trade", "<System> You can disable receiving these messages, by a setting in your profile.", 250, 150, 250);
/*      */ 
/*      */       
/* 3554 */       player.getCommunicator().sendMessage(mess4);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Integer> getBattleRanks(int num) {
/* 3560 */     Map<String, Integer> toReturn = new HashMap<>();
/* 3561 */     Connection dbcon = null;
/* 3562 */     PreparedStatement ps = null;
/* 3563 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3566 */       dbcon = DbConnector.getPlayerDbCon();
/* 3567 */       ps = dbcon.prepareStatement("select RANK, NAME from PLAYERS ORDER BY RANK DESC LIMIT ?");
/* 3568 */       ps.setInt(1, num);
/* 3569 */       rs = ps.executeQuery();
/* 3570 */       while (rs.next())
/*      */       {
/* 3572 */         toReturn.put(rs.getString("NAME"), Integer.valueOf(rs.getInt("RANK")));
/*      */       }
/*      */     }
/* 3575 */     catch (SQLException sqx) {
/*      */       
/* 3577 */       logger.log(Level.WARNING, "Problem getting battle ranks - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3581 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3582 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3584 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Integer> getMaxBattleRanks(int num) {
/* 3589 */     Map<String, Integer> toReturn = new HashMap<>();
/* 3590 */     Connection dbcon = null;
/* 3591 */     PreparedStatement ps = null;
/* 3592 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 3595 */       dbcon = DbConnector.getPlayerDbCon();
/* 3596 */       ps = dbcon.prepareStatement("select MAXRANK,RANK, NAME from PLAYERS ORDER BY MAXRANK DESC LIMIT ?");
/* 3597 */       ps.setInt(1, num);
/* 3598 */       rs = ps.executeQuery();
/* 3599 */       while (rs.next())
/*      */       {
/* 3601 */         toReturn.put(rs.getString("NAME"), Integer.valueOf(rs.getInt("MAXRANK")));
/*      */       }
/*      */     }
/* 3604 */     catch (SQLException sqx) {
/*      */       
/* 3606 */       logger.log(Level.WARNING, "Problem getting Max battle ranks - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 3610 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 3611 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 3613 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printMaxRanks() {
/* 3621 */     Writer output = null;
/*      */     
/*      */     try {
/* 3624 */       String dir = Constants.webPath;
/* 3625 */       if (!dir.endsWith(File.separator))
/* 3626 */         dir = dir + File.separator; 
/* 3627 */       File aFile = new File(dir + "maxranks.html");
/*      */ 
/*      */ 
/*      */       
/* 3631 */       output = new BufferedWriter(new FileWriter(aFile));
/* 3632 */       String start = "<TABLE class=\"gameDataTable\"><TR><TH><Name</TH><TH>Rank</TH></TR>";
/*      */       
/*      */       try {
/* 3635 */         output.write("<TABLE class=\"gameDataTable\"><TR><TH><Name</TH><TH>Rank</TH></TR>");
/*      */       }
/* 3637 */       catch (IOException iox) {
/*      */         
/* 3639 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/* 3641 */       int nums = 0;
/* 3642 */       Connection dbcon = null;
/* 3643 */       PreparedStatement ps = null;
/* 3644 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 3647 */         dbcon = DbConnector.getPlayerDbCon();
/* 3648 */         ps = dbcon.prepareStatement("select MAXRANK,RANK, NAME from PLAYERS ORDER BY MAXRANK DESC LIMIT ?");
/* 3649 */         ps.setInt(1, 30);
/* 3650 */         rs = ps.executeQuery();
/* 3651 */         while (rs.next())
/*      */         {
/* 3653 */           if (nums < 10) {
/* 3654 */             output.write("<TR class=\"gameDataTopTenTR\"><TD class=\"gameDataTopTenTDName\">" + rs
/* 3655 */                 .getString("NAME") + "</TD><TD class=\"gameDataTopTenTDValue\">" + rs
/* 3656 */                 .getInt("MAXRANK") + "</TD></TR>");
/*      */           } else {
/* 3658 */             output.write("<TR class=\"gameDataTR\"><TD class=\"gameDataTDName\">" + rs.getString("NAME") + "</TD><TD class=\"gameDataTDValue\">" + rs
/* 3659 */                 .getInt("MAXRANK") + "</TD></TR>");
/* 3660 */           }  nums++;
/*      */         }
/*      */       
/* 3663 */       } catch (SQLException sqx) {
/*      */         
/* 3665 */         logger.log(Level.WARNING, "Problem writing maxranks" + sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3669 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 3670 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3672 */       output.write("</TABLE>");
/*      */     }
/* 3674 */     catch (IOException iox) {
/*      */       
/* 3676 */       logger.log(Level.WARNING, "Failed to save maxranks.html", iox);
/*      */     } finally {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 3683 */         if (output != null) {
/* 3684 */           output.close();
/*      */         }
/* 3686 */       } catch (IOException iOException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 3692 */   private static String header2 = "<HTML>\n\t<HEAD>\n\t<TITLE>Wurm Online battle ranks</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t";
/*      */   
/*      */   private static final String footer2 = "\n</BODY>\n</HTML>";
/*      */   
/* 3696 */   private static String headerStats = "<!DOCTYPE html> <HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <TITLE>Wurm Online Server Stats</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t";
/* 3697 */   private static String headerStats2 = "<!DOCTYPE html> <HTML>\n\t<HEAD>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <TITLE>Wurm Online Champion Eternal Records</TITLE>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.wurmonline.com/css/gameData.css\" />\n\t</HEAD>\n\n<BODY id=\"body\" class=\"gameDataBody\">\n\t";
/*      */   
/*      */   private static final String footerStats = "\n</BODY>\n</HTML>";
/*      */ 
/*      */   
/*      */   public static void printRanks() {
/* 3703 */     printMaxRanks();
/* 3704 */     Writer output = null;
/*      */     
/*      */     try {
/* 3707 */       String dir = Constants.webPath;
/* 3708 */       if (!dir.endsWith(File.separator))
/* 3709 */         dir = dir + File.separator; 
/* 3710 */       File aFile = new File(dir + "ranks.html");
/*      */ 
/*      */ 
/*      */       
/* 3714 */       output = new BufferedWriter(new FileWriter(aFile));
/* 3715 */       output.write(header2);
/* 3716 */       String start = "<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Rank</TH>\n\t\t</TR>\n\t\t";
/*      */ 
/*      */       
/*      */       try {
/* 3720 */         output.write("<TABLE id=\"gameDataTable\">\n\t\t<TR>\n\t\t\t<TH>Name</TH>\n\t\t\t<TH>Rank</TH>\n\t\t</TR>\n\t\t");
/*      */       }
/* 3722 */       catch (IOException iox) {
/*      */         
/* 3724 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/* 3726 */       int nums = 0;
/* 3727 */       Connection dbcon = null;
/* 3728 */       PreparedStatement ps = null;
/* 3729 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 3732 */         dbcon = DbConnector.getPlayerDbCon();
/* 3733 */         ps = dbcon.prepareStatement("select RANK, NAME from PLAYERS ORDER BY RANK DESC LIMIT ?");
/* 3734 */         ps.setInt(1, 30);
/* 3735 */         rs = ps.executeQuery();
/* 3736 */         while (rs.next())
/*      */         {
/*      */           
/* 3739 */           if (nums < 10) {
/* 3740 */             output.write("<TR class=\"gameDataTopTenTR\">\n\t\t\t<TD class=\"gameDataTopTenTDName\">" + rs
/* 3741 */                 .getString("NAME") + "</TD>\n\t\t\t<TD class=\"gameDataTopTenTDValue\">" + rs
/* 3742 */                 .getInt("RANK") + "</TD>\n\t\t</TR>\n\t\t");
/*      */           } else {
/* 3744 */             output.write("<TR class=\"gameDataTR\">\n\t\t\t<TD class=\"gameDataTDName\">" + rs.getString("NAME") + "</TD>\n\t\t\t<TD class=\"gameDataTDValue\">" + rs
/* 3745 */                 .getInt("RANK") + "</TD>\n\t\t</TR>\n\n\t");
/*      */           } 
/*      */           
/* 3748 */           nums++;
/*      */         }
/*      */       
/* 3751 */       } catch (SQLException sqx) {
/*      */         
/* 3753 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 3757 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 3758 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3760 */       output.write("</TABLE>\n");
/*      */       
/* 3762 */       output.write("\n</BODY>\n</HTML>");
/*      */     }
/* 3764 */     catch (IOException iox) {
/*      */       
/* 3766 */       logger.log(Level.WARNING, "Failed to close ranks.html", iox);
/*      */     } finally {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 3773 */         if (output != null) {
/* 3774 */           output.close();
/*      */         }
/* 3776 */       } catch (IOException iOException) {}
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
/*      */   public static void printChampStats() {
/* 3791 */     WurmRecord[] alls = PlayerInfoFactory.getChampionRecords();
/* 3792 */     if (alls.length > 0) {
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
/* 3806 */       Writer output = null;
/*      */       
/*      */       try {
/* 3809 */         String dir = Constants.webPath;
/* 3810 */         if (!dir.endsWith(File.separator))
/* 3811 */           dir = dir + File.separator; 
/* 3812 */         File aFile = new File(dir + "champs.html");
/*      */ 
/*      */         
/* 3815 */         output = new BufferedWriter(new FileWriter(aFile));
/*      */ 
/*      */         
/*      */         try {
/* 3819 */           output.write(headerStats2);
/* 3820 */           String start = "<TABLE id=\"statsDataTable\">\n\t\t<TR>\n\t\t\t<TH></TH>\n\t\t\t<TH></TH>\n\t\t</TR>\n\t\t";
/* 3821 */           output.write("<TABLE id=\"statsDataTable\">\n\t\t<TR>\n\t\t\t<TH></TH>\n\t\t\t<TH></TH>\n\t\t</TR>\n\t\t");
/*      */           
/* 3823 */           int total = 0;
/* 3824 */           int totalLimit = 0;
/* 3825 */           for (WurmRecord entry : alls) {
/*      */             
/* 3827 */             output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">" + entry.getHolder() + " players</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + entry
/*      */                 
/* 3829 */                 .getValue() + " current=" + entry.isCurrent() + "</TD>\n\t\t</TR>\n\t\t");
/* 3830 */             total += entry.getValue();
/* 3831 */             totalLimit++;
/*      */           } 
/* 3833 */           output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Average points</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + total + "/" + totalLimit + "=" + (total / totalLimit) + "</TD>\n\t\t</TR>\n\t\t");
/*      */           
/* 3835 */           output.write("</TABLE>\n");
/* 3836 */           output.write("\n</BODY>\n</HTML>");
/*      */         }
/* 3838 */         catch (IOException iox) {
/*      */           
/* 3840 */           logger.log(Level.WARNING, "Problem writing server stats = " + iox.getMessage(), iox);
/*      */         }
/*      */       
/* 3843 */       } catch (IOException iox) {
/*      */         
/* 3845 */         logger.log(Level.WARNING, "Failed to open stats.html", iox);
/*      */       } finally {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 3852 */           if (output != null) {
/* 3853 */             output.close();
/*      */           }
/* 3855 */         } catch (IOException iOException) {}
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
/*      */   public static void printStats() {
/*      */     try {
/* 3872 */       String dir = Constants.webPath;
/* 3873 */       if (!dir.endsWith(File.separator))
/* 3874 */         dir = dir + File.separator; 
/* 3875 */       File aFile = new File(dir + "stats.xml");
/* 3876 */       StatsXMLWriter.createXML(aFile);
/*      */     }
/* 3878 */     catch (Exception ex) {
/*      */       
/* 3880 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/* 3882 */     Writer output = null;
/*      */     
/*      */     try {
/* 3885 */       String dir = Constants.webPath;
/* 3886 */       if (!dir.endsWith(File.separator))
/* 3887 */         dir = dir + File.separator; 
/* 3888 */       File aFile = new File(dir + "stats.html");
/*      */ 
/*      */       
/* 3891 */       output = new BufferedWriter(new FileWriter(aFile));
/*      */ 
/*      */       
/*      */       try {
/* 3895 */         output.write(headerStats);
/* 3896 */         String start = "<TABLE id=\"statsDataTable\">\n\t\t<TR>\n\t\t\t<TH></TH>\n\t\t\t<TH></TH>\n\t\t</TR>\n\t\t";
/* 3897 */         output.write("<TABLE id=\"statsDataTable\">\n\t\t<TR>\n\t\t\t<TH></TH>\n\t\t\t<TH></TH>\n\t\t</TR>\n\t\t");
/* 3898 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Server name</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + Servers.localServer
/* 3899 */             .getName() + "</TD>\n\t\t</TR>\n\t\t");
/* 3900 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Last updated</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + 
/* 3901 */             DateFormat.getDateInstance(2).format(new Timestamp(System.currentTimeMillis())) + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3903 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Status</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + (Servers.localServer.maintaining ? "Maintenance" : (
/*      */             
/* 3905 */             (Server.getMillisToShutDown() > 0L) ? ("Shutting down in " + (
/* 3906 */             Server.getMillisToShutDown() / 1000L) + " seconds") : "Up and running")) + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3908 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Uptime</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + 
/* 3909 */             Server.getTimeFor((Server.getSecondsUptime() * 1000)) + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3911 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Wurm Time</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + 
/* 3912 */             WurmCalendar.getTime() + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3914 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Weather</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + 
/* 3915 */             Server.getWeather().getWeatherString(false) + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3917 */         int total = 0;
/* 3918 */         int totalLimit = 0;
/* 3919 */         int epic = 0;
/* 3920 */         int epicMax = 0;
/* 3921 */         ServerEntry[] alls = Servers.getAllServers();
/* 3922 */         for (ServerEntry entry : alls) {
/*      */           
/* 3924 */           if (!entry.EPIC) {
/*      */             
/* 3926 */             if (!entry.isLocal)
/*      */             {
/* 3928 */               output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">" + entry.getName() + " players</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + entry.currentPlayers + "/" + entry.pLimit + "</TD>\n\t\t</TR>\n\t\t");
/*      */ 
/*      */               
/* 3931 */               total += entry.currentPlayers;
/* 3932 */               totalLimit += entry.pLimit;
/*      */             }
/*      */             else
/*      */             {
/* 3936 */               output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">" + entry.getName() + " players</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + 
/*      */                   
/* 3938 */                   getInstance().getNumberOfPlayers() + "/" + entry.pLimit + "</TD>\n\t\t</TR>\n\t\t");
/* 3939 */               total += getInstance().getNumberOfPlayers();
/* 3940 */               totalLimit += entry.pLimit;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 3945 */             epic += entry.currentPlayers;
/* 3946 */             epicMax += entry.pLimit;
/* 3947 */             totalLimit += entry.pLimit;
/* 3948 */             total += entry.currentPlayers;
/*      */           } 
/*      */         } 
/* 3951 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Epic cluster players</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + epic + "/" + epicMax + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3953 */         output.write("<TR class=\"statsTR\">\n\t\t\t<TD class=\"statsDataTDName\">Total players</TD>\n\t\t\t<TD class=\"statsDataTDValue\">" + total + "/" + totalLimit + "</TD>\n\t\t</TR>\n\t\t");
/*      */         
/* 3955 */         output.write("</TABLE>\n");
/* 3956 */         output.write("\n</BODY>\n</HTML>");
/*      */       }
/* 3958 */       catch (IOException iox) {
/*      */         
/* 3960 */         logger.log(Level.WARNING, "Problem writing server stats = " + iox.getMessage(), iox);
/*      */       }
/*      */     
/* 3963 */     } catch (IOException iox) {
/*      */       
/* 3965 */       logger.log(Level.WARNING, "Failed to open stats.html", iox);
/*      */     } finally {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 3972 */         if (output != null) {
/* 3973 */           output.close();
/*      */         }
/* 3975 */       } catch (IOException iOException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printRanks2() {
/* 3986 */     printMaxRanks();
/* 3987 */     Writer output = null;
/*      */     
/*      */     try {
/* 3990 */       String dir = Constants.webPath;
/* 3991 */       if (!dir.endsWith(File.separator))
/* 3992 */         dir = dir + File.separator; 
/* 3993 */       File aFile = new File(dir + "ranks.html");
/*      */ 
/*      */ 
/*      */       
/* 3997 */       output = new BufferedWriter(new FileWriter(aFile));
/*      */       
/* 3999 */       String start = "<TABLE class=\"gameDataTable\"><TR><TH><Name</TH><TH>Rank</TH></TR>";
/*      */ 
/*      */       
/*      */       try {
/* 4003 */         output.write("<TABLE class=\"gameDataTable\"><TR><TH><Name</TH><TH>Rank</TH></TR>");
/*      */       }
/* 4005 */       catch (IOException iox) {
/*      */         
/* 4007 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/* 4009 */       int nums = 0;
/* 4010 */       Connection dbcon = null;
/* 4011 */       PreparedStatement ps = null;
/* 4012 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 4015 */         dbcon = DbConnector.getPlayerDbCon();
/* 4016 */         ps = dbcon.prepareStatement("select RANK, NAME from PLAYERS ORDER BY RANK DESC LIMIT ?");
/* 4017 */         ps.setInt(1, 30);
/* 4018 */         rs = ps.executeQuery();
/* 4019 */         while (rs.next())
/*      */         {
/*      */           
/* 4022 */           if (nums < 10) {
/* 4023 */             output.write("<TR class=\"gameDataTopTenTR\"><TD class=\"gameDataTopTenTDName\">" + rs
/* 4024 */                 .getString("NAME") + "</TD><TD class=\"gameDataTopTenTDValue\">" + rs
/* 4025 */                 .getInt("RANK") + "</TD></TR>");
/*      */           } else {
/* 4027 */             output.write("<TR class=\"gameDataTR\"><TD class=\"gameDataTDName\">" + rs.getString("NAME") + "</TD><TD class=\"gameDataTDValue\">" + rs
/* 4028 */                 .getInt("RANK") + "</TD></TR>");
/* 4029 */           }  nums++;
/*      */         }
/*      */       
/* 4032 */       } catch (SQLException sqx) {
/*      */         
/* 4034 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 4038 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 4039 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 4041 */       output.write("</TABLE>");
/*      */     
/*      */     }
/* 4044 */     catch (IOException iox) {
/*      */       
/* 4046 */       logger.log(Level.WARNING, "Failed to close ranks.html", iox);
/*      */     } finally {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/* 4053 */         if (output != null) {
/* 4054 */           output.close();
/*      */         }
/* 4056 */       } catch (IOException iOException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Long> getFriends(long wurmid) {
/* 4064 */     Map<String, Long> toReturn = new HashMap<>();
/* 4065 */     Connection dbcon = null;
/* 4066 */     PreparedStatement ps = null;
/* 4067 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 4070 */       dbcon = DbConnector.getPlayerDbCon();
/* 4071 */       ps = dbcon.prepareStatement("select p.NAME,p.WURMID from PLAYERS p INNER JOIN FRIENDS f ON f.FRIEND=p.WURMID WHERE f.WURMID=? ORDER BY NAME");
/* 4072 */       ps.setLong(1, wurmid);
/* 4073 */       rs = ps.executeQuery();
/* 4074 */       while (rs.next())
/*      */       {
/* 4076 */         toReturn.put(rs.getString("NAME"), Long.valueOf(rs.getLong("WURMID")));
/*      */       }
/*      */     }
/* 4079 */     catch (SQLException sqx) {
/*      */       
/* 4081 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4085 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 4086 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 4088 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pruneMessages() {
/* 4093 */     Connection dbcon = null;
/* 4094 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4097 */       dbcon = DbConnector.getPlayerDbCon();
/* 4098 */       ps = dbcon.prepareStatement("DELETE FROM GMMESSAGES WHERE TIME<? AND MESSAGE NOT LIKE '<Roads> %' AND MESSAGE NOT LIKE '<System> Debug:'");
/* 4099 */       ps.setLong(1, System.currentTimeMillis() - 172800000L);
/* 4100 */       ps.executeUpdate();
/*      */     }
/* 4102 */     catch (SQLException sqx) {
/*      */       
/* 4104 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4108 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4109 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 4113 */       dbcon = DbConnector.getPlayerDbCon();
/* 4114 */       ps = dbcon.prepareStatement("DELETE FROM GMMESSAGES WHERE TIME<?");
/* 4115 */       ps.setLong(1, System.currentTimeMillis() - 604800000L);
/* 4116 */       ps.executeUpdate();
/*      */     }
/* 4118 */     catch (SQLException sqx) {
/*      */       
/* 4120 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4124 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4125 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*      */     try {
/* 4129 */       dbcon = DbConnector.getPlayerDbCon();
/* 4130 */       ps = dbcon.prepareStatement("DELETE FROM MGMTMESSAGES WHERE TIME<?");
/* 4131 */       ps.setLong(1, System.currentTimeMillis() - 86400000L);
/* 4132 */       ps.executeUpdate();
/*      */     }
/* 4134 */     catch (SQLException sqx) {
/*      */       
/* 4136 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4140 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4141 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addMgmtMessage(String sender, String message) {
/* 4147 */     Connection dbcon = null;
/* 4148 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4151 */       dbcon = DbConnector.getPlayerDbCon();
/* 4152 */       ps = dbcon.prepareStatement("INSERT INTO MGMTMESSAGES(TIME,SENDER,MESSAGE) VALUES(?,?,?)");
/* 4153 */       ps.setLong(1, System.currentTimeMillis());
/* 4154 */       ps.setString(2, sender);
/* 4155 */       ps.setString(3, message.substring(0, Math.min(message.length(), 200)));
/* 4156 */       ps.executeUpdate();
/*      */     }
/* 4158 */     catch (SQLException sqx) {
/*      */       
/* 4160 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4164 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4165 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addGmMessage(String sender, String message) {
/* 4171 */     if (!message.contains(" movement too ")) {
/*      */       
/* 4173 */       Connection dbcon = null;
/* 4174 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 4177 */         dbcon = DbConnector.getPlayerDbCon();
/* 4178 */         ps = dbcon.prepareStatement("INSERT INTO GMMESSAGES(TIME,SENDER,MESSAGE) VALUES(?,?,?)");
/* 4179 */         ps.setLong(1, System.currentTimeMillis());
/* 4180 */         ps.setString(2, sender);
/* 4181 */         ps.setString(3, message.substring(0, Math.min(message.length(), 200)));
/* 4182 */         ps.executeUpdate();
/*      */       }
/* 4184 */       catch (SQLException sqx) {
/*      */         
/* 4186 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 4190 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4191 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadAllPrivatePOIForPlayer(Player player) {
/* 4198 */     if (!player.getPrivateMapAnnotations().isEmpty()) {
/*      */       return;
/*      */     }
/* 4201 */     Connection dbcon = null;
/* 4202 */     PreparedStatement ps = null;
/* 4203 */     ResultSet rs = null;
/*      */ 
/*      */     
/*      */     try {
/* 4207 */       dbcon = DbConnector.getPlayerDbCon();
/* 4208 */       ps = dbcon.prepareStatement("SELECT * FROM MAP_ANNOTATIONS WHERE POITYPE=0 AND OWNERID=?");
/* 4209 */       ps.setLong(1, player.getWurmId());
/* 4210 */       rs = ps.executeQuery();
/* 4211 */       while (rs.next())
/*      */       {
/* 4213 */         long wid = rs.getLong("ID");
/* 4214 */         String name = rs.getString("NAME");
/* 4215 */         long position = rs.getLong("POSITION");
/* 4216 */         byte type = rs.getByte("POITYPE");
/* 4217 */         long ownerId = rs.getLong("OWNERID");
/* 4218 */         String server = rs.getString("SERVER");
/* 4219 */         byte icon = rs.getByte("ICON");
/* 4220 */         player.addMapPOI(new MapAnnotation(wid, name, type, position, ownerId, server, icon), false);
/*      */       }
/*      */     
/*      */     }
/* 4224 */     catch (SQLException sqx) {
/*      */       
/* 4226 */       logger.log(Level.WARNING, "Problem loading all private POI's - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4230 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 4231 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void loadAllArtists() {
/* 4239 */     Connection dbcon = null;
/* 4240 */     PreparedStatement ps = null;
/* 4241 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 4244 */       dbcon = DbConnector.getPlayerDbCon();
/* 4245 */       ps = dbcon.prepareStatement("SELECT * FROM ARTISTS");
/* 4246 */       rs = ps.executeQuery();
/* 4247 */       while (rs.next())
/*      */       {
/* 4249 */         long wid = rs.getLong("WURMID");
/* 4250 */         artists.put(Long.valueOf(wid), new Artist(wid, rs.getBoolean("SOUND"), rs.getBoolean("GRAPHICS")));
/*      */       }
/*      */     
/* 4253 */     } catch (SQLException sqx) {
/*      */       
/* 4255 */       logger.log(Level.WARNING, "Problem loading all artists - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4259 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 4260 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isArtist(long wurmid, boolean soundRequired, boolean graphicsRequired) {
/* 4266 */     if (!artists.containsKey(Long.valueOf(wurmid)))
/* 4267 */       return false; 
/* 4268 */     Artist artist = artists.get(Long.valueOf(wurmid));
/* 4269 */     if (soundRequired) {
/*      */       
/* 4271 */       if (artist.isSound()) {
/*      */         
/* 4273 */         if (graphicsRequired)
/* 4274 */           return artist.isGraphics(); 
/* 4275 */         return artist.isSound();
/*      */       } 
/*      */       
/* 4278 */       return false;
/*      */     } 
/* 4280 */     if (graphicsRequired) {
/*      */       
/* 4282 */       if (artist.isGraphics()) {
/*      */         
/* 4284 */         if (soundRequired)
/* 4285 */           return artist.isSound(); 
/* 4286 */         return artist.isGraphics();
/*      */       } 
/*      */       
/* 4289 */       return false;
/*      */     } 
/* 4291 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addArtist(long wurmid, boolean sound, boolean graphics) {
/* 4296 */     if (!artists.containsKey(Long.valueOf(wurmid))) {
/*      */       
/* 4298 */       Artist artist = new Artist(wurmid, sound, graphics);
/* 4299 */       artists.put(Long.valueOf(wurmid), artist);
/* 4300 */       Connection dbcon = null;
/* 4301 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 4304 */         dbcon = DbConnector.getPlayerDbCon();
/* 4305 */         ps = dbcon.prepareStatement("INSERT INTO ARTISTS (WURMID,SOUND,GRAPHICS) VALUES(?,?,?)");
/* 4306 */         ps.setLong(1, wurmid);
/* 4307 */         ps.setBoolean(2, sound);
/* 4308 */         ps.setBoolean(3, graphics);
/* 4309 */         ps.executeUpdate();
/*      */       }
/* 4311 */       catch (SQLException sqx) {
/*      */         
/* 4313 */         logger.log(Level.WARNING, "Problem adding artist with id: " + wurmid + " - " + sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 4317 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4318 */         DbConnector.returnConnection(dbcon);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4323 */       Artist artist = artists.get(Long.valueOf(wurmid));
/* 4324 */       if (artist.isSound() != sound || artist.isGraphics() != graphics) {
/*      */ 
/*      */ 
/*      */         
/* 4328 */         deleteArtist(wurmid);
/* 4329 */         addArtist(wurmid, sound, graphics);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void deleteArtist(long wurmid) {
/* 4336 */     artists.remove(Long.valueOf(wurmid));
/* 4337 */     Connection dbcon = null;
/* 4338 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4341 */       dbcon = DbConnector.getPlayerDbCon();
/* 4342 */       ps = dbcon.prepareStatement("DELETE FROM ARTISTS WHERE WURMID=?");
/* 4343 */       ps.setLong(1, wurmid);
/* 4344 */       ps.executeUpdate();
/*      */     }
/* 4346 */     catch (SQLException sqx) {
/*      */       
/* 4348 */       logger.log(Level.WARNING, "Problem deleting artist with id: " + wurmid + " - " + sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 4352 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4353 */       DbConnector.returnConnection(dbcon);
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
/*      */   public long getNumberOfKills() {
/* 4368 */     long totalPlayerKills = 0L;
/* 4369 */     for (PlayerKills pk : playerKills.values()) {
/*      */       
/* 4371 */       if (pk != null && pk.getNumberOfKills() > 0)
/*      */       {
/* 4373 */         totalPlayerKills += pk.getNumberOfKills();
/*      */       }
/*      */     } 
/* 4376 */     return totalPlayerKills;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PlayerKills getPlayerKillsFor(long wurmId) {
/* 4382 */     PlayerKills pk = playerKills.get(Long.valueOf(wurmId));
/* 4383 */     if (pk == null) {
/*      */       
/* 4385 */       pk = new PlayerKills(wurmId);
/* 4386 */       playerKills.put(Long.valueOf(wurmId), pk);
/*      */     } 
/* 4388 */     return pk;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOverKilling(long killerid, long victimid) {
/* 4393 */     PlayerKills pk = getPlayerKillsFor(killerid);
/* 4394 */     if (pk.isOverKilling(victimid)) {
/* 4395 */       return true;
/*      */     }
/* 4397 */     if (deathCount.containsKey(Long.valueOf(victimid)) && ((Short)deathCount.get(Long.valueOf(victimid))).shortValue() > 3) {
/* 4398 */       return true;
/*      */     }
/* 4400 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addKill(long killerid, long victimid, String victimName) {
/* 4405 */     PlayerKills pk = getPlayerKillsFor(killerid);
/* 4406 */     pk.addKill(victimid, victimName);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPvPDeath(long victimId) {
/* 4411 */     short currentCount = 0;
/* 4412 */     if (deathCount.containsKey(Long.valueOf(victimId))) {
/* 4413 */       currentCount = ((Short)deathCount.get(Long.valueOf(victimId))).shortValue();
/*      */     }
/* 4415 */     deathCount.put(Long.valueOf(victimId), Short.valueOf((short)(currentCount + 1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePvPDeath(long victimId) {
/* 4420 */     if (!deathCount.containsKey(Long.valueOf(victimId))) {
/*      */       return;
/*      */     }
/* 4423 */     short currentCount = ((Short)deathCount.get(Long.valueOf(victimId))).shortValue();
/* 4424 */     if (currentCount > 1) {
/* 4425 */       deathCount.put(Long.valueOf(victimId), Short.valueOf((short)(currentCount - 1)));
/*      */     } else {
/* 4427 */       deathCount.remove(Long.valueOf(victimId));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasPvpDeaths(long victimId) {
/* 4432 */     return deathCount.containsKey(Long.valueOf(victimId));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendLogoff(String reason) {
/* 4437 */     Player[] playarr = getPlayers();
/* 4438 */     for (Player lPlayer : playarr) {
/* 4439 */       lPlayer.getCommunicator().sendShutDown(reason, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public void logOffLinklessPlayers() {
/* 4444 */     Player[] playarr = getPlayers();
/* 4445 */     for (Player lPlayer : playarr) {
/*      */       
/* 4447 */       if (!lPlayer.hasLink()) {
/* 4448 */         logoutPlayer(lPlayer);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkAffinities() {
/* 4454 */     Player[] playarr = getPlayers();
/* 4455 */     for (Player lPlayer : playarr) {
/* 4456 */       lPlayer.checkAffinity();
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkElectors() {
/* 4461 */     Player[] playarr = getPlayers();
/* 4462 */     for (Player lPlayer : playarr) {
/*      */       
/* 4464 */       if (lPlayer.isAspiringKing())
/*      */         return; 
/*      */     } 
/* 4467 */     Methods.resetJennElector();
/* 4468 */     Methods.resetHotsElector();
/* 4469 */     Methods.resetMolrStone();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCRBonus(byte kingdomId) {
/* 4474 */     Float f = this.crBonuses.get(Byte.valueOf(kingdomId));
/* 4475 */     if (f != null)
/* 4476 */       return f.floatValue(); 
/* 4477 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendUpdateEpicMission(EpicMission mission) {
/* 4482 */     for (Player p : getPlayers()) {
/*      */       
/* 4484 */       if (!Servers.localServer.PVPSERVER) {
/* 4485 */         MissionPerformer.sendEpicMission(mission, p.getCommunicator());
/*      */       } else {
/* 4487 */         MissionPerformer.sendEpicMissionPvPServer(mission, (Creature)p, p.getCommunicator());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calcCRBonus() {
/* 4496 */     if (!Servers.isThisAHomeServer()) {
/*      */       
/* 4498 */       Map<Byte, Float> numPs = new HashMap<>();
/* 4499 */       float total = 0.0F;
/* 4500 */       for (Player lPlayer : getPlayers()) {
/*      */         
/* 4502 */         if (lPlayer.isPaying()) {
/*      */           
/* 4504 */           byte kingdomId = lPlayer.getKingdomId();
/* 4505 */           Float f = numPs.get(Byte.valueOf(kingdomId));
/* 4506 */           if (f == null) {
/* 4507 */             f = Float.valueOf(1.0F);
/*      */           } else {
/* 4509 */             f = Float.valueOf(f.floatValue() + 1.0F);
/* 4510 */           }  numPs.put(Byte.valueOf(kingdomId), f);
/* 4511 */           total++;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4523 */       Map<Byte, Float> alliedPs = new HashMap<>();
/* 4524 */       for (Byte b : numPs.keySet()) {
/*      */         
/* 4526 */         Float f = numPs.get(b);
/* 4527 */         alliedPs.put(b, f);
/* 4528 */         Kingdom k = Kingdoms.getKingdom(b.byteValue());
/* 4529 */         if (k != null) {
/*      */           
/* 4531 */           Map<Byte, Byte> allies = k.getAllianceMap();
/* 4532 */           for (Map.Entry<Byte, Byte> entry : allies.entrySet()) {
/*      */             
/* 4534 */             if (((Byte)entry.getValue()).byteValue() == 1) {
/*      */ 
/*      */ 
/*      */               
/* 4538 */               Float other = numPs.get(entry.getKey());
/* 4539 */               if (other != null)
/* 4540 */                 f = Float.valueOf(f.floatValue() + other.floatValue()); 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 4545 */       this.crBonuses.clear();
/*      */ 
/*      */       
/* 4548 */       if (total > 20.0F)
/*      */       {
/* 4550 */         for (Map.Entry<Byte, Float> totals : alliedPs.entrySet()) {
/*      */           
/* 4552 */           float numbers = ((Float)totals.getValue()).floatValue();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4557 */           if (numbers / total < 0.05F) {
/* 4558 */             this.crBonuses.put(totals.getKey(), Float.valueOf(2.0F)); continue;
/* 4559 */           }  if (numbers / total < 0.1F) {
/* 4560 */             this.crBonuses.put(totals.getKey(), Float.valueOf(1.0F));
/*      */           }
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
/*      */   public final void updateEigcInfo(EigcClient client) {
/* 4588 */     if (!client.getPlayerName().isEmpty()) {
/*      */       
/*      */       try {
/*      */         
/* 4592 */         Player p = getPlayer(client.getPlayerName());
/* 4593 */         p.getCommunicator().updateEigcInfo(client);
/*      */       }
/* 4595 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean existsPlayerWithIp(String ipAddress) {
/* 4605 */     for (Player p : getInstance().getPlayers()) {
/*      */ 
/*      */       
/* 4608 */       if (p.getSaveFile().getIpaddress().contains(ipAddress))
/* 4609 */         return true; 
/*      */     } 
/* 4611 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendGlobalGMMessage(Creature sender, String message) {
/* 4616 */     Message mess = new Message(sender, (byte)11, "GM", "<" + sender.getName() + "> " + message);
/*      */     
/* 4618 */     Server.getInstance().addMessage(mess);
/* 4619 */     addGmMessage(sender.getName(), message);
/* 4620 */     if (message.trim().length() > 1) {
/*      */ 
/*      */       
/* 4623 */       WCGmMessage wc = new WCGmMessage(WurmId.getNextWCCommandId(), sender.getName(), "(" + Servers.localServer.getAbbreviation() + ") " + message, false);
/* 4624 */       if (Servers.localServer.LOGINSERVER) {
/* 4625 */         wc.sendFromLoginServer();
/*      */       } else {
/* 4627 */         wc.sendToLoginServer();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void pollPlayers() {
/* 4633 */     long delta = System.currentTimeMillis() - this.lastPoll;
/* 4634 */     if ((float)delta < 0.095F) {
/*      */       return;
/*      */     }
/* 4637 */     this.lastPoll = System.currentTimeMillis();
/* 4638 */     for (Player lPlayer : getPlayers()) {
/*      */       
/* 4640 */       if (lPlayer != null) {
/* 4641 */         lPlayer.pollActions();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void sendKingdomToPlayers(Kingdom kingdom) {
/* 4647 */     for (Player lPlayer : getPlayers()) {
/*      */       
/* 4649 */       if (lPlayer.hasLink())
/*      */       {
/* 4651 */         lPlayer.getCommunicator().sendNewKingdom(kingdom.getId(), kingdom.getName(), kingdom.getSuffix());
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
/*      */   public static void tellFriends(PlayerState pState) {
/* 4663 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/* 4665 */       if (p.isFriend(pState.getPlayerId()))
/*      */       {
/* 4667 */         p.getCommunicator().sendFriend(pState);
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
/*      */   public final void sendTicket(Ticket ticket) {
/* 4679 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/* 4681 */       if (p.hasLink())
/*      */       {
/* 4683 */         if (ticket.isTicketShownTo(p))
/*      */         {
/* 4685 */           p.getCommunicator().sendTicket(ticket);
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
/*      */ 
/*      */   
/*      */   public final void sendTicket(Ticket ticket, @Nullable TicketAction ticketAction) {
/* 4700 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/* 4702 */       if (p.hasLink())
/*      */       {
/* 4704 */         if (ticket.isTicketShownTo(p) && (ticketAction == null || ticketAction
/* 4705 */           .isActionShownTo(p)))
/*      */         {
/* 4707 */           p.getCommunicator().sendTicket(ticket, ticketAction);
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
/*      */   public final void removeTicket(Ticket ticket) {
/* 4720 */     for (Player p : getInstance().getPlayers()) {
/*      */       
/* 4722 */       if (p.hasLink())
/*      */       {
/* 4724 */         if (ticket.isTicketShownTo(p))
/*      */         {
/* 4726 */           p.getCommunicator().removeTicket(ticket);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final void sendVotingOpen(VoteQuestion vq) {
/* 4733 */     for (Player p : getInstance().getPlayers())
/*      */     {
/* 4735 */       sendVotingOpen(p, vq);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sendVotingOpen(Player p, VoteQuestion vq) {
/* 4741 */     if (p.hasLink())
/*      */     {
/* 4743 */       if (vq.canVote(p))
/*      */       {
/* 4745 */         p.getCommunicator().sendServerMessage("Poll for " + vq.getQuestionTitle() + " is open, use /poll to participate.", 250, 150, 250);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMgmtMessage(Creature sender, String playerName, String message, boolean emote, boolean logit, int red, int green, int blue) {
/* 4754 */     Message mess = null;
/* 4755 */     if (emote) {
/* 4756 */       mess = new Message(sender, (byte)6, "MGMT", message);
/*      */     } else {
/*      */       
/* 4759 */       mess = new Message(sender, (byte)9, "MGMT", "<" + playerName + "> " + message, red, green, blue);
/*      */     } 
/* 4761 */     if (logit) {
/* 4762 */       addMgmtMessage(playerName, message);
/*      */     }
/* 4764 */     Player[] playerArr = getInstance().getPlayers();
/* 4765 */     for (Player lPlayer : playerArr) {
/*      */       
/* 4767 */       if (lPlayer.mayHearMgmtTalk()) {
/*      */ 
/*      */         
/* 4770 */         if (sender == null)
/* 4771 */           mess.setSender((Creature)lPlayer); 
/* 4772 */         lPlayer.getCommunicator().sendMessage(mess);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean shouldReceivePlayerList(Player player) {
/* 4779 */     return (player.getKingdomId() == 4 && (player
/* 4780 */       .isPlayerAssistant() || player.mayMute() || player.mayHearDevTalk() || player.getPower() > 0));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean getPollCheckClients() {
/* 4785 */     return pollCheckClients;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setPollCheckClients(boolean doit) {
/* 4790 */     pollCheckClients = doit;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void appointCA(Creature performer, String targetName) {
/* 4795 */     Player playerPerformer = null;
/*      */     
/* 4797 */     if (performer instanceof Player) {
/* 4798 */       playerPerformer = (Player)performer;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 4802 */     if (playerPerformer.mayAppointPlayerAssistant()) {
/*      */       
/* 4804 */       String pname = targetName;
/* 4805 */       pname = LoginHandler.raiseFirstLetter(pname);
/*      */       
/* 4807 */       Player p = null;
/*      */       
/*      */       try {
/* 4810 */         p = getInstance().getPlayer(pname);
/*      */       }
/* 4812 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 4814 */         playerPerformer.getCommunicator().sendNormalServerMessage("No player online with the name " + pname);
/*      */       } 
/* 4816 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(pname);
/*      */       
/*      */       try {
/* 4819 */         pinf.load();
/* 4820 */       } catch (IOException e) {
/*      */         
/* 4822 */         performer.getCommunicator().sendAlertServerMessage("This player does not exist.");
/*      */         
/*      */         return;
/*      */       } 
/* 4826 */       if (pinf.wurmId > 0L) {
/*      */         
/* 4828 */         if (pinf.isPlayerAssistant())
/*      */         {
/* 4830 */           pinf.setIsPlayerAssistant(false);
/* 4831 */           if (p != null) {
/* 4832 */             p.getCommunicator().sendAlertServerMessage("You no longer have the duties of a community assistant.", (byte)1);
/*      */           }
/* 4834 */           playerPerformer.getCommunicator().sendSafeServerMessage(pname + " no longer has the duties of being a community assistant.", (byte)1);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4839 */           WcDemotion wc = new WcDemotion(WurmId.getNextWCCommandId(), playerPerformer.getWurmId(), pinf.wurmId, (short)1);
/* 4840 */           wc.sendToLoginServer();
/*      */         }
/*      */         else
/*      */         {
/* 4844 */           if (p != null) {
/*      */             
/* 4846 */             p.setPlayerAssistant(true);
/* 4847 */             p.togglePlayerAssistantWindow(true);
/*      */             
/* 4849 */             p.getCommunicator().sendSafeServerMessage("You are now a Community Assistant and receives a CA window.");
/*      */             
/* 4851 */             p.getCommunicator().sendSafeServerMessage("New players will also receive that and may ask you questions.");
/*      */             
/* 4853 */             p.getCommunicator()
/* 4854 */               .sendSafeServerMessage("The suggested way to approach new players is not to approach them directly");
/*      */             
/* 4856 */             p.getCommunicator()
/* 4857 */               .sendSafeServerMessage("but instead let them ask questions. Otherwise many of them may become deterred");
/*      */             
/* 4859 */             p.getCommunicator()
/* 4860 */               .sendSafeServerMessage("since this may be an early online experience or they have poor english knowledge.");
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 4865 */             pinf.setIsPlayerAssistant(true);
/* 4866 */             pinf.togglePlayerAssistantWindow(true);
/* 4867 */             playerPerformer.getCommunicator().sendAlertServerMessage(pname + " needs to be online in order to receive the title.", (byte)2);
/*      */           } 
/* 4869 */           playerPerformer.getCommunicator().sendSafeServerMessage(pname + " is now appointed Community Assistant.", (byte)1);
/* 4870 */           if (playerPerformer.getLogger() != null)
/* 4871 */             playerPerformer.getLogger().log(Level.INFO, playerPerformer
/* 4872 */                 .getName() + " appoints " + pname + " community assistant."); 
/* 4873 */           logger.log(Level.INFO, playerPerformer.getName() + " appoints " + pname + " as community assistant.");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4878 */         playerPerformer.getCommunicator().sendNormalServerMessage("No player found with the name " + pname);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void appointCM(Creature performer, String targetName) {
/* 4884 */     if (performer.getPower() >= 1) {
/*      */       
/* 4886 */       String pname = targetName;
/* 4887 */       pname = LoginHandler.raiseFirstLetter(pname);
/*      */       
/* 4889 */       Player p = null;
/*      */       
/*      */       try {
/* 4892 */         p = getInstance().getPlayer(pname);
/*      */       }
/* 4894 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 4896 */         performer.getCommunicator().sendNormalServerMessage("No player online with the name " + pname);
/*      */       } 
/* 4898 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(pname);
/*      */       
/*      */       try {
/* 4901 */         pinf.load();
/* 4902 */       } catch (IOException e) {
/*      */         
/* 4904 */         performer.getCommunicator().sendAlertServerMessage("This player does not exist.");
/*      */         
/*      */         return;
/*      */       } 
/* 4908 */       if (pinf.wurmId > 0L) {
/*      */         
/* 4910 */         if (pinf.getPower() == 0) {
/*      */           
/* 4912 */           if (pinf.mayMute)
/*      */           {
/* 4914 */             pinf.setMayMute(false);
/* 4915 */             if (p != null) {
/* 4916 */               p.getCommunicator().sendAlertServerMessage("You may no longer mute other players.", (byte)1);
/*      */             }
/* 4918 */             performer.getCommunicator().sendSafeServerMessage(pname + " may no longer mute other players.");
/*      */ 
/*      */ 
/*      */             
/* 4922 */             WcDemotion wc = new WcDemotion(WurmId.getNextWCCommandId(), performer.getWurmId(), pinf.wurmId, (short)2);
/* 4923 */             wc.sendToLoginServer();
/*      */           }
/*      */           else
/*      */           {
/* 4927 */             pinf.setMayMute(true);
/* 4928 */             if (p != null) {
/*      */               
/* 4930 */               p.getCommunicator()
/* 4931 */                 .sendSafeServerMessage("You may now mute other players. Use this with extreme care and wise judgement.");
/*      */               
/* 4933 */               p.getCommunicator().sendSafeServerMessage("The syntax is #mute <playername> <number of hours> <reason>");
/*      */               
/* 4935 */               p.getCommunicator().sendSafeServerMessage("For example: #mute unforgiven 6 foul language");
/*      */               
/* 4937 */               p.getCommunicator().sendSafeServerMessage("To unmute a player, use #unmute <playername>");
/*      */               
/* 4939 */               p.getCommunicator().sendSafeServerMessage("You may see who are muted with the command #showmuted");
/*      */             } 
/*      */             
/* 4942 */             performer.getCommunicator().sendSafeServerMessage(pname + " may now mute other players.", (byte)1);
/* 4943 */             if (performer.getLogger() != null)
/* 4944 */               performer.getLogger().log(Level.INFO, performer
/* 4945 */                   .getName() + " allows " + pname + " to mute other players."); 
/* 4946 */             logger.log(Level.INFO, performer.getName() + " allows " + pname + " to mute other players.");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 4951 */           performer.getCommunicator().sendNormalServerMessage(pinf.getName() + " may already mute, because he is a Hero or higher.");
/*      */         } 
/*      */       } else {
/* 4954 */         performer.getCommunicator().sendNormalServerMessage("No player found with the name " + pname);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void displayLCMInfo(Creature performer, String targetName) {
/* 4960 */     if (performer == null || !performer.hasLink()) {
/*      */       return;
/*      */     }
/* 4963 */     if (performer.getPower() >= 1) {
/*      */       
/*      */       try {
/*      */         
/* 4967 */         PlayerInfo targetInfo = PlayerInfoFactory.createPlayerInfo(targetName);
/* 4968 */         targetInfo.load();
/* 4969 */         PlayerState targetState = PlayerInfoFactory.getPlayerState(targetInfo.wurmId);
/*      */         
/* 4971 */         Logger logger = performer.getLogger();
/* 4972 */         if (logger != null)
/*      */         {
/* 4974 */           logger.log(Level.INFO, performer.getName() + " tried to view the info of " + targetInfo.getName());
/*      */         }
/*      */         
/* 4977 */         if (performer.getPower() < targetInfo.getPower()) {
/*      */           
/* 4979 */           performer.getCommunicator().sendSafeServerMessage("You can't just look at the information of higher ranking staff members!");
/*      */           
/*      */           return;
/*      */         } 
/* 4983 */         String email = targetInfo.emailAddress;
/* 4984 */         String ip = targetInfo.getIpaddress();
/* 4985 */         String lastLogout = (new Date(targetState.getLastLogout())).toString();
/* 4986 */         String timePlayed = Server.getTimeFor(targetInfo.playingTime);
/* 4987 */         String CAInfo = targetInfo.getName() + " is " + (targetInfo.isPlayerAssistant() ? "a CA." : "not a CA.");
/* 4988 */         String CMInfo = targetInfo.getName() + " is " + (targetInfo.mayMute ? "a CM." : "not a CM.");
/*      */         
/* 4990 */         performer.getCommunicator().sendNormalServerMessage("Information about " + targetInfo.getName());
/* 4991 */         performer.getCommunicator().sendNormalServerMessage("-----");
/* 4992 */         performer.getCommunicator().sendNormalServerMessage("Email address: " + email);
/* 4993 */         performer.getCommunicator().sendNormalServerMessage("IP address: " + ip);
/* 4994 */         performer.getCommunicator().sendNormalServerMessage("Last logout: " + lastLogout);
/* 4995 */         performer.getCommunicator().sendNormalServerMessage("Time played: " + timePlayed);
/* 4996 */         performer.getCommunicator().sendNormalServerMessage(CAInfo);
/* 4997 */         performer.getCommunicator().sendNormalServerMessage(CMInfo);
/* 4998 */         performer.getCommunicator().sendNormalServerMessage("-----");
/*      */       }
/* 5000 */       catch (Exception e) {
/*      */         
/* 5002 */         performer.getCommunicator().sendAlertServerMessage("This player does not exist.");
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
/*      */   public void updateTabs(byte tab, TabData tabData) {
/* 5016 */     if (tab == 2) {
/*      */ 
/*      */       
/* 5019 */       removeFromTabs(tabData.getWurmId(), tabData.getName());
/*      */     }
/* 5021 */     else if (tab == 0) {
/*      */ 
/*      */       
/* 5024 */       tabListGM.put(tabData.getName(), tabData);
/* 5025 */       for (Player player : getInstance().getPlayers()) {
/*      */         
/* 5027 */         if (player.mayHearDevTalk())
/*      */         {
/* 5029 */           if (tabData.isVisible() || tabData.getPower() <= player.getPower()) {
/* 5030 */             player.getCommunicator().sendAddGm(tabData.getName(), tabData.getWurmId());
/*      */           } else {
/* 5032 */             player.getCommunicator().sendRemoveGm(tabData.getName());
/*      */           } 
/*      */         }
/*      */       } 
/* 5036 */     } else if (tab == 1) {
/*      */ 
/*      */       
/* 5039 */       tabListMGMT.put(tabData.getName(), tabData);
/* 5040 */       for (Player player : getInstance().getPlayers()) {
/*      */         
/* 5042 */         if (player.mayHearMgmtTalk())
/*      */         {
/* 5044 */           if (tabData.isVisible()) {
/* 5045 */             player.getCommunicator().sendAddMgmt(tabData.getName(), tabData.getWurmId());
/*      */           } else {
/* 5047 */             player.getCommunicator().sendRemoveMgmt(tabData.getName());
/*      */           } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendToTabs(Player player, boolean showMe, boolean justGM) {
/* 5066 */     if (player.getPower() >= 2 || player.mayHearDevTalk()) {
/*      */       
/* 5068 */       boolean sendGM = false;
/* 5069 */       TabData tabData = tabListGM.get(player.getName());
/* 5070 */       if (tabData == null) {
/*      */ 
/*      */         
/* 5073 */         tabData = new TabData(player.getWurmId(), player.getName(), (byte)player.getPower(), (showMe || player.getPower() < 2));
/* 5074 */         sendGM = true;
/*      */       }
/* 5076 */       else if (tabData.isVisible() != showMe && player.getPower() >= 2) {
/*      */         
/* 5078 */         tabData = new TabData(player.getWurmId(), player.getName(), (byte)player.getPower(), showMe);
/* 5079 */         sendGM = true;
/*      */       } 
/* 5081 */       if (sendGM) {
/*      */         
/* 5083 */         updateTabs((byte)0, tabData);
/* 5084 */         WcTabLists wtl = new WcTabLists((byte)0, tabData);
/* 5085 */         if (Servers.isThisLoginServer()) {
/* 5086 */           wtl.sendFromLoginServer();
/*      */         } else {
/* 5088 */           wtl.sendToLoginServer();
/*      */         } 
/*      */       } 
/* 5091 */     }  if (!justGM) {
/*      */       
/* 5093 */       boolean sendMGMT = false;
/* 5094 */       TabData tabData = tabListMGMT.get(player.getName());
/* 5095 */       if (tabData == null) {
/*      */ 
/*      */ 
/*      */         
/* 5099 */         tabData = new TabData(player.getWurmId(), player.getName(), (byte)player.getPower(), (showMe || player.getPower() < 2));
/* 5100 */         sendMGMT = true;
/*      */       }
/* 5102 */       else if (tabData.isVisible() != showMe && player.getPower() >= 2) {
/*      */         
/* 5104 */         tabData = new TabData(player.getWurmId(), player.getName(), (byte)player.getPower(), showMe);
/* 5105 */         sendMGMT = true;
/*      */       } 
/* 5107 */       if (sendMGMT) {
/*      */         
/* 5109 */         updateTabs((byte)1, tabData);
/* 5110 */         WcTabLists wtl = new WcTabLists((byte)1, tabData);
/* 5111 */         if (Servers.isThisLoginServer()) {
/* 5112 */           wtl.sendFromLoginServer();
/*      */         } else {
/* 5114 */           wtl.sendToLoginServer();
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
/*      */   
/*      */   public void removeFromTabs(long wurmId, String name) {
/* 5128 */     TabData oldGMTabData = tabListGM.remove(name);
/* 5129 */     TabData oldMGMTTabData = tabListMGMT.remove(name);
/*      */     
/* 5131 */     if (oldGMTabData != null || oldMGMTTabData != null)
/*      */     {
/* 5133 */       for (Player player : getInstance().getPlayers()) {
/*      */         
/* 5135 */         if (oldGMTabData != null && player.mayHearDevTalk())
/*      */         {
/* 5137 */           player.getCommunicator().sendRemoveGm(name);
/*      */         }
/* 5139 */         if (oldMGMTTabData != null && player.mayHearMgmtTalk())
/*      */         {
/* 5141 */           player.getCommunicator().sendRemoveMgmt(name);
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
/*      */ 
/*      */   
/*      */   public void sendRemoveFromTabs(long wurmId, String name) {
/* 5156 */     TabData tabData = new TabData(wurmId, name, (byte)0, false);
/* 5157 */     if (tabData != null) {
/*      */       
/* 5159 */       WcTabLists wtl = new WcTabLists((byte)2, tabData);
/* 5160 */       if (Servers.isThisLoginServer()) {
/* 5161 */         wtl.sendFromLoginServer();
/*      */       } else {
/* 5163 */         wtl.sendToLoginServer();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Players.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */