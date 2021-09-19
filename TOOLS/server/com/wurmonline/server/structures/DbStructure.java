/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DbStructure
/*     */   extends Structure
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(DbStructure.class.getName());
/*     */ 
/*     */   
/*     */   private static final String GET_STRUCTURE = "SELECT * FROM STRUCTURES WHERE WURMID=?";
/*     */ 
/*     */   
/*     */   private static final String SAVE_STRUCTURE = "UPDATE STRUCTURES SET CENTERX=?,CENTERY=?,ROOF=?,SURFACED=?,NAME=?,FINISHED=?,WRITID=?,FINFINISHED=?,ALLOWSVILLAGERS=?,ALLOWSALLIES=?,ALLOWSKINGDOM=?,PLANNER=?,OWNERID=?,SETTINGS=?,VILLAGE=? WHERE WURMID=?";
/*     */ 
/*     */   
/*     */   private static final String CREATE_STRUCTURE = "INSERT INTO STRUCTURES(WURMID, STRUCTURETYPE) VALUES(?,?)";
/*     */ 
/*     */   
/*     */   private static final String DELETE_STRUCTURE = "DELETE FROM STRUCTURES WHERE WURMID=?";
/*     */   
/*     */   private static final String ADD_BUILDTILE = "INSERT INTO BUILDTILES(STRUCTUREID,TILEX,TILEY,LAYER) VALUES (?,?,?,?)";
/*     */   
/*     */   private static final String DELETE_BUILDTILE = "DELETE FROM BUILDTILES WHERE STRUCTUREID=? AND TILEX=? AND TILEY=? AND LAYER=?";
/*     */   
/*     */   private static final String DELETE_ALLBUILDTILES = "DELETE FROM BUILDTILES WHERE STRUCTUREID=?";
/*     */   
/*     */   private static final String LOAD_ALLBUILDTILES = "SELECT * FROM BUILDTILES";
/*     */   
/*     */   private static final String SET_FINISHED = "UPDATE STRUCTURES SET FINISHED=? WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_FIN_FINISHED = "UPDATE STRUCTURES SET FINFINISHED=? WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_WRITID = "UPDATE STRUCTURES SET WRITID=? WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_OWNERID = "UPDATE STRUCTURES SET OWNERID=? WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_SETTINGS = "UPDATE STRUCTURES SET SETTINGS=?,VILLAGE=? WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_NAME = "UPDATE STRUCTURES SET NAME=? WHERE WURMID=?";
/*     */ 
/*     */   
/*     */   DbStructure(byte theStructureType, String aName, long id, int x, int y, boolean isSurfaced) {
/*  84 */     super(theStructureType, aName, id, x, y, isSurfaced);
/*     */   }
/*     */ 
/*     */   
/*     */   DbStructure(long id) throws IOException, NoSuchStructureException {
/*  89 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DbStructure(byte theStructureType, String aName, long aId, boolean aIsSurfaced, byte aRoof, boolean aFinished, boolean aFinFinished, long aWritId, String aPlanner, long aOwnerId, int aSettings, int aVillageId, boolean aAllowsVillagers, boolean aAllowsAllies, boolean aAllowKingdom) {
/*  97 */     super(theStructureType, aName, aId, aIsSurfaced, aRoof, aFinished, aFinFinished, aWritId, aPlanner, aOwnerId, aSettings, aVillageId, aAllowsVillagers, aAllowsAllies, aAllowKingdom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException, NoSuchStructureException {
/* 104 */     if (!isLoading()) {
/*     */       
/* 106 */       Connection dbcon = null;
/* 107 */       PreparedStatement ps = null;
/* 108 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 111 */         setLoading(true);
/* 112 */         dbcon = DbConnector.getZonesDbCon();
/* 113 */         ps = dbcon.prepareStatement("SELECT * FROM STRUCTURES WHERE WURMID=?");
/* 114 */         ps.setLong(1, getWurmId());
/* 115 */         rs = ps.executeQuery();
/* 116 */         if (rs.next()) {
/*     */           
/* 118 */           setStructureType(rs.getByte("STRUCTURETYPE"));
/* 119 */           setSurfaced(rs.getBoolean("SURFACED"));
/*     */           
/* 121 */           setRoof(rs.getByte("ROOF"));
/*     */           
/* 123 */           String lName = rs.getString("NAME");
/* 124 */           if (lName == null)
/* 125 */             lName = "Unknown structure"; 
/* 126 */           if (lName.length() >= 50)
/* 127 */             lName = lName.substring(0, 49); 
/* 128 */           setName(lName, false);
/*     */           
/* 130 */           this.finished = rs.getBoolean("FINISHED");
/* 131 */           this.finalfinished = rs.getBoolean("FINFINISHED");
/* 132 */           this.allowsVillagers = rs.getBoolean("ALLOWSVILLAGERS");
/* 133 */           this.allowsAllies = rs.getBoolean("ALLOWSALLIES");
/* 134 */           this.allowsKingdom = rs.getBoolean("ALLOWSKINGDOM");
/* 135 */           setPlanner(rs.getString("PLANNER"));
/* 136 */           setOwnerId(rs.getLong("OWNERID"));
/* 137 */           setSettings(rs.getInt("SETTINGS"));
/* 138 */           this.villageId = rs.getInt("VILLAGE");
/* 139 */           if (isTypeHouse()) {
/*     */             try
/*     */             {
/*     */               
/* 143 */               setWritid(rs.getLong("WRITID"), false);
/*     */             }
/* 145 */             catch (SQLException nsi)
/*     */             {
/* 147 */               logger.log(Level.INFO, "No writ for house with id:" + getWurmId() + " creating new after loading.", nsi);
/*     */             }
/*     */           
/*     */           }
/*     */         } else {
/*     */           
/* 153 */           throw new NoSuchStructureException("No structure found with id " + getWurmId());
/*     */         }
/*     */       
/* 156 */       } catch (SQLException sqex) {
/*     */         
/* 158 */         throw new IOException(sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 162 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 163 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 171 */     Connection dbcon = null;
/* 172 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 175 */       dbcon = DbConnector.getZonesDbCon();
/* 176 */       if (!exists(dbcon))
/* 177 */         create(dbcon); 
/* 178 */       ps = dbcon.prepareStatement("UPDATE STRUCTURES SET CENTERX=?,CENTERY=?,ROOF=?,SURFACED=?,NAME=?,FINISHED=?,WRITID=?,FINFINISHED=?,ALLOWSVILLAGERS=?,ALLOWSALLIES=?,ALLOWSKINGDOM=?,PLANNER=?,OWNERID=?,SETTINGS=?,VILLAGE=? WHERE WURMID=?");
/*     */       
/* 180 */       ps.setInt(1, getCenterX());
/* 181 */       ps.setInt(2, getCenterY());
/* 182 */       for (Iterator<VolaTile> it = this.structureTiles.iterator(); it.hasNext(); ) {
/*     */         
/* 184 */         VolaTile t = it.next();
/* 185 */         Wall[] wallArr = t.getWalls();
/* 186 */         for (int x = 0; x < wallArr.length; x++) {
/*     */ 
/*     */           
/*     */           try {
/* 190 */             wallArr[x].save();
/*     */           }
/* 192 */           catch (IOException iox) {
/*     */             
/* 194 */             logger.log(Level.WARNING, "Failed to save wall: " + wallArr[x]);
/*     */           } 
/*     */         } 
/* 197 */         Floor[] floorArr = t.getFloors();
/* 198 */         for (int i = 0; i < floorArr.length; i++) {
/*     */ 
/*     */           
/*     */           try {
/* 202 */             floorArr[i].save();
/*     */           }
/* 204 */           catch (IOException iox) {
/*     */             
/* 206 */             logger.log(Level.WARNING, "Failed to save floor: " + floorArr[i]);
/*     */           } 
/*     */         } 
/* 209 */         BridgePart[] partsArr = t.getBridgeParts();
/* 210 */         for (int j = 0; j < partsArr.length; j++) {
/*     */ 
/*     */           
/*     */           try {
/* 214 */             partsArr[j].save();
/*     */           }
/* 216 */           catch (IOException iox) {
/*     */             
/* 218 */             logger.log(Level.WARNING, "Failed to save bridge part: " + partsArr[j]);
/*     */           } 
/*     */         } 
/*     */       } 
/* 222 */       ps.setByte(3, getRoof());
/* 223 */       ps.setBoolean(4, isSurfaced());
/* 224 */       ps.setString(5, getName());
/* 225 */       ps.setBoolean(6, isFinished());
/* 226 */       ps.setLong(7, getWritId());
/* 227 */       ps.setBoolean(8, isFinalFinished());
/* 228 */       ps.setBoolean(9, allowsCitizens());
/* 229 */       ps.setBoolean(10, allowsAllies());
/* 230 */       ps.setBoolean(11, allowsKingdom());
/* 231 */       ps.setString(12, getPlanner());
/* 232 */       ps.setLong(13, getOwnerId());
/* 233 */       ps.setInt(14, getSettings().getPermissions());
/* 234 */       ps.setInt(15, getVillageId());
/* 235 */       ps.setLong(16, getWurmId());
/* 236 */       ps.executeUpdate();
/*     */     }
/* 238 */     catch (SQLException sqex) {
/*     */       
/* 240 */       logger.log(Level.WARNING, "Problem", sqex);
/* 241 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 245 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 246 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void create(Connection dbcon) throws IOException {
/* 252 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 255 */       ps = dbcon.prepareStatement("INSERT INTO STRUCTURES(WURMID, STRUCTURETYPE) VALUES(?,?)");
/* 256 */       ps.setLong(1, getWurmId());
/* 257 */       ps.setByte(2, getStructureType());
/* 258 */       ps.executeUpdate();
/*     */     }
/* 260 */     catch (SQLException sqex) {
/*     */       
/* 262 */       logger.log(Level.WARNING, "Problem", sqex);
/* 263 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 267 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 273 */     PreparedStatement ps = null;
/* 274 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 277 */       ps = dbcon.prepareStatement("SELECT * FROM STRUCTURES WHERE WURMID=?");
/* 278 */       ps.setLong(1, getWurmId());
/* 279 */       rs = ps.executeQuery();
/* 280 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 284 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void delete() {
/* 291 */     Connection dbcon = null;
/* 292 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 295 */       dbcon = DbConnector.getZonesDbCon();
/* 296 */       ps = dbcon.prepareStatement("DELETE FROM STRUCTURES WHERE WURMID=?");
/* 297 */       ps.setLong(1, getWurmId());
/* 298 */       ps.executeUpdate();
/*     */     }
/* 300 */     catch (SQLException sqx) {
/*     */       
/* 302 */       logger.log(Level.WARNING, "Failed to delete structure with id=" + getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 306 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 307 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 309 */     StructureSettings.remove(getWurmId());
/* 310 */     PermissionsHistories.remove(getWurmId());
/* 311 */     Structures.removeStructure(getWurmId());
/* 312 */     deleteAllBuildTiles();
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
/*     */   public void endLoading() throws IOException {
/* 325 */     if (!hasLoaded()) {
/*     */ 
/*     */ 
/*     */       
/* 329 */       setHasLoaded(true);
/*     */       
/* 331 */       List<Wall> structureWalls = Wall.getWallsAsArrayListFor(getWurmId());
/* 332 */       if (loadStructureTiles(structureWalls))
/*     */       {
/*     */         
/* 335 */         while (fillHoles())
/*     */         {
/* 337 */           logger.log(Level.INFO, "Filling holes " + getWurmId());
/*     */         }
/*     */       }
/*     */ 
/*     */       
/* 342 */       Set<Floor> floorset = Floor.getFloorsFor(getWurmId());
/*     */       
/* 344 */       if (floorset != null) {
/* 345 */         for (Iterator<Floor> iterator = floorset.iterator(); iterator.hasNext();) {
/*     */ 
/*     */           
/*     */           try {
/* 349 */             Floor floor = iterator.next();
/* 350 */             int tilex = floor.getTileX();
/* 351 */             int tiley = floor.getTileY();
/*     */             
/* 353 */             Zone zone = Zones.getZone(tilex, tiley, isSurfaced());
/* 354 */             VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/* 355 */             if (this.structureTiles.contains(tile)) {
/*     */               
/* 357 */               tile.addFloor(floor);
/*     */               
/*     */               continue;
/*     */             } 
/* 361 */             logger.log(Level.FINE, "Floor #" + floor.getId() + " thinks it belongs to structure " + getWurmId() + " but structureTiles disagrees.");
/*     */ 
/*     */           
/*     */           }
/* 365 */           catch (NoSuchZoneException e) {
/*     */             
/* 367 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 373 */       Set<BridgePart> bridgePartsset = BridgePart.getBridgePartsFor(getWurmId());
/*     */       
/* 375 */       for (Iterator<BridgePart> it = bridgePartsset.iterator(); it.hasNext();) {
/*     */ 
/*     */         
/*     */         try {
/* 379 */           BridgePart bridgePart = it.next();
/* 380 */           int tilex = bridgePart.getTileX();
/* 381 */           int tiley = bridgePart.getTileY();
/*     */           
/* 383 */           Zone zone = Zones.getZone(tilex, tiley, isSurfaced());
/* 384 */           VolaTile tile = zone.getOrCreateTile(tilex, tiley);
/* 385 */           if (this.structureTiles.contains(tile)) {
/*     */             
/* 387 */             tile.addBridgePart(bridgePart);
/*     */             
/*     */             continue;
/*     */           } 
/* 391 */           logger.log(Level.FINE, "BridgePart #" + bridgePart.getId() + " thinks it belongs to structure " + getWurmId() + " but structureTiles disagrees.");
/*     */ 
/*     */         
/*     */         }
/* 395 */         catch (NoSuchZoneException e) {
/*     */           
/* 397 */           logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */         } 
/*     */       } 
/*     */       
/* 401 */       Zone northW = null;
/* 402 */       Zone northE = null;
/* 403 */       Zone southW = null;
/* 404 */       Zone southE = null;
/*     */       
/*     */       try {
/* 407 */         northW = Zones.getZone(this.minX, this.minY, this.surfaced);
/* 408 */         northW.addStructure(this);
/*     */       }
/* 410 */       catch (NoSuchZoneException noSuchZoneException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 416 */         northE = Zones.getZone(this.maxX, this.minY, this.surfaced);
/* 417 */         if (northE != northW) {
/* 418 */           northE.addStructure(this);
/*     */         }
/* 420 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 426 */         southE = Zones.getZone(this.maxX, this.maxY, this.surfaced);
/* 427 */         if (southE != northE && southE != northW) {
/* 428 */           southE.addStructure(this);
/*     */         }
/* 430 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 436 */         southW = Zones.getZone(this.minX, this.maxY, this.surfaced);
/* 437 */         if (southW != northE && southW != northW && southW != southE) {
/* 438 */           southW.addStructure(this);
/*     */         }
/* 440 */       } catch (NoSuchZoneException noSuchZoneException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFinished(boolean finish) {
/* 450 */     if (isFinished() != finish) {
/*     */       
/* 452 */       Connection dbcon = null;
/* 453 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 456 */         this.finished = finish;
/* 457 */         dbcon = DbConnector.getZonesDbCon();
/* 458 */         ps = dbcon.prepareStatement("UPDATE STRUCTURES SET FINISHED=? WHERE WURMID=?");
/* 459 */         ps.setBoolean(1, isFinished());
/* 460 */         ps.setLong(2, getWurmId());
/* 461 */         ps.executeUpdate();
/*     */       }
/* 463 */       catch (SQLException sqx) {
/*     */         
/* 465 */         logger.log(Level.WARNING, "Failed to set finished to " + finish + " for structure " + getName() + " with id " + 
/* 466 */             getWurmId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 470 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 471 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFinalFinished(boolean finfinish) {
/* 479 */     if (isFinalFinished() != finfinish) {
/*     */       
/* 481 */       Connection dbcon = null;
/* 482 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 485 */         this.finalfinished = finfinish;
/* 486 */         dbcon = DbConnector.getZonesDbCon();
/* 487 */         ps = dbcon.prepareStatement("UPDATE STRUCTURES SET FINFINISHED=? WHERE WURMID=?");
/* 488 */         ps.setBoolean(1, isFinalFinished());
/* 489 */         ps.setLong(2, getWurmId());
/* 490 */         ps.executeUpdate();
/*     */       }
/* 492 */       catch (SQLException sqx) {
/*     */         
/* 494 */         logger.log(Level.WARNING, "Failed to set finfinished to " + finfinish + " for structure " + getName() + " with id " + 
/* 495 */             getWurmId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 499 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 500 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveWritId() {
/* 508 */     Connection dbcon = null;
/* 509 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 512 */       dbcon = DbConnector.getZonesDbCon();
/* 513 */       ps = dbcon.prepareStatement("UPDATE STRUCTURES SET WRITID=? WHERE WURMID=?");
/* 514 */       ps.setLong(1, this.writid);
/* 515 */       ps.setLong(2, getWurmId());
/* 516 */       ps.executeUpdate();
/*     */     }
/* 518 */     catch (SQLException sqx) {
/*     */       
/* 520 */       logger.log(Level.WARNING, "Failed to set writId to " + this.writid + " for structure " + getName() + " with id " + 
/* 521 */           getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 525 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 526 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveOwnerId() {
/* 533 */     Connection dbcon = null;
/* 534 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 537 */       dbcon = DbConnector.getZonesDbCon();
/* 538 */       ps = dbcon.prepareStatement("UPDATE STRUCTURES SET OWNERID=? WHERE WURMID=?");
/* 539 */       ps.setLong(1, this.ownerId);
/* 540 */       ps.setLong(2, getWurmId());
/* 541 */       ps.executeUpdate();
/*     */     }
/* 543 */     catch (SQLException sqx) {
/*     */       
/* 545 */       logger.log(Level.WARNING, "Failed to set ownerId to " + this.ownerId + " for structure " + getName() + " with id " + 
/* 546 */           getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 550 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 551 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveSettings() {
/* 558 */     Connection dbcon = null;
/* 559 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 562 */       dbcon = DbConnector.getZonesDbCon();
/* 563 */       ps = dbcon.prepareStatement("UPDATE STRUCTURES SET SETTINGS=?,VILLAGE=? WHERE WURMID=?");
/* 564 */       ps.setInt(1, getSettings().getPermissions());
/* 565 */       ps.setInt(2, getVillageId());
/* 566 */       ps.setLong(3, getWurmId());
/* 567 */       ps.executeUpdate();
/*     */     }
/* 569 */     catch (SQLException sqx) {
/*     */       
/* 571 */       logger.log(Level.WARNING, "Failed to set settings to " + getSettings().getPermissions() + " for structure " + getName() + " with id " + 
/* 572 */           getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 576 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 577 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveName() {
/* 584 */     Connection dbcon = null;
/* 585 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 588 */       dbcon = DbConnector.getZonesDbCon();
/* 589 */       ps = dbcon.prepareStatement("UPDATE STRUCTURES SET NAME=? WHERE WURMID=?");
/* 590 */       ps.setString(1, getName());
/* 591 */       ps.setLong(2, getWurmId());
/* 592 */       ps.executeUpdate();
/*     */     }
/* 594 */     catch (SQLException sqx) {
/*     */       
/* 596 */       logger.log(Level.WARNING, "Failed to set name to " + getName() + " for structure with id " + getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 600 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 601 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowVillagers(boolean allow) {
/* 608 */     if (allowsCitizens() != allow) {
/*     */       
/* 610 */       this.allowsVillagers = allow;
/*     */       
/* 612 */       if (allow) {
/* 613 */         addDefaultCitizenPermissions();
/*     */       } else {
/* 615 */         removeStructureGuest(-30L);
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
/*     */   public void setAllowKingdom(boolean allow) {
/* 627 */     if (allowsKingdom() != allow) {
/*     */       
/* 629 */       this.allowsKingdom = allow;
/*     */       
/* 631 */       if (allow) {
/* 632 */         addDefaultKingdomPermissions();
/*     */       } else {
/* 634 */         removeStructureGuest(-40L);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowAllies(boolean allow) {
/* 641 */     if (allowsAllies() != allow) {
/*     */       
/* 643 */       this.allowsAllies = allow;
/*     */       
/* 645 */       if (allow) {
/* 646 */         addDefaultAllyPermissions();
/*     */       } else {
/* 648 */         removeStructureGuest(-20L);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addNewGuest(long guestId, int aSettings) {
/* 655 */     StructureSettings.addPlayer(getWurmId(), guestId, aSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeStructureGuest(long guestId) {
/* 661 */     StructureSettings.removePlayer(getWurmId(), guestId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBuildTile(int tilex, int tiley, int layer) {
/* 667 */     Connection dbcon = null;
/* 668 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 671 */       dbcon = DbConnector.getZonesDbCon();
/* 672 */       ps = dbcon.prepareStatement("DELETE FROM BUILDTILES WHERE STRUCTUREID=? AND TILEX=? AND TILEY=? AND LAYER=?");
/* 673 */       ps.setLong(1, getWurmId());
/* 674 */       ps.setInt(2, tilex);
/* 675 */       ps.setInt(3, tiley);
/* 676 */       ps.setInt(4, layer);
/* 677 */       ps.executeUpdate();
/*     */     }
/* 679 */     catch (SQLException ex) {
/*     */       
/* 681 */       logger.log(Level.WARNING, "Failed to remove build tile for structure with id " + getWurmId(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 685 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 686 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNewBuildTile(int tilex, int tiley, int layer) {
/* 693 */     Connection dbcon = null;
/* 694 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 697 */       dbcon = DbConnector.getZonesDbCon();
/* 698 */       ps = dbcon.prepareStatement("INSERT INTO BUILDTILES(STRUCTUREID,TILEX,TILEY,LAYER) VALUES (?,?,?,?)");
/*     */       
/* 700 */       ps.setLong(1, getWurmId());
/* 701 */       ps.setInt(2, tilex);
/* 702 */       ps.setInt(3, tiley);
/* 703 */       ps.setInt(4, layer);
/* 704 */       ps.executeUpdate();
/*     */     }
/* 706 */     catch (SQLException ex) {
/*     */       
/* 708 */       logger.log(Level.WARNING, "Failed to add build tile for structure with id " + getWurmId(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 712 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 713 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadBuildTiles() {
/* 719 */     Connection dbcon = null;
/* 720 */     PreparedStatement ps = null;
/* 721 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 724 */       dbcon = DbConnector.getZonesDbCon();
/* 725 */       ps = dbcon.prepareStatement("SELECT * FROM BUILDTILES");
/* 726 */       rs = ps.executeQuery();
/* 727 */       while (rs.next()) {
/*     */         
/*     */         try
/*     */         {
/* 731 */           Structure structure = Structures.getStructure(rs.getLong("STRUCTUREID"));
/* 732 */           structure.addBuildTile(new BuildTile(rs.getInt("TILEX"), rs.getInt("TILEY"), rs.getInt("LAYER")));
/*     */         }
/* 734 */         catch (NoSuchStructureException nss)
/*     */         {
/* 736 */           logger.log(Level.WARNING, nss.getMessage());
/*     */         }
/*     */       
/*     */       } 
/* 740 */     } catch (SQLException ex) {
/*     */       
/* 742 */       logger.log(Level.WARNING, "Failed to load all tiles for structures" + ex.getMessage(), ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 747 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 748 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteAllBuildTiles() {
/* 755 */     Connection dbcon = null;
/* 756 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 759 */       dbcon = DbConnector.getZonesDbCon();
/* 760 */       ps = dbcon.prepareStatement("DELETE FROM BUILDTILES WHERE STRUCTUREID=?");
/* 761 */       ps.setLong(1, getWurmId());
/* 762 */       ps.executeUpdate();
/*     */     }
/* 764 */     catch (SQLException ex) {
/*     */       
/* 766 */       logger.log(Level.WARNING, "Failed to delete all build tiles for structure with id " + getWurmId(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 770 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 771 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItem() {
/* 778 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */