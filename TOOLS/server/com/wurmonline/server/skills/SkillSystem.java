/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SkillSystem
/*     */   implements TimeConstants
/*     */ {
/*  51 */   public static final Map<Integer, SkillTemplate> templates = new HashMap<>();
/*  52 */   public static final Map<Integer, String> skillNames = new HashMap<>();
/*  53 */   public static final Map<String, Integer> namesToSkill = new HashMap<>();
/*  54 */   private static final List<SkillTemplate> templateList = new ArrayList<>();
/*     */   
/*  56 */   private static final Random randomSource = new Random();
/*     */   public static final long SKILLGAIN_BASIC = 300000L;
/*     */   public static final long SKILLGAIN_CHARACTERISTIC = 200000L;
/*     */   public static final long SKILLGAIN_CHARACTERISTIC_BC = 150000L;
/*     */   public static final long SKILLGAIN_GROUP = 20000L;
/*     */   public static final long SKILLGAIN_FIGHTING = 4000L;
/*     */   public static final long SKILLGAIN_TOOL = 7000L;
/*     */   public static final long SKILLGAIN_NORMAL = 4000L;
/*     */   public static final long SKILLGAIN_FAST = 3000L;
/*     */   public static final long SKILLGAIN_RARE = 2000L;
/*     */   public static final long SKILLGAIN_FIGHTING_GROUP = 10000L;
/*  67 */   private static Integer[] skillnums = new Integer[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float priestSlowMod = 1.25F;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long STANDARD_DECAY = 1209600000L;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  80 */     addSkillTemplate(new SkillTemplate(2, "Mind", 300000.0F, MiscConstants.EMPTY_INT_ARRAY, 1209600000L, (short)1, false, true));
/*     */     
/*  82 */     addSkillTemplate(new SkillTemplate(1, "Body", 300000.0F, MiscConstants.EMPTY_INT_ARRAY, 1209600000L, (short)1, false, true));
/*     */     
/*  84 */     addSkillTemplate(new SkillTemplate(3, "Soul", 300000.0F, MiscConstants.EMPTY_INT_ARRAY, 1209600000L, (short)1, false, true));
/*     */ 
/*     */     
/*  87 */     SkillTemplate bct = new SkillTemplate(104, "Body control", 150000.0F, new int[] { 1 }, 1209600000L, (short)0, false, true);
/*     */ 
/*     */     
/*  90 */     addSkillTemplate(bct);
/*  91 */     SkillTemplate bst = new SkillTemplate(103, "Body stamina", 200000.0F, new int[] { 1 }, 1209600000L, (short)0, false, true);
/*     */     
/*  93 */     addSkillTemplate(bst);
/*  94 */     SkillTemplate bsr = new SkillTemplate(102, "Body strength", 200000.0F, new int[] { 1 }, 1209600000L, (short)0, false, true);
/*     */ 
/*     */     
/*  97 */     addSkillTemplate(bsr);
/*  98 */     SkillTemplate mlg = new SkillTemplate(100, "Mind logic", 200000.0F, new int[] { 2 }, 1209600000L, (short)0, false, true);
/*     */     
/* 100 */     addSkillTemplate(mlg);
/* 101 */     SkillTemplate msp = new SkillTemplate(101, "Mind speed", 200000.0F, new int[] { 2 }, 1209600000L, (short)0, false, true);
/*     */     
/* 103 */     addSkillTemplate(msp);
/* 104 */     addSkillTemplate(new SkillTemplate(106, "Soul depth", 200000.0F, new int[] { 3 }, 1209600000L, (short)0, false, true));
/*     */     
/* 106 */     addSkillTemplate(new SkillTemplate(105, "Soul strength", 200000.0F, new int[] { 3 }, 1209600000L, (short)0, false, true));
/*     */ 
/*     */     
/* 109 */     addSkillTemplate(new SkillTemplate(1000, "Swords", 10000.0F, new int[] { 104 }, 1209600000L, (short)2, false, true));
/*     */     
/* 111 */     addSkillTemplate(new SkillTemplate(1003, "Axes", 10000.0F, new int[] { 102 }, 1209600000L, (short)2, false, true));
/*     */     
/* 113 */     addSkillTemplate(new SkillTemplate(1001, "Knives", 10000.0F, new int[] { 104 }, 1209600000L, (short)2, false, true));
/*     */     
/* 115 */     addSkillTemplate(new SkillTemplate(1004, "Mauls", 10000.0F, new int[] { 102 }, 1209600000L, (short)2, false, true));
/*     */     
/* 117 */     addSkillTemplate(new SkillTemplate(1025, "Clubs", 10000.0F, new int[] { 102 }, 1209600000L, (short)2, false, true));
/*     */     
/* 119 */     addSkillTemplate(new SkillTemplate(1027, "Hammers", 10000.0F, new int[] { 102 }, 1209600000L, (short)2, false, true));
/*     */     
/* 121 */     addSkillTemplate(new SkillTemplate(1030, "Archery", 3000.0F, new int[] { 104 }, 1209600000L, (short)2, false, true));
/*     */     
/* 123 */     addSkillTemplate(new SkillTemplate(1033, "Polearms", 10000.0F, new int[] { 102 }, 1209600000L, (short)2, false, true));
/*     */     
/* 125 */     addSkillTemplate(new SkillTemplate(1012, "Tailoring", 20000.0F, new int[] { 100, 104 }, 1209600000L, (short)2));
/*     */     
/* 127 */     addSkillTemplate(new SkillTemplate(1018, "Cooking", 20000.0F, new int[] { 100, 106 }, 1209600000L, (short)2));
/*     */     
/* 129 */     addSkillTemplate(new SkillTemplate(1015, "Smithing", 20000.0F, new int[] { 102, 104 }, 1209600000L, (short)2));
/*     */     
/* 131 */     addSkillTemplate(new SkillTemplate(1016, "Weapon smithing", 
/* 132 */           Servers.localServer.isChallengeServer() ? 4000.0F : 20000.0F, new int[] { 1015 }, 1209600000L, (short)2));
/*     */ 
/*     */     
/* 135 */     addSkillTemplate(new SkillTemplate(1017, "Armour smithing", 20000.0F, new int[] { 1015 }, 1209600000L, (short)2));
/*     */ 
/*     */     
/* 138 */     addSkillTemplate(new SkillTemplate(1020, "Miscellaneous items", 20000.0F, new int[] { 100 }, 1209600000L, (short)4, false, true));
/*     */     
/* 140 */     addSkillTemplate(new SkillTemplate(1002, "Shields", 4000.0F, new int[] { 101, 104 }, 1209600000L, (short)2, true, true));
/*     */     
/* 142 */     addSkillTemplate(new SkillTemplate(1021, "Alchemy", 20000.0F, new int[] { 100 }, 1209600000L, (short)2));
/*     */     
/* 144 */     addSkillTemplate(new SkillTemplate(1019, "Nature", 20000.0F, new int[] { 106 }, 1209600000L, (short)2));
/*     */     
/* 146 */     addSkillTemplate(new SkillTemplate(1022, "Toys", 20000.0F, MiscConstants.EMPTY_INT_ARRAY, 1209600000L, (short)2));
/*     */     
/* 148 */     addSkillTemplate(new SkillTemplate(1023, "Fighting", 20000.0F, new int[] { 101, 104, 102 }, 1209600000L, (short)4, true, true));
/*     */ 
/*     */     
/* 151 */     addSkillTemplate(new SkillTemplate(1024, "Healing", 20000.0F, new int[] { 106, 100 }, 1209600000L, (short)2, false, true));
/*     */     
/* 153 */     addSkillTemplate(new SkillTemplate(1026, "Religion", 20000.0F, new int[] { 106, 105 }, 1209600000L, (short)2, false, true));
/*     */     
/* 155 */     addSkillTemplate(new SkillTemplate(1028, "Thievery", 4000.0F, new int[0], 1209600000L, (short)2, true, 0L));
/*     */ 
/*     */     
/* 158 */     addSkillTemplate(new SkillTemplate(1029, "War machines", 10000.0F, new int[] { 100 }, 1209600000L, (short)2, false, true));
/*     */ 
/*     */ 
/*     */     
/* 162 */     addSkillTemplate(new SkillTemplate(10049, "Farming", 4000.0F, new int[] { 1019, 102 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 165 */     addSkillTemplate(new SkillTemplate(10091, "Papyrusmaking", 4000.0F, new int[] { 1019, 102 }, 1209600000L, (short)4));
/*     */     
/* 167 */     addSkillTemplate(new SkillTemplate(10092, "Thatching", 4000.0F, new int[] { 104, 100 }, 1209600000L, (short)4));
/*     */     
/* 169 */     addSkillTemplate(new SkillTemplate(10045, "Gardening", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 171 */     addSkillTemplate(new SkillTemplate(10086, "Meditating", 2000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 173 */     addSkillTemplate(new SkillTemplate(10048, "Forestry", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 175 */     addSkillTemplate(new SkillTemplate(10004, "Rake", 7000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 177 */     addSkillTemplate(new SkillTemplate(10047, "Scythe", 7000.0F, new int[] { 1020 }, 1209600000L, (short)4, false, true));
/*     */     
/* 179 */     addSkillTemplate(new SkillTemplate(10046, "Sickle", 7000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 181 */     addSkillTemplate(new SkillTemplate(10001, "Small Axe", 7000.0F, new int[] { 1003 }, 1209600000L, (short)4, false, true));
/*     */     
/* 183 */     addSkillTemplate(new SkillTemplate(1008, "Mining", 8000.0F, new int[] { 103, 102, 105 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 186 */     addSkillTemplate(new SkillTemplate(1009, "Digging", 3000.0F, new int[] { 103, 102 }, 1209600000L, (short)4));
/*     */     
/* 188 */     addSkillTemplate(new SkillTemplate(10009, "Pickaxe", 7000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 190 */     addSkillTemplate(new SkillTemplate(10002, "Shovel", 7000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 192 */     addSkillTemplate(new SkillTemplate(1011, "Pottery", 4000.0F, new int[] { 106, 100 }, 1209600000L, (short)4));
/*     */     
/* 194 */     addSkillTemplate(new SkillTemplate(1014, "Ropemaking", 4000.0F, new int[] { 100 }, 1209600000L, (short)4));
/*     */     
/* 196 */     addSkillTemplate(new SkillTemplate(1007, "Woodcutting", 4000.0F, new int[] { 103, 102 }, 1209600000L, (short)4));
/*     */     
/* 198 */     addSkillTemplate(new SkillTemplate(10003, "Hatchet", 7000.0F, new int[] { 1003 }, 1209600000L, (short)4));
/*     */     
/* 200 */     addSkillTemplate(new SkillTemplate(10017, "Leatherworking", 4000.0F, new int[] { 1012 }, 1209600000L, (short)4));
/*     */     
/* 202 */     addSkillTemplate(new SkillTemplate(10016, "Cloth tailoring", 4000.0F, new int[] { 1012 }, 1209600000L, (short)4));
/*     */     
/* 204 */     addSkillTemplate(new SkillTemplate(1013, "Masonry", 4000.0F, new int[] { 102, 100 }, 1209600000L, (short)4));
/*     */     
/* 206 */     addSkillTemplate(new SkillTemplate(10010, "Blades smithing", 4000.0F, new int[] { 1016 }, 1209600000L, (short)4));
/*     */     
/* 208 */     addSkillTemplate(new SkillTemplate(10011, "Weapon heads smithing", 4000.0F, new int[] { 1016 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 211 */     addSkillTemplate(new SkillTemplate(10012, "Chain armour smithing", 4000.0F, new int[] { 1017 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 214 */     addSkillTemplate(new SkillTemplate(10013, "Plate armour smithing", 4000.0F, new int[] { 1017 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 217 */     addSkillTemplate(new SkillTemplate(10014, "Shield smithing", 4000.0F, new int[] { 1017 }, 1209600000L, (short)4));
/*     */     
/* 219 */     addSkillTemplate(new SkillTemplate(10015, "Blacksmithing", 4000.0F, new int[] { 1015 }, 1209600000L, (short)4));
/*     */     
/* 221 */     addSkillTemplate(new SkillTemplate(10037, "Dairy food making", 4000.0F, new int[] { 1018 }, 1209600000L, (short)4));
/*     */     
/* 223 */     addSkillTemplate(new SkillTemplate(10038, "Hot food cooking", 4000.0F, new int[] { 1018 }, 1209600000L, (short)4));
/*     */     
/* 225 */     addSkillTemplate(new SkillTemplate(10039, "Baking", 2000.0F, new int[] { 1018 }, 1209600000L, (short)4));
/*     */     
/* 227 */     addSkillTemplate(new SkillTemplate(10083, "Beverages", 4000.0F, new int[] { 1018 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 230 */     addSkillTemplate(new SkillTemplate(10005, "Longsword", 4000.0F, new int[] { 1000 }, 1209600000L, (short)4, false, true));
/*     */     
/* 232 */     addSkillTemplate(new SkillTemplate(10061, "Large maul", 4000.0F, new int[] { 1004 }, 1209600000L, (short)4, false, true));
/*     */     
/* 234 */     addSkillTemplate(new SkillTemplate(10062, "Medium maul", 4000.0F, new int[] { 1004 }, 1209600000L, (short)4, false, true));
/*     */     
/* 236 */     addSkillTemplate(new SkillTemplate(10063, "Small maul", 4000.0F, new int[] { 1004 }, 1209600000L, (short)4, false, true));
/*     */ 
/*     */     
/* 239 */     addSkillTemplate(new SkillTemplate(10070, "Warhammer", 4000.0F, new int[] { 1027 }, 1209600000L, (short)4, false, true));
/*     */     
/* 241 */     addSkillTemplate(new SkillTemplate(10088, "Long spear", 4000.0F, new int[] { 1033 }, 1209600000L, (short)4, false, true));
/*     */     
/* 243 */     addSkillTemplate(new SkillTemplate(10089, "Halberd", 4000.0F, new int[] { 1033 }, 1209600000L, (short)4, false, true));
/*     */     
/* 245 */     addSkillTemplate(new SkillTemplate(10090, "Staff", 4000.0F, new int[] { 1033 }, 1209600000L, (short)4, false, true));
/*     */     
/* 247 */     addSkillTemplate(new SkillTemplate(10007, "Carving knife", 4000.0F, new int[] { 1001 }, 1209600000L, (short)4));
/*     */     
/* 249 */     addSkillTemplate(new SkillTemplate(10029, "Butchering knife", 4000.0F, new int[] { 1001 }, 1209600000L, (short)4));
/*     */     
/* 251 */     addSkillTemplate(new SkillTemplate(10030, "Stone chisel", 4000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 253 */     addSkillTemplate(new SkillTemplate(10064, "Huge club", 4000.0F, new int[] { 1025 }, 1209600000L, (short)4, false, true));
/*     */ 
/*     */     
/* 256 */     addSkillTemplate(new SkillTemplate(10008, "Saw", 3000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 259 */     addSkillTemplate(new SkillTemplate(10059, "Butchering", 4000.0F, new int[] { 1018, 102 }, 1209600000L, (short)4));
/*     */     
/* 261 */     addSkillTemplate(new SkillTemplate(1005, "Carpentry", 4000.0F, new int[] { 104, 100 }, 1209600000L, (short)4));
/*     */     
/* 263 */     addSkillTemplate(new SkillTemplate(1010, "Firemaking", 4000.0F, new int[] { 100 }, 1209600000L, (short)4));
/*     */     
/* 265 */     addSkillTemplate(new SkillTemplate(10018, "Tracking", 2000.0F, new int[] { 100, 106 }, 1209600000L, (short)4, false, true));
/*     */     
/* 267 */     addSkillTemplate(new SkillTemplate(10019, "Small wooden shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 269 */     addSkillTemplate(new SkillTemplate(10020, "Medium wooden shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 271 */     addSkillTemplate(new SkillTemplate(10021, "Large wooden shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 273 */     addSkillTemplate(new SkillTemplate(10022, "Small metal shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 275 */     addSkillTemplate(new SkillTemplate(10023, "Large metal shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 277 */     addSkillTemplate(new SkillTemplate(10006, "Medium metal shield", 3000.0F, new int[] { 1002 }, 1209600000L, (short)4, true, true));
/*     */     
/* 279 */     addSkillTemplate(new SkillTemplate(10024, "Large axe", 4000.0F, new int[] { 1003 }, 1209600000L, (short)4, false, true));
/*     */     
/* 281 */     addSkillTemplate(new SkillTemplate(10025, "Huge axe", 4000.0F, new int[] { 1003 }, 1209600000L, (short)4, false, true));
/*     */     
/* 283 */     addSkillTemplate(new SkillTemplate(10027, "Shortsword", 4000.0F, new int[] { 1000 }, 1209600000L, (short)4, false, true));
/*     */     
/* 285 */     addSkillTemplate(new SkillTemplate(10028, "Two handed sword", 4000.0F, new int[] { 1000 }, 1209600000L, (short)4, false, true));
/*     */     
/* 287 */     addSkillTemplate(new SkillTemplate(10026, "Hammer", 4000.0F, new int[] { 1020 }, 1209600000L, (short)4, false, true));
/*     */     
/* 289 */     addSkillTemplate(new SkillTemplate(10031, "Paving", 4000.0F, new int[] { 102, 105 }, 1209600000L, (short)4));
/*     */     
/* 291 */     addSkillTemplate(new SkillTemplate(10032, "Prospecting", 2000.0F, MiscConstants.EMPTY_INT_ARRAY, 1209600000L, (short)4));
/*     */     
/* 293 */     addSkillTemplate(new SkillTemplate(10033, "Fishing", 3000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 295 */     addSkillTemplate(new SkillTemplate(10034, "Locksmithing", 4000.0F, new int[] { 1015 }, 1209600000L, (short)4));
/*     */     
/* 297 */     addSkillTemplate(new SkillTemplate(10035, "Repairing", 4000.0F, new int[] { 1020 }, 1209600000L, (short)4));
/*     */     
/* 299 */     addSkillTemplate(new SkillTemplate(10036, "Coal-making", 2000.0F, new int[] { 105, 100 }, 1209600000L, (short)4));
/*     */     
/* 301 */     addSkillTemplate(new SkillTemplate(10040, "Milling", 2000.0F, new int[] { 105, 103 }, 1209600000L, (short)4));
/*     */     
/* 303 */     addSkillTemplate(new SkillTemplate(10041, "Metallurgy", 4000.0F, new int[] { 1015 }, 1209600000L, (short)4));
/*     */     
/* 305 */     addSkillTemplate(new SkillTemplate(10042, "Natural substances", 4000.0F, new int[] { 1021 }, 1209600000L, (short)4));
/*     */     
/* 307 */     addSkillTemplate(new SkillTemplate(10043, "Jewelry smithing", 4000.0F, new int[] { 1015 }, 1209600000L, (short)4));
/*     */     
/* 309 */     addSkillTemplate(new SkillTemplate(10044, "Fine carpentry", 4000.0F, new int[] { 1005 }, 1209600000L, (short)4));
/*     */     
/* 311 */     addSkillTemplate(new SkillTemplate(1031, "Bowyery", 4000.0F, new int[] { 1005 }, 1209600000L, (short)4));
/*     */     
/* 313 */     addSkillTemplate(new SkillTemplate(1032, "Fletching", 4000.0F, new int[] { 1005 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 316 */     addSkillTemplate(new SkillTemplate(10050, "Yoyo", 7000.0F, new int[] { 1022 }, 1209600000L, (short)4));
/*     */     
/* 318 */     addSkillTemplate(new SkillTemplate(10087, "Puppeteering", 2000.0F, new int[] { 1022 }, 1209600000L, (short)4));
/*     */     
/* 320 */     addSkillTemplate(new SkillTemplate(10051, "Toy making", 4000.0F, new int[] { 1005 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 323 */     addSkillTemplate(new SkillTemplate(10052, "Weaponless fighting", 4000.0F, new int[] { 1023 }, 1209600000L, (short)4, true, true));
/*     */     
/* 325 */     addSkillTemplate(new SkillTemplate(10053, "Aggressive fighting", 4000.0F, new int[] { 1023, 101 }, 1209600000L, (short)4, true, true));
/*     */ 
/*     */     
/* 328 */     addSkillTemplate(new SkillTemplate(10054, "Defensive fighting", 4000.0F, new int[] { 1023, 101 }, 1209600000L, (short)4, true, true));
/*     */     
/* 330 */     addSkillTemplate(new SkillTemplate(10055, "Normal fighting", 4000.0F, new int[] { 1023, 101 }, 1209600000L, (short)4, true, true));
/*     */     
/* 332 */     addSkillTemplate(new SkillTemplate(10056, "First aid", 4000.0F, new int[] { 1024 }, 1209600000L, (short)4, false, true));
/*     */     
/* 334 */     addSkillTemplate(new SkillTemplate(10057, "Taunting", 3000.0F, new int[] { 1023 }, 1209600000L, (short)4, true, true));
/*     */     
/* 336 */     addSkillTemplate(new SkillTemplate(10058, "Shield bashing", 3000.0F, new int[] { 1023 }, 1209600000L, (short)4, true, true));
/*     */     
/* 338 */     addSkillTemplate(new SkillTemplate(10060, "Milking", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 340 */     addSkillTemplate(new SkillTemplate(10065, "Preaching", 2000.0F, new int[] { 1026 }, 1209600000L, (short)4));
/*     */     
/* 342 */     addSkillTemplate(new SkillTemplate(10066, "Prayer", 4000.0F, new int[] { 1026 }, 1209600000L, (short)4));
/*     */     
/* 344 */     addSkillTemplate(new SkillTemplate(10067, "Channeling", 4000.0F, new int[] { 1026 }, 1209600000L, (short)4, false, true));
/*     */     
/* 346 */     addSkillTemplate(new SkillTemplate(10068, "Exorcism", 2000.0F, new int[] { 1026 }, 1209600000L, (short)4));
/*     */     
/* 348 */     addSkillTemplate(new SkillTemplate(10069, "Archaeology", 4000.0F, new int[] { 100, 104 }, 1209600000L, (short)4, false, true));
/*     */     
/* 350 */     addSkillTemplate(new SkillTemplate(10071, "Foraging", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 352 */     addSkillTemplate(new SkillTemplate(10072, "Botanizing", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 354 */     addSkillTemplate(new SkillTemplate(10073, "Climbing", 4000.0F, new int[] { 104, 102 }, 1209600000L, (short)4));
/*     */     
/* 356 */     addSkillTemplate(new SkillTemplate(10074, "Stone cutting", 4000.0F, new int[] { 1013 }, 1209600000L, (short)4));
/*     */     
/* 358 */     addSkillTemplate(new SkillTemplate(10076, "Lock picking", 2000.0F, new int[] { 1028, 104, 100 }, 1209600000L, (short)4, true, 600000L));
/*     */ 
/*     */     
/* 361 */     addSkillTemplate(new SkillTemplate(10075, "Stealing", 2000.0F, new int[] { 1028 }, 1209600000L, (short)4, true, 600000L));
/*     */     
/* 363 */     addSkillTemplate(new SkillTemplate(10084, "Traps", 4000.0F, new int[] { 1028, 104, 100 }, 1209600000L, (short)4, true, 0L));
/*     */ 
/*     */     
/* 366 */     addSkillTemplate(new SkillTemplate(10077, "Catapults", 4000.0F, new int[] { 1029 }, 1209600000L, (short)4, false, true));
/*     */     
/* 368 */     addSkillTemplate(new SkillTemplate(10078, "Animal taming", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 370 */     addSkillTemplate(new SkillTemplate(10085, "Animal husbandry", 4000.0F, new int[] { 1019 }, 1209600000L, (short)4));
/*     */     
/* 372 */     addSkillTemplate(new SkillTemplate(10079, "Short bow", 4000.0F, new int[] { 1030 }, 1209600000L, (short)4, true, true));
/*     */     
/* 374 */     addSkillTemplate(new SkillTemplate(10081, "Long bow", 4000.0F, new int[] { 1030 }, 1209600000L, (short)4, true, true));
/*     */     
/* 376 */     addSkillTemplate(new SkillTemplate(10080, "Medium bow", 4000.0F, new int[] { 1030 }, 1209600000L, (short)4, true, true));
/*     */     
/* 378 */     addSkillTemplate(new SkillTemplate(10082, "Ship building", 7000.0F, new int[] { 1005 }, 1209600000L, (short)4));
/*     */ 
/*     */     
/* 381 */     addSkillTemplate(new SkillTemplate(10093, "Ballistae", 2000.0F, new int[] { 1029 }, 1209600000L, (short)4, false, true));
/*     */ 
/*     */     
/* 384 */     addSkillTemplate(new SkillTemplate(10094, "Trebuchets", 2000.0F, new int[] { 1029 }, 1209600000L, (short)4, false, true));
/*     */ 
/*     */     
/* 387 */     addSkillTemplate(new SkillTemplate(10095, "Restoration", 4000.0F, new int[] { 10069 }, 1209600000L, (short)4, false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNameFor(int skillNum) {
/* 402 */     String name = skillNames.get(Integer.valueOf(skillNum));
/* 403 */     if (name != null)
/* 404 */       return name; 
/* 405 */     return "unknown";
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getNextRandomFloat() {
/* 410 */     return randomSource.nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static int[] getDependenciesFor(int skillNum) {
/* 421 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 422 */     assert template != null;
/* 423 */     return template.getDependencies();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getDifficultyFor(int skillNum, boolean priest) {
/* 435 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 436 */     if (template.getType() == 0)
/*     */     {
/* 438 */       if (priest && template.isPriestSlowskillgain)
/* 439 */         return template.getDifficulty() * 1.25F; 
/*     */     }
/* 441 */     return template.getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   static long getDecayTimeFor(int skillNum) {
/* 446 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 447 */     return template.getDecayTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getTypeFor(int skillNum) {
/* 452 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 453 */     return template.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getTickTimeFor(int skillNum) {
/* 458 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 459 */     return template.getTickTime();
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isFightingSkill(int skillNum) {
/* 464 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 465 */     return template.fightSkill;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isThieverySkill(int skillNum) {
/* 470 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 471 */     return template.thieverySkill;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addSkillTemplate(SkillTemplate template) {
/* 476 */     templates.put(Integer.valueOf(template.getNumber()), template);
/* 477 */     SkillStat.addSkill(template.getNumber(), template.name);
/* 478 */     skillNames.put(Integer.valueOf(template.getNumber()), template.getName());
/* 479 */     namesToSkill.put(template.getName().toLowerCase(), Integer.valueOf(template.getNumber()));
/* 480 */     skillnums = (Integer[])templates.keySet().toArray((Object[])new Integer[templates.size()]);
/*     */     
/* 482 */     templateList.add(template);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean ignoresEnemies(int skillNum) {
/* 492 */     SkillTemplate template = templates.get(Integer.valueOf(skillNum));
/* 493 */     return template.ignoresEnemies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRandomSkillNum() {
/* 502 */     return skillnums[Server.rand.nextInt(templates.size())].intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SkillTemplate[] getAllSkillTemplates() {
/* 511 */     return (SkillTemplate[])templates.values().toArray((Object[])new SkillTemplate[templates.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getSkillByName(String name) {
/* 521 */     Integer i = namesToSkill.get(name.toLowerCase());
/* 522 */     if (i == null)
/* 523 */       return -1; 
/* 524 */     return i.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static SkillTemplate getSkillTemplateByIndex(int index) {
/* 529 */     if (index > templateList.size())
/* 530 */       return null; 
/* 531 */     return templateList.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumberOfSkillTemplates() {
/* 536 */     return templateList.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */