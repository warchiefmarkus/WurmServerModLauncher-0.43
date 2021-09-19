/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.FailedException;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.Wagoner;
/*      */ import com.wurmonline.server.economy.Economy;
/*      */ import com.wurmonline.server.economy.MonetaryConstants;
/*      */ import com.wurmonline.server.economy.Shop;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.tutorial.MissionTargets;
/*      */ import com.wurmonline.server.villages.Citizen;
/*      */ import com.wurmonline.server.villages.NoSuchRoleException;
/*      */ import com.wurmonline.server.villages.NoSuchVillageException;
/*      */ import com.wurmonline.server.villages.Village;
/*      */ import com.wurmonline.server.villages.VillageRole;
/*      */ import com.wurmonline.server.villages.VillageStatus;
/*      */ import com.wurmonline.server.villages.Villages;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.util.MaterialUtilities;
/*      */ import java.io.IOException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.logging.FileHandler;
/*      */ import java.util.logging.Handler;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.logging.SimpleFormatter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class TradingWindow
/*      */   implements MiscConstants, ItemTypes, VillageStatus, CreatureTypes, MonetaryConstants
/*      */ {
/*      */   private final Creature windowowner;
/*      */   private final Creature watcher;
/*      */   private final boolean offer;
/*      */   private final long wurmId;
/*      */   private Set<Item> items;
/*      */   private final Trade trade;
/*   77 */   private static final Logger logger = Logger.getLogger(TradingWindow.class.getName());
/*   78 */   private static final Map<String, Logger> loggers = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TradingWindow(Creature aOwner, Creature aWatcher, boolean aOffer, long aWurmId, Trade aTrade) {
/*   85 */     this.windowowner = aOwner;
/*   86 */     this.watcher = aWatcher;
/*   87 */     this.offer = aOffer;
/*   88 */     this.wurmId = aWurmId;
/*   89 */     this.trade = aTrade;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void stopLoggers() {
/*   94 */     for (Logger logger : loggers.values()) {
/*      */       
/*   96 */       if (logger != null) {
/*   97 */         for (Handler h : logger.getHandlers())
/*      */         {
/*   99 */           h.close();
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Logger getLogger(long wurmid) {
/*  106 */     String name = "trader" + wurmid;
/*  107 */     Logger personalLogger = loggers.get(name);
/*  108 */     if (personalLogger == null) {
/*      */       
/*  110 */       personalLogger = Logger.getLogger(name);
/*  111 */       personalLogger.setUseParentHandlers(false);
/*  112 */       Handler[] h = logger.getHandlers();
/*  113 */       for (int i = 0; i != h.length; i++)
/*      */       {
/*  115 */         personalLogger.removeHandler(h[i]);
/*      */       }
/*      */       
/*      */       try {
/*  119 */         FileHandler fh = new FileHandler(name + ".log", 0, 1, true);
/*  120 */         fh.setFormatter(new SimpleFormatter());
/*  121 */         personalLogger.addHandler(fh);
/*      */       }
/*  123 */       catch (IOException ie) {
/*      */         
/*  125 */         Logger.getLogger(name).log(Level.WARNING, name + ":no redirection possible!");
/*      */       } 
/*  127 */       loggers.put(name, personalLogger);
/*      */     } 
/*  129 */     return personalLogger;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayMoveItemToWindow(Item item, Creature creature, long window) {
/*  134 */     boolean toReturn = false;
/*  135 */     if (this.wurmId == 3L) {
/*      */       
/*  137 */       if (window == 1L) {
/*  138 */         toReturn = true;
/*      */       }
/*  140 */     } else if (this.wurmId == 4L) {
/*      */       
/*  142 */       if (window == 2L) {
/*  143 */         toReturn = true;
/*      */       }
/*  145 */     } else if (this.wurmId == 2L) {
/*      */       
/*  147 */       if (!this.windowowner.equals(creature)) {
/*      */         
/*  149 */         if (creature.isPlayer() && item.isCoin() && !this.windowowner.isPlayer())
/*  150 */           return false; 
/*  151 */         if (window == 4L) {
/*  152 */           toReturn = true;
/*      */         }
/*      */       } 
/*  155 */     } else if (this.wurmId == 1L) {
/*      */       
/*  157 */       if (!this.windowowner.equals(creature) && window == 3L)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  162 */         if (this.watcher == creature && item.getOwnerId() == this.windowowner.getWurmId())
/*  163 */           toReturn = true; 
/*      */       }
/*      */     } 
/*  166 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayAddFromInventory(Creature creature, Item item) {
/*  171 */     if (!item.isTraded())
/*      */     {
/*  173 */       if (item.isNoTrade()) {
/*      */         
/*  175 */         creature.getCommunicator().sendSafeServerMessage(item.getNameWithGenus() + " is not tradable.");
/*      */ 
/*      */       
/*      */       }
/*  179 */       else if (this.windowowner.equals(creature)) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/*  186 */           long owneri = item.getOwner();
/*  187 */           if (owneri != this.watcher.getWurmId() && owneri != this.windowowner.getWurmId()) {
/*  188 */             this.windowowner.setCheated("Traded " + item.getName() + "[" + item.getWurmId() + "] with " + this.watcher
/*  189 */                 .getName() + " owner=" + owneri);
/*      */           }
/*  191 */         } catch (NotOwnedException not) {
/*      */           
/*  193 */           this.windowowner.setCheated("Traded " + item.getName() + "[" + item.getWurmId() + "] with " + this.watcher
/*  194 */               .getName() + " not owned?");
/*      */         } 
/*      */         
/*  197 */         if (this.wurmId == 2L || this.wurmId == 1L) {
/*      */           
/*  199 */           if (item.isHollow()) {
/*      */             
/*  201 */             Item[] its = item.getAllItems(true);
/*  202 */             for (Item lIt : its) {
/*      */               
/*  204 */               if (lIt.isNoTrade() || lIt.isVillageDeed() || lIt.isHomesteadDeed() || lIt.getTemplateId() == 781) {
/*      */                 
/*  206 */                 creature.getCommunicator().sendSafeServerMessage(item
/*  207 */                     .getNameWithGenus() + " contains a non-tradable item.");
/*  208 */                 return false;
/*      */               } 
/*      */             } 
/*      */           } 
/*  212 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  217 */     return false;
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
/*      */   public long getWurmId() {
/*  257 */     return this.wurmId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item[] getItems() {
/*  266 */     if (this.items != null) {
/*  267 */       return this.items.<Item>toArray(new Item[this.items.size()]);
/*      */     }
/*  269 */     return new Item[0];
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeExistingContainedItems(Item item) {
/*  274 */     if (item.isHollow()) {
/*      */       
/*  276 */       Item[] itemarr = item.getItemsAsArray();
/*  277 */       for (Item lElement : itemarr) {
/*      */         
/*  279 */         removeExistingContainedItems(lElement);
/*  280 */         if (lElement.getTradeWindow() == this) {
/*  281 */           removeFromTrade(lElement, false);
/*  282 */         } else if (lElement.getTradeWindow() != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  287 */           lElement.getTradeWindow().removeItem(lElement);
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
/*      */   public Item[] getAllItems() {
/*  303 */     if (this.items != null) {
/*      */       
/*  305 */       Set<Item> toRet = new HashSet<>();
/*  306 */       for (Item item : this.items) {
/*      */         
/*  308 */         toRet.add(item);
/*  309 */         Item[] toAdd = item.getAllItems(false);
/*  310 */         for (Item lElement : toAdd) {
/*      */           
/*  312 */           if (lElement.tradeWindow == this)
/*  313 */             toRet.add(lElement); 
/*      */         } 
/*      */       } 
/*  316 */       return toRet.<Item>toArray(new Item[toRet.size()]);
/*      */     } 
/*      */     
/*  319 */     return new Item[0];
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
/*      */   public void stopReceivingItems() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void startReceivingItems() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addItem(Item item) {
/*  398 */     if (this.items == null)
/*  399 */       this.items = new HashSet<>(); 
/*  400 */     if (item.tradeWindow == null) {
/*      */       
/*  402 */       removeExistingContainedItems(item);
/*  403 */       Item parent = item;
/*      */       
/*      */       try {
/*  406 */         parent = item.getParent();
/*      */       }
/*  408 */       catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */       
/*  412 */       this.items.add(item);
/*  413 */       addToTrade(item, parent);
/*  414 */       if (item == parent || parent.isViewableBy(this.windowowner))
/*      */       {
/*  416 */         if (!this.windowowner.isPlayer()) {
/*      */ 
/*      */ 
/*      */           
/*  420 */           this.windowowner.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  421 */               .getWurmId() : 0L, 0);
/*      */         }
/*  423 */         else if (!this.watcher.isPlayer()) {
/*      */ 
/*      */           
/*  426 */           this.windowowner.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  427 */               .getWurmId() : 0L, this.watcher
/*  428 */               .getTradeHandler().getTraderBuyPriceForItem(item));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  433 */           this.windowowner.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  434 */               .getWurmId() : 0L, item.getPrice());
/*      */         } 
/*      */       }
/*  437 */       if (item == parent || parent.isViewableBy(this.watcher))
/*      */       {
/*  439 */         if (!this.watcher.isPlayer()) {
/*      */ 
/*      */ 
/*      */           
/*  443 */           this.watcher.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  444 */               .getWurmId() : 0L, 0);
/*      */         }
/*  446 */         else if (!this.windowowner.isPlayer()) {
/*      */ 
/*      */           
/*  449 */           this.watcher.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  450 */               .getWurmId() : 0L, this.windowowner
/*  451 */               .getTradeHandler().getTraderSellPriceForItem(item, this));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  456 */           this.watcher.getCommunicator().sendAddToInventory(item, this.wurmId, (parent.tradeWindow == this) ? parent
/*  457 */               .getWurmId() : 0L, item.getPrice());
/*      */         } 
/*      */       }
/*      */     } 
/*  461 */     tradeChanged();
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
/*      */   private void addToTrade(Item item, Item parent) {
/*  476 */     if (item.tradeWindow != this)
/*      */     {
/*  478 */       item.setTradeWindow(this);
/*      */     }
/*      */     
/*  481 */     Item[] its = item.getItemsAsArray();
/*  482 */     for (Item lIt : its)
/*      */     {
/*  484 */       addToTrade(lIt, item);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeFromTrade(Item item, boolean noSwap) {
/*  490 */     this.windowowner.getCommunicator().sendRemoveFromInventory(item, this.wurmId);
/*  491 */     this.watcher.getCommunicator().sendRemoveFromInventory(item, this.wurmId);
/*      */     
/*  493 */     if (noSwap && item.isCoin()) {
/*      */       
/*  495 */       if (item.getOwnerId() == -10L)
/*  496 */         Economy.getEconomy().returnCoin(item, "Notrade", true); 
/*  497 */       item.setTradeWindow(null);
/*      */     }
/*      */     else {
/*      */       
/*  501 */       item.setTradeWindow(null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeItem(Item item) {
/*  507 */     if (this.items != null)
/*      */     {
/*  509 */       if (item.tradeWindow == this) {
/*      */         
/*  511 */         removeExistingContainedItems(item);
/*  512 */         this.items.remove(item);
/*  513 */         removeFromTrade(item, true);
/*  514 */         tradeChanged();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateItem(Item item) {
/*  521 */     if (this.items != null)
/*      */     {
/*  523 */       if (item.tradeWindow == this) {
/*      */         
/*  525 */         if (!this.windowowner.isPlayer()) {
/*      */ 
/*      */ 
/*      */           
/*  529 */           this.windowowner.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, 0);
/*      */         }
/*  531 */         else if (!this.watcher.isPlayer()) {
/*      */ 
/*      */           
/*  534 */           this.windowowner.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, this.watcher
/*  535 */               .getTradeHandler().getTraderBuyPriceForItem(item));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  540 */           this.windowowner.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, item.getPrice());
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  545 */         if (!this.watcher.isPlayer()) {
/*      */ 
/*      */ 
/*      */           
/*  549 */           this.watcher.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, 0);
/*      */         }
/*  551 */         else if (!this.windowowner.isPlayer()) {
/*      */ 
/*      */           
/*  554 */           this.watcher.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, this.windowowner
/*  555 */               .getTradeHandler().getTraderSellPriceForItem(item, this));
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  560 */           this.watcher.getCommunicator().sendUpdateInventoryItem(item, this.wurmId, item.getPrice());
/*      */         } 
/*  562 */         tradeChanged();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void tradeChanged() {
/*  569 */     if (this.wurmId == 2L && !this.trade.creatureTwo.isPlayer())
/*      */     {
/*  571 */       this.trade.setCreatureTwoSatisfied(false);
/*      */     }
/*  573 */     if (this.wurmId == 3L || this.wurmId == 4L) {
/*      */       
/*  575 */       this.trade.setCreatureOneSatisfied(false);
/*  576 */       this.trade.setCreatureTwoSatisfied(false);
/*  577 */       int c = this.trade.getNextTradeId();
/*  578 */       this.windowowner.getCommunicator().sendTradeChanged(c);
/*  579 */       this.watcher.getCommunicator().sendTradeChanged(c);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasInventorySpace() {
/*  588 */     if (!this.offer) {
/*      */       
/*  590 */       if (!(this.watcher instanceof Player))
/*  591 */         return true; 
/*  592 */       Item inventory = this.watcher.getInventory();
/*  593 */       if (inventory == null) {
/*      */         
/*  595 */         this.windowowner.getCommunicator().sendAlertServerMessage("Could not find inventory for " + this.watcher
/*  596 */             .getName() + ". Trade aborted.");
/*  597 */         this.watcher.getCommunicator().sendAlertServerMessage("Could not find your inventory item. Trade aborted. Please contact administrators.");
/*      */         
/*  599 */         logger.log(Level.WARNING, "Failed to locate inventory for " + this.watcher.getName());
/*  600 */         return false;
/*      */       } 
/*  602 */       if (this.items != null) {
/*      */         
/*  604 */         int nums = 0;
/*  605 */         for (Item item : this.items) {
/*      */           
/*  607 */           if (!inventory.testInsertItem(item))
/*  608 */             return false; 
/*  609 */           if (!item.isCoin())
/*  610 */             nums++; 
/*  611 */           if (!item.canBeDropped(false) && ((Player)this.watcher).isGuest()) {
/*      */             
/*  613 */             this.windowowner.getCommunicator().sendAlertServerMessage("Guests cannot receive the item " + item
/*  614 */                 .getName() + ".");
/*  615 */             this.watcher.getCommunicator().sendAlertServerMessage("Guests cannot receive the item " + item
/*  616 */                 .getName() + ".");
/*  617 */             return false;
/*      */           } 
/*      */         } 
/*  620 */         if (this.watcher.getPower() <= 0 && nums + inventory.getNumItemsNotCoins() > 99) {
/*      */           
/*  622 */           this.watcher.getCommunicator().sendAlertServerMessage("You may not carry that many items in your inventory.");
/*  623 */           this.windowowner.getCommunicator().sendAlertServerMessage(this.watcher
/*  624 */               .getName() + " may not carry that many items in " + this.watcher.getHisHerItsString() + " inventory.");
/*      */           
/*  626 */           return false;
/*      */         } 
/*      */       } 
/*  629 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  633 */     this.windowowner.getCommunicator().sendAlertServerMessage("There is a bug in the trade system. This shouldn't happen. Please report.");
/*      */     
/*  635 */     this.watcher.getCommunicator().sendAlertServerMessage("There is a bug in the trade system. This shouldn't happen. Please report.");
/*      */     
/*  637 */     logger.log(Level.WARNING, "Inconsistency! This is offer window number " + this.wurmId + ". Traders are " + this.watcher
/*  638 */         .getName() + ", " + this.windowowner
/*  639 */         .getName());
/*      */     
/*  641 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   int getWeight() {
/*  646 */     int toReturn = 0;
/*  647 */     if (this.items != null)
/*      */     {
/*  649 */       for (Item item : this.items)
/*      */       {
/*  651 */         toReturn += item.getFullWeight();
/*      */       }
/*      */     }
/*  654 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean validateTrade() {
/*  659 */     if (this.windowowner.isDead())
/*  660 */       return false; 
/*  661 */     if (this.windowowner instanceof Player && !this.windowowner.hasLink())
/*  662 */       return false; 
/*  663 */     if (this.items != null)
/*  664 */       for (Item tit : this.items) {
/*      */         
/*  666 */         if (this.windowowner instanceof Player || !tit.isCoin())
/*      */         {
/*  668 */           if (tit.getOwnerId() != this.windowowner.getWurmId()) {
/*      */             
/*  670 */             this.windowowner.getCommunicator().sendAlertServerMessage(tit
/*  671 */                 .getName() + " is not owned by you. Trade aborted.");
/*  672 */             this.watcher.getCommunicator().sendAlertServerMessage(tit
/*  673 */                 .getName() + " is not owned by " + this.windowowner.getName() + ". Trade aborted.");
/*  674 */             return false;
/*      */           } 
/*      */         }
/*  677 */         Item[] allItems = tit.getAllItems(false);
/*  678 */         for (Item lAllItem : allItems) {
/*      */           
/*  680 */           if (this.windowowner instanceof Player || !lAllItem.isCoin())
/*      */           {
/*  682 */             if (lAllItem.getOwnerId() != this.windowowner.getWurmId()) {
/*      */               
/*  684 */               this.windowowner.getCommunicator().sendAlertServerMessage(lAllItem
/*  685 */                   .getName() + " is not owned by you. Trade aborted.");
/*  686 */               this.watcher.getCommunicator().sendAlertServerMessage(lAllItem
/*  687 */                   .getName() + " is not owned by " + this.windowowner.getName() + ". Trade aborted.");
/*  688 */               return false;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }  
/*  693 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   void swapOwners() {
/*  698 */     boolean errors = false;
/*  699 */     if (!this.offer) {
/*      */       
/*  701 */       Item inventory = this.watcher.getInventory();
/*  702 */       Item ownInventory = this.windowowner.getInventory();
/*  703 */       Shop shop = null;
/*  704 */       int moneyAdded = 0;
/*  705 */       int moneyLost = 0;
/*  706 */       if (this.windowowner.isNpcTrader()) {
/*  707 */         shop = Economy.getEconomy().getShop(this.windowowner);
/*  708 */       } else if (this.watcher.isNpcTrader()) {
/*  709 */         shop = Economy.getEconomy().getShop(this.watcher);
/*      */       } 
/*  711 */       if (this.items != null) {
/*      */         
/*  713 */         Item[] its = this.items.<Item>toArray(new Item[this.items.size()]);
/*  714 */         for (Item lIt : its) {
/*      */           
/*  716 */           Item item = lIt;
/*      */           
/*  718 */           removeExistingContainedItems(lIt);
/*  719 */           removeFromTrade(item, false);
/*  720 */           boolean coin = item.isCoin();
/*  721 */           long parentId = item.getParentId();
/*  722 */           boolean ok = true;
/*  723 */           if (this.windowowner instanceof Player) {
/*      */             
/*  725 */             if (this.watcher instanceof Player) {
/*      */               
/*  727 */               if (item.isVillageDeed() || item.isHomesteadDeed()) {
/*      */                 
/*  729 */                 int data = item.getData2();
/*  730 */                 if (data > 0)
/*      */                 {
/*  732 */                   if (!this.watcher.isPaying()) {
/*      */                     
/*  734 */                     this.windowowner.getCommunicator().sendNormalServerMessage("You need to be premium in order to receive a deed.");
/*      */                     
/*  736 */                     ok = false;
/*      */                   } else {
/*      */ 
/*      */                     
/*      */                     try {
/*  741 */                       Village village = Villages.getVillage(data);
/*  742 */                       Citizen oldMayor = village.getCitizen(this.windowowner.getWurmId());
/*  743 */                       Village oldVillage = this.watcher.getCitizenVillage();
/*      */                       
/*  745 */                       if (this.windowowner.getKingdomId() != this.watcher.getKingdomId()) {
/*      */                         
/*  747 */                         this.windowowner.getCommunicator().sendNormalServerMessage("You cannot trade the deed for " + village
/*  748 */                             .getName() + " to another kingdom.");
/*      */                         
/*  750 */                         ok = false;
/*      */                       } 
/*  752 */                       if (ok && oldVillage != null && oldVillage != village) {
/*      */                         
/*  754 */                         Citizen oldCit = oldVillage.getCitizen(this.watcher.getWurmId());
/*  755 */                         VillageRole role = oldCit.getRole();
/*  756 */                         if (role.getStatus() == 2) {
/*      */                           
/*  758 */                           this.watcher.getCommunicator().sendNormalServerMessage("You cannot trade the deed for " + village
/*  759 */                               .getName() + " since you are already the mayor of " + oldVillage
/*      */                               
/*  761 */                               .getName());
/*  762 */                           this.windowowner.getCommunicator().sendNormalServerMessage("You cannot trade the deed for " + village
/*  763 */                               .getName() + " since " + this.watcher
/*  764 */                               .getName() + " is already the mayor of " + oldVillage
/*  765 */                               .getName());
/*  766 */                           ok = false;
/*      */                         } 
/*  768 */                         if (ok && 
/*  769 */                           oldCit != null)
/*  770 */                           oldVillage.removeCitizen(this.watcher); 
/*      */                       } 
/*  772 */                       if (ok) {
/*      */                         
/*  774 */                         if (oldMayor != null) {
/*      */                           
/*      */                           try {
/*      */                             
/*  778 */                             if (item.isVillageDeed()) {
/*  779 */                               oldMayor.setRole(village.getRoleForStatus((byte)3));
/*      */                             } else {
/*  781 */                               village.removeCitizen(oldMayor);
/*      */                             } 
/*  783 */                           } catch (IOException iox) {
/*      */                             
/*  785 */                             logger.log(Level.WARNING, "Error when removing " + this.windowowner
/*  786 */                                 .getName() + " as mayor: " + iox
/*  787 */                                 .getMessage(), iox);
/*  788 */                             this.watcher.getCommunicator().sendSafeServerMessage("An error occured when removing " + this.windowowner
/*  789 */                                 .getName() + " as mayor. Please contact administration.");
/*      */                             
/*  791 */                             this.windowowner
/*  792 */                               .getCommunicator()
/*  793 */                               .sendSafeServerMessage("An error occured when removing you as mayor. Please contact administration.");
/*      */                           } 
/*      */                         }
/*      */                         
/*  797 */                         if (village.getMayor() != null) {
/*      */                           
/*  799 */                           logger.log(Level.WARNING, "Error when changing mayor. Mayor should have been removed - " + this.windowowner
/*      */ 
/*      */                               
/*  802 */                               .getName() + " with wurmid: " + this.windowowner
/*  803 */                               .getWurmId() + ". Current mayor is " + village
/*  804 */                               .getMayor().getId() + ". Removing that mayor anyways.");
/*      */ 
/*      */                           
/*      */                           try {
/*  808 */                             village.getMayor().setRole(village.getRoleForStatus((byte)3));
/*      */                           }
/*  810 */                           catch (IOException iox) {
/*      */                             
/*  812 */                             logger.log(Level.WARNING, "Error when removing " + this.windowowner
/*  813 */                                 .getName() + " as mayor: " + iox
/*  814 */                                 .getMessage(), iox);
/*  815 */                             this.watcher.getCommunicator().sendSafeServerMessage("An error occured when removing " + this.windowowner
/*  816 */                                 .getName() + " as mayor. Please contact administration.");
/*      */                             
/*  818 */                             this.windowowner
/*  819 */                               .getCommunicator()
/*  820 */                               .sendSafeServerMessage("An error occured when removing you as mayor. Please contact administration.");
/*      */                           } 
/*      */                         } 
/*      */                         
/*  824 */                         Citizen newMayor = village.getCitizen(this.watcher.getWurmId());
/*  825 */                         if (newMayor == null) {
/*      */ 
/*      */                           
/*      */                           try {
/*  829 */                             village.addCitizen(this.watcher, village.getRoleForStatus((byte)2));
/*      */                           }
/*  831 */                           catch (IOException iox) {
/*      */                             
/*  833 */                             logger.log(Level.WARNING, "Error when setting " + this.watcher.getName() + " as mayor: " + iox
/*  834 */                                 .getMessage(), iox);
/*  835 */                             this.windowowner.getCommunicator().sendSafeServerMessage("An error occured when setting " + this.watcher
/*  836 */                                 .getName() + " as mayor. Please contact administration.");
/*      */                             
/*  838 */                             this.watcher.getCommunicator()
/*  839 */                               .sendSafeServerMessage("An error occured when setting you as mayor. Please contact administration.");
/*      */                           } 
/*      */                         } else {
/*      */ 
/*      */                           
/*      */                           try {
/*      */ 
/*      */                             
/*  847 */                             newMayor.setRole(village.getRoleForStatus((byte)2));
/*      */                           }
/*  849 */                           catch (IOException iox) {
/*      */                             
/*  851 */                             logger.log(Level.WARNING, "Error when setting " + this.watcher.getName() + " as mayor: " + iox
/*  852 */                                 .getMessage(), iox);
/*  853 */                             this.windowowner.getCommunicator().sendSafeServerMessage("An error occured when setting " + this.watcher
/*  854 */                                 .getName() + " as mayor. Please contact administration.");
/*      */                             
/*  856 */                             this.watcher.getCommunicator()
/*  857 */                               .sendSafeServerMessage("An error occured when setting you as mayor. Please contact administration.");
/*      */                           } 
/*      */                         } 
/*      */ 
/*      */                         
/*      */                         try {
/*  863 */                           village.setMayor(this.watcher.getName());
/*      */                         }
/*  865 */                         catch (IOException iox) {
/*      */                           
/*  867 */                           logger.log(Level.WARNING, this.watcher.getName() + ", " + this.windowowner.getName() + ":" + iox
/*  868 */                               .getMessage(), iox);
/*      */                         }
/*      */                       
/*      */                       } 
/*  872 */                     } catch (NoSuchVillageException nsv) {
/*      */                       
/*  874 */                       logger.log(Level.WARNING, "Weird. No village with id " + data + " when " + this.windowowner
/*  875 */                           .getName() + " sold deed with id " + item.getWurmId());
/*      */                     }
/*  877 */                     catch (NoSuchRoleException nsr) {
/*      */                       
/*  879 */                       logger.log(Level.WARNING, "Error when setting " + this.watcher.getName() + " as mayor: " + nsr
/*  880 */                           .getMessage(), (Throwable)nsr);
/*  881 */                       this.windowowner.getCommunicator().sendSafeServerMessage("An error occured when setting " + this.watcher
/*  882 */                           .getName() + " as mayor. Please contact administration.");
/*      */                       
/*  884 */                       this.watcher.getCommunicator()
/*  885 */                         .sendSafeServerMessage("An error occured when setting you as mayor. Please contact administration.");
/*      */                     }
/*      */                   
/*      */                   } 
/*      */                 }
/*  890 */               } else if (item.getTemplateId() == 300) {
/*      */                 
/*  892 */                 long traderId = item.getData();
/*  893 */                 if (traderId != -1L) {
/*      */                   try
/*      */                   {
/*      */                     
/*  897 */                     Creature trader = Server.getInstance().getCreature(traderId);
/*  898 */                     if (trader.isNpcTrader())
/*  899 */                       shop = Economy.getEconomy().getShop(trader); 
/*  900 */                     shop.setOwner(this.watcher.getWurmId());
/*  901 */                     this.watcher.getCommunicator().sendNormalServerMessage("You are now in control of " + trader
/*  902 */                         .getName() + ".");
/*  903 */                     this.windowowner.getCommunicator().sendNormalServerMessage("You are no longer in control of " + trader
/*  904 */                         .getName() + ".");
/*      */                   }
/*  906 */                   catch (NoSuchPlayerException nsp)
/*      */                   {
/*  908 */                     logger.log(Level.WARNING, "Trader for " + traderId + " is a player? Well it can't be found.");
/*      */                     
/*  910 */                     item.setData(-10L);
/*      */                   }
/*  912 */                   catch (NoSuchCreatureException nsc)
/*      */                   {
/*  914 */                     logger.log(Level.WARNING, "Trader for " + traderId + " can't be found.");
/*  915 */                     item.setData(-10L);
/*      */                   }
/*      */                 
/*      */                 }
/*  919 */               } else if (item.getTemplateId() == 1129) {
/*      */                 
/*  921 */                 long wagonerId = item.getData();
/*  922 */                 if (wagonerId != -1L) {
/*      */ 
/*      */                   
/*  925 */                   Wagoner wagoner = Wagoner.getWagoner(wagonerId);
/*  926 */                   if (wagoner != null)
/*      */                   {
/*      */                     
/*  929 */                     wagoner.setOwnerId(this.watcher.getWurmId());
/*  930 */                     this.watcher.getCommunicator().sendNormalServerMessage("You are now in control of " + wagoner
/*  931 */                         .getName() + ".");
/*  932 */                     this.windowowner.getCommunicator().sendNormalServerMessage("You are no longer in control of " + wagoner
/*  933 */                         .getName() + ".");
/*      */                   }
/*      */                 
/*      */                 } 
/*  937 */               } else if (item.isRoyal()) {
/*      */                 
/*  939 */                 if (item.getTemplateId() != 530 && item
/*  940 */                   .getTemplateId() != 533 && item
/*  941 */                   .getTemplateId() != 536)
/*      */                 {
/*  943 */                   if (!this.watcher.isKing())
/*      */                   {
/*  945 */                     this.watcher.getCommunicator().sendNormalServerMessage(this.windowowner
/*  946 */                         .getName() + " seems hesitatant about trading " + item.getName() + ". You need to be crowned the ruler first.");
/*      */                     
/*  948 */                     this.windowowner.getCommunicator().sendNormalServerMessage("Those noble items should not be tainted by simple trade. You need to crown " + this.watcher
/*      */                         
/*  950 */                         .getName() + " ruler first.");
/*  951 */                     ok = false;
/*      */                   }
/*      */                 
/*      */                 }
/*  955 */               } else if (item.getTemplateId() == 781) {
/*      */                 
/*  957 */                 this.watcher.getCommunicator().sendNormalServerMessage("You may not trade the " + item
/*  958 */                     .getName() + ".");
/*  959 */                 this.windowowner.getCommunicator().sendNormalServerMessage("You may not trade the " + item
/*  960 */                     .getName() + ".");
/*  961 */                 ok = false;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  966 */               if (item.isVillageDeed() || item.isHomesteadDeed()) {
/*      */                 
/*  968 */                 int data = item.getData2();
/*  969 */                 if (data > 0) {
/*      */                   
/*      */                   try {
/*      */                     
/*  973 */                     Village village = Villages.getVillage(data);
/*  974 */                     Citizen oldMayor = village.getCitizen(this.windowowner.getWurmId());
/*  975 */                     if (oldMayor != null) {
/*      */                       try
/*      */                       {
/*      */                         
/*  979 */                         oldMayor.setRole(village.getRoleForStatus((byte)3));
/*      */                       }
/*  981 */                       catch (IOException iox)
/*      */                       {
/*  983 */                         logger.log(Level.WARNING, "Error when removing " + this.windowowner.getName() + " as mayor: " + iox
/*  984 */                             .getMessage(), iox);
/*  985 */                         this.watcher.getCommunicator().sendSafeServerMessage("An error occured when removing " + this.windowowner
/*  986 */                             .getName() + " as mayor. Please contact administration.");
/*      */                         
/*  988 */                         this.windowowner
/*  989 */                           .getCommunicator()
/*  990 */                           .sendSafeServerMessage("An error occured when removing you as mayor. Please contact administration.");
/*      */                       
/*      */                       }
/*  993 */                       catch (NoSuchRoleException nsr)
/*      */                       {
/*  995 */                         logger.log(Level.WARNING, "Error when removing " + this.windowowner.getName() + " as mayor: " + nsr
/*  996 */                             .getMessage(), (Throwable)nsr);
/*  997 */                         this.watcher.getCommunicator().sendSafeServerMessage("An error occured when removing " + this.windowowner
/*  998 */                             .getName() + " as mayor. Please contact administration.");
/*      */                         
/* 1000 */                         this.windowowner
/* 1001 */                           .getCommunicator()
/* 1002 */                           .sendSafeServerMessage("An error occured when removing you as mayor. Please contact administration.");
/*      */                       }
/*      */                     
/*      */                     }
/*      */                   }
/* 1007 */                   catch (NoSuchVillageException nsv) {
/*      */                     
/* 1009 */                     logger.log(Level.WARNING, "Weird. No village with id " + data + " when " + this.windowowner
/* 1010 */                         .getName() + " sold deed with id " + item
/* 1011 */                         .getWurmId());
/*      */                   } 
/*      */                 }
/*      */               } 
/* 1015 */               if (this.windowowner.isLogged())
/*      */               {
/* 1017 */                 this.windowowner.getLogger().log(Level.INFO, this.windowowner
/*      */                     
/* 1019 */                     .getName() + " selling " + item.getName() + " with id " + item.getWurmId() + " to " + this.watcher
/* 1020 */                     .getName());
/*      */               }
/*      */             } 
/* 1023 */             if (item.getTemplateId() == 166)
/*      */             {
/* 1025 */               if (this.watcher.getPower() == 0) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1029 */                   Structure s = Structures.getStructureForWrit(item.getWurmId());
/* 1030 */                   if (s != null)
/*      */                   {
/* 1032 */                     if (MissionTargets.destroyStructureTargets(s.getWurmId(), this.watcher.getName()))
/*      */                     {
/* 1034 */                       this.watcher.getCommunicator().sendAlertServerMessage("A mission trigger was removed for " + s
/* 1035 */                           .getName() + ".");
/*      */                     }
/*      */                   }
/*      */                 }
/* 1039 */                 catch (NoSuchStructureException noSuchStructureException) {}
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1048 */           else if (this.watcher.isLogged()) {
/*      */             
/* 1050 */             this.watcher.getLogger().log(Level.INFO, this.windowowner
/*      */                 
/* 1052 */                 .getName() + " buying " + item.getName() + " with id " + item.getWurmId() + " from " + this.watcher
/* 1053 */                 .getName());
/*      */           } 
/*      */           
/* 1056 */           if (ok) {
/*      */ 
/*      */             
/*      */             try {
/* 1060 */               Item parent = Items.getItem(parentId);
/* 1061 */               parent.dropItem(item.getWurmId(), false);
/*      */             }
/* 1063 */             catch (NoSuchItemException nsi) {
/*      */               
/* 1065 */               if (!coin)
/* 1066 */                 logger.log(Level.WARNING, "Parent not found for item " + item.getWurmId()); 
/*      */             } 
/* 1068 */             if (this.watcher instanceof Player) {
/*      */               
/* 1070 */               inventory.insertItem(item);
/*      */ 
/*      */               
/* 1073 */               if (coin && shop != null) {
/*      */ 
/*      */ 
/*      */                 
/* 1077 */                 if (!shop.isPersonal() || shop.getOwnerId() == this.watcher.getWurmId()) {
/*      */                   
/* 1079 */                   long v = Economy.getValueFor(item.getTemplateId());
/* 1080 */                   moneyLost = (int)(moneyLost + v);
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1086 */                 if (shop.getOwnerId() == this.watcher.getWurmId()) {
/* 1087 */                   getLogger(shop.getWurmId()).log(Level.INFO, this.watcher
/*      */                       
/* 1089 */                       .getName() + " received " + 
/* 1090 */                       MaterialUtilities.getMaterialString(item.getMaterial()) + " " + item
/* 1091 */                       .getName() + ", id: " + item.getWurmId() + ", QL: " + item
/* 1092 */                       .getQualityLevel());
/*      */                 }
/* 1094 */               } else if (shop != null) {
/*      */                 
/* 1096 */                 if (!shop.isPersonal())
/*      */                 {
/* 1098 */                   int deminc = 1;
/* 1099 */                   if (item.isCombine()) {
/* 1100 */                     deminc = Math.max(1, item.getWeightGrams() / item.getTemplate().getWeightGrams());
/*      */                   }
/* 1102 */                   Economy.getEconomy().addItemSoldByTraders(item.getTemplateId());
/* 1103 */                   shop.getLocalSupplyDemand().addItemSold(item.getTemplateId(), deminc);
/*      */                 }
/*      */                 else
/*      */                 {
/* 1107 */                   getLogger(shop.getWurmId()).log(Level.INFO, this.watcher
/*      */                       
/* 1109 */                       .getName() + " received " + 
/* 1110 */                       MaterialUtilities.getMaterialString(item.getMaterial()) + " " + item
/* 1111 */                       .getName() + ", id: " + item.getWurmId() + ", QL: " + item
/* 1112 */                       .getQualityLevel());
/*      */                 }
/*      */               
/*      */               } 
/* 1116 */             } else if (coin) {
/*      */               
/* 1118 */               if (shop != null) {
/*      */                 
/* 1120 */                 if (shop.isPersonal())
/*      */                 {
/* 1122 */                   getLogger(shop.getWurmId()).log(Level.INFO, this.watcher
/*      */                       
/* 1124 */                       .getName() + " received " + 
/* 1125 */                       MaterialUtilities.getMaterialString(item.getMaterial()) + " " + item
/* 1126 */                       .getName() + ", id: " + item.getWurmId() + ", QL: " + item
/* 1127 */                       .getQualityLevel());
/*      */                   
/* 1129 */                   if (this.windowowner.getWurmId() == shop.getOwnerId())
/*      */                   {
/* 1131 */                     inventory.insertItem(item);
/* 1132 */                     moneyAdded += Economy.getValueFor(item.getTemplateId());
/*      */ 
/*      */                   
/*      */                   }
/*      */                   else
/*      */                   {
/*      */ 
/*      */                     
/* 1140 */                     Economy.getEconomy().returnCoin(item, "PersonalShop");
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */                 else
/*      */                 {
/* 1147 */                   Economy.getEconomy().returnCoin(item, "TraderShop");
/* 1148 */                   long v = Economy.getValueFor(item.getTemplateId());
/* 1149 */                   moneyAdded = (int)(moneyAdded + v);
/*      */                 }
/*      */               
/*      */               } else {
/*      */                 
/* 1154 */                 logger.log(Level.WARNING, this.windowowner.getName() + ", id=" + this.windowowner.getWurmId() + " failed to locate TraderMoney.");
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 1159 */               inventory.insertItem(item);
/* 1160 */               if (shop != null)
/*      */               {
/* 1162 */                 if (!shop.isPersonal()) {
/*      */                   
/* 1164 */                   item.setPrice(0);
/* 1165 */                   int deminc = 1;
/* 1166 */                   if (item.isCombine()) {
/* 1167 */                     deminc = Math.max(1, item.getWeightGrams() / item.getTemplate().getWeightGrams());
/*      */                   }
/* 1169 */                   Economy.getEconomy().addItemBoughtByTraders(item.getTemplateId());
/* 1170 */                   shop.getLocalSupplyDemand().addItemPurchased(item.getTemplateId(), deminc);
/*      */                   
/* 1172 */                   if (item.isVillageDeed() || item.isHomesteadDeed()) {
/*      */                     
/* 1174 */                     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1175 */                     kingsMoney.setMoney(kingsMoney.getMoney() - item.getValue());
/* 1176 */                     item.setAuxData((byte)0);
/* 1177 */                     logger.log(Level.INFO, "King bought a deed for " + item.getValue() + " and now has " + kingsMoney
/* 1178 */                         .getMoney());
/* 1179 */                     long v = Economy.getValueFor(item.getTemplateId());
/* 1180 */                     moneyLost = (int)(moneyLost - v);
/*      */                   } 
/*      */                 } else {
/*      */                   
/* 1184 */                   getLogger(shop.getWurmId()).log(Level.INFO, this.watcher
/*      */                       
/* 1186 */                       .getName() + " received " + 
/* 1187 */                       MaterialUtilities.getMaterialString(item.getMaterial()) + " " + item
/* 1188 */                       .getName() + ", id: " + item.getWurmId() + ", QL: " + item
/* 1189 */                       .getQualityLevel());
/*      */                 }  } 
/*      */             } 
/*      */           } 
/* 1193 */           if (!(this.windowowner instanceof Player))
/*      */           {
/* 1195 */             if (!coin && ok)
/*      */             {
/* 1197 */               if (!item.isPurchased() && !shop.isPersonal()) {
/*      */                 
/*      */                 try {
/*      */                   
/* 1201 */                   if (this.windowowner.getCarriedItem(item.getTemplateId()) == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1209 */                     byte material = item.getMaterial();
/* 1210 */                     if (item.isFullprice() || item.isNoSellback())
/* 1211 */                       material = item.getTemplate().getMaterial(); 
/* 1212 */                     Item newItem = ItemFactory.createItem(item.getTemplateId(), item
/* 1213 */                         .getQualityLevel(), material, (byte)0, null);
/* 1214 */                     ownInventory.insertItem(newItem);
/*      */                   } 
/*      */                   
/* 1217 */                   if (item.isVillageDeed() || item.isHomesteadDeed()) {
/*      */                     
/* 1219 */                     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1220 */                     kingsMoney.setMoney(kingsMoney.getMoney() + (item.getValue() / 2));
/* 1221 */                     item.setLeftAuxData(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1227 */                     Economy.getEconomy().addItemSoldByTraders(item.getName(), item.getValue(), this.windowowner
/* 1228 */                         .getName(), this.watcher.getName(), item.getTemplateId());
/* 1229 */                     moneyAdded -= item.getValue();
/*      */                   } 
/* 1231 */                   if (item.isNoSellback() || item.getTemplateId() == 682) {
/*      */                     
/* 1233 */                     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1234 */                     kingsMoney.setMoney(kingsMoney.getMoney() + (item.getValue() / 4));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1241 */                     Economy.getEconomy().addItemSoldByTraders(item.getName(), item.getValue(), this.windowowner
/* 1242 */                         .getName(), this.watcher.getName(), item.getTemplateId());
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1247 */                     moneyAdded -= item.getValue() * 3 / 4;
/*      */                   } 
/* 1249 */                   if (item.getTemplateId() == 300 || item
/* 1250 */                     .getTemplateId() == 299)
/*      */                   {
/* 1252 */                     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1253 */                     kingsMoney.setMoney(kingsMoney.getMoney() + (item.getValue() / 4));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1259 */                     Economy.getEconomy().addItemSoldByTraders(item.getName(), item.getValue(), this.windowowner
/* 1260 */                         .getName(), this.watcher.getName(), item.getTemplateId());
/* 1261 */                     moneyAdded -= item.getValue() * 3 / 4;
/*      */                   }
/* 1263 */                   else if (item.getTemplateId() == 1129)
/*      */                   {
/*      */                     
/* 1266 */                     Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1267 */                     kingsMoney.setMoney(kingsMoney.getMoney() + (item.getValue() / 2));
/*      */                     
/* 1269 */                     Economy.getEconomy().addItemSoldByTraders(item.getName(), item.getValue(), this.windowowner
/* 1270 */                         .getName(), this.watcher.getName(), item.getTemplateId());
/* 1271 */                     moneyAdded -= item.getValue();
/*      */                   }
/*      */                 
/* 1274 */                 } catch (NoSuchTemplateException nst) {
/*      */                   
/* 1276 */                   logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */                 }
/* 1278 */                 catch (FailedException fe) {
/*      */                   
/* 1280 */                   logger.log(Level.WARNING, fe.getMessage(), (Throwable)fe);
/*      */                 } 
/*      */               }
/*      */             }
/*      */           }
/* 1285 */           if (!ok)
/* 1286 */             errors = true; 
/*      */         } 
/*      */       } 
/* 1289 */       if (!errors) {
/* 1290 */         this.windowowner.getCommunicator().sendNormalServerMessage("The trade was completed successfully.");
/*      */       } else {
/* 1292 */         this.windowowner.getCommunicator().sendNormalServerMessage("The trade was completed, not all items were traded.");
/* 1293 */       }  if (shop != null)
/*      */       {
/* 1295 */         int diff = moneyAdded - moneyLost;
/*      */ 
/*      */ 
/*      */         
/* 1299 */         if (shop.isPersonal())
/*      */         {
/* 1301 */           if (diff != 0) {
/* 1302 */             shop.setMoney(shop.getMoney() + diff);
/*      */           }
/* 1304 */           long moneyToAdd = (long)((float)this.trade.getMoneyAdded() * 0.9F);
/* 1305 */           long kadd = this.trade.getMoneyAdded() - moneyToAdd;
/*      */ 
/*      */           
/* 1308 */           if (moneyToAdd != 0L)
/*      */           {
/* 1310 */             if (this.windowowner.isNpcTrader()) {
/*      */               
/* 1312 */               Item[] c = Economy.getEconomy().getCoinsFor(moneyToAdd);
/* 1313 */               for (Item lElement : c)
/* 1314 */                 ownInventory.insertItem(lElement, true); 
/* 1315 */               shop.setMoney(shop.getMoney() + moneyToAdd);
/*      */               
/* 1317 */               if (this.watcher.getWurmId() != shop.getOwnerId()) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1322 */                 if (kadd != 0L) {
/*      */                   
/* 1324 */                   Shop kingsMoney = Economy.getEconomy().getKingsShop();
/* 1325 */                   kingsMoney.setMoney(kingsMoney.getMoney() + kadd);
/*      */                   
/* 1327 */                   shop.addMoneySpent(kadd);
/*      */                 } 
/* 1329 */                 shop.addMoneyEarned(moneyToAdd);
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1338 */             shop.setLastPolled(System.currentTimeMillis());
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */         else
/*      */         {
/*      */           
/* 1348 */           if (diff >= 1000000)
/* 1349 */             this.watcher.achievement(132); 
/* 1350 */           this.trade.addShopDiff(diff);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1388 */       this.windowowner.getCommunicator().sendAlertServerMessage("There is a bug in the trade system. This shouldn't happen. Please report.");
/*      */       
/* 1390 */       this.watcher.getCommunicator().sendAlertServerMessage("There is a bug in the trade system. This shouldn't happen. Please report.");
/*      */       
/* 1392 */       logger.log(Level.WARNING, "Inconsistency! This is offer window number " + this.wurmId + ". Traders are " + this.watcher
/* 1393 */           .getName() + ", " + this.windowowner
/* 1394 */           .getName());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void endTrade() {
/* 1400 */     if (this.items != null) {
/*      */       
/* 1402 */       Item[] its = this.items.<Item>toArray(new Item[this.items.size()]);
/* 1403 */       for (Item lIt : its) {
/*      */         
/* 1405 */         removeExistingContainedItems(lIt);
/* 1406 */         this.items.remove(lIt);
/* 1407 */         removeFromTrade(lIt, true);
/*      */       } 
/*      */     } 
/* 1410 */     this.items = null;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\TradingWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */