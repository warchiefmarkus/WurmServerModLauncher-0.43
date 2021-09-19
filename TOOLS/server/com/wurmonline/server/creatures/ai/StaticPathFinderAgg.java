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
/*     */ public final class StaticPathFinderAgg
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
/*  45 */   private static StaticPathMeshAgg mesh = null;
/*  46 */   private static final int WORLD_SIZE = 1 << Constants.meshSize;
/*     */   
/*  48 */   private static final Logger logger = Logger.getLogger(PathFinder.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticPathFinderAgg() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticPathFinderAgg(boolean ignoresWalls) {
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
/* 108 */         toReturn = rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 117 */       catch (NoPathException np)
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
/* 128 */         toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
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
/* 141 */       toReturn = startAstar(this.startX, this.startY, this.endX, this.endY);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 146 */     return toReturn;
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
/* 163 */     Path toReturn = new Path();
/*     */     
/*     */     try {
/* 166 */       toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/* 167 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 169 */         logger.finest(this.creature.getName() + " astared a path.");
/*     */       }
/*     */     }
/* 172 */     catch (NoPathException np2) {
/*     */       
/* 174 */       if (this.creature != null) {
/*     */         
/* 176 */         if ((this.creature.isKingdomGuard() || this.creature.isSpiritGuard() || this.creature.isUnique() || this.creature.isDominated()) && this.creature.target == -10L) {
/*     */ 
/*     */           
/* 179 */           int _diffX = Math.max(1, Math.abs(_endX - _startX) / 2);
/* 180 */           int _diffY = Math.max(1, Math.abs(_endY - _startY) / 2);
/*     */           
/* 182 */           int stepsX = Server.rand.nextInt(Math.min(50, _diffX + 1));
/* 183 */           if (this.endX < this.startX)
/* 184 */             stepsX = -stepsX; 
/* 185 */           this.endX = this.startX + stepsX;
/* 186 */           int stepsY = Server.rand.nextInt(Math.min(50, _diffY + 1));
/* 187 */           if (this.endY < this.startY)
/* 188 */             stepsY = -stepsY; 
/* 189 */           this.endY = this.startY + stepsY;
/* 190 */           if (stepsY != 0 || stepsX != 0) {
/*     */             
/* 192 */             setMesh();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 197 */             if (!this.surfaced || this.creature.isKingdomGuard() || this.creature.isUnique() || this.creature.isDominated()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 207 */               toReturn = astar(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             } else {
/*     */               
/* 210 */               toReturn = rayCast(_startX, _startY, _endX, _endY, this.surfaced);
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 216 */           throw np2;
/*     */         } 
/*     */       } else {
/* 219 */         throw np2;
/*     */       } 
/* 221 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean surf, int areaSz) throws NoPathException {
/* 228 */     this.startX = Math.max(0, startTileX);
/* 229 */     this.startY = Math.max(0, startTileY);
/* 230 */     this.endX = Math.min(WORLD_SIZE - 1, endTileX);
/* 231 */     this.endY = Math.min(WORLD_SIZE - 1, endTileY);
/* 232 */     this.surfaced = surf;
/* 233 */     this.areaSize = areaSz;
/* 234 */     setMesh();
/*     */     
/* 236 */     return rayCast(this.startX, this.startY, this.endX, this.endY, this.surfaced);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setMesh() {
/* 246 */     StaticPathMeshAgg.clearPathables();
/* 247 */     mesh = new StaticPathMeshAgg(this.startX, this.startY, this.endX, this.endY, this.surfaced, this.areaSize);
/*     */     
/* 249 */     this.current = mesh.getStart();
/* 250 */     this.start = mesh.getStart();
/* 251 */     this.finish = mesh.getFinish();
/* 252 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 254 */       logger.finest("Start is " + this.start.toString() + ", finish " + this.finish.toString());
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
/*     */   
/*     */   Path rayCast(int startTileX, int startTileY, int endTileX, int endTileY, boolean aSurfaced) throws NoPathException {
/* 274 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 275 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 276 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/* 277 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 278 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight)
/* 279 */       throw new NoPathException("Target in water."); 
/* 280 */     this.maxSteps = Math.max(Math.abs(this.endX - this.startX), Math.abs(this.endY - this.startY)) + 1;
/* 281 */     this.stepsTaken = 0;
/* 282 */     pathList = new LinkedList<>();
/* 283 */     float diffX = (this.endX - this.startX);
/* 284 */     float diffY = (this.endY - this.startY);
/* 285 */     if (diffX == 0.0F) {
/*     */       
/* 287 */       this.derivX = 0.0F;
/* 288 */       this.derivY = diffY;
/*     */     } 
/* 290 */     if (diffY == 0.0F) {
/*     */       
/* 292 */       this.derivY = 0.0F;
/* 293 */       this.derivX = diffX;
/*     */     } 
/* 295 */     if (diffX != 0.0F && diffY != 0.0F) {
/*     */       
/* 297 */       this.derivX = Math.abs(diffX / diffY);
/* 298 */       this.derivY = Math.abs(diffY / diffX);
/*     */     } 
/* 300 */     if (diffY < 0.0F && this.derivY > 0.0F)
/* 301 */       this.derivY = -this.derivY; 
/* 302 */     if (diffX < 0.0F && this.derivX > 0.0F)
/* 303 */       this.derivX = -this.derivX; 
/* 304 */     while (!this.current.equals(this.finish)) {
/*     */       
/* 306 */       this.current = step();
/* 307 */       pathList.add(this.current);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     return new Path(pathList);
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
/* 326 */     int x = this.current.getTileX();
/* 327 */     int y = this.current.getTileY();
/* 328 */     boolean raycend = true;
/* 329 */     if (!this.surfaced)
/* 330 */       raycend = false; 
/* 331 */     if (raycend && Math.abs(this.endX - x) <= 1 && Math.abs(this.endY - y) <= 1) {
/*     */ 
/*     */       
/* 334 */       x = this.endX;
/* 335 */       y = this.endY;
/*     */     }
/* 337 */     else if (Math.abs(this.endX - x) < 1 && Math.abs(this.endY - y) < 1) {
/*     */       
/* 339 */       x = this.endX;
/* 340 */       y = this.endY;
/* 341 */       logger.log(Level.INFO, "This really shouldn't happen i guess, since it should have been detected already.");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 346 */       if (this.derivX > 0.0F && x < this.endX) {
/*     */         
/* 348 */         if (this.derivX >= 1.0F) {
/* 349 */           x++;
/*     */         } else {
/*     */           
/* 352 */           this.restX += this.derivX;
/* 353 */           if (this.restX >= 1.0F)
/*     */           {
/* 355 */             x++;
/* 356 */             this.restX--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 361 */       } else if (this.derivX < 0.0F && x > this.endX) {
/*     */         
/* 363 */         if (this.derivX <= -1.0F) {
/* 364 */           x--;
/*     */         } else {
/*     */           
/* 367 */           this.restX += this.derivX;
/* 368 */           if (this.restX <= -1.0F) {
/*     */             
/* 370 */             x--;
/* 371 */             this.restX++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 376 */       if (this.derivY > 0.0F && y < this.endY) {
/*     */         
/* 378 */         if (this.derivY >= 1.0F) {
/* 379 */           y++;
/*     */         } else {
/*     */           
/* 382 */           this.restY += this.derivY;
/* 383 */           if (this.restY >= 1.0F)
/*     */           {
/* 385 */             y++;
/* 386 */             this.restY--;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 391 */       } else if (this.derivY < 0.0F && y > this.endY) {
/*     */         
/* 393 */         if (this.derivY <= -1.0F) {
/*     */           
/* 395 */           y--;
/*     */         }
/*     */         else {
/*     */           
/* 399 */           this.restY += this.derivY;
/* 400 */           if (this.restY <= -1.0F) {
/*     */             
/* 402 */             y--;
/* 403 */             this.restY++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 408 */     if (!mesh.contains(x, y))
/* 409 */       throw new NoPathException("Path missed at " + x + ", " + y); 
/* 410 */     PathTile toReturn = null;
/*     */     
/*     */     try {
/* 413 */       toReturn = mesh.getPathTile(x, y);
/*     */       
/* 415 */       if (!canPass(this.current, toReturn))
/*     */       {
/* 417 */         throw new NoPathException("Path blocked between " + this.current.toString() + " and " + toReturn.toString());
/*     */       }
/*     */     }
/* 420 */     catch (ArrayIndexOutOfBoundsException ai) {
/*     */       
/* 422 */       logger.log(Level.WARNING, "OUT OF BOUNDS AT RAYCAST: " + x + ", " + y + ": " + ai.getMessage(), ai);
/* 423 */       logger.log(Level.WARNING, "Mesh info: " + mesh.getBorderStartX() + ", " + mesh.getBorderStartY() + ", to " + mesh
/* 424 */           .getBorderEndX() + ", " + mesh.getBorderEndY());
/* 425 */       logger.log(Level.WARNING, "Size of meshx=" + mesh.getSizex() + ", meshy=" + mesh.getSizey());
/* 426 */       throw new NoPathException("Path missed at " + x + ", " + y);
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
/* 444 */     if (this.stepsTaken > this.maxSteps) {
/*     */       
/* 446 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 448 */         logger.finest("Raycaster stops searching after " + this.stepsTaken + " steps, suspecting it missed the target.");
/*     */       }
/* 450 */       throw new NoPathException("Probably missed target using raycaster.");
/*     */     } 
/* 452 */     this.stepsTaken++;
/* 453 */     return toReturn;
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
/* 467 */     return low * (Math.abs(a.getTileX() - b.getTileX()) + Math.abs(a.getTileY() - b.getTileY()) - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static float getCost(int tile) {
/* 477 */     if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/* 478 */       return Float.MAX_VALUE; 
/* 479 */     if (Tiles.decodeHeight(tile) < 1)
/*     */     {
/* 481 */       return 3.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 487 */     return 1.0F;
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
/* 522 */     int endHeight = Tiles.decodeHeight(this.finish.getTile());
/* 523 */     int startHeight = Tiles.decodeHeight(this.start.getTile());
/* 524 */     if (this.creature != null && !this.creature.isSwimming() && !this.creature.isSubmerged() && endHeight < -this.creatureHalfHeight && 
/*     */       
/* 526 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_CAVE_EXIT.id && 
/* 527 */       Tiles.decodeType(this.finish.getTile()) != Tiles.Tile.TILE_HOLE.id && endHeight < startHeight) {
/* 528 */       throw new NoPathException("Target in water.");
/*     */     }
/* 530 */     pathList = new LinkedList<>();
/*     */ 
/*     */     
/* 533 */     if (this.start != null && this.finish != null && this.start.equals(this.finish))
/* 534 */       return null; 
/* 535 */     if (this.finish == null) {
/*     */       
/* 537 */       if (this.creature != null) {
/* 538 */         logger.log(Level.WARNING, this.creature.getName() + " finish=null at " + endTileX + ", " + endTileY);
/*     */       } else {
/* 540 */         logger.log(Level.WARNING, "Finish=null at " + endTileX + ", " + endTileY);
/* 541 */       }  return null;
/*     */     } 
/* 543 */     if (this.start == null) {
/*     */       
/* 545 */       if (this.creature != null) {
/* 546 */         logger.log(Level.WARNING, this.creature.getName() + " start=null at " + startTileX + ", " + startTileY);
/*     */       } else {
/* 548 */         logger.log(Level.WARNING, "start=null at " + startTileX + ", " + startTileY);
/* 549 */       }  return null;
/*     */     } 
/* 551 */     this.start.setDistanceFromStart(this.start, 0.0F);
/* 552 */     pathList.add(this.start);
/*     */     
/* 554 */     int pass = 0;
/*     */     
/* 556 */     int lState = 0;
/* 557 */     while (lState == 0 && pass < 10000) {
/*     */       
/* 559 */       pass++;
/*     */       
/* 561 */       lState = step2();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 571 */     if (lState == 1) {
/*     */       
/* 573 */       if (pass > 4000) {
/*     */         
/* 575 */         String cname = "Unknown";
/* 576 */         if (this.creature != null)
/* 577 */           cname = this.creature.getName(); 
/* 578 */         logger.log(Level.INFO, cname + " pathed from " + this.startX + ", " + this.startY + " to " + this.endX + ", " + this.endY + " and found path after " + pass + " steps.");
/*     */       } 
/*     */ 
/*     */       
/* 582 */       return setPath();
/*     */     } 
/*     */ 
/*     */     
/* 586 */     if (lState == 2) {
/* 587 */       throw new NoPathException("No path possible after " + pass + " tries.");
/*     */     }
/* 589 */     throw new NoPathException("No path found after " + pass + " tries.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDebug(boolean aDebug) {
/* 599 */     this.debug = aDebug;
/* 600 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 602 */       logger.finest("Debug in pathfinding - " + aDebug);
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
/* 619 */     boolean found = false;
/* 620 */     float min = Float.MAX_VALUE;
/*     */     
/* 622 */     float score = 0.0F;
/* 623 */     PathTile best = pathList.get(pathList.size() - 1);
/* 624 */     PathTile now = null;
/*     */     
/* 626 */     for (int i = 0; i < pathList.size(); i++) {
/*     */       
/* 628 */       now = pathList.get(i);
/* 629 */       score = now.getDistanceFromStart();
/* 630 */       score += cbDist(now, this.finish, getCost(now.getTile()));
/*     */       
/* 632 */       if (!now.isUsed())
/*     */       {
/* 634 */         if (score < min) {
/*     */           
/* 636 */           min = score;
/* 637 */           best = now;
/*     */         } 
/*     */       }
/*     */     } 
/* 641 */     now = best;
/* 642 */     pathList.remove(now);
/* 643 */     now.setUsed();
/*     */     
/* 645 */     PathTile[] next = mesh.getAdjacent(now);
/*     */     
/* 647 */     for (int j = 0; j < next.length; j++) {
/*     */       
/* 649 */       if (next[j] != null)
/*     */       {
/* 651 */         if (canPass(now, next[j])) {
/*     */           
/* 653 */           if (!pathList.contains(next[j]) && next[j].isNotUsed())
/*     */           {
/* 655 */             pathList.add(next[j]);
/*     */           }
/* 657 */           if (next[j] == this.finish)
/*     */           {
/* 659 */             found = true;
/*     */           }
/* 661 */           score = now.getDistanceFromStart() + next[j].getMoveCost();
/* 662 */           next[j].setDistanceFromStart(now, score);
/*     */         } 
/*     */       }
/* 665 */       if (found)
/* 666 */         return 1; 
/*     */     } 
/* 668 */     if (pathList.isEmpty())
/* 669 */       return 2; 
/* 670 */     return 0;
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
/* 704 */     return now.getLink();
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
/* 717 */     setDebug(this.debug);
/* 718 */     boolean finished = false;
/*     */     
/* 720 */     PathTile now = this.finish;
/* 721 */     PathTile stop = this.start;
/* 722 */     pList = new LinkedList<>();
/* 723 */     PathTile lastCurrent = now;
/*     */     
/* 725 */     while (!finished) {
/*     */ 
/*     */ 
/*     */       
/* 729 */       pList.add(now);
/*     */       
/* 731 */       PathTile next = findLowestDist(this.start, now);
/* 732 */       if (lastCurrent.equals(next)) {
/*     */         
/* 734 */         finished = true;
/* 735 */         logger.log(Level.WARNING, "Loop in heuristicastar.");
/*     */       } 
/* 737 */       lastCurrent = now;
/* 738 */       now = next;
/* 739 */       if (now.equals(stop)) {
/*     */         
/* 741 */         if (Math.abs(lastCurrent.getTileX() - now.getTileX()) > 1 || 
/* 742 */           Math.abs(lastCurrent.getTileY() - now.getTileY()) > 1)
/* 743 */           pList.add(now); 
/* 744 */         finished = true;
/*     */       } 
/*     */     } 
/* 747 */     LinkedList<PathTile> inverted = new LinkedList<>();
/* 748 */     for (Iterator<PathTile> it = pList.iterator(); it.hasNext();)
/*     */     {
/* 750 */       inverted.addFirst(it.next());
/*     */     }
/*     */     
/* 753 */     Path path = new Path(inverted);
/*     */     
/* 755 */     setDebug(false);
/* 756 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\StaticPathFinderAgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */