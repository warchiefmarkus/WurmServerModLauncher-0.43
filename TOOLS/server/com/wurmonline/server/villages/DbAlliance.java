/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
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
/*     */ 
/*     */ 
/*     */ public final class DbAlliance
/*     */   extends Alliance
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(DbAlliance.class.getName());
/*     */   
/*     */   private static final String createAlliance = "INSERT INTO ALLIANCES (VILLONE, VILLTWO) VALUES (?,?)";
/*     */   private static final String deleteAlliance = "DELETE FROM ALLIANCES WHERE VILLONE=? AND VILLTWO=?";
/*     */   
/*     */   DbAlliance(Village vone, Village vtwo) {
/*  42 */     super(vone, vtwo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void save() {
/*  53 */     Connection dbcon = null;
/*  54 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  57 */       dbcon = DbConnector.getZonesDbCon();
/*  58 */       ps = dbcon.prepareStatement("INSERT INTO ALLIANCES (VILLONE, VILLTWO) VALUES (?,?)");
/*  59 */       ps.setInt(1, this.villone.getId());
/*  60 */       ps.setInt(2, this.villtwo.getId());
/*  61 */       ps.executeUpdate();
/*     */     }
/*  63 */     catch (SQLException sqx) {
/*     */       
/*  65 */       logger.log(Level.WARNING, "Failed to create alliance between " + this.villone.getName() + " and " + this.villtwo.getName(), sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/*  70 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  71 */       DbConnector.returnConnection(dbcon);
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
/*     */   void delete() {
/*  83 */     Connection dbcon = null;
/*  84 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  87 */       dbcon = DbConnector.getZonesDbCon();
/*  88 */       ps = dbcon.prepareStatement("DELETE FROM ALLIANCES WHERE VILLONE=? AND VILLTWO=?");
/*  89 */       ps.setInt(1, this.villone.getId());
/*  90 */       ps.setInt(2, this.villtwo.getId());
/*  91 */       ps.executeUpdate();
/*     */     }
/*  93 */     catch (SQLException sqx) {
/*     */       
/*  95 */       logger.log(Level.WARNING, "Failed to delete alliance between " + this.villone.getName() + " and " + this.villtwo.getName(), sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 100 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 101 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbAlliance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */