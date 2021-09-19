/*      */ package com.wurmonline.server.intra;
/*      */ 
/*      */ import com.wurmonline.communication.SimpleConnectionListener;
/*      */ import com.wurmonline.communication.SocketConnection;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginHandler;
/*      */ import com.wurmonline.server.Message;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerMonitoring;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.WurmId;
/*      */ import com.wurmonline.server.bodys.WoundMetaData;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureDataStream;
/*      */ import com.wurmonline.server.creatures.CreaturePos;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.deities.Deities;
/*      */ import com.wurmonline.server.economy.Change;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.effects.EffectMetaData;
/*      */ import com.wurmonline.server.items.DbStrings;
/*      */ import com.wurmonline.server.items.FrozenItemDbStrings;
/*      */ import com.wurmonline.server.items.InscriptionData;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemData;
/*      */ import com.wurmonline.server.items.ItemDbStrings;
/*      */ import com.wurmonline.server.items.ItemFactory;
/*      */ import com.wurmonline.server.items.ItemMealData;
/*      */ import com.wurmonline.server.items.ItemMetaData;
/*      */ import com.wurmonline.server.items.ItemRequirement;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.Itempool;
/*      */ import com.wurmonline.server.items.Puppet;
/*      */ import com.wurmonline.server.items.RecipesByPlayer;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Achievement;
/*      */ import com.wurmonline.server.players.AchievementTemplate;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Awards;
/*      */ import com.wurmonline.server.players.Cultist;
/*      */ import com.wurmonline.server.players.EpicPlayerTransferMetaData;
/*      */ import com.wurmonline.server.players.MapAnnotation;
/*      */ import com.wurmonline.server.players.PermissionsByPlayer;
/*      */ import com.wurmonline.server.players.PermissionsHistories;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.PlayerMetaData;
/*      */ import com.wurmonline.server.questions.QuestionParser;
/*      */ import com.wurmonline.server.skills.Affinities;
/*      */ import com.wurmonline.server.skills.AffinitiesTimed;
/*      */ import com.wurmonline.server.skills.SkillMetaData;
/*      */ import com.wurmonline.server.spells.Cooldowns;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.server.spells.SpellEffectMetaData;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.Zone;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.BitSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
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
/*      */ public final class IntraServerConnection
/*      */   implements SimpleConnectionListener, MiscConstants, TimeConstants
/*      */ {
/*      */   private final SocketConnection conn;
/*  124 */   private static final Logger logger = Logger.getLogger(IntraServerConnection.class.getName());
/*      */   
/*      */   private ByteArrayOutputStream dataStream;
/*      */   
/*      */   private final ServerMonitoring wurmserver;
/*      */   
/*      */   private static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE WURMID=?";
/*      */   
/*      */   private static final String DELETE_ENEMIES = "DELETE FROM ENEMIES WHERE WURMID=?";
/*      */   
/*      */   private static final String DELETE_IGNORED = "DELETE FROM IGNORED WHERE WURMID=?";
/*      */   
/*      */   private static final String DELETE_TITLES = "DELETE FROM TITLES WHERE WURMID=?";
/*      */   
/*      */   private static final String DELETE_HISTORY_IP = "DELETE FROM PLAYERHISTORYIPS WHERE PLAYERID=?";
/*      */   
/*      */   private static final String DELETE_HISTORY_EMAIL = "DELETE FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=?";
/*      */   
/*      */   private static final int DISCONNECT_TICKS = 200;
/*      */   
/*  144 */   private static long draggedItem = -10L;
/*      */   
/*  146 */   private static final Set<String> moneyDetails = new HashSet<>();
/*      */   
/*  148 */   private static final Set<String> timeDetails = new HashSet<>();
/*      */   
/*  150 */   public static String lastItemName = "unknown";
/*      */   
/*  152 */   public static long lastItemId = -10L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   IntraServerConnection(SocketConnection aConn, ServerMonitoring aServer) {
/*  161 */     this.conn = aConn;
/*  162 */     this.wurmserver = aServer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reallyHandle(int num, ByteBuffer byteBuffer) {
/*  173 */     long check = System.currentTimeMillis();
/*  174 */     short cmd = (short)byteBuffer.get();
/*  175 */     if (logger.isLoggable(Level.FINER))
/*      */     {
/*  177 */       logger.finer("Received cmd " + cmd);
/*      */     }
/*  179 */     if (cmd == 1) {
/*      */       
/*  181 */       validate(byteBuffer);
/*      */     }
/*  183 */     else if (cmd == 13) {
/*      */ 
/*      */       
/*      */       try {
/*  187 */         sendPingAnswer();
/*      */       }
/*  189 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  194 */     else if (cmd == 9) {
/*      */       
/*  196 */       long wurmid = byteBuffer.getLong();
/*      */       
/*      */       try {
/*  199 */         String name = Players.getInstance().getNameFor(wurmid);
/*  200 */         PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*  201 */         pinf.load();
/*  202 */         sendPlayerVersion(pinf.version);
/*      */       }
/*  204 */       catch (NoSuchPlayerException nsp) {
/*      */ 
/*      */         
/*      */         try {
/*  208 */           sendPlayerVersion(0L);
/*      */         }
/*  210 */         catch (IOException iox) {
/*      */           
/*      */           try
/*      */           {
/*  214 */             sendCommandFailed();
/*      */           }
/*  216 */           catch (IOException iox2)
/*      */           {
/*  218 */             logger.log(Level.WARNING, "Failed to send command failed.");
/*      */           }
/*      */         
/*      */         } 
/*  222 */       } catch (IOException iox3) {
/*      */         
/*      */         try
/*      */         {
/*  226 */           sendCommandFailed();
/*      */         }
/*  228 */         catch (IOException iox2)
/*      */         {
/*  230 */           logger.log(Level.WARNING, "Failed to send command failed.");
/*      */         }
/*      */       
/*      */       } 
/*  234 */     } else if (cmd == 11) {
/*      */       
/*  236 */       long wid = byteBuffer.getLong();
/*      */       
/*      */       try {
/*  239 */         String name = Players.getInstance().getNameFor(wid);
/*  240 */         PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(name);
/*  241 */         pinf.load();
/*  242 */         sendPlayerPaymentExpire(pinf.getPaymentExpire());
/*      */       }
/*  244 */       catch (NoSuchPlayerException nsp) {
/*      */ 
/*      */         
/*      */         try {
/*  248 */           sendPlayerPaymentExpire(0L);
/*      */         }
/*  250 */         catch (IOException iox) {
/*      */           
/*      */           try
/*      */           {
/*  254 */             sendCommandFailed();
/*      */           }
/*  256 */           catch (IOException iox2)
/*      */           {
/*  258 */             logger.log(Level.WARNING, "Failed to send command failed.");
/*      */           }
/*      */         
/*      */         } 
/*  262 */       } catch (IOException iox3) {
/*      */         
/*      */         try
/*      */         {
/*  266 */           sendCommandFailed();
/*      */         }
/*  268 */         catch (IOException iox2)
/*      */         {
/*  270 */           logger.log(Level.WARNING, "Failed to send command failed.");
/*      */         }
/*      */       
/*      */       } 
/*  274 */     } else if (cmd == 6) {
/*      */       
/*  276 */       validateTransferRequest(byteBuffer);
/*      */     }
/*  278 */     else if (cmd == 3) {
/*      */       
/*  280 */       int posx = byteBuffer.getInt();
/*  281 */       int posy = byteBuffer.getInt();
/*  282 */       boolean surfaced = (byteBuffer.get() != 0);
/*  283 */       if (unpackPlayerData(posx, posy, surfaced)) {
/*      */ 
/*      */         
/*      */         try {
/*  287 */           sendCommandDone();
/*      */         }
/*  289 */         catch (IOException ex) {
/*      */ 
/*      */           
/*      */           try {
/*  293 */             logger.log(Level.WARNING, "Failed to receive user: " + ex.getMessage(), ex);
/*  294 */             sendCommandFailed();
/*      */           }
/*  296 */           catch (IOException ex2) {
/*      */             
/*  298 */             logger.log(Level.WARNING, "Failed to send command failed.");
/*      */           } 
/*  300 */           this.conn.ticksToDisconnect = 200;
/*      */         } 
/*      */       } else {
/*      */         
/*      */         try
/*      */         {
/*      */           
/*  307 */           sendCommandFailed();
/*  308 */           logger.log(Level.WARNING, "Failed to unpack data.");
/*  309 */           this.conn.ticksToDisconnect = 200;
/*      */         }
/*  311 */         catch (IOException ex2)
/*      */         {
/*  313 */           logger.log(Level.WARNING, "Failed to send command failed.");
/*      */         }
/*      */       
/*      */       } 
/*  317 */     } else if (cmd == 7) {
/*      */       
/*  319 */       if (readNextDataBlock(byteBuffer)) {
/*      */         
/*      */         try {
/*      */           
/*  323 */           sendDataReceived();
/*      */         }
/*  325 */         catch (IOException iox) {
/*      */           
/*      */           try
/*      */           {
/*  329 */             sendCommandFailed();
/*      */           }
/*  331 */           catch (IOException ex2)
/*      */           {
/*  333 */             logger.log(Level.WARNING, "Failed to send command failed.");
/*      */           }
/*      */         
/*      */         } 
/*      */       }
/*  338 */     } else if (cmd == 16) {
/*      */       
/*  340 */       this.conn.ticksToDisconnect = 200;
/*  341 */       long wurmid = byteBuffer.getLong();
/*  342 */       long currentMoney = byteBuffer.getLong();
/*  343 */       long moneyAdded = byteBuffer.getLong();
/*  344 */       int length = byteBuffer.getInt();
/*  345 */       byte[] det = new byte[length];
/*  346 */       byteBuffer.get(det);
/*  347 */       String detail = "unknown";
/*      */       
/*      */       try {
/*  350 */         detail = new String(det, "UTF-8");
/*  351 */         if (moneyDetails.contains(detail)) {
/*      */           
/*      */           try {
/*      */             
/*  355 */             sendCommandDone();
/*      */             
/*      */             return;
/*  358 */           } catch (IOException iOException) {}
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  363 */       catch (UnsupportedEncodingException ex) {
/*      */         
/*  365 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*  367 */       PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/*  368 */       if (info != null) {
/*      */ 
/*      */         
/*      */         try {
/*  372 */           info.load();
/*      */         }
/*  374 */         catch (IOException iox) {
/*      */ 
/*      */           
/*      */           try {
/*  378 */             logger.log(Level.WARNING, "Failed to load player info for " + wurmid + ": " + iox.getMessage(), iox);
/*  379 */             sendCommandFailed();
/*      */           }
/*  381 */           catch (IOException iOException) {}
/*      */ 
/*      */           
/*  384 */           this.conn.ticksToDisconnect = 200;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } else {
/*  390 */         logger.log(Level.WARNING, wurmid + ", failed to locate player info and set money to " + currentMoney + "!");
/*      */         
/*      */         try {
/*  393 */           sendCommandFailed();
/*      */         }
/*  395 */         catch (IOException iOException) {}
/*      */       } 
/*      */ 
/*      */       
/*  399 */       if (info != null && info.wurmId > 0L) {
/*      */         
/*  401 */         if (info.currentServer != Servers.localServer.id)
/*      */         {
/*  403 */           logger.warning("Received a CMD_SET_PLAYER_MONEY for player " + info.getName() + " (id: " + wurmid + ") but their currentserver (id: " + info
/*  404 */               .getCurrentServer() + ") is not this server (id: " + Servers.localServer.id + ")");
/*      */         }
/*      */ 
/*      */         
/*      */         try {
/*  409 */           info.setMoney(currentMoney);
/*  410 */           if (detail.contains("Premium")) {
/*      */             
/*  412 */             Shop kingsShop = Economy.getEconomy().getKingsShop();
/*  413 */             if (kingsShop != null)
/*  414 */               kingsShop.setMoney(kingsShop.getMoney() - moneyAdded); 
/*      */           } 
/*  416 */           new MoneyTransfer(info.getName(), wurmid, currentMoney, moneyAdded, detail, (byte)0, "");
/*      */           
/*  418 */           sendCommandDone();
/*  419 */           boolean referred = false;
/*  420 */           if (detail.startsWith("Referred by ")) {
/*      */             
/*  422 */             referred = true;
/*  423 */             info.addToSleep(3600);
/*      */           } 
/*  425 */           moneyDetails.add(detail);
/*      */ 
/*      */           
/*      */           try {
/*  429 */             Player p = Players.getInstance().getPlayer(wurmid);
/*  430 */             Change c = new Change(currentMoney);
/*  431 */             p.getCommunicator().sendNormalServerMessage("Your available money in the bank is now " + c
/*  432 */                 .getChangeString() + ".");
/*  433 */             if (referred)
/*      */             {
/*  435 */               String sleepString = "You also received an hour of sleep bonus which will increase your skill gain speed.";
/*  436 */               p.getCommunicator().sendSafeServerMessage("You also received an hour of sleep bonus which will increase your skill gain speed.");
/*      */             }
/*      */           
/*  439 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */         
/*      */         }
/*  443 */         catch (IOException iox) {
/*      */           
/*  445 */           logger.log(Level.WARNING, wurmid + ", failed to set money to " + currentMoney + ".", iox);
/*      */           
/*      */           try {
/*  448 */             sendCommandFailed();
/*      */           }
/*  450 */           catch (IOException iox2) {
/*      */             
/*  452 */             this.conn.disconnect();
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  458 */         logger.log(Level.WARNING, wurmid + ", failed to locate player info and set money to " + currentMoney + "!");
/*      */         
/*      */         try {
/*  461 */           sendCommandFailed();
/*      */         }
/*  463 */         catch (IOException iox2) {
/*      */           
/*  465 */           this.conn.disconnect();
/*      */         } 
/*      */       } 
/*  468 */       if (System.currentTimeMillis() - check > 1000L) {
/*  469 */         logger.log(Level.INFO, "Lag detected at CMD_SET_PLAYER_MONEY: " + 
/*  470 */             (int)((System.currentTimeMillis() - check) / 1000L));
/*      */       }
/*  472 */     } else if (cmd == 17) {
/*      */       
/*  474 */       this.conn.ticksToDisconnect = 200;
/*  475 */       long wurmid = byteBuffer.getLong();
/*  476 */       long currentExpire = byteBuffer.getLong();
/*  477 */       int days = byteBuffer.getInt();
/*  478 */       int months = byteBuffer.getInt();
/*  479 */       boolean dealItems = (byteBuffer.get() > 0);
/*  480 */       int length = byteBuffer.getInt();
/*  481 */       byte[] det = new byte[length];
/*  482 */       byteBuffer.get(det);
/*  483 */       String detail = "unknown";
/*      */       
/*      */       try {
/*  486 */         detail = new String(det, "UTF-8");
/*  487 */         if (timeDetails.contains(detail)) {
/*      */           
/*      */           try {
/*      */             
/*  491 */             sendCommandDone();
/*      */             
/*      */             return;
/*  494 */           } catch (IOException iOException) {}
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  500 */       catch (UnsupportedEncodingException ex) {
/*      */         
/*  502 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/*  504 */       PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmid);
/*  505 */       if (info != null)
/*      */         
/*      */         try {
/*      */           
/*  509 */           info.load();
/*      */         }
/*  511 */         catch (IOException iox) {
/*      */ 
/*      */           
/*      */           try {
/*  515 */             sendCommandFailed();
/*      */           
/*      */           }
/*  518 */           catch (IOException iox2) {
/*      */             
/*  520 */             this.conn.disconnect();
/*      */           } 
/*  522 */           this.conn.ticksToDisconnect = 200;
/*  523 */           if (System.currentTimeMillis() - check > 1000L) {
/*  524 */             logger.log(Level.INFO, "Lag detected at CMD_SET_PLAYER_PAYMENTEXPIRE IOEXCEPTION: " + 
/*      */ 
/*      */                 
/*  527 */                 (int)((System.currentTimeMillis() - check) / 1000L));
/*      */           }
/*      */           return;
/*  530 */         } catch (NullPointerException np) {
/*      */           
/*  532 */           logger.log(Level.WARNING, "No player with id=" + wurmid + " on this server.");
/*      */           
/*      */           try {
/*  535 */             sendCommandFailed();
/*      */           
/*      */           }
/*  538 */           catch (IOException iox2) {
/*      */             
/*  540 */             this.conn.disconnect();
/*      */           } 
/*  542 */           this.conn.ticksToDisconnect = 200;
/*  543 */           if (System.currentTimeMillis() - check > 1000L) {
/*  544 */             logger.log(Level.INFO, "Lag detected at CMD_SET_PLAYER_PAYMENTEXPIRE IOEXCEPTION: " + 
/*      */ 
/*      */                 
/*  547 */                 (int)((System.currentTimeMillis() - check) / 1000L));
/*      */           }
/*      */           return;
/*      */         }  
/*  551 */       if (info.wurmId > 0L) {
/*      */ 
/*      */ 
/*      */         
/*  555 */         if (info.currentServer != Servers.localServer.id)
/*      */         {
/*  557 */           logger.warning("Received a CMD_SET_PLAYER_PAYMENTEXPIRE for player " + info.getName() + " (id: " + wurmid + ") but their currentserver (id: " + info
/*  558 */               .getCurrentServer() + ") is not this server (id: " + Servers.localServer.id + ")");
/*      */         }
/*      */ 
/*      */         
/*      */         try {
/*  563 */           if (currentExpire > System.currentTimeMillis())
/*      */           {
/*  565 */             if (info.getPaymentExpire() <= 0L) {
/*  566 */               Server.addNewPlayer(info.getName());
/*      */             } else {
/*  568 */               Server.incrementOldPremiums(info.getName());
/*      */             }  } 
/*  570 */           info.setPaymentExpire(currentExpire);
/*  571 */           boolean referred = false;
/*  572 */           if (detail.startsWith("Referred by ")) {
/*      */             
/*  574 */             referred = true;
/*  575 */             info.addToSleep(3600);
/*      */           } 
/*  577 */           new TimeTransfer(info.getName(), wurmid, months, dealItems, days, detail);
/*      */           
/*  579 */           sendCommandDone();
/*      */           
/*  581 */           timeDetails.add(detail);
/*      */           
/*      */           try {
/*  584 */             Player p = Players.getInstance().getPlayer(wurmid);
/*      */ 
/*      */             
/*  587 */             String expireString = "You now have premier playing time until " + WurmCalendar.formatGmt(currentExpire) + ".";
/*  588 */             p.getCommunicator().sendSafeServerMessage(expireString);
/*  589 */             if (referred) {
/*      */               
/*  591 */               String sleepString = "You also received an hour of sleep bonus which will increase your skill gain speed.";
/*  592 */               p.getCommunicator().sendSafeServerMessage("You also received an hour of sleep bonus which will increase your skill gain speed.");
/*      */             } 
/*  594 */             if (dealItems) {
/*      */               try
/*      */               {
/*      */                 
/*  598 */                 Item inventory = p.getInventory();
/*  599 */                 for (int x = 0; x < months; x++) {
/*      */                   
/*  601 */                   Item i = ItemFactory.createItem(666, 99.0F, "");
/*  602 */                   inventory.insertItem(i, true);
/*      */                 } 
/*  604 */                 logger.log(Level.INFO, "Inserted " + months + " sleep powder in " + p.getName() + " inventory " + inventory
/*  605 */                     .getWurmId());
/*  606 */                 Message rmess = new Message(null, (byte)3, ":Event", "You have received " + months + " sleeping powders in your inventory.");
/*      */                 
/*  608 */                 rmess.setReceiver(p.getWurmId());
/*  609 */                 Server.getInstance().addMessage(rmess);
/*      */               }
/*  611 */               catch (Exception ex)
/*      */               {
/*  613 */                 logger.log(Level.INFO, ex.getMessage(), ex);
/*      */               }
/*      */             
/*      */             }
/*  617 */           } catch (NoSuchPlayerException exp) {
/*      */             
/*  619 */             if (dealItems) {
/*      */               try
/*      */               {
/*  622 */                 long inventoryId = DbCreatureStatus.getInventoryIdFor(info.wurmId);
/*  623 */                 for (int x = 0; x < months; x++) {
/*      */                   
/*  625 */                   Item i = ItemFactory.createItem(666, 99.0F, "");
/*  626 */                   i.setParentId(inventoryId, true);
/*  627 */                   i.setOwnerId(info.wurmId);
/*      */                 } 
/*  629 */                 logger.log(Level.INFO, "Inserted " + months + " sleep powder in offline " + info
/*  630 */                     .getName() + " inventory " + inventoryId);
/*      */ 
/*      */               
/*      */               }
/*  634 */               catch (Exception ex)
/*      */               {
/*  636 */                 logger.log(Level.INFO, ex.getMessage(), ex);
/*      */               }
/*      */             
/*      */             }
/*      */           } 
/*  641 */         } catch (IOException iox) {
/*      */           
/*      */           try
/*      */           {
/*  645 */             sendCommandFailed();
/*      */           }
/*  647 */           catch (IOException iox2)
/*      */           {
/*  649 */             this.conn.disconnect();
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  655 */         logger.log(Level.WARNING, wurmid + ", failed to locate player info and set expire time to " + currentExpire + "!");
/*      */ 
/*      */         
/*      */         try {
/*  659 */           sendCommandFailed();
/*      */         }
/*  661 */         catch (IOException iox2) {
/*      */           
/*  663 */           this.conn.disconnect();
/*      */         } 
/*      */       } 
/*      */       
/*  667 */       if (System.currentTimeMillis() - check > 1000L) {
/*  668 */         logger.log(Level.INFO, "Lag detected at CMD_SET_PLAYER_PAYMENTEXPIRE: " + 
/*  669 */             (int)((System.currentTimeMillis() - check) / 1000L));
/*      */       }
/*  671 */     } else if (cmd == 10) {
/*      */ 
/*      */       
/*      */       try {
/*  675 */         sendTimeSync();
/*      */       }
/*  677 */       catch (IOException ex2) {
/*      */         
/*  679 */         this.conn.ticksToDisconnect = 200;
/*      */       } 
/*  681 */       if (System.currentTimeMillis() - check > 1000L) {
/*  682 */         logger.log(Level.INFO, "Lag detected at CMD_GET_TIME: " + (int)((System.currentTimeMillis() - check) / 1000L));
/*      */       }
/*  684 */     } else if (cmd == 18) {
/*      */       
/*  686 */       if (changePassword(byteBuffer)) {
/*      */ 
/*      */         
/*      */         try {
/*  690 */           sendCommandDone();
/*      */         }
/*  692 */         catch (IOException iox) {
/*      */           
/*  694 */           this.conn.ticksToDisconnect = 200;
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  701 */           sendCommandFailed();
/*      */         }
/*  703 */         catch (IOException iox) {
/*      */           
/*  705 */           this.conn.ticksToDisconnect = 200;
/*      */         } 
/*      */       } 
/*  708 */       if (System.currentTimeMillis() - check > 1000L) {
/*  709 */         logger.log(Level.INFO, "Lag detected at CMD_SET_PLAYER_PASSWORD: " + 
/*  710 */             (int)((System.currentTimeMillis() - check) / 1000L));
/*      */       }
/*  712 */     } else if (cmd == 15) {
/*      */       
/*  714 */       logger.log(Level.INFO, "Received disconnect.");
/*  715 */       this.conn.disconnect();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean changePassword(ByteBuffer byteBuffer) {
/*  721 */     long playerId = byteBuffer.getLong();
/*      */     
/*  723 */     int length = byteBuffer.getInt();
/*  724 */     byte[] pw = new byte[length];
/*  725 */     byteBuffer.get(pw);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  730 */       String hashedPassword = new String(pw, "UTF-8");
/*  731 */       return setNewPassword(playerId, hashedPassword);
/*      */     }
/*  733 */     catch (Exception ex) {
/*      */       
/*  735 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final boolean setNewPassword(long playerId, String newHashedPassword) {
/*  741 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(playerId);
/*  742 */     if (info != null) {
/*      */ 
/*      */       
/*      */       try {
/*  746 */         info.load();
/*      */       }
/*  748 */       catch (Exception eex) {
/*      */         
/*  750 */         logger.log(Level.WARNING, "Failed to load info for wurmid " + playerId + ". Password unchanged." + eex
/*  751 */             .getMessage(), eex);
/*      */       } 
/*  753 */       if (info.wurmId <= 0L) {
/*      */         
/*  755 */         logger.log(Level.WARNING, "Failed to load info for wurmid " + playerId + ". No info available. Password unchanged.");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  760 */         info.setPassword(newHashedPassword);
/*      */         
/*      */         try {
/*  763 */           Player p = Players.getInstance().getPlayer(playerId);
/*  764 */           p.getCommunicator().sendAlertServerMessage("Your password has been updated. Use the new one to connect next time.");
/*      */ 
/*      */         
/*      */         }
/*  768 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  774 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean readNextDataBlock(ByteBuffer byteBuffer) {
/*  779 */     if (this.dataStream == null)
/*  780 */       this.dataStream = new ByteArrayOutputStream(); 
/*  781 */     int length = byteBuffer.getInt();
/*      */     
/*  783 */     byte[] toput = new byte[length];
/*  784 */     byteBuffer.get(toput);
/*  785 */     this.dataStream.write(toput, 0, length);
/*  786 */     return (byteBuffer.get() == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deletePlayer(long id) throws IOException {
/*  791 */     CreaturePos.delete(id);
/*  792 */     Connection dbcon = null;
/*  793 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  796 */       dbcon = DbConnector.getPlayerDbCon();
/*  797 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  799 */         logger.finest("Deleting Player ID: " + id);
/*      */       }
/*  801 */       ps = dbcon.prepareStatement("DELETE FROM PLAYERS WHERE WURMID=?");
/*  802 */       ps.setLong(1, id);
/*  803 */       ps.executeUpdate();
/*  804 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  805 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  807 */         logger.finest("Deleting Skills for Player ID: " + id);
/*      */       }
/*  809 */       ps = dbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=?");
/*  810 */       ps.setLong(1, id);
/*  811 */       ps.executeUpdate();
/*  812 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  813 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  815 */         logger.finest("Deleting Wounds for Player ID: " + id);
/*      */       }
/*  817 */       ps = dbcon.prepareStatement("DELETE FROM WOUNDS WHERE OWNER=?");
/*  818 */       ps.setLong(1, id);
/*  819 */       ps.executeUpdate();
/*  820 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  821 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  823 */         logger.finest("Deleting Friends for Player ID: " + id);
/*      */       }
/*  825 */       ps = dbcon.prepareStatement("DELETE FROM FRIENDS WHERE WURMID=?");
/*  826 */       ps.setLong(1, id);
/*  827 */       ps.executeUpdate();
/*  828 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*  830 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  832 */         logger.finest("Deleting Enemies for Player ID: " + id);
/*      */       }
/*  834 */       ps = dbcon.prepareStatement("DELETE FROM ENEMIES WHERE WURMID=?");
/*  835 */       ps.setLong(1, id);
/*  836 */       ps.executeUpdate();
/*  837 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*  839 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  841 */         logger.finest("Deleting Ignored for Player ID: " + id);
/*      */       }
/*  843 */       ps = dbcon.prepareStatement("DELETE FROM IGNORED WHERE WURMID=?");
/*  844 */       ps.setLong(1, id);
/*  845 */       ps.executeUpdate();
/*  846 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*  848 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  850 */         logger.finest("Deleting Titles for Player ID: " + id);
/*      */       }
/*  852 */       ps = dbcon.prepareStatement("DELETE FROM TITLES WHERE WURMID=?");
/*  853 */       ps.setLong(1, id);
/*  854 */       ps.executeUpdate();
/*  855 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*  857 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  859 */         logger.finest("Deleting IP History for Player ID: " + id);
/*      */       }
/*  861 */       ps = dbcon.prepareStatement("DELETE FROM PLAYERHISTORYIPS WHERE PLAYERID=?");
/*  862 */       ps.setLong(1, id);
/*  863 */       ps.executeUpdate();
/*  864 */       DbUtilities.closeDatabaseObjects(ps, null);
/*      */       
/*  866 */       if (logger.isLoggable(Level.FINEST))
/*      */       {
/*  868 */         logger.finest("Deleting Email History for Player ID: " + id);
/*      */       }
/*  870 */       ps = dbcon.prepareStatement("DELETE FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=?");
/*  871 */       ps.setLong(1, id);
/*  872 */       ps.executeUpdate();
/*  873 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  874 */       SpellEffect.deleteEffectsForPlayer(id);
/*  875 */       RecipesByPlayer.deleteRecipesForPlayer(id);
/*  876 */       AffinitiesTimed.deleteTimedAffinitiesForPlayer(id);
/*      */     }
/*  878 */     catch (SQLException sqex) {
/*      */       
/*  880 */       throw new IOException("Problem deleting playerid: " + id + " due to " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  884 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  885 */       DbConnector.returnConnection(dbcon);
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
/*      */   private static final void deletePlayer(String name, long id) throws IOException {
/*  899 */     Connection dbcon = null;
/*  900 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  903 */       dbcon = DbConnector.getPlayerDbCon();
/*      */       
/*  905 */       if (id > -10L)
/*      */       {
/*  907 */         ps = dbcon.prepareStatement("DELETE FROM PLAYERS WHERE NAME=?");
/*  908 */         ps.setString(1, name);
/*  909 */         ps.executeUpdate();
/*  910 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  912 */         ps = dbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=?");
/*  913 */         ps.setLong(1, id);
/*  914 */         ps.executeUpdate();
/*  915 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  917 */         ps = dbcon.prepareStatement("DELETE FROM WOUNDS WHERE OWNER=?");
/*  918 */         ps.setLong(1, id);
/*  919 */         ps.executeUpdate();
/*  920 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  922 */         ps = dbcon.prepareStatement("DELETE FROM FRIENDS WHERE WURMID=?");
/*  923 */         ps.setLong(1, id);
/*  924 */         ps.executeUpdate();
/*  925 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  927 */         ps = dbcon.prepareStatement("DELETE FROM ENEMIES WHERE WURMID=?");
/*  928 */         ps.setLong(1, id);
/*  929 */         ps.executeUpdate();
/*  930 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  932 */         ps = dbcon.prepareStatement("DELETE FROM IGNORED WHERE WURMID=?");
/*  933 */         ps.setLong(1, id);
/*  934 */         ps.executeUpdate();
/*  935 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  937 */         ps = dbcon.prepareStatement("DELETE FROM TITLES WHERE WURMID=?");
/*  938 */         ps.setLong(1, id);
/*  939 */         ps.executeUpdate();
/*  940 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  942 */         ps = dbcon.prepareStatement("DELETE FROM PLAYERHISTORYIPS WHERE PLAYERID=?");
/*  943 */         ps.setLong(1, id);
/*  944 */         ps.executeUpdate();
/*  945 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  947 */         ps = dbcon.prepareStatement("DELETE FROM PLAYEREHISTORYEMAIL WHERE PLAYERID=?");
/*  948 */         ps.setLong(1, id);
/*  949 */         ps.executeUpdate();
/*  950 */         DbUtilities.closeDatabaseObjects(ps, null);
/*      */         
/*  952 */         SpellEffect.deleteEffectsForPlayer(id);
/*      */       }
/*      */     
/*  955 */     } catch (SQLException sqex) {
/*      */       
/*  957 */       throw new IOException(name + " " + sqex.getMessage(), sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  961 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  962 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void deleteItem(long id, boolean frozen) throws IOException {
/*  968 */     if (WurmId.getType(id) != 19) {
/*      */       
/*  970 */       Connection dbcon = null;
/*  971 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/*  974 */         dbcon = DbConnector.getItemDbCon();
/*  975 */         DbStrings dbstrings = Item.getDbStringsByWurmId(id);
/*  976 */         if (logger.isLoggable(Level.FINEST))
/*      */         {
/*  978 */           logger.finest("Deleting item: " + id);
/*      */         }
/*      */         
/*  981 */         ps = dbcon.prepareStatement(dbstrings.deleteTransferedItem());
/*      */         
/*  983 */         ps.setLong(1, id);
/*  984 */         int rows = ps.executeUpdate();
/*  985 */         DbUtilities.closeDatabaseObjects(ps, null);
/*  986 */         if (dbstrings == ItemDbStrings.getInstance()) {
/*      */ 
/*      */           
/*  989 */           FrozenItemDbStrings frozenItemDbStrings = FrozenItemDbStrings.getInstance();
/*  990 */           ps = dbcon.prepareStatement(frozenItemDbStrings.deleteTransferedItem());
/*      */           
/*  992 */           ps.setLong(1, id);
/*  993 */           if (rows == 0) {
/*  994 */             rows = ps.executeUpdate();
/*      */           } else {
/*  996 */             ps.executeUpdate();
/*  997 */           }  DbUtilities.closeDatabaseObjects(ps, null);
/*      */         } 
/*  999 */         if (rows > 0)
/*      */         {
/* 1001 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 1003 */             logger.finest("Deleting effects for item: " + id);
/*      */           }
/* 1005 */           ps = dbcon.prepareStatement("DELETE FROM EFFECTS WHERE OWNER=?");
/* 1006 */           ps.setLong(1, id);
/* 1007 */           ps.executeUpdate();
/* 1008 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1009 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 1011 */             logger.finest("Deleting itemdata for item: " + id);
/*      */           }
/* 1013 */           ps = dbcon.prepareStatement("DELETE FROM ITEMDATA WHERE WURMID=?");
/* 1014 */           ps.setLong(1, id);
/* 1015 */           ps.executeUpdate();
/* 1016 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */           
/* 1018 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 1020 */             logger.finest("Deleting inscription data for item: " + id);
/*      */           }
/* 1022 */           ps = dbcon.prepareStatement("DELETE FROM INSCRIPTIONS WHERE WURMID=?");
/* 1023 */           ps.setLong(1, id);
/* 1024 */           ps.executeUpdate();
/* 1025 */           DbUtilities.closeDatabaseObjects(ps, null);
/*      */           
/* 1027 */           if (logger.isLoggable(Level.FINEST))
/*      */           {
/* 1029 */             logger.finest("Deleting locks for item: " + id);
/*      */           }
/* 1031 */           ps = dbcon.prepareStatement("DELETE FROM LOCKS WHERE WURMID=?");
/* 1032 */           ps.setLong(1, id);
/* 1033 */           ps.executeUpdate();
/* 1034 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 1035 */           ItemSpellEffects spefs = ItemSpellEffects.getSpellEffects(id);
/* 1036 */           if (spefs != null)
/* 1037 */             spefs.clear(); 
/* 1038 */           SpellEffect.deleteEffectsForItem(id);
/*      */         }
/*      */       
/* 1041 */       } catch (SQLException sqex) {
/*      */         
/* 1043 */         if (Servers.localServer.LOGINSERVER) {
/* 1044 */           logger.log(Level.WARNING, "ITEMDELETE Failed to delete item " + id + " " + sqex.getMessage(), sqex);
/*      */         } else {
/* 1046 */           throw new IOException(id + " " + sqex.getMessage(), sqex);
/*      */         } 
/*      */       } finally {
/*      */         
/* 1050 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1051 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean unpackPlayerData(int posx, int posy, boolean surfaced) {
/*      */     try {
/* 1060 */       this.dataStream.flush();
/* 1061 */       this.dataStream.close();
/* 1062 */       byte[] bytes = this.dataStream.toByteArray();
/* 1063 */       return (savePlayerToDisk(bytes, posx, posy, surfaced, false) > 0L);
/*      */     }
/* 1065 */     catch (IOException ex) {
/*      */       
/* 1067 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/* 1068 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void readNullCultist(DataInputStream dis, String name, long wurmId) {
/*      */     try {
/* 1076 */       Cultist cultist = Cultist.getCultist(wurmId);
/* 1077 */       if (cultist != null)
/*      */       {
/* 1079 */         cultist.deleteCultist();
/*      */       }
/*      */     }
/* 1082 */     catch (IOException iox) {
/*      */       
/* 1084 */       logger.log(Level.WARNING, "Failed to read cultist for " + name + " " + wurmId);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void readCultist(DataInputStream dis, String name, long wurmId) {
/*      */     try {
/* 1092 */       byte clevel = dis.readByte();
/* 1093 */       byte cpath = dis.readByte();
/* 1094 */       long lastMeditated = dis.readLong();
/* 1095 */       long lastReceivedLevel = dis.readLong();
/* 1096 */       long lastAppointedLevel = dis.readLong();
/* 1097 */       long cd1 = dis.readLong();
/* 1098 */       long cd2 = dis.readLong();
/* 1099 */       long cd3 = dis.readLong();
/* 1100 */       long cd4 = dis.readLong();
/* 1101 */       long cd5 = dis.readLong();
/* 1102 */       long cd6 = dis.readLong();
/* 1103 */       long cd7 = dis.readLong();
/* 1104 */       byte skillgainCount = dis.readByte();
/* 1105 */       Cultist cultist = Cultist.getCultist(wurmId);
/*      */ 
/*      */ 
/*      */       
/* 1109 */       if (cultist == null)
/*      */       {
/*      */         
/* 1112 */         Cultist c = new Cultist(wurmId, lastMeditated, lastReceivedLevel, lastAppointedLevel, clevel, cpath, cd1, cd2, cd3, cd4, cd5, cd6, cd7);
/*      */ 
/*      */         
/*      */         try {
/* 1116 */           c.saveCultist(true);
/*      */         }
/* 1118 */         catch (IOException iox) {
/*      */           
/* 1120 */           logger.log(Level.WARNING, "Failed to save cultist " + name + " level=" + clevel + " path=" + cpath + " " + iox
/* 1121 */               .getMessage(), iox);
/*      */         } 
/* 1123 */         c.setSkillgainCount(skillgainCount);
/*      */       }
/*      */       else
/*      */       {
/* 1127 */         cultist.deleteCultist();
/*      */         
/* 1129 */         Cultist c = new Cultist(wurmId, lastMeditated, lastReceivedLevel, lastAppointedLevel, clevel, cpath, cd1, cd2, cd3, cd4, cd5, cd6, cd7);
/*      */ 
/*      */         
/*      */         try {
/* 1133 */           c.saveCultist(true);
/*      */         }
/* 1135 */         catch (IOException iox) {
/*      */           
/* 1137 */           logger.log(Level.WARNING, "Failed to save cultist " + name + " level=" + clevel + " path=" + cpath + " " + iox
/* 1138 */               .getMessage(), iox);
/*      */         } 
/* 1140 */         c.setSkillgainCount(skillgainCount);
/*      */       }
/*      */     
/* 1143 */     } catch (IOException iox) {
/*      */       
/* 1145 */       logger.log(Level.WARNING, "Failed to read cultist for " + name + " " + wurmId);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final byte calculateBloodFromKingdom(byte kingdom) {
/* 1151 */     if (kingdom == 3)
/* 1152 */       return 1; 
/* 1153 */     if (kingdom == 2)
/* 1154 */       return 8; 
/* 1155 */     if (kingdom == 1)
/* 1156 */       return 4; 
/* 1157 */     if (kingdom == 4)
/* 1158 */       return 2; 
/* 1159 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static long savePlayerEpicTransfer(DataInputStream dis) {
/*      */     try {
/* 1166 */       logger.log(Level.INFO, "Epic transfer");
/*      */       
/* 1168 */       long wurmId = dis.readLong();
/*      */       
/* 1170 */       String name = dis.readUTF();
/* 1171 */       String password = dis.readUTF();
/* 1172 */       String session = dis.readUTF();
/* 1173 */       String emailAddress = dis.readUTF();
/* 1174 */       long sessionExpiration = dis.readLong();
/*      */       
/* 1176 */       byte power = dis.readByte();
/*      */       
/* 1178 */       long money = dis.readLong();
/*      */       
/* 1180 */       long paymentExpire = dis.readLong();
/* 1181 */       int numignored = dis.readInt();
/* 1182 */       long[] ignored = new long[numignored];
/* 1183 */       for (int ni = 0; ni < numignored; ni++)
/* 1184 */         ignored[ni] = dis.readLong(); 
/* 1185 */       if (numignored == 0)
/* 1186 */         ignored = EMPTY_LONG_PRIMITIVE_ARRAY; 
/* 1187 */       int numfriends = dis.readInt();
/* 1188 */       long[] friends = new long[numfriends];
/* 1189 */       byte[] friendCats = new byte[numfriends];
/* 1190 */       for (int nf = 0; nf < numfriends; nf++) {
/*      */         
/* 1192 */         friends[nf] = dis.readLong();
/* 1193 */         friendCats[nf] = dis.readByte();
/*      */       } 
/* 1195 */       if (numfriends == 0) {
/*      */         
/* 1197 */         friends = EMPTY_LONG_PRIMITIVE_ARRAY;
/* 1198 */         friendCats = EMPTY_BYTE_PRIMITIVE_ARRAY;
/*      */       } 
/*      */       
/* 1201 */       long playingTime = dis.readLong();
/*      */       
/* 1203 */       long creationDate = dis.readLong();
/* 1204 */       long lastwarned = dis.readLong();
/* 1205 */       byte kingdom = dis.readByte();
/* 1206 */       boolean banned = dis.readBoolean();
/* 1207 */       long banexpiry = dis.readLong();
/* 1208 */       String banreason = dis.readUTF();
/* 1209 */       boolean mute = dis.readBoolean();
/* 1210 */       short muteTimes = dis.readShort();
/* 1211 */       long muteexpiry = dis.readLong();
/* 1212 */       String mutereason = dis.readUTF();
/* 1213 */       boolean maymute = dis.readBoolean();
/* 1214 */       boolean overRideShop = dis.readBoolean();
/* 1215 */       boolean reimbursed = dis.readBoolean();
/* 1216 */       int warnings = dis.readInt();
/* 1217 */       boolean mayHearDevtalk = dis.readBoolean();
/* 1218 */       String ipaddress = dis.readUTF();
/* 1219 */       long version = dis.readLong();
/* 1220 */       long referrer = dis.readLong();
/* 1221 */       String pwQuestion = dis.readUTF();
/* 1222 */       String pwAnswer = dis.readUTF();
/*      */       
/* 1224 */       boolean logging = dis.readBoolean();
/* 1225 */       boolean seesCAWin = dis.readBoolean();
/* 1226 */       boolean isCA = dis.readBoolean();
/* 1227 */       boolean mayAppointCA = dis.readBoolean();
/* 1228 */       long face = dis.readLong();
/* 1229 */       byte blood = dis.readByte();
/* 1230 */       long flags = dis.readLong();
/* 1231 */       long flags2 = dis.readLong();
/* 1232 */       byte chaosKingdom = dis.readByte();
/*      */       
/* 1234 */       byte undeadType = dis.readByte();
/* 1235 */       int undeadKills = dis.readInt();
/* 1236 */       int undeadPKills = dis.readInt();
/* 1237 */       int undeadPSecs = dis.readInt();
/* 1238 */       long lastResetEarningsCounter = dis.readLong();
/* 1239 */       long moneyEarnedBySellingLastHour = dis.readLong();
/* 1240 */       long moneyEarnedBySellingEver = dis.readLong();
/*      */       
/* 1242 */       int daysPrem = 0;
/* 1243 */       long lastTicked = 0L;
/* 1244 */       int monthsPaidEver = 0;
/* 1245 */       int monthsPaidInARow = 0;
/* 1246 */       int monthsPaidSinceReset = 0;
/* 1247 */       int silverPaidEver = 0;
/* 1248 */       int currentLoyalty = 0;
/* 1249 */       int totalLoyalty = 0;
/* 1250 */       boolean awards = false;
/*      */       
/* 1252 */       if (dis.readBoolean()) {
/*      */         
/* 1254 */         awards = true;
/* 1255 */         daysPrem = dis.readInt();
/* 1256 */         lastTicked = dis.readLong();
/* 1257 */         monthsPaidEver = dis.readInt();
/* 1258 */         monthsPaidInARow = dis.readInt();
/* 1259 */         monthsPaidSinceReset = dis.readInt();
/* 1260 */         silverPaidEver = dis.readInt();
/* 1261 */         currentLoyalty = dis.readInt();
/* 1262 */         totalLoyalty = dis.readInt();
/*      */       } 
/*      */       
/* 1265 */       byte sex = dis.readByte();
/* 1266 */       int epicServerId = dis.readInt();
/* 1267 */       byte epicServerKingdom = dis.readByte();
/*      */       
/* 1269 */       int numskills = dis.readInt();
/* 1270 */       for (int s = 0; s < numskills; s++) {
/*      */         
/* 1272 */         long skillId = dis.readLong();
/* 1273 */         int skillNumber = dis.readInt();
/* 1274 */         double skillValue = dis.readDouble();
/* 1275 */         double skillMinimum = dis.readDouble();
/* 1276 */         long skillLastUsed = dis.readLong();
/* 1277 */         if (Servers.isThisAnEpicServer()) {
/*      */ 
/*      */           
/* 1280 */           SkillMetaData sk = SkillMetaData.copyToEpicSkill(skillId, wurmId, skillNumber, skillValue, skillMinimum, skillLastUsed);
/*      */ 
/*      */           
/* 1283 */           SkillMetaData.deleteSkill(wurmId, skillNumber);
/* 1284 */           sk.save();
/*      */         } 
/*      */       } 
/*      */       
/* 1288 */       unpackAchievements(wurmId, dis);
/* 1289 */       RecipesByPlayer.unPackRecipes(dis, wurmId);
/*      */       
/* 1291 */       PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/*      */       
/* 1293 */       if (pinf == null) {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1298 */           LoginHandler.createPlayer(name, password, pwQuestion, pwAnswer, emailAddress, kingdom, power, face, sex, false, false, wurmId);
/*      */         
/*      */         }
/* 1301 */         catch (Exception ex) {
/*      */           
/* 1303 */           logger.log(Level.WARNING, "Creation exception " + ex.getMessage(), ex);
/* 1304 */           return -1L;
/*      */         } 
/* 1306 */         pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/* 1307 */         if (!Servers.localServer.EPIC) {
/* 1308 */           pinf.lastChangedKindom = 0L;
/*      */         }
/* 1310 */       } else if (Servers.isThisAChaosServer()) {
/*      */         
/* 1312 */         chaosKingdom = pinf.getChaosKingdom();
/* 1313 */         Village cv = Villages.getVillageForCreature(wurmId);
/* 1314 */         if (cv != null) {
/* 1315 */           kingdom = cv.kingdom;
/*      */         }
/*      */         
/* 1318 */         if (blood == 0) {
/* 1319 */           blood = calculateBloodFromKingdom(chaosKingdom);
/*      */         }
/*      */       } 
/* 1322 */       if (pinf != null) {
/*      */         
/* 1324 */         if (awards) {
/*      */           
/* 1326 */           pinf.awards = Awards.getAwards(pinf.wurmId);
/* 1327 */           if (pinf.awards != null) {
/*      */             
/* 1329 */             pinf.awards = new Awards(wurmId, daysPrem, monthsPaidEver, monthsPaidInARow, monthsPaidSinceReset, silverPaidEver, lastTicked, currentLoyalty, totalLoyalty, false);
/*      */             
/* 1331 */             pinf.awards.update();
/*      */           } else {
/*      */             
/* 1334 */             pinf.awards = new Awards(wurmId, daysPrem, monthsPaidEver, monthsPaidInARow, monthsPaidSinceReset, silverPaidEver, lastTicked, currentLoyalty, totalLoyalty, true);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1339 */         unpackPMList(pinf, dis);
/*      */       } 
/*      */       
/* 1342 */       if (Servers.isThisLoginServer())
/*      */       {
/* 1344 */         if (pinf != null) {
/*      */           
/* 1346 */           paymentExpire = pinf.getPaymentExpire();
/* 1347 */           overRideShop = pinf.overRideShop;
/* 1348 */           if (pinf.emailAddress.length() > 0)
/* 1349 */             emailAddress = pinf.emailAddress; 
/* 1350 */           password = pinf.getPassword();
/* 1351 */           if (money != pinf.money) {
/* 1352 */             logger.log(Level.INFO, "Setting money for " + pinf.getName() + " to " + pinf.money + " instead of " + money);
/*      */           }
/* 1354 */           money = pinf.money;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1359 */       if (blood == 0 || (Servers.localServer.EPIC && blood == 2))
/*      */       {
/* 1361 */         blood = calculateBloodFromKingdom(kingdom); } 
/* 1362 */       EpicPlayerTransferMetaData pmd = new EpicPlayerTransferMetaData(wurmId, name, password, session, sessionExpiration, power, lastwarned, playingTime, kingdom, banned, banexpiry, banreason, reimbursed, warnings, mayHearDevtalk, paymentExpire, ignored, friends, friendCats, ipaddress, mute, sex, version, money, face, seesCAWin, logging, isCA, mayAppointCA, referrer, pwQuestion, pwAnswer, overRideShop, muteTimes, muteexpiry, mutereason, maymute, emailAddress, creationDate, epicServerId, epicServerKingdom, chaosKingdom, blood, flags, flags2, undeadType, undeadKills, undeadPKills, undeadPSecs, moneyEarnedBySellingEver, daysPrem, lastTicked, currentLoyalty, totalLoyalty, monthsPaidEver, monthsPaidInARow, monthsPaidSinceReset, silverPaidEver, awards);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1370 */       pmd.save();
/* 1371 */       if (pinf != null) {
/*      */         
/* 1373 */         boolean setPremFlag = pinf.isFlagSet(8);
/* 1374 */         pinf.setMoneyEarnedBySellingLastHour(moneyEarnedBySellingLastHour);
/* 1375 */         pinf.setLastResetEarningsCounter(lastResetEarningsCounter);
/* 1376 */         if (!password.equals(pinf.getPassword()))
/*      */         {
/* 1378 */           logger.log(Level.WARNING, name + " after transfer but before loading: password now is " + pinf
/* 1379 */               .getPassword() + ". Sent " + password);
/*      */         }
/*      */         
/* 1382 */         pinf.loaded = false;
/*      */         
/*      */         try {
/* 1385 */           pinf.load();
/* 1386 */           boolean updateFlags = false;
/* 1387 */           if (pinf.flags != flags) {
/*      */             
/* 1389 */             pinf.flags = flags;
/* 1390 */             pinf.setFlagBits(pinf.flags);
/* 1391 */             if (setPremFlag)
/* 1392 */               pinf.setFlag(8, true); 
/* 1393 */             updateFlags = true;
/*      */           } 
/* 1395 */           if (pinf.flags2 != flags2) {
/*      */             
/* 1397 */             pinf.flags2 = flags2;
/* 1398 */             pinf.setFlag2Bits(pinf.flags2);
/* 1399 */             updateFlags = true;
/*      */           } 
/* 1401 */           if (updateFlags) {
/* 1402 */             pinf.forceFlagsUpdate();
/*      */           }
/* 1404 */           if (!password.equals(pinf.getPassword()))
/*      */           {
/* 1406 */             logger.log(Level.WARNING, name + " after transfer: password now is " + pinf.getPassword() + "  Sent " + password);
/*      */           
/*      */           }
/*      */         }
/* 1410 */         catch (IOException iox) {
/*      */           
/* 1412 */           logger.log(Level.WARNING, iox.getMessage());
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1417 */       pinf.loaded = false;
/* 1418 */       pinf.load();
/* 1419 */       pinf.lastUsedEpicPortal = System.currentTimeMillis();
/* 1420 */       return wurmId;
/*      */     }
/* 1422 */     catch (IOException ex) {
/*      */       
/* 1424 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/* 1425 */       return -1L;
/*      */     }
/*      */     finally {
/*      */       
/* 1429 */       if (dis != null) {
/*      */         
/*      */         try {
/*      */           
/* 1433 */           dis.close();
/*      */         }
/* 1435 */         catch (IOException iOException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void unpackAchievements(long wurmId, DataInputStream dis) throws IOException {
/* 1445 */     int templateNums = dis.readInt();
/* 1446 */     for (int x = 0; x < templateNums; x++) {
/*      */       
/* 1448 */       int number = dis.readInt();
/* 1449 */       String tname = dis.readUTF();
/* 1450 */       String desc = dis.readUTF();
/* 1451 */       String creator = dis.readUTF();
/* 1452 */       AchievementTemplate t = Achievement.getTemplate(number);
/* 1453 */       if (t == null)
/*      */       {
/*      */         
/* 1456 */         new AchievementTemplate(number, tname, false, 1, desc, creator, false, false);
/*      */       }
/*      */     } 
/* 1459 */     int nums = dis.readInt();
/* 1460 */     Achievements.deleteAllAchievements(wurmId);
/* 1461 */     for (int i = 0; i < nums; i++) {
/*      */       
/* 1463 */       int achievement = dis.readInt();
/* 1464 */       int counter = dis.readInt();
/* 1465 */       long date = dis.readLong();
/* 1466 */       Timestamp ts = new Timestamp(date);
/* 1467 */       (new Achievement(achievement, ts, wurmId, counter, -1)).create(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void unpackPMList(PlayerInfo pinf, DataInputStream dis) throws IOException {
/* 1473 */     int theCount = dis.readInt();
/* 1474 */     for (int x = 0; x < theCount; x++) {
/*      */       
/* 1476 */       String targetName = dis.readUTF();
/* 1477 */       long targetId = dis.readLong();
/* 1478 */       pinf.addPMTarget(targetName, targetId);
/*      */     } 
/*      */     
/* 1481 */     long sessionFlags = dis.readLong();
/* 1482 */     pinf.setSessionFlags(sessionFlags);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void unpackPrivateMapAnnotations(long playerID, DataInputStream dis) throws IOException {
/* 1487 */     MapAnnotation.deletePrivateAnnotationsForOwner(playerID);
/* 1488 */     boolean containsAnnotations = dis.readBoolean();
/* 1489 */     if (containsAnnotations) {
/*      */       
/* 1491 */       int count = dis.readInt();
/* 1492 */       for (int i = 0; i < count; i++) {
/*      */         
/* 1494 */         long id = dis.readLong();
/* 1495 */         byte type = dis.readByte();
/* 1496 */         String name = dis.readUTF();
/* 1497 */         String server = dis.readUTF();
/* 1498 */         long position = dis.readLong();
/* 1499 */         long ownerId = dis.readLong();
/* 1500 */         byte icon = dis.readByte();
/*      */         
/* 1502 */         MapAnnotation.createNew(id, name, type, position, ownerId, server, icon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean saving = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public static long savePlayerToDisk(byte[] bytes, int posx, int posy, boolean surfaced, boolean newPlayer) {
/* 1515 */     if (saving)
/* 1516 */       return -10L; 
/* 1517 */     saving = true;
/* 1518 */     DataInputStream dis = null;
/*      */     
/*      */     try {
/* 1521 */       dis = new DataInputStream(new ByteArrayInputStream(bytes));
/*      */       
/* 1523 */       if (dis.readBoolean())
/*      */       {
/* 1525 */         return savePlayerEpicTransfer(dis);
/*      */       }
/* 1527 */       long wurmId = dis.readLong();
/*      */       
/*      */       try {
/* 1530 */         Player p = Players.getInstance().getPlayer(wurmId);
/* 1531 */         Players.getInstance().logoutPlayer(p);
/*      */       }
/* 1533 */       catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */       
/*      */       }
/* 1537 */       catch (Exception ex) {
/*      */         
/* 1539 */         logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */       } 
/* 1541 */       PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(wurmId);
/* 1542 */       if (info != null) {
/* 1543 */         info.lastLogout = System.currentTimeMillis();
/*      */       }
/*      */       
/* 1546 */       byte oldChaosKingdom = 0;
/* 1547 */       boolean setPremFlag = false;
/* 1548 */       if (info != null) {
/*      */         
/* 1550 */         oldChaosKingdom = info.getChaosKingdom();
/* 1551 */         if (info.isFlagSet(8))
/* 1552 */           setPremFlag = true; 
/*      */       } 
/* 1554 */       deletePlayer(wurmId);
/*      */       
/* 1556 */       Cooldowns.deleteCooldownsFor(wurmId);
/* 1557 */       Set<Long> itemIds = Items.loadAllNonTransferredItemsIdsForCreature(wurmId, info);
/* 1558 */       for (Iterator<Long> it = itemIds.iterator(); it.hasNext();)
/*      */       {
/* 1560 */         deleteItem(((Long)it.next()).longValue(), (info != null && info.hasMovedInventory()));
/*      */       }
/* 1562 */       int numwounds = dis.readInt();
/*      */       
/* 1564 */       for (int w = 0; w < numwounds; w++) {
/*      */ 
/*      */         
/* 1567 */         WoundMetaData wm = new WoundMetaData(dis.readLong(), dis.readByte(), dis.readByte(), dis.readFloat(), wurmId, dis.readFloat(), dis.readFloat(), dis.readBoolean(), dis.readLong(), dis.readByte());
/* 1568 */         wm.save();
/*      */       } 
/* 1570 */       String name = dis.readUTF();
/* 1571 */       if (info == null) {
/*      */ 
/*      */ 
/*      */         
/* 1575 */         info = PlayerInfoFactory.createPlayerInfo(name);
/* 1576 */         info.loaded = false;
/*      */         
/*      */         try {
/* 1579 */           info.load();
/*      */           
/* 1581 */           logger.log(Level.INFO, "Found old player info for the name " + name + ". Deleting old information with wurmid " + info.wurmId + ". New wurmid=" + wurmId);
/*      */           
/* 1583 */           if (info.wurmId > 0L) {
/*      */ 
/*      */             
/* 1586 */             deletePlayer(name, info.wurmId);
/* 1587 */             info.wurmId = wurmId;
/* 1588 */             info.loaded = false;
/* 1589 */             info = null;
/* 1590 */             logger.log(Level.INFO, "Player " + name + " deleted. PlayerInfo is null");
/*      */           } else {
/*      */             
/* 1593 */             logger.log(Level.INFO, "Since the player information for " + name + " had wurmid " + info.wurmId + " it was not deleted.");
/*      */           }
/*      */         
/* 1596 */         } catch (IOException iox) {
/*      */           
/* 1598 */           info = null;
/*      */         } 
/*      */       } 
/*      */       
/* 1602 */       String password = dis.readUTF();
/* 1603 */       String session = dis.readUTF();
/* 1604 */       String email = dis.readUTF();
/* 1605 */       long sessionExpiration = dis.readLong();
/* 1606 */       byte power = dis.readByte();
/* 1607 */       byte deity = dis.readByte();
/* 1608 */       float align = dis.readFloat();
/*      */       
/* 1610 */       float faith = dis.readFloat();
/* 1611 */       float favor = dis.readFloat();
/* 1612 */       byte god = dis.readByte();
/* 1613 */       byte realdeath = dis.readByte();
/* 1614 */       long lastChangedDeity = dis.readLong();
/* 1615 */       int fatiguesecsleft = dis.readInt();
/* 1616 */       int fatigueSecsToday = dis.readInt();
/* 1617 */       int fatigueSecsYesterday = dis.readInt();
/* 1618 */       long lastfatigue = dis.readLong();
/* 1619 */       long lastwarned = dis.readLong();
/* 1620 */       long lastcheated = dis.readLong();
/* 1621 */       long plantedSign = dis.readLong();
/* 1622 */       long playingTime = dis.readLong();
/* 1623 */       long creationDate = dis.readLong();
/* 1624 */       byte kingdom = dis.readByte();
/* 1625 */       boolean votedKing = dis.readBoolean();
/*      */       
/* 1627 */       int rank = dis.readInt();
/* 1628 */       int maxRank = dis.readInt();
/* 1629 */       long lastModifiedRank = dis.readLong();
/* 1630 */       boolean banned = dis.readBoolean();
/* 1631 */       long banexpiry = dis.readLong();
/* 1632 */       String banreason = dis.readUTF();
/* 1633 */       short muteTimes = dis.readShort();
/* 1634 */       boolean reimbursed = dis.readBoolean();
/* 1635 */       int warnings = dis.readInt();
/* 1636 */       boolean mayHearDevtalk = dis.readBoolean();
/* 1637 */       long paymentExpire = dis.readLong();
/* 1638 */       int numignored = dis.readInt();
/* 1639 */       long[] ignored = new long[numignored];
/* 1640 */       for (int ni = 0; ni < numignored; ni++)
/* 1641 */         ignored[ni] = dis.readLong(); 
/* 1642 */       if (numignored == 0)
/* 1643 */         ignored = EMPTY_LONG_PRIMITIVE_ARRAY; 
/* 1644 */       int numfriends = dis.readInt();
/* 1645 */       long[] friends = new long[numfriends];
/* 1646 */       byte[] friendCats = new byte[numfriends];
/* 1647 */       for (int nf = 0; nf < numfriends; nf++) {
/*      */         
/* 1649 */         friends[nf] = dis.readLong();
/* 1650 */         friendCats[nf] = dis.readByte();
/*      */       } 
/* 1652 */       if (numfriends == 0) {
/*      */         
/* 1654 */         friends = EMPTY_LONG_PRIMITIVE_ARRAY;
/* 1655 */         friendCats = EMPTY_BYTE_PRIMITIVE_ARRAY;
/*      */       } 
/* 1657 */       String ipaddress = dis.readUTF();
/* 1658 */       long version = dis.readLong();
/* 1659 */       boolean dead = dis.readBoolean();
/* 1660 */       boolean mute = dis.readBoolean();
/* 1661 */       long lastFaith = dis.readLong();
/* 1662 */       byte numFaith = dis.readByte();
/* 1663 */       long money = dis.readLong();
/* 1664 */       boolean climbing = dis.readBoolean();
/* 1665 */       byte changedKingdom = dis.readByte();
/*      */       
/* 1667 */       long face = dis.readLong();
/* 1668 */       byte blood = dis.readByte();
/* 1669 */       long flags = dis.readLong();
/* 1670 */       long flags2 = dis.readLong();
/* 1671 */       long abilities = dis.readLong();
/* 1672 */       int scenarioKarma = dis.readInt();
/* 1673 */       int abilityTitle = dis.readInt();
/* 1674 */       byte chaosKingdom = dis.readByte();
/*      */       
/* 1676 */       byte undeadType = dis.readByte();
/* 1677 */       int undeadKills = dis.readInt();
/* 1678 */       int undeadPKills = dis.readInt();
/* 1679 */       int undeadPSecs = dis.readInt();
/* 1680 */       long lastResetEarningsCounter = dis.readLong();
/* 1681 */       long moneyEarnedBySellingLastHour = dis.readLong();
/* 1682 */       long moneyEarnedBySellingEver = dis.readLong();
/* 1683 */       int daysPrem = 0;
/* 1684 */       long lastTicked = 0L;
/* 1685 */       int monthsPaidEver = 0;
/* 1686 */       int monthsPaidInARow = 0;
/* 1687 */       int monthsPaidSinceReset = 0;
/* 1688 */       int silverPaidEver = 0;
/* 1689 */       int currentLoyalty = 0;
/* 1690 */       int totalLoyalty = 0;
/*      */       
/* 1692 */       boolean awards = false;
/*      */       
/* 1694 */       if (dis.readBoolean()) {
/*      */         
/* 1696 */         awards = true;
/* 1697 */         daysPrem = dis.readInt();
/* 1698 */         lastTicked = dis.readLong();
/* 1699 */         monthsPaidEver = dis.readInt();
/* 1700 */         monthsPaidInARow = dis.readInt();
/* 1701 */         monthsPaidSinceReset = dis.readInt();
/* 1702 */         silverPaidEver = dis.readInt();
/* 1703 */         currentLoyalty = dis.readInt();
/* 1704 */         totalLoyalty = dis.readInt();
/* 1705 */         if (info != null) {
/*      */           
/* 1707 */           info.awards = Awards.getAwards(info.wurmId);
/* 1708 */           if (info.awards != null) {
/*      */             
/* 1710 */             info.awards = new Awards(wurmId, daysPrem, monthsPaidEver, monthsPaidInARow, monthsPaidSinceReset, silverPaidEver, lastTicked, currentLoyalty, totalLoyalty, false);
/*      */             
/* 1712 */             info.awards.update();
/*      */           } else {
/*      */             
/* 1715 */             info.awards = new Awards(wurmId, daysPrem, monthsPaidEver, monthsPaidInARow, monthsPaidSinceReset, silverPaidEver, lastTicked, currentLoyalty, totalLoyalty, true);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1720 */       short hotaWins = dis.readShort();
/* 1721 */       boolean hasFreeTransfer = dis.readBoolean();
/* 1722 */       int reputation = dis.readInt();
/* 1723 */       long lastPolledRep = dis.readLong();
/* 1724 */       long pet = dis.readLong();
/* 1725 */       if (pet != -10L) {
/*      */         
/* 1727 */         if (!Creatures.getInstance().isCreatureOffline(Long.valueOf(pet))) {
/*      */           
/*      */           try {
/*      */             
/* 1731 */             Creature petcret = Creatures.getInstance().getCreature(pet);
/* 1732 */             if (petcret.dominator != wurmId) {
/* 1733 */               pet = -10L;
/*      */             }
/* 1735 */           } catch (NoSuchCreatureException nsc) {
/*      */             
/* 1737 */             pet = Creatures.getInstance().getPetId(wurmId);
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/* 1742 */         pet = Creatures.getInstance().getPetId(wurmId);
/* 1743 */       }  long nicotime = dis.readLong();
/* 1744 */       long alcotime = dis.readLong();
/* 1745 */       float nicotine = dis.readFloat();
/* 1746 */       float alcohol = dis.readFloat();
/* 1747 */       boolean logging = dis.readBoolean();
/* 1748 */       int title = dis.readInt();
/* 1749 */       int secondTitle = dis.readInt();
/* 1750 */       int numTitles = dis.readInt();
/* 1751 */       int[] titleArr = EMPTY_INT_ARRAY;
/* 1752 */       if (numTitles > 0) {
/*      */         
/* 1754 */         titleArr = new int[numTitles];
/* 1755 */         for (int i = 0; i < numTitles; i++)
/*      */         {
/* 1757 */           titleArr[i] = dis.readInt();
/*      */         }
/*      */       } 
/* 1760 */       long muteexpiry = dis.readLong();
/* 1761 */       String mutereason = dis.readUTF();
/* 1762 */       boolean maymute = dis.readBoolean();
/* 1763 */       boolean overRideShop = dis.readBoolean();
/* 1764 */       int currentServer = dis.readInt();
/* 1765 */       int lastServer = dis.readInt();
/* 1766 */       long referrer = dis.readLong();
/* 1767 */       String pwQuestion = dis.readUTF();
/* 1768 */       String pwAnswer = dis.readUTF();
/* 1769 */       boolean isPriest = dis.readBoolean();
/* 1770 */       byte priestType = 0;
/* 1771 */       long lastChangedPriest = 0L;
/* 1772 */       if (isPriest) {
/*      */         
/* 1774 */         priestType = dis.readByte();
/* 1775 */         lastChangedPriest = dis.readLong();
/*      */       } 
/*      */ 
/*      */       
/* 1779 */       if (Servers.localServer.PVPSERVER) {
/*      */         
/* 1781 */         if (oldChaosKingdom != 0)
/* 1782 */           chaosKingdom = oldChaosKingdom; 
/* 1783 */         if (chaosKingdom != 0)
/* 1784 */           kingdom = chaosKingdom; 
/* 1785 */         Village cv = Villages.getVillageForCreature(wurmId);
/* 1786 */         if (cv != null) {
/* 1787 */           kingdom = cv.kingdom;
/*      */         }
/*      */         
/* 1790 */         if (blood == 0) {
/* 1791 */           blood = calculateBloodFromKingdom(chaosKingdom);
/*      */         }
/* 1793 */         if (info != null)
/*      */         {
/* 1795 */           if (info.getDeity() != null && info.getDeity().getNumber() == 4) {
/*      */ 
/*      */             
/* 1798 */             BitSet flag2Bits = MiscConstants.createBitSetLong(flags2);
/* 1799 */             if (!flag2Bits.get(11)) {
/*      */ 
/*      */               
/* 1802 */               if (deity == 0)
/* 1803 */                 deity = 4; 
/* 1804 */               if (info.getFaith() > faith)
/* 1805 */                 faith = info.getFaith(); 
/* 1806 */               if (info.isPriest && !isPriest) {
/* 1807 */                 isPriest = info.isPriest;
/*      */               }
/* 1809 */               flag2Bits.set(11);
/* 1810 */               flags2 = MiscConstants.bitSetToLong(flag2Bits);
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1822 */         if (Deities.getDeity(deity) != null && 
/* 1823 */           !QuestionParser.doesKingdomTemplateAcceptDeity(Kingdoms.getKingdomTemplateFor(kingdom), Deities.getDeity(deity)))
/*      */         {
/*      */           
/* 1826 */           if (kingdom == 4)
/*      */           {
/* 1828 */             kingdom = 3;
/*      */           }
/*      */           else
/*      */           {
/* 1832 */             faith = 0.0F;
/* 1833 */             favor = 0.0F;
/* 1834 */             align = 0.0F;
/* 1835 */             deity = 0;
/* 1836 */             isPriest = false;
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       }
/* 1842 */       else if (power <= 0 && !Servers.localServer.PVPSERVER && Servers.localServer.HOMESERVER) {
/*      */         
/* 1844 */         kingdom = (Servers.localServer.getKingdom() != 0) ? Servers.localServer.getKingdom() : 4;
/* 1845 */         if (deity == 4)
/*      */         {
/* 1847 */           if (info != null)
/*      */           {
/* 1849 */             if (info.getDeity() != null && info.getDeity().getNumber() != 4) {
/*      */ 
/*      */               
/* 1852 */               BitSet flag2Bits = MiscConstants.createBitSetLong(flags2);
/* 1853 */               if (!flag2Bits.get(11)) {
/*      */ 
/*      */                 
/* 1856 */                 if (info.getFaith() > faith)
/* 1857 */                   faith = info.getFaith(); 
/* 1858 */                 if (info.isPriest && !isPriest) {
/* 1859 */                   isPriest = info.isPriest;
/*      */                 }
/* 1861 */                 flag2Bits.set(11);
/* 1862 */                 flags2 = MiscConstants.bitSetToLong(flag2Bits);
/*      */               } 
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1892 */       if (blood == 0) {
/* 1893 */         blood = calculateBloodFromKingdom(chaosKingdom);
/*      */       }
/* 1895 */       long bed = dis.readLong();
/* 1896 */       int sleep = dis.readInt();
/* 1897 */       boolean theftWarned = dis.readBoolean();
/* 1898 */       boolean noReimbursmentLeft = dis.readBoolean();
/* 1899 */       boolean deathProt = dis.readBoolean();
/* 1900 */       byte fightmode = dis.readByte();
/* 1901 */       long naffinity = dis.readLong();
/* 1902 */       int tutLevel = dis.readInt();
/* 1903 */       boolean autof = dis.readBoolean();
/* 1904 */       long appoints = dis.readLong();
/* 1905 */       boolean seesPAWin = dis.readBoolean();
/* 1906 */       boolean isPA = dis.readBoolean();
/* 1907 */       boolean mayAppointPA = dis.readBoolean();
/* 1908 */       long lastChangedKingdom = dis.readLong();
/*      */       
/* 1910 */       float px = ((posx << 2) + 2);
/* 1911 */       float py = ((posy << 2) + 2);
/*      */       
/* 1913 */       float posz = 0.0F;
/* 1914 */       int zoneId = 0;
/*      */       
/* 1916 */       if (!Servers.localServer.LOGINSERVER) {
/*      */         
/*      */         try {
/*      */           
/* 1920 */           if (posx > Zones.worldTileSizeX || posx < 0)
/* 1921 */             posx = Zones.worldTileSizeX / 2; 
/* 1922 */           if (posy > Zones.worldTileSizeY || posy < 0) {
/* 1923 */             posy = Zones.worldTileSizeY / 2;
/*      */           }
/* 1925 */           px = ((posx << 2) + 2);
/* 1926 */           py = ((posy << 2) + 2);
/*      */           
/* 1928 */           Zone zone = Zones.getZone(posx, posy, surfaced);
/* 1929 */           zoneId = zone.getId();
/* 1930 */           posz = 0.0F;
/* 1931 */           int tile = Server.surfaceMesh.getTile(posx, posy);
/* 1932 */           if (!surfaced) {
/*      */             
/* 1934 */             tile = Server.caveMesh.getTile(posx, posy);
/* 1935 */             posz = Math.max(-1.45F, Tiles.decodeHeightAsFloat(tile));
/*      */           } else {
/*      */             
/* 1938 */             posz = Math.max(-1.45F, Tiles.decodeHeightAsFloat(tile));
/*      */           } 
/* 1940 */         } catch (NoSuchZoneException nsz) {
/*      */           
/* 1942 */           logger.log(Level.WARNING, "No end zone for " + wurmId + " at " + posx + ", " + posy);
/* 1943 */           return -1L;
/*      */         } 
/*      */       }
/*      */       
/* 1947 */       long lastLostChampion = dis.readLong();
/* 1948 */       short championPoints = dis.readShort();
/* 1949 */       float champChanneling = dis.readFloat();
/* 1950 */       byte epicKingdom = dis.readByte();
/* 1951 */       int epicServerId = dis.readInt();
/* 1952 */       int karma = dis.readInt();
/* 1953 */       int maxKarma = dis.readInt();
/* 1954 */       int totalKarma = dis.readInt();
/*      */       
/* 1956 */       String templateName = dis.readUTF();
/*      */       
/* 1958 */       short chigh = dis.readShort();
/* 1959 */       short clong = dis.readShort();
/* 1960 */       short cwide = dis.readShort();
/* 1961 */       float rotation = dis.readFloat();
/* 1962 */       long bodyId = dis.readLong();
/* 1963 */       long buildingId = dis.readLong();
/* 1964 */       int damage = dis.readInt();
/* 1965 */       int hunger = dis.readInt();
/* 1966 */       int stunned = dis.readInt();
/* 1967 */       int thirst = dis.readInt();
/* 1968 */       int stamina = dis.readInt();
/* 1969 */       float nutritionLevel = dis.readFloat();
/* 1970 */       byte sex = dis.readByte();
/* 1971 */       long inventoryId = dis.readLong();
/*      */ 
/*      */       
/* 1974 */       boolean onSurface = dis.readBoolean();
/* 1975 */       boolean unconscious = dis.readBoolean();
/* 1976 */       int age = dis.readInt();
/* 1977 */       long lastPolledAge = dis.readLong();
/* 1978 */       byte fat = dis.readByte();
/* 1979 */       short detectionSecs = dis.readShort();
/* 1980 */       byte disease = dis.readByte();
/* 1981 */       float calories = dis.readFloat();
/* 1982 */       float carbs = dis.readFloat();
/* 1983 */       float fats = dis.readFloat();
/* 1984 */       float proteins = dis.readFloat();
/*      */ 
/*      */       
/* 1987 */       if (dis.readBoolean()) {
/*      */         
/* 1989 */         readCultist(dis, name, wurmId);
/*      */       }
/*      */       else {
/*      */         
/* 1993 */         readNullCultist(dis, name, wurmId);
/*      */       } 
/* 1995 */       long lastChangedPath = dis.readLong();
/*      */ 
/*      */ 
/*      */       
/* 1999 */       long lastPuppeteered = dis.readLong();
/* 2000 */       if (lastPuppeteered > 0L) {
/* 2001 */         Puppet.addPuppetTime(wurmId, lastPuppeteered);
/*      */       }
/* 2003 */       int numcooldowns = dis.readInt();
/* 2004 */       Map<Integer, Long> cooldowns = new HashMap<>();
/* 2005 */       if (numcooldowns > 0)
/*      */       {
/* 2007 */         for (int i = 0; i < numcooldowns; i++)
/*      */         {
/* 2009 */           cooldowns.put(Integer.valueOf(dis.readInt()), Long.valueOf(dis.readLong()));
/*      */         }
/*      */       }
/*      */       
/* 2013 */       int numItems = dis.readInt();
/* 2014 */       Set<ItemMetaData> idset = new HashSet<>();
/* 2015 */       for (int x = 0; x < numItems; x++)
/*      */       {
/* 2017 */         createItem(dis, px, py, posz, idset, (info != null && info.hasMovedInventory()));
/*      */       }
/* 2019 */       Affinities.deleteAllPlayerAffinity(wurmId);
/* 2020 */       int numskills = dis.readInt();
/* 2021 */       for (int s = 0; s < numskills; s++) {
/*      */ 
/*      */         
/* 2024 */         SkillMetaData sk = new SkillMetaData(dis.readLong(), wurmId, dis.readInt(), dis.readDouble(), dis.readDouble(), dis.readLong());
/* 2025 */         if (Servers.localServer.isChallengeServer())
/* 2026 */           sk.setChallenge(); 
/* 2027 */         sk.save();
/*      */       } 
/* 2029 */       int numAffinities = dis.readInt();
/* 2030 */       for (int xa = 0; xa < numAffinities; xa++) {
/*      */         
/* 2032 */         int skillNumber = dis.readInt();
/* 2033 */         int affinity = dis.readByte() & 0xFF;
/* 2034 */         if (affinity > 0)
/* 2035 */           Affinities.setAffinity(wurmId, skillNumber, affinity, false); 
/*      */       } 
/* 2037 */       int numspeffects = dis.readInt();
/* 2038 */       for (int seff = 0; seff < numspeffects; seff++)
/*      */       {
/* 2040 */         (new SpellEffectMetaData(dis.readLong(), wurmId, dis.readByte(), dis.readFloat(), dis.readInt(), false)).save();
/*      */       }
/*      */       
/* 2043 */       unpackAchievements(wurmId, dis);
/*      */ 
/*      */       
/*      */       try {
/* 2047 */         RecipesByPlayer.unPackRecipes(dis, wurmId);
/*      */       }
/* 2049 */       catch (Exception e) {
/*      */         
/* 2051 */         logger.warning("Exception unpacking recipes: " + e.getMessage());
/* 2052 */         e.printStackTrace();
/* 2053 */         logger.warning("Deleting recipes for player to prevent corruption.");
/* 2054 */         RecipesByPlayer.deleteRecipesForPlayer(wurmId);
/*      */       } 
/*      */       
/* 2057 */       PlayerMetaData pmd = new PlayerMetaData(wurmId, name, password, session, chigh, clong, cwide, sessionExpiration, power, deity, align, faith, favor, god, realdeath, lastChangedDeity, fatiguesecsleft, lastfatigue, lastwarned, lastcheated, plantedSign, playingTime, kingdom, rank, banned, banexpiry, banreason, reimbursed, warnings, mayHearDevtalk, paymentExpire, ignored, friends, friendCats, templateName, ipaddress, dead, mute, bodyId, buildingId, damage, hunger, stunned, thirst, stamina, sex, inventoryId, surfaced, unconscious, px, py, posz, rotation, zoneId, version, lastFaith, numFaith, money, climbing, changedKingdom, age, lastPolledAge, fat, face, reputation, lastPolledRep, title, secondTitle, titleArr);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2065 */       pmd.pet = pet;
/* 2066 */       pmd.alcohol = alcohol;
/* 2067 */       pmd.alcoholTime = alcotime;
/* 2068 */       pmd.nicotine = nicotine;
/* 2069 */       pmd.nicotineTime = nicotime;
/* 2070 */       pmd.priestType = priestType;
/* 2071 */       pmd.lastChangedPriestType = lastChangedPriest;
/* 2072 */       pmd.logging = logging;
/* 2073 */       pmd.mayMute = maymute;
/* 2074 */       pmd.overrideshop = overRideShop;
/* 2075 */       pmd.maxRank = maxRank;
/* 2076 */       pmd.lastModifiedRank = lastModifiedRank;
/* 2077 */       pmd.muteexpiry = muteexpiry;
/* 2078 */       pmd.mutereason = mutereason;
/* 2079 */       pmd.lastServer = lastServer;
/* 2080 */       pmd.currentServer = currentServer;
/* 2081 */       pmd.referrer = referrer;
/* 2082 */       pmd.pwQuestion = pwQuestion;
/* 2083 */       pmd.pwAnswer = pwAnswer;
/* 2084 */       pmd.isPriest = isPriest;
/* 2085 */       pmd.bed = bed;
/* 2086 */       pmd.sleep = sleep;
/* 2087 */       pmd.creationDate = creationDate;
/* 2088 */       pmd.istheftwarned = theftWarned;
/* 2089 */       pmd.noReimbLeft = noReimbursmentLeft;
/* 2090 */       pmd.deathProt = deathProt;
/* 2091 */       pmd.fatigueSecsToday = fatigueSecsToday;
/* 2092 */       pmd.fatigueSecsYday = fatigueSecsYesterday;
/* 2093 */       pmd.fightmode = fightmode;
/* 2094 */       pmd.nextAffinity = naffinity;
/* 2095 */       pmd.detectionSecs = detectionSecs;
/* 2096 */       pmd.tutLevel = tutLevel;
/* 2097 */       pmd.autofight = autof;
/* 2098 */       pmd.appointments = appoints;
/* 2099 */       pmd.seesPAWin = seesPAWin;
/* 2100 */       pmd.isPA = isPA;
/* 2101 */       pmd.mayAppointPA = mayAppointPA;
/* 2102 */       pmd.nutrition = nutritionLevel;
/* 2103 */       pmd.disease = disease;
/* 2104 */       pmd.calories = calories;
/* 2105 */       pmd.carbs = carbs;
/* 2106 */       pmd.fats = fats;
/* 2107 */       pmd.proteins = proteins;
/* 2108 */       pmd.cooldowns = cooldowns;
/* 2109 */       pmd.lastChangedKingdom = lastChangedKingdom;
/* 2110 */       pmd.lastLostChampion = lastLostChampion;
/* 2111 */       pmd.championPoints = championPoints;
/* 2112 */       pmd.champChanneling = champChanneling;
/* 2113 */       pmd.muteTimes = muteTimes;
/* 2114 */       pmd.voteKing = votedKing;
/* 2115 */       pmd.epicKingdom = epicKingdom;
/* 2116 */       pmd.epicServerId = epicServerId;
/* 2117 */       pmd.chaosKingdom = chaosKingdom;
/* 2118 */       pmd.hotaWins = hotaWins;
/* 2119 */       pmd.hasFreeTransfer = hasFreeTransfer;
/* 2120 */       pmd.karma = karma;
/* 2121 */       pmd.maxKarma = maxKarma;
/* 2122 */       pmd.totalKarma = totalKarma;
/* 2123 */       if (blood == 0)
/* 2124 */         blood = calculateBloodFromKingdom(kingdom); 
/* 2125 */       pmd.blood = blood;
/* 2126 */       pmd.flags = flags;
/* 2127 */       pmd.flags2 = flags2;
/* 2128 */       pmd.scenarioKarma = scenarioKarma;
/* 2129 */       pmd.abilities = abilities;
/* 2130 */       pmd.abilityTitle = abilityTitle;
/*      */       
/* 2132 */       pmd.undeadType = undeadType;
/* 2133 */       pmd.undeadKills = undeadKills;
/* 2134 */       pmd.undeadPKills = undeadPKills;
/* 2135 */       pmd.undeadPSecs = undeadPSecs;
/* 2136 */       pmd.moneySalesEver = moneyEarnedBySellingEver;
/* 2137 */       pmd.daysPrem = daysPrem;
/* 2138 */       pmd.lastTicked = lastTicked;
/* 2139 */       pmd.currentLoyaltyPoints = currentLoyalty;
/* 2140 */       pmd.totalLoyaltyPoints = totalLoyalty;
/* 2141 */       pmd.monthsPaidEver = monthsPaidEver;
/* 2142 */       pmd.monthsPaidInARow = monthsPaidInARow;
/* 2143 */       pmd.monthsPaidSinceReset = monthsPaidSinceReset;
/* 2144 */       pmd.silverPaidEver = silverPaidEver;
/* 2145 */       pmd.hasAwards = awards;
/* 2146 */       if (Servers.isThisLoginServer()) {
/*      */         
/* 2148 */         if (info != null) {
/*      */           
/* 2150 */           pmd.paymentExpire = info.getPaymentExpire();
/* 2151 */           if (info.emailAddress.length() == 0) {
/* 2152 */             pmd.emailAdress = email;
/*      */           } else {
/* 2154 */             pmd.emailAdress = info.emailAddress;
/* 2155 */           }  pmd.password = info.getPassword();
/*      */           
/* 2157 */           if (pmd.money != info.money) {
/* 2158 */             logger.log(Level.INFO, "Setting money for " + info.getName() + " to " + info.money + " instead of " + pmd.money);
/*      */           }
/* 2160 */           pmd.money = info.money;
/*      */         } else {
/*      */           
/* 2163 */           pmd.emailAdress = email;
/* 2164 */         }  pmd.save();
/*      */       }
/*      */       else {
/*      */         
/* 2168 */         pmd.emailAdress = email;
/* 2169 */         pmd.save();
/*      */       } 
/* 2171 */       logger.log(Level.INFO, "has info:" + ((info != null) ? 1 : 0));
/* 2172 */       if (info != null) {
/*      */         
/* 2174 */         unpackPMList(info, dis);
/* 2175 */         unpackPrivateMapAnnotations(info.getPlayerId(), dis);
/*      */         
/* 2177 */         if (!password.equals(info.getPassword()))
/*      */         {
/* 2179 */           logger.log(Level.WARNING, name + " after transfer but before loading: password now is " + info
/* 2180 */               .getPassword() + ". Sent " + password);
/*      */         }
/*      */         
/* 2183 */         info.loaded = false;
/*      */         
/*      */         try {
/* 2186 */           info.load();
/* 2187 */           boolean updateFlags = false;
/* 2188 */           if (info.flags != flags) {
/*      */             
/* 2190 */             info.flags = flags;
/* 2191 */             info.setFlagBits(info.flags);
/* 2192 */             if (setPremFlag)
/* 2193 */               info.setFlag(8, true); 
/* 2194 */             updateFlags = true;
/*      */           } 
/* 2196 */           if (info.flags2 != flags2) {
/*      */             
/* 2198 */             info.flags2 = flags2;
/* 2199 */             info.setFlag2Bits(info.flags2);
/* 2200 */             updateFlags = true;
/*      */           } 
/* 2202 */           if (updateFlags) {
/* 2203 */             info.forceFlagsUpdate();
/*      */           }
/* 2205 */           if (!password.equals(info.getPassword()))
/*      */           {
/* 2207 */             logger.log(Level.WARNING, name + " after transfer: password now is " + info.getPassword() + "  Sent " + password);
/*      */           
/*      */           }
/*      */         }
/* 2211 */         catch (IOException iox) {
/*      */           
/* 2213 */           logger.log(Level.WARNING, iox.getMessage());
/*      */         } 
/*      */         
/* 2216 */         info.setMoneyEarnedBySellingLastHour(moneyEarnedBySellingLastHour);
/* 2217 */         info.setLastResetEarningsCounter(lastResetEarningsCounter);
/* 2218 */         if (lastChangedPath > info.getLastChangedPath())
/* 2219 */           info.setLastChangedPath(lastChangedPath); 
/*      */       } 
/* 2221 */       if (draggedItem >= 0L) {
/*      */ 
/*      */         
/*      */         try {
/* 2225 */           Item d = Items.getItem(draggedItem);
/*      */           
/*      */           try {
/* 2228 */             Zone z = Zones.getZone((int)d.getPosX() >> 2, (int)d.getPosY() >> 2, true);
/* 2229 */             z.addItem(d);
/*      */           }
/* 2231 */           catch (NoSuchZoneException nsz) {
/*      */             
/* 2233 */             logger.log(Level.WARNING, nsz.getMessage(), (Throwable)nsz);
/*      */           }
/*      */         
/* 2236 */         } catch (NoSuchItemException nsi) {
/*      */           
/* 2238 */           logger.log(Level.WARNING, "Weird. No dragged item " + draggedItem + " after it was saved.");
/*      */         } 
/* 2240 */         draggedItem = -10L;
/*      */       } 
/* 2242 */       if (newPlayer) {
/* 2243 */         logger.log(Level.FINE, name + " created successfully.");
/*      */       } else {
/* 2245 */         logger.log(Level.FINE, name + " unpacked successfully.");
/*      */       } 
/* 2247 */       Village v = Villages.getVillageForCreature(wurmId);
/* 2248 */       if (v != null)
/*      */       {
/* 2250 */         if (v.kingdom != kingdom)
/*      */         {
/* 2252 */           if (v.getMayor().getId() != wurmId) {
/*      */             
/* 2254 */             Citizen c = v.getCitizen(wurmId);
/* 2255 */             v.removeCitizen(c);
/*      */           }
/* 2257 */           else if (Servers.localServer.HOMESERVER) {
/*      */             
/* 2259 */             v.startDisbanding(null, name, wurmId);
/*      */           } 
/*      */         }
/*      */       }
/* 2263 */       saving = false;
/* 2264 */       return wurmId;
/*      */     }
/* 2266 */     catch (IOException ex) {
/*      */       
/* 2268 */       saving = false;
/* 2269 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/* 2270 */       return -1L;
/*      */     }
/*      */     finally {
/*      */       
/* 2274 */       saving = false;
/* 2275 */       if (dis != null) {
/*      */         
/*      */         try {
/*      */           
/* 2279 */           dis.close();
/*      */         }
/* 2281 */         catch (IOException iOException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final void resetTransferVariables(String playerName) {
/* 2291 */     logger.log(Level.INFO, playerName + " resetting transfer data");
/* 2292 */     lastItemName = "unknown";
/* 2293 */     lastItemId = -10L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void createItem(DataInputStream dis, float posx, float posy, float posz, Set<ItemMetaData> metadataset, boolean frozen) throws IOException {
/*      */     try {
/* 2302 */       boolean isStoredAnimalItem = dis.readBoolean();
/* 2303 */       if (isStoredAnimalItem)
/*      */       {
/* 2305 */         CreatureDataStream.fromStream(dis);
/*      */       }
/*      */     }
/* 2308 */     catch (IOException iOException) {
/*      */       
/* 2310 */       logger.log(Level.WARNING, "Exception", iOException);
/*      */     } 
/*      */     
/* 2313 */     boolean locked = dis.readBoolean();
/* 2314 */     long lockid = dis.readLong();
/* 2315 */     if (lockid != -10L) {
/*      */       
/* 2317 */       boolean ok = dis.readBoolean();
/* 2318 */       if (ok)
/* 2319 */         createItem(dis, posx, posy, posz, metadataset, frozen); 
/*      */     } 
/* 2321 */     long itemId = dis.readLong();
/*      */     
/* 2323 */     deleteItem(itemId, frozen);
/* 2324 */     boolean dragged = dis.readBoolean();
/* 2325 */     if (dragged) {
/* 2326 */       draggedItem = itemId;
/*      */     }
/*      */     
/* 2329 */     int numEffects = dis.readInt();
/* 2330 */     for (int e = 0; e < numEffects; e++)
/*      */     {
/* 2332 */       (new EffectMetaData(itemId, dis.readShort(), 0.0F, 0.0F, 0.0F, dis.readLong())).save();
/*      */     }
/* 2334 */     int numspeffects = dis.readInt();
/* 2335 */     for (int seff = 0; seff < numspeffects; seff++)
/*      */     {
/* 2337 */       (new SpellEffectMetaData(dis.readLong(), itemId, dis.readByte(), dis.readFloat(), dis.readInt(), true)).save();
/*      */     }
/*      */ 
/*      */     
/* 2341 */     int numKeys = dis.readInt();
/* 2342 */     long[] keyids = EMPTY_LONG_PRIMITIVE_ARRAY;
/* 2343 */     if (numKeys > 0) {
/*      */       
/* 2345 */       keyids = new long[numKeys];
/* 2346 */       for (int k = 0; k < numKeys; k++)
/*      */       {
/* 2348 */         keyids[k] = dis.readLong();
/*      */       }
/*      */     } 
/* 2351 */     long lastowner = dis.readLong();
/* 2352 */     int data1 = dis.readInt();
/* 2353 */     int data2 = dis.readInt();
/* 2354 */     int extra1 = dis.readInt();
/* 2355 */     int extra2 = dis.readInt();
/* 2356 */     if (data1 != -1 || data2 != -1 || extra1 != -1 || extra2 != -1) {
/*      */       
/* 2358 */       ItemData d = new ItemData(itemId, data1, data2, extra1, extra2);
/*      */       
/*      */       try {
/* 2361 */         d.createDataEntry(DbConnector.getItemDbCon());
/*      */       }
/* 2363 */       catch (SQLException sqx) {
/*      */         
/* 2365 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       } 
/*      */     } 
/* 2368 */     String itname = dis.readUTF();
/* 2369 */     if (Servers.isThisATestServer()) {
/* 2370 */       logger.log(Level.INFO, "Creating " + itname + ", " + itemId);
/*      */     }
/* 2372 */     String desc = dis.readUTF();
/* 2373 */     long ownerId = dis.readLong();
/* 2374 */     long parentId = dis.readLong();
/* 2375 */     long lastmaintained = dis.readLong();
/* 2376 */     float ql = dis.readFloat();
/* 2377 */     float itemdam = dis.readFloat();
/* 2378 */     float origQl = dis.readFloat();
/* 2379 */     int itemtemplateId = dis.readInt();
/* 2380 */     int weight = dis.readInt();
/* 2381 */     short place = dis.readShort();
/* 2382 */     int sizex = dis.readInt();
/* 2383 */     int sizey = dis.readInt();
/* 2384 */     int sizez = dis.readInt();
/* 2385 */     int bless = dis.readInt();
/* 2386 */     byte enchantment = dis.readByte();
/* 2387 */     byte material = dis.readByte();
/* 2388 */     int price = dis.readInt();
/* 2389 */     short temp = dis.readShort();
/* 2390 */     boolean banked = dis.readBoolean();
/* 2391 */     byte auxdata = dis.readByte();
/* 2392 */     long creationDate = dis.readLong();
/* 2393 */     byte creationState = dis.readByte();
/* 2394 */     int realTemplate = dis.readInt();
/* 2395 */     boolean hasMoreItems = dis.readBoolean();
/* 2396 */     if (hasMoreItems) {
/*      */       
/* 2398 */       ItemRequirement.deleteRequirements(itemId);
/* 2399 */       int nums = dis.readInt();
/* 2400 */       for (int xa = 0; xa < nums; xa++) {
/*      */         
/* 2402 */         int templateId = dis.readInt();
/* 2403 */         int numsDone = dis.readInt();
/* 2404 */         ItemRequirement.setRequirements(itemId, templateId, numsDone, true, true);
/*      */       } 
/*      */     } 
/* 2407 */     boolean wornAsArmour = dis.readBoolean();
/* 2408 */     boolean female = dis.readBoolean();
/* 2409 */     boolean mailed = dis.readBoolean();
/* 2410 */     byte mailTimes = dis.readByte();
/* 2411 */     byte rarity = dis.readByte();
/* 2412 */     long onBridge = dis.readLong();
/* 2413 */     int settings = dis.readInt();
/* 2414 */     int numPermissions = dis.readInt();
/* 2415 */     ItemSettings.remove(itemId);
/* 2416 */     LinkedList<String> added = new LinkedList<>();
/* 2417 */     for (int p = 0; p < numPermissions; p++) {
/*      */       
/* 2419 */       long pId = dis.readLong();
/* 2420 */       int pSettings = dis.readInt();
/* 2421 */       ItemSettings.addPlayer(itemId, pId, pSettings);
/*      */       
/* 2423 */       String pName = PermissionsByPlayer.getPlayerOrGroupName(pId);
/*      */       
/* 2425 */       BitSet permissionBits = new BitSet(32);
/* 2426 */       for (int x = 0; x < 32; x++) {
/*      */         
/* 2428 */         if ((pSettings >>> x & 0x1) == 1) {
/* 2429 */           permissionBits.set(x);
/*      */         }
/*      */       } 
/* 2432 */       LinkedList<String> perms = new LinkedList<>();
/* 2433 */       if (permissionBits.get(ItemSettings.ItemPermissions.MANAGE.getBit()))
/* 2434 */         perms.add("+Manage"); 
/* 2435 */       if (permissionBits.get(ItemSettings.VehiclePermissions.COMMANDER.getBit()))
/* 2436 */         perms.add("+Commander"); 
/* 2437 */       if (permissionBits.get(ItemSettings.VehiclePermissions.PASSENGER.getBit()))
/* 2438 */         perms.add("+Passenger"); 
/* 2439 */       if (permissionBits.get(ItemSettings.VehiclePermissions.ACCESS_HOLD.getBit()))
/* 2440 */         perms.add("+Access Hold"); 
/* 2441 */       if (permissionBits.get(ItemSettings.BedPermissions.MAY_USE_BED.getBit()))
/* 2442 */         perms.add("+Sleep"); 
/* 2443 */       if (permissionBits.get(ItemSettings.BedPermissions.FREE_SLEEP.getBit()))
/* 2444 */         perms.add("+Free Sleep"); 
/* 2445 */       if (permissionBits.get(ItemSettings.MessageBoardPermissions.MAY_POST_NOTICES.getBit()))
/* 2446 */         perms.add("+Add Notices"); 
/* 2447 */       if (permissionBits.get(ItemSettings.MessageBoardPermissions.MAY_ADD_PMS.getBit()))
/* 2448 */         perms.add("+Add PMs"); 
/* 2449 */       if (permissionBits.get(ItemSettings.VehiclePermissions.DRAG.getBit()))
/* 2450 */         perms.add("+Drag"); 
/* 2451 */       if (permissionBits.get(ItemSettings.VehiclePermissions.EXCLUDE.getBit())) {
/* 2452 */         perms.add("+Deny All");
/*      */       }
/* 2454 */       added.add(pName + "(" + String.join(", ", (Iterable)perms) + ")");
/*      */     } 
/* 2456 */     if (!added.isEmpty()) {
/*      */       
/* 2458 */       String stuffAdded = "Imported " + String.join(", ", (Iterable)added);
/* 2459 */       PermissionsHistories.addHistoryEntry(itemId, System.currentTimeMillis(), -10L, "Transfered", stuffAdded);
/*      */     } 
/* 2461 */     boolean hasInscription = dis.readBoolean();
/* 2462 */     if (hasInscription) {
/*      */ 
/*      */ 
/*      */       
/* 2466 */       InscriptionData insdata = new InscriptionData(itemId, dis.readUTF(), dis.readUTF(), 0);
/*      */       
/*      */       try {
/* 2469 */         insdata.createInscriptionEntry(DbConnector.getItemDbCon());
/*      */       }
/* 2471 */       catch (SQLException sqx) {
/*      */         
/* 2473 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       } 
/*      */     } 
/* 2476 */     int color = dis.readInt();
/* 2477 */     int color2 = dis.readInt();
/* 2478 */     String creator = dis.readUTF();
/*      */     
/* 2480 */     Itempool.deleteItem(itemtemplateId, itemId);
/* 2481 */     if (dis.readBoolean()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2486 */       short calories = dis.readShort();
/* 2487 */       short carbs = dis.readShort();
/* 2488 */       short fats = dis.readShort();
/* 2489 */       short proteins = dis.readShort();
/* 2490 */       byte bonus = dis.readByte();
/* 2491 */       byte stages = dis.readByte();
/* 2492 */       byte ingredients = dis.readByte();
/* 2493 */       short recipeId = dis.readShort();
/* 2494 */       ItemMealData.save(itemId, recipeId, calories, carbs, fats, proteins, bonus, stages, ingredients);
/*      */     } 
/*      */     
/* 2497 */     ItemMetaData imd = new ItemMetaData(locked, lockid, itemId, keyids, lastowner, data1, data2, extra1, extra2, itname, desc, ownerId, parentId, lastmaintained, ql, itemdam, origQl, itemtemplateId, weight, sizex, sizey, sizez, bless, enchantment, material, price, temp, banked, auxdata, creationDate, creationState, realTemplate, wornAsArmour, color, color2, place, posx, posy, posz, creator, female, mailed, mailTimes, rarity, onBridge, hasInscription, settings, frozen);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2503 */     imd.save();
/* 2504 */     metadataset.add(imd);
/* 2505 */     lastItemName = itname;
/* 2506 */     lastItemId = itemId;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void validateTransferRequest(ByteBuffer byteBuffer) {
/* 2512 */     if (this.wurmserver.isLagging()) {
/*      */ 
/*      */       
/*      */       try {
/* 2516 */         sendTransferUserRequestAnswer(false, "The server is lagging. Try later.", 10);
/*      */       }
/* 2518 */       catch (IOException iOException) {}
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 2524 */     if (Constants.maintaining) {
/*      */ 
/*      */       
/*      */       try {
/* 2528 */         sendTransferUserRequestAnswer(false, "The server is in maintenance mode.", 0);
/*      */       }
/* 2530 */       catch (IOException iOException) {}
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 2536 */     boolean isDev = (byteBuffer.get() != 0);
/* 2537 */     if (!isDev && Players.getInstance().numberOfPlayers() > Servers.localServer.pLimit) {
/*      */ 
/*      */       
/*      */       try {
/* 2541 */         sendTransferUserRequestAnswer(false, "The server is full. Try later", 30);
/*      */       }
/* 2543 */       catch (IOException iOException) {}
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validate(ByteBuffer byteBuffer) {
/* 2553 */     int protocolVersion = byteBuffer.getInt();
/* 2554 */     if (protocolVersion != 1) {
/*      */ 
/*      */       
/*      */       try {
/* 2558 */         sendLoginAnswer(false, "You are using an old protocol.\nPlease update the server.", 0);
/*      */       }
/* 2560 */       catch (IOException iOException) {}
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 2566 */     byte[] bytes = new byte[byteBuffer.get() & 0xFF];
/* 2567 */     byteBuffer.get(bytes);
/*      */ 
/*      */     
/* 2570 */     boolean dev = (byteBuffer.get() == 1);
/* 2571 */     String password = "Unknown";
/*      */     
/*      */     try {
/* 2574 */       password = new String(bytes, "UTF-8");
/*      */     }
/* 2576 */     catch (UnsupportedEncodingException nse) {
/*      */       
/* 2578 */       password = new String(bytes);
/* 2579 */       logger.log(Level.WARNING, "Unsupported encoding for password.", nse);
/*      */     } 
/* 2581 */     if (!password.equals(Servers.localServer.INTRASERVERPASSWORD)) {
/*      */ 
/*      */       
/*      */       try {
/* 2585 */         sendLoginAnswer(false, "Wrong password: " + password, 0);
/*      */       
/*      */       }
/* 2588 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2599 */       sendLoginAnswer(true, "ok" + Server.rand.nextInt(1000000), 0);
/*      */     }
/* 2601 */     catch (IOException iOException) {}
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
/*      */   private void sendLoginAnswer(boolean ok, String message, int retrySeconds) throws IOException {
/*      */     try {
/* 2621 */       byte[] messageb = message.getBytes("UTF-8");
/* 2622 */       ByteBuffer bb = this.conn.getBuffer();
/*      */       
/* 2624 */       bb.put((byte)2);
/* 2625 */       if (ok) {
/* 2626 */         bb.put((byte)1);
/*      */       } else {
/* 2628 */         bb.put((byte)0);
/*      */       } 
/* 2630 */       bb.putShort((short)messageb.length);
/* 2631 */       bb.put(messageb);
/* 2632 */       bb.putShort((short)retrySeconds);
/* 2633 */       bb.putLong(System.currentTimeMillis());
/* 2634 */       this.conn.flush();
/* 2635 */       if (!ok) {
/* 2636 */         this.conn.ticksToDisconnect = 200;
/*      */       }
/*      */     }
/* 2639 */     catch (Exception ex) {
/*      */       
/* 2641 */       logger.log(Level.WARNING, "Failed to send login answer.", ex);
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
/*      */   private void sendTransferUserRequestAnswer(boolean ok, String sessionKey, int retrySeconds) throws IOException {
/*      */     try {
/* 2658 */       byte[] messageb = sessionKey.getBytes("UTF-8");
/* 2659 */       ByteBuffer bb = this.conn.getBuffer();
/*      */       
/* 2661 */       bb.put((byte)6);
/* 2662 */       if (ok) {
/* 2663 */         bb.put((byte)1);
/*      */       } else {
/* 2665 */         bb.put((byte)0);
/*      */       } 
/* 2667 */       bb.putShort((short)messageb.length);
/* 2668 */       bb.put(messageb);
/* 2669 */       bb.putShort((short)retrySeconds);
/* 2670 */       bb.putLong(System.currentTimeMillis());
/* 2671 */       this.conn.flush();
/* 2672 */       if (!ok)
/* 2673 */         this.conn.ticksToDisconnect = 200; 
/* 2674 */       logger.log(Level.INFO, "Intraserver sent transferrequestanswer. " + ok);
/*      */     }
/* 2676 */     catch (Exception ex) {
/*      */       
/* 2678 */       logger.log(Level.WARNING, "Failed to send TransferUserRequest answer.", ex);
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
/*      */   private void sendCommandDone() throws IOException {
/*      */     try {
/* 2691 */       ByteBuffer bb = this.conn.getBuffer();
/* 2692 */       bb.put((byte)4);
/* 2693 */       this.conn.flush();
/* 2694 */       this.conn.ticksToDisconnect = 200;
/*      */     }
/* 2696 */     catch (Exception ex) {
/*      */       
/* 2698 */       logger.log(Level.WARNING, "Failed to send command done.", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendCommandFailed() throws IOException {
/* 2709 */     logger.log(Level.WARNING, "Command failed : ", new Exception());
/*      */     
/*      */     try {
/* 2712 */       ByteBuffer bb = this.conn.getBuffer();
/* 2713 */       bb.put((byte)5);
/* 2714 */       this.conn.flush();
/* 2715 */       this.conn.ticksToDisconnect = 200;
/*      */     }
/* 2717 */     catch (Exception ex) {
/*      */       
/* 2719 */       logger.log(Level.WARNING, "Failed to send command failed.", ex);
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
/*      */   private void sendDataReceived() throws IOException {
/*      */     try {
/* 2732 */       ByteBuffer bb = this.conn.getBuffer();
/* 2733 */       bb.put((byte)8);
/* 2734 */       this.conn.flush();
/*      */     }
/* 2736 */     catch (Exception ex) {
/*      */       
/* 2738 */       logger.log(Level.WARNING, "Failed to send DataReceived.", ex);
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
/*      */   private void sendTimeSync() throws IOException {
/*      */     try {
/* 2751 */       ByteBuffer bb = this.conn.getBuffer();
/* 2752 */       bb.put((byte)10);
/* 2753 */       bb.putLong(WurmCalendar.currentTime);
/* 2754 */       this.conn.flush();
/*      */     }
/* 2756 */     catch (Exception ex) {
/*      */       
/* 2758 */       logger.log(Level.WARNING, "Failed to send timesync.", ex);
/*      */     } 
/* 2760 */     this.conn.ticksToDisconnect = 200;
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
/*      */   private void sendPlayerVersion(long playerversion) throws IOException {
/*      */     try {
/* 2774 */       ByteBuffer bb = this.conn.getBuffer();
/* 2775 */       bb.put((byte)9);
/* 2776 */       bb.putLong(playerversion);
/* 2777 */       this.conn.flush();
/*      */     }
/* 2779 */     catch (Exception ex) {
/*      */       
/* 2781 */       logger.log(Level.WARNING, "Failed to send player version.", ex);
/*      */     } 
/*      */     
/* 2784 */     this.conn.ticksToDisconnect = 200;
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
/*      */   private void sendPlayerPaymentExpire(long paymentExpire) throws IOException {
/*      */     try {
/* 2798 */       ByteBuffer bb = this.conn.getBuffer();
/* 2799 */       bb.put((byte)11);
/* 2800 */       bb.putLong(paymentExpire);
/* 2801 */       this.conn.flush();
/*      */     }
/* 2803 */     catch (Exception ex) {
/*      */       
/* 2805 */       logger.log(Level.WARNING, "Failed to send expiretime.", ex);
/* 2806 */       sendCommandFailed();
/*      */     } 
/* 2808 */     this.conn.ticksToDisconnect = 200;
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
/*      */   private void sendPingAnswer() throws IOException {
/*      */     try {
/* 2821 */       ByteBuffer bb = this.conn.getBuffer();
/* 2822 */       if (Server.getMillisToShutDown() > -1000L && Server.getMillisToShutDown() < 120000L) {
/* 2823 */         bb.put((byte)14);
/*      */       } else {
/*      */         
/* 2826 */         bb.put((byte)13);
/* 2827 */         if (Constants.maintaining) {
/* 2828 */           bb.put((byte)1);
/*      */         } else {
/* 2830 */           bb.put((byte)0);
/* 2831 */         }  bb.putInt(Players.getInstance().getNumberOfPlayers());
/* 2832 */         bb.putInt(Servers.localServer.pLimit);
/* 2833 */         if (Server.getMillisToShutDown() > 0L) {
/* 2834 */           bb.putInt(Math.max(1, (int)(Server.getMillisToShutDown() / 1000L)));
/*      */         } else {
/* 2836 */           bb.putInt(0);
/* 2837 */         }  bb.putInt(Constants.meshSize);
/*      */       } 
/* 2839 */       this.conn.flush();
/*      */     
/*      */     }
/* 2842 */     catch (Exception ex) {
/*      */       
/* 2844 */       logger.log(Level.WARNING, "Failed to send ping answer.", ex);
/* 2845 */       sendCommandFailed();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\IntraServerConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */