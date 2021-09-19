/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.meshgen.ImprovedNoise;
/*      */ import com.wurmonline.server.meshgen.IslandAdder;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.Den;
/*      */ import com.wurmonline.server.zones.FocusZone;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
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
/*      */ public class TerraformingTask
/*      */   implements MiscConstants, ItemMaterials
/*      */ {
/*   58 */   private int counter = 0;
/*      */   private final int task;
/*      */   private final byte kingdom;
/*      */   private final int entityId;
/*      */   private final String entityName;
/*   63 */   private int startX = 0;
/*   64 */   private int startY = 0;
/*   65 */   private int startHeight = 0;
/*      */   
/*   67 */   private TerraformingTask next = null;
/*      */   private final int tasksRemaining;
/*   69 */   private int totalTasks = 0;
/*   70 */   private final Random random = new Random();
/*      */   
/*      */   private final boolean firstTask;
/*      */   public static final int ERUPT = 0;
/*      */   public static final int INDENT = 1;
/*      */   public static final int PLATEAU = 2;
/*      */   public static final int CRATERS = 3;
/*      */   public static final int MULTIPLATEAU = 4;
/*      */   public static final int RAVINE = 5;
/*      */   public static final int ISLAND = 6;
/*      */   public static final int MULTIRAVINE = 7;
/*   81 */   private int radius = 0;
/*   82 */   private int length = 0;
/*   83 */   private int direction = 0;
/*      */   private MeshIO topLayer;
/*      */   private MeshIO rockLayer;
/*   86 */   private final String[] prefixes = new String[] { "Et", "De", "Old", "Gaz", "Mak", "Fir", "Fyre", "Eld", "Vagn", "Mag", "Lav", "Volc", "Rad", "Ash", "Ask" };
/*      */   
/*   88 */   private final String[] suffixes = new String[] { "na", "cuse", "fir", "egap", "dire", "haul", "vann", "un", "lik", "ingan", "enken", "mosh", "kil", "atrask", "eskap" };
/*      */ 
/*      */ 
/*      */   
/*   92 */   private static final Logger logger = Logger.getLogger(TerraformingTask.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TerraformingTask(int whatToDo, byte targetKingdom, String epicEntityName, int epicEntityId, int tasksLeft, boolean isFirstTask) {
/*  102 */     this.task = whatToDo;
/*  103 */     this.kingdom = targetKingdom;
/*  104 */     this.entityName = epicEntityName;
/*  105 */     this.entityId = epicEntityId;
/*  106 */     if (tasksLeft < 0)
/*      */     {
/*  108 */       if (this.task == 3) {
/*      */         
/*  110 */         int numCratersRoot = 1;
/*  111 */         this.totalTasks = 1 + 1 * this.random.nextInt(Math.max(1, 1));
/*  112 */         tasksLeft = this.totalTasks;
/*      */       }
/*  114 */       else if (this.task == 4 || this.task == 7) {
/*      */         
/*  116 */         this.totalTasks = 1 + this.random.nextInt(2);
/*  117 */         tasksLeft = this.totalTasks;
/*      */       } 
/*      */     }
/*  120 */     this.firstTask = isFirstTask;
/*  121 */     this.tasksRemaining = tasksLeft;
/*      */     
/*  123 */     this.startX = 0;
/*  124 */     this.startY = 0;
/*  125 */     Server.getInstance().addTerraformingTask(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSXY(int sx, int sy) {
/*  136 */     this.startX = sx;
/*  137 */     this.startY = sy;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setHeight(int sheight) {
/*  142 */     this.startHeight = sheight;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setTotalTasks(int total) {
/*  147 */     this.totalTasks = total;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean setCoordinates() {
/*  152 */     boolean toReturn = false;
/*  153 */     switch (this.task)
/*      */     
/*      */     { case 0:
/*  156 */         toReturn = eruptCoord();
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
/*  178 */         return toReturn;case 1: toReturn = indentCoord(); return toReturn;case 2: case 4: toReturn = plateauCoord(); return toReturn;case 3: toReturn = craterCoord(); return toReturn;case 5: case 7: toReturn = ravineCoord(); return toReturn;case 6: toReturn = islandCoord(); return toReturn; }  toReturn = false; return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean poll() {
/*  183 */     if (this.next != null)
/*  184 */       return this.next.poll(); 
/*  185 */     if (this.counter == 0)
/*      */     {
/*  187 */       if (this.startX != 0 && this.startY != 0) {
/*  188 */         sendEffect();
/*  189 */       } else if (this.startX == 0 && this.startY == 0 && setCoordinates()) {
/*  190 */         sendEffect();
/*      */       }
/*      */       else {
/*      */         
/*  194 */         return true;
/*      */       } 
/*      */     }
/*  197 */     if (this.counter == 60) {
/*      */       
/*  199 */       terraform();
/*      */ 
/*      */       
/*  202 */       if (this.tasksRemaining == 0) {
/*  203 */         return true;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  209 */     if (this.counter == 65)
/*      */     {
/*  211 */       if (this.tasksRemaining > 0) {
/*      */         
/*  213 */         this.next = new TerraformingTask(this.task, this.kingdom, this.entityName, this.entityId, this.tasksRemaining - 1, false);
/*      */         
/*  215 */         this.next.setCoordinates();
/*      */         
/*  217 */         this.next.setSXY(this.startX, this.startY);
/*  218 */         if (this.task == 4 || this.task == 7) {
/*      */ 
/*      */           
/*  221 */           if (this.random.nextBoolean())
/*      */           {
/*  223 */             int modx = (this.startX + this.radius - this.startX - this.radius) / (1 + this.random.nextInt(4));
/*  224 */             int mody = (this.startY + this.radius - this.startY - this.radius) / (1 + this.random.nextInt(4));
/*  225 */             if (this.random.nextBoolean())
/*  226 */               modx = -modx; 
/*  227 */             if (this.random.nextBoolean()) {
/*  228 */               mody = -mody;
/*      */             }
/*  230 */             if (this.startX + modx > Zones.worldTileSizeX - 200)
/*  231 */               this.startX -= 200; 
/*  232 */             if (this.startY + mody > Zones.worldTileSizeY - 200)
/*  233 */               this.startY -= 200; 
/*  234 */             if (this.startX + modx < 200)
/*  235 */               this.startX += 200; 
/*  236 */             if (this.startY + mody < 200)
/*  237 */               this.startY += 200; 
/*  238 */             this.next.setSXY(this.startX + modx, this.startY + mody);
/*      */           }
/*      */         
/*  241 */         } else if (this.task == 3) {
/*      */           
/*  243 */           int modx = (int)(this.random.nextGaussian() * 3.0D * this.radius);
/*  244 */           int mody = (int)(this.random.nextGaussian() * 3.0D * this.radius);
/*  245 */           if (this.startX + modx > Zones.worldTileSizeX - 200)
/*  246 */             this.startX -= modx; 
/*  247 */           if (this.startY + mody > Zones.worldTileSizeY - 200)
/*  248 */             this.startY -= mody; 
/*  249 */           if (this.startX + modx < 200)
/*  250 */             this.startX += modx; 
/*  251 */           if (this.startY + mody < 200)
/*  252 */             this.startY += mody; 
/*  253 */           this.next.setSXY(this.startX + modx, this.startY + mody);
/*      */         } 
/*  255 */         this.next.setHeight(this.startHeight);
/*  256 */         this.next.setTotalTasks(this.totalTasks);
/*      */       } 
/*      */     }
/*  259 */     this.counter++;
/*  260 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void terraform() {
/*  265 */     switch (this.task) {
/*      */       
/*      */       case 0:
/*  268 */         erupt();
/*      */         break;
/*      */       case 1:
/*  271 */         indent();
/*      */         break;
/*      */       case 2:
/*      */       case 4:
/*  275 */         plateau();
/*      */         break;
/*      */       case 3:
/*  278 */         crater();
/*      */         break;
/*      */       case 5:
/*      */       case 7:
/*  282 */         ravine();
/*      */         break;
/*      */       case 6:
/*  285 */         island();
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean ravineCoord() {
/*  293 */     boolean toReturn = false;
/*  294 */     this.radius = 5 + this.random.nextInt(5);
/*      */     
/*  296 */     this.length = 20 + this.random.nextInt(40);
/*  297 */     this.direction = this.random.nextInt(8);
/*  298 */     for (int runs = 0; runs < 20; runs++) {
/*      */       
/*  300 */       if (this.firstTask) {
/*      */         
/*  302 */         this.startX = this.random.nextInt(Zones.worldTileSizeX);
/*  303 */         this.startY = this.random.nextInt(Zones.worldTileSizeY);
/*      */         
/*  305 */         if (this.startX > Zones.worldTileSizeX - 200)
/*  306 */           this.startX -= 200; 
/*  307 */         if (this.startY > Zones.worldTileSizeY - 200)
/*  308 */           this.startY -= 200; 
/*  309 */         if (this.startX < 200)
/*  310 */           this.startX += 200; 
/*  311 */         if (this.startY < 200) {
/*  312 */           this.startY += 200;
/*      */         }
/*  314 */         if (Tiles.decodeHeight(Server.surfaceMesh.getTile(this.startX, this.startY)) > 0)
/*      */         {
/*  316 */           if (isOutsideOwnKingdom(this.startX, this.startY)) {
/*      */             
/*  318 */             toReturn = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } else {
/*  324 */         return true;
/*      */       } 
/*  326 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ravine() {
/*  335 */     if (this.totalTasks > 0 && 
/*  336 */       this.totalTasks % 2 == 0) {
/*  337 */       this.direction = this.totalTasks;
/*      */     }
/*  339 */     IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*  340 */     Map<Integer, Set<Integer>> changes = null;
/*  341 */     changes = isl.createRavine(Zones.safeTileX(this.startX - this.radius), Zones.safeTileY(this.startY - this.radius), this.length, this.direction);
/*  342 */     logger.log(Level.INFO, "Ravine at " + this.startX + "," + this.startY);
/*      */     
/*  344 */     if (changes != null) {
/*      */       
/*  346 */       int minx = Zones.worldTileSizeX;
/*  347 */       int miny = Zones.worldTileSizeY;
/*  348 */       int maxx = 0;
/*  349 */       int maxy = 0;
/*      */       
/*  351 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  353 */         Integer x = me.getKey();
/*  354 */         if (x.intValue() < minx)
/*  355 */           minx = x.intValue(); 
/*  356 */         if (x.intValue() > maxx)
/*  357 */           maxx = x.intValue(); 
/*  358 */         Set<Integer> set = me.getValue();
/*  359 */         for (Integer y : set) {
/*      */           
/*  361 */           if (y.intValue() < miny)
/*  362 */             miny = y.intValue(); 
/*  363 */           if (y.intValue() > maxy)
/*  364 */             maxy = y.intValue(); 
/*  365 */           Terraforming.forceSetAsRock(x.intValue(), y.intValue(), Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id, 100);
/*  366 */           changeTile(x.intValue(), y.intValue());
/*  367 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*  368 */           destroyStructures(x.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  374 */         ItemFactory.createItem(696, 99.0F, ((minx + (maxx - minx) / 2) * 4 + 2), ((miny + (maxy - miny) / 2) * 4 + 2), this.random
/*  375 */             .nextFloat() * 350.0F, true, (byte)57, (byte)0, -10L, null);
/*      */       
/*      */       }
/*  378 */       catch (Exception ex) {
/*      */         
/*  380 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void destroyStructures(int x, int y) {
/*  387 */     VolaTile t = Zones.getTileOrNull(x, y, true);
/*  388 */     if (t != null) {
/*      */       
/*  390 */       short[] steepness = Creature.getTileSteepness(x, y, true);
/*  391 */       if (t.getStructure() != null)
/*      */       {
/*  393 */         if (steepness[1] > 40)
/*      */         {
/*  395 */           for (Wall w : t.getWalls())
/*      */           {
/*  397 */             w.setAsPlan();
/*      */           }
/*      */         }
/*      */       }
/*  401 */       for (Fence f : t.getFences()) {
/*      */         
/*  403 */         if (steepness[1] > 40)
/*      */         {
/*  405 */           f.destroy();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean craterCoord() {
/*  413 */     boolean toReturn = false;
/*  414 */     this.radius = 10 + this.random.nextInt(20);
/*  415 */     for (int runs = 0; runs < 20; runs++) {
/*      */       
/*  417 */       if (this.firstTask && this.startX <= 0 && this.startY <= 0) {
/*      */         
/*  419 */         this.startX = this.random.nextInt(Zones.worldTileSizeX);
/*  420 */         this.startY = this.random.nextInt(Zones.worldTileSizeY);
/*      */         
/*  422 */         if (Tiles.decodeHeight(Server.surfaceMesh.getTile(this.startX, this.startY)) > 0)
/*      */         {
/*  424 */           if (isOutsideOwnKingdom(this.startX, this.startY)) {
/*      */             
/*  426 */             toReturn = true;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } else {
/*  433 */         return true;
/*      */       } 
/*      */     } 
/*  436 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void changeTile(int x, int y) {
/*  441 */     VolaTile tile1 = Zones.getTileOrNull(x, y, true);
/*  442 */     if (tile1 != null) {
/*      */       
/*  444 */       Creature[] crets = tile1.getCreatures();
/*  445 */       for (Creature lCret2 : crets)
/*  446 */         lCret2.setChangedTileCounter(); 
/*  447 */       tile1.change();
/*      */     } 
/*  449 */     VolaTile tile2 = Zones.getTileOrNull(x, y, false);
/*  450 */     if (tile2 != null) {
/*      */       
/*  452 */       Creature[] crets = tile2.getCreatures();
/*  453 */       for (Creature lCret2 : crets)
/*  454 */         lCret2.setChangedTileCounter(); 
/*  455 */       tile2.change();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void crater() {
/*  461 */     boolean ok = true;
/*  462 */     if (this.radius == 0)
/*  463 */       this.radius = 10 + this.random.nextInt(20); 
/*  464 */     for (int x = 0; x < 10; x++) {
/*      */       
/*  466 */       int i = Zones.safeTileX(this.startX - this.radius);
/*  467 */       int j = Zones.safeTileY(this.startY - this.radius);
/*  468 */       int k = Zones.safeTileX(this.startX + this.radius);
/*  469 */       int m = Zones.safeTileY(this.startY + this.radius);
/*      */       
/*  471 */       Set<Village> blockers = Villages.getVillagesWithin(i, j, k, m);
/*  472 */       if (blockers == null || blockers.size() == 0) {
/*      */         
/*  474 */         ok = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  479 */       for (Village v : blockers)
/*      */       {
/*  481 */         logger.log(Level.WARNING, v.getName() + " is in the way at " + i + "," + j + " to " + k + "," + m);
/*      */       }
/*  483 */       ok = false;
/*      */       
/*  485 */       int modx = (int)(this.random.nextGaussian() * this.radius);
/*  486 */       int mody = (int)(this.random.nextGaussian() * this.radius);
/*      */       
/*  488 */       if (this.startX + modx > Zones.worldTileSizeX - 200)
/*  489 */         this.startX -= modx; 
/*  490 */       if (this.startY + mody > Zones.worldTileSizeY - 200)
/*  491 */         this.startY -= mody; 
/*  492 */       if (this.startX + modx < 200)
/*  493 */         this.startX += modx; 
/*  494 */       if (this.startY + mody < 200)
/*  495 */         this.startY += mody; 
/*  496 */       if (Servers.localServer.testServer) {
/*  497 */         logger.log(Level.INFO, "MOdx=" + modx + ", mody=" + mody + " radius=" + this.radius + " yields sx=" + (this.startX + modx) + " sy " + (this.startY + mody));
/*      */       }
/*  499 */       setSXY(this.startX + modx, this.startY + mody);
/*      */     } 
/*      */     
/*  502 */     if (!ok) {
/*      */       
/*  504 */       logger.log(Level.INFO, "Avoiding Crater at " + this.startX + "," + this.startY + " radius=" + this.radius);
/*      */       return;
/*      */     } 
/*  507 */     Map<Integer, Set<Integer>> changes = null;
/*  508 */     IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*      */     
/*  510 */     int sx = Zones.safeTileX(this.startX - this.radius);
/*  511 */     int sy = Zones.safeTileY(this.startY - this.radius);
/*  512 */     int ex = Zones.safeTileX(this.startX + this.radius);
/*  513 */     int ey = Zones.safeTileY(this.startY + this.radius);
/*  514 */     changes = isl.createCrater(sx, sy, ex, ey);
/*  515 */     logger.log(Level.INFO, "Crater at " + this.startX + "," + this.startY + " radius=" + this.radius);
/*      */     
/*  517 */     if (changes != null) {
/*      */       
/*  519 */       int minx = Zones.worldTileSizeX;
/*  520 */       int miny = Zones.worldTileSizeY;
/*  521 */       int maxx = 0;
/*  522 */       int maxy = 0;
/*      */       
/*  524 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  526 */         Integer integer = me.getKey();
/*  527 */         if (integer.intValue() < minx)
/*  528 */           minx = integer.intValue(); 
/*  529 */         if (integer.intValue() > maxx)
/*  530 */           maxx = integer.intValue(); 
/*  531 */         Set<Integer> set = me.getValue();
/*  532 */         for (Integer y : set) {
/*      */           
/*  534 */           if (y.intValue() < miny)
/*  535 */             miny = y.intValue(); 
/*  536 */           if (y.intValue() > maxy) {
/*  537 */             maxy = y.intValue();
/*      */           }
/*  539 */           Terraforming.forceSetAsRock(integer.intValue(), y.intValue(), Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL.id, 100);
/*  540 */           changeTile(integer.intValue(), y.intValue());
/*  541 */           Players.getInstance().sendChangedTile(integer.intValue(), y.intValue(), true, true);
/*  542 */           destroyStructures(integer.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*      */       
/*      */       try {
/*  547 */         ItemFactory.createItem(696, 99.0F, (this.startX * 4 + 2), (this.startY * 4 + 2), this.random
/*  548 */             .nextFloat() * 350.0F, true, (byte)57, (byte)0, -10L, null);
/*      */       }
/*  550 */       catch (Exception exs) {
/*      */         
/*  552 */         logger.log(Level.WARNING, exs.getMessage(), exs);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean indentCoord() {
/*  559 */     boolean toReturn = false;
/*  560 */     this.radius = 10 + this.random.nextInt(20);
/*  561 */     for (int runs = 0; runs < 20; runs++) {
/*      */       
/*  563 */       this.startX = this.random.nextInt(Zones.worldTileSizeX);
/*  564 */       this.startY = this.random.nextInt(Zones.worldTileSizeY);
/*      */       
/*  566 */       if (Tiles.decodeHeight(Server.surfaceMesh.getTile(this.startX, this.startY)) > 0)
/*      */       {
/*  568 */         if (isOutsideOwnKingdom(this.startX, this.startY)) {
/*      */           
/*  570 */           toReturn = true;
/*      */           break;
/*      */         } 
/*      */       }
/*      */     } 
/*  575 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private void indent() {
/*  580 */     Map<Integer, Set<Integer>> changes = null;
/*  581 */     IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*  582 */     changes = isl.createRockIndentation(Zones.safeTileX(this.startX - this.radius), Zones.safeTileY(this.startY - this.radius), 
/*  583 */         Zones.safeTileX(this.startX + this.radius), Zones.safeTileY(this.startY + this.radius));
/*  584 */     logger.log(Level.INFO, "Rock Indentation at " + this.startX + "," + this.startY);
/*      */     
/*  586 */     if (changes != null) {
/*      */       
/*  588 */       int minx = Zones.worldTileSizeX;
/*  589 */       int miny = Zones.worldTileSizeY;
/*  590 */       int maxx = 0;
/*  591 */       int maxy = 0;
/*      */       
/*  593 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  595 */         Integer x = me.getKey();
/*  596 */         if (x.intValue() < minx)
/*  597 */           minx = x.intValue(); 
/*  598 */         if (x.intValue() > maxx)
/*  599 */           maxx = x.intValue(); 
/*  600 */         Set<Integer> set = me.getValue();
/*  601 */         for (Integer y : set) {
/*      */           
/*  603 */           if (y.intValue() < miny)
/*  604 */             miny = y.intValue(); 
/*  605 */           if (y.intValue() > maxy)
/*  606 */             maxy = y.intValue(); 
/*  607 */           Terraforming.forceSetAsRock(x.intValue(), y.intValue(), (byte)1, 100);
/*  608 */           changeTile(x.intValue(), y.intValue());
/*  609 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*  610 */           destroyStructures(x.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean isOutsideOwnKingdom(int tilex, int tiley) {
/*  618 */     byte kingdomId = Zones.getKingdom(tilex, tiley);
/*  619 */     Kingdom k = Kingdoms.getKingdom(kingdomId);
/*  620 */     if (k == null || k.getTemplate() != this.kingdom)
/*  621 */       return true; 
/*  622 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean eruptCoord() {
/*  628 */     boolean toReturn = false;
/*  629 */     int maxTries = 20;
/*  630 */     for (int x = 0; x < 20; ) {
/*      */       
/*  632 */       Den d = Zones.getRandomTop();
/*  633 */       this.radius = 10 + this.random.nextInt(20);
/*  634 */       if (this.startX <= 0 && this.startY <= 0) {
/*      */         
/*  636 */         if (d != null)
/*      */         {
/*  638 */           if (isOutsideOwnKingdom(d.getTilex(), d.getTiley())) {
/*      */             
/*  640 */             this.startX = d.getTilex();
/*  641 */             this.startY = d.getTiley();
/*  642 */             toReturn = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */         x++;
/*      */       } 
/*      */     } 
/*  650 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private void erupt() {
/*  655 */     IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*  656 */     Map<Integer, Set<Integer>> changes = null;
/*  657 */     changes = isl.createVolcano(Zones.safeTileX(this.startX - this.radius), Zones.safeTileY(this.startY - this.radius), 
/*  658 */         Zones.safeTileX(this.startX + this.radius), Zones.safeTileY(this.startY + this.radius));
/*  659 */     logger.log(Level.INFO, "Volcano Eruption at " + this.startX + "," + this.startY);
/*      */     
/*  661 */     if (changes != null) {
/*      */       
/*  663 */       int minx = Zones.worldTileSizeX;
/*  664 */       int miny = Zones.worldTileSizeY;
/*  665 */       int maxx = 0;
/*  666 */       int maxy = 0;
/*      */       
/*  668 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  670 */         Integer x = me.getKey();
/*  671 */         if (x.intValue() < minx)
/*  672 */           minx = x.intValue(); 
/*  673 */         if (x.intValue() > maxx)
/*  674 */           maxx = x.intValue(); 
/*  675 */         Set<Integer> set = me.getValue();
/*  676 */         for (Integer y : set) {
/*      */           
/*  678 */           if (y.intValue() < miny)
/*  679 */             miny = y.intValue(); 
/*  680 */           if (y.intValue() > maxy)
/*  681 */             maxy = y.intValue(); 
/*  682 */           Terraforming.forceSetAsRock(x.intValue(), y.intValue(), Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE.id, 100);
/*  683 */           changeTile(x.intValue(), y.intValue());
/*  684 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*  685 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), false, true);
/*  686 */           destroyStructures(x.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*  689 */       String name = "Unknown";
/*  690 */       if (Server.rand.nextBoolean()) {
/*      */         
/*  692 */         name = this.prefixes[Server.rand.nextInt(this.prefixes.length)];
/*  693 */         if (Server.rand.nextInt(10) > 0)
/*      */         {
/*  695 */           name = name + this.suffixes[Server.rand.nextInt(this.suffixes.length)];
/*      */         }
/*      */       } 
/*  698 */       if (Server.rand.nextBoolean()) {
/*      */         
/*  700 */         name = this.suffixes[Server.rand.nextInt(this.suffixes.length)];
/*  701 */         if (Server.rand.nextInt(10) > 0)
/*      */         {
/*  703 */           name = name + this.prefixes[Server.rand.nextInt(this.prefixes.length)];
/*      */         }
/*      */       } 
/*  706 */       name = LoginHandler.raiseFirstLetter(name);
/*  707 */       new FocusZone(minx, maxx, miny, maxy, (byte)1, name, "", true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean plateauCoord() {
/*  713 */     boolean toReturn = false;
/*  714 */     this.radius = 10 + this.random.nextInt(20);
/*  715 */     for (int runs = 0; runs < 20; runs++) {
/*      */ 
/*      */       
/*  718 */       if (this.firstTask) {
/*      */         
/*  720 */         this.startX = this.random.nextInt(Zones.worldTileSizeX);
/*  721 */         this.startY = this.random.nextInt(Zones.worldTileSizeY);
/*  722 */         this.startHeight = 200;
/*      */ 
/*      */         
/*  725 */         if (Tiles.decodeHeight(Server.surfaceMesh.getTile(this.startX, this.startY)) > 0)
/*      */         {
/*  727 */           if (isOutsideOwnKingdom(this.startX, this.startY)) {
/*      */             
/*  729 */             toReturn = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } else {
/*  735 */         return true;
/*      */       } 
/*      */     } 
/*  738 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   private void plateau() {
/*  743 */     int modx = 0;
/*  744 */     int mody = 0;
/*  745 */     boolean ok = true;
/*  746 */     if (!this.firstTask)
/*      */     {
/*  748 */       for (int x = 0; x < 20; x++) {
/*      */         
/*  750 */         modx = (this.startX + this.radius - this.startX - this.radius) / (1 + this.random.nextInt(4));
/*  751 */         mody = (this.startY + this.radius - this.startY - this.radius) / (1 + this.random.nextInt(4));
/*  752 */         if (this.random.nextBoolean())
/*  753 */           modx = -modx; 
/*  754 */         if (this.random.nextBoolean()) {
/*  755 */           mody = -mody;
/*      */         }
/*  757 */         int i = Zones.safeTileX(this.startX + modx - this.radius);
/*  758 */         int j = Zones.safeTileX(this.startX + modx + this.radius);
/*  759 */         int k = Zones.safeTileY(this.startY + mody - this.radius);
/*  760 */         int m = Zones.safeTileY(this.startY + mody + this.radius);
/*  761 */         Set<Village> vills = Villages.getVillagesWithin(i, k, j, m);
/*  762 */         if (vills == null || vills.size() == 0) {
/*      */           
/*  764 */           ok = true;
/*      */           
/*      */           break;
/*      */         } 
/*  768 */         ok = false;
/*      */       } 
/*      */     }
/*  771 */     if (!ok) {
/*      */       
/*  773 */       logger.log(Level.INFO, "Skipping Plateu at " + this.startX + "," + this.startY);
/*      */       return;
/*      */     } 
/*  776 */     int sx = Zones.safeTileX(this.startX + modx - this.radius);
/*  777 */     int ex = Zones.safeTileX(this.startX + modx + this.radius);
/*  778 */     int sy = Zones.safeTileY(this.startY + mody - this.radius);
/*  779 */     int ey = Zones.safeTileY(this.startY + mody + this.radius);
/*      */     
/*  781 */     IslandAdder isl = new IslandAdder(Server.surfaceMesh, Server.rockMesh);
/*      */     
/*  783 */     Map<Integer, Set<Integer>> changes = null;
/*  784 */     changes = isl.createPlateau(sx, sy, ex, ey, this.startHeight + this.random.nextInt(150));
/*  785 */     logger.log(Level.INFO, "Plateu at " + this.startX + "," + this.startY);
/*      */     
/*  787 */     if (changes != null) {
/*      */       
/*  789 */       int minx = Zones.worldTileSizeX;
/*  790 */       int miny = Zones.worldTileSizeY;
/*  791 */       int maxx = 0;
/*  792 */       int maxy = 0;
/*      */       
/*  794 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  796 */         Integer x = me.getKey();
/*  797 */         if (x.intValue() < minx)
/*  798 */           minx = x.intValue(); 
/*  799 */         if (x.intValue() > maxx)
/*  800 */           maxx = x.intValue(); 
/*  801 */         Set<Integer> set = me.getValue();
/*  802 */         for (Integer y : set) {
/*      */           
/*  804 */           if (y.intValue() < miny)
/*  805 */             miny = y.intValue(); 
/*  806 */           if (y.intValue() > maxy)
/*  807 */             maxy = y.intValue(); 
/*  808 */           changeTile(x.intValue(), y.intValue());
/*  809 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*  810 */           destroyStructures(x.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendEffect() {
/*  818 */     int tx = 0;
/*  819 */     int ty = 0;
/*      */     
/*  821 */     switch (this.task) {
/*      */       
/*      */       case 0:
/*  824 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)12, this.startX, this.startY, 
/*  825 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(this.startX, this.startY)), this.radius, this.direction, this.length, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */       
/*      */       case 1:
/*  829 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)13, this.startX, this.startY, 
/*  830 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(this.startX, this.startY)), this.radius, this.direction, this.length, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */       
/*      */       case 2:
/*      */       case 4:
/*  835 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)11, this.startX, this.startY, 
/*  836 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(this.startX, this.startY)), this.radius, this.direction, this.length, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  841 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)10, this.startX, this.startY, 
/*  842 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(0, 0)), this.radius, this.tasksRemaining, this.direction, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */       
/*      */       case 5:
/*      */       case 7:
/*  847 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)14, 
/*  848 */             Zones.safeTileX(this.startX - this.radius), Zones.safeTileY(this.startY - this.radius), 
/*  849 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(this.startX, this.startY)), this.radius, this.length, this.direction, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*  856 */         Players.getInstance().sendGlobalNonPersistantComplexEffect(-10L, (short)15, this.startX, this.startY, 
/*  857 */             Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(this.startX, this.startY)), this.radius, this.length, this.direction, this.kingdom, (byte)this.entityId);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean islandCoord() {
/*  866 */     this.rockLayer = Server.rockMesh;
/*  867 */     this.topLayer = Server.surfaceMesh;
/*      */     
/*  869 */     int minSize = Zones.worldTileSizeX / 15;
/*  870 */     for (int i = 800; i >= minSize; i--) {
/*      */       
/*  872 */       for (int j = 0; j < 2; j++) {
/*      */         
/*  874 */         int width = i;
/*  875 */         int height = width;
/*  876 */         int x = this.random.nextInt(Zones.worldTileSizeX - width - 128) + 64;
/*  877 */         int y = this.random.nextInt(Zones.worldTileSizeY - width - 128) + 64;
/*      */         
/*  879 */         if (isIslandOk(x, y, x + width, y + height)) {
/*      */           
/*  881 */           this.startX = x + width / 2;
/*  882 */           this.startY = y + width / 2;
/*  883 */           this.length = width / 2;
/*  884 */           this.radius = height / 2;
/*  885 */           logger.info("Found island location " + i + " @ " + (x + width / 2) + ", " + (y + height / 2));
/*  886 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*  890 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void island() {
/*  895 */     Map<Integer, Set<Integer>> changes = null;
/*  896 */     changes = addIsland(this.startX - this.length, this.startX + this.length, this.startY - this.radius, this.startY + this.radius);
/*  897 */     if (changes != null)
/*      */     {
/*  899 */       for (Map.Entry<Integer, Set<Integer>> me : changes.entrySet()) {
/*      */         
/*  901 */         Integer x = me.getKey();
/*  902 */         Set<Integer> set = me.getValue();
/*  903 */         for (Integer y : set) {
/*      */           
/*  905 */           changeTile(x.intValue(), y.intValue());
/*  906 */           Players.getInstance().sendChangedTile(x.intValue(), y.intValue(), true, true);
/*  907 */           destroyStructures(x.intValue(), y.intValue());
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> addToChanges(Map<Integer, Set<Integer>> changes, int x, int y) {
/*  915 */     Set<Integer> s = changes.get(Integer.valueOf(x));
/*  916 */     if (s == null)
/*  917 */       s = new HashSet<>(); 
/*  918 */     if (!s.contains(Integer.valueOf(y)))
/*  919 */       s.add(Integer.valueOf(y)); 
/*  920 */     changes.put(Integer.valueOf(x), s);
/*  921 */     return changes;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isIslandOk(int x0, int y0, int x1, int y1) {
/*  926 */     int xm = (x1 + x0) / 2;
/*  927 */     int ym = (y1 + y0) / 2;
/*      */     
/*  929 */     for (int x = x0; x < x1; x++) {
/*      */       
/*  931 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  932 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  934 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  935 */         double d = Math.sqrt(xd * xd + yd * yd);
/*  936 */         if (d < 1.0D) {
/*      */           
/*  938 */           int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*      */           
/*  940 */           if (height > -2)
/*      */           {
/*  942 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  947 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> addIsland(int x0, int y0, int x1, int y1) {
/*  952 */     int xm = (x1 + x0) / 2;
/*  953 */     int ym = (y1 + y0) / 2;
/*  954 */     double dirOffs = this.random.nextDouble() * Math.PI * 2.0D;
/*      */ 
/*      */ 
/*      */     
/*  958 */     int branchCount = this.random.nextInt(7) + 3;
/*      */     
/*  960 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*  961 */     float[] branches = new float[branchCount];
/*  962 */     for (int i = 0; i < branchCount; i++)
/*      */     {
/*  964 */       branches[i] = this.random.nextFloat() * 0.25F + 0.75F;
/*      */     }
/*      */     
/*  967 */     ImprovedNoise noise = new ImprovedNoise(this.random.nextLong());
/*      */     int x;
/*  969 */     for (x = x0; x < x1; x++) {
/*      */       
/*  971 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  972 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  974 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  975 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  977 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  978 */         while (dir < 0.0D)
/*  979 */           dir++; 
/*  980 */         while (dir >= 1.0D) {
/*  981 */           dir--;
/*      */         }
/*  983 */         int branch = (int)(dir * branchCount);
/*  984 */         float step = (float)dir * branchCount - branch;
/*  985 */         float last = branches[branch];
/*  986 */         float nextBranch = branches[(branch + 1) % branchCount];
/*      */         
/*  988 */         float pow = last + (nextBranch - last) * step;
/*  989 */         double d = od;
/*  990 */         d /= pow;
/*      */         
/*  992 */         if (d < 1.0D) {
/*      */ 
/*      */           
/*  995 */           d *= d;
/*  996 */           d *= d;
/*  997 */           d = 1.0D - d;
/*      */           
/*  999 */           int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1000 */           float n = (float)(noise.perlinNoise(x, y) * 64.0D) + 100.0F;
/* 1001 */           n *= 2.0F;
/* 1002 */           int hh = (int)(height + (n - height) * d);
/*      */           
/* 1004 */           byte type = Tiles.Tile.TILE_DIRT.id;
/* 1005 */           if (hh > 5)
/*      */           {
/* 1007 */             if (this.random.nextInt(100) == 0)
/*      */             {
/* 1009 */               type = Tiles.Tile.TILE_GRASS.id;
/*      */             }
/*      */           }
/* 1012 */           if (hh > 0) {
/*      */             
/* 1014 */             hh = (int)(hh + 0.07F);
/*      */           }
/*      */           else {
/*      */             
/* 1018 */             hh = (int)(hh - 0.07F);
/*      */           } 
/* 1020 */           this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode((short)hh, type, (byte)0);
/* 1021 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1026 */     for (x = x0; x < x1; x++) {
/*      */       
/* 1028 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/* 1029 */       for (int y = y0; y < y1; y++) {
/*      */         
/* 1031 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/* 1032 */         double d = Math.sqrt(xd * xd + yd * yd);
/* 1033 */         double od = d * (x1 - x0);
/*      */         
/* 1035 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/* 1036 */         while (dir < 0.0D)
/* 1037 */           dir++; 
/* 1038 */         while (dir >= 1.0D) {
/* 1039 */           dir--;
/*      */         }
/* 1041 */         int branch = (int)(dir * branchCount);
/* 1042 */         float step = (float)dir * branchCount - branch;
/* 1043 */         float last = branches[branch];
/* 1044 */         float nextBranch = branches[(branch + 1) % branchCount];
/*      */         
/* 1046 */         float pow = last + (nextBranch - last) * step;
/* 1047 */         d /= pow;
/*      */         
/* 1049 */         int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1050 */         int dd = this.rockLayer.data[x | y << this.topLayer.getSizeLevel()];
/*      */         
/* 1052 */         float hh = height / 10.0F - 8.0F;
/*      */ 
/*      */         
/* 1055 */         d = 1.0D - d;
/*      */         
/* 1057 */         if (d < 0.0D)
/* 1058 */           d = 0.0D; 
/* 1059 */         d = Math.sin(d * Math.PI) * 2.0D - 1.0D;
/* 1060 */         if (d < 0.0D) {
/* 1061 */           d = 0.0D;
/*      */         }
/* 1063 */         float n = (float)noise.perlinNoise(x / 2.0D, y / 2.0D);
/* 1064 */         if (n > 0.5F)
/* 1065 */           n -= (n - 0.5F) * 2.0F; 
/* 1066 */         n /= 0.5F;
/* 1067 */         if (n < 0.0F)
/* 1068 */           n = 0.0F; 
/* 1069 */         hh = (float)(hh + (n * (x1 - x0) / 8.0F) * d);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1077 */         this.rockLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode(hh, Tiles.decodeType(dd), 
/* 1078 */             Tiles.decodeData(dd));
/* 1079 */         changes = addToChanges(changes, x, y);
/* 1080 */         float ddd = (float)od / 16.0F;
/* 1081 */         if (ddd < 1.0F) {
/*      */           
/* 1083 */           ddd = ddd * 2.0F - 1.0F;
/* 1084 */           if (ddd > 1.0F)
/* 1085 */             ddd = 1.0F; 
/* 1086 */           if (ddd < 0.0F)
/* 1087 */             ddd = 0.0F; 
/* 1088 */           dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/* 1089 */           float hh1 = Tiles.decodeHeightAsFloat(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1090 */           hh = Tiles.decodeHeightAsFloat(this.rockLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*      */           
/* 1092 */           hh += (hh1 - hh) * ddd;
/* 1093 */           this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode(hh, Tiles.decodeType(dd), 
/* 1094 */               Tiles.decodeData(dd));
/* 1095 */           changes = addToChanges(changes, x, y);
/*      */         }
/*      */         else {
/*      */           
/* 1099 */           dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/* 1100 */           hh = Tiles.decodeHeightAsFloat(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1101 */           hh = hh * 0.5F + ((int)hh / 2 * 2) * 0.5F;
/* 1102 */           if (hh > 0.0F) {
/*      */             
/* 1104 */             hh += 0.07F;
/*      */           }
/*      */           else {
/*      */             
/* 1108 */             hh -= 0.07F;
/*      */           } 
/*      */           
/* 1111 */           this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode(hh, Tiles.decodeType(dd), 
/* 1112 */               Tiles.decodeData(dd));
/* 1113 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1118 */     for (x = x0; x < x1; x++) {
/*      */       
/* 1120 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/* 1121 */       for (int y = y0; y < y1; y++) {
/*      */         
/* 1123 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/* 1124 */         double d = Math.sqrt(xd * xd + yd * yd);
/* 1125 */         double od = d * (x1 - x0);
/* 1126 */         boolean rock = true;
/*      */         
/* 1128 */         for (int xx = 0; xx < 2; xx++) {
/*      */           
/* 1130 */           for (int yy = 0; yy < 2; yy++) {
/*      */             
/* 1132 */             int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1133 */             int groundHeight = Tiles.decodeHeight(this.rockLayer.data[x | y << this.topLayer.getSizeLevel()]);
/* 1134 */             if (groundHeight < height) {
/*      */               
/* 1136 */               rock = false;
/*      */             }
/*      */             else {
/*      */               
/* 1140 */               int dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/* 1141 */               this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode((short)groundHeight, 
/* 1142 */                   Tiles.decodeType(dd), Tiles.decodeData(dd));
/* 1143 */               changes = addToChanges(changes, x, y);
/*      */             } 
/*      */           } 
/*      */         } 
/* 1147 */         if (rock) {
/*      */           
/* 1149 */           float ddd = (float)od / 16.0F;
/* 1150 */           if (ddd < 1.0F) {
/*      */             
/* 1152 */             int dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/* 1153 */             this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode(Tiles.decodeHeight(dd), Tiles.Tile.TILE_LAVA.id, (byte)-1);
/*      */             
/* 1155 */             changes = addToChanges(changes, x, y);
/*      */           }
/*      */           else {
/*      */             
/* 1159 */             int dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/* 1160 */             this.topLayer.data[x | y << this.topLayer.getSizeLevel()] = Tiles.encode(Tiles.decodeHeight(dd), Tiles.Tile.TILE_ROCK.id, (byte)0);
/*      */             
/* 1162 */             changes = addToChanges(changes, x, y);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1167 */     return changes;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TerraformingTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */