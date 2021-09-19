/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.MineDoorPermission;
/*     */ import com.wurmonline.server.structures.Blocking;
/*     */ import com.wurmonline.server.structures.BlockingResult;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.zones.VirtualZone;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
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
/*     */ public abstract class CreatureAI
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(CreatureAI.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean DETAILED_TIME_LOG = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void simpleMovementTick(Creature c) {
/*  53 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*     */     try {
/*  57 */       int tileX = -1;
/*  58 */       int tileY = -1;
/*     */ 
/*     */       
/*  61 */       if (Server.rand.nextInt(100) < 5) {
/*     */         
/*  63 */         tileX = c.getTileX() + Server.rand.nextInt(4) - 2;
/*  64 */         tileY = c.getTileY() + Server.rand.nextInt(4) - 2;
/*     */       } 
/*     */       
/*  67 */       moveTowardsTile(c, tileX, tileY, true);
/*     */     } finally {}
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
/*     */   protected void pathedMovementTick(Creature c) {
/*  89 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*     */     try {
/*  93 */       Path p = c.getStatus().getPath();
/*  94 */       if (p != null && !p.isEmpty()) {
/*     */         
/*  96 */         PathTile nextTile = p.getFirst();
/*  97 */         if (nextTile.hasSpecificPos()) {
/*     */           
/*  99 */           c.turnTowardsPoint(nextTile.getPosX(), nextTile.getPosY());
/* 100 */           creatureMovementTick(c, true);
/*     */           
/* 102 */           float diffX = c.getStatus().getPositionX() - nextTile.getPosX();
/* 103 */           float diffY = c.getStatus().getPositionY() - nextTile.getPosY();
/* 104 */           double totalDist = Math.sqrt((diffX * diffX + diffY * diffY));
/* 105 */           float lMod = c.getMoveModifier((c.isOnSurface() ? Server.surfaceMesh : Server.caveMesh)
/* 106 */               .getTile((int)c.getStatus().getPositionX() >> 2, (int)c.getStatus().getPositionY() >> 2));
/* 107 */           float aiDataMoveModifier = (c.getCreatureAIData() != null) ? c.getCreatureAIData().getMovementSpeedModifier() : 1.0F;
/*     */           
/* 109 */           if (totalDist <= (c.getSpeed() * lMod * aiDataMoveModifier))
/*     */           {
/* 111 */             p.removeFirst();
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 116 */           if (nextTile.getTileX() == c.getTileX() && nextTile.getTileY() == c.getTileY()) {
/*     */ 
/*     */             
/* 119 */             p.removeFirst();
/*     */             
/* 121 */             if (p.isEmpty()) {
/*     */               return;
/*     */             }
/* 124 */             nextTile = p.getFirst();
/*     */           } 
/*     */           
/* 127 */           moveTowardsTile(c, nextTile.getTileX(), nextTile.getTileY(), true);
/*     */           
/* 129 */           if ((nextTile.getTileX() == c.getTileX() && nextTile.getTileY() == c.getTileY()) || (c
/* 130 */             .getTarget() != null && c.getPos2f().distance(c.getTarget().getPos2f()) < 4.0F)) {
/* 131 */             p.removeFirst();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {}
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
/*     */   protected void moveTowardsTile(Creature c, int tileX, int tileY, boolean moveToTarget) {
/* 160 */     if (c.getTarget() != null && moveToTarget) {
/* 161 */       c.turnTowardsCreature(c.getTarget());
/* 162 */     } else if (tileX > 0 && tileY > 0) {
/* 163 */       c.turnTowardsTile((short)tileX, (short)tileY);
/*     */     } 
/* 165 */     creatureMovementTick(c, true);
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
/*     */   protected void creatureMovementTick(Creature c, boolean rotateFromBlocker) {
/* 179 */     float lMoveModifier, aiDataMoveModifier, lPosX = c.getStatus().getPositionX();
/* 180 */     float lPosY = c.getStatus().getPositionY();
/* 181 */     float lPosZ = c.getStatus().getPositionZ();
/* 182 */     float lRotation = c.getStatus().getRotation();
/*     */     
/* 184 */     float oldPosX = lPosX;
/* 185 */     float oldPosY = lPosY;
/* 186 */     float oldPosZ = lPosZ;
/* 187 */     int lOldTileX = (int)lPosX >> 2;
/* 188 */     int lOldTileY = (int)lPosY >> 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     if (!c.isOnSurface()) {
/* 194 */       lMoveModifier = c.getMoveModifier(Server.caveMesh.getTile(lOldTileX, lOldTileY));
/*     */     } else {
/* 196 */       lMoveModifier = c.getMoveModifier(Server.surfaceMesh.getTile(lOldTileX, lOldTileY));
/*     */     } 
/* 198 */     if (c.getCreatureAIData() != null) {
/* 199 */       aiDataMoveModifier = c.getCreatureAIData().getMovementSpeedModifier();
/*     */     } else {
/* 201 */       aiDataMoveModifier = 1.0F;
/*     */     } 
/*     */ 
/*     */     
/* 205 */     float lXPosMod = (float)Math.sin((lRotation * 0.017453292F)) * c.getSpeed() * lMoveModifier * aiDataMoveModifier;
/* 206 */     float lYPosMod = -((float)Math.cos((lRotation * 0.017453292F))) * c.getSpeed() * lMoveModifier * aiDataMoveModifier;
/*     */     
/* 208 */     int lNewTileX = (int)(lPosX + lXPosMod) >> 2;
/* 209 */     int lNewTileY = (int)(lPosY + lYPosMod) >> 2;
/*     */     
/* 211 */     int lDiffTileX = lNewTileX - lOldTileX;
/* 212 */     int lDiffTileY = lNewTileY - lOldTileY;
/*     */     
/* 214 */     long newBridgeId = c.getBridgeId();
/*     */ 
/*     */     
/* 217 */     if (lDiffTileX != 0 || lDiffTileY != 0) {
/*     */       
/* 219 */       if (!c.isOnSurface()) {
/*     */ 
/*     */ 
/*     */         
/* 223 */         if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(lOldTileX, lOldTileY)))) {
/*     */           
/* 225 */           logger.log(Level.INFO, "Destroying creature " + c.getName() + " due to being inside rock.");
/* 226 */           c.die(false, "Suffocating in rock (3)");
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */ 
/*     */         
/* 233 */         if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(lNewTileX, lNewTileY)))) {
/*     */ 
/*     */           
/* 236 */           if (c.getCurrentTile().isTransition()) {
/*     */             
/* 238 */             if (!Tiles.isMineDoor(
/* 239 */                 Tiles.decodeType(Server.caveMesh.getTile(c.getTileX(), c.getTileY()))) || 
/* 240 */               MineDoorPermission.getPermission(c.getTileX(), c.getTileY()).mayPass(c)) {
/* 241 */               c.setLayer(0, true);
/* 242 */             } else if (rotateFromBlocker) {
/* 243 */               c.rotateRandom(lRotation, 45);
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 249 */           if (rotateFromBlocker) {
/* 250 */             c.rotateRandom(lRotation, 45);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/* 258 */       } else if (Tiles.Tile.TILE_LAVA.id == Tiles.decodeType(Server.surfaceMesh.getTile(lNewTileX, lNewTileY))) {
/*     */         
/* 260 */         if (rotateFromBlocker) {
/* 261 */           c.rotateRandom(lRotation, 45);
/*     */         }
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 268 */       VolaTile t = Zones.getOrCreateTile(lNewTileX, lNewTileY, c.isOnSurface());
/* 269 */       if ((!c.isHuman() && t.isGuarded() && !c.getCurrentTile().isGuarded()) || (c.isAnimal() && t.hasFire())) {
/*     */         
/* 271 */         if (rotateFromBlocker) {
/* 272 */           c.rotateRandom(lRotation, 100);
/*     */         }
/*     */         return;
/*     */       } 
/* 276 */       newBridgeId = getNewBridgeId(lOldTileX, lOldTileY, c.isOnSurface(), c.getBridgeId(), c.getFloorLevel(), lNewTileX, lNewTileY);
/*     */ 
/*     */       
/* 279 */       BlockingResult result = Blocking.getBlockerBetween(c, lPosX, lPosY, lPosX + lXPosMod, lPosY + lYPosMod, c
/* 280 */           .getPositionZ(), c.getPositionZ(), c.isOnSurface(), c.isOnSurface(), false, 6, -1L, c
/* 281 */           .getBridgeId(), newBridgeId, c.followsGround());
/*     */ 
/*     */       
/* 284 */       if (result != null && !c.isWagoner())
/*     */       {
/* 286 */         if ((!c.isKingdomGuard() && !c.isSpiritGuard()) || !result.getFirstBlocker().isDoor()) {
/*     */           
/* 288 */           if (rotateFromBlocker) {
/* 289 */             c.rotateRandom(lRotation, 100);
/*     */           }
/*     */           return;
/*     */         } 
/*     */       }
/*     */     } 
/* 295 */     lPosX += lXPosMod;
/* 296 */     lPosY += lYPosMod;
/*     */ 
/*     */     
/* 299 */     if (lPosX >= (Zones.worldTileSizeX - 1 << 2) || lPosX < 0.0F || lPosY < 0.0F || lPosY >= (Zones.worldTileSizeY - 1 << 2)) {
/*     */       
/* 301 */       c.destroy();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 306 */     VolaTile vt = Zones.getOrCreateTile(lNewTileX, lNewTileY, c.isOnSurface());
/* 307 */     lPosZ = Zones.calculatePosZ(lPosX, lPosY, vt, c.isOnSurface(), c.isFloating(), c.getPositionZ(), c, newBridgeId);
/*     */     
/* 309 */     if (c.isFloating()) {
/* 310 */       lPosZ = Math.max((c.getTemplate()).offZ, lPosZ);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 319 */     if (lPosZ < 0.5D)
/*     */     {
/* 321 */       if ((lPosZ > -2.0F || oldPosZ <= -2.0F) && (oldPosZ < 0.0F || c.getTarget() != null) && c.isSwimming()) {
/*     */         
/* 323 */         lPosZ = Math.max(-1.25F, lPosZ);
/* 324 */         if (c.isFloating()) {
/* 325 */           lPosZ = Math.max((c.getTemplate()).offZ, lPosZ);
/*     */         }
/* 327 */       } else if (lPosZ < -0.7D && !c.isSubmerged()) {
/*     */         
/* 329 */         if (rotateFromBlocker)
/* 330 */           c.rotateRandom(lRotation, 100); 
/* 331 */         if (c.getTarget() != null) {
/* 332 */           c.setTarget(-10L, true);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 338 */     c.getStatus().setPositionX(lPosX);
/* 339 */     c.getStatus().setPositionY(lPosY);
/* 340 */     c.getStatus().setRotation(lRotation);
/*     */     
/* 342 */     if (Structure.isGroundFloorAtPosition(lPosX, lPosY, c.isOnSurface())) {
/* 343 */       c.getStatus().setPositionZ(lPosZ + 0.25F);
/*     */     } else {
/* 345 */       c.getStatus().setPositionZ(lPosZ);
/*     */     } 
/* 347 */     c.moved(lPosX - oldPosX, lPosY - oldPosY, lPosZ - oldPosZ, lDiffTileX, lDiffTileY);
/*     */ 
/*     */     
/* 350 */     if (c.getTarget() != null)
/*     */     {
/* 352 */       if (c.getTarget().getCurrentTile() == c.getCurrentTile() && c.getTarget().getFloorLevel() != c.getFloorLevel())
/*     */       {
/* 354 */         if (c.isSpiritGuard()) {
/* 355 */           c.pushToFloorLevel(c.getTarget().getFloorLevel());
/*     */         } else {
/*     */           
/* 358 */           Floor[] currentTileFloors = c.getCurrentTile().getFloors();
/* 359 */           for (Floor f : currentTileFloors) {
/*     */             
/* 361 */             if (c.getTarget().getFloorLevel() > c.getFloorLevel()) {
/*     */               
/* 363 */               if (f.getFloorLevel() == c.getFloorLevel() + 1 && (
/* 364 */                 f.isOpening() || f.isStair())) {
/* 365 */                 c.pushToFloorLevel(f.getFloorLevel());
/*     */               
/*     */               }
/*     */             }
/* 369 */             else if (f.getFloorLevel() == c.getFloorLevel() - 1 && (
/* 370 */               f.isOpening() || f.isStair())) {
/* 371 */               c.pushToFloorLevel(f.getFloorLevel());
/*     */             } 
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getNewBridgeId(int oldTileX, int oldTileY, boolean onSurface, long oldBridgeId, int floorLevel, int newTileX, int newTileY) {
/* 393 */     if (oldBridgeId == -10L) {
/*     */ 
/*     */       
/* 396 */       BridgePart bp = Zones.getBridgePartFor(newTileX, newTileY, onSurface);
/* 397 */       if (bp != null && bp.isFinished() && bp.hasAnExit())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 402 */         if (newTileY < oldTileY && bp.getSouthExitFloorLevel() == floorLevel) {
/* 403 */           return bp.getStructureId();
/*     */         }
/* 405 */         if (newTileX > oldTileX && bp.getWestExitFloorLevel() == floorLevel) {
/* 406 */           return bp.getStructureId();
/*     */         }
/* 408 */         if (newTileY > oldTileY && bp.getNorthExitFloorLevel() == floorLevel) {
/* 409 */           return bp.getStructureId();
/*     */         }
/* 411 */         if (newTileX < oldTileX && bp.getEastExitFloorLevel() == floorLevel) {
/* 412 */           return bp.getStructureId();
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 418 */       BridgePart bp = Zones.getBridgePartFor(newTileX, newTileY, onSurface);
/* 419 */       if (bp == null)
/*     */       {
/*     */         
/* 422 */         return -10L;
/*     */       }
/*     */     } 
/* 425 */     return oldBridgeId;
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
/*     */   protected PathTile getMovementTarget(Creature c, int tilePosX, int tilePosY) {
/* 439 */     tilePosX = Zones.safeTileX(tilePosX);
/* 440 */     tilePosY = Zones.safeTileY(tilePosY);
/*     */     
/* 442 */     if (!c.isOnSurface()) {
/*     */       
/* 444 */       int tile = Server.caveMesh.getTile(tilePosX, tilePosY);
/* 445 */       if (!Tiles.isSolidCave(Tiles.decodeType(tile)) && (
/* 446 */         Tiles.decodeHeight(tile) > -c.getHalfHeightDecimeters() || c.isSwimming() || c.isSubmerged())) {
/* 447 */         return new PathTile(tilePosX, tilePosY, tile, c.isOnSurface(), -1);
/*     */       }
/*     */     } else {
/*     */       
/* 451 */       int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY);
/* 452 */       if (Tiles.decodeHeight(tile) > -c.getHalfHeightDecimeters() || c.isSwimming() || c.isSubmerged()) {
/* 453 */         return new PathTile(tilePosX, tilePosY, tile, c.isOnSurface(), c.getFloorLevel());
/*     */       }
/*     */     } 
/* 456 */     return null;
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
/*     */   public boolean pollCreature(Creature c, long delta) {
/* 470 */     long start = System.nanoTime();
/* 471 */     boolean isDead = false;
/*     */     
/* 473 */     if (c.getSpellEffects() != null) {
/* 474 */       c.getSpellEffects().poll();
/*     */     }
/* 476 */     if (pollSpecialPreAttack(c, delta)) return true; 
/* 477 */     isDead = pollAttack(c, delta);
/*     */ 
/*     */     
/* 480 */     if (c.getActions().poll(c))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 491 */       if (c.isFighting()) {
/* 492 */         c.setFighting();
/*     */       } else {
/*     */         
/* 495 */         if (isDead) return true; 
/* 496 */         if (pollSpecialPreBreeding(c, delta)) return true; 
/* 497 */         isDead = pollBreeding(c, delta);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 507 */         if (isDead) return true; 
/* 508 */         if (pollSpecialPreMovement(c, delta)) return true; 
/* 509 */         isDead = pollMovement(c, delta);
/*     */         
/* 511 */         if (isDead) return true; 
/* 512 */         if (System.currentTimeMillis() - c.lastSavedPos > 3600000L) {
/*     */           
/* 514 */           c.lastSavedPos = System.currentTimeMillis() + (Server.rand.nextInt(3600) * 1000);
/*     */           try {
/* 516 */             c.savePosition(c.getStatus().getZoneId());
/* 517 */             c.getStatus().save();
/* 518 */           } catch (IOException e) {
/* 519 */             logger.warning("Unable to save creature position, creature id: " + c.getWurmId() + " reason: " + e.getMessage());
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
/* 533 */     if (c.getDamageCounter() > 0) {
/*     */       
/* 535 */       c.setDamageCounter((short)(c.getDamageCounter() - 1));
/* 536 */       isDead = pollDamageCounter(c, delta, c.getDamageCounter());
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
/* 547 */     if (isDead) return true;
/*     */     
/* 549 */     if (pollSpecialPreItems(c, delta)) return true; 
/* 550 */     pollItems(c, delta);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 560 */     if (pollSpecialPreMisc(c, delta)) return true; 
/* 561 */     isDead = pollMisc(c, delta);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     if (isDead) return true;
/*     */     
/* 572 */     isDead = pollSpecialFinal(c, delta);
/* 573 */     return isDead;
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
/*     */   public int woundDamageChanged(Creature c, int damageToAdd) {
/* 588 */     return damageToAdd;
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
/*     */   public double causedWound(Creature c, @Nullable Creature target, byte dmgType, int dmgPosition, float armourMod, double damage) {
/* 607 */     return damage;
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
/*     */   public double receivedWound(Creature c, @Nullable Creature performer, byte dmgType, int dmgPosition, float armourMod, double damage) {
/* 626 */     return damage;
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
/*     */   public boolean creatureDied(Creature creature) {
/* 639 */     return false;
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
/*     */   public boolean maybeAttackCreature(Creature c, VirtualZone vz, Creature mover) {
/* 654 */     return false;
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
/*     */   protected abstract boolean pollMovement(Creature paramCreature, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean pollAttack(Creature paramCreature, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean pollBreeding(Creature paramCreature, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollSpecialPreBreeding(Creature c, long delta) {
/* 708 */     return false;
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
/*     */   protected boolean pollSpecialPreMovement(Creature c, long delta) {
/* 722 */     return false;
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
/*     */   protected boolean pollSpecialPreAttack(Creature c, long delta) {
/* 736 */     return false;
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
/*     */   protected boolean pollSpecialPreItems(Creature c, long delta) {
/* 750 */     return false;
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
/*     */   protected boolean pollSpecialPreMisc(Creature c, long delta) {
/* 764 */     return false;
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
/*     */   protected boolean pollSpecialFinal(Creature c, long delta) {
/* 778 */     return false;
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
/*     */   protected boolean pollDamageCounter(Creature c, long delta, short damageCounter) {
/* 791 */     if (damageCounter == 0) {
/* 792 */       c.removeWoundMod();
/*     */     }
/* 794 */     return false;
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
/*     */   protected void pollItems(Creature c, long delta) {
/* 808 */     c.pollItems();
/* 809 */     if (c.getBody() != null) {
/* 810 */       c.getBody().poll();
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
/*     */   protected boolean pollMisc(Creature c, long delta) {
/* 824 */     c.pollStamina();
/* 825 */     c.pollFavor();
/* 826 */     c.pollLoyalty();
/*     */     
/* 828 */     c.trimAttackers(false);
/* 829 */     c.numattackers = 0;
/* 830 */     c.hasAddedToAttack = false;
/*     */     
/* 832 */     if (!c.isUnique() && !c.isFighting() && c.isDominated() && c.goOffline) {
/*     */       
/* 834 */       logger.log(Level.INFO, c.getName() + " going offline.");
/* 835 */       Creatures.getInstance().setCreatureOffline(c);
/* 836 */       c.goOffline = false;
/* 837 */       return true;
/*     */     } 
/*     */     
/* 840 */     return false;
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
/*     */   protected boolean isTimerReady(Creature c, int timerId, long minTime) {
/* 854 */     if (c.getCreatureAIData().getTimer(timerId) < minTime) {
/* 855 */       return false;
/*     */     }
/* 857 */     return true;
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
/*     */   protected void increaseTimer(Creature c, long delta, int... timerIds) {
/* 869 */     for (int id : timerIds) {
/* 870 */       c.getCreatureAIData().setTimer(id, c.getCreatureAIData().getTimer(id) + delta);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetTimer(Creature c, int... timerIds) {
/* 881 */     for (int timerId : timerIds)
/* 882 */       c.getCreatureAIData().setTimer(timerId, 0L); 
/*     */   }
/*     */   
/*     */   public abstract CreatureAIData createCreatureAIData();
/*     */   
/*     */   public abstract void creatureCreated(Creature paramCreature);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\CreatureAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */