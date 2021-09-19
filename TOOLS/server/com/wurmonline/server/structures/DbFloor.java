/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.tutorial.MissionTargets;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.shared.constants.StructureConstants;
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
/*     */ 
/*     */ public class DbFloor
/*     */   extends Floor
/*     */ {
/*     */   private static final String CREATE_FLOOR = "INSERT INTO FLOORS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL, DAMAGE, STRUCTURE, TILEX, TILEY, STATE,COLOR, MATERIAL,HEIGHTOFFSET,LAYER,DIR) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   private static final String UPDATE_FLOOR = "UPDATE FLOORS SET TYPE=?, LASTMAINTAINED =?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STRUCTURE=?, STATE=?,MATERIAL=?,HEIGHTOFFSET=?,DIR=? WHERE ID=?";
/*     */   private static final String GET_FLOOR = "SELECT * FROM FLOORS WHERE ID=?";
/*     */   private static final String DELETE_FLOOR = "DELETE FROM FLOORS WHERE ID=?";
/*     */   private static final String SET_DAMAGE = "UPDATE FLOORS SET DAMAGE=? WHERE ID=?";
/*     */   private static final String SET_QUALITY_LEVEL = "UPDATE FLOORS SET CURRENTQL=? WHERE ID=?";
/*     */   private static final String SET_STATE = "UPDATE FLOORS SET STATE=?,MATERIAL=? WHERE ID=?";
/*     */   private static final String SET_LAST_USED = "UPDATE FLOORS SET LASTMAINTAINED=? WHERE ID=?";
/*     */   private static final String SET_SETTINGS = "UPDATE FLOORS SET SETTINGS=? WHERE ID=?";
/*  56 */   private static final Logger logger = Logger.getLogger(DbWall.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFence() {
/*  66 */     return false;
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
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbFloor(int id, StructureConstants.FloorType floorType, int tilex, int tiley, byte aDbState, int heightOffset, float currentQl, long structureId, StructureConstants.FloorMaterial floorMaterial, int layer, float origQL, float aDamage, long lastmaintained, byte dir) {
/*  84 */     super(id, floorType, tilex, tiley, aDbState, heightOffset, currentQl, structureId, floorMaterial, layer, origQL, aDamage, lastmaintained, dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbFloor(StructureConstants.FloorType floorType, int tilex, int tiley, int heightOffset, float qualityLevel, long structure, StructureConstants.FloorMaterial material, int layer) {
/*  91 */     super(floorType, tilex, tiley, heightOffset, qualityLevel, structure, material, layer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setState(byte newState) {
/*  97 */     if (this.dbState != newState) {
/*     */       
/*  99 */       Connection dbcon = null;
/* 100 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 103 */         this.dbState = newState;
/* 104 */         dbcon = DbConnector.getZonesDbCon();
/* 105 */         ps = dbcon.prepareStatement("UPDATE FLOORS SET STATE=?,MATERIAL=? WHERE ID=?");
/* 106 */         ps.setByte(1, this.dbState);
/* 107 */         ps.setByte(2, getMaterial().getCode());
/* 108 */         ps.setInt(3, getNumber());
/* 109 */         ps.executeUpdate();
/*     */       
/*     */       }
/* 112 */       catch (SQLException sqx) {
/*     */         
/* 114 */         logger.log(Level.WARNING, "Failed to set state to " + newState + " for floor with id " + getNumber(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 118 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 119 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 127 */     Connection dbcon = null;
/* 128 */     PreparedStatement ps = null;
/* 129 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 132 */       dbcon = DbConnector.getZonesDbCon();
/* 133 */       if (exists(dbcon))
/*     */       {
/* 135 */         ps = dbcon.prepareStatement("UPDATE FLOORS SET TYPE=?, LASTMAINTAINED =?, CURRENTQL=?, ORIGINALQL=?,DAMAGE=?, STRUCTURE=?, STATE=?,MATERIAL=?,HEIGHTOFFSET=?,DIR=? WHERE ID=?");
/* 136 */         ps.setByte(1, getType().getCode());
/* 137 */         ps.setLong(2, getLastUsed());
/* 138 */         ps.setFloat(3, getCurrentQL());
/* 139 */         ps.setFloat(4, getOriginalQL());
/* 140 */         ps.setFloat(5, getDamage());
/* 141 */         ps.setLong(6, getStructureId());
/* 142 */         ps.setByte(7, getState());
/* 143 */         ps.setByte(8, getMaterial().getCode());
/* 144 */         ps.setInt(9, getHeightOffset());
/* 145 */         ps.setByte(10, getDir());
/* 146 */         ps.setInt(11, getNumber());
/*     */         
/* 148 */         ps.executeUpdate();
/*     */       }
/*     */       else
/*     */       {
/* 152 */         ps = dbcon.prepareStatement("INSERT INTO FLOORS(TYPE, LASTMAINTAINED , CURRENTQL, ORIGINALQL, DAMAGE, STRUCTURE, TILEX, TILEY, STATE,COLOR, MATERIAL,HEIGHTOFFSET,LAYER,DIR) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
/* 153 */         ps.setByte(1, getType().getCode());
/* 154 */         ps.setLong(2, getLastUsed());
/* 155 */         ps.setFloat(3, getCurrentQL());
/* 156 */         ps.setFloat(4, getOriginalQL());
/* 157 */         ps.setFloat(5, getDamage());
/* 158 */         ps.setLong(6, getStructureId());
/* 159 */         ps.setInt(7, getTileX());
/* 160 */         ps.setInt(8, getTileY());
/* 161 */         ps.setByte(9, getState());
/* 162 */         ps.setInt(10, getColor());
/* 163 */         ps.setByte(11, getMaterial().getCode());
/* 164 */         ps.setInt(12, getHeightOffset());
/* 165 */         ps.setByte(13, getLayer());
/* 166 */         ps.setByte(14, getDir());
/* 167 */         ps.executeUpdate();
/* 168 */         rs = ps.getGeneratedKeys();
/* 169 */         if (rs.next()) {
/* 170 */           setNumber(rs.getInt(1));
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 177 */     catch (SQLException sqx) {
/*     */       
/* 179 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 183 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 184 */       DbConnector.returnConnection(dbcon);
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
/* 198 */     boolean forcePlan = false;
/* 199 */     if (isIndestructible())
/* 200 */       return false; 
/* 201 */     if (aDamage >= 100.0F) {
/*     */       
/* 203 */       VolaTile tile = getTile();
/* 204 */       if (tile != null) {
/*     */         
/* 206 */         Structure struct = tile.getStructure();
/* 207 */         if (struct != null)
/*     */         {
/* 209 */           if (struct.wouldCreateFlyingStructureIfRemoved(this))
/*     */           {
/* 211 */             forcePlan = true;
/*     */           }
/*     */         }
/*     */       } 
/* 215 */       if (forcePlan) {
/*     */         
/* 217 */         setFloorState(StructureConstants.FloorState.PLANNING);
/* 218 */         setQualityLevel(1.0F);
/* 219 */         if (tile != null)
/* 220 */           tile.updateFloor(this); 
/*     */       } 
/*     */     } 
/* 223 */     if (this.damage != aDamage) {
/*     */       
/* 225 */       boolean updateState = false;
/* 226 */       if ((this.damage >= 60.0F && aDamage < 60.0F) || (this.damage < 60.0F && aDamage >= 60.0F))
/*     */       {
/* 228 */         updateState = true;
/*     */       }
/* 230 */       this.damage = aDamage;
/* 231 */       if (forcePlan)
/* 232 */         this.damage = 0.0F; 
/* 233 */       if (this.damage < 100.0F) {
/*     */         
/* 235 */         Connection dbcon = null;
/* 236 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 239 */           dbcon = DbConnector.getZonesDbCon();
/* 240 */           ps = dbcon.prepareStatement("UPDATE FLOORS SET DAMAGE=? WHERE ID=?");
/* 241 */           ps.setFloat(1, getDamage());
/* 242 */           ps.setInt(2, getNumber());
/* 243 */           ps.executeUpdate();
/*     */         }
/* 245 */         catch (SQLException sqx) {
/*     */           
/* 247 */           logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */         }
/*     */         finally {
/*     */           
/* 251 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 252 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */         
/* 255 */         if (updateState)
/*     */         {
/* 257 */           VolaTile tile = getTile();
/* 258 */           if (tile != null)
/*     */           {
/* 260 */             getTile().updateFloorDamageState(this);
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 266 */         VolaTile t = getTile();
/* 267 */         if (t != null) {
/* 268 */           t.removeFloor(this);
/*     */         }
/* 270 */         delete();
/*     */       } 
/*     */     } 
/* 273 */     return (this.damage >= 100.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastUsed(long now) {
/* 279 */     if (this.lastUsed != now) {
/*     */       
/* 281 */       this.lastUsed = now;
/* 282 */       Connection dbcon = null;
/* 283 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 286 */         dbcon = DbConnector.getZonesDbCon();
/* 287 */         ps = dbcon.prepareStatement("UPDATE FLOORS SET LASTMAINTAINED=? WHERE ID=?");
/* 288 */         ps.setLong(1, this.lastUsed);
/* 289 */         ps.setInt(2, getNumber());
/* 290 */         ps.executeUpdate();
/*     */       }
/* 292 */       catch (SQLException sqx) {
/*     */         
/* 294 */         logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 298 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 299 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setQualityLevel(float ql) {
/* 307 */     if (ql > 100.0F)
/* 308 */       ql = 100.0F; 
/* 309 */     if (this.currentQL != ql) {
/*     */       
/* 311 */       Connection dbcon = null;
/* 312 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 315 */         this.currentQL = ql;
/* 316 */         dbcon = DbConnector.getZonesDbCon();
/* 317 */         ps = dbcon.prepareStatement("UPDATE FLOORS SET CURRENTQL=? WHERE ID=?");
/* 318 */         ps.setFloat(1, this.currentQL);
/* 319 */         ps.setInt(2, getNumber());
/* 320 */         ps.executeUpdate();
/*     */       }
/* 322 */       catch (SQLException sqx) {
/*     */         
/* 324 */         logger.log(Level.WARNING, getName() + ", " + getNumber() + " " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 328 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 329 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/* 332 */     return (ql >= 100.0F);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 386 */     PreparedStatement ps = null;
/* 387 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 390 */       ps = dbcon.prepareStatement("SELECT * FROM FLOORS WHERE ID=?");
/* 391 */       ps.setInt(1, getNumber());
/* 392 */       rs = ps.executeQuery();
/* 393 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 397 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 404 */     MissionTargets.destroyMissionTarget(getId(), true);
/* 405 */     MethodsHighways.removeNearbyMarkers(this);
/*     */     
/* 407 */     Connection dbcon = null;
/* 408 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 411 */       dbcon = DbConnector.getZonesDbCon();
/* 412 */       ps = dbcon.prepareStatement("DELETE FROM FLOORS WHERE ID=?");
/* 413 */       ps.setInt(1, getNumber());
/* 414 */       ps.executeUpdate();
/*     */     }
/* 416 */     catch (SQLException sqx) {
/*     */       
/* 418 */       logger.log(Level.WARNING, "Failed to delete floor with id " + getNumber(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 422 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 423 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void savePermissions() {
/* 430 */     Connection dbcon = null;
/* 431 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 434 */       dbcon = DbConnector.getZonesDbCon();
/* 435 */       ps = dbcon.prepareStatement("UPDATE FLOORS SET SETTINGS=? WHERE ID=?");
/* 436 */       ps.setLong(1, this.permissions.getPermissions());
/* 437 */       ps.setLong(2, getNumber());
/* 438 */       ps.executeUpdate();
/*     */     }
/* 440 */     catch (SQLException sqx) {
/*     */       
/* 442 */       logger.log(Level.WARNING, "Failed to save settings for floor with id " + getNumber(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 446 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 447 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbFloor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */