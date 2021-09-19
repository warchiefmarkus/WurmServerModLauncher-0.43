/*     */ package com.wurmonline.server.loot;
/*     */ import com.wurmonline.server.behaviours.MethodsItems;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.Materials;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class LootPool {
/*  15 */   protected static final Logger logger = Logger.getLogger(LootPool.class.getName());
/*     */   private String name;
/*     */   private double lootPoolChance;
/*     */   private boolean groupLoot;
/*  19 */   private List<LootItem> lootItems = new ArrayList<>();
/*  20 */   private Random random = new Random();
/*  21 */   private List<Integer> excludeIds = new ArrayList<>();
/*  22 */   private List<Integer> includeIds = new ArrayList<>();
/*     */   
/*  24 */   private ActiveFunc activeFunc = new DefaultActiveFunc();
/*     */   
/*  26 */   private ItemMessageFunc itemLootMessageFunc = new DefaultItemMessageFunc();
/*     */   
/*  28 */   private LootPoolChanceFunc lootPoolChanceFunc = new PercentChanceLootPoolChanceFunc();
/*     */   
/*  30 */   private LootItemFunc itemFunc = new PercentChanceLootItemFunc();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LootPool(int aChance) {
/*  38 */     this.lootPoolChance = aChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool(int aChance, ActiveFunc aActiveFunc) {
/*  43 */     this(aChance);
/*  44 */     this.activeFunc = aActiveFunc;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LootItem> getLootItems() {
/*  49 */     return this.lootItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setLootItems(List<LootItem> items) {
/*  54 */     this.lootItems = items;
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Random getRandom() {
/*  60 */     return this.random;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  65 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setName(String name) {
/*  70 */     this.name = name;
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setItemLootMessageFunc(ItemMessageFunc itemLootMessageFunc) {
/*  76 */     this.itemLootMessageFunc = itemLootMessageFunc;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGroupLoot() {
/*  82 */     return this.groupLoot;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setGroupLoot(boolean groupLoot) {
/*  87 */     this.groupLoot = groupLoot;
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive(Creature victim, Creature receiver) {
/*  93 */     if (this.excludeIds.contains(Integer.valueOf(victim.getTemplate().getTemplateId()))) {
/*     */       
/*  95 */       logger.info("Skipping loot pool '" + getName() + "' for " + receiver.getName() + ": " + victim.getTemplate().getTemplateId() + " is excluded (" + victim.getTemplate().getName() + ").");
/*  96 */       return false;
/*     */     } 
/*  98 */     if (this.includeIds.size() > 0 && !this.includeIds.contains(Integer.valueOf(victim.getTemplate().getTemplateId()))) {
/*     */       
/* 100 */       logger.info("Skipping loot pool '" + getName() + "' for " + receiver.getName() + ": " + victim.getTemplate().getTemplateId() + " is not included (" + victim.getTemplate().getName() + ").");
/* 101 */       return false;
/*     */     } 
/* 103 */     return this.activeFunc.active(victim, receiver);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setActiveFunc(ActiveFunc func) {
/* 108 */     this.activeFunc = func;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setItemFunc(LootItemFunc func) {
/* 114 */     this.itemFunc = func;
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void awardLootItem(Creature victim, HashSet<Creature> receivers) {
/* 120 */     if (isGroupLoot()) {
/* 121 */       receivers.forEach(receiver -> awardLootItem(victim, receiver));
/* 122 */     } else if (receivers.size() > 0) {
/* 123 */       awardLootItem(victim, (new ArrayList<>(receivers)).get(this.random.nextInt(receivers.size())));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void awardLootItem(Creature victim, Creature receiver) {
/* 128 */     if (!isActive(victim, receiver))
/* 129 */       return;  if (!passedChanceRoll(victim, receiver))
/* 130 */       return;  Optional<Item> item = this.itemFunc.item(victim, receiver, this).flatMap(loot -> loot.createItem(victim, receiver));
/* 131 */     item.ifPresent(i -> {
/*     */           receiver.getInventoryOptional().ifPresent(());
/*     */           sendLootMessages(victim, receiver, i);
/*     */           logger.info("Awarding loot " + MethodsItems.getRarityName(i.getRarity()) + " " + i.getName() + " [" + Materials.convertMaterialByteIntoString(i.getMaterial()) + "] (" + i.getWurmId() + ") to " + receiver.getName() + " (" + receiver.getWurmId() + ")");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLootPoolChance() {
/* 142 */     return this.lootPoolChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setLootPoolChance(double aWeight) {
/* 147 */     this.lootPoolChance = aWeight;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setLootItems(LootItem[] items) {
/* 153 */     return setLootItems(Arrays.asList(items));
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool setLootPoolChanceFunc(LootPoolChanceFunc func) {
/* 158 */     this.lootPoolChanceFunc = func;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean passedChanceRoll(Creature victim, Creature receiver) {
/* 164 */     boolean chance = this.lootPoolChanceFunc.chance(victim, receiver, this);
/* 165 */     if (!chance)
/* 166 */       logger.info("Skipping loot pool '" + getName() + "' for " + receiver.getName() + ": failed chance roll."); 
/* 167 */     return chance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendLootMessages(Creature victim, Creature receiver, Item item) {
/* 172 */     this.itemLootMessageFunc.message(victim, receiver, item);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool addIncludeIds(Integer... ids) {
/* 177 */     this.includeIds.addAll(Arrays.asList(ids));
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPool addExcludeIds(Integer... ids) {
/* 183 */     this.excludeIds.addAll(Arrays.asList(ids));
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public LootPool() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\LootPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */