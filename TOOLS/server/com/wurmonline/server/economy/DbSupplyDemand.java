/*     */ package com.wurmonline.server.economy;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
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
/*     */ final class DbSupplyDemand
/*     */   extends SupplyDemand
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(DbSupplyDemand.class.getName());
/*     */ 
/*     */   
/*     */   private static final String UPDATE_BOUGHT_ITEMS = "UPDATE SUPPLYDEMAND SET ITEMSBOUGHT=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String UPDATE_SOLD_ITEMS = "UPDATE SUPPLYDEMAND SET ITEMSSOLD=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String CHECK_SUPLLY_DEMAND = "SELECT ID FROM SUPPLYDEMAND WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String RESET_SUPPLY_DEMAND = "DELETE FROM SUPPLYDEMAND WHERE ID=?";
/*     */ 
/*     */   
/*     */   private static final String CREATE_SUPPLY_DEMAND = "INSERT INTO SUPPLYDEMAND (ID, ITEMSBOUGHT,ITEMSSOLD, LASTPOLLED) VALUES(?,?,?,?)";
/*     */ 
/*     */   
/*     */   DbSupplyDemand(int aId, int aItemsBought, int aItemsSold) {
/*  58 */     super(aId, aItemsBought, aItemsSold);
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
/*     */   DbSupplyDemand(int aId, int aItemsBought, int aItemsSold, long aLastPolled) {
/*  71 */     super(aId, aItemsBought, aItemsSold, aLastPolled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean supplyDemandExists() {
/*  82 */     Connection dbcon = null;
/*  83 */     PreparedStatement ps = null;
/*  84 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  87 */       dbcon = DbConnector.getEconomyDbCon();
/*  88 */       ps = dbcon.prepareStatement("SELECT ID FROM SUPPLYDEMAND WHERE ID=?");
/*  89 */       ps.setInt(1, this.id);
/*  90 */       rs = ps.executeQuery();
/*  91 */       return rs.next();
/*     */     }
/*  93 */     catch (SQLException sqx) {
/*     */       
/*  95 */       logger.log(Level.WARNING, "Failed to check if supplyDemandExists for ID: " + this.id + " due to " + sqx.getMessage(), sqx);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 100 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 101 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updateItemsBoughtByTraders(int items) {
/* 114 */     if (supplyDemandExists()) {
/*     */       
/* 116 */       if (this.itemsBought != items) {
/*     */         
/* 118 */         Connection dbcon = null;
/* 119 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 122 */           this.itemsBought = items;
/* 123 */           dbcon = DbConnector.getEconomyDbCon();
/* 124 */           ps = dbcon.prepareStatement("UPDATE SUPPLYDEMAND SET ITEMSBOUGHT=? WHERE ID=?");
/* 125 */           ps.setInt(1, this.itemsBought);
/* 126 */           ps.setInt(2, this.id);
/* 127 */           ps.executeUpdate();
/*     */         }
/* 129 */         catch (SQLException sqx) {
/*     */           
/* 131 */           logger.log(Level.WARNING, "Failed to update supplyDemand with ID: " + this.id + ", items: " + items + " due to " + sqx
/* 132 */               .getMessage(), sqx);
/*     */         }
/*     */         finally {
/*     */           
/* 136 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 137 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 142 */       createSupplyDemand(1000 + items, 1000);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updateItemsSoldByTraders(int items) {
/* 153 */     if (supplyDemandExists()) {
/*     */       
/* 155 */       if (this.itemsSold != items) {
/*     */         
/* 157 */         Connection dbcon = null;
/* 158 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 161 */           this.itemsSold = items;
/* 162 */           dbcon = DbConnector.getEconomyDbCon();
/* 163 */           ps = dbcon.prepareStatement("UPDATE SUPPLYDEMAND SET ITEMSSOLD=? WHERE ID=?");
/* 164 */           ps.setInt(1, this.itemsSold);
/* 165 */           ps.setInt(2, this.id);
/* 166 */           ps.executeUpdate();
/*     */         }
/* 168 */         catch (SQLException sqx) {
/*     */           
/* 170 */           logger.log(Level.WARNING, "Failed to update supplyDemand with ID: " + this.id + ", items: " + items + " due to " + sqx
/* 171 */               .getMessage(), sqx);
/*     */         }
/*     */         finally {
/*     */           
/* 175 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 176 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 181 */       createSupplyDemand(1000, 1000 + items);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void createSupplyDemand(int aItemsBought, int aItemsSold) {
/* 192 */     Connection dbcon = null;
/* 193 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 196 */       this.lastPolled = System.currentTimeMillis();
/* 197 */       dbcon = DbConnector.getEconomyDbCon();
/* 198 */       ps = dbcon.prepareStatement("INSERT INTO SUPPLYDEMAND (ID, ITEMSBOUGHT,ITEMSSOLD, LASTPOLLED) VALUES(?,?,?,?)");
/* 199 */       ps.setInt(1, this.id);
/* 200 */       ps.setInt(2, aItemsBought);
/* 201 */       ps.setInt(3, aItemsSold);
/* 202 */       ps.setLong(4, this.lastPolled);
/* 203 */       ps.executeUpdate();
/*     */     }
/* 205 */     catch (SQLException sqx) {
/*     */       
/* 207 */       logger.log(Level.WARNING, "Failed to create supplyDemand with ID: " + this.id + ", itemsBought: " + aItemsBought + ", itemsSold: " + aItemsSold + " due to " + sqx
/* 208 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 212 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 213 */       DbConnector.returnConnection(dbcon);
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
/*     */   void reset(long time) {
/* 225 */     Connection dbcon = null;
/* 226 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 229 */       this.itemsBought = Math.max(1000, (int)(this.itemsBought * 0.99D));
/* 230 */       this.itemsSold = Math.max(1000, (int)(this.itemsSold * 0.99D));
/* 231 */       dbcon = DbConnector.getEconomyDbCon();
/* 232 */       ps = dbcon.prepareStatement("DELETE FROM SUPPLYDEMAND WHERE ID=?");
/* 233 */       ps.setInt(1, this.id);
/* 234 */       ps.executeUpdate();
/* 235 */       this.lastPolled = time;
/* 236 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 237 */       ps = dbcon.prepareStatement("INSERT INTO SUPPLYDEMAND (ID, ITEMSBOUGHT,ITEMSSOLD, LASTPOLLED) VALUES(?,?,?,?)");
/* 238 */       ps.setInt(1, this.id);
/* 239 */       ps.setInt(2, this.itemsBought);
/* 240 */       ps.setInt(3, this.itemsSold);
/* 241 */       ps.setLong(4, this.lastPolled);
/* 242 */       ps.executeUpdate();
/*     */     }
/* 244 */     catch (SQLException sqx) {
/*     */       
/* 246 */       logger.log(Level.WARNING, "Failed to reset supplyDemand with ID: " + this.id + ", time: " + time + " due to " + sqx
/* 247 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 251 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 252 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\DbSupplyDemand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */