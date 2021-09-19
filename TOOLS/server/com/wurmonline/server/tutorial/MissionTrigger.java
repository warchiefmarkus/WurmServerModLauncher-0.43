/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.behaviours.Actions;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.Spawnpoint;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
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
/*     */ public final class MissionTrigger
/*     */   implements MiscConstants, Comparable<MissionTrigger>, CounterTypes
/*     */ {
/*  56 */   private static Logger logger = Logger.getLogger(MissionTrigger.class.getName());
/*     */   
/*     */   private static final String UPDATE_TRIGGER = "UPDATE MISSIONTRIGGERS SET NAME=?,DESCRIPTION=?,ONITEMCREATED=?,ONACTIONPERFORMED=?,ONTARGET=?,MISSION_REQ=?,MISSION_STATE_REQ=?,MISSION_STATE_END=?,SECONDS=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,SPAWNPOINT=? WHERE ID=?";
/*     */   
/*     */   private static final String CREATE_TRIGGER = "INSERT INTO MISSIONTRIGGERS (NAME,DESCRIPTION,ONITEMCREATED,ONACTIONPERFORMED,ONTARGET,MISSION_REQ,MISSION_STATE_REQ,MISSION_STATE_END,SECONDS,INACTIVE,CREATOR,CREATORID,CREATORTYPE,CREATEDDATE,LASTMODIFIER,SPAWNPOINT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String DELETE_TRIGGER = "DELETE FROM MISSIONTRIGGERS WHERE ID=?";
/*     */   
/*  64 */   private int id = 0;
/*     */   
/*     */   private String name;
/*     */   
/*     */   private String description;
/*     */   
/*     */   private int onItemUsedId;
/*     */   
/*     */   private int onActionPerformed;
/*     */   
/*     */   private long onActionTargetId;
/*     */   
/*  76 */   private int missionRequired = 0;
/*     */   
/*  78 */   private float stateRequired = 100.0F;
/*     */   
/*  80 */   private float stateEnd = 0.0F;
/*     */   
/*  82 */   private int seconds = 0;
/*     */   
/*     */   private boolean inActive = false;
/*     */   
/*     */   private String creatorName;
/*     */   
/*     */   private String createdDate;
/*     */   
/*     */   private String lastModifierName;
/*     */   
/*     */   private Timestamp lastModifiedDate;
/*     */   
/*  94 */   private long ownerId = 0L;
/*     */   
/*  96 */   private byte creatorType = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean spawnPoint = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOnActionPerformed() {
/* 109 */     return this.onActionPerformed;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 114 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCreatedDate(String aCreatedDate) {
/* 123 */     this.createdDate = aCreatedDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreatedDate() {
/* 128 */     return this.createdDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setLastModifiedDate(Timestamp aLastModifiedDate) {
/* 137 */     this.lastModifiedDate = aLastModifiedDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastModifiedDate() {
/* 142 */     return DateFormat.getDateInstance(2).format(this.lastModifiedDate);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 147 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDescription(String n) {
/* 152 */     this.description = n;
/* 153 */     this.description = this.description.substring(0, Math.min(this.description.length(), 100));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String n) {
/* 158 */     this.name = n;
/* 159 */     this.name = this.name.substring(0, Math.min(this.name.length(), 40));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTarget() {
/* 164 */     return this.onActionTargetId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemUsedId() {
/* 169 */     return this.onItemUsedId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMissionRequired() {
/* 174 */     return this.missionRequired;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreatorType(byte aCreatorType) {
/* 179 */     this.creatorType = aCreatorType;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getCreatorType() {
/* 184 */     return this.creatorType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwnerId(long aWurmId) {
/* 189 */     this.ownerId = aWurmId;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getOwnerId() {
/* 194 */     return this.ownerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setId(int aId) {
/* 203 */     this.id = aId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 208 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStateRequired() {
/* 213 */     return this.stateRequired;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStateEnd() {
/* 218 */     return this.stateEnd;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStateRange() {
/* 223 */     if (getStateEnd() > getStateRequired())
/* 224 */       return getStateRequired() + " to " + getStateEnd(); 
/* 225 */     return Float.toString(getStateRequired());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTriggered(float missionState, boolean checkActive) {
/* 230 */     boolean stateOk = false;
/* 231 */     if (getStateEnd() != 0.0F && getStateEnd() > getStateRequired()) {
/*     */ 
/*     */       
/* 234 */       if (missionState >= getStateRequired() && missionState <= getStateEnd()) {
/* 235 */         stateOk = true;
/*     */       }
/* 237 */     } else if (getStateRequired() == missionState) {
/*     */       
/* 239 */       stateOk = true;
/*     */     } 
/* 241 */     if (stateOk) {
/*     */       
/* 243 */       if (!checkActive)
/* 244 */         return true; 
/* 245 */       if (!isInactive())
/* 246 */         return true; 
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeconds() {
/* 253 */     return this.seconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeconds(int secs) {
/* 258 */     this.seconds = secs;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInactive() {
/* 263 */     return this.inActive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpawnPoint() {
/* 268 */     return this.spawnPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnTargetId(long t) {
/* 273 */     this.onActionTargetId = t;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnItemUsedId(int n) {
/* 278 */     this.onItemUsedId = n;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnActionPerformed(int a) {
/* 283 */     this.onActionPerformed = a;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMissionRequirement(int rq) {
/* 288 */     this.missionRequired = rq;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStateRequirement(float sq) {
/* 293 */     this.stateRequired = sq;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStateEnd(float sq) {
/* 298 */     this.stateEnd = sq;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInactive(boolean inactive) {
/* 303 */     this.inActive = inactive;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsSpawnpoint(boolean isSpawnPoint) {
/* 308 */     this.spawnPoint = isSpawnPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreatorName() {
/* 313 */     return this.creatorName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastModifierName() {
/* 318 */     return this.lastModifierName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreatorName(String n) {
/* 323 */     this.creatorName = n;
/* 324 */     this.creatorName = this.creatorName.substring(0, Math.min(this.creatorName.length(), 40));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastModifierName(String n) {
/* 329 */     this.lastModifierName = n;
/* 330 */     this.lastModifierName = this.lastModifierName.substring(0, Math.min(this.lastModifierName.length(), 40));
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 335 */     Connection dbcon = null;
/* 336 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 339 */       dbcon = DbConnector.getPlayerDbCon();
/* 340 */       ps = dbcon.prepareStatement("UPDATE MISSIONTRIGGERS SET NAME=?,DESCRIPTION=?,ONITEMCREATED=?,ONACTIONPERFORMED=?,ONTARGET=?,MISSION_REQ=?,MISSION_STATE_REQ=?,MISSION_STATE_END=?,SECONDS=?,INACTIVE=?,CREATOR=?,CREATEDDATE=?,LASTMODIFIER=?,SPAWNPOINT=? WHERE ID=?");
/* 341 */       ps.setString(1, this.name);
/* 342 */       ps.setString(2, this.description);
/* 343 */       ps.setInt(3, this.onItemUsedId);
/* 344 */       ps.setInt(4, this.onActionPerformed);
/* 345 */       ps.setLong(5, this.onActionTargetId);
/* 346 */       ps.setInt(6, this.missionRequired);
/* 347 */       ps.setFloat(7, this.stateRequired);
/* 348 */       ps.setFloat(8, this.stateEnd);
/* 349 */       ps.setInt(9, this.seconds);
/* 350 */       ps.setBoolean(10, this.inActive);
/* 351 */       ps.setString(11, this.creatorName);
/* 352 */       ps.setString(12, this.createdDate);
/* 353 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 354 */       ps.setString(13, this.lastModifierName);
/* 355 */       ps.setBoolean(14, this.spawnPoint);
/* 356 */       ps.setInt(15, this.id);
/* 357 */       ps.executeUpdate();
/*     */     }
/* 359 */     catch (SQLException sqx) {
/*     */       
/* 361 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 365 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 366 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void create() {
/* 372 */     Connection dbcon = null;
/* 373 */     PreparedStatement ps = null;
/* 374 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 377 */       dbcon = DbConnector.getPlayerDbCon();
/* 378 */       ps = dbcon.prepareStatement("INSERT INTO MISSIONTRIGGERS (NAME,DESCRIPTION,ONITEMCREATED,ONACTIONPERFORMED,ONTARGET,MISSION_REQ,MISSION_STATE_REQ,MISSION_STATE_END,SECONDS,INACTIVE,CREATOR,CREATORID,CREATORTYPE,CREATEDDATE,LASTMODIFIER,SPAWNPOINT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 379 */       ps.setString(1, this.name);
/* 380 */       ps.setString(2, this.description);
/* 381 */       ps.setInt(3, this.onItemUsedId);
/* 382 */       ps.setInt(4, this.onActionPerformed);
/* 383 */       ps.setLong(5, this.onActionTargetId);
/* 384 */       ps.setInt(6, this.missionRequired);
/* 385 */       ps.setFloat(7, this.stateRequired);
/* 386 */       ps.setFloat(8, this.stateEnd);
/* 387 */       ps.setInt(9, this.seconds);
/* 388 */       ps.setBoolean(10, this.inActive);
/* 389 */       ps.setString(11, this.creatorName);
/* 390 */       ps.setLong(12, this.ownerId);
/* 391 */       ps.setByte(13, this.creatorType);
/* 392 */       this.createdDate = DateFormat.getDateInstance(2).format(new Timestamp(System.currentTimeMillis()));
/* 393 */       this.lastModifiedDate = new Timestamp(System.currentTimeMillis());
/* 394 */       ps.setString(14, this.createdDate);
/* 395 */       ps.setString(15, this.lastModifierName);
/* 396 */       ps.setBoolean(16, this.spawnPoint);
/* 397 */       ps.executeUpdate();
/* 398 */       rs = ps.getGeneratedKeys();
/* 399 */       if (rs.next())
/* 400 */         this.id = rs.getInt(1); 
/* 401 */       logger.log(Level.INFO, "Mission trigger " + this.name + " (" + this.id + ") created at " + this.createdDate);
/*     */     }
/* 403 */     catch (SQLException sqx) {
/*     */       
/* 405 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 409 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 410 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 416 */     MissionTriggers.removeTrigger(this.id);
/* 417 */     Triggers2Effects.deleteTrigger(this.id);
/* 418 */     Connection dbcon = null;
/* 419 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 422 */       dbcon = DbConnector.getPlayerDbCon();
/* 423 */       ps = dbcon.prepareStatement("DELETE FROM MISSIONTRIGGERS WHERE ID=?");
/* 424 */       ps.setInt(1, this.id);
/* 425 */       ps.executeUpdate();
/*     */     }
/* 427 */     catch (SQLException sqx) {
/*     */       
/* 429 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 433 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 434 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Spawnpoint getSpawnPoint() {
/* 440 */     Spawnpoint toReturn = null;
/* 441 */     long targetId = getTarget();
/* 442 */     if (WurmId.getType(targetId) == 1) {
/*     */       
/*     */       try {
/*     */         
/* 446 */         Creature c = Creatures.getInstance().getCreature(targetId);
/*     */         
/* 448 */         toReturn = new Spawnpoint((byte)1, c.getName(), (short)c.getTileX(), (short)c.getTileY(), c.isOnSurface(), c.getKingdomId());
/*     */       }
/* 450 */       catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */     }
/*     */ 
/*     */     
/* 454 */     if (WurmId.getType(targetId) == 0) {
/*     */       
/*     */       try {
/*     */         
/* 458 */         Player p = Players.getInstance().getPlayer(targetId);
/*     */         
/* 460 */         toReturn = new Spawnpoint((byte)1, p.getName(), (short)p.getTileX(), (short)p.getTileY(), p.isOnSurface(), p.getKingdomId());
/*     */       }
/* 462 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */     }
/*     */ 
/*     */     
/* 466 */     if (WurmId.getType(targetId) == 5) {
/*     */       
/* 468 */       int x = (int)(targetId >> 32L) & 0xFFFF;
/* 469 */       int y = (int)(targetId >> 16L) & 0xFFFF;
/*     */       
/* 471 */       Wall wall = Wall.getWall(targetId);
/* 472 */       if (wall != null)
/*     */       {
/* 474 */         toReturn = new Spawnpoint((byte)1, wall.getName(), (short)x, (short)y, true, (byte)0);
/*     */       }
/*     */     } 
/* 477 */     if (WurmId.getType(targetId) == 2 || WurmId.getType(targetId) == 6 || 
/* 478 */       WurmId.getType(targetId) == 19 || WurmId.getType(targetId) == 20) {
/*     */ 
/*     */       
/*     */       try {
/* 482 */         Item targetItem = Items.getItem(targetId);
/*     */         
/* 484 */         toReturn = new Spawnpoint((byte)1, targetItem.getName(), (short)targetItem.getTileX(), (short)targetItem.getTileY(), targetItem.isOnSurface(), (byte)0);
/*     */       }
/* 486 */       catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */     
/*     */     }
/* 490 */     else if (WurmId.getType(targetId) == 7) {
/*     */ 
/*     */       
/* 493 */       int x = (int)(targetId >> 32L) & 0xFFFF;
/* 494 */       int y = (int)(targetId >> 16L) & 0xFFFF;
/* 495 */       Fence fence = Fence.getFence(targetId);
/* 496 */       if (fence != null)
/*     */       {
/* 498 */         toReturn = new Spawnpoint((byte)1, fence.getName(), (short)x, (short)y, true, (byte)0);
/*     */       }
/*     */     }
/* 501 */     else if (WurmId.getType(targetId) == 3) {
/*     */ 
/*     */       
/* 504 */       int x = (int)(targetId >> 32L) & 0xFFFF;
/* 505 */       int y = (int)(targetId >> 16L) & 0xFFFF;
/* 506 */       if (x > Zones.worldTileSizeX) {
/*     */         
/* 508 */         int oldx = x;
/* 509 */         x = (int)(targetId >> 40L) & 0xFFFFFF;
/* 510 */         int heightOffset = (int)(targetId >> 48L) & 0xFFFF;
/* 511 */         long newTarg = Tiles.getTileId(x, y, heightOffset);
/* 512 */         setOnTargetId(newTarg);
/* 513 */         update();
/* 514 */         logger.log(Level.INFO, "Updated mission trigger " + getName() + " to " + x + "," + y + " from " + oldx + "," + y);
/*     */       } 
/*     */ 
/*     */       
/* 518 */       int tile = Server.surfaceMesh.getTile(x, y);
/*     */       
/* 520 */       byte type = Tiles.decodeType(tile);
/* 521 */       Tiles.Tile t = Tiles.getTile(type);
/* 522 */       toReturn = new Spawnpoint((byte)1, t.tiledesc, (short)x, (short)y, true, (byte)0);
/*     */     }
/* 524 */     else if (WurmId.getType(targetId) == 17) {
/*     */ 
/*     */       
/* 527 */       int x = (int)(targetId >> 32L) & 0xFFFF;
/* 528 */       int y = (int)(targetId >> 16L) & 0xFFFF;
/* 529 */       int tile = Server.caveMesh.getTile(x, y);
/*     */       
/* 531 */       byte type = Tiles.decodeType(tile);
/* 532 */       Tiles.Tile t = Tiles.getTile(type);
/* 533 */       toReturn = new Spawnpoint((byte)1, t.tiledesc, (short)x, (short)y, false, (byte)0);
/*     */     } 
/* 535 */     return toReturn;
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
/*     */   public int compareTo(MissionTrigger aOtherMissionTrigger) {
/* 552 */     return this.name.compareTo(aOtherMissionTrigger.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTargetOf(long currentTargetId, Creature performer) {
/* 563 */     return (this.onActionTargetId == currentTargetId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActionString() {
/* 573 */     return Actions.getActionString((short)this.onActionPerformed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetAsString(Creature creature) {
/* 583 */     return MissionTriggers.getTargetAsString(creature, this.onActionTargetId);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\MissionTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */