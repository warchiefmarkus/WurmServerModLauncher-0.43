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
/*     */ final class DbShop
/*     */   extends Shop
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(DbShop.class.getName());
/*     */   
/*     */   private static final String updateTraderMoney = "UPDATE TRADER SET MONEY=? WHERE WURMID=?";
/*     */   
/*     */   private static final String setUseGlobalPrice = "UPDATE TRADER SET FOLLOWGLOBALPRICE=? WHERE WURMID=?";
/*     */   
/*     */   private static final String setUseLocalPrice = "UPDATE TRADER SET USELOCALPRICE=? WHERE WURMID=?";
/*     */   private static final String updateTraderPriceMod = "UPDATE TRADER SET PRICEMODIFIER=? WHERE WURMID=?";
/*     */   private static final String createTrader = "INSERT INTO TRADER(WURMID,MONEY, OWNER, PRICEMODIFIER, FOLLOWGLOBALPRICE , USELOCALPRICE , LASTPOLLED, NUMBEROFITEMS, WHENEMPTY) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   private static final String checkTrader = "SELECT WURMID FROM TRADER WHERE WURMID=?";
/*     */   private static final String deleteTrader = "DELETE FROM TRADER WHERE WURMID=?";
/*     */   private static final String setLastPolled = "UPDATE TRADER SET LASTPOLLED=? WHERE WURMID=?";
/*     */   private static final String setEarnings = "UPDATE TRADER SET EARNED=?,EARNEDLIFE=? WHERE WURMID=?";
/*     */   private static final String setSpendings = "UPDATE TRADER SET SPENT=?,SPENTLIFE=? WHERE WURMID=?";
/*     */   private static final String resetSpendings = "UPDATE TRADER SET EARNED=0,SPENTLASTMONTH=SPENT,SPENT=0 WHERE WURMID=?";
/*     */   private static final String setTaxRate = "UPDATE TRADER SET TAX=? WHERE WURMID=?";
/*     */   private static final String setOwner = "UPDATE TRADER SET OWNER=? WHERE WURMID=?";
/*     */   private static final String setTaxPaid = "UPDATE TRADER SET TAXPAID=? WHERE WURMID=?";
/*     */   private static final String setMerchantData = "UPDATE TRADER SET NUMBEROFITEMS=?, WHENEMPTY=? WHERE WURMID=?";
/*     */   
/*     */   DbShop(long aWurmid, long aMoney) {
/*  58 */     super(aWurmid, aMoney);
/*     */   }
/*     */ 
/*     */   
/*     */   DbShop(long aWurmid, long aMoney, long aOwnerId) {
/*  63 */     super(aWurmid, aMoney, aOwnerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DbShop(long aWurmid, long aMoney, long aOwner, float aPriceMod, boolean aFollowGlobalPrice, boolean aUseLocalPrice, long aLastPolled, float aTax, long spentMonth, long spentLife, long earnedMonth, long earnedLife, long spentLast, long _taxPaid, int _numberOfItems, long _whenEmpty, boolean aLoad) {
/*  71 */     super(aWurmid, aMoney, aOwner, aPriceMod, aFollowGlobalPrice, aUseLocalPrice, aLastPolled, aTax, spentMonth, spentLife, earnedMonth, earnedLife, spentLast, _taxPaid, _numberOfItems, _whenEmpty, aLoad);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void create() {
/*  81 */     Connection dbcon = null;
/*  82 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  85 */       this.lastPolled = System.currentTimeMillis();
/*  86 */       dbcon = DbConnector.getEconomyDbCon();
/*  87 */       ps = dbcon.prepareStatement("INSERT INTO TRADER(WURMID,MONEY, OWNER, PRICEMODIFIER, FOLLOWGLOBALPRICE , USELOCALPRICE , LASTPOLLED, NUMBEROFITEMS, WHENEMPTY) VALUES(?,?,?,?,?,?,?,?,?)");
/*  88 */       ps.setLong(1, this.wurmid);
/*  89 */       ps.setLong(2, this.money);
/*  90 */       ps.setLong(3, this.ownerId);
/*  91 */       ps.setFloat(4, this.priceModifier);
/*  92 */       ps.setBoolean(5, this.followGlobalPrice);
/*  93 */       ps.setBoolean(6, this.useLocalPrice);
/*  94 */       ps.setLong(7, this.lastPolled);
/*  95 */       ps.setInt(8, this.numberOfItems);
/*  96 */       ps.setLong(9, this.whenEmpty);
/*  97 */       ps.executeUpdate();
/*  98 */       if (this.ownerId == -10L) {
/*  99 */         numTraders++;
/*     */       }
/* 101 */     } catch (SQLException sqx) {
/*     */       
/* 103 */       logger.log(Level.WARNING, "Failed to create traderMoney for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 107 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 108 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean traderMoneyExists() {
/* 118 */     Connection dbcon = null;
/* 119 */     PreparedStatement ps = null;
/* 120 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 123 */       dbcon = DbConnector.getEconomyDbCon();
/* 124 */       ps = dbcon.prepareStatement("SELECT WURMID FROM TRADER WHERE WURMID=?");
/* 125 */       ps.setLong(1, this.wurmid);
/* 126 */       rs = ps.executeQuery();
/* 127 */       return rs.next();
/*     */     }
/* 129 */     catch (SQLException sqx) {
/*     */       
/* 131 */       logger.log(Level.WARNING, "Failed to check trader with id " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 135 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 136 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMoney(long mon) {
/* 147 */     if (this.money != mon) {
/*     */ 
/*     */ 
/*     */       
/* 151 */       Connection dbcon = null;
/* 152 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 155 */         this.money = mon;
/* 156 */         dbcon = DbConnector.getEconomyDbCon();
/* 157 */         ps = dbcon.prepareStatement("UPDATE TRADER SET MONEY=? WHERE WURMID=?");
/* 158 */         ps.setLong(1, this.money);
/* 159 */         ps.setLong(2, this.wurmid);
/* 160 */         ps.executeUpdate();
/*     */       }
/* 162 */       catch (SQLException sqx) {
/*     */         
/* 164 */         logger.log(Level.WARNING, "Failed to update traderMoney for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 168 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 169 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFollowGlobalPrice(boolean global) {
/* 180 */     if (this.followGlobalPrice != global) {
/*     */       
/* 182 */       Connection dbcon = null;
/* 183 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 186 */         this.followGlobalPrice = global;
/* 187 */         dbcon = DbConnector.getEconomyDbCon();
/* 188 */         ps = dbcon.prepareStatement("UPDATE TRADER SET FOLLOWGLOBALPRICE=? WHERE WURMID=?");
/* 189 */         ps.setBoolean(1, this.followGlobalPrice);
/* 190 */         ps.setLong(2, this.wurmid);
/* 191 */         ps.executeUpdate();
/*     */       }
/* 193 */       catch (SQLException sqx) {
/*     */         
/* 195 */         logger.log(Level.WARNING, "Failed to update followGlobalPrice for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 199 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 200 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseLocalPrice(boolean local) {
/* 208 */     if (this.useLocalPrice != local) {
/*     */       
/* 210 */       Connection dbcon = null;
/* 211 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 214 */         this.useLocalPrice = local;
/* 215 */         dbcon = DbConnector.getEconomyDbCon();
/* 216 */         ps = dbcon.prepareStatement("UPDATE TRADER SET USELOCALPRICE=? WHERE WURMID=?");
/* 217 */         ps.setBoolean(1, this.useLocalPrice);
/* 218 */         ps.setLong(2, this.wurmid);
/* 219 */         ps.executeUpdate();
/*     */       }
/* 221 */       catch (SQLException sqx) {
/*     */         
/* 223 */         logger.log(Level.WARNING, "Failed to update useLocalPrice for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 227 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 228 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMoneySpent(long moneyS) {
/* 236 */     Connection dbcon = null;
/* 237 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 240 */       this.moneySpent += moneyS;
/* 241 */       this.moneySpentLife += moneyS;
/* 242 */       dbcon = DbConnector.getEconomyDbCon();
/* 243 */       ps = dbcon.prepareStatement("UPDATE TRADER SET SPENT=?,SPENTLIFE=? WHERE WURMID=?");
/* 244 */       ps.setLong(1, this.moneySpent);
/* 245 */       ps.setLong(2, this.moneySpentLife);
/* 246 */       ps.setLong(3, this.wurmid);
/* 247 */       ps.executeUpdate();
/*     */     }
/* 249 */     catch (SQLException sqx) {
/*     */       
/* 251 */       logger.log(Level.WARNING, "Failed to update lastPolled for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 255 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 256 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetEarnings() {
/* 263 */     Connection dbcon = null;
/* 264 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 267 */       this.moneySpentLastMonth = this.moneySpent;
/* 268 */       this.moneySpent = 0L;
/* 269 */       this.moneyEarned = 0L;
/* 270 */       dbcon = DbConnector.getEconomyDbCon();
/* 271 */       ps = dbcon.prepareStatement("UPDATE TRADER SET EARNED=0,SPENTLASTMONTH=SPENT,SPENT=0 WHERE WURMID=?");
/* 272 */       ps.setLong(1, this.wurmid);
/* 273 */       ps.executeUpdate();
/*     */     }
/* 275 */     catch (SQLException sqx) {
/*     */       
/* 277 */       logger.log(Level.WARNING, "Failed to update lastPolled for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 281 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 282 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMoneyEarned(long moneyE) {
/* 289 */     Connection dbcon = null;
/* 290 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 293 */       this.moneyEarned += moneyE;
/* 294 */       this.moneyEarnedLife += moneyE;
/* 295 */       dbcon = DbConnector.getEconomyDbCon();
/* 296 */       ps = dbcon.prepareStatement("UPDATE TRADER SET EARNED=?,EARNEDLIFE=? WHERE WURMID=?");
/* 297 */       ps.setLong(1, this.moneyEarned);
/* 298 */       ps.setLong(2, this.moneyEarnedLife);
/* 299 */       ps.setLong(3, this.wurmid);
/* 300 */       ps.executeUpdate();
/*     */     }
/* 302 */     catch (SQLException sqx) {
/*     */       
/* 304 */       logger.log(Level.WARNING, "Failed to update lastPolled for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 308 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 309 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastPolled(long lastPoll) {
/* 316 */     if (this.lastPolled != lastPoll) {
/*     */       
/* 318 */       Connection dbcon = null;
/* 319 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 322 */         this.lastPolled = lastPoll;
/* 323 */         dbcon = DbConnector.getEconomyDbCon();
/* 324 */         ps = dbcon.prepareStatement("UPDATE TRADER SET LASTPOLLED=? WHERE WURMID=?");
/* 325 */         ps.setLong(1, this.lastPolled);
/* 326 */         ps.setLong(2, this.wurmid);
/* 327 */         ps.executeUpdate();
/*     */       }
/* 329 */       catch (SQLException sqx) {
/*     */         
/* 331 */         logger.log(Level.WARNING, "Failed to update lastPolled for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 335 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 336 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 344 */     Connection dbcon = null;
/* 345 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 348 */       dbcon = DbConnector.getEconomyDbCon();
/* 349 */       ps = dbcon.prepareStatement("DELETE FROM TRADER WHERE WURMID=?");
/* 350 */       ps.setLong(1, this.wurmid);
/* 351 */       ps.executeUpdate();
/* 352 */       if (this.ownerId == -10L) {
/* 353 */         numTraders--;
/*     */       }
/* 355 */     } catch (SQLException sqx) {
/*     */       
/* 357 */       logger.log(Level.WARNING, "Failed to delete trader" + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 361 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 362 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 364 */     Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() + Math.max(0L, this.money));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPriceModifier(float priceMod) {
/* 370 */     if (this.priceModifier != priceMod) {
/*     */       
/* 372 */       this.priceModifier = priceMod;
/* 373 */       Connection dbcon = null;
/* 374 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 377 */         dbcon = DbConnector.getEconomyDbCon();
/* 378 */         ps = dbcon.prepareStatement("UPDATE TRADER SET PRICEMODIFIER=? WHERE WURMID=?");
/* 379 */         ps.setFloat(1, this.priceModifier);
/* 380 */         ps.setLong(2, this.wurmid);
/* 381 */         ps.executeUpdate();
/*     */       }
/* 383 */       catch (SQLException sqx) {
/*     */         
/* 385 */         logger.log(Level.WARNING, "Failed to update trader pricemodifier for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 389 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 390 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTax(float newtax) {
/* 398 */     if (this.tax != newtax) {
/*     */       
/* 400 */       this.tax = newtax;
/* 401 */       Connection dbcon = null;
/* 402 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 405 */         dbcon = DbConnector.getEconomyDbCon();
/* 406 */         ps = dbcon.prepareStatement("UPDATE TRADER SET TAX=? WHERE WURMID=?");
/* 407 */         ps.setFloat(1, this.tax);
/* 408 */         ps.setLong(2, this.wurmid);
/* 409 */         ps.executeUpdate();
/*     */       }
/* 411 */       catch (SQLException sqx) {
/*     */         
/* 413 */         logger.log(Level.WARNING, "Failed to update trader tax for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 417 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 418 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTax(long addedtax) {
/* 426 */     this.taxPaid += addedtax;
/* 427 */     Connection dbcon = null;
/* 428 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 431 */       dbcon = DbConnector.getEconomyDbCon();
/* 432 */       ps = dbcon.prepareStatement("UPDATE TRADER SET TAXPAID=? WHERE WURMID=?");
/* 433 */       ps.setLong(1, this.taxPaid);
/* 434 */       ps.setLong(2, this.wurmid);
/* 435 */       ps.executeUpdate();
/*     */     }
/* 437 */     catch (SQLException sqx) {
/*     */       
/* 439 */       logger.log(Level.WARNING, "Failed to update trader tax for " + this.wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 443 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 444 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(long newOwnerId) {
/* 451 */     if (newOwnerId != this.ownerId) {
/*     */       
/* 453 */       this.ownerId = newOwnerId;
/* 454 */       Connection dbcon = null;
/* 455 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 458 */         dbcon = DbConnector.getEconomyDbCon();
/* 459 */         ps = dbcon.prepareStatement("UPDATE TRADER SET OWNER=? WHERE WURMID=?");
/* 460 */         ps.setLong(1, this.ownerId);
/* 461 */         ps.setLong(2, this.wurmid);
/* 462 */         ps.executeUpdate();
/*     */       }
/* 464 */       catch (SQLException sqx) {
/*     */         
/* 466 */         logger.log(Level.WARNING, "Failed to update trader owner " + this.ownerId + " for " + this.wurmid + ": " + sqx
/* 467 */             .getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 471 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 472 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerchantData(int _numberOfItems, long _whenEmpty) {
/* 480 */     if (_numberOfItems != this.numberOfItems || _whenEmpty != this.whenEmpty) {
/*     */       
/* 482 */       this.numberOfItems = _numberOfItems;
/* 483 */       this.whenEmpty = _whenEmpty;
/* 484 */       Connection dbcon = null;
/* 485 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 488 */         dbcon = DbConnector.getEconomyDbCon();
/* 489 */         ps = dbcon.prepareStatement("UPDATE TRADER SET NUMBEROFITEMS=?, WHENEMPTY=? WHERE WURMID=?");
/* 490 */         ps.setInt(1, this.numberOfItems);
/* 491 */         ps.setLong(2, this.whenEmpty);
/* 492 */         ps.setLong(3, this.wurmid);
/* 493 */         ps.executeUpdate();
/*     */       }
/* 495 */       catch (SQLException sqx) {
/*     */         
/* 497 */         logger.log(Level.WARNING, "Failed to update merchant data " + this.numberOfItems + "," + this.whenEmpty + " for " + this.wurmid + ": " + sqx
/* 498 */             .getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 502 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 503 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\DbShop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */