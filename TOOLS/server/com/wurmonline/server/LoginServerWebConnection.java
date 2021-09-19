/*      */ package com.wurmonline.server;
/*      */ 
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.epic.EpicEntity;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.players.Ban;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.webinterface.WcCreateEpicMission;
/*      */ import com.wurmonline.server.webinterface.WebCommand;
/*      */ import com.wurmonline.server.webinterface.WebInterface;
/*      */ import com.wurmonline.server.webinterface.WebInterfaceImpl;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.rmi.Naming;
/*      */ import java.rmi.NotBoundException;
/*      */ import java.rmi.RemoteException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class LoginServerWebConnection
/*      */ {
/*   54 */   private WebInterface wurm = null;
/*      */   
/*   56 */   private static Logger logger = Logger.getLogger(LoginServerWebConnection.class.getName());
/*   57 */   private int serverId = Servers.loginServer.id;
/*      */   
/*      */   private static final char EXCLAMATION_MARK = '!';
/*      */   
/*      */   private static final String FAILED_TO_CREATE_TRINKET = ", failed to create trinket! ";
/*      */   
/*      */   private static final String YOU_RECEIVED = "You received ";
/*      */   
/*      */   private static final String AN_ERROR_OCCURRED_WHEN_CONTACTING_THE_LOGIN_SERVER = "An error occurred when contacting the login server. Please try later.";
/*      */   
/*      */   private static final String FAILED_TO_CONTACT_THE_LOGIN_SERVER = "Failed to contact the login server ";
/*      */   
/*      */   private static final String FAILED_TO_CONTACT_THE_LOGIN_SERVER_PLEASE_TRY_LATER = "Failed to contact the login server. Please try later.";
/*      */   
/*      */   private static final String FAILED_TO_CONTACT_THE_BANK_PLEASE_TRY_LATER = "Failed to contact the bank. Please try later.";
/*      */   
/*      */   private static final String GAME_SERVER_IS_CURRENTLY_UNAVAILABLE = "The game server is currently unavailable.";
/*      */   
/*      */   private static final char COLON_CHAR = ':';
/*      */   
/*   77 */   private String intraServerPassword = Servers.localServer.INTRASERVERPASSWORD;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoginServerWebConnection(int aServerId) {
/*   87 */     this.serverId = aServerId;
/*      */   }
/*      */ 
/*      */   
/*      */   private void connect() throws MalformedURLException, RemoteException, NotBoundException {
/*   92 */     if (this.wurm == null)
/*      */     {
/*   94 */       if (Servers.localServer.id == this.serverId) {
/*      */         
/*   96 */         this.wurm = (WebInterface)new WebInterfaceImpl();
/*      */       }
/*      */       else {
/*      */         
/*  100 */         long lStart = System.nanoTime();
/*      */         
/*  102 */         String name = null;
/*      */         
/*      */         try {
/*  105 */           ServerEntry server = Servers.getServerWithId(this.serverId);
/*  106 */           if (server == null)
/*  107 */             throw new RemoteException("Server " + this.serverId + " not found"); 
/*  108 */           if (!server.isAvailable(5, true))
/*  109 */             throw new RemoteException("Server unavailable"); 
/*  110 */           this.intraServerPassword = server.INTRASERVERPASSWORD;
/*  111 */           name = "//" + server.INTRASERVERADDRESS + ':' + server.RMI_PORT + "/" + "wuinterface";
/*      */           
/*  113 */           this.wurm = (WebInterface)Naming.lookup(name);
/*      */         }
/*      */         finally {
/*      */           
/*  117 */           if (logger.isLoggable(Level.FINE))
/*      */           {
/*  119 */             logger.fine("Looking up WebInterface RMI: " + name + " took " + ((float)(System.nanoTime() - lStart) / 1000000.0F) + "ms.");
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getServerId() {
/*  129 */     return this.serverId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] createAndReturnPlayer(String playerName, String hashedIngamePassword, String challengePhrase, String challengeAnswer, String emailAddress, byte kingdom, byte power, long appearance, byte gender, boolean titleKeeper, boolean addPremium, boolean passwordIsHashed) throws Exception {
/*  137 */     if (this.wurm == null)
/*      */     {
/*  139 */       connect();
/*      */     }
/*  141 */     if (this.wurm != null)
/*      */     {
/*  143 */       return this.wurm.createAndReturnPlayer(this.intraServerPassword, playerName, hashedIngamePassword, challengePhrase, challengeAnswer, emailAddress, kingdom, power, appearance, gender, titleKeeper, addPremium, passwordIsHashed);
/*      */     }
/*      */     
/*  146 */     throw new RemoteException("Failed to create web connection.");
/*      */   }
/*      */ 
/*      */   
/*      */   public long chargeMoney(String playerName, long moneyToCharge) {
/*  151 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  155 */         connect();
/*      */       }
/*  157 */       catch (Exception ex) {
/*      */         
/*  159 */         logger.log(Level.WARNING, playerName + " + Failed to contact the login server " + ex.getMessage());
/*      */         
/*  161 */         return -10L;
/*      */       } 
/*      */     }
/*  164 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  168 */         return this.wurm.chargeMoney(this.intraServerPassword, playerName, moneyToCharge);
/*      */       }
/*  170 */       catch (RemoteException rx) {
/*      */         
/*  172 */         return -10L;
/*      */       } 
/*      */     }
/*  175 */     return -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addPlayingTime(Creature player, String name, int months, int days, String detail) {
/*  181 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  185 */         connect();
/*      */       }
/*  187 */       catch (Exception ex) {
/*      */         
/*  189 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  190 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */         
/*  192 */         return false;
/*      */       } 
/*      */     }
/*  195 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  199 */         Map<String, String> result = this.wurm.addPlayingTime(this.intraServerPassword, name, months, days, detail, (Servers.localServer.testServer || player
/*  200 */             .getPower() > 0));
/*  201 */         for (Map.Entry<String, String> e : result.entrySet())
/*      */         {
/*  203 */           if (((String)e.getKey()).equals("error")) {
/*      */             
/*  205 */             player.getCommunicator().sendAlertServerMessage(e.getValue());
/*  206 */             return false;
/*      */           } 
/*  208 */           if (((String)e.getKey()).equals("ok"))
/*      */           {
/*  210 */             return true;
/*      */           }
/*      */         }
/*      */       
/*  214 */       } catch (RemoteException rx) {
/*      */         
/*  216 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  217 */         return false;
/*      */       } 
/*      */     }
/*  220 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addMoney(Creature player, String name, long money, String detail) {
/*  225 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  229 */         connect();
/*      */       }
/*  231 */       catch (Exception ex) {
/*      */         
/*  233 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  234 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */         
/*  236 */         return false;
/*      */       } 
/*      */     }
/*  239 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  243 */         Map<String, String> result = this.wurm.addMoneyToBank(this.intraServerPassword, name, money, detail, false);
/*  244 */         for (Map.Entry<String, String> e : result.entrySet())
/*      */         {
/*  246 */           if (((String)e.getKey()).equals("error")) {
/*      */             
/*  248 */             player.getCommunicator().sendAlertServerMessage(e.getValue());
/*  249 */             return false;
/*      */           } 
/*  251 */           if (((String)e.getKey()).equals("ok"))
/*      */           {
/*  253 */             return true;
/*      */           }
/*      */         }
/*      */       
/*  257 */       } catch (RemoteException rx) {
/*      */         
/*  259 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  260 */         return false;
/*      */       } 
/*      */     }
/*  263 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getMoney(Creature player) {
/*  268 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  272 */         connect();
/*      */       }
/*  274 */       catch (Exception ex) {
/*      */         
/*  276 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  277 */         logger.log(Level.WARNING, player.getName() + " " + "Failed to contact the login server " + " " + this.serverId + " " + ex
/*  278 */             .getMessage());
/*      */         
/*  280 */         return 0L;
/*      */       } 
/*      */     }
/*  283 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  288 */         return this.wurm.getMoney(this.intraServerPassword, player.getWurmId(), player.getName());
/*      */       }
/*  290 */       catch (RemoteException rx) {
/*      */         
/*  292 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the bank. Please try later.");
/*  293 */         return 0L;
/*      */       } 
/*      */     }
/*  296 */     return 0L;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addMoney(long wurmid, String name, long money, String detail) {
/*  301 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  305 */         connect();
/*      */       }
/*  307 */       catch (Exception ex) {
/*      */         
/*  309 */         logger.log(Level.WARNING, wurmid + ": failed to receive " + money + ", " + detail + ", " + ex.getMessage());
/*  310 */         return false;
/*      */       } 
/*      */     }
/*  313 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  317 */         Map<String, String> result = this.wurm.addMoneyToBank(this.intraServerPassword, name, wurmid, money, detail, false);
/*      */         
/*  319 */         for (Map.Entry<String, String> e : result.entrySet())
/*      */         {
/*  321 */           if (((String)e.getKey()).equals("error")) {
/*      */             
/*  323 */             logger.log(Level.WARNING, wurmid + ": failed to receive " + money + ", " + detail + ", " + (String)e.getValue());
/*  324 */             return false;
/*      */           } 
/*  326 */           if (((String)e.getKey()).equals("ok"))
/*      */           {
/*  328 */             return true;
/*      */           }
/*      */         }
/*      */       
/*  332 */       } catch (RemoteException rx) {
/*      */         
/*  334 */         logger.log(Level.WARNING, wurmid + ": failed to receive " + money + ", " + detail + ", " + rx, rx);
/*  335 */         return false;
/*      */       } 
/*      */     }
/*  338 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void testAdding(String playerName) {
/*  343 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  347 */         connect();
/*      */       }
/*  349 */       catch (Exception ex) {
/*      */         
/*  351 */         logger.log(Level.WARNING, playerName + ": " + ex.getMessage(), ex);
/*  352 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*      */     try {
/*  358 */       Map<String, String> result = this.wurm.addPlayingTime(this.intraServerPassword, playerName, 1, 4, "test" + 
/*  359 */           System.currentTimeMillis());
/*  360 */       for (Map.Entry<String, String> e : result.entrySet())
/*      */       {
/*  362 */         logger.log(Level.INFO, (String)e.getKey() + ':' + (String)e.getValue());
/*      */       }
/*  364 */       Map<String, String> result2 = this.wurm.addMoneyToBank(this.intraServerPassword, playerName, 10000L, "test" + 
/*  365 */           System.currentTimeMillis());
/*  366 */       for (Map.Entry<String, String> e : result2.entrySet())
/*      */       {
/*  368 */         logger.log(Level.INFO, (String)e.getKey() + ':' + (String)e.getValue());
/*      */       }
/*      */     }
/*  371 */     catch (RemoteException rx) {
/*      */       
/*  373 */       logger.log(Level.WARNING, rx.getMessage(), rx);
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWeather(float windRotation, float windpower, float windDir) {
/*  380 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  384 */         connect();
/*      */       }
/*  386 */       catch (Exception ex) {
/*      */         return;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  392 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  396 */         this.wurm.setWeather(this.intraServerPassword, windRotation, windpower, windDir);
/*      */       }
/*  398 */       catch (RemoteException rx) {
/*      */         return;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Byte> getReferrers(Creature player, long wurmid) {
/*  408 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  412 */         connect();
/*      */       }
/*  414 */       catch (Exception ex) {
/*      */         
/*  416 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the login server. Please try later.");
/*  417 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*  418 */         return null;
/*      */       } 
/*      */     }
/*  421 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  425 */         return this.wurm.getReferrers(this.intraServerPassword, wurmid);
/*      */       }
/*  427 */       catch (RemoteException rx) {
/*      */         
/*  429 */         player.getCommunicator().sendAlertServerMessage("An error occurred when contacting the login server. Please try later.");
/*      */       } 
/*      */     }
/*  432 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addReferrer(Player player, String receiver) {
/*  437 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  441 */         connect();
/*      */       }
/*  443 */       catch (Exception ex) {
/*      */         
/*  445 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the login server. Please try later.");
/*  446 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */         return;
/*      */       } 
/*      */     }
/*  450 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  454 */         String mess = this.wurm.addReferrer(this.intraServerPassword, receiver, player.getWurmId());
/*      */         
/*      */         try {
/*  457 */           long referrer = Long.parseLong(mess);
/*  458 */           player.getSaveFile().setReferedby(referrer);
/*  459 */           player.getCommunicator().sendNormalServerMessage("Okay, you have set " + receiver + " as your referrer.");
/*      */         }
/*  461 */         catch (NumberFormatException nfe) {
/*      */           
/*  463 */           player.getCommunicator().sendNormalServerMessage(mess);
/*      */         }
/*      */       
/*  466 */       } catch (RemoteException rx) {
/*      */         
/*  468 */         player.getCommunicator().sendAlertServerMessage("An error occurred when contacting the login server. Please try later.");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void acceptReferrer(Creature player, String referrerName, boolean money) {
/*  475 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  479 */         connect();
/*      */       }
/*  481 */       catch (Exception ex) {
/*      */         
/*  483 */         player.getCommunicator().sendAlertServerMessage("Failed to contact the login server. Please try later.");
/*  484 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */         return;
/*      */       } 
/*      */     }
/*  488 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  492 */         player.getCommunicator().sendNormalServerMessage(this.wurm
/*  493 */             .acceptReferrer(this.intraServerPassword, player.getWurmId(), referrerName, money));
/*      */       }
/*  495 */       catch (RemoteException rx) {
/*      */         
/*  497 */         player.getCommunicator().sendAlertServerMessage("An error occurred when contacting the login server. Please try later.");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String getReimburseInfo(Player player) {
/*  504 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  508 */         connect();
/*      */       }
/*  510 */       catch (Exception ex) {
/*      */         
/*  512 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*  513 */         return "Failed to contact the login server. Please try later.";
/*      */       } 
/*      */     }
/*  516 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  520 */         return this.wurm.getReimbursementInfo(this.intraServerPassword, (player.getSaveFile()).emailAddress);
/*      */       }
/*  522 */       catch (RemoteException rx) {
/*      */         
/*  524 */         return "An error occurred when contacting the login server. Please try later.";
/*      */       } 
/*      */     }
/*  527 */     return "Failed to contact the login server. Please try later.";
/*      */   }
/*      */ 
/*      */   
/*      */   public long[] getCurrentServer(String name, long wurmid) throws Exception {
/*  532 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  536 */         connect();
/*      */       }
/*  538 */       catch (Exception ex) {
/*      */         
/*  540 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*  541 */         throw new WurmServerException("Failed to contact the login server. Please try later.");
/*      */       } 
/*      */     }
/*  544 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  548 */         return this.wurm.getCurrentServerAndWurmid(this.intraServerPassword, name, wurmid);
/*      */       }
/*  550 */       catch (RemoteException rx) {
/*      */         
/*  552 */         throw new WurmServerException("An error occurred when contacting the login server. Please try later.", rx);
/*      */       } 
/*      */     }
/*  555 */     throw new WurmServerException("Failed to contact the login server. Please try later.");
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Long, byte[]> getPlayerStates(long[] wurmids) throws WurmServerException {
/*  560 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  564 */         connect();
/*      */       }
/*  566 */       catch (Exception ex) {
/*      */         
/*  568 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*  569 */         throw new WurmServerException("Failed to contact the login server. Please try later.");
/*      */       } 
/*      */     }
/*  572 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  576 */         return this.wurm.getPlayerStates(this.intraServerPassword, wurmids);
/*      */       }
/*  578 */       catch (RemoteException rx) {
/*      */         
/*  580 */         throw new WurmServerException("An error occurred when contacting the login server. Please try later.", rx);
/*      */       } 
/*      */     }
/*  583 */     throw new WurmServerException("Failed to contact the login server. Please try later.");
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
/*      */   public void manageFeature(int aServerId, int featureId, boolean aOverridden, boolean aEnabled, boolean global) {
/*  597 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  601 */         connect();
/*      */       }
/*  603 */       catch (Exception ex) {
/*      */         
/*  605 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */       } 
/*      */     }
/*      */     
/*  609 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  613 */         this.wurm.manageFeature(this.intraServerPassword, aServerId, featureId, aOverridden, aEnabled, global);
/*      */       }
/*  615 */       catch (RemoteException rx) {
/*      */         
/*  617 */         logger.log(Level.WARNING, "An error occurred when contacting the login server. Please try later. " + this.serverId + " " + rx
/*  618 */             .getMessage());
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
/*      */   public void startShutdown(String instigator, int seconds, String reason) {
/*  632 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  636 */         connect();
/*      */       }
/*  638 */       catch (Exception ex) {
/*      */         
/*  640 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */       } 
/*      */     }
/*      */     
/*  644 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  648 */         this.wurm.startShutdown(this.intraServerPassword, instigator, seconds, reason);
/*      */       }
/*  650 */       catch (RemoteException rx) {
/*      */         
/*  652 */         logger.log(Level.WARNING, "An error occurred when contacting the login server. Please try later. " + this.serverId + " " + rx
/*  653 */             .getMessage());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String withDraw(Player player, String name, String _email, int _months, int _silvers, boolean titlebok, boolean mbok, int _daysLeft) {
/*  662 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  666 */         connect();
/*      */       }
/*  668 */       catch (Exception ex) {
/*      */         
/*  670 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*  671 */         return "Failed to contact the login server. Please try later.";
/*      */       } 
/*      */     }
/*  674 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  678 */         if (this.wurm.withDraw(this.intraServerPassword, player.getName(), name, _email, _months, _silvers, titlebok, _daysLeft)) {
/*      */           
/*  680 */           if (titlebok) {
/*      */ 
/*      */             
/*      */             try {
/*      */               
/*  685 */               Item bok = ItemFactory.createItem(443, 99.0F, player.getName());
/*  686 */               if (mbok) {
/*      */                 
/*  688 */                 bok.setName("Master bag of keeping");
/*  689 */                 bok.setSizes(3, 10, 20);
/*      */               } 
/*  691 */               player.getInventory().insertItem(bok, true);
/*  692 */               player.getCommunicator().sendSafeServerMessage("You received " + bok
/*  693 */                   .getNameWithGenus() + '!');
/*      */             }
/*  695 */             catch (FailedException fe) {
/*      */               
/*  697 */               logger.log(Level.WARNING, player.getName() + ", failed to create bok! " + fe.getMessage(), (Throwable)fe);
/*      */             }
/*  699 */             catch (NoSuchTemplateException nsi) {
/*      */               
/*  701 */               logger.log(Level.WARNING, player.getName() + ", failed to create bok! " + nsi.getMessage(), (Throwable)nsi);
/*      */             } 
/*  703 */             player.addTitle(Titles.Title.Ageless);
/*  704 */             if (mbok)
/*  705 */               player.addTitle(Titles.Title.KeeperTruth); 
/*      */           } 
/*  707 */           if (_months > 0) {
/*      */ 
/*      */             
/*      */             try {
/*  711 */               Item spyglass = ItemFactory.createItem(489, 80.0F + Server.rand.nextInt(20), player
/*  712 */                   .getName());
/*  713 */               player.getInventory().insertItem(spyglass, true);
/*  714 */               player.getCommunicator().sendSafeServerMessage("You received " + spyglass
/*  715 */                   .getNameWithGenus() + '!');
/*      */             }
/*  717 */             catch (FailedException fe) {
/*      */               
/*  719 */               logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */             }
/*  721 */             catch (NoSuchTemplateException nsi) {
/*      */               
/*  723 */               logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */             } 
/*  725 */             Item trinket = null;
/*  726 */             if (_months > 1) {
/*      */ 
/*      */               
/*      */               try {
/*  730 */                 trinket = ItemFactory.createItem(509, 80.0F, player.getName());
/*  731 */                 player.getInventory().insertItem(trinket, true);
/*  732 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  733 */                     .getNameWithGenus() + '!');
/*      */               }
/*  735 */               catch (FailedException fe) {
/*      */                 
/*  737 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */               }
/*  739 */               catch (NoSuchTemplateException nsi) {
/*      */                 
/*  741 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */               } 
/*      */               
/*      */               try {
/*  745 */                 trinket = ItemFactory.createItem(93, 30.0F, player.getName());
/*  746 */                 player.getInventory().insertItem(trinket, true);
/*  747 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  748 */                     .getNameWithGenus() + '!');
/*  749 */                 trinket = ItemFactory.createItem(79, 30.0F, player.getName());
/*  750 */                 player.getInventory().insertItem(trinket, true);
/*  751 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  752 */                     .getNameWithGenus() + '!');
/*  753 */                 trinket = ItemFactory.createItem(20, 30.0F, player.getName());
/*  754 */                 player.getInventory().insertItem(trinket, true);
/*  755 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  756 */                     .getNameWithGenus() + '!');
/*  757 */                 trinket = ItemFactory.createItem(313, 40.0F, player.getName());
/*  758 */                 player.getInventory().insertItem(trinket, true);
/*  759 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  760 */                     .getNameWithGenus() + '!');
/*  761 */                 trinket = ItemFactory.createItem(8, 30.0F, player.getName());
/*  762 */                 player.getInventory().insertItem(trinket, true);
/*  763 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  764 */                     .getNameWithGenus() + '!');
/*  765 */                 trinket = ItemFactory.createItem(90, 30.0F, player.getName());
/*  766 */                 player.getInventory().insertItem(trinket, true);
/*  767 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  768 */                     .getNameWithGenus() + '!');
/*      */               }
/*  770 */               catch (FailedException fe) {
/*      */                 
/*  772 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */               }
/*  774 */               catch (NoSuchTemplateException nsi) {
/*      */                 
/*  776 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */               } 
/*      */             } 
/*  779 */             if (_months > 2) {
/*      */               
/*      */               try {
/*      */                 
/*  783 */                 trinket = ItemFactory.createItem(105, 30.0F, player.getName());
/*  784 */                 player.getInventory().insertItem(trinket, true);
/*  785 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  786 */                     .getNameWithGenus() + '!');
/*      */                 
/*  788 */                 trinket = ItemFactory.createItem(105, 30.0F, player.getName());
/*  789 */                 player.getInventory().insertItem(trinket, true);
/*  790 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  791 */                     .getNameWithGenus() + '!');
/*      */                 
/*  793 */                 trinket = ItemFactory.createItem(107, 30.0F, player.getName());
/*  794 */                 player.getInventory().insertItem(trinket, true);
/*  795 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  796 */                     .getNameWithGenus() + '!');
/*      */                 
/*  798 */                 trinket = ItemFactory.createItem(103, 30.0F, player.getName());
/*  799 */                 player.getInventory().insertItem(trinket, true);
/*  800 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  801 */                     .getNameWithGenus() + '!');
/*      */                 
/*  803 */                 trinket = ItemFactory.createItem(103, 30.0F, player.getName());
/*  804 */                 player.getInventory().insertItem(trinket, true);
/*  805 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  806 */                     .getNameWithGenus() + '!');
/*      */                 
/*  808 */                 trinket = ItemFactory.createItem(108, 30.0F, player.getName());
/*  809 */                 player.getInventory().insertItem(trinket, true);
/*  810 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  811 */                     .getNameWithGenus() + '!');
/*      */                 
/*  813 */                 trinket = ItemFactory.createItem(104, 30.0F, player.getName());
/*  814 */                 player.getInventory().insertItem(trinket, true);
/*  815 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  816 */                     .getNameWithGenus() + '!');
/*      */                 
/*  818 */                 trinket = ItemFactory.createItem(106, 30.0F, player.getName());
/*  819 */                 player.getInventory().insertItem(trinket, true);
/*  820 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  821 */                     .getNameWithGenus() + '!');
/*      */                 
/*  823 */                 trinket = ItemFactory.createItem(106, 30.0F, player.getName());
/*  824 */                 player.getInventory().insertItem(trinket, true);
/*  825 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  826 */                     .getNameWithGenus() + '!');
/*      */                 
/*  828 */                 trinket = ItemFactory.createItem(4, 30.0F, player.getName());
/*  829 */                 player.getInventory().insertItem(trinket, true);
/*  830 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  831 */                     .getNameWithGenus() + '!');
/*      */               }
/*  833 */               catch (FailedException fe) {
/*      */                 
/*  835 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */               }
/*  837 */               catch (NoSuchTemplateException nsi) {
/*      */                 
/*  839 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */               } 
/*      */             }
/*  842 */             if (_months > 3) {
/*      */               
/*      */               try {
/*      */                 
/*  846 */                 trinket = ItemFactory.createItem(135, 50.0F, player.getName());
/*  847 */                 player.getInventory().insertItem(trinket, true);
/*  848 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  849 */                     .getNameWithGenus() + '!');
/*  850 */                 trinket = ItemFactory.createItem(480, 70.0F, player.getName());
/*  851 */                 player.getInventory().insertItem(trinket, true);
/*  852 */                 player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  853 */                     .getNameWithGenus() + '!');
/*      */               }
/*  855 */               catch (FailedException fe) {
/*      */                 
/*  857 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */               }
/*  859 */               catch (NoSuchTemplateException nsi) {
/*      */                 
/*  861 */                 logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */               } 
/*      */             }
/*  864 */             if (_months > 4)
/*      */             {
/*  866 */               for (int x = 0; x < 3; x++) {
/*      */ 
/*      */                 
/*      */                 try {
/*  870 */                   trinket = ItemFactory.createItem(509, 80.0F, player.getName());
/*  871 */                   player.getInventory().insertItem(trinket, true);
/*  872 */                   player.getCommunicator().sendSafeServerMessage("You received " + trinket
/*  873 */                       .getNameWithGenus() + '!');
/*      */                 }
/*  875 */                 catch (FailedException fe) {
/*      */                   
/*  877 */                   logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + fe.getMessage(), (Throwable)fe);
/*      */                 }
/*  879 */                 catch (NoSuchTemplateException nsi) {
/*      */                   
/*  881 */                   logger.log(Level.WARNING, player.getName() + ", failed to create trinket! " + nsi.getMessage(), (Throwable)nsi);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */           
/*  887 */           return "You have been reimbursed.";
/*      */         } 
/*      */         
/*  890 */         return "There was an error with your request. The server may be unavailable. You may also want to verify the amounts entered.";
/*      */       }
/*  892 */       catch (RemoteException rx) {
/*      */         
/*  894 */         return "An error occurred when contacting the login server. Please try later.";
/*      */       } 
/*      */     }
/*  897 */     return "Failed to contact the login server. Please try later.";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean transferPlayer(Player player, String playerName, int posx, int posy, boolean surfaced, byte[] data) {
/*  903 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  907 */         connect();
/*      */       }
/*  909 */       catch (Exception ex) {
/*      */         
/*  911 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + "," + ex.getMessage());
/*  912 */         if (player != null)
/*  913 */           player.getCommunicator().sendAlertServerMessage("Failed to contact the login server. Please try later."); 
/*  914 */         return false;
/*      */       } 
/*      */     }
/*  917 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  921 */         if (!this.wurm.transferPlayer(this.intraServerPassword, playerName, posx, posy, surfaced, player.getPower(), data)) {
/*      */           
/*  923 */           if (player != null) {
/*  924 */             player.getCommunicator()
/*  925 */               .sendAlertServerMessage("An error was reported from the login server. Please try later or report this using /support if the problem persists.");
/*      */           }
/*  927 */           return false;
/*      */         } 
/*      */         
/*  930 */         return true;
/*      */       }
/*  932 */       catch (RemoteException rx) {
/*      */         
/*  934 */         logger.log(Level.WARNING, "Failed to transfer " + playerName + " to the login server " + rx.getMessage());
/*  935 */         if (player != null)
/*  936 */           player.getCommunicator().sendAlertServerMessage("An error occurred when contacting the login server. Please try later."); 
/*  937 */         return false;
/*      */       } 
/*      */     }
/*  940 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean changePassword(long wurmId, String newPassword) {
/*  945 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  949 */         connect();
/*      */       }
/*  951 */       catch (Exception ex) {
/*      */         
/*  953 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage() + " server=" + this.serverId);
/*      */         
/*  955 */         return false;
/*      */       } 
/*      */     }
/*  958 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  962 */         return this.wurm.changePassword(this.intraServerPassword, wurmId, newPassword);
/*      */       }
/*  964 */       catch (RemoteException rx) {
/*      */         
/*  966 */         logger.log(Level.WARNING, "Failed to change password for  " + wurmId + "." + rx.getMessage());
/*  967 */         return false;
/*      */       } 
/*      */     }
/*  970 */     return false;
/*      */   }
/*      */   
/*  973 */   static final int[] failedIntZero = new int[] { -1, -1 };
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getPremTimeSilvers(long wurmId) {
/*  978 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/*  982 */         connect();
/*      */       }
/*  984 */       catch (Exception ex) {
/*      */         
/*  986 */         return failedIntZero;
/*      */       } 
/*      */     }
/*  989 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/*  993 */         return this.wurm.getPremTimeSilvers(this.intraServerPassword, wurmId);
/*      */       }
/*  995 */       catch (RemoteException remoteException) {}
/*      */     }
/*      */ 
/*      */     
/*  999 */     return failedIntZero;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setCurrentServer(String name, int currentServer) {
/* 1004 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1008 */         connect();
/*      */       }
/* 1010 */       catch (Exception ex) {
/*      */         
/* 1012 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1013 */         return false;
/*      */       } 
/*      */     }
/* 1016 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1020 */         if (this.wurm.setCurrentServer(this.intraServerPassword, name, currentServer))
/*      */         {
/* 1022 */           return true;
/*      */         }
/*      */       }
/* 1025 */       catch (RemoteException rx) {
/*      */         
/* 1027 */         logger.log(Level.WARNING, "failed to set current server of " + name + " to " + currentServer + ", " + rx
/* 1028 */             .getMessage());
/* 1029 */         return false;
/*      */       } 
/*      */     }
/* 1032 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String renamePlayer(String oldName, String newName, String newPass, int power) {
/* 1037 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1041 */         connect();
/*      */       }
/* 1043 */       catch (Exception ex) {
/*      */         
/* 1045 */         logger.log(Level.WARNING, "Failed to contact the login server " + ex.getMessage() + "" + this.serverId);
/* 1046 */         return "Failed to contact server. Try later. This is an Error.";
/*      */       } 
/*      */     }
/* 1049 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1053 */         return this.wurm.rename(this.intraServerPassword, oldName, newName, newPass, power);
/*      */       }
/* 1055 */       catch (RemoteException rx) {
/*      */         
/* 1057 */         logger.log(Level.WARNING, "Failed to change name of " + oldName + ", " + rx.getMessage());
/* 1058 */         return "Failed to contact server. Try later. This is an Error.";
/*      */       } 
/*      */     }
/* 1061 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String changePassword(String changerName, String name, String newPass, int power) {
/* 1066 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1070 */         connect();
/*      */       }
/* 1072 */       catch (Exception ex) {
/*      */         
/* 1074 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1075 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1078 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1082 */         return this.wurm.changePassword(this.intraServerPassword, changerName, name, newPass, power);
/*      */       }
/* 1084 */       catch (RemoteException rx) {
/*      */         
/* 1086 */         logger.log(Level.WARNING, changerName + " failed to change password of " + name + ", " + rx.getMessage());
/* 1087 */         return rx.getMessage();
/*      */       } 
/*      */     }
/* 1090 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ascend(int newDeityId, String deityName, long wurmid, byte existingDeity, byte gender, byte newPower, float initialBStr, float initialBSta, float initialBCon, float initialML, float initialMS, float initialSS, float initialSD) {
/* 1097 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1101 */         connect();
/*      */       }
/* 1103 */       catch (Exception ex) {
/*      */         
/* 1105 */         logger.log(Level.WARNING, "Failed to contact the login server " + ex.getMessage() + " " + this.serverId);
/* 1106 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1109 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1113 */         return this.wurm.ascend(this.intraServerPassword, newDeityId, deityName, wurmid, existingDeity, gender, newPower, initialBStr, initialBSta, initialBCon, initialML, initialMS, initialSS, initialSD);
/*      */       
/*      */       }
/* 1116 */       catch (RemoteException rx) {
/*      */         
/* 1118 */         logger.log(Level.WARNING, wurmid + " failed to create deity " + deityName + ", " + rx.getMessage());
/* 1119 */         return rx.getMessage();
/*      */       } 
/*      */     }
/* 1122 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String changeEmail(String changerName, String name, String newEmail, String password, int power, String pwQuestion, String pwAnswer) {
/* 1128 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1132 */         connect();
/*      */       }
/* 1134 */       catch (Exception ex) {
/*      */         
/* 1136 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1137 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1140 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1144 */         return this.wurm.changeEmail(this.intraServerPassword, changerName, name, newEmail, password, power, pwQuestion, pwAnswer);
/*      */       }
/* 1146 */       catch (RemoteException rx) {
/*      */         
/* 1148 */         logger.log(Level.WARNING, changerName + " failed to change email of " + name + ", " + rx.getMessage());
/* 1149 */         return rx.getMessage();
/*      */       } 
/*      */     }
/* 1152 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String addReimb(String changerName, String name, int numMonths, int _silver, int _daysLeft, boolean setbok) {
/* 1158 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1162 */         connect();
/*      */       }
/* 1164 */       catch (Exception ex) {
/*      */         
/* 1166 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1167 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1170 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1174 */         return this.wurm.addReimb(this.intraServerPassword, changerName, name, numMonths, _silver, _daysLeft, setbok);
/*      */       }
/* 1176 */       catch (RemoteException rx) {
/*      */         
/* 1178 */         logger.log(Level.WARNING, changerName + " failed to add reimb of " + name + ", " + rx.getMessage());
/* 1179 */         return rx.getMessage();
/*      */       } 
/*      */     }
/* 1182 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String sendMail(byte[] maildata, byte[] items, long sender, long wurmid, int targetServer) {
/* 1188 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1192 */         connect();
/*      */       }
/* 1194 */       catch (Exception ex) {
/*      */         
/* 1196 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1197 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1200 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1204 */         return this.wurm.sendMail(this.intraServerPassword, maildata, items, sender, wurmid, targetServer);
/*      */       }
/* 1206 */       catch (RemoteException rx) {
/*      */         
/* 1208 */         logger.log(Level.WARNING, "Failed to send mail " + rx.getMessage());
/* 1209 */         return rx.getMessage();
/*      */       } 
/*      */     }
/* 1212 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String ban(String name, String reason, int days) {
/* 1217 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1221 */         connect();
/*      */       }
/* 1223 */       catch (Exception ex) {
/*      */         
/* 1225 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1226 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1229 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1233 */         return this.wurm.ban(this.intraServerPassword, name, reason, days);
/*      */       }
/* 1235 */       catch (RemoteException rx) {
/*      */         
/* 1237 */         logger.log(Level.WARNING, "Failed to ban " + name + ':' + rx.getMessage());
/* 1238 */         return "Failed to ban " + name + ':' + rx.getMessage();
/*      */       } 
/*      */     }
/* 1241 */     return "Failed to contact the login server. Please try later.";
/*      */   }
/*      */ 
/*      */   
/*      */   public String addBannedIp(String ip, String reason, int days) {
/* 1246 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1250 */         connect();
/*      */       }
/* 1252 */       catch (Exception ex) {
/*      */         
/* 1254 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1255 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1258 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1262 */         return this.wurm.addBannedIp(this.intraServerPassword, ip, reason, days);
/*      */       }
/* 1264 */       catch (RemoteException rx) {
/*      */         
/* 1266 */         logger.log(Level.WARNING, "Failed to ban " + ip + ':' + rx.getMessage());
/* 1267 */         return "Failed to ban " + ip + ':' + rx.getMessage();
/*      */       } 
/*      */     }
/* 1270 */     return "Failed to contact the login server. Please try later.";
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban[] getPlayersBanned() throws Exception {
/* 1275 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1279 */         connect();
/*      */       }
/* 1281 */       catch (Exception ex) {
/*      */         
/* 1283 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1284 */         throw new WurmServerException("Failed to contact the login server:" + ex.getMessage());
/*      */       } 
/*      */     }
/* 1287 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1291 */         return this.wurm.getPlayersBanned(this.intraServerPassword);
/*      */       }
/* 1293 */       catch (RemoteException rx) {
/*      */         
/* 1295 */         logger.log(Level.WARNING, "Failed to retrieve banned players :" + rx.getMessage());
/* 1296 */         throw new WurmServerException("Failed to retrieve banned players :" + rx.getMessage());
/*      */       } 
/*      */     }
/* 1299 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Ban[] getIpsBanned() throws Exception {
/* 1304 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1308 */         connect();
/*      */       }
/* 1310 */       catch (Exception ex) {
/*      */         
/* 1312 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1313 */         throw new WurmServerException("Failed to contact the login server:" + ex.getMessage());
/*      */       } 
/*      */     }
/* 1316 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1320 */         return this.wurm.getIpsBanned(this.intraServerPassword);
/*      */       }
/* 1322 */       catch (RemoteException rx) {
/*      */         
/* 1324 */         logger.log(Level.WARNING, "Failed to retrieve banned ips :" + rx.getMessage());
/* 1325 */         throw new WurmServerException("Failed to retrieve banned ips :" + rx.getMessage());
/*      */       } 
/*      */     }
/* 1328 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public String pardonban(String name) throws RemoteException {
/* 1333 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1337 */         connect();
/*      */       }
/* 1339 */       catch (Exception ex) {
/*      */         
/* 1341 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1342 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1345 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1349 */         return this.wurm.pardonban(this.intraServerPassword, name);
/*      */       }
/* 1351 */       catch (RemoteException rx) {
/*      */         
/* 1353 */         logger.log(Level.WARNING, "Failed to pardon " + name + ':' + rx.getMessage());
/* 1354 */         return "Failed to pardon " + name + ':' + rx.getMessage();
/*      */       } 
/*      */     }
/* 1357 */     return "Failed to contact the login server. Please try later.";
/*      */   }
/*      */ 
/*      */   
/*      */   public String removeBannedIp(String ip) {
/* 1362 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1366 */         connect();
/*      */       }
/* 1368 */       catch (Exception ex) {
/*      */         
/* 1370 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1371 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1374 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1378 */         return this.wurm.removeBannedIp(this.intraServerPassword, ip);
/*      */       }
/* 1380 */       catch (RemoteException rx) {
/*      */         
/* 1382 */         logger.log(Level.WARNING, "Failed to ban " + ip + ':' + rx.getMessage());
/* 1383 */         return "Failed to ban " + ip + ':' + rx.getMessage();
/*      */       } 
/*      */     }
/* 1386 */     return "Failed to contact the login server. Please try later.";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> doesPlayerExist(String playerName) {
/* 1450 */     Map<String, String> toReturn = new HashMap<>();
/* 1451 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1455 */         connect();
/*      */       }
/* 1457 */       catch (Exception ex) {
/*      */         
/* 1459 */         toReturn.put("ResponseCode", "NOTOK");
/* 1460 */         toReturn.put("ErrorMessage", "The game server is currently unavailable.");
/* 1461 */         toReturn.put("display_text", "The game server is currently unavailable.");
/* 1462 */         return toReturn;
/*      */       } 
/*      */     }
/* 1465 */     if (this.wurm != null) {
/*      */       
/*      */       try
/*      */       {
/* 1469 */         return this.wurm.doesPlayerExist(this.intraServerPassword, playerName);
/*      */       }
/* 1471 */       catch (RemoteException rx)
/*      */       {
/* 1473 */         logger.log(Level.WARNING, "Failed to contact server.");
/* 1474 */         toReturn.put("ResponseCode", "NOTOK");
/* 1475 */         toReturn.put("ErrorMessage", "The game server is currently unavailable.");
/* 1476 */         toReturn.put("display_text", "The game server is currently unavailable.");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1481 */       toReturn.put("ResponseCode", "NOTOK");
/* 1482 */       toReturn.put("ErrorMessage", "The game server is currently unavailable.");
/* 1483 */       toReturn.put("display_text", "The game server is currently unavailable.");
/*      */     } 
/* 1485 */     return toReturn;
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
/*      */   public String sendVehicle(byte[] passengerdata, byte[] itemdata, long pilot, long vehicleId, int targetServer, int tilex, int tiley, int layer, float rotation) {
/* 1509 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1513 */         connect();
/*      */       }
/* 1515 */       catch (Exception ex) {
/*      */         
/* 1517 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1518 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1521 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1525 */         return this.wurm.sendVehicle(this.intraServerPassword, passengerdata, itemdata, pilot, vehicleId, targetServer, tilex, tiley, layer, rotation);
/*      */       }
/* 1527 */       catch (RemoteException rx) {
/*      */         
/* 1529 */         logger.log(Level.WARNING, "Failed to send vehicle " + rx.getMessage());
/* 1530 */         return rx.getMessage();
/*      */       }
/* 1532 */       catch (Exception ex) {
/*      */         
/* 1534 */         return ex.getMessage();
/*      */       } 
/*      */     }
/* 1537 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendWebCommand(final short type, final WebCommand command) {
/* 1542 */     (new Thread()
/*      */       {
/*      */         
/*      */         public void run()
/*      */         {
/* 1547 */           boolean ok = false;
/* 1548 */           if (LoginServerWebConnection.this.wurm == null) {
/*      */             
/*      */             try {
/*      */               
/* 1552 */               LoginServerWebConnection.this.connect();
/*      */             }
/* 1554 */             catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1559 */           if (LoginServerWebConnection.this.wurm != null) {
/*      */             
/*      */             try {
/*      */               
/* 1563 */               LoginServerWebConnection.this.wurm.genericWebCommand(LoginServerWebConnection.this.intraServerPassword, type, command.getWurmId(), command.getData());
/* 1564 */               ok = true;
/*      */             }
/* 1566 */             catch (RemoteException rx) {
/*      */               
/* 1568 */               LoginServerWebConnection.logger.log(Level.WARNING, "Failed to send command " + rx.getMessage());
/*      */             } 
/*      */           }
/* 1571 */           if (!ok && command.getType() == 11)
/*      */           {
/* 1573 */             if (Servers.localServer.LOGINSERVER) {
/*      */               
/*      */               try {
/*      */ 
/*      */                 
/* 1578 */                 EpicEntity entity = Server.getEpicMap().getEntity(((WcCreateEpicMission)command).entityNumber);
/* 1579 */                 if (entity != null) {
/* 1580 */                   entity.addFailedServer(LoginServerWebConnection.this.serverId);
/*      */                 }
/* 1582 */               } catch (Exception ex) {
/*      */                 
/* 1584 */                 LoginServerWebConnection.logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */               } 
/*      */             }
/*      */           }
/*      */         }
/* 1589 */       }).start();
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
/*      */   public void setKingdomInfo(byte kingdomId, byte templateKingdom, String _name, String _password, String _chatName, String _suffix, String mottoOne, String mottoTwo, boolean acceptsPortals) {
/* 1612 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1616 */         connect();
/*      */       }
/* 1618 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1623 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1627 */         this.wurm.setKingdomInfo(this.intraServerPassword, Servers.localServer.id, kingdomId, templateKingdom, _name, _password, _chatName, _suffix, mottoOne, mottoTwo, acceptsPortals);
/*      */       
/*      */       }
/* 1630 */       catch (RemoteException rx) {
/*      */         
/* 1632 */         logger.log(Level.WARNING, "Failed to send command " + rx.getMessage());
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
/*      */   public boolean kingdomExists(int thisServerId, byte kingdomId, boolean exists) {
/* 1650 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1654 */         connect();
/*      */       }
/* 1656 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1661 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1665 */         return this.wurm.kingdomExists(this.intraServerPassword, thisServerId, kingdomId, exists);
/*      */       }
/* 1667 */       catch (RemoteException rx) {
/*      */         
/* 1669 */         logger.log(Level.WARNING, "Failed to send command " + rx.getMessage());
/*      */       } 
/*      */     }
/* 1672 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void requestDemigod(byte existingDeity, String deityName) {
/* 1677 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1681 */         connect();
/*      */       }
/* 1683 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1688 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1692 */         this.wurm.requestDemigod(this.intraServerPassword, existingDeity, deityName);
/*      */       }
/* 1694 */       catch (RemoteException rx) {
/*      */         
/* 1696 */         logger.log(Level.WARNING, "Failed to send command " + rx.getMessage());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean requestDeityMove(int deityNum, int desiredHex, String guide) {
/* 1703 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1707 */         connect();
/*      */       }
/* 1709 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1714 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1718 */         return this.wurm.requestDeityMove(this.intraServerPassword, deityNum, desiredHex, guide);
/*      */       }
/* 1720 */       catch (RemoteException rx) {
/*      */         
/* 1722 */         logger.log(Level.WARNING, "Failed to send command " + rx.getMessage());
/*      */       } 
/*      */     }
/* 1725 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean awardPlayer(long wurmid, String name, int days, int months) {
/* 1730 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1734 */         connect();
/*      */       }
/* 1736 */       catch (Exception ex) {
/*      */         
/* 1738 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/* 1739 */         return false;
/*      */       } 
/*      */     }
/* 1742 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1746 */         this.wurm.awardPlayer(this.intraServerPassword, wurmid, name, days, months);
/*      */       }
/* 1748 */       catch (RemoteException rx) {
/*      */         
/* 1750 */         logger.log(Level.WARNING, "failed to set award " + wurmid + " (" + name + ") " + months + " months, " + days + " days, " + rx
/* 1751 */             .getMessage());
/* 1752 */         return false;
/*      */       } 
/*      */     }
/* 1755 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFeatureEnabled(int featureId) {
/* 1760 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1764 */         connect();
/*      */       }
/* 1766 */       catch (Exception ex) {
/*      */         
/* 1768 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */       } 
/*      */     }
/* 1771 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1775 */         return this.wurm.isFeatureEnabled(this.intraServerPassword, featureId);
/*      */       }
/* 1777 */       catch (RemoteException rx) {
/*      */         
/* 1779 */         logger.log(Level.WARNING, "An error occurred when contacting the login server. Please try later. " + this.serverId + " " + rx
/* 1780 */             .getMessage());
/*      */       } 
/*      */     }
/* 1783 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setPlayerFlag(long wurmid, int flag, boolean set) {
/* 1788 */     if (this.wurm == null) {
/*      */       
/*      */       try {
/*      */         
/* 1792 */         connect();
/*      */       }
/* 1794 */       catch (Exception ex) {
/*      */         
/* 1796 */         logger.log(Level.WARNING, "Failed to contact the login server  " + this.serverId + " " + ex.getMessage());
/*      */       } 
/*      */     }
/* 1799 */     if (this.wurm != null) {
/*      */       
/*      */       try {
/*      */         
/* 1803 */         return this.wurm.setPlayerFlag(Servers.localServer.INTRASERVERPASSWORD, wurmid, flag, set);
/*      */       }
/* 1805 */       catch (RemoteException rx) {
/*      */         
/* 1807 */         logger.log(Level.WARNING, "An error occurred when contacting the login server. Please try later. " + this.serverId + " " + rx.getMessage());
/*      */       } 
/*      */     }
/* 1810 */     return false;
/*      */   }
/*      */   
/*      */   public LoginServerWebConnection() {}
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\LoginServerWebConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */