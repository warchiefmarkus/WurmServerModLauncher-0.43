/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.math.Vector2f;
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.tutorial.PlayerTutorial;
/*     */ import com.wurmonline.server.utils.CreaturePositionDatabaseUpdater;
/*     */ import com.wurmonline.server.utils.CreaturePositionDbUpdatable;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.utils.PlayerPositionDatabaseUpdater;
/*     */ import com.wurmonline.server.utils.PlayerPositionDbUpdatable;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreaturePos
/*     */   implements CounterTypes, TimeConstants, MiscConstants
/*     */ {
/*  54 */   private static final Logger logger = Logger.getLogger(CreaturePos.class.getName());
/*     */   
/*     */   private static final String createPos = "insert into POSITION (POSX, POSY, POSZ, ROTATION,ZONEID,LAYER,ONBRIDGE, WURMID) values (?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String updatePosOld = "update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?";
/*     */   
/*     */   private static final String updatePos = "INSERT INTO POSITION (POSX, POSY, POSZ, ROTATION, ZONEID, LAYER, ONBRIDGE, WURMID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE POSX=VALUES(POSX), POSY=VALUES(POSY), POSZ=VALUES(POSZ), ROTATION=VALUES(ROTATION), ZONEID=VALUES(ZONEID), LAYER=VALUES(LAYER), ONBRIDGE=VALUES(ONBRIDGE)";
/*     */   private static final String loadAllPos = "SELECT * FROM POSITION";
/*     */   private static final String deletePosition = "DELETE FROM POSITION WHERE WURMID=?";
/*     */   private boolean changed = false;
/*     */   private float posX;
/*     */   private float posY;
/*     */   private float posZ;
/*     */   private float rotation;
/*     */   private int zoneId;
/*     */   private int layer;
/*  70 */   private long bridgeId = -10L;
/*     */   
/*     */   private final long wurmid;
/*     */   private final boolean isPlayer;
/*  74 */   protected long lastSavedPos = System.currentTimeMillis() - Server.rand.nextInt(2000000);
/*     */   
/*     */   public static boolean logCreaturePos = false;
/*     */   
/*     */   protected static final long saveIntervalPlayer = 60000L;
/*     */   protected static final long saveIntervalCreature = 600000L;
/*  80 */   private static final ConcurrentHashMap<Long, CreaturePos> allPositions = new ConcurrentHashMap<>();
/*     */   
/*  82 */   private static PreparedStatement cretPosPS = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static int cretPosPSCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static int totalCretPosPSCount = 0;
/*     */   
/*  94 */   private static PreparedStatement playPosPS = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private static int playPosPSCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static int totalPlayPosPSCount = 0;
/*     */ 
/*     */   
/* 107 */   private static final CreaturePositionDatabaseUpdater creatureDbPosUpdater = new CreaturePositionDatabaseUpdater("Creature Database Position Updater", Constants.numberOfDbCreaturePositionsToUpdateEachTime);
/*     */ 
/*     */   
/* 110 */   private static final PlayerPositionDatabaseUpdater playerDbPosUpdater = new PlayerPositionDatabaseUpdater("Player Database Position Updater", Constants.numberOfDbPlayerPositionsToUpdateEachTime);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreaturePos(long wurmId, float posx, float posy, float posz, float rot, int zone, int layerId, long bridge, boolean createInDatabase) {
/* 119 */     this.wurmid = wurmId;
/* 120 */     this.isPlayer = (WurmId.getType(this.wurmid) == 0);
/* 121 */     setPosX(posx);
/* 122 */     setPosY(posy);
/* 123 */     setPosZ(posz, false);
/*     */     
/* 125 */     this.rotation = rot;
/* 126 */     setZoneId(zone);
/* 127 */     setLayer(layerId);
/* 128 */     setBridgeId(bridge);
/* 129 */     allPositions.put(Long.valueOf(this.wurmid), this);
/* 130 */     if (createInDatabase) {
/*     */       
/* 132 */       this.changed = true;
/* 133 */       save(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChanged() {
/* 144 */     return this.changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged(boolean hasChanged) {
/* 155 */     this.changed = hasChanged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Vector2f getPos2f() {
/* 164 */     return new Vector2f(this.posX, this.posY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Vector3f getPos3f() {
/* 173 */     return new Vector3f(this.posX, this.posY, this.posZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosX() {
/* 183 */     return this.posX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosX(float posx) {
/* 194 */     if (this.posX != posx) {
/*     */       
/* 196 */       if ((int)this.posX >> 2 != (int)posx >> 2)
/* 197 */         this.changed = true; 
/* 198 */       this.posX = posx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosY() {
/* 209 */     return this.posY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosY(float posy) {
/* 220 */     if (this.posY != posy) {
/*     */       
/* 222 */       if ((int)this.posY >> 2 != (int)posy >> 2)
/* 223 */         this.changed = true; 
/* 224 */       this.posY = posy;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosZ() {
/* 235 */     return this.posZ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosZ(float posz, boolean forceSave) {
/* 246 */     if (this.posZ != posz) {
/*     */       
/* 248 */       this.posZ = posz;
/* 249 */       if (forceSave)
/*     */       {
/* 251 */         this.changed = true;
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
/*     */   public float getRotation() {
/* 263 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(float rot) {
/* 274 */     if (this.rotation != rot) {
/*     */       
/* 276 */       this.rotation = rot;
/* 277 */       this.changed = true;
/*     */       
/* 279 */       PlayerTutorial.firePlayerTrigger(this.wurmid, PlayerTutorial.PlayerTrigger.MOVED_PLAYER_VIEW);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getZoneId() {
/* 290 */     return this.zoneId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZoneId(int zoneid) {
/* 301 */     if (zoneid != this.zoneId) {
/*     */       
/* 303 */       this.zoneId = zoneid;
/* 304 */       this.changed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayer() {
/* 315 */     return this.layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLayer(int layerId) {
/* 326 */     if (this.layer != layerId) {
/*     */       
/* 328 */       this.layer = layerId;
/* 329 */       this.changed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBridgeId() {
/* 340 */     return this.bridgeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBridgeId(long bridgeid) {
/* 351 */     if (this.bridgeId != bridgeid) {
/*     */ 
/*     */       
/* 354 */       this.bridgeId = bridgeid;
/* 355 */       this.changed = true;
/* 356 */       save(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmid() {
/* 367 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void save(boolean create) {
/* 372 */     if (this.changed) {
/*     */       
/* 374 */       this.changed = false;
/* 375 */       PreparedStatement ps = null;
/* 376 */       Connection dbcon = null;
/*     */       
/*     */       try {
/* 379 */         if (isPlayer()) {
/*     */           
/* 381 */           dbcon = DbConnector.getPlayerDbCon();
/*     */         } else {
/*     */           
/* 384 */           dbcon = DbConnector.getCreatureDbCon();
/* 385 */         }  if (create) {
/* 386 */           ps = dbcon.prepareStatement("insert into POSITION (POSX, POSY, POSZ, ROTATION,ZONEID,LAYER,ONBRIDGE, WURMID) values (?,?,?,?,?,?,?,?)");
/*     */         } else {
/* 388 */           ps = dbcon.prepareStatement("update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?");
/* 389 */         }  ps.setFloat(1, getPosX());
/* 390 */         ps.setFloat(2, getPosY());
/* 391 */         ps.setFloat(3, getPosZ());
/* 392 */         ps.setFloat(4, getRotation());
/* 393 */         ps.setInt(5, getZoneId());
/* 394 */         ps.setInt(6, getLayer());
/* 395 */         ps.setLong(7, getBridgeId());
/* 396 */         ps.setLong(8, getWurmid());
/* 397 */         ps.executeUpdate();
/*     */ 
/*     */       
/*     */       }
/* 401 */       catch (SQLException sqex) {
/*     */         
/* 403 */         logger.log(Level.WARNING, "Failed to update creaturePos for " + 
/* 404 */             getWurmid() + " " + sqex
/* 405 */             .getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 409 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 410 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearBatches() {
/*     */     try {
/* 419 */       if (cretPosPS != null) {
/*     */         
/* 421 */         int[] x = cretPosPS.executeBatch();
/* 422 */         logger.log(Level.INFO, "Creatures Position saved batch size " + x.length);
/*     */ 
/*     */         
/* 425 */         DbUtilities.closeDatabaseObjects(cretPosPS, null);
/* 426 */         cretPosPS = null;
/* 427 */         cretPosPSCount = 0;
/*     */       } 
/* 429 */       if (playPosPS != null)
/*     */       {
/* 431 */         int[] x = playPosPS.executeBatch();
/* 432 */         logger.log(Level.INFO, "Players Position saved batch size " + x.length);
/*     */ 
/*     */         
/* 435 */         DbUtilities.closeDatabaseObjects(playPosPS, null);
/* 436 */         playPosPS = null;
/* 437 */         playPosPSCount = 0;
/*     */       }
/*     */     
/* 440 */     } catch (Exception iox) {
/*     */       
/* 442 */       logger.log(Level.WARNING, iox.getMessage(), iox);
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
/*     */   protected final void savePlayerPosition(int zoneid, boolean immediately) throws SQLException {
/* 456 */     setZoneId(zoneid);
/* 457 */     if (System.currentTimeMillis() - this.lastSavedPos > 60000L || immediately)
/*     */     {
/* 459 */       if (this.changed) {
/*     */         
/* 461 */         if (Constants.useScheduledExecutorToUpdatePlayerPositionInDatabase) {
/*     */ 
/*     */           
/* 464 */           PlayerPositionDbUpdatable lUpdatable = new PlayerPositionDbUpdatable(getWurmid(), getPosX(), getPosY(), getPosZ(), getRotation(), getZoneId(), getLayer(), getBridgeId());
/* 465 */           playerDbPosUpdater.addToQueue(lUpdatable);
/* 466 */           totalPlayPosPSCount++;
/* 467 */           if (immediately) {
/* 468 */             playerDbPosUpdater.saveImmediately();
/*     */           }
/*     */         } else {
/*     */           
/* 472 */           if (playPosPS == null) {
/*     */             
/* 474 */             Connection dbcon = DbConnector.getPlayerDbCon();
/* 475 */             if (Server.getInstance().isPS()) {
/* 476 */               playPosPS = dbcon.prepareStatement("update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?");
/*     */             } else {
/* 478 */               playPosPS = dbcon.prepareStatement("INSERT INTO POSITION (POSX, POSY, POSZ, ROTATION, ZONEID, LAYER, ONBRIDGE, WURMID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE POSX=VALUES(POSX), POSY=VALUES(POSY), POSZ=VALUES(POSZ), ROTATION=VALUES(ROTATION), ZONEID=VALUES(ZONEID), LAYER=VALUES(LAYER), ONBRIDGE=VALUES(ONBRIDGE)");
/*     */             } 
/* 480 */           }  playPosPS.setFloat(1, getPosX());
/* 481 */           playPosPS.setFloat(2, getPosY());
/* 482 */           playPosPS.setFloat(3, getPosZ());
/* 483 */           setRotation(Creature.normalizeAngle(getRotation()));
/* 484 */           playPosPS.setFloat(4, getRotation());
/* 485 */           playPosPS.setInt(5, getZoneId());
/* 486 */           playPosPS.setInt(6, getLayer());
/* 487 */           playPosPS.setLong(7, getBridgeId());
/* 488 */           playPosPS.setLong(8, getWurmid());
/* 489 */           playPosPS.addBatch();
/* 490 */           playPosPSCount++;
/* 491 */           totalPlayPosPSCount++;
/* 492 */           if (playPosPSCount > Constants.numberOfDbPlayerPositionsToUpdateEachTime || immediately) {
/*     */             
/* 494 */             long checkms = System.nanoTime();
/* 495 */             playPosPS.executeBatch();
/* 496 */             DbUtilities.closeDatabaseObjects(playPosPS, null);
/* 497 */             playPosPS = null;
/* 498 */             float elapsedMilliseconds = (float)(System.nanoTime() - checkms) / 1000000.0F;
/* 499 */             if (elapsedMilliseconds > 300.0F || logger.isLoggable(Level.FINER)) {
/* 500 */               logger.log(Level.WARNING, "SavePlayerPos batch took " + elapsedMilliseconds + " ms for " + playPosPSCount + " updates.");
/*     */             }
/*     */             
/* 503 */             playPosPSCount = 0;
/*     */           } 
/*     */         } 
/* 506 */         this.changed = false;
/*     */         
/* 508 */         this.lastSavedPos = System.currentTimeMillis();
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
/*     */   protected void saveCreaturePosition(int zoneid, boolean immediately) throws SQLException {
/* 524 */     setZoneId(zoneid);
/* 525 */     if (System.currentTimeMillis() - this.lastSavedPos > 600000L || immediately)
/*     */     {
/* 527 */       if (this.changed) {
/*     */         
/* 529 */         if (Constants.useScheduledExecutorToUpdateCreaturePositionInDatabase && !immediately) {
/*     */ 
/*     */           
/* 532 */           CreaturePositionDbUpdatable lUpdatable = new CreaturePositionDbUpdatable(getWurmid(), getPosX(), getPosY(), getPosZ(), getRotation(), getZoneId(), getLayer(), getBridgeId());
/* 533 */           creatureDbPosUpdater.addToQueue(lUpdatable);
/* 534 */           totalCretPosPSCount++;
/* 535 */           this.lastSavedPos = System.currentTimeMillis();
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 542 */           if (cretPosPS == null) {
/*     */             
/* 544 */             Connection dbcon = DbConnector.getCreatureDbCon();
/* 545 */             if (Server.getInstance().isPS()) {
/* 546 */               cretPosPS = dbcon.prepareStatement("update POSITION set POSX=?, POSY=?, POSZ=?, ROTATION=?,ZONEID=?,LAYER=?,ONBRIDGE=? where WURMID=?");
/*     */             } else {
/* 548 */               cretPosPS = dbcon.prepareStatement("INSERT INTO POSITION (POSX, POSY, POSZ, ROTATION, ZONEID, LAYER, ONBRIDGE, WURMID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE POSX=VALUES(POSX), POSY=VALUES(POSY), POSZ=VALUES(POSZ), ROTATION=VALUES(ROTATION), ZONEID=VALUES(ZONEID), LAYER=VALUES(LAYER), ONBRIDGE=VALUES(ONBRIDGE)");
/*     */             } 
/* 550 */           }  cretPosPS.setFloat(1, getPosX());
/* 551 */           cretPosPS.setFloat(2, getPosY());
/* 552 */           cretPosPS.setFloat(3, getPosZ());
/* 553 */           setRotation(Creature.normalizeAngle(getRotation()));
/* 554 */           cretPosPS.setFloat(4, getRotation());
/* 555 */           cretPosPS.setInt(5, getZoneId());
/* 556 */           cretPosPS.setInt(6, getLayer());
/* 557 */           cretPosPS.setLong(7, getBridgeId());
/* 558 */           cretPosPS.setLong(8, getWurmid());
/* 559 */           cretPosPS.addBatch();
/* 560 */           cretPosPSCount++;
/* 561 */           totalCretPosPSCount++;
/* 562 */           if (cretPosPSCount > Constants.numberOfDbCreaturePositionsToUpdateEachTime || immediately) {
/*     */             
/* 564 */             cretPosPS.executeBatch();
/* 565 */             DbUtilities.closeDatabaseObjects(cretPosPS, null);
/* 566 */             cretPosPS = null;
/* 567 */             cretPosPSCount = 0;
/*     */           } 
/*     */         } 
/* 570 */         this.changed = false;
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
/*     */   public boolean isPlayer() {
/* 582 */     return this.isPlayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllPositions() {
/* 587 */     Connection dbcon = null;
/* 588 */     PreparedStatement ps = null;
/* 589 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 592 */       dbcon = DbConnector.getPlayerDbCon();
/* 593 */       ps = dbcon.prepareStatement("SELECT * FROM POSITION");
/* 594 */       rs = ps.executeQuery();
/* 595 */       while (rs.next())
/*     */       {
/* 597 */         new CreaturePos(rs.getLong("WURMID"), rs.getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs
/* 598 */             .getFloat("ROTATION"), rs.getInt("ZONEID"), rs.getInt("LAYER"), rs.getLong("ONBRIDGE"), false);
/*     */       }
/* 600 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 601 */       DbConnector.returnConnection(dbcon);
/*     */       
/* 603 */       dbcon = DbConnector.getCreatureDbCon();
/* 604 */       ps = dbcon.prepareStatement("SELECT * FROM POSITION");
/* 605 */       rs = ps.executeQuery();
/* 606 */       while (rs.next())
/*     */       {
/* 608 */         new CreaturePos(rs.getLong("WURMID"), rs.getFloat("POSX"), rs.getFloat("POSY"), rs.getFloat("POSZ"), rs
/* 609 */             .getFloat("ROTATION"), rs.getInt("ZONEID"), rs.getInt("LAYER"), rs.getLong("ONBRIDGE"), false);
/*     */       }
/*     */     }
/* 612 */     catch (Exception sqex) {
/*     */       
/* 614 */       logger.log(Level.WARNING, "Failed to load all positions", sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 618 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 619 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final CreaturePos getPosition(long wurmId) {
/* 625 */     return allPositions.get(Long.valueOf(wurmId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void delete(long wurmId) {
/* 630 */     Connection dbcon = null;
/* 631 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 634 */       boolean player = false;
/* 635 */       CreaturePos pos = getPosition(wurmId);
/* 636 */       if (pos != null) {
/*     */         
/* 638 */         player = pos.isPlayer();
/* 639 */         allPositions.remove(Long.valueOf(wurmId));
/*     */       } 
/* 641 */       if (player) {
/* 642 */         dbcon = DbConnector.getPlayerDbCon();
/*     */       } else {
/* 644 */         dbcon = DbConnector.getCreatureDbCon();
/* 645 */       }  ps = dbcon.prepareStatement("DELETE FROM POSITION WHERE WURMID=?");
/* 646 */       ps.setLong(1, wurmId);
/* 647 */       ps.executeUpdate();
/*     */     }
/* 649 */     catch (Exception sqex) {
/*     */       
/* 651 */       logger.log(Level.WARNING, "Failed to load all positions", sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 655 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 656 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CreaturePositionDatabaseUpdater getCreatureDbPosUpdater() {
/* 667 */     return creatureDbPosUpdater;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PlayerPositionDatabaseUpdater getPlayerDbPosUpdater() {
/* 677 */     return playerDbPosUpdater;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreaturePos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */