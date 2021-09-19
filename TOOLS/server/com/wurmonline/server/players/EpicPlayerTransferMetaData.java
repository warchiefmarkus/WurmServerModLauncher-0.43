/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ public class EpicPlayerTransferMetaData
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(EpicPlayerTransferMetaData.class.getName());
/*     */ 
/*     */   
/*     */   private static final String UPDATE_PLAYER = "UPDATE PLAYERS SET NAME=?, PASSWORD=?, MAYUSESHOP=?, PLAYINGTIME=?, SEX=?, IPADDRESS=?, REIMBURSED=?, BANNED=?, PAYMENTEXPIRE=?, POWER=?,    DEVTALK=?, WARNINGS=?, LASTWARNED=?, KINGDOM=?, SESSIONKEY=?, SESSIONEXPIRE=?, VERSION=?, MUTED=?, MONEY=?, BANEXPIRY=?,        BANREASON=?, FACE=?, LOGGING=?, MAYMUTE=?, MUTEEXPIRY=?, MUTEREASON=?, REFERRER=?, EMAIL=?, PWQUESTION=?, PWANSWER=?,        CREATIONDATE=?, PA=?, APPOINTPA=?, PAWINDOW=?, MUTETIMES=?, EPICKINGDOM=?, EPICSERVER=?, CURRENTSERVER=?, CHAOSKINGDOM=?, BLOOD=?,       FLAGS=?, FLAGS2=?, MONEYSALES=?     WHERE WURMID=?";
/*     */ 
/*     */   
/*  45 */   private static final String INSERT_FRIEND = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO FRIENDS(WURMID,FRIEND,CATEGORY) VALUES(?,?,?)" : "INSERT IGNORE INTO FRIENDS(WURMID,FRIEND,CATEGORY) VALUES(?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE WURMID=?";
/*     */ 
/*     */   
/*  51 */   private static final String INSERT_IGNORED = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO IGNORED(WURMID,IGNOREE) VALUES(?,?)" : "INSERT IGNORE INTO IGNORED(WURMID,IGNOREE) VALUES(?,?)";
/*     */ 
/*     */   
/*     */   private static final String DELETE_IGNORED = "DELETE FROM IGNORED WHERE WURMID=?";
/*     */ 
/*     */   
/*     */   private final long wurmid;
/*     */ 
/*     */   
/*     */   private final String password;
/*     */ 
/*     */   
/*     */   private final String session;
/*     */ 
/*     */   
/*     */   private final String email;
/*     */ 
/*     */   
/*     */   private final boolean overRideShop;
/*     */ 
/*     */   
/*     */   private final long sessionExpiration;
/*     */ 
/*     */   
/*     */   private final byte power;
/*     */ 
/*     */   
/*     */   private final long money;
/*     */ 
/*     */   
/*     */   private final long paymentExpire;
/*     */ 
/*     */   
/*     */   private final long[] ignored;
/*     */   
/*     */   private final long[] friends;
/*     */   
/*     */   private final byte[] friendcats;
/*     */   
/*     */   private final long playingTime;
/*     */   
/*     */   private final long creationDate;
/*     */   
/*     */   private final long lastwarned;
/*     */   
/*     */   private final byte kingdom;
/*     */   
/*     */   private final boolean banned;
/*     */   
/*     */   private final long banexpiry;
/*     */   
/*     */   private final String banreason;
/*     */   
/*     */   private final boolean mute;
/*     */   
/*     */   private final short muteTimes;
/*     */   
/*     */   private final long muteexpiry;
/*     */   
/*     */   private final String mutereason;
/*     */   
/*     */   private final boolean maymute;
/*     */   
/*     */   private final boolean reimbursed;
/*     */   
/*     */   private final int warnings;
/*     */   
/*     */   private final boolean mayHearDevtalk;
/*     */   
/*     */   private final long referrer;
/*     */   
/*     */   private final String pwQuestion;
/*     */   
/*     */   private final String pwAnswer;
/*     */   
/*     */   private final boolean logging;
/*     */   
/*     */   private final boolean seesPAWin;
/*     */   
/*     */   private final boolean isCA;
/*     */   
/*     */   private final boolean mayAppointPA;
/*     */   
/*     */   private final long face;
/*     */   
/*     */   private final byte blood;
/*     */   
/*     */   private final byte sex;
/*     */   
/*     */   private final long ver;
/*     */   
/*     */   private final String lastip;
/*     */   
/*     */   private final int epicServerId;
/*     */   
/*     */   private final byte epicServerKingdom;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final byte chaosKingdom;
/*     */   
/*     */   private final long flags;
/*     */   
/*     */   private final long flags2;
/*     */   
/*     */   private byte undeadType;
/*     */   
/*     */   private int undeadKills;
/*     */   
/*     */   private int undeadPKills;
/*     */   
/*     */   private int undeadPSecs;
/*     */   
/*     */   private long moneySales;
/*     */   
/*     */   private int daysPrem;
/*     */   
/*     */   private long lastTicked;
/*     */   
/*     */   private int currentLoyaltyPoints;
/*     */   
/*     */   private int totalLoyaltyPoints;
/*     */   
/*     */   private int monthsPaidEver;
/*     */   
/*     */   private int monthsPaidInARow;
/*     */   
/*     */   private int monthsPaidSinceReset;
/*     */   
/*     */   private int silverPaidEver;
/*     */   
/*     */   private boolean hasAwards;
/*     */ 
/*     */   
/*     */   public EpicPlayerTransferMetaData(long aWurmid, String aName, String aPassword, String aSession, long aSessionExpiration, byte aPower, long aLastwarned, long aPlayingTime, byte aKingdom, boolean aBanned, long aBanexpiry, String aBanreason, boolean aReimbursed, int aWarnings, boolean aMayHearDevtalk, long aPaymentExpire, long[] aIgnored, long[] aFriends, byte[] aCats, String aLastip, boolean aMute, byte aSex, long version, long aMoney, long _face, boolean seesPa, boolean islogged, boolean _isCA, boolean mayAppointCA, long _referrer, String _pwQuestion, String _pwAnswer, boolean _overRideShop, short _muteTimes, long muteExpiry, String muteReason, boolean mayMute, String emailAddress, long _creationDate, int _epicServerId, byte _epicServerKingdom, byte _chaosKingdom, byte _blood, long _flags, long _flags2, byte udt, int udk, int udpk, int udps, long salesEver, int aDaysPrem, long aLastTicked, int currentLoyaltyPoints, int totalLoyaltyPoints, int aMonthsPaidEver, int aMonthsPaidInARow, int aMonthsPaidSinceReset, int aSilverPaidEver, boolean awards) {
/* 186 */     this.wurmid = aWurmid;
/* 187 */     this.name = aName;
/* 188 */     this.email = emailAddress;
/* 189 */     this.creationDate = _creationDate;
/* 190 */     this.password = aPassword;
/* 191 */     this.session = aSession;
/* 192 */     this.sessionExpiration = aSessionExpiration;
/* 193 */     this.lastwarned = aLastwarned;
/* 194 */     this.playingTime = aPlayingTime;
/* 195 */     this.kingdom = aKingdom;
/* 196 */     this.banned = aBanned;
/* 197 */     this.banexpiry = aBanexpiry;
/* 198 */     this.banreason = aBanreason;
/* 199 */     this.reimbursed = aReimbursed;
/* 200 */     this.warnings = aWarnings;
/* 201 */     this.mayHearDevtalk = aMayHearDevtalk;
/* 202 */     this.paymentExpire = aPaymentExpire;
/* 203 */     this.ignored = aIgnored;
/* 204 */     this.friends = aFriends;
/* 205 */     this.friendcats = aCats;
/* 206 */     this.lastip = aLastip;
/* 207 */     this.mute = aMute;
/* 208 */     this.muteTimes = _muteTimes;
/* 209 */     this.muteexpiry = muteExpiry;
/* 210 */     this.mutereason = muteReason;
/* 211 */     this.maymute = mayMute;
/* 212 */     this.sex = aSex;
/* 213 */     this.money = aMoney;
/* 214 */     this.logging = islogged;
/* 215 */     this.seesPAWin = seesPa;
/* 216 */     this.isCA = _isCA;
/* 217 */     this.mayAppointPA = mayAppointCA;
/* 218 */     this.ver = version;
/* 219 */     this.face = _face;
/* 220 */     this.referrer = _referrer;
/* 221 */     this.pwQuestion = _pwQuestion;
/* 222 */     this.pwAnswer = _pwAnswer;
/* 223 */     this.power = aPower;
/* 224 */     this.overRideShop = _overRideShop;
/* 225 */     this.epicServerId = _epicServerId;
/* 226 */     this.epicServerKingdom = _epicServerKingdom;
/* 227 */     this.chaosKingdom = _chaosKingdom;
/* 228 */     this.blood = _blood;
/* 229 */     this.flags = _flags;
/* 230 */     this.flags2 = _flags2;
/* 231 */     this.undeadType = udt;
/* 232 */     this.undeadPKills = udpk;
/* 233 */     this.undeadKills = udk;
/* 234 */     this.undeadPSecs = udps;
/* 235 */     this.moneySales = salesEver;
/* 236 */     this.daysPrem = aDaysPrem;
/* 237 */     this.lastTicked = aLastTicked;
/* 238 */     this.monthsPaidEver = aMonthsPaidEver;
/* 239 */     this.monthsPaidInARow = aMonthsPaidInARow;
/* 240 */     this.monthsPaidSinceReset = aMonthsPaidSinceReset;
/* 241 */     this.silverPaidEver = aSilverPaidEver;
/* 242 */     this.hasAwards = awards;
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
/*     */   public void save() throws IOException {
/* 256 */     Connection dbcon = null;
/* 257 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 260 */       dbcon = DbConnector.getPlayerDbCon();
/* 261 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET NAME=?, PASSWORD=?, MAYUSESHOP=?, PLAYINGTIME=?, SEX=?, IPADDRESS=?, REIMBURSED=?, BANNED=?, PAYMENTEXPIRE=?, POWER=?,    DEVTALK=?, WARNINGS=?, LASTWARNED=?, KINGDOM=?, SESSIONKEY=?, SESSIONEXPIRE=?, VERSION=?, MUTED=?, MONEY=?, BANEXPIRY=?,        BANREASON=?, FACE=?, LOGGING=?, MAYMUTE=?, MUTEEXPIRY=?, MUTEREASON=?, REFERRER=?, EMAIL=?, PWQUESTION=?, PWANSWER=?,        CREATIONDATE=?, PA=?, APPOINTPA=?, PAWINDOW=?, MUTETIMES=?, EPICKINGDOM=?, EPICSERVER=?, CURRENTSERVER=?, CHAOSKINGDOM=?, BLOOD=?,       FLAGS=?, FLAGS2=?, MONEYSALES=?     WHERE WURMID=?");
/* 262 */       ps.setString(1, this.name);
/* 263 */       ps.setString(2, this.password);
/* 264 */       ps.setBoolean(3, this.overRideShop);
/* 265 */       ps.setLong(4, this.playingTime);
/* 266 */       ps.setByte(5, this.sex);
/* 267 */       ps.setString(6, this.lastip);
/* 268 */       ps.setBoolean(7, this.reimbursed);
/* 269 */       ps.setBoolean(8, this.banned);
/* 270 */       ps.setLong(9, this.paymentExpire);
/* 271 */       ps.setByte(10, this.power);
/* 272 */       ps.setBoolean(11, this.mayHearDevtalk);
/* 273 */       ps.setShort(12, (short)this.warnings);
/* 274 */       ps.setLong(13, this.lastwarned);
/* 275 */       ps.setByte(14, this.kingdom);
/* 276 */       ps.setString(15, this.session);
/* 277 */       ps.setLong(16, this.sessionExpiration);
/* 278 */       ps.setLong(17, this.ver);
/* 279 */       ps.setBoolean(18, this.mute);
/* 280 */       ps.setLong(19, this.money);
/* 281 */       ps.setLong(20, this.banexpiry);
/* 282 */       ps.setString(21, this.banreason);
/* 283 */       ps.setLong(22, this.face);
/* 284 */       ps.setBoolean(23, this.logging);
/* 285 */       ps.setBoolean(24, this.maymute);
/* 286 */       ps.setLong(25, this.muteexpiry);
/* 287 */       ps.setString(26, this.mutereason);
/* 288 */       ps.setLong(27, this.referrer);
/* 289 */       ps.setString(28, this.email);
/* 290 */       ps.setString(29, this.pwQuestion);
/* 291 */       ps.setString(30, this.pwAnswer);
/* 292 */       ps.setLong(31, this.creationDate);
/* 293 */       ps.setBoolean(32, this.isCA);
/* 294 */       ps.setBoolean(33, this.mayAppointPA);
/* 295 */       ps.setBoolean(34, this.seesPAWin);
/* 296 */       ps.setShort(35, this.muteTimes);
/* 297 */       ps.setByte(36, this.epicServerKingdom);
/* 298 */       ps.setInt(37, this.epicServerId);
/* 299 */       ps.setInt(38, Servers.localServer.id);
/* 300 */       ps.setByte(39, this.chaosKingdom);
/* 301 */       ps.setByte(40, this.blood);
/* 302 */       ps.setLong(41, this.flags);
/* 303 */       ps.setLong(42, this.flags2);
/* 304 */       ps.setLong(43, this.moneySales);
/*     */       
/* 306 */       ps.setLong(44, this.wurmid);
/* 307 */       ps.executeUpdate();
/* 308 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 310 */       ps = dbcon.prepareStatement("DELETE FROM FRIENDS WHERE WURMID=?");
/* 311 */       ps.setLong(1, this.wurmid);
/* 312 */       ps.executeUpdate();
/* 313 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 315 */       if (this.friends != null)
/*     */       {
/* 317 */         for (int x = 0; x < this.friends.length; x++) {
/*     */           
/* 319 */           ps = dbcon.prepareStatement(INSERT_FRIEND);
/* 320 */           ps.setLong(1, this.wurmid);
/* 321 */           ps.setLong(2, this.friends[x]);
/* 322 */           ps.setByte(3, this.friendcats[x]);
/* 323 */           ps.executeUpdate();
/* 324 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         } 
/*     */       }
/*     */       
/* 328 */       ps = dbcon.prepareStatement("DELETE FROM IGNORED WHERE WURMID=?");
/* 329 */       ps.setLong(1, this.wurmid);
/* 330 */       ps.executeUpdate();
/* 331 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 333 */       if (this.ignored != null)
/*     */       {
/* 335 */         for (int x = 0; x < this.ignored.length; x++)
/*     */         {
/* 337 */           ps = dbcon.prepareStatement(INSERT_IGNORED);
/* 338 */           ps.setLong(1, this.wurmid);
/* 339 */           ps.setLong(2, this.ignored[x]);
/* 340 */           ps.executeUpdate();
/* 341 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/*     */       
/*     */       }
/*     */     }
/* 346 */     catch (SQLException sqex) {
/*     */       
/* 348 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 349 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 353 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 354 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 356 */     if (this.hasAwards) {
/*     */ 
/*     */       
/*     */       try {
/* 360 */         dbcon = DbConnector.getPlayerDbCon();
/* 361 */         ps = dbcon.prepareStatement("DELETE FROM AWARDS WHERE WURMID=?");
/* 362 */         ps.setLong(1, this.wurmid);
/* 363 */         ps.executeUpdate();
/*     */       }
/* 365 */       catch (SQLException sqex) {
/*     */         
/* 367 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 368 */         throw new IOException(sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 372 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 373 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */       
/*     */       try {
/* 377 */         dbcon = DbConnector.getPlayerDbCon();
/* 378 */         ps = dbcon.prepareStatement("INSERT INTO AWARDS(WURMID, DAYSPREM, MONTHSPREM, MONTHSEVER, CONSECMONTHS, SILVERSPURCHASED, LASTTICKEDPREM, CURRENTLOYALTY, TOTALLOYALTY) VALUES(?,?,?,?,?,?,?,?,?)");
/* 379 */         ps.setLong(1, this.wurmid);
/* 380 */         ps.setInt(2, this.daysPrem);
/* 381 */         ps.setInt(3, this.monthsPaidSinceReset);
/* 382 */         ps.setInt(4, this.monthsPaidEver);
/* 383 */         ps.setInt(5, this.monthsPaidInARow);
/* 384 */         ps.setInt(6, this.silverPaidEver);
/* 385 */         ps.setLong(7, this.lastTicked);
/* 386 */         ps.setInt(8, this.currentLoyaltyPoints);
/* 387 */         ps.setInt(9, this.totalLoyaltyPoints);
/* 388 */         ps.executeUpdate();
/*     */       }
/* 390 */       catch (SQLException sqex) {
/*     */         
/* 392 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 393 */         throw new IOException(sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 397 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 398 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\EpicPlayerTransferMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */