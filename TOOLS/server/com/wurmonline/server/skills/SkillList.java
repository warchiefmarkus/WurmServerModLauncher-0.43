/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SkillList
/*     */ {
/*     */   public static final int SKILLS = 2147483647;
/*     */   public static final int CHARACTERISTICS = 2147483646;
/*     */   public static final int FAITH = 2147483645;
/*     */   public static final int FAVOR = 2147483644;
/*     */   public static final int RELIGION = 2147483643;
/*     */   public static final int ALIGNMENT = 2147483642;
/*     */   public static final int BODY = 1;
/*     */   public static final int MIND = 2;
/*     */   public static final int SOUL = 3;
/*     */   public static final int MIND_LOGICAL = 100;
/*     */   public static final int MIND_SPEED = 101;
/*     */   public static final int BODY_STRENGTH = 102;
/*     */   public static final int BODY_STAMINA = 103;
/*     */   public static final int BODY_CONTROL = 104;
/*     */   public static final int SOUL_STRENGTH = 105;
/*     */   public static final int SOUL_DEPTH = 106;
/*     */   public static final int GROUP_SWORDS = 1000;
/*     */   public static final int GROUP_KNIVES = 1001;
/*     */   public static final int GROUP_SHIELDS = 1002;
/*     */   public static final int GROUP_AXES = 1003;
/*     */   public static final int GROUP_MAULS = 1004;
/*     */   public static final int CARPENTRY = 1005;
/*     */   public static final int WOODCUTTING = 1007;
/*     */   public static final int MINING = 1008;
/*     */   public static final int DIGGING = 1009;
/*     */   public static final int FIREMAKING = 1010;
/*     */   public static final int POTTERY = 1011;
/*     */   public static final int GROUP_TAILORING = 1012;
/*     */   public static final int MASONRY = 1013;
/*     */   public static final int ROPEMAKING = 1014;
/*     */   public static final int GROUP_SMITHING = 1015;
/*     */   public static final int GROUP_SMITHING_WEAPONSMITHING = 1016;
/*     */   public static final int GROUP_SMITHING_ARMOURSMITHING = 1017;
/*     */   public static final int GROUP_COOKING = 1018;
/*     */   public static final int GROUP_NATURE = 1019;
/*     */   public static final int MISCELLANEOUS = 1020;
/*     */   public static final int GROUP_ALCHEMY = 1021;
/*     */   
/*     */   public static boolean IsBlacksmithing(int id) {
/*  49 */     return (id == 10015);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int GROUP_TOYS = 1022;
/*     */   
/*     */   public static final int GROUP_FIGHTING = 1023;
/*     */   
/*     */   public static final int GROUP_HEALING = 1024;
/*     */   
/*     */   public static final int GROUP_CLUBS = 1025;
/*     */   
/*     */   public static final int GROUP_RELIGION = 1026;
/*     */   
/*     */   public static final int GROUP_HAMMERS = 1027;
/*     */   
/*     */   public static final int GROUP_THIEVERY = 1028;
/*     */   
/*     */   public static final int GROUP_WARMACHINES = 1029;
/*     */   
/*     */   public static final int GROUP_ARCHERY = 1030;
/*     */   
/*     */   public static final int GROUP_BOWYERY = 1031;
/*     */   
/*     */   public static final int GROUP_FLETCHING = 1032;
/*     */   
/*     */   public static final int GROUP_POLEARMS = 1033;
/*     */   
/*     */   public static final int ARCHAEOLOGY = 10069;
/*     */   
/*     */   public static final int AXE_SMALL = 10001;
/*     */   
/*     */   public static final int SHOVEL = 10002;
/*     */   
/*     */   public static final int HATCHET = 10003;
/*     */   
/*     */   public static final int RAKE = 10004;
/*     */   
/*     */   public static final int SWORD_LONG = 10005;
/*     */   
/*     */   public static final int SHIELD_MEDIUM_METAL = 10006;
/*     */   
/*     */   public static final int KNIFE_CARVING = 10007;
/*     */   
/*     */   public static final int SAW = 10008;
/*     */   
/*     */   public static final int PICKAXE = 10009;
/*     */   
/*     */   public static final int SMITHING_WEAPON_BLADES = 10010;
/*     */   
/*     */   public static final int SMITHING_WEAPON_HEADS = 10011;
/*     */   
/*     */   public static final int SMITHING_ARMOUR_CHAIN = 10012;
/*     */   
/*     */   public static final int SMITHING_ARMOUR_PLATE = 10013;
/*     */   
/*     */   public static final int SMITHING_SHIELDS = 10014;
/*     */   
/*     */   public static final int SMITHING_BLACKSMITHING = 10015;
/*     */   
/*     */   public static final int CLOTHTAILORING = 10016;
/*     */   
/*     */   public static final int LEATHERWORKING = 10017;
/*     */   
/*     */   public static final int TRACKING = 10018;
/*     */   
/*     */   public static final int SHIELD_SMALL_WOOD = 10019;
/*     */   
/*     */   public static final int SHIELD_MEDIUM_WOOD = 10020;
/*     */   
/*     */   public static final int SHIELD_LARGE_WOOD = 10021;
/*     */   
/*     */   public static final int SHIELD_SMALL_METAL = 10022;
/*     */   
/*     */   public static final int SHIELD_LARGE_METAL = 10023;
/*     */   
/*     */   public static final int AXE_LARGE = 10024;
/*     */   
/*     */   public static final int AXE_HUGE = 10025;
/*     */   
/*     */   public static final int HAMMER = 10026;
/*     */   
/*     */   public static final int SWORD_SHORT = 10027;
/*     */   
/*     */   public static final int SWORD_TWOHANDED = 10028;
/*     */   
/*     */   public static final int KNIFE_BUTCHERING = 10029;
/*     */   
/*     */   public static final int STONE_CHISEL = 10030;
/*     */   
/*     */   public static final int PAVING = 10031;
/*     */   
/*     */   public static final int PROSPECT = 10032;
/*     */   
/*     */   public static final int FISHING = 10033;
/*     */   
/*     */   public static final int SMITHING_LOCKSMITHING = 10034;
/*     */   
/*     */   public static final int REPAIR = 10035;
/*     */   
/*     */   public static final int COALING = 10036;
/*     */   
/*     */   public static final int COOKING_DAIRIES = 10037;
/*     */   
/*     */   public static final int COOKING_STEAKING = 10038;
/*     */   
/*     */   public static final int COOKING_BAKING = 10039;
/*     */   
/*     */   public static final int MILLING = 10040;
/*     */   
/*     */   public static final int SMITHING_METALLURGY = 10041;
/*     */   
/*     */   public static final int ALCHEMY_NATURAL = 10042;
/*     */   
/*     */   public static final int SMITHING_GOLDSMITHING = 10043;
/*     */   
/*     */   public static final int CARPENTRY_FINE = 10044;
/*     */   
/*     */   public static final int GARDENING = 10045;
/*     */   
/*     */   public static final int SICKLE = 10046;
/*     */   
/*     */   public static final int SCYTHE = 10047;
/*     */   
/*     */   public static final int FORESTRY = 10048;
/*     */   
/*     */   public static final int FARMING = 10049;
/*     */   
/*     */   public static final int YOYO = 10050;
/*     */   
/*     */   public static final int TOYMAKING = 10051;
/*     */   
/*     */   public static final int WEAPONLESS_FIGHTING = 10052;
/*     */   
/*     */   public static final int FIGHT_AGGRESSIVESTYLE = 10053;
/*     */   
/*     */   public static final int FIGHT_DEFENSIVESTYLE = 10054;
/*     */   
/*     */   public static final int FIGHT_NORMALSTYLE = 10055;
/*     */   
/*     */   public static final int FIRSTAID = 10056;
/*     */   
/*     */   public static final int TAUNTING = 10057;
/*     */   
/*     */   public static final int SHIELDBASHING = 10058;
/*     */   
/*     */   public static final int BUTCHERING = 10059;
/*     */   
/*     */   public static final int MILKING = 10060;
/*     */   
/*     */   public static final int MAUL_LARGE = 10061;
/*     */   
/*     */   public static final int MAUL_MEDIUM = 10062;
/*     */   
/*     */   public static final int MAUL_SMALL = 10063;
/*     */   
/*     */   public static final int CLUB_HUGE = 10064;
/*     */   
/*     */   public static final int PREACHING = 10065;
/*     */   public static final int PRAYER = 10066;
/*     */   public static final int CHANNELING = 10067;
/*     */   public static final int EXORCISM = 10068;
/*     */   public static final int WARHAMMER = 10070;
/*     */   public static final int FORAGING = 10071;
/*     */   public static final int BOTANIZING = 10072;
/*     */   public static final int CLIMBING = 10073;
/*     */   public static final int STONECUTTING = 10074;
/*     */   public static final int STEALING = 10075;
/*     */   public static final int LOCKPICKING = 10076;
/*     */   public static final int CATAPULT = 10077;
/*     */   public static final int TAMEANIMAL = 10078;
/*     */   public static final int BOW_SHORT = 10079;
/*     */   public static final int BOW_MEDIUM = 10080;
/*     */   public static final int BOW_LONG = 10081;
/*     */   public static final int SHIPBUILDING = 10082;
/*     */   public static final int COOKING_BEVERAGES = 10083;
/*     */   public static final int TRAPS = 10084;
/*     */   public static final int BREEDING = 10085;
/*     */   public static final int MEDITATING = 10086;
/*     */   public static final int PUPPETEERING = 10087;
/*     */   public static final int SPEAR_LONG = 10088;
/*     */   public static final int HALBERD = 10089;
/*     */   public static final int STAFF = 10090;
/*     */   public static final int PAPYRUSMAKING = 10091;
/*     */   public static final int THATCHING = 10092;
/*     */   public static final int BALLISTA = 10093;
/*     */   public static final int TREBUCHET = 10094;
/*     */   public static final int RESTORATION = 10095;
/*     */   public static final short TYPE_BASIC = 0;
/*     */   public static final short TYPE_MEMORY = 1;
/*     */   public static final short TYPE_ENHANCING = 2;
/*     */   public static final short TYPE_NORMAL = 4;
/* 241 */   public static final int[] skillArray = new int[] { 1000, 1001, 1002, 1003, 1004, 1005, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 10001, 10002, 10003, 10004, 10005, 10006, 10007, 10008, 10009, 10010, 10011, 10012, 10013, 10014, 10015, 10016, 10017, 10018, 10019, 10020, 10021, 10022, 10023, 10024, 10025, 10026, 10027, 10028, 10029, 10030, 10031, 10032, 10033, 10034, 10035, 10036, 10037, 10038, 10039, 10040, 10041, 10042, 10043, 10044, 10045, 10046, 10047, 10048, 10049, 10050, 10051, 10052, 10053, 10054, 10055, 10056, 10057, 10058, 10059, 10060, 10061, 10062, 10063, 10064, 10065, 10066, 10067, 10068, 10069, 10070, 10071, 10072, 10073, 10074, 10075, 10076, 10077, 10078, 10079, 10080, 10081, 10082, 10083, 10084, 10085, 10086, 10087, 10088, 10089, 10090, 10091, 10092, 10093, 10094, 10095 };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */