/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.economy.Economy;
/*     */ import com.wurmonline.server.economy.Shop;
/*     */ import com.wurmonline.server.villages.Village;
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
/*     */ public class Trade
/*     */   implements MiscConstants
/*     */ {
/*     */   private final TradingWindow creatureOneOfferWindow;
/*     */   private final TradingWindow creatureTwoOfferWindow;
/*     */   private final TradingWindow creatureOneRequestWindow;
/*     */   private final TradingWindow creatureTwoRequestWindow;
/*     */   public final Creature creatureOne;
/*     */   public final Creature creatureTwo;
/*     */   private boolean creatureOneSatisfied = false;
/*     */   private boolean creatureTwoSatisfied = false;
/*  45 */   private int currentCounter = -1;
/*     */   
/*  47 */   private static final Logger logger = Logger.getLogger(Trade.class.getName());
/*     */   
/*     */   public static final long OFFERWINTWO = 1L;
/*     */   
/*     */   public static final long OFFERWINONE = 2L;
/*     */   
/*     */   public static final long REQUESTWINONE = 3L;
/*     */   public static final long REQUESTWINTWO = 4L;
/*  55 */   private long moneyAdded = 0L;
/*  56 */   private long shopDiff = 0L;
/*  57 */   private long tax = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Trade(Creature aCreatureOne, Creature aCreatureTwo) {
/*  65 */     this.creatureOne = aCreatureOne;
/*  66 */     this.creatureOne.startTrading();
/*  67 */     this.creatureTwo = aCreatureTwo;
/*  68 */     this.creatureTwo.startTrading();
/*  69 */     this.creatureTwoOfferWindow = new TradingWindow(aCreatureTwo, aCreatureOne, true, 1L, this);
/*  70 */     this.creatureOneOfferWindow = new TradingWindow(aCreatureOne, aCreatureTwo, true, 2L, this);
/*  71 */     this.creatureOneRequestWindow = new TradingWindow(aCreatureTwo, aCreatureOne, false, 3L, this);
/*  72 */     this.creatureTwoRequestWindow = new TradingWindow(aCreatureOne, aCreatureTwo, false, 4L, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMoneyAdded(long money) {
/*  82 */     this.moneyAdded = money;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addShopDiff(long money) {
/*  87 */     if (this.creatureOne.getPower() >= 5) {
/*  88 */       this.creatureOne.getCommunicator().sendNormalServerMessage("Adding " + money + " to shop diff " + this.shopDiff + "=" + (this.shopDiff + money));
/*     */     }
/*     */ 
/*     */     
/*  92 */     this.shopDiff += money;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getMoneyAdded() {
/* 101 */     return this.moneyAdded;
/*     */   }
/*     */ 
/*     */   
/*     */   public TradingWindow getTradingWindow(long id) {
/* 106 */     if (id == 1L)
/* 107 */       return this.creatureTwoOfferWindow; 
/* 108 */     if (id == 2L)
/* 109 */       return this.creatureOneOfferWindow; 
/* 110 */     if (id == 3L) {
/* 111 */       return this.creatureOneRequestWindow;
/*     */     }
/* 113 */     return this.creatureTwoRequestWindow;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSatisfied(Creature creature, boolean satisfied, int id) {
/* 118 */     if (id == this.currentCounter) {
/*     */       
/* 120 */       if (creature.equals(this.creatureOne)) {
/* 121 */         this.creatureOneSatisfied = satisfied;
/*     */       } else {
/* 123 */         this.creatureTwoSatisfied = satisfied;
/* 124 */       }  if (this.creatureOneSatisfied && this.creatureTwoSatisfied) {
/*     */         
/* 126 */         if (makeTrade())
/*     */         {
/* 128 */           this.creatureOne.getCommunicator().sendCloseTradeWindow();
/* 129 */           this.creatureTwo.getCommunicator().sendCloseTradeWindow();
/*     */         }
/*     */         else
/*     */         {
/* 133 */           this.creatureOne.getCommunicator().sendTradeAgree(creature, satisfied);
/* 134 */           this.creatureTwo.getCommunicator().sendTradeAgree(creature, satisfied);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 141 */         this.creatureOne.getCommunicator().sendTradeAgree(creature, satisfied);
/* 142 */         this.creatureTwo.getCommunicator().sendTradeAgree(creature, satisfied);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   int getNextTradeId() {
/* 149 */     return ++this.currentCounter;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean makeTrade() {
/* 154 */     if ((this.creatureOne.isPlayer() && !this.creatureOne.hasLink()) || this.creatureOne.isDead()) {
/*     */       
/* 156 */       if (this.creatureOne.hasLink())
/* 157 */         this.creatureOne.getCommunicator().sendNormalServerMessage("You may not trade right now.", (byte)3); 
/* 158 */       this.creatureTwo.getCommunicator().sendNormalServerMessage(this.creatureOne.getName() + " cannot trade right now.", (byte)3);
/* 159 */       end(this.creatureOne, false);
/* 160 */       return true;
/*     */     } 
/* 162 */     if ((this.creatureTwo.isPlayer() && !this.creatureTwo.hasLink()) || this.creatureTwo.isDead()) {
/*     */       
/* 164 */       if (this.creatureTwo.hasLink())
/* 165 */         this.creatureTwo.getCommunicator().sendNormalServerMessage("You may not trade right now.", (byte)3); 
/* 166 */       this.creatureOne.getCommunicator().sendNormalServerMessage(this.creatureTwo.getName() + " cannot trade right now.", (byte)3);
/* 167 */       end(this.creatureTwo, false);
/* 168 */       return true;
/*     */     } 
/* 170 */     if (this.creatureOneRequestWindow.hasInventorySpace() && this.creatureTwoRequestWindow.hasInventorySpace()) {
/*     */       
/* 172 */       int reqOneWeight = this.creatureOneRequestWindow.getWeight();
/* 173 */       int reqTwoWeight = this.creatureTwoRequestWindow.getWeight();
/*     */       
/* 175 */       int diff = reqOneWeight - reqTwoWeight;
/* 176 */       if (diff > 0 && this.creatureOne instanceof com.wurmonline.server.players.Player)
/*     */       {
/* 178 */         if (!this.creatureOne.canCarry(diff)) {
/*     */           
/* 180 */           this.creatureTwo.getCommunicator().sendNormalServerMessage(this.creatureOne.getName() + " cannot carry that much.", (byte)3);
/*     */           
/* 182 */           this.creatureOne.getCommunicator().sendNormalServerMessage("You cannot carry that much.", (byte)3);
/*     */           
/* 184 */           if (this.creatureOne.getPower() > 0) {
/* 185 */             this.creatureOne.getCommunicator().sendNormalServerMessage("You cannot carry that much. You would carry " + diff + " more.");
/*     */           }
/* 187 */           return false;
/*     */         } 
/*     */       }
/* 190 */       diff = reqTwoWeight - reqOneWeight;
/* 191 */       if (diff > 0 && this.creatureTwo instanceof com.wurmonline.server.players.Player)
/*     */       {
/* 193 */         if (!this.creatureTwo.canCarry(diff)) {
/*     */           
/* 195 */           this.creatureOne.getCommunicator().sendNormalServerMessage(this.creatureTwo.getName() + " cannot carry that much.", (byte)3);
/* 196 */           this.creatureTwo.getCommunicator().sendNormalServerMessage("You cannot carry that much.", (byte)3);
/* 197 */           return false;
/*     */         } 
/*     */       }
/* 200 */       boolean ok = this.creatureOneRequestWindow.validateTrade();
/* 201 */       if (ok) {
/* 202 */         ok = this.creatureTwoRequestWindow.validateTrade();
/*     */       } else {
/* 204 */         return false;
/* 205 */       }  if (ok) {
/*     */         
/* 207 */         this.creatureOneRequestWindow.swapOwners();
/* 208 */         this.creatureTwoRequestWindow.swapOwners();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         this.creatureTwoOfferWindow.endTrade();
/* 215 */         this.creatureOneOfferWindow.endTrade();
/* 216 */         Shop shop = null;
/* 217 */         Village citizenVillage = null;
/* 218 */         if (this.creatureOne.isNpcTrader()) {
/*     */           
/* 220 */           shop = Economy.getEconomy().getShop(this.creatureOne);
/* 221 */           shop.setMerchantData(this.creatureOne.getNumberOfShopItems());
/* 222 */           citizenVillage = this.creatureOne.getCitizenVillage();
/*     */         } 
/* 224 */         if (this.creatureTwo.isNpcTrader()) {
/*     */           
/* 226 */           shop = Economy.getEconomy().getShop(this.creatureTwo);
/* 227 */           shop.setMerchantData(this.creatureTwo.getNumberOfShopItems());
/* 228 */           citizenVillage = this.creatureTwo.getCitizenVillage();
/*     */         } 
/* 230 */         if (shop != null)
/*     */         {
/* 232 */           if (this.shopDiff != 0L) {
/*     */             
/* 234 */             if (this.shopDiff > 0L) {
/*     */               
/* 236 */               if (shop.getTax() > 0.0F)
/*     */               {
/* 238 */                 if (!shop.isPersonal() && citizenVillage != null && this.creatureOne.citizenVillage != citizenVillage)
/*     */                 {
/*     */                   
/* 241 */                   if (citizenVillage.plan != null) {
/*     */                     
/* 243 */                     setTax((long)((float)this.shopDiff * shop.getTax()));
/* 244 */                     logger.log(Level.INFO, this.creatureOne.getName() + " and " + this.creatureTwo.getName() + " adding " + 
/* 245 */                         getTax() + " tax to " + citizenVillage.getName());
/* 246 */                     citizenVillage.plan.addMoney(getTax());
/* 247 */                     shop.addTax(getTax());
/*     */                   } 
/*     */                 }
/*     */               }
/* 251 */               if (this.creatureOne.getPower() >= 5)
/* 252 */                 this.creatureOne.getCommunicator().sendNormalServerMessage("Adding " + (this.shopDiff - 
/* 253 */                     getTax()) + " to shop"); 
/* 254 */               shop.addMoneyEarned(this.shopDiff - getTax());
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */               
/* 260 */               if (this.creatureOne.getPower() >= 5)
/* 261 */                 this.creatureOne.getCommunicator().sendNormalServerMessage("Shop spending " + this.shopDiff + "."); 
/* 262 */               shop.addMoneySpent(Math.abs(this.shopDiff));
/*     */             } 
/*     */ 
/*     */             
/* 266 */             if (this.creatureOne.getPower() >= 5) {
/* 267 */               this.creatureOne.getCommunicator().sendNormalServerMessage("Shop setting money " + (shop
/* 268 */                   .getMoney() + this.shopDiff - getTax()) + " (" + shop.getMoney() + " before).");
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 273 */             shop.setMoney(shop.getMoney() + this.shopDiff - getTax());
/*     */           } 
/*     */         }
/* 276 */         this.creatureOne.setTrade(null);
/* 277 */         this.creatureTwo.setTrade(null);
/* 278 */         return true;
/*     */       } 
/*     */     } 
/* 281 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end(Creature creature, boolean closed) {
/*     */     try {
/* 290 */       if (creature.equals(this.creatureOne))
/*     */       {
/* 292 */         this.creatureTwo.getCommunicator().sendCloseTradeWindow();
/* 293 */         if (!closed)
/* 294 */           this.creatureOne.getCommunicator().sendCloseTradeWindow(); 
/* 295 */         this.creatureTwo.getCommunicator().sendNormalServerMessage(this.creatureOne.getName() + " withdrew from the trade.", (byte)2);
/* 296 */         this.creatureOne.getCommunicator().sendNormalServerMessage("You withdraw from the trade.", (byte)2);
/*     */       }
/*     */       else
/*     */       {
/* 300 */         this.creatureOne.getCommunicator().sendCloseTradeWindow();
/* 301 */         if (!closed || !this.creatureTwo.isPlayer())
/* 302 */           this.creatureTwo.getCommunicator().sendCloseTradeWindow(); 
/* 303 */         this.creatureOne.getCommunicator().sendNormalServerMessage(this.creatureTwo.getName() + " withdrew from the trade.", (byte)2);
/* 304 */         this.creatureTwo.getCommunicator().sendNormalServerMessage("You withdraw from the trade.", (byte)2);
/*     */       }
/*     */     
/* 307 */     } catch (Exception nsp) {
/*     */       
/* 309 */       logger.log(Level.WARNING, nsp.getMessage(), nsp);
/*     */     } 
/*     */     
/* 312 */     this.creatureTwoOfferWindow.endTrade();
/* 313 */     this.creatureOneOfferWindow.endTrade();
/* 314 */     this.creatureOneRequestWindow.endTrade();
/* 315 */     this.creatureTwoRequestWindow.endTrade();
/* 316 */     this.creatureOne.setTrade(null);
/* 317 */     this.creatureTwo.setTrade(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isCreatureOneSatisfied() {
/* 327 */     return this.creatureOneSatisfied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCreatureOneSatisfied(boolean aCreatureOneSatisfied) {
/* 338 */     this.creatureOneSatisfied = aCreatureOneSatisfied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isCreatureTwoSatisfied() {
/* 348 */     return this.creatureTwoSatisfied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCreatureTwoSatisfied(boolean aCreatureTwoSatisfied) {
/* 359 */     this.creatureTwoSatisfied = aCreatureTwoSatisfied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentCounter() {
/* 369 */     return this.currentCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCurrentCounter(int aCurrentCounter) {
/* 380 */     this.currentCounter = aCurrentCounter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTax() {
/* 390 */     return this.tax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTax(long aTax) {
/* 401 */     this.tax = aTax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TradingWindow getCreatureOneRequestWindow() {
/* 411 */     return this.creatureOneRequestWindow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TradingWindow getCreatureTwoRequestWindow() {
/* 421 */     return this.creatureTwoRequestWindow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Creature getCreatureOne() {
/* 431 */     return this.creatureOne;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Creature getCreatureTwo() {
/* 441 */     return this.creatureTwo;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Trade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */