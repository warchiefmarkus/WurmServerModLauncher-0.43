/*      */ package com.wurmonline.server.villages;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.players.Permissions;
/*      */ import java.io.IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class VillageRole
/*      */   implements VillageStatus, MiscConstants
/*      */ {
/*      */   public int id;
/*      */   byte status;
/*      */   int villageid;
/*   36 */   public String name = "";
/*      */   boolean mayTerraform = false;
/*      */   boolean mayCuttrees = false;
/*      */   boolean mayMine = false;
/*      */   boolean mayFarm = false;
/*      */   boolean mayBuild = false;
/*      */   boolean mayHire = false;
/*      */   boolean mayInvite = false;
/*      */   boolean mayDestroy = false;
/*      */   boolean mayManageRoles = false;
/*      */   boolean mayExpand = false;
/*      */   boolean mayLockFences = false;
/*      */   boolean mayPassAllFences = false;
/*      */   boolean diplomat = false;
/*      */   boolean mayAttackCitizens = false;
/*   51 */   boolean mayAttackNonCitizens = Servers.localServer.PVPSERVER;
/*      */   boolean mayFish = true;
/*      */   boolean mayCutOldTrees = false;
/*      */   boolean mayPushPullTurn = true;
/*      */   boolean mayUpdateMap = false;
/*      */   boolean mayLead = false;
/*      */   boolean mayPickup = false;
/*      */   boolean mayTame = false;
/*      */   boolean mayLoad = false;
/*      */   boolean mayButcher = false;
/*      */   boolean mayAttachLock = false;
/*      */   boolean mayPickLocks = false;
/*   63 */   int villageAppliedTo = 0;
/*   64 */   long playerAppliedTo = -10L;
/*      */   
/*   66 */   Permissions settings = new Permissions();
/*   67 */   Permissions moreSettings = new Permissions();
/*   68 */   Permissions extraSettings = new Permissions();
/*      */   
/*      */   public enum RolePermissions
/*      */     implements Permissions.IPermission
/*      */   {
/*   73 */     BUTCHER(0, "Animals", "Butcher"),
/*   74 */     GROOM(1, "Animals", "Groom"),
/*   75 */     LEAD(2, "Animals", "Lead"),
/*   76 */     MILK_SHEAR(3, "Animals", "Milk and Shear"),
/*   77 */     SACRIFICE(4, "Animals", "Sacrifice"),
/*   78 */     TAME(5, "Animals", "Tame"),
/*      */     
/*   80 */     BUILD(6, "Construction", "Build"),
/*   81 */     DESTROY_FENCE(7, "Construction", "Destroy Fence"),
/*   82 */     DESTROY_ITEMS(8, "Construction", "Destroy Items"),
/*   83 */     PICK_LOCKS(9, "Construction", "Pick Locks"),
/*   84 */     PLAN_BUILDINGS(10, "Construction", "Plan Buildings"),
/*      */     
/*   86 */     CULTIVATE(11, "Digging", "Cultivate"),
/*   87 */     DIG_RESOURCE(12, "Digging", "Dig Resource"),
/*   88 */     TERRAFORM(13, "Digging", "Terraform"),
/*      */     
/*   90 */     HARVEST_FIELDS(14, "Farming", "Harvest Fields"),
/*   91 */     SOW_FIELDS(15, "Farming", "Sow Fields"),
/*   92 */     TEND_FIELDS(16, "Farming", "Tend Fields"),
/*      */     
/*   94 */     CHOP_DOWN_ALL_TREES(17, "Forestry/Gardening", "Chop Down All Trees"),
/*   95 */     CHOP_DOWN_OLD_TREES(18, "Forestry/Gardening", "Chop Down Old Trees"),
/*   96 */     CUT_GRASS(19, "Forestry/Gardening", "Cut Grass"),
/*   97 */     HARVEST_FRUIT(20, "Forestry/Gardening", "Harvest Fruit"),
/*   98 */     MAKE_LAWN(21, "Forestry/Gardening", "Make Lawn"),
/*   99 */     PICK_SPROUTS(22, "Forestry/Gardening", "Pick Sprouts"),
/*  100 */     PLANT_FLOWERS(23, "Forestry/Gardening", "Plant Flowers"),
/*  101 */     PLANT_SPROUTS(24, "Forestry/Gardening", "Plant Sprouts"),
/*  102 */     PRUNE(25, "Prune", "Prune"),
/*      */     
/*  104 */     ATTACK_CITIZENS(26, "General", "Attack Citizens"),
/*  105 */     ATTACK_NON_CITIZENS(27, "General", "Attack Non Citizens"),
/*  106 */     CAST_DEITY_SPELLS(28, "General", "Cast Deity Spells"),
/*  107 */     CAST_SORCERY_SPELLS(29, "General", "Cast Sorcery Spells"),
/*  108 */     FORAGE(30, "General", "Forage/Botanize"),
/*  109 */     PLACE_MERCHANTS(31, "General", "May Place Merchants");
/*      */ 
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
/*      */     RolePermissions(int aBit, String category, String aDescription) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = category;
/*      */       this.header2 = "";
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
/*      */     public byte getBit() {
/*      */       return this.bit;
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
/*  193 */     private static final Permissions.Allow[] types = Permissions.Allow.values(); public int getValue() {
/*      */       return 1 << this.bit;
/*      */     } public String getDescription() {
/*      */       return this.description;
/*      */     } public static Permissions.IPermission[] getPermissions() {
/*  198 */       return (Permissions.IPermission[])types; } public String getHeader1() { return this.header1; } public String getHeader2() {
/*      */       return this.header2;
/*      */     } public String getHover() {
/*      */       return "";
/*      */     } static {
/*      */     
/*      */     } }
/*  205 */   public enum MoreRolePermissions implements Permissions.IPermission { MEDITATION_ABILITY(0, "General", "Use Meditation Ability"),
/*      */     
/*  207 */     ATTACH_LOCKS(1, "Item Management", "Attach Locks"),
/*  208 */     IMPROVE_REPAIR(2, "Item Management", "Improve / Repair"),
/*  209 */     LOAD(3, "Item Management", "Load"),
/*  210 */     PICKUP(4, "Item Management", "Pickup"),
/*  211 */     PICKUP_PLANTED(5, "Item Management", "Pickup Planted Items"),
/*  212 */     PULL_PUSH(6, "Item Management", "Pull/Push/Turn"),
/*      */     
/*  214 */     MINE_FLOOR(7, "Mining", "Mine Floor"),
/*  215 */     MINE_IRON(8, "Mining", "Mine Iron Vein"),
/*  216 */     MINE_OTHER(9, "Mining", "Mine Other Vein"),
/*  217 */     MINE_ROCK(10, "Mining", "Mine Rock"),
/*  218 */     SURFACE_MINING(11, "Mining", "Surface Mining"),
/*  219 */     TUNNEL(12, "Mining", "Tunnelling"),
/*      */     
/*  221 */     ALLOW_ACTIONS_ON_ALLIED_DEED(13, "Settlement Management", "Allow Actions on Allied Deeds"),
/*  222 */     DIPLOMAT(14, "Settlement Management", "Diplomat"),
/*  223 */     DESTROY_ANY_BUILDING(15, "Construction", "Destroy Any Building"),
/*  224 */     INVITE_CITIZENS(16, "Settlement Management", "Invite Citizens"),
/*  225 */     MANAGE_CITIZEN_ROLES(17, "Settlement Management", "Manage Citizen Roles"),
/*  226 */     MANAGE_GUARDS(18, "Settlement Management", "Manage Guards"),
/*  227 */     MANAGE_MAP(19, "Settlement Management", "Manage Map"),
/*  228 */     MANAGE_REPUTATIONS(20, "Settlement Management", "Manage Reputations"),
/*  229 */     MANAGE_ROLES(21, "Settlement Management", "Manage Roles"),
/*  230 */     MANAGE_SETTINGS(22, "Settlement Management", "Manage Settings"),
/*  231 */     MAY_CONFIGURE_TWITTER(23, "Settlement Management", "May Configure Twitter"),
/*  232 */     RESIZE(24, "Settlement Management", "Resize"),
/*  233 */     MANAGE_ALLOWED_OBJECTS(25, "Settlement Management", "Manage Allowed Objects"),
/*  234 */     REINFORCE(26, "Mining", "Add/Remove Reinforcements"),
/*  235 */     BREED(27, "Animals", "Breed"),
/*  236 */     PACK(28, "Digging", "Pack"),
/*  237 */     PAVE(29, "General", "Drop"),
/*  238 */     DROP(30, "Item Management", "Drop"),
/*  239 */     UNLOAD(31, "Item Management", "Unload");
/*      */ 
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
/*      */     MoreRolePermissions(int aBit, String category, String aDescription) {
/*      */       this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = category;
/*      */       this.header2 = "";
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
/*      */     public byte getBit() {
/*      */       return this.bit;
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
/*  323 */     private static final Permissions.Allow[] types = Permissions.Allow.values();
/*      */     public int getValue() { return 1 << this.bit; }
/*      */     public String getDescription() { return this.description; }
/*      */     public String getHeader1() { return this.header1; }
/*      */     public String getHeader2() { return this.header2; } public static Permissions.IPermission[] getPermissions() {
/*  328 */       return (Permissions.IPermission[])types;
/*      */     } public String getHover() {
/*      */       return "";
/*      */     } static {
/*      */     
/*      */     } }
/*  334 */   public enum ExtraRolePermissions implements Permissions.IPermission { BRAND(0, "Animals", "Brand"),
/*  335 */     PASS_GATES(1, "General", "Pass Gates"),
/*  336 */     PLANT_ITEM(2, "Item Management", "Plant Items"),
/*  337 */     SPARE03(3, "Unknown", "Spare");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  421 */     private static final Permissions.Allow[] types = Permissions.Allow.values(); ExtraRolePermissions(int aBit, String category, String aDescription) { this.bit = (byte)aBit;
/*      */       this.description = aDescription;
/*      */       this.header1 = category;
/*      */       this.header2 = ""; }
/*      */     public byte getBit() { return this.bit; }
/*  426 */     public static Permissions.IPermission[] getPermissions() { return (Permissions.IPermission[])types; }
/*      */     
/*      */     public int getValue() {
/*      */       return 1 << this.bit;
/*      */     }
/*      */     public String getDescription() {
/*      */       return this.description;
/*      */     }
/*      */     public String getHeader1() {
/*      */       return this.header1;
/*      */     }
/*      */     public String getHeader2() {
/*      */       return this.header2;
/*      */     }
/*      */     public String getHover() {
/*      */       return "";
/*      */     }
/*      */     static {
/*      */     
/*      */     } }
/*      */   
/*      */   VillageRole(int aVillageid, String aName, boolean aTerraform, boolean aCutTrees, boolean aMine, boolean aFarm, boolean aBuild, boolean aHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayLockFences, boolean aMayPassFences, boolean aIsDiplomat, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, byte aStatus, int appliedToVillage, boolean aMayPushPullTurn, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long appliedToPlayer, int aSettings, int aMoreSettings, int aExtraSettings) throws IOException {
/*  448 */     this.villageid = aVillageid;
/*  449 */     this.name = aName;
/*  450 */     this.villageAppliedTo = appliedToVillage;
/*  451 */     this.status = aStatus;
/*      */     
/*  453 */     this.mayTerraform = aTerraform;
/*  454 */     this.mayCuttrees = aCutTrees;
/*  455 */     this.mayMine = aMine;
/*  456 */     this.mayFarm = aFarm;
/*  457 */     this.mayBuild = aBuild;
/*  458 */     this.mayHire = aHire;
/*  459 */     this.mayInvite = aMayInvite;
/*  460 */     this.mayDestroy = aMayDestroy;
/*  461 */     this.mayManageRoles = aMayManageRoles;
/*  462 */     this.mayExpand = aMayExpand;
/*  463 */     this.mayLockFences = aMayLockFences;
/*  464 */     this.mayPassAllFences = aMayPassFences;
/*  465 */     this.diplomat = aIsDiplomat;
/*  466 */     this.mayAttackCitizens = aMayAttackCitizens;
/*  467 */     this.mayAttackNonCitizens = aMayAttackNonCitizens;
/*  468 */     this.mayFish = aMayFish;
/*  469 */     this.mayCutOldTrees = aMayCutOldTrees;
/*  470 */     this.mayPushPullTurn = aMayPushPullTurn;
/*  471 */     this.mayUpdateMap = aMayUpdateMap;
/*  472 */     this.mayLead = aMayLead;
/*  473 */     this.mayPickup = aMayPickup;
/*  474 */     this.mayTame = aMayTame;
/*  475 */     this.mayLoad = aMayLoad;
/*  476 */     this.mayButcher = aMayButcher;
/*  477 */     this.mayAttachLock = aMayAttachLock;
/*  478 */     this.mayPickLocks = aMayPickLocks;
/*      */     
/*  480 */     this.playerAppliedTo = appliedToPlayer;
/*  481 */     this.settings.setPermissionBits(aSettings);
/*  482 */     this.moreSettings.setPermissionBits(aMoreSettings);
/*  483 */     this.extraSettings.setPermissionBits(aExtraSettings);
/*      */     
/*  485 */     create();
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
/*      */   VillageRole(int aId, int aVillageid, String aRoleName, boolean aMayTerraform, boolean aMayCuttrees, boolean aMayMine, boolean aMayFarm, boolean aMayBuild, boolean aMayHire, boolean aMayInvite, boolean aMayDestroy, boolean aMayManageRoles, boolean aMayExpand, boolean aMayPassAllFences, boolean aMayLockFences, boolean aMayAttackCitizens, boolean aMayAttackNonCitizens, boolean aMayFish, boolean aMayCutOldTrees, boolean aMayPushPullTurn, boolean aDiplomat, byte aStatus, int aVillageAppliedTo, boolean aMayUpdateMap, boolean aMayLead, boolean aMayPickup, boolean aMayTame, boolean aMayLoad, boolean aMayButcher, boolean aMayAttachLock, boolean aMayPickLocks, long aPlayerAppliedTo, int aSettings, int aMoreSettings, int aExtraSettings) {
/*  503 */     this.id = aId;
/*  504 */     this.villageid = aVillageid;
/*  505 */     this.name = aRoleName;
/*  506 */     this.mayTerraform = aMayTerraform;
/*  507 */     this.mayCuttrees = aMayCuttrees;
/*  508 */     this.mayMine = aMayMine;
/*  509 */     this.mayFarm = aMayFarm;
/*  510 */     this.mayBuild = aMayBuild;
/*  511 */     this.mayHire = aMayHire;
/*  512 */     this.mayInvite = aMayInvite;
/*  513 */     this.mayDestroy = aMayDestroy;
/*  514 */     this.mayManageRoles = aMayManageRoles;
/*  515 */     this.mayExpand = aMayExpand;
/*  516 */     this.mayPassAllFences = aMayPassAllFences;
/*  517 */     this.mayLockFences = aMayLockFences;
/*  518 */     this.mayAttackCitizens = aMayAttackCitizens;
/*  519 */     this.mayAttackNonCitizens = aMayAttackNonCitizens;
/*  520 */     this.mayFish = aMayFish;
/*  521 */     this.mayCutOldTrees = aMayCutOldTrees;
/*  522 */     this.mayPushPullTurn = aMayPushPullTurn;
/*  523 */     this.diplomat = aDiplomat;
/*  524 */     this.status = aStatus;
/*  525 */     this.villageAppliedTo = aVillageAppliedTo;
/*  526 */     this.mayUpdateMap = aMayUpdateMap;
/*  527 */     this.mayLead = aMayLead;
/*  528 */     this.mayPickup = aMayPickup;
/*  529 */     this.mayTame = aMayTame;
/*  530 */     this.mayLoad = aMayLoad;
/*  531 */     this.mayButcher = aMayButcher;
/*  532 */     this.mayAttachLock = aMayAttachLock;
/*  533 */     this.playerAppliedTo = aPlayerAppliedTo;
/*  534 */     if (getStatus() == 2) {
/*      */ 
/*      */       
/*  537 */       this.settings.setPermissionBits(-1);
/*  538 */       this.moreSettings.setPermissionBits(-1);
/*  539 */       this.extraSettings.setPermissionBits(-1);
/*      */     }
/*      */     else {
/*      */       
/*  543 */       this.settings.setPermissionBits(aSettings);
/*  544 */       this.moreSettings.setPermissionBits(aMoreSettings);
/*  545 */       this.extraSettings.setPermissionBits(aExtraSettings);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void convertSettings() {
/*  551 */     if (this.status == 2) {
/*      */ 
/*      */       
/*  554 */       this.settings.setPermissionBits(-1);
/*  555 */       this.moreSettings.setPermissionBits(-1);
/*  556 */       this.extraSettings.setPermissionBits(-1);
/*      */     }
/*      */     else {
/*      */       
/*  560 */       boolean isMayor = (this.status == 2);
/*  561 */       boolean isAnyone = (isMayor || this.status == 3 || this.status == 0 || this.status == 5);
/*      */ 
/*      */       
/*  564 */       boolean isCitizen = ((isMayor || this.status == 3 || this.status == 0) && this.villageAppliedTo == 0);
/*      */       
/*  566 */       boolean mayPlaceMerchants = isCitizen;
/*      */ 
/*      */       
/*      */       try {
/*  570 */         Village village = Villages.getVillage(this.villageid);
/*  571 */         mayPlaceMerchants = village.acceptsMerchants;
/*      */       }
/*  573 */       catch (NoSuchVillageException noSuchVillageException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  580 */       setCanBreed((isMayor || this.mayLead));
/*  581 */       setCanButcher((isMayor || this.mayButcher));
/*  582 */       setCanGroom((isMayor || this.mayLead));
/*  583 */       setCanLead((isMayor || this.mayLead));
/*  584 */       setCanMilkShear((isMayor || this.mayFarm));
/*  585 */       setCanSacrifice((isMayor || this.mayButcher));
/*  586 */       setCanTame((isMayor || this.mayTame));
/*      */       
/*  588 */       setCanBuild((isMayor || this.mayBuild));
/*  589 */       setCanDestroyFence((isMayor || isAnyone));
/*  590 */       setCanDestroyItems((isMayor || isAnyone));
/*  591 */       setCanPickLocks((isMayor || this.mayPickLocks));
/*  592 */       setCanPlanBuildings((isMayor || this.mayBuild));
/*      */       
/*  594 */       setCanCultivate((isMayor || this.mayTerraform));
/*  595 */       setCanDigResource((isMayor || this.mayTerraform));
/*  596 */       setCanPack((isMayor || this.mayTerraform));
/*  597 */       setCanTerraform((isMayor || this.mayTerraform));
/*      */       
/*  599 */       setCanHarvestFields((isMayor || this.mayFarm));
/*  600 */       setCanSowFields((isMayor || this.mayFarm));
/*  601 */       setCanTendFields((isMayor || this.mayFarm));
/*      */       
/*  603 */       setCanChopDownAllTrees((isMayor || this.mayCuttrees));
/*  604 */       setCanChopDownOldTrees((isMayor || this.mayCutOldTrees));
/*  605 */       setCanCutGrass((isMayor || isAnyone));
/*  606 */       setCanHarvestFruit((isMayor || this.mayCuttrees));
/*  607 */       setCanMakeLawn((isMayor || this.mayTerraform));
/*  608 */       setCanPickSprouts((isMayor || this.mayCuttrees));
/*  609 */       setCanPlantFlowers((isMayor || this.mayCuttrees));
/*  610 */       setCanPlantSprouts((isMayor || this.mayCuttrees));
/*  611 */       setCanPrune((isMayor || this.mayCuttrees));
/*      */       
/*  613 */       setCanAttackCitizens((isMayor || this.mayAttackCitizens));
/*  614 */       setCanAttackNonCitizens((isMayor || this.mayAttackNonCitizens));
/*  615 */       setCanCastDeitySpells((isMayor || isAnyone));
/*  616 */       setCanCastSorcerySpells((isMayor || isAnyone));
/*  617 */       setCanForageBotanize((isMayor || this.mayFarm));
/*  618 */       setCanPave((isMayor || this.mayTerraform));
/*  619 */       setCanPlaceMerchants((isMayor || mayPlaceMerchants));
/*  620 */       setCanUseMeditationAbility((isMayor || isAnyone));
/*      */       
/*  622 */       setCanAttachLocks((isMayor || this.mayAttachLock));
/*  623 */       setCanDrop((isMayor || isAnyone));
/*  624 */       setCanImproveRepair((isMayor || isAnyone));
/*  625 */       setCanLoad((isMayor || this.mayLoad));
/*  626 */       setCanPickup((isMayor || this.mayPickup));
/*  627 */       setCanPickupPlanted((isMayor || this.mayPickup));
/*  628 */       setCanPullPushTurn((isMayor || this.mayPushPullTurn));
/*  629 */       setCanUnload((isMayor || this.mayLoad));
/*      */       
/*  631 */       setCanMineFloor((isMayor || this.mayMine));
/*  632 */       setCanMineIron((isMayor || this.mayMine));
/*  633 */       setCanMineOther((isMayor || this.mayMine));
/*  634 */       setCanMineRock((isMayor || this.mayMine));
/*  635 */       setCanSurface((isMayor || this.mayMine));
/*  636 */       setCanTunnel((isMayor || this.mayMine));
/*      */       
/*  638 */       SetCanPerformActionsOnAlliedDeeds(isCitizen);
/*  639 */       setCanDiplomat((isMayor || this.diplomat));
/*  640 */       setCanDestroyAnyBuilding((isMayor || this.mayDestroy));
/*  641 */       setCanManageGuards((isMayor || this.mayHire));
/*  642 */       setCanInviteCitizens((isMayor || this.mayInvite));
/*  643 */       setCanManageCitizenRoles((isMayor || this.mayManageRoles));
/*  644 */       setCanManageMap((isMayor || this.mayUpdateMap));
/*  645 */       setCanManageReputations((isMayor || this.mayManageRoles));
/*  646 */       setCanManageRoles((isMayor || this.mayManageRoles));
/*  647 */       setCanManageSettings((isMayor || this.mayManageRoles));
/*  648 */       setCanConfigureTwitter((isMayor || this.diplomat));
/*  649 */       setCanResizeSettlement((isMayor || this.mayExpand));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  659 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getVillageId() {
/*  664 */     return this.villageid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayAttachLock() {
/*  672 */     return this.moreSettings.hasPermission(MoreRolePermissions.ATTACH_LOCKS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayAttackCitizens() {
/*  680 */     return this.settings.hasPermission(RolePermissions.ATTACK_CITIZENS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayAttackNonCitizens() {
/*  688 */     return this.settings.hasPermission(RolePermissions.ATTACK_NON_CITIZENS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayBrand() {
/*  696 */     return this.extraSettings.hasPermission(ExtraRolePermissions.BRAND.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayBreed() {
/*  704 */     return this.moreSettings.hasPermission(MoreRolePermissions.BREED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayBuild() {
/*  712 */     return this.settings.hasPermission(RolePermissions.BUILD.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayButcher() {
/*  720 */     return this.settings.hasPermission(RolePermissions.BUTCHER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCastDeitySpells() {
/*  728 */     return this.settings.hasPermission(RolePermissions.CAST_DEITY_SPELLS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCastSorcerySpells() {
/*  736 */     return this.settings.hasPermission(RolePermissions.CAST_SORCERY_SPELLS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayChopDownAllTrees() {
/*  744 */     return this.settings.hasPermission(RolePermissions.CHOP_DOWN_ALL_TREES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayChopDownOldTrees() {
/*  752 */     return this.settings.hasPermission(RolePermissions.CHOP_DOWN_OLD_TREES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayConfigureTwitter() {
/*  760 */     return this.moreSettings.hasPermission(MoreRolePermissions.MAY_CONFIGURE_TWITTER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCultivate() {
/*  768 */     return this.settings.hasPermission(RolePermissions.CULTIVATE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCutGrass() {
/*  776 */     return this.settings.hasPermission(RolePermissions.CUT_GRASS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCuttrees() {
/*  784 */     return this.mayCuttrees;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayCutOldTrees() {
/*  792 */     return this.mayCutOldTrees;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDestroy() {
/*  801 */     return this.mayDestroy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDestroyAnyBuilding() {
/*  809 */     return this.moreSettings.hasPermission(MoreRolePermissions.DESTROY_ANY_BUILDING.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDestroyFences() {
/*  817 */     return this.settings.hasPermission(RolePermissions.DESTROY_FENCE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDestroyItems() {
/*  825 */     return this.settings.hasPermission(RolePermissions.DESTROY_ITEMS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDigResources() {
/*  833 */     return this.settings.hasPermission(RolePermissions.DIG_RESOURCE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDiplomat() {
/*  841 */     return this.moreSettings.hasPermission(MoreRolePermissions.DIPLOMAT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDisbandSettlement() {
/*  849 */     return (this.status == 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayDrop() {
/*  857 */     return this.moreSettings.hasPermission(MoreRolePermissions.DROP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayExpand() {
/*  865 */     return this.mayExpand;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayFarm() {
/*  873 */     return this.mayFarm;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayFish() {
/*  881 */     return this.mayFish;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayForageAndBotanize() {
/*  889 */     return this.settings.hasPermission(RolePermissions.FORAGE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayGroom() {
/*  897 */     return this.settings.hasPermission(RolePermissions.GROOM.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayHarvestFields() {
/*  905 */     return this.settings.hasPermission(RolePermissions.HARVEST_FIELDS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayHarvestFruit() {
/*  913 */     return this.settings.hasPermission(RolePermissions.HARVEST_FRUIT.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayHire() {
/*  921 */     return this.mayHire;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayImproveAndRepair() {
/*  929 */     return this.moreSettings.hasPermission(MoreRolePermissions.IMPROVE_REPAIR.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayInviteCitizens() {
/*  937 */     return this.moreSettings.hasPermission(MoreRolePermissions.INVITE_CITIZENS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayLead() {
/*  945 */     return this.settings.hasPermission(RolePermissions.LEAD.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayLoad() {
/*  953 */     return this.moreSettings.hasPermission(MoreRolePermissions.LOAD.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayLockFences() {
/*  961 */     return this.mayLockFences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMakeLawn() {
/*  969 */     return this.settings.hasPermission(RolePermissions.MAKE_LAWN.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageAllowedObjects() {
/*  977 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_ALLOWED_OBJECTS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageCitizenRoles() {
/*  985 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_CITIZEN_ROLES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageGuards() {
/*  993 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_GUARDS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageMap() {
/* 1001 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_MAP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageReputations() {
/* 1009 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_REPUTATIONS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageRoles() {
/* 1017 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_ROLES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayManageSettings() {
/* 1025 */     return this.moreSettings.hasPermission(MoreRolePermissions.MANAGE_SETTINGS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMilkAndShear() {
/* 1033 */     return this.settings.hasPermission(RolePermissions.MILK_SHEAR.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMine() {
/* 1041 */     return this.mayMine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMineFloor() {
/* 1049 */     return this.moreSettings.hasPermission(MoreRolePermissions.MINE_FLOOR.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMineIronVeins() {
/* 1057 */     return this.moreSettings.hasPermission(MoreRolePermissions.MINE_IRON.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMineOtherVeins() {
/* 1065 */     return this.moreSettings.hasPermission(MoreRolePermissions.MINE_OTHER.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMineRock() {
/* 1073 */     return this.moreSettings.hasPermission(MoreRolePermissions.MINE_ROCK.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayMineSurface() {
/* 1081 */     return this.moreSettings.hasPermission(MoreRolePermissions.SURFACE_MINING.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPack() {
/* 1089 */     return this.moreSettings.hasPermission(MoreRolePermissions.PACK.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPassGates() {
/* 1097 */     return this.extraSettings.hasPermission(ExtraRolePermissions.PASS_GATES.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPave() {
/* 1105 */     return this.moreSettings.hasPermission(MoreRolePermissions.PAVE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPerformActionsOnAlliedDeeds() {
/* 1113 */     return this.moreSettings.hasPermission(MoreRolePermissions.ALLOW_ACTIONS_ON_ALLIED_DEED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPickLocks() {
/* 1121 */     return this.settings.hasPermission(RolePermissions.PICK_LOCKS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPickSprouts() {
/* 1129 */     return this.settings.hasPermission(RolePermissions.PICK_SPROUTS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPickup() {
/* 1137 */     return this.moreSettings.hasPermission(MoreRolePermissions.PICKUP.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPickupPlanted() {
/* 1145 */     return this.moreSettings.hasPermission(MoreRolePermissions.PICKUP_PLANTED.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPlaceMerchants() {
/* 1153 */     return this.settings.hasPermission(RolePermissions.PLACE_MERCHANTS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPlanBuildings() {
/* 1161 */     return this.settings.hasPermission(RolePermissions.PLAN_BUILDINGS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPlantFlowers() {
/* 1169 */     return this.settings.hasPermission(RolePermissions.PLANT_FLOWERS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPlantItem() {
/* 1178 */     return this.extraSettings.hasPermission(ExtraRolePermissions.PLANT_ITEM.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPlantSprouts() {
/* 1186 */     return this.settings.hasPermission(RolePermissions.PLANT_SPROUTS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPrune() {
/* 1194 */     return this.settings.hasPermission(RolePermissions.PRUNE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayPushPullTurn() {
/* 1202 */     return this.moreSettings.hasPermission(MoreRolePermissions.PULL_PUSH.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayReinforce() {
/* 1211 */     return this.moreSettings.hasPermission(MoreRolePermissions.REINFORCE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayResizeSettlement() {
/* 1219 */     return this.moreSettings.hasPermission(MoreRolePermissions.RESIZE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean maySacrifice() {
/* 1227 */     return this.settings.hasPermission(RolePermissions.SACRIFICE.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean maySowFields() {
/* 1235 */     return this.settings.hasPermission(RolePermissions.SOW_FIELDS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayTame() {
/* 1243 */     return this.settings.hasPermission(RolePermissions.TAME.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayTendFields() {
/* 1251 */     return this.settings.hasPermission(RolePermissions.TEND_FIELDS.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayTerraform() {
/* 1259 */     return this.settings.hasPermission(RolePermissions.TERRAFORM.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayTunnel() {
/* 1267 */     return this.moreSettings.hasPermission(MoreRolePermissions.TUNNEL.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayUnload() {
/* 1275 */     return this.moreSettings.hasPermission(MoreRolePermissions.UNLOAD.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayUpdateMap() {
/* 1283 */     return this.mayUpdateMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean mayUseMeditationAbilities() {
/* 1291 */     return this.moreSettings.hasPermission(MoreRolePermissions.MEDITATION_ABILITY.getBit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getVillageAppliedTo() {
/* 1299 */     return this.villageAppliedTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getPlayerAppliedTo() {
/* 1307 */     return this.playerAppliedTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getStatus() {
/* 1316 */     return this.status;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getId() {
/* 1325 */     return this.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanBrand(boolean canBrand) {
/* 1330 */     this.extraSettings.setPermissionBit(ExtraRolePermissions.BRAND.getBit(), canBrand);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanBreed(boolean canBreed) {
/* 1335 */     this.moreSettings.setPermissionBit(MoreRolePermissions.BREED.getBit(), canBreed);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanButcher(boolean canButcher) {
/* 1340 */     this.settings.setPermissionBit(RolePermissions.BUTCHER.getBit(), canButcher);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanGroom(boolean canGroom) {
/* 1345 */     this.settings.setPermissionBit(RolePermissions.GROOM.getBit(), canGroom);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanLead(boolean canLead) {
/* 1350 */     this.settings.setPermissionBit(RolePermissions.LEAD.getBit(), canLead);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMilkShear(boolean canMilkShear) {
/* 1355 */     this.settings.setPermissionBit(RolePermissions.MILK_SHEAR.getBit(), canMilkShear);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPassGates(boolean canPassGates) {
/* 1360 */     this.extraSettings.setPermissionBit(ExtraRolePermissions.PASS_GATES.getBit(), canPassGates);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanSacrifice(boolean canSacrifice) {
/* 1365 */     this.settings.setPermissionBit(RolePermissions.SACRIFICE.getBit(), canSacrifice);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanTame(boolean canTame) {
/* 1370 */     this.settings.setPermissionBit(RolePermissions.TAME.getBit(), canTame);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanBuild(boolean canBuild) {
/* 1375 */     this.settings.setPermissionBit(RolePermissions.BUILD.getBit(), canBuild);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDestroyFence(boolean canDestroyFence) {
/* 1380 */     this.settings.setPermissionBit(RolePermissions.DESTROY_FENCE.getBit(), canDestroyFence);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDestroyItems(boolean canDestroyItems) {
/* 1385 */     this.settings.setPermissionBit(RolePermissions.DESTROY_ITEMS.getBit(), canDestroyItems);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPickLocks(boolean canPickLocks) {
/* 1390 */     this.settings.setPermissionBit(RolePermissions.PICK_LOCKS.getBit(), canPickLocks);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPlanBuildings(boolean canPlanBuildings) {
/* 1395 */     this.settings.setPermissionBit(RolePermissions.PLAN_BUILDINGS.getBit(), canPlanBuildings);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanCultivate(boolean canCultivate) {
/* 1400 */     this.settings.setPermissionBit(RolePermissions.CULTIVATE.getBit(), canCultivate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDigResource(boolean canDigResource) {
/* 1405 */     this.settings.setPermissionBit(RolePermissions.DIG_RESOURCE.getBit(), canDigResource);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPack(boolean canPack) {
/* 1410 */     this.moreSettings.setPermissionBit(MoreRolePermissions.PACK.getBit(), canPack);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanTerraform(boolean canTerraform) {
/* 1415 */     this.settings.setPermissionBit(RolePermissions.TERRAFORM.getBit(), canTerraform);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanHarvestFields(boolean canHarvestFields) {
/* 1420 */     this.settings.setPermissionBit(RolePermissions.HARVEST_FIELDS.getBit(), canHarvestFields);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanSowFields(boolean canSowFields) {
/* 1425 */     this.settings.setPermissionBit(RolePermissions.SOW_FIELDS.getBit(), canSowFields);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanTendFields(boolean canTendFields) {
/* 1430 */     this.settings.setPermissionBit(RolePermissions.TEND_FIELDS.getBit(), canTendFields);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanChopDownAllTrees(boolean canChopDownAllTrees) {
/* 1435 */     this.settings.setPermissionBit(RolePermissions.CHOP_DOWN_ALL_TREES.getBit(), canChopDownAllTrees);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanChopDownOldTrees(boolean canChopDownOldTrees) {
/* 1440 */     this.settings.setPermissionBit(RolePermissions.CHOP_DOWN_OLD_TREES.getBit(), canChopDownOldTrees);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanCutGrass(boolean canCutGrass) {
/* 1445 */     this.settings.setPermissionBit(RolePermissions.CUT_GRASS.getBit(), canCutGrass);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanHarvestFruit(boolean canHarvestFruit) {
/* 1450 */     this.settings.setPermissionBit(RolePermissions.HARVEST_FRUIT.getBit(), canHarvestFruit);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMakeLawn(boolean canMakeLawn) {
/* 1455 */     this.settings.setPermissionBit(RolePermissions.MAKE_LAWN.getBit(), canMakeLawn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPickSprouts(boolean canPickSprouts) {
/* 1460 */     this.settings.setPermissionBit(RolePermissions.PICK_SPROUTS.getBit(), canPickSprouts);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPlantFlowers(boolean canPlantFlowers) {
/* 1465 */     this.settings.setPermissionBit(RolePermissions.PLANT_FLOWERS.getBit(), canPlantFlowers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPlantSprouts(boolean canPlantSprouts) {
/* 1470 */     this.settings.setPermissionBit(RolePermissions.PLANT_SPROUTS.getBit(), canPlantSprouts);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPrune(boolean canPrune) {
/* 1475 */     this.settings.setPermissionBit(RolePermissions.PRUNE.getBit(), canPrune);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanAttackCitizens(boolean canAttackCitizens) {
/* 1480 */     this.settings.setPermissionBit(RolePermissions.ATTACK_CITIZENS.getBit(), canAttackCitizens);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanAttackNonCitizens(boolean canAttackNonCitizens) {
/* 1485 */     this.settings.setPermissionBit(RolePermissions.ATTACK_NON_CITIZENS.getBit(), canAttackNonCitizens);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanCastDeitySpells(boolean canCastDeitySpells) {
/* 1490 */     this.settings.setPermissionBit(RolePermissions.CAST_DEITY_SPELLS.getBit(), canCastDeitySpells);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanCastSorcerySpells(boolean canCastSorcerySpells) {
/* 1495 */     this.settings.setPermissionBit(RolePermissions.CAST_SORCERY_SPELLS.getBit(), canCastSorcerySpells);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanForageBotanize(boolean canForageBotanize) {
/* 1500 */     this.settings.setPermissionBit(RolePermissions.FORAGE.getBit(), canForageBotanize);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPlaceMerchants(boolean canPlaceMerchants) {
/* 1505 */     this.settings.setPermissionBit(RolePermissions.PLACE_MERCHANTS.getBit(), canPlaceMerchants);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPave(boolean canPave) {
/* 1510 */     this.moreSettings.setPermissionBit(MoreRolePermissions.PAVE.getBit(), canPave);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanUseMeditationAbility(boolean canUseMeditationAbility) {
/* 1515 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MEDITATION_ABILITY.getBit(), canUseMeditationAbility);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanAttachLocks(boolean canAttachLocks) {
/* 1520 */     this.moreSettings.setPermissionBit(MoreRolePermissions.ATTACH_LOCKS.getBit(), canAttachLocks);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDrop(boolean canDrop) {
/* 1525 */     this.moreSettings.setPermissionBit(MoreRolePermissions.DROP.getBit(), canDrop);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanImproveRepair(boolean canImproveRepair) {
/* 1530 */     this.moreSettings.setPermissionBit(MoreRolePermissions.IMPROVE_REPAIR.getBit(), canImproveRepair);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanLoad(boolean canLoad) {
/* 1535 */     this.moreSettings.setPermissionBit(MoreRolePermissions.LOAD.getBit(), canLoad);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPickup(boolean canPickup) {
/* 1540 */     this.moreSettings.setPermissionBit(MoreRolePermissions.PICKUP.getBit(), canPickup);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPickupPlanted(boolean canPickupPlanted) {
/* 1545 */     this.moreSettings.setPermissionBit(MoreRolePermissions.PICKUP_PLANTED.getBit(), canPickupPlanted);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPlantItem(boolean canPlantItem) {
/* 1550 */     this.extraSettings.setPermissionBit(ExtraRolePermissions.PLANT_ITEM.getBit(), canPlantItem);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanPullPushTurn(boolean canPullPushTurn) {
/* 1555 */     this.moreSettings.setPermissionBit(MoreRolePermissions.PULL_PUSH.getBit(), canPullPushTurn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanUnload(boolean canUnload) {
/* 1560 */     this.moreSettings.setPermissionBit(MoreRolePermissions.UNLOAD.getBit(), canUnload);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMineFloor(boolean canMineFloor) {
/* 1565 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MINE_FLOOR.getBit(), canMineFloor);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMineIron(boolean canMineIronVeins) {
/* 1570 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MINE_IRON.getBit(), canMineIronVeins);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMineOther(boolean canMineOtherVeins) {
/* 1575 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MINE_OTHER.getBit(), canMineOtherVeins);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanMineRock(boolean canMineRock) {
/* 1580 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MINE_ROCK.getBit(), canMineRock);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanSurface(boolean canMineSurface) {
/* 1585 */     this.moreSettings.setPermissionBit(MoreRolePermissions.SURFACE_MINING.getBit(), canMineSurface);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanTunnel(boolean canTunnel) {
/* 1590 */     this.moreSettings.setPermissionBit(MoreRolePermissions.TUNNEL.getBit(), canTunnel);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanReinforce(boolean canReinforce) {
/* 1595 */     this.moreSettings.setPermissionBit(MoreRolePermissions.REINFORCE.getBit(), canReinforce);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanConfigureTwitter(boolean canConfigureTwitter) {
/* 1600 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MAY_CONFIGURE_TWITTER.getBit(), canConfigureTwitter);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDiplomat(boolean canDiplomat) {
/* 1605 */     this.moreSettings.setPermissionBit(MoreRolePermissions.DIPLOMAT.getBit(), canDiplomat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanDestroyAnyBuilding(boolean canDestroyAnyBuilding) {
/* 1610 */     if (getStatus() == 1 && canDestroyAnyBuilding) {
/*      */       
/* 1612 */       Thread.dumpStack();
/*      */       return;
/*      */     } 
/* 1615 */     this.moreSettings.setPermissionBit(MoreRolePermissions.DESTROY_ANY_BUILDING.getBit(), canDestroyAnyBuilding);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanInviteCitizens(boolean canInviteCitizens) {
/* 1620 */     this.moreSettings.setPermissionBit(MoreRolePermissions.INVITE_CITIZENS.getBit(), canInviteCitizens);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageAllowedObjects(boolean canManageAllowedObjects) {
/* 1625 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_ALLOWED_OBJECTS.getBit(), canManageAllowedObjects);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageCitizenRoles(boolean canManageCitizenRoles) {
/* 1630 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_CITIZEN_ROLES.getBit(), canManageCitizenRoles);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageGuards(boolean canManageGuards) {
/* 1635 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_GUARDS.getBit(), canManageGuards);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageMap(boolean canManageMap) {
/* 1640 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_MAP.getBit(), canManageMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageReputations(boolean canManageReputations) {
/* 1645 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_REPUTATIONS.getBit(), canManageReputations);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageRoles(boolean canManageRoles) {
/* 1650 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_ROLES.getBit(), canManageRoles);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanManageSettings(boolean canManageSettings) {
/* 1655 */     this.moreSettings.setPermissionBit(MoreRolePermissions.MANAGE_SETTINGS.getBit(), canManageSettings);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanResizeSettlement(boolean canResizeSettlement) {
/* 1660 */     this.moreSettings.setPermissionBit(MoreRolePermissions.RESIZE.getBit(), canResizeSettlement);
/*      */   }
/*      */ 
/*      */   
/*      */   public void SetCanPerformActionsOnAlliedDeeds(boolean canPerformActionsOnAlliedDeeds) {
/* 1665 */     this.moreSettings.setPermissionBit(MoreRolePermissions.ALLOW_ACTIONS_ON_ALLIED_DEED.getBit(), canPerformActionsOnAlliedDeeds);
/*      */   }
/*      */   
/*      */   abstract void create() throws IOException;
/*      */   
/*      */   public abstract void setName(String paramString) throws IOException;
/*      */   
/*      */   public abstract void setMayHire(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayBuild(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayCuttrees(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayMine(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayFarm(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayManageRoles(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayDestroy(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayTerraform(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayExpand(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayInvite(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayPassAllFences(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayLockFences(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayAttackCitizens(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayAttackNonCitizens(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setDiplomat(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setVillageAppliedTo(int paramInt) throws IOException;
/*      */   
/*      */   public abstract void setMayFish(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayPushPullTurn(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayLead(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayPickup(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayTame(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayLoad(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayButcher(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayAttachLock(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayPickLocks(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setMayUpdateMap(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void setCutOld(boolean paramBoolean) throws IOException;
/*      */   
/*      */   public abstract void delete() throws IOException;
/*      */   
/*      */   public abstract void save() throws IOException;
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillageRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */