/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import coffee.keenan.network.wrappers.upnp.UPNPService;
/*      */ import com.wurmonline.communication.ServerListener;
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.communication.SocketServer;
/*      */ import com.wurmonline.math.TilePos;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.banks.Banks;
/*      */ import com.wurmonline.server.batchjobs.PlayerBatchJob;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.behaviours.TerraformingTask;
/*      */ import com.wurmonline.server.behaviours.TileRockBehaviour;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.combat.Arrows;
/*      */ import com.wurmonline.server.combat.Battles;
/*      */ import com.wurmonline.server.combat.ServerProjectile;
/*      */ import com.wurmonline.server.combat.WeaponCreator;
/*      */ import com.wurmonline.server.creatures.AnimalSettings;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreaturePos;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateCreator;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.Delivery;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.MineDoorSettings;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Offspring;
/*      */ import com.wurmonline.server.creatures.VisionArea;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.deities.DbRitual;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.LocalSupplyDemand;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.effects.EffectFactory;
/*      */ import com.wurmonline.server.endgames.EndGameItems;
/*      */ import com.wurmonline.server.epic.Effectuator;
/*      */ import com.wurmonline.server.epic.EpicMapListener;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.epic.EpicXmlWriter;
/*      */ import com.wurmonline.server.epic.HexMap;
/*      */ import com.wurmonline.server.epic.Hota;
/*      */ import com.wurmonline.server.epic.ValreiMapData;
/*      */ import com.wurmonline.server.highways.HighwayFinder;
/*      */ import com.wurmonline.server.highways.Routes;
/*      */ import com.wurmonline.server.intra.IntraCommand;
/*      */ import com.wurmonline.server.intra.IntraServer;
/*      */ import com.wurmonline.server.intra.MoneyTransfer;
/*      */ import com.wurmonline.server.intra.MountTransfer;
/*      */ import com.wurmonline.server.intra.PasswordTransfer;
/*      */ import com.wurmonline.server.intra.TimeSync;
/*      */ import com.wurmonline.server.intra.TimeTransfer;
/*      */ import com.wurmonline.server.items.BodyDbStrings;
/*      */ import com.wurmonline.server.items.CoinDbStrings;
/*      */ import com.wurmonline.server.items.DbItem;
/*      */ import com.wurmonline.server.items.DbStrings;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemDbStrings;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemMealData;
/*      */ import com.wurmonline.server.items.ItemRequirement;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.items.ItemTemplateCreator;
/*      */ import com.wurmonline.server.items.Recipes;
/*      */ import com.wurmonline.server.items.TradingWindow;
/*      */ import com.wurmonline.server.items.WurmMail;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.loot.LootTableCreator;
/*      */ import com.wurmonline.server.players.AchievementGenerator;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.AwardLadder;
/*      */ import com.wurmonline.server.players.Cultist;
/*      */ import com.wurmonline.server.players.HackerIp;
/*      */ import com.wurmonline.server.players.PendingAccount;
/*      */ import com.wurmonline.server.players.PendingAward;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerCommunicatorSender;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.PlayerVotes;
/*      */ import com.wurmonline.server.players.Reimbursement;
/*      */ import com.wurmonline.server.players.WurmRecord;
/*      */ import com.wurmonline.server.questions.Questions;
/*      */ import com.wurmonline.server.skills.Affinities;
/*      */ import com.wurmonline.server.skills.AffinitiesTimed;
/*      */ import com.wurmonline.server.skills.SkillStat;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.spells.Cooldowns;
/*      */ import com.wurmonline.server.spells.SpellGenerator;
/*      */ import com.wurmonline.server.spells.SpellResist;
/*      */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*      */ import com.wurmonline.server.statistics.ChallengeSummary;
/*      */ import com.wurmonline.server.statistics.Statistics;
/*      */ import com.wurmonline.server.steam.SteamHandler;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.DoorSettings;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.StructureSettings;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.support.Trello;
/*      */ import com.wurmonline.server.support.VoteQuestions;
/*      */ import com.wurmonline.server.tutorial.MissionTriggers;
/*      */ import com.wurmonline.server.tutorial.Missions;
/*      */ import com.wurmonline.server.tutorial.TriggerEffects;
/*      */ import com.wurmonline.server.utils.DbIndexManager;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.utils.logging.TileEvent;
/*      */ import com.wurmonline.server.villages.PvPAlliance;
/*      */ import com.wurmonline.server.villages.RecruitmentAds;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageMessages;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.weather.Weather;
/*      */ import com.wurmonline.server.webinterface.WcEpicKarmaCommand;
/*      */ import com.wurmonline.server.webinterface.WebCommand;
/*      */ import com.wurmonline.server.zones.AreaSpellEffect;
/*      */ import com.wurmonline.server.zones.CropTilePoller;
/*      */ import com.wurmonline.server.zones.Dens;
/*      */ import com.wurmonline.server.zones.ErrorChecks;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.TilePoller;
/*      */ import com.wurmonline.server.zones.Trap;
/*      */ import com.wurmonline.server.zones.Water;
/*      */ import com.wurmonline.server.zones.WaterType;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.server.zones.ZonesUtility;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nonnull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Server
/*      */   extends TimerTask
/*      */   implements Runnable, ServerMonitoring, ServerListener, CounterTypes, MiscConstants, CreatureTemplateIds, TimeConstants, EpicMapListener
/*      */ {
/*      */   private SocketServer socketServer;
/*      */   private boolean isPS = false;
/*  195 */   private static final Logger logger = Logger.getLogger(Server.class.getName());
/*      */   
/*  197 */   private static Server instance = null;
/*      */   
/*      */   private static boolean EpicServer;
/*      */   
/*      */   private static boolean ChallengeServer;
/*  202 */   public static final Random rand = new Random();
/*      */   
/*  204 */   public static final Object SYNC_LOCK = new Object();
/*      */ 
/*      */   
/*      */   public static final long SLEEP_TIME = 25L;
/*      */ 
/*      */   
/*      */   private static final long LIGHTNING_INTERVAL = 5000L;
/*      */ 
/*      */   
/*      */   private static final long DIRTY_MESH_ROW_SAVE_INTERVAL = 60000L;
/*      */ 
/*      */   
/*      */   private static final long SKILL_POLL_INTERVAL = 21600000L;
/*      */ 
/*      */   
/*      */   private static final long MACROING_RESET_INTERVAL = 14400000L;
/*      */ 
/*      */   
/*      */   private static final long ARROW_POLL_INTERVAL = 100L;
/*      */ 
/*      */   
/*      */   private static final long MAIL_POLL_INTERVAL = 364000L;
/*      */ 
/*      */   
/*      */   private static final long RUBBLE_POLL_INTERVAL = 60000L;
/*      */ 
/*      */   
/*      */   private static final long WATER_POLL_INTERVAL = 1000L;
/*      */ 
/*      */   
/*      */   private static final float STORM_RAINY_THRESHOLD = 0.5F;
/*      */ 
/*      */   
/*      */   private static final float STORM_CLOUDY_THRESHOLD = 0.5F;
/*      */ 
/*      */   
/*      */   private static final long WEATHER_SET_INTERVAL = 70000L;
/*      */   
/*  242 */   private static short counter = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Long> playersAtLogin;
/*      */ 
/*      */   
/*  249 */   private static final ReentrantReadWriteLock PLAYERS_AT_LOGIN_RW_LOCK = new ReentrantReadWriteLock();
/*      */ 
/*      */   
/*      */   private static boolean locked = false;
/*      */   
/*  254 */   private static short molRehanX = 438;
/*      */ 
/*      */   
/*  257 */   private static short molRehanY = 2142;
/*      */   
/*  259 */   private static int newPremiums = 0;
/*      */   
/*  261 */   private static int expiredPremiums = 0;
/*  262 */   private static long lastResetNewPremiums = 0L;
/*      */   
/*  264 */   private static long lastPolledSupplyDepots = 0L;
/*      */ 
/*      */   
/*  267 */   private static long savedChallengePage = System.currentTimeMillis() + 120000L;
/*  268 */   private static int oldPremiums = 0;
/*      */   
/*  270 */   private static long lastResetOldPremiums = 0L;
/*      */ 
/*      */   
/*      */   public static MeshIO surfaceMesh;
/*      */ 
/*      */   
/*      */   public static MeshIO caveMesh;
/*      */ 
/*      */   
/*      */   public static MeshIO resourceMesh;
/*      */ 
/*      */   
/*      */   public static MeshIO rockMesh;
/*      */ 
/*      */   
/*      */   public static HexMap epicMap;
/*      */ 
/*      */   
/*      */   private static MeshIO flagsMesh;
/*      */   
/*      */   private static final int bitBonatize = 128;
/*      */   
/*      */   private static final int bitForage = 64;
/*      */   
/*      */   private static final int bitGather = 32;
/*      */   
/*      */   private static final int bitInvestigate = 16;
/*      */   
/*      */   private static final int bitGrubs = 2048;
/*      */   
/*      */   private static final int bitHiveCheck = 1024;
/*      */   
/*      */   private static final int bitBeingTransformed = 512;
/*      */   
/*      */   private static final int bitTransformed = 256;
/*      */   
/*      */   private boolean needSeeds = false;
/*      */   
/*  308 */   private static List<Creature> creaturesToRemove = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  313 */   private static final ReentrantReadWriteLock CREATURES_TO_REMOVE_RW_LOCK = new ReentrantReadWriteLock();
/*      */   
/*  315 */   private static final Set<WebCommand> webcommands = new HashSet<>();
/*      */   
/*  317 */   private static final Set<TerraformingTask> terraformingTasks = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  322 */   public static final ReentrantReadWriteLock TERRAFORMINGTASKS_RW_LOCK = new ReentrantReadWriteLock();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  327 */   public static final ReentrantReadWriteLock WEBCOMMANDS_RW_LOCK = new ReentrantReadWriteLock();
/*      */   
/*  329 */   public static int lagticks = 0;
/*  330 */   public static float lastLagticks = 0.0F;
/*  331 */   public static int lagMoveModifier = 0;
/*  332 */   private static int lastSentWarning = 0;
/*      */   
/*  334 */   private static long lastAwardedBattleCamps = System.currentTimeMillis();
/*      */   
/*  336 */   private static long startTime = System.currentTimeMillis();
/*      */   
/*  338 */   private static long lastSecond = System.currentTimeMillis();
/*      */   
/*  340 */   private static long lastPolledRubble = 0L;
/*  341 */   private static long lastPolledShopCultist = System.currentTimeMillis();
/*      */   
/*  343 */   private static Map<String, Boolean> ips = new ConcurrentHashMap<>();
/*  344 */   private static ConcurrentLinkedQueue<PendingAward> pendingAwards = new ConcurrentLinkedQueue<>();
/*  345 */   private static int numips = 0;
/*      */   
/*  347 */   private static int logons = 0;
/*      */   
/*  349 */   private static int logonsPrem = 0;
/*      */   
/*  351 */   private static int newbies = 0;
/*      */ 
/*      */   
/*  354 */   private static volatile long millisToShutDown = Long.MIN_VALUE;
/*      */   
/*  356 */   private static long lastPinged = 0L;
/*      */   
/*  358 */   private static long lastDeletedPlayer = 0L;
/*      */   
/*  360 */   private static long lastLoweredRanks = System.currentTimeMillis() + 600000L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  365 */   private static volatile String shutdownReason = "Reason: unknown";
/*      */   
/*  367 */   private static List<Long> finalLogins = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  372 */   private static final ReentrantReadWriteLock FINAL_LOGINS_RW_LOCK = new ReentrantReadWriteLock();
/*      */   
/*      */   private static boolean pollCommunicators = false;
/*      */   
/*      */   public static final int VILLAGE_POLL_MOD = 4000;
/*      */   
/*  378 */   private long lastTicked = 0L;
/*      */   
/*  380 */   private static long lastWeather = 0L;
/*      */   
/*  382 */   private static long lastArrow = 0L;
/*  383 */   private static long lastMailCheck = System.currentTimeMillis();
/*      */   
/*  385 */   private static long lastFaith = 0L;
/*      */   
/*  387 */   private static long lastRecruitmentPoll = 0L;
/*      */   
/*  389 */   private static long lastAwardedItems = System.currentTimeMillis();
/*  390 */   private static int lostConnections = 0;
/*      */   
/*  392 */   private long nextTerraformPoll = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  397 */   private static int totalTicks = 0;
/*  398 */   private static int commPollCounter = 0;
/*  399 */   private static int commPollCounterInit = 1;
/*  400 */   private long lastLogged = 0L;
/*      */ 
/*      */   
/*  403 */   private static long lastPolledBanks = 0L;
/*  404 */   private static long lastPolledWater = 0L;
/*  405 */   private static long lastPolledHighwayFinder = 0L;
/*      */   
/*  407 */   private byte[] externalIp = new byte[4];
/*      */   
/*  409 */   private byte[] internalIp = new byte[4];
/*      */ 
/*      */   
/*  412 */   private static final Weather weather = new Weather();
/*      */   
/*      */   private boolean thunderMode = false;
/*      */   
/*  416 */   private long lastFlash = 0L;
/*      */   
/*      */   private IntraServer intraServer;
/*      */   
/*  420 */   private final List<IntraCommand> intraCommands = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  425 */   private static final ReentrantReadWriteLock INTRA_COMMANDS_RW_LOCK = new ReentrantReadWriteLock();
/*      */   
/*  427 */   private long lastClearedFaithGain = 0L;
/*      */   
/*  429 */   private static int exceptions = 0;
/*      */   
/*  431 */   private static int secondsLag = 0;
/*      */   
/*  433 */   public static String alertMessage1 = "";
/*      */   
/*  435 */   public static long lastAlertMess1 = Long.MAX_VALUE;
/*      */   
/*  437 */   public static String alertMessage2 = "";
/*      */   
/*  439 */   public static long lastAlertMess2 = Long.MAX_VALUE;
/*      */   
/*  441 */   public static String alertMessage3 = "";
/*      */   
/*  443 */   public static long lastAlertMess3 = Long.MAX_VALUE;
/*      */   
/*  445 */   public static String alertMessage4 = "";
/*      */   
/*  447 */   public static long lastAlertMess4 = Long.MAX_VALUE;
/*      */   
/*  449 */   public static long timeBetweenAlertMess1 = Long.MAX_VALUE;
/*      */   
/*  451 */   public static long timeBetweenAlertMess2 = Long.MAX_VALUE;
/*      */   
/*  453 */   public static long timeBetweenAlertMess3 = Long.MAX_VALUE;
/*      */   
/*  455 */   public static long timeBetweenAlertMess4 = Long.MAX_VALUE;
/*      */   
/*  457 */   private static long lastPolledSkills = 0L;
/*  458 */   private static long lastPolledRifts = 0L;
/*  459 */   private static long lastResetAspirations = System.currentTimeMillis();
/*      */   
/*  461 */   private static long lastPolledTileEffects = System.currentTimeMillis();
/*      */   
/*  463 */   private static long lastResetTiles = System.currentTimeMillis();
/*      */ 
/*      */   
/*  466 */   private static int combatCounter = 0;
/*      */ 
/*      */   
/*  469 */   private static int secondsUptime = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private ScheduledExecutorService scheduledExecutorService;
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean allowTradeCheat = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private ExecutorService mainExecutorService;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int EXECUTOR_SERVICE_NUMBER_OF_THREADS = 20;
/*      */ 
/*      */   
/*      */   private static PlayerCommunicatorSender playerCommunicatorSender;
/*      */ 
/*      */   
/*      */   private static boolean appointedSixThousand = false;
/*      */ 
/*      */   
/*      */   static final double FMOD = 1.3571428060531616D;
/*      */ 
/*      */   
/*      */   static final double RMOD = 0.1666666716337204D;
/*      */ 
/*      */   
/*  500 */   public static int playersThroughTutorial = 0;
/*      */   
/*  502 */   public Water waterThread = null;
/*  503 */   public HighwayFinder highwayFinderThread = null;
/*      */   
/*  505 */   private static Map<Integer, Short> lowDirtHeight = new ConcurrentHashMap<>();
/*      */   
/*  507 */   private static Set<Integer> newYearEffects = new HashSet<>();
/*  508 */   public SteamHandler steamHandler = new SteamHandler();
/*      */   
/*  510 */   private static final ConcurrentHashMap<Long, Long> tempEffects = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Server getInstance() {
/*  518 */     while (locked) {
/*      */ 
/*      */       
/*      */       try {
/*  522 */         Thread.sleep(1000L);
/*  523 */         logger.log(Level.INFO, "Thread sleeping 1 second waiting for server to start.");
/*      */       }
/*  525 */       catch (InterruptedException interruptedException) {}
/*      */     } 
/*      */ 
/*      */     
/*  529 */     if (instance == null) {
/*      */       
/*      */       try {
/*      */         
/*  533 */         locked = true;
/*  534 */         instance = new Server();
/*  535 */         locked = false;
/*      */       }
/*  537 */       catch (Exception ex) {
/*      */         
/*  539 */         logger.log(Level.SEVERE, "Failed to create server instance... shutting down.", ex);
/*  540 */         System.exit(0);
/*      */       } 
/*      */     }
/*  543 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addCreatureToRemove(Creature creature) {
/*  548 */     CREATURES_TO_REMOVE_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/*  551 */       creaturesToRemove.add(creature);
/*      */     }
/*      */     finally {
/*      */       
/*  555 */       CREATURES_TO_REMOVE_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void startShutdown(int seconds, String reason) {
/*  561 */     millisToShutDown = seconds * 1000L;
/*      */     
/*  563 */     shutdownReason = "Reason: " + reason;
/*  564 */     int mins = seconds / 60;
/*  565 */     int secs = seconds - mins * 60;
/*  566 */     StringBuffer buf = new StringBuffer();
/*  567 */     if (mins > 0) {
/*      */       
/*  569 */       buf.append(mins + " minute");
/*  570 */       if (mins > 1)
/*  571 */         buf.append("s"); 
/*      */     } 
/*  573 */     if (secs > 0) {
/*      */       
/*  575 */       if (mins > 0)
/*  576 */         buf.append(" and "); 
/*  577 */       buf.append(secs + " seconds");
/*      */     } 
/*      */     
/*  580 */     broadCastAlert("The server is shutting down in " + buf.toString() + ". " + shutdownReason, true, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeCreatures() {
/*  585 */     CREATURES_TO_REMOVE_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/*  588 */       Creature[] crets = creaturesToRemove.<Creature>toArray(new Creature[creaturesToRemove.size()]);
/*  589 */       for (Creature lCret : crets) {
/*      */         
/*  591 */         if (lCret instanceof Player) {
/*      */           
/*  593 */           Players.getInstance().logoutPlayer((Player)lCret);
/*      */         } else {
/*      */           
/*  596 */           Creatures.getInstance().removeCreature(lCret);
/*  597 */         }  creaturesToRemove.remove(lCret);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  606 */       if (creaturesToRemove.size() > 0)
/*      */       {
/*  608 */         logger.log(Level.WARNING, "Okay something is weird here. Deleting list. Debug more.");
/*  609 */         creaturesToRemove = new ArrayList<>();
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/*  614 */       CREATURES_TO_REMOVE_RW_LOCK.writeLock().unlock();
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
/*      */   public boolean isLagging() {
/*  633 */     return (lagticks >= 2000);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setExternalIp() {
/*  638 */     StringTokenizer tokens = new StringTokenizer(Servers.localServer.EXTERNALIP, ".");
/*  639 */     int x = 0;
/*  640 */     while (tokens.hasMoreTokens()) {
/*      */       
/*  642 */       String next = tokens.nextToken();
/*  643 */       this.externalIp[x] = Integer.valueOf(next).byteValue();
/*      */       
/*  645 */       x++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setInternalIp() {
/*  651 */     StringTokenizer tokens = new StringTokenizer(Servers.localServer.INTRASERVERADDRESS, ".");
/*  652 */     int x = 0;
/*  653 */     while (tokens.hasMoreTokens()) {
/*      */       
/*  655 */       String next = tokens.nextToken();
/*  656 */       this.internalIp[x] = Integer.valueOf(next).byteValue();
/*      */       
/*  658 */       x++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void initialiseExecutorService(int aNumberOfThreads) {
/*  664 */     logger.info("Initialising ExecutorService with NumberOfThreads: " + aNumberOfThreads);
/*  665 */     this.mainExecutorService = Executors.newFixedThreadPool(aNumberOfThreads);
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
/*      */   public ExecutorService getMainExecutorService() {
/*  677 */     return this.mainExecutorService;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void startRunning() throws Exception {
/*  683 */     Constants.logConstantValues(false);
/*  684 */     addShutdownHook();
/*      */ 
/*      */     
/*  687 */     logCodeVersionInformation();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  693 */     DbConnector.initialize();
/*  694 */     if (Constants.dbAutoMigrate) {
/*      */       
/*  696 */       if (DbConnector.hasPendingMigrations() && DbConnector.performMigrations().isError())
/*      */       {
/*      */         
/*  699 */         throw new WurmServerException("Could not perform migrations successfully, they must either be performed manually or disabled.");
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  705 */       logger.info("Database auto-migration is not enabled - skipping migrations checks");
/*      */     } 
/*      */     
/*  708 */     if (Constants.checkAllDbTables) {
/*      */       
/*  710 */       DbUtilities.performAdminOnAllTables(DbConnector.getLoginDbCon(), DbUtilities.DbAdminAction.CHECK_MEDIUM);
/*      */     }
/*      */     else {
/*      */       
/*  714 */       logger.info("checkAllDbTables is false so not checking database tables for errors.");
/*      */     } 
/*  716 */     if (Constants.analyseAllDbTables) {
/*      */       
/*  718 */       DbUtilities.performAdminOnAllTables(DbConnector.getLoginDbCon(), DbUtilities.DbAdminAction.ANALYZE);
/*      */     }
/*      */     else {
/*      */       
/*  722 */       logger.info("analyseAllDbTables is false so not analysing database tables to update indices.");
/*      */     } 
/*  724 */     if (Constants.optimiseAllDbTables) {
/*      */       
/*  726 */       DbUtilities.performAdminOnAllTables(DbConnector.getLoginDbCon(), DbUtilities.DbAdminAction.OPTIMIZE);
/*      */     }
/*      */     else {
/*      */       
/*  730 */       logger.info("OptimizeAllDbTables is false so not optimising database tables.");
/*      */     } 
/*      */     
/*  733 */     Servers.loadAllServers(false);
/*      */ 
/*      */     
/*  736 */     if (Constants.useDirectByteBuffersForMeshIO)
/*      */     {
/*  738 */       MeshIO.setAllocateDirectBuffers(true);
/*      */     }
/*      */ 
/*      */     
/*  742 */     if (this.steamHandler.getIsOfflineServer()) {
/*      */       
/*  744 */       Servers.localServer.EXTERNALIP = "0.0.0.0";
/*  745 */       Servers.localServer.INTRASERVERADDRESS = "0.0.0.0";
/*      */     } 
/*      */     
/*  748 */     loadWorldMesh();
/*  749 */     loadCaveMesh();
/*  750 */     loadResourceMesh();
/*  751 */     loadRockMesh();
/*  752 */     loadFlagsMesh();
/*  753 */     logger.info("Max height: " + getMaxHeight());
/*      */     
/*      */     try {
/*  756 */       boolean bool = Features.Feature.SURFACEWATER.isEnabled();
/*      */     }
/*  758 */     catch (Exception ex) {
/*      */       
/*  760 */       throw ex;
/*      */     } 
/*  762 */     if (Features.Feature.SURFACEWATER.isEnabled())
/*      */     {
/*  764 */       Water.loadWaterMesh();
/*      */     }
/*  766 */     surfaceMesh.calcDistantTerrain();
/*  767 */     Features.loadAllFeatures();
/*      */     
/*  769 */     MessageServer.initialise();
/*  770 */     PLAYERS_AT_LOGIN_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/*  773 */       this.playersAtLogin = new ArrayList<>();
/*      */     }
/*      */     finally {
/*      */       
/*  777 */       PLAYERS_AT_LOGIN_RW_LOCK.writeLock().unlock();
/*      */     } 
/*  779 */     Groups.addGroup(new Group("wurm"));
/*  780 */     EpicServer = Servers.localServer.EPIC;
/*  781 */     ChallengeServer = Servers.localServer.isChallengeServer();
/*  782 */     logger.log(Level.INFO, "Protocol: 250990585");
/*  783 */     ItemTemplateCreator.initialiseItemTemplates();
/*  784 */     SpellGenerator.createSpells();
/*      */ 
/*      */     
/*  787 */     CreatureTemplateCreator.createCreatureTemplates();
/*  788 */     LootTableCreator.initializeLootTables();
/*      */ 
/*      */     
/*  791 */     if (Constants.createTemporaryDatabaseIndicesAtStartup) {
/*      */       
/*  793 */       DbIndexManager.createIndexes();
/*      */     }
/*      */     else {
/*      */       
/*  797 */       logger.warning("createTemporaryDatabaseIndicesAtStartup is false so not creating indices. This is only for development and should not happen in production");
/*      */     } 
/*      */ 
/*      */     
/*  801 */     if (Features.Feature.CROP_POLLER.isEnabled())
/*      */     {
/*  803 */       CropTilePoller.initializeFields();
/*      */     }
/*      */     
/*  806 */     if (Constants.RUNBATCH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  830 */     if (Constants.crashed) {
/*      */       
/*  832 */       PlayerBatchJob.reimburseFatigue();
/*      */     }
/*  834 */     else if (!Servers.localServer.LOGINSERVER) {
/*  835 */       Constants.crashed = true;
/*      */     } 
/*      */ 
/*      */     
/*  839 */     EffectFactory.getInstance().loadEffects();
/*      */     
/*  841 */     AnimalSettings.loadAll();
/*  842 */     ItemSettings.loadAll();
/*  843 */     DoorSettings.loadAll();
/*  844 */     StructureSettings.loadAll();
/*  845 */     MineDoorSettings.loadAll();
/*  846 */     PermissionsHistories.loadAll();
/*  847 */     Items.loadAllItemData();
/*  848 */     Items.loadAllItempInscriptionData();
/*  849 */     Items.loadAllStaticItems();
/*  850 */     Items.loadAllZoneItems((DbStrings)BodyDbStrings.getInstance());
/*  851 */     Items.loadAllZoneItems((DbStrings)ItemDbStrings.getInstance());
/*  852 */     Items.loadAllZoneItems((DbStrings)CoinDbStrings.getInstance());
/*  853 */     ItemRequirement.loadAllItemRequirements();
/*  854 */     ArmourTemplate.initialize();
/*  855 */     WeaponCreator.createWeapons();
/*  856 */     Banks.loadAllBanks();
/*  857 */     Wall.loadAllWalls();
/*  858 */     Floor.loadAllFloors();
/*  859 */     BridgePart.loadAllBridgeParts();
/*  860 */     Kingdom.loadAllKingdoms();
/*  861 */     King.loadAllEra();
/*  862 */     Cooldowns.loadAllCooldowns();
/*  863 */     TilePoller.mask = (1 << Constants.meshSize) * (1 << Constants.meshSize) - 1;
/*  864 */     Zones.getZone(0, 0, true);
/*      */     
/*  866 */     Villages.loadVillages();
/*      */     
/*  868 */     if (Features.Feature.HIGHWAYS.isEnabled()) {
/*      */       
/*  870 */       this.highwayFinderThread = new HighwayFinder();
/*  871 */       this.highwayFinderThread.start();
/*  872 */       Routes.generateAllRoutes();
/*  873 */       if (Features.Feature.WAGONER.isEnabled()) {
/*      */         
/*  875 */         Delivery.dbLoadAllDeliveries();
/*  876 */         Wagoner.dbLoadAllWagoners();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  882 */       CreaturePos.loadAllPositions();
/*  883 */       Creatures.getInstance().loadAllCreatures();
/*      */     }
/*  885 */     catch (NoSuchCreatureException nsc) {
/*      */       
/*  887 */       logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*  888 */       System.exit(0);
/*      */       
/*      */       return;
/*      */     } 
/*  892 */     Villages.loadDeadVillages();
/*  893 */     Villages.loadCitizens();
/*  894 */     Villages.loadGuards();
/*  895 */     fixHoles();
/*  896 */     Items.loadAllItemEffects();
/*  897 */     MineDoorPermission.loadAllMineDoors();
/*  898 */     Zones.loadTowers();
/*  899 */     PvPAlliance.loadPvPAlliances();
/*      */     
/*  901 */     Villages.loadWars();
/*  902 */     Villages.loadWarDeclarations();
/*  903 */     RecruitmentAds.loadRecruitmentAds();
/*  904 */     Zones.addWarDomains();
/*  905 */     long start = System.nanoTime();
/*  906 */     Economy.getEconomy();
/*  907 */     if (Servers.localServer.getKingsmoneyAtRestart() > 0)
/*  908 */       Economy.getEconomy().getKingsShop().setMoney(Servers.localServer.getKingsmoneyAtRestart()); 
/*  909 */     logger.log(Level.INFO, "Loading economy took " + ((float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*      */     
/*  911 */     EndGameItems.loadEndGameItems();
/*  912 */     if (!Servers.localServer.HOMESERVER)
/*      */     {
/*  914 */       if ((Items.getWarTargets()).length == 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  920 */     if (!Servers.localServer.EPIC || !Servers.localServer.HOMESERVER)
/*      */     {
/*  922 */       if ((Items.getSourceSprings()).length == 0)
/*      */       {
/*  924 */         Zones.shouldSourceSprings = true;
/*      */       }
/*      */     }
/*      */     
/*  928 */     if (!Features.Feature.NEWDOMAINS.isEnabled())
/*  929 */       Zones.checkAltars(); 
/*  930 */     PlayerInfoFactory.loadPlayerInfos();
/*  931 */     WurmRecord.loadAllChampRecords();
/*  932 */     Affinities.loadAffinities();
/*  933 */     PlayerInfoFactory.loadReferers();
/*  934 */     Dens.loadDens();
/*      */     
/*  936 */     Reimbursement.loadAll();
/*  937 */     PendingAccount.loadAllPendingAccounts();
/*  938 */     PasswordTransfer.loadAllPasswordTransfers();
/*  939 */     Trap.loadAllTraps();
/*  940 */     setExternalIp();
/*  941 */     setInternalIp();
/*  942 */     AchievementGenerator.generateAchievements();
/*  943 */     Achievements.loadAllAchievements();
/*  944 */     if (Constants.isGameServer)
/*      */     {
/*  946 */       Zones.writeZones();
/*      */     }
/*  948 */     if (Constants.dropTemporaryDatabaseIndicesAtStartup) {
/*      */       
/*  950 */       DbIndexManager.removeIndexes();
/*      */     }
/*      */     else {
/*      */       
/*  954 */       logger.warning("dropTemporaryDatabaseIndicesAtStartup is false so not dropping indices. This is only for development and should not happen in production");
/*      */     } 
/*      */ 
/*      */     
/*  958 */     TilePoller.entryServer = Servers.localServer.entryServer;
/*  959 */     WcEpicKarmaCommand.loadAllKarmaHelpers();
/*  960 */     FocusZone.loadAll();
/*  961 */     Hota.loadAllHotaItems();
/*  962 */     Hota.loadAllHelpers();
/*  963 */     if (Constants.createSeeds || this.needSeeds)
/*  964 */       Zones.createSeeds(); 
/*  965 */     if (Servers.localServer.testServer == true)
/*  966 */       Zones.createInvestigatables(); 
/*  967 */     this.intraServer = new IntraServer(this);
/*      */     
/*  969 */     Statistics.getInstance().startup(logger);
/*      */ 
/*      */     
/*  972 */     WurmHarvestables.setStartTimes();
/*      */ 
/*      */     
/*  975 */     WurmMail.loadAllMails();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  980 */     HistoryManager.loadHistory();
/*  981 */     Cultist.loadAllCultists();
/*  982 */     Effectuator.loadEffects();
/*  983 */     EpicServerStatus.loadLocalEntries();
/*  984 */     Tickets.loadTickets();
/*  985 */     VoteQuestions.loadVoteQuestions();
/*      */     
/*  987 */     PlayerVotes.loadAllPlayerVotes();
/*  988 */     Recipes.loadAllRecipes();
/*  989 */     ItemMealData.loadAllMealData();
/*  990 */     AffinitiesTimed.loadAllPlayerTimedAffinities();
/*  991 */     VillageMessages.loadVillageMessages();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  999 */     if (Constants.RUNBATCH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1015 */     Constants.RUNBATCH = false;
/*      */     
/* 1017 */     if (Constants.useMultiThreadedBankPolling || Constants.useQueueToSendDataToPlayers) {
/*      */       
/* 1019 */       initialiseExecutorService(20);
/* 1020 */       initialisePlayerCommunicatorSender();
/*      */     } 
/* 1022 */     setupScheduledExecutors();
/*      */ 
/*      */ 
/*      */     
/* 1026 */     Eigc.loadAllAccounts();
/* 1027 */     this
/* 1028 */       .socketServer = new SocketServer(this.externalIp, Integer.parseInt(Servers.localServer.EXTERNALPORT), Integer.parseInt(Servers.localServer.EXTERNALPORT) + 1, this);
/* 1029 */     SocketServer.MIN_MILLIS_BETWEEN_CONNECTIONS = Constants.minMillisBetweenPlayerConns;
/*      */     
/* 1031 */     logger.log(Level.INFO, "The Wurm Server is listening on ip " + Servers.localServer.EXTERNALIP + " and port " + Servers.localServer.EXTERNALPORT + ". Min time between same ip connections=" + SocketServer.MIN_MILLIS_BETWEEN_CONNECTIONS);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1036 */     commPollCounterInit = 1;
/* 1037 */     if (!Servers.localServer.PVPSERVER)
/*      */     {
/* 1039 */       if (Zones.worldTileSizeX > 5000) {
/* 1040 */         commPollCounterInit = 6;
/*      */       }
/*      */     }
/*      */     
/* 1044 */     logger.log(Level.INFO, "commPollCounterInit=" + commPollCounterInit);
/*      */ 
/*      */ 
/*      */     
/* 1048 */     if (Constants.useScheduledExecutorForServer) {
/*      */ 
/*      */       
/* 1051 */       ScheduledExecutorService scheduledServerRunExecutor = Executors.newScheduledThreadPool(Constants.scheduledExecutorServiceThreads);
/* 1052 */       for (int i = 0; i < Constants.scheduledExecutorServiceThreads; i++)
/*      */       {
/*      */         
/* 1055 */         scheduledServerRunExecutor.scheduleWithFixedDelay(this, (i * 2), 25L, TimeUnit.MILLISECONDS);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1060 */       Timer timer = new Timer();
/* 1061 */       timer.scheduleAtFixedRate(this, 0L, 25L);
/*      */       
/* 1063 */       startTime = System.currentTimeMillis();
/*      */     } 
/* 1065 */     Missions.getAllMissions();
/* 1066 */     MissionTriggers.getAllTriggers();
/* 1067 */     TriggerEffects.getAllEffects();
/* 1068 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 1070 */       epicMap = EpicServerStatus.getValrei();
/* 1071 */       epicMap.loadAllEntities();
/* 1072 */       epicMap.addListener(this);
/* 1073 */       EpicXmlWriter.dumpEntities(epicMap);
/*      */     } 
/* 1075 */     if (Features.Feature.SURFACEWATER.isEnabled()) {
/*      */       
/* 1077 */       this.waterThread = new Water();
/* 1078 */       this.waterThread.loadSprings();
/* 1079 */       this.waterThread.start();
/*      */     } 
/* 1081 */     if (Constants.startChallenge) {
/*      */       
/* 1083 */       Servers.localServer.setChallengeStarted(System.currentTimeMillis());
/* 1084 */       Servers.localServer.setChallengeEnds(System.currentTimeMillis() + Constants.challengeDays * 86400000L);
/* 1085 */       Servers.localServer.saveChallengeTimes();
/* 1086 */       Constants.startChallenge = false;
/*      */     } 
/*      */     
/* 1089 */     ChallengeSummary.loadLocalChallengeScores();
/*      */     
/* 1091 */     Creatures.getInstance().startPollTask();
/* 1092 */     WaterType.calcWaterTypes();
/*      */     
/* 1094 */     this.steamHandler.initializeSteam();
/* 1095 */     this.steamHandler.createServer("wurmunlimitedserver", "wurmunlimitedserver", "Wurm Unlimited Server", "1.0.0.0");
/*      */ 
/*      */     
/* 1098 */     DbRitual.loadRiteEvents();
/* 1099 */     DbRitual.loadRiteClaims();
/*      */     
/* 1101 */     logger.info("End of game server initialisation");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupScheduledExecutors() {
/* 1110 */     if (Constants.useScheduledExecutorToWriteLogs || Constants.useScheduledExecutorToSaveConstants || Constants.useScheduledExecutorToTickCalendar || Constants.useScheduledExecutorToCountEggs || Constants.useScheduledExecutorToSaveDirtyMeshRows || Constants.useScheduledExecutorToSendTimeSync || Constants.useScheduledExecutorToSwitchFatigue || Constants.useScheduledExecutorToUpdateCreaturePositionInDatabase || Constants.useScheduledExecutorToUpdateItemDamageInDatabase || Constants.useScheduledExecutorToUpdateItemOwnerInDatabase || Constants.useScheduledExecutorToUpdateItemLastOwnerInDatabase || Constants.useScheduledExecutorToUpdateItemParentInDatabase || Constants.useScheduledExecutorToConnectToTwitter || Constants.useScheduledExecutorToUpdatePlayerPositionInDatabase || Constants.useItemTransferLog || Constants.useTileEventLog)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1123 */       this.scheduledExecutorService = Executors.newScheduledThreadPool(15);
/*      */     }
/*      */     
/* 1126 */     if (Constants.useScheduledExecutorToWriteLogs) {
/*      */       
/* 1128 */       logger.info("Going to use a ScheduledExecutorService to write logs");
/* 1129 */       long lInitialDelay = 60L;
/* 1130 */       long lDelay = 300L;
/*      */ 
/*      */       
/* 1133 */       this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable()
/*      */           {
/*      */             
/*      */             public void run()
/*      */             {
/* 1138 */               if (Server.logger.isLoggable(Level.FINER))
/*      */               {
/* 1140 */                 Server.logger.finer("Running newSingleThreadScheduledExecutor for stat log writing");
/*      */               }
/*      */             }
/*      */           },  60L, 300L, TimeUnit.SECONDS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1153 */       long lPingDelay = 300L;
/* 1154 */       long lInitialDelay2 = 5000L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1160 */       this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable()
/*      */           {
/*      */             
/*      */             public void run()
/*      */             {
/* 1165 */               if (Server.logger.isLoggable(Level.FINEST));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 1172 */                 Servers.pingServers();
/*      */               }
/* 1174 */               catch (RuntimeException e) {
/*      */                 
/* 1176 */                 Server.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorServicePollServers while calling pingServers()", e);
/*      */                 
/* 1178 */                 throw e;
/*      */               } 
/*      */             }
/*      */           }5000L, 300L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */     
/* 1184 */     if (Constants.useScheduledExecutorToCountEggs) {
/*      */       
/* 1186 */       logger.info("Going to use a ScheduledExecutorService to count eggs");
/* 1187 */       long lInitialDelay = 1000L;
/* 1188 */       long lDelay = 3600000L;
/* 1189 */       this.scheduledExecutorService.scheduleWithFixedDelay(new Items.EggCounter(), 1000L, 3600000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1193 */     if (Constants.useScheduledExecutorToSaveConstants) {
/*      */       
/* 1195 */       logger.info("Going to use a ScheduledExecutorService to save Constants to wurm.ini");
/* 1196 */       long lInitialDelay = 1000L;
/* 1197 */       long lDelay = 1000L;
/* 1198 */       this.scheduledExecutorService.scheduleWithFixedDelay(new Constants.ConstantsSaver(), 1000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1202 */     if (Constants.useScheduledExecutorToSaveDirtyMeshRows) {
/*      */       
/* 1204 */       logger.info("Going to use a ScheduledExecutorService to call MeshIO.saveNextDirtyRow()");
/* 1205 */       long lInitialDelay = 60000L;
/* 1206 */       long lDelay = 1000L;
/* 1207 */       long delayInterval = 250L;
/* 1208 */       this.scheduledExecutorService.scheduleWithFixedDelay(new MeshSaver(surfaceMesh, "SurfaceMesh", Constants.numberOfDirtyMeshRowsToSaveEachCall), lInitialDelay, 1000L, TimeUnit.MILLISECONDS);
/*      */       
/* 1210 */       lInitialDelay += 250L;
/* 1211 */       this.scheduledExecutorService.scheduleWithFixedDelay(new MeshSaver(caveMesh, "CaveMesh", Constants.numberOfDirtyMeshRowsToSaveEachCall), lInitialDelay, 1000L, TimeUnit.MILLISECONDS);
/*      */       
/* 1213 */       lInitialDelay += 250L;
/* 1214 */       this.scheduledExecutorService.scheduleWithFixedDelay(new MeshSaver(rockMesh, "RockMesh", Constants.numberOfDirtyMeshRowsToSaveEachCall), lInitialDelay, 1000L, TimeUnit.MILLISECONDS);
/*      */       
/* 1216 */       lInitialDelay += 250L;
/* 1217 */       this.scheduledExecutorService.scheduleWithFixedDelay(new MeshSaver(resourceMesh, "ResourceMesh", Constants.numberOfDirtyMeshRowsToSaveEachCall), lInitialDelay, 1000L, TimeUnit.MILLISECONDS);
/*      */       
/* 1219 */       lInitialDelay += 250L;
/* 1220 */       this.scheduledExecutorService.scheduleWithFixedDelay(new MeshSaver(flagsMesh, "FlagsMesh", Constants.numberOfDirtyMeshRowsToSaveEachCall), lInitialDelay, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1224 */     if (Constants.useScheduledExecutorToSendTimeSync)
/*      */     {
/* 1226 */       if (Servers.localServer.LOGINSERVER) {
/*      */         
/* 1228 */         logger.warning("This is the login server so it will not send TimeSync commands");
/*      */       }
/*      */       else {
/*      */         
/* 1232 */         logger.info("Going to use a ScheduledExecutorService to send TimeSync commands");
/* 1233 */         long lInitialDelay = 1000L;
/* 1234 */         long lDelay = 3600000L;
/* 1235 */         this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)new TimeSync.TimeSyncSender(), 1000L, 3600000L, TimeUnit.MILLISECONDS);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1240 */     if (Constants.useScheduledExecutorToSwitchFatigue) {
/*      */       
/* 1242 */       logger.info("Going to use a ScheduledExecutorService to switch fatigue");
/* 1243 */       long lInitialDelay = 60000L;
/* 1244 */       long lDelay = 86400000L;
/* 1245 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)new PlayerInfoFactory.FatigueSwitcher(), 60000L, 86400000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1249 */     if (Constants.useScheduledExecutorToTickCalendar) {
/*      */       
/* 1251 */       logger.info("Going to use a ScheduledExecutorService to call WurmCalendar.tickSeconds()");
/* 1252 */       long lInitialDelay = 125L;
/* 1253 */       long lDelay = 125L;
/* 1254 */       this.scheduledExecutorService.scheduleWithFixedDelay(new WurmCalendar.Ticker(), 125L, 125L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1258 */     if (Constants.useItemTransferLog) {
/*      */       
/* 1260 */       logger.info("Going to use a ScheduledExecutorService to log Item Transfers");
/* 1261 */       long lInitialDelay = 60000L;
/* 1262 */       long lDelay = 1000L;
/* 1263 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)Item.getItemlogger(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */     
/* 1266 */     if (Constants.useTileEventLog) {
/*      */       
/* 1268 */       logger.info("Going to use a ScheduledExecutorService to log tile events");
/* 1269 */       long lInitialDelay = 60000L;
/* 1270 */       long lDelay = 1000L;
/* 1271 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)TileEvent.getTilelogger(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1275 */     if (Constants.useScheduledExecutorToUpdateCreaturePositionInDatabase) {
/*      */       
/* 1277 */       logger.info("Going to use a ScheduledExecutorService to update creature positions in database");
/* 1278 */       long lInitialDelay = 60000L;
/* 1279 */       long lDelay = 1000L;
/* 1280 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)CreaturePos.getCreatureDbPosUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1284 */     if (Constants.useScheduledExecutorToUpdatePlayerPositionInDatabase) {
/*      */       
/* 1286 */       logger.info("Going to use a ScheduledExecutorService to update player positions in database");
/* 1287 */       long lInitialDelay = 60000L;
/* 1288 */       long lDelay = 1000L;
/* 1289 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)CreaturePos.getPlayerDbPosUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1293 */     if (Constants.useScheduledExecutorToUpdateItemDamageInDatabase) {
/*      */       
/* 1295 */       logger.info("Going to use a ScheduledExecutorService to update item damage in database");
/* 1296 */       long lInitialDelay = 60000L;
/* 1297 */       long lDelay = 1000L;
/* 1298 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)DbItem.getItemDamageDatabaseUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1302 */     if (Constants.useScheduledExecutorToUpdateItemOwnerInDatabase) {
/*      */       
/* 1304 */       logger.info("Going to use a ScheduledExecutorService to update item owner in database");
/* 1305 */       long lInitialDelay = 60000L;
/* 1306 */       long lDelay = 1000L;
/* 1307 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)DbItem.getItemOwnerDatabaseUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1311 */     if (Constants.useScheduledExecutorToUpdateItemLastOwnerInDatabase) {
/*      */       
/* 1313 */       logger.info("Going to use a ScheduledExecutorService to update item last owner in database");
/* 1314 */       long lInitialDelay = 60000L;
/* 1315 */       long lDelay = 1000L;
/* 1316 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)DbItem.getItemLastOwnerDatabaseUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1320 */     if (Constants.useScheduledExecutorToUpdateItemParentInDatabase) {
/*      */       
/* 1322 */       logger.info("Going to use a ScheduledExecutorService to update item parent in database");
/* 1323 */       long lInitialDelay = 60000L;
/* 1324 */       long lDelay = 1000L;
/* 1325 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)DbItem.getItemParentDatabaseUpdater(), 60000L, 1000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */ 
/*      */     
/* 1329 */     if (Constants.useScheduledExecutorToConnectToTwitter) {
/*      */       
/* 1331 */       logger.info("Going to use a ScheduledExecutorService to connect to twitter");
/* 1332 */       long lInitialDelay = 60000L;
/* 1333 */       long lDelay = 5000L;
/* 1334 */       this.scheduledExecutorService.scheduleWithFixedDelay(Twit.getTwitterThread(), 60000L, 5000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */     
/* 1337 */     if (Constants.useScheduledExecutorForTrello) {
/*      */       
/* 1339 */       logger.info("Going to use a ScheduledExecutorService for maintaining tickets in Trello");
/* 1340 */       long lInitialDelay = 5000L;
/* 1341 */       long lDelay = 60000L;
/* 1342 */       this.scheduledExecutorService.scheduleWithFixedDelay((Runnable)Trello.getTrelloThread(), 5000L, 60000L, TimeUnit.MILLISECONDS);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void twitLocalServer(String message) {
/* 1349 */     Twit t = Servers.localServer.createTwit(message);
/* 1350 */     if (t != null) {
/* 1351 */       Twit.twit(t);
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
/*      */   private void logCodeVersionInformation() {
/*      */     try {
/* 1366 */       Package p = Class.forName("com.wurmonline.server.Server").getPackage();
/* 1367 */       if (p == null)
/*      */       {
/* 1369 */         logger.warning("Wurm Build Date: UNKNOWN (Package.getPackage() is null!)");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1374 */         logger.info("Wurm Impl Title: " + p.getImplementationTitle());
/* 1375 */         logger.info("Wurm Impl Vendor: " + p.getImplementationVendor());
/* 1376 */         logger.info("Wurm Impl Version: " + p.getImplementationVersion());
/*      */       }
/*      */     
/* 1379 */     } catch (Exception ex) {
/*      */       
/* 1381 */       logger.severe("Wurm version: UNKNOWN (Error getting version number from MANIFEST.MF)");
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1386 */       Package p = Class.forName("com.wurmonline.shared.constants.ProtoConstants").getPackage();
/* 1387 */       if (p == null)
/*      */       {
/* 1389 */         logger.warning("Wurm Common: UNKNOWN (Package.getPackage() is null!)");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1394 */         logger.info("Wurm Common Impl Title: " + p.getImplementationTitle());
/* 1395 */         logger.info("Wurm Common Impl Vendor: " + p.getImplementationVendor());
/* 1396 */         logger.info("Wurm Common Impl Version: " + p.getImplementationVersion());
/*      */       }
/*      */     
/* 1399 */     } catch (Exception ex) {
/*      */       
/* 1401 */       logger.severe("Wurm Common: UNKNOWN (Error getting version number from MANIFEST.MF)");
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1406 */       Package p = Class.forName("com.mysql.jdbc.Driver").getPackage();
/* 1407 */       if (p == null)
/*      */       {
/* 1409 */         logger.warning("MySQL JDBC: UNKNOWN (Package.getPackage() is null!)");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1414 */         logger.info("MySQL JDBC Spec Title: " + p.getSpecificationTitle());
/* 1415 */         logger.info("MySQL JDBC Spec Vendor: " + p.getSpecificationVendor());
/* 1416 */         logger.info("MySQL JDBC Spec Version: " + p.getSpecificationVersion());
/*      */         
/* 1418 */         logger.info("MySQL JDBC Impl Title: " + p.getImplementationTitle());
/* 1419 */         logger.info("MySQL JDBC Impl Vendor: " + p.getImplementationVendor());
/* 1420 */         logger.info("MySQL JDBC Impl Version: " + p.getImplementationVersion());
/*      */       }
/*      */     
/* 1423 */     } catch (Exception ex) {
/*      */       
/* 1425 */       logger.severe("MySQL JDBC: UNKNOWN (Error getting version number from MANIFEST.MF)");
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1430 */       Package p = Class.forName("javax.mail.Message").getPackage();
/* 1431 */       if (p == null)
/*      */       {
/* 1433 */         logger.warning("Javax Mail: UNKNOWN (Package.getPackage() is null!)");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 1438 */         logger.info("Javax Mail Spec Title: " + p.getSpecificationTitle());
/* 1439 */         logger.info("Javax Mail Spec Vendor: " + p.getSpecificationVendor());
/* 1440 */         logger.info("Javax Mail Spec Version: " + p.getSpecificationVersion());
/*      */         
/* 1442 */         logger.info("Javax Mail Impl Title: " + p.getImplementationTitle());
/* 1443 */         logger.info("Javax Mail Impl Vendor: " + p.getImplementationVendor());
/* 1444 */         logger.info("Javax Mail Impl Version: " + p.getImplementationVersion());
/*      */       }
/*      */     
/* 1447 */     } catch (Exception ex) {
/*      */       
/* 1449 */       logger.severe("Javax Mail: UNKNOWN (Error getting version number from MANIFEST.MF)");
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1454 */       Package p = Class.forName("javax.activation.DataSource").getPackage();
/* 1455 */       if (p == null)
/*      */       {
/* 1457 */         logger.warning("Javax Activation: UNKNOWN (Package.getPackage() is null!)");
/*      */       
/*      */       }
/*      */       else
/*      */       {
/*      */         
/* 1463 */         logger.info("Javax Activation Spec Title: " + p.getSpecificationTitle());
/* 1464 */         logger.info("Javax Activation Spec Vendor: " + p.getSpecificationVendor());
/* 1465 */         logger.info("Javax Activation Spec Version: " + p.getSpecificationVersion());
/*      */         
/* 1467 */         logger.info("Javax Activation Impl Title: " + p.getImplementationTitle());
/* 1468 */         logger.info("Javax Activation Impl Vendor: " + p.getImplementationVendor());
/* 1469 */         logger.info("Javax Activation Impl Version: " + p.getImplementationVersion());
/*      */       }
/*      */     
/* 1472 */     } catch (Exception ex) {
/*      */       
/* 1474 */       logger.severe("Javax Activation: UNKNOWN (Error getting version number from MANIFEST.MF)");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initialisePlayerCommunicatorSender() {
/* 1483 */     if (Constants.useQueueToSendDataToPlayers) {
/*      */       
/* 1485 */       playerCommunicatorSender = new PlayerCommunicatorSender();
/* 1486 */       getMainExecutorService().execute((Runnable)playerCommunicatorSender);
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
/*      */   private static void fixHoles() {
/* 1511 */     logger.log(Level.INFO, "Fixing cave entrances.");
/* 1512 */     long start = System.nanoTime();
/* 1513 */     int found = 0;
/* 1514 */     int fixed = 0;
/* 1515 */     int fixed2 = 0;
/* 1516 */     int fixed3 = 0;
/* 1517 */     int fixed4 = 0;
/* 1518 */     int fixed5 = 0;
/* 1519 */     int fixedWalls = 0;
/* 1520 */     int min = 0;
/* 1521 */     int ms = Constants.meshSize;
/* 1522 */     int max = 1 << ms;
/* 1523 */     for (int x = 0; x < max; x++) {
/*      */       
/* 1525 */       for (int y = 0; y < max; y++) {
/*      */         
/* 1527 */         int tile = surfaceMesh.getTile(x, y);
/*      */         
/* 1529 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_HOLE.id) {
/*      */ 
/*      */           
/* 1532 */           found++;
/* 1533 */           boolean fix = false;
/* 1534 */           int t = caveMesh.getTile(x, y);
/* 1535 */           if (Tiles.decodeType(t) != Tiles.Tile.TILE_CAVE_EXIT.id) {
/*      */             
/* 1537 */             fixed++;
/* 1538 */             setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_ROCK.id, (byte)0);
/*      */           } else {
/*      */             int xx;
/*      */             
/* 1542 */             for (xx = 0; xx <= 1; xx++) {
/*      */               
/* 1544 */               for (int yy = 0; yy <= 1; yy++) {
/*      */                 
/* 1546 */                 int tt = caveMesh.getTile(x + xx, y + yy);
/* 1547 */                 if (Tiles.decodeHeight(tt) == -100 && Tiles.decodeData(tt) == 0) {
/*      */                   
/* 1549 */                   fix = true;
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1554 */             if (fix) {
/*      */               
/* 1556 */               fixed2++;
/* 1557 */               for (xx = 0; xx <= 1; xx++) {
/*      */                 
/* 1559 */                 for (int yy = 0; yy <= 1; yy++)
/*      */                 {
/* 1561 */                   caveMesh.setTile(x + xx, y + yy, 
/*      */ 
/*      */                       
/* 1564 */                       Tiles.encode((short)-100, 
/* 1565 */                         TileRockBehaviour.prospect(x + xx, y + yy, false), (byte)0));
/*      */                 }
/*      */               } 
/* 1568 */               setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_ROCK.id, (byte)0);
/*      */             } 
/*      */           } 
/* 1571 */           if (!fix)
/*      */           {
/* 1573 */             int lowestX = 100000;
/* 1574 */             int lowestY = 100000;
/* 1575 */             int nextLowestX = lowestX;
/* 1576 */             int nextLowestY = lowestY;
/* 1577 */             int lowestHeight = 100000;
/* 1578 */             int nextLowestHeight = lowestHeight;
/* 1579 */             for (int xa = 0; xa <= 1; xa++) {
/*      */               
/* 1581 */               for (int ya = 0; ya <= 1; ya++) {
/*      */                 
/* 1583 */                 if (x + xa < max && y + ya < max) {
/*      */ 
/*      */                   
/* 1586 */                   int rockTile = rockMesh.getTile(x + xa, y + ya);
/* 1587 */                   int rockHeight = Tiles.decodeHeight(rockTile);
/*      */                   
/* 1589 */                   if (rockHeight <= lowestHeight) {
/*      */                     
/* 1591 */                     if (lowestHeight < nextLowestHeight && 
/* 1592 */                       TileRockBehaviour.isAdjacent(lowestX, lowestY, x + xa, y + ya)) {
/*      */                       
/* 1594 */                       nextLowestHeight = lowestHeight;
/* 1595 */                       nextLowestX = lowestX;
/* 1596 */                       nextLowestY = lowestY;
/*      */                     } 
/* 1598 */                     lowestHeight = rockHeight;
/* 1599 */                     lowestX = x + xa;
/* 1600 */                     lowestY = y + ya;
/*      */                   }
/* 1602 */                   else if (rockHeight <= nextLowestHeight && nextLowestHeight > lowestHeight && 
/* 1603 */                     TileRockBehaviour.isAdjacent(lowestX, lowestY, x + xa, y + ya)) {
/*      */                     
/* 1605 */                     nextLowestHeight = rockHeight;
/* 1606 */                     nextLowestX = x + xa;
/* 1607 */                     nextLowestY = y + ya;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } 
/* 1612 */             if (lowestX != 100000 && lowestY != 100000 && nextLowestX != 100000 && nextLowestY != 100000)
/*      */             {
/* 1614 */               int lowestRock = rockMesh.getTile(lowestX, lowestY);
/* 1615 */               int nextLowestRock = rockMesh.getTile(nextLowestX, nextLowestY);
/* 1616 */               int lowestCave = caveMesh.getTile(lowestX, lowestY);
/* 1617 */               int nextLowestCave = caveMesh.getTile(nextLowestX, nextLowestY);
/* 1618 */               int lowestSurf = surfaceMesh.getTile(lowestX, lowestY);
/* 1619 */               int nextLowestSurf = surfaceMesh.getTile(nextLowestX, nextLowestY);
/* 1620 */               short lrockHeight = Tiles.decodeHeight(lowestRock);
/* 1621 */               short nlrockHeight = Tiles.decodeHeight(nextLowestRock);
/* 1622 */               short lcaveHeight = Tiles.decodeHeight(lowestCave);
/* 1623 */               short nlcaveHeight = Tiles.decodeHeight(nextLowestCave);
/* 1624 */               short lsurfHeight = Tiles.decodeHeight(lowestSurf);
/* 1625 */               short nlsurfHeight = Tiles.decodeHeight(nextLowestSurf);
/* 1626 */               if (lcaveHeight != lrockHeight || Tiles.decodeData(lowestCave) != 0) {
/*      */                 
/* 1628 */                 fixed4++;
/* 1629 */                 caveMesh.setTile(lowestX, lowestY, 
/* 1630 */                     Tiles.encode(lrockHeight, Tiles.decodeType(lowestCave), (byte)0));
/*      */               } 
/* 1632 */               if (nlcaveHeight != nlrockHeight || Tiles.decodeData(nextLowestCave) != 0) {
/*      */                 
/* 1634 */                 fixed4++;
/* 1635 */                 caveMesh.setTile(nextLowestX, nextLowestY, 
/* 1636 */                     Tiles.encode(nlrockHeight, Tiles.decodeType(nextLowestCave), (byte)0));
/*      */               } 
/* 1638 */               if (lsurfHeight != lrockHeight) {
/*      */                 
/* 1640 */                 fixed5++;
/* 1641 */                 setSurfaceTile(lowestX, lowestY, lrockHeight, Tiles.decodeType(lowestSurf), 
/* 1642 */                     Tiles.decodeData(lowestSurf));
/*      */               } 
/* 1644 */               if (nlsurfHeight != nlrockHeight)
/*      */               {
/* 1646 */                 fixed5++;
/* 1647 */                 setSurfaceTile(nextLowestX, nextLowestY, nlrockHeight, Tiles.decodeType(nextLowestSurf), 
/* 1648 */                     Tiles.decodeData(nextLowestSurf));
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1660 */           tile = caveMesh.getTile(x, y);
/* 1661 */           if (Tiles.decodeType(tile) == Tiles.Tile.TILE_CAVE.id) {
/*      */             
/* 1663 */             int minheight = -100;
/*      */             
/* 1665 */             boolean fix = false; int xx;
/* 1666 */             for (xx = 0; xx <= 1; xx++) {
/*      */               
/* 1668 */               for (int yy = 0; yy <= 1; yy++) {
/*      */                 
/* 1670 */                 int tt = caveMesh.getTile(x + xx, y + yy);
/* 1671 */                 if (Tiles.decodeHeight(tt) == -100 && Tiles.decodeData(tt) == 0) {
/*      */                   
/* 1673 */                   fix = true;
/* 1674 */                   if (Tiles.decodeHeight(tt) > minheight) {
/* 1675 */                     minheight = Tiles.decodeHeight(tt);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */             
/* 1681 */             if (fix)
/*      */             {
/* 1683 */               fixed3++;
/* 1684 */               for (xx = 0; xx <= 1; xx++)
/*      */               {
/* 1686 */                 for (int yy = 0; yy <= 1; yy++)
/*      */                 {
/* 1688 */                   int tt = caveMesh.getTile(x + xx, y + yy);
/* 1689 */                   int rocktile = rockMesh.getTile(x + xx, y + yy);
/* 1690 */                   int rockHeight = Tiles.decodeHeight(rocktile);
/* 1691 */                   int maxHeight = rockHeight - minheight;
/* 1692 */                   if (Tiles.decodeHeight(tt) == -100 && 
/* 1693 */                     Tiles.decodeData(tt) == 0)
/*      */                   {
/* 1695 */                     caveMesh.setTile(x + xx, y + yy, 
/*      */ 
/*      */                         
/* 1698 */                         Tiles.encode((short)minheight, Tiles.decodeType(tt), 
/* 1699 */                           (byte)Math.min(maxHeight, 5)));
/*      */                   }
/*      */                 }
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */           
/* 1707 */           } else if (Tiles.getTile(Tiles.decodeType(tile)) == null) {
/*      */             
/* 1709 */             caveMesh.setTile(x, y, 
/*      */ 
/*      */                 
/* 1712 */                 Tiles.encode((short)-100, TileRockBehaviour.prospect(x & (1 << Constants.meshSize) - 1, y >> Constants.meshSize, false), (byte)0));
/*      */             
/* 1714 */             logger.log(Level.INFO, "Mended a " + Tiles.decodeType(tile) + " cave tile at " + x + "," + y);
/*      */           }
/*      */           else {
/*      */             
/* 1718 */             int cavet = caveMesh.getTile(x, y);
/* 1719 */             if (Tiles.decodeData(cavet) != 0) {
/*      */               
/* 1721 */               byte cceil = Tiles.decodeData(cavet);
/* 1722 */               int caveh = Tiles.decodeHeight(cavet);
/* 1723 */               int rockHeight = Tiles.decodeHeight(rockMesh.getTile(x, y));
/* 1724 */               if (cceil + caveh > rockHeight) {
/*      */                 
/* 1726 */                 fixedWalls++;
/* 1727 */                 int maxHeight = rockHeight - caveh;
/* 1728 */                 caveMesh.setTile(x, y, 
/*      */ 
/*      */                     
/* 1731 */                     Tiles.encode((short)caveh, Tiles.decodeType(cavet), 
/* 1732 */                       (byte)Math.min(maxHeight, cceil)));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1742 */       surfaceMesh.saveAll();
/* 1743 */       logger.log(Level.INFO, "Set " + fixed + " cave entrances to rock out of " + found);
/*      */     }
/* 1745 */     catch (IOException iox) {
/*      */       
/* 1747 */       logger.log(Level.WARNING, "Failed to save surfaceMesh", iox);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1757 */     if (fixed2 > 0 || fixed3 > 0 || fixedWalls > 0 || fixed4 > 0 || fixed5 > 0) {
/*      */       
/*      */       try {
/*      */         
/* 1761 */         caveMesh.saveAll();
/* 1762 */         logger.log(Level.INFO, "Fixed " + fixed2 + " crazy cave entrances and " + fixed3 + " weird caves as well. Also fixed " + fixedWalls + " walls sticking up. Also fixed " + fixed4 + " unleavable exit nodes. Fixed " + fixed5 + " misaligned surface tile nodes.");
/*      */ 
/*      */       
/*      */       }
/* 1766 */       catch (IOException iox) {
/*      */         
/* 1768 */         logger.log(Level.WARNING, "Failed to save surfaceMesh", iox);
/*      */       } 
/*      */     }
/* 1771 */     float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 1772 */     logger.info("Fixing cave entrances took " + lElapsedTime + " ms");
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkShutDown() {
/* 1777 */     int secondsToShutDown = (int)(millisToShutDown / 1000L);
/* 1778 */     if (secondsToShutDown == 2400) {
/*      */       
/* 1780 */       if (lastSentWarning != 2400)
/*      */       {
/* 1782 */         lastSentWarning = 2400;
/* 1783 */         broadCastAlert("40 minutes to shutdown. ", false, (byte)1);
/* 1784 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1787 */     } else if (secondsToShutDown == 1200) {
/*      */       
/* 1789 */       if (lastSentWarning != 1200)
/*      */       {
/* 1791 */         lastSentWarning = 1200;
/* 1792 */         broadCastAlert("20 minutes to shutdown. ", false, (byte)1);
/* 1793 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1796 */     } else if (secondsToShutDown == 600) {
/*      */       
/* 1798 */       if (lastSentWarning != 600)
/*      */       {
/* 1800 */         lastSentWarning = 600;
/* 1801 */         broadCastAlert("10 minutes to shutdown. ", false, (byte)1);
/* 1802 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1805 */     } else if (secondsToShutDown == 300) {
/*      */       
/* 1807 */       if (lastSentWarning != 300)
/*      */       {
/* 1809 */         lastSentWarning = 300;
/* 1810 */         broadCastAlert("5 minutes to shutdown. ", true, (byte)1);
/* 1811 */         broadCastAlert(shutdownReason, true, (byte)0);
/* 1812 */         Players.getInstance().setChallengeStep(2);
/*      */       }
/*      */     
/* 1815 */     } else if (secondsToShutDown == 180) {
/*      */       
/* 1817 */       if (lastSentWarning != 180)
/*      */       {
/* 1819 */         lastSentWarning = 180;
/* 1820 */         broadCastAlert("3 minutes to shutdown. ", false, (byte)1);
/* 1821 */         broadCastAlert(shutdownReason, false, (byte)0);
/* 1822 */         Players.getInstance().setChallengeStep(3);
/* 1823 */         Players.getInstance().setChallengeStep(4);
/*      */       }
/*      */     
/* 1826 */     } else if (secondsToShutDown == 60) {
/*      */       
/* 1828 */       if (lastSentWarning != 60)
/*      */       {
/* 1830 */         lastSentWarning = 60;
/* 1831 */         broadCastAlert("1 minute to shutdown. ", false, (byte)1);
/* 1832 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1835 */     } else if (secondsToShutDown == 30) {
/*      */       
/* 1837 */       if (lastSentWarning != 30)
/*      */       {
/* 1839 */         lastSentWarning = 30;
/* 1840 */         broadCastAlert("30 seconds to shutdown. ", false, (byte)1);
/* 1841 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1844 */     } else if (secondsToShutDown == 20) {
/*      */       
/* 1846 */       if (lastSentWarning != 20)
/*      */       {
/* 1848 */         lastSentWarning = 20;
/* 1849 */         broadCastAlert("20 seconds to shutdown. ", false, (byte)1);
/* 1850 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1853 */     } else if (secondsToShutDown == 10) {
/*      */       
/* 1855 */       if (lastSentWarning != 10)
/*      */       {
/* 1857 */         lastSentWarning = 10;
/* 1858 */         FocusZone hotaZone = FocusZone.getHotaZone();
/* 1859 */         if (hotaZone != null)
/* 1860 */           Hota.forcePillarsToWorld(); 
/* 1861 */         broadCastAlert("10 seconds to shutdown. ", false, (byte)1);
/* 1862 */         broadCastAlert(shutdownReason, false, (byte)0);
/*      */       }
/*      */     
/* 1865 */     } else if (secondsToShutDown == 3) {
/*      */       
/* 1867 */       if (lastSentWarning != 1) {
/*      */         
/* 1869 */         lastSentWarning = 1;
/* 1870 */         broadCastAlert("Server shutting down NOW!/%7?o#### NO CARRIER", false);
/* 1871 */         Players.getInstance().sendLogoff("The server shut down: " + shutdownReason);
/* 1872 */         twitLocalServer("The server shut down: " + shutdownReason);
/*      */       } 
/*      */     } 
/* 1875 */     if (secondsToShutDown < 120) {
/* 1876 */       Constants.maintaining = true;
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
/*      */   public void run() {
/* 1890 */     long now = 0L;
/*      */ 
/*      */ 
/*      */     
/* 1894 */     long check = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1899 */       now = System.currentTimeMillis();
/* 1900 */       check = now;
/* 1901 */       if (Constants.isGameServer)
/*      */       {
/* 1903 */         TilePoller.pollNext();
/*      */       }
/* 1905 */       if (!Servers.localServer.testServer && System.currentTimeMillis() - check > Constants.lagThreshold)
/* 1906 */         logger.log(Level.INFO, "Lag detected at tilepoller.pollnext (0.1): " + (
/* 1907 */             (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/* 1908 */       check = System.currentTimeMillis();
/*      */ 
/*      */       
/* 1911 */       Zones.pollNextZones(25L);
/* 1912 */       if (Features.Feature.CROP_POLLER.isEnabled())
/*      */       {
/* 1914 */         CropTilePoller.pollCropTiles();
/*      */       }
/* 1916 */       Players.getInstance().pollPlayers();
/* 1917 */       Delivery.poll();
/*      */       
/* 1919 */       if (!Servers.localServer.testServer && System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 1920 */         logger.log(Level.INFO, "Lag detected at Zones.pollnextzones (0.5): " + (
/* 1921 */             (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */       }
/* 1923 */       if (millisToShutDown > -1000L)
/*      */       {
/* 1925 */         if (millisToShutDown < 0L) {
/* 1926 */           shutDown();
/*      */         } else {
/*      */           
/* 1929 */           checkShutDown();
/* 1930 */           millisToShutDown -= 25L;
/*      */         } 
/*      */       }
/* 1933 */       if (counter == 2) {
/*      */         
/* 1935 */         VoteQuestions.handleVoting();
/* 1936 */         VoteQuestions.handleArchiveTickets();
/* 1937 */         if (Features.Feature.HIGHWAYS.isEnabled())
/* 1938 */           Routes.handlePathsToSend(); 
/*      */       } 
/* 1940 */       if (counter == 3) {
/*      */         
/* 1942 */         PlayerInfoFactory.handlePlayerStateList();
/* 1943 */         Tickets.handleArchiveTickets();
/* 1944 */         Tickets.handleTicketsToSend();
/*      */       } 
/* 1946 */       if ((counter = (short)(counter + 1)) == 5) {
/*      */         
/* 1948 */         if (Constants.useScheduledExecutorToTickCalendar) {
/*      */           
/* 1950 */           if (logger.isLoggable(Level.FINEST));
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1957 */           WurmCalendar.tickSecond();
/*      */         } 
/* 1959 */         ServerProjectile.pollAll();
/* 1960 */         if (now - this.lastLogged > 300000L) {
/*      */           
/* 1962 */           this.lastLogged = now;
/* 1963 */           if (Constants.useScheduledExecutorToWriteLogs)
/*      */           {
/* 1965 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/* 1967 */               logger.finer("Using a ScheduledExecutorService to write logs so do not call writePlayerLog() from main Server thread");
/*      */             }
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1973 */           if (Constants.isGameServer && 
/* 1974 */             System.currentTimeMillis() - Servers.localServer.getFatigueSwitch() > 86400000L) {
/*      */             
/* 1976 */             if (Constants.useScheduledExecutorToSwitchFatigue) {
/*      */               
/* 1978 */               if (logger.isLoggable(Level.FINER))
/*      */               {
/* 1980 */                 logger.finer("Using a ScheduledExecutorService to switch fatigue so do not call PlayerInfoFactory.switchFatigue() from main Server thread");
/*      */               
/*      */               }
/*      */             }
/*      */             else {
/*      */               
/* 1986 */               PlayerInfoFactory.switchFatigue();
/*      */             } 
/* 1988 */             Offspring.resetOffspringCounters();
/* 1989 */             Servers.localServer.setFatigueSwitch(System.currentTimeMillis());
/*      */           } 
/* 1991 */           King.pollKings();
/* 1992 */           Players.getInstance().checkElectors();
/* 1993 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 1994 */             logger.log(Level.INFO, "Lag detected at 1: " + (
/* 1995 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/*      */         } 
/* 1997 */         if (Constants.isGameServer && now - lastArrow > 100L) {
/*      */           
/* 1999 */           Arrows.pollAll((float)(now - lastArrow));
/* 2000 */           lastArrow = now;
/*      */         } 
/*      */ 
/*      */         
/* 2004 */         boolean startHota = (Servers.localServer.getNextHota() > 0L && System.currentTimeMillis() > Servers.localServer.getNextHota());
/* 2005 */         if (startHota)
/* 2006 */           Hota.poll(); 
/* 2007 */         if (now - lastMailCheck > 364000L) {
/*      */ 
/*      */           
/* 2010 */           WurmMail.poll();
/* 2011 */           lastMailCheck = now;
/*      */         } 
/* 2013 */         if (now - lastPolledRubble > 60000L) {
/*      */           
/* 2015 */           lastPolledRubble = System.currentTimeMillis();
/* 2016 */           for (Fence fence : Fence.getRubbleFences())
/*      */           {
/* 2018 */             fence.poll(now);
/*      */           }
/* 2020 */           for (Wall wall : Wall.getRubbleWalls())
/*      */           {
/* 2022 */             wall.poll(now, null, null);
/*      */           }
/* 2024 */           if (ChallengeServer)
/*      */           {
/* 2026 */             if (Servers.localServer.getChallengeEnds() > 0L && 
/* 2027 */               System.currentTimeMillis() > Servers.localServer.getChallengeEnds())
/*      */             {
/* 2029 */               if (millisToShutDown < 0L) {
/*      */                 
/* 2031 */                 for (Village v : Villages.getVillages())
/*      */                 {
/* 2033 */                   v.disband("System");
/*      */                 }
/* 2035 */                 startShutdown(600, "The world is ending.");
/* 2036 */                 Players.getInstance().setChallengeStep(1);
/*      */               } 
/*      */             }
/*      */           }
/* 2040 */           if (tempEffects.size() > 0) {
/*      */             
/* 2042 */             HashSet<Long> toRemove = new HashSet<>();
/* 2043 */             for (Map.Entry<Long, Long> entry : tempEffects.entrySet()) {
/*      */               
/* 2045 */               if (System.currentTimeMillis() > ((Long)entry.getValue()).longValue())
/* 2046 */                 toRemove.add(entry.getKey()); 
/*      */             } 
/* 2048 */             for (Long val : toRemove) {
/*      */               
/* 2050 */               tempEffects.remove(val);
/* 2051 */               Players.getInstance().removeGlobalEffect(val.longValue());
/*      */             } 
/*      */           } 
/*      */         } 
/* 2055 */         if (now - lastPolledWater > 1000L) {
/*      */           
/* 2057 */           pollSurfaceWater();
/* 2058 */           lastPolledWater = System.currentTimeMillis();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2067 */         if (now - lastWeather > 70000L) {
/*      */           
/* 2069 */           check = System.currentTimeMillis();
/* 2070 */           lastWeather = now;
/* 2071 */           boolean setw = true;
/* 2072 */           if (weather.tick())
/*      */           {
/* 2074 */             if (Servers.localServer.LOGINSERVER) {
/*      */               
/* 2076 */               startSendWeatherThread();
/* 2077 */               setw = false;
/*      */             } 
/*      */           }
/* 2080 */           if (setw)
/* 2081 */             Players.getInstance().setShouldSendWeather(true); 
/* 2082 */           this.thunderMode = (weather.getRain() > 0.5F && weather.getCloudiness() > 0.5F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2089 */           if (WurmCalendar.isChristmas()) {
/*      */             
/* 2091 */             Zones.loadChristmas();
/*      */           }
/* 2093 */           else if (WurmCalendar.wasTestChristmas) {
/*      */             
/* 2095 */             WurmCalendar.wasTestChristmas = false;
/* 2096 */             Zones.deleteChristmas();
/*      */           }
/* 2098 */           else if (WurmCalendar.isAfterChristmas()) {
/*      */             
/* 2100 */             Zones.deleteChristmas();
/*      */           } 
/* 2102 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2103 */             logger.log(Level.INFO, "Lag detected at Weather (2): " + (
/* 2104 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/* 2105 */           if (!startHota)
/* 2106 */             Hota.poll(); 
/*      */         } 
/* 2108 */         if (Constants.isGameServer && this.thunderMode)
/*      */         {
/* 2110 */           if (now - this.lastFlash > 5000L) {
/*      */             
/* 2112 */             this.lastFlash = now;
/* 2113 */             if (weather.getRain() - 0.5F + weather.getCloudiness() - 0.5F > rand.nextFloat())
/* 2114 */               Zones.flash(); 
/*      */           } 
/*      */         }
/* 2117 */         if (Constants.isGameServer && now - lastSecond > 60000L) {
/*      */           
/* 2119 */           check = System.currentTimeMillis();
/* 2120 */           lastSecond = now;
/* 2121 */           if (Constants.useScheduledExecutorToSaveDirtyMeshRows) {
/*      */             
/* 2123 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/* 2125 */               logger.finer("useScheduledExecutorToSaveDirtyMeshRows is true so do not save the meshes from Server.run()");
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 2130 */             caveMesh.saveNextDirtyRow();
/* 2131 */             surfaceMesh.saveNextDirtyRow();
/* 2132 */             rockMesh.saveNextDirtyRow();
/* 2133 */             resourceMesh.saveNextDirtyRow();
/* 2134 */             flagsMesh.saveNextDirtyRow();
/*      */           } 
/* 2136 */           MountTransfer.pruneTransfers();
/* 2137 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2138 */             logger.log(Level.INFO, "Lag detected at Meshes.saveNextDirtyRow (4): " + (
/* 2139 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2146 */         if (Constants.isGameServer && now - lastPolledSkills > 21600000L) {
/*      */           
/* 2148 */           check = System.currentTimeMillis();
/* 2149 */           if (!Features.Feature.SKILLSTAT_DISABLE.isEnabled())
/* 2150 */             SkillStat.pollSkills(); 
/* 2151 */           lastPolledSkills = System.currentTimeMillis();
/* 2152 */           EndGameItems.pollAll();
/* 2153 */           Trap.checkUpdate();
/* 2154 */           Items.pollUnstableRifts();
/* 2155 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2156 */             logger.log(Level.INFO, "Lag detected at pollskills (4.5): " + (
/* 2157 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/* 2158 */           if (System.currentTimeMillis() - Servers.localServer.getLastSpawnedUnique() > 1209600000L)
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 2163 */             Dens.checkDens(true);
/*      */           }
/*      */         } 
/*      */         
/* 2167 */         if (Constants.isGameServer && now - lastResetTiles > 14400000L) {
/*      */           
/* 2169 */           Zones.saveProtectedTiles();
/* 2170 */           lastResetTiles = System.currentTimeMillis();
/*      */         } 
/* 2172 */         if (Servers.localServer.LOGINSERVER)
/*      */         {
/* 2174 */           if (System.currentTimeMillis() > Servers.localServer.getNextEpicPoll()) {
/*      */             
/* 2176 */             epicMap.pollAllEntities(false);
/*      */ 
/*      */             
/* 2179 */             Servers.localServer.setNextEpicPoll(System.currentTimeMillis() + 1200000L);
/*      */           } 
/*      */         }
/*      */         
/* 2183 */         ValreiMapData.pollValreiData();
/*      */         
/* 2185 */         SpellResist.onServerPoll();
/*      */         
/* 2187 */         if (now - lastRecruitmentPoll > 86400000L) {
/*      */           
/* 2189 */           lastRecruitmentPoll = System.currentTimeMillis();
/* 2190 */           RecruitmentAds.poll();
/*      */         } 
/*      */         
/* 2193 */         if (now - lastAwardedItems > 2000L) {
/*      */           
/* 2195 */           ValreiMapData.pollValreiData();
/* 2196 */           pollPendingAwards();
/* 2197 */           AwardLadder.clearItemAwards();
/* 2198 */           lastAwardedItems = System.currentTimeMillis();
/*      */         } 
/*      */         
/* 2201 */         if (now - lastFaith > 3600000L) {
/*      */           
/* 2203 */           check = System.currentTimeMillis();
/* 2204 */           lastFaith = System.currentTimeMillis();
/* 2205 */           if (Constants.isGameServer) {
/*      */             
/* 2207 */             Deities.calculateFaiths();
/* 2208 */             if (now - this.lastClearedFaithGain > 86400000L) {
/*      */               
/* 2210 */               Players.resetFaithGain();
/* 2211 */               this.lastClearedFaithGain = now;
/*      */             } 
/* 2213 */             Creatures.getInstance().pollOfflineCreatures();
/*      */           } 
/* 2215 */           if (!Servers.isThisLoginServer()) {
/*      */             
/* 2217 */             if (Constants.useScheduledExecutorToSendTimeSync)
/*      */             {
/* 2219 */               if (logger.isLoggable(Level.FINER))
/*      */               {
/* 2221 */                 logger.finer("useScheduledExecutorToSendTimeSync is true so do not send TimeSync from Server.run()");
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 2226 */               TimeSync synch = new TimeSync();
/* 2227 */               addIntraCommand((IntraCommand)synch);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2232 */             ErrorChecks.checkItemWatchers();
/*      */           } 
/* 2234 */           if (rand.nextInt(3) == 0)
/* 2235 */             PendingAccount.poll(); 
/* 2236 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2237 */             logger.log(Level.INFO, "Lag detected at 5: " + (
/* 2238 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/*      */         } 
/* 2241 */         if (Constants.isGameServer && now - lastPolledBanks > 3601000L) {
/*      */           
/* 2243 */           check = System.currentTimeMillis();
/* 2244 */           if (Constants.useScheduledExecutorToCountEggs) {
/*      */             
/* 2246 */             if (logger.isLoggable(Level.FINER))
/*      */             {
/* 2248 */               logger.finer("useScheduledExecutorToCountEggs is true so do not call Items.countEggs() from Server.run()");
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 2253 */             Items.countEggs();
/*      */           } 
/*      */           
/* 2256 */           lastPolledBanks = now;
/* 2257 */           Banks.poll(now);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2265 */           Players.getInstance().checkAffinities();
/* 2266 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2267 */             logger.log(Level.INFO, "Lag detected at Banks and Eggs (6): " + (
/* 2268 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/*      */         } 
/* 2270 */         if (Constants.isGameServer && WurmCalendar.currentTime % 4000L == 0L) {
/*      */           
/* 2272 */           check = System.currentTimeMillis();
/* 2273 */           Players.getInstance().calcCRBonus();
/*      */ 
/*      */ 
/*      */           
/* 2277 */           Villages.poll();
/* 2278 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2279 */             logger.log(Level.INFO, "Lag detected at Villages.poll (7): " + (
/* 2280 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/* 2281 */           check = System.currentTimeMillis();
/* 2282 */           Kingdoms.poll();
/* 2283 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2284 */             logger.log(Level.INFO, "Lag detected at Kingdoms.poll (7.1): " + (
/* 2285 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/* 2286 */           check = System.currentTimeMillis();
/* 2287 */           Questions.trimQuestions();
/* 2288 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2289 */             logger.log(Level.INFO, "Lag detected at Questions.trimQuestions (7.2): " + (
/*      */                 
/* 2291 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2301 */         if (WurmCalendar.currentTime % 100L == 0L) {
/*      */           
/* 2303 */           check = System.currentTimeMillis();
/* 2304 */           Skills.switchSkills(check);
/* 2305 */           Battles.poll(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2315 */           Servers.localServer.saveTimers();
/*      */           
/* 2317 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2318 */             logger.log(Level.INFO, "Lag detected at Battles and Constants (9): " + (
/* 2319 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/* 2321 */         } else if (WurmCalendar.currentTime % 1050L == 0L) {
/*      */           
/* 2323 */           Players.getInstance().pollChamps();
/* 2324 */           Effectuator.pollEpicEffects();
/*      */         } 
/* 2326 */         if (now - lastDeletedPlayer > 3000L) {
/*      */           
/* 2328 */           PlayerInfoFactory.checkIfDeleteOnePlayer();
/* 2329 */           lastDeletedPlayer = System.currentTimeMillis();
/*      */         } 
/* 2331 */         if (now - lastLoweredRanks > 600000L) {
/*      */           
/* 2333 */           PlayerInfoFactory.pruneRanks(now);
/* 2334 */           EpicServerStatus.pollExpiredMissions();
/* 2335 */           lastLoweredRanks = System.currentTimeMillis();
/*      */         } 
/* 2337 */         if (now > this.nextTerraformPoll) {
/*      */           
/* 2339 */           pollTerraformingTasks();
/* 2340 */           this.nextTerraformPoll = System.currentTimeMillis() + 1000L;
/*      */         } 
/* 2342 */         if (Servers.localServer.EPIC && !Servers.localServer.HOMESERVER && 
/* 2343 */           now > lastPolledSupplyDepots + 60000L) {
/*      */           
/* 2345 */           for (Item depot : Items.getSupplyDepots())
/*      */           {
/* 2347 */             depot.checkItemSpawn();
/*      */           }
/* 2349 */           lastPolledSupplyDepots = now;
/*      */         } 
/* 2351 */         if (Servers.localServer.isChallengeServer()) {
/*      */           
/* 2353 */           if (now - lastAwardedBattleCamps > 600000L) {
/*      */             
/* 2355 */             for (Item i : Items.getWarTargets()) {
/*      */               
/* 2357 */               Kingdom k = Kingdoms.getKingdom(i.getKingdom());
/* 2358 */               if (k != null)
/* 2359 */                 k.addWinpoints(1); 
/* 2360 */               for (PlayerInfo pinf : PlayerInfoFactory.getPlayerInfos()) {
/*      */                 
/* 2362 */                 if (System.currentTimeMillis() - pinf.lastLogin < 86400000L)
/*      */                 {
/* 2364 */                   if (Players.getInstance().getKingdomForPlayer(pinf.wurmId) == i.getKingdom())
/*      */                   {
/* 2366 */                     ChallengeSummary.addToScore(pinf, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), 1.0F);
/*      */                   }
/*      */                 }
/*      */               } 
/*      */             } 
/* 2371 */             lastAwardedBattleCamps = System.currentTimeMillis();
/*      */           } 
/* 2373 */           if (now > lastPolledSupplyDepots + 60000L) {
/*      */             
/* 2375 */             for (Item depot : Items.getSupplyDepots())
/*      */             {
/* 2377 */               depot.checkItemSpawn();
/*      */             }
/* 2379 */             lastPolledSupplyDepots = now;
/*      */           } 
/* 2381 */           if (now - savedChallengePage > 10000L) {
/*      */             
/* 2383 */             ChallengeSummary.saveCurrentGlobalHtmlPage();
/* 2384 */             savedChallengePage = System.currentTimeMillis();
/*      */           } 
/*      */         } 
/*      */         
/* 2388 */         if (now - lastPinged > 1000L) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2398 */           Trap.checkQuickUpdate();
/* 2399 */           Players.getInstance().checkSendWeather();
/* 2400 */           check = System.currentTimeMillis();
/*      */ 
/*      */           
/* 2403 */           if (lostConnections > 20 && lostConnections > Players.getInstance().numberOfPlayers() / 2) {
/*      */             
/* 2405 */             logger.log(Level.INFO, "Trying to forcibly log off linkless players: " + lostConnections);
/* 2406 */             Players.getInstance().logOffLinklessPlayers();
/*      */           } 
/* 2408 */           lostConnections = 0;
/* 2409 */           checkAlertMessages();
/* 2410 */           lastPinged = now;
/* 2411 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2412 */             logger.log(Level.INFO, "Lag detected at checkAlertMessages (10): " + (
/* 2413 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/*      */         } 
/* 2416 */         if (Constants.isGameServer && now - lastPolledShopCultist > 86400000L) {
/*      */           
/* 2418 */           lastPolledShopCultist = System.currentTimeMillis();
/* 2419 */           Cultist.resetSkillGain();
/* 2420 */           logger.log(Level.INFO, "Polling shop demands");
/* 2421 */           check = System.currentTimeMillis();
/* 2422 */           pollShopDemands();
/* 2423 */           if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2424 */             logger.log(Level.INFO, "Lag detected at pollShopDemands (11): " + (
/* 2425 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds"); 
/*      */         } 
/* 2427 */         if (System.currentTimeMillis() - lastPolledTileEffects > 3000L) {
/*      */           
/* 2429 */           AreaSpellEffect.pollEffects();
/* 2430 */           lastPolledTileEffects = System.currentTimeMillis();
/* 2431 */           Players.printStats();
/*      */         } 
/* 2433 */         if (System.currentTimeMillis() - lastResetAspirations > 90000000L) {
/*      */           
/* 2435 */           Methods.resetAspirants();
/* 2436 */           lastResetAspirations = System.currentTimeMillis();
/*      */         } 
/* 2438 */         if (this.playersAtLogin.size() > 0) {
/*      */           
/* 2440 */           check = System.currentTimeMillis();
/* 2441 */           for (Iterator<Long> it = this.playersAtLogin.listIterator(); it.hasNext(); ) {
/*      */             
/* 2443 */             long pid = ((Long)it.next()).longValue();
/*      */             
/*      */             try {
/* 2446 */               Player player = Players.getInstance().getPlayer(pid);
/*      */               
/* 2448 */               if (player.getVisionArea() == null) {
/*      */                 
/* 2450 */                 logger.log(Level.INFO, "VisionArea null for " + player.getName() + ", creating one.");
/* 2451 */                 player.createVisionArea();
/*      */               } 
/*      */               
/* 2454 */               VisionArea area = player.getVisionArea();
/*      */               
/* 2456 */               if (area != null && area.isInitialized()) {
/*      */                 
/* 2458 */                 it.remove();
/*      */ 
/*      */ 
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 2468 */                 if (area != null && !player.isDead()) {
/* 2469 */                   area.sendNextStrip(); continue;
/* 2470 */                 }  if (area == null)
/*      */                 {
/* 2472 */                   if (!player.isDead() && !player.isTeleporting())
/*      */                   {
/* 2474 */                     logger.log(Level.WARNING, "VisionArea is null for player " + player.getName() + ". Removing from login.");
/*      */                     
/* 2476 */                     it.remove();
/*      */ 
/*      */                   
/*      */                   }
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/* 2486 */               catch (Exception ex) {
/*      */                 
/* 2488 */                 logger.log(Level.INFO, ex.getMessage(), ex);
/* 2489 */                 it.remove();
/*      */               }
/*      */             
/*      */             }
/* 2493 */             catch (NoSuchPlayerException nsp) {
/*      */               
/* 2495 */               logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/* 2496 */               it.remove();
/*      */             } 
/*      */           } 
/* 2499 */           if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2500 */             logger.log(Level.INFO, "Lag detected at VisionArea (12): " + (
/* 2501 */                 (float)(System.currentTimeMillis() - check) / 1000.0F) + " seconds");
/*      */           }
/*      */         } 
/* 2504 */         check = System.currentTimeMillis();
/* 2505 */         removeCreatures();
/* 2506 */         if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2507 */           logger.log(Level.INFO, "Lag detected at removeCreatures (13.5): " + (
/* 2508 */               (float)(System.currentTimeMillis() - check) / 1000.0F)); 
/* 2509 */         counter = 0;
/*      */         
/* 2511 */         pollWebCommands();
/*      */       } 
/*      */       
/* 2514 */       check = System.currentTimeMillis();
/* 2515 */       MessageServer.sendMessages();
/* 2516 */       if (System.currentTimeMillis() - check > Constants.lagThreshold)
/* 2517 */         logger.log(Level.INFO, "Lag detected at sendMessages (14): " + (
/* 2518 */             (float)(System.currentTimeMillis() - check) / 1000.0F)); 
/* 2519 */       check = System.currentTimeMillis();
/*      */       
/* 2521 */       sendFinals();
/* 2522 */       if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2523 */         logger.log(Level.INFO, "Lag detected at sendFinals (15): " + (
/* 2524 */             (float)(System.currentTimeMillis() - check) / 1000.0F));
/*      */       }
/* 2526 */       check = System.currentTimeMillis();
/* 2527 */       this.socketServer.tick();
/*      */ 
/*      */ 
/*      */       
/* 2531 */       int realTicks = (int)(now - startTime) / 25;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2539 */       totalTicks = realTicks - totalTicks;
/* 2540 */       if (--commPollCounter <= 0) {
/*      */         
/* 2542 */         pollComms(now);
/* 2543 */         commPollCounter = commPollCounterInit;
/*      */       } 
/* 2545 */       totalTicks = realTicks;
/* 2546 */       if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/*      */         
/* 2548 */         logger.log(Level.INFO, "Lag detected at socketserver.tick (15.5): " + (
/* 2549 */             (float)(System.currentTimeMillis() - check) / 1000.0F));
/* 2550 */         logger.log(Level.INFO, "Numcommands=" + 
/* 2551 */             Communicator.getNumcommands() + ", last=" + Communicator.getLastcommand() + ", prev=" + 
/* 2552 */             Communicator.getPrevcommand() + " target=" + Communicator.getCommandAction() + ", Message=" + 
/* 2553 */             Communicator.getCommandMessage());
/* 2554 */         logger.log(Level.INFO, "Size of connections=" + this.socketServer.getNumberOfConnections() + " logins=" + LoginHandler.logins + ", redirs=" + LoginHandler.redirects + " exceptions=" + exceptions);
/*      */       } 
/*      */       
/* 2557 */       LoginHandler.logins = 0;
/* 2558 */       LoginHandler.redirects = 0;
/* 2559 */       exceptions = 0;
/* 2560 */       check = System.currentTimeMillis();
/* 2561 */       pollIntraCommands();
/* 2562 */       if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2563 */         logger.log(Level.INFO, "Lag detected at pollintracommands (15.8): " + (
/* 2564 */             (float)(System.currentTimeMillis() - check) / 1000.0F));
/*      */       }
/*      */       try {
/* 2567 */         check = System.currentTimeMillis();
/* 2568 */         this.intraServer.socketServer.tick();
/* 2569 */         if (System.currentTimeMillis() - check > Constants.lagThreshold) {
/* 2570 */           logger.log(Level.INFO, "Lag detected at intraServer.tick (16): " + (
/* 2571 */               (float)(System.currentTimeMillis() - check) / 1000.0F));
/*      */         }
/* 2573 */       } catch (IOException iox1) {
/*      */         
/* 2575 */         logger.log(Level.INFO, "Failed to update intraserver.", iox1);
/*      */       } 
/*      */       
/* 2578 */       long runLoopTime = System.currentTimeMillis() - now;
/* 2579 */       if (runLoopTime > 1000L)
/*      */       {
/* 2581 */         secondsLag = (int)(secondsLag + runLoopTime / 1000L);
/* 2582 */         logger.info("Elapsed time (" + runLoopTime + "ms) for this loop was more than 1 second so adding it to the lag count, which is now: " + secondsLag);
/*      */       }
/*      */     
/*      */     }
/* 2586 */     catch (IOException e1) {
/*      */       
/* 2588 */       logger.log(Level.INFO, "Failed to update updserver", e1);
/*      */     }
/* 2590 */     catch (Throwable t) {
/*      */       
/* 2592 */       logger.log(Level.SEVERE, t.getMessage(), t);
/* 2593 */       if (t.getMessage() == null && t.getCause() == null)
/*      */       {
/* 2595 */         logger.log(Level.SEVERE, "Server is shutting down but there is no information in the Exception so creating a new one", new Exception());
/*      */       }
/*      */ 
/*      */       
/* 2599 */       shutDown();
/*      */     }
/*      */     finally {
/*      */       
/* 2603 */       if (logger.isLoggable(Level.FINEST));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2609 */     this.steamHandler.update();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void pollComms(long now) {
/* 2614 */     long check = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2642 */     Map<String, Player> playerMap = Players.getInstance().getPlayerMap();
/*      */     
/* 2644 */     for (Map.Entry<String, Player> mapEntry : playerMap.entrySet()) {
/*      */       
/* 2646 */       if (((Player)mapEntry.getValue()).getCommunicator() != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2660 */         for (int xm = 0; xm < 10;) {
/*      */           
/* 2662 */           if (((Player)mapEntry.getValue()).getCommunicator().getMoves() > 0 && ((Player)mapEntry.getValue()).getCommunicator().getAvailableMoves() > 0) {
/*      */             
/* 2664 */             if (((Player)mapEntry.getValue()).getCommunicator().pollNextMove()) {
/* 2665 */               ((Player)mapEntry.getValue()).getCommunicator().setAvailableMoves(((Player)mapEntry.getValue()).getCommunicator().getAvailableMoves() - 1);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             xm++;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2683 */         if (!((Player)mapEntry.getValue()).moveWarned && (((Player)mapEntry
/* 2684 */           .getValue()).getCommunicator().getMoves() > 240 || ((Player)mapEntry.getValue()).getCommunicator()
/* 2685 */           .getMoves() < -240)) {
/*      */           
/* 2687 */           if (((Player)mapEntry.getValue()).getPower() >= 5) {
/* 2688 */             ((Player)mapEntry.getValue()).getCommunicator().sendAlertServerMessage("Moves at " + ((Player)mapEntry.getValue()).getCommunicator().getMoves());
/*      */           } else {
/* 2690 */             ((Player)mapEntry.getValue()).getCommunicator().sendAlertServerMessage("Your position on the server is not updated. Please move slower.");
/*      */           } 
/*      */           
/* 2693 */           ((Player)mapEntry.getValue()).moveWarned = true;
/* 2694 */           ((Player)mapEntry.getValue()).moveWarnedTime = System.currentTimeMillis();
/*      */         }
/* 2696 */         else if (((Player)mapEntry.getValue()).moveWarned && ((Player)mapEntry
/* 2697 */           .getValue()).getCommunicator().getMoves() > -24 && ((Player)mapEntry.getValue()).getCommunicator()
/* 2698 */           .getMoves() < 24) {
/*      */           
/* 2700 */           ((Player)mapEntry.getValue()).getCommunicator().sendSafeServerMessage("Your position on the server is now updated.");
/*      */           
/* 2702 */           long seconds = (System.currentTimeMillis() - ((Player)mapEntry.getValue()).moveWarnedTime) / 1000L;
/* 2703 */           logger.log(Level.INFO, ((Player)mapEntry.getValue()).getName() + " moves down to " + ((Player)mapEntry
/* 2704 */               .getValue()).getCommunicator().getMoves() + ". Was lagging " + seconds + " seconds with a peak of " + ((Player)mapEntry
/* 2705 */               .getValue()).peakMoves + " moves.");
/* 2706 */           ((Player)mapEntry.getValue()).moveWarned = false;
/* 2707 */           ((Player)mapEntry.getValue()).peakMoves = 0L;
/* 2708 */           ((Player)mapEntry.getValue()).moveWarnedTime = 0L;
/*      */         }
/* 2710 */         else if (((Player)mapEntry.getValue()).moveWarned && (((Player)mapEntry
/* 2711 */           .getValue()).getCommunicator().getMoves() > 1440 || ((Player)mapEntry.getValue()).getCommunicator()
/* 2712 */           .getMoves() < -1440)) {
/*      */           
/* 2714 */           ((Player)mapEntry.getValue()).getCommunicator().sendAlertServerMessage("You are out of synch with the server. Please stand still.");
/*      */         } 
/*      */ 
/*      */         
/* 2718 */         if (((Player)mapEntry.getValue()).getCommunicator().getMoves() > 240) {
/*      */           
/* 2720 */           if (((Player)mapEntry.getValue()).peakMoves < ((Player)mapEntry.getValue()).getCommunicator().getMoves())
/* 2721 */             ((Player)mapEntry.getValue()).peakMoves = ((Player)mapEntry.getValue()).getCommunicator().getMoves();  continue;
/*      */         } 
/* 2723 */         if (((Player)mapEntry.getValue()).getCommunicator().getMoves() < -240)
/*      */         {
/* 2725 */           if (((Player)mapEntry.getValue()).peakMoves > ((Player)mapEntry.getValue()).getCommunicator().getMoves()) {
/* 2726 */             ((Player)mapEntry.getValue()).peakMoves = ((Player)mapEntry.getValue()).getCommunicator().getMoves();
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/* 2731 */     long time = System.currentTimeMillis() - this.lastTicked;
/* 2732 */     if (time <= 3L)
/*      */     {
/* 2734 */       lagticks++;
/*      */     }
/*      */ 
/*      */     
/* 2738 */     this.lastTicked = System.currentTimeMillis();
/* 2739 */     if (System.currentTimeMillis() - check > Constants.lagThreshold)
/*      */     {
/* 2741 */       logger.log(Level.INFO, "Lag detected at Player Moves (13): " + (
/* 2742 */           (float)(System.currentTimeMillis() - check) / 1000.0F));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void pollSurfaceWater() {
/* 2749 */     if (this.waterThread != null) {
/* 2750 */       this.waterThread.propagateChanges();
/*      */     }
/*      */   }
/*      */   
/*      */   public void pollShopDemands() {
/* 2755 */     Shop[] shops = Economy.getEconomy().getShops();
/* 2756 */     for (Shop lShop : shops)
/* 2757 */       lShop.getLocalSupplyDemand().lowerDemands(); 
/* 2758 */     LocalSupplyDemand.increaseAllDemands();
/* 2759 */     Economy.getEconomy().pollTraderEarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addNewPlayer(String name) {
/* 2764 */     if (System.currentTimeMillis() - lastResetNewPremiums > 10800000L) {
/*      */       
/* 2766 */       newPremiums = 0;
/* 2767 */       lastResetNewPremiums = System.currentTimeMillis();
/*      */     } 
/* 2769 */     newPremiums++;
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
/*      */   public static final void addNewbie() {
/* 2781 */     newbies++;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addExpiry() {
/* 2786 */     expiredPremiums++;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendFinals() {
/* 2791 */     if (FINAL_LOGINS_RW_LOCK.writeLock().tryLock()) {
/*      */       
/*      */       try {
/*      */         
/* 2795 */         for (ListIterator<Long> it = finalLogins.listIterator(); it.hasNext();) {
/*      */           
/*      */           try
/*      */           {
/* 2799 */             long pid = ((Long)it.next()).longValue();
/* 2800 */             Player player = Players.getInstance().getPlayer(pid);
/* 2801 */             int step = player.getLoginStep();
/* 2802 */             if (player.isNew()) {
/*      */               
/* 2804 */               if (player.hasLink()) {
/*      */                 
/* 2806 */                 int result = LoginHandler.createPlayer(player, step);
/*      */ 
/*      */                 
/* 2809 */                 if (result == Integer.MAX_VALUE) {
/*      */                   
/* 2811 */                   it.remove();
/* 2812 */                   if (!isPlayerReceivingTiles(player))
/* 2813 */                     this.playersAtLogin.add(new Long(player.getWurmId())); 
/* 2814 */                   player.setLoginHandler(null);
/*      */                 }
/* 2816 */                 else if (result >= 0) {
/* 2817 */                   player.setLoginStep(++result);
/*      */                 } else {
/*      */                   
/* 2820 */                   player.setLoginHandler(null);
/* 2821 */                   it.remove();
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 2826 */                 player.setLoginHandler(null);
/* 2827 */                 it.remove();
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 2832 */             else if (player.hasLink()) {
/*      */               
/* 2834 */               LoginHandler handler = player.getLoginhandler();
/* 2835 */               if (handler != null) {
/*      */                 
/* 2837 */                 int result = handler.loadPlayer(player, step);
/*      */ 
/*      */                 
/* 2840 */                 if (result == Integer.MAX_VALUE) {
/*      */                   
/* 2842 */                   it.remove();
/* 2843 */                   if (!isPlayerReceivingTiles(player))
/* 2844 */                     this.playersAtLogin.add(new Long(player.getWurmId())); 
/* 2845 */                   player.setLoginHandler(null);
/*      */                 }
/* 2847 */                 else if (result >= 0) {
/* 2848 */                   player.setLoginStep(++result);
/*      */                 } else {
/*      */                   
/* 2851 */                   player.setLoginHandler(null);
/* 2852 */                   it.remove();
/*      */                 } 
/*      */               } else {
/*      */                 
/* 2856 */                 it.remove();
/*      */               } 
/*      */             } else {
/*      */               
/* 2860 */               player.setLoginHandler(null);
/* 2861 */               it.remove();
/*      */             } 
/*      */             
/* 2864 */             player.getStatus().setMoving(false);
/* 2865 */             if (!player.hasLink())
/*      */             {
/* 2867 */               Players.getInstance().logoutPlayer(player);
/*      */             }
/*      */           }
/* 2870 */           catch (NoSuchPlayerException nsp)
/*      */           {
/* 2872 */             logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/* 2873 */             it.remove();
/*      */           }
/*      */         
/*      */         } 
/*      */       } finally {
/*      */         
/* 2879 */         FINAL_LOGINS_RW_LOCK.writeLock().unlock();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addCreatureToPort(Creature creature) {
/* 2886 */     if (creature.isPlayer()) {
/*      */       
/* 2888 */       PLAYERS_AT_LOGIN_RW_LOCK.writeLock().lock();
/*      */       
/*      */       try {
/* 2891 */         if (!this.playersAtLogin.contains(new Long(creature.getWurmId())))
/*      */         {
/* 2893 */           this.playersAtLogin.add(new Long(creature.getWurmId()));
/*      */         }
/*      */       }
/*      */       finally {
/*      */         
/* 2898 */         PLAYERS_AT_LOGIN_RW_LOCK.writeLock().unlock();
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
/*      */   public void clientConnected(SocketConnection serverConnection) {
/* 2911 */     HackerIp ip = LoginHandler.failedIps.get(serverConnection.getIp());
/* 2912 */     if (ip == null || System.currentTimeMillis() > ip.mayTryAgain) {
/*      */       
/*      */       try
/*      */       {
/* 2916 */         LoginHandler login = new LoginHandler(serverConnection);
/*      */         
/* 2918 */         serverConnection.setConnectionListener(login);
/*      */       }
/* 2920 */       catch (Exception ex)
/*      */       {
/* 2922 */         logger.log(Level.SEVERE, "Failed to create login handler for serverConnection: " + serverConnection + '.', ex);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2927 */       logger.log(Level.INFO, ip.name + " Because of the repeated failures the conn may try again in " + 
/* 2928 */           getTimeFor(ip.mayTryAgain - System.currentTimeMillis()) + '.');
/* 2929 */       serverConnection.disconnect();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToPlayersAtLogin(Player player) {
/* 2938 */     if (WurmId.getType(player.getWurmId()) != 0)
/* 2939 */       logger.log(Level.WARNING, "Adding " + player.getName() + " to playersAtLogin.", new Exception()); 
/* 2940 */     if (!isPlayerReceivingTiles(player)) {
/*      */       
/* 2942 */       PLAYERS_AT_LOGIN_RW_LOCK.writeLock().lock();
/*      */       
/*      */       try {
/* 2945 */         this.playersAtLogin.add(new Long(player.getWurmId()));
/*      */       }
/*      */       finally {
/*      */         
/* 2949 */         PLAYERS_AT_LOGIN_RW_LOCK.writeLock().unlock();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPlayer(Player player) {
/* 2956 */     Players.getInstance().addPlayer(player);
/* 2957 */     if (player.isPaying())
/* 2958 */       logonsPrem++; 
/* 2959 */     logons++;
/*      */   }
/*      */ 
/*      */   
/*      */   void addIp(String ip) {
/* 2964 */     if (!ips.keySet().contains(ip)) {
/*      */       
/* 2966 */       ips.put(ip, Boolean.FALSE);
/* 2967 */       numips++;
/*      */     }
/*      */     else {
/*      */       
/* 2971 */       Boolean newb = ips.get(ip);
/* 2972 */       if (!newb.booleanValue())
/*      */       {
/* 2974 */         ips.put(ip, Boolean.FALSE);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkAlertMessages() {
/* 2981 */     if (timeBetweenAlertMess1 < Long.MAX_VALUE)
/*      */     {
/* 2983 */       if (alertMessage1.length() > 0 && lastAlertMess1 + timeBetweenAlertMess1 < System.currentTimeMillis()) {
/*      */         
/* 2985 */         broadCastAlert(alertMessage1);
/* 2986 */         lastAlertMess1 = System.currentTimeMillis();
/*      */       } 
/*      */     }
/* 2989 */     if (timeBetweenAlertMess2 < Long.MAX_VALUE)
/*      */     {
/* 2991 */       if (alertMessage2.length() > 0 && lastAlertMess2 + timeBetweenAlertMess2 < System.currentTimeMillis()) {
/*      */         
/* 2993 */         broadCastAlert(alertMessage2);
/* 2994 */         lastAlertMess2 = System.currentTimeMillis();
/*      */       } 
/*      */     }
/*      */     
/* 2998 */     if (timeBetweenAlertMess3 < Long.MAX_VALUE)
/*      */     {
/* 3000 */       if (alertMessage3.length() > 0 && lastAlertMess3 + timeBetweenAlertMess3 < System.currentTimeMillis()) {
/*      */         
/* 3002 */         broadCastAlert(alertMessage3);
/* 3003 */         lastAlertMess3 = System.currentTimeMillis();
/*      */       } 
/*      */     }
/* 3006 */     if (timeBetweenAlertMess4 < Long.MAX_VALUE)
/*      */     {
/* 3008 */       if (alertMessage4.length() > 0 && lastAlertMess4 + timeBetweenAlertMess4 < System.currentTimeMillis()) {
/*      */         
/* 3010 */         broadCastAlert(alertMessage4);
/* 3011 */         lastAlertMess4 = System.currentTimeMillis();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void startSendingFinals(Player player) {
/* 3018 */     FINAL_LOGINS_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/* 3021 */       finalLogins.add(new Long(player.getWurmId()));
/*      */     }
/*      */     finally {
/*      */       
/* 3025 */       FINAL_LOGINS_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isPlayerReceivingTiles(Player player) {
/* 3031 */     PLAYERS_AT_LOGIN_RW_LOCK.readLock().lock();
/*      */     
/*      */     try {
/* 3034 */       return this.playersAtLogin.contains(new Long(player.getWurmId()));
/*      */     }
/*      */     finally {
/*      */       
/* 3038 */       PLAYERS_AT_LOGIN_RW_LOCK.readLock().unlock();
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
/*      */   public void clientException(SocketConnection conn, Exception ex) {
/* 3053 */     exceptions++;
/*      */     
/*      */     try {
/* 3056 */       Player player = Players.getInstance().getPlayer(conn);
/* 3057 */       lostConnections++;
/* 3058 */       if (this.playersAtLogin != null) {
/*      */         
/* 3060 */         PLAYERS_AT_LOGIN_RW_LOCK.writeLock().lock();
/*      */         
/*      */         try {
/* 3063 */           this.playersAtLogin.remove(new Long(player.getWurmId()));
/*      */         }
/*      */         finally {
/*      */           
/* 3067 */           PLAYERS_AT_LOGIN_RW_LOCK.writeLock().unlock();
/*      */         } 
/*      */       } 
/* 3070 */       if (finalLogins != null) {
/*      */         
/* 3072 */         FINAL_LOGINS_RW_LOCK.writeLock().lock();
/*      */         
/*      */         try {
/* 3075 */           finalLogins.remove(new Long(player.getWurmId()));
/*      */         }
/*      */         finally {
/*      */           
/* 3079 */           FINAL_LOGINS_RW_LOCK.writeLock().unlock();
/*      */         } 
/*      */       } 
/* 3082 */       player.setLink(false);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 3087 */     catch (Exception ex2) {
/*      */       
/* 3089 */       Player player = Players.getInstance().logout(conn);
/* 3090 */       if (player != null) {
/*      */         
/* 3092 */         if (this.playersAtLogin != null) {
/*      */           
/* 3094 */           PLAYERS_AT_LOGIN_RW_LOCK.writeLock().lock();
/*      */           
/*      */           try {
/* 3097 */             this.playersAtLogin.remove(new Long(player.getWurmId()));
/*      */           }
/*      */           finally {
/*      */             
/* 3101 */             PLAYERS_AT_LOGIN_RW_LOCK.writeLock().unlock();
/*      */           } 
/*      */         } 
/* 3104 */         if (finalLogins != null) {
/*      */           
/* 3106 */           FINAL_LOGINS_RW_LOCK.writeLock().lock();
/*      */           
/*      */           try {
/* 3109 */             finalLogins.remove(new Long(player.getWurmId()));
/*      */           }
/*      */           finally {
/*      */             
/* 3113 */             FINAL_LOGINS_RW_LOCK.writeLock().unlock();
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3121 */         logger.log(Level.INFO, player.getName() + " lost link at exception 2");
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
/*      */   public Creature getCreature(long creatureId) throws NoSuchPlayerException, NoSuchCreatureException {
/*      */     Player player;
/* 3136 */     Creature toReturn = null;
/* 3137 */     if (WurmId.getType(creatureId) == 1) {
/* 3138 */       toReturn = Creatures.getInstance().getCreature(creatureId);
/*      */     } else {
/* 3140 */       player = Players.getInstance().getPlayer(creatureId);
/* 3141 */     }  return (Creature)player;
/*      */   }
/*      */ 
/*      */   
/*      */   public Creature getCreatureOrNull(long creatureId) {
/* 3146 */     if (WurmId.getType(creatureId) == 1)
/* 3147 */       return Creatures.getInstance().getCreatureOrNull(creatureId); 
/* 3148 */     return (Creature)Players.getInstance().getPlayerOrNull(creatureId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMessage(Message message) {
/* 3156 */     MessageServer.addMessage(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastNormal(String message) {
/* 3164 */     broadCastNormal(message, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastNormal(String message, boolean twit) {
/* 3172 */     MessageServer.broadCastNormal(message);
/* 3173 */     if (twit) {
/* 3174 */       twitLocalServer(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastSafe(String message) {
/* 3182 */     broadCastSafe(message, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastSafe(String message, boolean twit) {
/* 3190 */     broadCastSafe(message, twit, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastSafe(String message, boolean twit, byte messageType) {
/* 3195 */     MessageServer.broadCastSafe(message, messageType);
/* 3196 */     if (twit) {
/* 3197 */       twitLocalServer(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAlert(String message) {
/* 3205 */     broadCastAlert(message, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAlert(String message, boolean twit) {
/* 3213 */     broadCastAlert(message, twit, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadCastAlert(String message, boolean twit, byte messageType) {
/* 3218 */     MessageServer.broadCastAlert(message, messageType);
/* 3219 */     if (twit) {
/* 3220 */       twitLocalServer(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, int tileDist, boolean combat) {
/* 3228 */     MessageServer.broadCastAction(message, performer, null, tileDist, combat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, int tileDist) {
/* 3236 */     MessageServer.broadCastAction(message, performer, tileDist);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, Creature receiver, int tileDist) {
/* 3244 */     MessageServer.broadCastAction(message, performer, receiver, tileDist);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastAction(String message, Creature performer, Creature receiver, int tileDist, boolean combat) {
/* 3253 */     MessageServer.broadCastAction(message, performer, receiver, tileDist, combat);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastMessage(String message, int tilex, int tiley, boolean surfaced, int tiledistance) {
/* 3262 */     MessageServer.broadCastMessage(message, tilex, tiley, surfaced, tiledistance);
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadCaveMesh() {
/* 3267 */     long start = System.nanoTime();
/*      */     
/*      */     try {
/* 3270 */       caveMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "map_cave.map");
/*      */     }
/* 3272 */     catch (IOException iex) {
/*      */       
/* 3274 */       logger.log(Level.SEVERE, "Cavemap doesn't exist... initializing... size will be " + (1 << Constants.meshSize) + "!");
/*      */ 
/*      */       
/*      */       try {
/* 3278 */         Constants.caveImg = true;
/* 3279 */         int msize = (1 << Constants.meshSize) * (1 << Constants.meshSize);
/* 3280 */         int[] caveArr = new int[msize];
/* 3281 */         for (int x = 0; x < msize; x++) {
/*      */           
/* 3283 */           if (x % 100000 == 0)
/* 3284 */             logger.log(Level.INFO, "Created " + x + " tiles out of " + msize); 
/* 3285 */           caveArr[x] = Tiles.encode((short)-100, 
/* 3286 */               TileRockBehaviour.prospect(x & (1 << Constants.meshSize) - 1, x >> Constants.meshSize, false), (byte)0);
/*      */         } 
/*      */         
/* 3289 */         caveMesh = MeshIO.createMap(ServerDirInfo.getFileDBPath() + "map_cave.map", Constants.meshSize, caveArr);
/*      */       }
/* 3291 */       catch (IOException iox) {
/*      */         
/* 3293 */         logger.log(Level.INFO, "Failed to initialize caves. Exiting. " + iox.getMessage(), iox);
/* 3294 */         System.exit(0);
/*      */       }
/* 3296 */       catch (ArrayIndexOutOfBoundsException ex2) {
/*      */         
/* 3298 */         logger.log(Level.WARNING, "Failed to initialize caves. Exiting. " + ex2.getMessage(), ex2);
/* 3299 */         System.exit(0);
/*      */       }
/* 3301 */       catch (Exception ex) {
/*      */         
/* 3303 */         logger.log(Level.WARNING, "Failed to initialize caves. Exiting. " + ex.getMessage(), ex);
/* 3304 */         System.exit(0);
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 3309 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 3310 */       logger.info("Loading cave mesh, size: " + caveMesh.getSize() + " took " + lElapsedTime + " ms");
/*      */     } 
/* 3312 */     if (Constants.reprospect)
/* 3313 */       TileRockBehaviour.reProspect(); 
/* 3314 */     if (Constants.caveImg) {
/*      */       
/* 3316 */       ZonesUtility.saveAsImg(caveMesh);
/* 3317 */       logger.log(Level.INFO, "Saved cave mesh as img");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadWorldMesh() {
/* 3323 */     long start = System.nanoTime();
/*      */     
/*      */     try {
/* 3326 */       surfaceMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "top_layer.map");
/*      */     }
/* 3328 */     catch (IOException iex) {
/*      */       
/* 3330 */       logger.log(Level.SEVERE, "Worldmap " + ServerDirInfo.getFileDBPath() + "top_layer.map doesn't exist.. Shutting down..", iex);
/*      */       
/* 3332 */       System.exit(0);
/*      */     }
/*      */     finally {
/*      */       
/* 3336 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 3337 */       logger.info("Loading world mesh, size: " + surfaceMesh.getSize() + " took " + lElapsedTime + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadRockMesh() {
/* 3343 */     long start = System.nanoTime();
/*      */     
/*      */     try {
/* 3346 */       rockMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "rock_layer.map");
/*      */     }
/* 3348 */     catch (IOException iex) {
/*      */       
/* 3350 */       logger.log(Level.SEVERE, "Worldmap " + ServerDirInfo.getFileDBPath() + "rock_layer.map doesn't exist.. Shutting down..", iex);
/*      */       
/* 3352 */       System.exit(0);
/*      */     }
/*      */     finally {
/*      */       
/* 3356 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 3357 */       logger.info("Loading rock mesh, size: " + rockMesh.getSize() + " took " + lElapsedTime + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getCaveResource(int tilex, int tiley) {
/* 3363 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3364 */     int toReturn = value >> 16 & 0xFFFF;
/* 3365 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setCaveResource(int tilex, int tiley, int newValue) {
/* 3370 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3371 */     if ((value >> 16 & 0xFFFF) != newValue) {
/* 3372 */       resourceMesh.setTile(tilex, tiley, ((newValue & 0xFFFF) << 16) + (value & 0xFFFF));
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getWorldResource(int tilex, int tiley) {
/* 3377 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3378 */     int toReturn = value & 0xFFFF;
/* 3379 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setWorldResource(int tilex, int tiley, int newValue) {
/* 3384 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3385 */     if ((value & 0xFFFF) != newValue) {
/* 3386 */       resourceMesh.setTile(tilex, tiley, (value & 0xFFFF0000) + (newValue & 0xFFFF));
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getDigCount(int tilex, int tiley) {
/* 3391 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3392 */     int digCount = value & 0xFF;
/* 3393 */     return digCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setDigCount(int tilex, int tiley, int newValue) {
/* 3398 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3399 */     if ((value & 0xFF) != newValue) {
/* 3400 */       resourceMesh.setTile(tilex, tiley, (value & 0xFFFFFF00) + (newValue & 0xFF));
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getPotionQLCount(int tilex, int tiley) {
/* 3405 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3406 */     int pQLCount = (value & 0xFF00) >> 8;
/* 3407 */     if (pQLCount == 255)
/* 3408 */       return 0; 
/* 3409 */     return pQLCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setPotionQLCount(int tilex, int tiley, int newValue) {
/* 3414 */     int pQLCount = newValue << 8;
/* 3415 */     int value = resourceMesh.getTile(tilex, tiley);
/* 3416 */     if ((value & 0xFF00) != pQLCount) {
/* 3417 */       resourceMesh.setTile(tilex, tiley, (value & 0xFFFF00FF) + (pQLCount & 0xFF00));
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isBotanizable(int tilex, int tiley) {
/* 3422 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3423 */     return ((value & 0x80) == 128);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setBotanizable(int tilex, int tiley, boolean isBotanizable) {
/* 3428 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3429 */     int newValue = isBotanizable ? 128 : 0;
/* 3430 */     if ((value & 0x80) != newValue) {
/* 3431 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFF7F | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isForagable(int tilex, int tiley) {
/* 3436 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3437 */     return ((value & 0x40) == 64);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setForagable(int tilex, int tiley, boolean isForagable) {
/* 3442 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3443 */     int newValue = isForagable ? 64 : 0;
/* 3444 */     if ((value & 0x40) != newValue) {
/* 3445 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFFBF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isGatherable(int tilex, int tiley) {
/* 3450 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3451 */     return ((value & 0x20) == 32);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setGatherable(int tilex, int tiley, boolean isGather) {
/* 3456 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3457 */     int newValue = isGather ? 32 : 0;
/* 3458 */     if ((value & 0x20) != newValue) {
/* 3459 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFFDF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isInvestigatable(int tilex, int tiley) {
/* 3464 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3465 */     return ((value & 0x10) == 16);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setInvestigatable(int tilex, int tiley, boolean isInvestigate) {
/* 3470 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3471 */     int newValue = isInvestigate ? 16 : 0;
/* 3472 */     if ((value & 0x10) != newValue) {
/* 3473 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFFEF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isCheckHive(int tilex, int tiley) {
/* 3478 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3479 */     return ((value & 0x400) == 1024);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setCheckHive(int tilex, int tiley, boolean isChecked) {
/* 3484 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3485 */     int newValue = isChecked ? 1024 : 0;
/* 3486 */     if ((value & 0x400) != newValue) {
/* 3487 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFBFF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean wasTransformed(int tilex, int tiley) {
/* 3492 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3493 */     return ((value & 0x100) == 256);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setTransformed(int tilex, int tiley, boolean isTransformed) {
/* 3498 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3499 */     int newValue = isTransformed ? 256 : 0;
/* 3500 */     if ((value & 0x100) != newValue) {
/* 3501 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFEFF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isBeingTransformed(int tilex, int tiley) {
/* 3506 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3507 */     return ((value & 0x200) == 512);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setBeingTransformed(int tilex, int tiley, boolean isTransformed) {
/* 3512 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3513 */     int newValue = isTransformed ? 512 : 0;
/* 3514 */     if ((value & 0x200) != newValue) {
/* 3515 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFFDFF | newValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean hasGrubs(int tilex, int tiley) {
/* 3520 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3521 */     return ((value & 0x800) == 2048);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setGrubs(int tilex, int tiley, boolean grubs) {
/* 3526 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3527 */     int newValue = grubs ? 2048 : 0;
/* 3528 */     if ((value & 0x800) != newValue) {
/* 3529 */       flagsMesh.setTile(tilex, tiley, value & 0xFFFFF7FF | newValue);
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
/*      */   public static byte getClientSurfaceFlags(int tilex, int tiley) {
/* 3542 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3543 */     return (byte)(value & 0xFF);
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
/*      */   public static byte getServerSurfaceFlags(int tilex, int tiley) {
/* 3556 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3557 */     return (byte)(value >>> 8 & 0xFF);
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
/*      */   public static byte getServerCaveFlags(int tilex, int tiley) {
/* 3570 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3571 */     return (byte)(value >>> 24 & 0xFF);
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
/*      */   public static byte getClientCaveFlags(int tilex, int tiley) {
/* 3584 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3585 */     return (byte)(value >>> 16 & 0xFF);
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
/*      */   public static void setServerCaveFlags(int tilex, int tiley, byte newByte) {
/* 3597 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3598 */     flagsMesh.setTile(tilex, tiley, value & 0xFFFFFF | (newByte & 0xFF) << 24);
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
/*      */   public static void setClientCaveFlags(int tilex, int tiley, byte newByte) {
/* 3610 */     int value = flagsMesh.getTile(tilex, tiley);
/* 3611 */     flagsMesh.setTile(tilex, tiley, value & 0xFF00FFFF | (newByte & 0xFF) << 16);
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
/*      */   public static void setSurfaceTile(@Nonnull TilePos tilePos, short newHeight, byte newTileType, byte newTileData) {
/* 3625 */     setSurfaceTile(tilePos.x, tilePos.y, newHeight, newTileType, newTileData);
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
/*      */   public static void setSurfaceTile(int tilex, int tiley, short newHeight, byte newTileType, byte newTileData) {
/* 3640 */     int oldTile = surfaceMesh.getTile(tilex, tiley);
/* 3641 */     byte oldType = Tiles.decodeType(oldTile);
/* 3642 */     if (oldType != newTileType)
/* 3643 */       modifyFlagsByTileType(tilex, tiley, newTileType); 
/* 3644 */     surfaceMesh.setTile(tilex, tiley, Tiles.encode(newHeight, newTileType, newTileData));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void modifyFlagsByTileType(int tilex, int tiley, byte newTileType) {
/* 3651 */     Tiles.Tile theNewTile = Tiles.getTile(newTileType);
/*      */     
/* 3653 */     if (!theNewTile.canBotanize())
/*      */     {
/* 3655 */       setBotanizable(tilex, tiley, false);
/*      */     }
/*      */     
/* 3658 */     if (!theNewTile.canForage())
/*      */     {
/* 3660 */       setForagable(tilex, tiley, false);
/*      */     }
/* 3662 */     setGatherable(tilex, tiley, false);
/*      */     
/* 3664 */     setBeingTransformed(tilex, tiley, false);
/* 3665 */     setTransformed(tilex, tiley, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canBotanize(byte type) {
/* 3670 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_MARSH.id || type == Tiles.Tile.TILE_MOSS.id || type == Tiles.Tile.TILE_PEAT.id || 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3675 */       Tiles.isNormalBush(type) || 
/* 3676 */       Tiles.isNormalTree(type))
/* 3677 */       return true; 
/* 3678 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canForage(byte type) {
/* 3683 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_TUNDRA.id || type == Tiles.Tile.TILE_MARSH.id || 
/*      */ 
/*      */ 
/*      */       
/* 3687 */       Tiles.isNormalBush(type) || 
/* 3688 */       Tiles.isNormalTree(type))
/* 3689 */       return true; 
/* 3690 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canBearFruit(byte type) {
/* 3695 */     if (Tiles.isTree(type) || Tiles.isBush(type))
/* 3696 */       return true; 
/* 3697 */     return false;
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
/*      */   public void shutDown(String aReason, Throwable aCause) {
/*      */     try {
/* 3713 */       logger.log(Level.INFO, "Shutting down the server - reason: " + aReason);
/* 3714 */       logger.log(Level.INFO, "Shutting down the server - cause: ", aCause);
/*      */     }
/*      */     finally {
/*      */       
/* 3718 */       shutDown();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void shutDown() {
/* 3724 */     if (ServerProperties.getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward)) {
/* 3725 */       UPNPService.shutdown();
/*      */     }
/* 3727 */     Creatures.getInstance().shutDownPolltask();
/* 3728 */     Creature.shutDownPathFinders();
/* 3729 */     logger.log(Level.INFO, "Shutting down at: ", new Exception());
/*      */ 
/*      */     
/* 3732 */     if (this.highwayFinderThread != null) {
/*      */       
/* 3734 */       logger.info("Shutting down - Stopping HighwayFinder");
/* 3735 */       this.highwayFinderThread.shouldStop();
/*      */     } 
/* 3737 */     ServerProjectile.clear();
/* 3738 */     logger.info("Shutting down - Polling Battles");
/* 3739 */     if (Constants.isGameServer)
/*      */     {
/* 3741 */       Battles.poll(true);
/*      */     }
/* 3743 */     Zones.saveProtectedTiles();
/* 3744 */     logger.info("Shutting down - Saving Players");
/* 3745 */     Players.getInstance().savePlayersAtShutdown();
/*      */ 
/*      */     
/* 3748 */     logger.info("Shutting down - Clearing Item Database Batches");
/* 3749 */     DbItem.clearBatches();
/*      */     
/* 3751 */     logger.info("Shutting down - Saving Creatures");
/* 3752 */     logger.info("Shutting down - Clearing Creature Database Batches");
/* 3753 */     for (Creature c : Creatures.getInstance().getCreatures()) {
/*      */       
/* 3755 */       if (c.getStatus().getPosition() != null && c.getStatus().getPosition().isChanged())
/*      */         
/*      */         try {
/* 3758 */           c.getStatus().savePosition(c.getWurmId(), false, c.getStatus().getZoneId(), true);
/*      */         }
/* 3760 */         catch (IOException iox) {
/*      */           
/* 3762 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         }  
/*      */     } 
/* 3765 */     if (Constants.useScheduledExecutorToUpdateCreaturePositionInDatabase)
/*      */     {
/* 3767 */       CreaturePos.getCreatureDbPosUpdater().saveImmediately();
/*      */     }
/* 3769 */     CreaturePos.clearBatches();
/* 3770 */     logger.info("Shutting down - Saving all creatures");
/*      */     
/* 3772 */     Creatures.getInstance().saveCreatures();
/* 3773 */     logger.info("Shutting down - Saving All Zones");
/* 3774 */     Zones.saveAllZones();
/* 3775 */     if (this.scheduledExecutorService != null && !this.scheduledExecutorService.isShutdown())
/*      */     {
/* 3777 */       this.scheduledExecutorService.shutdown();
/*      */     }
/* 3779 */     logger.info("Shutting down - Saving Surface Mesh");
/*      */     
/*      */     try {
/* 3782 */       surfaceMesh.saveAll();
/* 3783 */       surfaceMesh.close();
/*      */     }
/* 3785 */     catch (IOException iox) {
/*      */       
/* 3787 */       logger.log(Level.WARNING, "Failed to save surfacemesh!", iox);
/*      */     } 
/* 3789 */     logger.info("Shutting down - Saving Rock Mesh");
/*      */     
/*      */     try {
/* 3792 */       rockMesh.saveAll();
/* 3793 */       rockMesh.close();
/*      */     }
/* 3795 */     catch (IOException iox) {
/*      */       
/* 3797 */       logger.log(Level.WARNING, "Failed to save rockmesh!", iox);
/*      */     } 
/* 3799 */     logger.info("Shutting down - Saving Cave Mesh");
/*      */     
/*      */     try {
/* 3802 */       caveMesh.saveAll();
/* 3803 */       caveMesh.close();
/*      */     }
/* 3805 */     catch (IOException iox) {
/*      */       
/* 3807 */       logger.log(Level.WARNING, "Failed to save cavemesh!", iox);
/*      */     } 
/* 3809 */     logger.info("Shutting down - Saving Resource Mesh");
/*      */     
/*      */     try {
/* 3812 */       resourceMesh.saveAll();
/* 3813 */       resourceMesh.close();
/*      */     }
/* 3815 */     catch (IOException iox) {
/*      */       
/* 3817 */       logger.log(Level.WARNING, "Failed to save resourcemesh!", iox);
/*      */     } 
/* 3819 */     logger.info("Shutting down - Saving Flags Mesh");
/*      */     
/*      */     try {
/* 3822 */       flagsMesh.saveAll();
/* 3823 */       flagsMesh.close();
/*      */     }
/* 3825 */     catch (IOException iox) {
/*      */       
/* 3827 */       logger.log(Level.WARNING, "Failed to save flagsmesh!", iox);
/*      */     } 
/* 3829 */     if (this.waterThread != null) {
/*      */       
/* 3831 */       logger.info("Shutting down - Saving Water Mesh");
/* 3832 */       this.waterThread.shouldStop = true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3839 */     logger.info("Shutting down - Saving Constants");
/* 3840 */     Constants.crashed = false;
/* 3841 */     Constants.save();
/* 3842 */     logger.info("Shutting down - Saving WurmID Numbers");
/* 3843 */     WurmId.updateNumbers();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3853 */     this.steamHandler.closeServer();
/*      */     
/* 3855 */     logger.info("Shutting down - Closing Database Connections");
/* 3856 */     DbConnector.closeAll();
/* 3857 */     logger.log(Level.INFO, "The server shut down nicely. Wurmcalendar time is " + WurmCalendar.currentTime);
/* 3858 */     System.exit(0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadResourceMesh() {
/* 3863 */     long start = System.nanoTime();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3873 */       resourceMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "resources.map");
/*      */     }
/* 3875 */     catch (IOException iex) {
/*      */       
/* 3877 */       logger.log(Level.INFO, "resources doesn't exist.. creating..");
/* 3878 */       int[] resourceArr = new int[(1 << Constants.meshSize) * (1 << Constants.meshSize)];
/* 3879 */       for (int x = 0; x < (1 << Constants.meshSize) * (1 << Constants.meshSize); x++) {
/* 3880 */         resourceArr[x] = -1;
/*      */       }
/*      */       try {
/* 3883 */         resourceMesh = MeshIO.createMap(ServerDirInfo.getFileDBPath() + "resources.map", Constants.meshSize, resourceArr);
/*      */       }
/* 3885 */       catch (IOException iox) {
/*      */         
/* 3887 */         logger.log(Level.SEVERE, "Failed to create resources. Exiting.", iox);
/* 3888 */         System.exit(0);
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 3893 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 3894 */       logger.info("Loading resource mesh, size: " + resourceMesh.getSize() + " took " + lElapsedTime + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadFlagsMesh() {
/* 3900 */     long start = System.nanoTime();
/*      */     
/*      */     try {
/* 3903 */       flagsMesh = MeshIO.open(ServerDirInfo.getFileDBPath() + "flags.map");
/*      */       
/* 3905 */       int first = flagsMesh.getTile(0, 0);
/* 3906 */       if ((first & 0xFFFFFF00) == -256) {
/*      */ 
/*      */         
/* 3909 */         logger.log(Level.INFO, "converting flags.");
/* 3910 */         for (int x = 0; x < 1 << Constants.meshSize; x++) {
/*      */           
/* 3912 */           for (int y = 0; y < 1 << Constants.meshSize; y++)
/*      */           {
/* 3914 */             int value = flagsMesh.getTile(x, y) & 0xFF;
/*      */             
/* 3916 */             int serverSurfaceFlag = value & 0xF;
/* 3917 */             value |= serverSurfaceFlag << 8;
/*      */             
/* 3919 */             value &= 0xFFF0;
/* 3920 */             flagsMesh.setTile(x, y, value);
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/* 3925 */     } catch (IOException iex) {
/*      */       
/* 3927 */       logger.log(Level.INFO, "flags doesn't exist.. creating..");
/* 3928 */       int[] resourceArr = new int[(1 << Constants.meshSize) * (1 << Constants.meshSize)];
/* 3929 */       for (int x = 0; x < (1 << Constants.meshSize) * (1 << Constants.meshSize); x++)
/*      */       {
/* 3931 */         resourceArr[x] = 0;
/*      */       }
/*      */       try {
/* 3934 */         flagsMesh = MeshIO.createMap(ServerDirInfo.getFileDBPath() + "flags.map", Constants.meshSize, resourceArr);
/* 3935 */         this.needSeeds = true;
/*      */       }
/* 3937 */       catch (IOException iox) {
/*      */         
/* 3939 */         logger.log(Level.SEVERE, "Failed to create flags. Exiting.", iox);
/* 3940 */         System.exit(0);
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 3945 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 3946 */       logger.info("Loading flags mesh, size: " + flagsMesh.getSize() + " took " + lElapsedTime + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addPendingAward(PendingAward award) {
/* 3952 */     pendingAwards.add(award);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void pollPendingAwards() {
/* 3957 */     for (PendingAward award : pendingAwards)
/*      */     {
/* 3959 */       award.award();
/*      */     }
/* 3961 */     pendingAwards.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getTimeFor(long aTime) {
/* 3970 */     String times = "";
/* 3971 */     if (aTime < 60000L) {
/*      */       
/* 3973 */       long secs = aTime / 1000L;
/* 3974 */       times = times + secs + ((secs == 1L) ? " second" : " seconds");
/*      */     }
/*      */     else {
/*      */       
/* 3978 */       long daysleft = aTime / 86400000L;
/*      */       
/* 3980 */       long hoursleft = (aTime - daysleft * 86400000L) / 3600000L;
/* 3981 */       long minutesleft = (aTime - daysleft * 86400000L - hoursleft * 3600000L) / 60000L;
/*      */       
/* 3983 */       if (daysleft > 0L)
/* 3984 */         times = times + daysleft + ((daysleft == 1L) ? " day" : " days"); 
/* 3985 */       if (hoursleft > 0L) {
/*      */         
/* 3987 */         String aft = "";
/* 3988 */         if (daysleft > 0L && minutesleft > 0L) {
/*      */           
/* 3990 */           times = times + ", ";
/* 3991 */           aft = aft + " and ";
/*      */         }
/* 3993 */         else if (daysleft > 0L) {
/*      */           
/* 3995 */           times = times + " and ";
/*      */         }
/* 3997 */         else if (minutesleft > 0L) {
/* 3998 */           aft = aft + " and ";
/* 3999 */         }  times = times + hoursleft + ((hoursleft == 1L) ? " hour" : " hours") + aft;
/*      */       } 
/* 4001 */       if (minutesleft > 0L) {
/*      */         
/* 4003 */         String aft = "";
/* 4004 */         if (daysleft > 0L && hoursleft == 0L)
/* 4005 */           aft = " and "; 
/* 4006 */         times = times + aft + minutesleft + ((minutesleft == 1L) ? " minute" : " minutes");
/*      */       } 
/*      */     } 
/* 4009 */     if (times.length() == 0)
/* 4010 */       times = "nothing"; 
/* 4011 */     return times;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void transaction(long itemId, long oldownerid, long newownerid, String reason, long value) {
/* 4017 */     Economy.getEconomy().transaction(itemId, oldownerid, newownerid, reason, value);
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
/*      */   private void pollIntraCommands() {
/*      */     try {
/* 4030 */       if (INTRA_COMMANDS_RW_LOCK.writeLock().tryLock()) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 4035 */           IntraCommand[] comms = this.intraCommands.<IntraCommand>toArray(new IntraCommand[this.intraCommands.size()]);
/* 4036 */           for (int i = 0; i < comms.length; i++)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4044 */             if (i < 40)
/*      */             {
/* 4046 */               if (comms[i].poll()) {
/* 4047 */                 this.intraCommands.remove(comms[i]);
/*      */               
/*      */               }
/*      */             
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*      */         finally {
/*      */           
/* 4057 */           INTRA_COMMANDS_RW_LOCK.writeLock().unlock();
/*      */         } 
/*      */       }
/*      */       
/* 4061 */       MoneyTransfer[] transfers = (MoneyTransfer[])MoneyTransfer.transfers.toArray((Object[])new MoneyTransfer[MoneyTransfer.transfers.size()]);
/* 4062 */       for (int x = 0; x < transfers.length; x++) {
/*      */         
/* 4064 */         if (transfers[x].poll())
/*      */         {
/* 4066 */           if ((transfers[x]).deleted || (transfers[x]).pollTimes > 500) {
/*      */             
/* 4068 */             logger.log(Level.INFO, "Polling MoneyTransfer " + x + " deleted: " + transfers[x]);
/* 4069 */             MoneyTransfer.transfers.remove(transfers[x]);
/*      */           } 
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
/* 4081 */       TimeTransfer[] ttransfers = (TimeTransfer[])TimeTransfer.transfers.toArray((Object[])new TimeTransfer[TimeTransfer.transfers.size()]);
/* 4082 */       for (TimeTransfer lTtransfer : ttransfers) {
/*      */         
/* 4084 */         if (lTtransfer.poll())
/*      */         {
/* 4086 */           if (lTtransfer.deleted) {
/*      */             
/* 4088 */             logger.log(Level.INFO, "Polling tt deleted");
/* 4089 */             TimeTransfer.transfers.remove(lTtransfer);
/*      */           } 
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
/* 4102 */       PasswordTransfer[] ptransfers = (PasswordTransfer[])PasswordTransfer.transfers.toArray((Object[])new PasswordTransfer[PasswordTransfer.transfers.size()]);
/* 4103 */       for (PasswordTransfer lPtransfer : ptransfers)
/*      */       {
/* 4105 */         if (lPtransfer.poll())
/*      */         {
/* 4107 */           if (lPtransfer.deleted)
/*      */           {
/*      */             
/* 4110 */             PasswordTransfer.transfers.remove(lPtransfer);
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 4122 */     catch (Exception ex) {
/*      */       
/* 4124 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addIntraCommand(IntraCommand command) {
/* 4130 */     INTRA_COMMANDS_RW_LOCK.writeLock().lock();
/*      */     
/*      */     try {
/* 4133 */       this.intraCommands.add(command);
/*      */     }
/*      */     finally {
/*      */       
/* 4137 */       INTRA_COMMANDS_RW_LOCK.writeLock().unlock();
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
/*      */   public void addWebCommand(WebCommand command) {
/*      */     try {
/* 4161 */       WEBCOMMANDS_RW_LOCK.writeLock().lock();
/* 4162 */       webcommands.add(command);
/*      */     }
/*      */     finally {
/*      */       
/* 4166 */       WEBCOMMANDS_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pollWebCommands() {
/*      */     try {
/* 4175 */       WEBCOMMANDS_RW_LOCK.writeLock().lock();
/* 4176 */       for (WebCommand wc : webcommands)
/*      */       {
/* 4178 */         wc.execute();
/*      */       }
/* 4180 */       webcommands.clear();
/*      */     }
/*      */     finally {
/*      */       
/* 4184 */       WEBCOMMANDS_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTerraformingTask(TerraformingTask task) {
/*      */     try {
/* 4192 */       TERRAFORMINGTASKS_RW_LOCK.writeLock().lock();
/* 4193 */       terraformingTasks.add(task);
/*      */     }
/*      */     finally {
/*      */       
/* 4197 */       TERRAFORMINGTASKS_RW_LOCK.writeLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pollTerraformingTasks() {
/*      */     try {
/* 4206 */       TERRAFORMINGTASKS_RW_LOCK.writeLock().lock();
/* 4207 */       TerraformingTask[] tasks = terraformingTasks.<TerraformingTask>toArray(new TerraformingTask[terraformingTasks.size()]);
/* 4208 */       for (TerraformingTask task : tasks) {
/*      */         
/* 4210 */         if (task.poll()) {
/* 4211 */           terraformingTasks.remove(task);
/*      */         }
/*      */       } 
/*      */     } finally {
/*      */       
/* 4216 */       TERRAFORMINGTASKS_RW_LOCK.writeLock().unlock();
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
/*      */   public byte[] getExternalIp() {
/* 4229 */     return this.externalIp;
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
/*      */   public byte[] getInternalIp() {
/* 4241 */     return this.internalIp;
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
/*      */   public int getIntraServerPort() {
/* 4253 */     return Integer.parseInt(Servers.localServer.INTRASERVERPORT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short getMolRehanX() {
/* 4263 */     return molRehanX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setMolRehanX(short aMolRehanX) {
/* 4274 */     molRehanX = aMolRehanX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short getMolRehanY() {
/* 4284 */     return molRehanY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setMolRehanY(short aMolRehanY) {
/* 4295 */     molRehanY = aMolRehanY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void incrementOldPremiums(String name) {
/* 4303 */     if (System.currentTimeMillis() - lastResetOldPremiums > 10800000L) {
/*      */       
/* 4305 */       oldPremiums = 0;
/* 4306 */       lastResetOldPremiums = System.currentTimeMillis();
/*      */     } 
/* 4308 */     oldPremiums++;
/* 4309 */     if (!appointedSixThousand && (PlayerInfoFactory.getNumberOfPayingPlayers() + 1) % 1000 == 0) {
/*      */       
/* 4311 */       logger.log(Level.INFO, name + " IS THE NUMBER " + (PlayerInfoFactory.getNumberOfPayingPlayers() + 1) + " PAYING PLAYER");
/*      */       
/* 4313 */       appointedSixThousand = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getStartTime() {
/* 4324 */     return startTime;
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
/*      */   public static long getMillisToShutDown() {
/* 4336 */     return millisToShutDown;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShutdownReason() {
/* 4346 */     return shutdownReason;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Weather getWeather() {
/* 4356 */     return weather;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getCombatCounter() {
/* 4366 */     return combatCounter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void incrementCombatCounter() {
/* 4374 */     combatCounter++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getSecondsUptime() {
/* 4384 */     return secondsUptime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void incrementSecondsUptime() {
/* 4392 */     secondsUptime++;
/* 4393 */     Players.getInstance().tickSecond();
/* 4394 */     lastLagticks = lagticks;
/*      */     
/* 4396 */     if (lastLagticks > 0.0F) {
/* 4397 */       lagMoveModifier = (int)Math.max(10.0F, lastLagticks / 30.0F * 24.0F);
/*      */     } else {
/* 4399 */       lagMoveModifier = 0;
/* 4400 */     }  lagticks = 0;
/* 4401 */     if (WurmCalendar.isNewYear1()) {
/*      */       
/* 4403 */       logger.log(Level.INFO, "IT's NEW YEAR");
/* 4404 */       if (secondsUptime % 20 == 0)
/*      */       {
/* 4406 */         if (rand.nextBoolean()) {
/*      */           
/* 4408 */           Effect globalEffect = EffectFactory.getInstance().createSpawnEff(WurmId.getNextTempItemId(), rand
/* 4409 */               .nextFloat() * Zones.worldMeterSizeX, rand.nextFloat() * Zones.worldMeterSizeY, 0.0F, true);
/*      */ 
/*      */           
/* 4412 */           newYearEffects.add(Integer.valueOf(globalEffect.getId()));
/*      */           
/*      */           try {
/* 4415 */             ItemFactory.createItem(52, rand.nextFloat() * 90.0F + 1.0F, globalEffect
/* 4416 */                 .getPosX(), globalEffect.getPosY(), globalEffect.getPosZ(), true, (byte)8, 
/* 4417 */                 getRandomRarityNotCommon(), -10L, "", (byte)0);
/*      */           }
/* 4419 */           catch (Exception exception) {}
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 4426 */           Effect globalEffect = EffectFactory.getInstance().createChristmasEff(WurmId.getNextTempItemId(), rand
/* 4427 */               .nextFloat() * Zones.worldMeterSizeX, rand.nextFloat() * Zones.worldMeterSizeY, 0.0F, true);
/*      */           
/* 4429 */           newYearEffects.add(Integer.valueOf(globalEffect.getId()));
/*      */           
/*      */           try {
/* 4432 */             ItemFactory.createItem(52, rand.nextFloat() * 90.0F + 1.0F, globalEffect
/* 4433 */                 .getPosX(), globalEffect.getPosY(), globalEffect.getPosZ(), true, (byte)8, 
/* 4434 */                 getRandomRarityNotCommon(), -10L, "", (byte)0);
/*      */           }
/* 4436 */           catch (Exception exception) {}
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 4442 */       if (secondsUptime % 11 == 0)
/*      */       {
/* 4444 */         Zones.sendNewYear();
/*      */       }
/*      */     }
/* 4447 */     else if (WurmCalendar.isAfterNewYear1()) {
/*      */       
/* 4449 */       if (newYearEffects != null && !newYearEffects.isEmpty())
/*      */       {
/* 4451 */         for (Integer l : newYearEffects)
/*      */         {
/* 4453 */           EffectFactory.getInstance().deleteEffect(l.intValue());
/*      */         }
/*      */       }
/* 4456 */       if (newYearEffects != null)
/*      */       {
/* 4458 */         newYearEffects.clear();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte getRandomRarityNotCommon() {
/* 4465 */     if (rand.nextFloat() * 10000.0F <= 1.0F)
/* 4466 */       return 3; 
/* 4467 */     if (rand.nextInt(100) <= 0)
/* 4468 */       return 2; 
/* 4469 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void startSendWeatherThread() {
/* 4474 */     (new Thread()
/*      */       {
/*      */         
/*      */         public void run()
/*      */         {
/* 4479 */           Servers.sendWeather(Server.weather.getWindRotation(), Server.weather.getWindPower(), Server.weather.getWindDir());
/*      */         }
/* 4482 */       }).start();
/*      */     
/* 4484 */     Players.getInstance().sendWeather();
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
/*      */   private static void addShutdownHook() {
/* 4496 */     logger.info("Adding Shutdown Hook");
/*      */     
/* 4498 */     Runtime.getRuntime().addShutdownHook(new Thread("WurmServerShutdownHook-Thread")
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public void run()
/*      */           {
/* 4506 */             Server.logger.info("\nWurm Server Shutdown hook is running\n");
/*      */             
/* 4508 */             DbConnector.closeAll();
/* 4509 */             ServerLauncher.stopLoggers();
/* 4510 */             TradingWindow.stopLoggers();
/* 4511 */             Players.stopLoggers();
/*      */           }
/*      */         });
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
/*      */   public static final double getModifiedFloatEffect(double eff) {
/* 4540 */     if (EpicServer) {
/*      */       
/* 4542 */       double modEff = 0.0D;
/*      */       
/* 4544 */       if (eff >= 1.0D) {
/*      */         
/* 4546 */         if (eff <= 70.0D) {
/* 4547 */           modEff = 1.3571428060531616D * eff;
/*      */         } else {
/* 4549 */           modEff = 0.949999988079071D + (eff - 70.0D) * 0.1666666716337204D;
/*      */         } 
/*      */       } else {
/* 4552 */         modEff = 1.0D - (1.0D - eff) * (1.0D - eff);
/* 4553 */       }  return modEff;
/*      */     } 
/* 4555 */     return eff;
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
/*      */   public static final double getModifiedPercentageEffect(double eff) {
/* 4567 */     if (EpicServer || ChallengeServer) {
/*      */       
/* 4569 */       double modEff = 0.0D;
/*      */ 
/*      */       
/* 4572 */       if (eff >= 100.0D) {
/*      */         
/* 4574 */         if (eff <= 7000.0D) {
/* 4575 */           modEff = 1.3571428060531616D * eff;
/*      */         } else {
/* 4577 */           modEff = 95.0D + (eff - 7000.0D) * 0.1666666716337204D;
/*      */         } 
/*      */       } else {
/* 4580 */         modEff = (10000.0D - (100.0D - eff) * (100.0D - eff)) / 100.0D;
/* 4581 */       }  return modEff;
/*      */     } 
/* 4583 */     return eff;
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
/*      */   public static final double getBuffedQualityEffect(double eff) {
/* 4597 */     if (eff < 1.0D) {
/* 4598 */       return Math.max(0.05D, 1.0D - (1.0D - eff) * (1.0D - eff));
/*      */     }
/*      */     
/* 4601 */     double base = 2.0D;
/* 4602 */     double pow1 = 1.3D;
/* 4603 */     double pow2 = 3.0D;
/* 4604 */     double newPower = 1.0D + base * (1.0D - Math.pow(2.0D, -Math.pow(eff - 1.0D, pow1) / pow2));
/* 4605 */     return newPower;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final HexMap getEpicMap() {
/* 4610 */     return epicMap;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastEpicEvent(String event) {
/* 4616 */     Servers.localServer.createChampTwit(event);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadCastEpicWinCondition(String scenarioname, String scenarioQuest) {
/* 4622 */     Servers.localServer.createChampTwit(scenarioname + " has begun. " + scenarioQuest);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasThunderMode() {
/* 4627 */     return this.thunderMode;
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
/*      */   public final short getLowDirtHeight(int x, int y) {
/* 4639 */     Integer xy = Integer.valueOf(x | y << Constants.meshSize);
/* 4640 */     if (lowDirtHeight.containsKey(xy))
/*      */     {
/* 4642 */       return ((Short)lowDirtHeight.get(xy)).shortValue();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4647 */     return Tiles.decodeHeight(surfaceMesh.getTile(x, y));
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
/*      */   public static final boolean isDirtHeightLower(int x, int y, short ht) {
/*      */     short cHt;
/* 4664 */     Integer xy = Integer.valueOf(x | y << Constants.meshSize);
/*      */     
/* 4666 */     if (lowDirtHeight.containsKey(xy)) {
/*      */       
/* 4668 */       cHt = ((Short)lowDirtHeight.get(xy)).shortValue();
/* 4669 */       if (ht < cHt) {
/* 4670 */         lowDirtHeight.put(xy, Short.valueOf(ht));
/*      */       }
/*      */     } else {
/*      */       
/* 4674 */       cHt = Tiles.decodeHeight(surfaceMesh.getTile(x, y));
/* 4675 */       lowDirtHeight.put(xy, Short.valueOf((short)Math.min(cHt, ht)));
/*      */     } 
/* 4677 */     return (ht < cHt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPS() {
/* 4687 */     return this.isPS;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIsPS(boolean ps) {
/* 4692 */     this.isPS = ps;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addGlobalTempEffect(long id, long expiretime) {
/* 4697 */     tempEffects.put(Long.valueOf(id), Long.valueOf(expiretime));
/*      */   }
/*      */ 
/*      */   
/*      */   public static short getMaxHeight() {
/* 4702 */     return surfaceMesh.getMaxHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   public HighwayFinder getHighwayFinderThread() {
/* 4707 */     return this.highwayFinderThread;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Server.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */