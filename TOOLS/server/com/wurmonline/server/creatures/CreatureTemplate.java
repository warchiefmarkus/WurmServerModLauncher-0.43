/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.loot.LootPool;
/*      */ import com.wurmonline.server.loot.LootTable;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CreatureTemplate
/*      */   implements CreatureTemplateIds, CreatureTypes, MiscConstants, SoundNames, ItemMaterials, Comparable<CreatureTemplate>
/*      */ {
/*      */   private final String name;
/*      */   private final String plural;
/*   65 */   private static final Logger logger = Logger.getLogger(CreatureTemplate.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte bodyType;
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte sex;
/*      */ 
/*      */ 
/*      */   
/*      */   private final Skills skills;
/*      */ 
/*      */ 
/*      */   
/*      */   private short vision;
/*      */ 
/*      */ 
/*      */   
/*      */   private final short centimetersHigh;
/*      */ 
/*      */ 
/*      */   
/*      */   private final short centimetersLong;
/*      */ 
/*      */ 
/*      */   
/*      */   private final short centimetersWide;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int size;
/*      */ 
/*      */ 
/*      */   
/*      */   private final String longDesc;
/*      */ 
/*      */ 
/*      */   
/*      */   private final String modelName;
/*      */ 
/*      */ 
/*      */   
/*      */   public final int id;
/*      */ 
/*      */ 
/*      */   
/*  113 */   private final int reputation = 100;
/*      */ 
/*      */   
/*      */   private final String deathSoundMale;
/*      */ 
/*      */   
/*      */   private final String hitSoundMale;
/*      */ 
/*      */   
/*      */   private final String deathSoundFemale;
/*      */ 
/*      */   
/*      */   private final String hitSoundFemale;
/*      */ 
/*      */   
/*      */   private final byte meatMaterial;
/*      */ 
/*      */   
/*      */   static final String BITE_DAMAGE_STRING = "bite";
/*      */ 
/*      */   
/*      */   static final String BREATHE_DAMAGE_STRING = "breathe";
/*      */ 
/*      */   
/*      */   static final String BURN_DAMAGE_STRING = "burn";
/*      */ 
/*      */   
/*      */   static final String POISON_DAMAGE_STRING = "poison";
/*      */ 
/*      */   
/*      */   static final String CLAW_DAMAGE_STRING = "claw";
/*      */ 
/*      */   
/*      */   static final String HEAD_BUTT_DAMAGE_STRING = "headbutt";
/*      */ 
/*      */   
/*      */   static final String HIT_DAMAGE_STRING = "hit";
/*      */   
/*      */   static final String KICK_DAMAGE_STRING = "kick";
/*      */   
/*      */   static final String MAUL_DAMAGE_STRING = "maul";
/*      */   
/*      */   static final String SQUEEZE_DAMAGE_STRING = "squeeze";
/*      */   
/*      */   static final String STING_DAMAGE_STRING = "sting";
/*      */   
/*      */   static final String TAIL_WHIP_DAMAGE_STRING = "tailwhip";
/*      */   
/*      */   static final String WING_BUFF_DAMAGE_STRING = "wingbuff";
/*      */   
/*  163 */   int abilityTitle = -1;
/*  164 */   long abilities = -10L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   private String denName = null;
/*      */   
/*  171 */   private byte denMaterial = 14;
/*      */ 
/*      */ 
/*      */   
/*  175 */   private String handDamString = "hit";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   private String biteDamString = "bite";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  187 */   private String kickDamString = "kick";
/*      */   
/*  189 */   private String headbuttDamString = "headbutt";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  195 */   private String breathDamString = "breathe";
/*      */   
/*      */   private final int aggressivity;
/*      */   
/*  199 */   private float alignment = 0.0F;
/*      */   
/*  201 */   private final Deity deity = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  208 */   private float faith = 0.0F;
/*      */ 
/*      */   
/*      */   private String corpsename;
/*      */ 
/*      */   
/*      */   private final float naturalArmour;
/*      */ 
/*      */   
/*  217 */   private ArmourTemplate.ArmourType armourType = ArmourTemplate.ARMOUR_TYPE_CLOTH;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean sentinel = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean trader = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean moveRandom = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean animal = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean human = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean monster = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean invulnerable = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean npcTrader = false;
/*      */ 
/*      */   
/*      */   private boolean onlyAttacksPlayers = false;
/*      */ 
/*      */   
/*      */   private boolean aggHuman = false;
/*      */ 
/*      */   
/*      */   private boolean moveLocal = false;
/*      */ 
/*      */   
/*      */   private boolean moveGlobal = false;
/*      */ 
/*      */   
/*      */   private boolean grazer = false;
/*      */ 
/*      */   
/*      */   private boolean herd = false;
/*      */ 
/*      */   
/*      */   private boolean villageGuard = false;
/*      */ 
/*      */   
/*      */   private boolean swimming = false;
/*      */ 
/*      */   
/*      */   private boolean hunter = false;
/*      */ 
/*      */   
/*      */   private boolean leadable = false;
/*      */ 
/*      */   
/*      */   private boolean milkable = false;
/*      */ 
/*      */   
/*      */   private boolean regenerating = false;
/*      */ 
/*      */   
/*      */   private boolean dragon = false;
/*      */ 
/*      */   
/*      */   private boolean kingdomGuard = false;
/*      */ 
/*      */   
/*      */   private boolean spiritGuard = false;
/*      */ 
/*      */   
/*      */   private boolean ghost = false;
/*      */ 
/*      */   
/*      */   private boolean bartender = false;
/*      */ 
/*      */   
/*      */   private boolean defendKingdom = false;
/*      */ 
/*      */   
/*      */   private boolean isWarGuard = false;
/*      */ 
/*      */   
/*      */   private boolean aggWhitie = false;
/*      */ 
/*      */   
/*      */   private boolean herbivore = false;
/*      */ 
/*      */   
/*      */   private boolean carnivore = false;
/*      */ 
/*      */   
/*      */   private boolean omnivore = false;
/*      */ 
/*      */   
/*      */   public boolean climber = false;
/*      */ 
/*      */   
/*      */   private boolean dominatable = false;
/*      */ 
/*      */   
/*      */   private boolean undead = false;
/*      */ 
/*      */   
/*      */   private boolean caveDweller = false;
/*      */ 
/*      */   
/*      */   private boolean eggLayer = false;
/*      */ 
/*      */   
/*  336 */   private int eggTemplateId = -1;
/*  337 */   private int childTemplateId = -1;
/*      */   
/*  339 */   private static final int[] emptyMoves = EMPTY_INT_ARRAY;
/*      */   
/*  341 */   private int[] combatMoves = emptyMoves;
/*      */ 
/*      */   
/*      */   private boolean subterranean = false;
/*      */   
/*      */   private boolean isNoSkillgain = false;
/*      */   
/*      */   private boolean isSubmerged = false;
/*      */   
/*      */   private final boolean royalAspiration;
/*      */   
/*      */   private boolean isFloating = false;
/*      */   
/*  354 */   public float offZ = 0.0F;
/*      */ 
/*      */   
/*      */   private boolean isBreakFence = false;
/*      */   
/*      */   private final float handDamage;
/*      */   
/*  361 */   private int mateTemplateId = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final float biteDamage;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final float kickDamage;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final float headButtDamage;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final float breathDamage;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final float speed;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int moveRate;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int[] butcheredItems;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int maxHuntDistance;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean unique = false;
/*      */ 
/*      */ 
/*      */   
/*  408 */   private int leaderTemplateId = -1;
/*      */   
/*  410 */   private int adultFemaleTemplateId = -1;
/*      */   
/*  412 */   private int adultMaleTemplateId = -1;
/*      */   
/*      */   public boolean keepSex = false;
/*      */   
/*      */   private boolean fleeing = false;
/*      */   
/*  418 */   private int maxGroupAttackSize = 1;
/*      */   
/*      */   private boolean tutorial = false;
/*      */   
/*  422 */   private int maxAge = Integer.MAX_VALUE;
/*      */   
/*  424 */   public float baseCombatRating = 1.0F;
/*      */   
/*  426 */   private float bonusCombatRating = 1.0F;
/*      */   
/*  428 */   public byte combatDamageType = 0;
/*      */   
/*  430 */   public int colorRed = 255;
/*  431 */   private int colorGreen = 255;
/*  432 */   private int colorBlue = 255;
/*      */ 
/*      */   
/*  435 */   private String[] colourNameOverrides = new String[] { "grey", "brown", "gold", "black", "white", "piebaldPinto", "bloodBay", "ebonyBlack", "skewbaldpinto", "goldbuckskin", "blacksilver", "appaloosa", "chestnut" };
/*      */ 
/*      */   
/*  438 */   public int maxColourCount = 1;
/*      */ 
/*      */   
/*      */   private boolean glowing = false;
/*      */   
/*  443 */   private int paintMode = 1;
/*      */   
/*  445 */   private int sizeModX = 64;
/*  446 */   private int sizeModY = 64;
/*  447 */   private int sizeModZ = 64;
/*      */   
/*      */   private boolean isOnFire = false;
/*      */   
/*  451 */   private byte fireRadius = 0;
/*      */   
/*      */   private boolean isDetectInvis = false;
/*      */   
/*      */   public boolean nonNewbie = false;
/*      */   
/*      */   boolean isVehicle = false;
/*      */   
/*      */   boolean isHorse = false;
/*      */   
/*      */   boolean hasHands = false;
/*      */   
/*  463 */   byte daysOfPregnancy = 5;
/*      */   
/*      */   public boolean domestic = false;
/*      */   
/*      */   public boolean isBlackOrWhite = false;
/*      */   
/*      */   public boolean isColoured = false;
/*      */   
/*      */   private boolean careful = false;
/*      */   
/*      */   private boolean canOpenDoors = false;
/*      */   
/*      */   private boolean noCorpse = false;
/*      */   
/*      */   public float fireResistance;
/*      */   
/*      */   public float coldResistance;
/*      */   
/*      */   public float diseaseResistance;
/*      */   
/*      */   public float physicalResistance;
/*      */   
/*      */   public float pierceResistance;
/*      */   
/*      */   public float slashResistance;
/*      */   
/*      */   public float crushResistance;
/*      */   
/*      */   public float biteResistance;
/*      */   
/*      */   public float poisonResistance;
/*      */   
/*      */   public float waterResistance;
/*      */   
/*      */   public float acidResistance;
/*      */   
/*      */   public float internalResistance;
/*      */   
/*      */   public float fireVulnerability;
/*      */   
/*      */   public float coldVulnerability;
/*      */   
/*      */   public float diseaseVulnerability;
/*      */   
/*      */   public float physicalVulnerability;
/*      */   
/*      */   public float pierceVulnerability;
/*      */   
/*      */   public float slashVulnerability;
/*      */   
/*      */   public float crushVulnerability;
/*      */   
/*      */   public float biteVulnerability;
/*      */   
/*      */   public float poisonVulnerability;
/*      */   
/*      */   public float waterVulnerability;
/*      */   
/*      */   public float acidVulnerability;
/*      */   
/*      */   public float internalVulnerability;
/*      */   
/*      */   private boolean towerBasher = false;
/*      */   
/*      */   public boolean attacksVehicles = true;
/*      */   
/*      */   public boolean isPrey = false;
/*      */   public boolean isFromValrei = false;
/*      */   public boolean isBeachDweller = false;
/*  532 */   private float maxPercentOfCreatures = 0.01F;
/*  533 */   private int maxPopulationOfCreatures = 0;
/*      */   
/*      */   private boolean usesMaxPopulation = false;
/*      */   
/*      */   private boolean woolProducer = false;
/*      */   
/*      */   private boolean burning = false;
/*      */   
/*      */   private boolean riftCreature = false;
/*      */   private boolean isStealth = false;
/*      */   private boolean isCaster = false;
/*      */   private boolean isSummoner = false;
/*      */   private boolean useNewAttacks = false;
/*  546 */   private final List<AttackAction> primaryAttacks = new ArrayList<>();
/*  547 */   private final List<AttackAction> secondaryAttacks = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isEpicSlayable = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isEpicTraitor = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isMissionDisabled = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isNotRebirthable = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isBabyCreature = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private float boundMinXMeter;
/*      */ 
/*      */ 
/*      */   
/*      */   private float boundMaxXMeter;
/*      */ 
/*      */ 
/*      */   
/*      */   private float boundMinYMeter;
/*      */ 
/*      */ 
/*      */   
/*      */   private float boundMaxYMeter;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasBoundingBox = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean noServerSounds = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private CreatureAI creatureAI;
/*      */ 
/*      */ 
/*      */   
/*      */   private LootTable lootTable;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CreatureTemplate(int aId, String aName, String aPlural, String aLongDesc, String aModelname, int[] aTypes, byte aBodyType, Skills aSkills, short aVision, byte aSex, short aCentimetersHigh, short aCentimetersLong, short aCentimetersWide, String aDeathSndMale, String aDeathSndFemale, String aHitSndMale, String aHitSndFemale, float aNaturalArmour, float aHandDam, float aKickDam, float aBiteDam, float aHeadDam, float aBreathDam, float aSpeed, int aMoveActivity, int[] aItemsButchered, int aMaxHuntdist, int aAggress, byte aMeatMaterial) {
/*  610 */     this.name = aName;
/*  611 */     this.plural = aPlural;
/*  612 */     this.corpsename = aName.trim().toLowerCase().replaceAll(" ", "") + ".";
/*  613 */     this.mateTemplateId = aId;
/*  614 */     this.modelName = aModelname;
/*  615 */     this.sex = aSex;
/*  616 */     this.bodyType = aBodyType;
/*  617 */     this.skills = aSkills;
/*  618 */     this.vision = aVision;
/*  619 */     this.centimetersHigh = aCentimetersHigh;
/*  620 */     this.centimetersLong = aCentimetersLong;
/*  621 */     this.centimetersWide = aCentimetersWide;
/*  622 */     if (aCentimetersHigh > 400 || aCentimetersLong > 400 || aCentimetersWide > 400) {
/*  623 */       this.size = 5;
/*  624 */     } else if (aCentimetersHigh > 200 || aCentimetersLong > 200 || aCentimetersWide > 200) {
/*  625 */       this.size = 4;
/*  626 */     } else if (aCentimetersHigh > 100 || aCentimetersLong > 100 || aCentimetersWide > 100) {
/*  627 */       this.size = 3;
/*  628 */     } else if (aCentimetersHigh > 50 || aCentimetersLong > 50 || aCentimetersWide > 50) {
/*  629 */       this.size = 2;
/*      */     } else {
/*  631 */       this.size = 1;
/*  632 */     }  this.longDesc = aLongDesc;
/*      */     
/*  634 */     this.id = aId;
/*  635 */     if (this.id == 62 || this.id == 63) {
/*  636 */       this.royalAspiration = true;
/*      */     } else {
/*  638 */       this.royalAspiration = false;
/*  639 */     }  this.naturalArmour = aNaturalArmour;
/*  640 */     this.speed = aSpeed;
/*  641 */     if (aMoveActivity > 1900) {
/*  642 */       this.moveRate = 1900;
/*      */     } else {
/*  644 */       this.moveRate = aMoveActivity;
/*  645 */     }  this.handDamage = aHandDam;
/*  646 */     this.kickDamage = aKickDam;
/*  647 */     this.biteDamage = aBiteDam;
/*  648 */     this.headButtDamage = aHeadDam;
/*  649 */     this.breathDamage = aBreathDam;
/*  650 */     this.butcheredItems = aItemsButchered;
/*  651 */     this.maxHuntDistance = aMaxHuntdist;
/*  652 */     this.aggressivity = aAggress;
/*  653 */     this.hitSoundFemale = aHitSndFemale;
/*  654 */     this.hitSoundMale = aHitSndMale;
/*  655 */     this.deathSoundMale = aDeathSndMale;
/*  656 */     this.deathSoundFemale = aDeathSndFemale;
/*  657 */     this.meatMaterial = aMeatMaterial;
/*  658 */     assignTypes(aTypes);
/*  659 */     checkNoCorpse();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void checkNoCorpse() {
/*  664 */     if (this.id == 78 || this.id == 81 || this.id == 79 || this.id == 80)
/*      */     {
/*  666 */       this.noCorpse = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public final int[] getItemsButchered() {
/*  671 */     if (this.butcheredItems != null) {
/*  672 */       return this.butcheredItems;
/*      */     }
/*  674 */     return EMPTY_INT_ARRAY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setHeadbuttDamString(String damString) {
/*  683 */     this.headbuttDamString = damString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setKickDamString(String damString) {
/*  692 */     this.kickDamString = damString;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaxPercentOfCreatures(float percent) {
/*  697 */     this.maxPercentOfCreatures = percent;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getMaxPercentOfCreatures() {
/*  702 */     return this.maxPercentOfCreatures;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaxPopulationOfCreatures(int maxPopulation) {
/*  707 */     this.maxPopulationOfCreatures = maxPopulation;
/*  708 */     this.usesMaxPopulation = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBoundsValues(float minX, float minY, float maxX, float maxY) {
/*  713 */     this.boundMinXMeter = minX;
/*  714 */     this.boundMinYMeter = minY;
/*  715 */     this.boundMaxXMeter = maxX;
/*  716 */     this.boundMaxYMeter = maxY;
/*  717 */     this.hasBoundingBox = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasBoundingBox() {
/*  722 */     return this.hasBoundingBox;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getBoundMinX() {
/*  727 */     return this.boundMinXMeter;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getBoundMaxX() {
/*  732 */     return this.boundMaxXMeter;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getBoundMinY() {
/*  737 */     return this.boundMinYMeter;
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getBoundMaxY() {
/*  742 */     return this.boundMaxYMeter;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getMaxPopulationOfCreatures() {
/*  747 */     return this.maxPopulationOfCreatures;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean usesMaxPopulation() {
/*  752 */     return this.usesMaxPopulation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBreathDamString(String damString) {
/*  761 */     this.breathDamString = damString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setHandDamString(String damString) {
/*  770 */     this.handDamString = damString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBiteDamString(String damString) {
/*  779 */     this.biteDamString = damString;
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
/*      */   public final float getSpeed() {
/*  801 */     return this.speed;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getDeathSound(byte aSex) {
/*  806 */     if (aSex == 1) {
/*  807 */       return this.deathSoundFemale;
/*      */     }
/*  809 */     return this.deathSoundMale;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getHitSound(byte aSex) {
/*  814 */     if (aSex == 1) {
/*  815 */       return this.hitSoundFemale;
/*      */     }
/*  817 */     return this.hitSoundMale;
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
/*      */   final int getMoveRate() {
/*  839 */     return this.moveRate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ArmourTemplate.ArmourType getArmourType() {
/*  849 */     return this.armourType;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPrimaryAttack(AttackAction attack) {
/*  854 */     this.primaryAttacks.add(attack);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addSecondaryAttack(AttackAction attack) {
/*  859 */     this.secondaryAttacks.add(attack);
/*      */   }
/*      */ 
/*      */   
/*      */   public final List<AttackAction> getPrimaryAttacks() {
/*  864 */     return this.primaryAttacks;
/*      */   }
/*      */ 
/*      */   
/*      */   public final List<AttackAction> getSecondaryAttacks() {
/*  869 */     return this.secondaryAttacks;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setUsesNewAttacks(boolean newAttacks) {
/*  874 */     this.useNewAttacks = newAttacks;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isUsingNewAttacks() {
/*  879 */     return this.useNewAttacks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getNaturalArmour() {
/*  888 */     return this.naturalArmour;
/*      */   }
/*      */ 
/*      */   
/*      */   final boolean isVowel(String letter) {
/*  893 */     return ("aeiouAEIOU".indexOf(letter) != -1);
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
/*      */   public final String getName() {
/*  908 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getPlural() {
/*  917 */     return this.plural;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTemplateId() {
/*  922 */     return this.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String examine() {
/*  927 */     return this.longDesc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getModelName() {
/*  936 */     return this.modelName;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isNoCorpse() {
/*  941 */     return this.noCorpse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getBodyType() {
/*  950 */     return this.bodyType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getVision() {
/*  959 */     return this.vision;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVision(short vision) {
/*  964 */     this.vision = vision;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getSex() {
/*  973 */     return this.sex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Skills getSkills() throws Exception {
/*  983 */     return this.skills;
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
/*      */   public final short getCentimetersLong() {
/* 1002 */     return this.centimetersLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getCentimetersHigh() {
/* 1011 */     return this.centimetersHigh;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getCentimetersWide() {
/* 1020 */     return this.centimetersWide;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getCorpsename() {
/* 1030 */     return this.corpsename;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCorpseName(String corpseName) {
/* 1035 */     this.corpsename = corpseName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getSize() {
/* 1044 */     return this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   private void assignTypes(int[] aTypes) {
/* 1049 */     for (int x = 0; x < aTypes.length; x++) {
/*      */       
/* 1051 */       switch (aTypes[x]) {
/*      */         
/*      */         case 0:
/* 1054 */           this.sentinel = true;
/*      */           break;
/*      */         case 1:
/* 1057 */           this.trader = true;
/*      */           break;
/*      */         case 2:
/* 1060 */           this.moveRandom = true;
/*      */           break;
/*      */         case 3:
/* 1063 */           this.animal = true;
/*      */           break;
/*      */         case 17:
/* 1066 */           this.human = true;
/*      */           break;
/*      */         case 16:
/* 1069 */           this.monster = true;
/*      */           break;
/*      */         
/*      */         case 4:
/* 1073 */           this.invulnerable = true;
/*      */           break;
/*      */         case 5:
/* 1076 */           this.npcTrader = true;
/* 1077 */           this.trader = true;
/*      */           break;
/*      */         case 6:
/* 1080 */           this.aggHuman = true;
/*      */           break;
/*      */         case 7:
/* 1083 */           this.moveLocal = true;
/*      */           break;
/*      */         case 8:
/* 1086 */           this.moveGlobal = true;
/*      */           break;
/*      */         case 9:
/* 1089 */           this.grazer = true;
/*      */           break;
/*      */         case 10:
/* 1092 */           this.herd = true;
/*      */           break;
/*      */         case 11:
/* 1095 */           this.villageGuard = true;
/*      */           break;
/*      */         case 12:
/* 1098 */           this.swimming = true;
/*      */           break;
/*      */         case 13:
/* 1101 */           this.hunter = true;
/*      */           break;
/*      */         case 14:
/* 1104 */           this.leadable = true;
/*      */           break;
/*      */         case 15:
/* 1107 */           this.milkable = true;
/*      */           break;
/*      */         
/*      */         case 18:
/* 1111 */           this.regenerating = true;
/*      */           break;
/*      */         case 19:
/* 1114 */           this.dragon = true;
/*      */           break;
/*      */         case 20:
/* 1117 */           this.unique = true;
/*      */           break;
/*      */         case 21:
/* 1120 */           this.kingdomGuard = true;
/*      */           break;
/*      */         case 23:
/* 1123 */           this.spiritGuard = true;
/* 1124 */           this.isFloating = true;
/*      */           break;
/*      */         case 22:
/* 1127 */           this.ghost = true;
/*      */           break;
/*      */         case 26:
/* 1130 */           this.bartender = true;
/*      */           break;
/*      */         case 24:
/* 1133 */           this.defendKingdom = true;
/*      */           break;
/*      */         case 25:
/* 1136 */           this.aggWhitie = true;
/*      */           break;
/*      */         case 28:
/* 1139 */           this.herbivore = true;
/* 1140 */           this.fleeing = true;
/*      */           break;
/*      */         case 29:
/* 1143 */           this.carnivore = true;
/*      */           break;
/*      */         case 27:
/* 1146 */           this.omnivore = true;
/*      */           break;
/*      */         case 30:
/* 1149 */           this.climber = true;
/*      */           break;
/*      */         case 32:
/* 1152 */           this.dominatable = true;
/*      */           break;
/*      */         case 33:
/* 1155 */           this.undead = true;
/*      */           break;
/*      */         case 34:
/* 1158 */           this.caveDweller = true;
/*      */           break;
/*      */         case 35:
/* 1161 */           this.fleeing = true;
/*      */           break;
/*      */         case 36:
/* 1164 */           this.isDetectInvis = true;
/*      */           break;
/*      */         case 37:
/* 1167 */           this.isSubmerged = true;
/*      */           break;
/*      */         case 38:
/* 1170 */           this.isFloating = true;
/*      */           break;
/*      */         case 39:
/* 1173 */           this.nonNewbie = true;
/*      */           break;
/*      */         case 40:
/* 1176 */           this.isBreakFence = true;
/*      */           break;
/*      */         case 41:
/* 1179 */           this.isVehicle = true;
/*      */           break;
/*      */         case 42:
/* 1182 */           this.isHorse = true;
/*      */           break;
/*      */         case 43:
/* 1185 */           this.domestic = true;
/*      */           break;
/*      */         case 54:
/* 1188 */           this.isBlackOrWhite = true;
/*      */           break;
/*      */         case 64:
/* 1191 */           this.isColoured = true;
/*      */           break;
/*      */         case 44:
/* 1194 */           this.moveRate = 100;
/* 1195 */           this.careful = true;
/*      */           break;
/*      */         case 45:
/* 1198 */           this.canOpenDoors = true;
/*      */           break;
/*      */         case 46:
/* 1201 */           setTowerBasher(true);
/*      */           break;
/*      */         case 47:
/* 1204 */           setOnlyAttacksPlayers(true);
/*      */           break;
/*      */         case 48:
/* 1207 */           this.attacksVehicles = false;
/*      */           break;
/*      */         case 49:
/* 1210 */           this.isPrey = true;
/*      */           break;
/*      */         case 50:
/* 1213 */           this.isFromValrei = true;
/*      */           break;
/*      */         case 51:
/* 1216 */           this.isBeachDweller = true;
/*      */           break;
/*      */         case 52:
/* 1219 */           this.woolProducer = true;
/*      */           break;
/*      */         case 53:
/* 1222 */           setWarGuard(true);
/*      */           break;
/*      */         case 55:
/* 1225 */           this.burning = true;
/*      */           break;
/*      */         case 56:
/* 1228 */           setRiftCreature(true);
/*      */           break;
/*      */         case 57:
/* 1231 */           setStealth(true);
/*      */           break;
/*      */         case 58:
/* 1234 */           setCaster(true);
/*      */           break;
/*      */         case 59:
/* 1237 */           setSummoner(true);
/*      */           break;
/*      */         case 60:
/* 1240 */           this.isEpicSlayable = true;
/*      */           break;
/*      */         case 61:
/* 1243 */           this.isEpicTraitor = true;
/*      */           break;
/*      */         case 62:
/* 1246 */           this.isNotRebirthable = true;
/*      */           break;
/*      */         case 63:
/* 1249 */           this.isBabyCreature = true;
/*      */           break;
/*      */         case 65:
/* 1252 */           this.isMissionDisabled = true;
/*      */           break;
/*      */         default:
/* 1255 */           logger.warning("Ignoring unexpected CreatureTemplate type: " + aTypes[x]);
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isBeachDweller() {
/* 1263 */     return this.isBeachDweller;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSentinel() {
/* 1272 */     return this.sentinel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrey() {
/* 1281 */     return this.isPrey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCareful() {
/* 1290 */     return this.careful;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setOnlyAttacksPlayers(boolean attacks) {
/* 1295 */     this.onlyAttacksPlayers = attacks;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean onlyAttacksPlayers() {
/* 1300 */     return this.onlyAttacksPlayers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canOpenDoors() {
/* 1309 */     return this.canOpenDoors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubterranean() {
/* 1319 */     return this.subterranean;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isHellHorse() {
/* 1324 */     return (this.id == 83);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isUnicorn() {
/* 1329 */     return (this.id == 21);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean cantRideUntamed() {
/* 1334 */     return (this.id == 59 || this.id == 12 || this.id == 58 || this.id == 21 || 
/*      */ 
/*      */ 
/*      */       
/* 1338 */       isDragon());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSubterranean(boolean aSubterranean) {
/* 1349 */     this.subterranean = aSubterranean;
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
/*      */   public final boolean isNeedFood() {
/* 1361 */     return (this.carnivore || this.herbivore || this.omnivore || this.grazer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTrader() {
/* 1370 */     return this.trader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMoveRandom() {
/* 1379 */     return this.moveRandom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAnimal() {
/* 1388 */     return this.animal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMonster() {
/* 1398 */     return this.monster;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isDragon() {
/* 1408 */     return this.dragon;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isDragon(int typeId) {
/* 1413 */     return (isFullyGrownDragon(typeId) || isDragonHatchling(typeId));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isFullyGrownDragon(int typeId) {
/* 1418 */     return (typeId == 89 || typeId == 91 || typeId == 90 || typeId == 92 || typeId == 16);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isDragonHatchling(int typeId) {
/* 1424 */     return (typeId == 18 || typeId == 104 || typeId == 17 || typeId == 103 || typeId == 19);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFleeing() {
/* 1435 */     return this.fleeing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHuman() {
/* 1445 */     return this.human;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isRegenerating() {
/* 1455 */     return this.regenerating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isInvulnerable() {
/* 1464 */     return this.invulnerable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isNpcTrader() {
/* 1473 */     return this.npcTrader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAggHuman() {
/* 1482 */     return this.aggHuman;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMoveLocal() {
/* 1491 */     return this.moveLocal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isMoveGlobal() {
/* 1500 */     return this.moveGlobal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isGrazer() {
/* 1509 */     return this.grazer;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWoolProducer() {
/* 1514 */     return this.woolProducer;
/*      */   }
/*      */   
/*      */   public final boolean isBurning() {
/* 1518 */     return this.burning;
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
/*      */   final boolean isHerd() {
/* 1535 */     return this.herd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSwimming() {
/* 1544 */     return this.swimming;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isLeadable() {
/* 1553 */     return this.leadable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getCombatDamageType() {
/* 1563 */     return this.combatDamageType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getAggressivity() {
/* 1573 */     return this.aggressivity;
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
/*      */   public final float getHandDamage() {
/* 1593 */     return this.handDamage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBiteDamage() {
/* 1602 */     return this.biteDamage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getKickDamage() {
/* 1611 */     return this.kickDamage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getHeadButtDamage() {
/* 1620 */     return this.headButtDamage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getBreathDamage() {
/* 1629 */     return this.breathDamage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getKickDamString() {
/* 1638 */     return this.kickDamString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getBiteDamString() {
/* 1647 */     return this.biteDamString;
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getHeadButtDamString() {
/* 1652 */     return this.headbuttDamString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getBreathDamString() {
/* 1661 */     return this.breathDamString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getHandDamString() {
/* 1670 */     return this.handDamString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isHunter() {
/* 1679 */     return this.hunter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isUnique() {
/* 1688 */     return this.unique;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isMilkable() {
/* 1697 */     return this.milkable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFaith() {
/* 1706 */     if (isCaster() || isSummoner())
/* 1707 */       return 100.0F; 
/* 1708 */     return this.faith;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Deity getDeity() {
/* 1717 */     if (isCaster() || isSummoner())
/* 1718 */       return Deities.getDeity(4); 
/* 1719 */     return this.deity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLeaderTemplateId(int aLeaderTemplateId) {
/* 1730 */     this.leaderTemplateId = aLeaderTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getLeaderTemplateId() {
/* 1739 */     return this.leaderTemplateId;
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
/*      */   public final int compareTo(CreatureTemplate o1) {
/* 1760 */     return getName().compareTo(o1.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean hasDen() {
/* 1770 */     return (this.denName != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getDenName() {
/* 1780 */     return this.denName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDenName(String aDenName) {
/* 1791 */     this.denName = aDenName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getDenMaterial() {
/* 1801 */     return this.denMaterial;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDenMaterial(byte aMaterial) {
/* 1812 */     this.denMaterial = aMaterial;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isRoyalAspiration() {
/* 1817 */     return this.royalAspiration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getAlignment() {
/* 1827 */     return this.alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlignment(float aAlignment) {
/* 1838 */     this.alignment = aAlignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGhost() {
/* 1848 */     return this.ghost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBartender() {
/* 1858 */     return this.bartender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDefendKingdom() {
/* 1868 */     return this.defendKingdom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAggWhitie() {
/* 1878 */     return this.aggWhitie;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHerbivore() {
/* 1888 */     return this.herbivore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCarnivore() {
/* 1898 */     return this.carnivore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClimber() {
/* 1908 */     return this.climber;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClimber(boolean aClimber) {
/* 1919 */     this.climber = aClimber;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDominatable() {
/* 1929 */     return this.dominatable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCaveDweller() {
/* 1939 */     return this.caveDweller;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEggLayer() {
/* 1949 */     return this.eggLayer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEggLayer(boolean aEggLayer) {
/* 1960 */     this.eggLayer = aEggLayer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getEggTemplateId() {
/* 1970 */     return this.eggTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEggTemplateId(int aEggTemplateId) {
/* 1981 */     this.eggTemplateId = aEggTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getChildTemplateId() {
/* 1991 */     return this.childTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChildTemplateId(int aChildTemplateId) {
/* 2002 */     this.childTemplateId = aChildTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getCombatMoves() {
/* 2012 */     return this.combatMoves;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCombatMoves(int[] aCombatMoves) {
/* 2023 */     this.combatMoves = aCombatMoves;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFloating() {
/* 2033 */     return this.isFloating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBreakFence() {
/* 2043 */     return this.isBreakFence;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAdultFemaleTemplateId() {
/* 2053 */     return this.adultFemaleTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAdultFemaleTemplateId(int aAdultFemaleTemplateId) {
/* 2064 */     this.adultFemaleTemplateId = aAdultFemaleTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAdultMaleTemplateId() {
/* 2074 */     return this.adultMaleTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAdultMaleTemplateId(int aAdultMaleTemplateId) {
/* 2085 */     this.adultMaleTemplateId = aAdultMaleTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getBaseCombatRating() {
/* 2095 */     return this.baseCombatRating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaseCombatRating(float aBaseCombatRating) {
/* 2106 */     this.baseCombatRating = aBaseCombatRating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColorRed() {
/* 2116 */     return this.colorRed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColorRed(int aColorRed) {
/* 2127 */     this.colorRed = aColorRed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColorGreen() {
/* 2137 */     return this.colorGreen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColorGreen(int aColorGreen) {
/* 2148 */     this.colorGreen = aColorGreen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColorBlue() {
/* 2158 */     return this.colorBlue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColorBlue(int aColorBlue) {
/* 2169 */     this.colorBlue = aColorBlue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getModelColourName(CreatureStatus status) {
/* 2175 */     String colourString = "grey";
/*      */     
/* 2177 */     if (this.isHorse || this.isColoured) {
/*      */       
/* 2179 */       int id = getColourCode(status);
/* 2180 */       if (id >= this.maxColourCount) {
/* 2181 */         id = 0;
/*      */       }
/* 2183 */       colourString = this.colourNameOverrides[id].replaceAll(" ", "");
/*      */     }
/* 2185 */     else if (this.isBlackOrWhite) {
/*      */       
/* 2187 */       if (status.isTraitBitSet(15) || status.isTraitBitSet(16) || status.isTraitBitSet(18) || status
/* 2188 */         .isTraitBitSet(24) || status.isTraitBitSet(25) || status.isTraitBitSet(23)) {
/*      */         
/* 2190 */         colourString = "white";
/*      */       }
/* 2192 */       else if (status.isTraitBitSet(17)) {
/* 2193 */         colourString = "black";
/*      */       } 
/*      */     } 
/* 2196 */     return colourString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getColourName(CreatureStatus status) {
/* 2207 */     String colourString = this.colourNameOverrides[0];
/*      */     
/* 2209 */     if (this.isHorse || this.isColoured) {
/*      */       
/* 2211 */       int id = getColourCode(status);
/* 2212 */       if (id >= this.maxColourCount) {
/* 2213 */         id = 0;
/*      */       }
/* 2215 */       colourString = this.colourNameOverrides[id];
/*      */     }
/* 2217 */     else if (this.isBlackOrWhite) {
/*      */       
/* 2219 */       if (status.isTraitBitSet(15) || status.isTraitBitSet(16) || status.isTraitBitSet(18) || status
/* 2220 */         .isTraitBitSet(24) || status.isTraitBitSet(25) || status.isTraitBitSet(23) || status
/* 2221 */         .isTraitBitSet(30) || status.isTraitBitSet(31) || status.isTraitBitSet(32) || status
/* 2222 */         .isTraitBitSet(33) || status.isTraitBitSet(34)) {
/*      */         
/* 2224 */         colourString = this.colourNameOverrides[4];
/*      */       }
/* 2226 */       else if (status.isTraitBitSet(17)) {
/* 2227 */         colourString = this.colourNameOverrides[3];
/*      */       } 
/*      */     } 
/* 2230 */     return colourString;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getColourCode(CreatureStatus status) {
/* 2235 */     if (this.isHorse || this.isColoured) {
/*      */       
/* 2237 */       if (status.isTraitBitSet(15))
/* 2238 */         return 1; 
/* 2239 */       if (status.isTraitBitSet(16))
/* 2240 */         return 2; 
/* 2241 */       if (status.isTraitBitSet(17))
/* 2242 */         return 3; 
/* 2243 */       if (status.isTraitBitSet(18))
/* 2244 */         return 4; 
/* 2245 */       if (status.isTraitBitSet(24))
/* 2246 */         return 5; 
/* 2247 */       if (status.isTraitBitSet(25))
/* 2248 */         return 6; 
/* 2249 */       if (status.isTraitBitSet(23))
/* 2250 */         return 7; 
/* 2251 */       if (status.isTraitBitSet(30))
/* 2252 */         return 8; 
/* 2253 */       if (status.isTraitBitSet(31))
/* 2254 */         return 9; 
/* 2255 */       if (status.isTraitBitSet(32))
/* 2256 */         return 10; 
/* 2257 */       if (status.isTraitBitSet(33))
/* 2258 */         return 11; 
/* 2259 */       if (status.isTraitBitSet(34)) {
/* 2260 */         return 12;
/*      */       }
/* 2262 */     } else if (this.isBlackOrWhite) {
/*      */       
/* 2264 */       if (status.isTraitBitSet(15) || status.isTraitBitSet(16) || status.isTraitBitSet(18) || status
/* 2265 */         .isTraitBitSet(24) || status.isTraitBitSet(25) || status.isTraitBitSet(23) || status
/* 2266 */         .isTraitBitSet(30) || status.isTraitBitSet(31) || status.isTraitBitSet(32) || status
/* 2267 */         .isTraitBitSet(33) || status.isTraitBitSet(34))
/*      */       {
/* 2269 */         return 4;
/*      */       }
/* 2271 */       if (status.isTraitBitSet(17))
/* 2272 */         return 3; 
/*      */     } 
/* 2274 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTemplateColourName(int trait) {
/* 2284 */     int index = 0;
/*      */     
/* 2286 */     if (trait == 15) {
/* 2287 */       index = 1;
/* 2288 */     } else if (trait == 16) {
/* 2289 */       index = 2;
/* 2290 */     } else if (trait == 17) {
/* 2291 */       index = 3;
/* 2292 */     } else if (trait == 18) {
/* 2293 */       index = 4;
/* 2294 */     } else if (trait == 24) {
/* 2295 */       index = 5;
/* 2296 */     } else if (trait == 25) {
/* 2297 */       index = 6;
/* 2298 */     } else if (trait == 23) {
/* 2299 */       index = 7;
/* 2300 */     } else if (trait == 30) {
/* 2301 */       index = 8;
/* 2302 */     } else if (trait == 31) {
/* 2303 */       index = 9;
/* 2304 */     } else if (trait == 32) {
/* 2305 */       index = 10;
/* 2306 */     } else if (trait == 33) {
/* 2307 */       index = 11;
/* 2308 */     } else if (trait == 34) {
/* 2309 */       index = 12;
/*      */     } 
/* 2311 */     return this.colourNameOverrides[index];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColourNames(String[] colours) {
/*      */     int x;
/* 2322 */     for (x = 0; x < colours.length && x < 13; x++)
/* 2323 */       this.colourNameOverrides[x] = colours[x]; 
/* 2324 */     for (x = colours.length; x < this.colourNameOverrides.length; x++) {
/* 2325 */       this.colourNameOverrides[x] = "unused";
/*      */     }
/* 2327 */     this.maxColourCount = colours.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGlowing() {
/* 2337 */     return this.glowing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGlowing(boolean aGlowing) {
/* 2348 */     this.glowing = aGlowing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getFireRadius() {
/* 2358 */     return this.fireRadius;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFireRadius(byte aFireRadius) {
/* 2369 */     this.fireRadius = aFireRadius;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDetectInvis() {
/* 2379 */     return this.isDetectInvis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDetectInvis(boolean aIsDetectInvis) {
/* 2390 */     this.isDetectInvis = aIsDetectInvis;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getDaysOfPregnancy() {
/* 2400 */     return this.daysOfPregnancy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDaysOfPregnancy(byte aDaysOfPregnancy) {
/* 2411 */     this.daysOfPregnancy = aDaysOfPregnancy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArmourType(ArmourTemplate.ArmourType aArmourType) {
/* 2422 */     this.armourType = aArmourType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCombatDamageType(byte aCombatDamageType) {
/* 2433 */     this.combatDamageType = aCombatDamageType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKingdomGuard() {
/* 2443 */     return this.kingdomGuard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpiritGuard() {
/* 2453 */     return this.spiritGuard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOmnivore() {
/* 2463 */     return this.omnivore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUndead() {
/* 2473 */     return this.undead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoSkillgain() {
/* 2483 */     return this.isNoSkillgain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoSkillgain(boolean aIsNoSkillgain) {
/* 2494 */     this.isNoSkillgain = aIsNoSkillgain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSubmerged() {
/* 2504 */     return this.isSubmerged;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMateTemplateId() {
/* 2514 */     return this.mateTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMateTemplateId(int aMateTemplateId) {
/* 2525 */     this.mateTemplateId = aMateTemplateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKeepSex() {
/* 2535 */     return this.keepSex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepSex(boolean aKeepSex) {
/* 2546 */     this.keepSex = aKeepSex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxGroupAttackSize() {
/* 2556 */     return this.maxGroupAttackSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxGroupAttackSize(int aMaxGroupAttackSize) {
/* 2567 */     this.maxGroupAttackSize = aMaxGroupAttackSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTutorial() {
/* 2577 */     return this.tutorial;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTutorial(boolean aTutorial) {
/* 2588 */     this.tutorial = aTutorial;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxAge() {
/* 2598 */     return this.maxAge;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxAge(int aMaxAge) {
/* 2609 */     this.maxAge = aMaxAge;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPaintMode() {
/* 2619 */     return this.paintMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaintMode(int aPaintMode) {
/* 2630 */     this.paintMode = aPaintMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeModX() {
/* 2640 */     return this.sizeModX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSizeModX(int aSizeModX) {
/* 2651 */     this.sizeModX = aSizeModX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeModY() {
/* 2661 */     return this.sizeModY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSizeModY(int aSizeModY) {
/* 2672 */     this.sizeModY = aSizeModY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeModZ() {
/* 2682 */     return this.sizeModZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSizeModZ(int aSizeModZ) {
/* 2693 */     this.sizeModZ = aSizeModZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOnFire() {
/* 2703 */     return this.isOnFire;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOnFire(boolean aIsOnFire) {
/* 2714 */     this.isOnFire = aIsOnFire;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNonNewbie() {
/* 2724 */     return this.nonNewbie;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNonNewbie(boolean aNonNewbie) {
/* 2735 */     this.nonNewbie = aNonNewbie;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVehicle() {
/* 2745 */     return this.isVehicle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVehicle(boolean aIsVehicle) {
/* 2756 */     this.isVehicle = aIsVehicle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReputation() {
/* 2766 */     return 100;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxHuntDistance() {
/* 2776 */     return this.maxHuntDistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFireResistance() {
/* 2781 */     return this.fireResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getColdResistance() {
/* 2786 */     return this.coldResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getDiseaseResistance() {
/* 2791 */     return this.diseaseResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPhysicalResistance() {
/* 2796 */     return this.physicalResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPierceResistance() {
/* 2801 */     return this.pierceResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSlashResistance() {
/* 2806 */     return this.slashResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCrushResistance() {
/* 2811 */     return this.crushResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBiteResistance() {
/* 2816 */     return this.biteResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPoisonResistance() {
/* 2821 */     return this.poisonResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getWaterResistance() {
/* 2826 */     return this.waterResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAcidResistance() {
/* 2831 */     return this.acidResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getInternalResistance() {
/* 2836 */     return this.internalResistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFireVulnerability() {
/* 2841 */     return this.fireVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getColdVulnerability() {
/* 2846 */     return this.coldVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getDiseaseVulnerability() {
/* 2851 */     return this.diseaseVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPhysicalVulnerability() {
/* 2856 */     return this.physicalVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPierceVulnerability() {
/* 2861 */     return this.pierceVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSlashVulnerability() {
/* 2866 */     return this.slashVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCrushVulnerability() {
/* 2871 */     return this.crushVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBiteVulnerability() {
/* 2876 */     return this.biteVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getPoisonVulnerability() {
/* 2881 */     return this.poisonVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getWaterVulnerability() {
/* 2886 */     return this.waterVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAcidVulnerability() {
/* 2891 */     return this.acidVulnerability;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getInternalVulnerability() {
/* 2896 */     return this.internalVulnerability;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String toString() {
/* 2905 */     return "CreatureTemplate [id: " + this.id + ", name: " + this.name + ", modelName: " + this.modelName + ']';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTowerBasher() {
/* 2915 */     return this.towerBasher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTowerBasher(boolean aTowerBasher) {
/* 2926 */     this.towerBasher = aTowerBasher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getBonusCombatRating() {
/* 2936 */     return this.bonusCombatRating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBonusCombatRating(float aBonusCombatRating) {
/* 2947 */     this.bonusCombatRating = aBonusCombatRating;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean noServerSounds() {
/* 2957 */     return this.noServerSounds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoServerSounds(boolean onServerSounds) {
/* 2968 */     this.noServerSounds = onServerSounds;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getWeight() {
/* 2973 */     return (this.centimetersHigh * this.centimetersLong * this.centimetersWide) / 1.4F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWarGuard() {
/* 2983 */     return this.isWarGuard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWarGuard(boolean isWarGuard) {
/* 2994 */     this.isWarGuard = isWarGuard;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CreatureAI getCreatureAI() {
/* 3005 */     return this.creatureAI;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreatureAI(CreatureAI creatureAI) {
/* 3016 */     this.creatureAI = creatureAI;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRiftCreature() {
/* 3021 */     return this.riftCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRiftCreature(boolean riftCreature) {
/* 3026 */     this.riftCreature = riftCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isStealth() {
/* 3031 */     return this.isStealth;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStealth(boolean isStealth) {
/* 3036 */     this.isStealth = isStealth;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCaster() {
/* 3041 */     return this.isCaster;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCaster(boolean isCaster) {
/* 3046 */     this.isCaster = isCaster;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSummoner() {
/* 3051 */     return this.isSummoner;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSummoner(boolean isSummoner) {
/* 3056 */     this.isSummoner = isSummoner;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEpicMissionSlayable() {
/* 3061 */     return this.isEpicSlayable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEpicMissionTraitor() {
/* 3066 */     return this.isEpicTraitor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMissionDisabled() {
/* 3075 */     return this.isMissionDisabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isNotRebirthable() {
/* 3080 */     return this.isNotRebirthable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBabyCreature() {
/* 3085 */     return this.isBabyCreature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getMeatMaterial() {
/* 3095 */     return this.meatMaterial;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addLootPool(LootPool... pool) {
/* 3100 */     if (this.lootTable == null)
/* 3101 */       this.lootTable = new LootTable(); 
/* 3102 */     this.lootTable.addLootPools(pool);
/*      */   }
/*      */ 
/*      */   
/*      */   public Optional<LootTable> getLootTable() {
/* 3107 */     return Optional.ofNullable(this.lootTable);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */