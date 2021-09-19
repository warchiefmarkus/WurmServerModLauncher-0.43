/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.combat.CombatConstants;
/*      */ import com.wurmonline.server.creatures.Communicator;
/*      */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.highways.Route;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.steam.SteamId;
/*      */ import java.io.IOException;
/*      */ import java.util.BitSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class PlayerInfo
/*      */   implements MiscConstants, TimeConstants, CombatConstants, Comparable<PlayerInfo>
/*      */ {
/*   72 */   public long lastCreatedHistoryEvent = 0L;
/*      */   
/*   74 */   public int timeToCheckPrem = 61 + Server.rand.nextInt(28000);
/*      */ 
/*      */ 
/*      */   
/*   78 */   String name = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   83 */   String password = "EmptyPassword";
/*      */   
/*   85 */   public long wurmId = -10L;
/*      */   
/*   87 */   public long lastToggledFSleep = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   public long lastLogin = 0L;
/*      */   
/*   94 */   private long lastDeath = 0L;
/*   95 */   public long playingTime = 0L;
/*      */ 
/*      */   
/*   98 */   private static Logger logger = Logger.getLogger(PlayerInfo.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ConcurrentHashMap<Long, Friend> friends;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasLoadedFriends = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<Long> enemies;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   boolean reimbursed = (!WurmCalendar.isChristmas() && !WurmCalendar.isEaster());
/*      */   
/*  122 */   public long plantedSign = System.currentTimeMillis() - 604800000L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean banned = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  132 */   String ipaddress = "";
/*      */ 
/*      */ 
/*      */   
/*      */   SteamId steamId;
/*      */ 
/*      */   
/*  139 */   private Set<Long> ignored = null;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean muted = false;
/*      */ 
/*      */   
/*  146 */   byte power = 0;
/*      */   
/*      */   long paymentExpireDate;
/*      */   
/*  150 */   public long version = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  155 */   int rank = 1000;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  160 */   int maxRank = 1000;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayHearDevTalk = false;
/*      */ 
/*      */   
/*  167 */   public long lastWarned = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  172 */   public int warnings = 0;
/*      */   
/*  174 */   public long lastChangedDeity = System.currentTimeMillis() - 604800000L;
/*      */   
/*  176 */   public byte realdeath = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  181 */   float alignment = 1.0F;
/*      */   
/*  183 */   Deity deity = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  188 */   float faith = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  193 */   float favor = 0.0F;
/*      */   
/*  195 */   Deity god = null;
/*      */   
/*      */   public static final String LOADED_CLASSES_DISCONNECT = "CLASS_CHECK_DISCONNECT";
/*      */   
/*  199 */   public long lastCheated = 0L;
/*      */   
/*  201 */   public int fatigueSecsLeft = 28800;
/*      */   
/*  203 */   public int fatigueSecsToday = 0;
/*      */   
/*  205 */   public int fatigueSecsYesterday = 0;
/*      */   
/*  207 */   public long lastFatigue = System.currentTimeMillis();
/*      */ 
/*      */   
/*      */   public static final int MAX_FATIGUE_SECONDS = 43200;
/*      */   
/*      */   private static final int FATIGUE_INCREASE_TIME = 3600;
/*      */   
/*      */   private static final long FATIGUE_INCREASE_DELAY_PREMIUM = 10800000L;
/*      */   
/*      */   public static final long MINTIME_BETWEEN_CHAMPION = 14515200000L;
/*      */   
/*      */   public boolean dead = false;
/*      */   
/*  220 */   String sessionKey = "";
/*      */   
/*  222 */   public long sessionExpiration = 0L;
/*      */   
/*      */   public byte numFaith;
/*      */   
/*      */   public long lastFaith;
/*      */   
/*      */   protected byte sex;
/*      */   
/*  230 */   public long money = 0L;
/*      */   
/*      */   public boolean climbing = false;
/*      */   
/*      */   protected boolean spamMode = true;
/*      */   
/*  236 */   private long lastGasp = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  243 */   byte changedKingdom = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  248 */   public long MIN_KINGDOM_CHANGE_TIME = 1209600000L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  253 */   public long lastChangedKindom = System.currentTimeMillis() - 1209600000L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  259 */   public long championTimeStamp = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  267 */   public short championPoints = 0;
/*      */   
/*  269 */   public long creationDate = System.currentTimeMillis();
/*      */   
/*  271 */   public String banreason = "";
/*      */   
/*  273 */   public long banexpiry = 0L;
/*      */   
/*  275 */   public long face = 0L;
/*      */   
/*  277 */   protected byte blood = 0;
/*  278 */   public long lastChangedCluster = 0L;
/*      */   
/*  280 */   public short muteTimes = 0;
/*      */   
/*  282 */   public long nextAvailableMute = 0L;
/*      */   
/*  284 */   public long startedReceivingMutes = 0L;
/*      */   
/*  286 */   public short mutesReceived = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  291 */   public int reputation = 100;
/*      */   
/*  293 */   public long lastPolledReputation = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  298 */   Set<Titles.Title> titles = new HashSet<>();
/*      */   
/*  300 */   public Titles.Title title = null;
/*      */   
/*  302 */   public Titles.Title secondTitle = null;
/*      */   
/*  304 */   public String kingdomtitle = "";
/*      */   
/*  306 */   public long pet = -10L;
/*      */   
/*  308 */   public float alcohol = 0.0F;
/*      */   
/*  310 */   public float nicotine = 0.0F;
/*      */   
/*  312 */   public long alcoholAddiction = 0L;
/*      */   
/*  314 */   public long nicotineAddiction = 0L;
/*      */   
/*      */   public boolean mayMute = false;
/*      */   
/*  318 */   public String mutereason = "";
/*      */   
/*  320 */   public long muteexpiry = 0L;
/*      */   
/*      */   public boolean logging = false;
/*      */   
/*      */   public int lastServer;
/*      */   
/*      */   public int currentServer;
/*      */   
/*      */   public boolean loaded = false;
/*      */   
/*  330 */   public long referrer = 0L;
/*      */   
/*  332 */   public String emailAddress = "";
/*      */   
/*  334 */   public long lastLogout = 0L;
/*      */   
/*  336 */   public String pwQuestion = "";
/*      */   
/*  338 */   public String pwAnswer = "";
/*      */   
/*  340 */   public long lastRequestedPassword = 0L;
/*      */   
/*  342 */   long lastChangedVillage = 0L;
/*      */   
/*      */   public boolean isPriest = false;
/*      */   
/*  346 */   public long bed = -10L;
/*      */   
/*  348 */   public int sleep = 0;
/*      */   
/*      */   public boolean frozenSleep = true;
/*      */   
/*      */   public boolean overRideShop = false;
/*      */   
/*      */   public boolean isTheftWarned = false;
/*      */   
/*      */   public boolean noReimbursementLeft = false;
/*      */   
/*      */   public boolean deathProtected = false;
/*      */   
/*  360 */   public byte fightmode = 2;
/*      */   
/*  362 */   public long nextAffinity = 0L;
/*      */   
/*  364 */   public int tutorialLevel = 0;
/*      */   
/*      */   public boolean autoFighting = false;
/*      */   
/*  368 */   public long appointments = 0L;
/*      */   
/*  370 */   public long lastvehicle = -10L;
/*      */   
/*      */   boolean playerAssistant = false;
/*      */   
/*      */   boolean mayAppointPlayerAssistant = false;
/*      */   
/*      */   boolean seesPlayerAssistantWindow = false;
/*      */   
/*      */   protected boolean hasMovedInventory = false;
/*      */   
/*  380 */   public byte priestType = 0;
/*      */   
/*  382 */   public long lastChangedPriestType = 0L;
/*      */   
/*  384 */   byte lastTaggedKindom = 0;
/*      */   
/*  386 */   private final Map<Long, Integer> macroAttackers = new HashMap<>();
/*      */   
/*  388 */   private final Map<Long, Integer> macroArchers = new HashMap<>();
/*      */ 
/*      */   
/*      */   private static final int MAX_MACRO_ATTACKS = 100;
/*      */   
/*  393 */   long lastMovedBetweenKingdom = System.currentTimeMillis();
/*      */   
/*  395 */   public long lastModifiedRank = System.currentTimeMillis();
/*  396 */   public long lastChangedJoat = System.currentTimeMillis();
/*      */   
/*      */   public boolean hasFreeTransfer = false;
/*      */   
/*  400 */   public int lastTriggerEffect = 0;
/*      */   
/*      */   public boolean hasSkillGain = true;
/*      */   
/*  404 */   public float champChanneling = 0.0F;
/*      */   
/*      */   public boolean votedKing = false;
/*      */   
/*  408 */   public int epicServerId = -1;
/*      */   
/*  410 */   public byte epicKingdom = 0;
/*      */   
/*  412 */   public long lastUsedEpicPortal = 0L;
/*      */   
/*  414 */   byte chaosKingdom = 0;
/*      */   
/*  416 */   short hotaWins = 0;
/*      */ 
/*      */   
/*  419 */   protected int karma = 0;
/*      */ 
/*      */   
/*  422 */   protected int maxKarma = 0;
/*      */ 
/*      */   
/*  425 */   protected int totalKarma = 0;
/*      */   
/*      */   public long abilities;
/*      */   
/*  429 */   public int abilityTitle = -1;
/*      */   
/*  431 */   public Awards awards = null;
/*  432 */   protected BitSet abilityBits = new BitSet(64);
/*      */   public long flags;
/*      */   public long flags2;
/*  435 */   protected BitSet flagBits = new BitSet(64);
/*  436 */   protected BitSet flag2Bits = new BitSet(64);
/*  437 */   public int scenarioKarma = 0;
/*      */   
/*  439 */   public byte undeadType = 0;
/*  440 */   public int undeadKills = 0;
/*  441 */   public int undeadPlayerKills = 0;
/*  442 */   public int undeadPlayerSeconds = 0;
/*  443 */   private long moneyToSend = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  449 */   private final ConcurrentHashMap<String, Long> targetPMIds = new ConcurrentHashMap<>();
/*      */   
/*  451 */   private long sessionFlags = 0L;
/*  452 */   protected BitSet sessionFlagBits = new BitSet(64);
/*      */   
/*  454 */   protected String modelName = "Human";
/*      */   
/*  456 */   private long moneyEarnedBySellingLastHour = 0L;
/*      */   
/*  458 */   protected long moneyEarnedBySellingEver = 0L;
/*      */   
/*  460 */   private long lastResetEarningsCounter = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  465 */   public final ConcurrentHashMap<String, Long> historyIPStart = new ConcurrentHashMap<>();
/*  466 */   public final ConcurrentHashMap<String, Long> historyIPLast = new ConcurrentHashMap<>();
/*  467 */   public final ConcurrentHashMap<String, Long> historyEmail = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*  470 */   public final ConcurrentHashMap<SteamId, SteamIdHistory> historySteamId = new ConcurrentHashMap<>();
/*      */   
/*      */   private Map<Short, SpellResistance> spellResistances;
/*      */   
/*  474 */   private float limitingArmourFactor = 0.3F;
/*  475 */   private long lastChangedPath = 0L;
/*      */ 
/*      */   
/*  478 */   private List<Route> highwayPath = null;
/*  479 */   private List<Float> highwayDistances = null;
/*  480 */   private String highwayPathDestination = "";
/*      */ 
/*      */   
/*      */   PlayerInfo(String aname) {
/*  484 */     this.name = aname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(PlayerInfo otherPlayerInfo) {
/*  490 */     return getName().compareTo(otherPlayerInfo.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getPower() {
/*  499 */     return this.power;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getPaymentExpire() {
/*  505 */     return System.currentTimeMillis() + 29030400000L;
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
/*      */   public final boolean isPaying() {
/*  531 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isQAAccount() {
/*  536 */     return isFlagSet(26);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBanned() {
/*  545 */     return this.banned;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getChangedKingdom() {
/*  554 */     return this.changedKingdom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLogin() {
/*  561 */     calculateSleep();
/*  562 */     this.lastLogin = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastLogin() {
/*  572 */     return this.lastLogin;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getLastLogout() {
/*  577 */     return this.lastLogout;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayBecomeChampion() {
/*  582 */     return (System.currentTimeMillis() - this.championTimeStamp > 14515200000L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final short getChampionPoints() {
/*  587 */     return this.championPoints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isReimbursed() {
/*  596 */     return this.reimbursed;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasLoadedFriends() {
/*  601 */     return this.hasLoadedFriends;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void setLoadedFriends(boolean hasLoaded) {
/*  606 */     this.hasLoadedFriends = hasLoaded;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getPlayerId() {
/*  611 */     return this.wurmId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getPassword() {
/*  620 */     return this.password;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean hasPlantedSign() {
/*  625 */     return (System.currentTimeMillis() - this.plantedSign < 86400000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Titles.Title[] getTitles() {
/*  634 */     return this.titles.<Titles.Title>toArray(new Titles.Title[this.titles.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean mayChangeDeity(int targetDeity) {
/*  639 */     if (targetDeity == 4)
/*  640 */       return true; 
/*  641 */     return (System.currentTimeMillis() - this.lastChangedDeity > 604800000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPassword(String pw) {
/*  650 */     this.password = pw;
/*      */     
/*      */     try {
/*  653 */       save();
/*      */     }
/*  655 */     catch (IOException iox) {
/*      */       
/*  657 */       logger.log(Level.WARNING, "Failed to change password for " + this.name, iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void initialize(String aName, long aWurmId, String aPassword, String aPwQuestion, String aPwAnswer, long aFace, boolean aGuest) throws IOException {
/*  664 */     this.name = aName;
/*  665 */     this.wurmId = aWurmId;
/*  666 */     this.password = aPassword;
/*  667 */     this.face = aFace;
/*  668 */     this.pwQuestion = aPwQuestion;
/*  669 */     this.pwAnswer = aPwAnswer;
/*  670 */     this.lastLogout = System.currentTimeMillis();
/*      */     
/*  672 */     this.flagBits.set(3, true);
/*  673 */     this.flags = getFlagLong();
/*  674 */     if (!aGuest)
/*  675 */       save(); 
/*  676 */     PlayerInfoFactory.addPlayerInfo(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  685 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Friend[] getFriends() {
/*  695 */     if (!hasLoadedFriends()) {
/*  696 */       loadFriends(this.wurmId);
/*      */     }
/*  698 */     if (this.friends != null)
/*  699 */       return (Friend[])this.friends.values().toArray((Object[])new Friend[this.friends.size()]); 
/*  700 */     return new Friend[0];
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final Friend getFriend(long friendId) {
/*  706 */     if (!hasLoadedFriends())
/*  707 */       loadFriends(friendId); 
/*  708 */     return this.friends.get(Long.valueOf(friendId));
/*      */   }
/*      */ 
/*      */   
/*      */   final void addFriend(long friendId, byte catId, String note, boolean loading) {
/*  713 */     if (this.friends == null)
/*  714 */       this.friends = new ConcurrentHashMap<>(); 
/*  715 */     Long fid = new Long(friendId);
/*  716 */     if (!this.friends.containsKey(fid)) {
/*      */       
/*  718 */       this.friends.put(fid, new Friend(friendId, catId, note));
/*  719 */       if (!loading) {
/*      */         
/*      */         try {
/*      */           
/*  723 */           saveFriend(this.wurmId, friendId, catId, note);
/*      */         }
/*  725 */         catch (IOException iox) {
/*      */           
/*  727 */           if (this.name != null) {
/*  728 */             logger.log(Level.WARNING, "Failed to save friends for " + this.name, iox);
/*      */           } else {
/*  730 */             logger.log(Level.WARNING, "Failed to save friends for unknown player.", iox);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   final void updateFriendData(long friendId, byte catId, String note) {
/*  738 */     if (this.friends == null)
/*  739 */       this.friends = new ConcurrentHashMap<>(); 
/*  740 */     Long fid = new Long(friendId);
/*  741 */     if (this.friends.containsKey(fid)) {
/*      */       
/*  743 */       Friend friend = this.friends.put(fid, new Friend(friendId, catId, note));
/*  744 */       if (friend.getCatId() != catId || !friend.getNote().equals(note)) {
/*      */         
/*      */         try {
/*      */           
/*  748 */           updateFriend(this.wurmId, friendId, catId, note);
/*      */         }
/*  750 */         catch (IOException iox) {
/*      */           
/*  752 */           if (this.name != null) {
/*  753 */             logger.log(Level.WARNING, "Failed to update friend (" + friend.getName() + ") for " + this.name, iox);
/*      */           } else {
/*  755 */             logger.log(Level.WARNING, "Failed to update friend (" + friend.getName() + ") for unknown player.", iox);
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
/*      */   public final boolean isFriendsWith(long friendId) {
/*  769 */     if (this.friends == null)
/*  770 */       loadFriends(this.wurmId); 
/*  771 */     if (this.friends != null)
/*      */     {
/*  773 */       return this.friends.containsKey(Long.valueOf(friendId));
/*      */     }
/*  775 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean removeFriend(long friendId) {
/*  782 */     if (this.friends == null)
/*  783 */       loadFriends(this.wurmId); 
/*  784 */     if (this.friends != null) {
/*      */       
/*  786 */       Long fid = new Long(friendId);
/*  787 */       if (this.friends.containsKey(fid)) {
/*      */         
/*  789 */         this.friends.remove(fid);
/*      */         
/*      */         try {
/*  792 */           deleteFriend(this.wurmId, friendId);
/*      */         }
/*  794 */         catch (IOException iox) {
/*      */           
/*  796 */           if (this.name != null) {
/*  797 */             logger.log(Level.WARNING, "Failed to save friends for " + this.name, iox);
/*      */           } else {
/*  799 */             logger.log(Level.WARNING, "Failed to save friends for unknown player.", iox);
/*      */           } 
/*  801 */         }  return true;
/*      */       } 
/*      */     } 
/*  804 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final void addEnemy(long enemyId, boolean loading) {
/*  809 */     if (this.enemies == null)
/*  810 */       this.enemies = new HashSet<>(); 
/*  811 */     Long fid = new Long(enemyId);
/*  812 */     if (!this.enemies.contains(fid)) {
/*      */       
/*  814 */       this.enemies.add(fid);
/*  815 */       if (!loading) {
/*      */         
/*      */         try {
/*      */           
/*  819 */           saveEnemy(this.wurmId, enemyId);
/*      */         }
/*  821 */         catch (IOException iox) {
/*      */           
/*  823 */           if (this.name != null) {
/*  824 */             logger.log(Level.WARNING, "Failed to save friends for " + this.name, iox);
/*      */           } else {
/*  826 */             logger.log(Level.WARNING, "Failed to save friends for unknown player.", iox);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean removeIgnored(long ignoredId) {
/*  885 */     if (this.ignored != null) {
/*      */       
/*  887 */       Long fid = new Long(ignoredId);
/*  888 */       if (this.ignored.contains(fid)) {
/*      */         
/*  890 */         this.ignored.remove(fid);
/*      */         
/*      */         try {
/*  893 */           deleteIgnored(this.wurmId, ignoredId);
/*  894 */           return true;
/*      */         }
/*  896 */         catch (IOException iox) {
/*      */           
/*  898 */           if (this.name != null) {
/*  899 */             logger.log(Level.WARNING, "Failed to delete ignored for " + this.name, iox);
/*      */           } else {
/*  901 */             logger.log(Level.WARNING, "Failed to delete ignored for unknown player.", iox);
/*      */           } 
/*      */         } 
/*      */       } 
/*  905 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isIgnored(long playerId) {
/*  910 */     if (this.ignored != null) {
/*  911 */       for (Iterator<Long> it = this.ignored.iterator(); it.hasNext(); ) {
/*      */         
/*  913 */         Long id = it.next();
/*  914 */         if (id.longValue() == playerId)
/*  915 */           return true; 
/*      */       } 
/*      */     } else {
/*  918 */       return false;
/*  919 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long[] getIgnored() {
/*  928 */     long[] toReturn = EMPTY_LONG_PRIMITIVE_ARRAY;
/*  929 */     if (this.ignored != null && this.ignored.size() > 0) {
/*      */       
/*  931 */       toReturn = new long[this.ignored.size()];
/*  932 */       int x = 0;
/*  933 */       for (Iterator<Long> it = this.ignored.iterator(); it.hasNext(); ) {
/*      */         
/*  935 */         Long ig = it.next();
/*  936 */         toReturn[x] = ig.longValue();
/*  937 */         x++;
/*      */       } 
/*      */     } 
/*  940 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getWarnings() {
/*  949 */     return this.warnings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastWarned() {
/*  958 */     return this.lastWarned;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getWarningStats(long getLastWarned) {
/*  963 */     String warnString = this.name + " has never been warned before.";
/*  964 */     if (getLastWarned > 0L)
/*  965 */       warnString = "Last warning received was " + Server.getTimeFor(System.currentTimeMillis() - getLastWarned) + " ago."; 
/*  966 */     return this.name + " has played " + Server.getTimeFor(this.playingTime) + " and received " + this.warnings + " warnings. " + warnString;
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
/*      */   public final int getRank() {
/*  978 */     return this.rank;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxRank() {
/*  987 */     return this.maxRank;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getAlignment() {
/*  996 */     return this.alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFaith() {
/* 1005 */     return this.faith;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Deity getDeity() {
/* 1014 */     return this.deity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Deity getGod() {
/* 1023 */     return this.god;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean spamMode() {
/* 1031 */     return this.spamMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFavor() {
/* 1040 */     return this.favor;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void sendReligionStatus(int itemNum, float value) {
/*      */     try {
/* 1047 */       Player p = Players.getInstance().getPlayer(this.wurmId);
/* 1048 */       p.getCommunicator().sendUpdateSkill(itemNum, value, 0);
/* 1049 */       p.checkFaithTitles();
/*      */     }
/* 1051 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void sendAttitudeChange() {
/*      */     try {
/* 1061 */       Players.getInstance().getPlayer(this.wurmId).sendAttitudeChange();
/*      */     }
/* 1063 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean checkPrayerFaith() {
/* 1072 */     if (this.deity == null) {
/* 1073 */       return false;
/*      */     }
/*      */     
/* 1076 */     if (this.numFaith >= (isFlagSet(81) ? 6 : 5)) {
/* 1077 */       return false;
/*      */     }
/*      */     
/* 1080 */     if (System.currentTimeMillis() - this.lastFaith > 1200000L) {
/*      */       
/* 1082 */       this.lastFaith = System.currentTimeMillis();
/* 1083 */       if (getFaith() < 30.0F || isPaying()) {
/*      */         
/* 1085 */         if (!Servers.localServer.isChallengeServer()) {
/*      */           
/* 1087 */           modifyFaith(Math.min(1.0F, (100.0F - getFaith()) / 10.0F * Math.max(1.0F, getFaith())));
/*      */         }
/*      */         else {
/*      */           
/* 1091 */           modifyFaith(1.0F);
/*      */         } 
/* 1093 */         this.numFaith = (byte)(this.numFaith + 1);
/*      */       } 
/*      */       
/*      */       try {
/* 1097 */         setNumFaith(this.numFaith, this.lastFaith);
/*      */       }
/* 1099 */       catch (IOException iox) {
/*      */         
/* 1101 */         logger.log(Level.WARNING, this.name + " " + iox.getMessage(), iox);
/*      */       } 
/* 1103 */       return true;
/*      */     } 
/* 1105 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final void modifyFaith(float mod) {
/* 1110 */     if (this.deity != null && mod != 0.0F)
/*      */     {
/* 1112 */       if (getFaith() < 30.0F || mod < 0.0F || (this.isPriest && isPaying())) {
/*      */         
/*      */         try {
/*      */           
/* 1116 */           setFaith(Math.max(1.0F, getFaith() + Math.min(1.0F, mod)));
/*      */         }
/* 1118 */         catch (IOException iox) {
/*      */           
/* 1120 */           logger.log(Level.WARNING, this.name, iox);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final void decreaseFatigue() {
/* 1128 */     this.fatigueSecsLeft--;
/* 1129 */     this.fatigueSecsToday++;
/* 1130 */     if (this.fatigueSecsLeft % 100 == 0) {
/* 1131 */       setFatigueSecs(this.fatigueSecsLeft, this.lastFatigue);
/*      */     }
/*      */   }
/*      */   
/*      */   final boolean checkFatigue() {
/* 1136 */     long times = 0L;
/*      */     
/* 1138 */     times = (System.currentTimeMillis() - this.lastFatigue) / 10800000L;
/*      */ 
/*      */     
/* 1141 */     if (times > 0L) {
/*      */       
/* 1143 */       for (int x = 0; x < Math.min(times, 8L); x++)
/* 1144 */         this.fatigueSecsLeft = Math.min(this.fatigueSecsLeft + 3600, 43200); 
/* 1145 */       this.lastFatigue += times * 10800000L;
/* 1146 */       setFatigueSecs(this.fatigueSecsLeft, this.lastFatigue);
/* 1147 */       return true;
/*      */     } 
/* 1149 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final int hardSetFatigueSecs(int fatsecsleft) {
/* 1154 */     this.fatigueSecsLeft = Math.max(0, Math.min(this.fatigueSecsLeft + fatsecsleft, 43200));
/* 1155 */     return this.fatigueSecsLeft;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMute() {
/* 1160 */     return this.muted;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addIgnored(long id, boolean load) throws IOException {
/* 1165 */     if (this.ignored == null)
/* 1166 */       this.ignored = new HashSet<>(); 
/* 1167 */     if (!this.ignored.contains(new Long(id))) {
/*      */       
/* 1169 */       this.ignored.add(new Long(id));
/* 1170 */       if (!load)
/* 1171 */         saveIgnored(this.wurmId, id); 
/* 1172 */       return true;
/*      */     } 
/* 1174 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getReputation() {
/* 1183 */     return this.reputation;
/*      */   }
/*      */ 
/*      */   
/*      */   final void pollReputation(long now) {
/* 1188 */     if (now > this.lastPolledReputation + 3600000L) {
/*      */       
/* 1190 */       long nums = (now - this.lastPolledReputation) / 3600000L;
/* 1191 */       setReputation(this.reputation + (int)nums);
/* 1192 */       this.lastPolledReputation = System.currentTimeMillis();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void logout() {
/* 1198 */     if (this.lastLogin > 0L)
/*      */     {
/* 1200 */       this.playingTime = this.playingTime + System.currentTimeMillis() - this.lastLogin;
/*      */     }
/* 1202 */     if (this.lastLogin > 0L)
/* 1203 */       this.lastLogout = System.currentTimeMillis(); 
/* 1204 */     this.lastLogin = 0L;
/* 1205 */     setSessionFlags(0L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getSleepLeft() {
/* 1210 */     if (this.sleep <= 0)
/* 1211 */       this.frozenSleep = true; 
/* 1212 */     return this.sleep;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSleepFrozen() {
/* 1217 */     return this.frozenSleep;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean hasSleepBonus() {
/* 1222 */     if (this.frozenSleep)
/* 1223 */       return false; 
/* 1224 */     return (this.sleep > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void calculateSleep() {
/* 1229 */     if (this.bed > 0L) {
/*      */       
/* 1231 */       long sleepTime = System.currentTimeMillis() - this.lastLogout;
/* 1232 */       if (sleepTime > 10800000L) {
/*      */         
/* 1234 */         Optional<Item> beds = Items.getItemOptional(this.bed);
/* 1235 */         if (beds.isPresent()) {
/*      */           
/* 1237 */           Item bed = beds.get();
/* 1238 */           if (bed.isBed())
/* 1239 */             bed.setData(0L); 
/*      */         } 
/*      */       } 
/* 1242 */       if (sleepTime > 3600000L) {
/*      */         
/* 1244 */         sleepTime /= 1000L;
/* 1245 */         long secs = sleepTime / 24L;
/* 1246 */         setSleep((int)(this.sleep + secs));
/*      */       } 
/*      */       
/* 1249 */       setBed(0L);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addToSleep(int secs) {
/* 1255 */     setSleep(this.sleep + secs);
/*      */     
/*      */     try {
/* 1258 */       Player p = Players.getInstance().getPlayer(this.wurmId);
/* 1259 */       p.getCommunicator().sendSleepInfo();
/*      */     }
/* 1261 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean eligibleForAffinity() {
/* 1269 */     if (System.currentTimeMillis() > this.nextAffinity || this.nextAffinity == 0L) {
/*      */       
/* 1271 */       setNextAffinity(System.currentTimeMillis() + 2419200000L + Server.rand.nextInt(50000));
/* 1272 */       return true;
/*      */     } 
/* 1274 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastTrigger() {
/* 1338 */     return this.lastTriggerEffect;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCurrentServer() {
/* 1543 */     return this.currentServer;
/*      */   }
/*      */ 
/*      */   
/*      */   final void addAppointment(int aid) {
/* 1548 */     if (!hasAppointment(aid)) {
/*      */       
/* 1550 */       this.appointments += 1L << aid;
/* 1551 */       saveAppointments();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void removeAppointment(int aid) {
/* 1557 */     if (hasAppointment(aid)) {
/*      */       
/* 1559 */       this.appointments -= 1L << aid;
/* 1560 */       saveAppointments();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void clearAppointments() {
/* 1566 */     this.appointments = 0L;
/* 1567 */     saveAppointments();
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean hasAppointment(int aid) {
/* 1572 */     if ((this.appointments >> aid & 0x1L) == 1L)
/*      */     {
/* 1574 */       return true;
/*      */     }
/* 1576 */     return false;
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
/*      */   public final boolean isPlayerAssistant() {
/* 1590 */     return this.playerAssistant;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayAppointPlayerAssistant() {
/* 1595 */     return this.mayAppointPlayerAssistant;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean seesPlayerAssistantWindow() {
/* 1600 */     return this.seesPlayerAssistantWindow;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasMovedInventory() {
/* 1605 */     return this.hasMovedInventory;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayUseLastGasp() {
/* 1610 */     return (System.currentTimeMillis() - this.lastGasp > 21600000L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void useLastGasp() {
/* 1615 */     this.lastGasp = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isUsingLastGasp() {
/* 1620 */     return (System.currentTimeMillis() - this.lastGasp < 120000L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final byte getChaosKingdom() {
/* 1625 */     return this.chaosKingdom;
/*      */   }
/*      */ 
/*      */   
/*      */   public final short getHotaWins() {
/* 1630 */     return this.hotaWins;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getLastDeath() {
/* 1635 */     return this.lastDeath;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void died() {
/* 1640 */     this.lastDeath = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void checkHotaTitles() {
/* 1645 */     if (this.hotaWins == 1)
/* 1646 */       addTitle(Titles.Title.Hota_One); 
/* 1647 */     if (this.hotaWins == 3)
/* 1648 */       addTitle(Titles.Title.Hota_Two); 
/* 1649 */     if (this.hotaWins == 7)
/* 1650 */       addTitle(Titles.Title.Hota_Three); 
/* 1651 */     if (this.hotaWins == 15)
/* 1652 */       addTitle(Titles.Title.Hota_Four); 
/* 1653 */     if (this.hotaWins == 30) {
/* 1654 */       addTitle(Titles.Title.Hota_Five);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getKarma() {
/* 1743 */     return this.karma;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxKarma() {
/* 1753 */     return this.maxKarma;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTotalKarma() {
/* 1763 */     return this.totalKarma;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getScenarioKarma() {
/* 1773 */     return this.scenarioKarma;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAbilityBitSet(int abilityBit) {
/* 1778 */     if (this.abilities != 0L)
/*      */     {
/* 1780 */       return this.abilityBits.get(abilityBit);
/*      */     }
/* 1782 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isFlagSet(int flagBit) {
/* 1787 */     if (flagBit < 64) {
/*      */       
/* 1789 */       if (this.flags != 0L)
/*      */       {
/* 1791 */         return this.flagBits.get(flagBit);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1796 */     else if (this.flags2 != 0L) {
/*      */       
/* 1798 */       return this.flag2Bits.get(flagBit - 64);
/*      */     } 
/*      */     
/* 1801 */     return false;
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
/*      */   public byte getBlood() {
/* 1813 */     return this.blood;
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
/*      */   public ConcurrentHashMap<String, Long> getAllTargetPMIds() {
/* 1834 */     return this.targetPMIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasPMTarget(String targetName) {
/* 1839 */     return this.targetPMIds.containsKey(targetName);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getPMTargetId(String targetName) {
/* 1844 */     if (this.targetPMIds.containsKey(targetName))
/*      */     {
/* 1846 */       return ((Long)this.targetPMIds.get(targetName)).longValue();
/*      */     }
/* 1848 */     return -10L;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPMTarget(String targetName, long targetId) {
/* 1853 */     if (!this.targetPMIds.containsKey(targetName))
/*      */     {
/* 1855 */       this.targetPMIds.put(targetName, Long.valueOf(targetId));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePMTarget(String targetName) {
/* 1861 */     if (this.targetPMIds.containsKey(targetName))
/*      */     {
/* 1863 */       this.targetPMIds.remove(targetName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public long getSessionFlags() {
/* 1869 */     return this.sessionFlags;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSessionFlags(long aFlags) {
/* 1874 */     this.sessionFlags = aFlags;
/* 1875 */     this.sessionFlagBits.clear();
/* 1876 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 1878 */       if ((aFlags >>> x & 0x1L) == 1L)
/*      */       {
/* 1880 */         this.sessionFlagBits.set(x);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isSessionFlagSet(int flagBit) {
/* 1887 */     if (this.sessionFlags != 0L)
/*      */     {
/* 1889 */       return this.sessionFlagBits.get(flagBit);
/*      */     }
/* 1891 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setSessionFlag(int number, boolean value) {
/* 1896 */     this.sessionFlagBits.set(number, value);
/* 1897 */     this.sessionFlags = getSessionFlagLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getModelName() {
/* 1902 */     return this.modelName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final long getSessionFlagLong() {
/* 1909 */     long ret = 0L;
/* 1910 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 1912 */       if (this.sessionFlagBits.get(x))
/*      */       {
/* 1914 */         ret += 1L << x;
/*      */       }
/*      */     } 
/* 1917 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String toString() {
/* 1928 */     return "PlayerInfo [wurmId: " + this.wurmId + ", name: " + this.name + ", currentServer: " + this.currentServer + ", lastLogin: " + this.lastLogin + ", lastLogout: " + this.lastLogout + ", banned: " + this.banned + ", ipaddress: " + this.ipaddress + ", power: " + this.power + ", creationDate: " + this.creationDate + ", paymentExpireDate: " + this.paymentExpireDate + ", playingTime: " + this.playingTime + ", money: " + this.money + ']';
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
/*      */   public long getMoneyEarnedBySellingLastHour() {
/* 1941 */     return this.moneyEarnedBySellingLastHour;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getMoneyToSend() {
/* 1946 */     return this.moneyToSend;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void resetMoneyToSend() {
/* 1951 */     this.moneyToSend = 0L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMoneyEarnedBySellingLastHour(long aMoney) {
/* 1962 */     this.moneyToSend += aMoney;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1968 */     if (getMoneyEarnedBySellingLastHour() == 0L)
/*      */     {
/*      */       
/* 1971 */       setLastResetEarningsCounter(System.currentTimeMillis());
/*      */     }
/* 1973 */     setMoneyEarnedBySellingLastHour(getMoneyEarnedBySellingLastHour() + aMoney);
/* 1974 */     addMoneyEarnedBySellingEver(aMoney);
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkIfResetSellEarning() {
/* 1979 */     if (System.currentTimeMillis() - getLastResetEarningsCounter() > (Servers.isThisATestServer() ? 20000L : 3600000L)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1986 */       setLastResetEarningsCounter(System.currentTimeMillis());
/* 1987 */       setMoneyEarnedBySellingLastHour(0L);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLastResetEarningsCounter() {
/* 1993 */     return this.lastResetEarningsCounter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMoneyEarnedBySellingEver() {
/* 2003 */     return this.moneyEarnedBySellingEver;
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
/*      */   public void setMoneyEarnedBySellingLastHour(long aMoneyEarnedBySellingLastHour) {
/* 2026 */     this.moneyEarnedBySellingLastHour = aMoneyEarnedBySellingLastHour;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastResetEarningsCounter(long aLastResetEarningsCounter) {
/* 2037 */     this.lastResetEarningsCounter = aLastResetEarningsCounter;
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
/*      */   public final float addSpellResistance(short spellId) {
/* 2052 */     if (this.spellResistances == null)
/* 2053 */       this.spellResistances = new ConcurrentHashMap<>(); 
/* 2054 */     SpellResistance existing = this.spellResistances.get(Short.valueOf(spellId));
/* 2055 */     if (existing == null) {
/*      */       
/* 2057 */       existing = new SpellResistance(spellId);
/* 2058 */       this.spellResistances.put(Short.valueOf(spellId), existing);
/*      */     } 
/* 2060 */     float toReturn = existing.getResistance();
/* 2061 */     existing.setResistance();
/* 2062 */     return 1.0F - toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final SpellResistance getSpellResistance(short spellId) {
/* 2067 */     if (this.spellResistances == null)
/* 2068 */       this.spellResistances = new ConcurrentHashMap<>(); 
/* 2069 */     return this.spellResistances.get(Short.valueOf(spellId));
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
/*      */   public final void pollResistances(Communicator comm) {
/* 2081 */     if (this.spellResistances != null) {
/*      */       
/* 2083 */       SpellResistance[] resisArr = (SpellResistance[])this.spellResistances.values().toArray((Object[])new SpellResistance[this.spellResistances.size()]);
/* 2084 */       for (SpellResistance resist : resisArr) {
/*      */         
/* 2086 */         if (resist.tickSecond(comm))
/*      */         {
/* 2088 */           this.spellResistances.remove(Short.valueOf(resist.getSpellType()));
/*      */         }
/*      */       } 
/* 2091 */       if (this.spellResistances.isEmpty()) {
/* 2092 */         this.spellResistances = null;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void clearSpellResistances(Communicator communicator) {
/* 2098 */     if (this.spellResistances != null) {
/*      */       
/* 2100 */       for (SpellResistance resist : this.spellResistances.values())
/*      */       {
/* 2102 */         resist.sendUpdateToClient(communicator, (byte)0);
/*      */       }
/* 2104 */       this.spellResistances.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendSpellResistances(Communicator communicator) {
/* 2110 */     if (this.spellResistances != null)
/*      */     {
/* 2112 */       for (SpellResistance resist : this.spellResistances.values())
/*      */       {
/* 2114 */         resist.sendUpdateToClient(communicator, (byte)2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setArmourLimitingFactor(float factor, Communicator communicator, boolean initializing) {
/* 2121 */     float factorToUse = factor;
/* 2122 */     if (this.favor >= 35.0F && this.faith >= 70.0F && this.deity != null && this.deity.number == 2) {
/*      */ 
/*      */       
/* 2125 */       float tempfactor = this.limitingArmourFactor;
/* 2126 */       if (factorToUse == -0.15F) {
/*      */         
/* 2128 */         tempfactor = 0.0F;
/*      */         
/* 2130 */         if (tempfactor == this.limitingArmourFactor) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 2135 */         factorToUse = tempfactor;
/*      */       } 
/*      */     } 
/* 2138 */     if (this.limitingArmourFactor == factorToUse && !initializing) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 2143 */     this.limitingArmourFactor = factorToUse;
/*      */ 
/*      */     
/* 2146 */     communicator.sendRemoveSpellEffect(SpellEffectsEnum.ARMOUR_LIMIT_NONE);
/* 2147 */     communicator.sendRemoveSpellEffect(SpellEffectsEnum.ARMOUR_LIMIT_LIGHT);
/* 2148 */     communicator.sendRemoveSpellEffect(SpellEffectsEnum.ARMOUR_LIMIT_MEDIUM);
/* 2149 */     communicator.sendRemoveSpellEffect(SpellEffectsEnum.ARMOUR_LIMIT_HEAVY);
/*      */     
/* 2151 */     SpellEffectsEnum toSend = SpellEffectsEnum.ARMOUR_LIMIT_NONE;
/* 2152 */     if (this.limitingArmourFactor == -0.3F) {
/*      */       
/* 2154 */       toSend = SpellEffectsEnum.ARMOUR_LIMIT_HEAVY;
/*      */     }
/* 2156 */     else if (this.limitingArmourFactor == -0.15F) {
/*      */       
/* 2158 */       toSend = SpellEffectsEnum.ARMOUR_LIMIT_MEDIUM;
/*      */     }
/* 2160 */     else if (this.limitingArmourFactor == 0.0F) {
/*      */       
/* 2162 */       toSend = SpellEffectsEnum.ARMOUR_LIMIT_LIGHT;
/*      */     } 
/* 2164 */     if (toSend != null) {
/* 2165 */       communicator.sendAddStatusEffect(toSend, 100000);
/*      */     }
/*      */   }
/*      */   
/*      */   public final float getArmourLimitingFactor() {
/* 2170 */     return this.limitingArmourFactor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastChangedPath() {
/* 2180 */     return this.lastChangedPath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastChangedPath(long lastChangedPath) {
/* 2191 */     this.lastChangedPath = lastChangedPath;
/*      */   }
/*      */   
/*      */   public final byte getSex() {
/* 2195 */     return this.sex;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMale() {
/* 2200 */     return (this.sex == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isFemale() {
/* 2205 */     return (this.sex == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOnlineHere() {
/* 2214 */     return (this.currentServer == Servers.localServer.id && Players.getInstance().getPlayerOrNull(this.wurmId) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<Route> getHighwayPath() {
/* 2224 */     return this.highwayPath;
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
/*      */   void setHighwayPath(String newDestination, List<Route> newPath) {
/* 2236 */     this.highwayPath = newPath;
/* 2237 */     if (newPath == null) {
/*      */       
/* 2239 */       this.highwayPathDestination = "";
/*      */     }
/*      */     else {
/*      */       
/* 2243 */       this.highwayPathDestination = newDestination;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getHighwayPathDestination() {
/* 2254 */     return this.highwayPathDestination;
/*      */   }
/*      */ 
/*      */   
/*      */   public SteamId getSteamId() {
/* 2259 */     return this.steamId;
/*      */   }
/*      */   
/*      */   public abstract void setPower(byte paramByte) throws IOException;
/*      */   
/*      */   public abstract void setPaymentExpire(long paramLong) throws IOException;
/*      */   
/*      */   public abstract void setPaymentExpire(long paramLong, boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setBanned(boolean paramBoolean, String paramString, long paramLong) throws IOException;
/*      */   
/*      */   public abstract void resetWarnings() throws IOException;
/*      */   
/*      */   abstract void setReputation(int paramInt);
/*      */   
/*      */   public abstract void setMuted(boolean paramBoolean, String paramString, long paramLong);
/*      */   
/*      */   abstract void setFatigueSecs(int paramInt, long paramLong);
/*      */   
/*      */   abstract void setCheated(String paramString);
/*      */   
/*      */   public abstract void updatePassword(String paramString) throws IOException;
/*      */   
/*      */   public abstract void setRealDeath(byte paramByte) throws IOException;
/*      */   
/*      */   public abstract void setFavor(float paramFloat) throws IOException;
/*      */   
/*      */   public abstract void setFaith(float paramFloat) throws IOException;
/*      */   
/*      */   abstract void setDeity(Deity paramDeity) throws IOException;
/*      */   
/*      */   abstract void setAlignment(float paramFloat) throws IOException;
/*      */   
/*      */   abstract void setGod(Deity paramDeity) throws IOException;
/*      */   
/*      */   public abstract void load() throws IOException;
/*      */   
/*      */   public abstract void warn() throws IOException;
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */   
/*      */   public abstract void setLastTrigger(int paramInt);
/*      */   
/*      */   abstract void setIpaddress(String paramString) throws IOException;
/*      */   
/*      */   abstract void setSteamId(SteamId paramSteamId) throws IOException;
/*      */   
/*      */   public abstract void setRank(int paramInt) throws IOException;
/*      */   
/*      */   public abstract void setReimbursed(boolean paramBoolean) throws IOException;
/*      */   
/*      */   abstract void setPlantedSign() throws IOException;
/*      */   
/*      */   abstract void setChangedDeity() throws IOException;
/*      */   
/*      */   public abstract String getIpaddress();
/*      */   
/*      */   abstract void setDead(boolean paramBoolean);
/*      */   
/*      */   public abstract void setSessionKey(String paramString, long paramLong) throws IOException;
/*      */   
/*      */   abstract void setName(String paramString) throws IOException;
/*      */   
/*      */   public abstract void setVersion(long paramLong) throws IOException;
/*      */   
/*      */   abstract void saveFriend(long paramLong1, long paramLong2, byte paramByte, String paramString) throws IOException;
/*      */   
/*      */   abstract void updateFriend(long paramLong1, long paramLong2, byte paramByte, String paramString) throws IOException;
/*      */   
/*      */   abstract void deleteFriend(long paramLong1, long paramLong2) throws IOException;
/*      */   
/*      */   abstract void saveEnemy(long paramLong1, long paramLong2) throws IOException;
/*      */   
/*      */   abstract void deleteEnemy(long paramLong1, long paramLong2) throws IOException;
/*      */   
/*      */   abstract void saveIgnored(long paramLong1, long paramLong2) throws IOException;
/*      */   
/*      */   abstract void deleteIgnored(long paramLong1, long paramLong2) throws IOException;
/*      */   
/*      */   public abstract void setNumFaith(byte paramByte, long paramLong) throws IOException;
/*      */   
/*      */   abstract long getFlagLong();
/*      */   
/*      */   abstract long getFlag2Long();
/*      */   
/*      */   public abstract void setMoney(long paramLong) throws IOException;
/*      */   
/*      */   abstract void setSex(byte paramByte) throws IOException;
/*      */   
/*      */   abstract void setClimbing(boolean paramBoolean) throws IOException;
/*      */   
/*      */   abstract void setChangedKingdom(byte paramByte, boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setFace(long paramLong) throws IOException;
/*      */   
/*      */   abstract boolean addTitle(Titles.Title paramTitle);
/*      */   
/*      */   abstract boolean removeTitle(Titles.Title paramTitle);
/*      */   
/*      */   abstract void setAlcohol(float paramFloat);
/*      */   
/*      */   abstract void setPet(long paramLong);
/*      */   
/*      */   public abstract void setNicotineTime(long paramLong);
/*      */   
/*      */   public abstract boolean setAlcoholTime(long paramLong);
/*      */   
/*      */   abstract void setNicotine(float paramFloat);
/*      */   
/*      */   public abstract void setMayMute(boolean paramBoolean);
/*      */   
/*      */   public abstract void setEmailAddress(String paramString);
/*      */   
/*      */   abstract void setPriest(boolean paramBoolean);
/*      */   
/*      */   public abstract void setOverRideShop(boolean paramBoolean);
/*      */   
/*      */   public abstract void setReferedby(long paramLong);
/*      */   
/*      */   public abstract void setBed(long paramLong);
/*      */   
/*      */   abstract void setLastChangedVillage(long paramLong);
/*      */   
/*      */   abstract void setSleep(int paramInt);
/*      */   
/*      */   abstract void setTheftwarned(boolean paramBoolean);
/*      */   
/*      */   public abstract void setHasNoReimbursementLeft(boolean paramBoolean);
/*      */   
/*      */   abstract void setDeathProtected(boolean paramBoolean);
/*      */   
/*      */   public abstract void setCurrentServer(int paramInt);
/*      */   
/*      */   public abstract void setDevTalk(boolean paramBoolean);
/*      */   
/*      */   public abstract void transferDeity(@Nullable Deity paramDeity) throws IOException;
/*      */   
/*      */   abstract void saveSwitchFatigue();
/*      */   
/*      */   abstract void saveFightMode(byte paramByte);
/*      */   
/*      */   abstract void setNextAffinity(long paramLong);
/*      */   
/*      */   public abstract void saveAppointments();
/*      */   
/*      */   abstract void setTutorialLevel(int paramInt);
/*      */   
/*      */   abstract void setAutofight(boolean paramBoolean);
/*      */   
/*      */   abstract void setLastVehicle(long paramLong);
/*      */   
/*      */   public abstract void setIsPlayerAssistant(boolean paramBoolean);
/*      */   
/*      */   public abstract void setMayAppointPlayerAssistant(boolean paramBoolean);
/*      */   
/*      */   public abstract boolean togglePlayerAssistantWindow(boolean paramBoolean);
/*      */   
/*      */   public abstract void setLastTaggedTerr(byte paramByte);
/*      */   
/*      */   public abstract void setNewPriestType(byte paramByte, long paramLong);
/*      */   
/*      */   public abstract void setChangedJoat();
/*      */   
/*      */   public abstract void setMovedInventory(boolean paramBoolean);
/*      */   
/*      */   public abstract void setFreeTransfer(boolean paramBoolean);
/*      */   
/*      */   public abstract boolean setHasSkillGain(boolean paramBoolean);
/*      */   
/*      */   public abstract void loadIgnored(long paramLong);
/*      */   
/*      */   public abstract void loadTitles(long paramLong);
/*      */   
/*      */   public abstract void loadFriends(long paramLong);
/*      */   
/*      */   public abstract void loadHistorySteamIds(long paramLong);
/*      */   
/*      */   public abstract void loadHistoryIPs(long paramLong);
/*      */   
/*      */   public abstract void loadHistoryEmails(long paramLong);
/*      */   
/*      */   public abstract boolean setChampionPoints(short paramShort);
/*      */   
/*      */   public abstract void setChangedKingdom();
/*      */   
/*      */   public abstract void setChampionTimeStamp();
/*      */   
/*      */   public abstract void setChampChanneling(float paramFloat);
/*      */   
/*      */   public abstract void setMuteTimes(short paramShort);
/*      */   
/*      */   public abstract void setVotedKing(boolean paramBoolean);
/*      */   
/*      */   public abstract void setEpicLocation(byte paramByte, int paramInt);
/*      */   
/*      */   public abstract void setChaosKingdom(byte paramByte);
/*      */   
/*      */   public abstract void setHotaWins(short paramShort);
/*      */   
/*      */   public abstract void setSpamMode(boolean paramBoolean);
/*      */   
/*      */   public abstract void setKarma(int paramInt);
/*      */   
/*      */   public abstract void setScenarioKarma(int paramInt);
/*      */   
/*      */   public abstract void setBlood(byte paramByte);
/*      */   
/*      */   public abstract void setFlag(int paramInt, boolean paramBoolean);
/*      */   
/*      */   public abstract void setFlagBits(long paramLong);
/*      */   
/*      */   public abstract void setFlag2Bits(long paramLong);
/*      */   
/*      */   public abstract void forceFlagsUpdate();
/*      */   
/*      */   public abstract void setAbility(int paramInt, boolean paramBoolean);
/*      */   
/*      */   public abstract void setCurrentAbilityTitle(int paramInt);
/*      */   
/*      */   public abstract void setUndeadData();
/*      */   
/*      */   public abstract void setModelName(String paramString);
/*      */   
/*      */   public abstract void addMoneyEarnedBySellingEver(long paramLong);
/*      */   
/*      */   public abstract void setPointsForChamp();
/*      */   
/*      */   public abstract void switchChamp();
/*      */   
/*      */   public abstract void setPassRetrieval(String paramString1, String paramString2) throws IOException;
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */