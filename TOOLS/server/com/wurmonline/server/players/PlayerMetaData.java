/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.CreaturePos;
/*     */ import com.wurmonline.server.spells.Cooldowns;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class PlayerMetaData
/*     */   implements MiscConstants
/*     */ {
/*     */   private static final String CREATE_PLAYER = "insert into PLAYERS (MAYUSESHOP,PASSWORD,WURMID,LASTLOGOUT,PLAYINGTIME,TEMPLATENAME,SEX,CENTIMETERSHIGH,CENTIMETERSLONG,CENTIMETERSWIDE,INVENTORYID,BODYID,MONEYSALES,BUILDINGID,STAMINA,HUNGER,THIRST,IPADDRESS,REIMBURSED,PLANTEDSIGN,BANNED,PAYMENTEXPIRE,POWER,RANK,DEVTALK,WARNINGS,LASTWARNED,FAITH,DEITY,ALIGNMENT,GOD,FAVOR,LASTCHANGEDDEITY,REALDEATH,CHEATED,LASTFATIGUE,FATIGUE,DEAD,KINGDOM,SESSIONKEY,SESSIONEXPIRE,VERSION,MUTED,LASTFAITH,NUMFAITH,MONEY,CLIMBING,NUMSCHANGEDKINGDOM,AGE,LASTPOLLEDAGE,FAT,BANEXPIRY,BANREASON,FACE,REPUTATION,LASTPOLLEDREP,TITLE,PET,NICOTINE,NICOTINETIME,ALCOHOL,ALCOHOLTIME,LOGGING,MAYMUTE,MUTEEXPIRY,MUTEREASON,LASTSERVER,CURRENTSERVER,REFERRER,EMAIL,PWQUESTION,PWANSWER,PRIEST,BED,SLEEP,CREATIONDATE,THEFTWARNED,NOREIMB,DEATHPROT,FATIGUETODAY,FATIGUEYDAY,FIGHTMODE,NEXTAFFINITY,DETECTIONSECS,TUTORIALLEVEL,AUTOFIGHT,APPOINTMENTS,PA,APPOINTPA,PAWINDOW,NUTRITION,DISEASE,PRIESTTYPE,LASTCHANGEDPRIEST,LASTCHANGEDKINGDOM,LASTLOSTCHAMPION,CHAMPIONPOINTS,CHAMPCHANNELING,MUTETIMES,VOTEDKING,EPICKINGDOM,EPICSERVER,CHAOSKINGDOM,FREETRANSFER,HOTA_WINS,LASTMODIFIEDRANK,MAXRANK,KARMA,MAXKARMA,TOTALKARMA,BLOOD,FLAGS,FLAGS2,ABILITIES,ABILITYTITLE,SCENARIOKARMA,UNDEADTYPE,UNDEADKILLS,UNDEADPKILLS,UNDEADPSECS,NAME,CALORIES,CARBS,FATS,PROTEINS,SECONDTITLE) VALUES(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   private static final String DELETE_PLAYER = "DELETE FROM PLAYERS WHERE WURMID=?";
/*     */   private static final String GET_PLAYER = "select NAME from PLAYERS where NAME=?";
/*     */   private static final String INSERT_TITLE = "INSERT INTO TITLES (WURMID,TITLEID,TITLENAME) VALUES(?,?,?)";
/*  76 */   private static final String INSERT_FRIEND = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO FRIENDS(WURMID,FRIEND,CATEGORY) VALUES(?,?,?)" : "INSERT IGNORE INTO FRIENDS(WURMID,FRIEND,CATEGORY) VALUES(?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE WURMID=?";
/*     */ 
/*     */   
/*  82 */   private static final String INSERT_ENEMY = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ENEMIES(WURMID,ENEMY) VALUES(?,?)" : "INSERT IGNORE INTO ENEMIES(WURMID,ENEMY) VALUES(?,?)";
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final String INSERT_IGNORED = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO IGNORED(WURMID,IGNOREE) VALUES(?,?)" : "INSERT IGNORE INTO IGNORED(WURMID,IGNOREE) VALUES(?,?)";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DELETE_IGNORED = "DELETE FROM IGNORED WHERE WURMID=?";
/*     */ 
/*     */   
/*  93 */   private static final Logger logger = Logger.getLogger(PlayerMetaData.class.getName());
/*     */ 
/*     */   
/*     */   private final long wurmid;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public String password;
/*     */   
/*     */   private final String session;
/*     */   
/*     */   private final short centhigh;
/*     */   
/*     */   private final short centlong;
/*     */   
/*     */   private final short centwide;
/*     */   
/*     */   private final long lastChangedDeity;
/*     */   
/*     */   private final byte changedKingdom;
/*     */   
/*     */   private final long sessionExpiration;
/*     */   
/*     */   private final byte power;
/*     */   
/*     */   private final byte deity;
/*     */   
/*     */   private final float align;
/*     */   
/*     */   private final float faith;
/*     */   
/*     */   private final float favor;
/*     */   
/*     */   private final byte god;
/*     */   
/*     */   private final byte realdeathcounter;
/*     */   
/*     */   private final int fatiguesecsleft;
/*     */   
/*     */   public int fatigueSecsToday;
/*     */   
/*     */   public int fatigueSecsYday;
/*     */   
/*     */   public short muteTimes;
/*     */   
/*     */   private final long lastfatigue;
/*     */   
/*     */   private final long lastwarned;
/*     */   
/*     */   private final long lastcheated;
/*     */   
/*     */   private final long plantedSign;
/*     */   
/*     */   private final long playingTime;
/*     */   
/*     */   private final byte kingdom;
/*     */   
/*     */   private final int rank;
/*     */   
/*     */   public int maxRank;
/*     */   
/*     */   private final boolean banned;
/*     */   
/*     */   private final long banexpiry;
/*     */   
/*     */   private final String banreason;
/*     */   
/*     */   private final boolean reimbursed;
/*     */   
/*     */   private final int warnings;
/*     */   
/*     */   private final boolean mayHearDevtalk;
/*     */   
/*     */   public long paymentExpire;
/*     */   
/*     */   private final long[] ignored;
/*     */   
/*     */   private final long[] friends;
/*     */   
/*     */   private final byte[] friendcats;
/*     */   
/*     */   private long[] enemies;
/*     */   
/*     */   private final String lastip;
/*     */   
/*     */   private final String templateName;
/*     */   
/*     */   private final long bodyId;
/*     */   
/*     */   private final long buildingId;
/*     */   
/*     */   private final int damage;
/*     */   
/*     */   private final int hunger;
/*     */   
/* 189 */   public float nutrition = 0.0F;
/*     */ 
/*     */   
/*     */   private final int stunned;
/*     */ 
/*     */   
/*     */   private final int thirst;
/*     */ 
/*     */   
/*     */   private final int stamina;
/*     */ 
/*     */   
/*     */   private final byte sex;
/*     */ 
/*     */   
/*     */   private final long inventoryId;
/*     */   
/*     */   private final boolean onSurface;
/*     */   
/*     */   private final boolean unconscious;
/*     */   
/*     */   private final float posx;
/*     */   
/*     */   private final float posy;
/*     */   
/*     */   private final float posz;
/*     */   
/*     */   private final float rotation;
/*     */   
/*     */   private final int zoneid;
/*     */   
/*     */   private final boolean dead;
/*     */   
/*     */   private final boolean mute;
/*     */   
/*     */   private final long ver;
/*     */   
/*     */   private final long lastfaith;
/*     */   
/*     */   private final byte numfaith;
/*     */   
/*     */   public long money;
/*     */   
/*     */   private final boolean climbing;
/*     */   
/*     */   private final int age;
/*     */   
/*     */   private final long lastPolledAge;
/*     */   
/*     */   private final byte fat;
/*     */   
/*     */   private final long face;
/*     */   
/* 242 */   public byte blood = 0;
/*     */   
/*     */   private final int reputation;
/*     */   
/*     */   private final long lastPolledReputation;
/*     */   
/*     */   private final int title;
/*     */   
/*     */   private final int secondTitle;
/*     */   
/*     */   private final int[] titleArr;
/* 253 */   public long pet = -10L;
/*     */   
/* 255 */   public long nicotineTime = 0L;
/*     */   
/* 257 */   public long alcoholTime = 0L;
/*     */   
/* 259 */   public float alcohol = 0.0F;
/*     */   
/* 261 */   public float nicotine = 0.0F;
/*     */   
/*     */   public boolean logging = false;
/*     */   
/* 265 */   public long muteexpiry = 0L;
/*     */   
/* 267 */   public String mutereason = "";
/*     */   
/*     */   public boolean mayMute = false;
/*     */   
/*     */   public boolean overrideshop = false;
/*     */   
/* 273 */   public long lastModifiedRank = 0L;
/*     */   
/*     */   public int currentServer;
/*     */   
/*     */   public int lastServer;
/*     */   
/*     */   public long referrer;
/* 280 */   public String emailAdress = "";
/*     */   
/* 282 */   public String pwQuestion = "";
/*     */   
/* 284 */   public String pwAnswer = "";
/*     */   
/*     */   public boolean isPriest = false;
/*     */   
/* 288 */   public byte priestType = 0;
/*     */   
/* 290 */   public long lastChangedPriestType = 0L;
/*     */   
/* 292 */   public long bed = -10L;
/*     */   
/* 294 */   public int sleep = 0;
/*     */   
/* 296 */   public long creationDate = 0L;
/*     */   
/*     */   public boolean istheftwarned = false;
/*     */   
/*     */   public boolean noReimbLeft = false;
/*     */   
/*     */   public boolean deathProt = false;
/*     */   
/*     */   public byte fightmode;
/*     */   
/*     */   public long nextAffinity;
/*     */   
/* 308 */   public short detectionSecs = 0;
/*     */   
/* 310 */   public int tutLevel = 0;
/*     */   
/*     */   public boolean autofight = false;
/*     */   
/* 314 */   public long appointments = 0L;
/*     */   
/*     */   public boolean seesPAWin = false;
/*     */   
/*     */   public boolean isPA = false;
/*     */   
/*     */   public boolean mayAppointPA = false;
/*     */   
/* 322 */   public byte disease = 0;
/*     */   
/* 324 */   public long lastChangedKingdom = System.currentTimeMillis();
/*     */   
/* 326 */   public long lastLostChampion = 0L;
/*     */   
/* 328 */   public short championPoints = 0;
/*     */   
/* 330 */   public float champChanneling = 0.0F;
/*     */   
/*     */   public boolean voteKing = false;
/*     */   
/* 334 */   public Map<Integer, Long> cooldowns = new HashMap<>();
/*     */   
/* 336 */   public byte epicKingdom = 0;
/*     */   
/* 338 */   public int epicServerId = 0;
/*     */   
/* 340 */   public byte chaosKingdom = 0;
/*     */   public boolean hasFreeTransfer = false;
/* 342 */   public short hotaWins = 0;
/*     */ 
/*     */   
/* 345 */   public int karma = 0;
/*     */ 
/*     */   
/* 348 */   public int maxKarma = 0;
/*     */ 
/*     */   
/* 351 */   public int totalKarma = 0;
/*     */   
/* 353 */   public long abilities = 0L;
/*     */   
/* 355 */   public long flags = 0L;
/* 356 */   public long flags2 = 0L;
/* 357 */   public int abilityTitle = -1;
/* 358 */   public int scenarioKarma = 0;
/*     */ 
/*     */   
/*     */   public byte undeadType;
/*     */   
/*     */   public int undeadKills;
/*     */   
/*     */   public int undeadPKills;
/*     */   
/*     */   public int undeadPSecs;
/*     */   
/*     */   public long moneySalesEver;
/*     */   
/*     */   public int daysPrem;
/*     */   
/*     */   public long lastTicked;
/*     */   
/*     */   public int currentLoyaltyPoints;
/*     */   
/*     */   public int totalLoyaltyPoints;
/*     */   
/*     */   public int monthsPaidEver;
/*     */   
/*     */   public int monthsPaidInARow;
/*     */   
/*     */   public int monthsPaidSinceReset;
/*     */   
/*     */   public int silverPaidEver;
/*     */   
/*     */   public boolean hasAwards;
/*     */   
/*     */   public float calories;
/*     */   
/*     */   public float carbs;
/*     */   
/*     */   public float fats;
/*     */   
/*     */   public float proteins;
/*     */ 
/*     */   
/*     */   public PlayerMetaData(long aWurmid, String aName, String aPassword, String aSession, short chigh, short clong, short cwide, long aSessionExpiration, byte aPower, byte aDeity, float aAlign, float aFaith, float aFavor, byte aGod, byte realdeathc, long aLastChangedDeity, int aFatiguesecsleft, long aLastfatigue, long aLastwarned, long aLastcheated, long aPlantedSign, long aPlayingTime, byte aKingdom, int aRank, boolean aBanned, long aBanexpiry, String aBanreason, boolean aReimbursed, int aWarnings, boolean aMayHearDevtalk, long aPaymentExpire, long[] aIgnored, long[] aFriends, byte[] aCats, String aTemplateName, String aLastip, boolean aDead, boolean aMute, long aBodyId, long aBuildingId, int aDamage, int aHunger, int aStunned, int aThirst, int aStamina, byte aSex, long aInventoryId, boolean aOnSurface, boolean aUnconscious, float aPosx, float aPosy, float aPosz, float aRotation, int aZoneid, long version, long lastFaith, byte numFaith, long aMoney, boolean climb, byte changedKingd, int aAge, long aLastPolledAge, byte aFat, long _face, int rep, long lastPolledRep, int _title, int _secondTitle, int[] _titleArr) {
/* 399 */     this.wurmid = aWurmid;
/* 400 */     this.name = aName;
/* 401 */     this.password = aPassword;
/* 402 */     this.session = aSession;
/* 403 */     this.centhigh = chigh;
/* 404 */     this.centlong = clong;
/* 405 */     this.centwide = cwide;
/* 406 */     this.sessionExpiration = aSessionExpiration;
/* 407 */     this.power = aPower;
/* 408 */     this.deity = aDeity;
/* 409 */     this.align = aAlign;
/* 410 */     this.faith = aFaith;
/* 411 */     this.favor = aFavor;
/* 412 */     this.god = aGod;
/* 413 */     this.realdeathcounter = realdeathc;
/* 414 */     this.lastChangedDeity = aLastChangedDeity;
/* 415 */     this.fatiguesecsleft = aFatiguesecsleft;
/* 416 */     this.lastfatigue = aLastfatigue;
/* 417 */     this.lastwarned = aLastwarned;
/*     */     
/* 419 */     this.lastcheated = 0L;
/* 420 */     this.plantedSign = aPlantedSign;
/* 421 */     this.playingTime = aPlayingTime;
/* 422 */     this.kingdom = aKingdom;
/* 423 */     this.rank = aRank;
/* 424 */     this.banned = aBanned;
/* 425 */     this.banexpiry = aBanexpiry;
/* 426 */     this.banreason = aBanreason;
/* 427 */     this.reimbursed = aReimbursed;
/* 428 */     this.warnings = aWarnings;
/* 429 */     this.mayHearDevtalk = aMayHearDevtalk;
/* 430 */     this.paymentExpire = aPaymentExpire;
/* 431 */     this.ignored = aIgnored;
/* 432 */     this.friends = aFriends;
/* 433 */     this.friendcats = aCats;
/* 434 */     this.lastip = aLastip;
/* 435 */     this.dead = aDead;
/* 436 */     this.mute = aMute;
/* 437 */     this.templateName = aTemplateName;
/* 438 */     this.bodyId = aBodyId;
/* 439 */     this.buildingId = aBuildingId;
/* 440 */     this.damage = aDamage;
/* 441 */     this.hunger = aHunger;
/* 442 */     this.stunned = aStunned;
/* 443 */     this.thirst = aThirst;
/* 444 */     this.stamina = aStamina;
/* 445 */     this.sex = aSex;
/* 446 */     this.inventoryId = aInventoryId;
/* 447 */     this.onSurface = aOnSurface;
/* 448 */     this.unconscious = aUnconscious;
/* 449 */     this.posx = aPosx;
/* 450 */     this.posy = aPosy;
/* 451 */     this.posz = aPosz;
/* 452 */     this.rotation = aRotation;
/* 453 */     this.zoneid = aZoneid;
/* 454 */     this.lastfaith = lastFaith;
/* 455 */     this.numfaith = numFaith;
/* 456 */     this.money = aMoney;
/* 457 */     this.ver = version;
/* 458 */     this.climbing = climb;
/* 459 */     this.changedKingdom = changedKingd;
/* 460 */     this.age = aAge;
/* 461 */     this.lastPolledAge = aLastPolledAge;
/* 462 */     this.fat = aFat;
/* 463 */     this.face = _face;
/* 464 */     this.reputation = rep;
/* 465 */     this.lastPolledReputation = lastPolledRep;
/* 466 */     this.title = _title;
/* 467 */     this.secondTitle = _secondTitle;
/* 468 */     this.titleArr = _titleArr;
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
/*     */   public void save() throws IOException {
/* 481 */     Connection dbcon = null;
/* 482 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 485 */       CreaturePos pos = CreaturePos.getPosition(this.wurmid);
/* 486 */       if (pos != null) {
/*     */         
/* 488 */         pos.setPosX(this.posx);
/* 489 */         pos.setPosY(this.posy);
/* 490 */         pos.setPosZ(this.posz, false);
/* 491 */         pos.setRotation(this.rotation);
/* 492 */         pos.setZoneId(this.zoneid);
/* 493 */         pos.setLayer(0);
/*     */       } else {
/*     */         
/* 496 */         new CreaturePos(this.wurmid, this.posx, this.posy, this.posz, this.rotation, this.zoneid, 0, -10L, true);
/* 497 */       }  dbcon = DbConnector.getPlayerDbCon();
/* 498 */       if (exists(dbcon)) {
/*     */         
/* 500 */         ps = dbcon.prepareStatement("DELETE FROM PLAYERS WHERE WURMID=?");
/* 501 */         ps.setLong(1, this.wurmid);
/* 502 */         ps.executeUpdate();
/* 503 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/* 505 */       ps = dbcon.prepareStatement("insert into PLAYERS (MAYUSESHOP,PASSWORD,WURMID,LASTLOGOUT,PLAYINGTIME,TEMPLATENAME,SEX,CENTIMETERSHIGH,CENTIMETERSLONG,CENTIMETERSWIDE,INVENTORYID,BODYID,MONEYSALES,BUILDINGID,STAMINA,HUNGER,THIRST,IPADDRESS,REIMBURSED,PLANTEDSIGN,BANNED,PAYMENTEXPIRE,POWER,RANK,DEVTALK,WARNINGS,LASTWARNED,FAITH,DEITY,ALIGNMENT,GOD,FAVOR,LASTCHANGEDDEITY,REALDEATH,CHEATED,LASTFATIGUE,FATIGUE,DEAD,KINGDOM,SESSIONKEY,SESSIONEXPIRE,VERSION,MUTED,LASTFAITH,NUMFAITH,MONEY,CLIMBING,NUMSCHANGEDKINGDOM,AGE,LASTPOLLEDAGE,FAT,BANEXPIRY,BANREASON,FACE,REPUTATION,LASTPOLLEDREP,TITLE,PET,NICOTINE,NICOTINETIME,ALCOHOL,ALCOHOLTIME,LOGGING,MAYMUTE,MUTEEXPIRY,MUTEREASON,LASTSERVER,CURRENTSERVER,REFERRER,EMAIL,PWQUESTION,PWANSWER,PRIEST,BED,SLEEP,CREATIONDATE,THEFTWARNED,NOREIMB,DEATHPROT,FATIGUETODAY,FATIGUEYDAY,FIGHTMODE,NEXTAFFINITY,DETECTIONSECS,TUTORIALLEVEL,AUTOFIGHT,APPOINTMENTS,PA,APPOINTPA,PAWINDOW,NUTRITION,DISEASE,PRIESTTYPE,LASTCHANGEDPRIEST,LASTCHANGEDKINGDOM,LASTLOSTCHAMPION,CHAMPIONPOINTS,CHAMPCHANNELING,MUTETIMES,VOTEDKING,EPICKINGDOM,EPICSERVER,CHAOSKINGDOM,FREETRANSFER,HOTA_WINS,LASTMODIFIEDRANK,MAXRANK,KARMA,MAXKARMA,TOTALKARMA,BLOOD,FLAGS,FLAGS2,ABILITIES,ABILITYTITLE,SCENARIOKARMA,UNDEADTYPE,UNDEADKILLS,UNDEADPKILLS,UNDEADPSECS,NAME,CALORIES,CARBS,FATS,PROTEINS,SECONDTITLE) VALUES(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/* 506 */       ps.setBoolean(1, this.overrideshop);
/* 507 */       ps.setString(2, this.password);
/* 508 */       ps.setLong(3, this.wurmid);
/* 509 */       ps.setLong(4, System.currentTimeMillis());
/* 510 */       ps.setLong(5, this.playingTime);
/* 511 */       ps.setString(6, this.templateName);
/* 512 */       ps.setByte(7, this.sex);
/* 513 */       ps.setShort(8, this.centhigh);
/* 514 */       ps.setShort(9, this.centlong);
/* 515 */       ps.setShort(10, this.centwide);
/* 516 */       ps.setLong(11, this.inventoryId);
/* 517 */       ps.setLong(12, this.bodyId);
/* 518 */       ps.setLong(13, this.moneySalesEver);
/* 519 */       ps.setLong(14, this.buildingId);
/* 520 */       ps.setShort(15, (short)this.stamina);
/* 521 */       ps.setShort(16, (short)this.hunger);
/* 522 */       ps.setShort(17, (short)this.thirst);
/* 523 */       ps.setString(18, this.lastip);
/* 524 */       ps.setBoolean(19, this.reimbursed);
/* 525 */       ps.setLong(20, this.plantedSign);
/* 526 */       ps.setBoolean(21, this.banned);
/* 527 */       ps.setLong(22, this.paymentExpire);
/* 528 */       ps.setByte(23, this.power);
/* 529 */       ps.setInt(24, this.rank);
/* 530 */       ps.setBoolean(25, this.mayHearDevtalk);
/* 531 */       ps.setShort(26, (short)this.warnings);
/* 532 */       ps.setLong(27, this.lastwarned);
/* 533 */       ps.setFloat(28, this.faith);
/* 534 */       ps.setByte(29, this.deity);
/* 535 */       ps.setFloat(30, this.align);
/* 536 */       ps.setByte(31, this.god);
/* 537 */       ps.setFloat(32, this.favor);
/* 538 */       ps.setLong(33, this.lastChangedDeity);
/* 539 */       ps.setByte(34, this.realdeathcounter);
/* 540 */       ps.setLong(35, this.lastcheated);
/* 541 */       ps.setLong(36, this.lastfatigue);
/* 542 */       ps.setInt(37, this.fatiguesecsleft);
/* 543 */       ps.setBoolean(38, this.dead);
/* 544 */       ps.setByte(39, this.kingdom);
/* 545 */       ps.setString(40, this.session);
/* 546 */       ps.setLong(41, this.sessionExpiration);
/* 547 */       ps.setLong(42, this.ver);
/* 548 */       ps.setBoolean(43, this.mute);
/* 549 */       ps.setLong(44, this.lastfaith);
/* 550 */       ps.setByte(45, this.numfaith);
/* 551 */       ps.setLong(46, this.money);
/* 552 */       ps.setBoolean(47, this.climbing);
/* 553 */       ps.setByte(48, this.changedKingdom);
/* 554 */       ps.setInt(49, this.age);
/* 555 */       ps.setLong(50, this.lastPolledAge);
/* 556 */       ps.setByte(51, this.fat);
/* 557 */       ps.setLong(52, this.banexpiry);
/* 558 */       ps.setString(53, this.banreason);
/* 559 */       ps.setLong(54, this.face);
/* 560 */       ps.setInt(55, this.reputation);
/* 561 */       ps.setLong(56, this.lastPolledReputation);
/* 562 */       ps.setInt(57, this.title);
/* 563 */       ps.setLong(58, this.pet);
/* 564 */       ps.setFloat(59, this.nicotine);
/* 565 */       ps.setLong(60, this.nicotineTime);
/* 566 */       ps.setFloat(61, this.alcohol);
/* 567 */       ps.setLong(62, this.alcoholTime);
/* 568 */       ps.setBoolean(63, this.logging);
/* 569 */       ps.setBoolean(64, this.mayMute);
/* 570 */       ps.setLong(65, this.muteexpiry);
/* 571 */       ps.setString(66, this.mutereason);
/* 572 */       ps.setInt(67, this.lastServer);
/* 573 */       ps.setInt(68, this.currentServer);
/* 574 */       ps.setLong(69, this.referrer);
/* 575 */       ps.setString(70, this.emailAdress);
/* 576 */       ps.setString(71, this.pwQuestion);
/* 577 */       ps.setString(72, this.pwAnswer);
/* 578 */       ps.setBoolean(73, this.isPriest);
/* 579 */       ps.setLong(74, this.bed);
/* 580 */       ps.setInt(75, this.sleep);
/* 581 */       ps.setLong(76, this.creationDate);
/* 582 */       ps.setBoolean(77, this.istheftwarned);
/* 583 */       ps.setBoolean(78, this.noReimbLeft);
/* 584 */       ps.setBoolean(79, this.deathProt);
/* 585 */       ps.setInt(80, this.fatigueSecsToday);
/* 586 */       ps.setInt(81, this.fatigueSecsYday);
/* 587 */       ps.setByte(82, this.fightmode);
/* 588 */       ps.setLong(83, this.nextAffinity);
/* 589 */       ps.setShort(84, this.detectionSecs);
/* 590 */       ps.setInt(85, this.tutLevel);
/* 591 */       ps.setBoolean(86, this.autofight);
/* 592 */       ps.setLong(87, this.appointments);
/* 593 */       ps.setBoolean(88, this.isPA);
/* 594 */       ps.setBoolean(89, this.mayAppointPA);
/* 595 */       ps.setBoolean(90, this.seesPAWin);
/* 596 */       ps.setFloat(91, this.nutrition);
/* 597 */       ps.setByte(92, this.disease);
/* 598 */       ps.setByte(93, this.priestType);
/* 599 */       ps.setLong(94, this.lastChangedPriestType);
/* 600 */       ps.setLong(95, this.lastChangedKingdom);
/* 601 */       ps.setLong(96, this.lastLostChampion);
/* 602 */       ps.setShort(97, this.championPoints);
/* 603 */       ps.setFloat(98, this.champChanneling);
/* 604 */       ps.setShort(99, this.muteTimes);
/* 605 */       ps.setBoolean(100, this.voteKing);
/* 606 */       ps.setByte(101, this.epicKingdom);
/* 607 */       ps.setInt(102, this.epicServerId);
/* 608 */       ps.setInt(103, this.chaosKingdom);
/* 609 */       ps.setBoolean(104, this.hasFreeTransfer);
/* 610 */       ps.setShort(105, this.hotaWins);
/*     */       
/* 612 */       ps.setLong(106, this.lastModifiedRank);
/* 613 */       ps.setInt(107, this.maxRank);
/* 614 */       ps.setInt(108, this.karma);
/* 615 */       ps.setInt(109, this.maxKarma);
/* 616 */       ps.setInt(110, this.totalKarma);
/* 617 */       ps.setByte(111, this.blood);
/* 618 */       ps.setLong(112, this.flags);
/* 619 */       ps.setLong(113, this.flags2);
/* 620 */       ps.setLong(114, this.abilities);
/* 621 */       ps.setInt(115, this.abilityTitle);
/* 622 */       ps.setInt(116, this.scenarioKarma);
/*     */       
/* 624 */       ps.setByte(117, this.undeadType);
/* 625 */       ps.setInt(118, this.undeadKills);
/* 626 */       ps.setInt(119, this.undeadPKills);
/* 627 */       ps.setInt(120, this.undeadPSecs);
/*     */       
/* 629 */       ps.setString(121, this.name);
/* 630 */       ps.setFloat(122, this.calories);
/* 631 */       ps.setFloat(123, this.carbs);
/* 632 */       ps.setFloat(124, this.fats);
/* 633 */       ps.setFloat(125, this.proteins);
/*     */       
/* 635 */       ps.setInt(126, this.secondTitle);
/*     */       
/* 637 */       ps.executeUpdate();
/* 638 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 640 */       Players.getInstance().registerNewKingdom(this.wurmid, this.kingdom);
/*     */       
/* 642 */       ps = dbcon.prepareStatement("DELETE FROM FRIENDS WHERE WURMID=?");
/* 643 */       ps.setLong(1, this.wurmid);
/* 644 */       ps.executeUpdate();
/* 645 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 647 */       if (this.friends != null)
/*     */       {
/* 649 */         for (int x = 0; x < this.friends.length; x++) {
/*     */           
/* 651 */           ps = dbcon.prepareStatement(INSERT_FRIEND);
/* 652 */           ps.setLong(1, this.wurmid);
/* 653 */           ps.setLong(2, this.friends[x]);
/* 654 */           ps.setByte(3, this.friendcats[x]);
/* 655 */           ps.executeUpdate();
/* 656 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         } 
/*     */       }
/*     */       
/* 660 */       ps = dbcon.prepareStatement("DELETE FROM IGNORED WHERE WURMID=?");
/* 661 */       ps.setLong(1, this.wurmid);
/* 662 */       ps.executeUpdate();
/* 663 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 665 */       if (this.ignored != null)
/*     */       {
/* 667 */         for (int x = 0; x < this.ignored.length; x++) {
/*     */           
/* 669 */           ps = dbcon.prepareStatement(INSERT_IGNORED);
/* 670 */           ps.setLong(1, this.wurmid);
/* 671 */           ps.setLong(2, this.ignored[x]);
/* 672 */           ps.executeUpdate();
/* 673 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         } 
/*     */       }
/*     */       
/* 677 */       if (this.enemies != null)
/*     */       {
/* 679 */         for (int x = 0; x < this.enemies.length; x++) {
/*     */           
/* 681 */           ps = dbcon.prepareStatement(INSERT_ENEMY);
/* 682 */           ps.setLong(1, this.wurmid);
/* 683 */           ps.setLong(2, this.enemies[x]);
/* 684 */           ps.executeUpdate();
/* 685 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         } 
/*     */       }
/*     */       
/* 689 */       if (this.titleArr.length > 0)
/*     */       {
/* 691 */         for (int x = 0; x < this.titleArr.length; x++) {
/*     */           
/* 693 */           Titles.Title t = Titles.Title.getTitle(this.titleArr[x]);
/* 694 */           if (t != null) {
/*     */             
/* 696 */             ps = dbcon.prepareStatement("INSERT INTO TITLES (WURMID,TITLEID,TITLENAME) VALUES(?,?,?)");
/* 697 */             ps.setLong(1, this.wurmid);
/* 698 */             ps.setInt(2, this.titleArr[x]);
/* 699 */             ps.setString(3, t.getName(true));
/* 700 */             ps.executeUpdate();
/* 701 */             DbUtilities.closeDatabaseObjects(ps, null);
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 706 */       if (this.hasAwards) {
/*     */         
/* 708 */         ps = dbcon.prepareStatement("DELETE FROM AWARDS WHERE WURMID=?");
/* 709 */         ps.setLong(1, this.wurmid);
/* 710 */         ps.executeUpdate();
/* 711 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */         
/* 713 */         ps = dbcon.prepareStatement("INSERT INTO AWARDS(WURMID, DAYSPREM, MONTHSPREM, MONTHSEVER, CONSECMONTHS, SILVERSPURCHASED, LASTTICKEDPREM, CURRENTLOYALTY, TOTALLOYALTY) VALUES(?,?,?,?,?,?,?,?,?)");
/* 714 */         ps.setLong(1, this.wurmid);
/* 715 */         ps.setInt(2, this.daysPrem);
/* 716 */         ps.setInt(3, this.monthsPaidSinceReset);
/* 717 */         ps.setInt(4, this.monthsPaidEver);
/* 718 */         ps.setInt(5, this.monthsPaidInARow);
/* 719 */         ps.setInt(6, this.silverPaidEver);
/* 720 */         ps.setLong(7, this.lastTicked);
/* 721 */         ps.setInt(8, this.currentLoyaltyPoints);
/* 722 */         ps.setInt(9, this.totalLoyaltyPoints);
/* 723 */         ps.executeUpdate();
/* 724 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/* 726 */       if (this.cooldowns.size() > 0)
/*     */       {
/* 728 */         Cooldowns cd = Cooldowns.getCooldownsFor(this.wurmid, true);
/* 729 */         for (Map.Entry<Integer, Long> ent : this.cooldowns.entrySet())
/*     */         {
/* 731 */           cd.addCooldown(((Integer)ent.getKey()).intValue(), ((Long)ent.getValue()).longValue(), false);
/*     */         }
/*     */       }
/*     */     
/* 735 */     } catch (SQLException sqex) {
/*     */       
/* 737 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 738 */       throw new IOException(sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 742 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 743 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean exists(Connection dbcon) throws SQLException {
/*     */     boolean existed;
/* 750 */     PreparedStatement ps = null;
/* 751 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 754 */       ps = dbcon.prepareStatement("select NAME from PLAYERS where NAME=?");
/* 755 */       ps.setString(1, this.name);
/* 756 */       rs = ps.executeQuery();
/* 757 */       existed = rs.next();
/*     */     }
/*     */     finally {
/*     */       
/* 761 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/* 763 */     return existed;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */