/*      */ package com.wurmonline.server.webinterface;
/*      */ 
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.GeneralUtilities;
/*      */ import com.wurmonline.server.HistoryManager;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.Mailer;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.banks.Bank;
/*      */ import com.wurmonline.server.banks.BankSlot;
/*      */ import com.wurmonline.server.banks.BankUnavailableException;
/*      */ import com.wurmonline.server.banks.Banks;
/*      */ import com.wurmonline.server.behaviours.Vehicles;
/*      */ import com.wurmonline.server.bodys.Body;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.deities.Deity;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.epic.EpicEntity;
/*      */ import com.wurmonline.server.epic.HexMap;
/*      */ import com.wurmonline.server.epic.MapHex;
/*      */ import com.wurmonline.server.intra.IntraServerConnection;
/*      */ import com.wurmonline.server.intra.MoneyTransfer;
/*      */ import com.wurmonline.server.intra.MountTransfer;
/*      */ import com.wurmonline.server.intra.PasswordTransfer;
/*      */ import com.wurmonline.server.intra.TimeTransfer;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemMetaData;
/*      */ import com.wurmonline.server.items.WurmMail;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Ban;
/*      */ import com.wurmonline.server.players.PendingAccount;
/*      */ import com.wurmonline.server.players.PendingAward;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.Reimbursement;
/*      */ import com.wurmonline.server.questions.AscensionQuestion;
/*      */ import com.wurmonline.server.questions.NewsInfo;
/*      */ import com.wurmonline.server.questions.WurmInfo;
/*      */ import com.wurmonline.server.questions.WurmInfo2;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.skills.SkillStat;
/*      */ import com.wurmonline.server.skills.SkillSystem;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.skills.SkillsFactory;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.tutorial.Mission;
/*      */ import com.wurmonline.server.tutorial.MissionPerformed;
/*      */ import com.wurmonline.server.tutorial.MissionPerformer;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.VolaTile;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.CounterTypes;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigInteger;
/*      */ import java.net.InetAddress;
/*      */ import java.net.URLEncoder;
/*      */ import java.net.UnknownHostException;
/*      */ import java.rmi.AccessException;
/*      */ import java.rmi.RemoteException;
/*      */ import java.rmi.server.ServerNotActiveException;
/*      */ import java.rmi.server.UnicastRemoteObject;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
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
/*      */ public final class WebInterfaceImpl
/*      */   extends UnicastRemoteObject
/*      */   implements WebInterface, Serializable, MiscConstants, TimeConstants, CounterTypes, MonetaryConstants
/*      */ {
/*      */   public static final String VERSION = "$Revision: 1.54 $";
/*  133 */   public static String mailAccount = "mail@mydomain.com";
/*      */   
/*  135 */   public static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[\\w\\.\\+-=]+@[\\w\\.-]+\\.[\\w-]+$");
/*      */   
/*      */   private static final String PASSWORD_CHARS = "abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789";
/*      */   
/*      */   private static final long serialVersionUID = -2682536434841429586L;
/*      */   
/*      */   private final boolean isRunning = true;
/*      */   
/*  143 */   private final Random faceRandom = new Random();
/*      */   private static final long faceRandomSeed = 8263186381637L;
/*  145 */   private static final DecimalFormat twoDecimals = new DecimalFormat("##0.00");
/*      */   
/*  147 */   private static final Set<String> moneyDetails = new HashSet<>();
/*      */   
/*  149 */   private static final Set<String> timeDetails = new HashSet<>();
/*      */ 
/*      */   
/*  152 */   private static final Logger logger = Logger.getLogger(WebInterfaceImpl.class.getName());
/*      */   
/*  154 */   private static final long[] noInfoLong = new long[] { -1L, -1L };
/*      */ 
/*      */   
/*      */   private static final String BAD_PASSWORD = "Access denied.";
/*      */   
/*  159 */   private final SimpleDateFormat alloformatter = new SimpleDateFormat("yy.MM.dd'-'hh:mm:ss");
/*      */   
/*  161 */   private String hostname = "localhost";
/*      */   
/*  163 */   private static final Map<String, Long> ipAttempts = new HashMap<>();
/*  164 */   private String[] bannedMailHosts = new String[] { "sharklasers", "spam4", "grr.la", "guerrillamail" };
/*      */ 
/*      */ 
/*      */   
/*      */   public WebInterfaceImpl(int port) throws RemoteException {
/*  169 */     super(port);
/*      */     
/*      */     try {
/*  172 */       InetAddress localMachine = InetAddress.getLocalHost();
/*  173 */       this.hostname = localMachine.getHostName();
/*  174 */       logger.info("Hostname of local machine used to send registration emails: " + this.hostname);
/*      */     }
/*  176 */     catch (UnknownHostException uhe) {
/*      */       
/*  178 */       throw new RemoteException("Could not find localhost for WebInterface", uhe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WebInterfaceImpl() throws RemoteException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getRemoteClientDetails() {
/*      */     try {
/*  198 */       return getClientHost();
/*      */     }
/*  200 */     catch (ServerNotActiveException e) {
/*      */       
/*  202 */       logger.log(Level.WARNING, "Could not get ClientHost details due to " + e.getMessage(), e);
/*  203 */       return "Unknown Remote Client";
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
/*      */   public int getPower(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  217 */     validateIntraServerPassword(intraServerPassword);
/*  218 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  220 */       logger.finer(getRemoteClientDetails() + " getPower for playerID: " + aPlayerID);
/*      */     }
/*      */     
/*      */     try {
/*  224 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(Players.getInstance().getNameFor(aPlayerID));
/*      */       
/*  226 */       p.load();
/*  227 */       return p.getPower();
/*      */     }
/*  229 */     catch (IOException iox) {
/*      */       
/*  231 */       logger.log(Level.WARNING, aPlayerID + ": " + iox.getMessage(), iox);
/*  232 */       return 0;
/*      */     }
/*  234 */     catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */ 
/*      */       
/*  238 */       return 0;
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
/*      */   public boolean isRunning(String intraServerPassword) throws RemoteException {
/*  252 */     validateIntraServerPassword(intraServerPassword);
/*  253 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  255 */       logger.finer(getRemoteClientDetails() + " isRunning");
/*      */     }
/*  257 */     return true;
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
/*      */   public int getPlayerCount(String intraServerPassword) throws RemoteException {
/*  271 */     validateIntraServerPassword(intraServerPassword);
/*  272 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  274 */       logger.finer(getRemoteClientDetails() + " getPlayerCount");
/*      */     }
/*  276 */     return Players.getInstance().numberOfPlayers();
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
/*      */   public int getPremiumPlayerCount(String intraServerPassword) throws RemoteException {
/*  290 */     validateIntraServerPassword(intraServerPassword);
/*  291 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  293 */       logger.finer(getRemoteClientDetails() + " getPremiumPlayerCount");
/*      */     }
/*  295 */     return Players.getInstance().numberOfPremiumPlayers();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTestMessage(String intraServerPassword) throws RemoteException {
/*  305 */     validateIntraServerPassword(intraServerPassword);
/*  306 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  308 */       logger.finer(getRemoteClientDetails() + " getTestMessage");
/*      */     }
/*  310 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  312 */       return "HEj! " + System.currentTimeMillis();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void broadcastMessage(String intraServerPassword, String message) throws RemoteException {
/*  323 */     validateIntraServerPassword(intraServerPassword);
/*  324 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  326 */       logger.finer(getRemoteClientDetails() + " broadcastMessage: " + message);
/*      */     }
/*  328 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  330 */       Server.getInstance().broadCastAlert(message);
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
/*      */   public long getAccountStatusForPlayer(String intraServerPassword, String playerName) throws RemoteException {
/*  343 */     validateIntraServerPassword(intraServerPassword);
/*  344 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  346 */       logger.finer(getRemoteClientDetails() + " getAccountStatusForPlayer for player: " + playerName);
/*      */     }
/*  348 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  350 */       if (Servers.localServer.id != Servers.loginServer.id)
/*      */       {
/*  352 */         throw new RemoteException("Not a valid request for this server. Ask the login server instead.");
/*      */       }
/*  354 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/*  357 */         p.load();
/*  358 */         return p.money;
/*      */       }
/*  360 */       catch (IOException iox) {
/*      */         
/*  362 */         logger.log(Level.WARNING, playerName + ": " + iox.getMessage(), iox);
/*  363 */         return 0L;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Integer> getBattleRanks(String intraServerPassword, int numberOfRanksToGet) throws RemoteException {
/*  381 */     validateIntraServerPassword(intraServerPassword);
/*  382 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  384 */       logger.finer(getRemoteClientDetails() + " getBattleRanks number of Ranks: " + numberOfRanksToGet);
/*      */     }
/*  386 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  388 */       return Players.getBattleRanks(numberOfRanksToGet);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServerStatus(String intraServerPassword) throws RemoteException {
/*  411 */     validateIntraServerPassword(intraServerPassword);
/*  412 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  414 */       logger.finer(getRemoteClientDetails() + " getServerStatus");
/*      */     }
/*  416 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  418 */       String toReturn = "Up and running.";
/*  419 */       if (Server.getMillisToShutDown() > -1000L) {
/*      */ 
/*      */         
/*  422 */         toReturn = "Shutting down in " + (Server.getMillisToShutDown() / 1000L) + " seconds: " + Server.getShutdownReason();
/*      */       }
/*  424 */       else if (Constants.maintaining) {
/*      */         
/*  426 */         toReturn = "The server is in maintenance mode and not open for connections.";
/*      */       } 
/*  428 */       return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Long> getFriends(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  446 */     validateIntraServerPassword(intraServerPassword);
/*  447 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  449 */       logger.finer(getRemoteClientDetails() + " getFriends for playerid: " + aPlayerID);
/*      */     }
/*  451 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  453 */       return Players.getFriends(aPlayerID);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> getInventory(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  473 */     validateIntraServerPassword(intraServerPassword);
/*  474 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  476 */       logger.finer(getRemoteClientDetails() + " getInventory for playerid: " + aPlayerID);
/*      */     }
/*  478 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  480 */       Map<String, String> toReturn = new HashMap<>();
/*      */       
/*      */       try {
/*  483 */         Player p = Players.getInstance().getPlayer(aPlayerID);
/*  484 */         Item inventory = p.getInventory();
/*  485 */         Item[] items = inventory.getAllItems(false);
/*  486 */         for (int x = 0; x < items.length; x++)
/*      */         {
/*  488 */           toReturn.put(String.valueOf(items[x].getWurmId()), items[x]
/*  489 */               .getName() + ", QL: " + items[x].getQualityLevel() + ", DAM: " + items[x].getDamage());
/*      */         }
/*      */       }
/*  492 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */       
/*  496 */       return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Long, Long> getBodyItems(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  517 */     validateIntraServerPassword(intraServerPassword);
/*  518 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  520 */       logger.finer(getRemoteClientDetails() + " getBodyItems for playerid: " + aPlayerID);
/*      */     }
/*  522 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  524 */       Map<Long, Long> toReturn = new HashMap<>();
/*      */       
/*      */       try {
/*  527 */         Player p = Players.getInstance().getPlayer(aPlayerID);
/*  528 */         Body lBody = p.getBody();
/*  529 */         if (lBody != null)
/*      */         {
/*  531 */           Item[] items = lBody.getAllItems();
/*  532 */           for (int x = 0; x < items.length; x++)
/*      */           {
/*  534 */             toReturn.put(Long.valueOf(items[x].getWurmId()), Long.valueOf(items[x].getParentId()));
/*      */           }
/*      */         }
/*      */       
/*  538 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */       
/*  542 */       return toReturn;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Float> getSkills(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  559 */     validateIntraServerPassword(intraServerPassword);
/*  560 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  562 */       logger.finer(getRemoteClientDetails() + " getSkills for playerid: " + aPlayerID);
/*      */     }
/*  564 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  566 */       Map<String, Float> toReturn = new HashMap<>();
/*  567 */       Skills skills = SkillsFactory.createSkills(aPlayerID);
/*      */       
/*      */       try {
/*  570 */         skills.load();
/*  571 */         Skill[] skillarr = skills.getSkills();
/*  572 */         for (int x = 0; x < skillarr.length; x++)
/*      */         {
/*  574 */           toReturn.put(skillarr[x].getName(), new Float(skillarr[x].getKnowledge(0.0D)));
/*      */         }
/*      */       }
/*  577 */       catch (Exception iox) {
/*      */         
/*  579 */         logger.log(Level.WARNING, aPlayerID + ": " + iox.getMessage(), iox);
/*      */       } 
/*  581 */       return toReturn;
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
/*      */   public Map<String, ?> getPlayerSummary(String intraServerPassword, long aPlayerID) throws RemoteException {
/*  624 */     validateIntraServerPassword(intraServerPassword);
/*  625 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  627 */       logger.finer(getRemoteClientDetails() + " getPlayerSummary for playerid: " + aPlayerID);
/*      */     }
/*  629 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  631 */       Map<String, Object> toReturn = new HashMap<>();
/*  632 */       if (WurmId.getType(aPlayerID) == 0) {
/*      */ 
/*      */         
/*      */         try {
/*  636 */           Player p = Players.getInstance().getPlayer(aPlayerID);
/*  637 */           toReturn.put("Name", p.getName());
/*  638 */           if (p.citizenVillage != null) {
/*      */             
/*  640 */             Citizen citiz = p.citizenVillage.getCitizen(aPlayerID);
/*  641 */             toReturn.put("CitizenVillage", p.citizenVillage.getName());
/*  642 */             toReturn.put("CitizenRole", citiz.getRole().getName());
/*      */           } 
/*  644 */           String location = "unknown";
/*  645 */           if (p.currentVillage != null) {
/*  646 */             location = p.currentVillage.getName() + ", in " + Kingdoms.getNameFor(p.currentVillage.kingdom);
/*  647 */           } else if (p.currentKingdom != 0) {
/*  648 */             location = Kingdoms.getNameFor(p.currentKingdom);
/*  649 */           }  toReturn.put("Location", location);
/*  650 */           if (p.getDeity() != null)
/*  651 */             toReturn.put("Deity", (p.getDeity()).name); 
/*  652 */           toReturn.put("Faith", new Float(p.getFaith()));
/*  653 */           toReturn.put("Favor", new Float(p.getFavor()));
/*  654 */           toReturn.put("Gender", Byte.valueOf(p.getSex()));
/*  655 */           toReturn.put("Alignment", new Float(p.getAlignment()));
/*  656 */           toReturn.put("Kingdom", Byte.valueOf(p.getKingdomId()));
/*  657 */           toReturn.put("Battle rank", Integer.valueOf(p.getRank()));
/*  658 */           toReturn.put("WurmId", new Long(aPlayerID));
/*      */           
/*  660 */           toReturn.put("Banned", Boolean.valueOf(p.getSaveFile().isBanned()));
/*  661 */           toReturn.put("Money in bank", Long.valueOf(p.getMoney()));
/*  662 */           toReturn.put("Payment", new Date(p.getPaymentExpire()));
/*  663 */           toReturn.put("Email", (p.getSaveFile()).emailAddress);
/*  664 */           toReturn.put("Current server", Integer.valueOf(Servers.localServer.id));
/*  665 */           toReturn.put("Last login", new Date(p.getLastLogin()));
/*  666 */           toReturn.put("Last logout", new Date(Players.getInstance().getLastLogoutForPlayer(aPlayerID)));
/*  667 */           if (p.getSaveFile().isBanned()) {
/*      */             
/*  669 */             toReturn.put("IPBan reason", (p.getSaveFile()).banreason);
/*  670 */             toReturn.put("IPBan expires in", 
/*  671 */                 Server.getTimeFor((p.getSaveFile()).banexpiry - System.currentTimeMillis()));
/*      */           } 
/*  673 */           toReturn.put("Warnings", String.valueOf(p.getSaveFile().getWarnings()));
/*  674 */           if (p.isMute()) {
/*      */             
/*  676 */             toReturn.put("Muted", Boolean.TRUE);
/*  677 */             toReturn.put("Mute reason", (p.getSaveFile()).mutereason);
/*  678 */             toReturn.put("Mute expires in", 
/*  679 */                 Server.getTimeFor((p.getSaveFile()).muteexpiry - System.currentTimeMillis()));
/*      */           } 
/*  681 */           toReturn.put("PlayingTime", Server.getTimeFor((p.getSaveFile()).playingTime));
/*  682 */           toReturn.put("Reputation", Integer.valueOf(p.getReputation()));
/*  683 */           if (p.getTitle() != null || (Features.Feature.COMPOUND_TITLES.isEnabled() && p.getSecondTitle() != null))
/*      */           {
/*  685 */             toReturn.put("Title", p.getTitleString());
/*      */           }
/*  687 */           toReturn.put("Coord x", Integer.valueOf((int)p.getStatus().getPositionX() >> 2));
/*  688 */           toReturn.put("Coord y", Integer.valueOf((int)p.getStatus().getPositionY() >> 2));
/*  689 */           if (p.isPriest())
/*      */           {
/*  691 */             toReturn.put("Priest", Boolean.TRUE);
/*      */           }
/*  693 */           toReturn.put("LoggedOut", Boolean.valueOf(p.loggedout));
/*      */         }
/*  695 */         catch (NoSuchPlayerException nsp) {
/*      */ 
/*      */           
/*      */           try {
/*  699 */             PlayerInfo p = PlayerInfoFactory.createPlayerInfo(Players.getInstance().getNameFor(aPlayerID));
/*  700 */             p.load();
/*  701 */             toReturn.put("Name", p.getName());
/*  702 */             if (p.getDeity() != null)
/*  703 */               toReturn.put("Deity", (p.getDeity()).name); 
/*  704 */             toReturn.put("Faith", new Float(p.getFaith()));
/*  705 */             toReturn.put("Favor", new Float(p.getFavor()));
/*  706 */             toReturn.put("Current server", Integer.valueOf(p.currentServer));
/*  707 */             toReturn.put("Alignment", new Float(p.getAlignment()));
/*      */             
/*  709 */             toReturn.put("Battle rank", Integer.valueOf(p.getRank()));
/*  710 */             toReturn.put("WurmId", new Long(aPlayerID));
/*  711 */             toReturn.put("Banned", Boolean.valueOf(p.isBanned()));
/*  712 */             toReturn.put("Money in bank", new Long(p.money));
/*  713 */             toReturn.put("Payment", new Date(p.getPaymentExpire()));
/*  714 */             toReturn.put("Email", p.emailAddress);
/*  715 */             toReturn.put("Last login", new Date(p.getLastLogin()));
/*  716 */             toReturn.put("Last logout", new Date(Players.getInstance().getLastLogoutForPlayer(aPlayerID)));
/*  717 */             if (p.isBanned()) {
/*      */               
/*  719 */               toReturn.put("IPBan reason", p.banreason);
/*  720 */               toReturn.put("IPBan expires in", Server.getTimeFor(p.banexpiry - System.currentTimeMillis()));
/*      */             } 
/*  722 */             toReturn.put("Warnings", String.valueOf(p.getWarnings()));
/*  723 */             if (p.isMute()) {
/*      */               
/*  725 */               toReturn.put("Muted", Boolean.TRUE);
/*  726 */               toReturn.put("Mute reason", p.mutereason);
/*  727 */               toReturn.put("Mute expires in", Server.getTimeFor(p.muteexpiry - System.currentTimeMillis()));
/*      */             } 
/*  729 */             toReturn.put("PlayingTime", Server.getTimeFor(p.playingTime));
/*  730 */             toReturn.put("Reputation", Integer.valueOf(p.getReputation()));
/*  731 */             if (p.title != null && p.title.getName(true) != null)
/*      */             {
/*  733 */               toReturn.put("Title", p.title.getName(true));
/*      */             }
/*  735 */             if (p.isPriest)
/*      */             {
/*  737 */               toReturn.put("Priest", Boolean.TRUE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*  747 */           catch (IOException iox) {
/*      */             
/*  749 */             logger.log(Level.WARNING, aPlayerID + ":" + iox.getMessage(), iox);
/*      */           }
/*  751 */           catch (NoSuchPlayerException nsp2) {
/*      */             
/*  753 */             logger.log(Level.WARNING, aPlayerID + ":" + nsp2.getMessage(), (Throwable)nsp2);
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  758 */         toReturn.put("Not a player", String.valueOf(aPlayerID));
/*  759 */       }  return toReturn;
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
/*      */ 
/*      */   
/*      */   public long getLocalCreationTime(String intraServerPassword) throws RemoteException {
/*  775 */     validateIntraServerPassword(intraServerPassword);
/*  776 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  778 */       logger.finer(getRemoteClientDetails() + " getLocalCreationTime");
/*      */     }
/*  780 */     return Server.getStartTime();
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
/*      */   public Map<Integer, String> getKingdoms(String intraServerPassword) throws RemoteException {
/*  795 */     validateIntraServerPassword(intraServerPassword);
/*  796 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  798 */       logger.finer(getRemoteClientDetails() + " getKingdoms");
/*      */     }
/*  800 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  802 */       Map<Integer, String> toReturn = new HashMap<>();
/*  803 */       if (Servers.localServer.HOMESERVER) {
/*      */         
/*  805 */         toReturn.put(Integer.valueOf(Servers.localServer.KINGDOM), Kingdoms.getNameFor(Servers.localServer.KINGDOM));
/*      */       }
/*      */       else {
/*      */         
/*  809 */         toReturn.put(Integer.valueOf(1), Kingdoms.getNameFor((byte)1));
/*  810 */         toReturn.put(Integer.valueOf(3), Kingdoms.getNameFor((byte)3));
/*  811 */         toReturn.put(Integer.valueOf(2), 
/*  812 */             Kingdoms.getNameFor((byte)2));
/*      */       } 
/*  814 */       return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Long, String> getPlayersForKingdom(String intraServerPassword, int aKingdom) throws RemoteException {
/*  834 */     validateIntraServerPassword(intraServerPassword);
/*  835 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  837 */       logger.finer(getRemoteClientDetails() + " getPlayersForKingdom: " + aKingdom);
/*      */     }
/*  839 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  841 */       Map<Long, String> toReturn = new HashMap<>();
/*  842 */       Player[] players = Players.getInstance().getPlayers();
/*  843 */       for (int x = 0; x < players.length; x++) {
/*      */         
/*  845 */         if (players[x].getKingdomId() == aKingdom)
/*  846 */           toReturn.put(new Long(players[x].getWurmId()), players[x].getName()); 
/*      */       } 
/*  848 */       return toReturn;
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
/*      */   public long getPlayerId(String intraServerPassword, String name) throws RemoteException {
/*  862 */     validateIntraServerPassword(intraServerPassword);
/*  863 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  865 */       logger.finer(getRemoteClientDetails() + " getPlayerId for player name: " + name);
/*      */     }
/*  867 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  869 */       return Players.getInstance().getWurmIdByPlayerName(LoginHandler.raiseFirstLetter(name));
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
/*      */   public Map<String, ?> createPlayer(String intraServerPassword, String name, String password, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender) throws RemoteException {
/*  900 */     validateIntraServerPassword(intraServerPassword);
/*  901 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  903 */       logger.finer(getRemoteClientDetails() + " createPlayer for player name: " + name);
/*      */     }
/*  905 */     appearance = Server.rand.nextInt(5);
/*      */     
/*  907 */     this.faceRandom.setSeed(8263186381637L + appearance);
/*  908 */     appearance = this.faceRandom.nextLong();
/*      */     
/*  910 */     Map<String, Object> toReturn = new HashMap<>();
/*  911 */     logger.log(Level.INFO, "Trying to create player " + name);
/*  912 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  914 */       if (isEmailValid(emailAddress)) {
/*      */ 
/*      */         
/*      */         try {
/*  918 */           toReturn.put("PlayerId", new Long(
/*      */                 
/*  920 */                 LoginHandler.createPlayer(name, password, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender)));
/*      */         
/*      */         }
/*  923 */         catch (Exception ex) {
/*      */           
/*  925 */           toReturn.put("PlayerId", Long.valueOf(-1L));
/*  926 */           toReturn.put("error", ex.getMessage());
/*  927 */           logger.log(Level.WARNING, name + ":" + ex.getMessage(), ex);
/*      */         } 
/*      */       } else {
/*      */         
/*  931 */         toReturn.put("error", "The email address " + emailAddress + " is not valid.");
/*      */       } 
/*  933 */     }  return toReturn;
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
/*      */   public Map<String, String> getPendingAccounts(String intraServerPassword) throws RemoteException {
/*  946 */     validateIntraServerPassword(intraServerPassword);
/*  947 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  949 */       logger.finer(getRemoteClientDetails() + " getPendingAccounts");
/*      */     }
/*  951 */     Map<String, String> toReturn = new HashMap<>();
/*  952 */     for (Iterator<Map.Entry<String, PendingAccount>> it = PendingAccount.accounts.entrySet().iterator(); it.hasNext(); ) {
/*      */       
/*  954 */       Map.Entry<String, PendingAccount> entry = it.next();
/*  955 */       toReturn.put(entry.getKey(), ((PendingAccount)entry
/*  956 */           .getValue()).emailAddress + ", " + GeneralUtilities.toGMTString(((PendingAccount)entry.getValue()).expiration));
/*      */     } 
/*  958 */     return toReturn;
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
/*      */   public Map<String, String> createPlayerPhaseOne(String intraServerPassword, String aPlayerName, String aEmailAddress) throws RemoteException {
/*  974 */     validateIntraServerPassword(intraServerPassword);
/*  975 */     Map<String, String> toReturn = new HashMap<>();
/*  976 */     if (Constants.maintaining) {
/*      */       
/*  978 */       toReturn.put("error", "The server is currently in maintenance mode.");
/*  979 */       return toReturn;
/*      */     } 
/*  981 */     logger.log(Level.INFO, getRemoteClientDetails() + " Trying to create player phase one " + aPlayerName);
/*  982 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/*  984 */       aPlayerName = LoginHandler.raiseFirstLetter(aPlayerName);
/*  985 */       String errstat = LoginHandler.checkName2(aPlayerName);
/*  986 */       if (errstat.length() == 0) {
/*      */         
/*  988 */         if (PlayerInfoFactory.doesPlayerExist(aPlayerName)) {
/*      */           
/*  990 */           toReturn.put("error", "The name " + aPlayerName + " is taken.");
/*  991 */           return toReturn;
/*      */         } 
/*  993 */         if (PendingAccount.doesPlayerExist(aPlayerName)) {
/*      */           
/*  995 */           toReturn.put("error", "The name " + aPlayerName + " is reserved for up to two days.");
/*  996 */           return toReturn;
/*      */         } 
/*  998 */         if (!isEmailValid(aEmailAddress)) {
/*      */           
/* 1000 */           toReturn.put("error", "The email " + aEmailAddress + " is invalid.");
/* 1001 */           return toReturn;
/*      */         } 
/* 1003 */         String[] numAccounts = PlayerInfoFactory.getAccountsForEmail(aEmailAddress);
/* 1004 */         if (numAccounts.length >= 5) {
/*      */           
/* 1006 */           String accnames = "";
/* 1007 */           for (int x = 0; x < numAccounts.length; x++)
/* 1008 */             accnames = accnames + " " + numAccounts[x]; 
/* 1009 */           toReturn.put("error", "You may only have 5 accounts. Please play Wurm with any of the following:" + accnames + ".");
/*      */           
/* 1011 */           return toReturn;
/*      */         } 
/* 1013 */         String[] numAccounts2 = PendingAccount.getAccountsForEmail(aEmailAddress);
/* 1014 */         if (numAccounts2.length >= 5) {
/*      */           
/* 1016 */           String accnames = "";
/* 1017 */           for (int x = 0; x < numAccounts2.length; x++)
/* 1018 */             accnames = accnames + " " + numAccounts2[x]; 
/* 1019 */           toReturn.put("error", "You may only have 5 accounts. The following accounts are awaiting confirmation by following the link in the verification email:" + accnames + ".");
/*      */ 
/*      */ 
/*      */           
/* 1023 */           return toReturn;
/*      */         } 
/* 1025 */         for (String blocked : this.bannedMailHosts) {
/*      */           
/* 1027 */           if (aEmailAddress.toLowerCase().contains(blocked)) {
/*      */             
/* 1029 */             String domain = aEmailAddress.substring(aEmailAddress.indexOf("@"), aEmailAddress.length());
/* 1030 */             toReturn.put("error", "We do not accept email addresses from :" + domain + ".");
/*      */             
/* 1032 */             return toReturn;
/*      */           } 
/*      */         } 
/* 1035 */         if (numAccounts.length + numAccounts2.length >= 5) {
/*      */           
/* 1037 */           String accnames = ""; int x;
/* 1038 */           for (x = 0; x < numAccounts.length; x++)
/* 1039 */             accnames = accnames + " " + numAccounts[x]; 
/* 1040 */           for (x = 0; x < numAccounts2.length; x++)
/* 1041 */             accnames = accnames + " " + numAccounts2[x]; 
/* 1042 */           toReturn.put("error", "You may only have 5 accounts. The following accounts are already registered or awaiting confirmation by following the link in the verification email:" + accnames + ".");
/*      */ 
/*      */ 
/*      */           
/* 1046 */           return toReturn;
/*      */         } 
/* 1048 */         String password = generateRandomPassword();
/*      */         
/* 1050 */         long expireTime = System.currentTimeMillis() + 172800000L;
/* 1051 */         PendingAccount pedd = new PendingAccount();
/* 1052 */         pedd.accountName = aPlayerName;
/* 1053 */         pedd.emailAddress = aEmailAddress;
/* 1054 */         pedd.expiration = expireTime;
/* 1055 */         pedd.password = password;
/* 1056 */         if (pedd.create()) {
/*      */           
/*      */           try
/*      */           {
/* 1060 */             if (!Constants.devmode) {
/*      */               
/* 1062 */               String email = Mailer.getPhaseOneMail();
/* 1063 */               email = email.replace("@pname", aPlayerName);
/* 1064 */               email = email.replace("@email", URLEncoder.encode(aEmailAddress, "UTF-8"));
/* 1065 */               email = email.replace("@expiration", GeneralUtilities.toGMTString(expireTime));
/* 1066 */               email = email.replace("@password", password);
/*      */               
/* 1068 */               Mailer.sendMail(mailAccount, aEmailAddress, "Wurm Online character creation request", email);
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 1073 */               toReturn.put("Hash", password);
/* 1074 */               logger.log(Level.WARNING, "NO MAIL SENT: DEVMODE ACTIVE");
/*      */             } 
/* 1076 */             toReturn.put("ok", "An email has been sent to " + aEmailAddress + ". You will have to click a link in order to proceed with the registration.");
/*      */           
/*      */           }
/* 1079 */           catch (Exception ex)
/*      */           {
/* 1081 */             toReturn.put("error", "An error occured when sending the mail: " + ex.getMessage() + ". No account was reserved.");
/*      */             
/* 1083 */             pedd.delete();
/* 1084 */             logger.log(Level.WARNING, aEmailAddress + ":" + ex.getMessage(), ex);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1089 */           toReturn.put("error", "The account could not be created. Please try later.");
/* 1090 */           logger.warning(aEmailAddress + " The account could not be created. Please try later.");
/*      */         } 
/*      */       } else {
/*      */         
/* 1094 */         toReturn.put("error", errstat);
/*      */       } 
/* 1096 */     }  return toReturn;
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
/*      */   public Map<String, ?> createPlayerPhaseTwo(String intraServerPassword, String playerName, String hashedIngamePassword, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender, String phaseOneHash) throws RemoteException {
/* 1140 */     validateIntraServerPassword(intraServerPassword);
/* 1141 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 1143 */       logger.finer(getRemoteClientDetails() + " createPlayerPhaseTwo for player name: " + playerName);
/*      */     }
/* 1145 */     appearance = Server.rand.nextInt(5);
/*      */     
/* 1147 */     this.faceRandom.setSeed(8263186381637L + appearance);
/* 1148 */     appearance = this.faceRandom.nextLong();
/* 1149 */     return createPlayerPhaseTwo(intraServerPassword, playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender, phaseOneHash, 1);
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
/*      */   public Map<String, ?> createPlayerPhaseTwo(String intraServerPassword, String playerName, String hashedIngamePassword, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender, String phaseOneHash, int serverId) throws RemoteException {
/* 1198 */     validateIntraServerPassword(intraServerPassword);
/* 1199 */     appearance = Server.rand.nextInt(5);
/*      */     
/* 1201 */     this.faceRandom.setSeed(8263186381637L + appearance);
/* 1202 */     appearance = this.faceRandom.nextLong();
/* 1203 */     return createPlayerPhaseTwo(intraServerPassword, playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender, phaseOneHash, serverId, true);
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
/*      */   public Map<String, ?> createPlayerPhaseTwo(String intraServerPassword, String playerName, String hashedIngamePassword, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender, String phaseOneHash, int serverId, boolean optInEmail) throws RemoteException {
/* 1253 */     validateIntraServerPassword(intraServerPassword);
/* 1254 */     serverId = 1;
/* 1255 */     appearance = Server.rand.nextInt(5);
/*      */     
/* 1257 */     this.faceRandom.setSeed(8263186381637L + appearance);
/* 1258 */     appearance = this.faceRandom.nextLong();
/*      */     
/* 1260 */     kingdom = 4;
/* 1261 */     if (kingdom == 3) {
/* 1262 */       serverId = 3;
/*      */     }
/*      */     
/* 1265 */     Map<String, Object> toReturn = new HashMap<>();
/* 1266 */     if (Constants.maintaining) {
/*      */       
/* 1268 */       toReturn.put("error", "The server is currently in maintenance mode.");
/* 1269 */       return toReturn;
/*      */     } 
/* 1271 */     logger.log(Level.INFO, getRemoteClientDetails() + " Trying to create player phase two " + playerName);
/* 1272 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/* 1274 */       if (playerName == null || hashedIngamePassword == null || challengePhrase == null || challengeAnswer == null || emailAddress == null || phaseOneHash == null) {
/*      */ 
/*      */         
/* 1277 */         if (playerName == null)
/* 1278 */           toReturn.put("error", "PlayerName is null."); 
/* 1279 */         if (hashedIngamePassword == null)
/* 1280 */           toReturn.put("error", "hashedIngamePassword is null."); 
/* 1281 */         if (challengePhrase == null)
/* 1282 */           toReturn.put("error", "ChallengePhrase is null."); 
/* 1283 */         if (challengeAnswer == null)
/* 1284 */           toReturn.put("error", "ChallengeAnswer is null."); 
/* 1285 */         if (emailAddress == null)
/* 1286 */           toReturn.put("error", "EmailAddress is null."); 
/* 1287 */         if (phaseOneHash == null)
/* 1288 */           toReturn.put("error", "phaseOneHash is null."); 
/* 1289 */         return toReturn;
/*      */       } 
/* 1291 */       if (challengePhrase.equals(challengeAnswer)) {
/*      */         
/* 1293 */         toReturn.put("error", "We don't allow the password retrieval question and answer to be the same.");
/* 1294 */         return toReturn;
/*      */       } 
/* 1296 */       playerName = LoginHandler.raiseFirstLetter(playerName);
/*      */       
/* 1298 */       String errstat = LoginHandler.checkName2(playerName);
/* 1299 */       if (errstat.length() > 0) {
/*      */         
/* 1301 */         toReturn.put("error", errstat);
/* 1302 */         return toReturn;
/*      */       } 
/* 1304 */       if (PlayerInfoFactory.doesPlayerExist(playerName)) {
/*      */         
/* 1306 */         toReturn.put("error", "The name " + playerName + " is taken. Your reservation must have expired.");
/* 1307 */         return toReturn;
/*      */       } 
/* 1309 */       if (hashedIngamePassword.length() < 6 || hashedIngamePassword.length() > 40) {
/*      */         
/* 1311 */         toReturn.put("error", "The hashed password must contain at least 6 characters and maximum 40 characters.");
/* 1312 */         return toReturn;
/*      */       } 
/* 1314 */       if (challengePhrase.length() < 4 || challengePhrase.length() > 120) {
/*      */         
/* 1316 */         toReturn.put("error", "The challenge phrase must contain at least 4 characters and max 120 characters.");
/* 1317 */         return toReturn;
/*      */       } 
/* 1319 */       if (challengeAnswer.length() < 1 || challengeAnswer.length() > 20) {
/*      */         
/* 1321 */         toReturn.put("error", "The challenge answer must contain at least 1 character and max 20 characters.");
/* 1322 */         return toReturn;
/*      */       } 
/* 1324 */       if (emailAddress.length() > 125) {
/*      */         
/* 1326 */         toReturn.put("error", "The email address consists of too many characters.");
/* 1327 */         return toReturn;
/*      */       } 
/* 1329 */       if (isEmailValid(emailAddress)) {
/*      */ 
/*      */         
/*      */         try {
/* 1333 */           PendingAccount pacc = PendingAccount.getAccount(playerName);
/* 1334 */           if (pacc == null) {
/*      */             
/* 1336 */             toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1337 */             toReturn.put("error", "The verification is done too late or the name was never reserved. The name reservation expires after two days. Please try to create the player again.");
/*      */ 
/*      */             
/* 1340 */             return toReturn;
/*      */           } 
/* 1342 */           if (pacc.password.equals(phaseOneHash)) {
/*      */             
/* 1344 */             if (pacc.emailAddress.toLowerCase().equals(emailAddress.toLowerCase())) {
/*      */               
/*      */               try
/*      */               {
/* 1348 */                 if (serverId == Servers.localServer.id)
/*      */                 {
/*      */                   
/* 1351 */                   toReturn.put("PlayerId", new Long(
/*      */                         
/* 1353 */                         LoginHandler.createPlayer(playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender)));
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 1359 */                 else if (Servers.localServer.LOGINSERVER)
/*      */                 {
/*      */ 
/*      */                   
/* 1363 */                   ServerEntry toCreateOn = Servers.getServerWithId(serverId);
/* 1364 */                   if (toCreateOn != null)
/*      */                   {
/* 1366 */                     int tilex = toCreateOn.SPAWNPOINTJENNX;
/* 1367 */                     int tiley = toCreateOn.SPAWNPOINTJENNY;
/* 1368 */                     if (kingdom == 2) {
/*      */                       
/* 1370 */                       tilex = toCreateOn.SPAWNPOINTMOLX;
/* 1371 */                       tiley = toCreateOn.SPAWNPOINTMOLY;
/*      */                     } 
/* 1373 */                     if (kingdom == 3) {
/*      */                       
/* 1375 */                       tilex = toCreateOn.SPAWNPOINTLIBX;
/* 1376 */                       tiley = toCreateOn.SPAWNPOINTLIBY;
/*      */                     } 
/* 1378 */                     if (serverId == 5) {
/*      */                       
/* 1380 */                       tilex = 2884;
/* 1381 */                       tiley = 3004;
/*      */                     } 
/* 1383 */                     LoginServerWebConnection lsw = new LoginServerWebConnection(serverId);
/* 1384 */                     byte[] playerData = lsw.createAndReturnPlayer(playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender, false, false, false);
/*      */ 
/*      */                     
/* 1387 */                     long wurmId = IntraServerConnection.savePlayerToDisk(playerData, tilex, tiley, true, true);
/*      */                     
/* 1389 */                     toReturn.put("PlayerId", Long.valueOf(wurmId));
/*      */                   }
/*      */                   else
/*      */                   {
/* 1393 */                     toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1394 */                     toReturn.put("error", "Failed to create player " + playerName + ": The desired server does not exist.");
/*      */                   }
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/* 1400 */                   toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1401 */                   toReturn.put("error", "Failed to create player " + playerName + ": This is not a login server.");
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/* 1406 */               catch (Exception cex)
/*      */               {
/* 1408 */                 logger.log(Level.WARNING, "Failed to create player " + playerName + "!" + cex.getMessage(), cex);
/* 1409 */                 toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1410 */                 toReturn.put("error", "Failed to create player " + playerName + ":" + cex.getMessage());
/* 1411 */                 return toReturn;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 1416 */               toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1417 */               toReturn.put("error", "The email supplied does not match with the one that was registered with the name.");
/*      */               
/* 1419 */               return toReturn;
/*      */             } 
/* 1421 */             pacc.delete();
/*      */             
/*      */             try {
/* 1424 */               if (!Constants.devmode)
/*      */               {
/* 1426 */                 String mail = Mailer.getPhaseTwoMail();
/* 1427 */                 mail = mail.replace("@pname", playerName);
/*      */                 
/* 1429 */                 Mailer.sendMail(mailAccount, emailAddress, "Wurm Online character creation success", mail);
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 1438 */             catch (Exception cex2) {
/*      */               
/* 1440 */               logger.log(Level.WARNING, "Failed to send email to " + emailAddress + " for player " + playerName + ":" + cex2
/* 1441 */                   .getMessage(), cex2);
/*      */               
/* 1443 */               toReturn.put("error", "Failed to send email to " + emailAddress + " for player " + playerName + ":" + cex2
/* 1444 */                   .getMessage());
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1449 */             toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1450 */             toReturn.put("error", "The verification hash does not match.");
/*      */           }
/*      */         
/* 1453 */         } catch (Exception ex) {
/*      */           
/* 1455 */           logger.log(Level.WARNING, "Failed to create player " + playerName + "!" + ex.getMessage(), ex);
/* 1456 */           toReturn.put("PlayerId", Long.valueOf(-1L));
/* 1457 */           toReturn.put("error", ex.getMessage());
/*      */         } 
/*      */       } else {
/*      */         
/* 1461 */         toReturn.put("error", "The email address " + emailAddress + " is not valid.");
/*      */       } 
/* 1463 */     }  return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] createAndReturnPlayer(String intraServerPassword, String playerName, String hashedIngamePassword, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender, boolean titleKeeper, boolean addPremium, boolean passwordIsHashed) throws RemoteException {
/* 1472 */     validateIntraServerPassword(intraServerPassword);
/* 1473 */     if (Constants.maintaining)
/*      */     {
/* 1475 */       throw new RemoteException("The server is currently in maintenance mode.");
/*      */     }
/*      */     
/*      */     try {
/* 1479 */       appearance = Server.rand.nextInt(5);
/*      */       
/* 1481 */       this.faceRandom.setSeed(8263186381637L + appearance);
/* 1482 */       appearance = this.faceRandom.nextLong();
/* 1483 */       logger.log(Level.INFO, getClientHost() + " Received create attempt for " + playerName);
/* 1484 */       return LoginHandler.createAndReturnPlayer(playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender, titleKeeper, addPremium, passwordIsHashed);
/*      */     
/*      */     }
/* 1487 */     catch (Exception ex) {
/*      */       
/* 1489 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 1490 */       throw new RemoteException(ex.getMessage());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> addMoneyToBank(String intraServerPassword, String name, long moneyToAdd, String transactionDetail) throws RemoteException {
/* 1511 */     validateIntraServerPassword(intraServerPassword);
/*      */ 
/*      */     
/* 1514 */     byte executor = 6;
/*      */     
/* 1516 */     boolean ok = true;
/* 1517 */     String campaignId = "";
/* 1518 */     name = LoginHandler.raiseFirstLetter(name);
/* 1519 */     Map<String, String> toReturn = new HashMap<>();
/*      */     
/* 1521 */     if (name == null || name.length() == 0) {
/*      */       
/* 1523 */       toReturn.put("error", "Illegal name.");
/* 1524 */       return toReturn;
/*      */     } 
/* 1526 */     if (moneyToAdd <= 0L) {
/*      */       
/* 1528 */       toReturn.put("error", "Invalid amount; must be greater than zero");
/* 1529 */       return toReturn;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1534 */     synchronized (Server.SYNC_LOCK) {
/*      */ 
/*      */       
/*      */       try {
/* 1538 */         Player p = Players.getInstance().getPlayer(name);
/* 1539 */         p.addMoney(moneyToAdd);
/* 1540 */         long money = p.getMoney();
/* 1541 */         new MoneyTransfer(p.getName(), p.getWurmId(), money, moneyToAdd, transactionDetail, executor, campaignId);
/* 1542 */         Change change = new Change(moneyToAdd);
/* 1543 */         Change current = new Change(money);
/* 1544 */         p.save();
/* 1545 */         toReturn.put("ok", "An amount of " + change.getChangeString() + " has been added to the account. Current balance is " + current
/* 1546 */             .getChangeString() + ".");
/*      */       }
/* 1548 */       catch (NoSuchPlayerException nsp) {
/*      */ 
/*      */         
/*      */         try {
/* 1552 */           PlayerInfo p = PlayerInfoFactory.createPlayerInfo(name);
/* 1553 */           p.load();
/* 1554 */           if (p.wurmId > 0L) {
/*      */             
/* 1556 */             p.setMoney(p.money + moneyToAdd);
/* 1557 */             Change change = new Change(moneyToAdd);
/* 1558 */             Change current = new Change(p.money);
/* 1559 */             p.save();
/* 1560 */             toReturn.put("ok", "An amount of " + change.getChangeString() + " has been added to the account. Current balance is " + current
/* 1561 */                 .getChangeString() + ". It may take a while to reach your server.");
/*      */             
/* 1563 */             if (Servers.localServer.id != p.currentServer)
/*      */             {
/* 1565 */               new MoneyTransfer(name, p.wurmId, p.money, moneyToAdd, transactionDetail, executor, campaignId, false);
/*      */             }
/*      */             else
/*      */             {
/* 1569 */               new MoneyTransfer(p.getName(), p.wurmId, p.money, moneyToAdd, transactionDetail, executor, campaignId);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1574 */             toReturn.put("error", "No player found with the name " + name + ".");
/*      */           }
/*      */         
/* 1577 */         } catch (IOException iox) {
/*      */           
/* 1579 */           logger.log(Level.WARNING, name + ":" + iox.getMessage(), iox);
/* 1580 */           throw new RemoteException("An error occured. Please contact customer support.");
/*      */         }
/*      */       
/* 1583 */       } catch (IOException iox) {
/*      */         
/* 1585 */         logger.log(Level.WARNING, name + ":" + iox.getMessage(), iox);
/* 1586 */         throw new RemoteException("An error occured. Please contact customer support.");
/*      */       }
/* 1588 */       catch (Exception ex) {
/*      */         
/* 1590 */         logger.log(Level.WARNING, name + ":" + ex.getMessage(), ex);
/* 1591 */         throw new RemoteException("An error occured. Please contact customer support.");
/*      */       } 
/*      */     } 
/*      */     
/* 1595 */     return toReturn;
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
/*      */   public long getMoney(String intraServerPassword, long playerId, String playerName) throws RemoteException {
/* 1611 */     validateIntraServerPassword(intraServerPassword);
/* 1612 */     PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(playerId);
/* 1613 */     if (p == null) {
/*      */       
/* 1615 */       p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/* 1618 */         p.load();
/*      */       }
/* 1620 */       catch (IOException iox) {
/*      */         
/* 1622 */         logger.log(Level.WARNING, "Failed to load pinfo for " + playerName);
/*      */       } 
/* 1624 */       if (p.wurmId <= 0L)
/* 1625 */         return 0L; 
/*      */     } 
/* 1627 */     if (p != null)
/* 1628 */       return p.money; 
/* 1629 */     return 0L;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> reversePayment(String intraServerPassword, long moneyToRemove, int monthsToRemove, int daysToRemove, String reversalTransactionID, String originalTransactionID, String playerName) throws RemoteException {
/* 1666 */     validateIntraServerPassword(intraServerPassword);
/* 1667 */     Map<String, String> toReturn = new HashMap<>();
/* 1668 */     logger.log(Level.INFO, getRemoteClientDetails() + " Reverse payment for player name: " + playerName + ", reversalTransactionID: " + reversalTransactionID + ", originalTransactionID: " + originalTransactionID);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1673 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/* 1674 */       p.load();
/* 1675 */       if (p.wurmId > 0L) {
/*      */         
/* 1677 */         if (moneyToRemove > 0L) {
/*      */           
/* 1679 */           if (p.money < moneyToRemove) {
/*      */             
/* 1681 */             Change lack = new Change(moneyToRemove - p.money);
/* 1682 */             toReturn.put("moneylack", "An amount of " + lack.getChangeString() + " was lacking from the account. Removing what we can.");
/*      */           } 
/*      */           
/* 1685 */           p.setMoney(Math.max(0L, p.money - moneyToRemove));
/* 1686 */           Change change = new Change(moneyToRemove);
/* 1687 */           Change current = new Change(p.money);
/* 1688 */           p.save();
/* 1689 */           toReturn.put("moneyok", "An amount of " + change.getChangeString() + " has been removed from the account. Current balance is " + current
/* 1690 */               .getChangeString() + ".");
/*      */           
/* 1692 */           if (Servers.localServer.id != p.currentServer) {
/*      */             
/* 1694 */             new MoneyTransfer(playerName, p.wurmId, p.money, moneyToRemove, originalTransactionID, (byte)4, "", false);
/*      */           }
/*      */           else {
/*      */             
/* 1698 */             new MoneyTransfer(playerName, p.wurmId, p.money, moneyToRemove, originalTransactionID, (byte)4, "");
/*      */           } 
/*      */         } 
/* 1701 */         if (daysToRemove > 0 || monthsToRemove > 0) {
/*      */           
/* 1703 */           long timeToRemove = 0L;
/* 1704 */           if (daysToRemove > 0)
/* 1705 */             timeToRemove = daysToRemove * 86400000L; 
/* 1706 */           if (monthsToRemove > 0)
/* 1707 */             timeToRemove += monthsToRemove * 86400000L * 30L; 
/* 1708 */           long currTime = p.getPaymentExpire();
/*      */           
/* 1710 */           currTime = Math.max(currTime, System.currentTimeMillis());
/* 1711 */           currTime = Math.max(currTime - timeToRemove, System.currentTimeMillis());
/*      */           
/*      */           try {
/* 1714 */             p.setPaymentExpire(currTime);
/* 1715 */             String expireString = "The premier playing time has expired now.";
/* 1716 */             if (System.currentTimeMillis() < currTime)
/*      */             {
/* 1718 */               expireString = "The player now has premier playing time until " + GeneralUtilities.toGMTString(currTime) + ". Your in game player account will be updated shortly.";
/*      */             }
/* 1720 */             p.save();
/* 1721 */             toReturn.put("timeok", expireString);
/* 1722 */             if (p.currentServer != Servers.localServer.id)
/*      */             {
/* 1724 */               new TimeTransfer(playerName, p.wurmId, -monthsToRemove, false, -daysToRemove, originalTransactionID, false);
/*      */ 
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */               
/* 1732 */               new TimeTransfer(p.getName(), p.wurmId, -monthsToRemove, false, -daysToRemove, originalTransactionID);
/*      */             }
/*      */           
/* 1735 */           } catch (IOException iox) {
/*      */             
/* 1737 */             toReturn.put("timeerror", p
/* 1738 */                 .getName() + ": failed to set expire to " + currTime + ", " + iox.getMessage());
/* 1739 */             logger.log(Level.WARNING, p
/* 1740 */                 .getName() + ": failed to set expire to " + currTime + ", " + iox.getMessage(), iox);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1746 */         toReturn.put("error", "No player found with the name " + playerName + ".");
/*      */       } 
/* 1748 */     } catch (IOException iox) {
/*      */       
/* 1750 */       logger.log(Level.WARNING, playerName + ":" + iox.getMessage(), iox);
/* 1751 */       throw new RemoteException("An error occured. Please contact customer support.");
/*      */     } 
/* 1753 */     return toReturn;
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
/*      */   public Map<String, String> addMoneyToBank(String intraServerPassword, String name, long moneyToAdd, String transactionDetail, boolean ingame) throws RemoteException {
/* 1773 */     validateIntraServerPassword(intraServerPassword);
/* 1774 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 1776 */       logger.finer(getRemoteClientDetails() + " addMoneyToBank for player name: " + name);
/*      */     }
/* 1778 */     return addMoneyToBank(intraServerPassword, name, -1L, moneyToAdd, transactionDetail, ingame);
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
/*      */   public static String encryptMD5(String plaintext) throws Exception {
/* 1790 */     MessageDigest md = null;
/*      */     
/*      */     try {
/* 1793 */       md = MessageDigest.getInstance("MD5");
/*      */     }
/* 1795 */     catch (NoSuchAlgorithmException e) {
/*      */       
/* 1797 */       throw new WurmServerException("No such algorithm 'MD5'", e);
/*      */     } 
/*      */     
/*      */     try {
/* 1801 */       md.update(plaintext.getBytes("UTF-8"));
/*      */     }
/* 1803 */     catch (UnsupportedEncodingException e) {
/*      */       
/* 1805 */       throw new WurmServerException("No such encoding: UTF-8", e);
/*      */     } 
/* 1807 */     byte[] raw = md.digest();
/* 1808 */     BigInteger bi = new BigInteger(1, raw);
/* 1809 */     String hash = bi.toString(16);
/* 1810 */     return hash;
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
/*      */   public Map<String, String> addMoneyToBank(String intraServerPassword, String name, long wurmId, long moneyToAdd, String transactionDetail, boolean ingame) throws RemoteException {
/* 1831 */     validateIntraServerPassword(intraServerPassword);
/* 1832 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/* 1834 */       Map<String, String> toReturn = new HashMap<>();
/* 1835 */       if ((name == null || name.length() == 0) && wurmId <= 0L) {
/*      */         
/* 1837 */         toReturn.put("error", "Illegal name.");
/* 1838 */         return toReturn;
/*      */       } 
/* 1840 */       if (moneyToAdd <= 0L) {
/*      */         
/* 1842 */         toReturn.put("error", "Invalid amount; must be greater than zero");
/* 1843 */         return toReturn;
/*      */       } 
/* 1845 */       if (name != null)
/* 1846 */         name = LoginHandler.raiseFirstLetter(name); 
/* 1847 */       byte executor = 6;
/* 1848 */       String campaignId = "";
/*      */       
/* 1850 */       logger.log(Level.INFO, getRemoteClientDetails() + " Add money to bank 2 , " + moneyToAdd + " for player name: " + name + ", wid " + wurmId);
/*      */ 
/*      */ 
/*      */       
/* 1854 */       if ((name != null && name.length() > 0) || wurmId > 0L) {
/*      */         
/*      */         try {
/*      */           
/* 1858 */           Player p = null;
/* 1859 */           if (wurmId <= 0L) {
/* 1860 */             p = Players.getInstance().getPlayer(name);
/*      */           } else {
/* 1862 */             p = Players.getInstance().getPlayer(wurmId);
/* 1863 */           }  p.addMoney(moneyToAdd);
/* 1864 */           long money = p.getMoney();
/* 1865 */           if (!ingame) {
/* 1866 */             new MoneyTransfer(p.getName(), p.getWurmId(), money, moneyToAdd, transactionDetail, (byte)6, "");
/*      */           }
/* 1868 */           Change change = new Change(moneyToAdd);
/* 1869 */           Change current = new Change(money);
/* 1870 */           p.save();
/* 1871 */           toReturn.put("ok", "An amount of " + change.getChangeString() + " has been added to the account. Current balance is " + current
/* 1872 */               .getChangeString() + ".");
/*      */         }
/* 1874 */         catch (NoSuchPlayerException nsp) {
/*      */ 
/*      */           
/*      */           try {
/* 1878 */             PlayerInfo p = null;
/* 1879 */             if (name != null && name.length() > 0) {
/* 1880 */               p = PlayerInfoFactory.createPlayerInfo(name);
/*      */             } else {
/* 1882 */               p = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/* 1883 */             }  if (p != null) {
/*      */               
/* 1885 */               p.load();
/* 1886 */               if (p.wurmId > 0L) {
/*      */                 
/* 1888 */                 p.setMoney(p.money + moneyToAdd);
/* 1889 */                 Change change = new Change(moneyToAdd);
/* 1890 */                 Change current = new Change(p.money);
/* 1891 */                 p.save();
/* 1892 */                 toReturn.put("ok", "An amount of " + change.getChangeString() + " has been added to the account. Current balance is " + current
/* 1893 */                     .getChangeString() + ". It may take a while to reach your server.");
/*      */                 
/* 1895 */                 if (!ingame)
/*      */                 {
/* 1897 */                   if (Servers.localServer.id != p.currentServer)
/*      */                   {
/* 1899 */                     new MoneyTransfer(p.getName(), p.wurmId, p.money, moneyToAdd, transactionDetail, (byte)6, "", false);
/*      */                   }
/*      */                   else
/*      */                   {
/* 1903 */                     new MoneyTransfer(p.getName(), p.wurmId, p.money, moneyToAdd, transactionDetail, (byte)6, "");
/*      */                   }
/*      */                 
/*      */                 }
/*      */               } else {
/*      */                 
/* 1909 */                 toReturn.put("error", "No player found with the wurmid " + p.wurmId + ".");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 1914 */               toReturn.put("error", "No player found with the name " + name + ".");
/*      */             }
/*      */           
/* 1917 */           } catch (IOException iox) {
/*      */             
/* 1919 */             logger.log(Level.WARNING, name + ": " + wurmId + "," + iox.getMessage(), iox);
/* 1920 */             throw new RemoteException("An error occured. Please contact customer support.");
/*      */           }
/*      */         
/* 1923 */         } catch (IOException iox) {
/*      */           
/* 1925 */           logger.log(Level.WARNING, name + ":" + wurmId + "," + iox.getMessage(), iox);
/* 1926 */           throw new RemoteException("An error occured. Please contact customer support.");
/*      */         }
/* 1928 */         catch (Exception ex) {
/*      */           
/* 1930 */           logger.log(Level.WARNING, name + ":" + wurmId + "," + ex.getMessage(), ex);
/* 1931 */           throw new RemoteException("An error occured. Please contact customer support.");
/*      */         } 
/*      */       }
/* 1934 */       return toReturn;
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
/*      */ 
/*      */   
/*      */   public long chargeMoney(String intraServerPassword, String playerName, long moneyToCharge) throws RemoteException {
/* 1950 */     validateIntraServerPassword(intraServerPassword);
/* 1951 */     logger.log(Level.INFO, getRemoteClientDetails() + " ChargeMoney for player name: " + playerName + ", money: " + moneyToCharge);
/*      */     
/* 1953 */     if (Servers.localServer.id == Servers.loginServer.id) {
/*      */       
/* 1955 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/* 1958 */         p.load();
/* 1959 */         if (p.money > 0L) {
/*      */           
/* 1961 */           if (p.money - moneyToCharge < 0L) {
/* 1962 */             return -10L;
/*      */           }
/*      */           
/* 1965 */           p.setMoney(p.money - moneyToCharge);
/* 1966 */           logger.info(playerName + " was charged " + moneyToCharge + " and now has " + p.money);
/*      */           
/* 1968 */           return p.money;
/*      */         } 
/* 1970 */         return -10L;
/*      */       }
/* 1972 */       catch (IOException iox) {
/*      */         
/* 1974 */         logger.log(Level.WARNING, playerName + ": " + iox.getMessage(), iox);
/* 1975 */         return -10L;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1980 */     logger.warning(playerName + " cannot charge " + moneyToCharge + " as this server is not the login server");
/* 1981 */     return -10L;
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
/*      */   public Map<String, String> addPlayingTime(String intraServerPassword, String name, int months, int days, String transactionDetail) throws RemoteException {
/* 2006 */     validateIntraServerPassword(intraServerPassword);
/* 2007 */     return addPlayingTime(intraServerPassword, name, months, days, transactionDetail, true);
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
/*      */   public Map<String, String> addPlayingTime(String intraServerPassword, String name, int months, int days, String transactionDetail, boolean addSleepPowder) throws RemoteException {
/* 2033 */     validateIntraServerPassword(intraServerPassword);
/* 2034 */     synchronized (Server.SYNC_LOCK) {
/*      */       
/* 2036 */       Map<String, String> toReturn = new HashMap<>();
/* 2037 */       if (name == null || name.length() == 0 || transactionDetail == null || transactionDetail.length() == 0) {
/*      */         
/* 2039 */         toReturn.put("error", "Illegal arguments. Check if name or transaction detail is null or empty strings.");
/* 2040 */         return toReturn;
/*      */       } 
/* 2042 */       if (months < 0 || days < 0) {
/*      */         
/* 2044 */         toReturn.put("error", "Illegal arguments. Make sure that the values for days and months are not negative.");
/* 2045 */         return toReturn;
/*      */       } 
/*      */       
/* 2048 */       boolean ok = true;
/*      */ 
/*      */ 
/*      */       
/* 2052 */       logger.log(Level.INFO, getRemoteClientDetails() + " Addplayingtime for player name: " + name + ", months: " + months + ", days: " + days + ", transactionDetail: " + transactionDetail);
/*      */ 
/*      */       
/* 2055 */       SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd'-'hh:mm:ss");
/*      */       
/* 2057 */       synchronized (Server.SYNC_LOCK) {
/*      */         
/* 2059 */         long timeToAdd = 0L;
/* 2060 */         if (days != 0)
/* 2061 */           timeToAdd = days * 86400000L; 
/* 2062 */         if (months != 0) {
/* 2063 */           timeToAdd += months * 86400000L * 30L;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 2072 */           Player p = Players.getInstance().getPlayer(name);
/* 2073 */           long currTime = p.getPaymentExpire();
/* 2074 */           if (timeToAdd > 0L)
/*      */           {
/* 2076 */             if (currTime <= 0L) {
/*      */               
/* 2078 */               Server.addNewPlayer(p.getName());
/*      */             } else {
/*      */               
/* 2081 */               Server.incrementOldPremiums(p.getName());
/*      */             }  } 
/* 2083 */           currTime = Math.max(currTime, System.currentTimeMillis());
/* 2084 */           currTime += timeToAdd;
/*      */           
/*      */           try {
/* 2087 */             p.getSaveFile().setPaymentExpire(currTime, !transactionDetail.startsWith("firstBuy"));
/* 2088 */             new TimeTransfer(p.getName(), p.getWurmId(), months, addSleepPowder, days, transactionDetail);
/*      */           }
/* 2090 */           catch (IOException iox) {
/*      */             
/* 2092 */             logger.log(Level.WARNING, p
/* 2093 */                 .getName() + ": failed to set expire to " + currTime + ", " + iox.getMessage(), iox);
/*      */           } 
/*      */           
/* 2096 */           String expireString = "You now have premier playing time until " + formatter.format(new Date(currTime)) + ".";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2102 */           p.save();
/* 2103 */           toReturn.put("ok", expireString);
/* 2104 */           Message mess = new Message(null, (byte)3, ":Event", expireString);
/*      */           
/* 2106 */           mess.setReceiver(p.getWurmId());
/* 2107 */           Server.getInstance().addMessage(mess);
/* 2108 */           logger.info(p.getName() + ' ' + expireString);
/* 2109 */           if (addSleepPowder)
/*      */             
/*      */             try {
/* 2112 */               Item inventory = p.getInventory();
/* 2113 */               for (int x = 0; x < months; x++) {
/*      */                 
/* 2115 */                 Item i = ItemFactory.createItem(666, 99.0F, "");
/* 2116 */                 inventory.insertItem(i, true);
/*      */               } 
/* 2118 */               logger.log(Level.INFO, "Inserted " + months + " sleep powder in " + p.getName() + " inventory " + inventory
/* 2119 */                   .getWurmId());
/* 2120 */               Message rmess = new Message(null, (byte)3, ":Event", "You have received " + months + " sleeping powders in your inventory.");
/*      */               
/* 2122 */               rmess.setReceiver(p.getWurmId());
/* 2123 */               Server.getInstance().addMessage(rmess);
/*      */             }
/* 2125 */             catch (Exception ex) {
/*      */               
/* 2127 */               logger.log(Level.INFO, ex.getMessage(), ex);
/*      */             }  
/* 2129 */           return toReturn;
/*      */         }
/* 2131 */         catch (NoSuchPlayerException nsp) {
/*      */           
/*      */           try
/*      */           {
/* 2135 */             PlayerInfo p = PlayerInfoFactory.createPlayerInfo(name);
/* 2136 */             p.load();
/* 2137 */             if (p.wurmId > 0L) {
/*      */               
/* 2139 */               long currTime = p.getPaymentExpire();
/*      */               
/* 2141 */               if (timeToAdd > 0L)
/*      */               {
/* 2143 */                 if (currTime <= 0L) {
/*      */                   
/* 2145 */                   Server.addNewPlayer(p.getName());
/*      */                 } else {
/*      */                   
/* 2148 */                   Server.incrementOldPremiums(p.getName());
/*      */                 }  } 
/* 2150 */               currTime = Math.max(currTime, System.currentTimeMillis());
/* 2151 */               currTime += timeToAdd;
/*      */               
/*      */               try {
/* 2154 */                 p.setPaymentExpire(currTime, !transactionDetail.startsWith("firstBuy"));
/*      */               }
/* 2156 */               catch (IOException iox) {
/*      */                 
/* 2158 */                 logger.log(Level.WARNING, p.getName() + ": failed to set expire to " + currTime + ", " + iox
/* 2159 */                     .getMessage(), iox);
/*      */               } 
/* 2161 */               ServerEntry entry = Servers.getServerWithId(p.currentServer);
/* 2162 */               String expireString = "Your premier playing time has expired now.";
/* 2163 */               if (System.currentTimeMillis() < currTime)
/*      */               {
/* 2165 */                 if (entry.entryServer) {
/*      */                   
/* 2167 */                   expireString = "You now have premier playing time until " + formatter.format(new Date(currTime)) + ". Your in game player account will be updated shortly. NOTE that you will have to use a portal to get to the premium servers in order to benefit from it.";
/*      */                 }
/*      */                 else {
/*      */                   
/* 2171 */                   expireString = "You now have premier playing time until " + formatter.format(new Date(currTime)) + ". Your in game player account will be updated shortly.";
/*      */                 } 
/*      */               }
/* 2174 */               p.save();
/* 2175 */               toReturn.put("ok", expireString);
/* 2176 */               logger.info(p.getName() + ' ' + expireString);
/* 2177 */               if (p.currentServer != Servers.localServer.id) {
/*      */                 
/* 2179 */                 new TimeTransfer(name, p.wurmId, months, addSleepPowder, days, transactionDetail, false);
/*      */               }
/*      */               else {
/*      */                 
/* 2183 */                 new TimeTransfer(p.getName(), p.wurmId, months, addSleepPowder, days, transactionDetail);
/* 2184 */                 if (addSleepPowder)
/*      */                   
/*      */                   try {
/* 2187 */                     long inventoryId = DbCreatureStatus.getInventoryIdFor(p.wurmId);
/* 2188 */                     for (int x = 0; x < months; x++) {
/*      */                       
/* 2190 */                       Item i = ItemFactory.createItem(666, 99.0F, "");
/* 2191 */                       i.setParentId(inventoryId, true);
/* 2192 */                       i.setOwnerId(p.wurmId);
/*      */                     } 
/* 2194 */                     logger.log(Level.INFO, "Inserted " + months + " sleep powder in offline " + p
/* 2195 */                         .getName() + " inventory " + inventoryId);
/*      */ 
/*      */                   
/*      */                   }
/* 2199 */                   catch (Exception ex) {
/*      */                     
/* 2201 */                     logger.log(Level.INFO, ex.getMessage(), ex);
/*      */                   }  
/*      */               } 
/* 2204 */               return toReturn;
/*      */             } 
/*      */ 
/*      */             
/* 2208 */             toReturn.put("error", "No player found with the name " + name + ".");
/* 2209 */             return toReturn;
/*      */           
/*      */           }
/* 2212 */           catch (IOException iox)
/*      */           {
/* 2214 */             logger.log(Level.WARNING, name + ":" + iox.getMessage(), iox);
/* 2215 */             throw new RemoteException("An error occured. Please contact customer support.");
/*      */           }
/*      */         
/* 2218 */         } catch (IOException iox) {
/*      */           
/* 2220 */           logger.log(Level.WARNING, name + ":" + iox.getMessage(), iox);
/* 2221 */           throw new RemoteException("An error occured. Please contact customer support.");
/*      */         }
/* 2223 */         catch (Exception ex) {
/*      */           
/* 2225 */           logger.log(Level.WARNING, name + ":" + ex.getMessage(), ex);
/* 2226 */           throw new RemoteException("An error occured. Please contact customer support.");
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Integer, String> getDeeds(String intraServerPassword) throws RemoteException {
/* 2246 */     validateIntraServerPassword(intraServerPassword);
/* 2247 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2249 */       logger.finer(getRemoteClientDetails() + " getDeeds");
/*      */     }
/* 2251 */     Map<Integer, String> toReturn = new HashMap<>();
/* 2252 */     Village[] vills = Villages.getVillages();
/* 2253 */     for (int x = 0; x < vills.length; x++)
/* 2254 */       toReturn.put(Integer.valueOf((vills[x]).id), vills[x].getName()); 
/* 2255 */     return toReturn;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ?> getDeedSummary(String intraServerPassword, int aVillageID) throws RemoteException {
/* 2294 */     validateIntraServerPassword(intraServerPassword);
/* 2295 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2297 */       logger.finer(getRemoteClientDetails() + " getDeedSummary for villageID: " + aVillageID);
/*      */     }
/*      */     
/*      */     try {
/* 2301 */       Village village = Villages.getVillage(aVillageID);
/* 2302 */       Map<String, Object> toReturn = new HashMap<>();
/* 2303 */       toReturn.put("Villageid", Integer.valueOf(village.getId()));
/* 2304 */       toReturn.put("Deedid", Long.valueOf(village.getDeedId()));
/* 2305 */       toReturn.put("Name", village.getName());
/* 2306 */       toReturn.put("Motto", village.getMotto());
/* 2307 */       toReturn.put("Location", Kingdoms.getNameFor(village.kingdom));
/* 2308 */       toReturn.put("Size", Integer.valueOf((village.getEndX() - village.getStartX()) / 2));
/* 2309 */       toReturn.put("Founder", village.getFounderName());
/* 2310 */       toReturn.put("Mayor", village.mayorName);
/* 2311 */       if (village.disband > 0L) {
/*      */         
/* 2313 */         toReturn.put("Disbanding in", Server.getTimeFor(village.disband - System.currentTimeMillis()));
/* 2314 */         toReturn.put("Disbander", Players.getInstance().getNameFor(village.disbander));
/*      */       } 
/* 2316 */       toReturn.put("Citizens", Integer.valueOf(village.citizens.size()));
/* 2317 */       toReturn.put("Allies", Integer.valueOf((village.getAllies()).length));
/* 2318 */       if (village.guards != null) {
/* 2319 */         toReturn.put("guards", Integer.valueOf(village.guards.size()));
/*      */       }
/*      */       try {
/* 2322 */         short[] sp = village.getTokenCoords();
/* 2323 */         toReturn.put("Token Coord x", Integer.valueOf(sp[0]));
/* 2324 */         toReturn.put("Token Coord y", Integer.valueOf(sp[1]));
/*      */       }
/* 2326 */       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */       
/* 2330 */       return toReturn;
/*      */     }
/* 2332 */     catch (Exception ex) {
/*      */       
/* 2334 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 2335 */       throw new RemoteException(ex.getMessage());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Long> getPlayersForDeed(String intraServerPassword, int aVillageID) throws RemoteException {
/* 2355 */     validateIntraServerPassword(intraServerPassword);
/* 2356 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2358 */       logger.finer(getRemoteClientDetails() + " getPlayersForDeed for villageID: " + aVillageID);
/*      */     }
/* 2360 */     Map<String, Long> toReturn = new HashMap<>();
/*      */     
/*      */     try {
/* 2363 */       Village village = Villages.getVillage(aVillageID);
/* 2364 */       Citizen[] citizens = village.getCitizens();
/* 2365 */       for (int x = 0; x < citizens.length; x++)
/*      */       {
/* 2367 */         if (WurmId.getType(citizens[x].getId()) == 0) {
/*      */           
/*      */           try {
/*      */             
/* 2371 */             toReturn.put(Players.getInstance().getNameFor(citizens[x].getId()), new Long(citizens[x].getId()));
/*      */           }
/* 2373 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2380 */     catch (Exception ex) {
/*      */       
/* 2382 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 2383 */       throw new RemoteException(ex.getMessage());
/*      */     } 
/*      */     
/* 2386 */     return toReturn;
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
/*      */   public Map<String, Integer> getAlliesForDeed(String intraServerPassword, int aVillageID) throws RemoteException {
/* 2405 */     validateIntraServerPassword(intraServerPassword);
/* 2406 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2408 */       logger.finer(getRemoteClientDetails() + " getAlliesForDeed for villageID: " + aVillageID);
/*      */     }
/* 2410 */     Map<String, Integer> toReturn = new HashMap<>();
/*      */     
/*      */     try {
/* 2413 */       Village village = Villages.getVillage(aVillageID);
/* 2414 */       Village[] allies = village.getAllies();
/* 2415 */       for (int x = 0; x < allies.length; x++)
/*      */       {
/* 2417 */         toReturn.put(allies[x].getName(), Integer.valueOf(allies[x].getId()));
/*      */       }
/*      */     }
/* 2420 */     catch (Exception ex) {
/*      */       
/* 2422 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 2423 */       throw new RemoteException(ex.getMessage());
/*      */     } 
/*      */     
/* 2426 */     return toReturn;
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
/*      */   public String[] getHistoryForDeed(String intraServerPassword, int villageID, int maxLength) throws RemoteException {
/* 2445 */     validateIntraServerPassword(intraServerPassword);
/* 2446 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2448 */       logger.finer(getRemoteClientDetails() + " getHistoryForDeed for villageID: " + villageID + ", maxLength: " + maxLength);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 2453 */       Village village = Villages.getVillage(villageID);
/* 2454 */       return village.getHistoryAsStrings(maxLength);
/*      */     }
/* 2456 */     catch (Exception ex) {
/*      */       
/* 2458 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 2459 */       throw new RemoteException(ex.getMessage());
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAreaHistory(String intraServerPassword, int maxLength) throws RemoteException {
/* 2477 */     validateIntraServerPassword(intraServerPassword);
/* 2478 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2480 */       logger.finer(getRemoteClientDetails() + " getAreaHistory maxLength: " + maxLength);
/*      */     }
/* 2482 */     return HistoryManager.getHistory(maxLength);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ?> getItemSummary(String intraServerPassword, long aWurmID) throws RemoteException {
/* 2522 */     validateIntraServerPassword(intraServerPassword);
/* 2523 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2525 */       logger.finer(getRemoteClientDetails() + " getItemSummary for WurmId: " + aWurmID);
/*      */     }
/* 2527 */     Map<String, Object> toReturn = new HashMap<>();
/*      */     
/*      */     try {
/* 2530 */       Item item = Items.getItem(aWurmID);
/* 2531 */       toReturn.put("WurmId", new Long(aWurmID));
/* 2532 */       toReturn.put("Name", item.getName());
/* 2533 */       toReturn.put("QL", String.valueOf(item.getQualityLevel()));
/* 2534 */       toReturn.put("DMG", String.valueOf(item.getDamage()));
/* 2535 */       toReturn.put("SizeX", String.valueOf(item.getSizeX()));
/* 2536 */       toReturn.put("SizeY", String.valueOf(item.getSizeY()));
/* 2537 */       toReturn.put("SizeZ", String.valueOf(item.getSizeZ()));
/* 2538 */       if (item.getOwnerId() != -10L) {
/* 2539 */         toReturn.put("Owner", new Long(item.getOwnerId()));
/*      */       } else {
/*      */         
/* 2542 */         toReturn.put("Last owner", new Long(item.lastOwner));
/*      */       } 
/* 2544 */       toReturn.put("Coord x", Integer.valueOf((int)item.getPosX() >> 2));
/* 2545 */       toReturn.put("Coord y", Integer.valueOf((int)item.getPosY() >> 2));
/* 2546 */       toReturn.put("Creator", item.creator);
/* 2547 */       toReturn.put("Creationdate", WurmCalendar.getTimeFor(item.creationDate));
/*      */ 
/*      */       
/* 2550 */       toReturn.put("Description", item.getDescription());
/* 2551 */       toReturn.put("Material", Item.getMaterialString(item.getMaterial()));
/*      */     }
/* 2553 */     catch (Exception ex) {
/*      */       
/* 2555 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 2556 */       throw new RemoteException(ex.getMessage());
/*      */     } 
/*      */     
/* 2559 */     return toReturn;
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
/*      */   public Map<String, String> getPlayerIPAddresses(String intraServerPassword) throws RemoteException {
/* 2576 */     validateIntraServerPassword(intraServerPassword);
/* 2577 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2579 */       logger.finer(getRemoteClientDetails() + " getPlayerIPAddresses");
/*      */     }
/* 2581 */     Map<String, String> toReturn = new HashMap<>();
/* 2582 */     Player[] playerArr = Players.getInstance().getPlayersByIp();
/*      */     
/* 2584 */     for (int x = 0; x < playerArr.length; x++) {
/*      */       
/* 2586 */       if (playerArr[x].getSaveFile().getPower() == 0)
/*      */       {
/* 2588 */         toReturn.put(playerArr[x].getName(), playerArr[x].getSaveFile().getIpaddress());
/*      */       }
/*      */     } 
/* 2591 */     return toReturn;
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
/*      */   public Map<String, String> getNameBans(String intraServerPassword) throws RemoteException {
/* 2609 */     validateIntraServerPassword(intraServerPassword);
/* 2610 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2612 */       logger.finer(getRemoteClientDetails() + " getNameBans");
/*      */     }
/* 2614 */     Map<String, String> toReturn = new HashMap<>();
/* 2615 */     Ban[] bips = Players.getInstance().getPlayersBanned();
/* 2616 */     if (bips.length > 0)
/*      */     {
/* 2618 */       for (int x = 0; x < bips.length; x++) {
/*      */         
/* 2620 */         long daytime = bips[x].getExpiry() - System.currentTimeMillis();
/* 2621 */         toReturn.put(bips[x].getIdentifier(), Server.getTimeFor(daytime) + ", " + bips[x].getReason());
/*      */       } 
/*      */     }
/* 2624 */     return toReturn;
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
/*      */   public Map<String, String> getIPBans(String intraServerPassword) throws RemoteException {
/* 2643 */     validateIntraServerPassword(intraServerPassword);
/* 2644 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2646 */       logger.finer(getRemoteClientDetails() + " getIPBans");
/*      */     }
/* 2648 */     Map<String, String> toReturn = new HashMap<>();
/* 2649 */     Ban[] bips = Players.getInstance().getBans();
/* 2650 */     if (bips.length > 0)
/*      */     {
/* 2652 */       for (int x = 0; x < bips.length; x++) {
/*      */         
/* 2654 */         long daytime = bips[x].getExpiry() - System.currentTimeMillis();
/* 2655 */         toReturn.put(bips[x].getIdentifier(), Server.getTimeFor(daytime) + ", " + bips[x].getReason());
/*      */       } 
/*      */     }
/* 2658 */     return toReturn;
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
/*      */   public Map<String, String> getWarnings(String intraServerPassword) throws RemoteException {
/* 2675 */     validateIntraServerPassword(intraServerPassword);
/* 2676 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2678 */       logger.finer(getRemoteClientDetails() + " getWarnings");
/*      */     }
/* 2680 */     Map<String, String> toReturn = new HashMap<>();
/* 2681 */     toReturn.put("Not implemented", "Need a name to check.");
/* 2682 */     return toReturn;
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
/*      */   public String getWurmTime(String intraServerPassword) throws RemoteException {
/* 2695 */     validateIntraServerPassword(intraServerPassword);
/* 2696 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2698 */       logger.finer(getRemoteClientDetails() + " getWurmTime");
/*      */     }
/* 2700 */     return WurmCalendar.getTime();
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
/*      */   public String getUptime(String intraServerPassword) throws RemoteException {
/* 2715 */     validateIntraServerPassword(intraServerPassword);
/* 2716 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2718 */       logger.finer(getRemoteClientDetails() + " getUptime");
/*      */     }
/* 2720 */     return Server.getTimeFor(System.currentTimeMillis() - Server.getStartTime());
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
/*      */   public String getNews(String intraServerPassword) throws RemoteException {
/* 2733 */     validateIntraServerPassword(intraServerPassword);
/* 2734 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2736 */       logger.finer(getRemoteClientDetails() + " getNews");
/*      */     }
/* 2738 */     return NewsInfo.getInfo();
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
/*      */   public String getGameInfo(String intraServerPassword) throws RemoteException {
/* 2752 */     validateIntraServerPassword(intraServerPassword);
/* 2753 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2755 */       logger.finer(getRemoteClientDetails() + " getGameInfo");
/*      */     }
/* 2757 */     return WurmInfo.getInfo() + WurmInfo2.getInfo();
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
/*      */   public Map<String, String> getKingdomInfluence(String intraServerPassword) throws RemoteException {
/* 2776 */     validateIntraServerPassword(intraServerPassword);
/* 2777 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2779 */       logger.finer(getRemoteClientDetails() + " getKingdomInfluence");
/*      */     }
/* 2781 */     Map<String, String> toReturn = new HashMap<>();
/* 2782 */     Zones.calculateZones(false);
/* 2783 */     Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 2784 */     for (int x = 0; x < kingdoms.length; x++)
/*      */     {
/* 2786 */       toReturn.put("Percent controlled by " + kingdoms[x].getName(), twoDecimals
/* 2787 */           .format(Zones.getPercentLandForKingdom(kingdoms[x].getId())));
/*      */     }
/* 2789 */     return toReturn;
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
/*      */   public Map<String, ?> getMerchantSummary(String intraServerPassword, long aWurmID) throws RemoteException {
/* 2817 */     validateIntraServerPassword(intraServerPassword);
/* 2818 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2820 */       logger.finer(getRemoteClientDetails() + " getMerchantSummary for WurmID: " + aWurmID);
/*      */     }
/* 2822 */     Map<String, Object> toReturn = new HashMap<>();
/* 2823 */     toReturn.put("Not implemented", "not yet");
/* 2824 */     return toReturn;
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
/*      */   
/*      */   public Map<String, ?> getBankAccount(String intraServerPassword, long aPlayerID) throws RemoteException {
/* 2858 */     validateIntraServerPassword(intraServerPassword);
/* 2859 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2861 */       logger.finer(getRemoteClientDetails() + " getBankAccount for playerid: " + aPlayerID);
/*      */     }
/* 2863 */     Map<String, Object> toReturn = new HashMap<>();
/* 2864 */     logger.log(Level.INFO, "GetBankAccount " + aPlayerID);
/*      */     
/*      */     try {
/* 2867 */       Bank lBank = Banks.getBank(aPlayerID);
/* 2868 */       if (lBank != null) {
/*      */         
/* 2870 */         toReturn.put("BankID", Long.valueOf(lBank.id));
/* 2871 */         toReturn.put("Owner", Long.valueOf(lBank.owner));
/* 2872 */         toReturn.put("StartedMoving", Long.valueOf(lBank.startedMoving));
/* 2873 */         toReturn.put("Open", Boolean.valueOf(lBank.open));
/* 2874 */         toReturn.put("Size", Integer.valueOf(lBank.size));
/*      */         
/*      */         try {
/* 2877 */           Village lCurrentVillage = lBank.getCurrentVillage();
/* 2878 */           if (lCurrentVillage != null)
/*      */           {
/* 2880 */             toReturn.put("CurrentVillageID", Integer.valueOf(lCurrentVillage.getId()));
/* 2881 */             toReturn.put("CurrentVillageName", lCurrentVillage.getName());
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 2887 */         catch (BankUnavailableException bankUnavailableException) {}
/*      */ 
/*      */ 
/*      */         
/* 2891 */         int lTargetVillageID = lBank.targetVillage;
/*      */ 
/*      */         
/* 2894 */         if (lTargetVillageID > 0)
/*      */         {
/* 2896 */           toReturn.put("TargetVillageID", Integer.valueOf(lTargetVillageID));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2901 */         BankSlot[] lSlots = lBank.slots;
/* 2902 */         if (lSlots != null && lSlots.length > 0)
/*      */         {
/* 2904 */           Map<Long, String> lItemsMap = new HashMap<>(lSlots.length + 1);
/* 2905 */           for (int i = 0; i < lSlots.length; i++) {
/*      */             
/* 2907 */             if (lSlots[i] == null) {
/*      */               
/* 2909 */               logger.log(Level.INFO, "Weird. Bank Slot " + i + " is null for " + aPlayerID);
/*      */             }
/*      */             else {
/*      */               
/* 2913 */               Item lItem = (lSlots[i]).item;
/* 2914 */               if (lItem != null)
/*      */               {
/* 2916 */                 lItemsMap.put(Long.valueOf(lItem.getWurmId()), lItem.getName() + ", Inserted: " + (lSlots[i]).inserted + ", Stasis: " + (lSlots[i]).stasis);
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/* 2921 */           if (lItemsMap != null && lItemsMap.size() > 0)
/*      */           {
/* 2923 */             toReturn.put("Items", lItemsMap);
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2929 */         toReturn.put("Error", "Cannot find bank for player ID " + aPlayerID);
/*      */       }
/*      */     
/* 2932 */     } catch (RuntimeException e) {
/*      */       
/* 2934 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), e);
/*      */ 
/*      */       
/* 2937 */       toReturn.put("Error", "Problem getting bank account for player ID " + aPlayerID + ", " + e);
/*      */     } 
/* 2939 */     return toReturn;
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
/*      */   public Map<String, ?> authenticateUser(String intraServerPassword, String playerName, String emailAddress, String hashedIngamePassword, Map params) throws RemoteException {
/* 2966 */     validateIntraServerPassword(intraServerPassword);
/* 2967 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 2969 */       logger.finer(getRemoteClientDetails() + " authenticateUser for player name: " + playerName);
/*      */     }
/* 2971 */     Map<String, Object> toReturn = new HashMap<>();
/* 2972 */     if (Constants.maintaining) {
/*      */       
/* 2974 */       toReturn.put("ResponseCode0", "NOTOK");
/* 2975 */       toReturn.put("ErrorMessage0", "The server is currently unavailable.");
/* 2976 */       toReturn.put("display_text0", "The server is in maintenance mode. Please try later.");
/* 2977 */       return toReturn;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 2982 */       boolean ver = false;
/* 2983 */       Object answer = params.get("VerifiedPayPalAccount");
/* 2984 */       if (answer != null && answer instanceof Boolean)
/*      */       {
/* 2986 */         ver = ((Boolean)answer).booleanValue();
/*      */       }
/* 2988 */       boolean rev = false;
/* 2989 */       answer = params.get("ChargebackOrReversal");
/* 2990 */       if (answer != null && answer instanceof Boolean)
/*      */       {
/* 2992 */         rev = ((Boolean)answer).booleanValue();
/*      */       }
/* 2994 */       Date lastReversal = (Date)params.get("LastChargebackOrReversal");
/*      */       
/* 2996 */       Date first = (Date)params.get("FirstTransactionDate");
/* 2997 */       Date last = (Date)params.get("LastTransactionDate");
/* 2998 */       int total = 0;
/* 2999 */       answer = params.get("TotalEurosSuccessful");
/* 3000 */       if (answer != null && answer instanceof Integer) {
/*      */         
/* 3002 */         total = ((Integer)answer).intValue();
/* 3003 */         if (total < 0)
/*      */         {
/* 3005 */           total = 0;
/*      */         }
/*      */       } 
/* 3008 */       int lastMonthEuros = 0;
/* 3009 */       answer = params.get("LastMonthEurosSuccessful");
/* 3010 */       if (answer != null && answer instanceof Integer) {
/*      */         
/* 3012 */         lastMonthEuros = ((Integer)answer).intValue();
/* 3013 */         if (lastMonthEuros < 0)
/*      */         {
/* 3015 */           lastMonthEuros = 0;
/*      */         }
/*      */       } 
/* 3018 */       String ipAddress = (String)params.get("IP");
/* 3019 */       if (ipAddress != null) {
/*      */         
/* 3021 */         logger.log(Level.INFO, "IP:" + ipAddress);
/* 3022 */         Long lastAttempt = ipAttempts.get(ipAddress);
/* 3023 */         if (lastAttempt != null)
/*      */         {
/* 3025 */           if (System.currentTimeMillis() - lastAttempt.longValue() < 5000L) {
/*      */             
/* 3027 */             toReturn.put("ResponseCode0", "NOTOK");
/* 3028 */             toReturn.put("ErrorMessage0", "Too many logon attempts. Please try again in a few seconds.");
/* 3029 */             toReturn.put("display_text0", "Too many logon attempts. Please try again in a few seconds.");
/* 3030 */             return toReturn;
/*      */           } 
/*      */         }
/* 3033 */         ipAttempts.put(ipAddress, Long.valueOf(System.currentTimeMillis()));
/*      */       } 
/* 3035 */       PlayerInfo file = PlayerInfoFactory.createPlayerInfo(playerName);
/* 3036 */       if (file.undeadType != 0) {
/*      */         
/* 3038 */         toReturn.put("ResponseCode0", "NOTOK");
/* 3039 */         toReturn.put("ErrorMessage0", "Undeads not allowed in here!");
/* 3040 */         toReturn.put("display_text0", "Undeads not allowed in here!");
/* 3041 */         return toReturn;
/*      */       } 
/*      */       
/*      */       try {
/* 3045 */         file.load();
/* 3046 */         if (file.undeadType != 0)
/*      */         {
/* 3048 */           toReturn.put("ResponseCode0", "NOTOK");
/* 3049 */           toReturn.put("ErrorMessage0", "Undeads not allowed in here!");
/* 3050 */           toReturn.put("display_text0", "Undeads not allowed in here!");
/* 3051 */           return toReturn;
/*      */         }
/*      */       
/* 3054 */       } catch (IOException iox) {
/*      */         
/* 3056 */         toReturn.put("ResponseCode0", "NOTOK");
/* 3057 */         toReturn.put("ErrorMessage0", "An error occurred when loading your account.");
/* 3058 */         toReturn.put("display_text0", "An error occurred when loading your account.");
/* 3059 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/* 3060 */         return toReturn;
/*      */       } 
/* 3062 */       if (!file.overRideShop && rev)
/*      */       {
/*      */         
/* 3065 */         if (lastReversal == null || last == null || lastReversal.after(last)) {
/*      */           
/* 3067 */           toReturn.put("ResponseCode0", "NOTOK");
/* 3068 */           toReturn.put("ErrorMessage0", "This paypal account has reversed transactions registered.");
/* 3069 */           toReturn.put("display_text0", "This paypal account has reversed transactions registered.");
/* 3070 */           return toReturn;
/*      */         } 
/*      */       }
/* 3073 */       toReturn = (Map)authenticateUser(intraServerPassword, playerName, emailAddress, hashedIngamePassword);
/* 3074 */       Integer max = (Integer)toReturn.get("maximum_silver0");
/* 3075 */       if (max != null) {
/*      */         
/* 3077 */         int maxval = max.intValue();
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
/* 3105 */         if (file.overRideShop) {
/*      */           
/* 3107 */           maxval = 50 + Math.min(50, (int)(file.playingTime / 3600000L * 3L));
/* 3108 */           toReturn.put("maximum_silver0", Integer.valueOf(maxval));
/*      */         
/*      */         }
/* 3111 */         else if (lastMonthEuros >= 400) {
/*      */           
/* 3113 */           maxval = 0;
/* 3114 */           toReturn.put("maximum_silver0", Integer.valueOf(maxval));
/* 3115 */           toReturn.put("display_text0", "You may only purchase 400 silver via PayPal per month");
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3123 */       return toReturn;
/*      */     }
/* 3125 */     catch (Exception ew) {
/*      */       
/* 3127 */       logger.log(Level.WARNING, "Error: " + ew.getMessage(), ew);
/* 3128 */       toReturn.put("ResponseCode0", "NOTOK");
/* 3129 */       toReturn.put("ErrorMessage0", "An error occured.");
/* 3130 */       return toReturn;
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
/*      */   public Map<String, String> doesPlayerExist(String intraServerPassword, String playerName) throws RemoteException {
/* 3142 */     validateIntraServerPassword(intraServerPassword);
/* 3143 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 3145 */       logger.finer(getRemoteClientDetails() + " doesPlayerExist for player name: " + playerName);
/*      */     }
/* 3147 */     Map<String, String> toReturn = new HashMap<>();
/* 3148 */     if (Constants.maintaining) {
/*      */       
/* 3150 */       toReturn.put("ResponseCode", "NOTOK");
/* 3151 */       toReturn.put("ErrorMessage", "The server is currently unavailable.");
/* 3152 */       toReturn.put("display_text", "The server is currently unavailable.");
/* 3153 */       return toReturn;
/*      */     } 
/* 3155 */     toReturn.put("ResponseCode", "OK");
/* 3156 */     if (playerName != null) {
/*      */       
/* 3158 */       PlayerInfo file = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/* 3161 */         file.load();
/* 3162 */         if (file.wurmId <= 0L)
/*      */         {
/* 3164 */           toReturn.clear();
/* 3165 */           toReturn.put("ResponseCode", "NOTOK");
/* 3166 */           toReturn.put("ErrorMessage", "No such player on the " + Servers.localServer.name + " game server. Maybe it has been deleted due to inactivity.");
/*      */           
/* 3168 */           toReturn.put("display_text", "No such player on the " + Servers.localServer.name + " game server. Maybe it has been deleted due to inactivity.");
/*      */         }
/*      */       
/*      */       }
/* 3172 */       catch (Exception ex) {
/*      */         
/* 3174 */         toReturn.clear();
/* 3175 */         toReturn.put("ResponseCode", "NOTOK");
/* 3176 */         toReturn.put("ErrorMessage", ex.getMessage());
/* 3177 */         toReturn.put("display_text", "An error occurred on the " + Servers.localServer.name + " game server: " + ex
/* 3178 */             .getMessage());
/*      */       } 
/*      */     } 
/* 3181 */     return toReturn;
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
/*      */   public Map<String, ?> authenticateUser(String intraServerPassword, String playerName, String emailAddress, String hashedIngamePassword) throws RemoteException {
/* 3206 */     validateIntraServerPassword(intraServerPassword);
/* 3207 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 3209 */       logger.finer(getRemoteClientDetails() + " authenticateUser for player name: " + playerName);
/*      */     }
/* 3211 */     Map<String, Object> toReturn = new HashMap<>();
/*      */     
/* 3213 */     if (Constants.maintaining) {
/*      */       
/* 3215 */       toReturn.put("ResponseCode0", "NOTOK");
/* 3216 */       toReturn.put("ErrorMessage0", "The server is currently unavailable.");
/* 3217 */       toReturn.put("display_text0", "The server is in maintenance mode. Please try later.");
/* 3218 */       return toReturn;
/*      */     } 
/* 3220 */     if (playerName != null) {
/*      */       
/* 3222 */       PlayerInfo file = PlayerInfoFactory.createPlayerInfo(playerName);
/* 3223 */       if (file.undeadType != 0) {
/*      */         
/* 3225 */         toReturn.put("ResponseCode0", "NOTOK");
/* 3226 */         toReturn.put("ErrorMessage0", "Undeads not allowed in here!");
/* 3227 */         toReturn.put("display_text0", "Undeads not allowed in here!");
/* 3228 */         return toReturn;
/*      */       } 
/*      */       
/*      */       try {
/* 3232 */         file.load();
/* 3233 */         if (file.undeadType != 0) {
/*      */           
/* 3235 */           toReturn.put("ResponseCode0", "NOTOK");
/* 3236 */           toReturn.put("ErrorMessage0", "Undeads not allowed in here!");
/* 3237 */           toReturn.put("display_text0", "Undeads not allowed in here!");
/* 3238 */           return toReturn;
/*      */         } 
/* 3240 */         if (file.wurmId <= 0L)
/*      */         {
/* 3242 */           toReturn.put("ResponseCode0", "NOTOK");
/* 3243 */           toReturn.put("ErrorMessage0", "No such player.");
/*      */         }
/* 3245 */         else if (hashedIngamePassword.equals(file.getPassword()))
/*      */         {
/* 3247 */           if (Servers.isThisLoginServer()) {
/*      */             
/* 3249 */             LoginServerWebConnection lsw = new LoginServerWebConnection(file.currentServer);
/* 3250 */             Map<String, String> m = lsw.doesPlayerExist(playerName);
/* 3251 */             String resp = m.get("ResponseCode");
/* 3252 */             if (resp != null && resp.equals("NOTOK")) {
/*      */               
/* 3254 */               toReturn.put("ResponseCode0", "NOTOK");
/* 3255 */               toReturn.put("ErrorMessage0", m.get("ErrorMessage"));
/* 3256 */               toReturn.put("display_text0", m.get("display_text"));
/* 3257 */               return toReturn;
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 3263 */           toReturn.put("ErrorMessage0", "");
/* 3264 */           if (file.getPaymentExpire() < 0L) {
/* 3265 */             toReturn.put("display_text0", "You are new to the game and may give away an in-game referral to the person who introduced you to Wurm Online using the chat command '/refer' if you purchase premium game time.");
/*      */           }
/*      */           else {
/*      */             
/* 3269 */             toReturn.put("display_text0", "Don't forget to use the in-game '/refer' chat command to refer the one who introduced you to Wurm Online.");
/*      */           } 
/* 3271 */           if (file.getPaymentExpire() < System.currentTimeMillis() + 604800000L) {
/*      */             
/* 3273 */             toReturn.put("display_text0", "You have less than a week left of premium game time so the amount of coins you can purchase is somewhat limited.");
/*      */             
/* 3275 */             toReturn.put("maximum_silver0", Integer.valueOf(10));
/*      */           } else {
/*      */             
/* 3278 */             toReturn.put("maximum_silver0", 
/* 3279 */                 Integer.valueOf(20 + Math.min(100, (int)(file.playingTime / 3600000L * 3L))));
/* 3280 */           }  if (!file.overRideShop && file.isBanned()) {
/*      */             
/* 3282 */             toReturn.put("PurchaseOk0", "NOTOK");
/* 3283 */             toReturn.put("maximum_silver0", Integer.valueOf(0));
/* 3284 */             toReturn.put("display_text0", "You have been banned. Reason: " + file.banreason);
/* 3285 */             toReturn.put("ErrorMessage0", "The player has been banned. Reason: " + file.banreason);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3298 */             toReturn.put("PurchaseOk0", "OK");
/*      */           } 
/*      */           
/* 3301 */           int maxMonths = 0;
/* 3302 */           if (file.getPaymentExpire() > System.currentTimeMillis()) {
/*      */ 
/*      */             
/* 3305 */             long maxMonthsMillis = System.currentTimeMillis() + 36288000000L - file.getPaymentExpire();
/* 3306 */             maxMonths = (int)(maxMonthsMillis / 2419200000L);
/* 3307 */             if (maxMonths < 0)
/*      */             {
/* 3309 */               maxMonths = 0;
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 3314 */             maxMonths = 12;
/*      */           } 
/* 3316 */           toReturn.put("maximum_months0", Integer.valueOf(maxMonths));
/*      */           
/* 3318 */           toReturn.put("new_customer0", Boolean.valueOf((file.getPaymentExpire() <= 0L)));
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3323 */           toReturn.put("ResponseCode0", "OK");
/* 3324 */           toReturn.put("PlayerID0", new Long(file.wurmId));
/* 3325 */           toReturn.put("ingameBankBalance0", new Long(file.money));
/* 3326 */           toReturn.put("PlayingTimeExpire0", new Long(file.getPaymentExpire()));
/*      */         }
/*      */         else
/*      */         {
/* 3330 */           toReturn.put("ResponseCode0", "NOTOK");
/* 3331 */           toReturn.put("ErrorMessage0", "Password does not match.");
/*      */         }
/*      */       
/* 3334 */       } catch (Exception ex) {
/*      */         
/* 3336 */         toReturn.put("ResponseCode0", "NOTOK");
/* 3337 */         toReturn.put("ErrorMessage0", ex.getMessage());
/* 3338 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       }
/*      */     
/* 3341 */     } else if (isEmailValid(emailAddress)) {
/*      */       
/* 3343 */       PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(emailAddress);
/* 3344 */       for (int x = 0; x < infos.length; x++) {
/*      */         
/* 3346 */         if (infos[x].getPassword().equals(hashedIngamePassword))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 3351 */           toReturn.put("ErrorMessage" + x, "");
/* 3352 */           if (infos[x].getPaymentExpire() < System.currentTimeMillis() + 604800000L) {
/* 3353 */             toReturn.put("maximum_silver" + x, Integer.valueOf(10));
/*      */           } else {
/* 3355 */             toReturn.put("maximum_silver" + x, 
/* 3356 */                 Integer.valueOf(10 + Math.min(100, (int)((infos[x]).playingTime / 86400000L))));
/*      */           } 
/* 3358 */           if (!(infos[x]).overRideShop && infos[x].isBanned()) {
/*      */             
/* 3360 */             toReturn.put("PurchaseOk" + x, "NOTOK");
/* 3361 */             toReturn.put("maximum_silver" + x, Integer.valueOf(0));
/* 3362 */             toReturn.put("display_text" + x, "You have been banned. Reason: " + (infos[x]).banreason);
/* 3363 */             toReturn.put("ErrorMessage" + x, "The player has been banned. Reason: " + (infos[x]).banreason);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3374 */             toReturn.put("PurchaseOk" + x, "OK");
/* 3375 */           }  int maxMonths = 0;
/* 3376 */           if (infos[x].getPaymentExpire() > System.currentTimeMillis()) {
/*      */ 
/*      */             
/* 3379 */             long maxMonthsMillis = System.currentTimeMillis() + 36288000000L - infos[x].getPaymentExpire();
/* 3380 */             maxMonths = (int)(maxMonthsMillis / 2419200000L);
/* 3381 */             if (maxMonths < 0)
/*      */             {
/* 3383 */               maxMonths = 0;
/*      */             }
/*      */           }
/*      */           else {
/*      */             
/* 3388 */             maxMonths = 12;
/*      */           } 
/* 3390 */           toReturn.put("maximum_months" + x, Integer.valueOf(maxMonths));
/* 3391 */           toReturn.put("new_customer" + x, Boolean.valueOf((infos[x].getPaymentExpire() <= 0L)));
/*      */ 
/*      */           
/* 3394 */           toReturn.put("ResponseCode" + x, "OK");
/* 3395 */           toReturn.put("PlayerID" + x, new Long((infos[x]).wurmId));
/* 3396 */           toReturn.put("ingameBankBalance" + x, new Long((infos[x]).money));
/* 3397 */           toReturn.put("PlayingTimeExpire" + x, new Long(infos[x].getPaymentExpire()));
/*      */         }
/*      */         else
/*      */         {
/* 3401 */           toReturn.put("ResponseCode" + x, "NOTOK");
/* 3402 */           toReturn.put("ErrorMessage" + x, "Password does not match.");
/*      */         }
/*      */       
/*      */       } 
/*      */     } else {
/*      */       
/* 3408 */       toReturn.put("ResponseCode0", "NOTOK");
/* 3409 */       toReturn.put("ErrorMessage0", "Invalid email: " + emailAddress);
/*      */     } 
/* 3411 */     return toReturn;
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
/*      */   public Map<String, String> changePassword(String intraServerPassword, String playerName, String emailAddress, String newPassword) throws RemoteException {
/* 3435 */     validateIntraServerPassword(intraServerPassword);
/* 3436 */     Map<String, String> toReturn = new HashMap<>();
/*      */ 
/*      */     
/*      */     try {
/* 3440 */       toReturn.put("Result", "Unknown email.");
/* 3441 */       logger.log(Level.INFO, getRemoteClientDetails() + " Changepassword Name: " + playerName + ", email: " + emailAddress);
/*      */       
/* 3443 */       if (emailAddress != null) {
/*      */         
/* 3445 */         if (!isEmailValid(emailAddress)) {
/* 3446 */           toReturn.put("Error", emailAddress + " is an invalid email.");
/*      */         } else {
/*      */           
/* 3449 */           PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(emailAddress);
/* 3450 */           int nums = 0;
/* 3451 */           for (int x = 0; x < infos.length; x++) {
/*      */ 
/*      */ 
/*      */             
/* 3455 */             if (infos[x].getPower() == 0) {
/*      */               
/*      */               try
/*      */               {
/* 3459 */                 infos[x].updatePassword(newPassword);
/* 3460 */                 if ((infos[x]).currentServer != Servers.localServer.id)
/*      */                 {
/* 3462 */                   new PasswordTransfer(infos[x].getName(), (infos[x]).wurmId, infos[x].getPassword(), 
/* 3463 */                       System.currentTimeMillis(), false);
/*      */                 }
/* 3465 */                 nums++;
/* 3466 */                 toReturn.put("Account" + nums, infos[x].getName() + " password was updated.");
/*      */               }
/* 3468 */               catch (IOException iox)
/*      */               {
/* 3470 */                 logger.log(Level.WARNING, "Failed to update password for " + infos[x].getName(), iox);
/* 3471 */                 toReturn.put("Error" + nums, infos[x].getName() + " password was _not_ updated.");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 3476 */               toReturn.put("Error" + nums, "Failed to update password for " + infos[x].getName());
/* 3477 */               logger.warning("Failed to update password for " + infos[x].getName() + " as power is " + infos[x]
/* 3478 */                   .getPower());
/*      */             } 
/*      */           } 
/* 3481 */           if (nums > 0) {
/*      */             
/* 3483 */             toReturn.put("Result", nums + " player accounts were affected.");
/*      */           }
/*      */           else {
/*      */             
/* 3487 */             toReturn.put("Error", nums + " player accounts were affected.");
/*      */           } 
/* 3489 */           return toReturn;
/*      */         }
/*      */       
/* 3492 */       } else if (playerName != null) {
/*      */         
/* 3494 */         PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */         
/*      */         try {
/* 3497 */           p.load();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3507 */           if (isEmailValid(p.emailAddress)) {
/*      */             
/* 3509 */             emailAddress = p.emailAddress;
/* 3510 */             PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(emailAddress);
/* 3511 */             int nums = 0;
/* 3512 */             boolean failed = false;
/* 3513 */             for (int x = 0; x < infos.length; x++) {
/*      */               
/* 3515 */               if (infos[x].getPower() == 0) {
/*      */                 
/*      */                 try
/*      */                 {
/* 3519 */                   infos[x].updatePassword(newPassword);
/* 3520 */                   if ((infos[x]).currentServer != Servers.localServer.id)
/*      */                   {
/* 3522 */                     new PasswordTransfer(infos[x].getName(), (infos[x]).wurmId, infos[x].getPassword(), 
/* 3523 */                         System.currentTimeMillis(), false);
/*      */                   }
/* 3525 */                   nums++;
/* 3526 */                   toReturn.put("Account" + nums, infos[x].getName() + " password was updated.");
/*      */                 }
/* 3528 */                 catch (IOException iox)
/*      */                 {
/* 3530 */                   failed = true;
/* 3531 */                   toReturn.put("Error" + nums, "Failed to update password for a player.");
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 3536 */                 failed = true;
/* 3537 */                 logger.warning("Failed to update password for " + infos[x].getName() + " as power is " + infos[x]
/* 3538 */                     .getPower());
/*      */               } 
/*      */             } 
/* 3541 */             if (nums > 0) {
/*      */               
/* 3543 */               toReturn.put("Result", nums + " player accounts were affected.");
/*      */             }
/*      */             else {
/*      */               
/* 3547 */               toReturn.put("Error", nums + " player accounts were affected.");
/*      */             } 
/* 3549 */             if (failed)
/* 3550 */               logger.log(Level.WARNING, "Failed to update password for one or more accounts."); 
/* 3551 */             return toReturn;
/*      */           } 
/*      */           
/* 3554 */           toReturn.put("Error", emailAddress + " is an invalid email.");
/*      */         }
/* 3556 */         catch (IOException iox) {
/*      */           
/* 3558 */           toReturn.put("Error", "Failed to load player data. Password not changed.");
/* 3559 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */         } 
/*      */       } 
/* 3562 */       return toReturn;
/*      */     }
/*      */     finally {
/*      */       
/* 3566 */       logger.info("Changepassword Name: " + playerName + ", email: " + emailAddress + ", exit: " + toReturn);
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
/*      */   public Map<String, String> changePassword(String intraServerPassword, String playerName, String emailAddress, String hashedOldPassword, String newPassword) throws RemoteException {
/* 3592 */     validateIntraServerPassword(intraServerPassword);
/* 3593 */     Map<String, String> toReturn = new HashMap<>();
/* 3594 */     toReturn.put("Result", "Unknown email.");
/* 3595 */     logger.log(Level.INFO, getRemoteClientDetails() + " Changepassword 2 for player name: " + playerName);
/* 3596 */     if (emailAddress != null) {
/*      */       
/* 3598 */       if (!isEmailValid(emailAddress)) {
/* 3599 */         toReturn.put("Result", emailAddress + " is an invalid email.");
/*      */       } else {
/*      */         
/* 3602 */         PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(emailAddress);
/* 3603 */         boolean ok = false;
/* 3604 */         int nums = 0;
/* 3605 */         for (int x = 0; x < infos.length; x++) {
/*      */           
/* 3607 */           if (infos[x].getPassword().equals(hashedOldPassword))
/*      */           {
/* 3609 */             ok = true;
/*      */           }
/*      */         } 
/* 3612 */         if (ok) {
/*      */           
/* 3614 */           boolean failed = false;
/* 3615 */           for (int i = 0; i < infos.length; i++) {
/*      */             
/* 3617 */             if (infos[i].getPower() == 0) {
/*      */               
/*      */               try
/*      */               {
/* 3621 */                 infos[i].updatePassword(newPassword);
/* 3622 */                 if ((infos[i]).currentServer != Servers.localServer.id)
/*      */                 {
/* 3624 */                   new PasswordTransfer(infos[i].getName(), (infos[i]).wurmId, infos[i].getPassword(), 
/* 3625 */                       System.currentTimeMillis(), false);
/*      */                 }
/* 3627 */                 nums++;
/* 3628 */                 toReturn.put("Account" + nums, infos[i].getName() + " password was updated.");
/*      */               }
/* 3630 */               catch (IOException iox)
/*      */               {
/* 3632 */                 failed = true;
/* 3633 */                 toReturn.put("Error" + nums, "Failed to update password for " + infos[i].getName());
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 3638 */               failed = true;
/* 3639 */               toReturn.put("Error" + nums, infos[i].getName() + " password was _not_ updated.");
/*      */             } 
/*      */           } 
/*      */           
/* 3643 */           if (failed)
/* 3644 */             logger.log(Level.WARNING, "Failed to update password for one or more accounts."); 
/*      */         } 
/* 3646 */         if (nums > 0) {
/*      */           
/* 3648 */           toReturn.put("Result", nums + " player accounts were affected.");
/*      */         }
/*      */         else {
/*      */           
/* 3652 */           toReturn.put("Error", nums + " player accounts were affected.");
/*      */         } 
/* 3654 */         return toReturn;
/*      */       }
/*      */     
/* 3657 */     } else if (playerName != null) {
/*      */       
/* 3659 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/* 3662 */         p.load();
/* 3663 */         boolean ok = false;
/*      */ 
/*      */ 
/*      */         
/* 3667 */         if (isEmailValid(p.emailAddress)) {
/*      */           
/* 3669 */           emailAddress = p.emailAddress;
/* 3670 */           PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(emailAddress);
/* 3671 */           for (int x = 0; x < infos.length; x++) {
/*      */             
/* 3673 */             if (infos[x].getPassword().equals(hashedOldPassword))
/* 3674 */               ok = true; 
/*      */           } 
/* 3676 */           int nums = 0;
/* 3677 */           if (ok) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3686 */             boolean failed = false;
/* 3687 */             for (int i = 0; i < infos.length; i++) {
/*      */               
/* 3689 */               if (infos[i].getPower() == 0) {
/*      */                 
/*      */                 try
/*      */                 {
/* 3693 */                   infos[i].updatePassword(newPassword);
/* 3694 */                   if ((infos[i]).currentServer != Servers.localServer.id)
/*      */                   {
/* 3696 */                     new PasswordTransfer(infos[i].getName(), (infos[i]).wurmId, infos[i].getPassword(), 
/* 3697 */                         System.currentTimeMillis(), false);
/*      */                   }
/* 3699 */                   nums++;
/* 3700 */                   toReturn.put("Account" + nums, infos[i].getName() + " password was updated.");
/*      */                 }
/* 3702 */                 catch (IOException iox)
/*      */                 {
/* 3704 */                   failed = true;
/* 3705 */                   toReturn.put("Error" + i, "Failed to update password for " + infos[i].getName());
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 3710 */                 failed = true;
/*      */               } 
/*      */             } 
/*      */             
/* 3714 */             if (failed)
/* 3715 */               logger.log(Level.WARNING, "Failed to update password for one or more accounts."); 
/*      */           } 
/* 3717 */           if (nums > 0) {
/*      */             
/* 3719 */             toReturn.put("Result", nums + " player accounts were affected.");
/*      */           }
/*      */           else {
/*      */             
/* 3723 */             toReturn.put("Error", nums + " player accounts were affected.");
/*      */           } 
/* 3725 */           return toReturn;
/*      */         } 
/*      */         
/* 3728 */         toReturn.put("Error", emailAddress + " is an invalid email.");
/*      */       }
/* 3730 */       catch (IOException iox) {
/*      */         
/* 3732 */         toReturn.put("Error", "Failed to load player data. Password not changed.");
/* 3733 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/* 3736 */     return toReturn;
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
/*      */   public Map<String, String> changeEmail(String intraServerPassword, String playerName, String oldEmailAddress, String newEmailAddress) throws RemoteException {
/* 3756 */     validateIntraServerPassword(intraServerPassword);
/* 3757 */     Map<String, String> toReturn = new HashMap<>();
/* 3758 */     toReturn.put("Result", "Unknown email.");
/* 3759 */     logger.log(Level.INFO, getRemoteClientDetails() + " Change Email for player name: " + playerName);
/* 3760 */     if (Constants.maintaining) {
/*      */       
/* 3762 */       toReturn.put("Error", "The server is currently unavailable.");
/* 3763 */       toReturn.put("Result", "The server is in maintenance mode. Please try later.");
/* 3764 */       return toReturn;
/*      */     } 
/* 3766 */     if (oldEmailAddress != null) {
/*      */       
/* 3768 */       if (!isEmailValid(oldEmailAddress)) {
/* 3769 */         toReturn.put("Error", "The old email address, " + oldEmailAddress + " is an invalid email.");
/* 3770 */       } else if (!isEmailValid(newEmailAddress)) {
/* 3771 */         toReturn.put("Error", "The new email address, " + newEmailAddress + " is an invalid email.");
/*      */       } else {
/*      */         
/* 3774 */         PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(oldEmailAddress);
/* 3775 */         int nums = 0;
/* 3776 */         for (int x = 0; x < infos.length; x++) {
/*      */           
/* 3778 */           if (infos[x].getPower() == 0) {
/*      */             
/* 3780 */             infos[x].setEmailAddress(newEmailAddress);
/* 3781 */             nums++;
/* 3782 */             toReturn.put("Account" + nums, infos[x].getName() + " account was affected.");
/*      */           } else {
/*      */             
/* 3785 */             toReturn.put("Account" + nums, infos[x].getName() + " account was _not_ affected.");
/*      */           } 
/* 3787 */         }  if (nums > 0) {
/*      */           
/* 3789 */           toReturn.put("Result", nums + " player accounts were affected.");
/*      */         }
/*      */         else {
/*      */           
/* 3793 */           toReturn.put("Error", nums + " player accounts were affected.");
/*      */         } 
/*      */       } 
/* 3796 */       return toReturn;
/*      */     } 
/* 3798 */     if (playerName != null) {
/*      */       
/* 3800 */       PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */       
/*      */       try {
/* 3803 */         p.load();
/* 3804 */         if (!isEmailValid(newEmailAddress)) {
/* 3805 */           toReturn.put("Error", "The new email address, " + newEmailAddress + " is an invalid email.");
/*      */         } else {
/*      */           
/* 3808 */           oldEmailAddress = p.emailAddress;
/* 3809 */           PlayerInfo[] infos = PlayerInfoFactory.getPlayerInfosWithEmail(oldEmailAddress);
/* 3810 */           int nums = 0;
/* 3811 */           for (int x = 0; x < infos.length; x++) {
/*      */             
/* 3813 */             if (infos[x].getPower() == 0) {
/*      */               
/* 3815 */               infos[x].setEmailAddress(newEmailAddress);
/* 3816 */               nums++;
/* 3817 */               toReturn.put("Account" + nums, infos[x].getName() + " account was affected.");
/*      */             } else {
/*      */               
/* 3820 */               toReturn.put("Account" + nums, infos[x].getName() + " account was _not_ affected.");
/*      */             } 
/* 3822 */           }  if (nums > 0) {
/*      */             
/* 3824 */             toReturn.put("Result", nums + " player accounts were affected.");
/*      */           }
/*      */           else {
/*      */             
/* 3828 */             toReturn.put("Error", nums + " player accounts were affected.");
/*      */           } 
/* 3830 */           return toReturn;
/*      */         }
/*      */       
/* 3833 */       } catch (IOException iox) {
/*      */         
/* 3835 */         toReturn.put("Error", "Failed to load player data. Email not changed.");
/* 3836 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*      */     } 
/* 3839 */     return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getChallengePhrase(String intraServerPassword, String playerName) throws RemoteException {
/* 3850 */     validateIntraServerPassword(intraServerPassword);
/* 3851 */     if (playerName.contains("@")) {
/*      */       
/* 3853 */       PlayerInfo[] pinfos = PlayerInfoFactory.getPlayerInfosForEmail(playerName);
/* 3854 */       if (pinfos.length > 0)
/* 3855 */         return (pinfos[0]).pwQuestion; 
/* 3856 */       return "Incorrect email.";
/*      */     } 
/* 3858 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 3860 */       logger.finer(getRemoteClientDetails() + " getChallengePhrase for player name: " + playerName);
/*      */     }
/* 3862 */     PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */     
/*      */     try {
/* 3865 */       p.load();
/* 3866 */       return p.pwQuestion;
/*      */     }
/* 3868 */     catch (IOException iox) {
/*      */       
/* 3870 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/* 3871 */       return "Error";
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
/*      */   public String[] getPlayerNamesForEmail(String intraServerPassword, String emailAddress) throws RemoteException {
/* 3883 */     validateIntraServerPassword(intraServerPassword);
/* 3884 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 3886 */       logger.finer(getRemoteClientDetails() + " getPlayerNamesForEmail: " + emailAddress);
/*      */     }
/*      */     
/* 3889 */     String[] nameArray = PlayerInfoFactory.getAccountsForEmail(emailAddress);
/* 3890 */     return nameArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEmailAddress(String intraServerPassword, String playerName) throws RemoteException {
/* 3901 */     validateIntraServerPassword(intraServerPassword);
/* 3902 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 3904 */       logger.finer(getRemoteClientDetails() + " getEmailAddress for player name: " + playerName);
/*      */     }
/*      */     
/* 3907 */     PlayerInfo p = PlayerInfoFactory.createPlayerInfo(playerName);
/*      */     
/*      */     try {
/* 3910 */       p.load();
/* 3911 */       return p.emailAddress;
/*      */     }
/* 3913 */     catch (IOException iox) {
/*      */       
/* 3915 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/* 3916 */       return "Error";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String generateRandomPassword() {
/* 3927 */     Random rand = new Random();
/*      */     
/* 3929 */     int length = rand.nextInt(3) + 6;
/* 3930 */     char[] password = new char[length];
/*      */     
/* 3932 */     for (int x = 0; x < length; x++) {
/*      */       
/* 3934 */       int randDecimalAsciiVal = rand.nextInt("abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789".length());
/* 3935 */       password[x] = "abcdefgijkmnopqrstwxyzABCDEFGHJKLMNPQRSTWXYZ23456789".charAt(randDecimalAsciiVal);
/*      */     } 
/*      */     
/* 3938 */     return String.valueOf(password);
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
/*      */   public static final boolean isEmailValid(String emailAddress) {
/* 3950 */     if (emailAddress == null)
/* 3951 */       return false; 
/* 3952 */     Matcher m = VALID_EMAIL_PATTERN.matcher(emailAddress);
/* 3953 */     return m.matches();
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
/*      */   public Map<String, String> requestPasswordReset(String intraServerPassword, String email, String challengePhraseAnswer) throws RemoteException {
/* 3979 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 3981 */     Map<String, String> toReturn = new HashMap<>();
/* 3982 */     if (Constants.maintaining) {
/*      */       
/* 3984 */       toReturn.put("Error0", "The server is currently in maintenance mode.");
/* 3985 */       return toReturn;
/*      */     } 
/* 3987 */     boolean ok = false;
/*      */     
/* 3989 */     String password = generateRandomPassword();
/* 3990 */     String playernames = "";
/* 3991 */     logger.log(Level.INFO, getRemoteClientDetails() + " Password reset for email/name: " + email);
/* 3992 */     if (challengePhraseAnswer == null || challengePhraseAnswer.length() < 1) {
/*      */       
/* 3994 */       toReturn.put("Error0", "The answer is too short.");
/* 3995 */       return toReturn;
/*      */     } 
/* 3997 */     if (!email.contains("@")) {
/*      */       
/* 3999 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(email);
/* 4000 */       if (!pinf.loaded) {
/*      */         
/*      */         try {
/* 4003 */           pinf.load();
/*      */           
/* 4005 */           logger.log(Level.INFO, email + " " + challengePhraseAnswer + " compares to " + pinf.pwAnswer);
/* 4006 */           if (System.currentTimeMillis() - pinf.lastRequestedPassword > 60000L) {
/*      */             
/* 4008 */             logger.log(Level.INFO, email + " time ok. comparing.");
/* 4009 */             if (pinf.pwAnswer.equalsIgnoreCase(challengePhraseAnswer))
/*      */             {
/* 4011 */               logger.log(Level.INFO, email + " challenge answer correct.");
/*      */               
/* 4013 */               ok = true;
/* 4014 */               playernames = pinf.getName();
/* 4015 */               pinf.updatePassword(password);
/* 4016 */               if (pinf.currentServer != Servers.localServer.id)
/*      */               {
/* 4018 */                 new PasswordTransfer(pinf.getName(), pinf.wurmId, pinf.getPassword(), 
/* 4019 */                     System.currentTimeMillis(), false);
/*      */               
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 4027 */             toReturn.put("Error", "Please try again in a minute.");
/* 4028 */             return toReturn;
/*      */           } 
/*      */           
/* 4031 */           pinf.lastRequestedPassword = System.currentTimeMillis();
/*      */         }
/* 4033 */         catch (IOException iox) {
/*      */           
/* 4035 */           logger.log(Level.WARNING, email + ":" + iox.getMessage(), iox);
/* 4036 */           toReturn.put("Error", "An error occured. Please try later.");
/* 4037 */           return toReturn;
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/* 4042 */       PlayerInfo[] p = PlayerInfoFactory.getPlayerInfosWithEmail(email);
/* 4043 */       for (int x = 0; x < p.length; x++) {
/*      */ 
/*      */         
/*      */         try {
/* 4047 */           p[x].load();
/*      */           
/* 4049 */           if ((p[x]).pwAnswer.toLowerCase().equals(challengePhraseAnswer.toLowerCase()) || ((p[x]).pwAnswer
/* 4050 */             .length() == 0 && (p[x]).pwQuestion.length() == 0))
/*      */           {
/* 4052 */             if (System.currentTimeMillis() - (p[x]).lastRequestedPassword > 60000L) {
/*      */               
/* 4054 */               ok = true;
/* 4055 */               if (playernames.length() > 0) {
/* 4056 */                 playernames = playernames + ", " + p[x].getName();
/*      */               } else {
/* 4058 */                 playernames = p[x].getName();
/* 4059 */               }  p[x].updatePassword(password);
/* 4060 */               if ((p[x]).currentServer != Servers.localServer.id)
/*      */               {
/* 4062 */                 new PasswordTransfer(p[x].getName(), (p[x]).wurmId, p[x].getPassword(), 
/* 4063 */                     System.currentTimeMillis(), false);
/*      */ 
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 4069 */             else if (!ok) {
/*      */               
/* 4071 */               toReturn.put("Error", "Please try again in a minute.");
/* 4072 */               return toReturn;
/*      */             } 
/*      */           }
/*      */           
/* 4076 */           (p[x]).lastRequestedPassword = System.currentTimeMillis();
/*      */         }
/* 4078 */         catch (IOException iox) {
/*      */           
/* 4080 */           logger.log(Level.WARNING, email + ":" + iox.getMessage(), iox);
/* 4081 */           toReturn.put("Error", "An error occured. Please try later.");
/* 4082 */           return toReturn;
/*      */         } 
/*      */       } 
/*      */     } 
/* 4086 */     if (ok) {
/* 4087 */       toReturn.put("Result", "Password was changed.");
/*      */     } else {
/* 4089 */       toReturn.put("Error", "Password was not changed.");
/* 4090 */     }  if (playernames.length() > 0) {
/*      */       
/*      */       try
/*      */       {
/* 4094 */         String mail = Mailer.getPasswordMail();
/* 4095 */         mail = mail.replace("@pname", playernames);
/* 4096 */         mail = mail.replace("@password", password);
/* 4097 */         Mailer.sendMail(mailAccount, email, "Wurm Online password request", mail);
/* 4098 */         toReturn.put("MailResult", "A mail was sent to the mail adress: " + email + " for " + playernames + ".");
/*      */       }
/* 4100 */       catch (Exception ex)
/*      */       {
/* 4102 */         logger.log(Level.WARNING, email + ":" + ex.getMessage(), ex);
/* 4103 */         toReturn.put("MailError", "An error occured - " + ex.getMessage() + ". Please try later.");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 4108 */       toReturn.put("Error", "Wrong answer.");
/* 4109 */       return toReturn;
/*      */     } 
/* 4111 */     return toReturn;
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
/*      */   public Map<Integer, String> getAllServers(String intraServerPassword) throws RemoteException {
/* 4131 */     validateIntraServerPassword(intraServerPassword);
/*      */ 
/*      */     
/* 4134 */     return getAllServerInternalAddresses(intraServerPassword);
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
/*      */   public Map<Integer, String> getAllServerInternalAddresses(String intraServerPassword) throws RemoteException {
/* 4152 */     validateIntraServerPassword(intraServerPassword);
/* 4153 */     Map<Integer, String> toReturn = new HashMap<>();
/* 4154 */     ServerEntry[] entries = Servers.getAllServers();
/* 4155 */     for (int x = 0; x < entries.length; x++)
/*      */     {
/* 4157 */       toReturn.put(Integer.valueOf((entries[x]).id), (entries[x]).INTRASERVERADDRESS);
/*      */     }
/* 4159 */     return toReturn;
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
/*      */   public boolean sendMail(String intraServerPassword, String sender, String receiver, String subject, String text) throws RemoteException {
/* 4178 */     validateIntraServerPassword(intraServerPassword);
/* 4179 */     if (!isEmailValid(sender))
/* 4180 */       return false; 
/* 4181 */     if (!isEmailValid(receiver)) {
/* 4182 */       return false;
/*      */     }
/*      */     
/*      */     try {
/* 4186 */       Mailer.sendMail(sender, receiver, subject, text);
/*      */     }
/* 4188 */     catch (Exception ex) {
/*      */       
/* 4190 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 4191 */       return false;
/*      */     } 
/* 4193 */     return true;
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
/*      */   public void shutDown(String intraServerPassword, String playerName, String password, String reason, int seconds) throws RemoteException {
/* 4215 */     validateIntraServerPassword(intraServerPassword);
/* 4216 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/* 4218 */       logger.fine(getRemoteClientDetails() + " shutDown by player name: " + playerName);
/*      */     }
/* 4220 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(LoginHandler.raiseFirstLetter(playerName));
/*      */     
/*      */     try {
/* 4223 */       pinf.load();
/* 4224 */       if (pinf.getPower() >= 4) {
/*      */ 
/*      */         
/*      */         try {
/* 4228 */           String pw = LoginHandler.hashPassword(password, LoginHandler.encrypt(LoginHandler.raiseFirstLetter(pinf.getName())));
/* 4229 */           if (pw.equals(pinf.getPassword())) {
/*      */ 
/*      */             
/* 4232 */             logger.log(Level.INFO, getRemoteClientDetails() + " player: " + playerName + " initiated shutdown in " + seconds + " seconds: " + reason);
/*      */             
/* 4234 */             if (seconds <= 0) {
/* 4235 */               Server.getInstance().shutDown();
/*      */             } else {
/* 4237 */               Server.getInstance().startShutdown(seconds, reason);
/*      */             } 
/*      */           } else {
/*      */             
/* 4241 */             logger.log(Level.WARNING, getRemoteClientDetails() + " player: " + playerName + " denied shutdown due to wrong password.");
/*      */           }
/*      */         
/*      */         }
/* 4245 */         catch (Exception ex) {
/*      */           
/* 4247 */           logger.log(Level.INFO, "Failed to encrypt password for player " + playerName, ex);
/*      */         } 
/*      */       } else {
/*      */         
/* 4251 */         logger.log(Level.INFO, getRemoteClientDetails() + " player: " + playerName + " DENIED shutdown in " + seconds + " seconds: " + reason);
/*      */       }
/*      */     
/* 4254 */     } catch (IOException iox) {
/*      */       
/* 4256 */       logger.log(Level.INFO, getRemoteClientDetails() + " player: " + playerName + ": " + iox.getMessage(), iox);
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
/*      */   public Map<String, Byte> getReferrers(String intraServerPassword, long wurmid) throws RemoteException {
/* 4268 */     validateIntraServerPassword(intraServerPassword);
/* 4269 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4271 */       logger.finer(getRemoteClientDetails() + " getReferrers for WurmID: " + wurmid);
/*      */     }
/* 4273 */     return PlayerInfoFactory.getReferrers(wurmid);
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
/*      */   public String addReferrer(String intraServerPassword, String receiver, long referrer) throws RemoteException {
/* 4285 */     validateIntraServerPassword(intraServerPassword);
/* 4286 */     logger.info(getRemoteClientDetails() + " addReferrer for Receiver player name: " + receiver + ", referrerID: " + referrer);
/*      */     
/* 4288 */     synchronized (Server.SYNC_LOCK) {
/*      */ 
/*      */ 
/*      */       
/* 4292 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(receiver);
/*      */       
/*      */       try {
/* 4295 */         pinf.load();
/*      */       }
/* 4297 */       catch (IOException iox) {
/*      */         
/* 4299 */         return receiver + " - no such player exists. Please check the spelling.";
/*      */       } 
/* 4301 */       if (pinf.wurmId == referrer)
/* 4302 */         return "You may not refer yourself."; 
/* 4303 */       if (pinf.getPaymentExpire() <= 0L)
/* 4304 */         return pinf.getName() + " has never had a premium account and may not receive referrals."; 
/* 4305 */       if (PlayerInfoFactory.addReferrer(pinf.wurmId, referrer)) {
/* 4306 */         return String.valueOf(pinf.wurmId);
/*      */       }
/* 4308 */       return "You have already awarded referral to that player.";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String acceptReferrer(String intraServerPassword, long wurmid, String awarderName, boolean money) throws RemoteException {
/* 4327 */     validateIntraServerPassword(intraServerPassword);
/* 4328 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/* 4330 */       logger.fine(getRemoteClientDetails() + " acceptReferrer for player wurmid: " + wurmid + ", awarderName: " + awarderName + ", money: " + money);
/*      */     }
/*      */     
/* 4333 */     String name = awarderName;
/* 4334 */     PlayerInfo pinf = null;
/*      */     
/*      */     try {
/* 4337 */       long l = Long.parseLong(awarderName);
/* 4338 */       pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(l);
/*      */     }
/* 4340 */     catch (NumberFormatException nfe) {
/*      */       
/* 4342 */       pinf = PlayerInfoFactory.createPlayerInfo(name);
/*      */       
/*      */       try {
/* 4345 */         pinf.load();
/*      */       }
/* 4347 */       catch (IOException iox) {
/*      */         
/* 4349 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/* 4350 */         return "Failed to locate the player " + awarderName + " in the database.";
/*      */       } 
/*      */     } 
/* 4353 */     if (pinf != null) {
/*      */ 
/*      */       
/*      */       try {
/* 4357 */         synchronized (Server.SYNC_LOCK) {
/*      */           
/* 4359 */           if (PlayerInfoFactory.acceptReferer(wurmid, pinf.wurmId, money)) {
/*      */ 
/*      */             
/*      */             try {
/* 4363 */               if (money) {
/*      */                 
/* 4365 */                 PlayerInfoFactory.addMoneyToBank(wurmid, 30000L, "Referred by " + pinf.getName());
/*      */               } else {
/*      */                 
/* 4368 */                 PlayerInfoFactory.addPlayingTime(wurmid, 0, 20, "Referred by " + pinf.getName());
/*      */               } 
/* 4370 */             } catch (Exception ex) {
/*      */               
/* 4372 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/* 4373 */               PlayerInfoFactory.revertReferer(wurmid, pinf.wurmId);
/* 4374 */               return "An error occured. Please try later or post a bug report.";
/*      */             } 
/*      */           } else {
/*      */             
/* 4378 */             return "Failed to match " + awarderName + " to any existing referral.";
/*      */           }
/*      */         
/*      */         } 
/* 4382 */       } catch (Exception ex) {
/*      */         
/* 4384 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/* 4385 */         return "An error occured. Please try later or post a bug report.";
/*      */       } 
/*      */     } else {
/*      */       
/* 4389 */       return "Failed to locate " + awarderName + " in the database.";
/* 4390 */     }  return "Okay, accepted the referral from " + awarderName + ". The reward will arrive soon if it has not already.";
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
/*      */   public Map<String, Double> getSkillStats(String intraServerPassword, int skillid) throws RemoteException {
/* 4404 */     validateIntraServerPassword(intraServerPassword);
/* 4405 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4407 */       logger.finer(getRemoteClientDetails() + " getSkillStats for skillid: " + skillid);
/*      */     }
/* 4409 */     Map<String, Double> toReturn = new HashMap<>();
/*      */     
/*      */     try {
/* 4412 */       SkillStat sk = SkillStat.getSkillStatForSkill(skillid);
/*      */       
/* 4414 */       for (Iterator<Map.Entry<Long, Double>> it = sk.stats.entrySet().iterator(); it.hasNext(); )
/*      */       {
/* 4416 */         Map.Entry<Long, Double> entry = it.next();
/* 4417 */         Long lid = entry.getKey();
/* 4418 */         long pid = lid.longValue();
/* 4419 */         PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(pid);
/* 4420 */         if (p != null)
/*      */         {
/* 4422 */           if (((Double)entry.getValue()).doubleValue() > 1.0D)
/*      */           {
/* 4424 */             toReturn.put(p.getName(), entry.getValue());
/*      */           }
/*      */         }
/*      */       }
/*      */     
/* 4429 */     } catch (Exception ex) {
/*      */       
/* 4431 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/* 4432 */       toReturn.put("ERROR: " + ex.getMessage(), Double.valueOf(0.0D));
/*      */     } 
/* 4434 */     return toReturn;
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
/*      */   public Map<Integer, String> getSkills(String intraServerPassword) throws RemoteException {
/* 4448 */     validateIntraServerPassword(intraServerPassword);
/* 4449 */     return SkillSystem.skillNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ?> getStructureSummary(String intraServerPassword, long aStructureID) throws RemoteException {
/* 4460 */     validateIntraServerPassword(intraServerPassword);
/* 4461 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4463 */       logger.finer(getRemoteClientDetails() + " getStructureSummary for StructureID: " + aStructureID);
/*      */     }
/* 4465 */     Map<String, Object> lToReturn = new HashMap<>(10);
/*      */     
/*      */     try {
/* 4468 */       Structure lStructure = Structures.getStructure(aStructureID);
/* 4469 */       if (lStructure != null)
/*      */       {
/* 4471 */         lToReturn.put("CenterX", Integer.valueOf(lStructure.getCenterX()));
/* 4472 */         lToReturn.put("CenterY", Integer.valueOf(lStructure.getCenterY()));
/*      */         
/* 4474 */         lToReturn.put("CreationDate", Long.valueOf(lStructure.getCreationDate()));
/* 4475 */         lToReturn.put("Door Count", Integer.valueOf(lStructure.getDoors()));
/* 4476 */         lToReturn.put("FinalFinished", Boolean.valueOf(lStructure.isFinalFinished()));
/* 4477 */         lToReturn.put("Finalized", Boolean.valueOf(lStructure.isFinalized()));
/* 4478 */         lToReturn.put("Finished", Boolean.valueOf(lStructure.isFinished()));
/* 4479 */         lToReturn.put("Guest Count", Integer.valueOf(lStructure.getPermissionsPlayerList().size()));
/* 4480 */         lToReturn.put("Limit", Integer.valueOf(lStructure.getLimit()));
/* 4481 */         lToReturn.put("Lockable", Boolean.valueOf(lStructure.isLockable()));
/* 4482 */         lToReturn.put("Locked", Boolean.valueOf(lStructure.isLocked()));
/* 4483 */         lToReturn.put("MaxX", Integer.valueOf(lStructure.getMaxX()));
/* 4484 */         lToReturn.put("MaxY", Integer.valueOf(lStructure.getMaxY()));
/* 4485 */         lToReturn.put("MinX", Integer.valueOf(lStructure.getMinX()));
/* 4486 */         lToReturn.put("MinY", Integer.valueOf(lStructure.getMinY()));
/* 4487 */         lToReturn.put("Name", lStructure.getName());
/* 4488 */         lToReturn.put("OwnerID", Long.valueOf(lStructure.getOwnerId()));
/* 4489 */         lToReturn.put("Roof", Byte.valueOf(lStructure.getRoof()));
/* 4490 */         lToReturn.put("Size", Integer.valueOf(lStructure.getSize()));
/* 4491 */         lToReturn.put("HasWalls", Boolean.valueOf(lStructure.hasWalls()));
/* 4492 */         Wall[] lWalls = lStructure.getWalls();
/* 4493 */         if (lWalls != null) {
/*      */           
/* 4495 */           lToReturn.put("Wall Count", Integer.valueOf(lWalls.length));
/*      */         }
/*      */         else {
/*      */           
/* 4499 */           lToReturn.put("Wall Count", Integer.valueOf(0));
/*      */         } 
/* 4501 */         lToReturn.put("WritID", Long.valueOf(lStructure.getWritId()));
/* 4502 */         lToReturn.put("WurmID", Long.valueOf(lStructure.getWurmId()));
/*      */       }
/*      */       else
/*      */       {
/* 4506 */         lToReturn.put("Error", "No such Structure");
/*      */       }
/*      */     
/* 4509 */     } catch (NoSuchStructureException nss) {
/*      */       
/* 4511 */       logger.log(Level.WARNING, "Structure with id " + aStructureID + " not found.", (Throwable)nss);
/* 4512 */       lToReturn.put("Error", "No such Structure");
/* 4513 */       lToReturn.put("Exception", nss.getMessage());
/*      */     }
/* 4515 */     catch (RuntimeException e) {
/*      */       
/* 4517 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), e);
/* 4518 */       lToReturn.put("Exception", e);
/*      */     } 
/* 4520 */     return lToReturn;
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
/*      */   public long getStructureIdFromWrit(String intraServerPassword, long aWritID) throws RemoteException {
/* 4535 */     validateIntraServerPassword(intraServerPassword);
/* 4536 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4538 */       logger.finer(getRemoteClientDetails() + " getStructureIdFromWrit for WritID: " + aWritID);
/*      */     }
/*      */     
/*      */     try {
/* 4542 */       Structure struct = Structures.getStructureForWrit(aWritID);
/* 4543 */       if (struct != null) {
/* 4544 */         return struct.getWurmId();
/*      */       }
/* 4546 */     } catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */     
/* 4549 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ?> getTileSummary(String intraServerPassword, int tilex, int tiley, boolean surfaced) throws RemoteException {
/* 4560 */     validateIntraServerPassword(intraServerPassword);
/* 4561 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4563 */       logger.finer(getRemoteClientDetails() + " getTileSummary for tile (x,y): " + tilex + ", " + tiley);
/*      */     }
/* 4565 */     Map<String, Object> lToReturn = new HashMap<>(10);
/*      */ 
/*      */     
/*      */     try {
/* 4569 */       Zone zone = Zones.getZone(tilex, tiley, surfaced);
/*      */       
/* 4571 */       VolaTile tile = zone.getTileOrNull(tilex, tiley);
/* 4572 */       if (tile != null)
/*      */       {
/*      */         
/* 4575 */         Structure lStructure = tile.getStructure();
/* 4576 */         if (lStructure != null) {
/*      */           
/* 4578 */           lToReturn.put("StructureID", Long.valueOf(lStructure.getWurmId()));
/* 4579 */           lToReturn.put("StructureName", lStructure.getName());
/*      */         } 
/*      */         
/* 4582 */         lToReturn.put("Kingdom", Byte.valueOf(tile.getKingdom()));
/*      */         
/* 4584 */         Village lVillage = tile.getVillage();
/* 4585 */         if (lVillage != null) {
/*      */           
/* 4587 */           lToReturn.put("VillageID", Integer.valueOf(lVillage.getId()));
/* 4588 */           lToReturn.put("VillageName", lVillage.getName());
/*      */         } 
/*      */         
/* 4591 */         lToReturn.put("Coord x", Integer.valueOf(tile.getTileX()));
/*      */         
/* 4593 */         lToReturn.put("Coord y", Integer.valueOf(tile.getTileY()));
/*      */       }
/*      */       else
/*      */       {
/* 4597 */         lToReturn.put("Error", "No such tile");
/*      */       }
/*      */     
/* 4600 */     } catch (NoSuchZoneException e) {
/*      */       
/* 4602 */       lToReturn.put("Error", "No such zone");
/* 4603 */       lToReturn.put("Exception", e.getMessage());
/*      */     }
/* 4605 */     catch (RuntimeException e) {
/*      */       
/* 4607 */       logger.log(Level.WARNING, "Error: " + e.getMessage(), e);
/* 4608 */       lToReturn.put("Exception", e);
/*      */     } 
/* 4610 */     return lToReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getReimbursementInfo(String intraServerPassword, String email) throws RemoteException {
/* 4621 */     validateIntraServerPassword(intraServerPassword);
/* 4622 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4624 */       logger.finer(getRemoteClientDetails() + " getReimbursementInfo for email: " + email);
/*      */     }
/* 4626 */     return Reimbursement.getReimbursementInfo(email);
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
/*      */   public boolean withDraw(String intraServerPassword, String retriever, String name, String _email, int _months, int _silvers, boolean titlebok, int _daysLeft) throws RemoteException {
/* 4638 */     validateIntraServerPassword(intraServerPassword);
/* 4639 */     logger.info(getRemoteClientDetails() + " withDraw for retriever: " + retriever + ", name: " + name + ", email: " + _email + ", months: " + _months + ", silvers: " + _silvers);
/*      */ 
/*      */     
/* 4642 */     return Reimbursement.withDraw(retriever, name, _email, _months, _silvers, titlebok, _daysLeft);
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
/*      */   public boolean transferPlayer(String intraServerPassword, String playerName, int posx, int posy, boolean surfaced, int power, byte[] data) throws RemoteException {
/* 4654 */     validateIntraServerPassword(intraServerPassword);
/* 4655 */     if (Constants.maintaining && power <= 0)
/* 4656 */       return false; 
/* 4657 */     logger.log(Level.INFO, getRemoteClientDetails() + " Transferplayer name: " + playerName + ", position (x,y): " + posx + ", " + posy + ", surfaced: " + surfaced);
/*      */     
/* 4659 */     if (IntraServerConnection.savePlayerToDisk(data, posx, posy, surfaced, false) > 0L) {
/*      */       
/* 4661 */       if (!Servers.isThisLoginServer()) {
/*      */ 
/*      */         
/* 4664 */         if ((new LoginServerWebConnection()).setCurrentServer(playerName, Servers.localServer.id))
/*      */         {
/*      */ 
/*      */           
/* 4668 */           return true;
/*      */         }
/*      */ 
/*      */         
/* 4672 */         return false;
/*      */       } 
/*      */       
/* 4675 */       return true;
/*      */     } 
/* 4677 */     return false;
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
/*      */   public boolean changePassword(String intraServerPassword, long wurmId, String newPassword) throws RemoteException {
/* 4695 */     validateIntraServerPassword(intraServerPassword);
/* 4696 */     logger.log(Level.INFO, getRemoteClientDetails() + " Changepassword name: " + wurmId);
/* 4697 */     return IntraServerConnection.setNewPassword(wurmId, newPassword);
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
/*      */   public boolean setCurrentServer(String intraServerPassword, String name, int currentServer) throws RemoteException {
/* 4709 */     validateIntraServerPassword(intraServerPassword);
/* 4710 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4712 */       logger.finer(getRemoteClientDetails() + " setCurrentServer to " + currentServer + " for player name: " + name);
/*      */     }
/* 4714 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/* 4715 */     if (pinf == null) {
/* 4716 */       return false;
/*      */     }
/* 4718 */     pinf.setCurrentServer(currentServer);
/* 4719 */     return true;
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
/*      */   public boolean addDraggedItem(String intraServerPassword, long itemId, byte[] itemdata, long draggerId, int posx, int posy) throws RemoteException {
/* 4731 */     validateIntraServerPassword(intraServerPassword);
/* 4732 */     DataInputStream iis = new DataInputStream(new ByteArrayInputStream(itemdata));
/* 4733 */     logger.log(Level.INFO, getRemoteClientDetails() + " Adddraggeditem itemID: " + itemId + ", draggerId: " + draggerId);
/*      */     
/*      */     try {
/* 4736 */       Set<ItemMetaData> idset = new HashSet<>();
/* 4737 */       int nums = iis.readInt();
/* 4738 */       for (int x = 0; x < nums; x++)
/*      */       {
/* 4740 */         IntraServerConnection.createItem(iis, 0.0F, 0.0F, 0.0F, idset, false);
/*      */       }
/* 4742 */       Items.convertItemMetaData(idset.<ItemMetaData>toArray(new ItemMetaData[idset.size()]));
/*      */     }
/* 4744 */     catch (IOException iox) {
/*      */       
/* 4746 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/* 4747 */       return false;
/*      */     } 
/*      */     
/*      */     try {
/* 4751 */       Item i = Items.getItem(itemId);
/* 4752 */       Zone z = Zones.getZone(posx, posy, true);
/* 4753 */       z.addItem(i);
/* 4754 */       return true;
/*      */     }
/* 4756 */     catch (NoSuchItemException nsi) {
/*      */       
/* 4758 */       logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/* 4759 */       return false;
/*      */     }
/* 4761 */     catch (NoSuchZoneException nsz) {
/*      */       
/* 4763 */       logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/* 4764 */       return false;
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
/*      */   public String rename(String intraServerPassword, String oldName, String newName, String newPass, int power) throws RemoteException {
/* 4777 */     validateIntraServerPassword(intraServerPassword);
/* 4778 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4780 */       logger.finer(getRemoteClientDetails() + " rename oldName: " + oldName + ", newName: " + newName + ", power: " + power);
/*      */     }
/*      */     
/* 4783 */     String toReturn = "";
/* 4784 */     newName = LoginHandler.raiseFirstLetter(newName);
/* 4785 */     if (Servers.localServer.LOGINSERVER && Players.getInstance().doesPlayerNameExist(newName))
/*      */     {
/* 4787 */       return "The name " + newName + " already exists. This is an Error.";
/*      */     }
/*      */     
/* 4790 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/* 4792 */       toReturn = toReturn + Servers.rename(oldName, newName, newPass, power);
/*      */     }
/* 4794 */     if (!toReturn.contains("Error."))
/*      */       
/*      */       try {
/* 4797 */         toReturn = PlayerInfoFactory.rename(oldName, newName, newPass, power);
/*      */       }
/* 4799 */       catch (IOException iox) {
/*      */         
/* 4801 */         toReturn = toReturn + Servers.localServer.name + " " + iox.getMessage() + ". This is an Error.\n";
/* 4802 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       }  
/* 4804 */     return toReturn;
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
/*      */   public String changePassword(String intraServerPassword, String changerName, String name, String newPass, int power) throws RemoteException {
/* 4816 */     validateIntraServerPassword(intraServerPassword);
/* 4817 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4819 */       logger.finer(getRemoteClientDetails() + " changePassword, changerName: " + changerName + ", for player name: " + name + ", power: " + power);
/*      */     }
/*      */     
/* 4822 */     String toReturn = "";
/* 4823 */     changerName = LoginHandler.raiseFirstLetter(changerName);
/* 4824 */     name = LoginHandler.raiseFirstLetter(name);
/*      */     
/*      */     try {
/* 4827 */       toReturn = PlayerInfoFactory.changePassword(changerName, name, newPass, power);
/*      */     }
/* 4829 */     catch (IOException iox) {
/*      */       
/* 4831 */       toReturn = toReturn + Servers.localServer.name + " " + iox.getMessage() + "\n";
/* 4832 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 4834 */     logger.log(Level.INFO, getRemoteClientDetails() + " changePassword, changerName: " + changerName + ", for player name: " + name);
/*      */     
/* 4836 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/* 4838 */       if (changerName.equals(name)) {
/*      */         
/* 4840 */         PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/* 4841 */         if (pinf != null && Servers.localServer.id != pinf.currentServer)
/*      */         {
/* 4843 */           LoginServerWebConnection lsw = new LoginServerWebConnection(pinf.currentServer);
/* 4844 */           toReturn = toReturn + lsw.changePassword(changerName, name, newPass, power);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 4849 */         toReturn = toReturn + Servers.sendChangePass(changerName, name, newPass, power);
/*      */       } 
/*      */     }
/* 4852 */     return toReturn;
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
/*      */   public String changeEmail(String intraServerPassword, String changerName, String name, String newEmail, String password, int power, String pwQuestion, String pwAnswer) throws RemoteException {
/* 4864 */     validateIntraServerPassword(intraServerPassword);
/* 4865 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4867 */       logger.finer(getRemoteClientDetails() + " changeEmail, changerName: " + changerName + ", for player name: " + name + ", power: " + power);
/*      */     }
/*      */     
/* 4870 */     changerName = LoginHandler.raiseFirstLetter(changerName);
/* 4871 */     name = LoginHandler.raiseFirstLetter(name);
/* 4872 */     String toReturn = "";
/* 4873 */     logger.log(Level.INFO, getRemoteClientDetails() + " changeEmail, changerName: " + changerName + ", for player name: " + name);
/*      */ 
/*      */     
/*      */     try {
/* 4877 */       toReturn = PlayerInfoFactory.changeEmail(changerName, name, newEmail, password, power, pwQuestion, pwAnswer);
/* 4878 */       if (toReturn.equals("NO") || toReturn
/* 4879 */         .equals("NO Retrieval info updated."))
/*      */       {
/* 4881 */         return "You may only have 5 accounts with the same email. Also you need to provide the correct password for a character with that email address in order to change to it.";
/*      */       }
/*      */     }
/* 4884 */     catch (IOException iox) {
/*      */       
/* 4886 */       toReturn = toReturn + Servers.localServer.name + " " + iox.getMessage() + "\n";
/* 4887 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/* 4889 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/* 4891 */       toReturn = toReturn + Servers.changeEmail(changerName, name, newEmail, password, power, pwQuestion, pwAnswer);
/*      */     }
/* 4893 */     return toReturn;
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
/*      */   public String addReimb(String intraServerPassword, String changerName, String name, int numMonths, int _silver, int _daysLeft, boolean setbok) throws RemoteException {
/* 4905 */     validateIntraServerPassword(intraServerPassword);
/* 4906 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/* 4908 */       logger.fine(getRemoteClientDetails() + " addReimb, changerName: " + changerName + ", for player name: " + name + ", numMonths: " + numMonths + ", silver: " + _silver + ", daysLeft: " + _daysLeft + ", setbok: " + setbok);
/*      */     }
/*      */     
/* 4911 */     changerName = LoginHandler.raiseFirstLetter(changerName);
/* 4912 */     name = LoginHandler.raiseFirstLetter(name);
/* 4913 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/* 4915 */       return Reimbursement.addReimb(changerName, name, numMonths, _silver, _daysLeft, setbok);
/*      */     }
/*      */ 
/*      */     
/* 4919 */     return Servers.localServer.name + " - failed to add reimbursement. This is not the login server.";
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
/*      */   public long[] getCurrentServerAndWurmid(String intraServerPassword, String name, long wurmid) throws RemoteException {
/* 4934 */     validateIntraServerPassword(intraServerPassword);
/* 4935 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4937 */       logger.finer(getRemoteClientDetails() + " getCurrentServerAndWurmid for player name: " + name + ", wurmid: " + wurmid);
/*      */     }
/*      */     
/* 4940 */     PlayerInfo pinf = null;
/* 4941 */     if (name != null && name.length() > 2) {
/*      */       
/* 4943 */       name = LoginHandler.raiseFirstLetter(name);
/* 4944 */       pinf = PlayerInfoFactory.createPlayerInfo(name);
/*      */ 
/*      */     
/*      */     }
/* 4948 */     else if (wurmid > 0L) {
/*      */       
/* 4950 */       pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/*      */     } 
/*      */     
/* 4953 */     if (pinf != null) {
/*      */       
/*      */       try {
/*      */         
/* 4957 */         pinf.load();
/* 4958 */         long[] toReturn = { pinf.currentServer, pinf.wurmId };
/*      */         
/* 4960 */         return toReturn;
/*      */       }
/* 4962 */       catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */     
/* 4966 */     return noInfoLong;
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
/*      */   public Map<Long, byte[]> getPlayerStates(String intraServerPassword, long[] wurmids) throws RemoteException, WurmServerException {
/* 4980 */     validateIntraServerPassword(intraServerPassword);
/* 4981 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 4983 */       if (wurmids.length == 0) {
/*      */         
/* 4985 */         logger.finer(getRemoteClientDetails() + " getPlayersSubInfo for ALL players.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 4990 */         StringBuilder buf = new StringBuilder();
/* 4991 */         for (int x = 0; x < wurmids.length; x++) {
/*      */           
/* 4993 */           if (x > 0)
/* 4994 */             buf.append(","); 
/* 4995 */           buf.append(wurmids[x]);
/*      */         } 
/* 4997 */         logger.finer(getRemoteClientDetails() + " getPlayersSubInfo for player wurmids: " + buf
/* 4998 */             .toString());
/*      */       } 
/*      */     }
/* 5001 */     return PlayerInfoFactory.getPlayerStates(wurmids);
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
/*      */   public void manageFeature(String intraServerPassword, int serverId, final int featureId, final boolean aOverridden, final boolean aEnabled, final boolean global) throws RemoteException {
/* 5015 */     validateIntraServerPassword(intraServerPassword);
/* 5016 */     if (logger.isLoggable(Level.FINER)) {
/* 5017 */       logger.finer(getRemoteClientDetails() + " manageFeature " + featureId);
/*      */     }
/* 5019 */     Thread t = new Thread("manageFeature-Thread-" + featureId)
/*      */       {
/*      */         
/*      */         public void run()
/*      */         {
/* 5024 */           Features.Feature.setOverridden(Servers.getLocalServerId(), featureId, aOverridden, aEnabled, global);
/*      */         }
/*      */       };
/* 5027 */     t.setPriority(4);
/* 5028 */     t.start();
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
/*      */   public void startShutdown(String intraServerPassword, String instigator, int seconds, String reason) throws RemoteException {
/* 5041 */     validateIntraServerPassword(intraServerPassword);
/* 5042 */     if (Servers.isThisLoginServer())
/*      */     {
/*      */       
/* 5045 */       Servers.startShutdown(instigator, seconds, reason);
/*      */     }
/* 5047 */     logger.log(Level.INFO, instigator + " shutting down server in " + seconds + " seconds, reason: " + reason);
/*      */     
/* 5049 */     Server.getInstance().startShutdown(seconds, reason);
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
/*      */   public String sendMail(String intraServerPassword, byte[] maildata, byte[] itemdata, long sender, long wurmid, int targetServer) throws RemoteException {
/* 5061 */     validateIntraServerPassword(intraServerPassword);
/* 5062 */     logger.log(Level.INFO, getRemoteClientDetails() + " sendMail " + sender + " to server " + targetServer + ", receiver ID: " + wurmid);
/*      */     
/* 5064 */     if (targetServer == Servers.localServer.id) {
/*      */       
/* 5066 */       DataInputStream dis = new DataInputStream(new ByteArrayInputStream(maildata));
/*      */       
/*      */       try {
/* 5069 */         int nums = dis.readInt();
/* 5070 */         for (int x = 0; x < nums; x++)
/*      */         {
/*      */           
/* 5073 */           WurmMail m = new WurmMail(dis.readByte(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readLong(), dis.readInt(), dis.readBoolean(), false);
/*      */           
/* 5075 */           WurmMail.addWurmMail(m);
/* 5076 */           m.createInDatabase();
/*      */         }
/*      */       
/* 5079 */       } catch (IOException iox) {
/*      */         
/* 5081 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/* 5082 */         return "A database error occurred. Please report this to a GM.";
/*      */       } 
/* 5084 */       DataInputStream iis = new DataInputStream(new ByteArrayInputStream(itemdata));
/*      */       
/*      */       try {
/* 5087 */         Set<ItemMetaData> idset = new HashSet<>();
/* 5088 */         int nums = iis.readInt();
/* 5089 */         for (int x = 0; x < nums; x++)
/*      */         {
/* 5091 */           IntraServerConnection.createItem(iis, 0.0F, 0.0F, 0.0F, idset, false);
/*      */         }
/* 5093 */         Items.convertItemMetaData(idset.<ItemMetaData>toArray(new ItemMetaData[idset.size()]));
/*      */       }
/* 5095 */       catch (IOException iox) {
/*      */         
/* 5097 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/* 5098 */         return "A database error occurred when inserting an item. Please report this to a GM.";
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5103 */       ServerEntry entry = Servers.getServerWithId(targetServer);
/* 5104 */       if (entry != null) {
/*      */         
/* 5106 */         if (entry.isAvailable(5, true)) {
/*      */           
/* 5108 */           LoginServerWebConnection lsw = new LoginServerWebConnection(targetServer);
/* 5109 */           return lsw.sendMail(maildata, itemdata, sender, wurmid, targetServer);
/*      */         } 
/*      */         
/* 5112 */         return "The target server is not available right now.";
/*      */       } 
/*      */       
/* 5115 */       return "Failed to locate target server.";
/*      */     } 
/* 5117 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String pardonban(String intraServerPassword, String name) throws RemoteException {
/* 5128 */     validateIntraServerPassword(intraServerPassword);
/* 5129 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 5131 */       logger.finer(getRemoteClientDetails() + " pardonban for player name: " + name);
/*      */     }
/* 5133 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 5135 */       PlayerInfo info = PlayerInfoFactory.createPlayerInfo(name);
/* 5136 */       if (info != null) {
/*      */ 
/*      */         
/*      */         try {
/* 5140 */           info.load();
/*      */         }
/* 5142 */         catch (IOException iox) {
/*      */           
/* 5144 */           logger.log(Level.WARNING, getRemoteClientDetails() + " Failed to load the player information. Not pardoned - " + iox
/* 5145 */               .getMessage(), iox);
/* 5146 */           return "Failed to load the player information. Not pardoned.";
/*      */         } 
/*      */         
/*      */         try {
/* 5150 */           info.setBanned(false, "", 0L);
/*      */         }
/* 5152 */         catch (IOException iox) {
/*      */           
/* 5154 */           logger.log(Level.WARNING, getRemoteClientDetails() + " Failed to save the player information. Not pardoned - " + iox
/* 5155 */               .getMessage(), iox);
/* 5156 */           return "Failed to save the player information. Not pardoned.";
/*      */         } 
/* 5158 */         logger.info(getRemoteClientDetails() + " Login server pardoned " + name);
/* 5159 */         return "Login server pardoned " + name + ".";
/*      */       } 
/*      */ 
/*      */       
/* 5163 */       logger.warning("Failed to locate the player " + name + ".");
/* 5164 */       return "Failed to locate the player " + name + ".";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5169 */     logger.warning(Servers.localServer.name + " not login server. Pardon failed.");
/* 5170 */     return Servers.localServer.name + " not login server. Pardon failed.";
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
/*      */   public String ban(String intraServerPassword, String name, String reason, int days) throws RemoteException {
/* 5182 */     validateIntraServerPassword(intraServerPassword);
/* 5183 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/* 5185 */       logger.finer(getRemoteClientDetails() + " ban for player name: " + name + ", reason: " + reason + ", for " + days + " days");
/*      */     }
/*      */     
/* 5188 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 5190 */       PlayerInfo info = PlayerInfoFactory.createPlayerInfo(name);
/* 5191 */       if (info != null) {
/*      */         
/* 5193 */         long expiry = System.currentTimeMillis() + days * 86400000L;
/*      */         
/*      */         try {
/* 5196 */           info.load();
/*      */         }
/* 5198 */         catch (IOException iox) {
/*      */           
/* 5200 */           logger.log(Level.WARNING, "Failed to load the player information. Not banned - " + iox.getMessage(), iox);
/* 5201 */           return "Failed to load the player information. Not banned.";
/*      */         } 
/*      */         
/*      */         try {
/* 5205 */           info.setBanned(true, reason, expiry);
/*      */         }
/* 5207 */         catch (IOException iox) {
/*      */           
/* 5209 */           logger.log(Level.WARNING, "Failed to save the player information. Not banned - " + iox.getMessage(), iox);
/* 5210 */           return "Failed to save the player information. Not banned.";
/*      */         } 
/* 5212 */         logger.info(getRemoteClientDetails() + " Login server banned " + name + ": " + reason + " for " + days + " days.");
/*      */         
/* 5214 */         return "Login server banned " + name + ": " + reason + " for " + days + " days.";
/*      */       } 
/*      */ 
/*      */       
/* 5218 */       logger.warning("Failed to locate the player " + name + ".");
/* 5219 */       return "Failed to locate the player " + name + ".";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5224 */     logger.warning(Servers.localServer.name + " not login server. IPBan failed.");
/* 5225 */     return Servers.localServer.name + " not login server. IPBan failed.";
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
/*      */   public String addBannedIp(String intraServerPassword, String ip, String reason, int days) throws RemoteException {
/* 5237 */     validateIntraServerPassword(intraServerPassword);
/* 5238 */     long expiry = System.currentTimeMillis() + days * 86400000L;
/* 5239 */     Players.getInstance().addBannedIp(ip, reason, expiry);
/* 5240 */     logger.info(getRemoteClientDetails() + " RMI client requested " + ip + " banned for " + days + " days - " + reason);
/* 5241 */     return ip + " banned for " + days + " days - " + reason;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ban[] getPlayersBanned(String intraServerPassword) throws RemoteException {
/* 5252 */     validateIntraServerPassword(intraServerPassword);
/* 5253 */     return Players.getInstance().getPlayersBanned();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ban[] getIpsBanned(String intraServerPassword) throws RemoteException {
/* 5264 */     validateIntraServerPassword(intraServerPassword);
/* 5265 */     return Players.getInstance().getBans();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String removeBannedIp(String intraServerPassword, String ip) throws RemoteException {
/* 5276 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5278 */     if (Players.getInstance().removeBan(ip)) {
/*      */       
/* 5280 */       logger.log(Level.INFO, getRemoteClientDetails() + " RMI client requested " + ip + " was pardoned.");
/* 5281 */       return "Okay, " + ip + " was pardoned.";
/*      */     } 
/*      */ 
/*      */     
/* 5285 */     logger.info(getRemoteClientDetails() + " RMI client requested pardon but the ip " + ip + " was not previously banned.");
/*      */     
/* 5287 */     return "The ip " + ip + " was not previously banned.";
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
/*      */   public String setPlayerMoney(String intraServerPassword, long wurmid, long currentMoney, long moneyAdded, String detail) throws RemoteException {
/* 5300 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5302 */     if (moneyDetails.contains(detail)) {
/*      */       
/* 5304 */       logger.warning(getRemoteClientDetails() + " RMI client The money transaction has already been performed, wurmid: " + wurmid + ", currentMoney: " + currentMoney + ", moneyAdded: " + moneyAdded + ", detail: " + detail);
/*      */       
/* 5306 */       return "The money transaction has already been performed";
/*      */     } 
/* 5308 */     logger.log(Level.INFO, getRemoteClientDetails() + " RMI client set player money for " + wurmid);
/* 5309 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 5310 */     if (info != null) {
/*      */       
/*      */       try
/*      */       {
/* 5314 */         info.load();
/*      */       }
/* 5316 */       catch (IOException iox)
/*      */       {
/* 5318 */         logger.log(Level.WARNING, "Failed to load player info for " + wurmid + ", detail: " + detail + ": " + iox
/* 5319 */             .getMessage(), iox);
/* 5320 */         return "Failed to load the player from database. Transaction failed.";
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5325 */       logger.log(Level.WARNING, wurmid + ", failed to locate player info and set money to " + currentMoney + ", detail: " + detail + "!");
/*      */       
/* 5327 */       return "Failed to locate the player in the database. The player account probably has been deleted. Transaction failed.";
/*      */     } 
/* 5329 */     if (info.wurmId > 0L) {
/*      */       
/* 5331 */       if (info.currentServer != Servers.localServer.id) {
/*      */         
/* 5333 */         logger.warning("Received a CMD_SET_PLAYER_MONEY for player " + info.getName() + " (id: " + wurmid + ") but their currentserver (id: " + info
/* 5334 */             .getCurrentServer() + ") is not this server (id: " + Servers.localServer.id + "), detail: " + detail);
/*      */         
/* 5336 */         return "There is inconsistency with regards to which server the player account is active on. Please email contact@wurmonline.com with this message. Transaction failed.";
/*      */       } 
/*      */       
/*      */       try {
/* 5340 */         info.setMoney(currentMoney);
/* 5341 */         new MoneyTransfer(info.getName(), wurmid, currentMoney, moneyAdded, detail, (byte)6, "");
/*      */         
/* 5343 */         Change c = new Change(currentMoney);
/*      */         
/* 5345 */         moneyDetails.add(detail);
/*      */         
/*      */         try {
/* 5348 */           logger.info(getRemoteClientDetails() + " RMI client Added " + moneyAdded + " to player ID: " + wurmid + ", currentMoney: " + currentMoney + ", detail: " + detail);
/*      */           
/* 5350 */           Player p = Players.getInstance().getPlayer(wurmid);
/*      */           
/* 5352 */           Message mess = new Message(null, (byte)3, ":Event", "Your available money in the bank is now " + c.getChangeString() + ".");
/* 5353 */           mess.setReceiver(p.getWurmId());
/* 5354 */           Server.getInstance().addMessage(mess);
/*      */         }
/* 5356 */         catch (NoSuchPlayerException exp) {
/*      */           
/* 5358 */           if (logger.isLoggable(Level.FINER))
/*      */           {
/* 5360 */             logger.finer("player ID: " + wurmid + " is not online, currentMoney: " + currentMoney + ", moneyAdded: " + moneyAdded + ", detail: " + detail);
/*      */           }
/*      */         } 
/*      */         
/* 5364 */         return "Okay. The player now has " + c.getChangeString() + " in the bank.";
/*      */       }
/* 5366 */       catch (IOException iox) {
/*      */         
/* 5368 */         logger.log(Level.WARNING, wurmid + ", failed to set money to " + currentMoney + ", detail: " + detail + ".", iox);
/*      */         
/* 5370 */         return "Money transaction failed. Error reported was " + iox.getMessage() + ".";
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5375 */     logger.log(Level.WARNING, wurmid + ", failed to locate player info and set money to " + currentMoney + ", detail: " + detail + "!");
/*      */     
/* 5377 */     return "Failed to locate the player in the database. The player account probably has been deleted. Transaction failed.";
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
/*      */   public String setPlayerPremiumTime(String intraServerPassword, long wurmid, long currentExpire, int days, int months, String detail) throws RemoteException {
/* 5390 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5392 */     if (timeDetails.contains(detail)) {
/*      */       
/* 5394 */       logger.warning(getRemoteClientDetails() + " RMI client The time transaction has already been performed, wurmid: " + wurmid + ", currentExpire: " + currentExpire + ", days: " + days + ", months: " + months + ", detail: " + detail);
/*      */ 
/*      */       
/* 5397 */       return "The time transaction has already been performed";
/*      */     } 
/* 5399 */     logger.log(Level.INFO, getRemoteClientDetails() + " RMI client set premium time for " + wurmid);
/* 5400 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 5401 */     if (info != null) {
/*      */ 
/*      */       
/*      */       try {
/* 5405 */         info.load();
/*      */       }
/* 5407 */       catch (IOException iox) {
/*      */         
/* 5409 */         logger.log(Level.WARNING, "Failed to load the player from database. Transaction failed, wurmid: " + wurmid + ", currentExpire: " + currentExpire + ", days: " + days + ", months: " + months + ", detail: " + detail, iox);
/*      */ 
/*      */         
/* 5412 */         return "Failed to load the player from database. Transaction failed.";
/*      */       } 
/*      */       
/* 5415 */       if (info.currentServer != Servers.localServer.id) {
/*      */         
/* 5417 */         logger.warning("Received a CMD_SET_PLAYER_PAYMENTEXPIRE for player " + info.getName() + " (id: " + wurmid + ") but their currentserver (id: " + info
/* 5418 */             .getCurrentServer() + ") is not this server (id: " + Servers.localServer.id + "), detail: " + detail);
/*      */         
/* 5420 */         return "There is inconsistency with regards to which server the player account is active on. Please email contact@wurmonline.com with this message. Transaction failed.";
/*      */       } 
/*      */       
/*      */       try {
/* 5424 */         info.setPaymentExpire(currentExpire);
/* 5425 */         new TimeTransfer(info.getName(), wurmid, months, false, days, detail);
/*      */         
/* 5427 */         timeDetails.add(detail);
/*      */         
/*      */         try {
/* 5430 */           Player p = Players.getInstance().getPlayer(wurmid);
/*      */ 
/*      */           
/* 5433 */           String expireString = "You now have premier playing time until " + WurmCalendar.formatGmt(currentExpire) + ".";
/* 5434 */           Message mess = new Message(null, (byte)3, ":Event", expireString);
/*      */           
/* 5436 */           mess.setReceiver(p.getWurmId());
/* 5437 */           Server.getInstance().addMessage(mess);
/*      */         }
/* 5439 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */         
/* 5443 */         logger.info(getRemoteClientDetails() + " RMI client " + info.getName() + " now has premier playing time until " + 
/* 5444 */             WurmCalendar.formatGmt(currentExpire) + ", wurmid: " + wurmid + ", currentExpire: " + currentExpire + ", days: " + days + ", months: " + months + ", detail: " + detail + '.');
/*      */         
/* 5446 */         return "Okay. " + info.getName() + " now has premier playing time until " + 
/* 5447 */           WurmCalendar.formatGmt(currentExpire) + ".";
/*      */       }
/* 5449 */       catch (IOException iox) {
/*      */         
/* 5451 */         logger.log(Level.WARNING, "Transaction failed, wurmid: " + wurmid + ", currentExpire: " + currentExpire + ", days: " + days + ", months: " + months + ", detail: " + detail + ", " + iox
/* 5452 */             .getMessage(), iox);
/* 5453 */         return "Time transaction failed. Error reported was " + iox.getMessage() + ".";
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5458 */     logger.log(Level.WARNING, wurmid + ", failed to locate player info and set expire time to " + currentExpire + "!, detail: " + detail);
/*      */     
/* 5460 */     return "Failed to locate the player in the database. The player account probably has been deleted. Transaction failed.";
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
/*      */   public void setWeather(String intraServerPassword, float windRotation, float windpower, float windDir) throws RemoteException {
/* 5473 */     validateIntraServerPassword(intraServerPassword);
/* 5474 */     Server.getWeather().setWindOnly(windRotation, windpower, windDir);
/* 5475 */     logger.log(Level.INFO, getRemoteClientDetails() + " RMI client. Received weather data from login server. Propagating windrot=" + windRotation);
/*      */     
/* 5477 */     Players.getInstance().setShouldSendWeather(true);
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
/*      */   public String sendVehicle(String intraServerPassword, byte[] passengerdata, byte[] itemdata, long pilotId, long vehicleId, int targetServer, int tilex, int tiley, int layer, float rot) throws RemoteException {
/* 5490 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5492 */     logger.log(Level.INFO, getRemoteClientDetails() + " RMI client send vehicle for pilot " + pilotId + " vehicle " + vehicleId + " itemdata bytes=" + itemdata.length + " passenger data bytes=" + passengerdata.length);
/*      */     
/* 5494 */     if (targetServer == Servers.localServer.id) {
/*      */       
/* 5496 */       long start = System.nanoTime();
/*      */       
/* 5498 */       DataInputStream iis = new DataInputStream(new ByteArrayInputStream(itemdata));
/* 5499 */       Set<ItemMetaData> idset = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 5506 */         int nums = iis.readInt();
/* 5507 */         logger.log(Level.INFO, "Trying to create " + nums + " items for vehicle: " + vehicleId);
/* 5508 */         float posx = (tilex * 4 + 2);
/* 5509 */         float posy = (tiley * 4 + 2);
/* 5510 */         IntraServerConnection.resetTransferVariables(String.valueOf(pilotId));
/* 5511 */         for (int x = 0; x < nums; x++)
/*      */         {
/* 5513 */           IntraServerConnection.createItem(iis, posx, posy, 0.0F, idset, false);
/*      */         }
/* 5515 */         Items.convertItemMetaData(idset.<ItemMetaData>toArray(new ItemMetaData[idset.size()]));
/*      */       }
/* 5517 */       catch (IOException iox) {
/*      */         
/* 5519 */         logger.log(Level.WARNING, iox.getMessage() + " Last item=" + IntraServerConnection.lastItemName + ", " + IntraServerConnection.lastItemId, iox);
/*      */         
/* 5521 */         for (ItemMetaData md : idset)
/*      */         {
/* 5523 */           logger.log(Level.INFO, md.itname + ", " + md.itemId);
/*      */         }
/* 5525 */         return "A database error occurred when inserting an item. Please report this to a GM.";
/*      */       }
/* 5527 */       catch (Exception ex) {
/*      */         
/* 5529 */         logger.log(Level.WARNING, ex.getMessage() + " Last item=" + IntraServerConnection.lastItemName + ", " + IntraServerConnection.lastItemId, ex);
/*      */         
/* 5531 */         return "A database error occurred when inserting an item. Please report this to a GM.";
/*      */       } 
/* 5533 */       DataInputStream dis = new DataInputStream(new ByteArrayInputStream(passengerdata));
/*      */       
/*      */       try {
/* 5536 */         Item i = Items.getItem(vehicleId);
/*      */         
/* 5538 */         i.setPosXYZ((tilex * 4 + 2), (tiley * 4 + 2), 0.0F);
/* 5539 */         i.setRotation(rot);
/* 5540 */         logger.log(Level.INFO, "Trying to put " + i.getName() + ", " + i.getDescription() + " at " + i.getTileX() + "," + i
/* 5541 */             .getTileY());
/*      */         
/* 5543 */         Zones.getZone(i.getTileX(), i.getTileY(), (layer == 0)).addItem(i);
/* 5544 */         Vehicles.createVehicle(i);
/* 5545 */         MountTransfer mt = new MountTransfer(vehicleId, pilotId);
/* 5546 */         int nums = dis.readInt();
/* 5547 */         for (int x = 0; x < nums; x++)
/*      */         {
/* 5549 */           mt.addToSeat(dis.readLong(), dis.readInt());
/*      */         }
/*      */       }
/* 5552 */       catch (NoSuchItemException nsi) {
/*      */         
/* 5554 */         logger.log(Level.WARNING, "Transferring vehicle " + vehicleId + ' ' + nsi.getMessage(), (Throwable)nsi);
/*      */       }
/* 5556 */       catch (NoSuchZoneException nsz) {
/*      */         
/* 5558 */         logger.log(Level.WARNING, "Transferring vehicle " + vehicleId + ' ' + nsz.getMessage(), (Throwable)nsz);
/*      */       }
/* 5560 */       catch (IOException iox) {
/*      */         
/* 5562 */         logger.log(Level.WARNING, "Transferring vehicle " + vehicleId + ' ' + iox.getMessage(), iox);
/* 5563 */         return "A database error occurred. Please report this to a GM.";
/*      */       } 
/* 5565 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/* 5566 */       logger.log(Level.INFO, "Transferring vehicle " + vehicleId + " took " + lElapsedTime + " ms.");
/*      */     }
/*      */     else {
/*      */       
/* 5570 */       ServerEntry entry = Servers.getServerWithId(targetServer);
/* 5571 */       if (entry != null) {
/*      */         
/* 5573 */         if (entry.isAvailable(5, true)) {
/*      */           
/* 5575 */           LoginServerWebConnection lsw = new LoginServerWebConnection(targetServer);
/* 5576 */           return lsw.sendVehicle(passengerdata, itemdata, pilotId, vehicleId, targetServer, tilex, tiley, layer, rot);
/*      */         } 
/*      */         
/* 5579 */         return "The target server is not available right now.";
/*      */       } 
/*      */       
/* 5582 */       return "Failed to locate target server.";
/*      */     } 
/* 5584 */     return "";
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
/*      */   public void genericWebCommand(String intraServerPassword, short wctype, long id, byte[] data) throws RemoteException {
/* 5606 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5608 */     WebCommand wc = WebCommand.createWebCommand(wctype, id, data);
/* 5609 */     if (wc != null) {
/*      */       
/* 5611 */       if (Servers.localServer.LOGINSERVER)
/*      */       {
/*      */         
/* 5614 */         if (wc.autoForward()) {
/* 5615 */           Servers.sendWebCommandToAllServers(wctype, wc, wc.isEpicOnly());
/*      */         }
/*      */       }
/* 5618 */       if (!((WurmId.getOrigin(id) == Servers.localServer.id) ? 1 : 0))
/*      */       {
/* 5620 */         Server.getInstance().addWebCommand(wc);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKingdomInfo(String intraServerPassword, int serverId, byte kingdomId, byte templateKingdom, String _name, String _password, String _chatName, String _suffix, String mottoOne, String mottoTwo, boolean acceptsPortals) throws RemoteException {
/* 5641 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5643 */     Kingdom newInfo = new Kingdom(kingdomId, templateKingdom, _name, _password, _chatName, _suffix, mottoOne, mottoTwo, acceptsPortals);
/*      */     
/* 5645 */     if (serverId != Servers.localServer.id)
/*      */     {
/* 5647 */       Kingdoms.addKingdom(newInfo);
/*      */     }
/* 5649 */     WcKingdomInfo wck = new WcKingdomInfo(WurmId.getNextWCCommandId(), true, kingdomId);
/* 5650 */     wck.encode();
/* 5651 */     Servers.sendWebCommandToAllServers((short)7, wck, wck.isEpicOnly());
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
/*      */   public boolean kingdomExists(String intraServerPassword, int serverId, byte kingdomId, boolean exists) throws RemoteException {
/* 5670 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5672 */     logger.log(Level.INFO, "serverId:" + serverId + " kingdom id " + kingdomId + " exists=" + exists);
/* 5673 */     boolean result = Servers.kingdomExists(serverId, kingdomId, exists);
/* 5674 */     if (Servers.getServerWithId(serverId) != null && (Servers.getServerWithId(serverId)).name != null) {
/* 5675 */       logger.log(Level.INFO, (Servers.getServerWithId(serverId)).name + " kingdom id " + kingdomId + " exists=" + exists);
/* 5676 */     } else if (Servers.getServerWithId(serverId) == null) {
/* 5677 */       logger.log(Level.INFO, serverId + " server is null " + kingdomId + " exists=" + exists);
/*      */     } else {
/* 5679 */       logger.log(Level.INFO, "Name for " + Servers.getServerWithId(serverId) + " server is null " + kingdomId + " exists=" + exists);
/*      */     } 
/* 5681 */     if (Servers.localServer.LOGINSERVER)
/*      */     {
/* 5683 */       if (!exists) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5688 */         if (!result)
/*      */         {
/* 5690 */           Kingdom k = Kingdoms.getKingdomOrNull(kingdomId);
/* 5691 */           boolean sendDelete = false;
/* 5692 */           if (k != null)
/*      */           {
/* 5694 */             if (k.isCustomKingdom())
/*      */             {
/*      */ 
/*      */               
/* 5698 */               k.delete();
/* 5699 */               Kingdoms.removeKingdom(kingdomId);
/* 5700 */               sendDelete = true;
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           
/* 5718 */           Servers.sendKingdomExistsToAllServers(serverId, kingdomId, false);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 5723 */         Servers.sendKingdomExistsToAllServers(serverId, kingdomId, true);
/*      */       } 
/*      */     }
/* 5726 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/* 5731 */     if (args.length == 2 && args[0].compareTo("ShutdownLive") == 0) {
/*      */       
/*      */       try
/*      */       {
/* 5735 */         WebInterfaceTest wit = new WebInterfaceTest();
/* 5736 */         System.out.println("Shutting down ALL live servers!");
/* 5737 */         wit.shutdownAll("Maintenance restart. Up to thirty minutes downtime.", Integer.parseInt(args[1]));
/* 5738 */         System.out.println("I do hope this is what you wanted. All servers will be down in approximately " + args[1] + " seconds.");
/*      */       
/*      */       }
/* 5741 */       catch (Exception ex)
/*      */       {
/* 5743 */         ex.printStackTrace();
/*      */       }
/*      */     
/* 5746 */     } else if (args.length == 3) {
/*      */       
/*      */       try
/*      */       {
/* 5750 */         WebInterfaceTest wit = new WebInterfaceTest();
/* 5751 */         System.out.println("Attempting to shutdown server at " + args[0] + ", port " + args[1]);
/* 5752 */         String[] userInfo = args[2].split(":");
/* 5753 */         wit.shutDown(args[0], args[1], userInfo[0], userInfo[1]);
/*      */       }
/* 5755 */       catch (Exception ex)
/*      */       {
/* 5757 */         logger.log(Level.INFO, "failed to shut down localhost", ex);
/* 5758 */         ex.printStackTrace();
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5763 */       System.out.println("Usage:\nNo arguments - This message.\nShutdownLive <delay> - Shutsdown ALL LIVE SERVERS using the seconds provided as a delay\n<host> <port> <user>:<password> - Shutdown the specified server using your GM credentials.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean validateAccount(String user, String password, byte power) throws IOException, Exception {
/* 5771 */     PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(LoginHandler.raiseFirstLetter(user));
/* 5772 */     if (pinf == null) {
/* 5773 */       return false;
/*      */     }
/* 5775 */     pinf.load();
/* 5776 */     if (pinf.getPower() <= power) {
/* 5777 */       return false;
/*      */     }
/* 5779 */     String pw = LoginHandler.encrypt(pinf.getName() + password);
/* 5780 */     if (pw.equals(pinf.getPassword()))
/* 5781 */       return true; 
/* 5782 */     return false;
/*      */   }
/*      */   
/*      */   private void interactiveShutdown() {
/* 5786 */     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/* 5787 */     int state = 0;
/* 5788 */     boolean interactive = true;
/* 5789 */     String user = "";
/* 5790 */     String message = "Maintenance shutdown. Up to thirty minutes downtime. See the forums for more information: http://forum.wurmonline.com/";
/* 5791 */     int delay = 1800;
/* 5792 */     System.out.println("[Shutdown Servers]\n(Type 'quit' at any time to abort)");
/* 5793 */     while (interactive) {
/*      */       try {
/*      */         String password, in;
/*      */         
/* 5797 */         switch (state) {
/*      */           
/*      */           case 0:
/* 5800 */             System.out.print("GM Name: ");
/* 5801 */             user = br.readLine().trim();
/* 5802 */             state = 1;
/*      */             break;
/*      */           case 1:
/* 5805 */             System.out.print("GM password: ");
/* 5806 */             password = br.readLine().trim();
/* 5807 */             if (!validateAccount(user, password, (byte)4)) {
/*      */               
/* 5809 */               interactive = false;
/* 5810 */               System.out.println("Invalid password or power level insufficient.");
/*      */               return;
/*      */             } 
/* 5813 */             state = 2;
/*      */             break;
/*      */           case 2:
/* 5816 */             System.out.print("Message: [default '" + message + "'] ");
/* 5817 */             in = br.readLine().trim();
/* 5818 */             if (!in.isEmpty())
/* 5819 */               message = in; 
/* 5820 */             state = 3;
/* 5821 */             in = "";
/*      */             break;
/*      */           case 3:
/* 5824 */             System.out.print("Delay: [default '" + delay + "']");
/* 5825 */             in = br.readLine().trim();
/* 5826 */             if (!in.isEmpty())
/* 5827 */               delay = Integer.valueOf(in).intValue(); 
/* 5828 */             state = 4;
/*      */             break;
/*      */         } 
/*      */         
/* 5832 */         String s = br.readLine();
/* 5833 */         System.out.print("Enter Integer:");
/* 5834 */         int i = Integer.parseInt(br.readLine());
/*      */       }
/* 5836 */       catch (NumberFormatException nfe) {
/*      */         
/* 5838 */         System.err.println("Invalid Format!");
/*      */       }
/* 5840 */       catch (Exception exception) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void requestDemigod(String intraServerPassword, byte existingDeity, String existingDeityName) throws RemoteException {
/* 5858 */     validateIntraServerPassword(intraServerPassword);
/*      */     
/* 5860 */     Player[] players = Players.getInstance().getPlayers();
/* 5861 */     for (int x = 0; x < players.length; x++) {
/*      */       
/* 5863 */       if (players[x].getKingdomTemplateId() == Deities.getFavoredKingdom(existingDeity) && (players[x]
/* 5864 */         .getPower() == 0 || Servers.localServer.testServer)) {
/*      */         
/* 5866 */         MissionPerformer mp = MissionPerformed.getMissionPerformer(players[x].getWurmId());
/* 5867 */         if (mp != null) {
/*      */           
/* 5869 */           MissionPerformed[] perfs = mp.getAllMissionsPerformed();
/* 5870 */           int numsForDeity = 0;
/* 5871 */           logger.log(Level.INFO, "Checking if " + players[x].getName() + " can be elevated.");
/* 5872 */           for (MissionPerformed mpf : perfs) {
/*      */             
/* 5874 */             Mission m = mpf.getMission();
/* 5875 */             if (m != null) {
/*      */               
/* 5877 */               logger.log(Level.INFO, "Found a mission for " + existingDeityName);
/* 5878 */               if (m.getCreatorType() == 2)
/*      */               {
/* 5880 */                 if (m.getOwnerId() == existingDeity)
/*      */                 {
/* 5882 */                   numsForDeity++;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/* 5888 */           logger.log(Level.INFO, "Found " + numsForDeity + " missions for " + players[x].getName());
/* 5889 */           if (Server.rand.nextInt(numsForDeity) > 2) {
/*      */             
/* 5891 */             logger.log(Level.INFO, "Sending ascension to " + players[x].getName());
/* 5892 */             AscensionQuestion asc = new AscensionQuestion((Creature)players[x], existingDeity, existingDeityName);
/* 5893 */             asc.sendQuestion();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ascend(String intraServerPassword, int newId, String deityName, long wurmid, byte existingDeity, byte gender, byte newPower, float initialBStr, float initialBSta, float initialBCon, float initialML, float initialMS, float initialSS, float initialSD) {
/*      */     try {
/* 5907 */       validateIntraServerPassword(intraServerPassword);
/*      */     }
/* 5909 */     catch (AccessException e) {
/*      */ 
/*      */       
/* 5912 */       e.printStackTrace();
/*      */     } 
/*      */     
/* 5915 */     String toReturn = "";
/*      */     
/* 5917 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 5919 */       Deity deity = null;
/* 5920 */       if (newPower == 2)
/*      */       {
/* 5922 */         deity = Deities.ascend(newId, deityName, wurmid, gender, newPower, -1.0F, -1.0F);
/* 5923 */         if (deity != null) {
/*      */           
/* 5925 */           StringBuilder builder = new StringBuilder("You have now ascended! ");
/* 5926 */           if (initialBStr < 30.0F) {
/* 5927 */             builder.append("The other immortals will not fear your strength initially. ");
/* 5928 */           } else if (initialBStr < 45.0F) {
/* 5929 */             builder.append("You have acceptable strength as a demigod. ");
/* 5930 */           } else if (initialBStr < 60.0F) {
/* 5931 */             builder.append("Your strength and skills will impress other immortals. ");
/*      */           } else {
/* 5933 */             builder.append("Your enormous strength will strike fear in other immortals. ");
/*      */           } 
/* 5935 */           if (initialBSta < 30.0F) {
/* 5936 */             builder.append("You are not the most vital demigod around so you will have to watch your back in the beginning. ");
/* 5937 */           } else if (initialBSta < 45.0F) {
/* 5938 */             builder.append("Your vitality is acceptable and will earn respect. ");
/* 5939 */           } else if (initialBSta < 60.0F) {
/* 5940 */             builder.append("You have good vitality and can expect a bright future as immortal. ");
/*      */           } else {
/* 5942 */             builder.append("Other immortals will envy your fantastic vitality and avoid confrontations with you. ");
/*      */           } 
/* 5944 */           if (deity.isHealer()) {
/* 5945 */             builder.append("Your love and kindness will be a beacon for everyone to follow. ");
/* 5946 */           } else if (deity.isHateGod()) {
/* 5947 */             builder.append("Your true nature turns out to be based on rage and hate. ");
/* 5948 */           }  if (deity.isForestGod())
/* 5949 */             builder.append("Love for trees and living things will bind your followers together. "); 
/* 5950 */           if (deity.isMountainGod())
/* 5951 */             builder.append("Your followers will look for you in high places and fear and adore you as they do the dragon. "); 
/* 5952 */           if (deity.isWaterGod())
/* 5953 */             builder.append("You will be considered the pathfinder and explorer of your kin. "); 
/* 5954 */           HexMap.VALREI.addDemigod(deityName, deity.number, existingDeity, initialBStr, initialBSta, initialBCon, initialML, initialMS, initialSS, initialSD);
/*      */           
/* 5956 */           toReturn = builder.toString();
/*      */         } else {
/*      */           
/* 5959 */           return "Ouch, failed to save your demigod on the login server. Please contact administration";
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 5965 */       else if (newPower > 2)
/*      */       {
/* 5967 */         String sgender = "He";
/* 5968 */         String sposs = "his";
/* 5969 */         if (gender == 1) {
/*      */           
/* 5971 */           sgender = "She";
/* 5972 */           sposs = "her";
/*      */         } 
/* 5974 */         Servers.ascend(newId, deityName, wurmid, existingDeity, gender, newPower, initialBStr, initialBSta, initialBCon, initialML, initialMS, initialSS, initialSD);
/*      */         
/* 5976 */         HistoryManager.addHistory(deityName, "has joined the ranks of true deities. " + sgender + " invites you to join " + sposs + " religion, as " + sgender
/* 5977 */             .toLowerCase() + " will now forever partake in the hunts on Valrei!");
/* 5978 */         Server.getInstance().broadCastSafe(deityName + " has joined the ranks of true deities. " + sgender + " invites you to join " + sposs + " religion, as " + sgender
/*      */             
/* 5980 */             .toLowerCase() + " will now forever partake in the hunts on Valrei!");
/*      */       }
/*      */     
/*      */     }
/* 5984 */     else if (newPower > 2) {
/*      */       
/* 5986 */       Deities.ascend(newId, deityName, wurmid, gender, newPower, -1.0F, -1.0F);
/* 5987 */       String sgender = "He";
/* 5988 */       String sposs = "his";
/* 5989 */       if (gender == 1) {
/*      */         
/* 5991 */         sgender = "She";
/* 5992 */         sposs = "her";
/*      */       } 
/* 5994 */       HistoryManager.addHistory(deityName, "has joined the ranks of true deities. " + sgender + " invites you to join " + sposs + " religion, as " + sgender
/* 5995 */           .toLowerCase() + " will now forever partake in the hunts on Valrei!");
/* 5996 */       Server.getInstance().broadCastSafe(deityName + " has joined the ranks of true deities. " + sgender + " invites you to join " + sposs + " religion, as " + sgender
/*      */           
/* 5998 */           .toLowerCase() + " will now forever partake in the hunts on Valrei!");
/*      */     } 
/* 6000 */     return toReturn;
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
/* 6057 */   static final int[] emptyIntZero = new int[] { 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] getPremTimeSilvers(String intraServerPassword, long wurmId) throws RemoteException {
/* 6063 */     validateIntraServerPassword(intraServerPassword);
/* 6064 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/* 6065 */     if (info != null) {
/*      */       
/*      */       try {
/*      */         
/* 6069 */         if (!info.loaded)
/* 6070 */           info.load(); 
/* 6071 */         if (info.getPaymentExpire() > 0L)
/*      */         {
/* 6073 */           if (info.awards != null)
/*      */           {
/*      */             
/* 6076 */             int[] toReturn = { info.awards.getMonthsPaidEver(), info.awards.getSilversPaidEver() };
/* 6077 */             return toReturn;
/*      */           }
/*      */         
/*      */         }
/* 6081 */       } catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6086 */     return emptyIntZero;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void awardPlayer(String intraServerPassword, long wurmid, String name, int days, int months) throws RemoteException {
/* 6093 */     validateIntraServerPassword(intraServerPassword);
/* 6094 */     Server.addPendingAward(new PendingAward(wurmid, name, days, months));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean requestDeityMove(String intraServerPassword, int deityNum, int desiredHex, String guide) throws RemoteException {
/* 6101 */     validateIntraServerPassword(intraServerPassword);
/* 6102 */     if (Servers.localServer.LOGINSERVER) {
/*      */       
/* 6104 */       EpicEntity entity = HexMap.VALREI.getEntity(deityNum);
/* 6105 */       if (entity != null) {
/*      */         
/* 6107 */         logger.log(Level.INFO, "Requesting move for " + entity);
/* 6108 */         MapHex mh = HexMap.VALREI.getMapHex(desiredHex);
/* 6109 */         if (mh != null) {
/*      */           
/* 6111 */           entity.setNextTargetHex(desiredHex);
/* 6112 */           entity.broadCastWithName(" was guided by " + guide + " towards " + mh.getName() + ".");
/* 6113 */           entity.sendEntityData();
/* 6114 */           return true;
/*      */         } 
/*      */         
/* 6117 */         logger.log(Level.INFO, "No hex for " + desiredHex);
/*      */       } else {
/*      */         
/* 6120 */         logger.log(Level.INFO, "Requesting move for nonexistant " + deityNum);
/*      */       } 
/* 6122 */     }  return false;
/*      */   }
/*      */   
/*      */   private void validateIntraServerPassword(String intraServerPassword) throws AccessException {
/* 6126 */     if (!Servers.localServer.INTRASERVERPASSWORD.equals(intraServerPassword)) {
/* 6127 */       throw new AccessException("Access denied.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFeatureEnabled(String intraServerPassword, int aFeatureId) throws RemoteException {
/* 6134 */     validateIntraServerPassword(intraServerPassword);
/* 6135 */     if (logger.isLoggable(Level.FINER))
/* 6136 */       logger.finer(getRemoteClientDetails() + " isFeatureEnabled " + aFeatureId); 
/* 6137 */     return Features.Feature.isFeatureEnabled(aFeatureId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setPlayerFlag(String intraServerPassword, long wurmid, int flag, boolean set) throws RemoteException {
/* 6143 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setPlayerFlag(long wurmid, int flag, boolean set) {
/* 6151 */     PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/* 6152 */     if (pinf != null) {
/*      */       
/* 6154 */       pinf.setFlag(flag, set);
/* 6155 */       return true;
/*      */     } 
/* 6157 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WebInterfaceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */