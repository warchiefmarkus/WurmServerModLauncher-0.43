/*       */ package com.wurmonline.server.creatures;
/*       */ 
/*       */ import com.wurmonline.math.TilePos;
/*       */ import com.wurmonline.math.Vector2f;
/*       */ import com.wurmonline.math.Vector3f;
/*       */ import com.wurmonline.mesh.GrassData;
/*       */ import com.wurmonline.mesh.MeshIO;
/*       */ import com.wurmonline.mesh.Tiles;
/*       */ import com.wurmonline.server.Constants;
/*       */ import com.wurmonline.server.FailedException;
/*       */ import com.wurmonline.server.Features;
/*       */ import com.wurmonline.server.HistoryManager;
/*       */ import com.wurmonline.server.Items;
/*       */ import com.wurmonline.server.LoginHandler;
/*       */ import com.wurmonline.server.Message;
/*       */ import com.wurmonline.server.MiscConstants;
/*       */ import com.wurmonline.server.NoSuchItemException;
/*       */ import com.wurmonline.server.NoSuchPlayerException;
/*       */ import com.wurmonline.server.Players;
/*       */ import com.wurmonline.server.PlonkData;
/*       */ import com.wurmonline.server.Server;
/*       */ import com.wurmonline.server.ServerEntry;
/*       */ import com.wurmonline.server.Servers;
/*       */ import com.wurmonline.server.Team;
/*       */ import com.wurmonline.server.TimeConstants;
/*       */ import com.wurmonline.server.WurmCalendar;
/*       */ import com.wurmonline.server.WurmId;
/*       */ import com.wurmonline.server.behaviours.Action;
/*       */ import com.wurmonline.server.behaviours.ActionStack;
/*       */ import com.wurmonline.server.behaviours.Actions;
/*       */ import com.wurmonline.server.behaviours.Behaviour;
/*       */ import com.wurmonline.server.behaviours.BehaviourDispatcher;
/*       */ import com.wurmonline.server.behaviours.Behaviours;
/*       */ import com.wurmonline.server.behaviours.FishEnums;
/*       */ import com.wurmonline.server.behaviours.MethodsItems;
/*       */ import com.wurmonline.server.behaviours.MethodsStructure;
/*       */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*       */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*       */ import com.wurmonline.server.behaviours.Seat;
/*       */ import com.wurmonline.server.behaviours.Terraforming;
/*       */ import com.wurmonline.server.behaviours.TileFieldBehaviour;
/*       */ import com.wurmonline.server.behaviours.TileRockBehaviour;
/*       */ import com.wurmonline.server.behaviours.Vehicle;
/*       */ import com.wurmonline.server.behaviours.Vehicles;
/*       */ import com.wurmonline.server.bodys.Body;
/*       */ import com.wurmonline.server.bodys.Wound;
/*       */ import com.wurmonline.server.bodys.Wounds;
/*       */ import com.wurmonline.server.combat.ArmourTemplate;
/*       */ import com.wurmonline.server.combat.Attack;
/*       */ import com.wurmonline.server.combat.Battle;
/*       */ import com.wurmonline.server.combat.BattleEvent;
/*       */ import com.wurmonline.server.combat.Battles;
/*       */ import com.wurmonline.server.combat.CombatConstants;
/*       */ import com.wurmonline.server.combat.CombatEngine;
/*       */ import com.wurmonline.server.combat.SpecialMove;
/*       */ import com.wurmonline.server.combat.Weapon;
/*       */ import com.wurmonline.server.creatures.ai.CreatureAIData;
/*       */ import com.wurmonline.server.creatures.ai.CreaturePathFinder;
/*       */ import com.wurmonline.server.creatures.ai.CreaturePathFinderAgg;
/*       */ import com.wurmonline.server.creatures.ai.CreaturePathFinderNPC;
/*       */ import com.wurmonline.server.creatures.ai.DecisionStack;
/*       */ import com.wurmonline.server.creatures.ai.NoPathException;
/*       */ import com.wurmonline.server.creatures.ai.Order;
/*       */ import com.wurmonline.server.creatures.ai.Path;
/*       */ import com.wurmonline.server.creatures.ai.PathFinder;
/*       */ import com.wurmonline.server.creatures.ai.PathTile;
/*       */ import com.wurmonline.server.creatures.ai.scripts.FishAI;
/*       */ import com.wurmonline.server.deities.Deities;
/*       */ import com.wurmonline.server.deities.Deity;
/*       */ import com.wurmonline.server.economy.Economy;
/*       */ import com.wurmonline.server.economy.MonetaryConstants;
/*       */ import com.wurmonline.server.economy.Shop;
/*       */ import com.wurmonline.server.effects.Effect;
/*       */ import com.wurmonline.server.endgames.EndGameItems;
/*       */ import com.wurmonline.server.epic.EpicMission;
/*       */ import com.wurmonline.server.epic.EpicMissionEnum;
/*       */ import com.wurmonline.server.epic.EpicServerStatus;
/*       */ import com.wurmonline.server.highways.Route;
/*       */ import com.wurmonline.server.intra.MountTransfer;
/*       */ import com.wurmonline.server.items.Item;
/*       */ import com.wurmonline.server.items.ItemFactory;
/*       */ import com.wurmonline.server.items.ItemSettings;
/*       */ import com.wurmonline.server.items.ItemSpellEffects;
/*       */ import com.wurmonline.server.items.ItemTemplate;
/*       */ import com.wurmonline.server.items.ItemTemplateFactory;
/*       */ import com.wurmonline.server.items.ItemTypes;
/*       */ import com.wurmonline.server.items.NoSpaceException;
/*       */ import com.wurmonline.server.items.NoSuchTemplateException;
/*       */ import com.wurmonline.server.items.Possessions;
/*       */ import com.wurmonline.server.items.Recipe;
/*       */ import com.wurmonline.server.items.Recipes;
/*       */ import com.wurmonline.server.items.Trade;
/*       */ import com.wurmonline.server.kingdom.Appointments;
/*       */ import com.wurmonline.server.kingdom.GuardTower;
/*       */ import com.wurmonline.server.kingdom.King;
/*       */ import com.wurmonline.server.kingdom.Kingdom;
/*       */ import com.wurmonline.server.kingdom.Kingdoms;
/*       */ import com.wurmonline.server.loot.LootTable;
/*       */ import com.wurmonline.server.modifiers.DoubleValueModifier;
/*       */ import com.wurmonline.server.modifiers.ModifierTypes;
/*       */ import com.wurmonline.server.players.Abilities;
/*       */ import com.wurmonline.server.players.Achievements;
/*       */ import com.wurmonline.server.players.Cultist;
/*       */ import com.wurmonline.server.players.ItemBonus;
/*       */ import com.wurmonline.server.players.KingdomIp;
/*       */ import com.wurmonline.server.players.MovementEntity;
/*       */ import com.wurmonline.server.players.MusicPlayer;
/*       */ import com.wurmonline.server.players.PermissionsByPlayer;
/*       */ import com.wurmonline.server.players.PermissionsPlayerList;
/*       */ import com.wurmonline.server.players.Player;
/*       */ import com.wurmonline.server.players.PlayerInfo;
/*       */ import com.wurmonline.server.players.PlayerInfoFactory;
/*       */ import com.wurmonline.server.players.PlayerKills;
/*       */ import com.wurmonline.server.players.SpellResistance;
/*       */ import com.wurmonline.server.players.Titles;
/*       */ import com.wurmonline.server.questions.SimplePopup;
/*       */ import com.wurmonline.server.questions.TestQuestion;
/*       */ import com.wurmonline.server.questions.TraderManagementQuestion;
/*       */ import com.wurmonline.server.skills.NoSuchSkillException;
/*       */ import com.wurmonline.server.skills.Skill;
/*       */ import com.wurmonline.server.skills.SkillSystem;
/*       */ import com.wurmonline.server.skills.Skills;
/*       */ import com.wurmonline.server.skills.SkillsFactory;
/*       */ import com.wurmonline.server.sounds.SoundPlayer;
/*       */ import com.wurmonline.server.spells.SpellEffect;
/*       */ import com.wurmonline.server.spells.SpellResist;
/*       */ import com.wurmonline.server.spells.Spells;
/*       */ import com.wurmonline.server.structures.Blocker;
/*       */ import com.wurmonline.server.structures.Blocking;
/*       */ import com.wurmonline.server.structures.BlockingResult;
/*       */ import com.wurmonline.server.structures.BridgePart;
/*       */ import com.wurmonline.server.structures.Door;
/*       */ import com.wurmonline.server.structures.Fence;
/*       */ import com.wurmonline.server.structures.Floor;
/*       */ import com.wurmonline.server.structures.NoSuchStructureException;
/*       */ import com.wurmonline.server.structures.NoSuchWallException;
/*       */ import com.wurmonline.server.structures.Structure;
/*       */ import com.wurmonline.server.structures.Structures;
/*       */ import com.wurmonline.server.structures.Wall;
/*       */ import com.wurmonline.server.tutorial.MissionPerformed;
/*       */ import com.wurmonline.server.tutorial.MissionPerformer;
/*       */ import com.wurmonline.server.tutorial.MissionTrigger;
/*       */ import com.wurmonline.server.tutorial.MissionTriggers;
/*       */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*       */ import com.wurmonline.server.utils.CreatureLineSegment;
/*       */ import com.wurmonline.server.utils.StringUtil;
/*       */ import com.wurmonline.server.utils.logging.TileEvent;
/*       */ import com.wurmonline.server.villages.Citizen;
/*       */ import com.wurmonline.server.villages.Guard;
/*       */ import com.wurmonline.server.villages.NoSuchVillageException;
/*       */ import com.wurmonline.server.villages.Village;
/*       */ import com.wurmonline.server.villages.VillageRole;
/*       */ import com.wurmonline.server.villages.Villages;
/*       */ import com.wurmonline.server.weather.Weather;
/*       */ import com.wurmonline.server.webinterface.WcEpicKarmaCommand;
/*       */ import com.wurmonline.server.webinterface.WcKillCommand;
/*       */ import com.wurmonline.server.webinterface.WcTrelloDeaths;
/*       */ import com.wurmonline.server.zones.Den;
/*       */ import com.wurmonline.server.zones.Dens;
/*       */ import com.wurmonline.server.zones.FaithZone;
/*       */ import com.wurmonline.server.zones.FocusZone;
/*       */ import com.wurmonline.server.zones.HiveZone;
/*       */ import com.wurmonline.server.zones.NoSuchZoneException;
/*       */ import com.wurmonline.server.zones.Trap;
/*       */ import com.wurmonline.server.zones.VirtualZone;
/*       */ import com.wurmonline.server.zones.VolaTile;
/*       */ import com.wurmonline.server.zones.Zone;
/*       */ import com.wurmonline.server.zones.Zones;
/*       */ import com.wurmonline.shared.constants.AttitudeConstants;
/*       */ import com.wurmonline.shared.constants.CounterTypes;
/*       */ import com.wurmonline.shared.constants.CreatureTypes;
/*       */ import com.wurmonline.shared.constants.ProtoConstants;
/*       */ import com.wurmonline.shared.exceptions.WurmServerException;
/*       */ import com.wurmonline.shared.util.MovementChecker;
/*       */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*       */ import com.wurmonline.shared.util.StringUtilities;
/*       */ import java.io.IOException;
/*       */ import java.util.ArrayList;
/*       */ import java.util.HashMap;
/*       */ import java.util.HashSet;
/*       */ import java.util.Iterator;
/*       */ import java.util.LinkedList;
/*       */ import java.util.List;
/*       */ import java.util.ListIterator;
/*       */ import java.util.Map;
/*       */ import java.util.Optional;
/*       */ import java.util.Set;
/*       */ import java.util.StringTokenizer;
/*       */ import java.util.concurrent.ConcurrentHashMap;
/*       */ import java.util.logging.Level;
/*       */ import java.util.logging.Logger;
/*       */ import javax.annotation.Nonnull;
/*       */ import javax.annotation.Nullable;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ public class Creature
/*       */   implements ItemTypes, CounterTypes, MiscConstants, CreatureTypes, TimeConstants, ProtoConstants, CombatConstants, ModifierTypes, CreatureTemplateIds, MonetaryConstants, AttitudeConstants, PermissionsPlayerList.ISettings, Comparable<Creature>
/*       */ {
/*       */   protected Skills skills;
/*   231 */   private int respawnCounter = 0;
/*       */   
/*       */   private static final int NPCRESPAWN = 600;
/*       */   protected CreatureStatus status;
/*   235 */   protected HashMap<Integer, SpellResist> spellResistances = new HashMap<>();
/*       */   
/*       */   private long id;
/*       */   
/*       */   private static final double skillLost = 0.25D;
/*       */   public static final double MAX_LEAD_DEPTH = -0.71D;
/*   241 */   public long loggerCreature1 = -10L;
/*       */   
/*   243 */   private long loggerCreature2 = -10L;
/*       */   
/*   245 */   public int combatRound = 0;
/*       */   
/*   247 */   public SpecialMove specialMove = null;
/*       */   
/*       */   protected boolean isVehicleCommander = false;
/*       */   
/*       */   public Creature lastOpponent;
/*       */   
/*   253 */   public int opponentCounter = 0;
/*       */   
/*       */   protected boolean _enterVehicle = false;
/*       */   
/*   257 */   protected MountAction mountAction = null;
/*       */   
/*       */   public boolean addingAfterTeleport = false;
/*       */   
/*   261 */   private static final Item[] emptyItems = new Item[0];
/*       */   
/*   263 */   protected long linkedTo = -10L;
/*       */   
/*       */   private boolean isInDuelRing = false;
/*       */   
/*   267 */   private static final DoubleValueModifier willowMod = new DoubleValueModifier(-0.15000000596046448D);
/*       */   
/*       */   public boolean shouldStandStill = false;
/*       */   
/*   271 */   public byte opportunityAttackCounter = 0;
/*       */   
/*   273 */   private long lastSentToolbelt = 0L;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected static final float submergedMinDepth = -5.0F;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   285 */   protected static final Logger logger = Logger.getLogger(Creature.class.getName());
/*       */   
/*       */   protected CreatureTemplate template;
/*       */   
/*   289 */   protected Vehicle hitchedTo = null;
/*       */   
/*       */   protected MusicPlayer musicPlayer;
/*       */   
/*       */   private boolean inHostilePerimeter = false;
/*       */   
/*   295 */   private int hugeMoveCounter = 0;
/*       */ 
/*       */ 
/*       */   
/*   299 */   protected String name = "Noname";
/*       */   
/*   301 */   protected String petName = "";
/*       */   
/*       */   protected Possessions possessions;
/*       */   
/*       */   protected Communicator communicator;
/*       */   
/*       */   private VisionArea visionArea;
/*       */   
/*       */   private final Behaviour behaviour;
/*       */   
/*       */   protected ActionStack actions;
/*       */   
/*       */   private Structure structure;
/*       */   
/*       */   public int numattackers;
/*       */   
/*       */   public int numattackerslast;
/*       */   
/*       */   protected Map<Long, Long> attackers;
/*       */   
/*   321 */   public Creature opponent = null;
/*       */   
/*   323 */   private Set<Long> riders = null;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private Set<Item> keys;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   333 */   protected byte fightlevel = 0;
/*       */ 
/*       */   
/*       */   protected boolean guest = false;
/*       */ 
/*       */   
/*       */   protected boolean isTeleporting = false;
/*       */ 
/*       */   
/*   342 */   private long startTeleportTime = Long.MIN_VALUE;
/*       */   
/*       */   public boolean faithful = true;
/*       */   
/*   346 */   private Door currentDoor = null;
/*       */   
/*   348 */   private float teleportX = -1.0F;
/*   349 */   private float teleportY = -1.0F;
/*       */   
/*   351 */   protected int teleportLayer = 0;
/*   352 */   protected int teleportFloorLevel = 0;
/*       */   
/*       */   protected boolean justSpawned = false;
/*   355 */   public String spawnWeapon = "";
/*   356 */   public String spawnArmour = "";
/*       */ 
/*       */   
/*       */   private LinkedList<int[]> openedTiles;
/*       */   
/*   361 */   private int carriedWeight = 0;
/*       */   
/*       */   private static final float DEGS_TO_RADS = 0.017453292F;
/*       */   
/*       */   private TradeHandler tradeHandler;
/*       */   
/*       */   public Village citizenVillage;
/*       */   
/*       */   public Village currentVillage;
/*       */   
/*   371 */   private Set<Item> itemsTaken = null;
/*       */   
/*   373 */   private Set<Item> itemsDropped = null;
/*       */   
/*       */   protected MovementScheme movementScheme;
/*       */   
/*   377 */   protected Battle battle = null;
/*       */   
/*   379 */   private Set<Long> stealthBreakers = null;
/*       */   
/*       */   private Set<DoubleValueModifier> visionModifiers;
/*       */   
/*   383 */   private final ConcurrentHashMap<Item, Float> weaponsUsed = new ConcurrentHashMap<>();
/*       */   
/*   385 */   private final ConcurrentHashMap<AttackAction, UsedAttackData> attackUsed = new ConcurrentHashMap<>();
/*       */   
/*   387 */   public long lastSavedPos = System.currentTimeMillis() - Server.rand.nextInt(1800000);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   393 */   protected byte guardSecondsLeft = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   398 */   private byte fightStyle = 2;
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean milked = false;
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean sheared = false;
/*       */ 
/*       */   
/*       */   private boolean isRiftSummoned = false;
/*       */ 
/*       */   
/*   412 */   public long target = -10L;
/*       */   
/*   414 */   public Creature leader = null;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   419 */   public long dominator = -10L;
/*       */   
/*   421 */   public float zoneBonus = 0.0F;
/*       */   
/*   423 */   private byte currentDeity = 0;
/*       */   
/*   425 */   public byte fleeCounter = 0;
/*       */   
/*       */   public boolean isLit = false;
/*       */   
/*   429 */   private int encumbered = 70000;
/*       */   
/*   431 */   private int moveslow = 40000;
/*       */   
/*   433 */   private int cantmove = 140000;
/*       */   
/*   435 */   private byte tilesMoved = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   441 */   private byte pathfindcounter = 0;
/*       */   
/*   443 */   protected Map<Creature, Item> followers = null;
/*       */   
/*   445 */   protected static final Creature[] emptyCreatures = new Creature[0];
/*       */   
/*   447 */   public byte currentKingdom = 0;
/*       */   
/*   449 */   protected short damageCounter = 0;
/*       */   
/*   451 */   private final DoubleValueModifier woundMoveMod = new DoubleValueModifier(7, -0.25D);
/*       */   
/*   453 */   public long lastParry = 0L;
/*       */   
/*       */   public VolaTile currentTile;
/*       */   
/*   457 */   public int staminaPollCounter = 0;
/*       */   
/*   459 */   private DecisionStack decisions = null;
/*       */ 
/*       */   
/*       */   private static final float HUNGER_RANGE = 20535.0F;
/*       */ 
/*       */   
/*       */   public boolean goOffline = false;
/*       */ 
/*       */   
/*   468 */   private Item bestLightsource = null;
/*       */   
/*   470 */   private Item bestCompass = null;
/*       */   
/*   472 */   private Item bestToolbelt = null;
/*       */   
/*   474 */   private Item bestBeeSmoker = null;
/*       */   
/*   476 */   private Item bestTackleBox = null;
/*       */   
/*       */   public boolean lightSourceChanged = false;
/*       */   
/*       */   public boolean lastSentHasCompass = false;
/*       */   
/*   482 */   private CombatHandler combatHandler = null;
/*       */   
/*   484 */   private int pollCounter = 0;
/*       */   
/*       */   private static final int secondsBetweenItemPolls = 10800;
/*       */   private static final int secondsBetweenTraderCoolingPolls = 600;
/*   488 */   private int heatCheckTick = 0;
/*       */ 
/*       */   
/*   491 */   private int mountPollCounter = 10;
/*       */   
/*   493 */   protected int breedCounter = 0;
/*       */ 
/*       */   
/*       */   private boolean visibleToPlayers = false;
/*       */   
/*       */   private boolean forcedBreed = false;
/*       */   
/*       */   private boolean hasSpiritStamina = false;
/*       */   
/*       */   protected boolean hasSpiritFavorgain = false;
/*       */   
/*       */   public boolean hasAddedToAttack = false;
/*       */   
/*   506 */   private static final long LOG_ELAPSED_TIME_THRESHOLD = Constants.lagThreshold;
/*       */ 
/*       */   
/*       */   private static final boolean DO_MORE_ELAPSED_TIME_MEASUREMENTS = false;
/*       */   
/*       */   protected boolean hasSentPoison = false;
/*       */   
/*   513 */   int pathRecalcLength = 0;
/*       */   
/*       */   protected boolean isInPvPZone = false;
/*       */   
/*       */   protected boolean isInNonPvPZone = false;
/*       */   
/*       */   protected boolean isInFogZone = false;
/*   520 */   private static final Set<Long> pantLess = new HashSet<>();
/*       */   
/*   522 */   private static final Map<Long, Set<MovementEntity>> illusions = new ConcurrentHashMap<>();
/*       */   
/*       */   protected boolean isInOwnBattleCamp = false;
/*       */   
/*       */   private boolean doLavaDamage = false;
/*       */   private boolean doAreaDamage = false;
/*   528 */   protected float webArmourModTime = 0.0F;
/*       */ 
/*       */   
/*       */   private ArrayList<Effect> effects;
/*       */ 
/*       */   
/*       */   private ServerEntry destination;
/*       */   
/*   536 */   private static CreaturePathFinder pathFinder = new CreaturePathFinder();
/*   537 */   private static CreaturePathFinderAgg pathFinderAgg = new CreaturePathFinderAgg();
/*   538 */   private static CreaturePathFinderNPC pathFinderNPC = new CreaturePathFinderNPC();
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   544 */   public long vehicle = -10L;
/*   545 */   protected byte seatType = -1;
/*   546 */   protected int teleports = 0;
/*   547 */   private long lastWaystoneChecked = -10L;
/*       */   
/*       */   private boolean checkedHotItemsAfterLogin = false;
/*       */   
/*       */   private boolean ignoreSaddleDamage = false;
/*       */   
/*       */   private boolean isPlacingItem = false;
/*       */   
/*   555 */   private Item placementItem = null;
/*   556 */   private float[] pendingPlacement = null;
/*       */ 
/*       */   
/*   559 */   private GuardTower guardTower = null;
/*       */   private int lastSecond;
/*       */   
/*       */   static {
/*   563 */     pathFinder.startRunning();
/*   564 */     pathFinderAgg.startRunning();
/*   565 */     pathFinderNPC.startRunning();
/*       */   }
/*       */ 
/*       */   
/*       */   public static void shutDownPathFinders() {
/*   570 */     pathFinder.shutDown();
/*   571 */     pathFinderAgg.shutDown();
/*   572 */     pathFinderNPC.shutDown();
/*       */   }
/*       */ 
/*       */   
/*       */   public static final CreaturePathFinder getPF() {
/*   577 */     return pathFinder;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final CreaturePathFinderAgg getPFA() {
/*   582 */     return pathFinderAgg;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final CreaturePathFinderNPC getPFNPC() {
/*   587 */     return pathFinderNPC;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void checkTrap() {
/*   608 */     if (!isDead()) {
/*       */       
/*   610 */       Trap trap = Trap.getTrap(this.currentTile.tilex, this.currentTile.tiley, getLayer());
/*   611 */       if (getPower() >= 3) {
/*       */         
/*   613 */         if (trap != null) {
/*   614 */           getCommunicator().sendNormalServerMessage("A " + trap.getName() + " is here.");
/*       */         
/*       */         }
/*       */       }
/*   618 */       else if (trap != null) {
/*       */         
/*   620 */         boolean trigger = false;
/*   621 */         if (trap.getKingdom() != getKingdomId()) {
/*       */           
/*   623 */           if (getKingdomId() == 0 && !isAggHuman()) {
/*       */             
/*   625 */             trigger = false;
/*   626 */             if (this.riders != null && this.riders.size() > 0)
/*       */             {
/*   628 */               for (Long rider : this.riders)
/*       */               {
/*       */                 
/*       */                 try {
/*   632 */                   Creature rr = Server.getInstance().getCreature(rider.longValue());
/*   633 */                   if (rr.getKingdomId() != trap.getKingdom()) {
/*   634 */                     trigger = true;
/*       */                   }
/*   636 */                 } catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */                 
/*       */                 }
/*   640 */                 catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */               
/*       */               }
/*       */             
/*       */             }
/*       */           }
/*       */           else {
/*       */             
/*   648 */             trigger = true;
/*       */           } 
/*   650 */         } else if (trap.getVillage() > 0) {
/*       */ 
/*       */           
/*       */           try {
/*   654 */             Village vill = Villages.getVillage(trap.getVillage());
/*   655 */             if (vill.isEnemy(this)) {
/*   656 */               trigger = true;
/*       */             }
/*   658 */           } catch (NoSuchVillageException noSuchVillageException) {}
/*       */         } 
/*       */ 
/*       */ 
/*       */         
/*   663 */         if (trigger)
/*       */         {
/*   665 */           trap.doEffect(this, this.currentTile.tilex, this.currentTile.tiley, getLayer());
/*       */         }
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendDetectTrap(Trap trap) {
/*   674 */     if (trap != null && Server.rand.nextInt(100) < getDetectDangerBonus()) {
/*   675 */       getCommunicator().sendAlertServerMessage("TRAP!", (byte)4);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void calculateFloorLevel(VolaTile tile, boolean forceAddFloorLayer) {
/*   686 */     calculateFloorLevel(tile, forceAddFloorLayer, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void calculateFloorLevel(VolaTile tile, boolean forceAddFloorLayer, boolean wasOnBridge) {
/*       */     try {
/*   701 */       if (tile.getStructure() != null && tile.getStructure().isTypeHouse()) {
/*       */         
/*   703 */         if (getFloorLevel() == 0 && !wasOnBridge) {
/*       */           
/*   705 */           if (!isPlayer()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   725 */             float oldposz = getPositionZ();
/*       */             
/*   727 */             if (oldposz >= -1.25D) {
/*       */ 
/*       */               
/*   730 */               float newPosz = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface()) + (((tile.getFloors(-10, 10)).length == 0) ? 0.0F : 0.25F);
/*   731 */               float diffz = newPosz - oldposz;
/*   732 */               setPositionZ(newPosz);
/*   733 */               if (this.currentTile != null && getVisionArea() != null) {
/*   734 */                 moved(0.0F, 0.0F, diffz, 0, 0);
/*       */               }
/*       */             } 
/*       */           } 
/*       */         } else {
/*       */           
/*   740 */           int targetFloorLevel = tile.getDropFloorLevel(getFloorLevel());
/*   741 */           if (targetFloorLevel != getFloorLevel()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   752 */             if (!isPlayer())
/*       */             {
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*   758 */               pushToFloorLevel(targetFloorLevel);
/*       */             }
/*       */           }
/*   761 */           else if (forceAddFloorLayer) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   771 */             if (!isPlayer()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*   777 */               float oldposz = getPositionZ();
/*       */               
/*   779 */               float newPosz = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface()) + (((tile.getFloors(-10, 10)).length == 0) ? 0.0F : 0.25F);
/*   780 */               float diffz = newPosz - oldposz;
/*   781 */               setPositionZ(newPosz);
/*   782 */               if (this.currentTile != null && getVisionArea() != null) {
/*   783 */                 moved(0.0F, 0.0F, diffz, 0, 0);
/*       */               }
/*       */             } 
/*       */           } 
/*       */         } 
/*   788 */       } else if (tile.getStructure() == null || !tile.getStructure().isTypeBridge()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*   797 */         if (getFloorLevel() >= 0)
/*       */         {
/*   799 */           if (!isPlayer()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*   808 */             float oldposz = getPositionZ();
/*       */             
/*   810 */             if (oldposz >= 0.0F) {
/*       */               
/*   812 */               float newPosz = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface());
/*   813 */               float diffz = newPosz - oldposz;
/*       */               
/*   815 */               setPositionZ(newPosz);
/*   816 */               if (this.currentTile != null && getVisionArea() != null) {
/*   817 */                 moved(0.0F, 0.0F, diffz, 0, 0);
/*       */               }
/*       */             } 
/*       */           } 
/*       */         }
/*       */       } 
/*   823 */     } catch (NoSuchZoneException noSuchZoneException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int compareTo(Creature otherCreature) {
/*   835 */     return getName().compareTo(otherCreature.getName());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean setNewTile(@Nullable VolaTile newtile, float diffZ, boolean ignoreBridge) {
/*   840 */     if (newtile != null && (getTileX() != newtile.tilex || getTileY() != newtile.tiley)) {
/*       */       
/*   842 */       logger.log(Level.WARNING, getName() + " set to " + newtile.tilex + "," + newtile.tiley + " but at " + getTileX() + "," + 
/*   843 */           getTileY(), new Exception());
/*   844 */       if (this.currentTile != null) {
/*       */         
/*   846 */         logger.log(Level.WARNING, "old is " + this.currentTile.tilex + "(" + getPosX() + "), " + this.currentTile.tiley + "(" + 
/*   847 */             getPosY() + "), vehic=" + getVehicle());
/*   848 */         if (isPlayer())
/*       */         {
/*   850 */           ((Player)this).intraTeleport(((this.currentTile.tilex << 2) + 2), ((this.currentTile.tiley << 2) + 2), getPositionZ(), 
/*   851 */               getStatus().getRotation(), getLayer(), "on wrong tile");
/*       */         }
/*       */       } 
/*   854 */       return false;
/*       */     } 
/*   856 */     boolean wasInDuelRing = false;
/*   857 */     Set<FocusZone> oldFocusZones = null;
/*   858 */     HiveZone oldHiveZone = null;
/*   859 */     boolean oldHiveClose = false;
/*   860 */     long oldBridgeId = getBridgeId();
/*   861 */     if (this.currentTile != null) {
/*       */       
/*   863 */       if (isPlayer()) {
/*       */         
/*   865 */         Item ring = Zones.isWithinDuelRing(this.currentTile.tilex, this.currentTile.tiley, this.currentTile.isOnSurface());
/*   866 */         if (ring != null)
/*   867 */           wasInDuelRing = true; 
/*   868 */         oldFocusZones = FocusZone.getZonesAt(this.currentTile.tilex, this.currentTile.tiley);
/*   869 */         oldHiveZone = Zones.getHiveZoneAt(this.currentTile.tilex, this.currentTile.tiley, this.currentTile.isOnSurface());
/*   870 */         if (oldHiveZone != null)
/*   871 */           oldHiveClose = oldHiveZone.isClose(this.currentTile.tilex, this.currentTile.tiley); 
/*       */       } 
/*   873 */       if (newtile != null && !isDead()) {
/*       */ 
/*       */         
/*   876 */         this.currentTile.checkOpportunityAttacks(this);
/*   877 */         if (this.currentTile != null) {
/*       */           
/*   879 */           int diffX = newtile.tilex - this.currentTile.tilex;
/*   880 */           int diffY = newtile.tiley - this.currentTile.tiley;
/*       */ 
/*       */           
/*   883 */           if (diffX != 0)
/*   884 */             sendDetectTrap(Trap.getTrap(newtile.tilex + diffX, newtile.tiley, getLayer())); 
/*   885 */           if (diffY != 0) {
/*   886 */             sendDetectTrap(Trap.getTrap(newtile.tilex, newtile.tiley + diffY, getLayer()));
/*       */           }
/*       */           
/*   889 */           if (diffY != 0 && diffX != 0) {
/*   890 */             sendDetectTrap(Trap.getTrap(newtile.tilex + diffX, newtile.tiley + diffY, getLayer()));
/*   891 */           } else if (diffX != 0) {
/*       */             
/*   893 */             sendDetectTrap(Trap.getTrap(newtile.tilex + diffX, newtile.tiley - 1, getLayer()));
/*   894 */             sendDetectTrap(Trap.getTrap(newtile.tilex + diffX, newtile.tiley + 1, getLayer()));
/*       */           }
/*   896 */           else if (diffY != 0) {
/*       */             
/*   898 */             sendDetectTrap(Trap.getTrap(newtile.tilex + 1, newtile.tiley + diffY, getLayer()));
/*   899 */             sendDetectTrap(Trap.getTrap(newtile.tilex - 1, newtile.tiley + diffY, getLayer()));
/*       */           } 
/*   901 */           if (this.currentTile != newtile)
/*   902 */             this.currentTile.removeCreature(this); 
/*       */         } 
/*   904 */         if (isPlayer()) {
/*   905 */           addTileMoved();
/*       */         }
/*       */       } else {
/*       */         
/*   909 */         this.currentTile.removeCreature(this);
/*       */       } 
/*   911 */       if (this.currentTile != null && isPlayer() && this.currentTile != newtile)
/*       */       {
/*       */         
/*   914 */         if (this.openedTiles != null) {
/*       */           
/*   916 */           ListIterator<int[]> openedIterator = (ListIterator)this.openedTiles.listIterator();
/*   917 */           while (openedIterator.hasNext()) {
/*       */             
/*   919 */             int[] opened = openedIterator.next();
/*   920 */             if (newtile == null || opened[0] != newtile.getTileX() || opened[1] != newtile
/*   921 */               .getTileY()) {
/*       */               
/*       */               try {
/*   924 */                 getCommunicator().sendTileDoor((short)opened[0], (short)opened[1], false);
/*   925 */                 openedIterator.remove();
/*       */                 
/*   927 */                 MineDoorPermission md = MineDoorPermission.getPermission((short)opened[0], (short)opened[1]);
/*   928 */                 if (md != null)
/*       */                 {
/*   930 */                   md.close(this);
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*   935 */               catch (IOException iOException) {}
/*       */             }
/*       */           } 
/*       */           
/*   939 */           if (this.openedTiles.isEmpty()) {
/*   940 */             this.openedTiles = null;
/*       */           }
/*       */         } 
/*       */       }
/*       */       
/*   945 */       if (this.currentTile != null && newtile != null) {
/*       */ 
/*       */         
/*   948 */         this.currentTile = newtile;
/*   949 */         checkTrap();
/*   950 */         if (isDead())
/*   951 */           return false; 
/*   952 */         if (!isPlayer() && !ignoreBridge) {
/*   953 */           checkBridgeMove(this.currentTile, newtile, diffZ);
/*       */         }
/*   955 */       } else if (newtile != null && !ignoreBridge) {
/*       */         
/*   957 */         if (!isPlayer()) {
/*   958 */           checkBridgeMove(null, newtile, diffZ);
/*       */         }
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/*   964 */     this.currentTile = newtile;
/*   965 */     if (this.currentTile != null) {
/*       */       
/*   967 */       if (!isRidden()) {
/*       */         
/*   969 */         boolean wasOnBridge = false;
/*   970 */         if (oldBridgeId != -10L)
/*       */         {
/*   972 */           if (oldBridgeId != getBridgeId())
/*       */           {
/*   974 */             wasOnBridge = true;
/*       */           }
/*       */         }
/*   977 */         calculateFloorLevel(this.currentTile, false, wasOnBridge);
/*       */       } 
/*   979 */       Set<FocusZone> newFocusZones = FocusZone.getZonesAt(this.currentTile.tilex, this.currentTile.tiley);
/*       */       
/*   981 */       if (!isPlayer()) {
/*       */         
/*   983 */         this.isInPvPZone = false;
/*   984 */         this.isInNonPvPZone = false;
/*   985 */         for (FocusZone fz : newFocusZones) {
/*       */           
/*   987 */           if (fz.isPvP()) {
/*       */             
/*   989 */             this.isInPvPZone = true;
/*       */             break;
/*       */           } 
/*   992 */           if (fz.isNonPvP()) {
/*       */             
/*   994 */             this.isInNonPvPZone = true;
/*       */             break;
/*       */           } 
/*       */         } 
/*   998 */         this.tilesMoved = (byte)(this.tilesMoved + 1);
/*   999 */         if (this.tilesMoved >= 10) {
/*       */           
/*  1001 */           if (isDominated() || isHorse()) {
/*       */             
/*       */             try {
/*  1004 */               savePosition(this.currentTile.getZone().getId());
/*       */             }
/*  1006 */             catch (IOException iOException) {}
/*       */           }
/*       */ 
/*       */           
/*  1010 */           this.tilesMoved = 0;
/*       */         } 
/*       */       } 
/*  1013 */       if (isPlayer()) {
/*       */ 
/*       */         
/*       */         try {
/*  1017 */           savePosition(this.currentTile.getZone().getId());
/*       */         }
/*  1019 */         catch (IOException iOException) {}
/*       */ 
/*       */ 
/*       */         
/*  1023 */         for (FocusZone fz : newFocusZones) {
/*       */           
/*  1025 */           if (fz.isFog())
/*       */           {
/*  1027 */             if (!this.isInFogZone) {
/*       */               
/*  1029 */               this.isInFogZone = true;
/*  1030 */               getCommunicator().sendSpecificWeather(0.85F);
/*       */             } 
/*       */           }
/*  1033 */           if (fz.isPvP()) {
/*       */             
/*  1035 */             if (!this.isInPvPZone) {
/*       */               
/*  1037 */               if (!isOnPvPServer()) {
/*       */                 
/*  1039 */                 achievement(4);
/*  1040 */                 getCommunicator().sendAlertServerMessage("You enter the " + fz
/*  1041 */                     .getName() + " PvP area. Other players may attack you here.", (byte)4);
/*       */               } else {
/*       */                 
/*  1044 */                 getCommunicator().sendAlertServerMessage("You enter the " + fz.getName() + " area.", (byte)4);
/*  1045 */               }  sendAttitudeChange();
/*       */             } 
/*  1047 */             this.isInPvPZone = true;
/*       */             break;
/*       */           } 
/*  1050 */           if (fz.isNonPvP()) {
/*       */             
/*  1052 */             if (!this.isInNonPvPZone) {
/*       */ 
/*       */               
/*  1055 */               if (isOnPvPServer()) {
/*  1056 */                 getCommunicator().sendSafeServerMessage("You enter the " + fz
/*  1057 */                     .getName() + " No-PvP area. Other players may no longer attack you here.", (byte)2);
/*       */               } else {
/*       */                 
/*  1060 */                 getCommunicator().sendSafeServerMessage("You enter the " + fz.getName() + " No-PvP area.", (byte)2);
/*  1061 */               }  sendAttitudeChange();
/*       */             } 
/*  1063 */             this.isInNonPvPZone = true;
/*       */             break;
/*       */           } 
/*  1066 */           if (fz.isName() || fz.isNamePopup() || fz.isNoBuild() || fz.isPremSpawnOnly())
/*       */           {
/*  1068 */             if (oldFocusZones == null || !oldFocusZones.contains(fz)) {
/*       */               
/*  1070 */               if (fz.isName() || fz.isNoBuild() || fz.isPremSpawnOnly()) {
/*       */                 
/*  1072 */                 getCommunicator().sendSafeServerMessage("You enter the " + fz.getName() + " area.", (byte)2);
/*       */                 
/*       */                 continue;
/*       */               } 
/*       */               
/*  1077 */               SimplePopup sp = new SimplePopup(this, "Entering " + fz.getName(), "You enter the " + fz.getName() + " area.", fz.getDescription());
/*  1078 */               sp.sendQuestion();
/*       */             } 
/*       */           }
/*       */         } 
/*       */         
/*  1083 */         if (oldFocusZones != null)
/*       */         {
/*  1085 */           for (FocusZone fz : oldFocusZones) {
/*       */             
/*  1087 */             if (fz.isFog())
/*       */             {
/*  1089 */               if (newFocusZones == null || !newFocusZones.contains(fz)) {
/*       */                 
/*  1091 */                 this.isInFogZone = false;
/*  1092 */                 getCommunicator().checkSendWeather();
/*       */               } 
/*       */             }
/*  1095 */             if (fz.isPvP()) {
/*       */               
/*  1097 */               if (newFocusZones == null || !newFocusZones.contains(fz)) {
/*       */                 
/*  1099 */                 this.isInPvPZone = false;
/*       */                 
/*  1101 */                 if (isOnPvPServer()) {
/*  1102 */                   getCommunicator().sendSafeServerMessage("You leave the " + fz.getName() + " area.", (byte)2);
/*       */                 } else {
/*  1104 */                   getCommunicator().sendSafeServerMessage("You leave the " + fz.getName() + " PvP area.", (byte)2);
/*  1105 */                 }  sendAttitudeChange();
/*       */               }  continue;
/*       */             } 
/*  1108 */             if (fz.isNonPvP()) {
/*       */               
/*  1110 */               if (newFocusZones == null || !newFocusZones.contains(fz)) {
/*       */                 
/*  1112 */                 this.isInNonPvPZone = false;
/*  1113 */                 sendAttitudeChange();
/*  1114 */                 if (isOnPvPServer()) {
/*  1115 */                   getCommunicator().sendAlertServerMessage("You leave the " + fz
/*  1116 */                       .getName() + " No-PvP area. Other players may attack you here.", (byte)2);
/*       */                   continue;
/*       */                 } 
/*  1119 */                 getCommunicator().sendAlertServerMessage("You leave the " + fz.getName() + " No-PvP area.", (byte)2);
/*       */               }  continue;
/*       */             } 
/*  1122 */             if (fz.isName() || fz.isNamePopup() || fz.isNoBuild() || fz.isPremSpawnOnly())
/*       */             {
/*  1124 */               if (newFocusZones == null || !newFocusZones.contains(fz)) {
/*       */                 
/*  1126 */                 if (fz.isName() || fz.isNoBuild() || fz.isPremSpawnOnly()) {
/*       */                   
/*  1128 */                   getCommunicator().sendSafeServerMessage("You leave the " + fz.getName() + " area.", (byte)2);
/*       */                   
/*       */                   continue;
/*       */                 } 
/*       */                 
/*  1133 */                 SimplePopup sp = new SimplePopup(this, "Leaving " + fz.getName(), "You leave the " + fz.getName() + " area.");
/*  1134 */                 sp.sendQuestion();
/*       */               } 
/*       */             }
/*       */           } 
/*       */         }
/*       */ 
/*       */         
/*  1141 */         if (!WurmCalendar.isSeasonWinter()) {
/*       */           
/*  1143 */           HiveZone newHiveZone = Zones.getHiveZoneAt(this.currentTile.tilex, this.currentTile.tiley, isOnSurface());
/*  1144 */           boolean newHiveClose = (newHiveZone == null) ? false : newHiveZone.isClose(this.currentTile.tilex, this.currentTile.tiley);
/*  1145 */           boolean domestic = (newHiveZone == null) ? false : ((newHiveZone.getCurrentHive().getTemplateId() == 1175));
/*  1146 */           if (oldHiveClose && !newHiveClose) {
/*  1147 */             getCommunicator().sendSafeServerMessage("The sounds of bees decreases as you move further away from the hive.", domestic ? 0 : 2);
/*       */           }
/*  1149 */           if (oldHiveZone == null && newHiveZone != null) {
/*  1150 */             getCommunicator().sendSafeServerMessage("You hear bees, maybe you are getting close to a hive.", domestic ? 0 : 2);
/*       */           }
/*  1152 */           else if (oldHiveZone != null && newHiveZone == null) {
/*  1153 */             getCommunicator().sendSafeServerMessage("The sounds of bees disappears in the distance.", 
/*  1154 */                 (oldHiveZone.getCurrentHive().getTemplateId() == 1175) ? 0 : 2);
/*  1155 */           }  if (!oldHiveClose && newHiveClose)
/*       */           {
/*  1157 */             if (newHiveZone.getCurrentHive().hasTwoQueens()) {
/*  1158 */               getCommunicator().sendSafeServerMessage("The bees noise is getting louder, sounds like there is unusual activity in the hive.", domestic ? 0 : 2);
/*       */             } else {
/*       */               
/*  1161 */               getCommunicator().sendSafeServerMessage("The bees noise is getting louder, maybe you are getting closer to their hive.", domestic ? 0 : 2);
/*       */             } 
/*       */           }
/*       */         } 
/*  1165 */         this.isInDuelRing = false;
/*  1166 */         Item ring = Zones.isWithinDuelRing(this.currentTile.tilex, this.currentTile.tiley, this.currentTile.isOnSurface());
/*  1167 */         if (ring != null) {
/*       */           
/*  1169 */           Kingdom k = Kingdoms.getKingdom(ring.getAuxData());
/*  1170 */           if (k != null)
/*       */           {
/*  1172 */             if (ring.getAuxData() == getKingdomId())
/*  1173 */               this.isInDuelRing = true; 
/*  1174 */             if (!wasInDuelRing)
/*       */             {
/*  1176 */               getCommunicator().sendAlertServerMessage("You enter the duelling area of " + k.getName() + ".", (byte)4);
/*  1177 */               if (this.isInDuelRing) {
/*  1178 */                 getCommunicator().sendAlertServerMessage("People from your own kingdom may slay you here without penalty.", (byte)4);
/*       */               }
/*       */             }
/*       */           
/*       */           }
/*       */         
/*       */         }
/*  1185 */         else if (wasInDuelRing) {
/*  1186 */           getCommunicator().sendSafeServerMessage("You leave the duelling area.", (byte)2);
/*       */         } 
/*  1188 */         if (!Servers.localServer.HOMESERVER)
/*       */         {
/*  1190 */           if (isOnSurface())
/*       */           {
/*  1192 */             if (getFaith() > 0.0F && Server.rand.nextInt(100) < getFaith())
/*       */             {
/*  1194 */               if (EndGameItems.getArtifactAtTile(this.currentTile.tilex, this.currentTile.tiley) != null)
/*       */               {
/*  1196 */                 if (getDeity() != null)
/*       */                 {
/*  1198 */                   getCommunicator().sendSafeServerMessage(
/*  1199 */                       (getDeity()).name + " urges you to deeply investigate the area!");
/*       */                 }
/*       */               }
/*       */             }
/*       */           }
/*       */         }
/*       */       } 
/*  1206 */       if (isPlayer() && !this.currentTile.isTransition)
/*       */       {
/*  1208 */         if (getVisionArea() != null && getVisionArea().isInitialized())
/*       */         {
/*  1210 */           checkOpenMineDoor();
/*       */         }
/*       */       }
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  1228 */     if (this.currentTile != null) {
/*       */       
/*  1230 */       checkInvisDetection();
/*  1231 */       boolean hostilePerimeter = false;
/*  1232 */       if (isPlayer()) {
/*       */         
/*  1234 */         Village lVill = Villages.getVillageWithPerimeterAt(getTileX(), getTileY(), true);
/*  1235 */         if (lVill != null)
/*       */         {
/*  1237 */           if (lVill.kingdom == getKingdomId() && lVill.isEnemy(this)) {
/*       */             
/*  1239 */             if (!this.inHostilePerimeter)
/*       */             {
/*  1241 */               getCommunicator().sendAlertServerMessage("You are now within the hostile perimeter of " + lVill
/*  1242 */                   .getName() + " and will be attacked by kingdom guards.", (byte)4);
/*       */             }
/*       */             
/*  1245 */             hostilePerimeter = true;
/*       */           } 
/*       */         }
/*       */       } 
/*  1249 */       if (!hostilePerimeter && this.inHostilePerimeter) {
/*       */         
/*  1251 */         getCommunicator().sendSafeServerMessage("You are now outside the hostile perimeters.");
/*  1252 */         this.inHostilePerimeter = false;
/*       */       } 
/*  1254 */       if (hostilePerimeter) {
/*  1255 */         this.inHostilePerimeter = true;
/*       */       }
/*  1257 */       if (isPlayer())
/*       */       {
/*  1259 */         MissionTriggers.activateTriggerPlate(this, this.currentTile.tilex, this.currentTile.tiley, getLayer());
/*       */       }
/*       */     } 
/*  1262 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInOwnDuelRing() {
/*  1267 */     return this.isInDuelRing;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasOpenedMineDoor(int tilex, int tiley) {
/*  1272 */     if (this.openedTiles == null)
/*  1273 */       return false; 
/*  1274 */     for (int[] openedTile : this.openedTiles) {
/*       */       
/*  1276 */       if (openedTile[0] == tilex && openedTile[1] == tiley)
/*  1277 */         return true; 
/*       */     } 
/*  1279 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void checkOpenMineDoor() {
/*  1284 */     if (this.currentTile != null) {
/*       */       
/*  1286 */       Set<int[]> oldM = Terraforming.getAllMineDoors(this.currentTile.tilex, this.currentTile.tiley);
/*  1287 */       if (oldM != null)
/*       */       {
/*  1289 */         for (int[] checkedTile : oldM) {
/*       */           
/*  1291 */           if (!hasOpenedMineDoor(checkedTile[0], checkedTile[1])) {
/*       */             
/*       */             try {
/*  1294 */               boolean ok = false;
/*  1295 */               MineDoorPermission md = MineDoorPermission.getPermission(checkedTile[0], checkedTile[1]);
/*  1296 */               if (md != null)
/*  1297 */                 if (md.mayPass(this)) {
/*       */                   
/*  1299 */                   ok = true;
/*  1300 */                   if (isPlayer())
/*       */                   {
/*  1302 */                     VolaTile tile = Zones.getOrCreateTile(checkedTile[0], checkedTile[1], true);
/*       */                     
/*  1304 */                     if (getEnemyPresense() > 0)
/*       */                     {
/*  1306 */                       if (tile == null || tile.getVillage() == null) {
/*  1307 */                         md.setClosingTime(System.currentTimeMillis() + (
/*  1308 */                             Servers.isThisAChaosServer() ? 30000L : 120000L));
/*       */                       }
/*       */                     }
/*       */                   }
/*       */                 
/*  1313 */                 } else if (md.isWideOpen()) {
/*  1314 */                   ok = true;
/*  1315 */                 }   if (ok)
/*       */               {
/*       */                 
/*  1318 */                 if (this.openedTiles == null)
/*  1319 */                   this.openedTiles = (LinkedList)new LinkedList<>(); 
/*  1320 */                 this.openedTiles.add(checkedTile);
/*  1321 */                 getMovementScheme().touchFreeMoveCounter();
/*  1322 */                 getVisionArea().checkCaves(false);
/*  1323 */                 getCommunicator().sendTileDoor((short)checkedTile[0], (short)checkedTile[1], true);
/*  1324 */                 md.open(this);
/*       */               }
/*       */             
/*  1327 */             } catch (IOException iOException) {}
/*       */           }
/*       */         } 
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature(CreatureTemplate aTemplate) throws Exception {
/*  1343 */     this();
/*  1344 */     this.template = aTemplate;
/*  1345 */     getMovementScheme().initalizeModifiersWithTemplate();
/*  1346 */     this.name = aTemplate.getName();
/*  1347 */     this.skills = aTemplate.getSkills();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getBestLightsource() {
/*  1356 */     return this.bestLightsource;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getBestCompass() {
/*  1365 */     return this.bestCompass;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getBestToolbelt() {
/*  1370 */     return this.bestToolbelt;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getBestBeeSmoker() {
/*  1375 */     return this.bestBeeSmoker;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getBestTackleBox() {
/*  1380 */     return this.bestTackleBox;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setBestLightsource(@Nullable Item item, boolean override) {
/*  1390 */     if (override || (getVisionArea() != null && getVisionArea().isInitialized())) {
/*       */       
/*  1392 */       this.bestLightsource = item;
/*  1393 */       this.lightSourceChanged = true;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setBestCompass(Item item) {
/*  1404 */     this.bestCompass = item;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setBestToolbelt(@Nullable Item item) {
/*  1413 */     this.bestToolbelt = item;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setBestBeeSmoker(Item item) {
/*  1418 */     this.bestBeeSmoker = item;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setBestTackleBox(Item item) {
/*  1423 */     this.bestTackleBox = item;
/*       */   }
/*       */ 
/*       */   
/*       */   public void resetCompassLantern() {
/*  1428 */     this.bestCompass = null;
/*  1429 */     this.bestToolbelt = null;
/*  1430 */     if (this.bestLightsource != null && (!this.bestLightsource.isOnFire() || this.bestLightsource.getOwnerId() != getWurmId())) {
/*       */       
/*  1432 */       this.bestLightsource = null;
/*  1433 */       this.lightSourceChanged = true;
/*       */     } 
/*  1435 */     this.bestBeeSmoker = null;
/*  1436 */     this.bestTackleBox = null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void pollToolbelt() {
/*  1445 */     if (this.bestToolbelt != null && this.lastSentToolbelt != this.bestToolbelt.getWurmId()) {
/*       */       
/*  1447 */       getCommunicator().sendToolbelt(this.bestToolbelt);
/*  1448 */       this.lastSentToolbelt = this.bestToolbelt.getWurmId();
/*       */     }
/*  1450 */     else if (this.bestToolbelt == null && this.lastSentToolbelt != 0L) {
/*       */       
/*  1452 */       getCommunicator().sendToolbelt(this.bestToolbelt);
/*  1453 */       this.lastSentToolbelt = 0L;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void resetLastSentToolbelt() {
/*  1459 */     this.lastSentToolbelt = 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void pollCompassLantern() {
/*  1466 */     if (!this.lastSentHasCompass) {
/*       */       
/*  1468 */       if (this.bestCompass != null)
/*       */       {
/*  1470 */         getCommunicator().sendCompass(this.bestCompass);
/*  1471 */         this.lastSentHasCompass = true;
/*       */       }
/*       */     
/*  1474 */     } else if (this.bestCompass == null) {
/*       */       
/*  1476 */       getCommunicator().sendCompass(this.bestCompass);
/*  1477 */       this.lastSentHasCompass = false;
/*       */     } 
/*       */     
/*  1480 */     pollToolbelt();
/*       */     
/*  1482 */     if (this.lightSourceChanged == true) {
/*       */       
/*  1484 */       if (this.bestLightsource != null) {
/*       */         
/*  1486 */         if (getCurrentTile() != null)
/*       */         {
/*  1488 */           getCurrentTile().setHasLightSource(this, this.bestLightsource);
/*       */ 
/*       */         
/*       */         }
/*       */       
/*       */       }
/*  1494 */       else if (this.bestLightsource == null) {
/*       */         
/*  1496 */         if (getCurrentTile() != null) {
/*       */           
/*  1498 */           getCurrentTile().setHasLightSource(this, null);
/*  1499 */           this.isLit = false;
/*       */         } 
/*       */       } 
/*  1502 */       this.lightSourceChanged = false;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void mute(boolean mute, String reason, long expiry) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isMute() {
/*  1527 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasSleepBonus() {
/*  1537 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setOpponent(@Nullable Creature _opponent) {
/*  1543 */     if (_opponent != null && this.target == -10L)
/*       */     {
/*  1545 */       if (!isPrey()) {
/*  1546 */         setTarget(_opponent.getWurmId(), true);
/*       */       }
/*       */     }
/*  1549 */     if (_opponent != null && _opponent.getAttackers() >= _opponent.getMaxGroupAttackSize())
/*       */     {
/*  1551 */       if (!_opponent.isPlayer() || isPlayer()) {
/*       */         return;
/*       */       }
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  1561 */     if (this.opponent != _opponent)
/*       */     {
/*  1563 */       if (_opponent != null && isPlayer() && _opponent.isPlayer()) {
/*       */         
/*  1565 */         this.battle = Battles.getBattleFor(this, _opponent);
/*  1566 */         this.battle.addEvent(new BattleEvent((short)-1, getName(), _opponent.getName()));
/*       */       } 
/*       */     }
/*  1569 */     this.opponent = _opponent;
/*  1570 */     if (this.opponent != null) {
/*       */       
/*  1572 */       this.opponent.getCommunicator().changeAttitude(getWurmId(), getAttitude(this.opponent));
/*  1573 */       if (!this.opponent.equals(this.lastOpponent)) {
/*       */         
/*  1575 */         resetWeaponsUsed();
/*  1576 */         resetAttackUsed();
/*  1577 */         getCombatHandler().setCurrentStance(-1, (byte)0);
/*  1578 */         this.lastOpponent = this.opponent;
/*  1579 */         this.combatRound = 0;
/*  1580 */         if (isPlayer() && this.opponent.isPlayer()) {
/*       */           
/*  1582 */           if (this.opponent.getKingdomId() != getKingdomId() && getKingdomId() != 0) {
/*       */             
/*  1584 */             Kingdom k = Kingdoms.getKingdom(getKingdomId());
/*  1585 */             k.lastConfrontationTileX = getTileX();
/*  1586 */             k.lastConfrontationTileY = getTileY();
/*       */           } 
/*       */           
/*  1589 */           if (getDeity() != null)
/*       */           {
/*  1591 */             (getDeity()).lastConfrontationTileX = getTileX();
/*  1592 */             (getDeity()).lastConfrontationTileY = getTileY();
/*       */           }
/*       */         
/*       */         } 
/*       */       } 
/*       */     } else {
/*       */       
/*  1599 */       resetWeaponsUsed();
/*  1600 */       resetAttackUsed();
/*       */     } 
/*  1602 */     this.status.sendStateString();
/*  1603 */     if (isPlayer())
/*       */     {
/*  1605 */       if (this.opponent == null) {
/*       */         
/*  1607 */         getCommunicator().sendSpecialMove((short)-1, "N/A");
/*  1608 */         getCommunicator().sendCombatOptions(CombatHandler.NO_COMBAT_OPTIONS, (short)-1);
/*  1609 */         getCombatHandler().setSentAttacks(false);
/*       */       }
/*       */       else {
/*       */         
/*  1613 */         getCombatHandler().setSentAttacks(false);
/*  1614 */         getCombatHandler().calcAttacks(false);
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean mayRaiseFightLevel() {
/*  1621 */     if (this.combatRound > 2)
/*       */     {
/*  1623 */       if (this.fightlevel < 5) {
/*       */         
/*  1625 */         if (this.fightlevel == 0)
/*  1626 */           return true; 
/*  1627 */         if (this.fightlevel == 1)
/*  1628 */           return (getFightingSkill().getKnowledge(0.0D) > 30.0D); 
/*  1629 */         if (this.fightlevel == 2)
/*  1630 */           return (getBodyControl() > 25.0D); 
/*  1631 */         if (this.fightlevel == 3)
/*  1632 */           return (getMindSpeed().getKnowledge(0.0D) > 25.0D); 
/*  1633 */         if (this.fightlevel == 4)
/*  1634 */           return (getSoulDepth().getKnowledge(0.0D) > 25.0D); 
/*       */       } 
/*       */     }
/*  1637 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public CombatHandler getCombatHandler() {
/*  1646 */     if (this.combatHandler == null)
/*  1647 */       this.combatHandler = new CombatHandler(this); 
/*  1648 */     return this.combatHandler;
/*       */   }
/*       */ 
/*       */   
/*       */   public void removeTarget(long targetId) {
/*  1653 */     this.actions.removeTarget(targetId);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isPlayer() {
/*  1661 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isLegal() {
/*  1669 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLegal(boolean mode) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setAutofight(boolean mode) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setFaithMode(boolean mode) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getDraggedItem() {
/*  1698 */     return this.movementScheme.getDraggedItem();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setDraggedItem(@Nullable Item dragged) {
/*  1704 */     this.movementScheme.setDraggedItem(dragged);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Door getCurrentDoor() {
/*  1713 */     return this.currentDoor;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCurrentDoor(@Nullable Door door) {
/*  1723 */     this.currentDoor = door;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Battle getBattle() {
/*  1732 */     return this.battle;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setBattle(@Nullable Battle batle) {
/*  1742 */     this.battle = batle;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCitizenVillage(@Nullable Village newVillage) {
/*  1751 */     this.citizenVillage = newVillage;
/*  1752 */     if (this.citizenVillage != null) {
/*       */       
/*  1754 */       setVillageSkillModifier(this.citizenVillage.getSkillModifier());
/*  1755 */       if (this.citizenVillage.kingdom != getKingdomId()) {
/*       */         
/*       */         try {
/*       */           
/*  1759 */           setKingdomId(this.citizenVillage.kingdom, true);
/*       */         }
/*  1761 */         catch (IOException iOException) {}
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  1767 */       if (isPlayer()) {
/*  1768 */         ((Player)this).maybeTriggerAchievement(576, true);
/*       */       }
/*       */     } else {
/*  1771 */       setVillageSkillModifier(0.0D);
/*  1772 */     }  refreshAttitudes();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Village getCitizenVillage() {
/*  1781 */     return this.citizenVillage;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setFightingStyle(byte style) {
/*  1786 */     setFightingStyle(style, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setFightingStyle(byte style, boolean loading) {
/*  1791 */     String mess = "";
/*  1792 */     if (style == 2) {
/*  1793 */       mess = "You will now fight defensively.";
/*  1794 */     } else if (style == 1) {
/*  1795 */       mess = "You will now fight aggressively.";
/*       */     } else {
/*  1797 */       mess = "You will now fight normally.";
/*  1798 */     }  if (isFighting()) {
/*  1799 */       getCommunicator().sendCombatNormalMessage(mess);
/*       */     } else {
/*  1801 */       getCommunicator().sendNormalServerMessage(mess);
/*  1802 */     }  getCombatHandler().setFightingStyle(style);
/*  1803 */     this.fightStyle = style;
/*  1804 */     getCommunicator().sendFightStyle(this.fightStyle);
/*  1805 */     this.status.sendStateString();
/*       */     
/*  1807 */     if (!loading)
/*       */     {
/*  1809 */       saveFightMode(this.fightStyle);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void saveFightMode(byte mode) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getFightStyle() {
/*  1828 */     return this.fightStyle;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getBaseCombatRating() {
/*  1833 */     if (isPlayer()) {
/*  1834 */       return this.template.getBaseCombatRating();
/*       */     }
/*       */     
/*  1837 */     if (getLoyalty() > 0.0F)
/*  1838 */       return (isReborn() ? 0.7F : 0.5F) * this.template.getBaseCombatRating() * this.status.getBattleRatingTypeModifier(); 
/*  1839 */     return this.template.getBaseCombatRating() * this.status.getBattleRatingTypeModifier();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public float getBonusCombatRating() {
/*  1845 */     return this.template.getBonusCombatRating();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isOkToKillBy(Creature attacker) {
/*  1851 */     if (!Servers.localServer.HOMESERVER && !Servers.localServer.isChallengeServer())
/*       */     {
/*       */       
/*  1854 */       return true;
/*       */     }
/*  1856 */     if (!attacker.isFriendlyKingdom(getKingdomId()))
/*       */     {
/*       */       
/*  1859 */       return true;
/*       */     }
/*  1861 */     if (Servers.isThisAChaosServer())
/*       */     {
/*       */       
/*  1864 */       return true;
/*       */     }
/*  1866 */     if (getKingdomTemplateId() == 3)
/*       */     {
/*  1868 */       return true; } 
/*  1869 */     if (hasAttackedUnmotivated())
/*       */     {
/*  1871 */       return true; } 
/*  1872 */     if (attacker.isDuelOrSpar(this))
/*       */     {
/*  1874 */       return true; } 
/*  1875 */     if (getReputation() < 0)
/*       */     {
/*  1877 */       return true; } 
/*  1878 */     if (isInOwnDuelRing())
/*       */     {
/*  1880 */       return true; } 
/*  1881 */     if (Zones.isWithinDuelRing(getTileX(), getTileY(), true) != null)
/*  1882 */       return true; 
/*  1883 */     if (attacker.getCitizenVillage() != null) {
/*       */       
/*  1885 */       if (attacker.getCitizenVillage().isEnemy(getCitizenVillage()))
/*  1886 */         return true; 
/*  1887 */       if (Servers.localServer.PVPSERVER) {
/*       */         
/*  1889 */         Village v = Villages.getVillageWithPerimeterAt(attacker.getTileX(), attacker.getTileY(), true);
/*       */         
/*  1891 */         if (v == attacker.getCitizenVillage() && getCurrentVillage() == v)
/*       */         {
/*  1893 */           return true;
/*       */         }
/*  1895 */         if (attacker.getCitizenVillage().isEnemy(this))
/*  1896 */           return true; 
/*  1897 */         if (attacker.getCitizenVillage().isAlly(getCitizenVillage()))
/*  1898 */           return false; 
/*       */       } 
/*       */     } 
/*  1901 */     if (isInPvPZone())
/*  1902 */       return true; 
/*  1903 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEnemyOnChaos(Creature creature) {
/*  1908 */     if (Servers.isThisAChaosServer())
/*       */     {
/*  1910 */       if (isInSameAlliance(creature)) {
/*  1911 */         return false;
/*       */       }
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  1918 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInSameAlliance(Creature creature) {
/*  1923 */     if (getCitizenVillage() == null)
/*  1924 */       return false; 
/*  1925 */     if (creature.getCitizenVillage() == null)
/*  1926 */       return false; 
/*  1927 */     return (getCitizenVillage().getAllianceNumber() == creature.getCitizenVillage().getAllianceNumber());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasAttackedUnmotivated() {
/*  1932 */     if (isDominated())
/*       */     {
/*  1934 */       if (getDominator() != null)
/*  1935 */         return getDominator().hasAttackedUnmotivated(); 
/*       */     }
/*  1937 */     SpellEffects effs = getSpellEffects();
/*  1938 */     if (effs == null)
/*       */     {
/*  1940 */       return false;
/*       */     }
/*       */ 
/*       */     
/*  1944 */     SpellEffect eff = effs.getSpellEffect((byte)64);
/*  1945 */     if (eff != null)
/*       */     {
/*  1947 */       return true;
/*       */     }
/*       */     
/*  1950 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setUnmotivatedAttacker() {
/*  1973 */     if (isNpc())
/*       */       return; 
/*  1975 */     if (!Servers.isThisAPvpServer() || !Servers.localServer.HOMESERVER) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/*  1980 */     if (getKingdomTemplateId() != 3) {
/*       */       
/*  1982 */       SpellEffects effs = getSpellEffects();
/*  1983 */       if (effs == null)
/*       */       {
/*  1985 */         effs = createSpellEffects();
/*       */       }
/*  1987 */       SpellEffect eff = effs.getSpellEffect((byte)64);
/*  1988 */       if (eff == null) {
/*       */ 
/*       */         
/*  1991 */         setVisible(false);
/*  1992 */         logger.log(Level.INFO, getName() + " set unmotivated attacker at ", new Exception());
/*       */         
/*  1994 */         eff = new SpellEffect(getWurmId(), (byte)64, 100.0F, (int)(Servers.isThisATestServer() ? 120L : 1800L), (byte)1, (byte)1, true);
/*       */         
/*  1996 */         effs.addSpellEffect(eff);
/*  1997 */         setVisible(true);
/*  1998 */         getCommunicator()
/*  1999 */           .sendAlertServerMessage("You have received the hunted status and may be attacked without penalty for half an hour.");
/*       */         
/*  2001 */         if (getCitizenVillage() != null)
/*  2002 */           getCitizenVillage().setVillageRep(getCitizenVillage().getVillageReputation() + 10); 
/*  2003 */         Achievements ach = Achievements.getAchievementObject(getWurmId());
/*  2004 */         if (ach != null && ach.getAchievement(369) != null)
/*       */         {
/*  2006 */           achievement(373);
/*  2007 */           removeTitle(Titles.Title.Knigt);
/*  2008 */           addTitle(Titles.Title.FallenKnight);
/*       */         }
/*       */       
/*       */       }
/*       */       else {
/*       */         
/*  2014 */         eff.setTimeleft(1800);
/*  2015 */         sendUpdateSpellEffect(eff);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void addAttacker(Creature creature) {
/*  2022 */     if (!isDuelOrSpar(creature)) {
/*       */ 
/*       */       
/*  2025 */       if (isSpiritGuard() && getCitizenVillage() != null && !getCitizenVillage().containsTarget(creature)) {
/*  2026 */         getCitizenVillage().addTarget(creature);
/*       */       }
/*  2028 */       if (this.attackers == null) {
/*  2029 */         this.attackers = new HashMap<>();
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  2035 */       if (creature.isPlayer()) {
/*       */         
/*  2037 */         if (!isInvulnerable())
/*  2038 */           setSecondsToLogout(getSecondsToLogout()); 
/*  2039 */         if (isPlayer()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  2047 */           if (!isOkToKillBy(creature))
/*       */           {
/*  2049 */             if (!creature.hasBeenAttackedBy(getWurmId()))
/*       */             {
/*  2051 */               creature.setUnmotivatedAttacker();
/*       */             
/*       */             }
/*       */           
/*       */           }
/*       */         
/*       */         }
/*  2058 */         else if (isRidden()) {
/*       */           
/*  2060 */           if (creature.getCitizenVillage() == null || getCurrentVillage() != creature.getCitizenVillage())
/*       */           {
/*  2062 */             for (Long riderLong : getRiders())
/*       */             {
/*       */               
/*       */               try {
/*  2066 */                 Creature rider = Server.getInstance().getCreature(riderLong.longValue());
/*  2067 */                 if (rider != creature)
/*       */                 {
/*  2069 */                   if (!creature.hasBeenAttackedBy(rider.getWurmId()) && 
/*  2070 */                     !creature.hasBeenAttackedBy(getWurmId()))
/*       */                   {
/*  2072 */                     if (!rider.isOkToKillBy(creature))
/*  2073 */                       creature.setUnmotivatedAttacker(); 
/*       */                   }
/*  2075 */                   rider.addAttacker(creature);
/*       */                 }
/*       */               
/*  2078 */               } catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */               
/*       */               }
/*  2082 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */             
/*       */             }
/*       */ 
/*       */           
/*       */           }
/*       */         }
/*  2089 */         else if (getHitched() != null) {
/*       */           
/*  2091 */           if (Servers.localServer.HOMESERVER)
/*       */           {
/*  2093 */             if (creature.getCitizenVillage() == null || getCurrentVillage() != creature.getCitizenVillage())
/*       */             {
/*  2095 */               if (!getHitched().isCreature()) {
/*       */                 try
/*       */                 {
/*       */                   
/*  2099 */                   Item i = Items.getItem((getHitched()).wurmid);
/*  2100 */                   long ownid = i.getLastOwnerId();
/*       */                   
/*       */                   try {
/*  2103 */                     if (ownid != creature.getWurmId())
/*       */                     {
/*  2105 */                       byte kingd = Players.getInstance().getKingdomForPlayer(ownid);
/*  2106 */                       if (creature.isFriendlyKingdom(kingd) && 
/*  2107 */                         !creature.hasBeenAttackedBy(ownid))
/*       */                       {
/*  2109 */                         creature.setUnmotivatedAttacker();
/*       */                       
/*       */                       }
/*       */                     }
/*       */                   
/*       */                   }
/*  2115 */                   catch (Exception exception) {}
/*       */ 
/*       */ 
/*       */                 
/*       */                 }
/*  2120 */                 catch (NoSuchItemException nsi)
/*       */                 {
/*  2122 */                   logger.log(Level.INFO, (getHitched()).wurmid + " no such item:", (Throwable)nsi);
/*       */                 }
/*       */               
/*       */               }
/*       */             }
/*       */           }
/*  2128 */         } else if (isDominated()) {
/*       */           
/*  2130 */           if (Servers.localServer.HOMESERVER) {
/*       */             
/*  2132 */             this.attackers.put(Long.valueOf(creature.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*  2133 */             if (creature.isFriendlyKingdom(getKingdomId()) && 
/*  2134 */               !creature.hasBeenAttackedBy(this.dominator) && 
/*  2135 */               !creature.hasBeenAttackedBy(getWurmId()))
/*       */             {
/*  2137 */               if (creature != getDominator()) {
/*  2138 */                 creature.setUnmotivatedAttacker();
/*       */               }
/*       */             }
/*       */           } 
/*  2142 */         } else if (getCurrentVillage() != null) {
/*       */           
/*  2144 */           if (Servers.localServer.HOMESERVER) {
/*       */             
/*  2146 */             Brand brand = Creatures.getInstance().getBrand(getWurmId());
/*  2147 */             if (brand != null) {
/*       */               
/*       */               try {
/*       */                 
/*  2151 */                 Village villageBrand = Villages.getVillage((int)brand.getBrandId());
/*       */ 
/*       */                 
/*  2154 */                 if (getCurrentVillage() == villageBrand)
/*       */                 {
/*  2156 */                   if (creature.getCitizenVillage() != villageBrand)
/*       */                   {
/*  2158 */                     if (!villageBrand.isEnemy(creature.getCitizenVillage())) {
/*  2159 */                       creature.setUnmotivatedAttacker();
/*       */                     }
/*       */                   }
/*       */                 }
/*  2163 */               } catch (NoSuchVillageException nsv) {
/*       */                 
/*  2165 */                 brand.deleteBrand();
/*       */               } 
/*       */             }
/*       */           } 
/*       */         } 
/*  2170 */         if (!creature.hasAddedToAttack) {
/*  2171 */           this.attackers.put(Long.valueOf(creature.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*       */         
/*       */         }
/*       */       }
/*  2175 */       else if (!creature.hasAddedToAttack) {
/*  2176 */         this.attackers.put(Long.valueOf(creature.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*       */       } 
/*  2178 */       if (!creature.hasAddedToAttack) {
/*       */         
/*  2180 */         this.numattackers++;
/*  2181 */         creature.hasAddedToAttack = true;
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getSecondsToLogout() {
/*  2191 */     return 300;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasBeenAttackedBy(long _id) {
/*  2196 */     if (!isPlayer())
/*  2197 */       return false; 
/*  2198 */     if (this.attackers == null) {
/*  2199 */       return false;
/*       */     }
/*       */     
/*  2202 */     Long l = Long.valueOf(_id);
/*  2203 */     return this.attackers.keySet().contains(l);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public long[] getLatestAttackers() {
/*  2209 */     if (this.attackers != null && this.attackers.size() > 0) {
/*       */       
/*  2211 */       Long[] lKeys = (Long[])this.attackers.keySet().toArray((Object[])new Long[this.attackers.size()]);
/*  2212 */       long[] toReturn = new long[lKeys.length];
/*  2213 */       for (int x = 0; x < toReturn.length; x++)
/*       */       {
/*  2215 */         toReturn[x] = lKeys[x].longValue();
/*       */       }
/*  2217 */       return toReturn;
/*       */     } 
/*       */     
/*  2220 */     return EMPTY_LONG_PRIMITIVE_ARRAY;
/*       */   }
/*       */ 
/*       */   
/*       */   protected long[] getAttackerIds() {
/*  2225 */     if (this.attackers == null)
/*  2226 */       return EMPTY_LONG_PRIMITIVE_ARRAY; 
/*  2227 */     Long[] longs = (Long[])this.attackers.keySet().toArray((Object[])new Long[this.attackers.size()]);
/*  2228 */     long[] ll = new long[longs.length];
/*  2229 */     for (int x = 0; x < longs.length; x++)
/*       */     {
/*  2231 */       ll[x] = longs[x].longValue();
/*       */     }
/*  2233 */     return ll;
/*       */   }
/*       */ 
/*       */   
/*       */   public void trimAttackers(boolean delete) {
/*  2238 */     if (delete) {
/*  2239 */       this.attackers = null;
/*  2240 */     } else if (this.attackers != null && this.attackers.size() > 0) {
/*       */       
/*  2242 */       Long[] lKeys = (Long[])this.attackers.keySet().toArray((Object[])new Long[this.attackers.size()]);
/*  2243 */       for (Long lLKey : lKeys) {
/*       */         
/*  2245 */         Long time = this.attackers.get(lLKey);
/*  2246 */         if (WurmId.getType(lLKey.longValue()) == 1) {
/*       */           
/*  2248 */           if (System.currentTimeMillis() - time.longValue() > 180000L) {
/*  2249 */             this.attackers.remove(lLKey);
/*       */           }
/*  2251 */         } else if (System.currentTimeMillis() - time.longValue() > 300000L) {
/*       */           
/*  2253 */           this.attackers.remove(lLKey);
/*       */         } 
/*       */       } 
/*  2256 */       if (this.attackers.isEmpty()) {
/*  2257 */         this.attackers = null;
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setMilked(boolean aMilked) {
/*  2267 */     this.milked = aMilked;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setSheared(boolean isSheared) {
/*  2272 */     this.sheared = isSheared;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isMilked() {
/*  2281 */     return this.milked;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isSheared() {
/*  2286 */     return this.sheared;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getAttackers() {
/*  2291 */     return this.numattackers;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getLastAttackers() {
/*  2299 */     return this.numattackerslast;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasBeenAttackedWithin(int seconds) {
/*  2304 */     if (this.attackers != null)
/*       */     {
/*  2306 */       for (Long l : this.attackers.values()) {
/*       */         
/*  2308 */         if (System.currentTimeMillis() - l.longValue() < (seconds * 1000))
/*  2309 */           return true; 
/*       */       } 
/*       */     }
/*  2312 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCurrentVillage(Village newVillage) {
/*  2321 */     if (this.currentVillage == null) {
/*       */       
/*  2323 */       if (newVillage != null)
/*       */       {
/*  2325 */         getCommunicator().sendNormalServerMessage("You enter " + newVillage.getName() + ".");
/*  2326 */         newVillage.checkIfRaiseAlert(this);
/*       */         
/*  2328 */         if (isPlayer() && getHighwayPathDestination().length() > 0 && getHighwayPathDestination().equalsIgnoreCase(newVillage.getName())) {
/*       */           
/*  2330 */           getCommunicator().sendNormalServerMessage("You have arrived at your destination.");
/*  2331 */           setLastWaystoneChecked(-10L);
/*  2332 */           setHighwayPath("", null);
/*  2333 */           if (isPlayer())
/*       */           {
/*  2335 */             for (Item waystone : Items.getWaystones()) {
/*       */               
/*  2337 */               VolaTile vt = Zones.getTileOrNull(waystone.getTileX(), waystone.getTileY(), waystone.isOnSurface());
/*       */               
/*  2339 */               if (vt != null)
/*       */               {
/*  2341 */                 for (VirtualZone vz : vt.getWatchers()) {
/*       */ 
/*       */                   
/*       */                   try {
/*  2345 */                     if (vz.getWatcher().getWurmId() == getWurmId()) {
/*       */                       
/*  2347 */                       getCommunicator().sendWaystoneData(waystone);
/*       */                       
/*       */                       break;
/*       */                     } 
/*  2351 */                   } catch (Exception e) {
/*       */                     
/*  2353 */                     logger.log(Level.WARNING, e.getMessage(), e);
/*       */                   } 
/*       */                 } 
/*       */               }
/*       */             } 
/*       */           }
/*       */         } 
/*  2360 */         if (getLogger() != null)
/*       */         {
/*  2362 */           getLogger().log(Level.INFO, getName() + " enters " + newVillage.getName() + ".");
/*       */         }
/*       */       }
/*       */     
/*  2366 */     } else if (!this.currentVillage.equals(newVillage)) {
/*       */       
/*  2368 */       if (newVillage == null) {
/*       */         
/*  2370 */         getCommunicator().sendNormalServerMessage("You leave " + this.currentVillage.getName() + ".");
/*  2371 */         if (!isFighting())
/*  2372 */           this.currentVillage.removeTarget(this); 
/*  2373 */         if (getLogger() != null)
/*       */         {
/*  2375 */           getLogger().log(Level.INFO, getName() + " leaves " + this.currentVillage.getName() + ".");
/*       */         }
/*       */       } 
/*  2378 */       if (newVillage != null) {
/*       */         
/*  2380 */         getCommunicator().sendNormalServerMessage("You enter " + newVillage.getName() + ".");
/*  2381 */         newVillage.checkIfRaiseAlert(this);
/*  2382 */         if (getLogger() != null)
/*       */         {
/*  2384 */           getLogger().log(Level.INFO, getName() + " enters " + newVillage.getName() + ".");
/*       */         }
/*       */       } 
/*       */     } 
/*  2388 */     this.currentVillage = newVillage;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Village getCurrentVillage() {
/*  2397 */     return this.currentVillage;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isVisible() {
/*  2402 */     return this.status.visible;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void refreshVisible() {
/*  2411 */     if (!isVisible()) {
/*       */       return;
/*       */     }
/*       */     
/*  2415 */     setVisible(false);
/*  2416 */     setVisible(true);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setVisible(boolean visible) {
/*  2421 */     this.status.visible = visible;
/*  2422 */     if ((getStatus()).offline) {
/*  2423 */       this.status.visible = false;
/*       */     } else {
/*       */       
/*  2426 */       int tilex = getTileX();
/*  2427 */       int tiley = getTileY();
/*       */       
/*       */       try {
/*  2430 */         Zone zone = Zones.getZone(tilex, tiley, isOnSurface());
/*  2431 */         VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/*  2432 */         if (visible) {
/*       */ 
/*       */           
/*       */           try {
/*  2436 */             if (!isDead())
/*       */             {
/*  2438 */               tile.makeVisible(this);
/*       */             }
/*       */           }
/*  2441 */           catch (NoSuchCreatureException nsc) {
/*       */             
/*  2443 */             logger.log(Level.INFO, nsc.getMessage() + " " + this.id + ", " + this.name, (Throwable)nsc);
/*       */           }
/*  2445 */           catch (NoSuchPlayerException nsp) {
/*       */             
/*  2447 */             logger.log(Level.INFO, nsp.getMessage() + " " + this.id + ", " + this.name, (Throwable)nsp);
/*       */           } 
/*       */         } else {
/*       */           
/*  2451 */           tile.makeInvisible(this);
/*       */         } 
/*  2453 */       } catch (NoSuchZoneException nsz) {
/*       */         
/*  2455 */         logger.log(Level.INFO, getName() + " outside of bounds when going invis.");
/*       */       } 
/*  2457 */       if (isPlayer())
/*       */       {
/*  2459 */         if (!this.status.visible) {
/*  2460 */           Players.getInstance().partChannels((Player)this);
/*       */         } else {
/*  2462 */           Players.getInstance().joinChannels((Player)this);
/*       */         }  } 
/*  2464 */       this.status.sendStateString();
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void calculateZoneBonus(int tilex, int tiley, boolean surfaced) {
/*       */     try {
/*  2481 */       if (Servers.localServer.HOMESERVER) {
/*       */         
/*  2483 */         if (this.currentKingdom == 0) {
/*  2484 */           this.currentKingdom = Servers.localServer.KINGDOM;
/*       */         }
/*       */       } else {
/*       */         
/*  2488 */         setCurrentKingdom(getCurrentKingdom());
/*       */       } 
/*  2490 */       this.zoneBonus = 0.0F;
/*  2491 */       Deity deity = getDeity();
/*  2492 */       if (deity != null) {
/*       */         
/*  2494 */         FaithZone z = Zones.getFaithZone(tilex, tiley, surfaced);
/*  2495 */         if (z != null) {
/*       */           
/*  2497 */           if (z.getCurrentRuler() == deity)
/*       */           {
/*  2499 */             if (getFaith() > 30.0F)
/*  2500 */               this.zoneBonus += 10.0F; 
/*  2501 */             if (getFaith() > 90.0F) {
/*  2502 */               this.zoneBonus += getFaith() - 90.0F;
/*       */             }
/*  2504 */             if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*  2505 */               this.zoneBonus += z.getStrengthForTile(tilex, tiley, surfaced) / 2.0F;
/*       */             } else {
/*  2507 */               this.zoneBonus += z.getStrength() / 2.0F;
/*       */             }
/*       */           
/*       */           }
/*  2511 */           else if ((Features.Feature.NEWDOMAINS.isEnabled() ? z.getStrengthForTile(tilex, tiley, surfaced) : z.getStrength()) == 0)
/*       */           {
/*  2513 */             if (getFaith() >= 90.0F) {
/*  2514 */               this.zoneBonus = 5.0F + getFaith() - 90.0F;
/*       */             }
/*       */           }
/*       */         
/*  2518 */         } else if (getFaith() >= 90.0F) {
/*  2519 */           this.zoneBonus = 5.0F + getFaith() - 90.0F;
/*       */         } 
/*       */       } 
/*  2522 */     } catch (NoSuchZoneException nsz) {
/*       */       
/*  2524 */       logger.log(Level.WARNING, "No faith zone at " + tilex + "," + tiley + ", surf=" + surfaced);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mustChangeTerritory() {
/*  2533 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   protected byte getLastTaggedKingdom() {
/*  2538 */     return this.currentKingdom;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLastTaggedTerr(byte newKingdom) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCurrentKingdom(byte newKingdom) {
/*  2594 */     if (this.currentKingdom == 0) {
/*       */       
/*  2596 */       if (newKingdom != 0) {
/*       */         
/*  2598 */         getCommunicator().sendNormalServerMessage("You enter " + Kingdoms.getNameFor(newKingdom) + ".");
/*       */ 
/*       */         
/*  2601 */         if (Servers.localServer.isChallengeOrEpicServer() && getLastTaggedKingdom() != newKingdom) {
/*       */ 
/*       */ 
/*       */           
/*  2605 */           if (mustChangeTerritory())
/*       */           {
/*  2607 */             getCommunicator().sendSafeServerMessage("You feel an energy boost, as if " + 
/*  2608 */                 getDeity().getName() + " turns " + 
/*  2609 */                 getDeity().getHisHerItsString() + " eyes at you.");
/*       */           }
/*       */           
/*  2612 */           setLastTaggedTerr(newKingdom);
/*       */         } 
/*  2614 */         if (newKingdom != getKingdomId())
/*  2615 */           achievement(374); 
/*  2616 */         if (this.musicPlayer != null)
/*       */         {
/*  2618 */           if (this.musicPlayer.isItOkToPlaySong(true))
/*       */           {
/*  2620 */             if (newKingdom != getKingdomTemplateId()) {
/*       */               
/*  2622 */               if (Kingdoms.getKingdomTemplateFor(newKingdom) == 3 && 
/*  2623 */                 Kingdoms.getKingdomTemplateFor(getKingdomId()) != 3) {
/*  2624 */                 this.musicPlayer.checkMUSIC_TERRITORYHOTS_SND();
/*  2625 */               } else if (Kingdoms.getKingdomTemplateFor(getKingdomId()) == 3) {
/*  2626 */                 this.musicPlayer.checkMUSIC_TERRITORYWL_SND();
/*       */               }
/*       */             
/*       */             } else {
/*       */               
/*  2631 */               playAnthem();
/*       */             }
/*       */           
/*       */           }
/*       */         }
/*       */       } 
/*  2637 */     } else if (newKingdom != this.currentKingdom) {
/*       */       
/*  2639 */       if (newKingdom == 0)
/*       */       {
/*  2641 */         getCommunicator().sendNormalServerMessage("You leave " + Kingdoms.getNameFor(this.currentKingdom) + ".");
/*       */       }
/*  2643 */       if (newKingdom != 0) {
/*       */ 
/*       */ 
/*       */         
/*  2647 */         getCommunicator().sendNormalServerMessage("You enter " + Kingdoms.getNameFor(newKingdom) + ".");
/*  2648 */         if (getPower() <= 0)
/*       */         {
/*  2650 */           if (this.musicPlayer != null)
/*       */           {
/*  2652 */             if (this.musicPlayer.isItOkToPlaySong(true))
/*       */             {
/*  2654 */               if (newKingdom != getKingdomId()) {
/*       */                 
/*  2656 */                 achievement(374);
/*  2657 */                 if (newKingdom == 3 && getKingdomId() != 3) {
/*  2658 */                   this.musicPlayer.checkMUSIC_TERRITORYHOTS_SND();
/*  2659 */                 } else if (getKingdomId() == 3) {
/*  2660 */                   this.musicPlayer.checkMUSIC_TERRITORYWL_SND();
/*  2661 */                 }  Appointments p = King.getCurrentAppointments(newKingdom);
/*  2662 */                 if (p != null)
/*       */                 {
/*  2664 */                   long secret = p.getOfficialForId(1500);
/*  2665 */                   if (secret > 0L) {
/*       */                     
/*       */                     try {
/*       */                       
/*  2669 */                       Creature c = Server.getInstance().getCreature(secret);
/*  2670 */                       if (c.getMindLogical().skillCheck(40.0D, 0.0D, false, 1.0F) > 0.0D)
/*       */                       {
/*  2672 */                         c.getCommunicator().sendNormalServerMessage("Your informers relay information that " + 
/*  2673 */                             getName() + " has entered your territory.");
/*       */                       
/*       */                       }
/*       */                     }
/*  2677 */                     catch (Exception exception) {}
/*       */                   
/*       */                   }
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*       */               else {
/*       */                 
/*  2686 */                 playAnthem();
/*       */               } 
/*       */             }
/*       */           }
/*       */         }
/*       */       } 
/*       */     } 
/*  2693 */     this.currentKingdom = newKingdom;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setCurrentDeity(Deity deity) {
/*  2698 */     if (deity != null) {
/*       */       
/*  2700 */       if (this.currentDeity != deity.number)
/*       */       {
/*  2702 */         this.currentDeity = (byte)deity.number;
/*  2703 */         getCommunicator().sendNormalServerMessage("You feel the presence of " + deity.name + ".");
/*       */       
/*       */       }
/*       */     
/*       */     }
/*  2708 */     else if (this.currentDeity != 0) {
/*       */       
/*  2710 */       getCommunicator().sendNormalServerMessage("You no longer feel the presence of " + 
/*  2711 */           (Deities.getDeity(this.currentDeity)).name + ".");
/*  2712 */       this.currentDeity = 0;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature(long aId) throws Exception {
/*  2725 */     this();
/*  2726 */     setWurmId(aId, 0.0F, 0.0F, 0.0F, 0);
/*  2727 */     this.skills = SkillsFactory.createSkills(aId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void loadTemplate() {
/*  2732 */     this.template = this.status.getTemplate();
/*  2733 */     getMovementScheme().initalizeModifiersWithTemplate();
/*  2734 */     this.breedCounter = (Servers.isThisAPvpServer() ? 900 : 2000) + Server.rand.nextInt(1000);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature setWurmId(long aId, float posx, float posy, float aRot, int layer) throws Exception {
/*  2750 */     this.id = aId;
/*  2751 */     this.status = CreatureStatusFactory.createCreatureStatus(this, posx, posy, aRot, layer);
/*       */ 
/*       */     
/*  2754 */     getMovementScheme().setBridgeId(getBridgeId());
/*  2755 */     return this;
/*       */   }
/*       */ 
/*       */   
/*       */   public void postLoad() throws Exception {
/*  2760 */     loadSkills();
/*       */     
/*  2762 */     if (!isDead() && !isOffline()) {
/*  2763 */       createVisionArea();
/*       */     }
/*  2765 */     if (getTemplate().getCreatureAI() != null) {
/*  2766 */       getTemplate().getCreatureAI().creatureCreated(this);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public TradeHandler getTradeHandler() {
/*  2775 */     if (this.tradeHandler == null)
/*  2776 */       this.tradeHandler = new TradeHandler(this, getStatus().getTrade()); 
/*  2777 */     return this.tradeHandler;
/*       */   }
/*       */ 
/*       */   
/*       */   public void endTrade() {
/*  2782 */     this.tradeHandler.end();
/*  2783 */     this.tradeHandler = null;
/*       */   }
/*       */ 
/*       */   
/*       */   public void addItemTaken(Item item) {
/*  2788 */     if (this.itemsTaken == null)
/*  2789 */       this.itemsTaken = new HashSet<>(); 
/*  2790 */     this.itemsTaken.add(item);
/*       */   }
/*       */ 
/*       */   
/*       */   public void addItemDropped(Item item) {
/*  2795 */     checkTheftWarnQuestion();
/*  2796 */     if (this.itemsDropped == null)
/*  2797 */       this.itemsDropped = new HashSet<>(); 
/*  2798 */     this.itemsDropped.add(item);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void addChallengeScore(int type, float scoreAdded) {}
/*       */ 
/*       */ 
/*       */   
/*       */   protected void sendItemsTaken() {
/*  2808 */     if (this.itemsTaken != null) {
/*       */       
/*  2810 */       PlayerTutorial.firePlayerTrigger(getWurmId(), PlayerTutorial.PlayerTrigger.TAKEN_ITEM);
/*  2811 */       Map<Integer, Integer> diffItems = new HashMap<>();
/*  2812 */       Map<String, Integer> foodItems = new HashMap<>();
/*  2813 */       for (Item item : this.itemsTaken) {
/*       */         
/*  2815 */         if (item.isFood()) {
/*       */ 
/*       */           
/*  2818 */           String name = item.getName();
/*  2819 */           if (foodItems.containsKey(name)) {
/*       */             
/*  2821 */             Integer num = foodItems.get(name);
/*  2822 */             int nums = num.intValue();
/*  2823 */             nums++;
/*  2824 */             foodItems.put(name, Integer.valueOf(nums));
/*       */             continue;
/*       */           } 
/*  2827 */           foodItems.put(name, Integer.valueOf(1));
/*       */           
/*       */           continue;
/*       */         } 
/*  2831 */         Integer templateId = Integer.valueOf(item.getTemplateId());
/*  2832 */         if (diffItems.containsKey(templateId)) {
/*       */           
/*  2834 */           Integer num = diffItems.get(templateId);
/*  2835 */           int nums = num.intValue();
/*  2836 */           nums++;
/*  2837 */           diffItems.put(templateId, Integer.valueOf(nums));
/*       */           continue;
/*       */         } 
/*  2840 */         diffItems.put(templateId, Integer.valueOf(1));
/*       */       } 
/*       */       
/*  2843 */       for (Integer key : diffItems.keySet()) {
/*       */ 
/*       */         
/*       */         try {
/*  2847 */           ItemTemplate lTemplate = ItemTemplateFactory.getInstance().getTemplate(key.intValue());
/*  2848 */           Integer num = diffItems.get(key);
/*  2849 */           int number = num.intValue();
/*  2850 */           if (number == 1) {
/*       */             
/*  2852 */             getCommunicator().sendNormalServerMessage("You get " + lTemplate.getNameWithGenus() + ".");
/*  2853 */             if (isVisible()) {
/*  2854 */               Server.getInstance().broadCastAction(this.name + " gets " + lTemplate.getNameWithGenus() + ".", this, 5);
/*       */             }
/*       */             continue;
/*       */           } 
/*  2858 */           getCommunicator().sendNormalServerMessage("You get " + 
/*  2859 */               StringUtilities.getWordForNumber(number) + " " + lTemplate.sizeString + lTemplate
/*  2860 */               .getPlural() + ".");
/*  2861 */           if (isVisible()) {
/*  2862 */             Server.getInstance().broadCastAction(this.name + " gets " + 
/*  2863 */                 StringUtilities.getWordForNumber(number) + " " + lTemplate.sizeString + lTemplate
/*  2864 */                 .getPlural() + ".", this, 5);
/*       */           }
/*       */         }
/*  2867 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  2869 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*       */         } 
/*       */       } 
/*  2872 */       for (String key : foodItems.keySet()) {
/*       */         
/*  2874 */         Integer num = foodItems.get(key);
/*  2875 */         int number = num.intValue();
/*  2876 */         if (number == 1) {
/*       */           
/*  2878 */           getCommunicator().sendNormalServerMessage("You get " + StringUtilities.addGenus(key) + ".");
/*  2879 */           if (isVisible()) {
/*  2880 */             Server.getInstance().broadCastAction(this.name + " gets " + StringUtilities.addGenus(key) + ".", this, 5);
/*       */           }
/*       */           continue;
/*       */         } 
/*  2884 */         getCommunicator().sendNormalServerMessage("You get " + StringUtilities.getWordForNumber(number) + " " + key + ".");
/*       */         
/*  2886 */         if (isVisible()) {
/*  2887 */           Server.getInstance().broadCastAction(this.name + " gets " + StringUtilities.getWordForNumber(number) + " " + key + ".", this, 5);
/*       */         }
/*       */       } 
/*       */       
/*  2891 */       this.itemsTaken = null;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isIgnored(long playerId) {
/*  2900 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void sendItemsDropped() {
/*  2905 */     if (this.itemsDropped != null) {
/*       */       
/*  2907 */       Map<Integer, Integer> diffItems = new HashMap<>();
/*  2908 */       Map<String, Integer> foodItems = new HashMap<>();
/*  2909 */       for (Item item : this.itemsDropped) {
/*       */         
/*  2911 */         if (item.isFood()) {
/*       */ 
/*       */           
/*  2914 */           String name = item.getName();
/*  2915 */           if (foodItems.containsKey(name)) {
/*       */             
/*  2917 */             Integer num = foodItems.get(name);
/*  2918 */             int nums = num.intValue();
/*  2919 */             nums++;
/*  2920 */             foodItems.put(name, Integer.valueOf(nums));
/*       */             continue;
/*       */           } 
/*  2923 */           foodItems.put(name, Integer.valueOf(1));
/*       */           
/*       */           continue;
/*       */         } 
/*  2927 */         Integer templateId = Integer.valueOf(item.getTemplateId());
/*  2928 */         if (diffItems.containsKey(templateId)) {
/*       */           
/*  2930 */           Integer num = diffItems.get(templateId);
/*  2931 */           int nums = num.intValue();
/*  2932 */           nums++;
/*  2933 */           diffItems.put(templateId, Integer.valueOf(nums));
/*       */           continue;
/*       */         } 
/*  2936 */         diffItems.put(templateId, Integer.valueOf(1));
/*       */       } 
/*       */       
/*  2939 */       for (Integer key : diffItems.keySet()) {
/*       */ 
/*       */         
/*       */         try {
/*  2943 */           ItemTemplate lTemplate = ItemTemplateFactory.getInstance().getTemplate(key.intValue());
/*  2944 */           Integer num = diffItems.get(key);
/*  2945 */           int number = num.intValue();
/*  2946 */           if (number == 1) {
/*       */             
/*  2948 */             getCommunicator().sendNormalServerMessage("You drop " + lTemplate.getNameWithGenus() + ".");
/*  2949 */             Server.getInstance().broadCastAction(this.name + " drops " + lTemplate.getNameWithGenus() + ".", this, 
/*  2950 */                 Math.max(3, lTemplate.getSizeZ() / 10));
/*       */             
/*       */             continue;
/*       */           } 
/*  2954 */           getCommunicator().sendNormalServerMessage("You drop " + 
/*  2955 */               StringUtilities.getWordForNumber(number) + " " + lTemplate.getPlural() + ".");
/*  2956 */           Server.getInstance()
/*  2957 */             .broadCastAction(this.name + " drops " + 
/*  2958 */               StringUtilities.getWordForNumber(number) + " " + lTemplate
/*  2959 */               .getPlural() + ".", this, 5);
/*       */         
/*       */         }
/*  2962 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  2964 */           logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*       */         } 
/*       */       } 
/*  2967 */       for (String key : foodItems.keySet()) {
/*       */         
/*  2969 */         Integer num = foodItems.get(key);
/*  2970 */         int number = num.intValue();
/*  2971 */         if (number == 1) {
/*       */           
/*  2973 */           getCommunicator().sendNormalServerMessage("You drop " + StringUtilities.addGenus(key) + ".");
/*  2974 */           Server.getInstance().broadCastAction(this.name + " drops " + StringUtilities.addGenus(key) + ".", this, 5);
/*       */           
/*       */           continue;
/*       */         } 
/*       */         
/*  2979 */         getCommunicator().sendNormalServerMessage("You drop " + 
/*  2980 */             StringUtilities.getWordForNumber(number) + " " + key + ".");
/*  2981 */         Server.getInstance().broadCastAction(this.name + " drops " + StringUtilities.getWordForNumber(number) + " " + key + ".", this, 5);
/*       */       } 
/*       */ 
/*       */       
/*  2985 */       this.itemsDropped = null;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public String getHoverText(@Nonnull Creature watcher) {
/*  2991 */     String hoverText = "";
/*       */     
/*  2993 */     if (!watcher.isPlayer() || !watcher.hasFlag(57))
/*       */     {
/*       */       
/*  2996 */       if (!isPlayer() || !hasFlag(58))
/*       */       {
/*  2998 */         if (getCitizenVillage() != null && isPlayer())
/*       */         {
/*       */ 
/*       */           
/*  3002 */           hoverText = hoverText + getCitizenVillage().getCitizen(getWurmId()).getRole().getName() + " of " + getCitizenVillage().getName();
/*       */         }
/*       */       }
/*       */     }
/*  3006 */     return hoverText;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public String getNameWithGenus() {
/*  3012 */     if (isUnique() || isPlayer()) {
/*  3013 */       return getName();
/*       */     }
/*       */     
/*  3016 */     if (this.name.toLowerCase().compareTo(this.template.getName().toLowerCase()) != 0)
/*  3017 */       return "the " + getName(); 
/*  3018 */     if (this.template.isVowel(getName().substring(0, 1))) {
/*  3019 */       return "an " + getName();
/*       */     }
/*  3021 */     return "a " + getName();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setTrade(@Nullable Trade trade) {
/*  3027 */     this.status.setTrade(trade);
/*       */   }
/*       */ 
/*       */   
/*       */   public Trade getTrade() {
/*  3032 */     return this.status.getTrade();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isTrading() {
/*  3037 */     return this.status.isTrading();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isLeadable(Creature potentialLeader) {
/*  3042 */     if (this.hitchedTo != null)
/*  3043 */       return false; 
/*  3044 */     if (this.riders != null && this.riders.size() > 0)
/*  3045 */       return false; 
/*  3046 */     if (isDominated()) {
/*       */       
/*  3048 */       if (getDominator() != null)
/*       */       {
/*  3050 */         return getDominator().equals(potentialLeader);
/*       */       }
/*       */ 
/*       */       
/*  3054 */       return false;
/*       */     } 
/*       */     
/*  3057 */     return this.template.isLeadable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOffline() {
/*  3067 */     return (getStatus()).offline;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isLoggedOut() {
/*  3072 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isStayonline() {
/*  3077 */     return (getStatus()).stayOnline;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean setStayOnline(boolean stayOnline) {
/*  3082 */     return getStatus().setStayOnline(stayOnline);
/*       */   }
/*       */ 
/*       */   
/*       */   void setOffline(boolean offline) {
/*  3087 */     getStatus().setOffline(offline);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature getLeader() {
/*  3096 */     return this.leader;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setWounded() {
/*  3101 */     removeIllusion();
/*  3102 */     if (this.damageCounter == 0)
/*  3103 */       addWoundMod(); 
/*  3104 */     playAnimation("wounded", false);
/*  3105 */     this.damageCounter = (short)(int)(30.0F * ItemBonus.getHurtingReductionBonus(this));
/*  3106 */     setStealth(false);
/*  3107 */     getStatus().sendStateString();
/*       */   }
/*       */ 
/*       */   
/*       */   private void addWoundMod() {
/*  3112 */     getMovementScheme().addModifier(this.woundMoveMod);
/*  3113 */     if (isPlayer()) {
/*  3114 */       getCommunicator().sendAddSpellEffect(SpellEffectsEnum.WOUNDMOVE, 100000, 100.0F);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public void removeWoundMod() {
/*  3120 */     getMovementScheme().removeModifier(this.woundMoveMod);
/*  3121 */     if (isPlayer()) {
/*  3122 */       getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.WOUNDMOVE);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isEncumbered() {
/*  3127 */     return (this.carriedWeight >= this.encumbered);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isMoveSlow() {
/*  3132 */     return (this.carriedWeight >= this.moveslow);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isCantMove() {
/*  3137 */     return (this.carriedWeight >= this.cantmove);
/*       */   }
/*       */ 
/*       */   
/*       */   public int getMovePenalty() {
/*  3142 */     if (isMoveSlow())
/*  3143 */       return 5; 
/*  3144 */     if (isEncumbered())
/*  3145 */       return 10; 
/*  3146 */     if (isCantMove())
/*  3147 */       return 20; 
/*  3148 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getMoveSlow() {
/*  3153 */     return this.moveslow;
/*       */   }
/*       */ 
/*       */   
/*       */   private void setMoveLimits() {
/*  3158 */     if (getPower() > 1) {
/*       */ 
/*       */       
/*  3161 */       this.moveslow = Integer.MAX_VALUE;
/*  3162 */       this.encumbered = Integer.MAX_VALUE;
/*  3163 */       this.cantmove = Integer.MAX_VALUE;
/*  3164 */       if (this.movementScheme.stealthMod == null) {
/*  3165 */         this.movementScheme.stealthMod = new DoubleValueModifier(-(80.0D - Math.min(79.0D, getBodyControl())) / 100.0D);
/*       */       } else {
/*  3167 */         this.movementScheme.stealthMod.setModifier(-(80.0D - Math.min(79.0D, getBodyControl())) / 100.0D);
/*       */       } 
/*       */     } else {
/*       */ 
/*       */       
/*       */       try {
/*  3173 */         Skill strength = this.skills.getSkill(102);
/*       */         
/*  3175 */         this.moveslow = (int)strength.getKnowledge(0.0D) * 2000;
/*  3176 */         this.encumbered = (int)strength.getKnowledge(0.0D) * 3500;
/*  3177 */         this.cantmove = (int)strength.getKnowledge(0.0D) * 7000;
/*  3178 */         if (this.movementScheme.stealthMod == null) {
/*  3179 */           this.movementScheme.stealthMod = new DoubleValueModifier(-(80.0D - Math.min(79.0D, getBodyControl())) / 100.0D);
/*       */         } else {
/*  3181 */           this.movementScheme.stealthMod.setModifier(-(80.0D - Math.min(79.0D, getBodyControl())) / 100.0D);
/*       */         } 
/*  3183 */       } catch (NoSuchSkillException nss) {
/*       */         
/*  3185 */         logger.log(Level.WARNING, "No strength skill for " + this, (Throwable)nss);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void calcBaseMoveMod() {
/*  3192 */     if (this.carriedWeight < this.moveslow) {
/*       */       
/*  3194 */       this.movementScheme.setEncumbered(false);
/*  3195 */       this.movementScheme.setBaseModifier(1.0F);
/*       */     }
/*  3197 */     else if (this.carriedWeight >= this.cantmove) {
/*       */       
/*  3199 */       this.movementScheme.setEncumbered(true);
/*  3200 */       this.movementScheme.setBaseModifier(0.05F);
/*  3201 */       getCommunicator().sendAlertServerMessage("You are encumbered and move extremely slow.");
/*       */     }
/*  3203 */     else if (this.carriedWeight >= this.encumbered) {
/*       */       
/*  3205 */       this.movementScheme.setEncumbered(false);
/*  3206 */       this.movementScheme.setBaseModifier(0.25F);
/*       */     }
/*  3208 */     else if (this.carriedWeight >= this.moveslow) {
/*       */       
/*  3210 */       this.movementScheme.setEncumbered(false);
/*  3211 */       this.movementScheme.setBaseModifier(0.75F);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void addCarriedWeight(int weight) {
/*  3217 */     boolean canTriggerPlonk = false;
/*  3218 */     if (isPlayer()) {
/*       */       
/*  3220 */       if (this.carriedWeight < this.moveslow) {
/*       */         
/*  3222 */         if (this.carriedWeight + weight >= this.cantmove)
/*       */         {
/*  3224 */           this.movementScheme.setEncumbered(true);
/*  3225 */           this.movementScheme.setBaseModifier(0.05F);
/*  3226 */           getCommunicator().sendAlertServerMessage("You are encumbered and move extremely slow.");
/*  3227 */           canTriggerPlonk = true;
/*       */         }
/*  3229 */         else if (this.carriedWeight + weight >= this.encumbered)
/*       */         {
/*  3231 */           this.movementScheme.setBaseModifier(0.25F);
/*  3232 */           canTriggerPlonk = true;
/*       */         }
/*  3234 */         else if (this.carriedWeight + weight >= this.moveslow)
/*       */         {
/*  3236 */           this.movementScheme.setBaseModifier(0.75F);
/*  3237 */           canTriggerPlonk = true;
/*       */         }
/*       */       
/*  3240 */       } else if (this.carriedWeight < this.encumbered) {
/*       */         
/*  3242 */         if (this.carriedWeight + weight >= this.cantmove)
/*       */         {
/*  3244 */           this.movementScheme.setEncumbered(true);
/*  3245 */           this.movementScheme.setBaseModifier(0.05F);
/*  3246 */           getCommunicator().sendAlertServerMessage("You are encumbered and move extremely slow.");
/*  3247 */           canTriggerPlonk = true;
/*       */         }
/*  3249 */         else if (this.carriedWeight + weight >= this.encumbered)
/*       */         {
/*  3251 */           this.movementScheme.setBaseModifier(0.25F);
/*  3252 */           canTriggerPlonk = true;
/*       */         }
/*       */       
/*  3255 */       } else if (this.carriedWeight < this.cantmove) {
/*       */         
/*  3257 */         if (this.carriedWeight + weight >= this.cantmove) {
/*       */           
/*  3259 */           this.movementScheme.setEncumbered(true);
/*  3260 */           this.movementScheme.setBaseModifier(0.05F);
/*  3261 */           getCommunicator().sendAlertServerMessage("You are encumbered and move extremely slow.");
/*  3262 */           canTriggerPlonk = true;
/*       */         } 
/*       */       } 
/*  3265 */       if (canTriggerPlonk && 
/*  3266 */         !PlonkData.ENCUMBERED.hasSeenThis(this)) {
/*  3267 */         PlonkData.ENCUMBERED.trigger(this);
/*       */       }
/*  3269 */       this.carriedWeight += weight;
/*       */       
/*  3271 */       if (getVehicle() != -10L) {
/*       */         
/*  3273 */         Creature c = Creatures.getInstance().getCreatureOrNull(getVehicle());
/*  3274 */         if (c != null)
/*       */         {
/*  3276 */           c.ignoreSaddleDamage = true;
/*  3277 */           c.getMovementScheme().update();
/*       */         }
/*       */       
/*       */       } 
/*       */     } else {
/*       */       
/*  3283 */       this.carriedWeight += weight;
/*  3284 */       this.ignoreSaddleDamage = true;
/*  3285 */       this.movementScheme.update();
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean removeCarriedWeight(int weight) {
/*  3295 */     if (isPlayer()) {
/*       */       
/*  3297 */       if (this.carriedWeight >= this.cantmove) {
/*       */         
/*  3299 */         if (this.carriedWeight - weight < this.moveslow)
/*       */         {
/*       */ 
/*       */           
/*  3303 */           this.movementScheme.setEncumbered(false);
/*  3304 */           this.movementScheme.setBaseModifier(1.0F);
/*  3305 */           getCommunicator().sendAlertServerMessage("You can now move again.");
/*       */         }
/*  3307 */         else if (this.carriedWeight - weight < this.encumbered)
/*       */         {
/*       */ 
/*       */           
/*  3311 */           this.movementScheme.setEncumbered(false);
/*  3312 */           this.movementScheme.setBaseModifier(0.75F);
/*  3313 */           getCommunicator().sendAlertServerMessage("You can now move again.");
/*       */         }
/*  3315 */         else if (this.carriedWeight - weight < this.cantmove)
/*       */         {
/*       */ 
/*       */           
/*  3319 */           this.movementScheme.setEncumbered(false);
/*  3320 */           this.movementScheme.setBaseModifier(0.25F);
/*  3321 */           getCommunicator().sendAlertServerMessage("You can now move again.");
/*       */         }
/*       */       
/*  3324 */       } else if (this.carriedWeight >= this.encumbered) {
/*       */         
/*  3326 */         if (this.carriedWeight - weight < this.moveslow)
/*       */         {
/*       */ 
/*       */           
/*  3330 */           this.movementScheme.setEncumbered(false);
/*  3331 */           this.movementScheme.setBaseModifier(1.0F);
/*       */         }
/*  3333 */         else if (this.carriedWeight - weight < this.encumbered)
/*       */         {
/*       */ 
/*       */           
/*  3337 */           this.movementScheme.setEncumbered(false);
/*  3338 */           this.movementScheme.setBaseModifier(0.75F);
/*       */         }
/*       */       
/*  3341 */       } else if (this.carriedWeight >= this.moveslow) {
/*       */         
/*  3343 */         if (this.carriedWeight - weight < this.moveslow) {
/*       */ 
/*       */ 
/*       */           
/*  3347 */           this.movementScheme.setEncumbered(false);
/*  3348 */           this.movementScheme.setBaseModifier(1.0F);
/*       */         } 
/*       */       } 
/*       */       
/*  3352 */       this.carriedWeight -= weight;
/*       */       
/*  3354 */       if (getVehicle() != -10L) {
/*       */         
/*  3356 */         Creature c = Creatures.getInstance().getCreatureOrNull(getVehicle());
/*  3357 */         if (c != null)
/*       */         {
/*  3359 */           c.ignoreSaddleDamage = true;
/*  3360 */           c.getMovementScheme().update();
/*       */         }
/*       */       
/*       */       } 
/*       */     } else {
/*       */       
/*  3366 */       this.carriedWeight -= weight;
/*  3367 */       this.ignoreSaddleDamage = true;
/*  3368 */       this.movementScheme.update();
/*       */     } 
/*       */     
/*  3371 */     if (this.carriedWeight < 0) {
/*       */       
/*  3373 */       logger.log(Level.WARNING, "Carried weight is less than 0 for " + this);
/*  3374 */       if (this instanceof Player)
/*  3375 */         logger.log(Level.INFO, this.name + " now carries " + this.carriedWeight + " AFTER removing " + weight + " gs. Modifier is:" + this.movementScheme
/*  3376 */             .getSpeedModifier() + "."); 
/*  3377 */       return false;
/*       */     } 
/*  3379 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canCarry(int weight) {
/*       */     try {
/*  3388 */       if (getPower() > 1)
/*       */       {
/*  3390 */         return true;
/*       */       }
/*       */       
/*  3393 */       Skill strength = this.skills.getSkill(102);
/*       */       
/*  3395 */       return (strength.getKnowledge(0.0D) * 7000.0D > (weight + this.carriedWeight));
/*       */     }
/*  3397 */     catch (NoSuchSkillException nss) {
/*       */       
/*  3399 */       logger.log(Level.WARNING, "No strength skill for " + this);
/*       */       
/*  3401 */       return false;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public int getCarryCapacityFor(int weight) {
/*       */     try {
/*  3408 */       Skill strength = this.skills.getSkill(102);
/*       */       
/*  3410 */       return (int)(strength.getKnowledge(0.0D) * 7000.0D - this.carriedWeight) / weight;
/*       */     }
/*  3412 */     catch (NoSuchSkillException nss) {
/*       */       
/*  3414 */       logger.log(Level.WARNING, "No strength skill for " + this);
/*       */       
/*  3416 */       return 0;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getCarriedWeight() {
/*  3425 */     return this.carriedWeight;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getSaddleBagsCarriedWeight() {
/*  3430 */     for (Item i : getBody().getAllItems()) {
/*       */       
/*  3432 */       if (i.isSaddleBags()) {
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  3437 */         float mod = 0.5F;
/*  3438 */         if (i.getTemplateId() == 1334) {
/*  3439 */           mod = 0.6F;
/*       */         }
/*  3441 */         return (int)(i.getFullWeight() * mod);
/*       */       } 
/*       */     } 
/*       */     
/*  3445 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public int getCarryingCapacityLeft() {
/*       */     try {
/*  3452 */       Skill strength = this.skills.getSkill(102);
/*       */       
/*  3454 */       return (int)(strength.getKnowledge(0.0D) * 7000.0D) - this.carriedWeight;
/*       */     }
/*  3456 */     catch (NoSuchSkillException nss) {
/*       */       
/*  3458 */       logger.log(Level.WARNING, "No strength skill for " + this);
/*       */       
/*  3460 */       return 0;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setTeleportPoints(short x, short y, int layer, int floorLevel) {
/*  3477 */     setTeleportPoints((x << 2) + 2.0F, (y << 2) + 2.0F, layer, floorLevel);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setTeleportPoints(float x, float y, int layer, int floorLevel) {
/*  3482 */     this.teleportX = x;
/*  3483 */     this.teleportY = y;
/*  3484 */     this.teleportLayer = layer;
/*  3485 */     this.teleportFloorLevel = floorLevel;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setTeleportLayer(int layer) {
/*  3490 */     this.teleportLayer = layer;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setTeleportFloorLevel(int floorLevel) {
/*  3495 */     this.teleportFloorLevel = floorLevel;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getTeleportLayer() {
/*  3500 */     return this.teleportLayer;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getTeleportFloorLevel() {
/*  3505 */     return this.teleportFloorLevel;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public VolaTile getCurrentTile() {
/*  3514 */     if (this.currentTile != null) {
/*  3515 */       return this.currentTile;
/*       */     }
/*       */     
/*  3518 */     if (this.status != null) {
/*       */       
/*  3520 */       int tilex = getTileX();
/*  3521 */       int tiley = getTileY();
/*       */       
/*       */       try {
/*  3524 */         Zone zone = Zones.getZone(tilex, tiley, isOnSurface());
/*       */ 
/*       */ 
/*       */         
/*  3528 */         this.currentTile = zone.getOrCreateTile(tilex, tiley);
/*  3529 */         return this.currentTile;
/*       */       }
/*  3531 */       catch (NoSuchZoneException noSuchZoneException) {}
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  3537 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getCurrentTileNum() {
/*  3542 */     int tilex = getTileX();
/*  3543 */     int tiley = getTileY();
/*  3544 */     if (isOnSurface()) {
/*  3545 */       return Server.surfaceMesh.getTile(tilex, tiley);
/*       */     }
/*  3547 */     return Server.caveMesh.getTile(tilex, tiley);
/*       */   }
/*       */ 
/*       */   
/*       */   public void addItemsToTrade() {
/*  3552 */     if (isTrader()) {
/*  3553 */       getTradeHandler().addItemsToTrade();
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean startTeleporting() {
/*  3558 */     disembark(false);
/*  3559 */     return startTeleporting(false);
/*       */   }
/*       */ 
/*       */   
/*       */   public float getTeleportX() {
/*  3564 */     return this.teleportX;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getTeleportY() {
/*  3569 */     return this.teleportY;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void startTrading() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean shouldStopTrading(boolean firstCall) {
/*  3590 */     if (isTrading()) {
/*       */       
/*  3592 */       if ((getTrade()).creatureOne != null && (getTrade()).creatureOne.isPlayer())
/*       */       {
/*  3594 */         if ((getTrade()).creatureOne.shouldStopTrading(false)) {
/*       */           
/*  3596 */           (getTrade()).creatureOne.getCommunicator().sendAlertServerMessage("You took too long to trade and " + 
/*  3597 */               getName() + " takes care of another customer.");
/*  3598 */           getTrade().end(this, false);
/*  3599 */           return true;
/*       */         } 
/*       */       }
/*  3602 */       if ((getTrade()).creatureTwo != null && (getTrade()).creatureTwo.isPlayer())
/*       */       {
/*  3604 */         if ((getTrade()).creatureTwo.shouldStopTrading(false)) {
/*       */           
/*  3606 */           (getTrade()).creatureTwo.getCommunicator().sendAlertServerMessage("You took too long to trade and " + 
/*  3607 */               getName() + " takes care of another customer.");
/*  3608 */           getTrade().end(this, false);
/*  3609 */           return true;
/*       */         } 
/*       */       }
/*       */     } 
/*  3613 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean startTeleporting(boolean enterVehicle) {
/*  3618 */     if (this.teleportLayer < 0)
/*       */     {
/*  3620 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)this.teleportX >> 2, (int)this.teleportY >> 2)))) {
/*       */         
/*  3622 */         getCommunicator().sendAlertServerMessage("The teleportation target is in rock!");
/*  3623 */         return false;
/*       */       } 
/*       */     }
/*       */ 
/*       */     
/*  3628 */     stopLeading();
/*  3629 */     this._enterVehicle = enterVehicle;
/*  3630 */     if (!enterVehicle) {
/*       */       
/*  3632 */       Creatures.getInstance().setCreatureDead(this);
/*  3633 */       Players.getInstance().setCreatureDead(this);
/*       */     } 
/*  3635 */     this.startTeleportTime = System.currentTimeMillis();
/*  3636 */     this.communicator.setReady(false);
/*  3637 */     if (this.status.isTrading())
/*       */     {
/*  3639 */       this.status.getTrade().end(this, false);
/*       */     }
/*  3641 */     if (this.movementScheme.draggedItem != null)
/*  3642 */       MethodsItems.stopDragging(this, this.movementScheme.draggedItem); 
/*  3643 */     int tileX = getTileX();
/*  3644 */     int tileY = getTileY();
/*       */ 
/*       */     
/*       */     try {
/*  3648 */       destroyVisionArea();
/*  3649 */       if (!isDead())
/*       */       {
/*  3651 */         Zone zone = Zones.getZone(tileX, tileY, isOnSurface());
/*  3652 */         zone.deleteCreature(this, true);
/*       */       }
/*       */     
/*  3655 */     } catch (NoSuchZoneException nsz) {
/*       */       
/*  3657 */       logger.log(Level.WARNING, getName() + " tried to teleport to nonexistant zone at " + tileX + ", " + tileY);
/*       */     }
/*  3659 */     catch (NoSuchCreatureException nsc) {
/*       */       
/*  3661 */       logger.log(Level.WARNING, this + " creature doesn't exist?", (Throwable)nsc);
/*       */     }
/*  3663 */     catch (NoSuchPlayerException nsp) {
/*       */       
/*  3665 */       logger.log(Level.WARNING, this + " player doesn't exist?", (Throwable)nsp);
/*       */     } 
/*       */     
/*  3668 */     this.status.setPositionX(this.teleportX);
/*  3669 */     this.status.setPositionY(this.teleportY);
/*       */     
/*       */     try {
/*  3672 */       this.status.setLayer((this.teleportLayer >= 0) ? 0 : -1);
/*  3673 */       boolean setOffZ = false;
/*  3674 */       if (this.mountAction != null)
/*       */       {
/*       */         
/*  3677 */         setOffZ = true;
/*       */       }
/*  3679 */       if (setOffZ)
/*       */       {
/*  3681 */         this.status.setPositionZ(Math.max(
/*  3682 */               Zones.calculateHeight(this.teleportX, this.teleportY, isOnSurface()) + this.mountAction.getOffZ(), this.mountAction
/*  3683 */               .getOffZ()));
/*  3684 */         (getMovementScheme()).offZ = this.mountAction.getOffZ();
/*       */       }
/*       */       else
/*       */       {
/*  3688 */         VolaTile targetTile = Zones.getTileOrNull((int)(this.teleportX / 4.0F), (int)(this.teleportY / 4.0F), (this.teleportLayer >= 0));
/*       */ 
/*       */         
/*  3691 */         float height = (this.teleportFloorLevel > 0) ? (this.teleportFloorLevel * 3) : 0.0F;
/*  3692 */         if (targetTile != null) {
/*       */           
/*  3694 */           getMovementScheme().setGroundOffset((int)(height * 10.0F), true);
/*  3695 */           calculateFloorLevel(targetTile, true);
/*       */         } 
/*  3697 */         this.status.setPositionZ(Zones.calculateHeight(this.teleportX, this.teleportY, isOnSurface()) + height);
/*       */       }
/*       */     
/*  3700 */     } catch (NoSuchZoneException nsz) {
/*       */       
/*  3702 */       logger.log(Level.WARNING, getName() + " tried to teleport to nonexistant zone at " + this.teleportX + ", " + this.teleportY);
/*       */     } 
/*       */     
/*  3705 */     getMovementScheme().setPosition(this.teleportX, this.teleportY, this.status.getPositionZ(), this.status.getRotation(), getLayer());
/*       */ 
/*       */     
/*  3708 */     getMovementScheme().haltSpeedModifier();
/*       */     
/*  3710 */     boolean zoneExists = true;
/*       */     
/*       */     try {
/*  3713 */       this.status.savePosition(getWurmId(), isPlayer(), 
/*  3714 */           Zones.getZoneIdFor((int)this.teleportX >> 2, (int)this.teleportY >> 2, isOnSurface()), true);
/*       */     }
/*  3716 */     catch (IOException iox) {
/*       */       
/*  3718 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*       */     }
/*  3720 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  3722 */       logger.log(Level.INFO, getName() + " no zone at " + ((int)this.teleportX >> 2) + ", " + ((int)this.teleportY >> 2) + ", surf=" + 
/*  3723 */           isOnSurface());
/*  3724 */       zoneExists = false;
/*       */     } 
/*       */ 
/*       */     
/*       */     try {
/*  3729 */       if (zoneExists) {
/*  3730 */         Zones.getZone((int)this.teleportX >> 2, (int)this.teleportY >> 2, isOnSurface()).addCreature(this.id);
/*       */       }
/*       */       
/*  3733 */       Server.getInstance().addCreatureToPort(this);
/*       */     }
/*  3735 */     catch (Exception ex) {
/*       */       
/*  3737 */       logger.log(Level.WARNING, getName() + " failed to recreate vision area after teleporting: " + ex.getMessage());
/*       */     } 
/*  3739 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getPlayingTime() {
/*  3744 */     return System.currentTimeMillis();
/*       */   }
/*       */ 
/*       */   
/*       */   public void teleport() {
/*  3749 */     teleport(true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void teleport(boolean destroyVisionArea) {
/*  3758 */     this.communicator.setReady(true);
/*       */     
/*  3760 */     if (destroyVisionArea)
/*       */       
/*       */       try {
/*  3763 */         Zone newzone = Zones.getZone(getTileX(), getTileY(), isOnSurface());
/*  3764 */         this.addingAfterTeleport = true;
/*  3765 */         newzone.addCreature(this.id);
/*  3766 */         sendActionControl("", false, 0);
/*       */         
/*       */         try {
/*  3769 */           createVisionArea();
/*       */         }
/*  3771 */         catch (Exception ex) {
/*       */           
/*  3773 */           logger.log(Level.WARNING, "Failed to create visionArea:" + ex.getMessage(), ex);
/*       */         } 
/*  3775 */         Server.getInstance().addCreatureToPort(this);
/*       */       }
/*  3777 */       catch (NoSuchZoneException nsz) {
/*       */         
/*  3779 */         logger.log(Level.WARNING, getName() + " tried to teleport to nonexistant zone at " + getTileX() + ", " + 
/*  3780 */             getTileY());
/*       */       }
/*  3782 */       catch (NoSuchCreatureException nsc) {
/*       */         
/*  3784 */         logger.log(Level.WARNING, "This creature doesn't exist?", (Throwable)nsc);
/*       */       }
/*  3786 */       catch (NoSuchPlayerException nsp) {
/*       */         
/*  3788 */         logger.log(Level.WARNING, "This player doesn't exist?", (Throwable)nsp);
/*       */       }  
/*  3790 */     this.addingAfterTeleport = false;
/*  3791 */     stopTeleporting();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void cancelTeleport() {
/*  3797 */     this.teleportX = -1.0F;
/*  3798 */     this.teleportY = -1.0F;
/*  3799 */     this.teleportLayer = 0;
/*  3800 */     this.startTeleportTime = Long.MIN_VALUE;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendMountData() {
/*  3807 */     if (this._enterVehicle) {
/*       */       
/*  3809 */       if (this.mountAction != null) {
/*       */         
/*  3811 */         this.mountAction.sendData(this);
/*  3812 */         MountTransfer mt = MountTransfer.getTransferFor(getWurmId());
/*  3813 */         if (mt != null)
/*       */         {
/*  3815 */           mt.remove(getWurmId());
/*       */         }
/*       */       } 
/*  3818 */       setMountAction(null);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void stopTeleporting() {
/*  3824 */     if (isTeleporting()) {
/*       */       
/*  3826 */       this.teleportX = -1.0F;
/*  3827 */       this.teleportY = -1.0F;
/*  3828 */       this.teleportLayer = 0;
/*  3829 */       this.startTeleportTime = Long.MIN_VALUE;
/*       */ 
/*       */       
/*  3832 */       if (!this._enterVehicle) {
/*       */         
/*  3834 */         getMovementScheme().setMooredMod(false);
/*  3835 */         getMovementScheme().addWindImpact((byte)0);
/*  3836 */         disembark(false);
/*  3837 */         setMountAction(null);
/*  3838 */         calcBaseMoveMod();
/*       */       } 
/*  3840 */       if (isPlayer()) {
/*       */         
/*  3842 */         ((Player)this).sentClimbing = 0L;
/*  3843 */         ((Player)this).sentMountSpeed = 0L;
/*  3844 */         ((Player)this).sentWind = 0L;
/*       */ 
/*       */         
/*  3847 */         if (!this._enterVehicle) {
/*       */           
/*       */           try {
/*  3850 */             if (getLayer() >= 0) {
/*  3851 */               getVisionArea().getSurface().checkIfEnemyIsPresent(false);
/*       */             } else {
/*  3853 */               getVisionArea().getUnderGround().checkIfEnemyIsPresent(false);
/*       */             } 
/*  3855 */           } catch (Exception exception) {}
/*       */         }
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  3862 */       this._enterVehicle = false;
/*       */       
/*  3864 */       if (!getCommunicator().stillLoggingIn() || !isPlayer())
/*  3865 */         setTeleporting(false); 
/*  3866 */       if (this.justSpawned)
/*       */       {
/*  3868 */         this.justSpawned = false;
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinTeleportTime() {
/*  3878 */     return (System.currentTimeMillis() - this.startTeleportTime < 30000L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isTeleporting() {
/*  3887 */     return this.isTeleporting;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setTeleporting(boolean teleporting) {
/*  3892 */     this.isTeleporting = teleporting;
/*       */   }
/*       */ 
/*       */   
/*       */   public Body getBody() {
/*  3897 */     return this.status.getBody();
/*       */   }
/*       */ 
/*       */   
/*       */   public String examine() {
/*  3902 */     return this.template.examine();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setSpam(boolean spam) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean spamMode() {
/*  3923 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public byte getSex() {
/*  3928 */     if (this.status.getSex() == Byte.MAX_VALUE) {
/*  3929 */       return this.template.getSex();
/*       */     }
/*  3931 */     return this.status.getSex();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean setSex(byte sex, boolean creation) {
/*  3936 */     this.status.setSex(sex);
/*  3937 */     if (!creation && this.currentTile != null) {
/*  3938 */       refreshVisible();
/*       */     }
/*  3940 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void spawnFreeItems() {
/*  3945 */     if (Features.Feature.FREE_ITEMS.isEnabled()) {
/*       */       
/*  3947 */       if (this.spawnWeapon != null && this.spawnWeapon.length() > 0) {
/*       */         
/*  3949 */         TestQuestion.createAndInsertItems(this, 319, 319, 40.0F, true, (byte)-1);
/*       */ 
/*       */         
/*       */         try {
/*  3953 */           int w = Integer.parseInt(this.spawnWeapon);
/*  3954 */           int lTemplate = 0;
/*  3955 */           boolean shield = false;
/*  3956 */           switch (w) {
/*       */             
/*       */             case 1:
/*  3959 */               lTemplate = 21;
/*  3960 */               shield = true;
/*       */               break;
/*       */             case 2:
/*  3963 */               lTemplate = 81;
/*       */               break;
/*       */             case 3:
/*  3966 */               lTemplate = 90;
/*  3967 */               shield = true;
/*       */               break;
/*       */             case 4:
/*  3970 */               lTemplate = 87;
/*       */               break;
/*       */             case 5:
/*  3973 */               lTemplate = 292;
/*  3974 */               shield = true;
/*       */               break;
/*       */             case 6:
/*  3977 */               lTemplate = 290;
/*       */               break;
/*       */             case 7:
/*  3980 */               lTemplate = 706;
/*       */               break;
/*       */             case 8:
/*  3983 */               lTemplate = 705;
/*       */               break;
/*       */           } 
/*  3986 */           if (lTemplate > 0) {
/*       */             try
/*       */             {
/*       */               
/*  3990 */               TestQuestion.createAndInsertItems(this, lTemplate, lTemplate, 40.0F, true, (byte)-1);
/*       */               
/*  3992 */               if (shield)
/*       */               {
/*  3994 */                 TestQuestion.createAndInsertItems(this, 84, 84, 40.0F, true, (byte)-1);
/*       */               
/*       */               }
/*       */             }
/*  3998 */             catch (Exception ex)
/*       */             {
/*  4000 */               logger.log(Level.INFO, "Failed to create item for spawning.", ex);
/*  4001 */               getCommunicator().sendAlertServerMessage("Failed to spawn weapon.");
/*       */             }
/*       */           
/*       */           }
/*  4005 */         } catch (Exception ex) {
/*       */           
/*  4007 */           getCommunicator().sendAlertServerMessage("Failed to spawn weapon.");
/*       */         } 
/*       */       } 
/*       */       
/*  4011 */       this.spawnWeapon = null;
/*  4012 */       if (this.spawnArmour != null && this.spawnArmour.length() > 0) {
/*       */         
/*       */         try {
/*       */           
/*  4016 */           int arm = Integer.parseInt(this.spawnArmour);
/*  4017 */           float ql = 20.0F;
/*  4018 */           byte matType = -1;
/*  4019 */           switch (arm) {
/*       */             
/*       */             case 1:
/*  4022 */               ql = 40.0F;
/*  4023 */               TestQuestion.createAndInsertItems(this, 274, 279, ql, true, (byte)-1);
/*       */ 
/*       */               
/*  4026 */               TestQuestion.createAndInsertItems(this, 278, 278, ql, true, (byte)-1);
/*       */               
/*  4028 */               TestQuestion.createAndInsertItems(this, 274, 274, ql, true, (byte)-1);
/*       */ 
/*       */               
/*  4031 */               TestQuestion.createAndInsertItems(this, 277, 277, ql, true, (byte)-1);
/*       */               break;
/*       */             
/*       */             case 2:
/*  4035 */               ql = 60.0F;
/*  4036 */               TestQuestion.createAndInsertItems(this, 103, 108, ql, true, (byte)-1);
/*       */               
/*  4038 */               TestQuestion.createAndInsertItems(this, 103, 103, ql, true, (byte)-1);
/*       */               
/*  4040 */               TestQuestion.createAndInsertItems(this, 105, 105, ql, true, (byte)-1);
/*       */               
/*  4042 */               TestQuestion.createAndInsertItems(this, 106, 106, ql, true, (byte)-1);
/*       */               break;
/*       */             
/*       */             case 3:
/*  4046 */               ql = 20.0F;
/*  4047 */               TestQuestion.createAndInsertItems(this, 280, 287, ql, true, (byte)-1);
/*       */ 
/*       */               
/*  4050 */               TestQuestion.createAndInsertItems(this, 284, 284, ql, true, (byte)-1);
/*       */               
/*  4052 */               TestQuestion.createAndInsertItems(this, 280, 280, ql, true, (byte)-1);
/*       */ 
/*       */               
/*  4055 */               TestQuestion.createAndInsertItems(this, 283, 283, ql, true, (byte)-1);
/*       */               break;
/*       */           } 
/*       */ 
/*       */         
/*  4060 */         } catch (Exception ex) {
/*       */           
/*  4062 */           getCommunicator().sendAlertServerMessage("Failed to spawn weapon.");
/*       */         } 
/*       */       }
/*  4065 */       this.spawnArmour = null;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Communicator getCommunicator() {
/*  4075 */     return this.communicator;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setSecondsToLogout(int seconds) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addKey(Item key, boolean loading) {
/*  4090 */     if (this.keys == null)
/*  4091 */       this.keys = new HashSet<>(); 
/*  4092 */     if (!this.keys.contains(key)) {
/*       */       
/*  4094 */       this.keys.add(key);
/*  4095 */       if (!loading) {
/*       */         
/*  4097 */         Item[] itemarr = getInventory().getAllItems(false);
/*  4098 */         if (!unlockItems(key, itemarr))
/*  4099 */           unlockItems(key, getBody().getAllItems()); 
/*  4100 */         updateGates(key, false);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void removeKey(Item key, boolean loading) {
/*  4107 */     if (this.keys != null) {
/*       */       
/*  4109 */       if (this.keys.remove(key))
/*       */       {
/*  4111 */         if (!loading) {
/*       */           
/*  4113 */           Item[] itemarr = getInventory().getAllItems(false);
/*  4114 */           if (!lockItems(key, itemarr))
/*  4115 */             lockItems(key, getBody().getAllItems()); 
/*  4116 */           updateGates(key, true);
/*       */         } 
/*       */       }
/*  4119 */       if (this.keys.isEmpty()) {
/*  4120 */         this.keys = null;
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   public void updateGates(Item key, boolean removedKey) {
/*  4126 */     VolaTile t = getCurrentTile();
/*  4127 */     if (t != null) {
/*       */       
/*  4129 */       Door[] doors = t.getDoors();
/*  4130 */       if (doors != null)
/*       */       {
/*  4132 */         for (Door lDoor : doors)
/*       */         {
/*  4134 */           lDoor.updateDoor(this, key, removedKey);
/*       */         }
/*       */       }
/*       */     } else {
/*       */       
/*  4139 */       logger.log(Level.WARNING, getName() + " was on null tile.", new Exception());
/*       */     } 
/*       */   }
/*       */   
/*       */   public void updateGates() {
/*  4144 */     VolaTile t = getCurrentTile();
/*  4145 */     if (t != null) {
/*       */       
/*  4147 */       Door[] doors = t.getDoors();
/*  4148 */       if (doors != null)
/*  4149 */         for (Door lDoor : doors) {
/*       */           
/*  4151 */           lDoor.removeCreature(this);
/*  4152 */           if (lDoor.covers(getPosX(), getPosY(), getPositionZ(), getFloorLevel(), followsGround())) {
/*  4153 */             lDoor.addCreature(this);
/*       */           }
/*       */         }  
/*       */     } else {
/*  4157 */       logger.log(Level.WARNING, getName() + " was on null tile.", new Exception());
/*       */     } 
/*       */   }
/*       */   
/*       */   public boolean unlockItems(Item key, Item[] items) {
/*  4162 */     for (Item lItem : items) {
/*       */       
/*  4164 */       if (lItem.isLockable() && lItem.getLockId() != -10L) {
/*       */         
/*       */         try {
/*       */           
/*  4168 */           Item lock = Items.getItem(lItem.getLockId());
/*  4169 */           long[] keyarr = lock.getKeyIds();
/*  4170 */           for (long lElement : keyarr) {
/*       */             
/*  4172 */             if (lElement == key.getWurmId())
/*       */             {
/*  4174 */               if (!lItem.isEmpty(false))
/*       */               {
/*  4176 */                 if (lItem.getOwnerId() == getWurmId()) {
/*  4177 */                   getCommunicator().sendHasMoreItems(-1L, lItem.getWurmId());
/*       */                 } else {
/*  4179 */                   getCommunicator().sendHasMoreItems(lItem.getTopParent(), lItem.getWurmId());
/*       */                 }  } 
/*  4181 */               return true;
/*       */             }
/*       */           
/*       */           } 
/*  4185 */         } catch (NoSuchItemException nsi) {
/*       */           
/*  4187 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*       */         } 
/*       */       }
/*       */     } 
/*  4191 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean lockItems(Item key, Item[] items) {
/*  4196 */     boolean stillUnlocked = false;
/*       */     
/*  4198 */     for (Item lItem : items) {
/*       */       
/*  4200 */       if (lItem.isLockable() && lItem.getLockId() != -10L) {
/*       */         
/*       */         try {
/*       */           
/*  4204 */           Item lock = Items.getItem(lItem.getLockId());
/*  4205 */           long[] keyarr = lock.getKeyIds();
/*  4206 */           boolean thisLock = false;
/*  4207 */           for (long lElement : keyarr) {
/*       */             
/*  4209 */             for (Item key2 : this.keys) {
/*       */               
/*  4211 */               if (lElement == key2.getWurmId())
/*  4212 */                 stillUnlocked = true; 
/*       */             } 
/*  4214 */             if (lElement == key.getWurmId())
/*  4215 */               thisLock = true; 
/*       */           } 
/*  4217 */           if (thisLock && !stillUnlocked) {
/*       */             
/*  4219 */             Set<Item> contItems = lItem.getItems();
/*  4220 */             for (Item item : contItems)
/*       */             {
/*  4222 */               item.removeWatcher(this, true);
/*       */             }
/*  4224 */             return true;
/*       */           } 
/*  4226 */           if (thisLock)
/*       */           {
/*  4228 */             return true;
/*       */           }
/*       */         }
/*  4231 */         catch (NoSuchItemException nsi) {
/*       */           
/*  4233 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*       */         } 
/*       */       }
/*       */     } 
/*  4237 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasKeyForLock(Item lock) {
/*  4242 */     if (lock.getWurmId() == getWurmId())
/*  4243 */       return true; 
/*  4244 */     if (this.keys == null || this.keys.isEmpty())
/*  4245 */       return false; 
/*  4246 */     if (lock.getWurmId() == 5390789413122L)
/*       */     {
/*  4248 */       if (lock.getParentId() == 5390755858690L) {
/*       */         
/*  4250 */         boolean ok = true;
/*  4251 */         if (!hasAbility(Abilities.getAbilityForItem(809, this)))
/*  4252 */           ok = false; 
/*  4253 */         if (!hasAbility(Abilities.getAbilityForItem(808, this)))
/*  4254 */           ok = false; 
/*  4255 */         if (!hasAbility(Abilities.getAbilityForItem(798, this)))
/*  4256 */           ok = false; 
/*  4257 */         if (!hasAbility(Abilities.getAbilityForItem(810, this)))
/*  4258 */           ok = false; 
/*  4259 */         if (!hasAbility(Abilities.getAbilityForItem(807, this)))
/*  4260 */           ok = false; 
/*  4261 */         if (!ok) {
/*       */           
/*  4263 */           getCommunicator().sendAlertServerMessage("There is some mysterious enchantment on this lock!");
/*  4264 */           return ok;
/*       */         } 
/*       */       } 
/*       */     }
/*  4268 */     long[] keyarr = lock.getKeyIds();
/*  4269 */     for (long lElement : keyarr) {
/*       */       
/*  4271 */       for (Item key : this.keys) {
/*       */         
/*  4273 */         if (lElement == key.getWurmId())
/*  4274 */           return true; 
/*       */       } 
/*       */     } 
/*  4277 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasAllKeysForLock(Item lock) {
/*  4282 */     for (long aKey : lock.getKeyIds()) {
/*       */       
/*  4284 */       boolean foundit = false;
/*  4285 */       for (Item key : getKeys()) {
/*       */         
/*  4287 */         if (aKey == key.getWurmId()) {
/*       */           
/*  4289 */           foundit = true;
/*       */           break;
/*       */         } 
/*       */       } 
/*  4293 */       if (!foundit)
/*  4294 */         return false; 
/*       */     } 
/*  4296 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item[] getKeys() {
/*  4305 */     Item[] toReturn = new Item[0];
/*  4306 */     if (this.keys != null)
/*  4307 */       toReturn = this.keys.<Item>toArray(new Item[this.keys.size()]); 
/*  4308 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isOnSurface() {
/*  4313 */     return this.status.isOnSurface();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLayer(int layer, boolean removeFromTile) {
/*  4321 */     if (getStatus().getLayer() != layer)
/*       */     {
/*  4323 */       if (isPlayer() || removeFromTile) {
/*       */         
/*  4325 */         if (this.currentTile != null) {
/*       */ 
/*       */           
/*  4328 */           if (!(this instanceof Player))
/*       */           {
/*  4330 */             setPositionZ(Zones.calculatePosZ(getPosX(), getPosY(), getCurrentTile(), isOnSurface(), isFloating(), 
/*  4331 */                   getPositionZ(), this, getBridgeId()));
/*       */           }
/*       */ 
/*       */ 
/*       */           
/*  4336 */           getStatus().setLayer(layer);
/*  4337 */           if (getVehicle() != -10L && isVehicleCommander()) {
/*       */             
/*  4339 */             Vehicle vehic = Vehicles.getVehicleForId(getVehicle());
/*  4340 */             if (vehic != null) {
/*       */               
/*  4342 */               boolean ok = true;
/*  4343 */               if (vehic.creature) {
/*       */ 
/*       */                 
/*       */                 try {
/*  4347 */                   Creature cretVehicle = Server.getInstance().getCreature(vehic.wurmid);
/*  4348 */                   if (layer < 0) {
/*       */ 
/*       */                     
/*  4351 */                     int tile = Server.caveMesh.getTile(cretVehicle.getTileX(), cretVehicle.getTileY());
/*  4352 */                     if (!Tiles.isSolidCave(Tiles.decodeType(tile)))
/*       */                     {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                       
/*  4373 */                       cretVehicle.setLayer(layer, false);
/*       */                     }
/*       */                   } else {
/*       */                     
/*  4377 */                     cretVehicle.setLayer(layer, false);
/*       */                   } 
/*  4379 */                 } catch (NoSuchCreatureException nsi) {
/*       */                   
/*  4381 */                   logger.log(Level.WARNING, this + ", cannot get creature for vehicle: " + vehic + " due to " + nsi
/*  4382 */                       .getMessage(), (Throwable)nsi);
/*       */                 }
/*  4384 */                 catch (NoSuchPlayerException nsp) {
/*       */                   
/*  4386 */                   logger.log(Level.WARNING, this + ", cannot get creature for vehicle: " + vehic + " due to " + nsp
/*  4387 */                       .getMessage(), (Throwable)nsp);
/*       */                 } 
/*       */               } else {
/*       */ 
/*       */                 
/*       */                 try {
/*       */                   
/*  4394 */                   Item itemVehicle = Items.getItem(vehic.wurmid);
/*       */                   
/*  4396 */                   if (layer < 0) {
/*       */                     
/*  4398 */                     int caveTile = Server.caveMesh.getTile((int)itemVehicle.getPosX() >> 2, 
/*  4399 */                         (int)itemVehicle.getPosY() >> 2);
/*  4400 */                     if (Tiles.isSolidCave(Tiles.decodeType(caveTile)))
/*       */                     {
/*  4402 */                       ok = false;
/*       */                     }
/*       */                   } 
/*  4405 */                   if (ok) {
/*       */                     
/*  4407 */                     itemVehicle.newLayer = (byte)layer;
/*       */ 
/*       */ 
/*       */                     
/*  4411 */                     Zone zone = null;
/*       */                     
/*       */                     try {
/*  4414 */                       zone = Zones.getZone((int)itemVehicle.getPosX() >> 2, 
/*  4415 */                           (int)itemVehicle.getPosY() >> 2, itemVehicle.isOnSurface());
/*  4416 */                       zone.removeItem(itemVehicle, true, true);
/*       */                     }
/*  4418 */                     catch (NoSuchZoneException nsz) {
/*       */                       
/*  4420 */                       logger.log(Level.WARNING, itemVehicle
/*  4421 */                           .getName() + " this shouldn't happen: " + nsz.getMessage() + " at " + (
/*  4422 */                           (int)itemVehicle.getPosX() >> 2) + ", " + (
/*  4423 */                           (int)itemVehicle.getPosY() >> 2), (Throwable)nsz);
/*       */                     } 
/*       */                     
/*       */                     try {
/*  4427 */                       zone = Zones.getZone((int)itemVehicle.getPosX() >> 2, 
/*  4428 */                           (int)itemVehicle.getPosY() >> 2, (layer >= 0));
/*       */ 
/*       */ 
/*       */                       
/*  4432 */                       zone.addItem(itemVehicle, false, false, false);
/*       */ 
/*       */                     
/*       */                     }
/*  4436 */                     catch (NoSuchZoneException nsz) {
/*       */                       
/*  4438 */                       logger.log(Level.WARNING, itemVehicle
/*  4439 */                           .getName() + " this shouldn't happen: " + nsz.getMessage() + " at " + (
/*  4440 */                           (int)itemVehicle.getPosX() >> 2) + ", " + (
/*  4441 */                           (int)itemVehicle.getPosY() >> 2), (Throwable)nsz);
/*       */                     } 
/*  4443 */                     itemVehicle.newLayer = Byte.MIN_VALUE;
/*  4444 */                     Seat[] seats = vehic.hitched;
/*  4445 */                     if (seats != null)
/*       */                     {
/*  4447 */                       for (int x = 0; x < seats.length; x++) {
/*       */                         
/*  4449 */                         if (seats[x] != null) {
/*       */                           
/*  4451 */                           if ((seats[x]).occupant != -10L) {
/*       */                             
/*       */                             try {
/*       */                               
/*  4455 */                               Creature c = Server.getInstance().getCreature((seats[x]).occupant);
/*       */                               
/*  4457 */                               c.getStatus().setLayer(layer);
/*  4458 */                               c.getCurrentTile().newLayer(c);
/*       */                             }
/*  4460 */                             catch (NoSuchPlayerException nsp) {
/*       */                               
/*  4462 */                               logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp);
/*       */                             }
/*  4464 */                             catch (NoSuchCreatureException nsc) {
/*       */                               
/*  4466 */                               logger.log(Level.WARNING, getName() + " " + nsc.getMessage(), (Throwable)nsc);
/*       */                             } 
/*       */                           }
/*       */                         } else {
/*       */                           
/*  4471 */                           logger.log(Level.WARNING, getName() + " " + vehic.name + ": lacking seat " + x, new Exception());
/*       */                         }
/*       */                       
/*       */                       } 
/*       */                     }
/*       */                   } 
/*  4477 */                 } catch (NoSuchItemException is) {
/*       */                   
/*  4479 */                   logger.log(Level.WARNING, getName() + " " + is.getMessage(), (Throwable)is);
/*       */                 } 
/*       */               } 
/*  4482 */               if (ok) {
/*       */                 
/*  4484 */                 Seat[] seats = vehic.seats;
/*  4485 */                 if (seats != null)
/*       */                 {
/*  4487 */                   for (int x = 0; x < seats.length; x++) {
/*       */                     
/*  4489 */                     if (x > 0)
/*       */                     {
/*       */ 
/*       */ 
/*       */                       
/*  4494 */                       if (seats[x] != null) {
/*       */                         
/*  4496 */                         if ((seats[x]).occupant != -10L) {
/*       */                           
/*       */                           try {
/*       */                             
/*  4500 */                             Creature c = Server.getInstance().getCreature((seats[x]).occupant);
/*  4501 */                             c.getStatus().setLayer(layer);
/*  4502 */                             c.getCurrentTile().newLayer(c);
/*  4503 */                             if (c.isPlayer())
/*       */                             {
/*  4505 */                               if (c.isOnSurface()) {
/*       */                                 
/*  4507 */                                 c.getCommunicator().sendNormalServerMessage("You leave the cave.");
/*       */                               }
/*       */                               else {
/*       */                                 
/*  4511 */                                 c.getCommunicator().sendNormalServerMessage("You enter the cave.");
/*  4512 */                                 if (c.getVisionArea() != null) {
/*  4513 */                                   c.getVisionArea().initializeCaves();
/*       */                                 }
/*       */                               } 
/*       */                             }
/*  4517 */                           } catch (NoSuchPlayerException nsp) {
/*       */                             
/*  4519 */                             logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp);
/*       */                           }
/*  4521 */                           catch (NoSuchCreatureException nsc) {
/*       */                             
/*  4523 */                             logger.log(Level.WARNING, getName() + " " + nsc.getMessage(), (Throwable)nsc);
/*       */                           } 
/*       */                         }
/*       */                       } else {
/*       */                         
/*  4528 */                         logger.log(Level.WARNING, getName() + " " + vehic.name + ": lacking seat " + x, new Exception());
/*       */                       } 
/*       */                     }
/*       */                   } 
/*       */                 }
/*       */               } 
/*       */             } 
/*       */           } 
/*       */           
/*  4537 */           this.currentTile.newLayer(this);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         }
/*       */         else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  4550 */           getStatus().setLayer(layer);
/*  4551 */         }  if (isPlayer()) {
/*       */           
/*  4553 */           if (layer < 0 && getVisionArea() != null)
/*  4554 */             getVisionArea().checkCaves(true); 
/*  4555 */           if (layer < 0) {
/*  4556 */             getCommunicator().sendNormalServerMessage("You enter the cave.");
/*       */           } else {
/*  4558 */             getCommunicator().sendNormalServerMessage("You leave the cave.");
/*       */           } 
/*  4560 */           Village v = Villages.getVillage(getTileX(), getTileY(), true);
/*  4561 */           if (v != null)
/*       */           {
/*  4563 */             if (v.isEnemy(this)) {
/*       */               
/*  4565 */               Guard[] guards = v.getGuards();
/*  4566 */               for (int gx = 0; gx < guards.length; gx++) {
/*       */                 
/*  4568 */                 if (guards[gx].getCreature().isWithinDistanceTo(this, 20.0F))
/*       */                 {
/*  4570 */                   if (visibilityCheck(guards[gx].getCreature(), 0.0F)) {
/*       */                     
/*  4572 */                     v.checkIfRaiseAlert(this);
/*       */ 
/*       */ 
/*       */                     
/*       */                     break;
/*       */                   } 
/*       */                 }
/*       */               } 
/*       */             } 
/*       */           }
/*       */         } 
/*       */       } else {
/*  4584 */         getStatus().setLayer(layer);
/*  4585 */         getCurrentTile().newLayer(this);
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public int getLayer() {
/*  4592 */     return getStatus().getLayer();
/*       */   }
/*       */ 
/*       */   
/*       */   public void setPositionX(float pos) {
/*  4597 */     this.status.setPositionX(pos);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setPositionY(float pos) {
/*  4602 */     this.status.setPositionY(pos);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setPositionZ(float pos) {
/*  4607 */     this.status.setPositionZ(pos);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setRotation(float aRot) {
/*  4612 */     this.status.setRotation(aRot);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void turnTo(float newRot) {
/*  4621 */     setRotation(normalizeAngle(newRot));
/*       */     
/*  4623 */     moved(0.0F, 0.0F, 0.0F, 0, 0);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void turnBy(float turnAmount) {
/*  4632 */     setRotation(normalizeAngle(this.status.getRotation() + turnAmount));
/*       */     
/*  4634 */     moved(0.0F, 0.0F, 0.0F, 0, 0);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void submerge() {
/*       */     try {
/*  4641 */       float lOldPosZ = getPositionZ();
/*  4642 */       float lNewPosZ = isFloating() ? this.template.offZ : (Zones.calculateHeight(getPosX(), getPosY(), true) / 2.0F);
/*       */       
/*  4644 */       moved(0.0F, 0.0F, lNewPosZ - lOldPosZ, 0, 0);
/*       */     }
/*  4646 */     catch (NoSuchZoneException noSuchZoneException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void surface() {
/*  4655 */     float lOldPosZ = getPositionZ();
/*  4656 */     float lNewPosZ = isFloating() ? this.template.offZ : -1.25F;
/*  4657 */     setPositionZ(lNewPosZ);
/*  4658 */     moved(0.0F, 0.0F, lNewPosZ - lOldPosZ, 0, 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public void almostSurface() {
/*  4663 */     float _oldPosZ = getPositionZ();
/*  4664 */     float _newPosZ = -2.0F;
/*  4665 */     setPositionZ(-2.0F);
/*  4666 */     moved(0.0F, 0.0F, -2.0F - _oldPosZ, 0, 0);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCommunicator(Communicator comm) {
/*  4675 */     this.communicator = comm;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void loadPossessions(long inventoryId) throws Exception {
/*       */     try {
/*  4682 */       this.possessions = new Possessions(this, inventoryId);
/*       */     }
/*  4684 */     catch (Exception ex) {
/*       */       
/*  4686 */       logger.log(Level.INFO, ex.getMessage(), ex);
/*  4687 */       this.status.createNewPossessions();
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public long createPossessions() throws Exception {
/*  4694 */     this.possessions = new Possessions(this);
/*  4695 */     return this.possessions.getInventory().getWurmId();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Behaviour getBehaviour() {
/*  4704 */     return this.behaviour;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasFightDistanceTo(Creature _target) {
/*  4709 */     if (Math.abs(getStatus().getPositionX() - _target.getStatus().getPositionX()) > Math.abs(getStatus().getPositionY() - _target
/*  4710 */         .getStatus().getPositionY())) {
/*  4711 */       return (Math.abs(getStatus().getPositionX() - _target.getStatus().getPositionX()) < 8.0F);
/*       */     }
/*  4713 */     return (Math.abs(getStatus().getPositionY() - _target.getStatus().getPositionY()) < 8.0F);
/*       */   }
/*       */ 
/*       */   
/*       */   public static final int rangeTo(Creature performer, Creature target) {
/*  4718 */     if (Math.abs(performer.getStatus().getPositionX() - target.getStatus().getPositionX()) > Math.abs(performer.getStatus()
/*  4719 */         .getPositionY() - target.getStatus().getPositionY())) {
/*  4720 */       return (int)Math.abs(performer.getStatus().getPositionX() - target.getStatus().getPositionX());
/*       */     }
/*  4722 */     return (int)Math.abs(performer.getStatus().getPositionY() - target.getStatus().getPositionY());
/*       */   }
/*       */ 
/*       */   
/*       */   private static final float calcModPosX(double sinRot, double cosRot, float widthCM, float lengthCM) {
/*  4727 */     return (float)(cosRot * widthCM - sinRot * lengthCM);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private static final float calcModPosY(double sinRot, double cosRot, float widthCM, float lengthCM) {
/*  4735 */     return (float)(widthCM * sinRot + lengthCM * cosRot);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private static Vector2f rotate(float angle, Vector2f center, Vector2f point) {
/*  4743 */     double rads = angle * Math.PI / 180.0D;
/*  4744 */     Vector2f nPoint = new Vector2f();
/*  4745 */     nPoint.x = (float)(center.x + (point.x - center.x) * Math.cos(rads) + (point.y - center.y) * Math.sin(rads));
/*  4746 */     nPoint.y = (float)(center.y - (point.x - center.x) * Math.sin(rads) + (point.y - center.y) * Math.cos(rads));
/*  4747 */     return nPoint;
/*       */   }
/*       */ 
/*       */   
/*       */   private static final boolean isLeftOf(Vector2f point, float posX) {
/*  4752 */     return (posX < point.x);
/*       */   }
/*       */ 
/*       */   
/*       */   private static final boolean isRightOf(Vector2f point, float posX) {
/*  4757 */     return (posX > point.x);
/*       */   }
/*       */ 
/*       */   
/*       */   private static final boolean isAbove(Vector2f point, float posY) {
/*  4762 */     return (posY > point.y);
/*       */   }
/*       */ 
/*       */   
/*       */   private static final boolean isBelow(Vector2f point, float posY) {
/*  4767 */     return (posY < point.y);
/*       */   }
/*       */ 
/*       */   
/*       */   private static final int closestPoint(Vector2f[] points, Vector2f pos, Vector2f[] ignore) {
/*  4772 */     boolean canIgnore = (ignore != null);
/*  4773 */     float min = 10000.0F;
/*  4774 */     int index = -1;
/*  4775 */     for (int i = 0; i < points.length; i++) {
/*       */       
/*  4777 */       if (canIgnore) {
/*       */         
/*  4779 */         boolean doIgnore = false;
/*  4780 */         for (int x = 0; x < ignore.length; x++) {
/*       */           
/*  4782 */           if (points[i] == ignore[x])
/*  4783 */             doIgnore = true; 
/*       */         } 
/*  4785 */         if (doIgnore) {
/*       */           continue;
/*       */         }
/*       */       } 
/*  4789 */       float len = pos.subtract(points[i]).length();
/*  4790 */       if (len < min) {
/*       */         
/*  4792 */         index = i;
/*  4793 */         min = len;
/*       */       } 
/*       */       continue;
/*       */     } 
/*  4797 */     return index;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final float rangeToInDec(Creature performer, Creature target) {
/*  4802 */     if (target.getTemplate().hasBoundingBox() && Features.Feature.CREATURE_COMBAT_CHANGES.isEnabled()) {
/*       */       
/*  4804 */       float minX = target.getTemplate().getBoundMinX() * target.getStatus().getSizeMod();
/*  4805 */       float minY = target.getTemplate().getBoundMinY() * target.getStatus().getSizeMod();
/*  4806 */       float maxX = target.getTemplate().getBoundMaxX() * target.getStatus().getSizeMod();
/*  4807 */       float maxY = target.getTemplate().getBoundMaxY() * target.getStatus().getSizeMod();
/*  4808 */       Vector2f center = new Vector2f(target.getStatus().getPositionX(), target.getStatus().getPositionY());
/*       */       
/*  4810 */       float PX = performer.getStatus().getPositionX();
/*  4811 */       float PY = performer.getStatus().getPositionY();
/*  4812 */       Vector3f cpos = new Vector3f(center.x, center.y, 1.0F);
/*       */       
/*  4814 */       float rotation = target.getStatus().getRotation();
/*       */       
/*  4816 */       Vector3f mp1 = new Vector3f(minX, minY, 0.0F);
/*  4817 */       Vector3f mp2 = new Vector3f(maxX, maxY, 0.0F);
/*  4818 */       BoxMatrix M = new BoxMatrix(true);
/*  4819 */       BoundBox box = new BoundBox(M, mp1, mp2);
/*  4820 */       box.M.translate(cpos);
/*  4821 */       box.M.rotate(rotation + 180.0F, false, false, true);
/*       */       
/*  4823 */       Vector3f ppos = new Vector3f(PX, PY, 0.5F);
/*       */ 
/*       */       
/*  4826 */       if (box.isPointInBox(ppos))
/*       */       {
/*  4828 */         return box.distOutside(ppos, cpos) * 10.0F;
/*       */       }
/*       */ 
/*       */       
/*  4832 */       return box.distOutside(ppos, cpos) * 10.0F;
/*       */     } 
/*       */ 
/*       */     
/*  4836 */     if (Math.abs(performer.getStatus().getPositionX() - target.getStatus().getPositionX()) > Math.abs(performer.getStatus()
/*  4837 */         .getPositionY() - target.getStatus().getPositionY())) {
/*  4838 */       return Math.abs(performer.getStatus().getPositionX() - target.getStatus().getPositionX()) * 10.0F;
/*       */     }
/*  4840 */     return Math.abs(performer.getStatus().getPositionY() - target.getStatus().getPositionY()) * 10.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public static int rangeTo(Creature performer, Item aTarget) {
/*  4846 */     if (Math.abs(performer.getStatus().getPositionX() - aTarget.getPosX()) > Math.abs(performer.getStatus().getPositionY() - aTarget
/*  4847 */         .getPosY())) {
/*  4848 */       return (int)Math.abs(performer.getStatus().getPositionX() - aTarget.getPosX());
/*       */     }
/*  4850 */     return (int)Math.abs(performer.getStatus().getPositionY() - aTarget.getPosY());
/*       */   }
/*       */ 
/*       */   
/*       */   public void setAction(Action action) {
/*  4855 */     this.actions.addAction(action);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public ActionStack getActions() {
/*  4862 */     return this.actions;
/*       */   }
/*       */ 
/*       */   
/*       */   public Action getCurrentAction() throws NoSuchActionException {
/*  4867 */     return this.actions.getCurrentAction();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void modifyRanking() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void dropLeadingItem(Item item) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getLeadingItem(Creature follower) {
/*  4898 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature getFollowedCreature(Item leadingItem) {
/*  4910 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isItemLeading(Item item) {
/*  4922 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addFollower(Creature follower, @Nullable Item leadingItem) {
/*  4934 */     if (this.followers == null)
/*  4935 */       this.followers = new HashMap<>(); 
/*  4936 */     this.followers.put(follower, leadingItem);
/*       */   }
/*       */ 
/*       */   
/*       */   public Creature[] getFollowers() {
/*  4941 */     if (this.followers == null || this.followers.size() == 0)
/*  4942 */       return emptyCreatures; 
/*  4943 */     return (Creature[])this.followers.keySet().toArray((Object[])new Creature[this.followers.size()]);
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getNumberOfFollowers() {
/*  4948 */     if (this.followers == null)
/*  4949 */       return 0; 
/*  4950 */     return this.followers.size();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void stopLeading() {
/*  4958 */     if (this.followers != null) {
/*       */       
/*  4960 */       Creature[] followArr = (Creature[])this.followers.keySet().toArray((Object[])new Creature[this.followers.size()]);
/*  4961 */       for (Creature lElement : followArr)
/*       */       {
/*  4963 */         lElement.setLeader(null);
/*       */       }
/*  4965 */       this.followers = null;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean mayLeadMoreCreatures() {
/*  4971 */     return (this.followers == null || this.followers.size() < 10);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isLeading(Creature checked) {
/*  4984 */     for (Creature c : getFollowers()) {
/*       */       
/*  4986 */       for (Creature c2 : getFollowers()) {
/*       */         
/*  4988 */         for (Creature c3 : getFollowers()) {
/*       */           
/*  4990 */           for (Creature c4 : getFollowers()) {
/*       */             
/*  4992 */             for (Creature c5 : getFollowers()) {
/*       */               
/*  4994 */               for (Creature c6 : getFollowers()) {
/*       */                 
/*  4996 */                 for (Creature c7 : getFollowers()) {
/*       */                   
/*  4998 */                   if (c7.getWurmId() == checked.getWurmId())
/*  4999 */                     return true; 
/*       */                 } 
/*  5001 */                 if (c6.getWurmId() == checked.getWurmId())
/*  5002 */                   return true; 
/*       */               } 
/*  5004 */               if (c5.getWurmId() == checked.getWurmId())
/*  5005 */                 return true; 
/*       */             } 
/*  5007 */             if (c4.getWurmId() == checked.getWurmId())
/*  5008 */               return true; 
/*       */           } 
/*  5010 */           if (c3.getWurmId() == checked.getWurmId())
/*  5011 */             return true; 
/*       */         } 
/*  5013 */         if (c2.getWurmId() == checked.getWurmId())
/*  5014 */           return true; 
/*       */       } 
/*  5016 */       if (c.getWurmId() == checked.getWurmId())
/*  5017 */         return true; 
/*       */     } 
/*  5019 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLeader(@Nullable Creature leadingCreature) {
/*  5035 */     if (leadingCreature == this) {
/*       */       
/*  5037 */       logger.log(Level.WARNING, getName() + " tries to lead itself at ", new Exception());
/*       */       return;
/*       */     } 
/*  5040 */     clearOrders();
/*  5041 */     if (this.leader == null) {
/*       */       
/*  5043 */       if (leadingCreature != null)
/*       */       {
/*       */         
/*  5046 */         if (isLeading(leadingCreature))
/*       */           return; 
/*  5048 */         this.leader = leadingCreature;
/*  5049 */         Creatures.getInstance().setLastLed(getWurmId(), this.leader.getWurmId());
/*  5050 */         Server.getInstance().broadCastAction(getNameWithGenus() + " now follows " + this.leader.getNameWithGenus() + ".", this.leader, this, 5);
/*       */         
/*  5052 */         this.leader.getCommunicator().sendNormalServerMessage("You start leading " + 
/*  5053 */             getNameWithGenus() + ".");
/*  5054 */         getCommunicator().sendNormalServerMessage("You start following " + this.leader.getNameWithGenus() + ".");
/*       */       
/*       */       }
/*       */     
/*       */     }
/*  5059 */     else if (leadingCreature == null) {
/*       */       
/*  5061 */       Server.getInstance().broadCastAction(getNameWithGenus() + " stops following " + this.leader.getNameWithGenus() + ".", this.leader, this, 5);
/*       */       
/*  5063 */       this.leader.getCommunicator().sendNormalServerMessage("You stop leading " + 
/*  5064 */           getNameWithGenus() + ".");
/*  5065 */       getCommunicator().sendNormalServerMessage("You stop following " + this.leader.getNameWithGenus() + ".");
/*  5066 */       this.leader.removeFollower(this);
/*       */ 
/*       */       
/*  5069 */       this.leader = null;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeFollower(Creature follower) {
/*  5081 */     if (this.followers != null) {
/*  5082 */       this.followers.remove(follower);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public void putInWorld() {
/*       */     try {
/*  5089 */       Zone z = Zones.getZone(getTileX(), getTileY(), (getLayer() >= 0));
/*  5090 */       z.addCreature(getWurmId());
/*       */     }
/*  5092 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  5094 */       logger.log(Level.WARNING, getName() + " " + nsz.getMessage(), (Throwable)nsz);
/*       */     }
/*  5096 */     catch (NoSuchPlayerException nsp) {
/*       */       
/*  5098 */       logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp);
/*       */     }
/*  5100 */     catch (NoSuchCreatureException nsc) {
/*       */       
/*  5102 */       logger.log(Level.WARNING, getName() + " " + nsc.getMessage(), (Throwable)nsc);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public static final double getRange(Creature performer, double targetX, double targetY) {
/*  5108 */     double diffx = Math.abs(performer.getPosX() - targetX);
/*  5109 */     double diffy = Math.abs(performer.getPosY() - targetY);
/*  5110 */     return Math.sqrt(diffx * diffx + diffy * diffy);
/*       */   }
/*       */ 
/*       */   
/*       */   public static final double getTileRange(Creature performer, int targetX, int targetY) {
/*  5115 */     double diffx = Math.abs(performer.getTileX() - targetX);
/*  5116 */     double diffy = Math.abs(performer.getTileY() - targetY);
/*  5117 */     return Math.sqrt(diffx * diffx + diffy * diffy);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinTileDistanceTo(int tileX, int tileY, int heigh1tOffset, int maxDist) {
/*  5124 */     int ptilex = getTileX();
/*  5125 */     int ptiley = getTileY();
/*  5126 */     if (ptilex > tileX + maxDist || ptilex < tileX - maxDist || ptiley > tileY + maxDist || ptiley < tileY - maxDist)
/*  5127 */       return false; 
/*  5128 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(@Nonnull Item item, float maxDist) {
/*  5136 */     return isWithinDistanceTo(item.getPos3f(), maxDist);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(@Nonnull Vector3f targetPos, float maxDist) {
/*  5141 */     return isWithinDistanceTo(targetPos.x, targetPos.y, targetPos.z, maxDist);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(float aPosX, float aPosY, float aPosZ, float maxDist) {
/*  5148 */     return (Math.abs(getStatus().getPositionX() + getAltOffZ() - aPosX) <= maxDist && 
/*  5149 */       Math.abs(getStatus().getPositionY() - aPosY) <= maxDist);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(Creature targetCret, float maxDist) {
/*  5156 */     return (Math.abs(getStatus().getPositionX() - targetCret.getPosX()) <= maxDist && 
/*  5157 */       Math.abs(getStatus().getPositionY() - targetCret.getPosY()) <= maxDist);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(float aPosX, float aPosY, float aPosZ, float maxDist, float modifier) {
/*  5165 */     return (Math.abs(getStatus().getPositionX() - aPosX + modifier) < maxDist && 
/*  5166 */       Math.abs(getStatus().getPositionY() - aPosY + modifier) < maxDist);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceToZ(float aPosZ, float maxDist, boolean addHalfHeight) {
/*  5171 */     return (Math.abs(getStatus().getPositionZ() + (addHalfHeight ? (getHalfHeightDecimeters() / 10.0F) : 0.0F) - aPosZ) < maxDist);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isWithinDistanceTo(int aPosX, int aPosY, int maxDistance) {
/*  5179 */     return (Math.abs(getTileX() - aPosX) <= maxDistance && 
/*  5180 */       Math.abs(getTileY() - aPosY) <= maxDistance);
/*       */   }
/*       */ 
/*       */   
/*       */   public void creatureMoved(Creature creature, float diffX, float diffY, float diffZ) {
/*  5185 */     if (this.leader != null && this.leader.equals(creature))
/*       */     {
/*  5187 */       if (!isRidden())
/*       */       {
/*  5189 */         if (diffX != 0.0F || diffY != 0.0F)
/*  5190 */           followLeader(); 
/*       */       }
/*       */     }
/*  5193 */     if (isTypeFleeing()) {
/*       */       
/*  5195 */       if (creature.isPlayer() && isBred())
/*       */         return; 
/*  5197 */       if (creature.isPlayer() || creature.isAggHuman() || creature.isHuman() || creature.isCarnivore() || creature
/*  5198 */         .isMonster()) {
/*       */ 
/*       */         
/*  5201 */         Vector2f mypos = new Vector2f(getPosX(), getPosY());
/*       */         
/*  5203 */         float oldDistance = (new Vector2f(creature.getPosX() - diffX, creature.getPosY() - diffY)).distance(mypos);
/*  5204 */         float newDistance = (new Vector2f(creature.getPosX(), creature.getPosY())).distance(mypos);
/*  5205 */         if (oldDistance > newDistance)
/*       */         {
/*  5207 */           if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/*       */             
/*  5209 */             int baseCounter = (int)(Math.max(1.0F, creature.getBaseCombatRating() - getBaseCombatRating()) * 5.0F);
/*  5210 */             if (baseCounter - newDistance > 0.0F) {
/*  5211 */               setFleeCounter((int)Math.min(60.0F, Math.max(3.0F, baseCounter - newDistance)));
/*       */             }
/*       */           } else {
/*       */             
/*  5215 */             setFleeCounter(60);
/*       */           } 
/*       */         }
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPrey() {
/*  5224 */     return this.template.isPrey();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSpy() {
/*  5229 */     return (this.status.modtype == 8 && (this.template.getTemplateId() == 84 || this.template.getTemplateId() == 10 || this.template.getTemplateId() == 12));
/*       */   }
/*       */ 
/*       */   
/*       */   public void delete() {
/*  5234 */     Server.getInstance().addCreatureToRemove(this);
/*       */   }
/*       */ 
/*       */   
/*       */   public void destroyVisionArea() {
/*  5239 */     if (this.visionArea != null)
/*  5240 */       this.visionArea.destroy(); 
/*  5241 */     this.visionArea = null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void createVisionArea() throws Exception {
/*  5248 */     if (this.visionArea != null)
/*  5249 */       this.visionArea.destroy(); 
/*  5250 */     this.visionArea = new VisionArea(this, this.template.getVision());
/*       */   }
/*       */ 
/*       */   
/*       */   public String getHisHerItsString() {
/*  5255 */     if (this.status.getSex() == 0)
/*  5256 */       return "his"; 
/*  5257 */     if (this.status.getSex() == 1) {
/*  5258 */       return "her";
/*       */     }
/*  5260 */     return "its";
/*       */   }
/*       */ 
/*       */   
/*       */   public String getHimHerItString() {
/*  5265 */     if (this.status.getSex() == 0)
/*  5266 */       return "him"; 
/*  5267 */     if (this.status.getSex() == 1) {
/*  5268 */       return "her";
/*       */     }
/*  5270 */     return "it";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mayAttack(@Nullable Creature cret) {
/*  5281 */     return (this.status.getStunned() <= 0.0F && !this.status.isUnconscious());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isStunned() {
/*  5286 */     return (this.status.getStunned() > 0.0F);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isUnconscious() {
/*  5291 */     return this.status.isUnconscious();
/*       */   }
/*       */ 
/*       */   
/*       */   public String getHeSheItString() {
/*  5296 */     if (this.status.getSex() == 0)
/*  5297 */       return "he"; 
/*  5298 */     if (this.status.getSex() == 1) {
/*  5299 */       return "she";
/*       */     }
/*  5301 */     return "it";
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void stopCurrentAction() {
/*       */     try {
/*  5308 */       String toSend = this.actions.stopCurrentAction(false);
/*  5309 */       if (toSend.length() > 0)
/*  5310 */         this.communicator.sendNormalServerMessage(toSend); 
/*  5311 */       sendActionControl("", false, 0);
/*       */     }
/*  5313 */     catch (NoSuchActionException noSuchActionException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void maybeInterruptAction(int damage) {
/*       */     try {
/*  5323 */       Action act = this.actions.getCurrentAction();
/*  5324 */       if (act.isVulnerable() && act
/*  5325 */         .getNumber() != Spells.SPELL_CHARM_ANIMAL.number && act
/*  5326 */         .getNumber() != Spells.SPELL_DOMINATE.number)
/*       */       {
/*  5328 */         if (getBodyControlSkill().skillCheck((damage / 100.0F), this.zoneBonus, false, 1.0F) < 0.0D)
/*       */         {
/*  5330 */           String toSend = this.actions.stopCurrentAction(false);
/*  5331 */           if (toSend.length() > 0)
/*  5332 */             this.communicator.sendNormalServerMessage(toSend); 
/*  5333 */           sendActionControl("", false, 0);
/*       */         }
/*       */       
/*       */       }
/*  5337 */     } catch (NoSuchActionException noSuchActionException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getCombatDamage(Item bodyPart) {
/*  5345 */     short pos = bodyPart.getPlace();
/*  5346 */     if (pos == 13 || pos == 14)
/*  5347 */       return getHandDamage(); 
/*  5348 */     if (pos == 34)
/*  5349 */       return getKickDamage(); 
/*  5350 */     if (pos == 1)
/*  5351 */       return getHeadButtDamage(); 
/*  5352 */     if (pos == 29)
/*  5353 */       return getBiteDamage(); 
/*  5354 */     if (pos == 2)
/*  5355 */       return getBreathDamage(); 
/*  5356 */     return 0.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public String getAttackStringForBodyPart(Item bodypart) {
/*  5361 */     if (bodypart.getPlace() == 13 || bodypart.getPlace() == 14)
/*  5362 */       return this.template.getHandDamString(); 
/*  5363 */     if (bodypart.getPlace() == 34)
/*  5364 */       return this.template.getKickDamString(); 
/*  5365 */     if (bodypart.getPlace() == 29)
/*  5366 */       return this.template.getBiteDamString(); 
/*  5367 */     if (bodypart.getPlace() == 1)
/*  5368 */       return this.template.getHeadButtDamString(); 
/*  5369 */     if (bodypart.getPlace() == 2) {
/*  5370 */       return this.template.getBreathDamString();
/*       */     }
/*  5372 */     return this.template.getHandDamString();
/*       */   }
/*       */ 
/*       */   
/*       */   public float getBodyWeaponSpeed(Item bodypart) {
/*  5377 */     float size = this.template.getSize();
/*  5378 */     if (bodypart.getPlace() == 13 || bodypart.getPlace() == 14)
/*  5379 */       return size + 1.0F; 
/*  5380 */     if (bodypart.getPlace() == 34)
/*  5381 */       return size + 2.0F; 
/*  5382 */     if (bodypart.getPlace() == 29)
/*  5383 */       return size + 2.5F; 
/*  5384 */     if (bodypart.getPlace() == 1)
/*  5385 */       return size + 3.0F; 
/*  5386 */     if (bodypart.getPlace() == 2) {
/*  5387 */       return size + 3.5F;
/*       */     }
/*  5389 */     return 4.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getArmour(byte location) throws NoArmourException, NoSpaceException {
/*  5394 */     Item bodyPart = null;
/*       */     
/*       */     try {
/*  5397 */       boolean barding = isHorse();
/*  5398 */       if (barding) {
/*       */         
/*  5400 */         bodyPart = this.status.getBody().getBodyPart(2);
/*       */       } else {
/*       */         
/*  5403 */         bodyPart = this.status.getBody().getBodyPart(location);
/*       */       } 
/*  5405 */       if (location == 29) {
/*       */         
/*  5407 */         Item helmet = getArmour((byte)1);
/*       */ 
/*       */ 
/*       */         
/*  5411 */         return helmet;
/*       */       } 
/*       */       
/*  5414 */       Set<Item> its = bodyPart.getItems();
/*  5415 */       for (Item item : its) {
/*       */         
/*  5417 */         if (item.isArmour()) {
/*       */           
/*  5419 */           byte[] spaces = item.getBodySpaces();
/*  5420 */           for (byte lSpace : spaces) {
/*       */             
/*  5422 */             if (lSpace == location || barding) {
/*  5423 */               return item;
/*       */             }
/*       */           } 
/*       */         } 
/*       */       } 
/*  5428 */     } catch (NoArmourException noa) {
/*       */ 
/*       */ 
/*       */       
/*  5432 */       throw noa;
/*       */     }
/*  5434 */     catch (Exception ex) {
/*       */       
/*  5436 */       throw new NoSpaceException(ex);
/*       */     } 
/*  5438 */     throw new NoArmourException("No armour worn on bodypart " + location);
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getCarriedItem(int itemTemplateId) {
/*  5443 */     Item inventory = getInventory();
/*  5444 */     Item[] items = inventory.getAllItems(false);
/*  5445 */     for (Item lItem : items) {
/*       */       
/*  5447 */       if (lItem.getTemplateId() == itemTemplateId)
/*       */       {
/*  5449 */         return lItem;
/*       */       }
/*       */     } 
/*  5452 */     Item body = getBody().getBodyItem();
/*  5453 */     items = body.getAllItems(false);
/*  5454 */     for (Item lItem : items) {
/*       */       
/*  5456 */       if (lItem.getTemplateId() == itemTemplateId)
/*  5457 */         return lItem; 
/*       */     } 
/*  5459 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getEquippedItem(byte location) throws NoSpaceException {
/*       */     try {
/*  5466 */       Set<Item> wornItems = this.status.getBody().getBodyPart(location).getItems();
/*  5467 */       for (Item item : wornItems) {
/*       */         
/*  5469 */         if (!item.isArmour() && !item.isBodyPartAttached()) {
/*  5470 */           return item;
/*       */         }
/*       */       } 
/*  5473 */     } catch (NullPointerException npe) {
/*       */       
/*  5475 */       if (this.status == null) {
/*  5476 */         logger.log(Level.WARNING, "status is null for creature" + getName(), npe);
/*  5477 */       } else if (this.status.getBody() == null) {
/*  5478 */         logger.log(Level.WARNING, "body is null for creature" + getName(), npe);
/*  5479 */       } else if (this.status.getBody().getBodyPart(location) == null) {
/*  5480 */         logger.log(Level.WARNING, "body inventoryspace(" + location + ") is null for creature" + getName(), npe);
/*       */       } else {
/*  5482 */         logger.log(Level.WARNING, "seems wornItems for inventoryspace was null for creature" + getName(), npe);
/*       */       } 
/*       */     } 
/*  5485 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getEquippedWeapon(byte location) throws NoSpaceException {
/*  5490 */     return getEquippedWeapon(location, true);
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getEquippedWeapon(byte location, boolean allowBow) throws NoSpaceException {
/*  5495 */     return getEquippedWeapon(location, allowBow, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getEquippedWeapon(byte location, boolean allowBow, boolean fetchBodypart) throws NoSpaceException {
/*  5500 */     Item bodyPart = null;
/*       */     
/*       */     try {
/*  5503 */       bodyPart = this.status.getBody().getBodyPart(location);
/*  5504 */       if (isAnimal())
/*  5505 */         return bodyPart; 
/*  5506 */       if ((bodyPart.getPlace() != 37 && bodyPart.getPlace() != 38 && bodyPart
/*  5507 */         .getPlace() != 13 && bodyPart.getPlace() != 14) || (
/*  5508 */         !isPlayer() && fetchBodypart)) {
/*  5509 */         return bodyPart;
/*       */       }
/*       */       
/*  5512 */       Set<Item> wornItems = bodyPart.getItems();
/*  5513 */       for (Item item : wornItems) {
/*       */         
/*  5515 */         if (!item.isArmour() && !item.isBodyPartAttached())
/*       */         {
/*  5517 */           if (Weapon.getBaseDamageForWeapon(item) > 0.0F || (item.isWeaponBow() && allowBow))
/*       */           {
/*  5519 */             return item;
/*       */           }
/*       */         }
/*       */       } 
/*       */ 
/*       */       
/*  5525 */       if (bodyPart.getPlace() == 37 || bodyPart.getPlace() == 38)
/*       */       {
/*  5527 */         int handSlot = (bodyPart.getPlace() == 37) ? 13 : 14;
/*       */         
/*  5529 */         bodyPart = this.status.getBody().getBodyPart(handSlot);
/*       */       }
/*       */     
/*  5532 */     } catch (NullPointerException npe) {
/*       */       
/*  5534 */       if (this.status == null) {
/*  5535 */         logger.log(Level.WARNING, "status is null for creature" + getName(), npe);
/*  5536 */       } else if (this.status.getBody() == null) {
/*  5537 */         logger.log(Level.WARNING, "body is null for creature" + getName(), npe);
/*  5538 */       } else if (this.status.getBody().getBodyPart(location) == null) {
/*  5539 */         logger.log(Level.WARNING, "body inventoryspace(" + location + ") is null for creature" + getName(), npe);
/*       */       } else {
/*  5541 */         logger.log(Level.WARNING, "seems wornItems for inventoryspace was null for creature" + getName(), npe);
/*  5542 */       }  throw new NoSpaceException("No  bodypart " + location, npe);
/*       */     } 
/*  5544 */     return bodyPart;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getTotalInventoryWeightGrams() {
/*  5549 */     Body body = this.status.getBody();
/*  5550 */     int weight = 0;
/*  5551 */     Item[] items = body.getAllItems();
/*  5552 */     for (Item lItem : items)
/*  5553 */       weight += lItem.getFullWeight(); 
/*  5554 */     Item[] inventoryItems = this.possessions.getInventory().getAllItems(true);
/*  5555 */     for (int x = 0; x < items.length; x++)
/*  5556 */       weight += inventoryItems[x].getFullWeight(); 
/*  5557 */     return weight;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void startPersonalAction(short action, long subject, long _target) {
/*       */     try {
/*  5564 */       BehaviourDispatcher.action(this, this.communicator, subject, _target, action);
/*       */     }
/*  5566 */     catch (FailedException failedException) {
/*       */ 
/*       */ 
/*       */     
/*       */     }
/*  5571 */     catch (NoSuchBehaviourException noSuchBehaviourException) {
/*       */ 
/*       */ 
/*       */     
/*       */     }
/*  5576 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */ 
/*       */     
/*       */     }
/*  5581 */     catch (NoSuchItemException noSuchItemException) {
/*       */ 
/*       */ 
/*       */     
/*       */     }
/*  5586 */     catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */ 
/*       */     
/*       */     }
/*  5591 */     catch (NoSuchWallException noSuchWallException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setFighting() {
/*  5600 */     if (this.opponent != null) {
/*       */       
/*  5602 */       if (getPower() > 0 && !isVisible()) {
/*       */         
/*  5604 */         setOpponent(null);
/*       */         
/*       */         return;
/*       */       } 
/*       */       try {
/*  5609 */         Action lCurrentAction = null;
/*       */         
/*       */         try {
/*  5612 */           lCurrentAction = getCurrentAction();
/*       */         }
/*  5614 */         catch (NoSuchActionException noSuchActionException) {}
/*       */ 
/*       */         
/*  5617 */         if (lCurrentAction == null || lCurrentAction.getNumber() != 114) {
/*       */ 
/*       */ 
/*       */           
/*  5621 */           BehaviourDispatcher.action(this, this.communicator, -1L, this.opponent.getWurmId(), (short)114);
/*       */         }
/*  5623 */         else if (lCurrentAction != null) {
/*  5624 */           sendToLoggers("busy " + lCurrentAction.getActionString() + " seconds " + lCurrentAction
/*  5625 */               .getCounterAsFloat() + " " + lCurrentAction.getTarget() + ", path is null:" + (
/*  5626 */               (this.status.getPath() == null) ? 1 : 0), (byte)4);
/*  5627 */         }  this.status.setPath(null);
/*       */       }
/*  5629 */       catch (FailedException fe) {
/*       */         
/*  5631 */         setOpponent(null);
/*       */ 
/*       */       
/*       */       }
/*  5635 */       catch (NoSuchBehaviourException nsb) {
/*       */         
/*  5637 */         setTarget(-10L, true);
/*  5638 */         setOpponent(null);
/*       */         
/*  5640 */         logger.log(Level.WARNING, nsb.getMessage(), (Throwable)nsb);
/*       */       }
/*  5642 */       catch (NoSuchCreatureException nsc) {
/*       */         
/*  5644 */         setTarget(-10L, true);
/*  5645 */         setOpponent(null);
/*       */       
/*       */       }
/*  5648 */       catch (NoSuchItemException nsi) {
/*       */         
/*  5650 */         setTarget(-10L, true);
/*  5651 */         setOpponent(null);
/*       */         
/*  5653 */         logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*       */       }
/*  5655 */       catch (NoSuchPlayerException nsp) {
/*       */         
/*  5657 */         setTarget(-10L, true);
/*  5658 */         setOpponent(null);
/*       */       }
/*  5660 */       catch (NoSuchWallException nsw) {
/*       */         
/*  5662 */         setOpponent(null);
/*  5663 */         logger.log(Level.WARNING, nsw.getMessage(), (Throwable)nsw);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public void attackTarget() {
/*  5670 */     if (this.target != -10L)
/*       */     {
/*  5672 */       if (this.opponent == null || this.opponent.getWurmId() != this.target) {
/*       */         
/*  5674 */         long start = System.nanoTime();
/*       */         
/*  5676 */         Creature tg = getTarget();
/*  5677 */         if (tg != null && (tg.isDead() || tg.isOffline())) {
/*  5678 */           setTarget(-10L, true);
/*  5679 */         } else if (isDominated() && tg != null && tg.isDominated() && getDominator() == tg.getDominator()) {
/*       */ 
/*       */ 
/*       */           
/*  5683 */           setTarget(-10L, true);
/*  5684 */           setOpponent(null);
/*       */         }
/*  5686 */         else if (tg != null) {
/*       */           
/*  5688 */           if (rangeTo(this, tg) < Actions.actionEntrys[114].getRange()) {
/*       */             
/*  5690 */             if (!isPlayer() && tg.getFloorLevel() != getFloorLevel())
/*       */             {
/*  5692 */               if (isSpiritGuard()) {
/*  5693 */                 pushToFloorLevel(getTarget().getFloorLevel());
/*       */               }
/*  5695 */               else if (tg.getFloorLevel() != getFloorLevel()) {
/*       */                 
/*  5697 */                 Floor[] floors = getCurrentTile().getFloors(
/*  5698 */                     Math.min(getFloorLevel(), tg.getFloorLevel()) * 30, 
/*  5699 */                     Math.max(getFloorLevel(), tg.getFloorLevel()) * 30);
/*  5700 */                 for (Floor f : floors) {
/*       */                   
/*  5702 */                   if (tg.getFloorLevel() > getFloorLevel()) {
/*       */                     
/*  5704 */                     if (f.getFloorLevel() == getFloorLevel() + 1)
/*       */                     {
/*  5706 */                       if ((f.isOpening() && canOpenDoors()) || f.isStair()) {
/*       */                         
/*  5708 */                         pushToFloorLevel(f.getFloorLevel());
/*       */ 
/*       */ 
/*       */                         
/*       */                         break;
/*       */                       } 
/*       */                     }
/*  5715 */                   } else if (f.getFloorLevel() == getFloorLevel()) {
/*       */                     
/*  5717 */                     if ((f.isOpening() && canOpenDoors()) || f.isStair()) {
/*       */                       
/*  5719 */                       pushToFloorLevel(f.getFloorLevel() - 1);
/*       */                       
/*       */                       break;
/*       */                     } 
/*       */                   } 
/*       */                 } 
/*       */               } 
/*       */             }
/*  5727 */             if (tg.getLayer() != getLayer())
/*       */             {
/*  5729 */               if (!(tg.getCurrentTile()).isTransition || !(getCurrentTile()).isTransition) {
/*       */                 return;
/*       */               }
/*       */             }
/*  5733 */             if (tg != this.opponent && tg.getAttackers() >= tg.getMaxGroupAttackSize()) {
/*       */               
/*  5735 */               ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  5736 */               segments.add(new CreatureLineSegment(tg));
/*  5737 */               segments.add(new MulticolorLineSegment(" is too crowded with attackers. You find no space.", (byte)0));
/*       */               
/*  5739 */               getCommunicator().sendColoredMessageCombat(segments);
/*       */ 
/*       */               
/*       */               return;
/*       */             } 
/*       */ 
/*       */             
/*  5746 */             if (!CombatHandler.prerequisitesFail(this, tg, true, getPrimWeapon())) {
/*       */               
/*  5748 */               if (!tg.isTeleporting())
/*       */               {
/*  5750 */                 setOpponent(tg);
/*  5751 */                 if (!tg.isPlayer() && this.fightlevel > 1) {
/*       */                   
/*  5753 */                   this.fightlevel = (byte)(this.fightlevel / 2);
/*  5754 */                   if (isPlayer())
/*  5755 */                     getCommunicator().sendFocusLevel(getWurmId()); 
/*       */                 } 
/*  5757 */                 if (!isPlayer()) {
/*  5758 */                   this.status.setMoving(false);
/*       */                 }
/*  5760 */                 ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/*  5761 */                 segments.add(new CreatureLineSegment(this));
/*  5762 */                 segments.add(new MulticolorLineSegment(" try to " + CombatEngine.getAttackString(this, getPrimWeapon()) + " ", (byte)0));
/*  5763 */                 segments.add(new CreatureLineSegment(tg));
/*  5764 */                 segments.add(new MulticolorLineSegment(".", (byte)0));
/*       */                 
/*  5766 */                 getCommunicator().sendColoredMessageCombat(segments);
/*  5767 */                 if (isPlayer() || isDominated())
/*       */                 {
/*  5769 */                   ((MulticolorLineSegment)segments.get(1)).setText(" tries to " + CombatEngine.getAttackString(this, getPrimWeapon()) + " ");
/*  5770 */                   tg.getCommunicator().sendColoredMessageCombat(segments);
/*       */                   
/*  5772 */                   if (isDominated() && getDominator() != null && getDominator().isPlayer())
/*       */                   {
/*  5774 */                     getDominator().getCommunicator().sendColoredMessageCombat(segments);
/*       */                   }
/*       */                 }
/*       */                 else
/*       */                 {
/*  5779 */                   ((MulticolorLineSegment)segments.get(1)).setText(" moves in to attack ");
/*  5780 */                   tg.getCommunicator().sendColoredMessageCombat(segments);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*       */                 }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*       */               }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*       */             }
/*  5816 */             else if (!isPlayer() && Server.rand.nextInt(50) == 0) {
/*  5817 */               setTarget(-10L, true);
/*       */             }
/*       */           
/*  5820 */           } else if (isSpellCaster()) {
/*       */             
/*  5822 */             if (rangeTo(this, tg) < 24)
/*       */             {
/*  5824 */               if (!isPlayer() && tg.getFloorLevel() == getFloorLevel())
/*       */               {
/*  5826 */                 if (getLayer() == tg.getLayer())
/*       */                 {
/*  5828 */                   if (getFavor() >= 100.0F && Server.rand.nextInt(10) == 0) {
/*       */                     
/*  5830 */                     setOpponent(tg);
/*  5831 */                     short spellAction = 420;
/*  5832 */                     switch (this.template.getTemplateId()) {
/*       */                       
/*       */                       case 110:
/*  5835 */                         if (Server.rand.nextInt(3) == 0)
/*  5836 */                           spellAction = 485; 
/*  5837 */                         if (Server.rand.nextBoolean())
/*  5838 */                           spellAction = 414; 
/*       */                         break;
/*       */                       case 111:
/*  5841 */                         if (Server.rand.nextInt(3) == 0)
/*  5842 */                           spellAction = 550; 
/*  5843 */                         if (Server.rand.nextBoolean())
/*  5844 */                           spellAction = 549; 
/*       */                         break;
/*       */                       default:
/*  5847 */                         spellAction = 420;
/*       */                         break;
/*       */                     } 
/*       */ 
/*       */                     
/*  5852 */                     if (this.opponent != null) {
/*       */                       
/*       */                       try {
/*  5855 */                         long itemId = -10L;
/*       */                         
/*       */                         try {
/*  5858 */                           Item bodyHand = getBody().getBodyPart(14);
/*  5859 */                           itemId = bodyHand.getWurmId();
/*       */                         }
/*  5861 */                         catch (Exception ex) {
/*       */                           
/*  5863 */                           logger.log(Level.INFO, getName() + ": No hand.");
/*       */                         } 
/*  5865 */                         if (spellAction == 420 || spellAction == 414) {
/*       */                           
/*  5867 */                           BehaviourDispatcher.action(this, this.communicator, itemId, Tiles.getTileId(this.opponent.getTileX(), this.opponent.getTileY(), 0), spellAction);
/*       */                         } else {
/*       */                           
/*  5870 */                           BehaviourDispatcher.action(this, this.communicator, itemId, this.opponent.getWurmId(), spellAction);
/*       */                         } 
/*  5872 */                       } catch (Exception ex) {
/*       */                         
/*  5874 */                         logger.log(Level.INFO, getName() + " casting " + spellAction + ":" + ex.getMessage(), ex);
/*       */                       } 
/*       */                     }
/*       */                   } 
/*       */                 }
/*       */               }
/*       */             }
/*       */           } 
/*       */         } else {
/*  5883 */           setTarget(-10L, true);
/*       */         } 
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void moan() {
/*  5899 */     if (isDominated()) {
/*       */       
/*  5901 */       if (getDominator() != null)
/*  5902 */         getDominator().getCommunicator().sendNormalServerMessage("You sense a disturbance in " + getNameWithGenus() + "."); 
/*  5903 */       if (isAnimal()) {
/*  5904 */         Server.getInstance().broadCastAction(getNameWithGenus() + " grunts.", this, 5);
/*       */       } else {
/*  5906 */         Server.getInstance().broadCastAction(getNameWithGenus() + " moans.", this, 5);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void frolic() {
/*  5915 */     if (isDominated()) {
/*       */       
/*  5917 */       if (getDominator() != null)
/*  5918 */         getDominator().getCommunicator().sendNormalServerMessage("You sense a sudden calm in " + getNameWithGenus() + "."); 
/*  5919 */       if (isAnimal()) {
/*  5920 */         Server.getInstance().broadCastAction(getNameWithGenus() + " purrs.", this, 5);
/*       */       } else {
/*  5922 */         Server.getInstance().broadCastAction(getNameWithGenus() + " hizzes.", this, 5);
/*       */       } 
/*       */     } 
/*       */   }
/*       */   
/*       */   private boolean isOutOfBounds() {
/*  5928 */     return (getTileX() < 0 || getTileX() > Zones.worldTileSizeX - 1 || getTileY() < 0 || 
/*  5929 */       getTileY() > Zones.worldTileSizeY - 1);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean isFlying() {
/*  5944 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean healRandomWound(int power) {
/*  5949 */     if (getBody().getWounds() != null) {
/*       */       
/*  5951 */       Wound[] wounds = getBody().getWounds().getWounds();
/*  5952 */       if (wounds.length > 0) {
/*       */         
/*  5954 */         int num = Server.rand.nextInt(wounds.length);
/*  5955 */         if (wounds[num].getSeverity() / 1000.0F < power) {
/*       */           
/*  5957 */           wounds[num].heal();
/*  5958 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  5962 */         wounds[num].modifySeverity(-power * 1000);
/*  5963 */         return true;
/*       */       } 
/*       */     } 
/*       */     
/*  5967 */     return false; }
/*       */   public void pollNPC() {}
/*       */   public void pollNPCChat() {}
/*       */   static long firstCreature = -10L;
/*       */   static int pollChecksPer = 301;
/*  5972 */   static final int breedPollCounter = 201; int breedTick; private int lastPolled; private CreatureAIData aiData; private boolean isPathing; private boolean setTargetNOID; private Creature creatureToBlinkTo; public boolean receivedPath; private PathTile targetPathTile; public CreatureAIData getCreatureAIData() { if (this.template.getCreatureAI() != null) { if (this.aiData == null) { this.aiData = this.template.getCreatureAI().createCreatureAIData(); this.aiData.setCreature(this); }  return this.aiData; }  return null; } public boolean poll() throws Exception { if (this.template.getCreatureAI() != null) { boolean toDestroy = this.template.getCreatureAI().pollCreature(this, System.currentTimeMillis() - getCreatureAIData().getLastPollTime()); getCreatureAIData().setLastPollTime(System.currentTimeMillis()); return toDestroy; }  if (this.breedTick++ >= 201) { checkBreedCounter(); this.breedTick = 0; }  if (isNpcTrader()) if (this.heatCheckTick++ >= 600) { getInventory().pollCoolingItems(this, 600000L); this.heatCheckTick = 0; }   if (isVisibleToPlayers() || isTrader() || this.lastPolled == 0 || this.status.getPath() != null || this.target != -10L || isUnique() || isNpc()) { if (firstCreature == -10L) firstCreature = getWurmId();  this.lastPolled = pollChecksPer - 1; } else { this.lastPolled--; return false; }  long start = System.nanoTime(); try { if (this.fleeCounter > 0) this.fleeCounter = (byte)(this.fleeCounter - 1);  setHugeMoveCounter(getHugeMoveCounter() - 1); decreaseOpportunityCounter(); if (this.guardSecondsLeft > 0) this.guardSecondsLeft = (byte)(this.guardSecondsLeft - 1);  if (getPathfindCounter() > 100) { if (isSpiritGuard()) logger.log(Level.WARNING, getName() + " " + getWurmId() + " pathfind " + getPathfindCounter() + ". Target was " + this.target + ". Surfaced=" + isOnSurface());  setPathfindcounter(0); setTarget(-10L, true); if (isDominated()) { logger.log(Level.WARNING, getName() + " was dominated and failed to find path."); if (getDominator() != null) getDominator().getCommunicator().sendNormalServerMessage("The " + getName() + " fails to follow your orders.");  if (this.decisions != null) this.decisions.clearOrders();  }  }  if (getTemplate().getTemplateId() == 88) { if (!WurmCalendar.isNight() && getLayer() >= 0) { die(false, "Wraith in Daylight"); return true; }  } else if (isOutOfBounds()) { handleCreatureOutOfBounds(); return true; }  if (this.opponentCounter > 0 && this.opponent == null) if (--this.opponentCounter == 0) { this.lastOpponent = null; getCombatHandler().setCurrentStance(-1, (byte)15); this.combatRound = 0; }   this.status.pollDetectInvis(); if (isStunned()) getStatus().setStunned((byte)(int)(getStatus().getStunned() - 1.0F), false);  if (!isDead()) { if (getSpellEffects() != null) getSpellEffects().poll();  pollNPCChat(); if (this.actions.poll(this)) { attackTarget(); if (isFighting()) { setFighting(); } else if (!isDead()) { if (Server.getSecondsUptime() != this.lastSecond) { this.lastSecond = Server.getSecondsUptime(); if (!isRidden() && isNeedFood() && canEat()) if (Server.rand.nextInt(60) == 0) { findFood(); if (hasTrait(7)) if (Zone.hasSpring(getTileX(), getTileY())) if (Server.rand.nextInt(5) == 0) frolic();    if (!isRidden() && hasTrait(12) && Server.rand.nextInt(10) == 0) if (getLeader() != null) { Server.getInstance().broadCastAction(getName() + " refuses to move on.", this, 5); setLeader(null); }   }   checkStealthing(); pollNPC(); checkEggLaying(); if (!isRidden() && !pollAge()) { checkMove(); startUsingPath(); }  if (getStatus().pollFat()) { boolean disease = ((getStatus()).disease >= 100); String deathCause = "starvation"; if (disease) deathCause = "disease";  Server.getInstance().broadCastAction(getNameWithGenus() + " rolls with the eyes, ejects " + getHisHerItsString() + " tongue and dies from " + deathCause + ".", this, 5); logger.log(Level.INFO, getName() + " dies from " + deathCause + "."); die(false, deathCause); } else { checkForEnemies(); }  }  } else { logger.log(Level.INFO, getName() + " died when attacking?"); }  }  }  if (this.webArmourModTime > 0.0F && this.webArmourModTime-- <= 1.0F) { this.webArmourModTime = 0.0F; if (getMovementScheme().setWebArmourMod(false, 0.0F)) getMovementScheme().setWebArmourMod(false, 0.0F);  if (!isFighting() && this.fightlevel > 0) { this.fightlevel = (byte)Math.max(0, this.fightlevel - 1); if (isPlayer()) getCommunicator().sendFocusLevel(getWurmId());  }  }  if (System.currentTimeMillis() - this.lastSavedPos > 3600000L) { this.lastSavedPos = System.currentTimeMillis() + (Server.rand.nextInt(3600) * 1000); savePosition(this.status.getZoneId()); getStatus().save(); if (getTemplateId() == 78 || getTemplateId() == 79 || getTemplateId() == 80 || getTemplateId() == 81 || getTemplateId() == 68) if (!EpicServerStatus.doesGiveItemMissionExist(getWurmId())) return true;   }  if (!this.status.dead) { if (this.damageCounter > 0) { this.damageCounter = (short)(this.damageCounter - 1); if (this.damageCounter <= 0) { removeWoundMod(); getStatus().sendStateString(); }  }  breakout(); pollItems(); if (this.tradeHandler != null) this.tradeHandler.balance();  sendItemsTaken(); sendItemsDropped(); if (isVehicle()) pollMount();  if (getBody() != null) { getBody().poll(); } else { logger.log(Level.WARNING, getName() + "'s body is null."); }  if (this.template.isMilkable()) if (!canEat() && Server.rand.nextInt(7200) == 0) setMilked(false);   if (this.template.isWoolProducer()) { if (!canEat() && Server.rand.nextInt(14400) == 0) setSheared(false);  } else { removeRandomItems(); }  pollStamina(); pollFavor(); pollLoyalty(); trimAttackers(false); this.numattackers = 0; this.hasAddedToAttack = false; if (isSpiritGuard()) if (this.citizenVillage != null && this.target == -10L && this.citizenVillage.targets.size() > 0) this.citizenVillage.assignTargets();   if (this.hitchedTo != null || isRidden()) this.goOffline = false;  if (!isUnique() && this.goOffline && !isFighting() && isDominated() && Players.getInstance().getPlayerOrNull(this.dominator) == null) { logger.log(Level.INFO, getName() + " going offline."); Creatures.getInstance().setCreatureOffline(this); this.goOffline = false; return true; }  return (isTransferring() || !isOnCurrentServer()); }  if (this.respawnCounter > 0) { this.respawnCounter--; if (this.respawnCounter == 0) { float[] xy = Player.findRandomSpawnX(true, true); try { setLayer(0, true); setPositionX(xy[0]); setPositionY(xy[1]); setPositionZ(calculatePosZ()); respawn(); Zone zone = Zones.getZone(getTileX(), getTileY(), isOnSurface()); zone.addCreature(getWurmId()); savePosition(zone.getId()); return false; } catch (NoSuchZoneException noSuchZoneException) {  } catch (NoSuchCreatureException noSuchCreatureException) {  } catch (NoSuchPlayerException noSuchPlayerException) {  } catch (Exception exception) {} }  }  return true; } finally { this.shouldStandStill = false; float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F; if (lElapsedTime > (float)LOG_ELAPSED_TIME_THRESHOLD) logger.info("Polled Creature id, " + getWurmId() + ", which took " + lElapsedTime + " millis.");  }  } public void setWebArmourModTime(float time) { this.webArmourModTime = time; } private void checkStealthing() {} public boolean isSpellCaster() { return this.template.isCaster(); } public boolean isSummoner() { return this.template.isSummoner(); } public boolean isRespawn() { return false; } private void handleCreatureOutOfBounds() { logger.log(Level.WARNING, getName() + " was out of bounds. Killing."); Creatures.getInstance().setCreatureDead(this); Players.getInstance().setCreatureDead(this); destroy(); } protected void checkBreedCounter() { if (this.breedCounter > 0) this.breedCounter -= 201;  if (this.breedCounter < 0) this.breedCounter = 0;  if (this.breedCounter == 0) { if (this.leader == null && !isDominated()) if (isInTheMoodToBreed(false)) checkBreedingPossibility();   float mod = (float)Servers.localServer.getBreedingTimer(); if (mod <= 0.0F) mod = 1.0F;  int base = (int)(84000.0F / mod); if (checkPregnancy(false)) { base = (int)(Servers.isThisAPvpServer() ? (2000.0F / mod) : (84000.0F / mod)); this.forcedBreed = true; } else { base = (int)(Servers.isThisAPvpServer() ? (900.0F / mod) : (2000.0F / mod)); this.forcedBreed = false; }  this.breedCounter = base + (int)(Server.rand.nextInt(Math.max(1000, 100 * Math.abs(20 - (getStatus()).age))) / mod); }  } public void pollLoyalty() { if (isDominated()) if (getStatus().pollLoyalty()) { if (getDominator() != null) { getDominator().getCommunicator().sendAlertServerMessage(getNameWithGenus() + " is tame no more.", (byte)2); if (getDominator().getPet() == this) getDominator().setPet(-10L);  }  setDominator(-10L); }   } public boolean isInRock() { if (getLayer() < 0) if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(getTileX(), getTileY())))) return true;   return false; } public void findFood() { if (this.currentTile != null) if (!graze()) { Item[] items = this.currentTile.getItems(); for (Item lItem : items) { if (lItem.isEdibleBy(this)) { if (lItem.getTemplateId() != 272) { eat(lItem); return; }  if (lItem.isCorpseLootable()) { eat(lItem); return; }  }  }  }   } public int eat(Item item) { int hungerStilled = MethodsItems.eat(this, item); if (hungerStilled > 0) { getStatus().modifyHunger(-hungerStilled, item.getNutritionLevel()); Server.getInstance().broadCastAction(getNameWithGenus() + " eats " + item.getNameWithGenus() + ".", this, 5); } else if (item.getTemplateId() != 272) { Server.getInstance().broadCastAction(getNameWithGenus() + " eats " + item.getNameWithGenus() + ".", this, 5); }  return hungerStilled; } public boolean graze() { if (isGrazer() && isOnSurface()) { if (hasTrait(13)) { if (Server.rand.nextBoolean()) { try { Skill str = this.skills.getSkill(102); if (str.getKnowledge() > 15.0D) str.setKnowledge(str.getKnowledge() - 0.003000000026077032D, false);  } catch (NoSuchSkillException nss) { this.skills.learn(102, 20.0F); }  return false; }  } else if (Server.rand.nextBoolean()) { try { Skill str = this.skills.getSkill(102); double templateStr = getTemplate().getSkills().getSkill(102).getKnowledge(); if (str.getKnowledge() < templateStr) str.setKnowledge(str.getKnowledge() + 0.029999999329447746D, false);  } catch (NoSuchSkillException e) { this.skills.learn(102, 20.0F); } catch (Exception exception) {} }  int tile = Server.surfaceMesh.getTile(this.currentTile.tilex, this.currentTile.tiley); byte type = Tiles.decodeType(tile); Village v = Villages.getVillage(this.currentTile.tilex, this.currentTile.tiley, this.currentTile.isOnSurface()); if (!hasTrait(22)) return grazeNonCorrupt(tile, type, v);  return grazeCorrupt(tile, type); }  return false; } private boolean grazeCorrupt(int tile, byte type) { if (type == Tiles.Tile.TILE_MYCELIUM.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) { getStatus().modifyHunger(-10000, 0.9F); if (Server.rand.nextInt(20) == 0) if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) { TileFieldBehaviour.graze(this.currentTile.tilex, this.currentTile.tiley, tile); } else if (type == Tiles.Tile.TILE_MYCELIUM.id) { GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)); if (growthStage == GrassData.GrowthStage.SHORT) { Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT_PACKED.id, (byte)0); } else { growthStage = growthStage.getPreviousStage(); Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, GrassData.encodeGrassTileData(growthStage, GrassData.FlowerType.NONE)); }  Players.getInstance().sendChangedTile(this.currentTile.tilex, this.currentTile.tiley, true, true); }   Server.getInstance().broadCastAction(getNameWithGenus() + " grazes.", this, 5); return true; }  return false; } private boolean grazeNonCorrupt(int tile, byte type, Village v) { if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_ENCHANTED_GRASS.id) { getStatus().modifyHunger(-10000, (type == Tiles.Tile.TILE_STEPPE.id) ? 0.5F : 0.9F); if (Server.rand.nextInt(20) == 0) { int enchGrassPackChance = 120; if (v == null) { enchGrassPackChance = 80; } else if (v.getCreatureRatio() > Village.OPTIMUMCRETRATIO) { enchGrassPackChance = 240; }  if (type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id) { TileFieldBehaviour.graze(this.currentTile.tilex, this.currentTile.tiley, tile); } else if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_STEPPE.id || (type == Tiles.Tile.TILE_ENCHANTED_GRASS.id && Server.rand.nextInt(enchGrassPackChance) == 0)) { GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)); if (growthStage == GrassData.GrowthStage.SHORT) { Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT_PACKED.id, (byte)0); } else { growthStage = growthStage.getPreviousStage(); Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, GrassData.encodeGrassTileData(growthStage, GrassData.FlowerType.NONE)); }  Players.getInstance().sendChangedTile(this.currentTile.tilex, this.currentTile.tiley, true, true); }  }  Server.getInstance().broadCastAction(getNameWithGenus() + " grazes.", this, 5); return true; }  return false; } public boolean pollAge() { long start = System.nanoTime(); try { int maxAge = this.template.getMaxAge(); if (isReborn()) maxAge = 14;  if (getStatus().pollAge(maxAge)) { sendDeathString(); die(true, "Old Age"); return true; }  return false; } finally {} } public void sendDeathString() { if (!isOffline()) { String act = "hiccups"; int x = Server.rand.nextInt(6); if (x == 0) { act = "drools"; } else if (x == 1) { act = "faints"; } else if (x == 2) { act = "makes a weird gurgly sound"; } else if (x == 3) { act = "falls down"; } else if (x == 4) { act = "rolls over"; }  Server.getInstance().broadCastAction(getNameWithGenus() + " " + act + " and dies.", this, 5); }  } public void pollFavor() { if ((isSpellCaster() || isSummoner()) && Server.rand.nextInt(30) == 0) try { setFavor(getFavor() + 10.0F); } catch (Exception exception) {}  } public boolean isSalesman() { return (this.template.getTemplateId() == 9); } public boolean isAvatar() { return (this.template.getTemplateId() == 78 || this.template.getTemplateId() == 79 || this.template.getTemplateId() == 80 || this.template.getTemplateId() == 81 || this.template.getTemplateId() == 68); } public void removeRandomItems() { if (!isTrading()) if (isNpcTrader()) if (Server.rand.nextInt(86400) == 0) try { this.actions.getCurrentAction(); } catch (NoSuchActionException nsa) { Shop myshop = Economy.getEconomy().getShop(this); if (myshop.getOwnerId() == -10L) { Shop kingsMoney = Economy.getEconomy().getKingsShop(); if (kingsMoney.getMoney() > 0L) { int value = 0; value = (int)(kingsMoney.getMoney() / Shop.getNumTraders()); if (!Servers.localServer.HOMESERVER) { value = (int)(value * (1.0F + Zones.getPercentLandForKingdom(getKingdomId()) / 100.0F)); value = (int)(value * (1.0F + Items.getBattleCampControl(getKingdomId()) / 10.0F)); }  if (value > 0) if (myshop != null) if (myshop.getMoney() < Servers.localServer.getTraderMaxIrons()) if (myshop.getSellRatio() > 0.1F || Server.getInstance().isPS()) if (Server.getInstance().isPS() || Servers.localServer.id != 15 || kingsMoney.getMoney() > 2000000L) { myshop.setMoney(myshop.getMoney() + value); kingsMoney.setMoney(kingsMoney.getMoney() - value); }      }  } else if (canAutoDismissMerchant(myshop)) { try { Item sign = ItemFactory.createItem(209, 10.0F + Server.rand.nextFloat() * 10.0F, getName()); sign.setDescription("Due to poor business I have moved on. Thank you for your time. " + getName()); sign.setLastOwnerId(myshop.getOwnerId()); sign.putItemInfrontof(this); sign.setIsPlanted(true); } catch (Exception e) { logger.log(Level.WARNING, e.getMessage() + " " + getName() + " at " + getTileX() + ", " + getTileY(), e); }  TraderManagementQuestion.dismissMerchant(this, getWurmId()); }  }     } private boolean canAutoDismissMerchant(Shop myshop) { if (myshop.howLongEmpty() > 7257600000L) return true;  PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(myshop.getOwnerId()); if (pinf != null) try { if (!pinf.loaded) pinf.load();  if (pinf.lastLogin == 0L && System.currentTimeMillis() - pinf.lastLogout > 7257600000L) { logger.log(Level.INFO, pinf.getName() + " last login was " + Server.getTimeFor(System.currentTimeMillis() - pinf.lastLogout) + " ago."); return true; }  return false; } catch (IOException iOException) {}  return true; } public float getArmourMod() { return this.template.getNaturalArmour(); } public final Vector2f getPos2f() { return getStatus().getPosition2f(); } public final Vector3f getPos3f() { return getStatus().getPosition3f(); } public final float getPosX() { return getStatus().getPositionX(); } public final float getPosY() { return getStatus().getPositionY(); } public final float getPositionZ() { return getStatus().getPositionZ(); } @Nonnull public final TilePos getTilePos() { return TilePos.fromXY(getTileX(), getTileY()); } public final int getTileX() { return (int)getPosX() >> 2; } public final int getTileY() { return (int)getPosY() >> 2; } public final int getPosZDirts() { return (int)(getPositionZ() * 10.0F); } public final void pollItems() { resetCompassLantern(); this.pollCounter++; boolean triggerPoll = false; if (isHorse()) if ((getBody().getAllItems()).length > 0) triggerPoll = true;   if (isPlayer() || ((isReborn() || isHuman()) && this.pollCounter > 10800) || (triggerPoll && this.pollCounter > 60L)) { if (!this.checkedHotItemsAfterLogin && isPlayer()) { this.checkedHotItemsAfterLogin = true; long timeSinceLastCoolingCheck = System.currentTimeMillis() - PlayerInfoFactory.createPlayerInfo(getName()).getLastLogout(); getInventory().pollCoolingItems(this, timeSinceLastCoolingCheck); }  getInventory().pollOwned(this); getBody().getBodyItem().pollOwned(this); if (triggerPoll) { getInventory().pollCoolingItems(this, (this.pollCounter - 1) * 1000L); getBody().getBodyItem().pollCoolingItems(this, (this.pollCounter - 1) * 1000L); }  }  if (this.pollCounter > 10800 || (triggerPoll && this.pollCounter > 60L)) this.pollCounter = 0;  pollCompassLantern(); } public boolean isLastDeath() { return false; } public boolean isOnHostileHomeServer() { return false; } public void playPersonalSound(String soundName) {} public final void setReputationEffects() { if (Servers.localServer.HOMESERVER) if ((!isPlayer() && isDominated()) || isRidden() || getHitched() != null) if (this.attackers != null) for (Long attl : this.attackers.keySet()) { try { Creature attacker = Server.getInstance().getCreature(attl.longValue()); if (attacker.isPlayer() || attacker.isDominated()) { if (isRidden()) { if (attacker.getCitizenVillage() == null || getCurrentVillage() != attacker.getCitizenVillage()) for (Long riderLong : getRiders()) { try { Creature rider = Server.getInstance().getCreature(riderLong.longValue()); if (rider != attacker && !rider.isOkToKillBy(attacker)) { attacker.setUnmotivatedAttacker(); attacker.setReputation(attacker.getReputation() - 10); }  } catch (NoSuchPlayerException noSuchPlayerException) {} }   continue; }  if (getHitched() != null) { if (attacker.getCitizenVillage() == null || getCurrentVillage() != attacker.getCitizenVillage()) if (!getHitched().isCreature()) try { Item i = Items.getItem((getHitched()).wurmid); long ownid = i.getLastOwnerId(); if (ownid != attacker.getWurmId()) try { byte kingd = Players.getInstance().getKingdomForPlayer(ownid); if (attacker.isFriendlyKingdom(kingd) && !attacker.hasBeenAttackedBy(ownid)) { boolean ok = false; try { Creature owner = Server.getInstance().getCreature(ownid); if (owner.isOkToKillBy(attacker)) ok = true;  } catch (NoSuchCreatureException noSuchCreatureException) {} if (!ok) { attacker.setUnmotivatedAttacker(); attacker.setReputation(attacker.getReputation() - 10); }  }  } catch (Exception exception) {}  } catch (NoSuchItemException nsi) { logger.log(Level.INFO, (getHitched()).wurmid + " no such item:", (Throwable)nsi); }    continue; }  if (isDominated()) { if (attacker.isFriendlyKingdom(getKingdomId())) { boolean ok = false; try { Creature owner = Server.getInstance().getCreature(this.dominator); if (attacker == owner || owner.isOkToKillBy(attacker)) ok = true;  } catch (NoSuchCreatureException noSuchCreatureException) {} if (!ok) { attacker.setUnmotivatedAttacker(); attacker.setReputation(attacker.getReputation() - 10); }  }  continue; }  if (getCurrentVillage() != null) { Brand brand = Creatures.getInstance().getBrand(getWurmId()); if (brand != null) try { Village villageBrand = Villages.getVillage((int)brand.getBrandId()); if (getCurrentVillage() == villageBrand) if (attacker.getCitizenVillage() != villageBrand) { attacker.setUnmotivatedAttacker(); attacker.setReputation(attacker.getReputation() - 10); }   } catch (NoSuchVillageException nsv) { brand.deleteBrand(); }   }  }  } catch (Exception exception) {} }     } public void die(boolean freeDeath, String reasonOfDeath) { die(freeDeath, reasonOfDeath, false); } public void die(boolean freeDeath, String reasonOfDeath, boolean noCorpse) { WcKillCommand wkc = new WcKillCommand(WurmId.getNextWCCommandId(), getWurmId()); if (Servers.isThisLoginServer()) { wkc.sendFromLoginServer(); } else { wkc.sendToLoginServer(); }  if (isPregnant()) Offspring.deleteSettings(getWurmId());  if (getTemplate().getCreatureAI() != null) { boolean fullOverride = getTemplate().getCreatureAI().creatureDied(this); if (fullOverride) return;  }  String corpseDescription = ""; if (this.template.isHorse) { String col = this.template.getColourName(this.status); corpseDescription = col; } else if (this.template.isBlackOrWhite) { if (!hasTrait(15) && !hasTrait(16) && !hasTrait(18) && !hasTrait(24) && !hasTrait(25) && !hasTrait(23) && !hasTrait(30) && !hasTrait(31) && !hasTrait(32) && !hasTrait(33) && !hasTrait(34)) if (hasTrait(17)) corpseDescription = "black";   } else if (this.template.isColoured) { corpseDescription = this.template.getColourName(getStatus()); }  if (isCaredFor()) if (corpseDescription.equals("")) { corpseDescription = corpseDescription + reasonOfDeath.toLowerCase(); } else { corpseDescription = corpseDescription + " [" + reasonOfDeath.toLowerCase() + "]"; }   if (getTemplate().getTemplateId() == 105) { try { Item water = ItemFactory.createItem(128, 100.0F, ""); getInventory().insertItem(water); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, getName() + " No template for item id " + ''); } catch (FailedException e) { logger.log(Level.WARNING, getName() + " failed for item id " + ''); }  Weather weather = Server.getWeather(); if (weather != null) weather.modifyFogTarget(-0.025F);  }  if (isUnique() && !isReborn()) { Player[] ps = Players.getInstance().getPlayers(); HashSet<Player> lootReceivers = new HashSet<>(); for (Player p : ps) { if (p != null && p.getInventory() != null && p.isWithinDistanceTo(this, 300.0F) && p.isPaying()) if (!p.isDead()) { try { Item blood = ItemFactory.createItem(866, 100.0F, ""); blood.setData2(this.template.getTemplateId()); p.getInventory().insertItem(blood); lootReceivers.add(p); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, p.getName() + " No template for item id " + '͢'); } catch (FailedException fe) { logger.log(Level.WARNING, p.getName() + " " + fe.getMessage() + ":" + '͢'); }  } else { logger.log(Level.INFO, "Player " + p.getName() + " is dead, and therefor received no loot from " + getNameWithGenus() + "."); }   }  setPathing(false, true); if (isDragon()) { Set<Player> primeLooters = new HashSet<>(); Set<Player> leecher = new HashSet<>(); for (Player looter : lootReceivers) { Skill bStrength = looter.getBodyStrength(); Skill bControl = looter.getBodyControlSkill(); Skill fighting = looter.getFightingSkill(); if ((bStrength != null && bStrength.getRealKnowledge() >= 30.0D) || (bControl != null && bControl.getRealKnowledge() >= 30.0D) || (fighting != null && fighting.getRealKnowledge() >= 65.0D) || looter.isPriest()) { primeLooters.add(looter); continue; }  leecher.add(looter); }  int lootTemplate = 371; if (getTemplate().getTemplateId() == 16 || getTemplate().getTemplateId() == 89 || getTemplate().getTemplateId() == 91 || getTemplate().getTemplateId() == 90 || getTemplate().getTemplateId() == 92) lootTemplate = 372;  try { distributeDragonScaleOrHide(primeLooters, leecher, lootTemplate); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, "No template for " + lootTemplate + "! Players to receive were:"); for (Player p : lootReceivers) logger.log(Level.WARNING, p.getName());  }  }  }  removeIllusion(); setReputationEffects(); getCombatHandler().clearMoveStack(); getCommunicator().setGroundOffset(0, true); setDoLavaDamage(false); setDoAreaEffect(false); if (isPlayer()) for (int x = 0; x < 5; x++) getStatus().decreaseFat();   this.combatRound = 0; Item corpse = null; int tilex = getTileX(); int tiley = getTileY(); try { boolean wasHunted = hasAttackedUnmotivated(); if (isPlayer()) { Item item = getDraggedItem(); if (item != null && (item.getTemplateId() == 539 || item.getTemplateId() == 186 || item.getTemplateId() == 445 || item.getTemplateId() == 1125)) achievement(72);  if (getVehicle() != -10L) { Vehicle vehic = Vehicles.getVehicleForId(getVehicle()); if (vehic != null && vehic.getPilotId() == getWurmId()) try { Item c = Items.getItem(getVehicle()); if (c.getTemplateId() == 539) achievement(71);  } catch (NoSuchItemException noSuchItemException) {}  }  if (!PlonkData.DEATH.hasSeenThis(this)) PlonkData.DEATH.trigger(this);  }  if (getDraggedItem() != null) MethodsItems.stopDragging(this, getDraggedItem());  stopLeading(); if (this.leader != null) this.leader.removeFollower(this);  clearLinks(); disableLink(); disembark(false); if (!hasNoServerSound()) SoundPlayer.playSound(getDeathSound(), this, 1.6F);  if (this.musicPlayer != null) this.musicPlayer.checkMUSIC_DYING1_SND();  Creatures.getInstance().setCreatureDead(this); Players.getInstance().setCreatureDead(this); if (getSpellEffects() != null) getSpellEffects().destroy(true);  if (this.currentVillage != null) this.currentVillage.removeTarget(getWurmId(), true);  setOpponent(null); this.target = -10L; try { getCurrentAction().stop(false); } catch (NoSuchActionException noSuchActionException) {} this.actions.clear(); if (isKing()) { King king = King.getKing(getKingdomId()); if (king != null) { if (king.getChallengeAcceptedDate() > 0L) if (System.currentTimeMillis() > king.getChallengeAcceptedDate()) king.setFailedChallenge();   if (isInOwnDuelRing()) if (!king.hasFailedAllChallenges()) king.setFailedChallenge();   }  }  getCommunicator().sendSafeServerMessage("You are dead."); getCommunicator().sendCombatSafeMessage("You are dead."); Server.getInstance().broadCastAction(getNameWithGenus() + " is dead. R.I.P.", this, 5); if (!isPlayer() && (isTrader() || isSalesman() || isBartender() || (this.template != null && (this.template.id == 63 || this.template.id == 62)))) { String message = "(" + getWurmId() + ") died at [" + getTileX() + ", " + getTileY() + "] surf=" + isOnSurface() + " with the reason of death being " + reasonOfDeath; if (this.attackers != null) if (this.attackers.size() > 0) { message = message + ". numAttackers=" + this.attackers.size() + " :"; int counter = 0; for (Iterator<Long> iterator = this.attackers.keySet().iterator(); iterator.hasNext(); ) { long playerID = ((Long)iterator.next()).longValue(); counter++; message = message + " " + PlayerInfoFactory.getPlayerName(playerID) + ((counter == this.attackers.size()) ? "," : "."); }  }   Players.getInstance().sendGmMessage(null, getName(), message, false); String templateAndName = ((getTemplate() != null) ? getTemplate().getName() : "Important creature") + " " + getName() + " died"; logger.warning(templateAndName + " " + message); WcTrelloDeaths wtd = new WcTrelloDeaths(templateAndName, message); wtd.sendToLoginServer(); }  if (!isGhost() && !this.template.isNoCorpse() && !noCorpse && (getCreatureAIData() == null || (getCreatureAIData() != null && getCreatureAIData().doesDropCorpse()))) { corpse = ItemFactory.createItem(272, 100.0F, null); corpse.setPosXY(getStatus().getPositionX(), getStatus().getPositionY()); corpse.setPosZ(calculatePosZ()); corpse.onBridge = getBridgeId(); if (hasCustomSize()) corpse.setSizes((int)((corpse.getSizeX() * (getSizeModX() & 0xFF)) / 64.0F), (int)((corpse.getSizeY() * (getSizeModY() & 0xFF)) / 64.0F), (int)((corpse.getSizeZ() * (getSizeModZ() & 0xFF)) / 64.0F));  corpse.setRotation(normalizeAngle(getStatus().getRotation() - 180.0F)); int nameLength = 10 + this.name.length() + getStatus().getAgeString().length() + 1 + getStatus().getTypeString().length(); int nameLengthNoType = 10 + this.name.length() + getStatus().getAgeString().length(); int nameLengthNoAge = 10 + this.name.length() + 1 + getStatus().getTypeString().length(); if (isPlayer()) { corpse.setName("corpse of " + this.name); } else if (nameLength < 40) { corpse.setName("corpse of " + getStatus().getAgeString() + " " + ((nameLength < 40) ? getStatus().getTypeString() : "") + this.name.toLowerCase()); } else if (nameLengthNoAge < 40) { corpse.setName("corpse of " + getStatus().getTypeString() + this.name.toLowerCase()); } else if (nameLengthNoType < 40) { corpse.setName("corpse of " + getStatus().getAgeString() + " " + this.name.toLowerCase()); } else if (("corpse of " + this.name).length() < 40) { corpse.setName("corpse of " + this.name.toLowerCase()); } else { StringTokenizer strt = new StringTokenizer(this.name.toLowerCase()); int maxNumber = strt.countTokens(); String coname = "corpse of " + strt.nextToken(); int number = 1; while (strt.hasMoreTokens()) { number++; String next = strt.nextToken(); if (maxNumber < 4 || (maxNumber > 4 && number > 4)) { if ((coname + " " + next).length() < 40) { coname = coname + " "; coname = coname + next; continue; }  break; }  }  corpse.setName(coname); }  byte extra1 = -1; byte extra2 = this.status.modtype; if (this.template.isHorse || this.template.isBlackOrWhite) extra1 = this.template.getColourCode(this.status);  if (isReborn()) { corpse.setDamage(20.0F); corpse.setButchered(); corpse.setAllData(this.template.getTemplateId(), 1, extra1, extra2); } else { corpse.setAllData(this.template.getTemplateId(), (getStatus()).fat << 1, extra1, extra2); }  if (isUnique()) { Server.getInstance().broadCastNormal(getNameWithGenus() + " has been slain."); if (!Servers.localServer.EPIC && !isReborn()) { try { boolean drop = false; if (isDragon()) { drop = (Server.rand.nextInt(10) == 0); } else { drop = Server.rand.nextBoolean(); }  if (drop) { int item = 795 + Server.rand.nextInt(16); if (item == 1009) { item = 807; } else if (item == 805) { item = 808; }  Item epicItem = ItemFactory.createItem(item, (60 + Server.rand.nextInt(20)), ""); epicItem.setOwnerId(corpse.getWurmId()); epicItem.setLastOwnerId(corpse.getWurmId()); if (isDragon()) epicItem.setAuxData((byte)2);  logger.info("Dropping a " + epicItem.getName() + " (" + epicItem.getWurmId() + ")  for the slaying of " + corpse.getName()); corpse.insertItem(epicItem); }  } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, "No template for item id 866"); } catch (FailedException fe) { logger.log(Level.WARNING, fe.getMessage() + ":" + '͢'); }  } else if (Servers.localServer.EPIC && !Servers.localServer.HOMESERVER) { if (isDragon()) try { boolean dropLoot = Server.rand.nextBoolean(); if (dropLoot) { int lootId = CreatureTemplateCreator.getDragonLoot(this.template.getTemplateId()); if (lootId > 0) { Item loot = ItemFactory.createItem(lootId, (60 + Server.rand.nextInt(20)), ""); logger.info("Dropping a " + loot.getName() + " (" + loot.getWurmId() + ") for the slaying of " + corpse.getName()); corpse.insertItem(loot); loot.setOwnerId(corpse.getWurmId()); }  }  } catch (Exception exception) {}  }  }  if (isPlayer() && !wasHunted && getReputation() >= 0 && !isInPvPZone() && Servers.localServer.KINGDOM != 0 && !isOnHostileHomeServer()) { boolean killedInVillageWar = false; if (this.attackers != null) for (Long l : this.attackers.keySet()) { try { Creature c = Creatures.getInstance().getCreature(l.longValue()); if (c.getCitizenVillage() != null && c.getCitizenVillage().isEnemy(this)) if (Servers.isThisAPvpServer()) { logger.log(Level.INFO, getName() + " was killed by " + c.getName() + " during village war. May be looted."); killedInVillageWar = true; }   } catch (Exception exception) {} }   if (!killedInVillageWar) corpse.setProtected(true);  }  corpse.setAuxData(getKingdomId()); corpse.setWeight((int)Math.min(50000.0F, this.status.body.getWeight(this.status.fat)), false); corpse.setLastOwnerId(getWurmId()); if (isKingdomGuard()) corpse.setDamage(50.0F);  if (getSex() == 1) corpse.setFemale(true);  corpse.setDescription(corpseDescription); if (!isPlayer() && !Servers.isThisAPvpServer()) { Brand brand = Creatures.getInstance().getBrand(getWurmId()); if (brand != null) try { corpse.setWasBrandedTo(brand.getBrandId()); PermissionsPlayerList allowedList = getPermissionsPlayerList(); PermissionsByPlayer[] pbpList = allowedList.getPermissionsByPlayer(); byte bito = ItemSettings.CorpsePermissions.COMMANDER.getBit(); int valueo = ItemSettings.CorpsePermissions.COMMANDER.getValue(); byte bitx = ItemSettings.CorpsePermissions.EXCLUDE.getBit(); int valuex = ItemSettings.CorpsePermissions.EXCLUDE.getValue(); Village bVill = null; for (PermissionsByPlayer pbp : pbpList) { if (pbp.getPlayerId() == -60L) { if (bVill == null) bVill = Villages.getVillage((int)brand.getBrandId());  int value = 0; if (pbp.hasPermission(bito)) value += valueo;  if (pbp.hasPermission(bitx)) value += valuex;  if (value != 0) for (Citizen citz : bVill.getCitizens()) { if (citz.isPlayer() && citz.getRole().mayBrand()) ItemSettings.addPlayer(corpse.getWurmId(), citz.wurmId, value);  }   }  }  for (PermissionsByPlayer pbp : pbpList) { if (pbp.getPlayerId() != -60L) { int value = 0; if (pbp.hasPermission(bito)) value += valueo;  if (pbp.hasPermission(bitx)) value += valuex;  if (value != 0) ItemSettings.addPlayer(corpse.getWurmId(), pbp.getPlayerId(), value);  }  }  } catch (NoSuchVillageException e) { Creatures.getInstance().setBrand(getWurmId(), -10L); }   }  VolaTile vvtile = Zones.getOrCreateTile(tilex, tiley, isOnSurface()); vvtile.addItem(corpse, false, getWurmId(), false); } else if (isGhost() || this.template.isNoCorpse()) { int[] butcheredItems = getTemplate().getItemsButchered(); for (int j = 0; j < butcheredItems.length; j++) { try { ItemFactory.createItem(butcheredItems[j], 20.0F + Server.rand.nextFloat() * 80.0F, getPosX(), getPosY(), (Server.rand.nextInt() * 360), isOnSurface(), (byte)0, getStatus().getBridgeId(), getName()); } catch (FailedException fe) { logger.log(Level.WARNING, fe.getMessage()); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, nst.getMessage()); }  }  }  VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, isOnSurface()); boolean keepItems = isTransferring(); if (!isOnCurrentServer()) keepItems = true;  if (getDeity() != null && getDeity().isDeathItemProtector() && getFaith() >= 70.0F && getFavor() >= 35.0F) { float chance = 0.35F; String successMessage = getDeity().getName() + " is with you and keeps your items safe."; String failMessage = getDeity().getName() + " could not keep your items safe this time."; float rand = Server.rand.nextFloat(); if (isDeathProtected()) { chance = 0.5F; if (rand > 0.35F && rand <= chance) { successMessage = getDeity().getName() + " could not keep your items safe this time, but ethereal strands of web attach to your items and keep them safe, close to your spirit!"; } else { failMessage = getDeity().getName() + " could not keep your items safe this time."; }  }  if (rand <= chance) { getCommunicator().sendNormalServerMessage(successMessage); keepItems = true; } else { getCommunicator().sendNormalServerMessage(failMessage); }  } else if (isDeathProtected()) { if (Server.rand.nextInt(2) > 0) { getCommunicator().sendNormalServerMessage("Ethereal strands of web attach to your items and keep them safe, close to your spirit!"); keepItems = true; } else { getCommunicator().sendNormalServerMessage("Your items could not be kept safe this time."); }  }  if (isPlayer()) try { Item legs = getBody().getBodyPart(19); boolean found = false; Set<Item> worn = legs.getItems(); if (worn != null) for (Item w : worn) { if (w.isArmour()) { found = true; break; }  }   if (!found) pantLess.add(Long.valueOf(getWurmId()));  } catch (NoSpaceException noSpaceException) {}  boolean insertItem = true; boolean dropNewbieItems = false; if (this.attackers != null) for (Long cid : this.attackers.keySet()) { if (WurmId.getType(cid.longValue()) == 0) if (!Servers.localServer.isChallengeServer() || getPlayingTime() > 86400000L) { dropNewbieItems = true; break; }   }   Item inventory = getInventory(); Item[] invarr = inventory.getAllItems(true); for (int x = 0; x < invarr.length; x++) { if (invarr[x].isTraded()) if (getTrade() != null) invarr[x].getTradeWindow().removeItem(invarr[x]);   boolean destroyChall = false; if (Features.Feature.FREE_ITEMS.isEnabled()) if (invarr[x].isChallengeNewbieItem()) if (invarr[x].isArmour() || invarr[x].isWeapon() || invarr[x].isShield()) destroyChall = true;    if (destroyChall) { Items.destroyItem(invarr[x].getWurmId()); } else if (invarr[x].isArtifact() || (!keepItems && !invarr[x].isNoDrop() && (!invarr[x].isNewbieItem() || dropNewbieItems || (invarr[x].isHollow() && !invarr[x].isTent())))) { try { Item parent = invarr[x].getParent(); if (inventory.equals(parent) || parent.getTemplateId() == 824) { parent.dropItem(invarr[x].getWurmId(), true); invarr[x].setBusy(false); if (corpse == null || !corpse.insertItem(invarr[x], true)) if (invarr[x].isTent() && invarr[x].isNewbieItem()) { Items.destroyItem(invarr[x].getWurmId()); } else { vtile.addItem(invarr[x], false, false); }   }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " " + invarr[x].getName() + ":" + nsi.getMessage(), (Throwable)nsi); }  } else if (!invarr[x].isArtifact() && !keepItems) { try { Item parent = invarr[x].getParent(); invarr[x].setBusy(false); insertItem = !parent.isNoDrop(); if (invarr[x].getTemplateId() == 443) if (!((getStrengthSkill() > 21.0D) ? 1 : 0) && !((getFaith() > 35.0F) ? 1 : 0)) { insertItem = false; if (!invarr[x].setDamage(invarr[x].getDamage() + 0.3F, true)) insertItem = true;  }   if (insertItem) { parent.dropItem(invarr[x].getWurmId(), false); inventory.insertItem(invarr[x], true); }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " " + invarr[x].getName() + ":" + nsi.getMessage(), (Throwable)nsi); }  }  }  Item[] boditems = getBody().getContainersAndWornItems(); for (int i = 0; i < boditems.length; i++) { if (boditems[i].isTraded()) if (getTrade() != null) boditems[i].getTradeWindow().removeItem(boditems[i]);   if (boditems[i].isArtifact() || (!keepItems && !boditems[i].isNoDrop() && (!boditems[i].isNewbieItem() || dropNewbieItems || (boditems[i].isHollow() && !boditems[i].isTent())))) { if (boditems[i].isHollow()) { Item[] containedItems = boditems[i].getAllItems(false); for (Item lContainedItem : containedItems) { if (lContainedItem.isNoDrop() || (lContainedItem.isNewbieItem() && !dropNewbieItems && !lContainedItem.isHollow())) try { lContainedItem.setBusy(false); Item parent = lContainedItem.getParent(); parent.dropItem(lContainedItem.getWurmId(), false); inventory.insertItem(lContainedItem, true); } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + ":" + nsi.getMessage(), (Throwable)nsi); }   }  }  try { Item parent = boditems[i].getParent(); parent.dropItem(boditems[i].getWurmId(), true); boditems[i].setBusy(false); if (corpse == null || !corpse.insertItem(boditems[i], true)) if (boditems[i].isTent() && boditems[i].isNewbieItem()) { Items.destroyItem(invarr[i].getWurmId()); } else { vtile.addItem(boditems[i], false, false); }   } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + ":" + nsi.getMessage(), (Throwable)nsi); }  } else if (!boditems[i].isArtifact() && !keepItems) { try { Item parent = boditems[i].getParent(); boditems[i].setBusy(false); insertItem = !parent.isNoDrop(); if (boditems[i].getTemplateId() == 443) if (!((getStrengthSkill() > 21.0D) ? 1 : 0) && !((getFaith() > 35.0F) ? 1 : 0)) { insertItem = false; if (!boditems[i].setDamage(boditems[i].getDamage() + 0.3F, true)) insertItem = true;  }   if (insertItem) { parent.dropItem(boditems[i].getWurmId(), false); inventory.insertItem(boditems[i], true); }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " " + boditems[i].getName() + ":" + nsi.getMessage(), (Throwable)nsi); }  }  }  } catch (FailedException fe) { logger.log(Level.WARNING, getName() + ":" + fe.getMessage(), (Throwable)fe); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, getName() + ":" + nst.getMessage(), (Throwable)nst); }  if (corpse != null) { if (isSuiciding()) if ((corpse.getAllItems(true)).length == 0) { Items.destroyItem(corpse.getWurmId()); corpse = null; }   } else { playAnimation("die", false); }  try { setBridgeId(-10L); getBody().healFully(); } catch (Exception ex) { logger.log(Level.WARNING, getName() + ex.getMessage(), ex); }  if (isTransferring() || !isOnCurrentServer()) return;  if (getTemplateId() == 78 || getTemplateId() == 79 || getTemplateId() == 80 || getTemplateId() == 81 || getTemplateId() == 68) EpicServerStatus.avatarCreatureKilled(getWurmId());  setDeathEffects(freeDeath, tilex, tiley); if (EpicServerStatus.doesTraitorMissionExist(getWurmId())) EpicServerStatus.traitorCreatureKilled(getWurmId());  } private void distributeDragonScaleOrHide(Set<Player> primeLooters, Set<Player> leecher, int lootTemplate) throws NoSuchTemplateException { ItemTemplate itemt = ItemTemplateFactory.getInstance().getTemplate(lootTemplate); float lootNums = calculateDragonLootMultiplier(); float totalWeightToDistribute = calculateDragonLootTotalWeight(itemt, lootNums) * ((lootTemplate == 371) ? 3.0F : 1.0F); float leecherShare = 0.0F; if (leecher.size() > 0) leecherShare = totalWeightToDistribute / 5.0F;  float primeShare = totalWeightToDistribute - leecherShare; if (leecher.size() > 0) { float lSplit = leecherShare / leecher.size(); float pSplit = primeShare / primeLooters.size(); if (lSplit > pSplit) { leecherShare = pSplit * 0.9F * leecher.size(); primeShare = totalWeightToDistribute - leecherShare; }  }  splitDragonLootTo(primeLooters, itemt, lootTemplate, primeShare); splitDragonLootTo(leecher, itemt, lootTemplate, leecherShare); } private final float calculateDragonLootTotalWeight(ItemTemplate template, float lootMult) { return 1.0F + template.getWeightGrams() * lootMult; } private final float calculateDragonLootMultiplier() { float lootNums = 1.0F; if (!Servers.isThisAnEpicServer()) lootNums = Math.max(1.0F, 1.0F + Server.rand.nextFloat() * 3.0F);  return lootNums; } private void splitDragonLootTo(Set<Player> lootReceivers, ItemTemplate itemt, int lootTemplate, float totalWeight) { if (lootReceivers.size() == 0) return;  float receivers = lootReceivers.size(); float weight = totalWeight / receivers; for (Player p : lootReceivers) { try { double power = 0.0D; try { Skill butchering = p.getSkills().getSkill(10059); power = Math.max(0.0D, butchering.skillCheck(10.0D, 0.0D, false, 10.0F)); } catch (NoSuchSkillException nss) { Skill butchering = p.getSkills().learn(10059, 1.0F); power = Math.max(0.0D, butchering.skillCheck(10.0D, 0.0D, false, 10.0F)); }  Item loot = ItemFactory.createItem(lootTemplate, (float)(80.0D + power / 5.0D), ""); String creatureName = getTemplate().getName().toLowerCase(); if (!loot.getName().contains(creatureName)) loot.setName(creatureName.toLowerCase() + " " + itemt.getName());  loot.setData2(this.template.getTemplateId()); loot.setWeight((int)weight, true); p.getInventory().insertItem(loot); lootReceivers.add(p); } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, p.getName() + " No template for item id " + lootTemplate); } catch (FailedException fe) { logger.log(Level.WARNING, p.getName() + " " + fe.getMessage() + ":" + lootTemplate); }  }  } public boolean isSuiciding() { return false; } public Item[] getAllItems() { Set<Item> allitems = new HashSet<>(); Item inventory = getInventory(); allitems.add(inventory); Item body = getBody().getBodyItem(); allitems.add(body); Item[] boditems = body.getAllItems(true); for (Item lBoditem : boditems) allitems.add(lBoditem);  Item[] invitems = inventory.getAllItems(true); for (Item lInvitem : invitems) allitems.add(lInvitem);  return allitems.<Item>toArray(new Item[allitems.size()]); } public void checkWorkMusic() { if (this.musicPlayer != null) this.musicPlayer.checkMUSIC_VILLAGEWORK_SND();  } public boolean isFightingSpiritGuard() { return (this.opponent != null && this.opponent.isSpiritGuard()); } public boolean isFighting(long opponentid) { return (this.opponent != null && this.opponent.getWurmId() == opponentid); } public void setFleeCounter(int newCounter) { setFleeCounter(newCounter, false); } public void setFleeCounter(int newCounter, boolean warded) { if (newCounter <= 0 || newCounter < this.fleeCounter) return;  if ((!isPlayer() && !isUnique() && (!isDominated() || warded)) || isPrey()) if (warded || isPrey()) { this.fleeCounter = (byte)newCounter; sendToLoggers("updated flee counter: " + this.fleeCounter); }   } public void setTarget(long targ, boolean switchTarget) { if (targ == getWurmId()) targ = -10L;  if (isPrey()) return;  if (targ != -10L && getVehicle() != -10L) try { Creature cret = Server.getInstance().getCreature(this.target); if (cret.getHitched() != null) { Vehicle v = Vehicles.getVehicleForId(getVehicle()); if (v != null && v == cret.getHitched()) { getCommunicator().sendNormalServerMessage("You cannot target " + cret.getName() + " while on the same vehicle."); targ = -10L; }  }  } catch (NoSuchPlayerException|NoSuchCreatureException noSuchPlayerException) {}  if (this.loggerCreature1 != -10L) logger.log(Level.FINE, getName() + " target=" + targ, new Exception());  if (targ == -10L) { getCommunicator().sendCombatStatus(0.0F, 0.0F, (byte)0); if (this.opponent != null && this.opponent.getWurmId() == this.target) setOpponent(null);  if (this.target != targ) try { Creature cret = Server.getInstance().getCreature(this.target); cret.getCommunicator().changeAttitude(getWurmId(), getAttitude(cret)); } catch (NoSuchCreatureException noSuchCreatureException) {  } catch (NoSuchPlayerException noSuchPlayerException) {}  this.target = targ; getCommunicator().sendTarget(targ); VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface()); if (t != null) t.sendUpdateTarget(this);  this.status.sendStateString(); } else if (this.target == -10L || switchTarget) { if (this.target != targ && (getBaseCombatRating() > 10.0F || this.fleeCounter <= 0)) { if (this.target != -10L) try { Creature cret = Server.getInstance().getCreature(this.target); cret.getCommunicator().changeAttitude(getWurmId(), getAttitude(cret)); } catch (NoSuchCreatureException noSuchCreatureException) {  } catch (NoSuchPlayerException noSuchPlayerException) {}  try { Creature cret = Server.getInstance().getCreature(targ); if (isSpiritGuard() && this.citizenVillage != null) { VolaTile currTile = cret.getCurrentTile(); if (currTile.getTileX() < this.citizenVillage.getStartX() - 5 || currTile.getTileX() > this.citizenVillage.getEndX() + 5 || currTile.getTileY() < this.citizenVillage.getStartY() - 5 || currTile.getTileY() > this.citizenVillage.getEndY() + 5) { if (cret.opponent == this) { cret.setOpponent(null); cret.setTarget(-10L, true); cret.getCommunicator().sendNormalServerMessage("The " + getName() + " suddenly becomes hazy and hard to target."); }  targ = -10L; setOpponent(null); if (this.status.getPath() == null) getMoveTarget(0);  } else { this.citizenVillage.cryForHelp(this, false); }  }  if (targ != -10L) cret.getCommunicator().changeAttitude(getWurmId(), getAttitude(cret));  } catch (NoSuchCreatureException noSuchCreatureException) {  } catch (NoSuchPlayerException noSuchPlayerException) {} this.target = targ; getCommunicator().sendTarget(targ); VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface()); if (t != null) t.sendUpdateTarget(this);  this.status.sendStateString(); }  }  } public boolean modifyFightSkill(int dtilex, int dtiley) { boolean pvp = false; Map<Creature, Double> lSkillReceivers = null; boolean activatedTrigger = false; if (!isNoSkillgain()) { lSkillReceivers = new HashMap<>(); long now = System.currentTimeMillis(); double kskill = 0.0D; double sumskill = 0.0D; boolean wasHelped = false; if (this.attackers != null && this.attackers.size() > 0) { ArrayList<Long> possibleTriggerOwners = new ArrayList<>(); for (null = this.attackers.keySet().iterator(); null.hasNext(); ) { long l = ((Long)null.next()).longValue(); if (now - ((Long)this.attackers.get(Long.valueOf(l))).longValue() < 600000L && WurmId.getType(l) == 0 && (!isPlayer() || !Players.getInstance().isOverKilling(l, getWurmId()))) possibleTriggerOwners.add(Long.valueOf(l));  }  if (!possibleTriggerOwners.isEmpty()) try { Player player = Players.getInstance().getPlayer(possibleTriggerOwners.get(Server.rand.nextInt(possibleTriggerOwners.size()))); MissionTrigger[] trigs = MissionTriggers.getMissionTriggersWith(getTemplate().getTemplateId(), 491, getWurmId()); for (MissionTrigger t : trigs) { EpicMission em = EpicServerStatus.getEpicMissionForMission(t.getMissionRequired()); if (em != null) { EpicMissionEnum missionEnum = EpicMissionEnum.getMissionForType(em.getMissionType()); if (missionEnum != null && EpicMissionEnum.isMissionKarmaGivenOnKill(missionEnum)) { float karmaSplit = (missionEnum.getKarmaBonusDiffMult() * em.getDifficulty()); float karmaGained = karmaSplit / EpicServerStatus.getNumberRequired(em.getDifficulty(), missionEnum); karmaGained = (float)Math.ceil((karmaGained / possibleTriggerOwners.size())); for (Iterator<Long> iterator = possibleTriggerOwners.iterator(); iterator.hasNext(); ) { long id = ((Long)iterator.next()).longValue(); try { Player p = Players.getInstance().getPlayer(id); if (Deities.getFavoredKingdom(em.getEpicEntityId()) != p.getKingdomTemplateId() && Servers.localServer.EPIC == true) continue;  MissionPerformer mp = MissionPerformed.getMissionPerformer(id); if (mp == null) { mp = MissionPerformed.startNewMission(t.getMissionRequired(), id, 1.0F); } else { MissionPerformed mperf = mp.getMission(t.getMissionRequired()); if (mperf == null) MissionPerformed.startNewMission(t.getMissionRequired(), id, 1.0F);  }  p.modifyKarma((int)karmaGained); if (p.isPaying()) { p.setScenarioKarma((int)(p.getScenarioKarma() + karmaGained)); if (Servers.localServer.EPIC) { WcEpicKarmaCommand wcek = new WcEpicKarmaCommand(WurmId.getNextWCCommandId(), new long[] { p.getWurmId() }, new int[] { p.getScenarioKarma() }, em.getEpicEntityId()); wcek.sendToLoginServer(); }  }  } catch (NoSuchPlayerException noSuchPlayerException) {} }  }  }  }  MissionTriggers.activateTriggers((Creature)player, getTemplate().getTemplateId(), 491, getWurmId(), 1); activatedTrigger = true; } catch (NoSuchPlayerException noSuchPlayerException) {}  for (Map.Entry<Long, Long> entry : this.attackers.entrySet()) { long attackerId = ((Long)entry.getKey()).longValue(); long attackTime = ((Long)entry.getValue()).longValue(); if (now - attackTime < 600000L) { if (WurmId.getType(attackerId) == 0) { pvp = true; if (!isPlayer() || !Players.getInstance().isOverKilling(attackerId, getWurmId())) try { Player player = Players.getInstance().getPlayer(attackerId); if (!isDuelOrSpar((Creature)player)) { kskill = player.getFightingSkill().getRealKnowledge(); lSkillReceivers.put(player, new Double(kskill)); sumskill += kskill; }  if (!isPlayer() && !isSpiritGuard() && !isKingdomGuard()) if (player.isPlayer() && !player.isDead()) player.checkCoinAward(this.attackers.size() * (isBred() ? 20 : (isDomestic() ? 50 : 100)));   if (isChampion() && player.isPlayer()) if (getKingdomId() != player.getKingdomId() || player.isEnemyOnChaos(this)) { player.addTitle(Titles.Title.ChampSlayer); if (player.isChampion()) { player.modifyChampionPoints(30); Servers.localServer.createChampTwit(player.getName() + " slays " + getName() + " and gains 30 champion points"); }  }   } catch (NoSuchPlayerException noSuchPlayerException) {}  continue; }  try { Creature c = Creatures.getInstance().getCreature(attackerId); if (c.isDominated()) { kskill = c.getFightingSkill().getKnowledge(); lSkillReceivers.put(c, new Double(kskill)); sumskill += kskill; continue; }  if (c.isSpiritGuard() || c.isKingdomGuard()) if (!isPlayer()) wasHelped = true;   } catch (NoSuchCreatureException noSuchCreatureException) {} }  }  }  kskill = getFightingSkill().getRealKnowledge(); getFightingSkill().touch(); if (isPlayer() && kskill <= 10.0D) kskill = 0.0D;  if (!isPlayer()) { kskill = getBaseCombatRating(); kskill += getBonusCombatRating(); if (kskill > 2.0D) if (!isReborn() && !isUndead()) { kskill *= 5.0D; } else if (getTemplate().getTemplateId() == 69) { kskill *= 0.20000000298023224D; }   } else { getFightingSkill().setKnowledge(Math.max(1.0D, getFightingSkill().getKnowledge() - 0.25D), false); }  if (kskill > 0.0D) { if (!isSpiritGuard() && !isKingdomGuard() && !isWarGuard()) { HashSet<Creature> lootReceivers = new HashSet<>(); for (Map.Entry<Creature, Double> entry : lSkillReceivers.entrySet()) { Creature p = entry.getKey(); Double psk = entry.getValue(); double pskill = psk.doubleValue(); double percentSkillGained = pskill / sumskill; double diff = kskill - pskill; double lMod = 0.20000000298023224D; if (diff > 1.0D) { lMod = Math.sqrt(diff); } else if (diff < -1.0D) { lMod = kskill / pskill; }  if (!isPlayer()) { lMod /= Servers.localServer.isChallengeServer() ? 2.0D : 7.0D; if (pskill > 70.0D) { double tomax = 100.0D - pskill; double modifier = tomax / (Servers.localServer.isChallengeServer() ? 30.0F : 500.0F); lMod *= modifier; }  if (wasHelped) lMod *= 0.10000000149011612D;  } else if (pskill > 50.0D && kskill < 20.0D) { lMod = 0.0D; } else if (getKingdomId() == p.getKingdomId()) { lMod = 0.0D; }  if (kskill <= 0.0D) lMod = 0.0D;  double skillGained = percentSkillGained * lMod * 0.25D * ItemBonus.getKillEfficiencyBonus(p); if (skillGained > 0.0D) { p.getFightingSkill().touch(); if (p.isPaying() || pskill < 20.0D) { if (pskill + skillGained > 100.0D) { p.getFightingSkill().setKnowledge(pskill + (100.0D - pskill) / 100.0D, false); } else { p.getFightingSkill().setKnowledge(pskill + skillGained, false); }  p.getFightingSkill().maybeSetMinimum(); }  p.getFightingSkill().checkInitialTitle(); }  if (!isPlayer()) { if (p.isPlayer()) { p.achievement(522); if (p.isUndead()) { (((Player)p).getSaveFile()).undeadKills++; ((Player)p).getSaveFile().setUndeadData(); p.achievement(335); }  if (isUnique()) HistoryManager.addHistory(p.getName(), "slayed " + getName());  int tid = getTemplate().getTemplateId(); try { if (CreatureTemplate.isDragon(tid)) { ((Player)p).addTitle(Titles.Title.DragonSlayer); } else if (tid == 11 || tid == 27) { ((Player)p).addTitle(Titles.Title.TrollSlayer); } else if (tid == 20 || tid == 22) { ((Player)p).addTitle(Titles.Title.GiantSlayer); } else if (isUnique()) { ((Player)p).addTitle(Titles.Title.UniqueSlayer); }  } catch (Exception ex) { logger.log(Level.WARNING, getName() + " and " + p.getName() + ":" + ex.getMessage()); }  switch (this.status.modtype) { case 1: p.achievement(253); break;case 2: p.achievement(254); break;case 3: p.achievement(255); break;case 4: p.achievement(256); break;case 5: p.achievement(257); break;case 6: p.achievement(258); break;case 7: p.achievement(259); break;case 8: p.achievement(260); break;case 9: p.achievement(261); break;case 10: p.achievement(262); break;case 11: p.achievement(263); break;case 99: p.achievement(264); break; }  if (tid == 58) { p.achievement(225); } else if (tid == 21 || tid == 118) { p.achievement(228); } else if (tid == 25) { p.achievement(231); } else if (tid == 11) { p.achievement(235); } else if (tid == 10) { p.achievement(237); } else if (tid == 54) { p.achievement(239); } else if (tid == 56) { p.achievement(243); } else if (tid == 57) { p.achievement(244); } else if (tid == 55) { p.achievement(265); } else if (tid == 43) { p.achievement(268); } else if (tid == 42 || tid == 12) { p.achievement(269); } else if (CreatureTemplate.isFullyGrownDragon(tid)) { p.achievement(270); } else if (CreatureTemplate.isDragonHatchling(tid)) { p.achievement(271); } else if (tid == 20) { p.achievement(272); } else if (tid == 23) { p.achievement(273); } else if (tid == 27) { p.achievement(274); } else if (tid == 68) { p.achievement(276); } else if (tid == 70) { p.achievement(277); } else if (tid == 71) { p.achievement(278); } else if (tid == 72) { p.achievement(279); } else if (tid == 73) { p.achievement(280); } else if (tid == 74) { p.achievement(281); } else if (tid == 75) { p.achievement(282); } else if (tid == 76) { p.achievement(283); } else if (tid == 77) { p.achievement(284); } else if (tid == 78) { p.achievement(285); } else if (tid == 79) { p.achievement(286); } else if (tid == 80) { p.achievement(287); } else if (tid == 81) { p.achievement(288); } else if (tid == 82) { p.achievement(289); } else if (tid == 83 || tid == 117) { p.achievement(291); } else if (tid == 84) { p.achievement(290); } else if (tid == 85) { p.achievement(292); } else if (tid == 59) { p.achievement(313); } else if (tid == 15) { p.achievement(314); } else if (tid == 14) { p.achievement(315); } else if (tid == 13) { p.achievement(316); } else if (tid == 22) { p.achievement(307); } else if (tid == 26) { p.achievement(308); } else if (tid == 64 || tid == 65) { p.achievement(309); } else if (tid == 49 || tid == 3 || tid == 50) { p.achievement(310); } else if (tid == 44) { p.achievement(311); } else if (tid == 51) { p.achievement(312); } else if (tid == 106) { p.achievement(378); } else if (tid == 107) { p.achievement(379); } else if (tid == 108) { p.achievement(380); } else if (tid == 109) { p.achievement(381); }  if (isDefendKingdom() && !isFriendlyKingdom(p.getKingdomId())) p.achievement(275);  if (isReborn()) p.achievement(248);  if (isUnique()) p.achievement(589);  }  } else if (isKing() && p.isPlayer()) { if (p.getKingdomId() != getKingdomId()) { ((Player)p).addTitle(Titles.Title.Kingslayer); HistoryManager.addHistory(p.getName(), "slayed " + getName()); }  }  if (isPlayer() && p.isPlayer() && !isUndead()) { if (p.isUndead()) { (((Player)p).getSaveFile()).undeadPlayerKills++; ((Player)p).getSaveFile().setUndeadData(); p.achievement(339); }  logger.log(Level.INFO, p.getName() + " killed " + getName() + " as champ=" + p.isChampion() + ". Diff=" + diff + " mod=" + lMod + " skillGained=" + skillGained + " pskill=" + pskill + " kskill=" + kskill); if (skillGained > 0.0D) { p.achievement(8); Item weapon = p.getPrimWeapon(); if (weapon != null) { if (weapon.isWeaponBow()) { p.achievement(11); } else if (weapon.isWeaponSword()) { p.achievement(14); } else if (weapon.isWeaponCrush()) { p.achievement(17); } else if (weapon.isWeaponAxe()) { p.achievement(20); } else if (weapon.isWeaponKnife()) { p.achievement(25); }  if (weapon.getTemplateId() == 314) { p.achievement(27); } else if (weapon.getTemplateId() == 567) { p.achievement(29); } else if (weapon.getTemplateId() == 20) { p.achievement(30); }  }  Item[] bodyItems = p.getBody().getAllItems(); int clothArmourFound = 0; int dragonPiecesFound = 0; for (Item i : bodyItems) { if (i.isArmour()) if (i.isCloth()) { clothArmourFound++; } else if (i.isDragonArmour()) { if (i.getTemplateId() == 476 || i.getTemplateId() == 475) dragonPiecesFound++;  }   }  if (clothArmourFound >= 8) p.achievement(31);  if (dragonPiecesFound >= 2) p.achievement(32);  if (pantLess.contains(Long.valueOf(getWurmId()))) achievement(33);  }  }  if (isPlayer() && kskill > 40.0D && lMod > 0.0D && p.isChampion()) { PlayerKills pk = Players.getInstance().getPlayerKillsFor(p.getWurmId()); if (System.currentTimeMillis() - pk.getLastKill(getWurmId()) > 86400000L) if (pk.getNumKills(getWurmId()) < 10L) { p.modifyChampionPoints(1); Servers.localServer.createChampTwit(p.getName() + " slays " + getName() + " and gains 1 champion point because of difficulty"); }   }  }  getTemplate().getLootTable().ifPresent(t -> t.awardAll(this, lootReceivers)); } else { for (Map.Entry<Creature, Double> entry : lSkillReceivers.entrySet()) { Creature p = entry.getKey(); if (p.isPlayer()) if (!isFriendlyKingdom(p.getKingdomId())) if (isSpiritGuard()) p.achievement(267);    }  }  } else { for (Map.Entry<Creature, Double> entry : lSkillReceivers.entrySet()) { Creature p = entry.getKey(); if (p.isPlayer()) if (!isFriendlyKingdom(p.getKingdomId())) if (isSpiritGuard()) p.achievement(267);    }  }  } else if (!isUndead()) { if (this.attackers != null && this.attackers.size() > 0) { ArrayList<Long> possibleTriggerOwners = new ArrayList<>(); for (null = this.attackers.keySet().iterator(); null.hasNext(); ) { long l = ((Long)null.next()).longValue(); if (WurmId.getType(l) == 0 && (!isPlayer() || !Players.getInstance().isOverKilling(l, getWurmId()))) possibleTriggerOwners.add(Long.valueOf(l));  }  if (!possibleTriggerOwners.isEmpty()) try { Player player = Players.getInstance().getPlayer(possibleTriggerOwners.get(Server.rand.nextInt(possibleTriggerOwners.size()))); MissionTriggers.activateTriggers((Creature)player, getTemplate().getTemplateId(), 491, getWurmId(), 1); MissionTrigger[] trigs = MissionTriggers.getMissionTriggersWith(getTemplate().getTemplateId(), 491, getWurmId()); for (MissionTrigger t : trigs) { EpicMission em = EpicServerStatus.getEpicMissionForMission(t.getMissionRequired()); if (em != null) { EpicMissionEnum missionEnum = EpicMissionEnum.getMissionForType(em.getMissionType()); if (missionEnum != null && EpicMissionEnum.isMissionKarmaGivenOnKill(missionEnum)) { float karmaSplit = (missionEnum.getKarmaBonusDiffMult() * em.getDifficulty()); float karmaGained = karmaSplit / EpicServerStatus.getNumberRequired(em.getDifficulty(), missionEnum); karmaGained = (float)Math.ceil((karmaGained / possibleTriggerOwners.size())); for (Iterator<Long> iterator = possibleTriggerOwners.iterator(); iterator.hasNext(); ) { long id = ((Long)iterator.next()).longValue(); try { Player p = Players.getInstance().getPlayer(id); if (Deities.getFavoredKingdom(em.getEpicEntityId()) != p.getKingdomTemplateId() && Servers.localServer.EPIC == true) continue;  MissionPerformer mp = MissionPerformed.getMissionPerformer(id); if (mp == null) { mp = MissionPerformed.startNewMission(t.getMissionRequired(), id, 1.0F); } else { MissionPerformed mperf = mp.getMission(t.getMissionRequired()); if (mperf == null) MissionPerformed.startNewMission(t.getMissionRequired(), id, 1.0F);  }  p.modifyKarma((int)karmaGained); if (p.isPaying()) { p.setScenarioKarma((int)(p.getScenarioKarma() + karmaGained)); if (Servers.localServer.EPIC) { WcEpicKarmaCommand wcek = new WcEpicKarmaCommand(WurmId.getNextWCCommandId(), new long[] { p.getWurmId() }, new int[] { p.getScenarioKarma() }, em.getEpicEntityId()); wcek.sendToLoginServer(); }  }  } catch (NoSuchPlayerException noSuchPlayerException) {} }  }  }  }  activatedTrigger = true; } catch (NoSuchPlayerException noSuchPlayerException) {}  for (Map.Entry<Long, Long> entry : this.attackers.entrySet()) { long attackerId = ((Long)entry.getKey()).longValue(); if (WurmId.getType(attackerId) == 0) { pvp = true; try { Player player = Players.getInstance().getPlayer(attackerId); if (!isFriendlyKingdom(player.getKingdomId())) if (isKingdomGuard()) player.achievement(266);   } catch (NoSuchPlayerException noSuchPlayerException) {} }  }  }  }  pantLess.remove(Long.valueOf(getWurmId())); return (pvp || (lSkillReceivers != null && lSkillReceivers.size() > 0)); } @Nullable public Creature getTarget() { Creature toReturn = null; if (this.target != -10L) try { toReturn = Server.getInstance().getCreature(this.target); } catch (NoSuchCreatureException nsc) { setTarget(-10L, true); } catch (NoSuchPlayerException nsp) { setTarget(-10L, true); }   return toReturn; } public void setDeathEffects(boolean freeDeath, int dtilex, int dtiley) { boolean respawn = false; removeWoundMod(); modifyFightSkill(dtilex, dtiley); if (isSpiritGuard() && this.citizenVillage != null) { respawn = true; } else if (isKingdomGuard() || (isNpc() && isRespawn())) { respawn = true; }  if (respawn) { setDestroyed(); if (this.name.endsWith("traitor")) try { setName(getNameWithoutPrefixes()); } catch (Exception ex) { logger.log(Level.WARNING, getName() + ", " + getWurmId() + ": failed to remove traitor name."); }   try { this.status.setDead(true); } catch (IOException ioex) { logger.log(Level.WARNING, getName() + ", " + getWurmId() + ": Set dead manually."); }  if (isSpiritGuard()) { Village vil = this.citizenVillage; if (vil != null) { vil.deleteGuard(this, false); vil.plan.returnGuard(this); } else { destroy(); }  } else if (isKingdomGuard()) { GuardTower tower = Kingdoms.getTower(this); if (tower != null) { try { tower.returnGuard(this); } catch (IOException iox) { logger.log(Level.WARNING, iox.getMessage(), iox); }  } else { logger.log(Level.INFO, getName() + ", " + getWurmId() + " without tower, destroying."); destroy(); }  } else { this.respawnCounter = 600; }  } else { destroy(); }  getStatus().setStunned(0.0F, false); trimAttackers(true); } public void respawn() { if (getVisionArea() == null) { try { if (!isNpc()) if (this.skills.getSkill(10052).getKnowledge(0.0D) > this.template.getSkills().getSkill(10052).getKnowledge(0.0D) * 2.0D || 100.0D - this.skills.getSkill(10052).getKnowledge(0.0D) < 30.0D || this.skills.getSkill(10052).getKnowledge(0.0D) < this.template.getSkills().getSkill(10052).getKnowledge(0.0D) / 2.0D) { this.skills.delete(); this.skills.clone(this.template.getSkills().getSkills()); this.skills.save(); (getStatus()).age = 0; } else if ((getStatus()).age >= this.template.getMaxAge() - 1) { (getStatus()).age = 0; }   this.status.setDead(false); this.pollCounter = 0; this.lastPolled = 0; setDisease((byte)0); getStatus().removeWounds(); getStatus().modifyStamina(65535.0F); getStatus().refresh(0.5F, false); createVisionArea(); } catch (Exception ex) { logger.log(Level.WARNING, getName() + ":" + ex.getMessage(), ex); }  } else { logger.log(Level.WARNING, getName() + " already has a visionarea.", new Exception()); }  Server.getInstance().broadCastAction(getNameWithGenus() + " has arrived.", this, 10); } protected void decreaseOpportunityCounter() { if (this.opportunityAttackCounter > 0)
/*  5973 */       this.opportunityAttackCounter = (byte)(this.opportunityAttackCounter - 1);  }
/*       */   public boolean hasColoredChat() { return false; }
/*       */   public int getCustomGreenChat() { return 140; }
/*       */   public int getCustomRedChat() { return 255; }
/*       */   public int getCustomBlueChat() { return 0; }
/*       */   public final boolean isFaithful() { return this.faithful; }
/*       */   public boolean isFighting() { return (this.opponent != null); }
/*       */   public MovementScheme getMovementScheme() { return this.movementScheme; }
/*       */   public boolean isOnGround() { return this.movementScheme.onGround; }
/*       */   public void pollStamina() { this.staminaPollCounter = Math.max(0, --this.staminaPollCounter); if (this.staminaPollCounter == 0) if (!isUndead() && WurmId.getType(this.id) == 0) { int hunger, hungMod = 4; int thirstMod = (int)(5.0F * ItemBonus.getReplenishBonus(this)); if (getSpellEffects() != null && getSpellEffects().getSpellEffect((byte)74) != null) { hungMod = 2; thirstMod = 2; }  hungMod = (int)(hungMod * ItemBonus.getReplenishBonus(this)); boolean reduceHunger = true; if (getDeity() != null && (getDeity()).number == 4) if (isOnSurface()) { int tile = Server.surfaceMesh.getTile(getTileX(), getTileY()); if (Tiles.getTile(Tiles.decodeType(tile)).isMycelium()) reduceHunger = false;  }   if (reduceHunger) { this.status.decreaseCCFPValues(); hunger = this.status.modifyHunger((int)(hungMod * (2.0F - this.status.getNutritionlevel())), 1.0F); } else { hunger = this.status.modifyHunger(-4, 0.99F); }  int thirst = this.status.modifyThirst(thirstMod); float hungpercent = 1.0F; if (hunger > 45000) { hungpercent = Math.max(1.0F, (65535 - hunger)) / 20535.0F; hungpercent *= hungpercent; }  float thirstpercent = Math.max((65535 - thirst), 1.0F) / 65535.0F; thirstpercent = thirstpercent * thirstpercent * thirstpercent; if (this.status.hasNormalRegen() && !isFighting()) { float toModify = 0.6F; if (isStealth()) toModify = 0.06F;  toModify = toModify * hungpercent * thirstpercent; double staminaModifier = this.status.getModifierValuesFor(1); if (getDeity() != null && getDeity().isStaminaBonus() && getFaith() >= 20.0F && getFavor() >= 10.0F) staminaModifier += 0.25D;  if (this.hasSpiritStamina) staminaModifier *= 1.1D;  if (hasSleepBonus()) { toModify = Math.max(0.006F, toModify * (float)(1.0D + staminaModifier) * 3.0F); } else { toModify = Math.max(0.004F, toModify * (float)(1.0D + staminaModifier)); }  if (hasSpellEffect((byte)95)) toModify *= 0.5F;  if ((getPower() == 0 && getVehicle() == -10L && (getPositionZ() + getAltOffZ()) < -1.45D) || isUsingLastGasp()) { toModify = 0.0F; } else { this.status.modifyStamina2(toModify); }  }  this.status.setNormalRegen(true); } else { if (isNeedFood()) { if (Server.rand.nextInt(600) == 0) if (hasTrait(14) || isPregnant()) { this.status.modifyHunger(1500, 1.0F); } else if (!isCarnivore()) { this.status.modifyHunger(700, 1.0F); } else { this.status.modifyHunger(150, 1.0F); }   } else { this.status.modifyHunger(-1, 0.5F); }  if (isRegenerating() || isUnique()) if (Server.rand.nextInt(10) == 0) healTick();   if (Server.rand.nextInt(100) == 0) { if (!isFighting() || isUnique()) this.status.resetCreatureStamina();  if (!isSwimming() && !isUnique() && !isSubmerged()) if ((getPositionZ() + getAltOffZ()) <= -1.25D) if (getVehicle() == -10L && this.hitchedTo == null && !isRidden() && getLeader() == null) if (!Tiles.isSolidCave(Tiles.decodeType(getCurrentTileNum()))) addWoundOfType(null, (byte)7, 2, false, 1.0F, false, (4000.0F + Server.rand.nextFloat() * 3000.0F), 0.0F, 0.0F, false, false);     }  this.status.setNormalRegen(true); }   }
/*       */   public void sendDeityEffectBonuses() {}
/*       */   public void sendRemoveDeityEffectBonus(int effectNumber) {}
/*       */   public void sendAddDeityEffectBonus(int effectNumber) {}
/*  5986 */   public final boolean checkPregnancy(boolean insta) { Offspring offspring = Offspring.getOffspring(getWurmId()); if (offspring != null) if (!offspring.isChecked() || insta) if (Server.rand.nextInt(4) == 0 || insta) { float creatureRatio = 10.0F; if (getCurrentVillage() != null) creatureRatio = getCurrentVillage().getCreatureRatio();  if ((this.status.hunger > 60000 && this.status.fat <= 2) || (creatureRatio < Village.OPTIMUMCRETRATIO && Server.rand.nextInt(Math.max((int)(creatureRatio / 2.0F), 1)) == 0)) if (Server.rand.nextInt(3) == 0) { Offspring.deleteSettings(getWurmId()); getCommunicator().sendAlertServerMessage("You suddenly bleed immensely and lose your unborn child due to malnourishment!"); Server.getInstance().broadCastAction(getNameWithGenus() + " bleeds immensely due to miscarriage.", this, 5); if (Server.rand.nextInt(5) == 0) die(false, "Miscarriage");  return false; }   if (offspring.decreaseDaysLeft()) try { int cid = this.template.getChildTemplateId(); if (cid <= 0) cid = this.template.getTemplateId();  CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(cid); String newname = temp.getName(); byte sex = temp.keepSex ? temp.getSex() : (byte)Server.rand.nextInt(2); if (isHorse()) { if (Server.rand.nextBoolean()) { newname = Offspring.generateGenericName(); } else if (sex == 1) { newname = Offspring.generateFemaleName(); } else { newname = Offspring.generateMaleName(); }  newname = LoginHandler.raiseFirstLetter(newname); }  if (isUnicorn()) { if (Server.rand.nextBoolean()) { newname = Offspring.generateGenericName(); } else if (sex == 1) { newname = Offspring.generateFemaleUnicornName(); } else { newname = Offspring.generateMaleUnicornName(); }  newname = LoginHandler.raiseFirstLetter(newname); }  boolean zombie = false; if (cid == 66) { zombie = true; if (sex == 1) { newname = LoginHandler.raiseFirstLetter("Daughter of " + this.name); } else { newname = LoginHandler.raiseFirstLetter("Son of " + this.name); }  if (getKingdomTemplateId() != 3) { cid = 25; zombie = false; }  }  Creature newCreature = doNew(cid, true, getPosX(), getPosY(), Server.rand.nextFloat() * 360.0F, getLayer(), newname, sex, isAggHuman() ? getKingdomId() : 0, Server.rand.nextBoolean() ? (getStatus()).modtype : 0, zombie, (byte)1); getCommunicator().sendAlertServerMessage("You give birth to " + newCreature.getName() + "!"); newCreature.getStatus().setTraitBits(offspring.getTraits()); newCreature.getStatus().setInheritance(offspring.getTraits(), offspring.getMother(), offspring.getFather()); newCreature.getStatus().saveCreatureName(newname); if (zombie) { if (getPet() != null) { getCommunicator().sendNormalServerMessage(getPet().getNameWithGenus() + " stops following you."); if (getPet().getLeader() == this) getPet().setLeader(null);  getPet().setDominator(-10L); setPet(-10L); }  newCreature.setDominator(getWurmId()); newCreature.setLoyalty(100.0F); setPet(newCreature.getWurmId()); newCreature.getSkills().delete(); newCreature.getSkills().clone(this.skills.getSkills()); Skill[] cskills = newCreature.getSkills().getSkills(); for (Skill lCskill : cskills) lCskill.setKnowledge(Math.min(40.0D, lCskill.getKnowledge() * 0.5D), false);  newCreature.getSkills().save(); }  newCreature.refreshVisible(); Server.getInstance().broadCastAction(getNameWithGenus() + " gives birth to " + newCreature.getNameWithGenus() + "!", this, 5); return true; } catch (NoSuchCreatureTemplateException nst) { logger.log(Level.WARNING, getName() + " gives birth to nonexistant template:" + this.template.getChildTemplateId()); } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   }    return false; } private long getTraits() { return this.status.traits; } public void mate(Creature father, @Nullable Creature breeder) { boolean inbred = false; if (father.getFather() == getFather() || father.getMother() == getMother() || father.getWurmId() == getFather() || father.getMother() == getWurmId()) inbred = true;  new Offspring(getWurmId(), father.getWurmId(), (breeder == null) ? Traits.calcNewTraits(inbred, getTraits(), father.getTraits()) : Traits.calcNewTraits(breeder.getAnimalHusbandrySkillValue(), inbred, getTraits(), father.getTraits()), (byte)(this.template.daysOfPregnancy + Server.rand.nextInt(5)), false); logger.log(Level.INFO, getName() + " gender=" + getSex() + " just got pregnant with " + father.getName() + " gender=" + father.getSex() + "."); } public boolean isBred() { return hasTrait(63); } static boolean isInbred(Creature maleCreature, Creature femaleCreature) { return (maleCreature.getFather() == femaleCreature.getFather() || maleCreature.getMother() == femaleCreature.getMother() || maleCreature.getWurmId() == femaleCreature.getFather() || maleCreature.getMother() == femaleCreature.getWurmId()); } public boolean isPregnant() { return (getOffspring() != null); } public Offspring getOffspring() { return Offspring.getOffspring(getWurmId()); } private void healTick() { if (this.status.damage > 0) try { Wound[] w = getBody().getWounds().getWounds(); if (w.length > 0) w[0].modifySeverity(-300);  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   } public void wearItems() { Item inventory = getInventory(); Body body = getBody(); Set<Item> invitems = inventory.getItems(); Item[] invarr = invitems.<Item>toArray(new Item[invitems.size()]); for (Item lElement : invarr) { if (lElement.isWeapon() && (!isPlayer() || (lElement.getTemplateId() != 7 && !lElement.isWeaponKnife()))) { try { byte rslot = isPlayer() ? 38 : 14; Item bodyPart = body.getBodyPart(rslot); if (bodyPart.testInsertItem(lElement)) { Item parent = lElement.getParent(); parent.dropItem(lElement.getWurmId(), false); bodyPart.insertItem(lElement); } else { byte lslot = isPlayer() ? 37 : 13; bodyPart = body.getBodyPart(lslot); if (bodyPart.testInsertItem(lElement)) { Item parent = lElement.getParent(); parent.dropItem(lElement.getWurmId(), false); bodyPart.insertItem(lElement); }  }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " " + nsi.getMessage(), (Throwable)nsi); } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp); }  } else if (lElement.isShield()) { try { Item bodyPart = body.getBodyPart(44); bodyPart.insertItem(lElement); } catch (NoSpaceException e) { e.printStackTrace(); }  } else { byte[] places = lElement.getBodySpaces(); for (byte lPlace : places) { try { Item bodyPart = body.getBodyPart(lPlace); if (bodyPart.testInsertItem(lElement)) { Item parent = lElement.getParent(); parent.dropItem(lElement.getWurmId(), false); bodyPart.insertItem(lElement); break; }  } catch (NoSpaceException nsp) { if (!Servers.localServer.testServer && lPlace != 28) logger.log(Level.WARNING, getName() + ":" + nsp.getMessage(), (Throwable)nsp);  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + ":" + nsi.getMessage(), (Throwable)nsi); }  }  }  }  } public float getStaminaMod() { int hunger = this.status.getHunger(); int thirst = this.status.getThirst(); float newhungpercent = 1.0F; if (hunger > 45000) { newhungpercent = Math.max(1.0F, (65535 - hunger)) / 20535.0F; newhungpercent *= newhungpercent; }  float thirstpercent = Math.max((65535 - thirst), 1.0F) / 65535.0F; thirstpercent = thirstpercent * thirstpercent * thirstpercent; return 1.0F - newhungpercent * thirstpercent; } public Skills getSkills() { return this.skills; } public double getSoulStrengthVal() { return getSoulStrength().getKnowledge(0.0D); } public Skill getClimbingSkill() { try { return this.skills.getSkill(10073); } catch (NoSuchSkillException nss) { return this.skills.learn(10073, 1.0F); }  } public double getLockPickingSkillVal() { try { return this.skills.getSkill(10076).getKnowledge(0.0D); } catch (NoSuchSkillException nss) { return 1.0D; }  } public double getLockSmithingSkill() { try { return this.skills.getSkill(10034).getKnowledge(0.0D); } catch (NoSuchSkillException nss) { return 1.0D; }  } public double getStrengthSkill() { try { if (isPlayer()) return this.skills.getSkill(102).getKnowledge(0.0D);  return this.skills.getSkill(102).getKnowledge(); } catch (NoSuchSkillException nss) { return 1.0D; }  } public Skill getStealSkill() { try { return this.skills.getSkill(10075); } catch (NoSuchSkillException nss) { return this.skills.learn(10075, 1.0F); }  } public Skill getStaminaSkill() { try { return this.skills.getSkill(103); } catch (NoSuchSkillException nss) { return this.skills.learn(103, 1.0F); }  } public final double getAnimalHusbandrySkillValue() { try { return this.skills.getSkill(10085).getKnowledge(0.0D); } catch (NoSuchSkillException nss) { return this.skills.learn(10085, 1.0F).getKnowledge(0.0D); }  } public double getBodyControl() { try { return this.skills.getSkill(104).getKnowledge(0.0D); } catch (NoSuchSkillException nss) { return this.skills.learn(104, 1.0F).getKnowledge(0.0D); }  } public Skill getBodyControlSkill() { try { return this.skills.getSkill(104); } catch (NoSuchSkillException nss) { return this.skills.learn(104, 1.0F); }  } public Skill getFightingSkill() { if (!isPlayer()) return getWeaponLessFightingSkill();  try { return this.skills.getSkill(1023); } catch (NoSuchSkillException nss) { return this.skills.learn(1023, 1.0F); }  } public Skill getWeaponLessFightingSkill() { try { return this.skills.getSkill(10052); } catch (NoSuchSkillException nss) { try { return this.skills.learn(10052, (float)this.template.getSkills().getSkill(10052).getKnowledge(0.0D)); } catch (NoSuchSkillException nss2) { logger.log(Level.WARNING, "Template for " + getName() + " has no weaponless skill?"); return this.skills.learn(10052, 20.0F); } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage() + " template for " + getName() + " has skills?"); return this.skills.learn(10052, 20.0F); }  }  } public byte getAttitude(Creature aTarget) { if (this.opponent == aTarget) return 2;  if (aTarget.isNpc() && isNpc() && aTarget.getKingdomId() == getKingdomId()) return 1;  if (isDominated()) { if (getDominator() != null) { if (getDominator() == aTarget) return 1;  if (getDominator() == aTarget.getDominator()) return 1;  return aTarget.getAttitude(getDominator()); }  if (getLoyalty() > 0.0F) if ((aTarget.getReputation() >= 0 || aTarget.getKingdomTemplateId() == 3) && isFriendlyKingdom(aTarget.getKingdomId())) return 0;   }  if (aTarget.isDominated()) { Creature lDominator = aTarget.getDominator(); if (lDominator != null) { if (lDominator == this) return 1;  if (aTarget.isHorse() && aTarget.isRidden()) { if (isHungry() && isCarnivore()) { if (Server.rand.nextInt(5) == 0) for (Long riderLong : aTarget.getRiders()) { try { Creature rider = Server.getInstance().getCreature(riderLong.longValue()); if (getAttitude(rider) == 2) return 2;  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage()); }  }   return 0; }  } else { return getAttitude(lDominator); }  }  if (isFriendlyKingdom(aTarget.getKingdomId()) && aTarget.getLoyalty() > 0.0F) return 0;  }  if (getPet() != null) if (aTarget == getPet()) return 1;   if (isInvulnerable()) return 0;  if (aTarget.isInvulnerable()) return 0;  if (!isPlayer() && aTarget.getCultist() != null) { if (aTarget.getCultist().hasFearEffect()) return 0;  if (aTarget.getCultist().hasLoveEffect()) return 1;  }  if (isReborn() && !aTarget.equals(getTarget()) && !aTarget.equals(this.opponent) && aTarget.getKingdomId() == getKingdomId()) return 0;  if (onlyAttacksPlayers() && !aTarget.isPlayer()) return 0;  if (!isPlayer() && aTarget.onlyAttacksPlayers()) return 0;  if (Servers.isThisAChaosServer()) if (getCitizenVillage() != null && getCitizenVillage().isEnemy(aTarget)) return 2;   if (isAggHuman()) { if (aTarget instanceof Player) { boolean atta = true; if (isAnimal()) if (aTarget.getDeity() != null) if (aTarget.getDeity().isBefriendCreature() && aTarget.getFaith() > 60.0F && aTarget.getFavor() >= 30.0F) atta = false;    if (isMonster() && !isUnique()) if (aTarget.getDeity() != null) if (aTarget.getDeity().isBefriendMonster() && aTarget.getFaith() > 60.0F && aTarget.getFavor() >= 30.0F) atta = false;    if (getLoyalty() > 0.0F) if ((aTarget.getReputation() >= 0 || aTarget.getKingdomTemplateId() == 3) && isFriendlyKingdom(aTarget.getKingdomId())) atta = false;   if (atta) return 2;  } else if ((aTarget.isSpiritGuard() && aTarget.getCitizenVillage() == null) || aTarget.isKingdomGuard()) { if (getLoyalty() <= 0.0F && !isUnique() && (!isHorse() || !isRidden())) return 2;  } else if (aTarget.isRidden()) { if (isHungry() && isCarnivore()) if (Server.rand.nextInt(5) == 0) for (Long riderLong : aTarget.getRiders()) { try { Creature rider = Server.getInstance().getCreature(riderLong.longValue()); if (getAttitude(rider) == 2) return 2;  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage()); }  }    return 0; }  } else { if (aTarget.getKingdomId() != 0 && !isFriendlyKingdom(aTarget.getKingdomId()) && (isDefendKingdom() || (isAggWhitie() && aTarget.getKingdomTemplateId() != 3))) return 2;  if (isSpiritGuard()) { if (this.citizenVillage != null) { if (aTarget instanceof Player) { if (this.citizenVillage.isEnemy(aTarget.citizenVillage)) return 2;  if (this.citizenVillage.getReputation(aTarget) <= -30) return 2;  if (this.citizenVillage.isEnemy(aTarget)) return 2;  if (this.citizenVillage.isAlly(aTarget)) return 1;  if (this.citizenVillage.isCitizen(aTarget)) return 1;  if (!isFriendlyKingdom(aTarget.getKingdomId())) return 2;  return 0; }  if (aTarget.getKingdomId() != 0) { if (!isFriendlyKingdom(getKingdomId())) return 2;  return 0; }  if (aTarget.isRidden()) { for (Long riderLong : aTarget.getRiders()) { try { Creature rider = Server.getInstance().getCreature(riderLong.longValue()); if (!isFriendlyKingdom(rider.getKingdomId())) return 2;  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage()); }  }  return 0; }  }  } else if (isKingdomGuard()) { if (aTarget.getKingdomId() != 0) { if (!isFriendlyKingdom(aTarget.getKingdomId())) return 2;  if (aTarget.getKingdomTemplateId() != 3 && aTarget.getReputation() <= -100) return 2;  if (aTarget.isPlayer()) { Village lVill = Villages.getVillageWithPerimeterAt(getTileX(), getTileY(), true); if (lVill != null && lVill.kingdom == getKingdomId()) if (lVill.isEnemy(aTarget)) return 2;   }  } else if (aTarget.isAggHuman() && !aTarget.isUnique()) { if (aTarget.getCurrentKingdom() == getKingdomId()) if (aTarget.getLoyalty() <= 0.0F && !aTarget.isRidden()) return 2;   }  if (aTarget.isRidden()) for (Long riderLong : aTarget.getRiders()) { try { Creature rider = Server.getInstance().getCreature(riderLong.longValue()); if (getAttitude(rider) == 2) return 2;  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage()); }  }   }  }  if (isCarnivore() && aTarget.isPrey() && Server.rand.nextInt(10) == 0 && canEat()) if (aTarget.getCurrentVillage() == null && aTarget.getHitched() == null) return 2;   return 0; } public final byte getCurrentKingdom() { return Zones.getKingdom(getTileX(), getTileY()); } public boolean isFriendlyKingdom(byte targetKingdom) { if (getKingdomId() == 0 || targetKingdom == 0) return false;  if (getKingdomId() == targetKingdom) return true;  Kingdom myKingd = Kingdoms.getKingdom(getKingdomId()); if (myKingd != null) return myKingd.isAllied(targetKingdom);  return false; } public Possessions getPossessions() { return this.possessions; } public Item getInventory() { if (this.possessions != null) return this.possessions.getInventory();  logger.warning("Posessions was null for " + this.id); return null; } public Optional<Item> getInventoryOptional() { if (this.possessions != null) return Optional.ofNullable(this.possessions.getInventory());  logger.warning("Posessions was null for " + this.id); return Optional.empty(); } public static final Item createItem(int templateId, float qualityLevel) throws Exception { Item item = ItemFactory.createItem(templateId, qualityLevel, (byte)0, (byte)0, null); return item; } public void save() throws IOException { this.possessions.save(); this.status.save(); this.skills.save(); } public void savePosition(int zoneid) throws IOException { this.status.savePosition(this.id, false, zoneid, false); } public boolean isGuest() { return this.guest; } public void setGuest(boolean g) { this.guest = g; } public CreatureTemplate getTemplate() { return this.template; } public void refreshAttitudes() { if (this.visionArea != null) this.visionArea.refreshAttitudes();  if (this.currentTile != null) this.currentTile.checkChangedAttitude(this);  } public static Creature doNew(int templateid, byte ctype, float aPosX, float aPosY, float aRot, int layer, String name, byte gender) throws Exception { return doNew(templateid, true, aPosX, aPosY, aRot, layer, name, gender, (byte)0, ctype, false); } public static Creature doNew(int templateid, float aPosX, float aPosY, float aRot, int layer, String name, byte gender) throws Exception { return doNew(templateid, aPosX, aPosY, aRot, layer, name, gender, (byte)0); } public static Creature doNew(int templateid, float aPosX, float aPosY, float aRot, int layer, String name, byte gender, byte kingdom) throws Exception { return doNew(templateid, true, aPosX, aPosY, aRot, layer, name, gender, kingdom, (byte)0, false); } public static Creature doNew(int templateid, boolean createPossessions, float aPosX, float aPosY, float aRot, int layer, String name, byte gender, byte kingdom, byte ctype, boolean reborn) throws Exception { return doNew(templateid, createPossessions, aPosX, aPosY, aRot, layer, name, gender, kingdom, ctype, reborn, (byte)0); } public static Creature doNew(int templateid, boolean createPossessions, float aPosX, float aPosY, float aRot, int layer, String name, byte gender, byte kingdom, byte ctype, boolean reborn, byte age) throws Exception { return doNew(templateid, createPossessions, aPosX, aPosY, aRot, layer, name, gender, kingdom, ctype, reborn, age, 0); } public static Creature doNew(int templateid, boolean createPossessions, float aPosX, float aPosY, float aRot, int layer, String name, byte gender, byte kingdom, byte ctype, boolean reborn, byte age, int floorLevel) throws Exception { Creature toReturn = (!reborn && (templateid == 1 || templateid == 113)) ? new Npc(CreatureTemplateFactory.getInstance().getTemplate(templateid)) : new Creature(CreatureTemplateFactory.getInstance().getTemplate(templateid)); long wid = WurmId.getNextCreatureId(); try { while (Creatures.getInstance().getCreature(wid) != null) wid = WurmId.getNextCreatureId();  } catch (Exception exception) {} toReturn.setWurmId(wid, aPosX, aPosY, normalizeAngle(aRot), layer); if (name.length() > 0) toReturn.setName(name);  if (toReturn.getTemplate().isRoyalAspiration()) if (toReturn.getTemplate().getTemplateId() == 62) { kingdom = 1; } else if (toReturn.getTemplate().getTemplateId() == 63) { kingdom = 3; }   if (reborn) (toReturn.getStatus()).reborn = true;  if (floorLevel > 0) { toReturn.pushToFloorLevel(floorLevel); } else { toReturn.setPositionZ(toReturn.calculatePosZ()); }  if (age <= 0) { (toReturn.getStatus()).age = (int)(1.0F + Server.rand.nextFloat() * Math.min(48, toReturn.getTemplate().getMaxAge())); } else { (toReturn.getStatus()).age = age; }  if (toReturn.isGhost() || toReturn.isKingdomGuard() || reborn) (toReturn.getStatus()).age = 12;  if (ctype != 0) (toReturn.getStatus()).modtype = ctype;  if (toReturn.isUnique()) (toReturn.getStatus()).age = 12 + (int)(Server.rand.nextFloat() * (toReturn.getTemplate().getMaxAge() - 12));  (toReturn.getStatus()).kingdom = kingdom; if (Kingdoms.getKingdom(kingdom) != null && Kingdoms.getKingdom(kingdom).getTemplate() == 3) { toReturn.setAlignment(-50.0F); toReturn.setDeity(Deities.getDeity(4)); toReturn.setFaith(1.0F); }  toReturn.setSex(gender, true); Creatures.getInstance().addCreature(toReturn, false, false); toReturn.loadSkills(); toReturn.createPossessions(); toReturn.getBody().createBodyParts(); if (!toReturn.isAnimal()) if (createPossessions) { createBasicItems(toReturn); toReturn.wearItems(); }   if (toReturn.isHorse() || (toReturn.getTemplate()).isBlackOrWhite) if (Server.rand.nextInt(10) == 0) setRandomColor(toReturn);   Creatures.getInstance().sendToWorld(toReturn); toReturn.createVisionArea(); toReturn.save(); if (reborn) toReturn.getStatus().setReborn(true);  if (ctype != 0) toReturn.getStatus().setType(ctype);  toReturn.getStatus().setKingdom(kingdom); if (kingdom == 3) { toReturn.setAlignment(-50.0F); toReturn.setDeity(Deities.getDeity(4)); toReturn.setFaith(1.0F); }  if (templateid != 119) Server.getInstance().broadCastAction(toReturn.getNameWithGenus() + " has arrived.", toReturn, 10);  if (toReturn.isUnique()) { Server.getInstance().broadCastSafe("Rumours of " + toReturn.getName() + " are starting to spread."); Servers.localServer.spawnedUnique(); logger.log(Level.INFO, "Unique " + toReturn.getName() + " spawned @ " + toReturn.getTileX() + ", " + toReturn.getTileY() + ", wurmID = " + toReturn.getWurmId()); }  if (toReturn.getTemplate().getCreatureAI() != null) toReturn.getTemplate().getCreatureAI().creatureCreated(toReturn);  return toReturn; } public float getSecondsPlayed() { return 1.0F; } public static void createBasicItems(Creature toReturn) { try { Item inventory = toReturn.getInventory(); if (toReturn.getTemplate().getTemplateId() == 11) { Item club = createItem(314, 45.0F); inventory.insertItem(club); Item paper = getRareRecipe("Da Wife", 1250, 1251, 1252, 1253); if (paper != null) inventory.insertItem(paper);  } else if (toReturn.getTemplate().getTemplateId() == 23) { Item paper = getRareRecipe("Granny Gobin", 1255, 1256, 1257, 1258); if (paper != null) inventory.insertItem(paper);  } else if (toReturn.getTemplate().getTemplateId() == 75) { Item swo = createItem(81, 85.0F); ItemSpellEffects effs = new ItemSpellEffects(swo.getWurmId()); effs.addSpellEffect(new SpellEffect(swo.getWurmId(), (byte)33, 90.0F, 20000000)); inventory.insertItem(swo); Item helmOne = createItem(285, 75.0F); Item helmTwo = createItem(285, 75.0F); helmOne.setMaterial((byte)9); helmTwo.setMaterial((byte)9); inventory.insertItem(helmOne); inventory.insertItem(helmTwo); } else if (toReturn.isUnique()) { if (toReturn.getTemplate().getTemplateId() == 26) { Item sword = createItem(80, 45.0F); inventory.insertItem(sword); Item shield = createItem(4, 45.0F); inventory.insertItem(shield); Item goboHat = createItem(1014, 55.0F); inventory.insertItem(goboHat); } else if (toReturn.getTemplate().getTemplateId() == 27) { Item club = createItem(314, 65.0F); inventory.insertItem(club); Item trollCrown = createItem(1015, 70.0F); inventory.insertItem(trollCrown); } else if (toReturn.getTemplate().getTemplateId() == 22 || toReturn.getTemplate().getTemplateId() == 20) { Item club = createItem(314, 65.0F); inventory.insertItem(club); } else if (!CreatureTemplate.isDragonHatchling(toReturn.getTemplate().getTemplateId())) { if (CreatureTemplate.isFullyGrownDragon(toReturn.getTemplate().getTemplateId())); }  }  } catch (Exception ex) { logger.log(Level.INFO, "Failed to create items for creature.", ex); }  } public Item getPrimWeapon() { return getPrimWeapon(false); } public Item getPrimWeapon(boolean onlyBodyPart) { Item primWeapon = null; if (isAnimal()) { try { if (getHandDamage() > 0.0F) return getEquippedWeapon((byte)14);  if (getKickDamage() > 0.0F) return getEquippedWeapon((byte)34);  if (getHeadButtDamage() > 0.0F) return getEquippedWeapon((byte)1);  if (getBiteDamage() > 0.0F) return getEquippedWeapon((byte)29);  if (getBreathDamage() > 0.0F) return getEquippedWeapon((byte)2);  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + nsp.getMessage(), (Throwable)nsp); }  } else { try { byte slot = isPlayer() ? 38 : 14; primWeapon = getEquippedWeapon(slot, true); } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  }  if (primWeapon == null) try { byte slot = isPlayer() ? 37 : 13; primWeapon = getEquippedWeapon(slot, true); if (!primWeapon.isTwoHanded()) { primWeapon = null; } else if (getShield() != null) { primWeapon = null; }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }   return primWeapon; } public Item getLefthandWeapon() { try { byte slot = isPlayer() ? 37 : 13; Set<Item> wornItems = this.status.getBody().getBodyPart(slot).getItems(); if (wornItems != null) for (Item item : wornItems) { if (!item.isArmour() && !item.isBodyPartAttached()) if (item.getDamagePercent() > 0) return item;   }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return null; } public Item getLefthandItem() { try { byte slot = isPlayer() ? 37 : 13; Set<Item> wornItems = this.status.getBody().getBodyPart(slot).getItems(); if (wornItems != null) for (Item item : wornItems) { if (!item.isArmour() && !item.isBodyPartAttached()) return item;  }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return null; } public Item getRighthandItem() { try { byte slot = isPlayer() ? 38 : 14; Set<Item> wornItems = this.status.getBody().getBodyPart(slot).getItems(); if (wornItems != null) for (Item item : wornItems) { if (!item.isArmour() && !item.isBodyPartAttached()) return item;  }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return null; } public Item getRighthandWeapon() { try { byte slot = isPlayer() ? 38 : 14; Set<Item> wornItems = this.status.getBody().getBodyPart(slot).getItems(); if (wornItems != null) for (Item item : wornItems) { if (!item.isArmour() && !item.isBodyPartAttached()) if (item.getDamagePercent() > 0) return item;   }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return null; } public Item getWornBelt() { try { byte slot = isPlayer() ? 43 : 34; Set<Item> wornItems = this.status.getBody().getBodyPart(slot).getItems(); if (wornItems != null) for (Item item : wornItems) { if (item.isBelt()) return item;  }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return null; } public Item[] getSecondaryWeapons() { Set<Item> toReturn = new HashSet<>(); if (getBiteDamage() > 0.0F) try { toReturn.add(getEquippedWeapon((byte)29)); } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " no face."); }   if (getHeadButtDamage() > 0.0F) try { toReturn.add(getEquippedWeapon((byte)1)); } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " no head."); }   if (getKickDamage() > 0.0F) try { if (isAnimal() || isMonster()) { toReturn.add(getEquippedWeapon((byte)34)); } else { try { getArmour((byte)34); } catch (NoArmourException nsp) { if (getCarryingCapacityLeft() > 40000) toReturn.add(getEquippedWeapon((byte)34));  }  }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " no legs."); }   if (getBreathDamage() > 0.0F) try { toReturn.add(getEquippedWeapon((byte)2)); } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " no torso."); }   if (getShield() == null) try { if (getPrimWeapon() == null || !getPrimWeapon().isTwoHanded()) if (isPlayer()) { toReturn.add(getEquippedWeapon((byte)37, false)); } else { toReturn.add(getEquippedWeapon((byte)13, false)); }   } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " - no arm. This may be possible later but not now." + nsp.getMessage(), (Throwable)nsp); }   if (!toReturn.isEmpty()) return toReturn.<Item>toArray(new Item[toReturn.size()]);  return emptyItems; } public Item getShield() { Item shield = null; try { byte slot = isPlayer() ? 44 : 3; shield = getEquippedItem(slot); if (shield != null && !shield.isShield()) shield = null;  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); }  return shield; } public float getSpeed() { if (getCreatureAIData() != null) return getCreatureAIData().getSpeed();  return this.template.getSpeed(); } public int calculateSize() { int centimetersHigh = getBody().getCentimetersHigh(); int centimetersLong = getBody().getCentimetersLong(); int centimetersWide = getBody().getCentimetersWide(); int size = 3; if (centimetersHigh > 400 || centimetersLong > 400 || centimetersWide > 400) { size = 5; } else if (centimetersHigh > 200 || centimetersLong > 200 || centimetersWide > 200) { size = 4; } else if (centimetersHigh > 100 || centimetersLong > 100 || centimetersWide > 100) { size = 3; } else if (centimetersHigh > 50 || centimetersLong > 50 || centimetersWide > 50) { size = 2; } else { size = 1; }  return size; } public void say(String message) { if (this.currentTile != null) this.currentTile.broadCastMessage(new Message(this, (byte)0, ":Local", "<" + getName() + "> " + message));  } public void say(String message, boolean emote) { if (this.currentTile != null) if (!emote) { say(message); } else { this.currentTile.broadCastMessage(new Message(this, (byte)6, ":Local", getName() + " " + message)); }   } protected Creature() throws Exception { this.lastSecond = 1;
/*       */ 
/*       */ 
/*       */     
/*  5990 */     this.breedTick = 0;
/*  5991 */     this.lastPolled = Server.rand.nextInt(pollChecksPer);
/*       */     
/*  5993 */     this.aiData = null;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11204 */     this.isPathing = false;
/* 11205 */     this.setTargetNOID = false;
/* 11206 */     this.creatureToBlinkTo = null;
/* 11207 */     this.receivedPath = false;
/*       */     
/* 11209 */     this.targetPathTile = null;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 16793 */     this.creatureFavor = 100.0F;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 19961 */     this.switchv = true; this.behaviour = Behaviours.getInstance().getBehaviour((short)4); this.communicator = new CreatureCommunicator(this); this.actions = new ActionStack(); this.movementScheme = new MovementScheme(this); this.pollCounter = Server.rand.nextInt(10800); } public void sendEquipment(Creature receiver) { if (receiver.addItemWatched(getBody().getBodyItem())) { receiver.getCommunicator().sendOpenInventoryWindow(getBody().getBodyItem().getWurmId(), getName()); getBody().getBodyItem().addWatcher(getBody().getBodyItem().getWurmId(), receiver); Wounds w = getBody().getWounds(); if (w != null) { Wound[] wounds = w.getWounds(); for (Wound lWound : wounds) { try { Item bodypart = getBody().getBodyPartForWound(lWound); receiver.getCommunicator().sendAddWound(lWound, bodypart); } catch (NoSpaceException nsp) { logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp); }  }  }  }  if (receiver.getPower() >= 2) if (receiver.addItemWatched(getInventory())) { receiver.getCommunicator().sendOpenInventoryWindow(getInventory().getWurmId(), getName() + " inventory"); getInventory().addWatcher(getInventory().getWurmId(), receiver); }   } public final void startUsingPath() { if (this.setTargetNOID) { setTarget(-10L, true); this.setTargetNOID = false; }  if (this.creatureToBlinkTo != null) { if (!this.creatureToBlinkTo.isDead()) { logger.log(Level.INFO, getName() + " at " + getTileX() + "," + getTileY() + " " + getLayer() + "  blingking to " + this.creatureToBlinkTo.getTileX() + "," + this.creatureToBlinkTo.getTileY() + "," + this.creatureToBlinkTo.getLayer()); blinkTo(this.creatureToBlinkTo.getTileX(), this.creatureToBlinkTo.getTileY(), this.creatureToBlinkTo.getLayer(), this.creatureToBlinkTo.getFloorLevel()); this.status.setPath(null); this.receivedPath = false; setPathing(false, true); }  this.creatureToBlinkTo = null; }  if (this.receivedPath) { this.receivedPath = false; setPathing(false, false); if (this.status.getPath() != null) { sendToLoggers("received path to " + this.status.getPath().getTargetTile().getTileX() + "," + this.status.getPath().getTargetTile().getTileY(), (byte)2); if (this.status.getPath().getSize() >= 4) { this.pathRecalcLength = this.status.getPath().getSize() / 2; } else { this.pathRecalcLength = 0; }  this.status.setMoving(true); if (moveAlongPath() || isTeleporting()) { this.status.setPath(null); this.status.setMoving(false); }  }  }  } protected void hunt() { if (!isPathing()) { Path path = null; boolean findPath = false; if (Server.rand.nextInt(2 * Math.max(1, this.template.getAggressivity())) == 0) { this.setTargetNOID = true; return; }  if (isAnimal() || isDominated()) { path = this.status.getPath(); if (path == null) findPath = true;  } else { findPath = true; }  if (findPath) { startPathing(10); } else if (path == null) { startPathing(100); }  }  } public void setAlertSeconds(int seconds) { this.guardSecondsLeft = (byte)seconds; } public byte getAlertSeconds() { return this.guardSecondsLeft; } public void callGuards() { if (this.guardSecondsLeft > 0) { getCommunicator().sendNormalServerMessage("You already called the guards. Wait a few seconds.", (byte)3); return; }  this.guardSecondsLeft = 10; if (getVisionArea() != null) if (isOnSurface()) { if (getVisionArea().getSurface() != null) getVisionArea().getSurface().callGuards();  } else if (getVisionArea().getUnderGround() != null) { getVisionArea().getUnderGround().callGuards(); }   } public final boolean isPathing() { return this.isPathing; } public final void setPathing(boolean pathing, boolean removeFromPathing) { this.isPathing = pathing; if (removeFromPathing) if (isHuman() || isGhost() || isUnique()) { pathFinderNPC.removeTarget(this); } else if (isAggHuman()) { pathFinderAgg.removeTarget(this); } else { pathFinder.removeTarget(this); }   } public final void startPathingToTile(PathTile p) { if (this.creatureToBlinkTo == null) { this.targetPathTile = p; if (p != null) { sendToLoggers("heading to specific " + p.getTileX() + "," + p.getTileY(), (byte)2); setPathing(true, false); if (isHuman() || isGhost() || isUnique()) { pathFinderNPC.addTarget(this, p); } else if (isAggHuman()) { pathFinderAgg.addTarget(this, p); } else { pathFinder.addTarget(this, p); }  }  }  } static int totx = 0; static int toty = 0; static int movesx = 0; static int movesy = 0; protected static final String NOPATH = "No pathing now"; float creatureFavor; boolean switchv; public final void startPathing(int seed) { if (this.creatureToBlinkTo == null) { PathTile p = getMoveTarget(seed); if (p != null) startPathingToTile(p);  }  } public final void checkMove() throws NoSuchCreatureException, NoSuchPlayerException { if (this.hitchedTo != null) return;  if (isSentinel()) return;  if (isHorse() || isUnicorn()) { Item torsoItem = getWornItem((byte)2); if (torsoItem != null) if (torsoItem.isSaddleLarge() || torsoItem.isSaddleNormal()) return;   }  if (isDominated()) { if (hasOrders()) if (this.target == -10L) { if (this.status.getPath() == null) { if (!isPathing()) startPathing(0);  } else if (moveAlongPath() || isTeleporting()) { this.status.setPath(null); this.status.setMoving(false); if (isSpy()) { Creature linkedToc = getCreatureLinkedTo(); if (isWithinSpyDist(linkedToc)) { turnTowardsCreature(linkedToc); for (Npc npc : Creatures.getInstance().getNpcs()) { if (!npc.isDead() && isSpyFriend(npc)) if (npc.isWithinDistanceTo(this, 400.0F)) if (npc.longTarget == null) { npc.longTarget = new LongTarget(linkedToc.getTileX(), linkedToc.getTileY(), 0, linkedToc.isOnSurface(), linkedToc.getFloorLevel(), npc); if (!npc.isWithinDistanceTo(linkedToc, 100.0F)) { int seed = Server.rand.nextInt(5); String mess = "Think I'll go hunt for " + linkedToc.getName() + " a bit..."; switch (seed) { case 0: mess = linkedToc.getName() + " is in trouble now!"; break;case 1: mess = "Going to check out what " + linkedToc.getName() + " is doing."; break;case 2: mess = "Heading to slay " + linkedToc.getName() + "."; break;case 3: mess = "Going to get me the scalp of " + linkedToc.getName() + " today."; break;case 4: mess = "Poor " + linkedToc.getName() + " won't know what hit " + linkedToc.getHimHerItString() + "."; break;default: mess = "Think I'll go hunt for " + linkedToc.getName() + " a bit..."; break; }  VolaTile tile = npc.getCurrentTile(); if (tile != null) { Message m = new Message(npc, (byte)0, ":Local", "<" + npc.getName() + "> " + mess); tile.broadCastMessage(m); }  }  }    }  }  }  }  } else if (this.status.getPath() != null) { if (moveAlongPath() || isTeleporting()) { this.status.setPath(null); this.status.setMoving(false); }  } else { hunt(); }   } else if (this.leader == null) { if (!this.shouldStandStill && !this.status.isUnconscious() && this.status.getStunned() == 0.0F) if (isMoveGlobal()) { if (this.status.getPath() != null) { if (moveAlongPath() || isTeleporting()) { this.status.setPath(null); this.status.setMoving(false); }  } else if (isHunter() && this.target != -10L && this.fleeCounter <= 0) { hunt(); } else { if (Server.rand.nextInt(100) == 0) { PathTile targ = getPersonalTargetTile(); if (targ != null) if (!this.isPathing) startPathingToTile(targ);   }  if (this.status.moving) { if (Server.rand.nextInt(100) < 5) this.status.setMoving(false);  } else { int mod = 1; int max = 2000; if ((isCareful() && (getStatus()).damage > 10000) || this.loggerCreature1 > 0L) { mod = 19; } else if (isBred() || isBranded() || isCaredFor()) { max = 20000; } else if (isNpc() && !isAggHuman() && getCitizenVillage() != null) { max = 200 + (int)(getWurmId() % 100L) * 3; }  if (Server.rand.nextInt(Math.max(1, max - this.template.getMoveRate() * mod)) < 5 || shouldFlee()) { this.status.setMoving(true); } else if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled() && (Server.rand.nextInt(Math.max(1, 1000 - this.template.getMoveRate())) < 5 || this.loggerCreature1 > 0L)) { for (Fence f : getCurrentTile().getAllFences()) { if (f.isHorizontal() && Math.abs(f.getPositionY() - getPosY()) < 1.25F) { takeSimpleStep(); break; }  if (!f.isHorizontal() && Math.abs(f.getPositionX() - getPosX()) < 1.25F) { takeSimpleStep(); break; }  }  }  }  if (this.status.moving && !isTeleporting()) takeSimpleStep();  }  } else if (this.status.getPath() == null) { if (!isTeleporting()) { if (!isPathing()) { if (this.target == -10L || shouldFlee()) { int mod = 1; int max = 2000; if (isCareful() && (getStatus()).damage > 10000) mod = 19;  if (this.loggerCreature1 > 0L) mod = 19;  int seed = Server.rand.nextInt(Math.max(2, max - this.template.getMoveRate() * mod)); if (getPositionZ() < 0.0F) seed -= 100;  if (seed < 8 || (isSpiritGuard() && this.citizenVillage != this.currentVillage) || shouldFlee()) startPathing(seed);  } else { hunt(); }  if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) { if (Server.rand.nextInt(Math.max(1, 1000 - this.template.getMoveRate())) < 5 || this.loggerCreature1 > 0L) { float xMod = getPosX() % 4.0F; float yMod = getPosY() % 4.0F; if (xMod > 3.5F || xMod < 0.5F || yMod > 3.5F || yMod < 0.5F) takeSimpleStep();  }  if (shouldFlee() && getPathfindCounter() > 10 && this.targetPathTile != null) if (getTileX() != this.targetPathTile.getTileX() || getTileY() != this.targetPathTile.getTileY()) { if (getPathfindCounter() % 50 == 0 && Server.rand.nextFloat() < 0.05F) turnTowardsTile((short)this.targetPathTile.getTileX(), (short)this.targetPathTile.getTileY());  takeSimpleStep(); }   }  } else { sendToLoggers("still pathing"); }  } else { this.status.setPath(null); this.status.setMoving(false); }  } else if (moveAlongPath() || isTeleporting()) { this.status.setPath(null); this.status.setMoving(false); }   }  } public float getMoveModifier(int tile) { short height = Tiles.decodeHeight(tile); if (height < 2) return 0.5F * this.status.getMovementTypeModifier();  return (Tiles.getTile(Tiles.decodeType(tile))).speed * this.status.getMovementTypeModifier(); } public boolean mayManageGuards() { if (this.citizenVillage != null) return this.citizenVillage.isActionAllowed((short)67, this);  return false; } public boolean isMoving() { return this.status.isMoving(); } public static final float normalizeAngle(float angle) { return MovementChecker.normalizeAngle(angle); } public final void checkBridgeMove(VolaTile oldTile, VolaTile newtile, float diffZ) { if (getBridgeId() == -10L && newtile.getStructure() != null) { BridgePart[] bridgeParts = newtile.getBridgeParts(); if (bridgeParts != null) for (BridgePart bp : bridgeParts) { if (bp.isFinished()) { boolean enter = false; float nz = Zones.calculatePosZ(getPosX(), getPosY(), newtile, isOnSurface(), isFloating(), getPositionZ(), this, bp.getStructureId()); float newDiff = Math.abs(nz - getPositionZ()); float maxDiff = 1.3F; if (oldTile != null) { if (bp.getDir() == 0 || bp.getDir() == 4) { if (oldTile.getTileY() == newtile.getTileY()) if (newDiff < 1.3F) if (bp.hasAnExit()) enter = true;    } else if (oldTile.getTileX() == newtile.getTileX()) { if (newDiff < 1.3F) if (bp.hasAnExit()) enter = true;   }  } else { enter = (newDiff < 1.3F); }  if (enter) { setBridgeId(bp.getStructureId()); float newDiffZ = nz - getPositionZ(); setPositionZ(nz); moved(0.0F, 0.0F, newDiffZ, 0, 0); break; }  }  }   } else if (getBridgeId() != -10L) { boolean leave = true; if (oldTile != null) { BridgePart[] bridgeParts = oldTile.getBridgeParts(); if (bridgeParts != null) for (BridgePart bp : bridgeParts) { if (bp.isFinished()) if (bp.getDir() == 0 || bp.getDir() == 4) { if (oldTile.getTileX() != newtile.getTileX()) leave = false;  } else if (oldTile.getTileY() != newtile.getTileY()) { leave = false; }   }   }  if (leave) if (newtile.getStructure() == null || newtile.getStructure().getWurmId() != getBridgeId()) { setBridgeId(-10L); } else { BridgePart[] bridgeParts = newtile.getBridgeParts(); boolean foundBridge = false; for (BridgePart bp : bridgeParts) { foundBridge = true; if (!bp.isFinished()) { setBridgeId(-10L); return; }  }  if (foundBridge) for (BridgePart bp : bridgeParts) { if (bp.isFinished() && bp.hasAnExit()) { setBridgeId(bp.getStructureId()); return; }  }   }   }  } public boolean moveAlongPath() { long start = System.nanoTime(); try { Path path = null; int mvs = 2; if (this.target != -10L) mvs = 3;  if (getSize() >= 5) mvs += 3;  for (x = 0; x < mvs; x++) { path = this.status.getPath(); if (path != null && !path.isEmpty()) { MeshIO lMesh; PathTile next = path.getFirst(); if (next.getTileX() == (getCurrentTile()).tilex && next.getTileY() == (getCurrentTile()).tiley) { boolean canRemove = true; if (next.hasSpecificPos()) { float diffX = this.status.getPositionX() - next.getPosX(); float diffY = this.status.getPositionY() - next.getPosY(); double totalDist = Math.sqrt((diffX * diffX + diffY * diffY)); float f1 = getMoveModifier((isOnSurface() ? Server.surfaceMesh : Server.caveMesh).getTile((int)this.status.getPositionX() >> 2, (int)this.status.getPositionY() >> 2)); if (totalDist > (getSpeed() * f1)) canRemove = false;  }  if (canRemove) { path.removeFirst(); if (getTarget() != null) if (getTarget().getTileX() == getTileX() && getTarget().getTileY() == getTileY() && getTarget().getFloorLevel() != getFloorLevel()) if (isSpiritGuard()) { pushToFloorLevel(getTarget().getFloorLevel()); } else if (canOpenDoors()) { Floor[] floors = getCurrentTile().getFloors(Math.min(getFloorLevel(), getTarget().getFloorLevel()) * 30, Math.max(getFloorLevel(), getTarget().getFloorLevel()) * 30); for (Floor f : floors) { if (getTarget().getFloorLevel() > getFloorLevel()) { if (f.getFloorLevel() == getFloorLevel() + 1) if (f.isOpening() || f.isAPlan()) { pushToFloorLevel(f.getFloorLevel()); break; }   } else if (f.getFloorLevel() == getFloorLevel()) { if (f.isOpening() || f.isAPlan()) { pushToFloorLevel(f.getFloorLevel() - 1); break; }  }  }  }    if (path.isEmpty()) return true;  next = path.getFirst(); }  }  float lPosX = this.status.getPositionX(); float lPosY = this.status.getPositionY(); float lPosZ = this.status.getPositionZ(); float lRotation = this.status.getRotation(); double lNewRotation = next.hasSpecificPos() ? Math.atan2((next.getPosY() - lPosY), (next.getPosX() - lPosX)) : Math.atan2((((next.getTileY() << 2) + 2) - lPosY), (((next.getTileX() << 2) + 2) - lPosX)); lRotation = (float)(lNewRotation * 57.29577951308232D) + 90.0F; int lOldTileX = (int)lPosX >> 2; int lOldTileY = (int)lPosY >> 2; if (isOnSurface()) { lMesh = Server.surfaceMesh; } else { lMesh = Server.caveMesh; }  float lMod = getMoveModifier(lMesh.getTile(lOldTileX, lOldTileY)); float lXPosMod = (float)Math.sin((lRotation * 0.017453292F)) * getSpeed() * lMod; float lYPosMod = -((float)Math.cos((lRotation * 0.017453292F))) * getSpeed() * lMod; int lNewTileX = (int)(lPosX + lXPosMod) >> 2; int lNewTileY = (int)(lPosY + lYPosMod) >> 2; int lDiffTileX = lNewTileX - lOldTileX; int lDiffTileY = lNewTileY - lOldTileY; if (Math.abs(lDiffTileX) > 1 || Math.abs(lDiffTileY) > 1) logger.log(Level.WARNING, getName() + "," + getWurmId() + " diffTileX=" + lDiffTileX + ", y=" + lDiffTileY);  if (lDiffTileX != 0 || lDiffTileY != 0) { if (!isOnSurface()) if (Tiles.isSolidCave(Tiles.decodeType(lMesh.getTile(lNewTileX, lNewTileY)))) { rotateRandom(lRotation, 45); try { takeSimpleStep(); } catch (NoSuchPlayerException noSuchPlayerException) {  } catch (NoSuchCreatureException noSuchCreatureException) {} return true; }   if (!isGhost()) { BlockingResult result = Blocking.getBlockerBetween(this, getPosX(), getPosY(), lPosX + lXPosMod, lPosY + lYPosMod, getPositionZ(), getPositionZ(), isOnSurface(), isOnSurface(), false, 6, true, -10L, getBridgeId(), getBridgeId(), followsGround()); if (result != null) { boolean foundDoor = false; for (Blocker blocker : result.getBlockerArray()) { if (blocker.isDoor()) if (!blocker.canBeOpenedBy(this, false)) foundDoor = true;   }  if (!foundDoor) { path.clear(); return true; }  }  }  if (!next.hasSpecificPos() && next.getTileX() == lNewTileX && next.getTileY() == lNewTileY) path.removeFirst();  movesx += lDiffTileX; movesy += lDiffTileY; }  float oldPosX = lPosX; float oldPosY = lPosY; float oldPosZ = lPosZ; int oldDeciZ = (int)(lPosZ * 10.0F); lPosX += lXPosMod; lPosY += lYPosMod; if (lPosX >= (Zones.worldTileSizeX - 1 << 2) || lPosX < 0.0F || lPosY < 0.0F || lPosY >= (Zones.worldTileSizeY - 1 << 2)) { destroy(); return true; }  lPosZ = calculatePosZ(); int newDeciZ = (int)(lPosZ * 10.0F); if (lPosZ < -0.5D) if (isSubmerged()) { if (isFloating() && newDeciZ > this.template.offZ * 10.0F) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return true; }  if (!isFloating() && lPosZ > -5.0F && oldDeciZ < newDeciZ) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return true; }  if (lPosZ < -5.0F) { if (x == 3) if (isFloating()) { lPosZ = this.template.offZ; } else { float newdiff = Math.max(-1.0F, Math.min(1.0F, (float)Server.rand.nextGaussian())); float newPosZ = Math.max(lPosZ, Math.min(-5.0F, getPositionZ() + newdiff)); lPosZ = newPosZ; }   } else if (x == 3) { if (isFloating()) lPosZ = this.template.offZ;  }  } else { lPosZ = Math.max(-1.25F, lPosZ); if (isFloating()) lPosZ = Math.max(this.template.offZ, lPosZ);  }   this.status.setPositionX(lPosX); this.status.setPositionY(lPosY); this.status.setPositionZ(lPosZ); this.status.setRotation(lRotation); moved(lPosX - oldPosX, lPosY - oldPosY, lPosZ - oldPosZ, lDiffTileX, lDiffTileY); }  }  if (path != null) { if (this.pathRecalcLength > 0 && path.getSize() <= this.pathRecalcLength) return true;  return path.isEmpty(); }  return true; } finally {} } protected boolean startDestroyingWall(Wall wall) { try { BehaviourDispatcher.action(this, this.communicator, getEquippedWeapon((byte)14).getWurmId(), wall.getId(), (short)180); } catch (FailedException fe) { return true; } catch (NoSuchBehaviourException nsb) { logger.log(Level.WARNING, nsb.getMessage(), (Throwable)nsb); return true; } catch (NoSuchCreatureException nsc) { logger.log(Level.WARNING, nsc.getMessage(), (Throwable)nsc); return true; } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi); return true; } catch (NoSuchPlayerException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); return true; } catch (NoSuchWallException nsw) { logger.log(Level.WARNING, nsw.getMessage(), (Throwable)nsw); return true; } catch (NoSpaceException nsp) { logger.log(Level.WARNING, nsp.getMessage(), (Throwable)nsp); return true; }  return false; } public void followLeader() { float iposx = this.leader.getStatus().getPositionX(); float iposy = this.leader.getStatus().getPositionY(); float diffx = iposx - this.status.getPositionX(); float diffy = iposy - this.status.getPositionY(); int diff = (int)Math.max(Math.abs(diffx), Math.abs(diffy)); if (diffx < 0.0F && this.status.getPositionX() < 10.0F) return;  if (diffy < 0.0F && this.status.getPositionY() < 10.0F) return;  if (diffy > 0.0F && this.status.getPositionY() > Zones.worldMeterSizeY - 10.0F) return;  if (diffx > 0.0F && this.status.getPositionX() > Zones.worldMeterSizeX - 10.0F) return;  if (diff > 35) { logger.log(Level.INFO, this.leader.getName() + " moved " + diff + "diffx=" + diffx + ", diffy=" + diffy); setLeader(null); } else if (diffx > 4.0F || diffy > 4.0F || diffx < -4.0F || diffy < -4.0F) { float lPosX = this.status.getPositionX(); float lPosY = this.status.getPositionY(); float lPosZ = this.status.getPositionZ(); int lOldTileX = (int)lPosX >> 2; int lOldTileY = (int)lPosY >> 2; double lNewrot = Math.atan2((iposy - lPosY), (iposx - lPosX)); lNewrot = lNewrot * 57.29577951308232D + 90.0D; if (lNewrot > 360.0D) lNewrot -= 360.0D;  if (lNewrot < 0.0D) lNewrot += 360.0D;  float movex = 0.0F; float movey = 0.0F; if (diffx < -4.0F) { movex = diffx + 4.0F; } else if (diffx > 4.0F) { movex = diffx - 4.0F; }  if (diffy < -4.0F) { movey = diffy + 4.0F; } else if (diffy > 4.0F) { movey = diffy - 4.0F; }  float lXPosMod = (float)Math.sin(lNewrot * 0.01745329238474369D) * Math.abs(movex + Server.rand.nextFloat()); float lYPosMod = -((float)Math.cos(lNewrot * 0.01745329238474369D)) * Math.abs(movey + Server.rand.nextFloat()); float newPosX = lPosX + lXPosMod; float newPosY = lPosY + lYPosMod; int lNewTileX = (int)newPosX >> 2; int lNewTileY = (int)newPosY >> 2; int lDiffTileX = lNewTileX - lOldTileX; int lDiffTileY = lNewTileY - lOldTileY; if (lDiffTileX != 0 || lDiffTileY != 0) if (!isGhost()) if (this.leader.getBridgeId() < 0L && getBridgeId() < 0L) { BlockingResult result = Blocking.getBlockerBetween(this, lPosX, lPosY, newPosX, newPosY, getPositionZ(), this.leader.getPositionZ(), isOnSurface(), isOnSurface(), false, 2, -1L, getBridgeId(), getBridgeId(), followsGround()); if (result != null) { Blocker first = result.getFirstBlocker(); if (!first.isDoor()) { this.leader.sendToLoggers("Your floor level " + this.leader.getFloorLevel() + ", creature: " + getFloorLevel()); setLeader(null); return; }  if (!first.canBeOpenedBy(this.leader, false) && !first.canBeOpenedBy(this, false)) { this.leader.sendToLoggers("Your floor level " + this.leader.getFloorLevel() + ", creature: " + getFloorLevel()); setLeader(null); return; }  }  }    if (!this.leader.isOnSurface() && !isOnSurface()) if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)newPosX >> 2, (int)newPosY >> 2)))) { newPosX = iposx; newPosY = iposy; }   float newPosZ = calculatePosZ(); if (!isSwimming() && newPosZ < -0.71D && newPosZ < lPosZ) { setLeader(null); this.status.setPositionZ(newPosZ); } else { newPosZ = Math.max(-1.25F, newPosZ); if (isFloating()) newPosZ = Math.max(this.template.offZ, newPosZ);  setRotation((float)lNewrot); int tilex = (int)lPosX >> 2; int tiley = (int)lPosY >> 2; int newtilex = (int)newPosX >> 2; int newtiley = (int)newPosY >> 2; this.status.setPositionX(newPosX); this.status.setPositionY(newPosY); this.status.setPositionZ(newPosZ); moved(newPosX - lPosX, newPosY - lPosY, newPosZ - lPosZ, newtilex - tilex, newtiley - tiley); }  }  } public void sendAttitudeChange() { if (this.currentTile != null) this.currentTile.checkChangedAttitude(this);  } public final void takeSimpleStep() throws NoSuchCreatureException, NoSuchPlayerException { long start = 0L; try { int mvs = 2; if (this.target != -10L) mvs = 3;  if (getSize() >= 5) mvs += 3;  int x = 0; while (true) { if (x < mvs) { float lMoveModifier, lPosX = this.status.getPositionX(); float lPosY = this.status.getPositionY(); float lPosZ = this.status.getPositionZ(); float lRotation = this.status.getRotation(); float oldPosX = lPosX; float oldPosY = lPosY; float oldPosZ = lPosZ; int oldDeciZ = (int)(lPosZ * 10.0F); int lOldTileX = (int)lPosX >> 2; int lOldTileY = (int)lPosY >> 2; if (this.target == -10L) { if (isOnSurface()) { int rand = Server.rand.nextInt(100); if (rand < 10) { float f1 = (float)Math.sin((lRotation * 0.017453292F)) * 12.0F; float f2 = -((float)Math.cos((lRotation * 0.017453292F))) * 12.0F; int i = Zones.safeTileX((int)(lPosX + f1) >> 2); int j = Zones.safeTileY((int)(lPosY + f2) >> 2); int tile = Zones.getTileIntForTile(i, j, getLayer()); if (isTargetTileTooHigh(i, j, tile, (lPosZ < 0.0F))) { short[] lLowestNode = getLowestTileCorner((short)lOldTileX, (short)lOldTileY); turnTowardsTile(lLowestNode[0], lLowestNode[1]); }  } else if (rand < 12) { rotateRandom(lRotation, 100); } else if (rand < 15) { lRotation = normalizeAngle(lRotation + Server.rand.nextInt(100)); }  } else { int rand = Server.rand.nextInt(100); if (rand < 2) { rotateRandom(lRotation, 100); } else if (rand < 5) { lRotation = normalizeAngle(lRotation + Server.rand.nextInt(100)); }  }  } else { turnTowardsCreature(getTarget()); }  lRotation = normalizeAngle(lRotation); if (!isOnSurface()) { lMoveModifier = getMoveModifier(Server.caveMesh.getTile(lOldTileX, lOldTileY)); } else { lMoveModifier = getMoveModifier(Server.surfaceMesh.getTile(lOldTileX, lOldTileY)); }  float lXPosMod = (float)Math.sin((lRotation * 0.017453292F)) * getSpeed() * lMoveModifier; float lYPosMod = -((float)Math.cos((lRotation * 0.017453292F))) * getSpeed() * lMoveModifier; int lNewTileX = (int)(lPosX + lXPosMod) >> 2; int lNewTileY = (int)(lPosY + lYPosMod) >> 2; int lDiffTileX = lNewTileX - lOldTileX; int lDiffTileY = lNewTileY - lOldTileY; if (lDiffTileX != 0 || lDiffTileY != 0) if (!isGhost()) { BlockingResult result; if (!isOnSurface()) { if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(lOldTileX, lOldTileY)))) { logger.log(Level.INFO, getName() + " is in rock at takesimplestep. Dying."); die(false, "Suffocated in Rock"); return; }  if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(lNewTileX, lNewTileY)))) { if (this.currentTile.isTransition) { sendToLoggers(lPosZ + " setting to surface then moving."); if (!Tiles.isMineDoor(Tiles.decodeType(Server.caveMesh.getTile(getTileX(), getTileY()))) || MineDoorPermission.getPermission(getTileX(), getTileY()).mayPass(this)) { setLayer(0, true); } else { rotateRandom(lRotation, 45); }  return; }  rotateRandom(lRotation, 45); return; }  } else if (Tiles.Tile.TILE_LAVA.id == Tiles.decodeType(Server.surfaceMesh.getTile(lNewTileX, lNewTileY))) { rotateRandom(lRotation, 45); return; }  if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) { result = Blocking.getBlockerBetween(this, lPosX, lPosY, lPosX + lXPosMod, lPosY + lYPosMod, getPositionZ(), getPositionZ(), isOnSurface(), isOnSurface(), false, 6, -1L, getBridgeId(), getBridgeId(), followsGround()); } else { result = Blocking.getBlockerBetween(this, lPosX, lPosY, lPosX + lXPosMod, lPosY + lYPosMod, getPositionZ(), getPositionZ(), isOnSurface(), isOnSurface(), false, 6, -1L, getBridgeId(), getBridgeId(), followsGround()); }  if (result != null) { Blocker first = result.getFirstBlocker(); if (isKingdomGuard() || isSpiritGuard()) { if (!first.isDoor()) { rotateRandom(lRotation, 100); return; }  } else { if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) { turnTowardsTile((short)getTileX(), (short)getTileY()); rotateRandom(this.status.getRotation(), 45); x = 0; getStatus().setMoving(false); } else { rotateRandom(lRotation, 100); return; }  x++; }  }  VolaTile t = Zones.getOrCreateTile(lNewTileX, lNewTileY, isOnSurface()); VolaTile myt = getCurrentTile(); if ((t.isGuarded() && myt != null && !myt.isGuarded()) || (isAnimal() && t.hasFire())) { rotateRandom(lRotation, 100); return; }  }   lPosX += lXPosMod; lPosY += lYPosMod; if (lPosX >= (Zones.worldTileSizeX - 1 << 2) || lPosX < 0.0F || lPosY < 0.0F || lPosY >= (Zones.worldTileSizeY - 1 << 2)) { destroy(); return; }  if (getFloorLevel() == 0) { try { lPosZ = Zones.calculateHeight(lPosX, lPosY, isOnSurface()); } catch (NoSuchZoneException nsz) { logger.log(Level.WARNING, this.name + " moved out of zone."); }  if (isFloating()) lPosZ = Math.max(this.template.offZ, lPosZ);  int newDeciZ = (int)(lPosZ * 10.0F); if (lPosZ < 0.5D) { if (isSubmerged()) { if (isFloating() && newDeciZ > this.template.offZ * 10.0F) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return; }  if (!isFloating() && lPosZ > -5.0F && oldDeciZ < newDeciZ) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return; }  if (lPosZ < -5.0F) { if (x == 3) if (isFloating()) { lPosZ = this.template.offZ; } else { float newdiff = Math.max(-1.0F, Math.min(1.0F, (float)Server.rand.nextGaussian())); float newPosZ = Math.max(lPosZ, Math.min(-5.0F, getPositionZ() + newdiff)); lPosZ = newPosZ; }   } else if (x == 3) { if (isFloating()) lPosZ = this.template.offZ;  }  }  if ((lPosZ > -2.0F || oldDeciZ <= -20) && (oldDeciZ < 0 || this.target != -10L) && isSwimming()) { lPosZ = Math.max(-1.25F, lPosZ); if (isFloating()) lPosZ = Math.max(this.template.offZ, lPosZ);  } else if (lPosZ < -0.5D && !isSubmerged()) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return; }  } else if (isSubmerged()) { if (oldDeciZ < newDeciZ) { rotateRandom(lRotation, 100); if (this.target != -10L) setTarget(-10L, true);  return; }  }  }  this.status.setPositionX(lPosX); this.status.setPositionY(lPosY); if (Structure.isGroundFloorAtPosition(lPosX, lPosY, isOnSurface())) { this.status.setPositionZ(lPosZ + 0.25F); } else { this.status.setPositionZ(lPosZ); }  this.status.setRotation(lRotation); moved(lPosX - oldPosX, lPosY - oldPosY, lPosZ - oldPosZ, lDiffTileX, lDiffTileY); } else { break; }  x++; }  } finally {} } public void rotateRandom(float aRot, int degrees) { aRot -= degrees; aRot += Server.rand.nextInt(degrees * 2); aRot = normalizeAngle(aRot); this.status.setRotation(aRot); moved(0.0F, 0.0F, 0.0F, 0, 0); } public int getAttackDistance() { return this.template.getSize(); } public void moved(float diffX, float diffY, float diffZ, int aDiffTileX, int aDiffTileY) { if (!isDead()) { try { if (isPlayer() || isWagoner()) { this.movementScheme.move(diffX, diffY, diffZ); if ((isWagoner() || hasLink()) && getVisionArea() != null) try { getVisionArea().move(aDiffTileX, aDiffTileY); } catch (IOException iox) { return; }   try { getCurrentTile().creatureMoved(this.id, diffX, diffY, diffZ, aDiffTileX, aDiffTileY); } catch (NoSuchPlayerException noSuchPlayerException) {  } catch (NoSuchCreatureException noSuchCreatureException) {} if (hasLink() && getVisionArea() != null) getVisionArea().linkZones(aDiffTileX, aDiffTileY);  } else { try { getVisionArea().move(aDiffTileX, aDiffTileY); } catch (IOException iox) { return; }  try { getCurrentTile().creatureMoved(this.id, diffX, diffY, diffZ, aDiffTileX, aDiffTileY); } catch (NoSuchPlayerException noSuchPlayerException) {  } catch (NoSuchCreatureException noSuchCreatureException) {} getVisionArea().linkZones(aDiffTileX, aDiffTileY); }  } catch (NullPointerException ex) { try { if (!isPlayer()) createVisionArea();  return; } catch (Exception exception) {} }  if (diffX != 0.0F || diffY != 0.0F) { try { if (isPlayer() && this.actions.getCurrentAction().isInterruptedAtMove()) { boolean stop = true; if (this.actions.getCurrentAction().getNumber() == 136) { getCommunicator().sendToggle(3, false); } else if (this.actions.getCurrentAction().getNumber() == 329 || this.actions.getCurrentAction().getNumber() == 162 || this.actions.getCurrentAction().getNumber() == 160) { if (getVehicle() != -10L) stop = false;  }  if (stop) { this.communicator.sendSafeServerMessage("You must not move while doing that."); stopCurrentAction(); }  }  } catch (NoSuchActionException noSuchActionException) {} if (aDiffTileX != 0 || aDiffTileY != 0) if (this.musicPlayer != null) this.musicPlayer.moveTile(getCurrentTileNum(), (getPositionZ() <= 0.0F));   }  if (this.status.isTrading()) { Trade trade = this.status.getTrade(); Creature lOpponent = null; if (trade.creatureOne == this) { lOpponent = trade.creatureTwo; } else { lOpponent = trade.creatureOne; }  if (rangeTo(this, lOpponent) > 6) trade.end(this, false);  }  }  } public void stopFighting() { if (this.actions != null) this.actions.removeAttacks(this);  } public void turnTowardsCreature(Creature targ) { if (targ != null) { double lNewrot = Math.atan2((targ.getStatus().getPositionY() - getStatus().getPositionY()), (targ.getStatus().getPositionX() - getStatus().getPositionX())); setRotation((float)(lNewrot * 57.29577951308232D) + 90.0F); if (isSubmerged()) try { float currFloor = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface()); float maxHeight = isFloating() ? this.template.offZ : Math.min(targ.getPositionZ(), Math.max(-5.0F, currFloor)); float oldHeight = getPositionZ(); int diffCentiZ = (int)((maxHeight - oldHeight) * 100.0F); moved(0.0F, 0.0F, diffCentiZ, 0, 0); return; } catch (NoSuchZoneException noSuchZoneException) {}  moved(0.0F, 0.0F, 0.0F, 0, 0); }  } public void turnTowardsPoint(float posX, float posY) { double lNewrot = Math.atan2((posY - getStatus().getPositionY()), (posX - getStatus().getPositionX())); setRotation((float)(lNewrot * 57.29577951308232D) + 90.0F); moved(0.0F, 0.0F, 0.0F, 0, 0); } public void turnTowardsTile(short tilex, short tiley) { double lNewrot = Math.atan2((((tiley << 2) + 2) - getStatus().getPositionY()), (((tilex << 2) + 2) - getStatus().getPositionX())); setRotation((float)(lNewrot * 57.29577951308232D) + 90.0F); moved(0.0F, 0.0F, 0.0F, 0, 0); } public long getWurmId() { return this.id; } public int getTemplateId() { return getTemplate().getTemplateId(); } public String getNameWithoutPrefixes() { return this.name; } public String getNameWithoutFatStatus() { if (getStatus() != null) return getStatus().getAgeString() + " " + getStatus().getTypeString() + this.name;  return "Unknown"; } public String getName() { String fullName = this.name; if (isWagoner()) return fullName;  if (isAnimal() || isMonster()) if (this.name.toLowerCase().compareTo(this.template.getName().toLowerCase()) == 0) { fullName = getPrefixes() + this.name.toLowerCase(); } else { fullName = getPrefixes() + StringUtilities.raiseFirstLetterOnly(this.name); }   if (this.petName.length() > 0) return fullName + " '" + this.petName + "'";  return fullName; } public String getNamePossessive() { String toReturn = getName(); if (toReturn.endsWith("s")) return toReturn + "'";  return toReturn + "'s"; } public String getPrefixes() { if (isUnique()) return "The " + getStatus().getAgeString() + " " + getStatus().getFatString() + getStatus().getTypeString();  return getStatus().getAgeString() + " " + getStatus().getFatString() + getStatus().getTypeString(); } public void setName(String _name) { this.name = _name; } public void setPetName(String aPetName) { if (aPetName == null) { this.petName = ""; } else { this.petName = aPetName.substring(0, Math.min(19, aPetName.length())); }  } public String getColourName() { return this.template.getColourName(this.status); } public String getColourName(int trait) { return this.template.getTemplateColourName(trait); } public CreatureStatus getStatus() { return this.status; } public VisionArea getVisionArea() { return this.visionArea; } public void trainSkill(String sname) throws Exception { Skill skill = this.skills.getSkill(sname); String message = getName() + " trains some " + sname + ", but learns nothing new."; double knowledge = skill.getKnowledge(0.0D); skill.skillCheck(50.0D, 0.0D, false, 3600.0F); if (skill.getKnowledge(0.0D) > knowledge) message = getName() + " trains some  " + sname + " and now have skill " + skill.getKnowledge(0.0D);  logger.log(Level.INFO, message); } public void setSkill(int skill, float val) { try { Skill sktomod = this.skills.getSkill(skill); sktomod.setKnowledge(val, false); } catch (NoSuchSkillException nss) { this.skills.learn(skill, val); }  } public void sendSkills() { try { loadAffinities(); Map<Integer, Skill> skilltree = this.skills.getSkillTree(); for (Integer number : skilltree.keySet()) { try { Skill skill = skilltree.get(number); int[] needed = skill.getDependencies(); int parentSkillId = 0; if (needed.length > 0) parentSkillId = needed[0];  if (parentSkillId != 0) { int parentType = SkillSystem.getTypeFor(parentSkillId); if (parentType == 0) parentSkillId = Integer.MAX_VALUE;  } else if (skill.getType() == 1) { parentSkillId = 2147483646; } else { parentSkillId = Integer.MAX_VALUE; }  getCommunicator().sendAddSkill(number.intValue(), parentSkillId, skill.getName(), (float)skill.getRealKnowledge(), (float)skill.getMinimumValue(), skill.affinity); } catch (NullPointerException np) { logger.log(Level.WARNING, "Inconsistency: " + getName() + " forgetting skill with number " + number.intValue(), np); }  }  } catch (Exception ex2) { logger.log(Level.WARNING, "Failed to load and create skills for creature with name " + this.name + ":" + ex2.getMessage(), ex2); }  } public void loadSkills() throws Exception { if (this.skills == null) logger.log(Level.WARNING, "Skills object is null in creature " + this.name);  try { if (!isPlayer()) { if (this.skills.getId() != -10L) this.skills.initializeSkills();  } else if (!this.guest) { getCommunicator().sendAddSkill(2147483646, 0, "Characteristics", 0.0F, 0.0F, 0); getCommunicator().sendAddSkill(2147483643, 0, "Religion", 0.0F, 0.0F, 0); getCommunicator().sendAddSkill(2147483647, 0, "Skills", 0.0F, 0.0F, 0); this.skills.load(); }  boolean created = false; if (this.skills.isTemplate() || (this.skills.getSkills()).length == 0) { Skills newSkills = SkillsFactory.createSkills(this.id); newSkills.clone(this.skills.getSkills()); this.skills = newSkills; created = true; if (!this.guest) this.skills.save();  this.skills.addTempSkills(); }  if (created) { if (isUndead()) { this.skills.learn(1023, 30.0F); this.skills.learn(10052, 50.0F); this.skills.getSkill(102).setKnowledge(25.0D, false); this.skills.getSkill(103).setKnowledge(25.0D, false); }  if (Servers.localServer.testServer && Servers.localServer.entryServer) if (WurmId.getType(this.id) == 0) { int level = 20; this.skills.learn(1023, level); this.skills.learn(10025, level); this.skills.learn(10006, level); this.skills.learn(10023, level); this.skills.learn(10022, level); this.skills.learn(10020, level); this.skills.learn(10021, level); this.skills.learn(10019, level); this.skills.learn(10001, level); this.skills.learn(10024, level); this.skills.learn(10005, level); this.skills.learn(10027, level); this.skills.learn(10028, level); this.skills.learn(10026, level); this.skills.learn(10064, level); this.skills.learn(10061, level); this.skills.learn(10062, level); this.skills.learn(10063, level); this.skills.learn(1002, level / 2.0F); this.skills.learn(1003, level / 2.0F); this.skills.learn(10056, level); this.skills.getSkill(104).setKnowledge(23.0D, false); this.skills.getSkill(1).setKnowledge(3.0D, false); this.skills.getSkill(102).setKnowledge(23.0D, false); this.skills.getSkill(103).setKnowledge(23.0D, false); this.skills.learn(10053, level); this.skills.learn(10054, level); level = (int)(Server.rand.nextFloat() * 100.0F); this.skills.learn(1030, level); this.skills.learn(10081, level); this.skills.learn(10079, level); this.skills.learn(10080, level); }   }  setMoveLimits(); } catch (Exception ex2) { logger.log(Level.WARNING, "Failed to load and create skills for creature with name " + this.name + ":" + ex2.getMessage(), ex2); }  } public void addStructureTile(VolaTile toAdd, byte structureType) { if (this.structure == null) { this.structure = Structures.createStructure(structureType, this.name + "'s planned structure", WurmId.getNextPlanId(), toAdd.tilex, toAdd.tiley, isOnSurface()); this.status.setBuildingId(this.structure.getWurmId()); } else { try { this.structure.addBuildTile(toAdd, false); if (structureType == 0) this.structure.clearAllWallsAndMakeWallsForStructureBorder(toAdd);  } catch (NoSuchZoneException nsz) { getCommunicator().sendNormalServerMessage("You can't build there.", (byte)3); }  }  } public long getBuildingId() { return this.status.buildingId; } public String getUndeadModelName() { if (getUndeadType() == 1) { if (this.status.sex == 0) return "model.creature.humanoid.human.player.zombie.male" + WurmCalendar.getSpecialMapping(true);  if (this.status.sex == 1) return "model.creature.humanoid.human.player.zombie.female" + WurmCalendar.getSpecialMapping(true);  return "model.creature.humanoid.human.player.zombie" + WurmCalendar.getSpecialMapping(true); }  if (getUndeadType() == 2) return "model.creature.humanoid.human.skeleton" + WurmCalendar.getSpecialMapping(true);  if (getUndeadType() == 3) return "model.creature.humanoid.human.spirit.shadow" + WurmCalendar.getSpecialMapping(true);  return getModelName(); } public String getModelName() { if (isReborn()) { if (this.status.sex == 0) return this.template.getModelName() + ".zombie.male" + WurmCalendar.getSpecialMapping(true);  if (this.status.sex == 1) return this.template.getModelName() + ".zombie.female" + WurmCalendar.getSpecialMapping(true);  return this.template.getModelName() + ".zombie" + WurmCalendar.getSpecialMapping(true); }  if (this.template.isHorse || this.template.isColoured) { String col = this.template.getModelColourName(this.status); StringBuilder stringBuilder = new StringBuilder(); stringBuilder.append(this.template.getModelName()); stringBuilder.append('.'); stringBuilder.append(col.toLowerCase()); if (this.status.sex == 0) stringBuilder.append(".male");  if (this.status.sex == 1) stringBuilder.append(".female");  if (this.status.disease > 0) stringBuilder.append(".diseased");  stringBuilder.append(WurmCalendar.getSpecialMapping(true)); return stringBuilder.toString(); }  if (this.template.isBlackOrWhite) { StringBuilder stringBuilder = new StringBuilder(); stringBuilder.append(this.template.getModelName()); if (this.status.sex == 0) stringBuilder.append(".male");  if (this.status.sex == 1) stringBuilder.append(".female");  if (!hasTrait(15) && !hasTrait(16) && !hasTrait(18) && !hasTrait(24) && !hasTrait(25) && !hasTrait(23) && !hasTrait(30) && !hasTrait(31) && !hasTrait(32) && !hasTrait(33) && !hasTrait(34)) if (hasTrait(17)) stringBuilder.append(".black");   if (this.status.disease > 0) stringBuilder.append(".diseased");  stringBuilder.append(WurmCalendar.getSpecialMapping(true)); return stringBuilder.toString(); }  if (this.template.getTemplateId() == 119) { StringBuilder stringBuilder = new StringBuilder(); FishAI.FishAIData faid = (FishAI.FishAIData)getCreatureAIData(); FishEnums.FishData fd = faid.getFishData(); stringBuilder.append(fd.getModelName()); return stringBuilder.toString(); }  StringBuilder s = new StringBuilder(); s.append(this.template.getModelName()); if (this.status.sex == 0) s.append(".male");  if (this.status.sex == 1) s.append(".female");  if (getKingdomId() != 0) { s.append('.'); Kingdom kingdomt = Kingdoms.getKingdom(getKingdomId()); if (kingdomt.getTemplate() != getKingdomId()) s.append(Kingdoms.getSuffixFor(kingdomt.getTemplate()));  s.append(Kingdoms.getSuffixFor(getKingdomId())); if (this.status.disease > 0) s.append("diseased.");  } else { s.append('.'); if (this.status.disease > 0) s.append("diseased.");  }  s.append(WurmCalendar.getSpecialMapping(false)); return s.toString(); } public String getHitSound() { return this.template.getHitSound(getSex()); } public String getDeathSound() { return this.template.getDeathSound(getSex()); } public final boolean hasNoServerSound() { return this.template.noServerSounds(); } public void setStructure(@Nullable Structure struct) { if (struct == null) this.status.setBuildingId(-10L);  this.structure = struct; } public float getNoticeChance() { if (this.template.getTemplateId() == 29 || this.template.getTemplateId() == 28 || this.template.getTemplateId() == 4) return 0.2F;  if (this.template.getTemplateId() == 5) return 0.3F;  if (this.template.getTemplateId() == 31 || this.template.getTemplateId() == 30 || this.template.getTemplateId() == 6) return 0.4F;  if (this.template.getTemplateId() == 7) return 0.6F;  if (this.template.getTemplateId() == 33 || this.template.getTemplateId() == 32 || this.template.getTemplateId() == 8) return 0.65F;  return 1.0F; } public Structure getStructure() throws NoSuchStructureException { if (this.structure == null) throw new NoSuchStructureException("This creature has no structure");  return this.structure; } public boolean hasLink() { return false; } public short getCentimetersLong() { return this.status.getBody().getCentimetersLong(); } public short getCentimetersHigh() { return this.status.getBody().getCentimetersHigh(); } public short getCentimetersWide() { return this.status.getBody().getCentimetersWide(); } public void setCentimetersLong(short centimetersLong) { this.status.getBody().setCentimetersLong(centimetersLong); } public void setCentimetersHigh(short centimetersHigh) { this.status.getBody().setCentimetersHigh(centimetersHigh); } public void setCentimetersWide(short centimetersWide) { this.status.getBody().setCentimetersWide(centimetersWide); } public float getWeight() { return this.status.getBody().getWeight((getStatus()).fat); } public int getSize() { return this.template.getSize(); } public boolean isClimber() { return this.template.climber; } public boolean addItemWatched(Item watched) { return true; } public boolean removeItemWatched(Item watched) { return true; } public boolean isItemWatched(Item watched) { return true; } public boolean isPaying() { return true; } public boolean isReallyPaying() { return true; } public int getPower() { return 0; } public void dropItem(Item item) { long parentId = item.getParentId(); item.setPosXY(getPosX(), getPosY()); if (parentId != -10L) try { Item parent = Items.getItem(parentId); parent.dropItem(item.getWurmId(), false); } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   int tilex = getTileX(); int tiley = getTileY(); try { Zone zone = Zones.getZone(tilex, tiley, isOnSurface()); VolaTile t = zone.getOrCreateTile(tilex, tiley); if (t != null) { t.addItem(item, false, false); } else { int x = Server.rand.nextInt(Zones.worldTileSizeX); int y = Server.rand.nextInt(Zones.worldTileSizeY); t = Zones.getOrCreateTile(x, y, true); t.addItem(item, false, false); }  } catch (NoSuchZoneException nsz) { logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz); }  } public void setDestroyed() { if (this.decisions != null) { this.decisions.clearOrders(); this.decisions = null; }  getStatus().setPath(null); try { savePosition(-10); } catch (IOException iox) { logger.log(Level.WARNING, iox.getMessage(), iox); }  this.damageCounter = 0; this.status.dead = true; setLeader(null); if (this.followers != null) stopLeading();  if (isTrading()) getTrade().end(this, true);  setTarget(-10L, true); destroyVisionArea(); if (isVehicle()) Vehicles.destroyVehicle(getWurmId());  } public void destroy() { if (isDominated()) setDominator(-10L);  getCurrentTile().deleteCreature(this); setDestroyed(); if (getSpellEffects() != null) getSpellEffects().destroy(false);  try { this.skills.delete(); } catch (Exception ex) { logger.log(Level.INFO, "Error when deleting creature skills: " + ex.getMessage(), ex); }  try { Item[] items = this.possessions.getInventory().getAllItems(true); for (int x = 0; x < items.length; x++) { if (!items[x].isUnique()) { Items.destroyItem(items[x].getWurmId()); } else { dropItem(items[x]); }  }  Items.destroyItem(this.possessions.getInventory().getWurmId()); } catch (Exception e) { logger.log(Level.INFO, "Error when decaying items: " + e.getMessage(), e); }  try { Item[] items = getBody().getBodyItem().getAllItems(true); for (int x = 0; x < items.length; x++) { if (!items[x].isUnique()) { Items.destroyItem(items[x].getWurmId()); } else { dropItem(items[x]); }  }  Items.destroyItem(getBody().getBodyItem().getWurmId()); } catch (Exception e) { logger.log(Level.INFO, "Error when decaying body items: " + e.getMessage(), e); }  if (this.citizenVillage != null) { Village vill = this.citizenVillage; Guard[] guards = this.citizenVillage.getGuards(); for (Guard lGuard : guards) { if (lGuard.getCreature() == this) { vill.deleteGuard(this, false); if (isSpiritGuard()) vill.plan.destroyGuard(this);  }  }  Wagoner[] wagoners = vill.getWagoners(); for (Wagoner wagoner : wagoners) { if (wagoner.getWurmId() == getWurmId()) vill.deleteWagoner(this);  }  }  if (isNpcTrader()) if (Economy.getEconomy().getShop(this, true) != null) { if (Economy.getEconomy().getShop(this, true).getMoney() > 0L) Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() + Economy.getEconomy().getShop(this, true).getMoney());  Economy.deleteShop(this.id); }   if (isKingdomGuard()) { GuardTower tower = Kingdoms.getTower(this); if (tower != null) try { tower.destroyGuard(this); } catch (IOException iox) { logger.log(Level.WARNING, iox.getMessage(), iox); }   }  Creatures.getInstance().permanentlyDelete(this); } public boolean isBreakFence() { return this.template.isBreakFence(); } public boolean isCareful() { return this.template.isCareful(); } public final void attackTower() { if (isOnSurface()) if (!isFriendlyKingdom(getCurrentKingdom())) for (int x = Zones.safeTileX(getTileX() - 3); x < Zones.safeTileX(getTileX() + 3); x++) { for (int y = Zones.safeTileY(getTileY() - 3); y < Zones.safeTileY(getTileY() + 3); y++) { VolaTile t = Zones.getTileOrNull(x, y, isOnSurface()); if (t != null) { Item[] items = t.getItems(); for (Item i : items) { if (i.isGuardTower()) if (!isFriendlyKingdom(i.getKingdom())) { GuardTower tower = Kingdoms.getTower(i); if (i.getCurrentQualityLevel() > 50.0F) { if (tower != null) tower.sendAttackWarning();  turnTowardsTile((short)i.getTileX(), (short)i.getTileY()); playAnimation("fight_strike", false); Server.getInstance().broadCastAction(getName() + " attacks the " + i.getName() + ".", this, 5); i.setDamage(i.getDamage() + (float)(getStrengthSkill() / 1000.0D)); if (Server.rand.nextInt(300) == 0) { if (Server.rand.nextBoolean()) GuardTower.spawnCommander(i, i.getKingdom());  for (int n = 0; n < 2 + Server.rand.nextInt(4); n++) GuardTower.spawnSoldier(i, i.getKingdom());  }  } else if (!Servers.localServer.HOMESERVER && Server.rand.nextInt(300) == 0) { if (tower != null && !tower.hasLiveGuards()) { Server.getInstance().broadCastAction(getName() + " conquers the " + tower.getName() + "!", this, 5); Server.getInstance().broadCastSafe(getName() + " conquers " + tower.getName() + "."); Kingdoms.convertTowersWithin(i.getTileX() - 10, i.getTileY() - 10, i.getTileX() + 10, i.getTileY() + 10, getKingdomId()); }  }  }   }  }  }  }    } public void breakout() { if (!isDominated() && (((isCaveDweller() || isBreakFence()) && this.status.hunger >= 60000) || isUnique())) if (!isSubmerged() && Server.rand.nextInt(100) == 0) { Village breakoutVillage = Zones.getVillage(getTileX(), getTileY(), isOnSurface()); if (breakoutVillage != null && breakoutVillage.isPermanent) return;  if (isBreakFence()) if (this.currentTile != null) { if (this.currentTile.getStructure() != null) { Wall[] walls = this.currentTile.getWallsForLevel(getFloorLevel()); if (walls.length > 0) { Wall tobreak = walls[Server.rand.nextInt(walls.length)]; if (!tobreak.isIndestructible()) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + tobreak.getName() + ".", this, 5); if (isUnique()) { tobreak.setDamage(tobreak.getDamage() + 100.0F); } else { tobreak.setDamage(tobreak.getDamage() + (float)getStrengthSkill() / 10.0F * tobreak.getDamageModifier()); }  }  }  }  boolean onSurface = true; if ((isOnSurface() || this.currentTile.isTransition) && isUnique()) { VolaTile t = Zones.getTileOrNull(getTileX() + 1, getTileY(), true); if (t != null) { Wall[] walls = t.getWallsForLevel(Math.max(0, getFloorLevel())); if (walls.length > 0) for (Wall tobreak : walls) { if (!tobreak.isIndestructible()) if (tobreak.getTileX() == getTileX() + 1 && !tobreak.isHorizontal()) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + tobreak.getName() + ".", this, 5); if (isUnique()) { tobreak.setDamage(tobreak.getDamage() + 100.0F); } else { tobreak.setDamage(tobreak.getDamage() + (float)getStrengthSkill() / 10.0F * tobreak.getDamageModifier()); }  }   }   }  t = Zones.getTileOrNull(getTileX() - 1, getTileY(), true); if (t != null) { Wall[] walls = t.getWallsForLevel(Math.max(0, getFloorLevel())); if (walls.length > 0) for (Wall tobreak : walls) { if (!tobreak.isIndestructible()) if (tobreak.getTileX() == getTileX() && !tobreak.isHorizontal()) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + tobreak.getName() + ".", this, 5); if (isUnique()) { tobreak.setDamage(tobreak.getDamage() + 100.0F); } else { tobreak.setDamage(tobreak.getDamage() + (float)getStrengthSkill() / 10.0F * tobreak.getDamageModifier()); }  }   }   }  t = Zones.getTileOrNull(getTileX(), getTileY() - 1, true); if (t != null) { Wall[] walls = t.getWallsForLevel(Math.max(0, getFloorLevel())); if (walls.length > 0) for (Wall tobreak : walls) { if (!tobreak.isIndestructible()) if (tobreak.getTileY() == getTileY() && tobreak.isHorizontal()) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + tobreak.getName() + ".", this, 5); if (isUnique()) { tobreak.setDamage(tobreak.getDamage() + 100.0F); } else { tobreak.setDamage(tobreak.getDamage() + (float)getStrengthSkill() / 10.0F * tobreak.getDamageModifier()); }  }   }   }  t = Zones.getTileOrNull(getTileX(), getTileY() + 1, true); if (t != null) { Wall[] walls = t.getWallsForLevel(Math.max(0, getFloorLevel())); if (walls.length > 0) for (Wall tobreak : walls) { if (!tobreak.isIndestructible()) if (tobreak.getTileY() == getTileY() + 1 && tobreak.isHorizontal()) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + tobreak.getName() + ".", this, 5); if (isUnique()) { tobreak.setDamage(tobreak.getDamage() + 100.0F); } else { tobreak.setDamage(tobreak.getDamage() + (float)getStrengthSkill() / 10.0F * tobreak.getDamageModifier()); }  }   }   }  }  Fence[] fences = this.currentTile.getFencesForLevel(this.currentTile.isTransition ? 0 : getFloorLevel()); boolean onlyHoriz = false; boolean onlyVert = false; if (fences == null) { if (isOnSurface()) { if (fences == null) { VolaTile t = Zones.getTileOrNull(this.currentTile.getTileX() + 1, this.currentTile.getTileY(), true); if (t != null) { fences = t.getFencesForLevel(getFloorLevel()); onlyVert = true; }  }  if (fences == null) { VolaTile t = Zones.getTileOrNull(this.currentTile.getTileX(), this.currentTile.getTileY() + 1, true); if (t != null) { fences = t.getFencesForLevel(getFloorLevel()); onlyHoriz = true; }  }  }  if (this.currentTile.isTransition) { if (!isOnSurface()) { VolaTile t = Zones.getTileOrNull(this.currentTile.getTileX(), this.currentTile.getTileY(), true); if (t != null) fences = t.getFencesForLevel(Math.max(0, getFloorLevel()));  if (fences == null) { t = Zones.getTileOrNull(this.currentTile.getTileX() + 1, this.currentTile.getTileY(), true); if (t != null) { fences = t.getFencesForLevel(Math.max(0, getFloorLevel())); onlyVert = true; }  }  if (fences == null) { t = Zones.getTileOrNull(this.currentTile.getTileX(), this.currentTile.getTileY() + 1, true); if (t != null) { fences = t.getFencesForLevel(Math.max(0, getFloorLevel())); onlyHoriz = true; }  }  }  if (getFloorLevel() <= 0) if (Tiles.isMineDoor(Tiles.decodeType(Zones.getTileIntForTile(this.currentTile.tilex, this.currentTile.tiley, 0)))) { int currQl = Server.getWorldResource(this.currentTile.tilex, this.currentTile.tiley); int damage = 1000; currQl = Math.max(0, currQl - 1000); Server.setWorldResource(this.currentTile.tilex, this.currentTile.tiley, currQl); try { MethodsStructure.sendDestroySound(this, getBody().getBodyPart(13), (Tiles.decodeType(Server.surfaceMesh.getTile(this.currentTile.tilex, this.currentTile.tiley)) == 25)); } catch (Exception ex) { logger.log(Level.INFO, getName() + ex.getMessage()); }  if (currQl == 0) { TileEvent.log(this.currentTile.tilex, this.currentTile.tiley, 0, getWurmId(), 174); TileEvent.log(this.currentTile.tilex, this.currentTile.tiley, -1, getWurmId(), 174); if (Tiles.decodeType(Server.caveMesh.getTile(this.currentTile.tilex, this.currentTile.tiley)) == Tiles.Tile.TILE_CAVE_EXIT.id) { Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(Server.surfaceMesh.getTile(this.currentTile.tilex, this.currentTile.tiley)), Tiles.Tile.TILE_HOLE.id, (byte)0); } else { Server.setSurfaceTile(this.currentTile.tilex, this.currentTile.tiley, Tiles.decodeHeight(Server.surfaceMesh.getTile(this.currentTile.tilex, this.currentTile.tiley)), Tiles.Tile.TILE_ROCK.id, (byte)0); }  Players.getInstance().sendChangedTile(this.currentTile.tilex, this.currentTile.tiley, true, true); MineDoorPermission.deleteMineDoor(this.currentTile.tilex, this.currentTile.tiley); Server.getInstance().broadCastAction(getName() + " damages a door and the last parts fall down with a crash.", this, 5); } else { Server.getInstance().broadCastAction(getName() + " damages the door.", this, 5); }  }   }  }  if (fences != null) for (Fence f : fences) { if (!f.isIndestructible()) if (f.isHorizontal()) { if (!onlyVert) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + f.getName() + ".", this, 5); if (isUnique()) { f.setDamage(f.getDamage() + Server.rand.nextInt(100)); } else { if (f.getVillage() != null) f.getVillage().addTarget(this);  f.setDamage(f.getDamage() + (float)getStrengthSkill() / 10.0F * f.getDamageModifier()); }  }  } else if (!onlyHoriz) { Server.getInstance().broadCastAction("The " + getName() + " smashes the " + f.getName() + ".", this, 5); if (isUnique()) { f.setDamage(f.getDamage() + Server.rand.nextInt(100)); } else { if (f.getVillage() != null) f.getVillage().addTarget(this);  f.setDamage(f.getDamage() + (float)getStrengthSkill() / 10.0F * f.getDamageModifier()); }  }   }   }   if (isUnique()) if (!isOnSurface() && Server.rand.nextInt(500) == 0) { boolean breakReinforcement = isUnique(); int tx = Zones.safeTileX(getTileX() - 1); int ty = Zones.safeTileY(getTileY()); int t = Zones.getTileIntForTile(tx, ty, 0); if (Tiles.isMineDoor(Tiles.decodeType(t))) { int currQl = Server.getWorldResource(tx, ty); try { MethodsStructure.sendDestroySound(this, getBody().getBodyPart(13), (Tiles.decodeType(Server.surfaceMesh.getTile(tx, ty)) == 25)); currQl = Math.max(0, currQl - 1000); Server.setWorldResource(tx, ty, currQl); if (currQl == 0) { TileEvent.log(tx, ty, 0, getWurmId(), 174); TileEvent.log(tx, ty, -1, getWurmId(), 174); if (Tiles.decodeType(Server.caveMesh.getTile(tx, ty)) == Tiles.Tile.TILE_CAVE_EXIT.id) { Server.setSurfaceTile(tx, ty, Tiles.decodeHeight(Server.surfaceMesh.getTile(tx, ty)), Tiles.Tile.TILE_HOLE.id, (byte)0); } else { Server.setSurfaceTile(tx, ty, Tiles.decodeHeight(Server.surfaceMesh.getTile(tx, ty)), Tiles.Tile.TILE_ROCK.id, (byte)0); }  Players.getInstance().sendChangedTile(tx, ty, true, true); MineDoorPermission.deleteMineDoor(tx, ty); Server.getInstance().broadCastAction(getNameWithGenus() + " damages a door and the last parts fall down with a crash.", this, 5); }  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage()); }  }  t = Zones.getTileIntForTile(tx, ty, -1); if (breakReinforcement && Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) { Server.caveMesh.setTile(tx, ty, Tiles.encode(Tiles.decodeHeight(t), Tiles.Tile.TILE_CAVE_WALL.id, Tiles.decodeData(t))); Players.getInstance().sendChangedTile(tx, ty, false, true); }  if (Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL.id) { Village v = Zones.getVillage(tx, ty, true); if (v == null || isOnPvPServer() || isUnique()) { TileRockBehaviour.createInsideTunnel(tx, ty, t, this, 145 + Server.rand.nextInt(3), 2, false, null); if (v != null) v.addTarget(this);  }  }  tx = Zones.safeTileX(getTileX()); ty = Zones.safeTileY(getTileY() - 1); t = Zones.getTileIntForTile(tx, ty, -1); if (breakReinforcement && Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) { Server.caveMesh.setTile(tx, ty, Tiles.encode(Tiles.decodeHeight(t), Tiles.Tile.TILE_CAVE_WALL.id, Tiles.decodeData(t))); Players.getInstance().sendChangedTile(tx, ty, false, true); }  if (Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL.id) { Village v = Zones.getVillage(tx, ty, true); if (v == null || isOnPvPServer() || isUnique()) { TileRockBehaviour.createInsideTunnel(tx, ty, t, this, 145 + Server.rand.nextInt(3), 3, false, null); if (v != null) v.addTarget(this);  }  }  tx = Zones.safeTileX(getTileX() + 1); ty = Zones.safeTileY(getTileY()); t = Zones.getTileIntForTile(tx, ty, -1); if (breakReinforcement && Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) { Server.caveMesh.setTile(tx, ty, Tiles.encode(Tiles.decodeHeight(t), Tiles.Tile.TILE_CAVE_WALL.id, Tiles.decodeData(t))); Players.getInstance().sendChangedTile(tx, ty, false, true); }  if (Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL.id) { Village v = Zones.getVillage(tx, ty, true); if (v == null || isOnPvPServer() || isUnique()) { TileRockBehaviour.createInsideTunnel(tx, ty, t, this, 145 + Server.rand.nextInt(3), 4, false, null); if (v != null) v.addTarget(this);  }  }  tx = Zones.safeTileX(getTileX()); ty = Zones.safeTileY(getTileY() + 1); t = Zones.getTileIntForTile(tx, ty, -1); if (breakReinforcement && Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL_REINFORCED.id) { Server.caveMesh.setTile(tx, ty, Tiles.encode(Tiles.decodeHeight(t), Tiles.Tile.TILE_CAVE_WALL.id, Tiles.decodeData(t))); Players.getInstance().sendChangedTile(tx, ty, false, true); }  if (Tiles.decodeType(t) == Tiles.Tile.TILE_CAVE_WALL.id) { Village v = Zones.getVillage(tx, ty, true); if (v == null || isOnPvPServer() || isUnique()) { TileRockBehaviour.createInsideTunnel(tx, ty, t, this, 145 + Server.rand.nextInt(3), 5, false, null); if (v != null) v.addTarget(this);  }  }  }   }   } public int getMaxHuntDistance() { if (isDominated()) return 20;  return this.template.getMaxHuntDistance(); } public Path findPath(int targetX, int targetY, @Nullable PathFinder pathfinder) throws NoPathException { Path path = null; PathFinder pf = (pathfinder != null) ? pathfinder : new PathFinder(); setPathfindcounter(getPathfindCounter() + 1); if (getPathfindCounter() < 10 || this.target != -10L || getPower() > 0) { if (isSpiritGuard() && this.citizenVillage != null) { if (this.target == -10L) { if (isWithinTileDistanceTo(targetX, targetY, (int)(this.status.getPositionZ() + getAltOffZ()) >> 2, getMaxHuntDistance())) path = pf.findPath(this, getTileX(), getTileY(), targetX, targetY, isOnSurface(), 10);  } else { try { path = pf.findPath(this, getTileX(), getTileY(), targetX, targetY, isOnSurface(), 10); } catch (NoPathException nsp) { if (this.currentVillage == this.citizenVillage) { if (targetX < this.citizenVillage.getStartX() - 5 || targetX > this.citizenVillage.getEndX() + 5 || targetY < this.citizenVillage.getStartY() - 5 || targetY > this.citizenVillage.getEndY() + 5) { this.setTargetNOID = true; } else if (getTarget() != null) { this.creatureToBlinkTo = getTarget(); return null; }  } else if (getTarget() != null) { this.creatureToBlinkTo = getTarget(); return null; }  }  }  } else if (isWithinTileDistanceTo(targetX, targetY, (int)this.status.getPositionZ() >> 2, Math.max(getMaxHuntDistance(), this.template.getVision()))) { path = pf.findPath(this, getTileX(), getTileY(), targetX, targetY, isOnSurface(), 5); } else if (isUnique() || isKingdomGuard() || isDominated() || this.template.isTowerBasher()) { if (this.target == -10L) { path = pf.findPath(this, getTileX(), getTileY(), targetX, targetY, isOnSurface(), 5); } else { this.setTargetNOID = true; }  }  } else { throw new NoPathException("No pathing now"); }  if (path != null) setPathfindcounter(0);  return path; } public boolean isSentinel() { return this.template.isSentinel(); } public boolean isNpc() { return false; } public boolean isTrader() { if (isReborn()) return false;  if (this.template.getTemplateId() == 1 && !isPlayer()) return false;  return this.template.isTrader(); } public boolean canEat() { return getStatus().canEat(); } public boolean isHungry() { return getStatus().isHungry(); } public boolean isNeedFood() { return this.template.isNeedFood(); } public boolean isMoveRandom() { return this.template.isMoveRandom(); } public boolean isSwimming() { return this.template.isSwimming(); } public boolean isAnimal() { return this.template.isAnimal(); } public boolean isHuman() { return this.template.isHuman(); } public boolean isRegenerating() { return (this.template.isRegenerating() || isUndead()); } public boolean isDragon() { return this.template.isDragon(); } public boolean isTypeFleeing() { return (isSpy() || this.template.isFleeing()); } public boolean isMonster() { return this.template.isMonster(); } public boolean isInvulnerable() { return this.template.isInvulnerable(); } public boolean isNpcTrader() { return this.template.isNpcTrader(); } public boolean isAggHuman() { if (isReborn()) return true;  return this.template.isAggHuman(); } public boolean isMoveLocal() { return (this.template.isMoveLocal() && this.status.modtype != 99); } public boolean isMoveGlobal() { boolean shouldMove = false; if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) if (getCurrentTile().getVillage() != null) if ((isBred() || isBranded() || isCaredFor()) && this.target == -10L) shouldMove = true;    return (this.template.isMoveGlobal() || this.status.modtype == 99 || shouldMove); } public boolean shouldFlee() { if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) { if (getCurrentTile().getVillage() != null) if (isBred() || isBranded() || isCaredFor()) return false;   if (getStatus().isChampion()) return false;  if (this.fleeCounter > 0) { Long[] visibleCreatures = getVisionArea().getSurface().getCreatures(); for (Long lCret : visibleCreatures) { try { Creature cret = Server.getInstance().getCreature(lCret.longValue()); if ((cret.getPower() == 0 || Servers.localServer.testServer) && (cret.isPlayer() || cret.isAggHuman() || cret.isCarnivore() || cret.isMonster() || cret.isHunter())) { float modifier = 1.0F; if (getCurrentTile().getVillage() != null && cret.isPlayer()) modifier = 2.0F;  sendToLoggers("checking if should flee from " + cret.getName() + ": " + (cret.getBaseCombatRating() - Math.abs(cret.getPos2f().distance(getPos2f()) / 4.0F)) + " vs " + (getBaseCombatRating() * modifier)); if (cret.getBaseCombatRating() - Math.abs(cret.getPos2f().distance(getPos2f()) / 2.0F) > getBaseCombatRating() * modifier) return true;  }  } catch (NoSuchPlayerException|NoSuchCreatureException noSuchPlayerException) {} }  }  return false; }  if (getStatus().isChampion()) return false;  return (this.fleeCounter > 0); } public boolean isGrazer() { return this.template.isGrazer(); } public boolean isHerd() { return this.template.isHerd(); } public boolean isHunter() { return this.template.isHunter(); } public boolean isMilkable() { return (this.template.isMilkable() && getSex() == 1 && (getStatus()).age >= 3); } public boolean isReborn() { return (getStatus()).reborn; } public boolean isDominatable(Creature aDominator) { if (getLeader() != null && getLeader() != aDominator) return false;  if (isRidden() || this.hitchedTo != null) return false;  return this.template.isDominatable(); } public final int getAggressivity() { return this.template.getAggressivity(); } final byte getCombatDamageType() { return this.template.getCombatDamageType(); } final float getBreathDamage() { if (isUndead()) return 10.0F;  if (isReborn()) return Math.max(3.0F, this.template.getBreathDamage());  return this.template.getBreathDamage(); } public float getHandDamage() { if (isUndead()) return 5.0F;  if (isReborn()) return Math.max(3.0F, this.template.getHandDamage());  return this.template.getHandDamage(); } public float getBiteDamage() { if (isUndead()) return 8.0F;  if (isReborn()) return Math.max(5.0F, this.template.getBiteDamage());  return this.template.getBiteDamage(); } public float getKickDamage() { if (isReborn()) return Math.max(2.0F, this.template.getKickDamage());  return this.template.getKickDamage(); } public float getHeadButtDamage() { if (isReborn()) return Math.max(4.0F, this.template.getKickDamage());  return this.template.getHeadButtDamage(); } public Logger getLogger() { return null; } public boolean isUnique() { return this.template.isUnique(); } public boolean isKingdomGuard() { return this.template.isKingdomGuard(); } public boolean isGuard() { return (isKingdomGuard() || isSpiritGuard() || isWarGuard()); } public boolean isGhost() { return this.template.isGhost(); } public boolean unDead() { return this.template.isUndead(); } public final boolean onlyAttacksPlayers() { return this.template.onlyAttacksPlayers(); } public boolean isSpiritGuard() { return this.template.isSpiritGuard(); } public boolean isZombieSummoned() { return (this.template.getTemplateId() == 69); }
/*       */   public boolean isBartender() { return this.template.isBartender(); }
/*       */   public boolean isDefendKingdom() { return this.template.isDefendKingdom(); }
/*       */   public boolean isNotFemale() { return (getSex() != 1); }
/*       */   public boolean isAggWhitie() { return (this.template.isAggWhitie() || isReborn()); }
/*       */   public boolean isHerbivore() { return this.template.isHerbivore(); }
/*       */   public boolean isCarnivore() { return this.template.isCarnivore(); }
/* 19968 */   private void pollMount() { if (isRidden())
/*       */     {
/* 19970 */       if (this.mountPollCounter <= 0 || Server.rand.nextInt(100) == 0)
/*       */       
/* 19972 */       { Vehicle vehic = Vehicles.getVehicleForId(getWurmId());
/* 19973 */         if (vehic != null) {
/*       */           
/*       */           try {
/*       */ 
/*       */             
/* 19978 */             Creature rider = Server.getInstance().getCreature((vehic.getPilotSeat()).occupant);
/* 19979 */             byte val = vehic.calculateNewMountSpeed(this, false);
/* 19980 */             if (this.switchv)
/* 19981 */               val = (byte)(val - 1); 
/* 19982 */             this.switchv = !this.switchv;
/* 19983 */             rider.getMovementScheme().addMountSpeed((short)val);
/*       */           }
/* 19985 */           catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */ 
/*       */           
/*       */           }
/* 19990 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */         }
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 19996 */         this.mountPollCounter = 20; }
/*       */       else
/*       */       
/* 19999 */       { this.mountPollCounter--; }  }  } public boolean isOmnivore() { return this.template.isOmnivore(); } public boolean isCaveDweller() { return this.template.isCaveDweller(); } public boolean isSubmerged() { return this.template.isSubmerged(); } public boolean isEggLayer() { return this.template.isEggLayer(); } public int getEggTemplateId() { return this.template.getEggTemplateId(); } public int getMaxGroupAttackSize() { if (isUnique()) return 100;  float mod = getStatus().getBattleRatingTypeModifier(); return (int)Math.max(this.template.getMaxGroupAttackSize(), this.template.getMaxGroupAttackSize() * mod); } public int getGroupSize() { int nums = 0; for (int x = Math.max(0, getCurrentTile().getTileX() - 3); x < Math.min(getCurrentTile().getTileX() + 3, Zones.worldTileSizeX - 1); x++) { for (int y = Math.max(0, getCurrentTile().getTileY() - 3); y < Math.min(getCurrentTile().getTileY() + 3, Zones.worldTileSizeY - 1); y++) { VolaTile t = Zones.getTileOrNull(x, y, isOnSurface()); if (t != null) if ((t.getCreatures()).length > 0) { Creature[] xret = t.getCreatures(); for (Creature lElement : xret) { if (lElement.getTemplate().getTemplateId() == this.template.getTemplateId() || lElement.getTemplate().getTemplateId() == this.template.getLeaderTemplateId()) nums++;  }  }   }  }  return nums; } public final TilePos getAdjacentTilePos(TilePos pos) { switch (Server.rand.nextInt(8)) { case 0: return pos.East();case 1: return pos.South();case 2: return pos.West();case 3: return pos.North();case 4: return pos.NorthEast();case 5: return pos.NorthWest();case 6: return pos.SouthWest();case 7: return pos.SouthEast(); }  return pos; } public void checkEggLaying() { if (isEggLayer()) if (this.template.getTemplateId() == 53) { if (Server.rand.nextInt(7200) == 0) if (WurmCalendar.isAfterEaster()) { destroy(); Server.getInstance().broadCastAction(getNameWithGenus() + " suddenly vanishes down into a hole!", this, 10); } else { try { Item egg = ItemFactory.createItem(466, 50.0F, null); egg.putItemInfrontof(this); Server.getInstance().broadCastAction(getNameWithGenus() + " throws something in the air!", this, 10); } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }  }   } else if (this.status.getSex() == 1 && isNeedFood() && !canEat()) { if (Items.mayLayEggs() || isUnique()) if (Server.rand.nextInt(20000 * (isUnique() ? 1000 : 1)) == 0) if (isOnSurface()) { byte type = Tiles.decodeType(Server.surfaceMesh.getTile((getCurrentTile()).tilex, (getCurrentTile()).tiley)); if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_DIRT_PACKED.id) { int templateId = 464; if (this.template.getSize() > 4) templateId = 465;  try { Item egg = ItemFactory.createItem(templateId, 99.0F, getPosX(), getPosY(), this.status.getRotation(), isOnSurface(), (byte)0, getStatus().getBridgeId(), null); if (templateId == 465 || Server.rand.nextInt(5) == 0) egg.setData1(this.template.getEggTemplateId());  } catch (NoSuchTemplateException nst) { logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst); } catch (FailedException fe) { logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe); }  this.status.hunger = 60000; }  }    }   } public boolean isNoSkillFor(Creature attacker) { if ((isKingdomGuard() || isSpiritGuard() || isZombieSummoned() || (isPlayer() && attacker.isPlayer()) || isWarGuard()) && isFriendlyKingdom(attacker.getKingdomId())) return true;  if (isPlayer() && attacker.isPlayer()) { if (Players.getInstance().isOverKilling(attacker.getWurmId(), getWurmId())) return true;  if (((Player)this).getSaveFile().getIpaddress().equals(((Player)attacker).getSaveFile().getIpaddress())) return true;  }  return false; } public int[] forageForFood(VolaTile currTile) { int[] toReturn = { -1, -1 }; if (canEat() && isNeedFood()) for (int x = -2; x <= 2; x++) { for (int y = -2; y <= 2; y++) { VolaTile t = Zones.getTileOrNull(Zones.safeTileX(currTile.getTileX() + x), Zones.safeTileY(currTile.getTileY() + y), isOnSurface()); if (t != null) { Item[] its = t.getItems(); for (Item lIt : its) { if (lIt.isEdibleBy(this)) if (Server.rand.nextInt(10) == 0) { sendToLoggers("Found " + lIt.getName()); toReturn[0] = Zones.safeTileX(currTile.getTileX() + x); toReturn[1] = Zones.safeTileY(currTile.getTileY() + y); return toReturn; }   }  }  if (isGrazer() && canEat() && Server.rand.nextInt(9) == 0) { byte type = Zones.getTextureForTile(Zones.safeTileX(currTile.getTileX() + x), Zones.safeTileY(currTile.getTileY() + y), getLayer()); if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_FIELD.id || type == Tiles.Tile.TILE_FIELD2.id || type == Tiles.Tile.TILE_STEPPE.id || type == Tiles.Tile.TILE_ENCHANTED_GRASS.id) { sendToLoggers("Found grass or field"); toReturn[0] = Zones.safeTileX(currTile.getTileX() + x); toReturn[1] = Zones.safeTileY(currTile.getTileY() + y); return toReturn; }  }  }  }   return toReturn; } public void blinkTo(int tilex, int tiley, int layer, int floorLevel) { getCurrentTile().deleteCreatureQuick(this); try { setPositionX(((tilex << 2) + 2)); setPositionY(((tiley << 2) + 2)); setLayer(Math.min(0, layer), false); if (floorLevel > 0) { pushToFloorLevel(floorLevel); } else { setPositionZ(Zones.calculateHeight(getStatus().getPositionX(), getStatus().getPositionY(), isOnSurface())); }  Zone z = Zones.getZone(tilex, tiley, (layer >= 0)); z.addCreature(getWurmId()); } catch (Exception ex) { logger.log(Level.WARNING, getName() + " - " + tilex + ", " + tiley + ", " + layer + ", " + floorLevel + ": " + ex.getMessage(), ex); }  } public final boolean isBeachDweller() { return this.template.isBeachDweller(); } public final boolean isWoolProducer() { return this.template.isWoolProducer(); } public boolean isTargetTileTooHigh(int targetX, int targetY, int currentTileNum, boolean swimming) { if (getFloorLevel() > 0) return false;  if (isFlying()) return false;  short currheight = Tiles.decodeHeight(currentTileNum); short[] lSteepness = getTileSteepness(targetX, targetY, isOnSurface()); if (swimming && lSteepness[0] < -200 && currheight > lSteepness[0] && !isFloating()) return true;  if (isBeachDweller()) { if (currheight > 20 && lSteepness[0] > currheight) return true;  if (currheight < 0 && lSteepness[0] > 0 && !WurmCalendar.isNight()) return true;  }  if (isOnSurface()) { VolaTile stile = Zones.getTileOrNull(targetX, targetY, isOnSurface()); if (stile != null) if (stile.getStructure() != null && stile.getStructure().isTypeBridge()) if (stile.getStructure().isHorizontal()) { if (stile.getStructure().getMaxX() == stile.getTileX() || stile.getStructure().getMinX() == stile.getTileX()) return false;  } else if (stile.getStructure().getMaxY() == stile.getTileY() || stile.getStructure().getMinY() == stile.getTileY()) { return false; }    }  if (currheight < 500) return false;  if (!swimming && (lSteepness[0] - currheight) > 60.0D * Math.max(1.0D, getTileRange(this, targetX, targetY)) && lSteepness[1] > 20) { if (Creatures.getInstance().isLog()) logger.log(Level.INFO, getName() + " Skipping moving up since avg steep=" + lSteepness[0] + "=" + (lSteepness[0] - currheight) + ">" + (60.0D * Math.max(1.0D, getTileRange(this, targetX, targetY))) + " at " + targetX + "," + targetY + " from " + getTileX() + ", " + getTileY());  return true; }  if (!swimming && (currheight - lSteepness[0]) > 60.0D * Math.max(1.0D, getTileRange(this, targetX, targetY)) && lSteepness[1] > 20) { if (Creatures.getInstance().isLog()) logger.log(Level.INFO, getName() + " Skipping moving down since avg steep=" + lSteepness[0] + "=" + (lSteepness[0] - currheight) + ">" + (60.0D * Math.max(1.0D, getTileRange(this, targetX, targetY))) + " at " + targetX + "," + targetY + " from " + getTileX() + ", " + getTileY());  return true; }  return false; } public final long getBridgeId() { if (getStatus().getPosition() != null) return getStatus().getPosition().getBridgeId();  return -10L; } public final boolean isWarGuard() { return this.template.isWarGuard(); } public PathTile getMoveTarget(int seed) { if (getStatus() == null) return null;  long now = System.currentTimeMillis(); float lPosX = this.status.getPositionX(); float lPosY = this.status.getPositionY(); boolean hasTarget = false; int tilePosX = (int)lPosX >> 2; int tilePosY = (int)lPosY >> 2; int tx = tilePosX; int ty = tilePosY; try { if (this.target == -10L || (this.fleeCounter > 0 && this.target == -10L)) { boolean flee = false; if (isDominated() && this.fleeCounter <= 0) { if (hasOrders()) { Order order = getFirstOrder(); if (order.isTile()) { boolean swimming = false; int ctile = isOnSurface() ? Server.surfaceMesh.getTile(tx, ty) : Server.caveMesh.getTile(tx, ty); if (Tiles.decodeHeight(ctile) <= 0) swimming = true;  int tile = Zones.getTileIntForTile(order.getTileX(), order.getTileY(), getLayer()); if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (Tiles.decodeHeight(tile) > 0 || swimming)) if (isOnSurface()) { if (!isTargetTileTooHigh(order.getTileX(), order.getTileY(), tile, swimming)) { hasTarget = true; tilePosX = order.getTileX(); tilePosY = order.getTileY(); }  } else { hasTarget = true; tilePosX = order.getTileX(); tilePosY = order.getTileY(); }   } else if (order.isCreature()) { Creature lTarget = order.getCreature(); if (lTarget != null) if (lTarget.isDead()) { removeOrder(order); } else { hasTarget = true; tilePosX = (lTarget.getCurrentTile()).tilex; tilePosY = (lTarget.getCurrentTile()).tiley; }   }  }  } else if (isTypeFleeing() || shouldFlee()) { if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) { if (isOnSurface() && getVisionArea() != null && getVisionArea().getSurface() != null) { int heatmapSize = this.template.getVision() * 2 + 1; float[][] rangeHeatmap = new float[heatmapSize][heatmapSize]; for (int i = 0; i < heatmapSize; i++) { for (int j = 0; j < heatmapSize; j++) rangeHeatmap[i][j] = -100.0F;  }  Long[] visibleCreatures = getVisionArea().getSurface().getCreatures(); for (Long lCret : visibleCreatures) { try { Creature cret = Server.getInstance().getCreature(lCret.longValue()); float tileModifier = 0.0F; int diffX = (int)(cret.getPosX() - getPosX()) >> 2; int diffY = (int)(cret.getPosY() - getPosY()) >> 2; for (int j = 0; j < heatmapSize; j++) { for (int k = 0; k < heatmapSize; k++) { int deltaX = Math.abs(this.template.getVision() + diffX - j); int deltaY = Math.abs(this.template.getVision() + diffY - k); if ((cret.getPower() == 0 || Servers.localServer.testServer) && (cret.isPlayer() || cret.isAggHuman() || cret.isCarnivore() || cret.isMonster() || cret.isHunter())) { tileModifier = cret.getBaseCombatRating(); if (cret.isBred() || cret.isBranded() || cret.isCaredFor()) tileModifier /= 3.0F;  if (cret.isDominated()) tileModifier /= 3.0F;  tileModifier -= Math.max(deltaX, deltaY); } else { tileModifier = 1.0F; }  rangeHeatmap[j][k] = rangeHeatmap[j][k] + tileModifier; }  }  } catch (NoSuchPlayerException|NoSuchCreatureException noSuchPlayerException) {} }  float currentVal = rangeHeatmap[this.template.getVision()][this.template.getVision()]; int currentValCount = 1; int currentTileHeight = Tiles.decodeHeight(Server.surfaceMesh.getTile(Zones.safeTileX(getTileX()), Zones.safeTileY(getTileY()))); int y; for (y = 0; y < heatmapSize; y++) { for (int x = 0; x < heatmapSize; x++) { int tileHeight = Tiles.decodeHeight(Server.surfaceMesh.getTile(Zones.safeTileX(getTileX() + x - this.template.getVision()), Zones.safeTileY(getTileY() + y - this.template.getVision()))); if (!isSubmerged() && tileHeight < 0) { if (!isSwimming()) { rangeHeatmap[x][y] = rangeHeatmap[x][y] + (100 + -tileHeight); } else { rangeHeatmap[x][y] = rangeHeatmap[x][y] + -tileHeight; }  } else if (tileHeight > 0) { rangeHeatmap[x][y] = rangeHeatmap[x][y] + (Math.abs(currentTileHeight - tileHeight) / 15); }  float testVal = rangeHeatmap[x][y]; if (testVal == currentVal) { currentValCount++; } else if (testVal < currentVal) { currentValCount = 1; currentVal = testVal; }  }  }  for (y = 0; y < heatmapSize && !flee; y++) { for (int x = 0; x < heatmapSize && !flee; x++) { if (currentVal == rangeHeatmap[x][y] && Server.rand.nextInt((int)Math.max(1.0F, currentValCount * 0.75F)) == 0) { tilePosX = tx + x - this.template.getVision(); tilePosY = ty + y - this.template.getVision(); flee = true; }  }  }  if (!flee) return null;  }  } else if (isOnSurface()) { if (Server.rand.nextBoolean()) { if (getCurrentTile() != null && getCurrentTile().getVillage() != null) { Long[] crets = getVisionArea().getSurface().getCreatures(); for (Long lCret : crets) { try { Creature cret = Server.getInstance().getCreature(lCret.longValue()); if (cret.getPower() == 0 && (cret.isPlayer() || cret.isAggHuman() || cret.isCarnivore() || cret.isMonster())) { if (cret.getPosX() > getPosX()) { tilePosX -= Server.rand.nextInt(6); } else { tilePosX += Server.rand.nextInt(6); }  if (cret.getPosY() > getPosY()) { tilePosY -= Server.rand.nextInt(6); } else { tilePosY += Server.rand.nextInt(6); }  flee = true; break; }  } catch (Exception exception) {} }  }  } else { for (Player p : Players.getInstance().getPlayers()) { if ((p.getPower() == 0 || Servers.localServer.testServer) && p.getVisionArea() != null && p.getVisionArea().getSurface() != null && p.getVisionArea().getSurface().containsCreature(this)) { if (p.getPosX() > getPosX()) { tilePosX -= Server.rand.nextInt(6); } else { tilePosX += Server.rand.nextInt(6); }  if (p.getPosY() > getPosY()) { tilePosY -= Server.rand.nextInt(6); } else { tilePosY += Server.rand.nextInt(6); }  flee = true; break; }  }  }  }  if (isSpy()) { int[] empty = { -1, -1 }; int[] newarr = getSpySpot(empty); if (newarr[0] > 0 && newarr[1] > 0) { flee = true; tilePosX = newarr[0]; tilePosY = newarr[1]; }  }  }  if (isMoveLocal() && !flee && !hasTarget) { VolaTile currTile = getCurrentTile(); if (isUnique() && Server.rand.nextInt(10) == 0) { Den den = Dens.getDen(this.template.getTemplateId()); if (den != null && (den.getTilex() != tx || den.getTiley() != ty)) { tilePosX = den.getTilex(); tilePosY = den.getTiley(); }  } else if (currTile != null) { int rand = Server.rand.nextInt(9); int tpx = currTile.getTileX() + 4 - rand; rand = Server.rand.nextInt(9); int tpy = currTile.getTileY() + 4 - rand; totx += currTile.getTileX() - tpx; toty += currTile.getTileY() - tpy; int[] foodSpot = forageForFood(currTile); boolean abort = false; if (Server.rand.nextBoolean()) if (foodSpot[0] != -1) { tpx = foodSpot[0]; tpy = foodSpot[1]; } else if (this.template.isTowerBasher() && Servers.localServer.PVPSERVER) { GuardTower closestTower = Kingdoms.getClosestEnemyTower(getTileX(), getTileY(), true, this); if (closestTower != null) { tilePosX = closestTower.getTower().getTileX(); tilePosY = closestTower.getTower().getTileY(); abort = true; }  } else if (isWarGuard()) { tilePosX = Zones.safeTileX(tpx); tilePosY = Zones.safeTileY(tpy); if (!isOnSurface()) { int[] tiles = { tilePosX, tilePosY }; if ((getCurrentTile()).isTransition) { setLayer(0, true); } else { tiles = findRandomCaveExit(tiles); if (tiles[0] != tilePosX && tiles[1] != tilePosY) { tilePosX = tiles[0]; tilePosY = tiles[1]; abort = true; } else { setLayer(0, true); }  }  } else { GuardTower gt = getGuardTower(); if (gt == null) gt = Kingdoms.getClosestTower(getTileX(), getTileY(), true);  boolean towerFound = false; if (gt != null && gt.getKingdom() == getKingdomId()) towerFound = true;  Item wtarget = Kingdoms.getClosestWarTarget(tx, ty, this); if (wtarget != null) if (!towerFound || getTileRange(this, wtarget.getTileX(), wtarget.getTileY()) < getTileRange(this, gt.getTower().getTileX(), gt.getTower().getTileY())) if (!isWithinTileDistanceTo(wtarget.getTileX(), wtarget.getTileY(), wtarget.getFloorLevel(), 15)) { rand = Server.rand.nextInt(9); tilePosX = Zones.safeTileX(wtarget.getTileX() + 4 - rand); rand = Server.rand.nextInt(9); tilePosY = Zones.safeTileY(wtarget.getTileY() + 4 - rand); setTarget(-10L, true); sendToLoggers("No target. Heading to my camp at " + tilePosX + "," + tilePosY); abort = true; }    if (!abort && towerFound && !isWithinTileDistanceTo(gt.getTower().getTileX(), gt.getTower().getTileY(), gt.getTower().getFloorLevel(), 15)) { rand = Server.rand.nextInt(9); tilePosX = Zones.safeTileX(gt.getTower().getTileX() + 4 - rand); rand = Server.rand.nextInt(9); tilePosY = Zones.safeTileY(gt.getTower().getTileY() + 4 - rand); setTarget(-10L, true); sendToLoggers("No target. Heading to my tower at " + tilePosX + "," + tilePosY); abort = true; }  }  }   tpx = Zones.safeTileX(tpx); tpy = Zones.safeTileY(tpy); if (!abort) { VolaTile t = Zones.getOrCreateTile(tpx, tpy, isOnSurface()); VolaTile myt = getCurrentTile(); if (!t.isGuarded() || (myt != null && myt.isGuarded() && !t.hasFire())) { boolean swimming = false; int ctile = isOnSurface() ? Server.surfaceMesh.getTile(tx, ty) : Server.caveMesh.getTile(tx, ty); if (Tiles.decodeHeight(ctile) <= 0) swimming = true;  int tile = Zones.getTileIntForTile(tpx, tpy, getLayer()); if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (Tiles.decodeHeight(tile) > 0 || swimming)) if (isOnSurface()) { boolean stepOnBridge = false; if (Server.rand.nextInt(5) == 0) for (VolaTile stile : this.currentTile.getThisAndSurroundingTiles(1)) { if (stile.getStructure() != null && stile.getStructure().isTypeBridge()) { if (stile.getStructure().isHorizontal()) { if (stile.getStructure().getMaxX() == stile.getTileX() || stile.getStructure().getMinX() == stile.getTileX()) if (getTileY() == stile.getTileY()) { tilePosX = stile.getTileX(); tilePosY = stile.getTileY(); stepOnBridge = true; break; }   continue; }  if (stile.getStructure().getMaxY() == stile.getTileY() || stile.getStructure().getMinY() == stile.getTileY()) if (getTileX() == stile.getTileX()) { tilePosX = stile.getTileX(); tilePosY = stile.getTileY(); stepOnBridge = true; break; }   }  }   if (!stepOnBridge) if (!isTargetTileTooHigh(tpx, tpy, tile, swimming)) if (t == null || (t.getCreatures()).length < 3) { tilePosX = tpx; tilePosY = tpy; }    } else if (t == null || (t.getCreatures()).length < 3) { tilePosX = tpx; tilePosY = tpy; }   }  }  }  } else if (isSpiritGuard() && !hasTarget) { if (this.citizenVillage != null) { int[] tiles = { tilePosX, tilePosY }; if (!isOnSurface()) { if ((getCurrentTile()).isTransition) { setLayer(0, true); } else { tiles = findRandomCaveExit(tiles); tilePosX = tiles[0]; tilePosY = tiles[1]; if (tilePosX == tx || tilePosY != ty); }  } else { int x = this.citizenVillage.startx + Server.rand.nextInt(this.citizenVillage.endx - this.citizenVillage.startx); int y = this.citizenVillage.starty + Server.rand.nextInt(this.citizenVillage.endy - this.citizenVillage.starty); VolaTile t = Zones.getTileOrNull(x, y, isOnSurface()); if (t != null) { if (t.getStructure() == null) { tilePosX = x; tilePosY = y; }  } else { tilePosX = x; tilePosY = y; }  }  } else { VolaTile currTile = getCurrentTile(); if (currTile != null) { int rand = Server.rand.nextInt(5); int tpx = currTile.getTileX() + 2 - rand; rand = Server.rand.nextInt(5); int tpy = currTile.getTileY() + 2 - rand; VolaTile t = Zones.getTileOrNull(tilePosX, tilePosY, isOnSurface()); tpx = Zones.safeTileX(tpx); tpy = Zones.safeTileY(tpy); if (t == null) { tilePosX = tpx; tilePosY = tpy; }  } else if (!isDead()) { currTile = Zones.getOrCreateTile(tilePosX, tilePosY, isOnSurface()); logger.log(Level.WARNING, getName() + " stuck on no tile at " + getTileX() + "," + getTileY() + "," + isOnSurface()); }  }  } else if (isKingdomGuard() && !hasTarget) { int[] tiles = { tilePosX, tilePosY }; if (!isOnSurface()) { tiles = findRandomCaveExit(tiles); tilePosX = tiles[0]; tilePosY = tiles[1]; if (tilePosX != tx && tilePosY != ty) hasTarget = true;  }  if (!hasTarget && Server.rand.nextInt(40) == 0) { GuardTower gt = Kingdoms.getTower(this); if (gt != null) { int tpx = gt.getTower().getTileX(); int tpy = gt.getTower().getTileY(); tilePosX = tpx; tilePosY = tpy; hasTarget = true; }  }  if (!hasTarget) { VolaTile currTile = getCurrentTile(); int rand = Server.rand.nextInt(5); int tpx = Zones.safeTileX(currTile.getTileX() + 2 - rand); rand = Server.rand.nextInt(5); int tpy = Zones.safeTileY(currTile.getTileY() + 2 - rand); VolaTile t = Zones.getOrCreateTile(tpx, tpy, isOnSurface()); if (t.getKingdom() == getKingdomId() || currTile.getKingdom() != getKingdomId()) if (t.getStructure() == null) { tilePosX = tpx; tilePosY = tpy; }   }  }  if (!isCaveDweller() && !isOnSurface() && (getCurrentTile()).isTransition && tilePosX == tx && tilePosY == ty) if (!Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tx, ty))) || MineDoorPermission.getPermission(tx, ty).mayPass(this)) setLayer(0, true);   } else if (this.target != -10L) { Creature targ = getTarget(); if (targ != null) { if (targ.getCultist() != null && targ.getCultist().hasFearEffect()) setTarget(-10L, true);  VolaTile currTile = targ.getCurrentTile(); if (currTile != null) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); }  if (isSpellCaster() || isSummoner()) { tilePosX = Server.rand.nextBoolean() ? (currTile.tilex - (Server.rand.nextBoolean() ? 0 : 5)) : (currTile.tilex + (Server.rand.nextBoolean() ? 0 : 5)); tilePosY = Server.rand.nextBoolean() ? (currTile.tiley - (Server.rand.nextBoolean() ? 0 : 5)) : (currTile.tiley + (Server.rand.nextBoolean() ? 0 : 5)); }  int targGroup = targ.getGroupSize(); int myGroup = getGroupSize(); if (isOnSurface() != currTile.isOnSurface()) { boolean changeLayer = false; if ((getCurrentTile()).isTransition) changeLayer = true;  if (isSpiritGuard()) { if (this.currentVillage == this.citizenVillage) { if (this.citizenVillage != null) { if (currTile.getTileX() < this.citizenVillage.getStartX() - 5 || currTile.getTileX() > this.citizenVillage.getEndX() + 5 || currTile.getTileY() < this.citizenVillage.getStartY() - 5 || currTile.getTileY() > this.citizenVillage.getEndY() + 5) { if (this.citizenVillage.isOnSurface() == isOnSurface()) try { changeLayer = false; tilePosX = this.citizenVillage.getToken().getTileX(); tilePosY = this.citizenVillage.getToken().getTileY(); } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " no token for village " + this.citizenVillage); }   setTarget(-10L, true); } else { blinkTo(tilePosX, tilePosY, targ.getLayer(), targ.getFloorLevel()); return null; }  } else { setTarget(-10L, true); }  } else if (this.citizenVillage != null) { if (currTile.getTileX() < this.citizenVillage.getStartX() - 5 || currTile.getTileX() > this.citizenVillage.getEndX() + 5 || currTile.getTileY() < this.citizenVillage.getStartY() - 5 || currTile.getTileY() > this.citizenVillage.getEndY() + 5) { if (this.citizenVillage.isOnSurface() == isOnSurface()) { try { tilePosX = this.citizenVillage.getToken().getTileX(); tilePosY = this.citizenVillage.getToken().getTileY(); changeLayer = false; } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " no token for village " + this.citizenVillage); }  } else if (!changeLayer) { int[] tiles = { tilePosX, tilePosY }; if (isOnSurface()) { tiles = findRandomCaveEntrance(tiles); } else { tiles = findRandomCaveExit(tiles); }  tilePosX = tiles[0]; tilePosY = tiles[1]; }  setTarget(-10L, true); } else { blinkTo(tilePosX, tilePosY, targ.getLayer(), 0); return null; }  } else { setTarget(-10L, true); }  } else if (isUnique()) { Den den = Dens.getDen(this.template.getTemplateId()); if (den != null) { tilePosX = den.getTilex(); tilePosY = den.getTiley(); if (!changeLayer) { int[] tiles = { tilePosX, tilePosY }; if (!isOnSurface()) tiles = findRandomCaveExit(tiles);  tilePosX = tiles[0]; tilePosY = tiles[1]; }  setTarget(-10L, true); } else if (!isOnSurface()) { if (!changeLayer) { int[] tiles = { tilePosX, tilePosY }; tiles = findRandomCaveExit(tiles); tilePosX = tiles[0]; tilePosY = tiles[1]; }  }  } else if (isKingdomGuard()) { if (getCurrentKingdom() == getKingdomId()) { if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { if (!changeLayer) { int[] tiles = { tilePosX, tilePosY }; if (isOnSurface()) { tiles = findRandomCaveEntrance(tiles); } else { tiles = findRandomCaveExit(tiles); }  tilePosX = tiles[0]; tilePosY = tiles[1]; }  } else { setTarget(-10L, true); }  } else { changeLayer = false; setTarget(-10L, true); }  } else if (getSize() > 3) { changeLayer = false; setTarget(-10L, true); } else { VolaTile t = getCurrentTile(); if ((isAggHuman() || isHunter() || isDominated()) && (!currTile.isGuarded() || (t != null && t.isGuarded())) && isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { if (!changeLayer) { int[] tiles = { tilePosX, tilePosY }; if (isOnSurface()) { tiles = findRandomCaveEntrance(tiles); } else { tiles = findRandomCaveExit(tiles); }  tilePosX = tiles[0]; tilePosY = tiles[1]; }  } else { setTarget(-10L, true); }  }  if (changeLayer) if (!Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(tx, ty))) || MineDoorPermission.getPermission(tx, ty).mayPass(this)) setLayer(isOnSurface() ? -1 : 0, true);   } else if (isSpiritGuard()) { if (this.currentVillage == this.citizenVillage) { if (this.citizenVillage != null) { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (targ.getCultist() != null && targ.getCultist().hasFearEffect()) { tilePosX = this.citizenVillage.getStartX() - 5 + Server.rand.nextInt(this.citizenVillage.getDiameterX() + 10); tilePosY = this.citizenVillage.getStartY() - 5 + Server.rand.nextInt(this.citizenVillage.getDiameterY() + 10); } else if (currTile.getTileX() < this.citizenVillage.getStartX() - 5 || currTile.getTileX() > this.citizenVillage.getEndX() + 5 || currTile.getTileY() < this.citizenVillage.getStartY() - 5 || currTile.getTileY() > this.citizenVillage.getEndY() + 5) { try { tilePosX = this.citizenVillage.getToken().getTileX(); tilePosY = this.citizenVillage.getToken().getTileY(); } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " no token for village " + this.citizenVillage); }  setTarget(-10L, true); } else { this.citizenVillage.cryForHelp(this, false); }  } else if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { logger.log(Level.WARNING, "Why does this happen to a " + getName() + " at " + (getCurrentTile()).tilex + ", " + (getCurrentTile()).tiley); tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); } else { setTarget(-10L, true); }  } else if (this.citizenVillage != null) { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (currTile.getTileX() < this.citizenVillage.getStartX() - 5 || currTile.getTileX() > this.citizenVillage.getEndX() + 5 || currTile.getTileY() < this.citizenVillage.getStartY() - 5 || currTile.getTileY() > this.citizenVillage.getEndY() + 5) { try { tilePosX = this.citizenVillage.getToken().getTileX(); tilePosY = this.citizenVillage.getToken().getTileY(); } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " no token for village " + this.citizenVillage); }  setTarget(-10L, true); } else { this.citizenVillage.cryForHelp(this, true); }  } else { if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { if (Server.rand.nextInt(100) != 0) { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); } else { setTarget(-10L, true); }  } else { setTarget(-10L, true); }  logger.log(Level.WARNING, getName() + " no citizen village."); }  } else if (isUnique()) { Den den = Dens.getDen(this.template.getTemplateId()); if (den != null) { if (Math.abs(currTile.getTileX() - den.getTilex()) > this.template.getVision() || Math.abs(currTile.getTileY() - den.getTiley()) > this.template.getVision()) if (Server.rand.nextInt(10) == 0) { if (!isFighting()) { setTarget(-10L, true); tilePosX = den.getTilex(); tilePosY = den.getTiley(); }  } else if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (getSize() < 5 && targ.getBridgeId() != -10L && getBridgeId() < 0L) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), targ.getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  } else if (getBridgeId() != targ.getBridgeId()) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  }  if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); }  } else if (!isFighting()) { setTarget(-10L, true); }   } else if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (getSize() < 5 && targ.getBridgeId() != -10L && getBridgeId() < 0L) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), targ.getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  } else if (getBridgeId() != targ.getBridgeId()) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  }  }  } else if (!isFighting()) { setTarget(-10L, true); }  } else if (isKingdomGuard()) { if (getCurrentKingdom() == getKingdomId()) { if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { GuardTower gt = Kingdoms.getTower(this); if (gt != null) { int tpx = gt.getTower().getTileX(); int tpy = gt.getTower().getTileY(); if (targGroup < myGroup * getMaxGroupAttackSize() && targ.isWithinTileDistanceTo(tpx, tpy, (int)gt.getTower().getPosZ(), 50)) { if (targ.getCultist() != null && targ.getCultist().hasFearEffect()) { if (Server.rand.nextBoolean()) { tilePosX = Math.max(currTile.getTileX() + 10, getTileX()); } else { tilePosX = Math.min(currTile.getTileX() - 10, getTileX()); }  if (Server.rand.nextBoolean()) { tilePosX = Math.max(currTile.getTileY() + 10, getTileY()); } else { tilePosX = Math.min(currTile.getTileY() - 10, getTileY()); }  } else if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (targ.getBridgeId() != -10L && getBridgeId() < 0L) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), targ.getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  } else if (getBridgeId() != targ.getBridgeId()) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  }  }  } else { tilePosX = tpx; tilePosY = tpy; setTarget(-10L, true); }  } else if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); }  } else { setTarget(-10L, true); }  } else { setTarget(-10L, true); }  } else if (targ.getCultist() != null && targ.getCultist().hasFearEffect()) { if (Server.rand.nextBoolean()) { tilePosX = Math.max(currTile.getTileX() + 10, getTileX()); } else { tilePosX = Math.min(currTile.getTileX() - 10, getTileX()); }  if (Server.rand.nextBoolean()) { tilePosX = Math.max(currTile.getTileY() + 10, getTileY()); } else { tilePosX = Math.min(currTile.getTileY() - 10, getTileY()); }  } else { boolean abort = false; boolean towerFound = false; if (isWarGuard()) { GuardTower gt = Kingdoms.getClosestTower(getTileX(), getTileY(), true); if (gt != null && gt.getKingdom() == getKingdomId()) towerFound = true;  Item wtarget = Kingdoms.getClosestWarTarget(tx, ty, this); if (wtarget != null) if (!towerFound || getTileRange(this, wtarget.getTileX(), wtarget.getTileY()) < getTileRange(this, gt.getTower().getTileX(), gt.getTower().getTileY())) if (!isWithinTileDistanceTo(wtarget.getTileX(), wtarget.getTileY(), wtarget.getFloorLevel(), 15)) { int rand = Server.rand.nextInt(9); tilePosX = Zones.safeTileX(wtarget.getTileX() + 4 - rand); rand = Server.rand.nextInt(9); tilePosY = Zones.safeTileY(wtarget.getTileY() + 4 - rand); abort = true; setTarget(-10L, true); sendToLoggers("Heading to my camp at " + tilePosX + "," + tilePosY); }    if (!abort && towerFound && !isWithinTileDistanceTo(gt.getTower().getTileX(), gt.getTower().getTileY(), gt.getTower().getFloorLevel(), 15)) { int rand = Server.rand.nextInt(9); tilePosX = Zones.safeTileX(gt.getTower().getTileX() + 4 - rand); rand = Server.rand.nextInt(9); tilePosY = Zones.safeTileY(gt.getTower().getTileY() + 4 - rand); abort = true; setTarget(-10L, true); sendToLoggers("Heading to my tower at " + tilePosX + "," + tilePosY); }  }  if (!abort) { VolaTile t = getCurrentTile(); if (targGroup <= myGroup * getMaxGroupAttackSize() && (isAggHuman() || isHunter()) && (!currTile.isGuarded() || (t != null && t.isGuarded()))) { if (isWithinTileDistanceTo(currTile.getTileX(), currTile.getTileY(), (int)targ.getPositionZ(), this.template.getMaxHuntDistance())) { if (targ.getKingdomId() != 0 && !isFriendlyKingdom(targ.getKingdomId()) && (isDefendKingdom() || (isAggWhitie() && targ.getKingdomTemplateId() != 3))) { if (!isFighting()) if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); setTarget(targ.getWurmId(), false); }   } else if (isSubmerged()) { try { float z = Zones.calculateHeight(targ.getPosX(), targ.getPosY(), targ.isOnSurface()); if (z < -5.0F) { if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); }  } else { int[] tiles = { tilePosX, tilePosY }; if (isOnSurface()) tiles = findRandomDeepSpot(tiles);  tilePosX = tiles[0]; tilePosY = tiles[1]; setTarget(-10L, true); }  } catch (NoSuchZoneException nsz) { setTarget(-10L, true); }  } else if (seed == 100) { tilePosX = currTile.tilex - 1 + Server.rand.nextInt(3); tilePosY = currTile.tiley - 1 + Server.rand.nextInt(3); } else { tilePosX = currTile.getTileX(); tilePosY = currTile.getTileY(); if (getSize() < 5 && targ.getBridgeId() != -10L && getBridgeId() < 0L) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), targ.getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  } else if (getBridgeId() != targ.getBridgeId()) { int[] tiles = findBestBridgeEntrance(targ.getTileX(), targ.getTileY(), targ.getLayer(), getBridgeId()); if (tiles[0] > 0) { tilePosX = tiles[0]; tilePosY = tiles[1]; if (getTileX() == tilePosX && getTileY() == tilePosY) { tilePosX = currTile.tilex; tilePosY = currTile.tiley; }  }  }  }  } else if (!isFighting()) { setTarget(-10L, true); }  } else if (!isFighting()) { setTarget(-10L, true); }  }  }  } else { setTarget(-10L, true); }  } else { setTarget(-10L, true); }  }  if (tilePosX == tx && tilePosY == ty) return null;  tilePosX = Zones.safeTileX(tilePosX); tilePosY = Zones.safeTileY(tilePosY); if (!isOnSurface()) { int tile = Server.caveMesh.getTile(tilePosX, tilePosY); if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged())) return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), getFloorLevel());  } else { int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY); if (Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged()) return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), getFloorLevel());  }  setTarget(-10L, true); if (isDominated() && hasOrders()) removeOrder(getFirstOrder());  return null; } catch (ArrayIndexOutOfBoundsException iao) { logger.log(Level.WARNING, getName() + " " + tilePosX + ", " + tilePosY + iao.getMessage(), iao); return null; } finally {} } public final boolean isBridgeBlockingAttack(Creature attacker, boolean justChecking) { if (isInvulnerable() || attacker.isInvulnerable()) return true;  if (getPositionZ() + getAltOffZ() < 0.0F && attacker.getBridgeId() > 0L) return true;  if (attacker.getPositionZ() + getAltOffZ() < 0.0F && getBridgeId() > 0L) return true;  if (!justChecking && getFloorLevel() != attacker.getFloorLevel() && getBridgeId() != attacker.getBridgeId() && getSize() < 5 && attacker.getSize() < 5) return true;  return false; } public final PathTile getPersonalTargetTile() { float lPosX = this.status.getPositionX(); float lPosY = this.status.getPositionY(); int tilePosX = (int)lPosX >> 2; int tilePosY = (int)lPosY >> 2; int tx = tilePosX; int ty = tilePosY; VolaTile currTile = getCurrentTile(); if (currTile != null) { int[] foodSpot = forageForFood(currTile); if (foodSpot[0] != -1) { tilePosX = foodSpot[0]; tilePosY = foodSpot[1]; } else if (this.template.isTowerBasher() && Servers.localServer.PVPSERVER) { GuardTower closestTower = Kingdoms.getClosestEnemyTower(getTileX(), getTileY(), true, this); if (closestTower != null) { tilePosX = closestTower.getTower().getTileX(); tilePosY = closestTower.getTower().getTileY(); }  }  }  if (tilePosX == tx && tilePosY == ty) return null;  tilePosX = Zones.safeTileX(tilePosX); tilePosY = Zones.safeTileY(tilePosY); if (!isOnSurface()) { int tile = Server.caveMesh.getTile(tilePosX, tilePosY); if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged())) return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), -1);  } else { int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY); if (Tiles.decodeHeight(tile) > -getHalfHeightDecimeters() || isSwimming() || isSubmerged()) return new PathTile(tilePosX, tilePosY, tile, isOnSurface(), 0);  }  return null; } public final int getHalfHeightDecimeters() { return getCentimetersHigh() / 20; } public int[] findRandomCaveExit(int[] tiles) { int startx = Math.max(0, this.currentTile.tilex - 20); int endx = Math.min(Zones.worldTileSizeX - 1, this.currentTile.tilex + 20); int starty = Math.max(0, this.currentTile.tiley - 20); int endy = Math.min(Zones.worldTileSizeY - 1, this.currentTile.tiley + 20); if (this.citizenVillage != null) if (Server.rand.nextInt(2) == 0) { startx = Math.max(0, this.citizenVillage.getStartX() - 5); endx = Math.min(Zones.worldTileSizeX - 1, this.citizenVillage.getEndX() + 5); starty = Math.max(0, this.citizenVillage.getStartY() - 5); endy = Math.min(Zones.worldTileSizeY - 1, this.citizenVillage.getEndY() + 5); int i = this.citizenVillage.startx + Server.rand.nextInt(this.citizenVillage.endx - this.citizenVillage.startx); int y = this.citizenVillage.starty + Server.rand.nextInt(this.citizenVillage.endy - this.citizenVillage.starty); if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(i, y)))) { tiles[0] = i; tiles[1] = y; setPathfindcounter(0); }  }   int rand = Server.rand.nextInt(endx - startx); startx += rand; rand = Server.rand.nextInt(endy - starty); starty += rand; for (int x = startx; x < endx; x++) { for (int y = starty; y < endy; y++) { if (Tiles.decodeType(Server.caveMesh.getTile(x, y)) == Tiles.Tile.TILE_CAVE_EXIT.id) { tiles[0] = x; tiles[1] = y; setPathfindcounter(0); return tiles; }  }  }  return tiles; } public int[] findRandomDeepSpot(int[] tiles) { int startx = Zones.safeTileX(this.currentTile.tilex - 50); int endx = Zones.safeTileX(this.currentTile.tilex + 50); int starty = Zones.safeTileY(this.currentTile.tiley - 50); int endy = Zones.safeTileY(this.currentTile.tiley + 50); int rand = Server.rand.nextInt(endx - startx); startx += rand; rand = Server.rand.nextInt(endy - starty); starty += rand; for (int x = startx; x < Math.min(endx, startx + 10); x++) { for (int y = starty; y < Math.min(endy, starty + 10); y++) { if (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) < -50.0F) { tiles[0] = x; tiles[1] = y; return tiles; }  }  }  return tiles; } public final boolean isSpyTarget(Creature c) { if (c.isDead() || c.getPower() > 0) return false;  if (getTemplate().getTemplateId() == 84 && c.getKingdomId() != 3) return true;  if (getTemplate().getTemplateId() == 12 && c.getKingdomId() != 1) return true;  if (getTemplate().getTemplateId() == 10 && c.getKingdomId() != 2) return true;  return false; } public final boolean isSpyFriend(Creature c) { if (c.isAggHuman() || c.getCitizenVillage() == null) return false;  if (getTemplate().getTemplateId() == 84 && c.getKingdomId() == 3) return true;  if (getTemplate().getTemplateId() == 12 && c.getKingdomId() == 1) return true;  if (getTemplate().getTemplateId() == 10 && c.getKingdomId() == 2) return true;  return false; } public final boolean isWithinSpyDist(Creature c) { return (c != null && c.isWithinTileDistanceTo(getTileX(), getTileY(), 100, 40)); } public int[] getSpySpot(int[] suggested) { if (isSpy()) { Player player; Creature linkedToc = getCreatureLinkedTo(); if (linkedToc == null || !linkedToc.isDead() || !isWithinSpyDist(linkedToc)) { this.linkedTo = -10L; for (Player player1 : Players.getInstance().getPlayers()) { if (isSpyTarget((Creature)player1) && !player1.isDead()) if (isWithinSpyDist((Creature)player1)) { player = player1; setLinkedTo(player1.getWurmId(), false); break; }   }  }  if (player != null) { int targX = player.getTileX() + 15 + Server.rand.nextInt(6); if (getTileX() < player.getTileX()) targX = player.getTileX() - 15 - Server.rand.nextInt(6);  int targY = player.getTileY() + 15 + Server.rand.nextInt(6); if (getTileY() < player.getTileY()) targX = player.getTileY() - 15 - Server.rand.nextInt(6);  targX = Zones.safeTileX(targX); targY = Zones.safeTileX(targY); return new int[] { targX, targY }; }  }  return suggested; } public int[] findRandomCaveEntrance(int[] tiles) { int startx = Math.max(0, this.currentTile.tilex - 20); int endx = Math.min(Zones.worldTileSizeX - 1, this.currentTile.tilex + 20); int starty = Math.max(0, this.currentTile.tiley - 20); int endy = Math.min(Zones.worldTileSizeY - 1, this.currentTile.tiley + 20); if (this.citizenVillage != null) { startx = Math.max(0, this.citizenVillage.getStartX() - 5); endx = Math.min(Zones.worldTileSizeX - 1, this.citizenVillage.getEndX() + 5); starty = Math.max(0, this.citizenVillage.getStartY() - 5); endy = Math.min(Zones.worldTileSizeY - 1, this.citizenVillage.getEndY() + 5); }  int rand = Server.rand.nextInt(endx - startx); startx += rand; rand = Server.rand.nextInt(endy - starty); starty += rand; boolean passMineDoors = (isKingdomGuard() || isGhost() || isUnique()); for (int x = startx; x < Math.min(endx, startx + 10); x++) { for (int y = starty; y < Math.min(endy, starty + 10); y++) { if (Tiles.decodeType(Server.surfaceMesh.getTile(x, y)) == Tiles.Tile.TILE_HOLE.id || (passMineDoors && Tiles.isMineDoor(Tiles.decodeType(Server.surfaceMesh.getTile(x, y))))) { tiles[0] = x; tiles[1] = y; return tiles; }  }  }  return tiles; } public int[] findBestBridgeEntrance(int tilex, int tiley, int layer, long bridgeId) { VolaTile t = Zones.getTileOrNull(tilex, tiley, (layer >= 0)); if (t != null) if (t.getStructure() != null) if (t.getStructure().getWurmId() == bridgeId) return t.getStructure().findBestBridgeEntrance(this, tilex, tiley, layer, bridgeId, this.pathfindcounter);    return Structure.noEntrance; } public void setAbilityTitle(int newTitle) {} public int getAbilityTitleVal() { return this.template.abilityTitle; } public String getAbilityTitle() { if (this.template.abilityTitle > -1) return Abilities.getAbilityString(this.template.abilityTitle) + " ";  return ""; } public boolean isLogged() { return false; } public float getFaith() { return this.template.getFaith(); } public Skill getChannelingSkill() { Skill channeling = null; try { channeling = this.skills.getSkill(10067); } catch (NoSuchSkillException nss) { if (getFaith() >= 10.0F) channeling = this.skills.learn(10067, 1.0F);  }  return channeling; } public Skill getMindLogical() { Skill toReturn = null; try { toReturn = getSkills().getSkill(100); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(100, 1.0F); }  return toReturn; } public Skill getMindSpeed() { Skill toReturn = null; try { toReturn = getSkills().getSkill(101); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(101, 1.0F); }  return toReturn; } public Skill getSoulDepth() { Skill toReturn = null; try { toReturn = getSkills().getSkill(106); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(106, 1.0F); }  return toReturn; } public Skill getBreedingSkill() { Skill toReturn; try { toReturn = getSkills().getSkill(10085); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(10085, 1.0F); }  return toReturn; } public Skill getSoulStrength() { Skill toReturn = null; try { toReturn = getSkills().getSkill(105); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(105, 1.0F); }  return toReturn; } public Skill getBodyStrength() { Skill toReturn = null; try { toReturn = getSkills().getSkill(102); } catch (NoSuchSkillException nss) { toReturn = getSkills().learn(102, 1.0F); }  return toReturn; } public Deity getDeity() { return this.template.getDeity(); } public void modifyFaith(float modifier) {} public boolean isActionFaithful(Action action) { if (getDeity() != null) if (this.faithful) return getDeity().isActionFaithful(action);   return true; } public void performActionOkey(Action action) { if (getDeity() != null) if (!getDeity().performActionOkey(this, action)) getCommunicator().sendNormalServerMessage((getDeity()).name + " noticed you!");   } public void setFaith(float faith) throws IOException {} public void setDeity(@Nullable Deity deity) throws IOException {} public boolean checkLoyaltyProgram() { return false; } public boolean maybeModifyAlignment(float modification) { return false; } public void setAlignment(float align) throws IOException {} public void setPriest(boolean priest) {} public boolean isPriest() { return (isSpellCaster() || isSummoner()); } public float getAlignment() { return this.template.getAlignment(); } public float getFavor() { if (isSpellCaster() || isSummoner()) return this.creatureFavor;  return this.template.getFaith(); } public float getFavorLinked() { return this.template.getFaith(); } public void setFavor(float favor) throws IOException { if (isSpellCaster() || isSummoner()) this.creatureFavor = favor;  } public void depleteFavor(float favorToRemove, boolean combatSpell) throws IOException { if (isSpellCaster() || isSummoner()) setFavor(getFavor() - favorToRemove);  } public boolean mayChangeDeity(int targetDeity) { return true; } public void setChangedDeity() throws IOException {} public boolean isNewbie() { return false; } public boolean maySteal() { return true; } public boolean isAtWarWith(Creature creature) { if (this.citizenVillage != null && creature.citizenVillage != null) return this.citizenVillage.isEnemy(creature.citizenVillage);  return false; } public boolean isChampion() { return false; } public void setRealDeath(byte realdeathcounter) throws IOException {} public boolean modifyChampionPoints(int championPointsModifier) { return false; } public int getFatigueLeft() { return 20000; } public void decreaseFatigue() {} public boolean checkPrayerFaith() { return false; } public boolean isAlive() { return !this.status.dead; } public boolean isDead() { return this.status.dead; } public byte getKingdomId() { if (!Servers.isThisAPvpServer()) { Village bVill = getBrandVillage(); if (bVill != null) return bVill.kingdom;  }  return this.status.kingdom; } public byte getKingdomTemplateId() { Kingdom k = Kingdoms.getKingdom(getKingdomId()); if (k != null) return k.getTemplate();  return 0; } public int getReputation() { return this.template.getReputation(); } public void setReputation(int reputation) {} public void playAnthem() { if (this.musicPlayer != null) { if (getKingdomTemplateId() == 3) this.musicPlayer.checkMUSIC_ANTHEMHOTS_SND();  if (getKingdomId() == 1) this.musicPlayer.checkMUSIC_ANTHEMJENN_SND();  if (getKingdomId() == 2) this.musicPlayer.checkMUSIC_ANTHEMMOLREHAN_SND();  }  } public boolean isTransferring() { return false; } public boolean isOnCurrentServer() { return true; } public boolean setKingdomId(byte kingdom) throws IOException { return setKingdomId(kingdom, false, true); } public boolean setKingdomId(byte kingdom, boolean forced) throws IOException { return setKingdomId(kingdom, forced, true); } public boolean setKingdomId(byte kingdom, boolean forced, boolean setTimeStamp) throws IOException { return setKingdomId(kingdom, forced, setTimeStamp, true); } public boolean setKingdomId(byte kingdom, boolean forced, boolean setTimeStamp, boolean online) throws IOException { boolean sendUpdate = false; if (getKingdomId() != kingdom) { if (isKing()) { getCommunicator().sendNormalServerMessage("You are the king, and may not change kingdom!"); return false; }  Village v = getCitizenVillage(); if (!forced && v != null) if (v.getMayor().getId() == getWurmId()) try { getCommunicator().sendNormalServerMessage("You are the mayor of " + v.getName() + ", and may not change kingdom!"); return false; } catch (Exception ex) { return false; }    if (Kingdoms.getKingdomTemplateFor(getKingdomId()) == 3 && Kingdoms.getKingdomTemplateFor(kingdom) != 3) { if (getDeity() != null && (getDeity()).number == 4) { setDeity(null); setFaith(0.0F); setAlignment(Math.max(1.0F, getAlignment())); }  } else if (Kingdoms.getKingdomTemplateFor(kingdom) == 3 && Kingdoms.getKingdomTemplateFor(getKingdomId()) != 3) { if (getDeity() == null || (getDeity()).number == 1 || (getDeity()).number == 2 || (getDeity()).number == 3) { setDeity(Deities.getDeity(4)); setAlignment(Math.min(getAlignment(), -50.0F)); setFaith(1.0F); }  }  if (getKingdomId() != 0 && !forced) { if (this.citizenVillage != null) this.citizenVillage.removeCitizen(this);  if (kingdom != 0 && Servers.localServer.PVPSERVER) increaseChangedKingdom(setTimeStamp);  sendUpdate = true; }  clearRoyalty(); setTeam(null, true); if (isPlayer() && getCommunicator() != null && hasLink()) if (Servers.localServer.PVPSERVER && !Servers.localServer.testServer) try { KingdomIp kip = KingdomIp.getKIP(getCommunicator().getConnection().getIp(), getKingdomId()); if (kip != null) kip.logon(kingdom);  } catch (Exception iox) { logger.log(Level.INFO, getName() + " " + iox.getMessage()); }    this.status.setKingdom(kingdom); if (isPlayer()) { if (Servers.localServer.isChallengeOrEpicServer() || Servers.isThisAChaosServer() || Servers.localServer.PVPSERVER) { if (getCommunicator().getConnection() != null) try { if (getCommunicator().getConnection().getIp() != null) { KingdomIp kip = KingdomIp.getKIP(getCommunicator().getConnection().getIp()); if (kip != null) kip.setKingdom(kingdom);  }  } catch (NullPointerException nullPointerException) {}  if ((Server.getInstance().isPS() && Servers.localServer.PVPSERVER) || Servers.isThisAChaosServer()) ((Player)this).getSaveFile().setChaosKingdom(kingdom);  }  Players.getInstance().registerNewKingdom(this); setVotedKing(false); }  playAnthem(); Creatures.getInstance().setCreatureDead(this); setTarget(-10L, true); if (sendUpdate && online) refreshVisible();  if (this.citizenVillage != null) if (!forced) { this.citizenVillage.removeCitizen(this); } else if ((this.citizenVillage.getMayor()).wurmId == getWurmId()) { this.citizenVillage.convertToKingdom(kingdom, true, setTimeStamp); }   }  return true; } public void setVotedKing(boolean voted) {} public boolean hasVotedKing() { return true; } public void clearRoyalty() {} public void checkForEnemies() { checkForEnemies(false); } public void checkForEnemies(boolean overrideRandomChance) { if (isWarGuard() || isKingdomGuard()) if (this.guardTower != null && this.guardTower.getKingdom() == getKingdomId() && System.currentTimeMillis() - this.guardTower.getLastSentWarning() < 180000L) overrideRandomChance = true;   if (overrideRandomChance || Server.rand.nextInt((isKingdomGuard() || isWarGuard()) ? 20 : 100) == 0) if (getVisionArea() != null) try { if (isOnSurface()) { getVisionArea().getSurface().checkForEnemies(); } else { getVisionArea().getUnderGround().checkForEnemies(); }  } catch (Exception ep) { logger.log(Level.WARNING, ep.getMessage(), ep); }    } public boolean sendTransfer(Server senderServer, String targetIp, int targetPort, String serverpass, int targetServerId, int tilex, int tiley, boolean surfaced, boolean toOrFromEpic, byte targetKingdomId) { logger.log(Level.WARNING, "Sendtransfer called in creature", new Exception()); return false; } public void increaseChangedKingdom(boolean setTimeStamp) throws IOException {} public boolean mayChangeKingdom(Creature converter) { return false; } public boolean isOfCustomKingdom() { Kingdom k = Kingdoms.getKingdom(getKingdomId()); if (k != null && k.isCustomKingdom()) return true;  return false; } public void punishSkills(double aMod, boolean pvp) { if (getCultist() != null && getCultist().isNoDecay()) return;  try { Skill bodyStr = this.skills.getSkill(102); bodyStr.setKnowledge(bodyStr.getKnowledge() - 0.009999999776482582D, false); Skill body = this.skills.getSkill(1); body.setKnowledge(body.getKnowledge() - 0.009999999776482582D, false); } catch (NoSuchSkillException nss) { this.skills.learn(102, 1.0F); logger.log(Level.WARNING, getName() + " learnt body strength."); }  if (!pvp) { Skill[] sk = this.skills.getSkills(); int nums = 0; for (Skill lElement : sk) { if (lElement.getType() == 4 || lElement.getType() == 2) if (lElement.getNumber() != 1023 && Server.rand.nextInt(10) == 0) if (lElement.getKnowledge(0.0D) > 2.0D && lElement.getKnowledge(0.0D) < 99.0D) { lElement.setKnowledge(Math.max(1.0D, lElement.getKnowledge() - aMod), false); nums++; if (nums > 4) break;  }    }  }  } public long getMoney() { return 0L; } public boolean addMoney(long moneyToAdd) throws IOException { return false; } public boolean chargeMoney(long moneyToCharge) throws IOException { return false; } public boolean hasCustomColor() { if (getPower() > 0) return true;  if (hasCustomKingdom()) return true;  if (this.status.hasCustomColor()) return true;  return (this.template.getColorRed() != 255 || this.template.getColorGreen() != 255 || this.template.getColorBlue() != 255); } public boolean hasCustomKingdom() { return (getKingdomId() > 4 || getKingdomId() < 0); } public byte getColorRed() { if (this.status.hasCustomColor()) return this.status.getColorRed();  return (byte)this.template.getColorRed(); } public byte getColorGreen() { if (this.status.hasCustomColor()) return this.status.getColorGreen();  return (byte)this.template.getColorGreen(); } public byte getColorBlue() { if (this.status.hasCustomColor()) return this.status.getColorBlue();  return (byte)this.template.getColorBlue(); } public boolean hasCustomSize() { if (this.status.getSizeMod() != 1.0F) return true;  return (this.template.getSizeModX() != 64 || this.template.getSizeModY() != 64 || this.template.getSizeModZ() != 64); } public byte getSizeModX() { return (byte)(int)Math.min(255.0F, this.template.getSizeModX() * this.status.getSizeMod()); } public byte getSizeModY() { return (byte)(int)Math.min(255.0F, this.template.getSizeModY() * this.status.getSizeMod()); } public byte getSizeModZ() { return (byte)(int)Math.min(255.0F, this.template.getSizeModZ() * this.status.getSizeMod()); } public void setMoney(long newMoney) throws IOException {} public void setClimbing(boolean climbing) throws IOException {} public boolean isClimbing() { return true; } public boolean acceptsInvitations() { return false; } public Cultist getCultist() { return null; } public static short[] getTileSteepness(int tilex, int tiley, boolean surfaced) { short highest = -100; short lowest = 32000; for (int x = 0; x <= 1; x++) { for (int y = 0; y <= 1; y++) { if (tilex + x < Zones.worldTileSizeX && tiley + y < Zones.worldTileSizeY) { short height = 0; if (surfaced) { height = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y)); } else { height = Tiles.decodeHeight(Server.caveMesh.getTile(tilex + x, tiley + y)); }  if (height > highest) highest = height;  if (height < lowest) lowest = height;  }  }  }  int med = (highest + lowest) / 2; return new short[] { (short)med, (short)(highest - lowest) }; } public short[] getLowestTileCorner(short tilex, short tiley) { short lowestX = tilex; short lowestY = tiley; short lowest = 32000; for (int x = 0; x <= 1; x++) { for (int y = 0; y <= 1; y++) { if (tilex + x < Zones.worldTileSizeX && tiley + y < Zones.worldTileSizeY) { short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex + x, tiley + y)); if (height < lowest) { lowest = height; lowestX = (short)(tilex + x); lowestY = (short)(tiley + y); }  }  }  }  return new short[] { lowestX, lowestY }; } public void setSecondTitle(Titles.Title title) {} public void setTitle(Titles.Title title) {} public Titles.Title getSecondTitle() { return null; } public Titles.Title getTitle() { return null; } public String getTitleString() { String suff = ""; if (getTitle() != null) if (getTitle().isRoyalTitle()) { if (getAppointments() != 0L || isAppointed()) suff = suff + getKingdomTitle();  } else { suff = suff + getTitle().getName(isNotFemale()); }   if (Features.Feature.COMPOUND_TITLES.isEnabled() && getSecondTitle() != null) { if (getTitle() != null) suff = suff + " ";  if (getSecondTitle().isRoyalTitle()) { if (getAppointments() != 0L || isAppointed()) suff = suff + getKingdomTitle();  } else { suff = suff + getSecondTitle().getName(isNotFemale()); }  }  return suff; } public String getKingdomTitle() { return ""; } public void setFinestAppointment() {} public float getSpellDamageProtectBonus() { return getBonusForSpellEffect((byte)19); } public float getDetectDangerBonus() { if (getKingdomTemplateId() == 3) return 50.0F + ItemBonus.getDetectionBonus(this);  SpellEffects eff = getSpellEffects(); if (eff != null) { SpellEffect effbon = eff.getSpellEffect((byte)21); if (effbon != null) return effbon.power + ItemBonus.getDetectionBonus(this);  }  return ItemBonus.getDetectionBonus(this); } public float getBonusForSpellEffect(byte enchantment) { SpellEffects eff = getSpellEffects(); if (eff != null) { SpellEffect skillgain = eff.getSpellEffect(enchantment); if (skillgain != null) return skillgain.power;  }  return 0.0F; } public float getNoLocateItemBonus(boolean reducePower) { Item[] bodyItems = getBody().getContainersAndWornItems(); float maxBonus = 0.0F; Item maxItem = null; for (int x = 0; x < bodyItems.length; x++) { if (bodyItems[x].isEnchantableJewelry() || bodyItems[x].isArtifact()) if (bodyItems[x].getNolocateBonus() > maxBonus) { maxBonus = bodyItems[x].getNolocateBonus(); maxItem = bodyItems[x]; }   }  if (maxItem != null) { maxBonus = (maxBonus + maxItem.getCurrentQualityLevel()) / 2.0F; ItemSpellEffects effs = maxItem.getSpellEffects(); if (effs == null) effs = new ItemSpellEffects(maxItem.getWurmId());  SpellEffect eff = effs.getSpellEffect((byte)29); if (eff != null && reducePower) eff.setPower(eff.power - 0.2F);  }  return maxBonus; } public int getNumberOfShopItems() { Set<Item> ite = getInventory().getItems(); int nums = 0; for (Item i : ite) { if (!i.isCoin()) nums++;  }  return nums; } public final void addNewbieBuffs() { if (getPlayingTime() < 86400000L) { SpellEffects effs = createSpellEffects(); SpellEffect eff = effs.getSpellEffect((byte)74); if (eff == null) { getCommunicator().sendSafeServerMessage("You require less food and drink as a new player."); eff = new SpellEffect(getWurmId(), (byte)74, 100.0F, (int)((86400000L - getPlayingTime()) / 1000L), (byte)1, (byte)0, true); effs.addSpellEffect(eff); }  SpellEffect range = effs.getSpellEffect((byte)73); if (range == null) { getCommunicator().sendSafeServerMessage("Creatures and monsters are less aggressive to new players."); range = new SpellEffect(getWurmId(), (byte)73, 100.0F, (int)((86400000L - getPlayingTime()) / 1000L), (byte)1, (byte)0, true); effs.addSpellEffect(range); }  SpellEffect health = effs.getSpellEffect((byte)75); if (health == null) { getCommunicator().sendSafeServerMessage("You regenerate health faster as a new player."); health = new SpellEffect(getWurmId(), (byte)75, 100.0F, (int)((86400000L - getPlayingTime()) / 1000L), (byte)1, (byte)0, true); effs.addSpellEffect(health); }  }  } public SpellEffects getSpellEffects() { return (getStatus()).spellEffects; } public void sendUpdateSpellEffect(SpellEffect effect) { SpellEffectsEnum spellEffect = SpellEffectsEnum.getEnumByName(effect.getName()); if (spellEffect != SpellEffectsEnum.NONE) { getCommunicator().sendAddSpellEffect(effect.id, spellEffect, effect.timeleft, effect.power); } else { getCommunicator().sendAddSpellEffect(effect.id, effect.getName(), effect.type, effect.getSpellEffectType(), effect.getSpellInfluenceType(), effect.timeleft, effect.power); }  } public void sendAddSpellEffect(SpellEffect effect) { SpellEffectsEnum spellEffect = SpellEffectsEnum.getEnumByName(effect.getName()); if (spellEffect != SpellEffectsEnum.NONE) { getCommunicator().sendAddSpellEffect(effect.id, spellEffect, effect.timeleft, effect.power); } else { getCommunicator().sendAddSpellEffect(effect.id, effect.getName(), effect.type, effect.getSpellEffectType(), effect.getSpellInfluenceType(), effect.timeleft, effect.power); }  if (effect.type == 23) { getCombatHandler().addDodgeModifier(willowMod); } else if (effect.type == 39) { getMovementScheme().setChargeMoveMod(true); }  } public void removeSpellEffect(SpellEffect effect) { SpellEffectsEnum spellEffect = SpellEffectsEnum.getEnumByName(effect.getName()); if (spellEffect != SpellEffectsEnum.NONE) { getCommunicator().sendRemoveSpellEffect(effect.id, spellEffect); } else { getCommunicator().sendRemoveSpellEffect(effect.id, null); }  getCommunicator().sendNormalServerMessage("You are no longer affected by " + effect.getName() + "."); if (effect.type == 23) { getCombatHandler().removeDodgeModifier(willowMod); } else if (effect.type == 39) { getMovementScheme().setChargeMoveMod(false); } else if (effect.type == 64) { setVisible(false); refreshAttitudes(); setVisible(true); } else if (effect.type == 72) { setModelName("Human"); }  } public final void removeIllusion() { if (getSpellEffects() != null) { SpellEffect ill = getSpellEffects().getSpellEffect((byte)72); if (ill != null) getSpellEffects().removeSpellEffect(ill);  }  } public void sendRemovePhantasms() {} public SpellEffects createSpellEffects() { if ((getStatus()).spellEffects == null) (getStatus()).spellEffects = new SpellEffects(getWurmId());  return (getStatus()).spellEffects; }
/*       */   @Deprecated public boolean dispelSpellEffect(double power) { boolean toret = false; if (getMovementScheme().setWebArmourMod(false, 0.0F)) { getMovementScheme().setWebArmourMod(false, 0.0F); toret = true; }  if (getSpellEffects() != null) { SpellEffect[] speffs = getSpellEffects().getEffects(); for (int x = 0; x < speffs.length; x++) { if ((speffs[x]).type != 64 && (speffs[x]).type != 74 && (speffs[x]).type != 73 && (speffs[x]).type != 75) if (Server.rand.nextInt(Math.max(1, (int)(speffs[x]).power)) < power) { getSpellEffects().removeSpellEffect(speffs[x]); if ((speffs[x]).type == 22 && getCurrentTile() != null) getCurrentTile().setNewRarityShader(this);  return true; }   }  }  return toret; }
/*       */   public byte getFarwalkerSeconds() { return 0; }
/*       */   protected void setFarwalkerSeconds(byte seconds) {}
/*       */   public void activeFarwalkerAmulet(Item amulet) {}
/*       */   public Creature getDominator() { if (this.dominator == -10L) return null;  try { return Server.getInstance().getCreature(this.dominator); } catch (Exception exception) { return null; }  }
/* 20005 */   public boolean mayChangeSpeed() { return (this.mountPollCounter <= 0); } public Item getWornItem(byte bodyPart) { try { return getEquippedItem(bodyPart); } catch (NoSpaceException nsp) { return null; }  } public boolean hasBridle() { if (isHorse() || isUnicorn()) { Item neckItem = getWornItem((byte)17); if (neckItem != null) return neckItem.isBridle();  }  return false; } private float calcHorseShoeBonus(boolean mounting) { float bonus = 0.0F; float leftFootB = 0.0F; float rightFootB = 0.0F; float leftHandB = 0.0F; float rightHandB = 0.0F; try { Item leftFoot = getEquippedItem((byte)15); if (leftFoot != null) { leftFootB += Math.max(10.0F, leftFoot.getCurrentQualityLevel()) / 2000.0F; leftFootB += leftFoot.getSpellSpeedBonus() / 2000.0F; leftFootB += leftFoot.getRarity() * 0.03F; if (!mounting && !this.ignoreSaddleDamage) leftFoot.setDamage(leftFoot.getDamage() + 0.001F);  }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " No left foot."); }  try { Item rightFoot = getEquippedItem((byte)16); if (rightFoot != null) { rightFootB += Math.max(10.0F, rightFoot.getCurrentQualityLevel()) / 2000.0F; rightFootB += rightFoot.getSpellSpeedBonus() / 2000.0F; rightFootB += rightFoot.getRarity() * 0.03F; if (!mounting && !this.ignoreSaddleDamage) rightFoot.setDamage(rightFoot.getDamage() + 0.001F);  }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " No left foot."); }  try { Item rightHand = getEquippedItem((byte)14); if (rightHand != null) { rightHandB += Math.max(10.0F, rightHand.getCurrentQualityLevel()) / 2000.0F; rightHandB += rightHand.getSpellSpeedBonus() / 2000.0F; rightHandB += rightHand.getRarity() * 0.03F; if (!mounting && !this.ignoreSaddleDamage) rightHand.setDamage(rightHand.getDamage() + 0.001F);  }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " No left foot."); }  try { Item leftHand = getEquippedItem((byte)13); if (leftHand != null) { leftHandB += Math.max(10.0F, leftHand.getCurrentQualityLevel()) / 2000.0F; leftHandB += leftHand.getSpellSpeedBonus() / 2000.0F; leftHandB += leftHand.getRarity() * 0.03F; if (!mounting && !this.ignoreSaddleDamage) leftHand.setDamage(leftHand.getDamage() + 0.001F);  }  } catch (NoSpaceException nsp) { logger.log(Level.WARNING, getName() + " No left foot."); }  bonus += leftHandB; bonus += rightHandB; bonus += leftFootB; bonus += rightFootB; return bonus; } public boolean hasHands() { return this.template.hasHands; } public boolean isDominated() { return (this.dominator > 0L); } public boolean setDominator(long newdominator) { if (newdominator == -10L) { if (this.decisions != null) { this.decisions.clearOrders(); this.decisions = null; }  try { setKingdomId((byte)0); } catch (IOException iox) { logger.log(Level.WARNING, iox.getMessage(), iox); }  setLoyalty(0.0F); setLeader(null); }  if (newdominator != this.dominator) { this.dominator = newdominator; getStatus().setDominator(this.dominator); sendAttitudeChange(); return true; }  return false; } public boolean hasPet() { return false; } public boolean isOnFire() { return this.template.isOnFire(); } public byte getFireRadius() { return this.template.getFireRadius(); } public int getPaintMode() { return this.template.getPaintMode(); } public boolean addOrder(Order order) { if (this.decisions == null) this.decisions = new DecisionStack();  return this.decisions.addOrder(order); } public void clearOrders() { if (this.decisions != null) this.decisions.clearOrders();  getStatus().setPath(null); getStatus().setMoving(false); setTarget(-10L, true); } public Order getFirstOrder() { if (this.decisions != null) return this.decisions.getFirst();  return null; } public void removeOrder(Order order) { if (this.decisions != null) this.decisions.removeOrder(order);  } public boolean hasOrders() { if (this.decisions != null) return this.decisions.hasOrders();  return false; } public boolean mayReceiveOrder() { if (this.decisions != null) return this.decisions.mayReceiveOrders();  if (isDominated()) { this.decisions = new DecisionStack(); return true; }  return false; } public void setPet(long petId) {} public Creature getPet() { return null; } public void modifyLoyalty(float modifier) { if (getStatus().modifyLoyalty(modifier)) { if (getDominator() != null) { getDominator().getCommunicator().sendAlertServerMessage(getNameWithGenus() + " is tame no more.", (byte)2); getDominator().setPet(-10L); }  setDominator(-10L); }  } public void setLoyalty(float loyalty) { getStatus().setLoyalty(loyalty); } public float getLoyalty() { return (getStatus()).loyalty; } public ArmourTemplate.ArmourType getArmourType() { return this.template.getArmourType(); } public boolean isFrozen() { return false; } public void toggleFrozen(Creature freezer) {} protected void setLastVehicle(long _lastvehicle, byte _seatType) { this.status.setVehicle(_lastvehicle, _seatType); } public void setVehicle(long vehicle, boolean teleport, byte _seatType) { setVehicle(vehicle, teleport, _seatType, -1, -1); } public void setVehicle(long _vehicle, boolean teleport, byte _seatType, int tilex, int tiley) { if (_vehicle == -10L) { if (this.vehicle != -10L) { removeIllusion(); if (getVisionArea() != null) { if (getVisionArea().getSurface() != null) getVisionArea().getSurface().clearMovementForCreature(this.vehicle);  if (getVisionArea().getUnderGround() != null) getVisionArea().getUnderGround().clearMovementForCreature(this.vehicle);  }  if (WurmId.getType(this.vehicle) == 1) { setLastVehicle(-10L, (byte)-1); try { Creature lVehicle = Server.getInstance().getCreature(this.vehicle); lVehicle.removeRider(getWurmId()); if (teleport) { Structure struct = getActualTileVehicle().getStructure(); if (struct != null && !struct.mayPass(this)) try { float newposx = lVehicle.getPosX(); float newposy = lVehicle.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   if (!isOnSurface()) if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) try { float newposx = lVehicle.getPosX(); float newposy = lVehicle.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }    }  } catch (NoSuchCreatureException nsi) { logger.log(Level.WARNING, getName() + " " + nsi.getMessage(), (Throwable)nsi); } catch (NoSuchPlayerException nsp) { logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp); }  } else { try { Item ivehic = Items.getItem(this.vehicle); boolean atTransferBorder = false; if (getTileX() < 20 || getTileX() > Zones.worldTileSizeX - 20 || getTileY() < 20 || getTileY() > Zones.worldTileSizeX - 20) atTransferBorder = true;  if (!ivehic.isBoat() || (!isTransferring() && !atTransferBorder)) { setLastVehicle(-10L, (byte)-1); if (teleport) { Structure struct = getActualTileVehicle().getStructure(); if (struct != null && struct.isTypeHouse() && !struct.mayPass(this)) try { Creature dragger = Items.getDragger(ivehic); float newposx = (dragger == null) ? ivehic.getPosX() : dragger.getPosX(); float newposy = (dragger == null) ? ivehic.getPosY() : dragger.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   if (struct != null && struct.isTypeBridge()) try { Creature dragger = Items.getDragger(ivehic); float newposx = (dragger == null) ? ivehic.getPosX() : dragger.getPosX(); float newposy = (dragger == null) ? ivehic.getPosY() : dragger.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }   if (!isOnSurface()) if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) try { float newposx = ivehic.getPosX(); float newposy = ivehic.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }    }  }  } catch (NoSuchItemException nsi) { setLastVehicle(-10L, (byte)-1); }  }  }  (getMovementScheme()).offZ = 0.0F; }  this.vehicle = _vehicle; this.seatType = _seatType; if (!isPlayer()) setLastVehicle(_vehicle, _seatType);  if (this.vehicle != -10L) { removeIllusion(); Vehicle vehic = Vehicles.getVehicleForId(this.vehicle); if (vehic != null) { clearDestination(); setFarwalkerSeconds((byte)0); getMovementScheme().setFarwalkerMoveMod(false); this.movementScheme.setEncumbered(false); this.movementScheme.setBaseModifier(1.0F); setStealth(false); float offx = 0.0F; float offy = 0.0F; for (int x = 0; x < vehic.seats.length; x++) { if ((vehic.seats[x]).occupant == getWurmId()) { offx = (vehic.seats[x]).offx; offy = (vehic.seats[x]).offy; break; }  }  if (vehic.creature) { try { Creature lVehicle = Server.getInstance().getCreature(this.vehicle); float r = (-lVehicle.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F; float s = (float)Math.sin(r); float c = (float)Math.cos(r); float xo = s * -offx - c * -offy; float yo = c * -offx + s * -offy; float newposx = lVehicle.getPosX() + xo; float newposy = lVehicle.getPosY() + yo; getMovementScheme().setVehicleRotation(lVehicle.getStatus().getRotation()); getStatus().setRotation(lVehicle.getStatus().getRotation()); setBridgeId(lVehicle.getBridgeId()); setTeleportPoints(newposx, newposy, lVehicle.getLayer(), lVehicle.getFloorLevel()); if (getVisionArea() != null && (int)newposx >> 2 == getTileX() && (int)newposy >> 2 == getTileY()) { embark(newposx, newposy, getPositionZ(), getStatus().getRotation(), this.teleportLayer, "Embarking " + vehic.name, null, lVehicle, vehic); } else if (!getCommunicator().stillLoggingIn()) { int tx = getTileX(); int ty = getTileY(); int nx = (int)newposx >> 2; int ny = (int)newposy >> 2; try { if (hasLink() && getVisionArea() != null) { getVisionArea().move(nx - tx, ny - ty); embark(newposx, newposy, getPositionZ(), getStatus().getRotation(), this.teleportLayer, "Embarking " + vehic.name, null, lVehicle, vehic); getVisionArea().linkZones(nx - tx, ny - ty); }  } catch (IOException ex) { startTeleporting(true); lVehicle.setLeader(null); lVehicle.addRider(getWurmId()); sendMountData(); if (isVehicleCommander()) { getCommunicator().sendTeleport(true, false, vehic.commandType); } else { getCommunicator().sendTeleport(false, false, (byte)0); }  }  } else { startTeleporting(true); lVehicle.setLeader(null); lVehicle.addRider(getWurmId()); sendMountData(); if (isVehicleCommander()) { getCommunicator().sendTeleport(true, false, vehic.commandType); } else { getCommunicator().sendTeleport(false, false, (byte)0); }  }  } catch (NoSuchCreatureException nsi) { logger.log(Level.WARNING, getName() + " " + nsi.getMessage(), (Throwable)nsi); } catch (NoSuchPlayerException nsp) { logger.log(Level.WARNING, getName() + " " + nsp.getMessage(), (Throwable)nsp); }  } else { try { Item lVehicle = Items.getItem(vehic.wurmid); float r = (-lVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F; float s = (float)Math.sin(r); float c = (float)Math.cos(r); float xo = s * -offx - c * -offy; float yo = c * -offx + s * -offy; float newposx = lVehicle.getPosX() + xo; float newposy = lVehicle.getPosY() + yo; getMovementScheme().setVehicleRotation(lVehicle.getRotation()); getStatus().setRotation(lVehicle.getRotation()); setBridgeId(lVehicle.getBridgeId()); if (getVisionArea() != null && (int)newposx >> 2 == getTileX() && (int)newposy >> 2 == getTileY()) { embark(newposx, newposy, getPositionZ(), getStatus().getRotation(), this.teleportLayer, "Embarking " + vehic.name, lVehicle, null, vehic); } else { setTeleportPoints(newposx, newposy, lVehicle.isOnSurface() ? 0 : -1, lVehicle.getFloorLevel()); if (isVehicleCommander()) { if (lVehicle.getKingdom() != getKingdomId()) { Server.getInstance().broadCastAction(LoginHandler.raiseFirstLetter(lVehicle.getName()) + " is now the property of " + Kingdoms.getNameFor(getKingdomId()) + "!", this, 10); String message = StringUtil.format("You declare the %s the property of %s.", new Object[] { lVehicle.getName(), Kingdoms.getNameFor(getKingdomId()) }); getCommunicator().sendNormalServerMessage(message); lVehicle.setLastOwnerId(getWurmId()); } else if (Servers.isThisAChaosServer()) { Village v = Villages.getVillageForCreature(lVehicle.getLastOwnerId()); if (v == null || v.isEnemy(getCitizenVillage())) { String vehname = getName(); if (getCitizenVillage() != null) vehname = getCitizenVillage().getName();  Server.getInstance().broadCastAction(LoginHandler.raiseFirstLetter(lVehicle.getName()) + " is now the property of " + vehname + "!", this, 10); String message = StringUtil.format("You declare the %s the property of %s.", new Object[] { lVehicle.getName(), vehname }); getCommunicator().sendNormalServerMessage(message); lVehicle.setLastOwnerId(getWurmId()); }  }  lVehicle.setAuxData(getKingdomId()); setEmbarkTeleportVehicle(newposx, newposy, vehic, lVehicle); } else { setEmbarkTeleportVehicle(newposx, newposy, vehic, lVehicle); }  }  } catch (NoSuchItemException nsi) { logger.log(Level.WARNING, getName() + " " + nsi.getMessage(), (Throwable)nsi); }  }  }  } else if (teleport) { if (tilex < 0 && tiley < 0 && !isOnSurface()) if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile((int)(getStatus().getPositionX() / 4.0F), (int)(getStatus().getPositionX() / 4.0F))))) try { if (WurmId.getType(this.vehicle) == 1) { Creature lVehicle = Server.getInstance().getCreature(this.vehicle); float newposx = lVehicle.getPosX(); float newposy = lVehicle.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; } else { Item ivehic = Items.getItem(this.vehicle); float newposx = ivehic.getPosX(); float newposy = ivehic.getPosY(); tilex = (int)newposx / 4; tiley = (int)newposy / 4; }  } catch (Exception ex) { logger.log(Level.WARNING, ex.getMessage(), ex); }    if (tilex > -1 || tiley > -1) { int ntx = tilex - getTileX(); int nty = tiley - getTileY(); float posz = getStatus().getPositionZ(); posz = Zones.calculatePosZ(getPosX(), getPosY(), getCurrentTile(), isOnSurface(), false, posz, this, getBridgeId()); try { if (hasLink() && getVisionArea() != null) { getVisionArea().move(ntx, nty); intraTeleport((tilex * 4 + 2), (tiley * 4 + 2), posz, getStatus().getRotation(), getLayer(), "left vehicle"); getVisionArea().linkZones(ntx, nty); }  } catch (IOException ex) { setTeleportPoints((short)tilex, (short)tiley, getLayer(), 0); startTeleporting(false); getCommunicator().sendTeleport(false, true, (byte)0); }  } else { Structure struct = (getCurrentTile() != null) ? getCurrentTile().getStructure() : Structures.getStructureForTile(getTileX(), getTileY(), isOnSurface()); if (struct == null || struct.mayPass(this)) { float posz = getStatus().getPositionZ(); posz = Zones.calculatePosZ(getPosX(), getPosY(), getCurrentTile(), isOnSurface(), false, posz, this, getBridgeId()); intraTeleport(getStatus().getPositionX(), getStatus().getPositionY(), posz, getStatus().getRotation(), getLayer(), "left vehicle"); }  }  getMovementScheme().addWindImpact((byte)0); calcBaseMoveMod(); (getMovementScheme()).commandingBoat = false; getCurrentTile().sendAttachCreature(getWurmId(), -1L, 0.0F, 0.0F, 0.0F, 0); } else { if (!getMovementScheme().isIntraTeleporting()) { getMovementScheme().addWindImpact((byte)0); calcBaseMoveMod(); getMovementScheme().setMooredMod(false); (getMovementScheme()).commandingBoat = false; }  getCurrentTile().sendAttachCreature(getWurmId(), -1L, 0.0F, 0.0F, 0.0F, 0); }  } public void intraTeleport(float posx, float posy, float posz, float aRot, int layer, String reason) { if (reason.contains("in rock")) { posx = (getMovementScheme()).xOld; posy = (getMovementScheme()).yOld; }  this.teleports++; if (isDead()) return;  posx = Math.max(0.0F, Math.min(posx, Zones.worldMeterSizeX - 1.0F)); posy = Math.max(0.0F, Math.min(posy, Zones.worldMeterSizeY - 1.0F)); VolaTile t = getCurrentTile(); if (t != null) { t.deleteCreatureQuick(this); } else { logger.log(Level.INFO, getName() + " no current tile when intrateleporting."); }  getStatus().setPositionX(posx); getStatus().setPositionY(posy); getStatus().setPositionZ(posz); getStatus().setRotation(aRot); if (layer == 0 && Zones.getTextureForTile((int)posx >> 2, (int)posy >> 2, layer) == Tiles.Tile.TILE_HOLE.id) layer = -1;  boolean visionAreaInitialized = false; if (getVisionArea() != null) visionAreaInitialized = getVisionArea().isInitialized();  if (!reason.contains("Embarking") && !reason.contains("left vehicle")) { logger.log(Level.INFO, getName() + " intrateleport to " + posx + "," + posy + ", " + posz + ", layer " + layer + " currentTile:null=" + ((t == null) ? 1 : 0) + " reason=" + reason + " hasVisionArea=" + ((getVisionArea() != null) ? 1 : 0) + ", initialized=" + visionAreaInitialized + " vehicle=" + this.vehicle, new Exception()); if (getPower() >= 3) getCommunicator().sendAlertServerMessage("IntraTeleporting " + reason);  }  getMovementScheme().setPosition(posx, posy, posz, aRot, layer); putInWorld(); getMovementScheme().haltSpeedModifier(); getCommunicator().setReady(false); getMovementScheme().setMooredMod(false); addCarriedWeight(0); try { sendActionControl("", false, 0); this.actions.stopCurrentAction(false); } catch (NoSuchActionException noSuchActionException) {} (getMovementScheme()).commandingBoat = false; getMovementScheme().addWindImpact((byte)0); getCommunicator().sendTeleport(true); disembark(false); getMovementScheme().addIntraTeleport(getTeleportCounter()); } public Vector3f getActualPosVehicle() { Vector3f toReturn = new Vector3f(getPosX(), getPosY(), getPositionZ()); if (this.vehicle != -10L) { Vehicle vehic = Vehicles.getVehicleForId(this.vehicle); if (vehic != null) { float offx = 0.0F; float offy = 0.0F; for (int x = 0; x < vehic.seats.length; x++) { if ((vehic.seats[x]).occupant == getWurmId()) { offx = (vehic.seats[x]).offx; offy = (vehic.seats[x]).offy; break; }  }  if (vehic.creature) { try { Creature lVehicle = Server.getInstance().getCreature(this.vehicle); float r = (-lVehicle.getStatus().getRotation() + 180.0F) * 3.1415927F / 180.0F; float s = (float)Math.sin(r); float c = (float)Math.cos(r); float xo = s * -offx - c * -offy; float yo = c * -offx + s * -offy; float newposx = lVehicle.getPosX() + xo; float newposy = lVehicle.getPosY() + yo; toReturn.setX(newposx); toReturn.setY(newposy); } catch (NoSuchCreatureException|NoSuchPlayerException noSuchCreatureException) {} } else { try { Item lVehicle = Items.getItem(vehic.wurmid); float r = (-lVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F; float s = (float)Math.sin(r); float c = (float)Math.cos(r); float xo = s * -offx - c * -offy; float yo = c * -offx + s * -offy; float newposx = lVehicle.getPosX() + xo; float newposy = lVehicle.getPosY() + yo; toReturn.setX(newposx); toReturn.setY(newposy); } catch (NoSuchItemException noSuchItemException) {} }  }  }  return toReturn; } protected VolaTile getActualTileVehicle() { Vector3f v = getActualPosVehicle(); int nx = (int)v.x >> 2; int ny = (int)v.y >> 2; return Zones.getOrCreateTile(nx, ny, isOnSurface()); } protected void setEmbarkTeleportVehicle(float newposx, float newposy, Vehicle vehic, Item lVehicle) { if (!getCommunicator().stillLoggingIn()) { int tx = getTileX(); int ty = getTileY(); int nx = (int)newposx >> 2; int ny = (int)newposy >> 2; try { if ((hasLink() || isWagoner()) && getVisionArea() != null) { getVisionArea().move(nx - tx, ny - ty); embark(newposx, newposy, getPositionZ(), getStatus().getRotation(), this.teleportLayer, "Embarking " + vehic.name, lVehicle, null, vehic); getVisionArea().linkZones(nx - tx, ny - ty); }  } catch (IOException ex) { startTeleporting(true); sendMountData(); getCommunicator().sendTeleport(true, false, vehic.commandType); }  } else { startTeleporting(true); sendMountData(); if (isVehicleCommander()) { getCommunicator().sendTeleport(true, false, vehic.commandType); } else { getCommunicator().sendTeleport(false, false, (byte)0); }  }  } private void embark(float posx, float posy, float posz, float aRot, int layer, String reason, @Nullable Item lVehicle, Creature cVehicle, Vehicle vehic) { if (!isVehicleCommander()) stopLeading();  VolaTile t = getCurrentTile(); if (t != null) { t.deleteCreatureQuick(this); } else { logger.log(Level.INFO, getName() + " no current tile when intrateleporting."); }  getStatus().setPositionX(posx); getStatus().setPositionY(posy); getStatus().setPositionZ(posz); getStatus().setRotation(aRot); if (layer == 0 && Zones.getTextureForTile((int)posx >> 2, (int)posy >> 2, layer) == Tiles.Tile.TILE_HOLE.id) layer = -1;  boolean setOffZ = false; if (this.mountAction != null) setOffZ = true;  if (setOffZ) { if (lVehicle != null) { float targetZ = lVehicle.getPosZ(); this.status.setPositionZ(targetZ + this.mountAction.getOffZ()); } else if (cVehicle != null) { float cretZ = cVehicle.getStatus().getPositionZ(); this.status.setPositionZ(cretZ + this.mountAction.getOffZ()); }  (getMovementScheme()).offZ = this.mountAction.getOffZ(); }  getMovementScheme().setPosition(posx, posy, this.status.getPositionZ(), this.status.getRotation(), getLayer()); putInWorld(); getMovementScheme().haltSpeedModifier(); getCommunicator().setReady(false); if (this.status.isTrading()) this.status.getTrade().end(this, false);  if (this.movementScheme.draggedItem != null) MethodsItems.stopDragging(this, this.movementScheme.draggedItem);  try { sendActionControl("", false, 0); this.actions.stopCurrentAction(false); } catch (NoSuchActionException noSuchActionException) {} this._enterVehicle = true; if (cVehicle != null) { cVehicle.setLeader(null); cVehicle.addRider(getWurmId()); }  sendMountData(); if (isVehicleCommander()) { if (lVehicle != null) { if (lVehicle.getKingdom() != getKingdomId()) { Server.getInstance().broadCastAction(LoginHandler.raiseFirstLetter(lVehicle.getName()) + " is now the property of " + Kingdoms.getNameFor(getKingdomId()) + "!", this, 10); String message = StringUtil.format("You declare the %s the property of %s.", new Object[] { lVehicle.getName(), Kingdoms.getNameFor(getKingdomId()) }); getCommunicator().sendNormalServerMessage(message); lVehicle.setLastOwnerId(getWurmId()); } else if (Servers.isThisAChaosServer()) { Village v = Villages.getVillageForCreature(lVehicle.getLastOwnerId()); if (v == null || v.isEnemy(getCitizenVillage())) { String vehname = getName(); if (getCitizenVillage() != null) vehname = getCitizenVillage().getName();  Server.getInstance().broadCastAction(LoginHandler.raiseFirstLetter(lVehicle.getName()) + " is now the property of " + vehname + "!", this, 10); String message = StringUtil.format("You declare the %s the property of %s.", new Object[] { lVehicle.getName(), vehname }); getCommunicator().sendNormalServerMessage(message); lVehicle.setLastOwnerId(getWurmId()); }  }  lVehicle.setAuxData(getKingdomId()); }  getCommunicator().sendTeleport(true, false, vehic.commandType); } else { getCommunicator().sendTeleport(true, false, (byte)0); }  getMovementScheme().addIntraTeleport(getTeleportCounter()); } public void disembark(boolean teleport) { disembark(teleport, -1, -1); } public void disembark(boolean teleport, int tilex, int tiley) { if (this.vehicle > -10L) { Vehicle vehic = Vehicles.getVehicleForId(this.vehicle); if (vehic != null) { if (vehic.pilotId == getWurmId()) { setVehicleCommander(false); vehic.pilotId = -10L; getCommunicator().setVehicleController(-1L, -1L, 0.0F, 0.0F, 0.0F, -2000.0F, 2000.0F, 2000.0F, 0.0F, 0); try { Item item = Items.getItem(this.vehicle); item.savePosition(); } catch (Exception exception) {} } else if (vehic.pilotId != -10L) { try { Item item = Items.getItem(this.vehicle); item.savePosition(); Creature pilot = Server.getInstance().getCreature(vehic.pilotId); if (!vehic.creature && item.isBoat()) { pilot.getMovementScheme().addMountSpeed((short)vehic.calculateNewBoatSpeed(true)); } else if (vehic.creature) { vehic.updateDraggedSpeed(true); }  } catch (WurmServerException wurmServerException) {  } catch (Exception exception) {} }  String vehicName = Vehicle.getVehicleName(vehic); if (vehic.isChair()) { getCommunicator().sendNormalServerMessage(StringUtil.format("You get up from the %s.", new Object[] { vehicName })); Server.getInstance().broadCastAction(StringUtil.format("%s gets up from the %s.", new Object[] { getName(), vehicName }), this, 5); } else { getCommunicator().sendNormalServerMessage(StringUtil.format("You leave the %s.", new Object[] { vehicName })); Server.getInstance().broadCastAction(StringUtil.format("%s leaves the %s.", new Object[] { getName(), vehicName }), this, 5); }  setVehicle(-10L, teleport, (byte)-1, tilex, tiley); int found = 0; for (int x = 0; x < vehic.seats.length; x++) { if ((vehic.seats[x]).occupant == getWurmId()) { (vehic.seats[x]).occupant = -10L; found++; }  }  if (found > 1) logger.log(Level.INFO, StringUtil.format("%s was occupying %d seats on %s.", new Object[] { getName(), Integer.valueOf(found), vehicName }));  } else { setVehicle(-10L, teleport, (byte)-1, tilex, tiley); }  }  } public int getTeleportCounter() { return 0; } public long getVehicle() { return this.vehicle; } public byte getSeatType() { return this.seatType; } public Vehicle getMountVehicle() { return Vehicles.getVehicleForId(getWurmId()); } public boolean isVehicleCommander() { return this.isVehicleCommander; } public double getVillageSkillModifier() { return 0.0D; } public void setVillageSkillModifier(double newModifier) {} public String getEmotePrefix() { return this.template.getName(); } public void playAnimation(String animationName, boolean looping) { if (this.currentTile != null) this.currentTile.sendAnimation(this, animationName, looping, -10L);  } public void playAnimation(String animationName, boolean looping, long aTarget) { if (this.currentTile != null) this.currentTile.sendAnimation(this, animationName, looping, aTarget);  } public void sendStance(byte stance) { if (this.currentTile != null) this.currentTile.sendStance(this, stance);  } public void sendDamage(float damPercent) { if (this.currentTile != null) this.currentTile.sendCreatureDamage(this, damPercent);  } public void sendFishingLine(float posX, float posY, byte floatType) { if (this.currentTile != null) this.currentTile.sendFishingLine(this, posX, posY, floatType);  } public void sendFishHooked(byte fishType, long fishId) { if (this.currentTile != null) this.currentTile.sendFishHooked(this, fishType, fishId);  } public void sendFishingStopped() { if (this.currentTile != null) this.currentTile.sendFishingStopped(this);  } public void sendSpearStrike(float posX, float posY) { if (this.currentTile != null) this.currentTile.sendSpearStrike(this, posX, posY);  } public void checkTheftWarnQuestion() {} public void setTheftWarned(boolean warned) {} public void checkChallengeWarnQuestion() {} public void setChallengeWarned(boolean warned) {} public void addEnemyPresense() {} public void removeEnemyPresense() {} public int getEnemyPresense() { return 0; } public boolean mayMute() { return false; } public boolean hasNoReimbursement() { return true; } public boolean isDeathProtected() { return false; } public void setDeathProtected(boolean _deathProtected) {} public long mayChangeVillageInMillis() { return 0L; } public boolean hasGlow() { if (getPower() > 0) return true;  return this.template.isGlowing(); } public void loadAffinities() {} public void increaseAffinity(int skillnumber, int value) {} public void decreaseAffinity(int skillnumber, int value) {} public boolean mayOpportunityAttack() { if (isStunned()) return false;  if (this.opportunityAttackCounter > 0) return false;  return (getCombatHandler().getOpportunityAttacks() < getFightingSkill().getKnowledge(0.0D) / 10.0D); } public boolean opportunityAttack(Creature creature) { if (creature.isInvulnerable()) return false;  if (!creature.isVisibleTo(this)) return false;  if (isPlayer() && creature.isPlayer() && !Servers.isThisAPvpServer() && !isDuelOrSpar(creature)) return false;  if (isFighting() || creature.getWurmId() == this.target) if (!isPlayer() || !creature.isPlayer()) { if (isBridgeBlockingAttack(creature, false)) return false;  if (mayOpportunityAttack()) if (getLayer() == creature.getLayer() && getMindSpeed().skillCheck((getCombatHandler().getOpportunityAttacks() * 10), 0.0D, false, 1.0F) > 0.0D) { if (this.opponent == null) setOpponent(creature);  return getCombatHandler().attack(creature, 10, true, 2.0F, null); }   }   return false; } public boolean isSparring(Creature _opponent) { return false; } public boolean isDuelling(Creature _opponent) { return false; } public boolean isDuelOrSpar(Creature _opponent) { return false; } public void setChangedTileCounter() {} public boolean isStealth() { return this.status.stealth; } public void setStealth(boolean stealth) { if (this.status.setStealth(stealth)) { if (stealth) { this.stealthBreakers = new HashSet<>(); if (isPlayer()) getCommunicator().sendNormalServerMessage("You attempt to hide from others.", (byte)4);  this.movementScheme.setStealthMod(true); } else { if (this.stealthBreakers != null) this.stealthBreakers.clear();  getCommunicator().sendNormalServerMessage("You no longer hide.", (byte)4); this.movementScheme.setStealthMod(false); }  checkInvisDetection(); }  } public void checkInvisDetection() { if (getBody().getBodyItem() != null) getCurrentTile().checkVisibility(this, (!isVisible() || isStealth()));  } public boolean visibilityCheck(Creature watcher, float difficultyModifier) { if (!isVisible()) return (getPower() > 0 && getPower() <= watcher.getPower());  if (isStealth()) { if (getPower() > 0 && getPower() <= watcher.getPower()) return true;  if (getPower() < watcher.getPower()) return true;  if (watcher.isUnique()) return true;  if (this.stealthBreakers != null) if (this.stealthBreakers.contains(Long.valueOf(watcher.getWurmId()))) return true;   int distModifier = (int)Math.max(Math.abs(watcher.getPosX() - getPosX()), Math.abs(watcher.getPosY() - getPosY())); if (watcher.getCurrentTile() == getCurrentTile() || watcher.isDetectInvis() || Server.rand.nextInt((int)(100.0F + difficultyModifier + distModifier)) < watcher.getDetectDangerBonus() / 5.0F || watcher.getMindLogical().skillCheck(getBodyControl() + difficultyModifier + distModifier, 0.0D, true, 1.0F) > 0.0D) { if (this.stealthBreakers == null) this.stealthBreakers = new HashSet<>();  this.stealthBreakers.add(Long.valueOf(watcher.getWurmId())); return true; }  return false; }  return true; } public boolean isDetectInvis() { if (this.template.isDetectInvis()) return true;  if (this.status.detectInvisCounter > 0) return true;  return false; } public boolean isVisibleTo(Creature watcher) { return isVisibleTo(watcher, false); } public boolean isVisibleTo(Creature watcher, boolean ignoreStealth) { if (!isVisible()) return (getPower() > 0 && getPower() <= watcher.getPower());  if (isStealth() && !ignoreStealth) { if (getPower() > 0 && getPower() <= watcher.getPower()) return true;  if (getPower() < watcher.getPower()) return true;  if (watcher.isUnique() || watcher.isDetectInvis()) return true;  if (this.stealthBreakers != null && this.stealthBreakers.contains(Long.valueOf(watcher.getWurmId()))) return true;  return false; }  return true; } public void addVisionModifier(DoubleValueModifier modifier) { if (this.visionModifiers == null) this.visionModifiers = new HashSet<>();  this.visionModifiers.add(modifier); } public void removeVisionModifier(DoubleValueModifier modifier) { if (this.visionModifiers != null) this.visionModifiers.remove(modifier);  } public double getVisionMod() { if (this.visionModifiers == null) return 0.0D;  double doubleModifier = 0.0D; for (DoubleValueModifier lDoubleValueModifier : this.visionModifiers) doubleModifier += lDoubleValueModifier.getModifier();  return doubleModifier; } public int[] getCombatMoves() { return this.template.getCombatMoves(); } public boolean isGuide() { return this.template.isTutorial(); } public int getTutorialLevel() { return 9999; } public void setTutorialLevel(int newLevel) {} public boolean skippedTutorial() { return false; } public String getCurrentMissionInstruction() { return ""; } public void missionFinished(boolean reward, boolean sendpopup) {} public boolean isNoSkillgain() { return (this.template.isNoSkillgain() || isBred() || !isPaying()); } public boolean isAutofight() { return false; } public final float getDamageModifier(boolean pvp, boolean spell) { double strength; if (!spell) { strength = getStrengthSkill(); } else { strength = getSoulStrengthVal(); }  float damMod = (float)(120.0D - strength) / 100.0F; if (isPlayer() && pvp && Servers.localServer.PVPSERVER) { damMod = (float)(1.0D - 0.15D * Math.log(Math.max(20.0D, strength) * 0.800000011920929D - 15.0D)); damMod = Math.max(Math.min(damMod, 1.0F), 0.2F); }  if (hasSpellEffect((byte)96)) damMod *= 1.1F;  if (getCultist() != null) { float percent = getCultist().getHalfDamagePercentage(); if (percent > 0.0F) if (isChampion()) { float red = 1.0F - 0.1F * percent; damMod *= red; } else { float red = 1.0F - 0.3F * percent; damMod *= red; }   }  return damMod; } public String toString() { return "Creature [id: " + this.id + ", name: " + this.name + ", Tile: " + this.currentTile + ", Template: " + this.template + ", Status: " + this.status + ']'; } public void sendToLoggers(String tolog) { sendToLoggers(tolog, (byte)2); } public void sendToLoggers(String tolog, byte restrictedToPower) { if (this.loggerCreature1 != -10L) try { Creature receiver = Server.getInstance().getCreature(this.loggerCreature1); receiver.getCommunicator().sendLogMessage(getName() + " [" + tolog + "]"); } catch (Exception ex) { this.loggerCreature1 = -10L; }   if (this.loggerCreature2 != -10L) try { Creature receiver = Server.getInstance().getCreature(this.loggerCreature2); receiver.getCommunicator().sendLogMessage(getName() + " [" + tolog + "]"); } catch (Exception ex) { this.loggerCreature2 = -10L; }   } public long getAppointments() { return 0L; } public void addAppointment(int aid) {} public void removeAppointment(int aid) {} public boolean isFloating() { return this.template.isFloating(); } public boolean hasAppointment(int aid) { return false; } public boolean isKing() { return false; } public final boolean isEligibleForKingdomBonus() { if (hasCustomKingdom()) { King king = King.getKing(getKingdomId()); if (king != null) return (king.currentLand > 2.0F);  return false; }  return true; } public String getAppointmentTitles() { return ""; } public boolean isRoyalAnnouncer() { return King.isOfficial(1510, getWurmId(), getKingdomId()); } public boolean isRoyalChef() { return King.isOfficial(1509, getWurmId(), getKingdomId()); } public boolean isRoyalPriest() { return King.isOfficial(1506, getWurmId(), getKingdomId()); } public boolean isRoyalSmith() { return King.isOfficial(1503, getWurmId(), getKingdomId()); } public boolean isRoyalExecutioner() { return King.isOfficial(1508, getWurmId(), getKingdomId()); } public boolean isEconomicAdvisor() { return King.isOfficial(1505, getWurmId(), getKingdomId()); } public boolean isInformationOfficer() { return King.isOfficial(1500, getWurmId(), getKingdomId()); } public String getAnnounceString() { return getName() + '!'; } public boolean isAppointed() { return false; } public boolean isArcheryMode() { return false; } public MusicPlayer getMusicPlayer() { return this.musicPlayer; } public int getPushCounter() { return 200; } public void setPushCounter(int val) {} public Seat getSeat() { return null; } public void setMountAction(@Nullable MountAction act) { this.mountAction = act; } public void activePotion(Item potion) {} public byte getCRCounterBonus() { return 0; } public boolean isNoAttackVehicles() { return !this.template.attacksVehicles; } public int getMaxNumActions() { return 10; } public int hashCode() { int prime = 31; int result = 1; result = 31 * result + (int)(this.id ^ this.id >>> 32L); result = 31 * result + (isPlayer() ? 1231 : 1237); return result; } public void setCheated(String reason) {} public boolean equals(Object obj) { if (this == obj) return true;  if (obj == null) return false;  if (!(obj instanceof Creature)) return false;  Creature other = (Creature)obj; if (this.id != other.id) return false;  if (isPlayer() != other.isPlayer()) return false;  return true; } public boolean seesPlayerAssistantWindow() { return false; } public void setHitched(@Nullable Vehicle _hitched, boolean loading) { this.hitchedTo = _hitched; if (this.hitchedTo != null) { clearOrders(); this.seatType = 2; if (!loading) getStatus().setVehicle(this.hitchedTo.wurmid, this.seatType);  } else { this.seatType = -1; getStatus().setVehicle(-10L, this.seatType); }  } public Vehicle getHitched() { return this.hitchedTo; } public boolean isPlayerAssistant() { return false; } public boolean isVehicle() { return this.template.isVehicle; } public Set<Long> getRiders() { return this.riders; } public boolean isRidden() { return (this.riders != null && this.riders.size() > 0); }
/*       */   public boolean isRiddenBy(long wurmid) { return (this.riders != null && this.riders.contains(Long.valueOf(wurmid))); }
/*       */   public void addRider(long newrider) { if (this.riders == null) this.riders = new HashSet<>();  this.riders.add(Long.valueOf(newrider)); }
/*       */   public void removeRider(long lostrider) { if (this.riders == null) this.riders = new HashSet<>();  this.riders.remove(Long.valueOf(lostrider)); }
/*       */   protected void forceMountSpeedChange() { this.mountPollCounter = 0; pollMount(); }
/* 20010 */   public float getMountSpeedPercent(boolean mounting) { float factor = 0.5F;
/* 20011 */     if (getStatus().getHunger() < 45000)
/* 20012 */       factor += 0.2F; 
/* 20013 */     if (getStatus().getHunger() < 10000)
/* 20014 */       factor += 0.1F; 
/* 20015 */     if ((getStatus()).damage < 10000) {
/* 20016 */       factor += 0.1F;
/* 20017 */     } else if ((getStatus()).damage > 20000) {
/* 20018 */       factor -= 0.5F;
/* 20019 */     } else if ((getStatus()).damage > 45000) {
/* 20020 */       factor -= 0.7F;
/*       */     } 
/*       */     
/* 20023 */     if (isHorse() || isUnicorn()) {
/*       */       
/* 20025 */       float hbonus = calcHorseShoeBonus(mounting);
/* 20026 */       sendToLoggers("Horse shoe bonus " + hbonus + " so factor from " + factor + " to " + (factor + hbonus));
/* 20027 */       factor += hbonus;
/*       */     } 
/* 20029 */     float tperc = getTraitMovePercent(mounting);
/*       */     
/* 20031 */     sendToLoggers("Trait move percent= " + tperc + " so factor from " + factor + " to " + (factor + tperc));
/* 20032 */     factor += tperc;
/*       */ 
/*       */     
/* 20035 */     if (getBonusForSpellEffect((byte)22) > 0.0F)
/*       */     {
/* 20037 */       factor -= 0.2F * getBonusForSpellEffect((byte)22) / 100.0F;
/*       */     }
/*       */     
/* 20040 */     if (isRidden()) {
/*       */       
/* 20042 */       Item torsoItem = getWornItem((byte)2);
/* 20043 */       if (torsoItem != null)
/*       */       {
/* 20045 */         if (torsoItem.isSaddleLarge() || torsoItem.isSaddleNormal()) {
/*       */           
/* 20047 */           factor += Math.max(10.0F, torsoItem.getCurrentQualityLevel()) / 1000.0F;
/* 20048 */           factor += torsoItem.getRarity() * 0.03F;
/* 20049 */           factor += torsoItem.getSpellSpeedBonus() / 2000.0F;
/*       */           
/* 20051 */           if (!mounting && !this.ignoreSaddleDamage)
/* 20052 */             torsoItem.setDamage(torsoItem.getDamage() + 0.001F); 
/* 20053 */           this.ignoreSaddleDamage = false;
/*       */         } 
/*       */       }
/* 20056 */       sendToLoggers("After saddle move percent= " + factor);
/* 20057 */       factor *= getMovementScheme().getSpeedModifier();
/* 20058 */       sendToLoggers("After speedModifier " + getMovementScheme().getSpeedModifier() + " move percent= " + factor);
/*       */     } 
/* 20060 */     return factor; }
/*       */ 
/*       */ 
/*       */   
/*       */   private int getCarriedMountWeight() {
/* 20065 */     int currWeight = getCarriedWeight();
/* 20066 */     int bagsWeight = getSaddleBagsCarriedWeight();
/* 20067 */     currWeight -= bagsWeight;
/* 20068 */     if (isRidden())
/*       */     {
/* 20070 */       for (Long lLong : this.riders) {
/*       */ 
/*       */         
/*       */         try {
/* 20074 */           Creature _rider = Server.getInstance().getCreature(lLong.longValue());
/* 20075 */           currWeight += Math.max(30000, (_rider.getStatus()).fat * 1000);
/* 20076 */           currWeight += _rider.getCarriedWeight();
/*       */         }
/* 20078 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */ 
/*       */         
/*       */         }
/* 20083 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */       } 
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 20090 */     return currWeight;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasTraits() {
/* 20095 */     return (this.status.traits != 0L);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasTrait(int traitbit) {
/* 20100 */     if (this.status.traits != 0L)
/*       */     {
/* 20102 */       return this.status.isTraitBitSet(traitbit);
/*       */     }
/* 20104 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasAbility(int abilityBit) {
/* 20109 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasFlag(int flagBit) {
/* 20114 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setFlag(int number, boolean value) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setAbility(int number, boolean value) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setTagItem(long itemId, String itemName) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public String getTaggedItemName() {
/* 20135 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public long getTaggedItemId() {
/* 20141 */     return -10L;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean removeRandomNegativeTrait() {
/* 20146 */     if (this.status.traits != 0L)
/*       */     {
/* 20148 */       return this.status.removeRandomNegativeTrait();
/*       */     }
/* 20150 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private float getTraitMovePercent(boolean mounting) {
/* 20155 */     float traitMod = 0.0F;
/* 20156 */     Creature r = null;
/* 20157 */     boolean moving = false;
/* 20158 */     if (isRidden() && getMountVehicle() != null) {
/*       */       
/*       */       try {
/*       */         
/* 20162 */         r = Server.getInstance().getCreature((getMountVehicle()).pilotId);
/* 20163 */         moving = r.isMoving();
/*       */       }
/* 20165 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */ 
/*       */       
/*       */       }
/* 20170 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 20176 */     int cweight = getCarriedMountWeight();
/*       */ 
/*       */ 
/*       */     
/* 20180 */     if (!mounting && this.status.traits != 0L) {
/*       */ 
/*       */ 
/*       */       
/* 20184 */       Skill sstrength = getSoulStrength();
/* 20185 */       if (this.status.isTraitBitSet(1) && (!isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20186 */         traitMod += 0.1F; 
/* 20187 */       if (!this.status.isTraitBitSet(15) && !this.status.isTraitBitSet(16) && 
/* 20188 */         !this.status.isTraitBitSet(17) && !this.status.isTraitBitSet(18) && 
/* 20189 */         !this.status.isTraitBitSet(24) && !this.status.isTraitBitSet(25) && this.status
/* 20190 */         .isTraitBitSet(23) && (
/* 20191 */         !isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20192 */         traitMod += 0.025F; 
/* 20193 */       if (this.status.isTraitBitSet(4) && (
/* 20194 */         !isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20195 */         traitMod += 0.2F; 
/* 20196 */       if (this.status.isTraitBitSet(8) && (!isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) < 0.0D))
/* 20197 */         traitMod -= 0.1F; 
/* 20198 */       if (this.status.isTraitBitSet(9) && (!isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) < 0.0D))
/* 20199 */         traitMod -= 0.3F; 
/* 20200 */       if (this.status.isTraitBitSet(6) && (
/* 20201 */         !isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20202 */         traitMod += 0.1F; 
/* 20203 */       float wmod = 0.0F;
/*       */       
/* 20205 */       if (this.status.isTraitBitSet(3) && (
/* 20206 */         !isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20207 */         wmod += 10000.0F; 
/* 20208 */       if (this.status.isTraitBitSet(5) && (!isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20209 */         wmod += 20000.0F; 
/* 20210 */       if (this.status.isTraitBitSet(11) && (!isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) < 0.0D))
/* 20211 */         wmod -= 30000.0F; 
/* 20212 */       if (this.status.isTraitBitSet(6) && (
/* 20213 */         !isHorse() || sstrength.skillCheck(20.0D, 0.0D, !moving, 1.0F) > 0.0D))
/* 20214 */         wmod += 10000.0F; 
/* 20215 */       if (cweight > getStrengthSkill() * 5000.0D + wmod)
/*       */       {
/*       */ 
/*       */ 
/*       */         
/* 20220 */         traitMod = (float)(traitMod - 0.15D * (cweight - getStrengthSkill() * 5000.0D - wmod) / 50000.0D);
/*       */ 
/*       */       
/*       */       }
/*       */ 
/*       */     
/*       */     }
/* 20227 */     else if (cweight > getStrengthSkill() * 5000.0D) {
/*       */ 
/*       */ 
/*       */       
/* 20231 */       traitMod = (float)(traitMod - 0.15D * (cweight - getStrengthSkill() * 5000.0D) / 50000.0D);
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 20239 */     return traitMod;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isHorse() {
/* 20244 */     return this.template.isHorse;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isUnicorn() {
/* 20249 */     return this.template.isUnicorn();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean cantRideUntame() {
/* 20254 */     return this.template.cantRideUntamed();
/*       */   }
/*       */ 
/*       */   
/*       */   public static void setRandomColor(Creature creature) {
/* 20259 */     if (Server.rand.nextInt(3) == 0) {
/* 20260 */       creature.getStatus().setTraitBit(15, true);
/* 20261 */     } else if (Server.rand.nextInt(3) == 0) {
/* 20262 */       creature.getStatus().setTraitBit(16, true);
/* 20263 */     } else if (Server.rand.nextInt(3) == 0) {
/* 20264 */       creature.getStatus().setTraitBit(17, true);
/* 20265 */     } else if (Server.rand.nextInt(3) == 0) {
/* 20266 */       creature.getStatus().setTraitBit(18, true);
/* 20267 */     } else if (Server.rand.nextInt(6) == 0) {
/* 20268 */       creature.getStatus().setTraitBit(24, true);
/* 20269 */     } else if (Server.rand.nextInt(12) == 0) {
/* 20270 */       creature.getStatus().setTraitBit(25, true);
/* 20271 */     } else if (Server.rand.nextInt(24) == 0) {
/* 20272 */       creature.getStatus().setTraitBit(23, true);
/*       */     
/*       */     }
/* 20275 */     else if ((creature.getTemplate()).maxColourCount > 8) {
/*       */       
/* 20277 */       if (Server.rand.nextInt(6) == 0) {
/* 20278 */         creature.getStatus().setTraitBit(30, true);
/* 20279 */       } else if (Server.rand.nextInt(6) == 0) {
/* 20280 */         creature.getStatus().setTraitBit(31, true);
/* 20281 */       } else if (Server.rand.nextInt(6) == 0) {
/* 20282 */         creature.getStatus().setTraitBit(32, true);
/* 20283 */       } else if (Server.rand.nextInt(6) == 0) {
/* 20284 */         creature.getStatus().setTraitBit(33, true);
/* 20285 */       } else if (Server.rand.nextInt(6) == 0) {
/* 20286 */         creature.getStatus().setTraitBit(34, true);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean mayMate(Creature potentialMate) {
/* 20293 */     if (isDead() || potentialMate.isDead()) {
/* 20294 */       return false;
/*       */     }
/*       */     
/* 20297 */     if (potentialMate.getTemplate().getMateTemplateId() == this.template.getTemplateId() || (this.template
/* 20298 */       .getTemplateId() == 96 && potentialMate.getTemplate().getTemplateId() == 96)) {
/*       */ 
/*       */       
/* 20301 */       if (this.template.getAdultFemaleTemplateId() != -1 || this.template
/* 20302 */         .getAdultMaleTemplateId() != -1)
/* 20303 */         return false; 
/* 20304 */       if (potentialMate.getSex() != getSex())
/*       */       {
/* 20306 */         if (potentialMate.getWurmId() != getWurmId())
/* 20307 */           return true; 
/*       */       }
/*       */     } 
/* 20310 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean checkBreedingPossibility() {
/* 20315 */     Creature[] crets = getCurrentTile().getCreatures();
/* 20316 */     if (!isKingdomGuard() && !isGhost() && !isHuman() && crets.length > 0)
/*       */     {
/* 20318 */       if (mayMate(crets[0]))
/*       */       {
/* 20320 */         if (!crets[0].isPregnant() && !isPregnant()) {
/*       */           
/*       */           try {
/*       */             
/* 20324 */             BehaviourDispatcher.action(this, getCommunicator(), -1L, crets[0].getWurmId(), (short)379);
/* 20325 */             return true;
/*       */           }
/* 20327 */           catch (Exception ex) {
/*       */             
/* 20329 */             return false;
/*       */           } 
/*       */         }
/*       */       }
/*       */     }
/* 20334 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheMoodToBreed(boolean forced) {
/* 20340 */     if (getStatus().getHunger() > 10000) {
/* 20341 */       return false;
/*       */     }
/* 20343 */     if (this.template.getAdultFemaleTemplateId() != -1 || this.template.getAdultMaleTemplateId() != -1)
/* 20344 */       return false; 
/* 20345 */     if ((getStatus()).age <= 3) {
/* 20346 */       return false;
/*       */     }
/*       */     
/* 20349 */     return (this.breedCounter == 0 || (forced && !this.forcedBreed));
/*       */   }
/*       */ 
/*       */   
/*       */   public int getBreedCounter() {
/* 20354 */     return this.breedCounter;
/*       */   }
/*       */ 
/*       */   
/*       */   public void resetBreedCounter() {
/* 20359 */     this.forcedBreed = true;
/* 20360 */     this.breedCounter = (Servers.isThisAPvpServer() ? 900 : 2000) + Server.rand.nextInt(Math.max(1000, 100 * Math.abs(20 - (getStatus()).age)));
/*       */   }
/*       */ 
/*       */   
/*       */   public long getMother() {
/* 20365 */     return this.status.mother;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getFather() {
/* 20370 */     return this.status.father;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMeditateX() {
/* 20380 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMeditateY() {
/* 20390 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setMeditateX(int tilex) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setMeditateY(int tiley) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setDisease(byte newDisease) {
/* 20411 */     boolean changed = false;
/* 20412 */     if ((getStatus()).disease > 0 && newDisease <= 0) {
/*       */       
/* 20414 */       if (getPower() < 2)
/* 20415 */         setVisible(false); 
/* 20416 */       changed = true;
/* 20417 */       getCommunicator().sendSafeServerMessage("You feel a lot better now as your disease is gone.", (byte)2);
/*       */       
/* 20419 */       if (isPlayer())
/*       */       {
/* 20421 */         getCommunicator().sendRemoveSpellEffect(SpellEffectsEnum.DISEASE);
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 20426 */     else if (isPlayer() && newDisease > 0) {
/*       */       
/* 20428 */       getCommunicator().sendAddSpellEffect(SpellEffectsEnum.DISEASE, 100000, 
/* 20429 */           (getStatus()).disease);
/* 20430 */       achievement(173);
/*       */     } 
/*       */     
/* 20433 */     if ((getStatus()).disease == 0 && newDisease == 1) {
/*       */       
/* 20435 */       if (isUnique() || isKingdomGuard() || isGhost() || this.status.modtype == 11)
/*       */         return; 
/* 20437 */       if (getPower() < 2)
/* 20438 */         setVisible(false); 
/* 20439 */       changed = true;
/* 20440 */       getCommunicator().sendAlertServerMessage("You scratch yourself. What did you catch now?", (byte)2);
/*       */ 
/*       */       
/* 20443 */       achievement(568);
/*       */     } 
/*       */     
/* 20446 */     getStatus().setDisease(newDisease);
/* 20447 */     if (changed && getPower() < 2) {
/* 20448 */       setVisible(true);
/*       */     }
/*       */   }
/*       */   
/*       */   public byte getDisease() {
/* 20453 */     return (getStatus()).disease;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getLastGroomed() {
/* 20458 */     return (getStatus()).lastGroomed;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setLastGroomed(long newLastGroomed) {
/* 20463 */     getStatus().setLastGroomed(newLastGroomed);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean canBeGroomed() {
/* 20468 */     return (System.currentTimeMillis() - getLastGroomed() > 3600000L);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isDomestic() {
/* 20473 */     return this.template.domestic;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLastKingdom() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addLink(Creature creature) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isLinked() {
/* 20494 */     return (this.linkedTo != -10L);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature getCreatureLinkedTo() {
/*       */     try {
/* 20501 */       return Server.getInstance().getCreature(this.linkedTo);
/*       */     }
/* 20503 */     catch (Exception exception) {
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 20508 */       return null;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeLink(long wurmid) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getNumLinks() {
/* 20524 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Creature[] getLinks() {
/* 20532 */     return emptyCreatures;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void clearLinks() {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLinkedTo(long wid, boolean linkback) {
/* 20545 */     this.linkedTo = wid;
/*       */   }
/*       */ 
/*       */   
/*       */   public void disableLink() {
/* 20550 */     setLinkedTo(-10L, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setFatigue(int fatigueToAdd) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isMissionairy() {
/* 20566 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public long getLastChangedPriestType() {
/* 20574 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setPriestType(byte type) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setPrayerSeconds(int prayerSeconds) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public long getLastChangedJoat() {
/* 20599 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void resetJoat() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Team getTeam() {
/* 20615 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setTeam(@Nullable Team newTeam, boolean sendRemove) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isTeamLeader() {
/* 20632 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mayInviteTeam() {
/* 20640 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setMayInviteTeam(boolean mayInvite) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendSystemMessage(String message) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendHelpMessage(String message) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void makeEmoteSound() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void poisonChanged(boolean hadPoison, Wound w) {
/* 20677 */     if (hadPoison) {
/*       */       
/* 20679 */       if (!isPoisoned())
/*       */       {
/*       */         
/* 20682 */         getCommunicator().sendRemoveSpellEffect(w.getWurmId(), SpellEffectsEnum.POISON);
/* 20683 */         this.hasSentPoison = false;
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 20688 */     else if (!this.hasSentPoison) {
/*       */ 
/*       */       
/* 20691 */       getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POISON.createId(w.getWurmId()), SpellEffectsEnum.POISON, 100000, w
/* 20692 */           .getPoisonSeverity());
/* 20693 */       this.hasSentPoison = true;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendAllPoisonEffect() {
/* 20700 */     Wounds w = getBody().getWounds();
/* 20701 */     if (w != null)
/*       */     {
/* 20703 */       if (w.getWounds() != null) {
/*       */         
/* 20705 */         Wound[] warr = w.getWounds();
/* 20706 */         for (int a = 0; a < warr.length; a++) {
/*       */           
/* 20708 */           if (warr[a].isPoison()) {
/*       */             
/* 20710 */             getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POISON.createId(warr[a].getWurmId()), SpellEffectsEnum.POISON, 100000, warr[a]
/*       */                 
/* 20712 */                 .getPoisonSeverity());
/* 20713 */             this.hasSentPoison = true;
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPoisoned() {
/* 20722 */     Wounds w = getBody().getWounds();
/* 20723 */     if (w != null)
/*       */     {
/* 20725 */       if (w.getWounds() != null) {
/*       */         
/* 20727 */         Wound[] warr = w.getWounds();
/* 20728 */         for (int a = 0; a < warr.length; a++) {
/*       */           
/* 20730 */           if (warr[a].isPoison())
/* 20731 */             return true; 
/*       */         } 
/*       */       } 
/*       */     }
/* 20735 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mayEmote() {
/* 20743 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasSkillGain() {
/* 20751 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setHasSkillGain(boolean hasSkillGain) {
/* 20759 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getChampTimeStamp() {
/* 20764 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void becomeChamp() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public void revertChamp() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public long getLastChangedCluster() {
/* 20779 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setLastChangedCluster() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheNorthWest() {
/* 20789 */     return (getTileX() < Zones.worldTileSizeX / 3 && getTileY() < Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheNorth() {
/* 20794 */     return (getTileY() < Zones.worldTileSizeX / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheNorthEast() {
/* 20799 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3 && getTileY() < Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheEast() {
/* 20804 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheSouthEast() {
/* 20809 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3 && 
/* 20810 */       getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheSouth() {
/* 20815 */     return (getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheSouthWest() {
/* 20820 */     return (getTileX() < Zones.worldTileSizeX / 3 && getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheWest() {
/* 20825 */     return (getTileX() < Zones.worldTileSizeX / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public int getGlobalMapPlacement() {
/* 20830 */     if (isInTheNorthWest())
/* 20831 */       return 7; 
/* 20832 */     if (isInTheNorthEast())
/* 20833 */       return 1; 
/* 20834 */     if (isInTheSouthEast())
/* 20835 */       return 3; 
/* 20836 */     if (isInTheSouthWest())
/* 20837 */       return 5; 
/* 20838 */     if (isInTheNorth())
/* 20839 */       return 0; 
/* 20840 */     if (isInTheEast())
/* 20841 */       return 2; 
/* 20842 */     if (isInTheSouth())
/* 20843 */       return 4; 
/* 20844 */     if (isInTheWest()) {
/* 20845 */       return 6;
/*       */     }
/* 20847 */     return -1;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean mayDestroy(Item item) {
/* 20852 */     if (item.isDestroyable(getWurmId()))
/* 20853 */       return true; 
/* 20854 */     if (item.isOwnerDestroyable() && !item.isLocked()) {
/*       */       
/* 20856 */       Village village = Zones.getVillage(item.getTilePos(), item.isOnSurface());
/* 20857 */       if (village != null)
/* 20858 */         return village.isActionAllowed((short)83, this); 
/* 20859 */       if (item.isUnfinished()) {
/*       */         
/* 20861 */         if (item.getRealTemplate() != null && item.getRealTemplate().isKingdomMarker() && 
/* 20862 */           getKingdomId() != item.getAuxData()) {
/* 20863 */           return true;
/*       */         }
/*       */       } else {
/* 20866 */         return true;
/*       */       } 
/*       */     } 
/* 20869 */     if (item.isEnchantedTurret()) {
/*       */       
/* 20871 */       VolaTile t = Zones.getTileOrNull(item.getTileX(), item.getTileY(), item.isOnSurface());
/* 20872 */       if (t != null)
/*       */       {
/* 20874 */         if (t.getVillage() != null && (t.getVillage()).isPermanent)
/*       */         {
/* 20876 */           if ((t.getVillage()).kingdom == item.getKingdom())
/* 20877 */             return false; 
/*       */         }
/*       */       }
/*       */     } 
/* 20881 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isCaredFor() {
/* 20886 */     if (isUnique() || onlyAttacksPlayers())
/* 20887 */       return false; 
/* 20888 */     return Creatures.getInstance().isCreatureProtected(getWurmId());
/*       */   }
/*       */ 
/*       */   
/*       */   public long getCareTakerId() {
/* 20893 */     return Creatures.getInstance().getCreatureProtectorFor(getWurmId());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isCaredFor(Player player) {
/* 20898 */     return (getCareTakerId() == player.getWurmId());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isBrandedBy(int villageId) {
/* 20903 */     Village bVill = getBrandVillage();
/* 20904 */     return (bVill != null && bVill.getId() == villageId);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isBranded() {
/* 20909 */     Village bVill = getBrandVillage();
/* 20910 */     return (bVill != null);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOnDeed() {
/* 20916 */     Village bVill = getBrandVillage();
/* 20917 */     if (bVill == null)
/* 20918 */       return false; 
/* 20919 */     Village pVill = Villages.getVillage(getTileX(), getTileY(), true);
/* 20920 */     return (pVill != null && bVill.getId() == pVill.getId());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isHitched() {
/* 20925 */     return (getHitched() != null);
/*       */   }
/*       */ 
/*       */   
/*       */   public int getNumberOfPossibleCreatureTakenCareOf() {
/* 20930 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setHasSpiritStamina(boolean hasStaminaGain) {
/* 20935 */     this.hasSpiritStamina = hasStaminaGain;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHasSpiritFavorgain(boolean hasFavorGain) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHasSpiritFervor(boolean hasSpiritFervor) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mayUseLastGasp() {
/* 20952 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void useLastGasp() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isUsingLastGasp() {
/* 20963 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final float addToWeaponUsed(Item weapon, float time) {
/* 20975 */     Float ftime = this.weaponsUsed.get(weapon);
/* 20976 */     if (ftime == null) {
/* 20977 */       ftime = Float.valueOf(time);
/*       */     } else {
/* 20979 */       ftime = Float.valueOf(ftime.floatValue() + time);
/* 20980 */     }  this.weaponsUsed.put(weapon, ftime);
/* 20981 */     return ftime.floatValue();
/*       */   }
/*       */ 
/*       */   
/*       */   public final UsedAttackData addToAttackUsed(AttackAction act, float time, int rounds) {
/* 20986 */     UsedAttackData data = this.attackUsed.get(act);
/* 20987 */     if (data == null) {
/* 20988 */       data = new UsedAttackData(time, rounds);
/*       */     } else {
/*       */       
/* 20991 */       data.setTime(data.getTime() + time);
/* 20992 */       data.setRounds(data.getRounds() + rounds);
/*       */     } 
/* 20994 */     this.attackUsed.put(act, data);
/* 20995 */     return data;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void updateAttacksUsed(float time) {
/* 21000 */     for (AttackAction key : this.attackUsed.keySet()) {
/*       */       
/* 21002 */       UsedAttackData data = this.attackUsed.get(key);
/* 21003 */       if (data != null)
/*       */       {
/* 21005 */         data.update(data.getTime() - time);
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final UsedAttackData getUsedAttackData(AttackAction act) {
/* 21012 */     return this.attackUsed.get(act);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final float deductFromWeaponUsed(Item weapon, float swingTime) {
/* 21025 */     Float ftime = this.weaponsUsed.get(weapon);
/* 21026 */     if (ftime == null) {
/* 21027 */       ftime = Float.valueOf(swingTime);
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 21034 */     while (ftime.floatValue() >= swingTime)
/*       */     {
/* 21036 */       ftime = Float.valueOf(ftime.floatValue() - swingTime);
/*       */     }
/* 21038 */     this.weaponsUsed.put(weapon, ftime);
/* 21039 */     return ftime.floatValue();
/*       */   }
/*       */ 
/*       */   
/*       */   public final void resetWeaponsUsed() {
/* 21044 */     this.weaponsUsed.clear();
/*       */   }
/*       */ 
/*       */   
/*       */   public final void resetAttackUsed() {
/* 21049 */     this.attackUsed.clear();
/* 21050 */     if (this.combatHandler != null)
/*       */     {
/* 21052 */       this.combatHandler.resetSecAttacks();
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getFightlevel() {
/* 21064 */     if (this.fightlevel < 0)
/*       */     {
/* 21066 */       this.fightlevel = 0;
/*       */     }
/* 21068 */     if (this.fightlevel > 5)
/*       */     {
/* 21070 */       this.fightlevel = 5;
/*       */     }
/* 21072 */     return this.fightlevel;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getFightlevelString() {
/* 21084 */     int fl = getFightlevel();
/* 21085 */     if (fl < 0)
/* 21086 */       fl = 0; 
/* 21087 */     if (fl >= 5)
/* 21088 */       fl = 5; 
/* 21089 */     return Attack.focusStrings[fl];
/*       */   }
/*       */ 
/*       */   
/*       */   public void increaseFightlevel(int delta) {
/* 21094 */     this.fightlevel = (byte)(this.fightlevel + delta);
/*       */     
/* 21096 */     if (this.fightlevel > 5)
/*       */     {
/* 21098 */       this.fightlevel = 5;
/*       */     }
/*       */     
/* 21101 */     if (this.fightlevel < 0)
/*       */     {
/* 21103 */       this.fightlevel = 0;
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setKickedOffBoat(boolean kicked) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean wasKickedOffBoat() {
/* 21124 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isOnPermaReputationGrounds() {
/* 21129 */     if (this.currentVillage != null)
/*       */     {
/* 21131 */       if (this.currentVillage.getReputationObject(getWurmId()) != null)
/* 21132 */         return this.currentVillage.getReputationObject(getWurmId()).isPermanent(); 
/*       */     }
/* 21134 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasFingerEffect() {
/* 21149 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHasFingerEffect(boolean eff) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendHasFingerEffect() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasFingerOfFoBonus() {
/* 21164 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHasCrownEffect(boolean eff) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendHasCrownEffect() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public void setCrownInfluence(int influence) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasCrownInfluence() {
/* 21184 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getEpicServerId() {
/* 21189 */     return -1;
/*       */   }
/*       */ 
/*       */   
/*       */   public byte getEpicServerKingdom() {
/* 21194 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean attackingIntoIllegalDuellingRing(int targetX, int targetY, boolean surfaced) {
/* 21199 */     if (surfaced) {
/*       */       
/* 21201 */       Item ring1 = Zones.isWithinDuelRing(getCurrentTile().getTileX(), getCurrentTile().getTileY(), 
/* 21202 */           getCurrentTile().isOnSurface());
/*       */       
/* 21204 */       Item ring = Zones.isWithinDuelRing(targetX, targetY, surfaced);
/* 21205 */       if (ring != ring1)
/*       */       {
/* 21207 */         return true;
/*       */       }
/*       */     } 
/* 21210 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasSpellEffect(byte spellEffect) {
/* 21215 */     if (getSpellEffects() != null)
/*       */     {
/* 21217 */       return (getSpellEffects().getSpellEffect(spellEffect) != null);
/*       */     }
/* 21219 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void reduceStoneSkin() {
/* 21224 */     if (getSpellEffects() != null) {
/*       */       
/* 21226 */       SpellEffect sk = getSpellEffects().getSpellEffect((byte)68);
/* 21227 */       if (sk != null)
/*       */       {
/* 21229 */         if (sk.getPower() > 34.0F) {
/* 21230 */           sk.setPower(sk.getPower() - 34.0F);
/*       */         } else {
/* 21232 */           getSpellEffects().removeSpellEffect(sk);
/*       */         } 
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   public final void removeTrueStrike() {
/* 21239 */     if (getSpellEffects() != null) {
/*       */       
/* 21241 */       SpellEffect sk = getSpellEffects().getSpellEffect((byte)67);
/* 21242 */       if (sk != null)
/*       */       {
/* 21244 */         getSpellEffects().removeSpellEffect(sk);
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean addWoundOfType(@Nullable Creature attacker, byte woundType, int pos, boolean randomizePos, float armourMod, boolean calculateArmour, double damage, float infection, float poison, boolean noMinimumDamage, boolean spell) {
/* 21254 */     if (woundType == 8 || woundType == 4 || woundType == 10)
/*       */     {
/* 21256 */       if (getCultist() != null && getCultist().hasNoElementalDamage())
/* 21257 */         return false; 
/*       */     }
/* 21259 */     if (hasSpellEffect((byte)69))
/*       */     {
/* 21261 */       damage *= 0.800000011920929D;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 21266 */       if (randomizePos)
/* 21267 */         pos = getBody().getRandomWoundPos(); 
/* 21268 */       if (calculateArmour) {
/*       */         
/* 21270 */         armourMod = getArmourMod();
/* 21271 */         if (armourMod == 1.0F || isVehicle() || isKingdomGuard()) {
/*       */           
/*       */           try {
/*       */             
/* 21275 */             byte protectionSlot = ArmourTemplate.getArmourPosition((byte)pos);
/* 21276 */             Item armour = getArmour(protectionSlot);
/* 21277 */             if (!isKingdomGuard()) {
/* 21278 */               armourMod = ArmourTemplate.calculateDR(armour, woundType);
/*       */             
/*       */             }
/*       */             else {
/*       */               
/* 21283 */               armourMod *= ArmourTemplate.calculateDR(armour, woundType);
/*       */             } 
/*       */ 
/*       */             
/* 21287 */             armour.setDamage(
/*       */                 
/* 21289 */                 (float)(armour.getDamage() + damage * armourMod / 30000.0D * armour.getDamageModifier() * ArmourTemplate.getArmourDamageModFor(armour, woundType)));
/* 21290 */             if (getBonusForSpellEffect((byte)22) > 0.0F)
/*       */             {
/* 21292 */               if (armourMod >= 1.0F) {
/* 21293 */                 armourMod = 0.2F + (1.0F - getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F;
/*       */               } else {
/*       */                 
/* 21296 */                 armourMod = Math.min(armourMod, 0.2F + (1.0F - 
/* 21297 */                     getBonusForSpellEffect((byte)22) / 100.0F) * 0.6F);
/*       */               } 
/*       */             }
/* 21300 */           } catch (NoArmourException noArmourException) {}
/*       */         }
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/* 21306 */       if (pos == 1 || pos == 29)
/* 21307 */         damage *= ItemBonus.getFaceDamReductionBonus(this); 
/* 21308 */       damage *= Wound.getResistModifier(attacker, this, woundType);
/* 21309 */       if (woundType == 8)
/* 21310 */         return CombatEngine.addColdWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21311 */       if (woundType == 7)
/* 21312 */         return CombatEngine.addDrownWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21313 */       if (woundType == 9)
/* 21314 */         return CombatEngine.addInternalWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21315 */       if (woundType == 10)
/* 21316 */         return CombatEngine.addAcidWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21317 */       if (woundType == 4)
/* 21318 */         return CombatEngine.addFireWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21319 */       if (woundType == 6)
/* 21320 */         return CombatEngine.addRotWound(attacker, this, pos, damage, armourMod, infection, poison, noMinimumDamage, spell); 
/* 21321 */       if (woundType == 5) {
/* 21322 */         return CombatEngine.addWound(attacker, this, woundType, pos, damage, armourMod, "poison", null, infection, poison, false, true, noMinimumDamage, spell);
/*       */       }
/* 21324 */       return CombatEngine.addWound(attacker, this, woundType, pos, damage, armourMod, "hit", null, infection, poison, false, true, noMinimumDamage, spell);
/*       */     
/*       */     }
/* 21327 */     catch (NoSpaceException nsp) {
/*       */       
/* 21329 */       logger.log(Level.WARNING, getName() + " no armour space on loc " + pos);
/*       */     }
/* 21331 */     catch (Exception e) {
/*       */       
/* 21333 */       logger.log(Level.WARNING, e.getMessage(), e);
/*       */     } 
/* 21335 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float addSpellResistance(short spellId) {
/* 21349 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public SpellResistance getSpellResistance(short spellId) {
/* 21355 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInPvPZone() {
/* 21360 */     if (this.isInNonPvPZone)
/* 21361 */       return false; 
/* 21362 */     return this.isInPvPZone;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOnPvPServer() {
/* 21367 */     if (this.isInNonPvPZone)
/* 21368 */       return false; 
/* 21369 */     if (Servers.localServer.PVPSERVER)
/* 21370 */       return true; 
/* 21371 */     if (this.isInPvPZone)
/* 21372 */       return true; 
/* 21373 */     if (this.isInDuelRing)
/* 21374 */       return true; 
/* 21375 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public short getHotaWins() {
/* 21380 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHotaWins(short wins) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public void setVehicleCommander(boolean isCommander) {
/* 21390 */     this.isVehicleCommander = isCommander;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getFace() {
/* 21395 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getRarity() {
/* 21405 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getRarityShader() {
/* 21415 */     if (getBonusForSpellEffect((byte)22) > 70.0F)
/* 21416 */       return 2; 
/* 21417 */     if (getBonusForSpellEffect((byte)22) > 0.0F) {
/* 21418 */       return 1;
/*       */     }
/* 21420 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void achievement(int achievementId) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addTitle(Titles.Title title) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeTitle(Titles.Title title) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void achievement(int achievementId, int counterModifier) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected void addTileMovedDragging() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected void addTileMovedRiding() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected void addTileMoved() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected void addTileMovedDriving() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected void addTileMovedPassenger() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getKarma() {
/* 21507 */     if (isSpellCaster() || isSummoner())
/* 21508 */       return 10000; 
/* 21509 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setKarma(int newKarma) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public void modifyKarma(int points) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public long getTimeToSummonCorpse() {
/* 21524 */     return 0L;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean maySummonCorpse() {
/* 21529 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void pushToFloorLevel(int floorLevel) {
/*       */     try {
/* 21536 */       if (!isPlayer())
/*       */       {
/* 21538 */         float oldposz = getPositionZ();
/* 21539 */         float newPosz = Zones.calculateHeight(getPosX(), getPosY(), isOnSurface()) + (floorLevel * 3) + 0.25F;
/*       */         
/* 21541 */         float diffz = newPosz - oldposz;
/* 21542 */         getStatus().setPositionZ(newPosz, true);
/* 21543 */         if (this.currentTile != null && getVisionArea() != null) {
/* 21544 */           moved(0.0F, 0.0F, diffz, 0, 0);
/*       */         
/*       */         }
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 21551 */     catch (NoSuchZoneException noSuchZoneException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final float calculatePosZ() {
/* 21559 */     return Zones.calculatePosZ(getPosX(), getPosY(), getCurrentTile(), isOnSurface(), isFloating(), getPositionZ(), this, 
/* 21560 */         getBridgeId());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canOpenDoors() {
/* 21565 */     return this.template.canOpenDoors();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getFloorLevel(boolean ignoreVehicleOffset) {
/*       */     try {
/* 21575 */       float vehicleOffsetToRemove = 0.0F;
/* 21576 */       if (ignoreVehicleOffset) {
/*       */ 
/*       */ 
/*       */         
/* 21580 */         long vehicleId = getVehicle();
/* 21581 */         if (vehicleId != -10L) {
/*       */ 
/*       */           
/* 21584 */           Vehicle vehicle = Vehicles.getVehicleForId(vehicleId);
/* 21585 */           if (vehicle == null) {
/*       */ 
/*       */ 
/*       */             
/* 21589 */             logger.log(Level.WARNING, "Unknown vehicle for id: " + vehicleId + " resulting in possinly incorrect floor level!");
/*       */           
/*       */           }
/*       */           else {
/*       */ 
/*       */             
/* 21595 */             Seat seat = vehicle.getSeatFor(this.id);
/* 21596 */             if (seat == null) {
/*       */ 
/*       */               
/* 21599 */               logger.log(Level.WARNING, "Unable to find the seat the player: " + this.id + " supposedly is on, Vehicle id: " + vehicleId + ". Resulting in possibly incorrect floor level calculation.");
/*       */ 
/*       */             
/*       */             }
/*       */             else {
/*       */ 
/*       */               
/* 21606 */               vehicleOffsetToRemove = Math.max(getAltOffZ(), seat.offz);
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } 
/* 21611 */       float playerPosZ = getPositionZ() + getAltOffZ();
/* 21612 */       float groundHeight = Math.max(0.0F, Zones.calculateHeight(getPosX(), getPosY(), isOnSurface()));
/* 21613 */       float posZ = Math.max(0.0F, (playerPosZ - groundHeight - vehicleOffsetToRemove + 0.5F) * 10.0F);
/*       */ 
/*       */       
/* 21616 */       return (int)posZ / 30;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*       */     }
/* 21624 */     catch (NoSuchZoneException snz) {
/*       */       
/* 21626 */       return 0;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getFloorLevel() {
/* 21632 */     return getFloorLevel(false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean fireTileLog() {
/* 21640 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendActionControl(String actionString, boolean start, int timeLeft) {}
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getBlood() {
/* 21653 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public Shop getShop() {
/* 21658 */     return Economy.getEconomy().getShop(this);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setScenarioKarma(int newKarma) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public int getScenarioKarma() {
/* 21668 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean knowsKarmaSpell(int karmaSpellActionNum) {
/* 21673 */     if (isSpellCaster() || isSummoner())
/* 21674 */       return true; 
/* 21675 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getFireResistance() {
/* 21680 */     return this.template.fireResistance;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean checkCoinAward(int chance) {
/* 21689 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getColdResistance() {
/* 21694 */     return this.template.coldResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getDiseaseResistance() {
/* 21699 */     return this.template.diseaseResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPhysicalResistance() {
/* 21704 */     return this.template.physicalResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPierceResistance() {
/* 21709 */     return this.template.pierceResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getSlashResistance() {
/* 21714 */     return this.template.slashResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getCrushResistance() {
/* 21719 */     return this.template.crushResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getBiteResistance() {
/* 21724 */     return this.template.biteResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPoisonResistance() {
/* 21729 */     return this.template.poisonResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getWaterResistance() {
/* 21734 */     return this.template.waterResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getAcidResistance() {
/* 21739 */     return this.template.acidResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getInternalResistance() {
/* 21744 */     return this.template.internalResistance;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getFireVulnerability() {
/* 21749 */     return this.template.fireVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getColdVulnerability() {
/* 21754 */     return this.template.coldVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getDiseaseVulnerability() {
/* 21759 */     return this.template.diseaseVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPhysicalVulnerability() {
/* 21764 */     return this.template.physicalVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPierceVulnerability() {
/* 21769 */     return this.template.pierceVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getSlashVulnerability() {
/* 21774 */     return this.template.slashVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getCrushVulnerability() {
/* 21779 */     return this.template.crushVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getBiteVulnerability() {
/* 21784 */     return this.template.biteVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getPoisonVulnerability() {
/* 21789 */     return this.template.poisonVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getWaterVulnerability() {
/* 21794 */     return this.template.waterVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getAcidVulnerability() {
/* 21799 */     return this.template.acidVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public float getInternalVulnerability() {
/* 21804 */     return this.template.internalVulnerability;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasAnyAbility() {
/* 21809 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final Set<MovementEntity> getIllusionsFor(long wurmid) {
/* 21814 */     return illusions.get(Long.valueOf(wurmid));
/*       */   }
/*       */ 
/*       */   
/*       */   public static final long getWurmIdForIllusion(long illusionId) {
/* 21819 */     for (Set<MovementEntity> set : illusions.values()) {
/*       */       
/* 21821 */       for (MovementEntity entity : set) {
/*       */         
/* 21823 */         if (entity.getWurmid() == illusionId)
/* 21824 */           return entity.getCreatorId(); 
/*       */       } 
/*       */     } 
/* 21827 */     return -10L;
/*       */   }
/*       */ 
/*       */   
/*       */   public void addIllusion(MovementEntity entity) {
/* 21832 */     Set<MovementEntity> entities = illusions.get(Long.valueOf(getWurmId()));
/* 21833 */     if (entities == null) {
/*       */       
/* 21835 */       entities = new HashSet<>();
/* 21836 */       illusions.put(Long.valueOf(getWurmId()), entities);
/*       */     } 
/* 21838 */     entities.add(entity);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isUndead() {
/* 21843 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public byte getUndeadType() {
/* 21848 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public String getUndeadTitle() {
/* 21853 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setBridgeId(long bid) {
/* 21862 */     setBridgeId(bid, true);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setBridgeId(long bid, boolean sendToSelf) {
/* 21867 */     this.status.getPosition().setBridgeId(bid);
/* 21868 */     if (getMovementScheme() != null)
/* 21869 */       getMovementScheme().setBridgeId(bid); 
/* 21870 */     if (getCurrentTile() != null) {
/* 21871 */       getCurrentTile().sendSetBridgeId(this, bid, sendToSelf);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public long getMoneyEarnedBySellingLastHour() {
/* 21882 */     return 0L;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addMoneyEarnedBySellingLastHour(long money) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setModelName(String newModelName) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void calcBattleCampBonus() {
/* 21903 */     Item closest = null;
/* 21904 */     for (FocusZone fz : FocusZone.getZonesAt(getTileX(), getTileY())) {
/*       */       
/* 21906 */       if (fz.isBattleCamp())
/*       */       {
/* 21908 */         for (Item wartarget : Items.getWarTargets()) {
/*       */           
/* 21910 */           if (closest == null || 
/* 21911 */             getRange(this, wartarget.getPosX(), wartarget.getPosY()) < getRange(this, closest.getPosX(), closest
/* 21912 */               .getPosY()))
/* 21913 */             closest = wartarget; 
/*       */         } 
/*       */       }
/*       */     } 
/* 21917 */     if (closest != null)
/* 21918 */       this.isInOwnBattleCamp = (closest.getKingdom() == getKingdomId()); 
/* 21919 */     this.isInOwnBattleCamp = false;
/* 21920 */     logger.log(Level.INFO, getName() + " set battle camp bonus to " + this.isInOwnBattleCamp);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasBattleCampBonus() {
/* 21925 */     return this.isInOwnBattleCamp;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isVisibleToPlayers() {
/* 21935 */     return this.visibleToPlayers;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setVisibleToPlayers(boolean aVisibleToPlayers) {
/* 21946 */     this.visibleToPlayers = aVisibleToPlayers;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isDoLavaDamage() {
/* 21956 */     return this.doLavaDamage;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setDoLavaDamage(boolean aDoLavaDamage) {
/* 21967 */     this.doLavaDamage = aDoLavaDamage;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean doLavaDamage() {
/* 21972 */     setDoLavaDamage(false);
/* 21973 */     if (!isInvulnerable() && !isGhost() && !isUnique())
/*       */     {
/* 21975 */       if (getDeity() == null || !getDeity().isMountainGod() || getFaith() < 35.0F)
/*       */       {
/* 21977 */         if (getFarwalkerSeconds() <= 0) {
/*       */           
/* 21979 */           Wound wound = null;
/* 21980 */           boolean dead = false;
/*       */           
/*       */           try {
/* 21983 */             byte pos = getBody().getRandomWoundPos((byte)10);
/* 21984 */             if (Server.rand.nextInt(10) <= 6)
/*       */             {
/* 21986 */               if (getBody().getWounds() != null) {
/*       */                 
/* 21988 */                 wound = getBody().getWounds().getWoundAtLocation(pos);
/* 21989 */                 if (wound != null) {
/*       */                   
/* 21991 */                   dead = wound.modifySeverity(
/* 21992 */                       (int)(5000.0F + Server.rand.nextInt(5000) * (100.0F - getSpellDamageProtectBonus()) / 100.0F));
/* 21993 */                   wound.setBandaged(false);
/* 21994 */                   setWounded();
/*       */                 } 
/*       */               } 
/*       */             }
/* 21998 */             if (wound == null)
/*       */             {
/* 22000 */               if (!isGhost() && !isUnique() && 
/* 22001 */                 !isKingdomGuard())
/*       */               {
/* 22003 */                 dead = addWoundOfType(null, (byte)4, pos, false, 1.0F, true, (5000.0F + Server.rand
/* 22004 */                     .nextInt(5000) * (100.0F - 
/* 22005 */                     getSpellDamageProtectBonus()) / 100.0F), 0.0F, 0.0F, false, false);
/*       */               }
/*       */             }
/* 22008 */             getCommunicator().sendAlertServerMessage("You are burnt by lava!");
/* 22009 */             if (dead)
/*       */             {
/* 22011 */               achievement(142);
/* 22012 */               return true;
/*       */             }
/*       */           
/* 22015 */           } catch (Exception ex) {
/*       */             
/* 22017 */             logger.log(Level.WARNING, getName() + " " + ex.getMessage(), ex);
/*       */           } 
/*       */         } 
/*       */       }
/*       */     }
/* 22022 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isDoAreaDamage() {
/* 22032 */     return this.doAreaDamage;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setDoAreaEffect(boolean aDoAreaDamage) {
/* 22043 */     this.doAreaDamage = aDoAreaDamage;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getPathfindCounter() {
/* 22053 */     return this.pathfindcounter;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setPathfindcounter(int i) {
/* 22066 */     this.pathfindcounter = (byte)i;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getHugeMoveCounter() {
/* 22076 */     return this.hugeMoveCounter;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHugeMoveCounter(int aHugeMoveCounter) {
/* 22087 */     this.hugeMoveCounter = Math.max(0, aHugeMoveCounter);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setArmourLimitingFactor(float factor, boolean initializing) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public float getArmourLimitingFactor() {
/* 22098 */     return 0.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void recalcLimitingFactor(Item currentItem) {}
/*       */ 
/*       */ 
/*       */   
/*       */   public final float getAltOffZ() {
/* 22108 */     if (getVehicle() != -10L) {
/*       */       
/* 22110 */       Vehicle vehic = Vehicles.getVehicleForId(getVehicle());
/* 22111 */       if (vehic != null) {
/*       */         
/* 22113 */         Seat s = vehic.getSeatFor(getWurmId());
/* 22114 */         if (s != null)
/* 22115 */           return s.getAltOffz(); 
/*       */       } 
/*       */     } 
/* 22118 */     return 0.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean followsGround() {
/* 22123 */     return (getBridgeId() == -10L && (!isPlayer() || (getMovementScheme()).onGround) && getFloorLevel() == 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWagoner() {
/* 22128 */     return (this.template.getTemplateId() == 114);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFish() {
/* 22133 */     return (this.template.getTemplateId() == 119);
/*       */   }
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public final Wagoner getWagoner() {
/* 22139 */     if (isWagoner())
/* 22140 */       return Wagoner.getWagoner(this.id); 
/* 22141 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getTypeName() {
/* 22152 */     return getTemplate().getName();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getObjectName() {
/* 22163 */     if (isWagoner())
/* 22164 */       return getName(); 
/* 22165 */     return this.petName;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setObjectName(String aNewName, Creature aCreature) {
/* 22177 */     setVisible(false);
/* 22178 */     setPetName(aNewName);
/* 22179 */     setVisible(true);
/* 22180 */     this.status.setChanged(true);
/* 22181 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isActualOwner(long playerId) {
/* 22192 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOwner(Creature creature) {
/* 22203 */     return isOwner(creature.getWurmId());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOwner(long playerId) {
/* 22214 */     if (isWagoner()) {
/*       */       
/* 22216 */       Wagoner wagoner = getWagoner();
/* 22217 */       if (wagoner != null)
/* 22218 */         return (wagoner.getOwnerId() == playerId); 
/* 22219 */       return false;
/*       */     } 
/* 22221 */     Village bVill = getBrandVillage();
/* 22222 */     return (bVill != null && bVill.isMayor(playerId));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canChangeOwner(Creature creature) {
/* 22233 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canChangeName(Creature creature) {
/* 22244 */     if (isWagoner())
/* 22245 */       return false; 
/* 22246 */     if (creature.getPower() > 1) {
/* 22247 */       return true;
/*       */     }
/* 22249 */     Village bVill = getBrandVillage();
/* 22250 */     if (bVill == null)
/* 22251 */       return false; 
/* 22252 */     return bVill.isMayor(creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setNewOwner(long playerId) {
/* 22264 */     if (isWagoner()) {
/*       */       
/* 22266 */       Wagoner wagoner = getWagoner();
/* 22267 */       if (wagoner != null) {
/*       */         
/* 22269 */         wagoner.setOwnerId(playerId);
/* 22270 */         return true;
/*       */       } 
/* 22272 */       return false;
/*       */     } 
/*       */     
/* 22275 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getOwnerName() {
/* 22307 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getWarning() {
/* 22318 */     if (isWagoner()) {
/* 22319 */       return "";
/*       */     }
/* 22321 */     Village bVill = getBrandVillage();
/* 22322 */     if (bVill == null)
/*       */     {
/* 22324 */       return "NEEDS TO BE BRANDED FOR PERMISSIONS TO WORK";
/*       */     }
/* 22326 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public PermissionsPlayerList getPermissionsPlayerList() {
/* 22337 */     return AnimalSettings.getPermissionsPlayerList(getWurmId());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isManaged() {
/* 22348 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isManageEnabled(Player player) {
/* 22360 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsManaged(boolean newIsManaged, Player player) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String mayManageText(Player player) {
/* 22382 */     if (isWagoner())
/* 22383 */       return ""; 
/* 22384 */     Village bVill = getBrandVillage();
/* 22385 */     if (bVill != null)
/* 22386 */       return "Settlement \"" + bVill.getName() + "\" may manage"; 
/* 22387 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String mayManageHover(Player aPlayer) {
/* 22398 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String messageOnTick() {
/* 22409 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String questionOnTick() {
/* 22420 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String messageUnTick() {
/* 22431 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String questionUnTick() {
/* 22442 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getSettlementName() {
/* 22453 */     String sName = "";
/*       */     
/* 22455 */     Village bVill = isWagoner() ? this.citizenVillage : getBrandVillage();
/* 22456 */     if (bVill != null)
/* 22457 */       sName = bVill.getName(); 
/* 22458 */     if (sName.length() > 0)
/* 22459 */       return "Citizens of \"" + sName + "\""; 
/* 22460 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getAllianceName() {
/* 22471 */     String aName = "";
/* 22472 */     Village bVill = isWagoner() ? this.citizenVillage : getBrandVillage();
/* 22473 */     if (bVill != null)
/* 22474 */       aName = bVill.getAllianceName(); 
/* 22475 */     if (aName.length() > 0)
/* 22476 */       return "Alliance of \"" + aName + "\""; 
/* 22477 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getKingdomName() {
/* 22488 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canAllowEveryone() {
/* 22499 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getRolePermissionName() {
/* 22511 */     Village bVill = getBrandVillage();
/* 22512 */     if (bVill != null)
/* 22513 */       return "Brand Permission of \"" + bVill.getName() + "\""; 
/* 22514 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isCitizen(Creature creature) {
/* 22525 */     Village bVill = isWagoner() ? this.citizenVillage : getBrandVillage();
/* 22526 */     if (bVill != null)
/* 22527 */       return bVill.isCitizen(creature); 
/* 22528 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isAllied(Creature creature) {
/* 22539 */     Village bVill = isWagoner() ? this.citizenVillage : getBrandVillage();
/* 22540 */     if (bVill != null)
/* 22541 */       return bVill.isAlly(creature); 
/* 22542 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isSameKingdom(Creature creature) {
/* 22554 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addGuest(long guestId, int aSettings) {
/* 22565 */     AnimalSettings.addPlayer(getWurmId(), guestId, aSettings);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeGuest(long guestId) {
/* 22576 */     AnimalSettings.removePlayer(getWurmId(), guestId);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addDefaultCitizenPermissions() {
/* 22587 */     if (!getPermissionsPlayerList().exists(-30L)) {
/*       */       
/* 22589 */       int value = AnimalSettings.Animal1Permissions.COMMANDER.getValue();
/* 22590 */       addNewGuest(-30L, value);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isGuest(Creature creature) {
/* 22602 */     return isGuest(creature.getWurmId());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isGuest(long playerId) {
/* 22614 */     return AnimalSettings.isGuest(this, playerId);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMaxAllowed() {
/* 22625 */     return AnimalSettings.getMaxAllowed();
/*       */   }
/*       */ 
/*       */   
/*       */   public void addNewGuest(long guestId, int aSettings) {
/* 22630 */     AnimalSettings.addPlayer(getWurmId(), guestId, aSettings);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Village getBrandVillage() {
/* 22641 */     Brand brand = Creatures.getInstance().getBrand(getWurmId());
/* 22642 */     if (brand != null) {
/*       */       
/*       */       try {
/*       */         
/* 22646 */         Village villageBrand = Villages.getVillage((int)brand.getBrandId());
/*       */         
/* 22648 */         return villageBrand;
/*       */       }
/* 22650 */       catch (NoSuchVillageException nsv) {
/*       */         
/* 22652 */         brand.deleteBrand();
/*       */       } 
/*       */     }
/* 22655 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canHavePermissions() {
/* 22667 */     if (isWagoner() && Features.Feature.WAGONER.isEnabled())
/* 22668 */       return true; 
/* 22669 */     return (getBrandVillage() != null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayLead(Creature creature) {
/* 22679 */     if (mayCommand(creature))
/* 22680 */       return true; 
/* 22681 */     if (AnimalSettings.isExcluded(this, creature)) {
/* 22682 */       return false;
/*       */     }
/* 22684 */     Village bvill = getBrandVillage();
/* 22685 */     if (bvill != null) {
/*       */       
/* 22687 */       VillageRole vr = bvill.getRoleFor(creature);
/* 22688 */       return vr.mayLead();
/*       */     } 
/*       */     
/* 22691 */     Village cvill = getCurrentVillage();
/* 22692 */     if (cvill != null) {
/*       */       
/* 22694 */       VillageRole vr = cvill.getRoleFor(creature);
/* 22695 */       return vr.mayLead();
/*       */     } 
/*       */     
/* 22698 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayShowPermissions(Creature creature) {
/* 22710 */     return (canHavePermissions() && mayManage(creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canManage(Creature creature) {
/* 22721 */     if (isWagoner()) {
/*       */ 
/*       */       
/* 22724 */       Wagoner wagoner = getWagoner();
/* 22725 */       if (wagoner != null) {
/*       */         
/* 22727 */         if (wagoner.getOwnerId() == creature.getWurmId()) {
/* 22728 */           return true;
/*       */         }
/*       */         
/* 22731 */         if (creature.getCitizenVillage() != null && creature.getCitizenVillage() == this.citizenVillage && creature
/* 22732 */           .getCitizenVillage().isMayor(creature))
/*       */         {
/* 22734 */           return true;
/*       */         }
/*       */       } 
/*       */     } 
/* 22738 */     if (AnimalSettings.isExcluded(this, creature)) {
/* 22739 */       return false;
/*       */     }
/* 22741 */     Village vill = getBrandVillage();
/* 22742 */     if (AnimalSettings.canManage(this, creature, vill))
/* 22743 */       return true; 
/* 22744 */     if (creature.getCitizenVillage() == null) {
/* 22745 */       return false;
/*       */     }
/* 22747 */     if (vill == null)
/* 22748 */       return false; 
/* 22749 */     if (!vill.isCitizen(creature))
/* 22750 */       return false; 
/* 22751 */     return vill.isActionAllowed((short)663, creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayManage(Creature creature) {
/* 22761 */     if (creature.getPower() > 1 && !isPlayer())
/* 22762 */       return true; 
/* 22763 */     return canManage(creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean maySeeHistory(Creature creature) {
/* 22773 */     if (isWagoner()) {
/*       */ 
/*       */       
/* 22776 */       Wagoner wagoner = getWagoner();
/* 22777 */       if (wagoner != null) {
/*       */         
/* 22779 */         if (wagoner.getOwnerId() == creature.getWurmId()) {
/* 22780 */           return true;
/*       */         }
/* 22782 */         if (creature.getCitizenVillage() != null && creature.getCitizenVillage() == this.citizenVillage && creature
/* 22783 */           .getCitizenVillage().isMayor(creature))
/*       */         {
/* 22785 */           return true;
/*       */         }
/*       */       } 
/*       */     } 
/* 22789 */     if (creature.getPower() > 1 && !isPlayer())
/* 22790 */       return true; 
/* 22791 */     Village bVill = getBrandVillage();
/* 22792 */     return (bVill != null && bVill.isMayor(creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayCommand(Creature creature) {
/* 22802 */     if (AnimalSettings.isExcluded(this, creature))
/* 22803 */       return false; 
/* 22804 */     return AnimalSettings.mayCommand(this, creature, getBrandVillage());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayPassenger(Creature creature) {
/* 22814 */     if (AnimalSettings.isExcluded(this, creature))
/* 22815 */       return false; 
/* 22816 */     return AnimalSettings.mayPassenger(this, creature, getBrandVillage());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayAccessHold(Creature creature) {
/* 22826 */     if (AnimalSettings.isExcluded(this, creature))
/* 22827 */       return false; 
/* 22828 */     return AnimalSettings.mayAccessHold(this, creature, getBrandVillage());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayUse(Creature creature) {
/* 22839 */     if (AnimalSettings.isExcluded(this, creature))
/* 22840 */       return false; 
/* 22841 */     return AnimalSettings.mayUse(this, creature, getBrandVillage());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean publicMayUse(Creature creature) {
/* 22851 */     if (AnimalSettings.isExcluded(this, creature))
/* 22852 */       return false; 
/* 22853 */     return AnimalSettings.publicMayUse(this);
/*       */   }
/*       */ 
/*       */   
/*       */   public ServerEntry getDestination() {
/* 22858 */     return this.destination;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setDestination(ServerEntry destination) {
/* 22863 */     if (destination != null && !destination.isChallengeOrEpicServer() && !destination.LOGINSERVER && destination != Servers.localServer)
/*       */     {
/* 22865 */       this.destination = destination;
/*       */     }
/*       */   }
/*       */   
/*       */   public void clearDestination() {
/* 22870 */     this.destination = null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getVillageId() {
/* 22879 */     if (getCitizenVillage() != null)
/* 22880 */       return getCitizenVillage().getId(); 
/* 22881 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private static Item getRareRecipe(String sig, int commonRecipeId, int rareRecipeId, int supremeRecipeId, int fantasticRecipeId) {
/* 22887 */     int rno = Server.rand.nextInt(Servers.isThisATestServer() ? 100 : 1000);
/* 22888 */     if (rno < 100) {
/*       */       
/* 22890 */       int recipeId = -10;
/* 22891 */       if (rno == 0 && fantasticRecipeId != -10) {
/* 22892 */         recipeId = fantasticRecipeId;
/* 22893 */       } else if (rno < 6 && supremeRecipeId != -10) {
/* 22894 */         recipeId = supremeRecipeId;
/* 22895 */       } else if (rno < 31 && rareRecipeId != -10) {
/* 22896 */         recipeId = rareRecipeId;
/* 22897 */       } else if (rno >= 50 && commonRecipeId != -10) {
/* 22898 */         recipeId = commonRecipeId;
/*       */       } 
/* 22900 */       if (recipeId == -10)
/* 22901 */         return null; 
/* 22902 */       Recipe recipe = Recipes.getRecipeById((short)recipeId);
/* 22903 */       if (recipe == null) {
/* 22904 */         return null;
/*       */       }
/* 22906 */       int pp = Server.rand.nextBoolean() ? 1272 : 748;
/* 22907 */       int itq = 20 + Server.rand.nextInt(50);
/*       */       
/*       */       try {
/* 22910 */         Item newItem = ItemFactory.createItem(pp, itq, (byte)0, recipe.getLootableRarity(), null);
/*       */         
/* 22912 */         newItem.setInscription(recipe, sig, 1550103);
/* 22913 */         return newItem;
/*       */       }
/* 22915 */       catch (FailedException e) {
/*       */ 
/*       */         
/* 22918 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/* 22920 */       catch (NoSuchTemplateException e) {
/*       */ 
/*       */         
/* 22923 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       } 
/*       */     } 
/* 22926 */     return null;
/*       */   }
/*       */   
/*       */   public short getDamageCounter() {
/* 22930 */     return this.damageCounter;
/*       */   }
/*       */   
/*       */   public void setDamageCounter(short damageCounter) {
/* 22934 */     this.damageCounter = damageCounter;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public List<Route> getHighwayPath() {
/* 22944 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setHighwayPath(String newDestination, List<Route> newPath) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getHighwayPathDestination() {
/* 22965 */     return "";
/*       */   }
/*       */ 
/*       */   
/*       */   public long getLastWaystoneChecked() {
/* 22970 */     return this.lastWaystoneChecked;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setLastWaystoneChecked(long waystone) {
/* 22975 */     this.lastWaystoneChecked = waystone;
/* 22976 */     Wagoner wagoner = getWagoner();
/*       */     
/* 22978 */     if (isWagoner() && wagoner != null)
/*       */     {
/* 22980 */       wagoner.setLastWaystoneId(waystone);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean embarkOn(long wurmId, byte type) {
/*       */     try {
/* 22995 */       Item item = Items.getItem(wurmId);
/* 22996 */       Vehicle vehicle = Vehicles.getVehicle(item);
/* 22997 */       if (vehicle != null) {
/*       */         
/* 22999 */         Seat[] seats = vehicle.getSeats();
/* 23000 */         for (int x = 0; x < seats.length; x++) {
/*       */           
/* 23002 */           if (seats[x].getType() == type && !seats[x].isOccupied())
/*       */           {
/* 23004 */             seats[x].occupy(vehicle, this);
/* 23005 */             if (type == 0)
/* 23006 */               vehicle.pilotId = getWurmId(); 
/* 23007 */             setVehicleCommander((type == 0));
/* 23008 */             MountAction m = new MountAction(null, item, vehicle, x, (type == 0), (vehicle.seats[x]).offz);
/* 23009 */             setMountAction(m);
/* 23010 */             setVehicle(item.getWurmId(), true, type);
/* 23011 */             return true;
/*       */           }
/*       */         
/*       */         } 
/*       */       } 
/* 23016 */     } catch (NoSuchItemException e) {
/*       */ 
/*       */       
/* 23019 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */     } 
/* 23021 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public ArrayList<Effect> getEffects() {
/* 23026 */     return this.effects;
/*       */   }
/*       */ 
/*       */   
/*       */   public void addEffect(Effect e) {
/* 23031 */     if (e == null) {
/*       */       return;
/*       */     }
/* 23034 */     if (this.effects == null) {
/* 23035 */       this.effects = new ArrayList<>();
/*       */     }
/* 23037 */     this.effects.add(e);
/*       */   }
/*       */ 
/*       */   
/*       */   public void removeEffect(Effect e) {
/* 23042 */     if (this.effects == null || e == null) {
/*       */       return;
/*       */     }
/* 23045 */     this.effects.remove(e);
/*       */     
/* 23047 */     if (this.effects.isEmpty()) {
/* 23048 */       this.effects = null;
/*       */     }
/*       */   }
/*       */   
/*       */   public void updateEffects() {
/* 23053 */     if (this.effects == null) {
/*       */       return;
/*       */     }
/* 23056 */     for (Effect e : this.effects) {
/* 23057 */       e.setPosXYZ(getPosX(), getPosY(), getPositionZ(), false);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isPlacingItem() {
/* 23062 */     return this.isPlacingItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setPlacingItem(boolean placingItem) {
/* 23067 */     this.isPlacingItem = placingItem;
/* 23068 */     if (!placingItem) {
/* 23069 */       setPlacementItem(null);
/*       */     }
/*       */   }
/*       */   
/*       */   public void setPlacingItem(boolean placingItem, Item placementItem) {
/* 23074 */     this.isPlacingItem = placingItem;
/* 23075 */     setPlacementItem(placementItem);
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getPlacementItem() {
/* 23080 */     return this.placementItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setPlacementItem(Item placementItem) {
/* 23085 */     this.placementItem = placementItem;
/* 23086 */     if (placementItem == null) {
/* 23087 */       this.pendingPlacement = null;
/*       */     }
/*       */   }
/*       */   
/*       */   public void setPendingPlacement(float xPos, float yPos, float zPos, float rot) {
/* 23092 */     if (this.placementItem != null) {
/* 23093 */       this
/* 23094 */         .pendingPlacement = new float[] { this.placementItem.getPosX(), this.placementItem.getPosY(), this.placementItem.getPosZ(), this.placementItem.getRotation(), xPos, yPos, zPos, (Math.abs(rot - this.placementItem.getRotation()) > 180.0F) ? (rot - 360.0F) : rot };
/*       */     } else {
/* 23096 */       this.pendingPlacement = null;
/*       */     } 
/*       */   }
/*       */   
/*       */   public float[] getPendingPlacement() {
/* 23101 */     return this.pendingPlacement;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean canUseWithEquipment() {
/* 23106 */     for (Item subjectItem : getBody().getContainersAndWornItems()) {
/*       */ 
/*       */       
/* 23109 */       if (subjectItem.isCreatureWearableOnly())
/*       */       {
/* 23111 */         if (subjectItem.isSaddleLarge()) {
/*       */           
/* 23113 */           if (getSize() <= 4)
/*       */           {
/* 23115 */             return false;
/*       */           }
/* 23117 */           if (isKingdomGuard())
/*       */           {
/* 23119 */             return false;
/*       */           }
/*       */         }
/* 23122 */         else if (subjectItem.isSaddleNormal()) {
/*       */           
/* 23124 */           if (getSize() > 4)
/*       */           {
/* 23126 */             return false;
/*       */           }
/* 23128 */           if (isKingdomGuard())
/*       */           {
/* 23130 */             return false;
/*       */           }
/*       */         }
/* 23133 */         else if (subjectItem.isHorseShoe()) {
/*       */ 
/*       */           
/* 23136 */           if (!isHorse() && (!isUnicorn() || (subjectItem
/* 23137 */             .getMaterial() != 7 && subjectItem
/* 23138 */             .getMaterial() != 8 && subjectItem
/* 23139 */             .getMaterial() != 96)))
/*       */           {
/*       */             
/* 23142 */             return false;
/*       */           }
/*       */         }
/* 23145 */         else if (subjectItem.isBarding()) {
/*       */           
/* 23147 */           if (!isHorse())
/*       */           {
/* 23149 */             return false; } 
/*       */         } 
/*       */       }
/*       */     } 
/* 23153 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public HashMap<Integer, SpellResist> getSpellResistances() {
/* 23158 */     return this.spellResistances;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isItem() {
/* 23164 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setGuardTower(GuardTower guardTower) {
/* 23169 */     this.guardTower = guardTower;
/*       */   }
/*       */ 
/*       */   
/*       */   public GuardTower getGuardTower() {
/* 23174 */     return this.guardTower;
/*       */   }
/*       */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Creature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */