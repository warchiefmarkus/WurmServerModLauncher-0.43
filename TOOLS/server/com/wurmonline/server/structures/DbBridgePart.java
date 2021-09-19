/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.tutorial.MissionTargets;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.BridgeConstants;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ public class DbBridgePart
/*     */   extends BridgePart
/*     */ {
/*     */   private static final String CREATEBRIDGEPART = "INSERT INTO BRIDGEPARTS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL, DAMAGE, STRUCTURE, TILEX, TILEY, STATE, MATERIAL, HEIGHTOFFSET, DIR, SLOPE, STAGECOUNT, NORTHEXIT, EASTEXIT, SOUTHEXIT, WESTEXIT, LAYER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   private static final String UPDATEBRIDGEPART = "UPDATE BRIDGEPARTS SET TYPE=?,LASTMAINTAINED=?,CURRENTQL=?,ORIGINALQL=?,DAMAGE=?,STRUCTURE=?,STATE=?,MATERIAL=?,HEIGHTOFFSET=?,DIR=?,SLOPE=?,STAGECOUNT=?,NORTHEXIT=?,EASTEXIT=?,SOUTHEXIT=?,WESTEXIT=?,LAYER=? WHERE ID=?";
/*     */   private static final String GETBRIDGEPART = "SELECT * FROM BRIDGEPARTS WHERE ID=?";
/*     */   private static final String DELETEBRIDGEPART = "DELETE FROM BRIDGEPARTS WHERE ID=?";
/*     */   private static final String SETDAMAGE = "UPDATE BRIDGEPARTS SET DAMAGE=? WHERE ID=?";
/*     */   private static final String SETQUALITYLEVEL = "UPDATE BRIDGEPARTS SET CURRENTQL=? WHERE ID=?";
/*     */   private static final String SETSTATE = "UPDATE BRIDGEPARTS SET STATE=?,MATERIAL=? WHERE ID=?";
/*     */   private static final String SETLASTUSED = "UPDATE BRIDGEPARTS SET LASTMAINTAINED=? WHERE ID=?";
/*     */   private static final String SET_SETTINGS = "UPDATE BRIDGEPARTS SET SETTINGS=? WHERE ID=?";
/*     */   private static final String SETROADTYPE = "UPDATE BRIDGEPARTS SET ROADTYPE=? WHERE ID=?";
/*  58 */   private static final Logger logger = Logger.getLogger(DbWall.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFence() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWall() {
/*  79 */     return false;
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
/*     */   public DbBridgePart(int id, BridgeConstants.BridgeType floorType, int tilex, int tiley, byte aDbState, int heightOffset, float currentQl, long structureId, BridgeConstants.BridgeMaterial floorMaterial, float origQL, float dam, int materialCount, long lastmaintained, byte dir, byte slope, int aNorthExit, int aEastExit, int aSouthExit, int aWestExit, byte roadType, int layer) {
/* 111 */     super(id, floorType, tilex, tiley, aDbState, heightOffset, currentQl, structureId, floorMaterial, origQL, dam, materialCount, lastmaintained, dir, slope, aNorthExit, aEastExit, aSouthExit, aWestExit, roadType, layer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbBridgePart(BridgeConstants.BridgeType floorType, int tilex, int tiley, int heightOffset, float qualityLevel, long structure, BridgeConstants.BridgeMaterial material, byte dir, byte slope, int aNorthExit, int aEastExit, int aSouthExit, int aWestExit, byte roadType, int layer) {
/* 120 */     super(floorType, tilex, tiley, heightOffset, qualityLevel, structure, material, dir, slope, aNorthExit, aEastExit, aSouthExit, aWestExit, roadType, layer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setState(byte newState) {
/* 127 */     if (this.dbState != newState) {
/*     */       
/* 129 */       Connection dbcon = null;
/* 130 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 133 */         this.dbState = newState;
/* 134 */         dbcon = DbConnector.getZonesDbCon();
/* 135 */         ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET STATE=?,MATERIAL=? WHERE ID=?");
/* 136 */         ps.setByte(1, this.dbState);
/* 137 */         ps.setByte(2, getMaterial().getCode());
/* 138 */         ps.setInt(3, getNumber());
/* 139 */         ps.executeUpdate();
/*     */       
/*     */       }
/* 142 */       catch (SQLException sqx) {
/*     */         
/* 144 */         logger.log(Level.WARNING, "Failed to set state to " + newState + " for bridge part with id " + getNumber(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 148 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 149 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 157 */     Connection dbcon = null;
/* 158 */     PreparedStatement ps = null;
/* 159 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 162 */       dbcon = DbConnector.getZonesDbCon();
/* 163 */       if (exists(dbcon))
/*     */       {
/* 165 */         ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET TYPE=?,LASTMAINTAINED=?,CURRENTQL=?,ORIGINALQL=?,DAMAGE=?,STRUCTURE=?,STATE=?,MATERIAL=?,HEIGHTOFFSET=?,DIR=?,SLOPE=?,STAGECOUNT=?,NORTHEXIT=?,EASTEXIT=?,SOUTHEXIT=?,WESTEXIT=?,LAYER=? WHERE ID=?");
/* 166 */         ps.setByte(1, getType().getCode());
/* 167 */         ps.setLong(2, getLastUsed());
/* 168 */         ps.setFloat(3, getCurrentQL());
/* 169 */         ps.setFloat(4, getOriginalQL());
/* 170 */         ps.setFloat(5, getDamage());
/* 171 */         ps.setLong(6, getStructureId());
/* 172 */         ps.setByte(7, getState());
/* 173 */         ps.setByte(8, getMaterial().getCode());
/* 174 */         ps.setInt(9, getHeightOffset());
/* 175 */         ps.setByte(10, getDir());
/* 176 */         ps.setByte(11, getSlope());
/* 177 */         ps.setInt(12, getMaterialCount());
/* 178 */         ps.setInt(13, getNorthExit());
/* 179 */         ps.setInt(14, getEastExit());
/* 180 */         ps.setInt(15, getSouthExit());
/* 181 */         ps.setInt(16, getWestExit());
/* 182 */         ps.setInt(17, getLayer());
/* 183 */         ps.setInt(18, getNumber());
/*     */         
/* 185 */         ps.executeUpdate();
/*     */       }
/*     */       else
/*     */       {
/* 189 */         ps = dbcon.prepareStatement("INSERT INTO BRIDGEPARTS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL, DAMAGE, STRUCTURE, TILEX, TILEY, STATE, MATERIAL, HEIGHTOFFSET, DIR, SLOPE, STAGECOUNT, NORTHEXIT, EASTEXIT, SOUTHEXIT, WESTEXIT, LAYER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 190 */         ps.setByte(1, getType().getCode());
/* 191 */         ps.setLong(2, getLastUsed());
/* 192 */         ps.setFloat(3, getCurrentQL());
/* 193 */         ps.setFloat(4, getOriginalQL());
/* 194 */         ps.setFloat(5, getDamage());
/* 195 */         ps.setLong(6, getStructureId());
/* 196 */         ps.setInt(7, getTileX());
/* 197 */         ps.setInt(8, getTileY());
/* 198 */         ps.setByte(9, getState());
/* 199 */         ps.setByte(10, getMaterial().getCode());
/* 200 */         ps.setInt(11, getHeightOffset());
/* 201 */         ps.setByte(12, getDir());
/* 202 */         ps.setByte(13, getSlope());
/* 203 */         ps.setInt(14, getMaterialCount());
/* 204 */         ps.setInt(15, getNorthExit());
/* 205 */         ps.setInt(16, getEastExit());
/* 206 */         ps.setInt(17, getSouthExit());
/* 207 */         ps.setInt(18, getWestExit());
/* 208 */         ps.setInt(19, getLayer());
/* 209 */         ps.executeUpdate();
/*     */         
/* 211 */         rs = ps.getGeneratedKeys();
/* 212 */         if (rs.next()) {
/* 213 */           setNumber(rs.getInt(1));
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 220 */     catch (SQLException sqx) {
/*     */       
/* 222 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 226 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 227 */       DbConnector.returnConnection(dbcon);
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
/*     */   public boolean setDamage(float aDamage) {
/* 241 */     boolean forcePlan = false;
/* 242 */     if (isIndestructible())
/* 243 */       return false; 
/* 244 */     if (aDamage >= 100.0F) {
/*     */       
/* 246 */       VolaTile tile = getTile();
/* 247 */       forcePlan = true;
/* 248 */       BridgeConstants.BridgeState oldBridgeState = getBridgePartState();
/* 249 */       setBridgePartState(BridgeConstants.BridgeState.PLANNED);
/* 250 */       setQualityLevel(1.0F);
/* 251 */       saveRoadType((byte)0);
/* 252 */       if (tile != null) {
/*     */         
/* 254 */         tile.updateBridgePart(this);
/*     */ 
/*     */         
/* 257 */         if (oldBridgeState != BridgeConstants.BridgeState.PLANNED) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 263 */           BridgeConstants.BridgeType bType = getType();
/* 264 */           switch (getMaterial()) {
/*     */ 
/*     */             
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case POTTERY:
/*     */             case RENDERED:
/*     */             case ROUNDED_STONE:
/*     */             case SANDSTONE:
/*     */             case SLATE:
/* 274 */               if (bType.isSupportType()) {
/* 275 */                 damageAdjacent("abutment", 50); break;
/* 276 */               }  if (bType.isAbutment()) {
/* 277 */                 damageAdjacent("bracing", 25); break;
/* 278 */               }  if (bType.isBracing()) {
/*     */                 
/* 280 */                 damageAdjacent("crown", 10);
/* 281 */                 damageAdjacent("floating", 10);
/*     */               } 
/*     */               break;
/*     */ 
/*     */             
/*     */             case WOOD:
/* 287 */               if (bType.isSupportType()) {
/*     */                 
/* 289 */                 damageAdjacent("abutment", 50);
/* 290 */                 damageAdjacent("crown", 25); break;
/*     */               } 
/* 292 */               if (bType.isAbutment()) {
/* 293 */                 damageAdjacent("crown", 10);
/*     */               }
/*     */               break;
/*     */             
/*     */             case ROPE:
/* 298 */               if (bType.isAbutment())
/*     */               {
/* 300 */                 damageAdjacent("crown", 50);
/*     */               }
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/* 308 */     if (this.damage != aDamage) {
/*     */       
/* 310 */       boolean updateState = false;
/* 311 */       if ((this.damage >= 60.0F && aDamage < 60.0F) || (this.damage < 60.0F && aDamage >= 60.0F))
/*     */       {
/* 313 */         updateState = true;
/*     */       }
/* 315 */       this.damage = aDamage;
/* 316 */       if (forcePlan)
/* 317 */         this.damage = 0.0F; 
/* 318 */       if (this.damage < 100.0F) {
/*     */         
/* 320 */         Connection dbcon = null;
/* 321 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 324 */           dbcon = DbConnector.getZonesDbCon();
/* 325 */           ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET DAMAGE=? WHERE ID=?");
/* 326 */           ps.setFloat(1, getDamage());
/* 327 */           ps.setInt(2, getNumber());
/* 328 */           ps.executeUpdate();
/*     */         }
/* 330 */         catch (SQLException sqx) {
/*     */           
/* 332 */           logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */         }
/*     */         finally {
/*     */           
/* 336 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 337 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */         
/* 340 */         if (updateState)
/*     */         {
/* 342 */           VolaTile tile = getTile();
/* 343 */           if (tile != null)
/*     */           {
/* 345 */             getTile().updateBridgePartDamageState(this);
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 351 */         VolaTile t = getTile();
/* 352 */         if (t != null) {
/* 353 */           t.removeBridgePart(this);
/*     */         }
/* 355 */         delete();
/*     */       } 
/*     */     } 
/* 358 */     return (this.damage >= 100.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void damageAdjacent(String typeName, int addDamage) {
/* 365 */     VolaTile vtNorth = Zones.getTileOrNull(getTileX(), getTileY() - 1, isOnSurface());
/* 366 */     if (vtNorth != null) {
/*     */       
/* 368 */       Structure structNorth = vtNorth.getStructure();
/* 369 */       if (structNorth != null && structNorth.getWurmId() == getStructureId()) {
/*     */         
/* 371 */         BridgePart[] bps = vtNorth.getBridgeParts();
/* 372 */         if (bps.length == 1 && bps[0].getType().getName().equalsIgnoreCase(typeName)) {
/* 373 */           bps[0].setDamage(bps[0].getDamage() + addDamage);
/*     */         }
/*     */       } 
/*     */     } 
/* 377 */     VolaTile vtEast = Zones.getTileOrNull(getTileX() + 1, getTileY(), isOnSurface());
/* 378 */     if (vtEast != null) {
/*     */       
/* 380 */       Structure structEast = vtEast.getStructure();
/* 381 */       if (structEast != null && structEast.getWurmId() == getStructureId()) {
/*     */         
/* 383 */         BridgePart[] bps = vtEast.getBridgeParts();
/* 384 */         if (bps.length == 1 && bps[0].getType().getName().equalsIgnoreCase(typeName)) {
/* 385 */           bps[0].setDamage(bps[0].getDamage() + addDamage);
/*     */         }
/*     */       } 
/*     */     } 
/* 389 */     VolaTile vtSouth = Zones.getTileOrNull(getTileX(), getTileY() + 1, isOnSurface());
/* 390 */     if (vtSouth != null) {
/*     */       
/* 392 */       Structure structSouth = vtSouth.getStructure();
/* 393 */       if (structSouth != null && structSouth.getWurmId() == getStructureId()) {
/*     */         
/* 395 */         BridgePart[] bps = vtSouth.getBridgeParts();
/* 396 */         if (bps.length == 1 && bps[0].getType().getName().equalsIgnoreCase(typeName)) {
/* 397 */           bps[0].setDamage(bps[0].getDamage() + addDamage);
/*     */         }
/*     */       } 
/*     */     } 
/* 401 */     VolaTile vtWest = Zones.getTileOrNull(getTileX() - 1, getTileY(), isOnSurface());
/* 402 */     if (vtWest != null) {
/*     */       
/* 404 */       Structure structWest = vtWest.getStructure();
/* 405 */       if (structWest != null && structWest.getWurmId() == getStructureId()) {
/*     */         
/* 407 */         BridgePart[] bps = vtWest.getBridgeParts();
/* 408 */         if (bps.length == 1 && bps[0].getType().getName().equalsIgnoreCase(typeName)) {
/* 409 */           bps[0].setDamage(bps[0].getDamage() + addDamage);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastUsed(long now) {
/* 417 */     if (this.lastUsed != now) {
/*     */       
/* 419 */       this.lastUsed = now;
/* 420 */       Connection dbcon = null;
/* 421 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 424 */         dbcon = DbConnector.getZonesDbCon();
/* 425 */         ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET LASTMAINTAINED=? WHERE ID=?");
/* 426 */         ps.setLong(1, this.lastUsed);
/* 427 */         ps.setInt(2, getNumber());
/* 428 */         ps.executeUpdate();
/*     */       }
/* 430 */       catch (SQLException sqx) {
/*     */         
/* 432 */         logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 436 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 437 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setQualityLevel(float ql) {
/* 445 */     if (ql > 100.0F)
/* 446 */       ql = 100.0F; 
/* 447 */     if (this.currentQL != ql) {
/*     */       
/* 449 */       Connection dbcon = null;
/* 450 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 453 */         this.currentQL = ql;
/* 454 */         dbcon = DbConnector.getZonesDbCon();
/* 455 */         ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET CURRENTQL=? WHERE ID=?");
/* 456 */         ps.setFloat(1, this.currentQL);
/* 457 */         ps.setInt(2, getNumber());
/* 458 */         ps.executeUpdate();
/*     */       }
/* 460 */       catch (SQLException sqx) {
/*     */         
/* 462 */         logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 466 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 467 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 470 */     return (ql >= 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 475 */     PreparedStatement ps = null;
/* 476 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 479 */       ps = dbcon.prepareStatement("SELECT * FROM BRIDGEPARTS WHERE ID=?");
/* 480 */       ps.setInt(1, getNumber());
/* 481 */       rs = ps.executeQuery();
/* 482 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 486 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 493 */     MissionTargets.destroyMissionTarget(getId(), true);
/* 494 */     MethodsHighways.removeNearbyMarkers(this);
/*     */     
/* 496 */     Connection dbcon = null;
/* 497 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 500 */       dbcon = DbConnector.getZonesDbCon();
/* 501 */       ps = dbcon.prepareStatement("DELETE FROM BRIDGEPARTS WHERE ID=?");
/* 502 */       ps.setInt(1, getNumber());
/* 503 */       ps.executeUpdate();
/*     */     }
/* 505 */     catch (SQLException sqx) {
/*     */       
/* 507 */       logger.log(Level.WARNING, "Failed to delete bridge part with id " + getNumber(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 511 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 512 */       DbConnector.returnConnection(dbcon);
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
/*     */   public long getTempId() {
/* 524 */     return -10L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void savePermissions() {
/* 530 */     Connection dbcon = null;
/* 531 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 534 */       dbcon = DbConnector.getZonesDbCon();
/* 535 */       ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET SETTINGS=? WHERE ID=?");
/* 536 */       ps.setLong(1, this.permissions.getPermissions());
/* 537 */       ps.setLong(2, getNumber());
/* 538 */       ps.executeUpdate();
/*     */     }
/* 540 */     catch (SQLException sqx) {
/*     */       
/* 542 */       logger.log(Level.WARNING, "Failed to save settings for bridge part with id " + getNumber(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 546 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 547 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveRoadType(byte roadType) {
/* 554 */     if (this.roadType != roadType) {
/*     */       
/* 556 */       this.roadType = roadType;
/* 557 */       Connection dbcon = null;
/* 558 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 561 */         dbcon = DbConnector.getZonesDbCon();
/* 562 */         ps = dbcon.prepareStatement("UPDATE BRIDGEPARTS SET ROADTYPE=? WHERE ID=?");
/* 563 */         ps.setByte(1, this.roadType);
/* 564 */         ps.setLong(2, getNumber());
/* 565 */         ps.executeUpdate();
/*     */       }
/* 567 */       catch (SQLException sqx) {
/*     */         
/* 569 */         logger.log(Level.WARNING, "Failed to save roadtype for bridge part with id " + getNumber(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 573 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 574 */         DbConnector.returnConnection(dbcon);
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
/*     */   public final boolean isOnSouthBorder(TilePos pos) {
/* 588 */     return false;
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
/*     */   public final boolean isOnNorthBorder(TilePos pos) {
/* 600 */     return false;
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
/*     */   public final boolean isOnWestBorder(TilePos pos) {
/* 612 */     return false;
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
/*     */   public final boolean isOnEastBorder(TilePos pos) {
/* 624 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbBridgePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */