/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerAction
/*     */   implements PlayerActionConstants
/*     */ {
/*     */   public static final int SURFACE_TILE = 1;
/*     */   public static final int SURFACE_TILE_BORDER = 2;
/*     */   public static final int CAVE_TILE = 16;
/*     */   public static final int INVENTORY_ITEM = 256;
/*     */   public static final int GROUND_ITEM = 512;
/*     */   public static final int WALL = 1024;
/*     */   public static final int HOUSE = 2048;
/*     */   public static final int CREATURE = 4096;
/*     */   public static final int PLAYER = 8192;
/*     */   public static final int ANY_TILE = 17;
/*     */   public static final int STRUCTURE = 3072;
/*     */   public static final int MOBILE = 12288;
/*     */   public static final int ANY_ITEM = 768;
/*     */   public static final int CREATURE_OR_ITEM = 4864;
/*     */   public static final int STATIC_OBJECT = 3840;
/*     */   public static final int ANYTHING = 65535;
/*     */   private final short id;
/*     */   private String bind;
/*     */   private final String name;
/*     */   private final boolean instant;
/*     */   private final boolean atomic;
/*     */   private final int targetMask;
/*  72 */   private static Map<Short, PlayerAction> actionIds = new HashMap<>();
/*     */ 
/*     */   
/*     */   public PlayerAction(String bind, short aId, int aTargetMask) {
/*  76 */     this(bind, aId, aTargetMask, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerAction(String bind, short aId, int aTargetMask, String aName) {
/*  81 */     this(aId, aTargetMask, aName, false);
/*  82 */     actionIds.put(Short.valueOf(aId), this);
/*  83 */     this.bind = bind;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerAction(short aId, int aTargetMask, String aName, boolean aInstant) {
/*  88 */     this.id = aId;
/*  89 */     this.name = aName;
/*  90 */     this.instant = aInstant;
/*  91 */     this.targetMask = aTargetMask;
/*  92 */     this.bind = null;
/*     */     
/*  94 */     switch (aId) {
/*     */       
/*     */       case 1:
/*     */       case 6:
/*     */       case 7:
/*     */       case 54:
/*     */       case 55:
/*     */       case 59:
/*     */       case 86:
/*     */       case 93:
/*     */       case 638:
/* 105 */         this.atomic = true;
/*     */         return;
/*     */     } 
/* 108 */     this.atomic = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final short getId() {
/* 116 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 121 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBind() {
/* 126 */     return this.bind;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInstant() {
/* 131 */     return this.instant;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAtomic() {
/* 136 */     return this.atomic;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTargetMask() {
/* 141 */     return this.targetMask;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PlayerAction getByActionId(short id) {
/* 146 */     return actionIds.get(Short.valueOf(id));
/*     */   }
/*     */   
/* 149 */   public static final PlayerAction[] emotes = new PlayerAction[] { new PlayerAction("", (short)-3, 65535, "Emotes"), new PlayerAction("", (short)-13, 65535, "Nice"), new PlayerAction("", (short)2000, 65535, "Smile"), new PlayerAction("", (short)2001, 65535, "Chuckle"), new PlayerAction("", (short)2002, 65535, "Applaud"), new PlayerAction("", (short)2003, 65535, "Hug"), new PlayerAction("", (short)2004, 65535, "Kiss"), new PlayerAction("", (short)2005, 65535, "Grovel"), new PlayerAction("", (short)2006, 65535, "Worship"), new PlayerAction("", (short)2007, 65535, "Comfort"), new PlayerAction("", (short)2008, 65535, "Dance"), new PlayerAction("", (short)2009, 65535, "Flirt"), new PlayerAction("", (short)2010, 65535, "Bow"), new PlayerAction("", (short)2011, 65535, "Kiss hand"), new PlayerAction("", (short)2012, 65535, "Tickle"), new PlayerAction("", (short)-16, 65535, "Neutral"), new PlayerAction("", (short)2013, 65535, "Wave"), new PlayerAction("", (short)2014, 65535, "Call"), new PlayerAction("", (short)2015, 65535, "Poke"), new PlayerAction("", (short)2016, 65535, "Roll with the eyes"), new PlayerAction("", (short)2017, 65535, "Disbelieve"), new PlayerAction("", (short)2018, 65535, "Worry"), new PlayerAction("", (short)2019, 65535, "Disagree"), new PlayerAction("", (short)2020, 65535, "Tease"), new PlayerAction("", (short)2021, 65535, "Laugh"), new PlayerAction("", (short)2022, 65535, "Cry"), new PlayerAction("", (short)2023, 65535, "Point"), new PlayerAction("", (short)2030, 65535, "Follow"), new PlayerAction("", (short)2031, 65535, "Goodbye"), new PlayerAction("", (short)2032, 65535, "Lead"), new PlayerAction("", (short)2033, 65535, "That way"), new PlayerAction("", (short)2034, 65535, "Wrong way"), new PlayerAction("", (short)-6, 65535, "Offensive"), new PlayerAction("", (short)2024, 65535, "Spit"), new PlayerAction("", (short)2025, 65535, "Fart"), new PlayerAction("", (short)2026, 65535, "Insult"), new PlayerAction("", (short)2027, 65535, "Push"), new PlayerAction("", (short)2028, 65535, "Curse"), new PlayerAction("", (short)2029, 65535, "Slap") };
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
/* 173 */   public static final PlayerAction DEFAULT_ACTION = new PlayerAction("DEFAULT_ACTION", (short)-1, 65535);
/* 174 */   public static final PlayerAction DEFAULT_TERRAFORM_ACTION = new PlayerAction("DEFAULT_TERRAFORM_ACTION", (short)718, 529);
/* 175 */   public static final PlayerAction EXAMINE = new PlayerAction("EXAMINE", (short)1, 65535, "Examine");
/* 176 */   public static final PlayerAction OPEN = new PlayerAction("OPEN", (short)3, 4864);
/* 177 */   public static final PlayerAction CLOSE = new PlayerAction("CLOSE", (short)4, 768);
/* 178 */   public static final PlayerAction TAKE = new PlayerAction("TAKE", (short)6, 768);
/* 179 */   public static final PlayerAction TRADE = new PlayerAction("TRADE", (short)63, 12288);
/* 180 */   public static final PlayerAction DROP = new PlayerAction("DROP", (short)7, 768);
/* 181 */   public static final PlayerAction COMBINE = new PlayerAction("COMBINE", (short)93, 256);
/* 182 */   public static final PlayerAction LOCK = new PlayerAction("LOCK", (short)28, 3840);
/* 183 */   public static final PlayerAction TAME = new PlayerAction("TAME", (short)46, 4096);
/* 184 */   public static final PlayerAction SELL = new PlayerAction("SELL", (short)31, 4864);
/* 185 */   public static final PlayerAction PLAN_BUILDING = new PlayerAction("PLAN_BUILDING", (short)56, 1);
/* 186 */   public static final PlayerAction FINALIZE_BUILDING = new PlayerAction("FINALIZE_BUILDING", (short)58, 2048);
/* 187 */   public static final PlayerAction ADD_FRIEND = new PlayerAction("ADD_FRIEND", (short)60, 8192);
/* 188 */   public static final PlayerAction REMOVE_FRIEND = new PlayerAction("REMOVE_FRIEND", (short)61, 8192);
/* 189 */   public static final PlayerAction DRAG = new PlayerAction("DRAG", (short)74, 512);
/* 190 */   public static final PlayerAction STOP_DRAGGING = new PlayerAction("STOP_DRAGGING", (short)75, 512);
/* 191 */   public static final PlayerAction SPAM_MODE = new PlayerAction("SPAM_MODE", (short)84, 65535, "Spam mode");
/* 192 */   public static final PlayerAction CUT_DOWN = new PlayerAction("CUT_DOWN", (short)96, 1025);
/* 193 */   public static final PlayerAction CHOP_UP = new PlayerAction("CHOP_UP", (short)97, 768);
/* 194 */   public static final PlayerAction PUSH = new PlayerAction("PUSH", (short)99, 512);
/* 195 */   public static final PlayerAction PUSH_GENTLY = new PlayerAction("PUSH_GENTLY", (short)696, 512);
/* 196 */   public static final PlayerAction MOVE_CENTER = new PlayerAction("MOVE_CENTER", (short)864, 512);
/* 197 */   public static final PlayerAction UNLOCK = new PlayerAction("UNLOCK", (short)102, 3840);
/* 198 */   public static final PlayerAction LEAD = new PlayerAction("LEAD", (short)106, 4096);
/* 199 */   public static final PlayerAction STOP_LEADING = new PlayerAction("STOP_LEADING", (short)107, 4096);
/* 200 */   public static final PlayerAction TRACK = new PlayerAction("TRACK", (short)109, 17);
/* 201 */   public static final PlayerAction BURY = new PlayerAction("BURY", (short)119, 768);
/* 202 */   public static final PlayerAction BURY_ALL = new PlayerAction("BURY_ALL", (short)707, 768);
/* 203 */   public static final PlayerAction BUTCHER = new PlayerAction("BUTCHER", (short)120, 768);
/* 204 */   public static final PlayerAction FILET = new PlayerAction("FILET", (short)225, 768);
/* 205 */   public static final PlayerAction PRAY = new PlayerAction("PRAY", (short)141, 529);
/* 206 */   public static final PlayerAction PREACH = new PlayerAction("PREACH", (short)216, 512);
/* 207 */   public static final PlayerAction LISTEN = new PlayerAction("LISTEN", (short)115, 8192);
/* 208 */   public static final PlayerAction LINK = new PlayerAction("LINK", (short)399, 8192);
/* 209 */   public static final PlayerAction SACRIFICE = new PlayerAction("SACRIFICE", (short)142, 4608);
/* 210 */   public static final PlayerAction MEDITATE = new PlayerAction("MEDITATE", (short)384, 512);
/* 211 */   public static final PlayerAction FIRSTAID = new PlayerAction("FIRSTAID", (short)196, 256);
/* 212 */   public static final PlayerAction TREAT = new PlayerAction("TREAT", (short)284, 256);
/* 213 */   public static final PlayerAction LOAD_CARGO = new PlayerAction("LOAD_CARGO", (short)605, 512);
/* 214 */   public static final PlayerAction UNLOAD_CARGO = new PlayerAction("UNLOAD_CARGO", (short)606, 256);
/* 215 */   public static final PlayerAction STOP = new PlayerAction("STOP", (short)149, 65535, "Stop");
/*     */   
/* 217 */   public static final PlayerAction MINE_FORWARD = new PlayerAction("MINE_FORWARD", (short)145, 19);
/* 218 */   public static final PlayerAction MINE_UP = new PlayerAction("MINE_UP", (short)146, 16);
/* 219 */   public static final PlayerAction MINE_DOWN = new PlayerAction("MINE_DOWN", (short)147, 16);
/* 220 */   public static final PlayerAction FARM = new PlayerAction("FARM", (short)151, 1);
/*     */   
/* 222 */   public static final PlayerAction HARVEST = new PlayerAction("HARVEST", (short)152, 513);
/* 223 */   public static final PlayerAction SOW = new PlayerAction("SOW", (short)153, 1);
/* 224 */   public static final PlayerAction PROSPECT = new PlayerAction("PROSPECT", (short)156, 17);
/* 225 */   public static final PlayerAction FISH = new PlayerAction("FISH", (short)160, 529);
/* 226 */   public static final PlayerAction REPAIR = new PlayerAction("REPAIR", (short)162, 3840);
/* 227 */   public static final PlayerAction BUILD_STONE_WALL = new PlayerAction("BUILD_STONE_WALL", (short)163, 2);
/* 228 */   public static final PlayerAction BUILD_TALL_STONE_WALL = new PlayerAction("BUILD_TALL_STONE_WALL", (short)164, 2);
/* 229 */   public static final PlayerAction BUILD_PALISADE = new PlayerAction("BUILD_PALISADE", (short)165, 2);
/* 230 */   public static final PlayerAction BUILD_FENCE = new PlayerAction("BUILD_FENCE", (short)166, 2);
/* 231 */   public static final PlayerAction BUILD_PALISADE_GATE = new PlayerAction("BUILD_PALISADE_GATE", (short)167, 2);
/* 232 */   public static final PlayerAction BUILD_FENCE_GATE = new PlayerAction("BUILD_FENCE_GATE", (short)168, 2);
/* 233 */   public static final PlayerAction CONTINUE = new PlayerAction("CONTINUE", (short)169, 65535);
/* 234 */   public static final PlayerAction CONTINUE_BUILDING = new PlayerAction("CONTINUE_BUILDING", (short)170, 3072);
/* 235 */   public static final PlayerAction DESTROY_FENCE_PLAN = new PlayerAction("DESTROY_FENCE_PLAN", (short)171, 1024);
/* 236 */   public static final PlayerAction DESTROY_FENCE = new PlayerAction("DESTROY_FENCE", (short)172, 1024);
/* 237 */   public static final PlayerAction TURN_CLOCKWISE = new PlayerAction("TURN_CLOCKWISE", (short)177, 512);
/* 238 */   public static final PlayerAction TURN_COUNTERCLOCKWISE = new PlayerAction("TURN_COUNTERCLOCKWISE", (short)178, 512);
/* 239 */   public static final PlayerAction PULL = new PlayerAction("PULL", (short)181, 512);
/* 240 */   public static final PlayerAction PULL_GENTLY = new PlayerAction("PULL_GENTLY", (short)697, 512);
/* 241 */   public static final PlayerAction DESTROY_WALL = new PlayerAction("DESTROY_WALL", (short)174, 2048);
/* 242 */   public static final PlayerAction DESTROY_ITEM = new PlayerAction("DESTROY_ITEM", (short)83, 512);
/* 243 */   public static final PlayerAction DESTROY_PAVEMENT = new PlayerAction("DESTROY_PAVEMENT", (short)191, 17);
/* 244 */   public static final PlayerAction EMBARK_DRIVER = new PlayerAction("EMBARK_DRIVER", (short)331, 512);
/* 245 */   public static final PlayerAction EMBARK_PASSENGER = new PlayerAction("EMBARK_PASSENGER", (short)332, 512);
/* 246 */   public static final PlayerAction DISEMBARK = new PlayerAction("DISEMBARK", (short)333, 529);
/*     */ 
/*     */   
/* 249 */   public static final PlayerAction PICK_SPROUT = new PlayerAction("PICK_SPROUT", (short)187, 1);
/* 250 */   public static final PlayerAction IMPROVE = new PlayerAction("IMPROVE", (short)192, 3840);
/* 251 */   public static final PlayerAction FORAGE = new PlayerAction("FORAGE", (short)223, 1);
/* 252 */   public static final PlayerAction BOTANIZE = new PlayerAction("BOTANIZE", (short)224, 1);
/* 253 */   public static final PlayerAction MINE_TUNNEL = new PlayerAction("MINE_TUNNEL", (short)227, 1);
/* 254 */   public static final PlayerAction FINISH = new PlayerAction("FINISH", (short)228, 768);
/* 255 */   public static final PlayerAction FEED = new PlayerAction("FEED", (short)230, 4096);
/* 256 */   public static final PlayerAction CULTIVATE = new PlayerAction("CULTIVATE", (short)318, 1);
/* 257 */   public static final PlayerAction TARGET = new PlayerAction("TARGET", (short)326, 12288);
/* 258 */   public static final PlayerAction TARGET_HOSTILE = new PlayerAction("TARGET_HOSTILE", (short)716, 12288);
/* 259 */   public static final PlayerAction NO_TARGET = new PlayerAction("NO_TARGET", (short)341, 65535, "No target");
/* 260 */   public static final PlayerAction ABSORB = new PlayerAction("ABSORB", (short)347, 1);
/* 261 */   public static final PlayerAction BREED = new PlayerAction("BREED", (short)379, 4096);
/* 262 */   public static final PlayerAction PROTECT = new PlayerAction("PROTECT", (short)381, 1);
/* 263 */   public static final PlayerAction GROOM = new PlayerAction("GROOM", (short)398, 4096);
/* 264 */   public static final PlayerAction DIG = new PlayerAction("DIG", (short)144, 1);
/* 265 */   public static final PlayerAction DIG_TO_PILE = new PlayerAction("DIG_TO_PILE", (short)921, 1);
/* 266 */   public static final PlayerAction FLATTEN = new PlayerAction("FLATTEN", (short)150, 19);
/* 267 */   public static final PlayerAction PACK = new PlayerAction("PACK", (short)154, 1);
/* 268 */   public static final PlayerAction PAVE = new PlayerAction("PAVE", (short)155, 17);
/* 269 */   public static final PlayerAction PAVE_CORNER = new PlayerAction("PAVE_CORNER", (short)576, 17);
/* 270 */   public static final PlayerAction DREDGE = new PlayerAction("DREDGE", (short)362, 529);
/* 271 */   public static final PlayerAction PRUNE = new PlayerAction("PRUNE", (short)373, 1537);
/* 272 */   public static final PlayerAction MUTETOOL = new PlayerAction("MUTETOOL", (short)467, 65535);
/* 273 */   public static final PlayerAction GMTOOL = new PlayerAction("GMTOOL", (short)534, 65535);
/* 274 */   public static final PlayerAction PAINT_TERRAIN = new PlayerAction("PAINT_TERRAIN", (short)604, 17);
/* 275 */   public static final PlayerAction LEVEL = new PlayerAction("LEVEL", (short)532, 19);
/* 276 */   public static final PlayerAction FLATTEN_BORDER = new PlayerAction("FLATTEN_BORDER", (short)533, 2);
/* 277 */   public static final PlayerAction ANALYSE = new PlayerAction("ANALYSE", (short)536, 256);
/* 278 */   public static final PlayerAction FORAGE_VEG = new PlayerAction("FORAGE_VEG", (short)569, 1);
/* 279 */   public static final PlayerAction FORAGE_RESOURCE = new PlayerAction("FORAGE_RESOURCE", (short)570, 1);
/* 280 */   public static final PlayerAction FORAGE_BERRIES = new PlayerAction("FORAGE_BERRIES", (short)571, 1);
/* 281 */   public static final PlayerAction BOTANIZE_SEEDS = new PlayerAction("BOTANIZE_SEEDS", (short)572, 1);
/* 282 */   public static final PlayerAction BOTANIZE_HERBS = new PlayerAction("BOTANIZE_HERBS", (short)573, 1);
/* 283 */   public static final PlayerAction BOTANIZE_PLANTS = new PlayerAction("BOTANIZE_PLANTS", (short)574, 1);
/* 284 */   public static final PlayerAction BOTANIZE_RESOURCE = new PlayerAction("BOTANIZE_RESOURCE", (short)575, 1);
/* 285 */   public static final PlayerAction BOTANIZE_SPICES = new PlayerAction("BOTANIZE_SPICES", (short)720, 1);
/* 286 */   public static final PlayerAction DROP_AS_PILE = new PlayerAction("DROP_AS_PILE", (short)638, 256);
/* 287 */   public static final PlayerAction SET_PRICE = new PlayerAction("SET_PRICE", (short)86, 768);
/* 288 */   public static final PlayerAction RENAME = new PlayerAction("RENAME", (short)59, 768);
/* 289 */   public static final PlayerAction TRIM = new PlayerAction("TRIM", (short)644, 1);
/* 290 */   public static final PlayerAction SHEAR = new PlayerAction("SHEAR", (short)646, 4096);
/* 291 */   public static final PlayerAction MILK = new PlayerAction("MILK", (short)345, 4096);
/* 292 */   public static final PlayerAction GATHER = new PlayerAction("GATHER", (short)645, 1);
/* 293 */   public static final PlayerAction SIT_ANY = new PlayerAction("SIT_ANY", (short)701, 512);
/* 294 */   public static final PlayerAction STAND_UP = new PlayerAction("STAND_UP", (short)708, 512);
/*     */   
/* 296 */   public static final PlayerAction CLIMB_UP = new PlayerAction("CLIMB_UP", (short)522, 2048);
/* 297 */   public static final PlayerAction CLIMB_DOWN = new PlayerAction("CLIMB_DOWN", (short)523, 2048);
/*     */   
/* 299 */   public static final PlayerAction BUILD_HOUSE_WALL = new PlayerAction("BUILD_HOUSE_WALL", (short)20000, 2048);
/* 300 */   public static final PlayerAction BUILD_HOUSE_WINDOW = new PlayerAction("BUILD_HOUSE_WINDOW", (short)20001, 2048);
/* 301 */   public static final PlayerAction BUILD_HOUSE_DOOR = new PlayerAction("BUILD_HOUSE_DOOR", (short)20002, 2048);
/*     */   
/* 303 */   public static final PlayerAction EQUIP_ITEM = new PlayerAction("EQUIP", (short)582, 256);
/* 304 */   public static final PlayerAction EQUIP_ITEM_LEFT = new PlayerAction("EQUIP_LEFT", (short)583, 256);
/* 305 */   public static final PlayerAction EQUIP_ITEM_RIGHT = new PlayerAction("EQUIP_RIGHT", (short)584, 256);
/* 306 */   public static final PlayerAction UNEQUIP_ITEM = new PlayerAction("UNEQUIP", (short)585, 256);
/*     */   
/* 308 */   public static final PlayerAction ADD_TO_CRAFTING_WINDOW = new PlayerAction("ADD_TO_CRAFTING_WINDOW", (short)607, 514);
/*     */ 
/*     */   
/* 311 */   public static final PlayerAction PLANT = new PlayerAction("PLANT", (short)186, 1);
/* 312 */   public static final PlayerAction PLANT_CENTER = new PlayerAction("PLANT_CENTER", (short)660, 1);
/* 313 */   public static final PlayerAction PICK = new PlayerAction("PICK", (short)137, 768);
/* 314 */   public static final PlayerAction COLLECT = new PlayerAction("COLLECT", (short)741, 1);
/*     */   
/* 316 */   public static final PlayerAction PLANT_SIGN = new PlayerAction("PLANT_SIGN", (short)176, 65535);
/* 317 */   public static final PlayerAction PLANT_LEFT = new PlayerAction("PLANT_LEFT", (short)746, 65535);
/* 318 */   public static final PlayerAction PLANT_RIGHT = new PlayerAction("PLANT_RIGHT", (short)747, 65535);
/*     */   
/* 320 */   public static final PlayerAction WINCH = new PlayerAction("WINCH", (short)237, 512);
/* 321 */   public static final PlayerAction WINCH5 = new PlayerAction("WINCH5", (short)238, 512);
/* 322 */   public static final PlayerAction WINCH10 = new PlayerAction("WINCH10", (short)239, 512);
/* 323 */   public static final PlayerAction UNWIND = new PlayerAction("UNWIND", (short)235, 512);
/* 324 */   public static final PlayerAction LOAD = new PlayerAction("LOAD", (short)233, 512);
/* 325 */   public static final PlayerAction UNLOAD = new PlayerAction("UNLOAD", (short)234, 512);
/* 326 */   public static final PlayerAction FIRE = new PlayerAction("FIRE", (short)236, 512);
/*     */ 
/*     */   
/* 329 */   public static final PlayerAction BLESS = new PlayerAction("BLESS", (short)245, 13056);
/* 330 */   public static final PlayerAction DISPEL = new PlayerAction("DISPEL", (short)450, 15121);
/* 331 */   public static final PlayerAction DIRT_SPELL = new PlayerAction("DIRT_SPELL", (short)453, 769);
/* 332 */   public static final PlayerAction LOCATE_SOUL = new PlayerAction("LOCATE_SOUL", (short)419, 13073);
/* 333 */   public static final PlayerAction LIGHT_TOKEN = new PlayerAction("LIGHT_TOKEN", (short)421, 15121);
/* 334 */   public static final PlayerAction NOLOCATE = new PlayerAction("NOLOCATE", (short)451, 13056);
/* 335 */   public static final PlayerAction CURE_LIGHT = new PlayerAction("CURE_LIGHT", (short)246, 256);
/* 336 */   public static final PlayerAction CURE_MEDIUM = new PlayerAction("CURE_MEDIUM", (short)247, 256);
/* 337 */   public static final PlayerAction CURE_SERIOUS = new PlayerAction("CURE_SERIOUS", (short)248, 256);
/*     */ 
/*     */   
/* 340 */   public static final PlayerAction HUMID_DRIZZLE = new PlayerAction("HUMID_DRIZZLE", (short)407, 2065);
/* 341 */   public static final PlayerAction WARD = new PlayerAction("WARD", (short)437, 2065);
/* 342 */   public static final PlayerAction WILD_GROWTH = new PlayerAction("WILD_GROWTH", (short)436, 2065);
/* 343 */   public static final PlayerAction LIGHT_OF_FO = new PlayerAction("LIGHT_OF_FO", (short)438, 2065);
/* 344 */   public static final PlayerAction WRATH_OF_MAGRANON = new PlayerAction("WRATH_OF_MAGRANON", (short)441, 2065);
/* 345 */   public static final PlayerAction DISINTEGRATE = new PlayerAction("DISINTEGRATE", (short)449, 16);
/* 346 */   public static final PlayerAction MASS_STAMINA = new PlayerAction("MASS_STAMINA", (short)425, 2065);
/* 347 */   public static final PlayerAction FIRE_PILLAR = new PlayerAction("FIRE_PILLAR", (short)420, 2065);
/* 348 */   public static final PlayerAction MOLE_SENSES = new PlayerAction("MOLE_SENSES", (short)439, 2065);
/* 349 */   public static final PlayerAction STRONGWALL = new PlayerAction("STRONGWALL", (short)440, 16);
/* 350 */   public static final PlayerAction LOCATE_ARTIFACT = new PlayerAction("LOCATE_ARTIFACT", (short)271, 2065);
/* 351 */   public static final PlayerAction TORNADO = new PlayerAction("TORNADO", (short)413, 2065);
/* 352 */   public static final PlayerAction TENTACLES = new PlayerAction("TENTACLES", (short)418, 2065);
/* 353 */   public static final PlayerAction REVEAL_CREATURES = new PlayerAction("REVEAL_CREATURES", (short)444, 2065);
/* 354 */   public static final PlayerAction ICE_PILLAR = new PlayerAction("ICE_PILLAR", (short)414, 2065);
/* 355 */   public static final PlayerAction REVEAL_SETTLEMENTS = new PlayerAction("REVEAL_SETTLEMENTS", (short)443, 2065);
/* 356 */   public static final PlayerAction FUNGUS_TRAP = new PlayerAction("FUNGUS_TRAP", (short)433, 2065);
/* 357 */   public static final PlayerAction PAINRAIN = new PlayerAction("PAINRAIN", (short)432, 2065);
/* 358 */   public static final PlayerAction FUNGUS = new PlayerAction("FUNGUS", (short)446, 2065);
/* 359 */   public static final PlayerAction SCORN_OF_LIBILA = new PlayerAction("SCORN_OF_LIBILA", (short)448, 2065);
/*     */ 
/*     */   
/* 362 */   public static final PlayerAction BEARPAWS = new PlayerAction("BEARPAWS", (short)406, 12544);
/* 363 */   public static final PlayerAction WILLOWSPINE = new PlayerAction("WILLOWSPINE", (short)405, 12544);
/* 364 */   public static final PlayerAction SIXTH_SENSE = new PlayerAction("SIXTH_SENSE", (short)376, 12544);
/* 365 */   public static final PlayerAction MORNING_FOG = new PlayerAction("MORNING_FOG", (short)282, 12544);
/* 366 */   public static final PlayerAction CHARM = new PlayerAction("CHARM", (short)275, 12288);
/* 367 */   public static final PlayerAction REFRESH_SPELL = new PlayerAction("REFRESH_SPELL", (short)250, 12544);
/* 368 */   public static final PlayerAction OAKSHELL = new PlayerAction("OAKSHELL", (short)404, 12544);
/* 369 */   public static final PlayerAction FOREST_GIANT_STRENGTH = new PlayerAction("FOREST_GIANT_STRENGTH", (short)410, 12544);
/* 370 */   public static final PlayerAction TANGLEWEAVE = new PlayerAction("TANGLEWEAVE", (short)641, 12288);
/* 371 */   public static final PlayerAction GENESIS = new PlayerAction("GENESIS", (short)408, 12288);
/* 372 */   public static final PlayerAction HEAL_SPELL = new PlayerAction("HEAL_SPELL", (short)249, 12544);
/* 373 */   public static final PlayerAction FRANTIC_CHARGE = new PlayerAction("FRANTIC_CHARGE", (short)423, 12544);
/* 374 */   public static final PlayerAction FIRE_HEART = new PlayerAction("FIRE_HEART", (short)424, 12288);
/* 375 */   public static final PlayerAction DOMINATE = new PlayerAction("DOMINATE", (short)274, 12288);
/* 376 */   public static final PlayerAction SMITE = new PlayerAction("SMITE", (short)252, 12288);
/* 377 */   public static final PlayerAction GOAT_SHAPE = new PlayerAction("GOAT_SHAPE", (short)422, 12544);
/* 378 */   public static final PlayerAction SHARD_OF_ICE = new PlayerAction("SHARD_OF_ICE", (short)485, 12288);
/* 379 */   public static final PlayerAction EXCEL = new PlayerAction("EXCEL", (short)442, 12544);
/* 380 */   public static final PlayerAction WISDOM_OF_VYNORA = new PlayerAction("WISDOM_OF_VYNORA", (short)445, 12544);
/* 381 */   public static final PlayerAction DRAIN_HEALTH = new PlayerAction("DRAIN_HEALTH", (short)255, 12288);
/* 382 */   public static final PlayerAction PHANTASMS = new PlayerAction("PHANTASMS", (short)426, 12288);
/* 383 */   public static final PlayerAction ROTTING_GUT = new PlayerAction("ROTTING_GUT", (short)428, 12288);
/* 384 */   public static final PlayerAction WEAKNESS = new PlayerAction("WEAKNESS", (short)429, 12288);
/* 385 */   public static final PlayerAction TRUEHIT = new PlayerAction("TRUEHIT", (short)447, 12544);
/* 386 */   public static final PlayerAction HELL_STRENGTH = new PlayerAction("HELL_STRENGTH", (short)427, 12544);
/* 387 */   public static final PlayerAction DRAIN_STAMINA = new PlayerAction("DRAIN_STAMINA", (short)254, 12288);
/* 388 */   public static final PlayerAction WORM_BRAINS = new PlayerAction("WORM_BRAINS", (short)430, 12288);
/*     */ 
/*     */   
/* 391 */   public static final PlayerAction LIBILAS_DEMISE = new PlayerAction("LIBILAS_DEMISE", (short)262, 768);
/* 392 */   public static final PlayerAction LURKER_IN_THE_WOODS = new PlayerAction("LURKER_IN_THE_WOODS", (short)458, 768);
/* 393 */   public static final PlayerAction LIFETRANSFER = new PlayerAction("LIFETRANSFER", (short)409, 768);
/* 394 */   public static final PlayerAction VESSEL = new PlayerAction("VESSEL", (short)272, 768);
/* 395 */   public static final PlayerAction DARK_MESSENGER = new PlayerAction("DARK_MESSENGER", (short)339, 768);
/* 396 */   public static final PlayerAction COURIER = new PlayerAction("COURIER", (short)338, 768);
/* 397 */   public static final PlayerAction VENOM = new PlayerAction("VENOM", (short)412, 768);
/* 398 */   public static final PlayerAction BREAK_ALTAR = new PlayerAction("BREAK_ALTAR", (short)258, 768);
/* 399 */   public static final PlayerAction FOS_TOUCH = new PlayerAction("FOS_TOUCH", (short)263, 768);
/* 400 */   public static final PlayerAction FOS_DEMISE = new PlayerAction("FOS_DEMISE", (short)259, 768);
/* 401 */   public static final PlayerAction DRAGONS_DEMISE = new PlayerAction("DRAGONS_DEMISE", (short)270, 768);
/* 402 */   public static final PlayerAction VYNORAS_DEMISE = new PlayerAction("VYNORAS_DEMISE", (short)261, 768);
/* 403 */   public static final PlayerAction MAGRANONS_SHIELD = new PlayerAction("MAGRANONS_SHIELD", (short)264, 768);
/* 404 */   public static final PlayerAction FLAMING_AURA = new PlayerAction("FLAMING_AURA", (short)277, 768);
/* 405 */   public static final PlayerAction LURKER_IN_THE_DARK = new PlayerAction("LURKER_IN_THE_DARK", (short)459, 768);
/* 406 */   public static final PlayerAction SELFHEALERS_DEMISE = new PlayerAction("SELFHEALERS_DEMISE", (short)268, 768);
/* 407 */   public static final PlayerAction AURA_OF_SHARED_PAIN = new PlayerAction("AURA_OF_SHARED_PAIN", (short)278, 768);
/* 408 */   public static final PlayerAction SUNDER = new PlayerAction("SUNDER", (short)253, 768);
/* 409 */   public static final PlayerAction ANIMALS_DEMISE = new PlayerAction("ANIMALS_DEMISE", (short)269, 768);
/* 410 */   public static final PlayerAction CIRCLE_OF_CUNNING = new PlayerAction("CIRCLE_OF_CUNNING", (short)276, 768);
/* 411 */   public static final PlayerAction OPULENCE = new PlayerAction("OPULENCE", (short)280, 768);
/* 412 */   public static final PlayerAction FROSTBRAND = new PlayerAction("FROSTBRAND", (short)417, 768);
/* 413 */   public static final PlayerAction MIND_STEALER = new PlayerAction("MIND_STEALER", (short)415, 768);
/* 414 */   public static final PlayerAction LURKER_IN_THE_DEEP = new PlayerAction("LURKER_IN_THE_DEEP", (short)457, 768);
/* 415 */   public static final PlayerAction VYNORAS_HAND = new PlayerAction("VYNORAS_HAND", (short)265, 768);
/* 416 */   public static final PlayerAction MEND = new PlayerAction("MEND", (short)251, 768);
/* 417 */   public static final PlayerAction WIND_OF_AGES = new PlayerAction("WIND_OF_AGES", (short)279, 768);
/* 418 */   public static final PlayerAction NIMBLENESS = new PlayerAction("NIMBLENESS", (short)416, 768);
/* 419 */   public static final PlayerAction HUMANS_DEMISE = new PlayerAction("HUMANS_DEMISE", (short)267, 768);
/* 420 */   public static final PlayerAction ROTTING_TOUCH = new PlayerAction("ROTTING_TOUCH", (short)281, 768);
/* 421 */   public static final PlayerAction MAGRANONS_DEMISE = new PlayerAction("MAGRANONS_DEMISE", (short)260, 768);
/* 422 */   public static final PlayerAction BLOODTHIRST = new PlayerAction("BLOODTHIRST", (short)454, 768);
/* 423 */   public static final PlayerAction WEB_ARMOUR = new PlayerAction("WEB_ARMOUR", (short)455, 768);
/* 424 */   public static final PlayerAction BLESSINGS_OF_THE_DARK = new PlayerAction("BLESSINGS_OF_THE_DARK", (short)456, 768);
/* 425 */   public static final PlayerAction LIBILAS_SHIELDING = new PlayerAction("LIBILAS_SHIELDING", (short)266, 768);
/* 426 */   public static final PlayerAction REBIRTH = new PlayerAction("REBIRTH", (short)273, 768);
/*     */ 
/*     */   
/* 429 */   public static final PlayerAction SHOOT = new PlayerAction("SHOOT", (short)124, 12288);
/* 430 */   public static final PlayerAction QUICK_SHOT = new PlayerAction("QUICK_SHOT", (short)125, 12288);
/* 431 */   public static final PlayerAction SHOOT_TORSO = new PlayerAction("SHOOT_TORSO", (short)128, 12288);
/* 432 */   public static final PlayerAction SHOOT_LEFTARM = new PlayerAction("SHOOT_LEFTARM", (short)129, 12288);
/* 433 */   public static final PlayerAction SHOOT_RIGHTARM = new PlayerAction("SHOOT_RIGHTARM", (short)130, 12288);
/* 434 */   public static final PlayerAction SHOOT_HEAD = new PlayerAction("SHOOT_HEAD", (short)126, 12288);
/* 435 */   public static final PlayerAction SHOOT_FACE = new PlayerAction("SHOOT_FACE", (short)127, 12288);
/* 436 */   public static final PlayerAction SHOOT_LEGS = new PlayerAction("SHOOT_LEGS", (short)131, 12288);
/*     */   
/* 438 */   public static final PlayerAction INVESTIGATE = new PlayerAction("INVESTIGATE", (short)910, 1);
/* 439 */   public static final PlayerAction IDENTIFY = new PlayerAction("IDENTIFY", (short)911, 768);
/* 440 */   public static final PlayerAction COMBINE_FRAGMENT = new PlayerAction("COMBINE_FRAGMENT", (short)912, 768);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\PlayerAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */