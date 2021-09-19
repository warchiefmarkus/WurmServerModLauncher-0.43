/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.structures.Blocker;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import com.wurmonline.server.structures.NoSuchStructureException;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */ public class PathFinder
/*     */   implements MiscConstants
/*     */ {
/*     */   protected PathTile current;
/*     */   protected PathTile finish;
/*     */   protected PathTile start;
/*     */   protected int startX;
/*     */   protected int startY;
/*     */   protected int endX;
/*     */   protected int endY;
/*     */   protected float derivX;
/*     */   protected float derivY;
/*  55 */   protected float restX = 0.0F;
/*  56 */   protected float restY = 0.0F;
/*     */   private LinkedList<PathTile> pathList;
/*     */   private LinkedList<PathTile> pList;
/*  59 */   protected Creature creature = null;
/*     */   private static final int NOT_FOUND = 0;
/*     */   private static final int FOUND = 1;
/*     */   private static final int NO_PATH = 2;
/*     */   private static final int MAX_DISTANCE = 50;
/*     */   protected boolean surfaced = true;
/*  65 */   protected int stepsTaken = 0;
/*     */   private static final int MAX_ASTAR_STEPS = 10000;
/*  67 */   protected int maxSteps = 10000;
/*     */   protected boolean debug = false;
/*  69 */   protected int areaSize = 2;
/*  70 */   protected PathMesh mesh = null;
/*  71 */   private static final int WORLD_SIZE = 1 << Constants.meshSize;
/*     */   
/*  73 */   private static final Logger logger = Logger.getLogger(PathFinder.class.getName());
/*     */   protected boolean ignoreWalls = false;
/*  75 */   protected int creatureHalfHeight = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathFinder() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public PathFinder(boolean ignoresWalls) {
/*  85 */     this.ignoreWalls = ignoresWalls;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path findPath(Creature aCreature, int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/*  91 */     this.creature = aCreature;
/*  92 */     if (this.creature != null)
/*  93 */       this.creatureHalfHeight = this.creature.getHalfHeightDecimeters(); 
/*  94 */     return findPath(startTileX, startTileY, endTileX, endTileY, surf, areaSz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Path findPath(int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/* 100 */     this.endX = Zones.safeTileX(endTileX);
/* 101 */     this.endY = Zones.safeTileY(endTileY);
/* 102 */     this.startX = Zones.safeTileX(startTileX);
/* 103 */     this.startY = Zones.safeTileY(startTileY);
/* 104 */     int _diffX = Math.abs(this.endX - this.startX);
/* 105 */     int _diffY = Math.abs(this.endY - this.startY);
/* 106 */     if (_diffX > 50) {
/*     */       
/* 108 */       int stepsX = Server.rand.nextInt(Math.min(50, _diffX + 1));
/* 109 */       if (this.endX < this.startX)
/* 110 */         stepsX = -stepsX; 
/* 111 */       this.endX = this.startX + stepsX;
/*     */     } 
/* 113 */     if (_diffY > 50) {
/*     */       
/* 115 */       int stepsY = Server.rand.nextInt(Math.min(50, _diffY + 1));
/* 116 */       if (this.endY < this.startY)
/* 117 */         stepsY = -stepsY; 
/* 118 */       this.endY = this.startY + stepsY;
/*     */     } 
/* 120 */     this.startX = Zones.safeTileX(this.startX);
/* 121 */     this.startY = Zones.safeTileY(this.startY);
/* 122 */     this.endX = Zones.safeTileX(this.endX);
/* 123 */     this.endY = Zones.safeTileY(this.endY);
/*     */     
/* 125 */     this.surfaced = surf;
/* 126 */     this.areaSize = areaSz;
/* 127 */     setMesh();
/* 128 */     Path toReturn = new Path();
/* 129 */     if (this.surfaced) {
/*     */       
/*     */       try
/*     */       {
/*     */         
/* 134 */         toReturn = rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 143 */       catch (NoPathException np)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 154 */         toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 167 */       toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (this.mesh != null)
/* 173 */       this.mesh.clearPathables(); 
/* 174 */     return toReturn;
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
/*     */   Path startAstar(int _startX, int _startY, int _endX, int _endY) throws NoPathException {
/* 190 */     Path toReturn = new Path();
/*     */     
/*     */     try {
/* 193 */       toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/* 194 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 196 */         logger.finest(this.creature.getName() + " astared a path.");
/*     */       }
/*     */     }
/* 199 */     catch (NoPathException np2) {
/*     */       
/* 201 */       if (this.creature != null) {
/*     */         
/* 203 */         if ((this.creature.isKingdomGuard() || this.creature.isSpiritGuard() || this.creature.isUnique() || this.creature.isDominated()) && this.creature.target == -10L) {
/*     */ 
/*     */           
/* 206 */           int _diffX = Math.max(1, Math.abs(_endX - _startX) / 2);
/* 207 */           int _diffY = Math.max(1, Math.abs(_endY - _startY) / 2);
/*     */           
/* 209 */           int stepsX = Server.rand.nextInt(Math.min(50, _diffX + 1));
/* 210 */           if (this.endX < this.startX)
/* 211 */             stepsX = -stepsX; 
/* 212 */           this.endX = this.startX + stepsX;
/* 213 */           int stepsY = Server.rand.nextInt(Math.min(50, _diffY + 1));
/* 214 */           if (this.endY < this.startY)
/* 215 */             stepsY = -stepsY; 
/* 216 */           this.endY = this.startY + stepsY;
/* 217 */           if (stepsY != 0 || stepsX != 0) {
/*     */             
/* 219 */             setMesh();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 224 */             if (!this.surfaced || this.creature.isKingdomGuard() || this.creature.isUnique() || this.creature.isDominated()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 234 */               toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             } else {
/*     */               
/* 237 */               toReturn = rayCast(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 243 */           throw np2;
/*     */         } 
/*     */       } else {
/* 246 */         throw np2;
/*     */       } 
/* 248 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/* 254 */     this.startX = Math.max(0, startTileX);
/* 255 */     this.startY = Math.max(0, startTileY);
/* 256 */     this.endX = Math.min(WORLD_SIZE - 1, endTileX);
/* 257 */     this.endY = Math.min(WORLD_SIZE - 1, endTileY);
/* 258 */     this.surfaced = surf;
/* 259 */     this.areaSize = areaSz;
/* 260 */     setMesh();
/*     */     
/* 262 */     return rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setMesh() {
/* 271 */     this.mesh = new PathMesh(this.startX, this.startY, this.endX, this.endY, this.surfaced, this.areaSize);
/*     */ 
/*     */     
/* 274 */     this.current = this.mesh.getStart();
/* 275 */     this.start = this.mesh.getStart();
/* 276 */     this.finish = this.mesh.getFinish();
/* 277 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 279 */       logger.finest("Start is " + this.start.toString() + ", finish " + this.finish.toString());
/*     */     }
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
/*     */   Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean aSurfaced) throws NoPathException {
/* 298 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 299 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 300 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/* 301 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 302 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight)
/* 303 */       throw new NoPathException("Target in water."); 
/* 304 */     this.maxSteps = Math.max(Math.abs(this.endX - this.startX), Math.abs(this.endY - this.startY)) + 1;
/* 305 */     this.pathList = new LinkedList<>();
/* 306 */     float diffX = (this.endX - this.startX);
/* 307 */     float diffY = (this.endY - this.startY);
/* 308 */     if (diffX == 0.0F) {
/*     */       
/* 310 */       this.derivX = 0.0F;
/* 311 */       this.derivY = diffY;
/*     */     } 
/* 313 */     if (diffY == 0.0F) {
/*     */       
/* 315 */       this.derivY = 0.0F;
/* 316 */       this.derivX = diffX;
/*     */     } 
/* 318 */     if (diffX != 0.0F && diffY != 0.0F) {
/*     */       
/* 320 */       this.derivX = Math.abs(diffX / diffY);
/* 321 */       this.derivY = Math.abs(diffY / diffX);
/*     */     } 
/* 323 */     if (diffY < 0.0F && this.derivY > 0.0F)
/* 324 */       this.derivY = -this.derivY; 
/* 325 */     if (diffX < 0.0F && this.derivX > 0.0F)
/* 326 */       this.derivX = -this.derivX; 
/* 327 */     while (!this.current.equals(this.finish)) {
/*     */       
/* 329 */       this.current = step();
/* 330 */       this.pathList.add(this.current);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 336 */     return new Path(this.pathList);
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
/*     */   PathTile step() throws NoPathException {
/* 348 */     int x = this.current.getTileX();
/* 349 */     int y = this.current.getTileY();
/* 350 */     boolean raycend = true;
/* 351 */     if (!this.surfaced)
/* 352 */       raycend = false; 
/* 353 */     if (raycend && Math.abs(this.endX - x) <= 1 && Math.abs(this.endY - y) <= 1) {
/*     */ 
/*     */       
/* 356 */       x = this.endX;
/* 357 */       y = this.endY;
/*     */     }
/* 359 */     else if (Math.abs(this.endX - x) < 1 && Math.abs(this.endY - y) < 1) {
/*     */       
/* 361 */       x = this.endX;
/* 362 */       y = this.endY;
/* 363 */       logger.log(Level.INFO, "This really shouldn't happen i guess, since it should have been detected already.");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 368 */       if (this.derivX > 0.0F && x < this.endX) {
/*     */         
/* 370 */         if (this.derivX >= 1.0F) {
/* 371 */           x++;
/*     */         } else {
/*     */           
/* 374 */           this.restX += this.derivX;
/* 375 */           if (this.restX >= 1.0F)
/*     */           {
/* 377 */             x++;
/* 378 */             this.restX--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 383 */       } else if (this.derivX < 0.0F && x > this.endX) {
/*     */         
/* 385 */         if (this.derivX <= -1.0F) {
/* 386 */           x--;
/*     */         } else {
/*     */           
/* 389 */           this.restX += this.derivX;
/* 390 */           if (this.restX <= -1.0F) {
/*     */             
/* 392 */             x--;
/* 393 */             this.restX++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 398 */       if (this.derivY > 0.0F && y < this.endY) {
/*     */         
/* 400 */         if (this.derivY >= 1.0F) {
/* 401 */           y++;
/*     */         } else {
/*     */           
/* 404 */           this.restY += this.derivY;
/* 405 */           if (this.restY >= 1.0F)
/*     */           {
/* 407 */             y++;
/* 408 */             this.restY--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 413 */       } else if (this.derivY < 0.0F && y > this.endY) {
/*     */         
/* 415 */         if (this.derivY <= -1.0F) {
/*     */           
/* 417 */           y--;
/*     */         }
/*     */         else {
/*     */           
/* 421 */           this.restY += this.derivY;
/* 422 */           if (this.restY <= -1.0F) {
/*     */             
/* 424 */             y--;
/* 425 */             this.restY++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 430 */     if (!this.mesh.contains(x, y))
/* 431 */       throw new NoPathException("Path missed at " + x + ", " + y); 
/* 432 */     PathTile toReturn = null;
/*     */     
/*     */     try {
/* 435 */       toReturn = this.mesh.getPathTile(x, y);
/*     */       
/* 437 */       if (!canPass(this.current, toReturn))
/*     */       {
/* 439 */         throw new NoPathException("Path blocked between " + this.current.toString() + " and " + toReturn.toString());
/*     */       }
/*     */     }
/* 442 */     catch (ArrayIndexOutOfBoundsException ai) {
/*     */       
/* 444 */       logger.log(Level.WARNING, "OUT OF BOUNDS AT RAYCAST: " + x + ", " + y + ": " + ai.getMessage(), ai);
/* 445 */       logger.log(Level.WARNING, "Mesh info: " + this.mesh.getBorderStartX() + ", " + this.mesh.getBorderStartY() + ", to " + this.mesh
/* 446 */           .getBorderEndX() + ", " + this.mesh.getBorderEndY());
/* 447 */       logger.log(Level.WARNING, "Size of meshx=" + this.mesh.getSizex() + ", meshy=" + this.mesh.getSizey());
/* 448 */       throw new NoPathException("Path missed at " + x + ", " + y);
/*     */     } 
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
/* 466 */     if (this.stepsTaken > this.maxSteps) {
/*     */       
/* 468 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 470 */         logger.finest("Raycaster stops searching after " + this.stepsTaken + " steps, suspecting it missed the target.");
/*     */       }
/* 472 */       throw new NoPathException("Probably missed target using raycaster.");
/*     */     } 
/* 474 */     this.stepsTaken++;
/* 475 */     return toReturn;
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
/*     */   static float cbDist(PathTile a, PathTile b, float low) {
/* 489 */     return low * (Math.abs(a.getTileX() - b.getTileX()) + Math.abs(a.getTileY() - b.getTileY()) - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static float getCost(int tile) {
/* 499 */     if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/* 500 */       return Float.MAX_VALUE; 
/* 501 */     if (Tiles.decodeHeight(tile) < 1)
/*     */     {
/* 503 */       return 3.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 509 */     return 1.0F;
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
/*     */   Path astar(int startTileX, int startTileY, int endTileX, int endTileY, boolean aSurfaced) throws NoPathException {
/* 543 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 544 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 545 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/*     */       
/* 547 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 548 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight) {
/* 549 */       throw new NoPathException("Target in water.");
/*     */     }
/* 551 */     this.pathList = new LinkedList<>();
/*     */ 
/*     */     
/* 554 */     if (this.start != null && this.finish != null && this.start.equals(this.finish))
/* 555 */       return null; 
/* 556 */     if (this.finish == null) {
/*     */       
/* 558 */       if (this.creature != null) {
/* 559 */         logger.log(Level.WARNING, this.creature.getName() + " finish=null at " + endTileX + ", " + endTileY);
/*     */       } else {
/* 561 */         logger.log(Level.WARNING, "Finish=null at " + endTileX + ", " + endTileY);
/* 562 */       }  return null;
/*     */     } 
/* 564 */     if (this.start == null) {
/*     */       
/* 566 */       if (this.creature != null) {
/* 567 */         logger.log(Level.WARNING, this.creature.getName() + " start=null at " + startTileX + ", " + startTileY);
/*     */       } else {
/* 569 */         logger.log(Level.WARNING, "start=null at " + startTileX + ", " + startTileY);
/* 570 */       }  return null;
/*     */     } 
/* 572 */     this.start.setDistanceFromStart(this.start, 0.0F);
/* 573 */     this.pathList.add(this.start);
/*     */     
/* 575 */     int pass = 0;
/*     */     
/* 577 */     int lState = 0;
/* 578 */     while (lState == 0 && pass < 10000) {
/*     */       
/* 580 */       pass++;
/*     */       
/* 582 */       lState = step2();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 592 */     if (lState == 1) {
/*     */       
/* 594 */       if (pass > 4000) {
/*     */         
/* 596 */         String cname = "Unknown";
/* 597 */         if (this.creature != null)
/* 598 */           cname = this.creature.getName(); 
/* 599 */         logger.log(Level.INFO, cname + " pathed from " + this.startX + ", " + this.startY + " to " + this.endX + ", " + this.endY + " and found path after " + pass + " steps.");
/*     */       } 
/*     */ 
/*     */       
/* 603 */       return setPath();
/*     */     } 
/*     */ 
/*     */     
/* 607 */     if (lState == 2) {
/* 608 */       throw new NoPathException("No path possible after " + pass + " tries.");
/*     */     }
/* 610 */     throw new NoPathException("No path found after " + pass + " tries.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDebug(boolean aDebug) {
/* 620 */     this.debug = aDebug;
/* 621 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 623 */       logger.finest("Debug in pathfinding - " + aDebug);
/*     */     }
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
/*     */   int step2() {
/* 639 */     boolean found = false;
/* 640 */     float min = Float.MAX_VALUE;
/*     */     
/* 642 */     float score = 0.0F;
/* 643 */     PathTile best = this.pathList.get(this.pathList.size() - 1);
/* 644 */     PathTile now = null;
/*     */     
/* 646 */     for (int i = 0; i < this.pathList.size(); i++) {
/*     */       
/* 648 */       now = this.pathList.get(i);
/* 649 */       score = now.getDistanceFromStart();
/* 650 */       score += cbDist(now, this.finish, getCost(now.getTile()));
/*     */       
/* 652 */       if (!now.isUsed())
/*     */       {
/* 654 */         if (score < min) {
/*     */           
/* 656 */           min = score;
/* 657 */           best = now;
/*     */         } 
/*     */       }
/*     */     } 
/* 661 */     now = best;
/* 662 */     this.pathList.remove(now);
/* 663 */     now.setUsed();
/*     */     
/* 665 */     PathTile[] next = this.mesh.getAdjacent(now);
/*     */     
/* 667 */     for (int j = 0; j < next.length; j++) {
/*     */       
/* 669 */       if (next[j] != null)
/*     */       {
/* 671 */         if (canPass(now, next[j])) {
/*     */           
/* 673 */           if (!this.pathList.contains(next[j]) && next[j].isNotUsed())
/*     */           {
/* 675 */             this.pathList.add(next[j]);
/*     */           }
/* 677 */           if (next[j] == this.finish)
/*     */           {
/* 679 */             found = true;
/*     */           }
/* 681 */           score = now.getDistanceFromStart() + next[j].getMoveCost();
/* 682 */           next[j].setDistanceFromStart(now, score);
/*     */         } 
/*     */       }
/* 685 */       if (found)
/* 686 */         return 1; 
/*     */     } 
/* 688 */     if (this.pathList.isEmpty())
/* 689 */       return 2; 
/* 690 */     return 0;
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
/*     */   boolean canPass(PathTile aStart, PathTile end) {
/* 707 */     if (Tiles.isSolidCave(Tiles.decodeType(end.getTile()))) {
/* 708 */       return false;
/*     */     }
/* 710 */     if (this.creature != null) {
/*     */       
/* 712 */       if (this.creature.isGhost())
/* 713 */         return true; 
/* 714 */       if (Tiles.decodeType(end.getTile()) == Tiles.Tile.TILE_LAVA.id)
/*     */       {
/* 716 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 721 */     if (this.creature == null) {
/*     */       
/* 723 */       if (!this.ignoreWalls)
/*     */       {
/* 725 */         BlockingResult result = Blocking.getBlockerBetween(this.creature, aStart.getPositionX(), aStart
/* 726 */             .getPositionY(), end.getPositionX(), end.getPositionY(), aStart
/* 727 */             .getMinZ(), end.getMinZ(), this.surfaced, end.isSurfaced(), false, 6, -1L, -10L, -10L, false);
/*     */ 
/*     */         
/* 730 */         if (result != null)
/*     */         {
/* 732 */           return false;
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 746 */       if (this.creature.isSubmerged() && Tiles.decodeHeight(end.getTile()) > -20)
/* 747 */         return false; 
/* 748 */       if (!this.ignoreWalls) {
/*     */         
/* 750 */         BlockingResult result = Blocking.getBlockerBetween(this.creature, aStart.getPositionX(), aStart
/* 751 */             .getPositionY(), end.getPositionX(), end.getPositionY(), this.creature.getPositionZ(), this.creature
/* 752 */             .getPositionZ(), this.surfaced, end.isSurfaced(), false, 6, -1L, this.creature
/* 753 */             .getBridgeId(), this.creature.getBridgeId(), this.creature.followsGround());
/*     */         
/* 755 */         if (result != null) {
/*     */           
/* 757 */           Blocker first = result.getFirstBlocker();
/*     */           
/* 759 */           if (first.isDoor() && first.canBeOpenedBy(this.creature, false)) {
/*     */             
/* 761 */             if (this.creature.canOpenDoors())
/* 762 */               end.setDoor(true); 
/* 763 */             return this.creature.canOpenDoors();
/*     */           } 
/* 765 */           if (this.creature.getBridgeId() > 0L) {
/*     */             
/*     */             try {
/*     */               
/* 769 */               Structure bridge = Structures.getStructure(this.creature.getBridgeId());
/*     */               
/* 771 */               if (bridge.contains(this.start.getTileX(), this.start.getTileY()))
/*     */               {
/* 773 */                 if (bridge.isHorizontal()) {
/*     */                   
/* 775 */                   if (end.getTileY() != this.start.getTileY())
/*     */                   {
/* 777 */                     return false;
/*     */                   }
/*     */                 }
/* 780 */                 else if (end.getTileX() != this.start.getTileX()) {
/*     */                   
/* 782 */                   return false;
/*     */                 } 
/*     */               }
/* 785 */               for (Blocker blocker : result.getBlockerArray()) {
/*     */                 
/* 787 */                 if ((blocker.getCenterPoint()).z > this.creature.getPositionZ())
/*     */                 {
/* 789 */                   return false;
/*     */                 }
/*     */               } 
/* 792 */               return true;
/*     */             }
/* 794 */             catch (NoSuchStructureException nss) {
/*     */               
/* 796 */               logger.log(Level.WARNING, this.creature
/* 797 */                   .getWurmId() + " at " + this.creature.getTileX() + "," + this.creature.getTileY() + " no structure");
/*     */             } 
/*     */           }
/*     */           
/* 801 */           return false;
/*     */         } 
/* 803 */         if (this.creature.getBridgeId() > 0L) {
/*     */           
/*     */           try {
/*     */             
/* 807 */             Structure bridge = Structures.getStructure(this.creature.getBridgeId());
/*     */             
/* 809 */             if (bridge.contains(this.start.getTileX(), this.start.getTileY()))
/*     */             {
/* 811 */               if (bridge.isHorizontal())
/*     */               {
/* 813 */                 if (end.getTileY() != this.start.getTileY())
/*     */                 {
/* 815 */                   return false;
/*     */                 }
/*     */               }
/* 818 */               else if (end.getTileX() != this.start.getTileX())
/*     */               {
/* 820 */                 return false;
/*     */               }
/*     */             
/*     */             }
/* 824 */           } catch (NoSuchStructureException nss) {
/*     */             
/* 826 */             logger.log(Level.WARNING, this.creature
/* 827 */                 .getWurmId() + " at " + this.creature.getTileX() + "," + this.creature.getTileY() + " no structure");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 839 */     return true;
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
/*     */   private PathTile findLowestDist(PathTile aStart, PathTile now) {
/* 873 */     return now.getLink();
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
/*     */   Path setPath() {
/* 885 */     setDebug(this.debug);
/* 886 */     boolean finished = false;
/*     */     
/* 888 */     PathTile now = this.finish;
/* 889 */     PathTile stop = this.start;
/* 890 */     this.pList = new LinkedList<>();
/* 891 */     PathTile lastCurrent = now;
/*     */     
/* 893 */     while (!finished) {
/*     */ 
/*     */ 
/*     */       
/* 897 */       this.pList.add(now);
/*     */       
/* 899 */       PathTile next = findLowestDist(this.start, now);
/* 900 */       if (lastCurrent.equals(next)) {
/*     */         
/* 902 */         finished = true;
/* 903 */         logger.log(Level.WARNING, "Loop in heuristicastar.");
/*     */       } 
/* 905 */       lastCurrent = now;
/* 906 */       now = next;
/* 907 */       if (now.equals(stop)) {
/*     */         
/* 909 */         if (Math.abs(lastCurrent.getTileX() - now.getTileX()) > 1 || 
/* 910 */           Math.abs(lastCurrent.getTileY() - now.getTileY()) > 1)
/* 911 */           this.pList.add(now); 
/* 912 */         finished = true;
/*     */       } 
/*     */     } 
/* 915 */     LinkedList<PathTile> inverted = new LinkedList<>();
/* 916 */     for (Iterator<PathTile> it = this.pList.iterator(); it.hasNext();)
/*     */     {
/* 918 */       inverted.addFirst(it.next());
/*     */     }
/*     */     
/* 921 */     Path path = new Path(inverted);
/*     */     
/* 923 */     setDebug(false);
/* 924 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\PathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */