/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.ai.PathTile;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Blocking
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final double RADS_TO_DEGS = 57.29577951308232D;
/*     */   public static final float DEGS_TO_RADS = 0.017453292F;
/*     */   private static final float MAXSTEP = 3.0F;
/*  48 */   private static final Logger logger = Logger.getLogger(Blocking.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult getRangedBlockerBetween(Creature performer, Item target) {
/*  60 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), target.getPosX(), target
/*  61 */         .getPosY(), performer.getPositionZ(), target.getPosZ(), performer.isOnSurface(), target.isOnSurface(), true, 5, target
/*  62 */         .getWurmId(), performer.getBridgeId(), target.getBridgeId(), (performer
/*  63 */         .followsGround() || (target.getFloorLevel() == 0 && target.getBridgeId() == -10L)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, Item target, int blockingType) {
/*  68 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), target.getPosX(), target
/*  69 */         .getPosY(), performer.getPositionZ(), target.getPosZ(), performer.isOnSurface(), target.isOnSurface(), false, blockingType, target
/*  70 */         .getWurmId(), performer.getBridgeId(), target.getBridgeId(), (performer
/*  71 */         .followsGround() || (target.getFloorLevel() == 0 && target.getBridgeId() == -10L)));
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
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, Floor floor, int blockingType) {
/*  86 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), (2 + 
/*  87 */         Tiles.decodeTileX(floor.getId()) * 4), (2 + Tiles.decodeTileY(floor.getId()) * 4), performer.getPositionZ(), floor
/*  88 */         .getMinZ() - 0.25F, performer.isOnSurface(), floor.isOnSurface(), false, 
/*  89 */         !performer.isOnSurface() ? 7 : blockingType, floor
/*  90 */         .getId(), performer.getBridgeId(), -10L, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, long target, boolean targetSurfaced, int blockingType, long sourceBridgeId, long targetBridgeId) {
/*  97 */     return getBlockerBetween(performer, target, targetSurfaced, blockingType, sourceBridgeId, targetBridgeId, 0);
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
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, long target, boolean targetSurfaced, int blockingType, long sourceBridgeId, long targetBridgeId, int ceilingHeight) {
/* 127 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), (2 + Tiles.decodeTileX(target) * 4), (2 + 
/* 128 */         Tiles.decodeTileY(target) * 4), performer.getPositionZ(), 
/* 129 */         Zones.getHeightForNode(Tiles.decodeTileX(target), Tiles.decodeTileY(target), targetSurfaced ? 0 : -1) + (
/* 130 */         Tiles.decodeFloorLevel(target) * 3) + ceilingHeight, performer
/* 131 */         .isOnSurface(), targetSurfaced, false, (!performer.isOnSurface() && blockingType != 8) ? 7 : blockingType, target, sourceBridgeId, targetBridgeId, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, Wall target, int blockingType) {
/* 138 */     float tpx = target.getPositionX();
/* 139 */     float tpy = target.getPositionY();
/*     */ 
/*     */     
/* 142 */     if (target.getDir() == Tiles.TileBorderDirection.DIR_HORIZ) {
/*     */ 
/*     */       
/* 145 */       if (tpy > (performer.getTileY() * 4)) {
/* 146 */         tpy -= 4.0F;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 151 */     else if (tpx > (performer.getTileX() * 4)) {
/* 152 */       tpx -= 4.0F;
/*     */     } 
/* 154 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), tpx, tpy, performer
/* 155 */         .getPositionZ(), target.getMinZ(), performer.isOnSurface(), target.isOnSurface(), false, blockingType, target
/* 156 */         .getId(), performer.getBridgeId(), -10L, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, Fence target, int blockingType) {
/* 161 */     float tpx = target.getPositionX();
/* 162 */     float tpy = target.getPositionY();
/*     */ 
/*     */     
/* 165 */     if (target.getDir() == Tiles.TileBorderDirection.DIR_HORIZ) {
/*     */ 
/*     */       
/* 168 */       if (tpy > (performer.getTileY() * 4)) {
/* 169 */         tpy -= 4.0F;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 174 */     else if (tpx > (performer.getTileX() * 4)) {
/* 175 */       tpx -= 4.0F;
/*     */     } 
/* 177 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), tpx, tpy, performer
/* 178 */         .getPositionZ(), target.getMinZ(), performer.isOnSurface(), target.isOnSurface(), false, blockingType, target
/* 179 */         .getId(), performer.getBridgeId(), -10L, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BlockingResult getRangedBlockerBetween(Creature performer, Creature target) {
/* 184 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), target.getPosX(), target.getPosY(), performer
/* 185 */         .getPositionZ() + performer.getHalfHeightDecimeters() / 10.0F, target
/* 186 */         .getPositionZ() + target.getHalfHeightDecimeters() / 10.0F, performer.isOnSurface(), target.isOnSurface(), true, 4, target
/* 187 */         .getWurmId(), performer.getBridgeId(), target.getBridgeId(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(Creature performer, Creature target, int blockingType) {
/* 192 */     return getBlockerBetween(performer, performer.getPosX(), performer.getPosY(), target.getPosX(), target.getPosY(), performer
/* 193 */         .getPositionZ(), target.getPositionZ(), performer.isOnSurface(), target.isOnSurface(), false, blockingType, target
/* 194 */         .getWurmId(), performer.getBridgeId(), target.getBridgeId(), (performer.followsGround() || target
/* 195 */         .followsGround()));
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
/*     */   public static final BlockingResult getBlockerBetween(@Nullable Creature creature, float startx, float starty, float endx, float endy, float startZ, float endZ, boolean surfaced, boolean targetSurfaced, boolean rangedAttack, int typeChecked, long target, long sourceBridgeId, long targetBridgeId, boolean followGround) {
/* 212 */     return getBlockerBetween(creature, startx, starty, endx, endy, startZ, endZ, surfaced, targetSurfaced, rangedAttack, typeChecked, true, target, sourceBridgeId, targetBridgeId, followGround);
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
/*     */   public static final boolean isSameFloorLevel(float startZ, float endZ) {
/* 229 */     return (Math.abs(startZ - endZ) < 3.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult getBlockerBetween(@Nullable Creature creature, float startx, float starty, float endx, float endy, float startZ, float endZ, boolean surfaced, boolean targetSurfaced, boolean rangedAttack, int typeChecked, boolean test, long target, long sourceBridgeId, long targetBridgeId, boolean followGround) {
/* 237 */     int starttilex = Zones.safeTileX((int)startx >> 2);
/* 238 */     int starttiley = Zones.safeTileY((int)starty >> 2);
/*     */     
/* 240 */     int endtilex = Zones.safeTileX((int)endx >> 2);
/* 241 */     int endtiley = Zones.safeTileY((int)endy >> 2);
/* 242 */     int max = rangedAttack ? 100 : 50;
/*     */     
/* 244 */     if (starttilex == endtilex && starttiley == endtiley && isSameFloorLevel(startZ, endZ))
/* 245 */       return null; 
/* 246 */     if (typeChecked == 0) {
/* 247 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (!rangedAttack && creature != null) {
/*     */       
/* 253 */       if (!creature.isPlayer()) {
/* 254 */         max = creature.getMaxHuntDistance() + 5;
/*     */       }
/* 256 */       Creature targetCret = creature.getTarget();
/* 257 */       if (targetCret != null && targetCret.getWurmId() == target)
/*     */       {
/* 259 */         creature.sendToLoggers("Now checking " + starttilex + "," + starttiley + " to " + endtilex + "," + endtiley + " startZ=" + startZ + " endZ=" + endZ + " surf=" + surfaced + "," + targetSurfaced + " follow ground=" + followGround);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 264 */     int nextTileX = starttilex;
/* 265 */     int nextTileY = starttiley;
/* 266 */     int lastTileX = starttilex;
/* 267 */     int lastTileY = starttiley;
/*     */     
/* 269 */     boolean isTransition = false;
/* 270 */     if (creature != null)
/*     */     {
/* 272 */       if (!creature.isOnSurface()) {
/*     */         
/* 274 */         isTransition = false;
/* 275 */         if (Tiles.decodeType(Server.caveMesh.getTile(starttilex, starttiley)) == Tiles.Tile.TILE_CAVE_EXIT.id)
/* 276 */           isTransition = true; 
/* 277 */         if (creature.isPlayer() && typeChecked != 6) {
/*     */           
/* 279 */           Vector3f actualStart = ((Player)creature).getActualPosVehicle();
/* 280 */           int actualStartX = Zones.safeTileX((int)actualStart.x >> 2);
/* 281 */           int actualStartY = Zones.safeTileX((int)actualStart.y >> 2);
/* 282 */           int tile = Server.caveMesh.getTile(actualStartX, actualStartY);
/* 283 */           if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */             
/* 285 */             BlockingResult toReturn = new BlockingResult();
/* 286 */             PathTile blocker = new PathTile(actualStartX, actualStartY, tile, false, -1);
/* 287 */             toReturn.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 288 */             return toReturn;
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
/*     */     
/* 301 */     Vector3f startPos = new Vector3f(startx, starty, startZ + 0.5F);
/*     */     
/* 303 */     Vector3f endPos = new Vector3f(endx, endy, endZ + 0.5F);
/* 304 */     Vector3f lastPos = new Vector3f(startPos);
/* 305 */     Vector3f nextPos = new Vector3f(startPos);
/* 306 */     Vector3f dir = (new Vector3f(endPos.subtract(startPos))).normalize();
/* 307 */     BlockingResult result = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 320 */     boolean found = false;
/* 321 */     int debugChecks = 0;
/* 322 */     while (!found) {
/*     */ 
/*     */       
/* 325 */       Vector3f remain = endPos.subtract(lastPos);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 332 */       if (remain.length() < 3.0F) {
/*     */         
/* 334 */         if (debugChecks++ > 60) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 342 */           found = true;
/*     */         }
/* 344 */         else if (remain.length() == 0.0F) {
/*     */ 
/*     */ 
/*     */           
/* 348 */           found = true;
/*     */         } 
/*     */         
/* 351 */         nextPos.addLocal(remain);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 357 */         nextPos.addLocal(dir.mult(3.0F));
/* 358 */         if (debugChecks++ > 60) {
/*     */           
/* 360 */           if (creature != null) {
/* 361 */             logger.log(Level.INFO, creature.getName() + " checking " + 3.0F + " meters failed. Checks=" + debugChecks);
/*     */           }
/* 363 */           found = true;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 372 */       lastTileY = nextTileY;
/* 373 */       lastTileX = nextTileX;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 381 */       nextTileX = (int)nextPos.x >> 2;
/* 382 */       nextTileY = (int)nextPos.y >> 2;
/* 383 */       int diffX = nextTileX - lastTileX;
/* 384 */       int diffY = nextTileY - lastTileY;
/* 385 */       if (diffX == 0 && diffY == 0) {
/*     */         
/* 387 */         nextPos.z = endPos.z;
/* 388 */         lastPos.z = startPos.z;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 405 */       if (diffX != 0 || diffY != 0 || !isSameFloorLevel(lastPos.z, nextPos.z)) {
/*     */ 
/*     */         
/* 408 */         if (!surfaced && (!isTransition || !targetSurfaced))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 426 */           if (typeChecked != 1 && typeChecked != 2 && typeChecked != 3) {
/*     */ 
/*     */             
/* 429 */             int t = Server.caveMesh.getTile(endtilex, endtiley);
/* 430 */             if (Tiles.isSolidCave(Tiles.decodeType(t)) && typeChecked == 6) {
/*     */               
/* 432 */               result = new BlockingResult();
/* 433 */               PathTile blocker = new PathTile(endtilex, endtiley, t, false, -1);
/* 434 */               result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 435 */               return result;
/*     */             } 
/*     */             
/* 438 */             result = isDiagonalRockBetween(creature, lastTileX, lastTileY, nextTileX, nextTileY);
/* 439 */             if (result == null) {
/* 440 */               result = isStraightRockBetween(creature, lastTileX, lastTileY, nextTileX, nextTileY);
/*     */             }
/* 442 */             if (result != null) {
/*     */ 
/*     */               
/* 445 */               if ((typeChecked != 7 && typeChecked != 8) || result
/* 446 */                 .getFirstBlocker().getTileX() != endtilex || result.getFirstBlocker().getTileY() != endtiley) {
/* 447 */                 return result;
/*     */               }
/* 449 */               result = null;
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 456 */         VolaTile checkedTile = null;
/*     */ 
/*     */         
/* 459 */         if (diffX >= 0 && diffY >= 0) {
/* 460 */           for (int x = Math.min(0, diffX); x <= diffX; x++) {
/*     */             
/* 462 */             for (int y = Math.min(0, diffY); y <= diffY; y++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 476 */               checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, surfaced);
/*     */               
/* 478 */               if (checkedTile != null)
/*     */               {
/* 480 */                 result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 485 */               if (surfaced != targetSurfaced) {
/*     */                 
/* 487 */                 checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, targetSurfaced);
/*     */                 
/* 489 */                 if (checkedTile != null)
/*     */                 {
/* 491 */                   result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 499 */         if (diffX < 0 && diffY >= 0) {
/* 500 */           for (int x = 0; x >= diffX; x--) {
/*     */             
/* 502 */             for (int y = Math.min(0, diffY); y <= diffY; y++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 514 */               checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, surfaced);
/*     */               
/* 516 */               if (checkedTile != null)
/*     */               {
/* 518 */                 result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 525 */               if (surfaced != targetSurfaced) {
/*     */                 
/* 527 */                 checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, targetSurfaced);
/*     */                 
/* 529 */                 if (checkedTile != null)
/*     */                 {
/* 531 */                   result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/* 538 */         if (diffX >= 0 && diffY < 0) {
/* 539 */           for (int x = Math.min(0, diffX); x <= diffX; x++) {
/*     */             
/* 541 */             for (int y = 0; y >= diffY; y--) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 550 */               checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, surfaced);
/*     */               
/* 552 */               if (checkedTile != null)
/*     */               {
/* 554 */                 result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 559 */               if (surfaced != targetSurfaced) {
/*     */                 
/* 561 */                 checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, targetSurfaced);
/*     */                 
/* 563 */                 if (checkedTile != null)
/*     */                 {
/* 565 */                   result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/* 572 */         if (diffX < 0 && diffY < 0) {
/* 573 */           for (int x = 0; x >= diffX; x--) {
/*     */             
/* 575 */             for (int y = 0; y >= diffY; y--) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 584 */               checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, surfaced);
/*     */               
/* 586 */               if (checkedTile != null)
/*     */               {
/* 588 */                 result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 593 */               if (surfaced != targetSurfaced) {
/*     */                 
/* 595 */                 checkedTile = Zones.getTileOrNull(lastTileX + x, lastTileY + y, targetSurfaced);
/*     */                 
/* 597 */                 if (checkedTile != null)
/*     */                 {
/* 599 */                   result = returnIterativeCheck(checkedTile, result, creature, dir, lastPos, nextPos, rangedAttack, starttilex, nextTileX, starttiley, nextTileY, typeChecked, target, sourceBridgeId, targetBridgeId, followGround);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 608 */       lastPos.set(nextPos);
/* 609 */       if (found)
/* 610 */         return result; 
/* 611 */       if (Math.abs(nextTileX - starttilex) > max || Math.abs(nextTileY - starttiley) > max)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 619 */         return result;
/*     */       }
/*     */     } 
/* 622 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final BlockingResult returnIterativeCheck(VolaTile checkedTile, BlockingResult result, Creature creature, Vector3f dir, Vector3f lastPos, Vector3f nextPos, boolean rangedAttack, int startTileX, int nextTileX, int startTileY, int nextTileY, int typeChecked, long targetId, long sourceBridgeId, long targetBridgeId, boolean followGround) {
/* 631 */     Blocker[] blockers = null;
/*     */     
/* 633 */     Vector3f toCheck = lastPos.clone();
/* 634 */     if (typeChecked == 4 || typeChecked == 2 || typeChecked == 5 || typeChecked == 6 || typeChecked == 7) {
/*     */ 
/*     */ 
/*     */       
/* 638 */       Wall[] arrayOfWall = checkedTile.getWalls();
/*     */       
/* 640 */       result = checkForResult(creature, result, (Blocker[])arrayOfWall, dir, toCheck, nextPos, rangedAttack, startTileX, nextTileX, startTileY, nextTileY, typeChecked, targetId, sourceBridgeId, targetBridgeId, followGround);
/*     */       
/* 642 */       if (result != null && result.getTotalCover() >= 100.0F)
/* 643 */         return result; 
/*     */     } 
/* 645 */     if (typeChecked == 4 || typeChecked == 1 || typeChecked == 5 || typeChecked == 6 || typeChecked == 7) {
/*     */ 
/*     */ 
/*     */       
/* 649 */       Fence[] arrayOfFence = checkedTile.getFences();
/* 650 */       toCheck = lastPos.clone();
/* 651 */       result = checkForResult(creature, result, (Blocker[])arrayOfFence, dir, toCheck, nextPos, rangedAttack, startTileX, nextTileX, startTileY, nextTileY, typeChecked, targetId, sourceBridgeId, targetBridgeId, followGround);
/*     */       
/* 653 */       if (result != null && result.getTotalCover() >= 100.0F)
/* 654 */         return result; 
/*     */     } 
/* 656 */     if (typeChecked == 4 || typeChecked == 3 || typeChecked == 5 || typeChecked == 6 || typeChecked == 7) {
/*     */ 
/*     */ 
/*     */       
/* 660 */       Floor[] arrayOfFloor = checkedTile.getFloors();
/* 661 */       toCheck = lastPos.clone();
/*     */ 
/*     */       
/* 664 */       result = checkForResult(creature, result, (Blocker[])arrayOfFloor, dir, toCheck, nextPos, rangedAttack, startTileX, nextTileX, startTileY, nextTileY, typeChecked, targetId, sourceBridgeId, targetBridgeId, followGround);
/*     */ 
/*     */ 
/*     */       
/* 668 */       BridgePart[] arrayOfBridgePart = checkedTile.getBridgeParts();
/* 669 */       toCheck = lastPos.clone();
/* 670 */       result = checkForResult(creature, result, (Blocker[])arrayOfBridgePart, dir, toCheck, nextPos, rangedAttack, startTileX, nextTileX, startTileY, nextTileY, typeChecked, targetId, sourceBridgeId, targetBridgeId, followGround);
/*     */ 
/*     */       
/* 673 */       if (result != null && result.getTotalCover() >= 100.0F)
/* 674 */         return result; 
/*     */     } 
/* 676 */     return result;
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
/*     */   private static final BlockingResult checkForResult(Creature creature, BlockingResult result, Blocker[] blockers, Vector3f dir, Vector3f startPos, Vector3f endPos, boolean rangedAttack, int starttilex, int currTileX, int starttiley, int currTileY, int blockType, long target, long sourceBridgeId, long targetBridgeId, boolean followGround) {
/* 688 */     for (int w = 0; w < blockers.length; w++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 707 */       if (blockers[w].isWithinZ(Math.max(startPos.z, endPos.z), Math.min(startPos.z, endPos.z), followGround)) {
/*     */         
/* 709 */         boolean skip = false;
/* 710 */         if (blockers[w] instanceof BridgePart) {
/*     */           
/* 712 */           BridgePart bp = (BridgePart)blockers[w];
/* 713 */           if (bp.getStructureId() == sourceBridgeId && (sourceBridgeId == targetBridgeId || blockType == 6))
/*     */           {
/*     */             
/* 716 */             skip = true;
/*     */           }
/*     */         } 
/* 719 */         if (!skip) {
/*     */           
/* 721 */           Vector3f intersection = blockers[w].isBlocking(creature, startPos, endPos, dir, blockType, target, followGround);
/*     */           
/* 723 */           if (intersection != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 735 */             if (result == null)
/* 736 */               result = new BlockingResult(); 
/* 737 */             if (!rangedAttack && blockType != 5) {
/*     */               
/* 739 */               result.addBlocker(blockers[w], intersection, 100.0F);
/* 740 */               return result;
/*     */             } 
/*     */             
/* 743 */             float addedCover = blockers[w].getBlockPercent(creature);
/* 744 */             if (Math.abs(starttilex - currTileX) > 1 || Math.abs(starttiley - currTileY) > 1 || addedCover >= 100.0F) {
/*     */ 
/*     */               
/* 747 */               if (addedCover >= 100.0F) {
/*     */                 
/* 749 */                 result.addBlocker(blockers[w], intersection, 100.0F);
/* 750 */                 return result;
/*     */               } 
/* 752 */               if (result.addBlocker(blockers[w], intersection, addedCover) >= 100.0F) {
/* 753 */                 return result;
/*     */ 
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 762 */             else if (result.addBlocker(blockers[w], intersection, addedCover) >= 100.0F) {
/* 763 */               return result;
/*     */             } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 788 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult isDiagonalRockBetween(Creature creature, int startx, int starty, int endx, int endy) {
/* 794 */     if (startx != endx && endy != starty) {
/*     */       
/* 796 */       int northTile = Server.caveMesh.getTile(Zones.safeTileX(startx), 
/* 797 */           Zones.safeTileY(starty - 1));
/* 798 */       int southTile = Server.caveMesh.getTile(Zones.safeTileX(startx), 
/* 799 */           Zones.safeTileY(starty + 1));
/* 800 */       int westTile = Server.caveMesh.getTile(Zones.safeTileX(startx - 1), 
/* 801 */           Zones.safeTileY(starty));
/* 802 */       int eastTile = Server.caveMesh.getTile(Zones.safeTileX(startx + 1), 
/* 803 */           Zones.safeTileY(starty));
/*     */       
/* 805 */       if (endx < startx) {
/*     */ 
/*     */         
/* 808 */         if (endy < starty) {
/*     */           
/* 810 */           if (Tiles.isSolidCave(Tiles.decodeType(westTile)) && Tiles.isSolidCave(Tiles.decodeType(northTile))) {
/*     */             
/* 812 */             BlockingResult result = new BlockingResult();
/* 813 */             PathTile blocker = new PathTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty), westTile, false, -1);
/*     */             
/* 815 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 816 */             PathTile blocker2 = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty - 1), northTile, false, -1);
/*     */             
/* 818 */             result.addBlocker((Blocker)blocker2, blocker2.getCenterPoint(), 100.0F);
/* 819 */             return result;
/*     */           } 
/* 821 */           int nw = Server.caveMesh.getTile(Zones.safeTileX(startx - 1), 
/* 822 */               Zones.safeTileY(starty - 1));
/* 823 */           if (Tiles.isSolidCave(Tiles.decodeType(nw))) {
/*     */             
/* 825 */             BlockingResult result = new BlockingResult();
/*     */             
/* 827 */             PathTile blocker = new PathTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty - 1), nw, false, -1);
/* 828 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 829 */             return result;
/*     */           } 
/*     */         } 
/*     */         
/* 833 */         if (endy > starty)
/*     */         {
/* 835 */           if (Tiles.isSolidCave(Tiles.decodeType(westTile)) && Tiles.isSolidCave(Tiles.decodeType(southTile))) {
/*     */             
/* 837 */             BlockingResult result = new BlockingResult();
/* 838 */             PathTile blocker = new PathTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty), westTile, false, -1);
/*     */             
/* 840 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 841 */             PathTile blocker2 = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty + 1), southTile, false, -1);
/*     */             
/* 843 */             result.addBlocker((Blocker)blocker2, blocker2.getCenterPoint(), 100.0F);
/* 844 */             return result;
/*     */           } 
/* 846 */           int sw = Server.caveMesh.getTile(Zones.safeTileX(startx - 1), 
/* 847 */               Zones.safeTileY(starty + 1));
/* 848 */           if (Tiles.isSolidCave(Tiles.decodeType(sw)))
/*     */           {
/* 850 */             BlockingResult result = new BlockingResult();
/*     */             
/* 852 */             PathTile blocker = new PathTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty + 1), sw, false, -1);
/* 853 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 854 */             return result;
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 862 */         if (endy < starty) {
/*     */           
/* 864 */           if (Tiles.isSolidCave(Tiles.decodeType(eastTile)) && Tiles.isSolidCave(Tiles.decodeType(northTile))) {
/*     */             
/* 866 */             BlockingResult result = new BlockingResult();
/* 867 */             PathTile blocker = new PathTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty), eastTile, false, -1);
/*     */             
/* 869 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 870 */             PathTile blocker2 = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty - 1), northTile, false, -1);
/*     */             
/* 872 */             result.addBlocker((Blocker)blocker2, blocker2.getCenterPoint(), 100.0F);
/* 873 */             return result;
/*     */           } 
/* 875 */           int ne = Server.caveMesh.getTile(Zones.safeTileX(startx + 1), 
/* 876 */               Zones.safeTileY(starty - 1));
/* 877 */           if (Tiles.isSolidCave(Tiles.decodeType(ne))) {
/*     */             
/* 879 */             BlockingResult result = new BlockingResult();
/*     */             
/* 881 */             PathTile blocker = new PathTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty - 1), ne, false, -1);
/* 882 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 883 */             return result;
/*     */           } 
/*     */         } 
/*     */         
/* 887 */         if (endy > starty) {
/*     */           
/* 889 */           if (Tiles.isSolidCave(Tiles.decodeType(eastTile)) && Tiles.isSolidCave(Tiles.decodeType(southTile))) {
/*     */             
/* 891 */             BlockingResult result = new BlockingResult();
/* 892 */             PathTile blocker = new PathTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty), eastTile, false, -1);
/*     */             
/* 894 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 895 */             PathTile blocker2 = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty + 1), southTile, false, -1);
/*     */             
/* 897 */             result.addBlocker((Blocker)blocker2, blocker2.getCenterPoint(), 100.0F);
/* 898 */             return result;
/*     */           } 
/* 900 */           int se = Server.caveMesh.getTile(Zones.safeTileX(startx + 1), 
/* 901 */               Zones.safeTileY(starty + 1));
/* 902 */           if (Tiles.isSolidCave(Tiles.decodeType(se))) {
/*     */             
/* 904 */             BlockingResult result = new BlockingResult();
/*     */             
/* 906 */             PathTile blocker = new PathTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty + 1), se, false, -1);
/* 907 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 908 */             return result;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 913 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BlockingResult isStraightRockBetween(Creature creature, int startx, int starty, int endx, int endy) {
/* 920 */     if (startx == endx || endy == starty)
/*     */     {
/*     */       
/* 923 */       if (endx < startx) {
/*     */         
/* 925 */         if (endy == starty) {
/*     */           
/* 927 */           int tile = Server.caveMesh.getTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty));
/* 928 */           if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/*     */           {
/* 930 */             BlockingResult result = new BlockingResult();
/* 931 */             PathTile blocker = new PathTile(Zones.safeTileX(startx - 1), Zones.safeTileY(starty), tile, false, -1);
/*     */             
/* 933 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 934 */             return result;
/*     */           }
/*     */         
/*     */         } 
/* 938 */       } else if (endx > startx) {
/*     */ 
/*     */         
/* 941 */         if (endy == starty) {
/*     */           
/* 943 */           int tile = Server.caveMesh.getTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty));
/* 944 */           if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/*     */           {
/* 946 */             BlockingResult result = new BlockingResult();
/* 947 */             PathTile blocker = new PathTile(Zones.safeTileX(startx + 1), Zones.safeTileY(starty), tile, false, -1);
/*     */             
/* 949 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 950 */             return result;
/*     */           }
/*     */         
/*     */         } 
/* 954 */       } else if (endy > starty) {
/*     */ 
/*     */         
/* 957 */         if (endx == startx) {
/*     */           
/* 959 */           int tile = Server.caveMesh.getTile(Zones.safeTileX(startx), Zones.safeTileY(starty + 1));
/* 960 */           if (Tiles.isSolidCave(Tiles.decodeType(tile)))
/*     */           {
/* 962 */             BlockingResult result = new BlockingResult();
/* 963 */             PathTile blocker = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty + 1), tile, false, -1);
/*     */             
/* 965 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 966 */             return result;
/*     */           }
/*     */         
/*     */         } 
/* 970 */       } else if (endy < starty) {
/*     */ 
/*     */         
/* 973 */         if (endx == startx) {
/*     */           
/* 975 */           int tile = Server.caveMesh.getTile(Zones.safeTileX(startx), Zones.safeTileY(starty - 1));
/* 976 */           if (Tiles.isSolidCave(Tiles.decodeType(tile))) {
/*     */             
/* 978 */             BlockingResult result = new BlockingResult();
/* 979 */             PathTile blocker = new PathTile(Zones.safeTileX(startx), Zones.safeTileY(starty - 1), tile, false, -1);
/*     */             
/* 981 */             result.addBlocker((Blocker)blocker, blocker.getCenterPoint(), 100.0F);
/* 982 */             return result;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 987 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\Blocking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */