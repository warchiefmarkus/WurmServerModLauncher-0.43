/*      */ package com.wurmonline.server.intra;
/*      */ 
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.LoginServerWebConnection;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.CreatureDataStream;
/*      */ import com.wurmonline.server.creatures.CreatureStatus;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.effects.Effect;
/*      */ import com.wurmonline.server.items.InscriptionData;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemMealData;
/*      */ import com.wurmonline.server.items.ItemRequirement;
/*      */ import com.wurmonline.server.items.ItemSettings;
/*      */ import com.wurmonline.server.items.ItemSpellEffects;
/*      */ import com.wurmonline.server.items.Puppet;
/*      */ import com.wurmonline.server.items.RecipesByPlayer;
/*      */ import com.wurmonline.server.players.Achievement;
/*      */ import com.wurmonline.server.players.AchievementTemplate;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Cultist;
/*      */ import com.wurmonline.server.players.Friend;
/*      */ import com.wurmonline.server.players.MapAnnotation;
/*      */ import com.wurmonline.server.players.PermissionsByPlayer;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.players.Titles;
/*      */ import com.wurmonline.server.skills.Affinities;
/*      */ import com.wurmonline.server.skills.Affinity;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import com.wurmonline.server.spells.Cooldowns;
/*      */ import com.wurmonline.server.spells.SpellEffect;
/*      */ import com.wurmonline.shared.util.IoUtilities;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public final class PlayerTransfer
/*      */   extends IntraCommand
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*   77 */   private static final Logger logger = Logger.getLogger(PlayerTransfer.class.getName());
/*      */ 
/*      */   
/*      */   private final Player player;
/*      */   
/*      */   private boolean done = false;
/*      */   
/*      */   private byte[] data;
/*      */   
/*      */   private final int posX;
/*      */   
/*      */   private final int posY;
/*      */   
/*      */   private final boolean surfaced;
/*      */   
/*      */   public boolean copiedToLoginServer = false;
/*      */   
/*   94 */   private long loginServerVersion = -10L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int targetServerId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int maxItems = 200;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int standardBodyInventoryItems = 12;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean toOrFromEpic;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte targetKingdomId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlayerTransfer(Server server, Player wurmplayer, String ip, int port, String serverpass, int _targetServerId, int posx, int posy, boolean aSurfaced, boolean toOrFromEpicCluster, byte targetKingdom) throws Exception {
/*  128 */     this.player = wurmplayer;
/*  129 */     this.toOrFromEpic = ((toOrFromEpicCluster && !Servers.isThisLoginServer()) || _targetServerId == 20);
/*  130 */     this.posX = posx;
/*  131 */     this.posY = posy;
/*  132 */     this.surfaced = aSurfaced;
/*  133 */     this.targetServerId = _targetServerId;
/*  134 */     if (Servers.isThisLoginServer())
/*      */     {
/*  136 */       this.copiedToLoginServer = true;
/*      */     }
/*  138 */     this.targetKingdomId = targetKingdom;
/*  139 */     wurmplayer.setIsTransferring(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean willItemsTransfer(Player player, boolean setTransferFlag, int targetServer) {
/*  146 */     return willItemsTransfer(player, setTransferFlag, targetServer, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean willItemsTransfer(Player player, boolean setTransferFlag, int targetServer, boolean changingCluster) {
/*  155 */     int numitems = 0;
/*  156 */     int stayBehind = 0;
/*  157 */     Item[] items = player.getInventory().getAllItems(true);
/*      */     
/*  159 */     boolean ok = true; int x;
/*  160 */     for (x = 0; x < items.length; x++) {
/*      */ 
/*      */       
/*  163 */       if (!items[x].willLeaveServer(setTransferFlag, changingCluster, (player.getPower() > 0))) {
/*      */         
/*  165 */         stayBehind++;
/*  166 */         ok = false;
/*  167 */         if (items[x].isArtifact() && setTransferFlag) {
/*  168 */           player.getCommunicator().sendAlertServerMessage("The " + items[x].getName() + " disappears!");
/*      */         } else {
/*  170 */           player.getCommunicator()
/*  171 */             .sendAlertServerMessage("The " + items[x].getName() + " will not leave the server.");
/*      */         } 
/*      */       } else {
/*      */         
/*  175 */         numitems++;
/*  176 */         if (player.getPower() == 0 && changingCluster)
/*      */         {
/*  178 */           if (numitems - stayBehind - 12 > 200)
/*      */           {
/*  180 */             if (!items[x].isInventory()) {
/*      */               
/*  182 */               if (setTransferFlag)
/*  183 */                 items[x].setTransferred(true); 
/*  184 */               player.getCommunicator().sendAlertServerMessage("The " + items[x].getName() + " stays behind.");
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*  190 */     items = player.getBody().getBodyItem().getAllItems(true);
/*  191 */     for (x = 0; x < items.length; x++) {
/*      */       
/*  193 */       if (!items[x].isBodyPartAttached())
/*      */       {
/*  195 */         if (!items[x].willLeaveServer(setTransferFlag, changingCluster, (player.getPower() > 0))) {
/*      */           
/*  197 */           stayBehind++;
/*  198 */           ok = false;
/*  199 */           if (items[x].isArtifact() && setTransferFlag) {
/*  200 */             player.getCommunicator().sendAlertServerMessage("The " + items[x].getName() + " disappears!");
/*      */           } else {
/*  202 */             player.getCommunicator()
/*  203 */               .sendAlertServerMessage("The " + items[x].getName() + " will not leave the server.");
/*      */           } 
/*      */         } else {
/*      */           
/*  207 */           numitems++;
/*  208 */           if (player.getPower() == 0 && changingCluster)
/*      */           {
/*  210 */             if (numitems - stayBehind - 12 > 200)
/*      */             {
/*  212 */               if (!items[x].isBodyPartAttached()) {
/*      */                 
/*  214 */                 if (setTransferFlag)
/*  215 */                   items[x].setTransferred(true); 
/*  216 */                 player.getCommunicator().sendAlertServerMessage("The " + items[x].getName() + " stays behind.");
/*      */               } 
/*      */             }
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  223 */     return ok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean poll() {
/*  234 */     if (this.data != null) {
/*      */       
/*  236 */       if (!this.copiedToLoginServer)
/*      */       {
/*  238 */         if (!Servers.isThisLoginServer()) {
/*      */           
/*  240 */           if (this.targetServerId == Servers.loginServer.id) {
/*  241 */             this.copiedToLoginServer = true;
/*      */           } else {
/*      */             
/*  244 */             long time = System.nanoTime();
/*  245 */             if ((new LoginServerWebConnection()).transferPlayer(this.player, this.player.getName(), this.posX, this.posY, this.surfaced, this.data)) {
/*      */               
/*  247 */               this.copiedToLoginServer = true;
/*  248 */               logger.log(Level.INFO, "Copy to login server for " + this.player
/*  249 */                   .getName() + " took " + ((float)(System.nanoTime() - time) / 1000000.0F) + " millis.");
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  254 */               logger.log(Level.INFO, "Failed copy to login server for " + this.player
/*  255 */                   .getName() + " took " + (
/*  256 */                   (float)(System.nanoTime() - time) / 1000000.0F) + " millis.");
/*      */               
/*  258 */               this.player.getCommunicator().sendAlertServerMessage("You can not transfer right now.");
/*  259 */               this.done = true;
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/*  264 */           this.copiedToLoginServer = true;
/*      */         }  } 
/*  266 */       if (this.copiedToLoginServer) {
/*      */         
/*  268 */         ServerEntry entry = Servers.getServerWithId(this.targetServerId);
/*  269 */         if (entry != null) {
/*      */           
/*      */           try {
/*      */             
/*  273 */             if (this.player.getDraggedItem() != null) {
/*  274 */               Items.stopDragging(this.player.getDraggedItem());
/*      */             }
/*  276 */             long time = System.currentTimeMillis();
/*  277 */             if ((new LoginServerWebConnection(entry.id)).transferPlayer(this.player, this.player.getName(), this.posX, this.posY, this.surfaced, this.data))
/*      */             {
/*      */ 
/*      */               
/*  281 */               logger.log(Level.INFO, "Copy to target server for " + entry
/*  282 */                   .getName() + " (" + entry.id + ") " + this.player.getName() + " took " + (
/*  283 */                   System.currentTimeMillis() - time) + " ms.");
/*  284 */               this.player.getSaveFile().setCurrentServer(this.targetServerId);
/*      */               
/*      */               try {
/*  287 */                 this.player.getSaveFile().save();
/*      */               }
/*  289 */               catch (IOException iox) {
/*      */                 
/*  291 */                 logger.log(Level.WARNING, "Failed to set target server=" + this.targetServerId + " for " + this.player
/*  292 */                     .getName() + ".", iox);
/*      */               } 
/*  294 */               this.player.getCommunicator().sendReconnect(entry.EXTERNALIP, Integer.parseInt(entry.EXTERNALPORT), this.player
/*  295 */                   .getSaveFile().getPassword());
/*      */               
/*  297 */               logger.log(Level.INFO, "Command executed. Player redirected.");
/*  298 */               this.player.logoutIn(10, "Redirected");
/*      */             }
/*      */             else
/*      */             {
/*  302 */               (new LoginServerWebConnection(Servers.loginServer.id)).setCurrentServer(this.player.getName(), 
/*  303 */                   Servers.getLocalServerId());
/*      */               
/*  305 */               logger.log(Level.INFO, "Failed copy to target server for " + this.player
/*      */                   
/*  307 */                   .getName() + " took " + (
/*  308 */                   System.currentTimeMillis() - time) + " ms.");
/*  309 */               this.player.getCommunicator().sendAlertServerMessage("You can not transfer right now.");
/*      */             }
/*      */           
/*  312 */           } catch (Exception ex) {
/*      */             
/*  314 */             (new LoginServerWebConnection(Servers.loginServer.id)).setCurrentServer(this.player.getName(), 
/*  315 */                 Servers.getLocalServerId());
/*  316 */             this.player.getCommunicator().sendAlertServerMessage("An error occurred. You can not transfer right now.");
/*  317 */             logger.log(Level.WARNING, "Command executed. Failed to transfer player:" + ex.getMessage(), ex);
/*      */           } 
/*      */         }
/*  320 */         this.done = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  325 */     if (this.data == null) {
/*      */ 
/*      */       
/*  328 */       Wound[] wounds = new Wound[0];
/*  329 */       if (!this.toOrFromEpic)
/*      */       {
/*  331 */         if (this.player.getBody().getWounds() != null) {
/*  332 */           wounds = this.player.getBody().getWounds().getWounds();
/*      */         }
/*      */       }
/*      */       try {
/*  336 */         if (this.toOrFromEpic)
/*      */         {
/*  338 */           this.data = createPlayerData(this.player.getSaveFile(), this.player.getStatus(), this.player.getSkills().getSkillsNoTemp(), this.targetServerId, this.targetKingdomId, 
/*  339 */               System.currentTimeMillis());
/*      */         }
/*      */         else
/*      */         {
/*  343 */           this.data = createPlayerData(wounds, this.player.getSaveFile(), this.player.getStatus(), this.player.getAllItems(), this.player
/*  344 */               .getSkills().getSkillsNoTemp(), this.player.getDraggedItem(), this.targetServerId, 
/*  345 */               System.currentTimeMillis(), this.targetKingdomId);
/*      */         }
/*      */       
/*      */       }
/*  349 */       catch (IOException iox) {
/*      */         
/*  351 */         this.done = true;
/*      */       } 
/*      */     } 
/*  354 */     return this.done;
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
/*      */   public void reschedule(IntraClient aClient) {
/*  366 */     this.timeOutAt = System.currentTimeMillis() + (aClient.retryInSeconds * 1000);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove(IntraClient aClient) {
/*  377 */     this.timeOutAt = System.currentTimeMillis();
/*  378 */     this.done = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void commandExecuted(IntraClient aClient) {
/*  389 */     this.done = true;
/*  390 */     if (this.copiedToLoginServer) {
/*      */       
/*  392 */       ServerEntry entry = Servers.getServerWithId(this.targetServerId);
/*  393 */       if (entry != null) {
/*      */         try
/*      */         {
/*      */           
/*  397 */           if (this.player.getDraggedItem() != null) {
/*  398 */             Items.stopDragging(this.player.getDraggedItem());
/*      */           }
/*      */ 
/*      */           
/*  402 */           if (this.player.lastKingdom != 0) {
/*  403 */             this.player.getStatus().setKingdom(this.player.lastKingdom);
/*      */           }
/*  405 */           if (Servers.isThisLoginServer()) {
/*      */             
/*  407 */             (this.player.getSaveFile()).currentServer = this.targetServerId;
/*  408 */             (this.player.getSaveFile()).lastServer = (this.player.getSaveFile()).currentServer;
/*      */             
/*      */             try {
/*  411 */               this.player.getSaveFile().save();
/*  412 */               this.player.getCommunicator().sendReconnect(entry.EXTERNALIP, Integer.parseInt(entry.EXTERNALPORT), this.player
/*  413 */                   .getSaveFile().getPassword());
/*  414 */               logger2.log(Level.INFO, "PLT Command " + num + " executed. Player redirected.");
/*  415 */               this.player.logoutIn(10, "Redirected");
/*      */             }
/*  417 */             catch (IOException iox) {
/*      */               
/*  419 */               this.player.getCommunicator().sendAlertServerMessage("Failed to save your data. Not redirecting to the new server.");
/*      */               
/*  421 */               logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/*  426 */             this.player.getCommunicator().sendReconnect(entry.EXTERNALIP, Integer.parseInt(entry.EXTERNALPORT), this.player
/*  427 */                 .getSaveFile().getPassword());
/*      */ 
/*      */             
/*  430 */             this.player.logoutIn(10, "Redirected");
/*      */           } 
/*  432 */           logger2.log(Level.INFO, "PLT Command executed " + num);
/*      */         }
/*  434 */         catch (Exception ex)
/*      */         {
/*  436 */           logger.log(Level.WARNING, "Command executed. Failed to transfer player:" + ex.getMessage(), ex);
/*      */         }
/*      */       
/*      */       }
/*      */     } else {
/*      */       
/*  442 */       this.copiedToLoginServer = true;
/*      */       
/*  444 */       logger2.log(Level.INFO, "Command executed. Player copied to login server.");
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
/*      */   public void commandFailed(IntraClient aClient) {
/*  456 */     logger2.log(Level.INFO, "Command failed. " + num, new Exception());
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
/*      */   public void dataReceived(IntraClient aClient) {
/*      */     try {
/*  470 */       aClient.executePlayerTransferRequest(this.posX, this.posY, this.surfaced);
/*      */     }
/*  472 */     catch (IOException iox) {
/*      */       
/*  474 */       commandFailed(aClient);
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
/*      */   public static final void sendItem(Item item, DataOutputStream dos, boolean dragged) throws UnsupportedEncodingException, IOException {
/*      */     try {
/*  492 */       if (item.getTemplateId() == 1310) {
/*      */         
/*  494 */         dos.writeBoolean(true);
/*  495 */         long animalId = item.getData();
/*  496 */         Creature animal = Creatures.getInstance().getCreature(animalId);
/*  497 */         if (!CreatureDataStream.validateCreature(animal)) {
/*      */           
/*  499 */           dos.writeBoolean(false);
/*      */           return;
/*      */         } 
/*  502 */         CreatureDataStream.toStream(animal, dos);
/*      */         
/*  504 */         if (animal.getDominator() != null) {
/*      */           
/*  506 */           for (Player player : Players.getInstance().getPlayers()) {
/*      */             
/*  508 */             if (player.getPet() == animal)
/*      */             {
/*  510 */               player.setPet(-10L);
/*      */             }
/*      */           } 
/*  513 */           animal.setDominator(-10L);
/*      */         } 
/*      */         
/*  516 */         if (animal.isDominated())
/*      */         {
/*      */ 
/*      */           
/*  520 */           PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(animal.dominator);
/*  521 */           if (pinf != null)
/*      */           {
/*  523 */             pinf.pet = -10L;
/*  524 */             animal.setDominator(-10L);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  531 */         dos.writeBoolean(false);
/*      */       }
/*      */     
/*  534 */     } catch (NoSuchCreatureException noSuchCreatureException) {
/*      */       
/*  536 */       logger.log(Level.WARNING, "No creature found!!", noSuchCreatureException.getMessage());
/*  537 */       dos.writeBoolean(false);
/*      */     }
/*  539 */     catch (IOException iOException) {
/*      */       
/*  541 */       logger.log(Level.WARNING, iOException.getMessage());
/*      */     } 
/*      */     
/*  544 */     dos.writeBoolean(item.getLocked());
/*      */     
/*  546 */     dos.writeLong(item.getLockId());
/*      */     
/*  548 */     if (item.getLockId() != -10L)
/*      */     {
/*  550 */       if (item.isHollow()) {
/*      */ 
/*      */         
/*      */         try {
/*  554 */           Item lock = Items.getItem(item.getLockId());
/*  555 */           dos.writeBoolean(true);
/*  556 */           sendItem(lock, dos, dragged);
/*      */         }
/*  558 */         catch (NoSuchItemException ex) {
/*      */           
/*  560 */           dos.writeBoolean(false);
/*      */         } 
/*      */       } else {
/*      */         
/*  564 */         dos.writeBoolean(false);
/*      */       }  } 
/*  566 */     dos.writeLong(item.getWurmId());
/*  567 */     dos.writeBoolean(dragged);
/*  568 */     Effect[] effects = item.getEffects();
/*  569 */     dos.writeInt(effects.length);
/*      */     
/*  571 */     for (int e = 0; e < effects.length; e++) {
/*      */       
/*  573 */       dos.writeShort(effects[e].getType());
/*  574 */       dos.writeLong(effects[e].getStartTime());
/*      */     } 
/*  576 */     ItemSpellEffects effs = item.getSpellEffects();
/*  577 */     if (effs != null) {
/*      */       
/*  579 */       SpellEffect[] sparr = effs.getEffects();
/*  580 */       dos.writeInt(sparr.length);
/*  581 */       for (int x = 0; x < sparr.length; x++) {
/*      */         
/*  583 */         dos.writeLong((sparr[x]).id);
/*  584 */         dos.writeByte((sparr[x]).type);
/*  585 */         dos.writeFloat((sparr[x]).power);
/*  586 */         dos.writeInt((sparr[x]).timeleft);
/*      */       } 
/*      */     } else {
/*      */       
/*  590 */       dos.writeInt(0);
/*      */     } 
/*  592 */     long[] keys = item.getKeyIds();
/*  593 */     dos.writeInt(keys.length);
/*  594 */     for (int k = 0; k < keys.length; k++) {
/*  595 */       dos.writeLong(keys[k]);
/*      */     }
/*  597 */     dos.writeLong(item.lastOwner);
/*  598 */     if (item.isFarwalkerItem()) {
/*      */       
/*  600 */       dos.writeInt(-1);
/*  601 */       dos.writeInt(-1);
/*  602 */       dos.writeInt(-1);
/*  603 */       dos.writeInt(-1);
/*      */     }
/*      */     else {
/*      */       
/*  607 */       dos.writeInt(item.getData1());
/*  608 */       dos.writeInt(item.getData2());
/*  609 */       dos.writeInt(item.getExtra1());
/*  610 */       dos.writeInt(item.getExtra2());
/*      */     } 
/*  612 */     dos.writeUTF(item.getActualName());
/*  613 */     dos.writeUTF(item.getDescription());
/*      */     
/*  615 */     dos.writeLong(item.getOwnerId());
/*  616 */     dos.writeLong(item.getParentId());
/*  617 */     dos.writeLong(item.lastMaintained);
/*  618 */     dos.writeFloat(item.getQualityLevel());
/*  619 */     dos.writeFloat(item.getDamage());
/*  620 */     dos.writeFloat(item.getOriginalQualityLevel());
/*  621 */     dos.writeInt(item.getTemplateId());
/*  622 */     dos.writeInt(item.getWeightGrams(false));
/*  623 */     dos.writeShort(item.getPlace());
/*  624 */     dos.writeInt(item.getSizeX(false));
/*  625 */     dos.writeInt(item.getSizeY(false));
/*  626 */     dos.writeInt(item.getSizeZ(false));
/*  627 */     if (item.getBless() != null) {
/*  628 */       dos.writeInt((item.getBless()).number);
/*      */     } else {
/*  630 */       dos.writeInt(0);
/*  631 */     }  dos.writeByte(item.enchantment);
/*  632 */     dos.writeByte(item.getMaterial());
/*  633 */     dos.writeInt(item.getPrice());
/*  634 */     dos.writeShort(item.getTemperature());
/*  635 */     dos.writeBoolean(item.isBanked());
/*  636 */     dos.writeByte(item.getAuxData());
/*  637 */     dos.writeLong(item.creationDate);
/*  638 */     dos.writeByte(item.creationState);
/*  639 */     dos.writeInt(item.realTemplate);
/*  640 */     boolean hasMoreItems = false;
/*  641 */     if (item.isUnfinished())
/*      */     {
/*  643 */       if (item.getTemplateId() == 179) {
/*      */         
/*  645 */         Set<ItemRequirement> doneSet = ItemRequirement.getRequirements(item.getWurmId());
/*  646 */         if (doneSet != null) {
/*      */           
/*  648 */           int nums = doneSet.size();
/*  649 */           if (nums > 0) {
/*      */             
/*  651 */             hasMoreItems = true;
/*  652 */             dos.writeBoolean(true);
/*  653 */             dos.writeInt(nums);
/*  654 */             for (ItemRequirement next : doneSet) {
/*      */               
/*  656 */               dos.writeInt(next.getTemplateId());
/*  657 */               dos.writeInt(next.getNumsDone());
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  663 */     if (!hasMoreItems)
/*  664 */       dos.writeBoolean(false); 
/*  665 */     dos.writeBoolean(item.wornAsArmour);
/*  666 */     dos.writeBoolean(item.female);
/*  667 */     dos.writeBoolean(item.mailed);
/*  668 */     dos.writeByte(item.getMailTimes());
/*  669 */     dos.writeByte(item.getRarity());
/*  670 */     dos.writeLong(item.getBridgeId());
/*  671 */     dos.writeInt(item.getSettings().getPermissions());
/*  672 */     PermissionsByPlayer[] pbps = ItemSettings.getPermissionsPlayerList(item.getWurmId()).getPermissionsByPlayer();
/*  673 */     dos.writeInt(pbps.length);
/*  674 */     for (PermissionsByPlayer pbp : pbps) {
/*      */       
/*  676 */       dos.writeLong(pbp.getPlayerId());
/*  677 */       dos.writeInt(pbp.getSettings());
/*      */     } 
/*      */     
/*  680 */     boolean hasInscription = (item.canHaveInscription() && item.getInscription() != null && item.getInscription().hasBeenInscribed());
/*      */     
/*  682 */     dos.writeBoolean(hasInscription);
/*      */ 
/*      */     
/*  685 */     if (hasInscription) {
/*      */       
/*  687 */       InscriptionData id = item.getInscription();
/*  688 */       if (id.getInscription() == null) {
/*      */         
/*  690 */         id.setInscription("");
/*  691 */         logger.log(Level.WARNING, "Inscription was null for " + item.getWurmId());
/*      */       } 
/*      */       
/*  694 */       dos.writeUTF(id.getInscription());
/*  695 */       if (id.getInscriber() == null) {
/*      */         
/*  697 */         logger.log(Level.WARNING, "Inscriber was null for " + item.getWurmId());
/*  698 */         id.setInscriber("unknown");
/*      */       } 
/*  700 */       dos.writeUTF(id.getInscriber());
/*      */     } 
/*  702 */     dos.writeInt(item.color);
/*  703 */     dos.writeInt(item.color2);
/*  704 */     dos.writeUTF(item.creator);
/*      */     
/*  706 */     ItemMealData imd = ItemMealData.getItemMealData(item.getWurmId());
/*  707 */     if (imd == null) {
/*  708 */       dos.writeBoolean(false);
/*      */     } else {
/*      */       
/*  711 */       dos.writeBoolean(true);
/*  712 */       dos.writeShort(imd.getCalories());
/*  713 */       dos.writeShort(imd.getCarbs());
/*  714 */       dos.writeShort(imd.getFats());
/*  715 */       dos.writeShort(imd.getProteins());
/*  716 */       dos.writeByte(imd.getBonus());
/*  717 */       dos.writeByte(imd.getStages());
/*  718 */       dos.writeByte(imd.getIngredients());
/*  719 */       dos.writeShort(imd.getRecipeId());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sendSpellEffects(CreatureStatus status, DataOutputStream dos) throws IOException {
/*  730 */     if (status.spellEffects == null) {
/*  731 */       dos.writeInt(0);
/*      */     } else {
/*      */       
/*  734 */       SpellEffect[] sparr = status.spellEffects.getEffects();
/*  735 */       dos.writeInt(sparr.length);
/*  736 */       for (int x = 0; x < sparr.length; x++) {
/*      */         
/*  738 */         dos.writeLong((sparr[x]).id);
/*  739 */         dos.writeByte((sparr[x]).type);
/*  740 */         dos.writeFloat((sparr[x]).power);
/*  741 */         dos.writeInt((sparr[x]).timeleft);
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
/*      */   public static byte[] createPlayerData(PlayerInfo pinf, CreatureStatus status, Skill[] skills, int targServId, byte targetKingdomId, long clientTimeDifference) throws IOException {
/*  760 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*      */     
/*      */     try {
/*  763 */       DataOutputStream dos = new DataOutputStream(bos);
/*      */ 
/*      */       
/*  766 */       dos.writeBoolean(true);
/*      */ 
/*      */       
/*  769 */       dos.writeLong(pinf.getPlayerId());
/*  770 */       dos.writeUTF(pinf.getName());
/*  771 */       dos.writeUTF(pinf.getPassword());
/*  772 */       dos.writeUTF("");
/*  773 */       dos.writeUTF(pinf.emailAddress);
/*  774 */       dos.writeLong(0L);
/*  775 */       dos.writeByte((byte)pinf.getPower());
/*      */       
/*  777 */       dos.writeLong(pinf.money);
/*  778 */       dos.writeLong(pinf.getPaymentExpire());
/*  779 */       long[] ignored = pinf.getIgnored();
/*  780 */       dos.writeInt(ignored.length);
/*  781 */       for (int x = 0; x < ignored.length; x++)
/*  782 */         dos.writeLong(ignored[x]); 
/*  783 */       if (!pinf.hasLoadedFriends())
/*  784 */         pinf.loadFriends(pinf.getPlayerId()); 
/*  785 */       Friend[] friends = pinf.getFriends();
/*  786 */       dos.writeInt(friends.length);
/*  787 */       for (int i = 0; i < friends.length; i++) {
/*      */         
/*  789 */         dos.writeLong(friends[i].getFriendId());
/*  790 */         dos.writeByte(friends[i].getCatId());
/*      */       } 
/*      */       
/*  793 */       if (pinf.lastLogin > 0L) {
/*  794 */         dos.writeLong(pinf.playingTime + System.currentTimeMillis() - pinf.lastLogin);
/*      */       } else {
/*  796 */         dos.writeLong(pinf.playingTime);
/*  797 */       }  dos.writeLong(pinf.creationDate);
/*  798 */       dos.writeLong(pinf.lastWarned);
/*  799 */       dos.writeByte(targetKingdomId);
/*  800 */       dos.writeBoolean(pinf.isBanned());
/*  801 */       dos.writeLong(pinf.banexpiry);
/*  802 */       if (pinf.banreason == null) {
/*  803 */         dos.writeUTF("");
/*      */       } else {
/*  805 */         dos.writeUTF(pinf.banreason);
/*  806 */       }  dos.writeBoolean(pinf.isMute());
/*  807 */       dos.writeShort(pinf.muteTimes);
/*      */       
/*  809 */       dos.writeLong(pinf.muteexpiry);
/*  810 */       dos.writeUTF(pinf.mutereason);
/*  811 */       dos.writeBoolean(pinf.mayMute);
/*  812 */       dos.writeBoolean(pinf.overRideShop);
/*  813 */       dos.writeBoolean(pinf.isReimbursed());
/*  814 */       dos.writeInt(pinf.warnings);
/*  815 */       dos.writeBoolean(pinf.mayHearDevTalk);
/*  816 */       dos.writeUTF((pinf.getIpaddress() != null) ? pinf.getIpaddress() : "");
/*  817 */       dos.writeLong(pinf.version);
/*  818 */       dos.writeLong(pinf.referrer);
/*  819 */       dos.writeUTF(pinf.pwQuestion);
/*  820 */       dos.writeUTF(pinf.pwAnswer);
/*  821 */       dos.writeBoolean(pinf.logging);
/*  822 */       dos.writeBoolean(pinf.seesPlayerAssistantWindow());
/*  823 */       dos.writeBoolean(pinf.isPlayerAssistant());
/*  824 */       dos.writeBoolean(pinf.mayAppointPlayerAssistant());
/*  825 */       dos.writeLong(pinf.face);
/*  826 */       dos.writeByte(pinf.getBlood());
/*  827 */       dos.writeLong(pinf.flags);
/*  828 */       dos.writeLong(pinf.flags2);
/*  829 */       dos.write(pinf.getChaosKingdom());
/*  830 */       dos.write(pinf.undeadType);
/*  831 */       dos.writeInt(pinf.undeadKills);
/*  832 */       dos.writeInt(pinf.undeadPlayerKills);
/*  833 */       dos.writeInt(pinf.undeadPlayerSeconds);
/*  834 */       dos.writeLong(pinf.getLastResetEarningsCounter());
/*  835 */       dos.writeLong(pinf.getMoneyEarnedBySellingLastHour());
/*  836 */       dos.writeLong(pinf.getMoneyEarnedBySellingEver());
/*      */ 
/*      */       
/*  839 */       if (pinf.awards != null) {
/*      */         
/*  841 */         dos.writeBoolean(true);
/*  842 */         dos.writeInt(pinf.awards.getDaysPrem());
/*  843 */         dos.writeLong(pinf.awards.getLastTickedDay());
/*  844 */         dos.writeInt(pinf.awards.getMonthsPaidEver());
/*  845 */         dos.writeInt(pinf.awards.getMonthsPaidInARow());
/*  846 */         dos.writeInt(pinf.awards.getMonthsPaidSinceReset());
/*  847 */         dos.writeInt(pinf.awards.getSilversPaidEver());
/*  848 */         dos.writeInt(pinf.awards.getCurrentLoyalty());
/*  849 */         dos.writeInt(pinf.awards.getTotalLoyalty());
/*      */       } else {
/*      */         
/*  852 */         dos.writeBoolean(false);
/*      */       } 
/*      */       
/*  855 */       dos.writeByte(status.getSex());
/*      */       
/*  857 */       if (Servers.localServer.entryServer) {
/*      */         
/*  859 */         dos.writeInt(targServId);
/*  860 */         dos.writeByte(0);
/*      */       }
/*      */       else {
/*      */         
/*  864 */         dos.writeInt(Servers.localServer.id);
/*  865 */         dos.writeByte(status.kingdom);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  871 */       dos.writeInt(skills.length);
/*  872 */       for (int s = 0; s < skills.length; s++) {
/*      */         
/*  874 */         double actualKnowledge = skills[s].getKnowledge();
/*  875 */         double actualMin = (skills[s]).minimum;
/*      */ 
/*      */         
/*  878 */         if (pinf.realdeath > 0)
/*      */         {
/*  880 */           switch (skills[s].getNumber()) {
/*      */             
/*      */             case 10066:
/*      */             case 10067:
/*      */             case 10068:
/*  885 */               if (skills[s].getNumber() == 10067) {
/*  886 */                 actualKnowledge = Math.max(pinf.champChanneling, actualKnowledge - 50.0D);
/*      */               } else {
/*  888 */                 actualKnowledge = Math.max(10.0D, actualKnowledge - 50.0D);
/*  889 */               }  actualMin = Math.max(actualKnowledge, actualMin - 50.0D);
/*      */               break;
/*      */             case 100:
/*      */             case 101:
/*      */             case 102:
/*      */             case 103:
/*      */             case 104:
/*      */             case 105:
/*      */             case 106:
/*  898 */               actualKnowledge -= 6.0D;
/*  899 */               actualMin -= 6.0D;
/*      */               break;
/*      */           } 
/*      */         }
/*  903 */         dos.writeLong((skills[s]).id);
/*  904 */         dos.writeInt(skills[s].getNumber());
/*  905 */         dos.writeDouble(actualKnowledge);
/*  906 */         dos.writeDouble(actualMin);
/*  907 */         dos.writeLong((skills[s]).lastUsed);
/*      */       } 
/*      */       
/*  910 */       sendAchievements(pinf.getPlayerId(), dos);
/*  911 */       RecipesByPlayer.packRecipes(dos, pinf.getPlayerId());
/*  912 */       sendPMList(pinf, dos);
/*  913 */       dos.flush();
/*  914 */       IoUtilities.closeClosable(dos);
/*      */     }
/*  916 */     catch (Exception ex) {
/*      */       
/*  918 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/*  920 */     byte[] barr = bos.toByteArray();
/*      */     
/*  922 */     return barr;
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
/*      */   public static byte[] createPlayerData(Wound[] wounds, PlayerInfo pinf, CreatureStatus status, Item[] items, Skill[] skills, @Nullable Item draggedItem, int targServId, long clientTimeDifference, byte targetKingdom) throws IOException {
/*  945 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*      */     
/*      */     try {
/*  948 */       DataOutputStream dos = new DataOutputStream(bos);
/*      */ 
/*      */       
/*  951 */       dos.writeBoolean(false);
/*      */ 
/*      */       
/*  954 */       dos.writeLong(pinf.getPlayerId());
/*  955 */       dos.writeInt(wounds.length);
/*  956 */       for (int w = 0; w < wounds.length; w++) {
/*      */         
/*  958 */         dos.writeLong(wounds[w].getWurmId());
/*  959 */         dos.writeByte(wounds[w].getType());
/*  960 */         dos.writeByte(wounds[w].getLocation());
/*  961 */         dos.writeFloat(wounds[w].getSeverity());
/*  962 */         dos.writeFloat(wounds[w].getPoisonSeverity());
/*  963 */         dos.writeFloat(wounds[w].getInfectionSeverity());
/*  964 */         dos.writeBoolean(wounds[w].isBandaged());
/*  965 */         dos.writeLong(wounds[w].getLastPolled());
/*  966 */         dos.writeByte(wounds[w].getHealEff());
/*      */       } 
/*  968 */       dos.writeUTF(pinf.getName());
/*  969 */       dos.writeUTF(pinf.getPassword());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  975 */       dos.writeUTF("");
/*  976 */       dos.writeUTF(pinf.emailAddress);
/*      */       
/*  978 */       dos.writeLong(0L);
/*  979 */       dos.writeByte((byte)pinf.getPower());
/*  980 */       if (pinf.getDeity() != null) {
/*  981 */         dos.writeByte((byte)(pinf.getDeity()).number);
/*      */       } else {
/*  983 */         dos.writeByte(0);
/*  984 */       }  dos.writeFloat(pinf.getAlignment());
/*  985 */       dos.writeFloat(pinf.getFaith());
/*  986 */       dos.writeFloat(pinf.getFavor());
/*      */       
/*  988 */       if (pinf.getGod() != null) {
/*  989 */         dos.writeByte((byte)(pinf.getGod()).number);
/*      */       } else {
/*  991 */         dos.writeByte(0);
/*  992 */       }  dos.writeByte(pinf.realdeath);
/*  993 */       dos.writeLong(pinf.lastChangedDeity);
/*  994 */       dos.writeInt(pinf.fatigueSecsLeft);
/*  995 */       dos.writeInt(pinf.fatigueSecsToday);
/*  996 */       dos.writeInt(pinf.fatigueSecsYesterday);
/*  997 */       dos.writeLong(pinf.lastFatigue);
/*  998 */       dos.writeLong(pinf.lastWarned);
/*  999 */       dos.writeLong(pinf.lastCheated);
/*      */       
/* 1001 */       dos.writeLong(pinf.plantedSign);
/* 1002 */       if (pinf.lastLogin > 0L) {
/* 1003 */         dos.writeLong(pinf.playingTime + System.currentTimeMillis() - pinf.lastLogin);
/*      */       } else {
/* 1005 */         dos.writeLong(pinf.playingTime);
/* 1006 */       }  dos.writeLong(pinf.creationDate);
/* 1007 */       if (Servers.localServer.entryServer && pinf.getPower() <= 0) {
/* 1008 */         dos.writeByte(targetKingdom);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1013 */         dos.writeByte(status.kingdom);
/* 1014 */       }  dos.writeBoolean(pinf.votedKing);
/* 1015 */       dos.writeInt(pinf.getRank());
/* 1016 */       dos.writeInt(pinf.getMaxRank());
/* 1017 */       dos.writeLong(pinf.lastModifiedRank);
/* 1018 */       dos.writeBoolean(pinf.isBanned());
/* 1019 */       dos.writeLong(pinf.banexpiry);
/* 1020 */       if (pinf.banreason == null) {
/* 1021 */         dos.writeUTF("");
/*      */       } else {
/* 1023 */         dos.writeUTF(pinf.banreason);
/* 1024 */       }  dos.writeShort(pinf.muteTimes);
/* 1025 */       dos.writeBoolean(pinf.isReimbursed());
/* 1026 */       dos.writeInt(pinf.warnings);
/* 1027 */       dos.writeBoolean(pinf.mayHearDevTalk);
/* 1028 */       dos.writeLong(pinf.getPaymentExpire());
/* 1029 */       long[] ignored = pinf.getIgnored();
/* 1030 */       dos.writeInt(ignored.length);
/* 1031 */       for (int x = 0; x < ignored.length; x++)
/* 1032 */         dos.writeLong(ignored[x]); 
/* 1033 */       if (!pinf.hasLoadedFriends())
/* 1034 */         pinf.loadFriends(pinf.getPlayerId()); 
/* 1035 */       Friend[] friends = pinf.getFriends();
/* 1036 */       dos.writeInt(friends.length);
/* 1037 */       for (int i = 0; i < friends.length; i++) {
/*      */         
/* 1039 */         dos.writeLong(friends[i].getFriendId());
/* 1040 */         dos.writeByte(friends[i].getCatId());
/*      */       } 
/*      */       
/* 1043 */       dos.writeUTF((pinf.getIpaddress() != null) ? pinf.getIpaddress() : "");
/*      */       
/* 1045 */       dos.writeLong(pinf.version);
/* 1046 */       dos.writeBoolean(pinf.dead);
/* 1047 */       dos.writeBoolean(pinf.isMute());
/* 1048 */       dos.writeLong(pinf.lastFaith);
/* 1049 */       dos.writeByte(pinf.numFaith);
/* 1050 */       dos.writeLong(pinf.money);
/* 1051 */       dos.writeBoolean(pinf.climbing);
/* 1052 */       dos.writeByte((byte)pinf.getChangedKingdom());
/* 1053 */       dos.writeLong(pinf.face);
/* 1054 */       dos.writeByte(pinf.getBlood());
/* 1055 */       dos.writeLong(pinf.flags);
/* 1056 */       dos.writeLong(pinf.flags2);
/* 1057 */       dos.writeLong(pinf.abilities);
/* 1058 */       dos.writeInt(pinf.scenarioKarma);
/*      */       
/* 1060 */       dos.writeInt(pinf.abilityTitle);
/* 1061 */       dos.write(pinf.getChaosKingdom());
/*      */       
/* 1063 */       dos.write(pinf.undeadType);
/* 1064 */       dos.writeInt(pinf.undeadKills);
/* 1065 */       dos.writeInt(pinf.undeadPlayerKills);
/* 1066 */       dos.writeInt(pinf.undeadPlayerSeconds);
/* 1067 */       dos.writeLong(pinf.getLastResetEarningsCounter());
/* 1068 */       dos.writeLong(pinf.getMoneyEarnedBySellingLastHour());
/* 1069 */       dos.writeLong(pinf.getMoneyEarnedBySellingEver());
/*      */       
/* 1071 */       if (pinf.awards != null) {
/*      */         
/* 1073 */         dos.writeBoolean(true);
/* 1074 */         dos.writeInt(pinf.awards.getDaysPrem());
/* 1075 */         dos.writeLong(pinf.awards.getLastTickedDay());
/* 1076 */         dos.writeInt(pinf.awards.getMonthsPaidEver());
/* 1077 */         dos.writeInt(pinf.awards.getMonthsPaidInARow());
/* 1078 */         dos.writeInt(pinf.awards.getMonthsPaidSinceReset());
/* 1079 */         dos.writeInt(pinf.awards.getSilversPaidEver());
/* 1080 */         dos.writeInt(pinf.awards.getCurrentLoyalty());
/* 1081 */         dos.writeInt(pinf.awards.getTotalLoyalty());
/*      */       } else {
/*      */         
/* 1084 */         dos.writeBoolean(false);
/*      */       } 
/* 1086 */       dos.writeShort(pinf.getHotaWins());
/* 1087 */       dos.writeBoolean(pinf.hasFreeTransfer);
/* 1088 */       dos.writeInt(pinf.reputation);
/* 1089 */       dos.writeLong(pinf.lastPolledReputation);
/* 1090 */       dos.writeLong(pinf.pet);
/* 1091 */       dos.writeLong(pinf.nicotineAddiction);
/* 1092 */       dos.writeLong(pinf.alcoholAddiction);
/* 1093 */       dos.writeFloat(pinf.nicotine);
/* 1094 */       dos.writeFloat(pinf.alcohol);
/* 1095 */       dos.writeBoolean(pinf.logging);
/* 1096 */       if (pinf.title != null) {
/* 1097 */         dos.writeInt(pinf.title.id);
/*      */       } else {
/* 1099 */         dos.writeInt(0);
/* 1100 */       }  if (pinf.secondTitle != null) {
/* 1101 */         dos.writeInt(pinf.secondTitle.id);
/*      */       } else {
/* 1103 */         dos.writeInt(0);
/* 1104 */       }  Titles.Title[] titles = pinf.getTitles();
/* 1105 */       dos.writeInt(titles.length);
/* 1106 */       for (int j = 0; j < titles.length; j++)
/* 1107 */         dos.writeInt((titles[j]).id); 
/* 1108 */       dos.writeLong(pinf.muteexpiry);
/* 1109 */       dos.writeUTF(pinf.mutereason);
/* 1110 */       dos.writeBoolean(pinf.mayMute);
/* 1111 */       dos.writeBoolean(pinf.overRideShop);
/*      */       
/* 1113 */       dos.writeInt(targServId);
/* 1114 */       dos.writeInt(pinf.currentServer);
/* 1115 */       dos.writeLong(pinf.referrer);
/* 1116 */       dos.writeUTF(pinf.pwQuestion);
/* 1117 */       dos.writeUTF(pinf.pwAnswer);
/* 1118 */       dos.writeBoolean(pinf.isPriest);
/* 1119 */       if (pinf.isPriest) {
/*      */         
/* 1121 */         dos.writeByte(pinf.priestType);
/* 1122 */         dos.writeLong(pinf.lastChangedPriestType);
/*      */       } 
/* 1124 */       dos.writeLong(pinf.bed);
/* 1125 */       dos.writeInt(pinf.sleep);
/* 1126 */       dos.writeBoolean(pinf.isTheftWarned);
/* 1127 */       dos.writeBoolean(pinf.noReimbursementLeft);
/* 1128 */       dos.writeBoolean(pinf.deathProtected);
/* 1129 */       dos.writeByte(pinf.fightmode);
/* 1130 */       dos.writeLong(pinf.nextAffinity);
/* 1131 */       dos.writeInt(pinf.tutorialLevel);
/* 1132 */       dos.writeBoolean(pinf.autoFighting);
/* 1133 */       dos.writeLong(pinf.appointments);
/* 1134 */       dos.writeBoolean(pinf.seesPlayerAssistantWindow());
/* 1135 */       dos.writeBoolean(pinf.isPlayerAssistant());
/* 1136 */       dos.writeBoolean(pinf.mayAppointPlayerAssistant());
/* 1137 */       dos.writeLong(pinf.lastChangedKindom);
/* 1138 */       dos.writeLong(pinf.championTimeStamp);
/* 1139 */       dos.writeShort(pinf.championPoints);
/* 1140 */       dos.writeFloat(pinf.champChanneling);
/* 1141 */       if (Servers.localServer.entryServer) {
/*      */         
/* 1143 */         dos.writeByte(0);
/* 1144 */         dos.writeInt(targServId);
/*      */       }
/*      */       else {
/*      */         
/* 1148 */         dos.write(pinf.epicKingdom);
/* 1149 */         dos.writeInt(pinf.epicServerId);
/*      */       } 
/* 1151 */       dos.writeInt(pinf.getKarma());
/* 1152 */       dos.writeInt(pinf.getMaxKarma());
/* 1153 */       dos.writeInt(pinf.getTotalKarma());
/*      */       
/* 1155 */       dos.writeUTF(status.getTemplate().getName());
/* 1156 */       dos.writeShort(status.getBody().getCentimetersHigh());
/* 1157 */       dos.writeShort(status.getBody().getCentimetersLong());
/* 1158 */       dos.writeShort(status.getBody().getCentimetersWide());
/* 1159 */       dos.writeFloat(status.getRotation());
/* 1160 */       dos.writeLong(status.getBodyId());
/* 1161 */       dos.writeLong(status.getBuildingId());
/* 1162 */       dos.writeInt(status.damage);
/* 1163 */       dos.writeInt(status.getHunger());
/* 1164 */       dos.writeInt((int)status.getStunned());
/* 1165 */       dos.writeInt(status.getThirst());
/* 1166 */       dos.writeInt(status.getStamina());
/* 1167 */       dos.writeFloat(status.getNutritionlevel());
/* 1168 */       dos.writeByte(status.getSex());
/* 1169 */       dos.writeLong(status.getInventoryId());
/* 1170 */       dos.writeBoolean(status.isOnSurface());
/* 1171 */       dos.writeBoolean(status.isUnconscious());
/* 1172 */       dos.writeInt(status.age);
/* 1173 */       dos.writeLong(status.lastPolledAge);
/* 1174 */       dos.writeByte(status.fat);
/* 1175 */       dos.writeShort((short)status.getDetectInvisCounter());
/* 1176 */       dos.write(status.disease);
/* 1177 */       dos.writeFloat(status.getCalories());
/* 1178 */       dos.writeFloat(status.getCarbs());
/* 1179 */       dos.writeFloat(status.getFats());
/* 1180 */       dos.writeFloat(status.getProteins());
/*      */ 
/*      */       
/* 1183 */       Cultist cultist = Cultist.getCultist(pinf.getPlayerId());
/* 1184 */       if (cultist != null) {
/*      */         
/* 1186 */         dos.writeBoolean(true);
/* 1187 */         dos.writeByte(cultist.getLevel());
/* 1188 */         dos.writeByte(cultist.getPath());
/* 1189 */         dos.writeLong(cultist.getLastMeditated());
/* 1190 */         dos.writeLong(cultist.getLastReceivedLevel());
/* 1191 */         dos.writeLong(cultist.getLastAppointedLevel());
/* 1192 */         dos.writeLong(cultist.getCooldown1());
/* 1193 */         dos.writeLong(cultist.getCooldown2());
/* 1194 */         dos.writeLong(cultist.getCooldown3());
/* 1195 */         dos.writeLong(cultist.getCooldown4());
/* 1196 */         dos.writeLong(cultist.getCooldown5());
/* 1197 */         dos.writeLong(cultist.getCooldown6());
/* 1198 */         dos.writeLong(cultist.getCooldown7());
/* 1199 */         dos.writeByte(cultist.getSkillgainCount());
/*      */       } else {
/*      */         
/* 1202 */         dos.writeBoolean(false);
/* 1203 */       }  dos.writeLong(pinf.getLastChangedPath());
/*      */       
/* 1205 */       dos.writeLong(Puppet.getLastPuppeteered(pinf.getPlayerId()));
/*      */ 
/*      */       
/* 1208 */       Cooldowns cd = Cooldowns.getCooldownsFor(pinf.getPlayerId(), false);
/* 1209 */       if (cd != null) {
/*      */         
/* 1211 */         dos.writeInt(cd.cooldowns.size());
/* 1212 */         if (cd.cooldowns.size() > 0)
/*      */         {
/* 1214 */           for (Map.Entry<Integer, Long> ent : (Iterable<Map.Entry<Integer, Long>>)cd.cooldowns.entrySet()) {
/*      */             
/* 1216 */             dos.writeInt(((Integer)ent.getKey()).intValue());
/* 1217 */             dos.writeLong(((Long)ent.getValue()).longValue());
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/* 1222 */         dos.writeInt(0);
/*      */       } 
/*      */       
/* 1225 */       int numItems = 0; int k;
/* 1226 */       for (k = 0; k < items.length; k++) {
/*      */         
/* 1228 */         if (!(items[k]).transferred && !items[k].isBodyPart() && !items[k].isTemporary()) {
/* 1229 */           numItems++;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1237 */       dos.writeInt(numItems);
/* 1238 */       for (k = 0; k < items.length; k++) {
/*      */         
/* 1240 */         if (!(items[k]).transferred && !items[k].isBodyPart() && !items[k].isTemporary()) {
/* 1241 */           sendItem(items[k], dos, false);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1248 */       dos.writeInt(skills.length);
/* 1249 */       for (int s = 0; s < skills.length; s++) {
/*      */         
/* 1251 */         dos.writeLong((skills[s]).id);
/* 1252 */         dos.writeInt(skills[s].getNumber());
/* 1253 */         dos.writeDouble(skills[s].getKnowledge());
/* 1254 */         dos.writeDouble((skills[s]).minimum);
/* 1255 */         dos.writeLong((skills[s]).lastUsed);
/*      */       } 
/* 1257 */       Affinity[] affs = Affinities.getAffinities(pinf.getPlayerId());
/* 1258 */       dos.writeInt(affs.length);
/* 1259 */       for (int xa = 0; xa < affs.length; xa++) {
/*      */         
/* 1261 */         dos.writeInt((affs[xa]).skillNumber);
/* 1262 */         dos.write((byte)(affs[xa]).number);
/*      */       } 
/* 1264 */       sendSpellEffects(status, dos);
/* 1265 */       sendAchievements(pinf.getPlayerId(), dos);
/* 1266 */       RecipesByPlayer.packRecipes(dos, pinf.getPlayerId());
/* 1267 */       sendPMList(pinf, dos);
/* 1268 */       sendPrivateMapAnnotations(pinf.getPlayerId(), dos);
/* 1269 */       dos.flush();
/* 1270 */       IoUtilities.closeClosable(dos);
/*      */     }
/* 1272 */     catch (Exception ex) {
/*      */       
/* 1274 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */     } 
/* 1276 */     byte[] barr = bos.toByteArray();
/*      */     
/* 1278 */     return barr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void sendPrivateMapAnnotations(long playerId, DataOutputStream dos) throws IOException {
/*      */     try {
/* 1285 */       Player player = Players.getInstance().getPlayer(playerId);
/* 1286 */       Set<MapAnnotation> annos = player.getPrivateMapAnnotations();
/* 1287 */       if (annos.size() > 0) {
/*      */         
/* 1289 */         dos.writeBoolean(true);
/* 1290 */         dos.writeInt(annos.size());
/* 1291 */         for (MapAnnotation anno : annos) {
/*      */           
/* 1293 */           dos.writeLong(anno.getId());
/* 1294 */           dos.writeByte(anno.getType());
/* 1295 */           dos.writeUTF(anno.getName());
/* 1296 */           dos.writeUTF(anno.getServer());
/* 1297 */           dos.writeLong(anno.getPosition());
/* 1298 */           dos.writeLong(anno.getOwnerId());
/* 1299 */           dos.writeByte(anno.getIcon());
/*      */         } 
/*      */       } else {
/*      */         
/* 1303 */         dos.writeBoolean(false);
/*      */       } 
/* 1305 */     } catch (NoSuchPlayerException nsp) {
/*      */       
/* 1307 */       logger.log(Level.WARNING, "Unable to find the player during transfer, no map annotations will be sent. " + nsp
/* 1308 */           .getMessage(), (Throwable)nsp);
/* 1309 */       dos.writeBoolean(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void sendAchievements(long wurmid, DataOutputStream dos) throws IOException {
/* 1315 */     Achievement[] myAchievements = Achievements.getAchievements(wurmid);
/* 1316 */     Set<AchievementTemplate> templatesToSend = new HashSet<>(); int x;
/* 1317 */     for (x = 0; x < myAchievements.length; x++) {
/*      */       
/* 1319 */       if (myAchievements[x].getTemplate().getType() == 2)
/* 1320 */         templatesToSend.add(myAchievements[x].getTemplate()); 
/*      */     } 
/* 1322 */     dos.writeInt(templatesToSend.size());
/* 1323 */     for (AchievementTemplate template : templatesToSend) {
/*      */       
/* 1325 */       dos.writeInt(template.getNumber());
/* 1326 */       dos.writeUTF(template.getName());
/* 1327 */       dos.writeUTF(template.getDescription());
/* 1328 */       dos.writeUTF(template.getCreator());
/*      */     } 
/* 1330 */     dos.writeInt(myAchievements.length);
/* 1331 */     for (x = 0; x < myAchievements.length; x++) {
/*      */       
/* 1333 */       dos.writeInt(myAchievements[x].getAchievement());
/* 1334 */       dos.writeInt(myAchievements[x].getCounter());
/* 1335 */       dos.writeLong(myAchievements[x].getDateAchieved().getTime());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void sendPMList(PlayerInfo pinf, DataOutputStream dos) throws IOException {
/* 1341 */     ConcurrentHashMap<String, Long> targetPMIds = pinf.getAllTargetPMIds();
/* 1342 */     int theCount = targetPMIds.size();
/* 1343 */     dos.writeInt(theCount);
/* 1344 */     for (Map.Entry<String, Long> x : targetPMIds.entrySet()) {
/*      */ 
/*      */       
/* 1347 */       dos.writeUTF(x.getKey());
/* 1348 */       dos.writeLong(((Long)x.getValue()).longValue());
/*      */     } 
/*      */     
/* 1351 */     long sessionFlags = pinf.getSessionFlags();
/* 1352 */     dos.writeLong(sessionFlags);
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
/*      */   public void receivingData(ByteBuffer buffer) {
/* 1364 */     this.loginServerVersion = buffer.getLong();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\PlayerTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */