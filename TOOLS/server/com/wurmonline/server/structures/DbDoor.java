/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
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
/*     */ public final class DbDoor
/*     */   extends Door
/*     */   implements MiscConstants
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(DbDoor.class.getName());
/*     */ 
/*     */   
/*     */   private static final String GET_DOOR = "SELECT * FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?";
/*     */   
/*     */   private static final String EXISTS_DOOR = "SELECT 1 FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?";
/*     */   
/*     */   private static final String CREATE_DOOR = "INSERT INTO DOORS (LOCKID,NAME,SETTINGS,STRUCTURE,INNERWALL) VALUES(?,?,?,?,?)";
/*     */   
/*     */   private static final String UPDATE_DOOR = "UPDATE DOORS SET LOCKID=?,NAME=?,SETTINGS=? WHERE STRUCTURE=? AND INNERWALL=?";
/*     */   
/*     */   private static final String DELETE_DOOR = "DELETE FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?";
/*     */   
/*     */   private static final String SET_NAME = "UPDATE DOORS SET NAME=? WHERE INNERWALL=?";
/*     */ 
/*     */   
/*     */   public DbDoor(Wall aWall) {
/*  53 */     super(aWall);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*  59 */     Connection dbcon = null;
/*  60 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  63 */       dbcon = DbConnector.getZonesDbCon();
/*  64 */       String string = "INSERT INTO DOORS (LOCKID,NAME,SETTINGS,STRUCTURE,INNERWALL) VALUES(?,?,?,?,?)";
/*  65 */       if (exists(dbcon))
/*  66 */         string = "UPDATE DOORS SET LOCKID=?,NAME=?,SETTINGS=? WHERE STRUCTURE=? AND INNERWALL=?"; 
/*  67 */       ps = dbcon.prepareStatement(string);
/*  68 */       ps.setLong(1, this.lock);
/*  69 */       ps.setString(2, this.name);
/*  70 */       ps.setInt(3, 0);
/*  71 */       ps.setLong(4, this.structure);
/*  72 */       long iid = this.wall.getId();
/*  73 */       ps.setLong(5, iid);
/*  74 */       ps.executeUpdate();
/*     */     }
/*  76 */     catch (SQLException ex) {
/*     */       
/*  78 */       logger.log(Level.WARNING, "Failed to save door for structure with id " + this.structure, ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/*  83 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  84 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void load() throws IOException {
/*  91 */     Connection dbcon = null;
/*  92 */     PreparedStatement ps = null;
/*  93 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  96 */       dbcon = DbConnector.getZonesDbCon();
/*  97 */       ps = dbcon.prepareStatement("SELECT * FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?");
/*  98 */       ps.setLong(1, this.structure);
/*  99 */       ps.setLong(2, this.wall.getId());
/* 100 */       rs = ps.executeQuery();
/* 101 */       if (rs.next()) {
/*     */         
/* 103 */         this.lock = rs.getLong("LOCKID");
/* 104 */         this.name = rs.getString("NAME");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 109 */         save();
/*     */       } 
/* 111 */     } catch (SQLException ex) {
/*     */       
/* 113 */       logger.log(Level.WARNING, "Failed to load door for structure with id " + this.structure, ex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 118 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 119 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 125 */     PreparedStatement ps = null;
/* 126 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 129 */       ps = dbcon.prepareStatement("SELECT 1 FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?");
/* 130 */       ps.setLong(1, this.structure);
/* 131 */       ps.setLong(2, this.wall.getId());
/* 132 */       rs = ps.executeQuery();
/* 133 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 137 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 144 */     Connection dbcon = null;
/* 145 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 148 */       dbcon = DbConnector.getZonesDbCon();
/* 149 */       ps = dbcon.prepareStatement("DELETE FROM DOORS WHERE STRUCTURE=? AND INNERWALL=?");
/* 150 */       ps.setLong(1, this.structure);
/* 151 */       ps.setLong(2, this.wall.getId());
/* 152 */       ps.executeUpdate();
/*     */     }
/* 154 */     catch (SQLException sqx) {
/*     */       
/* 156 */       logger.log(Level.WARNING, "Failed to delete wall for structure with id " + this.structure, sqx);
/*     */     }
/* 158 */     catch (Exception ex) {
/*     */       
/* 160 */       logger.log(Level.WARNING, this.structure + ":" + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 164 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 165 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 167 */     if (this.lock != -10L) {
/*     */       
/* 169 */       Items.decay(this.lock, null);
/* 170 */       this.lock = -10L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String aName) {
/* 177 */     String newname = aName.substring(0, Math.min(39, aName.length()));
/* 178 */     if (!getName().equals(newname)) {
/*     */       
/* 180 */       Connection dbcon = null;
/* 181 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 184 */         setNewName(newname);
/* 185 */         dbcon = DbConnector.getZonesDbCon();
/* 186 */         ps = dbcon.prepareStatement("UPDATE DOORS SET NAME=? WHERE INNERWALL=?");
/* 187 */         ps.setString(1, getName());
/* 188 */         ps.setLong(2, this.wall.getId());
/* 189 */         ps.executeUpdate();
/*     */       }
/* 191 */       catch (SQLException sqx) {
/*     */         
/* 193 */         logger.log(Level.WARNING, "Failed to set name to " + 
/* 194 */             getName() + " for door with innerwall of " + this.wall.getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 198 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 199 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItem() {
/* 207 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\DbDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */