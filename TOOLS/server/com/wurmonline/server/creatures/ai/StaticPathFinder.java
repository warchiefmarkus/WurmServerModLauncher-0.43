/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
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
/*     */ public final class StaticPathFinder
/*     */   extends PathFinder
/*     */   implements MiscConstants
/*     */ {
/*     */   private static LinkedList<PathTile> pathList;
/*     */   private static LinkedList<PathTile> pList;
/*     */   private static final int NOT_FOUND = 0;
/*     */   private static final int FOUND = 1;
/*     */   private static final int NO_PATH = 2;
/*     */   private static final int MAX_DISTANCE = 50;
/*     */   private static final int MAX_ASTAR_STEPS = 10000;
/*  45 */   private static StaticPathMesh mesh = null;
/*  46 */   private static final int WORLD_SIZE = 1 << Constants.meshSize;
/*     */   
/*  48 */   private static final Logger logger = Logger.getLogger(PathFinder.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticPathFinder() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticPathFinder(boolean ignoresWalls) {
/*  58 */     this.ignoreWalls = ignoresWalls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path findPath(Creature aCreature, int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/*  65 */     this.creature = aCreature;
/*  66 */     if (this.creature != null)
/*  67 */       this.creatureHalfHeight = this.creature.getHalfHeightDecimeters(); 
/*  68 */     return findPath(startTileX, startTileY, endTileX, endTileY, surf, areaSz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Path findPath(int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/*  74 */     this.endX = Zones.safeTileX(endTileX);
/*  75 */     this.endY = Zones.safeTileY(endTileY);
/*  76 */     this.startX = Zones.safeTileX(startTileX);
/*  77 */     this.startY = Zones.safeTileY(startTileY);
/*  78 */     int _diffX = Math.abs(this.endX - this.startX);
/*  79 */     int _diffY = Math.abs(this.endY - this.startY);
/*  80 */     if (_diffX > 50) {
/*     */       
/*  82 */       int stepsX = Server.rand.nextInt(Math.min(50, _diffX + 1));
/*  83 */       if (this.endX < this.startX)
/*  84 */         stepsX = -stepsX; 
/*  85 */       this.endX = this.startX + stepsX;
/*     */     } 
/*  87 */     if (_diffY > 50) {
/*     */       
/*  89 */       int stepsY = Server.rand.nextInt(Math.min(50, _diffY + 1));
/*  90 */       if (this.endY < this.startY)
/*  91 */         stepsY = -stepsY; 
/*  92 */       this.endY = this.startY + stepsY;
/*     */     } 
/*  94 */     this.startX = Zones.safeTileX(this.startX);
/*  95 */     this.startY = Zones.safeTileY(this.startY);
/*  96 */     this.endX = Zones.safeTileX(this.endX);
/*  97 */     this.endY = Zones.safeTileY(this.endY);
/*     */     
/*  99 */     this.surfaced = surf;
/* 100 */     this.areaSize = areaSz;
/* 101 */     setMesh();
/* 102 */     Path toReturn = new Path();
/* 103 */     if (this.surfaced) {
/*     */       
/*     */       try
/*     */       {
/*     */ 
/*     */         
/* 109 */         toReturn = rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 118 */       catch (NoPathException np)
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
/* 129 */         toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
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
/* 142 */       toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 147 */     return toReturn;
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
/*     */   Path startAstar(int _startX, int _startY, int _endX, int _endY) throws NoPathException {
/* 164 */     Path toReturn = new Path();
/*     */     
/*     */     try {
/* 167 */       toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/* 168 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 170 */         logger.finest(this.creature.getName() + " astared a path.");
/*     */       }
/*     */     }
/* 173 */     catch (NoPathException np2) {
/*     */       
/* 175 */       if (this.creature != null) {
/*     */         
/* 177 */         if ((this.creature.isKingdomGuard() || this.creature.isSpiritGuard() || this.creature.isUnique() || this.creature.isDominated()) && this.creature.target == -10L) {
/*     */ 
/*     */           
/* 180 */           int _diffX = Math.max(1, Math.abs(_endX - _startX) / 2);
/* 181 */           int _diffY = Math.max(1, Math.abs(_endY - _startY) / 2);
/*     */           
/* 183 */           int stepsX = Server.rand.nextInt(Math.min(50, _diffX + 1));
/* 184 */           if (this.endX < this.startX)
/* 185 */             stepsX = -stepsX; 
/* 186 */           this.endX = this.startX + stepsX;
/* 187 */           int stepsY = Server.rand.nextInt(Math.min(50, _diffY + 1));
/* 188 */           if (this.endY < this.startY)
/* 189 */             stepsY = -stepsY; 
/* 190 */           this.endY = this.startY + stepsY;
/* 191 */           if (stepsY != 0 || stepsX != 0) {
/*     */             
/* 193 */             setMesh();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 198 */             if (!this.surfaced || this.creature.isKingdomGuard() || this.creature.isUnique() || this.creature.isDominated()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 208 */               toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             } else {
/*     */               
/* 211 */               toReturn = rayCast(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 217 */           throw np2;
/*     */         } 
/*     */       } else {
/* 220 */         throw np2;
/*     */       } 
/* 222 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/* 229 */     this.startX = Math.max(0, startTileX);
/* 230 */     this.startY = Math.max(0, startTileY);
/* 231 */     this.endX = Math.min(WORLD_SIZE - 1, endTileX);
/* 232 */     this.endY = Math.min(WORLD_SIZE - 1, endTileY);
/* 233 */     this.surfaced = surf;
/* 234 */     this.areaSize = areaSz;
/* 235 */     setMesh();
/*     */     
/* 237 */     return rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setMesh() {
/* 247 */     StaticPathMesh.clearPathables();
/* 248 */     mesh = new StaticPathMesh(this.startX, this.startY, this.endX, this.endY, this.surfaced, this.areaSize);
/*     */     
/* 250 */     this.current = mesh.getStart();
/* 251 */     this.start = mesh.getStart();
/* 252 */     this.finish = mesh.getFinish();
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
/*     */   Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean aSurfaced) throws NoPathException {
/* 271 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 272 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 273 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/* 274 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 275 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight)
/* 276 */       throw new NoPathException("Target in water."); 
/* 277 */     this.maxSteps = Math.max(Math.abs(this.endX - this.startX), Math.abs(this.endY - this.startY)) + 1;
/* 278 */     this.stepsTaken = 0;
/* 279 */     pathList = new LinkedList<>();
/* 280 */     float diffX = (this.endX - this.startX);
/* 281 */     float diffY = (this.endY - this.startY);
/* 282 */     if (diffX == 0.0F) {
/*     */       
/* 284 */       this.derivX = 0.0F;
/* 285 */       this.derivY = diffY;
/*     */     } 
/* 287 */     if (diffY == 0.0F) {
/*     */       
/* 289 */       this.derivY = 0.0F;
/* 290 */       this.derivX = diffX;
/*     */     } 
/* 292 */     if (diffX != 0.0F && diffY != 0.0F) {
/*     */       
/* 294 */       this.derivX = Math.abs(diffX / diffY);
/* 295 */       this.derivY = Math.abs(diffY / diffX);
/*     */     } 
/* 297 */     if (diffY < 0.0F && this.derivY > 0.0F)
/*     */     {
/* 299 */       this.derivY = -this.derivY;
/*     */     }
/* 301 */     if (diffX < 0.0F && this.derivX > 0.0F)
/*     */     {
/* 303 */       this.derivX = -this.derivX;
/*     */     }
/*     */     
/* 306 */     while (!this.current.equals(this.finish)) {
/*     */       
/* 308 */       this.current = step();
/* 309 */       pathList.add(this.current);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     return new Path(pathList);
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
/*     */   PathTile step() throws NoPathException {
/* 328 */     int x = this.current.getTileX();
/* 329 */     int y = this.current.getTileY();
/* 330 */     boolean raycend = true;
/* 331 */     if (!this.surfaced)
/* 332 */       raycend = false; 
/* 333 */     if (raycend && Math.abs(this.endX - x) <= 1 && Math.abs(this.endY - y) <= 1) {
/*     */ 
/*     */       
/* 336 */       x = this.endX;
/* 337 */       y = this.endY;
/*     */     }
/* 339 */     else if (Math.abs(this.endX - x) < 1 && Math.abs(this.endY - y) < 1) {
/*     */       
/* 341 */       x = this.endX;
/* 342 */       y = this.endY;
/* 343 */       logger.log(Level.INFO, "This really shouldn't happen i guess, since it should have been detected already.");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 348 */       if (this.derivX > 0.0F && x < this.endX) {
/*     */         
/* 350 */         if (this.derivX >= 1.0F) {
/* 351 */           x++;
/*     */         } else {
/*     */           
/* 354 */           this.restX += this.derivX;
/* 355 */           if (this.restX >= 1.0F)
/*     */           {
/* 357 */             x++;
/* 358 */             this.restX--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 363 */       } else if (this.derivX < 0.0F && x > this.endX) {
/*     */         
/* 365 */         if (this.derivX <= -1.0F) {
/* 366 */           x--;
/*     */         } else {
/*     */           
/* 369 */           this.restX += this.derivX;
/* 370 */           if (this.restX <= -1.0F) {
/*     */             
/* 372 */             x--;
/* 373 */             this.restX++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 378 */       if (this.derivY > 0.0F && y < this.endY) {
/*     */         
/* 380 */         if (this.derivY >= 1.0F) {
/* 381 */           y++;
/*     */         } else {
/*     */           
/* 384 */           this.restY += this.derivY;
/* 385 */           if (this.restY >= 1.0F)
/*     */           {
/* 387 */             y++;
/* 388 */             this.restY--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 393 */       } else if (this.derivY < 0.0F && y > this.endY) {
/*     */         
/* 395 */         if (this.derivY <= -1.0F) {
/*     */           
/* 397 */           y--;
/*     */         }
/*     */         else {
/*     */           
/* 401 */           this.restY += this.derivY;
/* 402 */           if (this.restY <= -1.0F) {
/*     */             
/* 404 */             y--;
/* 405 */             this.restY++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 412 */     if (!mesh.contains(x, y))
/* 413 */       throw new NoPathException("Path missed at " + x + ", " + y); 
/* 414 */     PathTile toReturn = null;
/*     */     
/*     */     try {
/* 417 */       toReturn = mesh.getPathTile(x, y);
/*     */       
/* 419 */       if (!canPass(this.current, toReturn))
/*     */       {
/* 421 */         throw new NoPathException("Path blocked between " + this.current.toString() + " and " + toReturn.toString());
/*     */       }
/*     */     }
/* 424 */     catch (ArrayIndexOutOfBoundsException ai) {
/*     */       
/* 426 */       logger.log(Level.WARNING, "OUT OF BOUNDS AT RAYCAST: " + x + ", " + y + ": " + ai.getMessage(), ai);
/* 427 */       logger.log(Level.WARNING, "Mesh info: " + mesh.getBorderStartX() + ", " + mesh.getBorderStartY() + ", to " + mesh
/* 428 */           .getBorderEndX() + ", " + mesh.getBorderEndY());
/* 429 */       logger.log(Level.WARNING, "Size of meshx=" + mesh.getSizex() + ", meshy=" + mesh.getSizey());
/* 430 */       throw new NoPathException("Path missed at " + x + ", " + y);
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
/* 448 */     if (this.stepsTaken > this.maxSteps) {
/*     */       
/* 450 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 452 */         logger.finest("Raycaster stops searching after " + this.stepsTaken + " steps, suspecting it missed the target.");
/*     */       }
/* 454 */       throw new NoPathException("Probably missed target using raycaster " + this.stepsTaken + " of " + this.maxSteps + " dx=" + this.derivX + ",dy=" + this.derivY);
/*     */     } 
/*     */     
/* 457 */     this.stepsTaken++;
/* 458 */     return toReturn;
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
/* 472 */     return low * (Math.abs(a.getTileX() - b.getTileX()) + Math.abs(a.getTileY() - b.getTileY()) - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static float getCost(int tile) {
/* 482 */     if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/* 483 */       return Float.MAX_VALUE; 
/* 484 */     if (Tiles.decodeHeight(tile) < 1)
/*     */     {
/* 486 */       return 3.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     return 1.0F;
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
/*     */   Path astar(int startTileX, int startTileY, int endTileX, int endTileY, boolean aSurfaced) throws NoPathException {
/* 527 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 528 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 529 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/*     */       
/* 531 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 532 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight) {
/* 533 */       throw new NoPathException("Target in water.");
/*     */     }
/* 535 */     pathList = new LinkedList<>();
/*     */ 
/*     */     
/* 538 */     if (this.start != null && this.finish != null && this.start.equals(this.finish))
/* 539 */       return null; 
/* 540 */     if (this.finish == null) {
/*     */       
/* 542 */       if (this.creature != null) {
/* 543 */         logger.log(Level.WARNING, this.creature.getName() + " finish=null at " + endTileX + ", " + endTileY);
/*     */       } else {
/* 545 */         logger.log(Level.WARNING, "Finish=null at " + endTileX + ", " + endTileY);
/* 546 */       }  return null;
/*     */     } 
/* 548 */     if (this.start == null) {
/*     */       
/* 550 */       if (this.creature != null) {
/* 551 */         logger.log(Level.WARNING, this.creature.getName() + " start=null at " + startTileX + ", " + startTileY);
/*     */       } else {
/* 553 */         logger.log(Level.WARNING, "start=null at " + startTileX + ", " + startTileY);
/* 554 */       }  return null;
/*     */     } 
/* 556 */     this.start.setDistanceFromStart(this.start, 0.0F);
/* 557 */     pathList.add(this.start);
/*     */     
/* 559 */     int pass = 0;
/*     */     
/* 561 */     int lState = 0;
/* 562 */     while (lState == 0 && pass < 10000) {
/*     */       
/* 564 */       pass++;
/*     */       
/* 566 */       lState = step2();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 576 */     if (lState == 1) {
/*     */       
/* 578 */       if (pass > 4000) {
/*     */         
/* 580 */         String cname = "Unknown";
/* 581 */         if (this.creature != null)
/* 582 */           cname = this.creature.getName(); 
/* 583 */         logger.log(Level.INFO, cname + " pathed from " + this.startX + ", " + this.startY + " to " + this.endX + ", " + this.endY + " and found path after " + pass + " steps.");
/*     */       } 
/*     */ 
/*     */       
/* 587 */       return setPath();
/*     */     } 
/*     */ 
/*     */     
/* 591 */     if (lState == 2) {
/* 592 */       throw new NoPathException("No path possible after " + pass + " tries.");
/*     */     }
/* 594 */     throw new NoPathException("No path found after " + pass + " tries.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDebug(boolean aDebug) {
/* 604 */     this.debug = aDebug;
/* 605 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 607 */       logger.finest("Debug in pathfinding - " + aDebug);
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
/*     */   int step2() {
/* 624 */     boolean found = false;
/* 625 */     float min = Float.MAX_VALUE;
/*     */     
/* 627 */     float score = 0.0F;
/* 628 */     PathTile best = pathList.get(pathList.size() - 1);
/* 629 */     PathTile now = null;
/*     */     
/* 631 */     for (int i = 0; i < pathList.size(); i++) {
/*     */       
/* 633 */       now = pathList.get(i);
/* 634 */       score = now.getDistanceFromStart();
/* 635 */       score += cbDist(now, this.finish, getCost(now.getTile()));
/*     */       
/* 637 */       if (!now.isUsed())
/*     */       {
/* 639 */         if (score < min) {
/*     */           
/* 641 */           min = score;
/* 642 */           best = now;
/*     */         } 
/*     */       }
/*     */     } 
/* 646 */     now = best;
/* 647 */     pathList.remove(now);
/* 648 */     now.setUsed();
/*     */     
/* 650 */     PathTile[] next = mesh.getAdjacent(now);
/*     */     
/* 652 */     for (int j = 0; j < next.length; j++) {
/*     */       
/* 654 */       if (next[j] != null)
/*     */       {
/* 656 */         if (canPass(now, next[j])) {
/*     */           
/* 658 */           if (!pathList.contains(next[j]) && next[j].isNotUsed())
/*     */           {
/* 660 */             pathList.add(next[j]);
/*     */           }
/* 662 */           if (next[j] == this.finish)
/*     */           {
/* 664 */             found = true;
/*     */           }
/* 666 */           score = now.getDistanceFromStart() + next[j].getMoveCost();
/* 667 */           next[j].setDistanceFromStart(now, score);
/*     */         } 
/*     */       }
/* 670 */       if (found)
/* 671 */         return 1; 
/*     */     } 
/* 673 */     if (pathList.isEmpty())
/* 674 */       return 2; 
/* 675 */     return 0;
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
/* 709 */     return now.getLink();
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
/*     */   Path setPath() {
/* 722 */     setDebug(this.debug);
/* 723 */     boolean finished = false;
/*     */     
/* 725 */     PathTile now = this.finish;
/* 726 */     PathTile stop = this.start;
/* 727 */     pList = new LinkedList<>();
/* 728 */     PathTile lastCurrent = now;
/*     */     
/* 730 */     while (!finished) {
/*     */ 
/*     */ 
/*     */       
/* 734 */       pList.add(now);
/*     */       
/* 736 */       PathTile next = findLowestDist(this.start, now);
/* 737 */       if (lastCurrent.equals(next)) {
/*     */         
/* 739 */         finished = true;
/* 740 */         logger.log(Level.WARNING, "Loop in heuristicastar.");
/*     */       } 
/* 742 */       lastCurrent = now;
/* 743 */       now = next;
/* 744 */       if (now.equals(stop)) {
/*     */         
/* 746 */         if (Math.abs(lastCurrent.getTileX() - now.getTileX()) > 1 || 
/* 747 */           Math.abs(lastCurrent.getTileY() - now.getTileY()) > 1)
/* 748 */           pList.add(now); 
/* 749 */         finished = true;
/*     */       } 
/*     */     } 
/* 752 */     LinkedList<PathTile> inverted = new LinkedList<>();
/* 753 */     for (Iterator<PathTile> it = pList.iterator(); it.hasNext();)
/*     */     {
/* 755 */       inverted.addFirst(it.next());
/*     */     }
/*     */     
/* 758 */     Path path = new Path(inverted);
/*     */     
/* 760 */     setDebug(false);
/* 761 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\StaticPathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */