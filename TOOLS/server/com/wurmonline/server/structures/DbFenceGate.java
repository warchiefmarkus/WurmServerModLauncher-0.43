/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.players.PermissionsHistories;
/*     */ import com.wurmonline.server.utils.DbUtilities;
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
/*     */ public final class DbFenceGate
/*     */   extends FenceGate
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(DbFenceGate.class.getName());
/*     */ 
/*     */   
/*     */   private static final String GET_GATE = "SELECT * FROM GATES WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String EXISTS_GATE = "SELECT 1 FROM GATES WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String CREATE_GATE = "INSERT INTO GATES (NAME,OPENTIME,CLOSETIME,LOCKID,VILLAGE,ID) VALUES(?,?,?,?,?,?)";
/*     */   
/*     */   private static final String UPDATE_GATE = "UPDATE GATES SET NAME=?,OPENTIME=?,CLOSETIME=?,LOCKID=?,VILLAGE=? WHERE ID=?";
/*     */   
/*     */   private static final String DELETE_GATE = "DELETE FROM GATES WHERE ID=?";
/*     */   
/*     */   private static final String SET_NAME = "UPDATE GATES SET NAME=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_OPEN_TIME = "UPDATE GATES SET OPENTIME=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_CLOSE_TIME = "UPDATE GATES SET CLOSETIME=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_LOCKID = "UPDATE GATES SET LOCKID=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_VILLAGEID = "UPDATE GATES SET VILLAGE=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   public DbFenceGate(Fence aFence) {
/*  64 */     super(aFence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*  73 */     Connection dbcon = null;
/*  74 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  77 */       dbcon = DbConnector.getZonesDbCon();
/*  78 */       String string = "INSERT INTO GATES (NAME,OPENTIME,CLOSETIME,LOCKID,VILLAGE,ID) VALUES(?,?,?,?,?,?)";
/*  79 */       if (exists(dbcon))
/*  80 */         string = "UPDATE GATES SET NAME=?,OPENTIME=?,CLOSETIME=?,LOCKID=?,VILLAGE=? WHERE ID=?"; 
/*  81 */       ps = dbcon.prepareStatement(string);
/*  82 */       ps.setString(1, getName());
/*  83 */       ps.setByte(2, (byte)getOpenTime());
/*  84 */       ps.setByte(3, (byte)getCloseTime());
/*  85 */       ps.setLong(4, this.lock);
/*  86 */       ps.setInt(5, getVillageId());
/*  87 */       ps.setLong(6, getFence().getId());
/*  88 */       ps.executeUpdate();
/*     */     }
/*  90 */     catch (SQLException ex) {
/*     */       
/*  92 */       logger.log(Level.WARNING, "Failed to save gate with id " + getFence().getId(), ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/*  97 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  98 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 104 */     PreparedStatement ps = null;
/* 105 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 108 */       ps = dbcon.prepareStatement("SELECT 1 FROM GATES WHERE ID=?");
/* 109 */       ps.setFetchSize(1);
/* 110 */       ps.setLong(1, getFence().getId());
/* 111 */       rs = ps.executeQuery();
/* 112 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 116 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException {
/* 126 */     Connection dbcon = null;
/* 127 */     PreparedStatement ps = null;
/* 128 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 131 */       dbcon = DbConnector.getZonesDbCon();
/* 132 */       ps = dbcon.prepareStatement("SELECT * FROM GATES WHERE ID=?");
/* 133 */       ps.setFetchSize(1);
/* 134 */       ps.setLong(1, getFence().getId());
/* 135 */       rs = ps.executeQuery();
/* 136 */       if (rs.next()) {
/*     */         
/* 138 */         this.openTime = rs.getByte("OPENTIME");
/* 139 */         this.closeTime = rs.getByte("CLOSETIME");
/* 140 */         this.name = rs.getString("NAME");
/* 141 */         this.lock = rs.getLong("LOCKID");
/* 142 */         this.villageId = rs.getInt("VILLAGE");
/*     */       } else {
/*     */         
/* 145 */         save();
/*     */       } 
/* 147 */     } catch (SQLException ex) {
/*     */       
/* 149 */       logger.log(Level.WARNING, "Failed to load gate with id " + getFence().getId(), ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 154 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 155 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 165 */     gates.remove(new Long(getFence().getId()));
/* 166 */     DoorSettings.remove(getFence().getId());
/* 167 */     PermissionsHistories.remove(getFence().getId());
/* 168 */     Connection dbcon = null;
/* 169 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 172 */       dbcon = DbConnector.getZonesDbCon();
/* 173 */       ps = dbcon.prepareStatement("DELETE FROM GATES WHERE ID=?");
/* 174 */       ps.setLong(1, getFence().getId());
/* 175 */       ps.executeUpdate();
/*     */     }
/* 177 */     catch (SQLException sqx) {
/*     */       
/* 179 */       logger.log(Level.WARNING, "Failed to delete fencegate with id " + getFence().getId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 183 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 184 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 186 */     if (this.lock != -10L) {
/*     */       
/* 188 */       Items.decay(this.lock, null);
/* 189 */       this.lock = -10L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpenTime(int time) {
/* 196 */     if (getOpenTime() != time) {
/*     */       
/* 198 */       Connection dbcon = null;
/* 199 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 202 */         this.openTime = time;
/* 203 */         dbcon = DbConnector.getZonesDbCon();
/* 204 */         ps = dbcon.prepareStatement("UPDATE GATES SET OPENTIME=? WHERE ID=?");
/* 205 */         ps.setByte(1, (byte)getOpenTime());
/* 206 */         ps.setLong(2, getFence().getId());
/* 207 */         ps.executeUpdate();
/*     */       }
/* 209 */       catch (SQLException sqx) {
/*     */         
/* 211 */         logger.log(Level.WARNING, "Failed to set opentime to " + getOpenTime() + " for fencegate with id " + 
/* 212 */             getFence().getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 216 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 217 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseTime(int time) {
/* 225 */     if (getCloseTime() != time) {
/*     */       
/* 227 */       Connection dbcon = null;
/* 228 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 231 */         this.closeTime = time;
/* 232 */         dbcon = DbConnector.getZonesDbCon();
/* 233 */         ps = dbcon.prepareStatement("UPDATE GATES SET CLOSETIME=? WHERE ID=?");
/* 234 */         ps.setByte(1, (byte)getCloseTime());
/* 235 */         ps.setLong(2, getFence().getId());
/* 236 */         ps.executeUpdate();
/*     */       }
/* 238 */       catch (SQLException sqx) {
/*     */         
/* 240 */         logger.log(Level.WARNING, "Failed to set closetime to " + getCloseTime() + " fencegate with id " + 
/* 241 */             getFence().getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 245 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 246 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String aName) {
/* 254 */     String newname = aName.substring(0, Math.min(39, aName.length()));
/* 255 */     if (!getName().equals(newname)) {
/*     */       
/* 257 */       Connection dbcon = null;
/* 258 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 261 */         this.name = newname;
/* 262 */         dbcon = DbConnector.getZonesDbCon();
/* 263 */         ps = dbcon.prepareStatement("UPDATE GATES SET NAME=? WHERE ID=?");
/* 264 */         ps.setString(1, getName());
/* 265 */         ps.setLong(2, getFence().getId());
/* 266 */         ps.executeUpdate();
/*     */       }
/* 268 */       catch (SQLException sqx) {
/*     */         
/* 270 */         logger.log(Level.WARNING, "Failed to set name to " + 
/* 271 */             getName() + " for fencegate with id " + getFence().getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 275 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 276 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLock(long lockid) {
/* 284 */     if (this.lock != lockid) {
/*     */       
/* 286 */       Connection dbcon = null;
/* 287 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 290 */         this.lock = lockid;
/* 291 */         dbcon = DbConnector.getZonesDbCon();
/* 292 */         ps = dbcon.prepareStatement("UPDATE GATES SET LOCKID=? WHERE ID=?");
/* 293 */         ps.setLong(1, lockid);
/* 294 */         ps.setLong(2, getFence().getId());
/* 295 */         ps.executeUpdate();
/*     */       }
/* 297 */       catch (SQLException sqx) {
/*     */         
/* 299 */         logger.log(Level.WARNING, "Failed to set lock for fencegate with id " + getFence().getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 303 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 304 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVillageId(int newVillageId) {
/* 312 */     if (getVillageId() != newVillageId) {
/*     */       
/* 314 */       Connection dbcon = null;
/* 315 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 318 */         this.villageId = newVillageId;
/* 319 */         dbcon = DbConnector.getZonesDbCon();
/* 320 */         ps = dbcon.prepareStatement("UPDATE GATES SET VILLAGE=? WHERE ID=?");
/* 321 */         ps.setString(1, getName());
/* 322 */         ps.setLong(2, getVillageId());
/* 323 */         ps.executeUpdate();
/*     */       }
/* 325 */       catch (SQLException sqx) {
/*     */         
/* 327 */         logger.log(Level.WARNING, "Failed to set villageId to " + 
/* 328 */             getVillageId() + " for fencegate with id " + getFence().getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 332 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 333 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItem() {
/* 341 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbFenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */