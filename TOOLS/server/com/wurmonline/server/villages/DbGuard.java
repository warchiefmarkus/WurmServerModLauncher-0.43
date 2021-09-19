/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ public final class DbGuard
/*     */   extends Guard
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(DbGuard.class.getName());
/*     */   
/*     */   private static final String SET_EXPIREDATE = "UPDATE GUARDS SET EXPIREDATE=? WHERE WURMID=?";
/*     */   private static final String CREATE_GUARD = "INSERT INTO GUARDS (WURMID, VILLAGEID, EXPIREDATE ) VALUES (?,?,?)";
/*     */   static final String DELETE_GUARD = "DELETE FROM GUARDS WHERE WURMID=?";
/*     */   
/*     */   DbGuard(int aVillageId, Creature aCreature, long aExpireDate) {
/*  42 */     super(aVillageId, aCreature, aExpireDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void save() {
/*  52 */     Connection dbcon = null;
/*  53 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  56 */       dbcon = DbConnector.getZonesDbCon();
/*  57 */       ps = dbcon.prepareStatement("INSERT INTO GUARDS (WURMID, VILLAGEID, EXPIREDATE ) VALUES (?,?,?)");
/*  58 */       ps.setLong(1, this.creature.getWurmId());
/*  59 */       ps.setInt(2, this.villageId);
/*  60 */       ps.setLong(3, this.expireDate);
/*  61 */       ps.executeUpdate();
/*     */     }
/*  63 */     catch (SQLException sqx) {
/*     */       
/*  65 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  69 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  70 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setExpireDate(long newDate) {
/*  81 */     if (this.expireDate != newDate) {
/*     */       
/*  83 */       this.expireDate = newDate;
/*  84 */       Connection dbcon = null;
/*  85 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/*  88 */         dbcon = DbConnector.getZonesDbCon();
/*  89 */         ps = dbcon.prepareStatement("UPDATE GUARDS SET EXPIREDATE=? WHERE WURMID=?");
/*  90 */         ps.setLong(1, this.expireDate);
/*  91 */         ps.setLong(2, this.creature.getWurmId());
/*  92 */         ps.executeUpdate();
/*     */       }
/*  94 */       catch (SQLException sqx) {
/*     */         
/*  96 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 100 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 101 */         DbConnector.returnConnection(dbcon);
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
/*     */   void delete() {
/* 113 */     Connection dbcon = null;
/* 114 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 117 */       dbcon = DbConnector.getZonesDbCon();
/* 118 */       ps = dbcon.prepareStatement("DELETE FROM GUARDS WHERE WURMID=?");
/* 119 */       ps.setLong(1, this.creature.getWurmId());
/* 120 */       ps.executeUpdate();
/*     */     }
/* 122 */     catch (SQLException sqx) {
/*     */       
/* 124 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 128 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 129 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbGuard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */