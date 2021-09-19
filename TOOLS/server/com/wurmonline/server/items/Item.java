/*       */ package com.wurmonline.server.items;
/*       */ 
/*       */ import com.wurmonline.math.TilePos;
/*       */ import com.wurmonline.math.Vector2f;
/*       */ import com.wurmonline.math.Vector3f;
/*       */ import com.wurmonline.mesh.FoliageAge;
/*       */ import com.wurmonline.mesh.Tiles;
/*       */ import com.wurmonline.mesh.TreeData;
/*       */ import com.wurmonline.server.Constants;
/*       */ import com.wurmonline.server.FailedException;
/*       */ import com.wurmonline.server.Features;
/*       */ import com.wurmonline.server.GeneralUtilities;
/*       */ import com.wurmonline.server.HistoryManager;
/*       */ import com.wurmonline.server.Items;
/*       */ import com.wurmonline.server.LoginHandler;
/*       */ import com.wurmonline.server.Message;
/*       */ import com.wurmonline.server.MiscConstants;
/*       */ import com.wurmonline.server.NoSuchItemException;
/*       */ import com.wurmonline.server.NoSuchPlayerException;
/*       */ import com.wurmonline.server.Players;
/*       */ import com.wurmonline.server.Server;
/*       */ import com.wurmonline.server.Servers;
/*       */ import com.wurmonline.server.TimeConstants;
/*       */ import com.wurmonline.server.WurmCalendar;
/*       */ import com.wurmonline.server.WurmHarvestables;
/*       */ import com.wurmonline.server.WurmId;
/*       */ import com.wurmonline.server.banks.Banks;
/*       */ import com.wurmonline.server.behaviours.Action;
/*       */ import com.wurmonline.server.behaviours.ArtifactBehaviour;
/*       */ import com.wurmonline.server.behaviours.Behaviour;
/*       */ import com.wurmonline.server.behaviours.CargoTransportationMethods;
/*       */ import com.wurmonline.server.behaviours.CreatureBehaviour;
/*       */ import com.wurmonline.server.behaviours.Methods;
/*       */ import com.wurmonline.server.behaviours.MethodsItems;
/*       */ import com.wurmonline.server.behaviours.NoSuchActionException;
/*       */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*       */ import com.wurmonline.server.behaviours.TerraformingTask;
/*       */ import com.wurmonline.server.behaviours.TileRockBehaviour;
/*       */ import com.wurmonline.server.behaviours.TileTreeBehaviour;
/*       */ import com.wurmonline.server.behaviours.Vehicle;
/*       */ import com.wurmonline.server.behaviours.Vehicles;
/*       */ import com.wurmonline.server.bodys.BodyTemplate;
/*       */ import com.wurmonline.server.combat.ArmourTemplate;
/*       */ import com.wurmonline.server.combat.Arrows;
/*       */ import com.wurmonline.server.creatures.Brand;
/*       */ import com.wurmonline.server.creatures.Communicator;
/*       */ import com.wurmonline.server.creatures.Creature;
/*       */ import com.wurmonline.server.creatures.CreatureStatus;
/*       */ import com.wurmonline.server.creatures.CreatureTemplate;
/*       */ import com.wurmonline.server.creatures.CreatureTemplateCreator;
/*       */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*       */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*       */ import com.wurmonline.server.creatures.Creatures;
/*       */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*       */ import com.wurmonline.server.creatures.Delivery;
/*       */ import com.wurmonline.server.creatures.MovementScheme;
/*       */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*       */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*       */ import com.wurmonline.server.creatures.Offspring;
/*       */ import com.wurmonline.server.creatures.Traits;
/*       */ import com.wurmonline.server.creatures.Wagoner;
/*       */ import com.wurmonline.server.deities.Deities;
/*       */ import com.wurmonline.server.deities.Deity;
/*       */ import com.wurmonline.server.economy.MonetaryConstants;
/*       */ import com.wurmonline.server.economy.Shop;
/*       */ import com.wurmonline.server.effects.Effect;
/*       */ import com.wurmonline.server.effects.EffectFactory;
/*       */ import com.wurmonline.server.epic.EpicServerStatus;
/*       */ import com.wurmonline.server.highways.MethodsHighways;
/*       */ import com.wurmonline.server.highways.Routes;
/*       */ import com.wurmonline.server.kingdom.King;
/*       */ import com.wurmonline.server.kingdom.Kingdom;
/*       */ import com.wurmonline.server.kingdom.Kingdoms;
/*       */ import com.wurmonline.server.meshgen.IslandAdder;
/*       */ import com.wurmonline.server.players.Achievements;
/*       */ import com.wurmonline.server.players.ItemBonus;
/*       */ import com.wurmonline.server.players.Permissions;
/*       */ import com.wurmonline.server.players.PermissionsHistories;
/*       */ import com.wurmonline.server.players.PermissionsPlayerList;
/*       */ import com.wurmonline.server.players.Player;
/*       */ import com.wurmonline.server.players.PlayerInfo;
/*       */ import com.wurmonline.server.players.PlayerInfoFactory;
/*       */ import com.wurmonline.server.questions.NewKingQuestion;
/*       */ import com.wurmonline.server.skills.NoSuchSkillException;
/*       */ import com.wurmonline.server.skills.Skill;
/*       */ import com.wurmonline.server.skills.SkillSystem;
/*       */ import com.wurmonline.server.skills.Skills;
/*       */ import com.wurmonline.server.sounds.SoundPlayer;
/*       */ import com.wurmonline.server.spells.Spell;
/*       */ import com.wurmonline.server.spells.SpellEffect;
/*       */ import com.wurmonline.server.spells.Spells;
/*       */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*       */ import com.wurmonline.server.structures.Blocker;
/*       */ import com.wurmonline.server.structures.Blocking;
/*       */ import com.wurmonline.server.structures.BlockingResult;
/*       */ import com.wurmonline.server.structures.Structure;
/*       */ import com.wurmonline.server.tutorial.MissionTriggers;
/*       */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*       */ import com.wurmonline.server.utils.CoordUtils;
/*       */ import com.wurmonline.server.utils.StringUtil;
/*       */ import com.wurmonline.server.utils.logging.ItemTransfer;
/*       */ import com.wurmonline.server.utils.logging.ItemTransferDatabaseLogger;
/*       */ import com.wurmonline.server.utils.logging.WurmLoggable;
/*       */ import com.wurmonline.server.villages.DeadVillage;
/*       */ import com.wurmonline.server.villages.NoSuchVillageException;
/*       */ import com.wurmonline.server.villages.Village;
/*       */ import com.wurmonline.server.villages.Villages;
/*       */ import com.wurmonline.server.zones.NoSuchZoneException;
/*       */ import com.wurmonline.server.zones.VirtualZone;
/*       */ import com.wurmonline.server.zones.VolaTile;
/*       */ import com.wurmonline.server.zones.Zone;
/*       */ import com.wurmonline.server.zones.Zones;
/*       */ import com.wurmonline.shared.constants.CounterTypes;
/*       */ import com.wurmonline.shared.constants.EffectConstants;
/*       */ import com.wurmonline.shared.constants.ItemMaterials;
/*       */ import com.wurmonline.shared.constants.ProtoConstants;
/*       */ import com.wurmonline.shared.constants.SoundNames;
/*       */ import com.wurmonline.shared.exceptions.WurmServerException;
/*       */ import com.wurmonline.shared.util.MaterialUtilities;
/*       */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*       */ import com.wurmonline.shared.util.StringUtilities;
/*       */ import java.io.IOException;
/*       */ import java.math.BigInteger;
/*       */ import java.util.ArrayList;
/*       */ import java.util.Collections;
/*       */ import java.util.HashSet;
/*       */ import java.util.Iterator;
/*       */ import java.util.Map;
/*       */ import java.util.Random;
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
/*       */ public abstract class Item
/*       */   implements ItemTypes, TimeConstants, MiscConstants, CounterTypes, EffectConstants, ItemMaterials, SoundNames, MonetaryConstants, ProtoConstants, Comparable<Item>, Permissions.IAllow, PermissionsPlayerList.ISettings, CreatureTemplateIds
/*       */ {
/*   162 */   long id = -10L;
/*       */   
/*       */   protected boolean surfaced = true;
/*       */   
/*       */   ItemTemplate template;
/*       */   
/*   168 */   private static final Logger logger = Logger.getLogger(Item.class.getName());
/*       */   
/*       */   public static final int FISHING_REEL = 0;
/*       */   
/*       */   public static final int FISHING_LINE = 1;
/*       */   
/*       */   public static final int FISHING_FLOAT = 2;
/*       */   
/*       */   public static final int FISHING_HOOK = 3;
/*       */   public static final int FISHING_BAIT = 4;
/*   178 */   private static final ItemTransferDatabaseLogger itemLogger = new ItemTransferDatabaseLogger("Item transfer logger", 500);
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   boolean isBusy = false;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   Set<Effect> effects;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   Set<Long> keys;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   Set<Creature> watchers;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   Set<Item> items;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public long lastMaintained;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   float qualityLevel;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   float originalQualityLevel;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   int sizeX;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   int sizeY;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   int sizeZ;
/*       */ 
/*       */ 
/*       */   
/*       */   float posX;
/*       */ 
/*       */ 
/*       */   
/*       */   float posY;
/*       */ 
/*       */ 
/*       */   
/*       */   float posZ;
/*       */ 
/*       */ 
/*       */   
/*       */   float rotation;
/*       */ 
/*       */ 
/*       */   
/*   256 */   long parentId = -10L;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   261 */   long ownerId = -10L;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   266 */   public int zoneId = -10;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   InscriptionData inscription;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   277 */   String name = "";
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   282 */   String description = "";
/*       */ 
/*       */ 
/*       */   
/*       */   short place;
/*       */ 
/*       */ 
/*       */   
/*       */   boolean locked = false;
/*       */ 
/*       */ 
/*       */   
/*       */   float damage;
/*       */ 
/*       */   
/*       */   ItemData data;
/*       */ 
/*       */   
/*       */   @Nullable
/*   301 */   TradingWindow tradeWindow = null;
/*       */ 
/*       */   
/*   304 */   int weight = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   309 */   short temperature = 200;
/*       */ 
/*       */ 
/*       */   
/*       */   byte material;
/*       */ 
/*       */   
/*   316 */   long lockid = -10L;
/*       */ 
/*       */ 
/*       */   
/*       */   public static final int maxSizeMod = 4;
/*       */ 
/*       */   
/*   323 */   int price = 0;
/*       */   
/*   325 */   int tempChange = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   330 */   byte bless = 0;
/*       */ 
/*       */ 
/*       */   
/*       */   boolean banked = false;
/*       */ 
/*       */   
/*   337 */   public byte enchantment = 0;
/*       */   
/*   339 */   public long lastOwner = -10L;
/*       */   
/*       */   public boolean deleted = false;
/*       */   
/*   343 */   private int ticksSinceLastDecay = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean mailed = false;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nonnull
/*   358 */   private static final Effect[] emptyEffects = new Effect[0];
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   364 */   static final Item[] emptyItems = new Item[0];
/*       */   
/*   366 */   public byte newLayer = Byte.MIN_VALUE;
/*       */   
/*   368 */   private static long lastPolledWhiteAltar = 0L;
/*       */   
/*   370 */   private static long lastPolledBlackAltar = 0L;
/*       */ 
/*       */   
/*       */   public boolean transferred = false;
/*       */   
/*       */   private static final int REPLACE_SEED = 102539;
/*       */   
/*       */   private static final char dotchar = '.';
/*       */   
/*       */   static final float visibleDecayLimit = 50.0F;
/*       */   
/*       */   static final float visibleWornLimit = 25.0F;
/*       */   
/*   383 */   public long lastParentId = -10L;
/*       */   
/*       */   public boolean hatching = false;
/*       */   
/*   387 */   byte mailTimes = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   396 */   byte auxbyte = 0;
/*       */   
/*       */   public long creationDate;
/*       */   
/*   400 */   public byte creationState = 0;
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*   405 */   public int realTemplate = -10;
/*       */   
/*       */   public boolean wornAsArmour = false;
/*       */   
/*   409 */   public int color = -1;
/*   410 */   public int color2 = -1;
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean female = false;
/*       */ 
/*       */ 
/*       */   
/*   418 */   public String creator = "";
/*       */   
/*   420 */   int creatorMaxLength = 40;
/*       */   
/*       */   private static final String lit = " (lit)";
/*       */   
/*       */   private static final String modelit = ".lit";
/*       */   
/*       */   public boolean hidden = false;
/*       */   
/*   428 */   public byte rarity = 0;
/*       */ 
/*       */   
/*       */   public static final long TRASHBIN_TICK = 3600L;
/*       */ 
/*       */   
/*       */   private static final long TREBUCHET_RELOAD_TIME = 120L;
/*       */ 
/*       */   
/*       */   public static final int MAX_CONTAINED_ITEMS_ITEMCRATE_SMALL = 150;
/*       */ 
/*       */   
/*       */   public static final int MAX_CONTAINED_ITEMS_ITEMCRATE_LARGE = 300;
/*       */   
/*   442 */   public long onBridge = -10L;
/*       */   
/*   444 */   Permissions permissions = new Permissions();
/*       */   
/*       */   public static final byte FOOD_STATE_RAW = 0;
/*       */   
/*       */   public static final byte FOOD_STATE_FRIED = 1;
/*       */   
/*       */   public static final byte FOOD_STATE_GRILLED = 2;
/*       */   
/*       */   public static final byte FOOD_STATE_BOILED = 3;
/*       */   
/*       */   public static final byte FOOD_STATE_ROASTED = 4;
/*       */   
/*       */   public static final byte FOOD_STATE_STEAMED = 5;
/*       */   
/*       */   public static final byte FOOD_STATE_BAKED = 6;
/*       */   
/*       */   public static final byte FOOD_STATE_COOKED = 7;
/*       */   public static final byte FOOD_STATE_CANDIED = 8;
/*       */   public static final byte FOOD_STATE_CHOCOLATE_COATED = 9;
/*       */   public static final byte FOOD_STATE_CHOPPED_BIT = 16;
/*       */   public static final byte FOOD_STATE_MASHED_BIT = 32;
/*       */   public static final byte FOOD_STATE_WRAPPED_BIT = 64;
/*       */   public static final byte FOOD_STATE_FRESH_BIT = -128;
/*       */   public static final byte FOOD_STATE_CHOPPED_MASK = -17;
/*       */   public static final byte FOOD_STATE_MASHED_MASK = -33;
/*       */   public static final byte FOOD_STATE_WRAPPED_MASK = -65;
/*       */   public static final byte FOOD_STATE_FRESH_MASK = 127;
/*   471 */   private int internalVolume = 0;
/*       */ 
/*       */ 
/*       */   
/*   475 */   private long whenRented = 0L;
/*       */   
/*       */   private boolean isLightOverride = false;
/*       */   
/*   479 */   private short warmachineWinches = 0;
/*       */   
/*       */   public static final long DRAG_AFTER_RAM_TIME = 30000L;
/*   482 */   public long lastRammed = 0L;
/*   483 */   public long lastRamUser = -10L;
/*       */   
/*   485 */   private long lastPolled = 0L;
/*       */   
/*       */   private boolean wagonerWagon = false;
/*       */   private boolean replacing = false;
/*       */   private boolean isSealedOverride = false;
/*   490 */   private String whatHappened = "";
/*       */   
/*   492 */   private long wasBrandedTo = -10L;
/*       */ 
/*       */ 
/*       */   
/*       */   private long lastAuxPoll;
/*       */ 
/*       */ 
/*       */   
/*       */   protected boolean placedOnParent = false;
/*       */ 
/*       */ 
/*       */   
/*       */   protected boolean isChained = false;
/*       */ 
/*       */ 
/*       */   
/*       */   Item(long wurmId, String aName, ItemTemplate aTemplate, float aQLevel, byte aMaterial, byte aRarity, long bridgeId, @Nullable String aCreator) throws IOException {
/*   509 */     if (wurmId == -10L) {
/*   510 */       this.id = getNextWurmId(aTemplate);
/*       */     } else {
/*   512 */       this.id = wurmId;
/*   513 */     }  this.template = aTemplate;
/*   514 */     this.qualityLevel = aQLevel;
/*   515 */     this.originalQualityLevel = aQLevel;
/*   516 */     this.weight = aTemplate.getWeightGrams();
/*   517 */     this.name = aName;
/*   518 */     this.material = aMaterial;
/*   519 */     this.rarity = aRarity;
/*   520 */     this.onBridge = bridgeId;
/*   521 */     if (isNamed() && aCreator != null && aCreator.length() > 0)
/*       */     {
/*   523 */       this.creator = aCreator.substring(0, Math.min(aCreator.length(), this.creatorMaxLength));
/*       */     }
/*   525 */     if (!aTemplate.isBodyPart())
/*   526 */       create(this.qualityLevel, WurmCalendar.currentTime); 
/*   527 */     Items.putItem(this);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   Item(String aName, short aPlace, ItemTemplate aTemplate, float aQualityLevel, byte aMaterial, byte aRarity, long bridgeId, String aCreator) throws IOException {
/*   533 */     this(-10L, aName, aTemplate, aQualityLevel, aMaterial, aRarity, bridgeId, aCreator);
/*   534 */     setPlace(aPlace);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   Item(String aName, ItemTemplate aTemplate, float aQualityLevel, float aPosX, float aPosY, float aPosZ, float aRotation, byte aMaterial, byte aRarity, long bridgeId, String aCreator) throws IOException {
/*   542 */     this(-10L, aName, aTemplate, aQualityLevel, aMaterial, aRarity, bridgeId, aCreator);
/*   543 */     setPos(aPosX, aPosY, aPosZ, aRotation, bridgeId);
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
/*       */   public int compareTo(Item otherItem) {
/*   556 */     return getName().compareTo(otherItem.getName());
/*       */   }
/*       */ 
/*       */   
/*       */   public static DbStrings getDbStrings(int templateNum) {
/*   561 */     if (templateNum >= 10 && templateNum <= 19) {
/*       */       
/*   563 */       logWarn("THIS HAPPENS AT ", new Exception());
/*   564 */       return null;
/*       */     } 
/*   566 */     if (templateNum >= 50 && templateNum <= 61)
/*   567 */       return CoinDbStrings.getInstance(); 
/*   568 */     return ItemDbStrings.getInstance();
/*       */   }
/*       */ 
/*       */   
/*       */   public static DbStrings getDbStringsByWurmId(long wurmId) {
/*   573 */     if (WurmId.getType(wurmId) == 19) {
/*       */       
/*   575 */       logWarn("THIS HAPPENS AT ", new Exception());
/*   576 */       return null;
/*       */     } 
/*   578 */     if (WurmId.getType(wurmId) == 20)
/*   579 */       return CoinDbStrings.getInstance(); 
/*   580 */     return ItemDbStrings.getInstance();
/*       */   }
/*       */ 
/*       */   
/*       */   private static long getNextWurmId(ItemTemplate template) {
/*   585 */     if (template.isTemporary()) {
/*   586 */       return WurmId.getNextTempItemId();
/*       */     }
/*   588 */     if (template.isCoin()) {
/*   589 */       return WurmId.getNextCoinId();
/*       */     }
/*   591 */     return WurmId.getNextItemId();
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
/*       */   public final boolean mayLockItems() {
/*   608 */     return (isLock() && getTemplateId() != 167 && getTemplateId() != 252 && 
/*   609 */       getTemplateId() != 568);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBoatLock() {
/*   618 */     return (getTemplateId() == 568);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBrazier() {
/*   627 */     return this.template.isBrazier();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isAnchor() {
/*   636 */     return (getTemplateId() == 565);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isTrap() {
/*   645 */     return this.template.isTrap;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isDisarmTrap() {
/*   654 */     return this.template.isDisarmTrap;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isOre() {
/*   663 */     return this.template.isOre;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isShard() {
/*   672 */     return this.template.isShard;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBeingWorkedOn() {
/*   682 */     if (this.isBusy)
/*   683 */       return true; 
/*   684 */     if (isHollow())
/*       */     {
/*   686 */       if (this.items != null)
/*       */       {
/*   688 */         for (Iterator<Item> it = this.items.iterator(); it.hasNext(); ) {
/*       */           
/*   690 */           Item item = it.next();
/*   691 */           if (item.isBeingWorkedOn())
/*   692 */             return true; 
/*       */         } 
/*       */       }
/*       */     }
/*   696 */     return this.isBusy;
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
/*       */   public final boolean combine(Item target, Creature performer) throws FailedException {
/*   711 */     if (equals(target))
/*   712 */       return false; 
/*   713 */     Item parent = null;
/*   714 */     if (this.parentId != -10L && target.getParentId() != this.parentId) {
/*       */       
/*       */       try {
/*       */         
/*   718 */         parent = Items.getItem(this.parentId);
/*       */         
/*   720 */         if (!parent.hasSpaceFor(target.getVolume())) {
/*   721 */           throw new FailedException("The container could not contain the combined items.");
/*       */         }
/*   723 */       } catch (NoSuchItemException nsi) {
/*       */         
/*   725 */         logInfo("Strange, combining item without parent: " + this.id);
/*   726 */         throw new FailedException("The container could not contain the combined items.");
/*       */       } 
/*       */     }
/*   729 */     if (this.ownerId == -10L || target.getOwnerId() == -10L)
/*       */     {
/*   731 */       throw new FailedException("You need to carry both items to combine them.");
/*       */     }
/*       */     
/*   734 */     if (!isCombineCold() && isMetal() && target.getTemplateId() != 204 && performer
/*   735 */       .getPower() == 0)
/*       */     {
/*   737 */       if (this.temperature < 3500 || target.getTemperature() < 3500)
/*   738 */         throw new FailedException("Metal needs to be glowing hot to be combined."); 
/*       */     }
/*   740 */     if (getTemplateId() == target.getTemplateId() && isCombine()) {
/*       */       
/*   742 */       if (getMaterial() == target.getMaterial() || (isWood() && target.isWood())) {
/*       */         
/*   744 */         int allWeight = getWeightGrams() + target.getWeightGrams();
/*   745 */         if (isLiquid())
/*       */         {
/*   747 */           if (!parent.hasSpaceFor(allWeight))
/*       */           {
/*   749 */             throw new FailedException("The " + parent.getName() + " cannot contain that much " + getName() + ".");
/*       */           }
/*       */         }
/*       */ 
/*       */ 
/*       */         
/*   755 */         float maxW = (ItemFactory.isMetalLump(getTemplateId()) ? Math.max(this.template.getWeightGrams() * 4 * 4 * 4, 64000) : (this.template.getWeightGrams() * 4 * 4 * 4));
/*   756 */         if (allWeight <= maxW) {
/*       */           
/*   758 */           if (parent != null) {
/*       */             
/*       */             try {
/*       */               
/*   762 */               parent.dropItem(this.id, false);
/*       */             
/*       */             }
/*   765 */             catch (NoSuchItemException nsi) {
/*       */               
/*   767 */               logWarn("This item doesn't exist: " + this.id, (Throwable)nsi);
/*   768 */               return false;
/*       */             } 
/*       */           }
/*       */           
/*   772 */           float newQl = (getCurrentQualityLevel() * getWeightGrams() + target.getCurrentQualityLevel() * target.getWeightGrams()) / allWeight;
/*       */           
/*   774 */           if (allWeight > 0) {
/*       */             
/*   776 */             if (target.isColor() && isColor()) {
/*   777 */               setColor(WurmColor.mixColors(this.color, getWeightGrams(), target.color, target.getWeightGrams(), newQl));
/*       */             }
/*   779 */             if (getRarity() > target.getRarity()) {
/*       */ 
/*       */               
/*   782 */               if (Server.rand.nextInt(allWeight) > getWeightGrams() / 4) {
/*   783 */                 setRarity(target.getRarity());
/*       */               }
/*   785 */             } else if (target.getRarity() > getRarity()) {
/*       */ 
/*       */               
/*   788 */               if (Server.rand.nextInt(allWeight) > target.getWeightGrams() / 4)
/*   789 */                 setRarity(target.getRarity()); 
/*       */             } 
/*   791 */             setWeight(allWeight, false);
/*   792 */             setQualityLevel(newQl);
/*   793 */             setDamage(0.0F);
/*   794 */             Items.destroyItem(target.getWurmId());
/*       */             
/*   796 */             if (parent != null)
/*       */             {
/*   798 */               if (!parent.insertItem(this)) {
/*       */                 try
/*       */                 {
/*       */                   
/*   802 */                   long powner = parent.getOwner();
/*   803 */                   Creature pown = Server.getInstance().getCreature(powner);
/*   804 */                   pown.getInventory().insertItem(this);
/*       */                 }
/*   806 */                 catch (NoSuchCreatureException nsc)
/*       */                 {
/*   808 */                   logWarn(getName() + ", " + getWurmId() + nsc.getMessage(), (Throwable)nsc);
/*       */                 }
/*   810 */                 catch (NoSuchPlayerException nsc)
/*       */                 {
/*   812 */                   logWarn(getName() + ", " + getWurmId() + nsc.getMessage(), (Throwable)nsc);
/*       */                 }
/*   814 */                 catch (NotOwnedException no)
/*       */                 {
/*   816 */                   VolaTile tile = Zones.getOrCreateTile((int)getPosX() >> 2, (int)getPosY() >> 2, 
/*   817 */                       isOnSurface());
/*   818 */                   tile.addItem(this, false, false);
/*   819 */                   logWarn("The combined " + getName() + " was created on ground. This should not happen.");
/*       */                 }
/*       */               
/*       */               }
/*       */             }
/*       */           } else {
/*       */             
/*   826 */             Items.destroyItem(this.id);
/*   827 */           }  return true;
/*       */         } 
/*       */         
/*   830 */         throw new FailedException("The combined item would be too large to handle.");
/*       */       } 
/*       */       
/*   833 */       throw new FailedException("The items are of different materials.");
/*       */     } 
/*   835 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isKingdomFlag() {
/*   840 */     return this.template.isKingdomFlag;
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
/*       */   @Nullable
/*       */   public InscriptionData getInscription() {
/*   853 */     return this.inscription;
/*       */   }
/*       */ 
/*       */   
/*       */   public String getHoverText() {
/*   858 */     return "";
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
/*       */   public String getName() {
/*   870 */     return getName(true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getName(boolean showWrapped) {
/*   881 */     StringBuilder builder = new StringBuilder();
/*       */     
/*   883 */     int templateId = this.template.getTemplateId();
/*   884 */     String description = getDescription();
/*   885 */     String stoSend = "";
/*       */     
/*   887 */     if (descIsName() && !description.isEmpty()) {
/*       */       
/*   889 */       builder.append('"');
/*   890 */       builder.append(description);
/*   891 */       builder.append('"');
/*   892 */       return builder.toString();
/*       */     } 
/*   894 */     if (templateId == 1300) {
/*       */       
/*   896 */       if (getAuxData() == 1)
/*   897 */         return "faintly glowing " + this.name; 
/*   898 */       return "brightly glowing " + this.name;
/*       */     } 
/*   900 */     if (templateId == 1423)
/*       */     {
/*   902 */       if (getAuxBit(7) == true)
/*   903 */         return "small " + this.name + " token"; 
/*       */     }
/*   905 */     if (templateId == 1422)
/*       */     {
/*   907 */       return this.name + " hidden cache";
/*       */     }
/*       */     
/*   910 */     if (templateId == 1307)
/*       */     {
/*   912 */       if (getData1() > 0) {
/*       */         
/*   914 */         if (getRealTemplate() != null)
/*       */         {
/*   916 */           return (getRealTemplate()).sizeString + getRealTemplate().getName() + " " + this.name + " [" + 
/*   917 */             getAuxData() + "/" + getRealTemplate().getFragmentAmount() + "]";
/*       */         }
/*       */       }
/*       */       else {
/*       */         
/*   922 */         builder.append("unidentified ");
/*   923 */         if (getRealTemplate() != null && getAuxData() >= 65)
/*       */         {
/*   925 */           if (getRealTemplate().isWeapon()) {
/*   926 */             builder.append("weapon ");
/*   927 */           } else if (getRealTemplate().isArmour()) {
/*   928 */             builder.append("armour ");
/*   929 */           } else if (getRealTemplate().isTool()) {
/*   930 */             builder.append("tool ");
/*   931 */           } else if (getRealTemplate().isStatue()) {
/*   932 */             builder.append("statue ");
/*   933 */           } else if (getRealTemplate().isHollow()) {
/*   934 */             builder.append("container ");
/*   935 */           } else if (getRealTemplate().isRiftLoot()) {
/*   936 */             builder.append("rift ");
/*   937 */           } else if (getRealTemplate().isMetal()) {
/*   938 */             builder.append("metal ");
/*   939 */           } else if (getRealTemplate().isWood()) {
/*   940 */             builder.append("wooden ");
/*       */           }  } 
/*   942 */         builder.append(this.name);
/*       */         
/*   944 */         return builder.toString();
/*       */       } 
/*       */     }
/*       */     
/*   948 */     if (templateId == 854)
/*       */     {
/*   950 */       return description.isEmpty() ? "sign" : description;
/*       */     }
/*       */     
/*   953 */     if (isLight())
/*       */     {
/*   955 */       if (getTemplateId() != 37) {
/*       */         
/*   957 */         if (this.name.endsWith(" (lit)"))
/*       */         {
/*   959 */           this.name = this.name.replace(" (lit)", "");
/*       */         }
/*   961 */         if (isOnFire() && !this.name.endsWith(" (lit)") && !this.isLightOverride)
/*   962 */           stoSend = " (lit)"; 
/*       */       } 
/*       */     }
/*   965 */     if (this.template.getTemplateId() == 1243 && isOnFire())
/*       */     {
/*   967 */       stoSend = " (smoking)";
/*       */     }
/*   969 */     if (templateId == 1346) {
/*       */       
/*   971 */       Item reel = getFishingReel();
/*   972 */       if (reel != null) {
/*       */         
/*   974 */         if (reel.getTemplateId() == 1372)
/*   975 */           return "light fishing rod"; 
/*   976 */         if (reel.getTemplateId() == 1373)
/*   977 */           return "medium fishing rod"; 
/*   978 */         if (reel.getTemplateId() == 1374)
/*   979 */           return "deep water fishing rod"; 
/*   980 */         if (reel.getTemplateId() == 1375)
/*   981 */           return "professional fishing rod"; 
/*       */       } 
/*       */     } 
/*   984 */     if ((isWind() || this.template.isKingdomFlag) && getTemplateId() != 487) {
/*       */       
/*   986 */       if (getTemplateId() == 579 || getTemplateId() == 578 || 
/*   987 */         getTemplateId() == 999)
/*       */       {
/*   989 */         if (getKingdom() != 0) {
/*       */           
/*   991 */           builder.append(Kingdoms.getNameFor(getKingdom()));
/*   992 */           builder.append(' ');
/*       */         } 
/*       */       }
/*   995 */       builder.append(this.template.getName());
/*   996 */       return builder.toString();
/*       */     } 
/*   998 */     if (this.template.isRune() && getRealTemplate() != null) {
/*       */       
/*  1000 */       switch (getRealTemplate().getTemplateId()) {
/*       */         
/*       */         case 1104:
/*  1003 */           builder.append("wooden");
/*       */           break;
/*       */         case 1103:
/*  1006 */           builder.append("crystal");
/*       */           break;
/*       */         case 1102:
/*  1009 */           builder.append("stone");
/*       */           break;
/*       */         default:
/*  1012 */           builder.append("unknown");
/*       */           break;
/*       */       } 
/*  1015 */       builder.append(' ');
/*       */     } 
/*  1017 */     if (isBulkItem()) {
/*       */       
/*  1019 */       int nums = getBulkNums();
/*  1020 */       if (nums > 0) {
/*       */         
/*       */         try {
/*       */           
/*  1024 */           ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(getRealTemplateId());
/*       */           
/*  1026 */           builder.append(it.sizeString);
/*       */           
/*  1028 */           if (getAuxData() != 0 && it.usesFoodState())
/*  1029 */             builder.append(getFoodAuxByteName(it, false, true)); 
/*  1030 */           if (!getActualName().equalsIgnoreCase("bulk item")) {
/*  1031 */             builder.append(getActualName());
/*  1032 */           } else if (nums > 1) {
/*  1033 */             builder.append(it.getPlural());
/*       */           } else {
/*  1035 */             builder.append(it.getName());
/*       */           } 
/*  1037 */           return builder.toString();
/*       */         }
/*  1039 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  1041 */           logWarn(getWurmId() + " bulk nums=" + getBulkNums() + " but template is " + 
/*  1042 */               getBulkTemplateId());
/*       */         } 
/*       */       }
/*       */     } else {
/*  1046 */       if (isInventoryGroup()) {
/*       */ 
/*       */         
/*  1049 */         if (!description.isEmpty()) {
/*       */           
/*  1051 */           this.name = description;
/*  1052 */           setDescription("");
/*       */         } 
/*  1054 */         return this.name;
/*       */       } 
/*  1056 */       if (templateId == 853 && getItemCount() > 0) {
/*       */ 
/*       */         
/*  1059 */         Item ship = getItemsAsArray()[0];
/*  1060 */         stoSend = " [" + ship.getName() + "]";
/*       */       } 
/*  1062 */     }  if (this.name.equals("")) {
/*       */       
/*  1064 */       if (templateId == 179) {
/*       */         
/*       */         try
/*       */         {
/*  1068 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(
/*  1069 */               AdvancedCreationEntry.getTemplateId(this));
/*  1070 */           builder.append("unfinished ");
/*  1071 */           builder.append(temp.sizeString);
/*  1072 */           builder.append(temp.getName());
/*  1073 */           return builder.toString();
/*       */         }
/*  1075 */         catch (NoSuchTemplateException nst)
/*       */         {
/*  1077 */           logWarn(nst.getMessage(), (Throwable)nst);
/*       */         
/*       */         }
/*       */       
/*       */       }
/*  1082 */       else if (templateId == 177) {
/*       */         
/*  1084 */         int lData = getData1();
/*       */         
/*       */         try {
/*  1087 */           if (lData != -1)
/*       */           {
/*  1089 */             ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(lData);
/*  1090 */             builder.append("Pile of ");
/*  1091 */             builder.append(temp.sizeString);
/*  1092 */             builder.append(temp.getName());
/*  1093 */             return builder.toString();
/*       */           }
/*       */         
/*  1096 */         } catch (NoSuchTemplateException nst) {
/*       */           
/*  1098 */           logInfo("Inconsistency: " + lData + " does not exist as templateid.");
/*       */         } 
/*       */       } else {
/*  1101 */         if (templateId == 918 || templateId == 917 || templateId == 1017) {
/*       */ 
/*       */ 
/*       */           
/*  1105 */           builder.append(this.template.getName());
/*  1106 */           return builder.toString();
/*       */         } 
/*  1108 */         if (isWood()) {
/*       */           
/*  1110 */           builder.append(this.template.sizeString);
/*  1111 */           builder.append(this.template.getName());
/*  1112 */           return builder.toString();
/*       */         } 
/*  1114 */         if ((isSign() || isFlag()) && getTemplateId() != 835) {
/*       */           
/*  1116 */           if (isPlanted())
/*       */           {
/*  1118 */             if (!description.isEmpty())
/*       */             {
/*  1120 */               builder.append('"');
/*  1121 */               builder.append(description);
/*  1122 */               builder.append('"');
/*  1123 */               return builder.toString();
/*       */             }
/*       */           
/*       */           }
/*  1127 */         } else if (templateId == 518) {
/*       */           
/*  1129 */           if (!description.isEmpty())
/*       */           {
/*  1131 */             builder.append("Colossus of ");
/*  1132 */             builder.append(LoginHandler.raiseFirstLetter(description));
/*  1133 */             return builder.toString();
/*       */           }
/*       */         
/*  1136 */         } else if (isDecoration() || isMetal()) {
/*       */           
/*       */           try
/*       */           {
/*  1140 */             ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  1141 */             builder.append(temp.sizeString);
/*  1142 */             builder.append(this.template.getName());
/*  1143 */             builder.append(stoSend);
/*  1144 */             return builder.toString();
/*       */           }
/*  1146 */           catch (NoSuchTemplateException nst)
/*       */           {
/*  1148 */             logInfo("Inconsistency: " + templateId + " does not exist as templateid.");
/*       */           }
/*       */         
/*  1151 */         } else if (isBodyPart()) {
/*       */           
/*  1153 */           if (templateId == 11) {
/*       */             
/*  1155 */             if (this.place == 3) {
/*  1156 */               builder.append("left ");
/*       */             } else {
/*  1158 */               builder.append("right ");
/*  1159 */             }  builder.append(this.template.getName());
/*  1160 */             return builder.toString();
/*       */           } 
/*  1162 */           if (templateId == 14) {
/*       */             
/*  1164 */             if (this.place == 13) {
/*  1165 */               builder.append("left ");
/*       */             } else {
/*  1167 */               builder.append("right ");
/*  1168 */             }  builder.append(this.template.getName());
/*  1169 */             return builder.toString();
/*       */           } 
/*  1171 */           if (templateId == 15)
/*       */           {
/*  1173 */             if (this.place == 15) {
/*  1174 */               builder.append("left ");
/*       */             } else {
/*  1176 */               builder.append("right ");
/*  1177 */             }  builder.append(this.template.getName());
/*  1178 */             return builder.toString();
/*       */           }
/*       */         
/*  1181 */         } else if (isButcheredItem()) {
/*       */ 
/*       */           
/*       */           try {
/*  1185 */             CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(getData2());
/*  1186 */             builder.append(temp.getName());
/*  1187 */             builder.append(' ');
/*  1188 */             builder.append(this.template.getName());
/*  1189 */             return builder.toString();
/*       */           }
/*  1191 */           catch (Exception nst) {
/*       */             
/*  1193 */             logInfo(getWurmId() + " unknown butchered creature: " + nst.getMessage(), nst);
/*       */           } 
/*       */         } 
/*       */       } 
/*  1197 */       builder.append(this.template.sizeString);
/*  1198 */       builder.append(this.template.getName());
/*  1199 */       return builder.toString();
/*       */     } 
/*  1201 */     if ((isSign() && getTemplateId() != 835) || isFlag()) {
/*       */       
/*  1203 */       if (isPlanted())
/*       */       {
/*  1205 */         if (!description.isEmpty())
/*       */         {
/*  1207 */           builder.append('"');
/*  1208 */           builder.append(description);
/*       */           
/*  1210 */           builder.append('"');
/*       */           
/*  1212 */           return builder.toString();
/*       */         }
/*       */       
/*       */       }
/*  1216 */     } else if (templateId == 518) {
/*       */       
/*  1218 */       if (!description.isEmpty()) {
/*       */         
/*  1220 */         builder.append('"');
/*  1221 */         builder.append("Colossus of ");
/*  1222 */         builder.append(LoginHandler.raiseFirstLetter(description));
/*       */         
/*  1224 */         builder.append('"');
/*  1225 */         return builder.toString();
/*       */       } 
/*       */     } else {
/*  1228 */       if ((isVehicle() && !isChair() && !description.isEmpty()) || (
/*  1229 */         isChair() && !description.isEmpty() && getParentId() == -10L)) {
/*       */         
/*  1231 */         builder.append('"');
/*  1232 */         builder.append(description);
/*  1233 */         builder.append('"');
/*  1234 */         return builder.toString() + stoSend;
/*       */       } 
/*  1236 */       if (templateId == 654 && getAuxData() > 0) {
/*       */         
/*  1238 */         if (getBless() != null) {
/*  1239 */           builder.append("Active ");
/*       */         } else {
/*  1241 */           builder.append("Passive ");
/*       */         } 
/*  1243 */       } else if (templateId == 1239 || templateId == 1175) {
/*       */         
/*  1245 */         if (WurmCalendar.isSeasonWinter()) {
/*       */           
/*  1247 */           if (hasQueen()) {
/*  1248 */             builder.append("dormant ");
/*       */           } else {
/*  1250 */             builder.append("empty ");
/*       */           } 
/*  1252 */         } else if (hasTwoQueens()) {
/*  1253 */           builder.append("noisy ");
/*  1254 */         } else if (hasQueen()) {
/*  1255 */           builder.append("active ");
/*       */         } else {
/*  1257 */           builder.append("empty ");
/*       */         } 
/*  1259 */       } else if (usesFoodState()) {
/*       */         
/*  1261 */         builder.append(getFoodAuxByteName(this.template, false, showWrapped));
/*       */       }
/*  1263 */       else if (getTemplateId() == 729 && getAuxData() > 0) {
/*       */ 
/*       */         
/*  1266 */         builder.append("birthday ");
/*       */       }
/*  1268 */       else if (isSealedByPlayer()) {
/*       */         
/*  1270 */         builder.append("sealed ");
/*  1271 */         for (Item item : getItemsAsArray()) {
/*       */           
/*  1273 */           if (item.isLiquid() && item.isDye()) {
/*       */             
/*  1275 */             int red = WurmColor.getColorRed(item.getColor());
/*  1276 */             int green = WurmColor.getColorGreen(item.getColor());
/*  1277 */             int blue = WurmColor.getColorBlue(item.getColor());
/*  1278 */             stoSend = " [" + item.getName() + "] (" + red + "/" + green + "/" + blue + ")";
/*       */             
/*       */             break;
/*       */           } 
/*  1282 */           if (item.isLiquid()) {
/*       */             
/*  1284 */             stoSend = " [" + item.getName() + "]";
/*       */             
/*       */             break;
/*       */           } 
/*       */         } 
/*  1289 */       } else if (getTemplateId() == 1162 && getParentId() != -10L) {
/*       */ 
/*       */         
/*  1292 */         ItemTemplate rt = getRealTemplate();
/*  1293 */         if (rt != null)
/*       */         {
/*  1295 */           stoSend = " [" + rt.getName().replace(" ", "") + "]";
/*       */         }
/*       */       }
/*  1298 */       else if (getTemplateId() == 748 || getTemplateId() == 1272) {
/*       */         InscriptionData ins; WurmHarvestables.Harvestable harvestable;
/*  1300 */         switch (getAuxData()) {
/*       */ 
/*       */           
/*       */           case 0:
/*  1304 */             ins = getInscription();
/*  1305 */             if (ins != null && ins.hasBeenInscribed()) {
/*  1306 */               builder.append("inscribed "); break;
/*       */             } 
/*  1308 */             builder.append("blank ");
/*       */             break;
/*       */ 
/*       */           
/*       */           case 1:
/*  1313 */             builder.append("recipe ");
/*       */             break;
/*       */ 
/*       */           
/*       */           case 2:
/*  1318 */             builder.append("waxed ");
/*       */             break;
/*       */ 
/*       */ 
/*       */           
/*       */           default:
/*  1324 */             harvestable = WurmHarvestables.getHarvestable(getAuxData() - 8);
/*  1325 */             if (harvestable == null)
/*  1326 */               builder.append("ruined "); 
/*       */             break;
/*       */         } 
/*       */       } 
/*       */     } 
/*  1331 */     if (isTrellis()) {
/*       */       
/*  1333 */       FoliageAge age = FoliageAge.fromByte(getLeftAuxData());
/*  1334 */       stoSend = " (" + age.getAgeName() + ")";
/*       */     } 
/*  1336 */     builder.append(this.name);
/*  1337 */     builder.append(stoSend);
/*  1338 */     return builder.toString();
/*       */   }
/*       */ 
/*       */   
/*       */   public String getFoodAuxByteName(ItemTemplate it, boolean full, boolean showWrapped) {
/*  1343 */     StringBuilder builder = new StringBuilder();
/*       */     
/*  1345 */     if (getTemplateId() == 128) {
/*       */       
/*  1347 */       if (isSalted())
/*  1348 */         builder.append("salty "); 
/*  1349 */       return builder.toString();
/*       */     } 
/*  1351 */     if (isFresh()) {
/*  1352 */       builder.append("fresh ");
/*  1353 */     } else if (getDamage() > 90.0F) {
/*  1354 */       builder.append("rotten ");
/*  1355 */     } else if (getDamage() > 75.0F) {
/*  1356 */       builder.append("moldy ");
/*       */     } 
/*  1358 */     if (full) {
/*       */       
/*  1360 */       if (isSalted())
/*  1361 */         builder.append("salted "); 
/*  1362 */       if (isFish() && isUnderWeight()) {
/*  1363 */         builder.append("underweight ");
/*       */       }
/*       */     } 
/*  1366 */     if (isWrapped())
/*       */     {
/*  1368 */       if (it.canBeDistilled()) {
/*  1369 */         builder.append("undistilled ");
/*  1370 */       } else if (showWrapped) {
/*       */         
/*  1372 */         builder.append("wrapped ");
/*       */       }  } 
/*  1374 */     if (full && builder.length() == 0) {
/*  1375 */       builder.append("(none) ");
/*       */     }
/*  1377 */     switch (getRightAuxData()) {
/*       */       
/*       */       case 1:
/*  1380 */         builder.append("fried ");
/*       */         break;
/*       */       case 2:
/*  1383 */         builder.append("grilled ");
/*       */         break;
/*       */       case 3:
/*  1386 */         builder.append("boiled ");
/*       */         break;
/*       */       case 4:
/*  1389 */         builder.append("roasted ");
/*       */         break;
/*       */       case 5:
/*  1392 */         builder.append("steamed ");
/*       */         break;
/*       */       case 6:
/*  1395 */         builder.append("baked ");
/*       */         break;
/*       */       case 7:
/*  1398 */         builder.append("cooked ");
/*       */         break;
/*       */       case 8:
/*  1401 */         builder.append("candied ");
/*       */         break;
/*       */       case 9:
/*  1404 */         builder.append("chocolate coated ");
/*       */         break;
/*       */       default:
/*  1407 */         if (it.canShowRaw()) {
/*  1408 */           builder.append("raw "); break;
/*  1409 */         }  if (full)
/*  1410 */           builder.append("(raw) "); 
/*       */         break;
/*       */     } 
/*  1413 */     if (isChopped())
/*       */     {
/*  1415 */       if (it.isHerb() || it.isVegetable() || it.isFish() || it.isMushroom) {
/*  1416 */         builder.append("chopped ");
/*  1417 */       } else if (it.isMeat()) {
/*  1418 */         builder.append("diced ");
/*  1419 */       } else if (it.isSpice()) {
/*  1420 */         builder.append("ground ");
/*  1421 */       } else if (it.canBeFermented()) {
/*  1422 */         builder.append("unfermented ");
/*  1423 */       } else if (it.getTemplateId() == 1249) {
/*  1424 */         builder.append("whipped ");
/*       */       } else {
/*  1426 */         builder.append("zombified ");
/*       */       }  } 
/*  1428 */     if (isMashedBitSet())
/*       */     {
/*  1430 */       if (it.isMeat()) {
/*  1431 */         builder.append("minced ");
/*  1432 */       } else if (it.isVegetable()) {
/*  1433 */         builder.append("mashed ");
/*  1434 */       } else if (it.canBeFermented()) {
/*  1435 */         builder.append("fermenting ");
/*  1436 */       } else if (getTemplateId() == 1249) {
/*  1437 */         builder.append("clotted ");
/*       */       } 
/*       */     }
/*  1440 */     return builder.toString();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getActualName() {
/*  1449 */     return this.name;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isNamePlural() {
/*  1455 */     return this.template.isNamePlural();
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
/*       */   public final String getSignature() {
/*  1467 */     if (this.creator != null && this.creator.length() > 0 && getTemplateId() != 651) {
/*       */       
/*  1469 */       String toReturn = this.creator;
/*  1470 */       int ql = (int)getCurrentQualityLevel();
/*  1471 */       if (ql < 20)
/*  1472 */         return ""; 
/*  1473 */       if (ql < 90)
/*       */       {
/*  1475 */         toReturn = obscureWord(this.creator, ql);
/*       */       }
/*       */ 
/*       */       
/*  1479 */       return toReturn;
/*       */     } 
/*  1481 */     return this.creator;
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
/*       */   public static String obscureWord(String word, int ql) {
/*  1495 */     int containfactor = ql / 10;
/*  1496 */     char[] cword = word.toCharArray();
/*  1497 */     Random r = new Random();
/*  1498 */     r.setSeed(102539L);
/*  1499 */     for (int x = 0; x < word.length(); x++) {
/*       */       
/*  1501 */       if (r.nextInt(containfactor) > 0) {
/*       */         
/*  1503 */         cword[x] = word.charAt(x);
/*       */       }
/*       */       else {
/*       */         
/*  1507 */         cword[x] = '.';
/*       */       } 
/*       */     } 
/*  1510 */     return String.valueOf(cword);
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
/*       */   public String getNameWithGenus() {
/*  1523 */     return StringUtilities.addGenus(getName(), isNamePlural());
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
/*       */   public final float getNutritionLevel() {
/*  1538 */     boolean hasBonus = ((this.template.hasFoodBonusWhenCold() && this.temperature < 300) || (this.template.hasFoodBonusWhenHot() && this.temperature > 1000));
/*  1539 */     float ql = getCurrentQualityLevel();
/*       */     
/*  1541 */     if (isHighNutrition()) {
/*  1542 */       return 0.56F + ql / 300.0F + (hasBonus ? 0.09F : 0.0F);
/*       */     }
/*  1544 */     if (isGoodNutrition()) {
/*  1545 */       return 0.4F + ql / 500.0F + (hasBonus ? 0.1F : 0.0F);
/*       */     }
/*  1547 */     if (isMediumNutrition()) {
/*  1548 */       return 0.3F + ql / 1000.0F + (hasBonus ? 0.1F : 0.0F);
/*       */     }
/*  1550 */     if (isLowNutrition()) {
/*  1551 */       return 0.1F + ql / 1000.0F + (hasBonus ? 0.1F : 0.0F);
/*       */     }
/*       */     
/*  1554 */     return 0.01F + ql / 1000.0F + (hasBonus ? 0.05F : 0.0F);
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getTowerModel() {
/*  1559 */     byte kingdomTemplateId = getAuxData();
/*       */     
/*  1561 */     if (getAuxData() < 0 || getAuxData() > 4) {
/*       */       
/*  1563 */       Kingdom k = Kingdoms.getKingdom(getAuxData());
/*  1564 */       if (k != null)
/*       */       {
/*  1566 */         kingdomTemplateId = k.getTemplate();
/*       */       }
/*       */     } 
/*  1569 */     if (kingdomTemplateId == 3)
/*  1570 */       return 430; 
/*  1571 */     if (kingdomTemplateId == 2)
/*  1572 */       return 528; 
/*  1573 */     if (kingdomTemplateId == 4)
/*  1574 */       return 638; 
/*  1575 */     return 384;
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
/*       */   public final String getModelName() {
/*  1591 */     StringBuilder builder = new StringBuilder();
/*       */     
/*  1593 */     int templateId = this.template.getTemplateId();
/*  1594 */     if (templateId == 177) {
/*       */       
/*  1596 */       int lData = getData1();
/*       */       
/*       */       try {
/*  1599 */         if (lData != -1)
/*       */         {
/*  1601 */           builder.append(ItemTemplateFactory.getInstance().getTemplate(lData).getName());
/*  1602 */           builder.append(".");
/*  1603 */           builder.append(getMaterialString(getMaterial()));
/*  1604 */           StringBuilder b2 = new StringBuilder();
/*       */ 
/*       */           
/*  1607 */           b2.append(this.template.getModelName());
/*       */ 
/*       */ 
/*       */           
/*  1611 */           b2.append(builder.toString().replaceAll(" ", ""));
/*  1612 */           return b2.toString();
/*       */         }
/*       */       
/*  1615 */       } catch (NoSuchTemplateException nst) {
/*       */         
/*  1617 */         logInfo("Inconsistency: " + lData + " does not exist as templateid.");
/*       */       } 
/*       */     } else {
/*  1620 */       if (isDragonArmour()) {
/*       */         
/*  1622 */         builder.append(getTemplate().getModelName());
/*  1623 */         String matString = MaterialUtilities.getMaterialString(getMaterial()) + ".";
/*  1624 */         builder.append(matString);
/*  1625 */         String text = getDragonColorNameByColor(getColor());
/*  1626 */         builder.append(text);
/*  1627 */         return builder.toString();
/*       */       } 
/*  1629 */       if (templateId == 854) {
/*       */         
/*  1631 */         builder.append(getTemplate().getModelName());
/*  1632 */         String text = getAuxData() + ".";
/*  1633 */         builder.append(text);
/*  1634 */         return builder.toString();
/*       */       } 
/*  1636 */       if (templateId == 385) {
/*       */         
/*  1638 */         builder.append("model.fallen.");
/*  1639 */         builder.append(TileTreeBehaviour.getTreenameForMaterial(getMaterial()));
/*  1640 */         if (this.auxbyte >= 100)
/*  1641 */           builder.append(".animatedfalling"); 
/*  1642 */         builder.append(".seasoncycle");
/*  1643 */         return builder.toString();
/*       */       } 
/*  1645 */       if (templateId == 386) {
/*       */         
/*       */         try
/*       */         {
/*  1649 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate);
/*  1650 */           if (this.template.wood) {
/*       */             
/*  1652 */             builder.append(temp.getModelName());
/*  1653 */             builder.append("unfinished.");
/*  1654 */             builder.append(getMaterialString(getMaterial()));
/*  1655 */             return builder.toString();
/*       */           } 
/*       */ 
/*       */           
/*  1659 */           builder.append(temp.getModelName());
/*       */ 
/*       */           
/*  1662 */           builder.append("unfinished.");
/*  1663 */           builder.append(getMaterialString(getMaterial()));
/*       */           
/*  1665 */           return builder.toString();
/*       */         
/*       */         }
/*  1668 */         catch (NoSuchTemplateException nst)
/*       */         {
/*  1670 */           logWarn(this.realTemplate + ": " + nst.getMessage(), (Throwable)nst);
/*       */         }
/*       */       
/*  1673 */       } else if (templateId == 179) {
/*       */ 
/*       */         
/*       */         try {
/*  1677 */           int tempmodel = AdvancedCreationEntry.getTemplateId(this);
/*       */           
/*  1679 */           if (tempmodel == 384)
/*       */           {
/*  1681 */             tempmodel = getTowerModel();
/*       */           }
/*  1683 */           ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(tempmodel);
/*       */           
/*  1685 */           if (temp.isTapestry()) {
/*       */             
/*  1687 */             builder.append("model.furniture.tapestry.unfinished");
/*  1688 */             return builder.toString();
/*       */           } 
/*       */           
/*  1691 */           if (temp.wood) {
/*       */             
/*  1693 */             builder.append(temp.getModelName());
/*  1694 */             builder.append("unfinished.");
/*  1695 */             if (isVisibleDecay())
/*       */             {
/*  1697 */               if (this.damage >= 50.0F) {
/*  1698 */                 builder.append("decayed.");
/*  1699 */               } else if (this.damage >= 25.0F) {
/*  1700 */                 builder.append("worn.");
/*       */               }  } 
/*  1702 */             builder.append(getMaterialString(getMaterial()));
/*       */             
/*  1704 */             return builder.toString();
/*       */           } 
/*       */ 
/*       */           
/*  1708 */           builder.append(temp.getModelName());
/*       */ 
/*       */           
/*  1711 */           builder.append("unfinished.");
/*  1712 */           if (isVisibleDecay())
/*       */           {
/*  1714 */             if (this.damage >= 50.0F) {
/*  1715 */               builder.append("decayed.");
/*  1716 */             } else if (this.damage >= 25.0F) {
/*  1717 */               builder.append("worn.");
/*       */             }  } 
/*  1719 */           builder.append(getMaterialString(getMaterial()));
/*  1720 */           return builder.toString();
/*       */         
/*       */         }
/*  1723 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  1725 */           logWarn(this.realTemplate + ": " + nst.getMessage(), (Throwable)nst);
/*       */         } 
/*       */       } else {
/*  1728 */         if (templateId == 1307) {
/*       */           
/*  1730 */           builder.append(this.template.getModelName());
/*  1731 */           if (getData1() > 0) {
/*       */             
/*  1733 */             if (getMaterial() == 93) {
/*  1734 */               builder.append("iron");
/*  1735 */             } else if (getMaterial() == 94 || getMaterial() == 95) {
/*  1736 */               builder.append("steel");
/*       */             } else {
/*  1738 */               builder.append(getMaterialString(getMaterial()));
/*       */             } 
/*       */           } else {
/*  1741 */             builder.append("unidentified.");
/*       */           } 
/*  1743 */           return builder.toString();
/*       */         } 
/*  1745 */         if (templateId == 1346) {
/*       */           
/*  1747 */           builder.append(this.template.getModelName());
/*  1748 */           Item reel = getFishingReel();
/*  1749 */           if (reel != null)
/*       */           {
/*  1751 */             if (reel.getTemplateId() == 1372) {
/*  1752 */               builder.append("light.");
/*  1753 */             } else if (reel.getTemplateId() == 1373) {
/*  1754 */               builder.append("medium.");
/*  1755 */             } else if (reel.getTemplateId() == 1374) {
/*  1756 */               builder.append("deepwater.");
/*  1757 */             } else if (reel.getTemplateId() == 1375) {
/*  1758 */               builder.append("professional.");
/*       */             }  } 
/*  1760 */           builder.append(getMaterialString(getMaterial()));
/*  1761 */           return builder.toString();
/*       */         } 
/*  1763 */         if (templateId == 272)
/*       */         
/*       */         { 
/*       */           try {
/*  1767 */             CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(getData1());
/*  1768 */             builder.append(this.template.getModelName());
/*  1769 */             builder.append(temp.getCorpsename());
/*  1770 */             if (getDescription().length() > 0) {
/*       */               
/*  1772 */               if (getDescription().contains("[")) {
/*       */ 
/*       */                 
/*  1775 */                 String desc = getDescription().replace(" ", "");
/*  1776 */                 desc = desc.substring(0, desc.indexOf("["));
/*  1777 */                 builder.append(desc);
/*       */               } else {
/*       */                 
/*  1780 */                 builder.append(getDescription().replace(" ", ""));
/*  1781 */               }  builder.append(".");
/*       */             } 
/*  1783 */             if (isButchered())
/*  1784 */               builder.append("butchered."); 
/*  1785 */             if (this.female)
/*  1786 */               builder.append("female."); 
/*  1787 */             Kingdom k = Kingdoms.getKingdom(getKingdom());
/*  1788 */             if (k != null && k.getTemplate() != getKingdom())
/*  1789 */               builder.append(Kingdoms.getSuffixFor(k.getTemplate())); 
/*  1790 */             builder.append(Kingdoms.getSuffixFor(getKingdom()));
/*  1791 */             builder.append(WurmCalendar.getSpecialMapping(false));
/*  1792 */             return builder.toString();
/*       */           }
/*  1794 */           catch (NoSuchCreatureTemplateException noSuchCreatureTemplateException) {}
/*       */            }
/*       */         
/*       */         else
/*       */         
/*       */         { 
/*  1800 */           if (templateId == 853 || isWagonerWagon() || templateId == 1410) {
/*       */             
/*  1802 */             builder.append(getTemplate().getModelName());
/*  1803 */             if (isWagonerWagon())
/*  1804 */               builder.append("wagoner."); 
/*  1805 */             if (getItemCount() > 0) {
/*  1806 */               builder.append("loaded.");
/*       */             } else {
/*  1808 */               builder.append("unloaded.");
/*  1809 */             }  builder.append(WurmCalendar.getSpecialMapping(false));
/*  1810 */             builder.append(getMaterialString(getMaterial()));
/*  1811 */             return builder.toString();
/*       */           } 
/*  1813 */           if (templateId == 651 || templateId == 1097 || templateId == 1098) {
/*       */             
/*  1815 */             builder.append(getTemplate().getModelName());
/*  1816 */             switch (this.auxbyte)
/*       */             
/*       */             { case 0:
/*  1819 */                 builder.append("green");
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  1837 */                 return builder.toString();case 1: builder.append("blue"); return builder.toString();case 2: builder.append("striped"); return builder.toString();case 3: builder.append("candy"); return builder.toString();case 4: builder.append("holly"); return builder.toString(); }  builder.append("green"); return builder.toString();
/*       */           } 
/*  1839 */           if (isSign()) {
/*       */             
/*  1841 */             builder.append(getTemplate().getModelName());
/*  1842 */             if (isVisibleDecay())
/*       */             {
/*  1844 */               if (this.damage >= 50.0F) {
/*  1845 */                 builder.append("decayed.");
/*  1846 */               } else if (this.damage >= 25.0F) {
/*  1847 */                 builder.append("worn.");
/*       */               } 
/*       */             }
/*       */             
/*  1851 */             if (getTemplateId() == 656) {
/*       */               
/*  1853 */               builder.append(this.auxbyte);
/*  1854 */               builder.append('.');
/*       */             } 
/*  1856 */             builder.append(getMaterialString(getMaterial()));
/*  1857 */             return builder.toString();
/*       */           } 
/*  1859 */           if (templateId == 521)
/*       */           
/*       */           { 
/*       */             try {
/*  1863 */               CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(getData1());
/*  1864 */               builder.append(this.template.getModelName());
/*  1865 */               builder.append(temp.getCorpsename());
/*  1866 */               builder.append(WurmCalendar.getSpecialMapping(false));
/*  1867 */               return builder.toString();
/*       */             }
/*  1869 */             catch (NoSuchCreatureTemplateException noSuchCreatureTemplateException) {}
/*       */ 
/*       */ 
/*       */             
/*       */              }
/*       */           
/*  1875 */           else if (templateId == 387)
/*       */           
/*       */           { 
/*       */             try {
/*  1879 */               ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate);
/*  1880 */               if (this.template.wood) {
/*       */                 
/*  1882 */                 builder.append(temp.getModelName());
/*  1883 */                 if (isVisibleDecay())
/*       */                 {
/*  1885 */                   if (this.damage >= 50.0F) {
/*  1886 */                     builder.append("decayed.");
/*  1887 */                   } else if (this.damage >= 25.0F) {
/*  1888 */                     builder.append("worn.");
/*       */                   }  } 
/*  1890 */                 builder.append(getMaterialString(getMaterial()));
/*  1891 */                 return builder.toString();
/*       */               } 
/*       */ 
/*       */               
/*  1895 */               builder.append(temp.getModelName());
/*  1896 */               builder.append(getMaterialString(getMaterial()));
/*  1897 */               return builder.toString();
/*       */             
/*       */             }
/*  1900 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  1902 */               logWarn(this.realTemplate + ": " + nst.getMessage(), (Throwable)nst);
/*       */             }  }
/*       */           else
/*  1905 */           { if (templateId == 791) {
/*       */               
/*  1907 */               builder.append(this.template.getModelName());
/*  1908 */               builder.append(getMaterialString(getMaterial()));
/*  1909 */               if (WurmCalendar.isChristmas()) {
/*  1910 */                 builder.append(".red");
/*       */               } else {
/*  1912 */                 builder.append(".grey");
/*  1913 */               }  return builder.toString();
/*       */             } 
/*  1915 */             if (templateId == 518) {
/*       */               
/*  1917 */               builder.append(this.template.getModelName());
/*  1918 */               builder.append(getMaterialString(getMaterial()));
/*  1919 */               builder.append(WurmCalendar.getSpecialMapping(true));
/*  1920 */               builder.append(".");
/*  1921 */               builder.append(getDescription().toLowerCase());
/*  1922 */               return builder.toString().replaceAll(" ", "");
/*       */             } 
/*  1924 */             if (templateId == 538) {
/*       */               
/*  1926 */               builder.append(this.template.getModelName());
/*  1927 */               if (King.getKing((byte)2) == null)
/*  1928 */                 builder.append("occupied."); 
/*  1929 */               return builder.toString();
/*       */             } 
/*  1931 */             if (isItemSpawn())
/*       */             
/*  1933 */             { builder.append(this.template.getModelName());
/*  1934 */               builder.append(getAuxData());
/*  1935 */               return builder.toString(); }  }  } 
/*       */       } 
/*  1937 */     }  if (isWind() || this.template.isKingdomFlag || isProtectionTower()) {
/*       */       
/*  1939 */       builder.append(this.template.getModelName());
/*  1940 */       if (getKingdom() != 0)
/*       */       {
/*  1942 */         builder.append(Kingdoms.getSuffixFor(getKingdom()));
/*       */       }
/*  1944 */       return builder.toString();
/*       */     } 
/*  1946 */     if (isTent() || this.template.useMaterialAndKingdom) {
/*       */       
/*  1948 */       builder.append(this.template.getModelName());
/*  1949 */       builder.append(getMaterialString(getMaterial()));
/*  1950 */       builder.append(".");
/*  1951 */       if (getKingdom() != 0)
/*       */       {
/*  1953 */         builder.append(Kingdoms.getSuffixFor(getKingdom()));
/*       */       }
/*  1955 */       return builder.toString();
/*       */     } 
/*  1957 */     if (this.template.templateId == 850) {
/*       */       
/*  1959 */       builder.append(this.template.getModelName());
/*  1960 */       if (getData1() != 0)
/*  1961 */         builder.append(Kingdoms.getSuffixFor((byte)getData1())); 
/*  1962 */       builder.append(WurmCalendar.getSpecialMapping(true));
/*  1963 */       builder.append(getMaterialString(getMaterial()));
/*  1964 */       return builder.toString();
/*       */     } 
/*  1966 */     if (isFire()) {
/*       */       
/*  1968 */       builder.append(this.template.getModelName());
/*  1969 */       String cook = "";
/*  1970 */       boolean foundCook = false;
/*  1971 */       String meat = "";
/*  1972 */       boolean foundMeat = false;
/*  1973 */       String spit = "";
/*  1974 */       for (Item i : getItems()) {
/*       */         
/*  1976 */         if (i.isFoodMaker() && !foundCook) {
/*       */           
/*  1978 */           cook = i.getConcatName();
/*  1979 */           foundCook = true;
/*       */         } 
/*  1981 */         if ((i.getTemplateId() == 368 || i.getTemplateId() == 92) && !foundMeat) {
/*       */           
/*  1983 */           meat = "meat";
/*  1984 */           foundMeat = true;
/*       */         } 
/*  1986 */         if (i.getTemplateId() == 369 && !foundMeat) {
/*       */           
/*  1988 */           meat = "fish.fillet";
/*  1989 */           foundMeat = true;
/*       */         } 
/*  1991 */         if (i.isFish() && !foundMeat) {
/*       */           
/*  1993 */           meat = "fish";
/*  1994 */           foundMeat = true;
/*       */         } 
/*  1996 */         if (i.getTemplate().getModelName().contains(".spit.")) {
/*       */           
/*  1998 */           spit = i.getTemplate().getModelName().substring(11);
/*  1999 */           if (i.isRoasted())
/*  2000 */             spit = spit + "roasted."; 
/*       */         } 
/*       */       } 
/*  2003 */       if (spit.length() > 0) {
/*       */         
/*  2005 */         builder.append(spit);
/*       */       
/*       */       }
/*  2008 */       else if (foundCook) {
/*       */         
/*  2010 */         builder.append(cook);
/*       */       }
/*  2012 */       else if (foundMeat) {
/*       */         
/*  2014 */         builder.append(meat);
/*  2015 */         builder.append(".");
/*       */       } 
/*  2017 */       if (!isOnFire())
/*  2018 */         builder.append("unlit"); 
/*  2019 */       return builder.toString();
/*       */     } 
/*  2021 */     if (getTemplateId() == 1301) {
/*       */       
/*  2023 */       builder.append(this.template.getModelName());
/*       */       
/*  2025 */       switch (getAuxData()) {
/*       */ 
/*       */         
/*       */         case 1:
/*       */         case 2:
/*       */         case 11:
/*       */         case 16:
/*  2032 */           builder.append("meat.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 3:
/*  2037 */           builder.append("fish.fillet.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 4:
/*       */         case 5:
/*       */         case 14:
/*       */         case 19:
/*  2045 */           builder.append("fish.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 6:
/*       */         case 7:
/*  2051 */           builder.append("fryingpan.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 8:
/*       */         case 9:
/*       */         case 10:
/*  2058 */           builder.append("potterybowl.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 12:
/*       */         case 13:
/*  2064 */           builder.append("spit.pig.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 15:
/*  2069 */           builder.append("spit.lamb.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 17:
/*       */         case 18:
/*  2075 */           builder.append("spit.pig.roasted.");
/*       */           break;
/*       */ 
/*       */         
/*       */         case 20:
/*  2080 */           builder.append("spit.lamb.roasted.");
/*       */           break;
/*       */       } 
/*       */       
/*  2084 */       if (!isOnFire())
/*  2085 */         builder.append("unlit"); 
/*  2086 */       return builder.toString();
/*       */     } 
/*  2088 */     if (isRoadMarker()) {
/*       */ 
/*       */       
/*  2091 */       builder.append(this.template.getModelName());
/*  2092 */       if (this.template.templateId == 1114) {
/*       */         
/*  2094 */         int possibleRoutes = MethodsHighways.numberOfSetBits(getAuxData());
/*  2095 */         if (possibleRoutes == 1) {
/*  2096 */           builder.append("red.");
/*  2097 */         } else if (possibleRoutes == 2) {
/*       */           
/*  2099 */           if (Routes.isCatseyeUsed(this)) {
/*  2100 */             builder.append("green.");
/*       */           } else {
/*  2102 */             builder.append("blue.");
/*       */           } 
/*       */         } 
/*  2105 */       }  return builder.toString();
/*       */     } 
/*  2107 */     if (this.template.templateId == 1342) {
/*       */       
/*  2109 */       builder.append(this.template.getModelName());
/*  2110 */       if (isPlanted())
/*       */       {
/*  2112 */         builder.append("planted.");
/*       */       }
/*  2114 */       builder.append(WurmCalendar.getSpecialMapping(false));
/*  2115 */       builder.append(getMaterialString(getMaterial()) + ".");
/*       */       
/*  2117 */       return builder.toString();
/*       */     } 
/*  2119 */     if (this.template.wood) {
/*       */       
/*  2121 */       builder.append(this.template.getModelName());
/*  2122 */       if (getTemplateId() == 1432) {
/*       */         
/*  2124 */         boolean chicks = false;
/*  2125 */         boolean eggs = false;
/*  2126 */         for (Item item : getAllItems(true)) {
/*       */ 
/*       */           
/*  2129 */           if (item.getTemplateId() == 1436)
/*       */           {
/*  2131 */             if (!item.isEmpty(true)) {
/*       */               
/*  2133 */               chicks = true;
/*       */             }
/*       */             else {
/*       */               
/*  2137 */               chicks = false;
/*       */             } 
/*       */           }
/*       */           
/*  2141 */           if (item.getTemplateId() == 1433)
/*       */           {
/*  2143 */             if (!item.isEmpty(true)) {
/*       */               
/*  2145 */               eggs = true;
/*       */             }
/*       */             else {
/*       */               
/*  2149 */               eggs = false;
/*       */             } 
/*       */           }
/*       */         } 
/*  2153 */         if (eggs && chicks)
/*       */         {
/*  2155 */           builder.append("chicken.egg.");
/*       */         }
/*  2157 */         if (!eggs && chicks)
/*       */         {
/*  2159 */           builder.append("chicken.");
/*       */         }
/*  2161 */         if (eggs && !chicks)
/*       */         {
/*  2163 */           builder.append("egg.");
/*       */         }
/*  2165 */         if (!eggs && !chicks)
/*       */         {
/*  2167 */           builder.append("empty.");
/*       */         }
/*  2169 */         if (isUnfinished())
/*       */         {
/*  2171 */           builder.append("unfinished.");
/*       */         }
/*  2173 */         if (this.damage >= 60.0F)
/*       */         {
/*  2175 */           builder.append("decayed.");
/*       */         }
/*       */       } 
/*  2178 */       if (getTemplateId() == 1311) {
/*       */         
/*  2180 */         if (isEmpty(true) && !isUnfinished())
/*       */         {
/*  2182 */           setAuxData((byte)0);
/*       */         }
/*  2184 */         switch (getAuxData()) {
/*       */           
/*       */           case 0:
/*  2187 */             builder.append("empty.");
/*       */             break;
/*       */           case 64:
/*  2190 */             builder.append("horse.");
/*       */             break;
/*       */           case 82:
/*  2193 */             builder.append("bison.");
/*       */             break;
/*       */           case 83:
/*  2196 */             builder.append("hellhorse.");
/*       */             break;
/*       */           case 3:
/*  2199 */             builder.append("cow.");
/*       */             break;
/*       */           case 49:
/*  2202 */             builder.append("bull.");
/*       */             break;
/*       */           case 102:
/*  2205 */             builder.append("ram.");
/*       */             break;
/*       */           case 65:
/*  2208 */             builder.append("foal.");
/*       */             break;
/*       */           case 96:
/*  2211 */             builder.append("sheep.");
/*       */             break;
/*       */           case 117:
/*  2214 */             builder.append("hellhorse.foal.");
/*       */             break;
/*       */           default:
/*  2217 */             builder.append("generic.");
/*       */             break;
/*       */         } 
/*  2220 */         if (isEmpty(true))
/*       */         {
/*  2222 */           builder.append("empty.");
/*       */         }
/*  2224 */         if (isUnfinished())
/*       */         {
/*  2226 */           builder.append("unfinished.");
/*       */         }
/*  2228 */         if (this.damage >= 60.0F)
/*       */         {
/*  2230 */           builder.append("decayed.");
/*       */         }
/*       */       } 
/*       */       
/*  2234 */       if ((templateId == 1309 || isCrate()) && (
/*  2235 */         isSealedOverride() || isSealedByPlayer())) {
/*  2236 */         builder.append("sealed.");
/*       */       }
/*  2238 */       if (isBulkContainer() || getTemplateId() == 670)
/*       */       {
/*  2240 */         if (!isCrate()) {
/*       */           
/*  2242 */           if (isFull()) {
/*  2243 */             builder.append("full.");
/*       */           } else {
/*  2245 */             builder.append("empty.");
/*       */           }
/*       */         
/*       */         }
/*  2249 */         else if (getItemCount() > 0) {
/*  2250 */           builder.append("full.");
/*       */         } else {
/*  2252 */           builder.append("empty.");
/*       */         } 
/*       */       }
/*  2255 */       if (templateId == 724 || templateId == 725 || templateId == 758 || templateId == 759 || 
/*  2256 */         isBarrelRack() || templateId == 1312 || templateId == 1309 || templateId == 1315 || templateId == 1393)
/*       */       {
/*       */ 
/*       */         
/*  2260 */         if (isEmpty(false)) {
/*  2261 */           builder.append("empty.");
/*       */         } else {
/*  2263 */           builder.append("full.");
/*       */         }  } 
/*  2265 */       if (templateId == 580)
/*       */       {
/*  2267 */         if (isMerchantAbsentOrEmpty())
/*  2268 */           builder.append("empty."); 
/*       */       }
/*  2270 */       if ((templateId == 1239 || templateId == 1175) && getAuxData() > 0)
/*       */       {
/*  2272 */         if (!WurmCalendar.isSeasonWinter())
/*  2273 */           builder.append("queen."); 
/*       */       }
/*  2275 */       if (isHarvestable())
/*  2276 */         builder.append("harvestable."); 
/*  2277 */       if (isVisibleDecay())
/*       */       {
/*  2279 */         if (this.damage >= 50.0F) {
/*  2280 */           builder.append("decayed.");
/*  2281 */         } else if (this.damage >= 25.0F) {
/*  2282 */           builder.append("worn.");
/*       */         }  } 
/*  2284 */       if (this.damage >= 80.0F && templateId == 321)
/*  2285 */         builder.append("decay."); 
/*  2286 */       if (templateId == 1396 && isPlanted()) {
/*       */         
/*  2288 */         builder.append("planted.");
/*  2289 */         if (isOnFire())
/*  2290 */           builder.append("lit."); 
/*       */       } 
/*  2292 */       builder.append(WurmCalendar.getSpecialMapping(false));
/*  2293 */       builder.append(getMaterialString(getMaterial()) + ".");
/*       */       
/*  2295 */       return builder.toString();
/*       */     } 
/*  2297 */     if (isDuelRing()) {
/*       */       
/*  2299 */       builder.append(this.template.getModelName());
/*  2300 */       builder.append(getMaterialString(getMaterial()));
/*  2301 */       builder.append(".");
/*  2302 */       if (getKingdom() != 0)
/*       */       {
/*  2304 */         builder.append(Kingdoms.getSuffixFor(getKingdom()));
/*       */       }
/*  2306 */       return builder.toString();
/*       */     } 
/*  2308 */     if (getTemplateId() == 742) {
/*       */       
/*  2310 */       String hotaModel = "model.decoration.statue.hota.";
/*  2311 */       builder.append("model.decoration.statue.hota.");
/*  2312 */       switch (getAuxData() % 10)
/*       */       
/*       */       { case 0:
/*  2315 */           if (getData1() == 1) {
/*  2316 */             builder.append("femalefightinganaconda.");
/*       */           } else {
/*  2318 */             builder.append("dogsfightingboar.");
/*       */           } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  2360 */           builder.append(WurmCalendar.getSpecialMapping(false));
/*  2361 */           builder.append(getMaterialString(getMaterial()));
/*  2362 */           return builder.toString();case -1: case 1: builder.append("wolffightingbison."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -2: case 2: builder.append("deer."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -3: case 3: builder.append("bearfightingbull."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -4: case 4: builder.append("blackdragon."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -5: case 5: builder.append("ladylake."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -6: case 6: builder.append("nogump."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -7: case 7: builder.append("manfightingbear."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -8: case 8: builder.append("soldemon."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();case -9: case 9: builder.append("scorpion."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString(); }  builder.append("dogsfightingboar."); builder.append(WurmCalendar.getSpecialMapping(false)); builder.append(getMaterialString(getMaterial())); return builder.toString();
/*       */     } 
/*  2364 */     if (getTemplateId() == 821 || getTemplateId() == 822) {
/*       */       
/*  2366 */       builder.append(this.template.getModelName());
/*  2367 */       if (this.damage >= 50.0F)
/*  2368 */         builder.append("decayed"); 
/*  2369 */       return builder.toString();
/*       */     } 
/*  2371 */     if (getTemplateId() == 302) {
/*       */       
/*  2373 */       builder.append(this.template.getModelName());
/*       */       
/*  2375 */       if (getName().equalsIgnoreCase("Black bear fur")) {
/*  2376 */         builder.append("blackbear");
/*  2377 */       } else if (getName().equalsIgnoreCase("Brown bear fur")) {
/*  2378 */         builder.append("brownbear");
/*  2379 */       } else if (getName().equalsIgnoreCase("Black wolf fur")) {
/*  2380 */         builder.append("wolf");
/*       */       } else {
/*  2382 */         builder.append(getMaterialString(getMaterial()));
/*  2383 */       }  builder.append(".");
/*  2384 */       return builder.toString();
/*       */     } 
/*  2386 */     if (getTemplateId() == 1162) {
/*       */       
/*  2388 */       builder.append(this.template.getModelName());
/*  2389 */       ItemTemplate rt = getRealTemplate();
/*  2390 */       if (rt != null) {
/*       */         
/*  2392 */         builder.append(rt.getName().replace(" ", ""));
/*       */         
/*  2394 */         int age = getAuxData() & Byte.MAX_VALUE;
/*  2395 */         if (age == 0) {
/*  2396 */           builder.append(".0");
/*  2397 */         } else if (age < 5) {
/*  2398 */           builder.append(".1");
/*  2399 */         } else if (age < 10) {
/*  2400 */           builder.append(".2");
/*  2401 */         } else if (age < 65) {
/*  2402 */           builder.append(".3");
/*  2403 */         } else if (age < 75) {
/*  2404 */           builder.append(".4");
/*  2405 */         } else if (age < 95) {
/*  2406 */           builder.append(".5");
/*       */         } else {
/*  2408 */           builder.append(".6");
/*       */         } 
/*       */       } 
/*  2411 */       return builder.toString();
/*       */     } 
/*       */ 
/*       */     
/*  2415 */     builder.append(this.template.getModelName());
/*  2416 */     String rtName = "";
/*  2417 */     if (getRealTemplateId() != -10L && !isLight())
/*  2418 */       rtName = getRealTemplate().getName() + ".".replace(" ", ""); 
/*  2419 */     if (usesFoodState()) {
/*       */       
/*  2421 */       switch (getRightAuxData()) {
/*       */         
/*       */         case 1:
/*  2424 */           builder.append("fried.");
/*       */           break;
/*       */         case 2:
/*  2427 */           builder.append("grilled.");
/*       */           break;
/*       */         case 3:
/*  2430 */           builder.append("boiled.");
/*       */           break;
/*       */         case 4:
/*  2433 */           builder.append("roasted.");
/*       */           break;
/*       */         case 5:
/*  2436 */           builder.append("steamed.");
/*       */           break;
/*       */         case 6:
/*  2439 */           builder.append("baked.");
/*       */           break;
/*       */         case 7:
/*  2442 */           builder.append("cooked.");
/*       */           break;
/*       */         case 8:
/*  2445 */           builder.append("candied.");
/*       */           break;
/*       */         case 9:
/*  2448 */           builder.append("chocolate.");
/*       */           break;
/*       */       } 
/*  2451 */       if (isChoppedBitSet()) {
/*       */         
/*  2453 */         if (isHerb() || isVegetable() || isFish() || this.template.isMushroom) {
/*  2454 */           builder.append("chopped.");
/*  2455 */         } else if (isMeat()) {
/*  2456 */           builder.append("diced.");
/*  2457 */         } else if (isSpice()) {
/*  2458 */           builder.append("ground.");
/*  2459 */         } else if (canBeFermented()) {
/*  2460 */           builder.append("unfermented.");
/*  2461 */         } else if (getTemplateId() == 1249) {
/*  2462 */           builder.append("whipped.");
/*       */         } else {
/*  2464 */           builder.append("zombified.");
/*       */         } 
/*  2466 */       } else if (isMashedBitSet()) {
/*       */         
/*  2468 */         if (isMeat()) {
/*  2469 */           builder.append("minced.");
/*  2470 */         } else if (isVegetable()) {
/*  2471 */           builder.append("mashed.");
/*  2472 */         } else if (canBeFermented()) {
/*  2473 */           builder.append("fermenting.");
/*  2474 */         } else if (getTemplateId() == 1249) {
/*  2475 */           builder.append("clotted.");
/*  2476 */         } else if (isFish()) {
/*  2477 */           builder.append("underweight.");
/*       */         } 
/*  2479 */       }  if (isWrappedBitSet())
/*       */       {
/*  2481 */         if (canBeDistilled()) {
/*  2482 */           builder.append("undistilled.");
/*       */         } else {
/*  2484 */           builder.append("wrapped.");
/*       */         }  } 
/*  2486 */       if (isFreshBitSet()) {
/*       */         
/*  2488 */         if (isHerb() || isSpice())
/*  2489 */           builder.append("fresh."); 
/*  2490 */         if (isFish()) {
/*  2491 */           builder.append("live.");
/*       */         }
/*       */       } 
/*  2494 */       builder.append(rtName);
/*  2495 */       builder.append(getMaterialString(getMaterial()));
/*       */     }
/*  2497 */     else if (getTemplateId() == 1281) {
/*       */ 
/*       */       
/*  2500 */       builder.append(rtName);
/*  2501 */       builder.append(getMaterialString(getMaterial()));
/*       */     }
/*  2503 */     else if (getTemplateId() == 729) {
/*       */ 
/*       */       
/*  2506 */       if (getAuxData() > 0)
/*       */       {
/*       */ 
/*       */         
/*  2510 */         builder.append("birthday.");
/*       */       }
/*       */       
/*  2513 */       builder.append(rtName);
/*  2514 */       builder.append(getMaterialString(getMaterial()));
/*       */     
/*       */     }
/*       */     else {
/*       */       
/*  2519 */       builder.append(rtName);
/*  2520 */       builder.append(getMaterialString(getMaterial()));
/*  2521 */       if ((templateId == 178 || templateId == 180 || isFireplace() || 
/*  2522 */         isBrazier() || templateId == 1178 || templateId == 1301) && 
/*  2523 */         isOnFire()) {
/*       */         
/*  2525 */         builder.append(".lit");
/*       */       }
/*  2527 */       else if (templateId == 1243 && isOnFire()) {
/*       */         
/*  2529 */         builder.append(".smoke");
/*       */       } 
/*       */     } 
/*  2532 */     return builder.toString();
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
/*       */   public boolean isFull() {
/*  2544 */     return (getFreeVolume() < getVolume() / 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean isMerchantAbsentOrEmpty() {
/*       */     try {
/*  2552 */       TilePos tilePos = getTilePos();
/*  2553 */       Zone zone = Zones.getZone(tilePos, this.surfaced);
/*  2554 */       VolaTile tile = zone.getTileOrNull(tilePos);
/*  2555 */       if (tile == null) {
/*       */         
/*  2557 */         logWarn("No tile found in zone.");
/*  2558 */         return true;
/*       */       } 
/*       */       
/*  2561 */       for (Creature creature : tile.getCreatures()) {
/*       */         
/*  2563 */         if (creature.isNpcTrader()) {
/*       */           
/*  2565 */           Shop shop = creature.getShop();
/*  2566 */           if (shop != null) {
/*  2567 */             return (shop.getOwnerId() != -10L && shop.getNumberOfItems() == 0);
/*       */           }
/*       */           break;
/*       */         } 
/*       */       } 
/*  2572 */     } catch (NoSuchZoneException nsze) {
/*       */       
/*  2574 */       logWarn(nsze.getMessage(), (Throwable)nsze);
/*       */     } 
/*  2576 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setBusy(boolean busy) {
/*  2585 */     this.isBusy = busy;
/*  2586 */     if (getTemplateId() == 1344 || getTemplateId() == 1346) {
/*       */ 
/*       */ 
/*       */       
/*  2590 */       setIsNoPut(busy);
/*  2591 */       Item[] fishingItems = getFishingItems();
/*  2592 */       if (fishingItems[0] != null) {
/*       */         
/*  2594 */         fishingItems[0].setBusy(busy);
/*  2595 */         fishingItems[0].setIsNoPut(busy);
/*       */       } 
/*  2597 */       if (fishingItems[1] != null) {
/*       */         
/*  2599 */         fishingItems[1].setBusy(busy);
/*  2600 */         fishingItems[1].setIsNoPut(busy);
/*       */       } 
/*  2602 */       if (fishingItems[2] != null)
/*       */       {
/*  2604 */         fishingItems[2].setBusy(busy);
/*       */       }
/*  2606 */       if (fishingItems[3] != null) {
/*       */         
/*  2608 */         fishingItems[3].setBusy(busy);
/*  2609 */         fishingItems[3].setIsNoPut(busy);
/*       */       } 
/*  2611 */       if (fishingItems[4] != null)
/*       */       {
/*  2613 */         fishingItems[4].setBusy(busy);
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final String getConcatName() {
/*  2620 */     return this.template.getConcatName();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBusy() {
/*  2629 */     return (this.isBusy || this.tradeWindow != null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getDifficulty() {
/*  2638 */     return this.template.getDifficulty();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasPrimarySkill() {
/*  2648 */     return this.template.hasPrimarySkill();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getPrimarySkill() throws NoSuchSkillException {
/*  2659 */     return this.template.getPrimarySkill();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final byte getAuxData() {
/*  2668 */     if (getTemplateId() == 621 && this.auxbyte == 0 && getItemCount() > 0)
/*       */     {
/*  2670 */       for (Item i : getItems()) {
/*  2671 */         if (i.getTemplateId() == 1333)
/*  2672 */           return 1; 
/*  2673 */         if (i.getTemplateId() == 1334)
/*  2674 */           return 2; 
/*       */       }  } 
/*  2676 */     return this.auxbyte;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final byte getActualAuxData() {
/*  2685 */     return this.auxbyte;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final short getTemperature() {
/*  2694 */     return this.temperature;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPurchased() {
/*  2699 */     return this.template.isPurchased();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void checkSaveDamage() {}
/*       */ 
/*       */ 
/*       */   
/*       */   public final void addEffect(Effect effect) {
/*  2709 */     if (this.effects == null)
/*  2710 */       this.effects = new HashSet<>(); 
/*  2711 */     if (!this.effects.contains(effect)) {
/*  2712 */       this.effects.add(effect);
/*       */     }
/*       */   }
/*       */   
/*       */   private void deleteEffect(Effect effect) {
/*  2717 */     if (this.effects != null && effect != null) {
/*       */       
/*  2719 */       this.effects.remove(effect);
/*  2720 */       EffectFactory.getInstance().deleteEffect(effect.getId());
/*  2721 */       if (this.effects.isEmpty()) {
/*  2722 */         this.effects = null;
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   public final void deleteAllEffects() {
/*  2728 */     if (this.effects != null)
/*       */     {
/*  2730 */       for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); ) {
/*       */         
/*  2732 */         Effect toremove = it.next();
/*       */         
/*  2734 */         if (toremove != null)
/*       */         {
/*  2736 */           EffectFactory.getInstance().deleteEffect(toremove.getId());
/*       */         }
/*       */       } 
/*       */     }
/*  2740 */     this.effects = null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nonnull
/*       */   public final Effect[] getEffects() {
/*  2750 */     if (this.effects != null)
/*       */     {
/*  2752 */       return this.effects.<Effect>toArray(new Effect[this.effects.size()]);
/*       */     }
/*  2754 */     return emptyEffects;
/*       */   }
/*       */ 
/*       */   
/*       */   private float getMaterialRepairTimeMod() {
/*  2759 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*       */     {
/*  2761 */       switch (getMaterial()) {
/*       */         
/*       */         case 56:
/*  2764 */           return 0.9F;
/*       */         case 31:
/*  2766 */           return 0.975F;
/*       */         case 10:
/*  2768 */           return 1.075F;
/*       */         case 57:
/*  2770 */           return 0.95F;
/*       */         case 7:
/*  2772 */           return 1.05F;
/*       */         case 12:
/*  2774 */           return 1.1F;
/*       */         case 67:
/*  2776 */           return 0.95F;
/*       */         case 9:
/*  2778 */           return 0.975F;
/*       */         case 34:
/*  2780 */           return 1.025F;
/*       */         case 13:
/*  2782 */           return 1.05F;
/*       */       } 
/*       */     }
/*  2785 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final short getRepairTime(Creature mender) {
/*  2791 */     int time = 32767;
/*  2792 */     Skills skills = mender.getSkills();
/*  2793 */     Skill repair = null;
/*       */     
/*       */     try {
/*  2796 */       repair = skills.getSkill(10035);
/*       */     }
/*  2798 */     catch (NoSuchSkillException nss) {
/*       */       
/*  2800 */       repair = skills.learn(10035, 1.0F);
/*       */     } 
/*       */     
/*  2803 */     if (repair == null)
/*  2804 */       return (short)time; 
/*  2805 */     float cq = getCurrentQualityLevel();
/*       */     
/*  2807 */     float diffcq = this.originalQualityLevel - cq;
/*  2808 */     float weightmod = 1.0F;
/*  2809 */     double diff = this.template.getDifficulty();
/*  2810 */     if (this.realTemplate > 0)
/*  2811 */       diff = getRealTemplate().getDifficulty(); 
/*  2812 */     if (getWeightGrams() > 100000)
/*  2813 */       weightmod = Math.min(3, getWeightGrams() / 100000); 
/*  2814 */     time = (int)Math.max(20.0D, ((this.damage + diffcq) * weightmod / 4.0F) * (100.0D - repair.getChance(diff, null, 0.0D)));
/*  2815 */     time = (int)(time * getMaterialRepairTimeMod());
/*       */ 
/*       */ 
/*       */     
/*  2819 */     time = Math.min(32767, time);
/*       */ 
/*       */ 
/*       */     
/*  2823 */     return (short)time;
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
/*       */   public final double repair(@Nonnull Creature mender, short aTimeleft, float initialDamage) {
/*  2836 */     float timeleft = (float)Math.max(1.0D, Math.floor((aTimeleft / 10.0F)));
/*  2837 */     Skills skills = mender.getSkills();
/*  2838 */     Skill repair = skills.getSkillOrLearn(10035);
/*  2839 */     double power = repair.skillCheck(getDifficulty(), 0.0D, false, 1.0F);
/*       */     
/*  2841 */     float cq = getCurrentQualityLevel();
/*       */     
/*  2843 */     float diffcq = Math.max(this.originalQualityLevel, this.qualityLevel) - cq;
/*  2844 */     float runeModifier = 1.0F;
/*  2845 */     if (getSpellEffects() != null)
/*       */     {
/*  2847 */       runeModifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_REPAIRQL);
/*       */     }
/*       */ 
/*       */     
/*  2851 */     float newOrigcq = getQualityLevel() - (float)(diffcq * (100.0D - power) / 125.0D) / timeleft / (getRarity() + 1.0F) * runeModifier;
/*  2852 */     setQualityLevel(Math.max(1.0F, newOrigcq));
/*  2853 */     setOriginalQualityLevel(Math.max(1.0F, newOrigcq));
/*       */     
/*  2855 */     float oldDamage = getDamage();
/*       */     
/*  2857 */     setDamage(this.damage - initialDamage / timeleft);
/*       */ 
/*       */     
/*  2860 */     sendUpdate();
/*       */     
/*  2862 */     if (isVisibleDecay())
/*       */     {
/*  2864 */       if ((this.damage < 50.0F && oldDamage >= 50.0F) || (this.damage < 25.0F && oldDamage >= 25.0F))
/*       */       {
/*       */         
/*  2867 */         updateModelNameOnGroundItem();
/*       */       }
/*       */     }
/*       */ 
/*       */     
/*  2872 */     return power;
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
/*       */   public final long getTopParent() {
/*  2885 */     if (getParentId() == -10L) {
/*  2886 */       return this.id;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/*  2891 */       if (getParentId() == this.id) {
/*       */         
/*  2893 */         logWarn(getName() + " has itself as parent!:" + this.id);
/*  2894 */         return this.id;
/*       */       } 
/*  2896 */       Item parent = Items.getItem(getParentId());
/*  2897 */       return parent.getTopParent();
/*       */     }
/*  2899 */     catch (NoSuchItemException nsi) {
/*       */ 
/*       */       
/*  2902 */       logWarn("Item " + this.id + "," + getName() + " has parentid " + getParentId() + " but that doesn't exist?", new Exception());
/*       */       
/*  2904 */       return -10L;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final Item getTopParentOrNull() {
/*  2911 */     long topId = getTopParent();
/*  2912 */     if (topId == -10L) {
/*  2913 */       return null;
/*       */     }
/*       */     try {
/*  2916 */       return Items.getItem(topId);
/*       */     }
/*  2918 */     catch (NoSuchItemException nsi) {
/*       */       
/*  2920 */       String message = StringUtil.format("Unable to find top parent with ID: %d.", new Object[] {
/*       */             
/*  2922 */             Long.valueOf(topId) });
/*  2923 */       logWarn(message, (Throwable)nsi);
/*  2924 */       return null;
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
/*       */   private boolean hasNoParent() {
/*  2941 */     return (this.parentId == -10L);
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean hasSameOwner(Item item) {
/*  2946 */     return (item.getOwnerId() == this.ownerId);
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
/*       */   public final int getValue() {
/*  2966 */     if (isCoin() || isFullprice())
/*       */     {
/*  2968 */       return this.template.getValue();
/*       */     }
/*       */ 
/*       */     
/*  2972 */     if (isChallengeNewbieItem())
/*  2973 */       return 0; 
/*  2974 */     int val = this.template.getValue();
/*  2975 */     if (isCombine()) {
/*       */       
/*  2977 */       float nums = getWeightGrams() / this.template.getWeightGrams();
/*  2978 */       val = (int)(nums * this.template.getValue() * getQualityLevel() * getQualityLevel() / 10000.0F * (100.0F - getDamage()) / 100.0F);
/*       */     } else {
/*       */       
/*  2981 */       val = (int)(this.template.getValue() * getQualityLevel() * getQualityLevel() / 10000.0F * (100.0F - getDamage()) / 100.0F);
/*       */     } 
/*       */ 
/*       */     
/*  2985 */     if (this.template.priceAffectedByMaterial)
/*       */     {
/*  2987 */       val = (int)(val * getMaterialPriceModifier());
/*       */     }
/*  2989 */     if (this.rarity > 0)
/*  2990 */       val *= this.rarity; 
/*  2991 */     return val;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private float getMaterialPriceModifier() {
/*  2997 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*       */       
/*  2999 */       switch (getMaterial()) {
/*       */         
/*       */         case 56:
/*  3002 */           return 8.0F;
/*       */         case 30:
/*  3004 */           return 6.0F;
/*       */         case 31:
/*  3006 */           return 5.0F;
/*       */         case 10:
/*  3008 */           return 5.0F;
/*       */         case 57:
/*  3010 */           return 10.0F;
/*       */         case 7:
/*  3012 */           return 10.0F;
/*       */         case 12:
/*  3014 */           return 0.75F;
/*       */         case 67:
/*  3016 */           return 12.0F;
/*       */         case 8:
/*  3018 */           return 8.0F;
/*       */         case 9:
/*  3020 */           return 2.5F;
/*       */         case 13:
/*  3022 */           return 0.9F;
/*       */         case 96:
/*  3024 */           return 9.0F;
/*       */       } 
/*       */ 
/*       */     
/*       */     } else {
/*  3029 */       if (this.material == -10L)
/*  3030 */         return 1.0F; 
/*  3031 */       if (this.material == 7)
/*  3032 */         return 10.0F; 
/*  3033 */       if (this.material == 8)
/*  3034 */         return 8.0F; 
/*  3035 */       if (this.material == 31 || this.material == 30)
/*  3036 */         return 6.0F; 
/*  3037 */       if (this.material == 10 || this.material == 9) {
/*  3038 */         return 5.0F;
/*       */       }
/*  3040 */       return 1.0F;
/*       */     } 
/*       */     
/*  3043 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public final ArmourTemplate.ArmourType getArmourType() {
/*  3048 */     ArmourTemplate armourTemplate = ArmourTemplate.getArmourTemplate(this);
/*  3049 */     if (armourTemplate != null) {
/*  3050 */       return armourTemplate.getArmourType();
/*       */     }
/*  3052 */     return null;
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
/*       */   public final boolean moveToItem(Creature mover, long targetId, boolean lastMove) throws NoSuchItemException, NoSuchPlayerException, NoSuchCreatureException {
/*  3064 */     Item target = Items.getItem(targetId);
/*  3065 */     if (isNoTake() && (!getParent().isVehicle() || !target.isVehicle())) {
/*  3066 */       return false;
/*       */     }
/*  3068 */     if (isComponentItem())
/*  3069 */       return false; 
/*  3070 */     if (isBodyPartAttached()) {
/*  3071 */       return false;
/*       */     }
/*  3073 */     if (this.parentId == -10L || !getParent().isInventory() || target.getTemplateId() != 1315)
/*       */     {
/*       */ 
/*       */       
/*  3077 */       if (this.parentId == -10L || getParent().getTemplateId() != 1315 || !target.isInventory())
/*       */       {
/*       */ 
/*       */ 
/*       */         
/*  3082 */         if (getTemplate().isTransportable() && getTopParent() != getWurmId() && ((!getParent().isVehicle() && getParent().getTemplateId() != 1312 && getParent().getTemplateId() != 1309) || (
/*  3083 */           !target.isVehicle() && target.getTemplateId() != 1312 && target.getTemplateId() != 1309))) {
/*       */           
/*  3085 */           mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3086 */               getName() + " will not fit in the " + target.getName() + ".");
/*  3087 */           return false;
/*       */         }  }  } 
/*  3089 */     boolean toReturn = false;
/*  3090 */     long lOwnerId = getOwnerId();
/*  3091 */     Creature itemOwner = null;
/*  3092 */     long targetOwnerId = target.getOwnerId();
/*       */     
/*  3094 */     if (target.getTemplateId() == 1309) {
/*       */       
/*  3096 */       if (!target.isPlanted()) {
/*       */         
/*  3098 */         mover.getCommunicator().sendNormalServerMessage("The wagoner container must be planted so it can accept crates.");
/*  3099 */         return false;
/*       */       } 
/*  3101 */       if (target.getTopParent() != target.getWurmId()) {
/*       */         
/*  3103 */         mover.getCommunicator().sendNormalServerMessage(
/*  3104 */             StringUtil.format("You are not allowed to do that! The %s must be on the ground.", new Object[] { target.getName() }));
/*  3105 */         return false;
/*       */       } 
/*  3107 */       if (!isCrate()) {
/*       */         
/*  3109 */         mover.getCommunicator().sendNormalServerMessage("Only crates fit in the wagoner container.");
/*  3110 */         return false;
/*       */       } 
/*       */     } 
/*  3113 */     if (target.isTent()) {
/*       */       
/*  3115 */       if (target.getTopParent() != target.getWurmId()) {
/*       */         
/*  3117 */         mover.getCommunicator().sendNormalServerMessage(
/*  3118 */             StringUtil.format("You are not allowed to do that! The %s must be on the ground.", new Object[] { target.getName() }));
/*  3119 */         return false;
/*       */       } 
/*  3121 */       if (target.isTent() && target.isNewbieItem() && target.getLastOwnerId() != mover.getWurmId())
/*       */       {
/*  3123 */         if (!Servers.localServer.PVPSERVER) {
/*       */ 
/*       */           
/*  3126 */           mover.getCommunicator()
/*  3127 */             .sendNormalServerMessage("You don't want to put things in other people's tents since you aren't allowed to remove them.");
/*       */           
/*  3129 */           return false;
/*       */         } 
/*       */       }
/*       */     } 
/*  3133 */     if (target.banked || target.mailed) {
/*  3134 */       return false;
/*       */     }
/*  3136 */     Item targetTopParent = target.getTopParentOrNull();
/*  3137 */     if (targetTopParent != null && (targetTopParent
/*  3138 */       .getTemplateId() == 853 || targetTopParent.getTemplateId() == 1410))
/*  3139 */       return false; 
/*  3140 */     if (targetTopParent != null && targetTopParent.isHollow() && !targetTopParent.isInventory() && isTent() && 
/*  3141 */       isNewbieItem()) {
/*       */       
/*  3143 */       mover.getCommunicator().sendNormalServerMessage("You want to keep your tent easily retrievable.");
/*  3144 */       return false;
/*       */     } 
/*  3146 */     if (lOwnerId != -10L) {
/*       */       
/*  3148 */       itemOwner = Server.getInstance().getCreature(lOwnerId);
/*  3149 */       if (this.id == itemOwner.getBody().getBodyItem().getWurmId() || this.id == itemOwner
/*  3150 */         .getPossessions().getInventory().getWurmId())
/*  3151 */         return false; 
/*  3152 */       if (targetOwnerId == -10L || targetOwnerId != lOwnerId)
/*       */       {
/*  3154 */         if (itemOwner.getPower() < 3 && !canBeDropped(true)) {
/*       */           
/*  3156 */           if (isHollow()) {
/*       */             
/*  3158 */             if (itemOwner.equals(mover)) {
/*  3159 */               itemOwner.getCommunicator().sendSafeServerMessage("You are not allowed to drop that. It may contain a non-droppable item.");
/*       */             
/*       */             }
/*       */           
/*       */           }
/*  3164 */           else if (itemOwner.equals(mover)) {
/*  3165 */             itemOwner.getCommunicator().sendSafeServerMessage("You are not allowed to drop that.");
/*       */           } 
/*  3167 */           return false;
/*       */         } 
/*       */       }
/*       */     } 
/*  3171 */     boolean pickup = false;
/*  3172 */     Creature targetOwner = null;
/*  3173 */     if (targetOwnerId != -10L && lOwnerId != targetOwnerId) {
/*       */       
/*  3175 */       int lWeight = getFullWeight();
/*  3176 */       if (isLiquid() && target.isContainerLiquid())
/*       */       {
/*       */         
/*  3179 */         lWeight = Math.min(lWeight, target.getFreeVolume());
/*       */       }
/*  3181 */       targetOwner = Server.getInstance().getCreature(targetOwnerId);
/*  3182 */       if (!targetOwner.canCarry(lWeight) && lWeight != 0) {
/*       */         
/*  3184 */         if (targetOwner.equals(mover))
/*  3185 */           targetOwner.getCommunicator().sendSafeServerMessage("You cannot carry that much."); 
/*  3186 */         return false;
/*       */       } 
/*  3188 */       if (lOwnerId == -10L) {
/*       */         
/*  3190 */         pickup = true;
/*       */         
/*       */         try {
/*  3193 */           boolean ok = (targetOwner.isKingdomGuard() && targetOwner.getKingdomId() == mover.getKingdomId());
/*  3194 */           if (!Servers.isThisAPvpServer() && targetOwner.isBranded()) {
/*  3195 */             ok = (ok || targetOwner.mayAccessHold(mover));
/*       */           } else {
/*  3197 */             ok = (ok || targetOwner.getDominator() == mover);
/*  3198 */           }  if (mover.getWurmId() != targetOwnerId && !ok) {
/*       */             
/*  3200 */             mover.getCommunicator().sendNormalServerMessage("You can't give the " + 
/*  3201 */                 getName() + " to " + targetOwner.getName() + " like that.");
/*  3202 */             return false;
/*       */           } 
/*  3204 */           Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  3205 */           VolaTile tile = zone.getTileOrNull((int)getPosX() >> 2, (int)getPosY() >> 2);
/*  3206 */           if (tile != null)
/*       */           {
/*  3208 */             Structure struct = tile.getStructure();
/*       */             
/*  3210 */             VolaTile tile2 = targetOwner.getCurrentTile();
/*  3211 */             if (tile2 != null)
/*       */             {
/*  3213 */               if (tile.getStructure() != struct)
/*       */               {
/*  3215 */                 targetOwner.getCommunicator().sendNormalServerMessage("You can't reach the " + 
/*  3216 */                     getName() + " through the wall.");
/*  3217 */                 return false;
/*       */               
/*       */               }
/*       */             
/*       */             }
/*  3222 */             else if (struct != null)
/*       */             {
/*  3224 */               targetOwner.getCommunicator().sendNormalServerMessage("You can't reach the " + 
/*  3225 */                   getName() + " through the wall.");
/*  3226 */               return false;
/*       */             }
/*       */           
/*       */           }
/*       */           else
/*       */           {
/*  3232 */             logWarn("No tile found in zone.");
/*  3233 */             return false;
/*       */           }
/*       */         
/*  3236 */         } catch (NoSuchZoneException nsz) {
/*       */           
/*  3238 */           if (itemOwner != null) {
/*       */             
/*  3240 */             logWarn(itemOwner.getName() + ":" + nsz.getMessage(), (Throwable)nsz);
/*  3241 */             return false;
/*       */           } 
/*       */ 
/*       */           
/*  3245 */           if (this.parentId != -10L)
/*       */           {
/*  3247 */             logInfo("Parent is not NOID Look at the following exception:");
/*  3248 */             Item p = Items.getItem(this.parentId);
/*  3249 */             logWarn(nsz.getMessage() + " id=" + this.id + ' ' + getName() + ' ' + (
/*  3250 */                 (WurmId.getType(this.parentId) == 6) ? 1 : 0) + " parent=" + p.getName() + " ownerid=" + lOwnerId);
/*       */           
/*       */           }
/*       */           else
/*       */           {
/*  3255 */             logInfo(targetOwner.getName() + " trying to scam ZONEID=" + getZoneId() + ", Parent=NOID " + nsz
/*  3256 */                 .getMessage() + " id=" + this.id + ' ' + getName() + " ownerid=" + lOwnerId + ". Close these windows sometime.");
/*       */             
/*  3258 */             return false;
/*       */           }
/*       */         
/*       */         }
/*       */       
/*  3263 */       } else if (isCreatureWearableOnly()) {
/*       */         
/*  3265 */         if (mover.getVehicle() != -10L && itemOwner.getWurmId() == mover.getWurmId()) {
/*       */           
/*  3267 */           mover.getCommunicator().sendNormalServerMessage("You need to be standing on the ground to do that.");
/*  3268 */           return false;
/*       */         } 
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/*  3274 */     long pid = getParentId();
/*       */ 
/*       */ 
/*       */     
/*  3278 */     if (pid == target.getWurmId() || this.id == target.getParentId())
/*       */     {
/*       */       
/*  3281 */       return false;
/*       */     }
/*       */     
/*  3284 */     if (!target.isHollow() && pid == target.getParentId())
/*       */     {
/*       */       
/*  3287 */       return false;
/*       */     }
/*  3289 */     if (target.isBodyPart()) {
/*       */       
/*  3291 */       boolean found = false;
/*  3292 */       for (int x = 0; x < (getBodySpaces()).length; x++) {
/*       */         
/*  3294 */         if (getBodySpaces()[x] == target.getPlace()) {
/*  3295 */           found = true;
/*       */         }
/*       */       } 
/*  3298 */       if (!found)
/*       */       {
/*  3300 */         if (target.getPlace() != 13 && target.getPlace() != 14 && (target
/*  3301 */           .getPlace() < 35 || target.getPlace() >= 48)) {
/*       */           
/*  3303 */           if (itemOwner != null) {
/*  3304 */             itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3305 */                 getName() + " will not fit in the " + target.getName() + ".");
/*  3306 */           } else if (targetOwner != null) {
/*  3307 */             targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3308 */                 getName() + " will not fit in the " + target.getName() + ".");
/*  3309 */           } else if (mover != null) {
/*  3310 */             mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3311 */                 getName() + " will not fit in the " + target.getName() + ".");
/*  3312 */           }  return false;
/*       */         } 
/*       */       }
/*       */     } 
/*  3316 */     Item targetParent = null;
/*       */     
/*  3318 */     Item parent = null;
/*  3319 */     if (this.parentId != -10L)
/*  3320 */       parent = Items.getItem(this.parentId); 
/*  3321 */     if (target.hasNoParent()) {
/*       */       
/*  3323 */       if (target.isNoPut() && !insertOverrideNoPut(this, target)) {
/*  3324 */         return false;
/*       */       }
/*       */     } else {
/*       */       
/*  3328 */       if (target.isNoPut() && !insertOverrideNoPut(this, target))
/*  3329 */         return false; 
/*  3330 */       targetParent = Items.getItem(target.getParentId());
/*  3331 */       if (targetParent.isNoPut() && !insertOverrideNoPut(this, targetParent))
/*  3332 */         return false; 
/*  3333 */       if (targetParent.isMailBox()) {
/*       */         
/*  3335 */         mover.getCommunicator().sendNormalServerMessage("The spirits refuse to let you do that now.");
/*  3336 */         return false;
/*       */       } 
/*  3338 */       Item topParent = null;
/*       */       
/*       */       try {
/*  3341 */         topParent = Items.getItem(target.getTopParent());
/*  3342 */         if (topParent.isNoPut() && !insertOverrideNoPut(this, topParent))
/*  3343 */           return false; 
/*  3344 */         if (topParent.isMailBox())
/*       */         {
/*  3346 */           mover.getCommunicator().sendNormalServerMessage("The spirits refuse to let you do that now.");
/*  3347 */           return false;
/*       */         }
/*       */       
/*  3350 */       } catch (NoSuchItemException nsi) {
/*       */         
/*  3352 */         logWarn(nsi.getMessage(), (Throwable)nsi);
/*  3353 */         return false;
/*       */       } 
/*       */     } 
/*  3356 */     if (target.getTemplateId() == 1023) {
/*       */       
/*  3358 */       if (!isUnfired()) {
/*       */         
/*  3360 */         mover.getCommunicator().sendNormalServerMessage("Only unfired clay items can be put into a kiln.");
/*  3361 */         return false;
/*       */       } 
/*  3363 */       if (targetParent != null) {
/*       */         
/*  3365 */         mover.getCommunicator().sendNormalServerMessage("You cannot reach that whilst it is in a kiln.");
/*  3366 */         return false;
/*       */       } 
/*       */     } 
/*  3369 */     if (target.getTemplateId() == 1028 && !isOre()) {
/*       */       
/*  3371 */       mover.getCommunicator().sendNormalServerMessage("Only ore can be put into a smelter.");
/*  3372 */       return false;
/*       */     } 
/*  3374 */     if (target.isComponentItem() && targetParent == null) {
/*       */       
/*  3376 */       mover.getCommunicator().sendNormalServerMessage("You cannot put items in the " + target.getName() + " as it does not seem to be in anything.");
/*       */       
/*  3378 */       return false;
/*       */     } 
/*  3380 */     if (target.getTemplateId() == 1435) {
/*       */       
/*  3382 */       if (getTemplateId() != 128)
/*       */       {
/*  3384 */         mover.getCommunicator().sendNormalServerMessage("You can only put water into the drinker.");
/*  3385 */         return false;
/*       */       }
/*       */     
/*  3388 */     } else if (target.getTemplateId() == 1434) {
/*       */       
/*  3390 */       if (!isSeed()) {
/*       */         
/*  3392 */         mover.getCommunicator().sendNormalServerMessage("You can only put seeds into the feeder.");
/*  3393 */         return false;
/*       */       } 
/*       */     } 
/*  3396 */     if (target.getTemplateId() == 1432) {
/*       */       
/*  3398 */       mover.getCommunicator().sendNormalServerMessage("You can't put that there.");
/*  3399 */       return false;
/*       */     } 
/*  3401 */     if (target.isParentMustBeOnGround() && targetParent.getParentId() != -10L) {
/*       */       
/*  3403 */       mover.getCommunicator().sendNormalServerMessage("You cannot put items on the " + target.getName() + " whilst the " + targetParent
/*  3404 */           .getName() + " is not on the ground.");
/*  3405 */       return false;
/*       */     } 
/*  3407 */     if (target.getTemplateId() == 1278 && getTemplateId() != 1276) {
/*       */       
/*  3409 */       mover.getCommunicator().sendNormalServerMessage("Only snowballs can be put into an icebox.");
/*  3410 */       return false;
/*       */     } 
/*  3412 */     if (target.getTemplateId() == 1108 && getTemplateId() != 768) {
/*       */       
/*  3414 */       mover.getCommunicator().sendNormalServerMessage("Only wine barrels can be put on that rack.");
/*  3415 */       return false;
/*       */     } 
/*  3417 */     if (target.getTemplateId() == 1109 && getTemplateId() != 189) {
/*       */       
/*  3419 */       mover.getCommunicator().sendNormalServerMessage("Only small barrels can be put into that rack.");
/*  3420 */       return false;
/*       */     } 
/*  3422 */     if (target.getTemplateId() == 1110 && getTemplateId() != 1161 && getTemplateId() != 1162) {
/*       */       
/*  3424 */       mover.getCommunicator().sendNormalServerMessage("Only herb and spice planters can be put into that rack.");
/*  3425 */       return false;
/*       */     } 
/*  3427 */     if (target.getTemplateId() == 1111 && getTemplateId() != 1022 && getTemplateId() != 1020) {
/*       */       
/*  3429 */       mover.getCommunicator().sendNormalServerMessage("Only amphora can be put into that rack.");
/*  3430 */       return false;
/*       */     } 
/*  3432 */     if (target.getTemplateId() == 1279 && !canLarder() && (!usesFoodState() || getAuxData() != 0)) {
/*       */       
/*  3434 */       mover.getCommunicator().sendNormalServerMessage("Only processed food items can be put onto the shelf.");
/*  3435 */       return false;
/*       */     } 
/*  3437 */     if (target.getTemplateId() == 1120 && isBarrelRack()) {
/*       */       
/*  3439 */       mover.getCommunicator().sendNormalServerMessage("The " + getName() + " will not fit onto the shelf.");
/*  3440 */       return false;
/*       */     } 
/*  3442 */     if (target.isAlmanacContainer() && !isHarvestReport()) {
/*       */       
/*  3444 */       mover.getCommunicator().sendNormalServerMessage("Only harvest reports can be put in " + target
/*  3445 */           .getTemplate().getNameWithGenus() + ".");
/*  3446 */       return false;
/*       */     } 
/*  3448 */     if (target.getTemplateId() == 1312 && !isCrate()) {
/*       */       
/*  3450 */       mover.getCommunicator().sendNormalServerMessage("Only crates can be put into that rack.");
/*  3451 */       return false;
/*       */     } 
/*  3453 */     if (target.getTemplateId() == 1315 && (getTemplateId() != 662 || !isEmpty(false))) {
/*       */       
/*  3455 */       mover.getCommunicator().sendNormalServerMessage("Only empty bsb can be put into that rack.");
/*  3456 */       return false;
/*       */     } 
/*       */     
/*  3459 */     if (target.getTemplateId() == 1341)
/*       */     {
/*  3461 */       for (Item compartment : target.getItemsAsArray()) {
/*       */         
/*  3463 */         if (compartment.doesContainerRestrictionsAllowItem(this)) {
/*       */           
/*  3465 */           target = compartment;
/*       */           
/*       */           break;
/*       */         } 
/*       */       } 
/*       */     }
/*  3471 */     if (!target.canHold(this)) {
/*       */       
/*  3473 */       mover.getCommunicator().sendNormalServerMessage("There isn't enough room to fit " + getNameWithGenus() + " in " + target
/*  3474 */           .getNameWithGenus() + ".");
/*  3475 */       return false;
/*       */     } 
/*  3477 */     if (target.getTemplateId() == 1295 && !canLarder() && (!usesFoodState() || getAuxData() != 0)) {
/*       */       
/*  3479 */       mover.getCommunicator().sendNormalServerMessage("Only processed food items can be put into the food tin.");
/*  3480 */       return false;
/*       */     } 
/*  3482 */     if (!isLiquid() && target.getTemplateId() == 1294) {
/*       */       
/*  3484 */       mover.getCommunicator().sendNormalServerMessage("Only liquids may be put in a thermos.");
/*  3485 */       return false;
/*       */     } 
/*  3487 */     if (!isLiquid() && target.getTemplateId() == 1118) {
/*       */       
/*  3489 */       mover.getCommunicator().sendNormalServerMessage("Only liquids may be put into an alchemy storage vial.");
/*  3490 */       return false;
/*       */     } 
/*  3492 */     if (target.containsIngredientsOnly() && !isFood() && !isLiquid() && !isRecipeItem()) {
/*       */       
/*  3494 */       mover.getCommunicator().sendNormalServerMessage("Only ingredients that are used to make food can be put onto " + target.getNameWithGenus() + ".");
/*  3495 */       return false;
/*       */     } 
/*  3497 */     if (target.getTemplateId() == 1284)
/*       */     {
/*       */       
/*  3500 */       if (targetParent != null && targetParent.getTemplateId() == 1178 && targetParent.getParentId() != -10L) {
/*       */         
/*  3502 */         mover.getCommunicator().sendNormalServerMessage("You can only put liquids into the boiler when the still is not on the ground.");
/*  3503 */         return false;
/*       */       } 
/*       */     }
/*  3506 */     if (isLiquid()) {
/*       */       
/*  3508 */       if (!target.isContainerLiquid() && target.getTemplateId() != 75)
/*  3509 */         target = targetParent; 
/*  3510 */       if (target != null && (target.isContainerLiquid() || target.getTemplateId() == 75)) {
/*       */         
/*  3512 */         if (target.getTemplateId() == 1284 && target.isEmpty(false)) {
/*       */ 
/*       */ 
/*       */           
/*  3516 */           Item topParent = target.getTopParentOrNull();
/*       */           
/*  3518 */           if (topParent != null && topParent.getTemplateId() == 1178) {
/*       */ 
/*       */             
/*  3521 */             Item condenser = null;
/*  3522 */             for (Iterator<Item> iterator = topParent.getItems().iterator(); iterator.hasNext(); ) {
/*       */               
/*  3524 */               Item item = iterator.next();
/*  3525 */               if (item.getTemplateId() == 1285) {
/*  3526 */                 condenser = item;
/*       */               }
/*       */             } 
/*  3529 */             if (condenser != null) {
/*       */ 
/*       */               
/*  3532 */               Item[] contents = condenser.getItemsAsArray();
/*  3533 */               if (contents.length != 0)
/*       */               {
/*  3535 */                 if (contents[0].getTemplateId() != getTemplateId() || contents[0].getRealTemplateId() != getRealTemplateId() || contents[0]
/*  3536 */                   .getRarity() != getRarity()) {
/*       */                   
/*  3538 */                   mover.getCommunicator().sendNormalServerMessage("That would destroy the " + contents[0].getName() + ".");
/*  3539 */                   return false;
/*       */                 } 
/*       */               }
/*       */             } 
/*       */           } 
/*       */         } 
/*  3545 */         Item contained = null;
/*  3546 */         Item liquid = null;
/*  3547 */         int volAvail = target.getContainerVolume();
/*  3548 */         for (Iterator<Item> it = target.getItems().iterator(); it.hasNext(); ) {
/*       */           
/*  3550 */           contained = it.next();
/*  3551 */           if (MethodsItems.wouldDestroyLiquid(target, contained, this)) {
/*       */             
/*  3553 */             mover.getCommunicator().sendNormalServerMessage("That would destroy the liquid.");
/*  3554 */             return false;
/*       */           } 
/*  3556 */           if (contained.isLiquid()) {
/*       */             
/*  3558 */             if ((!target.isContainerLiquid() && target.getTemplateId() != 75) || (contained.getTemplateId() == getTemplateId() && contained
/*  3559 */               .getRealTemplateId() == getRealTemplateId() && contained.getLeftAuxData() == getLeftAuxData()))
/*  3560 */               liquid = contained; 
/*  3561 */             if (contained.isSalted() != isSalted())
/*  3562 */               liquid = contained; 
/*  3563 */             volAvail -= contained.getWeightGrams();
/*       */             continue;
/*       */           } 
/*  3566 */           volAvail -= contained.getVolume();
/*       */         } 
/*       */         
/*  3569 */         contained = liquid;
/*       */         
/*  3571 */         if (contained != null) {
/*       */           
/*  3573 */           if (getRarity() != contained.getRarity()) {
/*       */             
/*  3575 */             if (mover != null)
/*  3576 */               mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3577 */                   getName() + " or the " + contained.getName() + " would lose its rarity."); 
/*  3578 */             return false;
/*       */           } 
/*  3580 */           if (isSalted() != contained.isSalted()) {
/*       */             
/*  3582 */             if (mover != null) {
/*  3583 */               mover.getCommunicator().sendNormalServerMessage("Cannot mix salty water with non-salty water.");
/*       */             }
/*  3585 */             return false;
/*       */           } 
/*       */         } 
/*  3588 */         if (volAvail > 0) {
/*       */           
/*  3590 */           if (volAvail < getWeightGrams()) {
/*       */             
/*  3592 */             if (contained == null) {
/*       */               
/*       */               try
/*       */               {
/*  3596 */                 Item splitItem = MethodsItems.splitLiquid(this, volAvail, mover);
/*  3597 */                 target.insertItem(splitItem);
/*       */               }
/*  3599 */               catch (FailedException fe)
/*       */               {
/*  3601 */                 logWarn(fe.getMessage(), (Throwable)fe);
/*  3602 */                 return false;
/*       */               }
/*  3604 */               catch (NoSuchTemplateException nst)
/*       */               {
/*  3606 */                 logWarn(nst.getMessage(), (Throwable)nst);
/*  3607 */                 return false;
/*       */               }
/*       */             
/*       */             } else {
/*       */               
/*  3612 */               if (contained.getTemplateId() == 417 || getTemplateId() == 417)
/*       */               {
/*  3614 */                 if (contained.getRealTemplateId() != getRealTemplateId()) {
/*       */                   
/*  3616 */                   String name1 = "fruit";
/*  3617 */                   String name2 = "fruit";
/*  3618 */                   ItemTemplate t = contained.getRealTemplate();
/*  3619 */                   if (t != null)
/*  3620 */                     name1 = t.getName(); 
/*  3621 */                   ItemTemplate t2 = getRealTemplate();
/*  3622 */                   if (t2 != null) {
/*  3623 */                     name2 = t2.getName();
/*       */                   }
/*  3625 */                   if (!name1.equals(name2))
/*  3626 */                     contained.setName(name1 + " and " + name2 + " juice"); 
/*  3627 */                   contained.setRealTemplate(-10);
/*       */                 } 
/*       */               }
/*  3630 */               setWeight(getWeightGrams() - volAvail, true, (targetOwner != itemOwner));
/*  3631 */               int allWeight = contained.getWeightGrams() + volAvail;
/*       */               
/*  3633 */               float newQl = (getCurrentQualityLevel() * volAvail + contained.getCurrentQualityLevel() * contained.getWeightGrams()) / allWeight;
/*       */               
/*  3635 */               if (contained.isColor() && this.color != -1)
/*       */               {
/*  3637 */                 contained.setColor(WurmColor.mixColors(contained.color, contained.getWeightGrams(), this.color, volAvail, newQl));
/*       */               }
/*       */               
/*  3640 */               contained.setQualityLevel(newQl);
/*  3641 */               contained.setWeight(contained.getWeightGrams() + volAvail, true, (targetOwner != itemOwner));
/*  3642 */               contained.setDamage(0.0F);
/*       */             }
/*       */           
/*       */           } else {
/*       */             
/*  3647 */             if (contained != null) {
/*       */               
/*  3649 */               if (contained.getTemplateId() == 417 || getTemplateId() == 417)
/*       */               {
/*  3651 */                 if (contained.getRealTemplateId() != getRealTemplateId()) {
/*       */                   
/*  3653 */                   String name1 = "fruit";
/*  3654 */                   String name2 = "fruit";
/*  3655 */                   ItemTemplate t = contained.getRealTemplate();
/*  3656 */                   if (t != null)
/*  3657 */                     name1 = t.getName(); 
/*  3658 */                   ItemTemplate t2 = getRealTemplate();
/*  3659 */                   if (t2 != null) {
/*  3660 */                     name2 = t2.getName();
/*       */                   }
/*  3662 */                   if (!name1.equals(name2))
/*  3663 */                     contained.setName(name1 + " and " + name2 + " juice"); 
/*  3664 */                   contained.setRealTemplate(-10);
/*       */                 } 
/*       */               }
/*  3667 */               int allWeight = getWeightGrams() + contained.getWeightGrams();
/*       */               
/*  3669 */               float newQl = (getCurrentQualityLevel() * getWeightGrams() + contained.getCurrentQualityLevel() * contained.getWeightGrams()) / allWeight;
/*       */               
/*  3671 */               if (contained.isColor() && this.color != -1)
/*       */               {
/*  3673 */                 contained.setColor(WurmColor.mixColors(contained.color, contained.getWeightGrams(), this.color, 
/*  3674 */                       getWeightGrams(), newQl));
/*       */               }
/*  3676 */               contained.setQualityLevel(newQl);
/*  3677 */               contained.setDamage(0.0F);
/*  3678 */               contained.setWeight(allWeight, true);
/*  3679 */               Items.destroyItem(this.id);
/*       */               
/*  3681 */               SoundPlayer.playSound("sound.liquid.fillcontainer", this, 0.1F);
/*  3682 */               return false;
/*       */             } 
/*  3684 */             if (target.testInsertItem(this)) {
/*       */               
/*  3686 */               if (parent != null)
/*       */               {
/*  3688 */                 if (!hasSameOwner(target)) {
/*       */                   
/*  3690 */                   parent.dropItem(this.id, false);
/*       */                 }
/*       */                 else {
/*       */                   
/*  3694 */                   parent.removeItem(this.id, false, false, false);
/*       */                 } 
/*       */               }
/*       */               
/*  3698 */               setLastOwnerId(mover.getWurmId());
/*  3699 */               target.insertItem(this);
/*       */             
/*       */             }
/*       */             else {
/*       */ 
/*       */               
/*  3705 */               return false;
/*       */             } 
/*       */           } 
/*       */         } else {
/*  3709 */           return false;
/*       */         } 
/*       */         
/*  3712 */         SoundPlayer.playSound("sound.liquid.fillcontainer", this, 0.1F);
/*  3713 */         return true;
/*       */       } 
/*       */       
/*  3716 */       return false;
/*       */     } 
/*  3718 */     if (target.isContainerLiquid() || (targetParent != null && targetParent
/*  3719 */       .isContainerLiquid() && !target.isHollow())) {
/*       */       
/*  3721 */       if (!target.isContainerLiquid()) {
/*  3722 */         target = targetParent;
/*       */       }
/*       */ 
/*       */       
/*  3726 */       if (target.getSizeX() >= getSizeX() && target.getSizeY() >= getSizeY() && target.getSizeZ() > getSizeZ()) {
/*       */         
/*  3728 */         if (target.getItems().size() > 0) {
/*       */           
/*  3730 */           Item contained = null;
/*  3731 */           Item liquid = null;
/*       */           
/*  3733 */           for (Iterator<Item> it = target.getItems().iterator(); it.hasNext(); ) {
/*       */             
/*  3735 */             contained = it.next();
/*  3736 */             if (contained.isLiquid()) {
/*       */               
/*  3738 */               if (!isFood() && !isRecipeItem()) {
/*       */                 
/*  3740 */                 mover.getCommunicator().sendNormalServerMessage("That would destroy the liquid.");
/*  3741 */                 return false;
/*       */               } 
/*       */ 
/*       */               
/*  3745 */               liquid = contained;
/*       */             } 
/*       */           } 
/*       */           
/*  3749 */           if (liquid != null) {
/*       */             
/*  3751 */             int used = target.getUsedVolume();
/*  3752 */             int size = liquid.getWeightGrams();
/*  3753 */             int free = target.getVolume() - used;
/*       */             
/*  3755 */             if (free < getVolume()) {
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  3760 */               if (free + size <= getVolume()) {
/*       */                 
/*  3762 */                 if (itemOwner != null) {
/*  3763 */                   itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3764 */                       getName() + " will not fit in the " + target.getName() + ".");
/*  3765 */                 } else if (targetOwner != null) {
/*  3766 */                   targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3767 */                       getName() + " will not fit in the " + target.getName() + ".");
/*  3768 */                 } else if (mover != null) {
/*  3769 */                   mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3770 */                       getName() + " will not fit in the " + target.getName() + ".");
/*  3771 */                 }  return false;
/*       */               } 
/*       */ 
/*       */               
/*  3775 */               int leftNeeded = getVolume() - free;
/*       */               
/*  3777 */               if (leftNeeded < size && leftNeeded > 0)
/*       */               {
/*  3779 */                 liquid.setWeight(size - leftNeeded, true);
/*  3780 */                 mover.getCommunicator().sendNormalServerMessage("You spill some " + liquid.getName() + ".");
/*       */               }
/*  3782 */               else if (leftNeeded == size)
/*       */               {
/*  3784 */                 Items.destroyItem(liquid.getWurmId());
/*  3785 */                 mover.getCommunicator().sendNormalServerMessage("You spill the " + liquid.getName() + ".");
/*       */               }
/*       */             
/*       */             } 
/*       */           } 
/*       */         } 
/*       */       } else {
/*       */         
/*  3793 */         if (itemOwner != null) {
/*  3794 */           itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3795 */               getName() + " will not fit in the " + target.getName() + ".");
/*  3796 */         } else if (targetOwner != null) {
/*  3797 */           targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  3798 */               getName() + " will not fit in the " + target.getName() + ".");
/*  3799 */         } else if (mover != null) {
/*  3800 */           mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3801 */               getName() + " will not fit in the " + target.getName() + ".");
/*  3802 */         }  return false;
/*       */       } 
/*       */     } 
/*       */     
/*  3806 */     if (target.isLockable() && target.getLockId() != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  3810 */         Item lock = Items.getItem(target.getLockId());
/*  3811 */         long[] keyIds = lock.getKeyIds();
/*  3812 */         for (int x = 0; x < keyIds.length; x++) {
/*       */           
/*  3814 */           if (this.id == keyIds[x])
/*  3815 */             return false; 
/*  3816 */           if (this.items != null)
/*       */           {
/*  3818 */             for (Iterator<Item> it = this.items.iterator(); it.hasNext(); ) {
/*       */               
/*  3820 */               Item itkey = it.next();
/*  3821 */               if (itkey.getWurmId() == keyIds[x])
/*       */               {
/*  3823 */                 mover.getCommunicator().sendNormalServerMessage("The " + target
/*  3824 */                     .getName() + " is locked with a key inside the " + getName() + ".");
/*  3825 */                 return false;
/*       */               }
/*       */             
/*       */             } 
/*       */           }
/*       */         } 
/*  3831 */       } catch (NoSuchItemException nsi) {
/*       */         
/*  3833 */         logWarn(target.getWurmId() + ": item has a set lock but the lock does not exist?:" + target.getLockId(), (Throwable)nsi);
/*       */         
/*  3835 */         return false;
/*       */       } 
/*       */     }
/*       */     
/*  3839 */     if (targetParent != null && targetParent.isLockable() && targetParent.getLockId() != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  3843 */         Item lock = Items.getItem(targetParent.getLockId());
/*  3844 */         long[] keyIds = lock.getKeyIds();
/*  3845 */         for (int x = 0; x < keyIds.length; x++) {
/*       */           
/*  3847 */           if (this.id == keyIds[x])
/*  3848 */             return false; 
/*  3849 */           if (this.items != null)
/*       */           {
/*  3851 */             for (Iterator<Item> it = this.items.iterator(); it.hasNext(); ) {
/*       */               
/*  3853 */               Item itkey = it.next();
/*  3854 */               if (itkey.getWurmId() == keyIds[x])
/*       */               {
/*  3856 */                 mover.getCommunicator().sendNormalServerMessage("The " + target
/*  3857 */                     .getName() + " is locked with a key inside the " + getName() + ".");
/*  3858 */                 return false;
/*       */               }
/*       */             
/*       */             } 
/*       */           }
/*       */         } 
/*  3864 */       } catch (NoSuchItemException nsi) {
/*       */         
/*  3866 */         logWarn(targetParent.getWurmId() + ": item has a set lock but the lock does not exist?:" + targetParent
/*  3867 */             .getLockId(), (Throwable)nsi);
/*  3868 */         return false;
/*       */       } 
/*       */     }
/*  3871 */     if (targetParent != null && targetParent.isBulkContainer())
/*  3872 */       target = targetParent; 
/*  3873 */     if (target.isBulkContainer()) {
/*       */       
/*  3875 */       if (isEnchanted() && getSpellFoodBonus() == 0.0F) {
/*       */         
/*  3877 */         if (mover != null)
/*  3878 */           mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would lose its enchants."); 
/*  3879 */         return false;
/*       */       } 
/*  3881 */       if (getRarity() > 0) {
/*       */         
/*  3883 */         if (mover != null)
/*  3884 */           mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would lose its rarity."); 
/*  3885 */         return false;
/*       */       } 
/*  3887 */       Item topParent = target.getTopParentOrNull();
/*  3888 */       if ((target.getTopParent() != target.getWurmId() && !target.isCrate() && !targetParent.isVehicle() && targetParent
/*  3889 */         .getTemplateId() != 1316) || (topParent != null && (topParent
/*  3890 */         .getTemplateId() == 853 || topParent.getTemplateId() == 1410))) {
/*       */         
/*  3892 */         if (mover != null) {
/*       */           
/*  3894 */           String message = StringUtil.format("The %s needs to be on the ground.", new Object[] { target
/*       */                 
/*  3896 */                 .getName() });
/*  3897 */           mover.getCommunicator().sendNormalServerMessage(message);
/*       */         } 
/*  3899 */         return false;
/*       */       } 
/*  3901 */       if (canHaveInscription()) {
/*       */         
/*  3903 */         if (this.inscription != null && this.inscription.hasBeenInscribed()) {
/*       */           
/*  3905 */           if (mover != null)
/*       */           {
/*  3907 */             if (getAuxData() == 1 || getAuxData() > 8) {
/*  3908 */               mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3909 */                   getName() + " would be destroyed.");
/*       */             } else {
/*  3911 */               mover.getCommunicator().sendNormalServerMessage("The inscription on the " + 
/*  3912 */                   getName() + " would be destroyed.");
/*       */             }  } 
/*  3914 */           return false;
/*       */         } 
/*  3916 */         if (getAuxData() != 0) {
/*       */           
/*  3918 */           if (mover != null)
/*  3919 */             mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would be destroyed."); 
/*  3920 */           return false;
/*       */         } 
/*       */       } 
/*  3923 */       if (!isBulk()) {
/*       */         
/*  3925 */         if (mover != null)
/*  3926 */           mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would be destroyed."); 
/*  3927 */         return false;
/*       */       } 
/*  3929 */       if (isFood()) {
/*       */         
/*  3931 */         if (target.getTemplateId() != 661 && !target.isCrate()) {
/*       */           
/*  3933 */           if (mover != null)
/*  3934 */             mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would be destroyed."); 
/*  3935 */           return false;
/*       */         } 
/*       */         
/*  3938 */         if (isDish() || (usesFoodState() && isFreshBitSet() && isChoppedBitSet()))
/*       */         {
/*  3940 */           if (mover != null)
/*  3941 */             mover.getCommunicator().sendNormalServerMessage("Only unprocessed food items can be stored that way."); 
/*  3942 */           return false;
/*       */         
/*       */         }
/*       */       
/*       */       }
/*  3947 */       else if (target.getTemplateId() != 662 && target.getTemplateId() != 1317 && !target.isCrate()) {
/*       */         
/*  3949 */         if (mover != null)
/*  3950 */           mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would be destroyed."); 
/*  3951 */         return false;
/*       */       } 
/*       */ 
/*       */       
/*  3955 */       if ((mover == null || getTopParent() != mover.getInventory().getWurmId()) && 
/*  3956 */         MethodsItems.checkIfStealing(this, mover, null)) {
/*       */         
/*  3958 */         if (mover != null)
/*  3959 */           mover.getCommunicator().sendNormalServerMessage("You're not allowed to put things into this " + target
/*  3960 */               .getName() + "."); 
/*  3961 */         return false;
/*       */       } 
/*  3963 */       if (target.isLocked()) {
/*       */ 
/*       */         
/*  3966 */         if (mover != null && !target.mayAccessHold(mover))
/*       */         {
/*  3968 */           mover.getCommunicator().sendNormalServerMessage("You're not allowed to put things into this " + target
/*  3969 */               .getName() + ".");
/*  3970 */           return false;
/*       */         
/*       */         }
/*       */ 
/*       */       
/*       */       }
/*  3976 */       else if (mover != null && !Methods.isActionAllowed(mover, (short)7)) {
/*       */ 
/*       */ 
/*       */         
/*  3980 */         return false;
/*       */       } 
/*       */       
/*  3983 */       if (isFish() && isUnderWeight()) {
/*       */         
/*  3985 */         if (mover != null)
/*       */         {
/*  3987 */           mover.getCommunicator().sendNormalServerMessage("The " + 
/*  3988 */               getName() + " is not whole, and therefore is not allowed into the " + target.getName() + ".");
/*       */         }
/*  3990 */         return false;
/*       */       } 
/*  3992 */       if (target.isCrate() && target.canAddToCrate(this))
/*       */       {
/*  3994 */         return AddBulkItemToCrate(mover, target);
/*       */       }
/*  3996 */       if (!target.isCrate() && target.hasSpaceFor(getVolume()))
/*       */       {
/*  3998 */         return AddBulkItem(mover, target);
/*       */       }
/*       */ 
/*       */       
/*  4002 */       mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4003 */       return false;
/*       */     } 
/*       */     
/*  4006 */     if (target.getTemplateId() == 725)
/*       */     {
/*  4008 */       if (!isWeaponPolearm()) {
/*       */         
/*  4010 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4011 */         return false;
/*       */       } 
/*       */     }
/*  4014 */     if (target.getTemplateId() == 724)
/*       */     {
/*  4016 */       if (!isWeapon() || isWeaponPolearm()) {
/*       */         
/*  4018 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4019 */         return false;
/*       */       } 
/*       */     }
/*  4022 */     if (target.getTemplateId() == 758)
/*       */     {
/*  4024 */       if (!isWeaponBow() && !isBowUnstringed()) {
/*       */         
/*  4026 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4027 */         return false;
/*       */       } 
/*       */     }
/*  4030 */     if (target.getTemplateId() == 759)
/*       */     {
/*  4032 */       if (!isArmour()) {
/*       */         
/*  4034 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4035 */         return false;
/*       */       } 
/*       */     }
/*       */     
/*  4039 */     if (target.getTemplateId() == 892)
/*       */     {
/*  4041 */       if (!isArmour() && getTemplateId() != 831) {
/*       */         
/*  4043 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4044 */         return false;
/*       */       } 
/*       */     }
/*       */     
/*  4048 */     if (target.getTemplateId() == 757)
/*       */     {
/*  4050 */       if (getTemplateId() != 418) {
/*       */         
/*  4052 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " would be destroyed.");
/*  4053 */         return false;
/*       */       } 
/*       */     }
/*       */ 
/*       */     
/*  4058 */     if (target.isSaddleBags()) {
/*       */       
/*  4060 */       if (isArtifact()) {
/*       */         
/*  4062 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4063 */         return false;
/*       */       } 
/*  4065 */       if (isHollow() && containsItem() && 
/*  4066 */         containsArtifact()) {
/*  4067 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4068 */         return false;
/*       */       } 
/*       */     } 
/*       */     
/*  4072 */     if (isArtifact())
/*       */     {
/*  4074 */       if (target.isInside(new int[] { 1333, 1334 })) {
/*       */         
/*  4076 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4077 */         return false;
/*       */       } 
/*       */     }
/*       */     
/*  4081 */     if (target.getTemplateId() == 177) {
/*       */       
/*  4083 */       if (isDecoration() || !target.mayCreatureInsertItem()) {
/*       */         
/*  4085 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4086 */         return false;
/*       */       } 
/*       */       
/*  4089 */       if (parent != null)
/*       */       {
/*  4091 */         if (parent.getTemplateId() == 177) {
/*       */           
/*  4093 */           parent.dropItem(this.id, false);
/*  4094 */           parent.removeFromPile(this, !lastMove);
/*       */           
/*  4096 */           if (parent.getItemCount() < 3 && lastMove) {
/*       */             
/*  4098 */             for (Creature iwatcher : parent.getWatchers()) {
/*  4099 */               iwatcher.getCommunicator().sendCloseInventoryWindow(parent.getWurmId());
/*       */             }
/*  4101 */           } else if (lastMove) {
/*  4102 */             parent.updatePile();
/*       */           } 
/*       */         } else {
/*       */           
/*  4106 */           parent.dropItem(this.id, false);
/*       */         }  } 
/*  4108 */       setLastOwnerId(mover.getWurmId());
/*  4109 */       target.insertIntoPile(this);
/*       */       
/*  4111 */       toReturn = false;
/*  4112 */       if (itemOwner != null)
/*       */       {
/*  4114 */         itemOwner.addItemDropped(this);
/*       */       }
/*       */     }
/*  4117 */     else if (!target.isHollow() && targetParent != null && targetParent.getTemplateId() == 177) {
/*       */       
/*  4119 */       if (isDecoration() || (!targetParent.mayCreatureInsertItem() && mover.getPower() == 0)) {
/*       */         
/*  4121 */         mover.getCommunicator().sendNormalServerMessage("The " + getName() + " doesn't fit.");
/*  4122 */         return false;
/*       */       } 
/*  4124 */       if (parent != null)
/*  4125 */         parent.dropItem(this.id, false); 
/*  4126 */       setLastOwnerId(mover.getWurmId());
/*  4127 */       targetParent.insertIntoPile(this);
/*  4128 */       toReturn = false;
/*  4129 */       if (itemOwner != null)
/*       */       {
/*  4131 */         itemOwner.addItemDropped(this);
/*       */       }
/*       */     } else {
/*  4134 */       if (target.testInsertItem(this)) {
/*       */         
/*  4136 */         Item insertTarget = target.getInsertItem();
/*  4137 */         boolean mayInsert = (insertTarget == null) ? false : insertTarget.mayCreatureInsertItem();
/*  4138 */         if (insertTarget.isInventory() || insertTarget.isInventoryGroup()) {
/*       */ 
/*       */           
/*  4141 */           Item p = getParentOrNull();
/*  4142 */           if (p != null && (p.isInventory() || p.isInventoryGroup()))
/*  4143 */             mayInsert = true; 
/*       */         } 
/*  4145 */         if (target.getTemplateId() == 1404 && (getTemplateId() == 1272 || getTemplateId() == 748))
/*       */         {
/*  4147 */           if (getCurrentQualityLevel() < 30.0F) {
/*       */             
/*  4149 */             if (itemOwner != null) {
/*  4150 */               itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4151 */                   getName() + " is too low quality to be used as a report.");
/*  4152 */             } else if (targetOwner != null) {
/*  4153 */               targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4154 */                   getName() + " is too low quality to be used as a report.");
/*  4155 */             } else if (mover != null) {
/*  4156 */               mover.getCommunicator().sendNormalServerMessage("The " + 
/*  4157 */                   getName() + " is too low quality to be used as a report.");
/*  4158 */             }  return false;
/*       */           } 
/*       */         }
/*  4161 */         if (mayInsert || (itemOwner != null && itemOwner.getPower() > 0) || (targetOwner != null && targetOwner
/*  4162 */           .getPower() > 0)) {
/*       */           
/*  4164 */           if (parent != null) {
/*       */             
/*  4166 */             if (!hasSameOwner(target)) {
/*       */               
/*  4168 */               parent.dropItem(this.id, false);
/*  4169 */               if (parent.getTemplateId() == 177) {
/*       */                 
/*  4171 */                 parent.removeFromPile(this, !lastMove);
/*  4172 */                 if (parent.getItemCount() < 3 && lastMove) {
/*       */                   
/*  4174 */                   for (Creature iwatcher : parent.getWatchers()) {
/*  4175 */                     iwatcher.getCommunicator().sendCloseInventoryWindow(parent.getWurmId());
/*       */                   }
/*       */                 }
/*  4178 */                 else if (lastMove) {
/*  4179 */                   parent.updatePile();
/*       */                 } 
/*  4181 */               }  if (targetOwner != null) {
/*       */                 
/*  4183 */                 targetOwner.addItemTaken(this);
/*       */                 
/*  4185 */                 if (parent.isItemSpawn())
/*  4186 */                   targetOwner.addChallengeScore(ChallengePointEnum.ChallengePoint.ITEMSLOOTED.getEnumtype(), 0.1F); 
/*  4187 */                 if (pickup) {
/*  4188 */                   MissionTriggers.activateTriggers(mover, this, 6, -10L, 1);
/*       */                 }
/*  4190 */               } else if (itemOwner != null) {
/*       */                 
/*  4192 */                 itemOwner.addItemDropped(this);
/*       */               }
/*       */             
/*       */             } else {
/*       */               
/*  4197 */               if (parent.getTemplateId() == 177) {
/*       */                 
/*  4199 */                 parent.dropItem(this.id, false);
/*  4200 */                 parent.removeFromPile(this, !lastMove);
/*       */                 
/*  4202 */                 if (parent.getItemCount() < 3 && lastMove) {
/*       */                   
/*  4204 */                   for (Creature iwatcher : parent.getWatchers()) {
/*  4205 */                     iwatcher.getCommunicator().sendCloseInventoryWindow(parent.getWurmId());
/*       */                   }
/*       */                 }
/*  4208 */                 else if (lastMove) {
/*  4209 */                   parent.updatePile();
/*       */                 }
/*       */               
/*  4212 */               } else if (getTopParentOrNull() != null && getTopParentOrNull().getTemplateId() == 177) {
/*       */                 
/*  4214 */                 getTopParentOrNull().dropItem(this.id, false);
/*       */               } 
/*  4216 */               parent.removeItem(this.id, false, false, false);
/*       */             }
/*       */           
/*       */           } else {
/*       */             
/*  4221 */             if (targetOwner != null) {
/*       */               
/*  4223 */               targetOwner.addItemTaken(this);
/*       */               
/*  4225 */               if (pickup) {
/*  4226 */                 MissionTriggers.activateTriggers(mover, this, 6, -10L, 1);
/*       */               }
/*       */             } 
/*       */             try {
/*  4230 */               Zone z = Zones.getZone(getTilePos(), isOnSurface());
/*  4231 */               z.removeItem(this);
/*       */             }
/*  4233 */             catch (NoSuchZoneException noSuchZoneException) {}
/*       */           } 
/*       */ 
/*       */           
/*  4237 */           if (!isLocked())
/*       */           {
/*  4239 */             setLastOwnerId(mover.getWurmId());
/*       */           }
/*  4241 */           target.insertItem(this);
/*  4242 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  4246 */         if (insertTarget != null)
/*       */         {
/*  4248 */           if (itemOwner != null) {
/*  4249 */             itemOwner.getCommunicator().sendNormalServerMessage("The " + insertTarget
/*  4250 */                 .getName() + " contains too many items already.");
/*  4251 */           } else if (targetOwner != null) {
/*  4252 */             targetOwner.getCommunicator().sendNormalServerMessage("The " + insertTarget
/*  4253 */                 .getName() + " contains too many items already.");
/*       */           }  } 
/*  4255 */         return false;
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/*  4260 */       if (target.isHollow()) {
/*       */         
/*  4262 */         if (itemOwner != null) {
/*  4263 */           itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4264 */               getName() + " will not fit in the " + target.getName() + ".");
/*  4265 */         } else if (targetOwner != null) {
/*  4266 */           targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4267 */               getName() + " will not fit in the " + target.getName() + ".");
/*  4268 */         } else if (mover != null) {
/*  4269 */           mover.getCommunicator().sendNormalServerMessage("The " + 
/*  4270 */               getName() + " will not fit in the " + target.getName() + ".");
/*  4271 */         }  return false;
/*       */       } 
/*       */ 
/*       */       
/*  4275 */       Item cont = Items.getItem(target.getParentId());
/*  4276 */       if (!cont.isBodyPart())
/*       */       {
/*  4278 */         if (cont.mayCreatureInsertItem() || (itemOwner != null && itemOwner.getPower() > 0) || (targetOwner != null && targetOwner
/*  4279 */           .getPower() > 0)) {
/*       */           
/*  4281 */           if (cont.testInsertItem(this))
/*       */           {
/*  4283 */             if (parent != null) {
/*       */               
/*  4285 */               if (!hasSameOwner(target)) {
/*       */                 
/*  4287 */                 parent.dropItem(this.id, false);
/*  4288 */                 if (parent.getTemplateId() == 177)
/*  4289 */                   parent.removeFromPile(this); 
/*  4290 */                 if (targetOwner != null) {
/*       */                   
/*  4292 */                   targetOwner.addItemTaken(this);
/*  4293 */                   if (pickup) {
/*  4294 */                     MissionTriggers.activateTriggers(mover, this, 6, -10L, 1);
/*       */                   }
/*  4296 */                 } else if (itemOwner != null) {
/*       */                   
/*  4298 */                   itemOwner.addItemDropped(this);
/*       */                 }
/*       */               
/*       */               } else {
/*       */                 
/*  4303 */                 if (parent.getTemplateId() == 177)
/*  4304 */                   parent.removeFromPile(this); 
/*  4305 */                 parent.removeItem(this.id, false, false, false);
/*  4306 */                 toReturn = true;
/*       */               }
/*       */             
/*       */             } else {
/*       */               
/*  4311 */               if (targetOwner != null) {
/*       */                 
/*  4313 */                 targetOwner.addItemTaken(this);
/*  4314 */                 if (pickup) {
/*  4315 */                   MissionTriggers.activateTriggers(mover, this, 6, -10L, 1);
/*       */                 }
/*       */               } 
/*       */               try {
/*  4319 */                 Zone z = Zones.getZone(getTilePos(), isOnSurface());
/*  4320 */                 z.removeItem(this);
/*       */               }
/*  4322 */               catch (NoSuchZoneException noSuchZoneException) {}
/*       */             } 
/*       */ 
/*       */             
/*  4326 */             setLastOwnerId(mover.getWurmId());
/*  4327 */             cont.insertItem(this);
/*       */           }
/*       */           else
/*       */           {
/*  4331 */             if (itemOwner != null) {
/*  4332 */               itemOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4333 */                   getName() + " will not fit in the " + cont.getName() + ".");
/*  4334 */             } else if (targetOwner != null) {
/*  4335 */               targetOwner.getCommunicator().sendNormalServerMessage("The " + 
/*  4336 */                   getName() + " will not fit in the " + cont.getName() + ".");
/*  4337 */             }  return false;
/*       */           }
/*       */         
/*       */         } else {
/*       */           
/*  4342 */           if (itemOwner != null) {
/*  4343 */             itemOwner.getCommunicator().sendNormalServerMessage("The " + cont
/*  4344 */                 .getName() + " contains too many items already.");
/*  4345 */           } else if (targetOwner != null) {
/*  4346 */             targetOwner.getCommunicator().sendNormalServerMessage("The " + cont
/*  4347 */                 .getName() + " contains too many items already.");
/*  4348 */           }  return false;
/*       */         } 
/*       */       }
/*       */     } 
/*       */     
/*  4353 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean AddBulkItemToCrate(Creature mover, Item target) {
/*  4358 */     int remainingSpaces = target.getRemainingCrateSpace();
/*  4359 */     if (remainingSpaces <= 0)
/*  4360 */       return false; 
/*  4361 */     byte auxToCheck = 0;
/*  4362 */     if (usesFoodState())
/*       */     {
/*       */       
/*  4365 */       if (isFresh() || isLive()) {
/*  4366 */         auxToCheck = (byte)(getAuxData() & Byte.MAX_VALUE);
/*       */       } else {
/*  4368 */         auxToCheck = getAuxData();
/*       */       }  } 
/*  4370 */     Item toaddTo = target.getItemWithTemplateAndMaterial(getTemplateId(), getMaterial(), auxToCheck, getRealTemplateId());
/*       */     
/*  4372 */     if (toaddTo != null) {
/*       */       
/*  4374 */       if (MethodsItems.checkIfStealing(toaddTo, mover, null)) {
/*       */         
/*  4376 */         int tilex = (int)toaddTo.getPosX() >> 2;
/*  4377 */         int tiley = (int)toaddTo.getPosY() >> 2;
/*  4378 */         Village vil = Zones.getVillage(tilex, tiley, mover.isOnSurface());
/*  4379 */         if (mover.isLegal() && vil != null) {
/*       */           
/*  4381 */           mover.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.");
/*       */           
/*  4383 */           return false;
/*       */         } 
/*  4385 */         if (mover.getDeity() != null && !mover.getDeity().isLibila())
/*       */         {
/*  4387 */           if (mover.faithful) {
/*       */             
/*  4389 */             mover.getCommunicator().sendNormalServerMessage("Your deity would never allow stealing.");
/*  4390 */             return false;
/*       */           } 
/*       */         }
/*       */       } 
/*  4394 */       float percent = 1.0F;
/*  4395 */       if (!isFish() || getTemplateId() == 369)
/*  4396 */         percent = getWeightGrams() / this.template.getWeightGrams(); 
/*  4397 */       boolean destroyOriginal = true;
/*  4398 */       if (percent > remainingSpaces) {
/*       */         
/*  4400 */         percent = Math.min(remainingSpaces, percent);
/*  4401 */         destroyOriginal = false;
/*       */       } 
/*       */       
/*  4404 */       int templWeight = this.template.getWeightGrams();
/*  4405 */       Item tempItem = null;
/*  4406 */       if (!destroyOriginal) {
/*       */         
/*       */         try {
/*       */           
/*  4410 */           int newWeight = (int)(templWeight * percent);
/*  4411 */           tempItem = ItemFactory.createItem(this.template.templateId, getCurrentQualityLevel(), getMaterial(), (byte)0, null);
/*       */           
/*  4413 */           tempItem.setWeight(newWeight, true);
/*  4414 */           if (usesFoodState())
/*  4415 */             tempItem.setAuxData(auxToCheck); 
/*  4416 */           setWeight(getWeightGrams() - newWeight, true);
/*       */         }
/*  4418 */         catch (NoSuchTemplateException nst) {
/*       */           
/*  4420 */           logWarn("Adding to crate failed (missing template?).");
/*  4421 */           logWarn(nst.getMessage(), (Throwable)nst);
/*  4422 */           return false;
/*       */         }
/*  4424 */         catch (FailedException fe) {
/*       */           
/*  4426 */           logWarn("Adding to crate failed to create temp item.");
/*  4427 */           logWarn(fe.getMessage(), (Throwable)fe);
/*  4428 */           return false;
/*       */         } 
/*       */       }
/*  4431 */       if (tempItem == null) {
/*  4432 */         tempItem = this;
/*       */       }
/*  4434 */       float existingNumsBulk = toaddTo.getBulkNumsFloat(false);
/*       */       
/*  4436 */       float percentAdded = percent / (existingNumsBulk + percent);
/*  4437 */       float qlDiff = toaddTo.getQualityLevel() - getCurrentQualityLevel();
/*  4438 */       float qlChange = percentAdded * qlDiff;
/*  4439 */       if (qlDiff > 0.0F) {
/*       */         
/*  4441 */         float newQl = toaddTo.getQualityLevel() - qlChange * 1.1F;
/*  4442 */         toaddTo.setQualityLevel(Math.max(1.0F, newQl));
/*       */       }
/*  4444 */       else if (qlDiff < 0.0F) {
/*       */         
/*  4446 */         float newQl = toaddTo.getQualityLevel() - qlChange * 0.9F;
/*  4447 */         toaddTo.setQualityLevel(Math.max(1.0F, newQl));
/*       */       } 
/*  4449 */       toaddTo.setWeight(toaddTo.getWeightGrams() + (int)(percent * this.template.getVolume()), true);
/*  4450 */       if (destroyOriginal) {
/*  4451 */         Items.destroyItem(getWurmId());
/*       */       } else {
/*  4453 */         Items.destroyItem(tempItem.getWurmId());
/*       */       } 
/*  4455 */       mover.achievement(167, 1);
/*  4456 */       target.updateModelNameOnGroundItem();
/*  4457 */       return true;
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*       */     try {
/*  4464 */       toaddTo = ItemFactory.createItem(669, getCurrentQualityLevel(), getMaterial(), (byte)0, null);
/*       */       
/*  4466 */       toaddTo.setRealTemplate(getTemplateId());
/*  4467 */       if (usesFoodState()) {
/*       */         
/*  4469 */         toaddTo.setAuxData(auxToCheck);
/*  4470 */         if (getRealTemplateId() != -10)
/*  4471 */           toaddTo.setData1(getRealTemplateId()); 
/*  4472 */         toaddTo.setName(getActualName());
/*  4473 */         ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/*  4474 */         if (imd != null)
/*       */         {
/*       */           
/*  4477 */           ItemMealData.save(toaddTo.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd.getCarbs(), imd.getFats(), imd
/*  4478 */               .getProteins(), imd.getBonus(), imd.getStages(), imd.getIngredients());
/*       */         }
/*       */       } 
/*  4481 */       float percent = 1.0F;
/*       */       
/*  4483 */       if (!isFish() || getTemplateId() == 369)
/*  4484 */         percent = getWeightGrams() / this.template.getWeightGrams(); 
/*  4485 */       boolean destroy = true;
/*  4486 */       if (percent > remainingSpaces) {
/*       */         
/*  4488 */         percent = Math.min(remainingSpaces, percent);
/*  4489 */         destroy = false;
/*       */       } 
/*  4491 */       if (!toaddTo.setWeight((int)(percent * this.template.getVolume()), true))
/*  4492 */         target.insertItem(toaddTo, true); 
/*  4493 */       if (destroy) {
/*  4494 */         Items.destroyItem(getWurmId());
/*       */       } else {
/*       */         
/*  4497 */         int remove = (int)(this.template.getWeightGrams() * percent);
/*  4498 */         setWeight(getWeightGrams() - remove, true);
/*       */       } 
/*  4500 */       mover.achievement(167, 1);
/*  4501 */       target.updateModelNameOnGroundItem();
/*  4502 */       toaddTo.setLastOwnerId(mover.getWurmId());
/*  4503 */       return true;
/*       */     }
/*  4505 */     catch (NoSuchTemplateException|FailedException e) {
/*       */       
/*  4507 */       logWarn(e.getMessage(), (Throwable)e);
/*       */ 
/*       */       
/*  4510 */       return false;
/*       */     } 
/*       */   }
/*       */   
/*       */   private static boolean insertOverrideNoPut(Item item, Item target) {
/*  4515 */     if ((item.isShard() || item.isOre()) && 
/*  4516 */       target.isWarmachine())
/*  4517 */       return true; 
/*  4518 */     if (item.getTemplateId() == 1139 && target.getTemplateId() == 1175)
/*  4519 */       return true; 
/*  4520 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean AddBulkItem(Creature mover, Item target) {
/*  4525 */     boolean full = target.isFull();
/*  4526 */     byte auxToCheck = 0;
/*  4527 */     if (usesFoodState())
/*       */     {
/*       */       
/*  4530 */       if (isFresh() || isLive()) {
/*  4531 */         auxToCheck = (byte)(getAuxData() & Byte.MAX_VALUE);
/*       */       } else {
/*  4533 */         auxToCheck = getAuxData();
/*       */       } 
/*       */     }
/*  4536 */     Item toaddTo = target.getItemWithTemplateAndMaterial(getTemplateId(), getMaterial(), auxToCheck, getRealTemplateId());
/*  4537 */     if (toaddTo != null) {
/*       */       
/*  4539 */       if (MethodsItems.checkIfStealing(toaddTo, mover, null)) {
/*       */         
/*  4541 */         int tilex = (int)toaddTo.getPosX() >> 2;
/*  4542 */         int tiley = (int)toaddTo.getPosY() >> 2;
/*  4543 */         Village vil = Zones.getVillage(tilex, tiley, mover.isOnSurface());
/*  4544 */         if (mover.isLegal() && vil != null) {
/*       */           
/*  4546 */           mover.getCommunicator().sendNormalServerMessage("That would be illegal here. You can check the settlement token for the local laws.");
/*       */           
/*  4548 */           return false;
/*       */         } 
/*  4550 */         if (mover.getDeity() != null && !mover.getDeity().isLibila())
/*       */         {
/*  4552 */           if (mover.faithful) {
/*       */             
/*  4554 */             mover.getCommunicator().sendNormalServerMessage("Your deity would never allow stealing.");
/*  4555 */             return false;
/*       */           } 
/*       */         }
/*       */       } 
/*  4559 */       float existingNumsBulk = toaddTo.getBulkNumsFloat(false);
/*       */       
/*  4561 */       float percent = 1.0F;
/*  4562 */       if (!isFish() || getTemplateId() == 369) {
/*  4563 */         percent = getWeightGrams() / this.template.getWeightGrams();
/*       */       }
/*  4565 */       float percentAdded = percent / (existingNumsBulk + percent);
/*  4566 */       float qlDiff = toaddTo.getQualityLevel() - getCurrentQualityLevel();
/*  4567 */       float qlChange = percentAdded * qlDiff;
/*  4568 */       if (qlDiff > 0.0F) {
/*       */         
/*  4570 */         float newQl = toaddTo.getQualityLevel() - qlChange * 1.1F;
/*  4571 */         toaddTo.setQualityLevel(Math.max(1.0F, newQl));
/*       */       }
/*  4573 */       else if (qlDiff < 0.0F) {
/*       */         
/*  4575 */         float newQl = toaddTo.getQualityLevel() - qlChange * 0.9F;
/*  4576 */         toaddTo.setQualityLevel(Math.max(1.0F, newQl));
/*       */       } 
/*       */       
/*  4579 */       toaddTo.setWeight(toaddTo.getWeightGrams() + (int)(percent * this.template.getVolume()), true);
/*  4580 */       Items.destroyItem(getWurmId());
/*  4581 */       mover.achievement(167, 1);
/*  4582 */       if (full != target.isFull()) {
/*  4583 */         target.updateModelNameOnGroundItem();
/*       */       }
/*  4585 */       return true;
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*       */     try {
/*  4591 */       toaddTo = ItemFactory.createItem(669, getCurrentQualityLevel(), getMaterial(), (byte)0, null);
/*       */       
/*  4593 */       toaddTo.setRealTemplate(getTemplateId());
/*  4594 */       if (usesFoodState()) {
/*       */         
/*  4596 */         toaddTo.setAuxData(auxToCheck);
/*  4597 */         if (getRealTemplateId() != -10)
/*  4598 */           toaddTo.setData1(getRealTemplateId()); 
/*  4599 */         toaddTo.setName(getActualName());
/*  4600 */         ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/*  4601 */         if (imd != null)
/*       */         {
/*       */           
/*  4604 */           ItemMealData.save(toaddTo.getWurmId(), imd.getRecipeId(), imd.getCalories(), imd.getCarbs(), imd.getFats(), imd
/*  4605 */               .getProteins(), imd.getBonus(), imd.getStages(), imd.getIngredients());
/*       */         }
/*       */       } 
/*  4608 */       float percent = 1.0F;
/*       */       
/*  4610 */       if (!isFish() || getTemplateId() == 369) {
/*  4611 */         percent = getWeightGrams() / this.template.getWeightGrams();
/*       */       }
/*       */       
/*  4614 */       if (!toaddTo.setWeight((int)(percent * this.template.getVolume()), true))
/*  4615 */         target.insertItem(toaddTo, true); 
/*  4616 */       Items.destroyItem(getWurmId());
/*  4617 */       mover.achievement(167, 1);
/*  4618 */       if (full != target.isFull())
/*  4619 */         target.updateModelNameOnGroundItem(); 
/*  4620 */       toaddTo.setLastOwnerId(mover.getWurmId());
/*  4621 */       return true;
/*       */     }
/*  4623 */     catch (NoSuchTemplateException nst) {
/*       */       
/*  4625 */       logWarn(nst.getMessage(), (Throwable)nst);
/*       */     }
/*  4627 */     catch (FailedException fe) {
/*       */       
/*  4629 */       logWarn(fe.getMessage(), (Throwable)fe);
/*       */     } 
/*       */     
/*  4632 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeFromPile(Item item) {
/*       */     try {
/*  4640 */       Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  4641 */       zone.removeItem(item);
/*       */     }
/*  4643 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  4645 */       logWarn("Removed from nonexistant zone " + this.id);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void removeFromPile(Item item, boolean moving) {
/*       */     try {
/*  4654 */       Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  4655 */       zone.removeItem(item, moving, false);
/*       */     }
/*  4657 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  4659 */       logWarn("Removed from nonexistant zone " + this.id);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void updatePile() {
/*       */     try {
/*  4668 */       Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  4669 */       zone.updatePile(this);
/*       */     }
/*  4671 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  4673 */       logWarn("Removed from nonexistant zone " + this.id);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final void putInVoid() {
/*  4679 */     if (this.parentId != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  4683 */         Item parent = Items.getItem(this.parentId);
/*  4684 */         parent.dropItem(this.id, false);
/*       */       }
/*  4686 */       catch (NoSuchItemException nsi) {
/*       */         
/*  4688 */         logWarn(this.id + " had a parent that could not be found.", (Throwable)nsi);
/*       */       } 
/*       */     }
/*  4691 */     if (this.zoneId != -10) {
/*       */       
/*       */       try {
/*       */         
/*  4695 */         Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  4696 */         zone.removeItem(this);
/*       */       }
/*  4698 */       catch (NoSuchZoneException nsz) {
/*       */         
/*  4700 */         logWarn("No such zone: " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + "," + 
/*  4701 */             isOnSurface() + "?", (Throwable)nsz);
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void insertIntoPile(Item item) {
/*       */     try {
/*  4710 */       item.setPosXYZ(getPosX(), getPosY(), getPosZ());
/*  4711 */       Zone zone = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*       */ 
/*       */       
/*  4714 */       zone.addItem(item);
/*       */     }
/*  4716 */     catch (NoSuchZoneException nsz) {
/*       */       
/*  4718 */       logWarn("added to nonexistant zone " + this.id);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final long getOwner() throws NotOwnedException {
/*  4724 */     long lOwnerId = getOwnerId();
/*  4725 */     if (lOwnerId == -10L)
/*  4726 */       throw new NotOwnedException("Not owned item"); 
/*  4727 */     return lOwnerId;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private Creature getOwnerOrNull() {
/*       */     try {
/*  4734 */       return Server.getInstance().getCreature(getOwnerId());
/*       */     }
/*  4736 */     catch (NoSuchCreatureException nsc) {
/*       */       
/*  4738 */       logWarn(nsc.getMessage(), (Throwable)nsc);
/*       */     }
/*  4740 */     catch (NoSuchPlayerException nsp) {
/*       */       
/*  4742 */       PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(getOwnerId());
/*  4743 */       if (info == null)
/*  4744 */         logWarn(nsp.getMessage(), (Throwable)nsp); 
/*       */     } 
/*  4746 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getSurfaceArea() {
/*  4755 */     float modifier = 1.0F;
/*  4756 */     if (getSpellEffects() != null) {
/*  4757 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*       */     }
/*  4759 */     if (isLiquid()) {
/*  4760 */       return (int)(getWeightGrams() * modifier);
/*       */     }
/*  4762 */     if (!this.template.usesSpecifiedContainerSizes()) {
/*  4763 */       return 
/*       */         
/*  4765 */         (int)((getSizeX() * getSizeY() * 2 + getSizeY() * getSizeZ() * 2) + (getSizeX() * getSizeZ() * 2) * modifier);
/*       */     }
/*  4767 */     return 
/*       */       
/*  4769 */       (int)((this.template.getSizeX() * this.template.getSizeY() * 2 + this.template.getSizeY() * this.template.getSizeZ() * 2) + (this.template.getSizeX() * this.template.getSizeZ() * 2) * modifier);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getVolume() {
/*  4780 */     float modifier = 1.0F;
/*  4781 */     if (getSpellEffects() != null)
/*       */     {
/*  4783 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_VOLUME);
/*       */     }
/*       */     
/*  4786 */     if (isLiquid())
/*  4787 */       return (int)(getWeightGrams() * modifier); 
/*  4788 */     if (this.internalVolume != 0)
/*       */     {
/*  4790 */       return (int)(this.internalVolume * modifier);
/*       */     }
/*  4792 */     if (!this.template.usesSpecifiedContainerSizes())
/*       */     {
/*  4794 */       return (int)((getSizeX() * getSizeY() * getSizeZ()) * modifier);
/*       */     }
/*       */ 
/*       */     
/*  4798 */     return (int)((this.template.getSizeX() * this.template.getSizeY() * this.template.getSizeZ()) * modifier);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int setInternalVolumeFromAuxByte() {
/*  4805 */     int newVolume = 10;
/*  4806 */     switch (getAuxData())
/*       */     
/*       */     { case 12:
/*  4809 */         newVolume = 1;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  4847 */         this.internalVolume = newVolume;
/*  4848 */         return this.internalVolume;case 11: newVolume = 2; this.internalVolume = newVolume; return this.internalVolume;case 10: newVolume = 5; this.internalVolume = newVolume; return this.internalVolume;case 9: newVolume = 10; this.internalVolume = newVolume; return this.internalVolume;case 8: newVolume = 20; this.internalVolume = newVolume; return this.internalVolume;case 7: newVolume = 50; this.internalVolume = newVolume; return this.internalVolume;case 6: newVolume = 100; this.internalVolume = newVolume; return this.internalVolume;case 5: newVolume = 200; this.internalVolume = newVolume; return this.internalVolume;case 4: newVolume = 500; this.internalVolume = newVolume; return this.internalVolume;case 3: newVolume = 1000; this.internalVolume = newVolume; return this.internalVolume;case 2: newVolume = 2000; this.internalVolume = newVolume; return this.internalVolume;case 1: newVolume = 5000; this.internalVolume = newVolume; return this.internalVolume; }  newVolume = 10000; this.internalVolume = newVolume; return this.internalVolume;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getContainerVolume() {
/*  4858 */     float modifier = 1.0F;
/*  4859 */     if (getSpellEffects() != null) {
/*  4860 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_VOLUME);
/*       */     }
/*  4862 */     if (this.internalVolume != 0)
/*  4863 */       return (int)(this.internalVolume * modifier); 
/*  4864 */     if (this.template.usesSpecifiedContainerSizes()) {
/*  4865 */       return (int)(this.template.getContainerVolume() * modifier);
/*       */     }
/*  4867 */     return getVolume();
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSizeMod() {
/*  4872 */     float minMod = (getTemplateId() == 344) ? 0.3F : 0.8F;
/*  4873 */     if (getTemplateId() == 272 || isFish() || getTemplateId() == 344 || (isCombine() && getWeightGrams() > 5000))
/*  4874 */       return Math.max(minMod, Math.min(5.0F, (float)cubeRoot((getVolume() / this.template.getVolume())))); 
/*  4875 */     TreeData.TreeType ttype = Materials.getTreeTypeForWood(getMaterial());
/*  4876 */     if (ttype != null && !ttype.isFruitTree())
/*       */     {
/*  4878 */       if (getTemplateId() == 731 || getTemplateId() == 385) {
/*       */ 
/*       */ 
/*       */         
/*  4882 */         float ageScale = (((getAuxData() >= 100) ? (getAuxData() - 100) : getAuxData()) + 1) / 16.0F;
/*  4883 */         return ageScale * 16.0F;
/*       */       } 
/*       */     }
/*       */     
/*  4887 */     float modifier = 1.0F;
/*  4888 */     if (getSpellEffects() != null)
/*       */     {
/*  4890 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*       */     }
/*       */     
/*  4893 */     return 1.0F * modifier;
/*       */   }
/*       */ 
/*       */   
/*       */   public static double cubeRoot(double x) {
/*  4898 */     return Math.pow(x, 0.3333333333333333D);
/*       */   }
/*       */ 
/*       */   
/*       */   private int getUsedVolume() {
/*  4903 */     int used = 0;
/*  4904 */     for (Iterator<Item> it = getItems().iterator(); it.hasNext(); ) {
/*       */       
/*  4906 */       Item i = it.next();
/*  4907 */       if (!i.isInventoryGroup()) {
/*       */         
/*  4909 */         if (i.isLiquid() || i.isBulkItem()) {
/*  4910 */           used += i.getWeightGrams(); continue;
/*       */         } 
/*  4912 */         used += i.getVolume();
/*       */         continue;
/*       */       } 
/*  4915 */       used += i.getUsedVolume();
/*       */     } 
/*  4917 */     return used;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasSpaceFor(int volume) {
/*  4922 */     if (isInventory())
/*  4923 */       return true; 
/*  4924 */     return (getContainerVolume() - getUsedVolume() > volume);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean canAddToCrate(Item toAdd) {
/*  4929 */     if (!isCrate())
/*  4930 */       return false; 
/*  4931 */     if (!toAdd.isBulk()) {
/*  4932 */       return false;
/*       */     }
/*  4934 */     return (getRemainingCrateSpace() > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getRemainingCrateSpace() {
/*  4939 */     int count = 0;
/*  4940 */     Item[] cargo = getItemsAsArray();
/*  4941 */     for (Item cargoItem : cargo) {
/*  4942 */       count += cargoItem.getBulkNums();
/*       */     }
/*       */     
/*  4945 */     if (this.template.templateId == 852) {
/*  4946 */       return 300 - count;
/*       */     }
/*  4948 */     if (this.template.templateId == 851) {
/*  4949 */       return 150 - count;
/*       */     }
/*       */     
/*  4952 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final ItemTemplate getTemplate() {
/*  4961 */     return this.template;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean containsItem() {
/*  4966 */     for (Iterator<Item> it = getItems().iterator(); it.hasNext(); ) {
/*       */       
/*  4968 */       Item next = it.next();
/*  4969 */       if (!next.isBodyPart())
/*       */       {
/*  4971 */         return true;
/*       */       }
/*       */     } 
/*  4974 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean containsItemAndIsSameTypeOfItem(Item item) {
/*  4979 */     for (Iterator<Item> it = getItems().iterator(); it.hasNext(); ) {
/*       */       
/*  4981 */       Item next = it.next();
/*  4982 */       if (!next.isBodyPart())
/*       */       {
/*  4984 */         if (item.getTemplateId() == next.getTemplateId() || (item.isBarding() && next.isBarding()))
/*  4985 */           return true; 
/*       */       }
/*       */     } 
/*  4988 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean containsArtifact() {
/*  4993 */     for (Item item : getAllItems(true, true)) {
/*       */       
/*  4995 */       if (item.isArtifact())
/*  4996 */         return true; 
/*       */     } 
/*  4998 */     return false;
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
/*       */   private boolean containsArmor() {
/*  5016 */     for (Iterator<Item> it = getItems().iterator(); it.hasNext(); ) {
/*       */       
/*  5018 */       Item next = it.next();
/*  5019 */       if (!next.isBodyPart() && next.isArmour())
/*       */       {
/*  5021 */         return true;
/*       */       }
/*       */     } 
/*  5024 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean canHoldItem(Item item) {
/*       */     try {
/*  5032 */       Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  5033 */       if (this.auxbyte != 1 && this.auxbyte != 0 && owner
/*  5034 */         .isPlayer())
/*  5035 */         return false; 
/*  5036 */       Item rightWeapon = owner.getRighthandWeapon();
/*  5037 */       Item leftWeapon = owner.getLefthandWeapon();
/*  5038 */       Item shield = owner.getShield();
/*       */       
/*  5040 */       if (rightWeapon == null && leftWeapon == null && shield == null) {
/*  5041 */         return true;
/*       */       }
/*  5043 */       if (rightWeapon != null && item.isTwoHanded())
/*  5044 */         return false; 
/*  5045 */       byte rHeld = owner.isPlayer() ? 38 : 14;
/*  5046 */       if (rightWeapon != null && this.place == rHeld)
/*  5047 */         return false; 
/*  5048 */       if (rightWeapon != null && rightWeapon.isTwoHanded())
/*  5049 */         return false; 
/*  5050 */       byte lHeld = owner.isPlayer() ? 37 : 13;
/*  5051 */       if (leftWeapon != null && this.place == lHeld)
/*  5052 */         return false; 
/*  5053 */       if (leftWeapon != null && item.isTwoHanded())
/*  5054 */         return false; 
/*  5055 */       if (leftWeapon != null && leftWeapon.isTwoHanded())
/*  5056 */         return false; 
/*  5057 */       if (shield != null && item.isTwoHanded())
/*  5058 */         return false; 
/*  5059 */       if (shield != null && this.place == lHeld) {
/*  5060 */         return false;
/*       */       }
/*  5062 */       return true;
/*       */     }
/*  5064 */     catch (Exception ex) {
/*       */       
/*  5066 */       return false;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertShield(Item item, Creature owner) {
/*  5072 */     if (owner.isPlayer()) {
/*       */       
/*  5074 */       if (getPlace() != 44) {
/*  5075 */         return false;
/*       */       }
/*  5077 */       if (containsItem()) {
/*  5078 */         return false;
/*       */       
/*       */       }
/*       */     }
/*  5082 */     else if (containsItemAndIsSameTypeOfItem(item)) {
/*  5083 */       return false;
/*       */     } 
/*       */     
/*  5086 */     Item leftWeapon = owner.getLefthandWeapon();
/*  5087 */     if (leftWeapon != null)
/*  5088 */       return false; 
/*  5089 */     Item rightWeapon = owner.getRighthandWeapon();
/*  5090 */     if (rightWeapon != null && (rightWeapon.isWeaponBow() || rightWeapon.isTwoHanded()))
/*  5091 */       return false; 
/*  5092 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertItemIntoSlot(Item item, Creature owner) {
/*  5097 */     byte[] slots = item.getBodySpaces();
/*  5098 */     for (int i = 0; i < slots.length; i++) {
/*       */       
/*  5100 */       if (this.place == slots[i])
/*  5101 */         return !containsItem(); 
/*       */     } 
/*  5103 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertItemIntoAnimalSlot(Item item, Creature owner) {
/*  5108 */     byte[] slots = item.getBodySpaces();
/*  5109 */     for (int i = 0; i < slots.length; i++) {
/*       */       
/*  5111 */       if (this.place == slots[i])
/*  5112 */         return !containsItemAndIsSameTypeOfItem(item); 
/*       */     } 
/*  5114 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertWeapon(Item item, Creature owner) {
/*  5119 */     if (!canHoldItem(item) || (containsItem() && !containsArmor())) {
/*  5120 */       return false;
/*       */     }
/*  5122 */     if (!owner.hasHands()) {
/*       */       
/*  5124 */       boolean found = false;
/*  5125 */       byte[] armourplaces = item.getBodySpaces();
/*  5126 */       for (int x = 0; x < armourplaces.length; x++) {
/*  5127 */         if (armourplaces[x] == this.place) {
/*       */           
/*  5129 */           found = true; break;
/*       */         } 
/*       */       } 
/*  5132 */       if (!found)
/*  5133 */         return false; 
/*       */     } 
/*  5135 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertItemPlayer(Item item, Creature owner) {
/*  5140 */     if (isBodyPart() && isEquipmentSlot()) {
/*       */       
/*  5142 */       if (item.isArmour() && !item.isShield())
/*  5143 */         return false; 
/*  5144 */       if (item.isShield())
/*  5145 */         return testInsertShield(item, owner); 
/*  5146 */       if (item.getDamagePercent() > 0)
/*  5147 */         return testInsertWeapon(item, owner); 
/*  5148 */       if (this.place == 37 || this.place == 38) {
/*       */ 
/*       */         
/*  5151 */         if (containsItem() || !canHoldItem(item) || item.isInventoryGroup())
/*  5152 */           return false; 
/*  5153 */         return true;
/*       */       } 
/*       */       
/*  5156 */       return testInsertItemIntoSlot(item, owner);
/*       */     } 
/*       */     
/*  5159 */     if (isBodyPart() && !isEquipmentSlot()) {
/*       */       
/*  5161 */       if (item.isBarding())
/*  5162 */         return false; 
/*  5163 */       if ((!item.isArmour() && !item.isBracelet()) || item.isShield())
/*  5164 */         return false; 
/*  5165 */       return testInsertItemIntoSlot(item, owner);
/*       */     } 
/*  5167 */     if (isHollow())
/*       */     {
/*  5169 */       return testInsertHollowItem(item, false);
/*       */     }
/*       */ 
/*       */     
/*  5173 */     Item insertTarget = getInsertItem();
/*  5174 */     if (insertTarget == null) {
/*  5175 */       return false;
/*       */     }
/*  5177 */     return insertTarget.testInsertItem(item);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean bodyPartIsWeaponSlotNPC() {
/*  5183 */     return (this.place == 13 || this.place == 14);
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertHollowItem(Item item, boolean testItemCount) {
/*  5188 */     if (isNoDrop())
/*       */     {
/*  5190 */       if (item.isArtifact())
/*  5191 */         return false; 
/*       */     }
/*  5193 */     if (Features.Feature.FREE_ITEMS.isEnabled())
/*       */     {
/*  5195 */       if (item.isChallengeNewbieItem())
/*       */       {
/*  5197 */         if (this.ownerId == -10L && (
/*  5198 */           item.isArmour() || item.isWeapon() || item.isShield()))
/*       */         {
/*  5200 */           return false;
/*       */         }
/*       */       }
/*       */     }
/*       */ 
/*       */     
/*  5206 */     int freevol = getFreeVolume();
/*       */     
/*  5208 */     if (itemCanBeInserted(item))
/*       */     {
/*  5210 */       if (getTemplateId() == 177 || getTemplateId() == 0 || freevol >= item
/*  5211 */         .getVolume() || 
/*  5212 */         doesContainerRestrictionsAllowItem(item)) {
/*       */         
/*  5214 */         if (getTemplateId() == 621 && item.isSaddleBags()) {
/*       */           
/*  5216 */           Item parent = getParentOrNull();
/*  5217 */           if (parent != null && parent.isSaddleBags())
/*  5218 */             return false; 
/*       */         } 
/*  5220 */         if (testItemCount) {
/*  5221 */           return mayCreatureInsertItem();
/*       */         }
/*  5223 */         return true;
/*       */       } 
/*       */     }
/*  5226 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertHumanoidNPC(Item item, Creature owner) {
/*  5231 */     if (isBodyPart()) {
/*       */       
/*  5233 */       if (item.isShield() && this.place == 3)
/*  5234 */         return testInsertShield(item, owner); 
/*  5235 */       if (item.getDamagePercent() > 0 && bodyPartIsWeaponSlotNPC()) {
/*  5236 */         return testInsertWeapon(item, owner);
/*       */       }
/*  5238 */       return testInsertItemIntoAnimalSlot(item, owner);
/*       */     } 
/*  5240 */     if (isHollow())
/*  5241 */       return testInsertHollowItem(item, false); 
/*  5242 */     if (!isHollow()) {
/*       */       
/*  5244 */       Item insertTarget = getInsertItem();
/*  5245 */       if (insertTarget == null) {
/*  5246 */         return false;
/*       */       }
/*  5248 */       return insertTarget.testInsertItem(item);
/*       */     } 
/*       */     
/*  5251 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean testInsertAnimal(Item item, Creature owner) {
/*  5256 */     if (isBodyPart())
/*  5257 */       return testInsertItemIntoAnimalSlot(item, owner); 
/*  5258 */     if (isHollow())
/*       */     {
/*  5260 */       return testInsertHollowItem(item, false);
/*       */     }
/*  5262 */     if (!isHollow()) {
/*       */       
/*  5264 */       Item insertTarget = getInsertItem();
/*  5265 */       if (insertTarget == null) {
/*  5266 */         return false;
/*       */       }
/*  5268 */       return insertTarget.testInsertItem(item);
/*       */     } 
/*       */     
/*  5271 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item getInsertItem() {
/*  5276 */     if (isBodyPart() || isEquipmentSlot() || isHollow()) {
/*  5277 */       return this;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/*  5282 */       return getParent().getInsertItem();
/*       */     }
/*  5284 */     catch (NoSuchItemException nsi) {
/*       */       
/*  5286 */       return null;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean testInsertItem(Item item) {
/*  5293 */     if (item == this)
/*       */     {
/*  5295 */       return false;
/*       */     }
/*  5297 */     Creature owner = null;
/*       */     
/*       */     try {
/*  5300 */       owner = Server.getInstance().getCreature(this.ownerId);
/*  5301 */       if (owner.isPlayer())
/*  5302 */         return testInsertItemPlayer(item, owner); 
/*  5303 */       if (owner.isAnimal()) {
/*  5304 */         return testInsertAnimal(item, owner);
/*       */       }
/*  5306 */       return testInsertHumanoidNPC(item, owner);
/*       */     }
/*  5308 */     catch (NoSuchPlayerException nsp) {
/*       */       
/*  5310 */       if (Features.Feature.FREE_ITEMS.isEnabled())
/*       */       {
/*  5312 */         if (item.isChallengeNewbieItem())
/*       */         {
/*  5314 */           if (item.isArmour() || item.isWeapon() || item.isShield())
/*       */           {
/*  5316 */             return false;
/*       */           }
/*       */         }
/*       */       }
/*  5320 */       if (isHollow())
/*       */       {
/*  5322 */         return testInsertHollowItem(item, true);
/*       */       }
/*       */ 
/*       */       
/*  5326 */       Item insertTarget = getInsertItem();
/*  5327 */       if (insertTarget == null) {
/*  5328 */         return false;
/*       */       }
/*  5330 */       return insertTarget.testInsertItem(item);
/*       */     
/*       */     }
/*  5333 */     catch (NoSuchCreatureException nsc) {
/*       */       
/*  5335 */       String msg = "Unable to find owner for body part (creature). Part: " + this.name + " ownerID: " + this.ownerId;
/*  5336 */       logWarn(msg, (Throwable)nsc);
/*  5337 */       return false;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getFreeVolume() {
/*  5347 */     return getContainerVolume() - getUsedVolume();
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item getFirstContainedItem() {
/*  5352 */     Item[] contained = getItemsAsArray();
/*  5353 */     if (contained == null || contained.length == 0)
/*  5354 */       return null; 
/*  5355 */     return contained[0];
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean insertItem(Item item) {
/*  5360 */     return insertItem(item, false, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean insertItem(Item item, boolean unconditionally) {
/*  5365 */     return insertItem(item, unconditionally, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean insertItem(Item item, boolean unconditionally, boolean checkItemCount) {
/*  5370 */     return insertItem(item, unconditionally, checkItemCount, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean insertItem(Item item, boolean unconditionally, boolean checkItemCount, boolean isPlaced) {
/*  5375 */     boolean toReturn = false;
/*  5376 */     if (item == this) {
/*       */       
/*  5378 */       logWarn("Tried to insert same item into an item: ", new Exception());
/*  5379 */       return false;
/*       */     } 
/*  5381 */     if (isBodyPart()) {
/*       */       
/*  5383 */       Item armour = null;
/*  5384 */       Item held = null;
/*  5385 */       if (!item.isBodyPartAttached()) {
/*       */         
/*  5387 */         short lPlace = getPlace();
/*  5388 */         if (item.getDamagePercent() > 0)
/*       */         {
/*  5390 */           if (lPlace == 38 || lPlace == 37)
/*       */           {
/*  5392 */             if (!unconditionally) {
/*       */               
/*       */               try {
/*       */                 
/*  5396 */                 Creature owner = Server.getInstance().getCreature(this.ownerId);
/*       */                 
/*  5398 */                 Item rightWeapon = owner.getRighthandWeapon();
/*  5399 */                 if (rightWeapon != null && item.isTwoHanded())
/*  5400 */                   return false; 
/*  5401 */                 if (rightWeapon != null && lPlace == 38)
/*  5402 */                   return false; 
/*  5403 */                 if (rightWeapon != null && rightWeapon.isTwoHanded())
/*  5404 */                   return false; 
/*  5405 */                 Item leftWeapon = owner.getLefthandWeapon();
/*  5406 */                 if (leftWeapon != null && lPlace == 37)
/*  5407 */                   return false; 
/*  5408 */                 if (leftWeapon != null && item.isTwoHanded())
/*  5409 */                   return false; 
/*  5410 */                 if (leftWeapon != null && leftWeapon.isTwoHanded())
/*  5411 */                   return false; 
/*  5412 */                 Item shield = owner.getShield();
/*  5413 */                 if (shield != null && item.isTwoHanded())
/*  5414 */                   return false; 
/*  5415 */                 if (shield != null && lPlace == 37) {
/*  5416 */                   return false;
/*       */                 }
/*       */                 
/*  5419 */                 if (!owner.hasHands()) {
/*       */                   
/*  5421 */                   boolean found = false;
/*  5422 */                   byte[] armourplaces = item.getBodySpaces();
/*  5423 */                   for (int x = 0; x < armourplaces.length; x++) {
/*  5424 */                     if (armourplaces[x] == lPlace) {
/*       */                       
/*  5426 */                       found = true; break;
/*       */                     } 
/*       */                   } 
/*  5429 */                   if (!found) {
/*  5430 */                     return false;
/*       */                   }
/*       */                 } 
/*  5433 */               } catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */               
/*       */               }
/*  5437 */               catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */             }
/*       */           }
/*       */         }
/*       */ 
/*       */ 
/*       */         
/*  5444 */         if (item.isShield()) {
/*       */           
/*  5446 */           if (lPlace == 44)
/*       */           {
/*  5448 */             if (!unconditionally) {
/*       */               
/*       */               try {
/*       */                 
/*  5452 */                 Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  5453 */                 Item rightWeapon = owner.getRighthandWeapon();
/*  5454 */                 if (rightWeapon != null && rightWeapon.isTwoHanded())
/*  5455 */                   return false; 
/*  5456 */                 Item leftWeapon = owner.getLefthandWeapon();
/*  5457 */                 if (leftWeapon != null) {
/*  5458 */                   return false;
/*       */                 }
/*  5460 */               } catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */               
/*       */               }
/*  5464 */               catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */             
/*       */             }
/*       */           
/*       */           }
/*  5470 */           else if (lPlace == 13 || lPlace == 14)
/*       */           {
/*       */             
/*       */             try {
/*  5474 */               Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  5475 */               owner.getCommunicator().sendNormalServerMessage("You need to wear the " + item
/*  5476 */                   .getName() + " on the left arm.");
/*  5477 */               return false;
/*       */             }
/*  5479 */             catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */             
/*       */             }
/*  5483 */             catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */           
/*       */           }
/*       */ 
/*       */         
/*       */         }
/*  5489 */         else if (item.isBelt()) {
/*       */           
/*  5491 */           if (lPlace == 43) {
/*       */             
/*       */             try {
/*       */               
/*  5495 */               Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  5496 */               if (owner.getWornBelt() != null) {
/*  5497 */                 return false;
/*       */               }
/*  5499 */             } catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */             
/*       */             }
/*  5503 */             catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */           }
/*       */         } 
/*       */ 
/*       */         
/*  5508 */         if (this.place != 2 || item.getTemplateId() != 740)
/*       */         {
/*  5510 */           for (Iterator<Item> it = getItems().iterator(); it.hasNext(); ) {
/*       */             
/*  5512 */             Item tocheck = it.next();
/*  5513 */             if (tocheck.isBodyPart()) {
/*       */               continue;
/*       */             }
/*       */             
/*  5517 */             if (tocheck.isArmour()) {
/*       */               
/*  5519 */               if (armour == null) {
/*       */                 
/*  5521 */                 byte[] armourplaces = tocheck.getBodySpaces();
/*  5522 */                 for (int x = 0; x < armourplaces.length; x++) {
/*       */                   
/*  5524 */                   if (armourplaces[x] == lPlace)
/*  5525 */                     armour = tocheck; 
/*       */                 } 
/*  5527 */                 if (armour == null) {
/*  5528 */                   held = tocheck;
/*       */                 }
/*       */                 continue;
/*       */               } 
/*  5532 */               if (held == null) {
/*  5533 */                 held = tocheck; continue;
/*  5534 */               }  if (!unconditionally) {
/*  5535 */                 return false;
/*       */               }
/*       */               
/*       */               continue;
/*       */             } 
/*  5540 */             if (held == null) {
/*  5541 */               held = tocheck; continue;
/*  5542 */             }  if (!unconditionally) {
/*  5543 */               return false;
/*       */             }
/*       */           } 
/*       */         }
/*       */       } 
/*       */       
/*  5549 */       if (item.isArmour())
/*       */       
/*  5551 */       { if (this.place == 13 || this.place == 14) {
/*       */           
/*  5553 */           boolean worn = false;
/*  5554 */           if (armour == null) {
/*       */             
/*  5556 */             byte[] armourplaces = item.getBodySpaces();
/*  5557 */             for (int x = 0; x < armourplaces.length; x++) {
/*       */               
/*  5559 */               if (armourplaces[x] == this.place) {
/*       */                 
/*  5561 */                 worn = true;
/*  5562 */                 sendWear(item, (byte)this.place);
/*       */               } 
/*       */             } 
/*       */           } 
/*  5566 */           if (!worn)
/*       */           {
/*  5568 */             if (held != null)
/*       */             {
/*  5570 */               return false;
/*       */             }
/*       */             
/*  5573 */             sendHold(item);
/*       */           }
/*       */         
/*  5576 */         } else if (armour == null) {
/*       */           
/*  5578 */           boolean worn = false;
/*  5579 */           byte[] armourplaces = item.getBodySpaces();
/*  5580 */           for (int x = 0; x < armourplaces.length; x++) {
/*       */             
/*  5582 */             if (armourplaces[x] == this.place) {
/*       */               
/*  5584 */               if (item.isHollow() && !item.isEmpty(false)) {
/*       */ 
/*       */                 
/*       */                 try {
/*  5588 */                   Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  5589 */                   owner.getCommunicator().sendNormalServerMessage("There is not enough room in the " + item
/*  5590 */                       .getName() + " for your " + owner
/*  5591 */                       .getBody().getWoundLocationString(this.place) + " and all the other items in it!");
/*       */                 
/*       */                 }
/*  5594 */                 catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */                 
/*       */                 }
/*  5598 */                 catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */ 
/*       */                 
/*  5602 */                 return false;
/*       */               } 
/*  5604 */               worn = true;
/*  5605 */               sendWear(item, (byte)this.place);
/*       */               
/*  5607 */               if (item.isRoyal())
/*       */               {
/*       */                 
/*  5610 */                 if (this.place == 1 || this.place == 28)
/*       */                 {
/*       */                   
/*  5613 */                   if (item.getTemplateId() == 530 || item
/*  5614 */                     .getTemplateId() == 533 || item
/*  5615 */                     .getTemplateId() == 536)
/*       */                   {
/*       */                     
/*  5618 */                     if (item.getKingdom() != 0) {
/*       */ 
/*       */                       
/*  5621 */                       Kingdom kingdom = Kingdoms.getKingdom(item.getKingdom());
/*  5622 */                       if (kingdom != null && kingdom.existsHere() && kingdom.isCustomKingdom()) {
/*       */                         
/*       */                         try {
/*       */ 
/*       */                           
/*  5627 */                           Creature owner = Server.getInstance().getCreature(getOwnerId());
/*  5628 */                           if (owner.getKingdomId() == item.getKingdom())
/*       */                           {
/*       */                             
/*  5631 */                             if (!owner.isChampion()) {
/*       */ 
/*       */                               
/*  5634 */                               King k = King.getKing(item.getKingdom());
/*  5635 */                               if (k == null || k.kingid != getOwnerId())
/*       */                               {
/*  5637 */                                 King.createKing(item.getKingdom(), owner.getName(), owner
/*  5638 */                                     .getWurmId(), owner.getSex());
/*       */                                 
/*  5640 */                                 NewKingQuestion nk = new NewKingQuestion(owner, "New ruler!", "Congratulations!", owner.getWurmId());
/*       */                                 
/*  5642 */                                 nk.sendQuestion();
/*       */                               }
/*       */                             
/*       */                             } 
/*       */                           }
/*  5647 */                         } catch (NoSuchCreatureException nsc) {
/*       */                           
/*  5649 */                           logWarn(item.getName() + ": " + nsc.getMessage(), (Throwable)nsc);
/*       */                         }
/*  5651 */                         catch (NoSuchPlayerException nsp) {
/*       */                           
/*  5653 */                           logWarn(item.getName() + ": " + nsp.getMessage(), (Throwable)nsp);
/*       */                         } 
/*       */                       }
/*       */                     } 
/*       */                   }
/*       */                 }
/*       */               }
/*       */             } 
/*       */           } 
/*  5662 */           if (!worn)
/*       */           {
/*  5664 */             if (!unconditionally) {
/*  5665 */               return false;
/*       */             
/*       */             }
/*       */           }
/*       */         }
/*  5670 */         else if (!unconditionally) {
/*  5671 */           return false;
/*       */         }
/*       */          }
/*  5674 */       else if (item.isShield())
/*       */       
/*  5676 */       { if (held != null && !unconditionally)
/*  5677 */           return false; 
/*  5678 */         if (this.place == 13 || this.place == 14) {
/*  5679 */           sendHold(item);
/*  5680 */         } else if (this.place == 44) {
/*  5681 */           sendWearShield(item);
/*       */         }  }
/*  5683 */       else if (item.isBelt())
/*       */       
/*  5685 */       { if (held != null && !unconditionally)
/*  5686 */           return false; 
/*  5687 */         if (this.place == 13 || this.place == 14) {
/*  5688 */           sendHold(item);
/*  5689 */         } else if (this.place == 43) {
/*  5690 */           sendWear(item, (byte)this.place);
/*       */         }  }
/*  5692 */       else { if (held != null && !unconditionally)
/*  5693 */           return false; 
/*  5694 */         if (this.place == 37 || this.place == 38)
/*  5695 */         { sendHold(item); }
/*       */         else
/*  5697 */         { sendWear(item, (byte)this.place); }  }
/*  5698 */        addItem(item, false);
/*       */       
/*  5700 */       setThisAsParentFor(item, false);
/*  5701 */       toReturn = true;
/*       */     }
/*  5703 */     else if (isHollow()) {
/*       */       
/*  5705 */       if (isNoDrop())
/*       */       {
/*  5707 */         if (item.isArtifact())
/*  5708 */           return false; 
/*       */       }
/*  5710 */       int freevol = getFreeVolume();
/*  5711 */       if (unconditionally || itemCanBeInserted(item))
/*       */       {
/*       */ 
/*       */         
/*  5715 */         boolean canInsert = true;
/*  5716 */         if (checkItemCount && !unconditionally)
/*  5717 */           canInsert = mayCreatureInsertItem(); 
/*  5718 */         if (unconditionally || 
/*  5719 */           getTemplateId() == 177 || ((
/*  5720 */           getTemplateId() == 0 || freevol >= item.getVolume() || 
/*  5721 */           doesContainerRestrictionsAllowItem(item)) && canInsert)) {
/*       */           
/*  5723 */           if (getTemplateId() == 621 && item.isSaddleBags()) {
/*       */             
/*  5725 */             Item parent = getParentOrNull();
/*  5726 */             if (parent != null && parent.isSaddleBags()) {
/*  5727 */               return false;
/*       */             }
/*       */           } 
/*  5730 */           if (getTemplateId() == 1404)
/*       */           {
/*  5732 */             if (item.getTemplateId() == 1272 || item.getTemplateId() == 748) {
/*       */               
/*  5734 */               item.setTemplateId(1403);
/*  5735 */               item.setName("blank report");
/*  5736 */               item.sendUpdate();
/*       */             } 
/*       */           }
/*  5739 */           item.setPlacedOnParent(isPlaced);
/*  5740 */           addItem(item, false);
/*  5741 */           setThisAsParentFor(item, true);
/*  5742 */           updatePileMaterial();
/*       */           
/*  5744 */           toReturn = true;
/*       */         }
/*  5746 */         else if (freevol <= item.getWeightGrams() * 10) {
/*  5747 */           logInfo(getName() + " freevol(" + freevol + ")<=" + item.getName() + ".getWeightGrams()*10 (" + item
/*  5748 */               .getWeightGrams() + ")", new Exception());
/*       */         
/*       */         }
/*       */       
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/*  5757 */       Item insertTarget = getInsertItem();
/*  5758 */       if (insertTarget == null) {
/*  5759 */         return false;
/*       */       }
/*  5761 */       return insertTarget.insertItem(item, unconditionally);
/*       */     } 
/*  5763 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean doesContainerRestrictionsAllowItem(Item item) {
/*  5768 */     if (getTemplateId() == 1404 && (
/*  5769 */       item.getTemplateId() == 748 || item.getTemplateId() == 1272 || item.getTemplateId() == 1403)) {
/*  5770 */       return true;
/*       */     }
/*  5772 */     if (getTemplate().getContainerRestrictions() == null) {
/*  5773 */       return false;
/*       */     }
/*  5775 */     for (ContainerRestriction cRest : getTemplate().getContainerRestrictions()) {
/*  5776 */       if (cRest.canInsertItem(getItemsAsArray(), item))
/*  5777 */         return true; 
/*       */     } 
/*  5779 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean itemCanBeInserted(Item item) {
/*  5784 */     if (getTemplate().getContainerRestrictions() != null)
/*  5785 */       return doesContainerRestrictionsAllowItem(item); 
/*  5786 */     if (getTemplateId() == 1409 && 
/*  5787 */       item.getTemplateId() != 748 && item.getTemplateId() != 1272)
/*  5788 */       return false; 
/*  5789 */     if (getTemplateId() == 1404) {
/*       */       
/*  5791 */       if (item.getTemplateId() != 748 && item.getTemplateId() != 1272 && item.getTemplateId() != 1403)
/*  5792 */         return false; 
/*  5793 */       if (item.getTemplateId() != 1403 && (item.getAuxData() > 0 || item.getInscription() != null))
/*  5794 */         return false; 
/*  5795 */       if (getItemCount() >= Math.max(22.0D, Math.floor(getCurrentQualityLevel()))) {
/*  5796 */         return false;
/*       */       }
/*  5798 */       return true;
/*       */     } 
/*       */     
/*  5801 */     if ((item.isSaddleBags() || item.getTemplateId() == 621) && (
/*  5802 */       isSaddleBags() || isInside(new int[] { 1333, 1334 }))) {
/*  5803 */       return false;
/*       */     }
/*  5805 */     if (getContainerSizeX() >= item.getSizeX() && getContainerSizeY() >= item.getSizeY() && getContainerSizeZ() > item.getSizeZ()) {
/*  5806 */       return true;
/*       */     }
/*  5808 */     if (getTemplateId() == 177) {
/*  5809 */       return true;
/*       */     }
/*  5811 */     if (getTemplateId() == 0) {
/*  5812 */       return true;
/*       */     }
/*       */     
/*  5815 */     if (item.isHollow()) {
/*  5816 */       return false;
/*       */     }
/*  5818 */     if ((item.isCombine() || item.isFood() || item.isLiquid()) && getFreeVolume() >= item.getVolume()) {
/*  5819 */       return true;
/*       */     }
/*  5821 */     return false;
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
/*       */   private boolean isMultipleMaterialPileTemplate(int templateId) {
/*  5833 */     if (templateId == 9)
/*  5834 */       return true; 
/*  5835 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void updatePileMaterial() {
/*  5845 */     if (getTemplateId() == 177) {
/*       */       
/*  5847 */       boolean multipleMaterials = false;
/*  5848 */       byte currentMaterial = 0;
/*  5849 */       Item[] itemsArray = getItemsAsArray();
/*  5850 */       byte firstMaterial = 0;
/*  5851 */       int firstItem = 0;
/*  5852 */       int currentItem = 0;
/*       */       
/*  5854 */       for (int i = 0; i < itemsArray.length; i++) {
/*       */ 
/*       */         
/*  5857 */         currentMaterial = itemsArray[i].getMaterial();
/*       */ 
/*       */         
/*  5860 */         currentItem = itemsArray[i].getTemplateId();
/*       */ 
/*       */         
/*  5863 */         if (i == 0) {
/*       */ 
/*       */           
/*  5866 */           firstMaterial = currentMaterial;
/*  5867 */           firstItem = currentItem;
/*       */         } 
/*       */ 
/*       */         
/*  5871 */         if (currentItem != firstItem) {
/*       */ 
/*       */           
/*  5874 */           multipleMaterials = true;
/*       */           
/*       */           break;
/*       */         } 
/*       */         
/*  5879 */         if (currentMaterial != firstMaterial && !isMultipleMaterialPileTemplate(currentItem)) {
/*       */ 
/*       */           
/*  5882 */           multipleMaterials = true;
/*       */           
/*       */           break;
/*       */         } 
/*       */       } 
/*       */       
/*  5888 */       if (multipleMaterials) {
/*       */         
/*  5890 */         boolean changed = false;
/*       */ 
/*       */         
/*  5893 */         if (getData1() != -1) {
/*       */           
/*  5895 */           setData1(-1);
/*  5896 */           changed = true;
/*       */         } 
/*       */ 
/*       */         
/*  5900 */         if (getMaterial() != 0) {
/*       */           
/*  5902 */           setMaterial((byte)0);
/*  5903 */           changed = true;
/*       */         } 
/*       */ 
/*       */         
/*  5907 */         if (changed)
/*       */         {
/*  5909 */           updateModelNameOnGroundItem();
/*       */         }
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void sendHold(Item item) {
/*  5918 */     String holdst = "hold";
/*  5919 */     Creature owner = getOwnerOrNull();
/*  5920 */     if (owner == null)
/*       */       return; 
/*  5922 */     String hand = "right hand";
/*  5923 */     if (this.place == 37)
/*  5924 */       hand = "left hand"; 
/*  5925 */     if (item.isTwoHanded()) {
/*  5926 */       hand = "two hands";
/*       */     }
/*  5928 */     if (!owner.getCommunicator().stillLoggingIn()) {
/*       */       
/*  5930 */       owner.getCommunicator().sendNormalServerMessage("You hold " + item
/*  5931 */           .getNameWithGenus().toLowerCase() + " in your " + hand + ".");
/*  5932 */       PlayerTutorial.firePlayerTrigger(owner.getWurmId(), PlayerTutorial.PlayerTrigger.EQUIPPED_ITEM);
/*       */     } 
/*  5934 */     boolean send = true;
/*       */     
/*  5936 */     if (item.isArmour()) {
/*       */       
/*  5938 */       byte[] armourplaces = item.getBodySpaces();
/*  5939 */       for (int x = 0; x < armourplaces.length; x++) {
/*       */         
/*  5941 */         if (armourplaces[x] == this.place)
/*  5942 */           send = false; 
/*       */       } 
/*       */     } 
/*  5945 */     if (item.isWeapon() && item.getSpellEffects() != null)
/*  5946 */       owner.achievement(581); 
/*  5947 */     if (send) {
/*       */       
/*  5949 */       owner.getCurrentTile().sendWieldItem(getOwnerId(), (this.place == 37) ? 0 : 1, item
/*       */           
/*  5951 */           .getModelName(), item.getRarity(), WurmColor.getColorRed(item.getColor()), 
/*  5952 */           WurmColor.getColorGreen(item.getColor()), WurmColor.getColorBlue(item.getColor()), 
/*  5953 */           WurmColor.getColorRed(item.getColor2()), 
/*  5954 */           WurmColor.getColorGreen(item.getColor2()), 
/*  5955 */           WurmColor.getColorBlue(item.getColor2()));
/*       */       
/*  5957 */       byte equipementSlot = BodyTemplate.convertToItemEquipementSlot((byte)this.place);
/*  5958 */       owner.getCurrentTile().sendWearItem(getOwnerId(), item.getTemplateId(), equipementSlot, 
/*  5959 */           WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), WurmColor.getColorBlue(item.getColor()), 
/*  5960 */           WurmColor.getColorRed(item.getColor2()), 
/*  5961 */           WurmColor.getColorGreen(item.getColor2()), 
/*  5962 */           WurmColor.getColorBlue(item.getColor2()), item
/*  5963 */           .getMaterial(), item.getRarity());
/*  5964 */       owner.getCombatHandler().setCurrentStance(-1, (byte)0);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private void sendWear(Item item, byte bodyPart) {
/*  5970 */     if (!item.isBodyPartAttached()) {
/*       */       
/*  5972 */       Creature owner = getOwnerOrNull();
/*  5973 */       if (owner == null)
/*       */         return; 
/*  5975 */       if (!owner.getCommunicator().stillLoggingIn()) {
/*       */         
/*  5977 */         owner.getCommunicator().sendNormalServerMessage("You wear " + item.getNameWithGenus().toLowerCase() + ".");
/*  5978 */         PlayerTutorial.firePlayerTrigger(owner.getWurmId(), PlayerTutorial.PlayerTrigger.EQUIPPED_ITEM);
/*       */       } 
/*       */ 
/*       */       
/*  5982 */       byte equipmentSlot = item.isArmour() ? BodyTemplate.convertToArmorEquipementSlot(bodyPart) : BodyTemplate.convertToItemEquipementSlot(bodyPart);
/*       */       
/*  5984 */       if (owner.isAnimal() && owner.isVehicle()) {
/*       */         
/*  5986 */         owner.getCurrentTile().sendHorseWear(getOwnerId(), item.getTemplateId(), item.getMaterial(), equipmentSlot, item.getAuxData());
/*       */       }
/*       */       else {
/*       */         
/*  5990 */         owner.getCurrentTile().sendWearItem(getOwnerId(), item.getTemplateId(), equipmentSlot, 
/*  5991 */             WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), WurmColor.getColorBlue(item.getColor()), 
/*  5992 */             WurmColor.getColorRed(item.getColor2()), 
/*  5993 */             WurmColor.getColorGreen(item.getColor2()), 
/*  5994 */             WurmColor.getColorBlue(item.getColor2()), item
/*  5995 */             .getMaterial(), item.getRarity());
/*       */       } 
/*       */ 
/*       */       
/*  5999 */       if (item.isArmour())
/*       */       {
/*  6001 */         item.setWornAsArmour(true, getOwnerId());
/*       */       }
/*       */       
/*  6004 */       if (item.getTemplateId() == 330)
/*       */       {
/*  6006 */         owner.setHasCrownEffect(true);
/*       */       }
/*       */       
/*  6009 */       if (item.hasItemBonus() && !Servers.localServer.PVPSERVER)
/*       */       {
/*  6011 */         ItemBonus.calcAndAddBonus(item, owner);
/*       */       }
/*       */       
/*  6014 */       if (item.isPriceEffectedByMaterial())
/*       */       {
/*  6016 */         if (item.getTemplateId() == 297) {
/*  6017 */           owner.achievement(94);
/*  6018 */         } else if (item.getTemplateId() == 230) {
/*  6019 */           owner.achievement(95);
/*  6020 */         } else if (item.getTemplateId() == 231) {
/*  6021 */           owner.achievement(96);
/*       */         }  } 
/*  6023 */       if (item.isWeapon() && item.getSpellEffects() != null) {
/*  6024 */         owner.achievement(581);
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   private void sendWearShield(Item item) {
/*  6030 */     if (!item.isBodyPartAttached()) {
/*       */       
/*  6032 */       Creature owner = getOwnerOrNull();
/*  6033 */       if (owner == null)
/*       */         return; 
/*  6035 */       if (!owner.getCommunicator().stillLoggingIn()) {
/*       */         
/*  6037 */         owner.getCommunicator().sendNormalServerMessage("You wear " + item
/*  6038 */             .getNameWithGenus().toLowerCase() + " as shield.");
/*  6039 */         PlayerTutorial.firePlayerTrigger(owner.getWurmId(), PlayerTutorial.PlayerTrigger.EQUIPPED_ITEM);
/*       */       } 
/*  6041 */       byte equipementSlot = BodyTemplate.convertToItemEquipementSlot((byte)this.place);
/*  6042 */       owner.getCurrentTile().sendWearItem(getOwnerId(), item.getTemplateId(), equipementSlot, 
/*  6043 */           WurmColor.getColorRed(item.getColor()), WurmColor.getColorGreen(item.getColor()), 
/*  6044 */           WurmColor.getColorBlue(item.getColor()), 
/*  6045 */           WurmColor.getColorRed(item.getColor2()), 
/*  6046 */           WurmColor.getColorGreen(item.getColor2()), 
/*  6047 */           WurmColor.getColorBlue(item.getColor2()), item
/*  6048 */           .getMaterial(), item.getRarity());
/*       */       
/*  6050 */       owner.getCommunicator().sendToggleShield(true);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEmpty(boolean checkInitialContainers) {
/*  6056 */     if (checkInitialContainers && getTemplate().getInitialContainers() != null) {
/*       */ 
/*       */       
/*  6059 */       for (Item item : getItemsAsArray()) {
/*       */         
/*  6061 */         if (!item.isEmpty(false))
/*  6062 */           return false; 
/*       */       } 
/*  6064 */       return true;
/*       */     } 
/*  6066 */     if (this.items == null || this.items.isEmpty()) {
/*  6067 */       return true;
/*       */     }
/*  6069 */     for (Item item : getItemsAsArray()) {
/*       */       
/*  6071 */       if (!item.isTemporary())
/*  6072 */         return false; 
/*       */     } 
/*  6074 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void addCreationWindowWatcher(Player creature) {
/*  6079 */     if (this.watchers == null)
/*  6080 */       this.watchers = new HashSet<>(); 
/*  6081 */     if (!this.watchers.contains(creature))
/*       */     {
/*  6083 */       this.watchers.add(creature);
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void addWatcher(long inventoryWindow, Creature creature) {
/*  6091 */     if (this.watchers == null)
/*  6092 */       this.watchers = new HashSet<>(); 
/*  6093 */     if (!this.watchers.contains(creature))
/*       */     {
/*  6095 */       this.watchers.add(creature);
/*       */     }
/*  6097 */     if (inventoryWindow >= 1L && inventoryWindow <= 4L) {
/*       */       
/*  6099 */       if (this.tradeWindow != null && this.tradeWindow.getWurmId() == inventoryWindow)
/*       */       {
/*  6101 */         if (this.parentId != -10L) {
/*       */           
/*       */           try
/*       */           {
/*  6105 */             if (Items.getItem(this.parentId).isViewableBy(creature))
/*       */             {
/*       */               
/*  6108 */               creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*       */             }
/*  6110 */             if (isBodyPart())
/*       */             {
/*  6112 */               sendContainedItems(inventoryWindow, creature);
/*       */             }
/*       */           }
/*  6115 */           catch (NoSuchItemException nsi)
/*       */           {
/*  6117 */             logWarn(this.id + " has parent " + this.parentId + " but " + nsi.getMessage(), (Throwable)nsi);
/*       */           }
/*       */         
/*  6120 */         } else if (getTemplateId() == 0 || isBodyPart()) {
/*       */           
/*  6122 */           creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*  6123 */           sendContainedItems(inventoryWindow, creature);
/*       */         
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*  6129 */     else if (this.parentId != -10L) {
/*       */       
/*       */       try
/*       */       {
/*  6133 */         if (Items.getItem(this.parentId).isViewableBy(creature)) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  6141 */           if (isInside(new int[] { 1333, 1334 })) {
/*       */             
/*  6143 */             Item parentBags = getFirstParent(new int[] { 1333, 1334 });
/*  6144 */             creature.getCommunicator().sendAddToInventory(this, parentBags.getWurmId(), parentBags.getWurmId(), -1);
/*       */           } 
/*  6146 */           Item parentWindow = recursiveParentCheck();
/*  6147 */           if (parentWindow != null && parentWindow != this)
/*       */           {
/*  6149 */             creature.getCommunicator().sendAddToInventory(this, parentWindow.getWurmId(), parentWindow.getWurmId(), -1);
/*       */           }
/*  6151 */           creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*       */         } 
/*  6153 */         if (isBodyPart())
/*       */         {
/*  6155 */           sendContainedItems(inventoryWindow, creature);
/*       */         }
/*       */       }
/*  6158 */       catch (NoSuchItemException nsi)
/*       */       {
/*  6160 */         logWarn(this.id + " has parent " + this.parentId + " but " + nsi.getMessage(), (Throwable)nsi);
/*       */       }
/*       */     
/*  6163 */     } else if (getTemplateId() == 0) {
/*       */ 
/*       */       
/*  6166 */       creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*       */     }
/*  6168 */     else if (isBodyPart()) {
/*       */ 
/*       */       
/*  6171 */       creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*  6172 */       sendContainedItems(inventoryWindow, creature);
/*       */     }
/*  6174 */     else if (isHollow()) {
/*       */ 
/*       */ 
/*       */       
/*  6178 */       if (isBanked())
/*       */       {
/*       */         
/*  6181 */         creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*       */       }
/*  6183 */       if (this.watchers.size() == 1)
/*       */       {
/*  6185 */         VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  6186 */         if (t != null)
/*       */         {
/*  6188 */           if (getTopParent() == getWurmId())
/*       */           {
/*  6190 */             t.sendAnimation(creature, this, "open", false, false);
/*       */           }
/*       */         }
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/*  6197 */       creature.getCommunicator().sendAddToInventory(this, inventoryWindow, inventoryWindow, -1);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendContainedItems(long inventoryWindow, Creature creature) {
/*  6204 */     if (!isHollow()) {
/*       */       return;
/*       */     }
/*  6207 */     if (!isViewableBy(creature)) {
/*       */       return;
/*       */     }
/*       */     
/*  6211 */     int sentCount = 0;
/*  6212 */     for (Item item : getItems()) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*  6219 */       if (sentCount >= 1000) {
/*       */         break;
/*       */       }
/*  6222 */       if (!isCrate() || !item.isBulkItem()) {
/*       */         
/*  6224 */         item.addWatcher(inventoryWindow, creature);
/*  6225 */         sentCount++;
/*       */         
/*       */         continue;
/*       */       } 
/*  6229 */       int storageSpace = (this.template.templateId == 852) ? 300 : 150;
/*       */       
/*  6231 */       Item[] cargo = getItemsAsArray();
/*  6232 */       for (Item cargoItem : cargo) {
/*       */         
/*  6234 */         int count = cargoItem.getBulkNums();
/*  6235 */         if (count > storageSpace) {
/*       */ 
/*       */ 
/*       */           
/*  6239 */           ItemTemplate itemp = cargoItem.getRealTemplate();
/*  6240 */           if (itemp != null)
/*       */           
/*       */           { 
/*       */             
/*  6244 */             String cargoName = cargoItem.getName();
/*  6245 */             int newSize = itemp.getVolume() * storageSpace;
/*  6246 */             int oldSize = cargoItem.getVolume();
/*       */ 
/*       */ 
/*       */             
/*  6250 */             String toSend = "Trimming size of " + cargoName + " to " + newSize + " instead of " + oldSize + " at " + getTileX() + "," + getTileY();
/*  6251 */             logInfo(toSend);
/*  6252 */             Message mess = new Message(null, (byte)11, "GM", "<System> " + toSend);
/*       */ 
/*       */             
/*  6255 */             Server.getInstance().addMessage(mess);
/*  6256 */             Players.addGmMessage("System", "Trimming crate size of " + cargoName + " to " + newSize + " instead of " + oldSize);
/*       */ 
/*       */             
/*  6259 */             cargoItem.setWeight(newSize, true); } 
/*       */         } 
/*  6261 */       }  item.addWatcher(inventoryWindow, creature);
/*  6262 */       sentCount++;
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*  6267 */     if (getItemCount() > 0) {
/*  6268 */       creature.getCommunicator().sendIsEmpty(inventoryWindow, getWurmId());
/*       */     }
/*       */   }
/*       */   
/*       */   public final void removeWatcher(Creature creature, boolean send) {
/*  6273 */     removeWatcher(creature, send, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void removeWatcher(Creature creature, boolean send, boolean recursive) {
/*  6278 */     if (this.watchers != null)
/*       */     {
/*  6280 */       if (this.watchers.contains(creature)) {
/*       */         
/*  6282 */         this.watchers.remove(creature);
/*  6283 */         if (this.parentId != -10L && send)
/*       */         {
/*  6285 */           creature.getCommunicator().sendRemoveFromInventory(this);
/*       */         }
/*  6287 */         if (isHollow()) {
/*       */ 
/*       */           
/*  6290 */           if (this.items != null)
/*       */           {
/*  6292 */             for (Item item : this.items)
/*       */             {
/*  6294 */               item.removeWatcher(creature, false, true);
/*       */             }
/*       */           }
/*       */ 
/*       */ 
/*       */           
/*  6300 */           if (this.watchers.isEmpty() && !recursive) {
/*       */             
/*  6302 */             VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  6303 */             if (t != null)
/*       */             {
/*  6305 */               if (getTopParent() == getWurmId() || isPlacedOnParent())
/*       */               {
/*  6307 */                 t.sendAnimation(creature, this, "close", false, false);
/*       */               }
/*       */             }
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
/*       */   public final Set<Creature> getWatcherSet() {
/*  6323 */     return this.watchers;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nonnull
/*       */   public final Creature[] getWatchers() throws NoSuchCreatureException {
/*  6334 */     if (this.watchers != null) {
/*  6335 */       return this.watchers.<Creature>toArray(new Creature[this.watchers.size()]);
/*       */     }
/*  6337 */     throw new NoSuchCreatureException("Not watched");
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isViewableBy(Creature creature) {
/*  6342 */     if (this.parentId == this.id) {
/*       */       
/*  6344 */       logWarn("This shouldn't happen!");
/*  6345 */       return true;
/*       */     } 
/*       */     
/*  6348 */     if (isLockable() && getLockId() != -10L) {
/*       */       
/*       */       try {
/*       */         
/*  6352 */         Item lock = Items.getItem(this.lockid);
/*  6353 */         if (creature.hasKeyForLock(lock) || (
/*  6354 */           isDraggable() && MethodsItems.mayUseInventoryOfVehicle(creature, this)) || (
/*  6355 */           getTemplateId() == 850 && MethodsItems.mayUseInventoryOfVehicle(creature, this)) || (
/*  6356 */           isLocked() && mayAccessHold(creature))) {
/*       */           
/*  6358 */           if (this.parentId != -10L) {
/*  6359 */             return getParent().isViewableBy(creature);
/*       */           }
/*  6361 */           return true;
/*       */         } 
/*       */ 
/*       */         
/*  6365 */         return false;
/*       */       
/*       */       }
/*  6368 */       catch (NoSuchItemException nsi) {
/*       */         
/*  6370 */         logWarn(this.id + " is locked but lock " + this.lockid + " can not be found.", (Throwable)nsi);
/*       */         
/*       */         try {
/*  6373 */           if (this.parentId != -10L) {
/*  6374 */             return getParent().isViewableBy(creature);
/*       */           }
/*  6376 */           return true;
/*       */         }
/*  6378 */         catch (NoSuchItemException nsa) {
/*       */           
/*  6380 */           logWarn(this.id + " has parent " + this.parentId + " but " + nsa.getMessage(), (Throwable)nsa);
/*       */         } 
/*       */       } 
/*       */     }
/*  6384 */     if (this.parentId == -10L) {
/*  6385 */       return true;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/*  6390 */       return getParent().isViewableBy(creature);
/*       */     }
/*  6392 */     catch (NoSuchItemException nsi) {
/*       */       
/*  6394 */       logWarn(this.id + " has parent " + this.parentId + " but " + nsi.getMessage(), (Throwable)nsi);
/*  6395 */       return true;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final Item getParent() throws NoSuchItemException {
/*  6402 */     if (this.parentId != -10L)
/*  6403 */       return Items.getItem(this.parentId); 
/*  6404 */     throw new NoSuchItemException("No parent.");
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final long getLastOwner() {
/*  6414 */     return this.lastOwner;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final long getLastParentId() {
/*  6424 */     return this.lastParentId;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void setThisAsParentFor(Item item, boolean forceUpdateParent) {
/*  6433 */     if (item.getDbStrings() instanceof FrozenItemDbStrings) {
/*       */       
/*  6435 */       item.returnFromFreezer();
/*  6436 */       item.deleteInDatabase();
/*  6437 */       item.setDbStrings(ItemDbStrings.getInstance());
/*  6438 */       logInfo("Returning from frozen: " + item.getName() + " " + item.getWurmId(), new Exception());
/*       */     } 
/*  6440 */     if (item.getWatcherSet() != null) {
/*       */       
/*       */       try {
/*       */         
/*  6444 */         for (Creature watcher : item.getWatchers())
/*       */         {
/*  6446 */           if (this.watchers != null)
/*       */           {
/*  6448 */             if (!this.watchers.contains(watcher))
/*       */             {
/*  6450 */               item.removeWatcher(watcher, true);
/*       */             }
/*       */           }
/*       */         }
/*       */       
/*  6455 */       } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  6461 */     if (forceUpdateParent) {
/*       */       
/*       */       try {
/*       */ 
/*       */         
/*  6466 */         Item oldParent = item.getParent();
/*  6467 */         if (!oldParent.hasSameOwner(item))
/*       */         {
/*  6469 */           oldParent.dropItem(item.getWurmId(), false);
/*       */         }
/*  6471 */         else if (this != oldParent)
/*       */         {
/*  6473 */           oldParent.removeItem(item.getWurmId(), false, false, false);
/*       */         }
/*       */       
/*  6476 */       } catch (NoSuchItemException noSuchItemException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  6482 */     if (getTemplateId() == 621 && item.isSaddleBags()) {
/*       */       
/*       */       try {
/*       */         
/*  6486 */         Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  6487 */         if (owner.isAnimal() && owner.isVehicle())
/*       */         {
/*  6489 */           byte equipmentSlot = BodyTemplate.convertToItemEquipementSlot((byte)(getParent()).place);
/*  6490 */           owner.getCurrentTile().sendHorseWear(this.ownerId, getTemplateId(), getMaterial(), equipmentSlot, getAuxData());
/*       */         }
/*       */       
/*  6493 */       } catch (NoSuchPlayerException|NoSuchCreatureException|NoSuchItemException noSuchPlayerException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  6498 */     item.setParentId(this.id, isOnSurface());
/*  6499 */     item.lastParentId = this.id;
/*  6500 */     long itemOwnerId = -10L;
/*  6501 */     long lOwnerId = getOwnerId();
/*       */     
/*       */     try {
/*  6504 */       itemOwnerId = item.getOwner();
/*  6505 */       if (itemOwnerId != lOwnerId) {
/*  6506 */         item.setOwner(lOwnerId, true);
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*  6511 */       else if (this.watchers != null) {
/*       */         
/*  6513 */         for (Creature watcher : this.watchers)
/*       */         {
/*  6515 */           if (item.watchers == null || !item.watchers.contains(watcher))
/*       */           {
/*  6517 */             item.addWatcher(getTopParent(), watcher);
/*       */           
/*       */           }
/*       */         }
/*       */       
/*       */       }
/*       */     
/*       */     }
/*  6525 */     catch (NotOwnedException ex) {
/*       */ 
/*       */       
/*       */       try {
/*       */         
/*  6530 */         item.setOwner(lOwnerId, true);
/*       */         
/*  6532 */         if (lOwnerId == -10L) {
/*       */           
/*  6534 */           if (this.watchers != null)
/*       */           {
/*  6536 */             for (Iterator<Creature> it = this.watchers.iterator(); it.hasNext(); ) {
/*       */               
/*  6538 */               Creature watcher = it.next();
/*  6539 */               long inventoryWindow = item.getTopParent();
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  6546 */               item.addWatcher(inventoryWindow, watcher);
/*       */             } 
/*       */           }
/*  6549 */           if (isFire()) {
/*       */             
/*  6551 */             if (item.isFoodMaker() || item.isFood())
/*       */             {
/*  6553 */               VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  6554 */               if (t != null)
/*       */               {
/*  6556 */                 t.renameItem(this);
/*       */               }
/*       */             }
/*       */           
/*  6560 */           } else if (isWeaponContainer() || isBarrelRack()) {
/*       */             
/*  6562 */             VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  6563 */             if (t != null)
/*       */             {
/*  6565 */               t.renameItem(this);
/*       */             }
/*       */           }
/*       */         
/*       */         } 
/*  6570 */       } catch (Exception ex2) {
/*       */         
/*  6572 */         logWarn("Failed to set ownerId to " + lOwnerId + " for item " + item.getWurmId(), ex2);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setOwner(long newOwnerId, boolean startWatching) {
/*  6581 */     setOwner(newOwnerId, -10L, startWatching);
/*       */   }
/*       */   
/*       */   public final void setOwner(long newOwnerId, long newParent, boolean startWatching) {
/*  6585 */     long oldOwnerId = getOwnerId();
/*  6586 */     if (isCoin() && getValue() >= 1000000) {
/*  6587 */       logInfo("COINLOG " + newOwnerId + ", " + getWurmId() + " banked " + this.banked + " mailed=" + this.mailed, new Exception());
/*       */     }
/*       */     
/*  6590 */     if (newOwnerId != -10L) {
/*       */       
/*  6592 */       if (oldOwnerId == -10L || oldOwnerId != newOwnerId)
/*       */       {
/*  6594 */         if (oldOwnerId == -10L) {
/*       */           
/*  6596 */           int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / this.template.getDecayTime());
/*  6597 */           if (timesSinceLastUsed > 0)
/*       */           {
/*  6599 */             setLastMaintained(WurmCalendar.currentTime);
/*       */           }
/*       */         } 
/*  6602 */         setZoneId(-10, this.surfaced);
/*  6603 */         setOwnerId(newOwnerId);
/*  6604 */         this.watchers = null;
/*  6605 */         Creature owner = null;
/*       */         
/*       */         try {
/*  6608 */           owner = Server.getInstance().getCreature(newOwnerId);
/*  6609 */           if (Constants.useItemTransferLog) {
/*       */ 
/*       */             
/*  6612 */             ItemTransfer transfer = new ItemTransfer(this.id, this.name, oldOwnerId, String.valueOf(oldOwnerId), newOwnerId, owner.getName(), System.currentTimeMillis());
/*  6613 */             itemLogger.addToQueue((WurmLoggable)transfer);
/*       */           } 
/*       */           
/*  6616 */           if (isCoin()) {
/*       */             
/*  6618 */             Server.getInstance().transaction(this.id, oldOwnerId, newOwnerId, owner.getName(), getValue());
/*  6619 */             owner.addCarriedWeight(getWeightGrams());
/*       */           }
/*  6621 */           else if (isBodyPart()) {
/*       */             
/*  6623 */             if (isBodyPartRemoved()) {
/*  6624 */               owner.addCarriedWeight(getWeightGrams());
/*       */             }
/*       */           } else {
/*  6627 */             owner.addCarriedWeight(getWeightGrams());
/*  6628 */           }  if (startWatching) {
/*       */             
/*       */             try {
/*       */               
/*  6632 */               Creature[] watcherArr = getParent().getWatchers();
/*  6633 */               long tp = getTopParent();
/*  6634 */               for (Creature c : watcherArr) {
/*       */                 
/*  6636 */                 if (c == owner) {
/*  6637 */                   addWatcher(-1L, owner);
/*       */                 } else {
/*  6639 */                   addWatcher(tp, c);
/*       */                 } 
/*       */               } 
/*  6642 */             } catch (NoSuchItemException nsi) {
/*       */               
/*  6644 */               addWatcher(-1L, owner);
/*       */             } 
/*       */           }
/*  6647 */           if (isArtifact() || (isUnique() && !isRoyal())) {
/*       */             
/*  6649 */             owner.getCommunicator().sendNormalServerMessage("You will drop the " + 
/*  6650 */                 getName() + " when you leave the world.");
/*  6651 */             if (getTemplateId() == 329)
/*       */             {
/*  6653 */               owner.getCombatHandler().setRodEffect(true);
/*       */             }
/*  6655 */             if (getTemplateId() == 335)
/*       */             {
/*  6657 */               owner.setHasFingerEffect(true);
/*       */             }
/*  6659 */             if (getTemplateId() == 331 || getTemplateId() == 330)
/*       */             {
/*  6661 */               owner.getCommunicator().sendAlertServerMessage("Also, when you drop the " + 
/*  6662 */                   getName() + ", any aggressive pet you have will become enraged and lose its loyalty.");
/*       */             }
/*       */             
/*  6665 */             if (getTemplateId() == 338)
/*       */             {
/*  6667 */               owner.getCommunicator().sendAlertServerMessage("Also, when you drop the " + 
/*  6668 */                   getName() + ", any pet you have will become enraged and lose its loyalty.");
/*       */             }
/*       */ 
/*       */             
/*  6672 */             if (isArtifact() && getAuxData() > 30) {
/*       */               
/*  6674 */               if (Servers.isThisATestServer() && Servers.isThisAPvpServer() && owner
/*  6675 */                 .getPower() >= 2)
/*       */               {
/*  6677 */                 owner.getCommunicator().sendNormalServerMessage("Old power = " + getAuxData() + ".");
/*       */               }
/*  6679 */               setAuxData((byte)30);
/*  6680 */               if (Servers.isThisATestServer() && Servers.isThisAPvpServer() && owner
/*  6681 */                 .getPower() >= 2)
/*       */               {
/*  6683 */                 owner.getCommunicator().sendNormalServerMessage("New power = " + getAuxData() + ".");
/*       */               }
/*       */             } 
/*       */           } 
/*       */           
/*  6688 */           if (isKey())
/*       */           {
/*  6690 */             owner.addKey(this, false);
/*       */           }
/*       */         }
/*  6693 */         catch (NoSuchPlayerException noSuchPlayerException) {
/*       */ 
/*       */         
/*       */         }
/*  6697 */         catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*  6702 */       else if (this.zoneId != -10L)
/*       */       {
/*  6704 */         logWarn(getName() + " new owner " + newOwnerId + " zone id was " + this.zoneId, new Exception());
/*  6705 */         setZoneId(-10, true);
/*       */       }
/*       */     
/*       */     }
/*  6709 */     else if (oldOwnerId != -10L) {
/*       */       
/*  6711 */       Creature creature = null;
/*       */       
/*       */       try {
/*  6714 */         setZoneId(-10, true);
/*  6715 */         creature = Server.getInstance().getCreature(oldOwnerId);
/*       */         
/*  6717 */         if (Constants.useItemTransferLog && !isBodyPartAttached() && !isInventory()) {
/*       */ 
/*       */           
/*  6720 */           ItemTransfer transfer = new ItemTransfer(this.id, this.name, oldOwnerId, creature.getName(), newOwnerId, "" + this.ownerId, System.currentTimeMillis());
/*  6721 */           itemLogger.addToQueue((WurmLoggable)transfer);
/*       */         } 
/*       */ 
/*       */         
/*  6725 */         if (!isLocked()) {
/*  6726 */           setLastOwnerId(oldOwnerId);
/*       */         }
/*       */         
/*  6729 */         if (isCoin()) {
/*       */           
/*  6731 */           if (getParentId() != -10L) {
/*       */             
/*  6733 */             Server.getInstance().transaction(this.id, oldOwnerId, newOwnerId, creature.getName(), getValue());
/*  6734 */             if (!creature.removeCarriedWeight(getWeightGrams())) {
/*  6735 */               logWarn(getName() + " removed " + getWeightGrams(), new Exception());
/*       */             }
/*       */           } 
/*  6738 */         } else if (isBodyPart()) {
/*       */           
/*  6740 */           if (isBodyPartRemoved())
/*       */           {
/*  6742 */             if (!creature.removeCarriedWeight(getWeightGrams())) {
/*  6743 */               logWarn(getName() + " removed " + getWeightGrams(), new Exception());
/*       */             
/*       */             }
/*       */           }
/*       */         }
/*  6748 */         else if (!creature.removeCarriedWeight(getWeightGrams())) {
/*  6749 */           logWarn(getName() + " removed " + getWeightGrams(), new Exception());
/*       */         } 
/*  6751 */         if (isArmour())
/*       */         {
/*  6753 */           setWornAsArmour(false, oldOwnerId);
/*       */         }
/*  6755 */         if (isArtifact()) {
/*       */           
/*  6757 */           if (getTemplateId() == 329)
/*       */           {
/*  6759 */             creature.getCombatHandler().setRodEffect(false);
/*       */           }
/*  6761 */           if (getTemplateId() == 335)
/*       */           {
/*  6763 */             creature.setHasFingerEffect(false);
/*       */           }
/*  6765 */           if (getTemplateId() == 330)
/*       */           {
/*  6767 */             creature.setHasCrownEffect(false);
/*       */           }
/*  6769 */           if (getTemplateId() == 331 || getTemplateId() == 330 || 
/*  6770 */             getTemplateId() == 338) {
/*       */             
/*  6772 */             boolean untame = false;
/*  6773 */             if (newParent != -10L) {
/*       */ 
/*       */               
/*  6776 */               Item newParentItem = Items.getItem(newParent);
/*  6777 */               if (newParentItem.getOwnerId() != oldOwnerId) {
/*  6778 */                 untame = true;
/*       */               }
/*       */             } else {
/*       */               
/*  6782 */               untame = true;
/*  6783 */             }  if (creature.getPet() != null && untame) {
/*       */               
/*  6785 */               Creature pet = creature.getPet();
/*  6786 */               creature.getCommunicator().sendAlertServerMessage("As you drop the " + 
/*  6787 */                   getName() + ", you feel rage and confusion from the " + pet
/*  6788 */                   .getName() + ".");
/*  6789 */               creature.setPet(-10L);
/*  6790 */               pet.setLoyalty(0.0F);
/*  6791 */               pet.setDominator(-10L);
/*       */             } 
/*       */           } 
/*       */         } 
/*  6795 */         removeWatcher(creature, true);
/*  6796 */         if (isKey())
/*  6797 */           creature.removeKey(this, false); 
/*  6798 */         if (isLeadCreature())
/*       */         {
/*  6800 */           if (creature.isItemLeading(this))
/*       */           {
/*  6802 */             creature.dropLeadingItem(this);
/*       */           }
/*       */         }
/*  6805 */         if (!isFood() && !isAlwaysPoll())
/*       */         {
/*  6807 */           long decayt = this.template.getDecayTime();
/*       */           
/*  6809 */           int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / decayt);
/*  6810 */           if (timesSinceLastUsed > 0)
/*       */           {
/*  6812 */             setLastMaintained(WurmCalendar.currentTime);
/*       */           }
/*       */         }
/*       */       
/*  6816 */       } catch (NoSuchPlayerException nsp) {
/*       */         
/*  6818 */         logWarn("Removing object from unknown player: ", (Throwable)nsp);
/*       */       }
/*  6820 */       catch (NoSuchCreatureException cnf) {
/*       */         
/*  6822 */         logWarn("Removing object from unknown creature: ", (Throwable)cnf);
/*       */       }
/*  6824 */       catch (Exception ex) {
/*       */         
/*  6826 */         logWarn("Failed to save creature when dropping item with id " + this.id, ex);
/*       */       } 
/*  6828 */       setOwnerId(-10L);
/*       */     }
/*       */     else {
/*       */       
/*  6832 */       setOwnerId(-10L);
/*       */     } 
/*       */ 
/*       */     
/*  6836 */     if (isHollow())
/*       */     {
/*  6838 */       if (this.items != null)
/*       */       {
/*  6840 */         for (Item item : this.items) {
/*       */ 
/*       */           
/*  6843 */           if (isSealedByPlayer() && item.getTemplateId() == 169) {
/*       */             continue;
/*       */           }
/*       */           
/*  6847 */           if (item != this) {
/*  6848 */             item.setOwner(newOwnerId, false); continue;
/*       */           } 
/*  6850 */           logWarn("Item with id " + this.id + " has itself in the inventory!");
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
/*       */   public final Item dropItem(long aId, boolean setPosition) throws NoSuchItemException {
/*  6863 */     return dropItem(aId, -10L, setPosition, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item dropItem(long aId, long newParent, boolean setPosition) throws NoSuchItemException {
/*  6868 */     return dropItem(aId, newParent, setPosition, false);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item dropItem(long aId, boolean setPosition, boolean skipPileRemoval) throws NoSuchItemException {
/*  6873 */     return dropItem(aId, -10L, setPosition, skipPileRemoval);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final Item dropItem(long aId, long newParent, boolean setPosition, boolean skipPileRemoval) throws NoSuchItemException {
/*  6879 */     Item toReturn = removeItem(aId, setPosition, true, skipPileRemoval);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  6885 */     toReturn.setOwner(-10L, newParent, false);
/*  6886 */     toReturn.setParentId(-10L, this.surfaced);
/*  6887 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public static int[] getDropTile(Creature creature) throws NoSuchZoneException {
/*  6892 */     float lPosX = creature.getStatus().getPositionX();
/*  6893 */     float lPosY = creature.getStatus().getPositionY();
/*  6894 */     if (creature.getBridgeId() != -10L) {
/*       */ 
/*       */       
/*  6897 */       int i = CoordUtils.WorldToTile(lPosX);
/*  6898 */       int j = CoordUtils.WorldToTile(lPosY);
/*  6899 */       return new int[] { i, j };
/*       */     } 
/*       */     
/*  6902 */     float rot = creature.getStatus().getRotation();
/*  6903 */     float xPosMod = (float)Math.sin((rot * 0.017453292F)) * (1.0F + Server.rand.nextFloat());
/*  6904 */     float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * (1.0F + Server.rand.nextFloat());
/*  6905 */     float newPosX = lPosX + xPosMod;
/*  6906 */     float newPosY = lPosY + yPosMod;
/*  6907 */     BlockingResult result = Blocking.getBlockerBetween(creature, lPosX, lPosY, newPosX, newPosY, creature
/*  6908 */         .getPositionZ(), creature.getPositionZ(), creature.isOnSurface(), creature.isOnSurface(), false, 4, -1L, creature
/*  6909 */         .getBridgeId(), creature.getBridgeId(), false);
/*  6910 */     if (result != null) {
/*       */       
/*  6912 */       newPosX = lPosX + (float)Math.sin((rot * 0.017453292F)) * (-1.0F + Server.rand.nextFloat());
/*  6913 */       newPosY = lPosY - (float)Math.cos((rot * 0.017453292F)) * (-1.0F + Server.rand.nextFloat());
/*       */     } 
/*       */     
/*  6916 */     int newTileX = CoordUtils.WorldToTile(newPosX);
/*  6917 */     int newTileY = CoordUtils.WorldToTile(newPosY);
/*  6918 */     return new int[] { newTileX, newTileY };
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void putItemInCorner(Creature creature, int cornerX, int cornerY, boolean onSurface, long bridgeId, boolean atFeet) throws NoSuchItemException {
/*       */     float lRotation;
/*  6927 */     if (isRoadMarker()) {
/*       */ 
/*       */       
/*  6930 */       lRotation = 0.0F;
/*       */     }
/*  6932 */     else if (isTileAligned()) {
/*       */       
/*  6934 */       lRotation = 90.0F * Creature.normalizeAngle((int)((creature.getStatus().getRotation() + 45.0F) / 90.0F));
/*       */     }
/*       */     else {
/*       */       
/*  6938 */       lRotation = Creature.normalizeAngle(creature.getStatus().getRotation());
/*       */     } 
/*       */     
/*  6941 */     long lParentId = getParentId();
/*  6942 */     if (lParentId != -10L) {
/*       */       
/*  6944 */       Item parent = Items.getItem(lParentId);
/*  6945 */       parent.dropItem(getWurmId(), false);
/*       */     } 
/*       */     
/*  6948 */     float newPosX = CoordUtils.TileToWorld(cornerX);
/*  6949 */     float newPosY = CoordUtils.TileToWorld(cornerY);
/*       */     
/*  6951 */     if (atFeet) {
/*       */       
/*  6953 */       newPosX = creature.getPosX();
/*  6954 */       newPosY = creature.getPosY();
/*       */     
/*       */     }
/*  6957 */     else if (!isRoadMarker()) {
/*       */       
/*  6959 */       newPosX += 0.005F;
/*  6960 */       newPosY += 0.005F;
/*       */ 
/*       */ 
/*       */       
/*  6964 */       if (creature.getTileX() < cornerX)
/*  6965 */         newPosX -= 0.01F; 
/*  6966 */       if (creature.getTileY() < cornerY)
/*  6967 */         newPosY -= 0.01F; 
/*       */     } 
/*  6969 */     newPosX = Math.max(0.0F, newPosX);
/*  6970 */     newPosY = Math.max(0.0F, newPosY);
/*  6971 */     newPosY = Math.min(Zones.worldMeterSizeY, newPosY);
/*  6972 */     newPosX = Math.min(Zones.worldMeterSizeX, newPosX);
/*       */     
/*  6974 */     setOnBridge(bridgeId);
/*  6975 */     setSurfaced(onSurface);
/*  6976 */     float npsz = Zones.calculatePosZ(newPosX, newPosY, null, onSurface, (
/*  6977 */         isFloating() && getCurrentQualityLevel() > 10.0F), getPosZ(), creature, onBridge());
/*       */     
/*  6979 */     setPos(newPosX, newPosY, npsz, lRotation, onBridge());
/*       */     
/*       */     try {
/*  6982 */       Zone zone = Zones.getZone(Zones.safeTileX((int)newPosX >> 2), 
/*  6983 */           Zones.safeTileY((int)newPosY >> 2), onSurface);
/*  6984 */       zone.addItem(this);
/*       */     }
/*  6986 */     catch (NoSuchZoneException sex) {
/*       */       
/*  6988 */       logWarn(sex.getMessage(), (Throwable)sex);
/*  6989 */       creature.getInventory().insertItem(this, true);
/*  6990 */       creature.getCommunicator().sendNormalServerMessage("Unable to drop there.");
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
/*       */   public final void putItemInfrontof(Creature creature) throws NoSuchCreatureException, NoSuchItemException, NoSuchPlayerException, NoSuchZoneException {
/*  7007 */     putItemInfrontof(creature, 1.0F);
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
/*       */   public final void putItemInfrontof(Creature creature, float distance) throws NoSuchCreatureException, NoSuchItemException, NoSuchPlayerException, NoSuchZoneException {
/*       */     float lRotation;
/*  7024 */     CreatureStatus creatureStatus = creature.getStatus();
/*  7025 */     float lPosX = creatureStatus.getPositionX();
/*  7026 */     float lPosY = creatureStatus.getPositionY();
/*       */     
/*  7028 */     float rot = Creature.normalizeAngle(creatureStatus.getRotation());
/*  7029 */     float xPosMod = (float)Math.sin((rot * 0.017453292F)) * (distance + Server.rand.nextFloat() * distance);
/*  7030 */     float yPosMod = -((float)Math.cos((rot * 0.017453292F))) * (distance + Server.rand.nextFloat() * distance);
/*       */     
/*  7032 */     float newPosX = lPosX + xPosMod;
/*  7033 */     float newPosY = lPosY + yPosMod;
/*       */     
/*  7035 */     boolean onSurface = creature.isOnSurface();
/*  7036 */     if (distance != 0.0F) {
/*       */       
/*  7038 */       BlockingResult result = Blocking.getBlockerBetween(creature, lPosX, lPosY, newPosX, newPosY, creature
/*  7039 */           .getPositionZ(), creature.getPositionZ(), onSurface, onSurface, false, 4, -1L, creature
/*  7040 */           .getBridgeId(), creature.getBridgeId(), false);
/*  7041 */       if (result != null) {
/*       */         
/*  7043 */         newPosX = lPosX + (float)Math.sin((rot * 0.017453292F)) * (-1.0F + Server.rand.nextFloat());
/*  7044 */         newPosY = lPosY - (float)Math.cos((rot * 0.017453292F)) * (-1.0F + Server.rand.nextFloat());
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/*  7049 */     setOnBridge(creatureStatus.getBridgeId());
/*  7050 */     if (onBridge() != -10L) {
/*       */       
/*  7052 */       newPosX = lPosX;
/*  7053 */       newPosY = lPosY;
/*       */     } 
/*       */     
/*  7056 */     if (!onSurface && distance != 0.0F)
/*       */     {
/*       */ 
/*       */       
/*  7060 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(Zones.safeTileX((int)newPosX >> 2), 
/*  7061 */               Zones.safeTileY((int)newPosY >> 2))))) {
/*       */         
/*  7063 */         newPosX = lPosX;
/*  7064 */         newPosY = lPosY;
/*       */       } 
/*       */     }
/*  7067 */     newPosX = Math.max(0.0F, newPosX);
/*  7068 */     newPosY = Math.max(0.0F, newPosY);
/*  7069 */     newPosY = Math.min(Zones.worldMeterSizeY, newPosY);
/*  7070 */     newPosX = Math.min(Zones.worldMeterSizeX, newPosX);
/*       */ 
/*       */     
/*  7073 */     if (isTileAligned()) {
/*  7074 */       lRotation = 90.0F * Creature.normalizeAngle((int)((creatureStatus.getRotation() + 45.0F) / 90.0F));
/*       */ 
/*       */     
/*       */     }
/*       */     else {
/*       */ 
/*       */ 
/*       */       
/*  7082 */       lRotation = Creature.normalizeAngle(creatureStatus.getRotation());
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/*  7087 */     long lParentId = getParentId();
/*  7088 */     if (lParentId != -10L) {
/*       */       
/*  7090 */       Item parent = Items.getItem(lParentId);
/*  7091 */       parent.dropItem(getWurmId(), false);
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  7117 */     float npsz = Zones.calculatePosZ(newPosX, newPosY, null, onSurface, (
/*  7118 */         isFloating() && getCurrentQualityLevel() > 10.0F), getPosZ(), creature, onBridge());
/*  7119 */     setPos(newPosX, newPosY, npsz, lRotation, onBridge());
/*  7120 */     setSurfaced(onSurface);
/*       */     
/*       */     try {
/*  7123 */       Zone zone = Zones.getZone(Zones.safeTileX((int)newPosX >> 2), Zones.safeTileY((int)newPosY >> 2), onSurface);
/*       */       
/*  7125 */       zone.addItem(this);
/*  7126 */       if (creature.getPower() == 5 && isBoat()) {
/*  7127 */         creature.getCommunicator().sendNormalServerMessage("Adding to zone " + zone
/*  7128 */             .getId() + " at " + Zones.safeTileX((int)newPosX >> 2) + ", " + 
/*  7129 */             Zones.safeTileY((int)newPosY >> 2) + ", surf=" + onSurface);
/*       */       
/*       */       }
/*       */     }
/*  7133 */     catch (NoSuchZoneException sex) {
/*       */       
/*  7135 */       logWarn(sex.getMessage(), (Throwable)sex);
/*  7136 */       creature.getInventory().insertItem(this, true);
/*  7137 */       creature.getCommunicator().sendNormalServerMessage("Unable to drop there.");
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final float calculatePosZ(VolaTile tile, @Nullable Creature creature) {
/*  7144 */     boolean floating = (isFloating() && getCurrentQualityLevel() > 10.0F);
/*  7145 */     return Zones.calculatePosZ(getPosX(), getPosY(), tile, isOnSurface(), floating, getPosZ(), creature, onBridge());
/*       */   }
/*       */ 
/*       */   
/*       */   public final void updatePosZ(VolaTile tile) {
/*  7150 */     setPosZ(calculatePosZ(tile, null));
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWarmachine() {
/*  7155 */     return this.template.isWarmachine();
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean isWeaponContainer() {
/*  7160 */     int templateId = getTemplateId();
/*  7161 */     switch (templateId) {
/*       */       
/*       */       case 724:
/*       */       case 725:
/*       */       case 758:
/*       */       case 759:
/*       */       case 892:
/*  7168 */         return true;
/*       */     } 
/*  7170 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private Item removeItem(long _id, boolean setPosition, boolean ignoreWatchers, boolean skipPileRemoval) throws NoSuchItemException {
/*  7177 */     if (!isHollow()) {
/*  7178 */       throw new NoSuchItemException(String.valueOf(_id));
/*       */     }
/*       */     
/*  7181 */     Item item = Items.getItem(_id);
/*       */     
/*  7183 */     if (item.isBodyPart() && item.isBodyPartAttached()) {
/*  7184 */       throw new NoSuchItemException("Can't remove parts from a live body.");
/*       */     }
/*       */     
/*  7187 */     long ownerId = getOwnerId();
/*  7188 */     if (this.place > 0) {
/*       */       
/*       */       try {
/*       */         
/*  7192 */         byte equipmentSlot = BodyTemplate.convertToItemEquipementSlot((byte)this.place);
/*  7193 */         if (item.isArmour() && item.wornAsArmour)
/*       */         {
/*  7195 */           equipmentSlot = BodyTemplate.convertToArmorEquipementSlot((byte)this.place);
/*       */         }
/*       */         
/*  7198 */         Creature owner = Server.getInstance().getCreature(ownerId);
/*  7199 */         if (owner.isAnimal() && owner.isVehicle()) {
/*       */           
/*  7201 */           owner.getCurrentTile().sendRemoveHorseWear(ownerId, item.getTemplateId(), equipmentSlot);
/*       */         } else {
/*       */           
/*  7204 */           owner.getCurrentTile().sendRemoveWearItem(ownerId, equipmentSlot);
/*       */         } 
/*  7206 */         if (item.hasItemBonus() && owner.isPlayer())
/*       */         {
/*  7208 */           ItemBonus.removeBonus(item, owner);
/*       */         }
/*  7210 */         if (item.getTemplateId() == 330)
/*       */         {
/*  7212 */           owner.setHasCrownEffect(false);
/*       */         }
/*       */       }
/*  7215 */       catch (Exception exception) {}
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  7221 */     removeItem(item);
/*       */     
/*  7223 */     if (getTemplateId() == 621 && item.isSaddleBags()) {
/*       */       
/*       */       try {
/*       */         
/*  7227 */         Creature owner = Server.getInstance().getCreature(ownerId);
/*  7228 */         if (owner.isAnimal() && owner.isVehicle())
/*       */         {
/*  7230 */           byte equipmentSlot = BodyTemplate.convertToItemEquipementSlot((byte)(getParent()).place);
/*  7231 */           owner.getCurrentTile().sendHorseWear(ownerId, getTemplateId(), getMaterial(), equipmentSlot, getAuxData());
/*       */         }
/*       */       
/*  7234 */       } catch (NoSuchPlayerException|NoSuchCreatureException noSuchPlayerException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  7239 */     if (getTemplate().hasViewableSubItems() && (!getTemplate().isContainerWithSubItems() || item.isPlacedOnParent())) {
/*       */       
/*  7241 */       VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/*  7242 */       if (vt != null)
/*       */       {
/*  7244 */         for (VirtualZone vz : vt.getWatchers()) {
/*  7245 */           vz.getWatcher().getCommunicator().sendRemoveItem(item);
/*       */         }
/*       */       }
/*       */     } 
/*  7249 */     if (item.wornAsArmour)
/*       */     {
/*  7251 */       item.setWornAsArmour(false, ownerId);
/*       */     }
/*       */     
/*  7254 */     boolean send = true;
/*       */ 
/*       */     
/*  7257 */     if (item.isArmour()) {
/*       */       
/*  7259 */       byte[] armourplaces = item.getBodySpaces();
/*  7260 */       for (byte armourplace : armourplaces)
/*       */       {
/*  7262 */         if (armourplace == this.place)
/*       */         {
/*  7264 */           send = false;
/*       */         }
/*       */       }
/*       */     
/*  7268 */     } else if (isFire()) {
/*       */       
/*  7270 */       if (item.isFoodMaker() || item.isFood()) {
/*       */         
/*  7272 */         VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  7273 */         if (t != null)
/*       */         {
/*  7275 */           t.renameItem(this);
/*       */         }
/*       */       } 
/*       */     } 
/*  7279 */     if (isWeaponContainer() || isBarrelRack())
/*       */     {
/*  7281 */       if (isEmpty(false)) {
/*       */         
/*  7283 */         VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  7284 */         if (t != null)
/*       */         {
/*  7286 */           t.renameItem(this);
/*       */         }
/*       */       } 
/*       */     }
/*       */     
/*  7291 */     if (getTemplate().getContainerRestrictions() != null && !getTemplate().isNoPut())
/*       */     {
/*  7293 */       for (ContainerRestriction cRest : getTemplate().getContainerRestrictions()) {
/*       */         
/*  7295 */         if (cRest.doesItemOverrideSlot(item)) {
/*       */           
/*  7297 */           boolean skipAdd = false;
/*  7298 */           for (Item i : getItems()) {
/*  7299 */             if (i.getTemplateId() == 1392 && cRest.contains(i.getRealTemplateId())) {
/*  7300 */               skipAdd = true; continue;
/*  7301 */             }  if (cRest.contains(i.getTemplateId()))
/*  7302 */               skipAdd = true; 
/*       */           } 
/*  7304 */           if (!skipAdd) {
/*       */             
/*       */             try {
/*       */               
/*  7308 */               Item tempSlotItem = ItemFactory.createItem(1392, 100.0F, getCreatorName());
/*  7309 */               tempSlotItem.setRealTemplate(cRest.getEmptySlotTemplateId());
/*  7310 */               tempSlotItem.setName(cRest.getEmptySlotName());
/*       */               
/*  7312 */               insertItem(tempSlotItem, true);
/*       */             }
/*  7314 */             catch (FailedException|NoSuchTemplateException failedException) {}
/*       */           }
/*       */         } 
/*       */       } 
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*  7323 */     if (send)
/*       */     {
/*  7325 */       if (this.ownerId > 0L && (this.place == 37 || this.place == 38) && 
/*       */         
/*  7327 */         isBodyPartAttached()) {
/*       */ 
/*       */         
/*       */         try {
/*  7331 */           Creature owner = Server.getInstance().getCreature(ownerId);
/*  7332 */           owner.getCurrentTile().sendWieldItem(ownerId, (this.place == 37) ? 0 : 1, "", (byte)0, 0, 0, 0, 0, 0, 0);
/*       */ 
/*       */         
/*       */         }
/*  7336 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */         
/*  7339 */         } catch (NoSuchPlayerException nsp) {
/*       */           
/*  7341 */           logWarn(nsp.getMessage(), (Throwable)nsp);
/*       */         }
/*       */       
/*  7344 */       } else if (this.place == 3 && this.ownerId > 0L && item.isShield()) {
/*       */ 
/*       */         
/*       */         try {
/*  7348 */           Creature owner = Server.getInstance().getCreature(ownerId);
/*  7349 */           owner.getCommunicator().sendToggleShield(false);
/*       */         }
/*  7351 */         catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */         
/*  7354 */         } catch (NoSuchPlayerException nsp) {
/*       */           
/*  7356 */           logWarn(nsp.getMessage(), (Throwable)nsp);
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/*  7361 */     long topParent = getTopParent();
/*  7362 */     if (isEmpty(false)) {
/*       */       
/*  7364 */       if (this.watchers != null)
/*       */       {
/*  7366 */         for (Creature watcher : this.watchers) {
/*       */           
/*  7368 */           boolean isOwner = (ownerId == watcher.getWurmId());
/*  7369 */           long inventoryWindow = isOwner ? -1L : topParent;
/*  7370 */           Communicator watcherComm = watcher.getCommunicator();
/*  7371 */           if (item.getTopParentOrNull() != null && 
/*  7372 */             !item.getTopParentOrNull().isEquipmentSlot() && 
/*  7373 */             !item.getTopParentOrNull().isBodyPart() && 
/*  7374 */             !item.getTopParentOrNull().isInventory())
/*       */           {
/*  7376 */             watcherComm.sendRemoveFromInventory(item, inventoryWindow);
/*       */           }
/*  7378 */           watcherComm.sendIsEmpty(inventoryWindow, getWurmId());
/*       */         } 
/*       */       }
/*       */       
/*  7382 */       if (getTemplateId() == 177 && !skipPileRemoval) {
/*       */         
/*       */         try {
/*       */           
/*  7386 */           Zone z = Zones.getZone((int)getPosX() >> 2, (int)getPosY() >> 2, isOnSurface());
/*  7387 */           z.removeItem(this);
/*       */         }
/*  7389 */         catch (NoSuchZoneException noSuchZoneException) {}
/*       */       }
/*       */ 
/*       */ 
/*       */       
/*  7394 */       if (getTemplateId() == 995 || getTemplateId() == 1422)
/*       */       {
/*  7396 */         if (isEmpty(false))
/*       */         {
/*  7398 */           Items.destroyItem(getWurmId());
/*       */           
/*  7400 */           item.parentId = -10L;
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*  7405 */     else if (item.getTopParent() == topParent && 
/*  7406 */       !isEmpty(false) && item
/*  7407 */       .getTopParentOrNull() != null && 
/*  7408 */       !item.getTopParentOrNull().isInventory() && 
/*  7409 */       !item.getTopParentOrNull().isBodyPart() && 
/*  7410 */       !item.getTopParentOrNull().isEquipmentSlot()) {
/*       */       
/*  7412 */       if (this.watchers != null && !ignoreWatchers)
/*       */       {
/*  7414 */         for (Creature watcher : this.watchers)
/*       */         {
/*  7416 */           boolean isOwner = (ownerId == watcher.getWurmId());
/*  7417 */           long inventoryWindow = isOwner ? -1L : topParent;
/*  7418 */           watcher.getCommunicator().sendRemoveFromInventory(item, inventoryWindow);
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*  7423 */     else if (item.getTopParent() != topParent) {
/*       */       
/*  7425 */       if (ownerId != -10L && item.getOwnerId() == ownerId)
/*       */       {
/*       */ 
/*       */         
/*  7429 */         if (this.watchers != null)
/*       */         {
/*  7431 */           for (Creature watcher : this.watchers) {
/*       */             
/*  7433 */             logInfo(watcher.getName() + " checking if stopping to watch " + item.getName());
/*  7434 */             if (item.watchers == null || !item.watchers.contains(watcher)) {
/*       */               
/*  7436 */               logInfo("Removing watcher " + watcher + " in new method");
/*  7437 */               item.removeWatcher(watcher, true);
/*       */             } 
/*       */           } 
/*       */         }
/*       */       }
/*       */     } 
/*       */     
/*  7444 */     if (setPosition)
/*       */     {
/*  7446 */       item.setPosXYZ(getPosX(), getPosY(), getPosZ());
/*       */     }
/*       */     
/*  7449 */     return item;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final long getWurmId() {
/*  7455 */     return this.id;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void removeAndEmpty() {
/*  7460 */     this.deleted = true;
/*  7461 */     if (this.items != null && isHollow()) {
/*       */       
/*  7463 */       VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*       */       
/*  7465 */       Item[] contained = getAllItems(true);
/*  7466 */       if (isBulkContainer()) {
/*       */         
/*  7468 */         for (Item aContained : contained)
/*       */         {
/*  7470 */           Items.destroyItem(aContained.getWurmId());
/*       */         }
/*       */       }
/*       */       else {
/*       */         
/*  7475 */         for (Item item : contained) {
/*       */ 
/*       */           
/*  7478 */           if (item.getTemplateId() == 1392) {
/*       */             
/*  7480 */             Items.destroyItem(item.getWurmId());
/*       */           } else {
/*       */             
/*  7483 */             item.setPosXYZ(getPosX(), getPosY(), item.calculatePosZ(t, null));
/*       */           } 
/*       */         } 
/*       */       } 
/*       */     } 
/*       */     try {
/*  7489 */       Item parent = getParent();
/*       */       
/*  7491 */       boolean pile = (parent.getTemplateId() == 177);
/*       */       
/*  7493 */       int x = (int)getPosX() >> 2;
/*  7494 */       int y = (int)getPosY() >> 2;
/*  7495 */       parent.dropItem(this.id, false);
/*  7496 */       if (pile)
/*       */       {
/*  7498 */         parent.removeFromPile(this);
/*       */       }
/*  7500 */       Set<Item> its = getItems();
/*  7501 */       Item[] itarr = its.<Item>toArray(new Item[its.size()]);
/*  7502 */       for (Item item : itarr)
/*       */       {
/*  7504 */         dropItem(item.getWurmId(), false);
/*       */         
/*  7506 */         if (!item.isTransferred())
/*       */         {
/*       */ 
/*       */           
/*  7510 */           if (pile) {
/*       */ 
/*       */             
/*  7513 */             if (item.isLiquid()) {
/*       */               
/*  7515 */               Items.decay(item.getWurmId(), item.getDbStrings());
/*       */             } else {
/*       */ 
/*       */               
/*       */               try {
/*  7520 */                 Zone currentZone = Zones.getZone(x, y, isOnSurface());
/*       */ 
/*       */                 
/*  7523 */                 currentZone.addItem(item);
/*       */               }
/*  7525 */               catch (NoSuchZoneException nsz) {
/*  7526 */                 logWarn(getName() + " id:" + this.id + " at " + x + ", " + y, (Throwable)nsz);
/*       */               }
/*       */             
/*       */             }
/*       */           
/*  7531 */           } else if (item.isLiquid()) {
/*       */             
/*  7533 */             Items.decay(item.getWurmId(), item.getDbStrings());
/*       */           }
/*       */           else {
/*       */             
/*  7537 */             Item topParent = parent.getTopParentOrNull();
/*  7538 */             boolean dropToGround = false;
/*       */             
/*  7540 */             if (item.isUseOnGroundOnly() && topParent != null && topParent.getTemplateId() != 0) {
/*  7541 */               dropToGround = true;
/*       */             }
/*       */             
/*  7544 */             if (!dropToGround && parent.isBodyPartAttached()) {
/*  7545 */               dropToGround = true;
/*       */             }
/*       */             
/*  7548 */             if (!dropToGround && !parent.isBodyPartAttached() && !parent.insertItem(item, false, true)) {
/*  7549 */               dropToGround = true;
/*       */             }
/*  7551 */             if (dropToGround) {
/*       */ 
/*       */               
/*       */               try {
/*  7555 */                 Zone currentZone = Zones.getZone(x, y, isOnSurface());
/*       */ 
/*       */                 
/*  7558 */                 currentZone.addItem(item);
/*       */               }
/*  7560 */               catch (NoSuchZoneException nsz) {
/*       */                 
/*  7562 */                 logWarn(getName() + " id:" + this.id + " at " + x + ", " + y, (Throwable)nsz);
/*       */               } 
/*       */             } else {
/*       */ 
/*       */               
/*       */               try {
/*  7568 */                 Creature owner = Server.getInstance().getCreature(this.ownerId);
/*  7569 */                 owner.getInventory().insertItem(item);
/*       */               }
/*  7571 */               catch (Exception exception) {}
/*       */             
/*       */             }
/*       */ 
/*       */           
/*       */           }
/*       */ 
/*       */         
/*       */         }
/*       */       }
/*       */     
/*       */     }
/*  7583 */     catch (NoSuchItemException nsi) {
/*       */ 
/*       */       
/*  7586 */       if (getParentId() != -10L) {
/*       */         return;
/*       */       }
/*  7589 */       if (this.zoneId == -10L) {
/*       */         return;
/*       */       }
/*       */ 
/*       */       
/*       */       try {
/*  7595 */         int x = (int)getPosX() >> 2;
/*  7596 */         int y = (int)getPosY() >> 2;
/*  7597 */         Zone currentZone = Zones.getZone(x, y, isOnSurface());
/*       */         
/*  7599 */         currentZone.removeItem(this);
/*       */         
/*  7601 */         if (!isHollow()) {
/*       */           return;
/*       */         }
/*       */ 
/*       */         
/*       */         try {
/*  7607 */           Creature[] iwatchers = getWatchers();
/*  7608 */           for (Creature iwatcher : iwatchers) {
/*  7609 */             iwatcher.removeItemWatched(this);
/*  7610 */             iwatcher.getCommunicator().sendCloseInventoryWindow(getWurmId());
/*  7611 */             removeWatcher(iwatcher, true);
/*       */           }
/*       */         
/*  7614 */         } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  7620 */         Set<Item> its = getItems();
/*  7621 */         Item[] itarr = its.<Item>toArray(new Item[its.size()]);
/*  7622 */         for (Item item : itarr)
/*       */         {
/*       */           try {
/*  7625 */             dropItem(item.getWurmId(), false, true);
/*  7626 */             if (!item.isTransferred()) {
/*  7627 */               if (item.isLiquid()) {
/*  7628 */                 Items.decay(item.getWurmId(), item.getDbStrings());
/*       */               } else {
/*  7630 */                 currentZone.addItem(item);
/*       */               }
/*       */             
/*       */             }
/*  7634 */           } catch (NoSuchItemException nsi2) {
/*  7635 */             logWarn(getName() + " id:" + this.id + " at " + x + ", " + y + " failed to drop item " + item
/*  7636 */                 .getWurmId(), (Throwable)nsi2);
/*       */           } 
/*  7638 */           this.items.getClass();
/*       */         }
/*       */       
/*  7641 */       } catch (NoSuchZoneException nsz) {
/*       */         
/*  7643 */         logWarn(getName() + " id:" + this.id, (Throwable)nsz);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTypeRecycled() {
/*  7650 */     return this.template.isRecycled;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hideAddToCreationWindow() {
/*  7655 */     return this.template.hideAddToCreationWindow();
/*       */   }
/*       */ 
/*       */   
/*       */   private void hatch() {
/*  7660 */     if (isEgg() && getData1() > 0) {
/*       */ 
/*       */       
/*       */       try {
/*  7664 */         CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(getData1());
/*  7665 */         byte sex = temp.getSex();
/*  7666 */         if (sex == 0)
/*       */         {
/*  7668 */           if (Server.rand.nextInt(2) == 0) {
/*  7669 */             sex = 1;
/*       */           }
/*       */         }
/*  7672 */         if (temp.isUnique() || Server.rand.nextInt(10) == 0)
/*       */         {
/*  7674 */           if (temp.isUnique() || Creatures.getInstance().getNumberOfCreatures() < Servers.localServer.maxCreatures) {
/*       */             
/*  7676 */             CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(getData1());
/*  7677 */             String cname = "";
/*  7678 */             String description = getDescription();
/*  7679 */             if (!description.isEmpty())
/*       */             {
/*  7681 */               cname = LoginHandler.raiseFirstLetter(description
/*  7682 */                   .substring(0, Math.min(description.length(), 10)) + " the " + ct
/*  7683 */                   .getName());
/*       */             }
/*       */             
/*  7686 */             Creature c = Creature.doNew(getData1(), false, getPosX(), getPosY(), Server.rand.nextInt(360), 
/*  7687 */                 isOnSurface() ? 0 : -1, cname, sex, (byte)0, (byte)0, false, (byte)1);
/*       */             
/*  7689 */             if (temp.isUnique()) {
/*  7690 */               logInfo("Player/creature with wurmid " + getLastOwnerId() + " hatched " + c.getName() + " at " + ((int)this.posX / 4) + "," + ((int)this.posY / 4));
/*       */             }
/*       */             
/*  7693 */             if (Servers.isThisATestServer()) {
/*  7694 */               Players.getInstance().sendGmMessage(null, "System", "Debug: Player/creature with wurmid " + getLastOwnerId() + " hatched " + c.getName() + " at " + ((int)this.posX / 4) + "," + ((int)this.posY / 4), false);
/*       */             }
/*       */             
/*  7697 */             if (getData1() == 48)
/*       */             {
/*  7699 */               switch (Server.rand.nextInt(3)) {
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*       */                 case 1:
/*  7705 */                   c.getStatus().setTraitBit(15, true);
/*       */                   break;
/*       */                 case 2:
/*  7708 */                   c.getStatus().setTraitBit(16, true);
/*       */                   break;
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */           } 
/*       */         }
/*  7741 */       } catch (Exception ex) {
/*       */         
/*  7743 */         logWarn(ex.getMessage() + ' ' + getData1());
/*       */       } 
/*       */       
/*  7746 */       setData1(-1);
/*       */     }
/*  7748 */     else if (getTemplateId() == 466) {
/*       */       
/*  7750 */       if (this.ownerId == -10L) {
/*       */         
/*       */         try {
/*       */           
/*  7754 */           int x = (int)getPosX() >> 2;
/*  7755 */           int y = (int)getPosY() >> 2;
/*  7756 */           Zone currentZone = Zones.getZone(x, y, isOnSurface());
/*  7757 */           Item i = TileRockBehaviour.createRandomGem();
/*  7758 */           if (i != null)
/*       */           {
/*  7760 */             i.setLastOwnerId(this.lastOwner);
/*  7761 */             i.setPosXY(getPosX(), getPosY());
/*  7762 */             i.setRotation(Server.rand.nextFloat() * 180.0F);
/*  7763 */             currentZone.addItem(i);
/*       */           }
/*       */         
/*  7766 */         } catch (Exception ex) {
/*       */           
/*  7768 */           logWarn(ex.getMessage() + ' ' + getData1());
/*       */         } 
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean checkDecay() {
/*  7776 */     if (isHugeAltar()) {
/*  7777 */       return false;
/*       */     }
/*  7779 */     if (this.qualityLevel > 0.0F && this.damage < 100.0F) {
/*  7780 */       return false;
/*       */     }
/*       */     
/*  7783 */     boolean decayed = true;
/*  7784 */     if (this.ownerId != -10L) {
/*       */       
/*  7786 */       Creature owner = null;
/*       */       
/*       */       try {
/*  7789 */         owner = Server.getInstance().getCreature(getOwnerId());
/*  7790 */         if (hasItemBonus() && owner.isPlayer()) {
/*  7791 */           ItemBonus.removeBonus(this, owner);
/*       */         }
/*       */         try {
/*  7794 */           Action act = owner.getCurrentAction();
/*  7795 */           if (act.getSubjectId() == this.id) {
/*  7796 */             act.stop(false);
/*       */           }
/*       */         }
/*  7799 */         catch (NoSuchActionException noSuchActionException) {}
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  7804 */         Communicator ownerComm = owner.getCommunicator();
/*  7805 */         if (isEgg()) {
/*       */           
/*  7807 */           if (getTemplateId() == 466 || getData1() > 0) {
/*  7808 */             ownerComm.sendNormalServerMessage(
/*  7809 */                 LoginHandler.raiseFirstLetter(getNameWithGenus()) + " hatches!");
/*       */           }
/*  7811 */           if (Servers.isThisATestServer() && getData1() > 0) {
/*  7812 */             Players.getInstance().sendGmMessage(null, "System", "Debug: decayed a fertile egg at " + ((int)this.posX / 4) + "," + ((int)this.posY / 4) + ", Data1=" + getData1(), false);
/*       */           }
/*  7814 */           hatch();
/*       */           
/*  7816 */           if (getTemplateId() == 466) {
/*       */             
/*  7818 */             Item i = TileRockBehaviour.createRandomGem();
/*  7819 */             if (i != null) {
/*  7820 */               owner.getInventory().insertItem(i, true);
/*  7821 */               ownerComm.sendNormalServerMessage(
/*  7822 */                   LoginHandler.raiseFirstLetter("You find something in the " + getName()) + "!");
/*       */             }
/*       */           
/*       */           } 
/*  7826 */         } else if (!isFishingBait()) {
/*       */           
/*  7828 */           ownerComm.sendNormalServerMessage(
/*  7829 */               LoginHandler.raiseFirstLetter(getNameWithGenus()) + " is useless and you throw it away.");
/*       */         }
/*       */       
/*  7832 */       } catch (NoSuchCreatureException|NoSuchPlayerException noSuchCreatureException) {}
/*       */ 
/*       */     
/*       */     }
/*       */     else {
/*       */ 
/*       */ 
/*       */       
/*  7840 */       sendDecayMess();
/*  7841 */       if (isEgg())
/*       */       {
/*  7843 */         hatch();
/*       */       }
/*       */       
/*  7846 */       if (this.hatching)
/*       */       {
/*  7848 */         if (getTemplateId() == 805) {
/*       */           
/*  7850 */           IslandAdder adder = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*       */           
/*  7852 */           Map<Integer, Set<Integer>> changes = adder.forceIsland(50, 50, getTileX() - 25, getTileY() - 25);
/*       */           
/*  7854 */           if (changes != null)
/*       */           {
/*  7856 */             for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet())
/*       */             {
/*  7858 */               Integer x = me.getKey();
/*  7859 */               Set<Integer> set = me.getValue();
/*  7860 */               for (Integer y : set)
/*       */               {
/*  7862 */                 Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*       */               }
/*       */             }
/*       */           
/*       */           }
/*  7867 */         } else if (getTemplateId() == 1009) {
/*       */           
/*  7869 */           TerraformingTask task = new TerraformingTask(0, (byte)0, this.creator, 2, 0, true);
/*       */           
/*  7871 */           task.setCoordinates();
/*  7872 */           task.setSXY(getTileX(), getTileY());
/*       */         } 
/*       */       }
/*       */     } 
/*       */     
/*  7877 */     Items.destroyItem(this.id);
/*       */ 
/*       */     
/*  7880 */     return decayed;
/*       */   }
/*       */ 
/*       */   
/*       */   private void sendDecayMess() {
/*  7885 */     String msgSuff = "";
/*  7886 */     int dist = 0;
/*       */     
/*  7888 */     if (isEgg()) {
/*       */       
/*  7890 */       if (getData1() > 0) {
/*       */         
/*  7892 */         msgSuff = " cracks open!";
/*  7893 */         dist = 10;
/*  7894 */       } else if (getTemplateId() == 466) {
/*       */         
/*  7896 */         msgSuff = " cracks open! Something is inside!";
/*  7897 */         dist = 5;
/*       */       } 
/*  7899 */     } else if (!isTemporary()) {
/*       */       
/*  7901 */       msgSuff = " crumbles to dust.";
/*  7902 */       dist = 2;
/*       */     } 
/*       */     
/*  7905 */     if (msgSuff.isEmpty()) {
/*       */       return;
/*       */     }
/*       */     
/*  7909 */     String fullMsgStr = LoginHandler.raiseFirstLetter(getNameWithGenus()) + msgSuff;
/*       */     
/*  7911 */     if (this.watchers != null) {
/*  7912 */       for (Creature watcher : this.watchers) {
/*  7913 */         watcher.getCommunicator().sendNormalServerMessage(fullMsgStr);
/*       */       }
/*       */       
/*       */       return;
/*       */     } 
/*       */     
/*  7919 */     if ((this.parentId != -10L && WurmId.getType(this.parentId) == 6) || isRepairable()) {
/*       */       
/*       */       try {
/*       */         
/*  7923 */         TilePos tilePos = getTilePos();
/*  7924 */         Zone currentZone = Zones.getZone(tilePos, isOnSurface());
/*       */         
/*  7926 */         Server.getInstance().broadCastMessage(fullMsgStr, tilePos.x, tilePos.y, currentZone.isOnSurface(), dist);
/*       */       }
/*  7928 */       catch (NoSuchZoneException nsz) {
/*       */         
/*  7930 */         logWarn(getName() + " id:" + this.id, (Throwable)nsz);
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
/*       */   public final TilePos getTilePos() {
/*  7942 */     return CoordUtils.WorldToTile(getPos2f());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getTileX() {
/*  7952 */     return CoordUtils.WorldToTile(getPosX());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getTileY() {
/*  7962 */     return CoordUtils.WorldToTile(getPosY());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCorpse() {
/*  7967 */     return (this.template.templateId == 272);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCrate() {
/*  7972 */     return (this.template.templateId == 852 || this.template.templateId == 851);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBarrelRack() {
/*  7978 */     return (this.template.templateId == 1108 || this.template.templateId == 1109 || this.template.templateId == 1111 || this.template.templateId == 1110);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isCarpet() {
/*  7986 */     return this.template.isCarpet();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void pollCoolingItems(Creature owner, long timeSinceLastCooled) {
/*  7992 */     if (isHollow())
/*       */     {
/*  7994 */       if (this.items != null) {
/*       */         
/*  7996 */         Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  7997 */         for (Item anItarr : itarr) {
/*  7998 */           if (!anItarr.deleted)
/*  7999 */             anItarr.pollCoolingItems(owner, timeSinceLastCooled); 
/*       */         } 
/*       */       } 
/*       */     }
/*  8003 */     coolInventoryItem(timeSinceLastCooled);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean pollOwned(Creature owner) {
/*  8008 */     boolean decayed = false;
/*  8009 */     short oldTemperature = getTemperature();
/*       */     
/*  8011 */     long maintenanceTimeDelta = WurmCalendar.currentTime - this.lastMaintained;
/*  8012 */     if (isFood() || (isAlwaysPoll() && !isFlag()) || isCorpse() || isPlantedFlowerpot() || 
/*  8013 */       getTemplateId() == 1276 || isInTacklebox()) {
/*       */       
/*  8015 */       if (this.hatching)
/*       */       {
/*  8017 */         return pollHatching();
/*       */       }
/*       */       
/*  8020 */       long decayt = this.template.getDecayTime();
/*       */       
/*  8022 */       if (this.template.templateId == 386) {
/*       */         
/*       */         try
/*       */         {
/*  8026 */           decayt = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDecayTime();
/*       */         }
/*  8028 */         catch (NoSuchTemplateException nst)
/*       */         {
/*  8030 */           logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */         }
/*       */       
/*  8033 */       } else if (this.template.templateId == 339) {
/*       */         
/*  8035 */         if (ArtifactBehaviour.getOrbActivation() > 0L)
/*       */         {
/*  8037 */           if (System.currentTimeMillis() - ArtifactBehaviour.getOrbActivation() > 21000L)
/*       */           {
/*  8039 */             if (WurmCalendar.currentTime - getData() < 360000L) {
/*       */               
/*  8041 */               ArtifactBehaviour.resetOrbActivation();
/*  8042 */               Server.getInstance().broadCastAction("A deadly field surges through the air from the location of " + owner
/*  8043 */                   .getName() + " and the " + 
/*  8044 */                   getName() + "!", owner, 25);
/*  8045 */               ArtifactBehaviour.markOrbRecipients(owner, false, 0.0F, 0.0F, 0.0F);
/*       */             } 
/*       */           }
/*       */         }
/*       */       } 
/*       */       
/*  8051 */       if (decayt == 28800L)
/*       */       {
/*  8053 */         if (this.damage == 0.0F) {
/*  8054 */           decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */         }
/*       */         else {
/*       */           
/*  8058 */           decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */         } 
/*       */       }
/*  8061 */       float lunchboxMod = 0.0F;
/*  8062 */       if (isInLunchbox()) {
/*       */         
/*  8064 */         Item lunchbox = getParentOuterItemOrNull();
/*  8065 */         if (lunchbox != null && lunchbox.getTemplateId() == 1296) {
/*  8066 */           lunchboxMod = 8.0F;
/*  8067 */         } else if (lunchbox != null && lunchbox.getTemplateId() == 1297) {
/*  8068 */           lunchboxMod = 9.0F;
/*  8069 */         }  decayt *= (getRarity() / 4 + 2);
/*       */       } 
/*  8071 */       if (isInTacklebox()) {
/*       */         
/*  8073 */         lunchboxMod = 7.0F;
/*  8074 */         decayt *= (getRarity() / 4 + 2);
/*       */       } 
/*       */ 
/*       */       
/*  8078 */       int adjDelta = (int)(maintenanceTimeDelta / decayt);
/*  8079 */       int timesSinceLastUsed = isLight() ? Math.min(1, adjDelta) : adjDelta;
/*  8080 */       if (timesSinceLastUsed > 0)
/*       */       {
/*  8082 */         float decayMin = 0.5F;
/*       */         
/*  8084 */         if (isFood() && owner.getDeity() != null && owner.getDeity().isItemProtector()) {
/*       */           
/*  8086 */           if ((owner.getFaith() >= 70.0F && owner.getFavor() >= 35.0F) || isCorpse())
/*       */           {
/*  8088 */             if (Server.rand.nextInt(5) == 0)
/*       */             {
/*  8090 */               if (this.template.destroyOnDecay)
/*       */               {
/*  8092 */                 decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(1.0F, 10.0F - lunchboxMod));
/*       */               }
/*       */               else
/*       */               {
/*  8096 */                 decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  8097 */                     Math.max(0.5F, getDamageModifier(true) - lunchboxMod));
/*       */               
/*       */               }
/*       */             
/*       */             }
/*       */           }
/*  8103 */           else if (this.template.destroyOnDecay)
/*       */           {
/*  8105 */             decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(1.0F, 10.0F - lunchboxMod));
/*       */           }
/*       */           else
/*       */           {
/*  8109 */             decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(0.5F, getDamageModifier(true) - lunchboxMod));
/*       */           }
/*       */         
/*       */         }
/*       */         else {
/*       */           
/*  8115 */           if (this.template.destroyOnDecay) {
/*       */             
/*  8117 */             decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(1.0F, 10.0F - lunchboxMod));
/*       */           }
/*       */           else {
/*       */             
/*  8121 */             decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(0.5F, getDamageModifier(true) - lunchboxMod));
/*       */           } 
/*       */           
/*  8124 */           if (isPlantedFlowerpot() && decayed) {
/*       */             
/*       */             try {
/*       */               
/*  8128 */               int revertType = -1;
/*  8129 */               if (isPotteryFlowerPot()) {
/*  8130 */                 revertType = 813;
/*  8131 */               } else if (isMarblePlanter()) {
/*  8132 */                 revertType = 1001;
/*       */               } else {
/*       */                 
/*  8135 */                 revertType = -1;
/*       */               } 
/*  8137 */               if (revertType != -1)
/*       */               {
/*  8139 */                 Item pot = ItemFactory.createItem(revertType, getQualityLevel(), this.creator);
/*  8140 */                 pot.setLastOwnerId(getLastOwnerId());
/*  8141 */                 pot.setDescription(getDescription());
/*  8142 */                 pot.setDamage(getDamage());
/*  8143 */                 owner.getInventory().insertItem(pot);
/*       */               }
/*       */             
/*  8146 */             } catch (NoSuchTemplateException|FailedException e) {
/*       */               
/*  8148 */               logWarn(e.getMessage(), (Throwable)e);
/*       */             } 
/*       */           }
/*       */         } 
/*  8152 */         if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  8153 */           setLastMaintained(WurmCalendar.currentTime);
/*       */         
/*       */         }
/*       */       }
/*       */     
/*       */     }
/*  8159 */     else if (Features.Feature.SADDLEBAG_DECAY.isEnabled() && owner != null && 
/*  8160 */       !owner.isPlayer() && !owner.isNpc() && isInside(new int[] { 1333, 1334 })) {
/*       */       
/*  8162 */       long decayt = this.template.getDecayTime();
/*  8163 */       boolean decayQl = false;
/*  8164 */       if (decayt == 28800L) {
/*       */         
/*  8166 */         if (this.damage == 0.0F) {
/*  8167 */           decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */         } else {
/*  8169 */           decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*  8170 */         }  decayQl = true;
/*       */       } 
/*  8172 */       int adjDelta = (int)(maintenanceTimeDelta / decayt);
/*  8173 */       int timesSinceLastUsed = isLight() ? Math.min(1, adjDelta) : adjDelta;
/*  8174 */       if (timesSinceLastUsed > 0) {
/*       */         
/*  8176 */         if (decayQl || Server.rand.nextInt(6) == 0) {
/*       */           
/*  8178 */           float decayMin = 0.2F;
/*  8179 */           if (this.template.destroyOnDecay) {
/*  8180 */             decayed = setDamage(this.damage + timesSinceLastUsed * 4.0F);
/*       */           } else {
/*  8182 */             decayed = setDamage(this.damage + timesSinceLastUsed * 0.2F * getDamageModifier(true));
/*       */           } 
/*       */         } 
/*  8185 */         if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  8186 */           setLastMaintained(WurmCalendar.currentTime);
/*       */         }
/*       */       } 
/*  8189 */     } else if (isHollow()) {
/*       */       
/*  8191 */       if (this.items != null) {
/*       */         
/*  8193 */         Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  8194 */         for (Item anItarr : itarr)
/*       */         {
/*  8196 */           if (!anItarr.deleted)
/*       */           {
/*  8198 */             anItarr.pollOwned(owner);
/*       */           }
/*       */         }
/*       */       
/*       */       } 
/*  8203 */     } else if (this.template.templateId == 166 && maintenanceTimeDelta > 2419200L) {
/*       */ 
/*       */       
/*  8206 */       setLastMaintained(WurmCalendar.currentTime);
/*       */     } 
/*       */ 
/*       */     
/*       */     try {
/*  8211 */       Item parent = getParent();
/*  8212 */       if (parent.isBodyPartAttached()) {
/*  8213 */         ItemBonus.checkDepleteAndRename(this, owner);
/*       */       }
/*  8215 */     } catch (NoSuchItemException noSuchItemException) {}
/*  8216 */     if (decayed) {
/*  8217 */       return true;
/*       */     }
/*       */     
/*  8220 */     if (isCompass()) {
/*       */       
/*  8222 */       Item bestCompass = owner.getBestCompass();
/*  8223 */       if (bestCompass == null || (bestCompass != this && bestCompass
/*  8224 */         .getCurrentQualityLevel() < getCurrentQualityLevel()))
/*       */       {
/*       */         
/*  8227 */         owner.setBestCompass(this);
/*       */       }
/*       */     } 
/*  8230 */     if (getTemplateId() == 1341) {
/*       */ 
/*       */       
/*  8233 */       Item bestTackleBox = owner.getBestTackleBox();
/*  8234 */       if (bestTackleBox == null || (bestTackleBox != this && bestTackleBox
/*  8235 */         .getCurrentQualityLevel() < getCurrentQualityLevel()))
/*       */       {
/*       */         
/*  8238 */         owner.setBestTackleBox(this);
/*       */       }
/*       */     } 
/*       */     
/*  8242 */     if (isToolbelt()) {
/*       */       
/*       */       try {
/*       */         
/*  8246 */         Item parent = getParent();
/*  8247 */         if (parent.getPlace() == 43 && parent.isBodyPartAttached())
/*       */         {
/*  8249 */           Item bestBelt = owner.getBestToolbelt();
/*  8250 */           if (bestBelt == null || (bestBelt != this && bestBelt
/*  8251 */             .getCurrentQualityLevel() < getCurrentQualityLevel()))
/*       */           {
/*       */             
/*  8254 */             owner.setBestToolbelt(this);
/*       */           }
/*       */         }
/*       */       
/*  8258 */       } catch (NoSuchItemException noSuchItemException) {}
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  8263 */     if (getTemplateId() == 1243 && getTemperature() >= 10000) {
/*       */       
/*  8265 */       Item bestBeeSmoker = owner.getBestBeeSmoker();
/*  8266 */       if (bestBeeSmoker == null || (bestBeeSmoker != this && bestBeeSmoker
/*  8267 */         .getCurrentQualityLevel() < getCurrentQualityLevel()))
/*       */       {
/*       */         
/*  8270 */         owner.setBestBeeSmoker(this);
/*       */       }
/*       */     } 
/*       */     
/*  8274 */     coolInventoryItem();
/*       */     
/*  8276 */     if (isLight() && isOnFire()) {
/*       */       
/*  8278 */       if (owner.getBestLightsource() != null) {
/*  8279 */         if (!owner.getBestLightsource().isLightBright() && isLightBright())
/*       */         {
/*  8281 */           owner.setBestLightsource(this, false);
/*       */         }
/*  8283 */         else if (owner.getBestLightsource() != this && owner
/*  8284 */           .getBestLightsource().getCurrentQualityLevel() < getCurrentQualityLevel())
/*       */         {
/*  8286 */           owner.setBestLightsource(this, false);
/*       */         }
/*       */       
/*       */       } else {
/*       */         
/*  8291 */         owner.setBestLightsource(this, false);
/*       */       } 
/*  8293 */       decayed = pollLightSource();
/*       */     }
/*  8295 */     else if (getTemplateId() == 1243) {
/*       */       
/*  8297 */       decayed = pollLightSource();
/*       */     } 
/*       */ 
/*       */     
/*  8301 */     if (getTemperatureState(oldTemperature) != getTemperatureState(this.temperature))
/*       */     {
/*  8303 */       notifyWatchersTempChange();
/*       */     }
/*       */ 
/*       */     
/*  8307 */     return decayed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void attackEnemies(boolean watchTowerpoll) {
/*  8312 */     if (!Servers.localServer.PVPSERVER) {
/*       */       return;
/*       */     }
/*       */     
/*  8316 */     int tileX = getTileX();
/*  8317 */     int tileY = getTileY();
/*       */ 
/*       */     
/*  8320 */     if (watchTowerpoll) {
/*       */       
/*  8322 */       int dist = 10;
/*  8323 */       int x1 = Zones.safeTileX(tileX - 10);
/*  8324 */       int x2 = Zones.safeTileX(tileX + 10);
/*  8325 */       int y1 = Zones.safeTileY(tileY - 10);
/*  8326 */       int y2 = Zones.safeTileY(tileY + 10);
/*       */ 
/*       */ 
/*       */       
/*  8330 */       for (TilePos tPos : TilePos.areaIterator(x1, y1, x2, y2)) {
/*       */         
/*  8332 */         int x = tPos.x;
/*  8333 */         int y = tPos.y;
/*  8334 */         VolaTile t = Zones.getTileOrNull(x, y, true);
/*  8335 */         if (t == null) {
/*       */           continue;
/*       */         }
/*  8338 */         if (getKingdom() != t.getKingdom()) {
/*       */           continue;
/*       */         }
/*  8341 */         for (Creature c : t.getCreatures()) {
/*       */           
/*  8343 */           if (c.getPower() <= 0)
/*       */           {
/*       */             
/*  8346 */             if (!c.isUnique() && 
/*  8347 */               !c.isInvulnerable() && c
/*  8348 */               .getKingdomId() != 0 && c
/*  8349 */               .getTemplate().isTowerBasher())
/*       */             {
/*       */ 
/*       */               
/*  8353 */               if (!c.isFriendlyKingdom(getKingdom()))
/*       */               {
/*       */                 
/*  8356 */                 if (Server.rand.nextFloat() * 200.0F < getCurrentQualityLevel()) {
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  8361 */                   VolaTile[] tiles = Zones.getTilesSurrounding(x, y, c.isOnSurface(), 5);
/*  8362 */                   for (VolaTile tile : tiles) {
/*  8363 */                     tile.broadCast("The " + getName() + " fires at " + c.getNameWithGenus() + ".");
/*       */                   }
/*       */                   
/*  8366 */                   float mod = 1.0F / c.getArmourMod();
/*  8367 */                   Arrows.shootCreature(this, c, (int)(mod * 10000.0F));
/*       */                 }  }  } 
/*       */           }
/*       */         } 
/*       */       } 
/*       */     } else {
/*  8373 */       boolean isArcheryTower = (getTemplateId() == 939);
/*       */       
/*  8375 */       if (!isEnchantedTurret() && !isArcheryTower) {
/*       */         return;
/*       */       }
/*  8378 */       if (getOwnerId() != -10L || getParentId() != -10L) {
/*       */         return;
/*       */       }
/*  8381 */       if (isEnchantedTurret() && !isPlanted()) {
/*       */         return;
/*       */       }
/*       */ 
/*       */ 
/*       */       
/*  8387 */       VolaTile ts = Zones.getTileOrNull(tileX, tileY, true);
/*  8388 */       if (ts == null) {
/*       */         return;
/*       */       }
/*       */ 
/*       */       
/*  8393 */       if ((WurmCalendar.getCurrentTime() - this.lastMaintained) < 320.0D * (1.0D - (getCurrentQualityLevel() / 200.0F))) {
/*       */         return;
/*       */       }
/*       */ 
/*       */       
/*  8398 */       this.lastMaintained = WurmCalendar.getCurrentTime();
/*  8399 */       HashSet<Creature> targets = new HashSet<>();
/*       */ 
/*       */       
/*  8402 */       float distanceModifier = getCurrentQualityLevel() / 100.0F * 5.0F;
/*  8403 */       int dist = (int)((isArcheryTower ? 5 : 3) * distanceModifier);
/*  8404 */       int x1 = Zones.safeTileX(tileX - dist);
/*  8405 */       int x2 = Zones.safeTileX(tileX + dist);
/*  8406 */       int y1 = Zones.safeTileY(tileY - dist);
/*  8407 */       int y2 = Zones.safeTileY(tileY + dist);
/*       */       
/*  8409 */       for (TilePos tPos : TilePos.areaIterator(x1, y1, x2, y2)) {
/*       */         
/*  8411 */         int x = tPos.x;
/*  8412 */         int y = tPos.y;
/*  8413 */         VolaTile t = Zones.getTileOrNull(x, y, true);
/*       */         
/*  8415 */         if (t == null)
/*       */           continue; 
/*  8417 */         if (getKingdom() != t.getKingdom())
/*       */           continue; 
/*  8419 */         if (Zones.getCurrentTurret(x, y, true) != this) {
/*       */           continue;
/*       */         }
/*  8422 */         for (Creature c : t.getCreatures()) {
/*       */           
/*  8424 */           if (!c.isUnique() && 
/*  8425 */             !c.isInvulnerable() && c
/*  8426 */             .getKingdomId() != 0 && (c
/*  8427 */             .isPlayer() || c.getTemplate().isTowerBasher())) {
/*       */ 
/*       */ 
/*       */ 
/*       */             
/*  8432 */             Village v = Villages.getVillageWithPerimeterAt(tileX, tileY, true);
/*       */             
/*  8434 */             if (!c.isFriendlyKingdom(getKingdom()) || (Servers.localServer.PVPSERVER && v != null && v
/*  8435 */               .isEnemy(c)))
/*       */             {
/*       */ 
/*       */ 
/*       */               
/*  8440 */               if (c.getCurrentTile() != null)
/*       */               {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  8447 */                 targets.add(c); } 
/*       */             }
/*       */           } 
/*       */         } 
/*       */       } 
/*  8452 */       if (!targets.isEmpty()) {
/*       */         
/*  8454 */         Creature[] crets = (Creature[])targets.toArray((Object[])new Creature[targets.size()]);
/*  8455 */         Creature c = crets[Server.rand.nextInt(crets.length)];
/*  8456 */         if (Server.rand.nextFloat() * 200.0F < getCurrentQualityLevel()) {
/*       */ 
/*       */ 
/*       */           
/*  8460 */           BlockingResult result = Blocking.getBlockerBetween(null, getPosX(), getPosY(), c.getPosX(), c.getPosY(), 
/*  8461 */               getPosZ() + getTemplate().getSizeY() * 0.85F / 100.0F - 0.5F, c
/*  8462 */               .getPositionZ() + c.getCentimetersHigh() * 0.75F / 100.0F - 0.5F, 
/*  8463 */               isOnSurface(), c.isOnSurface(), true, 4, c
/*  8464 */               .getWurmId(), getBridgeId(), c.getBridgeId(), false);
/*       */           
/*  8466 */           if (result != null) {
/*       */             
/*  8468 */             for (Blocker b : result.getBlockerArray()) {
/*       */               
/*  8470 */               if (b.getBlockPercent(c) >= 100.0F) {
/*       */                 return;
/*       */               }
/*       */             } 
/*       */             
/*  8475 */             if (result.getTotalCover() > 0.0F) {
/*       */               return;
/*       */             }
/*       */           } 
/*  8479 */           float mod = 1.0F + c.getArmourMod();
/*  8480 */           float distToCret = 1.0F - c.getPos3f().distance(getPos3f()) / 150.0F;
/*  8481 */           float enchDamMod = getCurrentQualityLevel() * distToCret;
/*  8482 */           if (isEnchantedTurret()) {
/*       */             
/*  8484 */             enchDamMod = getSpellCourierBonus();
/*  8485 */             if (enchDamMod == 0.0F) {
/*       */               
/*  8487 */               logInfo("Reverted turret at " + tileX + "," + getTileY());
/*  8488 */               setTemplateId(934);
/*       */             } 
/*       */           } 
/*  8491 */           Arrows.shootCreature(this, c, (int)(mod * 75.0F * enchDamMod));
/*       */         }
/*  8493 */         else if (isEnchantedTurret()) {
/*       */           
/*  8495 */           VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/*  8496 */           if (t != null)
/*       */           {
/*  8498 */             t.sendAnimation(c, this, "shoot", false, false);
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean poll(Item parent, int parentTemp, boolean insideStructure, boolean deeded, boolean saveLastMaintained, boolean inMagicContainer, boolean inTrashbin) {
/*  8520 */     if (this.hatching)
/*       */     {
/*  8522 */       return pollHatching();
/*       */     }
/*  8524 */     boolean decayed = false;
/*  8525 */     if (Features.Feature.TRANSPORTABLE_CREATURES.isEnabled()) {
/*       */ 
/*       */       
/*  8528 */       long delay = System.currentTimeMillis() - 900000L;
/*  8529 */       if (getTemplateId() == 1310 && parent.getTemplateId() == 1311)
/*       */       {
/*  8531 */         if (delay > parent.getData())
/*       */         {
/*  8533 */           pollCreatureCages(parent);
/*       */         }
/*       */       }
/*       */     } 
/*  8537 */     if (Features.Feature.FREE_ITEMS.isEnabled() && 
/*  8538 */       isChallengeNewbieItem() && (
/*  8539 */       isArmour() || isWeapon() || isShield()))
/*       */     {
/*  8541 */       if (this.ownerId == -10L) {
/*       */         
/*  8543 */         Items.destroyItem(getWurmId());
/*  8544 */         return true;
/*       */       } 
/*       */     }
/*  8547 */     if (isHollow() && isSealedByPlayer()) {
/*       */ 
/*       */       
/*  8550 */       if (getTemplateId() == 768) {
/*       */ 
/*       */         
/*  8553 */         pollAging(insideStructure, deeded);
/*       */         
/*  8555 */         if (Server.rand.nextInt(20) == 0)
/*  8556 */           pollFermenting(); 
/*       */       } 
/*  8558 */       return false;
/*       */     } 
/*       */     
/*  8561 */     if (getTemplateId() == 70 || getTemplateId() == 1254) {
/*       */       
/*  8563 */       modTemp(parent, parentTemp, insideStructure);
/*  8564 */       return false;
/*       */     } 
/*  8566 */     if (this.template.getDecayTime() != Long.MAX_VALUE) {
/*       */       
/*  8568 */       boolean decaytimeql = false;
/*  8569 */       if (isFood() || isHollow() || (isAlwaysPoll() && !isFlag())) {
/*       */         
/*  8571 */         if (this.template.templateId == 339)
/*       */         {
/*  8573 */           if (ArtifactBehaviour.getOrbActivation() > 0L)
/*       */           {
/*  8575 */             if (System.currentTimeMillis() - ArtifactBehaviour.getOrbActivation() > 21000L)
/*       */             {
/*  8577 */               if (WurmCalendar.currentTime - getData() < 360000L) {
/*       */                 
/*  8579 */                 ArtifactBehaviour.resetOrbActivation();
/*  8580 */                 Server.getInstance().broadCastMessage("A deadly field surges through the air from the location of the " + 
/*  8581 */                     getName() + "!", 
/*  8582 */                     getTileX(), getTileY(), isOnSurface(), 25);
/*       */                 
/*  8584 */                 ArtifactBehaviour.markOrbRecipients(null, false, getPosX(), getPosY(), getPosZ());
/*       */               } 
/*       */             }
/*       */           }
/*       */         }
/*  8589 */         if (this.template.getTemplateId() == 1175 && parent.isVehicle() && hasQueen() && WurmCalendar.currentTime - this.lastMaintained > 604800L)
/*       */         {
/*       */ 
/*       */ 
/*       */           
/*  8594 */           if (hasTwoQueens()) {
/*       */             
/*  8596 */             if (removeQueen())
/*       */             {
/*  8598 */               if (Servers.isThisATestServer()) {
/*  8599 */                 Players.getInstance().sendGmMessage(null, "System", "Debug: Removed second queen from " + getWurmId() + " as travelling.", false);
/*       */               } else {
/*  8601 */                 logger.info("Removed second queen from " + getWurmId() + " as travelling.");
/*       */               }
/*       */             
/*       */             }
/*       */           } else {
/*       */             
/*  8607 */             Item sugar = getSugar();
/*  8608 */             if (sugar != null) {
/*       */ 
/*       */               
/*  8611 */               Items.destroyItem(sugar.getWurmId());
/*       */             }
/*       */             else {
/*       */               
/*  8615 */               Item honey = getHoney();
/*  8616 */               if (honey != null) {
/*       */ 
/*       */                 
/*  8619 */                 honey.setWeight(Math.max(0, honey.getWeightGrams() - 10), true);
/*       */               }
/*  8621 */               else if (Server.rand.nextInt(3) == 0) {
/*       */                 
/*  8623 */                 if (removeQueen())
/*       */                 {
/*  8625 */                   if (Servers.isThisATestServer()) {
/*  8626 */                     Players.getInstance().sendGmMessage(null, "System", "Debug: Removed queen from " + getWurmId() + " as travelling and No Honey!", false);
/*       */                   } else {
/*  8628 */                     logger.info("Removed queen from " + getWurmId() + " as travelling and No Honey!");
/*       */                   }  } 
/*       */               } 
/*       */             } 
/*       */           } 
/*       */         }
/*  8634 */         if (isHollow()) {
/*       */           
/*  8636 */           if (deeded && isCrate() && parent.getTemplateId() == 1312) {
/*       */ 
/*       */             
/*  8639 */             setLastMaintained(WurmCalendar.currentTime);
/*       */ 
/*       */             
/*  8642 */             for (Item i : getAllItems(true)) {
/*  8643 */               if (i.isBulkItem())
/*  8644 */                 i.setLastMaintained(WurmCalendar.currentTime); 
/*       */             } 
/*  8646 */             return false;
/*       */           } 
/*  8648 */           if (deeded && getTemplateId() == 662 && parent.getTemplateId() == 1315) {
/*       */ 
/*       */             
/*  8651 */             setLastMaintained(WurmCalendar.currentTime);
/*  8652 */             return false;
/*       */           } 
/*  8654 */           if (this.items != null) {
/*       */             
/*  8656 */             Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  8657 */             for (Item item : itarr) {
/*       */ 
/*       */ 
/*       */               
/*  8661 */               if (!item.deleted)
/*       */               {
/*  8663 */                 item.poll(this, getTemperature(), insideStructure, deeded, saveLastMaintained, (inMagicContainer || 
/*  8664 */                     isMagicContainer()), false);
/*       */               }
/*       */             } 
/*       */           } 
/*       */         } 
/*  8669 */         long decayTime = 1382400L;
/*  8670 */         if (WurmCalendar.currentTime > this.creationDate + 1382400L || inTrashbin || this.template.getDecayTime() < 3600L) {
/*       */           
/*  8672 */           long decayt = this.template.getDecayTime();
/*  8673 */           if (this.template.templateId == 386) {
/*       */             
/*       */             try {
/*       */               
/*  8677 */               decayt = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDecayTime();
/*       */             }
/*  8679 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  8681 */               logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */             } 
/*       */           }
/*  8684 */           if (decayt == 28800L) {
/*       */             
/*  8686 */             if (this.damage == 0.0F) {
/*       */               
/*  8688 */               decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */             }
/*       */             else {
/*       */               
/*  8692 */               decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */             } 
/*  8694 */             decaytimeql = true;
/*       */           } 
/*  8696 */           if (inTrashbin)
/*       */           {
/*  8698 */             if (!isHollow() || !isLocked())
/*       */             {
/*       */               
/*  8701 */               decayt = Math.min(decayt, 28800L);
/*       */             }
/*       */           }
/*  8704 */           int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / decayt);
/*       */           
/*  8706 */           if (timesSinceLastUsed > 0)
/*       */           {
/*  8708 */             if (inTrashbin)
/*       */             {
/*  8710 */               if (!isHollow() || !isLocked()) {
/*       */                 
/*  8712 */                 if (getDamage() > 0.0F) {
/*       */                   
/*  8714 */                   Items.destroyItem(getWurmId());
/*  8715 */                   return true;
/*       */                 } 
/*       */ 
/*       */                 
/*  8719 */                 return setDamage(getDamage() + 0.1F);
/*       */               } 
/*       */             }
/*       */ 
/*       */             
/*  8724 */             int num = 2;
/*  8725 */             float decayMin = 0.5F;
/*  8726 */             if (isFood()) {
/*  8727 */               decayMin = 1.0F;
/*       */             }
/*  8729 */             if (!isBulk() && this.template.templateId != 74)
/*       */             {
/*  8731 */               if (!isLight()) {
/*       */                 
/*  8733 */                 if (insideStructure)
/*       */                 {
/*  8735 */                   num = 10;
/*       */                 }
/*  8737 */                 if (deeded)
/*       */                 {
/*  8739 */                   num += 4;
/*       */                 }
/*       */               } 
/*       */             }
/*       */             
/*  8744 */             boolean decay = true;
/*       */ 
/*       */             
/*  8747 */             float dm = getDecayMultiplier();
/*  8748 */             if (dm > 1.0F) {
/*       */               
/*  8750 */               this.ticksSinceLastDecay += timesSinceLastUsed;
/*  8751 */               timesSinceLastUsed = (int)(this.ticksSinceLastDecay / dm);
/*  8752 */               if (timesSinceLastUsed > 0) {
/*       */                 
/*  8754 */                 this.ticksSinceLastDecay -= (int)(timesSinceLastUsed * dm);
/*       */               }
/*       */               else {
/*       */                 
/*  8758 */                 decay = false;
/*  8759 */                 setLastMaintained(WurmCalendar.currentTime);
/*       */               } 
/*       */             } 
/*       */             
/*  8763 */             if (decay)
/*       */             {
/*  8765 */               if (decaytimeql || isBulkItem() || Server.rand.nextInt(num) == 0)
/*       */               {
/*  8767 */                 if (this.template.positiveDecay) {
/*       */                   
/*  8769 */                   if (getTemplateId() == 738)
/*       */                   {
/*  8771 */                     setQualityLevel(Math.min(100.0F, this.qualityLevel + (100.0F - this.qualityLevel) * (100.0F - this.qualityLevel) / 10000.0F));
/*       */                     
/*  8773 */                     checkGnome();
/*       */                   
/*       */                   }
/*       */                 
/*       */                 }
/*  8778 */                 else if (isMagicContainer() || !inMagicContainer || ((
/*  8779 */                   isLight() || isFireplace()) && isOnFire())) {
/*       */                   
/*  8781 */                   if ((isLight() || isFireplace()) && isOnFire())
/*       */                   {
/*  8783 */                     pollLightSource();
/*       */                   }
/*  8785 */                   if (this.template.destroyOnDecay) {
/*       */ 
/*       */ 
/*       */ 
/*       */                     
/*  8790 */                     decayed = setDamage(this.damage + (timesSinceLastUsed * 10));
/*       */ 
/*       */                   
/*       */                   }
/*  8794 */                   else if (isBulkItem() && getBulkNums() > 0) {
/*       */ 
/*       */                     
/*       */                     try {
/*  8798 */                       ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(
/*  8799 */                           getRealTemplateId());
/*  8800 */                       if (getWeightGrams() < t.getVolume())
/*       */                       {
/*  8802 */                         Items.destroyItem(getWurmId());
/*  8803 */                         decayed = true;
/*       */                       }
/*       */                       else
/*       */                       {
/*  8807 */                         float mod = 0.05F;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                         
/*  8816 */                         decayed = setWeight((int)(getWeightGrams() - (getWeightGrams() * timesSinceLastUsed) * 0.05F), true);
/*       */                       
/*       */                       }
/*       */                     
/*       */                     }
/*  8821 */                     catch (NoSuchTemplateException nst) {
/*  8822 */                       Items.destroyItem(getWurmId());
/*  8823 */                       decayed = true;
/*       */                     }
/*       */                   
/*       */                   } else {
/*       */                     
/*  8828 */                     decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  8829 */                         Math.max(decayMin, getDamageModifier(true)));
/*       */                   } 
/*       */                 } 
/*       */               }
/*       */             }
/*       */ 
/*       */ 
/*       */             
/*  8837 */             if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  8838 */               setLastMaintained(WurmCalendar.currentTime);
/*       */             }
/*       */           }
/*       */         
/*       */         } 
/*  8843 */       } else if (getTemplateId() == 1162) {
/*       */         
/*  8845 */         if (WurmCalendar.currentTime - this.lastMaintained > 604800L)
/*       */         {
/*  8847 */           advancePlanterWeek();
/*       */         }
/*       */       }
/*  8850 */       else if (WurmCalendar.currentTime - this.creationDate > 1382400L || inTrashbin || this.template.getDecayTime() < 3600L) {
/*       */         
/*  8852 */         long decayt = this.template.getDecayTime();
/*  8853 */         if (this.template.templateId == 386) {
/*       */           
/*       */           try {
/*       */             
/*  8857 */             decayt = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDecayTime();
/*       */           }
/*  8859 */           catch (NoSuchTemplateException nst) {
/*       */             
/*  8861 */             logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */           } 
/*       */         }
/*       */         
/*  8865 */         if (decayt == 28800L) {
/*       */           
/*  8867 */           if (this.damage == 0.0F) {
/*       */             
/*  8869 */             decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */           }
/*       */           else {
/*       */             
/*  8873 */             decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */           } 
/*  8875 */           decaytimeql = true;
/*       */         } 
/*       */         
/*  8878 */         if (inTrashbin)
/*       */         {
/*  8880 */           if (!isHollow() || !isLocked())
/*       */           {
/*       */             
/*  8883 */             decayt = Math.min(decayt, 28800L);
/*       */           }
/*       */         }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  8892 */         int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / decayt);
/*  8893 */         if (timesSinceLastUsed > 0)
/*       */         {
/*  8895 */           if (inTrashbin)
/*       */           {
/*  8897 */             if (!isHollow() || !isLocked()) {
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  8902 */               if (getDamage() > 0.0F) {
/*       */                 
/*  8904 */                 Items.destroyItem(getWurmId());
/*  8905 */                 return true;
/*       */               } 
/*       */ 
/*       */               
/*  8909 */               return setDamage(getDamage() + 0.1F);
/*       */             } 
/*       */           }
/*       */ 
/*       */           
/*  8914 */           int num = 2;
/*  8915 */           if (!isBulk() && this.template.templateId != 74) {
/*       */             
/*  8917 */             if (insideStructure && !this.template.positiveDecay)
/*       */             {
/*  8919 */               num = 10;
/*       */             }
/*  8921 */             if (deeded)
/*       */             {
/*  8923 */               num += 4;
/*       */             }
/*       */           } 
/*       */           
/*  8927 */           boolean decay = true;
/*       */ 
/*       */           
/*  8930 */           if (getDecayMultiplier() > 1.0F) {
/*       */             
/*  8932 */             this.ticksSinceLastDecay += timesSinceLastUsed;
/*  8933 */             timesSinceLastUsed = (int)(this.ticksSinceLastDecay / getDecayMultiplier());
/*  8934 */             if (timesSinceLastUsed > 0) {
/*       */               
/*  8936 */               this.ticksSinceLastDecay -= (int)(timesSinceLastUsed * getDecayMultiplier());
/*       */             }
/*       */             else {
/*       */               
/*  8940 */               decay = false;
/*  8941 */               setLastMaintained(WurmCalendar.currentTime);
/*       */             } 
/*       */           } 
/*       */           
/*  8945 */           if (decay)
/*       */           {
/*  8947 */             if (decaytimeql || isBulkItem() || Server.rand.nextInt(num) == 0)
/*       */             {
/*  8949 */               if (this.template.positiveDecay && !inTrashbin) {
/*       */                 
/*  8951 */                 if (getTemplateId() == 738)
/*       */                 {
/*  8953 */                   setQualityLevel(Math.min(100.0F, this.qualityLevel + (100.0F - this.qualityLevel) * (100.0F - this.qualityLevel) / 10000.0F));
/*  8954 */                   checkGnome();
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*  8959 */               else if (isMagicContainer() || !inMagicContainer) {
/*       */                 
/*  8961 */                 if (this.template.destroyOnDecay) {
/*       */ 
/*       */ 
/*       */ 
/*       */                   
/*  8966 */                   decayed = setDamage(this.damage + (timesSinceLastUsed * 10));
/*       */ 
/*       */                 
/*       */                 }
/*  8970 */                 else if (isBulkItem() && getBulkNums() > 0) {
/*       */                   
/*       */                   try
/*       */                   {
/*  8974 */                     ItemTemplate t = ItemTemplateFactory.getInstance().getTemplate(
/*  8975 */                         getRealTemplateId());
/*  8976 */                     if (getWeightGrams() < t.getVolume()) {
/*       */                       
/*  8978 */                       Items.destroyItem(getWurmId());
/*  8979 */                       decayed = true;
/*       */                     } else {
/*       */                       float mod;
/*       */ 
/*       */                       
/*  8984 */                       VolaTile tile = Zones.getOrCreateTile(getTileX(), getTileY(), true);
/*  8985 */                       if (tile.getVillage() != null) {
/*       */                         
/*  8987 */                         mod = 0.0F;
/*       */                         
/*  8989 */                         decay = false;
/*  8990 */                         setLastMaintained(WurmCalendar.currentTime);
/*       */                       }
/*       */                       else {
/*       */                         
/*  8994 */                         mod = 0.05F;
/*       */                       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                       
/*  9002 */                       decayed = setWeight((int)(getWeightGrams() - (getWeightGrams() * timesSinceLastUsed) * mod), true);
/*       */                     }
/*       */                   
/*       */                   }
/*  9006 */                   catch (NoSuchTemplateException nst)
/*       */                   {
/*  9008 */                     Items.destroyItem(getWurmId());
/*  9009 */                     decayed = true;
/*       */                   }
/*       */                 
/*       */                 } else {
/*       */                   
/*  9014 */                   decayed = setDamage(this.damage + timesSinceLastUsed * Math.max(1.0F, getDamageModifier(true)));
/*       */                 } 
/*       */               } 
/*       */             }
/*       */           }
/*       */ 
/*       */ 
/*       */           
/*  9022 */           if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  9023 */             setLastMaintained(WurmCalendar.currentTime);
/*       */           }
/*       */         }
/*       */       
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/*  9032 */       if (saveLastMaintained && this.lastMaintained - WurmCalendar.currentTime > 1209600L && 
/*  9033 */         !isRiftLoot()) {
/*  9034 */         setLastMaintained(WurmCalendar.currentTime);
/*       */       }
/*       */       
/*  9037 */       if (isHollow())
/*       */       {
/*  9039 */         if (this.items != null) {
/*       */           
/*  9041 */           Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  9042 */           for (Item item : itarr) {
/*       */ 
/*       */             
/*  9045 */             if (!item.deleted)
/*       */             {
/*  9047 */               item.poll(this, getTemperature(), insideStructure, deeded, saveLastMaintained, (inMagicContainer || 
/*  9048 */                   isMagicContainer()), false);
/*       */             }
/*       */           } 
/*       */         } 
/*       */       }
/*       */     } 
/*       */ 
/*       */     
/*  9056 */     if (Features.Feature.CHICKEN_COOPS.isEnabled())
/*       */     {
/*  9058 */       ChickenCoops.poll(this);
/*       */     }
/*       */     
/*  9061 */     if (!decayed)
/*       */     {
/*  9063 */       modTemp(parent, parentTemp, insideStructure);
/*       */     }
/*       */     
/*  9066 */     return decayed;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void pollCreatureCages(Item parent) {
/*  9076 */     parent.setDamage(parent.damage + 10.0F / parent.getCurrentQualityLevel());
/*  9077 */     parent.setData(System.currentTimeMillis());
/*       */     
/*  9079 */     if (parent.getDamage() >= 80.0F) {
/*       */       try {
/*       */         int layer;
/*       */ 
/*       */         
/*  9084 */         if (isOnSurface()) {
/*  9085 */           layer = 0;
/*       */         } else {
/*  9087 */           layer = -1;
/*  9088 */         }  parent.setName("creature cage [Empty]");
/*  9089 */         Creature getCreature = Creatures.getInstance().getCreature(getData());
/*  9090 */         Creatures cstat = Creatures.getInstance();
/*  9091 */         getCreature.getStatus().setDead(false);
/*  9092 */         cstat.removeCreature(getCreature);
/*  9093 */         cstat.addCreature(getCreature, false);
/*  9094 */         getCreature.putInWorld();
/*  9095 */         CreatureBehaviour.blinkTo(getCreature, getPosX(), getPosY(), layer, getPosZ(), getBridgeId(), getFloorLevel());
/*  9096 */         parent.setAuxData((byte)0);
/*  9097 */         Items.destroyItem(getWurmId());
/*  9098 */         CargoTransportationMethods.updateItemModel(parent);
/*       */         
/*  9100 */         DbCreatureStatus.setLoaded(0, getCreature.getWurmId());
/*       */       }
/*  9102 */       catch (NoSuchCreatureException|IOException ex) {
/*       */         
/*  9104 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public final void ageTrellis() {
/*  9111 */     if (System.currentTimeMillis() - this.lastPolled < 86400000L) {
/*       */       return;
/*       */     }
/*  9114 */     this.lastPolled = System.currentTimeMillis();
/*       */     
/*  9116 */     int age = getLeftAuxData();
/*  9117 */     if (age != 15) {
/*       */       
/*  9119 */       int chance = Server.rand.nextInt(225);
/*  9120 */       if (chance <= (16 - age) * (16 - age) || !isPlanted())
/*       */       {
/*  9122 */         if (Server.rand.nextInt(5) == 0)
/*       */         {
/*  9124 */           age++;
/*  9125 */           if (chance > 8)
/*       */           {
/*  9127 */             if (WurmCalendar.isNight()) {
/*       */               
/*  9129 */               SoundPlayer.playSound("sound.birdsong.owl.short", getTileX(), getTileY(), true, 4.0F);
/*       */             }
/*       */             else {
/*       */               
/*  9133 */               SoundPlayer.playSound("sound.ambient.day.crickets", getTileX(), getTileY(), true, 0.0F);
/*       */             } 
/*       */           }
/*  9136 */           setLeftAuxData(age);
/*       */           
/*  9138 */           updateName();
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*       */     else {
/*       */       
/*  9145 */       int chance = Server.rand.nextInt(15);
/*  9146 */       if (chance == 1) {
/*       */         
/*  9148 */         setLeftAuxData(0);
/*       */         
/*  9150 */         updateName();
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
/*       */   public final boolean poll(boolean insideStructure, boolean deeded, long seed) {
/*  9163 */     boolean decayed = false;
/*  9164 */     int templateId = -1;
/*  9165 */     templateId = getTemplateId();
/*       */     
/*  9167 */     if (Features.Feature.FREE_ITEMS.isEnabled() && 
/*  9168 */       isChallengeNewbieItem() && (
/*  9169 */       isArmour() || isWeapon() || isShield()))
/*       */     {
/*  9171 */       if (this.ownerId == -10L) {
/*  9172 */         Items.destroyItem(getWurmId());
/*  9173 */         return true;
/*       */       } 
/*       */     }
/*       */     
/*  9177 */     if (templateId == 339 && 
/*  9178 */       ArtifactBehaviour.getOrbActivation() > 0L && 
/*  9179 */       System.currentTimeMillis() - ArtifactBehaviour.getOrbActivation() > 21000L)
/*       */     {
/*       */ 
/*       */       
/*  9183 */       if (WurmCalendar.currentTime - getData() < 360000L) {
/*       */         
/*  9185 */         ArtifactBehaviour.resetOrbActivation();
/*  9186 */         Server.getInstance().broadCastMessage("A deadly field surges through the air from the location of the " + 
/*  9187 */             getName() + "!", 
/*  9188 */             getTileX(), getTileY(), isOnSurface(), 25);
/*  9189 */         ArtifactBehaviour.markOrbRecipients(null, false, getPosX(), getPosY(), getPosZ());
/*       */       } 
/*       */     }
/*       */     
/*  9193 */     if (this.hatching)
/*       */     {
/*  9195 */       return pollHatching();
/*       */     }
/*  9197 */     if (getTemplateId() == 1437)
/*       */     {
/*  9199 */       if (WurmCalendar.getCurrentTime() - this.lastMaintained > 604800L) {
/*       */         
/*  9201 */         addSnowmanItem();
/*  9202 */         setLastMaintained(WurmCalendar.getCurrentTime());
/*       */       } 
/*       */     }
/*  9205 */     if (isHollow() && isSealedByPlayer()) {
/*       */ 
/*       */       
/*  9208 */       if (templateId == 768) {
/*       */ 
/*       */         
/*  9211 */         pollAging(insideStructure, deeded);
/*       */         
/*  9213 */         if (Server.rand.nextInt(20) == 0)
/*  9214 */           pollFermenting(); 
/*       */       } 
/*  9216 */       return false;
/*       */     } 
/*  9218 */     if (this.template.getDecayTime() != Long.MAX_VALUE) {
/*       */       
/*  9220 */       if (isHollow() || isFood() || isAlwaysPoll()) {
/*       */         
/*  9222 */         long decayt = this.template.getDecayTime();
/*  9223 */         if (templateId == 386) {
/*       */           
/*       */           try {
/*       */             
/*  9227 */             decayt = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDecayTime();
/*       */           }
/*  9229 */           catch (NoSuchTemplateException nst) {
/*       */             
/*  9231 */             logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */           } 
/*       */         }
/*  9234 */         if (decayt == 28800L)
/*       */         {
/*  9236 */           if (this.damage == 0.0F) {
/*       */             
/*  9238 */             decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */           }
/*       */           else {
/*       */             
/*  9242 */             decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */           } 
/*       */         }
/*  9245 */         if (deeded && isDecoration() && templateId != 74) {
/*  9246 */           decayt *= 3L;
/*       */         }
/*  9248 */         int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / decayt);
/*       */         
/*  9250 */         if (isHollow() && !isSealedByPlayer()) {
/*       */           
/*  9252 */           boolean lastm = (seed == 1L);
/*  9253 */           if (this.items != null) {
/*       */             
/*  9255 */             Item[] pollItems1 = this.items.<Item>toArray(new Item[this.items.size()]);
/*  9256 */             int destroyed = 0;
/*  9257 */             boolean trashBin = (templateId == 670);
/*  9258 */             long lasto = 0L;
/*  9259 */             for (Item pollItem : pollItems1) {
/*       */ 
/*       */               
/*  9262 */               if (!pollItem.deleted)
/*       */               {
/*       */ 
/*       */ 
/*       */ 
/*       */                 
/*  9268 */                 if (!trashBin) {
/*  9269 */                   pollItem.poll(this, getTemperature(), insideStructure, deeded, lastm, 
/*  9270 */                       isMagicContainer(), false);
/*       */ 
/*       */                 
/*       */                 }
/*       */                 else {
/*       */ 
/*       */                   
/*  9277 */                   if (lasto > 0L && lasto != pollItem.getLastOwnerId())
/*       */                   {
/*       */ 
/*       */ 
/*       */ 
/*       */                     
/*  9283 */                     if (destroyed > 0) {
/*       */                       
/*       */                       try {
/*       */ 
/*       */                         
/*  9288 */                         Creature lLastOwner = Server.getInstance().getCreature(lasto);
/*  9289 */                         lLastOwner.achievement(160, destroyed);
/*  9290 */                         destroyed = 0;
/*       */                       }
/*  9292 */                       catch (NoSuchCreatureException|NoSuchPlayerException nsc) {
/*       */ 
/*       */                         
/*  9295 */                         Achievements.triggerAchievement(lasto, 160, destroyed);
/*  9296 */                         destroyed = 0;
/*       */                       } 
/*       */                     }
/*       */                   }
/*       */ 
/*       */                   
/*  9302 */                   lasto = pollItem.getLastOwnerId();
/*       */ 
/*       */                   
/*  9305 */                   if (pollItem.isHollow())
/*       */                   {
/*  9307 */                     for (Item it : pollItem.getItemsAsArray()) {
/*       */                       
/*  9309 */                       if (it.poll(pollItem, getTemperature(), insideStructure, deeded, lastm, 
/*  9310 */                           isMagicContainer(), true))
/*       */                       {
/*  9312 */                         destroyed++;
/*       */                       }
/*       */                     } 
/*       */                   }
/*       */ 
/*       */                   
/*  9318 */                   if (pollItem.poll(this, getTemperature(), insideStructure, deeded, lastm, 
/*  9319 */                       isMagicContainer(), true))
/*       */                   {
/*  9321 */                     destroyed++;
/*       */                   }
/*       */                   
/*  9324 */                   if (destroyed >= 100) {
/*       */                     break;
/*       */                   }
/*       */                 } 
/*       */               }
/*       */             } 
/*  9330 */             if (destroyed > 0)
/*       */             {
/*  9332 */               if (lasto > 0L) {
/*       */                 try
/*       */                 {
/*       */                   
/*  9336 */                   Creature lastoner = Server.getInstance().getCreature(lasto);
/*  9337 */                   lastoner.achievement(160, destroyed);
/*  9338 */                   destroyed = 0;
/*       */                 }
/*  9340 */                 catch (NoSuchCreatureException|NoSuchPlayerException nsc)
/*       */                 {
/*  9342 */                   Achievements.triggerAchievement(lasto, 160, destroyed);
/*  9343 */                   destroyed = 0;
/*       */                 }
/*       */               
/*       */               }
/*       */             }
/*  9348 */           } else if (isCorpse()) {
/*       */             
/*  9350 */             if (getData1() == 67 || 
/*  9351 */               getData1() == 36 || 
/*  9352 */               getData1() == 35 || 
/*  9353 */               getData1() == 34) {
/*       */               
/*  9355 */               decayed = setDamage(100.0F);
/*       */             }
/*  9357 */             else if (Servers.localServer.isChallengeServer()) {
/*       */               
/*  9359 */               if (getData1() != 1)
/*       */               {
/*  9361 */                 if (WurmCalendar.currentTime - this.creationDate > 28800L) {
/*  9362 */                   decayed = setDamage(100.0F);
/*       */                 }
/*       */               }
/*       */             } 
/*       */           } 
/*  9367 */           checkDrift();
/*       */         } 
/*  9369 */         attackEnemies(false);
/*  9370 */         if (isSpringFilled())
/*       */         {
/*  9372 */           if (isSourceSpring()) {
/*       */             
/*  9374 */             if (Server.rand.nextInt(100) == 0) {
/*       */ 
/*       */               
/*  9377 */               int volAvail = getFreeVolume();
/*       */               
/*  9379 */               if (volAvail > 0) {
/*       */                 
/*  9381 */                 Item liquid = null;
/*  9382 */                 for (Item next : getItems()) {
/*       */                   
/*  9384 */                   if (next.isLiquid())
/*       */                   {
/*  9386 */                     liquid = next;
/*       */                   }
/*       */                 } 
/*  9389 */                 if (liquid != null) {
/*       */                   
/*  9391 */                   if (liquid.getTemplateId() == 763) {
/*  9392 */                     liquid.setWeight(liquid.getWeightGrams() + 10, true);
/*       */                   }
/*       */                 } else {
/*       */                   
/*       */                   try
/*       */                   {
/*       */ 
/*       */ 
/*       */ 
/*       */                     
/*  9402 */                     Random r = new Random(getWurmId());
/*  9403 */                     Item source = ItemFactory.createItem(763, 80.0F + r.nextFloat() * 20.0F, "");
/*       */                     
/*  9405 */                     insertItem(source, true);
/*       */                   }
/*  9407 */                   catch (NoSuchTemplateException nst)
/*       */                   {
/*  9409 */                     logInfo(nst.getMessage(), (Throwable)nst);
/*       */                   }
/*  9411 */                   catch (FailedException fe)
/*       */                   {
/*  9413 */                     logInfo(fe.getMessage(), (Throwable)fe);
/*       */                   }
/*       */                 
/*       */                 } 
/*       */               } 
/*       */             } 
/*  9419 */           } else if (Zone.hasSpring(getTileX(), getTileY()) || isAutoFilled()) {
/*       */ 
/*       */             
/*  9422 */             MethodsItems.fillContainer(this, null, false);
/*       */           } 
/*       */         }
/*  9425 */         if (timesSinceLastUsed > 0 && !decayed && !hasNoDecay()) {
/*       */           
/*  9427 */           if (templateId == 74)
/*       */           {
/*       */             
/*  9430 */             if (isOnFire()) {
/*       */               
/*  9432 */               for (int i = 0; i < timesSinceLastUsed; i++) {
/*       */                 
/*  9434 */                 createDaleItems();
/*  9435 */                 decayed = setDamage(this.damage + 1.0F * getDamageModifier(true));
/*       */                 
/*  9437 */                 if (decayed) {
/*       */                   break;
/*       */                 }
/*       */               } 
/*  9441 */               if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  9442 */                 setLastMaintained(WurmCalendar.currentTime);
/*       */               }
/*  9444 */               return decayed;
/*       */             } 
/*       */           }
/*       */           
/*  9448 */           if (templateId != 37 || getTemperature() <= 200)
/*       */           {
/*  9450 */             if (WurmCalendar.currentTime > this.creationDate + 1382400L || 
/*  9451 */               isAlwaysPoll() || this.template
/*  9452 */               .getDecayTime() < 3600L || (Servers.localServer
/*  9453 */               .isChallengeOrEpicServer() && this.template.destroyOnDecay))
/*       */             {
/*  9455 */               float decayMin = 0.5F;
/*       */               
/*  9457 */               boolean decay = true;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/*  9463 */               if (deeded && getTemplateId() == 1311 && isEmpty(true))
/*       */               {
/*  9465 */                 decay = false;
/*       */               }
/*       */ 
/*       */ 
/*       */               
/*  9470 */               if (!Servers.isThisAPvpServer() && deeded && isEnchantedTurret())
/*       */               {
/*  9472 */                 decay = false;
/*       */               }
/*       */               
/*  9475 */               if (isSign() || isStreetLamp() || isFlag() || isDecoration())
/*       */               {
/*  9477 */                 if (isPlanted() || (isDecoration() && !this.template.decayOnDeed()))
/*       */                 {
/*  9479 */                   if (deeded && (!isAlwaysPoll() || isFlag() || this.template.isCooker()))
/*       */                   {
/*  9481 */                     decay = false;
/*       */                   }
/*       */                 }
/*       */               }
/*       */ 
/*       */ 
/*       */               
/*  9488 */               float dm = getDecayMultiplier();
/*  9489 */               if (dm > 1.0F) {
/*       */                 
/*  9491 */                 this.ticksSinceLastDecay += timesSinceLastUsed;
/*  9492 */                 timesSinceLastUsed = (int)(this.ticksSinceLastDecay / dm);
/*  9493 */                 if (timesSinceLastUsed > 0) {
/*       */                   
/*  9495 */                   this.ticksSinceLastDecay -= (int)(timesSinceLastUsed * dm);
/*       */                 }
/*       */                 else {
/*       */                   
/*  9499 */                   decay = false;
/*  9500 */                   setLastMaintained(WurmCalendar.currentTime);
/*       */                 } 
/*       */               } 
/*       */               
/*  9504 */               if (decay) {
/*       */                 
/*  9506 */                 if (insideStructure) {
/*       */                   
/*  9508 */                   if (isFood()) {
/*  9509 */                     decayMin = 1.0F;
/*       */                   }
/*  9511 */                   if (this.template.destroyOnDecay)
/*       */                   {
/*  9513 */                     decayed = setDamage(this.damage + (timesSinceLastUsed * 10));
/*       */                   }
/*  9515 */                   else if (Server.rand.nextInt(deeded ? 12 : 8) == 0)
/*       */                   {
/*  9517 */                     decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  9518 */                         Math.max(decayMin, getDamageModifier(true)));
/*       */                   }
/*       */                 
/*       */                 } else {
/*       */                   
/*  9523 */                   if (isFood()) {
/*  9524 */                     decayMin = 2.0F;
/*       */                   }
/*  9526 */                   if (this.template.destroyOnDecay) {
/*  9527 */                     if (Servers.localServer.isChallengeServer()) {
/*  9528 */                       decayed = setDamage(100.0F);
/*       */                     } else {
/*  9530 */                       decayed = setDamage(this.damage + (timesSinceLastUsed * 10));
/*       */                     } 
/*       */                   } else {
/*  9533 */                     decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  9534 */                         Math.max(decayMin, getDamageModifier(true)));
/*       */                   }
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*       */               else {
/*       */                 
/*  9542 */                 this.lastMaintained = (WurmCalendar.currentTime + Server.rand.nextInt(10) == 0L) ? 1L : 0L;
/*       */               } 
/*       */               
/*  9545 */               if (!decayed && this.lastMaintained != WurmCalendar.currentTime && 
/*  9546 */                 !isRiftLoot()) {
/*  9547 */                 setLastMaintained(WurmCalendar.currentTime);
/*       */               }
/*       */             }
/*       */           
/*       */           }
/*       */         } 
/*  9553 */       } else if (!hasNoDecay()) {
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9558 */         if (getTemplateId() == 1162) {
/*       */           
/*  9560 */           if (WurmCalendar.currentTime - this.lastMaintained > 604800L)
/*       */           {
/*  9562 */             advancePlanterWeek();
/*       */           }
/*       */         }
/*  9565 */         else if (WurmCalendar.currentTime > this.creationDate + 1382400L || this.template.getDecayTime() < 3600L) {
/*       */           
/*  9567 */           templateId = getTemplateId();
/*  9568 */           long decayt = this.template.getDecayTime();
/*  9569 */           if (templateId == 386) {
/*       */             
/*       */             try {
/*       */               
/*  9573 */               decayt = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDecayTime();
/*       */             }
/*  9575 */             catch (NoSuchTemplateException nst) {
/*       */               
/*  9577 */               logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */             } 
/*       */           }
/*  9580 */           float decayMin = 0.5F;
/*  9581 */           if (decayt == 28800L) {
/*       */             
/*  9583 */             if (this.damage == 0.0F) {
/*  9584 */               decayt = 1382400L + (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */             } else {
/*  9586 */               decayt = (long)(28800.0F * Math.max(1.0F, this.qualityLevel / 3.0F));
/*       */             } 
/*  9588 */             decayMin = 1.0F;
/*       */           } 
/*  9590 */           if (!isBulk()) {
/*       */             
/*  9592 */             if (deeded) {
/*  9593 */               decayt *= 2L;
/*       */             }
/*  9595 */             if (insideStructure) {
/*  9596 */               decayt *= 2L;
/*       */             }
/*  9598 */             if (isRoadMarker() && !deeded && MethodsHighways.numberOfSetBits(getAuxData()) < 2) {
/*  9599 */               decayt = Math.max(1L, decayt / 10L);
/*       */             }
/*       */           } 
/*  9602 */           int timesSinceLastUsed = (int)((WurmCalendar.currentTime - this.lastMaintained) / decayt);
/*  9603 */           if (timesSinceLastUsed > 0)
/*       */           {
/*  9605 */             boolean decay = true;
/*  9606 */             if (isRoadMarker()) {
/*       */               
/*  9608 */               if (isPlanted())
/*       */               {
/*       */                 
/*  9611 */                 if (deeded || MethodsHighways.numberOfSetBits(getAuxData()) >= 2)
/*       */                 {
/*  9613 */                   decay = false;
/*  9614 */                   setLastMaintained(WurmCalendar.currentTime);
/*       */                 }
/*       */               
/*       */               }
/*  9618 */             } else if (isSign() || isStreetLamp() || isFlag() || isDecoration()) {
/*       */               
/*  9620 */               if (isPlanted() || (isDecoration() && !this.template.decayOnDeed()))
/*       */               {
/*  9622 */                 if (deeded && (!isAlwaysPoll() || isFlag() || getTemplateId() == 1178)) {
/*  9623 */                   decay = false;
/*  9624 */                 } else if (isStreetLamp() && getBless() != null) {
/*       */                   
/*  9626 */                   if (MethodsHighways.onHighway(this)) {
/*  9627 */                     decay = false;
/*       */                   }
/*       */                 } 
/*       */               }
/*       */             } 
/*       */             
/*  9633 */             float dm = getDecayMultiplier();
/*  9634 */             if (dm > 1.0F) {
/*       */               
/*  9636 */               this.ticksSinceLastDecay += timesSinceLastUsed;
/*  9637 */               timesSinceLastUsed = (int)(this.ticksSinceLastDecay / dm);
/*  9638 */               if (timesSinceLastUsed > 0) {
/*       */                 
/*  9640 */                 this.ticksSinceLastDecay -= (int)(timesSinceLastUsed * dm);
/*       */               }
/*       */               else {
/*       */                 
/*  9644 */                 decay = false;
/*  9645 */                 setLastMaintained(WurmCalendar.currentTime);
/*       */               } 
/*       */             } 
/*  9648 */             if (decay)
/*       */             {
/*  9650 */               if (insideStructure) {
/*       */                 
/*  9652 */                 if (this.template.destroyOnDecay)
/*       */                 {
/*       */ 
/*       */ 
/*       */                   
/*  9657 */                   decayed = setDamage(this.damage + (timesSinceLastUsed * 40));
/*       */                 }
/*  9659 */                 else if (Server.rand.nextInt(deeded ? 12 : 8) == 0)
/*       */                 {
/*  9661 */                   if (this.template.positiveDecay) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */                     
/*  9684 */                     if (getTemplateId() == 738)
/*       */                     {
/*  9686 */                       setQualityLevel(Math.min(100.0F, this.qualityLevel + (100.0F - this.qualityLevel) * (100.0F - this.qualityLevel) / 10000.0F));
/*       */                       
/*  9688 */                       checkGnome();
/*       */                     }
/*       */                   
/*       */                   } else {
/*       */                     
/*  9693 */                     decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  9694 */                         Math.max(decayMin, getDamageModifier(true)));
/*       */                   }
/*       */                 
/*       */                 }
/*       */               
/*       */               }
/*  9700 */               else if (this.template.destroyOnDecay) {
/*       */ 
/*       */ 
/*       */                 
/*  9704 */                 decayed = setDamage(this.damage + (timesSinceLastUsed * 10));
/*       */               } else {
/*  9706 */                 decayed = setDamage(this.damage + timesSinceLastUsed * 
/*  9707 */                     Math.max(decayMin, getDamageModifier(true)));
/*       */               } 
/*       */             }
/*       */ 
/*       */             
/*  9712 */             if (isPlantedFlowerpot() && decayed) {
/*       */               
/*       */               try {
/*       */                 
/*  9716 */                 int revertType = -1;
/*  9717 */                 if (isPotteryFlowerPot()) {
/*  9718 */                   revertType = 813;
/*  9719 */                 } else if (isMarblePlanter()) {
/*  9720 */                   revertType = 1001;
/*       */                 } else {
/*       */                   
/*  9723 */                   revertType = -1;
/*       */                 } 
/*       */                 
/*  9726 */                 if (revertType != -1)
/*       */                 {
/*  9728 */                   Item pot = ItemFactory.createItem(revertType, getQualityLevel(), this.creator);
/*  9729 */                   pot.setLastOwnerId(getLastOwnerId());
/*  9730 */                   pot.setDescription(getDescription());
/*  9731 */                   pot.setDamage(getDamage());
/*  9732 */                   pot.setPosXYZ(getPosX(), getPosY(), getPosZ());
/*  9733 */                   VolaTile tile = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/*  9734 */                   if (tile != null)
/*       */                   {
/*  9736 */                     tile.addItem(pot, false, false);
/*       */                   }
/*       */                 }
/*       */               
/*  9740 */               } catch (NoSuchTemplateException|FailedException e) {
/*       */                 
/*  9742 */                 logWarn(e.getMessage(), (Throwable)e);
/*       */               } 
/*       */             }
/*       */             
/*  9746 */             if (!decayed && this.lastMaintained != WurmCalendar.currentTime) {
/*  9747 */               setLastMaintained(WurmCalendar.currentTime);
/*       */             }
/*       */           }
/*       */         
/*       */         } 
/*       */       } 
/*  9753 */     } else if (this.template.hugeAltar) {
/*       */       
/*  9755 */       if (isHollow()) {
/*       */         
/*  9757 */         boolean lastm = true;
/*       */         
/*  9759 */         if (this.items != null) {
/*       */           
/*  9761 */           Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  9762 */           for (Item it : itarr) {
/*       */             
/*  9764 */             if (!it.deleted) {
/*  9765 */               it.poll(this, getTemperature(), insideStructure, deeded, true, true, false);
/*       */             }
/*       */           } 
/*       */         } 
/*       */       } 
/*  9770 */       pollHugeAltar();
/*       */     }
/*  9772 */     else if (templateId == 521) {
/*       */       
/*  9774 */       if (!isOnSurface()) {
/*       */ 
/*       */         
/*  9777 */         setDamage(getDamage() + 0.1F);
/*  9778 */         logInfo(getName() + " at " + getTilePos() + " on cave tile. Dealing damage.");
/*       */       }
/*       */       else {
/*       */         
/*  9782 */         VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/*  9783 */         if (t != null) {
/*  9784 */           if (t.isTransition) {
/*  9785 */             setDamage(getDamage() + 0.1F);
/*  9786 */             logInfo(getName() + " at " + getTilePos() + " on surface transition tile. Dealing damage.");
/*       */           } 
/*       */         } else {
/*       */           
/*  9790 */           logWarn(getName() + " at " + getTilePos() + " no tile on surface. Zone no. is " + 
/*  9791 */               getZoneId());
/*       */         }
/*       */       
/*       */       } 
/*  9795 */     } else if (templateId == 236) {
/*       */       
/*  9797 */       checkItemSpawn();
/*       */     } 
/*       */     
/*  9800 */     if (templateId != 74)
/*       */     {
/*  9802 */       coolOutSideItem(isAlwaysPoll(), insideStructure);
/*       */     }
/*       */     
/*  9805 */     if (templateId == 445)
/*       */     {
/*  9807 */       if (getData() > 0L) {
/*       */         
/*       */         try {
/*       */           
/*  9811 */           Item contained = Items.getItem(getData());
/*  9812 */           if (contained.poll(insideStructure, deeded, seed))
/*       */           {
/*  9814 */             setData(0L);
/*       */           }
/*       */         }
/*  9817 */         catch (NoSuchItemException noSuchItemException) {}
/*       */       }
/*       */     }
/*       */ 
/*       */ 
/*       */     
/*  9823 */     if (spawnsTrees())
/*       */     {
/*  9825 */       for (int n = 0; n < 10; n++) {
/*       */ 
/*       */         
/*  9828 */         int x = Zones.safeTileX(getTileX() - 18 + Server.rand.nextInt(36));
/*  9829 */         int y = Zones.safeTileY(getTileY() - 18 + Server.rand.nextInt(36));
/*  9830 */         boolean spawn = true;
/*  9831 */         for (int xx = x - 1; xx <= x + 1; xx++) {
/*       */           
/*  9833 */           for (int yy = y - 1; yy <= y + 1; yy++) {
/*       */             
/*  9835 */             int meshTile = Server.surfaceMesh.getTile(Zones.safeTileX(xx), Zones.safeTileY(yy));
/*  9836 */             if (Tiles.getTile(Tiles.decodeType(meshTile)).isNormalTree()) {
/*       */ 
/*       */               
/*  9839 */               spawn = false;
/*       */               break;
/*       */             } 
/*       */           } 
/*       */         } 
/*  9844 */         VolaTile t = Zones.getTileOrNull(x, y, isOnSurface());
/*  9845 */         if (t != null) {
/*       */           
/*  9847 */           Item[] its = t.getItems();
/*  9848 */           for (Item i : its) {
/*       */             
/*  9850 */             if (i.isDestroyedOnDecay()) {
/*  9851 */               Items.destroyItem(i.getWurmId());
/*       */             }
/*       */           } 
/*       */         } 
/*  9855 */         if (spawn) {
/*       */           
/*  9857 */           int tile = Server.surfaceMesh.getTile(x, y);
/*  9858 */           if (Tiles.decodeHeight(tile) > 0.3D)
/*       */           {
/*  9860 */             if (Tiles.canSpawnTree(Tiles.decodeType(tile))) {
/*       */ 
/*       */               
/*  9863 */               int age = 8 + Server.rand.nextInt(6);
/*  9864 */               byte tree = (byte)Server.rand.nextInt(9);
/*  9865 */               if (TreeData.TreeType.fromInt(tree).isFruitTree()) {
/*  9866 */                 tree = (byte)(tree + 4);
/*       */               }
/*  9868 */               byte newData = (byte)(age << 4);
/*  9869 */               byte newType = TreeData.TreeType.fromInt(tree).asNormalTree();
/*  9870 */               newData = (byte)(newData + 1 & 0xFF);
/*       */               
/*  9872 */               Server.setSurfaceTile(x, y, 
/*  9873 */                   Tiles.decodeHeight(tile), newType, newData);
/*  9874 */               Players.getInstance().sendChangedTile(x, y, true, false);
/*       */             } 
/*       */           }
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/*  9881 */     if (killsTrees()) {
/*       */       
/*  9883 */       TilePos currPos = getTilePos();
/*  9884 */       TilePos minPos = Zones.safeTile(currPos.add(-10, -10, null));
/*  9885 */       TilePos maxPos = Zones.safeTile(currPos.add(10, 10, null));
/*  9886 */       for (TilePos tPos : TilePos.areaIterator(minPos, maxPos)) {
/*       */         
/*  9888 */         int tile = Server.surfaceMesh.getTile(tPos);
/*       */         
/*  9890 */         byte tttype = Tiles.decodeType(tile);
/*  9891 */         Tiles.Tile theTile = Tiles.getTile(tttype);
/*  9892 */         if (theTile.isNormalTree() || tttype == Tiles.Tile.TILE_GRASS.id || tttype == Tiles.Tile.TILE_DIRT.id || tttype == Tiles.Tile.TILE_KELP.id || tttype == Tiles.Tile.TILE_REED.id) {
/*       */ 
/*       */           
/*  9895 */           Server.setSurfaceTile(tPos, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/*  9896 */           Players.getInstance().sendChangedTile(tPos, true, false);
/*       */           
/*       */           break;
/*       */         } 
/*       */       } 
/*       */     } 
/*  9902 */     if (isWind() && !insideStructure && 
/*       */       
/*  9904 */       getParentId() == -10L && 
/*  9905 */       isOnSurface()) {
/*       */       
/*  9907 */       float rot = Creature.normalizeAngle(Server.getWeather().getWindRotation() + 180.0F);
/*  9908 */       if (getRotation() != ladderRotate(rot))
/*       */       {
/*  9910 */         setRotation(rot);
/*       */       }
/*       */     } 
/*       */     
/*  9914 */     if (!decayed && !insideStructure && isFlickering() && isOnFire())
/*       */     {
/*  9916 */       if (Server.rand.nextFloat() * 10.0F < Server.getWeather().getRain())
/*       */       {
/*  9918 */         setTemperature((short)200);
/*       */       }
/*       */     }
/*  9921 */     if (getTemplateId() == 1178)
/*       */     {
/*  9923 */       if (Server.rand.nextInt(20) == 0) {
/*  9924 */         pollDistilling();
/*       */       }
/*       */     }
/*  9927 */     if (!decayed && isTrellis())
/*  9928 */       ageTrellis(); 
/*  9929 */     return decayed;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   void pollAging(boolean insideStructure, boolean deeded) {
/*  9940 */     if (this.items != null && this.items.size() == 1) {
/*       */       
/*  9942 */       int num = 2;
/*       */       
/*  9944 */       if (!isOnSurface())
/*  9945 */         num += 7; 
/*  9946 */       if (insideStructure)
/*  9947 */         num += 4; 
/*  9948 */       if (deeded) {
/*  9949 */         num += 2;
/*       */       }
/*  9951 */       Item[] itarr = this.items.<Item>toArray(new Item[this.items.size()]);
/*  9952 */       Item item = itarr[0];
/*       */       
/*  9954 */       if (!item.deleted && (item.getTemplate()).positiveDecay && item.isLiquid() && item.getAuxData() == 0) {
/*       */ 
/*       */         
/*  9957 */         int timesSinceLastUsed = (int)((WurmCalendar.currentTime - item.getLastMaintained()) / item.getTemplate().getDecayTime());
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*  9965 */         if (timesSinceLastUsed > 0 && Server.rand.nextInt(16 - num) == 0) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/*  9976 */           float bonus = getMaterialAgingModifier();
/*  9977 */           if (Servers.isThisATestServer()) {
/*  9978 */             logger.info("Positive Decay added to" + item.getName() + " (" + item.getWurmId() + ") in " + 
/*  9979 */                 getName() + " (" + this.id + ")");
/*       */           }
/*  9981 */           item.setQualityLevel(Math.min(100.0F, item.getQualityLevel() + (100.0F - item
/*  9982 */                 .getQualityLevel()) * (100.0F - item.getQualityLevel()) / 10000.0F * bonus));
/*  9983 */           item.setLastMaintained(WurmCalendar.currentTime);
/*       */         } 
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   void pollFermenting() {
/*  9993 */     if ((getItemsAsArray()).length != 2) {
/*       */       return;
/*       */     }
/*  9996 */     long lastMaintained = 0L;
/*  9997 */     Item liquid = null;
/*  9998 */     Item scrap = null;
/*  9999 */     long lastowner = -10L;
/* 10000 */     for (Item item : getItemsAsArray()) {
/*       */       
/* 10002 */       if (lastMaintained < item.getLastMaintained())
/* 10003 */         lastMaintained = item.getLastMaintained(); 
/* 10004 */       if (item.isLiquid()) {
/* 10005 */         liquid = item;
/*       */       } else {
/*       */         
/* 10008 */         scrap = item;
/*       */         
/* 10010 */         lastowner = scrap.lastOwner;
/*       */       } 
/*       */     } 
/* 10013 */     if (lastMaintained < WurmCalendar.currentTime - (Servers.isThisATestServer() ? 86400L : 2419200L)) {
/*       */ 
/*       */       
/* 10016 */       Recipe recipe = Recipes.getRecipeFor(lastowner, (byte)0, (Item)null, this, true, true);
/* 10017 */       if (recipe == null) {
/*       */         return;
/*       */       }
/* 10020 */       Skill primSkill = null;
/* 10021 */       Creature lastown = null;
/* 10022 */       float alc = 0.0F;
/* 10023 */       boolean chefMade = false;
/* 10024 */       double bonus = 0.0D;
/* 10025 */       boolean showOwner = false;
/*       */ 
/*       */       
/*       */       try {
/* 10029 */         lastown = Server.getInstance().getCreature(lastowner);
/* 10030 */         bonus = lastown.getVillageSkillModifier();
/* 10031 */         alc = ((Player)lastown).getAlcohol();
/* 10032 */         Skills skills = lastown.getSkills();
/* 10033 */         primSkill = skills.getSkillOrLearn(recipe.getSkillId());
/* 10034 */         if (lastown.isRoyalChef())
/* 10035 */           chefMade = true; 
/* 10036 */         showOwner = (primSkill.getKnowledge(0.0D) > 70.0D);
/*       */       }
/* 10038 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */       
/*       */       }
/* 10042 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */ 
/*       */ 
/*       */       
/* 10046 */       int diff = recipe.getDifficulty(this);
/* 10047 */       float power = 10.0F;
/* 10048 */       if (primSkill != null) {
/* 10049 */         power = (float)primSkill.skillCheck((diff + alc), null, bonus, false, (recipe.getIngredientCount() + diff));
/*       */       }
/*       */       
/* 10052 */       double ql = Math.min(99.0F, Math.max(1.0F, liquid.getCurrentQualityLevel() + power / 10.0F));
/* 10053 */       if (chefMade) {
/* 10054 */         ql = Math.max(30.0D, ql);
/*       */       }
/* 10056 */       if (primSkill != null) {
/* 10057 */         ql = Math.max(1.0D, Math.min(primSkill.getKnowledge(0.0D), ql));
/*       */       } else {
/*       */         
/* 10060 */         ql = Math.max(1.0D, Math.min(Math.max(scrap.getAuxData(), 20), ql));
/* 10061 */       }  if (ql > 70.0D)
/*       */       {
/* 10063 */         ql -= Math.min(20.0F, (100.0F - liquid.getCurrentQualityLevel()) / 5.0F);
/*       */       }
/* 10065 */       if (getRarity() > 0 || liquid.getRarity() > 0 || this.rarity > 0)
/*       */       {
/* 10067 */         ql = GeneralUtilities.calcRareQuality(ql, getRarity(), liquid.getRarity(), this.rarity);
/*       */       }
/*       */       
/* 10070 */       byte material = recipe.getResultMaterial(this);
/*       */       
/*       */       try {
/* 10073 */         ItemTemplate it = recipe.getResultTemplate(this);
/* 10074 */         String owner = showOwner ? PlayerInfoFactory.getPlayerName(lastowner) : null;
/* 10075 */         Item newItem = ItemFactory.createItem(it.getTemplateId(), (float)ql, material, (byte)0, owner);
/* 10076 */         newItem.setWeight(liquid.getWeightGrams(), true);
/* 10077 */         newItem.setLastOwnerId(lastowner);
/*       */         
/* 10079 */         if (RecipesByPlayer.saveRecipe(lastown, recipe, lastowner, null, this) && lastown != null) {
/* 10080 */           lastown.getCommunicator().sendServerMessage("Recipe \"" + recipe.getName() + "\" added to your cookbook.", 216, 165, 32, (byte)2);
/*       */         }
/*       */         
/* 10083 */         newItem.calculateAndSaveNutrition(null, this, recipe);
/*       */         
/* 10085 */         newItem.setName(recipe.getResultName(this));
/*       */         
/* 10087 */         ItemTemplate rit = recipe.getResultRealTemplate(this);
/* 10088 */         if (rit != null)
/* 10089 */           newItem.setRealTemplate(rit.getTemplateId()); 
/* 10090 */         if (recipe.hasResultState())
/* 10091 */           newItem.setAuxData(recipe.getResultState()); 
/* 10092 */         if (lastown != null) {
/* 10093 */           recipe.addAchievements(lastown, newItem);
/*       */         } else {
/* 10095 */           recipe.addAchievementsOffline(lastowner, newItem);
/*       */         } 
/* 10097 */         for (Item item : getItemsAsArray())
/*       */         {
/* 10099 */           Items.destroyItem(item.getWurmId());
/*       */         }
/* 10101 */         insertItem(newItem);
/*       */         
/* 10103 */         updateName();
/*       */       }
/* 10105 */       catch (FailedException e) {
/*       */ 
/*       */         
/* 10108 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/* 10110 */       catch (NoSuchTemplateException e) {
/*       */ 
/*       */         
/* 10113 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   void pollDistilling() {
/* 10121 */     if (getTemperature() < 1500) {
/*       */       return;
/*       */     }
/*       */     
/* 10125 */     Item boiler = null;
/* 10126 */     Item condenser = null;
/* 10127 */     for (Item item : getItemsAsArray()) {
/*       */       
/* 10129 */       if (item.getTemplateId() == 1284) {
/* 10130 */         boiler = item;
/* 10131 */       } else if (item.getTemplateId() == 1285) {
/* 10132 */         condenser = item;
/*       */       } 
/* 10134 */     }  if (boiler == null || condenser == null) {
/*       */ 
/*       */       
/* 10137 */       logger.warning("Still broken " + getWurmId());
/*       */       
/*       */       return;
/*       */     } 
/* 10141 */     if (boiler.getTemperature() < 1500) {
/*       */       return;
/*       */     }
/* 10144 */     Item[] boilerItems = boiler.getItemsAsArray();
/* 10145 */     if (boilerItems.length != 1)
/*       */       return; 
/* 10147 */     Item undistilled = boilerItems[0];
/* 10148 */     long lastowner = undistilled.lastOwner;
/*       */     
/* 10150 */     if (undistilled.getTemperature() < 1500) {
/*       */       return;
/*       */     }
/* 10153 */     if (condenser.getFreeVolume() <= 0) {
/*       */       return;
/*       */     }
/* 10156 */     if (undistilled.lastMaintained > WurmCalendar.currentTime - 600L) {
/*       */       return;
/*       */     }
/* 10159 */     Recipe recipe = Recipes.getRecipeFor(lastowner, (byte)0, (Item)null, boiler, true, true);
/* 10160 */     if (recipe == null) {
/*       */       return;
/*       */     }
/* 10163 */     Skill primSkill = null;
/* 10164 */     Creature lastown = null;
/* 10165 */     float alc = 0.0F;
/* 10166 */     boolean chefMade = false;
/* 10167 */     double bonus = 0.0D;
/* 10168 */     boolean showOwner = false;
/*       */ 
/*       */     
/*       */     try {
/* 10172 */       lastown = Server.getInstance().getCreature(lastowner);
/* 10173 */       bonus = lastown.getVillageSkillModifier();
/* 10174 */       alc = ((Player)lastown).getAlcohol();
/* 10175 */       Skills skills = lastown.getSkills();
/* 10176 */       primSkill = skills.getSkillOrLearn(recipe.getSkillId());
/* 10177 */       if (lastown.isRoyalChef())
/* 10178 */         chefMade = true; 
/* 10179 */       showOwner = (primSkill.getKnowledge(0.0D) > 70.0D);
/*       */     }
/* 10181 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */     
/*       */     }
/* 10185 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*       */ 
/*       */ 
/*       */     
/* 10189 */     int diff = recipe.getDifficulty(boiler);
/* 10190 */     float power = 0.0F;
/*       */     
/* 10192 */     if (primSkill != null) {
/* 10193 */       power = (float)primSkill.skillCheck((diff + alc), null, bonus, (Server.rand.nextInt(60) != 0), (recipe.getIngredientCount() + diff));
/*       */     }
/*       */     
/* 10196 */     double ql = Math.min(99.0F, Math.max(1.0F, undistilled.getCurrentQualityLevel() + power / 10.0F));
/* 10197 */     if (chefMade)
/* 10198 */       ql = Math.max(30.0D, ql); 
/* 10199 */     if (primSkill != null) {
/* 10200 */       ql = Math.max(1.0D, Math.min(primSkill.getKnowledge(0.0D), ql));
/*       */     } else {
/* 10202 */       ql = Math.max(1.0D, Math.min(Math.max(undistilled.getCurrentQualityLevel(), 20.0F), ql));
/* 10203 */     }  if (ql > 70.0D)
/*       */     {
/* 10205 */       ql -= Math.min(20.0F, (100.0F - undistilled.getCurrentQualityLevel()) / 5.0F);
/*       */     }
/* 10207 */     if (getRarity() > 0 || undistilled.getRarity() > 0 || this.rarity > 0)
/*       */     {
/* 10209 */       ql = GeneralUtilities.calcRareQuality(ql, getRarity(), undistilled.getRarity(), this.rarity);
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 10215 */     undistilled.setLastMaintained(WurmCalendar.currentTime);
/* 10216 */     int oldWeight = undistilled.getWeightGrams();
/* 10217 */     int usedWeight = Math.min(10, oldWeight);
/* 10218 */     Item distilled = null;
/* 10219 */     Item[] condenserItems = condenser.getItemsAsArray();
/* 10220 */     if (condenserItems.length == 0) {
/*       */ 
/*       */       
/* 10223 */       byte material = recipe.getResultMaterial(boiler);
/*       */       
/*       */       try {
/* 10226 */         ItemTemplate it = recipe.getResultTemplate(boiler);
/* 10227 */         String owner = showOwner ? PlayerInfoFactory.getPlayerName(lastowner) : null;
/* 10228 */         distilled = ItemFactory.createItem(it.getTemplateId(), (float)ql, material, (byte)0, owner);
/* 10229 */         distilled.setLastOwnerId(lastowner);
/* 10230 */         distilled.setWeight(usedWeight, true);
/* 10231 */         distilled.setTemperature((short)1990);
/*       */ 
/*       */         
/* 10234 */         if (RecipesByPlayer.saveRecipe(lastown, recipe, lastowner, null, boiler) && lastown != null) {
/* 10235 */           lastown.getCommunicator().sendServerMessage("Recipe \"" + recipe.getName() + "\" added to your cookbook.", 216, 165, 32, (byte)2);
/*       */         }
/*       */         
/* 10238 */         distilled.calculateAndSaveNutrition(null, undistilled, recipe);
/*       */         
/* 10240 */         distilled.setName(recipe.getResultName(boiler));
/* 10241 */         if (lastown != null) {
/* 10242 */           recipe.addAchievements(lastown, distilled);
/*       */         } else {
/* 10244 */           recipe.addAchievementsOffline(lastowner, distilled);
/*       */         } 
/* 10246 */         ItemTemplate rit = recipe.getResultRealTemplate(boiler);
/* 10247 */         if (rit != null)
/* 10248 */           distilled.setRealTemplate(rit.getTemplateId()); 
/* 10249 */         if (recipe.hasResultState()) {
/* 10250 */           distilled.setAuxData(recipe.getResultState());
/*       */         }
/* 10252 */         undistilled.setWeight(oldWeight - usedWeight, true);
/* 10253 */         condenser.insertItem(distilled);
/*       */       }
/* 10255 */       catch (FailedException e) {
/*       */ 
/*       */         
/* 10258 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/* 10260 */       catch (NoSuchTemplateException e) {
/*       */ 
/*       */         
/* 10263 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/* 10268 */       distilled = condenserItems[0];
/*       */       
/* 10270 */       int newUndistilledWeight = Math.max(oldWeight - usedWeight, 0);
/*       */       
/* 10272 */       float newQl = (distilled.getCurrentQualityLevel() * distilled.getWeightGrams() + (float)ql * usedWeight) / (distilled.getWeightGrams() + usedWeight);
/* 10273 */       int newTemp = 1990;
/*       */       
/* 10275 */       undistilled.setWeight(newUndistilledWeight, true);
/* 10276 */       distilled.setQualityLevel(newQl);
/* 10277 */       distilled.setTemperature((short)1990);
/* 10278 */       distilled.setWeight(distilled.getWeightGrams() + usedWeight, true);
/*       */     } 
/* 10280 */     distilled.setLastMaintained(WurmCalendar.currentTime);
/*       */   }
/*       */ 
/*       */   
/*       */   public void advancePlanterWeek() {
/* 10285 */     Item parent = getParentOrNull();
/* 10286 */     Item topParent = getTopParentOrNull();
/* 10287 */     if (parent != null && parent.getTemplateId() != 1110) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/* 10292 */     if (topParent != null && topParent != this && topParent != parent) {
/*       */       return;
/*       */     }
/*       */ 
/*       */ 
/*       */     
/* 10298 */     int age = getAuxData() & Byte.MAX_VALUE;
/* 10299 */     int newAge = age + 4;
/* 10300 */     if (newAge >= 100) {
/*       */       
/*       */       try
/*       */       {
/*       */         
/* 10305 */         Item newPot = ItemFactory.createItem(1161, getCurrentQualityLevel(), getRarity(), this.creator);
/* 10306 */         newPot.setLastOwnerId(getLastOwnerId());
/* 10307 */         newPot.setDescription(getDescription());
/* 10308 */         if (parent == null) {
/*       */ 
/*       */           
/* 10311 */           newPot.setPosXYZRotation(getPosX(), getPosY(), getPosZ(), getRotation());
/* 10312 */           newPot.setIsPlanted(isPlanted());
/* 10313 */           VolaTile tile = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 10314 */           if (tile != null)
/*       */           {
/* 10316 */             tile.addItem(newPot, false, false);
/*       */           
/*       */           }
/*       */         }
/*       */         else {
/*       */           
/* 10322 */           parent.insertItem(newPot, true);
/*       */         } 
/* 10324 */         Items.destroyItem(getWurmId());
/*       */       }
/* 10326 */       catch (FailedException e)
/*       */       {
/*       */         
/* 10329 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/* 10331 */       catch (NoSuchTemplateException e)
/*       */       {
/*       */         
/* 10334 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */       }
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 10340 */       if (newAge > 5 && newAge < 95)
/* 10341 */         newAge += 128; 
/* 10342 */       setAuxData((byte)newAge);
/* 10343 */       setLastMaintained(WurmCalendar.currentTime);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private void checkGnome() {
/* 10349 */     boolean found = false;
/* 10350 */     if (getItems() != null)
/*       */     {
/* 10352 */       for (Item i : getItemsAsArray()) {
/*       */         
/* 10354 */         if (i.getTemplateId() == 373) {
/*       */           
/* 10356 */           found = true;
/* 10357 */           setAuxData((byte)Math.min(100, getAuxData() + i.getWeightGrams() / 20));
/* 10358 */           Items.destroyItem(i.getWurmId());
/*       */         } 
/*       */       } 
/*       */     }
/*       */     
/* 10363 */     if (found) {
/*       */       return;
/*       */     }
/*       */     
/* 10367 */     setAuxData((byte)Math.max(0, getAuxData() - 10));
/* 10368 */     if (this.qualityLevel > 30.0F && getAuxData() == 0)
/*       */     {
/* 10370 */       doGnomeTrick();
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
/*       */   private float getParentDecayMultiplier(boolean includeMajor, boolean includeRune) {
/* 10384 */     float toReturn = 1.0F;
/*       */     
/* 10386 */     if (includeMajor && (
/* 10387 */       getTemplateId() == 1020 || getTemplateId() == 1022)) {
/*       */       
/* 10389 */       toReturn = (float)(toReturn * 1.5D);
/* 10390 */       includeMajor = false;
/*       */     } 
/*       */     
/* 10393 */     if (includeRune && 
/* 10394 */       getSpellEffects() != null) {
/*       */       
/* 10396 */       toReturn *= getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_INTERNAL_DECAY);
/* 10397 */       includeRune = false;
/*       */     } 
/*       */     
/* 10400 */     Item parent = getParentOrNull();
/* 10401 */     if (parent != null && (includeMajor || includeRune)) {
/* 10402 */       toReturn *= parent.getParentDecayMultiplier(includeMajor, includeRune);
/*       */     }
/* 10404 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getDecayMultiplier() {
/* 10412 */     float mult = getMaterialDecayTimeModifier();
/* 10413 */     boolean isWrapped = (isWrapped() && (canBePapyrusWrapped() || canBeClothWrapped()));
/* 10414 */     float wrapMod = isWrapped ? 5.0F : (isSalted() ? 1.5F : 1.0F);
/* 10415 */     mult *= wrapMod;
/*       */     
/* 10417 */     Item parent = getParentOrNull();
/* 10418 */     if (parent != null) {
/*       */       
/* 10420 */       Item topContainer = parent.getParentOuterItemOrNull();
/* 10421 */       if (isInLunchbox()) {
/*       */         
/* 10423 */         Item lunchbox = topContainer;
/* 10424 */         if (lunchbox != null)
/*       */         {
/* 10426 */           if (lunchbox.getTemplateId() == 1297) {
/* 10427 */             mult = (float)(mult * 1.5D);
/* 10428 */           } else if (lunchbox.getTemplateId() == 1296) {
/* 10429 */             mult = (float)(mult * 1.25D);
/*       */           } 
/*       */         }
/* 10432 */       } else if (isLiquid() && topContainer != null && topContainer.getTemplateId() == 1117) {
/*       */ 
/*       */         
/* 10435 */         mult = (float)(mult * 2.0D);
/*       */       } 
/* 10437 */       mult *= parent.getParentDecayMultiplier(true, true);
/*       */     } 
/*       */ 
/*       */     
/* 10441 */     Item topParent = getTopParentOrNull();
/* 10442 */     if (topParent != null && topParent.getTemplateId() == 1277)
/*       */     {
/* 10444 */       for (Item container : topParent.getItemsAsArray()) {
/*       */         
/* 10446 */         if (container.getTemplateId() == 1278) {
/*       */ 
/*       */ 
/*       */           
/* 10450 */           if ((container.getItemsAsArray()).length > 0)
/* 10451 */             mult *= (1 + (container.getItemsAsArray()).length / 5); 
/*       */           break;
/*       */         } 
/*       */       } 
/*       */     }
/* 10456 */     return mult;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isItemSpawn() {
/* 10461 */     return this.template.isItemSpawn();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void spawnItemSpawn(int[] templateTypes, float startQl, float qlValRange, int maxNums, boolean onGround) {
/* 10467 */     if (this.ownerId != -10L) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/* 10472 */     Item[] currentItems = getAllItems(true);
/* 10473 */     boolean[] hasTypes = new boolean[templateTypes.length];
/*       */     
/* 10475 */     for (Item item : currentItems) {
/*       */       
/* 10477 */       for (int i = 0; i < templateTypes.length; i++) {
/*       */         
/* 10479 */         if (templateTypes[i] == item.getTemplateId()) {
/*       */           
/* 10481 */           hasTypes[i] = true;
/*       */           
/*       */           break;
/*       */         } 
/*       */       } 
/*       */     } 
/* 10487 */     for (int x = 0; x < hasTypes.length; x++) {
/*       */       
/* 10489 */       if (!hasTypes[x])
/*       */       {
/*       */ 
/*       */         
/* 10493 */         for (int nums = 0; nums < maxNums; nums++) {
/*       */ 
/*       */           
/*       */           try {
/* 10497 */             int templateType = templateTypes[x];
/* 10498 */             if (onGround) {
/*       */               
/* 10500 */               ItemFactory.createItem(templateType, startQl + Server.rand.nextFloat() * qlValRange, 
/* 10501 */                   getPosX() + 0.3F, getPosY() + 0.3F, 65.0F, 
/* 10502 */                   isOnSurface(), (byte)0, -10L, "");
/*       */             }
/*       */             else {
/*       */               
/* 10506 */               boolean isBoneCollar = (templateType == 867);
/*       */ 
/*       */               
/* 10509 */               byte rrarity = (Server.rand.nextInt(100) == 0 || isBoneCollar) ? 1 : 0;
/*       */               
/* 10511 */               if (rrarity > 0)
/*       */               {
/*       */ 
/*       */                 
/* 10515 */                 rrarity = (Server.rand.nextInt(100) == 0 && isBoneCollar) ? 2 : 1;
/*       */               }
/* 10517 */               if (rrarity > 1)
/*       */               {
/*       */ 
/*       */                 
/* 10521 */                 rrarity = (Server.rand.nextInt(100) == 0 && isBoneCollar) ? 3 : 2;
/*       */               }
/*       */               
/* 10524 */               float newql = startQl + Server.rand.nextFloat() * qlValRange;
/* 10525 */               Item toInsert = ItemFactory.createItem(templateType, newql, rrarity, "");
/*       */               
/* 10527 */               if (templateType == 465)
/*       */               {
/*       */                 
/* 10530 */                 toInsert.setData1(CreatureTemplateCreator.getRandomDragonOrDrakeId());
/*       */               }
/* 10532 */               if (templateType == 371)
/*       */               {
/*       */                 
/* 10535 */                 toInsert.setData1(CreatureTemplateCreator.getRandomDrakeId());
/*       */               }
/* 10537 */               insertItem(toInsert, true);
/*       */             } 
/* 10539 */           } catch (FailedException|NoSuchTemplateException e) {
/*       */             
/* 10541 */             logWarn(e.getMessage(), (Throwable)e);
/*       */           } 
/*       */         } 
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   public final void fillTreasureChest() {
/* 10549 */     if (getAuxData() >= 0) {
/*       */       
/* 10551 */       int[] templateTypes = { 46, 72, 144, 316 };
/*       */       
/* 10553 */       spawnItemSpawn(templateTypes, 60.0F, 20.0F, 3, false);
/*       */     } 
/* 10555 */     if (getAuxData() >= 1) {
/*       */       
/* 10557 */       int[] templateTypes2 = { 204, 463 };
/*       */       
/* 10559 */       spawnItemSpawn(templateTypes2, 60.0F, 20.0F, 1, false);
/*       */     } 
/* 10561 */     if (getAuxData() >= 2) {
/*       */       
/* 10563 */       int[] templateTypes3 = { 765, 693, 697, 456 };
/*       */       
/* 10565 */       spawnItemSpawn(templateTypes3, 80.0F, 20.0F, 1, false);
/*       */     } 
/* 10567 */     if (getAuxData() >= 3) {
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 10572 */       int[] templateTypes4 = { 374 + Server.rand.nextInt(10), (Server.rand.nextInt(10) == 0) ? 525 : 837 };
/*       */       
/* 10574 */       spawnItemSpawn(templateTypes4, 80.0F, 20.0F, 1, false);
/*       */     } 
/* 10576 */     if (getAuxData() >= 4) {
/*       */ 
/*       */ 
/*       */       
/* 10580 */       int[] templateTypes5 = { 374 + Server.rand.nextInt(10), 610 + Server.rand.nextInt(10) };
/* 10581 */       spawnItemSpawn(templateTypes5, 50.0F, 50.0F, 1, false);
/*       */     } 
/* 10583 */     if (getAuxData() >= 5) {
/*       */       
/* 10585 */       int[] templateTypes6 = { 349, 456, 694 };
/*       */       
/* 10587 */       spawnItemSpawn(templateTypes6, 50.0F, 40.0F, 1, false);
/*       */     } 
/* 10589 */     if (getAuxData() >= 6) {
/*       */ 
/*       */       
/* 10592 */       int[] templateTypes7 = { 456, 204 };
/*       */       
/* 10594 */       spawnItemSpawn(templateTypes7, 80.0F, 20.0F, 5, false);
/*       */     } 
/* 10596 */     if (getAuxData() >= 7) {
/*       */ 
/*       */       
/* 10599 */       int valrei = (Server.rand.nextInt(5) == 0) ? 56 : (Server.rand.nextBoolean() ? 524 : 867);
/* 10600 */       if (Server.rand.nextInt((Players.getInstance().getNumberOfPlayers() > 200) ? 10 : 40) == 0)
/*       */       {
/*       */         
/* 10603 */         valrei = 795 + Server.rand.nextInt(16);
/*       */       }
/* 10605 */       if (Server.rand.nextInt(1000) == 0) {
/* 10606 */         valrei = 465;
/*       */       }
/* 10608 */       int[] templateTypes5 = { valrei };
/*       */       
/* 10610 */       spawnItemSpawn(templateTypes5, 99.0F, 1.0F, 1, false);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final void checkItemSpawn() {
/* 10616 */     if (this.ownerId != -10L) {
/*       */       return;
/*       */     }
/*       */     
/* 10620 */     int templateId = this.template.getTemplateId();
/*       */     
/* 10622 */     if (templateId == 236) {
/*       */       
/* 10624 */       if (Servers.localServer.isChallengeServer() && 
/* 10625 */         WurmCalendar.getCurrentTime() > this.lastMaintained + 28800L) {
/*       */         
/* 10627 */         VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 10628 */         if (t != null && (t.getItems()).length < 50) {
/*       */           
/* 10630 */           int[] templateTypes = { 46, 144 };
/* 10631 */           spawnItemSpawn(templateTypes, 1.0F, 80.0F, 1, true);
/*       */         } 
/* 10633 */         this.lastMaintained = WurmCalendar.getCurrentTime();
/*       */       } 
/*       */       
/*       */       return;
/*       */     } 
/* 10638 */     if (System.currentTimeMillis() > getData()) {
/*       */       int[] arrayOfInt1; Player[] arrayOfPlayer;
/* 10640 */       int templateTypes[], templateTypes2[], i, templateTypes3[], baseMins, templateTypes4[], mins2, valrei, playerMod = Players.getInstance().getNumberOfPlayers() / 100;
/* 10641 */       boolean haveManyPlayers = (Players.getInstance().getNumberOfPlayers() > 200);
/* 10642 */       switch (templateId) {
/*       */ 
/*       */         
/*       */         case 969:
/* 10646 */           arrayOfInt1 = new int[] { 46, 72, 144, 316 };
/*       */           
/* 10648 */           spawnItemSpawn(arrayOfInt1, 60.0F, 20.0F, 3 + playerMod, false);
/* 10649 */           templateTypes2 = new int[] { 204 };
/*       */           
/* 10651 */           spawnItemSpawn(templateTypes2, 60.0F, 20.0F, 3 + playerMod, false);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/* 10658 */           if (Servers.localServer.testServer) {
/*       */             
/* 10660 */             baseMins = 3;
/* 10661 */             mins2 = 1;
/*       */           }
/*       */           else {
/*       */             
/* 10665 */             baseMins = 5;
/*       */ 
/*       */             
/* 10668 */             int rndMins = haveManyPlayers ? 20 : 40;
/* 10669 */             mins2 = Server.rand.nextInt(rndMins);
/*       */           } 
/*       */           
/* 10672 */           setData(System.currentTimeMillis() + 60000L * (baseMins + mins2));
/*       */ 
/*       */ 
/*       */           
/* 10676 */           deleteAllItemspawnEffects();
/*       */           break;
/*       */         
/*       */         case 970:
/* 10680 */           if (Servers.localServer.isChallengeServer()) {
/*       */             
/* 10682 */             arrayOfInt1 = new int[] { 46 };
/*       */             
/* 10684 */             spawnItemSpawn(arrayOfInt1, 80.0F, 20.0F, 3 + playerMod, false);
/* 10685 */             int[] arrayOfInt2 = { 204, 144 };
/*       */             
/* 10687 */             spawnItemSpawn(arrayOfInt2, 80.0F, 20.0F, 5 + playerMod, false);
/*       */             
/* 10689 */             int[] arrayOfInt3 = { 765, 693, 697, 456 };
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 10696 */             spawnItemSpawn(arrayOfInt3, 80.0F, 20.0F, 4 + playerMod, false);
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 10701 */             int[] arrayOfInt4 = { 374 + Server.rand.nextInt(10), (Server.rand.nextInt(3) == 0) ? 372 : 371 };
/*       */             
/* 10703 */             spawnItemSpawn(arrayOfInt4, 80.0F, 20.0F, 3, false);
/*       */             
/* 10705 */             int j = (Server.rand.nextInt(5) == 0) ? 56 : (Server.rand.nextBoolean() ? 524 : 867);
/* 10706 */             if (Server.rand.nextInt(haveManyPlayers ? 10 : 40) == 0)
/*       */             {
/* 10708 */               j = 795 + Server.rand.nextInt(17);
/*       */             }
/* 10710 */             if (Server.rand.nextInt(1000) == 0) {
/* 10711 */               j = 465;
/*       */             }
/* 10713 */             int[] templateTypes5 = { j };
/*       */             
/* 10715 */             spawnItemSpawn(templateTypes5, 99.0F, 1.0F, 1, false);
/* 10716 */             setData(System.currentTimeMillis() + 3600000L + 60000L * Server.rand
/*       */                 
/* 10718 */                 .nextInt(haveManyPlayers ? 20 : 60));
/*       */           
/*       */           }
/*       */           else {
/*       */ 
/*       */             
/* 10724 */             int[] arrayOfInt2 = { 693, 697, 456 };
/*       */             
/* 10726 */             spawnItemSpawn(arrayOfInt2, 80.0F, 20.0F, 4 + playerMod, false);
/*       */             
/* 10728 */             int[] arrayOfInt3 = { 374 + Server.rand.nextInt(10) };
/* 10729 */             spawnItemSpawn(arrayOfInt3, 80.0F, 20.0F, 1, false);
/* 10730 */             int num = Server.rand.nextInt(7);
/* 10731 */             if (num == 0) {
/* 10732 */               int j = 867;
/* 10733 */               int[] templateTypes5 = { j };
/*       */               
/* 10735 */               spawnItemSpawn(templateTypes5, 99.0F, 1.0F, 1, false);
/* 10736 */             } else if (num == 1) {
/* 10737 */               int j = 973 + Server.rand.nextInt(6);
/* 10738 */               int[] templateTypes5 = { j };
/*       */               
/* 10740 */               spawnItemSpawn(templateTypes5, 99.0F, 1.0F, 1, false);
/* 10741 */             } else if (num == 2) {
/* 10742 */               int j = 623;
/* 10743 */               int[] templateTypes5 = { j };
/*       */               
/* 10745 */               spawnItemSpawn(templateTypes5, 80.0F, 10.0F, 4, false);
/* 10746 */             } else if (num == 3) {
/* 10747 */               int j = 666;
/* 10748 */               int[] templateTypes5 = { j };
/*       */               
/* 10750 */               spawnItemSpawn(templateTypes5, 80.0F, 10.0F, 1, false);
/*       */             } 
/*       */ 
/*       */ 
/*       */             
/* 10755 */             setData(System.currentTimeMillis() + 86400000L + 3600000L + 60000L * Server.rand
/*       */ 
/*       */                 
/* 10758 */                 .nextInt(haveManyPlayers ? 20 : 60));
/*       */           } 
/*       */           
/* 10761 */           for (arrayOfPlayer = Players.getInstance().getPlayers(), i = arrayOfPlayer.length, baseMins = 0; baseMins < i; ) { Player player = arrayOfPlayer[baseMins];
/* 10762 */             player.playPersonalSound("sound.spawn.item.central");
/*       */             baseMins++; }
/*       */           
/* 10765 */           deleteAllItemspawnEffects();
/*       */           break;
/*       */ 
/*       */         
/*       */         case 971:
/* 10770 */           templateTypes = new int[] { 44, 46, 132, 218, 38, 72, 30, 29, 32, 28, 35, 349, 456, 45, 694, 144, 316 };
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/* 10791 */           spawnItemSpawn(templateTypes, 50.0F, 40.0F, 50, false);
/*       */           
/* 10793 */           templateTypes3 = new int[] { 204 };
/* 10794 */           spawnItemSpawn(templateTypes3, 50.0F, 40.0F, 100, false);
/*       */ 
/*       */ 
/*       */ 
/*       */           
/* 10799 */           templateTypes4 = new int[] { 374 + Server.rand.nextInt(10), 610 + Server.rand.nextInt(10) };
/*       */           
/* 10801 */           spawnItemSpawn(templateTypes4, 50.0F, 50.0F, 10, false);
/*       */ 
/*       */           
/* 10804 */           valrei = Server.rand.nextBoolean() ? 867 : (Server.rand.nextBoolean() ? 524 : 525);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/* 10812 */           if (Server.rand.nextInt(1000) == 0) {
/* 10813 */             valrei = 465;
/*       */           }
/*       */           
/* 10816 */           if (Server.rand.nextInt(10) == 0) {
/*       */             
/* 10818 */             int[] arrayOfInt = { 765, 693, 697, 785, 456, 597, valrei };
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 10828 */             spawnItemSpawn(arrayOfInt, 50.0F, 40.0F, 1, false);
/*       */           } 
/*       */ 
/*       */           
/* 10832 */           setData(System.currentTimeMillis() + 43200000L);
/*       */           
/* 10834 */           for (Player player : Players.getInstance().getPlayers()) {
/* 10835 */             player.playPersonalSound("sound.spawn.item.perimeter");
/*       */           }
/*       */           
/* 10838 */           deleteAllItemspawnEffects();
/*       */           break;
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*       */     } else {
/*       */       long timeout;
/*       */       String effectName;
/* 10849 */       switch (templateId) {
/*       */         
/*       */         case 970:
/* 10852 */           effectName = "central";
/* 10853 */           timeout = 600000L;
/*       */           break;
/*       */         
/*       */         case 971:
/* 10857 */           effectName = "perimeter";
/* 10858 */           timeout = 1800000L;
/*       */           break;
/*       */         
/*       */         default:
/*       */           return;
/*       */       } 
/*       */       
/* 10865 */       if (System.currentTimeMillis() <= getData() - timeout) {
/*       */         return;
/*       */       }
/*       */       
/* 10869 */       for (Effect effect : getEffects()) {
/*       */         
/* 10871 */         if (effect.getType() == 19) {
/*       */           return;
/*       */         }
/*       */       } 
/*       */ 
/*       */       
/* 10877 */       logInfo("Spawning " + effectName + " effect since it doesn't exist: " + (getEffects()).length);
/*       */ 
/*       */       
/* 10880 */       Effect eff = EffectFactory.getInstance().createSpawnEff(getWurmId(), getPosX(), getPosY(), getPosZ(), isOnSurface());
/* 10881 */       addEffect(eff);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private void deleteAllItemspawnEffects() {
/* 10887 */     for (Effect eff : getEffects()) {
/* 10888 */       if (eff.getType() == 19) {
/* 10889 */         deleteEffect(eff);
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void doGnomeTrick() {
/* 10900 */     Village current = Zones.getVillage(getTileX(), getTileY(), true);
/*       */     
/* 10902 */     int dist = (int)Math.max(1.0F, this.qualityLevel - 30.0F);
/* 10903 */     int dist2 = dist * dist;
/*       */     
/* 10905 */     Item itemToPinch = null;
/* 10906 */     for (int x = 0; x < dist; x++) {
/*       */       
/* 10908 */       for (int y = 0; y < dist; y++) {
/*       */         
/* 10910 */         if (Server.rand.nextInt(dist2) == 0) {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */           
/* 10916 */           VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 10917 */           if (t != null)
/*       */           {
/*       */ 
/*       */             
/* 10921 */             if (current != null && t.getVillage() == current) {
/*       */ 
/*       */ 
/*       */               
/* 10925 */               Item[] titems = t.getItems();
/* 10926 */               if (titems.length > 0)
/*       */               {
/*       */ 
/*       */                 
/* 10930 */                 for (Item tt : titems) {
/*       */                   
/* 10932 */                   if (tt != this) {
/*       */ 
/*       */ 
/*       */                     
/* 10936 */                     if (testInsertItem(tt)) {
/*       */                       
/* 10938 */                       itemToPinch = tt;
/*       */                       
/*       */                       break;
/*       */                     } 
/* 10942 */                     if (tt.isHollow()) {
/*       */                       
/* 10944 */                       Item[] pitems = tt.getAllItems(false);
/* 10945 */                       for (Item pt : pitems) {
/*       */                         
/* 10947 */                         if (testInsertItem(pt)) {
/*       */                           
/* 10949 */                           itemToPinch = pt;
/*       */                           
/*       */                           break;
/*       */                         } 
/*       */                       } 
/*       */                     } 
/* 10955 */                     if (itemToPinch != null)
/*       */                       break; 
/*       */                   } 
/*       */                 }  } 
/*       */             }  } 
/*       */         } 
/*       */       } 
/*       */     } 
/* 10963 */     if (itemToPinch != null)
/*       */     {
/*       */       
/* 10966 */       if (itemToPinch.getSizeX() <= getSizeX() && itemToPinch.getSizeY() <= getSizeY() && itemToPinch.getSizeZ() <= getSizeZ())
/*       */       {
/*       */ 
/*       */         
/* 10970 */         if (!itemToPinch.isBulkItem() || !itemToPinch.isLiquid() || !itemToPinch.isHollow() || 
/* 10971 */           !itemToPinch.isNoTake() || !itemToPinch.isDecoration())
/*       */         {
/*       */           
/* 10974 */           if (itemToPinch.getWeightGrams() <= getWeightGrams()) {
/*       */ 
/*       */             
/* 10977 */             if ((getAllItems(true)).length >= 100) {
/*       */               return;
/*       */             }
/*       */ 
/*       */             
/* 10982 */             if (itemToPinch.getParentId() != -10L) {
/*       */               return;
/*       */             }
/*       */ 
/*       */             
/* 10987 */             if (getParentId() != -10L) {
/*       */               return;
/*       */             }
/*       */ 
/*       */             
/*       */             try {
/* 10993 */               logInfo(getName() + " " + getWurmId() + " pinching " + itemToPinch.getName());
/* 10994 */               itemToPinch.getParent().dropItem(itemToPinch.getWurmId(), false);
/* 10995 */               insertItem(itemToPinch, true);
/* 10996 */               setAuxData((byte)20);
/*       */             }
/* 10998 */             catch (NoSuchItemException nsi) {
/*       */               
/* 11000 */               logWarn("Unexpected " + itemToPinch.getName() + " " + nsi.getMessage());
/*       */             } 
/*       */           } 
/*       */         }
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public final float ladderRotate(float rot) {
/* 11010 */     int num = (int)(rot / 11.25D);
/* 11011 */     num /= 2;
/* 11012 */     return (float)(num * 22.5D);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void checkDrift() {
/* 11021 */     if (!isBoat() || !isOnSurface()) {
/*       */       return;
/*       */     }
/*       */     
/* 11025 */     if (this.parentId != -10L) {
/*       */       return;
/*       */     }
/*       */     
/* 11029 */     Vector3f worldPos = getPos3f();
/* 11030 */     if (worldPos.z >= -1.0F) {
/*       */       return;
/*       */     }
/* 11033 */     if (isMooredBoat())
/*       */     {
/* 11035 */       if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*       */ 
/*       */         
/*       */         try {
/* 11039 */           Item anchor = Items.getItem(getData());
/* 11040 */           if (anchor.isAnchor()) {
/*       */             
/* 11042 */             float driftChance = getMaterialAnchorBonus(anchor.getMaterial());
/* 11043 */             if (Server.rand.nextFloat() < driftChance) {
/*       */               return;
/*       */             }
/*       */           } 
/* 11047 */         } catch (NoSuchItemException e) {
/*       */           return;
/*       */         } 
/*       */       } else {
/*       */         return;
/*       */       } 
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11058 */     Vehicle vehic = Vehicles.getVehicleForId(this.id);
/* 11059 */     if (vehic == null) {
/*       */       return;
/*       */     }
/*       */     
/* 11063 */     if (getCurrentQualityLevel() < 10.0F) {
/*       */       return;
/*       */     }
/*       */     
/* 11067 */     if (vehic.pilotId != -10L) {
/*       */       return;
/*       */     }
/*       */     
/* 11071 */     VolaTile t = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 11072 */     if (t == null) {
/*       */       
/* 11074 */       logWarn(getName() + " drifting, item has no tile at " + getTilePos() + " surfaced=" + isOnSurface());
/*       */       
/*       */       return;
/*       */     } 
/* 11078 */     float nPosX = worldPos.x + Server.getWeather().getXWind();
/* 11079 */     float nPosY = worldPos.y + Server.getWeather().getYWind();
/*       */     
/* 11081 */     float worldMetersX = CoordUtils.TileToWorld(Zones.worldTileSizeX);
/* 11082 */     float worldMetersY = CoordUtils.TileToWorld(Zones.worldTileSizeY);
/*       */     
/* 11084 */     if (nPosX <= 5.0F || nPosX >= worldMetersX - 10.0F || nPosY <= 5.0F || nPosY >= worldMetersY - 10.0F) {
/*       */       return;
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11091 */     int diffdecx = (int)(nPosX * 100.0F - worldPos.x * 100.0F);
/* 11092 */     int diffdecy = (int)(nPosY * 100.0F - worldPos.y * 100.0F);
/*       */     
/* 11094 */     if (diffdecx == 0 && diffdecy == 0) {
/*       */       return;
/*       */     }
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11101 */     nPosX = worldPos.x + diffdecx * 0.01F;
/* 11102 */     nPosY = worldPos.y + diffdecy * 0.01F;
/*       */     
/* 11104 */     TilePos testPos = CoordUtils.WorldToTile(nPosX, nPosY);
/* 11105 */     int meshTile = Server.caveMesh.getTile(testPos);
/* 11106 */     byte tileType = Tiles.decodeType(meshTile);
/* 11107 */     if (Tiles.isSolidCave(tileType))
/*       */     {
/* 11109 */       if ((Tiles.decodeType(Server.caveMesh.getTile(getTilePos())) & 0xFF) != 201) {
/*       */         
/* 11111 */         logInfo(getName() + " drifting in rock at " + getTilePos() + ".");
/*       */ 
/*       */         
/*       */         return;
/*       */       } 
/*       */     }
/*       */     
/*       */     try {
/* 11119 */       if (Zones.calculateHeight(nPosX, nPosY, true) <= vehic.maxHeight) {
/*       */         
/* 11121 */         t.moveItem(this, nPosX, nPosY, worldPos.z, 
/* 11122 */             Creature.normalizeAngle(getRotation()), true, worldPos.z);
/* 11123 */         MovementScheme.itemVehicle = this;
/* 11124 */         MovementScheme.movePassengers(vehic, null, false);
/*       */       } 
/*       */       
/* 11127 */       if (Zones.calculateHeight(worldPos.x, worldPos.y, true) >= vehic.maxHeight)
/*       */       {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 11136 */         setDamage(getDamage() + (diffdecx + diffdecy) * 0.1F);
/*       */       }
/*       */     }
/* 11139 */     catch (NoSuchZoneException nsz) {
/*       */       
/* 11141 */       Items.destroyItem(this.id);
/* 11142 */       logInfo("ItemVehic " + getName() + " destroyed.");
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean pollHatching() {
/* 11148 */     if (isAbility()) {
/*       */       
/* 11150 */       if (isPlanted())
/*       */       {
/* 11152 */         if ((int)this.damage == 3) {
/*       */           
/* 11154 */           Server.getInstance().broadCastMessage("The " + 
/* 11155 */               getName() + " starts to emanate a weird worrying sound.", getTileX(), 
/* 11156 */               getTileY(), isOnSurface(), 50);
/* 11157 */           setRarity((byte)2);
/*       */         } 
/* 11159 */         if ((int)this.damage == 50) {
/*       */           
/* 11161 */           Server.getInstance().broadCastMessage("The " + 
/* 11162 */               getName() + " starts to pulsate with a bright light, drawing from the ground.", 
/* 11163 */               getTileX(), 
/* 11164 */               getTileY(), isOnSurface(), 50);
/* 11165 */           setRarity((byte)3);
/*       */         } 
/* 11167 */         if ((int)this.damage == 75)
/*       */         {
/* 11169 */           Server.getInstance()
/* 11170 */             .broadCastMessage("The ground around " + 
/*       */               
/* 11172 */               getName() + " is shivering and heaving! Something big is going to happen here soon! You have to get far away!", 
/*       */               
/* 11174 */               getTileX(), 
/* 11175 */               getTileY(), isOnSurface(), 50);
/*       */         }
/* 11177 */         else if ((int)this.damage == 95)
/*       */         {
/* 11179 */           Server.getInstance().broadCastMessage(
/* 11180 */               LoginHandler.raiseFirstLetter(getName() + " is now completely covered in cracks. Run!"), 
/* 11181 */               getTileX(), 
/* 11182 */               getTileY(), isOnSurface(), 50);
/*       */         }
/* 11184 */         else if ((int)this.damage == 99)
/*       */         {
/* 11186 */           Server.getInstance().broadCastMessage(
/* 11187 */               LoginHandler.raiseFirstLetter(getNameWithGenus() + " is gonna explode! Too late to run..."), 
/* 11188 */               getTileX(), 
/* 11189 */               getTileY(), isOnSurface(), 20);
/*       */         
/*       */         }
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 11196 */     else if ((int)this.damage == 85) {
/*       */       
/* 11198 */       Server.getInstance().broadCastMessage("Cracks are starting to form on " + getNameWithGenus() + ".", getTileX(), 
/* 11199 */           getTileY(), isOnSurface(), 20);
/*       */     }
/* 11201 */     else if ((int)this.damage == 95) {
/*       */       
/* 11203 */       Server.getInstance().broadCastMessage(
/* 11204 */           LoginHandler.raiseFirstLetter(getNameWithGenus() + " is now completely covered in cracks."), 
/* 11205 */           getTileX(), 
/* 11206 */           getTileY(), isOnSurface(), 20);
/*       */     }
/* 11208 */     else if ((int)this.damage == 99) {
/*       */       
/* 11210 */       Server.getInstance().broadCastMessage(
/* 11211 */           LoginHandler.raiseFirstLetter(getNameWithGenus() + " stirs as something emerges from it!"), getTileX(), 
/* 11212 */           getTileY(), isOnSurface(), 20);
/*       */     } 
/*       */ 
/*       */     
/* 11216 */     return setDamage(this.damage + 1.0F);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private boolean pollLightSource() {
/* 11227 */     if (this.isLightOverride) {
/* 11228 */       return false;
/*       */     }
/* 11230 */     float fuelModifier = 1.0F;
/* 11231 */     if (getSpellEffects() != null)
/*       */     {
/* 11233 */       fuelModifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FUELUSE);
/*       */     }
/*       */     
/* 11236 */     if (!isOnFire()) {
/*       */ 
/*       */       
/* 11239 */       if (getTemperature() > 600) {
/* 11240 */         setTemperature((short)200);
/*       */       }
/* 11242 */       return false;
/*       */     } 
/* 11244 */     if (getTemplateId() == 1301) {
/*       */       
/* 11246 */       pollWagonerCampfire(WurmCalendar.getHour());
/* 11247 */       return false;
/*       */     } 
/* 11249 */     if (getTemplateId() == 1243) {
/*       */ 
/*       */       
/* 11252 */       if (Server.getSecondsUptime() % (int)(600.0F * fuelModifier) == 0) {
/*       */         
/* 11254 */         int aux = getAuxData();
/* 11255 */         if (aux < 0) {
/* 11256 */           aux = 127;
/*       */         }
/* 11258 */         setAuxData((byte)Math.max(0, aux - 1));
/* 11259 */         if (getAuxData() <= 0)
/*       */         {
/*       */           
/* 11262 */           setTemperature((short)200);
/*       */         }
/*       */       } 
/* 11265 */       return false;
/*       */     } 
/* 11267 */     if (getTemplateId() == 1396) {
/*       */       
/* 11269 */       if (getBless() != null)
/*       */       {
/*       */         
/* 11272 */         return false;
/*       */       }
/* 11274 */       if (!isPlanted()) {
/*       */         
/* 11276 */         setTemperature((short)200);
/* 11277 */         return false;
/*       */       } 
/*       */     } 
/* 11280 */     if (!isOilConsuming() && !isCandleHolder() && !isFireplace()) {
/*       */       
/* 11282 */       if (!isIndestructible())
/*       */       {
/* 11284 */         if (Server.getSecondsUptime() % 60 == 0)
/*       */         {
/*       */ 
/*       */           
/* 11288 */           return setQualityLevel(getQualityLevel() - 0.5F);
/*       */         }
/*       */       }
/* 11291 */       return false;
/*       */     } 
/* 11293 */     if (!isStreetLamp() && !isBrazier() && !isFireplace()) {
/*       */ 
/*       */       
/* 11296 */       if (Server.getSecondsUptime() % (int)(300.0F * fuelModifier) == 0) {
/*       */ 
/*       */         
/* 11299 */         setAuxData((byte)Math.max(0, getAuxData() - 1));
/* 11300 */         if (getAuxData() <= 0)
/*       */         {
/*       */           
/* 11303 */           setTemperature((short)200);
/*       */         }
/*       */       } 
/* 11306 */       return false;
/*       */     } 
/* 11308 */     boolean onSurface = isOnSurface();
/* 11309 */     int hour = WurmCalendar.getHour();
/* 11310 */     boolean autoSnuffMe = isAutoLit();
/* 11311 */     VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/* 11312 */     Structure structure = (vt == null) ? null : vt.getStructure();
/* 11313 */     Village village = (vt == null) ? null : vt.getVillage();
/* 11314 */     if (!autoSnuffMe) {
/*       */ 
/*       */       
/* 11317 */       autoSnuffMe = (village != null || this.onBridge != -10L);
/* 11318 */       if (!autoSnuffMe && getBless() != null) {
/*       */ 
/*       */         
/* 11321 */         autoSnuffMe = (onBridge() != -10L);
/*       */         
/* 11323 */         if (!autoSnuffMe && structure == null)
/*       */         {
/* 11325 */           if (onSurface) {
/*       */             
/* 11327 */             int encodedTile = Server.surfaceMesh.getTile(getTilePos());
/* 11328 */             autoSnuffMe = (getBless() != null && Tiles.isRoadType(encodedTile));
/*       */           
/*       */           }
/*       */           else {
/*       */             
/* 11333 */             int encodedTile = Server.caveMesh.getTile(getTilePos());
/* 11334 */             autoSnuffMe = (getBless() != null && Tiles.isReinforcedFloor(Tiles.decodeType(encodedTile)));
/*       */           } 
/*       */         }
/*       */       } 
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11345 */     if (Server.rand.nextFloat() <= 0.16F * fuelModifier) {
/*       */       
/* 11347 */       if (!autoSnuffMe) {
/*       */ 
/*       */ 
/*       */         
/* 11351 */         setAuxData((byte)Math.max(0, getAuxData() - 1));
/*       */         
/* 11353 */         if (getAuxData() <= 10)
/*       */         {
/*       */           
/* 11356 */           refuelLampFromClosestVillage();
/*       */         }
/*       */       } 
/* 11359 */       if (isFireplace() && village != null)
/*       */       {
/*       */ 
/*       */         
/* 11363 */         if (structure != null && structure.isTypeHouse() && structure.isFinished())
/*       */         {
/* 11365 */           if (getAuxData() <= 10)
/*       */           {
/*       */ 
/*       */             
/* 11369 */             fillFromVillage(village, true);
/*       */           }
/*       */         }
/*       */       }
/* 11373 */       if (getAuxData() <= 0) {
/*       */ 
/*       */         
/* 11376 */         setTemperature((short)200);
/* 11377 */         if (isFireplace())
/* 11378 */           deleteFireEffect(); 
/* 11379 */         return false;
/*       */       } 
/*       */     } 
/*       */ 
/*       */     
/* 11384 */     if (onSurface && hour > 4 && hour < 16)
/*       */     {
/*       */       
/* 11387 */       if (autoSnuffMe) {
/*       */         
/* 11389 */         setTemperature((short)200);
/* 11390 */         if (isFireplace()) {
/* 11391 */           deleteFireEffect();
/*       */         }
/*       */       } 
/*       */     }
/* 11395 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void pollWagonerCampfire(int hour) {
/* 11401 */     boolean onSurface = isOnSurface();
/*       */ 
/*       */     
/* 11404 */     boolean atCamp = false;
/* 11405 */     Wagoner wagoner = Wagoner.getWagoner(this.lastOwner);
/* 11406 */     if (wagoner != null && wagoner.isIdle())
/* 11407 */       atCamp = true; 
/* 11408 */     boolean isLit = (getTemperature() > 200);
/* 11409 */     boolean snuff = false;
/* 11410 */     boolean light = false;
/* 11411 */     int aux = 0;
/* 11412 */     if (atCamp) {
/*       */ 
/*       */       
/* 11415 */       switch (hour) {
/*       */ 
/*       */         
/*       */         case 0:
/*       */         case 1:
/*       */         case 2:
/*       */         case 3:
/*       */         case 4:
/*       */         case 23:
/* 11424 */           aux = 0;
/* 11425 */           snuff = true;
/*       */           break;
/*       */ 
/*       */ 
/*       */ 
/*       */         
/*       */         case 5:
/* 11432 */           if (getAuxData() == 0)
/* 11433 */             aux = Server.rand.nextInt(10) + 1; 
/* 11434 */           light = true;
/* 11435 */           wagoner.say(Wagoner.Speech.BREAKFAST);
/*       */           break;
/*       */ 
/*       */         
/*       */         case 6:
/*       */         case 7:
/*       */         case 8:
/*       */         case 9:
/*       */         case 10:
/* 11444 */           aux = 0;
/* 11445 */           snuff = onSurface;
/* 11446 */           light = !onSurface;
/*       */           break;
/*       */ 
/*       */ 
/*       */         
/*       */         case 11:
/* 11452 */           if (getAuxData() == 0)
/* 11453 */             aux = Server.rand.nextInt(10) + 1; 
/* 11454 */           light = true;
/* 11455 */           wagoner.say(Wagoner.Speech.LUNCH);
/*       */           break;
/*       */ 
/*       */         
/*       */         case 12:
/*       */         case 13:
/*       */         case 14:
/*       */         case 15:
/*       */         case 16:
/* 11464 */           aux = 0;
/* 11465 */           snuff = onSurface;
/* 11466 */           light = !onSurface;
/*       */           break;
/*       */ 
/*       */         
/*       */         case 17:
/* 11471 */           if (getAuxData() == 0) {
/* 11472 */             aux = Server.rand.nextInt(5) + 11;
/* 11473 */           } else if (getAuxData() >= 11 && getAuxData() <= 15) {
/* 11474 */             aux = getAuxData() + 5;
/* 11475 */           }  light = true;
/* 11476 */           wagoner.say(Wagoner.Speech.DINNER);
/*       */           break;
/*       */ 
/*       */         
/*       */         case 18:
/*       */         case 19:
/*       */         case 20:
/*       */         case 21:
/*       */         case 22:
/* 11485 */           aux = 0;
/* 11486 */           light = true;
/*       */           break;
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/*       */     } else {
/* 11494 */       snuff = true;
/*       */     } 
/*       */     
/* 11497 */     if (snuff && isLit) {
/*       */       
/* 11499 */       setTemperature((short)200);
/* 11500 */       deleteFireEffect();
/* 11501 */       setAuxData((byte)aux);
/* 11502 */       updateIfGroundItem();
/*       */ 
/*       */     
/*       */     }
/* 11506 */     else if (light && !isLit) {
/*       */       
/* 11508 */       setTemperature((short)10000);
/*       */       
/* 11510 */       deleteFireEffect();
/*       */       
/* 11512 */       Effect effect = EffectFactory.getInstance().createFire(getWurmId(), getPosX(), getPosY(), 
/* 11513 */           getPosZ(), isOnSurface());
/* 11514 */       addEffect(effect);
/* 11515 */       setAuxData((byte)aux);
/* 11516 */       updateIfGroundItem();
/*       */ 
/*       */     
/*       */     }
/* 11520 */     else if (aux != getAuxData()) {
/*       */       
/* 11522 */       setAuxData((byte)aux);
/* 11523 */       updateIfGroundItem();
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void refuelLampFromClosestVillage() {
/* 11530 */     int startx = getTileX();
/* 11531 */     int starty = getTileY();
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 11541 */     int stepSize = 7;
/* 11542 */     for (int step = 1; step < 8; step++) {
/*       */       
/* 11544 */       int distance = step * 7;
/*       */ 
/*       */       
/* 11547 */       for (int yOffs = -distance; yOffs <= distance; yOffs += 14) {
/*       */         
/* 11549 */         int ys = Zones.safeTileY(starty + yOffs);
/* 11550 */         for (int x = startx - distance; x <= startx + distance; x += 7) {
/*       */           
/* 11552 */           Village vill = Villages.getVillage(Zones.safeTileX(x), ys, true);
/* 11553 */           if (fillFromVillage(vill, false)) {
/*       */             return;
/*       */           }
/*       */         } 
/*       */       } 
/*       */ 
/*       */ 
/*       */       
/* 11561 */       for (int xOffs = -distance; xOffs <= distance; xOffs += 14) {
/*       */         
/* 11563 */         int xs = Zones.safeTileX(startx + xOffs);
/* 11564 */         for (int y = starty - distance; y < starty + distance; y += 7) {
/*       */           
/* 11566 */           Village vill = Villages.getVillage(xs, Zones.safeTileY(y), true);
/* 11567 */           if (fillFromVillage(vill, false)) {
/*       */             return;
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
/*       */   private boolean fillFromVillage(@Nullable Village vill, boolean onDeed) {
/* 11583 */     if (vill != null) {
/*       */       
/* 11585 */       int received = vill.getOilAmount(110, onDeed);
/* 11586 */       if (received > 0)
/*       */       {
/* 11588 */         setAuxData((byte)(getAuxData() + received));
/*       */       }
/* 11590 */       return true;
/*       */     } 
/* 11592 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private void checkIfLightStreetLamp() {
/* 11597 */     boolean isValidStreetLamp = (isStreetLamp() && isPlanted() && getAuxData() > 0);
/* 11598 */     boolean isValidBrazier = (isBrazier() && getAuxData() > 0);
/* 11599 */     boolean isValidFireplace = (isFireplace() && getAuxData() > 0);
/*       */     
/* 11601 */     if (!isValidStreetLamp && !isValidBrazier && !isValidFireplace && getTemplateId() != 1301) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/* 11606 */     if (getTemperature() > 200) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/* 11611 */     if (getTemplateId() == 1301) {
/*       */       
/* 11613 */       pollWagonerCampfire(WurmCalendar.getHour());
/*       */       
/*       */       return;
/*       */     } 
/* 11617 */     boolean onSurface = isOnSurface();
/* 11618 */     int hour = WurmCalendar.getHour();
/* 11619 */     boolean autoLightMe = isAutoLit();
/* 11620 */     VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/* 11621 */     Structure structure = (vt == null) ? null : vt.getStructure();
/* 11622 */     Village village = (vt == null) ? null : vt.getVillage();
/* 11623 */     if (!autoLightMe) {
/*       */ 
/*       */       
/* 11626 */       autoLightMe = (village != null || this.onBridge != -10L);
/* 11627 */       if (!autoLightMe && getBless() != null) {
/*       */ 
/*       */         
/* 11630 */         autoLightMe = (onBridge() != -10L);
/*       */         
/* 11632 */         if (!autoLightMe && structure == null)
/*       */         {
/* 11634 */           if (onSurface) {
/*       */             
/* 11636 */             int encodedTile = Server.surfaceMesh.getTile(getTilePos());
/* 11637 */             autoLightMe = (getBless() != null && Tiles.isRoadType(encodedTile));
/*       */           
/*       */           }
/*       */           else {
/*       */             
/* 11642 */             int encodedTile = Server.caveMesh.getTile(getTilePos());
/* 11643 */             autoLightMe = (getBless() != null && Tiles.isReinforcedFloor(Tiles.decodeType(encodedTile)));
/*       */           } 
/*       */         }
/*       */       } 
/*       */     } 
/* 11648 */     if (onSurface && (hour < 4 || hour > 16))
/*       */     {
/* 11650 */       if (autoLightMe) {
/*       */         
/* 11652 */         setTemperature((short)10000);
/* 11653 */         if (isFireplace()) {
/*       */ 
/*       */           
/* 11656 */           deleteFireEffect();
/*       */           
/* 11658 */           Effect effect = EffectFactory.getInstance().createFire(getWurmId(), getPosX(), getPosY(), 
/* 11659 */               getPosZ(), isOnSurface());
/* 11660 */           addEffect(effect);
/*       */         } 
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   private void pollHugeAltar() {
/* 11669 */     if (this.damage <= 0.0F) {
/*       */       return;
/*       */     }
/*       */     
/* 11673 */     boolean heal = false;
/* 11674 */     if (getTemplateId() == 328) {
/*       */       
/* 11676 */       if (System.currentTimeMillis() - lastPolledBlackAltar > 600000L)
/*       */       {
/* 11678 */         heal = true;
/* 11679 */         lastPolledBlackAltar = System.currentTimeMillis();
/*       */       
/*       */       }
/*       */     
/*       */     }
/* 11684 */     else if (System.currentTimeMillis() - lastPolledWhiteAltar > 600000L) {
/*       */       
/* 11686 */       heal = true;
/* 11687 */       lastPolledWhiteAltar = System.currentTimeMillis();
/*       */     } 
/*       */ 
/*       */     
/* 11691 */     if (heal) {
/*       */       
/* 11693 */       setDamage(this.damage - 1.0F);
/* 11694 */       if (this.damage > 0.0F) {
/* 11695 */         Server.getInstance()
/* 11696 */           .broadCastNormal("You have a sudden vision of " + getName() + " being under attack.");
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
/*       */ 
/*       */ 
/*       */   
/*       */   private void coolItem(int ticks) {
/* 11714 */     if (getTemperature() > 200 && (!isLight() || isLightOverride()) && getTemplateId() != 1243 && getTemplateId() != 1301) {
/*       */       
/* 11716 */       short degrees = 5;
/* 11717 */       if (isInsulated()) {
/*       */         
/* 11719 */         Item outer = getParentOuterItemOrNull();
/* 11720 */         if (outer != null && Server.rand.nextInt(99) < 70 + outer.getRarity() * 2)
/*       */           return; 
/* 11722 */         degrees = 1;
/*       */       } 
/* 11724 */       short oldTemperature = getTemperature();
/* 11725 */       setTemperature((short)Math.max(200, getTemperature() - ticks * degrees));
/* 11726 */       if (getTemperatureState(oldTemperature) != getTemperatureState(getTemperature())) {
/* 11727 */         notifyWatchersTempChange();
/*       */       }
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   private void coolInventoryItem() {
/* 11734 */     coolItem(1);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void coolInventoryItem(long timeSinceLastCool) {
/* 11741 */     int ticks = (int)Math.min(timeSinceLastCool / 1000L, 429496728L);
/* 11742 */     coolItem(ticks);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOilConsuming() {
/* 11747 */     return this.template.oilConsuming;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isCandleHolder() {
/* 11752 */     return this.template.candleHolder;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFireplace() {
/* 11757 */     return (getTemplateId() == 889);
/*       */   }
/*       */ 
/*       */   
/*       */   private void coolOutSideItem(boolean everySecond, boolean insideStructure) {
/* 11762 */     if (this.temperature > 200) {
/*       */       
/* 11764 */       float speed = 1.0F;
/* 11765 */       if (insideStructure) {
/*       */         
/* 11767 */         speed *= 0.75F;
/*       */       }
/* 11769 */       else if (Server.getWeather().getRain() > 0.2D) {
/*       */         
/* 11771 */         speed *= 2.0F;
/*       */       } 
/* 11773 */       if (getRarity() > 0) {
/* 11774 */         speed = (float)(speed * Math.pow(0.8999999761581421D, getRarity()));
/*       */       }
/* 11776 */       int templateId = this.template.getTemplateId();
/* 11777 */       if (getSpellEffects() != null)
/*       */       {
/* 11779 */         if (templateId == 180 || templateId == 1023 || templateId == 1028 || templateId == 1178 || templateId == 37 || templateId == 178)
/*       */         {
/*       */ 
/*       */ 
/*       */           
/* 11784 */           if (Server.rand.nextFloat() < getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_FUELUSE) - 1.0F) {
/* 11785 */             speed = 0.0F;
/*       */           }
/*       */         }
/*       */       }
/* 11789 */       if (getTemplateId() == 180 || getTemplateId() == 178 || 
/* 11790 */         getTemplateId() == 1023 || getTemplateId() == 1028) {
/*       */ 
/*       */         
/* 11793 */         if (System.currentTimeMillis() - 60000L > this.lastAuxPoll)
/*       */         {
/* 11795 */           if (getTemperature() > 200 && getAuxData() < 30) {
/*       */             
/* 11797 */             setAuxData((byte)(getAuxData() + 1));
/* 11798 */             this.lastAuxPoll = System.currentTimeMillis();
/*       */           } 
/*       */         }
/*       */         
/* 11802 */         if (getAuxData() > 30)
/*       */         {
/* 11804 */           setAuxData((byte)30);
/*       */         }
/*       */       } 
/* 11807 */       if (templateId == 180 || templateId == 1023 || templateId == 1028) {
/*       */         
/* 11809 */         setTemperature((short)(int)Math.max(200.0F, this.temperature - speed * 
/* 11810 */               Math.max(1.0F, 11.0F - Math.max(1.0F, 20.0F * Math.max(30.0F, getCurrentQualityLevel()) / 200.0F))));
/*       */       }
/* 11812 */       else if (templateId == 1178) {
/*       */         
/* 11814 */         setTemperature((short)(int)Math.max(200.0F, this.temperature - speed * 0.5F * 
/* 11815 */               Math.max(1.0F, 11.0F - Math.max(1.0F, 10.0F * Math.max(30.0F, getCurrentQualityLevel()) / 200.0F))));
/*       */       }
/* 11817 */       else if (templateId == 37 || templateId == 178) {
/*       */         
/* 11819 */         setTemperature((short)(int)Math.max(200.0F, this.temperature - speed * 
/* 11820 */               Math.max(1.0F, 11.0F - Math.max(1.0F, 10.0F * Math.max(30.0F, getCurrentQualityLevel()) / 200.0F))));
/* 11821 */         if (templateId == 37)
/*       */         {
/* 11823 */           if (this.temperature <= 210)
/*       */           {
/* 11825 */             if (getItems().isEmpty()) {
/*       */               
/* 11827 */               float ql = getCurrentQualityLevel();
/*       */               
/*       */               try {
/* 11830 */                 ItemFactory.createItem(141, ql, getPosX(), getPosY(), getRotation(), isOnSurface(), 
/* 11831 */                     getRarity(), getBridgeId(), null);
/*       */               }
/* 11833 */               catch (NoSuchTemplateException nst) {
/*       */                 
/* 11835 */                 logWarn("No template for ash?" + nst.getMessage(), (Throwable)nst);
/*       */               }
/* 11837 */               catch (FailedException fe) {
/*       */                 
/* 11839 */                 logWarn("What's this: " + fe.getMessage(), (Throwable)fe);
/*       */               } 
/*       */             } 
/* 11842 */             setQualityLevel(0.0F);
/* 11843 */             deleteFireEffect();
/*       */           }
/*       */         
/*       */         }
/*       */       }
/* 11848 */       else if ((isLight() && !isLightOverride()) || isFireplace() || 
/* 11849 */         getTemplateId() == 1243 || getTemplateId() == 1301) {
/*       */         
/* 11851 */         pollLightSource();
/*       */       }
/* 11853 */       else if (everySecond) {
/*       */         
/* 11855 */         setTemperature((short)(int)Math.max(200.0F, this.temperature - speed * 20.0F));
/*       */       }
/*       */       else {
/*       */         
/* 11859 */         setTemperature((short)(int)Math.max(200.0F, this.temperature - speed * 800.0F * 5.0F));
/*       */       } 
/*       */     } 
/*       */     
/* 11863 */     if (!isOnFire())
/*       */     {
/* 11865 */       if (isStreetLamp() || isBrazier() || isFireplace() || getTemplateId() == 1301) {
/*       */         
/* 11867 */         checkIfLightStreetLamp();
/*       */       }
/*       */       else {
/*       */         
/* 11871 */         deleteFireEffect();
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   private void modTemp(Item parent, int parentTemp, boolean insideStructure) {
/* 11878 */     short oldTemperature = this.temperature;
/*       */     
/* 11880 */     if (parentTemp > 200) {
/*       */ 
/*       */       
/* 11883 */       float qualityModifier = 1.0F;
/* 11884 */       int parentTemplateId = parent.getTemplateId();
/* 11885 */       float tempMod = 10.0F + 10.0F * (float)Math.min(10.0D, 
/* 11886 */           Server.getModifiedPercentageEffect(getCurrentQualityLevel()) / 100.0D);
/*       */       
/* 11888 */       boolean dealDam = (Server.rand.nextInt(30) == 0);
/* 11889 */       Item top = null;
/*       */       
/*       */       try {
/* 11892 */         top = Items.getItem(getTopParent());
/* 11893 */         int tp = top.getTemplateId();
/* 11894 */         if (tp == 180 || tp == 1023 || tp == 1028) {
/*       */ 
/*       */           
/* 11897 */           tempMod = (float)(tempMod + (10 + top.getRarity()) * Math.min(10.0D, Server.getModifiedPercentageEffect(top.getCurrentQualityLevel()) / 70.0D));
/* 11898 */           if (isFood() && Server.rand.nextInt(5) == 0)
/* 11899 */             dealDam = true; 
/* 11900 */           if (isBulk() && isFood()) {
/* 11901 */             qualityModifier = 0.8F;
/*       */           }
/* 11903 */         } else if (tp == 37 || tp == 178 || tp == 1178) {
/*       */ 
/*       */           
/* 11906 */           tempMod = (float)(tempMod + (5 + top.getRarity()) * Math.min(10.0D, Server.getModifiedPercentageEffect(top.getCurrentQualityLevel()) / 70.0D));
/* 11907 */           if (isBulk())
/*       */           {
/* 11909 */             if (isFood()) {
/*       */               
/* 11911 */               if (tp == 37) {
/* 11912 */                 qualityModifier = 0.8F;
/*       */               }
/*       */             } else {
/*       */               
/* 11916 */               qualityModifier = 0.8F;
/*       */             }
/*       */           
/*       */           }
/*       */         } 
/* 11921 */       } catch (NoSuchItemException noSuchItemException) {}
/*       */ 
/*       */       
/* 11924 */       if (!parent.equals(top))
/*       */       {
/*       */         
/* 11927 */         tempMod = (float)(tempMod + (7 + parent.getRarity()) * Math.min(10.0D, Server.getModifiedPercentageEffect(parent.getCurrentQualityLevel()) / 100.0D));
/*       */       }
/* 11929 */       tempMod = Math.max(10.0F, tempMod);
/* 11930 */       dealDam = (parentTemplateId != 74);
/*       */       
/* 11932 */       short oldTemp = getTemperature();
/* 11933 */       short newTemp = oldTemp;
/* 11934 */       if (isFood() && oldTemp > 1500)
/* 11935 */         tempMod = 1.0F; 
/* 11936 */       short diff = (short)(parentTemp - oldTemp);
/* 11937 */       if (diff > 0) {
/* 11938 */         newTemp = (short)(int)Math.min(parentTemp, oldTemp + Math.min(diff, tempMod));
/* 11939 */       } else if (diff < 0) {
/* 11940 */         newTemp = (short)(int)Math.max(parentTemp, oldTemp + Math.max(diff, -tempMod));
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 11949 */       if (isBurnable()) {
/*       */         
/* 11951 */         if (newTemp > 1000)
/*       */         {
/* 11953 */           if (!isIndestructible()) {
/*       */             
/* 11955 */             if (dealDam)
/*       */             {
/* 11957 */               if (isRepairable()) {
/* 11958 */                 setQualityLevel(getQualityLevel() - Math.max(2.0F, tempMod / 10.0F));
/*       */               } else {
/* 11960 */                 setDamage(getDamage() + Math.max(2.0F, tempMod / 10.0F));
/*       */               }  } 
/* 11962 */             if (getDamage() >= 100.0F) {
/*       */ 
/*       */ 
/*       */ 
/*       */               
/* 11967 */               int w = getWeightGrams() * fuelEfficiency(getMaterial());
/* 11968 */               int newt = parentTemp + w;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */               
/* 11974 */               if (top != null && getTemplateId() == 1276 && top
/* 11975 */                 .isOnFire() && top.getTemplateId() != 1178) {
/*       */                 
/* 11977 */                 Player[] lastOwner = Players.getInstance().getPlayers();
/* 11978 */                 for (Player player : lastOwner) {
/*       */                   
/* 11980 */                   if (getLastOwner() == player.getWurmId())
/*       */                   {
/* 11982 */                     player.getCommunicator().sendNormalServerMessage("The " + 
/* 11983 */                         getName() + " melts and puts out the flames, why did you do that?");
/*       */                   }
/*       */                 } 
/*       */                 
/* 11987 */                 top.setTemperature((short)200);
/* 11988 */                 top.deleteFireEffect();
/*       */                 return;
/*       */               } 
/* 11991 */               if (getTemplateId() != 1276)
/*       */               {
/* 11993 */                 parent.setTemperature((short)Math.min(30000, newt));
/*       */               }
/*       */               return;
/*       */             } 
/* 11997 */             if (getQualityLevel() <= 1.0E-4F) {
/*       */ 
/*       */               
/* 12000 */               int w = getWeightGrams() * fuelEfficiency(getMaterial());
/* 12001 */               int newt = parentTemp + w;
/* 12002 */               parent.setTemperature((short)Math.min(30000, newt));
/*       */               
/*       */               return;
/*       */             } 
/*       */           } 
/*       */         }
/* 12008 */         this.temperature = newTemp;
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 12013 */         if (getTemplateId() == 1285) {
/* 12014 */           newTemp = (short)Math.min(300, newTemp);
/*       */         }
/* 12016 */         setTemperature(newTemp);
/*       */         
/* 12018 */         if (isEgg() && newTemp > 400)
/*       */         {
/* 12020 */           if (getData1() > 0)
/* 12021 */             setData1(-1); 
/*       */         }
/* 12023 */         if (getTemplateId() == 128 && isSalted() && newTemp > 3000 && parent.getItemCount() == 1) {
/*       */           
/*       */           try
/*       */           {
/*       */ 
/*       */ 
/*       */             
/* 12030 */             int salts = getWeightGrams() / 1000 / 10;
/* 12031 */             Skill skill = null;
/*       */             
/*       */             try {
/* 12034 */               Creature performer = Server.getInstance().getCreature(getLastOwnerId());
/* 12035 */               skill = performer.getSkills().getSkillOrLearn(10038);
/*       */             }
/* 12037 */             catch (Exception exception) {}
/*       */ 
/*       */ 
/*       */             
/* 12041 */             float ql = 20.0F;
/*       */             
/* 12043 */             if (skill != null) {
/*       */               
/* 12045 */               float result = (float)skill.skillCheck(50.0D, this, 0.0D, false, (salts / 10));
/*       */               
/* 12047 */               ql = 10.0F + (result + 100.0F) * 0.45F;
/*       */             } 
/* 12049 */             for (int x = 0; x < salts; x++) {
/*       */               
/* 12051 */               Item salt = ItemFactory.createItem(349, ql, (byte)36, getRarity(), this.creator);
/* 12052 */               salt.setLastOwnerId(getLastOwnerId());
/* 12053 */               salt.setTemperature(newTemp);
/* 12054 */               parent.insertItem(salt, true);
/*       */             } 
/* 12056 */             setWeight(getWeightGrams() - (salts + 1) * 100, true);
/* 12057 */             setIsSalted(false);
/*       */           }
/* 12059 */           catch (FailedException e)
/*       */           {
/*       */             
/* 12062 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */           }
/* 12064 */           catch (NoSuchTemplateException e)
/*       */           {
/*       */             
/* 12067 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */           }
/*       */         
/*       */         }
/* 12071 */         else if (!isLight() || isLightOverride()) {
/* 12072 */           TempStates.checkForChange(parent, this, oldTemp, newTemp, qualityModifier);
/*       */         } 
/*       */       } 
/* 12075 */     } else if (this.temperature > 200) {
/*       */       
/* 12077 */       if (!isLight() || isLightOverride()) {
/*       */         
/* 12079 */         float speed = 1.0F;
/* 12080 */         if (insideStructure) {
/* 12081 */           speed *= 0.75F;
/* 12082 */         } else if (Server.getWeather().getRain() > 0.5D) {
/* 12083 */           speed *= 1.5F;
/* 12084 */         }  if (isInLunchbox()) {
/* 12085 */           coolItem(1);
/* 12086 */         } else if (parent.isAlwaysPoll()) {
/* 12087 */           setTemperature((short)(int)Math.max(200.0F, this.temperature - 20.0F * speed));
/*       */         } else {
/* 12089 */           setTemperature((short)(int)Math.max(200.0F, this.temperature - Zones.numberOfZones / 3.0F * speed));
/*       */         } 
/*       */       } else {
/*       */         
/* 12093 */         pollLightSource();
/*       */       } 
/*       */     } 
/* 12096 */     if (getTemperatureState(oldTemperature) != getTemperatureState(this.temperature)) {
/* 12097 */       notifyWatchersTempChange();
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isBurnable() {
/* 12102 */     return (!isLocked() && (isWood() || isCloth() || isMelting() || isLiquidInflammable() || isPaper()) && 
/* 12103 */       !isLiquidCooking() && getTemplateId() != 651 && getTemplateId() != 1097 && 
/* 12104 */       getTemplateId() != 1098 && getTemplateId() != 1392);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   void notifyWatchersTempChange() {
/* 12113 */     if (this.watchers == null) {
/*       */       return;
/*       */     }
/* 12116 */     for (Creature watcher : this.watchers)
/*       */     {
/* 12118 */       watcher.getCommunicator().sendUpdateInventoryItemTemperature(this);
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
/*       */   public byte getTemperatureState(short theTemperature) {
/*       */     byte result;
/* 12134 */     if (theTemperature < 0) { result = -1; }
/* 12135 */     else if (theTemperature < 400) { result = 0; }
/* 12136 */     else if (theTemperature < 1000) { result = 1; }
/* 12137 */     else if (theTemperature < 2000) { result = 2; }
/* 12138 */     else if (isLiquid()) { result = 3; }
/* 12139 */     else if (theTemperature < 3500) { result = 4; }
/* 12140 */     else { result = 5; }
/*       */     
/* 12142 */     return result;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private void createDaleItems() {
/*       */     try {
/* 12153 */       float creationQL = getQualityLevel() * getMaterialDaleModifier();
/* 12154 */       Item coal = ItemFactory.createItem(204, creationQL, null);
/* 12155 */       coal.setLastOwnerId(this.lastOwner);
/* 12156 */       if (getRarity() > 0 && 
/* 12157 */         Server.rand.nextInt(10) == 0)
/* 12158 */         coal.setRarity(getRarity()); 
/* 12159 */       insertItem(coal);
/* 12160 */       Item tar = ItemFactory.createItem(153, creationQL, null);
/* 12161 */       tar.setLastOwnerId(this.lastOwner);
/* 12162 */       if (getRarity() > 0 && 
/* 12163 */         Server.rand.nextInt(10) == 0)
/* 12164 */         tar.setRarity(getRarity()); 
/* 12165 */       insertItem(tar);
/* 12166 */       Item ash = ItemFactory.createItem(141, creationQL, null);
/* 12167 */       ash.setLastOwnerId(this.lastOwner);
/* 12168 */       insertItem(ash);
/* 12169 */       if (getRarity() > 0 && 
/* 12170 */         Server.rand.nextInt(10) == 0) {
/* 12171 */         ash.setRarity(getRarity());
/*       */       }
/* 12173 */     } catch (NoSuchTemplateException nst) {
/*       */       
/* 12175 */       logWarn("No template for ash?" + nst.getMessage(), (Throwable)nst);
/*       */     }
/* 12177 */     catch (FailedException fe) {
/*       */       
/* 12179 */       logWarn("What's this: " + fe.getMessage(), (Throwable)fe);
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
/*       */   
/*       */   public void deleteFireEffect() {
/* 12197 */     Effect toDelete = null;
/* 12198 */     if (this.effects != null) {
/*       */       
/* 12200 */       for (Iterator<Effect> it = this.effects.iterator(); it.hasNext(); ) {
/*       */         
/* 12202 */         Effect eff = it.next();
/* 12203 */         if (eff.getType() == 0)
/* 12204 */           toDelete = eff; 
/*       */       } 
/* 12206 */       if (toDelete != null) {
/* 12207 */         deleteEffect(toDelete);
/*       */       }
/*       */     } 
/*       */   }
/*       */   
/*       */   private void sendStatus() {
/* 12213 */     sendUpdate();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOnFire() {
/* 12218 */     if (isAlwaysLit())
/* 12219 */       return true; 
/* 12220 */     if (this.temperature < 600)
/* 12221 */       return false; 
/* 12222 */     if (getTemplateId() != 729) {
/* 12223 */       return true;
/*       */     }
/* 12225 */     return (getAuxData() > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Behaviour getBehaviour() throws NoSuchBehaviourException {
/* 12230 */     return this.template.getBehaviour();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final short getImageNumber() {
/* 12236 */     if (getTemplateId() == 1310) {
/*       */       
/*       */       try {
/*       */         
/* 12240 */         Creature storedCreature = Creatures.getInstance().getCreature(getData());
/* 12241 */         if (storedCreature.getTemplateId() == 45)
/*       */         {
/* 12243 */           return 404;
/*       */         }
/*       */       }
/* 12246 */       catch (NoSuchCreatureException ex) {
/*       */         
/* 12248 */         logger.log(Level.WARNING, ex.getMessage(), (Throwable)ex);
/*       */       } 
/*       */     }
/* 12251 */     if (getTemplateId() == 1307) {
/*       */       
/* 12253 */       if (getData1() <= 0) {
/* 12254 */         return 1460;
/*       */       }
/*       */       
/* 12257 */       switch (getMaterial()) {
/*       */         
/*       */         case 62:
/* 12260 */           return 1461;
/*       */         case 15:
/* 12262 */           return 1462;
/*       */         case 89:
/* 12264 */           return 1463;
/*       */         case 61:
/* 12266 */           return 1464;
/*       */         case 19:
/* 12268 */           return 1465;
/*       */         case 11:
/* 12270 */           return 1467;
/*       */         case 7:
/*       */         case 96:
/* 12273 */           return 1468;
/*       */         case 8:
/* 12275 */           return 1469;
/*       */         case 10:
/* 12277 */           return 1470;
/*       */         case 30:
/* 12279 */           return 1471;
/*       */         case 13:
/* 12281 */           return 1472;
/*       */         case 12:
/* 12283 */           return 1473;
/*       */         case 34:
/* 12285 */           return 1474;
/*       */       } 
/*       */       
/* 12288 */       if (MaterialUtilities.isMetal(getMaterial())) {
/* 12289 */         return 1467;
/*       */       }
/* 12291 */       if (MaterialUtilities.isWood(getMaterial())) {
/* 12292 */         return 1466;
/*       */       }
/* 12294 */       if (MaterialUtilities.isClay(getMaterial())) {
/* 12295 */         return 1465;
/*       */       }
/* 12297 */       if (getRealTemplate() != null) {
/* 12298 */         return getRealTemplate().getImageNumber();
/*       */       }
/* 12300 */       return 1460;
/*       */     } 
/*       */     
/* 12303 */     if (getTemplateId() == 1346) {
/*       */ 
/*       */       
/* 12306 */       if (isEmpty(false)) {
/* 12307 */         return 846;
/*       */       }
/*       */       
/* 12310 */       Item reel = this.items.iterator().next();
/* 12311 */       if (reel.isEmpty(false))
/* 12312 */         return 866; 
/* 12313 */       return 886;
/*       */     } 
/*       */     
/* 12316 */     if (getTemplateId() == 387) {
/*       */ 
/*       */       
/*       */       try {
/* 12320 */         ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(getData1());
/* 12321 */         return temp.getImageNumber();
/*       */       }
/* 12323 */       catch (NoSuchTemplateException noSuchTemplateException) {}
/*       */     }
/*       */     else {
/*       */       
/* 12327 */       if (this.realTemplate > 0 && getTemplate().useRealTemplateIcon()) {
/*       */         
/* 12329 */         if ((getRealTemplateId() == 92 || getRealTemplateId() == 368) && !isRaw())
/*       */         {
/* 12331 */           return 523;
/*       */         }
/* 12333 */         ItemTemplate realTemplate = getRealTemplate();
/* 12334 */         assert realTemplate != null;
/* 12335 */         return realTemplate.getImageNumber();
/*       */       } 
/* 12337 */       if ((getTemplateId() == 92 || getTemplateId() == 368) && !isRaw())
/*       */       {
/* 12339 */         return 523;
/*       */       }
/*       */     } 
/*       */     
/* 12343 */     Recipe recipe = getRecipe();
/* 12344 */     if (recipe != null)
/*       */     {
/* 12346 */       if (!recipe.getResultItem().isFoodGroup())
/* 12347 */         return recipe.getResultItem().getIcon(); 
/*       */     }
/* 12349 */     return this.template.getImageNumber();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOnSurface() {
/* 12354 */     if (getZoneId() != -10L)
/* 12355 */       return this.surfaced; 
/* 12356 */     if (getOwnerId() == -10L) {
/* 12357 */       return this.surfaced;
/*       */     }
/*       */     
/*       */     try {
/* 12361 */       Creature owner = Server.getInstance().getCreature(getOwnerId());
/* 12362 */       return owner.isOnSurface();
/*       */     }
/* 12364 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*       */ 
/*       */     
/* 12367 */     } catch (NoSuchPlayerException nsp) {
/*       */       
/* 12369 */       logInfo(this.id + " strange. Owner " + getOwnerId() + " is no creature or player.");
/*       */     } 
/*       */     
/* 12372 */     return this.surfaced;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private final String containsExamine(Creature performer) {
/* 12382 */     String toReturn = " ";
/* 12383 */     if (isFishingReel()) {
/*       */       
/* 12385 */       toReturn = toReturn + "The rod contains a " + getName() + ". ";
/* 12386 */       Item fishingLine = getFishingLine();
/* 12387 */       if (fishingLine == null)
/*       */       {
/*       */         
/* 12390 */         String lineName = getFishingLineName();
/* 12391 */         toReturn = toReturn + "Requires a " + lineName + " to be able to be used for fishing.";
/*       */       }
/*       */       else
/*       */       {
/* 12395 */         toReturn = toReturn + "The reel contains a " + fishingLine.getName() + ".";
/* 12396 */         toReturn = toReturn + fishingLine.containsExamine(performer);
/*       */       }
/*       */     
/* 12399 */     } else if (isFishingLine()) {
/*       */ 
/*       */       
/* 12402 */       Item fishingFloat = getFishingFloat();
/* 12403 */       Item fishingHook = getFishingHook();
/* 12404 */       if (fishingFloat == null && fishingHook == null) {
/*       */         
/* 12406 */         toReturn = toReturn + "The line requires a float and a fishing hook to be able to be used for fishing.";
/*       */       }
/*       */       else {
/*       */         
/* 12410 */         if (fishingFloat == null) {
/* 12411 */           toReturn = toReturn + "The line requires a float to be able to be used for fishing.";
/*       */         } else {
/* 12413 */           toReturn = toReturn + fishingFloat.containsExamine(performer);
/* 12414 */         }  if (fishingHook == null) {
/* 12415 */           toReturn = toReturn + "The line requires a fishing hook to be able to be used for fishing.";
/*       */         } else {
/* 12417 */           toReturn = toReturn + fishingHook.containsExamine(performer);
/*       */         } 
/*       */       } 
/* 12420 */     } else if (isFishingFloat()) {
/*       */       
/* 12422 */       toReturn = toReturn + "There is a " + getName() + " float on the line.";
/*       */     }
/* 12424 */     else if (isFishingHook()) {
/*       */       
/* 12426 */       toReturn = toReturn + "There is a " + getName() + " on the end of the line. ";
/*       */       
/* 12428 */       Item fishingBait = getFishingBait();
/* 12429 */       if (fishingBait == null) {
/* 12430 */         toReturn = toReturn + "There is no bait, but then again some fish like that!";
/*       */       } else {
/* 12432 */         toReturn = toReturn + fishingBait.containsExamine(performer);
/*       */       } 
/* 12434 */     } else if (isFishingBait()) {
/*       */       
/* 12436 */       toReturn = toReturn + "There is " + getName() + " as bait on the fishing hook.";
/*       */     } 
/* 12438 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public final String examine(Creature performer) {
/* 12443 */     String toReturn = this.template.getDescriptionLong();
/*       */     
/* 12445 */     if (getTemplateId() == 1311)
/*       */     {
/* 12447 */       if (!isEmpty(true)) {
/*       */         
/* 12449 */         toReturn = this.template.getDescriptionLong() + " There is a creature in this cage.";
/*       */       }
/*       */       else {
/*       */         
/* 12453 */         toReturn = this.template.getDescriptionLong() + " This cage is empty.";
/*       */       } 
/*       */     }
/* 12456 */     if (this.template.templateId == 133 && getRealTemplateId() == 1254)
/*       */     {
/* 12458 */       toReturn = toReturn.replace("tallow", "beeswax");
/*       */     }
/*       */     
/* 12461 */     boolean gotDesc = false;
/* 12462 */     Recipe recipe = getRecipe();
/* 12463 */     if (recipe != null) {
/*       */ 
/*       */       
/* 12466 */       String desc = recipe.getResultItem().getResultDescription(this);
/* 12467 */       if (desc.length() > 0 && !desc.startsWith("Any ")) {
/*       */         
/* 12469 */         toReturn = desc;
/* 12470 */         gotDesc = true;
/*       */       } 
/*       */     } else {
/* 12473 */       if (getTemplateId() == 1344) {
/*       */         
/* 12475 */         toReturn = toReturn + " It is made from " + getMaterialString(getMaterial()) + ".";
/* 12476 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */ 
/*       */ 
/*       */         
/* 12480 */         Item fishingLine = getFishingLine();
/* 12481 */         if (fishingLine == null) {
/* 12482 */           toReturn = toReturn + " Requires a basic fishing line to be able to be used for fishing.";
/*       */         } else {
/*       */           
/* 12485 */           toReturn = toReturn + " The pole has a " + fishingLine.getName() + " attached to the end of it.";
/* 12486 */           toReturn = toReturn + fishingLine.containsExamine(performer);
/*       */         } 
/* 12488 */         toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/* 12489 */         return toReturn;
/*       */       } 
/* 12491 */       if (getTemplateId() == 1346) {
/*       */         
/* 12493 */         toReturn = toReturn + " It is made from " + getMaterialString(getMaterial()) + ".";
/* 12494 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */ 
/*       */         
/* 12497 */         Item fishingReel = getFishingReel();
/* 12498 */         if (fishingReel == null) {
/* 12499 */           toReturn = toReturn + " Requires a fishing reel to be able to be used for fishing.";
/*       */         } else {
/* 12501 */           toReturn = toReturn + fishingReel.containsExamine(performer);
/* 12502 */         }  toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/* 12503 */         return toReturn;
/*       */       } 
/* 12505 */       if (isFishingReel()) {
/*       */         
/* 12507 */         toReturn = toReturn + " It is made from " + getMaterialString(getMaterial()) + ".";
/* 12508 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */ 
/*       */ 
/*       */         
/* 12512 */         Item fishingLine = getFishingLine();
/* 12513 */         if (fishingLine == null) {
/*       */           
/* 12515 */           String lineName = getFishingLineName();
/* 12516 */           toReturn = toReturn + " Requires a " + lineName + " to be able to be used for fishing.";
/*       */         }
/*       */         else {
/*       */           
/* 12520 */           toReturn = toReturn + " The reel contains a " + fishingLine.getName() + ".";
/* 12521 */           toReturn = toReturn + fishingLine.containsExamine(performer);
/*       */         } 
/* 12523 */         toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/* 12524 */         return toReturn;
/*       */       } 
/* 12526 */       if (isFishingLine()) {
/*       */         
/* 12528 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */ 
/*       */         
/* 12531 */         Item fishingFloat = getFishingFloat();
/* 12532 */         Item fishingHook = getFishingHook();
/* 12533 */         if (fishingFloat == null && fishingHook == null) {
/* 12534 */           toReturn = toReturn + " The line requires a float and a fishing hook to be able to be used for fishing.";
/*       */         } else {
/*       */           
/* 12537 */           if (fishingFloat == null) {
/* 12538 */             toReturn = toReturn + " The line requires a float to be able to be used for fishing.";
/*       */           } else {
/* 12540 */             toReturn = toReturn + fishingFloat.containsExamine(performer);
/* 12541 */           }  if (fishingHook == null) {
/* 12542 */             toReturn = toReturn + " The line requires a fishing hook to be able to be used for fishing.";
/*       */           } else {
/* 12544 */             toReturn = toReturn + fishingHook.containsExamine(performer);
/*       */           } 
/* 12546 */         }  toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/* 12547 */         return toReturn;
/*       */       } 
/* 12549 */       if (isFishingHook()) {
/*       */         
/* 12551 */         toReturn = toReturn + " It is made from " + getMaterialString(getMaterial()) + ".";
/* 12552 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */ 
/*       */ 
/*       */         
/* 12556 */         Item fishingBait = getFishingBait();
/* 12557 */         if (fishingBait == null) {
/* 12558 */           toReturn = toReturn + "There is no bait, but then again some fish like that!";
/*       */         } else {
/* 12560 */           toReturn = toReturn + fishingBait.containsExamine(performer);
/* 12561 */         }  toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/* 12562 */         return toReturn;
/*       */       } 
/* 12564 */       if (getTemplateId() == 1310) {
/*       */ 
/*       */         
/* 12567 */         Creature[] storedanimal = Creatures.getInstance().getCreatures();
/* 12568 */         for (Creature cret : storedanimal)
/*       */         {
/* 12570 */           if (cret.getWurmId() == getData())
/*       */           {
/*       */             
/* 12573 */             String exa = cret.examine();
/* 12574 */             performer.getCommunicator().sendNormalServerMessage(exa);
/*       */             
/* 12576 */             Brand brand = Creatures.getInstance().getBrand(getData());
/* 12577 */             if (brand != null) {
/*       */               
/*       */               try {
/*       */                 
/* 12581 */                 Village v = Villages.getVillage((int)brand.getBrandId());
/* 12582 */                 performer.getCommunicator().sendNormalServerMessage("It has been branded by and belongs to the settlement of " + v
/* 12583 */                     .getName() + ".");
/*       */               }
/* 12585 */               catch (NoSuchVillageException nsv) {
/*       */                 
/* 12587 */                 brand.deleteBrand();
/*       */               } 
/*       */             }
/*       */             
/* 12591 */             if (cret.isCaredFor()) {
/*       */               
/* 12593 */               long careTaker = cret.getCareTakerId();
/* 12594 */               PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(careTaker);
/* 12595 */               if (info != null)
/*       */               {
/* 12597 */                 performer.getCommunicator().sendNormalServerMessage("It is being taken care of by " + info
/* 12598 */                     .getName() + ".");
/*       */               }
/*       */             } 
/*       */             
/* 12602 */             performer.getCommunicator().sendNormalServerMessage(StringUtilities.raiseFirstLetter(cret
/* 12603 */                   .getStatus().getBodyType()));
/*       */             
/* 12605 */             if (cret.isDomestic())
/*       */             {
/* 12607 */               if (System.currentTimeMillis() - cret.getLastGroomed() > 172800000L)
/*       */               {
/* 12609 */                 performer.getCommunicator().sendNormalServerMessage("This creature could use some grooming.");
/*       */               }
/*       */             }
/*       */ 
/*       */             
/* 12614 */             if (cret.hasTraits()) {
/*       */               try {
/*       */                 double knowl;
/*       */                 
/* 12618 */                 Skill breeding = performer.getSkills().getSkill(10085);
/*       */                 
/* 12620 */                 if (performer.getPower() > 0) {
/*       */                   
/* 12622 */                   knowl = 99.99D;
/*       */                 }
/*       */                 else {
/*       */                   
/* 12626 */                   knowl = breeding.getKnowledge(0.0D);
/*       */                 } 
/* 12628 */                 if (knowl > 20.0D) {
/*       */                   
/* 12630 */                   StringBuilder buf = new StringBuilder();
/* 12631 */                   for (int x = 0; x < 64; x++) {
/*       */                     
/* 12633 */                     if (cret.hasTrait(x) && knowl - 20.0D > x) {
/*       */                       
/* 12635 */                       String l = Traits.getTraitString(x);
/* 12636 */                       if (l.length() > 0) {
/*       */                         
/* 12638 */                         buf.append(l);
/* 12639 */                         buf.append(' ');
/*       */                       } 
/*       */                     } 
/*       */                   } 
/* 12643 */                   if (buf.toString().length() > 0) {
/* 12644 */                     performer.getCommunicator().sendNormalServerMessage(buf.toString());
/*       */                   }
/*       */                 } 
/* 12647 */               } catch (NoSuchSkillException noSuchSkillException) {}
/*       */             }
/*       */ 
/*       */ 
/*       */ 
/*       */             
/* 12653 */             if (cret.isPregnant()) {
/*       */               
/* 12655 */               Offspring o = cret.getOffspring();
/* 12656 */               Random rand = new Random(cret.getWurmId());
/* 12657 */               int left = o.getDaysLeft() + rand.nextInt(3);
/* 12658 */               performer.getCommunicator().sendNormalServerMessage(
/* 12659 */                   LoginHandler.raiseFirstLetter(cret.getHeSheItString()) + " will deliver in about " + left + ((left != 1) ? " days." : " day."));
/*       */             } 
/*       */ 
/*       */             
/* 12663 */             String motherfather = "";
/* 12664 */             if (cret.getMother() != -10L) {
/*       */               
/*       */               try {
/*       */                 
/* 12668 */                 Creature mother = Server.getInstance().getCreature(cret.getMother());
/* 12669 */                 motherfather = motherfather + StringUtilities.raiseFirstLetter(cret.getHisHerItsString()) + " mother is " + mother.getNameWithGenus() + ". ";
/*       */               }
/* 12671 */               catch (NoSuchCreatureException|NoSuchPlayerException noSuchCreatureException) {}
/*       */             }
/*       */ 
/*       */ 
/*       */             
/* 12676 */             if (cret.getFather() != -10L) {
/*       */               
/*       */               try {
/*       */                 
/* 12680 */                 Creature father = Server.getInstance().getCreature(cret.getFather());
/* 12681 */                 motherfather = motherfather + StringUtilities.raiseFirstLetter(cret.getHisHerItsString()) + " father is " + father.getNameWithGenus() + ". ";
/*       */               }
/* 12683 */               catch (NoSuchCreatureException|NoSuchPlayerException noSuchCreatureException) {}
/*       */             }
/*       */ 
/*       */ 
/*       */             
/* 12688 */             if (motherfather.length() > 0)
/*       */             {
/* 12690 */               performer.getCommunicator().sendNormalServerMessage(motherfather);
/*       */             }
/* 12692 */             if (cret.getStatus().getBody().getWounds() != null) {
/*       */               
/* 12694 */               performer.getCommunicator().sendNormalServerMessage("This creature seems to be injured.");
/*       */             
/*       */             }
/*       */             else {
/*       */               
/* 12699 */               performer.getCommunicator().sendNormalServerMessage("This creature seems healthy without any noticeable ailments.");
/*       */             } 
/*       */             
/* 12702 */             if (cret.isHorse())
/*       */             {
/* 12704 */               performer.getCommunicator().sendNormalServerMessage("Its colour is " + cret.getColourName() + ".");
/*       */             }
/*       */           }
/*       */         
/*       */         }
/*       */       
/* 12710 */       } else if (getTemplateId() == 92 && isCooked()) {
/*       */         
/* 12712 */         String animal = (getMaterial() == 2) ? "animal" : getMaterialString(getMaterial());
/* 12713 */         toReturn = "Cooked meat that originally came from some kind of " + animal + ".";
/* 12714 */         gotDesc = true;
/*       */       }
/* 12716 */       else if (getTemplateId() == 368 && isCooked()) {
/*       */         
/* 12718 */         String animal = (getMaterial() == 2) ? "animal" : getMaterialString(getMaterial());
/* 12719 */         toReturn = "Cooked fillet of meat that originally came from some kind of " + animal + ".";
/* 12720 */         gotDesc = true;
/*       */       }
/* 12722 */       else if (getTemplate().getFoodGroup() == 1201 && isSteamed()) {
/*       */         
/* 12724 */         toReturn = "Steamed " + this.template.getName() + " with all its flavours sealed in.";
/* 12725 */         gotDesc = true;
/*       */       }
/* 12727 */       else if (getTemplateId() == 369 && isSteamed()) {
/*       */         
/* 12729 */         ItemTemplate it = getRealTemplate();
/* 12730 */         String fish = (it == null) ? "fish fillet" : (it.getName() + " fillet");
/* 12731 */         toReturn = "Steamed " + fish + " with all its flavours sealed in.";
/* 12732 */         gotDesc = true;
/*       */       }
/* 12734 */       else if (getTemplate().getFoodGroup() == 1156 && isSteamed()) {
/*       */         
/* 12736 */         toReturn = "Steamed " + this.template.getName() + " with all its flavours sealed in.";
/* 12737 */         gotDesc = true;
/*       */       } 
/* 12739 */     }  if (!gotDesc && this.template.descIsExam)
/*       */     {
/* 12741 */       if (this.description.length() > 0)
/* 12742 */         toReturn = this.description; 
/*       */     }
/* 12744 */     if (this.template.templateId == 386) {
/*       */       
/*       */       try
/*       */       {
/* 12748 */         toReturn = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDescriptionLong();
/*       */       }
/* 12750 */       catch (NoSuchTemplateException nst)
/*       */       {
/* 12752 */         logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */       }
/*       */     
/* 12755 */     } else if (this.material == 0) {
/*       */       
/* 12757 */       if (this.realTemplate > 0 && this.template.templateId != 1307) {
/*       */         try
/*       */         {
/*       */           
/* 12761 */           toReturn = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getDescriptionLong();
/*       */         }
/* 12763 */         catch (NoSuchTemplateException nst)
/*       */         {
/* 12765 */           logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */         }
/*       */       
/*       */       }
/* 12769 */     } else if (getTemplateId() == 861) {
/*       */       
/* 12771 */       if (Servers.localServer.PVPSERVER) {
/*       */         
/* 12773 */         toReturn = toReturn + " Anyone may take the stuff inside but at least you can lock it.";
/* 12774 */         toReturn = toReturn + " You may tie a creature to it but that does not keep it safe.";
/*       */       }
/*       */       else {
/*       */         
/* 12778 */         toReturn = toReturn + " Nobody may take the things inside even if it is unlocked.";
/* 12779 */         toReturn = toReturn + " You may tie a creature to it and nobody will be able to steal it.";
/*       */       } 
/* 12781 */       toReturn = toReturn + " If left on the ground, an undamaged tent will decay within a few weeks.";
/*       */     
/*       */     }
/* 12784 */     else if (this.id == 636451406482434L) {
/*       */       
/* 12786 */       toReturn = "A simple but beautiful shirt made from the finest threads of cloth. It's as black as night, except the yellow emblem which contains the symbol of a bat.";
/*       */     }
/* 12788 */     else if (this.id == 1810562858091778L) {
/*       */       
/* 12790 */       toReturn = "A long and slender pen. This pen is mightier than a sword.";
/*       */     }
/* 12792 */     else if (this.id == 357896990754562L) {
/*       */       
/* 12794 */       toReturn = "A relic of the days of old, with a tear of Libila stained on the blade.";
/*       */     }
/* 12796 */     else if (this.id == 156637289513474L) {
/*       */       
/* 12798 */       toReturn = "An exquisite wave pattern glimmers down the blade of this famous katana.";
/*       */     }
/* 12800 */     else if (this.id == 2207297627489538L) {
/*       */       
/* 12802 */       toReturn = "A well-worn shovel that trumps them all.";
/*       */     }
/* 12804 */     else if (this.id == 3343742719231234L) {
/*       */       
/* 12806 */       toReturn = "For some reason, your feel tears come to your eyes as you look upon it. Or is it blood?";
/*       */     }
/* 12808 */     else if (isUnstableRift()) {
/*       */       
/* 12810 */       toReturn = toReturn + " It is unstable and will disappear in about " + Server.getTimeFor(Math.max(0L, 1482227988600L - System.currentTimeMillis())) + ".";
/*       */     } 
/* 12812 */     if (getTemplateId() == 1307)
/*       */     {
/* 12814 */       if (getData1() <= 0) {
/*       */         
/* 12816 */         if (getAuxData() < 65) {
/* 12817 */           toReturn = toReturn + " A chisel";
/*       */         } else {
/* 12819 */           toReturn = toReturn + " A metal brush";
/* 12820 */         }  toReturn = toReturn + " would be useful to clear away some dirt and rock from it.";
/*       */       }
/* 12822 */       else if (getRealTemplate() != null) {
/*       */ 
/*       */         
/* 12825 */         toReturn = "A small fragment from " + getRealTemplate().getNameWithGenus() + ". You think you could recreate the " + getRealTemplate().getName() + " if you had a bit more material.";
/*       */       } 
/*       */     }
/*       */     
/* 12829 */     if (getRarity() > 0) {
/* 12830 */       toReturn = toReturn + MethodsItems.getRarityDesc(getRarity());
/*       */     }
/* 12832 */     if (isInventory()) {
/*       */       
/* 12834 */       int itemCount = getItemCount();
/* 12835 */       toReturn = this.template.getDescriptionLong() + " Your inventory is ";
/* 12836 */       if (itemCount <= 25) {
/* 12837 */         toReturn = toReturn + this.template.getDescriptionRotten() + ".";
/* 12838 */       } else if (itemCount <= 50) {
/* 12839 */         toReturn = toReturn + this.template.getDescriptionBad() + ".";
/* 12840 */       } else if (itemCount <= 75) {
/* 12841 */         toReturn = toReturn + this.template.getDescriptionNormal() + ".";
/* 12842 */       } else if (itemCount >= 100) {
/* 12843 */         toReturn = toReturn + "full.";
/*       */       } else {
/* 12845 */         toReturn = toReturn + this.template.getDescriptionSuperb() + ".";
/*       */       } 
/* 12847 */       if (itemCount != 1) {
/* 12848 */         toReturn = toReturn + " It contains " + itemCount + " items.";
/*       */       } else {
/* 12850 */         toReturn = toReturn + " It contains " + itemCount + " item.";
/* 12851 */       }  return toReturn;
/*       */     } 
/* 12853 */     if (getTemplateId() == 387) {
/*       */       
/*       */       try
/*       */       {
/* 12857 */         ItemTemplate temp = ItemTemplateFactory.getInstance().getTemplate(getData1());
/* 12858 */         toReturn = temp.getDescriptionLong() + " It looks strangely crooked.";
/*       */       }
/* 12860 */       catch (NoSuchTemplateException nst)
/*       */       {
/* 12862 */         toReturn = toReturn + " It looks strangely crooked.";
/* 12863 */         logWarn(getData1() + ": " + nst.getMessage(), (Throwable)nst);
/*       */       }
/*       */     
/* 12866 */     } else if (getTemplateId() != 74 && getTemplateId() != 37) {
/*       */       
/* 12868 */       String tempString = "";
/* 12869 */       switch (getTemperatureState(this.temperature)) {
/*       */         
/*       */         case -1:
/* 12872 */           tempString = " It is frozen.";
/*       */           break;
/*       */         case 2:
/* 12875 */           tempString = " It is hot.";
/*       */           break;
/*       */         case 1:
/* 12878 */           tempString = " It is very warm.";
/*       */           break;
/*       */         case 4:
/* 12881 */           tempString = " It is searing hot.";
/*       */           break;
/*       */         case 3:
/* 12884 */           tempString = " It is boiling.";
/*       */           break;
/*       */         case 5:
/* 12887 */           tempString = " It is glowing from the heat.";
/*       */           break;
/*       */       } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 12896 */       toReturn = toReturn + tempString;
/*       */     } 
/* 12898 */     if (this.template.templateId == 1272 || this.template.templateId == 748) {
/*       */       InscriptionData ins;
/* 12900 */       switch (getAuxData()) {
/*       */         
/*       */         case 0:
/* 12903 */           ins = getInscription();
/* 12904 */           if (ins != null && ins.hasBeenInscribed()) {
/* 12905 */             toReturn = toReturn + " It has a message on it!"; break;
/*       */           } 
/* 12907 */           toReturn = toReturn + " It is blank.";
/*       */           break;
/*       */         case 1:
/* 12910 */           toReturn = toReturn + " It has a recipe on it!";
/*       */           break;
/*       */         case 2:
/* 12913 */           toReturn = toReturn + " It has a waxed coating!";
/*       */           break;
/*       */       } 
/*       */     
/*       */     } 
/* 12918 */     if (getTemplateId() == 481) {
/*       */       String healString;
/*       */ 
/*       */       
/* 12922 */       if (this.auxbyte <= 0) { healString = " It is useless to heal with."; }
/* 12923 */       else if (this.auxbyte < 5) { healString = " It will help some against wounds."; }
/* 12924 */       else if (this.auxbyte < 10) { healString = " It will be pretty efficient against wounds."; }
/* 12925 */       else if (this.auxbyte < 15) { healString = " It will be good against wounds."; }
/* 12926 */       else if (this.auxbyte < 20) { healString = " It will be very good against wounds."; }
/* 12927 */       else { healString = " It is supreme against wounds."; }
/*       */       
/* 12929 */       toReturn = toReturn + healString;
/*       */     } 
/*       */     
/* 12932 */     if (isBowUnstringed() || isWeaponBow()) {
/*       */       
/* 12934 */       if (getMaterial() == 40) {
/* 12935 */         toReturn = toReturn + " The willow wood used in this bow gives it good strength yet supreme flexibility.";
/*       */       }
/* 12937 */     } else if (getTemplateId() == 455 || getTemplateId() == 454 || 
/* 12938 */       getTemplateId() == 456) {
/*       */       
/* 12940 */       if (getMaterial() == 39) {
/* 12941 */         toReturn = toReturn + " Cedar arrows are straighter and smoother than other arrows.";
/* 12942 */       } else if (getMaterial() == 41) {
/* 12943 */         toReturn = toReturn + " Maple arrows are smooth, uniform and take less damage than other arrows.";
/*       */       } 
/* 12945 */     } else if (getTemplateId() == 526) {
/*       */       
/* 12947 */       toReturn = toReturn + " It has " + getAuxData() + " charges left.";
/*       */     }
/* 12949 */     else if (isAbility()) {
/*       */       
/* 12951 */       if (getTemplateId() == 794) {
/* 12952 */         toReturn = toReturn + " If solved, something very dramatic will happen.";
/*       */       
/*       */       }
/* 12955 */       else if (getAuxData() == 2) {
/* 12956 */         toReturn = toReturn + " It has one charge left.";
/*       */       } else {
/* 12958 */         toReturn = toReturn + " It has " + (3 - getAuxData()) + " charges left.";
/*       */       }
/*       */     
/* 12961 */     } else if (getTemplateId() == 726) {
/*       */       
/* 12963 */       if (this.auxbyte != 0) {
/*       */         
/* 12965 */         Kingdom k = Kingdoms.getKingdom(this.auxbyte);
/* 12966 */         if (k != null) {
/*       */           
/* 12968 */           toReturn = toReturn + " This is where the people of " + k.getName() + " meet and resolve disputes.";
/* 12969 */           King king = King.getKing(this.auxbyte);
/* 12970 */           if (king != null) {
/*       */             
/* 12972 */             if (king.getChallengeAcceptedDate() > 0L) {
/*       */               
/* 12974 */               long nca = king.getChallengeAcceptedDate();
/* 12975 */               String sa = Server.getTimeFor(nca - System.currentTimeMillis());
/* 12976 */               toReturn = toReturn + " The ruler must show up in " + sa + ".";
/*       */             } 
/* 12978 */             if (king.getChallengeDate() > 0L) {
/*       */               
/* 12980 */               long nca = king.getChallengeDate();
/* 12981 */               String sa = Server.getTimeFor(System.currentTimeMillis() - nca);
/* 12982 */               toReturn = toReturn + " The ruler was challenged " + sa + " ago.";
/*       */             } 
/*       */             
/* 12985 */             long nc = king.getNextChallenge();
/* 12986 */             if (nc > System.currentTimeMillis()) {
/*       */               
/* 12988 */               String s = Server.getTimeFor(nc - System.currentTimeMillis());
/* 12989 */               toReturn = toReturn + " Next challenge avail in " + s + ".";
/*       */             } 
/* 12991 */             if (king.hasFailedAllChallenges()) {
/*       */               
/* 12993 */               toReturn = toReturn + " The " + king.getRulerTitle() + " has failed all challenges. Voting for removal is in progress.";
/*       */               
/* 12995 */               if ((((Player)performer).getSaveFile()).votedKing) {
/* 12996 */                 toReturn = toReturn + " You have already voted.";
/*       */               } else {
/* 12998 */                 toReturn = toReturn + " You may now vote for removal of the current ruler.";
/*       */               } 
/* 13000 */             }  if (performer.getPower() > 0) {
/*       */               
/* 13002 */               performer.getLogger().log(Level.INFO, performer
/* 13003 */                   .getName() + " examining " + k.getName() + " duel ring.");
/*       */               
/* 13005 */               toReturn = toReturn + " Challenges: " + king.getChallengeSize() + " Declined: " + king.getDeclinedChallengesNumber() + " Votes: " + king.getVotes() + ".";
/*       */             } 
/*       */           } else {
/*       */             
/* 13009 */             toReturn = toReturn + " There is no ruler.";
/*       */           } 
/*       */         } 
/*       */       } 
/* 13013 */     } else if (getTemplateId() == 740) {
/*       */       
/* 13015 */       toReturn = toReturn + " It has the head of a ";
/* 13016 */       switch (getAuxData() % 10) {
/*       */         
/*       */         case 0:
/* 13019 */           toReturn = toReturn + "dog"; break;
/* 13020 */         case 1: toReturn = toReturn + "pheasant"; break;
/* 13021 */         case 2: toReturn = toReturn + "stag"; break;
/* 13022 */         case 3: toReturn = toReturn + "bull"; break;
/* 13023 */         case 4: toReturn = toReturn + "dragon"; break;
/* 13024 */         case 5: toReturn = toReturn + "nymph"; break;
/* 13025 */         case 6: toReturn = toReturn + "two-headed giant"; break;
/* 13026 */         case 7: toReturn = toReturn + "bear"; break;
/* 13027 */         case 8: toReturn = toReturn + "demon"; break;
/* 13028 */         case 9: toReturn = toReturn + "rabbit"; break;
/* 13029 */         default: toReturn = toReturn + "dog";
/*       */           break;
/*       */       } 
/* 13032 */       toReturn = toReturn + " on it.";
/*       */     }
/* 13034 */     else if (getTemplateId() == 1076) {
/*       */       
/* 13036 */       if (getData1() > 0) {
/*       */         
/* 13038 */         toReturn = toReturn + " It has a";
/* 13039 */         switch (getData1()) {
/*       */           
/*       */           case 1:
/* 13042 */             if (getData2() > 50) {
/* 13043 */               toReturn = toReturn + " star sapphire inserted in the socket."; break;
/*       */             } 
/* 13045 */             toReturn = toReturn + " sapphire inserted in the socket.";
/*       */             break;
/*       */           case 2:
/* 13048 */             if (getData2() > 50) {
/* 13049 */               toReturn = toReturn + " star emerald inserted in the socket."; break;
/*       */             } 
/* 13051 */             toReturn = toReturn + "n emerald inserted in the socket.";
/*       */             break;
/*       */           case 3:
/* 13054 */             if (getData2() > 50) {
/* 13055 */               toReturn = toReturn + " star ruby inserted in the socket."; break;
/*       */             } 
/* 13057 */             toReturn = toReturn + " ruby inserted in the socket.";
/*       */             break;
/*       */           case 4:
/* 13060 */             if (getData2() > 50) {
/* 13061 */               toReturn = toReturn + " black opal inserted in the socket."; break;
/*       */             } 
/* 13063 */             toReturn = toReturn + "n opal inserted in the socket.";
/*       */             break;
/*       */           case 5:
/* 13066 */             if (getData2() > 50) {
/* 13067 */               toReturn = toReturn + " star diamond inserted in the socket."; break;
/*       */             } 
/* 13069 */             toReturn = toReturn + " diamond inserted in the socket.";
/*       */             break;
/*       */         } 
/*       */       
/*       */       } else {
/* 13074 */         toReturn = toReturn + " You could add a gem in the empty socket.";
/*       */       } 
/* 13076 */     } else if (getTemplateId() == 1077) {
/*       */       
/* 13078 */       if (getData1() > 0)
/* 13079 */         toReturn = toReturn + " It will improve skillgain for " + SkillSystem.getNameFor(getData1()) + "."; 
/*       */     } 
/* 13081 */     if (getColor() != -1)
/*       */     {
/* 13083 */       if (!isDragonArmour() || getColor2() == -1) {
/*       */         
/* 13085 */         toReturn = toReturn + " ";
/* 13086 */         if (isWood()) {
/*       */           
/* 13088 */           toReturn = toReturn + "Wood ";
/* 13089 */           toReturn = toReturn + MethodsItems.getColorDesc(getColor()).toLowerCase();
/*       */         } else {
/*       */           
/* 13092 */           toReturn = toReturn + MethodsItems.getColorDesc(getColor());
/*       */         } 
/*       */       }  } 
/* 13095 */     if (supportsSecondryColor() && getColor2() != -1) {
/*       */       
/* 13097 */       toReturn = toReturn + " ";
/* 13098 */       if (isDragonArmour()) {
/* 13099 */         toReturn = toReturn + MethodsItems.getColorDesc(getColor2());
/*       */       } else {
/*       */         
/* 13102 */         toReturn = toReturn + LoginHandler.raiseFirstLetter(getSecondryItemName());
/* 13103 */         toReturn = toReturn + MethodsItems.getColorDesc(getColor2()).toLowerCase();
/*       */       } 
/*       */     } 
/*       */     
/* 13107 */     if (this.lockid != -10L)
/*       */     {
/* 13109 */       if (!isKey()) {
/*       */         
/*       */         try {
/*       */           
/* 13113 */           Item lock = Items.getItem(this.lockid);
/* 13114 */           if (lock.isLocked()) {
/* 13115 */             toReturn = toReturn + " It is locked with a lock of " + lock.getLockStrength() + " quality.";
/*       */           } else {
/* 13117 */             toReturn = toReturn + " It has a lock of " + lock.getLockStrength() + " quality, which is unlocked.";
/*       */           } 
/* 13119 */         } catch (NoSuchItemException nsi) {
/*       */           
/* 13121 */           logWarn(this.id + " has a lock that can't be found: " + this.lockid, (Throwable)nsi);
/*       */         } 
/*       */       }
/*       */     }
/*       */     
/* 13126 */     if (getBless() != null)
/*       */     {
/* 13128 */       if (performer.getFaith() > 20.0F)
/*       */       {
/* 13130 */         if (performer.getFaith() < 30.0F) {
/* 13131 */           toReturn = toReturn + " It has an interesting aura.";
/* 13132 */         } else if (performer.getFaith() < 40.0F) {
/*       */           
/* 13134 */           if (getBless().isHateGod()) {
/* 13135 */             toReturn = toReturn + " It has a malevolent aura.";
/*       */           } else {
/* 13137 */             toReturn = toReturn + " It has a benevolent aura.";
/*       */           } 
/*       */         } else {
/* 13140 */           toReturn = toReturn + " It bears an aura of " + (getBless()).name + ".";
/*       */         } 
/*       */       }
/*       */     }
/* 13144 */     if (isWood() && !isSeedling())
/*       */     {
/* 13146 */       toReturn = toReturn + " It is made from " + getMaterialString(getMaterial()) + ".";
/*       */     }
/*       */     
/* 13149 */     if (isRoyal()) {
/*       */       
/* 13151 */       Kingdom k = Kingdoms.getKingdom(getKingdom());
/* 13152 */       if (k != null) {
/* 13153 */         toReturn = toReturn + " It belongs to the " + k.getName() + ".";
/*       */       }
/*       */     } 
/* 13156 */     if (getTemplate().isRune())
/*       */     {
/* 13158 */       if (RuneUtilities.isEnchantRune(this)) {
/*       */         
/* 13160 */         toReturn = toReturn + " It can be attached to " + RuneUtilities.getAttachmentTargets(this) + " and will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(this)) + ".";
/* 13161 */       } else if (RuneUtilities.getModifier(RuneUtilities.getEnchantForRune(this), RuneUtilities.ModifierEffect.SINGLE_COLOR) > 0.0F || (
/* 13162 */         RuneUtilities.getSpellForRune(this) != null && RuneUtilities.getSpellForRune(this).isTargetAnyItem() && 
/* 13163 */         !RuneUtilities.getSpellForRune(this).isTargetTile())) {
/*       */         
/* 13165 */         toReturn = toReturn + " It can be used on " + RuneUtilities.getAttachmentTargets(this) + " and will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(this)) + ".";
/*       */       } else {
/* 13167 */         toReturn = toReturn + " It will " + RuneUtilities.getRuneLongDesc(RuneUtilities.getEnchantForRune(this)) + ".";
/*       */       } 
/*       */     }
/* 13170 */     if (getTemplateId() == 1423)
/*       */     {
/* 13172 */       if (getData() != -1L)
/*       */       {
/* 13174 */         if (getAuxData() != 0) {
/*       */           
/* 13176 */           DeadVillage dv = Villages.getDeadVillage(getData());
/*       */           
/* 13178 */           toReturn = toReturn + dv.getDeedName();
/* 13179 */           if (getAuxBit(1)) {
/*       */             
/* 13181 */             toReturn = toReturn + " was founded by " + dv.getFounderName();
/* 13182 */             if (getAuxBit(3)) {
/* 13183 */               toReturn = toReturn + " and was inhabited for about " + DeadVillage.getTimeString(dv.getTotalAge(), false) + ".";
/*       */             } else {
/* 13185 */               toReturn = toReturn + ".";
/*       */             } 
/* 13187 */           } else if (getAuxBit(3)) {
/*       */             
/* 13189 */             toReturn = toReturn + " was inhabited for about " + DeadVillage.getTimeString(dv.getTotalAge(), false) + ".";
/*       */           } 
/*       */           
/* 13192 */           if (getAuxBit(2)) {
/*       */             
/* 13194 */             if (getAuxBit(1) || getAuxBit(3))
/* 13195 */               toReturn = toReturn + " It"; 
/* 13196 */             toReturn = toReturn + " has been abandoned for roughly " + DeadVillage.getTimeString(dv.getTimeSinceDisband(), false);
/* 13197 */             if (getAuxBit(0)) {
/* 13198 */               toReturn = toReturn + " and was last mayored by " + dv.getMayorName() + ".";
/*       */             } else {
/* 13200 */               toReturn = toReturn + ".";
/*       */             } 
/*       */           } else {
/*       */             
/* 13204 */             if (getAuxBit(1) || getAuxBit(3))
/* 13205 */               toReturn = toReturn + " It"; 
/* 13206 */             toReturn = toReturn + " was last mayored by " + dv.getMayorName() + ".";
/*       */           } 
/*       */         } 
/*       */       }
/*       */     }
/*       */     
/* 13212 */     if (!isNewbieItem() && !isChallengeNewbieItem())
/*       */     {
/* 13214 */       if (getTemplateId() != 1310)
/*       */       {
/* 13216 */         toReturn = toReturn + MethodsItems.getImpDesc(performer, this);
/*       */       }
/*       */     }
/*       */     
/* 13220 */     if (isArtifact())
/*       */     {
/* 13222 */       toReturn = toReturn + " It may drop on the ground if you log out.";
/*       */     }
/*       */     
/* 13225 */     if (getTemplateId() == 937 || getTemplateId() == 445)
/*       */     {
/*       */       
/* 13228 */       toReturn = toReturn + " It has been " + ((getTemplateId() == 937) ? "weighted " : "winched ") + getWinches() + " times and currently has a firing angle of about " + (45 + getAuxData() * 5) + " degrees.";
/*       */     }
/*       */     
/* 13231 */     if ((isDecoration() || isNoTake()) && this.ownerId == -10L)
/*       */     {
/* 13233 */       if (getTemplateId() != 1310)
/*       */       {
/* 13235 */         toReturn = toReturn + " Ql: " + this.qualityLevel + ", Dam: " + this.damage + ".";
/*       */       }
/*       */     }
/*       */ 
/*       */     
/* 13240 */     if (getTemplateId() == 866) {
/*       */       
/*       */       try {
/*       */         
/* 13244 */         toReturn = toReturn + " This came from the " + CreatureTemplateFactory.getInstance().getTemplate(getData2()).getName() + ".";
/*       */       }
/* 13246 */       catch (NoSuchCreatureTemplateException e) {
/*       */         
/* 13248 */         logger.warning(String.format("Item %s [id %s] does not have valid blood data.", new Object[] { getName(), Long.valueOf(getWurmId()) }));
/*       */       } 
/*       */     }
/*       */     
/* 13252 */     if (isGem() && getData1() > 0) {
/*       */       
/* 13254 */       int d = getData1();
/*       */       
/* 13256 */       if (d < 10) { toReturn = toReturn + " It emits faint power."; }
/* 13257 */       else if (d < 20) { toReturn = toReturn + " It emits some power."; }
/* 13258 */       else if (d < 50) { toReturn = toReturn + " It emits power."; }
/* 13259 */       else if (d < 100) { toReturn = toReturn + " It emits quite a lot of power."; }
/* 13260 */       else if (d < 150) { toReturn = toReturn + " It emits very much power."; }
/* 13261 */       else { toReturn = toReturn + " It emits huge amounts of power."; }
/*       */     
/*       */     } 
/*       */     
/* 13265 */     if (isArtifact()) {
/*       */       
/* 13267 */       toReturn = toReturn + " It ";
/*       */       
/* 13269 */       int powerPercent = (int)Math.floor((this.auxbyte * 1.0F / 30.0F * 100.0F));
/* 13270 */       if (!ArtifactBehaviour.mayUseItem(this, null))
/*       */       {
/* 13272 */         toReturn = toReturn + "seems dormant but ";
/*       */       }
/*       */       
/* 13275 */       if (powerPercent > 99) { toReturn = toReturn + "emits an enormous sense of power."; }
/* 13276 */       else if (powerPercent > 82) { toReturn = toReturn + "emits a huge sense of power."; }
/* 13277 */       else if (powerPercent > 65) { toReturn = toReturn + "emits a strong sense of power."; }
/* 13278 */       else if (powerPercent > 48) { toReturn = toReturn + "emits a fair sense of power."; }
/* 13279 */       else if (powerPercent > 31) { toReturn = toReturn + "emits some sense of power."; }
/* 13280 */       else if (powerPercent > 14) { toReturn = toReturn + "emits a weak sense of power."; }
/* 13281 */       else { toReturn = toReturn + "emits almost no sense of power."; }
/*       */ 
/*       */ 
/*       */       
/* 13285 */       if (this.auxbyte <= 20)
/*       */       {
/* 13287 */         if (this.auxbyte > 10) {
/* 13288 */           toReturn = toReturn + " It will need to be recharged at the huge altar eventually.";
/* 13289 */         } else if (this.auxbyte <= 10 && this.auxbyte > 0) {
/* 13290 */           toReturn = toReturn + " It will need to be recharged at the huge altar soon.";
/*       */         } else {
/* 13292 */           toReturn = toReturn + " It will need to be recharged at the huge altar immediately or it will disappear.";
/*       */         }  } 
/* 13294 */       if (performer.getPower() > 0)
/*       */       {
/* 13296 */         toReturn = toReturn + " " + this.auxbyte + " charges remain. (" + powerPercent + "%)";
/*       */       }
/*       */     } 
/* 13299 */     if (getTemplateId() == 538) {
/*       */       
/* 13301 */       if (King.getKing((byte)2) == null) {
/* 13302 */         toReturn = toReturn + " It is occupied by a sword.";
/*       */       }
/* 13304 */     } else if (getTemplateId() == 654) {
/*       */ 
/*       */ 
/*       */       
/* 13308 */       boolean needBless = (getBless() == null);
/*       */       
/* 13310 */       switch (getAuxData()) {
/*       */         
/*       */         case 1:
/* 13313 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a sand tile to a clay tile.";
/*       */           break;
/*       */         case 2:
/* 13316 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a grass or mycelium tile to a peat tile.";
/*       */           break;
/*       */         case 3:
/* 13319 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a steppe tile to a tar tile.";
/*       */           break;
/*       */         case 4:
/* 13322 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a clay tile to a dirt tile.";
/*       */           break;
/*       */         case 5:
/* 13325 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a peat tile to a dirt tile.";
/*       */           break;
/*       */         case 6:
/* 13328 */           toReturn = toReturn + (needBless ? " Once blessed can" : " Can") + " help convert a tar tile to a dirt tile.";
/*       */           break;
/*       */       } 
/*       */     
/* 13332 */     } else if (getTemplateId() == 1101) {
/*       */       
/* 13334 */       if (getAuxData() < 10) {
/*       */         
/* 13336 */         toReturn = toReturn + " The bottle has something like " + (10 - getAuxData()) + " drinks left.";
/*       */       } else {
/*       */         
/* 13339 */         toReturn = toReturn + " The bottle is empty.";
/*       */       } 
/* 13341 */     }  if (getTemplateId() == 1162) {
/*       */ 
/*       */       
/* 13344 */       String growing = "unknown";
/*       */       
/*       */       try {
/* 13347 */         growing = ItemTemplateFactory.getInstance().getTemplate(this.realTemplate).getName();
/*       */       }
/* 13349 */       catch (NoSuchTemplateException nst) {
/*       */         
/* 13351 */         logInfo("No template for " + getName() + ", id=" + this.realTemplate);
/*       */       } 
/*       */       
/* 13354 */       int age = getAuxData() & Byte.MAX_VALUE;
/* 13355 */       if (age == 0) {
/* 13356 */         toReturn = toReturn + " you see bare dirt, maybe its too early to see whats growing.";
/* 13357 */       } else if (age < 5) {
/* 13358 */         toReturn = toReturn + " you see some shoots poking through the dirt.";
/* 13359 */       } else if (age < 10) {
/* 13360 */         toReturn = toReturn + " you see some shoots of " + growing + ".";
/* 13361 */       } else if (age < 65) {
/* 13362 */         toReturn = toReturn + " you see some " + growing + " growing, looks in its prime of life.";
/* 13363 */       } else if (age < 75) {
/* 13364 */         toReturn = toReturn + " you see some " + growing + " growing, looks a bit old now.";
/* 13365 */       } else if (age < 95) {
/* 13366 */         toReturn = toReturn + " you see some " + growing + " growing, looks ready to be picked.";
/*       */       } else {
/* 13368 */         toReturn = toReturn + " you see woody " + growing + ", looks like it needs replacing.";
/*       */       } 
/* 13370 */     }  if ((getTemplateId() == 490 || getTemplateId() == 491) && 
/* 13371 */       getExtra() != -1L)
/* 13372 */       toReturn = toReturn + " It has a keep net attached to it for storing freshly caught fish."; 
/* 13373 */     if (isMooredBoat())
/* 13374 */       toReturn = toReturn + " It is moored here."; 
/* 13375 */     if (getTemplateId() == 464)
/*       */     {
/* 13377 */       if (getData1() > 0) {
/* 13378 */         toReturn = toReturn + " You sense it could be fertile.";
/*       */       } else {
/* 13380 */         toReturn = toReturn + " You sense that it is infertile.";
/*       */       }  } 
/* 13382 */     if (isFood() || isLiquid()) {
/*       */       
/* 13384 */       float nut = getNutritionLevel();
/* 13385 */       if (isWrapped()) {
/*       */         
/* 13387 */         if (canBePapyrusWrapped() || canBeClothWrapped()) {
/*       */           
/* 13389 */           toReturn = toReturn + " It has been wrapped to reduce decay.";
/* 13390 */           return toReturn;
/*       */         } 
/* 13392 */         if (canBeRawWrapped()) {
/*       */           
/* 13394 */           toReturn = toReturn + " It has been wrapped ready to cook in a cooker of some kind.";
/* 13395 */           return toReturn;
/*       */         } 
/*       */       } 
/*       */       
/* 13399 */       if (nut > 0.9D) {
/* 13400 */         toReturn = toReturn + " This has a high nutrition value.";
/* 13401 */       } else if (nut > 0.7D) {
/* 13402 */         toReturn = toReturn + " This has a good nutrition value.";
/* 13403 */       } else if (nut > 0.5D) {
/* 13404 */         toReturn = toReturn + " This has a medium nutrition value.";
/* 13405 */       } else if (nut > 0.3D) {
/* 13406 */         toReturn = toReturn + " This has a poor nutrition value.";
/*       */       } else {
/* 13408 */         toReturn = toReturn + " This has a very low nutrition value.";
/* 13409 */       }  if (isSalted()) {
/* 13410 */         toReturn = toReturn + " Tastes like it has some salt in it.";
/*       */       }
/* 13412 */       if (recipe != null)
/*       */       {
/* 13414 */         if (performer.getSkills().getSkillOrLearn(recipe.getSkillId()).getKnowledge(0.0D) > 30.0D) {
/*       */           
/* 13416 */           String creat = getCreatorName();
/* 13417 */           if (creat.length() > 0) {
/*       */ 
/*       */             
/* 13420 */             toReturn = toReturn + " Made by " + creat + " on " + WurmCalendar.getDateFor(this.creationDate);
/*       */           } else {
/*       */             
/* 13423 */             toReturn = toReturn + " Created on " + WurmCalendar.getDateFor(this.creationDate);
/*       */           } 
/*       */         } 
/*       */       }
/* 13427 */       if (performer.getPower() > 1 || performer.hasFlag(51))
/*       */       {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 13434 */         toReturn = toReturn + " (testers only: Calories:" + getCaloriesByWeight() + ", Carbs:" + getCarbsByWeight() + ", Fats:" + getFatsByWeight() + ", Proteins:" + getProteinsByWeight() + ", Bonus:" + (getBonus() & 0xFF) + ", Nutrition:" + (int)(nut * 100.0F) + "%" + ((recipe != null) ? (", Recipe:" + recipe.getName() + " (" + recipe.getRecipeId() + ")") : "") + ", Stages:" + getFoodStages() + ", Ingredients:" + getFoodIngredients() + ".)";
/*       */       }
/*       */     } 
/* 13437 */     if (getTemplateId() == 1175 || getTemplateId() == 1239)
/*       */     {
/* 13439 */       switch (getAuxData()) {
/*       */         
/*       */         case 0:
/* 13442 */           toReturn = toReturn + " You cannot hear or see any activity around the hive.";
/*       */           break;
/*       */         case 1:
/* 13445 */           toReturn = toReturn + " You see and hear bees flying in and around the hive.";
/*       */           break;
/*       */         case 2:
/* 13448 */           toReturn = toReturn + " You see and hear there is more than the usual activity in the hive, could be there is a queen about to leave.";
/*       */           break;
/*       */       } 
/*       */     }
/* 13452 */     if (WurmId.getType(this.lastOwner) == 1) {
/*       */       
/* 13454 */       Wagoner wagoner = Wagoner.getWagoner(this.lastOwner);
/* 13455 */       if (wagoner != null) {
/*       */         
/* 13457 */         toReturn = toReturn + " This is owned by " + wagoner.getName() + ".";
/* 13458 */         if (getTemplateId() == 1112)
/*       */         {
/* 13460 */           setData(-10L);
/*       */         }
/*       */       } 
/*       */     } 
/* 13464 */     return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public final byte getEnchantmentDamageType() {
/* 13469 */     if (this.enchantment == 91 || this.enchantment == 90 || this.enchantment == 92)
/*       */     {
/* 13471 */       return this.enchantment;
/*       */     }
/* 13473 */     return 0;
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
/*       */   public final void sendColoredSalveImbue(Communicator comm, String salveName, String damageType, byte color) {
/* 13486 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 13487 */     segments.add(new MulticolorLineSegment("It is imbued with special abilities from a ", (byte)0));
/* 13488 */     segments.add(new MulticolorLineSegment(salveName, (byte)16));
/* 13489 */     segments.add(new MulticolorLineSegment(" and will deal ", (byte)0));
/* 13490 */     segments.add(new MulticolorLineSegment(damageType, color));
/* 13491 */     segments.add(new MulticolorLineSegment(" damage.", (byte)0));
/* 13492 */     comm.sendColoredMessageEvent(segments);
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
/*       */   public final void sendColoredDemise(Communicator comm, String demiseName, String targetName) {
/* 13504 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 13505 */     segments.add(new MulticolorLineSegment("It is enchanted with ", (byte)0));
/* 13506 */     segments.add(new MulticolorLineSegment(demiseName, (byte)16));
/* 13507 */     segments.add(new MulticolorLineSegment(" and will be more effective against ", (byte)0));
/* 13508 */     segments.add(new MulticolorLineSegment(targetName, (byte)17));
/* 13509 */     segments.add(new MulticolorLineSegment(".", (byte)0));
/* 13510 */     comm.sendColoredMessageEvent(segments);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendColoredSmear(Communicator comm, SpellEffect eff) {
/* 13521 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 13522 */     segments.add(new MulticolorLineSegment("It has been smeared with a ", (byte)0));
/* 13523 */     segments.add(new MulticolorLineSegment(eff.getName(), (byte)16));
/* 13524 */     segments.add(new MulticolorLineSegment(", and it ", (byte)0));
/* 13525 */     segments.add(new MulticolorLineSegment(eff.getLongDesc(), (byte)17));
/* 13526 */     segments.add(new MulticolorLineSegment(" [", (byte)0));
/* 13527 */     segments.add(new MulticolorLineSegment(String.format("%d", new Object[] { Integer.valueOf((int)eff.getPower()) }), (byte)18));
/* 13528 */     segments.add(new MulticolorLineSegment("]", (byte)0));
/* 13529 */     comm.sendColoredMessageEvent(segments);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendColoredRune(Communicator comm, SpellEffect eff) {
/* 13540 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 13541 */     segments.add(new MulticolorLineSegment("A ", (byte)0));
/* 13542 */     segments.add(new MulticolorLineSegment(eff.getName(), (byte)16));
/* 13543 */     segments.add(new MulticolorLineSegment(" has been attached, so it ", (byte)0));
/* 13544 */     segments.add(new MulticolorLineSegment(eff.getLongDesc(), (byte)17));
/* 13545 */     comm.sendColoredMessageEvent(segments);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendColoredEnchant(Communicator comm, SpellEffect eff) {
/* 13556 */     ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
/* 13557 */     segments.add(new MulticolorLineSegment(eff.getName(), (byte)16));
/* 13558 */     segments.add(new MulticolorLineSegment(" has been cast on it, so it ", (byte)0));
/* 13559 */     segments.add(new MulticolorLineSegment(eff.getLongDesc(), (byte)17));
/* 13560 */     segments.add(new MulticolorLineSegment(" [", (byte)0));
/* 13561 */     segments.add(new MulticolorLineSegment(String.format("%d", new Object[] { Integer.valueOf((int)eff.getPower()) }), (byte)18));
/* 13562 */     segments.add(new MulticolorLineSegment("]", (byte)0));
/* 13563 */     comm.sendColoredMessageEvent(segments);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendEnchantmentStrings(Communicator comm) {
/* 13574 */     if (this.enchantment != 0)
/*       */     {
/*       */       
/* 13577 */       if (this.enchantment == 91) {
/*       */         
/* 13579 */         sendColoredSalveImbue(comm, "salve of fire", "fire", (byte)20);
/*       */       }
/* 13581 */       else if (this.enchantment == 92) {
/*       */         
/* 13583 */         sendColoredSalveImbue(comm, "salve of frost", "frost", (byte)21);
/*       */       }
/* 13585 */       else if (this.enchantment == 90) {
/*       */         
/* 13587 */         sendColoredSalveImbue(comm, "potion of acid", "acid", (byte)19);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       }
/*       */       else {
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 13601 */         Spell ench = Spells.getEnchantment(this.enchantment);
/* 13602 */         if (ench != null)
/*       */         {
/*       */           
/* 13605 */           if (ench == Spells.SPELL_DEMISE_ANIMAL) {
/*       */             
/* 13607 */             sendColoredDemise(comm, ench.getName(), "animals");
/*       */           }
/* 13609 */           else if (ench == Spells.SPELL_DEMISE_LEGENDARY) {
/*       */             
/* 13611 */             sendColoredDemise(comm, ench.getName(), "legendary creatures");
/*       */           }
/* 13613 */           else if (ench == Spells.SPELL_DEMISE_MONSTER) {
/*       */             
/* 13615 */             sendColoredDemise(comm, ench.getName(), "monsters");
/*       */           }
/* 13617 */           else if (ench == Spells.SPELL_DEMISE_HUMAN) {
/*       */             
/* 13619 */             sendColoredDemise(comm, ench.getName(), "humans");
/*       */           }
/*       */           else {
/*       */             
/* 13623 */             comm.sendNormalServerMessage("It is enchanted with " + ench.name + ", and " + ench.effectdesc);
/*       */           } 
/*       */         }
/*       */       } 
/*       */     }
/*       */     
/* 13629 */     ItemSpellEffects eff = getSpellEffects();
/*       */     
/* 13631 */     if (eff != null) {
/*       */       
/* 13633 */       SpellEffect[] speffs = eff.getEffects();
/* 13634 */       for (SpellEffect speff : speffs) {
/*       */         
/* 13636 */         if (speff.isSmeared()) {
/*       */           
/* 13638 */           sendColoredSmear(comm, speff);
/*       */ 
/*       */ 
/*       */         
/*       */         }
/* 13643 */         else if (speff.type < -10L) {
/*       */           
/* 13645 */           sendColoredRune(comm, speff);
/*       */         
/*       */         }
/*       */         else {
/*       */ 
/*       */           
/* 13651 */           sendColoredEnchant(comm, speff);
/*       */         } 
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void sendExtraStrings(Communicator comm) {
/* 13662 */     if (Features.Feature.TOWER_CHAINING.isEnabled())
/*       */     {
/* 13664 */       if (getTemplateId() == 236 || isKingdomMarker())
/*       */       {
/* 13666 */         if (isChained()) {
/*       */           
/* 13668 */           comm.sendNormalServerMessage(String.format("The %s is chained to the kingdom influence.", new Object[] {
/* 13669 */                   getName()
/*       */                 }));
/*       */         } else {
/*       */           
/* 13673 */           comm.sendNormalServerMessage(String.format("The %s is not chained to the kingdom influence.", new Object[] {
/* 13674 */                   getName()
/*       */                 }));
/*       */         } 
/*       */       }
/*       */     }
/*       */   }
/*       */   
/*       */   public final float getSpellEffectPower(byte aEnchantment) {
/* 13682 */     ItemSpellEffects eff = getSpellEffects();
/* 13683 */     if (eff != null) {
/*       */       
/* 13685 */       SpellEffect skillgain = eff.getSpellEffect(aEnchantment);
/* 13686 */       if (skillgain != null)
/* 13687 */         return skillgain.getPower(); 
/*       */     } 
/* 13689 */     return 0.0F;
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
/*       */   public final float getSkillSpellImprovement(int skillNum) {
/* 13701 */     switch (skillNum) {
/*       */       
/*       */       case 1014:
/* 13704 */         return getSpellEffectPower((byte)78);
/*       */       
/*       */       case 1008:
/* 13707 */         return getSpellEffectPower((byte)79);
/*       */       
/*       */       case 1016:
/*       */       case 10010:
/*       */       case 10011:
/* 13712 */         return getSpellEffectPower((byte)77);
/*       */       
/*       */       case 10016:
/* 13715 */         return getSpellEffectPower((byte)80);
/*       */       
/*       */       case 10012:
/*       */       case 10013:
/*       */       case 10014:
/* 13720 */         return getSpellEffectPower((byte)81);
/*       */       
/*       */       case 1031:
/*       */       case 1032:
/* 13724 */         return getSpellEffectPower((byte)82);
/*       */       
/*       */       case 10015:
/* 13727 */         return getSpellEffectPower((byte)83);
/*       */       
/*       */       case 10017:
/* 13730 */         return getSpellEffectPower((byte)84);
/*       */       
/*       */       case 10082:
/* 13733 */         return getSpellEffectPower((byte)85);
/*       */       
/*       */       case 10074:
/* 13736 */         return getSpellEffectPower((byte)86);
/*       */       
/*       */       case 1013:
/* 13739 */         return getSpellEffectPower((byte)87);
/*       */       
/*       */       case 1007:
/* 13742 */         return getSpellEffectPower((byte)88);
/*       */       
/*       */       case 1005:
/*       */       case 10044:
/* 13746 */         return getSpellEffectPower((byte)89);
/*       */       
/*       */       case 10059:
/* 13749 */         return getSpellEffectPower((byte)99);
/*       */     } 
/* 13751 */     return 0.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public final String getExamineAsBml(Creature performer) {
/* 13756 */     StringBuilder buf = new StringBuilder();
/*       */     
/* 13758 */     buf.append("text{text=\"" + examine(performer) + "\"};");
/* 13759 */     if (this.enchantment != 0) {
/*       */       
/* 13761 */       Spell ench = Spells.getEnchantment(this.enchantment);
/* 13762 */       if (ench != null)
/* 13763 */         buf.append("text{text=\"It is enchanted with " + ench.name + ", and " + ench.effectdesc + "\"};"); 
/*       */     } 
/* 13765 */     ItemSpellEffects eff = getSpellEffects();
/* 13766 */     if (eff != null) {
/*       */       
/* 13768 */       SpellEffect[] speffs = eff.getEffects();
/* 13769 */       for (SpellEffect speff : speffs)
/*       */       {
/* 13771 */         buf.append("text{text=\"" + speff.getName() + " has been cast on it, so it " + speff.getLongDesc() + " [" + (int)speff.power + "]\"};");
/*       */       }
/*       */     } 
/*       */     
/* 13775 */     return buf.toString();
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellSkillBonus() {
/* 13780 */     if (isArtifact() && isWeapon())
/* 13781 */       return 99.0F; 
/* 13782 */     if (getBonusForSpellEffect((byte)13) > 0.0F)
/* 13783 */       return getBonusForSpellEffect((byte)13); 
/* 13784 */     return getBonusForSpellEffect((byte)47);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getWeaponSpellDamageBonus() {
/* 13789 */     if (isArtifact() && getTemplateId() == 340)
/* 13790 */       return 99.0F; 
/* 13791 */     return getBonusForSpellEffect((byte)18) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellRotModifier() {
/* 13796 */     if (isArtifact() && getTemplateId() == 340)
/* 13797 */       return 99.0F; 
/* 13798 */     return getBonusForSpellEffect((byte)18) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellFrostDamageBonus() {
/* 13803 */     return getBonusForSpellEffect((byte)33) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellExtraDamageBonus() {
/* 13808 */     return getBonusForSpellEffect((byte)45) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellEssenceDrainModifier() {
/* 13813 */     return getBonusForSpellEffect((byte)63);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellLifeTransferModifier() {
/* 13818 */     if (isArtifact() && getTemplateId() == 337)
/* 13819 */       return 99.0F; 
/* 13820 */     return getBonusForSpellEffect((byte)26);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellMindStealModifier() {
/* 13825 */     return getBonusForSpellEffect((byte)31);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellNimbleness() {
/* 13830 */     if (isArtifact() && isWeapon())
/* 13831 */       return 99.0F; 
/* 13832 */     return getBonusForSpellEffect((byte)32);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellDamageBonus() {
/* 13837 */     if (isArtifact() && isWeaponSword())
/* 13838 */       return 99.0F; 
/* 13839 */     return getBonusForSpellEffect((byte)14) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellVenomBonus() {
/* 13844 */     return getBonusForSpellEffect((byte)27) * ItemBonus.getWeaponSpellDamageIncreaseBonus(this.ownerId);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellPainShare() {
/* 13849 */     if (isArtifact() && (isShield() || isArmour()))
/* 13850 */       return 80.0F; 
/* 13851 */     return getBonusForSpellEffect((byte)17);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getNolocateBonus() {
/* 13856 */     if (isArtifact() && 
/* 13857 */       getTemplateId() == 329)
/* 13858 */       return 99.0F; 
/* 13859 */     return getBonusForSpellEffect((byte)29);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellSlowdown() {
/* 13864 */     if ((isArtifact() && isShield()) || (isRoyal() && isArmour()))
/* 13865 */       return 99.0F; 
/* 13866 */     return getBonusForSpellEffect((byte)46);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellFoodBonus() {
/* 13871 */     return getBonusForSpellEffect((byte)15);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellLocFishBonus() {
/* 13876 */     return getBonusForSpellEffect((byte)48);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellLocEnemyBonus() {
/* 13881 */     return getBonusForSpellEffect((byte)50);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellLocChampBonus() {
/* 13886 */     return getBonusForSpellEffect((byte)49);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLocateItem() {
/* 13891 */     return (getBonusForSpellEffect((byte)50) > 0.0F || 
/* 13892 */       getBonusForSpellEffect((byte)48) > 0.0F || 
/* 13893 */       getBonusForSpellEffect((byte)49) > 0.0F);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellSpeedBonus() {
/* 13898 */     if (getBonusForSpellEffect((byte)16) > 0.0F)
/* 13899 */       return getBonusForSpellEffect((byte)16); 
/* 13900 */     return getBonusForSpellEffect((byte)47);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellCourierBonus() {
/* 13905 */     if (getBonusForSpellEffect((byte)20) <= 0.0F)
/* 13906 */       return getBonusForSpellEffect((byte)44); 
/* 13907 */     return getBonusForSpellEffect((byte)20);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getSpellDarkMessengerBonus() {
/* 13912 */     return getBonusForSpellEffect((byte)44);
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getBonusForSpellEffect(byte aEnchantment) {
/* 13917 */     ItemSpellEffects eff = getSpellEffects();
/* 13918 */     if (eff != null) {
/*       */       
/* 13920 */       SpellEffect skillgain = eff.getSpellEffect(aEnchantment);
/* 13921 */       if (skillgain != null)
/* 13922 */         return skillgain.power; 
/*       */     } 
/* 13924 */     return 0.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public final SpellEffect getSpellEffect(byte aEnchantment) {
/* 13929 */     ItemSpellEffects eff = getSpellEffects();
/* 13930 */     if (eff != null) {
/*       */       
/* 13932 */       SpellEffect skillgain = eff.getSpellEffect(aEnchantment);
/* 13933 */       if (skillgain != null)
/* 13934 */         return skillgain; 
/*       */     } 
/* 13936 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getDamagePercent() {
/* 13941 */     if (getWeaponSpellDamageBonus() > 0.0F)
/*       */     {
/* 13943 */       return this.template.getDamagePercent() + (int)(5.0F * getWeaponSpellDamageBonus() / 100.0F);
/*       */     }
/* 13945 */     return this.template.getDamagePercent();
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getFullWeight() {
/* 13950 */     return getFullWeight(false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getFullWeight(boolean calcCorrectBulkWeight) {
/* 13959 */     int lWeight = getWeightGrams();
/* 13960 */     if (calcCorrectBulkWeight && isBulkItem()) {
/*       */       
/* 13962 */       float nums = getBulkNumsFloat(true);
/* 13963 */       ItemTemplate temp = getRealTemplate();
/* 13964 */       if (temp != null)
/*       */       {
/* 13966 */         lWeight = (int)(temp.getWeightGrams() * nums);
/*       */       }
/*       */     } 
/* 13969 */     if (isHollow()) {
/*       */ 
/*       */       
/* 13972 */       Set<Item> allItems = getItems();
/* 13973 */       for (Item it : allItems) {
/*       */         
/* 13975 */         if (it != this) {
/* 13976 */           lWeight += it.getFullWeight(calcCorrectBulkWeight); continue;
/*       */         } 
/* 13978 */         logWarn(getName() + " Wurmid=" + getWurmId() + " contains itself!");
/*       */       } 
/*       */     } 
/* 13981 */     return lWeight;
/*       */   }
/*       */ 
/*       */   
/*       */   public final byte[] getBodySpaces() {
/* 13986 */     return this.template.getBodySpaces();
/*       */   }
/*       */ 
/*       */   
/*       */   public final long[] getKeyIds() {
/* 13991 */     if (this.keys == null || this.keys.isEmpty()) {
/* 13992 */       return EMPTY_LONG_PRIMITIVE_ARRAY;
/*       */     }
/*       */     
/* 13995 */     long[] keyids = new long[this.keys.size()];
/* 13996 */     int x = 0;
/* 13997 */     for (Long key : this.keys) {
/*       */       
/* 13999 */       keyids[x] = key.longValue();
/* 14000 */       x++;
/*       */     } 
/*       */     
/* 14003 */     return keyids;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void addKey(long keyid) {
/* 14008 */     if (this.keys == null) {
/* 14009 */       this.keys = new HashSet<>();
/*       */     }
/* 14011 */     if (!this.keys.contains(Long.valueOf(keyid))) {
/*       */       
/* 14013 */       this.keys.add(Long.valueOf(keyid));
/* 14014 */       addNewKey(keyid);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void removeKey(long keyid) {
/* 14021 */     if (this.keys == null) {
/*       */       return;
/*       */     }
/* 14024 */     if (!this.keys.contains(Long.valueOf(keyid))) {
/*       */       return;
/*       */     }
/*       */     
/* 14028 */     this.keys.remove(Long.valueOf(keyid));
/* 14029 */     removeNewKey(keyid);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isUnlockedBy(long keyId) {
/* 14039 */     if (this.keys != null && keyId != -10L)
/*       */     {
/* 14041 */       return this.keys.contains(Long.valueOf(keyId));
/*       */     }
/* 14043 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void lock() {
/* 14048 */     setLocked(true);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void unlock() {
/* 14053 */     setLocked(false);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isEquipmentSlot() {
/* 14058 */     return this.template.isEquipmentSlot();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isLocked() {
/* 14067 */     if (isKey())
/* 14068 */       return false; 
/* 14069 */     if (isLock()) {
/* 14070 */       return getLocked();
/*       */     }
/* 14072 */     if (!isLockable() || this.lockid == -10L) {
/* 14073 */       return false;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 14078 */       Item lock = Items.getItem(this.lockid);
/*       */ 
/*       */ 
/*       */ 
/*       */       
/* 14083 */       boolean isAffectedLock = (lock.getTemplateId() == 568 || lock.getTemplateId() == 194 || lock.getTemplateId() == 193);
/*       */       
/* 14085 */       if (!lock.isLocked() && isAffectedLock) {
/*       */         
/* 14087 */         logInfo(getName() + "(" + getWurmId() + ") had lock (" + lock.getWurmId() + ") that was unlocked. So was auto-locked as should not have been in that state.");
/*       */         
/* 14089 */         lock.setLocked(true);
/*       */       } 
/*       */       
/* 14092 */       return lock.isLocked();
/*       */     }
/* 14094 */     catch (NoSuchItemException e) {
/*       */ 
/*       */       
/* 14097 */       logWarn(getName() + "," + getWurmId() + ":" + e.getMessage(), (Throwable)e);
/*       */ 
/*       */       
/* 14100 */       return false;
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
/*       */   public boolean isServerPortal() {
/* 14115 */     return this.template.isServerPortal;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isColor() {
/* 14120 */     return this.template.isColor;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateIsColorable() {
/* 14125 */     return this.template.colorable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPuppet() {
/* 14130 */     return this.template.puppet;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOverrideNonEnchantable() {
/* 14135 */     return this.template.overrideNonEnchantable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getDragonColor() {
/* 14140 */     int creatureTemplate = getData2();
/* 14141 */     switch (creatureTemplate) {
/*       */       
/*       */       case 16:
/*       */       case 103:
/* 14145 */         return WurmColor.createColor(215, 40, 40);
/*       */       
/*       */       case 18:
/*       */       case 89:
/* 14149 */         return WurmColor.createColor(10, 10, 10);
/*       */       
/*       */       case 17:
/*       */       case 90:
/* 14153 */         return WurmColor.createColor(10, 210, 10);
/*       */       
/*       */       case 19:
/*       */       case 92:
/* 14157 */         return WurmColor.createColor(255, 255, 255);
/*       */       
/*       */       case 91:
/*       */       case 104:
/* 14161 */         return WurmColor.createColor(40, 40, 215);
/*       */     } 
/*       */     
/* 14164 */     return WurmColor.createColor(100, 100, 100);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final String getDragonColorNameByColor(int color) {
/* 14172 */     if (color == WurmColor.createColor(215, 40, 40)) return "red"; 
/* 14173 */     if (color == WurmColor.createColor(10, 10, 10)) return "black"; 
/* 14174 */     if (color == WurmColor.createColor(10, 210, 10)) return "green"; 
/* 14175 */     if (color == WurmColor.createColor(255, 255, 255)) return "white"; 
/* 14176 */     if (color == WurmColor.createColor(40, 40, 215)) return "blue";
/*       */ 
/*       */     
/* 14179 */     return "";
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final String getDragonColorName() {
/*       */     try {
/* 14186 */       CreatureTemplate temp = CreatureTemplateFactory.getInstance().getTemplate(getData2());
/* 14187 */       String pre = temp.getName();
/* 14188 */       StringTokenizer st = new StringTokenizer(pre);
/* 14189 */       pre = st.nextToken();
/* 14190 */       return pre.toLowerCase();
/*       */     }
/* 14192 */     catch (Exception exception) {
/*       */ 
/*       */       
/* 14195 */       return "";
/*       */     } 
/*       */   }
/*       */   
/*       */   public final String getLockStrength() {
/* 14200 */     String lockStrength = "fantastic";
/* 14201 */     float lQualityLevel = getCurrentQualityLevel();
/*       */ 
/*       */     
/* 14204 */     int qlDivTen = (int)lQualityLevel / 10;
/*       */     
/* 14206 */     switch (qlDivTen) { case 0:
/* 14207 */         lockStrength = "very poor";
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 14219 */         return lockStrength;case 1: lockStrength = "poor"; return lockStrength;case 2: lockStrength = "below average"; return lockStrength;case 3: lockStrength = "okay"; return lockStrength;case 4: lockStrength = "above average"; return lockStrength;case 5: lockStrength = "pretty good"; return lockStrength;case 6: lockStrength = "good"; return lockStrength;case 7: lockStrength = "very good"; return lockStrength;case 8: lockStrength = "exceptional"; return lockStrength; }  lockStrength = "fantastic"; return lockStrength;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setSizes(int aSizeX, int aSizeY, int aSizeZ) {
/* 14224 */     if (aSizeX == 0 || aSizeY == 0 || aSizeZ == 0) {
/*       */       
/* 14226 */       Items.destroyItem(this.id);
/*       */       
/*       */       return;
/*       */     } 
/* 14230 */     setSizeX(aSizeX);
/* 14231 */     setSizeY(aSizeY);
/* 14232 */     setSizeZ(aSizeZ);
/*       */     
/* 14234 */     sendStatus();
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
/*       */   final boolean depleteSizeWith(int aSizeX, int aSizeY, int aSizeZ) {
/* 14262 */     int prevSizeX = getSizeX();
/* 14263 */     int prevSizeY = getSizeY();
/* 14264 */     int prevSizeZ = getSizeZ();
/*       */     
/* 14266 */     int prevol = prevSizeX * prevSizeY * prevSizeZ;
/* 14267 */     int vol = aSizeX * aSizeY * aSizeZ;
/*       */     
/* 14269 */     if (logger.isLoggable(Level.FINER))
/*       */     {
/* 14271 */       logger.finer("id: " + this.id + ", vol: " + vol + " prevol: " + prevol + ' ' + prevSizeX + ' ' + prevSizeY + ' ' + prevSizeZ);
/*       */     }
/*       */ 
/*       */     
/* 14275 */     if (vol >= prevol) {
/*       */       
/* 14277 */       Items.destroyItem(this.id);
/* 14278 */       return true;
/*       */     } 
/*       */     
/* 14281 */     float factor = vol / prevol;
/*       */     
/* 14283 */     prevSizeX = Math.max(1, prevSizeX - (int)(prevSizeX * factor));
/* 14284 */     prevSizeY = Math.max(1, prevSizeY - (int)(prevSizeY * factor));
/* 14285 */     prevSizeZ = Math.max(1, prevSizeZ - (int)(prevSizeZ * factor));
/*       */     
/* 14287 */     int newPrevSz = prevSizeZ;
/* 14288 */     int newPrevSy = prevSizeY;
/* 14289 */     int newPrevSx = prevSizeX;
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 14294 */     if (prevSizeZ < prevSizeY) {
/*       */ 
/*       */       
/* 14297 */       newPrevSz = prevSizeY;
/* 14298 */       newPrevSy = prevSizeZ;
/*       */     } 
/*       */     
/* 14301 */     if (prevSizeZ < prevSizeX) {
/*       */ 
/*       */       
/* 14304 */       newPrevSx = prevSizeZ;
/* 14305 */       newPrevSy = prevSizeX;
/*       */     } 
/*       */     
/* 14308 */     if (prevSizeY < prevSizeX) {
/*       */ 
/*       */       
/* 14311 */       newPrevSy = prevSizeY;
/* 14312 */       newPrevSz = prevSizeX;
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 14318 */     setSizeX(newPrevSx);
/* 14319 */     setSizeY(newPrevSy);
/* 14320 */     setSizeZ(newPrevSz);
/*       */     
/* 14322 */     sendStatus();
/* 14323 */     return false;
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
/*       */   
/*       */   public final void sleep(Creature sleeper, boolean epicServer) throws IOException {
/* 14356 */     if (this.template.artifact && sleeper != null) {
/*       */       
/* 14358 */       if (getOwnerId() == sleeper.getWurmId())
/*       */       {
/* 14360 */         sleeper.dropItem(this);
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       return;
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 14380 */     if (isHollow()) {
/*       */ 
/*       */       
/* 14383 */       Item[] allItems = getAllItems(true, false);
/* 14384 */       for (Item allit : allItems) {
/*       */         
/* 14386 */         if (allit.hasDroppableItem(epicServer)) {
/* 14387 */           allit.sleep(sleeper, epicServer);
/*       */         } else {
/* 14389 */           allit.sleepNonRecursive(sleeper, epicServer);
/*       */         } 
/*       */       } 
/*       */     } 
/* 14393 */     if (!this.template.alwaysLoaded)
/*       */     {
/* 14395 */       Items.removeItem(this.id);
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
/*       */   public final boolean hasDroppableItem(boolean epicServer) {
/* 14407 */     if (!isHollow()) {
/* 14408 */       return false;
/*       */     }
/* 14410 */     for (Item i : this.items) {
/*       */       
/* 14412 */       if (i.isArtifact()) {
/* 14413 */         return true;
/*       */       }
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/* 14419 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void sleepNonRecursive(Creature sleeper, boolean epicServer) throws IOException {
/* 14424 */     if (this.template.artifact && sleeper != null) {
/*       */       
/* 14426 */       if (getOwnerId() == sleeper.getWurmId())
/*       */       {
/* 14428 */         sleeper.dropItem(this);
/*       */       }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */       
/*       */       return;
/*       */     } 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */     
/* 14448 */     if (!this.template.alwaysLoaded)
/*       */     {
/* 14450 */       Items.removeItem(this.id);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item[] getAllItems(boolean getLockedItems) {
/* 14456 */     return getAllItems(getLockedItems, true);
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
/*       */   @Nonnull
/*       */   public final Item[] getAllItems(boolean getLockedItems, boolean loadArtifacts) {
/* 14478 */     if (!isHollow()) {
/* 14479 */       return emptyItems;
/*       */     }
/* 14481 */     if (this.lockid != -10L && !getLockedItems) {
/* 14482 */       return emptyItems;
/*       */     }
/* 14484 */     if (!loadArtifacts && this.template.artifact) {
/* 14485 */       return emptyItems;
/*       */     }
/*       */     
/* 14488 */     Set<Item> allItems = new HashSet<>();
/* 14489 */     for (Item item : getItems()) {
/*       */       
/* 14491 */       allItems.add(item);
/*       */       
/* 14493 */       Collections.addAll(allItems, item.getAllItems(getLockedItems, loadArtifacts));
/*       */     } 
/* 14495 */     return allItems.<Item>toArray(new Item[allItems.size()]);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item findFirstContainedItem(int templateid) {
/* 14500 */     Item[] its = getAllItems(false);
/* 14501 */     for (int x = 0; x < its.length; x++) {
/*       */       
/* 14503 */       if (its[x].getTemplateId() == templateid)
/* 14504 */         return its[x]; 
/*       */     } 
/* 14506 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public final Item findItem(int templateid) {
/* 14512 */     for (Item item : getItems()) {
/*       */       
/* 14514 */       if (item.getTemplateId() == templateid) {
/* 14515 */         return item;
/*       */       }
/*       */     } 
/* 14518 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item findItem(int templateid, boolean searchInGroups) {
/* 14523 */     for (Item item : getItems()) {
/*       */       
/* 14525 */       if (item.getTemplateId() == templateid) {
/* 14526 */         return item;
/*       */       }
/* 14528 */       if (item.isInventoryGroup() && searchInGroups) {
/*       */         
/* 14530 */         Item inGroup = item.findItem(templateid, false);
/* 14531 */         if (inGroup != null) {
/* 14532 */           return inGroup;
/*       */         }
/*       */       } 
/*       */     } 
/* 14536 */     return null;
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
/*       */   public final float getCurrentQualityLevel() {
/* 14567 */     return this.qualityLevel * Math.max(1.0F, 100.0F - this.damage) / 100.0F;
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
/*       */   public final byte getRadius() {
/* 14579 */     if (getSpellEffects() != null) {
/*       */       
/* 14581 */       float modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_GLOW) - 1.0F;
/* 14582 */       if (modifier > 0.0F)
/*       */       {
/* 14584 */         return (byte)(int)Math.min(127.0F, 127.0F * modifier - 20.0F);
/*       */       }
/*       */     } 
/*       */     
/* 14588 */     float ql = getCurrentQualityLevel();
/* 14589 */     boolean qlAbove20 = (ql > 20.0F);
/* 14590 */     float baseRange = ql / (qlAbove20 ? 100.0F : 20.0F);
/* 14591 */     if (isLightBright())
/*       */     {
/* 14593 */       return (byte)(int)(qlAbove20 ? (baseRange * 127.0F) : ((baseRange - 1.0F) * 32.0F));
/*       */     }
/* 14595 */     return (byte)(int)(qlAbove20 ? (baseRange * 64.0F) : ((baseRange - 1.0F) * 64.0F));
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
/*       */   public final boolean isCrystal() {
/* 14608 */     return (this.material == 52 || isDiamond());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDiamond() {
/* 14613 */     return (this.material == 54);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getMaterialDaleModifier() {
/* 14621 */     switch (getMaterial()) {
/*       */       
/*       */       case 64:
/* 14624 */         return 1.1F;
/*       */     } 
/* 14626 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   private float getMaterialDamageModifier() {
/* 14631 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*       */       
/* 14633 */       switch (getMaterial()) {
/*       */         
/*       */         case 56:
/* 14636 */           return 0.4F;
/*       */         case 30:
/* 14638 */           return 0.95F;
/*       */         case 31:
/* 14640 */           return 0.9F;
/*       */         case 10:
/* 14642 */           return 1.15F;
/*       */         case 57:
/* 14644 */           return 0.6F;
/*       */         case 7:
/* 14646 */           return 1.2F;
/*       */         case 12:
/* 14648 */           return 1.3F;
/*       */         case 67:
/* 14650 */           return 0.5F;
/*       */         case 8:
/* 14652 */           return 1.025F;
/*       */         case 9:
/* 14654 */           return 0.8F;
/*       */         case 34:
/* 14656 */           return 1.2F;
/*       */         case 13:
/* 14658 */           return 1.25F;
/*       */         case 96:
/* 14660 */           return 0.9F;
/*       */         
/*       */         case 38:
/* 14663 */           return 0.8F;
/*       */         
/*       */         case 35:
/* 14666 */           return 0.2F;
/*       */       } 
/*       */ 
/*       */     
/*       */     } else {
/* 14671 */       if (getMaterial() == 9)
/* 14672 */         return 0.8F; 
/* 14673 */       if (getMaterial() == 57)
/* 14674 */         return 0.6F; 
/* 14675 */       if (getMaterial() == 56)
/* 14676 */         return 0.4F; 
/*       */     } 
/* 14678 */     if (isFishingLine())
/*       */     {
/* 14680 */       switch (getTemplateId()) {
/*       */         
/*       */         case 1347:
/* 14683 */           return 1.2F;
/*       */         case 1348:
/* 14685 */           return 1.0F;
/*       */         case 1349:
/* 14687 */           return 0.9F;
/*       */         case 1350:
/* 14689 */           return 0.8F;
/*       */         case 1351:
/* 14691 */           return 0.7F;
/*       */       } 
/*       */     }
/* 14694 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   private float getMaterialDecayModifier() {
/* 14699 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*       */       
/* 14701 */       switch (getMaterial()) {
/*       */         
/*       */         case 56:
/* 14704 */           return 0.4F;
/*       */         case 30:
/* 14706 */           return 0.95F;
/*       */         case 31:
/* 14708 */           return 0.85F;
/*       */         case 10:
/* 14710 */           return 0.95F;
/*       */         case 57:
/* 14712 */           return 0.6F;
/*       */         case 7:
/* 14714 */           return 0.4F;
/*       */         case 12:
/* 14716 */           return 0.8F;
/*       */         case 67:
/* 14718 */           return 0.5F;
/*       */         case 8:
/* 14720 */           return 0.7F;
/*       */         case 9:
/* 14722 */           return 0.7F;
/*       */         case 34:
/* 14724 */           return 0.925F;
/*       */         case 13:
/* 14726 */           return 1.2F;
/*       */         case 96:
/* 14728 */           return 0.8F;
/*       */         
/*       */         case 38:
/* 14731 */           return 0.8F;
/*       */         
/*       */         case 35:
/* 14734 */           return 0.9F;
/*       */       } 
/*       */ 
/*       */     
/*       */     } else {
/* 14739 */       if (getMaterial() == 9)
/* 14740 */         return 0.8F; 
/* 14741 */       if (getMaterial() == 57)
/* 14742 */         return 0.6F; 
/* 14743 */       if (getMaterial() == 56) {
/* 14744 */         return 0.4F;
/*       */       }
/*       */     } 
/* 14747 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getMaterialFlexibiltyModifier() {
/* 14757 */     switch (getMaterial()) {
/*       */       
/*       */       case 40:
/* 14760 */         return 0.7F;
/*       */     } 
/* 14762 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getMaterialDecayTimeModifier() {
/* 14770 */     switch (getMaterial()) {
/*       */       
/*       */       case 39:
/* 14773 */         return 1.5F;
/*       */     } 
/* 14775 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMaterialBowDifficulty() {
/* 14783 */     switch (getMaterial()) {
/*       */       
/*       */       case 40:
/* 14786 */         return 5;
/*       */     } 
/* 14788 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMaterialArrowDifficulty() {
/* 14796 */     switch (getMaterial()) {
/*       */       
/*       */       case 39:
/* 14799 */         return 5;
/*       */       case 41:
/* 14801 */         return 3;
/*       */     } 
/* 14803 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getMaterialArrowDamageModifier() {
/* 14811 */     switch (getMaterial()) {
/*       */       
/*       */       case 41:
/* 14814 */         return 0.8F;
/*       */     } 
/* 14816 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private float getMaterialAgingModifier() {
/* 14824 */     switch (getMaterial()) {
/*       */       
/*       */       case 38:
/* 14827 */         return 1.1F;
/*       */     } 
/* 14829 */     return 1.05F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getMaterialFragrantModifier() {
/* 14839 */     switch (getMaterial()) {
/*       */       
/*       */       case 39:
/* 14842 */         return 0.9F;
/*       */       case 65:
/* 14844 */         return 0.85F;
/*       */       case 37:
/* 14846 */         return 0.95F;
/*       */       case 43:
/* 14848 */         return 0.75F;
/*       */       case 88:
/* 14850 */         return 0.75F;
/*       */       case 42:
/* 14852 */         return 0.8F;
/*       */       case 51:
/* 14854 */         return 0.8F;
/*       */     } 
/* 14856 */     return 1.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final float getDamageModifier() {
/* 14866 */     return getDamageModifier(false);
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
/*       */   public final float getDamageModifier(boolean decayDamage) {
/* 14878 */     return getDamageModifier(decayDamage, false);
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
/*       */   public final float getDamageModifier(boolean decayDamage, boolean flexibilityDamage) {
/* 14891 */     float rotMod = 1.0F;
/* 14892 */     float materialMod = 1.0F;
/*       */     
/* 14894 */     if (decayDamage) {
/* 14895 */       materialMod *= getMaterialDecayModifier();
/* 14896 */     } else if (flexibilityDamage) {
/* 14897 */       materialMod *= getMaterialFlexibiltyModifier();
/*       */     } else {
/* 14899 */       materialMod *= getMaterialDamageModifier();
/*       */     } 
/* 14901 */     if (getSpellRotModifier() > 0.0F)
/*       */     {
/* 14903 */       rotMod += getSpellRotModifier() / 100.0F;
/*       */     }
/* 14905 */     if (isCrude())
/* 14906 */       rotMod *= 10.0F; 
/* 14907 */     if (isCrystal()) {
/* 14908 */       rotMod *= 0.1F;
/* 14909 */     } else if (isFood()) {
/*       */       
/* 14911 */       if (isHighNutrition())
/* 14912 */         rotMod += (isSalted() ? 5 : 10); 
/* 14913 */       if (isGoodNutrition())
/* 14914 */         rotMod += (isSalted() ? 2 : 5); 
/* 14915 */       if (isMediumNutrition())
/* 14916 */         rotMod = (float)(rotMod + (isSalted() ? 1.5D : 3.0D)); 
/*       */     } 
/* 14918 */     if (isInTacklebox())
/*       */     {
/* 14920 */       rotMod *= 0.5F;
/*       */     }
/* 14922 */     Item parent = getParentOrNull();
/* 14923 */     if (parent != null && parent.getTemplateId() == 1342) {
/* 14924 */       rotMod *= 0.5F;
/*       */     }
/* 14926 */     if (getRarity() > 0) {
/* 14927 */       rotMod = (float)(rotMod * Math.pow(0.9D, getRarity()));
/*       */     }
/* 14929 */     if (getSpellEffects() != null)
/*       */     {
/* 14931 */       rotMod *= getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_DECAY);
/*       */     }
/* 14933 */     return 100.0F * rotMod / Math.max(1.0F, this.qualityLevel * (100.0F - this.damage) / 100.0F) * materialMod;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTraded() {
/* 14938 */     return (this.tradeWindow != null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setTradeWindow(@Nullable TradingWindow _tradeWindow) {
/* 14947 */     this.tradeWindow = _tradeWindow;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final TradingWindow getTradeWindow() {
/* 14958 */     return this.tradeWindow;
/*       */   }
/*       */ 
/*       */   
/*       */   public final String getTasteString() {
/* 14963 */     String toReturn = "royal, noble, and utterly delicious!";
/* 14964 */     float ql = getCurrentQualityLevel();
/*       */     
/* 14966 */     if (ql < 5.0F) { toReturn = "rotten, bad, evil and dangerous."; }
/* 14967 */     else if (ql < 20.0F) { toReturn = "extremely bad."; }
/* 14968 */     else if (ql < 30.0F) { toReturn = "pretty bad."; }
/* 14969 */     else if (ql < 40.0F) { toReturn = "okay."; }
/* 14970 */     else if (ql < 50.0F) { toReturn = "pretty good."; }
/* 14971 */     else if (ql < 60.0F) { toReturn = "good."; }
/* 14972 */     else if (ql < 70.0F) { toReturn = "very good."; }
/* 14973 */     else if (ql < 80.0F) { toReturn = "extremely good."; }
/* 14974 */     else if (ql < 90.0F) { toReturn = "so good you almost feel like singing."; }
/*       */     
/* 14976 */     return toReturn;
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
/*       */   public static byte fuelEfficiency(byte material) {
/* 14989 */     switch (material) {
/*       */       
/*       */       case 14:
/* 14992 */         return 2;
/* 14993 */       case 58: return 8;
/* 14994 */       case 59: return 4;
/* 14995 */     }  return 1;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static String getMaterialString(byte material) {
/* 15002 */     return MaterialUtilities.getMaterialString(material);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isHollow() {
/* 15013 */     return this.template.isHollow();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponSlash() {
/* 15018 */     return this.template.weaponslash;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isShield() {
/* 15023 */     return this.template.shield;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isArmour() {
/* 15028 */     return this.template.armour;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBracelet() {
/* 15033 */     return this.template.isBracelet();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFood() {
/* 15038 */     return this.template.isFood();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isNamed() {
/* 15043 */     if (this.realTemplate > 0 && !isDish()) {
/*       */       
/* 15045 */       ItemTemplate realTemplate = getRealTemplate();
/* 15046 */       assert realTemplate != null;
/* 15047 */       return realTemplate.namedCreator;
/*       */     } 
/* 15049 */     return this.template.namedCreator;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOnePerTile() {
/* 15054 */     return this.template.onePerTile;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFourPerTile() {
/* 15059 */     return this.template.fourPerTile;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTenPerTile() {
/* 15064 */     return this.template.tenPerTile;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTrellis() {
/* 15069 */     return this.template.isTrellis();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isMagic() {
/* 15079 */     return this.template.magic;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEgg() {
/* 15084 */     return this.template.egg;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFieldTool() {
/* 15089 */     return this.template.fieldtool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBodyPart() {
/* 15094 */     return this.template.bodypart;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBodyPartAttached() {
/* 15100 */     return (this.template.bodypart && this.auxbyte != 100);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBodyPartRemoved() {
/* 15106 */     return (this.template.bodypart && this.auxbyte == 100);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInventory() {
/* 15111 */     return this.template.inventory;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInventoryGroup() {
/* 15116 */     return this.template.isInventoryGroup();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isDragonArmour() {
/* 15125 */     return this.template.isDragonArmour;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getImproveItem() {
/* 15130 */     return this.template.getImproveItem();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isMiningtool() {
/* 15139 */     return this.template.miningtool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWand() {
/* 15144 */     return (getTemplateId() == 315 || getTemplateId() == 176);
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isCompass() {
/* 15149 */     return this.template.isCompass;
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isToolbelt() {
/* 15154 */     return this.template.isToolbelt;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBelt() {
/* 15159 */     return this.template.isBelt;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCarpentryTool() {
/* 15164 */     return this.template.carpentrytool;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isSmithingTool() {
/* 15174 */     return this.template.smithingtool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponBow() {
/* 15179 */     return this.template.bow;
/*       */   }
/*       */   
/*       */   public final boolean isArrow() {
/* 15183 */     switch (getTemplateId()) {
/*       */       
/*       */       case 454:
/*       */       case 455:
/*       */       case 456:
/* 15188 */         return true;
/*       */     } 
/* 15190 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isBowUnstringed() {
/* 15196 */     return this.template.bowUnstringed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponPierce() {
/* 15201 */     return this.template.weaponpierce;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponCrush() {
/* 15206 */     return this.template.weaponcrush;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponAxe() {
/* 15211 */     return this.template.weaponaxe;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponSword() {
/* 15216 */     return this.template.weaponsword;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponPolearm() {
/* 15221 */     return this.template.weaponPolearm;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponKnife() {
/* 15226 */     return this.template.weaponknife;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeaponMisc() {
/* 15231 */     return this.template.weaponmisc;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDiggingtool() {
/* 15236 */     return this.template.diggingtool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoTrade() {
/* 15241 */     return this.template.notrade;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSeed() {
/* 15246 */     return this.template.seed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSeedling() {
/* 15251 */     switch (getTemplateId()) {
/*       */       case 917:
/*       */       case 918:
/*       */       case 1017:
/* 15255 */         return true;
/*       */     } 
/* 15257 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isAbility() {
/* 15262 */     return this.template.isAbility;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLiquid() {
/* 15267 */     return this.template.liquid;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDye() {
/* 15272 */     switch (getTemplateId()) {
/*       */       
/*       */       case 431:
/*       */       case 432:
/*       */       case 433:
/*       */       case 434:
/*       */       case 435:
/*       */       case 438:
/* 15280 */         return true;
/*       */     } 
/* 15282 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isLightBright() {
/* 15288 */     return this.template.brightLight;
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
/*       */   public static byte getRLight(int brightness) {
/* 15302 */     return (byte)(255 * brightness / 255);
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
/*       */   public static byte getGLight(int brightness) {
/* 15316 */     return (byte)(239 * brightness / 255);
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
/*       */   public static byte getBLight(int brightness) {
/* 15330 */     return (byte)(173 * brightness / 255);
/*       */   }
/*       */ 
/*       */   
/*       */   public final long getDecayTime() {
/* 15335 */     return this.template.getDecayTime();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRefreshedOnUse() {
/* 15340 */     return (this.template.getDecayTime() == 28800L);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDish() {
/* 15345 */     return this.template.isDish;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMelting() {
/* 15350 */     return this.template.melting;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMeat() {
/* 15355 */     return this.template.meat;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSign() {
/* 15360 */     return this.template.sign;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFence() {
/* 15365 */     return this.template.fence;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isVegetable() {
/* 15370 */     return this.template.vegetable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRoadMarker() {
/* 15375 */     return this.template.isRoadMarker();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPaveable() {
/* 15380 */     return this.template.isPaveable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCavePaveable() {
/* 15385 */     return this.template.isCavePaveable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean containsIngredientsOnly() {
/* 15390 */     return this.template.containsIngredientsOnly();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isShelf() {
/* 15395 */     return this.template.isShelf();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isComponentItem() {
/* 15400 */     return this.template.isComponentItem();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isParentMustBeOnGround() {
/* 15405 */     return this.template.isParentMustBeOnGround();
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isVillageRecruitmentBoard() {
/* 15410 */     return (this.template.templateId == 835);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBed() {
/* 15415 */     return this.template.bed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNewbieItem() {
/* 15420 */     return (this.template.newbieItem && this.auxbyte > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMilk() {
/* 15425 */     return this.template.isMilk;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCheese() {
/* 15430 */     return this.template.isCheese;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isChallengeNewbieItem() {
/* 15435 */     return (this.template.challengeNewbieItem && this.auxbyte > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWood() {
/* 15440 */     if (this.material == 0) {
/*       */       
/* 15442 */       if (this.realTemplate > 0) {
/*       */         
/* 15444 */         ItemTemplate realTemplate = getRealTemplate();
/* 15445 */         assert realTemplate != null;
/* 15446 */         return realTemplate.wood;
/*       */       } 
/*       */       
/* 15449 */       return this.template.wood;
/*       */     } 
/*       */     
/* 15452 */     return Materials.isWood(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isStone() {
/* 15457 */     if (this.material == 0) {
/* 15458 */       return this.template.stone;
/*       */     }
/* 15460 */     return Materials.isStone(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCombineCold() {
/* 15465 */     return this.template.isCombineCold();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isGem() {
/* 15470 */     return this.template.gem;
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isFlickering() {
/* 15475 */     return this.template.flickeringLight;
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
/*       */   public final ItemTemplate getRealTemplate() {
/*       */     try {
/* 15489 */       return ItemTemplateFactory.getInstance().getTemplate(this.realTemplate);
/*       */     }
/* 15491 */     catch (NoSuchTemplateException noSuchTemplateException) {
/*       */ 
/*       */       
/* 15494 */       return null;
/*       */     } 
/*       */   }
/*       */   
/*       */   public final boolean isMetal() {
/* 15499 */     if (this.material == 0) {
/*       */       
/* 15501 */       if (this.realTemplate > 0) {
/*       */         
/* 15503 */         ItemTemplate realTemplate = getRealTemplate();
/* 15504 */         assert realTemplate != null;
/* 15505 */         return realTemplate.isMetal();
/*       */       } 
/*       */       
/* 15508 */       return this.template.isMetal();
/*       */     } 
/* 15510 */     return Materials.isMetal(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLeather() {
/* 15515 */     if (this.material == 0) {
/*       */       
/* 15517 */       if (this.realTemplate > 0) {
/*       */         
/* 15519 */         ItemTemplate realTemplate = getRealTemplate();
/* 15520 */         assert realTemplate != null;
/* 15521 */         return realTemplate.leather;
/*       */       } 
/*       */       
/* 15524 */       return this.template.leather;
/*       */     } 
/*       */     
/* 15527 */     return Materials.isLeather(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPaper() {
/* 15532 */     if (this.material == 0) {
/*       */       
/* 15534 */       if (this.realTemplate > 0) {
/*       */         
/* 15536 */         ItemTemplate realTemplate = getRealTemplate();
/* 15537 */         assert realTemplate != null;
/* 15538 */         return realTemplate.paper;
/*       */       } 
/*       */       
/* 15541 */       return this.template.paper;
/*       */     } 
/*       */     
/* 15544 */     return Materials.isPaper(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCloth() {
/* 15549 */     if (this.material == 0) {
/*       */       
/* 15551 */       if (this.realTemplate > 0) {
/*       */         
/* 15553 */         ItemTemplate realTemplate = getRealTemplate();
/* 15554 */         assert realTemplate != null;
/* 15555 */         return realTemplate.cloth;
/*       */       } 
/*       */       
/* 15558 */       return this.template.cloth;
/*       */     } 
/*       */     
/* 15561 */     return Materials.isCloth(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWool() {
/* 15566 */     if (this.material == 0) {
/*       */       
/* 15568 */       if (this.realTemplate > 0) {
/*       */         
/* 15570 */         ItemTemplate realTemplate = getRealTemplate();
/* 15571 */         assert realTemplate != null;
/* 15572 */         return (realTemplate.getMaterial() == 69);
/*       */       } 
/*       */       
/* 15575 */       return (this.template.getMaterial() == 69);
/*       */     } 
/*       */     
/* 15578 */     return (this.material == 69);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPottery() {
/* 15583 */     if (this.material == 0) {
/*       */       
/* 15585 */       if (this.realTemplate > 0) {
/*       */         
/* 15587 */         ItemTemplate realTemplate = getRealTemplate();
/* 15588 */         assert realTemplate != null;
/* 15589 */         return realTemplate.pottery;
/*       */       } 
/* 15591 */       return this.template.pottery;
/*       */     } 
/*       */     
/* 15594 */     return Materials.isPottery(this.material);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPlantedFlowerpot() {
/* 15599 */     return this.template.isPlantedFlowerpot();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPotteryFlowerPot() {
/* 15604 */     int tempId = getTemplateId();
/* 15605 */     switch (tempId) {
/*       */       
/*       */       case 812:
/*       */       case 813:
/*       */       case 814:
/*       */       case 815:
/*       */       case 816:
/*       */       case 817:
/*       */       case 818:
/*       */       case 819:
/*       */       case 820:
/* 15616 */         return true;
/*       */     } 
/* 15618 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isMarblePlanter() {
/* 15624 */     int tempId = getTemplateId();
/* 15625 */     switch (tempId) {
/*       */       
/*       */       case 1001:
/*       */       case 1002:
/*       */       case 1003:
/*       */       case 1004:
/*       */       case 1005:
/*       */       case 1006:
/*       */       case 1007:
/*       */       case 1008:
/* 15635 */         return true;
/*       */     } 
/* 15637 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isMagicStaff() {
/* 15643 */     return this.template.isMagicStaff();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isImproveUsingTypeAsMaterial() {
/* 15648 */     return this.template.isImproveUsingTypeAsMaterial();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLight() {
/* 15653 */     return (this.template.light || this.isLightOverride);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isContainerLiquid() {
/* 15658 */     return this.template.containerliquid;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLiquidInflammable() {
/* 15663 */     return this.template.liquidinflammable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHealingSalve() {
/* 15668 */     return (this.template.getTemplateId() == 650);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isForgeOrOven() {
/* 15673 */     return (this.template.getTemplateId() == 180 || this.template.getTemplateId() == 178);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSpawnPoint() {
/* 15678 */     return (this.template.getTemplateId() == 1016);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isWeaponMelee() {
/* 15688 */     return this.template.weaponmelee;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFish() {
/* 15693 */     return this.template.fish;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMailBox() {
/* 15698 */     return (this.template.templateId >= 510 && this.template.templateId <= 513);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUnenchantedTurret() {
/* 15703 */     return (this.template.templateId == 934);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEnchantedTurret() {
/* 15708 */     switch (this.template.templateId) {
/*       */       
/*       */       case 940:
/*       */       case 941:
/*       */       case 942:
/*       */       case 968:
/* 15714 */         return true;
/*       */     } 
/* 15716 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isMarketStall() {
/* 15722 */     return (this.template.templateId == 580);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWeapon() {
/* 15727 */     return this.template.weapon;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTool() {
/* 15732 */     return this.template.tool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCookingTool() {
/* 15737 */     return this.template.isCookingTool();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLock() {
/* 15742 */     return this.template.lock;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateIndestructible() {
/* 15747 */     return this.template.indestructible;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isKey() {
/* 15752 */     return this.template.key;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBulkContainer() {
/* 15757 */     return this.template.bulkContainer;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTopParentPile() {
/* 15762 */     Item item = getTopParentOrNull();
/* 15763 */     if (item == null)
/* 15764 */       return false; 
/* 15765 */     return (item.getTemplateId() == 177);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Item getItemWithTemplateAndMaterial(int stemplateId, int smaterial, byte auxByte, int srealTemplateId) {
/* 15770 */     for (Item i : getItems()) {
/*       */       
/* 15772 */       if (i.getRealTemplateId() == stemplateId && smaterial == i.getMaterial() && i.getAuxData() == auxByte && ((srealTemplateId == -10 && i
/* 15773 */         .getData1() == -1) || i.getData1() == srealTemplateId))
/* 15774 */         return i; 
/*       */     } 
/* 15776 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBulkItem() {
/* 15781 */     return (getTemplateId() == 669);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBulk() {
/* 15786 */     return this.template.bulk;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFire() {
/* 15791 */     return this.template.isFire;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canBeDropped(boolean checkTraded) {
/* 15796 */     if (isNoDrop()) {
/* 15797 */       return false;
/*       */     }
/* 15799 */     if (checkTraded && isTraded()) {
/* 15800 */       return false;
/*       */     }
/* 15802 */     if (this.items == null || !isHollow()) {
/* 15803 */       return true;
/*       */     }
/* 15805 */     for (Item item : this.items) {
/*       */       
/* 15807 */       if (!item.canBeDropped(true)) {
/* 15808 */         return false;
/*       */       }
/*       */       
/* 15811 */       if (item.isNoTrade()) {
/* 15812 */         return false;
/*       */       }
/*       */     } 
/* 15815 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWind() {
/* 15820 */     return this.template.isWind;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFlag() {
/* 15825 */     return this.template.isFlag;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRepairable() {
/* 15830 */     if (this.permissions.hasPermission(Permissions.Allow.NO_REPAIR.getBit()))
/* 15831 */       return false; 
/* 15832 */     return isRepairableDefault();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRepairableDefault() {
/* 15837 */     if (this.realTemplate > 0 && getTemplateId() != 1307) {
/*       */       
/* 15839 */       ItemTemplate realTemplate = getRealTemplate();
/* 15840 */       assert realTemplate != null;
/* 15841 */       return (realTemplate.repairable || realTemplate.templateId == 74 || realTemplate.templateId == 480);
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/* 15846 */     if (getTemplateId() == 179) {
/* 15847 */       return true;
/*       */     }
/*       */     
/* 15850 */     return this.template.repairable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRoyal() {
/* 15855 */     return this.template.isRoyal;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTemporary() {
/* 15860 */     return this.template.temporary;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCombine() {
/* 15865 */     return this.template.combine;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateIsLockable() {
/* 15870 */     return this.template.lockable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUnfired() {
/* 15875 */     return this.template.isUnfired;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canHaveInscription() {
/* 15880 */     return this.template.canHaveInscription();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isAlmanacContainer() {
/* 15885 */     return this.template.isAlmanacContainer();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHarvestReport() {
/* 15890 */     if (getTemplateId() == 1272 || getTemplateId() == 748)
/* 15891 */       return (getAuxData() > 8); 
/* 15892 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public final WurmHarvestables.Harvestable getHarvestable() {
/* 15898 */     return WurmHarvestables.getHarvestable(getAuxData() - 8);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasData() {
/* 15903 */     return this.template.hasdata;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasExtraData() {
/* 15908 */     return this.template.hasExtraData();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDraggable() {
/* 15913 */     return (this.template.draggable && !isNoDrag());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isVillageDeed() {
/* 15918 */     return this.template.villagedeed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTransmutable() {
/* 15923 */     return this.template.isTransmutable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFarwalkerItem() {
/* 15928 */     return this.template.farwalkerItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHomesteadDeed() {
/* 15933 */     return this.template.homesteaddeed;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoRename() {
/* 15938 */     return this.template.norename;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoNutrition() {
/* 15943 */     return this.template.isNoNutrition();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLowNutrition() {
/* 15948 */     return this.template.isLowNutrition();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMediumNutrition() {
/* 15953 */     return this.template.isMediumNutrition();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHighNutrition() {
/* 15958 */     return this.template.isHighNutrition();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isGoodNutrition() {
/* 15963 */     return this.template.isGoodNutrition();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFoodMaker() {
/* 15968 */     return this.template.isFoodMaker();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canLarder() {
/* 15973 */     return this.template.canLarder();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateAlwaysLit() {
/* 15978 */     return this.template.alwaysLit;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEpicTargetItem() {
/* 15983 */     if (this.realTemplate > 0) {
/*       */       
/* 15985 */       ItemTemplate realTemplate = getRealTemplate();
/* 15986 */       assert realTemplate != null;
/* 15987 */       return realTemplate.isEpicTargetItem;
/*       */     } 
/* 15989 */     return this.template.isEpicTargetItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private final boolean checkPlantedPermissions(Creature creature) {
/* 15996 */     VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), this.surfaced);
/* 15997 */     if (vt != null) {
/*       */       
/* 15999 */       Structure structure = vt.getStructure();
/* 16000 */       if (structure != null && structure.isTypeHouse())
/*       */       {
/* 16002 */         return structure.isActionAllowed(creature, (short)685);
/*       */       }
/* 16004 */       Village village = vt.getVillage();
/* 16005 */       if (village != null)
/*       */       {
/* 16007 */         return village.isActionAllowed((short)685, creature);
/*       */       }
/*       */     } 
/* 16010 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTurnable(@Nonnull Creature turner) {
/* 16015 */     if (getParentId() != -10L && (getParentOrNull() != getTopParentOrNull() || !getParentOrNull().getTemplate().hasViewableSubItems() || (
/* 16016 */       getParentOrNull().getTemplate().isContainerWithSubItems() && !isPlacedOnParent())))
/* 16017 */       return false; 
/* 16018 */     if (isOwnedByWagoner())
/* 16019 */       return false; 
/* 16020 */     if (isTurnable() && !isPlanted())
/* 16021 */       return true; 
/* 16022 */     if (turner.getPower() >= 2)
/* 16023 */       return true; 
/* 16024 */     if (isPlanted() && checkPlantedPermissions(turner)) {
/* 16025 */       return true;
/*       */     }
/* 16027 */     return ((isOwnerTurnable() || isPlanted()) && this.lastOwner == turner.getWurmId());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMoveable(@Nonnull Creature mover) {
/* 16032 */     if (getParentId() != -10L)
/* 16033 */       return false; 
/* 16034 */     if (isOwnedByWagoner())
/* 16035 */       return false; 
/* 16036 */     if (isEpicTargetItem())
/*       */     {
/* 16038 */       if (EpicServerStatus.getRitualMissionForTarget(getWurmId()) != null)
/* 16039 */         return false; 
/*       */     }
/* 16041 */     if (!isNoMove() && !isPlanted())
/* 16042 */       return true; 
/* 16043 */     if (mover.getPower() >= 2)
/* 16044 */       return true; 
/* 16045 */     if (isPlanted() && checkPlantedPermissions(mover)) {
/* 16046 */       return true;
/*       */     }
/* 16048 */     return ((isOwnerMoveable() || isPlanted()) && this.lastOwner == mover.getWurmId());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isGuardTower() {
/* 16053 */     return this.template.isGuardTower();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHerb() {
/* 16058 */     return this.template.herb;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSpice() {
/* 16063 */     return this.template.spice;
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isFruit() {
/* 16068 */     return this.template.fruit;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateIsNoMove() {
/* 16073 */     return this.template.isNoMove;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isPoison() {
/* 16083 */     return this.template.poison;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOutsideOnly() {
/* 16088 */     return this.template.outsideonly;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isInsideOnly() {
/* 16098 */     return this.template.insideOnly;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCoin() {
/* 16103 */     return this.template.coin;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getRentCost() {
/* 16108 */     switch (this.auxbyte) {
/*       */       
/*       */       case 0:
/* 16111 */         return 0;
/* 16112 */       case 1: return 100;
/* 16113 */       case 2: return 1000;
/* 16114 */       case 3: return 10000;
/* 16115 */       case 4: return 100000;
/* 16116 */       case 5: return 10;
/* 16117 */       case 6: return 25;
/* 16118 */       case 7: return 50;
/*       */     } 
/*       */     
/* 16121 */     return 0;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDecoration() {
/* 16126 */     if (this.realTemplate > 0 && getTemplateId() != 1162 && 
/* 16127 */       !isPlanted() && getTemplateId() != 1307) {
/*       */       
/* 16129 */       ItemTemplate realTemplate = getRealTemplate();
/* 16130 */       assert realTemplate != null;
/*       */       
/* 16132 */       VolaTile v = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 16133 */       if (v != null && 
/* 16134 */         v.getNumberOfDecorations(getFloorLevel()) >= 15) {
/* 16135 */         return false;
/*       */       }
/* 16137 */       return realTemplate.decoration;
/*       */     } 
/* 16139 */     if (this.template.decoration)
/* 16140 */       return true; 
/* 16141 */     return (this.template.decorationWhenPlanted && isPlanted());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBag() {
/* 16146 */     return this.template.isBag;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isQuiver() {
/* 16151 */     return this.template.isQuiver;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFullprice() {
/* 16156 */     return this.template.fullprice;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoSellback() {
/* 16161 */     return this.template.isNoSellBack;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBarding() {
/* 16166 */     return this.template.isBarding();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRope() {
/* 16171 */     return this.template.isRope();
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
/*       */   public final boolean isAlwaysPoll() {
/* 16183 */     return this.template.alwayspoll;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isProtectionTower() {
/* 16188 */     return this.template.protectionTower;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFloating() {
/* 16193 */     if (this.template.templateId == 1396 && isPlanted())
/* 16194 */       return true; 
/* 16195 */     return this.template.floating;
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isButcheredItem() {
/* 16200 */     return this.template.isButcheredItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoWorkParent() {
/* 16205 */     return this.template.noWorkParent;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoBank() {
/* 16210 */     return this.template.nobank;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isAlwaysBankable() {
/* 16215 */     return this.template.alwaysBankable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLeadCreature() {
/* 16220 */     return this.template.isLeadCreature;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isLeadMultipleCreatures() {
/* 16225 */     return this.template.isLeadMultipleCreatures;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean descIsExam() {
/* 16230 */     return this.template.descIsExam;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getPrice() {
/* 16239 */     return this.price;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDomainItem() {
/* 16244 */     return this.template.domainItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCrude() {
/* 16249 */     return this.template.isCrude();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMinable() {
/* 16254 */     return this.template.minable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEnchantableJewelry() {
/* 16259 */     return this.template.isEnchantableJewelry;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUseOnGroundOnly() {
/* 16264 */     if (this.realTemplate > 0 && getTemplateId() != 1307) {
/*       */       
/* 16266 */       ItemTemplate realTemplate = getRealTemplate();
/* 16267 */       assert realTemplate != null;
/* 16268 */       return realTemplate.useOnGroundOnly;
/*       */     } 
/* 16270 */     return this.template.useOnGroundOnly;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHugeAltar() {
/* 16275 */     return this.template.hugeAltar;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDestroyHugeAltar() {
/* 16280 */     return this.template.destroysHugeAltar;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isArtifact() {
/* 16285 */     return this.template.artifact;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUnique() {
/* 16290 */     return this.template.unique;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTwoHanded() {
/* 16295 */     return this.template.isTwohanded;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isServerBound() {
/* 16300 */     return this.template.isServerBound;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isKingdomMarker() {
/* 16305 */     return this.template.kingdomMarker;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDestroyable(long destroyerId) {
/* 16310 */     if (this.template.destroyable) {
/* 16311 */       return true;
/*       */     }
/* 16313 */     if (!this.template.ownerDestroyable) {
/* 16314 */       return false;
/*       */     }
/*       */     
/* 16317 */     long lockId = getLockId();
/* 16318 */     if (lockId == -10L)
/*       */     {
/* 16320 */       return (this.lastOwner == destroyerId);
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 16325 */       Item lock = Items.getItem(lockId);
/* 16326 */       if (lock.lastOwner == destroyerId) {
/* 16327 */         return true;
/*       */       }
/* 16329 */     } catch (NoSuchItemException nsi) {
/*       */       
/* 16331 */       if (this.lastOwner == destroyerId) {
/* 16332 */         return true;
/*       */       }
/*       */     } 
/* 16335 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDrinkable() {
/* 16340 */     return this.template.drinkable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isVehicle() {
/* 16345 */     return this.template.isVehicle();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isChair() {
/* 16350 */     return this.template.isChair;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCart() {
/* 16355 */     return this.template.isCart;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWagonerWagon() {
/* 16360 */     return this.wagonerWagon;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setWagonerWagon(boolean isWagonerWagon) {
/* 16365 */     this.wagonerWagon = isWagonerWagon;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBoat() {
/* 16370 */     return (this.template.isVehicle() && this.template.isFloating());
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMooredBoat() {
/* 16375 */     return (isBoat() && getData() != -1L);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRechargeable() {
/* 16380 */     return this.template.isRechargeable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMineDoor() {
/* 16385 */     return this.template.isMineDoor;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOwnerDestroyable() {
/* 16390 */     return this.template.ownerDestroyable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHitchTarget() {
/* 16395 */     return this.template.isHitchTarget();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRiftAltar() {
/* 16400 */     return this.template.isRiftAltar();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isRiftItem() {
/* 16405 */     return this.template.isRiftItem();
/*       */   }
/*       */   
/*       */   public final boolean isRiftLoot() {
/* 16409 */     return this.template.isRiftLoot();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean hasItemBonus() {
/* 16414 */     return this.template.isHasItemBonus();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isPriceEffectedByMaterial() {
/* 16423 */     return this.template.priceAffectedByMaterial;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDeathProtection() {
/* 16428 */     return this.template.isDeathProtection;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInPvPZone() {
/* 16433 */     return Zones.isOnPvPServer(getTilePos());
/*       */   }
/*       */ 
/*       */   
/*       */   public final void getContainedItems() {
/* 16438 */     if (!isHollow() && !isBodyPart()) {
/*       */       return;
/*       */     }
/*       */     
/* 16442 */     Set<Item> set = Items.getContainedItems(this.id);
/* 16443 */     if (set == null) {
/*       */       return;
/*       */     }
/*       */     
/* 16447 */     for (Item item : set) {
/*       */       
/* 16449 */       if (item.getOwnerId() != this.ownerId) {
/*       */         
/* 16451 */         logWarn(item.getName() + " at " + ((int)item.getPosX() >> 2) + ", " + ((int)item.getPosY() >> 2) + " with id " + item
/* 16452 */             .getWurmId() + " doesn't have the same owner as " + getName() + " with id " + this.id + ". Deleting.");
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 16458 */         Items.decay(item.getWurmId(), item.getDbStrings());
/*       */         
/*       */         continue;
/*       */       } 
/*       */       
/* 16463 */       addItem(item, true);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHolyItem() {
/* 16469 */     if (!this.template.holyItem) {
/* 16470 */       return false;
/*       */     }
/* 16472 */     switch (getMaterial()) {
/*       */       
/*       */       case 7:
/*       */       case 8:
/* 16476 */         return true;
/*       */       case 67:
/*       */       case 96:
/* 16479 */         if (Features.Feature.METALLIC_ITEMS.isEnabled())
/* 16480 */           return true; 
/*       */         break;
/*       */     } 
/* 16483 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHolyItem(Deity deity) {
/* 16488 */     return (deity != null && deity.holyItem == getTemplateId());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   final boolean isPassFullData() {
/* 16498 */     return this.template.passFullData;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMeditation() {
/* 16503 */     return this.template.isMeditation;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTileAligned() {
/* 16508 */     if (this.realTemplate > 0) {
/*       */       
/* 16510 */       ItemTemplate realTemplate = getRealTemplate();
/* 16511 */       assert realTemplate != null;
/* 16512 */       return realTemplate.isTileAligned;
/*       */     } 
/* 16514 */     return this.template.isTileAligned;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNewDeed() {
/* 16519 */     return (getTemplateId() == 663);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isOldDeed() {
/* 16524 */     return ((isVillageDeed() || isHomesteadDeed()) && !isNewDeed());
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isLiquidCooking() {
/* 16529 */     return this.template.isLiquidCooking();
/*       */   }
/*       */ 
/*       */   
/*       */   final boolean isForm() {
/* 16534 */     return this.template.isForm;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isFlower() {
/* 16539 */     return this.template.isFlower;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNaturePlantable() {
/* 16544 */     return this.template.isNaturePlantable;
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
/*       */   public final boolean isBanked() {
/* 16564 */     if (this.banked && this.parentId != -10L && 
/* 16565 */       !Banks.isItemBanked(getWurmId())) {
/* 16566 */       logger.warning("Bugged item showing as banked: " + toString());
/* 16567 */       setBanked(false);
/*       */     } 
/*       */     
/* 16570 */     return this.banked;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isAltar() {
/* 16575 */     return this.template.isAltar;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final Deity getBless() {
/* 16584 */     if (this.bless > 0) {
/*       */       
/* 16586 */       Deity deity = Deities.getDeity(this.bless);
/* 16587 */       if (deity != null)
/* 16588 */         return deity; 
/*       */     } 
/* 16590 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setData(long aData) {
/* 16595 */     if (aData == -10L) {
/*       */       
/* 16597 */       setData(-1, -1);
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 16602 */       setData((int)(aData >> 32L), (int)aData);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final long getData() {
/* 16612 */     int data1 = getData1();
/* 16613 */     int data2 = getData2();
/* 16614 */     if (data1 == -1 || data2 == -1)
/*       */     {
/*       */       
/* 16617 */       return -1L;
/*       */     }
/*       */ 
/*       */     
/* 16621 */     return (data2 & 0xFFFFFFFFL) + BigInteger.valueOf(data1 & 0xFFFFFFFFL).shiftLeft(32).longValue();
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setExtra(long aExtra) {
/* 16627 */     if (aExtra == -10L) {
/*       */       
/* 16629 */       setExtra(-1, -1);
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 16634 */       setExtra((int)(aExtra >> 32L), (int)aExtra);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final long getExtra() {
/* 16644 */     int extra1 = getExtra1();
/* 16645 */     int extra2 = getExtra2();
/* 16646 */     if (extra1 == -1 || extra2 == -1)
/*       */     {
/*       */       
/* 16649 */       return -1L;
/*       */     }
/*       */ 
/*       */     
/* 16653 */     return (extra2 & 0xFFFFFFFFL) + BigInteger.valueOf(extra1 & 0xFFFFFFFFL).shiftLeft(32).longValue();
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
/*       */   public final void setDataXY(int aTileX, int aTileY) {
/* 16665 */     setData1(aTileX << 16 | aTileY);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final short getDataX() {
/* 16675 */     int data1 = getData1();
/* 16676 */     if (data1 == -1) {
/* 16677 */       return -1;
/*       */     }
/*       */     
/* 16680 */     return (short)(data1 >> 16 & 0xFFFF);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final short getDataY() {
/* 16691 */     int data1 = getData1();
/* 16692 */     if (data1 == -1) {
/* 16693 */       return -1;
/*       */     }
/* 16695 */     return (short)(getData1() & 0xFFFF);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setSizes(int aWeight) {
/* 16700 */     ItemTemplate lTemplate = getTemplate();
/*       */     
/* 16702 */     int sizeX = lTemplate.getSizeX();
/* 16703 */     int sizeY = lTemplate.getSizeY();
/* 16704 */     int sizeZ = lTemplate.getSizeZ();
/* 16705 */     float mod = aWeight / lTemplate.getWeightGrams();
/* 16706 */     if (mod > 64.0F) {
/*       */       
/* 16708 */       setSizeZ(sizeZ * 4);
/* 16709 */       setSizeY(sizeY * 4);
/* 16710 */       setSizeX(sizeX * 4);
/*       */     }
/* 16712 */     else if (mod > 16.0F) {
/*       */       
/* 16714 */       setSizeZ(sizeZ * 4);
/* 16715 */       setSizeY(sizeY * 4);
/* 16716 */       mod = mod / 4.0F * 4.0F;
/* 16717 */       setSizeX((int)(sizeX * mod));
/*       */     }
/* 16719 */     else if (mod > 4.0F) {
/*       */       
/* 16721 */       setSizeZ(sizeZ * 4);
/* 16722 */       mod /= 4.0F;
/* 16723 */       setSizeY((int)(sizeY * mod));
/* 16724 */       setSizeX(sizeX);
/*       */     
/*       */     }
/*       */     else {
/*       */       
/* 16729 */       setSizes(Math.max(1, (int)(sizeX * mod)), 
/* 16730 */           Math.max(1, (int)(sizeY * mod)), 
/* 16731 */           Math.max(1, (int)(sizeZ * mod)));
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final ItemSpellEffects getSpellEffects() {
/* 16737 */     return ItemSpellEffects.getSpellEffects(this.id);
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final float getDamageModifierForItem(Item item) {
/* 16743 */     float mod = 0.0F;
/* 16744 */     if (isStone())
/*       */     
/*       */     { 
/* 16747 */       if (item.getTemplateId() == 20) { mod = 0.007F; }
/* 16748 */       else if (item.isWeaponCrush()) { mod = 0.003F; }
/* 16749 */       else if (item.isWeaponAxe()) { mod = 0.0015F; }
/* 16750 */       else if (item.isWeaponSlash()) { mod = 0.001F; }
/* 16751 */       else if (item.isWeaponPierce()) { mod = 0.001F; }
/* 16752 */       else if (item.isWeaponMisc()) { mod = 0.001F; }
/*       */       
/*       */        }
/* 16755 */     else if (getMaterial() == 38)
/*       */     
/*       */     { 
/* 16758 */       if (item.isWeaponAxe()) { mod = 0.007F; }
/* 16759 */       else if (item.isWeaponCrush()) { mod = 0.003F; }
/* 16760 */       else if (item.isWeaponSlash()) { mod = 0.005F; }
/* 16761 */       else if (item.isWeaponPierce()) { mod = 0.002F; }
/* 16762 */       else if (item.isWeaponMisc()) { mod = 0.001F; }
/*       */       
/*       */        }
/* 16765 */     else if (isWood() || isCloth() || isFood())
/*       */     
/*       */     { 
/* 16768 */       if (item.isWeaponAxe()) { mod = 0.003F; }
/* 16769 */       else if (item.isWeaponCrush()) { mod = 0.002F; }
/* 16770 */       else if (item.isWeaponSlash()) { mod = 0.0015F; }
/* 16771 */       else if (item.isWeaponPierce()) { mod = 0.001F; }
/* 16772 */       else if (item.isWeaponMisc()) { mod = 7.0E-4F; }
/*       */       
/*       */        }
/* 16775 */     else if (isMetal())
/*       */     
/*       */     { 
/*       */ 
/*       */       
/* 16780 */       if (item.isWeaponAxe()) { mod = 0.001F; }
/* 16781 */       else if (item.isWeaponCrush()) { mod = 0.0015F; }
/* 16782 */       else if (item.isWeaponSlash()) { mod = 0.001F; }
/* 16783 */       else if (item.isWeaponPierce()) { mod = 5.0E-4F; }
/* 16784 */       else if (item.isWeaponMisc()) { mod = 0.001F;
/*       */          }
/*       */ 
/*       */       
/*       */        }
/*       */     
/* 16790 */     else if (item.isWeaponAxe()) { mod = 0.001F; }
/* 16791 */     else if (item.isWeaponCrush()) { mod = 0.0015F; }
/* 16792 */     else if (item.isWeaponSlash()) { mod = 0.001F; }
/* 16793 */     else if (item.isWeaponPierce()) { mod = 5.0E-4F; }
/* 16794 */     else if (item.isWeaponMisc()) { mod = 0.001F; }
/*       */ 
/*       */     
/* 16797 */     if (isTent())
/* 16798 */       mod *= 50.0F; 
/* 16799 */     return mod;
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
/*       */   public final Vector2f getPos2f() {
/* 16811 */     if (this.parentId == -10L && !isBodyPartAttached() && !isInventory()) {
/* 16812 */       return new Vector2f(this.posX, this.posY);
/*       */     }
/* 16814 */     if (this.ownerId != -10L) {
/*       */       
/*       */       try {
/* 16817 */         Creature creature = Server.getInstance().getCreature(this.ownerId);
/* 16818 */         return creature.getPos2f();
/*       */       }
/* 16820 */       catch (NoSuchCreatureException e) {
/* 16821 */         if (!Items.isItemLoaded(this.parentId)) {
/* 16822 */           return new Vector2f(this.posX, this.posY);
/*       */         }
/*       */         try {
/* 16825 */           Item parent = Items.getItem(this.parentId);
/* 16826 */           return parent.getPos2f();
/*       */         }
/* 16828 */         catch (NoSuchItemException e1) {
/* 16829 */           logWarn("This REALLY shouldn't happen!", (Throwable)e1);
/*       */         }
/*       */       
/* 16832 */       } catch (NoSuchPlayerException ignored) {
/* 16833 */         if (!Items.isItemLoaded(this.parentId)) {
/* 16834 */           return new Vector2f(this.posX, this.posY);
/*       */         }
/*       */         try {
/* 16837 */           Item parent = Items.getItem(this.parentId);
/* 16838 */           return parent.getPos2f();
/*       */         }
/* 16840 */         catch (NoSuchItemException e) {
/* 16841 */           logWarn("This REALLY shouldn't happen!", (Throwable)e);
/*       */         } 
/*       */       } 
/*       */     }
/* 16845 */     if (Items.isItemLoaded(this.parentId)) {
/*       */       try {
/* 16847 */         Item parent = Items.getItem(this.parentId);
/* 16848 */         return parent.getPos2f();
/*       */       }
/* 16850 */       catch (NoSuchItemException nsi2) {
/* 16851 */         logWarn("This REALLY shouldn't happen!", (Throwable)nsi2);
/*       */       } 
/*       */     }
/* 16854 */     return new Vector2f(this.posX, this.posY);
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
/*       */   @Nonnull
/*       */   public final Vector3f getPos3f() {
/* 16868 */     if (this.parentId == -10L && !isBodyPartAttached() && !isInventory()) {
/* 16869 */       return new Vector3f(this.posX, this.posY, this.posZ);
/*       */     }
/* 16871 */     if (this.ownerId != -10L) {
/*       */       
/*       */       try {
/* 16874 */         Creature creature = Server.getInstance().getCreature(this.ownerId);
/* 16875 */         return creature.getPos3f();
/*       */       }
/* 16877 */       catch (NoSuchCreatureException nsc) {
/* 16878 */         if (!Items.isItemLoaded(this.parentId)) {
/* 16879 */           return new Vector3f(this.posX, this.posY, this.posZ);
/*       */         }
/*       */         try {
/* 16882 */           Item parent = Items.getItem(this.parentId);
/* 16883 */           return parent.getPos3f();
/*       */         }
/* 16885 */         catch (NoSuchItemException nsi) {
/* 16886 */           logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */         }
/*       */       
/* 16889 */       } catch (NoSuchPlayerException nsp) {
/* 16890 */         if (!Items.isItemLoaded(this.parentId)) {
/* 16891 */           return new Vector3f(this.posX, this.posY, this.posZ);
/*       */         }
/*       */         try {
/* 16894 */           Item parent = Items.getItem(this.parentId);
/* 16895 */           return parent.getPos3f();
/*       */         }
/* 16897 */         catch (NoSuchItemException nsi) {
/* 16898 */           logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */         } 
/*       */       } 
/*       */     }
/* 16902 */     if (Items.isItemLoaded(this.parentId)) {
/*       */       try {
/* 16904 */         Item parent = Items.getItem(this.parentId);
/* 16905 */         return parent.getPos3f();
/*       */       }
/* 16907 */       catch (NoSuchItemException nsi2) {
/* 16908 */         logWarn("This REALLY shouldn't happen!", (Throwable)nsi2);
/*       */       } 
/*       */     }
/* 16911 */     return new Vector3f(this.posX, this.posY, this.posZ);
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
/*       */   public final float getPosX() {
/* 16925 */     if (this.parentId == -10L && !isBodyPartAttached() && !isInventory()) {
/* 16926 */       return this.posX;
/*       */     }
/*       */     
/* 16929 */     if (this.ownerId != -10L) {
/*       */ 
/*       */       
/*       */       try {
/* 16933 */         Creature creature = Server.getInstance().getCreature(this.ownerId);
/* 16934 */         return creature.getStatus().getPositionX();
/*       */       }
/* 16936 */       catch (NoSuchCreatureException nsc) {
/*       */         
/* 16938 */         if (Items.isItemLoaded(this.parentId)) {
/*       */           try
/*       */           {
/*       */             
/* 16942 */             Item parent = Items.getItem(this.parentId);
/* 16943 */             return parent.getPosX();
/*       */           }
/* 16945 */           catch (NoSuchItemException nsi)
/*       */           {
/* 16947 */             logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */           }
/*       */         
/*       */         }
/* 16951 */       } catch (NoSuchPlayerException nsp) {
/*       */         
/* 16953 */         if (Items.isItemLoaded(this.parentId)) {
/*       */           try
/*       */           {
/*       */             
/* 16957 */             Item parent = Items.getItem(this.parentId);
/* 16958 */             return parent.getPosX();
/*       */           }
/* 16960 */           catch (NoSuchItemException nsi)
/*       */           {
/* 16962 */             logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */           }
/*       */         
/*       */         }
/*       */       }
/*       */     
/*       */     }
/* 16969 */     else if (Items.isItemLoaded(this.parentId)) {
/*       */ 
/*       */       
/*       */       try {
/* 16973 */         Item parent = Items.getItem(this.parentId);
/* 16974 */         if (!parent.isTemporary()) {
/* 16975 */           return parent.getPosX();
/*       */         }
/* 16977 */       } catch (NoSuchItemException nsi) {
/*       */         
/* 16979 */         logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */       } 
/*       */     } 
/*       */     
/* 16983 */     return this.posX;
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getPosXRaw() {
/* 16988 */     return this.posX;
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
/*       */   public final float getPosY() {
/* 17002 */     if (this.parentId == -10L && !isBodyPartAttached() && !isInventory()) {
/* 17003 */       return this.posY;
/*       */     }
/*       */     
/* 17006 */     if (this.ownerId != -10L) {
/*       */ 
/*       */       
/*       */       try {
/* 17010 */         Creature creature = Server.getInstance().getCreature(this.ownerId);
/* 17011 */         return creature.getStatus().getPositionY();
/*       */       }
/* 17013 */       catch (NoSuchCreatureException nsc) {
/*       */         
/* 17015 */         if (Items.isItemLoaded(this.parentId)) {
/*       */           try
/*       */           {
/*       */             
/* 17019 */             Item parent = Items.getItem(this.parentId);
/* 17020 */             return parent.getPosY();
/*       */           }
/* 17022 */           catch (NoSuchItemException nsi)
/*       */           {
/* 17024 */             logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */           }
/*       */         
/*       */         }
/* 17028 */       } catch (NoSuchPlayerException nsp) {
/*       */         
/* 17030 */         if (Items.isItemLoaded(this.parentId)) {
/*       */           try
/*       */           {
/*       */             
/* 17034 */             Item parent = Items.getItem(this.parentId);
/* 17035 */             return parent.getPosY();
/*       */           }
/* 17037 */           catch (NoSuchItemException nsi)
/*       */           {
/* 17039 */             logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */           }
/*       */         
/*       */         }
/*       */       }
/*       */     
/*       */     }
/* 17046 */     else if (Items.isItemLoaded(this.parentId)) {
/*       */ 
/*       */       
/*       */       try {
/* 17050 */         Item parent = Items.getItem(this.parentId);
/* 17051 */         return parent.getPosY();
/*       */       }
/* 17053 */       catch (NoSuchItemException nsi) {
/*       */         
/* 17055 */         logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */       } 
/*       */     } 
/*       */     
/* 17059 */     return this.posY;
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getPosYRaw() {
/* 17064 */     return this.posY;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final float getPosZ() {
/* 17074 */     if (this.parentId != -10L || isBodyPartAttached() || isInventory())
/*       */     {
/* 17076 */       if (this.ownerId != -10L) {
/*       */ 
/*       */         
/*       */         try {
/* 17080 */           Creature creature = Server.getInstance().getCreature(this.ownerId);
/* 17081 */           return creature.getStatus().getPositionZ() + creature.getAltOffZ();
/*       */         }
/* 17083 */         catch (NoSuchCreatureException nsc) {
/*       */           
/* 17085 */           if (Items.isItemLoaded(this.parentId)) {
/*       */             try
/*       */             {
/*       */               
/* 17089 */               Item parent = Items.getItem(this.parentId);
/* 17090 */               return parent.getPosZ();
/*       */             }
/* 17092 */             catch (NoSuchItemException nsi)
/*       */             {
/* 17094 */               logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */             }
/*       */           
/*       */           }
/* 17098 */         } catch (NoSuchPlayerException nsp) {
/*       */           
/* 17100 */           if (Items.isItemLoaded(this.parentId)) {
/*       */             try
/*       */             {
/*       */               
/* 17104 */               Item parent = Items.getItem(this.parentId);
/* 17105 */               return parent.getPosZ();
/*       */             }
/* 17107 */             catch (NoSuchItemException nsi)
/*       */             {
/* 17109 */               logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */             }
/*       */           
/*       */           }
/*       */         }
/*       */       
/*       */       }
/* 17116 */       else if (Items.isItemLoaded(this.parentId)) {
/*       */ 
/*       */         
/*       */         try {
/* 17120 */           Item parent = Items.getItem(this.parentId);
/* 17121 */           return parent.getPosZ();
/*       */         }
/* 17123 */         catch (NoSuchItemException nsi) {
/*       */           
/* 17125 */           logWarn("This REALLY shouldn't happen!", (Throwable)nsi);
/*       */         } 
/*       */       } 
/*       */     }
/*       */ 
/*       */     
/* 17131 */     if (isFloating())
/*       */     {
/* 17133 */       return Math.max(0.0F, this.posZ);
/*       */     }
/*       */     
/* 17136 */     return this.posZ;
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getPosZRaw() {
/* 17141 */     return this.posZ;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEdibleBy(Creature creature) {
/* 17146 */     if (creature.getTemplate().getTemplateId() == 93) {
/* 17147 */       return isFish();
/*       */     }
/* 17149 */     if (isDish() || (isMeat() && (!isBodyPart() || isBodyPartRemoved())) || isFish()) {
/* 17150 */       return (creature.isCarnivore() || creature.isOmnivore());
/*       */     }
/* 17152 */     if (isSeed() || getTemplateId() == 620) {
/* 17153 */       return (creature.isHerbivore() || creature.isOmnivore());
/*       */     }
/* 17155 */     if (isVegetable()) {
/* 17156 */       return (creature.isHerbivore() || creature.isOmnivore());
/*       */     }
/* 17158 */     if (isFood()) {
/* 17159 */       return creature.isOmnivore();
/*       */     }
/* 17161 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final byte getKingdom() {
/* 17166 */     if (isRoyal() || getTemplateId() == 272) {
/* 17167 */       return getAuxData();
/*       */     }
/*       */     
/* 17170 */     if (isKingdomMarker() || 
/* 17171 */       isWind() || 
/* 17172 */       isVehicle() || this.template.isKingdomFlag || 
/*       */       
/* 17174 */       isDuelRing() || 
/* 17175 */       isEpicTargetItem() || 
/* 17176 */       isWarTarget() || 
/* 17177 */       isTent() || this.template.useMaterialAndKingdom || 
/*       */       
/* 17179 */       isEnchantedTurret() || 
/* 17180 */       isProtectionTower())
/*       */     {
/* 17182 */       return getAuxData();
/*       */     }
/* 17184 */     return 0;
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
/*       */   public final boolean isWithin(int startX, int endX, int startY, int endY) {
/* 17197 */     return (getTileX() >= startX && getTileX() <= endX && 
/* 17198 */       getTileY() >= startY && getTileY() <= endY);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isDuelRing() {
/* 17203 */     return this.template.isDuelRing;
/*       */   }
/*       */ 
/*       */   
/*       */   public final long getBridgeId() {
/* 17208 */     return this.onBridge;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean mayCreatureInsertItem() {
/* 17213 */     if (isInventoryGroup()) {
/*       */ 
/*       */       
/* 17216 */       Item parent = getParentOrNull();
/* 17217 */       return (parent != null && parent.mayCreatureInsertItem());
/*       */     } 
/*       */     
/* 17220 */     if (isInventory() && getNumItemsNotCoins() < 100) {
/* 17221 */       return true;
/*       */     }
/* 17223 */     return (getItemCount() < 100);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final Item getParentOrNull() {
/*       */     try {
/* 17231 */       return getParent();
/*       */     }
/* 17233 */     catch (NoSuchItemException nsi) {
/*       */       
/* 17235 */       return null;
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getNumItemsNotCoins() {
/* 17241 */     if (this.items == null) {
/* 17242 */       return 0;
/*       */     }
/*       */     
/* 17245 */     int toReturn = 0;
/*       */     
/* 17247 */     for (Item nItem : this.items) {
/*       */       
/* 17249 */       if (!nItem.isCoin() && !nItem.isInventoryGroup() && nItem.getTemplateId() != 666) {
/* 17250 */         toReturn++; continue;
/*       */       } 
/* 17252 */       if (nItem.isInventoryGroup()) {
/* 17253 */         toReturn += nItem.getNumItemsNotCoins();
/*       */       }
/*       */     } 
/* 17256 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public int getNumberCages() {
/* 17262 */     if (this.items == null) {
/* 17263 */       return 0;
/*       */     }
/*       */     
/* 17266 */     int toReturn = 0;
/* 17267 */     for (Item nItem : getAllItems(true)) {
/*       */       
/* 17269 */       if (nItem.getTemplateId() == 1311)
/*       */       {
/* 17271 */         toReturn++;
/*       */       }
/*       */     } 
/* 17274 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getFat() {
/* 17280 */     return getData2() >> 1 & 0xFF;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHealing() {
/* 17285 */     return this.template.healing;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getAlchemyType() {
/* 17290 */     return this.template.alchemyType;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isButchered() {
/* 17295 */     return ((getData2() & 0x1) == 1);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMovingItem() {
/* 17300 */     return this.template.isMovingItem;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean spawnsTrees() {
/* 17305 */     return this.template.spawnsTrees;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean killsTrees() {
/* 17310 */     return this.template.killsTrees;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNonDeedable() {
/* 17315 */     return this.template.nonDeedable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getBulkTemplateId() {
/* 17320 */     return getData1();
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getBulkNums() {
/* 17325 */     if (isBulkItem()) {
/*       */ 
/*       */ 
/*       */       
/* 17329 */       ItemTemplate itemp = getRealTemplate();
/* 17330 */       if (itemp != null)
/* 17331 */         return Math.max(1, getWeightGrams() / itemp.getVolume()); 
/* 17332 */       return 0;
/*       */     } 
/* 17334 */     return getData2();
/*       */   }
/*       */ 
/*       */   
/*       */   public final float getBulkNumsFloat(boolean useMaxOne) {
/* 17339 */     if (isBulkItem()) {
/*       */ 
/*       */ 
/*       */       
/* 17343 */       ItemTemplate itemp = getRealTemplate();
/* 17344 */       if (itemp != null) {
/*       */         
/* 17346 */         if (useMaxOne) {
/* 17347 */           return Math.max(1.0F, getWeightGrams() / itemp.getVolume());
/*       */         }
/* 17349 */         return getWeightGrams() / itemp.getVolume();
/*       */       } 
/* 17351 */       return 0.0F;
/*       */     } 
/* 17353 */     return getData2();
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getPlacedItemCount() {
/* 17358 */     int itemsCount = 0;
/* 17359 */     boolean normalContainer = getTemplate().isContainerWithSubItems();
/* 17360 */     for (Item item : getItems()) {
/*       */       
/* 17362 */       if ((normalContainer && item.isPlacedOnParent()) || !normalContainer)
/* 17363 */         itemsCount++; 
/*       */     } 
/* 17365 */     return itemsCount;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getItemCount() {
/* 17370 */     int itemsCount = 0;
/* 17371 */     for (Item item : getItems()) {
/*       */       
/* 17373 */       if (!item.isInventoryGroup()) {
/* 17374 */         itemsCount++; continue;
/*       */       } 
/* 17376 */       itemsCount += item.getItemCount();
/*       */     } 
/* 17378 */     return itemsCount;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setBulkTemplateId(int newid) {
/* 17383 */     setData1(newid);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void updateName() {
/* 17388 */     if (getParentId() != -10L) {
/*       */ 
/*       */       
/* 17391 */       sendUpdate();
/*       */       
/*       */       return;
/*       */     } 
/* 17395 */     if (this.zoneId <= 0 || this.parentId != -10L) {
/*       */       return;
/*       */     }
/*       */     
/* 17399 */     VolaTile t = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 17400 */     if (t != null)
/*       */     {
/* 17402 */       t.renameItem(this);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setButchered() {
/* 17408 */     setData2(1);
/*       */     
/* 17410 */     if (this.ownerId != -10L) {
/*       */       return;
/*       */     }
/* 17413 */     if (this.zoneId == -10) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 17419 */       Zone z = Zones.getZone(this.zoneId);
/* 17420 */       z.removeItem(this);
/* 17421 */       z.addItem(this);
/*       */     }
/* 17423 */     catch (NoSuchZoneException nsz) {
/*       */       
/* 17425 */       logWarn("Zone at " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + ",surf=" + 
/* 17426 */           isOnSurface() + " no such zone.");
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   final void updateParents() {
/* 17432 */     sendUpdate();
/*       */     
/*       */     try {
/* 17435 */       Item parent = getParent();
/* 17436 */       parent.updateParents();
/*       */     }
/* 17438 */     catch (NoSuchItemException noSuchItemException) {}
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void sendUpdate() {
/* 17445 */     if (this.watchers == null) {
/*       */       return;
/*       */     }
/*       */     
/* 17449 */     for (Creature watcher : this.watchers)
/*       */     {
/* 17451 */       watcher.getCommunicator().sendUpdateInventoryItem(this);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isCreatureWearableOnly() {
/* 17457 */     return this.template.isCreatureWearableOnly();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUnfinished() {
/* 17462 */     return (this.template.getTemplateId() == 386 || this.template.getTemplateId() == 179);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void updateIfGroundItem() {
/* 17467 */     if (getParentId() != -10L || this.zoneId == -10) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 17473 */       Zone z = Zones.getZone(this.zoneId);
/* 17474 */       z.removeItem(this);
/* 17475 */       z.addItem(this);
/*       */     }
/* 17477 */     catch (NoSuchZoneException nsz) {
/*       */       
/* 17479 */       logWarn("Zone at " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + ",surf=" + 
/* 17480 */           isOnSurface() + " no such zone. Item: " + this);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void updateModelNameOnGroundItem() {
/* 17487 */     if (getParentId() != -10L || this.zoneId == -10) {
/*       */       return;
/*       */     }
/*       */ 
/*       */     
/*       */     try {
/* 17493 */       Zone z = Zones.getZone(this.zoneId);
/* 17494 */       z.updateModelName(this);
/*       */     }
/* 17496 */     catch (NoSuchZoneException nsz) {
/*       */       
/* 17498 */       logWarn("Zone at " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + ",surf=" + 
/* 17499 */           isOnSurface() + " no such zone. Item: " + this);
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public final void updatePos() {
/* 17505 */     if (getParentId() != -10L) {
/*       */ 
/*       */       
/*       */       try {
/* 17509 */         Item parent = getParent();
/*       */         
/* 17511 */         if (parent.getTemplateId() == 177)
/*       */         {
/* 17513 */           parent.removeFromPile(this);
/* 17514 */           parent.insertIntoPile(this);
/*       */         }
/*       */         else
/*       */         {
/* 17518 */           parent.dropItem(this.id, false);
/* 17519 */           parent.insertItem(this, true);
/*       */         }
/*       */       
/* 17522 */       } catch (NoSuchItemException nsi) {
/*       */         
/* 17524 */         logWarn("Item with id " + getWurmId() + " has no parent item with id " + getParentId(), new Exception());
/*       */       
/*       */       }
/*       */ 
/*       */     
/*       */     }
/* 17530 */     else if (this.zoneId != -10) {
/*       */ 
/*       */       
/*       */       try {
/* 17534 */         Zone z = Zones.getZone(this.zoneId);
/* 17535 */         z.removeItem(this);
/* 17536 */         z.addItem(this);
/*       */       }
/* 17538 */       catch (NoSuchZoneException nsz) {
/*       */         
/* 17540 */         logWarn("Zone at " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + ",surf=" + 
/* 17541 */             isOnSurface() + " no such zone. Item: " + this);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isStreetLamp() {
/* 17549 */     return this.template.streetlamp;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSaddleLarge() {
/* 17554 */     return (this.template.getTemplateId() == 622);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSaddleNormal() {
/* 17559 */     return (this.template.getTemplateId() == 621);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isHorseShoe() {
/* 17564 */     return (this.template.getTemplateId() == 623);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isBridle() {
/* 17569 */     return (this.template.getTemplateId() == 624);
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setTempPositions(float posx, float posy, float posz, float rot) {
/* 17574 */     this.posX = posx;
/* 17575 */     this.posY = posy;
/* 17576 */     this.posZ = posz;
/* 17577 */     this.rotation = rot;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setTempXPosition(float posx) {
/* 17582 */     this.posX = posx;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setTempYPosition(float posy) {
/* 17587 */     this.posY = posy;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setTempZandRot(float posz, float rot) {
/* 17592 */     this.posZ = posz;
/* 17593 */     this.rotation = rot;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final byte getLeftAuxData() {
/* 17601 */     return (byte)(this.auxbyte >> 4 & 0xF);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final byte getRightAuxData() {
/* 17609 */     return (byte)(this.auxbyte & 0xF);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDestroyedOnDecay() {
/* 17614 */     return this.template.destroyOnDecay;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isWarTarget() {
/* 17619 */     return this.template.isWarTarget;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isVisibleDecay() {
/* 17624 */     return this.template.visibleDecay;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoDiscard() {
/* 17629 */     return (isCoin() || 
/* 17630 */       isNewbieItem() || 
/* 17631 */       isChallengeNewbieItem() || 
/* 17632 */       isLiquid() || this.template
/* 17633 */       .isNoDiscard() || 
/* 17634 */       isBodyPart() || this.template
/* 17635 */       .getTemplateId() == 862 || this.template
/* 17636 */       .getValue() > 5000 || 
/* 17637 */       getValue() > 100 || 
/* 17638 */       isIndestructible() || 
/* 17639 */       getSpellEffects() != null || this.enchantment != 0 || 
/*       */       
/* 17641 */       isMagic() || 
/* 17642 */       isNoDrop() || 
/* 17643 */       isInventory() || 
/* 17644 */       isNoTrade() || 
/* 17645 */       getRarity() > 0 || (
/* 17646 */       isHollow() && !isEmpty(false)));
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isInstaDiscard() {
/* 17651 */     return this.template.isInstaDiscard();
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setLeftAuxData(int ldata) {
/* 17656 */     setAuxData((byte)(getRightAuxData() + (ldata << 4 & 0xF0)));
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setRightAuxData(int rdata) {
/* 17661 */     setAuxData((byte)((getLeftAuxData() << 4) + (rdata & 0xF)));
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEpicPortal() {
/* 17666 */     return this.template.isEpicPortal;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUnstableRift() {
/* 17671 */     return this.template.isUnstableRift();
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setSurfaced(boolean newValue) {
/* 17676 */     this.surfaced = newValue;
/* 17677 */     if (isHollow() && this.items != null)
/*       */     {
/* 17679 */       for (Item item : getAllItems(true, true)) {
/* 17680 */         item.setSurfacedNotRecursive(this.surfaced);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   private final void setSurfacedNotRecursive(boolean newValue) {
/* 17687 */     this.surfaced = newValue;
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getSizeX(boolean useModifier) {
/* 17945 */     if (useModifier) {
/* 17946 */       return getSizeX();
/*       */     }
/* 17948 */     return this.sizeX;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getSizeY(boolean useModifier) {
/* 17959 */     if (useModifier) {
/* 17960 */       return getSizeY();
/*       */     }
/* 17962 */     return this.sizeY;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getSizeZ(boolean useModifier) {
/* 17973 */     if (useModifier) {
/* 17974 */       return getSizeZ();
/*       */     }
/* 17976 */     return this.sizeZ;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getContainerSizeX() {
/* 17981 */     float modifier = 1.0F;
/* 17982 */     if (getSpellEffects() != null)
/*       */     {
/* 17984 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*       */     }
/*       */     
/* 17987 */     if (this.template.usesSpecifiedContainerSizes())
/* 17988 */       return (int)(this.template.getContainerSizeX() * modifier); 
/* 17989 */     return getSizeX();
/*       */   }
/*       */ 
/*       */   
/*       */   public int getContainerSizeY() {
/* 17994 */     float modifier = 1.0F;
/* 17995 */     if (getSpellEffects() != null)
/*       */     {
/* 17997 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*       */     }
/*       */     
/* 18000 */     if (this.template.usesSpecifiedContainerSizes())
/* 18001 */       return (int)(this.template.getContainerSizeY() * modifier); 
/* 18002 */     return getSizeY();
/*       */   }
/*       */ 
/*       */   
/*       */   public int getContainerSizeZ() {
/* 18007 */     float modifier = 1.0F;
/* 18008 */     if (getSpellEffects() != null)
/*       */     {
/* 18010 */       modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_SIZE);
/*       */     }
/*       */     
/* 18013 */     if (this.template.usesSpecifiedContainerSizes())
/* 18014 */       return (int)(this.template.getContainerSizeZ() * modifier); 
/* 18015 */     return getSizeZ();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getWeightGrams(boolean useModifier) {
/* 18022 */     if (useModifier) {
/* 18023 */       return getWeightGrams();
/*       */     }
/* 18025 */     return this.weight;
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final long getLastOwnerId() {
/* 18167 */     return this.lastOwner;
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
/*       */   final byte getCreationState() {
/* 18180 */     return this.creationState;
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
/*       */   public final int getRealTemplateId() {
/* 18197 */     return this.realTemplate;
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
/*       */   final boolean isWornAsArmour() {
/* 18217 */     return this.wornAsArmour;
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
/*       */   public final int getColor() {
/* 18236 */     if (isStreetLamp() || isLight() || isLightBright())
/*       */     {
/* 18238 */       if (this.color == WurmColor.createColor(0, 0, 0))
/*       */       {
/* 18240 */         setColor(WurmColor.createColor(1, 1, 1));
/*       */       }
/*       */     }
/* 18243 */     if (getTemplateId() == 531)
/* 18244 */       return WurmColor.createColor(40, 40, 215); 
/* 18245 */     if (getTemplateId() == 534)
/* 18246 */       return WurmColor.createColor(215, 40, 40); 
/* 18247 */     if (getTemplateId() == 537) {
/* 18248 */       return WurmColor.createColor(10, 10, 10);
/*       */     }
/* 18250 */     return this.color;
/*       */   }
/*       */ 
/*       */   
/*       */   public final int getColor2() {
/* 18255 */     if (getTemplateId() == 531)
/* 18256 */       return WurmColor.createColor(0, 130, 0); 
/* 18257 */     if (getTemplateId() == 534)
/* 18258 */       return WurmColor.createColor(255, 255, 0); 
/* 18259 */     if (getTemplateId() == 537) {
/* 18260 */       return WurmColor.createColor(110, 0, 150);
/*       */     }
/* 18262 */     return this.color2;
/*       */   }
/*       */ 
/*       */   
/*       */   public final String getSecondryItemName() {
/* 18267 */     return this.template.getSecondryItemName();
/*       */   }
/*       */ 
/*       */   
/*       */   public final byte getMailTimes() {
/* 18272 */     return this.mailTimes;
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
/*       */   final boolean isFemale() {
/* 18289 */     return this.female;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMushroom() {
/* 18294 */     return this.template.isMushroom();
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
/*       */   public final boolean isTransferred() {
/* 18311 */     return this.transferred;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMagicContainer() {
/* 18316 */     return this.template.magicContainer;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean willLeaveServer(boolean leaving, boolean changingCluster, boolean ownerDeity) {
/* 18321 */     if (isBodyPartAttached()) {
/* 18322 */       return true;
/*       */     }
/*       */     
/* 18325 */     if (isServerBound())
/*       */     {
/* 18327 */       if (isVillageDeed() || isHomesteadDeed()) {
/*       */         
/* 18329 */         if (getData2() > 0) {
/*       */           
/* 18331 */           if (leaving)
/* 18332 */             setTransferred(true); 
/* 18333 */           return false;
/*       */         } 
/*       */ 
/*       */         
/* 18337 */         if (leaving) {
/* 18338 */           setTransferred(false);
/*       */         }
/*       */       } else {
/*       */         
/* 18342 */         if (getTemplateId() == 166) {
/* 18343 */           if (leaving)
/* 18344 */             setTransferred(true); 
/* 18345 */           return false;
/*       */         } 
/* 18347 */         if (getTemplateId() == 300 || getTemplateId() == 1129) {
/*       */           
/* 18349 */           if (getData() > 0L) {
/*       */             
/* 18351 */             if (leaving)
/* 18352 */               setTransferred(true); 
/* 18353 */             return false;
/*       */           } 
/*       */ 
/*       */           
/* 18357 */           if (leaving) {
/* 18358 */             setTransferred(false);
/*       */           }
/*       */         } else {
/* 18361 */           if (isRoyal()) {
/*       */             
/* 18363 */             if (leaving)
/*       */             {
/* 18365 */               setTransferred(true);
/*       */             }
/* 18367 */             return false;
/*       */           } 
/* 18369 */           if (isArtifact()) {
/*       */             
/* 18371 */             if (leaving) {
/*       */               try {
/*       */                 String act;
/*       */                 
/* 18375 */                 getParent().dropItem(getWurmId(), false);
/*       */                 
/* 18377 */                 switch (Server.rand.nextInt(6)) {
/*       */                   
/*       */                   case 0:
/* 18380 */                     act = "is reported to have disappeared."; break;
/* 18381 */                   case 1: act = "is gone missing."; break;
/* 18382 */                   case 2: act = "returned to the depths."; break;
/* 18383 */                   case 3: act = "seems to have decided to leave."; break;
/* 18384 */                   case 4: act = "has found a new location."; break;
/* 18385 */                   default: act = "has vanished.";
/*       */                     break;
/*       */                 } 
/* 18388 */                 HistoryManager.addHistory("The " + getName(), act);
/* 18389 */                 int onethird = Zones.worldTileSizeX / 3;
/* 18390 */                 int ntx = onethird + Server.rand.nextInt(onethird);
/* 18391 */                 int nty = onethird + Server.rand.nextInt(onethird);
/* 18392 */                 float npx = (ntx * 4 + 2);
/* 18393 */                 float npy = (nty * 4 + 2);
/* 18394 */                 setPosXY(npx, npy);
/*       */                 
/* 18396 */                 Zone z = Zones.getZone(ntx, nty, true);
/* 18397 */                 z.addItem(this);
/*       */               }
/* 18399 */               catch (NoSuchItemException nsi) {
/*       */                 
/* 18401 */                 logWarn(getName() + ", " + getWurmId() + " no parent " + nsi.getMessage(), (Throwable)nsi);
/*       */               }
/* 18403 */               catch (NoSuchZoneException nz) {
/*       */                 
/* 18405 */                 logWarn(getName() + ", " + getWurmId() + " no zone " + nz.getMessage(), (Throwable)nz);
/*       */               } 
/*       */             }
/* 18408 */             return false;
/*       */           } 
/*       */ 
/*       */           
/* 18412 */           if (leaving)
/* 18413 */             setTransferred(true); 
/* 18414 */           return false;
/*       */         } 
/*       */       } 
/*       */     }
/* 18418 */     if (changingCluster && !ownerDeity) {
/*       */       
/* 18420 */       if (isNewbieItem()) {
/* 18421 */         return true;
/*       */       }
/*       */       
/* 18424 */       if (leaving)
/* 18425 */         setTransferred(true); 
/* 18426 */       return false;
/*       */     } 
/*       */ 
/*       */ 
/*       */     
/* 18431 */     if (isTransferred()) {
/* 18432 */       setTransferred(false);
/*       */     }
/* 18434 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final int getFloorLevel() {
/*       */     try {
/*       */       float z2;
/* 18445 */       Vector2f pos2f = getPos2f();
/* 18446 */       TilePos tilePos = CoordUtils.WorldToTile(pos2f);
/* 18447 */       Zone zone = Zones.getZone(tilePos, isOnSurface());
/* 18448 */       VolaTile tile = zone.getOrCreateTile(tilePos);
/*       */       
/* 18450 */       if (tile.getStructure() == null) {
/* 18451 */         return 0;
/*       */       }
/*       */       
/* 18454 */       float posZ = getPosZ();
/*       */       
/* 18456 */       long bridgeId = getBridgeId();
/*       */       
/* 18458 */       if (bridgeId > 0L) {
/* 18459 */         z2 = Zones.calculatePosZ(pos2f.x, pos2f.y, tile, isOnSurface(), false, posZ, null, bridgeId);
/*       */       } else {
/* 18461 */         z2 = Zones.calculateHeight(pos2f.x, pos2f.y, isOnSurface());
/*       */       } 
/* 18463 */       int floor = (int)(Math.max(0.0F, posZ - z2 + 0.5F) * 10.0F) / 30;
/*       */       
/* 18465 */       return floor;
/*       */     }
/* 18467 */     catch (NoSuchZoneException snz) {
/*       */       
/* 18469 */       return 0;
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isMailed() {
/* 18514 */     return this.mailed;
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
/*       */   final boolean isHidden() {
/* 18538 */     return this.hidden;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSpringFilled() {
/* 18543 */     return this.template.isSpringFilled;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isTurnable() {
/* 18552 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_TURNABLE.getBit()))
/* 18553 */       return false; 
/* 18554 */     return templateTurnable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateTurnable() {
/* 18559 */     return this.template.turnable;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean templateNoTake() {
/* 18564 */     if (isUnfinished() && this.realTemplate > 0) {
/* 18565 */       ItemTemplate realTemplate = getRealTemplate();
/* 18566 */       assert realTemplate != null;
/* 18567 */       return realTemplate.isUnfinishedNoTake;
/*       */     } 
/* 18569 */     return this.template.notake;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isColorable() {
/* 18578 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_PAINTABLE.getBit()))
/* 18579 */       return false; 
/* 18580 */     return templateIsColorable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSourceSpring() {
/* 18585 */     return this.template.isSourceSpring;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSource() {
/* 18590 */     return this.template.isSource;
/*       */   }
/*       */ 
/*       */   
/*       */   public byte getRarity() {
/* 18595 */     return this.rarity;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDredgingTool() {
/* 18600 */     return this.template.isDredgingTool;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isTent() {
/* 18605 */     return this.template.isTent();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isUseMaterialAndKingdom() {
/* 18610 */     return this.template.useMaterialAndKingdom;
/*       */   }
/*       */ 
/*       */   
/*       */   public final void setProtected(boolean isProtected) {
/* 18615 */     Items.setProtected(getWurmId(), isProtected);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isCorpseLootable() {
/* 18620 */     return !Items.isProtected(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static ItemTransferDatabaseLogger getItemlogger() {
/* 18630 */     return itemLogger;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isColorComponent() {
/* 18635 */     return this.template.isColorComponent;
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
/*       */   public long onBridge() {
/* 18651 */     return this.onBridge;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setOnBridge(long theBridge) {
/* 18661 */     this.onBridge = theBridge;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setSettings(int newSettings) {
/* 18667 */     this.permissions.setPermissionBits(newSettings);
/*       */   }
/*       */ 
/*       */   
/*       */   public final Permissions getSettings() {
/* 18672 */     return this.permissions;
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
/*       */   public final int hashCode() {
/* 18685 */     int prime = 31;
/* 18686 */     int result = 1;
/* 18687 */     result = 31 * result + (int)(this.id ^ this.id >>> 32L);
/* 18688 */     return result;
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
/*       */   public final boolean equals(Object obj) {
/* 18701 */     if (this == obj)
/* 18702 */       return true; 
/* 18703 */     if (obj == null)
/* 18704 */       return false; 
/* 18705 */     if (!(obj instanceof Item))
/* 18706 */       return false; 
/* 18707 */     Item other = (Item)obj;
/* 18708 */     if (this.id != other.id)
/* 18709 */       return false; 
/* 18710 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   @Nonnull
/*       */   public String toString() {
/* 18717 */     String toReturn = "Item [ID: " + getWurmId() + ", Name: " + getName();
/*       */     
/* 18719 */     if (getTemplate() != null && 
/* 18720 */       getTemplate().getName() != null) {
/* 18721 */       toReturn = toReturn + ", Template: " + getTemplate().getName();
/*       */     }
/* 18723 */     if (getRealTemplate() != null && 
/* 18724 */       getRealTemplate().getName() != null) {
/* 18725 */       toReturn = toReturn + ", Realtemplate: " + getRealTemplate().getName();
/*       */     }
/* 18727 */     toReturn = toReturn + ", QL: " + getQualityLevel() + ", Rarity: " + getRarity();
/* 18728 */     toReturn = toReturn + ", Tile: " + getTileX() + ',' + getTileY() + ']';
/*       */     
/* 18730 */     return toReturn;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheNorthWest() {
/* 18740 */     return (getTileX() < Zones.worldTileSizeX / 3 && 
/* 18741 */       getTileY() < Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheNorthEast() {
/* 18750 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3 && 
/* 18751 */       getTileY() < Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheSouthEast() {
/* 18760 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3 && 
/* 18761 */       getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTheSouthWest() {
/* 18770 */     return (getTileX() < Zones.worldTileSizeX / 3 && 
/* 18771 */       getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheNorth() {
/* 18776 */     return (getTileY() < Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheEast() {
/* 18781 */     return (getTileX() > Zones.worldTileSizeX - Zones.worldTileSizeX / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheSouth() {
/* 18786 */     return (getTileY() > Zones.worldTileSizeY - Zones.worldTileSizeY / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInTheWest() {
/* 18791 */     return (getTileX() < Zones.worldTileSizeX / 3);
/*       */   }
/*       */ 
/*       */   
/*       */   public int getGlobalMapPlacement() {
/* 18796 */     if (isInTheNorthWest())
/* 18797 */       return 7; 
/* 18798 */     if (isInTheNorthEast())
/* 18799 */       return 1; 
/* 18800 */     if (isInTheSouthEast())
/* 18801 */       return 3; 
/* 18802 */     if (isInTheSouthWest())
/* 18803 */       return 5; 
/* 18804 */     return -1;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOwnedByWagoner() {
/* 18813 */     if (WurmId.getType(this.lastOwner) == 1)
/*       */     {
/* 18815 */       return (Wagoner.getWagoner(this.lastOwner) != null);
/*       */     }
/* 18817 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isWagonerCamp() {
/* 18827 */     long data = getData();
/* 18828 */     if (data == -1L) {
/* 18829 */       return false;
/*       */     }
/* 18831 */     if (WurmId.getType(data) == 1)
/*       */     {
/* 18833 */       if (Wagoner.getWagoner(data) != null)
/* 18834 */         return true; 
/*       */     }
/* 18836 */     setData(-10L);
/* 18837 */     return false;
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
/*       */   public final int getPotionTemplateIdForBlood() {
/* 18854 */     int toReturn = -1;
/* 18855 */     switch (getData2())
/*       */     
/*       */     { case 27:
/* 18858 */         toReturn = 881;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 18906 */         return toReturn;case 26: toReturn = 874; return toReturn;case 16: toReturn = 872; return toReturn;case 103: toReturn = 871; return toReturn;case 18: toReturn = 876; return toReturn;case 17: toReturn = 886; return toReturn;case 19: toReturn = 888; return toReturn;case 22: toReturn = 879; return toReturn;case 20: toReturn = 883; return toReturn;case 70: toReturn = 880; return toReturn;case 89: toReturn = 884; return toReturn;case 92: toReturn = 878; return toReturn;case 91: toReturn = 1413; return toReturn;case 104: toReturn = 877; return toReturn;case 90: toReturn = 875; return toReturn; }  toReturn = 884; return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public static final int getRandomImbuePotionTemplateId() {
/* 18911 */     int toReturn = -1;
/* 18912 */     switch (Server.rand.nextInt(17))
/*       */     
/*       */     { case 0:
/* 18915 */         toReturn = 881;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 18967 */         return toReturn;case 1: toReturn = 874; return toReturn;case 2: toReturn = 872; return toReturn;case 3: toReturn = 871; return toReturn;case 4: toReturn = 876; return toReturn;case 5: toReturn = 886; return toReturn;case 6: toReturn = 888; return toReturn;case 7: toReturn = 879; return toReturn;case 8: toReturn = 883; return toReturn;case 9: toReturn = 880; return toReturn;case 10: toReturn = 882; return toReturn;case 11: toReturn = 878; return toReturn;case 12: toReturn = 873; return toReturn;case 13: toReturn = 877; return toReturn;case 14: toReturn = 875; return toReturn;case 15: toReturn = 1413; return toReturn; }  toReturn = 884; return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSmearable() {
/* 18972 */     return this.template.isSmearable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canBePapyrusWrapped() {
/* 18977 */     return this.template.canBePapyrusWrapped();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canBeRawWrapped() {
/* 18982 */     return this.template.canBeRawWrapped();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean canBeClothWrapped() {
/* 18987 */     return this.template.canBeClothWrapped();
/*       */   }
/*       */ 
/*       */   
/*       */   public final byte getEnchantForPotion() {
/* 18992 */     byte toReturn = -1;
/* 18993 */     switch (getTemplateId())
/*       */     
/*       */     { case 881:
/* 18996 */         toReturn = 86;
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */         
/* 19056 */         return toReturn;case 874: toReturn = 79; return toReturn;case 871: toReturn = 77; return toReturn;case 876: toReturn = 81; return toReturn;case 886: toReturn = 90; return toReturn;case 888: toReturn = 92; return toReturn;case 879: toReturn = 84; return toReturn;case 883: toReturn = 88; return toReturn;case 880: toReturn = 85; return toReturn;case 872: toReturn = 78; return toReturn;case 873: toReturn = 76; return toReturn;case 875: toReturn = 80; return toReturn;case 877: toReturn = 82; return toReturn;case 878: toReturn = 83; return toReturn;case 887: toReturn = 91; return toReturn;case 884: toReturn = 89; return toReturn;case 882: toReturn = 87; return toReturn;case 1413: toReturn = 99; return toReturn;case 1091: toReturn = 98; return toReturn; }  toReturn = -1; return toReturn;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean mayFireTrebuchet() {
/* 19061 */     return (WurmCalendar.currentTime - getLastMaintained() > 120L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBeAlwaysLit() {
/* 19072 */     return (isLight() && !templateAlwaysLit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBeAutoFilled() {
/* 19083 */     return isSpringFilled();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBeAutoLit() {
/* 19094 */     return (isLight() && !templateAlwaysLit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBePlanted() {
/* 19105 */     return this.template.isPlantable();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isPlantOneAWeek() {
/* 19110 */     return this.template.isPlantOneAWeeek();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean descIsName() {
/* 19115 */     return this.template.descIsName;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBeSealedByPlayer() {
/* 19126 */     if (!this.template.canBeSealed())
/* 19127 */       return false; 
/* 19128 */     Item[] items = getItemsAsArray();
/* 19129 */     return (items.length == 1 && items[0].isLiquid());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canBePeggedByPlayer() {
/* 19140 */     if (!this.template.canBePegged())
/* 19141 */       return false; 
/* 19142 */     Item[] items = getItemsAsArray();
/* 19143 */     return (items.length == 1 && items[0].isLiquid() && !items[0].isFermenting());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canChangeCreator() {
/* 19154 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableDecay() {
/* 19165 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableDestroy() {
/* 19176 */     return !templateIndestructible();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableDrag() {
/* 19187 */     return this.template.draggable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableDrop() {
/* 19198 */     return !this.template.nodrop;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableEatAndDrink() {
/* 19209 */     return (isFood() || isLiquid());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableImprove() {
/* 19220 */     return !this.template.isNoImprove();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableLocking() {
/* 19231 */     return templateIsLockable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableLockpicking() {
/* 19242 */     return templateIsLockable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableMoveable() {
/* 19253 */     return !templateIsNoMove();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableOwnerMoveing() {
/* 19264 */     return !this.template.isOwnerMoveable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableOwnerTurning() {
/* 19275 */     return !this.template.isOwnerTurnable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisablePainting() {
/* 19286 */     return templateIsColorable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisablePut() {
/* 19297 */     return !this.template.isNoPut;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableRepair() {
/* 19308 */     return isRepairableDefault();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canDisableRuneing() {
/* 19319 */     return !this.template.isNotRuneable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableSpellTarget() {
/* 19330 */     return !this.template.cannotBeSpellTarget();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableTake() {
/* 19341 */     return !templateNoTake();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canDisableTurning() {
/* 19352 */     return templateTurnable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canHaveCourier() {
/* 19363 */     return (isMailBox() || isSpringFilled() || isUnenchantedTurret() || isPuppet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean canHaveDakrMessenger() {
/* 19374 */     return (isMailBox() || isSpringFilled() || isUnenchantedTurret() || isPuppet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nonnull
/*       */   public final String getCreatorName() {
/* 19386 */     if (this.creator != null && !this.creator.isEmpty())
/*       */     {
/* 19388 */       return this.creator;
/*       */     }
/* 19390 */     return "";
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
/*       */   public final boolean hasCourier() {
/* 19417 */     return this.permissions.hasPermission(Permissions.Allow.HAS_COURIER.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean hasDarkMessenger() {
/* 19428 */     return this.permissions.hasPermission(Permissions.Allow.HAS_DARK_MESSENGER.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean hasNoDecay() {
/* 19439 */     return this.permissions.hasPermission(Permissions.Allow.DECAY_DISABLED.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isAlwaysLit() {
/* 19450 */     if (this.permissions.hasPermission(Permissions.Allow.ALWAYS_LIT.getBit()))
/* 19451 */       return true; 
/* 19452 */     return templateAlwaysLit();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isAutoFilled() {
/* 19463 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_FILL.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isAutoLit() {
/* 19474 */     return this.permissions.hasPermission(Permissions.Allow.AUTO_LIGHT.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isIndestructible() {
/* 19485 */     if (this.permissions.hasPermission(Permissions.Allow.NO_BASH.getBit()))
/* 19486 */       return true; 
/* 19487 */     if (getTemplateId() == 1112)
/*       */     {
/* 19489 */       if (getData() != -1L || Items.isWaystoneInUse(getWurmId()))
/* 19490 */         return true; 
/*       */     }
/* 19492 */     if (getTemplateId() == 1309 && isSealedByPlayer())
/* 19493 */       return true; 
/* 19494 */     return templateIndestructible();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoDrag() {
/* 19505 */     if (this.permissions.hasPermission(Permissions.Allow.NO_DRAG.getBit()))
/* 19506 */       return true; 
/* 19507 */     return !this.template.draggable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoDrop() {
/* 19518 */     if (this.permissions.hasPermission(Permissions.Allow.NO_DROP.getBit()))
/* 19519 */       return true; 
/* 19520 */     if (this.realTemplate > 0) {
/* 19521 */       ItemTemplate realTemplate = getRealTemplate();
/* 19522 */       assert realTemplate != null;
/* 19523 */       return realTemplate.nodrop;
/*       */     } 
/* 19525 */     return this.template.nodrop;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isNoEatOrDrink() {
/* 19536 */     return this.permissions.hasPermission(Permissions.Allow.NO_EAT_OR_DRINK.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoImprove() {
/* 19547 */     if (this.permissions.hasPermission(Permissions.Allow.NO_IMPROVE.getBit()))
/* 19548 */       return true; 
/* 19549 */     return this.template.isNoImprove();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoMove() {
/* 19560 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_MOVEABLE.getBit()))
/* 19561 */       return true; 
/* 19562 */     return templateIsNoMove();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoPut() {
/* 19573 */     if (this.permissions.hasPermission(Permissions.Allow.NO_PUT.getBit()))
/* 19574 */       return true; 
/* 19575 */     if (getTemplateId() == 1342)
/*       */     {
/* 19577 */       return (!isPlanted() && getData() == -1L);
/*       */     }
/* 19579 */     return this.template.isNoPut;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isNoRepair() {
/* 19590 */     return !isRepairable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNoTake() {
/* 19601 */     if (this.permissions.hasPermission(Permissions.Allow.NO_TAKE.getBit()))
/* 19602 */       return true; 
/* 19603 */     if (templateNoTake())
/* 19604 */       return true; 
/* 19605 */     if ((getTemplateId() == 1312 || getTemplateId() == 1309) && !isEmpty(false))
/* 19606 */       return true; 
/* 19607 */     if (getTemplateId() == 1315 && !isEmpty(false))
/* 19608 */       return true; 
/* 19609 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isNoTake(Creature creature) {
/* 19614 */     if (isNoTake())
/* 19615 */       return true; 
/* 19616 */     if (getTemplateId() == 272 && this.wasBrandedTo != -10L) {
/*       */       
/* 19618 */       if (mayCommand(creature))
/* 19619 */         return false; 
/* 19620 */       return true;
/*       */     } 
/* 19622 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNotLockable() {
/* 19633 */     return !isLockable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isLockable() {
/* 19642 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_LOCKABLE.getBit()))
/* 19643 */       return false; 
/* 19644 */     return templateIsLockable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNotLockpickable() {
/* 19655 */     return !isLockpickable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isLockpickable() {
/* 19664 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_LOCKPICKABLE.getBit()))
/* 19665 */       return false; 
/* 19666 */     return templateIsLockable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNotPaintable() {
/* 19677 */     return !isColorable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isNotRuneable() {
/* 19688 */     if (this.permissions.hasPermission(Permissions.Allow.NOT_RUNEABLE.getBit()))
/* 19689 */       return true; 
/* 19690 */     return this.template.isNotRuneable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNotSpellTarget() {
/* 19701 */     if (this.permissions.hasPermission(Permissions.Allow.NO_SPELLS.getBit()))
/* 19702 */       return true; 
/* 19703 */     return this.template.cannotBeSpellTarget();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isNotTurnable() {
/* 19714 */     return !isTurnable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOwnerMoveable() {
/* 19725 */     if (this.permissions.hasPermission(Permissions.Allow.OWNER_MOVEABLE.getBit()))
/* 19726 */       return true; 
/* 19727 */     return this.template.isOwnerMoveable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isOwnerTurnable() {
/* 19738 */     if (this.permissions.hasPermission(Permissions.Allow.OWNER_TURNABLE.getBit()))
/* 19739 */       return true; 
/* 19740 */     return this.template.isOwnerTurnable;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isPlanted() {
/* 19751 */     return this.permissions.hasPermission(Permissions.Allow.PLANTED.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean isSealedByPlayer() {
/* 19762 */     if (this.isSealedOverride)
/* 19763 */       return true; 
/* 19764 */     if (this.permissions.hasPermission(Permissions.Allow.SEALED_BY_PLAYER.getBit()))
/* 19765 */       return true; 
/* 19766 */     return false;
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
/*       */   public final void setHasCourier(boolean aCourier) {
/* 19793 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_COURIER.getBit(), aCourier);
/*       */     
/* 19795 */     savePermissions();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setHasDarkMessenger(boolean aDarkmessenger) {
/* 19806 */     this.permissions.setPermissionBit(Permissions.Allow.HAS_DARK_MESSENGER.getBit(), aDarkmessenger);
/*       */     
/* 19808 */     savePermissions();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setHasNoDecay(boolean aNoDecay) {
/* 19819 */     this.permissions.setPermissionBit(Permissions.Allow.DECAY_DISABLED.getBit(), aNoDecay);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsAlwaysLit(boolean aAlwaysLit) {
/* 19830 */     this.permissions.setPermissionBit(Permissions.Allow.ALWAYS_LIT.getBit(), aAlwaysLit);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsAutoFilled(boolean aAutoFill) {
/* 19841 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_FILL.getBit(), aAutoFill);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsAutoLit(boolean aAutoLight) {
/* 19852 */     this.permissions.setPermissionBit(Permissions.Allow.AUTO_LIGHT.getBit(), aAutoLight);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsIndestructible(boolean aNoDestroy) {
/* 19863 */     this.permissions.setPermissionBit(Permissions.Allow.NO_BASH.getBit(), aNoDestroy);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoDrag(boolean aNoDrag) {
/* 19874 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DRAG.getBit(), aNoDrag);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoDrop(boolean aNoDrop) {
/* 19885 */     this.permissions.setPermissionBit(Permissions.Allow.NO_DROP.getBit(), aNoDrop);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoEatOrDrink(boolean aNoEatOrDrink) {
/* 19896 */     this.permissions.setPermissionBit(Permissions.Allow.NO_EAT_OR_DRINK.getBit(), aNoEatOrDrink);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoImprove(boolean aNoImprove) {
/* 19907 */     this.permissions.setPermissionBit(Permissions.Allow.NO_IMPROVE.getBit(), aNoImprove);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNoMove(boolean aNoMove) {
/* 19918 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_MOVEABLE.getBit(), aNoMove);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoPut(boolean aNoPut) {
/* 19929 */     this.permissions.setPermissionBit(Permissions.Allow.NO_PUT.getBit(), aNoPut);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNoRepair(boolean aNoRepair) {
/* 19940 */     this.permissions.setPermissionBit(Permissions.Allow.NO_REPAIR.getBit(), aNoRepair);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNoTake(boolean aNoTake) {
/* 19951 */     this.permissions.setPermissionBit(Permissions.Allow.NO_TAKE.getBit(), aNoTake);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNotLockable(boolean aNoLock) {
/* 19962 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKABLE.getBit(), aNoLock);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNotLockpickable(boolean aNoLockpick) {
/* 19973 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_LOCKPICKABLE.getBit(), aNoLockpick);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNotPaintable(boolean aNoPaint) {
/* 19984 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_PAINTABLE.getBit(), aNoPaint);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsNotRuneable(boolean aNoRune) {
/* 19995 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_RUNEABLE.getBit(), aNoRune);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNotSpellTarget(boolean aNoSpells) {
/* 20006 */     this.permissions.setPermissionBit(Permissions.Allow.NO_SPELLS.getBit(), aNoSpells);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsNotTurnable(boolean aNoTurn) {
/* 20017 */     this.permissions.setPermissionBit(Permissions.Allow.NOT_TURNABLE.getBit(), aNoTurn);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsOwnerMoveable(boolean aOwnerMove) {
/* 20028 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_MOVEABLE.getBit(), aOwnerMove);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsOwnerTurnable(boolean aOwnerTurn) {
/* 20039 */     this.permissions.setPermissionBit(Permissions.Allow.OWNER_TURNABLE.getBit(), aOwnerTurn);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final void setIsPlanted(boolean aPlant) {
/* 20050 */     boolean wasPlanted = isPlanted();
/* 20051 */     this.permissions.setPermissionBit(Permissions.Allow.PLANTED.getBit(), aPlant);
/*       */ 
/*       */ 
/*       */     
/* 20055 */     if (isRoadMarker())
/*       */     {
/*       */       
/* 20058 */       if (!aPlant && wasPlanted) {
/*       */         
/* 20060 */         MethodsHighways.removeLinksTo(this);
/* 20061 */         setIsNoTake(false);
/*       */       }
/* 20063 */       else if (aPlant && !wasPlanted) {
/*       */ 
/*       */         
/* 20066 */         setIsNoTake(true);
/* 20067 */         this.replacing = false;
/*       */       } 
/*       */     }
/* 20070 */     if (getTemplateId() == 1396)
/*       */     {
/*       */       
/* 20073 */       if (!aPlant && wasPlanted) {
/*       */ 
/*       */         
/* 20076 */         setTemperature((short)200);
/* 20077 */         updateIfGroundItem();
/*       */       }
/* 20079 */       else if (aPlant && !wasPlanted) {
/*       */ 
/*       */         
/* 20082 */         updateIfGroundItem();
/*       */       } 
/*       */     }
/* 20085 */     if (getTemplateId() == 1342)
/*       */     {
/*       */ 
/*       */       
/* 20089 */       if (!aPlant && wasPlanted) {
/*       */         
/* 20091 */         setIsNoMove(false);
/*       */       }
/* 20093 */       else if (aPlant && !wasPlanted) {
/*       */ 
/*       */         
/* 20096 */         setIsNoMove(true);
/*       */       } 
/*       */     }
/* 20099 */     if (getTemplateId() == 677)
/*       */     {
/*       */       
/* 20102 */       if (!aPlant && wasPlanted) {
/*       */         
/* 20104 */         Items.removeGmSign(this);
/* 20105 */         setIsNoTake(false);
/*       */       }
/* 20107 */       else if (aPlant && !wasPlanted) {
/*       */ 
/*       */         
/* 20110 */         Items.addGmSign(this);
/* 20111 */         setIsNoTake(true);
/*       */       } 
/*       */     }
/* 20114 */     if (getTemplateId() == 1309)
/*       */     {
/* 20116 */       if (!aPlant && wasPlanted) {
/*       */         
/* 20118 */         Items.removeWagonerContainer(this);
/* 20119 */         setData(-1L);
/*       */       }
/* 20121 */       else if (!aPlant || !wasPlanted) {
/*       */       
/*       */       } 
/*       */     }
/*       */ 
/*       */     
/* 20127 */     savePermissions();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsSealedByPlayer(boolean aSealed) {
/* 20138 */     this.permissions.setPermissionBit(Permissions.Allow.SEALED_BY_PLAYER.getBit(), aSealed);
/* 20139 */     this.isSealedOverride = false;
/* 20140 */     if (!aSealed) {
/*       */ 
/*       */       
/* 20143 */       for (Item item : getItemsAsArray())
/*       */       {
/* 20145 */         item.setLastMaintained(WurmCalendar.getCurrentTime());
/*       */       }
/* 20147 */       if (getTemplateId() == 1309)
/*       */       {
/* 20149 */         Delivery.freeContainer(getWurmId());
/*       */       }
/*       */     } 
/* 20152 */     updateName();
/* 20153 */     setIsNoPut(aSealed);
/*       */     
/* 20155 */     savePermissions();
/*       */     
/* 20157 */     Item topParent = getTopParentOrNull();
/* 20158 */     if (topParent != null && topParent.isHollow()) {
/*       */       
/* 20160 */       if (this.watchers == null) {
/*       */         return;
/*       */       }
/*       */       
/* 20164 */       long inventoryWindow = getTopParent();
/* 20165 */       if (topParent.isInventory()) {
/* 20166 */         inventoryWindow = -1L;
/*       */       }
/* 20168 */       for (Creature watcher : this.watchers) {
/*       */ 
/*       */         
/* 20171 */         watcher.getCommunicator().sendRemoveFromInventory(this, inventoryWindow);
/* 20172 */         watcher.getCommunicator().sendAddToInventory(this, inventoryWindow, -1L, -1);
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
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getObjectName() {
/* 20201 */     return getDescription().replace("\"", "'");
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
/* 20212 */     return getActualName().replace("\"", "'");
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setObjectName(String newName, Creature creature) {
/* 20223 */     return setDescription(newName);
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
/* 20234 */     return (playerId == this.lastOwner);
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
/* 20245 */     return isOwner(creature.getWurmId());
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
/* 20256 */     if (isOwnedByWagoner())
/* 20257 */       return false; 
/* 20258 */     if (isBed()) {
/*       */ 
/*       */       
/* 20261 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20262 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20263 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20264 */         return structure.isOwner(playerId); 
/* 20265 */       return false;
/*       */     } 
/* 20267 */     return isActualOwner(playerId);
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
/* 20278 */     if (getTemplateId() == 272)
/* 20279 */       return false; 
/* 20280 */     return (isOwner(creature) || creature.getPower() >= 2);
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
/* 20291 */     if (isBed() || isNoTrade() || getTemplateId() == 272)
/* 20292 */       return false; 
/* 20293 */     return (creature.getPower() > 1 || isOwner(creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setNewOwner(long playerId) {
/* 20304 */     if (ItemSettings.exists(getWurmId())) {
/*       */       
/* 20306 */       ItemSettings.remove(getWurmId());
/* 20307 */       PermissionsHistories.addHistoryEntry(getWurmId(), System.currentTimeMillis(), -10L, "Auto", "Cleared Permissions");
/*       */     } 
/*       */     
/* 20310 */     setLastOwnerId(playerId);
/* 20311 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getOwnerName() {
/* 20322 */     return PlayerInfoFactory.getPlayerName(this.lastOwner);
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
/* 20333 */     if (isOwnedByWagoner())
/* 20334 */       return "WAGONER OWNS THIS"; 
/* 20335 */     if (isBed()) {
/*       */ 
/*       */       
/* 20338 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20339 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/*       */       
/* 20341 */       if (structure == null || !structure.isTypeHouse())
/* 20342 */         return "BED NEEDS TO BE INSIDE A BUILDING TO WORK"; 
/* 20343 */       if (!structure.isFinished())
/* 20344 */         return "BED NEEDS TO BE IN FINISHED BUILDING TO WORK"; 
/* 20345 */       return "";
/*       */     } 
/* 20347 */     if (getTemplateId() == 1271)
/*       */     {
/*       */       
/* 20350 */       return "";
/*       */     }
/* 20352 */     if (getTemplateId() == 272) {
/*       */       
/* 20354 */       if (this.wasBrandedTo != -10L)
/* 20355 */         return "VIEW ONLY"; 
/* 20356 */       return "NEEDS TO HAVE BEEN BRANDED TO SEE PERMISSIONS";
/*       */     } 
/* 20358 */     if (getLockId() == -10L) {
/* 20359 */       return "NEEDS TO HAVE A LOCK FOR PERMISSIONS TO WORK";
/*       */     }
/*       */     
/* 20362 */     if (!isLocked())
/* 20363 */       return "NEEDS TO BE LOCKED OTHERWISE EVERYONE CAN USE THIS"; 
/* 20364 */     return "";
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
/* 20375 */     return ItemSettings.getPermissionsPlayerList(getWurmId());
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
/*       */   public boolean isManaged() {
/* 20387 */     return this.permissions.hasPermission(Permissions.Allow.SETTLEMENT_MAY_MANAGE.getBit());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isManageEnabled(Player player) {
/* 20398 */     return false;
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
/*       */   public String mayManageText(Player aPlayer) {
/* 20421 */     return "";
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
/* 20432 */     return "";
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
/* 20443 */     return "";
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
/* 20454 */     return "";
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
/* 20465 */     return "";
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
/* 20476 */     return "";
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
/* 20487 */     if (isOwnedByWagoner())
/* 20488 */       return ""; 
/* 20489 */     if (isBed()) {
/*       */ 
/*       */ 
/*       */       
/* 20493 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20494 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/*       */       
/* 20496 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20497 */         return structure.getSettlementName(); 
/* 20498 */       return "";
/*       */     } 
/* 20500 */     if (getTemplateId() == 272) {
/*       */       
/* 20502 */       if (this.wasBrandedTo != -10L) {
/*       */         try
/*       */         {
/*       */           
/* 20506 */           Village lbVillage = Villages.getVillage((int)this.wasBrandedTo);
/* 20507 */           return "Citizens of \"" + lbVillage.getName() + "\"";
/*       */         }
/* 20509 */         catch (NoSuchVillageException e)
/*       */         {
/*       */           
/* 20512 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*       */     else {
/*       */       
/* 20519 */       Village loVillage = Villages.getVillageForCreature(this.lastOwner);
/* 20520 */       if (loVillage != null)
/*       */       {
/* 20522 */         return "Citizens of \"" + loVillage.getName() + "\"";
/*       */       }
/*       */     } 
/* 20525 */     return "";
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
/* 20536 */     if (isOwnedByWagoner())
/* 20537 */       return ""; 
/* 20538 */     if (isBed()) {
/*       */ 
/*       */ 
/*       */       
/* 20542 */       VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 20543 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/*       */       
/* 20545 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20546 */         return structure.getAllianceName(); 
/* 20547 */       return "";
/*       */     } 
/* 20549 */     if (getTemplateId() == 272) {
/*       */       
/* 20551 */       if (this.wasBrandedTo != -10L) {
/*       */         try
/*       */         {
/*       */           
/* 20555 */           Village lbVillage = Villages.getVillage((int)this.wasBrandedTo);
/* 20556 */           if (lbVillage != null && lbVillage.getAllianceNumber() > 0)
/*       */           {
/* 20558 */             return "Alliance of \"" + lbVillage.getAllianceName() + "\"";
/*       */           }
/*       */         }
/* 20561 */         catch (NoSuchVillageException e)
/*       */         {
/*       */           
/* 20564 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */         }
/*       */       
/*       */       }
/*       */     }
/*       */     else {
/*       */       
/* 20571 */       Village loVillage = Villages.getVillageForCreature(this.lastOwner);
/* 20572 */       if (loVillage != null && loVillage.getAllianceNumber() > 0)
/*       */       {
/* 20574 */         return "Alliance of \"" + loVillage.getAllianceName() + "\"";
/*       */       }
/*       */     } 
/* 20577 */     return "";
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
/* 20588 */     String toReturn = "";
/* 20589 */     byte kingdom = getKingdom();
/*       */     
/* 20591 */     if (isVehicle() && kingdom != 0)
/* 20592 */       toReturn = "Kingdom of \"" + Kingdoms.getNameFor(kingdom) + "\""; 
/* 20593 */     return toReturn;
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
/* 20604 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getRolePermissionName() {
/* 20615 */     return "";
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
/* 20626 */     if (isOwnedByWagoner())
/* 20627 */       return false; 
/* 20628 */     if (isBed()) {
/*       */ 
/*       */ 
/*       */       
/* 20632 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20633 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/*       */       
/* 20635 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20636 */         return structure.isCitizen(creature); 
/* 20637 */       return false;
/*       */     } 
/* 20639 */     if (getTemplateId() == 272) {
/*       */       
/* 20641 */       if (this.wasBrandedTo != -10L) {
/*       */         try
/*       */         {
/*       */           
/* 20645 */           Village lbVillage = Villages.getVillage((int)this.wasBrandedTo);
/* 20646 */           if (lbVillage != null)
/*       */           {
/* 20648 */             return lbVillage.isCitizen(creature);
/*       */           }
/*       */         }
/* 20651 */         catch (NoSuchVillageException e)
/*       */         {
/*       */           
/* 20654 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */         }
/*       */       
/*       */       }
/*       */     } else {
/*       */       
/* 20660 */       Village ownerVillage = Villages.getVillageForCreature(getLastOwnerId());
/* 20661 */       if (ownerVillage != null)
/* 20662 */         return ownerVillage.isCitizen(creature); 
/*       */     } 
/* 20664 */     return false;
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
/* 20675 */     if (isOwnedByWagoner())
/* 20676 */       return false; 
/* 20677 */     if (isBed()) {
/*       */ 
/*       */ 
/*       */       
/* 20681 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20682 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20683 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20684 */         return structure.isAllied(creature); 
/* 20685 */       return false;
/*       */     } 
/* 20687 */     if (getTemplateId() == 272) {
/*       */       
/* 20689 */       if (this.wasBrandedTo != -10L) {
/*       */         try
/*       */         {
/*       */           
/* 20693 */           Village lbVillage = Villages.getVillage((int)this.wasBrandedTo);
/* 20694 */           if (lbVillage != null)
/*       */           {
/* 20696 */             return lbVillage.isAlly(creature);
/*       */           }
/*       */         }
/* 20699 */         catch (NoSuchVillageException e)
/*       */         {
/*       */           
/* 20702 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*       */         }
/*       */       
/*       */       }
/*       */     } else {
/*       */       
/* 20708 */       Village ownerVillage = Villages.getVillageForCreature(getLastOwnerId());
/* 20709 */       if (ownerVillage != null)
/* 20710 */         return ownerVillage.isAlly(creature); 
/*       */     } 
/* 20712 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isSameKingdom(Creature creature) {
/* 20723 */     if (isOwnedByWagoner())
/* 20724 */       return false; 
/* 20725 */     if (isBed()) {
/*       */ 
/*       */ 
/*       */       
/* 20729 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20730 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20731 */       if (structure != null && structure.isTypeHouse() && structure.isFinished())
/* 20732 */         return structure.isSameKingdom(creature); 
/* 20733 */       return false;
/*       */     } 
/*       */     
/* 20736 */     if (isVehicle() && getKingdom() != 0)
/* 20737 */       return (getKingdom() == creature.getKingdomId()); 
/* 20738 */     return (Players.getInstance().getKingdomForPlayer(getLastOwnerId()) == creature.getKingdomId());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public void addGuest(long guestId, int settings) {
/* 20749 */     ItemSettings.addPlayer(this.id, guestId, settings);
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
/* 20760 */     ItemSettings.removePlayer(this.id, guestId);
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
/*       */   public void addDefaultCitizenPermissions() {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isGuest(Creature creature) {
/* 20782 */     return isGuest(creature.getWurmId());
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
/* 20794 */     return ItemSettings.isGuest(this, playerId);
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
/*       */   public void save() throws IOException {}
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getMaxAllowed() {
/* 20816 */     return ItemSettings.getMaxAllowed();
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
/* 20828 */     if (isOwnedByWagoner())
/* 20829 */       return false; 
/* 20830 */     return (isLockable() || isBed() || getTemplateId() == 1271 || (
/* 20831 */       getTemplateId() == 272 && this.wasBrandedTo != -10L));
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
/* 20843 */     if (isOwnedByWagoner())
/* 20844 */       return false; 
/* 20845 */     if (isBed()) {
/*       */ 
/*       */       
/* 20848 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20849 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20850 */       return (structure != null && structure.isTypeHouse() && structure.isFinished() && mayManage(creature));
/*       */     } 
/* 20852 */     if (getTemplateId() == 1271)
/* 20853 */       return mayManage(creature); 
/* 20854 */     if (getTemplateId() == 272)
/* 20855 */       return (this.wasBrandedTo != -10L && creature.getPower() > 1); 
/* 20856 */     return (isLocked() && mayManage(creature));
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
/* 20867 */     if (isOwnedByWagoner())
/* 20868 */       return false; 
/* 20869 */     if (ItemSettings.isExcluded(this, creature)) {
/* 20870 */       return false;
/*       */     }
/* 20872 */     return ItemSettings.canManage(this, creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayManage(Creature creature) {
/* 20882 */     if (isOwnedByWagoner()) {
/* 20883 */       return false;
/*       */     }
/* 20885 */     if (isBed()) {
/*       */       
/* 20887 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20888 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20889 */       return (structure != null && structure.isTypeHouse() && structure.isFinished() && structure
/* 20890 */         .mayManage(creature));
/*       */     } 
/* 20892 */     if (creature.getPower() > 1)
/* 20893 */       return true; 
/* 20894 */     return canManage(creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean maySeeHistory(Creature creature) {
/* 20904 */     if (isOwnedByWagoner())
/* 20905 */       return false; 
/* 20906 */     if (creature.getPower() > 1)
/* 20907 */       return true; 
/* 20908 */     return isOwner(creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayCommand(Creature creature) {
/* 20918 */     if (isOwnedByWagoner())
/* 20919 */       return false; 
/* 20920 */     if (ItemSettings.isExcluded(this, creature))
/* 20921 */       return false; 
/* 20922 */     return (canHavePermissions() && ItemSettings.mayCommand(this, creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayPassenger(Creature creature) {
/* 20932 */     if (isOwnedByWagoner())
/* 20933 */       return false; 
/* 20934 */     if (ItemSettings.isExcluded(this, creature))
/* 20935 */       return false; 
/* 20936 */     if (isChair() && getData() == creature.getWurmId())
/* 20937 */       return true; 
/* 20938 */     return (canHavePermissions() && ItemSettings.mayPassenger(this, creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayAccessHold(Creature creature) {
/* 20948 */     if (isOwnedByWagoner())
/* 20949 */       return false; 
/* 20950 */     if (ItemSettings.isExcluded(this, creature))
/* 20951 */       return false; 
/* 20952 */     if (!canHavePermissions())
/* 20953 */       return true; 
/* 20954 */     return ItemSettings.mayAccessHold(this, creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayUseBed(Creature creature) {
/* 20964 */     if (isOwnedByWagoner()) {
/* 20965 */       return false;
/*       */     }
/* 20967 */     if (!ItemSettings.exists(getWurmId())) {
/*       */       
/* 20969 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20970 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20971 */       return (structure != null && structure.isTypeHouse());
/*       */     } 
/* 20973 */     if (ItemSettings.isExcluded(this, creature))
/* 20974 */       return false; 
/* 20975 */     return ItemSettings.mayUseBed(this, creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayFreeSleep(Creature creature) {
/* 20985 */     if (isOwnedByWagoner()) {
/* 20986 */       return false;
/*       */     }
/* 20988 */     if (!ItemSettings.exists(getWurmId())) {
/*       */       
/* 20990 */       VolaTile vt = Zones.getTileOrNull(getTilePos(), isOnSurface());
/* 20991 */       Structure structure = (vt != null) ? vt.getStructure() : null;
/* 20992 */       return (structure != null && structure.isTypeHouse() && structure
/* 20993 */         .mayPass(creature));
/*       */     } 
/* 20995 */     if (ItemSettings.isExcluded(this, creature))
/* 20996 */       return false; 
/* 20997 */     return ItemSettings.mayFreeSleep(this, creature);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayDrag(Creature creature) {
/* 21007 */     if (isOwnedByWagoner())
/* 21008 */       return false; 
/* 21009 */     if (creature.isOnPvPServer() && !isMooredBoat() && !isLocked())
/* 21010 */       return true; 
/* 21011 */     if (ItemSettings.isExcluded(this, creature))
/* 21012 */       return false; 
/* 21013 */     return (canHavePermissions() && ItemSettings.mayDrag(this, creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayPostNotices(Creature creature) {
/* 21023 */     if (ItemSettings.isExcluded(this, creature))
/* 21024 */       return false; 
/* 21025 */     return (canHavePermissions() && ItemSettings.mayPostNotices(this, creature));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public final boolean mayAddPMs(Creature creature) {
/* 21035 */     if (ItemSettings.isExcluded(this, creature))
/* 21036 */       return false; 
/* 21037 */     return (canHavePermissions() && ItemSettings.mayAddPMs(this, creature));
/*       */   }
/*       */ 
/*       */   
/*       */   public long getWhenRented() {
/* 21042 */     return this.whenRented;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setWhenRented(long when) {
/* 21047 */     this.whenRented = when;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private static void logInfo(String msg) {
/* 21055 */     logger.info(msg);
/*       */   }
/*       */   
/*       */   private static void logInfo(String msg, Throwable thrown) {
/* 21059 */     logger.log(Level.INFO, msg, thrown);
/*       */   }
/*       */ 
/*       */   
/*       */   private static void logWarn(String msg) {
/* 21064 */     logger.warning(msg);
/*       */   }
/*       */   
/*       */   private static void logWarn(String msg, Throwable thrown) {
/* 21068 */     logger.log(Level.WARNING, msg, thrown);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isPotable() {
/* 21076 */     return this.template.isPotable();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean usesFoodState() {
/* 21085 */     return this.template.usesFoodState();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isRecipeItem() {
/* 21094 */     return this.template.isRecipeItem();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isAlcohol() {
/* 21103 */     return this.template.isAlcohol();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canBeDistilled() {
/* 21112 */     return this.template.canBeDistilled();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean canBeFermented() {
/* 21121 */     return this.template.canBeFermented();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isCrushable() {
/* 21130 */     return this.template.isCrushable();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isSurfaceOnly() {
/* 21135 */     return this.template.isSurfaceOnly();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean hasSeeds() {
/* 21144 */     return this.template.hasSeeds();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getAlcoholStrength() {
/* 21153 */     return this.template.getAlcoholStrength();
/*       */   }
/*       */ 
/*       */   
/*       */   public void closeAll() {
/* 21158 */     if (isHollow())
/*       */     {
/* 21160 */       if (this.watchers != null) {
/*       */         
/* 21162 */         Creature[] watcherArray = this.watchers.<Creature>toArray(new Creature[this.watchers.size()]);
/* 21163 */         for (Creature watcher : watcherArray)
/*       */         {
/* 21165 */           close(watcher);
/*       */         }
/*       */       } 
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public void close(Creature performer) {
/* 21173 */     if (isHollow())
/*       */     {
/* 21175 */       if (performer.getCommunicator().sendCloseInventoryWindow(getWurmId()))
/*       */       {
/* 21177 */         if (getParentId() == -10L) {
/* 21178 */           removeWatcher(performer, true);
/*       */         } else {
/*       */           
/* 21181 */           boolean found = false;
/*       */           
/*       */           try {
/* 21184 */             Creature[] crets = getParent().getWatchers();
/* 21185 */             for (int x = 0; x < crets.length; x++) {
/*       */               
/* 21187 */               if (crets[x].getWurmId() == performer.getWurmId()) {
/*       */                 
/* 21189 */                 found = true;
/*       */                 
/*       */                 break;
/*       */               } 
/*       */             } 
/* 21194 */           } catch (NoSuchItemException noSuchItemException) {
/*       */ 
/*       */           
/* 21197 */           } catch (NoSuchCreatureException noSuchCreatureException) {}
/*       */ 
/*       */           
/* 21200 */           if (!found) {
/* 21201 */             removeWatcher(performer, true);
/*       */           }
/*       */         } 
/*       */       }
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean hasQueen() {
/* 21209 */     return (getAuxData() > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean hasTwoQueens() {
/* 21214 */     return (getAuxData() > 1);
/*       */   }
/*       */ 
/*       */   
/*       */   public void addQueen() {
/* 21219 */     int queens = getAuxData();
/* 21220 */     if (queens > 1)
/*       */       return; 
/* 21222 */     setAuxData((byte)(queens + 1));
/*       */     
/* 21224 */     updateHiveModel();
/* 21225 */     if (queens == 0)
/*       */     {
/* 21227 */       Zones.addHive(this, false);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean removeQueen() {
/* 21233 */     int queens = getAuxData();
/* 21234 */     if (queens == 0)
/* 21235 */       return false; 
/* 21236 */     setAuxData((byte)(queens - 1));
/*       */     
/* 21238 */     updateHiveModel();
/* 21239 */     if (queens == 1)
/*       */     {
/* 21241 */       Zones.removeHive(this, false);
/*       */     }
/* 21243 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   void updateHiveModel() {
/* 21248 */     if (getTemplateId() == 1239 || hasTwoQueens()) {
/* 21249 */       updateName();
/*       */     } else {
/*       */ 
/*       */       
/*       */       try {
/*       */         
/* 21255 */         Zone z = Zones.getZone(this.zoneId);
/* 21256 */         z.removeItem(this);
/* 21257 */         z.addItem(this);
/*       */       }
/* 21259 */       catch (NoSuchZoneException nsz) {
/*       */         
/* 21261 */         logWarn("Zone at " + ((int)getPosX() >> 2) + "," + ((int)getPosY() >> 2) + ",surf=" + 
/* 21262 */             isOnSurface() + " no such zone. Item: " + this);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isPStateNone() {
/* 21272 */     if (usesFoodState())
/* 21273 */       return (getLeftAuxData() == 0); 
/* 21274 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setRaw() {
/* 21279 */     if (usesFoodState()) {
/* 21280 */       setRightAuxData(0);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isRaw() {
/* 21285 */     if (usesFoodState())
/* 21286 */       return (getRightAuxData() == 0); 
/* 21287 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsFried() {
/* 21292 */     if (usesFoodState()) {
/* 21293 */       setRightAuxData(1);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isFried() {
/* 21298 */     if (usesFoodState())
/* 21299 */       return (getRightAuxData() == 1); 
/* 21300 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsGrilled() {
/* 21305 */     if (usesFoodState()) {
/* 21306 */       setRightAuxData(2);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isGrilled() {
/* 21311 */     if (usesFoodState())
/* 21312 */       return (getRightAuxData() == 2); 
/* 21313 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsBoiled() {
/* 21318 */     if (usesFoodState()) {
/* 21319 */       setRightAuxData(3);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isBoiled() {
/* 21324 */     if (usesFoodState())
/* 21325 */       return (getRightAuxData() == 3); 
/* 21326 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsRoasted() {
/* 21331 */     if (usesFoodState()) {
/* 21332 */       setRightAuxData(4);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isRoasted() {
/* 21337 */     if (usesFoodState())
/* 21338 */       return (getRightAuxData() == 4); 
/* 21339 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsSteamed() {
/* 21344 */     if (usesFoodState()) {
/* 21345 */       setRightAuxData(5);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isSteamed() {
/* 21350 */     if (usesFoodState())
/* 21351 */       return (getRightAuxData() == 5); 
/* 21352 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsBaked() {
/* 21357 */     if (usesFoodState()) {
/* 21358 */       setRightAuxData(6);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isBaked() {
/* 21363 */     if (usesFoodState())
/* 21364 */       return (getRightAuxData() == 6); 
/* 21365 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsCooked() {
/* 21370 */     if (usesFoodState()) {
/* 21371 */       setRightAuxData(7);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isCooked() {
/* 21376 */     if (usesFoodState())
/* 21377 */       return (getRightAuxData() == 7); 
/* 21378 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsCandied() {
/* 21383 */     if (usesFoodState()) {
/* 21384 */       setRightAuxData(8);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isCandied() {
/* 21389 */     if (usesFoodState())
/* 21390 */       return (getRightAuxData() == 8); 
/* 21391 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsChocolateCoated() {
/* 21396 */     if (usesFoodState())
/* 21397 */       setRightAuxData(9); 
/*       */   }
/*       */   
/*       */   public boolean isChocolateCoated() {
/* 21401 */     if (usesFoodState())
/* 21402 */       return (getRightAuxData() == 9); 
/* 21403 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean isChoppedBitSet() {
/* 21408 */     return ((getAuxData() & 0x10) != 0);
/*       */   }
/*       */ 
/*       */   
/*       */   private void setIsChoppedBit(boolean setBit) {
/* 21413 */     setAuxData((byte)((getAuxData() & 0xFFFFFFEF) + (setBit ? 16 : 0)));
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean isMashedBitSet() {
/* 21418 */     return ((getAuxData() & 0x20) != 0);
/*       */   }
/*       */ 
/*       */   
/*       */   private void setIsMashedBit(boolean setBit) {
/* 21423 */     setAuxData((byte)((getAuxData() & 0xFFFFFFDF) + (setBit ? 32 : 0)));
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean isWrappedBitSet() {
/* 21428 */     return ((getAuxData() & 0x40) != 0);
/*       */   }
/*       */ 
/*       */   
/*       */   private void setIsWrappedBit(boolean setBit) {
/* 21433 */     setAuxData((byte)((getAuxData() & 0xFFFFFFBF) + (setBit ? 64 : 0)));
/*       */   }
/*       */ 
/*       */   
/*       */   private boolean isFreshBitSet() {
/* 21438 */     return ((getAuxData() & Byte.MIN_VALUE) != 0);
/*       */   }
/*       */ 
/*       */   
/*       */   private void setIsFreshBit(boolean setBit) {
/* 21443 */     setAuxData((byte)((getAuxData() & Byte.MAX_VALUE) + (setBit ? -128 : 0)));
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsChopped(boolean isChopped) {
/* 21449 */     if (usesFoodState()) {
/* 21450 */       setIsChoppedBit(isChopped);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isChopped() {
/* 21456 */     if (usesFoodState())
/* 21457 */       return isChoppedBitSet(); 
/* 21458 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsDiced(boolean isDiced) {
/* 21464 */     if (usesFoodState()) {
/* 21465 */       setIsChoppedBit(isDiced);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isDiced() {
/* 21471 */     if (usesFoodState())
/* 21472 */       return isChoppedBitSet(); 
/* 21473 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsGround(boolean isGround) {
/* 21479 */     if (usesFoodState()) {
/* 21480 */       setIsChoppedBit(isGround);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isGround() {
/* 21486 */     if (usesFoodState())
/* 21487 */       return isChoppedBitSet(); 
/* 21488 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsUnfermented(boolean isUnfermented) {
/* 21494 */     if (usesFoodState()) {
/* 21495 */       setIsChoppedBit(isUnfermented);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isUnfermented() {
/* 21501 */     if (usesFoodState())
/* 21502 */       return isChoppedBitSet(); 
/* 21503 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsZombiefied(boolean isZombiefied) {
/* 21509 */     if (usesFoodState()) {
/* 21510 */       setIsChoppedBit(isZombiefied);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isZombiefied() {
/* 21516 */     if (usesFoodState())
/* 21517 */       return isChoppedBitSet(); 
/* 21518 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsWhipped(boolean isWhipped) {
/* 21523 */     if (usesFoodState() && getTemplateId() == 1249) {
/* 21524 */       setIsChoppedBit(isWhipped);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isWhipped() {
/* 21530 */     if (usesFoodState() && getTemplateId() == 1249)
/* 21531 */       return isChoppedBitSet(); 
/* 21532 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsMashed(boolean isMashed) {
/* 21538 */     if (usesFoodState() && isVegetable()) {
/* 21539 */       setIsMashedBit(isMashed);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isMashed() {
/* 21545 */     if (usesFoodState() && isVegetable())
/* 21546 */       return isMashedBitSet(); 
/* 21547 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsMinced(boolean isMinced) {
/* 21553 */     if (usesFoodState() && isMeat()) {
/* 21554 */       setIsMashedBit(isMinced);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isMinced() {
/* 21560 */     if (usesFoodState() && isMeat())
/* 21561 */       return isMashedBitSet(); 
/* 21562 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsFermenting(boolean isFermenting) {
/* 21567 */     if (usesFoodState() && canBeFermented()) {
/* 21568 */       setIsMashedBit(isFermenting());
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isFermenting() {
/* 21573 */     if (usesFoodState() && canBeFermented())
/* 21574 */       return isMashedBitSet(); 
/* 21575 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsUnderWeight(boolean isUnderWeight) {
/* 21580 */     if (usesFoodState() && isFish()) {
/* 21581 */       setIsMashedBit(isUnderWeight);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isUnderWeight() {
/* 21586 */     if (usesFoodState() && isFish())
/* 21587 */       return isMashedBitSet(); 
/* 21588 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsClotted(boolean isClotted) {
/* 21593 */     if (usesFoodState() && getTemplateId() == 1249) {
/* 21594 */       setIsMashedBit(isClotted);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isClotted() {
/* 21599 */     if (usesFoodState() && getTemplateId() == 1249)
/* 21600 */       return isMashedBitSet(); 
/* 21601 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsWrapped(boolean isWrapped) {
/* 21607 */     if (usesFoodState()) {
/* 21608 */       setIsWrappedBit(isWrapped);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isWrapped() {
/* 21614 */     if (usesFoodState())
/* 21615 */       return isWrappedBitSet(); 
/* 21616 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsUndistilled(boolean isUndistilled) {
/* 21621 */     if (usesFoodState()) {
/* 21622 */       setIsWrappedBit(isUndistilled);
/*       */     }
/*       */   }
/*       */   
/*       */   public boolean isUndistilled() {
/* 21627 */     if (usesFoodState())
/* 21628 */       return isWrappedBitSet(); 
/* 21629 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsFresh(boolean isFresh) {
/* 21635 */     if (isHerb() || isSpice()) {
/* 21636 */       setIsFreshBit(isFresh);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFresh() {
/* 21642 */     if (isHerb() || isSpice())
/* 21643 */       return isFreshBitSet(); 
/* 21644 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsSalted(boolean isSalted) {
/* 21650 */     if (usesFoodState() && (isDish() || isLiquid())) {
/*       */       
/* 21652 */       setIsFreshBit(isSalted);
/* 21653 */       updateName();
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isSalted() {
/* 21660 */     if (usesFoodState() && (isDish() || isLiquid()))
/* 21661 */       return isFreshBitSet(); 
/* 21662 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public void setIsLive(boolean isLive) {
/* 21668 */     if (isFish()) {
/* 21669 */       setIsFreshBit(isLive);
/*       */     }
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isLive() {
/* 21675 */     if (isFish())
/* 21676 */       return isFreshBitSet(); 
/* 21677 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isCorrectFoodState(byte cookState, byte physicalState) {
/* 21688 */     if (cookState != -1)
/*       */     {
/* 21690 */       if ((cookState & 0xF) == 7) {
/*       */ 
/*       */         
/* 21693 */         if (getRightAuxData() == 0) {
/* 21694 */           return false;
/*       */         
/*       */         }
/*       */       
/*       */       }
/* 21699 */       else if (getRightAuxData() != (cookState & 0xF)) {
/* 21700 */         return false;
/*       */       } 
/*       */     }
/*       */     
/* 21704 */     if (physicalState != -1) {
/*       */       
/* 21706 */       if ((physicalState & Byte.MAX_VALUE) == 0)
/*       */       {
/*       */         
/* 21709 */         return ((getLeftAuxData() & 0x7) == (physicalState >>> 4 & 0x7));
/*       */       }
/*       */       
/* 21712 */       return (getLeftAuxData() == (physicalState >>> 4 & 0xF));
/*       */     } 
/* 21714 */     return true;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getFoodStages() {
/* 21723 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21724 */     if (imd != null) {
/* 21725 */       return imd.getStages();
/*       */     }
/* 21727 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public byte getFoodIngredients() {
/* 21736 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21737 */     if (imd != null) {
/* 21738 */       return imd.getIngredients();
/*       */     }
/* 21740 */     return 0;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getFoodComplexity() {
/* 21750 */     float stageDif = getFoodStages() / 10.0F;
/* 21751 */     float ingredientDif = getFoodIngredients() / 30.0F;
/* 21752 */     float totalDif = stageDif * ingredientDif * 2.0F;
/* 21753 */     return totalDif;
/*       */   }
/*       */ 
/*       */   
/*       */   float calcFoodPercentage() {
/* 21758 */     float rarityMod = 1.0F + (getRarity() * getRarity()) * 0.1F;
/* 21759 */     float percentage = getCurrentQualityLevel() / 100.0F * rarityMod;
/* 21760 */     return percentage;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getCaloriesByWeight() {
/* 21769 */     return getCaloriesByWeight(getWeightGrams());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getCaloriesByWeight(int weight) {
/* 21779 */     return (getCalories() * weight) / 1000.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public short getCalories() {
/* 21789 */     if (getTemplateId() == 488 && getRealTemplateId() == 488)
/* 21790 */       return 0; 
/* 21791 */     float percentage = calcFoodPercentage();
/* 21792 */     if (!this.template.calcNutritionValues())
/* 21793 */       return (short)(int)(this.template.getCalories() * percentage); 
/* 21794 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21795 */     if (imd != null) {
/* 21796 */       return (short)(int)(imd.getCalories() * percentage);
/*       */     }
/* 21798 */     return (short)(int)(this.template.getCalories() * percentage);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getCarbsByWeight() {
/* 21807 */     return getCarbsByWeight(getWeightGrams());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getCarbsByWeight(int weight) {
/* 21817 */     return (getCarbs() * weight) / 1000.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public short getCarbs() {
/* 21827 */     if (getTemplateId() == 488 && getRealTemplateId() == 488)
/* 21828 */       return 0; 
/* 21829 */     float percentage = calcFoodPercentage();
/* 21830 */     if (!this.template.calcNutritionValues())
/* 21831 */       return (short)(int)(this.template.getCarbs() * percentage); 
/* 21832 */     if (!this.template.calcNutritionValues())
/* 21833 */       return this.template.getCarbs(); 
/* 21834 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21835 */     if (imd != null) {
/* 21836 */       return (short)(int)(imd.getCarbs() * percentage);
/*       */     }
/* 21838 */     return (short)(int)(this.template.getCarbs() * percentage);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getFatsByWeight() {
/* 21847 */     return getFatsByWeight(getWeightGrams());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getFatsByWeight(int weight) {
/* 21857 */     return (getFats() * weight) / 1000.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public short getFats() {
/* 21867 */     if (getTemplateId() == 488 && getRealTemplateId() == 488)
/* 21868 */       return 0; 
/* 21869 */     float percentage = calcFoodPercentage();
/* 21870 */     if (!this.template.calcNutritionValues())
/* 21871 */       return (short)(int)(this.template.getFats() * percentage); 
/* 21872 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21873 */     if (imd != null) {
/* 21874 */       return (short)(int)(imd.getFats() * percentage);
/*       */     }
/* 21876 */     return (short)(int)(this.template.getFats() * percentage);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getProteinsByWeight() {
/* 21885 */     return getProteinsByWeight(getWeightGrams());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getProteinsByWeight(int weight) {
/* 21895 */     return (getProteins() * weight) / 1000.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public short getProteins() {
/* 21905 */     if (getTemplateId() == 488 && getRealTemplateId() == 488) {
/* 21906 */       return 0;
/*       */     }
/*       */ 
/*       */     
/* 21910 */     float percentage = calcFoodPercentage();
/* 21911 */     if (!this.template.calcNutritionValues())
/* 21912 */       return (short)(int)(this.template.getProteins() * percentage); 
/* 21913 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21914 */     if (imd != null) {
/* 21915 */       return (short)(int)(imd.getProteins() * percentage);
/*       */     }
/* 21917 */     return (short)(int)(this.template.getProteins() * percentage);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public int getBonus() {
/* 21926 */     if (getTemplateId() == 488 && getRealTemplateId() == 488)
/* 21927 */       return -1; 
/* 21928 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21929 */     if (imd != null) {
/* 21930 */       return imd.getBonus() & 0xFF;
/*       */     }
/* 21932 */     return -1;
/*       */   }
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Recipe getRecipe() {
/* 21938 */     if (getTemplateId() == 488 && getRealTemplateId() == 488)
/* 21939 */       return null; 
/* 21940 */     ItemMealData imd = ItemMealData.getItemMealData(getWurmId());
/* 21941 */     if (imd != null && imd.getRecipeId() > -1)
/*       */     {
/* 21943 */       return Recipes.getRecipeById(imd.getRecipeId());
/*       */     }
/*       */     
/* 21946 */     return null;
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
/*       */   public void calculateAndSaveNutrition(@Nullable Item source, Item target, Recipe recipe) {
/* 21959 */     byte stages = 1;
/* 21960 */     byte ingredients = 0;
/* 21961 */     if (source != null && !source.isCookingTool()) {
/*       */       
/* 21963 */       stages = (byte)(stages + source.getFoodStages());
/* 21964 */       ingredients = (byte)(ingredients + source.getFoodIngredients() + 1);
/*       */     } 
/* 21966 */     if (target.isFoodMaker()) {
/*       */ 
/*       */       
/* 21969 */       Map<Integer, Item> items = new ConcurrentHashMap<>();
/* 21970 */       for (Item item : target.getItemsAsArray())
/*       */       {
/* 21972 */         items.put(Integer.valueOf(item.getTemplateId()), item);
/*       */       }
/*       */       
/* 21975 */       for (Item item : items.values())
/*       */       {
/* 21977 */         stages = (byte)(stages + item.getFoodStages());
/* 21978 */         ingredients = (byte)(ingredients + item.getFoodIngredients() + 1);
/*       */       }
/*       */     
/*       */     } else {
/*       */       
/* 21983 */       stages = (byte)(stages + target.getFoodStages());
/* 21984 */       ingredients = (byte)(ingredients + target.getFoodIngredients() + 1);
/*       */     } 
/*       */     
/* 21987 */     short calories = this.template.getCalories();
/* 21988 */     short carbs = this.template.getCarbs();
/* 21989 */     short fats = this.template.getFats();
/* 21990 */     short proteins = this.template.getProteins();
/*       */     
/* 21992 */     if (this.template.calcNutritionValues()) {
/*       */ 
/*       */ 
/*       */       
/* 21996 */       float caloriesTotal = 0.0F;
/* 21997 */       float carbsTotal = 0.0F;
/* 21998 */       float fatsTotal = 0.0F;
/* 21999 */       float proteinsTotal = 0.0F;
/* 22000 */       int weight = 0;
/*       */       
/* 22002 */       if (source != null && !source.isCookingTool()) {
/*       */         
/* 22004 */         Ingredient ingredient = recipe.getActiveItem();
/*       */         
/* 22006 */         int iweight = (ingredient == null) ? source.getWeightGrams() : (int)(source.getWeightGrams() * (100.0F - ingredient.getLoss()) / 100.0F);
/* 22007 */         caloriesTotal += source.getCaloriesByWeight(iweight);
/* 22008 */         carbsTotal += source.getCarbsByWeight(iweight);
/* 22009 */         fatsTotal += source.getFatsByWeight(iweight);
/* 22010 */         proteinsTotal += source.getProteinsByWeight(iweight);
/* 22011 */         weight += iweight;
/*       */       } 
/* 22013 */       if (target.isFoodMaker()) {
/*       */ 
/*       */         
/* 22016 */         for (Item item : target.getItemsAsArray())
/*       */         {
/* 22018 */           Ingredient ingredient = recipe.findMatchingIngredient(item);
/*       */           
/* 22020 */           int iweight = (ingredient == null) ? item.getWeightGrams() : (int)(item.getWeightGrams() * (100.0F - ingredient.getLoss()) / 100.0F);
/* 22021 */           caloriesTotal += item.getCaloriesByWeight(iweight);
/* 22022 */           carbsTotal += item.getCarbsByWeight(iweight);
/* 22023 */           fatsTotal += item.getFatsByWeight(iweight);
/* 22024 */           proteinsTotal += item.getProteinsByWeight(iweight);
/* 22025 */           weight += iweight;
/*       */         }
/*       */       
/*       */       }
/*       */       else {
/*       */         
/* 22031 */         Ingredient ingredient = recipe.getTargetItem();
/*       */         
/* 22033 */         int iweight = (ingredient == null) ? target.getWeightGrams() : (int)(target.getWeightGrams() * (100.0F - ingredient.getLoss()) / 100.0F);
/* 22034 */         caloriesTotal += target.getCaloriesByWeight(iweight);
/* 22035 */         carbsTotal += target.getCarbsByWeight(iweight);
/* 22036 */         fatsTotal += target.getFatsByWeight(iweight);
/* 22037 */         proteinsTotal += target.getProteinsByWeight(iweight);
/* 22038 */         weight += iweight;
/*       */       } 
/*       */ 
/*       */       
/* 22042 */       float rarityMod = 1.0F + (getRarity() * getRarity()) * 0.1F;
/* 22043 */       calories = (short)(int)(caloriesTotal * 1000.0F / weight * rarityMod);
/* 22044 */       carbs = (short)(int)(carbsTotal * 1000.0F / weight * rarityMod);
/* 22045 */       fats = (short)(int)(fatsTotal * 1000.0F / weight * rarityMod);
/* 22046 */       proteins = (short)(int)(proteinsTotal * 1000.0F / weight * rarityMod);
/*       */     } 
/*       */ 
/*       */     
/* 22050 */     int ibonus = 0;
/* 22051 */     if (source != null && recipe.hasActiveItem() && recipe.getActiveItem().getTemplateId() != 14) {
/*       */       
/* 22053 */       ibonus += source.getTemplateId();
/* 22054 */       if (!Server.getInstance().isPS())
/* 22055 */         ibonus += source.getRarity(); 
/*       */     } 
/* 22057 */     if (recipe.hasCooker()) {
/*       */       
/* 22059 */       Item cooker = target.getTopParentOrNull();
/* 22060 */       if (cooker != null) {
/*       */         
/* 22062 */         ibonus += cooker.getTemplateId();
/* 22063 */         if (!Server.getInstance().isPS())
/* 22064 */           ibonus += cooker.getRarity(); 
/*       */       } 
/*       */     } 
/* 22067 */     ibonus += target.getTemplateId();
/* 22068 */     if (target.isFoodMaker()) {
/*       */       
/* 22070 */       for (Item item : target.getItemsAsArray()) {
/*       */         
/* 22072 */         ibonus += item.getTemplateId();
/* 22073 */         if (item.usesFoodState())
/* 22074 */           ibonus += item.getAuxData(); 
/* 22075 */         ibonus += item.getMaterial();
/* 22076 */         ibonus += item.getRealTemplateId();
/* 22077 */         if (!Server.getInstance().isPS()) {
/* 22078 */           ibonus += item.getRarity();
/*       */         }
/*       */       } 
/*       */     } else {
/*       */       
/* 22083 */       if (target.usesFoodState())
/* 22084 */         ibonus += target.getAuxData(); 
/* 22085 */       ibonus += target.getMaterial();
/* 22086 */       ibonus += target.getRealTemplateId();
/* 22087 */       if (getTemplateId() == 272)
/* 22088 */         ibonus += target.getData1(); 
/* 22089 */       if (!Server.getInstance().isPS()) {
/* 22090 */         ibonus += target.getRarity();
/*       */       }
/*       */     } 
/* 22093 */     byte bonus = (byte)(ibonus % SkillSystem.getNumberOfSkillTemplates());
/* 22094 */     ItemMealData.save(getWurmId(), recipe.getRecipeId(), calories, carbs, fats, proteins, bonus, stages, ingredients);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setHarvestable(boolean harvestable) {
/* 22099 */     if (this.template.isHarvestable()) {
/*       */       
/* 22101 */       if (harvestable && isPlanted()) {
/* 22102 */         setRightAuxData(1);
/*       */       } else {
/* 22104 */         setRightAuxData(0);
/* 22105 */       }  updateModelNameOnGroundItem();
/*       */     } 
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isHarvestable() {
/* 22111 */     return (this.template.isHarvestable() && getRightAuxData() == 1 && isPlanted() && 
/* 22112 */       getLeftAuxData() > FoliageAge.YOUNG_FOUR.getAgeId() && getLeftAuxData() < FoliageAge.OVERAGED.getAgeId());
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getHoney() {
/* 22117 */     Item[] items = getItemsAsArray();
/* 22118 */     for (Item item : items) {
/*       */       
/* 22120 */       if (item.getTemplateId() == 70)
/* 22121 */         return item; 
/*       */     } 
/* 22123 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getSugar() {
/* 22128 */     Item[] items = getItemsAsArray();
/* 22129 */     for (Item item : items) {
/*       */       
/* 22131 */       if (item.getTemplateId() == 1139)
/* 22132 */         return item; 
/*       */     } 
/* 22134 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getWaxCount() {
/* 22139 */     int waxCount = 0;
/* 22140 */     Item[] items = getItemsAsArray();
/* 22141 */     for (Item item : items) {
/*       */       
/* 22143 */       if (item.getTemplateId() == 1254)
/* 22144 */         waxCount++; 
/*       */     } 
/* 22146 */     return waxCount;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getVinegar() {
/* 22151 */     Item[] items = getItemsAsArray();
/* 22152 */     for (Item item : items) {
/*       */       
/* 22154 */       if (item.getTemplateId() == 1246)
/* 22155 */         return item; 
/*       */     } 
/* 22157 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean setInscription(Recipe recipe, String theInscriber, int thePenColour) {
/* 22164 */     this.inscription = new InscriptionData(getWurmId(), recipe, theInscriber, thePenColour);
/* 22165 */     setAuxData((byte)1);
/* 22166 */     setName("\"" + recipe.getName() + "\"", true);
/* 22167 */     setRarity(recipe.getLootableRarity());
/* 22168 */     return saveInscription();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isMoonMetal() {
/* 22173 */     switch (getMaterial()) {
/*       */       
/*       */       case 56:
/*       */       case 57:
/*       */       case 67:
/* 22178 */         return true;
/*       */     } 
/* 22180 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isAlloyMetal() {
/* 22185 */     switch (getMaterial()) {
/*       */       
/*       */       case 9:
/*       */       case 30:
/*       */       case 31:
/*       */       case 96:
/* 22191 */         return true;
/*       */     } 
/*       */     
/* 22194 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public final String debugLog(int depth) {
/* 22200 */     StringBuilder buf = new StringBuilder();
/* 22201 */     buf.append(toString()).append("\n");
/* 22202 */     buf.append("Last Owner: ").append(this.lastOwner).append(" | Owner: ").append(this.ownerId)
/* 22203 */       .append(" | Parent: ").append(this.parentId).append("\n");
/* 22204 */     buf.append("Material: ").append(getMaterialString(getMaterial())).append(" | Rarity: ").append(getRarity())
/* 22205 */       .append(" | Description: ").append(getDescription()).append("\n");
/*       */     
/*       */     try {
/* 22208 */       StackTraceElement[] steArr = (new Throwable()).getStackTrace();
/* 22209 */       for (int i = 1; i < steArr.length; i++)
/*       */       {
/* 22211 */         if (i >= depth)
/*       */           break; 
/* 22213 */         buf.append(steArr[i].getClassName()).append(".").append(steArr[i].getMethodName()).append(":")
/* 22214 */           .append(steArr[i].getLineNumber()).append("\n");
/*       */       }
/*       */     
/* 22217 */     } catch (Exception e) {
/*       */       
/* 22219 */       e.printStackTrace();
/*       */     } 
/* 22221 */     return buf.toString();
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isSorceryItem() {
/* 22226 */     switch (getTemplateId()) {
/*       */       
/*       */       case 794:
/*       */       case 795:
/*       */       case 796:
/*       */       case 797:
/*       */       case 798:
/*       */       case 799:
/*       */       case 800:
/*       */       case 801:
/*       */       case 802:
/*       */       case 803:
/*       */       case 804:
/*       */       case 805:
/*       */       case 806:
/*       */       case 807:
/*       */       case 808:
/*       */       case 809:
/*       */       case 810:
/* 22245 */         return true;
/*       */     } 
/* 22247 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isEnchanted() {
/* 22252 */     return (getSpellEffects() != null && (getSpellEffects().getEffects()).length > 0);
/*       */   }
/*       */ 
/*       */   
/*       */   public final boolean isDebugLogged() {
/* 22257 */     return (this.rarity > 0 || isMoonMetal() || isEnchanted() || isSorceryItem() || (
/* 22258 */       !getDescription().isEmpty() && !isBulkItem() && !isCorpse() && !isTemporary()) || (
/* 22259 */       !isBodyPart() && !isFood() && !isMushroom() && !isTypeRecycled() && !isCorpse() && !isInventory() && 
/* 22260 */       !isBulk() && !isBulkItem() && getTemplateId() != 177 && getLastOwnerId() > 0L && 
/* 22261 */       getTemplateId() != 314 && getTemplateId() != 1392));
/*       */   }
/*       */ 
/*       */   
/*       */   public void setLightOverride(boolean lightOverride) {
/* 22266 */     this.isLightOverride = lightOverride;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isLightOverride() {
/* 22271 */     return this.isLightOverride;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item getOuterItemOrNull() {
/* 22281 */     if (!getTemplate().isComponentItem())
/* 22282 */       return this; 
/* 22283 */     if (getParentOrNull() == null)
/* 22284 */       return null; 
/* 22285 */     return getParentOrNull().getOuterItemOrNull();
/*       */   }
/*       */   
/*       */   public Item getParentOuterItemOrNull() {
/* 22289 */     Item parent = getParentOrNull();
/* 22290 */     if (parent != null)
/* 22291 */       return parent.getOuterItemOrNull(); 
/* 22292 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getMaxItemCount() {
/* 22297 */     if (getTemplateId() == 1295) {
/*       */       
/* 22299 */       Item outer = getOuterItemOrNull();
/* 22300 */       if (outer != null)
/*       */       {
/* 22302 */         return outer.getMaxItemCount();
/*       */       }
/*       */     } 
/* 22305 */     return getTemplate().getMaxItemCount();
/*       */   }
/*       */ 
/*       */   
/*       */   public int getMaxItemWeight() {
/* 22310 */     if (getTemplateId() == 1295) {
/*       */       
/* 22312 */       Item outer = getOuterItemOrNull();
/* 22313 */       if (outer != null)
/*       */       {
/* 22315 */         return outer.getMaxItemWeight();
/*       */       }
/*       */     } 
/* 22318 */     return getTemplate().getMaxItemWeight();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean canHold(Item target) {
/* 22323 */     if (getMaxItemCount() > -1 && getItemCount() >= getMaxItemCount())
/* 22324 */       return false; 
/* 22325 */     if (getMaxItemWeight() > -1 && 
/* 22326 */       getFullWeight() - getWeightGrams() + target.getFullWeight() > getMaxItemWeight())
/* 22327 */       return false; 
/* 22328 */     return true;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInsulated() {
/* 22333 */     Item parent = getParentOrNull();
/* 22334 */     return (parent != null && parent.getTemplate().isInsulated());
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInLunchbox() {
/* 22339 */     if (!canLarder() && !isLiquid())
/* 22340 */       return false; 
/* 22341 */     Item lunchbox = getParentOuterItemOrNull();
/* 22342 */     return (lunchbox != null && (lunchbox
/* 22343 */       .getTemplateId() == 1296 || lunchbox
/* 22344 */       .getTemplateId() == 1297));
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInTacklebox() {
/* 22350 */     Item parent = getParentOrNull();
/* 22351 */     if (parent == null || parent.getTemplateId() == 1341)
/* 22352 */       return false; 
/* 22353 */     Item container = getParentOuterItemOrNull();
/* 22354 */     return (container != null && container
/* 22355 */       .getTemplateId() == 1341);
/*       */   }
/*       */ 
/*       */   
/*       */   public short getWinches() {
/* 22360 */     return this.warmachineWinches;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setWinches(short newWinches) {
/* 22365 */     this.warmachineWinches = newWinches;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setReplacing(boolean replacing) {
/* 22370 */     this.replacing = replacing;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isReplacing() {
/* 22375 */     return this.replacing;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setIsSealedOverride(boolean overrideSealed) {
/* 22380 */     this.isSealedOverride = overrideSealed;
/* 22381 */     closeAll();
/* 22382 */     updateName();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isSealedOverride() {
/* 22387 */     return this.isSealedOverride;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setWhatHappened(String destroyReason) {
/* 22392 */     this.whatHappened = destroyReason;
/*       */   }
/*       */ 
/*       */   
/*       */   public String getWhatHappened() {
/* 22397 */     return this.whatHappened;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setWasBrandedTo(long wasBrandedTo) {
/* 22402 */     this.wasBrandedTo = wasBrandedTo;
/*       */   }
/*       */ 
/*       */   
/*       */   public long getWasBrandedTo() {
/* 22407 */     return this.wasBrandedTo;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isSaddleBags() {
/* 22412 */     return getTemplate().isSaddleBags();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFishingReel() {
/* 22417 */     return getTemplate().isFishingReel();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFishingLine() {
/* 22422 */     return getTemplate().isFishingLine();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFishingFloat() {
/* 22427 */     return getTemplate().isFishingFloat();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFishingHook() {
/* 22432 */     return getTemplate().isFishingHook();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFishingBait() {
/* 22437 */     return getTemplate().isFishingBait();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public Item[] getFishingItems() {
/* 22445 */     Item fishingReel = null;
/* 22446 */     Item fishingLine = null;
/* 22447 */     Item fishingFloat = null;
/* 22448 */     Item fishingHook = null;
/* 22449 */     Item fishingBait = null;
/* 22450 */     if (getTemplateId() == 1344 || getTemplateId() == 1346) {
/*       */       
/* 22452 */       if (getTemplateId() == 1344) {
/* 22453 */         fishingLine = getFishingLine();
/*       */       } else {
/*       */         
/* 22456 */         fishingReel = getFishingReel();
/* 22457 */         if (fishingReel != null)
/* 22458 */           fishingLine = fishingReel.getFishingLine(); 
/*       */       } 
/* 22460 */       if (fishingLine != null) {
/*       */         
/* 22462 */         fishingFloat = fishingLine.getFishingFloat();
/* 22463 */         fishingHook = fishingLine.getFishingHook();
/*       */       } 
/* 22465 */       if (fishingHook != null)
/* 22466 */         fishingBait = fishingHook.getFishingBait(); 
/*       */     } 
/* 22468 */     return new Item[] { fishingReel, fishingLine, fishingFloat, fishingHook, fishingBait };
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item getFishingReel() {
/* 22477 */     Item[] containsItems = getItemsAsArray();
/*       */     
/* 22479 */     if (containsItems.length > 0)
/*       */     {
/* 22481 */       for (Item contains : containsItems) {
/*       */         
/* 22483 */         if (contains.isFishingReel())
/*       */         {
/*       */           
/* 22486 */           return contains;
/*       */         }
/*       */       } 
/*       */     }
/* 22490 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item getFishingLine() {
/* 22499 */     Item[] containsItems = getItemsAsArray();
/*       */     
/* 22501 */     if (containsItems.length > 0)
/*       */     {
/* 22503 */       for (Item contains : containsItems) {
/*       */         
/* 22505 */         if (contains.isFishingLine())
/*       */         {
/*       */           
/* 22508 */           return contains;
/*       */         }
/*       */       } 
/*       */     }
/* 22512 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public String getFishingLineName() {
/* 22520 */     String lineName = "";
/* 22521 */     switch (getTemplateId()) {
/*       */       
/*       */       case 1372:
/* 22524 */         lineName = "light fishing line";
/*       */         break;
/*       */       case 1373:
/* 22527 */         lineName = "medium fishing line";
/*       */         break;
/*       */       case 1374:
/* 22530 */         lineName = "heavy fishing line";
/*       */         break;
/*       */       case 1375:
/* 22533 */         lineName = "braided fishing line";
/*       */         break;
/*       */     } 
/* 22536 */     return lineName;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item getFishingHook() {
/* 22545 */     Item[] containsItems = getItemsAsArray();
/*       */     
/* 22547 */     if (containsItems.length > 0)
/*       */     {
/* 22549 */       for (Item contains : containsItems) {
/*       */         
/* 22551 */         if (contains.isFishingHook())
/*       */         {
/*       */           
/* 22554 */           return contains;
/*       */         }
/*       */       } 
/*       */     }
/* 22558 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item getFishingFloat() {
/* 22567 */     Item[] containsItems = getItemsAsArray();
/*       */     
/* 22569 */     if (containsItems.length > 0)
/*       */     {
/* 22571 */       for (Item contains : containsItems) {
/*       */         
/* 22573 */         if (contains.isFishingFloat())
/*       */         {
/*       */           
/* 22576 */           return contains;
/*       */         }
/*       */       } 
/*       */     }
/* 22580 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item getFishingBait() {
/* 22589 */     Item[] containsItems = getItemsAsArray();
/* 22590 */     if (containsItems.length > 0)
/*       */     {
/* 22592 */       for (Item contains : containsItems) {
/*       */         
/* 22594 */         if (contains.isFishingBait())
/*       */         {
/*       */           
/* 22597 */           return contains;
/*       */         }
/*       */       } 
/*       */     }
/* 22601 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isFlyTrap() {
/* 22606 */     if (getTemplateId() != 76)
/* 22607 */       return false; 
/* 22608 */     Item[] items = getItemsAsArray();
/* 22609 */     if (items.length >= 1) {
/*       */       
/* 22611 */       boolean hasHoney = false;
/* 22612 */       boolean hasVinegar = false;
/* 22613 */       int count = 0;
/* 22614 */       boolean contaminated = false;
/* 22615 */       for (Item contained : items) {
/*       */         
/* 22617 */         if (contained.getTemplateId() == 70) {
/* 22618 */           hasHoney = true;
/* 22619 */         } else if (contained.getTemplateId() == 1246) {
/* 22620 */           hasVinegar = true;
/* 22621 */         } else if (contained.getTemplateId() == 1359) {
/* 22622 */           count++;
/*       */         } else {
/* 22624 */           contaminated = true;
/*       */         } 
/* 22626 */       }  if (hasHoney && hasVinegar)
/* 22627 */         contaminated = true; 
/* 22628 */       return ((hasHoney || hasVinegar) && !contaminated && count < 99);
/*       */     } 
/* 22630 */     return false;
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
/*       */   public boolean isInside(int... itemTemplateIds) {
/* 22642 */     Item parent = getParentOrNull();
/* 22643 */     if (parent != null) {
/*       */       
/* 22645 */       for (int itemTemplateId : itemTemplateIds) {
/* 22646 */         if (parent.getTemplateId() == itemTemplateId)
/* 22647 */           return true; 
/*       */       } 
/* 22649 */       return parent.isInside(itemTemplateIds);
/*       */     } 
/*       */     
/* 22652 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public Item getFirstParent(int... itemTemplateIds) {
/* 22657 */     Item parent = getParentOrNull();
/* 22658 */     if (parent != null) {
/*       */       
/* 22660 */       for (int itemTemplateId : itemTemplateIds) {
/* 22661 */         if (parent.getTemplateId() == itemTemplateId)
/* 22662 */           return parent; 
/*       */       } 
/* 22664 */       return parent.getFirstParent(itemTemplateIds);
/*       */     } 
/*       */     
/* 22667 */     return null;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isInsidePlacedContainer() {
/* 22677 */     if (getParentOrNull() != null) {
/*       */       
/* 22679 */       if (getParentOrNull().isPlacedOnParent())
/*       */       {
/* 22681 */         return true;
/*       */       }
/*       */ 
/*       */       
/* 22685 */       return getParentOrNull().isInsidePlacedContainer();
/*       */     } 
/*       */     
/* 22688 */     return false;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isInsidePlaceableContainer() {
/* 22693 */     if (getParentOrNull() != null)
/*       */     {
/* 22695 */       if (getParentOrNull().getTemplate().hasViewableSubItems() && (
/* 22696 */         !getParentOrNull().getTemplate().isContainerWithSubItems() || isPlacedOnParent()) && 
/* 22697 */         getTopParent() == getParentId()) {
/* 22698 */         return true;
/*       */       }
/*       */     }
/* 22701 */     return false;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public boolean isProcessedFood() {
/* 22709 */     return (getName().contains("mashed ") || getName().contains("chopped "));
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean doesShowSlopes() {
/* 22714 */     return getTemplate().doesShowSlopes();
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean supportsSecondryColor() {
/* 22719 */     return getTemplate().supportsSecondryColor();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public float getMaterialImpBonus() {
/* 22729 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*       */     {
/* 22731 */       switch (getMaterial()) {
/*       */         
/*       */         case 30:
/* 22734 */           return 1.025F;
/*       */         case 10:
/* 22736 */           return 1.05F;
/*       */         case 12:
/* 22738 */           return 1.1F;
/*       */         case 34:
/* 22740 */           return 1.025F;
/*       */         case 13:
/* 22742 */           return 1.075F;
/*       */       } 
/*       */     }
/* 22745 */     return 1.0F;
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
/*       */   public static float getMaterialCreationBonus(byte material) {
/* 22757 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*       */     {
/* 22759 */       switch (material) {
/*       */         
/*       */         case 30:
/* 22762 */           return 0.1F;
/*       */         case 31:
/* 22764 */           return 0.05F;
/*       */         case 12:
/* 22766 */           return 0.05F;
/*       */         case 34:
/* 22768 */           return 0.05F;
/*       */       } 
/*       */     }
/* 22771 */     return 0.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static float getMaterialLockpickBonus(byte material) {
/* 22782 */     if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*       */       
/* 22784 */       switch (material) {
/*       */         
/*       */         case 56:
/* 22787 */           return 0.05F;
/*       */         case 10:
/* 22789 */           return -0.05F;
/*       */         case 57:
/* 22791 */           return 0.05F;
/*       */         case 7:
/* 22793 */           return -0.025F;
/*       */         case 12:
/* 22795 */           return -0.05F;
/*       */         case 67:
/* 22797 */           return 0.05F;
/*       */         case 8:
/* 22799 */           return 0.025F;
/*       */         case 9:
/* 22801 */           return 0.05F;
/*       */         case 34:
/* 22803 */           return -0.025F;
/*       */         case 13:
/* 22805 */           return -0.025F;
/*       */       } 
/*       */ 
/*       */     
/*       */     } else {
/* 22810 */       if (material == 10)
/* 22811 */         return -0.05F; 
/* 22812 */       if (material == 9)
/* 22813 */         return 0.05F; 
/*       */     } 
/* 22815 */     return 0.0F;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static float getMaterialAnchorBonus(byte material) {
/* 22826 */     if (Features.Feature.METALLIC_ITEMS.isEnabled())
/*       */     {
/* 22828 */       switch (material) {
/*       */         
/*       */         case 56:
/* 22831 */           return 1.5F;
/*       */         case 30:
/* 22833 */           return 0.9F;
/*       */         case 31:
/* 22835 */           return 0.85F;
/*       */         case 10:
/* 22837 */           return 0.95F;
/*       */         case 57:
/* 22839 */           return 1.25F;
/*       */         case 7:
/* 22841 */           return 1.7F;
/*       */         case 11:
/* 22843 */           return 0.85F;
/*       */         case 12:
/* 22845 */           return 1.0F;
/*       */         case 67:
/* 22847 */           return 1.25F;
/*       */         case 8:
/* 22849 */           return 0.975F;
/*       */         case 9:
/* 22851 */           return 0.85F;
/*       */         case 34:
/* 22853 */           return 0.8F;
/*       */         case 13:
/* 22855 */           return 0.75F;
/*       */         case 96:
/* 22857 */           return 1.0F;
/*       */       } 
/*       */     }
/* 22860 */     return 1.0F;
/*       */   }
/*       */ 
/*       */   
/*       */   public int getMaxPlaceableItems() {
/* 22865 */     return (getContainerSizeY() + getContainerSizeZ()) / 10;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean getAuxBit(int bitLoc) {
/* 22870 */     return ((getAuxData() >> bitLoc & 0x1) == 1);
/*       */   }
/*       */ 
/*       */   
/*       */   public void setAuxBit(int bitLoc, boolean value) {
/* 22875 */     if (!value) {
/* 22876 */       setAuxData((byte)(getAuxData() & (1 << bitLoc ^ 0xFFFFFFFF)));
/*       */     } else {
/* 22878 */       setAuxData((byte)(getAuxData() | 1 << bitLoc));
/*       */     } 
/*       */   }
/*       */   
/*       */   public boolean isPlacedOnParent() {
/* 22883 */     return this.placedOnParent;
/*       */   }
/*       */ 
/*       */ 
/*       */   
/*       */   @Nullable
/*       */   public Item recursiveParentCheck() {
/* 22890 */     Item parent = getParentOrNull();
/* 22891 */     if (parent != null) {
/*       */       
/* 22893 */       if (parent.getTemplate().hasViewableSubItems() && (
/* 22894 */         !parent.getTemplate().isContainerWithSubItems() || isPlacedOnParent())) {
/* 22895 */         return this;
/*       */       }
/* 22897 */       return parent.recursiveParentCheck();
/*       */     } 
/*       */     
/* 22900 */     return null;
/*       */   }
/*       */ 
/*       */   
/*       */   public void setChained(boolean chained) {
/* 22905 */     this.isChained = chained;
/*       */   }
/*       */ 
/*       */   
/*       */   public boolean isChained() {
/* 22910 */     return this.isChained;
/*       */   }
/*       */ 
/*       */   
/*       */   public void addSnowmanItem() {
/* 22915 */     if (getTemplateId() != 1437) {
/*       */       return;
/*       */     }
/* 22918 */     if (!isEmpty(true)) {
/*       */       return;
/*       */     }
/* 22921 */     int snowId = 1276;
/* 22922 */     float rand = Server.rand.nextFloat() * 100.0F;
/* 22923 */     if (rand < 1.0E-4F) {
/* 22924 */       snowId = 381;
/* 22925 */     } else if (rand < 0.001F) {
/* 22926 */       snowId = 380;
/* 22927 */     } else if (rand < 0.01F) {
/* 22928 */       snowId = 1397;
/* 22929 */     } else if (rand < 0.02F) {
/* 22930 */       snowId = 205;
/*       */     } 
/*       */     
/*       */     try {
/* 22934 */       Item toPlace = ItemFactory.createItem(snowId, Server.rand.nextFloat() * 50.0F + 50.0F, (byte)0, null);
/* 22935 */       toPlace.setPos(0.0F, 0.0F, 0.8F, Server.rand.nextFloat() * 360.0F, onBridge());
/* 22936 */       toPlace.setLastOwnerId(getWurmId());
/* 22937 */       if (insertItem(toPlace, false, false, true)) {
/*       */         
/* 22939 */         VolaTile vt = Zones.getTileOrNull(getTileX(), getTileY(), isOnSurface());
/* 22940 */         if (vt != null)
/*       */         {
/* 22942 */           for (VirtualZone vz : vt.getWatchers()) {
/*       */             
/* 22944 */             if (vz.isVisible(this, vt))
/*       */             {
/*       */               
/* 22947 */               vz.getWatcher().getCommunicator().sendItem(toPlace, -10L, false);
/* 22948 */               if (toPlace.isLight() && toPlace.isOnFire())
/* 22949 */                 vt.addLightSource(toPlace); 
/* 22950 */               if ((toPlace.getEffects()).length > 0)
/* 22951 */                 for (Effect e : toPlace.getEffects())
/* 22952 */                   vz.addEffect(e, false);  
/* 22953 */               if (toPlace.getColor() != -1) {
/* 22954 */                 vz.sendRepaint(toPlace.getWurmId(), (byte)WurmColor.getColorRed(toPlace.getColor()), 
/* 22955 */                     (byte)WurmColor.getColorGreen(toPlace.getColor()), (byte)WurmColor.getColorBlue(toPlace.getColor()), (byte)-1, (byte)0);
/*       */               }
/* 22957 */               if (toPlace.getColor2() != -1) {
/* 22958 */                 vz.sendRepaint(toPlace.getWurmId(), (byte)WurmColor.getColorRed(toPlace.getColor2()), 
/* 22959 */                     (byte)WurmColor.getColorGreen(toPlace.getColor2()), (byte)WurmColor.getColorBlue(toPlace.getColor2()), (byte)-1, (byte)1);
/*       */               }
/*       */             }
/*       */           
/*       */           } 
/*       */         }
/*       */       } else {
/*       */         
/* 22967 */         Items.destroyItem(toPlace.getWurmId());
/*       */       }
/*       */     
/* 22970 */     } catch (FailedException|NoSuchTemplateException failedException) {}
/*       */   }
/*       */   
/*       */   Item() {}
/*       */   
/*       */   abstract void create(float paramFloat, long paramLong) throws IOException;
/*       */   
/*       */   abstract void load() throws Exception;
/*       */   
/*       */   public abstract void loadEffects();
/*       */   
/*       */   public abstract void bless(int paramInt);
/*       */   
/*       */   public abstract void enchant(byte paramByte);
/*       */   
/*       */   abstract void setPlace(short paramShort);
/*       */   
/*       */   public abstract short getPlace();
/*       */   
/*       */   public abstract void setLastMaintained(long paramLong);
/*       */   
/*       */   public abstract long getLastMaintained();
/*       */   
/*       */   public abstract long getOwnerId();
/*       */   
/*       */   public abstract boolean setOwnerId(long paramLong);
/*       */   
/*       */   public abstract boolean getLocked();
/*       */   
/*       */   public abstract void setLocked(boolean paramBoolean);
/*       */   
/*       */   public abstract int getTemplateId();
/*       */   
/*       */   public abstract void setTemplateId(int paramInt);
/*       */   
/*       */   public abstract void setZoneId(int paramInt, boolean paramBoolean);
/*       */   
/*       */   public abstract int getZoneId();
/*       */   
/*       */   public abstract boolean setDescription(@Nonnull String paramString);
/*       */   
/*       */   @Nonnull
/*       */   public abstract String getDescription();
/*       */   
/*       */   public abstract boolean setInscription(@Nonnull String paramString1, @Nonnull String paramString2);
/*       */   
/*       */   public abstract boolean setInscription(@Nonnull String paramString1, @Nonnull String paramString2, int paramInt);
/*       */   
/*       */   public abstract void setName(@Nonnull String paramString);
/*       */   
/*       */   public abstract void setName(String paramString, boolean paramBoolean);
/*       */   
/*       */   public abstract float getRotation();
/*       */   
/*       */   public abstract void setPosXYZRotation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*       */   
/*       */   public abstract void setPosXYZ(float paramFloat1, float paramFloat2, float paramFloat3);
/*       */   
/*       */   public abstract void setPosXY(float paramFloat1, float paramFloat2);
/*       */   
/*       */   public abstract void setPosX(float paramFloat);
/*       */   
/*       */   public abstract void setPosY(float paramFloat);
/*       */   
/*       */   public abstract void setPosZ(float paramFloat);
/*       */   
/*       */   public abstract void setPos(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*       */   
/*       */   public abstract void savePosition();
/*       */   
/*       */   public abstract void setRotation(float paramFloat);
/*       */   
/*       */   public abstract Set<Item> getItems();
/*       */   
/*       */   public abstract Item[] getItemsAsArray();
/*       */   
/*       */   public abstract void setParentId(long paramLong, boolean paramBoolean);
/*       */   
/*       */   public abstract long getParentId();
/*       */   
/*       */   abstract void setSizeX(int paramInt);
/*       */   
/*       */   abstract void setSizeY(int paramInt);
/*       */   
/*       */   abstract void setSizeZ(int paramInt);
/*       */   
/*       */   public abstract int getSizeX();
/*       */   
/*       */   public abstract int getSizeY();
/*       */   
/*       */   public abstract int getSizeZ();
/*       */   
/*       */   public abstract int getWeightGrams();
/*       */   
/*       */   public abstract boolean setWeight(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
/*       */   
/*       */   public abstract boolean setWeight(int paramInt, boolean paramBoolean);
/*       */   
/*       */   public abstract void setOriginalQualityLevel(float paramFloat);
/*       */   
/*       */   public abstract float getOriginalQualityLevel();
/*       */   
/*       */   public abstract boolean setDamage(float paramFloat, boolean paramBoolean);
/*       */   
/*       */   public abstract void setData1(int paramInt);
/*       */   
/*       */   public abstract void setData2(int paramInt);
/*       */   
/*       */   public abstract void setData(int paramInt1, int paramInt2);
/*       */   
/*       */   public abstract int getData1();
/*       */   
/*       */   public abstract int getData2();
/*       */   
/*       */   public abstract void setExtra1(int paramInt);
/*       */   
/*       */   public abstract void setExtra2(int paramInt);
/*       */   
/*       */   public abstract void setExtra(int paramInt1, int paramInt2);
/*       */   
/*       */   public abstract int getExtra1();
/*       */   
/*       */   public abstract int getExtra2();
/*       */   
/*       */   public abstract void setAllData(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*       */   
/*       */   public abstract void setTemperature(short paramShort);
/*       */   
/*       */   public abstract byte getMaterial();
/*       */   
/*       */   public abstract void setMaterial(byte paramByte);
/*       */   
/*       */   public abstract long getLockId();
/*       */   
/*       */   public abstract void setLockId(long paramLong);
/*       */   
/*       */   public abstract void setPrice(int paramInt);
/*       */   
/*       */   abstract void addItem(@Nullable Item paramItem, boolean paramBoolean);
/*       */   
/*       */   abstract void removeItem(Item paramItem);
/*       */   
/*       */   public abstract void setBanked(boolean paramBoolean);
/*       */   
/*       */   public abstract void setLastOwnerId(long paramLong);
/*       */   
/*       */   public abstract void setAuxData(byte paramByte);
/*       */   
/*       */   public abstract void setCreationState(byte paramByte);
/*       */   
/*       */   public abstract void setRealTemplate(int paramInt);
/*       */   
/*       */   abstract void setWornAsArmour(boolean paramBoolean, long paramLong);
/*       */   
/*       */   public abstract void setColor(int paramInt);
/*       */   
/*       */   public abstract void setColor2(int paramInt);
/*       */   
/*       */   public abstract void setFemale(boolean paramBoolean);
/*       */   
/*       */   public abstract void setTransferred(boolean paramBoolean);
/*       */   
/*       */   abstract void addNewKey(long paramLong);
/*       */   
/*       */   abstract void removeNewKey(long paramLong);
/*       */   
/*       */   public abstract void setMailed(boolean paramBoolean);
/*       */   
/*       */   abstract void clear(long paramLong1, String paramString1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, String paramString2, String paramString3, float paramFloat5, byte paramByte1, byte paramByte2, long paramLong2);
/*       */   
/*       */   public abstract void setHidden(boolean paramBoolean);
/*       */   
/*       */   public abstract void setOwnerStuff(ItemTemplate paramItemTemplate);
/*       */   
/*       */   public abstract boolean setRarity(byte paramByte);
/*       */   
/*       */   public abstract void setDbStrings(DbStrings paramDbStrings);
/*       */   
/*       */   public abstract DbStrings getDbStrings();
/*       */   
/*       */   public abstract void setMailTimes(byte paramByte);
/*       */   
/*       */   public abstract void moveToFreezer();
/*       */   
/*       */   public abstract void returnFromFreezer();
/*       */   
/*       */   public abstract void deleteInDatabase();
/*       */   
/*       */   public abstract float getDamage();
/*       */   
/*       */   public abstract float getQualityLevel();
/*       */   
/*       */   public abstract void setCreator(String paramString);
/*       */   
/*       */   public abstract boolean setDamage(float paramFloat);
/*       */   
/*       */   public abstract boolean setQualityLevel(float paramFloat);
/*       */   
/*       */   public abstract void savePermissions();
/*       */   
/*       */   abstract boolean saveInscription();
/*       */   
/*       */   public abstract void setPlacedOnParent(boolean paramBoolean);
/*       */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */