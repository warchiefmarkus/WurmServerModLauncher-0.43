/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import com.wurmonline.shared.constants.StructureConstants;
/*     */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class Structures
/*     */   implements MiscConstants, CounterTypes
/*     */ {
/*     */   private static final String GET_STRUCTURES = "SELECT * FROM STRUCTURES";
/*     */   private static Map<Long, Structure> structures;
/*     */   private static Map<Long, Structure> bridges;
/*  58 */   private static final Structure[] emptyStructures = new Structure[0];
/*     */   
/*  60 */   private static final Logger logger = Logger.getLogger(Structures.class.getName());
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
/*     */   public static int getNumberOfStructures() {
/*  82 */     if (structures != null)
/*     */     {
/*  84 */       return structures.size();
/*     */     }
/*     */ 
/*     */     
/*  88 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Structure[] getAllStructures() {
/*  94 */     if (structures == null)
/*  95 */       return emptyStructures; 
/*  96 */     return (Structure[])structures.values().toArray((Object[])new Structure[structures.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Structure[] getManagedBuildingsFor(Player player, int villageId, boolean includeAll) {
/* 107 */     if (structures == null)
/* 108 */       return emptyStructures; 
/* 109 */     Set<Structure> buildings = new HashSet<>();
/* 110 */     for (Structure structure : structures.values()) {
/*     */       
/* 112 */       if (structure.isTypeHouse()) {
/*     */         
/* 114 */         if (structure.canManage((Creature)player))
/* 115 */           buildings.add(structure); 
/* 116 */         if (includeAll)
/*     */         {
/* 118 */           if ((villageId >= 0 && structure.getVillageId() == villageId) || structure
/* 119 */             .isActualOwner(player.getWurmId())) {
/* 120 */             buildings.add(structure);
/*     */           }
/*     */         }
/* 123 */         if (structure.getWritid() != -10L && structure.isActualOwner(player.getWurmId())) {
/*     */           
/* 125 */           Items.destroyItem(structure.getWritId());
/* 126 */           structure.setWritid(-10L, true);
/*     */         } 
/*     */       } 
/*     */     } 
/* 130 */     return buildings.<Structure>toArray(new Structure[buildings.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Structure[] getOwnedBuildingFor(Player player) {
/* 135 */     if (structures == null)
/* 136 */       return emptyStructures; 
/* 137 */     Set<Structure> buildings = new HashSet<>();
/* 138 */     for (Structure structure : structures.values()) {
/*     */       
/* 140 */       if (structure.isTypeHouse())
/*     */       {
/* 142 */         if (structure.isOwner((Creature)player) || structure.isActualOwner(player.getWurmId()))
/* 143 */           buildings.add(structure); 
/*     */       }
/*     */     } 
/* 146 */     return buildings.<Structure>toArray(new Structure[buildings.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Structure getStructureOrNull(long id) {
/* 151 */     Structure structure = null;
/* 152 */     if (structures == null) {
/* 153 */       structures = new ConcurrentHashMap<>();
/*     */     } else {
/* 155 */       structure = structures.get(new Long(id));
/* 156 */     }  if (structure == null)
/*     */     {
/* 158 */       if (WurmId.getType(id) == 4) {
/*     */         
/*     */         try {
/*     */           
/* 162 */           structure = loadStructure(id);
/* 163 */           addStructure(structure);
/*     */         }
/* 165 */         catch (IOException iOException) {
/*     */ 
/*     */         
/* 168 */         } catch (NoSuchStructureException noSuchStructureException) {}
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 173 */     return structure;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Structure getStructure(long id) throws NoSuchStructureException {
/* 178 */     Structure structure = getStructureOrNull(id);
/* 179 */     if (structure == null)
/* 180 */       throw new NoSuchStructureException("No such structure."); 
/* 181 */     return structure;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addStructure(Structure structure) {
/* 186 */     if (structures == null)
/* 187 */       structures = new ConcurrentHashMap<>(); 
/* 188 */     structures.put(new Long(structure.getWurmId()), structure);
/* 189 */     if (structure.isTypeBridge()) {
/* 190 */       addBridge(structure);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final void addBridge(Structure bridge) {
/* 195 */     if (bridges == null)
/* 196 */       bridges = new ConcurrentHashMap<>(); 
/* 197 */     bridges.put(new Long(bridge.getWurmId()), bridge);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeBridge(long id) {
/* 202 */     if (bridges != null) {
/* 203 */       bridges.remove(new Long(id));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final Structure getBridge(long id) {
/* 208 */     Structure bridge = null;
/* 209 */     if (bridges != null)
/* 210 */       bridge = bridges.get(new Long(id)); 
/* 211 */     return bridge;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeStructure(long id) {
/* 216 */     if (structures != null) {
/* 217 */       structures.remove(new Long(id));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Structure createStructure(byte theStructureType, String name, long id, int startx, int starty, boolean surfaced) {
/* 224 */     Structure toReturn = null;
/*     */ 
/*     */     
/* 227 */     toReturn = new DbStructure(theStructureType, name, id, startx, starty, surfaced);
/* 228 */     addStructure(toReturn);
/*     */     
/* 230 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Structure loadStructure(long id) throws IOException, NoSuchStructureException {
/* 235 */     Structure toReturn = null;
/*     */ 
/*     */     
/* 238 */     toReturn = new DbStructure(id);
/* 239 */     addStructure(toReturn);
/*     */     
/* 241 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Structure getStructureForWrit(long writId) throws NoSuchStructureException {
/* 246 */     if (writId == -10L) {
/* 247 */       throw new NoSuchStructureException("No structure for writid " + writId);
/*     */     }
/*     */     
/* 250 */     for (Structure s : structures.values()) {
/*     */       
/* 252 */       if (s.getWritId() == writId)
/* 253 */         return s; 
/*     */     } 
/* 255 */     throw new NoSuchStructureException("No structure for writid " + writId);
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
/*     */   public static void endLoadAll() {
/* 290 */     if (structures != null)
/*     */     {
/* 292 */       for (Structure struct : structures.values()) {
/*     */ 
/*     */         
/*     */         try {
/* 296 */           struct.endLoading();
/*     */         }
/* 298 */         catch (IOException iox) {
/*     */           
/* 300 */           logger.log(Level.WARNING, iox.getMessage() + ": " + struct.getWurmId() + " writ " + struct.getWritid());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadAllStructures() throws IOException {
/* 313 */     logger.info("Loading all Structures");
/* 314 */     long start = System.nanoTime();
/* 315 */     Connection dbcon = null;
/* 316 */     PreparedStatement ps = null;
/* 317 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 320 */       dbcon = DbConnector.getZonesDbCon();
/* 321 */       ps = dbcon.prepareStatement("SELECT * FROM STRUCTURES");
/* 322 */       rs = ps.executeQuery();
/* 323 */       while (rs.next())
/*     */       {
/* 325 */         long wurmid = rs.getLong("WURMID");
/* 326 */         byte structureType = rs.getByte("STRUCTURETYPE");
/* 327 */         boolean surfaced = rs.getBoolean("SURFACED");
/*     */         
/* 329 */         byte roof = rs.getByte("ROOF");
/*     */         
/* 331 */         String name = rs.getString("NAME");
/* 332 */         if (name == null)
/* 333 */           name = "Unknown structure"; 
/* 334 */         if (name.length() >= 50)
/* 335 */           name = name.substring(0, 49); 
/* 336 */         boolean finished = rs.getBoolean("FINISHED");
/* 337 */         boolean finalfinished = rs.getBoolean("FINFINISHED");
/* 338 */         boolean allowsCitizens = rs.getBoolean("ALLOWSVILLAGERS");
/* 339 */         boolean allowsAllies = rs.getBoolean("ALLOWSALLIES");
/* 340 */         boolean allowsKingdom = rs.getBoolean("ALLOWSKINGDOM");
/* 341 */         String planner = rs.getString("PLANNER");
/* 342 */         long ownerId = rs.getLong("OWNERID");
/* 343 */         int settings = rs.getInt("SETTINGS");
/* 344 */         int villageId = rs.getInt("VILLAGE");
/* 345 */         long writid = -10L;
/*     */         
/*     */         try {
/* 348 */           writid = rs.getLong("WRITID");
/*     */         }
/* 350 */         catch (Exception nsi) {
/*     */           
/* 352 */           if (structureType == 0)
/* 353 */             logger.log(Level.INFO, "No writ for house with id:" + wurmid + " creating new after loading.", nsi); 
/*     */         } 
/* 355 */         addStructure(new DbStructure(structureType, name, wurmid, surfaced, roof, finished, finalfinished, writid, planner, ownerId, settings, villageId, allowsCitizens, allowsAllies, allowsKingdom));
/*     */       }
/*     */     
/*     */     }
/* 359 */     catch (SQLException sqex) {
/*     */       
/* 361 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 365 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 366 */       DbConnector.returnConnection(dbcon);
/* 367 */       int numberOfStructures = (structures != null) ? structures.size() : 0;
/* 368 */       logger.log(Level.INFO, "Structures loaded. Number of structures=" + numberOfStructures + ". That took " + (
/*     */           
/* 370 */           (float)(System.nanoTime() - start) / 1000000.0F) + " ms.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Structure getStructureForTile(int tilex, int tiley, boolean onSurface) {
/* 376 */     if (structures != null)
/*     */     {
/* 378 */       for (Structure s : structures.values()) {
/*     */         
/* 380 */         if (s.isOnSurface() == onSurface && s.contains(tilex, tiley))
/*     */         {
/* 382 */           return s;
/*     */         }
/*     */       } 
/*     */     }
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Structure getBuildingForTile(int tilex, int tiley) {
/* 391 */     if (structures != null)
/*     */     {
/* 393 */       for (Structure s : structures.values()) {
/*     */         
/* 395 */         if (s.contains(tilex, tiley))
/*     */         {
/* 397 */           return s;
/*     */         }
/*     */       } 
/*     */     }
/* 401 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createRandomStructure(Creature creator, int stx, int endtx, int sty, int endty, int centerx, int centery, byte material, String sname) {
/* 407 */     if (creator.getCurrentTile() == null || creator.getCurrentTile().getStructure() == null) {
/*     */       
/*     */       try {
/*     */         
/* 411 */         Structure struct = createStructure((byte)0, sname, WurmId.getNextPlanId(), centerx, centery, true);
/*     */         
/* 413 */         for (int currx = stx; currx <= endtx; currx++) {
/* 414 */           for (int curry = sty; curry <= endty; curry++) {
/*     */             
/* 416 */             if (currx != stx || (curry != sty && Server.rand.nextInt(3) < 2)) {
/*     */               
/* 418 */               VolaTile vtile = Zones.getOrCreateTile(currx, curry, true);
/* 419 */               struct.addBuildTile(vtile, false);
/* 420 */               struct.clearAllWallsAndMakeWallsForStructureBorder(vtile);
/*     */             } 
/*     */           } 
/* 423 */         }  float rot = Creature.normalizeAngle(creator.getStatus().getRotation());
/* 424 */         struct.makeFinal(creator, sname);
/* 425 */         for (VolaTile bt : struct.getStructureTiles()) {
/*     */           
/* 427 */           StructureTypeEnum wtype = StructureTypeEnum.SOLID;
/* 428 */           if (Server.rand.nextInt(2) == 0)
/* 429 */             wtype = StructureTypeEnum.WINDOW; 
/* 430 */           for (Wall plan : bt.getWalls()) {
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
/* 454 */             if (!plan.isHorizontal() && plan.getStartY() == creator.getTileY() && 
/* 455 */               rot <= 315.0F && rot >= 235.0F)
/* 456 */               wtype = StructureTypeEnum.DOOR; 
/* 457 */             if (plan.isHorizontal() && plan.getStartX() == creator.getTileX() && ((
/* 458 */               rot >= 315.0F && rot <= 360.0F) || (rot >= 0.0F && rot <= 45.0F)))
/* 459 */               wtype = StructureTypeEnum.DOOR; 
/* 460 */             if (plan.isHorizontal() && plan.getStartX() == creator.getTileX() && 
/* 461 */               rot >= 135.0F && rot <= 215.0F)
/* 462 */               wtype = StructureTypeEnum.DOOR; 
/* 463 */             if (!plan.isHorizontal() && plan.getStartY() == creator.getTileY() && 
/* 464 */               rot <= 135.0F && rot >= 45.0F)
/* 465 */               wtype = StructureTypeEnum.DOOR; 
/* 466 */             if (material == 15) {
/* 467 */               plan.setMaterial(StructureMaterialEnum.STONE);
/*     */             } else {
/* 469 */               plan.setMaterial(StructureMaterialEnum.WOOD);
/* 470 */             }  plan.setType(wtype);
/* 471 */             plan.setQualityLevel(80.0F);
/* 472 */             plan.setState(StructureStateEnum.FINISHED);
/* 473 */             bt.updateWall(plan);
/* 474 */             if (plan.isDoor()) {
/*     */ 
/*     */               
/* 477 */               Door door = new DbDoor(plan);
/* 478 */               door.setStructureId(struct.getWurmId());
/* 479 */               struct.addDoor(door);
/* 480 */               door.save();
/* 481 */               door.addToTiles();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 488 */         struct.setFinished(true);
/* 489 */         struct.setFinalFinished(true);
/* 490 */         for (VolaTile bt : struct.getStructureTiles())
/*     */         {
/* 492 */           Floor floor = new DbFloor(StructureConstants.FloorType.FLOOR, bt.getTileX(), bt.getTileY(), 0, 80.0F, struct.getWurmId(), StructureConstants.FloorMaterial.WOOD, 0);
/*     */           
/* 494 */           floor.setFloorState(StructureConstants.FloorState.COMPLETED);
/* 495 */           bt.addFloor(floor);
/* 496 */           floor.save();
/* 497 */           Floor roof = new DbFloor(StructureConstants.FloorType.ROOF, bt.getTileX(), bt.getTileY(), 30, 80.0F, struct.getWurmId(), StructureConstants.FloorMaterial.THATCH, 0);
/*     */ 
/*     */           
/* 500 */           roof.setFloorState(StructureConstants.FloorState.COMPLETED);
/* 501 */           bt.addFloor(roof);
/* 502 */           roof.save();
/*     */         }
/*     */       
/* 505 */       } catch (Exception ex) {
/*     */         
/* 507 */         logger.log(Level.WARNING, "exception " + ex, ex);
/* 508 */         creator.getCommunicator().sendAlertServerMessage(ex.getMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void createSquareStructure(Creature creator, int stx, int endtx, int sty, int endty, int centerx, int centery, byte material, String sname) {
/* 516 */     if (creator.getCurrentTile() == null || creator.getCurrentTile().getStructure() == null)
/*     */       
/*     */       try {
/*     */         
/* 520 */         Structure struct = createStructure((byte)0, sname, WurmId.getNextPlanId(), centerx, centery, true);
/*     */         
/* 522 */         for (int currx = stx; currx <= endtx; currx++) {
/* 523 */           for (int curry = sty; curry <= endty; curry++) {
/*     */             
/* 525 */             VolaTile vtile = Zones.getOrCreateTile(currx, curry, true);
/* 526 */             struct.addBuildTile(vtile, false);
/* 527 */             struct.clearAllWallsAndMakeWallsForStructureBorder(vtile);
/*     */           } 
/* 529 */         }  float rot = Creature.normalizeAngle(creator.getStatus().getRotation());
/* 530 */         struct.makeFinal(creator, sname); int i;
/* 531 */         for (i = stx; i <= endtx; i++) {
/* 532 */           for (int curry = sty; curry <= endty; curry++) {
/*     */             
/* 534 */             VolaTile vtile = Zones.getOrCreateTile(i, curry, true);
/* 535 */             StructureTypeEnum wtype = StructureTypeEnum.SOLID;
/* 536 */             if (Server.rand.nextInt(2) == 0)
/* 537 */               wtype = StructureTypeEnum.WINDOW; 
/* 538 */             if (i == stx)
/*     */             {
/* 540 */               for (Wall plan : vtile.getWalls()) {
/*     */                 
/* 542 */                 if (!plan.isHorizontal() && plan.getStartX() == i) {
/*     */                   
/* 544 */                   if (curry == creator.getTileY() && 
/* 545 */                     rot <= 315.0F && rot >= 235.0F)
/*     */                   {
/* 547 */                     wtype = StructureTypeEnum.DOOR; } 
/* 548 */                   if (material == 15) {
/* 549 */                     plan.setMaterial(StructureMaterialEnum.STONE);
/*     */                   } else {
/* 551 */                     plan.setMaterial(StructureMaterialEnum.WOOD);
/* 552 */                   }  plan.setType(wtype);
/* 553 */                   plan.setQualityLevel(80.0F);
/* 554 */                   plan.setState(StructureStateEnum.FINISHED);
/* 555 */                   vtile.updateWall(plan);
/* 556 */                   if (plan.isDoor()) {
/*     */ 
/*     */                     
/* 559 */                     Door door = new DbDoor(plan);
/* 560 */                     door.setStructureId(struct.getWurmId());
/* 561 */                     struct.addDoor(door);
/* 562 */                     door.save();
/* 563 */                     door.addToTiles();
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             }
/* 568 */             if (curry == sty)
/*     */             {
/* 570 */               for (Wall plan : vtile.getWalls()) {
/*     */                 
/* 572 */                 if (plan.isHorizontal() && plan.getStartY() == curry) {
/*     */ 
/*     */                   
/* 575 */                   if (i == creator.getTileX() && ((
/* 576 */                     rot >= 315.0F && rot <= 360.0F) || (rot >= 0.0F && rot <= 45.0F)))
/*     */                   {
/*     */ 
/*     */                     
/* 580 */                     wtype = StructureTypeEnum.DOOR; } 
/* 581 */                   if (material == 15) {
/* 582 */                     plan.setMaterial(StructureMaterialEnum.STONE);
/*     */                   } else {
/* 584 */                     plan.setMaterial(StructureMaterialEnum.WOOD);
/* 585 */                   }  plan.setType(wtype);
/* 586 */                   plan.setQualityLevel(80.0F);
/* 587 */                   plan.setState(StructureStateEnum.FINISHED);
/* 588 */                   vtile.updateWall(plan);
/* 589 */                   if (plan.isDoor()) {
/*     */ 
/*     */                     
/* 592 */                     Door door = new DbDoor(plan);
/* 593 */                     door.setStructureId(struct.getWurmId());
/* 594 */                     struct.addDoor(door);
/* 595 */                     door.save();
/* 596 */                     door.addToTiles();
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             }
/* 601 */             if (curry == endty)
/*     */             {
/* 603 */               for (Wall plan : vtile.getWalls()) {
/*     */                 
/* 605 */                 if (plan.isHorizontal() && plan.getStartY() == curry + 1) {
/*     */ 
/*     */                   
/* 608 */                   if (i == creator.getTileX() && 
/* 609 */                     rot >= 135.0F && rot <= 215.0F)
/*     */                   {
/* 611 */                     wtype = StructureTypeEnum.DOOR; } 
/* 612 */                   if (material == 15) {
/* 613 */                     plan.setMaterial(StructureMaterialEnum.STONE);
/*     */                   } else {
/* 615 */                     plan.setMaterial(StructureMaterialEnum.WOOD);
/* 616 */                   }  plan.setType(wtype);
/* 617 */                   plan.setQualityLevel(80.0F);
/* 618 */                   plan.setState(StructureStateEnum.FINISHED);
/* 619 */                   vtile.updateWall(plan);
/* 620 */                   if (plan.isDoor()) {
/*     */ 
/*     */                     
/* 623 */                     Door door = new DbDoor(plan);
/* 624 */                     door.setStructureId(struct.getWurmId());
/* 625 */                     struct.addDoor(door);
/* 626 */                     door.save();
/* 627 */                     door.addToTiles();
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             }
/* 632 */             if (i == endtx)
/*     */             {
/* 634 */               for (Wall plan : vtile.getWalls()) {
/*     */                 
/* 636 */                 if (!plan.isHorizontal() && plan.getStartX() == i + 1) {
/*     */ 
/*     */                   
/* 639 */                   if (curry == creator.getTileY() && 
/* 640 */                     rot <= 135.0F && rot >= 45.0F)
/* 641 */                     wtype = StructureTypeEnum.DOOR; 
/* 642 */                   if (material == 15) {
/* 643 */                     plan.setMaterial(StructureMaterialEnum.STONE);
/*     */                   } else {
/* 645 */                     plan.setMaterial(StructureMaterialEnum.WOOD);
/* 646 */                   }  plan.setType(wtype);
/* 647 */                   plan.setQualityLevel(80.0F);
/* 648 */                   plan.setState(StructureStateEnum.FINISHED);
/* 649 */                   vtile.updateWall(plan);
/* 650 */                   if (plan.isDoor()) {
/*     */ 
/*     */                     
/* 653 */                     Door door = new DbDoor(plan);
/* 654 */                     door.setStructureId(struct.getWurmId());
/* 655 */                     struct.addDoor(door);
/* 656 */                     door.save();
/* 657 */                     door.addToTiles();
/*     */                   } 
/*     */                 } 
/*     */               }  } 
/*     */           } 
/*     */         } 
/* 663 */         struct.setFinished(true);
/* 664 */         struct.setFinalFinished(true);
/* 665 */         for (i = stx; i <= endtx; i++) {
/* 666 */           for (int curry = sty; curry <= endty; curry++) {
/*     */             
/* 668 */             VolaTile vtile = Zones.getOrCreateTile(i, curry, true);
/* 669 */             Floor floor = new DbFloor(StructureConstants.FloorType.FLOOR, i, curry, 0, 80.0F, struct.getWurmId(), StructureConstants.FloorMaterial.WOOD, 0);
/*     */             
/* 671 */             floor.setFloorState(StructureConstants.FloorState.COMPLETED);
/* 672 */             vtile.addFloor(floor);
/* 673 */             floor.save();
/* 674 */             Floor roof = new DbFloor(StructureConstants.FloorType.ROOF, i, curry, 30, 80.0F, struct.getWurmId(), StructureConstants.FloorMaterial.THATCH, 0);
/*     */ 
/*     */             
/* 677 */             roof.setFloorState(StructureConstants.FloorState.COMPLETED);
/* 678 */             vtile.addFloor(roof);
/* 679 */             roof.save();
/*     */           } 
/*     */         } 
/* 682 */       } catch (Exception ex) {
/*     */         
/* 684 */         logger.log(Level.WARNING, "exception " + ex, ex);
/* 685 */         creator.getCommunicator().sendAlertServerMessage(ex.getMessage());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Structures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */