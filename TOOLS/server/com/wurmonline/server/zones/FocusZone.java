/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.FoliageAge;
/*     */ import com.wurmonline.mesh.GrassData;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.mesh.TreeData;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.behaviours.Terraforming;
/*     */ import com.wurmonline.server.epic.Hota;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ public final class FocusZone
/*     */   extends Zone
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final String loadAll = "SELECT * FROM FOCUSZONES";
/*     */   private static final String addZone = "INSERT INTO FOCUSZONES (STARTX,STARTY,ENDX,ENDY,TYPE,NAME,DESCRIPTION) VALUES (?,?,?,?,?,?,?)";
/*     */   private static final String deleteZone = "DELETE FROM FOCUSZONES WHERE STARTX=? AND STARTY=? AND ENDX=? AND ENDY=? AND TYPE=? AND NAME=?";
/*  57 */   private static final Set<FocusZone> focusZones = new HashSet<>();
/*     */   
/*  59 */   private static final Logger logger = Logger.getLogger(FocusZone.class.getName());
/*     */   
/*     */   private final byte type;
/*     */   public static final byte TYPE_NONE = 0;
/*     */   public static final byte TYPE_VOLCANO = 1;
/*     */   public static final byte TYPE_PVP = 2;
/*     */   public static final byte TYPE_NAME = 3;
/*     */   public static final byte TYPE_NAME_POPUP = 4;
/*     */   public static final byte TYPE_NON_PVP = 5;
/*     */   public static final byte TYPE_PVP_HOTA = 6;
/*     */   public static final byte TYPE_PVP_BATTLECAMP = 7;
/*     */   public static final byte TYPE_FLATTEN_DIRT = 8;
/*     */   public static final byte TYPE_HOUSE_WOOD = 9;
/*     */   public static final byte TYPE_HOUSE_STONE = 10;
/*     */   public static final byte TYPE_PREM_SPAWN = 11;
/*     */   public static final byte TYPE_NO_BUILD = 12;
/*     */   public static final byte TYPE_TALLWALLS = 13;
/*     */   public static final byte TYPE_FOG = 14;
/*     */   public static final byte TYPE_FLATTEN_ROCK = 15;
/*     */   public static final byte TYPE_REPLENISH_DIRT = 16;
/*     */   public static final byte TYPE_REPLENISH_TREES = 17;
/*     */   public static final byte TYPE_REPLENISH_ORES = 18;
/*  81 */   private int polls = 0;
/*  82 */   private Item projectile = null;
/*  83 */   private int pollSecondLanded = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FocusZone(int aStartX, int aEndX, int aStartY, int aEndY, byte zoneType, String aName, String aDescription, boolean save) {
/*  98 */     super(aStartX, aEndX, aStartY, aEndY, true);
/*  99 */     this.name = aName;
/* 100 */     this.description = aDescription;
/* 101 */     this.type = zoneType;
/* 102 */     if (save) {
/*     */       
/*     */       try {
/*     */         
/* 106 */         save();
/* 107 */         focusZones.add(this);
/*     */       }
/* 109 */       catch (IOException iox) {
/*     */         
/* 111 */         logger.log(Level.INFO, iox.getMessage(), iox);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 118 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDescription() {
/* 123 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPvP() {
/* 128 */     return (this.type == 2 || this.type == 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNonPvP() {
/* 133 */     return (this.type == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNamePopup() {
/* 138 */     return (this.type == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isName() {
/* 143 */     return (this.type == 3 || this.type == 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isBattleCamp() {
/* 148 */     return (this.type == 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPvPHota() {
/* 153 */     return (this.type == 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPremSpawnOnly() {
/* 158 */     return (this.type == 11);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNoBuild() {
/* 163 */     return (this.type == 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isFog() {
/* 168 */     return (this.type == 14);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isType(byte wantedType) {
/* 173 */     return (this.type == wantedType);
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
/*     */   void load() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pollAll() {
/* 196 */     for (FocusZone fz : focusZones)
/*     */     {
/* 198 */       fz.poll();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<FocusZone> getZonesAt(int tilex, int tiley) {
/* 204 */     if (focusZones.size() > 0) {
/*     */       
/* 206 */       Set<FocusZone> toReturn = new HashSet<>();
/* 207 */       for (FocusZone fz : focusZones) {
/*     */         
/* 209 */         if (fz.covers(tilex, tiley))
/* 210 */           toReturn.add(fz); 
/*     */       } 
/* 212 */       return toReturn;
/*     */     } 
/* 214 */     return focusZones;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isPvPZoneAt(int tilex, int tiley) {
/* 219 */     if (focusZones.size() > 0) {
/*     */       
/* 221 */       for (FocusZone fz : focusZones) {
/*     */         
/* 223 */         if (fz.covers(tilex, tiley) && fz.isPvP())
/* 224 */           return true; 
/*     */       } 
/* 226 */       return false;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isNonPvPZoneAt(int tilex, int tiley) {
/* 233 */     if (focusZones.size() > 0) {
/*     */       
/* 235 */       for (FocusZone fz : focusZones) {
/*     */         
/* 237 */         if (fz.covers(tilex, tiley) && fz.isNonPvP())
/* 238 */           return true; 
/*     */       } 
/* 240 */       return false;
/*     */     } 
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isPremSpawnOnlyZoneAt(int tilex, int tiley) {
/* 247 */     if (focusZones.size() > 0) {
/*     */       
/* 249 */       for (FocusZone fz : focusZones) {
/*     */         
/* 251 */         if (fz.covers(tilex, tiley) && fz.isPremSpawnOnly())
/* 252 */           return true; 
/*     */       } 
/* 254 */       return false;
/*     */     } 
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isNoBuildZoneAt(int tilex, int tiley) {
/* 261 */     if (focusZones.size() > 0) {
/*     */       
/* 263 */       for (FocusZone fz : focusZones) {
/*     */         
/* 265 */         if (fz.covers(tilex, tiley) && fz.isNoBuild())
/* 266 */           return true; 
/*     */       } 
/* 268 */       return false;
/*     */     } 
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isFogZoneAt(int tilex, int tiley) {
/* 275 */     if (focusZones.size() > 0) {
/*     */       
/* 277 */       for (FocusZone fz : focusZones) {
/*     */         
/* 279 */         if (fz.covers(tilex, tiley) && fz.isFog())
/* 280 */           return true; 
/*     */       } 
/* 282 */       return false;
/*     */     } 
/* 284 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isZoneAt(int tilex, int tiley, byte wantedType) {
/* 289 */     if (focusZones.size() > 0) {
/*     */       
/* 291 */       for (FocusZone fz : focusZones) {
/*     */         
/* 293 */         if (fz.covers(tilex, tiley) && fz.isType(wantedType))
/* 294 */           return true; 
/*     */       } 
/* 296 */       return false;
/*     */     } 
/* 298 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FocusZone[] getAllZones() {
/* 303 */     return focusZones.<FocusZone>toArray(new FocusZone[focusZones.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FocusZone getHotaZone() {
/* 308 */     for (FocusZone fz : getAllZones()) {
/*     */       
/* 310 */       if (fz.isPvPHota())
/* 311 */         return fz; 
/*     */     } 
/* 313 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAll() {
/* 318 */     long now = System.nanoTime();
/* 319 */     int numberOfZonesLoaded = 0;
/* 320 */     Connection dbcon = null;
/* 321 */     PreparedStatement ps = null;
/* 322 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 325 */       dbcon = DbConnector.getZonesDbCon();
/* 326 */       ps = dbcon.prepareStatement("SELECT * FROM FOCUSZONES");
/* 327 */       rs = ps.executeQuery();
/* 328 */       while (rs.next())
/*     */       {
/*     */         
/* 331 */         FocusZone fz = new FocusZone(rs.getInt("STARTX"), rs.getInt("ENDX"), rs.getInt("STARTY"), rs.getInt("ENDY"), rs.getByte("TYPE"), rs.getString("NAME"), rs.getString("DESCRIPTION"), false);
/* 332 */         focusZones.add(fz);
/* 333 */         numberOfZonesLoaded++;
/*     */       }
/*     */     
/* 336 */     } catch (SQLException sqex) {
/*     */       
/* 338 */       logger.log(Level.WARNING, "Problem loading focus zone, count is " + numberOfZonesLoaded + " due to " + sqex
/* 339 */           .getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 343 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 344 */       DbConnector.returnConnection(dbcon);
/* 345 */       float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 346 */       logger.log(Level.INFO, "Loaded " + numberOfZonesLoaded + " focus zones. It took " + lElapsedTime + " millis.");
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
/*     */   void loadFences() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() throws IOException {
/* 367 */     if (getHotaZone() == this)
/*     */     {
/* 369 */       Hota.destroyHota();
/*     */     }
/* 371 */     focusZones.remove(this);
/* 372 */     Connection dbcon = null;
/* 373 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 376 */       dbcon = DbConnector.getZonesDbCon();
/* 377 */       ps = dbcon.prepareStatement("DELETE FROM FOCUSZONES WHERE STARTX=? AND STARTY=? AND ENDX=? AND ENDY=? AND TYPE=? AND NAME=?");
/* 378 */       ps.setInt(1, this.startX);
/* 379 */       ps.setInt(2, this.startY);
/* 380 */       ps.setInt(3, this.endX);
/* 381 */       ps.setInt(4, this.endY);
/* 382 */       ps.setByte(5, this.type);
/* 383 */       ps.setString(6, this.name);
/* 384 */       ps.executeUpdate();
/*     */     }
/* 386 */     catch (SQLException sqex) {
/*     */       
/* 388 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 392 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 393 */       DbConnector.returnConnection(dbcon);
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
/*     */   void save() throws IOException {
/* 405 */     Connection dbcon = null;
/* 406 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 409 */       dbcon = DbConnector.getZonesDbCon();
/* 410 */       ps = dbcon.prepareStatement("INSERT INTO FOCUSZONES (STARTX,STARTY,ENDX,ENDY,TYPE,NAME,DESCRIPTION) VALUES (?,?,?,?,?,?,?)");
/* 411 */       ps.setInt(1, this.startX);
/* 412 */       ps.setInt(2, this.startY);
/* 413 */       ps.setInt(3, this.endX);
/* 414 */       ps.setInt(4, this.endY);
/* 415 */       ps.setByte(5, this.type);
/* 416 */       ps.setString(6, this.name);
/* 417 */       ps.setString(7, this.description);
/* 418 */       ps.executeUpdate();
/*     */     }
/* 420 */     catch (SQLException sqex) {
/*     */       
/* 422 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 426 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 427 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void poll() {
/* 436 */     this.polls++;
/* 437 */     if (this.type == 1 && this.polls % 5 == 0) {
/*     */       
/* 439 */       boolean foundLava = false;
/* 440 */       for (int x = this.startX; x < this.endX; x++) {
/*     */         
/* 442 */         for (int y = this.startY; y < this.endY; y++) {
/*     */           
/* 444 */           if (Tiles.decodeType(Server.caveMesh.getTile(x, y)) == Tiles.Tile.TILE_CAVE_WALL_LAVA.id)
/*     */           {
/* 446 */             for (int xx = -1; xx <= 1; xx++) {
/*     */               
/* 448 */               for (int yy = -1; yy <= 1; yy++) {
/*     */ 
/*     */                 
/* 451 */                 if ((xx != 0 || yy != 0) && (xx == 0 || yy == 0))
/*     */                 {
/* 453 */                   if (!Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x + xx, y + yy)))) {
/*     */                     
/* 455 */                     logger.log(Level.INFO, "Lava flow at " + (x + xx) + "," + (y + yy));
/* 456 */                     Terraforming.setAsRock(x + xx, y + yy, true, true);
/* 457 */                     foundLava = true;
/*     */                     break;
/*     */                   } 
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/* 464 */           if (foundLava)
/*     */             break; 
/*     */         } 
/*     */       } 
/* 468 */       if (this.pollSecondLanded > 0 && this.polls >= this.pollSecondLanded) {
/*     */         
/* 470 */         if (this.projectile != null) {
/*     */ 
/*     */           
/*     */           try {
/* 474 */             Zone z = Zones.getZone(this.projectile.getTileX(), this.projectile.getTileY(), true);
/* 475 */             z.addItem(this.projectile);
/* 476 */             logger.log(Level.INFO, "Added projectile to " + this.projectile.getTileX() + "," + this.projectile.getTileY());
/*     */           }
/* 478 */           catch (NoSuchZoneException nsz) {
/*     */             
/* 480 */             logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*     */           } 
/* 482 */           this.projectile = null;
/*     */         } 
/* 484 */         this.pollSecondLanded = 0;
/* 485 */         this.polls = 0;
/*     */       }
/* 487 */       else if (this.polls == 42600L) {
/*     */         
/* 489 */         Server.getInstance().broadCastNormal(this.name + " rumbles.");
/*     */       }
/* 491 */       else if (this.polls == 43200L) {
/*     */         
/* 493 */         Server.getInstance().broadCastNormal(this.name + " rumbles intensely.");
/*     */       }
/* 495 */       else if (this.polls >= 43200L) {
/*     */ 
/*     */         
/* 498 */         if (Server.rand.nextInt(3600) == 0) {
/*     */           
/*     */           try {
/*     */             
/* 502 */             this.projectile = ItemFactory.createItem(692, 80.0F + Server.rand
/* 503 */                 .nextFloat() * 20.0F, null);
/* 504 */             int centerX = getStartX() + getSize() / 2;
/* 505 */             int centerY = getStartY() + getSize() / 2;
/* 506 */             int randX = Zones.safeTileX(centerX - 10 + Server.rand.nextInt(21));
/* 507 */             int randY = Zones.safeTileY(centerY - 10 + Server.rand.nextInt(21));
/* 508 */             int landX = Zones.safeTileX(randX - 100 + Server.rand.nextInt(200));
/* 509 */             int landY = Zones.safeTileY(randY - 100 + Server.rand.nextInt(200));
/*     */             
/* 511 */             int secondsInAir = Math.max(5, Math.max(Math.abs(randX - landX), Math.abs(randY - landY)) / 10);
/* 512 */             this.pollSecondLanded = this.polls + secondsInAir;
/* 513 */             float sx = (randX * 4 + 2);
/* 514 */             float sy = (randY * 4 + 2);
/* 515 */             float ex = (landX * 4 + 2);
/* 516 */             float ey = (landY * 4 + 2);
/* 517 */             float rot = Server.rand.nextFloat() * 360.0F;
/* 518 */             logger.log(Level.INFO, "Creating projectile from " + randX + "," + randY + " to " + landX + "," + landY);
/*     */             
/*     */             try {
/* 521 */               float sh = Zones.calculateHeight(sx, sy, true) - 10.0F;
/* 522 */               float eh = Zones.calculateHeight(ex, ey, true);
/*     */               
/* 524 */               this.projectile.setPosXYZRotation(ex, ey, eh, rot);
/*     */               
/* 526 */               Player[] players = Players.getInstance().getPlayers();
/* 527 */               for (int i = 0; i < players.length; i++)
/*     */               {
/*     */ 
/*     */ 
/*     */                 
/* 532 */                 if (players[i].isWithinDistanceTo(sx, sy, sh, 500.0F) || players[i]
/* 533 */                   .isWithinDistanceTo(ex, ey, eh, 500.0F))
/*     */                 {
/* 535 */                   players[i].getCommunicator().sendProjectile(this.projectile.getWurmId(), (byte)3, this.projectile
/* 536 */                       .getModelName(), this.projectile
/* 537 */                       .getName(), this.projectile.getMaterial(), sx, sy, sh, rot, (byte)0, landX, landY, eh, -10L, -10L, secondsInAir, secondsInAir);
/*     */                 }
/*     */               }
/*     */             
/*     */             }
/* 542 */             catch (NoSuchZoneException nsz) {
/*     */               
/* 544 */               logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/* 545 */               this.projectile = null;
/*     */               
/* 547 */               this.pollSecondLanded = 0;
/* 548 */               this.polls = 0;
/*     */             }
/*     */           
/* 551 */           } catch (FailedException fe) {
/*     */             
/* 553 */             logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/* 554 */             this.projectile = null;
/*     */             
/* 556 */             this.pollSecondLanded = 0;
/* 557 */             this.polls = 0;
/*     */           }
/* 559 */           catch (NoSuchTemplateException nst) {
/*     */             
/* 561 */             logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/* 562 */             this.projectile = null;
/*     */             
/* 564 */             this.pollSecondLanded = 0;
/* 565 */             this.polls = 0;
/*     */           }
/*     */         
/*     */         }
/*     */       } 
/* 570 */     } else if (this.type == 16) {
/*     */ 
/*     */       
/* 573 */       if (this.polls % 900L == 0L) {
/*     */ 
/*     */         
/* 576 */         float avgHeight = (Zones.getHeightForNode(getStartX(), getStartY(), 1) + Zones.getHeightForNode(getStartX(), getEndY() + 1, 1) + Zones.getHeightForNode(getEndX() + 1, getStartY(), 1) + Zones.getHeightForNode(getEndX() + 1, getEndY() + 1, 1)) / 4.0F;
/*     */         
/* 578 */         for (int tileX = getStartX() + 1; tileX < getEndX(); tileX++) {
/*     */           
/* 580 */           for (int tileY = getStartY() + 1; tileY < getEndY(); tileY++) {
/*     */             
/* 582 */             int tile = Server.surfaceMesh.getTile(tileX, tileY);
/* 583 */             byte type = Tiles.decodeType(tile);
/*     */             
/* 585 */             if (type != Tiles.Tile.TILE_DIRT.id && type != Tiles.Tile.TILE_DIRT_PACKED.id && type != Tiles.Tile.TILE_SAND.id && !Tiles.isGrassType(type)) {
/*     */               continue;
/*     */             }
/* 588 */             short actualHeight = Tiles.decodeHeight(tile);
/* 589 */             if (actualHeight > avgHeight * 10.0F + 5.0F) {
/* 590 */               Server.surfaceMesh.setTile(tileX, tileY, Tiles.encode((short)(actualHeight - 1), type, Tiles.decodeData(tile)));
/* 591 */             } else if (actualHeight < avgHeight * 10.0F - 5.0F) {
/* 592 */               Server.surfaceMesh.setTile(tileX, tileY, Tiles.encode((short)(actualHeight + 1), type, Tiles.decodeData(tile)));
/*     */             } else {
/*     */               continue;
/*     */             } 
/* 596 */             Players.getInstance().sendChangedTile(tileX, tileY, true, true);
/*     */ 
/*     */             
/*     */             try {
/* 600 */               Zone toCheckForChange = Zones.getZone(tileX, tileY, true);
/* 601 */               toCheckForChange.changeTile(tileX, tileY);
/*     */             }
/* 603 */             catch (NoSuchZoneException nsz) {
/*     */               
/* 605 */               logger.log(Level.INFO, "no such zone?: " + tileX + ", " + tileY, (Throwable)nsz);
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/* 612 */     } else if (this.type == 17) {
/*     */ 
/*     */       
/* 615 */       if (this.polls % 300L == 0L)
/*     */       {
/* 617 */         for (int tileX = getStartX() + 1; tileX < getEndX(); tileX++) {
/*     */           
/* 619 */           for (int tileY = getStartY() + 1; tileY < getEndY(); tileY++) {
/*     */             
/* 621 */             int tile = Server.surfaceMesh.getTile(tileX, tileY);
/* 622 */             byte type = Tiles.decodeType(tile);
/*     */             
/* 624 */             if (Tiles.isTree(type)) {
/*     */ 
/*     */               
/* 627 */               byte age = FoliageAge.getAgeAsByte(Tiles.decodeData(tile));
/* 628 */               if (age <= FoliageAge.MATURE_THREE.getAgeId())
/*     */               {
/*     */                 
/* 631 */                 int newData = Tiles.encodeTreeData((byte)(age + 1), false, false, GrassData.GrowthTreeStage.decodeTileData(Tiles.decodeData(tile)));
/* 632 */                 Server.surfaceMesh.setTile(tileX, tileY, Tiles.encode(Tiles.decodeHeight(tile), Tiles.decodeType(tile), (byte)newData));
/* 633 */                 Players.getInstance().sendChangedTile(tileX, tileY, true, false);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 638 */               boolean skip = false;
/*     */               
/* 640 */               for (int x = tileX - 1; x < tileX + 1; x++) {
/* 641 */                 for (int y = tileY - 1; y < tileY + 1; y++) {
/* 642 */                   if (Tiles.isTree(Tiles.decodeType(Server.surfaceMesh.getTile(x, y))))
/*     */                   {
/* 644 */                     if (tileX == 0 || tileY == 0)
/* 645 */                       skip = true;  } 
/*     */                 } 
/*     */               } 
/* 648 */               if (!skip) {
/*     */ 
/*     */ 
/*     */                 
/* 652 */                 TreeData.TreeType treeType = TreeData.TreeType.BIRCH;
/* 653 */                 switch (Server.rand.nextInt(5)) {
/*     */                   
/*     */                   case 0:
/* 656 */                     treeType = TreeData.TreeType.LINDEN;
/*     */                     break;
/*     */                   case 1:
/* 659 */                     treeType = TreeData.TreeType.PINE;
/*     */                     break;
/*     */                   case 2:
/* 662 */                     treeType = TreeData.TreeType.WALNUT;
/*     */                     break;
/*     */                   case 3:
/* 665 */                     treeType = TreeData.TreeType.CEDAR;
/*     */                     break;
/*     */                 } 
/* 668 */                 int newData = Tiles.encodeTreeData(FoliageAge.YOUNG_ONE, false, false, GrassData.GrowthTreeStage.SHORT);
/* 669 */                 Server.setSurfaceTile(tileX, tileY, Tiles.decodeHeight(tile), treeType.asNormalTree(), (byte)newData);
/* 670 */                 Server.setWorldResource(tileX, tileY, 0);
/* 671 */                 Players.getInstance().sendChangedTile(tileX, tileY, true, true);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }  } 
/* 676 */     } else if (this.type == 18) {
/*     */       
/* 678 */       if (this.polls % 900L == 0L)
/*     */       {
/* 680 */         for (int tileX = getStartX() + 1; tileX < getEndX(); tileX++) {
/*     */           
/* 682 */           for (int tileY = getStartY() + 1; tileY < getEndY(); tileY++) {
/*     */             
/* 684 */             int tile = Server.caveMesh.getTile(tileX, tileY);
/* 685 */             byte type = Tiles.decodeType(tile);
/*     */             
/* 687 */             if (Tiles.isOreCave(type)) {
/*     */ 
/*     */               
/* 690 */               int resource = Server.getCaveResource(tileX, tileY);
/* 691 */               if (resource < 1000) {
/*     */                 
/* 693 */                 resource = Server.rand.nextInt(10000) + 10000;
/* 694 */                 Server.setCaveResource(tileX, tileY, resource);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\FocusZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */