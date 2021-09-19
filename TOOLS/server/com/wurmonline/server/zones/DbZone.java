/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.structures.DbFence;
/*     */ import com.wurmonline.server.structures.DbFenceGate;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*     */ import com.wurmonline.shared.constants.StructureStateEnum;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
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
/*     */ final class DbZone
/*     */   extends Zone
/*     */   implements CounterTypes
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(DbZone.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String GET_FENCES = "Select * from FENCES where ZONEID=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DELETE_FENCES = "DELETE from FENCES where ZONEID=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DbZone(int aStartX, int aEndX, int aStartY, int aEndY, boolean aIsOnSurface) throws IOException {
/*  75 */     super(aStartX, aEndX, aStartY, aEndY, aIsOnSurface);
/*  76 */     this.zoneWatchers = new HashSet<>();
/*  77 */     this.structures = new HashSet<>();
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
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void loadFences() throws IOException {
/* 202 */     Connection dbcon = null;
/* 203 */     PreparedStatement ps = null;
/* 204 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 207 */       dbcon = DbConnector.getZonesDbCon();
/* 208 */       ps = dbcon.prepareStatement("Select * from FENCES where ZONEID=?");
/* 209 */       ps.setInt(1, this.id);
/* 210 */       rs = ps.executeQuery();
/* 211 */       while (rs.next()) {
/*     */         
/* 213 */         int fid = -10;
/*     */         
/*     */         try {
/* 216 */           fid = rs.getInt("ID");
/* 217 */           int tilex = rs.getInt("TILEX");
/* 218 */           int tiley = rs.getInt("TILEY");
/* 219 */           float currentQL = rs.getFloat("ORIGINALQL");
/* 220 */           float originalQL = rs.getFloat("CURRENTQL");
/* 221 */           long lastUsed = rs.getLong("LASTMAINTAINED");
/* 222 */           StructureConstantsEnum type = StructureConstantsEnum.getEnumByValue(rs.getShort("TYPE"));
/* 223 */           StructureStateEnum state = StructureStateEnum.getStateByValue(rs.getByte("STATE"));
/* 224 */           int color = rs.getInt("COLOR");
/* 225 */           int dir = rs.getByte("DIR");
/* 226 */           float damage = rs.getFloat("DAMAGE");
/* 227 */           int heightOffset = rs.getInt("HEIGHTOFFSET");
/* 228 */           int layer = rs.getInt("LAYER");
/* 229 */           int settings = rs.getInt("SETTINGS");
/* 230 */           DbFence dbFence = new DbFence(fid, type, state, color, tilex, tiley, heightOffset, currentQL, originalQL, lastUsed, (dir == 0) ? Tiles.TileBorderDirection.DIR_HORIZ : Tiles.TileBorderDirection.DIR_DOWN, this.id, this.isOnSurface, damage, layer, settings);
/*     */ 
/*     */ 
/*     */           
/* 234 */           if (dir == 3 || dir == 1) {
/*     */ 
/*     */             
/*     */             try {
/* 238 */               dbFence.delete();
/*     */             }
/* 240 */             catch (Exception ex) {
/*     */               
/* 242 */               logger.log(Level.WARNING, "Failed to delete fence " + ex.getMessage(), ex);
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/* 247 */           addFence((Fence)dbFence);
/* 248 */           if (dbFence.isDoor())
/*     */           {
/* 250 */             if (dbFence.isFinished())
/*     */             {
/* 252 */               DbFenceGate dbFenceGate = new DbFenceGate((Fence)dbFence);
/* 253 */               dbFenceGate.addToTiles();
/*     */             }
/*     */           
/*     */           }
/*     */         }
/* 258 */         catch (SQLException iox) {
/*     */           
/* 260 */           logger.log(Level.WARNING, "Failed to load fence with id " + fid, iox);
/*     */         }
/*     */       
/*     */       } 
/* 264 */     } catch (SQLException sqx) {
/*     */       
/* 266 */       logger.log(Level.WARNING, "Failed to load fences for zone with id " + this.id, sqx);
/* 267 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 271 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 272 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */   
/*     */   void save() throws IOException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\DbZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */