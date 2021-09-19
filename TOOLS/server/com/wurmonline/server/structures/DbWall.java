/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.MethodsStructure;
/*     */ import com.wurmonline.server.tutorial.MissionTargets;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
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
/*     */ public final class DbWall
/*     */   extends Wall
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(DbWall.class.getName());
/*     */ 
/*     */   
/*     */   private static final String createWall = "insert into WALLS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL,DAMAGE, STRUCTURE, STARTX, STARTY, ENDX, ENDY, OUTERWALL, TILEX, TILEY, STATE,MATERIAL,ISINDOOR, HEIGHTOFFSET, LAYER, WALLORIENTATION) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String updateWall = "update WALLS set TYPE=?, LASTMAINTAINED =?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STRUCTURE=?, STATE=?,MATERIAL=?,ISINDOOR=?,HEIGHTOFFSET=?,LAYER=?,TILEX=?,TILEY=? where ID=?";
/*     */ 
/*     */   
/*     */   private static final String getWall = "select * from WALLS where ID=?";
/*     */ 
/*     */   
/*     */   private static final String deleteWall = "delete from WALLS where ID=?";
/*     */ 
/*     */   
/*     */   private static final String setDamage = "update WALLS set DAMAGE=? where ID=?";
/*     */   
/*     */   private static final String setState = "update WALLS set STATE=?,MATERIAL=? where ID=?";
/*     */   
/*     */   private static final String setQL = "update WALLS set CURRENTQL=? where ID=?";
/*     */   
/*     */   private static final String setOrigQL = "update WALLS set ORIGINALQL=? where ID=?";
/*     */   
/*     */   private static final String setLastUsed = "update WALLS set LASTMAINTAINED=? where ID=?";
/*     */   
/*     */   private static final String setIsIndoor = "update WALLS set ISINDOOR=? where ID=?";
/*     */   
/*     */   private static final String setColor = "update WALLS set COLOR=? WHERE ID=?";
/*     */   
/*     */   private static final String setOrientation = "update WALLS set WALLORIENTATION=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_SETTINGS = "UPDATE WALLS SET SETTINGS=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   public DbWall(StructureTypeEnum aType, int aTileX, int aTileY, int aStartX, int aStartY, int aEndX, int aEndY, float aQualityLevel, long aStructure, StructureMaterialEnum aMaterial, boolean aIsIndoor, int aHeightOffset, int aLayer) {
/*  79 */     super(aType, aTileX, aTileY, aStartX, aStartY, aEndX, aEndY, aQualityLevel, aStructure, aMaterial, aIsIndoor, aHeightOffset, aLayer);
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
/*     */   DbWall(int aNumber, StructureTypeEnum aType, int aTileX, int aTileY, int aStartX, int aStartY, int aEndX, int aEndY, float aQualityLevel, float aOriginalQl, float aDamage, long aStructure, long aLastUsed, StructureStateEnum aState, int aColor, StructureMaterialEnum aMaterial, boolean aIsIndoor, int aHeightOffset, int aLayer, boolean wallOrientation, int aSettings) {
/*  92 */     super(aNumber, aType, aTileX, aTileY, aStartX, aStartY, aEndX, aEndY, aQualityLevel, aOriginalQl, aDamage, aStructure, aLastUsed, aState, aColor, aMaterial, aIsIndoor, aHeightOffset, aLayer, wallOrientation, aSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbWall(int aNumber) throws IOException {
/*  99 */     super(aNumber, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 105 */     Connection dbcon = null;
/* 106 */     PreparedStatement ps = null;
/* 107 */     ResultSet rs = null;
/*     */ 
/*     */     
/*     */     try {
/* 111 */       dbcon = DbConnector.getZonesDbCon();
/* 112 */       if (exists(dbcon)) {
/*     */ 
/*     */ 
/*     */         
/* 116 */         ps = dbcon.prepareStatement("update WALLS set TYPE=?, LASTMAINTAINED =?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STRUCTURE=?, STATE=?,MATERIAL=?,ISINDOOR=?,HEIGHTOFFSET=?,LAYER=?,TILEX=?,TILEY=? where ID=?");
/* 117 */         ps.setByte(1, this.type.value);
/* 118 */         ps.setLong(2, this.lastUsed);
/* 119 */         ps.setFloat(3, this.currentQL);
/* 120 */         ps.setFloat(4, this.originalQL);
/* 121 */         ps.setFloat(5, this.damage);
/* 122 */         ps.setLong(6, this.structureId);
/* 123 */         ps.setByte(7, this.state.state);
/* 124 */         ps.setByte(8, (getMaterial()).material);
/* 125 */         ps.setBoolean(9, isIndoor());
/* 126 */         ps.setInt(10, getHeight());
/* 127 */         ps.setInt(11, getLayer());
/* 128 */         ps.setInt(12, getTileX());
/* 129 */         ps.setInt(13, getTileY());
/* 130 */         ps.setInt(14, this.number);
/* 131 */         ps.executeUpdate();
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 137 */         ps = dbcon.prepareStatement("insert into WALLS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL,DAMAGE, STRUCTURE, STARTX, STARTY, ENDX, ENDY, OUTERWALL, TILEX, TILEY, STATE,MATERIAL,ISINDOOR, HEIGHTOFFSET, LAYER, WALLORIENTATION) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 138 */         ps.setByte(1, this.type.value);
/* 139 */         ps.setLong(2, this.lastUsed);
/* 140 */         ps.setFloat(3, this.currentQL);
/* 141 */         ps.setFloat(4, this.originalQL);
/* 142 */         ps.setFloat(5, this.damage);
/* 143 */         ps.setLong(6, this.structureId);
/* 144 */         ps.setInt(7, getStartX());
/* 145 */         ps.setInt(8, getStartY());
/* 146 */         ps.setInt(9, getEndX());
/* 147 */         ps.setInt(10, getEndY());
/* 148 */         ps.setBoolean(11, false);
/* 149 */         ps.setInt(12, this.tilex);
/* 150 */         ps.setInt(13, this.tiley);
/* 151 */         ps.setByte(14, this.state.state);
/* 152 */         ps.setByte(15, (getMaterial()).material);
/* 153 */         ps.setBoolean(16, isIndoor());
/* 154 */         ps.setInt(17, getHeight());
/* 155 */         ps.setInt(18, getLayer());
/* 156 */         ps.setBoolean(19, false);
/* 157 */         ps.executeUpdate();
/* 158 */         rs = ps.getGeneratedKeys();
/* 159 */         if (rs.next()) {
/* 160 */           this.number = rs.getInt(1);
/*     */         }
/*     */       } 
/* 163 */     } catch (SQLException sqx) {
/*     */       
/* 165 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 169 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 170 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException {
/* 177 */     Connection dbcon = null;
/* 178 */     PreparedStatement ps = null;
/* 179 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 182 */       dbcon = DbConnector.getZonesDbCon();
/* 183 */       ps = dbcon.prepareStatement("select * from WALLS where ID=?");
/* 184 */       ps.setInt(1, this.number);
/* 185 */       rs = ps.executeQuery();
/* 186 */       if (rs.next()) {
/*     */         
/* 188 */         this.x1 = rs.getInt("STARTX");
/* 189 */         this.x2 = rs.getInt("ENDX");
/* 190 */         this.y1 = rs.getInt("STARTY");
/* 191 */         this.y2 = rs.getInt("ENDY");
/* 192 */         this.tilex = rs.getInt("TILEX");
/* 193 */         this.tiley = rs.getInt("TILEY");
/* 194 */         this.currentQL = rs.getFloat("ORIGINALQL");
/* 195 */         this.originalQL = rs.getFloat("CURRENTQL");
/* 196 */         this.lastUsed = rs.getLong("LASTMAINTAINED");
/* 197 */         this.structureId = rs.getLong("STRUCTURE");
/* 198 */         this.type = StructureTypeEnum.getTypeByINDEX(rs.getByte("TYPE"));
/* 199 */         this.state = StructureStateEnum.getStateByValue(rs.getByte("STATE"));
/* 200 */         this.damage = rs.getFloat("DAMAGE");
/* 201 */         setColor(rs.getInt("COLOR"));
/* 202 */         setIndoor(rs.getBoolean("ISINDOOR"));
/* 203 */         this.heightOffset = rs.getInt("HEIGHTOFFSET");
/* 204 */         this.wallOrientationFlag = rs.getBoolean("WALLORIENTATION");
/*     */       } else {
/*     */         
/* 207 */         logger.log(Level.WARNING, "Failed to find wall with number " + this.number);
/* 208 */       }  DbUtilities.closeDatabaseObjects(ps, rs);
/* 209 */       if (this.state.state <= StructureStateEnum.UNINITIALIZED.state) {
/*     */         
/* 211 */         this.state = StructureStateEnum.FINISHED;
/* 212 */         save();
/*     */       } 
/* 214 */       if (this.type.value == Byte.MAX_VALUE) {
/*     */         
/* 216 */         this.type = StructureTypeEnum.PLAN;
/* 217 */         save();
/*     */       } 
/* 219 */       if (this.type == StructureTypeEnum.RUBBLE)
/*     */       {
/* 221 */         addRubble(this);
/*     */       }
/*     */     }
/* 224 */     catch (SQLException sqx) {
/*     */       
/* 226 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 230 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 231 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 237 */     PreparedStatement ps = null;
/* 238 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 241 */       ps = dbcon.prepareStatement("select * from WALLS where ID=?");
/* 242 */       ps.setInt(1, this.number);
/* 243 */       rs = ps.executeQuery();
/* 244 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 248 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 255 */     MissionTargets.destroyMissionTarget(getId(), true);
/* 256 */     Connection dbcon = null;
/* 257 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 260 */       dbcon = DbConnector.getZonesDbCon();
/* 261 */       ps = dbcon.prepareStatement("delete from WALLS where ID=?");
/* 262 */       ps.setInt(1, this.number);
/* 263 */       ps.executeUpdate();
/*     */     }
/* 265 */     catch (SQLException sqx) {
/*     */       
/* 267 */       logger.log(Level.WARNING, "Failed to delete wall with id " + this.number, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 271 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 272 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setDamage(float dam) {
/* 279 */     if (dam >= 100.0F) {
/*     */ 
/*     */       
/* 282 */       if (Servers.localServer.testServer)
/*     */       {
/*     */         
/* 285 */         logger.fine("TEMPORARY LOGGING FOR BUG #1264 - Destroying wall with ID:" + getId() + " which was part of structure with id:" + getStructureId());
/*     */       }
/*     */       
/* 288 */       boolean forcePlan = false;
/* 289 */       VolaTile tile = getTile();
/* 290 */       if (tile != null) {
/*     */         
/* 292 */         Structure struct = tile.getStructure();
/* 293 */         if (struct != null)
/*     */         {
/* 295 */           if (struct.wouldCreateFlyingStructureIfRemoved(this))
/*     */           {
/* 297 */             forcePlan = true;
/*     */           }
/*     */         }
/*     */       } 
/* 301 */       if (MethodsStructure.isWallInsideStructure(this, isOnSurface()) && !forcePlan) {
/*     */ 
/*     */         
/* 304 */         destroy();
/*     */         
/* 306 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 310 */       if (Servers.localServer.isChallengeServer()) {
/*     */         
/* 312 */         if (isFinished() && getType() != StructureTypeEnum.RUBBLE) {
/*     */           
/* 314 */           setAsRubble();
/* 315 */           return true;
/*     */         } 
/*     */ 
/*     */         
/* 319 */         dam = 0.0F;
/* 320 */         setAsPlan();
/* 321 */         setQualityLevel(1.0F);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 326 */         dam = 0.0F;
/* 327 */         setAsPlan();
/* 328 */         setQualityLevel(1.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 332 */     if (this.damage != dam) {
/*     */       
/* 334 */       boolean updateState = false;
/* 335 */       if ((this.damage >= 60.0F && dam < 60.0F) || (this.damage < 60.0F && dam >= 60.0F))
/*     */       {
/* 337 */         updateState = true;
/*     */       }
/* 339 */       Connection dbcon = null;
/* 340 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 343 */         this.damage = dam;
/* 344 */         dbcon = DbConnector.getZonesDbCon();
/* 345 */         ps = dbcon.prepareStatement("update WALLS set DAMAGE=? where ID=?");
/* 346 */         ps.setFloat(1, this.damage);
/* 347 */         ps.setInt(2, this.number);
/* 348 */         ps.executeUpdate();
/*     */       }
/* 350 */       catch (SQLException sqx) {
/*     */         
/* 352 */         logger.log(Level.WARNING, "Failed to set damage to " + dam + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 356 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 357 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */       
/* 360 */       if (updateState) {
/*     */         
/* 362 */         VolaTile tile = getTile();
/* 363 */         if (tile != null)
/*     */         {
/* 365 */           getTile().updateWallDamageState(this);
/*     */         }
/*     */       } 
/*     */     } 
/* 369 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setQualityLevel(float ql) {
/* 375 */     if (ql > 100.0F)
/* 376 */       ql = 100.0F; 
/* 377 */     if (this.currentQL != ql) {
/*     */       
/* 379 */       Connection dbcon = null;
/* 380 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 383 */         this.currentQL = ql;
/* 384 */         dbcon = DbConnector.getZonesDbCon();
/* 385 */         ps = dbcon.prepareStatement("update WALLS set CURRENTQL=? where ID=?");
/* 386 */         ps.setFloat(1, this.currentQL);
/* 387 */         ps.setInt(2, this.number);
/* 388 */         ps.executeUpdate();
/*     */       }
/* 390 */       catch (SQLException sqx) {
/*     */         
/* 392 */         logger.log(Level.WARNING, "Failed to set quality to " + ql + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 396 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 397 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 400 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void improveOrigQualityLevel(float ql) {
/* 406 */     if (ql > 100.0F)
/* 407 */       ql = 100.0F; 
/* 408 */     if (this.originalQL != ql) {
/*     */       
/* 410 */       Connection dbcon = null;
/* 411 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 414 */         this.originalQL = ql;
/* 415 */         dbcon = DbConnector.getZonesDbCon();
/* 416 */         ps = dbcon.prepareStatement("update WALLS set ORIGINALQL=? where ID=?");
/* 417 */         ps.setFloat(1, this.originalQL);
/* 418 */         ps.setInt(2, this.number);
/* 419 */         ps.executeUpdate();
/*     */       }
/* 421 */       catch (SQLException sqx) {
/*     */         
/* 423 */         logger.log(Level.WARNING, "Failed to set original quality to " + ql + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 427 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 428 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndoor(boolean indoor) {
/* 436 */     if (this.isIndoor != indoor) {
/*     */       
/* 438 */       Connection dbcon = null;
/* 439 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 442 */         this.isIndoor = indoor;
/* 443 */         dbcon = DbConnector.getZonesDbCon();
/* 444 */         ps = dbcon.prepareStatement("update WALLS set ISINDOOR=? where ID=?");
/* 445 */         ps.setBoolean(1, this.isIndoor);
/* 446 */         ps.setInt(2, this.number);
/* 447 */         ps.executeUpdate();
/*     */       }
/* 449 */       catch (SQLException sqx) {
/*     */         
/* 451 */         logger.log(Level.WARNING, "Failed to set indoor to " + indoor + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 455 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 456 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastUsed(long last) {
/* 464 */     if (this.lastUsed != last) {
/*     */       
/* 466 */       Connection dbcon = null;
/* 467 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 470 */         this.lastUsed = last;
/* 471 */         dbcon = DbConnector.getZonesDbCon();
/* 472 */         ps = dbcon.prepareStatement("update WALLS set LASTMAINTAINED=? where ID=?");
/* 473 */         ps.setLong(1, last);
/* 474 */         ps.setInt(2, this.number);
/* 475 */         ps.executeUpdate();
/*     */       }
/* 477 */       catch (SQLException sqx) {
/*     */         
/* 479 */         logger.log(Level.WARNING, "Failed to set lastUsed to " + last + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 483 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 484 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(StructureStateEnum newState) {
/* 492 */     if (this.state == StructureStateEnum.FINISHED)
/*     */     {
/* 494 */       if (newState != StructureStateEnum.INITIALIZED)
/*     */         return; 
/*     */     }
/* 497 */     if (newState.state >= (getFinalState()).state)
/* 498 */       newState = StructureStateEnum.FINISHED; 
/* 499 */     if (this.state != newState) {
/*     */       
/* 501 */       Connection dbcon = null;
/* 502 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 505 */         this.state = newState;
/* 506 */         dbcon = DbConnector.getZonesDbCon();
/* 507 */         ps = dbcon.prepareStatement("update WALLS set STATE=?,MATERIAL=? where ID=?");
/* 508 */         ps.setByte(1, this.state.state);
/* 509 */         ps.setByte(2, (getMaterial()).material);
/* 510 */         ps.setInt(3, this.number);
/* 511 */         ps.executeUpdate();
/*     */       }
/* 513 */       catch (SQLException sqx) {
/*     */         
/* 515 */         logger.log(Level.WARNING, "Failed to set state to " + this.state + " for wall with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 519 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 520 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWallOrientation(boolean rotated) {
/* 528 */     if (this.wallOrientationFlag != rotated) {
/*     */       
/* 530 */       this.wallOrientationFlag = rotated;
/* 531 */       Connection dbcon = null;
/* 532 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 535 */         dbcon = DbConnector.getZonesDbCon();
/* 536 */         ps = dbcon.prepareStatement("update WALLS set WALLORIENTATION=? WHERE ID=?");
/* 537 */         ps.setBoolean(1, this.wallOrientationFlag);
/* 538 */         ps.setInt(2, this.number);
/* 539 */         ps.executeUpdate();
/*     */ 
/*     */         
/*     */         try {
/* 543 */           Structure struct = Structures.getStructure(this.structureId);
/* 544 */           VolaTile tile = struct.getTileFor(this);
/* 545 */           if (tile != null)
/*     */           {
/* 547 */             tile.updateWall(this);
/*     */           }
/*     */         }
/* 550 */         catch (NoSuchStructureException nss) {
/*     */           
/* 552 */           logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ", StructureId: " + this.structureId + " - " + nss
/* 553 */               .getMessage(), (Throwable)nss);
/*     */         }
/*     */       
/* 556 */       } catch (SQLException sqx) {
/*     */         
/* 558 */         logger.log(Level.WARNING, "Failed to set wall orientation to " + this.wallOrientationFlag + " for wall with id " + this.number, sqx);
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 563 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 564 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean changeColor(int newcolor) {
/* 572 */     if (getColor() != newcolor) {
/*     */       
/* 574 */       this.color = newcolor;
/* 575 */       Connection dbcon = null;
/* 576 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 579 */         dbcon = DbConnector.getZonesDbCon();
/* 580 */         ps = dbcon.prepareStatement("update WALLS set COLOR=? WHERE ID=?");
/* 581 */         ps.setInt(1, newcolor);
/* 582 */         ps.setInt(2, this.number);
/* 583 */         ps.executeUpdate();
/*     */         
/*     */         try {
/* 586 */           Structure struct = Structures.getStructure(this.structureId);
/* 587 */           VolaTile tile = struct.getTileFor(this);
/* 588 */           if (tile != null)
/*     */           {
/* 590 */             tile.updateWall(this);
/*     */           }
/*     */         }
/* 593 */         catch (NoSuchStructureException nss) {
/*     */           
/* 595 */           logger.log(Level.WARNING, "wall at " + this.x1 + ", " + this.y1 + "-" + this.x2 + "," + this.y2 + ", StructureId: " + this.structureId + " - " + nss
/*     */               
/* 597 */               .getMessage(), (Throwable)nss);
/*     */         } 
/* 599 */         return true;
/*     */       }
/* 601 */       catch (SQLException sqx) {
/*     */         
/* 603 */         logger.log(Level.WARNING, "Failed to set color to " + getColor() + " for wall with id " + this.number, sqx);
/* 604 */         return true;
/*     */       }
/*     */       finally {
/*     */         
/* 608 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 609 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */     
/* 613 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void savePermissions() {
/* 619 */     Connection dbcon = null;
/* 620 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 623 */       dbcon = DbConnector.getZonesDbCon();
/* 624 */       ps = dbcon.prepareStatement("UPDATE WALLS SET SETTINGS=? WHERE ID=?");
/* 625 */       ps.setLong(1, this.permissions.getPermissions());
/* 626 */       ps.setLong(2, this.number);
/* 627 */       ps.executeUpdate();
/*     */     }
/* 629 */     catch (SQLException sqx) {
/*     */       
/* 631 */       logger.log(Level.WARNING, "Failed to save settings for wall with id " + this.number, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 635 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 636 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbWall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */