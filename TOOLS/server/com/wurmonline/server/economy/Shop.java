/*     */ package com.wurmonline.server.economy;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.VolaTile;
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
/*     */ public abstract class Shop
/*     */   implements MiscConstants
/*     */ {
/*     */   final long wurmid;
/*     */   long money;
/*  46 */   long taxPaid = 0L;
/*     */   
/*  48 */   private static final Logger logger = Logger.getLogger(Shop.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  52 */   long ownerId = -10L;
/*     */ 
/*     */ 
/*     */   
/*  56 */   float priceModifier = 1.4F;
/*  57 */   static int numTraders = 0;
/*     */   
/*     */   boolean followGlobalPrice = true;
/*     */   
/*     */   boolean useLocalPrice = true;
/*     */   
/*  63 */   long lastPolled = System.currentTimeMillis();
/*  64 */   long moneyEarned = 0L;
/*  65 */   long moneySpent = 0L;
/*  66 */   long moneySpentLastMonth = 0L;
/*  67 */   long moneyEarnedLife = 0L;
/*  68 */   long moneySpentLife = 0L;
/*     */   private final LocalSupplyDemand localSupplyDemand;
/*  70 */   float tax = 0.0F;
/*  71 */   int numberOfItems = 0;
/*  72 */   long whenEmpty = 0L;
/*     */ 
/*     */   
/*     */   Shop(long aWurmid, long aMoney) {
/*  76 */     this.wurmid = aWurmid;
/*  77 */     this.money = aMoney;
/*  78 */     if (!traderMoneyExists()) {
/*     */       
/*  80 */       create();
/*  81 */       if (aWurmid > 0L) {
/*     */         
/*     */         try {
/*     */           
/*  85 */           Creature c = Server.getInstance().getCreature(aWurmid);
/*  86 */           createShop(c);
/*  87 */           Economy.getEconomy().getKingsShop().setMoney(Economy.getEconomy().getKingsShop().getMoney() - aMoney);
/*     */         }
/*  89 */         catch (NoSuchCreatureException nsc) {
/*     */           
/*  91 */           logger.log(Level.WARNING, "Failed to locate creature owner for shop id " + aWurmid, (Throwable)nsc);
/*     */         }
/*  93 */         catch (NoSuchPlayerException nsp) {
/*     */           
/*  95 */           logger.log(Level.WARNING, "Creature a player?: Failed to locate creature owner for shop id " + aWurmid, (Throwable)nsp);
/*     */         } 
/*     */       }
/*     */     } 
/*  99 */     this.ownerId = -10L;
/* 100 */     this.localSupplyDemand = new LocalSupplyDemand(aWurmid);
/* 101 */     Economy.addShop(this);
/*     */   }
/*     */ 
/*     */   
/*     */   Shop(long aWurmid, long aMoney, long aOwnerid) {
/* 106 */     this.wurmid = aWurmid;
/* 107 */     this.money = aMoney;
/* 108 */     this.ownerId = aOwnerid;
/* 109 */     if (aOwnerid != -10L) {
/*     */       
/* 111 */       this.numberOfItems = 0;
/* 112 */       this.whenEmpty = System.currentTimeMillis();
/*     */     } 
/* 114 */     if (!traderMoneyExists())
/* 115 */       create(); 
/* 116 */     this.localSupplyDemand = new LocalSupplyDemand(aWurmid);
/* 117 */     Economy.addShop(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Shop(long aWurmid, long aMoney, long aOwnerid, float aPriceMod, boolean aFollowGlobalPrice, boolean aUseLocalPrice, long aLastPolled, float aTax, long spentMonth, long spentLife, long earnedMonth, long earnedLife, long spentLast, long taxpaid, int _numberOfItems, long _whenEmpty, boolean aLoad) {
/* 125 */     this.wurmid = aWurmid;
/* 126 */     this.money = aMoney;
/* 127 */     this.ownerId = aOwnerid;
/* 128 */     this.priceModifier = aPriceMod;
/* 129 */     this.followGlobalPrice = aFollowGlobalPrice;
/* 130 */     this.useLocalPrice = aUseLocalPrice;
/* 131 */     this.lastPolled = aLastPolled;
/* 132 */     this.localSupplyDemand = new LocalSupplyDemand(aWurmid);
/* 133 */     this.tax = aTax;
/* 134 */     this.moneySpent = spentMonth;
/* 135 */     this.moneyEarned = earnedMonth;
/* 136 */     this.moneySpentLife = spentLife;
/* 137 */     this.moneyEarnedLife = earnedLife;
/* 138 */     this.moneySpentLastMonth = spentLast;
/* 139 */     this.taxPaid = taxpaid;
/* 140 */     if (this.ownerId > 0L && _numberOfItems == 0) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 146 */         Creature creature = Creatures.getInstance().getCreature(this.wurmid);
/* 147 */         Item[] invItems = creature.getInventory().getItemsAsArray();
/*     */         
/* 149 */         int noItems = 0;
/* 150 */         for (int x = 0; x < invItems.length; x++) {
/*     */           
/* 152 */           if (!invItems[x].isCoin()) {
/* 153 */             noItems++;
/*     */           }
/*     */         } 
/* 156 */         if (noItems == 0) {
/* 157 */           setMerchantData(0, _whenEmpty);
/*     */         } else {
/* 159 */           setMerchantData(noItems, 0L);
/*     */         } 
/* 161 */       } catch (NoSuchCreatureException e) {
/*     */         
/* 163 */         logger.log(Level.WARNING, "Merchant not loaded in time. " + e.getMessage(), (Throwable)e);
/*     */         
/* 165 */         this.numberOfItems = _numberOfItems;
/* 166 */         this.whenEmpty = _whenEmpty;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 171 */       this.numberOfItems = _numberOfItems;
/* 172 */       this.whenEmpty = _whenEmpty;
/*     */     } 
/* 174 */     Economy.addShop(this);
/* 175 */     if (this.ownerId <= 0L) {
/* 176 */       numTraders++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void createShop(Creature toReturn) {
/*     */     try {
/* 183 */       Item inventory = toReturn.getInventory();
/* 184 */       for (int x = 0; x < 3; x++) {
/*     */         
/* 186 */         Item item = Creature.createItem(143, (10 + Server.rand.nextInt(40)));
/* 187 */         inventory.insertItem(item);
/* 188 */         item = Creature.createItem(509, 80.0F);
/* 189 */         inventory.insertItem(item);
/* 190 */         item = Creature.createItem(525, 80.0F);
/* 191 */         inventory.insertItem(item);
/* 192 */         item = Creature.createItem(524, 80.0F);
/* 193 */         inventory.insertItem(item);
/* 194 */         item = Creature.createItem(601, (60 + Server.rand.nextInt(40)));
/* 195 */         inventory.insertItem(item);
/* 196 */         item = Creature.createItem(664, 40.0F);
/* 197 */         inventory.insertItem(item);
/* 198 */         item = Creature.createItem(665, 40.0F);
/* 199 */         inventory.insertItem(item);
/* 200 */         if (Features.Feature.NAMECHANGE.isEnabled()) {
/*     */           
/* 202 */           item = Creature.createItem(843, (60 + Server.rand.nextInt(40)));
/* 203 */           inventory.insertItem(item);
/*     */         } 
/* 205 */         item = Creature.createItem(666, 99.0F);
/* 206 */         inventory.insertItem(item);
/* 207 */         item = Creature.createItem(668, (60 + Server.rand.nextInt(40)));
/* 208 */         inventory.insertItem(item);
/* 209 */         item = Creature.createItem(667, (60 + Server.rand.nextInt(40)));
/* 210 */         inventory.insertItem(item);
/*     */       } 
/* 212 */       if (!Features.Feature.BLOCKED_TRADERS.isEnabled()) {
/*     */         
/* 214 */         Item item = Creature.createItem(299, (10 + Server.rand.nextInt(80)));
/* 215 */         inventory.insertItem(item);
/*     */       } 
/* 217 */       if (Servers.localServer.PVPSERVER) {
/*     */ 
/*     */         
/* 220 */         Item declaration = Creature.createItem(682, (10 + Server.rand.nextInt(80)));
/* 221 */         inventory.insertItem(declaration);
/*     */       } 
/* 223 */       Item contract = Creature.createItem(300, (10 + Server.rand.nextInt(80)));
/* 224 */       inventory.insertItem(contract);
/*     */     }
/* 226 */     catch (Exception ex) {
/*     */       
/* 228 */       logger.log(Level.INFO, "Failed to create merchant inventory items for shop, creature: " + toReturn, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean followsGlobalPrice() {
/* 234 */     return this.followGlobalPrice;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean usesLocalPrice() {
/* 239 */     return this.useLocalPrice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastPolled() {
/* 248 */     return this.lastPolled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long howLongEmpty() {
/* 253 */     if (this.numberOfItems == 0)
/* 254 */       return System.currentTimeMillis() - this.whenEmpty; 
/* 255 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getWurmId() {
/* 260 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getMoney() {
/* 269 */     return this.money;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPersonal() {
/* 274 */     return (this.ownerId > 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getOwnerId() {
/* 283 */     return this.ownerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getPriceModifier() {
/* 292 */     return this.priceModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getNumTraders() {
/* 302 */     return numTraders;
/*     */   }
/*     */ 
/*     */   
/*     */   public final double getLocalTraderSellPrice(Item item, int currentStock, int numberSold) {
/* 307 */     double globalPrice = 1000000.0D;
/*     */ 
/*     */ 
/*     */     
/* 311 */     globalPrice = item.getValue();
/*     */     
/* 313 */     if (this.useLocalPrice)
/*     */     {
/* 315 */       globalPrice = this.localSupplyDemand.getPrice(item.getTemplateId(), globalPrice, numberSold, true);
/*     */     }
/*     */     
/* 318 */     return Math.max(0.0D, globalPrice);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getLocalTraderBuyPrice(Item item, int currentStock, int extra) {
/* 323 */     long globalPrice = 1L;
/*     */ 
/*     */ 
/*     */     
/* 327 */     globalPrice = item.getValue();
/*     */     
/* 329 */     if (this.useLocalPrice)
/*     */     {
/* 331 */       globalPrice = (long)this.localSupplyDemand.getPrice(item.getTemplateId(), globalPrice, extra, false);
/*     */     }
/*     */     
/* 334 */     return Math.max(0L, globalPrice);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final VolaTile getPos() {
/*     */     try {
/* 341 */       Creature c = Creatures.getInstance().getCreature(this.wurmid);
/* 342 */       return c.getCurrentTile();
/*     */     }
/* 344 */     catch (NoSuchCreatureException nsc) {
/*     */       
/* 346 */       logger.log(Level.WARNING, "No creature for shop " + this.wurmid);
/*     */       
/* 348 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void create();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean traderMoneyExists();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setMoney(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void delete();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setPriceModifier(float paramFloat);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setFollowGlobalPrice(boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setUseLocalPrice(boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setLastPolled(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setTax(float paramFloat);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getTax() {
/* 402 */     return this.tax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getTaxAsInt() {
/* 412 */     return (int)(this.tax * 100.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSellRatio() {
/* 417 */     if (this.moneyEarned > 0L) {
/*     */       
/* 419 */       if (this.moneySpent > 0L) {
/* 420 */         return (float)this.moneyEarned / (float)this.moneySpent;
/*     */       }
/* 422 */     } else if (this.moneySpent > 0L) {
/*     */       
/* 424 */       return (float)-this.moneySpent;
/*     */     } 
/* 426 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMoneySpentMonth() {
/* 431 */     return this.moneySpent;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMoneySpentLastMonth() {
/* 436 */     return this.moneySpentLastMonth;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMoneyEarnedMonth() {
/* 441 */     return this.moneyEarned;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMoneySpentLife() {
/* 446 */     return this.moneySpent;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMoneyEarnedLife() {
/* 451 */     return this.moneyEarnedLife;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTaxPaid() {
/* 456 */     return this.taxPaid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final LocalSupplyDemand getLocalSupplyDemand() {
/* 466 */     return this.localSupplyDemand;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMerchantData(int _numberOfItems) {
/* 472 */     if (_numberOfItems == 0) {
/*     */       
/* 474 */       if (this.numberOfItems == 0) {
/*     */         
/* 476 */         if (this.whenEmpty == 0L) {
/* 477 */           setMerchantData(0, this.lastPolled);
/*     */         } else {
/* 479 */           setMerchantData(0, this.whenEmpty);
/*     */         } 
/*     */       } else {
/* 482 */         setMerchantData(0, System.currentTimeMillis());
/*     */       } 
/*     */     } else {
/* 485 */       setMerchantData(_numberOfItems, 0L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final int getNumberOfItems() {
/* 490 */     return this.numberOfItems;
/*     */   }
/*     */   
/*     */   public abstract void addMoneyEarned(long paramLong);
/*     */   
/*     */   public abstract void addMoneySpent(long paramLong);
/*     */   
/*     */   public abstract void resetEarnings();
/*     */   
/*     */   public abstract void addTax(long paramLong);
/*     */   
/*     */   public abstract void setOwner(long paramLong);
/*     */   
/*     */   public abstract void setMerchantData(int paramInt, long paramLong);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\Shop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */