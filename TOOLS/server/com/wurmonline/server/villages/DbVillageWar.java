/*    */ package com.wurmonline.server.villages;
/*    */ 
/*    */ import com.wurmonline.server.DbConnector;
/*    */ import com.wurmonline.server.utils.DbUtilities;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DbVillageWar
/*    */   extends VillageWar
/*    */ {
/* 35 */   private static final Logger logger = Logger.getLogger(DbVillageWar.class.getName());
/*    */   
/*    */   private static final String createWar = "INSERT INTO VILLAGEWARS (VILLONE, VILLTWO) VALUES (?,?)";
/*    */   private static final String deleteWar = "DELETE FROM VILLAGEWARS WHERE VILLONE=? AND VILLTWO=?";
/*    */   
/*    */   DbVillageWar(Village vone, Village vtwo) {
/* 41 */     super(vone, vtwo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void save() {
/* 51 */     Connection dbcon = null;
/* 52 */     PreparedStatement ps = null;
/*    */     
/*    */     try {
/* 55 */       dbcon = DbConnector.getZonesDbCon();
/* 56 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGEWARS (VILLONE, VILLTWO) VALUES (?,?)");
/* 57 */       ps.setInt(1, this.villone.getId());
/* 58 */       ps.setInt(2, this.villtwo.getId());
/* 59 */       ps.executeUpdate();
/*    */     }
/* 61 */     catch (SQLException sqx) {
/*    */       
/* 63 */       logger.log(Level.WARNING, "Failed to create war between " + this.villone.getName() + " and " + this.villtwo.getName(), sqx);
/*    */     }
/*    */     finally {
/*    */       
/* 67 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 68 */       DbConnector.returnConnection(dbcon);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void delete() {
/* 79 */     Connection dbcon = null;
/* 80 */     PreparedStatement ps = null;
/*    */     
/*    */     try {
/* 83 */       dbcon = DbConnector.getZonesDbCon();
/* 84 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEWARS WHERE VILLONE=? AND VILLTWO=?");
/* 85 */       ps.setInt(1, this.villone.getId());
/* 86 */       ps.setInt(2, this.villtwo.getId());
/* 87 */       ps.executeUpdate();
/*    */     }
/* 89 */     catch (SQLException sqx) {
/*    */       
/* 91 */       logger.log(Level.WARNING, "Failed to delete war between " + this.villone.getName() + " and " + this.villtwo.getName(), sqx);
/*    */     }
/*    */     finally {
/*    */       
/* 95 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 96 */       DbConnector.returnConnection(dbcon);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbVillageWar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */