/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.Behaviour;
/*      */ import com.wurmonline.server.behaviours.Behaviours;
/*      */ import com.wurmonline.server.behaviours.NoSuchBehaviourException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.util.MaterialUtilities;
/*      */ import com.wurmonline.shared.util.StringUtilities;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
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
/*      */ public final class ItemTemplate
/*      */   implements MiscConstants, ItemMaterials, ItemTypes, ItemSizes, Comparable<ItemTemplate>
/*      */ {
/*   47 */   public long damUpdates = 0L;
/*      */   
/*   49 */   public long maintUpdates = 0L;
/*      */ 
/*      */   
/*      */   boolean isDuelRing = false;
/*      */ 
/*      */   
/*      */   private final String name;
/*      */ 
/*      */   
/*      */   private final short behaviourType;
/*      */ 
/*      */   
/*      */   public final short imageNumber;
/*      */ 
/*      */   
/*      */   private final String itemDescriptionLong;
/*      */ 
/*      */   
/*      */   private final String itemDescriptionSuperb;
/*      */ 
/*      */   
/*      */   private final String itemDescriptionNormal;
/*      */ 
/*      */   
/*      */   private final String itemDescriptionBad;
/*      */ 
/*      */   
/*      */   private final String itemDescriptionRotten;
/*      */ 
/*      */   
/*      */   private String concatName;
/*      */ 
/*      */   
/*      */   final int templateId;
/*      */ 
/*      */   
/*      */   private final long decayTime;
/*      */ 
/*      */   
/*   88 */   public float priceHalfSize = 100.0F;
/*      */   
/*      */   private final int centimetersX;
/*      */   
/*      */   private final int centimetersY;
/*      */   
/*      */   private final int centimetersZ;
/*      */   
/*      */   private boolean usesSpecifiedContainerSizes;
/*      */   
/*      */   private int containerCentimetersX;
/*      */   
/*      */   private int containerCentimetersY;
/*      */   private int containerCentimetersZ;
/*      */   private boolean calcNutritionValues = true;
/*  103 */   private short calories = 1000;
/*  104 */   private short carbs = 150;
/*  105 */   private short fats = 40;
/*  106 */   private short proteins = 25;
/*  107 */   private int grows = 0;
/*  108 */   private int crushsTo = 0;
/*  109 */   private int pickSeeds = 0;
/*  110 */   private int alcoholStrength = 0;
/*      */   
/*      */   private boolean canBeCookingOil = false;
/*      */   
/*      */   private boolean useRealTemplateIcon = false;
/*      */   
/*      */   private boolean canBeRawWrapped = false;
/*      */   private boolean canBePapyrusWrapped = false;
/*      */   private boolean canBeClothWrapped = false;
/*      */   private boolean surfaceonly = false;
/*  120 */   private int dyePrimaryAmountRequired = 0;
/*  121 */   private int dyeSecondaryAmountRequired = 0;
/*  122 */   private String secondaryItemName = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int volume;
/*      */ 
/*      */ 
/*      */   
/*      */   private int containerVolume;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int primarySkill;
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte[] bodySpaces;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int combatDamage;
/*      */ 
/*      */ 
/*      */   
/*      */   private final String modelName;
/*      */ 
/*      */ 
/*      */   
/*      */   private final float difficulty;
/*      */ 
/*      */ 
/*      */   
/*  155 */   private static final Logger logger = Logger.getLogger(ItemTemplate.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int weight;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte material;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int value;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isPurchased;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String plural;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int size;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSharp;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hollow = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponslash = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean shield = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean armour = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean food = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean magic = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean magicContainer = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fieldtool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean bodypart = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean inventory = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean inventoryGroup = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean unstableRift = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean miningtool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean carpentrytool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean smithingtool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponpierce = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponcrush = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponaxe = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponsword = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponPolearm = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponknife = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponmisc = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean rechargeable = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean bow = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean bowUnstringed = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean diggingtool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean seed = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean liquid = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean melting = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean meat = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean sign = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fence = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean streetlamp = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean vegetable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean wood = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean metal = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean stone = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean notrade = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean visibleDecay = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean leather = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean cloth = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean paper = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean pottery = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean notake = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean light = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean containerliquid = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean liquidinflammable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weaponmelee = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fish = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean weapon = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean tool = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean lock = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean indestructible = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean key = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean nodrop = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean repairable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean lockable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean temporary = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean combine = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean canHaveInscription = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasdata = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasExtraData = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean viewableSubItems = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isContainerWithSubItems = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean outsideonly = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean coin = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean turnable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean decoration = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fullprice = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean norename = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean nonutrition = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean lownutrition = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mediumnutrition = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean goodnutrition = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean highnutrition = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDish = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isFoodMaker = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean herb = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean spice = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fruit = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean poison = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean draggable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean villagedeed = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean farwalkerItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean homesteaddeed = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean alwayspoll = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean protectionTower = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean floating = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isButcheredItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isNoPut = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isLeadCreature = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isLeadMultipleCreatures = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isFire = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isCarpet = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean domainItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean useOnGroundOnly = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean holyItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hugeAltar = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean artifact = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean unique = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean destroysHugeAltar = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean passFullData = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isForm = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean descIsExam = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isServerBound = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isTwohanded = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean kingdomMarker = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean destroyable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean priceAffectedByMaterial = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean liquidCooking = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean positiveDecay = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean drinkable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isColor = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean colorable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean gem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean egg = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean newbieItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean challengeNewbieItem = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isTileAligned = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isDragonArmour = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isCompass = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isToolbelt = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isBelt = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean oilConsuming = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean candleHolder = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean flickeringLight = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean namedCreator = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean onePerTile = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean fourPerTile = false;
/*      */ 
/*      */ 
/*      */   
/*      */   boolean tenPerTile = false;
/*      */ 
/*      */ 
/*      */   
/*  748 */   Deity deity = null;
/*      */   
/*  750 */   public String sizeString = "";
/*      */   
/*  752 */   int alchemyType = 0;
/*      */   
/*      */   boolean healing = false;
/*      */   
/*      */   boolean bed = false;
/*      */   
/*      */   boolean insideOnly = false;
/*      */   
/*      */   boolean nobank = false;
/*      */   
/*      */   boolean isRecycled = false;
/*      */   
/*      */   public boolean alwaysLoaded = false;
/*      */   
/*      */   boolean brightLight = false;
/*      */   
/*      */   private boolean isVehicle = false;
/*      */   
/*      */   public boolean isChair = false;
/*      */   
/*      */   public boolean isCart = false;
/*      */   
/*      */   boolean isVehicleDragged = false;
/*      */   
/*      */   boolean isMovingItem = false;
/*      */   
/*      */   boolean isFlower = false;
/*      */   
/*      */   boolean isNaturePlantable = false;
/*      */   
/*      */   boolean isImproveItem = false;
/*      */   
/*      */   boolean isDeathProtection = false;
/*      */   
/*      */   public boolean isRoyal = false;
/*      */   
/*      */   public boolean isNoMove = false;
/*      */   
/*      */   public boolean isWind = false;
/*      */   
/*      */   public boolean isDredgingTool = false;
/*      */   
/*      */   public boolean isMineDoor = false;
/*      */   
/*      */   public boolean isNoSellBack = false;
/*      */   
/*      */   public boolean isSpringFilled = false;
/*      */   
/*      */   public boolean destroyOnDecay = false;
/*      */   
/*      */   public boolean isServerPortal = false;
/*      */   
/*      */   public boolean isTrap = false;
/*      */   
/*      */   public boolean isDisarmTrap = false;
/*      */   
/*      */   public boolean nonDeedable = false;
/*      */   
/*      */   boolean plantedFlowerpot = false;
/*      */   
/*      */   boolean ownerDestroyable = false;
/*      */   
/*      */   boolean wearableByCreaturesOnly = false;
/*      */   
/*      */   boolean puppet = false;
/*      */   
/*      */   boolean overrideNonEnchantable = false;
/*      */   
/*      */   boolean isMeditation = false;
/*      */   
/*      */   boolean isTransmutable = false;
/*      */   
/*      */   boolean bulkContainer = false;
/*      */   
/*      */   boolean bulk = false;
/*      */   
/*      */   boolean missions = false;
/*      */   
/*      */   boolean notMissions = false;
/*      */   
/*      */   boolean combineCold = false;
/*      */   
/*      */   boolean spawnsTrees = false;
/*      */   
/*      */   boolean killsTrees = false;
/*      */   
/*      */   boolean isKingdomFlag = false;
/*      */   
/*      */   boolean useMaterialAndKingdom = false;
/*      */   
/*      */   boolean isFlag = false;
/*      */   
/*      */   boolean isCrude = false;
/*      */   
/*      */   boolean minable = false;
/*      */   
/*      */   boolean isEnchantableJewelry = false;
/*      */   
/*      */   boolean isEpicTargetItem = false;
/*      */   
/*      */   boolean isEpicPortal = false;
/*      */   
/*      */   private boolean isMassProduction = false;
/*      */   
/*      */   boolean noWorkParent = false;
/*      */   boolean alwaysBankable = false;
/*  858 */   int improveItem = -1;
/*      */   
/*      */   public boolean alwaysLit = false;
/*      */   
/*      */   public boolean isMushroom = false;
/*      */   
/*      */   public boolean isWarTarget = false;
/*      */   
/*      */   public boolean isSourceSpring = false;
/*      */   
/*      */   public boolean isSource = false;
/*      */   
/*      */   public boolean isColorComponent = false;
/*      */   
/*      */   boolean isTutorialItem = false;
/*      */   
/*      */   private boolean isEquipmentSlot = false;
/*      */   
/*      */   public boolean isOre = false;
/*      */   
/*      */   public boolean isShard = false;
/*      */   
/*      */   boolean isAbility = false;
/*      */   
/*      */   boolean isAltar = false;
/*      */   
/*      */   public boolean isBag = false;
/*      */   
/*      */   public boolean isQuiver = false;
/*      */   
/*      */   private boolean isMagicStaff = false;
/*      */   
/*      */   private boolean isTent = false;
/*      */   
/*      */   private boolean improveUsesTypeAsMaterial = false;
/*      */   
/*      */   private boolean noDiscard;
/*      */   
/*      */   private boolean instaDiscard;
/*      */   
/*      */   private boolean isTransportable = false;
/*      */   
/*      */   private boolean isWarmachine = false;
/*      */   
/*      */   private boolean hideAddToCreationWindow = false;
/*      */   
/*      */   private boolean isBrazier = false;
/*      */   private boolean isSmearable = false;
/*      */   private boolean isItemSpawn = false;
/*      */   private boolean noImprove = false;
/*      */   private boolean isTapestry = false;
/*      */   boolean isUnfinishedNoTake = false;
/*      */   boolean isMilk = false;
/*      */   boolean isCheese = false;
/*      */   boolean isOwnerTurnable = false;
/*      */   boolean isOwnerMoveable = false;
/*      */   boolean isUnfired = false;
/*      */   boolean isPlantable = false;
/*      */   boolean isPlantOneAWeek = false;
/*      */   boolean isRiftItem = false;
/*      */   private boolean isHitchTarget = false;
/*      */   private boolean isRiftAltar = false;
/*      */   private boolean isRiftLoot = false;
/*      */   private boolean hasItemBonus = false;
/*      */   private boolean isBracelet = false;
/*      */   private boolean isPotable = false;
/*      */   private boolean canBeGrownInPot = false;
/*      */   private boolean isAlcohol = false;
/*      */   private boolean isCrushable = false;
/*      */   private boolean hasSeeds = false;
/*      */   private boolean isCooker = false;
/*      */   private boolean isFoodGroup = false;
/*  930 */   private int inFoodGroup = 0;
/*      */   private boolean isCookingTool = false;
/*      */   private boolean isRecipeItem = false;
/*      */   private boolean isNoCreate = false;
/*      */   private boolean usesFoodState = false;
/*      */   private boolean canBeFermented = false;
/*      */   private boolean canBeDistilled = false;
/*      */   private boolean canBeSealed = false;
/*      */   private boolean hovers = false;
/*      */   private boolean foodBonusHot = false;
/*      */   private boolean foodBonusCold = false;
/*      */   private boolean isHarvestable = false;
/*  942 */   private int harvestTo = 0;
/*      */   
/*      */   private boolean isRune = false;
/*      */   private boolean canBePegged = false;
/*      */   private boolean decayOnDeed = false;
/*  947 */   private InitialContainer[] initialContainers = null;
/*      */   private boolean canShowRaw = false;
/*      */   private boolean cannotBeSpellTarget = false;
/*      */   private boolean isTrellis = false;
/*      */   private boolean containsIngredientsOnly = false;
/*      */   private boolean usesRealTemplate = false;
/*      */   private boolean canLarder = false;
/*      */   private boolean isInsulated = false;
/*      */   private boolean isGuardTower = false;
/*      */   private boolean isComponentItem = false;
/*      */   private boolean parentMustBeOnGround = false;
/*  958 */   private int maxItemCount = -1;
/*  959 */   private int maxItemWeight = -1;
/*      */   private boolean isRoadMarker = false;
/*      */   private boolean isPaveable = false;
/*      */   private boolean isCavePaveable = false;
/*      */   public boolean decorationWhenPlanted = false;
/*  964 */   private int fragmentAmount = 3;
/*      */   
/*      */   boolean descIsName = false;
/*      */   boolean isNotRuneable = false;
/*      */   private boolean showsSlopes = false;
/*      */   private boolean supportsSecondryColor = false;
/*      */   private boolean createsWithLock = false;
/*      */   private boolean isFishingReel = false;
/*      */   private boolean isFishingLine = false;
/*      */   private boolean isFishingFloat = false;
/*      */   private boolean isFishingHook = false;
/*      */   private boolean isFishingBait = false;
/*  976 */   private ArrayList<ContainerRestriction> containerRestrictions = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isPluralName = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate(int aTemplateId, int aSize, String aName, String aPlural, String aItemDescriptionSuperb, String aItemDescriptionNormal, String aItemDescriptionBad, String aItemDescriptionRotten, String aItemDescriptionLong, short[] aItemTypes, short aImageNumber, short aBehaviourType, int aCombatDamage, long aDecayTime, int aCentimetersX, int aCentimetersY, int aCentimetersZ, int aPrimarySkill, byte[] aBodySpaces, String aModelName, float aDifficulty, int aWeight, byte aMaterial, int aValue, boolean aIsPurchased) {
/*  997 */     this.templateId = aTemplateId;
/*  998 */     this.name = aName;
/*  999 */     this.plural = aPlural;
/* 1000 */     this.itemDescriptionSuperb = aItemDescriptionSuperb;
/* 1001 */     this.itemDescriptionNormal = aItemDescriptionNormal;
/* 1002 */     this.itemDescriptionBad = aItemDescriptionBad;
/* 1003 */     this.itemDescriptionRotten = aItemDescriptionRotten;
/* 1004 */     this.itemDescriptionLong = aItemDescriptionLong;
/* 1005 */     this.imageNumber = aImageNumber;
/* 1006 */     this.behaviourType = aBehaviourType;
/* 1007 */     this.combatDamage = aCombatDamage;
/* 1008 */     this.decayTime = aDecayTime;
/* 1009 */     int[] sizes = { aCentimetersX, aCentimetersY, aCentimetersZ };
/* 1010 */     Arrays.sort(sizes);
/* 1011 */     this.centimetersX = sizes[0];
/* 1012 */     this.centimetersY = sizes[1];
/* 1013 */     this.centimetersZ = sizes[2];
/* 1014 */     this.volume = aCentimetersX * aCentimetersY * aCentimetersZ;
/* 1015 */     this.primarySkill = aPrimarySkill;
/* 1016 */     this.bodySpaces = aBodySpaces;
/*      */     
/* 1018 */     this.concatName = aName.trim().toLowerCase().replaceAll(" ", "") + ".";
/* 1019 */     if (aModelName == null) {
/*      */       
/* 1021 */       this.modelName = "UNSET";
/* 1022 */       logger.log(Level.WARNING, "Modelname was null for template with id=" + this.templateId);
/*      */     }
/*      */     else {
/*      */       
/* 1026 */       this.modelName = aModelName;
/*      */     } 
/* 1028 */     this.difficulty = aDifficulty;
/* 1029 */     this.weight = aWeight;
/* 1030 */     this.material = aMaterial;
/* 1031 */     this.value = aValue;
/* 1032 */     this.isPurchased = aIsPurchased;
/* 1033 */     this.size = aSize;
/* 1034 */     this.usesSpecifiedContainerSizes = false;
/* 1035 */     setSizeString();
/* 1036 */     assignTypes(aItemTypes);
/* 1037 */     assignTemplateTypes();
/* 1038 */     checkHolyItem();
/* 1039 */     if (this.weight > 2000) {
/* 1040 */       setFragmentAmount(Math.max(3, this.weight / 750));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignTemplateTypes() {
/* 1048 */     if (this.templateId == 527 || this.templateId == 525 || this.templateId == 524 || this.templateId == 509)
/*      */     {
/* 1050 */       this.farwalkerItem = true; } 
/* 1051 */     if (this.templateId == 516 || this.templateId == 102)
/* 1052 */       this.isBelt = true; 
/* 1053 */     if (this.templateId == 664 || this.templateId == 665)
/* 1054 */       this.magicContainer = true; 
/* 1055 */     if (this.templateId == 578 || this.templateId == 579 || this.templateId == 999) {
/*      */ 
/*      */       
/* 1058 */       this.isKingdomFlag = true;
/* 1059 */       this.isFlag = true;
/*      */     }
/* 1061 */     else if (this.templateId == 577 || this.templateId == 487) {
/* 1062 */       this.isFlag = true;
/* 1063 */     } else if (this.templateId == 726 || this.templateId == 728 || this.templateId == 727) {
/*      */ 
/*      */       
/* 1066 */       this.isDuelRing = true;
/* 1067 */       this.nonDeedable = true;
/*      */     }
/* 1069 */     else if (this.templateId == 712 || this.templateId == 714 || this.templateId == 713 || this.templateId == 715 || this.templateId == 716 || this.templateId == 717) {
/*      */ 
/*      */       
/* 1072 */       this.isEpicTargetItem = true;
/*      */       
/* 1074 */       this.onePerTile = true;
/*      */     }
/* 1076 */     else if (this.templateId == 732 || this.templateId == 733) {
/*      */       
/* 1078 */       this.isEpicPortal = true;
/* 1079 */       this.onePerTile = true;
/*      */     }
/* 1081 */     else if (this.templateId == 969 || this.templateId == 970 || this.templateId == 971) {
/*      */       
/* 1083 */       this.isItemSpawn = true;
/* 1084 */     }  if (this.templateId == 931)
/* 1085 */       this.fence = true; 
/* 1086 */     if (this.templateId == 228 || this.templateId == 844 || this.templateId == 729)
/* 1087 */       this.candleHolder = true; 
/* 1088 */     if (this.templateId >= 322 && this.templateId <= 328)
/* 1089 */       this.isAltar = true; 
/* 1090 */     if (this.templateId == 1 || this.templateId == 443 || this.templateId == 2)
/* 1091 */       this.isBag = true; 
/* 1092 */     if (this.templateId == 462)
/* 1093 */       this.isQuiver = true; 
/* 1094 */     if (this.templateId == 824)
/* 1095 */       this.inventoryGroup = true; 
/* 1096 */     if (this.templateId == 939)
/* 1097 */       this.protectionTower = true; 
/* 1098 */     if (this.templateId == 1026)
/* 1099 */       this.unstableRift = true; 
/* 1100 */     if (this.templateId >= 1033 && this.templateId <= 1048) {
/* 1101 */       this.isRiftItem = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean isItemSpawn() {
/* 1106 */     return this.isItemSpawn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isUnstableRift() {
/* 1111 */     return this.unstableRift;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEpicTargetItem() {
/* 1116 */     return this.isEpicTargetItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkHolyItem() {
/* 1121 */     Deity[] deities = Deities.getDeities();
/* 1122 */     for (int x = 0; x < deities.length; x++) {
/*      */       
/* 1124 */       if ((deities[x]).holyItem == this.templateId) {
/*      */         
/* 1126 */         this.deity = deities[x];
/* 1127 */         this.holyItem = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConcatName() {
/* 1135 */     return this.concatName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getValue() {
/* 1144 */     return this.value;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setSizeString() {
/* 1149 */     if (this.size == 1) {
/* 1150 */       this.sizeString = "tiny ";
/* 1151 */     } else if (this.size == 2) {
/* 1152 */       this.sizeString = "small ";
/* 1153 */     } else if (this.size == 4) {
/* 1154 */       this.sizeString = "large ";
/* 1155 */     } else if (this.size == 5) {
/* 1156 */       this.sizeString = "huge ";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1165 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNamePlural() {
/* 1170 */     return this.isPluralName;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getProspectName() {
/* 1175 */     if (getTemplateId() == 1238)
/*      */     {
/* 1177 */       return getName();
/*      */     }
/*      */     
/* 1180 */     return MaterialUtilities.getMaterialString(getMaterial());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPurchased() {
/* 1189 */     return this.isPurchased;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNameWithGenus() {
/* 1194 */     return StringUtilities.addGenus(this.name, this.isPluralName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getMaterial() {
/* 1203 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPlural() {
/* 1212 */     return this.plural;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrimarySkill() {
/* 1222 */     return (this.primarySkill != -10L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPrimarySkill() throws NoSuchSkillException {
/* 1232 */     if (this.primarySkill == -10L)
/* 1233 */       throw new NoSuchSkillException("No skill needed for item " + this.name); 
/* 1234 */     return this.primarySkill;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescriptionSuperb() {
/* 1239 */     return this.itemDescriptionSuperb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDifficulty() {
/* 1248 */     return this.difficulty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getModelName() {
/* 1257 */     return this.modelName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getImageNumber() {
/* 1266 */     return this.imageNumber;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescriptionNormal() {
/* 1271 */     return this.itemDescriptionNormal;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescriptionBad() {
/* 1276 */     return this.itemDescriptionBad;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescriptionRotten() {
/* 1281 */     return this.itemDescriptionRotten;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDescriptionLong() {
/* 1286 */     return this.itemDescriptionLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDamagePercent() {
/* 1291 */     return this.combatDamage;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMassProduction() {
/* 1296 */     return this.isMassProduction;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMushroom() {
/* 1301 */     return this.isMushroom;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hideAddToCreationWindow() {
/* 1306 */     return this.hideAddToCreationWindow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void assignTypes(short[] types) {
/* 1315 */     for (int x = 0; x < types.length; x++) {
/*      */       
/* 1317 */       switch (types[x]) {
/*      */         
/*      */         case 1:
/* 1320 */           this.hollow = true;
/*      */           break;
/*      */         case 2:
/* 1323 */           this.weapon = true;
/* 1324 */           this.weaponslash = true;
/*      */           break;
/*      */         case 3:
/* 1327 */           this.shield = true;
/*      */           break;
/*      */         case 4:
/* 1330 */           this.armour = true;
/*      */           break;
/*      */         case 5:
/* 1333 */           this.food = true;
/* 1334 */           this.canLarder = true;
/*      */           break;
/*      */         case 6:
/* 1337 */           this.magic = true;
/*      */           break;
/*      */         case 7:
/* 1340 */           this.fieldtool = true;
/*      */           break;
/*      */         case 8:
/* 1343 */           this.bodypart = true;
/* 1344 */           this.temporary = true;
/*      */           break;
/*      */         case 9:
/* 1347 */           this.inventory = true;
/*      */           break;
/*      */         case 10:
/* 1350 */           this.miningtool = true;
/*      */           break;
/*      */         case 11:
/* 1353 */           this.carpentrytool = true;
/*      */           break;
/*      */         case 12:
/* 1356 */           this.smithingtool = true;
/*      */           break;
/*      */         case 13:
/* 1359 */           this.weapon = true;
/* 1360 */           this.weaponpierce = true;
/*      */           break;
/*      */         case 14:
/* 1363 */           this.weapon = true;
/* 1364 */           this.weaponcrush = true;
/*      */           break;
/*      */         case 15:
/* 1367 */           this.weapon = true;
/* 1368 */           this.weaponaxe = true;
/*      */           break;
/*      */         case 16:
/* 1371 */           this.weapon = true;
/* 1372 */           this.weaponsword = true;
/*      */           break;
/*      */         case 154:
/* 1375 */           this.weapon = true;
/* 1376 */           this.weaponPolearm = true;
/*      */           break;
/*      */         case 17:
/* 1379 */           this.weapon = true;
/* 1380 */           this.weaponknife = true;
/*      */           break;
/*      */         case 18:
/* 1383 */           this.weapon = true;
/* 1384 */           this.weaponmisc = true;
/*      */           break;
/*      */         case 19:
/* 1387 */           this.diggingtool = true;
/*      */           break;
/*      */         case 20:
/* 1390 */           this.seed = true;
/*      */           break;
/*      */         case 21:
/* 1393 */           this.wood = true;
/*      */           break;
/*      */         case 22:
/* 1396 */           this.metal = true;
/*      */           break;
/*      */         case 23:
/* 1399 */           this.leather = true;
/*      */           break;
/*      */         case 24:
/* 1402 */           this.cloth = true;
/*      */           break;
/*      */         case 25:
/* 1405 */           this.stone = true;
/*      */           break;
/*      */         
/*      */         case 26:
/* 1409 */           this.liquid = true;
/*      */           break;
/*      */         case 27:
/* 1412 */           this.melting = true;
/*      */           break;
/*      */         case 28:
/* 1415 */           this.meat = true;
/*      */           break;
/*      */         case 142:
/* 1418 */           this.sign = true;
/*      */           break;
/*      */         case 143:
/* 1421 */           this.streetlamp = true;
/*      */           break;
/*      */         case 29:
/* 1424 */           this.vegetable = true;
/*      */           break;
/*      */         case 30:
/* 1427 */           this.pottery = true;
/*      */           break;
/*      */         case 31:
/* 1430 */           this.notake = true;
/*      */           break;
/*      */         case 32:
/* 1433 */           this.light = true;
/*      */           break;
/*      */         case 33:
/* 1436 */           this.containerliquid = true;
/*      */           break;
/*      */         
/*      */         case 34:
/* 1440 */           this.liquidinflammable = true;
/*      */           break;
/*      */         case 35:
/* 1443 */           this.weapon = true;
/* 1444 */           this.weaponmelee = true;
/*      */           break;
/*      */         case 36:
/* 1447 */           this.fish = true;
/*      */           break;
/*      */         case 37:
/* 1450 */           this.weapon = true;
/*      */           break;
/*      */         case 38:
/* 1453 */           this.tool = true;
/*      */           break;
/*      */         case 39:
/* 1456 */           this.lock = true;
/*      */           break;
/*      */         case 40:
/* 1459 */           this.indestructible = true;
/* 1460 */           this.isNotRuneable = true;
/*      */           break;
/*      */         case 41:
/* 1463 */           this.key = true;
/*      */           break;
/*      */         case 42:
/* 1466 */           this.nodrop = true;
/*      */           break;
/*      */         case 44:
/* 1469 */           this.repairable = true;
/*      */           break;
/*      */         case 45:
/* 1472 */           this.temporary = true;
/*      */           break;
/*      */         case 46:
/* 1475 */           this.combine = true;
/*      */           break;
/*      */         case 47:
/* 1478 */           this.lockable = true;
/*      */           break;
/*      */         case 159:
/* 1481 */           this.canHaveInscription = true;
/*      */           break;
/*      */         case 48:
/* 1484 */           this.hasdata = true;
/*      */           break;
/*      */         case 49:
/* 1487 */           this.outsideonly = true;
/*      */           break;
/*      */         case 50:
/* 1490 */           this.coin = true;
/* 1491 */           this.fullprice = true;
/*      */           break;
/*      */         case 51:
/* 1494 */           this.turnable = true;
/*      */           break;
/*      */         case 52:
/* 1497 */           this.decoration = true;
/*      */           break;
/*      */         case 53:
/* 1500 */           this.fullprice = true;
/*      */           break;
/*      */         case 54:
/* 1503 */           this.norename = true;
/*      */           break;
/*      */         case 137:
/* 1506 */           this.nonutrition = true;
/*      */           break;
/*      */         case 55:
/* 1509 */           this.lownutrition = true;
/*      */           break;
/*      */         case 74:
/* 1512 */           this.mediumnutrition = true;
/*      */           break;
/*      */         case 75:
/* 1515 */           this.goodnutrition = true;
/*      */           break;
/*      */         case 76:
/* 1518 */           this.highnutrition = true;
/*      */           break;
/*      */         case 77:
/* 1521 */           this.isFoodMaker = true;
/*      */           break;
/*      */         case 56:
/* 1524 */           this.draggable = true;
/*      */           break;
/*      */         case 57:
/* 1527 */           this.villagedeed = true;
/*      */           break;
/*      */         case 58:
/* 1530 */           this.homesteaddeed = true;
/*      */           break;
/*      */         case 59:
/* 1533 */           this.alwayspoll = true;
/*      */           break;
/*      */         case 60:
/* 1536 */           this.floating = true;
/*      */           break;
/*      */         case 61:
/* 1539 */           this.notrade = true;
/*      */           break;
/*      */         case 62:
/* 1542 */           this.hasdata = true;
/* 1543 */           this.isButcheredItem = true;
/*      */           break;
/*      */         case 63:
/* 1546 */           this.isNoPut = true;
/*      */           break;
/*      */         case 64:
/* 1549 */           this.isLeadCreature = true;
/*      */           break;
/*      */         case 198:
/* 1552 */           this.isLeadMultipleCreatures = true;
/*      */           break;
/*      */         case 65:
/* 1555 */           this.isFire = true;
/*      */           break;
/*      */         case 66:
/* 1558 */           this.domainItem = true;
/*      */           break;
/*      */         case 67:
/* 1561 */           this.useOnGroundOnly = true;
/*      */           break;
/*      */         case 68:
/* 1564 */           this.hugeAltar = true;
/* 1565 */           this.nonDeedable = true;
/*      */           break;
/*      */         case 69:
/* 1568 */           this.artifact = true;
/* 1569 */           this.alwaysLoaded = true;
/* 1570 */           this.isServerBound = true;
/*      */           break;
/*      */         case 70:
/* 1573 */           this.unique = true;
/* 1574 */           this.alwaysLoaded = true;
/*      */           break;
/*      */         case 71:
/* 1577 */           this.destroysHugeAltar = true;
/*      */           break;
/*      */         case 72:
/* 1580 */           this.passFullData = true;
/*      */           break;
/*      */         case 73:
/* 1583 */           this.isForm = true;
/*      */           break;
/*      */         case 78:
/* 1586 */           this.herb = true;
/*      */           break;
/*      */         case 205:
/* 1589 */           this.spice = true;
/*      */           break;
/*      */         case 79:
/* 1592 */           this.poison = true;
/*      */           break;
/*      */         case 80:
/* 1595 */           this.fruit = true;
/*      */           break;
/*      */         case 81:
/* 1598 */           this.descIsExam = true;
/*      */           break;
/*      */         case 82:
/* 1601 */           this.isDish = true;
/* 1602 */           this.namedCreator = true;
/* 1603 */           this.food = true;
/* 1604 */           this.canLarder = true;
/*      */           break;
/*      */         case 83:
/* 1607 */           this.isServerBound = true;
/*      */           break;
/*      */         case 84:
/* 1610 */           this.isTwohanded = true;
/*      */           break;
/*      */         case 85:
/* 1613 */           this.kingdomMarker = true;
/*      */           break;
/*      */         case 86:
/* 1616 */           this.destroyable = true;
/*      */           break;
/*      */         case 87:
/* 1619 */           this.priceAffectedByMaterial = true;
/*      */           break;
/*      */         case 88:
/* 1622 */           this.liquidCooking = true;
/*      */           break;
/*      */         case 89:
/* 1625 */           this.positiveDecay = true;
/*      */           break;
/*      */         case 90:
/* 1628 */           this.drinkable = true;
/*      */           break;
/*      */         case 91:
/* 1631 */           this.isColor = true;
/*      */           break;
/*      */         case 92:
/* 1634 */           this.colorable = true;
/*      */           break;
/*      */         case 93:
/* 1637 */           this.gem = true;
/*      */           break;
/*      */         case 94:
/* 1640 */           this.bow = true;
/*      */           break;
/*      */         case 95:
/* 1643 */           this.bowUnstringed = true;
/*      */           break;
/*      */         case 96:
/* 1646 */           this.egg = true;
/*      */           break;
/*      */         case 97:
/* 1649 */           this.newbieItem = true;
/*      */           break;
/*      */         case 189:
/* 1652 */           this.challengeNewbieItem = true;
/*      */           break;
/*      */         case 98:
/* 1655 */           this.isTileAligned = true;
/*      */           break;
/*      */         case 99:
/* 1658 */           this.isDragonArmour = true;
/* 1659 */           setImproveItem();
/*      */           break;
/*      */         case 100:
/* 1662 */           this.isCompass = true;
/*      */           break;
/*      */         case 121:
/* 1665 */           this.isToolbelt = true;
/*      */           break;
/*      */         case 101:
/* 1668 */           this.oilConsuming = true;
/*      */           break;
/*      */         case 102:
/* 1671 */           this.healing = true;
/* 1672 */           this.alchemyType = 1;
/*      */           break;
/*      */         case 103:
/* 1675 */           this.healing = true;
/* 1676 */           this.alchemyType = 2;
/*      */           break;
/*      */         case 104:
/* 1679 */           this.healing = true;
/* 1680 */           this.alchemyType = 3;
/*      */           break;
/*      */         case 105:
/* 1683 */           this.healing = true;
/* 1684 */           this.alchemyType = 4;
/*      */           break;
/*      */         case 106:
/* 1687 */           this.healing = true;
/* 1688 */           this.alchemyType = 5;
/*      */           break;
/*      */         case 108:
/* 1691 */           this.namedCreator = true;
/*      */           break;
/*      */         case 109:
/* 1694 */           this.onePerTile = true;
/*      */           break;
/*      */         case 167:
/* 1697 */           this.fourPerTile = true;
/*      */           break;
/*      */         case 166:
/* 1700 */           this.tenPerTile = true;
/*      */           break;
/*      */         case 110:
/* 1703 */           this.bed = true;
/*      */           break;
/*      */         case 111:
/* 1706 */           this.insideOnly = true;
/*      */           break;
/*      */         case 112:
/* 1709 */           this.nobank = true;
/*      */           break;
/*      */         case 155:
/* 1712 */           this.alwaysBankable = true;
/* 1713 */           this.nobank = false;
/*      */           break;
/*      */         case 113:
/* 1716 */           this.isRecycled = true;
/* 1717 */           this.nobank = true;
/*      */           break;
/*      */         case 114:
/* 1720 */           this.alwaysLoaded = true;
/*      */           break;
/*      */         case 115:
/* 1723 */           this.flickeringLight = true;
/*      */           break;
/*      */         case 116:
/* 1726 */           this.brightLight = true;
/* 1727 */           this.light = true;
/*      */           break;
/*      */         case 117:
/* 1730 */           this.isVehicle = true;
/*      */           break;
/*      */         case 197:
/* 1733 */           this.isChair = true;
/*      */           break;
/*      */         case 134:
/* 1736 */           this.isVehicleDragged = true;
/*      */           break;
/*      */         case 193:
/* 1739 */           this.isCart = true;
/*      */           break;
/*      */         case 118:
/* 1742 */           this.isFlower = true;
/* 1743 */           this.isNaturePlantable = true;
/*      */           break;
/*      */         case 186:
/* 1746 */           this.isNaturePlantable = true;
/*      */           break;
/*      */         case 119:
/* 1749 */           this.isImproveItem = true;
/*      */           break;
/*      */         case 120:
/* 1752 */           this.isDeathProtection = true;
/*      */           break;
/*      */         case 122:
/* 1755 */           this.isRoyal = true;
/* 1756 */           this.isServerBound = true;
/* 1757 */           this.alwaysLoaded = true;
/*      */           break;
/*      */         case 123:
/* 1760 */           this.isNoMove = true;
/*      */           break;
/*      */         case 124:
/* 1763 */           this.isWind = true;
/* 1764 */           this.alwayspoll = true;
/*      */           break;
/*      */         case 125:
/* 1767 */           this.isDredgingTool = true;
/*      */           break;
/*      */         case 126:
/* 1770 */           this.isMineDoor = true;
/*      */           break;
/*      */         case 127:
/* 1773 */           this.isNoSellBack = true;
/*      */           break;
/*      */         case 128:
/* 1776 */           this.isSpringFilled = true;
/*      */           break;
/*      */         case 129:
/* 1779 */           this.destroyOnDecay = true;
/*      */           break;
/*      */         case 130:
/* 1782 */           this.rechargeable = true;
/*      */           break;
/*      */         case 131:
/* 1785 */           this.isServerPortal = true;
/*      */           break;
/*      */         case 132:
/* 1788 */           this.isTrap = true;
/*      */           break;
/*      */         case 133:
/* 1791 */           this.isDisarmTrap = true;
/*      */           break;
/*      */         case 135:
/* 1794 */           this.ownerDestroyable = true;
/*      */           break;
/*      */         case 136:
/* 1797 */           this.wearableByCreaturesOnly = true;
/*      */           break;
/*      */         case 138:
/* 1800 */           this.puppet = true;
/*      */           break;
/*      */         case 139:
/* 1803 */           this.overrideNonEnchantable = true;
/*      */           break;
/*      */         case 140:
/* 1806 */           this.isMeditation = true;
/*      */           break;
/*      */         case 141:
/* 1809 */           this.isTransmutable = true;
/*      */           break;
/*      */         case 144:
/* 1812 */           this.visibleDecay = true;
/*      */           break;
/*      */         case 145:
/* 1815 */           this.bulkContainer = true;
/*      */           break;
/*      */         case 146:
/* 1818 */           this.bulk = true;
/* 1819 */           this.isNotRuneable = true;
/*      */           break;
/*      */         case 147:
/* 1822 */           this.missions = true;
/*      */           break;
/*      */         case 157:
/* 1825 */           this.notMissions = true;
/*      */           break;
/*      */         case 148:
/* 1828 */           this.combineCold = true;
/*      */           break;
/*      */         case 149:
/* 1831 */           this.spawnsTrees = true;
/*      */           break;
/*      */         case 150:
/* 1834 */           this.killsTrees = true;
/*      */           break;
/*      */         case 151:
/* 1837 */           this.isCrude = true;
/*      */           break;
/*      */         case 152:
/* 1840 */           this.minable = true;
/*      */           break;
/*      */         case 153:
/* 1843 */           this.isEnchantableJewelry = true;
/*      */           break;
/*      */         case 156:
/* 1846 */           this.alwaysLit = true;
/*      */           break;
/*      */         case 158:
/* 1849 */           this.isMassProduction = true;
/*      */           break;
/*      */         case 160:
/* 1852 */           this.noWorkParent = true;
/*      */           break;
/*      */         case 161:
/* 1855 */           this.isWarTarget = true;
/*      */           break;
/*      */         case 162:
/* 1858 */           this.isSourceSpring = true;
/*      */           break;
/*      */         case 163:
/* 1861 */           this.isSource = true;
/*      */           break;
/*      */         case 164:
/* 1864 */           this.isColorComponent = true;
/*      */           break;
/*      */         case 165:
/* 1867 */           if (Servers.localServer.entryServer)
/* 1868 */             this.isTutorialItem = true; 
/*      */           break;
/*      */         case 170:
/* 1871 */           this.isEquipmentSlot = true;
/*      */           break;
/*      */         case 168:
/* 1874 */           this.isAbility = true;
/*      */           break;
/*      */         case 169:
/* 1877 */           this.plantedFlowerpot = true;
/*      */           break;
/*      */         case 172:
/* 1880 */           this.isMagicStaff = true;
/*      */           break;
/*      */         case 173:
/* 1883 */           this.improveUsesTypeAsMaterial = true;
/*      */           break;
/*      */         case 174:
/* 1886 */           this.noDiscard = true;
/*      */           break;
/*      */         case 175:
/* 1889 */           this.instaDiscard = true;
/*      */           break;
/*      */         case 176:
/* 1892 */           this.isTransportable = true;
/*      */           break;
/*      */         case 177:
/* 1895 */           this.isWarmachine = true;
/*      */           break;
/*      */         case 178:
/* 1898 */           this.hideAddToCreationWindow = true;
/*      */           break;
/*      */         case 179:
/* 1901 */           this.isBrazier = true;
/*      */           break;
/*      */         case 180:
/* 1904 */           this.usesSpecifiedContainerSizes = true;
/*      */           break;
/*      */         case 181:
/* 1907 */           setTent(true);
/*      */           break;
/*      */         case 182:
/* 1910 */           this.useMaterialAndKingdom = true;
/*      */           break;
/*      */         case 183:
/* 1913 */           setSmearable(true);
/*      */           break;
/*      */         case 184:
/* 1916 */           this.isCarpet = true;
/*      */           break;
/*      */         case 191:
/* 1919 */           this.isMilk = true;
/*      */           break;
/*      */         case 192:
/* 1922 */           this.isCheese = true;
/*      */           break;
/*      */         case 187:
/* 1925 */           this.noImprove = true;
/*      */           break;
/*      */         case 188:
/* 1928 */           this.isTapestry = true;
/*      */           break;
/*      */         case 190:
/* 1931 */           this.isUnfinishedNoTake = true;
/*      */           break;
/*      */         case 194:
/* 1934 */           this.isOwnerTurnable = true;
/*      */           break;
/*      */         case 195:
/* 1937 */           this.isOwnerMoveable = true;
/*      */           break;
/*      */         case 196:
/* 1940 */           this.isUnfired = true;
/*      */           break;
/*      */         case 199:
/* 1943 */           this.isPlantable = true;
/*      */           break;
/*      */         case 200:
/* 1946 */           this.isPlantable = true;
/* 1947 */           this.isPlantOneAWeek = true;
/*      */           break;
/*      */         case 201:
/* 1950 */           this.isHitchTarget = true;
/*      */           break;
/*      */         case 206:
/* 1953 */           this.isPotable = true;
/*      */           break;
/*      */         case 221:
/* 1956 */           this.canBeGrownInPot = true;
/*      */           break;
/*      */         case 209:
/* 1959 */           this.isCooker = true;
/*      */           break;
/*      */         case 208:
/* 1962 */           this.isFoodGroup = true;
/*      */           break;
/*      */         case 210:
/* 1965 */           this.isCookingTool = true;
/*      */           break;
/*      */         case 211:
/* 1968 */           this.isRecipeItem = true;
/*      */           break;
/*      */         case 207:
/* 1971 */           this.isNoCreate = true;
/*      */           break;
/*      */         case 212:
/* 1974 */           this.usesFoodState = true;
/*      */           break;
/*      */         case 213:
/* 1977 */           this.canBeFermented = true;
/*      */           break;
/*      */         case 214:
/* 1980 */           this.canBeDistilled = true;
/*      */           break;
/*      */         case 215:
/* 1983 */           this.canBeSealed = true;
/*      */           break;
/*      */         case 236:
/* 1986 */           this.canBePegged = true;
/*      */           break;
/*      */         case 217:
/* 1989 */           this.canBeCookingOil = true;
/*      */           break;
/*      */         case 216:
/* 1992 */           this.useRealTemplateIcon = true;
/*      */           break;
/*      */         case 218:
/* 1995 */           this.hovers = true;
/*      */           break;
/*      */         case 219:
/* 1998 */           this.foodBonusHot = true;
/*      */           break;
/*      */         case 220:
/* 2001 */           this.foodBonusCold = true;
/*      */           break;
/*      */         case 223:
/* 2004 */           this.canBeRawWrapped = true;
/*      */           break;
/*      */         case 222:
/* 2007 */           this.canBePapyrusWrapped = true;
/*      */           
/* 2009 */           this.usesFoodState = true;
/*      */           break;
/*      */         case 224:
/* 2012 */           this.canBeClothWrapped = true;
/*      */           break;
/*      */         case 225:
/* 2015 */           this.surfaceonly = true;
/*      */           break;
/*      */         case 226:
/* 2018 */           this.isMushroom = true;
/*      */           break;
/*      */         case 228:
/* 2021 */           this.canShowRaw = true;
/*      */           break;
/*      */         case 229:
/* 2024 */           this.cannotBeSpellTarget = true;
/*      */           break;
/*      */         case 230:
/* 2027 */           this.isTrellis = true;
/*      */           break;
/*      */         case 231:
/* 2030 */           this.containsIngredientsOnly = true;
/*      */           break;
/*      */         case 232:
/* 2033 */           this.isComponentItem = true;
/*      */           break;
/*      */         case 240:
/* 2036 */           this.parentMustBeOnGround = true;
/*      */           break;
/*      */         case 233:
/* 2039 */           this.usesRealTemplate = true;
/*      */           break;
/*      */         case 234:
/* 2042 */           this.canLarder = true;
/*      */           break;
/*      */         case 235:
/* 2045 */           this.isRune = true;
/* 2046 */           this.isNotRuneable = true;
/*      */           break;
/*      */         case 237:
/* 2049 */           this.decayOnDeed = true;
/*      */           break;
/*      */         case 238:
/* 2052 */           this.isInsulated = true;
/*      */           break;
/*      */         case 239:
/* 2055 */           this.isGuardTower = true;
/*      */           break;
/*      */         case 241:
/* 2058 */           this.isRoadMarker = true;
/*      */           break;
/*      */         case 242:
/* 2061 */           this.isPaveable = true;
/*      */           break;
/*      */         case 243:
/* 2064 */           this.isCavePaveable = true;
/*      */           break;
/*      */         case 244:
/* 2067 */           this.isPlantable = true;
/* 2068 */           this.decorationWhenPlanted = true;
/*      */           break;
/*      */         case 245:
/* 2071 */           this.descIsName = true;
/*      */           break;
/*      */         case 246:
/* 2074 */           this.isNotRuneable = true;
/*      */           break;
/*      */         case 247:
/* 2077 */           this.showsSlopes = true;
/*      */           break;
/*      */         case 248:
/* 2080 */           this.isPluralName = true;
/*      */           break;
/*      */         case 249:
/* 2083 */           this.supportsSecondryColor = true;
/*      */           break;
/*      */         case 250:
/* 2086 */           this.isFishingReel = true;
/*      */           break;
/*      */         case 251:
/* 2089 */           this.isFishingLine = true;
/*      */           break;
/*      */         case 252:
/* 2092 */           this.isFishingFloat = true;
/*      */           break;
/*      */         case 253:
/* 2095 */           this.isFishingHook = true;
/*      */           break;
/*      */         case 254:
/* 2098 */           this.isFishingBait = true;
/*      */           break;
/*      */         case 255:
/* 2101 */           this.hasExtraData = true;
/*      */           break;
/*      */         case 256:
/* 2104 */           this.viewableSubItems = true;
/*      */           break;
/*      */         case 259:
/* 2107 */           this.viewableSubItems = true;
/* 2108 */           this.isContainerWithSubItems = true;
/*      */           break;
/*      */         case 257:
/* 2111 */           this.createsWithLock = true;
/*      */           break;
/*      */         case 258:
/* 2114 */           this.isBracelet = true;
/*      */           break;
/*      */         default:
/* 2117 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/* 2119 */             logger.fine("Cannot assign type for: " + types[x]);
/*      */           }
/*      */           break;
/*      */       } 
/*      */     } 
/* 2124 */     this.isMovingItem = (this.isVehicle || this.draggable);
/* 2125 */     setIsSharp();
/* 2126 */     setIsOre();
/* 2127 */     setIsShard();
/*      */   }
/*      */ 
/*      */   
/*      */   private void setIsSharp() {
/* 2132 */     if (isWeaponSlash()) {
/* 2133 */       this.isSharp = true;
/*      */     }
/* 2135 */     switch (getTemplateId()) {
/*      */       
/*      */       case 8:
/*      */       case 25:
/*      */       case 93:
/*      */       case 121:
/*      */       case 125:
/*      */       case 126:
/*      */       case 258:
/*      */       case 267:
/*      */       case 268:
/*      */       case 269:
/*      */       case 270:
/* 2148 */         this.isSharp = true;
/*      */         return;
/*      */     } 
/* 2151 */     this.isSharp = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setIsOre() {
/* 2158 */     switch (getTemplateId()) {
/*      */       
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 207:
/*      */       case 693:
/*      */       case 697:
/* 2169 */         this.isOre = true;
/*      */         return;
/*      */     } 
/* 2172 */     this.isOre = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setIsShard() {
/* 2179 */     switch (getTemplateId()) {
/*      */       
/*      */       case 146:
/*      */       case 770:
/*      */       case 785:
/*      */       case 1116:
/*      */       case 1238:
/* 2186 */         this.isShard = true;
/*      */         return;
/*      */     } 
/* 2189 */     this.isShard = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setImproveItem() {
/* 2196 */     if (this.isDragonArmour) {
/*      */       
/* 2198 */       this.improveItem = 371;
/* 2199 */       if (getTemplateId() >= 474) {
/* 2200 */         this.improveItem = 372;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getImproveItem() {
/* 2206 */     return this.improveItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDecayTime() {
/* 2215 */     return this.decayTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSizeX() {
/* 2220 */     return this.centimetersX;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getContainerSizeX() {
/* 2225 */     if (this.usesSpecifiedContainerSizes)
/* 2226 */       return this.containerCentimetersX; 
/* 2227 */     return getSizeX();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSizeY() {
/* 2232 */     return this.centimetersY;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getContainerSizeY() {
/* 2237 */     if (this.usesSpecifiedContainerSizes)
/* 2238 */       return this.containerCentimetersY; 
/* 2239 */     return getSizeY();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSizeZ() {
/* 2244 */     return this.centimetersZ;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getContainerSizeZ() {
/* 2249 */     if (this.usesSpecifiedContainerSizes)
/* 2250 */       return this.containerCentimetersZ; 
/* 2251 */     return getSizeZ();
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemTemplate setContainerSize(int x, int y, int z) {
/* 2256 */     int[] sizes = { x, y, z };
/* 2257 */     Arrays.sort(sizes);
/* 2258 */     this.containerCentimetersX = sizes[0];
/* 2259 */     this.containerCentimetersY = sizes[1];
/* 2260 */     this.containerCentimetersZ = sizes[2];
/* 2261 */     this.containerVolume = x * y * z;
/* 2262 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getContainerVolume() {
/* 2267 */     if (this.usesSpecifiedContainerSizes)
/* 2268 */       return this.containerVolume; 
/* 2269 */     return getVolume();
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean usesSpecifiedContainerSizes() {
/* 2274 */     return this.usesSpecifiedContainerSizes;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ItemTemplate setDyeAmountGrams(int dyeOverrideAmount) {
/* 2279 */     this.dyePrimaryAmountRequired = dyeOverrideAmount;
/* 2280 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ItemTemplate setDyeAmountGrams(int dyeOverridePrimary, int dyeOverrideSecondary) {
/* 2285 */     this.dyePrimaryAmountRequired = dyeOverridePrimary;
/* 2286 */     this.dyeSecondaryAmountRequired = dyeOverrideSecondary;
/* 2287 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDyePrimaryAmountGrams() {
/* 2292 */     return this.dyePrimaryAmountRequired;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDyeSecondaryAmountGrams() {
/* 2297 */     return this.dyeSecondaryAmountRequired;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ItemTemplate setSecondryItem(String secondryItemName, int dyeOverrideAmount) {
/* 2302 */     this.secondaryItemName = secondryItemName;
/* 2303 */     this.dyeSecondaryAmountRequired = dyeOverrideAmount;
/* 2304 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final ItemTemplate setSecondryItem(String secondaryItemName) {
/* 2309 */     this.secondaryItemName = secondaryItemName;
/* 2310 */     this.dyeSecondaryAmountRequired = 0;
/* 2311 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getSecondryItemName() {
/* 2316 */     return this.secondaryItemName;
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
/*      */   public ItemTemplate setNutritionValues(int calories, int carbs, int fats, int proteins) {
/* 2330 */     this.calcNutritionValues = false;
/* 2331 */     this.calories = (short)calories;
/* 2332 */     this.carbs = (short)carbs;
/* 2333 */     this.fats = (short)fats;
/* 2334 */     this.proteins = (short)proteins;
/* 2335 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setAlcoholStrength(int newAlcoholStrength) {
/* 2344 */     this.alcoholStrength = newAlcoholStrength;
/* 2345 */     this.isAlcohol = true;
/* 2346 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setGrows(int growsTemplateId) {
/* 2356 */     this.grows = growsTemplateId;
/* 2357 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getGrows() {
/* 2366 */     if (this.grows == 0)
/* 2367 */       return this.templateId; 
/* 2368 */     return this.grows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setCrushsTo(int toTemplateId) {
/* 2378 */     this.crushsTo = toTemplateId;
/* 2379 */     this.isCrushable = true;
/* 2380 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCrushsTo() {
/* 2389 */     return this.crushsTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setHarvestsTo(int toTemplateId) {
/* 2399 */     this.harvestTo = toTemplateId;
/* 2400 */     this.isHarvestable = true;
/* 2401 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHarvestable() {
/* 2406 */     return this.isHarvestable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHarvestsTo() {
/* 2417 */     return this.harvestTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setPickSeeds(int seedTemplateId) {
/* 2427 */     this.pickSeeds = seedTemplateId;
/* 2428 */     this.hasSeeds = true;
/* 2429 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPickSeeds() {
/* 2438 */     return this.pickSeeds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setFoodGroup(int foodGroupTemplateId) {
/* 2448 */     this.inFoodGroup = foodGroupTemplateId;
/* 2449 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFoodGroup() {
/* 2458 */     if (this.inFoodGroup > 0)
/* 2459 */       return this.inFoodGroup; 
/* 2460 */     return getTemplateId();
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
/*      */   ItemTemplate addContainerRestriction(boolean onlyOneOf, int... itemTemplateId) {
/* 2473 */     if (this.containerRestrictions == null) {
/* 2474 */       this.containerRestrictions = new ArrayList<>();
/*      */     }
/* 2476 */     this.containerRestrictions.add(new ContainerRestriction(onlyOneOf, itemTemplateId));
/* 2477 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   ItemTemplate addContainerRestriction(boolean onlyOneOf, String emptySlotName, int... itemTemplateId) {
/* 2482 */     if (this.containerRestrictions == null) {
/* 2483 */       this.containerRestrictions = new ArrayList<>();
/*      */     }
/* 2485 */     this.containerRestrictions.add(new ContainerRestriction(onlyOneOf, emptySlotName, itemTemplateId));
/* 2486 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<ContainerRestriction> getContainerRestrictions() {
/* 2496 */     return this.containerRestrictions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ItemTemplate setInitialContainers(InitialContainer[] containers) {
/* 2506 */     this.initialContainers = containers;
/* 2507 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InitialContainer[] getInitialContainers() {
/* 2516 */     return this.initialContainers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean calcNutritionValues() {
/* 2525 */     return this.calcNutritionValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getCalories() {
/* 2534 */     return this.calories;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getCarbs() {
/* 2543 */     return this.carbs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getFats() {
/* 2552 */     return this.fats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getProteins() {
/* 2561 */     return this.proteins;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVolume() {
/* 2570 */     return this.volume;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBodySpaces() {
/* 2579 */     return this.bodySpaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTemplateId() {
/* 2588 */     return this.templateId;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getBehaviourType() {
/* 2593 */     return this.behaviourType;
/*      */   }
/*      */ 
/*      */   
/*      */   public Behaviour getBehaviour() throws NoSuchBehaviourException {
/* 2598 */     return Behaviours.getInstance().getBehaviour(this.behaviourType);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getWeightGrams() {
/* 2603 */     return this.weight;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isVehicle() {
/* 2608 */     return this.isVehicle;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAlwaysBankable() {
/* 2613 */     return this.alwaysBankable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHollow() {
/* 2622 */     return this.hollow;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponSlash() {
/* 2627 */     return this.weaponslash;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isShield() {
/* 2636 */     return this.shield;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCrude() {
/* 2641 */     return this.isCrude;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isCarpet() {
/* 2646 */     return this.isCarpet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArmour() {
/* 2655 */     return this.armour;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBracelet() {
/* 2660 */     return this.isBracelet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFood() {
/* 2669 */     return this.food;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFruit() {
/* 2674 */     return this.fruit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMagic() {
/* 2683 */     return this.magic;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFieldTool() {
/* 2688 */     return this.fieldtool;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBodyPart() {
/* 2693 */     return this.bodypart;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBoat() {
/* 2698 */     return (isVehicle() && isFloating());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPuppet() {
/* 2703 */     return this.puppet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInventory() {
/* 2712 */     return this.inventory;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInventoryGroup() {
/* 2717 */     return this.inventoryGroup;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isImproveUsingTypeAsMaterial() {
/* 2722 */     return this.improveUsesTypeAsMaterial;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMiningtool() {
/* 2731 */     return this.miningtool;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCarpentryTool() {
/* 2736 */     return this.carpentrytool;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSmithingTool() {
/* 2741 */     return this.smithingtool;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponPierce() {
/* 2746 */     return this.weaponpierce;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponCrush() {
/* 2751 */     return this.weaponcrush;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponAxe() {
/* 2756 */     return this.weaponaxe;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponSword() {
/* 2761 */     return this.weaponsword;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponPolearm() {
/* 2766 */     return this.weaponPolearm;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponKnife() {
/* 2771 */     return this.weaponknife;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponMisc() {
/* 2776 */     return this.weaponmisc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRechargeable() {
/* 2785 */     return this.rechargeable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDiggingtool() {
/* 2794 */     return this.diggingtool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSeed() {
/* 2803 */     return this.seed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLiquid() {
/* 2812 */     return this.liquid;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLiquidCooking() {
/* 2817 */     return this.liquidCooking;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMelting() {
/* 2825 */     return this.melting;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMeat() {
/* 2834 */     return this.meat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVegetable() {
/* 2843 */     return this.vegetable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWood() {
/* 2852 */     return this.wood;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStone() {
/* 2861 */     return this.stone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMetal() {
/* 2870 */     return this.metal;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoTrade() {
/* 2875 */     return this.notrade;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLeather() {
/* 2884 */     return this.leather;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCloth() {
/* 2893 */     return this.cloth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPottery() {
/* 2902 */     return this.pottery;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPlantedFlowerpot() {
/* 2907 */     return this.plantedFlowerpot;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoTake() {
/* 2912 */     return this.notake;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoImprove() {
/* 2917 */     return this.noImprove;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFlower() {
/* 2922 */     return this.isFlower;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLight() {
/* 2931 */     return this.light;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isContainerLiquid() {
/* 2936 */     return this.containerliquid;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLiquidInflammable() {
/* 2941 */     return this.liquidinflammable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWeaponMelee() {
/* 2946 */     return this.weaponmelee;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFish() {
/* 2955 */     return this.fish;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWeapon() {
/* 2964 */     return this.weapon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTool() {
/* 2973 */     return this.tool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLock() {
/* 2982 */     return this.lock;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIndestructible() {
/* 2991 */     return this.indestructible;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKey() {
/* 3000 */     return this.key;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoDrop() {
/* 3005 */     return this.nodrop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRepairable() {
/* 3014 */     return this.repairable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTemporary() {
/* 3023 */     return this.temporary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCombine() {
/* 3032 */     return this.combine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLockable() {
/* 3041 */     return this.lockable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHaveInscription() {
/* 3046 */     return this.canHaveInscription;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasData() {
/* 3051 */     return this.hasdata;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasExtraData() {
/* 3056 */     return this.hasExtraData;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasViewableSubItems() {
/* 3061 */     return this.viewableSubItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isContainerWithSubItems() {
/* 3066 */     return this.isContainerWithSubItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOutsideOnly() {
/* 3071 */     return this.outsideonly;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSurfaceOnly() {
/* 3076 */     return this.surfaceonly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCoin() {
/* 3085 */     return this.coin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTurnable() {
/* 3094 */     return this.turnable;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTapestry() {
/* 3099 */     return this.isTapestry;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isTransportable() {
/* 3104 */     return this.isTransportable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDecoration() {
/* 3113 */     return this.decoration;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean decayOnDeed() {
/* 3118 */     return this.decayOnDeed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFullprice() {
/* 3127 */     return this.fullprice;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNoRename() {
/* 3132 */     return this.norename;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTutorialItem() {
/* 3137 */     return this.isTutorialItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLownutrition() {
/* 3146 */     return this.lownutrition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDraggable() {
/* 3155 */     return this.draggable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVillageDeed() {
/* 3160 */     return this.villagedeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBarding() {
/* 3165 */     switch (getTemplateId()) {
/*      */       
/*      */       case 702:
/*      */       case 703:
/*      */       case 704:
/* 3170 */         return true;
/*      */     } 
/* 3172 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isRope() {
/* 3178 */     return (getTemplateId() == 319 || getTemplateId() == 1029);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHomesteadDeed() {
/* 3183 */     return this.homesteaddeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAlwaysPoll() {
/* 3188 */     return this.alwayspoll;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMissionItem() {
/* 3193 */     if (this.notMissions)
/* 3194 */       return false; 
/* 3195 */     if (this.isRiftItem || this.isCrude)
/* 3196 */       return false; 
/* 3197 */     return (this.bulk || this.newbieItem || this.missions);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCombineCold() {
/* 3202 */     return this.combineCold;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBulk() {
/* 3207 */     return this.bulk;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isUnfired() {
/* 3212 */     return this.isUnfired;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFloating() {
/* 3221 */     return this.floating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isButcheredItem() {
/* 3230 */     return this.isButcheredItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoPut() {
/* 3239 */     return this.isNoPut;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLeadCreature() {
/* 3248 */     return this.isLeadCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLeadMultipleCreatures() {
/* 3253 */     return this.isLeadMultipleCreatures;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isKingdomMarker() {
/* 3258 */     return this.kingdomMarker;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFire() {
/* 3267 */     return this.isFire;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDomainItem() {
/* 3276 */     return this.domainItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCreatureWearableOnly() {
/* 3281 */     return this.wearableByCreaturesOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseOnGroundOnly() {
/* 3290 */     return this.useOnGroundOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHolyItem() {
/* 3299 */     return this.holyItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBrazier() {
/* 3304 */     return this.isBrazier;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPlantable() {
/* 3309 */     return this.isPlantable;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isPlantOneAWeeek() {
/* 3314 */     return this.isPlantOneAWeek;
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
/*      */   public String toPipeString() {
/* 3328 */     StringBuilder lBuilder = new StringBuilder();
/*      */     
/* 3330 */     lBuilder.append(this.templateId);
/* 3331 */     lBuilder.append('|').append(this.size);
/* 3332 */     lBuilder.append('|').append(this.name);
/* 3333 */     lBuilder.append('|').append(this.plural);
/* 3334 */     lBuilder.append('|').append(this.itemDescriptionSuperb);
/* 3335 */     lBuilder.append('|').append(this.itemDescriptionNormal);
/* 3336 */     lBuilder.append('|').append(this.itemDescriptionBad);
/* 3337 */     lBuilder.append('|').append(this.itemDescriptionRotten);
/* 3338 */     lBuilder.append('|').append(this.itemDescriptionLong);
/*      */     
/* 3340 */     lBuilder.append('|').append(this.imageNumber);
/* 3341 */     lBuilder.append('|').append(this.behaviourType);
/* 3342 */     lBuilder.append('|').append(this.combatDamage);
/* 3343 */     lBuilder.append('|').append(this.decayTime);
/* 3344 */     lBuilder.append('|').append(this.centimetersX);
/* 3345 */     lBuilder.append('|').append(this.centimetersY);
/* 3346 */     lBuilder.append('|').append(this.centimetersZ);
/* 3347 */     lBuilder.append('|').append(this.primarySkill);
/* 3348 */     lBuilder.append('|').append(Arrays.toString(this.bodySpaces));
/* 3349 */     lBuilder.append('|').append(this.modelName);
/* 3350 */     lBuilder.append('|').append(this.difficulty);
/* 3351 */     lBuilder.append('|').append(this.weight);
/* 3352 */     lBuilder.append('|').append(Byte.toString(this.material));
/* 3353 */     lBuilder.append('|').append(this.value);
/* 3354 */     lBuilder.append('|').append(this.isPurchased);
/* 3355 */     lBuilder.append('|').append(-1);
/*      */     
/* 3357 */     return lBuilder.toString();
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
/*      */   public String toString() {
/* 3370 */     StringBuilder lBuilder = new StringBuilder();
/*      */     
/* 3372 */     lBuilder.append("ItemTemplate[");
/* 3373 */     lBuilder.append("ID: ").append(this.templateId);
/* 3374 */     lBuilder.append(", size: ").append(this.size);
/* 3375 */     lBuilder.append(", name: ").append(this.name);
/* 3376 */     lBuilder.append(", plural: ").append(this.plural);
/* 3377 */     lBuilder.append(", itemDescriptionSuperb: ").append(this.itemDescriptionSuperb);
/* 3378 */     lBuilder.append(", itemDescriptionNormal: ").append(this.itemDescriptionNormal);
/* 3379 */     lBuilder.append(", itemDescriptionBad: ").append(this.itemDescriptionBad);
/* 3380 */     lBuilder.append(", itemDescriptionRotten: ").append(this.itemDescriptionRotten);
/* 3381 */     lBuilder.append(", itemDescriptionLong: ").append(this.itemDescriptionLong);
/*      */ 
/*      */     
/* 3384 */     lBuilder.append(", imageNumber: ").append(this.imageNumber);
/* 3385 */     lBuilder.append(", behaviourType: ").append(this.behaviourType);
/* 3386 */     lBuilder.append(", combatDamage: ").append(this.combatDamage);
/* 3387 */     lBuilder.append(", decayTime: ").append(this.decayTime);
/* 3388 */     lBuilder.append(", centimetersX: ").append(this.centimetersX);
/* 3389 */     lBuilder.append(", centimetersY: ").append(this.centimetersY);
/* 3390 */     lBuilder.append(", centimetersZ: ").append(this.centimetersZ);
/* 3391 */     lBuilder.append(", primarySkill: ").append(this.primarySkill);
/* 3392 */     lBuilder.append(", bodySpaces: ").append(Arrays.toString(this.bodySpaces));
/* 3393 */     lBuilder.append(", modelName: ").append(this.modelName);
/* 3394 */     lBuilder.append(", difficulty: ").append(this.difficulty);
/* 3395 */     lBuilder.append(", weight: ").append(this.weight);
/* 3396 */     lBuilder.append(", material: ").append(Byte.toString(this.material));
/* 3397 */     lBuilder.append(", value: ").append(this.value);
/* 3398 */     lBuilder.append(", isPurchased: ").append(this.isPurchased);
/* 3399 */     lBuilder.append(", armourType: ").append(-1);
/* 3400 */     lBuilder.append("]");
/*      */     
/* 3402 */     return lBuilder.toString();
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
/*      */   public int compareTo(ItemTemplate aItemTemplate) {
/* 3415 */     return getName().compareTo(aItemTemplate.getName());
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
/*      */   public boolean isSharp() {
/* 3428 */     return this.isSharp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEquipmentSlot() {
/* 3438 */     return this.isEquipmentSlot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMagicStaff() {
/* 3448 */     return this.isMagicStaff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInstaDiscard() {
/* 3458 */     return this.instaDiscard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInstaDiscard(boolean aInstaDiscard) {
/* 3469 */     this.instaDiscard = aInstaDiscard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDiscard() {
/* 3479 */     return this.noDiscard;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWarmachine() {
/* 3484 */     return this.isWarmachine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoDiscard(boolean aNoDiscard) {
/* 3495 */     this.noDiscard = aNoDiscard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTent() {
/* 3505 */     return this.isTent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTent(boolean aIsTent) {
/* 3516 */     this.isTent = aIsTent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSmearable() {
/* 3526 */     return this.isSmearable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSmearable(boolean aIsSmearable) {
/* 3537 */     this.isSmearable = aIsSmearable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHitchTarget() {
/* 3547 */     return this.isHitchTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftAltar() {
/* 3552 */     return this.isRiftAltar;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRiftAltar(boolean isRiftAltar) {
/* 3557 */     this.isRiftAltar = isRiftAltar;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftItem() {
/* 3562 */     return this.isRiftItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftStoneDeco() {
/* 3567 */     return (getTemplateId() == 1033 || 
/* 3568 */       getTemplateId() == 1034 || 
/* 3569 */       getTemplateId() == 1035 || 
/* 3570 */       getTemplateId() == 1036);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftPlantDeco() {
/* 3575 */     return (getTemplateId() == 1041 || 
/* 3576 */       getTemplateId() == 1042 || 
/* 3577 */       getTemplateId() == 1043 || 
/* 3578 */       getTemplateId() == 1044);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftCrystalDeco() {
/* 3583 */     return (getTemplateId() == 1037 || 
/* 3584 */       getTemplateId() == 1038 || 
/* 3585 */       getTemplateId() == 1039 || 
/* 3586 */       getTemplateId() == 1040);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftLoot() {
/* 3591 */     return this.isRiftLoot;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRiftLoot(boolean isRiftLoot) {
/* 3596 */     this.isRiftLoot = isRiftLoot;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHasItemBonus() {
/* 3601 */     return this.hasItemBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHasItemBonus(boolean hasItemBonus) {
/* 3606 */     this.hasItemBonus = hasItemBonus;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPotable() {
/* 3616 */     return this.isPotable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeGrownInPot() {
/* 3625 */     return this.canBeGrownInPot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesFoodState() {
/* 3634 */     return this.usesFoodState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeFermented() {
/* 3643 */     return this.canBeFermented;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeDistilled() {
/* 3652 */     return this.canBeDistilled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeSealed() {
/* 3661 */     return this.canBeSealed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBePegged() {
/* 3670 */     return this.canBePegged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hovers() {
/* 3679 */     return this.hovers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFoodBonusWhenHot() {
/* 3688 */     return this.foodBonusHot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFoodBonusWhenCold() {
/* 3697 */     return this.foodBonusCold;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canShowRaw() {
/* 3706 */     return this.canShowRaw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean cannotBeSpellTarget() {
/* 3715 */     return this.cannotBeSpellTarget;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTrellis() {
/* 3724 */     return this.isTrellis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsIngredientsOnly() {
/* 3733 */     return this.containsIngredientsOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isShelf() {
/* 3742 */     return (this.isComponentItem && this.parentMustBeOnGround);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesRealTemplate() {
/* 3751 */     return this.usesRealTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCooker() {
/* 3760 */     return this.isCooker;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFoodGroup() {
/* 3769 */     return this.isFoodGroup;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCookingTool() {
/* 3778 */     return this.isCookingTool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRecipeItem() {
/* 3787 */     return this.isRecipeItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoCreate() {
/* 3796 */     return this.isNoCreate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAlcohol() {
/* 3805 */     return this.isAlcohol;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCrushable() {
/* 3814 */     return this.isCrushable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSeeds() {
/* 3823 */     return this.hasSeeds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHerb() {
/* 3832 */     return this.herb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRoadMarker() {
/* 3841 */     return this.isRoadMarker;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPaveable() {
/* 3850 */     return this.isPaveable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCavePaveable() {
/* 3859 */     return this.isCavePaveable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeCookingOil() {
/* 3868 */     return this.canBeCookingOil;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBePapyrusWrapped() {
/* 3877 */     return this.canBePapyrusWrapped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeRawWrapped() {
/* 3886 */     return this.canBeRawWrapped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canBeClothWrapped() {
/* 3895 */     return this.canBeClothWrapped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean useRealTemplateIcon() {
/* 3904 */     return this.useRealTemplateIcon;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMilk() {
/* 3913 */     return this.isMilk;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpice() {
/* 3922 */     return this.spice;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isNoNutrition() {
/* 3927 */     return this.nonutrition;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isLowNutrition() {
/* 3932 */     return this.lownutrition;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isMediumNutrition() {
/* 3937 */     return this.mediumnutrition;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHighNutrition() {
/* 3942 */     return this.highnutrition;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isGoodNutrition() {
/* 3947 */     return this.goodnutrition;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFoodMaker() {
/* 3952 */     return this.isFoodMaker;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAlcoholStrength() {
/* 3961 */     return this.alcoholStrength;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canLarder() {
/* 3966 */     return this.canLarder;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRune() {
/* 3971 */     return this.isRune;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInsulated() {
/* 3976 */     return this.isInsulated;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isGuardTower() {
/* 3981 */     return this.isGuardTower;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isParentMustBeOnGround() {
/* 3986 */     return this.parentMustBeOnGround;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isComponentItem() {
/* 3991 */     return this.isComponentItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAlmanacContainer() {
/* 3996 */     return (this.templateId == 1127 || this.templateId == 1128);
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemTemplate setMaxItemCount(int count) {
/* 4001 */     this.maxItemCount = count;
/* 4002 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemTemplate setMaxItemWeight(int grams) {
/* 4007 */     this.maxItemWeight = grams;
/* 4008 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxItemCount() {
/* 4013 */     return this.maxItemCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxItemWeight() {
/* 4018 */     return this.maxItemWeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemTemplate setFragmentAmount(int count) {
/* 4023 */     this.fragmentAmount = Math.min(127, count);
/* 4024 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFragmentAmount() {
/* 4029 */     return this.fragmentAmount;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSaddleBags() {
/* 4034 */     if (this.templateId == 1333 || this.templateId == 1334) {
/* 4035 */       return true;
/*      */     }
/* 4037 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doesShowSlopes() {
/* 4042 */     return this.showsSlopes;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSecondryColor() {
/* 4047 */     return this.supportsSecondryColor;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doesCreateWithLock() {
/* 4052 */     return this.createsWithLock;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isStatue() {
/* 4057 */     return (this.templateId == 402 || this.templateId == 399 || this.templateId == 400 || this.templateId == 1330 || this.templateId == 1323 || this.templateId == 1328 || this.templateId == 403 || this.templateId == 1325 || this.templateId == 811 || this.templateId == 742 || this.templateId == 1329 || this.templateId == 1327 || this.templateId == 398 || this.templateId == 401 || this.templateId == 1405 || this.templateId == 1407 || this.templateId == 1406 || this.templateId == 1408 || this.templateId == 1324 || this.templateId == 1326 || this.templateId == 1415 || this.templateId == 1416 || this.templateId == 1417 || this.templateId == 1418 || this.templateId == 1419 || this.templateId == 1420 || this.templateId == 1421 || this.templateId == 1430);
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
/*      */   public boolean isMask() {
/* 4071 */     return (this.templateId == 977 || this.templateId == 973 || this.templateId == 978 || this.templateId == 1099 || this.templateId == 975 || this.templateId == 974 || this.templateId == 976 || this.templateId == 1321 || this.templateId == 1306);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStorageRack() {
/* 4078 */     return (this.templateId == 1111 || this.templateId == 1315 || this.templateId == 1312 || this.templateId == 1110 || this.templateId == 1109 || this.templateId == 1108 || this.templateId == 1316 || this.templateId == 724 || this.templateId == 758 || this.templateId == 725);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFishingReel() {
/* 4085 */     return this.isFishingReel;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFishingLine() {
/* 4090 */     return this.isFishingLine;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFishingFloat() {
/* 4095 */     return this.isFishingFloat;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFishingHook() {
/* 4100 */     return this.isFishingHook;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFishingBait() {
/* 4105 */     return this.isFishingBait;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMetalLump() {
/* 4110 */     return (this.templateId == 694 || this.templateId == 221 || this.templateId == 223 || this.templateId == 47 || this.templateId == 698 || this.templateId == 44 || this.templateId == 46 || this.templateId == 49 || this.templateId == 837 || this.templateId == 45 || this.templateId == 205 || this.templateId == 220 || this.templateId == 48 || this.templateId == 1411);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */