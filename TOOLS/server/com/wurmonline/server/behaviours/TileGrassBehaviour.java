/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.GrassData;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.items.RuneUtilities;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.skills.NoSuchSkillException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Trap;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TileGrassBehaviour
/*     */   extends TileBehaviour
/*     */ {
/*  67 */   private static final Logger logger = Logger.getLogger(TileBehaviour.class.getName());
/*  68 */   private static final Map<Integer, Byte> flowers = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  72 */     flowers.put(Integer.valueOf(498), Byte.valueOf((byte)1));
/*  73 */     flowers.put(Integer.valueOf(499), Byte.valueOf((byte)2));
/*  74 */     flowers.put(Integer.valueOf(500), Byte.valueOf((byte)3));
/*  75 */     flowers.put(Integer.valueOf(501), Byte.valueOf((byte)4));
/*  76 */     flowers.put(Integer.valueOf(502), Byte.valueOf((byte)5));
/*  77 */     flowers.put(Integer.valueOf(503), Byte.valueOf((byte)6));
/*  78 */     flowers.put(Integer.valueOf(504), Byte.valueOf((byte)7));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TileGrassBehaviour() {
/*  86 */     super((short)8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int getFlowerTypeFor(GrassData.FlowerType flowerType) {
/*  92 */     for (Map.Entry<Integer, Byte> entry : flowers.entrySet()) {
/*     */       
/*  94 */       if (((Byte)entry.getValue()).byteValue() == flowerType.getEncodedData())
/*  95 */         return ((Integer)entry.getKey()).intValue(); 
/*     */     } 
/*  97 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   static byte getDataForFlower(int flowerTemplate) {
/* 102 */     Byte b = flowers.get(Integer.valueOf(flowerTemplate));
/* 103 */     if (b == null)
/* 104 */       return 0; 
/* 105 */     return b.byteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   ActionEntry getGrassBehaviour(int tilex, int tiley, int tile, GrassData.GrowthStage growthStage) {
/* 110 */     byte tileType = Tiles.decodeType(tile);
/*     */     
/* 112 */     String actionString = "Gather";
/* 113 */     if (tileType == Tiles.Tile.TILE_GRASS.id && growthStage == GrassData.GrowthStage.SHORT)
/* 114 */       return Actions.actionEntrys[644]; 
/* 115 */     if (tileType == Tiles.Tile.TILE_GRASS.id) {
/* 116 */       actionString = "Cut grass";
/* 117 */     } else if (tileType == Tiles.Tile.TILE_REED.id) {
/* 118 */       actionString = "Gather reed";
/* 119 */     } else if (tileType == Tiles.Tile.TILE_KELP.id) {
/* 120 */       actionString = "Gather kelp";
/*     */     } 
/* 122 */     return new ActionEntry((short)645, actionString, "gathering", new int[] { 43 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/* 131 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 132 */     toReturn.addAll(super.getBehavioursFor(performer, tilex, tiley, onSurface, tile));
/* 133 */     if (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id) {
/*     */       
/* 135 */       byte tileType = Tiles.decodeType(tile);
/* 136 */       byte tileData = Tiles.decodeData(tile);
/* 137 */       boolean canCollect = TileBehaviour.canCollectSnow(performer, tilex, tiley, tileType, tileData);
/*     */       
/* 139 */       int sz = -2;
/* 140 */       boolean enchant = false;
/* 141 */       if (performer.getCultist() != null && performer.getCultist().mayEnchantNature()) {
/*     */         
/* 143 */         sz--;
/* 144 */         enchant = true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 149 */       toReturn.add(new ActionEntry((short)sz, "Nature", "nature", emptyIntArr));
/* 150 */       toReturn.addAll(getBehavioursForForage(performer));
/* 151 */       toReturn.addAll(getBehavioursForBotanize(performer));
/*     */       
/* 153 */       if (enchant) {
/* 154 */         toReturn.add(Actions.actionEntrys[388]);
/*     */       }
/*     */       
/* 157 */       if (canCollect)
/* 158 */         toReturn.add(Actions.actionEntrys[741]); 
/*     */     } 
/* 160 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/* 167 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 168 */     toReturn.addAll(super.getBehavioursFor(performer, subject, tilex, tiley, onSurface, tile));
/* 169 */     List<ActionEntry> nature = new LinkedList<>();
/*     */     
/* 171 */     byte tileType = Tiles.decodeType(tile);
/* 172 */     byte tileData = Tiles.decodeData(tile);
/* 173 */     boolean isGrassTile = Tiles.isGrassType(tileType);
/*     */     
/* 175 */     if (tileType == Tiles.Tile.TILE_MYCELIUM.id) {
/*     */       
/* 177 */       if (subject.getTemplateId() == 394 || subject.getTemplateId() == 176) {
/* 178 */         nature.add(new ActionEntry((short)644, "Trim mycelium", "Triming"));
/*     */       }
/* 180 */       if (subject.getTemplateId() == 266)
/*     */       {
/* 182 */         nature.add(Actions.actionEntrys[186]);
/* 183 */         nature.add(Actions.actionEntrys[660]);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 188 */     else if (isGrassTile && tileType != Tiles.Tile.TILE_LAWN.id) {
/*     */       
/* 190 */       if (subject.getTemplateId() == 266) {
/*     */         
/* 192 */         nature.add(Actions.actionEntrys[186]);
/* 193 */         nature.add(Actions.actionEntrys[660]);
/*     */       }
/* 195 */       else if (subject.isFlower()) {
/* 196 */         toReturn.add(new ActionEntry((short)186, "Plant Flowers", "planting"));
/* 197 */       }  GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile));
/*     */       
/* 199 */       if (growthStage != GrassData.GrowthStage.SHORT && subject.getTemplate().isSharp()) {
/* 200 */         nature.add(getGrassBehaviour(tilex, tiley, tile, growthStage));
/*     */       }
/* 202 */       if (growthStage == GrassData.GrowthStage.SHORT && tileType == Tiles.Tile.TILE_GRASS.id)
/*     */       {
/* 204 */         if (subject.getTemplateId() == 394 || (subject
/* 205 */           .getTemplateId() == 176 && performer.getPower() >= 2))
/* 206 */           nature.add(getGrassBehaviour(tilex, tiley, tile, growthStage)); 
/*     */       }
/* 208 */       if (subject.getTemplateId() == 267 || (subject
/* 209 */         .getTemplateId() == 176 && performer.getPower() >= 2))
/*     */       {
/* 211 */         if (GrassData.FlowerType.decodeTileData(tileData) != GrassData.FlowerType.NONE && tileType == Tiles.Tile.TILE_GRASS.id)
/* 212 */           nature.add(new ActionEntry((short)187, "Pick flowers", "picking", emptyIntArr)); 
/*     */       }
/* 214 */       if (subject.getTemplateId() == 176 && performer.getPower() >= 2) {
/* 215 */         nature.add(new ActionEntry((short)118, "Grow trees", "growing"));
/*     */       }
/* 217 */       if (tileType == Tiles.Tile.TILE_GRASS.id)
/*     */       {
/* 219 */         if (performer.getCultist() != null && performer.getCultist().mayEnchantNature()) {
/* 220 */           nature.add(Actions.actionEntrys[388]);
/*     */         }
/*     */       }
/*     */       
/* 224 */       if (subject.getTemplateId() == 526 && (performer.getKingdomTemplateId() == 3 || 
/* 225 */         GrassData.FlowerType.decodeTileData(tileData) == GrassData.FlowerType.NONE)) {
/* 226 */         nature.add(Actions.actionEntrys[118]);
/*     */       }
/* 228 */       byte data = Tiles.decodeData(tile);
/* 229 */       if (canCollectSnow(performer, tilex, tiley, tileType, data) && subject.getTemplateId() == 204)
/*     */       {
/* 231 */         toReturn.add(new ActionEntry((short)148, "Build snowman", "creating", emptyIntArr));
/*     */       }
/*     */     } 
/* 234 */     if (tileType != Tiles.Tile.TILE_LAWN.id) {
/* 235 */       toReturn.addAll(getNatureMenu(performer, tilex, tiley, tileType, tileData, nature));
/*     */     }
/* 237 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/* 244 */     boolean done = true;
/* 245 */     byte tileType = Tiles.decodeType(tile);
/* 246 */     String tileName = Tiles.getTile(tileType).getName().toLowerCase();
/*     */     
/* 248 */     if (action == 1) {
/*     */       
/* 250 */       Communicator comm = performer.getCommunicator();
/* 251 */       if (Tiles.isGrassType(tileType)) {
/*     */         
/* 253 */         if (Tiles.decodeType(tile) == Tiles.Tile.TILE_LAWN.id)
/*     */         {
/* 255 */           comm.sendNormalServerMessage("You see a patch of well maintained lawn.");
/*     */         }
/*     */         else
/*     */         {
/* 259 */           comm.sendNormalServerMessage(
/* 260 */               StringUtil.format("You see a patch of %s. The %s is %s and seems to like it here.", new Object[] {
/*     */                   
/* 262 */                   tileName, tileName, StringUtil.toLowerCase(GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)))
/*     */                 }));
/*     */         }
/*     */       
/*     */       }
/* 267 */       else if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MYCELIUM.id) {
/*     */         
/* 269 */         comm.sendNormalServerMessage("You see large entwined fungus roots on rotten grass. " + 
/*     */             
/* 271 */             StringUtil.format("The %s is %s and seems to thrive here.", new Object[] {
/*     */                 
/* 273 */                 tileName, StringUtil.toLowerCase(GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)))
/*     */               }));
/*     */       }
/*     */       else {
/*     */         
/* 278 */         comm.sendNormalServerMessage("You see large entwined fungus roots on rotten lawn.");
/*     */       } 
/*     */       
/* 281 */       sendVillageString(performer, tilex, tiley, true);
/* 282 */       sendTileTransformationState(performer, tilex, tiley, tileType);
/*     */       
/* 284 */       Trap t = Trap.getTrap(tilex, tiley, performer.getLayer());
/* 285 */       if (performer.getPower() > 3)
/*     */       {
/* 287 */         comm.sendNormalServerMessage("Your rot: " + Creature.normalizeAngle(performer.getStatus().getRotation()) + ", Wind rot=" + 
/* 288 */             Server.getWeather().getWindRotation() + ", pow=" + Server.getWeather().getWindPower() + " x=" + 
/* 289 */             Server.getWeather().getXWind() + ", y=" + Server.getWeather().getYWind());
/* 290 */         comm.sendNormalServerMessage("Tile is spring=" + Zone.hasSpring(tilex, tiley));
/* 291 */         if (performer.getPower() >= 5)
/* 292 */           comm.sendNormalServerMessage("tilex: " + tilex + ", tiley=" + tiley); 
/* 293 */         if (t != null) {
/*     */           
/* 295 */           String villageName = "none";
/* 296 */           if (t.getVillage() > 0) {
/*     */             
/*     */             try {
/*     */               
/* 300 */               villageName = Villages.getVillage(t.getVillage()).getName();
/*     */             }
/* 302 */             catch (NoSuchVillageException noSuchVillageException) {}
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 307 */           comm.sendNormalServerMessage("A " + t.getName() + ", ql=" + t.getQualityLevel() + " kingdom=" + 
/* 308 */               Kingdoms.getNameFor(t.getKingdom()) + ", vill=" + villageName + ", rotdam=" + t.getRotDamage() + " firedam=" + t
/* 309 */               .getFireDamage() + " speed=" + t.getSpeedBon());
/*     */         } 
/* 311 */         if (Tiles.isGrassType(tileType))
/*     */         {
/* 313 */           int tileData = Tiles.decodeData(tile);
/* 314 */           comm.sendNormalServerMessage("Type: " + Tiles.decodeType(tile) + " data=" + tileData);
/* 315 */           comm.sendNormalServerMessage("Grass is at: " + 
/* 316 */               GrassData.GrowthStage.decodeTileData(tileData).toString().toLowerCase() + " " + tileType + ", flowers: " + 
/*     */               
/* 318 */               GrassData.FlowerType.decodeTileData(tileData).toString().toLowerCase());
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 323 */       else if (t != null)
/*     */       {
/* 325 */         if (t.getKingdom() == performer.getKingdomId() || performer.getDetectDangerBonus() > 0.0F)
/*     */         {
/* 327 */           String qlString = "average";
/* 328 */           if (t.getQualityLevel() < 20) {
/* 329 */             qlString = "low";
/* 330 */           } else if (t.getQualityLevel() > 80) {
/* 331 */             qlString = "deadly";
/* 332 */           } else if (t.getQualityLevel() > 50) {
/* 333 */             qlString = "high";
/* 334 */           }  String villageName = ".";
/* 335 */           if (t.getVillage() > 0) {
/*     */             
/*     */             try {
/*     */               
/* 339 */               villageName = " of " + Villages.getVillage(t.getVillage()).getName() + ".";
/*     */             }
/* 341 */             catch (NoSuchVillageException noSuchVillageException) {}
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 346 */           String rotDam = "";
/* 347 */           if (t.getRotDamage() > 0)
/* 348 */             rotDam = " It has ugly black-green speckles."; 
/* 349 */           String fireDam = "";
/* 350 */           if (t.getFireDamage() > 0)
/* 351 */             fireDam = " It has the rune of fire."; 
/* 352 */           StringBuilder buf = new StringBuilder();
/* 353 */           buf.append("You detect a ");
/* 354 */           buf.append(t.getName());
/* 355 */           buf.append(" here, of ");
/* 356 */           buf.append(qlString);
/* 357 */           buf.append(" quality.");
/* 358 */           buf.append(" It has been set by people from ");
/* 359 */           buf.append(Kingdoms.getNameFor(t.getKingdom()));
/* 360 */           buf.append(villageName);
/* 361 */           buf.append(rotDam);
/* 362 */           buf.append(fireDam);
/* 363 */           comm.sendNormalServerMessage(buf.toString());
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 368 */     } else if (action == 645) {
/*     */       
/* 370 */       performer.getCommunicator().sendNormalServerMessage("You need a tool to cut the grass.");
/* 371 */       done = true;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 378 */       done = super.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/* 379 */     }  return done;
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
/*     */   private boolean cutGrass(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/* 404 */     float maxQLFromUsedTool = 5.0F;
/* 405 */     short yield = 2;
/* 406 */     int time = 0;
/* 407 */     Skill gardening = null;
/* 408 */     Skill toolskill = null;
/* 409 */     Item toolUsed = null;
/* 410 */     boolean toReturn = false;
/*     */     
/* 412 */     byte tileType = Tiles.decodeType(tile);
/* 413 */     byte tileData = Tiles.decodeData(tile);
/* 414 */     Tiles.Tile theTile = Tiles.getTile(tileType);
/* 415 */     String tileName = theTile.getName().toLowerCase();
/* 416 */     GrassData.GrowthStage growthStage = GrassData.GrowthStage.decodeTileData(tileData);
/* 417 */     GrassData.FlowerType flowerType = GrassData.FlowerType.decodeTileData(tileData);
/*     */     
/* 419 */     if (tileType == Tiles.Tile.TILE_MYCELIUM.id) {
/*     */       
/* 421 */       performer.getCommunicator().sendNormalServerMessage("You can see there is nothing to gather here.");
/*     */       
/* 423 */       byte data = GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, flowerType);
/* 424 */       Server.surfaceMesh
/* 425 */         .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), tileType, data));
/*     */       
/* 427 */       Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 428 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 433 */       if (tileType == Tiles.Tile.TILE_KELP.id || tileType == Tiles.Tile.TILE_REED.id) {
/*     */ 
/*     */         
/* 436 */         boolean ok = false;
/* 437 */         float pht = performer.getPositionZ() + performer.getAltOffZ();
/* 438 */         if (pht < getSurfaceHeight(tilex, tiley) + 2.0F) {
/* 439 */           ok = true;
/* 440 */         } else if (pht < getSurfaceHeight(tilex + 1, tiley) + 2.0F) {
/* 441 */           ok = true;
/* 442 */         } else if (pht < getSurfaceHeight(tilex + 1, tiley + 1) + 2.0F) {
/* 443 */           ok = true;
/* 444 */         } else if (pht < getSurfaceHeight(tilex, tiley + 1) + 2.0F) {
/* 445 */           ok = true;
/*     */         } 
/* 447 */         if (!ok) {
/*     */           
/* 449 */           performer.getCommunicator().sendNormalServerMessage("This " + tileName + " is growing out of your reach.");
/*     */           
/* 451 */           return true;
/*     */         } 
/*     */       } 
/* 454 */       float tilexpos = ((tilex << 2) + 2);
/* 455 */       float tileypos = ((tiley << 2) + 2);
/* 456 */       float tilezposNW = Zones.calculateHeight(tilexpos, tileypos, true);
/* 457 */       if (!performer.isWithinDistanceTo(tilexpos, tileypos, tilezposNW, 40.0F))
/*     */       {
/* 459 */         performer.getCommunicator().sendNormalServerMessage("This " + tileName + " is growing out of your reach.");
/*     */         
/* 461 */         return true;
/*     */       }
/*     */     
/* 464 */     } catch (NoSuchZoneException nsze) {
/*     */       
/* 466 */       logger.log(Level.WARNING, " No such zone exception at " + tilex + "," + tiley + " when player tried to TileGrassBehaviour.cutGrass()", (Throwable)nsze);
/*     */     } 
/*     */ 
/*     */     
/* 470 */     if (source != null) {
/*     */ 
/*     */       
/* 473 */       if (source.getTemplateId() == 267 || source.getTemplateId() == 268 || source
/* 474 */         .getTemplateId() == 176) {
/*     */         
/* 476 */         maxQLFromUsedTool = 100.0F;
/*     */       }
/* 478 */       else if (source.getTemplate().isSharp()) {
/*     */         
/* 480 */         maxQLFromUsedTool = 20.0F;
/*     */       }
/* 482 */       else if (source.getTemplateId() == 14) {
/*     */         
/* 484 */         maxQLFromUsedTool = 5.0F;
/*     */       }
/*     */       else {
/*     */         
/* 488 */         performer.getCommunicator().sendNormalServerMessage("You can't cut " + tileName + " with " + source
/* 489 */             .getNameWithGenus() + ".");
/* 490 */         return true;
/*     */       } 
/* 492 */       toolUsed = source;
/*     */     }
/*     */     else {
/*     */       
/* 496 */       performer.getCommunicator().sendNormalServerMessage("You need a tool to cut the " + tileName + ".");
/* 497 */       return true;
/*     */     } 
/*     */     
/* 500 */     yield = GrassData.GrowthStage.getYield(growthStage);
/*     */     
/* 502 */     if (yield == 0) {
/*     */       
/* 504 */       performer.getCommunicator().sendNormalServerMessage("You try to cut some " + growthStage
/* 505 */           .toString().toLowerCase() + " " + tileName + " but you fail to get any significant amount.");
/*     */       
/* 507 */       return true;
/*     */     } 
/* 509 */     if (counter == 1.0F) {
/*     */       
/* 511 */       double toolBonus = 0.0D;
/*     */       
/*     */       try {
/* 514 */         int weight = 0;
/* 515 */         if (tileType == Tiles.Tile.TILE_GRASS.id) {
/* 516 */           weight = ItemTemplateFactory.getInstance().getTemplate(620).getWeightGrams() * yield;
/* 517 */         } else if (tileType == Tiles.Tile.TILE_REED.id) {
/* 518 */           weight = ItemTemplateFactory.getInstance().getTemplate(743).getWeightGrams() * yield;
/* 519 */         } else if (tileType == Tiles.Tile.TILE_KELP.id) {
/* 520 */           weight = ItemTemplateFactory.getInstance().getTemplate(755).getWeightGrams() * yield;
/* 521 */         }  if (performer.getInventory().getNumItemsNotCoins() + 1 >= 100) {
/*     */           
/* 523 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the grass. You need to drop something first.");
/*     */           
/* 525 */           return true;
/*     */         } 
/* 527 */         if (!performer.canCarry(weight))
/*     */         {
/* 529 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the grass. You need to drop some things first.");
/*     */           
/* 531 */           return true;
/*     */         }
/*     */       
/* 534 */       } catch (NoSuchTemplateException nst) {
/*     */         
/* 536 */         logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 537 */         return true;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 542 */         gardening = performer.getSkills().getSkill(10045);
/*     */       }
/* 544 */       catch (NoSuchSkillException nss) {
/*     */         
/* 546 */         gardening = performer.getSkills().learn(10045, 1.0F);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 551 */         toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 552 */         toolBonus = toolskill.getKnowledge(0.0D);
/*     */       }
/* 554 */       catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */ 
/*     */       
/* 558 */       time = Actions.getStandardActionTime(performer, gardening, source, toolBonus);
/* 559 */       performer.getCommunicator().sendNormalServerMessage("You start to gather " + growthStage
/* 560 */           .toString().toLowerCase() + " " + tileName + ".");
/*     */ 
/*     */       
/* 563 */       Server.getInstance().broadCastAction(performer.getName() + " starts to gather " + tileName + ".", performer, 5);
/* 564 */       performer.sendActionControl("gathering " + tileName, true, time);
/* 565 */       act.setTimeLeft(time);
/* 566 */       toReturn = false;
/*     */     } else {
/*     */       
/* 569 */       time = act.getTimeLeft();
/* 570 */     }  if (act.mayPlaySound())
/* 571 */       Methods.sendSound(performer, "sound.work.foragebotanize"); 
/* 572 */     if (counter * 10.0F >= time) {
/*     */ 
/*     */       
/*     */       try {
/* 576 */         int weight = ItemTemplateFactory.getInstance().getTemplate(620).getWeightGrams() * yield;
/*     */         
/* 578 */         if (!performer.canCarry(weight))
/*     */         {
/* 580 */           performer.getCommunicator().sendNormalServerMessage("You would not be able to carry the " + tileName + ". You need to drop some things first.");
/*     */ 
/*     */           
/* 583 */           return true;
/*     */         }
/*     */       
/* 586 */       } catch (NoSuchTemplateException nst) {
/*     */         
/* 588 */         logger.log(Level.WARNING, nst.getLocalizedMessage(), (Throwable)nst);
/* 589 */         return true;
/*     */       } 
/* 591 */       source.setDamage(source.getDamage() + 0.003F * source.getDamageModifier());
/* 592 */       double toolBonus = 0.0D;
/* 593 */       double power = 0.0D;
/*     */ 
/*     */       
/*     */       try {
/* 597 */         gardening = performer.getSkills().getSkill(10045);
/*     */       }
/* 599 */       catch (NoSuchSkillException nss) {
/*     */         
/* 601 */         gardening = performer.getSkills().learn(10045, 1.0F);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 606 */         toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 607 */         toolBonus = Math.max(1.0D, toolskill.skillCheck(1.0D, toolUsed, 0.0D, false, counter));
/*     */       }
/* 609 */       catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */ 
/*     */       
/* 613 */       power = gardening.skillCheck(1.0D, toolUsed, toolBonus, false, counter);
/* 614 */       if (toolUsed.getSpellEffects() != null)
/*     */       {
/* 616 */         power *= toolUsed.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_RESGATHERED);
/*     */       }
/* 618 */       power += toolUsed.getRarity();
/*     */       
/*     */       try {
/* 621 */         Item yieldItem = null;
/* 622 */         for (int i = 0; i < yield; i++) {
/*     */           
/* 624 */           maxQLFromUsedTool = Math.min(maxQLFromUsedTool, (float)Math.min(100.0D, power));
/* 625 */           if (tileType == Tiles.Tile.TILE_GRASS.id) {
/* 626 */             yieldItem = ItemFactory.createItem(620, Math.max(1.0F, maxQLFromUsedTool), null);
/* 627 */           } else if (tileType == Tiles.Tile.TILE_REED.id) {
/* 628 */             yieldItem = ItemFactory.createItem(743, Math.max(1.0F, maxQLFromUsedTool), null);
/* 629 */           } else if (tileType == Tiles.Tile.TILE_KELP.id) {
/* 630 */             yieldItem = ItemFactory.createItem(755, Math.max(1.0F, maxQLFromUsedTool), null);
/*     */           } 
/* 632 */           if (power < 0.0D) {
/* 633 */             yieldItem.setDamage((float)-power / 2.0F);
/*     */           }
/* 635 */           performer.getInventory().insertItem(yieldItem);
/*     */         } 
/*     */         
/* 638 */         byte data = GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, flowerType);
/* 639 */         Server.surfaceMesh
/* 640 */           .setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), tileType, data));
/*     */         
/* 642 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 643 */         performer.getCommunicator().sendNormalServerMessage("You gather " + yield + " " + yieldItem.getName() + ".");
/* 644 */         Server.getInstance().broadCastAction(performer.getName() + " gathers some " + yieldItem.getName() + ".", performer, 5);
/*     */ 
/*     */       
/*     */       }
/* 648 */       catch (NoSuchTemplateException nst) {
/*     */         
/* 650 */         logger.log(Level.WARNING, "No template for grass type item!", (Throwable)nst);
/* 651 */         performer.getCommunicator().sendNormalServerMessage("You fail to gather the grass. Your sensitive mind notices a wrongness in the fabric of space.");
/*     */       
/*     */       }
/* 654 */       catch (FailedException fe) {
/*     */         
/* 656 */         logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 657 */         performer.getCommunicator().sendNormalServerMessage("You fail to gather the grass. Your sensitive mind notices a wrongness in the fabric of space.");
/*     */       } 
/*     */       
/* 660 */       toReturn = true;
/*     */     } 
/* 662 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getSurfaceHeight(int tilex, int tiley) {
/* 667 */     int tileNW = Server.surfaceMesh.getTile(tilex, tiley);
/* 668 */     return Tiles.decodeHeightAsFloat(tileNW);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean makeLawn(Action act, Creature performer, Item source, int tilex, int tiley, int tile, short action, float counter) {
/* 687 */     byte tileType = Tiles.decodeType(tile);
/* 688 */     String grass = "grass";
/* 689 */     if (tileType == Tiles.Tile.TILE_MYCELIUM.id)
/* 690 */       grass = "mycelium"; 
/* 691 */     int time = 0;
/* 692 */     Skill gardening = null;
/* 693 */     Skill toolskill = null;
/* 694 */     Item toolUsed = null;
/* 695 */     boolean toReturn = Terraforming.cannotMakeLawn(performer, tilex, tiley);
/* 696 */     if (toReturn) {
/* 697 */       return toReturn;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 703 */       float tilexpos = ((tilex << 2) + 1);
/* 704 */       float tileypos = ((tiley << 2) + 1);
/* 705 */       float tilezpos = Zones.calculateHeight(tilexpos, tileypos, true);
/* 706 */       if (!performer.isWithinDistanceTo(tilexpos, tileypos, tilezpos, 20.0F))
/*     */       {
/* 708 */         performer.getCommunicator().sendNormalServerMessage("This " + grass + " is growing out of your reach.");
/*     */         
/* 710 */         return true;
/*     */       }
/*     */     
/* 713 */     } catch (NoSuchZoneException nsze) {
/*     */       
/* 715 */       logger.log(Level.WARNING, " No such zone exception at " + tilex + "," + tiley + " when player tried to TileGrassBehaviour.makeLawn()", (Throwable)nsze);
/*     */     } 
/*     */ 
/*     */     
/* 719 */     if (source != null) {
/*     */ 
/*     */       
/* 722 */       if (source.getTemplateId() == 394 || source
/* 723 */         .getTemplateId() == 176) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 732 */         toolUsed = source;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 740 */         if (counter == 1.0F) {
/*     */           
/* 742 */           gardening = performer.getSkills().getSkillOrLearn(10045);
/* 743 */           double toolBonus = 0.0D;
/*     */           
/*     */           try {
/* 746 */             toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 747 */             toolBonus = toolskill.getKnowledge(0.0D);
/*     */           }
/* 749 */           catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */ 
/*     */           
/* 753 */           time = Actions.getStandardActionTime(performer, gardening, source, toolBonus);
/* 754 */           performer.getCommunicator().sendNormalServerMessage("You start to trim the " + grass + " to lawn length.");
/*     */ 
/*     */           
/* 757 */           Server.getInstance().broadCastAction(performer.getName() + " starts to trim the " + grass + ".", performer, 5);
/* 758 */           performer.sendActionControl("trimming " + grass, true, time);
/* 759 */           act.setTimeLeft(time);
/* 760 */           toReturn = false;
/*     */         } else {
/*     */           
/* 763 */           time = act.getTimeLeft();
/*     */         } 
/* 765 */         if (act.mayPlaySound()) {
/* 766 */           Methods.sendSound(performer, "sound.work.foragebotanize");
/*     */         }
/* 768 */         if (counter * 10.0F >= time) {
/*     */ 
/*     */           
/* 771 */           source.setDamage(source.getDamage() + 0.003F * source.getDamageModifier());
/* 772 */           double toolBonus = 0.0D;
/*     */           
/* 774 */           gardening = performer.getSkills().getSkillOrLearn(10045);
/*     */           
/*     */           try {
/* 777 */             toolskill = performer.getSkills().getSkill(source.getTemplateId());
/* 778 */             toolBonus = Math.max(1.0D, toolskill.skillCheck(1.0D, toolUsed, 0.0D, false, counter));
/*     */           }
/* 780 */           catch (NoSuchSkillException noSuchSkillException) {}
/*     */ 
/*     */ 
/*     */           
/* 784 */           gardening.skillCheck(1.0D, toolUsed, toolBonus, false, counter);
/* 785 */           byte data = GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, GrassData.FlowerType.NONE);
/* 786 */           if (tileType == Tiles.Tile.TILE_MYCELIUM.id) {
/* 787 */             Server.surfaceMesh.setTile(tilex, tiley, 
/* 788 */                 Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM_LAWN.id, data));
/*     */           } else {
/* 790 */             Server.surfaceMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_LAWN.id, data));
/*     */           } 
/* 792 */           Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/* 793 */           performer.getCommunicator().sendNormalServerMessage("You trim the " + grass + " to look like a lawn.");
/* 794 */           Server.getInstance().broadCastAction(performer
/* 795 */               .getName() + " looks pleased that the " + grass + " is trimmed and now looks like a lawn.", performer, 5);
/*     */           
/* 797 */           toReturn = true;
/*     */         } 
/* 799 */         return toReturn;
/*     */       } 
/*     */       performer.getCommunicator().sendNormalServerMessage("You can't trim the " + grass + " with " + source.getNameWithGenus() + ".");
/*     */       return true;
/*     */     } 
/*     */     performer.getCommunicator().sendNormalServerMessage("You need a tool to trim the " + grass + ".");
/*     */     return true;
/*     */   } public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 807 */     boolean done = true;
/* 808 */     byte tileType = Tiles.decodeType(tile);
/* 809 */     byte tileData = Tiles.decodeData(tile);
/* 810 */     if (action == 1) {
/* 811 */       done = action(act, performer, tilex, tiley, onSurface, tile, action, counter);
/* 812 */     } else if (action == 186) {
/*     */       
/* 814 */       if (source.isNaturePlantable()) {
/* 815 */         done = Terraforming.plantFlower(performer, source, tilex, tiley, onSurface, tile, counter);
/*     */       } else {
/* 817 */         done = Terraforming.plantSprout(performer, source, tilex, tiley, onSurface, tile, counter, false);
/*     */       } 
/* 819 */     } else if (action == 660) {
/*     */       
/* 821 */       done = Terraforming.plantSprout(performer, source, tilex, tiley, onSurface, tile, counter, true);
/*     */     }
/* 823 */     else if (action == 187) {
/*     */       
/* 825 */       if (source.getTemplateId() == 267 || source.getTemplateId() == 176)
/*     */       {
/* 827 */         done = Terraforming.pickFlower(performer, source, tilex, tiley, tile, counter, act);
/*     */       }
/*     */     }
/* 830 */     else if (action == 644) {
/*     */       
/* 832 */       if (Tiles.decodeType(tile) == Tiles.Tile.TILE_MYCELIUM.id) {
/*     */         
/* 834 */         if (source.getTemplateId() == 394 || (source
/* 835 */           .getTemplateId() == 176 && performer.getPower() >= 2)) {
/* 836 */           done = makeLawn(act, performer, source, tilex, tiley, tile, action, counter);
/*     */         }
/* 838 */       } else if (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id) {
/*     */         
/* 840 */         if (GrassData.GrowthStage.decodeTileData(Tiles.decodeData(tile)) == GrassData.GrowthStage.SHORT && (source
/* 841 */           .getTemplateId() == 394 || (source
/* 842 */           .getTemplateId() == 176 && performer.getPower() >= 2))) {
/* 843 */           done = makeLawn(act, performer, source, tilex, tiley, tile, action, counter);
/*     */         }
/*     */       } else {
/* 846 */         return true;
/*     */       } 
/* 848 */     } else if (action == 645 && (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id || 
/* 849 */       Tiles.decodeType(tile) == Tiles.Tile.TILE_KELP.id || Tiles.decodeType(tile) == Tiles.Tile.TILE_REED.id)) {
/*     */       
/* 851 */       done = cutGrass(act, performer, source, tilex, tiley, tile, action, counter);
/*     */     }
/* 853 */     else if (action == 188 && (source
/* 854 */       .getTemplateId() == 315 || source.getTemplateId() == 176)) {
/*     */ 
/*     */       
/* 857 */       byte d = (byte)(tileData & 0xFF);
/*     */       
/* 859 */       d = (byte)(d | GrassData.GrowthStage.WILD.getEncodedData() | GrassData.FlowerType.FLOWER_7.getEncodedData());
/* 860 */       Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, d);
/* 861 */       performer.getCommunicator().sendNormalServerMessage("You create some " + GrassData.GrowthStage.WILD
/* 862 */           .name().toLowerCase() + " grass with some " + GrassData.FlowerType.FLOWER_7
/* 863 */           .getDescription() + ".");
/* 864 */       Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*     */     }
/* 866 */     else if (action == 118 && source.getTemplateId() == 526) {
/*     */       
/* 868 */       performer.getCommunicator().sendNormalServerMessage("You draw a circle in the air in front of you with " + source
/* 869 */           .getNameWithGenus() + ".");
/* 870 */       Server.getInstance().broadCastAction(performer
/* 871 */           .getName() + " draws a circle in the air in front of " + performer.getHimHerItString() + " with " + source
/* 872 */           .getNameWithGenus() + ".", performer, 5);
/* 873 */       done = true;
/*     */       
/* 875 */       if (source.getAuxData() > 0) {
/*     */         
/* 877 */         if (performer.getKingdomTemplateId() == 3) {
/* 878 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/*     */         }
/* 880 */         else if (Server.rand.nextInt(2) == 0) {
/* 881 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, 
/* 882 */               (byte)(Server.rand.nextInt(7) + 1));
/*     */         } else {
/* 884 */           Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_TREE.id, 
/* 885 */               (byte)(Server.rand.nextInt(16) & 15 + (Server.rand.nextInt(13) << 4) & 0xFF));
/* 886 */         }  Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
/* 887 */         source.setAuxData((byte)(source.getAuxData() - 1));
/*     */       } else {
/*     */         
/* 890 */         performer.getCommunicator().sendNormalServerMessage("Nothing happens.");
/*     */       } 
/* 892 */     } else if (action == 118 && source.getTemplateId() == 176 && performer.getPower() >= 2) {
/*     */       
/* 894 */       Terraforming.rampantGrowth(performer, tilex, tiley);
/*     */     }
/* 896 */     else if (action == 148 && canCollectSnow(performer, tilex, tiley, tileType, tileData) && source
/* 897 */       .getTemplateId() == 204) {
/*     */       
/* 899 */       VolaTile t = Zones.getTileOrNull(tilex, tiley, onSurface);
/* 900 */       if (t != null)
/*     */       {
/* 902 */         if ((t.getItems()).length > 0) {
/*     */           
/* 904 */           performer.getCommunicator().sendNormalServerMessage("Remove all obstructing items first.");
/* 905 */           return true;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 913 */       done = false;
/* 914 */       if (counter == 1.0F)
/*     */       {
/* 916 */         performer.getCommunicator().sendNormalServerMessage("You start making a snowman.");
/* 917 */         Server.getInstance().broadCastAction(performer.getName() + " starts making a snowman.", performer, 5);
/* 918 */         performer.sendActionControl("creating", true, 600);
/*     */       }
/* 920 */       else if (act.currentSecond() == 10)
/*     */       {
/* 922 */         performer.getCommunicator().sendNormalServerMessage("You have now rolled the large bottom ball.");
/* 923 */         Server.getInstance().broadCastAction(performer.getName() + " has now rolled the large bottom ball.", performer, 5);
/*     */       
/*     */       }
/* 926 */       else if (act.currentSecond() == 20)
/*     */       {
/* 928 */         performer.getCommunicator().sendNormalServerMessage("You have now rolled a smaller ball to use as the chest.");
/* 929 */         Server.getInstance().broadCastAction(performer.getName() + " has now rolled a smaller ball to use as chest.", performer, 5);
/*     */       
/*     */       }
/* 932 */       else if (act.currentSecond() == 30)
/*     */       {
/* 934 */         performer.getCommunicator().sendNormalServerMessage("You have now created the head ball.");
/* 935 */         Server.getInstance().broadCastAction(performer.getName() + " has now created the head that goes on top.", performer, 5);
/*     */       
/*     */       }
/* 938 */       else if (act.currentSecond() == 40)
/*     */       {
/* 940 */         performer.getCommunicator().sendNormalServerMessage("You use a couple of twigs for arms.");
/* 941 */         Server.getInstance().broadCastAction(performer.getName() + " uses a couple of twigs for arms.", performer, 5);
/*     */       }
/* 943 */       else if (act.currentSecond() == 50)
/*     */       {
/* 945 */         performer.getCommunicator().sendNormalServerMessage("You start to assemble the snowman.");
/* 946 */         Server.getInstance().broadCastAction(performer.getName() + " starts to assemble the snowman.", performer, 5);
/*     */       }
/* 948 */       else if (act.currentSecond() == 60)
/*     */       {
/* 950 */         if (act.getRarity() != 0)
/*     */         {
/* 952 */           performer.playPersonalSound("sound.fx.drumroll");
/*     */         }
/* 954 */         done = true;
/* 955 */         Server.setGatherable(tilex, tiley, false);
/* 956 */         performer.getCommunicator().sendNormalServerMessage("As a final touch you put the charcoal as eyes and the snowman comes to life!");
/*     */         
/* 958 */         Server.getInstance().broadCastAction(performer.getName() + " uses charcoal as eyes and finishes the snowman.", performer, 5);
/*     */ 
/*     */         
/*     */         try {
/* 962 */           ItemFactory.createItem(655, 90.0F, ((tilex << 2) + 2), ((tiley << 2) + 2), performer.getStatus()
/* 963 */               .getRotation(), true, act.getRarity(), performer.getBridgeId(), performer.getName());
/*     */         }
/* 965 */         catch (FailedException failedException) {
/*     */ 
/*     */         
/*     */         }
/* 969 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */ 
/*     */ 
/*     */         
/* 973 */         Items.destroyItem(source.getWurmId());
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 981 */       done = super.action(act, performer, source, tilex, tiley, onSurface, heightOffset, tile, action, counter);
/* 982 */     }  return done;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TileGrassBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */