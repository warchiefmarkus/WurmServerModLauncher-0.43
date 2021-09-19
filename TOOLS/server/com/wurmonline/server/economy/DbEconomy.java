/*     */ package com.wurmonline.server.economy;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.List;
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
/*     */ public final class DbEconomy
/*     */   extends Economy
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(DbEconomy.class.getName());
/*     */   
/*     */   private static final String createEconomy = "insert into ECONOMY(ID, GOLDCOINS, SILVERCOINS, COPPERCOINS, IRONCOINS)values(?,?,?,?,?)";
/*     */   
/*     */   private static final String getEconomy = "SELECT * FROM ECONOMY WHERE ID=?";
/*     */   
/*     */   private static final String updateLastPolledTraders = "UPDATE ECONOMY SET LASTPOLLED=? WHERE ID=?";
/*     */   
/*     */   private static final String updateCreatedGold = "UPDATE ECONOMY SET GOLDCOINS=? WHERE ID=?";
/*     */   
/*     */   private static final String updateCreatedSilver = "UPDATE ECONOMY SET SILVERCOINS=? WHERE ID=?";
/*     */   private static final String updateCreatedCopper = "UPDATE ECONOMY SET COPPERCOINS=? WHERE ID=?";
/*     */   private static final String updateCreatedIron = "UPDATE ECONOMY SET IRONCOINS=? WHERE ID=?";
/*     */   private static final String logSoldItem = "INSERT INTO ITEMSSOLD (ITEMNAME,ITEMVALUE,TRADERNAME,PLAYERNAME, TEMPLATEID) VALUES(?,?,?,?,?)";
/*     */   private static final String getCoins = "SELECT * FROM COINS WHERE TEMPLATEID=? AND OWNERID=-10 AND PARENTID=-10 AND ZONEID=-10 AND BANKED=1 AND MAILED=0";
/*     */   private static final String getSupplyDemand = "SELECT * FROM SUPPLYDEMAND";
/*     */   private static final String getTraderMoney = "SELECT * FROM TRADER";
/*     */   private static final String createTransaction = "INSERT INTO TRANSACTS (ITEMID, OLDOWNERID,NEWOWNERID,REASON, VALUE) VALUES (?,?,?,?,?)";
/*     */   
/*     */   DbEconomy(int serverNumber) throws IOException {
/*  61 */     super(serverNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void initialize() throws IOException {
/*  67 */     Connection dbcon = null;
/*  68 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  71 */       dbcon = DbConnector.getEconomyDbCon();
/*  72 */       if (exists(dbcon))
/*     */       {
/*  74 */         load();
/*     */       }
/*     */       else
/*     */       {
/*  78 */         ps = dbcon.prepareStatement("insert into ECONOMY(ID, GOLDCOINS, SILVERCOINS, COPPERCOINS, IRONCOINS)values(?,?,?,?,?)");
/*  79 */         ps.setInt(1, this.id);
/*  80 */         ps.setLong(2, goldCoins);
/*  81 */         ps.setLong(3, silverCoins);
/*  82 */         ps.setLong(4, copperCoins);
/*  83 */         ps.setLong(5, ironCoins);
/*  84 */         ps.executeUpdate();
/*     */       }
/*     */     
/*  87 */     } catch (SQLException sqx) {
/*     */       
/*  89 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  93 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  94 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*  96 */     loadSupplyDemand();
/*  97 */     loadShopMoney();
/*     */   }
/*     */ 
/*     */   
/*     */   private void load() throws IOException {
/* 102 */     Connection dbcon = null;
/* 103 */     PreparedStatement ps = null;
/* 104 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 107 */       dbcon = DbConnector.getEconomyDbCon();
/* 108 */       ps = dbcon.prepareStatement("SELECT * FROM ECONOMY WHERE ID=?");
/* 109 */       ps.setInt(1, this.id);
/* 110 */       rs = ps.executeQuery();
/* 111 */       if (rs.next()) {
/*     */         
/* 113 */         goldCoins = rs.getLong("GOLDCOINS");
/* 114 */         silverCoins = rs.getLong("SILVERCOINS");
/* 115 */         copperCoins = rs.getLong("COPPERCOINS");
/* 116 */         ironCoins = rs.getLong("IRONCOINS");
/* 117 */         lastPolledTraders = rs.getLong("LASTPOLLED");
/*     */       } 
/* 119 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 120 */       Change change = new Change(ironCoins + copperCoins * 100L + silverCoins * 10000L + goldCoins * 1000000L);
/*     */       
/* 122 */       if (lastPolledTraders <= 0L)
/* 123 */         lastPolledTraders = System.currentTimeMillis() - 2419200000L; 
/* 124 */       updateCreatedIron(change.getIronCoins());
/* 125 */       logger.log(Level.INFO, "Iron=" + ironCoins);
/* 126 */       updateCreatedCopper(change.getCopperCoins());
/* 127 */       logger.log(Level.INFO, "Copper=" + copperCoins);
/* 128 */       updateCreatedSilver(change.getSilverCoins());
/* 129 */       logger.log(Level.INFO, "Silver=" + silverCoins);
/* 130 */       updateCreatedGold(change.getGoldCoins());
/* 131 */       logger.log(Level.INFO, "Gold=" + goldCoins);
/* 132 */       loadCoins(50);
/* 133 */       loadCoins(54);
/* 134 */       loadCoins(58);
/* 135 */       loadCoins(53);
/* 136 */       loadCoins(57);
/* 137 */       loadCoins(61);
/* 138 */       loadCoins(51);
/* 139 */       loadCoins(55);
/* 140 */       loadCoins(59);
/* 141 */       loadCoins(52);
/* 142 */       loadCoins(56);
/* 143 */       loadCoins(60);
/*     */     }
/* 145 */     catch (SQLException sqx) {
/*     */       
/* 147 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 151 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 152 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/* 158 */     PreparedStatement ps = null;
/* 159 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 162 */       ps = dbcon.prepareStatement("SELECT * FROM ECONOMY WHERE ID=?");
/* 163 */       ps.setInt(1, this.id);
/* 164 */       rs = ps.executeQuery();
/* 165 */       return rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 169 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void transaction(long itemId, long oldownerid, long newownerid, String newReason, long value) {
/* 177 */     if (DbConnector.isUseSqlite())
/*     */       return; 
/* 179 */     Connection dbcon = null;
/* 180 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 183 */       String reason = newReason.substring(0, Math.min(19, newReason.length()));
/* 184 */       dbcon = DbConnector.getEconomyDbCon();
/* 185 */       ps = dbcon.prepareStatement("INSERT INTO TRANSACTS (ITEMID, OLDOWNERID,NEWOWNERID,REASON, VALUE) VALUES (?,?,?,?,?)");
/* 186 */       ps.setLong(1, itemId);
/* 187 */       ps.setLong(2, oldownerid);
/* 188 */       ps.setLong(3, newownerid);
/* 189 */       ps.setString(4, reason);
/* 190 */       ps.setLong(5, value);
/* 191 */       ps.executeUpdate();
/*     */     }
/* 193 */     catch (SQLException sqx) {
/*     */       
/* 195 */       logger.log(Level.WARNING, "Failed to create transaction for itemId: " + itemId + " due to " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 199 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 200 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCreatedGold(long number) {
/* 207 */     Connection dbcon = null;
/* 208 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 211 */       goldCoins = number;
/* 212 */       dbcon = DbConnector.getEconomyDbCon();
/* 213 */       ps = dbcon.prepareStatement("UPDATE ECONOMY SET GOLDCOINS=? WHERE ID=?");
/* 214 */       ps.setLong(1, goldCoins);
/* 215 */       ps.setInt(2, this.id);
/* 216 */       ps.executeUpdate();
/*     */     }
/* 218 */     catch (SQLException sqx) {
/*     */       
/* 220 */       logger.log(Level.WARNING, "Failed to update num gold: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 224 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 225 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLastPolled() {
/* 232 */     Connection dbcon = null;
/* 233 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 236 */       lastPolledTraders = System.currentTimeMillis();
/* 237 */       dbcon = DbConnector.getEconomyDbCon();
/* 238 */       ps = dbcon.prepareStatement("UPDATE ECONOMY SET LASTPOLLED=? WHERE ID=?");
/* 239 */       ps.setLong(1, lastPolledTraders);
/* 240 */       ps.setInt(2, this.id);
/* 241 */       ps.executeUpdate();
/*     */     }
/* 243 */     catch (SQLException sqx) {
/*     */       
/* 245 */       logger.log(Level.WARNING, "Failed to update last polled traders: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 249 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 250 */       DbConnector.returnConnection(dbcon);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCreatedSilver(long number) {
/* 277 */     Connection dbcon = null;
/* 278 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 281 */       silverCoins = number;
/* 282 */       dbcon = DbConnector.getEconomyDbCon();
/* 283 */       ps = dbcon.prepareStatement("UPDATE ECONOMY SET SILVERCOINS=? WHERE ID=?");
/* 284 */       ps.setLong(1, silverCoins);
/* 285 */       ps.setInt(2, this.id);
/* 286 */       ps.executeUpdate();
/*     */     }
/* 288 */     catch (SQLException sqx) {
/*     */       
/* 290 */       logger.log(Level.WARNING, "Failed to update num silver: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 294 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 295 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCreatedCopper(long number) {
/* 302 */     Connection dbcon = null;
/* 303 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 306 */       copperCoins = number;
/* 307 */       dbcon = DbConnector.getEconomyDbCon();
/* 308 */       ps = dbcon.prepareStatement("UPDATE ECONOMY SET COPPERCOINS=? WHERE ID=?");
/* 309 */       ps.setLong(1, copperCoins);
/* 310 */       ps.setInt(2, this.id);
/* 311 */       ps.executeUpdate();
/*     */     }
/* 313 */     catch (SQLException sqx) {
/*     */       
/* 315 */       logger.log(Level.WARNING, "Failed to update num copper: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 319 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 320 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCreatedIron(long number) {
/* 327 */     Connection dbcon = null;
/* 328 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 331 */       ironCoins = number;
/* 332 */       dbcon = DbConnector.getEconomyDbCon();
/* 333 */       ps = dbcon.prepareStatement("UPDATE ECONOMY SET IRONCOINS=? WHERE ID=?");
/* 334 */       ps.setLong(1, ironCoins);
/* 335 */       ps.setInt(2, this.id);
/* 336 */       ps.executeUpdate();
/*     */     }
/* 338 */     catch (SQLException sqx) {
/*     */       
/* 340 */       logger.log(Level.WARNING, "Failed to update num iron: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 344 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 345 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadCoins(int type) {
/* 351 */     List<Item> current = getListForCointype(type);
/* 352 */     Connection dbcon = null;
/* 353 */     PreparedStatement ps = null;
/* 354 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 357 */       dbcon = DbConnector.getItemDbCon();
/* 358 */       ps = dbcon.prepareStatement("SELECT * FROM COINS WHERE TEMPLATEID=? AND OWNERID=-10 AND PARENTID=-10 AND ZONEID=-10 AND BANKED=1 AND MAILED=0");
/* 359 */       ps.setInt(1, type);
/* 360 */       rs = ps.executeQuery();
/* 361 */       while (rs.next()) {
/*     */         
/*     */         try
/*     */         {
/* 365 */           Item toAdd = Items.getItem(rs.getLong("WURMID"));
/* 366 */           current.add(toAdd);
/*     */         }
/* 368 */         catch (NoSuchItemException nsi)
/*     */         {
/* 370 */           logger.log(Level.WARNING, "Failed to load coin: " + rs.getLong("WURMID"), (Throwable)nsi);
/*     */         }
/*     */       
/*     */       } 
/* 374 */     } catch (SQLException sqx) {
/*     */       
/* 376 */       logger.log(Level.WARNING, "Failed to load coins: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 380 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 381 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shop createShop(long wurmid) {
/* 388 */     return new DbShop(wurmid, Servers.localServer.getInitialTraderIrons());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shop createShop(long wurmid, long ownerid) {
/* 394 */     int coins = 0;
/* 395 */     if (ownerid == -10L)
/* 396 */       coins = Servers.localServer.getInitialTraderIrons(); 
/* 397 */     return new DbShop(wurmid, coins, ownerid);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SupplyDemand createSupplyDemand(int aId) {
/* 403 */     return new DbSupplyDemand(aId, 1000, 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void loadSupplyDemand() {
/* 409 */     Connection dbcon = null;
/* 410 */     PreparedStatement ps = null;
/* 411 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 414 */       dbcon = DbConnector.getEconomyDbCon();
/* 415 */       ps = dbcon.prepareStatement("SELECT * FROM SUPPLYDEMAND");
/* 416 */       rs = ps.executeQuery();
/* 417 */       while (rs.next())
/*     */       {
/* 419 */         new DbSupplyDemand(rs.getInt("ID"), rs.getInt("ITEMSBOUGHT"), rs.getInt("ITEMSSOLD"), rs.getLong("LASTPOLLED"));
/*     */       }
/*     */     }
/* 422 */     catch (SQLException sqx) {
/*     */       
/* 424 */       logger.log(Level.WARNING, "Failed to load supplyDemand: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 428 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 429 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void loadShopMoney() {
/* 436 */     Connection dbcon = null;
/* 437 */     PreparedStatement ps = null;
/* 438 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 441 */       dbcon = DbConnector.getEconomyDbCon();
/* 442 */       ps = dbcon.prepareStatement("SELECT * FROM TRADER");
/* 443 */       rs = ps.executeQuery();
/* 444 */       while (rs.next())
/*     */       {
/* 446 */         new DbShop(rs.getLong("WURMID"), rs.getLong("MONEY"), rs.getLong("OWNER"), rs.getFloat("PRICEMODIFIER"), rs
/* 447 */             .getBoolean("FOLLOWGLOBALPRICE"), rs.getBoolean("USELOCALPRICE"), rs.getLong("LASTPOLLED"), rs
/* 448 */             .getFloat("TAX"), rs.getLong("SPENT"), rs.getLong("SPENTLIFE"), rs.getLong("EARNED"), rs
/* 449 */             .getLong("EARNEDLIFE"), rs.getLong("SPENTLASTMONTH"), rs.getLong("TAXPAID"), rs
/* 450 */             .getInt("NUMBEROFITEMS"), rs.getLong("WHENEMPTY"), true);
/*     */       }
/*     */     }
/* 453 */     catch (SQLException sqx) {
/*     */       
/* 455 */       logger.log(Level.WARNING, "Failed to load traderMoney: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 459 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 460 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItemSoldByTraders(String name, long money, String traderName, String playerName, int templateId) {
/* 468 */     Connection dbcon = null;
/* 469 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 472 */       dbcon = DbConnector.getEconomyDbCon();
/* 473 */       ps = dbcon.prepareStatement("INSERT INTO ITEMSSOLD (ITEMNAME,ITEMVALUE,TRADERNAME,PLAYERNAME, TEMPLATEID) VALUES(?,?,?,?,?)");
/* 474 */       ps.setString(1, name.substring(0, Math.min(29, name.length())));
/* 475 */       ps.setLong(2, money);
/* 476 */       ps.setString(3, traderName.substring(0, Math.min(29, traderName.length())));
/* 477 */       ps.setString(4, playerName.substring(0, Math.min(29, playerName.length())));
/* 478 */       ps.setInt(5, templateId);
/* 479 */       ps.executeUpdate();
/*     */     }
/* 481 */     catch (SQLException sqx) {
/*     */       
/* 483 */       logger.log(Level.WARNING, "Failed to update num iron: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 487 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 488 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\DbEconomy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */