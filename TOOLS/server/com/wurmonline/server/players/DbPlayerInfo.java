/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.behaviours.Methods;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.intra.MoneyTransfer;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.statistics.ChallengePointEnum;
/*      */ import com.wurmonline.server.statistics.ChallengeSummary;
/*      */ import com.wurmonline.server.steam.SteamId;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class DbPlayerInfo
/*      */   extends PlayerInfo
/*      */   implements MonetaryConstants
/*      */ {
/*   59 */   private static final Logger logger = Logger.getLogger(DbPlayerInfo.class.getName());
/*      */   
/*      */   private static final int MAX_HISTORY_IP = 10;
/*      */   
/*      */   private static final int MAX_HISTORY_EMAIL = 5;
/*      */   
/*      */   private static final String GET_PLAYER = "select * from PLAYERS where NAME=?";
/*      */   
/*      */   private static final String GET_TITLES = "select TITLEID from TITLES where WURMID=?";
/*      */   
/*      */   private static final String CHECK_PLAYER = "select NAME from PLAYERS where NAME=?";
/*      */   
/*      */   private static final String SAVE_PLAYER = "update PLAYERS set WURMID=?, PASSWORD=?, LASTLOGOUT=?, PLAYINGTIME=?,  REIMBURSED=?, BANNED=?, PAYMENTEXPIRE=?,POWER=?, RANK=?,LASTCHANGEDDEITY=?,FATIGUE=?, LASTFATIGUE=?, SESSIONKEY=?,SESSIONEXPIRE=?,VERSION=?,CREATIONDATE=?,FACE=?,REPUTATION=?,LASTPOLLEDREP=?,TITLE=?,CURRENTSERVER=?, LASTSERVER=?, EMAIL=?,PWQUESTION=?, PWANSWER=?,BED=?,SLEEP=?, FATIGUETODAY=?, FATIGUEYDAY=?, LASTCHANGEDKINGDOM=?, SECONDTITLE=? where NAME=?";
/*      */   
/*      */   private static final String CREATE_PLAYER = "insert into PLAYERS ( WURMID, PASSWORD, LASTLOGOUT, PLAYINGTIME, REIMBURSED,BANNED, PAYMENTEXPIRE,POWER,RANK,LASTCHANGEDDEITY,FATIGUE,LASTFATIGUE,SESSIONKEY,SESSIONEXPIRE,VERSION,CREATIONDATE,FACE,REPUTATION,LASTPOLLEDREP,TITLE,CURRENTSERVER, LASTSERVER,EMAIL,PWQUESTION,PWANSWER,BED,SLEEP,FATIGUETODAY,FATIGUEYDAY,LASTCHANGEDKINGDOM,SECONDTITLE,NAME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*      */   
/*      */   private static final String LOAD_AWARDS = "SELECT * FROM AWARDS WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_IPADDRESS = "update PLAYERS set IPADDRESS=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_REIMBURSED = "update PLAYERS set REIMBURSED=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PLANTEDSIGN = "update PLAYERS set PLANTEDSIGN=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_BANNED = "update PLAYERS set BANNED=?,BANREASON=?,BANEXPIRY=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_POWER = "update PLAYERS set POWER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PASSWORD = "update PLAYERS set PASSWORD=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_NAME = "update PLAYERS set NAME=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PAYMENTEXPIRE = "update PLAYERS set PAYMENTEXPIRE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ENEMYTERR = "update PLAYERS set ENEMYTERR=?, LASTMOVEDTERR=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FREETRANSFER = "update PLAYERS set FREETRANSFER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_RANK = "update PLAYERS set RANK=?, MAXRANK=?, LASTMODIFIEDRANK=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHAMPION = "update CHAMPIONS set POINTS=?,NAME=? WHERE WURMID=? AND CURRENT=1";
/*      */   
/*      */   private static final String ADD_CHAMPION = "INSERT INTO CHAMPIONS(POINTS,NAME,WURMID,CURRENT) VALUES (?,?,?,1)";
/*      */   
/*      */   private static final String SET_INACTIVE_CHAMPION = "update CHAMPIONS set POINTS=?,NAME=?,CURRENT=0 WHERE WURMID=?";
/*      */   
/*      */   private static final String SET_UNDEAD = "update PLAYERS SET UNDEADTYPE=?,UNDEADKILLS=?,UNDEADPKILLS=?,UNDEADPSECS=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MONEY = "update PLAYERS set MONEY=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MONEYSALES = "update PLAYERS set MONEYSALES =? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_EMAIL = "update PLAYERS set EMAIL=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_APPOINTMENTS = "update PLAYERS set APPOINTMENTS=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PRIEST = "update PLAYERS set PRIEST=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_DEITY = "update PLAYERS set DEITY=? WHERE NAME=?";
/*      */   
/*      */   private static final String TOUCH_JOAT = "update PLAYERS set LASTJOAT=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_GOD = "update PLAYERS set GOD=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MODELNAME = "update PLAYERS set MODELNAME=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FAITH = "update PLAYERS set FAITH=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FAVOR = "update PLAYERS set FAVOR=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHEATED = "update PLAYERS set CHEATED=?,CHEATREASON=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CLIMBING = "update PLAYERS set CLIMBING=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ALIGNMENT = "update PLAYERS set ALIGNMENT=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_VERSION = "update PLAYERS set VERSION=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_LASTTRIGGER = "update PLAYERS set LASTTRIGGER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_DEAD = "update PLAYERS set DEAD=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FACE = "update PLAYERS set FACE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MUTED = "update PLAYERS set MUTED=?,MUTEREASON=?,MUTEEXPIRY=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_REALDEATH = "update PLAYERS set REALDEATH=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHANGED_DEITY = "update PLAYERS set LASTCHANGEDDEITY=? WHERE NAME=?";
/*      */   private static final String WARN = "update PLAYERS set WARNINGS=?, LASTWARNED=? WHERE NAME=?";
/*      */   private static final String SET_SESSION = "update PLAYERS set SESSIONKEY=?, SESSIONEXPIRE=? WHERE NAME=?";
/*      */   private static final String SET_FATIGUE = "update PLAYERS set FATIGUE=?,FATIGUETODAY=?, LASTFATIGUE=? WHERE NAME=?";
/*      */   private static final String SWITCH_FATIGUE = "update PLAYERS set FATIGUETODAY=?, FATIGUEYDAY=? WHERE NAME=?";
/*      */   private static final String SET_NUMCHANGEKINGDOM = "update PLAYERS set NUMSCHANGEDKINGDOM=? WHERE NAME=?";
/*  153 */   private static final String ADD_FRIEND = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO FRIENDS (WURMID,FRIEND,CATEGORY,NOTE) VALUES(?,?,?,?)" : "INSERT IGNORE INTO FRIENDS (WURMID,FRIEND,CATEGORY,NOTE) VALUES(?,?,?,?)";
/*      */ 
/*      */   
/*      */   private static final String UPDATE_FRIEND = "UPDATE FRIENDS SET CATEGORY=?,NOTE=? WHERE WURMID=? AND FRIEND=?";
/*      */   
/*      */   private static final String LOAD_FRIENDS = "SELECT FRIEND,CATEGORY,NOTE FROM FRIENDS WHERE WURMID=?";
/*      */   
/*      */   private static final String REMOVE_FRIEND = "DELETE FROM FRIENDS WHERE WURMID=? AND FRIEND=?";
/*      */   
/*  162 */   private static final String ADD_ENEMY = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ENEMIES (WURMID,ENEMY) VALUES(?,?)" : "INSERT IGNORE INTO ENEMIES (WURMID,ENEMY) VALUES(?,?)";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String REMOVE_ENEMY = "DELETE FROM ENEMIES WHERE WURMID=? AND ENEMY=?";
/*      */ 
/*      */ 
/*      */   
/*  170 */   private static final String ADD_IGNORED = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO IGNORED (WURMID,IGNOREE) VALUES(?,?)" : "INSERT IGNORE INTO IGNORED (WURMID,IGNOREE) VALUES(?,?)";
/*      */   
/*      */   private static final String LOAD_IGNORED = "SELECT IGNOREE FROM IGNORED WHERE WURMID=?";
/*      */   
/*      */   private static final String REMOVE_IGNORED = "DELETE FROM IGNORED WHERE WURMID=? AND IGNOREE=?";
/*      */   
/*      */   private static final String SET_NUMFAITH = "update PLAYERS set NUMFAITH=?, LASTFAITH=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_SEX = "update PLAYERS set SEX=? WHERE NAME=?";
/*      */   
/*      */   private static final String ADD_TITLE = "INSERT INTO TITLES (WURMID, TITLEID, TITLENAME) VALUES(?,?,?)";
/*      */   
/*      */   private static final String REMOVE_TITLE = "DELETE FROM TITLES WHERE WURMID=? AND TITLEID=?";
/*      */   
/*      */   private static final String SET_REPUTATION = "update PLAYERS set REPUTATION=?, LASTPOLLEDREP=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PET = "update PLAYERS set PET=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_NICOTINE = "update PLAYERS set NICOTINE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_NICOTINETIME = "update PLAYERS set NICOTINETIME=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ALCOHOL = "update PLAYERS set ALCOHOL=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ALCOHOLTIME = "update PLAYERS set ALCOHOLTIME=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MAYMUTE = "update PLAYERS set MAYMUTE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_REFERRER = "update PLAYERS set REFERRER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_BED = "update PLAYERS set BED=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_LASTCHANGEDVILLAGE = "update PLAYERS set CHANGEDVILLAGE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MAYUSESHOP = "update PLAYERS set MAYUSESHOP=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_THEFTWARNED = "update PLAYERS set THEFTWARNED=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_NOREIMB = "update PLAYERS set NOREIMB=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_DEATHPROT = "update PLAYERS set DEATHPROT=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_SLEEP = "update PLAYERS set SLEEP=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_DEVTALK = "update PLAYERS set DEVTALK=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_VOTEDKING = "update PLAYERS set VOTEDKING=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CURRENTSERVER = "update PLAYERS set LASTSERVER=?,CURRENTSERVER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FIGHTMODE = "update PLAYERS set FIGHTMODE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_NEXTAFFINITY = "update PLAYERS set NEXTAFFINITY=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MOVEDINVENTORY = "update PLAYERS set MOVEDINV=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_TUTORIAL = "update PLAYERS set TUTORIALLEVEL=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_AUTOFIGHT = "update PLAYERS set AUTOFIGHT=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_VEHICLE = "update PLAYERS set VEHICLE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PA = "update PLAYERS set PA=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_APPOINTPA = "update PLAYERS set APPOINTPA=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PAWINDOW = "update PLAYERS set PAWINDOW=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PRIESTTYPE = "update PLAYERS set PRIESTTYPE=?,LASTCHANGEDPRIEST=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_HASSKILLGAIN = "update PLAYERS set HASSKILLGAIN=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHANGEDKINGDOM = "update PLAYERS set LASTCHANGEDKINGDOM=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_LOSTCHAMPION = "update PLAYERS set LASTLOSTCHAMPION=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHAMPIONPOINTS = "update PLAYERS set CHAMPIONPOINTS=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHAMPCHANNELING = "update PLAYERS set CHAMPCHANNELING=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_MUTETIMES = "update PLAYERS set MUTETIMES=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_EPICLOCATION = "update PLAYERS set EPICKINGDOM=?, EPICSERVER=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_CHAOSKINGDOM = "update PLAYERS set CHAOSKINGDOM=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_HOTA_WINS = "update PLAYERS set HOTA_WINS=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_SPAMMODE = "update PLAYERS set SPAMMODE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_BLOOD = "update PLAYERS set BLOOD=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_FLAGS = "update PLAYERS set FLAGS=?,FLAGS2=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ABILITIES = "update PLAYERS set ABILITIES=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_ABILITYTITLE = "update PLAYERS set ABILITYTITLE=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_KARMAVALUES = "update PLAYERS set KARMA=?, MAXKARMA=?, TOTALKARMA=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_SCENARIOKARMA = "update PLAYERS set SCENARIOKARMA=? WHERE NAME=?";
/*      */   
/*      */   private static final String SET_PASSRETRIEVAL = "update PLAYERS set PWQUESTION=?,PWANSWER=? WHERE NAME=?";
/*      */   
/*      */   private static final String GET_HISTORY_IPS = "SELECT * FROM PLAYERHISTORYIPS WHERE PLAYERID=?";
/*      */   
/*      */   private static final String ADD_HISTORY_IP = "INSERT INTO PLAYERHISTORYIPS(PLAYERID,IPADDRESS,FIRSTUSED,LASTUSED) VALUES(?,?,?,?)";
/*      */   
/*      */   private static final String UPDATE_HISTORY_IP = "UPDATE PLAYERHISTORYIPS SET LASTUSED=? WHERE PLAYERID=? AND IPADDRESS=?";
/*      */   
/*      */   private static final String DELETE_HISTORY_IP = "DELETE FROM PLAYERHISTORYIPS WHERE PLAYERID=? AND IPADDRESS=?";
/*      */   
/*      */   private static final String GET_HISTORY_EMAIL = "SELECT * FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=?";
/*      */   
/*      */   private static final String ADD_HISTORY_EMAIL = "INSERT INTO PLAYEREHISTORYEMAIL(PLAYERID,EMAIL_ADDRESS,DATED) VALUES(?,?,?)";
/*      */   
/*      */   private static final String DELETE_HISTORY_EMAIL = "DELETE FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=? AND EMAIL_ADDRESS=?";
/*      */   private static final String GET_HISTORY_STEAM_IDS = "SELECT * FROM STEAM_IDS WHERE PLAYER_ID=?";
/*      */   private static final String ADD_HISTORY_STEAM_ID = "INSERT INTO STEAM_IDS(PLAYER_ID,STEAM_ID,FIRST_USED,LAST_USED) VALUES(?,?,?,?)";
/*      */   private static final String UPDATE_HISTORY_STEAM_ID = "UPDATE STEAM_IDS SET LAST_USED=? WHERE PLAYER_ID=? AND STEAM_ID=?";
/*      */   private static final String DELETE_HISTORY_STEAM_ID = "DELETE FROM STEAM_IDS WHERE PLAYER_ID=? AND STEAM_ID=?";
/*      */   
/*      */   DbPlayerInfo(String filename) {
/*  293 */     super(filename);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void load() throws IOException {
/*  299 */     if (this.loaded)
/*      */       return; 
/*  301 */     Connection dbcon = null;
/*  302 */     PreparedStatement ps = null;
/*  303 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  306 */       dbcon = DbConnector.getPlayerDbCon();
/*  307 */       ps = dbcon.prepareStatement("select * from PLAYERS where NAME=?");
/*  308 */       ps.setString(1, this.name);
/*  309 */       rs = ps.executeQuery();
/*  310 */       boolean existed = false;
/*  311 */       if (rs.next()) {
/*      */         
/*  313 */         this.wurmId = rs.getLong("WURMID");
/*  314 */         this.password = rs.getString("PASSWORD");
/*  315 */         this.playingTime = rs.getLong("PLAYINGTIME");
/*  316 */         this.reimbursed = rs.getBoolean("REIMBURSED");
/*  317 */         this.plantedSign = rs.getLong("PLANTEDSIGN");
/*  318 */         this.banned = rs.getBoolean("BANNED");
/*  319 */         this.power = rs.getByte("POWER");
/*  320 */         this.rank = rs.getInt("RANK");
/*  321 */         this.maxRank = rs.getInt("MAXRANK");
/*  322 */         this.lastModifiedRank = rs.getLong("LASTMODIFIEDRANK");
/*  323 */         this.mayHearDevTalk = rs.getBoolean("DEVTALK");
/*  324 */         this.paymentExpireDate = rs.getLong("PAYMENTEXPIRE");
/*  325 */         this.lastLogout = rs.getLong("LASTLOGOUT");
/*  326 */         this.lastWarned = rs.getLong("LASTWARNED");
/*  327 */         this.warnings = rs.getShort("WARNINGS");
/*  328 */         this.lastCheated = rs.getLong("CHEATED");
/*  329 */         this.lastFatigue = rs.getLong("LASTFATIGUE");
/*  330 */         this.fatigueSecsLeft = rs.getInt("FATIGUE");
/*  331 */         this.fatigueSecsToday = rs.getInt("FATIGUETODAY");
/*  332 */         this.fatigueSecsYesterday = rs.getInt("FATIGUEYDAY");
/*  333 */         this.dead = rs.getBoolean("DEAD");
/*  334 */         this.sessionKey = rs.getString("SESSIONKEY");
/*  335 */         this.sessionExpiration = rs.getLong("SESSIONEXPIRE");
/*  336 */         this.version = rs.getLong("VERSION");
/*  337 */         this.money = rs.getLong("MONEY");
/*  338 */         this.climbing = rs.getBoolean("CLIMBING");
/*  339 */         this.banexpiry = rs.getLong("BANEXPIRY");
/*  340 */         this.banreason = rs.getString("BANREASON");
/*  341 */         if (this.banreason == null)
/*  342 */           this.banreason = ""; 
/*  343 */         this.logging = rs.getBoolean("LOGGING");
/*  344 */         this.referrer = rs.getLong("REFERRER");
/*  345 */         this.appointments = rs.getLong("APPOINTMENTS");
/*  346 */         this.hasFreeTransfer = rs.getBoolean("FREETRANSFER");
/*  347 */         this.votedKing = rs.getBoolean("VOTEDKING");
/*  348 */         this.sex = rs.getByte("SEX");
/*  349 */         if (this.sessionKey == null)
/*  350 */           this.sessionKey = ""; 
/*  351 */         if (this.playingTime < 0L)
/*  352 */           this.playingTime = 0L; 
/*  353 */         if (this.playingTime > 0L) {
/*  354 */           logger.log(Level.INFO, this.name + " has played " + Methods.getTimeString(this.playingTime) + " at load.");
/*      */         }
/*      */         
/*  357 */         this.alignment = rs.getFloat("ALIGNMENT");
/*  358 */         byte deityNum = rs.getByte("DEITY");
/*  359 */         if (deityNum > 0) {
/*      */           
/*  361 */           Deity d = Deities.getDeity(deityNum);
/*  362 */           this.deity = d;
/*      */         }
/*      */         else {
/*      */           
/*  366 */           this.deity = null;
/*      */         } 
/*  368 */         this.favor = rs.getFloat("FAVOR");
/*  369 */         this.faith = rs.getFloat("FAITH");
/*  370 */         byte gid = rs.getByte("GOD");
/*  371 */         if (gid > 0) {
/*      */           
/*  373 */           Deity d = Deities.getDeity(gid);
/*  374 */           this.god = d;
/*      */         } 
/*  376 */         this.lastChangedDeity = rs.getLong("LASTCHANGEDDEITY");
/*  377 */         this.changedKingdom = rs.getByte("NUMSCHANGEDKINGDOM");
/*  378 */         this.realdeath = rs.getByte("REALDEATH");
/*  379 */         this.muted = rs.getBoolean("MUTED");
/*  380 */         this.muteTimes = rs.getShort("MUTETIMES");
/*  381 */         this.lastFaith = rs.getLong("LASTFAITH");
/*  382 */         this.numFaith = rs.getByte("NUMFAITH");
/*  383 */         this.creationDate = rs.getLong("CREATIONDATE");
/*  384 */         this.face = rs.getLong("FACE");
/*  385 */         this.reputation = rs.getInt("REPUTATION");
/*  386 */         this.lastPolledReputation = rs.getLong("LASTPOLLEDREP");
/*  387 */         if (this.lastPolledReputation == 0L)
/*  388 */           this.lastPolledReputation = System.currentTimeMillis(); 
/*  389 */         int titnum = rs.getInt("TITLE");
/*  390 */         if (titnum > 0) {
/*  391 */           this.title = Titles.Title.getTitle(titnum);
/*      */         }
/*      */         try {
/*  394 */           int secTitleNum = rs.getInt("SECONDTITLE");
/*  395 */           if (secTitleNum > 0)
/*      */           {
/*  397 */             this.secondTitle = Titles.Title.getTitle(secTitleNum);
/*      */           }
/*      */         }
/*  400 */         catch (SQLException ex) {
/*      */           
/*  402 */           logger.severe("You may need to run the script addSecondTitle.sql!");
/*  403 */           logger.severe(ex.getMessage());
/*  404 */           this.secondTitle = null;
/*      */         } 
/*  406 */         this.pet = rs.getLong("PET");
/*  407 */         this.nicotine = rs.getFloat("NICOTINE");
/*  408 */         this.alcohol = rs.getFloat("ALCOHOL");
/*  409 */         this.nicotineAddiction = rs.getLong("NICOTINETIME");
/*  410 */         this.alcoholAddiction = rs.getLong("ALCOHOLTIME");
/*  411 */         this.mayMute = rs.getBoolean("MAYMUTE");
/*  412 */         this.overRideShop = rs.getBoolean("MAYUSESHOP");
/*  413 */         this.muteexpiry = rs.getLong("MUTEEXPIRY");
/*  414 */         this.mutereason = rs.getString("MUTEREASON");
/*  415 */         this.lastServer = rs.getInt("LASTSERVER");
/*  416 */         this.currentServer = rs.getInt("CURRENTSERVER");
/*  417 */         this.emailAddress = rs.getString("EMAIL");
/*  418 */         this.pwQuestion = rs.getString("PWQUESTION");
/*  419 */         this.pwAnswer = rs.getString("PWANSWER");
/*  420 */         this.isPriest = rs.getBoolean("PRIEST");
/*  421 */         this.bed = rs.getLong("BED");
/*  422 */         this.sleep = rs.getInt("SLEEP");
/*  423 */         this.isTheftWarned = rs.getBoolean("THEFTWARNED");
/*  424 */         this.noReimbursementLeft = rs.getBoolean("NOREIMB");
/*  425 */         this.deathProtected = rs.getBoolean("DEATHPROT");
/*  426 */         this.lastChangedVillage = rs.getLong("CHANGEDVILLAGE");
/*  427 */         this.fightmode = rs.getByte("FIGHTMODE");
/*  428 */         this.nextAffinity = rs.getLong("NEXTAFFINITY");
/*  429 */         this.tutorialLevel = rs.getInt("TUTORIALLEVEL");
/*  430 */         this.autoFighting = rs.getBoolean("AUTOFIGHT");
/*  431 */         this.playerAssistant = rs.getBoolean("PA");
/*  432 */         this.mayAppointPlayerAssistant = rs.getBoolean("APPOINTPA");
/*  433 */         this.seesPlayerAssistantWindow = rs.getBoolean("PAWINDOW");
/*  434 */         this.lastTaggedKindom = rs.getByte("ENEMYTERR");
/*  435 */         this.lastMovedBetweenKingdom = rs.getLong("LASTMOVEDTERR");
/*  436 */         this.priestType = rs.getByte("PRIESTTYPE");
/*  437 */         this.lastChangedPriestType = rs.getLong("LASTCHANGEDPRIEST");
/*  438 */         this.hasMovedInventory = rs.getBoolean("MOVEDINV");
/*  439 */         this.hasSkillGain = rs.getBoolean("HASSKILLGAIN");
/*  440 */         this.lastTriggerEffect = rs.getInt("LASTTRIGGER");
/*  441 */         this.lastChangedKindom = rs.getLong("LASTCHANGEDKINGDOM");
/*  442 */         this.championTimeStamp = rs.getLong("LASTLOSTCHAMPION");
/*  443 */         this.championPoints = rs.getShort("CHAMPIONPOINTS");
/*  444 */         this.champChanneling = rs.getFloat("CHAMPCHANNELING");
/*  445 */         this.epicKingdom = rs.getByte("EPICKINGDOM");
/*  446 */         this.epicServerId = rs.getInt("EPICSERVER");
/*  447 */         this.chaosKingdom = rs.getByte("CHAOSKINGDOM");
/*  448 */         this.hotaWins = rs.getShort("HOTA_WINS");
/*  449 */         this.spamMode = rs.getBoolean("SPAMMODE");
/*  450 */         this.karma = rs.getInt("KARMA");
/*  451 */         this.maxKarma = rs.getInt("MAXKARMA");
/*  452 */         this.totalKarma = rs.getInt("TOTALKARMA");
/*  453 */         this.blood = rs.getByte("BLOOD");
/*  454 */         this.flags = rs.getLong("FLAGS");
/*  455 */         this.flags2 = rs.getLong("FLAGS2");
/*  456 */         this.abilities = rs.getLong("ABILITIES");
/*  457 */         this.abilityTitle = rs.getInt("ABILITYTITLE");
/*      */         
/*  459 */         this.undeadType = rs.getByte("UNDEADTYPE");
/*  460 */         this.undeadKills = rs.getInt("UNDEADKILLS");
/*  461 */         this.undeadPlayerKills = rs.getInt("UNDEADPKILLS");
/*  462 */         this.undeadPlayerSeconds = rs.getInt("UNDEADPSECS");
/*  463 */         this.moneyEarnedBySellingEver = rs.getLong("MONEYSALES");
/*  464 */         setFlagBits(this.flags);
/*  465 */         setFlag2Bits(this.flags2);
/*  466 */         setAbilityBits(this.abilities);
/*  467 */         this.scenarioKarma = rs.getInt("SCENARIOKARMA");
/*  468 */         if (Servers.localServer.id == this.currentServer || Servers.localServer.LOGINSERVER)
/*      */         {
/*  470 */           if (this.paymentExpireDate > 0L)
/*  471 */             this.awards = loadAward(this.wurmId); 
/*      */         }
/*  473 */         existed = true;
/*      */       } 
/*  475 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  476 */       if (!existed) {
/*  477 */         throw new IOException("No such player - " + this.name);
/*      */       }
/*      */       
/*  480 */       loadIgnored(this.wurmId);
/*  481 */       loadFriends(this.wurmId);
/*      */       
/*  483 */       loadTitles(this.wurmId);
/*  484 */       loadHistoryIPs(this.wurmId);
/*  485 */       loadHistorySteamIds(this.wurmId);
/*  486 */       loadHistoryEmails(this.wurmId);
/*      */       
/*  488 */       this.loaded = true;
/*  489 */       PlayerInfoFactory.addPlayerInfo(this);
/*      */     }
/*  491 */     catch (SQLException sqex) {
/*      */       
/*  493 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage());
/*  494 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  498 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  499 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean exists(Connection dbcon) throws SQLException {
/*  505 */     PreparedStatement ps = null;
/*  506 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  509 */       ps = dbcon.prepareStatement("select NAME from PLAYERS where NAME=?");
/*  510 */       ps.setString(1, this.name);
/*  511 */       rs = ps.executeQuery();
/*  512 */       return rs.next();
/*      */     }
/*      */     finally {
/*      */       
/*  516 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void save() throws IOException {
/*  523 */     Connection dbcon = null;
/*  524 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  527 */       dbcon = DbConnector.getPlayerDbCon();
/*  528 */       if (exists(dbcon)) {
/*  529 */         ps = dbcon.prepareStatement("update PLAYERS set WURMID=?, PASSWORD=?, LASTLOGOUT=?, PLAYINGTIME=?,  REIMBURSED=?, BANNED=?, PAYMENTEXPIRE=?,POWER=?, RANK=?,LASTCHANGEDDEITY=?,FATIGUE=?, LASTFATIGUE=?, SESSIONKEY=?,SESSIONEXPIRE=?,VERSION=?,CREATIONDATE=?,FACE=?,REPUTATION=?,LASTPOLLEDREP=?,TITLE=?,CURRENTSERVER=?, LASTSERVER=?, EMAIL=?,PWQUESTION=?, PWANSWER=?,BED=?,SLEEP=?, FATIGUETODAY=?, FATIGUEYDAY=?, LASTCHANGEDKINGDOM=?, SECONDTITLE=? where NAME=?");
/*      */       } else {
/*      */         
/*  532 */         ps = dbcon.prepareStatement("insert into PLAYERS ( WURMID, PASSWORD, LASTLOGOUT, PLAYINGTIME, REIMBURSED,BANNED, PAYMENTEXPIRE,POWER,RANK,LASTCHANGEDDEITY,FATIGUE,LASTFATIGUE,SESSIONKEY,SESSIONEXPIRE,VERSION,CREATIONDATE,FACE,REPUTATION,LASTPOLLEDREP,TITLE,CURRENTSERVER, LASTSERVER,EMAIL,PWQUESTION,PWANSWER,BED,SLEEP,FATIGUETODAY,FATIGUEYDAY,LASTCHANGEDKINGDOM,SECONDTITLE,NAME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
/*  533 */         this.lastServer = Servers.localServer.id;
/*  534 */         this.currentServer = Servers.localServer.id;
/*      */       } 
/*  536 */       ps.setLong(1, this.wurmId);
/*  537 */       ps.setString(2, this.password);
/*      */       
/*  539 */       ps.setLong(3, this.lastLogout);
/*      */       
/*  541 */       ps.setLong(4, this.playingTime);
/*      */       
/*  543 */       ps.setBoolean(5, this.reimbursed);
/*      */       
/*  545 */       ps.setBoolean(6, this.banned);
/*      */       
/*  547 */       ps.setLong(7, this.paymentExpireDate);
/*  548 */       ps.setByte(8, this.power);
/*  549 */       ps.setInt(9, this.rank);
/*  550 */       ps.setLong(10, this.lastChangedDeity);
/*  551 */       ps.setInt(11, this.fatigueSecsLeft);
/*  552 */       ps.setLong(12, this.lastFatigue);
/*  553 */       ps.setString(13, this.sessionKey);
/*  554 */       ps.setLong(14, this.sessionExpiration);
/*  555 */       ps.setLong(15, WurmCalendar.currentTime);
/*  556 */       ps.setLong(16, this.creationDate);
/*  557 */       ps.setLong(17, this.face);
/*  558 */       ps.setInt(18, this.reputation);
/*  559 */       ps.setLong(19, this.lastPolledReputation);
/*  560 */       if (this.title != null) {
/*  561 */         ps.setInt(20, this.title.id);
/*      */       } else {
/*  563 */         ps.setInt(20, 0);
/*  564 */       }  ps.setInt(21, this.currentServer);
/*  565 */       ps.setInt(22, this.lastServer);
/*  566 */       ps.setString(23, this.emailAddress);
/*  567 */       ps.setString(24, this.pwQuestion);
/*      */       
/*  569 */       if (this.pwAnswer.length() > 20)
/*  570 */         this.pwAnswer = this.pwAnswer.substring(0, 20); 
/*  571 */       ps.setString(25, this.pwAnswer);
/*  572 */       ps.setLong(26, this.bed);
/*  573 */       ps.setInt(27, this.sleep);
/*  574 */       ps.setInt(28, this.fatigueSecsToday);
/*  575 */       ps.setInt(29, this.fatigueSecsYesterday);
/*  576 */       ps.setLong(30, this.lastChangedKindom);
/*  577 */       if (this.secondTitle != null) {
/*  578 */         ps.setInt(31, this.secondTitle.id);
/*      */       } else {
/*  580 */         ps.setInt(31, 0);
/*  581 */       }  ps.setString(32, this.name);
/*  582 */       ps.executeUpdate();
/*      */     }
/*  584 */     catch (SQLException sqex) {
/*      */       
/*  586 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  587 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  591 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  592 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIpaddress() {
/*  599 */     if (this.ipaddress == null) {
/*  600 */       this.ipaddress = "";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  632 */     return this.ipaddress;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIpaddress(String address) throws IOException {
/*  638 */     Connection dbcon = null;
/*  639 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  642 */       dbcon = DbConnector.getPlayerDbCon();
/*  643 */       if (exists(dbcon)) {
/*      */         
/*  645 */         ps = dbcon.prepareStatement("update PLAYERS set IPADDRESS=? WHERE NAME=?");
/*  646 */         ps.setString(1, address);
/*  647 */         ps.setString(2, this.name);
/*  648 */         ps.executeUpdate();
/*      */         
/*  650 */         long now = System.currentTimeMillis();
/*      */         
/*  652 */         if (this.historyIPStart.containsKey(address)) {
/*      */           
/*  654 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  655 */           ps = dbcon.prepareStatement("UPDATE PLAYERHISTORYIPS SET LASTUSED=? WHERE PLAYERID=? AND IPADDRESS=?");
/*  656 */           ps.setLong(1, now);
/*  657 */           ps.setLong(2, this.wurmId);
/*  658 */           ps.setString(3, address);
/*  659 */           ps.executeUpdate();
/*      */         }
/*      */         else {
/*      */           
/*  663 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  664 */           ps = dbcon.prepareStatement("INSERT INTO PLAYERHISTORYIPS(PLAYERID,IPADDRESS,FIRSTUSED,LASTUSED) VALUES(?,?,?,?)");
/*  665 */           ps.setLong(1, this.wurmId);
/*  666 */           ps.setString(2, address);
/*  667 */           ps.setLong(3, now);
/*  668 */           ps.setLong(4, now);
/*  669 */           ps.executeUpdate();
/*  670 */           this.historyIPStart.put(address, Long.valueOf(now));
/*      */         } 
/*  672 */         this.historyIPLast.put(address, Long.valueOf(now));
/*      */         
/*  674 */         if (this.historyIPStart.size() > 10) {
/*      */ 
/*      */           
/*  677 */           long oldest = -1L;
/*  678 */           String ipAddress = "";
/*  679 */           for (Map.Entry<String, Long> entry : this.historyIPStart.entrySet()) {
/*      */             
/*  681 */             if (oldest == -1L || ((Long)entry.getValue()).longValue() < oldest) {
/*      */               
/*  683 */               ipAddress = entry.getKey();
/*  684 */               oldest = ((Long)entry.getValue()).longValue();
/*      */             } 
/*      */           } 
/*      */           
/*  688 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  689 */           ps = dbcon.prepareStatement("DELETE FROM PLAYERHISTORYIPS WHERE PLAYERID=? AND IPADDRESS=?");
/*  690 */           ps.setLong(1, this.wurmId);
/*  691 */           ps.setString(2, ipAddress);
/*  692 */           ps.executeUpdate();
/*  693 */           this.historyIPStart.remove(ipAddress);
/*  694 */           this.historyIPLast.remove(ipAddress);
/*      */         } 
/*      */       } 
/*  697 */       this.ipaddress = address;
/*      */     }
/*  699 */     catch (SQLException sqex) {
/*      */       
/*  701 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  702 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  706 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  707 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSteamId(SteamId aSteamId) throws IOException {
/*  714 */     Connection dbcon = null;
/*  715 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  718 */       dbcon = DbConnector.getPlayerDbCon();
/*  719 */       if (!exists(dbcon))
/*  720 */         return;  long now = System.currentTimeMillis();
/*  721 */       if (this.historySteamId.containsKey(aSteamId)) {
/*      */         
/*  723 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  724 */         ps = dbcon.prepareStatement("UPDATE STEAM_IDS SET LAST_USED=? WHERE PLAYER_ID=? AND STEAM_ID=?");
/*  725 */         ps.setLong(1, now);
/*  726 */         ps.setLong(2, this.wurmId);
/*  727 */         ps.setLong(3, aSteamId.getSteamID64());
/*  728 */         ps.executeUpdate();
/*  729 */         ((SteamIdHistory)this.historySteamId.get(aSteamId)).setLastUsed(now);
/*      */       }
/*      */       else {
/*      */         
/*  733 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  734 */         ps = dbcon.prepareStatement("INSERT INTO STEAM_IDS(PLAYER_ID,STEAM_ID,FIRST_USED,LAST_USED) VALUES(?,?,?,?)");
/*  735 */         ps.setLong(1, this.wurmId);
/*  736 */         ps.setLong(2, aSteamId.getSteamID64());
/*  737 */         ps.setLong(3, now);
/*  738 */         ps.setLong(4, now);
/*  739 */         ps.executeUpdate();
/*  740 */         SteamIdHistory history = new SteamIdHistory(this.wurmId, aSteamId, now, now);
/*  741 */         this.historySteamId.put(aSteamId, history);
/*      */       } 
/*  743 */       this.steamId = aSteamId;
/*      */     }
/*  745 */     catch (SQLException sqex) {
/*      */       
/*  747 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  748 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  752 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  753 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassRetrieval(String pwQ, String pwA) throws IOException {
/*  760 */     Connection dbcon = null;
/*  761 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  764 */       this.pwQuestion = pwQ;
/*  765 */       this.pwAnswer = pwA;
/*  766 */       dbcon = DbConnector.getPlayerDbCon();
/*  767 */       if (exists(dbcon))
/*      */       {
/*  769 */         ps = dbcon.prepareStatement("update PLAYERS set PWQUESTION=?,PWANSWER=? WHERE NAME=?");
/*  770 */         ps.setString(1, this.pwQuestion);
/*  771 */         ps.setString(2, this.pwAnswer);
/*  772 */         ps.setString(3, this.name);
/*  773 */         ps.executeUpdate();
/*      */       }
/*      */     
/*  776 */     } catch (SQLException sqex) {
/*      */       
/*  778 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  779 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  783 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  784 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReimbursed(boolean reimb) throws IOException {
/*  791 */     if (reimb != this.reimbursed) {
/*      */       
/*  793 */       Connection dbcon = null;
/*  794 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  797 */         this.reimbursed = reimb;
/*  798 */         dbcon = DbConnector.getPlayerDbCon();
/*  799 */         if (exists(dbcon))
/*      */         {
/*  801 */           ps = dbcon.prepareStatement("update PLAYERS set REIMBURSED=? WHERE NAME=?");
/*  802 */           ps.setBoolean(1, this.reimbursed);
/*  803 */           ps.setString(2, this.name);
/*  804 */           ps.executeUpdate();
/*      */         }
/*      */       
/*  807 */       } catch (SQLException sqex) {
/*      */         
/*  809 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  810 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  814 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  815 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlantedSign() throws IOException {
/*  823 */     Connection dbcon = null;
/*  824 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  827 */       this.plantedSign = System.currentTimeMillis();
/*  828 */       dbcon = DbConnector.getPlayerDbCon();
/*  829 */       if (exists(dbcon))
/*      */       {
/*  831 */         ps = dbcon.prepareStatement("update PLAYERS set PLANTEDSIGN=? WHERE NAME=?");
/*  832 */         ps.setLong(1, this.plantedSign);
/*  833 */         ps.setString(2, this.name);
/*  834 */         ps.executeUpdate();
/*      */       }
/*      */     
/*  837 */     } catch (SQLException sqex) {
/*      */       
/*  839 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  840 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  844 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  845 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBanned(boolean ban, String reason, long expiry) throws IOException {
/*  852 */     this.banned = ban;
/*  853 */     this.banexpiry = expiry;
/*  854 */     this.banreason = reason;
/*  855 */     if (this.banned) {
/*      */       
/*  857 */       Village v = Villages.getVillageForCreature(this.wurmId);
/*  858 */       if (v != null)
/*      */       {
/*  860 */         if ((v.getMayor()).wurmId == this.wurmId)
/*  861 */           v.setDemocracy(true); 
/*      */       }
/*  863 */       setRank(1000);
/*      */     } 
/*  865 */     Connection dbcon = null;
/*  866 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  869 */       dbcon = DbConnector.getPlayerDbCon();
/*  870 */       if (exists(dbcon))
/*      */       {
/*  872 */         ps = dbcon.prepareStatement("update PLAYERS set BANNED=?,BANREASON=?,BANEXPIRY=? WHERE NAME=?");
/*  873 */         ps.setBoolean(1, this.banned);
/*  874 */         ps.setString(2, this.banreason);
/*  875 */         ps.setLong(3, this.banexpiry);
/*  876 */         ps.setString(4, this.name);
/*  877 */         ps.executeUpdate();
/*      */       }
/*      */     
/*  880 */     } catch (SQLException sqex) {
/*      */       
/*  882 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  883 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  887 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  888 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPower(byte pow) throws IOException {
/*  895 */     if (this.power != pow) {
/*      */       
/*  897 */       this.power = pow;
/*  898 */       Connection dbcon = null;
/*  899 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  902 */         dbcon = DbConnector.getPlayerDbCon();
/*  903 */         if (exists(dbcon))
/*      */         {
/*  905 */           ps = dbcon.prepareStatement("update PLAYERS set POWER=? WHERE NAME=?");
/*  906 */           ps.setByte(1, this.power);
/*  907 */           ps.setString(2, this.name);
/*  908 */           ps.executeUpdate();
/*      */         }
/*      */       
/*  911 */       } catch (SQLException sqex) {
/*      */         
/*  913 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*  914 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  918 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  919 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaymentExpire(long paymentExpire) throws IOException {
/*  927 */     setPaymentExpire(paymentExpire, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPaymentExpire(long paymentExpire, boolean silverReturn) throws IOException {
/*  932 */     int numsPaying = PlayerInfoFactory.getNumberOfPayingPlayers();
/*  933 */     if (getPaymentExpire() <= 0L && paymentExpire > System.currentTimeMillis()) {
/*      */       
/*  935 */       if (this.awards == null)
/*      */       {
/*  937 */         this.awards = new Awards(this.wurmId, 0, 0, 0, 0, 0, System.currentTimeMillis(), 0, 0, true);
/*      */       }
/*  939 */       if (Servers.localServer.LOGINSERVER) {
/*      */         
/*  941 */         if (silverReturn) {
/*      */ 
/*      */           
/*  944 */           setMoney(this.money + 20000L);
/*  945 */           new MoneyTransfer(this.name, this.wurmId, this.money, 20000L, this.name + "Premium", (byte)3, "", false);
/*      */           
/*  947 */           logger.log(Level.INFO, "Added 2 silver to " + this.name + " as premium bonus.");
/*      */         } 
/*      */         
/*  950 */         if (!Servers.isThisATestServer()) {
/*      */           
/*  952 */           long timeplayed = 0L;
/*      */           
/*  954 */           if (this.lastLogin > 0L) {
/*  955 */             timeplayed = this.playingTime + System.currentTimeMillis() - this.lastLogin;
/*      */           } else {
/*  957 */             timeplayed = this.playingTime;
/*      */           } 
/*      */         } 
/*      */       } 
/*  961 */     } else if (Features.Feature.RETURNER_PACK_REGISTRATION.isEnabled()) {
/*      */       
/*  963 */       logger.log(Level.INFO, getName() + " already prem v2: " + ((System.currentTimeMillis() > getPaymentExpire()) ? 1 : 0) + ", received return pack: " + isFlagSet(47) + ", received gift pack: " + isFlagSet(46) + ", on this server: " + ((Servers.localServer.id == this.currentServer) ? 1 : 0) + " (on " + this.currentServer + ") Days play time: " + getDaysPlayTime());
/*      */       
/*  965 */       if (System.currentTimeMillis() > getPaymentExpire())
/*      */       {
/*      */         
/*  968 */         if (!isFlagSet(47))
/*      */         {
/*      */           
/*  971 */           if (!isFlagSet(46))
/*      */           {
/*      */             
/*  974 */             if (Servers.localServer.id == this.currentServer)
/*      */             {
/*      */               
/*  977 */               if (getPaymentExpire() > 0L && getDaysPlayTime() > 14L) {
/*      */                 
/*  979 */                 logger.log(Level.INFO, getName() + " setting to receive returners pack!");
/*  980 */                 setFlag(47, true);
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } 
/*  987 */     if (isFlagSet(8))
/*      */     {
/*  989 */       if (paymentExpire > this.paymentExpireDate)
/*  990 */         setFlag(8, false); 
/*      */     }
/*  992 */     this.paymentExpireDate = paymentExpire;
/*  993 */     setFlag(63, false);
/*      */     
/*  995 */     Connection dbcon = null;
/*  996 */     PreparedStatement ps = null;
/*      */ 
/*      */     
/*      */     try {
/*      */       try {
/* 1001 */         Skills skills = Players.getInstance().getPlayer(this.wurmId).getSkills();
/* 1002 */         if (skills != null && !Servers.isThisATestServer())
/*      */         {
/*      */           
/* 1005 */           skills.paying = true;
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1010 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */       
/* 1014 */       dbcon = DbConnector.getPlayerDbCon();
/* 1015 */       if (exists(dbcon))
/*      */       {
/* 1017 */         ps = dbcon.prepareStatement("update PLAYERS set PAYMENTEXPIRE=? WHERE NAME=?");
/* 1018 */         ps.setLong(1, this.paymentExpireDate);
/* 1019 */         ps.setString(2, this.name);
/* 1020 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1023 */     } catch (SQLException sqex) {
/*      */       
/* 1025 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1026 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1030 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1031 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1033 */     if (Servers.localServer.LOGINSERVER && numsPaying < 3000 && PlayerInfoFactory.getNumberOfPayingPlayers() == 3000)
/*      */     {
/* 1035 */       logger.log(Level.INFO, this.name + " NUMBER 3000!");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private long getDaysPlayTime() {
/* 1041 */     return this.playingTime / 86400000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCheated(String reason) {
/* 1049 */     if (this.lastCheated == 0L || "CLASS_CHECK_DISCONNECT".equals(reason)) {
/*      */       
/* 1051 */       this.lastCheated = System.currentTimeMillis();
/* 1052 */       Connection dbcon = null;
/* 1053 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1056 */         if (reason.length() > 254)
/* 1057 */           reason = reason.substring(0, 254); 
/* 1058 */         dbcon = DbConnector.getPlayerDbCon();
/* 1059 */         if (exists(dbcon))
/*      */         {
/* 1061 */           ps = dbcon.prepareStatement("update PLAYERS set CHEATED=?,CHEATREASON=? WHERE NAME=?");
/* 1062 */           ps.setLong(1, this.lastCheated);
/* 1063 */           ps.setString(2, reason);
/* 1064 */           ps.setString(3, this.name);
/* 1065 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1068 */       } catch (SQLException sqex) {
/*      */         
/* 1070 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1074 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1075 */         DbConnector.returnConnection(dbcon);
/* 1076 */         logger.log(Level.WARNING, this.name + " CHEATED " + reason);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFreeTransfer(boolean freeTrans) {
/* 1084 */     this.hasFreeTransfer = freeTrans;
/* 1085 */     Connection dbcon = null;
/* 1086 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1089 */       dbcon = DbConnector.getPlayerDbCon();
/* 1090 */       ps = dbcon.prepareStatement("update PLAYERS set FREETRANSFER=? WHERE NAME=?");
/* 1091 */       ps.setBoolean(1, this.hasFreeTransfer);
/* 1092 */       ps.setString(2, this.name);
/* 1093 */       ps.executeUpdate();
/*      */     }
/* 1095 */     catch (SQLException sqex) {
/*      */       
/* 1097 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1101 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1102 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastTaggedTerr(byte newKingdom) {
/* 1109 */     if (this.lastTaggedKindom != newKingdom) {
/*      */       
/* 1111 */       this.lastTaggedKindom = newKingdom;
/* 1112 */       this.lastMovedBetweenKingdom = System.currentTimeMillis();
/* 1113 */       Connection dbcon = null;
/* 1114 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1117 */         dbcon = DbConnector.getPlayerDbCon();
/* 1118 */         if (exists(dbcon))
/*      */         {
/* 1120 */           ps = dbcon.prepareStatement("update PLAYERS set ENEMYTERR=?, LASTMOVEDTERR=? WHERE NAME=?");
/* 1121 */           ps.setByte(1, this.lastTaggedKindom);
/* 1122 */           ps.setLong(2, this.lastMovedBetweenKingdom);
/* 1123 */           ps.setString(3, this.name);
/* 1124 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1127 */       } catch (SQLException sqex) {
/*      */         
/* 1129 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1133 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1134 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRank(int r) throws IOException {
/* 1142 */     if (this.rank != Math.max(1000, r)) {
/*      */       
/* 1144 */       if (this.realdeath > 0 && r > this.rank)
/*      */       {
/* 1146 */         setChampionPoints((short)(getChampionPoints() + Math.max(1000, r) - this.rank));
/*      */       }
/* 1148 */       if (Servers.localServer.isChallengeServer())
/*      */       {
/* 1150 */         if (Math.max(1000, r) - this.rank != 0) {
/*      */           
/* 1152 */           ChallengeSummary.addToScore(this, ChallengePointEnum.ChallengePoint.BATTLEPOINTS.getEnumtype(), (
/* 1153 */               Math.max(1000, r) - this.rank));
/* 1154 */           ChallengeSummary.addToScore(this, ChallengePointEnum.ChallengePoint.OVERALL.getEnumtype(), (
/* 1155 */               Math.max(1000, r) - this.rank));
/*      */         } 
/*      */       }
/* 1158 */       this.rank = Math.max(1000, r);
/* 1159 */       if (this.rank > this.maxRank)
/* 1160 */         this.maxRank = this.rank; 
/* 1161 */       Connection dbcon = null;
/* 1162 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1165 */         dbcon = DbConnector.getPlayerDbCon();
/* 1166 */         if (exists(dbcon))
/*      */         {
/* 1168 */           this.lastModifiedRank = System.currentTimeMillis();
/* 1169 */           ps = dbcon.prepareStatement("update PLAYERS set RANK=?, MAXRANK=?, LASTMODIFIEDRANK=? WHERE NAME=?");
/* 1170 */           ps.setInt(1, this.rank);
/* 1171 */           ps.setInt(2, this.maxRank);
/* 1172 */           ps.setLong(3, this.lastModifiedRank);
/* 1173 */           ps.setString(4, this.name);
/* 1174 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1177 */       } catch (SQLException sqex) {
/*      */         
/* 1179 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1180 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1184 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1185 */         DbConnector.returnConnection(dbcon);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1190 */       this.lastModifiedRank = System.currentTimeMillis();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn() throws IOException {
/* 1196 */     this.lastWarned = System.currentTimeMillis();
/* 1197 */     this.warnings++;
/* 1198 */     Connection dbcon = null;
/* 1199 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1202 */       dbcon = DbConnector.getPlayerDbCon();
/* 1203 */       if (exists(dbcon))
/*      */       {
/* 1205 */         ps = dbcon.prepareStatement("update PLAYERS set WARNINGS=?, LASTWARNED=? WHERE NAME=?");
/* 1206 */         ps.setShort(1, (short)this.warnings);
/* 1207 */         ps.setLong(2, this.lastWarned);
/* 1208 */         ps.setString(3, this.name);
/* 1209 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1212 */     } catch (SQLException sqex) {
/*      */       
/* 1214 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1215 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1219 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1220 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetWarnings() throws IOException {
/* 1227 */     this.lastWarned = System.currentTimeMillis();
/* 1228 */     this.warnings = 0;
/* 1229 */     Connection dbcon = null;
/* 1230 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1233 */       dbcon = DbConnector.getPlayerDbCon();
/* 1234 */       if (exists(dbcon))
/*      */       {
/* 1236 */         ps = dbcon.prepareStatement("update PLAYERS set WARNINGS=?, LASTWARNED=? WHERE NAME=?");
/* 1237 */         ps.setShort(1, (short)0);
/* 1238 */         ps.setLong(2, 0L);
/* 1239 */         ps.setString(3, this.name);
/* 1240 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1243 */     } catch (SQLException sqex) {
/*      */       
/* 1245 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1246 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1250 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1251 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFaith(float aNewFaith) throws IOException {
/* 1258 */     float lFaith = Math.max(aNewFaith, 0.0F);
/* 1259 */     lFaith = Math.min(100.0F, lFaith);
/* 1260 */     if (!this.isPriest) {
/* 1261 */       lFaith = Math.min(30.0F, lFaith);
/*      */     } else {
/*      */       
/* 1264 */       lFaith = Math.max(lFaith, 1.0F);
/* 1265 */       if (lFaith < 30.0F) {
/*      */         
/* 1267 */         lFaith = Math.min(lFaith, 20.0F);
/* 1268 */         setPriest(false);
/*      */         
/*      */         try {
/* 1271 */           Players.getInstance().getPlayer(this.wurmId).getCommunicator()
/* 1272 */             .sendAlertServerMessage(this.deity.name + " no longer accepts you as a priest!", (byte)2);
/* 1273 */           Players.getInstance().getPlayer(this.wurmId).clearLinks();
/*      */         }
/* 1275 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1281 */     if (this.faith != lFaith) {
/*      */       
/* 1283 */       this.faith = lFaith;
/* 1284 */       Connection dbcon = null;
/* 1285 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1288 */         dbcon = DbConnector.getPlayerDbCon();
/* 1289 */         if (exists(dbcon))
/*      */         {
/* 1291 */           ps = dbcon.prepareStatement("update PLAYERS set FAITH=? WHERE NAME=?");
/* 1292 */           ps.setFloat(1, this.faith);
/* 1293 */           ps.setString(2, this.name);
/* 1294 */           ps.executeUpdate();
/* 1295 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1296 */           sendReligionStatus(2147483645, this.faith);
/*      */         }
/*      */       
/* 1299 */       } catch (SQLException sqex) {
/*      */         
/* 1301 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1302 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1306 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1307 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFavor(float fav) throws IOException {
/* 1315 */     float lFav = Math.max(fav, 0.0F);
/* 1316 */     lFav = Math.min(100.0F, lFav);
/* 1317 */     lFav = Math.min(this.faith, lFav);
/* 1318 */     if (!isPaying())
/* 1319 */       lFav = Math.min(20.0F, lFav); 
/* 1320 */     if (this.favor != lFav) {
/*      */       
/* 1322 */       if ((this.deity != null && this.deity.number == 2 && this.favor >= 35.0F && lFav < 35.0F) || (this.favor < 35.0F && lFav >= 35.0F)) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 1328 */           Players.getInstance().getPlayer(this.wurmId).recalcLimitingFactor((Item)null);
/*      */         }
/* 1330 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1335 */       this.favor = lFav;
/*      */       
/* 1337 */       Connection dbcon = null;
/* 1338 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1341 */         dbcon = DbConnector.getPlayerDbCon();
/* 1342 */         if (exists(dbcon))
/*      */         {
/* 1344 */           ps = dbcon.prepareStatement("update PLAYERS set FAVOR=? WHERE NAME=?");
/* 1345 */           ps.setFloat(1, this.favor);
/* 1346 */           ps.setString(2, this.name);
/* 1347 */           ps.executeUpdate();
/* 1348 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1349 */           sendReligionStatus(2147483644, this.favor);
/*      */         }
/*      */       
/* 1352 */       } catch (SQLException sqex) {
/*      */         
/* 1354 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1355 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1359 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1360 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeity(@Nullable Deity d) throws IOException {
/* 1369 */     if (this.deity != d) {
/*      */       
/* 1371 */       if (this.isPriest) {
/*      */         
/* 1373 */         setPriest(false);
/*      */         
/*      */         try {
/* 1376 */           Players.getInstance().getPlayer(this.wurmId).clearLinks();
/*      */         }
/* 1378 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1383 */       this.deity = d;
/* 1384 */       Connection dbcon = null;
/* 1385 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1388 */         byte num = 0;
/* 1389 */         if (this.deity != null) {
/* 1390 */           num = (byte)this.deity.number;
/*      */         }
/* 1392 */         dbcon = DbConnector.getPlayerDbCon();
/* 1393 */         if (exists(dbcon)) {
/*      */           
/* 1395 */           ps = dbcon.prepareStatement("update PLAYERS set DEITY=? WHERE NAME=?");
/* 1396 */           ps.setByte(1, num);
/* 1397 */           ps.setString(2, this.name);
/* 1398 */           ps.executeUpdate();
/* 1399 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } 
/* 1401 */         if (num == 0) {
/*      */           
/* 1403 */           setFaith(0.0F);
/* 1404 */           setFavor(0.0F);
/*      */         } 
/* 1406 */         sendAttitudeChange();
/*      */       }
/* 1408 */       catch (SQLException sqex) {
/*      */         
/* 1410 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1411 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1415 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1416 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transferDeity(@Nullable Deity d) throws IOException {
/* 1425 */     if (this.deity != d && this.deity != null) {
/*      */       
/* 1427 */       if (d.isHateGod() != this.deity.isHateGod()) {
/*      */         
/* 1429 */         this.alignment *= -1.0F;
/* 1430 */         Connection connection = null;
/* 1431 */         PreparedStatement preparedStatement = null;
/*      */         
/*      */         try {
/* 1434 */           connection = DbConnector.getPlayerDbCon();
/* 1435 */           if (exists(connection))
/*      */           {
/* 1437 */             preparedStatement = connection.prepareStatement("update PLAYERS set ALIGNMENT=? WHERE NAME=?");
/* 1438 */             preparedStatement.setFloat(1, this.alignment);
/* 1439 */             preparedStatement.setString(2, this.name);
/* 1440 */             preparedStatement.executeUpdate();
/* 1441 */             DbUtilities.closeDatabaseObjects(preparedStatement, null);
/* 1442 */             sendReligionStatus(2147483642, this.alignment);
/*      */           }
/*      */         
/* 1445 */         } catch (SQLException sqex) {
/*      */           
/* 1447 */           logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1448 */           throw new IOException(sqex);
/*      */         }
/*      */         finally {
/*      */           
/* 1452 */           DbUtilities.closeDatabaseObjects(preparedStatement, null);
/* 1453 */           DbConnector.returnConnection(connection);
/*      */         } 
/*      */       } 
/* 1456 */       this.deity = d;
/* 1457 */       Connection dbcon = null;
/* 1458 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1461 */         byte num = (byte)this.deity.number;
/*      */         
/* 1463 */         dbcon = DbConnector.getPlayerDbCon();
/* 1464 */         if (exists(dbcon)) {
/*      */           
/* 1466 */           ps = dbcon.prepareStatement("update PLAYERS set DEITY=? WHERE NAME=?");
/* 1467 */           ps.setByte(1, num);
/* 1468 */           ps.setString(2, this.name);
/* 1469 */           ps.executeUpdate();
/* 1470 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } 
/* 1472 */         sendAttitudeChange();
/*      */       }
/* 1474 */       catch (SQLException sqex) {
/*      */         
/* 1476 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1477 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1481 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1482 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlignment(float align) throws IOException {
/* 1490 */     float lAlign = Math.max(-100.0F, align);
/* 1491 */     if (this.deity != null && this.deity.isHateGod()) {
/* 1492 */       lAlign = Math.min(-1.0F, lAlign);
/*      */     } else {
/* 1494 */       lAlign = Math.min(100.0F, lAlign);
/* 1495 */     }  if (this.alignment != lAlign) {
/*      */       
/* 1497 */       this.alignment = lAlign;
/* 1498 */       Connection dbcon = null;
/* 1499 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1502 */         dbcon = DbConnector.getPlayerDbCon();
/* 1503 */         if (exists(dbcon))
/*      */         {
/* 1505 */           ps = dbcon.prepareStatement("update PLAYERS set ALIGNMENT=? WHERE NAME=?");
/* 1506 */           ps.setFloat(1, this.alignment);
/* 1507 */           ps.setString(2, this.name);
/* 1508 */           ps.executeUpdate();
/* 1509 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1510 */           sendReligionStatus(2147483642, this.alignment);
/*      */         }
/*      */       
/* 1513 */       } catch (SQLException sqex) {
/*      */         
/* 1515 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1516 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1520 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1521 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1523 */       if (this.deity != null && !this.deity.isHateGod())
/*      */       {
/* 1525 */         if (this.realdeath == 0)
/*      */         {
/* 1527 */           if (!this.deity.accepts(lAlign)) {
/*      */ 
/*      */             
/*      */             try {
/* 1531 */               Players.getInstance().getPlayer(this.wurmId).getCommunicator()
/* 1532 */                 .sendNormalServerMessage(this.deity.name + " no longer accepts you as a follower.");
/*      */             }
/* 1534 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */             
/* 1538 */             setDeity((Deity)null);
/* 1539 */             setChangedDeity();
/*      */           } 
/*      */         }
/*      */       }
/*      */       
/* 1544 */       if ((this.alignment == -100.0F && this.deity != null && this.deity.isHateGod()) || (this.alignment == 100.0F && this.deity != null && 
/* 1545 */         !this.deity.isHateGod())) {
/*      */         
/*      */         try {
/*      */           
/* 1549 */           Players.getInstance().getPlayer(this.wurmId).maybeTriggerAchievement(626, true);
/* 1550 */         } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGod(Deity g) throws IOException {
/* 1561 */     if (this.god != g)
/*      */     {
/* 1563 */       if (this.realdeath == 0) {
/*      */         
/* 1565 */         this.god = g;
/* 1566 */         Connection dbcon = null;
/* 1567 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/* 1570 */           byte num = 0;
/* 1571 */           if (this.god != null)
/* 1572 */             num = (byte)this.god.number; 
/* 1573 */           dbcon = DbConnector.getPlayerDbCon();
/* 1574 */           if (exists(dbcon))
/*      */           {
/* 1576 */             ps = dbcon.prepareStatement("update PLAYERS set GOD=? WHERE NAME=?");
/* 1577 */             ps.setByte(1, num);
/* 1578 */             ps.setString(2, this.name);
/* 1579 */             ps.executeUpdate();
/*      */           }
/*      */         
/* 1582 */         } catch (SQLException sqex) {
/*      */           
/* 1584 */           logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1585 */           throw new IOException(sqex);
/*      */         }
/*      */         finally {
/*      */           
/* 1589 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1590 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModelName(String newModelName) {
/* 1599 */     if (!this.modelName.equals(newModelName)) {
/*      */       
/* 1601 */       this.modelName = newModelName;
/* 1602 */       Connection dbcon = null;
/* 1603 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1606 */         dbcon = DbConnector.getPlayerDbCon();
/* 1607 */         ps = dbcon.prepareStatement("update PLAYERS set MODELNAME=? WHERE NAME=?");
/* 1608 */         ps.setString(1, this.modelName);
/* 1609 */         ps.setString(2, this.name);
/* 1610 */         ps.executeUpdate();
/*      */       }
/* 1612 */       catch (SQLException sqex) {
/*      */         
/* 1614 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage() + " fail to set modelname to " + this.modelName, sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1618 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1619 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChangedJoat() {
/* 1634 */     Connection dbcon = null;
/* 1635 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1638 */       this.lastChangedJoat = System.currentTimeMillis();
/* 1639 */       dbcon = DbConnector.getPlayerDbCon();
/* 1640 */       if (exists(dbcon))
/*      */       {
/* 1642 */         ps = dbcon.prepareStatement("update PLAYERS set LASTJOAT=? WHERE NAME=?");
/* 1643 */         ps.setLong(1, this.lastChangedJoat);
/* 1644 */         ps.setString(2, this.name);
/* 1645 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1648 */     } catch (SQLException sqex) {
/*      */       
/* 1650 */       logger.log(Level.WARNING, this.name + " touch joat " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1654 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1655 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChangedDeity() throws IOException {
/* 1662 */     Connection dbcon = null;
/* 1663 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1666 */       this.lastChangedDeity = System.currentTimeMillis();
/* 1667 */       dbcon = DbConnector.getPlayerDbCon();
/* 1668 */       if (exists(dbcon))
/*      */       {
/* 1670 */         ps = dbcon.prepareStatement("update PLAYERS set LASTCHANGEDDEITY=? WHERE NAME=?");
/* 1671 */         ps.setLong(1, this.lastChangedDeity);
/* 1672 */         ps.setString(2, this.name);
/* 1673 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1676 */     } catch (SQLException sqex) {
/*      */       
/* 1678 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 1679 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1683 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1684 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFatigueSecs(int fatigueSecs, long lastReceived) {
/* 1691 */     Connection dbcon = null;
/* 1692 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1695 */       dbcon = DbConnector.getPlayerDbCon();
/* 1696 */       if (exists(dbcon))
/*      */       {
/* 1698 */         ps = dbcon.prepareStatement("update PLAYERS set FATIGUE=?,FATIGUETODAY=?, LASTFATIGUE=? WHERE NAME=?");
/* 1699 */         ps.setInt(1, fatigueSecs);
/* 1700 */         ps.setInt(2, this.fatigueSecsToday);
/* 1701 */         ps.setLong(3, lastReceived);
/* 1702 */         ps.setString(4, this.name);
/* 1703 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 1706 */     } catch (SQLException sqex) {
/*      */       
/* 1708 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1712 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1713 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveSwitchFatigue() {
/* 1720 */     if (this.fatigueSecsYesterday > 0 || this.fatigueSecsToday > 0) {
/*      */       
/* 1722 */       this.fatigueSecsYesterday = this.fatigueSecsToday;
/* 1723 */       this.fatigueSecsToday = 0;
/* 1724 */       Connection dbcon = null;
/* 1725 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1728 */         dbcon = DbConnector.getPlayerDbCon();
/* 1729 */         if (exists(dbcon))
/*      */         {
/* 1731 */           ps = dbcon.prepareStatement("update PLAYERS set FATIGUETODAY=?, FATIGUEYDAY=? WHERE NAME=?");
/* 1732 */           ps.setInt(1, this.fatigueSecsToday);
/* 1733 */           ps.setInt(2, this.fatigueSecsYesterday);
/* 1734 */           ps.setString(3, this.name);
/* 1735 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1738 */       } catch (SQLException sqex) {
/*      */         
/* 1740 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1744 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1745 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDead(boolean isdead) {
/* 1753 */     if (this.dead != isdead) {
/*      */       
/* 1755 */       this.dead = isdead;
/* 1756 */       Connection dbcon = null;
/* 1757 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1760 */         dbcon = DbConnector.getPlayerDbCon();
/* 1761 */         if (exists(dbcon))
/*      */         {
/* 1763 */           ps = dbcon.prepareStatement("update PLAYERS set DEAD=? WHERE NAME=?");
/* 1764 */           ps.setBoolean(1, this.dead);
/* 1765 */           ps.setString(2, this.name);
/* 1766 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1769 */       } catch (SQLException sqex) {
/*      */         
/* 1771 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1775 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1776 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMuted(boolean mute, String reason, long expiry) {
/* 1784 */     if (this.muted != mute) {
/*      */       
/* 1786 */       Connection dbcon = null;
/* 1787 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1790 */         this.muted = mute;
/* 1791 */         this.mutereason = reason;
/* 1792 */         this.muteexpiry = expiry;
/* 1793 */         dbcon = DbConnector.getPlayerDbCon();
/* 1794 */         if (exists(dbcon))
/*      */         {
/* 1796 */           ps = dbcon.prepareStatement("update PLAYERS set MUTED=?,MUTEREASON=?,MUTEEXPIRY=? WHERE NAME=?");
/* 1797 */           ps.setBoolean(1, this.muted);
/* 1798 */           ps.setString(2, this.mutereason);
/* 1799 */           ps.setLong(3, this.muteexpiry);
/* 1800 */           ps.setString(4, this.name);
/* 1801 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1804 */       } catch (SQLException sqex) {
/*      */         
/* 1806 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1810 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1811 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 1813 */       if (this.muted)
/*      */       {
/* 1815 */         setMuteTimes((short)(this.muteTimes + 1));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayMute(boolean mmute) {
/* 1823 */     if (this.mayMute != mmute) {
/*      */       
/* 1825 */       this.mayMute = mmute;
/* 1826 */       if (this.mayMute) {
/* 1827 */         addTitle(Titles.Title.CM);
/*      */       } else {
/* 1829 */         removeTitle(Titles.Title.CM);
/*      */       } 
/* 1831 */       Connection dbcon = null;
/* 1832 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1835 */         dbcon = DbConnector.getPlayerDbCon();
/* 1836 */         if (exists(dbcon))
/*      */         {
/* 1838 */           ps = dbcon.prepareStatement("update PLAYERS set MAYMUTE=? WHERE NAME=?");
/* 1839 */           ps.setBoolean(1, this.mayMute);
/* 1840 */           ps.setString(2, this.name);
/* 1841 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1844 */       } catch (SQLException sqex) {
/*      */         
/* 1846 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1850 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1851 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReferedby(long referer) {
/* 1859 */     if (this.referrer != referer) {
/*      */       
/* 1861 */       Connection dbcon = null;
/* 1862 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1865 */         this.referrer = referer;
/* 1866 */         dbcon = DbConnector.getPlayerDbCon();
/* 1867 */         if (exists(dbcon))
/*      */         {
/* 1869 */           ps = dbcon.prepareStatement("update PLAYERS set REFERRER=? WHERE NAME=?");
/* 1870 */           ps.setLong(1, this.referrer);
/* 1871 */           ps.setString(2, this.name);
/* 1872 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1875 */       } catch (SQLException sqex) {
/*      */         
/* 1877 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1881 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1882 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newname) throws IOException {
/* 1890 */     if (!this.name.equals(newname)) {
/*      */       
/* 1892 */       Connection dbcon = null;
/* 1893 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1896 */         dbcon = DbConnector.getPlayerDbCon();
/* 1897 */         if (exists(dbcon)) {
/*      */           
/* 1899 */           ps = dbcon.prepareStatement("update PLAYERS set NAME=? WHERE NAME=?");
/* 1900 */           ps.setString(1, newname);
/* 1901 */           ps.setString(2, this.name);
/* 1902 */           ps.executeUpdate();
/*      */         } 
/* 1904 */         this.name = newname;
/*      */       }
/* 1906 */       catch (SQLException sqex) {
/*      */         
/* 1908 */         logger.log(Level.WARNING, this.name + " changing to " + newname + " " + sqex.getMessage(), sqex);
/* 1909 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1913 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1914 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVersion(long newversion) {
/* 1922 */     if (this.version != newversion) {
/*      */       
/* 1924 */       this.version = newversion;
/* 1925 */       Connection dbcon = null;
/* 1926 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1929 */         dbcon = DbConnector.getPlayerDbCon();
/* 1930 */         if (exists(dbcon))
/*      */         {
/* 1932 */           ps = dbcon.prepareStatement("update PLAYERS set VERSION=? WHERE NAME=?");
/* 1933 */           ps.setLong(1, this.version);
/* 1934 */           ps.setString(2, this.name);
/* 1935 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1938 */       } catch (SQLException sqex) {
/*      */         
/* 1940 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1944 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1945 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastTrigger(int newTrigger) {
/* 1953 */     if (this.lastTriggerEffect != newTrigger) {
/*      */       
/* 1955 */       this.lastTriggerEffect = newTrigger;
/* 1956 */       Connection dbcon = null;
/* 1957 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 1960 */         dbcon = DbConnector.getPlayerDbCon();
/* 1961 */         if (exists(dbcon))
/*      */         {
/* 1963 */           ps = dbcon.prepareStatement("update PLAYERS set LASTTRIGGER=? WHERE NAME=?");
/* 1964 */           ps.setInt(1, this.lastTriggerEffect);
/* 1965 */           ps.setString(2, this.name);
/* 1966 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 1969 */       } catch (SQLException sqex) {
/*      */         
/* 1971 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 1975 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1976 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSessionKey(String key, long expiration) throws IOException {
/* 1984 */     this.sessionKey = key;
/* 1985 */     this.sessionExpiration = expiration;
/* 1986 */     Connection dbcon = null;
/* 1987 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1990 */       dbcon = DbConnector.getPlayerDbCon();
/* 1991 */       if (exists(dbcon))
/*      */       {
/* 1993 */         ps = dbcon.prepareStatement("update PLAYERS set SESSIONKEY=?, SESSIONEXPIRE=? WHERE NAME=?");
/* 1994 */         ps.setString(1, this.sessionKey);
/* 1995 */         ps.setLong(2, this.sessionExpiration);
/* 1996 */         ps.setString(3, this.name);
/* 1997 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 2000 */     } catch (SQLException sqex) {
/*      */       
/* 2002 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 2003 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 2007 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2008 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRealDeath(byte rdcounter) throws IOException {
/* 2015 */     if (this.realdeath != rdcounter) {
/*      */       
/* 2017 */       this.realdeath = rdcounter;
/* 2018 */       Connection dbcon = null;
/* 2019 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2022 */         dbcon = DbConnector.getPlayerDbCon();
/* 2023 */         if (exists(dbcon))
/*      */         {
/* 2025 */           ps = dbcon.prepareStatement("update PLAYERS set REALDEATH=? WHERE NAME=?");
/* 2026 */           ps.setByte(1, this.realdeath);
/* 2027 */           ps.setString(2, this.name);
/* 2028 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 2031 */       } catch (SQLException sqex) {
/*      */         
/* 2033 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 2034 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 2038 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2039 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClimbing(boolean climb) throws IOException {
/* 2047 */     if (this.climbing != climb) {
/*      */       
/* 2049 */       this.climbing = climb;
/* 2050 */       Connection dbcon = null;
/* 2051 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2054 */         dbcon = DbConnector.getPlayerDbCon();
/* 2055 */         if (exists(dbcon))
/*      */         {
/* 2057 */           ps = dbcon.prepareStatement("update PLAYERS set CLIMBING=? WHERE NAME=?");
/* 2058 */           ps.setBoolean(1, this.climbing);
/* 2059 */           ps.setString(2, this.name);
/* 2060 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 2063 */       } catch (SQLException sqex) {
/*      */         
/* 2065 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 2066 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 2070 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2071 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMoney(long newmoney) throws IOException {
/* 2079 */     if (newmoney != this.money) {
/*      */       
/* 2081 */       this.money = newmoney;
/* 2082 */       if (this.money < 0L)
/* 2083 */         this.money = 0L; 
/* 2084 */       Connection dbcon = null;
/* 2085 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2088 */         dbcon = DbConnector.getPlayerDbCon();
/* 2089 */         if (exists(dbcon))
/*      */         {
/* 2091 */           ps = dbcon.prepareStatement("update PLAYERS set MONEY=? WHERE NAME=?");
/* 2092 */           ps.setLong(1, this.money);
/* 2093 */           ps.setString(2, this.name);
/* 2094 */           ps.executeUpdate();
/*      */         }
/*      */       
/* 2097 */       } catch (SQLException sqex) {
/*      */         
/* 2099 */         logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 2100 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/* 2104 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2105 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updatePassword(String newpassword) throws IOException {
/* 2113 */     Connection dbcon = null;
/* 2114 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2117 */       String encp = LoginHandler.hashPassword(newpassword, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(this.name)));
/* 2118 */       this.password = encp;
/* 2119 */       dbcon = DbConnector.getPlayerDbCon();
/* 2120 */       if (exists(dbcon))
/*      */       {
/* 2122 */         ps = dbcon.prepareStatement("update PLAYERS set PASSWORD=? WHERE NAME=?");
/* 2123 */         ps.setString(1, encp);
/* 2124 */         ps.setString(2, this.name);
/* 2125 */         ps.executeUpdate();
/*      */       }
/*      */     
/* 2128 */     } catch (SQLException sqex) {
/*      */       
/* 2130 */       logger.log(Level.WARNING, this.name + " " + sqex.getMessage(), sqex);
/* 2131 */       throw new IOException(sqex);
/*      */     }
/* 2133 */     catch (Exception ex) {
/*      */       
/* 2135 */       logger.log(Level.INFO, "Failed to encrypt password for player " + this.name, ex);
/* 2136 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2140 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2141 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFriend(long wurmid, long friend, byte catId, String note) throws IOException {
/* 2148 */     Connection dbcon = null;
/* 2149 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2152 */       dbcon = DbConnector.getPlayerDbCon();
/* 2153 */       ps = dbcon.prepareStatement("UPDATE FRIENDS SET CATEGORY=?,NOTE=? WHERE WURMID=? AND FRIEND=?");
/* 2154 */       ps.setByte(1, catId);
/* 2155 */       ps.setString(2, note);
/* 2156 */       ps.setLong(3, wurmid);
/* 2157 */       ps.setLong(4, friend);
/* 2158 */       ps.executeUpdate();
/*      */     }
/* 2160 */     catch (SQLException ex) {
/*      */       
/* 2162 */       logger.log(Level.INFO, "Failed to update friend for  " + wurmid, ex);
/* 2163 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2167 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2168 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveFriend(long wurmid, long friend, byte catId, String note) throws IOException {
/* 2175 */     Connection dbcon = null;
/* 2176 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2179 */       dbcon = DbConnector.getPlayerDbCon();
/* 2180 */       ps = dbcon.prepareStatement(ADD_FRIEND);
/* 2181 */       ps.setLong(1, wurmid);
/* 2182 */       ps.setLong(2, friend);
/* 2183 */       ps.setByte(3, catId);
/* 2184 */       ps.setString(4, note);
/* 2185 */       ps.executeUpdate();
/*      */     }
/* 2187 */     catch (SQLException ex) {
/*      */       
/* 2189 */       logger.log(Level.INFO, "Failed to add friend for  " + wurmid, ex);
/* 2190 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2194 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2195 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteFriend(long wurmid, long friend) throws IOException {
/* 2202 */     Connection dbcon = null;
/* 2203 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2206 */       dbcon = DbConnector.getPlayerDbCon();
/* 2207 */       ps = dbcon.prepareStatement("DELETE FROM FRIENDS WHERE WURMID=? AND FRIEND=?");
/* 2208 */       ps.setLong(1, wurmid);
/* 2209 */       ps.setLong(2, friend);
/* 2210 */       ps.executeUpdate();
/*      */     }
/* 2212 */     catch (SQLException ex) {
/*      */       
/* 2214 */       logger.log(Level.INFO, "Failed to remove friend for  " + wurmid, ex);
/* 2215 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2219 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2220 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveEnemy(long wurmid, long enemy) throws IOException {
/* 2227 */     Connection dbcon = null;
/* 2228 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2231 */       dbcon = DbConnector.getPlayerDbCon();
/*      */       
/* 2233 */       ps = dbcon.prepareStatement(ADD_ENEMY);
/* 2234 */       ps.setLong(1, wurmid);
/* 2235 */       ps.setLong(2, enemy);
/* 2236 */       ps.executeUpdate();
/*      */     }
/* 2238 */     catch (SQLException ex) {
/*      */       
/* 2240 */       logger.log(Level.INFO, "Failed to add enemy for  " + wurmid, ex);
/* 2241 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2245 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2246 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteEnemy(long wurmid, long enemy) throws IOException {
/* 2253 */     Connection dbcon = null;
/* 2254 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2257 */       dbcon = DbConnector.getPlayerDbCon();
/* 2258 */       ps = dbcon.prepareStatement("DELETE FROM ENEMIES WHERE WURMID=? AND ENEMY=?");
/* 2259 */       ps.setLong(1, wurmid);
/* 2260 */       ps.setLong(2, enemy);
/* 2261 */       ps.executeUpdate();
/*      */     }
/* 2263 */     catch (SQLException ex) {
/*      */       
/* 2265 */       logger.log(Level.INFO, "Failed to remove enemy for  " + wurmid, ex);
/* 2266 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2270 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2271 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadFriends(long wurmid) {
/* 2278 */     Connection dbcon = null;
/* 2279 */     PreparedStatement ps = null;
/* 2280 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2283 */       dbcon = DbConnector.getPlayerDbCon();
/* 2284 */       ps = dbcon.prepareStatement("SELECT FRIEND,CATEGORY,NOTE FROM FRIENDS WHERE WURMID=?");
/* 2285 */       ps.setLong(1, wurmid);
/* 2286 */       rs = ps.executeQuery();
/* 2287 */       while (rs.next())
/*      */       {
/* 2289 */         addFriend(rs.getLong("FRIEND"), rs.getByte("CATEGORY"), rs.getString("NOTE"), true);
/*      */       }
/*      */     }
/* 2292 */     catch (SQLException ex) {
/*      */       
/* 2294 */       logger.log(Level.INFO, "Failed to load friends for  " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2298 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2299 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 2301 */     setLoadedFriends(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveIgnored(long wurmid, long ignored) throws IOException {
/* 2334 */     Connection dbcon = null;
/* 2335 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2338 */       dbcon = DbConnector.getPlayerDbCon();
/* 2339 */       ps = dbcon.prepareStatement(ADD_IGNORED);
/* 2340 */       ps.setLong(1, wurmid);
/* 2341 */       ps.setLong(2, ignored);
/* 2342 */       ps.executeUpdate();
/*      */     }
/* 2344 */     catch (SQLException ex) {
/*      */       
/* 2346 */       logger.log(Level.INFO, "Failed to add ignored for  " + wurmid, ex);
/* 2347 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2351 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2352 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteIgnored(long wurmid, long ignored) throws IOException {
/* 2359 */     Connection dbcon = null;
/* 2360 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2363 */       dbcon = DbConnector.getPlayerDbCon();
/* 2364 */       ps = dbcon.prepareStatement("DELETE FROM IGNORED WHERE WURMID=? AND IGNOREE=?");
/* 2365 */       ps.setLong(1, wurmid);
/* 2366 */       ps.setLong(2, ignored);
/* 2367 */       ps.executeUpdate();
/*      */     }
/* 2369 */     catch (SQLException ex) {
/*      */       
/* 2371 */       logger.log(Level.INFO, "Failed to remove ignored for  " + wurmid, ex);
/* 2372 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2376 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2377 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadIgnored(long wurmid) {
/* 2384 */     Connection dbcon = null;
/* 2385 */     PreparedStatement ps = null;
/* 2386 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2389 */       dbcon = DbConnector.getPlayerDbCon();
/* 2390 */       ps = dbcon.prepareStatement("SELECT IGNOREE FROM IGNORED WHERE WURMID=?");
/* 2391 */       ps.setLong(1, wurmid);
/* 2392 */       rs = ps.executeQuery();
/* 2393 */       while (rs.next())
/*      */       {
/* 2395 */         addIgnored(rs.getLong("IGNOREE"), true);
/*      */       }
/*      */     }
/* 2398 */     catch (SQLException ex) {
/*      */       
/* 2400 */       logger.log(Level.INFO, "Failed to load ignored for  " + wurmid, ex);
/*      */     }
/* 2402 */     catch (IOException ex) {
/*      */       
/* 2404 */       logger.log(Level.INFO, "Failed to load ignored for  " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2408 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2409 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNumFaith(byte aNumFaith, long aLastFaith) throws IOException {
/* 2416 */     Connection dbcon = null;
/* 2417 */     PreparedStatement ps = null;
/* 2418 */     this.numFaith = aNumFaith;
/*      */     
/*      */     try {
/* 2421 */       dbcon = DbConnector.getPlayerDbCon();
/* 2422 */       ps = dbcon.prepareStatement("update PLAYERS set NUMFAITH=?, LASTFAITH=? WHERE NAME=?");
/* 2423 */       ps.setByte(1, aNumFaith);
/* 2424 */       ps.setLong(2, aLastFaith);
/* 2425 */       ps.setString(3, this.name);
/* 2426 */       ps.executeUpdate();
/*      */     }
/* 2428 */     catch (SQLException ex) {
/*      */       
/* 2430 */       logger.log(Level.INFO, "Failed to set lastfaith for " + this.name, ex);
/* 2431 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2435 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2436 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSex(byte newsex) throws IOException {
/* 2443 */     Connection dbcon = null;
/* 2444 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 2447 */       dbcon = DbConnector.getPlayerDbCon();
/* 2448 */       ps = dbcon.prepareStatement("update PLAYERS set SEX=? WHERE NAME=?");
/* 2449 */       ps.setByte(1, newsex);
/* 2450 */       ps.setString(2, this.name);
/* 2451 */       ps.executeUpdate();
/*      */     }
/* 2453 */     catch (SQLException ex) {
/*      */       
/* 2455 */       logger.log(Level.INFO, "Failed to set sex for " + this.name, ex);
/* 2456 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2460 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 2461 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPet(long newpet) {
/* 2468 */     if (newpet != this.pet) {
/*      */       
/* 2470 */       Connection dbcon = null;
/* 2471 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2474 */         this.pet = newpet;
/* 2475 */         dbcon = DbConnector.getPlayerDbCon();
/* 2476 */         ps = dbcon.prepareStatement("update PLAYERS set PET=? WHERE NAME=?");
/* 2477 */         ps.setLong(1, this.pet);
/* 2478 */         ps.setString(2, this.name);
/* 2479 */         ps.executeUpdate();
/*      */       }
/* 2481 */       catch (SQLException ex) {
/*      */         
/* 2483 */         logger.log(Level.WARNING, "Failed to set pet for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2487 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2488 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNicotine(float newnicotine) {
/* 2496 */     if (newnicotine != this.nicotine) {
/*      */       
/* 2498 */       Connection dbcon = null;
/* 2499 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2502 */         this.nicotine = newnicotine;
/* 2503 */         dbcon = DbConnector.getPlayerDbCon();
/* 2504 */         ps = dbcon.prepareStatement("update PLAYERS set NICOTINE=? WHERE NAME=?");
/* 2505 */         ps.setFloat(1, this.nicotine);
/* 2506 */         ps.setString(2, this.name);
/* 2507 */         ps.executeUpdate();
/*      */       }
/* 2509 */       catch (SQLException ex) {
/*      */         
/* 2511 */         logger.log(Level.WARNING, "Failed to set nicotine for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2515 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2516 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlcohol(float newalcohol) {
/* 2524 */     float lNewalcohol = Math.max(0.0F, newalcohol);
/* 2525 */     lNewalcohol = Math.min(100.0F, lNewalcohol);
/* 2526 */     if (lNewalcohol != this.alcohol) {
/*      */       
/* 2528 */       Connection dbcon = null;
/* 2529 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2532 */         this.alcohol = lNewalcohol;
/*      */         
/* 2534 */         dbcon = DbConnector.getPlayerDbCon();
/* 2535 */         ps = dbcon.prepareStatement("update PLAYERS set ALCOHOL=? WHERE NAME=?");
/* 2536 */         ps.setFloat(1, this.alcohol);
/* 2537 */         ps.setString(2, this.name);
/* 2538 */         ps.executeUpdate();
/*      */       }
/* 2540 */       catch (SQLException ex) {
/*      */         
/* 2542 */         logger.log(Level.WARNING, "Failed to set alcohol for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2546 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2547 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setAlcoholTime(long newalcohol) {
/* 2555 */     boolean titleAdded = false;
/* 2556 */     long lNewalcohol = Math.max(0L, Math.min(10000L, newalcohol));
/* 2557 */     if (lNewalcohol != this.alcoholAddiction) {
/*      */       
/* 2559 */       Connection dbcon = null;
/* 2560 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2563 */         this.alcoholAddiction = lNewalcohol;
/*      */ 
/*      */         
/* 2566 */         if (this.alcoholAddiction >= 10000L)
/* 2567 */           titleAdded = addTitle(Titles.Title.Alcoholic); 
/* 2568 */         dbcon = DbConnector.getPlayerDbCon();
/* 2569 */         ps = dbcon.prepareStatement("update PLAYERS set ALCOHOLTIME=? WHERE NAME=?");
/* 2570 */         ps.setLong(1, this.alcoholAddiction);
/* 2571 */         ps.setString(2, this.name);
/* 2572 */         ps.executeUpdate();
/*      */       }
/* 2574 */       catch (SQLException ex) {
/*      */         
/* 2576 */         logger.log(Level.WARNING, "Failed to set alcoholTime for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2580 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2581 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 2584 */     return titleAdded;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNicotineTime(long newnicotine) {
/* 2590 */     if (newnicotine != this.nicotineAddiction) {
/*      */       
/* 2592 */       Connection dbcon = null;
/* 2593 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2596 */         this.nicotineAddiction = newnicotine;
/* 2597 */         dbcon = DbConnector.getPlayerDbCon();
/* 2598 */         ps = dbcon.prepareStatement("update PLAYERS set NICOTINETIME=? WHERE NAME=?");
/* 2599 */         ps.setLong(1, this.nicotineAddiction);
/* 2600 */         ps.setString(2, this.name);
/* 2601 */         ps.executeUpdate();
/*      */       }
/* 2603 */       catch (SQLException ex) {
/*      */         
/* 2605 */         logger.log(Level.WARNING, "Failed to set nicotineTime for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2609 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2610 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChangedKingdom(byte changed, boolean setTimeStamp) throws IOException {
/* 2618 */     if (this.changedKingdom != changed) {
/*      */       
/* 2620 */       Connection dbcon = null;
/* 2621 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2624 */         this.changedKingdom = changed;
/* 2625 */         dbcon = DbConnector.getPlayerDbCon();
/* 2626 */         ps = dbcon.prepareStatement("update PLAYERS set NUMSCHANGEDKINGDOM=? WHERE NAME=?");
/* 2627 */         ps.setByte(1, this.changedKingdom);
/* 2628 */         ps.setString(2, this.name);
/* 2629 */         ps.executeUpdate();
/*      */       }
/* 2631 */       catch (SQLException ex) {
/*      */         
/* 2633 */         logger.log(Level.INFO, "Failed to set changedKingdom for " + this.name, ex);
/* 2634 */         throw new IOException(ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2638 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2639 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */ 
/*      */       
/* 2643 */       if (setTimeStamp) {
/* 2644 */         setChangedKingdom();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFace(long _face) throws IOException {
/* 2651 */     if (this.face != _face) {
/*      */       
/* 2653 */       Connection dbcon = null;
/* 2654 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2657 */         this.face = _face;
/* 2658 */         dbcon = DbConnector.getPlayerDbCon();
/* 2659 */         ps = dbcon.prepareStatement("update PLAYERS set FACE=? WHERE NAME=?");
/* 2660 */         ps.setLong(1, this.face);
/* 2661 */         ps.setString(2, this.name);
/* 2662 */         ps.executeUpdate();
/*      */       }
/* 2664 */       catch (SQLException ex) {
/*      */         
/* 2666 */         logger.log(Level.INFO, "Failed to set face for " + this.name, ex);
/* 2667 */         throw new IOException(ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2671 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2672 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReputation(int rep) {
/* 2680 */     int lNewReputation = Math.min(rep, 100);
/*      */ 
/*      */ 
/*      */     
/* 2684 */     lNewReputation = Math.max(lNewReputation, -300);
/* 2685 */     if (this.reputation != lNewReputation) {
/*      */       
/* 2687 */       Connection dbcon = null;
/* 2688 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2691 */         this.reputation = lNewReputation;
/* 2692 */         this.lastPolledReputation = System.currentTimeMillis();
/* 2693 */         dbcon = DbConnector.getPlayerDbCon();
/* 2694 */         ps = dbcon.prepareStatement("update PLAYERS set REPUTATION=?, LASTPOLLEDREP=? WHERE NAME=?");
/* 2695 */         ps.setInt(1, this.reputation);
/* 2696 */         ps.setLong(2, this.lastPolledReputation);
/* 2697 */         ps.setString(3, this.name);
/* 2698 */         ps.executeUpdate();
/*      */       }
/* 2700 */       catch (SQLException ex) {
/*      */         
/* 2702 */         logger.log(Level.WARNING, "Failed to set reputation for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2706 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2707 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addTitle(Titles.Title aTitle) {
/* 2715 */     if (!this.titles.contains(aTitle)) {
/*      */       
/* 2717 */       this.titles.add(aTitle);
/* 2718 */       Connection dbcon = null;
/* 2719 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2722 */         dbcon = DbConnector.getPlayerDbCon();
/* 2723 */         ps = dbcon.prepareStatement("INSERT INTO TITLES (WURMID, TITLEID, TITLENAME) VALUES(?,?,?)");
/* 2724 */         ps.setLong(1, this.wurmId);
/* 2725 */         ps.setInt(2, aTitle.id);
/*      */         
/* 2727 */         ps.setString(3, aTitle.getName(true));
/* 2728 */         ps.executeUpdate();
/* 2729 */         return true;
/*      */       }
/* 2731 */       catch (SQLException ex) {
/*      */         
/* 2733 */         logger.log(Level.WARNING, "Failed to add title for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2737 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2738 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 2741 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeTitle(Titles.Title aTitle) {
/* 2747 */     if (this.titles.contains(aTitle)) {
/*      */       
/* 2749 */       this.titles.remove(aTitle);
/* 2750 */       Connection dbcon = null;
/* 2751 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2754 */         dbcon = DbConnector.getPlayerDbCon();
/* 2755 */         ps = dbcon.prepareStatement("DELETE FROM TITLES WHERE WURMID=? AND TITLEID=?");
/* 2756 */         ps.setLong(1, this.wurmId);
/* 2757 */         ps.setInt(2, aTitle.id);
/* 2758 */         ps.executeUpdate();
/* 2759 */         return true;
/*      */       }
/* 2761 */       catch (SQLException ex) {
/*      */         
/* 2763 */         logger.log(Level.WARNING, "Failed to remove title for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2767 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2768 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 2771 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadHistorySteamIds(long wurmid) {
/* 2777 */     Connection db = null;
/* 2778 */     PreparedStatement ps = null;
/* 2779 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2782 */       db = DbConnector.getPlayerDbCon();
/* 2783 */       ps = db.prepareStatement("SELECT * FROM STEAM_IDS WHERE PLAYER_ID=?");
/* 2784 */       ps.setLong(1, wurmid);
/* 2785 */       rs = ps.executeQuery();
/* 2786 */       while (rs.next())
/*      */       {
/* 2788 */         SteamIdHistory history = new SteamIdHistory(rs);
/* 2789 */         this.historySteamId.put(history.getSteamId(), history);
/*      */       }
/*      */     
/* 2792 */     } catch (SQLException ex) {
/*      */       
/* 2794 */       logger.log(Level.INFO, "Failed to load Steam ID history for " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2798 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2799 */       DbConnector.returnConnection(db);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadHistoryIPs(long wurmid) {
/* 2806 */     Connection dbcon = null;
/* 2807 */     PreparedStatement ps = null;
/* 2808 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2811 */       dbcon = DbConnector.getPlayerDbCon();
/* 2812 */       ps = dbcon.prepareStatement("SELECT * FROM PLAYERHISTORYIPS WHERE PLAYERID=?");
/* 2813 */       ps.setLong(1, wurmid);
/* 2814 */       rs = ps.executeQuery();
/* 2815 */       while (rs.next())
/*      */       {
/* 2817 */         this.historyIPStart.put(rs.getString("IPADDRESS"), Long.valueOf(rs.getLong("FIRSTUSED")));
/* 2818 */         this.historyIPLast.put(rs.getString("IPADDRESS"), Long.valueOf(rs.getLong("LASTUSED")));
/*      */       }
/*      */     
/* 2821 */     } catch (SQLException ex) {
/*      */       
/* 2823 */       logger.log(Level.INFO, "Failed to load IP history for  " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2827 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2828 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadHistoryEmails(long wurmid) {
/* 2835 */     Connection dbcon = null;
/* 2836 */     PreparedStatement ps = null;
/* 2837 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2840 */       dbcon = DbConnector.getPlayerDbCon();
/* 2841 */       ps = dbcon.prepareStatement("SELECT * FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=?");
/* 2842 */       ps.setLong(1, wurmid);
/* 2843 */       rs = ps.executeQuery();
/* 2844 */       while (rs.next())
/*      */       {
/* 2846 */         this.historyEmail.put(rs.getString("EMAIL_ADDRESS"), Long.valueOf(rs.getLong("DATED")));
/*      */       }
/* 2848 */       if (this.historyEmail.isEmpty())
/*      */       {
/*      */         
/* 2851 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 2852 */         long now = System.currentTimeMillis();
/* 2853 */         ps = dbcon.prepareStatement("INSERT INTO PLAYEREHISTORYEMAIL(PLAYERID,EMAIL_ADDRESS,DATED) VALUES(?,?,?)");
/* 2854 */         ps.setLong(1, this.wurmId);
/* 2855 */         ps.setString(2, this.emailAddress);
/* 2856 */         ps.setLong(3, now);
/* 2857 */         ps.executeUpdate();
/* 2858 */         this.historyEmail.put(this.emailAddress, Long.valueOf(now));
/*      */       }
/*      */     
/* 2861 */     } catch (SQLException ex) {
/*      */       
/* 2863 */       logger.log(Level.INFO, "Failed to load email history for  " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2867 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2868 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadTitles(long wurmid) {
/* 2875 */     Connection dbcon = null;
/* 2876 */     PreparedStatement ps = null;
/* 2877 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 2880 */       dbcon = DbConnector.getPlayerDbCon();
/* 2881 */       ps = dbcon.prepareStatement("select TITLEID from TITLES where WURMID=?");
/* 2882 */       ps.setLong(1, wurmid);
/* 2883 */       rs = ps.executeQuery();
/* 2884 */       while (rs.next())
/*      */       {
/* 2886 */         this.titles.add(Titles.Title.getTitle(rs.getInt("TITLEID")));
/*      */       }
/*      */     }
/* 2889 */     catch (SQLException ex) {
/*      */       
/* 2891 */       logger.log(Level.INFO, "Failed to load titles for  " + wurmid, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 2895 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 2896 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 2898 */     if (this.playerAssistant) {
/* 2899 */       addTitle(Titles.Title.PA);
/*      */     } else {
/* 2901 */       removeTitle(Titles.Title.PA);
/* 2902 */     }  if (this.mayMute) {
/* 2903 */       addTitle(Titles.Title.CM);
/*      */     } else {
/* 2905 */       removeTitle(Titles.Title.CM);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEmailAddress(String email) {
/* 2911 */     if (!this.emailAddress.equals(email)) {
/*      */       
/* 2913 */       this.emailAddress = email;
/* 2914 */       Connection dbcon = null;
/* 2915 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2918 */         dbcon = DbConnector.getPlayerDbCon();
/* 2919 */         ps = dbcon.prepareStatement("update PLAYERS set EMAIL=? WHERE NAME=?");
/* 2920 */         ps.setString(1, this.emailAddress);
/* 2921 */         ps.setString(2, this.name);
/* 2922 */         ps.executeUpdate();
/*      */         
/* 2924 */         if (!this.historyEmail.containsKey(email)) {
/*      */           
/* 2926 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 2927 */           long now = System.currentTimeMillis();
/* 2928 */           ps = dbcon.prepareStatement("INSERT INTO PLAYEREHISTORYEMAIL(PLAYERID,EMAIL_ADDRESS,DATED) VALUES(?,?,?)");
/* 2929 */           ps.setLong(1, this.wurmId);
/* 2930 */           ps.setString(2, this.emailAddress);
/* 2931 */           ps.setLong(3, now);
/* 2932 */           ps.executeUpdate();
/* 2933 */           this.historyEmail.put(this.emailAddress, Long.valueOf(now));
/*      */         } 
/*      */         
/* 2936 */         if (this.historyEmail.size() > 5)
/*      */         {
/*      */           
/* 2939 */           long oldest = -1L;
/* 2940 */           String oldEmail = "";
/* 2941 */           for (Map.Entry<String, Long> entry : this.historyEmail.entrySet()) {
/*      */             
/* 2943 */             if (oldest == -1L || ((Long)entry.getValue()).longValue() < oldest) {
/*      */               
/* 2945 */               oldEmail = entry.getKey();
/* 2946 */               oldest = ((Long)entry.getValue()).longValue();
/*      */             } 
/*      */           } 
/*      */           
/* 2950 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 2951 */           ps = dbcon.prepareStatement("DELETE FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=? AND EMAIL_ADDRESS=?");
/* 2952 */           ps.setLong(1, this.wurmId);
/* 2953 */           ps.setString(2, oldEmail);
/* 2954 */           ps.executeUpdate();
/* 2955 */           this.historyEmail.remove(oldEmail);
/*      */         }
/*      */       
/* 2958 */       } catch (SQLException ex) {
/*      */         
/* 2960 */         logger.log(Level.WARNING, "Failed to set email for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 2964 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 2965 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPriest(boolean priest) {
/* 2973 */     if (this.isPriest != priest) {
/*      */       
/* 2975 */       this.isPriest = priest;
/* 2976 */       if (this.isPriest) {
/*      */         
/* 2978 */         Player p = Players.getInstance().getPlayerOrNull(this.wurmId);
/* 2979 */         if (p != null)
/* 2980 */           p.maybeTriggerAchievement(604, true); 
/*      */       } 
/* 2982 */       if (Servers.localServer.isChallengeOrEpicServer() && this.realdeath == 0) {
/*      */         
/*      */         try {
/*      */           
/* 2986 */           Skills skills = Players.getInstance().getPlayer(this.wurmId).getSkills();
/* 2987 */           if (skills != null)
/*      */           {
/* 2989 */             skills.priest = this.isPriest;
/*      */           }
/*      */         }
/* 2992 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2997 */       Connection dbcon = null;
/* 2998 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3001 */         dbcon = DbConnector.getPlayerDbCon();
/* 3002 */         ps = dbcon.prepareStatement("update PLAYERS set PRIEST=? WHERE NAME=?");
/* 3003 */         ps.setBoolean(1, this.isPriest);
/* 3004 */         ps.setString(2, this.name);
/* 3005 */         ps.executeUpdate();
/*      */       }
/* 3007 */       catch (SQLException ex) {
/*      */         
/* 3009 */         logger.log(Level.WARNING, "Failed to set priest for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3013 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3014 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBed(long _bed) {
/* 3022 */     if (this.bed != _bed) {
/*      */       
/* 3024 */       this.bed = _bed;
/* 3025 */       Connection dbcon = null;
/* 3026 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3029 */         dbcon = DbConnector.getPlayerDbCon();
/* 3030 */         ps = dbcon.prepareStatement("update PLAYERS set BED=? WHERE NAME=?");
/* 3031 */         ps.setLong(1, this.bed);
/* 3032 */         ps.setString(2, this.name);
/* 3033 */         ps.executeUpdate();
/*      */       }
/* 3035 */       catch (SQLException ex) {
/*      */         
/* 3037 */         logger.log(Level.WARNING, "Failed to set bed for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3041 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3042 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSleep(int _sleep) {
/* 3050 */     setSleep(_sleep, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSleep(int _sleep, boolean cap5h) {
/* 3063 */     int lSleep = (int)Math.min(Math.max(this.sleep, cap5h ? (3600L * (isFlagSet(77) ? 6L : 5L)) : 36000L), _sleep);
/* 3064 */     if (this.sleep != lSleep) {
/*      */       
/* 3066 */       this.sleep = lSleep;
/* 3067 */       Connection dbcon = null;
/* 3068 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3071 */         dbcon = DbConnector.getPlayerDbCon();
/* 3072 */         ps = dbcon.prepareStatement("update PLAYERS set SLEEP=? WHERE NAME=?");
/* 3073 */         ps.setInt(1, this.sleep);
/* 3074 */         ps.setString(2, this.name);
/* 3075 */         ps.executeUpdate();
/*      */       }
/* 3077 */       catch (SQLException ex) {
/*      */         
/* 3079 */         logger.log(Level.WARNING, "Failed to set sleep for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3083 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3084 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverRideShop(boolean mayUse) {
/* 3092 */     if (this.overRideShop != mayUse) {
/*      */       
/* 3094 */       this.overRideShop = mayUse;
/* 3095 */       Connection dbcon = null;
/* 3096 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3099 */         dbcon = DbConnector.getPlayerDbCon();
/* 3100 */         ps = dbcon.prepareStatement("update PLAYERS set MAYUSESHOP=? WHERE NAME=?");
/* 3101 */         ps.setBoolean(1, this.overRideShop);
/* 3102 */         ps.setString(2, this.name);
/* 3103 */         ps.executeUpdate();
/*      */       }
/* 3105 */       catch (SQLException ex) {
/*      */         
/* 3107 */         logger.log(Level.WARNING, "Failed to set use shop for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3111 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3112 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTheftwarned(boolean warned) {
/* 3120 */     if (this.isTheftWarned != warned) {
/*      */       
/* 3122 */       this.isTheftWarned = warned;
/* 3123 */       Connection dbcon = null;
/* 3124 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3127 */         dbcon = DbConnector.getPlayerDbCon();
/* 3128 */         ps = dbcon.prepareStatement("update PLAYERS set THEFTWARNED=? WHERE NAME=?");
/* 3129 */         ps.setBoolean(1, this.isTheftWarned);
/* 3130 */         ps.setString(2, this.name);
/* 3131 */         ps.executeUpdate();
/*      */       }
/* 3133 */       catch (SQLException ex) {
/*      */         
/* 3135 */         logger.log(Level.WARNING, "Failed to set theftwarned for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3139 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3140 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHasNoReimbursementLeft(boolean hasReimbursementLeft) {
/* 3148 */     if (this.noReimbursementLeft != hasReimbursementLeft) {
/*      */       
/* 3150 */       this.noReimbursementLeft = hasReimbursementLeft;
/* 3151 */       Connection dbcon = null;
/* 3152 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3155 */         dbcon = DbConnector.getPlayerDbCon();
/* 3156 */         ps = dbcon.prepareStatement("update PLAYERS set NOREIMB=? WHERE NAME=?");
/* 3157 */         ps.setBoolean(1, this.noReimbursementLeft);
/* 3158 */         ps.setString(2, this.name);
/* 3159 */         ps.executeUpdate();
/*      */       }
/* 3161 */       catch (SQLException ex) {
/*      */         
/* 3163 */         logger.log(Level.WARNING, "Failed to set noReimbursementLeft for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3167 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3168 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeathProtected(boolean _deathProtected) {
/* 3176 */     if (this.deathProtected != _deathProtected) {
/*      */       
/* 3178 */       this.deathProtected = _deathProtected;
/* 3179 */       Connection dbcon = null;
/* 3180 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3183 */         dbcon = DbConnector.getPlayerDbCon();
/* 3184 */         ps = dbcon.prepareStatement("update PLAYERS set DEATHPROT=? WHERE NAME=?");
/* 3185 */         ps.setBoolean(1, this.deathProtected);
/* 3186 */         ps.setString(2, this.name);
/* 3187 */         ps.executeUpdate();
/*      */       }
/* 3189 */       catch (SQLException ex) {
/*      */         
/* 3191 */         logger.log(Level.WARNING, "Failed to set deathProtected for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3195 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3196 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCurrentServer(int _currentServer) {
/* 3204 */     if (this.lastServer != this.currentServer || this.currentServer != _currentServer) {
/*      */       
/* 3206 */       this.lastServer = this.currentServer;
/* 3207 */       this.currentServer = _currentServer;
/* 3208 */       Connection dbcon = null;
/* 3209 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3212 */         dbcon = DbConnector.getPlayerDbCon();
/* 3213 */         ps = dbcon.prepareStatement("update PLAYERS set LASTSERVER=?,CURRENTSERVER=? WHERE NAME=?");
/* 3214 */         ps.setInt(1, this.lastServer);
/* 3215 */         ps.setInt(2, this.currentServer);
/* 3216 */         ps.setString(3, this.name);
/* 3217 */         ps.executeUpdate();
/*      */       }
/* 3219 */       catch (SQLException ex) {
/*      */         
/* 3221 */         logger.log(Level.WARNING, "Failed to set currentServer for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3225 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3226 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDevTalk(boolean _devtalk) {
/* 3234 */     if (this.mayHearDevTalk != _devtalk) {
/*      */       
/* 3236 */       this.mayHearDevTalk = _devtalk;
/* 3237 */       Connection dbcon = null;
/* 3238 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3241 */         dbcon = DbConnector.getPlayerDbCon();
/* 3242 */         ps = dbcon.prepareStatement("update PLAYERS set DEVTALK=? WHERE NAME=?");
/* 3243 */         ps.setBoolean(1, this.mayHearDevTalk);
/* 3244 */         ps.setString(2, this.name);
/* 3245 */         ps.executeUpdate();
/*      */       }
/* 3247 */       catch (SQLException ex) {
/*      */         
/* 3249 */         logger.log(Level.WARNING, "Failed to set devtalk for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3253 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3254 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastChangedVillage(long _lastChanged) {
/* 3262 */     if (this.lastChangedVillage != _lastChanged) {
/*      */       
/* 3264 */       this.lastChangedVillage = _lastChanged;
/* 3265 */       Connection dbcon = null;
/* 3266 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3269 */         dbcon = DbConnector.getPlayerDbCon();
/* 3270 */         ps = dbcon.prepareStatement("update PLAYERS set CHANGEDVILLAGE=? WHERE NAME=?");
/* 3271 */         ps.setLong(1, this.lastChangedVillage);
/* 3272 */         ps.setString(2, this.name);
/* 3273 */         ps.executeUpdate();
/*      */       }
/* 3275 */       catch (SQLException ex) {
/*      */         
/* 3277 */         logger.log(Level.WARNING, "Failed to set changedvillage for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3281 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3282 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveFightMode(byte _mode) {
/* 3290 */     if (this.fightmode != _mode) {
/*      */       
/* 3292 */       this.fightmode = _mode;
/* 3293 */       Connection dbcon = null;
/* 3294 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3297 */         dbcon = DbConnector.getPlayerDbCon();
/* 3298 */         ps = dbcon.prepareStatement("update PLAYERS set FIGHTMODE=? WHERE NAME=?");
/* 3299 */         ps.setByte(1, _mode);
/* 3300 */         ps.setString(2, this.name);
/* 3301 */         ps.executeUpdate();
/*      */       }
/* 3303 */       catch (SQLException ex) {
/*      */         
/* 3305 */         logger.log(Level.WARNING, "Failed to save fightmode for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3309 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3310 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNextAffinity(long next) {
/* 3318 */     if (this.nextAffinity != next) {
/*      */       
/* 3320 */       this.nextAffinity = next;
/* 3321 */       Connection dbcon = null;
/* 3322 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3325 */         dbcon = DbConnector.getPlayerDbCon();
/* 3326 */         ps = dbcon.prepareStatement("update PLAYERS set NEXTAFFINITY=? WHERE NAME=?");
/* 3327 */         ps.setLong(1, this.nextAffinity);
/* 3328 */         ps.setString(2, this.name);
/* 3329 */         ps.executeUpdate();
/*      */       }
/* 3331 */       catch (SQLException ex) {
/*      */         
/* 3333 */         logger.log(Level.WARNING, "Failed to save next affinity for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3337 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3338 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTutorialLevel(int newLevel) {
/* 3346 */     if (this.tutorialLevel != newLevel) {
/*      */       
/* 3348 */       this.tutorialLevel = newLevel;
/* 3349 */       Connection dbcon = null;
/* 3350 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3353 */         dbcon = DbConnector.getPlayerDbCon();
/* 3354 */         ps = dbcon.prepareStatement("update PLAYERS set TUTORIALLEVEL=? WHERE NAME=?");
/* 3355 */         ps.setInt(1, this.tutorialLevel);
/* 3356 */         ps.setString(2, this.name);
/* 3357 */         ps.executeUpdate();
/*      */       }
/* 3359 */       catch (SQLException ex) {
/*      */         
/* 3361 */         logger.log(Level.WARNING, "Failed to save tutorialLevel for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3365 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3366 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutofight(boolean af) {
/* 3374 */     if (this.autoFighting != af) {
/*      */       
/* 3376 */       this.autoFighting = af;
/* 3377 */       Connection dbcon = null;
/* 3378 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3381 */         dbcon = DbConnector.getPlayerDbCon();
/* 3382 */         ps = dbcon.prepareStatement("update PLAYERS set AUTOFIGHT=? WHERE NAME=?");
/* 3383 */         ps.setBoolean(1, this.autoFighting);
/* 3384 */         ps.setString(2, this.name);
/* 3385 */         ps.executeUpdate();
/*      */       }
/* 3387 */       catch (SQLException ex) {
/*      */         
/* 3389 */         logger.log(Level.WARNING, "Failed to set autofight for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3393 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3394 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveAppointments() {
/* 3402 */     Connection dbcon = null;
/* 3403 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3406 */       dbcon = DbConnector.getPlayerDbCon();
/* 3407 */       ps = dbcon.prepareStatement("update PLAYERS set APPOINTMENTS=? WHERE NAME=?");
/* 3408 */       ps.setLong(1, this.appointments);
/* 3409 */       ps.setString(2, this.name);
/* 3410 */       ps.executeUpdate();
/*      */     }
/* 3412 */     catch (SQLException ex) {
/*      */       
/* 3414 */       logger.log(Level.WARNING, "Failed to save appointments for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3418 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3419 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastVehicle(long _lastvehicle) {
/* 3426 */     if (this.lastvehicle != _lastvehicle) {
/*      */       
/* 3428 */       this.lastvehicle = _lastvehicle;
/* 3429 */       Connection dbcon = null;
/* 3430 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3433 */         dbcon = DbConnector.getPlayerDbCon();
/* 3434 */         ps = dbcon.prepareStatement("update PLAYERS set VEHICLE=? WHERE NAME=?");
/* 3435 */         ps.setLong(1, this.lastvehicle);
/* 3436 */         ps.setString(2, this.name);
/* 3437 */         ps.executeUpdate();
/*      */       }
/* 3439 */       catch (SQLException ex) {
/*      */         
/* 3441 */         logger.log(Level.WARNING, "Failed to save last vehicle for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3445 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3446 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsPlayerAssistant(boolean assistant) {
/* 3454 */     if (this.playerAssistant != assistant) {
/*      */       
/* 3456 */       this.playerAssistant = assistant;
/* 3457 */       Connection dbcon = null;
/* 3458 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3461 */         dbcon = DbConnector.getPlayerDbCon();
/* 3462 */         ps = dbcon.prepareStatement("update PLAYERS set PA=? WHERE NAME=?");
/* 3463 */         ps.setBoolean(1, this.playerAssistant);
/* 3464 */         ps.setString(2, this.name);
/* 3465 */         ps.executeUpdate();
/*      */       }
/* 3467 */       catch (SQLException ex) {
/*      */         
/* 3469 */         logger.log(Level.WARNING, "Failed to save pa for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3473 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3474 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMayAppointPlayerAssistant(boolean mayAppoint) {
/* 3482 */     if (this.mayAppointPlayerAssistant != mayAppoint) {
/*      */       
/* 3484 */       this.mayAppointPlayerAssistant = mayAppoint;
/* 3485 */       Connection dbcon = null;
/* 3486 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3489 */         dbcon = DbConnector.getPlayerDbCon();
/* 3490 */         ps = dbcon.prepareStatement("update PLAYERS set APPOINTPA=? WHERE NAME=?");
/* 3491 */         ps.setBoolean(1, this.mayAppointPlayerAssistant);
/* 3492 */         ps.setString(2, this.name);
/* 3493 */         ps.executeUpdate();
/*      */       }
/* 3495 */       catch (SQLException ex) {
/*      */         
/* 3497 */         logger.log(Level.WARNING, "Failed to save pa for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3501 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3502 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setHasSkillGain(boolean hasGain) {
/* 3510 */     if (this.hasSkillGain != hasGain) {
/*      */       
/* 3512 */       this.hasSkillGain = hasGain;
/* 3513 */       Connection dbcon = null;
/* 3514 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3517 */         dbcon = DbConnector.getPlayerDbCon();
/* 3518 */         ps = dbcon.prepareStatement("update PLAYERS set HASSKILLGAIN=? WHERE NAME=?");
/* 3519 */         ps.setBoolean(1, this.hasSkillGain);
/* 3520 */         ps.setString(2, this.name);
/* 3521 */         ps.executeUpdate();
/*      */       }
/* 3523 */       catch (SQLException ex) {
/*      */         
/* 3525 */         logger.log(Level.WARNING, "Failed to save skillgain for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3529 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3530 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 3533 */     return this.hasSkillGain;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChangedKingdom() {
/* 3539 */     this.lastChangedKindom = System.currentTimeMillis();
/* 3540 */     Connection dbcon = null;
/* 3541 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3544 */       dbcon = DbConnector.getPlayerDbCon();
/* 3545 */       ps = dbcon.prepareStatement("update PLAYERS set LASTCHANGEDKINGDOM=? WHERE NAME=?");
/* 3546 */       ps.setLong(1, this.lastChangedKindom);
/* 3547 */       ps.setString(2, this.name);
/* 3548 */       ps.executeUpdate();
/*      */     }
/* 3550 */     catch (SQLException ex) {
/*      */       
/* 3552 */       logger.log(Level.WARNING, "Failed to save kingdom stamp for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3556 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3557 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChampionTimeStamp() {
/* 3564 */     this.championTimeStamp = System.currentTimeMillis();
/* 3565 */     Connection dbcon = null;
/* 3566 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3569 */       dbcon = DbConnector.getPlayerDbCon();
/* 3570 */       ps = dbcon.prepareStatement("update PLAYERS set LASTLOSTCHAMPION=? WHERE NAME=?");
/* 3571 */       ps.setLong(1, this.championTimeStamp);
/* 3572 */       ps.setString(2, this.name);
/* 3573 */       ps.executeUpdate();
/*      */     }
/* 3575 */     catch (SQLException ex) {
/*      */       
/* 3577 */       logger.log(Level.WARNING, "Failed to save kingdom stamp for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3581 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3582 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChaosKingdom(byte newKingdom) {
/* 3590 */     if (this.chaosKingdom != newKingdom) {
/*      */       
/* 3592 */       this.chaosKingdom = newKingdom;
/* 3593 */       Connection dbcon = null;
/* 3594 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3597 */         dbcon = DbConnector.getPlayerDbCon();
/* 3598 */         ps = dbcon.prepareStatement("update PLAYERS set CHAOSKINGDOM=? WHERE NAME=?");
/* 3599 */         ps.setByte(1, this.chaosKingdom);
/* 3600 */         ps.setString(2, this.name);
/* 3601 */         ps.executeUpdate();
/*      */       }
/* 3603 */       catch (SQLException ex) {
/*      */         
/* 3605 */         logger.log(Level.WARNING, "Failed to save chaos kingdom for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3609 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3610 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChampChanneling(float channeling) {
/* 3618 */     this.champChanneling = channeling;
/* 3619 */     Connection dbcon = null;
/* 3620 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3623 */       dbcon = DbConnector.getPlayerDbCon();
/* 3624 */       ps = dbcon.prepareStatement("update PLAYERS set CHAMPCHANNELING=? WHERE NAME=?");
/* 3625 */       ps.setFloat(1, this.champChanneling);
/* 3626 */       ps.setString(2, this.name);
/* 3627 */       ps.executeUpdate();
/*      */     }
/* 3629 */     catch (SQLException ex) {
/*      */       
/* 3631 */       logger.log(Level.WARNING, "Failed to save kingdom stamp for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3635 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3636 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setChampionPoints(short points) {
/* 3643 */     points = (short)Math.max(0, points);
/* 3644 */     if (this.championPoints != points || (this.realdeath > 0 && points == 0)) {
/*      */       
/* 3646 */       this.championPoints = points;
/* 3647 */       logger.log(Level.INFO, "Set CHAMPION points of " + getName() + " to " + this.championPoints);
/* 3648 */       Connection dbcon = null;
/* 3649 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3652 */         dbcon = DbConnector.getPlayerDbCon();
/* 3653 */         ps = dbcon.prepareStatement("update PLAYERS set CHAMPIONPOINTS=? WHERE NAME=?");
/* 3654 */         ps.setShort(1, this.championPoints);
/* 3655 */         ps.setString(2, this.name);
/* 3656 */         ps.executeUpdate();
/*      */       }
/* 3658 */       catch (SQLException ex) {
/*      */         
/* 3660 */         logger.log(Level.WARNING, "Failed to save champion points for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3664 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3665 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3667 */       setPointsForChamp();
/*      */     } 
/* 3669 */     return (this.championPoints <= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean togglePlayerAssistantWindow(boolean sees) {
/* 3675 */     if (this.seesPlayerAssistantWindow != sees) {
/*      */       
/* 3677 */       this.seesPlayerAssistantWindow = sees;
/* 3678 */       Connection dbcon = null;
/* 3679 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3682 */         dbcon = DbConnector.getPlayerDbCon();
/* 3683 */         ps = dbcon.prepareStatement("update PLAYERS set PAWINDOW=? WHERE NAME=?");
/* 3684 */         ps.setBoolean(1, this.seesPlayerAssistantWindow);
/* 3685 */         ps.setString(2, this.name);
/* 3686 */         ps.executeUpdate();
/*      */       }
/* 3688 */       catch (SQLException ex) {
/*      */         
/* 3690 */         logger.log(Level.WARNING, "Failed to save pa for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3694 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3695 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/* 3698 */     return this.seesPlayerAssistantWindow;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNewPriestType(byte newType, long changeTime) {
/* 3704 */     if (this.priestType != newType) {
/*      */       
/* 3706 */       this.priestType = newType;
/* 3707 */       this.lastChangedPriestType = changeTime;
/* 3708 */       Connection dbcon = null;
/* 3709 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3712 */         dbcon = DbConnector.getPlayerDbCon();
/* 3713 */         ps = dbcon.prepareStatement("update PLAYERS set PRIESTTYPE=?,LASTCHANGEDPRIEST=? WHERE NAME=?");
/* 3714 */         ps.setByte(1, this.priestType);
/* 3715 */         ps.setLong(2, this.lastChangedPriestType);
/* 3716 */         ps.setString(3, this.name);
/* 3717 */         ps.executeUpdate();
/*      */       }
/* 3719 */       catch (SQLException ex) {
/*      */         
/* 3721 */         logger.log(Level.WARNING, "Failed to set priest type for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3725 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3726 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMovedInventory(boolean isMoved) {
/* 3734 */     this.hasMovedInventory = isMoved;
/* 3735 */     Connection dbcon = null;
/* 3736 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3739 */       dbcon = DbConnector.getPlayerDbCon();
/* 3740 */       ps = dbcon.prepareStatement("update PLAYERS set MOVEDINV=? WHERE NAME=?");
/* 3741 */       ps.setBoolean(1, this.hasMovedInventory);
/* 3742 */       ps.setString(2, this.name);
/* 3743 */       ps.executeUpdate();
/*      */     }
/* 3745 */     catch (SQLException ex) {
/*      */       
/* 3747 */       logger.log(Level.WARNING, "Failed to set moved inventory for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3751 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3752 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMuteTimes(short mutetimes) {
/* 3759 */     this.muteTimes = mutetimes;
/* 3760 */     Connection dbcon = null;
/* 3761 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3764 */       dbcon = DbConnector.getPlayerDbCon();
/* 3765 */       ps = dbcon.prepareStatement("update PLAYERS set MUTETIMES=? WHERE NAME=?");
/* 3766 */       ps.setShort(1, this.muteTimes);
/* 3767 */       ps.setString(2, this.name);
/* 3768 */       ps.executeUpdate();
/*      */     }
/* 3770 */     catch (SQLException ex) {
/*      */       
/* 3772 */       logger.log(Level.WARNING, "Failed to set mute times for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3776 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3777 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVotedKing(boolean voted) {
/* 3784 */     this.votedKing = voted;
/* 3785 */     Connection dbcon = null;
/* 3786 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3789 */       dbcon = DbConnector.getPlayerDbCon();
/* 3790 */       ps = dbcon.prepareStatement("update PLAYERS set VOTEDKING=? WHERE NAME=?");
/* 3791 */       ps.setBoolean(1, voted);
/* 3792 */       ps.setString(2, this.name);
/* 3793 */       ps.executeUpdate();
/*      */     }
/* 3795 */     catch (SQLException ex) {
/*      */       
/* 3797 */       logger.log(Level.WARNING, "Failed to set voted king for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3801 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3802 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEpicLocation(byte kingdom, int server) {
/* 3809 */     this.epicKingdom = kingdom;
/* 3810 */     this.epicServerId = server;
/* 3811 */     this.lastUsedEpicPortal = System.currentTimeMillis();
/* 3812 */     Connection dbcon = null;
/* 3813 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 3816 */       dbcon = DbConnector.getPlayerDbCon();
/* 3817 */       ps = dbcon.prepareStatement("update PLAYERS set EPICKINGDOM=?, EPICSERVER=? WHERE NAME=?");
/* 3818 */       ps.setByte(1, this.epicKingdom);
/* 3819 */       ps.setInt(2, this.epicServerId);
/* 3820 */       ps.setString(3, this.name);
/* 3821 */       ps.executeUpdate();
/*      */     }
/* 3823 */     catch (SQLException ex) {
/*      */       
/* 3825 */       logger.log(Level.WARNING, "Failed to set epic location for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 3829 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 3830 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHotaWins(short newHotaWins) {
/* 3837 */     if (this.hotaWins != newHotaWins) {
/*      */       
/* 3839 */       this.hotaWins = newHotaWins;
/* 3840 */       Connection dbcon = null;
/* 3841 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3844 */         dbcon = DbConnector.getPlayerDbCon();
/* 3845 */         ps = dbcon.prepareStatement("update PLAYERS set HOTA_WINS=? WHERE NAME=?");
/* 3846 */         ps.setShort(1, this.hotaWins);
/* 3847 */         ps.setString(2, this.name);
/* 3848 */         ps.executeUpdate();
/*      */       }
/* 3850 */       catch (SQLException ex) {
/*      */         
/* 3852 */         logger.log(Level.WARNING, "Failed to set hota wins for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3856 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3857 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/* 3859 */       checkHotaTitles();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpamMode(boolean newSpamMode) {
/* 3866 */     if (this.spamMode != newSpamMode) {
/*      */       
/* 3868 */       this.spamMode = newSpamMode;
/* 3869 */       Connection dbcon = null;
/* 3870 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3873 */         dbcon = DbConnector.getPlayerDbCon();
/* 3874 */         ps = dbcon.prepareStatement("update PLAYERS set SPAMMODE=? WHERE NAME=?");
/* 3875 */         ps.setBoolean(1, this.spamMode);
/* 3876 */         ps.setString(2, this.name);
/* 3877 */         ps.executeUpdate();
/*      */       }
/* 3879 */       catch (SQLException ex) {
/*      */         
/* 3881 */         logger.log(Level.WARNING, "Failed to set spamMode for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3885 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3886 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKarma(int newKarma) {
/* 3894 */     if (this.karma != newKarma) {
/*      */       
/* 3896 */       int diff = newKarma - this.karma;
/*      */       
/* 3898 */       this.karma = newKarma;
/* 3899 */       this.totalKarma += diff;
/* 3900 */       if (this.karma > this.maxKarma)
/* 3901 */         this.maxKarma = this.karma; 
/* 3902 */       Connection dbcon = null;
/* 3903 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3906 */         dbcon = DbConnector.getPlayerDbCon();
/* 3907 */         ps = dbcon.prepareStatement("update PLAYERS set KARMA=?, MAXKARMA=?, TOTALKARMA=? WHERE NAME=?");
/* 3908 */         ps.setInt(1, this.karma);
/* 3909 */         ps.setInt(2, this.maxKarma);
/* 3910 */         ps.setInt(3, this.totalKarma);
/* 3911 */         ps.setString(4, this.name);
/* 3912 */         ps.executeUpdate();
/*      */       }
/* 3914 */       catch (SQLException ex) {
/*      */         
/* 3916 */         logger.log(Level.WARNING, "Failed to set karma values for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 3920 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 3921 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setAbilityBits(long bits) {
/* 3928 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 3930 */       if (x == 0) {
/*      */         
/* 3932 */         if ((bits & 0x1L) == 1L) {
/* 3933 */           this.abilityBits.set(x, true);
/*      */         } else {
/* 3935 */           this.abilityBits.set(x, false);
/*      */         } 
/* 3937 */       } else if ((bits >> x & 0x1L) == 1L) {
/* 3938 */         this.abilityBits.set(x, true);
/*      */       } else {
/* 3940 */         this.abilityBits.set(x, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFlagBits(long bits) {
/* 3947 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 3949 */       if (x == 0) {
/*      */         
/* 3951 */         if ((bits & 0x1L) == 1L) {
/* 3952 */           this.flagBits.set(x, true);
/*      */         } else {
/* 3954 */           this.flagBits.set(x, false);
/*      */         } 
/* 3956 */       } else if ((bits >> x & 0x1L) == 1L) {
/* 3957 */         this.flagBits.set(x, true);
/*      */       } else {
/* 3959 */         this.flagBits.set(x, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFlag2Bits(long bits) {
/* 3966 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 3968 */       if (x == 0) {
/*      */         
/* 3970 */         if ((bits & 0x1L) == 1L) {
/* 3971 */           this.flag2Bits.set(x, true);
/*      */         } else {
/* 3973 */           this.flag2Bits.set(x, false);
/*      */         } 
/* 3975 */       } else if ((bits >> x & 0x1L) == 1L) {
/* 3976 */         this.flag2Bits.set(x, true);
/*      */       } else {
/* 3978 */         this.flag2Bits.set(x, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlood(byte newBlood) {
/* 3985 */     if (this.blood != newBlood) {
/*      */       
/* 3987 */       this.blood = newBlood;
/* 3988 */       Connection dbcon = null;
/* 3989 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 3992 */         dbcon = DbConnector.getPlayerDbCon();
/* 3993 */         ps = dbcon.prepareStatement("update PLAYERS set BLOOD=? WHERE NAME=?");
/* 3994 */         ps.setByte(1, this.blood);
/* 3995 */         ps.setString(2, this.name);
/* 3996 */         ps.executeUpdate();
/*      */       }
/* 3998 */       catch (SQLException ex) {
/*      */         
/* 4000 */         logger.log(Level.WARNING, "Failed to set blood for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 4004 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4005 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFlag(int number, boolean value) {
/* 4013 */     if (number < 64) {
/*      */       
/* 4015 */       this.flagBits.set(number, value);
/* 4016 */       this.flags = getFlagLong();
/*      */     }
/*      */     else {
/*      */       
/* 4020 */       this.flag2Bits.set(number - 64, value);
/* 4021 */       this.flags2 = getFlag2Long();
/*      */     } 
/* 4023 */     forceFlagsUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void forceFlagsUpdate() {
/* 4029 */     Connection dbcon = null;
/* 4030 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4033 */       dbcon = DbConnector.getPlayerDbCon();
/* 4034 */       ps = dbcon.prepareStatement("update PLAYERS set FLAGS=?,FLAGS2=? WHERE NAME=?");
/* 4035 */       ps.setLong(1, this.flags);
/* 4036 */       ps.setLong(2, this.flags2);
/* 4037 */       ps.setString(3, this.name);
/* 4038 */       ps.executeUpdate();
/*      */     }
/* 4040 */     catch (SQLException ex) {
/*      */       
/* 4042 */       logger.log(Level.WARNING, "Failed to save flags for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4046 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4047 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final long getFlagLong() {
/* 4054 */     long ret = 0L;
/* 4055 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 4057 */       if (this.flagBits.get(x))
/*      */       {
/* 4059 */         ret += 1L << x;
/*      */       }
/*      */     } 
/* 4062 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final long getFlag2Long() {
/* 4068 */     long ret = 0L;
/* 4069 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 4071 */       if (this.flag2Bits.get(x))
/*      */       {
/* 4073 */         ret += 1L << x;
/*      */       }
/*      */     } 
/* 4076 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAbility(int number, boolean value) {
/* 4082 */     this.abilityBits.set(number, value);
/* 4083 */     this.abilities = getAbilityLong();
/* 4084 */     Connection dbcon = null;
/* 4085 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4088 */       dbcon = DbConnector.getPlayerDbCon();
/* 4089 */       ps = dbcon.prepareStatement("update PLAYERS set ABILITIES=? WHERE NAME=?");
/* 4090 */       ps.setLong(1, this.abilities);
/* 4091 */       ps.setString(2, this.name);
/* 4092 */       ps.executeUpdate();
/*      */     }
/* 4094 */     catch (SQLException ex) {
/*      */       
/* 4096 */       logger.log(Level.WARNING, "Failed to save abilities for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4100 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4101 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCurrentAbilityTitle(int number) {
/* 4108 */     this.abilityTitle = number;
/* 4109 */     Connection dbcon = null;
/* 4110 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4113 */       dbcon = DbConnector.getPlayerDbCon();
/* 4114 */       ps = dbcon.prepareStatement("update PLAYERS set ABILITYTITLE=? WHERE NAME=?");
/* 4115 */       ps.setLong(1, this.abilityTitle);
/* 4116 */       ps.setString(2, this.name);
/* 4117 */       ps.executeUpdate();
/*      */     }
/* 4119 */     catch (SQLException ex) {
/*      */       
/* 4121 */       logger.log(Level.WARNING, "Failed to save abilities for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4125 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4126 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setScenarioKarma(int newValue) {
/* 4133 */     if (newValue != this.scenarioKarma) {
/*      */       
/* 4135 */       this.scenarioKarma = newValue;
/* 4136 */       Connection dbcon = null;
/* 4137 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 4140 */         dbcon = DbConnector.getPlayerDbCon();
/* 4141 */         ps = dbcon.prepareStatement("update PLAYERS set SCENARIOKARMA=? WHERE NAME=?");
/* 4142 */         ps.setInt(1, this.scenarioKarma);
/* 4143 */         ps.setString(2, this.name);
/* 4144 */         ps.executeUpdate();
/*      */       }
/* 4146 */       catch (SQLException ex) {
/*      */         
/* 4148 */         logger.log(Level.WARNING, "Failed to set scenario karma for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 4152 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4153 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUndeadData() {
/* 4161 */     Connection dbcon = null;
/* 4162 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4165 */       dbcon = DbConnector.getPlayerDbCon();
/* 4166 */       ps = dbcon.prepareStatement("update PLAYERS SET UNDEADTYPE=?,UNDEADKILLS=?,UNDEADPKILLS=?,UNDEADPSECS=? WHERE NAME=?");
/* 4167 */       ps.setByte(1, this.undeadType);
/* 4168 */       ps.setInt(2, this.undeadKills);
/* 4169 */       ps.setInt(3, this.undeadPlayerKills);
/* 4170 */       ps.setInt(4, this.undeadPlayerSeconds);
/* 4171 */       ps.setString(5, this.name);
/* 4172 */       ps.executeUpdate();
/*      */     }
/* 4174 */     catch (SQLException ex) {
/*      */       
/* 4176 */       logger.log(Level.WARNING, "Failed to set scenario karma for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4180 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4181 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final long getAbilityLong() {
/* 4187 */     long ret = 0L;
/* 4188 */     for (int x = 0; x < 64; x++) {
/*      */       
/* 4190 */       if (this.abilityBits.get(x))
/*      */       {
/* 4192 */         ret += 1L << x;
/*      */       }
/*      */     } 
/* 4195 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMoneyEarnedBySellingEver(long aMoney) {
/* 4201 */     this.moneyEarnedBySellingEver += aMoney;
/* 4202 */     Connection dbcon = null;
/* 4203 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4206 */       dbcon = DbConnector.getPlayerDbCon();
/* 4207 */       ps = dbcon.prepareStatement("update PLAYERS set MONEYSALES =? WHERE NAME=?");
/* 4208 */       ps.setLong(1, this.moneyEarnedBySellingEver);
/* 4209 */       ps.setString(2, this.name);
/* 4210 */       ps.executeUpdate();
/*      */     }
/* 4212 */     catch (SQLException ex) {
/*      */       
/* 4214 */       logger.log(Level.WARNING, "Failed to set moneyEarnedBySellingEver for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4218 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4219 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Awards loadAward(long wurmId) {
/* 4226 */     Connection dbcon = null;
/* 4227 */     PreparedStatement ps = null;
/* 4228 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 4231 */       dbcon = DbConnector.getPlayerDbCon();
/* 4232 */       ps = dbcon.prepareStatement("SELECT * FROM AWARDS WHERE WURMID=?");
/* 4233 */       ps.setLong(1, wurmId);
/* 4234 */       rs = ps.executeQuery();
/* 4235 */       if (rs.next())
/*      */       {
/* 4237 */         return new Awards(rs.getLong("WURMID"), rs.getInt("DAYSPREM"), rs.getInt("MONTHSEVER"), rs
/* 4238 */             .getInt("CONSECMONTHS"), rs.getInt("MONTHSPREM"), rs.getInt("SILVERSPURCHASED"), rs
/* 4239 */             .getLong("LASTTICKEDPREM"), rs.getInt("CURRENTLOYALTY"), rs.getInt("TOTALLOYALTY"), false);
/*      */       }
/*      */     }
/* 4242 */     catch (SQLException sqex) {
/*      */       
/* 4244 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 4248 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 4249 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 4251 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPointsForChamp() {
/* 4257 */     WurmRecord record = PlayerInfoFactory.getChampionRecord(this.name);
/* 4258 */     if (record == null) {
/*      */       
/* 4260 */       PlayerInfoFactory.addChampRecord(new WurmRecord(this.championPoints, this.name, true));
/* 4261 */       Connection dbcon = null;
/* 4262 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 4265 */         dbcon = DbConnector.getPlayerDbCon();
/* 4266 */         ps = dbcon.prepareStatement("INSERT INTO CHAMPIONS(POINTS,NAME,WURMID,CURRENT) VALUES (?,?,?,1)");
/* 4267 */         ps.setInt(1, this.championPoints);
/* 4268 */         ps.setString(2, this.name);
/* 4269 */         ps.setLong(3, this.wurmId);
/* 4270 */         ps.executeUpdate();
/*      */       }
/* 4272 */       catch (SQLException ex) {
/*      */         
/* 4274 */         logger.log(Level.WARNING, "Failed to set SET_CHAMPION for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 4278 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4279 */         DbConnector.returnConnection(dbcon);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4284 */       Connection dbcon = null;
/* 4285 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 4288 */         dbcon = DbConnector.getPlayerDbCon();
/* 4289 */         ps = dbcon.prepareStatement("update CHAMPIONS set POINTS=?,NAME=? WHERE WURMID=? AND CURRENT=1");
/* 4290 */         ps.setInt(1, this.championPoints);
/* 4291 */         ps.setString(2, this.name);
/* 4292 */         ps.setLong(3, this.wurmId);
/* 4293 */         ps.executeUpdate();
/*      */       }
/* 4295 */       catch (SQLException ex) {
/*      */         
/* 4297 */         logger.log(Level.WARNING, "Failed to set SET_CHAMPION for " + this.name, ex);
/*      */       }
/*      */       finally {
/*      */         
/* 4301 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 4302 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void switchChamp() {
/* 4310 */     Connection dbcon = null;
/* 4311 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 4314 */       dbcon = DbConnector.getPlayerDbCon();
/* 4315 */       ps = dbcon.prepareStatement("update CHAMPIONS set POINTS=?,NAME=?,CURRENT=0 WHERE WURMID=?");
/* 4316 */       ps.setInt(1, this.championPoints);
/* 4317 */       ps.setString(2, this.name);
/* 4318 */       ps.setLong(3, this.wurmId);
/* 4319 */       ps.executeUpdate();
/*      */     }
/* 4321 */     catch (SQLException ex) {
/*      */       
/* 4323 */       logger.log(Level.WARNING, "Failed to set SET_INACTIVE_CHAMPION for " + this.name, ex);
/*      */     }
/*      */     finally {
/*      */       
/* 4327 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 4328 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\DbPlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */