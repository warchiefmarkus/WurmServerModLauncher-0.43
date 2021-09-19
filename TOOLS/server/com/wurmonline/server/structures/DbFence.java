/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.server.tutorial.MissionTargets;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
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
/*     */ public final class DbFence
/*     */   extends Fence
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(DbFence.class.getName());
/*     */ 
/*     */   
/*     */   private static final String CREATE_FENCE = "insert into FENCES(TYPE,LASTMAINTAINED,CURRENTQL,ORIGINALQL,DAMAGE,TILEX,TILEY,DIR,ZONEID,STATE,HEIGHTOFFSET,LAYER) values(?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String UPDATE_FENCE = "update FENCES set TYPE=?, LASTMAINTAINED=?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STATE=? where ID=?";
/*     */ 
/*     */   
/*     */   private static final String GET_FENCE = "select * from FENCES where ID=?";
/*     */ 
/*     */   
/*     */   private static final String DELETE_FENCE = "delete from FENCES where ID=?";
/*     */ 
/*     */   
/*     */   private static final String SET_ZONE_ID = "update FENCES set ZONEID=? where ID=?";
/*     */ 
/*     */   
/*     */   private static final String SET_DAMAGE = "update FENCES set DAMAGE=? where ID=?";
/*     */   
/*     */   private static final String SET_QL = "update FENCES set CURRENTQL=? where ID=?";
/*     */   
/*     */   private static final String SET_ORIGINAL_QL = "update FENCES set ORIGINALQL=? where ID=?";
/*     */   
/*     */   private static final String SET_LAST_USED = "update FENCES set LASTMAINTAINED=? where ID=?";
/*     */   
/*     */   private static final String SET_COLOR = "update FENCES set COLOR=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_SETTINGS = "UPDATE FENCES SET SETTINGS=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   public DbFence(StructureConstantsEnum aType, int aTileX, int aTileY, int aHeightOffset, float aQualityLevel, Tiles.TileBorderDirection aDir, int aZoneId, int aLayer) {
/*  79 */     super(aType, aTileX, aTileY, aHeightOffset, aQualityLevel, aDir, aZoneId, aLayer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbFence(int aNumber, StructureConstantsEnum aType, StructureStateEnum aState, int aColor, int aTileX, int aTileY, int aHeightOffset, float aQualityLevel, float aOriginalQl, long aLastUsed, Tiles.TileBorderDirection aDir, int aZoneId, boolean aSurfaced, float aDamage, int aLayer, int aSettings) {
/*  90 */     super(aNumber, aType, aState, aColor, aTileX, aTileY, aHeightOffset, aQualityLevel, aOriginalQl, aLastUsed, aDir, aZoneId, aSurfaced, aDamage, aLayer, aSettings);
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
/*     */   public void save() throws IOException {
/* 104 */     Connection dbcon = null;
/* 105 */     PreparedStatement ps = null;
/* 106 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 109 */       dbcon = DbConnector.getZonesDbCon();
/* 110 */       if (exists(dbcon)) {
/*     */         
/* 112 */         ps = dbcon.prepareStatement("update FENCES set TYPE=?, LASTMAINTAINED=?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STATE=? where ID=?");
/* 113 */         ps.setShort(1, this.type.value);
/* 114 */         ps.setLong(2, this.lastUsed);
/* 115 */         ps.setFloat(3, this.currentQL);
/* 116 */         ps.setFloat(4, this.originalQL);
/* 117 */         ps.setFloat(5, this.damage);
/* 118 */         ps.setByte(6, this.state.state);
/* 119 */         ps.setInt(7, this.number);
/* 120 */         ps.executeUpdate();
/*     */       }
/*     */       else {
/*     */         
/* 124 */         ps = dbcon.prepareStatement("insert into FENCES(TYPE,LASTMAINTAINED,CURRENTQL,ORIGINALQL,DAMAGE,TILEX,TILEY,DIR,ZONEID,STATE,HEIGHTOFFSET,LAYER) values(?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 125 */         ps.setShort(1, this.type.value);
/* 126 */         ps.setLong(2, this.lastUsed);
/* 127 */         ps.setFloat(3, this.currentQL);
/* 128 */         ps.setFloat(4, this.originalQL);
/* 129 */         ps.setFloat(5, 0.0F);
/* 130 */         ps.setInt(6, this.tilex);
/* 131 */         ps.setInt(7, this.tiley);
/* 132 */         ps.setByte(8, this.dir);
/* 133 */         ps.setInt(9, this.zoneId);
/* 134 */         ps.setByte(10, this.state.state);
/* 135 */         ps.setInt(11, this.heightOffset);
/* 136 */         ps.setInt(12, this.layer);
/* 137 */         ps.executeUpdate();
/* 138 */         rs = ps.getGeneratedKeys();
/* 139 */         if (rs.next()) {
/* 140 */           this.number = rs.getInt(1);
/*     */         }
/*     */       } 
/* 143 */     } catch (SQLException sqx) {
/*     */       
/* 145 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 149 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 150 */       DbConnector.returnConnection(dbcon);
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
/*     */   void load() throws IOException {
/* 165 */     Connection dbcon = null;
/* 166 */     PreparedStatement ps = null;
/* 167 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 170 */       dbcon = DbConnector.getZonesDbCon();
/* 171 */       ps = dbcon.prepareStatement("select * from FENCES where ID=?");
/* 172 */       ps.setInt(1, this.number);
/* 173 */       rs = ps.executeQuery();
/* 174 */       if (rs.next()) {
/*     */         
/* 176 */         this.tilex = rs.getInt("TILEX");
/* 177 */         this.tiley = rs.getInt("TILEY");
/* 178 */         this.currentQL = rs.getFloat("ORIGINALQL");
/* 179 */         this.originalQL = rs.getFloat("CURRENTQL");
/* 180 */         this.lastUsed = rs.getLong("LASTMAINTAINED");
/* 181 */         this.type = StructureConstantsEnum.getEnumByValue(rs.getShort("TYPE"));
/* 182 */         this.state = StructureStateEnum.getStateByValue(rs.getByte("STATE"));
/* 183 */         this.zoneId = rs.getInt("ZONEID");
/* 184 */         this.dir = rs.getByte("DIR");
/* 185 */         this.damage = rs.getFloat("DAMAGE");
/* 186 */         setSettings(rs.getInt("SETTINGS"));
/*     */       } else {
/*     */         
/* 189 */         logger.log(Level.WARNING, "Failed to find fence with number " + this.number);
/*     */       } 
/* 191 */     } catch (SQLException sqx) {
/*     */       
/* 193 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 197 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 198 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 204 */     PreparedStatement ps = null;
/* 205 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 208 */       ps = dbcon.prepareStatement("select * from FENCES where ID=?");
/* 209 */       ps.setInt(1, this.number);
/* 210 */       rs = ps.executeQuery();
/* 211 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 215 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 222 */     MissionTargets.destroyMissionTarget(getId(), true);
/* 223 */     Connection dbcon = null;
/* 224 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 227 */       dbcon = DbConnector.getZonesDbCon();
/* 228 */       ps = dbcon.prepareStatement("delete from FENCES where ID=?");
/* 229 */       ps.setInt(1, this.number);
/* 230 */       ps.executeUpdate();
/*     */     }
/* 232 */     catch (SQLException sqx) {
/*     */       
/* 234 */       logger.log(Level.WARNING, "Failed to delete fence with id " + this.number, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 238 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 239 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZoneId(int zid) {
/* 246 */     if (this.zoneId != zid) {
/*     */       
/* 248 */       Connection dbcon = null;
/* 249 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 252 */         this.zoneId = zid;
/* 253 */         dbcon = DbConnector.getZonesDbCon();
/* 254 */         ps = dbcon.prepareStatement("update FENCES set ZONEID=? where ID=?");
/* 255 */         ps.setInt(1, this.zoneId);
/* 256 */         ps.setInt(2, this.number);
/* 257 */         ps.executeUpdate();
/*     */       }
/* 259 */       catch (SQLException sqx) {
/*     */         
/* 261 */         logger.log(Level.WARNING, "Failed to set zoneid to " + zid + " for fence with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 265 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 266 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setDamage(float dam) {
/* 274 */     boolean destroyed = false;
/* 275 */     if (isIndestructible())
/* 276 */       return false; 
/* 277 */     if (dam >= 100.0F) {
/*     */       
/* 279 */       DoorSettings.remove(getId());
/* 280 */       PermissionsHistories.remove(getId());
/* 281 */       destroyed = true;
/* 282 */       if (supports()) {
/*     */         
/* 284 */         boolean forcePlan = false;
/* 285 */         VolaTile tile = getTile();
/* 286 */         if (tile != null) {
/*     */           
/* 288 */           Structure struct = tile.getStructure();
/* 289 */           if (struct != null)
/*     */           {
/* 291 */             if (struct.wouldCreateFlyingStructureIfRemoved(this))
/*     */             {
/* 293 */               forcePlan = true;
/*     */             }
/*     */           }
/*     */         } 
/* 297 */         if (forcePlan) {
/*     */           
/* 299 */           dam = 0.0F;
/* 300 */           setType(getFencePlanForType(getType()));
/* 301 */           setQualityLevel(1.0F);
/* 302 */           if (tile != null)
/* 303 */             tile.updateFence(this); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 307 */     if (dam >= 100.0F) {
/* 308 */       destroy();
/*     */     
/*     */     }
/* 311 */     else if (this.damage != dam) {
/*     */       
/* 313 */       boolean updateState = false;
/* 314 */       if ((this.damage >= 60.0F && dam < 60.0F) || (this.damage < 60.0F && dam >= 60.0F))
/*     */       {
/* 316 */         updateState = true;
/*     */       }
/* 318 */       Connection dbcon = null;
/* 319 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 322 */         this.damage = Math.max(0.0F, dam);
/* 323 */         dbcon = DbConnector.getZonesDbCon();
/* 324 */         ps = dbcon.prepareStatement("update FENCES set DAMAGE=? where ID=?");
/* 325 */         ps.setFloat(1, this.damage);
/* 326 */         ps.setInt(2, this.number);
/* 327 */         ps.executeUpdate();
/*     */       }
/* 329 */       catch (SQLException sqx) {
/*     */         
/* 331 */         logger.log(Level.WARNING, "Failed to set damage to " + dam + " for fence with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 335 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 336 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/* 338 */       if (updateState && !isMagic())
/*     */       {
/* 340 */         if (getTile() != null) {
/* 341 */           getTile().updateFenceState(this);
/*     */         }
/*     */       }
/*     */     } 
/* 345 */     return destroyed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setQualityLevel(float ql) {
/* 351 */     if (ql > 100.0F)
/* 352 */       ql = 100.0F; 
/* 353 */     if (this.currentQL != ql) {
/*     */       
/* 355 */       Connection dbcon = null;
/* 356 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 359 */         this.currentQL = ql;
/* 360 */         dbcon = DbConnector.getZonesDbCon();
/* 361 */         ps = dbcon.prepareStatement("update FENCES set CURRENTQL=? where ID=?");
/* 362 */         ps.setFloat(1, this.currentQL);
/* 363 */         ps.setInt(2, this.number);
/* 364 */         ps.executeUpdate();
/*     */       }
/* 366 */       catch (SQLException sqx) {
/*     */         
/* 368 */         logger.log(Level.WARNING, "Failed to set QL to " + ql + " for fence with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 372 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 373 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 376 */     return (ql >= 100.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void improveOrigQualityLevel(float ql) {
/* 382 */     if (ql > 100.0F)
/* 383 */       ql = 100.0F; 
/* 384 */     if (this.originalQL != ql) {
/*     */       
/* 386 */       Connection dbcon = null;
/* 387 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 390 */         this.originalQL = ql;
/* 391 */         dbcon = DbConnector.getZonesDbCon();
/* 392 */         ps = dbcon.prepareStatement("update FENCES set ORIGINALQL=? where ID=?");
/* 393 */         ps.setFloat(1, this.originalQL);
/* 394 */         ps.setInt(2, this.number);
/* 395 */         ps.executeUpdate();
/*     */       }
/* 397 */       catch (SQLException sqx) {
/*     */         
/* 399 */         logger.log(Level.WARNING, "Failed to set original QL to " + ql + " for fence with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 403 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 404 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastUsed(long last) {
/* 412 */     if (this.lastUsed != last) {
/*     */       
/* 414 */       Connection dbcon = null;
/* 415 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 418 */         this.lastUsed = last;
/* 419 */         dbcon = DbConnector.getZonesDbCon();
/* 420 */         ps = dbcon.prepareStatement("update FENCES set LASTMAINTAINED=? where ID=?");
/* 421 */         ps.setLong(1, last);
/* 422 */         ps.setInt(2, this.number);
/* 423 */         ps.executeUpdate();
/*     */       }
/* 425 */       catch (SQLException sqx) {
/*     */         
/* 427 */         logger.log(Level.WARNING, "Failed to set lastUsed to " + last + " for fence with id " + this.number, sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 431 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 432 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean changeColor(int newcolor) {
/* 440 */     if (getColor() != newcolor) {
/*     */       
/* 442 */       this.color = newcolor;
/* 443 */       Connection dbcon = null;
/* 444 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 447 */         dbcon = DbConnector.getZonesDbCon();
/* 448 */         ps = dbcon.prepareStatement("update FENCES set COLOR=? WHERE ID=?");
/* 449 */         ps.setInt(1, newcolor);
/* 450 */         ps.setInt(2, this.number);
/* 451 */         ps.executeUpdate();
/* 452 */         VolaTile tile = Zones.getOrCreateTile(getTileX(), getTileY(), true);
/* 453 */         tile.updateFence(this);
/* 454 */         return true;
/*     */       }
/* 456 */       catch (SQLException sqx) {
/*     */         
/* 458 */         logger.log(Level.WARNING, "Failed to set color to " + getColor() + " for fence with id " + this.number, sqx);
/* 459 */         return true;
/*     */       }
/*     */       finally {
/*     */         
/* 463 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 464 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */     
/* 468 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void savePermissions() {
/* 474 */     Connection dbcon = null;
/* 475 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 478 */       dbcon = DbConnector.getZonesDbCon();
/* 479 */       ps = dbcon.prepareStatement("UPDATE FENCES SET SETTINGS=? WHERE ID=?");
/* 480 */       ps.setLong(1, this.permissions.getPermissions());
/* 481 */       ps.setLong(2, getNumber());
/* 482 */       ps.executeUpdate();
/*     */     }
/* 484 */     catch (SQLException sqx) {
/*     */       
/* 486 */       logger.log(Level.WARNING, "Failed to save settings for fence id " + getNumber(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 490 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 491 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbFence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */