/*      */ package com.wurmonline.server.villages;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Group;
/*      */ import com.wurmonline.server.Groups;
/*      */ import com.wurmonline.server.HistoryEvent;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.Twit;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.behaviours.Actions;
/*      */ import com.wurmonline.server.behaviours.Vehicle;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.MineDoorPermission;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.epic.Hota;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.items.WurmColor;
/*      */ import com.wurmonline.server.kingdom.GuardTower;
/*      */ import com.wurmonline.server.kingdom.InfluenceChain;
/*      */ import com.wurmonline.server.kingdom.King;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.MapAnnotation;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.VillageTeleportQuestion;
/*      */ import com.wurmonline.server.structures.FenceGate;
/*      */ import com.wurmonline.server.structures.NoSuchLockException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import java.awt.Rectangle;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Village
/*      */   implements MiscConstants, VillageStatus, TimeConstants, CounterTypes, MonetaryConstants, Comparable<Village>
/*      */ {
/*      */   public static final int MINIMUM_PERIMETER = 5;
/*      */   public static final int ATTACK_PERIMETER = 2;
/*      */   public static final byte SPAWN_VILLAGE_ALLIES = 0;
/*      */   public static final byte SPAWN_KINGDOM = 1;
/*      */   public static final int BADREPUTATION = 50;
/*      */   public static final int MAXBADREPUTATION = 150;
/*  116 */   private final Set<Item> oilBarrels = new HashSet<>();
/*      */   
/*  118 */   byte spawnSituation = 0;
/*      */ 
/*      */   
/*      */   private boolean alerted = false;
/*      */   
/*      */   public int startx;
/*      */   
/*      */   public int endx;
/*      */   
/*      */   public int starty;
/*      */   
/*      */   public int endy;
/*      */   
/*  131 */   private static final Logger logger = Logger.getLogger(Village.class.getName());
/*      */   
/*      */   String name;
/*      */   
/*      */   final String founderName;
/*      */   
/*  137 */   int perimeterTiles = 0;
/*      */   
/*      */   public String mayorName;
/*      */   
/*  141 */   public int id = -10;
/*      */   
/*      */   final long creationDate;
/*      */   
/*      */   public final Map<Long, Citizen> citizens;
/*      */   
/*      */   public long deedid;
/*      */   
/*      */   long upkeep;
/*      */   
/*      */   final boolean surfaced;
/*      */   
/*      */   final Map<Integer, VillageRole> roles;
/*      */   
/*      */   boolean democracy = true;
/*      */   
/*  157 */   String motto = "A settlement just like any other!";
/*      */   
/*      */   protected final Group group;
/*      */   
/*      */   private final Set<FenceGate> gates;
/*      */   
/*      */   private final Set<MineDoorPermission> mineDoors;
/*      */   
/*  165 */   long tokenId = -10L;
/*      */   
/*  167 */   public final Map<Long, Guard> guards = new HashMap<>();
/*      */   
/*  169 */   private static final int maxGuardsOnThisServer = Servers.localServer.isChallengeOrEpicServer() ? 4 : 4;
/*      */   
/*  171 */   public long disband = 0L;
/*      */   
/*  173 */   public long disbander = -10L;
/*      */   
/*      */   private static final long disbandTime = 86400000L;
/*      */   
/*  177 */   final Map<Long, Reputation> reputations = new HashMap<>();
/*      */   
/*  179 */   public Set<Long> targets = new HashSet<>();
/*      */   
/*  181 */   private Set<MapAnnotation> villageMapAnnotations = new HashSet<>();
/*      */   
/*  183 */   private Set<VillageRecruitee> recruitees = new HashSet<>();
/*      */   
/*      */   public static final int REPUTATION_CRIMINAL = -30;
/*      */   
/*  187 */   long lastLogin = 0L;
/*      */   
/*      */   private Map<Village, VillageWar> wars;
/*      */   
/*      */   public Map<Village, WarDeclaration> warDeclarations;
/*      */   
/*  193 */   private long lastPolledReps = System.currentTimeMillis();
/*      */   
/*      */   public byte kingdom;
/*      */   
/*      */   public GuardPlan plan;
/*      */   
/*  199 */   Permissions settings = new Permissions();
/*      */   
/*      */   public boolean unlimitedCitizens = false;
/*      */   
/*  203 */   public long lastChangedName = 0L;
/*      */ 
/*      */   
/*      */   boolean acceptsMerchants = false;
/*      */   
/*  208 */   LinkedList<HistoryEvent> history = new LinkedList<>();
/*      */   
/*  210 */   int maxCitizens = 0;
/*      */ 
/*      */   
/*      */   public final boolean isPermanent;
/*      */ 
/*      */   
/*      */   final byte spawnKingdom;
/*      */ 
/*      */   
/*      */   private static boolean freeDisbands = false;
/*      */ 
/*      */   
/*      */   private static final String upkeepString = "upkeep";
/*      */   
/*      */   boolean allowsAggCreatures = false;
/*      */   
/*  226 */   String consumerKeyToUse = "";
/*      */   
/*  228 */   String consumerSecretToUse = "";
/*      */   
/*  230 */   String applicationToken = "";
/*      */   
/*  232 */   String applicationSecret = "";
/*      */   
/*      */   boolean twitChat = false;
/*      */   
/*      */   private boolean canTwit = false;
/*      */   
/*      */   boolean twitEnabled = true;
/*      */   
/*  240 */   float faithWar = 0.0F;
/*      */   
/*  242 */   float faithHeal = 0.0F;
/*      */   
/*  244 */   float faithCreate = 0.0F;
/*      */   
/*  246 */   float faithDivideVal = 1.0F;
/*      */   
/*  248 */   int allianceNumber = 0;
/*      */   
/*  250 */   short hotaWins = 0;
/*      */   
/*  252 */   protected String motd = "";
/*      */   
/*  254 */   static final Village[] emptyVillages = new Village[0];
/*      */   
/*  256 */   int villageReputation = 0;
/*      */   
/*  258 */   VillageRole everybody = null;
/*      */   
/*  260 */   public long pmkKickDate = 0L;
/*      */   
/*      */   private short[] outsideSpawn;
/*      */   
/*  264 */   public final Map<Long, Wagoner> wagoners = new ConcurrentHashMap<>();
/*      */   long lastSentPmkWarning;
/*      */   boolean detectedBunny;
/*      */   
/*      */   public enum VillagePermissions implements Permissions.IPermission {
/*  269 */     HIGHWAY_OPT_IN(0, "Village", "Highway Opt-in"),
/*  270 */     ALLOW_KOS(1, "Village", "Allow KOS"),
/*  271 */     ALLOW_HIGHWAYS(2, "Village", "Allow Highways"),
/*  272 */     SPARE03(3, "Unknown", "Spare"),
/*  273 */     SPARE04(4, "Unknown", "Spare"),
/*  274 */     SPARE05(5, "Unknown", "Spare"),
/*  275 */     SPARE06(6, "Unknown", "Spare"),
/*  276 */     SPARE07(7, "Unknown", "Spare");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte bit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String header2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  360 */     private static final Permissions.Allow[] types = Permissions.Allow.values();
/*      */     VillagePermissions(int aBit, String category, String aDescription) { this.bit = (byte)aBit; this.description = aDescription; this.header1 = category; this.header2 = ""; }
/*      */     public byte getBit() { return this.bit; }
/*      */     public int getValue() { return 1 << this.bit; }
/*      */     public String getDescription() { return this.description; }
/*  365 */     public String getHeader1() { return this.header1; } public String getHeader2() { return this.header2; } public String getHover() { return ""; } static {  } public static Permissions.IPermission[] getPermissions() { return (Permissions.IPermission[])types; }
/*      */   
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
/*      */   public boolean canTwit() {
/*  499 */     this.canTwit = false;
/*  500 */     if (this.consumerKeyToUse != null && this.consumerKeyToUse.length() > 5 && 
/*  501 */       this.consumerSecretToUse != null && this.consumerSecretToUse.length() > 5 && 
/*  502 */       this.applicationToken != null && this.applicationToken.length() > 5 && 
/*  503 */       this.applicationSecret != null && this.applicationSecret.length() > 5)
/*  504 */       this.canTwit = true; 
/*  505 */     return this.canTwit;
/*      */   }
/*      */ 
/*      */   
/*      */   final void createInitialUpkeepPlan() {
/*  510 */     this.plan = new DbGuardPlan(0, this.id);
/*      */   }
/*      */ 
/*      */   
/*      */   final void initialize() {
/*  515 */     Zone[] coveredZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, true); int x;
/*  516 */     for (x = 0; x < coveredZones.length; x++)
/*      */     {
/*  518 */       coveredZones[x].addVillage(this);
/*      */     }
/*  520 */     coveredZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, false);
/*  521 */     for (x = 0; x < coveredZones.length; x++)
/*      */     {
/*  523 */       coveredZones[x].addVillage(this);
/*      */     }
/*  525 */     setKingdomInfluence();
/*  526 */     if (Features.Feature.TOWER_CHAINING.isEnabled()) {
/*      */       
/*      */       try {
/*      */         
/*  530 */         InfluenceChain.addTokenToChain(this.kingdom, getToken());
/*      */       }
/*  532 */       catch (NoSuchItemException e) {
/*      */         
/*  534 */         logger.warning(String.format("Village Initialize Error: No token found for village %s.", new Object[] { getName() }));
/*      */       } 
/*      */     }
/*  537 */     this.outsideSpawn = calcOutsideSpawn();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setKingdomInfluence() {
/*  542 */     for (int x = this.startx - 5 - this.perimeterTiles; x < this.endx + 5 + this.perimeterTiles; x++) {
/*  543 */       for (int y = this.starty - 5 - this.perimeterTiles; y < this.endy + 5 + this.perimeterTiles; y++)
/*  544 */         Zones.setKingdom(x, y, this.kingdom); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final double getSkillModifier() {
/*  549 */     long timeSinceCreated = System.currentTimeMillis() - this.creationDate;
/*  550 */     if (timeSinceCreated > 174182400000L)
/*  551 */       return 4.0D; 
/*  552 */     if (timeSinceCreated > 116121600000L)
/*  553 */       return 3.0D; 
/*  554 */     if (timeSinceCreated > 1.016064E11D)
/*  555 */       return 2.75D; 
/*  556 */     if (timeSinceCreated > 87091200000L)
/*  557 */       return 2.5D; 
/*  558 */     if (timeSinceCreated > 7.2576E10D)
/*  559 */       return 2.25D; 
/*  560 */     if (timeSinceCreated > 58060800000L)
/*  561 */       return 2.0D; 
/*  562 */     if (timeSinceCreated > 4.35456E10D)
/*  563 */       return 1.75D; 
/*  564 */     if (timeSinceCreated > 29030400000L)
/*  565 */       return 1.5D; 
/*  566 */     if (timeSinceCreated > 21772800000L)
/*  567 */       return 1.0D; 
/*  568 */     if (timeSinceCreated > 14515200000L)
/*  569 */       return 0.5D; 
/*  570 */     if (timeSinceCreated > 7257600000L)
/*  571 */       return 0.25D; 
/*  572 */     return 0.1D;
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRoles() {
/*  577 */     createRoleEverybody();
/*  578 */     createRoleCitizen();
/*  579 */     createRoleMayor();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void checkIfRaiseAlert(Creature creature) {
/*  584 */     if (creature.getPower() <= 0)
/*      */     {
/*  586 */       if (isEnemy(creature)) {
/*  587 */         addTarget(creature);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean acceptsNewCitizens() {
/*  593 */     if (this.unlimitedCitizens)
/*  594 */       return true; 
/*  595 */     int g = 0;
/*  596 */     if (this.guards != null)
/*  597 */       g = this.guards.size(); 
/*  598 */     return (getMaxCitizens() > this.citizens.size() - g);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasToomanyCitizens() {
/*  603 */     int g = 0;
/*  604 */     if (this.guards != null)
/*  605 */       g = this.guards.size(); 
/*  606 */     return (getMaxCitizens() < this.citizens.size() - g);
/*      */   }
/*      */ 
/*      */   
/*      */   final void checkForEnemies() {
/*  611 */     if (this.guards.size() > 0)
/*      */     {
/*  613 */       for (int x = this.startx; x <= this.endx; x++) {
/*      */         
/*  615 */         for (int y = this.starty; y <= this.endy; y++) {
/*      */           
/*  617 */           checkForEnemiesOn(x, y, true);
/*  618 */           checkForEnemiesOn(x, y, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkForEnemiesOn(int x, int y, boolean onSurface) {
/*  626 */     VolaTile tile = Zones.getTileOrNull(x, y, onSurface);
/*  627 */     if (tile != null) {
/*      */       
/*  629 */       Creature[] creatures = tile.getCreatures();
/*  630 */       for (int c = 0; c < creatures.length; c++) {
/*      */         
/*  632 */         if (isEnemy(creatures[c])) {
/*  633 */           addTarget(creatures[c]);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean isEnemy(Creature creature) {
/*  640 */     return isEnemy(creature, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isEnemy(Creature creature, boolean ignoreInvulnerable) {
/*  645 */     if ((creature.isInvulnerable() && !ignoreInvulnerable) || creature.isUnique()) {
/*  646 */       return false;
/*      */     }
/*  648 */     if (creature.getKingdomId() != 0 && !creature.isFriendlyKingdom(this.kingdom))
/*      */     {
/*  650 */       return true;
/*      */     }
/*  652 */     if (creature.isDominated())
/*      */     {
/*  654 */       if (creature.getDominator() != null) {
/*      */         
/*  656 */         if (isEnemy((creature.getDominator()).citizenVillage))
/*      */         {
/*  658 */           return true;
/*      */         }
/*      */ 
/*      */         
/*  662 */         Reputation rep = this.reputations.get(new Long(creature.dominator));
/*  663 */         if (rep != null && rep.getValue() <= -30)
/*      */         {
/*  665 */           if (creature.getCurrentTile() != null && creature.getCurrentTile().getVillage() == this)
/*      */           {
/*  667 */             return true;
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  675 */     if (!creature.isPlayer()) {
/*      */       
/*  677 */       if (creature.isAggHuman()) {
/*      */         
/*  679 */         if (!creature.isFriendlyKingdom(this.kingdom))
/*      */         {
/*      */           
/*  682 */           return !allowsAggCreatures();
/*      */         }
/*      */ 
/*      */         
/*  686 */         return false;
/*      */       } 
/*      */       
/*  689 */       if ((creature.getTemplate()).isFromValrei && creature.getKingdomId() == 0)
/*      */       {
/*  691 */         return !allowsAggCreatures();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  696 */     if (isEnemy(creature.getCitizenVillage()))
/*      */     {
/*  698 */       return true;
/*      */     }
/*  700 */     if (getReputation(creature) <= -30)
/*      */     {
/*  702 */       if (isWithinMinimumPerimeter(creature.getTileX(), creature.getTileY()))
/*      */       {
/*  704 */         return true;
/*      */       }
/*      */     }
/*  707 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addTarget(Creature creature) {
/*  712 */     if (creature.isInvulnerable() || creature.isUnique())
/*      */       return; 
/*  714 */     if (creature.getCultist() != null && creature.getCultist().hasFearEffect())
/*      */       return; 
/*  716 */     if (creature.isTransferring())
/*      */       return; 
/*  718 */     if (this.guards.size() > 0) {
/*      */       
/*  720 */       if (!isAlerted()) {
/*      */         
/*  722 */         setAlerted(true);
/*  723 */         broadCastAlert(creature.getName() + " raises the settlement alarm!", (byte)4);
/*      */         
/*      */         try {
/*  726 */           if (this.gates != null && this.gates.size() > 0) {
/*  727 */             Server.getInstance().broadCastMessage("A horn sounds and the gates are locked. " + 
/*  728 */                 getName() + " is put on alert!", 
/*  729 */                 getToken().getTileX(), getToken().getTileY(), isOnSurface(), this.endx - this.startx);
/*      */           } else {
/*  731 */             Server.getInstance().broadCastMessage("A horn sounds. " + getName() + " is put on alert!", 
/*  732 */                 getToken().getTileX(), getToken().getTileY(), isOnSurface(), this.endx - this.startx);
/*      */           } 
/*  734 */         } catch (NoSuchItemException nsi) {
/*      */           
/*  736 */           logger.log(Level.WARNING, "No settlement token for " + getName() + ": " + this.tokenId, (Throwable)nsi);
/*      */         } 
/*      */       } 
/*  739 */       if (!this.targets.contains(new Long(creature.getWurmId())))
/*      */       {
/*  741 */         this.targets.add(new Long(creature.getWurmId()));
/*      */       }
/*  743 */       assignTargets();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void assignTargets() {
/*  749 */     if (this.guards.size() > 0 && this.targets.size() > 0) {
/*      */       
/*  751 */       LinkedList<Guard> g = new LinkedList<>();
/*  752 */       g.addAll(this.guards.values());
/*  753 */       Long[] targs = getTargets();
/*  754 */       for (int x = 0; x < targs.length; x++) {
/*      */         
/*  756 */         int guardsAssigned = 0;
/*  757 */         long targid = targs[x].longValue();
/*  758 */         Guard best = null;
/*  759 */         int bestdist = Integer.MAX_VALUE;
/*  760 */         Guard nextBest = null;
/*  761 */         int nextBestdist = Integer.MAX_VALUE;
/*  762 */         Guard thirdBest = null;
/*  763 */         int thirdBestdist = Integer.MAX_VALUE;
/*      */ 
/*      */ 
/*      */         
/*  767 */         if (!g.isEmpty()) {
/*      */           
/*      */           try {
/*      */             
/*  771 */             Creature target = Server.getInstance().getCreature(targid);
/*  772 */             if (!target.isDead()) {
/*      */               
/*  774 */               if (target.getCurrentTile().getTileX() < getStartX() - 5 || target
/*  775 */                 .getCurrentTile().getTileX() > getEndX() + 5 || target
/*  776 */                 .getCurrentTile().getTileY() < getStartY() - 5 || target
/*  777 */                 .getCurrentTile().getTileY() > getEndY() + 5) {
/*  778 */                 removeTarget(target.getWurmId(), false);
/*      */               } else {
/*      */                 
/*  781 */                 for (ListIterator<Guard> it2 = g.listIterator(); it2.hasNext(); ) {
/*      */                   
/*  783 */                   Guard guard = it2.next();
/*  784 */                   if (guard.creature.target == targid) {
/*      */                     
/*  786 */                     guardsAssigned++;
/*  787 */                     it2.remove();
/*  788 */                     if (guardsAssigned >= 3) {
/*      */                       break;
/*      */                     }
/*      */                     continue;
/*      */                   } 
/*  793 */                   if (guard.creature.target == -10L) {
/*      */                     
/*  795 */                     int diffx = (int)Math.abs(guard.creature.getPosX() - target.getPosX());
/*  796 */                     int diffy = (int)Math.abs(guard.creature.getPosY() - target.getPosY());
/*  797 */                     int dist = Math.max(diffx, diffy);
/*  798 */                     if (dist < bestdist) {
/*      */                       
/*  800 */                       best = guard;
/*  801 */                       bestdist = dist; continue;
/*      */                     } 
/*  803 */                     if (dist < nextBestdist) {
/*      */                       
/*  805 */                       nextBest = guard;
/*  806 */                       nextBestdist = dist; continue;
/*      */                     } 
/*  808 */                     if (dist < thirdBestdist) {
/*      */                       
/*  810 */                       thirdBest = guard;
/*  811 */                       thirdBestdist = dist;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */                 
/*  816 */                 if (guardsAssigned < 3 && best != null) {
/*      */                   
/*  818 */                   best.creature.setTarget(targid, false);
/*  819 */                   best.creature.say("I'll take care of " + target.getName() + "!");
/*  820 */                   g.remove(best);
/*  821 */                   guardsAssigned++;
/*  822 */                   if (guardsAssigned < 3 && nextBest != null) {
/*      */                     
/*  824 */                     nextBest.creature.setTarget(targid, false);
/*  825 */                     nextBest.creature.say("I'll help you with " + target.getName() + "!");
/*  826 */                     g.remove(nextBest);
/*  827 */                     guardsAssigned++;
/*      */                   } 
/*  829 */                   if (guardsAssigned < 3 && thirdBest != null) {
/*      */                     
/*  831 */                     thirdBest.creature.setTarget(targid, false);
/*  832 */                     thirdBest.creature.say("I'll help you with " + target.getName() + "!");
/*  833 */                     g.remove(thirdBest);
/*  834 */                     guardsAssigned++;
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             } else {
/*      */               
/*  840 */               this.targets.remove(Long.valueOf(targid));
/*      */             } 
/*  842 */           } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */           
/*      */           }
/*  846 */           catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*      */   public final boolean isAlerted() {
/*  877 */     return this.alerted;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCapital() {
/*  882 */     King k = King.getKing(this.kingdom);
/*  883 */     if (k != null)
/*      */     {
/*  885 */       return k.capital.equalsIgnoreCase(getName());
/*      */     }
/*  887 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addBarrel(Item barrel) {
/*  892 */     this.oilBarrels.add(barrel);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeBarrel(Item barrel) {
/*  897 */     this.oilBarrels.remove(barrel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getOilAmount(int amount, boolean onDeed) {
/*  907 */     if (amount <= 0) {
/*  908 */       return 0;
/*      */     }
/*  910 */     if (this.guards.size() == 0 && !onDeed)
/*  911 */       return 0; 
/*  912 */     if (this.isPermanent)
/*  913 */       return 100; 
/*  914 */     for (Item i : this.oilBarrels) {
/*      */       
/*  916 */       if (!i.isEmpty(false)) {
/*      */         
/*  918 */         Item[] contained = i.getAllItems(false);
/*  919 */         for (Item liquid : contained) {
/*      */           
/*  921 */           if (liquid.isLiquidInflammable()) {
/*      */             
/*  923 */             if (amount >= liquid.getWeightGrams()) {
/*      */               
/*  925 */               Items.destroyItem(liquid.getWurmId());
/*  926 */               return liquid.getWeightGrams();
/*      */             } 
/*      */ 
/*      */             
/*  930 */             liquid.setWeight(liquid.getWeightGrams() - amount, true);
/*  931 */             return amount;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  938 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Long[] getTargets() {
/*  947 */     return this.targets.<Long>toArray(new Long[this.targets.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean containsTarget(Creature creature) {
/*  952 */     return this.targets.contains(new Long(creature.getWurmId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean containsItem(Item item) {
/*  957 */     if (item.getZoneId() > 0)
/*      */     {
/*  959 */       if (getStartX() <= item.getTileX() && getEndX() >= item.getTileX() && 
/*  960 */         getStartY() <= item.getTileY() && getEndY() >= item.getTileY())
/*  961 */         return true; 
/*      */     }
/*  963 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWithinMinimumPerimeter(int tilex, int tiley) {
/*  968 */     if (getStartX() - 5 <= tilex && getEndX() + 5 >= tilex && 
/*  969 */       getStartY() - 5 <= tiley && getEndY() + 5 >= tiley)
/*  970 */       return true; 
/*  971 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWithinAttackPerimeter(int tilex, int tiley) {
/*  976 */     if (getStartX() - 2 <= tilex && getEndX() + 2 >= tilex && 
/*  977 */       getStartY() - 2 <= tiley && getEndY() + 2 >= tiley)
/*  978 */       return true; 
/*  979 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean lessThanWeekLeft() {
/*  984 */     if (this.plan != null)
/*  985 */       return (this.plan.getTimeLeft() < 604800000L); 
/*  986 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean moreThanMonthLeft() {
/*  993 */     if (!isChained())
/*  994 */       return false; 
/*  995 */     if (this.plan != null)
/*  996 */       return (this.plan.getTimeLeft() > 2419200000L); 
/*  997 */     return true;
/*      */   }
/*      */   
/* 1000 */   Village(int aStartX, int aEndX, int aStartY, int aEndY, String aName, Creature aFounder, long aDeedId, boolean aSurfaced, boolean aDemocracy, String aMotto, boolean aPermanent, byte aSpawnKingdom, int initialPerimeter) throws NoSuchCreatureException, NoSuchPlayerException, IOException { this.lastSentPmkWarning = 0L;
/* 1001 */     this.detectedBunny = false; this.citizens = new HashMap<>(); this.group = new Group(aName); Groups.addGroup(this.group); this.roles = new HashMap<>(); this.startx = aStartX; this.endx = aEndX; this.starty = aStartY; this.endy = aEndY; this.name = aName; this.founderName = aFounder.getName(); this.kingdom = aFounder.getKingdomId(); Kingdom k = Kingdoms.getKingdom(this.kingdom); if (k != null) k.setExistsHere(true);  this.mayorName = this.founderName; this.creationDate = System.currentTimeMillis(); this.lastLogin = this.creationDate; this.deedid = aDeedId; this.surfaced = aSurfaced; this.democracy = aDemocracy; this.motto = aMotto; this.isPermanent = aPermanent; this.spawnKingdom = aSpawnKingdom; this.perimeterTiles = initialPerimeter; this.id = create(); this.gates = new HashSet<>(); this.mineDoors = new HashSet<>(); createRoles(); } Village(int aId, int aStartX, int aEndX, int aStartY, int aEndY, String aName, String aFounderName, String aMayor, long aDeedId, boolean aSurfaced, boolean aDemocracy, String aDevise, long _creationDate, boolean aHomestead, long aTokenid, long aDisbandTime, long aDisbId, long aLast, byte aKingdom, long aUpkeep, byte aSettings, boolean aAcceptsHomes, boolean aAcceptsMerchants, int aMaxCitizens, boolean aPermanent, byte aSpawnkingdom, int perimetert, boolean allowsAggro, String _consumerKeyToUse, String _consumerSecretToUse, String _applicationToken, String _applicationSecret, boolean _twitChat, boolean _twitEnabled, float _faithWar, float _faithHeal, float _faithCreate, byte _spawnSituation, int _allianceNumber, short _hotaWins, long lastChangeName, String _motd) { this.lastSentPmkWarning = 0L; this.detectedBunny = false; this.citizens = new HashMap<>(); this.group = new Group(aName); Groups.addGroup(this.group); this.roles = new HashMap<>(); this.startx = aStartX; this.endx = aEndX; this.starty = aStartY; this.endy = aEndY; this.name = aName; this.founderName = aFounderName; this.mayorName = aMayor; this.deedid = aDeedId; this.surfaced = aSurfaced; this.id = aId; this.democracy = aDemocracy; this.motto = aDevise; this.tokenId = aTokenid; this.kingdom = aKingdom; Kingdom k = Kingdoms.getKingdom(this.kingdom); if (k != null)
/*      */       k.setExistsHere(true);  this.gates = new HashSet<>(); this.mineDoors = new HashSet<>(); this.disband = aDisbandTime; this.disbander = aDisbId; this.lastLogin = aLast; this.upkeep = aUpkeep; this.settings.setPermissionBits(aSettings & 0xFF); this.unlimitedCitizens = aAcceptsHomes; this.acceptsMerchants = aAcceptsMerchants; this.maxCitizens = aMaxCitizens; this.isPermanent = aPermanent; this.spawnKingdom = aSpawnkingdom; this.creationDate = _creationDate; this.perimeterTiles = perimetert; this.allowsAggCreatures = allowsAggro; this.consumerKeyToUse = _consumerKeyToUse; this.consumerSecretToUse = _consumerSecretToUse; this.applicationToken = _applicationToken; this.applicationSecret = _applicationSecret; this.twitChat = _twitChat; this.twitEnabled = _twitEnabled; this.faithWar = _faithWar; this.faithHeal = _faithHeal; this.faithCreate = _faithCreate; this.spawnSituation = _spawnSituation; this.allianceNumber = _allianceNumber; this.hotaWins = _hotaWins; this.lastChangedName = lastChangeName; this.motd = _motd; canTwit(); if (!Features.Feature.HIGHWAYS.isEnabled())
/*      */       try { if (this.settings.getPermissions() != 0) { this.settings.setPermissionBits(0); saveSettings(); }  } catch (IOException e) { logger.log(Level.WARNING, e.getMessage(), e); }
/* 1004 */         } final void poll(long now, boolean reduceFaith) { if (logger.isLoggable(Level.FINER))
/*      */     {
/* 1006 */       logger.finer("Polling settlement: " + this);
/*      */     }
/* 1008 */     boolean disb = this.plan.poll();
/* 1009 */     if (disb) {
/*      */       
/* 1011 */       this.disband = now - 1L;
/* 1012 */       disband("upkeep");
/*      */     }
/* 1014 */     else if (checkDisband(now)) {
/*      */       
/* 1016 */       disb = true;
/* 1017 */       String pname = "Unknown Player";
/*      */       
/*      */       try {
/* 1020 */         pname = Players.getInstance().getNameFor(this.disbander);
/*      */       }
/* 1022 */       catch (NoSuchPlayerException nsp) {
/*      */         
/* 1024 */         logger.log(Level.WARNING, "No name for " + this.disbander, (Throwable)nsp);
/*      */       }
/* 1026 */       catch (IOException iox) {
/*      */         
/* 1028 */         logger.log(Level.WARNING, "No name for " + this.disbander, iox);
/*      */       } 
/*      */ 
/*      */       
/* 1032 */       if (disb) {
/* 1033 */         disband(pname);
/*      */       }
/*      */     } else {
/*      */       
/* 1037 */       this.faithDivideVal = Math.max(1.0F, Math.min(3.0F, ((getCitizens()).length - this.plan.getNumHiredGuards()) / 2.0F));
/* 1038 */       if (reduceFaith) {
/*      */         
/* 1040 */         setFaithCreate(Math.max(0.0F, this.faithCreate - Math.max(0.01F, this.faithCreate / 15.0F)));
/* 1041 */         setFaithWar(Math.max(0.0F, this.faithWar - Math.max(0.01F, this.faithWar / 15.0F)));
/* 1042 */         setFaithHeal(Math.max(0.0F, this.faithHeal - Math.max(0.01F, this.faithHeal / 15.0F)));
/*      */       } 
/*      */       
/* 1045 */       if (WurmCalendar.isEaster() && this.isPermanent && (!Servers.localServer.entryServer || Server.getInstance().isPS()))
/*      */       {
/* 1047 */         if (!this.detectedBunny) {
/*      */           
/* 1049 */           for (Citizen citiz : getCitizens()) {
/*      */ 
/*      */             
/*      */             try {
/* 1053 */               Creature bunny = Creatures.getInstance().getCreature(citiz.getId());
/* 1054 */               if (bunny.getTemplate().getTemplateId() == 53) {
/* 1055 */                 this.detectedBunny = true;
/*      */               }
/* 1057 */             } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1062 */           if (!this.detectedBunny) {
/*      */             
/* 1064 */             int tilex = getCenterX();
/* 1065 */             int tiley = getCenterY();
/* 1066 */             boolean ok = false;
/* 1067 */             int tries = 0;
/* 1068 */             while (!ok && tries++ < 100) {
/*      */               
/* 1070 */               switch (Server.rand.nextInt(4)) {
/*      */                 
/*      */                 case 0:
/* 1073 */                   tilex = getStartX() - 20 + Server.rand.nextInt(40);
/*      */                   break;
/*      */                 case 1:
/* 1076 */                   tilex = getEndX() + 20 + Server.rand.nextInt(40);
/*      */                   break;
/*      */                 case 2:
/* 1079 */                   tiley = getEndY() + 20 + Server.rand.nextInt(40);
/*      */                   break;
/*      */                 case 3:
/* 1082 */                   tiley = getStartY() - 20 + Server.rand.nextInt(40);
/*      */                   break;
/*      */               } 
/* 1085 */               VolaTile t = Zones.getTileOrNull(tilex, tiley, true);
/* 1086 */               if (t == null || ((t.getFences()).length == 0 && t.getStructure() == null))
/*      */               {
/* 1088 */                 if (Tiles.decodeHeight(Zones.getTileIntForTile(tilex, tiley, 0)) > 0)
/*      */                 {
/* 1090 */                   ok = true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */             
/*      */             try {
/* 1096 */               byte sex = 0;
/* 1097 */               if (Server.rand.nextBoolean())
/* 1098 */                 sex = 1; 
/* 1099 */               Creature bunny = Creature.doNew(53, true, (tilex * 4 + 2), (tiley * 4 + 2), Server.rand
/* 1100 */                   .nextFloat() * 360.0F, 0, "Easter Bunny", sex, (byte)0, (byte)0, false, (byte)1);
/*      */               
/* 1102 */               addCitizen(bunny, getRole(3));
/* 1103 */               logger.log(Level.INFO, "Created easter bunny for " + getName());
/* 1104 */               this.detectedBunny = true;
/*      */             }
/* 1106 */             catch (Exception ex) {
/*      */               
/* 1108 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1114 */     if (isLeavingPmk())
/*      */     {
/* 1116 */       if (System.currentTimeMillis() - this.lastSentPmkWarning > 1800000L) {
/*      */         
/* 1118 */         Kingdom pmk = Kingdoms.getKingdom(this.kingdom);
/* 1119 */         Kingdom template = Kingdoms.getKingdom(Servers.isThisAChaosServer() ? 4 : pmk
/* 1120 */             .getTemplate());
/* 1121 */         if (pmk != null && pmk.isCustomKingdom()) {
/*      */           
/* 1123 */           if (checkLeavePmk(System.currentTimeMillis())) {
/*      */             
/* 1125 */             this.lastSentPmkWarning = 0L;
/* 1126 */             addHistory(getName(), "converts to " + template.getName() + " from " + pmk.getName() + ".");
/* 1127 */             broadCastAlert(getName() + " leaves " + pmk.getName() + " for " + template.getName() + ".", (byte)4);
/*      */             
/* 1129 */             convertToKingdom(Servers.isThisAChaosServer() ? 4 : pmk
/* 1130 */                 .getTemplate(), false, false);
/*      */           }
/*      */           else {
/*      */             
/* 1134 */             this.lastSentPmkWarning = System.currentTimeMillis();
/* 1135 */             broadCastAlert(getName() + " is leaving " + pmk.getName() + " for " + template.getName() + " in " + 
/* 1136 */                 Server.getTimeFor(this.pmkKickDate - System.currentTimeMillis()) + ".", (byte)4);
/*      */           } 
/*      */         } else {
/*      */           
/* 1140 */           this.pmkKickDate = 0L;
/*      */         } 
/*      */       }  } 
/* 1143 */     if (this.targets.size() > 0) {
/*      */       
/* 1145 */       Long[] targArr = getTargets();
/* 1146 */       for (int x = 0; x < targArr.length; x++) {
/*      */ 
/*      */         
/*      */         try {
/* 1150 */           Creature c = Server.getInstance().getCreature(targArr[x].longValue());
/* 1151 */           VolaTile t = c.getCurrentTile();
/* 1152 */           if (t != null)
/*      */           {
/* 1154 */             if (t.getVillage() != this) {
/* 1155 */               this.targets.remove(targArr[x]);
/*      */             }
/*      */           }
/* 1158 */         } catch (NoSuchPlayerException nsp) {
/*      */           
/* 1160 */           this.targets.remove(targArr[x]);
/*      */         
/*      */         }
/* 1163 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 1165 */           this.targets.remove(targArr[x]);
/*      */         } 
/*      */       } 
/*      */       
/* 1169 */       if (this.targets.size() == 0) {
/*      */         
/* 1171 */         setAlerted(false);
/* 1172 */         broadCastSafe("The danger is over for now.");
/*      */       } 
/*      */     } 
/* 1175 */     if (now - this.lastPolledReps > 7200000L) {
/*      */       
/* 1177 */       if (getVillageReputation() > 0)
/* 1178 */         setVillageRep(getVillageReputation() - 1); 
/* 1179 */       Long[] keys = (Long[])this.reputations.keySet().toArray((Object[])new Long[this.reputations.keySet().size()]);
/* 1180 */       for (int x = 0; x < keys.length; x++) {
/*      */         
/* 1182 */         Reputation r = this.reputations.get(keys[x]);
/* 1183 */         int old = r.getValue();
/* 1184 */         if (old < 0) {
/*      */           
/* 1186 */           r.modify(1);
/* 1187 */           int newr = r.getValue();
/* 1188 */           if (newr >= 0)
/* 1189 */             this.reputations.remove(keys[x]); 
/*      */         } 
/*      */       } 
/* 1192 */       this.lastPolledReps = System.currentTimeMillis();
/*      */     } 
/* 1194 */     if (this.warDeclarations != null) {
/*      */       
/* 1196 */       WarDeclaration[] declArr = (WarDeclaration[])this.warDeclarations.values().toArray((Object[])new WarDeclaration[this.warDeclarations.size()]);
/* 1197 */       for (int x = 0; x < declArr.length; x++) {
/*      */         
/* 1199 */         if (now - (declArr[x]).time > 86400000L)
/*      */         {
/* 1201 */           declArr[x].accept();
/*      */         }
/*      */       } 
/*      */     }  }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeTarget(long target, boolean ignoreFighting) {
/* 1209 */     this.targets.remove(new Long(target));
/* 1210 */     Guard[] _guards = getGuards();
/* 1211 */     boolean fighting = false;
/* 1212 */     for (int x = 0; x < _guards.length; x++) {
/*      */       
/* 1214 */       if ((_guards[x]).creature.target == target)
/*      */       {
/* 1216 */         if (!(_guards[x]).creature.isFighting() || ignoreFighting) {
/*      */           
/* 1218 */           if ((_guards[x]).creature.opponent != null && (_guards[x]).creature.opponent.getWurmId() == target) {
/*      */             
/* 1220 */             (_guards[x]).creature.opponent.setTarget(-10L, true);
/* 1221 */             (_guards[x]).creature.opponent.setOpponent(null);
/* 1222 */             (_guards[x]).creature.setOpponent(null);
/*      */           } 
/* 1224 */           (_guards[x]).creature.setTarget(-10L, true);
/*      */         } else {
/*      */           
/* 1227 */           fighting = true;
/*      */         }  } 
/*      */     } 
/* 1230 */     if (this.targets.size() == 0 && !fighting) {
/* 1231 */       setAlerted(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void removeTarget(Creature target) {
/* 1236 */     this.targets.remove(new Long(target.getWurmId()));
/* 1237 */     Guard[] _guards = getGuards();
/* 1238 */     boolean fighting = false;
/* 1239 */     for (int x = 0; x < _guards.length; x++) {
/*      */       
/* 1241 */       if ((_guards[x]).creature.target == target.getWurmId())
/*      */       {
/* 1243 */         if (!(_guards[x]).creature.isFighting()) {
/*      */           
/* 1245 */           (_guards[x]).creature.setTarget(-10L, true);
/*      */         } else {
/*      */           
/* 1248 */           fighting = true;
/*      */         }  } 
/*      */     } 
/* 1251 */     if (this.targets.size() == 0 && !fighting) {
/* 1252 */       setAlerted(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setAlerted(boolean alert) {
/* 1261 */     int n = 0;
/* 1262 */     if (alert) {
/*      */       
/* 1264 */       for (Iterator<FenceGate> it = this.gates.iterator(); it.hasNext(); ) {
/*      */         
/* 1266 */         FenceGate g = it.next();
/* 1267 */         if (g.startAlert((n == 0)))
/* 1268 */           n++; 
/*      */       } 
/* 1270 */       this.alerted = true;
/* 1271 */       if (this.plan != null) {
/* 1272 */         this.plan.startSiege();
/*      */       }
/* 1274 */     } else if (this.alerted) {
/*      */       
/* 1276 */       for (Iterator<FenceGate> it = this.gates.iterator(); it.hasNext(); ) {
/*      */         
/* 1278 */         FenceGate g = it.next();
/* 1279 */         if (g.endAlert((n == 0)))
/* 1280 */           n++; 
/*      */       } 
/* 1282 */       this.alerted = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected VillageRole createRoleEverybody() {
/* 1288 */     if (this.everybody == null) {
/*      */       
/*      */       try {
/*      */         
/* 1292 */         Permissions settings = new Permissions();
/* 1293 */         Permissions moreSettings = new Permissions();
/* 1294 */         Permissions extraSettings = new Permissions();
/* 1295 */         boolean atknon = Servers.localServer.PVPSERVER;
/* 1296 */         if (atknon) {
/* 1297 */           settings.setPermissionBit(VillageRole.RolePermissions.ATTACK_NON_CITIZENS.getBit(), true);
/*      */         }
/* 1299 */         this
/*      */ 
/*      */           
/* 1302 */           .everybody = new DbVillageRole(this.id, "non-citizens", false, false, false, false, false, false, false, false, false, false, false, false, false, false, atknon, false, false, (byte)1, 0, false, false, false, false, false, false, false, false, false, -10L, settings.getPermissions(), moreSettings.getPermissions(), extraSettings.getPermissions());
/* 1303 */         this.roles.put(Integer.valueOf(this.everybody.getId()), this.everybody);
/*      */       }
/* 1305 */       catch (IOException iox) {
/*      */         
/* 1307 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     }
/* 1310 */     return this.everybody;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPerimeterSize() {
/* 1315 */     return this.perimeterTiles;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTotalPerimeterSize() {
/* 1321 */     return this.perimeterTiles + 5;
/*      */   }
/*      */ 
/*      */   
/*      */   private VillageRole createRoleGuard() {
/* 1326 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 1329 */       Permissions settings = new Permissions();
/* 1330 */       Permissions moreSettings = new Permissions();
/* 1331 */       Permissions extraSettings = new Permissions();
/* 1332 */       settings.setPermissionBit(VillageRole.RolePermissions.ATTACK_CITIZENS.getBit(), true);
/* 1333 */       settings.setPermissionBit(VillageRole.RolePermissions.ATTACK_NON_CITIZENS.getBit(), true);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1338 */       role = new DbVillageRole(this.id, "guard", false, true, false, true, false, false, false, false, false, true, true, true, false, true, true, true, true, (byte)4, 0, true, false, false, false, false, false, false, false, false, -10L, settings.getPermissions(), moreSettings.getPermissions(), extraSettings.getPermissions());
/* 1339 */       this.roles.put(Integer.valueOf(role.getId()), role);
/*      */     }
/* 1341 */     catch (IOException iox) {
/*      */       
/* 1343 */       logger.log(Level.WARNING, "Failed to create role guard for settlement " + getName() + " " + iox.getMessage(), iox);
/*      */     } 
/* 1345 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   private VillageRole createRoleWagoner() {
/* 1350 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 1353 */       Permissions settings = new Permissions();
/* 1354 */       Permissions moreSettings = new Permissions();
/* 1355 */       Permissions extraSettings = new Permissions();
/*      */       
/* 1357 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.PICKUP.getBit(), true);
/* 1358 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.LOAD.getBit(), true);
/* 1359 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.UNLOAD.getBit(), true);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1364 */       role = new DbVillageRole(this.id, "wagoner", false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, (byte)6, 0, false, false, false, true, false, true, false, false, false, -10L, settings.getPermissions(), moreSettings.getPermissions(), extraSettings.getPermissions());
/* 1365 */       this.roles.put(Integer.valueOf(role.getId()), role);
/*      */     }
/* 1367 */     catch (IOException iox) {
/*      */       
/* 1369 */       logger.log(Level.WARNING, "Failed to create role wagoner for settlement " + getName() + " " + iox.getMessage(), iox);
/*      */     } 
/* 1371 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   private VillageRole createRoleCitizen() {
/* 1376 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 1379 */       Permissions settings = new Permissions();
/* 1380 */       Permissions moreSettings = new Permissions();
/* 1381 */       Permissions extraSettings = new Permissions();
/*      */       
/* 1383 */       settings.setPermissionBit(VillageRole.RolePermissions.BUTCHER.getBit(), true);
/* 1384 */       settings.setPermissionBit(VillageRole.RolePermissions.GROOM.getBit(), true);
/* 1385 */       settings.setPermissionBit(VillageRole.RolePermissions.LEAD.getBit(), true);
/* 1386 */       settings.setPermissionBit(VillageRole.RolePermissions.MILK_SHEAR.getBit(), true);
/* 1387 */       settings.setPermissionBit(VillageRole.RolePermissions.TAME.getBit(), true);
/*      */ 
/*      */ 
/*      */       
/* 1391 */       settings.setPermissionBit(VillageRole.RolePermissions.DIG_RESOURCE.getBit(), true);
/*      */       
/* 1393 */       settings.setPermissionBit(VillageRole.RolePermissions.SOW_FIELDS.getBit(), true);
/* 1394 */       settings.setPermissionBit(VillageRole.RolePermissions.TEND_FIELDS.getBit(), true);
/*      */       
/* 1396 */       settings.setPermissionBit(VillageRole.RolePermissions.CHOP_DOWN_OLD_TREES.getBit(), true);
/* 1397 */       settings.setPermissionBit(VillageRole.RolePermissions.CUT_GRASS.getBit(), true);
/* 1398 */       settings.setPermissionBit(VillageRole.RolePermissions.HARVEST_FRUIT.getBit(), true);
/* 1399 */       settings.setPermissionBit(VillageRole.RolePermissions.MAKE_LAWN.getBit(), true);
/* 1400 */       settings.setPermissionBit(VillageRole.RolePermissions.PICK_SPROUTS.getBit(), true);
/* 1401 */       settings.setPermissionBit(VillageRole.RolePermissions.PLANT_FLOWERS.getBit(), true);
/* 1402 */       settings.setPermissionBit(VillageRole.RolePermissions.PLANT_SPROUTS.getBit(), true);
/* 1403 */       settings.setPermissionBit(VillageRole.RolePermissions.PRUNE.getBit(), true);
/*      */       
/* 1405 */       settings.setPermissionBit(VillageRole.RolePermissions.ATTACK_NON_CITIZENS.getBit(), true);
/* 1406 */       settings.setPermissionBit(VillageRole.RolePermissions.FORAGE.getBit(), true);
/* 1407 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MEDITATION_ABILITY.getBit(), true);
/*      */       
/* 1409 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.IMPROVE_REPAIR.getBit(), true);
/* 1410 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.PICKUP.getBit(), true);
/* 1411 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.PULL_PUSH.getBit(), true);
/*      */       
/* 1413 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_IRON.getBit(), true);
/* 1414 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_OTHER.getBit(), true);
/* 1415 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_ROCK.getBit(), true);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1420 */       role = new DbVillageRole(this.id, "citizen", false, true, false, true, false, false, false, false, false, true, true, false, false, false, true, true, true, (byte)3, 0, true, false, true, true, true, true, true, false, false, -10L, settings.getPermissions(), moreSettings.getPermissions(), extraSettings.getPermissions());
/* 1421 */       this.roles.put(Integer.valueOf(role.getId()), role);
/*      */     }
/* 1423 */     catch (IOException iox) {
/*      */       
/* 1425 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 1427 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   private VillageRole createRoleMayor() {
/* 1432 */     String title = "mayor";
/*      */     
/* 1434 */     boolean mayinvite = true;
/*      */     
/* 1436 */     VillageRole role = null;
/*      */ 
/*      */     
/*      */     try {
/* 1440 */       int permissions = -1;
/* 1441 */       int morePermissions = -1;
/* 1442 */       int extraPermissions = -1;
/*      */       
/* 1444 */       role = new DbVillageRole(this.id, "mayor", true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, (byte)2, 0, true, true, true, true, true, true, true, true, true, -10L, -1, -1, -1);
/*      */ 
/*      */ 
/*      */       
/* 1448 */       this.roles.put(Integer.valueOf(role.getId()), role);
/*      */     }
/* 1450 */     catch (IOException iox) {
/*      */       
/* 1452 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 1454 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getReputation(Creature creature) {
/* 1459 */     if (creature.getKingdomId() != 0 && !creature.isFriendlyKingdom(this.kingdom))
/* 1460 */       return -100; 
/* 1461 */     long wid = creature.getWurmId();
/* 1462 */     if (creature.getCitizenVillage() != null && 
/* 1463 */       isEnemy(creature.getCitizenVillage()))
/* 1464 */       return -100; 
/* 1465 */     Reputation rep = this.reputations.get(new Long(wid));
/* 1466 */     if (rep != null)
/*      */     {
/* 1468 */       return rep.getValue();
/*      */     }
/* 1470 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getReputation(long wid) {
/* 1475 */     Village vill = Villages.getVillageForCreature(wid);
/* 1476 */     if (vill != null && 
/* 1477 */       isEnemy(vill))
/* 1478 */       return -100; 
/* 1479 */     Reputation rep = this.reputations.get(new Long(wid));
/* 1480 */     if (rep != null)
/*      */     {
/* 1482 */       return rep.getValue();
/*      */     }
/* 1484 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reputation[] getReputations() {
/* 1493 */     return (Reputation[])this.reputations.values().toArray((Object[])new Reputation[this.reputations.values().size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   protected VillageRole createRoleAlly() {
/* 1498 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 1501 */       Permissions settings = new Permissions();
/* 1502 */       Permissions moreSettings = new Permissions();
/* 1503 */       Permissions extraSettings = new Permissions();
/*      */       
/* 1505 */       settings.setPermissionBit(VillageRole.RolePermissions.BUTCHER.getBit(), true);
/* 1506 */       settings.setPermissionBit(VillageRole.RolePermissions.GROOM.getBit(), true);
/* 1507 */       settings.setPermissionBit(VillageRole.RolePermissions.LEAD.getBit(), true);
/* 1508 */       settings.setPermissionBit(VillageRole.RolePermissions.MILK_SHEAR.getBit(), true);
/* 1509 */       settings.setPermissionBit(VillageRole.RolePermissions.TAME.getBit(), true);
/*      */ 
/*      */ 
/*      */       
/* 1513 */       settings.setPermissionBit(VillageRole.RolePermissions.DIG_RESOURCE.getBit(), true);
/*      */       
/* 1515 */       settings.setPermissionBit(VillageRole.RolePermissions.SOW_FIELDS.getBit(), true);
/* 1516 */       settings.setPermissionBit(VillageRole.RolePermissions.TEND_FIELDS.getBit(), true);
/*      */       
/* 1518 */       settings.setPermissionBit(VillageRole.RolePermissions.CHOP_DOWN_OLD_TREES.getBit(), true);
/* 1519 */       settings.setPermissionBit(VillageRole.RolePermissions.CUT_GRASS.getBit(), true);
/* 1520 */       settings.setPermissionBit(VillageRole.RolePermissions.HARVEST_FRUIT.getBit(), true);
/* 1521 */       settings.setPermissionBit(VillageRole.RolePermissions.MAKE_LAWN.getBit(), true);
/* 1522 */       settings.setPermissionBit(VillageRole.RolePermissions.PICK_SPROUTS.getBit(), true);
/* 1523 */       settings.setPermissionBit(VillageRole.RolePermissions.PLANT_FLOWERS.getBit(), true);
/* 1524 */       settings.setPermissionBit(VillageRole.RolePermissions.PLANT_SPROUTS.getBit(), true);
/* 1525 */       settings.setPermissionBit(VillageRole.RolePermissions.PRUNE.getBit(), true);
/*      */       
/* 1527 */       settings.setPermissionBit(VillageRole.RolePermissions.ATTACK_NON_CITIZENS.getBit(), true);
/* 1528 */       settings.setPermissionBit(VillageRole.RolePermissions.FORAGE.getBit(), true);
/* 1529 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MEDITATION_ABILITY.getBit(), true);
/*      */       
/* 1531 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.IMPROVE_REPAIR.getBit(), true);
/* 1532 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.PULL_PUSH.getBit(), true);
/*      */       
/* 1534 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_IRON.getBit(), true);
/* 1535 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_OTHER.getBit(), true);
/* 1536 */       moreSettings.setPermissionBit(VillageRole.MoreRolePermissions.MINE_ROCK.getBit(), true);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1541 */       role = new DbVillageRole(this.id, "ally", false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, (byte)5, 0, false, false, true, false, false, false, false, false, false, -10L, settings.getPermissions(), moreSettings.getPermissions(), extraSettings.getPermissions());
/* 1542 */       this.roles.put(Integer.valueOf(role.getId()), role);
/*      */     }
/* 1544 */     catch (IOException iox) {
/*      */       
/* 1546 */       logger.log(Level.WARNING, "Failed to create role allied for settlement " + getName() + " " + iox.getMessage(), iox);
/*      */     } 
/* 1548 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void resetRoles() {
/* 1553 */     Citizen mayor = getMayor();
/* 1554 */     VillageRole[] roleArr = getRoles();
/* 1555 */     Set<Citizen> guardSet = new HashSet<>();
/* 1556 */     Set<Citizen> wagonerSet = new HashSet<>();
/*      */     
/* 1558 */     Citizen[] citiz = getCitizens();
/* 1559 */     for (int y = 0; y < citiz.length; y++) {
/*      */       
/* 1561 */       if (citiz[y].getRole().getStatus() == 4)
/* 1562 */         guardSet.add(citiz[y]); 
/* 1563 */       if (citiz[y].getRole().getStatus() == 6)
/* 1564 */         wagonerSet.add(citiz[y]); 
/*      */     } 
/* 1566 */     for (int x = 0; x < roleArr.length; x++) {
/*      */ 
/*      */       
/*      */       try {
/* 1570 */         roleArr[x].delete();
/*      */       }
/* 1572 */       catch (IOException iox) {
/*      */         
/* 1574 */         logger.log(Level.WARNING, getName() + " role: " + roleArr[x].getName() + " " + iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/* 1577 */     this.roles.clear();
/* 1578 */     if (this.allianceNumber > 0)
/* 1579 */       createRoleAlly(); 
/* 1580 */     VillageRole citizRole = createRoleCitizen();
/*      */     
/* 1582 */     this.everybody = null;
/* 1583 */     createRoleEverybody();
/* 1584 */     VillageRole mayorRole = createRoleMayor();
/* 1585 */     VillageRole guardRole = null;
/* 1586 */     VillageRole wagonerRole = null;
/*      */     
/* 1588 */     if (guardSet.size() > 0) {
/*      */       
/* 1590 */       guardRole = createRoleGuard();
/* 1591 */       for (Iterator<Citizen> it = guardSet.iterator(); it.hasNext(); ) {
/*      */         
/* 1593 */         Citizen g = it.next();
/*      */         
/*      */         try {
/* 1596 */           g.setRole(guardRole);
/*      */         }
/* 1598 */         catch (IOException iox0) {
/*      */           
/* 1600 */           logger.log(Level.WARNING, getName(), iox0);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1604 */     if (wagonerSet.size() > 0) {
/*      */       
/* 1606 */       wagonerRole = createRoleWagoner();
/* 1607 */       for (Iterator<Citizen> it = wagonerSet.iterator(); it.hasNext(); ) {
/*      */         
/* 1609 */         Citizen g = it.next();
/*      */         
/*      */         try {
/* 1612 */           g.setRole(wagonerRole);
/*      */         }
/* 1614 */         catch (IOException iox0) {
/*      */           
/* 1616 */           logger.log(Level.WARNING, getName(), iox0);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1620 */     for (int i = 0; i < citiz.length; i++) {
/*      */       
/* 1622 */       if (citiz[i] != mayor) {
/*      */ 
/*      */         
/*      */         try {
/* 1626 */           boolean addRole = true;
/* 1627 */           if (guardRole != null && citiz[i].getRole() == guardRole)
/* 1628 */             addRole = false; 
/* 1629 */           if (wagonerRole != null && citiz[i].getRole() == wagonerRole)
/* 1630 */             addRole = false; 
/* 1631 */           if (addRole) {
/* 1632 */             citiz[i].setRole(citizRole);
/*      */           }
/* 1634 */         } catch (IOException iox) {
/*      */           
/* 1636 */           logger.log(Level.WARNING, getName(), iox);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1643 */           citiz[i].setRole(mayorRole);
/*      */         }
/* 1645 */         catch (IOException iox) {
/*      */           
/* 1647 */           logger.log(Level.WARNING, getName(), iox);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final short[] getSpawnPoint() {
/* 1655 */     int x = getCenterX();
/* 1656 */     int y = getCenterY();
/* 1657 */     int tries = 0;
/* 1658 */     while (tries < 10) {
/*      */       
/* 1660 */       tries++;
/* 1661 */       x = Server.rand.nextInt(this.endx - this.startx) + this.startx;
/* 1662 */       y = Server.rand.nextInt(this.endy - this.starty) + this.starty;
/* 1663 */       VolaTile tile = Zones.getTileOrNull(x, y, isOnSurface());
/* 1664 */       if (tile == null) {
/* 1665 */         return new short[] { (short)x, (short)y };
/*      */       }
/*      */     } 
/* 1668 */     x = getCenterX();
/* 1669 */     y = getCenterY();
/*      */     
/*      */     try {
/* 1672 */       Item token = getToken();
/* 1673 */       x = (int)token.getPosX() >> 2;
/* 1674 */       y = (int)token.getPosY() >> 2;
/*      */     }
/* 1676 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1678 */       logger.log(Level.WARNING, "No token found for settlement " + getName(), (Throwable)nsi);
/*      */     } 
/* 1680 */     return new short[] { (short)x, (short)y };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final short[] getTokenCoords() throws NoSuchItemException {
/* 1686 */     Item token = getToken();
/* 1687 */     int x = (int)token.getPosX() >> 2;
/* 1688 */     int y = (int)token.getPosY() >> 2;
/* 1689 */     return new short[] { (short)x, (short)y };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getStartX() {
/* 1695 */     return this.startx;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getEndX() {
/* 1700 */     return this.endx;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getStartY() {
/* 1705 */     return this.starty;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getEndY() {
/* 1710 */     return this.endy;
/*      */   }
/*      */ 
/*      */   
/*      */   final int getCenterX() {
/* 1715 */     return this.startx + (this.endx - this.startx) / 2;
/*      */   }
/*      */ 
/*      */   
/*      */   final int getCenterY() {
/* 1720 */     return this.starty + (this.endy - this.starty) / 2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTokenX() {
/*      */     try {
/* 1727 */       Item token = getToken();
/* 1728 */       return (int)token.getPosX() >> 2;
/*      */     }
/* 1730 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/* 1733 */       return this.startx + (this.endx - this.startx) / 2;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTokenY() {
/*      */     try {
/* 1741 */       Item token = getToken();
/* 1742 */       return (int)token.getPosY() >> 2;
/*      */     }
/* 1744 */     catch (NoSuchItemException e) {
/*      */ 
/*      */       
/* 1747 */       return this.starty + (this.endy - this.starty) / 2;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Set<FenceGate> getGates() {
/* 1757 */     return this.gates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addGate(FenceGate gate) {
/* 1764 */     if (!this.gates.contains(gate)) {
/*      */       
/* 1766 */       this.gates.add(gate);
/* 1767 */       gate.setVillage(this);
/*      */       
/*      */       try {
/* 1770 */         Item lock = gate.getLock();
/* 1771 */         lock.addKey(this.deedid);
/*      */       }
/* 1773 */       catch (NoSuchLockException noSuchLockException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Set<MineDoorPermission> getMineDoors() {
/* 1782 */     return this.mineDoors;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addMineDoor(MineDoorPermission mineDoor) {
/* 1787 */     if (!this.mineDoors.contains(mineDoor)) {
/*      */       
/* 1789 */       this.mineDoors.add(mineDoor);
/* 1790 */       mineDoor.setVillage(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasAllies() {
/* 1796 */     return (this.allianceNumber > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isEnemy(Village village) {
/* 1801 */     if (village == null)
/* 1802 */       return false; 
/* 1803 */     if (village.kingdom != this.kingdom)
/*      */     {
/* 1805 */       if (!Kingdoms.getKingdom(this.kingdom).isAllied(village.kingdom)) {
/* 1806 */         return true;
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
/* 1822 */     if (this.wars != null) {
/* 1823 */       return (this.wars.get(village) != null);
/*      */     }
/* 1825 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   final void addWar(VillageWar war) {
/* 1830 */     if (this.wars == null)
/* 1831 */       this.wars = new HashMap<>(); 
/* 1832 */     Village opponent = war.getVilltwo();
/* 1833 */     if (opponent == this)
/* 1834 */       opponent = war.getVillone(); 
/* 1835 */     if (opponent != this)
/*      */     {
/* 1837 */       if (!isEnemy(opponent)) {
/*      */         
/* 1839 */         this.wars.put(opponent, war);
/* 1840 */         if (isAlly(opponent))
/*      */         {
/* 1842 */           if (this.allianceNumber == getId()) {
/*      */ 
/*      */             
/* 1845 */             opponent.setAllianceNumber(0);
/*      */           }
/* 1847 */           else if (opponent.getId() == this.allianceNumber) {
/*      */ 
/*      */             
/* 1850 */             setAllianceNumber(0);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayDeclareWarOn(Village village) {
/* 1859 */     if (village == this)
/*      */     {
/* 1861 */       return false;
/*      */     }
/* 1863 */     if (!Servers.localServer.PVPSERVER)
/*      */     {
/* 1865 */       return false;
/*      */     }
/* 1867 */     if (village.kingdom != this.kingdom)
/*      */     {
/* 1869 */       return false;
/*      */     }
/* 1871 */     if (village.isPermanent)
/*      */     {
/* 1873 */       return false;
/*      */     }
/* 1875 */     if (village.getVillageReputation() < 50 && !Servers.isThisAChaosServer()) {
/*      */       
/* 1877 */       Kingdom kingd = Kingdoms.getKingdom(this.kingdom);
/* 1878 */       if (!kingd.isCustomKingdom() || isCapital())
/* 1879 */         return false; 
/*      */     } 
/* 1881 */     if (this.warDeclarations != null)
/*      */     {
/* 1883 */       if (this.warDeclarations.containsKey(village))
/* 1884 */         return false; 
/*      */     }
/* 1886 */     if (this.wars != null)
/*      */     {
/* 1888 */       if (this.wars.containsKey(village))
/* 1889 */         return false; 
/*      */     }
/* 1891 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAtPeaceWith(Village village) {
/* 1896 */     if (!Servers.localServer.PVPSERVER)
/* 1897 */       return true; 
/* 1898 */     if (village.kingdom != this.kingdom)
/* 1899 */       return false; 
/* 1900 */     if (village.isPermanent)
/* 1901 */       return true; 
/* 1902 */     if (this.warDeclarations != null)
/*      */     {
/* 1904 */       if (this.warDeclarations.containsKey(village))
/* 1905 */         return false; 
/*      */     }
/* 1907 */     if (this.wars != null)
/*      */     {
/* 1909 */       if (this.wars.containsKey(village))
/* 1910 */         return false; 
/*      */     }
/* 1912 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   final void addWarDeclaration(WarDeclaration declaration) {
/* 1917 */     if (this.warDeclarations == null)
/* 1918 */       this.warDeclarations = new HashMap<>(); 
/* 1919 */     boolean wedeclare = false;
/* 1920 */     Village opponent = declaration.declarer;
/* 1921 */     if (opponent == this) {
/*      */       
/* 1923 */       wedeclare = true;
/* 1924 */       opponent = declaration.receiver;
/*      */     } 
/* 1926 */     if (opponent != this) {
/*      */       
/* 1928 */       if (this.warDeclarations.containsKey(opponent)) {
/*      */         return;
/*      */       }
/*      */       
/* 1932 */       this.warDeclarations.put(opponent, declaration);
/* 1933 */       if (isAlly(opponent)) {
/*      */         
/* 1935 */         if (this.allianceNumber == getId()) {
/*      */ 
/*      */           
/* 1938 */           opponent.setAllianceNumber(0);
/*      */         }
/* 1940 */         else if (opponent.getId() == this.allianceNumber) {
/*      */ 
/*      */           
/* 1943 */           setAllianceNumber(0);
/*      */         } 
/* 1945 */         if (wedeclare) {
/* 1946 */           broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has declared war with the treacherous " + opponent
/* 1947 */               .getName() + ". Citizens, be strong and brave!");
/*      */         } else {
/*      */           
/* 1950 */           broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has been challenged by the treacherous " + opponent
/* 1951 */               .getName() + " and will be forced into war. Citizens, be strong and brave!");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1956 */       else if (wedeclare) {
/* 1957 */         broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has declared war with the cowardly " + opponent
/* 1958 */             .getName() + ". Citizens, be strong and brave!");
/*      */       } else {
/*      */         
/* 1961 */         broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has been challenged by the cowardly " + opponent
/* 1962 */             .getName() + ". Citizens, be strong and brave - war is coming our way!");
/*      */       } 
/*      */       
/* 1965 */       addHistory(opponent.getName(), "is now under war declaration");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1970 */       logger.log(Level.WARNING, "Added declaration to " + 
/* 1971 */           getName() + " but the war is for " + declaration.declarer.getName() + " and " + declaration.receiver
/* 1972 */           .getName() + ". Deleting.");
/* 1973 */       declaration.delete();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void startWar(VillageWar war, boolean wedeclare) {
/* 1979 */     if (this.wars == null)
/* 1980 */       this.wars = new HashMap<>(); 
/* 1981 */     Village opponent = war.getVilltwo();
/* 1982 */     if (opponent == this)
/* 1983 */       opponent = war.getVillone(); 
/* 1984 */     if (opponent != this) {
/*      */       
/* 1986 */       if (!isEnemy(opponent))
/*      */       {
/* 1988 */         this.wars.put(opponent, war);
/* 1989 */         if (isAlly(opponent)) {
/*      */           
/* 1991 */           if (this.allianceNumber == getId()) {
/*      */ 
/*      */             
/* 1994 */             opponent.setAllianceNumber(0);
/*      */           }
/* 1996 */           else if (opponent.getId() == this.allianceNumber) {
/*      */ 
/*      */             
/* 1999 */             setAllianceNumber(0);
/*      */           } 
/*      */           
/* 2002 */           if (wedeclare) {
/* 2003 */             broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has decided to go to war with the treacherous " + opponent
/* 2004 */                 .getName() + ". Citizens, be strong and brave!");
/*      */           } else {
/*      */             
/* 2007 */             broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " was betrayed by the treacherous " + opponent
/* 2008 */                 .getName() + " and forced into war. Citizens, be strong and brave!");
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2013 */         else if (wedeclare) {
/* 2014 */           broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has decided to go to war with the cowardly " + opponent
/* 2015 */               .getName() + ". Citizens, be strong and brave!");
/*      */         } else {
/*      */           
/* 2018 */           broadCastNormal("Under the rule of " + getMayor().getName() + ", " + getName() + " has been attacked by the cowardly " + opponent
/* 2019 */               .getName() + ". Citizens, be strong and brave - we go to war!");
/*      */         } 
/*      */         
/* 2022 */         addHistory(opponent.getName(), "is now a deadly enemy");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2027 */       logger.log(Level.WARNING, "Added war to " + getName() + " but the war is for " + war.getVilltwo().getName() + " and " + war
/* 2028 */           .getVillone().getName() + ". Deleting.");
/* 2029 */       war.delete();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAlly(Village village) {
/* 2035 */     if (village != null)
/*      */     {
/* 2037 */       return (this.allianceNumber > 0 && village.getAllianceNumber() == this.allianceNumber);
/*      */     }
/*      */     
/* 2040 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Village[] getEnemies() {
/* 2045 */     if (this.wars != null && this.wars.size() > 0) {
/* 2046 */       return (Village[])this.wars.keySet().toArray((Object[])new Village[this.wars.size()]);
/*      */     }
/* 2048 */     return new Village[0];
/*      */   }
/*      */ 
/*      */   
/*      */   final void declarePeace(Creature breaker, Creature accepter, Village village, boolean webreak) {
/* 2053 */     if (this.wars != null || this.warDeclarations != null) {
/*      */       
/* 2055 */       if (webreak) {
/*      */         
/* 2057 */         broadCastNormal("The wise " + breaker.getName() + " has ended the war with " + village.getName() + " through their intermediary " + accepter
/* 2058 */             .getName() + ". Amnesty for all perpetrators is declared.");
/* 2059 */         addHistory(breaker.getName(), "ends the war with " + village.getName());
/*      */       }
/*      */       else {
/*      */         
/* 2063 */         broadCastNormal(breaker.getName() + " of " + village.getName() + " has been given the grace of peace with " + 
/* 2064 */             getName() + " through the wise " + accepter.getName() + "! Amnesty for all perpetrators is declared.");
/* 2065 */         addHistory(accepter.getName(), "accepts peace with " + village.getName());
/*      */       } 
/* 2067 */       declarePeace(village);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final void removeWarDeclaration(Village village) {
/* 2073 */     declarePeace(village);
/* 2074 */     addHistory("someone", "removes the war declaration from " + village.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final void declarePeace(Village village) {
/* 2079 */     if (this.wars != null) {
/*      */       
/* 2081 */       Citizen[] vcitiz = village.getCitizens();
/* 2082 */       for (int x = 0; x < vcitiz.length; x++)
/* 2083 */         removeReputation(vcitiz[x].getId()); 
/* 2084 */       this.wars.remove(village);
/*      */     } 
/* 2086 */     if (this.warDeclarations != null) {
/*      */       
/* 2088 */       WarDeclaration decl = this.warDeclarations.remove(village);
/* 2089 */       if (decl != null) {
/* 2090 */         decl.delete();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final Village[] getAllies() {
/* 2096 */     if (this.allianceNumber > 0) {
/*      */       
/* 2098 */       PvPAlliance pvpall = PvPAlliance.getPvPAlliance(this.allianceNumber);
/* 2099 */       if (pvpall != null) {
/* 2100 */         return pvpall.getVillages();
/*      */       }
/* 2102 */       logger.log(Level.WARNING, getName() + " has allianceNumber " + this.allianceNumber + " which doesn't exist.");
/*      */     } 
/* 2104 */     return emptyVillages;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getAllianceName() {
/* 2109 */     if (this.allianceNumber > 0) {
/*      */       
/* 2111 */       PvPAlliance alliance = PvPAlliance.getPvPAlliance(this.allianceNumber);
/* 2112 */       if (alliance != null)
/* 2113 */         return alliance.getName(); 
/*      */     } 
/* 2115 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAlly(Creature creature) {
/* 2120 */     Village village = creature.getCitizenVillage();
/* 2121 */     if (village == null)
/* 2122 */       return false; 
/* 2123 */     if (this.allianceNumber > 0)
/*      */     {
/*      */       
/* 2126 */       if (village.getAllianceNumber() == this.allianceNumber) {
/*      */         
/* 2128 */         Citizen cit = village.getCitizen(creature.getWurmId());
/* 2129 */         if (cit != null) {
/*      */           
/* 2131 */           VillageRole vr = cit.getRole();
/* 2132 */           return (vr != null && vr.mayPerformActionsOnAlliedDeeds());
/*      */         } 
/*      */       } 
/*      */     }
/* 2136 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void replaceDeed(Creature performer, Item oldDeed) {
/* 2141 */     long oldDeedid = this.deedid;
/* 2142 */     long newDeedid = -10L;
/* 2143 */     if (oldDeedid != oldDeed.getWurmId()) {
/*      */       
/* 2145 */       performer.getCommunicator().sendNormalServerMessage("This deed is not registered for this settlement called " + 
/* 2146 */           getName() + ".");
/* 2147 */       logger.log(Level.WARNING, this.deedid + " does not match " + oldDeed.getWurmId() + " for " + performer.getName() + " in settlement " + 
/* 2148 */           getName());
/*      */       return;
/*      */     } 
/* 2151 */     long deedVal = oldDeed.getValue();
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2156 */       ItemTemplate newdeedtype = ItemTemplateFactory.getInstance().getTemplate(663);
/*      */       
/* 2158 */       long toreimb = deedVal - newdeedtype.getValue();
/* 2159 */       if (toreimb > 0L) {
/*      */         
/* 2161 */         LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 2162 */         if (!lsw.addMoney(performer.getWurmId(), performer.getName(), toreimb, "Replace" + oldDeedid)) {
/*      */           
/* 2164 */           performer.getCommunicator().sendSafeServerMessage("Failed to contact your bank. Please try later.");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2169 */         Items.destroyItem(oldDeedid);
/*      */       }
/*      */       else {
/*      */         
/* 2173 */         Items.destroyItem(oldDeedid);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2182 */       Item newDeed = ItemFactory.createItem(663, 50.0F + Server.rand
/* 2183 */           .nextFloat() * 50.0F, performer.getName());
/* 2184 */       newDeed.setName("Settlement deed");
/* 2185 */       performer.getInventory().insertItem(newDeed, true);
/*      */       
/*      */       try {
/* 2188 */         newDeed.setDescription(getName());
/* 2189 */         newDeed.setData2(this.id);
/* 2190 */         setDeedId(newDeed.getWurmId());
/* 2191 */         if (this.gates != null)
/*      */         {
/* 2193 */           for (Iterator<FenceGate> it = this.gates.iterator(); it.hasNext(); ) {
/*      */             
/* 2195 */             FenceGate gate = it.next();
/*      */             
/*      */             try {
/* 2198 */               Item lock = gate.getLock();
/* 2199 */               lock.addKey(-10L);
/* 2200 */               lock.removeKey(oldDeedid);
/*      */             }
/* 2202 */             catch (NoSuchLockException noSuchLockException) {}
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2208 */         performer.addKey(newDeed, false);
/* 2209 */         this.plan.hiredGuardNumber = 50;
/* 2210 */         int newNum = 0;
/* 2211 */         this.plan.changePlan(0, newNum);
/* 2212 */         newNum = 3;
/* 2213 */         this.plan.changePlan(0, newNum);
/*      */ 
/*      */         
/*      */         try {
/* 2217 */           if (!getRoleForStatus((byte)2).mayInviteCitizens())
/*      */           {
/* 2219 */             logger.log(Level.INFO, "Set mayor to be able to invite for " + getName());
/* 2220 */             getRoleForStatus((byte)2).setMayInvite(true);
/*      */           }
/*      */         
/* 2223 */         } catch (NoSuchRoleException nsr) {
/*      */           
/* 2225 */           logger.log(Level.INFO, "Failed to find mayo role to invite for " + performer.getName());
/*      */         } 
/* 2227 */         performer
/* 2228 */           .getCommunicator()
/* 2229 */           .sendAlertServerMessage("You will be set to " + newNum + " heavy guards. You need to manage guards in order to make sure you have the desired amount.");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2234 */       catch (IOException iox) {
/*      */         
/* 2236 */         performer.getCommunicator().sendNormalServerMessage("A server error occured while saving the new deed id.");
/* 2237 */         logger.log(Level.WARNING, iox.getMessage(), iox); return;
/*      */       } 
/*      */     } catch (NoSuchTemplateException nst) {
/*      */       logger.log(Level.WARNING, "No template for new deeds."); performer.getCommunicator().sendSafeServerMessage("An error occurred."); return;
/* 2241 */     } catch (FailedException fe) {
/*      */       
/* 2243 */       logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 2244 */       performer.getCommunicator().sendSafeServerMessage("An error occurred when creating the new deed.");
/*      */       
/*      */       return;
/* 2247 */     } catch (NoSuchTemplateException snt) {
/*      */       
/* 2249 */       logger.log(Level.WARNING, snt.getMessage(), (Throwable)snt);
/* 2250 */       performer.getCommunicator().sendSafeServerMessage("An error occurred when creating the new deed.");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeReputation(long wid) {
/* 2257 */     Reputation reput = this.reputations.remove(new Long(wid));
/* 2258 */     if (reput != null) {
/* 2259 */       reput.delete();
/*      */     }
/*      */   }
/*      */   
/*      */   final void addGates() {
/* 2264 */     Zone[] zones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, isOnSurface());
/* 2265 */     for (int x = 0; x < zones.length; x++)
/*      */     {
/* 2267 */       zones[x].addGates(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeGate(FenceGate gate) {
/* 2273 */     if (this.gates.contains(gate)) {
/*      */       
/* 2275 */       this.gates.remove(gate);
/* 2276 */       if (gate.getVillageId() == getId())
/*      */       {
/*      */         
/* 2279 */         gate.setIsManaged(false, null);
/*      */       }
/* 2281 */       gate.setVillage(null);
/*      */       
/*      */       try {
/* 2284 */         Item lock = gate.getLock();
/* 2285 */         lock.removeKey(this.deedid);
/*      */       }
/* 2287 */       catch (NoSuchLockException noSuchLockException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void addMineDoors() {
/* 2296 */     Zone[] zones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, isOnSurface());
/* 2297 */     for (int x = 0; x < zones.length; x++)
/*      */     {
/* 2299 */       zones[x].addMineDoors(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeMineDoor(MineDoorPermission mineDoor) {
/* 2305 */     if (this.mineDoors.contains(mineDoor)) {
/*      */       
/* 2307 */       this.mineDoors.remove(mineDoor);
/* 2308 */       if (mineDoor.getVillageId() == getId())
/*      */       {
/*      */         
/* 2311 */         mineDoor.setIsManaged(false, null);
/*      */       }
/* 2313 */       mineDoor.setVillage(null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getDeedId() {
/* 2319 */     return this.deedid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VillageRole[] getRoles() {
/* 2328 */     VillageRole[] toReturn = (VillageRole[])this.roles.values().toArray((Object[])new VillageRole[this.roles.size()]);
/* 2329 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getMotto() {
/* 2338 */     return this.motto;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getMotd() {
/* 2347 */     return this.motd;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final Message getDisbandMessage() {
/* 2352 */     String left = "Less than a months upkeep left.";
/* 2353 */     if (!isChained())
/* 2354 */       left = "Not connected to kingdom influence."; 
/* 2355 */     if (lessThanWeekLeft())
/* 2356 */       left = "Under a weeks upkeep left."; 
/* 2357 */     return new Message(null, (byte)3, "Village", "Village:" + left, 250, 150, 250);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final Message getMotdMessage() {
/* 2362 */     return new Message(null, (byte)3, "Village", "MOTD:" + this.motd, 250, 150, 250);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final Message getRepMessage(String toSend) {
/* 2367 */     return new Message(null, (byte)3, "Village", toSend);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isChained() {
/* 2377 */     if (!Features.Feature.TOWER_CHAINING.isEnabled()) {
/* 2378 */       return true;
/*      */     }
/*      */     
/*      */     try {
/* 2382 */       Item token = getToken();
/*      */       
/* 2384 */       return token.isChained();
/*      */     }
/* 2386 */     catch (NoSuchItemException e) {
/*      */       
/* 2388 */       logger.warning(String.format("Village Error: No token found for village %s.", new Object[] { getName() }));
/*      */ 
/*      */       
/* 2391 */       return true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDemocracy() {
/* 2400 */     return this.democracy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isOnSurface() {
/* 2405 */     return this.surfaced;
/*      */   }
/*      */ 
/*      */   
/*      */   final void createGuard(Creature creature, long expireDate) {
/* 2410 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 2413 */       role = getRoleForStatus((byte)4);
/*      */     }
/* 2415 */     catch (NoSuchRoleException nsr) {
/*      */       
/* 2417 */       role = createRoleGuard();
/*      */     } 
/*      */     
/*      */     try {
/* 2421 */       addCitizen(creature, role);
/*      */     }
/* 2423 */     catch (IOException iox) {
/*      */       
/* 2425 */       logger.log(Level.WARNING, "Failed to add guard as citizen for settlement " + getName() + " " + iox.getMessage(), iox);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2432 */     Guard guard = new DbGuard(this.id, creature, expireDate);
/* 2433 */     guard.save();
/* 2434 */     this.guards.put(new Long(creature.getWurmId()), guard);
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
/*      */   public final void deleteGuard(Creature creature, boolean deleteCreature) {
/* 2446 */     removeCitizen(creature);
/* 2447 */     Guard guard = this.guards.get(new Long(creature.getWurmId()));
/* 2448 */     this.guards.remove(new Long(creature.getWurmId()));
/*      */     
/* 2450 */     if (guard != null) {
/*      */       
/* 2452 */       guard.delete();
/* 2453 */       if (deleteCreature) {
/*      */         
/* 2455 */         if (this.plan != null) {
/* 2456 */           this.plan.destroyGuard(creature);
/*      */         }
/* 2458 */         guard.getCreature().destroy();
/*      */       } 
/*      */     } 
/* 2461 */     assignTargets();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Guard[] getGuards() {
/* 2470 */     return (Guard[])this.guards.values().toArray((Object[])new Guard[this.guards.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void createWagoner(Creature creature) {
/* 2475 */     VillageRole role = null;
/*      */     
/*      */     try {
/* 2478 */       role = getRoleForStatus((byte)6);
/*      */     }
/* 2480 */     catch (NoSuchRoleException nsr) {
/*      */       
/* 2482 */       role = createRoleWagoner();
/*      */     } 
/*      */     
/*      */     try {
/* 2486 */       addCitizen(creature, role);
/*      */     }
/* 2488 */     catch (IOException iox) {
/*      */       
/* 2490 */       logger.log(Level.WARNING, "Failed to add wagoner as citizen for settlement " + getName() + " " + iox.getMessage(), iox);
/*      */     } 
/*      */     
/* 2493 */     Wagoner wagoner = creature.getWagoner();
/* 2494 */     if (wagoner == null) {
/*      */       
/* 2496 */       logger.log(Level.WARNING, "Wagoner not found!");
/*      */     } else {
/*      */       
/* 2499 */       this.wagoners.put(new Long(creature.getWurmId()), wagoner);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void deleteWagoner(Creature creature) {
/* 2508 */     removeCitizen(creature);
/* 2509 */     this.wagoners.remove(new Long(creature.getWurmId()));
/* 2510 */     Wagoner wagoner = creature.getWagoner();
/* 2511 */     if (wagoner != null)
/*      */     {
/*      */       
/* 2514 */       wagoner.clrVillage();
/*      */     }
/* 2516 */     if (this.wagoners.isEmpty()) {
/*      */       
/*      */       try {
/*      */         
/* 2520 */         removeRole(getRoleForStatus((byte)6));
/*      */       }
/* 2522 */       catch (NoSuchRoleException nsrx) {
/*      */         
/* 2524 */         logger.log(Level.WARNING, "Cannot find role for wagoner so cannot remove it for settlement " + getName() + " " + nsrx.getMessage(), (Throwable)nsrx);
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
/*      */   public final Wagoner[] getWagoners() {
/* 2536 */     return (Wagoner[])this.wagoners.values().toArray((Object[])new Wagoner[this.wagoners.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final VillageRole getRole(int aId) throws NoSuchRoleException {
/* 2541 */     VillageRole toReturn = this.roles.get(Integer.valueOf(aId));
/* 2542 */     if (toReturn == null)
/* 2543 */       throw new NoSuchRoleException("No role with id " + aId); 
/* 2544 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final VillageRole getRoleForStatus(byte status) throws NoSuchRoleException {
/* 2549 */     for (VillageRole role : this.roles.values()) {
/*      */       
/* 2551 */       if (role.getStatus() == status)
/* 2552 */         return role; 
/*      */     } 
/* 2554 */     throw new NoSuchRoleException("No role with status " + status);
/*      */   }
/*      */ 
/*      */   
/*      */   public final VillageRole getRoleForVillage(int villageId) {
/* 2559 */     if (villageId > 0)
/*      */     {
/* 2561 */       for (VillageRole role : this.roles.values()) {
/*      */         
/* 2563 */         if (role.getVillageAppliedTo() == villageId)
/* 2564 */           return role; 
/*      */       } 
/*      */     }
/* 2567 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VillageRole getRoleForPlayer(long playerId) {
/* 2577 */     Citizen citiz = this.citizens.get(new Long(playerId));
/* 2578 */     if (citiz != null) {
/* 2579 */       return citiz.getRole();
/*      */     }
/* 2581 */     if (playerId > 0L)
/*      */     {
/* 2583 */       for (VillageRole role : this.roles.values()) {
/*      */         
/* 2585 */         if (role.getPlayerAppliedTo() == playerId)
/* 2586 */           return role; 
/*      */       } 
/*      */     }
/* 2589 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void addRole(VillageRole role) {
/* 2594 */     this.roles.put(Integer.valueOf(role.getId()), role);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeRole(VillageRole role) {
/* 2599 */     for (Citizen citiz : this.citizens.values()) {
/*      */       
/* 2601 */       if (citiz.getRole() == role) {
/*      */         
/*      */         try {
/*      */           
/* 2605 */           citiz.setRole(getRoleForStatus((byte)3));
/*      */         }
/* 2607 */         catch (IOException iox) {
/*      */           
/* 2609 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         }
/* 2611 */         catch (NoSuchRoleException nsr) {
/*      */           
/* 2613 */           logger.log(Level.WARNING, nsr.getMessage(), (Throwable)nsr);
/*      */         } 
/*      */       }
/*      */     } 
/* 2617 */     this.roles.remove(Integer.valueOf(role.getId()));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean covers(int x, int y) {
/* 2622 */     return (x >= this.startx && x <= this.endx && y >= this.starty && y <= this.endy);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean coversPlus(int x, int y, int extra) {
/* 2627 */     return (x >= this.startx - extra && x <= this.endx + extra && y >= this.starty - extra && y <= this.endy + extra);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean coversWithPerimeter(int x, int y) {
/* 2632 */     return (x >= this.startx - 5 - this.perimeterTiles && x <= this.endx + 5 + this.perimeterTiles && y >= this.starty - 5 - this.perimeterTiles && y <= this.endy + 5 + this.perimeterTiles);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean coversWithPerimeterAndBuffer(int x, int y, int bufferTiles) {
/* 2640 */     return (x >= this.startx - 5 - this.perimeterTiles - bufferTiles && x <= this.endx + 5 + this.perimeterTiles + bufferTiles && y >= this.starty - 5 - this.perimeterTiles - bufferTiles && y <= this.endy + 5 + this.perimeterTiles + bufferTiles);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void modifyReputations(int num, Creature perpetrator) {
/* 2648 */     if (!isEnemy(perpetrator.getCitizenVillage()))
/*      */     {
/* 2650 */       modifyReputation(perpetrator.getWurmId(), num, perpetrator.isGuest());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void modifyReputations(Action action, Creature perpetrator) {
/* 2657 */     if (!isEnemy(perpetrator.getCitizenVillage())) {
/*      */       
/* 2659 */       if (perpetrator.isFriendlyKingdom(this.kingdom))
/* 2660 */         perpetrator.setUnmotivatedAttacker(); 
/* 2661 */       if (action.isOffensive()) {
/* 2662 */         setReputation(perpetrator.getWurmId(), -100, perpetrator.isGuest(), false);
/* 2663 */       } else if (Actions.isActionDestroy(action.getNumber())) {
/* 2664 */         modifyReputation(perpetrator.getWurmId(), -10, perpetrator.isGuest());
/* 2665 */       } else if (action.getNumber() == 74 || action.getNumber() == 6 || action
/* 2666 */         .getNumber() == 100 || action.getNumber() == 101 || action
/* 2667 */         .getNumber() == 465) {
/* 2668 */         modifyReputation(perpetrator.getWurmId(), -20, perpetrator.isGuest());
/*      */       } else {
/* 2670 */         modifyReputation(perpetrator.getWurmId(), -5, perpetrator.isGuest());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean checkGuards(Action action, Creature perpetrator) {
/* 2676 */     float mod = 1.0F;
/* 2677 */     if (Servers.localServer.HOMESERVER)
/* 2678 */       mod = 1.5F; 
/* 2679 */     perpetrator.setSecondsToLogout(300);
/* 2680 */     float dist = Math.max(Math.abs(getCenterX() - action.getTileX()), Math.abs(getCenterY() - action.getTileY()));
/* 2681 */     Guard[] g = getGuards();
/* 2682 */     if (g.length == 0)
/* 2683 */       return false; 
/* 2684 */     boolean noticed = false;
/* 2685 */     boolean dryrun = false;
/* 2686 */     Reputation rep = getReputationObject(perpetrator.getWurmId());
/* 2687 */     if (rep != null && rep.getValue() >= 0 && rep.isPermanent())
/* 2688 */       dryrun = true; 
/* 2689 */     if (dist <= 5.0F)
/*      */     {
/* 2691 */       if (action.getNumber() == 100 || action.isEquipAction() || action
/* 2692 */         .getNumber() == 101) {
/*      */         
/* 2694 */         if (perpetrator.getStealSkill().getKnowledge(0.0D) < 50.0D) {
/*      */           
/* 2696 */           perpetrator.getStealSkill().skillCheck(50.0D, 0.0D, dryrun, 10.0F);
/* 2697 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 2701 */         float diff = 75.0F - dist;
/* 2702 */         if (Servers.localServer.HOMESERVER)
/* 2703 */           diff = 80.0F - dist; 
/* 2704 */         noticed = (perpetrator.getStealSkill().skillCheck(diff, 0.0D, dryrun, 10.0F) < 0.0D);
/*      */       }
/*      */       else {
/*      */         
/* 2708 */         return true;
/*      */       }  } 
/* 2710 */     if (!noticed) {
/*      */       
/* 2712 */       float factor = dist * dist / 5.0F;
/* 2713 */       float guardfactor = this.guards.size() / factor;
/* 2714 */       if (Server.rand.nextFloat() < guardfactor) {
/*      */         
/* 2716 */         if (action.getNumber() == 100 || action.isEquipAction() || action.getNumber() == 101)
/* 2717 */           perpetrator.getStealSkill().skillCheck(20.0D, 0.0D, dryrun, 10.0F); 
/* 2718 */         return true;
/*      */       } 
/*      */       
/* 2721 */       for (int x = 0; x < g.length; x++) {
/*      */         
/* 2723 */         int tx = (g[x]).creature.getTileX();
/* 2724 */         int ty = (g[x]).creature.getTileY();
/*      */ 
/*      */         
/* 2727 */         int d = Math.max(Math.abs(tx - action.getTileX()), Math.abs(ty - action.getTileY()));
/* 2728 */         if (d <= 5)
/*      */         {
/* 2730 */           if (Server.rand.nextFloat() * mod < (g[x]).creature.getNoticeChance())
/*      */           {
/* 2732 */             if (action.getNumber() == 100 || action.isEquipAction() || action
/* 2733 */               .getNumber() == 101) {
/*      */               
/* 2735 */               if (perpetrator.getStealSkill().skillCheck(((g[x]).creature.getNoticeChance() * 100.0F), 0.0D, dryrun, x) < 0.0D) {
/* 2736 */                 return true;
/*      */               }
/*      */             } else {
/* 2739 */               return true;
/*      */             }  } 
/*      */         }
/*      */       } 
/* 2743 */       return false;
/*      */     } 
/* 2745 */     return noticed;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean checkGuards(int tilex, int tiley, Creature perpetrator) {
/* 2750 */     return checkGuards(tilex, tiley, perpetrator, 5.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean checkGuards(int tilex, int tiley, Creature perpetrator, float maxdist) {
/* 2755 */     perpetrator.setSecondsToLogout(300);
/* 2756 */     float dist = Math.max(Math.abs(getCenterX() - tilex), Math.abs(getCenterY() - tiley));
/* 2757 */     if (dist <= 5.0F) {
/* 2758 */       return true;
/*      */     }
/*      */     
/* 2761 */     float factor = dist * dist / 5.0F;
/* 2762 */     float guardfactor = this.guards.size() / factor;
/* 2763 */     if (Server.rand.nextFloat() < guardfactor) {
/* 2764 */       return true;
/*      */     }
/* 2766 */     Guard[] g = getGuards();
/* 2767 */     for (int x = 0; x < g.length; x++) {
/*      */       
/* 2769 */       int tx = (g[x]).creature.getTileX();
/* 2770 */       int ty = (g[x]).creature.getTileY();
/* 2771 */       int d = Math.max(Math.abs(tx - tilex), Math.abs(ty - tiley));
/* 2772 */       if (d <= maxdist)
/*      */       {
/* 2774 */         if (Server.rand.nextFloat() < (g[x]).creature.getNoticeChance())
/* 2775 */           return true; 
/*      */       }
/*      */     } 
/* 2778 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxGuardsAttacking() {
/* 2784 */     return Math.max(maxGuardsOnThisServer, this.guards.size() / 20);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void cryForHelp(Creature needhelp, boolean cry) {
/* 2789 */     int guardsAssigned = 0;
/* 2790 */     Guard best = null;
/* 2791 */     int bestdist = Integer.MAX_VALUE;
/* 2792 */     if (this.guards.size() > 1) {
/*      */       
/* 2794 */       Creature target = needhelp.getTarget();
/* 2795 */       if (target != null) {
/*      */         
/* 2797 */         for (Guard guard : this.guards.values()) {
/*      */           
/* 2799 */           if (guard.creature.target == target.getWurmId()) {
/*      */             
/* 2801 */             guardsAssigned++;
/* 2802 */             if (guardsAssigned >= getMaxGuardsAttacking()) {
/*      */               break;
/*      */             }
/*      */             continue;
/*      */           } 
/* 2807 */           if (guard.creature.target == -10L) {
/*      */             
/* 2809 */             int diffx = (int)Math.abs(guard.creature.getPosX() - target.getPosX());
/* 2810 */             int diffy = (int)Math.abs(guard.creature.getPosY() - target.getPosY());
/* 2811 */             int dist = Math.max(diffx, diffy);
/* 2812 */             if (dist < bestdist) {
/*      */               
/* 2814 */               best = guard;
/* 2815 */               bestdist = dist;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2821 */         if (guardsAssigned < getMaxGuardsAttacking() && best != null) {
/*      */           
/* 2823 */           if (cry)
/* 2824 */             best.creature.say("I'll help you with " + target.getName() + "!"); 
/* 2825 */           boolean attackTarget = true;
/* 2826 */           if (target.getVehicle() != -10L)
/*      */           {
/* 2828 */             if (Server.rand.nextInt(3) == 0) {
/*      */               
/* 2830 */               Vehicle vehic = Vehicles.getVehicleForId(target.getVehicle());
/* 2831 */               if (vehic != null && 
/* 2832 */                 vehic.creature) {
/*      */                 
/* 2834 */                 best.creature.setTarget(target.getVehicle(), false);
/* 2835 */                 attackTarget = false;
/*      */               } 
/*      */             } 
/*      */           }
/* 2839 */           if (attackTarget) {
/* 2840 */             best.creature.setTarget(target.getWurmId(), false);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void resolveDispute(Creature performer, Creature defender) {
/* 2848 */     if (this.guards.size() > 0) {
/*      */       
/* 2850 */       if (mayAttack(performer, defender))
/*      */       {
/* 2852 */         if (!mayAttack(defender, performer))
/* 2853 */           setReputation(defender.getWurmId(), -100, defender.isGuest(), false); 
/*      */       }
/* 2855 */       if (mayAttack(defender, performer))
/*      */       {
/* 2857 */         if (!mayAttack(performer, defender)) {
/* 2858 */           setReputation(performer.getWurmId(), -100, performer.isGuest(), false);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean isActionAllowed(short action, Creature creature) {
/* 2865 */     return isActionAllowed(action, creature, false, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isActionAllowed(short action, Creature creature, boolean setHunted, int encodedTile, int dir) {
/* 2871 */     boolean ok = isActionAllowed(setHunted, action, creature, encodedTile, dir);
/* 2872 */     if (!ok && Servers.isThisAPvpServer()) {
/*      */       
/* 2874 */       if (creature.isFriendlyKingdom(this.kingdom) && setHunted)
/*      */       {
/* 2876 */         if (creature.getCitizenVillage() == null || !creature.getCitizenVillage().isEnemy(this))
/* 2877 */           creature.setUnmotivatedAttacker(); 
/*      */       }
/* 2879 */       if (isEnemy(creature)) {
/*      */         
/* 2881 */         if (Actions.actionEntrys[action].isEnemyAllowedWhenNoGuards() && this.guards.size() == 0)
/* 2882 */           return true; 
/* 2883 */         if (Actions.actionEntrys[action].isEnemyNeverAllowed())
/* 2884 */           return false; 
/* 2885 */         if (Actions.actionEntrys[action].isEnemyAlwaysAllowed()) {
/* 2886 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 2890 */     return ok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isActionAllowed(boolean ignoreGuardCount, short action, Creature creature, int encodedTile, int dir) {
/* 2901 */     if (creature.getPower() >= 2)
/* 2902 */       return true; 
/* 2903 */     if (System.currentTimeMillis() - this.creationDate < 120000L)
/* 2904 */       return true; 
/* 2905 */     VillageRole role = getRoleFor(creature);
/* 2906 */     if (role == null)
/* 2907 */       return false; 
/* 2908 */     if (action == 100 || action == 350 || action == 537) {
/* 2909 */       return false;
/*      */     }
/* 2911 */     boolean onSurface = (creature.getLayer() >= 0);
/* 2912 */     byte tileType = Tiles.decodeType(encodedTile);
/* 2913 */     Tiles.Tile t = Tiles.getTile(tileType);
/*      */     
/* 2915 */     if (t == null) {
/*      */       
/* 2917 */       logger.log(Level.SEVERE, "Unknown tile type " + tileType + " for " + creature.getName() + " at " + creature.getTilePos());
/* 2918 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2923 */     if (Actions.isActionBrand(action)) {
/* 2924 */       return (role.mayBrand() || this.everybody.mayBrand());
/*      */     }
/* 2926 */     if (Actions.isActionBreed(action)) {
/* 2927 */       return (role.mayBreed() || this.everybody.mayBreed());
/*      */     }
/* 2929 */     if (Actions.isActionButcher(action)) {
/* 2930 */       return (role.mayButcher() || this.everybody.mayButcher());
/*      */     }
/* 2932 */     if (Actions.isActionGroom(action)) {
/* 2933 */       return (role.mayGroom() || this.everybody.mayGroom());
/*      */     }
/* 2935 */     if (Actions.isActionLead(action)) {
/* 2936 */       return (role.mayLead() || this.everybody.mayLead());
/*      */     }
/* 2938 */     if (Actions.isActionMilkOrShear(action)) {
/* 2939 */       return (role.mayMilkAndShear() || this.everybody.mayMilkAndShear());
/*      */     }
/* 2941 */     if (Actions.isActionSacrifice(action)) {
/* 2942 */       return (role.maySacrifice() || this.everybody.maySacrifice());
/*      */     }
/* 2944 */     if (Actions.isActionTame(action)) {
/* 2945 */       return (role.mayTame() || this.everybody.mayTame());
/*      */     }
/*      */ 
/*      */     
/* 2949 */     if (Actions.isActionBuild(action) || Actions.isActionChangeBuilding(action)) {
/* 2950 */       return (role.mayBuild() || this.everybody.mayBuild());
/*      */     }
/* 2952 */     if (Actions.isActionDestroyFence(action)) {
/* 2953 */       return (role.mayDestroyFences() || this.everybody.mayDestroyFences());
/*      */     }
/* 2955 */     if (Actions.isActionDestroyItem(action)) {
/* 2956 */       return (role.mayDestroyItems() || this.everybody.mayDestroyItems());
/*      */     }
/* 2958 */     if (Actions.isActionLockPick(action)) {
/* 2959 */       return role.mayPickLocks();
/*      */     }
/* 2961 */     if (Actions.isActionPlanBuilding(action)) {
/* 2962 */       return (role.mayPlanBuildings() || this.everybody.mayPlanBuildings());
/*      */     }
/*      */ 
/*      */     
/* 2966 */     if (Actions.isActionCultivate(action)) {
/* 2967 */       return (role.mayCultivate() || this.everybody.mayCultivate());
/*      */     }
/* 2969 */     if (Actions.isActionDig(action) && (tileType == Tiles.Tile.TILE_CLAY.id || tileType == Tiles.Tile.TILE_MOSS.id || tileType == Tiles.Tile.TILE_PEAT.id || tileType == Tiles.Tile.TILE_TAR.id))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2974 */       return (role.mayDigResources() || this.everybody.mayDigResources());
/*      */     }
/* 2976 */     if (Actions.isActionPack(action)) {
/* 2977 */       return (role.mayPack() || this.everybody.mayPack());
/*      */     }
/* 2979 */     if (Actions.isActionTerraform(action, onSurface)) {
/* 2980 */       return (role.mayTerraform() || this.everybody.mayTerraform());
/*      */     }
/*      */ 
/*      */     
/* 2984 */     if (Actions.isActionHarvest(action) && (tileType == Tiles.Tile.TILE_FIELD.id || tileType == Tiles.Tile.TILE_FIELD2.id)) {
/* 2985 */       return (role.mayHarvestFields() || this.everybody.mayHarvestFields());
/*      */     }
/* 2987 */     if (Actions.isActionSow(action)) {
/* 2988 */       return (role.maySowFields() || this.everybody.maySowFields());
/*      */     }
/* 2990 */     if (Actions.isActionFarm(action)) {
/* 2991 */       return (role.mayTendFields() || this.everybody.mayTendFields());
/*      */     }
/*      */     
/* 2994 */     if (encodedTile == 0 && Actions.isActionChop(action))
/*      */     {
/*      */       
/* 2997 */       return (role.mayDestroyFences() || this.everybody.mayDestroyFences());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3002 */     Tiles.Tile theTile = Tiles.getTile(tileType);
/* 3003 */     if (Actions.isActionChop(action) && (theTile.isTree() || theTile.isBush())) {
/*      */ 
/*      */       
/* 3006 */       byte tileData = Tiles.decodeData(encodedTile);
/* 3007 */       int treeAge = tileData >> 4 & 0xF;
/*      */       
/* 3009 */       if (treeAge > 11 && treeAge < 15 && theTile.isTree()) {
/* 3010 */         return (role.mayChopDownOldTrees() || this.everybody.mayChopDownOldTrees());
/*      */       }
/* 3012 */       return (role.mayChopDownAllTrees() || this.everybody.mayChopDownAllTrees());
/*      */     } 
/*      */ 
/*      */     
/* 3016 */     if (Actions.isActionGather(action)) {
/* 3017 */       return (role.mayCutGrass() || this.everybody.mayCutGrass());
/*      */     }
/* 3019 */     if (Actions.isActionPick(action) || (Actions.isActionHarvest(action) && (t.isTree() || t.isBush() || encodedTile == 0))) {
/* 3020 */       return (role.mayHarvestFruit() || this.everybody.mayHarvestFruit());
/*      */     }
/* 3022 */     if (Actions.isActionTrim(action)) {
/* 3023 */       return (role.mayMakeLawn() || this.everybody.mayMakeLawn());
/*      */     }
/* 3025 */     if (Actions.isActionPickSprout(action)) {
/* 3026 */       return (role.mayPickSprouts() || this.everybody.mayPickSprouts());
/*      */     }
/* 3028 */     if (Actions.isActionPlant(action)) {
/* 3029 */       return (role.mayPlantFlowers() || this.everybody.mayPlantFlowers());
/*      */     }
/* 3031 */     if (Actions.isActionPlantCenter(action)) {
/* 3032 */       return (role.mayPlantSprouts() || this.everybody.mayPlantSprouts());
/*      */     }
/* 3034 */     if (Actions.isActionPrune(action)) {
/* 3035 */       return (role.mayPrune() || this.everybody.mayPrune());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3041 */     if (Actions.isActionDietySpell(action)) {
/* 3042 */       return (role.mayCastDeitySpells() || this.everybody.mayCastDeitySpells());
/*      */     }
/* 3044 */     if (Actions.isActionSorcerySpell(action)) {
/* 3045 */       return (role.mayCastSorcerySpells() || this.everybody.mayCastSorcerySpells());
/*      */     }
/* 3047 */     if (Actions.isActionForageBotanizeInvestigate(action)) {
/* 3048 */       return (role.mayForageAndBotanize() || this.everybody.mayForageAndBotanize());
/*      */     }
/* 3050 */     if (Actions.isActionPlaceNPCs(action)) {
/* 3051 */       return (role.mayPlaceMerchants() || this.everybody.mayPlaceMerchants());
/*      */     }
/* 3053 */     if (Actions.isActionPave(action)) {
/* 3054 */       return (role.mayPave() || this.everybody.mayPave());
/*      */     }
/* 3056 */     if (Actions.isActionMeditate(action)) {
/* 3057 */       return (role.mayUseMeditationAbilities() || this.everybody.mayUseMeditationAbilities());
/*      */     }
/*      */     
/* 3060 */     if (Actions.isActionAttachLock(action))
/* 3061 */       return (role.mayAttachLock() || this.everybody.mayAttachLock()); 
/* 3062 */     if (Actions.isActionDrop(action))
/* 3063 */       return (role.mayDrop() || this.everybody.mayDrop()); 
/* 3064 */     if (Actions.isActionImproveOrRepair(action))
/* 3065 */       return (role.mayImproveAndRepair() || this.everybody.mayImproveAndRepair()); 
/* 3066 */     if (Actions.isActionLoad(action))
/* 3067 */       return (role.mayLoad() || this.everybody.mayLoad()); 
/* 3068 */     if (Actions.isActionTake(action))
/* 3069 */       return (role.mayPickup() || this.everybody.mayPickup()); 
/* 3070 */     if (Actions.isActionPickupPlanted(action))
/* 3071 */       return (role.mayPickupPlanted() || this.everybody.mayPickupPlanted()); 
/* 3072 */     if (Actions.isActionPlantItem(action))
/* 3073 */       return (role.mayPlantItem() || this.everybody.mayPlantItem()); 
/* 3074 */     if (Actions.isActionPullPushTurn(action))
/* 3075 */       return (role.mayPushPullTurn() || this.everybody.mayPushPullTurn()); 
/* 3076 */     if (Actions.isActionUnload(action)) {
/* 3077 */       return (role.mayUnload() || this.everybody.mayUnload());
/*      */     }
/*      */ 
/*      */     
/* 3081 */     if (!onSurface && (dir == 0 || dir == 1) && 
/* 3082 */       Actions.isActionMineFloor(action))
/* 3083 */       return (role.mayMineFloor() || this.everybody.mayMineFloor()); 
/* 3084 */     boolean isCaveWall = (!onSurface && dir != 0 && dir != 1);
/*      */     
/* 3086 */     if (isCaveWall && tileType == Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id && Actions.isActionMine(action)) {
/* 3087 */       return (role.mayMineIronVeins() || this.everybody.mayMineIronVeins());
/*      */     }
/* 3089 */     if (isCaveWall && tileType != Tiles.Tile.TILE_CAVE_WALL_ORE_IRON.id && t
/* 3090 */       .isOreCave() && Actions.isActionMine(action)) {
/* 3091 */       return (role.mayMineOtherVeins() || this.everybody.mayMineOtherVeins());
/*      */     }
/* 3093 */     if (isCaveWall && Actions.isActionMine(action) && (tileType == Tiles.Tile.TILE_CAVE_WALL.id || 
/* 3094 */       Tiles.isReinforcedCave(tileType))) {
/* 3095 */       return (role.mayMineRock() || this.everybody.mayMineRock());
/*      */     }
/* 3097 */     if (onSurface && Actions.isActionMineSurface(action)) {
/* 3098 */       return (role.mayMineSurface() || this.everybody.mayMineSurface());
/*      */     }
/* 3100 */     if (Actions.isActionTunnel(action)) {
/* 3101 */       return (role.mayTunnel() || this.everybody.mayTunnel());
/*      */     }
/* 3103 */     if (!onSurface && Actions.isActionReinforce(action)) {
/* 3104 */       return (role.mayReinforce() || this.everybody.mayReinforce());
/*      */     }
/*      */ 
/*      */     
/* 3108 */     if (Actions.isActionDestroy(action)) {
/* 3109 */       return role.mayDestroyAnyBuilding();
/*      */     }
/*      */     
/* 3112 */     if (action == 73) {
/* 3113 */       return (role.mayInviteCitizens() || creature.getCitizenVillage() != this);
/*      */     }
/*      */ 
/*      */     
/* 3117 */     if (Actions.isActionManage(action)) {
/* 3118 */       return role.mayManageAllowedObjects();
/*      */     }
/* 3120 */     if (action == 66) {
/* 3121 */       return role.mayManageCitizenRoles();
/*      */     }
/* 3123 */     if (action == 67) {
/* 3124 */       return role.mayManageGuards();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3129 */     if (action == 69) {
/* 3130 */       return role.mayManageReputations();
/*      */     }
/* 3132 */     if (action == 540) {
/* 3133 */       return role.mayManageRoles();
/*      */     }
/* 3135 */     if (action == 68) {
/* 3136 */       return role.mayManageSettings();
/*      */     }
/* 3138 */     if (action == 481) {
/* 3139 */       return role.mayConfigureTwitter();
/*      */     }
/* 3141 */     if (action == 348) {
/* 3142 */       return role.mayDisbandSettlement();
/*      */     }
/* 3144 */     if (action == 76)
/* 3145 */       return role.mayResizeSettlement(); 
/* 3146 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void updateGatesForRole(VillageRole role) {
/* 3151 */     if (this.citizens != null)
/*      */     {
/* 3153 */       if (this.gates != null)
/*      */       {
/* 3155 */         for (FenceGate gate : this.gates) {
/*      */           
/* 3157 */           for (Citizen citiz : this.citizens.values()) {
/*      */             
/* 3159 */             if (citiz.getRole() == role) {
/*      */               
/*      */               try {
/*      */                 
/* 3163 */                 Creature creat = Server.getInstance().getCreature(citiz.getId());
/* 3164 */                 if (gate.containsCreature(creat)) {
/* 3165 */                   creat.updateGates();
/*      */                 }
/* 3167 */               } catch (NoSuchCreatureException nsc) {
/*      */                 
/* 3169 */                 logger.log(Level.WARNING, citiz.getName() + " - creature not found:", (Throwable)nsc);
/*      */               }
/* 3171 */               catch (NoSuchPlayerException noSuchPlayerException) {}
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
/*      */   public final boolean mayAttack(Creature attacker, Creature defender) {
/* 3184 */     if (Servers.localServer.PVPSERVER) {
/*      */       
/* 3186 */       if (attacker.isFriendlyKingdom(this.kingdom) && !defender.isFriendlyKingdom(this.kingdom))
/* 3187 */         return true; 
/* 3188 */       if (!attacker.isFriendlyKingdom(this.kingdom))
/* 3189 */         return true; 
/* 3190 */       if (attacker.isEnemyOnChaos(defender))
/* 3191 */         return true; 
/*      */     } 
/* 3193 */     if (this.guards.size() >= 1 || ((
/* 3194 */       !attacker.isOnPvPServer() || !defender.isOnPvPServer()) && !isEnemy(defender))) {
/*      */       
/* 3196 */       VillageRole attackerRole = getRoleFor(attacker);
/* 3197 */       if (attackerRole == null)
/* 3198 */         return false; 
/* 3199 */       Citizen def = this.citizens.get(new Long(defender.getWurmId()));
/*      */       
/* 3201 */       if (!Servers.isThisAPvpServer() && def == null && defender.isBrandedBy(getId()))
/* 3202 */         return (attackerRole.mayAttackCitizens() || this.everybody.mayAttackCitizens()); 
/* 3203 */       if (def != null) {
/* 3204 */         return (attackerRole.mayAttackCitizens() || this.everybody.mayAttackCitizens());
/*      */       }
/*      */       
/* 3207 */       if (isAlly(defender)) {
/* 3208 */         return (attackerRole.mayAttackCitizens() || this.everybody.mayAttackCitizens());
/*      */       }
/*      */       
/* 3211 */       if (!defender.isAtWarWith(attacker))
/*      */       {
/* 3213 */         return (attackerRole.mayAttackNonCitizens() || this.everybody.mayAttackNonCitizens());
/*      */       }
/*      */ 
/*      */       
/* 3217 */       if (Kingdoms.getKingdomTemplateFor(this.kingdom) != 3)
/*      */       {
/* 3219 */         if (attacker.getReputation() < 0 && defender.getReputation() >= 0)
/* 3220 */           return false; 
/*      */       }
/*      */     } 
/* 3223 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayDoDiplomacy(Creature creature) {
/* 3228 */     Citizen citiz = this.citizens.get(new Long(creature.getWurmId()));
/* 3229 */     VillageRole role = null;
/* 3230 */     if (citiz != null) {
/* 3231 */       role = citiz.getRole();
/*      */     } else {
/* 3233 */       return false;
/* 3234 */     }  return role.isDiplomat();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getId() {
/* 3243 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/* 3252 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getFounderName() {
/* 3261 */     return this.founderName;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addCitizen(Creature creature, VillageRole role) throws IOException {
/* 3266 */     long wurmid = creature.getWurmId();
/* 3267 */     boolean first = true;
/* 3268 */     if (creature.getCitizenVillage() != null) {
/*      */       
/* 3270 */       creature.getCitizenVillage().removeCitizen(creature);
/* 3271 */       first = false;
/*      */     } 
/*      */     
/* 3274 */     if (this.citizens.keySet().contains(new Long(wurmid)))
/*      */     {
/* 3276 */       return false;
/*      */     }
/* 3278 */     Citizen citizen = null;
/* 3279 */     citizen = new DbCitizen(wurmid, creature.getName(), role, -10L, -10L);
/* 3280 */     citizen.create(creature, this.id);
/* 3281 */     boolean ok = false;
/* 3282 */     if (citizen != null) {
/*      */       
/* 3284 */       broadCastSafe(creature.getName() + " is now a citizen of " + this.name + "!", (byte)2);
/* 3285 */       this.citizens.put(new Long(citizen.getId()), citizen);
/* 3286 */       creature.getCommunicator().sendSafeServerMessage("Congratulations! You are now the proud citizen of " + this.name + ".", (byte)2);
/* 3287 */       this.group.addMember(creature.getName(), creature);
/* 3288 */       MapAnnotation[] annotations = getVillageMapAnnotationsArray();
/* 3289 */       if (annotations != null && creature.isPlayer())
/* 3290 */         creature.getCommunicator().sendMapAnnotations(annotations); 
/* 3291 */       creature.setCitizenVillage(this);
/*      */       
/* 3293 */       if (creature.isPlayer()) {
/* 3294 */         sendCitizensToPlayer((Player)creature);
/*      */       }
/* 3296 */       if (getAllianceNumber() > 0) {
/*      */         
/* 3298 */         PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(getAllianceNumber());
/* 3299 */         if (pvpAll != null && pvpAll.getMotd().length() > 0) {
/*      */           
/* 3301 */           Message mess = pvpAll.getMotdMessage();
/* 3302 */           creature.getCommunicator().sendMessage(mess);
/*      */         }
/*      */         else {
/*      */           
/* 3306 */           Message mess = new Message(creature, (byte)15, "Alliance", "");
/* 3307 */           creature.getCommunicator().sendMessage(mess);
/*      */         } 
/* 3309 */         if (pvpAll != null)
/*      */         {
/* 3311 */           creature.getCommunicator().sendMapAnnotations(pvpAll.getAllianceMapAnnotationsArray());
/*      */         }
/*      */       } 
/* 3314 */       setReputation(creature.getWurmId(), 0, false, true);
/* 3315 */       ok = true;
/*      */     } 
/*      */     
/* 3318 */     if (ok && creature.isPlayer()) {
/*      */       
/* 3320 */       if (first)
/* 3321 */         creature.achievement(171); 
/* 3322 */       addHistory(creature.getName(), "becomes a citizen");
/* 3323 */       Citizen[] lCitizens = getCitizens();
/* 3324 */       int plays = 0;
/* 3325 */       for (int x = 0; x < lCitizens.length; x++) {
/*      */         
/* 3327 */         if (lCitizens[x].isPlayer()) {
/*      */           
/*      */           try {
/*      */             
/* 3331 */             Player p = Players.getInstance().getPlayer(lCitizens[x].getId());
/* 3332 */             if (lCitizens[x].getId() != wurmid) {
/*      */               
/* 3334 */               p.getCommunicator().sendAddVillager(creature.getName(), lCitizens[x].getId());
/* 3335 */               plays++;
/*      */             } else {
/*      */               
/* 3338 */               p.setLastChangedVillage(System.currentTimeMillis());
/*      */             } 
/* 3340 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3346 */       Players.getInstance().sendAddToAlliance(creature, this);
/* 3347 */       if (plays > this.maxCitizens) {
/*      */         
/* 3349 */         if (this.maxCitizens < 1000 && plays >= 1000) {
/*      */           
/* 3351 */           addHistory(creature.getName(), "breaks the thousand citizen count");
/* 3352 */           HistoryManager.addHistory(creature.getName(), "breaks the thousand citizen count of " + getName());
/*      */         } 
/* 3354 */         if (this.maxCitizens < 200 && plays >= 200) {
/*      */           
/* 3356 */           addHistory(creature.getName(), "breaks the twohundred citizen count");
/* 3357 */           HistoryManager.addHistory(creature.getName(), "breaks the twohundred citizen count of " + getName());
/*      */         }
/* 3359 */         else if (this.maxCitizens < 100 && plays >= 100) {
/*      */           
/* 3361 */           addHistory(creature.getName(), "breaks the hundred citizen count");
/* 3362 */           HistoryManager.addHistory(creature.getName(), "breaks the hundred citizen count of " + getName());
/*      */         }
/* 3364 */         else if (this.maxCitizens < 50 && plays >= 50) {
/*      */           
/* 3366 */           addHistory(creature.getName(), "breaks the fifty citizen count");
/* 3367 */           HistoryManager.addHistory(creature.getName(), "breaks the fifty citizen count of " + getName());
/*      */         }
/* 3369 */         else if (this.maxCitizens < 20 && plays >= 20) {
/*      */           
/* 3371 */           addHistory(creature.getName(), "breaks the twenty citizen count");
/* 3372 */           HistoryManager.addHistory(creature.getName(), "breaks the twenty citizen count of " + getName());
/*      */         }
/* 3374 */         else if (this.maxCitizens < 5 && plays >= 5) {
/*      */           
/* 3376 */           addHistory(creature.getName(), "breaks the five citizen count");
/*      */         } 
/* 3378 */         setMaxcitizens(plays);
/*      */       } 
/*      */     } 
/* 3381 */     return ok;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void removeCitizen(Creature creature) {
/* 3386 */     if (creature.isPlayer())
/* 3387 */       Players.getInstance().sendRemoveFromAlliance(creature, this); 
/* 3388 */     this.citizens.remove(new Long(creature.getWurmId()));
/* 3389 */     this.group.dropMember(creature.getName());
/* 3390 */     creature.setCitizenVillage(null);
/* 3391 */     if (creature.isPlayer() || creature.isWagoner())
/* 3392 */       broadCastSafe(creature.getName() + " is no longer a citizen of " + this.name + "."); 
/* 3393 */     creature.getCommunicator().sendSafeServerMessage("You are no longer citizen of " + this.name + ".", (byte)2);
/* 3394 */     if (creature.isPlayer() && creature instanceof Player) {
/*      */       
/* 3396 */       ((Player)creature).sendClearVillageMapAnnotations();
/* 3397 */       ((Player)creature).sendClearAllianceMapAnnotations();
/*      */     } 
/*      */     
/*      */     try {
/* 3401 */       Citizen.delete(creature.getWurmId());
/*      */     }
/* 3403 */     catch (IOException iox) {
/*      */       
/* 3405 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 3407 */     if (WurmId.getType(creature.getWurmId()) == 0) {
/*      */       
/* 3409 */       addHistory(creature.getName(), "is no longer a citizen");
/* 3410 */       Citizen[] lCitizens = getCitizens();
/* 3411 */       for (int x = 0; x < lCitizens.length; x++) {
/*      */         
/* 3413 */         if (WurmId.getType(lCitizens[x].getId()) == 0) {
/*      */           
/*      */           try {
/*      */             
/* 3417 */             Player p = Players.getInstance().getPlayer(lCitizens[x].getId());
/* 3418 */             p.getCommunicator().sendRemoveVillager(creature.getName());
/*      */           }
/* 3420 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3426 */       VillageMessages.delete(getId(), creature.getWurmId());
/*      */     } 
/* 3428 */     if (creature.isWagoner())
/*      */     {
/* 3430 */       addHistory(creature.getName(), "is no longer a citizen");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void sendCitizensToPlayer(Player player) {
/* 3439 */     if (this.motd != null && this.motd.length() > 0) {
/* 3440 */       player.getCommunicator().sendMessage(getMotdMessage());
/*      */     }
/* 3442 */     if (!moreThanMonthLeft()) {
/* 3443 */       player.getCommunicator().sendMessage(getDisbandMessage());
/*      */     }
/* 3445 */     Citizen[] lCitizens = getCitizens();
/* 3446 */     for (int x = 0; x < lCitizens.length; x++) {
/*      */       
/* 3448 */       if (WurmId.getType(lCitizens[x].getId()) == 0 && lCitizens[x].getId() != player.getWurmId()) {
/*      */         
/*      */         try {
/*      */           
/* 3452 */           Player p = Players.getInstance().getPlayer(lCitizens[x].getId());
/* 3453 */           player.getCommunicator().sendAddVillager(p.getName(), lCitizens[x].getId());
/*      */         }
/* 3455 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeCitizen(Citizen citizen) {
/* 3465 */     Creature creature = null;
/*      */     
/*      */     try {
/* 3468 */       creature = Server.getInstance().getCreature(citizen.getId());
/*      */     }
/* 3470 */     catch (NoSuchCreatureException nsc) {
/*      */       
/* 3472 */       logger.log(Level.WARNING, "No creature exists with wurmid " + citizen.getId() + " any longer?", (Throwable)nsc);
/*      */     }
/* 3474 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */     
/* 3477 */     if (creature != null) {
/*      */       
/* 3479 */       removeCitizen(creature);
/* 3480 */       if (citizen.getRole().getStatus() == 4)
/*      */       {
/* 3482 */         deleteGuard(creature, false);
/*      */       }
/* 3484 */       if (citizen.getRole().getStatus() == 6)
/*      */       {
/* 3486 */         deleteWagoner(creature);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 3491 */       this.citizens.remove(new Long(citizen.getId()));
/* 3492 */       broadCastSafe(citizen.getName() + " is no longer a citizen of " + this.name + ".");
/* 3493 */       addHistory(citizen.getName(), "is no longer a citizen");
/*      */       
/*      */       try {
/* 3496 */         Citizen.delete(citizen.getId());
/*      */       }
/* 3498 */       catch (IOException iox) {
/*      */         
/* 3500 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastSafe(String message) {
/* 3507 */     broadCastSafe(message, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastSafe(String message, byte messageType) {
/* 3512 */     this.group.broadCastSafe(message, messageType);
/* 3513 */     twit(message);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastAlert(String message) {
/* 3518 */     broadCastAlert(message, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastAlert(String message, byte messageType) {
/* 3523 */     this.group.broadCastAlert(message, messageType);
/* 3524 */     twit(message);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastNormal(String message) {
/* 3529 */     this.group.broadCastNormal(message);
/* 3530 */     twit(message);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastMessage(Message message) {
/* 3535 */     broadCastMessage(message, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void broadCastMessage(Message message, boolean twit) {
/* 3540 */     this.group.sendMessage(message);
/* 3541 */     if (twit) {
/* 3542 */       twit(message.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */   public final VillageRole getRoleFor(Creature creature) {
/* 3547 */     if (this.everybody == null)
/* 3548 */       this.everybody = createRoleEverybody(); 
/* 3549 */     VillageRole role = getRoleForPlayer(creature.getWurmId());
/* 3550 */     if (role == null) {
/*      */ 
/*      */       
/* 3553 */       if (creature.getCitizenVillage() != null)
/* 3554 */         role = getRoleForVillage(creature.getCitizenVillage().getId()); 
/* 3555 */       if (role == null) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/* 3560 */           if (isAlly(creature)) {
/* 3561 */             role = getRoleForStatus((byte)5);
/*      */           } else {
/* 3563 */             role = getRoleForStatus((byte)1);
/*      */           } 
/* 3565 */         } catch (NoSuchRoleException nsr) {
/*      */           
/* 3567 */           logger.log(Level.WARNING, nsr.getMessage(), (Throwable)nsr);
/*      */         } 
/*      */       }
/*      */     } 
/* 3571 */     return role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VillageRole getRoleFor(long creatureId) {
/* 3582 */     if (this.everybody == null)
/* 3583 */       this.everybody = createRoleEverybody(); 
/* 3584 */     VillageRole role = getRoleForPlayer(creatureId);
/* 3585 */     if (role == null) {
/*      */ 
/*      */ 
/*      */       
/* 3589 */       Village citvill = Villages.getVillageForCreature(creatureId);
/* 3590 */       if (citvill != null) {
/*      */         
/* 3592 */         role = getRoleForVillage(citvill.getId());
/* 3593 */         if (role == null)
/*      */         {
/*      */           
/* 3596 */           if (this.allianceNumber > 0)
/*      */           {
/*      */             
/* 3599 */             if (citvill.getAllianceNumber() == this.allianceNumber) {
/*      */               
/* 3601 */               Citizen cit = citvill.getCitizen(creatureId);
/* 3602 */               if (cit != null) {
/*      */                 
/* 3604 */                 VillageRole vr = cit.getRole();
/* 3605 */                 if (vr != null && vr.mayPerformActionsOnAlliedDeeds()) {
/*      */                   
/*      */                   try {
/*      */                     
/* 3609 */                     return getRoleForStatus((byte)5);
/*      */                   }
/* 3611 */                   catch (NoSuchRoleException e) {
/*      */ 
/*      */                     
/* 3614 */                     logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/* 3622 */       role = this.everybody;
/*      */     } 
/* 3624 */     return role;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Citizen getCitizen(long wurmId) {
/* 3629 */     return this.citizens.get(new Long(wurmId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Citizen[] getCitizens() {
/* 3638 */     Citizen[] toReturn = new Citizen[0];
/* 3639 */     if (this.citizens.size() > 0)
/* 3640 */       toReturn = (Citizen[])this.citizens.values().toArray((Object[])new Citizen[this.citizens.size()]); 
/* 3641 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void replaceNoDeed(Creature mayor) {
/*      */     try {
/* 3648 */       Item newDeed = ItemFactory.createItem(663, 50.0F + Server.rand
/* 3649 */           .nextFloat() * 50.0F, mayor.getName());
/* 3650 */       logger.log(Level.INFO, mayor.getName() + " replacing deed for " + getName() + " with id " + newDeed.getWurmId() + " from " + this.deedid);
/*      */       
/* 3652 */       newDeed.setName("Settlement deed");
/* 3653 */       newDeed.setDescription(getName());
/* 3654 */       newDeed.setData2(this.id);
/* 3655 */       mayor.getInventory().insertItem(newDeed, true);
/* 3656 */       logger.log(Level.INFO, "Inserted " + newDeed + " into inventory of " + mayor.getName());
/* 3657 */       long oldDeed = this.deedid;
/* 3658 */       if (this.gates != null)
/*      */       {
/* 3660 */         for (FenceGate gate : this.gates) {
/*      */ 
/*      */           
/*      */           try {
/* 3664 */             Item lock = gate.getLock();
/* 3665 */             lock.addKey(newDeed.getWurmId());
/* 3666 */             lock.removeKey(this.deedid);
/*      */           }
/* 3668 */           catch (NoSuchLockException noSuchLockException) {}
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 3673 */       logger.log(Level.INFO, "Fixed gates. Now destroying " + this.deedid);
/* 3674 */       Items.destroyItem(this.deedid);
/* 3675 */       setDeedId(newDeed.getWurmId());
/* 3676 */       logger.log(Level.INFO, "Setting deedid to " + newDeed.getWurmId());
/* 3677 */       mayor.addKey(newDeed, false);
/*      */       
/*      */       try {
/* 3680 */         logger.log(Level.INFO, "Verifying existance of deed " + newDeed.getWurmId());
/* 3681 */         Item i = Items.getItem(newDeed.getWurmId());
/* 3682 */         logger.log(Level.INFO, "Item " + i.getWurmId() + " was properly found in database! Data 2 is " + i.getData2());
/*      */       }
/* 3684 */       catch (NoSuchItemException nsi) {
/*      */         
/* 3686 */         logger.log(Level.INFO, "Item " + newDeed.getWurmId() + " not found in database!");
/*      */       } 
/*      */       
/*      */       try {
/* 3690 */         logger.log(Level.INFO, "Verifying removal of deed " + oldDeed);
/* 3691 */         Item i = Items.getItem(oldDeed);
/* 3692 */         logger.log(Level.INFO, "Deed " + oldDeed + " was erroneously found in database! Data is " + i.getData2());
/*      */       }
/* 3694 */       catch (NoSuchItemException nsi) {
/*      */         
/* 3696 */         logger.log(Level.INFO, "Item " + oldDeed + " properly not found in database!");
/*      */       }
/*      */     
/* 3699 */     } catch (NoSuchTemplateException nsi) {
/*      */       
/* 3701 */       logger.log(Level.WARNING, "No deed template for settlement " + this.name, (Throwable)nsi);
/*      */     }
/* 3703 */     catch (FailedException nsf) {
/*      */       
/* 3705 */       logger.log(Level.WARNING, "Failed to create deed for settlement " + this.name, (Throwable)nsf);
/*      */     }
/* 3707 */     catch (IOException iox) {
/*      */       
/* 3709 */       logger.log(Level.WARNING, "failed to set new deed id for the settlement of " + this.name, iox);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNewBounds(int newsx, int newsy, int newex, int newey) {
/* 3815 */     Zone[] coveredOldSurfaceZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, true);
/*      */     
/* 3817 */     Zone[] coveredOldCaveZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, false);
/* 3818 */     int oldStartPerimeterX = this.startx - 5 - this.perimeterTiles;
/* 3819 */     int oldStartPerimeterY = this.starty - 5 - this.perimeterTiles;
/* 3820 */     int oldEndPerimeterX = this.endx + 5 + this.perimeterTiles;
/* 3821 */     int oldEndPerimeterY = this.endy + 5 + this.perimeterTiles;
/*      */     
/* 3823 */     Rectangle oldPerimeter = new Rectangle(oldStartPerimeterX, oldStartPerimeterY, oldEndPerimeterX - oldStartPerimeterX, oldEndPerimeterY - oldStartPerimeterY);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3828 */       setStartX(newsx);
/* 3829 */       setStartY(newsy);
/* 3830 */       setEndX(newex);
/* 3831 */       setEndY(newey);
/*      */     }
/* 3833 */     catch (IOException iox) {
/*      */       
/* 3835 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 3837 */     int newStartPerimeterX = this.startx - 5 - this.perimeterTiles;
/* 3838 */     int newStartPerimeterY = this.starty - 5 - this.perimeterTiles;
/* 3839 */     int newEndPerimeterX = this.endx + 5 + this.perimeterTiles;
/* 3840 */     int newEndPerimeterY = this.endy + 5 + this.perimeterTiles;
/*      */     
/* 3842 */     for (int x = newStartPerimeterX; x <= newEndPerimeterX; x++) {
/* 3843 */       for (int n = newStartPerimeterY; n <= newEndPerimeterY; n++) {
/*      */         
/* 3845 */         if (!oldPerimeter.contains(x, n))
/* 3846 */           Zones.setKingdom(x, n, this.kingdom); 
/*      */       } 
/* 3848 */     }  Rectangle newPerimeter = new Rectangle(newStartPerimeterX, newStartPerimeterY, newEndPerimeterX - newStartPerimeterX, newEndPerimeterY - newStartPerimeterY);
/*      */     
/* 3850 */     for (int i = oldStartPerimeterX; i <= oldEndPerimeterX; i++) {
/*      */       
/* 3852 */       for (int n = oldStartPerimeterY; n <= oldEndPerimeterY; n++) {
/*      */         
/* 3854 */         if (!newPerimeter.contains(i, n)) {
/* 3855 */           Zones.setKingdom(i, n, (byte)0);
/*      */         }
/*      */       } 
/*      */     } 
/* 3859 */     GuardTower nw = Kingdoms.getClosestTower(Math.min(oldStartPerimeterX, newStartPerimeterX), 
/* 3860 */         Math.min(oldStartPerimeterY, newStartPerimeterY), true);
/* 3861 */     if (nw != null)
/* 3862 */       Kingdoms.addTowerKingdom(nw.getTower()); 
/* 3863 */     GuardTower ne = Kingdoms.getClosestTower(Math.max(oldEndPerimeterX, newEndPerimeterX), 
/* 3864 */         Math.min(oldStartPerimeterY, newStartPerimeterY), true);
/* 3865 */     if (ne != null && ne != nw)
/* 3866 */       Kingdoms.addTowerKingdom(ne.getTower()); 
/* 3867 */     GuardTower se = Kingdoms.getClosestTower(Math.min(oldStartPerimeterX, newStartPerimeterX), 
/* 3868 */         Math.max(oldEndPerimeterY, newEndPerimeterY), true);
/* 3869 */     if (se != null && se != nw && se != ne)
/* 3870 */       Kingdoms.addTowerKingdom(se.getTower()); 
/* 3871 */     GuardTower sw = Kingdoms.getClosestTower(Math.max(oldEndPerimeterX, newEndPerimeterX), 
/* 3872 */         Math.max(oldEndPerimeterY, newEndPerimeterY), true);
/* 3873 */     if (sw != null && sw != nw && sw != ne && sw != nw) {
/* 3874 */       Kingdoms.addTowerKingdom(sw.getTower());
/*      */     }
/* 3876 */     Zone[] coveredNewSurfaceZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, true);
/* 3877 */     Set<Zone> notfound = new HashSet<>();
/*      */     
/* 3879 */     for (int y = 0; y < coveredOldSurfaceZones.length; y++)
/*      */     {
/* 3881 */       notfound.add(coveredOldSurfaceZones[y]);
/*      */     }
/* 3883 */     boolean found = false;
/* 3884 */     for (int k = 0; k < coveredNewSurfaceZones.length; k++) {
/*      */       
/* 3886 */       found = false;
/* 3887 */       for (int n = 0; n < coveredOldSurfaceZones.length; n++) {
/*      */         
/* 3889 */         if (coveredNewSurfaceZones[k].getId() == coveredOldSurfaceZones[n].getId()) {
/*      */ 
/*      */ 
/*      */           
/* 3893 */           coveredNewSurfaceZones[k].updateVillage(this, true);
/* 3894 */           notfound.remove(coveredOldSurfaceZones[n]);
/* 3895 */           found = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 3900 */       if (!found)
/*      */       {
/* 3902 */         coveredNewSurfaceZones[k].updateVillage(this, true);
/*      */       }
/*      */     } 
/*      */     
/* 3906 */     for (Zone z : notfound)
/*      */     {
/* 3908 */       z.updateVillage(this, false);
/*      */     }
/* 3910 */     notfound.clear();
/* 3911 */     for (int j = 0; j < coveredOldCaveZones.length; j++)
/*      */     {
/* 3913 */       notfound.add(coveredOldCaveZones[j]);
/*      */     }
/* 3915 */     Zone[] coveredNewCaveZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, false);
/* 3916 */     for (int m = 0; m < coveredNewCaveZones.length; m++) {
/*      */       
/* 3918 */       found = false;
/* 3919 */       for (int n = 0; n < coveredOldCaveZones.length; n++) {
/*      */         
/* 3921 */         if (coveredNewCaveZones[m].getId() == coveredOldCaveZones[n].getId()) {
/*      */           
/* 3923 */           coveredOldCaveZones[n].updateVillage(this, true);
/* 3924 */           notfound.remove(coveredOldCaveZones[n]);
/* 3925 */           found = true;
/*      */           break;
/*      */         } 
/*      */       } 
/* 3929 */       if (!found) {
/* 3930 */         coveredNewCaveZones[m].updateVillage(this, true);
/*      */       }
/*      */     } 
/* 3933 */     for (Zone z : notfound)
/*      */     {
/* 3935 */       z.updateVillage(this, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCitizen(Creature creature) {
/* 3941 */     long wid = creature.getWurmId();
/* 3942 */     return this.citizens.keySet().contains(new Long(wid));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCitizen(long wid) {
/* 3947 */     return this.citizens.keySet().contains(new Long(wid));
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMayor(Creature creature) {
/* 3952 */     return isMayor(creature.getWurmId());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMayor(long playerId) {
/* 3957 */     Citizen c = getCitizen(playerId);
/* 3958 */     return (c != null && c.getRole().getStatus() == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkLeadership() {
/* 3963 */     Citizen[] citizarr = getCitizens();
/* 3964 */     Citizen leader = null;
/* 3965 */     Citizen currMayor = null;
/* 3966 */     Map<Long, Integer> votees = new HashMap<>();
/* 3967 */     int votesCast = 0;
/* 3968 */     for (int x = 0; x < citizarr.length; x++) {
/*      */       
/* 3970 */       if (citizarr[x].hasVoted()) {
/*      */         
/* 3972 */         votesCast++;
/* 3973 */         long votedFor = citizarr[x].getVotedFor();
/* 3974 */         Long vote = new Long(votedFor);
/* 3975 */         Integer votei = votees.get(vote);
/* 3976 */         if (votei == null)
/* 3977 */           votei = Integer.valueOf(0); 
/* 3978 */         int votes = votei.intValue() + 1;
/* 3979 */         votei = Integer.valueOf(votes);
/* 3980 */         votees.put(vote, votei);
/*      */       } 
/* 3982 */       if (citizarr[x].getRole().getStatus() == 2) {
/* 3983 */         currMayor = citizarr[x];
/*      */       }
/*      */       try {
/* 3986 */         citizarr[x].setVoteDate(-10L);
/* 3987 */         citizarr[x].setVotedFor(-10L);
/*      */       }
/* 3989 */       catch (IOException iox) {
/*      */         
/* 3991 */         logger.log(Level.WARNING, "Failed to clear votes for " + citizarr[x].getName() + ": " + iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/* 3994 */     long leaderlong = -10L;
/* 3995 */     int maxvotes = 0;
/* 3996 */     for (Long target : votees.keySet()) {
/*      */       
/* 3998 */       Integer votes = votees.get(target);
/* 3999 */       if (votes.intValue() > maxvotes) {
/*      */         
/* 4001 */         leaderlong = target.longValue();
/* 4002 */         maxvotes = votes.intValue();
/*      */       } 
/*      */     } 
/* 4005 */     leader = this.citizens.get(new Long(leaderlong));
/* 4006 */     logger.log(Level.INFO, getName() + " Checking if " + leader + " will become mayor with " + maxvotes + " out of " + votesCast + ".");
/*      */     
/* 4008 */     if (leader != null && changeRule(maxvotes, votesCast)) {
/*      */       
/* 4010 */       logger.log(Level.INFO, getName() + " swapping owners - old: " + currMayor + ", new: " + leader);
/* 4011 */       swapDeedOwners(currMayor, leader);
/*      */       
/*      */       try {
/* 4014 */         this.group.broadCastSafe(this.name + " has a new " + getRoleForStatus((byte)2).getName() + "! Hail " + leader
/* 4015 */             .getName() + "!", (byte)2);
/* 4016 */         addHistory(leader.getName(), "new " + getRoleForStatus((byte)2).getName() + " by a vote of " + maxvotes + " out of " + votesCast + " cast");
/*      */       
/*      */       }
/* 4019 */       catch (NoSuchRoleException nsr) {
/*      */         
/* 4021 */         logger.log(Level.WARNING, this.name + " has no ROLE_MAYOR!");
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 4028 */         if (currMayor != null)
/*      */         {
/* 4030 */           this.group.broadCastSafe(currMayor.getName() + " will be your " + getRoleForStatus((byte)2).getName() + " for another period! Hail " + currMayor
/* 4031 */               .getName() + "!", (byte)2);
/* 4032 */           addHistory(currMayor.getName(), "stays " + getRoleForStatus((byte)2).getName() + ". Number of votes cast: " + votesCast);
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 4037 */           this.group.broadCastSafe("You will have no " + getRoleForStatus((byte)2).getName() + " for another voting period.", (byte)2);
/*      */         }
/*      */       
/*      */       }
/* 4041 */       catch (NoSuchRoleException nsr) {
/*      */         
/* 4043 */         logger.log(Level.WARNING, this.name + " has no ROLE_MAYOR!");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Citizen getMayor() {
/* 4050 */     Citizen[] citizarr = getCitizens();
/* 4051 */     for (int x = 0; x < citizarr.length; x++) {
/*      */       
/* 4053 */       VillageRole role = citizarr[x].getRole();
/* 4054 */       if (role.getStatus() == 2)
/* 4055 */         return citizarr[x]; 
/*      */     } 
/* 4057 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void swapDeedOwners(Citizen mayor, Citizen newMayor) {
/*      */     try {
/* 4064 */       if (newMayor != null) {
/*      */ 
/*      */         
/*      */         try {
/* 4068 */           Item deed = Items.getItem(this.deedid);
/* 4069 */           Creature mayorCreature = null;
/* 4070 */           if (mayor != null) {
/*      */             
/*      */             try {
/*      */               
/* 4074 */               mayorCreature = Server.getInstance().getCreature(mayor.getId());
/*      */             }
/* 4076 */             catch (NoSuchCreatureException nsc) {
/*      */               
/* 4078 */               logger.log(Level.WARNING, "The mayor for " + this.name + " is a creature?", (Throwable)nsc);
/*      */             }
/* 4080 */             catch (NoSuchPlayerException nsp) {
/*      */               
/* 4082 */               logger.log(Level.INFO, mayor.getName() + " is offline loosing mayorship.");
/*      */             } 
/*      */           }
/* 4085 */           Creature newMayorCreature = null;
/*      */           
/*      */           try {
/* 4088 */             newMayorCreature = Server.getInstance().getCreature(newMayor.getId());
/*      */           }
/* 4090 */           catch (NoSuchCreatureException nsc) {
/*      */             
/* 4092 */             logger.log(Level.WARNING, "The mayor for " + this.name + " is a creature?", (Throwable)nsc);
/*      */           }
/* 4094 */           catch (NoSuchPlayerException nsp) {
/*      */             
/* 4096 */             logger.log(Level.INFO, newMayor.getName() + " is offline becoming mayor.");
/*      */           } 
/*      */           
/*      */           try {
/* 4100 */             if (mayor != null)
/* 4101 */               mayor.setRole(getRoleForStatus((byte)3)); 
/* 4102 */             newMayor.setRole(getRoleForStatus((byte)2));
/* 4103 */             if (mayorCreature != null && newMayorCreature != null) {
/* 4104 */               swapDeedOwners(mayorCreature, newMayorCreature, deed);
/* 4105 */             } else if (mayorCreature == null && newMayorCreature != null) {
/* 4106 */               swapDeedOwners(mayor, newMayorCreature, deed);
/* 4107 */             } else if (newMayorCreature == null && mayorCreature != null) {
/* 4108 */               swapDeedOwners(mayorCreature, newMayor, deed);
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 4113 */               Items.returnItemFromFreezer(this.deedid);
/* 4114 */               deed.setParentId(DbCreatureStatus.getInventoryIdFor(newMayor.getId()), true);
/* 4115 */               deed.setOwnerId(newMayor.getId());
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4121 */             if (mayorCreature != null) {
/* 4122 */               mayorCreature.getCommunicator().sendSafeServerMessage("You are no longer the mayor of " + this.name + ".");
/*      */             }
/* 4124 */             if (newMayorCreature != null) {
/* 4125 */               newMayorCreature.getCommunicator().sendSafeServerMessage("You are now the new mayor of " + this.name + ". Serve it well.");
/*      */             }
/* 4127 */             setMayor(newMayor.getName());
/*      */           }
/* 4129 */           catch (IOException iox) {
/*      */             
/* 4131 */             logger.log(Level.WARNING, getName() + " failed to set mayor status: " + iox.getMessage(), iox);
/*      */           }
/* 4133 */           catch (NoSuchRoleException nsr) {
/*      */             
/* 4135 */             logger.log(Level.WARNING, 
/* 4136 */                 getName() + " this settlement doesn't have the correct roles: " + nsr.getMessage(), (Throwable)nsr);
/*      */           }
/*      */         
/* 4139 */         } catch (NoSuchItemException nsi) {
/*      */           
/* 4141 */           logger.log(Level.WARNING, "Deed with id " + this.deedid + " for settlement " + getName() + ", " + this.id + " not found!", (Throwable)nsi);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 4147 */       else if (newMayor == null) {
/* 4148 */         logger.log(Level.INFO, "Error, new mayor is null: " + this.name + ".", new Exception());
/*      */       }
/*      */     
/* 4151 */     } catch (NullPointerException nsp) {
/*      */       
/* 4153 */       logger.log(Level.INFO, nsp.getMessage(), nsp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void swapDeedOwners(Creature owner, Creature receiver, Item deed) throws NoSuchItemException {
/* 4164 */     owner.getInventory().dropItem(deed.getWurmId(), false);
/* 4165 */     receiver.getInventory().insertItem(deed);
/*      */   }
/*      */ 
/*      */   
/*      */   private void swapDeedOwners(Creature owner, Citizen receiver, Item deed) throws NoSuchItemException {
/* 4170 */     owner.getInventory().dropItem(deed.getWurmId(), false);
/* 4171 */     deed.setOwnerId(receiver.getId());
/*      */     
/* 4173 */     deed.setParentId(DbCreatureStatus.getInventoryIdFor(receiver.getId()), owner.isOnSurface());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enoughVotes() {
/* 4181 */     if (!isDemocracy())
/*      */     {
/* 4183 */       return false;
/*      */     }
/* 4185 */     Citizen[] citizarr = getCitizens();
/* 4186 */     int votes = 0;
/* 4187 */     for (int x = 0; x < citizarr.length; x++) {
/*      */       
/* 4189 */       if (citizarr[x].hasVoted())
/* 4190 */         votes++; 
/*      */     } 
/* 4192 */     int activeCitizens = 0;
/* 4193 */     for (Long it : this.citizens.keySet()) {
/*      */       
/* 4195 */       long wurmid = it.longValue();
/* 4196 */       if (WurmId.getType(wurmid) == 0) {
/*      */         
/* 4198 */         long lastLogout = Players.getInstance().getLastLogoutForPlayer(wurmid);
/* 4199 */         if (System.currentTimeMillis() - lastLogout < 1209600000L)
/* 4200 */           activeCitizens++; 
/*      */       } 
/*      */     } 
/* 4203 */     logger.log(Level.INFO, getName() + " votes is " + votes + " for the last week, active citizens are " + activeCitizens);
/* 4204 */     return changeRule(votes, activeCitizens);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void vote(Creature voter, String targname) throws IOException, NoSuchPlayerException {
/* 4209 */     if (!isDemocracy()) {
/*      */       
/* 4211 */       voter.getCommunicator().sendNormalServerMessage("You vote for " + targname + " is noted, but ignored.", (byte)3);
/*      */       return;
/*      */     } 
/* 4214 */     if (!voter.getName().equals(targname)) {
/*      */       
/* 4216 */       Citizen votercit = this.citizens.get(new Long(voter.getWurmId()));
/* 4217 */       if (votercit != null) {
/*      */         
/* 4219 */         if (!votercit.hasVoted()) {
/*      */           
/* 4221 */           long vid = Players.getInstance().getWurmIdFor(targname);
/* 4222 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(vid);
/* 4223 */           if (pinf != null) {
/*      */             
/* 4225 */             if (pinf.isPaying()) {
/*      */               
/* 4227 */               Citizen targcit = this.citizens.get(new Long(vid));
/* 4228 */               if (targcit != null) {
/*      */                 
/* 4230 */                 votercit.setVotedFor(vid);
/* 4231 */                 votercit.setVoteDate(System.currentTimeMillis());
/* 4232 */                 voter.getCommunicator().sendNormalServerMessage("You vote for " + targname + " as mayor this week.");
/*      */                 
/* 4234 */                 if (enoughVotes()) {
/* 4235 */                   checkLeadership();
/*      */                 }
/*      */               } else {
/* 4238 */                 voter.getCommunicator()
/* 4239 */                   .sendNormalServerMessage(targname + " is not a citizen of " + this.name + ".", (byte)3);
/*      */               } 
/*      */             } else {
/* 4242 */               voter.getCommunicator().sendNormalServerMessage("You may only vote for premium players as mayor.", (byte)3);
/*      */             } 
/*      */           } else {
/* 4245 */             voter.getCommunicator().sendNormalServerMessage(targname + " is not a citizen of " + this.name + ".", (byte)3);
/*      */           } 
/*      */         } else {
/* 4248 */           voter.getCommunicator().sendNormalServerMessage("You have already voted in the election this week.", (byte)3);
/*      */         } 
/*      */       } else {
/* 4251 */         logger.log(Level.WARNING, voter.getName() + " tried to vote in a settlement he wasn't citizen of!");
/*      */       } 
/*      */     } else {
/* 4254 */       voter.getCommunicator().sendNormalServerMessage("You cannot vote for yourself in the mayor elections.", (byte)3);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void swapDeedOwners(Citizen owner, Creature receiver, Item deed) throws NoSuchItemException {
/* 4259 */     receiver.getInventory().insertItem(deed, true);
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
/*      */   private boolean changeRule(int votes, int totalVotes) {
/* 4271 */     logger.log(Level.INFO, getName() + " total votes is " + totalVotes + ". Votes is " + votes + " so fraction is " + (votes / totalVotes) + ". This is a democracy=" + this.democracy + ": 0.51*=" + (0.51D * totalVotes) + ", 0.81*=" + (0.81D * totalVotes));
/*      */ 
/*      */     
/* 4274 */     if (this.democracy) {
/* 4275 */       return (votes >= 0.51D * totalVotes);
/*      */     }
/* 4277 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Item getToken() throws NoSuchItemException {
/* 4282 */     return Items.getItem(this.tokenId);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getTag() {
/* 4287 */     return getName().substring(0, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getDisbanding() {
/* 4292 */     return this.disband;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDisbanding() {
/* 4297 */     return (this.disband != 0L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean checkDisband(long now) {
/* 4302 */     return (this.disband != 0L && now > this.disband);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isLeavingPmk() {
/* 4307 */     return (this.pmkKickDate != 0L);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean checkLeavePmk(long now) {
/* 4312 */     return (this.pmkKickDate > 0L && now > this.pmkKickDate);
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
/*      */   public final void startDisbanding(Creature performer, String aName, long disbid) {
/* 4330 */     addHistory(aName, "starts disbanding");
/* 4331 */     if (performer == null || (getMayor().getId() == disbid && getDiameterX() < 30 && getDiameterY() < 30)) {
/*      */ 
/*      */       
/*      */       try {
/* 4335 */         setDisbandTime(System.currentTimeMillis() + 3600000L);
/* 4336 */         setDisbander(disbid);
/*      */       }
/* 4338 */       catch (IOException iox) {
/*      */         
/* 4340 */         this.disband = System.currentTimeMillis() + 3600000L;
/* 4341 */         logger.log(Level.WARNING, "Failed to set disband time for settlement with id " + getId() + ".", iox);
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/* 4348 */         setDisbandTime(System.currentTimeMillis() + 86400000L);
/* 4349 */         setDisbander(disbid);
/*      */       }
/* 4351 */       catch (IOException iox) {
/*      */         
/* 4353 */         this.disband = System.currentTimeMillis() + 86400000L;
/* 4354 */         logger.log(Level.WARNING, "Failed to set disband time for settlement with id " + getId() + ".", iox);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getDisbander() {
/* 4365 */     return this.disbander;
/*      */   }
/*      */ 
/*      */   
/*      */   final void stopDisbanding() {
/* 4370 */     if (this.disband != 0L) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*      */         try {
/* 4376 */           Player player = Players.getInstance().getPlayer(getDisbander());
/* 4377 */           player.getCommunicator().sendAlertServerMessage("The settlement of " + getName() + " has been salvaged!", (byte)2);
/* 4378 */           addHistory(player.getName(), "salvages the settlement from disbanding");
/*      */         }
/* 4380 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 4382 */           addHistory("", "the settlement has been salvaged from disbanding");
/*      */         } 
/*      */         
/* 4385 */         Village[] allies = getAllies();
/* 4386 */         for (int x = 0; x < allies.length; x++)
/*      */         {
/* 4388 */           allies[x].broadCastSafe("The settlement of " + getName() + " has been salvaged.", (byte)2);
/*      */         }
/* 4390 */         setDisbandTime(0L);
/* 4391 */         setDisbander(-10L);
/*      */       }
/* 4393 */       catch (IOException iox) {
/*      */         
/* 4395 */         this.disband = 0L;
/* 4396 */         addHistory("", "the settlement has been salvaged from disbanding");
/* 4397 */         logger.log(Level.WARNING, "Failed to set disband time to 0 for settlement with id " + getId() + ".", iox);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private long getFoundingCost() {
/* 4404 */     int tiles = getDiameterX() * getDiameterY();
/* 4405 */     long moneyNeeded = tiles * Villages.TILE_COST;
/* 4406 */     moneyNeeded += this.perimeterTiles * Villages.PERIMETER_COST;
/* 4407 */     return moneyNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean givesTheftBonus() {
/* 4412 */     return (this.plan.isUnderSiege() && this.plan.hiredGuardNumber > 9);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void disband(String disbanderName) {
/* 4418 */     long moneyToReimburse = 0L;
/*      */ 
/*      */     
/* 4421 */     if (!disbanderName.equals("upkeep")) {
/*      */       
/* 4423 */       Citizen mayor = getMayor();
/* 4424 */       if (mayor != null) {
/*      */         
/* 4426 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(mayor.wurmId);
/* 4427 */         if (pinf != null)
/*      */         {
/* 4429 */           if (freeDisbands) {
/*      */             
/* 4431 */             long left = Servers.localServer.isFreeDeeds() ? 0L : this.plan.moneyLeft;
/*      */ 
/*      */             
/* 4434 */             left -= 10000L;
/*      */             
/*      */             try {
/* 4437 */               Item deed = Items.getItem(this.deedid);
/* 4438 */               if (deed.isNewDeed() && !Servers.localServer.isFreeDeeds())
/*      */               {
/* 4440 */                 logger.log(Level.INFO, "DISBANDING " + getName() + " left=" + left + ". Found cost=" + 
/* 4441 */                     getFoundingCost());
/* 4442 */                 left += getFoundingCost();
/*      */               }
/*      */             
/* 4445 */             } catch (NoSuchItemException nsi) {
/*      */               
/* 4447 */               logger.log(Level.WARNING, getName() + " No deed id with id=" + this.deedid, (Throwable)nsi);
/*      */             } 
/* 4449 */             Citizen[] arrayOfCitizen = getCitizens();
/* 4450 */             for (int n = 0; n < arrayOfCitizen.length; n++) {
/*      */               
/* 4452 */               if (WurmId.getType((arrayOfCitizen[n]).wurmId) == 1) {
/*      */                 
/*      */                 try {
/*      */                   
/* 4456 */                   Creature c = Creatures.getInstance().getCreature((arrayOfCitizen[n]).wurmId);
/* 4457 */                   if (c.isNpcTrader()) {
/*      */                     
/* 4459 */                     Shop shop = Economy.getEconomy().getShop(c);
/* 4460 */                     if (shop != null)
/*      */                     {
/* 4462 */                       if (!shop.isPersonal())
/*      */                       {
/* 4464 */                         logger.log(Level.INFO, "Adding 20 silver to " + pinf.getName() + " for trader in settlement " + 
/* 4465 */                             getName());
/* 4466 */                         left += 200000L;
/*      */                       }
/*      */                     
/*      */                     }
/* 4470 */                   } else if (c.isSpiritGuard() && !Servers.localServer.isFreeDeeds()) {
/*      */                     
/* 4472 */                     logger.log(Level.INFO, "Adding guard cost to " + pinf.getName() + " for guard in settlement " + 
/* 4473 */                         getName());
/* 4474 */                     left += Villages.GUARD_COST;
/*      */                   }
/*      */                 
/* 4477 */                 } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */               }
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 4483 */             if (left > 0L) {
/*      */               
/* 4485 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 4486 */               if (!lsw.addMoney(mayor.wurmId, pinf.getName(), left, "Disb " + getName())) {
/*      */                 
/* 4488 */                 logger.log(Level.INFO, "Postponing disbanding " + getName() + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } else {
/* 4503 */             long left = 0L;
/*      */ 
/*      */             
/* 4506 */             if ((!Servers.localServer.isFreeDeeds() && Servers.localServer.isUpkeep()) || (Servers.localServer
/* 4507 */               .isFreeDeeds() && Servers.localServer.isUpkeep() && this.creationDate > 
/* 4508 */               System.currentTimeMillis() + 2419200000L))
/*      */             {
/* 4510 */               left = this.plan.getDisbandMoneyLeft();
/*      */             }
/*      */             
/* 4513 */             moneyToReimburse += Math.max(left, 0L);
/* 4514 */             if (moneyToReimburse > 0L) {
/*      */               
/* 4516 */               LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 4517 */               if (!lsw.addMoney(mayor.wurmId, pinf.getName(), moneyToReimburse, "Disb " + getName())) {
/*      */                 
/* 4519 */                 logger.log(Level.INFO, "Postponing disbanding " + getName() + ".");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } else {
/* 4532 */         logger.log(Level.INFO, "NO mayor found for " + getName() + " when disbanding.");
/*      */       } 
/* 4534 */     }  if (this.gates != null)
/*      */     {
/*      */       
/* 4537 */       for (FenceGate gate : this.gates) {
/*      */ 
/*      */ 
/*      */         
/* 4541 */         gate.setOpenTime(0);
/* 4542 */         gate.setCloseTime(0);
/*      */       } 
/*      */     }
/* 4545 */     FenceGate.unManageGatesFor(getId());
/* 4546 */     MineDoorPermission.unManageMineDoorsFor(getId());
/* 4547 */     Creatures.getInstance().removeBrandingFor(getId());
/* 4548 */     VillageMessages.delete(getId());
/*      */     
/* 4550 */     Zone[] coveredZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, true); int x;
/* 4551 */     for (x = 0; x < coveredZones.length; x++)
/*      */     {
/* 4553 */       coveredZones[x].removeVillage(this);
/*      */     }
/* 4555 */     coveredZones = Zones.getZonesCoveredBy(this.startx, this.starty, this.endx, this.endy, false);
/* 4556 */     for (x = 0; x < coveredZones.length; x++)
/*      */     {
/* 4558 */       coveredZones[x].removeVillage(this);
/*      */     }
/* 4560 */     Zones.setKingdom(this.startx - 5 - getPerimeterSize(), this.starty - 5 - 
/* 4561 */         getPerimeterSize(), getPerimeterDiameterX(), getPerimeterDiameterY(), (byte)0);
/* 4562 */     Kingdoms.reAddKingdomInfluences(-5 - getPerimeterSize() * 2, this.starty - 5 - 
/* 4563 */         getPerimeterSize() * 2, this.endx + 5 + 
/* 4564 */         getPerimeterSize() * 2, this.endy + 5 + getPerimeterSize() * 2);
/*      */ 
/*      */     
/*      */     try {
/* 4568 */       Item token = getToken();
/* 4569 */       Items.destroyItem(token.getWurmId());
/*      */     }
/* 4571 */     catch (NoSuchItemException nsi) {
/*      */       
/* 4573 */       logger.log(Level.WARNING, "No token for settlement " + getName() + " when destroying it at " + getStartX() + ", " + 
/*      */           
/* 4575 */           getStartY() + ".", (Throwable)nsi);
/*      */     } 
/* 4577 */     Guard[] guardarr = getGuards();
/* 4578 */     for (int i = 0; i < guardarr.length; i++) {
/*      */       
/* 4580 */       Creature c = guardarr[i].getCreature();
/* 4581 */       deleteGuard(c, true);
/*      */     } 
/* 4583 */     Wagoner[] wagonerarr = getWagoners();
/* 4584 */     for (int j = 0; j < wagonerarr.length; j++) {
/*      */       
/* 4586 */       Creature c = wagonerarr[j].getCreature();
/* 4587 */       if (c != null)
/* 4588 */         deleteWagoner(c); 
/*      */     } 
/* 4590 */     Citizen[] citizarr = getCitizens();
/* 4591 */     for (int k = 0; k < citizarr.length; k++) {
/*      */       
/* 4593 */       if (WurmId.getType((citizarr[k]).wurmId) == 1) {
/*      */         
/*      */         try {
/*      */           
/* 4597 */           Creature c = Creatures.getInstance().getCreature((citizarr[k]).wurmId);
/* 4598 */           if (c.isNpcTrader()) {
/* 4599 */             c.destroy();
/*      */           }
/* 4601 */         } catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 4611 */         Creature c = Server.getInstance().getCreature((citizarr[k]).wurmId);
/* 4612 */         if (c.getMusicPlayer() != null)
/*      */         {
/* 4614 */           c.getMusicPlayer().checkMUSIC_DISBAND_SND();
/*      */         }
/*      */       }
/* 4617 */       catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */       
/*      */       }
/* 4621 */       catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4626 */       removeCitizen(citizarr[k]);
/*      */     } 
/* 4628 */     if (this.citizens != null)
/* 4629 */       this.citizens.clear(); 
/* 4630 */     VillageRole[] rolearr = getRoles();
/* 4631 */     for (int m = 0; m < rolearr.length; m++) {
/*      */ 
/*      */       
/*      */       try {
/* 4635 */         rolearr[m].delete();
/*      */       }
/* 4637 */       catch (IOException iox) {
/*      */         
/* 4639 */         logger.log(Level.WARNING, "Failed to delete role with id " + rolearr[m].getId() + " for settlement " + 
/* 4640 */             getName() + " with id " + this.id + " from db: " + iox
/* 4641 */             .getMessage(), iox);
/*      */       } 
/*      */     } 
/* 4644 */     if (this.roles != null) {
/* 4645 */       this.roles.clear();
/*      */     }
/*      */     try {
/* 4648 */       RecruitmentAds.deleteVillageAd(this);
/* 4649 */       delete();
/* 4650 */       deleteVillageMapAnnotations();
/*      */     }
/* 4652 */     catch (IOException iox) {
/*      */       
/* 4654 */       logger.log(Level.WARNING, "Failed to delete settlement " + getName() + " from db: " + iox.getMessage(), iox);
/*      */     } 
/* 4656 */     if (this.wars != null) {
/*      */       
/* 4658 */       for (Village opponent : this.wars.keySet()) {
/*      */         
/* 4660 */         opponent.broadCastSafe(getName() + " has just been disbanded!", (byte)2);
/* 4661 */         if (opponent.wars != null)
/* 4662 */           opponent.wars.remove(this); 
/* 4663 */         VillageWar war = this.wars.get(opponent);
/* 4664 */         war.delete();
/*      */       } 
/* 4666 */       this.wars.clear();
/*      */     } 
/* 4668 */     if (this.warDeclarations != null) {
/*      */       
/* 4670 */       for (Village opponent : this.warDeclarations.keySet()) {
/*      */         
/* 4672 */         opponent.broadCastSafe(getName() + " has just been disbanded!", (byte)2);
/* 4673 */         if (opponent.warDeclarations != null)
/* 4674 */           opponent.warDeclarations.remove(this); 
/* 4675 */         WarDeclaration war = this.warDeclarations.get(opponent);
/* 4676 */         war.delete();
/*      */       } 
/* 4678 */       this.warDeclarations.clear();
/*      */     } 
/* 4680 */     if (this.reputations != null) {
/*      */       
/* 4682 */       Reputation[] reps = getReputations();
/* 4683 */       for (int n = 0; n < reps.length; n++)
/*      */       {
/* 4685 */         reps[n].delete();
/*      */       }
/* 4687 */       this.reputations.clear();
/*      */     } 
/* 4689 */     this.plan.delete();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4698 */     this.plan = null;
/* 4699 */     Villages.removeVillage(this.id);
/* 4700 */     Server.getInstance().broadCastSafe(WurmCalendar.getTime(), false);
/* 4701 */     String vil = "settlement";
/* 4702 */     if (disbanderName.equals("upkeep")) {
/* 4703 */       Server.getInstance().broadCastSafe("The settlement of " + getName() + " has just been disbanded.", true, (byte)2);
/*      */     } else {
/* 4705 */       Server.getInstance().broadCastSafe("The settlement of " + 
/* 4706 */           getName() + " has just been disbanded by " + disbanderName + ".", true, (byte)2);
/* 4707 */     }  addHistory(disbanderName, "disbanded");
/* 4708 */     HistoryManager.addHistory(disbanderName, "disbanded " + getName(), false);
/* 4709 */     long check = System.currentTimeMillis();
/* 4710 */     if (Villages.wasLastVillage(this)) {
/*      */       
/* 4712 */       Kingdom kingdom = Kingdoms.getKingdom(this.kingdom);
/* 4713 */       if (kingdom != null)
/*      */       {
/* 4715 */         kingdom.disband();
/*      */       }
/*      */     } 
/* 4718 */     leavePvPAlliance();
/*      */     
/* 4720 */     if (freeDisbands) {
/*      */       
/* 4722 */       if (disbanderName.equals("upkeep")) {
/* 4723 */         Items.destroyItem(getDeedId());
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/* 4728 */           Item deed = Items.getItem(this.deedid);
/* 4729 */           if (!deed.isNewDeed()) {
/*      */             
/* 4731 */             deed.setName(deed.getTemplate().getName());
/* 4732 */             deed.setDescription("");
/* 4733 */             deed.setData(-1, -1);
/* 4734 */             deed.setAuxData((byte)0);
/*      */           } else {
/*      */             
/* 4737 */             Items.destroyItem(this.deedid);
/*      */           } 
/* 4739 */         } catch (NoSuchItemException noSuchItemException) {}
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 4746 */       Items.destroyItem(getDeedId());
/* 4747 */     }  logger.info("The settlement of " + getName() + ", " + this.id + " has just been disbanded by " + disbanderName + ".");
/* 4748 */     if (System.currentTimeMillis() - check > 1000L) {
/* 4749 */       logger.log(Level.INFO, "Lag detected when destroying deed at 7.11: " + 
/* 4750 */           (int)((System.currentTimeMillis() - check) / 1000L));
/*      */     }
/* 4752 */     GuardTower nw = Kingdoms.getClosestTower(this.startx, this.starty, true);
/* 4753 */     if (nw != null)
/* 4754 */       Kingdoms.addTowerKingdom(nw.getTower()); 
/* 4755 */     GuardTower ne = Kingdoms.getClosestTower(this.endx, this.starty, true);
/* 4756 */     if (ne != null && ne != nw)
/* 4757 */       Kingdoms.addTowerKingdom(ne.getTower()); 
/* 4758 */     GuardTower se = Kingdoms.getClosestTower(this.startx, this.endy, true);
/* 4759 */     if (se != null && se != ne && se != nw)
/* 4760 */       Kingdoms.addTowerKingdom(se.getTower()); 
/* 4761 */     GuardTower sw = Kingdoms.getClosestTower(this.endx, this.endy, true);
/* 4762 */     if (sw != null && sw != nw && sw != ne && sw != nw) {
/* 4763 */       Kingdoms.addTowerKingdom(sw.getTower());
/*      */     }
/*      */   }
/*      */   
/*      */   private void leavePvPAlliance() {
/* 4768 */     PvPAlliance pvpAll = PvPAlliance.getPvPAlliance(getAllianceNumber());
/* 4769 */     if (pvpAll != null)
/*      */     {
/* 4771 */       if (getId() == getAllianceNumber()) {
/*      */         
/* 4773 */         Village newCap = null;
/* 4774 */         Village[] allyArr = getAllies();
/* 4775 */         setAllianceNumber(0);
/* 4776 */         boolean alldisb = false;
/* 4777 */         if (!pvpAll.exists()) {
/*      */           
/* 4779 */           alldisb = true;
/* 4780 */           pvpAll.delete();
/* 4781 */           pvpAll.sendClearAllianceAnnotations();
/* 4782 */           pvpAll.deleteAllianceMapAnnotations();
/*      */         } 
/* 4784 */         for (Village v : allyArr)
/*      */         {
/* 4786 */           if (v.getId() != getId())
/*      */           {
/* 4788 */             if (alldisb)
/*      */             {
/* 4790 */               v.broadCastAlert(pvpAll.getName() + " alliance has been disbanded.");
/* 4791 */               v.setAllianceNumber(0);
/*      */             }
/* 4793 */             else if (newCap == null)
/*      */             {
/* 4795 */               newCap = v;
/* 4796 */               v.setAllianceNumber(newCap.getId());
/* 4797 */               pvpAll.setIdNumber(newCap.getId());
/* 4798 */               v.broadCastAlert(getName() + " has left the " + pvpAll.getName() + " and " + v.getName() + " is the new main settlement.");
/*      */               
/* 4800 */               v.addHistory(getName(), "left the " + pvpAll.getName());
/*      */             }
/*      */             else
/*      */             {
/* 4804 */               v.setAllianceNumber(newCap.getId());
/* 4805 */               v.broadCastAlert(getName() + " has left the " + pvpAll.getName() + " and " + newCap.getName() + " is the new capital.");
/*      */               
/* 4807 */               v.addHistory(getName(), "left the " + pvpAll.getName() + ", making " + newCap.getName() + " the new capital.");
/*      */             }
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4815 */         Village[] allyArr = getAllies();
/* 4816 */         boolean alldisb = false;
/* 4817 */         setAllianceNumber(0);
/* 4818 */         if (!pvpAll.exists()) {
/*      */           
/* 4820 */           alldisb = true;
/* 4821 */           pvpAll.delete();
/*      */         } 
/* 4823 */         for (Village v : allyArr) {
/*      */           
/* 4825 */           if (v.getId() != getId())
/*      */           {
/* 4827 */             if (alldisb) {
/*      */               
/* 4829 */               v.broadCastAlert(pvpAll.getName() + " alliance has been disbanded.");
/* 4830 */               v.setAllianceNumber(0);
/*      */             }
/*      */             else {
/*      */               
/* 4834 */               v.broadCastAlert(getName() + " has left the " + pvpAll.getName() + ".");
/* 4835 */               v.addHistory(getName(), "left the " + pvpAll.getName() + ".");
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final Reputation setReputation(long wurmid, int val, boolean guest, boolean override) {
/* 4845 */     if (WurmId.getType(wurmid) == 0) {
/*      */       
/* 4847 */       Long key = new Long(wurmid);
/* 4848 */       Reputation r = this.reputations.get(key);
/* 4849 */       if (r != null) {
/*      */         
/* 4851 */         r.setValue(val, override);
/* 4852 */         if (r.getValue() == 0)
/*      */         {
/* 4854 */           this.reputations.remove(key);
/* 4855 */           r = null;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 4860 */       else if (val != 0) {
/*      */         
/* 4862 */         r = new Reputation(wurmid, this.id, false, val, guest, false);
/* 4863 */         this.reputations.put(key, r);
/*      */       } 
/*      */       
/* 4866 */       if (val <= -30) {
/*      */ 
/*      */         
/*      */         try {
/* 4870 */           Creature cret = Server.getInstance().getCreature(wurmid);
/* 4871 */           checkIfRaiseAlert(cret);
/*      */         }
/* 4873 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */         
/*      */         }
/* 4877 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 4883 */         removeTarget(wurmid, true);
/* 4884 */       }  return r;
/*      */     } 
/* 4886 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void modifyReputation(long wurmid, int val, boolean guest) {
/* 4891 */     if (WurmId.getType(wurmid) == 0) {
/*      */       
/* 4893 */       Long key = new Long(wurmid);
/* 4894 */       Reputation r = this.reputations.get(key);
/* 4895 */       if (r != null) {
/*      */         
/* 4897 */         r.modify(val);
/* 4898 */         if (r.getValue() == 0)
/*      */         {
/* 4900 */           this.reputations.remove(key);
/* 4901 */           r = null;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 4906 */       else if (val != 0) {
/*      */         
/* 4908 */         r = new Reputation(wurmid, this.id, false, val, guest, false);
/* 4909 */         this.reputations.put(key, r);
/*      */       } 
/*      */       
/* 4912 */       if (r != null && r.getValue() <= -30) {
/*      */ 
/*      */         
/*      */         try {
/* 4916 */           Creature cret = Server.getInstance().getCreature(wurmid);
/* 4917 */           checkIfRaiseAlert(cret);
/*      */         }
/* 4919 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 4921 */           logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp);
/*      */         }
/* 4923 */         catch (NoSuchCreatureException nsc) {
/*      */           
/* 4925 */           logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc);
/*      */         } 
/*      */       } else {
/*      */         
/* 4929 */         removeTarget(wurmid, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final Reputation getReputationObject(long creatureId) {
/* 4935 */     return this.reputations.get(new Long(creatureId));
/*      */   }
/*      */ 
/*      */   
/*      */   public final void modifyUpkeep(long upkeepMod) throws IOException {
/* 4940 */     setUpkeep(upkeepMod + this.upkeep);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHighwayFound() {
/* 4945 */     return this.settings.hasPermission(VillagePermissions.HIGHWAY_OPT_IN.getBit());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isKosAllowed() {
/* 4950 */     return this.settings.hasPermission(VillagePermissions.ALLOW_KOS.getBit());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHighwayAllowed() {
/* 4955 */     return this.settings.hasPermission(VillagePermissions.ALLOW_HIGHWAYS.getBit());
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setIsHighwayFound(boolean highwayFound) {
/* 4960 */     this.settings.setPermissionBit(VillagePermissions.HIGHWAY_OPT_IN.getBit(), highwayFound);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setIsKosAllowed(boolean kosAlloed) {
/* 4965 */     this.settings.setPermissionBit(VillagePermissions.ALLOW_KOS.getBit(), kosAlloed);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setIsHighwayAllowed(boolean highwayAllowed) {
/* 4970 */     this.settings.setPermissionBit(VillagePermissions.ALLOW_HIGHWAYS.getBit(), highwayAllowed);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean acceptsMerchants() {
/* 4975 */     return this.acceptsMerchants;
/*      */   }
/*      */ 
/*      */   
/*      */   public final HistoryEvent[] getHistoryEvents() {
/* 4980 */     return this.history.<HistoryEvent>toArray(new HistoryEvent[this.history.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean twitChat() {
/* 4985 */     return this.twitChat;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getHistorySize() {
/* 4990 */     return this.history.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public final String[] getHistoryAsStrings(int numevents) {
/* 4995 */     String[] hist = new String[0];
/* 4996 */     if (this.history.size() > 0) {
/*      */       
/* 4998 */       int numbersToFetch = Math.min(numevents, this.history.size());
/*      */       
/* 5000 */       hist = new String[numbersToFetch];
/* 5001 */       HistoryEvent[] events = getHistoryEvents();
/* 5002 */       for (int x = 0; x < numbersToFetch; x++)
/*      */       {
/* 5004 */         hist[x] = events[x].getLongDesc();
/*      */       }
/*      */     } 
/* 5007 */     return hist;
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
/*      */   public final String toString() {
/* 5119 */     return "Village [ID: " + this.id + ", Name: " + this.name + ", DeedId: " + this.deedid + ", Kingdom: " + Kingdoms.getNameFor(this.kingdom) + ", Size: " + ((this.endx - this.startx) / 2) + ']';
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void putGuardsAtToken() {
/* 5125 */     Guard[] guardarr = getGuards();
/*      */     
/*      */     try {
/* 5128 */       for (int x = 0; x < guardarr.length; x++)
/*      */       {
/* 5130 */         guardarr[x].getCreature().blinkTo(getToken().getTileX(), getToken().getTileY(), 
/* 5131 */             getToken().isOnSurface() ? 0 : -1, 0);
/*      */       }
/*      */     }
/* 5134 */     catch (Exception ex) {
/*      */       
/* 5136 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean allowsAggCreatures() {
/* 5142 */     return this.allowsAggCreatures;
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
/*      */   public int getDiameterX() {
/* 5157 */     return this.endx - this.startx + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDiameterY() {
/* 5162 */     return this.endy - this.starty + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxGuards() {
/* 5167 */     return GuardPlan.getMaxGuards(getDiameterX(), getDiameterY());
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumTiles() {
/* 5172 */     return getDiameterX() * getDiameterY();
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getNumCreaturesNotHuman() {
/* 5177 */     float found = 0.0F;
/* 5178 */     for (int x = getStartX(); x <= getEndX(); x++) {
/* 5179 */       for (int y = getStartY(); y <= getEndY(); y++) {
/*      */         
/* 5181 */         found += getNumCreaturesNotHumanOn(x, y, true, false);
/* 5182 */         found += getNumCreaturesNotHumanOn(x, y, false, false);
/*      */       } 
/* 5184 */     }  return found;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getNumBrandedCreaturesNotHuman() {
/* 5189 */     float found = 0.0F;
/* 5190 */     for (int x = getStartX(); x <= getEndX(); x++) {
/* 5191 */       for (int y = getStartY(); y <= getEndY(); y++) {
/*      */         
/* 5193 */         found += getNumCreaturesNotHumanOn(x, y, true, true);
/* 5194 */         found += getNumCreaturesNotHumanOn(x, y, false, true);
/*      */       } 
/* 5196 */     }  return found;
/*      */   }
/*      */ 
/*      */   
/*      */   private float getNumCreaturesNotHumanOn(int x, int y, boolean onSurface, boolean findBranded) {
/* 5201 */     float found = 0.0F;
/* 5202 */     VolaTile t = Zones.getTileOrNull(x, y, onSurface);
/* 5203 */     if (t != null && t.getVillage() == this) {
/*      */       
/* 5205 */       Creature[] crets = t.getCreatures();
/* 5206 */       for (Creature c : crets) {
/*      */         
/* 5208 */         if (!c.isHuman() && (c.isAnimal() || c.isMonster()))
/*      */         {
/*      */           
/* 5211 */           if (findBranded && c.isBrandedBy(getId())) {
/* 5212 */             found++;
/* 5213 */           } else if (!findBranded) {
/* 5214 */             found++;
/*      */           } 
/*      */         }
/*      */       } 
/* 5218 */       Item[] items = t.getItems();
/* 5219 */       for (Item i : items) {
/*      */         
/* 5221 */         if (i.getTemplateId() == 1311 && !i.isEmpty(true))
/* 5222 */           found++; 
/* 5223 */         if (i.isVehicle())
/*      */         {
/* 5225 */           for (Item v : i.getAllItems(true)) {
/*      */             
/* 5227 */             if (v.getTemplateId() == 1311 && !v.isEmpty(true)) {
/* 5228 */               found++;
/*      */             }
/*      */           } 
/*      */         }
/* 5232 */         if (i.getTemplateId() == 1432)
/*      */         {
/* 5234 */           for (Item item : i.getAllItems(true)) {
/*      */             
/* 5236 */             if (item.getTemplateId() == 1436 && !item.isEmpty(true)) {
/*      */               
/* 5238 */               Item[] chickens = item.getAllItems(true);
/* 5239 */               for (int z = 0; z < chickens.length; z++)
/*      */               {
/* 5241 */                 found++;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 5248 */     return found;
/*      */   }
/*      */   
/* 5251 */   public static final float OPTIMUMCRETRATIO = Servers.localServer.PVPSERVER ? 5.0F : 15.0F;
/*      */   
/*      */   public static final float OFFDEEDCRETRATIO = 10.0F;
/*      */ 
/*      */   
/*      */   public final float getCreatureRatio() {
/* 5257 */     return getNumTiles() / getNumCreaturesNotHuman();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPerimeterDiameterX() {
/* 5262 */     return getDiameterX() + 5 + 5 + this.perimeterTiles * 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPerimeterDiameterY() {
/* 5267 */     return getDiameterY() + 5 + 5 + this.perimeterTiles * 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxCitizens() {
/* 5272 */     return getNumTiles() / 11;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getConsumerKey() {
/* 5277 */     return this.consumerKeyToUse;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getConsumerSecret() {
/* 5282 */     return this.consumerSecretToUse;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getApplicationToken() {
/* 5287 */     return this.applicationToken;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getApplicationSecret() {
/* 5292 */     return this.applicationSecret;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPerimeterNonFreeTiles() {
/* 5297 */     return getPerimeterDiameterX() * 
/* 5298 */       getPerimeterDiameterY() - (
/* 5299 */       getDiameterX() + 5 + 5) * (getDiameterY() + 5 + 5);
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
/*      */   public int compareTo(Village aVillage) {
/* 5313 */     return getName().compareTo(aVillage.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertOfflineCitizensToKingdom(byte newKingdom, boolean updateTimeStamp) {
/* 5318 */     Citizen[] citiz = getCitizens();
/* 5319 */     for (Citizen c : citiz) {
/*      */       
/* 5321 */       if (WurmId.getType(c.getId()) == 0) {
/*      */         
/*      */         try {
/*      */           
/* 5325 */           Players.getInstance().getPlayer(c.getId());
/*      */         }
/* 5327 */         catch (NoSuchPlayerException nsp) {
/*      */           
/* 5329 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(c.getId());
/* 5330 */           if (updateTimeStamp)
/* 5331 */             pinf.setChangedKingdom(); 
/* 5332 */           Players.convertPlayerToKingdom(c.getId(), newKingdom);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertTowersWithinDistance(int distance) {
/* 5340 */     int sx = Zones.safeTileX(getStartX() - getPerimeterSize() - 5 - distance);
/* 5341 */     int ex = Zones.safeTileX(getEndX() + getPerimeterSize() + 5 + distance);
/* 5342 */     int sy = Zones.safeTileY(getStartY() - getPerimeterSize() - 5 - distance);
/* 5343 */     int ey = Zones.safeTileY(getEndY() + getPerimeterSize() + 5 + distance);
/* 5344 */     Kingdoms.convertTowersWithin(sx, sy, ex, ey, this.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertTowersWithinPerimeter() {
/* 5349 */     int sx = Zones.safeTileX(getStartX() - getPerimeterSize() - 5);
/* 5350 */     int ex = Zones.safeTileX(getEndX() + getPerimeterSize() + 5);
/* 5351 */     int sy = Zones.safeTileY(getStartY() - getPerimeterSize() - 5);
/* 5352 */     int ey = Zones.safeTileY(getEndY() + getPerimeterSize() + 5);
/* 5353 */     Kingdoms.convertTowersWithin(sx, sy, ex, ey, this.kingdom);
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertToKingdom(byte newKingdom, boolean convertOnlyCitizens, boolean setTimeStamp) {
/* 5358 */     if (newKingdom != this.kingdom) {
/*      */       
/*      */       try {
/* 5361 */         leavePvPAlliance();
/* 5362 */         byte oldKingdom = this.kingdom;
/* 5363 */         setKingdom(newKingdom, setTimeStamp);
/* 5364 */         int sx = Zones.safeTileX(getStartX() - getPerimeterSize() - 5);
/* 5365 */         int ex = Zones.safeTileX(getEndX() + getPerimeterSize() + 5);
/* 5366 */         int sy = Zones.safeTileY(getStartY() - getPerimeterSize() - 5);
/* 5367 */         int ey = Zones.safeTileY(getEndY() + getPerimeterSize() + 5);
/* 5368 */         Kingdoms.convertTowersWithin(sx, sy, ex, ey, newKingdom);
/* 5369 */         for (int x = sx; x < ex; x++) {
/*      */           
/* 5371 */           for (int y = sy; y < ey; y++)
/*      */           {
/* 5373 */             convertCreatures(oldKingdom, newKingdom, x, y, true, convertOnlyCitizens, setTimeStamp);
/* 5374 */             convertCreatures(oldKingdom, newKingdom, x, y, false, convertOnlyCitizens, setTimeStamp);
/*      */           }
/*      */         
/*      */         } 
/* 5378 */       } catch (IOException iox) {
/*      */         
/* 5380 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean convertCreatures(byte oldkingdom, byte newkingdom, int x, int y, boolean tsurfaced, boolean convertOnlyCitizens, boolean setTimeStamp) {
/* 5387 */     VolaTile t = Zones.getTileOrNull(x, y, tsurfaced);
/* 5388 */     if (t != null) {
/*      */       
/* 5390 */       Creature[] crets = t.getCreatures();
/* 5391 */       if (crets.length > 0)
/*      */       {
/* 5393 */         for (int c = 0; c < crets.length; c++) {
/*      */           
/* 5395 */           if (crets[c].getKingdomId() == oldkingdom) {
/*      */             
/*      */             try {
/*      */ 
/*      */               
/* 5400 */               boolean convertedMayor = false;
/* 5401 */               Citizen mayor = getMayor();
/* 5402 */               if (mayor != null && crets[c].getWurmId() == mayor.getId()) {
/*      */ 
/*      */                 
/*      */                 try {
/* 5406 */                   mayor.role = getRoleForStatus((byte)3);
/*      */                 }
/* 5408 */                 catch (NoSuchRoleException e) {
/*      */                   
/* 5410 */                   logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */                 } 
/* 5412 */                 convertedMayor = true;
/*      */               } 
/* 5414 */               if (!crets[c].isPlayer() || !convertOnlyCitizens || isCitizen(crets[c]))
/* 5415 */                 crets[c].setKingdomId(newkingdom, true, setTimeStamp); 
/* 5416 */               if (crets[c].isKingdomGuard()) {
/*      */                 
/* 5418 */                 GuardTower tower = Kingdoms.getTower(crets[c]);
/* 5419 */                 if (tower != null)
/*      */                 {
/* 5421 */                   if (tower.getTower().getAuxData() != newkingdom) {
/*      */                     
/* 5423 */                     Kingdoms.removeInfluenceForTower(tower.getTower());
/* 5424 */                     tower.getTower().setAuxData(newkingdom);
/*      */                     
/* 5426 */                     Kingdom k = Kingdoms.getKingdom(newkingdom);
/* 5427 */                     if (k != null) {
/*      */                       
/* 5429 */                       String aName = k.getName() + " guard tower";
/* 5430 */                       tower.getTower().setName(aName);
/*      */                     } 
/* 5432 */                     Kingdoms.addTowerKingdom(tower.getTower());
/* 5433 */                     tower.getTower().updateIfGroundItem();
/*      */                   } 
/*      */                 }
/*      */               } 
/* 5437 */               if (convertedMayor)
/*      */               {
/* 5439 */                 if (mayor != null) {
/*      */                   
/*      */                   try {
/* 5442 */                     mayor.role = getRoleForStatus((byte)2);
/*      */                   }
/* 5444 */                   catch (NoSuchRoleException e) {
/*      */                     
/* 5446 */                     logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */                   } 
/*      */                 } else {
/* 5449 */                   logger.log(Level.WARNING, "Mayor role became null while converting.");
/*      */                 } 
/*      */               }
/* 5452 */             } catch (IOException iox) {
/*      */               
/* 5454 */               logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 5460 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private final Twit createTwit(String message) {
/* 5465 */     if (this.canTwit)
/* 5466 */       return new Twit(this.name, message, this.consumerKeyToUse, this.consumerSecretToUse, this.applicationToken, this.applicationSecret, true); 
/* 5467 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void twit(String message) {
/* 5472 */     if (isTwitEnabled()) {
/*      */       
/* 5474 */       Twit t = createTwit(message);
/* 5475 */       if (t != null) {
/* 5476 */         Twit.twit(t);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final boolean isTwitEnabled() {
/* 5482 */     return this.twitEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithWarValue() {
/* 5487 */     return this.faithWar;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithHealValue() {
/* 5492 */     return this.faithHeal;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithCreateValue() {
/* 5497 */     return this.faithCreate;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithWarBonus() {
/* 5502 */     return Math.min(30.0F, this.faithWar / this.faithDivideVal);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithHealBonus() {
/* 5507 */     return Math.min(30.0F, this.faithHeal / this.faithDivideVal);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFaithCreateBonus() {
/* 5512 */     return Math.min(30.0F, this.faithCreate / this.faithDivideVal);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getSpawnSituation() {
/* 5517 */     if (isCapital() || this.isPermanent)
/* 5518 */       return 1; 
/* 5519 */     return this.spawnSituation;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAllianceNumber() {
/* 5524 */     return this.allianceNumber;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addHotaWin() {
/* 5529 */     for (Citizen citizen : this.citizens.values()) {
/*      */       
/* 5531 */       if (citizen.isPlayer()) {
/*      */         
/* 5533 */         PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(citizen.getId());
/* 5534 */         if (pinf != null)
/*      */         {
/* 5536 */           pinf.setHotaWins((short)(pinf.getHotaWins() + 1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5545 */     setHotaWins((short)(this.hotaWins + 1));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void createHotaPrize(int winStreak) {
/*      */     try {
/* 5552 */       Item statue = ItemFactory.createItem(742, 99.0F, null);
/* 5553 */       byte material = 7;
/* 5554 */       if (winStreak > 50) {
/* 5555 */         material = 56;
/* 5556 */       } else if (winStreak > 40) {
/* 5557 */         material = 57;
/* 5558 */       } else if (winStreak > 30) {
/* 5559 */         material = 54;
/* 5560 */       } else if (winStreak > 15) {
/* 5561 */         material = 52;
/* 5562 */       }  statue.setMaterial(material);
/* 5563 */       float posX = getToken().getPosX() - 2.0F + Server.rand.nextFloat() * 4.0F;
/* 5564 */       float posY = getToken().getPosY() - 2.0F + Server.rand.nextFloat() * 4.0F;
/* 5565 */       statue.setPosXYZRotation(posX, posY, Zones.calculateHeight(posX, posY, true), Server.rand.nextInt(350));
/*      */       
/* 5567 */       for (int i = 0; i < winStreak; i++) {
/*      */         
/* 5569 */         if (i / 11 == winStreak % 11) {
/*      */           
/* 5571 */           statue.setAuxData((byte)0);
/* 5572 */           statue.setData1(1);
/*      */         }
/*      */         else {
/*      */           
/* 5576 */           statue.setAuxData((byte)winStreak);
/*      */         } 
/*      */       } 
/* 5579 */       int r = winStreak * 50 & 0xFF;
/* 5580 */       int g = 0;
/* 5581 */       int b = 0;
/* 5582 */       if (winStreak > 5 && winStreak < 16)
/* 5583 */         r = 0; 
/* 5584 */       if (winStreak > 5 && winStreak < 20)
/* 5585 */         g = winStreak * 50 & 0xFF; 
/* 5586 */       if (winStreak > 5 && winStreak < 30)
/* 5587 */         b = winStreak * 50 & 0xFF; 
/* 5588 */       if (winStreak >= 30) {
/*      */         
/* 5590 */         g = winStreak * 80 & 0xFF;
/* 5591 */         b = winStreak * 120 & 0xFF;
/*      */       } 
/* 5593 */       statue.setColor(WurmColor.createColor(r, g, b));
/* 5594 */       statue.getColor();
/* 5595 */       Zone z = Zones.getZone(statue.getTileX(), statue.getTileY(), true);
/*      */       
/* 5597 */       int numHelpers = 0;
/* 5598 */       for (Citizen c : this.citizens.values()) {
/*      */         
/* 5600 */         if (Hota.getHelpValue(c.getId()) > 0)
/* 5601 */           numHelpers++; 
/*      */       } 
/* 5603 */       numHelpers = Math.min(20, numHelpers); int x;
/* 5604 */       for (x = 0; x < numHelpers; x++) {
/*      */         
/* 5606 */         Item medallion = ItemFactory.createItem(740, Math.min(99, 80 + winStreak), null);
/* 5607 */         medallion.setAuxData((byte)winStreak);
/* 5608 */         if (winStreak > 40) {
/* 5609 */           medallion.setMaterial((byte)57);
/* 5610 */         } else if (winStreak > 30) {
/* 5611 */           medallion.setMaterial((byte)56);
/* 5612 */         } else if (winStreak > 20) {
/* 5613 */           medallion.setMaterial((byte)54);
/* 5614 */         } else if (winStreak > 10) {
/* 5615 */           medallion.setMaterial((byte)52);
/* 5616 */         }  statue.insertItem(medallion);
/*      */       } 
/* 5618 */       for (x = 0; x < 5; x++) {
/*      */         
/* 5620 */         Item lump = ItemFactory.createItem(694, Math.min(99, 50 + winStreak), null);
/* 5621 */         statue.insertItem(lump);
/*      */       } 
/* 5623 */       for (x = 0; x < 5; x++) {
/*      */         
/* 5625 */         Item lump = ItemFactory.createItem(698, Math.min(99, 50 + winStreak), null);
/* 5626 */         statue.insertItem(lump);
/*      */       } 
/* 5628 */       z.addItem(statue);
/*      */     }
/* 5630 */     catch (Exception ex) {
/*      */       
/* 5632 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getHotaWins() {
/* 5638 */     return this.hotaWins;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean mayChangeName() {
/* 5643 */     return (System.currentTimeMillis() - this.lastChangedName > (Servers.localServer.testServer ? 60000L : 14515200000L));
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
/*      */   public final long getAvailablePlanMoney() {
/* 5668 */     if (this.plan.moneyLeft < 30000L)
/* 5669 */       return 0L; 
/* 5670 */     return this.plan.moneyLeft - 30000L;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getSettings() {
/* 5675 */     return this.settings.getPermissions();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getVillageReputation() {
/* 5680 */     return this.villageReputation;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasBadReputation() {
/* 5685 */     return (this.villageReputation >= 50);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<Citizen> getTraders() {
/* 5693 */     List<Citizen> toReturn = new ArrayList<>();
/*      */     
/* 5695 */     for (Citizen citizen : this.citizens.values()) {
/*      */       
/* 5697 */       if (WurmId.getType(citizen.wurmId) == 1) {
/*      */         
/*      */         try {
/*      */           
/* 5701 */           Creature c = Creatures.getInstance().getCreature(citizen.wurmId);
/* 5702 */           if (c.isNpcTrader())
/*      */           {
/* 5704 */             toReturn.add(citizen);
/*      */           }
/*      */         }
/* 5707 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5713 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addVillageRecruitee(String pName, long pId) {
/* 5718 */     VillageRecruitee newRecruit = new VillageRecruitee(getId(), pId, pName);
/* 5719 */     if (addVillageRecruitee(newRecruit)) {
/*      */       
/* 5721 */       saveRecruitee(newRecruit);
/* 5722 */       return true;
/*      */     } 
/* 5724 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean removeRecruitee(long wid) {
/* 5729 */     for (Iterator<VillageRecruitee> it = this.recruitees.iterator(); it.hasNext(); ) {
/*      */       
/* 5731 */       VillageRecruitee vr = it.next();
/* 5732 */       if (vr.getRecruiteeId() == wid) {
/*      */         
/* 5734 */         deleteRecruitee(vr);
/* 5735 */         return this.recruitees.remove(vr);
/*      */       } 
/*      */     } 
/*      */     
/* 5739 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final VillageRecruitee[] getRecruitees() {
/* 5746 */     VillageRecruitee[] array = new VillageRecruitee[this.recruitees.size()];
/* 5747 */     array = this.recruitees.<VillageRecruitee>toArray(array);
/* 5748 */     return array;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean joinVillage(Player player) {
/* 5753 */     VillageRecruitee vr = getRecruiteeById(player.getWurmId());
/* 5754 */     if (vr == null) {
/*      */       
/* 5756 */       player.getCommunicator().sendNormalServerMessage("You are not on the village recruitment list.");
/* 5757 */       return false;
/*      */     } 
/*      */     
/* 5760 */     if (player.getCitizenVillage() != null && player.getCitizenVillage().isMayor(player.getWurmId())) {
/*      */       
/* 5762 */       player.getCommunicator().sendNormalServerMessage("You may not join a village while being the mayor of another village.");
/* 5763 */       return false;
/*      */     } 
/*      */     
/* 5766 */     if (player.isPlayer() && player.mayChangeVillageInMillis() > 0L) {
/*      */       
/* 5768 */       player.getCommunicator().sendNormalServerMessage("You may not change village until " + Server.getTimeFor(player.mayChangeVillageInMillis()) + " has elapsed.");
/* 5769 */       return false;
/*      */     } 
/*      */     
/* 5772 */     if (this.kingdom != player.getKingdomId()) {
/*      */       
/* 5774 */       player.getCommunicator().sendNormalServerMessage("You must be of the same kingdom as the village you are trying to join.");
/* 5775 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 5780 */       addCitizen((Creature)player, getRoleForStatus((byte)3));
/* 5781 */       if (player.canUseFreeVillageTeleport()) {
/*      */         
/* 5783 */         VillageTeleportQuestion vtq = new VillageTeleportQuestion((Creature)player);
/* 5784 */         vtq.sendQuestion();
/*      */       } 
/*      */       
/* 5787 */       removeRecruitee(player.getWurmId());
/*      */       
/* 5789 */       return true;
/*      */     }
/* 5791 */     catch (IOException iox) {
/*      */       
/* 5793 */       logger.log(Level.INFO, "Failed to add " + player.getName() + " to settlement " + getName() + "." + iox
/* 5794 */           .getMessage(), iox);
/* 5795 */       player.getCommunicator().sendNormalServerMessage("Failed to add you to the settlement. Please contact administration.");
/*      */     
/*      */     }
/* 5798 */     catch (NoSuchRoleException nsr) {
/*      */       
/* 5800 */       logger.log(Level.INFO, "Failed to add " + player.getName() + " to settlement " + getName() + "." + nsr
/* 5801 */           .getMessage(), (Throwable)nsr);
/* 5802 */       player.getCommunicator().sendNormalServerMessage("Failed to add you to the settlement. Please contact administration.");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5807 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final boolean addVillageRecruitee(VillageRecruitee recruitee) {
/* 5812 */     if (recruiteeExists(recruitee)) {
/* 5813 */       return false;
/*      */     }
/* 5815 */     return this.recruitees.add(recruitee);
/*      */   }
/*      */ 
/*      */   
/*      */   private final VillageRecruitee getRecruiteeById(long wid) {
/* 5820 */     for (Iterator<VillageRecruitee> it = this.recruitees.iterator(); it.hasNext(); ) {
/*      */       
/* 5822 */       VillageRecruitee vr = it.next();
/* 5823 */       if (vr.getRecruiteeId() == wid) {
/* 5824 */         return vr;
/*      */       }
/*      */     } 
/* 5827 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean recruiteeExists(VillageRecruitee recruitee) {
/* 5832 */     for (Iterator<VillageRecruitee> it = this.recruitees.iterator(); it.hasNext(); ) {
/*      */       
/* 5834 */       VillageRecruitee vr = it.next();
/* 5835 */       if (vr.getRecruiteeId() == recruitee.getRecruiteeId()) {
/* 5836 */         return true;
/*      */       }
/*      */     } 
/* 5839 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean addVillageMapAnnotation(MapAnnotation annotation, boolean send) {
/* 5844 */     if (this.villageMapAnnotations.size() < 500) {
/*      */       
/* 5846 */       this.villageMapAnnotations.add(annotation);
/* 5847 */       if (send)
/*      */       {
/* 5849 */         sendMapAnnotationsToVillagers(new MapAnnotation[] { annotation });
/*      */       }
/*      */       
/* 5852 */       return true;
/*      */     } 
/* 5854 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeVillageMapAnnotation(MapAnnotation annotation) {
/* 5859 */     if (this.villageMapAnnotations.contains(annotation)) {
/*      */       
/* 5861 */       this.villageMapAnnotations.remove(annotation);
/*      */       
/*      */       try {
/* 5864 */         MapAnnotation.deleteAnnotation(annotation.getId());
/* 5865 */         sendRemoveMapAnnotationToVillagers(annotation);
/*      */       }
/* 5867 */       catch (IOException iex) {
/*      */         
/* 5869 */         logger.log(Level.WARNING, "Error when deleting annotation: " + annotation
/* 5870 */             .getId() + " : " + iex.getMessage(), iex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Set<MapAnnotation> getVillageMapAnnotations() {
/* 5878 */     return this.villageMapAnnotations;
/*      */   }
/*      */ 
/*      */   
/*      */   public final MapAnnotation[] getVillageMapAnnotationsArray() {
/* 5883 */     if (this.villageMapAnnotations == null || this.villageMapAnnotations.size() == 0)
/* 5884 */       return null; 
/* 5885 */     MapAnnotation[] annotations = new MapAnnotation[this.villageMapAnnotations.size()];
/* 5886 */     this.villageMapAnnotations.toArray(annotations);
/* 5887 */     return annotations;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendMapAnnotationsToVillagers(MapAnnotation[] annotations) {
/* 5892 */     if (this.group != null && annotations != null) {
/* 5893 */       this.group.sendMapAnnotation(annotations);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendRemoveMapAnnotationToVillagers(MapAnnotation annotation) {
/* 5898 */     if (this.group != null) {
/* 5899 */       this.group.sendRemoveMapAnnotation(annotation);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendClearMapAnnotationsOfType(byte type) {
/* 5904 */     if (this.group != null) {
/* 5905 */       this.group.sendClearMapAnnotationsOfType(type);
/*      */     }
/*      */   }
/*      */   
/*      */   public final long getCreationDate() {
/* 5910 */     return this.creationDate;
/*      */   }
/*      */ 
/*      */   
/*      */   private short[] calcOutsideSpawn() {
/* 5915 */     logger.info("Calculating outside spawn for " + getName());
/*      */     
/* 5917 */     boolean surfaced = isOnSurface();
/*      */ 
/*      */ 
/*      */     
/* 5921 */     if (Zones.isGoodTileForSpawn(getStartX() - 5, getStartY() - 5, surfaced)) {
/* 5922 */       return new short[] { (short)(getStartX() - 5), (short)(getStartY() - 5) };
/*      */     }
/* 5924 */     if (Zones.isGoodTileForSpawn(getStartX() - 5, getStartY() - 5, surfaced)) {
/* 5925 */       return new short[] { (short)(getEndX() + 5), (short)(getStartY() - 5) };
/*      */     }
/* 5927 */     if (Zones.isGoodTileForSpawn(getEndX() + 5, getStartY() - 5, surfaced)) {
/* 5928 */       return new short[] { (short)(getEndX() + 5), (short)(getEndY() + 5) };
/*      */     }
/* 5930 */     if (Zones.isGoodTileForSpawn(getStartX() - 5, getStartY() - 5, surfaced)) {
/* 5931 */       return new short[] { (short)(getStartX() - 5), (short)(getEndY() + 5) };
/*      */     }
/*      */     
/* 5934 */     int tilex = getStartX() - 5;
/* 5935 */     int tiley = getStartY() - 5;
/* 5936 */     for (int i1 = 1; i1 < 20; i1++) {
/*      */       
/* 5938 */       if (Zones.isGoodTileForSpawn(tilex - i1, tiley, surfaced))
/* 5939 */         return new short[] { (short)(tilex - i1), (short)tiley }; 
/*      */     } 
/* 5941 */     for (int n = 1; n < 20; n++) {
/*      */       
/* 5943 */       if (Zones.isGoodTileForSpawn(tilex, tiley - n, surfaced)) {
/* 5944 */         return new short[] { (short)tilex, (short)(tiley - n) };
/*      */       }
/*      */     } 
/*      */     
/* 5948 */     tilex = getEndX() + 5;
/* 5949 */     tiley = getEndY() + 5;
/* 5950 */     for (int m = 1; m < 20; m++) {
/*      */       
/* 5952 */       if (Zones.isGoodTileForSpawn(tilex + m, tiley, surfaced))
/* 5953 */         return new short[] { (short)(tilex + m), (short)tiley }; 
/*      */     } 
/* 5955 */     for (int k = 1; k < 20; k++) {
/*      */       
/* 5957 */       if (Zones.isGoodTileForSpawn(tilex, tiley + k, surfaced)) {
/* 5958 */         return new short[] { (short)tilex, (short)(tiley + k) };
/*      */       }
/*      */     } 
/*      */     
/* 5962 */     tilex = getEndX() + 5;
/* 5963 */     tiley = getStartY() - 5;
/* 5964 */     for (int j = 1; j < 20; j++) {
/*      */       
/* 5966 */       if (Zones.isGoodTileForSpawn(tilex + j, tiley, surfaced))
/* 5967 */         return new short[] { (short)(tilex + j), (short)tiley }; 
/*      */     } 
/* 5969 */     for (int i = 1; i < 20; i++) {
/*      */       
/* 5971 */       if (Zones.isGoodTileForSpawn(tilex, tiley - i, surfaced)) {
/* 5972 */         return new short[] { (short)tilex, (short)(tiley - i) };
/*      */       }
/*      */     } 
/*      */     
/* 5976 */     tilex = getStartX() - 5;
/* 5977 */     tiley = getEndY() + 5;
/* 5978 */     for (int x = 1; x < 20; x++) {
/*      */       
/* 5980 */       if (Zones.isGoodTileForSpawn(tilex - x, tiley, surfaced))
/* 5981 */         return new short[] { (short)(tilex - x), (short)tiley }; 
/*      */     } 
/* 5983 */     for (int y = 1; y < 20; y++) {
/*      */       
/* 5985 */       if (Zones.isGoodTileForSpawn(tilex, tiley + y, surfaced))
/* 5986 */         return new short[] { (short)tilex, (short)(tiley + y) }; 
/*      */     } 
/* 5988 */     return new short[] { -1, -1 };
/*      */   }
/*      */ 
/*      */   
/*      */   public short[] getOutsideSpawn() {
/* 5993 */     if (this.outsideSpawn == null || !Zones.isGoodTileForSpawn(this.outsideSpawn[0], this.outsideSpawn[1], isOnSurface())) {
/*      */       
/* 5995 */       this.outsideSpawn = calcOutsideSpawn();
/* 5996 */       if (!Zones.isGoodTileForSpawn(this.outsideSpawn[0], this.outsideSpawn[1], isOnSurface()))
/* 5997 */         logger.warning("Could not find outside spawn point for " + getName()); 
/*      */     } 
/* 5999 */     return this.outsideSpawn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHighway() {
/* 6005 */     for (Item marker : Items.getMarkers()) {
/*      */       
/* 6007 */       if (coversPlus(marker.getTileX(), marker.getTileY(), 2))
/*      */       {
/* 6009 */         return true;
/*      */       }
/*      */     } 
/* 6012 */     return false;
/*      */   }
/*      */   
/*      */   abstract int create() throws IOException;
/*      */   
/*      */   abstract void delete() throws IOException;
/*      */   
/*      */   abstract void save();
/*      */   
/*      */   abstract void loadCitizens();
/*      */   
/*      */   abstract void loadVillageMapAnnotations();
/*      */   
/*      */   abstract void loadVillageRecruitees();
/*      */   
/*      */   abstract void deleteVillageMapAnnotations();
/*      */   
/*      */   public abstract void setMayor(String paramString) throws IOException;
/*      */   
/*      */   public abstract void setDisbandTime(long paramLong) throws IOException;
/*      */   
/*      */   public abstract void setLogin();
/*      */   
/*      */   public abstract void setDisbander(long paramLong) throws IOException;
/*      */   
/*      */   public abstract void setName(String paramString) throws IOException;
/*      */   
/*      */   abstract void setStartX(int paramInt) throws IOException;
/*      */   
/*      */   abstract void setEndX(int paramInt) throws IOException;
/*      */   
/*      */   abstract void setStartY(int paramInt) throws IOException;
/*      */   
/*      */   abstract void setEndY(int paramInt) throws IOException;
/*      */   
/*      */   public abstract void setDemocracy(boolean paramBoolean) throws IOException;
/*      */   
/*      */   abstract void setDeedId(long paramLong) throws IOException;
/*      */   
/*      */   public abstract void setTokenId(long paramLong) throws IOException;
/*      */   
/*      */   abstract void loadRoles();
/*      */   
/*      */   abstract void loadGuards();
/*      */   
/*      */   abstract void loadReputations();
/*      */   
/*      */   public abstract void setMotto(String paramString) throws IOException;
/*      */   
/*      */   abstract void setUpkeep(long paramLong) throws IOException;
/*      */   
/*      */   public abstract void setUnlimitedCitizens(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMotd(String paramString) throws IOException;
/*      */   
/*      */   public abstract void saveSettings() throws IOException;
/*      */   
/*      */   abstract void loadHistory();
/*      */   
/*      */   public abstract void addHistory(String paramString1, String paramString2);
/*      */   
/*      */   abstract void saveRecruitee(VillageRecruitee paramVillageRecruitee);
/*      */   
/*      */   abstract void setMaxcitizens(int paramInt);
/*      */   
/*      */   public abstract void setAcceptsMerchants(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setAllowsAggroCreatures(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setPerimeter(int paramInt) throws IOException;
/*      */   
/*      */   public abstract void setKingdom(byte paramByte) throws IOException;
/*      */   
/*      */   public abstract void setKingdom(byte paramByte, boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setTwitCredentials(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2);
/*      */   
/*      */   public abstract void setFaithCreate(float paramFloat);
/*      */   
/*      */   public abstract void setFaithWar(float paramFloat);
/*      */   
/*      */   public abstract void setFaithHeal(float paramFloat);
/*      */   
/*      */   public abstract void setSpawnSituation(byte paramByte);
/*      */   
/*      */   public abstract void setAllianceNumber(int paramInt);
/*      */   
/*      */   public abstract void setHotaWins(short paramShort);
/*      */   
/*      */   public abstract void setLastChangedName(long paramLong);
/*      */   
/*      */   public abstract void setVillageRep(int paramInt);
/*      */   
/*      */   abstract void deleteRecruitee(VillageRecruitee paramVillageRecruitee);
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Village.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */