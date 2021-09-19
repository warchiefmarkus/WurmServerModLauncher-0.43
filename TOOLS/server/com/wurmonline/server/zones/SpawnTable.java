/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class SpawnTable
/*     */   implements CreatureTemplateIds
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(SpawnTable.class.getName());
/*  38 */   private static final Set<EncounterType> land = new HashSet<>();
/*  39 */   private static final Set<EncounterType> water = new HashSet<>();
/*  40 */   private static final Set<EncounterType> beach = new HashSet<>();
/*  41 */   private static final Set<EncounterType> deepwater = new HashSet<>();
/*  42 */   private static final Set<EncounterType> flying = new HashSet<>();
/*  43 */   private static final Set<EncounterType> flyinghigh = new HashSet<>();
/*  44 */   private static final Set<EncounterType> caves = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addTileType(EncounterType type) {
/*  55 */     if (type.getElev() == 0) {
/*  56 */       land.add(type);
/*  57 */     } else if (type.getElev() == 2) {
/*  58 */       deepwater.add(type);
/*  59 */     } else if (type.getElev() == 1) {
/*  60 */       water.add(type);
/*  61 */     } else if (type.getElev() == 3) {
/*  62 */       flying.add(type);
/*  63 */     } else if (type.getElev() == 4) {
/*  64 */       flyinghigh.add(type);
/*  65 */     } else if (type.getElev() == -1) {
/*  66 */       caves.add(type);
/*  67 */     } else if (type.getElev() == 5) {
/*  68 */       beach.add(type);
/*     */     } else {
/*  70 */       logger.warning("Cannot add unknown EncounterType: " + type);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static EncounterType getType(byte tiletype, byte elevation) {
/*  75 */     Set<EncounterType> types = land;
/*  76 */     if (elevation == 2) {
/*  77 */       types = deepwater;
/*  78 */     } else if (elevation == 1) {
/*  79 */       types = water;
/*  80 */     } else if (elevation == 3) {
/*  81 */       types = flying;
/*  82 */     } else if (elevation == 4) {
/*  83 */       types = flyinghigh;
/*  84 */     } else if (elevation == -1) {
/*  85 */       types = caves;
/*  86 */     } else if (elevation == 5) {
/*  87 */       types = beach;
/*  88 */     }  for (EncounterType type : types) {
/*     */       
/*  90 */       if (type.getTiletype() == tiletype)
/*     */       {
/*  92 */         return type;
/*     */       }
/*     */     } 
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Encounter getRandomEncounter(byte tiletype, byte elevation) {
/* 100 */     EncounterType enc = getType(tiletype, elevation);
/*     */     
/* 102 */     if (enc != null) {
/*     */       
/* 104 */       Encounter t = enc.getRandomEncounter();
/*     */       
/* 106 */       if (t == EncounterType.NULL_ENCOUNTER) {
/* 107 */         return null;
/*     */       }
/* 109 */       return t;
/*     */     } 
/*     */     
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static void createEncounters() {
/* 117 */     logger.info("Creating Encounters");
/* 118 */     long now = System.nanoTime();
/*     */     
/* 120 */     Encounter cow = new Encounter();
/* 121 */     cow.addType(3, 1);
/*     */     
/* 123 */     Encounter sheep = new Encounter();
/* 124 */     sheep.addType(96, 1);
/* 125 */     Encounter ram = new Encounter();
/* 126 */     sheep.addType(102, 1);
/* 127 */     Encounter anaconda = new Encounter();
/* 128 */     anaconda.addType(38, 1);
/*     */     
/* 130 */     Encounter horse = new Encounter();
/* 131 */     horse.addType(64, 2);
/*     */     
/* 133 */     Encounter wolf = new Encounter();
/* 134 */     wolf.addType(10, 4);
/*     */     
/* 136 */     Encounter bearbrown = new Encounter();
/* 137 */     bearbrown.addType(12, 2);
/*     */     
/* 139 */     Encounter bearblack = new Encounter();
/* 140 */     bearblack.addType(42, 2);
/*     */     
/* 142 */     Encounter rat = new Encounter();
/* 143 */     rat.addType(13, 3);
/*     */     
/* 145 */     Encounter mountainlion = new Encounter();
/* 146 */     mountainlion.addType(14, 2);
/*     */     
/* 148 */     Encounter wildcat = new Encounter();
/* 149 */     wildcat.addType(15, 2);
/*     */     
/* 151 */     Encounter cavebugs = new Encounter();
/* 152 */     cavebugs.addType(43, 5);
/*     */     
/* 154 */     Encounter pig = new Encounter();
/* 155 */     pig.addType(44, 3);
/*     */     
/* 157 */     Encounter deer = new Encounter();
/* 158 */     deer.addType(54, 2);
/*     */     
/* 160 */     Encounter bison = new Encounter();
/* 161 */     bison.addType(82, 10);
/*     */     
/* 163 */     Encounter bull = new Encounter();
/* 164 */     bull.addType(49, 3);
/*     */     
/* 166 */     Encounter calf = new Encounter();
/* 167 */     calf.addType(50, 1);
/*     */     
/* 169 */     Encounter hen = new Encounter();
/* 170 */     hen.addType(45, 3);
/*     */     
/* 172 */     Encounter rooster = new Encounter();
/* 173 */     rooster.addType(52, 1);
/*     */     
/* 175 */     Encounter pheasant = new Encounter();
/* 176 */     pheasant.addType(55, 2);
/*     */     
/* 178 */     Encounter dog = new Encounter();
/* 179 */     dog.addType(51, 2);
/*     */     
/* 181 */     Encounter spider = new Encounter();
/* 182 */     spider.addType(25, 6);
/*     */     
/* 184 */     Encounter lavaSpiderMulti = new Encounter();
/* 185 */     lavaSpiderMulti.addType(56, 10);
/*     */     
/* 187 */     Encounter lavaSpiderSingle = new Encounter();
/* 188 */     lavaSpiderSingle.addType(56, 1);
/*     */     
/* 190 */     Encounter scorpionSingle = new Encounter();
/* 191 */     scorpionSingle.addType(59, 3);
/*     */     
/* 193 */     Encounter crocodileSingle = new Encounter();
/* 194 */     crocodileSingle.addType(58, 1);
/*     */     
/* 196 */     Encounter lavaCreatureSingle = new Encounter();
/* 197 */     lavaCreatureSingle.addType(57, 1);
/*     */     
/* 199 */     Encounter trollSingle = new Encounter();
/* 200 */     trollSingle.addType(11, 1);
/*     */     
/* 202 */     Encounter hellHorseSingle = new Encounter();
/* 203 */     hellHorseSingle.addType(83, 1);
/*     */     
/* 205 */     Encounter hellHoundSingle = new Encounter();
/* 206 */     hellHoundSingle.addType(84, 1);
/*     */     
/* 208 */     Encounter hellScorpSingle = new Encounter();
/* 209 */     hellScorpSingle.addType(85, 1);
/*     */     
/* 211 */     Encounter sealPair = new Encounter();
/* 212 */     sealPair.addType(93, 2);
/*     */     
/* 214 */     Encounter crabSingle = new Encounter();
/* 215 */     crabSingle.addType(95, 1);
/*     */     
/* 217 */     Encounter tortoiseSingle = new Encounter();
/* 218 */     tortoiseSingle.addType(94, 1);
/*     */     
/* 220 */     Encounter uttacha = new Encounter();
/* 221 */     uttacha.addType(74, 1);
/* 222 */     Encounter eagleSpirit = new Encounter();
/* 223 */     eagleSpirit.addType(77, 1);
/* 224 */     Encounter crawler = new Encounter();
/* 225 */     crawler.addType(73, 1);
/* 226 */     Encounter nogump = new Encounter();
/* 227 */     nogump.addType(75, 1);
/* 228 */     Encounter drakeSpirit = new Encounter();
/* 229 */     drakeSpirit.addType(76, 1);
/* 230 */     Encounter demonsol = new Encounter();
/* 231 */     demonsol.addType(72, 1);
/*     */     
/* 233 */     Encounter unicorn = new Encounter();
/* 234 */     unicorn.addType(21, 1);
/*     */     
/* 236 */     EncounterType grassGround = new EncounterType(Tiles.Tile.TILE_GRASS.id, (byte)0);
/* 237 */     grassGround.addEncounter(cow, 1);
/* 238 */     grassGround.addEncounter(wildcat, 2);
/* 239 */     grassGround.addEncounter(dog, 3);
/*     */     
/* 241 */     grassGround.addEncounter(hen, 1);
/* 242 */     grassGround.addEncounter(rooster, 1);
/* 243 */     grassGround.addEncounter(calf, 1);
/* 244 */     grassGround.addEncounter(bull, 1);
/* 245 */     grassGround.addEncounter(pheasant, 1);
/* 246 */     grassGround.addEncounter(horse, 2);
/* 247 */     grassGround.addEncounter(sheep, 3);
/* 248 */     grassGround.addEncounter(ram, 1);
/* 249 */     grassGround.addEncounter(unicorn, 1);
/* 250 */     addTileType(grassGround);
/*     */     
/* 252 */     EncounterType beachSide = new EncounterType(Tiles.Tile.TILE_SAND.id, (byte)5);
/* 253 */     beachSide.addEncounter(crabSingle, 8);
/* 254 */     beachSide.addEncounter(tortoiseSingle, 1);
/* 255 */     beachSide.addEncounter(sealPair, 3);
/* 256 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 258 */       beachSide.addEncounter(crawler, 1);
/* 259 */       beachSide.addEncounter(uttacha, 1);
/*     */     } 
/* 261 */     addTileType(beachSide);
/*     */     
/* 263 */     EncounterType rockSide = new EncounterType(Tiles.Tile.TILE_ROCK.id, (byte)1);
/* 264 */     rockSide.addEncounter(rat, 2);
/* 265 */     rockSide.addEncounter(sealPair, 2);
/* 266 */     rockSide.addEncounter(lavaCreatureSingle, 1);
/* 267 */     rockSide.addEncounter(EncounterType.NULL_ENCOUNTER, 5);
/* 268 */     if (Servers.localServer.isChallengeServer())
/* 269 */       rockSide.addEncounter(uttacha, 1); 
/* 270 */     addTileType(rockSide);
/*     */     
/* 272 */     EncounterType mycGround = new EncounterType(Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/* 273 */     mycGround.addEncounter(spider, 4);
/* 274 */     mycGround.addEncounter(rat, 2);
/* 275 */     mycGround.addEncounter(dog, 1);
/* 276 */     mycGround.addEncounter(wolf, 1);
/* 277 */     mycGround.addEncounter(unicorn, 1);
/* 278 */     mycGround.addEncounter(hellHorseSingle, 1);
/* 279 */     mycGround.addEncounter(hellHoundSingle, 1);
/* 280 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 282 */       mycGround.addEncounter(demonsol, 1);
/* 283 */       mycGround.addEncounter(crawler, 1);
/*     */     } 
/* 285 */     addTileType(mycGround);
/*     */     
/* 287 */     EncounterType marsh = new EncounterType(Tiles.Tile.TILE_MARSH.id, (byte)0);
/* 288 */     marsh.addEncounter(rat, 2);
/* 289 */     marsh.addEncounter(anaconda, 1);
/* 290 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 292 */       marsh.addEncounter(nogump, 1);
/* 293 */       marsh.addEncounter(demonsol, 1);
/*     */     } 
/* 295 */     addTileType(marsh);
/*     */     
/* 297 */     EncounterType steppe = new EncounterType(Tiles.Tile.TILE_STEPPE.id, (byte)0);
/* 298 */     steppe.addEncounter(pheasant, 1);
/* 299 */     steppe.addEncounter(horse, 4);
/* 300 */     steppe.addEncounter(wildcat, 1);
/* 301 */     steppe.addEncounter(hellHorseSingle, 1);
/* 302 */     steppe.addEncounter(bison, 1);
/* 303 */     steppe.addEncounter(sheep, 1);
/* 304 */     steppe.addEncounter(ram, 1);
/* 305 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 307 */       steppe.addEncounter(drakeSpirit, 1);
/* 308 */       steppe.addEncounter(eagleSpirit, 1);
/*     */     } 
/* 310 */     addTileType(steppe);
/*     */     
/* 312 */     EncounterType treeGround = new EncounterType(Tiles.Tile.TILE_TREE.id, (byte)0);
/* 313 */     treeGround.addEncounter(pig, 1);
/* 314 */     treeGround.addEncounter(wolf, 1);
/* 315 */     treeGround.addEncounter(bearbrown, 1);
/*     */     
/* 317 */     treeGround.addEncounter(hellHoundSingle, 1);
/* 318 */     treeGround.addEncounter(pheasant, 1);
/* 319 */     treeGround.addEncounter(deer, 1);
/* 320 */     treeGround.addEncounter(spider, 2);
/* 321 */     treeGround.addEncounter(trollSingle, 1);
/*     */     
/* 323 */     treeGround.addEncounter(mountainlion, 1);
/* 324 */     if (Servers.localServer.isChallengeServer()) {
/*     */       
/* 326 */       treeGround.addEncounter(demonsol, 1);
/* 327 */       treeGround.addEncounter(crawler, 1);
/*     */     } 
/* 329 */     addTileType(treeGround);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     EncounterType sandGround = new EncounterType(Tiles.Tile.TILE_SAND.id, (byte)0);
/* 338 */     sandGround.addEncounter(crocodileSingle, 10);
/* 339 */     sandGround.addEncounter(scorpionSingle, 10);
/* 340 */     sandGround.addEncounter(hellScorpSingle, 1);
/* 341 */     sandGround.addEncounter(anaconda, 1);
/* 342 */     addTileType(sandGround);
/*     */     
/* 344 */     EncounterType clayGround = new EncounterType(Tiles.Tile.TILE_CLAY.id, (byte)0);
/* 345 */     clayGround.addEncounter(crocodileSingle, 10);
/* 346 */     clayGround.addEncounter(anaconda, 1);
/* 347 */     addTileType(clayGround);
/*     */     
/* 349 */     EncounterType underGround = new EncounterType(Tiles.Tile.TILE_CAVE.id, (byte)-1);
/* 350 */     underGround.addEncounter(bearblack, 4);
/* 351 */     underGround.addEncounter(rat, 2);
/* 352 */     underGround.addEncounter(cavebugs, 5);
/* 353 */     underGround.addEncounter(spider, 2);
/* 354 */     underGround.addEncounter(lavaSpiderSingle, 2);
/* 355 */     underGround.addEncounter(lavaCreatureSingle, 4);
/* 356 */     underGround.addEncounter(mountainlion, 1);
/*     */     
/* 358 */     addTileType(underGround);
/*     */     
/* 360 */     EncounterType lavaGround = new EncounterType(Tiles.Tile.TILE_LAVA.id, (byte)0);
/* 361 */     lavaGround.addEncounter(lavaSpiderMulti, 10);
/* 362 */     lavaGround.addEncounter(lavaCreatureSingle, 10);
/* 363 */     addTileType(lavaGround);
/*     */     
/* 365 */     EncounterType lavaRock = new EncounterType(Tiles.Tile.TILE_LAVA.id, (byte)-1);
/* 366 */     lavaRock.addEncounter(lavaCreatureSingle, 10);
/* 367 */     addTileType(lavaRock);
/*     */     
/* 369 */     logger.log(Level.INFO, "Created Encounters. It took " + ((float)(System.nanoTime() - now) / 1000000.0F) + " ms.");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\SpawnTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */