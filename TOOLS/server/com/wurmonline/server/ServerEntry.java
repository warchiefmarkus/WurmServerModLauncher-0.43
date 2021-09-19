/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.epic.EpicEntity;
/*      */ import com.wurmonline.server.intra.IntraClient;
/*      */ import com.wurmonline.server.intra.IntraServerConnectionListener;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.Spawnpoint;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WcEpicStatusReport;
/*      */ import com.wurmonline.server.webinterface.WcKingdomInfo;
/*      */ import com.wurmonline.server.webinterface.WcPlayerStatus;
/*      */ import com.wurmonline.server.webinterface.WcSpawnPoints;
/*      */ import com.wurmonline.server.webinterface.WcTicket;
/*      */ import com.wurmonline.server.webinterface.WebCommand;
/*      */ import com.wurmonline.server.zones.TilePoller;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ServerEntry
/*      */   implements MiscConstants, IntraServerConnectionListener, TimeConstants, Comparable<ServerEntry>, MonetaryConstants
/*      */ {
/*      */   public int id;
/*      */   public int SPAWNPOINTJENNX;
/*      */   public int SPAWNPOINTJENNY;
/*      */   public int SPAWNPOINTMOLX;
/*      */   public int SPAWNPOINTMOLY;
/*      */   public int SPAWNPOINTLIBX;
/*      */   public int SPAWNPOINTLIBY;
/*      */   public boolean HOMESERVER;
/*      */   public boolean PVPSERVER;
/*      */   public boolean EPIC = false;
/*   78 */   public String INTRASERVERADDRESS = "";
/*      */   
/*   80 */   public String INTRASERVERPORT = "";
/*      */   
/*   82 */   public int RMI_PORT = 7220;
/*      */   
/*   84 */   public int REGISTRATION_PORT = 7221;
/*      */   
/*      */   public ServerEntry serverNorth;
/*      */   
/*      */   public ServerEntry serverEast;
/*      */   
/*      */   public ServerEntry serverSouth;
/*      */   
/*      */   public ServerEntry serverWest;
/*   93 */   public String INTRASERVERPASSWORD = "";
/*      */   
/*      */   public boolean testServer = false;
/*      */   
/*   97 */   public String name = "Unknown";
/*   98 */   public String mapname = "";
/*   99 */   public String EXTERNALIP = "";
/*      */   
/*      */   public boolean LOGINSERVER = false;
/*      */   
/*  103 */   public String EXTERNALPORT = "";
/*  104 */   public String STEAMQUERYPORT = "";
/*      */   
/*  106 */   private byte[] externalIpBytes = null;
/*      */   
/*  108 */   private byte[] internalIpBytes = null;
/*      */   
/*  110 */   public byte KINGDOM = 0;
/*      */ 
/*      */   
/*      */   public boolean challengeServer = false;
/*      */ 
/*      */   
/*      */   public boolean entryServer = false;
/*      */ 
/*      */   
/*      */   public boolean ISPAYMENT = false;
/*      */ 
/*      */   
/*      */   protected boolean isAvailable = false;
/*      */   
/*  124 */   private static final Logger logger = Logger.getLogger(ServerEntry.class.getName());
/*      */   
/*      */   private boolean done;
/*      */   
/*      */   private IntraClient client;
/*      */   
/*      */   private long timeOutAt;
/*      */   
/*  132 */   private long timeOutTime = 20000L;
/*      */   
/*      */   private long startTime;
/*      */   
/*  136 */   private long lastPing = 0L;
/*      */   
/*  138 */   public long lastDecreasedChampionPoints = 0L;
/*      */   
/*  140 */   private final Set<Byte> existingKingdoms = new HashSet<>();
/*      */   
/*  142 */   private float skillGainRate = 1.0F;
/*  143 */   private float actionTimer = 1.0F;
/*  144 */   private int hotaDelay = 2160;
/*  145 */   private float combatRatingModifier = 1.0F;
/*      */ 
/*      */   
/*  148 */   String consumerKeyToUse = "";
/*      */ 
/*      */   
/*  151 */   String consumerSecretToUse = "";
/*      */ 
/*      */   
/*  154 */   String applicationToken = "";
/*      */ 
/*      */   
/*  157 */   String applicationSecret = "";
/*      */ 
/*      */   
/*  160 */   String champConsumerKeyToUse = "";
/*      */ 
/*      */   
/*  163 */   String champConsumerSecretToUse = "";
/*      */ 
/*      */   
/*  166 */   String champApplicationToken = "";
/*      */ 
/*      */   
/*  169 */   String champApplicationSecret = "";
/*      */ 
/*      */   
/*      */   private static final String SET_CHAMP_TWITTER = "UPDATE SERVERS SET CHAMPTWITKEY=?,CHAMPTWITSECRET=?,CHAMPTWITAPP=?,CHAMPTWITAPPSECRET=? WHERE SERVER=?";
/*      */ 
/*      */   
/*      */   private static final String SET_TWITTER = "UPDATE SERVERS SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=? WHERE SERVER=?";
/*      */ 
/*      */   
/*      */   private static final int PLAYER_LIMIT_MARGIN = 10;
/*      */   
/*      */   private boolean canTwit = false;
/*      */   
/*      */   public boolean canTwitChamps = false;
/*      */   
/*      */   public boolean maintaining = false;
/*      */   
/*  186 */   public int pLimit = 200;
/*      */   
/*      */   public boolean playerLimitOverridable = true;
/*  189 */   public int maxCreatures = 1000;
/*      */   
/*  191 */   public int maxTypedCreatures = 250;
/*  192 */   public float percentAggCreatures = 10.0F;
/*  193 */   public int treeGrowth = 20;
/*  194 */   public int currentPlayers = 0;
/*      */   
/*  196 */   public int isShuttingDownIn = 0;
/*      */   
/*      */   public boolean loggingIn = false;
/*      */   
/*      */   private static final String SET_CHAMPSTAMP = "UPDATE SERVERS SET LASTRESETCHAMPS=? WHERE SERVER=?";
/*      */   
/*      */   public boolean isLocal = false;
/*      */   
/*      */   public boolean reloading = false;
/*      */   
/*  206 */   public int meshSize = Constants.meshSize;
/*      */ 
/*      */   
/*      */   public boolean shouldResendKingdoms = false;
/*      */ 
/*      */   
/*  212 */   private long movedArtifacts = System.currentTimeMillis();
/*      */   
/*      */   private static final String SET_MOVEDARTIFACTS = "UPDATE SERVERS SET MOVEDARTIS=? WHERE SERVER=?";
/*      */   
/*      */   private static final String SET_SPAWNEDUNIQUE = "UPDATE SERVERS SET SPAWNEDUNIQUE=? WHERE SERVER=?";
/*      */   
/*      */   private static final String MOVEPLAYERS = "UPDATE PLAYERS SET CURRENTSERVER=? WHERE CURRENTSERVER=?";
/*      */   private static final String UPDATE_CAHELPGROUP = "UPDATE SERVERS SET CAHELPGROUP=? WHERE SERVER=?";
/*      */   private static final String UPDATE_CHALLENGETIMES = "UPDATE SERVERS SET CHALLENGESTARTED=?, CHALLENGEEND=? WHERE SERVER=?";
/*      */   public static final String SET_TIMERS = "UPDATE SERVERS SET SKILLDAYSWITCH=?,SKILLWEEKSWITCH=?,NEXTEPICPOLL=?,FATIGUESWITCH=?,NEXTHOTA=?,WORLDTIME=?,TILEREST=?,POLLTILE=?,POLLMOD=?,POLLROUND=? WHERE SERVER=?";
/*  222 */   private long skillDaySwitch = 0L;
/*  223 */   private long skillWeekSwitch = 0L;
/*  224 */   private long nextEpicPoll = 0L;
/*  225 */   private long fatigueSwitch = 0L;
/*      */   
/*  227 */   private long lastSpawnedUnique = 0L;
/*      */ 
/*      */   
/*  230 */   private long nextHota = 0L;
/*      */   
/*      */   private Spawnpoint[] spawns;
/*      */   
/*  234 */   private byte caHelpGroup = -1;
/*      */   
/*  236 */   private long challengeStarted = 0L;
/*  237 */   private long challengeEnds = 0L;
/*      */   
/*      */   public boolean isCreating = false;
/*      */   
/*      */   public boolean randomSpawns = false;
/*      */   public static final String UPDATE_SERVER_NEW = "UPDATE SERVERS SET SERVER=?,NAME=?,MAXCREATURES=?,MAXPLAYERS=?,PERCENT_AGG_CREATURES=?,TREEGROWTH=?,SKILLGAINRATE=?,ACTIONTIMER=?,HOTADELAY=?,PVP=?,          HOMESERVER=?,KINGDOM=?,INTRASERVERPASSWORD=?,EXTERNALIP=?,EXTERNALPORT=?,INTRASERVERADDRESS=?,INTRASERVERPORT=?,ISTEST=?,ISPAYMENT=?,LOGINSERVER=?,           RMIPORT=?,REGISTRATIONPORT=?,LOCAL=?,RANDOMSPAWNS=?,SKILLBASICSTART=?,SKILLMINDLOGICSTART=?,SKILLFIGHTINGSTART=?,SKILLOVERALLSTART=?,EPIC=?,CRMOD=?,            STEAMPW=?,UPKEEP=?,MAXDEED=?,FREEDEEDS=?,TRADERMAX=?,TRADERINIT=?,BREEDING=?,FIELDGROWTH=?,KINGSMONEY=?, MOTD=?,     TUNNELING=?,SKILLBODYCONTROLSTART=? WHERE SERVER=?";
/*  243 */   private float skillbasicval = 20.0F;
/*  244 */   private float skillmindval = 20.0F;
/*  245 */   private float skillfightval = 1.0F;
/*  246 */   private float skilloverallval = 1.0F;
/*  247 */   private float skillbcval = 20.0F;
/*      */   
/*  249 */   private String steamServerPassword = "";
/*      */   private boolean upkeep = true;
/*  251 */   private int maxDeedSize = 0;
/*      */   private boolean freeDeeds = false;
/*  253 */   private int traderMaxIrons = 500000;
/*  254 */   private int initialTraderIrons = 10000;
/*  255 */   private int tunnelingHits = 51;
/*  256 */   private long breedingTimer = 0L;
/*  257 */   private long fieldGrowthTime = 86400000L;
/*  258 */   private int kingsmoneyAtRestart = 0;
/*  259 */   private String motd = "";
/*      */   
/*  261 */   public String adminPassword = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int pingcounter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canTwit() {
/*  329 */     if (this.consumerKeyToUse != null && this.consumerKeyToUse.length() > 5 && this.consumerSecretToUse != null && this.consumerSecretToUse
/*  330 */       .length() > 5 && this.applicationToken != null && this.applicationToken.length() > 5 && this.applicationSecret != null && this.applicationSecret
/*  331 */       .length() > 5) {
/*      */       
/*  333 */       this.canTwit = true;
/*      */     }
/*      */     else {
/*      */       
/*  337 */       this.canTwit = false;
/*      */     } 
/*  339 */     if (this.champConsumerKeyToUse != null && this.champConsumerKeyToUse.length() > 5 && this.champConsumerSecretToUse != null && this.champConsumerSecretToUse
/*  340 */       .length() > 5 && this.champApplicationToken != null && this.champApplicationToken.length() > 5 && this.champApplicationSecret != null && this.champApplicationSecret
/*  341 */       .length() > 5) {
/*      */       
/*  343 */       this.canTwitChamps = true;
/*      */     }
/*      */     else {
/*      */       
/*  347 */       this.canTwitChamps = false;
/*      */     } 
/*  349 */     return this.canTwit;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isChaosServer() {
/*  354 */     return (this.id == 3 || (this.testServer && this.PVPSERVER && Features.Feature.CHAOS.isEnabled()));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isChallengeServer() {
/*  359 */     return this.challengeServer;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isChallengeOrEpicServer() {
/*  364 */     return (this.challengeServer || this.EPIC);
/*      */   }
/*      */ 
/*      */   
/*      */   public Twit createTwit(String message) {
/*  369 */     if (this.canTwit)
/*  370 */       return new Twit(this.name, message, this.consumerKeyToUse, this.consumerSecretToUse, this.applicationToken, this.applicationSecret, false); 
/*  371 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void createChampTwit(String message) {
/*  376 */     if (this.canTwitChamps) {
/*      */       
/*  378 */       Twit t = new Twit(this.name, message, this.champConsumerKeyToUse, this.champConsumerSecretToUse, this.champApplicationToken, this.champApplicationSecret, false);
/*      */       
/*  380 */       if (t != null) {
/*  381 */         Twit.twit(t);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public byte[] getExternalIpAsBytes() {
/*  387 */     if (this.externalIpBytes == null) {
/*      */       
/*  389 */       this.externalIpBytes = new byte[4];
/*  390 */       StringTokenizer tokens = new StringTokenizer(this.EXTERNALIP);
/*  391 */       int x = 0;
/*  392 */       while (tokens.hasMoreTokens()) {
/*      */         
/*  394 */         String next = tokens.nextToken();
/*  395 */         this.externalIpBytes[x] = Integer.valueOf(next).byteValue();
/*      */ 
/*      */         
/*  398 */         x++;
/*      */       } 
/*      */     } 
/*  401 */     return this.externalIpBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAvailable(boolean available, boolean maintain, int currentPlayerCount, int plimit, int secsToShutdown, int mSize) {
/*  411 */     this.pLimit = plimit;
/*  412 */     this.currentPlayers = currentPlayerCount;
/*  413 */     this.isShuttingDownIn = secsToShutdown;
/*  414 */     this.meshSize = mSize;
/*  415 */     this.maintaining = maintain;
/*      */ 
/*      */     
/*  418 */     if (available != this.isAvailable) {
/*      */       
/*  420 */       this.isAvailable = available;
/*  421 */       if (available) {
/*      */         
/*  423 */         logger.log(Level.INFO, this.name + " is now available.");
/*  424 */         final int serverId = getId();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  435 */         (new Thread("ServerEntry.setAvailable-Thread")
/*      */           {
/*      */             
/*      */             public void run()
/*      */             {
/*  440 */               long now = System.nanoTime();
/*  441 */               if (ServerEntry.logger.isLoggable(Level.FINE))
/*      */               {
/*  443 */                 ServerEntry.logger.fine("Starting ServerEntry.setAvailable() thread");
/*      */               }
/*  445 */               ServerEntry.this.sendKingdomInfo();
/*  446 */               ServerEntry.this.setupPlayerStates();
/*  447 */               ServerEntry.this.sendSpawnpoints();
/*  448 */               if (Servers.localServer.LOGINSERVER)
/*      */               {
/*  450 */                 for (EpicEntity entity : Server.getEpicMap().getAllEntities()) {
/*      */                   
/*  452 */                   if (entity.isDeity())
/*      */                   {
/*  454 */                     entity.checkifServerFailed(serverId);
/*      */                   }
/*      */                 } 
/*      */               }
/*  458 */               if (ServerEntry.logger.isLoggable(Level.FINE))
/*      */               {
/*  460 */                 float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/*  461 */                 ServerEntry.logger.fine("Finished ServerEntry.setAvailable() thread. That took " + lElapsedTime + " millis.");
/*      */               }
/*      */             
/*      */             }
/*  465 */           }).start();
/*      */       }
/*      */       else {
/*      */         
/*  469 */         logger.log(Level.INFO, this.name + " is no longer available.");
/*      */ 
/*      */         
/*  472 */         PlayerInfoFactory.setPlayerStatesToOffline(this.id);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendSpawnpoints() {
/*  479 */     if (Servers.getLocalServerId() != this.id) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  484 */       Set<Spawnpoint> lSpawns = new HashSet<>();
/*  485 */       for (Kingdom kingdom : Kingdoms.getAllKingdoms()) {
/*      */         
/*  487 */         Village[] villages = Villages.getPermanentVillages(kingdom.getId());
/*  488 */         if (villages.length > 0)
/*      */         {
/*  490 */           for (Village vill : villages) {
/*      */             
/*  492 */             String toSend = vill.getMotto();
/*  493 */             if (Servers.localServer.isChallengeServer())
/*      */             {
/*  495 */               if (vill.getId() == 1 || vill.getId() == 7 || vill.getId() == 9) {
/*  496 */                 toSend = "Forward Base, for experienced players";
/*      */               } else {
/*  498 */                 toSend = "Far Base, for new players";
/*      */               } 
/*      */             }
/*  501 */             lSpawns.add(new Spawnpoint(vill.getName(), (byte)1, toSend, (short)vill.getTokenX(), 
/*  502 */                   (short)vill.getTokenY(), true, vill.kingdom));
/*      */           } 
/*      */         }
/*      */       } 
/*  506 */       if (lSpawns.size() > 0) {
/*      */         
/*  508 */         WcSpawnPoints wcp = new WcSpawnPoints(WurmId.getNextWCCommandId());
/*  509 */         wcp.setSpawns(lSpawns.<Spawnpoint>toArray(new Spawnpoint[lSpawns.size()]));
/*  510 */         wcp.sendToServer(this.id);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAvailable(int power, boolean isPremium) {
/*  521 */     if (Servers.getLocalServerId() == this.id && (!this.maintaining || power > 0))
/*  522 */       return true; 
/*  523 */     if (!this.isAvailable)
/*  524 */       return false; 
/*  525 */     if (this.maintaining && power <= 0)
/*  526 */       return false; 
/*  527 */     if (isFull() && !isPremium && power <= 0)
/*  528 */       return false; 
/*  529 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFull() {
/*  534 */     return (this.currentPlayers >= this.pLimit - 10);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isConnected() {
/*  539 */     return this.isAvailable;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getInternalIpAsBytes() {
/*  544 */     if (this.internalIpBytes == null) {
/*      */       
/*  546 */       this.internalIpBytes = new byte[4];
/*  547 */       StringTokenizer tokens = new StringTokenizer(this.INTRASERVERADDRESS);
/*  548 */       int x = 0;
/*  549 */       while (tokens.hasMoreTokens()) {
/*      */         
/*  551 */         String next = tokens.nextToken();
/*  552 */         this.internalIpBytes[x] = Integer.valueOf(next).byteValue();
/*      */ 
/*      */         
/*  555 */         x++;
/*      */       } 
/*      */     } 
/*  558 */     return this.internalIpBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void sendKingdomInfo() {
/*  563 */     LoginServerWebConnection lsw = new LoginServerWebConnection(this.id);
/*  564 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/*  566 */       lsw.setWeather(Server.getWeather().getWindRotation(), Server.getWeather().getWindPower(), Server.getWeather()
/*  567 */           .getWindDir());
/*  568 */       WcKingdomInfo wc = new WcKingdomInfo(WurmId.getNextWCCommandId(), false, (byte)0);
/*  569 */       wc.encode();
/*  570 */       lsw.sendWebCommand((short)7, (WebCommand)wc);
/*  571 */       WcEpicStatusReport report = new WcEpicStatusReport(WurmId.getNextWCCommandId(), false, 0, (byte)-1, -1);
/*  572 */       report.fillStatusReport(Server.getEpicMap());
/*  573 */       report.sendToServer(this.id);
/*      */     } 
/*  575 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/*  576 */     for (Kingdom k : kingdoms) {
/*      */       
/*  578 */       if (k.existsHere()) {
/*  579 */         lsw.kingdomExists(Servers.getLocalServerId(), k.getId(), true);
/*  580 */       } else if (logger.isLoggable(Level.FINER)) {
/*      */         
/*  582 */         logger.log(Level.FINER, k.getName() + " doesn't exist here");
/*      */       } 
/*      */     } 
/*  585 */     this.shouldResendKingdoms = false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void setupPlayerStates() {
/*  590 */     if (Servers.isThisLoginServer() && this.id != Servers.loginServer.id) {
/*      */ 
/*      */ 
/*      */       
/*  594 */       WcPlayerStatus wps = new WcPlayerStatus();
/*  595 */       wps.sendToServer(this.id);
/*      */ 
/*      */ 
/*      */       
/*  599 */       WcTicket wt = new WcTicket(Tickets.getLatestActionDate());
/*  600 */       wt.sendToServer(this.id);
/*      */     }
/*  602 */     else if (!Servers.isThisLoginServer() && this.id == Servers.loginServer.id) {
/*      */ 
/*      */ 
/*      */       
/*  606 */       Tickets.checkBatchNos();
/*      */       
/*  608 */       PlayerInfoFactory.grabPlayerStates();
/*      */       
/*  610 */       WcTicket wt = new WcTicket(Tickets.getLatestActionDate());
/*  611 */       wt.sendToServer(this.id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean poll() {
/*  617 */     if (this.id == Servers.getLocalServerId()) {
/*      */       
/*  619 */       this.isAvailable = true;
/*  620 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  624 */     if (this.client != null)
/*      */     {
/*  626 */       if (this.client.hasFailedConnection) {
/*      */         
/*  628 */         setAvailable(false, false, 0, 0, 0, 10);
/*  629 */         if (this.client != null)
/*  630 */           this.client.disconnect("Failed."); 
/*  631 */         this.client = null;
/*  632 */         this.done = true;
/*  633 */         this.loggingIn = false;
/*      */       }
/*  635 */       else if (!this.client.isConnecting && !this.client.loggedIn && !this.loggingIn) {
/*      */ 
/*      */         
/*  638 */         this.loggingIn = true;
/*  639 */         this.client.login(this.INTRASERVERPASSWORD, true);
/*      */       } 
/*      */     }
/*  642 */     if (this.client == null && System.currentTimeMillis() > this.timeOutAt) {
/*      */ 
/*      */       
/*  645 */       this.startTime = System.currentTimeMillis();
/*  646 */       this.timeOutAt = this.startTime + this.timeOutTime;
/*  647 */       this.done = false;
/*  648 */       this.client = new IntraClient();
/*  649 */       this.loggingIn = false;
/*  650 */       this.client.reconnectAsynch(this.INTRASERVERADDRESS, Integer.parseInt(this.INTRASERVERPORT), this);
/*      */     } 
/*  652 */     if (this.client != null && !this.done)
/*      */     {
/*  654 */       if (!this.client.isConnecting) {
/*      */         
/*  656 */         if (System.currentTimeMillis() > this.timeOutAt)
/*      */         {
/*  658 */           if (!this.client.loggedIn)
/*      */           {
/*      */             
/*  661 */             this.done = true;
/*      */           }
/*      */         }
/*  664 */         if (!this.done) {
/*      */           
/*      */           try {
/*      */             
/*  668 */             if (this.client.loggedIn)
/*      */             {
/*  670 */               if (System.currentTimeMillis() - this.lastPing > 10000L) {
/*      */                 
/*  672 */                 if (this.shouldResendKingdoms)
/*      */                 {
/*  674 */                   sendKingdomInfo();
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/*      */                 try {
/*  680 */                   this.client.executePingCommand();
/*  681 */                   this.lastPing = System.currentTimeMillis();
/*  682 */                   this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/*      */                 }
/*  684 */                 catch (Exception ex) {
/*      */                   
/*  686 */                   this.done = true;
/*  687 */                   this.client.disconnect(ex.getMessage());
/*      */                 } 
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/*  693 */             if (!this.done)
/*      */             {
/*  695 */               this.client.update();
/*      */             }
/*      */           }
/*  698 */           catch (IOException iox) {
/*      */             
/*  700 */             logger.log(Level.INFO, "IOException to " + this.name + ". Disc:" + iox.getMessage(), iox);
/*  701 */             this.done = true;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*  706 */     if (this.done && this.client != null) {
/*      */       
/*  708 */       this.client.disconnect("done");
/*  709 */       this.client = null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  714 */     return this.done;
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
/*      */   public void commandExecuted(IntraClient aClient) {
/*  727 */     this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
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
/*      */   public void commandFailed(IntraClient aClient) {
/*  739 */     setAvailable(false, false, 0, 0, 0, 10);
/*  740 */     this.done = true;
/*  741 */     if (this.loggingIn) {
/*  742 */       this.loggingIn = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dataReceived(IntraClient aClient) {
/*  753 */     logger.log(Level.INFO, "Datareceived " + this.name);
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
/*      */   public void reschedule(IntraClient aClient) {
/*  768 */     setAvailable(false, false, 0, 0, 0, 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove(IntraClient aClient) {
/*  779 */     this.done = true;
/*      */   }
/*      */   
/*  782 */   public ServerEntry() { this.pingcounter = 0; } ServerEntry(int aId, String aName, boolean aEntryServer, boolean aHomeServer, boolean aPvpServer, boolean aLoginServer, boolean aIsPayment, byte aKingdom, String aExternalIP, String aExternalPort, String aIntraServerAddress, String aIntraServerPort, String aIntraServerPassword, int aSpawnPointJennX, int aSpawnPointJennY, int aSpawPpointMolX, int aSpawPpointMolY, int aSpawnPointLibX, int aSpawnPointLibY, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean isTest, long lastDecreasedChamps, long movedArtis, long spawnedUniques, boolean challenge) { this.pingcounter = 0; this.id = aId; this.name = aName; this.entryServer = aEntryServer; this.HOMESERVER = aHomeServer; this.PVPSERVER = aPvpServer; this.LOGINSERVER = aLoginServer; this.ISPAYMENT = aIsPayment; this.KINGDOM = aKingdom; this.EXTERNALIP = aExternalIP; this.EXTERNALPORT = aExternalPort; this.INTRASERVERADDRESS = aIntraServerAddress; this.INTRASERVERPORT = aIntraServerPort; this.INTRASERVERPASSWORD = aIntraServerPassword; this.SPAWNPOINTJENNX = aSpawnPointJennX; this.SPAWNPOINTJENNY = aSpawnPointJennY; this.SPAWNPOINTMOLX = aSpawPpointMolX; this.SPAWNPOINTMOLY = aSpawPpointMolY; this.SPAWNPOINTLIBX = aSpawnPointLibX; this.SPAWNPOINTLIBY = aSpawnPointLibY; this.consumerKeyToUse = _consumerKeyToUse; this.consumerSecretToUse = _consumerSecretToUse; this.applicationToken = _applicationToken; this.applicationSecret = _applicationSecret;
/*      */     this.lastDecreasedChampionPoints = lastDecreasedChamps;
/*      */     this.lastSpawnedUnique = spawnedUniques;
/*      */     this.testServer = isTest;
/*      */     this.challengeServer = challenge;
/*      */     if (movedArtis > 0L) {
/*      */       setMovedArtifacts(movedArtis);
/*      */     } else {
/*      */       movedArtifacts();
/*      */     } 
/*  792 */     canTwit(); } public void receivingData(ByteBuffer buffer) { this.maintaining = ((buffer.get() & 0x1) == 1);
/*  793 */     int numsPlaying = buffer.getInt();
/*  794 */     int maxLimit = buffer.getInt();
/*  795 */     int secsToShutdown = buffer.getInt();
/*  796 */     int mSize = buffer.getInt();
/*  797 */     setAvailable(true, this.maintaining, numsPlaying, maxLimit, secsToShutdown, mSize);
/*      */     
/*  799 */     this.timeOutAt = System.currentTimeMillis() + this.timeOutTime;
/*  800 */     this.pingcounter++;
/*  801 */     if (this.pingcounter == 20)
/*      */     {
/*      */       
/*  804 */       this.pingcounter = 0;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConsumerKey() {
/*  815 */     return this.consumerKeyToUse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConsumerKeyToUse(String aConsumerKey) {
/*  826 */     this.consumerKeyToUse = aConsumerKey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConsumerSecret() {
/*  836 */     return this.consumerSecretToUse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConsumerSecret(String aConsumerSecret) {
/*  847 */     this.consumerSecretToUse = aConsumerSecret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getApplicationToken() {
/*  857 */     return this.applicationToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setApplicationToken(String aApplicationToken) {
/*  868 */     this.applicationToken = aApplicationToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getApplicationSecret() {
/*  878 */     return this.applicationSecret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setApplicationSecret(String aApplicationSecret) {
/*  889 */     this.applicationSecret = aApplicationSecret;
/*      */   }
/*      */ 
/*      */   
/*      */   void addExistingKingdom(byte kingdomId) {
/*  894 */     if (!kingdomExists(kingdomId)) {
/*  895 */       this.existingKingdoms.add(Byte.valueOf(kingdomId));
/*      */     }
/*      */   }
/*      */   
/*      */   boolean kingdomExists(byte kingdomId) {
/*  900 */     return this.existingKingdoms.contains(Byte.valueOf(kingdomId));
/*      */   }
/*      */ 
/*      */   
/*      */   boolean removeKingdom(byte kingdomId) {
/*  905 */     return this.existingKingdoms.remove(Byte.valueOf(kingdomId));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Byte> getExistingKingdoms() {
/*  910 */     return this.existingKingdoms;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getKingdom() {
/*  920 */     return this.KINGDOM;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  930 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getId() {
/*  940 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  951 */     return "ServerEntry [id: " + this.id + ", Name: " + this.name + ", IntraIP: " + this.INTRASERVERADDRESS + ':' + this.INTRASERVERPORT + ", ExternalIP: " + this.EXTERNALIP + ':' + this.EXTERNALPORT + ", canTwit: " + 
/*  952 */       canTwit() + ']';
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setChampTwitter(String newChampConsumerKeyToUse, String newChampConsumerSecretToUse, String newChampApplicationToken, String newChampApplicationSecret) {
/*  958 */     this.champConsumerKeyToUse = newChampConsumerKeyToUse;
/*  959 */     this.champConsumerSecretToUse = newChampConsumerSecretToUse;
/*  960 */     this.champApplicationToken = newChampApplicationToken;
/*  961 */     this.champApplicationSecret = newChampApplicationSecret;
/*  962 */     Connection dbcon = null;
/*  963 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  966 */       dbcon = DbConnector.getLoginDbCon();
/*  967 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET CHAMPTWITKEY=?,CHAMPTWITSECRET=?,CHAMPTWITAPP=?,CHAMPTWITAPPSECRET=? WHERE SERVER=?");
/*  968 */       ps.setString(1, this.champConsumerKeyToUse);
/*  969 */       ps.setString(2, this.champConsumerSecretToUse);
/*  970 */       ps.setString(3, this.champApplicationToken);
/*  971 */       ps.setString(4, this.champApplicationSecret);
/*  972 */       ps.setInt(5, this.id);
/*  973 */       ps.executeUpdate();
/*      */     }
/*  975 */     catch (SQLException sqx) {
/*      */       
/*  977 */       logger.log(Level.WARNING, "Failed to set champ stamp for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/*  981 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  982 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  984 */     canTwit();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setChampStamp() {
/*  989 */     Connection dbcon = null;
/*  990 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  993 */       this.lastDecreasedChampionPoints = System.currentTimeMillis();
/*  994 */       dbcon = DbConnector.getLoginDbCon();
/*  995 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET LASTRESETCHAMPS=? WHERE SERVER=?");
/*  996 */       ps.setLong(1, this.lastDecreasedChampionPoints);
/*  997 */       ps.setInt(2, this.id);
/*  998 */       ps.executeUpdate();
/*      */     }
/* 1000 */     catch (SQLException sqx) {
/*      */       
/* 1002 */       logger.log(Level.WARNING, "Failed to set champ stamp for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1006 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1007 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean saveTwitter() {
/* 1013 */     Connection dbcon = null;
/* 1014 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1017 */       dbcon = DbConnector.getLoginDbCon();
/* 1018 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET TWITKEY=?,TWITSECRET=?,TWITAPP=?,TWITAPPSECRET=? WHERE SERVER=?");
/* 1019 */       ps.setString(1, this.consumerKeyToUse);
/* 1020 */       ps.setString(2, this.consumerSecretToUse);
/* 1021 */       ps.setString(3, this.applicationToken);
/* 1022 */       ps.setString(4, this.applicationSecret);
/* 1023 */       ps.setInt(5, this.id);
/* 1024 */       ps.executeUpdate();
/*      */     }
/* 1026 */     catch (SQLException sqx) {
/*      */       
/* 1028 */       logger.log(Level.WARNING, "Failed to save twitter for server " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1032 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1033 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1035 */     return canTwit();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void movedArtifacts() {
/* 1040 */     Connection dbcon = null;
/* 1041 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1044 */       setMovedArtifacts(System.currentTimeMillis());
/* 1045 */       dbcon = DbConnector.getLoginDbCon();
/* 1046 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET MOVEDARTIS=? WHERE SERVER=?");
/* 1047 */       ps.setLong(1, getMovedArtifacts());
/* 1048 */       ps.setInt(2, this.id);
/* 1049 */       ps.executeUpdate();
/*      */     }
/* 1051 */     catch (SQLException sqx) {
/*      */       
/* 1053 */       logger.log(Level.WARNING, "Failed to set moved artifacts stamp for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1057 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1058 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCAHelpGroup(byte dbCAHelpGroup) {
/* 1067 */     this.caHelpGroup = dbCAHelpGroup;
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getCAHelpGroup() {
/* 1072 */     return this.caHelpGroup;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void saveChallengeTimes() {
/* 1078 */     Connection dbcon = null;
/* 1079 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1082 */       dbcon = DbConnector.getLoginDbCon();
/* 1083 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET CHALLENGESTARTED=?, CHALLENGEEND=? WHERE SERVER=?");
/* 1084 */       ps.setLong(1, this.challengeStarted);
/* 1085 */       ps.setLong(2, this.challengeEnds);
/* 1086 */       ps.setLong(3, getId());
/* 1087 */       ps.executeUpdate();
/*      */     }
/* 1089 */     catch (SQLException sqx) {
/*      */       
/* 1091 */       logger.log(Level.WARNING, "Failed to update ChallengeTimes for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1095 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1096 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void updateCAHelpGroup(byte newCAHelpGroup) {
/* 1102 */     if (this.caHelpGroup != newCAHelpGroup) {
/*      */       
/* 1104 */       this.caHelpGroup = newCAHelpGroup;
/* 1105 */       Connection dbcon = null;
/* 1106 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1109 */         dbcon = DbConnector.getLoginDbCon();
/* 1110 */         ps = dbcon.prepareStatement("UPDATE SERVERS SET CAHELPGROUP=? WHERE SERVER=?");
/* 1111 */         ps.setByte(1, newCAHelpGroup);
/* 1112 */         ps.setInt(2, this.id);
/* 1113 */         ps.executeUpdate();
/*      */       }
/* 1115 */       catch (SQLException sqx) {
/*      */         
/* 1117 */         logger.log(Level.WARNING, "Failed to update CAHelp Group for localserver ", sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1121 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1122 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setLastSpawnedUnique(long val) {
/* 1129 */     this.lastSpawnedUnique = val;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getLastSpawnedUnique() {
/* 1134 */     return this.lastSpawnedUnique;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void spawnedUnique() {
/* 1139 */     this.lastSpawnedUnique = System.currentTimeMillis();
/* 1140 */     Connection dbcon = null;
/* 1141 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1144 */       dbcon = DbConnector.getLoginDbCon();
/* 1145 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET SPAWNEDUNIQUE=? WHERE SERVER=?");
/* 1146 */       ps.setLong(1, this.lastSpawnedUnique);
/* 1147 */       ps.setInt(2, this.id);
/* 1148 */       ps.executeUpdate();
/*      */     }
/* 1150 */     catch (SQLException sqx) {
/*      */       
/* 1152 */       logger.log(Level.WARNING, "Failed to set moved artifacts stamp for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1156 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1157 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void movePlayersFromId(int oldId) {
/* 1163 */     Connection dbcon = null;
/* 1164 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1167 */       dbcon = DbConnector.getPlayerDbCon();
/* 1168 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET CURRENTSERVER=? WHERE CURRENTSERVER=?");
/* 1169 */       ps.setInt(1, this.id);
/* 1170 */       ps.setInt(2, oldId);
/* 1171 */       ps.executeUpdate();
/*      */     }
/* 1173 */     catch (SQLException sqx) {
/*      */       
/* 1175 */       logger.log(Level.WARNING, "Failed to move players from server id " + oldId + " to localserver id " + this.id, sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1179 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1180 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void saveNewGui(int oldId) {
/* 1189 */     Connection dbcon = null;
/* 1190 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1193 */       dbcon = DbConnector.getLoginDbCon();
/* 1194 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET SERVER=?,NAME=?,MAXCREATURES=?,MAXPLAYERS=?,PERCENT_AGG_CREATURES=?,TREEGROWTH=?,SKILLGAINRATE=?,ACTIONTIMER=?,HOTADELAY=?,PVP=?,          HOMESERVER=?,KINGDOM=?,INTRASERVERPASSWORD=?,EXTERNALIP=?,EXTERNALPORT=?,INTRASERVERADDRESS=?,INTRASERVERPORT=?,ISTEST=?,ISPAYMENT=?,LOGINSERVER=?,           RMIPORT=?,REGISTRATIONPORT=?,LOCAL=?,RANDOMSPAWNS=?,SKILLBASICSTART=?,SKILLMINDLOGICSTART=?,SKILLFIGHTINGSTART=?,SKILLOVERALLSTART=?,EPIC=?,CRMOD=?,            STEAMPW=?,UPKEEP=?,MAXDEED=?,FREEDEEDS=?,TRADERMAX=?,TRADERINIT=?,BREEDING=?,FIELDGROWTH=?,KINGSMONEY=?, MOTD=?,     TUNNELING=?,SKILLBODYCONTROLSTART=? WHERE SERVER=?");
/* 1195 */       ps.setInt(1, this.id);
/* 1196 */       ps.setString(2, this.name);
/* 1197 */       ps.setInt(3, this.maxCreatures);
/* 1198 */       ps.setInt(4, this.pLimit);
/* 1199 */       ps.setFloat(5, this.percentAggCreatures);
/* 1200 */       ps.setInt(6, this.treeGrowth);
/* 1201 */       ps.setFloat(7, this.skillGainRate);
/* 1202 */       ps.setFloat(8, this.actionTimer);
/* 1203 */       ps.setInt(9, this.hotaDelay);
/* 1204 */       ps.setBoolean(10, this.PVPSERVER);
/* 1205 */       ps.setBoolean(11, this.HOMESERVER);
/* 1206 */       ps.setByte(12, this.KINGDOM);
/* 1207 */       ps.setString(13, this.INTRASERVERPASSWORD);
/* 1208 */       ps.setString(14, this.EXTERNALIP);
/* 1209 */       ps.setString(15, this.EXTERNALPORT);
/* 1210 */       ps.setString(16, this.INTRASERVERADDRESS);
/* 1211 */       ps.setString(17, this.INTRASERVERPORT);
/* 1212 */       ps.setBoolean(18, this.testServer);
/* 1213 */       ps.setBoolean(19, this.ISPAYMENT);
/* 1214 */       ps.setBoolean(20, this.LOGINSERVER);
/* 1215 */       ps.setString(21, String.valueOf(this.RMI_PORT));
/* 1216 */       ps.setString(22, String.valueOf(this.REGISTRATION_PORT));
/* 1217 */       ps.setBoolean(23, this.isLocal);
/* 1218 */       ps.setBoolean(24, this.randomSpawns);
/* 1219 */       ps.setFloat(25, getSkillbasicval());
/* 1220 */       ps.setFloat(26, getSkillmindval());
/* 1221 */       ps.setFloat(27, getSkillfightval());
/* 1222 */       ps.setFloat(28, getSkilloverallval());
/* 1223 */       ps.setBoolean(29, this.EPIC);
/* 1224 */       ps.setFloat(30, getCombatRatingModifier());
/* 1225 */       ps.setString(31, getSteamServerPassword());
/* 1226 */       ps.setBoolean(32, isUpkeep());
/* 1227 */       ps.setInt(33, getMaxDeedSize());
/* 1228 */       ps.setBoolean(34, isFreeDeeds());
/* 1229 */       ps.setInt(35, getTraderMaxIrons());
/* 1230 */       ps.setInt(36, getInitialTraderIrons());
/* 1231 */       ps.setLong(37, getBreedingTimer());
/* 1232 */       ps.setLong(38, getFieldGrowthTime());
/* 1233 */       ps.setInt(39, getKingsmoneyAtRestart());
/* 1234 */       ps.setString(40, this.motd);
/* 1235 */       ps.setInt(41, getTunnelingHits());
/* 1236 */       ps.setFloat(42, getSkillbcval());
/* 1237 */       ps.setInt(43, oldId);
/* 1238 */       ps.executeUpdate();
/*      */     }
/* 1240 */     catch (SQLException sqx) {
/*      */       
/* 1242 */       logger.log(Level.WARNING, "Failed to save new stuff from gui or command line", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1246 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1247 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void saveTimers() {
/* 1253 */     Connection dbcon = null;
/* 1254 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1257 */       dbcon = DbConnector.getLoginDbCon();
/* 1258 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET SKILLDAYSWITCH=?,SKILLWEEKSWITCH=?,NEXTEPICPOLL=?,FATIGUESWITCH=?,NEXTHOTA=?,WORLDTIME=?,TILEREST=?,POLLTILE=?,POLLMOD=?,POLLROUND=? WHERE SERVER=?");
/* 1259 */       ps.setLong(1, getSkillDaySwitch());
/* 1260 */       ps.setLong(2, getSkillWeekSwitch());
/* 1261 */       ps.setLong(3, getNextEpicPoll());
/* 1262 */       ps.setLong(4, getFatigueSwitch());
/* 1263 */       ps.setLong(5, getNextHota());
/* 1264 */       ps.setLong(6, WurmCalendar.getCurrentTime());
/* 1265 */       ps.setInt(7, TilePoller.rest);
/* 1266 */       ps.setInt(8, TilePoller.currentPollTile);
/* 1267 */       ps.setInt(9, TilePoller.pollModifier);
/* 1268 */       ps.setInt(10, TilePoller.pollround);
/*      */       
/* 1270 */       ps.setInt(11, this.id);
/* 1271 */       ps.executeUpdate();
/*      */     }
/* 1273 */     catch (SQLException sqx) {
/*      */       
/* 1275 */       logger.log(Level.WARNING, "Failed to set time stamps for localserver ", sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1279 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1280 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCurrentPlayersForSort() {
/* 1287 */     if (this.id == 3)
/* 1288 */       return 1000; 
/* 1289 */     return this.currentPlayers;
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
/*      */   public int compareTo(ServerEntry entry) {
/* 1303 */     return getCurrentPlayersForSort() - entry.getCurrentPlayersForSort();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAbbreviation() {
/* 1308 */     if (this.testServer) {
/* 1309 */       return this.name.substring(0, 2) + this.name.substring(this.name.length() - 1);
/*      */     }
/* 1311 */     return this.name.substring(0, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMovedArtifacts() {
/* 1321 */     return this.movedArtifacts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMovedArtifacts(long aMovedArtifacts) {
/* 1332 */     this.movedArtifacts = aMovedArtifacts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSkillDaySwitch() {
/* 1342 */     return this.skillDaySwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillDaySwitch(long aSkillDaySwitch) {
/* 1353 */     this.skillDaySwitch = aSkillDaySwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSkillWeekSwitch() {
/* 1363 */     return this.skillWeekSwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillWeekSwitch(long aSkillWeekSwitch) {
/* 1374 */     this.skillWeekSwitch = aSkillWeekSwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getNextEpicPoll() {
/* 1384 */     return this.nextEpicPoll;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNextEpicPoll(long aNextEpicPoll) {
/* 1395 */     this.nextEpicPoll = aNextEpicPoll;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getFatigueSwitch() {
/* 1405 */     return this.fatigueSwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFatigueSwitch(long aFatigueSwitch) {
/* 1416 */     this.fatigueSwitch = aFatigueSwitch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getNextHota() {
/* 1426 */     return this.nextHota;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNextHota(long aNextHota) {
/* 1437 */     this.nextHota = aNextHota;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Spawnpoint[] getSpawns() {
/* 1447 */     return this.spawns;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpawns(Spawnpoint[] aSpawns) {
/* 1458 */     this.spawns = aSpawns;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateSpawns() {
/* 1463 */     Connection dbcon = null;
/* 1464 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1467 */       dbcon = DbConnector.getLoginDbCon();
/*      */       
/* 1469 */       ps = dbcon.prepareStatement("UPDATE SERVERS SET SPAWNPOINTJENNX=?,SPAWNPOINTJENNY=?,SPAWNPOINTLIBX=?,SPAWNPOINTLIBY=?,SPAWNPOINTMOLX=?,SPAWNPOINTMOLY=? WHERE SERVER=?");
/* 1470 */       ps.setInt(1, this.SPAWNPOINTJENNX);
/* 1471 */       ps.setInt(2, this.SPAWNPOINTJENNY);
/* 1472 */       ps.setInt(3, this.SPAWNPOINTLIBX);
/* 1473 */       ps.setInt(4, this.SPAWNPOINTLIBY);
/* 1474 */       ps.setInt(5, this.SPAWNPOINTMOLX);
/* 1475 */       ps.setInt(6, this.SPAWNPOINTMOLY);
/* 1476 */       ps.setInt(7, this.id);
/*      */       
/* 1478 */       ps.executeUpdate();
/*      */     }
/* 1480 */     catch (SQLException sqex) {
/*      */       
/* 1482 */       logger.log(Level.WARNING, "Failed to update spawnpoints." + sqex
/* 1483 */           .getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1487 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1488 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getChallengeStarted() {
/* 1499 */     return this.challengeStarted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChallengeStarted(long challengeStarted) {
/* 1510 */     this.challengeStarted = challengeStarted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getChallengeEnds() {
/* 1520 */     return this.challengeEnds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChallengeEnds(long challengeEnds) {
/* 1531 */     this.challengeEnds = challengeEnds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkillGainRate() {
/* 1541 */     return this.skillGainRate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillGainRate(float skillGainRate) {
/* 1552 */     this.skillGainRate = skillGainRate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getActionTimer() {
/* 1562 */     return this.actionTimer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActionTimer(float actionTimer) {
/* 1573 */     this.actionTimer = actionTimer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHotaDelay() {
/* 1583 */     return this.hotaDelay;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHotaDelay(int hotaDelay) {
/* 1594 */     this.hotaDelay = hotaDelay;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkillfightval() {
/* 1604 */     return this.skillfightval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillfightval(float skillfightval) {
/* 1615 */     this.skillfightval = skillfightval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkillbasicval() {
/* 1625 */     return this.skillbasicval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillbasicval(float skillbasicval) {
/* 1636 */     this.skillbasicval = skillbasicval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkillmindval() {
/* 1646 */     return this.skillmindval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillmindval(float skillmindval) {
/* 1657 */     this.skillmindval = skillmindval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkilloverallval() {
/* 1667 */     return this.skilloverallval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkilloverallval(float skilloverallval) {
/* 1678 */     this.skilloverallval = skilloverallval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCombatRatingModifier() {
/* 1688 */     return this.combatRatingModifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCombatRatingModifier(float combatRatingModifier) {
/* 1699 */     this.combatRatingModifier = combatRatingModifier;
/*      */   }
/*      */   
/*      */   public String getSteamServerPassword() {
/* 1703 */     return this.steamServerPassword;
/*      */   }
/*      */   
/*      */   public void setSteamServerPassword(String steamServerPassword) {
/* 1707 */     this.steamServerPassword = steamServerPassword;
/*      */   }
/*      */   
/*      */   public boolean isUpkeep() {
/* 1711 */     return this.upkeep;
/*      */   }
/*      */   
/*      */   public void setUpkeep(boolean upkeep) {
/* 1715 */     this.upkeep = upkeep;
/*      */   }
/*      */   
/*      */   public boolean isFreeDeeds() {
/* 1719 */     return this.freeDeeds;
/*      */   }
/*      */   
/*      */   public void setFreeDeeds(boolean freeDeeds) {
/* 1723 */     this.freeDeeds = freeDeeds;
/*      */   }
/*      */   
/*      */   public int getMaxDeedSize() {
/* 1727 */     return this.maxDeedSize;
/*      */   }
/*      */   
/*      */   public void setMaxDeedSize(int maxDeedSize) {
/* 1731 */     this.maxDeedSize = maxDeedSize;
/*      */   }
/*      */   
/*      */   public int getTraderMaxIrons() {
/* 1735 */     return this.traderMaxIrons;
/*      */   }
/*      */   
/*      */   public void setTraderMaxIrons(int traderMaxIrons) {
/* 1739 */     this.traderMaxIrons = traderMaxIrons;
/*      */   }
/*      */   
/*      */   public int getInitialTraderIrons() {
/* 1743 */     return this.initialTraderIrons;
/*      */   }
/*      */   
/*      */   public void setInitialTraderIrons(int initialTraderIrons) {
/* 1747 */     this.initialTraderIrons = initialTraderIrons;
/*      */   }
/*      */   
/*      */   public int getTunnelingHits() {
/* 1751 */     return this.tunnelingHits;
/*      */   }
/*      */   
/*      */   public void setTunnelingHits(int tunnelingHits) {
/* 1755 */     this.tunnelingHits = tunnelingHits;
/*      */   }
/*      */   
/*      */   public long getBreedingTimer() {
/* 1759 */     return this.breedingTimer;
/*      */   }
/*      */   
/*      */   public void setBreedingTimer(long breedingTimer) {
/* 1763 */     this.breedingTimer = breedingTimer;
/*      */   }
/*      */   
/*      */   public long getFieldGrowthTime() {
/* 1767 */     return this.fieldGrowthTime;
/*      */   }
/*      */   
/*      */   public void setFieldGrowthTime(long fieldGrowthTime) {
/* 1771 */     this.fieldGrowthTime = fieldGrowthTime;
/*      */   }
/*      */   
/*      */   public int getKingsmoneyAtRestart() {
/* 1775 */     return this.kingsmoneyAtRestart;
/*      */   }
/*      */   
/*      */   public void setKingsmoneyAtRestart(int kingsmoneyAtRestart) {
/* 1779 */     this.kingsmoneyAtRestart = kingsmoneyAtRestart;
/*      */   }
/*      */   
/*      */   public String getMotd() {
/* 1783 */     return this.motd;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasMotd() {
/* 1788 */     return (getMotd() != null && getMotd().length() > 0);
/*      */   }
/*      */   
/*      */   public void setMotd(String nmotd) {
/* 1792 */     this.motd = nmotd;
/* 1793 */     if (hasMotd())
/* 1794 */       Constants.motd = this.motd; 
/*      */   }
/*      */   
/*      */   public float getSkillbcval() {
/* 1798 */     return this.skillbcval;
/*      */   }
/*      */   
/*      */   public void setSkillbcval(float skillbcval) {
/* 1802 */     this.skillbcval = skillbcval;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */