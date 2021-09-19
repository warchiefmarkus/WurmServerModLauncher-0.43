/*     */ package com.wurmonline.server.effects;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public final class DbEffect
/*     */   extends Effect
/*     */   implements CounterTypes
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(DbEffect.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1839666903728378027L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CREATE_EFFECT_SQL = "insert into EFFECTS(OWNER, TYPE, POSX, POSY, POSZ, STARTTIME) values(?,?,?,?,?,?)";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UPDATE_EFFECT_SQL = "update EFFECTS set OWNER=?, TYPE=?, POSX=?, POSY=?, POSZ=? where ID=?";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String GET_EFFECT_SQL = "select * from EFFECTS where ID=?";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DELETE_EFFECT_SQL = "delete from EFFECTS where ID=?";
/*     */ 
/*     */ 
/*     */   
/*     */   DbEffect(long aOwner, short aType, float aPosX, float aPosY, float aPosZ, boolean aSurfaced) {
/*  62 */     super(aOwner, aType, aPosX, aPosY, aPosZ, aSurfaced);
/*     */   }
/*     */ 
/*     */   
/*     */   DbEffect(long aOwner, int aNumber) throws IOException {
/*  67 */     super(aOwner, aNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   DbEffect(int num, long ownerid, short typ, float posx, float posy, float posz, long stime) {
/*  72 */     super(num, ownerid, typ, posx, posy, posz, stime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*  78 */     if (WurmId.getType(getOwner()) != 6) {
/*     */       
/*  80 */       Connection dbcon = null;
/*  81 */       PreparedStatement ps = null;
/*  82 */       ResultSet rs = null;
/*     */       
/*     */       try {
/*  85 */         if (WurmId.getType(getOwner()) == 2 || 
/*  86 */           WurmId.getType(getOwner()) == 19 || 
/*  87 */           WurmId.getType(getOwner()) == 20) {
/*     */           
/*  89 */           dbcon = DbConnector.getItemDbCon();
/*  90 */           if (exists(dbcon)) {
/*     */             
/*  92 */             ps = dbcon.prepareStatement("update EFFECTS set OWNER=?, TYPE=?, POSX=?, POSY=?, POSZ=? where ID=?");
/*     */             
/*  94 */             ps.setLong(1, getOwner());
/*  95 */             ps.setShort(2, getType());
/*  96 */             ps.setFloat(3, getPosX());
/*  97 */             ps.setFloat(4, getPosY());
/*  98 */             ps.setFloat(5, getPosZ());
/*  99 */             ps.setInt(6, getId());
/* 100 */             ps.executeUpdate();
/*     */           }
/*     */           else {
/*     */             
/* 104 */             ps = dbcon.prepareStatement("insert into EFFECTS(OWNER, TYPE, POSX, POSY, POSZ, STARTTIME) values(?,?,?,?,?,?)", 1);
/* 105 */             ps.setLong(1, getOwner());
/* 106 */             ps.setShort(2, getType());
/* 107 */             ps.setFloat(3, getPosX());
/* 108 */             ps.setFloat(4, getPosY());
/* 109 */             ps.setFloat(5, getPosZ());
/* 110 */             ps.setLong(6, getStartTime());
/* 111 */             ps.executeUpdate();
/* 112 */             rs = ps.getGeneratedKeys();
/* 113 */             if (rs.next()) {
/* 114 */               setId(rs.getInt(1));
/*     */             }
/*     */           } 
/* 117 */         } else if (getId() == 0) {
/* 118 */           setId(-Math.abs(Server.rand.nextInt()));
/*     */         } 
/* 120 */       } catch (SQLException sqx) {
/*     */         
/* 122 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 126 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 127 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/* 130 */     } else if (getId() == 0) {
/* 131 */       setId(-Math.abs(Server.rand.nextInt()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void load() throws IOException {
/* 137 */     if (WurmId.getType(getOwner()) != 6) {
/*     */       
/* 139 */       Connection dbcon = null;
/* 140 */       PreparedStatement ps = null;
/* 141 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 144 */         if (WurmId.getType(getOwner()) == 2 || 
/* 145 */           WurmId.getType(getOwner()) == 19 || 
/* 146 */           WurmId.getType(getOwner()) == 20) {
/*     */           
/* 148 */           dbcon = DbConnector.getItemDbCon();
/* 149 */           ps = dbcon.prepareStatement("select * from EFFECTS where ID=?");
/* 150 */           ps.setInt(1, getId());
/* 151 */           rs = ps.executeQuery();
/* 152 */           if (rs.next()) {
/*     */             
/* 154 */             setPosX(rs.getFloat("POSX"));
/* 155 */             setPosY(rs.getFloat("POSY"));
/* 156 */             setPosZ(rs.getFloat("POSZ"));
/* 157 */             setType(rs.getShort("TYPE"));
/* 158 */             setOwner(rs.getLong("OWNER"));
/* 159 */             setStartTime(rs.getLong("STARTTIME"));
/*     */           } else {
/*     */             
/* 162 */             logger.log(Level.WARNING, "Failed to find effect with number " + getId());
/*     */           } 
/*     */         } 
/* 165 */       } catch (SQLException sqx) {
/*     */         
/* 167 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 171 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 172 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void delete() {
/* 180 */     if (WurmId.getType(getOwner()) != 6) {
/*     */       
/* 182 */       Connection dbcon = null;
/* 183 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 186 */         if (WurmId.getType(getOwner()) == 2 || 
/* 187 */           WurmId.getType(getOwner()) == 19 || 
/* 188 */           WurmId.getType(getOwner()) == 20)
/*     */         {
/* 190 */           dbcon = DbConnector.getItemDbCon();
/* 191 */           ps = dbcon.prepareStatement("delete from EFFECTS where ID=?");
/* 192 */           ps.setInt(1, getId());
/* 193 */           ps.executeUpdate();
/*     */         }
/*     */       
/* 196 */       } catch (SQLException sqx) {
/*     */         
/* 198 */         logger.log(Level.WARNING, "Failed to delete effect with id " + getId(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 202 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 203 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 210 */     PreparedStatement ps = null;
/* 211 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 214 */       ps = dbcon.prepareStatement("select * from EFFECTS where ID=?");
/* 215 */       ps.setInt(1, getId());
/* 216 */       rs = ps.executeQuery();
/* 217 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 221 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\effects\DbEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */