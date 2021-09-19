/*     */ package com.wurmonline.server.loot;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LootItem
/*     */ {
/*     */   private int itemTemplateId;
/*     */   private byte maxRarity;
/*     */   private byte minRarity;
/*  17 */   private float minQuality = 1.0F;
/*  18 */   private float maxQuality = 100.0F;
/*  19 */   private double itemChance = 1.0D;
/*  20 */   private ItemCreateFunc itemCreateFunc = new DefaultItemCreateFunc();
/*     */ 
/*     */ 
/*     */   
/*     */   public LootItem() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public LootItem(int aItemTemplateId, byte aMinRarity, byte aMaxRarity, float aMinQuality, float aMaxQuality, double aItemChance, ItemCreateFunc aItemCreateFunc) {
/*  29 */     this(aItemTemplateId, aMinRarity, aMaxRarity, aMinQuality, aMaxQuality, aItemChance);
/*  30 */     this.itemCreateFunc = aItemCreateFunc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootItem(int aItemTemplateId, byte aMinRarity, byte aMaxRarity, float aMinQuality, float aMaxQuality, double aItemChance) {
/*  36 */     this.itemTemplateId = aItemTemplateId;
/*  37 */     this.maxRarity = aMaxRarity;
/*  38 */     this.minRarity = aMinRarity;
/*  39 */     this.maxQuality = aMaxQuality;
/*  40 */     this.minQuality = aMinQuality;
/*  41 */     this.itemChance = aItemChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMaxRarity() {
/*  46 */     return this.maxRarity;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setMaxRarity(byte aMaxRarity) {
/*  51 */     this.maxRarity = aMaxRarity;
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMinRarity() {
/*  57 */     return this.minRarity;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setMinRarity(byte aMinRarity) {
/*  62 */     this.minRarity = aMinRarity;
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMaxQuality() {
/*  68 */     return this.maxQuality;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setMaxQuality(float aMaxQuality) {
/*  73 */     this.maxQuality = aMaxQuality;
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMinQuality() {
/*  79 */     return this.minQuality;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setMinQuality(float aMinQuality) {
/*  84 */     this.minQuality = aMinQuality;
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getItemChance() {
/*  90 */     return this.itemChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setItemChance(double itemChance) {
/*  95 */     this.itemChance = itemChance;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setItemCreateFunc(ItemCreateFunc func) {
/* 101 */     this.itemCreateFunc = func;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Item> createItem(Creature victim, Creature receiver) {
/* 107 */     return this.itemCreateFunc.create(victim, receiver, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemTemplateId() {
/* 112 */     return this.itemTemplateId;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItem setItemTemplateId(int id) {
/* 117 */     this.itemTemplateId = id;
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemName() {
/*     */     try {
/* 125 */       return ItemTemplateFactory.getInstance().getTemplate(getItemTemplateId()).getName();
/*     */     }
/* 127 */     catch (NoSuchTemplateException e) {
/*     */       
/* 129 */       e.printStackTrace();
/*     */       
/* 131 */       return "<invalid template for id# " + getItemTemplateId() + ">";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\LootItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */