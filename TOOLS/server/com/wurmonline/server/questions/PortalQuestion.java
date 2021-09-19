/*      */ package com.wurmonline.server.questions;
/*      */ 
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.Spawnpoint;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
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
/*      */ public final class PortalQuestion
/*      */   extends Question
/*      */   implements MonetaryConstants, TimeConstants
/*      */ {
/*      */   private final Item portal;
/*   55 */   private static final Logger logger = Logger.getLogger(PortalQuestion.class.getName());
/*      */ 
/*      */   
/*      */   private static final int maxItems = 200;
/*      */ 
/*      */   
/*      */   private static final int standardBodyInventoryItems = 12;
/*      */ 
/*      */   
/*      */   public static final int PORTAL_FREEDOM_ID = 100000;
/*      */   
/*      */   public static final int PORTAL_EPIC_ID = 100001;
/*      */   
/*      */   public static final int PORTAL_CHALLENGE_ID = 100002;
/*      */   
/*      */   public static final boolean allowPortalToLatestServer = true;
/*      */   
/*   72 */   private int step = 0;
/*      */   
/*   74 */   private int selectedServer = 100000;
/*      */   
/*   76 */   private byte selectedKingdom = 0;
/*   77 */   private int selectedSpawn = -1;
/*      */   
/*      */   public static boolean epicPortalsEnabled = true;
/*      */   
/*   81 */   private String cyan = "66,200,200";
/*   82 */   private String green = "66,225,66";
/*   83 */   private String orange = "255,156,66";
/*   84 */   private String purple = "166,166,66";
/*   85 */   private String red = "255,66,66";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PortalQuestion(Creature aResponder, String aTitle, String aQuestion, Item _portal) {
/*   95 */     super(aResponder, aTitle, aQuestion, 76, _portal.getWurmId());
/*   96 */     this.portal = _portal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void answer(Properties aAnswers) {
/*  107 */     String val = aAnswers.getProperty("portalling");
/*      */     
/*  109 */     getResponder().sendToLoggers(" at A: " + val + " selectedServer=" + this.selectedServer);
/*  110 */     if (val != null && val.equals("true")) {
/*      */       
/*  112 */       if (this.portal != null) {
/*      */         
/*  114 */         byte targetKingdom = 0;
/*  115 */         int data1 = this.portal.getData1();
/*  116 */         if (this.step == 1)
/*  117 */           data1 = this.selectedServer; 
/*  118 */         getResponder().sendToLoggers(" at A: " + val + " selectedServer=" + data1);
/*  119 */         ServerEntry entry = Servers.getServerWithId(data1);
/*  120 */         if (entry != null) {
/*      */           
/*  122 */           getResponder().sendToLoggers(" at 1: " + data1);
/*      */           
/*  124 */           if (entry.id == Servers.loginServer.id)
/*  125 */             entry = Servers.loginServer; 
/*  126 */           boolean changingCluster = false;
/*  127 */           boolean newTutorial = (this.portal.getTemplateId() == 855);
/*  128 */           if (Servers.localServer.EPIC != entry.EPIC && !newTutorial) {
/*      */ 
/*      */             
/*  131 */             changingCluster = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  138 */             if (!this.portal.isEpicPortal()) {
/*      */               
/*  140 */               getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. This is not an epic portal.");
/*      */               
/*      */               return;
/*      */             } 
/*  144 */             if (!epicPortalsEnabled && getResponder().getPower() == 0) {
/*      */               
/*  146 */               getResponder().getCommunicator().sendNormalServerMessage("The portal won't let you just yet.");
/*      */ 
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*  152 */           } else if (Servers.localServer.EPIC && getResponder().isChampion()) {
/*      */             
/*  154 */             if (!this.portal.isEpicPortal()) {
/*      */               
/*  156 */               getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. You could not use this portal since you are a champion.");
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           
/*  163 */           if (getResponder().getEnemyPresense() > 0) {
/*      */             
/*  165 */             getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. You sense a disturbance.");
/*      */             
/*      */             return;
/*      */           } 
/*  169 */           if (getResponder().hasBeenAttackedWithin(300)) {
/*      */             
/*  171 */             getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. You sense a disturbance - maybe your are not calm enough yet.");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  176 */           if (Servers.localServer.isChallengeServer())
/*  177 */             changingCluster = true; 
/*  178 */           if (Servers.localServer.entryServer)
/*  179 */             changingCluster = false; 
/*  180 */           if (changingCluster)
/*      */           {
/*  182 */             if (getResponder().isChampion() && !Servers.localServer.EPIC)
/*      */             {
/*  184 */               if (!this.portal.isEpicPortal()) {
/*      */                 
/*  186 */                 getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. You could not use this portal since you are a champion.");
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             }
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  198 */           if (getResponder().getPower() == 0 && entry.entryServer && !Servers.localServer.testServer) {
/*      */             
/*  200 */             getResponder().getCommunicator().sendNormalServerMessage("Nothing happens.");
/*      */             return;
/*      */           } 
/*  203 */           if (this.portal.isEpicPortal()) {
/*      */             
/*  205 */             if (!changingCluster && !Servers.localServer.entryServer) {
/*      */               
/*  207 */               getResponder().getCommunicator().sendNormalServerMessage("Nothing happens. Actually this shouldn't be possible.");
/*      */               
/*      */               return;
/*      */             } 
/*      */             
/*  212 */             long time = System.currentTimeMillis() - (((Player)getResponder()).getSaveFile()).lastUsedEpicPortal;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  220 */             if (getResponder().getEpicServerKingdom() == 0) {
/*      */               
/*  222 */               String kingdomid = "kingdid";
/*  223 */               String kval = aAnswers.getProperty("kingdid");
/*  224 */               if (kval != null) {
/*      */                 
/*      */                 try {
/*      */                   
/*  228 */                   targetKingdom = Byte.parseByte(kval);
/*      */                 }
/*  230 */                 catch (NumberFormatException nfe) {
/*      */                   
/*  232 */                   logger.log(Level.WARNING, "Failed to parse " + kval + " to a valid byte.");
/*  233 */                   getResponder().getCommunicator().sendAlertServerMessage("An error occured with the target kingdom. You can't select that kingdom.");
/*      */ 
/*      */ 
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               }
/*      */             } else {
/*  241 */               targetKingdom = getResponder().getEpicServerKingdom();
/*  242 */               if (Servers.isThisAChaosServer()) {
/*  243 */                 logger.log(Level.INFO, getResponder().getName() + " joining " + targetKingdom);
/*      */               }
/*      */             } 
/*  246 */             ((Player)getResponder()).getSaveFile().setEpicLocation(targetKingdom, entry.id);
/*  247 */             getResponder().setRotation(270.0F);
/*  248 */             int targetTileX = entry.SPAWNPOINTJENNX;
/*  249 */             int targetTileY = entry.SPAWNPOINTJENNY;
/*  250 */             if (targetKingdom == 2) {
/*      */               
/*  252 */               getResponder().setRotation(90.0F);
/*  253 */               targetTileX = entry.SPAWNPOINTMOLX;
/*  254 */               targetTileY = entry.SPAWNPOINTMOLY;
/*      */             }
/*  256 */             else if (targetKingdom == 3) {
/*      */               
/*  258 */               getResponder().setRotation(1.0F);
/*  259 */               targetTileX = entry.SPAWNPOINTLIBX;
/*  260 */               targetTileY = entry.SPAWNPOINTLIBY;
/*      */             } 
/*  262 */             if (Servers.localServer.entryServer)
/*      */             {
/*  264 */               if (getResponder().isPlayer())
/*      */               {
/*  266 */                 ((Player)getResponder()).addTitle(Titles.Title.Educated);
/*      */               }
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  272 */             if (getResponder().isPlayer()) {
/*  273 */               ((Player)getResponder()).getSaveFile().setBed(this.portal.getWurmId());
/*      */             }
/*  275 */             getResponder().sendTransfer(Server.getInstance(), entry.INTRASERVERADDRESS, 
/*  276 */                 Integer.parseInt(entry.INTRASERVERPORT), entry.INTRASERVERPASSWORD, entry.id, targetTileX, targetTileY, true, 
/*  277 */                 (getResponder().getPower() <= 0), targetKingdom);
/*      */             
/*      */             return;
/*      */           } 
/*  281 */           if (entry.HOMESERVER) {
/*      */             
/*  283 */             if (entry.KINGDOM != 0) {
/*  284 */               targetKingdom = entry.KINGDOM;
/*      */             } else {
/*  286 */               targetKingdom = (this.selectedKingdom == 0) ? getResponder().getKingdomId() : this.selectedKingdom;
/*      */             } 
/*      */           } else {
/*      */             
/*  290 */             String kingdomid = "kingdid";
/*  291 */             String kval = aAnswers.getProperty("kingdid");
/*  292 */             if (kval != null) {
/*      */ 
/*      */               
/*      */               try {
/*  296 */                 targetKingdom = Byte.parseByte(kval);
/*  297 */                 getResponder().sendToLoggers(" at kingdid: " + entry
/*  298 */                     .getName() + " selected kingdom " + targetKingdom);
/*      */               }
/*  300 */               catch (NumberFormatException nfe) {
/*      */                 
/*  302 */                 targetKingdom = getResponder().getKingdomId();
/*      */               } 
/*      */             } else {
/*      */               
/*  306 */               targetKingdom = (this.selectedKingdom == 0) ? getResponder().getKingdomId() : this.selectedKingdom;
/*      */             } 
/*  308 */           }  getResponder().sendToLoggers(" at 1: " + entry.getName() + " target kingdom " + targetKingdom);
/*  309 */           if (entry.isAvailable(getResponder().getPower(), getResponder().isReallyPaying())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  317 */             if (!entry.ISPAYMENT || getResponder().isReallyPaying()) {
/*      */               
/*  319 */               int numitems = 0;
/*  320 */               int stayBehind = 0;
/*  321 */               Item[] inventoryItems = getResponder().getInventory().getAllItems(true);
/*  322 */               for (int x = 0; x < inventoryItems.length; x++) {
/*      */                 
/*  324 */                 if (!inventoryItems[x].willLeaveServer(true, changingCluster, 
/*  325 */                     (getResponder().getPower() > 0))) {
/*      */                   
/*  327 */                   stayBehind++;
/*  328 */                   getResponder().getCommunicator().sendNormalServerMessage("The " + inventoryItems[x]
/*  329 */                       .getName() + " stays behind.");
/*      */                 } 
/*      */               } 
/*  332 */               Item[] bodyItems = getResponder().getBody().getAllItems();
/*  333 */               for (int i = 0; i < bodyItems.length; i++) {
/*      */                 
/*  335 */                 if (!bodyItems[i].willLeaveServer(true, changingCluster, (getResponder().getPower() > 0))) {
/*      */                   
/*  337 */                   stayBehind++;
/*  338 */                   getResponder().getCommunicator().sendNormalServerMessage("The " + bodyItems[i]
/*  339 */                       .getName() + " stays behind.");
/*      */                 } 
/*      */               } 
/*  342 */               if (getResponder().getPower() == 0)
/*  343 */                 numitems = inventoryItems.length + bodyItems.length - stayBehind - 12; 
/*  344 */               if (numitems < 200) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  353 */                 getResponder().getCommunicator().sendNormalServerMessage("You step through the portal. Will you ever return?");
/*      */                 
/*  355 */                 if (getResponder().getPower() == 0 && changingCluster) {
/*      */                   
/*      */                   try {
/*      */ 
/*      */ 
/*      */                     
/*  361 */                     getResponder().setLastKingdom();
/*  362 */                     getResponder().getStatus().setKingdom(targetKingdom);
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   }
/*  368 */                   catch (IOException iox) {
/*      */                     
/*  370 */                     getResponder().getCommunicator().sendNormalServerMessage("A sudden strong wind blows through the portal, throwing you back!");
/*      */                     
/*  372 */                     logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */ 
/*      */ 
/*      */                     
/*      */                     return;
/*      */                   } 
/*      */                 }
/*      */ 
/*      */                 
/*  381 */                 if (changingCluster) {
/*      */                   
/*  383 */                   if (getResponder().getPower() <= 0) {
/*      */ 
/*      */                     
/*      */                     try {
/*  387 */                       Skill fs = getResponder().getSkills().getSkill(1023);
/*  388 */                       if (fs.getKnowledge() > 50.0D)
/*      */                       {
/*  390 */                         double d1 = 100.0D - fs.getKnowledge();
/*  391 */                         d1 -= d1 * 0.95D;
/*  392 */                         double newskill = fs.getKnowledge() - d1;
/*  393 */                         fs.setKnowledge(newskill, false);
/*  394 */                         getResponder().getCommunicator().sendAlertServerMessage("Your group fighting skill has been set to " + fs
/*  395 */                             .getKnowledge(0.0D) + "!");
/*      */                       }
/*      */                     
/*      */                     }
/*  399 */                     catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*      */                     try {
/*  405 */                       Skill as = getResponder().getSkills().getSkill(1030);
/*  406 */                       if (as.getKnowledge() > 50.0D)
/*      */                       {
/*  408 */                         double d1 = 100.0D - as.getKnowledge();
/*  409 */                         d1 -= d1 * 0.95D;
/*  410 */                         double newskill = as.getKnowledge() - d1;
/*  411 */                         as.setKnowledge(newskill, false);
/*  412 */                         getResponder().getCommunicator().sendAlertServerMessage("Your archery skill has been set to " + as
/*  413 */                             .getKnowledge(0.0D) + "!");
/*      */                       }
/*      */                     
/*  416 */                     } catch (NoSuchSkillException noSuchSkillException) {}
/*      */                   } 
/*      */ 
/*      */ 
/*      */                   
/*  421 */                   getResponder().setLastChangedCluster();
/*      */                 } 
/*  423 */                 int targetTileX = entry.SPAWNPOINTJENNX;
/*  424 */                 int targetTileY = entry.SPAWNPOINTJENNY;
/*  425 */                 if (targetKingdom == 2) {
/*      */                   
/*  427 */                   targetTileX = entry.SPAWNPOINTMOLX;
/*  428 */                   targetTileY = entry.SPAWNPOINTMOLY;
/*      */                 }
/*  430 */                 else if (targetKingdom == 3) {
/*      */                   
/*  432 */                   targetTileX = entry.SPAWNPOINTLIBX;
/*  433 */                   targetTileY = entry.SPAWNPOINTLIBY;
/*      */                 } 
/*  435 */                 getResponder().sendToLoggers("Before spawnpoints: " + this.selectedSpawn + ", server=" + this.selectedServer + ",kingdom=" + this.selectedKingdom + " entry name=" + entry
/*      */                     
/*  437 */                     .getName());
/*  438 */                 Spawnpoint[] spawns = entry.getSpawns();
/*  439 */                 if (spawns != null) {
/*      */                   
/*  441 */                   String kval = aAnswers.getProperty("spawnpoint");
/*  442 */                   getResponder().sendToLoggers("Inside spawns. Length is " + spawns.length + " kval=" + kval);
/*      */                   
/*  444 */                   int spnum = -1;
/*  445 */                   if (kval != null) {
/*      */                     
/*  447 */                     kval = kval.replace("spawn", "");
/*      */                     
/*      */                     try {
/*  450 */                       spnum = Integer.parseInt(kval);
/*      */                     }
/*  452 */                     catch (NumberFormatException nfe) {
/*      */                       
/*  454 */                       spnum = this.selectedSpawn;
/*      */                     } 
/*      */                   } else {
/*      */                     
/*  458 */                     spnum = this.selectedSpawn;
/*  459 */                   }  getResponder().sendToLoggers("Before loop. " + spnum);
/*  460 */                   for (Spawnpoint sp : spawns) {
/*      */                     
/*  462 */                     if (!entry.HOMESERVER && spnum < 0)
/*      */                     {
/*  464 */                       if (sp.kingdom == targetKingdom) {
/*      */                         
/*  466 */                         this.selectedSpawn = sp.number;
/*  467 */                         getResponder().sendToLoggers("Inside spawnpoints. Just selected " + this.selectedSpawn + " AT RANDOM, server=" + this.selectedServer + ",kingdom=" + this.selectedKingdom);
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/*  472 */                         targetTileX = sp.tilex - 2 + Server.rand.nextInt(5);
/*  473 */                         targetTileY = sp.tiley - 2 + Server.rand.nextInt(5);
/*      */                         
/*      */                         break;
/*      */                       } 
/*      */                     }
/*  478 */                     if (sp.number == this.selectedSpawn) {
/*      */                       
/*  480 */                       getResponder().sendToLoggers("Using selected spawn " + this.selectedSpawn);
/*      */                       
/*  482 */                       targetTileX = sp.tilex - 2 + Server.rand.nextInt(5);
/*  483 */                       targetTileY = sp.tiley - 2 + Server.rand.nextInt(5);
/*      */                       break;
/*      */                     } 
/*  486 */                     if (spnum == sp.number) {
/*      */                       
/*  488 */                       this.selectedSpawn = sp.number;
/*  489 */                       getResponder().sendToLoggers("Inside spawnpoints. Just selected " + this.selectedSpawn + ", server=" + this.selectedServer + ",kingdom=" + this.selectedKingdom);
/*      */ 
/*      */ 
/*      */                       
/*  493 */                       if (getResponder().getPower() <= 0 && targetKingdom == 0)
/*  494 */                         targetKingdom = sp.kingdom; 
/*  495 */                       targetTileX = sp.tilex - 2 + Server.rand.nextInt(5);
/*  496 */                       targetTileY = sp.tiley - 2 + Server.rand.nextInt(5);
/*      */                       break;
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*  501 */                 getResponder().sendToLoggers(" at 4: " + entry
/*  502 */                     .getName() + " target kingdom " + targetKingdom + "tx=" + targetTileX + ", ty=" + targetTileY);
/*      */                 
/*  504 */                 if (Servers.localServer.entryServer) {
/*      */                   
/*  506 */                   getResponder().setRotation(270.0F);
/*  507 */                   if (getResponder().isPlayer())
/*      */                   {
/*  509 */                     ((Player)getResponder()).addTitle(Titles.Title.Educated);
/*      */                   }
/*      */                 } 
/*  512 */                 if (newTutorial)
/*  513 */                   getResponder().setFlag(76, false); 
/*  514 */                 getResponder().sendTransfer(Server.getInstance(), entry.INTRASERVERADDRESS, 
/*  515 */                     Integer.parseInt(entry.INTRASERVERPORT), entry.INTRASERVERPASSWORD, entry.id, targetTileX, targetTileY, true, entry
/*  516 */                     .isChallengeServer(), targetKingdom);
/*      */               } else {
/*      */                 
/*  519 */                 getResponder().getCommunicator().sendNormalServerMessage("The portal does not work. You are probably carrying too much. Try 200 items on body and in inventory.");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  524 */               getResponder()
/*  525 */                 .getCommunicator()
/*  526 */                 .sendNormalServerMessage("Alas! A trifle stops you from entering the portal. You need to purchase some nice premium time in order to enter the portal.");
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  531 */           else if (entry.maintaining) {
/*  532 */             getResponder().getCommunicator().sendNormalServerMessage("The portal is shut but a flicker indicates that it may open soon. You may try later.");
/*      */           }
/*  534 */           else if (entry.isFull()) {
/*  535 */             getResponder()
/*  536 */               .getCommunicator()
/*  537 */               .sendNormalServerMessage("The portal is shut. " + entry.currentPlayers + " people are on the other side of the portal but only " + entry.pLimit + " are allowed. Please note that we are adding new servers as soon as possible when all available servers are full.");
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/*  544 */             getResponder().getCommunicator().sendNormalServerMessage("The portal is shut. The lands beyond are not available at the moment.");
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  549 */           getResponder().getCommunicator().sendNormalServerMessage("The portal is shut. No matter what you try nothing happens.");
/*      */         } 
/*      */       } else {
/*      */         
/*  553 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to step through the portal.");
/*      */       }
/*      */     
/*      */     }
/*  557 */     else if (this.step == 1) {
/*      */ 
/*      */       
/*  560 */       String val2 = aAnswers.getProperty("sid");
/*  561 */       if (val2 != null || val == null) {
/*      */         
/*      */         try {
/*      */           
/*  565 */           int spnum = this.selectedSpawn;
/*  566 */           getResponder()
/*  567 */             .sendToLoggers("At 1: " + this.selectedSpawn + ", server=" + this.selectedServer + ", val2=" + val2 + " kingdom=" + this.selectedKingdom);
/*      */ 
/*      */ 
/*      */           
/*  571 */           if (val2 != null) {
/*      */             
/*  573 */             this.selectedServer = Integer.parseInt(val2);
/*  574 */             getResponder().sendToLoggers("At 2: val 2 is not null server=" + this.selectedServer + ", val2=" + val2);
/*      */           } 
/*      */ 
/*      */           
/*  578 */           ServerEntry entry = Servers.getServerWithId(this.selectedServer);
/*  579 */           if (entry != null) {
/*      */             
/*  581 */             Spawnpoint[] spawns = entry.getSpawns();
/*  582 */             if (spawns != null) {
/*      */               
/*  584 */               getResponder().sendToLoggers("At 2.5: server=" + this.selectedServer + " spawn " + spnum);
/*      */               
/*  586 */               String str = aAnswers.getProperty("spawnpoint");
/*  587 */               if (str != null) {
/*      */                 
/*  589 */                 getResponder().sendToLoggers("At 2.6: server=" + this.selectedServer + " spawn kval " + str);
/*      */                 
/*  591 */                 str = str.replace("spawn", "");
/*      */                 
/*      */                 try {
/*  594 */                   spnum = Integer.parseInt(str);
/*  595 */                   getResponder().sendToLoggers("At 2.7: server=" + this.selectedServer + " spawn spnum " + spnum);
/*      */                   
/*  597 */                   for (Spawnpoint sp : spawns) {
/*      */                     
/*  599 */                     if (sp.number == spnum) {
/*      */                       
/*  601 */                       getResponder().sendToLoggers("At 2.8: spawn " + sp.name);
/*  602 */                       this.selectedKingdom = sp.kingdom;
/*      */                       
/*      */                       break;
/*      */                     } 
/*      */                   } 
/*  607 */                 } catch (NumberFormatException numberFormatException) {}
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  614 */           String kingdomid = "kingdid";
/*  615 */           String kval = aAnswers.getProperty("kingdid");
/*  616 */           if (kval != null) {
/*      */             
/*      */             try {
/*      */               
/*  620 */               this.selectedKingdom = Byte.parseByte(kval);
/*  621 */               getResponder().sendToLoggers("At 3: " + spnum + ", server=" + this.selectedServer + ", val2=" + val2 + " selected kingdom=" + this.selectedKingdom);
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*  626 */             catch (NumberFormatException nfe) {
/*      */               
/*  628 */               this.selectedKingdom = getResponder().getKingdomId();
/*      */             } 
/*      */           }
/*      */           
/*  632 */           PortalQuestion pq = new PortalQuestion(getResponder(), "Entering portal", "Go ahead!", this.portal);
/*      */           
/*  634 */           pq.step = 1;
/*  635 */           pq.selectedServer = this.selectedServer;
/*  636 */           pq.selectedSpawn = spnum;
/*  637 */           pq.selectedKingdom = this.selectedKingdom;
/*  638 */           pq.sendQuestion();
/*      */         }
/*  640 */         catch (NumberFormatException nfe) {
/*      */           
/*  642 */           logger.log(Level.WARNING, nfe.getMessage() + ": " + val2);
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
/*      */   public void sendQuestion() {
/*  657 */     StringBuilder buf = new StringBuilder();
/*  658 */     buf.append(getBmlHeader());
/*  659 */     if (this.portal != null) {
/*      */       
/*  661 */       byte targetKingdom = this.selectedKingdom;
/*  662 */       int data1 = this.portal.getData1();
/*      */       
/*  664 */       int epicServerId = getResponder().getEpicServerId();
/*  665 */       if (this.step == 1) {
/*  666 */         data1 = this.selectedServer;
/*      */       
/*      */       }
/*  669 */       else if (this.portal.isEpicPortal()) {
/*      */ 
/*      */ 
/*      */         
/*  673 */         if (epicServerId > 0 && epicServerId != Servers.localServer.id) {
/*      */           
/*  675 */           data1 = epicServerId;
/*      */           
/*  677 */           ServerEntry serverEntry = Servers.getServerWithId(data1);
/*  678 */           if (serverEntry != null)
/*      */           {
/*  680 */             if (serverEntry.EPIC == Servers.localServer.EPIC) {
/*  681 */               data1 = 100001;
/*      */             }
/*      */           }
/*      */         } else {
/*      */           
/*  686 */           data1 = 100001;
/*      */         } 
/*      */       } 
/*      */       
/*  690 */       ServerEntry entry = Servers.getServerWithId(data1);
/*  691 */       if (entry != null) {
/*      */         
/*  693 */         if (entry.id == Servers.loginServer.id)
/*  694 */           entry = Servers.loginServer; 
/*  695 */         if (getResponder().getPower() == 0 && !Servers.isThisATestServer() && (entry.entryServer || Servers.localServer
/*  696 */           .isChallengeServer() || (entry
/*  697 */           .isChallengeServer() && !Servers.localServer.entryServer))) {
/*      */           
/*  699 */           buf.append("text{type='bold';text=\"The portal looks dormant.\"};");
/*      */ 
/*      */         
/*      */         }
/*  703 */         else if (this.portal.isEpicPortal()) {
/*      */           
/*  705 */           if (epicServerId == entry.id) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  715 */             if (entry.isAvailable(getResponder().getPower(), getResponder().isReallyPaying()))
/*      */             {
/*  717 */               this.step = 1;
/*  718 */               this.selectedServer = entry.id;
/*  719 */               if (entry.EPIC)
/*      */               {
/*  721 */                 buf.append("text{text=\"This portal leads to the Epic server " + entry.name + " where you last left it.\"}");
/*      */               
/*      */               }
/*  724 */               else if (entry.isChallengeServer())
/*      */               {
/*  726 */                 buf.append("text{text=\"This portal leads to the Challenge server '" + entry.name + "'.\"}");
/*      */               }
/*  728 */               else if (entry.PVPSERVER)
/*      */               {
/*  730 */                 buf.append("text{text=\"This portal leads back to the Wild server " + entry.name + " where you last left it.\"}");
/*      */               
/*      */               }
/*      */               else
/*      */               {
/*  735 */                 buf.append("text{text=\"This portal leads to back the Freedom server " + entry.name + " where you last left it.\"}");
/*      */               }
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*  741 */               buf.append("text{text=\"The " + entry.name + " server is currently unavailable to you.\"}");
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  746 */           else if (entry.isAvailable(getResponder().getPower(), getResponder().isReallyPaying())) {
/*      */             
/*  748 */             if (entry.EPIC)
/*      */             {
/*  750 */               buf.append("text{text=\"This portal leads to the Epic server " + entry.name + ". Please select a kingdom to join:\"}");
/*      */               
/*  752 */               addKingdoms(entry, buf);
/*      */             }
/*  754 */             else if (entry.PVPSERVER)
/*      */             {
/*  756 */               buf.append("text{text=\"This portal leads to the Wild server " + entry.name + ". Please select a kingdom to join:\"}");
/*      */               
/*  758 */               addKingdoms(entry, buf);
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  768 */               buf.append("text{text=\"This portal leads to the Freedom server " + entry.name + ". You will join:\"}");
/*      */               
/*  770 */               addKingdoms(entry, buf);
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  776 */             buf.append("text{text=\"The " + entry.name + " server is currently unavailable to you.\"}");
/*      */           } 
/*      */           
/*  779 */           if (!entry.ISPAYMENT || getResponder().isReallyPaying()) {
/*      */             
/*  781 */             if (Servers.localServer.entryServer && getResponder().getPower() == 0) {
/*  782 */               buf.append("text{text=\"Do you wish to enter this portal never to return?\"};");
/*      */             } else {
/*  784 */               buf.append("text{text=\"Do you wish to enter this portal?\"};");
/*  785 */             }  buf.append("radio{ group='portalling'; id='true';text='Yes'}");
/*  786 */             buf.append("radio{ group='portalling'; id='false';text='No';selected='true'}");
/*      */           } else {
/*      */             
/*  789 */             buf.append("text{text=\"Alas! A trifle stops you from entering the portal. You need to purchase some nice premium time in order to enter the portal.\"}");
/*      */           } 
/*      */         } else {
/*      */           
/*  793 */           if (!entry.PVPSERVER) {
/*      */             
/*  795 */             buf.append("text{text='This portal leads to the safe lands of " + 
/*  796 */                 Kingdoms.getNameFor(entry.KINGDOM) + ".'}");
/*      */             
/*  798 */             if (!entry.PVPSERVER && getResponder().getDeity() != null && 
/*  799 */               (getResponder().getDeity()).number == 4)
/*      */             {
/*  801 */               buf.append("text{text=\"You will lose connection with " + (getResponder().getDeity()).name + " if you enter the portal.\"}");
/*      */             }
/*      */             
/*  804 */             if (entry.KINGDOM != 0) {
/*  805 */               targetKingdom = entry.KINGDOM;
/*      */             } else {
/*  807 */               targetKingdom = getResponder().getKingdomId();
/*      */             }
/*      */           
/*      */           }
/*  811 */           else if (entry.KINGDOM != 0 && getResponder().getPower() == 0 && Servers.localServer.entryServer && targetKingdom == 0) {
/*      */             
/*  813 */             targetKingdom = entry.KINGDOM;
/*      */ 
/*      */           
/*      */           }
/*  817 */           else if (targetKingdom == 0) {
/*      */             
/*  819 */             getResponder().sendToLoggers("Not setting kingdom at 12");
/*  820 */             targetKingdom = getResponder().getKingdomId();
/*      */           } else {
/*      */             
/*  823 */             getResponder().sendToLoggers("Keeping kingdom at 12:" + targetKingdom);
/*      */           } 
/*      */           
/*  826 */           if (entry.isAvailable(getResponder().getPower(), getResponder().isReallyPaying())) {
/*      */             
/*  828 */             boolean changingCluster = false;
/*  829 */             boolean changingEpicCluster = false;
/*      */ 
/*      */             
/*  832 */             if (Servers.localServer.PVPSERVER != entry.PVPSERVER) {
/*  833 */               changingCluster = true;
/*  834 */             } else if (Servers.localServer.EPIC != entry.EPIC) {
/*      */ 
/*      */               
/*  837 */               changingCluster = true;
/*  838 */               changingEpicCluster = true;
/*  839 */               buf.append("text{text=\"You will not be able to use this portal. You must use an Epic Portal which you can build yourself using stones and logs.\"};");
/*      */             }
/*  841 */             else if (targetKingdom == 3) {
/*      */               
/*  843 */               buf.append("text{text=\"The portal comes to life! You may pass to " + 
/*  844 */                   Kingdoms.getNameFor((byte)3) + "!\"}");
/*      */             } 
/*  846 */             if (Servers.localServer.entryServer)
/*  847 */               changingCluster = false; 
/*  848 */             if (changingCluster && !changingEpicCluster) {
/*      */               
/*  850 */               if (getResponder().isChampion() && !Servers.localServer.EPIC) {
/*  851 */                 buf.append("text{text=\"You will not be able to use this portal since you are a champion.\"};");
/*      */               }
/*  853 */               if (getResponder().getLastChangedCluster() + 3600000L > System.currentTimeMillis())
/*      */               {
/*  855 */                 buf.append("text{text=\"You will not be able to use this portal since you may only change cluster once per hour.\"};");
/*      */               }
/*  857 */               if (getResponder().getPower() <= 0) {
/*      */ 
/*      */                 
/*      */                 try {
/*  861 */                   Skill fs = getResponder().getSkills().getSkill(1023);
/*  862 */                   if (fs.getKnowledge(0.0D) > 50.0D) {
/*  863 */                     buf.append("text{text=\"Your new group fighting skill will become " + (fs
/*  864 */                         .getKnowledge(0.0D) * 0.949999988079071D) + "!\"};");
/*      */                   }
/*  866 */                 } catch (NoSuchSkillException noSuchSkillException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 try {
/*  872 */                   Skill as = getResponder().getSkills().getSkill(1030);
/*  873 */                   if (as.getKnowledge(0.0D) > 50.0D) {
/*  874 */                     buf.append("text{text=\"Your new group archery skill will become " + (as
/*  875 */                         .getKnowledge(0.0D) * 0.949999988079071D) + "!\"};");
/*      */                   }
/*  877 */                 } catch (NoSuchSkillException noSuchSkillException) {}
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  884 */             int numitems = 0;
/*  885 */             if (!changingEpicCluster) {
/*      */               
/*  887 */               int stayBehind = 0;
/*  888 */               Item[] inventoryItems = getResponder().getInventory().getAllItems(true);
/*  889 */               for (int x = 0; x < inventoryItems.length; x++) {
/*      */                 
/*  891 */                 if (!inventoryItems[x].willLeaveServer(false, changingCluster, 
/*  892 */                     (getResponder().getPower() > 0))) {
/*      */                   
/*  894 */                   stayBehind++;
/*  895 */                   buf.append("text{text=\"The " + inventoryItems[x].getName() + " will stay behind.\"};");
/*  896 */                   if (Servers.localServer.entryServer && inventoryItems[x]
/*  897 */                     .getTemplateId() == 166)
/*  898 */                     buf.append("text{text=\"The structure will be destroyed.\"};"); 
/*      */                 } 
/*      */               } 
/*  901 */               Item[] bodyItems = getResponder().getBody().getAllItems();
/*  902 */               for (int i = 0; i < bodyItems.length; i++) {
/*      */                 
/*  904 */                 if (!bodyItems[i].willLeaveServer(false, changingCluster, 
/*  905 */                     (getResponder().getPower() > 0))) {
/*      */                   
/*  907 */                   stayBehind++;
/*  908 */                   buf.append("text{text=\"The " + bodyItems[i].getName() + " will stay behind.\"};");
/*  909 */                   if (Servers.localServer.entryServer && bodyItems[i]
/*  910 */                     .getTemplateId() == 166)
/*  911 */                     buf.append("text{text=\"The structure will be destroyed.\"};"); 
/*      */                 } 
/*      */               } 
/*  914 */               if (stayBehind > 0)
/*  915 */                 buf.append("text{text=\"Items that stay behind will normally be available again when you return here.\"};"); 
/*  916 */               if (getResponder().getPower() == 0) {
/*  917 */                 numitems = inventoryItems.length + bodyItems.length - stayBehind - 12;
/*      */               }
/*      */             } 
/*      */             
/*  921 */             if (numitems > 200) {
/*      */               
/*  923 */               buf.append("text{text=\"The portal seems to become unresponsive as you approach. You are carrying too much. Try removing " + (numitems - 200) + " items from body and inventory.\"};");
/*      */             
/*      */             }
/*  926 */             else if (!entry.ISPAYMENT || getResponder().isReallyPaying()) {
/*      */               
/*  928 */               if (Servers.localServer.entryServer && getResponder().getPower() == 0) {
/*  929 */                 buf.append("text{text=\"Do you wish to enter this portal never to return?\"};");
/*      */               } else {
/*  931 */                 buf.append("text{text=\"Do you wish to enter this portal?\"};");
/*  932 */               }  if (getResponder().getPower() == 0 && Servers.localServer.entryServer)
/*  933 */                 buf.append("text{type='bold';text=\"Note that you will automatically convert to a " + 
/*  934 */                     Kingdoms.getNameFor(targetKingdom) + "!\"};"); 
/*  935 */               buf.append("radio{ group='portalling'; id='true';text='Yes'}");
/*  936 */               buf.append("radio{ group='portalling'; id='false';text='No';selected='true'}");
/*      */             } else {
/*      */               
/*  939 */               buf.append("text{text=\"Alas! A trifle stops you from entering the portal. You need to purchase some nice premium time in order to enter the portal.\"}");
/*      */             }
/*      */           
/*      */           }
/*  943 */           else if (entry.maintaining) {
/*  944 */             buf.append("text{text=\"The portal is shut but a flicker indicates that it may open soon. You may try later.\"}");
/*  945 */           } else if (entry.isFull()) {
/*  946 */             buf.append("text{text=\"The portal is shut. " + entry.currentPlayers + " people are on the other side of the portal but only " + entry.pLimit + " are allowed.\"}");
/*      */           }
/*      */           else {
/*      */             
/*  950 */             buf.append("text{text=\"The portal is shut. The lands beyond are not available at the moment.\"}");
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  955 */         if (data1 == 100000 || data1 == 100001 || data1 == 100002) {
/*      */           
/*  957 */           buf.setLength(0);
/*  958 */           sendQuestion2(data1);
/*      */           
/*      */           return;
/*      */         } 
/*  962 */         buf.append("text{text=\"The portal is shut. No matter what you try nothing happens.\"}");
/*      */       } 
/*      */     } else {
/*  965 */       buf.append("text{text=\"The portal fades from view and becomes immaterial. No matter what you try nothing happens.\"}");
/*  966 */     }  buf.append(createAnswerButton2());
/*  967 */     getResponder().getCommunicator().sendBml(700, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void sendQuestion2(int portalNumber) {
/*  972 */     StringBuilder buf = new StringBuilder();
/*  973 */     buf.append(getBmlHeader());
/*  974 */     this.step = 1;
/*  975 */     boolean selected = true;
/*      */     
/*  977 */     if (portalNumber != 100000 && portalNumber != 100001 && portalNumber != 100002)
/*  978 */       this.selectedServer = portalNumber; 
/*  979 */     List<ServerEntry> entries = Servers.getServerList(portalNumber);
/*  980 */     if (this.portal.isEpicPortal() && !epicPortalsEnabled && getResponder().getPower() == 0)
/*  981 */       entries.clear(); 
/*  982 */     if (entries.size() == 0) {
/*      */       
/*  984 */       buf.append("text{text=\"The portal is shut. No matter what you try nothing happens.\"}");
/*      */     }
/*      */     else {
/*      */       
/*  988 */       ServerEntry[] entryArr = entries.<ServerEntry>toArray(new ServerEntry[entries.size()]);
/*  989 */       Arrays.sort((Object[])entryArr);
/*  990 */       for (ServerEntry sentry : entryArr) {
/*      */         
/*  992 */         if (getResponder().getPower() > 0 || !sentry.entryServer || Servers.localServer.testServer) {
/*      */           
/*  994 */           String kingdomname, pvp, kingdoms, desc = "";
/*  995 */           String colour = "";
/*  996 */           switch (sentry.id) {
/*      */             
/*      */             case 1:
/*  999 */               desc = " - This is the tutorial server.";
/* 1000 */               colour = this.purple;
/*      */               break;
/*      */             case 3:
/* 1003 */               desc = " - This is an old and large PvP server in the Freedom cluster. Custom kingdoms can be formed here.";
/* 1004 */               colour = this.orange;
/*      */               break;
/*      */             case 5:
/* 1007 */               desc = " - This is the oldest large PvE server in the Freedom cluster.";
/* 1008 */               colour = this.green;
/*      */               break;
/*      */             case 6:
/*      */             case 7:
/*      */             case 8:
/* 1013 */               desc = " - This is a standard sized, well developed PvE server in the Freedom cluster.";
/* 1014 */               colour = this.green;
/*      */               break;
/*      */             case 9:
/* 1017 */               desc = " - This is the Jenn-Kellon Home PvP server in the Epic cluster. Home servers have large bonuses against attackers.";
/* 1018 */               colour = this.orange;
/*      */               break;
/*      */             case 10:
/* 1021 */               desc = " - This is the Mol Rehan Home PvP server in the Epic cluster. Home servers have large bonuses against attackers.";
/* 1022 */               colour = this.orange;
/*      */               break;
/*      */             case 11:
/* 1025 */               desc = " - This is the Horde of The Summoned Home PvP server in the Epic cluster. Home servers have large bonuses against attackers.";
/* 1026 */               colour = this.orange;
/*      */               break;
/*      */             case 12:
/* 1029 */               desc = " - This is the central PvP server in the Epic cluster. This is where the kingdoms clash, and custom kingdoms are formed.";
/* 1030 */               colour = this.red;
/*      */               break;
/*      */             case 13:
/*      */             case 14:
/* 1034 */               desc = " - This is a standard sized, fairly well developed PvE server in the Freedom cluster.";
/* 1035 */               colour = this.green;
/*      */               break;
/*      */             case 15:
/* 1038 */               desc = " - The most recent Land Rush server. It is bigger than all the other servers together.";
/* 1039 */               colour = this.green;
/*      */               break;
/*      */             case 20:
/* 1042 */               desc = " - This is the Challenge server. Very quick skillgain, small and compact providing lots of action. Full loot PvP with highscore lists and prizes. Resets after a while.";
/* 1043 */               colour = this.cyan;
/*      */               break;
/*      */             default:
/* 1046 */               kingdomname = Kingdoms.getNameFor(sentry.KINGDOM);
/* 1047 */               pvp = " Pvp Kingdoms ";
/* 1048 */               kingdoms = " (" + kingdomname + "): ";
/* 1049 */               if (!sentry.PVPSERVER) {
/* 1050 */                 pvp = " Non-Pvp";
/* 1051 */               } else if (sentry.HOMESERVER) {
/* 1052 */                 pvp = " Pvp Home";
/*      */               } else {
/* 1054 */                 kingdoms = ": ";
/* 1055 */               }  desc = " - Test Server. " + pvp + kingdoms;
/* 1056 */               colour = this.cyan;
/*      */               break;
/*      */           } 
/* 1059 */           if (sentry.id != Servers.localServer.id) {
/*      */ 
/*      */ 
/*      */             
/* 1063 */             boolean full = sentry.isFull();
/*      */             
/* 1065 */             if (sentry.isAvailable(getResponder().getPower(), getResponder().isReallyPaying())) {
/*      */               
/* 1067 */               if (entryArr.length == 1) {
/*      */                 
/* 1069 */                 buf.append("harray{radio{group='sid';id='" + sentry.id + "';selected='true'}label{color='" + colour + "';text='" + sentry.name + desc + (full ? " (Full)" : "") + "'}}");
/*      */ 
/*      */                 
/* 1072 */                 buf.append("text{text=''}");
/* 1073 */                 buf.append("text{text='You will join the following kingdom:'}");
/* 1074 */                 addKingdoms(sentry, buf);
/*      */               } else {
/*      */                 
/* 1077 */                 buf.append("harray{radio{group='sid';id='" + sentry.id + "';selected='" + selected + "'}label{color='" + colour + "';text='" + sentry.name + desc + (full ? " (Full)" : "") + "'}}");
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1083 */               selected = false;
/*      */             }
/*      */             else {
/*      */               
/* 1087 */               String reason = "unavailable";
/* 1088 */               if (full && sentry.isConnected())
/* 1089 */                 reason = "full"; 
/* 1090 */               if (sentry.maintaining)
/* 1091 */                 reason = "maintenance"; 
/* 1092 */               buf.append("label{color=\"" + colour + "\";text=\"    " + sentry.name + desc + " Unavailable: " + reason + ".\"}");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
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
/* 1116 */     buf.append(createAnswerButton2());
/* 1117 */     getResponder().getCommunicator().sendBml(700, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void addVillages(ServerEntry entry, StringBuilder buf, byte selectedKingdom) {
/* 1122 */     Spawnpoint[] spawns = entry.getSpawns();
/* 1123 */     if (spawns != null && spawns.length > 0) {
/*      */       
/* 1125 */       buf.append("text{text=\"Also, please select a start village:\"}");
/*      */       
/* 1127 */       int numSelected = Server.rand.nextInt(spawns.length);
/* 1128 */       int curr = 0;
/* 1129 */       for (Spawnpoint spawn : spawns) {
/*      */         
/* 1131 */         if (selectedKingdom != 0 && spawn.kingdom == selectedKingdom)
/*      */         {
/* 1133 */           buf.append("radio{group=\"spawnpoint\";id=\"spawn" + spawn.number + "\"; text=\"" + spawn.name + " (" + spawn.description + ")\";selected=\"" + ((numSelected == curr++) ? 1 : 0) + "\"}");
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void addKingdoms(ServerEntry entry, StringBuilder buf) {
/* 1143 */     Set<Byte> kingdoms = entry.getExistingKingdoms();
/* 1144 */     if (entry.HOMESERVER) {
/*      */       
/* 1146 */       Kingdom kingd = Kingdoms.getKingdom(entry.KINGDOM);
/* 1147 */       if (kingd != null)
/*      */       {
/* 1149 */         buf.append("radio{group=\"kingdid\";id=\"" + entry.KINGDOM + "\"; text=\"" + kingd.getName() + "\";selected=\"" + '\001' + "\"}");
/*      */       }
/*      */       
/* 1152 */       buf.append("text{text=\"\"}");
/* 1153 */       addVillages(entry, buf, entry.KINGDOM);
/*      */     }
/* 1155 */     else if (entry.isChallengeServer()) {
/*      */       
/* 1157 */       Spawnpoint[] spawns = entry.getSpawns();
/* 1158 */       if (spawns != null && spawns.length > 0) {
/*      */ 
/*      */ 
/*      */         
/* 1162 */         int numSelected = Server.rand.nextInt(spawns.length);
/* 1163 */         int curr = 0;
/* 1164 */         for (Spawnpoint spawn : spawns) {
/*      */           
/* 1166 */           Kingdom kingd = Kingdoms.getKingdom(spawn.kingdom);
/*      */ 
/*      */ 
/*      */           
/* 1170 */           if (kingd != null && kingd.acceptsTransfers()) {
/*      */             
/* 1172 */             buf.append("radio{group=\"spawnpoint\";id=\"spawn" + spawn.number + "\"; text=\"" + spawn.name + " in " + kingd
/* 1173 */                 .getName() + " (" + spawn.description + ")\";selected=\"" + ((numSelected == curr) ? 1 : 0) + "\"}");
/*      */ 
/*      */             
/* 1176 */             curr++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1183 */       buf.append("text{text=\"\"}");
/*      */     }
/*      */     else {
/*      */       
/* 1187 */       boolean selected = true;
/* 1188 */       for (Byte k : kingdoms) {
/*      */         
/* 1190 */         Kingdom kingd = Kingdoms.getKingdom(k.byteValue());
/* 1191 */         if (kingd != null && kingd.acceptsTransfers()) {
/*      */           
/* 1193 */           buf.append("radio{group=\"kingdid\";id=\"" + k.byteValue() + "\"; text=\"" + kingd.getName() + " '" + kingd
/* 1194 */               .getFirstMotto() + " " + kingd.getSecondMotto() + "'\";selected=\"" + selected + "\"}");
/* 1195 */           selected = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\PortalQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */