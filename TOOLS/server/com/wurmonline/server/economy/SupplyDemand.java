/*     */ package com.wurmonline.server.economy;
/*     */ 
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
/*     */ public abstract class SupplyDemand
/*     */ {
/*     */   final int id;
/*     */   int itemsBought;
/*     */   int itemsSold;
/*  46 */   private static final Logger logger = Logger.getLogger(SupplyDemand.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long lastPolled;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SupplyDemand(int aId, int aItemsBought, int aItemsSold) {
/*  64 */     this.id = aId;
/*  65 */     this.itemsBought = aItemsBought;
/*  66 */     this.itemsSold = aItemsSold;
/*  67 */     if (!supplyDemandExists()) {
/*  68 */       createSupplyDemand(aItemsBought, aItemsSold);
/*     */     } else {
/*  70 */       logger.log(Level.INFO, "Creating supply demand for already existing id: " + aId);
/*  71 */     }  Economy.addSupplyDemand(this);
/*     */   }
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
/*     */   SupplyDemand(int aId, int aItemsBought, int aItemsSold, long aLastPolled) {
/*  87 */     this.id = aId;
/*  88 */     this.itemsBought = aItemsBought;
/*  89 */     this.itemsSold = aItemsSold;
/*  90 */     this.lastPolled = aLastPolled;
/*  91 */     Economy.addSupplyDemand(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getDemandMod(int extraSold) {
/* 102 */     return Math.max(1000.0F, this.itemsSold) / Math.max(1000.0F, this.itemsBought + extraSold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getItemsBoughtByTraders() {
/* 114 */     return this.itemsBought;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getItemsSoldByTraders() {
/* 124 */     return this.itemsSold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addItemBoughtByTrader() {
/* 132 */     updateItemsBoughtByTraders(this.itemsBought + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addItemSoldByTrader() {
/* 140 */     updateItemsSoldByTraders(this.itemsSold + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getId() {
/* 151 */     return this.id;
/*     */   }
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
/*     */   public final int getPool() {
/* 165 */     return this.itemsBought - this.itemsSold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastPolled() {
/* 174 */     return this.lastPolled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void updateItemsBoughtByTraders(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void updateItemsSoldByTraders(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void createSupplyDemand(int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean supplyDemandExists();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void reset(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 217 */     return "SupplyDemand [TemplateID: " + this.id + ", Items bought:" + this.itemsBought + ", Sold:" + this.itemsSold + ", Pool: " + 
/* 218 */       getPool() + ", Time last polled: " + this.lastPolled + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\economy\SupplyDemand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */