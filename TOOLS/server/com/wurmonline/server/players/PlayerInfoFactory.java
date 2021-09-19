/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.Mailer;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.intra.IntraCommand;
/*      */ import com.wurmonline.server.intra.IntraServerConnection;
/*      */ import com.wurmonline.server.intra.MoneyTransfer;
/*      */ import com.wurmonline.server.intra.PlayerTransfer;
/*      */ import com.wurmonline.server.intra.TimeTransfer;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.support.Tickets;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.webinterface.WcPlayerStatus;
/*      */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*      */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.rmi.RemoteException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ public final class PlayerInfoFactory
/*      */   implements TimeConstants, MiscConstants
/*      */ {
/*   76 */   private static final ConcurrentHashMap<String, PlayerInfo> playerInfos = new ConcurrentHashMap<>();
/*      */   
/*   78 */   private static final ConcurrentHashMap<Long, PlayerInfo> playerInfosWurmId = new ConcurrentHashMap<>();
/*      */   
/*   80 */   private static final ConcurrentHashMap<Long, PlayerState> playerStatus = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*   83 */   private static final Set<Long> failedIds = new HashSet<>();
/*      */ 
/*      */   
/*   86 */   private static final ConcurrentLinkedDeque<PlayerState> statesToUpdate = new ConcurrentLinkedDeque<>();
/*      */   
/*   88 */   private static final ConcurrentHashMap<Long, PlayerState> friendsToUpdate = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*   91 */   private static final Logger logger = Logger.getLogger(PlayerInfoFactory.class.getName());
/*      */   
/*   93 */   private static final Logger deletelogger = Logger.getLogger("deletions");
/*      */ 
/*      */   
/*      */   private static final String LOAD_AWARDS = "SELECT * FROM AWARDS";
/*      */   
/*      */   private static final String GET_ALL_PLAYERS = "SELECT * FROM PLAYERS";
/*      */   
/*      */   private static final long EXPIRATION_TIME = 7257600000L;
/*      */   
/*      */   protected static final long NOTICE_TIME = 604800000L;
/*      */   
/*  104 */   private static final Map<Long, Set<Referer>> referrers = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*      */   private static final String LOAD_REFERERS = "SELECT * FROM REFERERS";
/*      */   
/*      */   private static final String SET_REFERER = "UPDATE REFERERS SET HANDLED=1, MONEY=? WHERE WURMID=? AND REFERER=?";
/*      */   
/*      */   private static final String ADD_REFERER = "INSERT INTO REFERERS (WURMID, REFERER,HANDLED, MONEY ) VALUES(?,?,0,0)";
/*      */   
/*      */   private static final String REVERT_REFERER = "UPDATE REFERERS SET HANDLED=0, MONEY=0 WHERE WURMID=? AND REFERER=?";
/*      */   
/*      */   private static final String RESET_SCENARIOKARMA = "UPDATE PLAYERS SET SCENARIOKARMA=0";
/*      */   
/*  117 */   private static int deletedPlayers = 0;
/*      */   
/*      */   public static final String NOPERMISSION = "NO";
/*      */   
/*      */   public static final String RETRIEVAL = " Retrieval info updated.";
/*  122 */   private static long OFFLINETIME_UNTIL_FREEZE = 1296000000L;
/*      */   
/*  124 */   private static final LinkedList<WurmRecord> championRecords = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlayerInfo createPlayerInfo(String name) {
/*  135 */     name = LoginHandler.raiseFirstLetter(name);
/*  136 */     if (playerInfos.containsKey(name))
/*      */     {
/*  138 */       return playerInfos.get(name);
/*      */     }
/*  140 */     return new DbPlayerInfo(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addPlayerInfo(PlayerInfo info) {
/*  145 */     if (!doesPlayerInfoExist(info.getName())) {
/*      */       
/*  147 */       playerInfos.put(info.name, info);
/*  148 */       playerInfosWurmId.put(Long.valueOf(info.wurmId), info);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doesPlayerInfoExist(String aName) {
/*  158 */     return playerInfos.containsKey(aName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getPlayerMoney() {
/*  168 */     PlayerInfo[] p = getPlayerInfos();
/*  169 */     long toRet = 0L;
/*  170 */     for (PlayerInfo lElement : p) {
/*      */       
/*  172 */       if (lElement.currentServer == Servers.localServer.id || Servers.localServer.LOGINSERVER)
/*  173 */         toRet += lElement.money; 
/*      */     } 
/*  175 */     return toRet;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadReferers() throws IOException {
/*  180 */     long start = System.nanoTime();
/*  181 */     int loadedReferrers = 0;
/*  182 */     if (Servers.localServer.id == Servers.loginServer.id) {
/*      */       
/*  184 */       Connection dbcon = null;
/*  185 */       PreparedStatement ps = null;
/*  186 */       ResultSet rs = null;
/*      */       
/*      */       try {
/*  189 */         dbcon = DbConnector.getPlayerDbCon();
/*  190 */         ps = dbcon.prepareStatement("SELECT * FROM REFERERS");
/*      */ 
/*      */         
/*  193 */         rs = ps.executeQuery();
/*  194 */         while (rs.next())
/*      */         {
/*      */           
/*  197 */           Referer r = new Referer(rs.getLong("WURMID"), rs.getLong("REFERER"), rs.getBoolean("MONEY"), rs.getBoolean("HANDLED"));
/*  198 */           Long wid = new Long(r.getWurmid());
/*  199 */           Set<Referer> s = referrers.get(wid);
/*  200 */           if (s == null) {
/*      */             
/*  202 */             s = new HashSet<>();
/*  203 */             referrers.put(wid, s);
/*  204 */             loadedReferrers++;
/*      */           } 
/*  206 */           s.add(r);
/*      */         }
/*      */       
/*  209 */       } catch (SQLException sqex) {
/*      */         
/*  211 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*  212 */         throw new IOException(sqex);
/*      */       }
/*      */       finally {
/*      */         
/*  216 */         DbUtilities.closeDatabaseObjects(ps, rs);
/*  217 */         DbConnector.returnConnection(dbcon);
/*  218 */         long end = System.nanoTime();
/*  219 */         logger.info("Loaded " + loadedReferrers + " referrers from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  225 */       logger.info("Not Loading referrers from the database as this is not the login server");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean addReferrer(long wurmid, long referrer) throws IOException {
/*  231 */     Set<Referer> s = referrers.get(new Long(wurmid));
/*  232 */     if (s != null) {
/*      */       
/*  234 */       for (Referer referer : s) {
/*      */         
/*  236 */         if (referer.getReferer() == referrer) {
/*  237 */           return false;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/*  242 */       s = new HashSet<>();
/*  243 */       referrers.put(new Long(wurmid), s);
/*      */     } 
/*  245 */     Referer r = new Referer(wurmid, referrer);
/*  246 */     s.add(r);
/*  247 */     Connection dbcon = null;
/*  248 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  251 */       dbcon = DbConnector.getPlayerDbCon();
/*  252 */       ps = dbcon.prepareStatement("INSERT INTO REFERERS (WURMID, REFERER,HANDLED, MONEY ) VALUES(?,?,0,0)");
/*  253 */       ps.setLong(1, wurmid);
/*  254 */       ps.setLong(2, referrer);
/*  255 */       ps.executeUpdate();
/*      */     }
/*  257 */     catch (SQLException ex) {
/*      */       
/*  259 */       logger.log(Level.WARNING, "Failed to add referrer " + referrer + " for " + wurmid);
/*  260 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/*  264 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  265 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */     
/*  268 */     PlayerInfo pinf = getPlayerInfoWithWurmId(referrer);
/*  269 */     if (pinf != null)
/*  270 */       pinf.setReferedby(wurmid); 
/*  271 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean acceptReferer(long wurmid, long referrer, boolean money) throws IOException {
/*  276 */     Set<Referer> s = referrers.get(new Long(wurmid));
/*  277 */     if (s != null) {
/*      */       
/*  279 */       boolean found = false;
/*  280 */       for (Referer r : s) {
/*      */         
/*  282 */         if (r.getReferer() == referrer) {
/*      */           
/*  284 */           found = true;
/*  285 */           r.setMoney(money);
/*  286 */           r.setHandled(true);
/*      */           break;
/*      */         } 
/*      */       } 
/*  290 */       if (found) {
/*      */         
/*  292 */         Connection dbcon = null;
/*  293 */         PreparedStatement ps = null;
/*      */         
/*      */         try {
/*  296 */           dbcon = DbConnector.getPlayerDbCon();
/*  297 */           ps = dbcon.prepareStatement("UPDATE REFERERS SET HANDLED=1, MONEY=? WHERE WURMID=? AND REFERER=?");
/*  298 */           ps.setBoolean(1, money);
/*  299 */           ps.setLong(2, wurmid);
/*  300 */           ps.setLong(3, referrer);
/*  301 */           ps.executeUpdate();
/*  302 */           return true;
/*      */         }
/*  304 */         catch (SQLException ex) {
/*      */           
/*  306 */           logger.log(Level.WARNING, "Failed to set referrer " + referrer + " for " + wurmid + " and money=" + money);
/*  307 */           throw new IOException(ex);
/*      */         }
/*      */         finally {
/*      */           
/*  311 */           DbUtilities.closeDatabaseObjects(ps, null);
/*  312 */           DbConnector.returnConnection(dbcon);
/*      */         } 
/*      */       } 
/*      */     } 
/*  316 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void revertReferer(long wurmid, long referrer) throws IOException {
/*  321 */     Connection dbcon = null;
/*  322 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  325 */       dbcon = DbConnector.getPlayerDbCon();
/*  326 */       ps = dbcon.prepareStatement("UPDATE REFERERS SET HANDLED=0, MONEY=0 WHERE WURMID=? AND REFERER=?");
/*      */       
/*  328 */       ps.setLong(1, wurmid);
/*  329 */       ps.setLong(2, referrer);
/*  330 */       ps.executeUpdate();
/*      */     }
/*  332 */     catch (SQLException ex) {
/*      */       
/*  334 */       logger.log(Level.WARNING, "Failed to revert referrer " + referrer + " for " + wurmid);
/*  335 */       throw new IOException(ex);
/*      */     }
/*      */     finally {
/*      */       
/*  339 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  340 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Map<String, Byte> getReferrers(long wurmid) {
/*  346 */     Map<String, Byte> map = new HashMap<>();
/*  347 */     Set<Referer> s = referrers.get(new Long(wurmid));
/*  348 */     if (s != null)
/*      */     {
/*  350 */       for (Referer r : s) {
/*      */         
/*  352 */         byte type = 0;
/*  353 */         if (r.isHandled())
/*      */         {
/*  355 */           if (r.isMoney()) {
/*  356 */             type = 1;
/*      */           } else {
/*  358 */             type = 2;
/*      */           }  } 
/*  360 */         String name = String.valueOf(r.getReferer());
/*      */         
/*      */         try {
/*  363 */           name = Players.getInstance().getNameFor(r.getReferer());
/*      */         }
/*  365 */         catch (Exception e) {
/*      */           
/*  367 */           logger.log(Level.WARNING, "No name found for " + r.getReferer());
/*      */         } 
/*  369 */         map.put(name, Byte.valueOf(type));
/*      */       } 
/*      */     }
/*  372 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean addMoneyToBank(long wurmid, long moneyToAdd, String transactionDetail) throws Exception {
/*      */     try {
/*  380 */       Player p = Players.getInstance().getPlayer(wurmid);
/*  381 */       if (moneyToAdd >= 1000000L)
/*  382 */         logger.log(Level.INFO, "Adding " + moneyToAdd + " to " + p.getName(), new Exception()); 
/*  383 */       p.addMoney(moneyToAdd);
/*  384 */       long money = p.getMoney();
/*  385 */       new MoneyTransfer(p.getName(), p.getWurmId(), money, moneyToAdd, transactionDetail, (byte)0, "");
/*      */       
/*  387 */       Change change = new Change(moneyToAdd);
/*  388 */       Change current = new Change(money);
/*  389 */       p.save();
/*  390 */       p.getCommunicator().sendSafeServerMessage("An amount of " + change
/*  391 */           .getChangeString() + " has been added to your bank account. Current balance is " + current
/*  392 */           .getChangeString() + ".");
/*  393 */       if (transactionDetail.startsWith("Referred by "))
/*      */       {
/*  395 */         p.getSaveFile().addToSleep(3600);
/*  396 */         String sleepString = "You received an hour of sleep bonus which will increase your skill gain speed.";
/*  397 */         p.getCommunicator().sendSafeServerMessage("You received an hour of sleep bonus which will increase your skill gain speed.");
/*      */       }
/*      */     
/*  400 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/*  402 */       PlayerInfo p = getPlayerInfoWithWurmId(wurmid);
/*  403 */       if (p.wurmId > 0L) {
/*      */         
/*  405 */         if (moneyToAdd >= 1000000L)
/*  406 */           logger.log(Level.INFO, "Adding " + moneyToAdd + " to " + p.getName(), new Exception()); 
/*  407 */         p.setMoney(p.money + moneyToAdd);
/*  408 */         p.save();
/*  409 */         if (transactionDetail.startsWith("Referred by "))
/*      */         {
/*  411 */           p.addToSleep(3600);
/*      */         }
/*  413 */         if (Servers.localServer.id != p.currentServer) {
/*      */           
/*  415 */           new MoneyTransfer(p.getName(), p.wurmId, p.money, moneyToAdd, transactionDetail, (byte)5, "", false);
/*      */         }
/*      */         else {
/*      */           
/*  419 */           new MoneyTransfer(p.getName(), p.wurmId, p.money, moneyToAdd, transactionDetail, (byte)5, "");
/*      */         } 
/*      */       } else {
/*      */         
/*  423 */         return false;
/*      */       } 
/*  425 */     }  return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean addPlayingTime(long wurmid, int months, int days, String transactionDetail) throws Exception {
/*  431 */     if (wurmid < 0L || transactionDetail == null || transactionDetail.length() == 0)
/*      */     {
/*  433 */       throw new WurmServerException("Illegal arguments. Check if name or transaction detail is null or empty strings.");
/*      */     }
/*  435 */     if (months < 0 || days < 0)
/*      */     {
/*  437 */       throw new WurmServerException("Illegal arguments. Make sure that the values for days and months are not negative.");
/*      */     }
/*  439 */     long timeToAdd = 0L;
/*  440 */     if (days != 0)
/*  441 */       timeToAdd = days * 86400000L; 
/*  442 */     if (months != 0) {
/*  443 */       timeToAdd += months * 86400000L * 30L;
/*      */     }
/*      */     
/*      */     try {
/*  447 */       Player p = Players.getInstance().getPlayer(wurmid);
/*      */       
/*  449 */       long currTime = p.getPaymentExpire();
/*  450 */       currTime = Math.max(currTime, System.currentTimeMillis());
/*  451 */       currTime += timeToAdd;
/*  452 */       if (transactionDetail.startsWith("Referred by ")) {
/*      */         
/*  454 */         p.getSaveFile().addToSleep(3600);
/*  455 */         String sleepString = "You received an hour of sleep bonus which will increase your skill gain speed.";
/*  456 */         p.getCommunicator().sendSafeServerMessage("You received an hour of sleep bonus which will increase your skill gain speed.");
/*      */       } 
/*  458 */       p.setPaymentExpire(currTime);
/*  459 */       new TimeTransfer(p.getName(), p.getWurmId(), months, false, days, transactionDetail);
/*      */       
/*  461 */       p.save();
/*  462 */       p.getCommunicator().sendNormalServerMessage("You now have premier playing time until " + 
/*  463 */           WurmCalendar.formatGmt(currTime) + ".");
/*      */     }
/*  465 */     catch (NoSuchPlayerException nsp) {
/*      */       
/*  467 */       PlayerInfo p = getPlayerInfoWithWurmId(wurmid);
/*  468 */       if (p.wurmId > 0L) {
/*      */         
/*  470 */         long currTime = p.getPaymentExpire();
/*  471 */         currTime = Math.max(currTime, System.currentTimeMillis());
/*  472 */         currTime += timeToAdd;
/*  473 */         p.setPaymentExpire(currTime);
/*  474 */         if (transactionDetail.startsWith("Referred by "))
/*  475 */           p.addToSleep(3600); 
/*  476 */         if (p.currentServer != Servers.localServer.id) {
/*      */           
/*  478 */           new TimeTransfer(p.getName(), p.wurmId, months, false, days, transactionDetail, false);
/*      */         } else {
/*      */           
/*  481 */           new TimeTransfer(p.getName(), p.wurmId, months, false, days, transactionDetail);
/*      */         } 
/*      */       } else {
/*      */         
/*  485 */         return false;
/*      */       } 
/*      */     } 
/*  488 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pruneRanks(long now) {
/*  493 */     for (PlayerInfo pinf : getPlayerInfos()) {
/*      */       
/*  495 */       if (pinf.getRank() > 1000)
/*      */       {
/*  497 */         if (now - pinf.lastModifiedRank > 864000000L) {
/*      */           
/*      */           try {
/*      */             
/*  501 */             pinf.setRank((int)(pinf.getRank() * 0.975D));
/*  502 */             logger.log(Level.INFO, "Set rank of " + pinf.getName() + " to " + pinf.getRank());
/*      */           }
/*  504 */           catch (IOException iox) {
/*      */             
/*  506 */             logger.log(Level.INFO, pinf.getName() + ": " + iox.getMessage());
/*      */           } 
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void pollPremiumPlayers() {
/*  515 */     for (PlayerInfo info : getPlayerInfos()) {
/*      */       
/*  517 */       if (info.timeToCheckPrem-- <= 0) {
/*      */ 
/*      */         
/*  520 */         info.timeToCheckPrem = (int)((86400000L + System.currentTimeMillis()) / 1000L) + Server.rand.nextInt(200);
/*  521 */         if (info.getPower() <= 0 && info.paymentExpireDate > 0L && !info.isFlagSet(63))
/*      */         {
/*  523 */           if (info.awards != null)
/*      */           {
/*  525 */             if (System.currentTimeMillis() - info.awards.getLastTickedDay() > 86400000L) {
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
/*  576 */               boolean wasPrem = (info.awards.getLastTickedDay() < info.paymentExpireDate);
/*  577 */               if (info.isQAAccount() || info.paymentExpireDate > System.currentTimeMillis() || wasPrem) {
/*      */ 
/*      */                 
/*  580 */                 info.awards.setDaysPrem(info.awards.getDaysPrem() + 1);
/*      */                 
/*  582 */                 info.timeToCheckPrem = 86400 + Server.rand.nextInt(200);
/*  583 */                 if (info.awards.getDaysPrem() % 28 == 0)
/*      */                 {
/*  585 */                   info.awards.setMonthsPaidSinceReset(info.awards.getMonthsPaidSinceReset() + 1);
/*  586 */                   info.awards.setMonthsPaidInARow(info.awards.getMonthsPaidInARow() + 1);
/*  587 */                   AwardLadder.award(info, true);
/*      */                 }
/*      */               
/*  590 */               } else if (info.awards.getMonthsPaidInARow() > 0) {
/*      */                 
/*  592 */                 info.awards.setMonthsPaidInARow(0);
/*      */               } 
/*  594 */               info.awards.setLastTickedDay(System.currentTimeMillis());
/*  595 */               info.awards.update();
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/*  601 */               info
/*  602 */                 .timeToCheckPrem = (int)((info.awards.getLastTickedDay() + 86400000L - System.currentTimeMillis()) / 1000L) + 100;
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void checkIfDeleteOnePlayer() {
/*  612 */     long now = System.currentTimeMillis();
/*  613 */     boolean loginServer = Servers.localServer.LOGINSERVER;
/*  614 */     if (Constants.pruneDb)
/*      */     {
/*  616 */       if (Server.getSecondsUptime() > 30)
/*      */       {
/*  618 */         for (PlayerInfo pinf : getPlayerInfos()) {
/*      */           
/*  620 */           if (pinf.creationDate < now - 604800000L)
/*      */           {
/*  622 */             if (!pinf.isQAAccount())
/*      */             {
/*  624 */               if (pinf.power == 0 && pinf.playingTime < 86400000L && pinf.lastLogout < now - 7257600000L && (pinf.paymentExpireDate == 0L || pinf
/*  625 */                 .isFlagSet(63)) && Servers.localServer.id != 20 && pinf.currentServer != 20 && pinf.lastServer != 20) {
/*      */ 
/*      */                 
/*      */                 try {
/*      */                   
/*  630 */                   if (pinf.money < 50000L) {
/*      */ 
/*      */                     
/*  633 */                     deletedPlayers++;
/*  634 */                     Village[] vills = Villages.getVillages();
/*  635 */                     for (Village v : vills) {
/*      */                       
/*  637 */                       if (v.getMayor() != null && v.getMayor().getId() == pinf.wurmId)
/*      */                       {
/*  639 */                         v.disband(pinf.getName() + " deleted");
/*      */                       }
/*      */                     } 
/*  642 */                     Set<Item> items = Items.loadAllItemsForCreatureWithId(pinf.wurmId, pinf
/*  643 */                         .hasMovedInventory());
/*  644 */                     for (Item item : items) {
/*      */                       
/*  646 */                       if (!item.isIndestructible() && 
/*  647 */                         !item.isVillageDeed() && 
/*  648 */                         !item.isHomesteadDeed() && 
/*  649 */                         WurmId.getType(item.getWurmId()) != 19) {
/*      */                         
/*  651 */                         IntraServerConnection.deleteItem(item.getWurmId(), pinf.hasMovedInventory());
/*  652 */                         Items.removeItem(item.getWurmId());
/*      */                       } 
/*      */                     } 
/*  655 */                     IntraServerConnection.deletePlayer(pinf.wurmId);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*  662 */                     deletelogger.log(Level.INFO, "Deleted " + pinf.name + ", email[" + pinf.emailAddress + "] " + pinf.wurmId);
/*      */ 
/*      */ 
/*      */                     
/*  666 */                     MissionPerformed.deleteMissionPerformer(pinf.wurmId);
/*  667 */                     playerStatus.remove(Long.valueOf(pinf.wurmId));
/*  668 */                     playerInfos.remove(pinf.getName());
/*      */                     
/*      */                     return;
/*      */                   } 
/*      */                   
/*  673 */                   if (loginServer)
/*      */                   {
/*  675 */                     sendDeletePreventLetter(pinf);
/*      */                   }
/*      */                   
/*  678 */                   deletelogger.log(Level.INFO, "Kept and charged 5 silver from " + pinf.name + ", " + pinf);
/*      */                   
/*  680 */                   pinf.setMoney(pinf.money - 50000L);
/*  681 */                   pinf.lastLogout = now;
/*  682 */                   pinf.setFlag(8, false);
/*  683 */                   pinf.save();
/*      */ 
/*      */                   
/*      */                   return;
/*  687 */                 } catch (IOException iox) {
/*      */                   
/*  689 */                   logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */                 }
/*      */               
/*  692 */               } else if (!pinf.isOnlineHere() && now - pinf.lastLogout > OFFLINETIME_UNTIL_FREEZE) {
/*      */                 
/*  694 */                 if (!pinf.hasMovedInventory())
/*      */                 {
/*  696 */                   if (!failedIds.contains(Long.valueOf(pinf.wurmId)))
/*      */                   {
/*  698 */                     if (Items.moveItemsToFreezerFor(pinf.wurmId)) {
/*      */                       
/*  700 */                       pinf.setMovedInventory(true);
/*  701 */                       deletelogger.log(Level.INFO, "Froze items for " + pinf.getName());
/*      */                       
/*      */                       return;
/*      */                     } 
/*      */                     
/*  706 */                     failedIds.add(Long.valueOf(pinf.wurmId));
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/*  711 */               else if (pinf.power == 0 && pinf.playingTime < 86400000L && pinf.lastLogout < now - 7257600000L - 604800000L && (pinf.paymentExpireDate == 0L || pinf
/*  712 */                 .isFlagSet(63))) {
/*      */                 
/*  714 */                 if (loginServer)
/*      */                 {
/*  716 */                   if (!pinf.isFlagSet(8))
/*      */                   {
/*  718 */                     sendDeleteLetter(pinf);
/*      */                   }
/*      */                 }
/*      */               }
/*  722 */               else if (pinf.power == 0 && pinf.paymentExpireDate > now && pinf.paymentExpireDate < now + 604800000L) {
/*      */ 
/*      */                 
/*  725 */                 if (loginServer)
/*      */                 {
/*  727 */                   if (!pinf.isFlagSet(8))
/*      */                   {
/*  729 */                     sendPremiumWarningLetter(pinf);
/*      */                   }
/*      */                 }
/*      */               }
/*  733 */               else if (pinf.power == 0 && pinf.paymentExpireDate < now) {
/*      */                 
/*  735 */                 if (!pinf.isFlagSet(9)) {
/*      */                   
/*  737 */                   Server.addExpiry();
/*  738 */                   pinf.setFlag(9, true);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void sendDeletePreventLetter(PlayerInfo pinf) {
/*      */     try {
/*  752 */       String email = Mailer.getAccountDelPreventionMail();
/*  753 */       email = email.replace("@pname", pinf.getName());
/*      */       
/*  755 */       Mailer.sendMail(WebInterfaceImpl.mailAccount, pinf.emailAddress, "Wurm Online deletion protection", email);
/*      */     
/*      */     }
/*  758 */     catch (Exception ex) {
/*      */       
/*  760 */       logger.log(Level.INFO, ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void sendDeleteLetter(PlayerInfo pinf) {
/*      */     try {
/*  768 */       String email = Mailer.getAccountDelMail();
/*  769 */       email = email.replace("@pname", pinf.getName());
/*      */       
/*  771 */       Mailer.sendMail(WebInterfaceImpl.mailAccount, pinf.emailAddress, "Wurm Online character deletion", email);
/*      */     
/*      */     }
/*  774 */     catch (Exception ex) {
/*      */       
/*  776 */       logger.log(Level.INFO, ex.getMessage(), ex);
/*      */     } 
/*  778 */     pinf.setFlag(8, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void sendPremiumWarningLetter(PlayerInfo pinf) {
/*  783 */     if (pinf.awards != null) {
/*      */       
/*  785 */       String rewString = "We have no award specified at this level of total premium time since this program started";
/*  786 */       String reward = "unspecified";
/*  787 */       String nextRewardMonth = "lots more";
/*  788 */       int ql = (int)AwardLadder.consecutiveItemQL(pinf.awards.getMonthsPaidInARow() + 1);
/*  789 */       AwardLadder next = pinf.awards.getNextReward();
/*  790 */       if (next != null) {
/*      */         
/*  792 */         rewString = "Your next award is <i>@award</i> which will occur when you have @nextmonths months of premium time since this program started";
/*  793 */         reward = next.getName();
/*  794 */         nextRewardMonth = next.getMonthsRequiredReset() + "";
/*  795 */         rewString = rewString.replace("@award", reward);
/*  796 */         rewString = rewString.replace("@nextmonths", nextRewardMonth);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  805 */         String email = Mailer.getPremExpiryMail();
/*  806 */         email = email.replace("@pname", pinf.getName());
/*  807 */         email = email.replace("@reward", rewString);
/*  808 */         email = email.replace("@qualityLevel", ql + "");
/*  809 */         email = email.replace("@currmonths", pinf.awards.getMonthsPaidSinceReset() + "");
/*      */ 
/*      */         
/*  812 */         Mailer.sendMail(WebInterfaceImpl.mailAccount, pinf.emailAddress, "Wurm Online premium expiry warning", email);
/*      */       
/*      */       }
/*  815 */       catch (Exception ex) {
/*      */         
/*  817 */         logger.log(Level.INFO, ex.getMessage(), ex);
/*      */       } 
/*  819 */       pinf.setFlag(8, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Logger getDeleteLogger() {
/*  825 */     return deletelogger;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAwards() {
/*  830 */     Connection dbcon = null;
/*  831 */     PreparedStatement ps = null;
/*  832 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  835 */       dbcon = DbConnector.getPlayerDbCon();
/*  836 */       ps = dbcon.prepareStatement("SELECT * FROM AWARDS");
/*  837 */       rs = ps.executeQuery();
/*  838 */       while (rs.next())
/*      */       {
/*  840 */         new Awards(rs.getLong("WURMID"), rs.getInt("DAYSPREM"), rs.getInt("MONTHSEVER"), rs.getInt("CONSECMONTHS"), rs
/*  841 */             .getInt("MONTHSPREM"), rs.getInt("SILVERSPURCHASED"), rs.getLong("LASTTICKEDPREM"), rs.getInt("CURRENTLOYALTY"), rs
/*  842 */             .getInt("TOTALLOYALTY"), false);
/*      */       }
/*      */     }
/*  845 */     catch (SQLException sqex) {
/*      */       
/*  847 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  851 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  852 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadPlayerInfos() throws IOException {
/*  858 */     Players.loadAllArtists();
/*      */ 
/*      */     
/*  861 */     loadAwards();
/*  862 */     long now = System.currentTimeMillis();
/*  863 */     Connection dbcon = null;
/*  864 */     PreparedStatement ps = null;
/*  865 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  868 */       if (Constants.pruneDb)
/*  869 */         logger.log(Level.INFO, "Loading player infos. Going to prune DB."); 
/*  870 */       dbcon = DbConnector.getPlayerDbCon();
/*  871 */       ps = dbcon.prepareStatement("SELECT * FROM PLAYERS");
/*  872 */       rs = ps.executeQuery();
/*  873 */       while (rs.next())
/*      */       {
/*  875 */         String name = rs.getString("NAME");
/*  876 */         name = LoginHandler.raiseFirstLetter(name);
/*  877 */         DbPlayerInfo pinf = new DbPlayerInfo(name);
/*  878 */         pinf.wurmId = rs.getLong("WURMID");
/*  879 */         pinf.password = rs.getString("PASSWORD");
/*  880 */         pinf.playingTime = rs.getLong("PLAYINGTIME");
/*  881 */         pinf.reimbursed = rs.getBoolean("REIMBURSED");
/*  882 */         pinf.plantedSign = rs.getLong("PLANTEDSIGN");
/*  883 */         pinf.ipaddress = rs.getString("IPADDRESS");
/*  884 */         pinf.banned = rs.getBoolean("BANNED");
/*  885 */         pinf.power = rs.getByte("POWER");
/*  886 */         pinf.rank = rs.getInt("RANK");
/*  887 */         pinf.maxRank = rs.getInt("MAXRANK");
/*  888 */         pinf.lastModifiedRank = rs.getLong("LASTMODIFIEDRANK");
/*  889 */         pinf.mayHearDevTalk = rs.getBoolean("DEVTALK");
/*  890 */         pinf.paymentExpireDate = rs.getLong("PAYMENTEXPIRE");
/*  891 */         pinf.lastWarned = rs.getLong("LASTWARNED");
/*  892 */         pinf.warnings = rs.getShort("WARNINGS");
/*  893 */         pinf.lastCheated = rs.getLong("CHEATED");
/*  894 */         pinf.lastFatigue = rs.getLong("LASTFATIGUE");
/*  895 */         pinf.fatigueSecsLeft = rs.getInt("FATIGUE");
/*  896 */         pinf.fatigueSecsToday = rs.getInt("FATIGUETODAY");
/*  897 */         pinf.fatigueSecsYesterday = rs.getInt("FATIGUEYDAY");
/*  898 */         pinf.dead = rs.getBoolean("DEAD");
/*  899 */         pinf.version = rs.getLong("VERSION");
/*  900 */         pinf.money = rs.getLong("MONEY");
/*  901 */         pinf.climbing = rs.getBoolean("CLIMBING");
/*  902 */         pinf.banexpiry = rs.getLong("BANEXPIRY");
/*  903 */         pinf.banreason = rs.getString("BANREASON");
/*  904 */         pinf.emailAddress = rs.getString("EMAIL");
/*  905 */         if (pinf.banreason == null)
/*  906 */           pinf.banreason = ""; 
/*  907 */         pinf.logging = rs.getBoolean("LOGGING");
/*  908 */         pinf.referrer = rs.getLong("REFERRER");
/*  909 */         pinf.isPriest = rs.getBoolean("PRIEST");
/*  910 */         pinf.bed = rs.getLong("BED");
/*  911 */         pinf.sleep = rs.getInt("SLEEP");
/*  912 */         pinf.isTheftWarned = rs.getBoolean("THEFTWARNED");
/*  913 */         pinf.noReimbursementLeft = rs.getBoolean("NOREIMB");
/*  914 */         pinf.deathProtected = rs.getBoolean("DEATHPROT");
/*  915 */         pinf.tutorialLevel = rs.getInt("TUTORIALLEVEL");
/*  916 */         pinf.autoFighting = rs.getBoolean("AUTOFIGHT");
/*  917 */         pinf.appointments = rs.getLong("APPOINTMENTS");
/*  918 */         pinf.playerAssistant = rs.getBoolean("PA");
/*  919 */         pinf.mayAppointPlayerAssistant = rs.getBoolean("APPOINTPA");
/*  920 */         pinf.seesPlayerAssistantWindow = rs.getBoolean("PAWINDOW");
/*  921 */         pinf.hasFreeTransfer = rs.getBoolean("FREETRANSFER");
/*  922 */         pinf.votedKing = rs.getBoolean("VOTEDKING");
/*      */         
/*  924 */         byte kingdom = rs.getByte("KINGDOM");
/*  925 */         Players.getInstance().registerNewKingdom(pinf.wurmId, kingdom);
/*      */         
/*  927 */         if (pinf.playingTime < 0L) {
/*  928 */           pinf.playingTime = 0L;
/*      */         }
/*  930 */         pinf.alignment = rs.getFloat("ALIGNMENT");
/*  931 */         byte deityNum = rs.getByte("DEITY");
/*  932 */         if (deityNum > 0) {
/*      */           
/*  934 */           Deity d = Deities.getDeity(deityNum);
/*  935 */           pinf.deity = d;
/*      */         }
/*      */         else {
/*      */           
/*  939 */           pinf.deity = null;
/*      */         } 
/*  941 */         pinf.favor = rs.getFloat("FAVOR");
/*  942 */         pinf.faith = rs.getFloat("FAITH");
/*  943 */         byte gid = rs.getByte("GOD");
/*  944 */         if (gid > 0) {
/*      */           
/*  946 */           Deity d = Deities.getDeity(gid);
/*  947 */           pinf.god = d;
/*      */         } 
/*  949 */         pinf.lastChangedDeity = rs.getLong("LASTCHANGEDDEITY");
/*  950 */         pinf.changedKingdom = rs.getByte("NUMSCHANGEDKINGDOM");
/*  951 */         pinf.realdeath = rs.getByte("REALDEATH");
/*  952 */         pinf.muted = rs.getBoolean("MUTED");
/*  953 */         pinf.muteTimes = rs.getShort("MUTETIMES");
/*  954 */         pinf.lastFaith = rs.getLong("LASTFAITH");
/*  955 */         pinf.numFaith = rs.getByte("NUMFAITH");
/*  956 */         pinf.creationDate = rs.getLong("CREATIONDATE");
/*  957 */         long face = rs.getLong("FACE");
/*  958 */         if (face == 0L)
/*  959 */           face = Server.rand.nextLong(); 
/*  960 */         pinf.face = face;
/*  961 */         pinf.reputation = rs.getInt("REPUTATION");
/*  962 */         pinf.lastPolledReputation = rs.getLong("LASTPOLLEDREP");
/*  963 */         if (pinf.lastPolledReputation == 0L)
/*  964 */           pinf.lastPolledReputation = System.currentTimeMillis(); 
/*  965 */         int titnum = rs.getInt("TITLE");
/*  966 */         if (titnum > 0) {
/*  967 */           pinf.title = Titles.Title.getTitle(titnum);
/*      */         }
/*      */         try {
/*  970 */           int secTitleNum = rs.getInt("SECONDTITLE");
/*  971 */           if (secTitleNum > 0)
/*      */           {
/*  973 */             pinf.secondTitle = Titles.Title.getTitle(secTitleNum);
/*      */           }
/*      */         }
/*  976 */         catch (SQLException ex) {
/*      */           
/*  978 */           logger.severe("You may need to run the script addSecondTitle.sql!");
/*  979 */           logger.severe(ex.getMessage());
/*  980 */           pinf.secondTitle = null;
/*      */         } 
/*  982 */         pinf.pet = rs.getLong("PET");
/*  983 */         pinf.lastLogout = rs.getLong("LASTLOGOUT");
/*      */         
/*  985 */         pinf.nicotine = rs.getFloat("NICOTINE");
/*  986 */         pinf.alcohol = rs.getFloat("ALCOHOL");
/*  987 */         pinf.nicotineAddiction = rs.getLong("NICOTINETIME");
/*  988 */         pinf.alcoholAddiction = rs.getLong("ALCOHOLTIME");
/*  989 */         pinf.mayMute = rs.getBoolean("MAYMUTE");
/*  990 */         pinf.overRideShop = rs.getBoolean("MAYUSESHOP");
/*  991 */         pinf.muteexpiry = rs.getLong("MUTEEXPIRY");
/*  992 */         pinf.mutereason = rs.getString("MUTEREASON");
/*  993 */         pinf.lastServer = rs.getInt("LASTSERVER");
/*  994 */         pinf.currentServer = rs.getInt("CURRENTSERVER");
/*  995 */         pinf.pwQuestion = rs.getString("PWQUESTION");
/*  996 */         pinf.pwAnswer = rs.getString("PWANSWER");
/*  997 */         pinf.lastChangedVillage = rs.getLong("CHANGEDVILLAGE");
/*  998 */         pinf.fightmode = rs.getByte("FIGHTMODE");
/*  999 */         pinf.nextAffinity = rs.getLong("NEXTAFFINITY");
/* 1000 */         pinf.lastvehicle = rs.getLong("VEHICLE");
/* 1001 */         pinf.lastTaggedKindom = rs.getByte("ENEMYTERR");
/* 1002 */         pinf.lastMovedBetweenKingdom = rs.getLong("LASTMOVEDTERR");
/* 1003 */         pinf.priestType = rs.getByte("PRIESTTYPE");
/* 1004 */         pinf.lastChangedPriestType = rs.getLong("LASTCHANGEDPRIEST");
/* 1005 */         pinf.hasMovedInventory = rs.getBoolean("MOVEDINV");
/* 1006 */         pinf.hasSkillGain = rs.getBoolean("HASSKILLGAIN");
/* 1007 */         pinf.lastTriggerEffect = rs.getInt("LASTTRIGGER");
/* 1008 */         pinf.lastChangedKindom = rs.getLong("LASTCHANGEDKINGDOM");
/* 1009 */         pinf.championTimeStamp = rs.getLong("LASTLOSTCHAMPION");
/* 1010 */         pinf.championPoints = rs.getShort("CHAMPIONPOINTS");
/* 1011 */         pinf.champChanneling = rs.getFloat("CHAMPCHANNELING");
/* 1012 */         pinf.epicKingdom = rs.getByte("EPICKINGDOM");
/* 1013 */         pinf.epicServerId = rs.getInt("EPICSERVER");
/* 1014 */         pinf.chaosKingdom = rs.getByte("CHAOSKINGDOM");
/* 1015 */         pinf.hotaWins = rs.getShort("HOTA_WINS");
/* 1016 */         pinf.spamMode = rs.getBoolean("SPAMMODE");
/* 1017 */         pinf.karma = rs.getInt("KARMA");
/* 1018 */         pinf.maxKarma = rs.getInt("MAXKARMA");
/* 1019 */         pinf.totalKarma = rs.getInt("TOTALKARMA");
/* 1020 */         pinf.blood = rs.getByte("BLOOD");
/* 1021 */         pinf.flags = rs.getLong("FLAGS");
/* 1022 */         pinf.flags2 = rs.getLong("FLAGS2");
/* 1023 */         pinf.abilities = rs.getLong("ABILITIES");
/* 1024 */         pinf.abilityTitle = rs.getInt("ABILITYTITLE");
/* 1025 */         pinf.undeadType = rs.getByte("UNDEADTYPE");
/* 1026 */         pinf.undeadKills = rs.getInt("UNDEADKILLS");
/* 1027 */         pinf.undeadPlayerKills = rs.getInt("UNDEADPKILLS");
/* 1028 */         pinf.undeadPlayerSeconds = rs.getInt("UNDEADPSECS");
/* 1029 */         pinf.moneyEarnedBySellingEver = rs.getLong("MONEYSALES");
/* 1030 */         pinf.setFlagBits(pinf.flags);
/* 1031 */         pinf.setFlag2Bits(pinf.flags2);
/* 1032 */         pinf.setAbilityBits(pinf.abilities);
/* 1033 */         pinf.scenarioKarma = rs.getInt("SCENARIOKARMA");
/* 1034 */         pinf.loaded = true;
/*      */         
/* 1036 */         if (Servers.localServer.id == pinf.currentServer || Servers.localServer.LOGINSERVER)
/*      */         {
/* 1038 */           if (pinf.paymentExpireDate > 0L)
/*      */           {
/*      */ 
/*      */             
/* 1042 */             pinf.awards = Awards.getAwards(pinf.wurmId);
/*      */           }
/*      */         }
/*      */         
/* 1046 */         playerInfos.put(name, pinf);
/* 1047 */         playerInfosWurmId.put(Long.valueOf(pinf.wurmId), pinf);
/*      */ 
/*      */         
/* 1050 */         if (Servers.isThisLoginServer()) {
/* 1051 */           playerStatus.put(Long.valueOf(pinf.wurmId), new PlayerState(pinf.currentServer, pinf.wurmId, pinf.name, pinf.lastLogin, pinf.lastLogout, PlayerOnlineStatus.OFFLINE));
/*      */         }
/*      */       }
/*      */     
/* 1055 */     } catch (SQLException sqex) {
/*      */       
/* 1057 */       logger.log(Level.WARNING, sqex.getMessage(), sqex);
/* 1058 */       throw new IOException(sqex);
/*      */     }
/*      */     finally {
/*      */       
/* 1062 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1063 */       DbConnector.returnConnection(dbcon);
/* 1064 */       long end = System.currentTimeMillis();
/* 1065 */       logger.info("Loaded " + playerInfos.size() + " PlayerInfos from the database took " + (end - now) + " ms");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void transferPlayersToWild() {
/* 1071 */     logger.log(Level.INFO, "Starting to migrate accounts");
/* 1072 */     ServerEntry localServer = Servers.localServer;
/* 1073 */     ServerEntry wildServer = Servers.getServerWithId(3);
/* 1074 */     String targetIp = wildServer.INTRASERVERADDRESS;
/* 1075 */     int targetPort = Integer.parseInt(wildServer.INTRASERVERPORT);
/* 1076 */     String serverpass = wildServer.INTRASERVERPASSWORD;
/* 1077 */     int tilex = wildServer.SPAWNPOINTJENNX;
/* 1078 */     int tiley = wildServer.SPAWNPOINTJENNY;
/*      */     
/* 1080 */     for (PlayerInfo p : playerInfos.values()) {
/*      */       
/* 1082 */       if (p.getPaymentExpire() > 0L || p.money >= 50000L)
/*      */       {
/* 1084 */         if (p.currentServer == localServer.id && !p.banned)
/*      */           
/*      */           try {
/* 1087 */             Player player = new Player(p);
/* 1088 */             Server.getInstance().addPlayer(player);
/*      */             
/* 1090 */             player.checkBodyInventoryConsistency();
/*      */             
/* 1092 */             player.loadSkills();
/*      */             
/* 1094 */             Items.loadAllItemsForCreature(player, player.getStatus().getInventoryId());
/*      */             
/* 1096 */             player.getBody().load();
/*      */             
/* 1098 */             PlayerTransfer.willItemsTransfer(player, true, 3);
/* 1099 */             tilex = wildServer.SPAWNPOINTJENNX;
/* 1100 */             tiley = wildServer.SPAWNPOINTJENNY;
/* 1101 */             if (player.getKingdomId() == 3) {
/*      */               
/* 1103 */               tilex = wildServer.SPAWNPOINTLIBX;
/* 1104 */               tiley = wildServer.SPAWNPOINTLIBY;
/*      */             }
/* 1106 */             else if (player.getKingdomId() == 2) {
/*      */               
/* 1108 */               tilex = wildServer.SPAWNPOINTMOLX;
/* 1109 */               tiley = wildServer.SPAWNPOINTMOLY;
/*      */             } 
/*      */             
/* 1112 */             PlayerTransfer pt = new PlayerTransfer(Server.getInstance(), player, targetIp, targetPort, serverpass, 3, tilex, tiley, true, false, player.getKingdomId());
/*      */             
/* 1114 */             pt.copiedToLoginServer = true;
/* 1115 */             Server.getInstance().addIntraCommand((IntraCommand)pt);
/*      */           }
/* 1117 */           catch (Exception ex) {
/*      */             
/* 1119 */             logger.log(Level.INFO, ex.getMessage(), ex);
/*      */           }  
/*      */       }
/*      */     } 
/* 1123 */     logger.log(Level.INFO, "Created intra commands");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final PlayerInfo[] getPlayerInfos() {
/* 1132 */     return (PlayerInfo[])playerInfos.values().toArray((Object[])new PlayerInfo[playerInfos.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final PlayerInfo[] getPlayerInfosWithEmail(String email) {
/* 1137 */     Set<PlayerInfo> infos = new HashSet<>();
/* 1138 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1140 */       if (info == null) {
/* 1141 */         logger.log(Level.WARNING, "getPlayerInfosWithEmail() NULL in playerInfos.values()??");
/*      */         
/*      */         continue;
/*      */       } 
/*      */       try {
/* 1146 */         info.load();
/* 1147 */         if (wildCardMatch(info.emailAddress.toLowerCase(), email.toLowerCase()))
/*      */         {
/* 1149 */           infos.add(info);
/*      */         }
/*      */       }
/* 1152 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1155 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */     
/* 1159 */     return infos.<PlayerInfo>toArray(new PlayerInfo[infos.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final PlayerInfo[] getPlayerInfosWithIpAddress(String ipaddress) {
/* 1164 */     Set<PlayerInfo> infos = new HashSet<>();
/* 1165 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1167 */       if (info == null) {
/* 1168 */         logger.log(Level.WARNING, "getPlayerInfosWithIpAddress() NULL in playerInfos.values()??");
/*      */         
/*      */         continue;
/*      */       } 
/*      */       try {
/* 1173 */         info.load();
/* 1174 */         if (info.ipaddress != null && wildCardMatch(info.ipaddress, ipaddress))
/*      */         {
/* 1176 */           infos.add(info);
/*      */         }
/*      */       }
/* 1179 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1182 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */     
/* 1186 */     return infos.<PlayerInfo>toArray(new PlayerInfo[infos.size()]);
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
/*      */   public static boolean wildCardMatch(String text, String pattern) {
/* 1206 */     String[] cards = pattern.split("\\*");
/*      */ 
/*      */     
/* 1209 */     int offset = 0;
/* 1210 */     boolean first = true;
/* 1211 */     for (String card : cards) {
/*      */       
/* 1213 */       if (card.length() > 0) {
/*      */         
/* 1215 */         int idx = text.indexOf(card, offset);
/*      */ 
/*      */         
/* 1218 */         if (idx == -1 || (first && idx != 0)) {
/* 1219 */           return false;
/*      */         }
/*      */         
/* 1222 */         offset = idx + card.length();
/*      */       } 
/*      */       
/* 1225 */       first = false;
/*      */     } 
/* 1227 */     if (offset < text.length() && !pattern.endsWith("*"))
/* 1228 */       return false; 
/* 1229 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlayerInfo getPlayerInfoWithWurmId(long wurmId) {
/* 1234 */     return playerInfosWurmId.get(Long.valueOf(wurmId));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Optional<PlayerInfo> getPlayerInfoOptional(long wurmId) {
/* 1239 */     return Optional.ofNullable(playerInfosWurmId.get(Long.valueOf(wurmId)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlayerInfo getPlayerInfoWithName(String name) {
/* 1244 */     return playerInfos.get(LoginHandler.raiseFirstLetter(name));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Optional<PlayerInfo> getPlayerInfoOptional(String name) {
/* 1249 */     return Optional.ofNullable(playerInfos.get(LoginHandler.raiseFirstLetter(name)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<Long, byte[]> getPlayerStates(long[] wurmids) throws RemoteException, WurmServerException {
/* 1254 */     if (Servers.localServer.id == Servers.loginServer.id) {
/*      */       
/* 1256 */       Map<Long, byte[]> toReturn = (Map)new HashMap<>();
/* 1257 */       if (wurmids.length > 0) {
/*      */         
/* 1259 */         for (int x = 0; x < wurmids.length; x++)
/*      */         {
/* 1261 */           toReturn.put(Long.valueOf(wurmids[x]), (new PlayerState(wurmids[x])).encode());
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1267 */         for (PlayerState pState : playerStatus.values())
/*      */         {
/* 1269 */           toReturn.put(Long.valueOf(pState.getPlayerId()), pState.encode());
/*      */         }
/*      */       } 
/* 1272 */       return toReturn;
/*      */     } 
/*      */ 
/*      */     
/* 1276 */     LoginServerWebConnection lsw = new LoginServerWebConnection();
/* 1277 */     return lsw.getPlayerStates(wurmids);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static PlayerState getPlayerState(long playerWurmId) {
/* 1283 */     return playerStatus.get(Long.valueOf(playerWurmId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean playerJustTransfered(PlayerState playerState) {
/* 1291 */     PlayerInfo pinf = getPlayerInfoWithWurmId(playerState.getPlayerId());
/* 1292 */     if (pinf != null) {
/*      */       
/*      */       try {
/*      */         
/* 1296 */         pinf.load();
/* 1297 */         if (pinf.currentServer != Servers.getLocalServerId())
/*      */         {
/*      */           
/* 1300 */           return true;
/*      */         }
/*      */       }
/* 1303 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1306 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1313 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void setPlantedSignFalse() {
/* 1318 */     for (PlayerInfo info : playerInfos.values())
/*      */     {
/* 1320 */       info.plantedSign = 0L;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean doesPlayerExist(String name) {
/* 1326 */     name = LoginHandler.raiseFirstLetter(name);
/* 1327 */     return doesPlayerInfoExist(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] getAccountsForEmail(String email) {
/* 1332 */     Set<String> set = new HashSet<>();
/* 1333 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1335 */       if (info.emailAddress.toLowerCase().equals(email.toLowerCase()))
/* 1336 */         set.add(info.name); 
/*      */     } 
/* 1338 */     return set.<String>toArray(new String[set.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlayerInfo[] getPlayerInfosForEmail(String email) {
/* 1343 */     Set<PlayerInfo> set = new HashSet<>();
/* 1344 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1346 */       if (info.emailAddress.toLowerCase().equals(email.toLowerCase()))
/* 1347 */         set.add(info); 
/*      */     } 
/* 1349 */     return set.<PlayerInfo>toArray(new PlayerInfo[set.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static PlayerInfo getPlayerSleepingInBed(long bedid) {
/* 1354 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1356 */       if (info.bed == bedid) {
/*      */         
/* 1358 */         if (info.lastLogin <= 0L && info.lastLogout > System.currentTimeMillis() - 86400000L && info.currentServer == Servers.localServer.id)
/*      */         {
/* 1360 */           return info; } 
/* 1361 */         return null;
/*      */       } 
/*      */     } 
/* 1364 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberOfPayingPlayers() {
/* 1369 */     long now = System.currentTimeMillis();
/* 1370 */     int nums = 0;
/*      */     
/*      */     try {
/* 1373 */       for (PlayerInfo info : playerInfos.values()) {
/*      */         
/* 1375 */         if (info.getPower() == 0 && info.paymentExpireDate > now && (info
/* 1376 */           .getCurrentServer() == Servers.localServer.id || Servers.localServer.LOGINSERVER)) {
/* 1377 */           nums++;
/*      */         }
/*      */       } 
/* 1380 */     } catch (Exception ex) {
/*      */       
/* 1382 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/* 1384 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberOfPayingPlayersEver() {
/* 1389 */     int nums = 0;
/*      */     
/*      */     try {
/* 1392 */       for (PlayerInfo info : playerInfos.values()) {
/*      */         
/* 1394 */         if (info.getPower() == 0 && info.paymentExpireDate > 0L && (info
/* 1395 */           .getCurrentServer() == Servers.localServer.id || Servers.localServer.LOGINSERVER)) {
/* 1396 */           nums++;
/*      */         }
/*      */       } 
/* 1399 */     } catch (Exception ex) {
/*      */       
/* 1401 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/* 1403 */     return nums;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String rename(String oldName, String newName, String newPass, int power) throws IOException {
/* 1409 */     logger.log(Level.INFO, "Trying to rename " + oldName + " to " + newName);
/* 1410 */     if (playerInfos.containsKey(oldName)) {
/*      */       
/* 1412 */       PlayerInfo pinf = playerInfos.get(oldName);
/* 1413 */       logger.log(Level.INFO, "Trying to rename " + oldName + " to " + newName + " power=" + power + ", pinf power=" + pinf.power);
/*      */       
/* 1415 */       if (pinf.power < power) {
/*      */         
/* 1417 */         pinf.setName(newName);
/* 1418 */         pinf.updatePassword(newPass);
/* 1419 */         playerInfos.remove(oldName);
/* 1420 */         playerInfos.put(newName, pinf);
/*      */         
/*      */         try {
/* 1423 */           Player p = Players.getInstance().getPlayer(oldName);
/* 1424 */           p.refreshVisible();
/* 1425 */           p.getCommunicator().sendSelfToLocal();
/* 1426 */           p.getCommunicator().sendSafeServerMessage("Your password now is '" + newPass + "'. Please take a note of this.");
/*      */         
/*      */         }
/* 1429 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1435 */           Village[] villages = Villages.getVillages();
/* 1436 */           for (Village lVillage : villages) {
/*      */             
/* 1438 */             if (lVillage.mayorName.equals(oldName)) {
/* 1439 */               lVillage.setMayor(newName);
/*      */             }
/*      */           } 
/* 1442 */         } catch (IOException iox) {
/*      */           
/* 1444 */           logger.log(Level.WARNING, oldName + " failed to change the mayorname to " + newName, iox);
/* 1445 */           return Servers.localServer.name + " failed to change the mayor name from " + oldName + " to " + newName;
/*      */         } 
/*      */         
/* 1448 */         return Servers.localServer.name + " - ok\n";
/*      */       } 
/* 1450 */       return Servers.localServer.name + " you do not have the power to do that.";
/*      */     } 
/*      */     
/* 1453 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String changePassword(String changerName, String name, String newPass, int power) throws IOException {
/* 1459 */     if (playerInfos.containsKey(name)) {
/*      */       
/* 1461 */       PlayerInfo pinf = playerInfos.get(name);
/* 1462 */       if (pinf.power < power || changerName.equals(name)) {
/*      */         
/* 1464 */         pinf.updatePassword(newPass);
/*      */         
/* 1466 */         if (!changerName.equals(name)) {
/*      */           
/*      */           try {
/*      */             
/* 1470 */             Player p = Players.getInstance().getPlayer(name);
/* 1471 */             p.getCommunicator().sendSafeServerMessage("Your password has been changed by " + changerName + " to " + newPass);
/*      */           
/*      */           }
/* 1474 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */ 
/*      */         
/* 1478 */         logger.log(Level.INFO, changerName + " changed the password of " + name + ".");
/* 1479 */         return Servers.localServer.name + " - ok\n";
/*      */       } 
/* 1481 */       return Servers.localServer.name + " you do not have the power to do that.";
/*      */     } 
/* 1483 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean doesEmailExist(String email) {
/* 1488 */     PlayerInfo[] accs = getPlayerInfosForEmail(email);
/* 1489 */     return (accs.length > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean verifyPasswordForEmail(String email, String password, int power) {
/* 1494 */     PlayerInfo[] accs = getPlayerInfosForEmail(email);
/* 1495 */     boolean ok = true;
/* 1496 */     if (accs.length > 0) {
/*      */ 
/*      */       
/* 1499 */       if (accs.length > 4)
/* 1500 */         return false; 
/* 1501 */       ok = false;
/* 1502 */       if (power > 0)
/* 1503 */         ok = true; 
/* 1504 */       for (PlayerInfo lAcc : accs) {
/*      */         
/* 1506 */         if (power == 0 || lAcc.power > power) {
/*      */           
/* 1508 */           ok = false;
/* 1509 */           if (password != null) {
/*      */             
/*      */             try {
/*      */ 
/*      */               
/* 1514 */               if (lAcc.password.equals(LoginHandler.hashPassword(password, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(lAcc.name))))) {
/* 1515 */                 return true;
/*      */               }
/* 1517 */             } catch (Exception exception) {}
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1525 */     return ok;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String changeEmail(String changerName, String name, String newEmail, String password, int power, String pwQuestion, String pwAnswer) throws IOException {
/* 1531 */     if (playerInfos.containsKey(name)) {
/*      */       
/* 1533 */       PlayerInfo pinf = playerInfos.get(name);
/* 1534 */       if (pinf.power < power || changerName.equals(name)) {
/*      */         
/* 1536 */         boolean ok = false;
/*      */ 
/*      */         
/* 1539 */         String retrievalInfo = "";
/* 1540 */         if (pwQuestion != null && pwAnswer != null && changerName.equals(name))
/*      */         {
/* 1542 */           if ((pwQuestion.length() > 3 && !pwQuestion.equals(pinf.pwQuestion)) || (pwAnswer
/* 1543 */             .length() > 2 && !pwAnswer.equals(pinf.pwAnswer))) {
/*      */             
/* 1545 */             pinf.setPassRetrieval(pwQuestion, pwAnswer);
/*      */             
/* 1547 */             retrievalInfo = " Retrieval info updated.";
/*      */           } 
/*      */         }
/* 1550 */         if (doesEmailExist(newEmail)) {
/*      */           
/* 1552 */           if (verifyPasswordForEmail(newEmail, password, power))
/*      */           {
/* 1554 */             ok = true;
/*      */           }
/* 1556 */           logger.log(Level.INFO, "Email exists for " + pinf.name + " " + pinf.password + " " + pinf.emailAddress + " new email:" + newEmail + " verified=" + ok);
/*      */ 
/*      */ 
/*      */         
/*      */         }
/* 1561 */         else if (pinf.power >= power) {
/*      */           
/*      */           try {
/* 1564 */             if (pinf.password.equals(LoginHandler.hashPassword(password, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(pinf.name))))) {
/* 1565 */               ok = true;
/*      */             }
/* 1567 */           } catch (Exception ex) {
/*      */ 
/*      */             
/* 1570 */             logger.log(Level.INFO, "Skipped " + pinf.name + " " + pinf.password + " " + pinf.emailAddress);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1575 */           ok = true;
/*      */         } 
/*      */ 
/*      */         
/* 1579 */         if (!ok)
/*      */         {
/* 1581 */           return "NO" + retrievalInfo;
/*      */         }
/* 1583 */         pinf.setEmailAddress(newEmail);
/*      */         
/* 1585 */         logger.log(Level.INFO, changerName + " changed the email of " + name + " to " + newEmail + "." + retrievalInfo);
/*      */         
/*      */         try {
/* 1588 */           Player p = Players.getInstance().getPlayer(name);
/* 1589 */           p.getCommunicator().sendSafeServerMessage("Your email has been changed by " + changerName + " to " + newEmail + "." + retrievalInfo);
/*      */         
/*      */         }
/* 1592 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */         
/* 1595 */         return Servers.localServer.name + " - ok " + retrievalInfo + "\n";
/*      */       } 
/* 1597 */       return Servers.localServer.name + " you do not have the power to do that.";
/*      */     } 
/* 1599 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void switchFatigue() {
/* 1604 */     for (PlayerInfo info : playerInfos.values())
/*      */     {
/* 1606 */       info.saveSwitchFatigue();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void resetScenarioKarma() {
/* 1613 */     Connection dbcon = null;
/* 1614 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/* 1617 */       dbcon = DbConnector.getPlayerDbCon();
/* 1618 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET SCENARIOKARMA=0");
/*      */       
/* 1620 */       ps.executeUpdate();
/*      */     }
/* 1622 */     catch (SQLException ex) {
/*      */       
/* 1624 */       logger.log(Level.WARNING, "Failed to reset scenario karma");
/*      */     }
/*      */     finally {
/*      */       
/* 1628 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 1629 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/* 1631 */     for (PlayerInfo info : playerInfos.values())
/*      */     {
/* 1633 */       info.scenarioKarma = 0;
/*      */     }
/* 1635 */     for (Player p : Players.getInstance().getPlayers())
/*      */     {
/* 1637 */       p.sendScenarioKarma();
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
/*      */   public static void resetFaithGain() {
/* 1649 */     for (PlayerInfo p : playerInfos.values()) {
/*      */       
/* 1651 */       p.numFaith = 0;
/* 1652 */       p.lastFaith = 0L;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberOfActivePlayersWithDeity(int deityNumber) {
/* 1658 */     int nums = 0;
/* 1659 */     long breakOff = System.currentTimeMillis() - 604800000L;
/* 1660 */     for (PlayerInfo p : playerInfos.values()) {
/*      */       
/* 1662 */       if (p.getDeity() != null && 
/* 1663 */         (p.getDeity()).number == deityNumber && p.getLastLogin() > breakOff) {
/* 1664 */         nums++;
/*      */       }
/*      */     } 
/* 1667 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final PlayerInfo[] getActivePriestsForDeity(int deityNumber) {
/* 1672 */     Set<PlayerInfo> infos = new HashSet<>();
/* 1673 */     long breakOff = System.currentTimeMillis() - 604800000L;
/* 1674 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1676 */       if (info == null) {
/* 1677 */         logger.log(Level.WARNING, "getPlayerInfosWithEmail() NULL in playerInfos.values()??");
/*      */         
/*      */         continue;
/*      */       } 
/*      */       try {
/* 1682 */         info.load();
/* 1683 */         if (info.getDeity() != null && (info.getDeity()).number == deityNumber && info.isPriest && info
/* 1684 */           .getLastLogin() > breakOff)
/*      */         {
/* 1686 */           infos.add(info);
/*      */         }
/*      */       }
/* 1689 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1692 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */     
/* 1696 */     return infos.<PlayerInfo>toArray(new PlayerInfo[infos.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final PlayerInfo[] getActiveFollowersForDeity(int deityNumber) {
/* 1701 */     Set<PlayerInfo> infos = new HashSet<>();
/* 1702 */     long breakOff = System.currentTimeMillis() - 604800000L;
/* 1703 */     for (PlayerInfo info : playerInfos.values()) {
/*      */       
/* 1705 */       if (info == null) {
/* 1706 */         logger.log(Level.WARNING, "getPlayerInfosWithEmail() NULL in playerInfos.values()??");
/*      */         
/*      */         continue;
/*      */       } 
/*      */       try {
/* 1711 */         info.load();
/* 1712 */         if (info.getDeity() != null && (info.getDeity()).number == deityNumber && info
/* 1713 */           .getLastLogin() > breakOff)
/*      */         {
/* 1715 */           infos.add(info);
/*      */         }
/*      */       }
/* 1718 */       catch (IOException e) {
/*      */ 
/*      */         
/* 1721 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       } 
/*      */     } 
/*      */     
/* 1725 */     return infos.<PlayerInfo>toArray(new PlayerInfo[infos.size()]);
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
/*      */   public static final class FatigueSwitcher
/*      */     implements Runnable
/*      */   {
/*      */     public void run() {
/* 1753 */       if (PlayerInfoFactory.logger.isLoggable(Level.FINER))
/*      */       {
/* 1755 */         PlayerInfoFactory.logger.finer("Running newSingleThreadScheduledExecutor for calling PlayerInfoFactory.switchFatigue()");
/*      */       }
/*      */       
/*      */       try {
/* 1759 */         long now = System.nanoTime();
/*      */         
/* 1761 */         PlayerInfoFactory.switchFatigue();
/*      */         
/* 1763 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 1764 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*      */         {
/* 1766 */           PlayerInfoFactory.logger.info("Finished calling PlayerInfoFactory.switchFatigue(), which took " + lElapsedTime + " millis.");
/*      */         
/*      */         }
/*      */       }
/* 1770 */       catch (RuntimeException e) {
/*      */         
/* 1772 */         PlayerInfoFactory.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling PlayerInfoFactory.switchFatigue()", e);
/*      */         
/* 1774 */         throw e;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getVotesForKingdom(byte kingdom) {
/* 1781 */     int nums = 0;
/* 1782 */     PlayerInfo[] pinfs = getPlayerInfos();
/* 1783 */     for (PlayerInfo lPinf : pinfs) {
/*      */       
/* 1785 */       if (lPinf.votedKing)
/*      */       {
/* 1787 */         if (Players.getInstance().getKingdomForPlayer(lPinf.wurmId) == kingdom)
/*      */         {
/* 1789 */           nums++;
/*      */         }
/*      */       }
/*      */     } 
/* 1793 */     return nums;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void resetVotesForKingdom(byte kingdom) {
/* 1798 */     PlayerInfo[] pinfs = getPlayerInfos();
/* 1799 */     for (PlayerInfo lPinf : pinfs) {
/*      */       
/* 1801 */       if (lPinf.votedKing)
/*      */       {
/* 1803 */         if (Players.getInstance().getKingdomForPlayer(lPinf.wurmId) == kingdom)
/*      */         {
/*      */ 
/*      */           
/* 1807 */           lPinf.setVotedKing(false);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final int getNumberOfChamps(int deityNum) {
/* 1815 */     int nums = 0;
/* 1816 */     PlayerInfo[] pinfs = getPlayerInfos();
/* 1817 */     for (PlayerInfo lPinf : pinfs) {
/*      */       
/* 1819 */       if (lPinf.realdeath > 0 && lPinf.realdeath < 4 && 
/* 1820 */         System.currentTimeMillis() - lPinf.championTimeStamp < 14515200000L)
/*      */       {
/* 1822 */         if (lPinf.getDeity() != null && (lPinf.getDeity()).number == deityNum)
/*      */         {
/* 1824 */           nums++;
/*      */         }
/*      */       }
/*      */     } 
/* 1828 */     return nums;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void whosOnline() {
/* 1836 */     grabPlayerStates();
/*      */ 
/*      */     
/* 1839 */     for (PlayerState entry : playerStatus.values()) {
/*      */       
/* 1841 */       if (entry.getState() == PlayerOnlineStatus.ONLINE && entry.getServerId() == Servers.getLocalServerId()) {
/*      */         
/* 1843 */         WcPlayerStatus wps = new WcPlayerStatus(entry);
/* 1844 */         wps.sendToLoginServer();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void grabPlayerStates() {
/* 1851 */     if (playerStatus.size() < 100) {
/*      */       
/*      */       try {
/*      */         
/* 1855 */         Map<Long, byte[]> statusBytes = getPlayerStates(EMPTY_LONG_PRIMITIVE_ARRAY);
/*      */         
/* 1857 */         for (byte[] entry : statusBytes.values()) {
/*      */           
/* 1859 */           PlayerState pState = new PlayerState(entry);
/* 1860 */           playerStatus.put(Long.valueOf(pState.getPlayerId()), pState);
/*      */         } 
/* 1862 */         logger.log(Level.INFO, "Got " + playerStatus.size() + " player status");
/*      */       }
/* 1864 */       catch (RemoteException e) {
/*      */ 
/*      */         
/* 1867 */         logger.log(Level.WARNING, e.getMessage(), e);
/*      */       }
/* 1869 */       catch (WurmServerException e) {
/*      */ 
/*      */         
/* 1872 */         logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void updatePlayerState(Player player, long whenStateChanged, PlayerOnlineStatus aStatus) {
/* 1880 */     PlayerState pState = new PlayerState(player.getWurmId(), player.getName(), whenStateChanged, aStatus);
/*      */     
/* 1882 */     if (playerJustTransfered(pState)) {
/*      */       return;
/*      */     }
/* 1885 */     updatePlayerState(pState);
/*      */ 
/*      */     
/* 1888 */     if (aStatus == PlayerOnlineStatus.ONLINE)
/*      */     {
/* 1890 */       for (Friend f : player.getFriends()) {
/*      */         
/* 1892 */         PlayerState fState = playerStatus.get(Long.valueOf(f.getFriendId()));
/* 1893 */         if (fState != null) {
/* 1894 */           player.getCommunicator().sendFriend(fState, f.getNote());
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1900 */     WcPlayerStatus wps = new WcPlayerStatus(pState);
/* 1901 */     if (Servers.isThisLoginServer()) {
/*      */ 
/*      */       
/* 1904 */       wps.sendFromLoginServer();
/*      */     } else {
/*      */       
/* 1907 */       wps.sendToLoginServer();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void updatePlayerState(PlayerState pState) {
/* 1917 */     if (pState.getState() == PlayerOnlineStatus.UNKNOWN) {
/*      */       return;
/*      */     }
/* 1920 */     statesToUpdate.add(pState);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void handlePlayerStateList() {
/* 1925 */     PlayerState pState = statesToUpdate.pollFirst();
/* 1926 */     while (pState != null) {
/*      */       
/* 1928 */       boolean tellAll = true;
/* 1929 */       PlayerState oldState = playerStatus.get(Long.valueOf(pState.getPlayerId()));
/* 1930 */       if (pState.getState() == PlayerOnlineStatus.DELETE_ME) {
/* 1931 */         playerStatus.remove(Long.valueOf(pState.getPlayerId()));
/*      */       
/*      */       }
/* 1934 */       else if (oldState != null && oldState.getState() == PlayerOnlineStatus.OFFLINE && pState
/* 1935 */         .getState() == PlayerOnlineStatus.LOST_LINK) {
/*      */ 
/*      */ 
/*      */         
/* 1939 */         tellAll = false;
/*      */       } else {
/*      */         
/* 1942 */         playerStatus.put(Long.valueOf(pState.getPlayerId()), pState);
/*      */       } 
/*      */       
/* 1945 */       if (tellAll) {
/*      */ 
/*      */         
/* 1948 */         Players.tellFriends(pState);
/*      */ 
/*      */ 
/*      */         
/* 1952 */         if (oldState == null) {
/* 1953 */           Tickets.playerStateChange(pState);
/* 1954 */         } else if (pState.getState() != oldState.getState()) {
/*      */ 
/*      */ 
/*      */           
/* 1958 */           if (pState.getState() == PlayerOnlineStatus.ONLINE || oldState
/* 1959 */             .getState() == PlayerOnlineStatus.ONLINE)
/* 1960 */             Tickets.playerStateChange(pState); 
/*      */         } 
/* 1962 */       }  pState = statesToUpdate.pollFirst();
/*      */     } 
/*      */ 
/*      */     
/* 1966 */     for (Map.Entry<Long, PlayerState> entry : friendsToUpdate.entrySet()) {
/*      */ 
/*      */       
/*      */       try {
/* 1970 */         long playerWurmId = ((Long)entry.getKey()).longValue();
/* 1971 */         Player player = Players.getInstance().getPlayer(playerWurmId);
/* 1972 */         player.getCommunicator().sendFriend(entry.getValue());
/*      */       }
/* 1974 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1979 */     friendsToUpdate.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean isPlayerOnline(long playerWurmId) {
/* 1984 */     PlayerState pState = playerStatus.get(Long.valueOf(playerWurmId));
/* 1985 */     if (pState != null && pState.getState() == PlayerOnlineStatus.ONLINE)
/* 1986 */       return true; 
/* 1987 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getPlayerName(long playerWurmId) {
/* 1992 */     PlayerState pState = playerStatus.get(Long.valueOf(playerWurmId));
/* 1993 */     if (pState != null)
/* 1994 */       return pState.getPlayerName(); 
/* 1995 */     return "Unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long getWurmId(String name) {
/* 2006 */     for (PlayerState pState : playerStatus.values()) {
/*      */       
/* 2008 */       if (pState.getPlayerName().equalsIgnoreCase(name))
/*      */       {
/* 2010 */         return pState.getPlayerId();
/*      */       }
/*      */     } 
/* 2013 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final PlayerState getPlayerState(String name) {
/* 2024 */     for (PlayerState pState : playerStatus.values()) {
/*      */       
/* 2026 */       if (pState.getPlayerName().equalsIgnoreCase(name))
/*      */       {
/* 2028 */         return pState;
/*      */       }
/*      */     } 
/* 2031 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void setPlayerStatesToOffline(int serverId) {
/* 2042 */     for (PlayerState pState : playerStatus.values()) {
/*      */       
/* 2044 */       if (pState.getServerId() == serverId && pState.getState() != PlayerOnlineStatus.OFFLINE) {
/*      */ 
/*      */         
/* 2047 */         PlayerState newState = new PlayerState(pState.getServerId(), pState.getPlayerId(), pState.getPlayerName(), System.currentTimeMillis(), PlayerOnlineStatus.OFFLINE);
/*      */         
/* 2049 */         updatePlayerState(newState);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] getPlayersOnCurrentServer() {
/* 2056 */     Set<Long> pIds = new HashSet<>();
/* 2057 */     for (PlayerState pState : playerStatus.values()) {
/*      */       
/* 2059 */       if (pState.getServerId() == Servers.getLocalServerId())
/*      */       {
/* 2061 */         pIds.add(Long.valueOf(pState.getPlayerId()));
/*      */       }
/*      */     } 
/* 2064 */     long[] ans = new long[pIds.size()];
/* 2065 */     int x = 0;
/* 2066 */     for (Long pId : pIds)
/* 2067 */       ans[x++] = pId.longValue(); 
/* 2068 */     return ans;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static long breakFriendship(String playerName, long playerWurmId, String friendName) {
/* 2074 */     for (PlayerState fState : playerStatus.values()) {
/*      */       
/* 2076 */       if (fState.getPlayerName().equalsIgnoreCase(friendName)) {
/*      */         
/* 2078 */         long friendWurmId = fState.getPlayerId();
/* 2079 */         breakFriendship(playerName, playerWurmId, friendName, friendWurmId);
/* 2080 */         return friendWurmId;
/*      */       } 
/*      */     } 
/*      */     
/* 2084 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void breakFriendship(String playerName, long playerWurmId, String friendName, long friendWurmId) {
/* 2094 */     breakFriendship(playerWurmId, friendWurmId, friendName);
/* 2095 */     breakFriendship(friendWurmId, playerWurmId, playerName);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void breakFriendship(long playerWurmId, long friendWurmId, String friendName) {
/* 2100 */     PlayerInfo pInfo = getPlayerInfoWithWurmId(playerWurmId);
/* 2101 */     if (pInfo != null) {
/*      */       
/* 2103 */       pInfo.removeFriend(friendWurmId);
/* 2104 */       PlayerState pState = new PlayerState(friendWurmId, friendName, -1L, PlayerOnlineStatus.DELETE_ME);
/* 2105 */       friendsToUpdate.put(Long.valueOf(playerWurmId), pState);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final WurmRecord getChampionRecord(String name) {
/* 2111 */     for (WurmRecord record : championRecords) {
/*      */       
/* 2113 */       if (record.getHolder().toLowerCase().equals(name.toLowerCase()) && record.isCurrent())
/* 2114 */         return record; 
/*      */     } 
/* 2116 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addChampRecord(WurmRecord record) {
/* 2121 */     championRecords.add(record);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final WurmRecord[] getChampionRecords() {
/* 2126 */     return championRecords.<WurmRecord>toArray(new WurmRecord[championRecords.size()]);
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
/*      */   public static void expelMember(long playerId, byte fromKingdomId, byte toKingdomId, int originServer) {
/* 2146 */     boolean isOnline = true;
/* 2147 */     ServerEntry server = Servers.getServerWithId(originServer);
/* 2148 */     if (server == null) {
/*      */       
/* 2150 */       logger.warning("ExpelMember request from invalid server ID " + originServer + " for playerID " + playerId);
/*      */       return;
/*      */     } 
/* 2153 */     Player p = Players.getInstance().getPlayerOrNull(playerId);
/* 2154 */     if (p == null) {
/*      */       
/* 2156 */       PlayerInfo pInfo = playerInfosWurmId.get(Long.valueOf(playerId));
/* 2157 */       if (pInfo == null)
/*      */         return; 
/* 2159 */       isOnline = false;
/*      */       
/*      */       try {
/* 2162 */         pInfo.load();
/* 2163 */         p = new Player(pInfo);
/*      */       }
/* 2165 */       catch (Exception ex) {
/*      */         
/* 2167 */         logger.log(Level.WARNING, "Unable to complete expel command for: " + playerId, ex);
/*      */         return;
/*      */       } 
/*      */     } 
/* 2171 */     logger.info("Expelling " + p.getName() + " from " + Kingdoms.getNameFor(fromKingdomId) + " on " + server
/* 2172 */         .getName() + ", new kingdom: " + Kingdoms.getNameFor(toKingdomId));
/*      */     
/* 2174 */     if (server.EPIC && Servers.localServer.EPIC) {
/*      */ 
/*      */       
/*      */       try {
/* 2178 */         if (!p.setKingdomId(toKingdomId, false, false, isOnline)) {
/*      */           
/* 2180 */           logger.log(Level.WARNING, "Unable to complete expel command for: " + p.getName());
/*      */           
/*      */           return;
/*      */         } 
/* 2184 */       } catch (IOException iox) {
/*      */         
/* 2186 */         logger.log(Level.WARNING, "Unable to complete expel command for: " + p.getName(), iox);
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/* 2192 */     } else if (server.EPIC && !Server.getInstance().isPS()) {
/* 2193 */       p.getSaveFile().setEpicLocation(toKingdomId, (p.getSaveFile()).epicServerId);
/* 2194 */     } else if (server.PVPSERVER || server.isChaosServer()) {
/* 2195 */       p.getSaveFile().setChaosKingdom(toKingdomId);
/*      */     } 
/* 2197 */     if (isOnline)
/*      */     {
/* 2199 */       p.getCommunicator().sendAlertServerMessage("You have been expelled from " + 
/* 2200 */           Kingdoms.getNameFor(fromKingdomId) + " on " + server.getName() + "!");
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerInfoFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */