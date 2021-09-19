/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.players.Ban;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Properties;
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
/*      */ public class GmInterface
/*      */   extends Question
/*      */   implements TimeConstants
/*      */ {
/*   43 */   private static final Logger logger = Logger.getLogger(GmInterface.class.getName());
/*   44 */   private final LinkedList<Player> playlist = new LinkedList<>();
/*   45 */   private final LinkedList<String> iplist = new LinkedList<>();
/*      */   
/*      */   private static final String muteReasonOne = "Profane language";
/*      */   
/*      */   private static final String muteReasonTwo = "Racial or sexist remarks";
/*      */   
/*      */   private static final String muteReasonThree = "Staff bashing";
/*      */   
/*      */   private static final String muteReasonFour = "Harassment";
/*      */   
/*      */   private static final String muteReasonFive = "Spam";
/*      */   
/*      */   private static final String muteReasonSix = "Insubordination";
/*      */   
/*      */   private static final String muteReasonSeven = "Repeated warnings";
/*      */   
/*      */   private PlayerInfo playerInfo;
/*      */   
/*      */   private Player targetPlayer;
/*      */   private boolean doneSomething = false;
/*      */   
/*      */   public GmInterface(Creature aResponder, long aTarget) {
/*   67 */     super(aResponder, "Player Management", "How may we help you?", 83, aTarget);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties aAnswer) {
/*   78 */     setAnswer(aAnswer);
/*   79 */     if (this.type == 0) {
/*      */       
/*   81 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*      */       return;
/*      */     } 
/*   84 */     if (this.type == 83)
/*      */     {
/*   86 */       if (getResponder().getPower() >= 2 || getResponder().mayMute()) {
/*      */         
/*   88 */         this.doneSomething = false;
/*      */         
/*   90 */         String pname = aAnswer.getProperty("pname");
/*   91 */         if (pname == null || pname.length() == 0) {
/*      */           
/*   93 */           String pid = aAnswer.getProperty("ddname");
/*   94 */           int num = Integer.parseInt(pid);
/*   95 */           if (num > 0) {
/*      */             
/*   97 */             this.targetPlayer = this.playlist.get(num - 1);
/*   98 */             pname = this.targetPlayer.getName();
/*   99 */             if (!this.targetPlayer.hasLink())
/*  100 */               this.targetPlayer = null; 
/*      */           } 
/*      */         } 
/*  103 */         boolean nameSpecified = (pname != null && pname.length() > 0);
/*      */         
/*  105 */         if (nameSpecified) {
/*      */           
/*  107 */           this.playerInfo = PlayerInfoFactory.createPlayerInfo(pname);
/*      */           
/*      */           try {
/*  110 */             this.playerInfo.load();
/*      */           }
/*  112 */           catch (IOException iox) {
/*      */             
/*  114 */             getResponder().getCommunicator().sendNormalServerMessage("Failed to load data for the player with name " + pname + ".");
/*      */             
/*      */             return;
/*      */           } 
/*  118 */           if (this.playerInfo == null || this.playerInfo.wurmId <= 0L) {
/*      */ 
/*      */             
/*  121 */             long[] info = { Servers.localServer.id, -1L };
/*      */             
/*  123 */             LoginServerWebConnection lsw = new LoginServerWebConnection();
/*      */             
/*      */             try {
/*  126 */               info = lsw.getCurrentServer(pname, -1L);
/*      */             }
/*  128 */             catch (Exception e) {
/*      */               
/*  130 */               info = new long[] { -1L, -1L };
/*      */             } 
/*      */             
/*  133 */             if (info[0] == -1L) {
/*  134 */               getResponder().getCommunicator().sendNormalServerMessage("Player with name " + pname + " not found anywhere!.");
/*      */             } else {
/*      */               
/*  137 */               getResponder().getCommunicator().sendNormalServerMessage("Player with name " + pname + " has never been on this server, but is currently on server " + info[0] + ", their WurmId is " + info[1] + ".");
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  142 */           if (getResponder().mayMute() || getResponder().getPower() >= 2)
/*      */           {
/*  144 */             checkMute(pname);
/*      */           }
/*  146 */           if (getResponder().getPower() >= 2) {
/*      */             
/*  148 */             checkBans(pname);
/*      */             
/*  150 */             String key = "summon";
/*  151 */             String val = aAnswer.getProperty(key);
/*  152 */             if (val != null) {
/*      */               
/*  154 */               boolean summon = val.equals("true");
/*  155 */               if (summon)
/*      */               {
/*  157 */                 if (pname.equalsIgnoreCase(getResponder().getName())) {
/*  158 */                   getResponder().getCommunicator().sendNormalServerMessage("You cannot summon yourself.");
/*      */                 } else {
/*      */                   
/*  161 */                   this.doneSomething = true;
/*  162 */                   int tilex = getResponder().getTileX();
/*  163 */                   int tiley = getResponder().getTileY();
/*  164 */                   byte layer = (byte)getResponder().getLayer();
/*  165 */                   key = "tilex";
/*  166 */                   val = aAnswer.getProperty(key);
/*  167 */                   if (val != null && val.length() > 0)
/*      */                   {
/*  169 */                     tilex = Integer.parseInt(val);
/*      */                   }
/*  171 */                   key = "tiley";
/*  172 */                   val = aAnswer.getProperty(key);
/*  173 */                   if (val != null && val.length() > 0)
/*      */                   {
/*  175 */                     tiley = Integer.parseInt(val);
/*      */                   }
/*  177 */                   key = "surfaced";
/*  178 */                   val = aAnswer.getProperty(key);
/*  179 */                   if (val != null && val.length() > 0)
/*      */                   {
/*  181 */                     layer = (byte)(val.equals("true") ? 0 : -1);
/*      */                   }
/*  183 */                   QuestionParser.summon(pname, getResponder(), tilex, tiley, layer);
/*      */                 } 
/*      */               }
/*      */             } 
/*      */             
/*  188 */             key = "locate";
/*  189 */             val = aAnswer.getProperty(key);
/*  190 */             if (val != null) {
/*      */               
/*  192 */               boolean locate = val.equals("true");
/*  193 */               if (locate) {
/*      */                 
/*  195 */                 logger.log(Level.INFO, getResponder().getName() + " locating " + pname);
/*  196 */                 this.doneSomething = true;
/*  197 */                 if (!LocatePlayerQuestion.locateCorpse(pname, getResponder(), 100.0D, true))
/*      */                 {
/*  199 */                   getResponder().getCommunicator().sendNormalServerMessage("No such soul found.");
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  206 */         if (!this.doneSomething && getResponder().getPower() >= 2) {
/*      */ 
/*      */ 
/*      */           
/*  210 */           String key = "gmtool";
/*  211 */           String val = aAnswer.getProperty("gmtool");
/*  212 */           if (val != null) {
/*      */             
/*  214 */             boolean gmtool = val.equals("true");
/*      */             
/*  216 */             if (gmtool) {
/*      */               
/*  218 */               String strWurmId = aAnswer.getProperty("wurmid");
/*  219 */               String searchemail = aAnswer.getProperty("searchemail");
/*  220 */               String searchip = aAnswer.getProperty("searchip");
/*      */               
/*  222 */               boolean wurmIdSpecified = (strWurmId != null && strWurmId.length() > 0);
/*  223 */               boolean searchemailSpecified = (searchemail != null && searchemail.length() > 0);
/*  224 */               boolean searchipSpecified = (searchip != null && searchip.length() > 0);
/*      */               
/*  226 */               int optCount = 0;
/*  227 */               if (nameSpecified)
/*  228 */                 optCount++; 
/*  229 */               if (wurmIdSpecified)
/*  230 */                 optCount++; 
/*  231 */               if (searchemailSpecified)
/*  232 */                 optCount++; 
/*  233 */               if (searchipSpecified) {
/*  234 */                 optCount++;
/*      */               }
/*  236 */               if (optCount != 1) {
/*      */                 
/*  238 */                 getResponder().getCommunicator().sendNormalServerMessage("Name or Number or Email search or IP search must be specified");
/*      */               
/*      */               }
/*      */               else {
/*      */                 
/*  243 */                 long toolWurmId = -1L;
/*  244 */                 byte toolType = 1;
/*  245 */                 byte toolSubType = 1;
/*  246 */                 String toolSearch = "";
/*  247 */                 if (getResponder().getLogger() != null && this.playerInfo != null)
/*  248 */                   getResponder().getLogger().log(Level.INFO, "GM Tool for " + this.playerInfo
/*  249 */                       .getName() + ", " + this.playerInfo.wurmId); 
/*  250 */                 if (nameSpecified) {
/*      */                   
/*  252 */                   toolWurmId = this.playerInfo.wurmId;
/*      */                 }
/*  254 */                 else if (wurmIdSpecified) {
/*      */ 
/*      */                   
/*      */                   try {
/*  258 */                     toolWurmId = Long.parseLong(strWurmId);
/*      */                   }
/*  260 */                   catch (Exception e) {
/*      */                     
/*  262 */                     getResponder().getCommunicator().sendNormalServerMessage("Wurm ID is not a number!");
/*      */                     
/*      */                     return;
/*      */                   } 
/*  266 */                 } else if (searchemailSpecified) {
/*      */                   
/*  268 */                   toolType = 2;
/*  269 */                   toolSubType = 1;
/*  270 */                   toolSearch = searchemail;
/*      */                 }
/*  272 */                 else if (searchipSpecified) {
/*      */                   
/*  274 */                   toolType = 2;
/*  275 */                   toolSubType = 2;
/*  276 */                   toolSearch = searchip;
/*      */                 } 
/*      */                 
/*  279 */                 this.doneSomething = true;
/*      */                 
/*  281 */                 GmTool gt = new GmTool(getResponder(), toolType, toolSubType, toolWurmId, toolSearch, "", 50, (byte)0);
/*      */                 
/*  283 */                 gt.sendQuestion();
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  288 */           if (!this.doneSomething)
/*      */           {
/*  290 */             if (!nameSpecified) {
/*      */               
/*  292 */               getResponder().getCommunicator().sendNormalServerMessage("No player name provided. Doing nothing!");
/*      */             }
/*      */             else {
/*      */               
/*  296 */               getResponder().getCommunicator().sendNormalServerMessage("Nothing selected. Doing nothing!");
/*      */             } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkMute(String pname) {
/*  314 */     String muteReason = getAnswer().getProperty("mutereason");
/*  315 */     String mutereasontb = getAnswer().getProperty("mutereasontb");
/*  316 */     if (mutereasontb != null && mutereasontb.length() > 2) {
/*  317 */       muteReason = mutereasontb;
/*  318 */     } else if (muteReason != null && muteReason.length() > 0) {
/*      */ 
/*      */       
/*      */       try {
/*  322 */         int reason = Integer.parseInt(muteReason);
/*      */         
/*  324 */         switch (reason) {
/*      */           
/*      */           case 0:
/*  327 */             muteReason = "Profane language";
/*      */             break;
/*      */           case 1:
/*  330 */             muteReason = "Racial or sexist remarks";
/*      */             break;
/*      */           case 2:
/*  333 */             muteReason = "Staff bashing";
/*      */             break;
/*      */           case 3:
/*  336 */             muteReason = "Harassment";
/*      */             break;
/*      */           case 4:
/*  339 */             muteReason = "Spam";
/*      */             break;
/*      */           case 5:
/*  342 */             muteReason = "Insubordination";
/*      */             break;
/*      */           case 6:
/*  345 */             muteReason = "Repeated warnings";
/*      */             break;
/*      */           default:
/*  348 */             logger.warning("Unexpected parsed value: " + reason + " from mute reason: " + muteReason + ". Responder: " + 
/*  349 */                 getResponder());
/*      */             break;
/*      */         } 
/*  352 */       } catch (NumberFormatException nfe) {
/*      */         
/*  354 */         logger.log(Level.WARNING, "Problem parsing the mute reason: " + muteReason + ". Responder: " + getResponder() + " due to " + nfe, nfe);
/*      */       } 
/*      */     } 
/*      */     
/*  358 */     String key = "mutewarn";
/*  359 */     String val = getAnswer().getProperty(key);
/*  360 */     if (val != null && val.equals("true"))
/*      */     {
/*  362 */       if (this.targetPlayer != null)
/*      */       {
/*  364 */         if (this.targetPlayer.getPower() <= getResponder().getPower()) {
/*      */           
/*  366 */           logMgmt("mutewarns " + pname + " (" + muteReason + ")");
/*  367 */           this.targetPlayer
/*  368 */             .getCommunicator()
/*  369 */             .sendAlertServerMessage(
/*  370 */               getResponder().getName() + " issues a warning that you may be muted. Be silent for a while and try to understand why or change the subject of your conversation please.");
/*      */           
/*  372 */           if (muteReason.length() > 0) {
/*  373 */             this.targetPlayer.getCommunicator().sendAlertServerMessage("The reason for this is '" + muteReason + "'");
/*      */           }
/*  375 */           getResponder().getCommunicator().sendSafeServerMessage("You warn " + this.targetPlayer
/*  376 */               .getName() + " that " + this.targetPlayer.getHeSheItString() + " may be muted.");
/*      */           
/*  378 */           getResponder().getCommunicator().sendSafeServerMessage("The reason you gave was '" + muteReason + "'.");
/*  379 */           this.doneSomething = true;
/*      */         }
/*      */         else {
/*      */           
/*  383 */           getResponder().getCommunicator().sendNormalServerMessage("You threaten " + pname + " with muting!");
/*  384 */           this.targetPlayer.getCommunicator().sendNormalServerMessage(
/*  385 */               getResponder().getName() + " tried to threaten you with muting!");
/*  386 */           if (muteReason.length() > 0) {
/*  387 */             this.targetPlayer.getCommunicator().sendNormalServerMessage("The formal reason for this is '" + muteReason + "'");
/*      */           }
/*  389 */           this.doneSomething = true;
/*      */         } 
/*      */       }
/*      */     }
/*  393 */     key = "unmute";
/*  394 */     val = getAnswer().getProperty(key);
/*  395 */     if (val != null && val.equals("true")) {
/*      */       
/*  397 */       if (this.playerInfo != null) {
/*      */         
/*  399 */         this.playerInfo.setMuted(false, "", 0L);
/*  400 */         logMgmt("unmutes " + pname);
/*  401 */         this.doneSomething = true;
/*  402 */         getResponder().getCommunicator().sendNormalServerMessage("You have given " + pname + " the voice back.");
/*      */       } 
/*      */       
/*  405 */       if (this.targetPlayer != null) {
/*  406 */         this.targetPlayer.getCommunicator().sendAlertServerMessage("You have been given your voice back and can shout again.");
/*      */       }
/*      */     } 
/*  409 */     int hours = 1;
/*  410 */     key = "mute";
/*  411 */     val = getAnswer().getProperty(key);
/*  412 */     long expiry = 0L;
/*  413 */     if (val != null && val.equals("true")) {
/*      */       
/*  415 */       logger.log(Level.INFO, "Muting");
/*  416 */       this.doneSomething = true;
/*  417 */       String muteTime = getAnswer().getProperty("mutetime");
/*  418 */       if (muteTime != null && muteTime.length() > 0) {
/*      */         
/*      */         try {
/*      */           
/*  422 */           int index = Integer.parseInt(muteTime);
/*  423 */           switch (index) {
/*      */             
/*      */             case 0:
/*  426 */               hours = 1;
/*      */               break;
/*      */             case 1:
/*  429 */               hours = 2;
/*      */               break;
/*      */             case 2:
/*  432 */               hours = 5;
/*      */               break;
/*      */             case 3:
/*  435 */               hours = 8;
/*      */               break;
/*      */             case 4:
/*  438 */               hours = 24;
/*      */               break;
/*      */             case 5:
/*  441 */               hours = 48;
/*      */               break;
/*      */             default:
/*  444 */               logger.warning("Unexpected muteTime value: " + muteTime + ". Responder: " + getResponder()); break;
/*      */           } 
/*  446 */           expiry = System.currentTimeMillis() + hours * 3600000L;
/*      */         }
/*  448 */         catch (NumberFormatException nfe) {
/*      */           
/*  450 */           getResponder().getCommunicator().sendNormalServerMessage("An error occurred with the number of hours for the mute: " + muteTime + ".");
/*      */           
/*      */           return;
/*      */         } 
/*      */       }
/*  455 */       if (expiry > System.currentTimeMillis()) {
/*      */         
/*  457 */         logger.log(Level.INFO, "Muting");
/*  458 */         if (this.playerInfo != null)
/*      */         {
/*  460 */           if (this.playerInfo.getPower() < getResponder().getPower() || this.playerInfo.getPower() == 0) {
/*      */             
/*  462 */             this.playerInfo.setMuted(true, muteReason, expiry);
/*      */             
/*  464 */             getResponder().getCommunicator().sendNormalServerMessage("You have muted " + this.playerInfo
/*  465 */                 .getName() + " for " + hours + " hours.");
/*      */             
/*  467 */             logMgmt("muted " + pname + " for " + hours + " hours. Reason: " + muteReason);
/*  468 */             if (this.targetPlayer != null) {
/*      */               
/*  470 */               this.targetPlayer.getCommunicator().sendAlertServerMessage("You have been muted by " + 
/*  471 */                   getResponder().getName() + " for " + hours + " hours and cannot shout anymore. Reason: " + muteReason);
/*      */             }
/*      */             else {
/*      */               
/*  475 */               getResponder().getCommunicator().sendNormalServerMessage(this.playerInfo.getName() + " is offline.");
/*      */             } 
/*      */           } else {
/*  478 */             getResponder().getCommunicator().sendNormalServerMessage("You are too weak to mute " + pname + '!');
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkBans(String pname) {
/*  487 */     String banTime = getAnswer().getProperty("bantime");
/*  488 */     long expiry = 0L;
/*  489 */     int days = 0;
/*  490 */     String banReason = getAnswer().getProperty("banreason");
/*  491 */     if (banReason == null || banReason.length() < 2)
/*      */     {
/*  493 */       banReason = "Banned";
/*      */     }
/*  495 */     String key = "pardon";
/*  496 */     String val = getAnswer().getProperty(key);
/*  497 */     if (val != null && val.equals("true")) {
/*      */       
/*  499 */       this.doneSomething = true;
/*  500 */       if (this.playerInfo != null)
/*      */       {
/*  502 */         if (this.playerInfo.isBanned()) {
/*      */           
/*      */           try {
/*      */             
/*  506 */             this.playerInfo.setBanned(false, "", 0L);
/*  507 */             String bip = this.playerInfo.getIpaddress();
/*  508 */             Players.getInstance().removeBan(bip);
/*  509 */             getResponder().getCommunicator().sendSafeServerMessage("You have gratiously pardoned " + pname + " and the ipaddress " + bip);
/*      */ 
/*      */             
/*  512 */             log("pardons player " + pname + " and ipaddress " + bip + '.');
/*      */           }
/*  514 */           catch (IOException iOException) {}
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  521 */     if (banTime != null && banTime.length() > 0) {
/*      */       
/*      */       try {
/*      */         
/*  525 */         int index = Integer.parseInt(banTime);
/*  526 */         switch (index) {
/*      */           
/*      */           case 0:
/*  529 */             days = 1;
/*      */             break;
/*      */           case 1:
/*  532 */             days = 3;
/*      */             break;
/*      */           case 2:
/*  535 */             days = 7;
/*      */             break;
/*      */           case 3:
/*  538 */             days = 30;
/*      */             break;
/*      */           case 4:
/*  541 */             days = 90;
/*      */             break;
/*      */           case 5:
/*  544 */             days = 365;
/*      */             break;
/*      */           case 6:
/*  547 */             days = 9999;
/*      */             break;
/*      */           default:
/*  550 */             logger.warning("Unexpected banTime value: " + banTime + ". Responder: " + getResponder()); break;
/*      */         } 
/*  552 */         expiry = System.currentTimeMillis() + days * 86400000L;
/*      */       }
/*  554 */       catch (NumberFormatException nfe) {
/*      */         
/*  556 */         getResponder().getCommunicator().sendNormalServerMessage("An error occurred with the number of days for the ban: " + banTime + ".");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*  561 */     boolean bannedip = false;
/*  562 */     key = "ban";
/*  563 */     val = getAnswer().getProperty(key);
/*  564 */     if (val != null && val.equals("true"))
/*      */     {
/*  566 */       if (expiry > System.currentTimeMillis()) {
/*      */         
/*  568 */         this.doneSomething = true;
/*      */         
/*      */         try {
/*  571 */           if (this.targetPlayer != null && this.targetPlayer.hasLink()) {
/*      */             
/*  573 */             this.targetPlayer.getCommunicator().sendAlertServerMessage("You have been banned for " + days + " days and thrown out from the game. The reason is " + banReason, (byte)1);
/*      */ 
/*      */             
/*  576 */             this.targetPlayer.setFrozen(true);
/*  577 */             this.targetPlayer.logoutIn(10, "banned");
/*  578 */             getResponder().getCommunicator().sendSafeServerMessage(
/*  579 */                 String.format("Player %s was successfully found and will be removed from the world in 10 seconds.", new Object[] {
/*  580 */                     this.targetPlayer.getName()
/*      */                   }));
/*      */           } else {
/*      */             
/*  584 */             getResponder().getCommunicator().sendSafeServerMessage(
/*  585 */                 String.format("Something went wrong and %s was not removed from the world. You may need to kick them.", new Object[] {
/*  586 */                     this.playerInfo.getName() }));
/*      */           } 
/*  588 */           this.playerInfo.setBanned(true, banReason, expiry);
/*  589 */           key = "banip";
/*  590 */           val = getAnswer().getProperty(key);
/*  591 */           if (val != null && val.equals("true")) {
/*      */             
/*  593 */             bannedip = true;
/*  594 */             String bip = this.playerInfo.getIpaddress();
/*  595 */             getResponder().getCommunicator()
/*  596 */               .sendSafeServerMessage("You ban and kick " + pname + ". The server won't accept connections from " + bip + " anymore.", (byte)1);
/*      */ 
/*      */             
/*  599 */             log("bans player " + pname + " for " + days + " days and ipaddress " + bip + " for " + 
/*  600 */                 Math.min(days, 7) + " days.");
/*  601 */             if (Servers.localServer.LOGINSERVER) {
/*  602 */               Players.getInstance().addBannedIp(bip, "[" + pname + "] " + banReason, 
/*  603 */                   Math.min(expiry, System.currentTimeMillis() + 604800000L));
/*      */             } else {
/*      */               
/*      */               try
/*      */               {
/*  608 */                 LoginServerWebConnection c = new LoginServerWebConnection();
/*  609 */                 getResponder().getCommunicator().sendSafeServerMessage(c
/*  610 */                     .addBannedIp(bip, "[" + pname + "] " + banReason, Math.min(days, 7)));
/*      */               }
/*  612 */               catch (Exception e)
/*      */               {
/*  614 */                 getResponder().getCommunicator().sendAlertServerMessage("Failed to ban on login server:" + e
/*  615 */                     .getMessage());
/*  616 */                 logger.log(Level.INFO, 
/*  617 */                     getResponder().getName() + " banning ip on login server failed: " + e.getMessage(), e);
/*      */               }
/*      */             
/*      */             } 
/*      */           } else {
/*      */             
/*  623 */             getResponder().getCommunicator().sendSafeServerMessage("You ban and kick " + pname + ". No IP ban was issued.");
/*      */             
/*  625 */             log("bans player " + pname + " for " + days + " days. No IP ban was issued.");
/*      */           } 
/*  627 */           if (!Servers.localServer.LOGINSERVER) {
/*      */             try
/*      */             {
/*      */               
/*  631 */               LoginServerWebConnection c = new LoginServerWebConnection();
/*  632 */               getResponder().getCommunicator().sendSafeServerMessage(c.ban(pname, banReason, days));
/*      */             }
/*  634 */             catch (Exception e)
/*      */             {
/*  636 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to ban on login server:" + e
/*  637 */                   .getMessage());
/*  638 */               logger.log(Level.INFO, getResponder().getName() + " banning " + pname + " on login server failed: " + e
/*  639 */                   .getMessage(), e);
/*      */             }
/*      */           
/*      */           }
/*  643 */         } catch (IOException e) {
/*      */           
/*  645 */           getResponder().getCommunicator().sendAlertServerMessage("Failed to ban on local server:" + e.getMessage());
/*  646 */           logger.log(Level.INFO, 
/*  647 */               getResponder().getName() + " banning " + pname + " on local server failed: " + e.getMessage(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  652 */     key = "banip";
/*  653 */     val = getAnswer().getProperty(key);
/*  654 */     if (!bannedip && val != null && val.equals("true"))
/*      */     {
/*  656 */       if (expiry > System.currentTimeMillis()) {
/*      */         
/*  658 */         this.doneSomething = true;
/*  659 */         boolean ok = true;
/*  660 */         String ipToBan = getAnswer().getProperty("iptoban");
/*  661 */         if (ipToBan == null || ipToBan.length() < 5)
/*      */         {
/*      */ 
/*      */           
/*  665 */           ok = false;
/*      */         }
/*  667 */         if (ok) {
/*      */           
/*      */           try {
/*      */             
/*  671 */             if (ipToBan.charAt(0) != '/') {
/*  672 */               ipToBan = '/' + ipToBan;
/*      */             
/*      */             }
/*      */           }
/*  676 */           catch (Exception ex) {
/*      */             
/*  678 */             ok = false;
/*      */           } 
/*      */         }
/*  681 */         if (ok) {
/*      */           
/*  683 */           int dots = ipToBan.indexOf('*');
/*  684 */           if (dots > 0 && dots < 5) {
/*      */             
/*  686 */             getResponder().getCommunicator().sendAlertServerMessage("Failed to ban the ip. The ip address must be at least 5 characters long.");
/*      */             
/*      */             return;
/*      */           } 
/*  690 */           Player[] players = Players.getInstance().getPlayers();
/*  691 */           for (int x = 0; x < players.length; x++) {
/*      */             
/*  693 */             if (players[x].hasLink()) {
/*      */               
/*  695 */               boolean ban = players[x].getCommunicator().getConnection().getIp().equals(ipToBan);
/*  696 */               if (!ban && dots > 0)
/*      */               {
/*  698 */                 ban = players[x].getCommunicator().getConnection().getIp().startsWith(ipToBan.substring(0, dots)); } 
/*  699 */               if (ban)
/*      */               {
/*  701 */                 if (players[x].getPower() < getResponder().getPower()) {
/*      */                   
/*  703 */                   Players.getInstance().logoutPlayer(players[x]);
/*      */                 }
/*      */                 else {
/*      */                   
/*  707 */                   ok = false;
/*  708 */                   getResponder().getCommunicator().sendNormalServerMessage("You cannot kick " + players[x]
/*  709 */                       .getName() + '!');
/*  710 */                   players[x].getCommunicator().sendAlertServerMessage(
/*  711 */                       getResponder().getName() + " tried to kick you from the game and ban your ip.");
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*  716 */           if (Servers.localServer.LOGINSERVER) {
/*  717 */             Players.getInstance().addBannedIp(ipToBan, banReason, expiry);
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
/*  722 */               LoginServerWebConnection c = new LoginServerWebConnection();
/*  723 */               getResponder().getCommunicator().sendSafeServerMessage(c.addBannedIp(ipToBan, banReason, days));
/*      */             }
/*  725 */             catch (Exception e) {
/*      */               
/*  727 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to ban on login server:" + e
/*  728 */                   .getMessage());
/*  729 */               logger.log(Level.INFO, 
/*  730 */                   getResponder().getName() + " banning ip on login server failed: " + e.getMessage(), e);
/*      */             } 
/*      */           } 
/*  733 */           getResponder().getCommunicator().sendSafeServerMessage("You ban " + ipToBan + " for " + days + " days. The server won't accept connections from " + ipToBan + " anymore.");
/*      */ 
/*      */           
/*  736 */           log("bans ipaddress " + ipToBan + " for " + days + " days. Reason " + banReason);
/*      */         } 
/*      */       } 
/*      */     }
/*  740 */     key = "warn";
/*  741 */     val = getAnswer().getProperty(key);
/*  742 */     if (val != null && val.equals("true")) {
/*      */       
/*  744 */       this.doneSomething = true;
/*  745 */       long lastWarned = this.playerInfo.getLastWarned();
/*      */       
/*      */       try {
/*  748 */         this.playerInfo.warn();
/*      */       }
/*  750 */       catch (IOException iox) {
/*      */         
/*  752 */         logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */       } 
/*  754 */       String wst = this.playerInfo.getWarningStats(lastWarned);
/*  755 */       getResponder().getCommunicator().sendSafeServerMessage("You have officially warned " + pname + ". " + wst, (byte)1);
/*  756 */       if (this.targetPlayer != null) {
/*  757 */         this.targetPlayer.getCommunicator().sendAlertServerMessage("You have just received an official warning. Too many of these will get you banned from the game.", (byte)1);
/*      */       }
/*  759 */       log("issues an official warning to " + pname + '.');
/*      */     } 
/*  761 */     key = "resetWarn";
/*  762 */     val = getAnswer().getProperty(key);
/*  763 */     if (val != null && val.equals("true")) {
/*      */       
/*  765 */       this.doneSomething = true;
/*      */       
/*      */       try {
/*  768 */         this.playerInfo.resetWarnings();
/*      */         
/*  770 */         getResponder().getCommunicator().sendSafeServerMessage("You have officially removed the warnings for " + pname + '.');
/*      */         
/*  772 */         if (this.targetPlayer != null) {
/*  773 */           this.targetPlayer.getCommunicator().sendSafeServerMessage("Your warnings have just been officially removed.", (byte)1);
/*      */         }
/*  775 */         log("removes warnings for " + pname + '.');
/*      */       }
/*  777 */       catch (IOException iox) {
/*      */         
/*  779 */         logger.log(Level.INFO, getResponder().getName() + " fails to reset warnings for " + pname + '.', iox);
/*      */       } 
/*      */     } 
/*  782 */     key = "pardonip";
/*  783 */     val = getAnswer().getProperty(key);
/*  784 */     if (val != null && val.length() > 0) {
/*      */       
/*      */       try {
/*      */         
/*  788 */         int num = Integer.parseInt(val);
/*  789 */         if (num > 0) {
/*      */           
/*  791 */           this.doneSomething = true;
/*      */           
/*  793 */           String ip = this.iplist.get(num - 1);
/*  794 */           Ban bip = Players.getInstance().getBannedIp(ip);
/*  795 */           if (bip != null)
/*      */           {
/*  797 */             if (Players.getInstance().removeBan(ip)) {
/*      */               
/*  799 */               getResponder().getCommunicator().sendSafeServerMessage("You have gratiously pardoned the ipaddress " + ip, (byte)1);
/*      */               
/*  801 */               log("pardons ipaddress " + ip + '.');
/*      */               
/*      */               try {
/*  804 */                 LoginServerWebConnection c = new LoginServerWebConnection();
/*  805 */                 getResponder().getCommunicator().sendSafeServerMessage(c.removeBannedIp(ip));
/*      */               }
/*  807 */               catch (Exception e) {
/*      */                 
/*  809 */                 getResponder().getCommunicator().sendAlertServerMessage("Failed to remove ip ban on login server:" + e
/*  810 */                     .getMessage());
/*  811 */                 logger.log(Level.INFO, getResponder().getName() + " removing ip ban " + bip + " on login server failed: " + e
/*  812 */                     .getMessage(), e);
/*      */               } 
/*      */             } else {
/*      */               
/*  816 */               getResponder().getCommunicator().sendAlertServerMessage("Failed to unban ip " + ip + '.');
/*      */             } 
/*      */           }
/*      */         } 
/*  820 */       } catch (NumberFormatException nfe) {
/*      */ 
/*      */         
/*  823 */         logger.log(Level.WARNING, "Problem parsing the pardonip value: " + val + ". Responder: " + getResponder() + " due to " + nfe, nfe);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void logMgmt(String logString) {
/*  831 */     if (getResponder().getLogger() != null)
/*  832 */       getResponder().getLogger().log(Level.INFO, getResponder().getName() + " " + logString); 
/*  833 */     logger.log(Level.INFO, getResponder().getName() + " " + logString);
/*  834 */     Players.addMgmtMessage(getResponder().getName(), logString);
/*  835 */     Message mess = new Message(getResponder(), (byte)9, "MGMT", "<" + getResponder().getName() + "> " + logString);
/*      */     
/*  837 */     Server.getInstance().addMessage(mess);
/*      */   }
/*      */ 
/*      */   
/*      */   private final void log(String logString) {
/*  842 */     if (getResponder().getLogger() != null)
/*  843 */       getResponder().getLogger().log(Level.INFO, getResponder().getName() + " " + logString); 
/*  844 */     logger.log(Level.INFO, getResponder().getName() + " " + logString);
/*  845 */     Players.addGmMessage(getResponder().getName(), logString);
/*  846 */     Message mess = new Message(getResponder(), (byte)11, "GM", "<" + getResponder().getName() + "> " + logString);
/*      */     
/*  848 */     Server.getInstance().addMessage(mess);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuestion() {
/*  859 */     StringBuilder buf = new StringBuilder();
/*  860 */     buf.append(getBmlHeader());
/*  861 */     if (getResponder().getPower() >= 2 || getResponder().mayMute()) {
/*      */       
/*  863 */       Player[] players = Players.getInstance().getPlayers();
/*      */       
/*  865 */       Arrays.sort((Object[])players);
/*  866 */       buf.append("text{type=\"bold\";text=\"---------------- Select Player ------------------\"}");
/*  867 */       String displayWarnings = "";
/*  868 */       if (getResponder().getPower() >= 2)
/*      */       {
/*  870 */         displayWarnings = " - warnings:";
/*      */       }
/*  872 */       buf.append("harray{label{text=\"Player name - (m)uted" + displayWarnings + "\"}");
/*  873 */       buf.append("dropdown{id='ddname';options=\"");
/*  874 */       buf.append("Use textbox");
/*  875 */       StringBuilder chattingBuild = new StringBuilder();
/*  876 */       for (int x = 0; x < players.length; x++) {
/*      */         
/*  878 */         if (players[x].getWurmId() != getResponder().getWurmId())
/*      */         {
/*  880 */           if (players[x].getPower() <= getResponder().getWurmId())
/*      */           {
/*  882 */             if (getResponder().getPower() >= 2 || (
/*  883 */               getResponder().getKingdomId() == players[x].getKingdomId() && players[x].isActiveInChat())) {
/*      */               
/*  885 */               buf.append(",");
/*  886 */               this.playlist.add(players[x]);
/*  887 */               buf.append(players[x].getName());
/*  888 */               if (players[x].isMute())
/*      */               {
/*  890 */                 buf.append(" (m)");
/*      */               }
/*  892 */               if (getResponder().getPower() >= 2 && players[x].getWarnings() > 0) {
/*      */                 
/*  894 */                 buf.append(" - ");
/*  895 */                 buf.append(players[x].getWarnings());
/*      */               } 
/*  897 */               if (getResponder().getPower() >= 2 && players[x].isActiveInChat())
/*  898 */                 chattingBuild.append(players[x].getName() + " "); 
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*  903 */       buf.append("\"}label{text=\"  \"};input{id='pname';maxchars='32'}}");
/*  904 */       buf.append("text{text=\"\"};");
/*  905 */       buf.append("text{type=\"bold\";text=\"-------------- Chat control --------------------\"}");
/*  906 */       if (getResponder().getPower() > 0) {
/*      */         
/*  908 */         buf.append("text{text=\"Recently active in kchat:\"}");
/*  909 */         buf.append("text{text=\"" + chattingBuild.toString() + "\"}");
/*      */       } 
/*  911 */       buf.append("harray{checkbox{id=\"mute\";text=\"Mute \"};checkbox{id=\"unmute\";text=\"Unmute \"};checkbox{id=\"mutewarn\";text=\"Mutewarn \"};dropdown{id='mutereason';options=\"");
/*      */       
/*  913 */       buf.append("Profane language");
/*  914 */       buf.append(", ");
/*  915 */       buf.append("Racial or sexist remarks");
/*  916 */       buf.append(", ");
/*  917 */       buf.append("Staff bashing");
/*  918 */       buf.append(", ");
/*  919 */       buf.append("Harassment");
/*  920 */       buf.append(", ");
/*  921 */       buf.append("Spam");
/*  922 */       buf.append(", ");
/*  923 */       buf.append("Insubordination");
/*  924 */       buf.append(", ");
/*  925 */       buf.append("Repeated warnings");
/*  926 */       buf.append("\"}");
/*  927 */       buf.append("label{text=\"Hours:\"};dropdown{id='mutetime';default='0';options=\"");
/*  928 */       buf.append(1);
/*  929 */       buf.append(", ");
/*  930 */       buf.append(2);
/*  931 */       buf.append(", ");
/*  932 */       buf.append(5);
/*  933 */       buf.append(", ");
/*  934 */       buf.append(8);
/*  935 */       buf.append(", ");
/*  936 */       buf.append(24);
/*  937 */       buf.append(", ");
/*  938 */       buf.append(48);
/*  939 */       buf.append("\"}}");
/*  940 */       buf.append("harray{label{text=\"Or enter reason:\"};input{maxchars=\"40\";id=\"mutereasontb\"};}");
/*  941 */       buf.append("text{text=\"\"};");
/*      */       
/*  943 */       if (getResponder().getPower() >= 2) {
/*      */         
/*  945 */         buf.append("text{type=\"bold\";text=\"---------------- Summon ------------------\"}");
/*  946 */         String sel = ";selected=\"true\"";
/*  947 */         if (!getResponder().isOnSurface())
/*      */         {
/*  949 */           sel = ";selected=\"false\"";
/*      */         }
/*  951 */         buf.append("harray{checkbox{id='summon';selected='false';text=\"Teleport/Set to \"};label{text=\"TX:\"};input{id='tilex';maxchars='5';text=\"" + 
/*  952 */             getResponder().getTileX() + "\"};label{text=\"TY:\"};input{id='tiley';maxchars='5';text=\"" + 
/*  953 */             getResponder().getTileY() + "\"};checkbox{id='surfaced'" + sel + ";text=\"Surfaced \"}}");
/*      */         
/*  955 */         buf.append("text{text=\"\"};");
/*  956 */         buf.append("text{type=\"bold\";text=\"--------------- IPBan control -------------------\"}");
/*  957 */         buf.append("harray{checkbox{id=\"pardon\";text=\"Pardon ban \"};checkbox{id=\"ban\";text=\"IPBan \"};checkbox{id=\"banip\";text=\"IPBan IP \"};checkbox{id=\"warn\";text=\"Warn \"};checkbox{id=\"resetWarn\";text=\"Reset warnings \"}};");
/*      */ 
/*      */         
/*  960 */         buf.append("harray{label{text=\"Ip to ban:\"};input{id='iptoban';maxchars='16'}};");
/*  961 */         buf.append("harray{label{text=\"IPBan reason (max 250 chars):\"};input{id='banreason';text=\"Griefing\"}};");
/*  962 */         buf.append("harray{label{text=\"Days:\"};dropdown{id='bantime';default=\"1\";options=\"");
/*  963 */         buf.append(1);
/*  964 */         buf.append(", ");
/*  965 */         buf.append(3);
/*  966 */         buf.append(", ");
/*  967 */         buf.append(7);
/*  968 */         buf.append(", ");
/*  969 */         buf.append(30);
/*  970 */         buf.append(", ");
/*  971 */         buf.append(90);
/*  972 */         buf.append(", ");
/*  973 */         buf.append(365);
/*  974 */         buf.append(", ");
/*  975 */         buf.append(9999);
/*  976 */         buf.append("\"}}");
/*  977 */         buf.append("text{text=\"\"};");
/*  978 */         buf.append("text{type=\"bold\";text=\"--------------- Bans -------------------\"}");
/*  979 */         buf.append("harray{label{text=\"Pardon:\"};dropdown{id='pardonip';options=\"");
/*  980 */         buf.append("None");
/*  981 */         Ban[] bans = Players.getInstance().getBans();
/*  982 */         for (int i = 0; i < bans.length; i++) {
/*      */           
/*  984 */           buf.append(", ");
/*  985 */           buf.append(bans[i].getIdentifier());
/*  986 */           this.iplist.add(bans[i].getIdentifier());
/*      */         } 
/*  988 */         buf.append("\"}};");
/*  989 */         buf.append("text{text=\"\"};");
/*  990 */         buf.append("text{type=\"bold\";text=\"---------------- Locate Corpse ------------------\"}");
/*  991 */         buf.append("text{type=\"italic\";text=\"Uses name at top of form now.\"}");
/*  992 */         buf.append("harray{checkbox{id=\"locate\";text=\"Locate Corpse? \"}}");
/*  993 */         buf.append("text{text=\"\"};");
/*  994 */         buf.append("text{type=\"bold\";text=\"---------------- GM Tool (In-Game GM Interface) ------------------\"}");
/*  995 */         buf.append("text{type=\"italic\";text=\"This should only be used on the server pertaining to the item or player.\"}");
/*  996 */         buf.append("checkbox{id=\"gmtool\";text=\"Start GM Tool? \"}");
/*  997 */         buf.append("label{text=\"Either select player name at top\"}");
/*  998 */         buf.append("harray{label{text=\"or specify a WurmId: \"};input{id=\"wurmid\";maxchars=\"20\";text=\"\"}}");
/*  999 */         buf.append("harray{label{text=\"or specify an Email Address to search for: \"};input{id=\"searchemail\";maxchars=\"60\";text=\"\"}}");
/* 1000 */         buf.append("harray{label{text=\"or specify an IP Address to search for: \"};input{id=\"searchip\";maxchars=\"30\";text=\"\"}}");
/* 1001 */         buf.append("text{text=\"\"};");
/*      */       } 
/* 1003 */       buf.append("text{type=\"bold\";text=\"--------------- Help -------------------\"}");
/* 1004 */       buf.append("text{text=\"Either type a name in the textbox or select a name from the list.\"}");
/* 1005 */       buf.append("text{text=\"You may check as many boxes you wish and all options will apply to the player you select.\"}");
/* 1006 */       if (getResponder().getPower() >= 2) {
/*      */         
/* 1008 */         buf.append("text{text=\"If you cross the ban ip checkbox when a player is selected, his ip will be banned for 7 days max.\"}");
/* 1009 */         buf.append("text{text=\"If you want to extend this ban you will have to type it in the ip address box.\"}");
/*      */       } 
/* 1011 */       buf.append(createAnswerButton2());
/* 1012 */       int len = (getResponder().getPower() >= 2) ? 500 : 300;
/* 1013 */       getResponder().getCommunicator().sendBml(500, len, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */