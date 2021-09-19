/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Awards
/*     */ {
/*     */   public static final String INSERT_AWARDS = "INSERT INTO AWARDS(WURMID, DAYSPREM, MONTHSPREM, MONTHSEVER, CONSECMONTHS, SILVERSPURCHASED, LASTTICKEDPREM, CURRENTLOYALTY, TOTALLOYALTY) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   public static final String UPDATE_AWARDS = "UPDATE AWARDS SET DAYSPREM=?, MONTHSPREM=?, MONTHSEVER=?, CONSECMONTHS=?, SILVERSPURCHASED=?, LASTTICKEDPREM=?, TOTALLOYALTY=?, CURRENTLOYALTY=? WHERE WURMID=?";
/*     */   public static final String DELETE_AWARDS = "DELETE FROM AWARDS WHERE WURMID=?";
/*     */   private long wurmId;
/*  48 */   private static final Logger logger = Logger.getLogger(Awards.class.getName());
/*     */ 
/*     */   
/*     */   private int daysPrem;
/*     */   
/*     */   private int monthsPaidEver;
/*     */   
/*     */   private int monthsPaidSinceReset;
/*     */   
/*     */   private int monthsPaidInARow;
/*     */   
/*     */   private int silversPaidEver;
/*     */   
/*     */   private long lastTickedDay;
/*     */   
/*     */   private int currentLoyaltyPoints;
/*     */   
/*     */   private int totalLoyaltyPoints;
/*     */   
/*  67 */   public static final Map<Long, Awards> allAwards = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Awards(long playerId, int daysPremium, int _monthsPaidEver, int monthsPaidInSuccession, int _monthsPaidSinceReset, int silversPurchased, long _lastTickedDay, int _currentLoyalty, int _totalLoyalty, boolean createInDb) {
/*  76 */     this.wurmId = playerId;
/*  77 */     this.daysPrem = daysPremium;
/*  78 */     this.monthsPaidEver = _monthsPaidEver;
/*  79 */     this.monthsPaidInARow = monthsPaidInSuccession;
/*  80 */     this.silversPaidEver = silversPurchased;
/*  81 */     this.lastTickedDay = _lastTickedDay;
/*  82 */     this.monthsPaidSinceReset = _monthsPaidSinceReset;
/*  83 */     this.currentLoyaltyPoints = _currentLoyalty;
/*  84 */     this.totalLoyaltyPoints = _totalLoyalty;
/*  85 */     allAwards.put(Long.valueOf(this.wurmId), this);
/*  86 */     if (createInDb) {
/*  87 */       save();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  94 */     return "Awards for " + this.wurmId + ", days=" + this.daysPrem + ", mo's ever=" + this.monthsPaidEver + ", mo's row=" + this.monthsPaidInARow + ", mo's reset=" + this.monthsPaidSinceReset + ", silvers=" + this.silversPaidEver + ", loyalty=" + this.currentLoyaltyPoints + ", totalLoyalty=" + this.totalLoyaltyPoints + ", tick=" + 
/*     */ 
/*     */       
/*  97 */       Server.getTimeFor(System.currentTimeMillis() - this.lastTickedDay) + " ago";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/* 107 */     return this.wurmId;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Awards getAwards(long wurmid) {
/* 112 */     return allAwards.get(Long.valueOf(wurmid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWurmId(long aWurmId) {
/* 123 */     this.wurmId = aWurmId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonthsPaidEver() {
/* 133 */     return this.monthsPaidEver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMonthsPaidEver(int aMonthsPaidEver) {
/* 144 */     this.monthsPaidEver = aMonthsPaidEver;
/*     */   }
/*     */ 
/*     */   
/*     */   public AwardLadder getNextReward() {
/* 149 */     return AwardLadder.getNextTotalAward(getMonthsPaidSinceReset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonthsPaidInARow() {
/* 159 */     return this.monthsPaidInARow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMonthsPaidInARow(int aMonthsPaidInARow) {
/* 170 */     this.monthsPaidInARow = aMonthsPaidInARow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSilversPaidEver() {
/* 180 */     return this.silversPaidEver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSilversPaidEver(int aSilversPaidEver) {
/* 191 */     this.silversPaidEver = aSilversPaidEver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastTickedDay() {
/* 201 */     return this.lastTickedDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastTickedDay(long aLastTickedDay) {
/* 212 */     this.lastTickedDay = aLastTickedDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDaysPrem() {
/* 222 */     return this.daysPrem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDaysPrem(int aDaysPrem) {
/* 233 */     this.daysPrem = aDaysPrem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonthsPaidSinceReset() {
/* 243 */     return this.monthsPaidSinceReset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMonthsPaidSinceReset(int aMonthsPaidSinceReset) {
/* 254 */     this.monthsPaidSinceReset = aMonthsPaidSinceReset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentLoyalty() {
/* 264 */     return this.currentLoyaltyPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentLoyalty(int newLoyalty) {
/* 274 */     this.currentLoyaltyPoints = newLoyalty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalLoyalty() {
/* 284 */     return this.totalLoyaltyPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTotalLoyaltyPoints(int newTotal) {
/* 294 */     this.totalLoyaltyPoints = newTotal;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void save() {
/* 300 */     Connection dbcon = null;
/* 301 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 304 */       dbcon = DbConnector.getPlayerDbCon();
/* 305 */       ps = dbcon.prepareStatement("INSERT INTO AWARDS(WURMID, DAYSPREM, MONTHSPREM, MONTHSEVER, CONSECMONTHS, SILVERSPURCHASED, LASTTICKEDPREM, CURRENTLOYALTY, TOTALLOYALTY) VALUES(?,?,?,?,?,?,?,?,?)");
/* 306 */       ps.setLong(1, this.wurmId);
/* 307 */       ps.setInt(2, this.daysPrem);
/* 308 */       ps.setInt(3, this.monthsPaidSinceReset);
/* 309 */       ps.setInt(4, this.monthsPaidEver);
/* 310 */       ps.setInt(5, this.monthsPaidInARow);
/* 311 */       ps.setInt(6, this.silversPaidEver);
/* 312 */       ps.setLong(7, this.lastTickedDay);
/* 313 */       ps.setInt(8, this.currentLoyaltyPoints);
/* 314 */       ps.setInt(9, this.totalLoyaltyPoints);
/* 315 */       ps.executeUpdate();
/*     */     }
/* 317 */     catch (SQLException sqex) {
/*     */       
/* 319 */       logger.log(Level.WARNING, this.wurmId + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 323 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 324 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void update() {
/* 330 */     Connection dbcon = null;
/* 331 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 334 */       dbcon = DbConnector.getPlayerDbCon();
/* 335 */       ps = dbcon.prepareStatement("UPDATE AWARDS SET DAYSPREM=?, MONTHSPREM=?, MONTHSEVER=?, CONSECMONTHS=?, SILVERSPURCHASED=?, LASTTICKEDPREM=?, TOTALLOYALTY=?, CURRENTLOYALTY=? WHERE WURMID=?");
/* 336 */       ps.setInt(1, this.daysPrem);
/* 337 */       ps.setInt(2, this.monthsPaidSinceReset);
/* 338 */       ps.setInt(3, this.monthsPaidEver);
/* 339 */       ps.setInt(4, this.monthsPaidInARow);
/* 340 */       ps.setInt(5, this.silversPaidEver);
/* 341 */       ps.setLong(6, this.lastTickedDay);
/* 342 */       ps.setInt(7, this.currentLoyaltyPoints);
/* 343 */       ps.setInt(8, this.totalLoyaltyPoints);
/* 344 */       ps.setLong(9, this.wurmId);
/* 345 */       ps.executeUpdate();
/*     */     }
/* 347 */     catch (SQLException sqex) {
/*     */       
/* 349 */       logger.log(Level.WARNING, this.wurmId + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 353 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 354 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Awards.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */