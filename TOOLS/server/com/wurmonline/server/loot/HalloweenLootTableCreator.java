/*     */ package com.wurmonline.server.loot;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HalloweenLootTableCreator
/*     */ {
/*  20 */   protected static final Logger logger = Logger.getLogger(LootTableCreator.class.getName());
/*     */ 
/*     */   
/*     */   private static Optional<Item> itemCreator(Creature victim, Creature receiver, LootItem lootItem, byte material) {
/*  24 */     byte rarity = LootTable.rollForRarity(receiver);
/*  25 */     float quality = 50.0F + Server.rand.nextFloat() * 40.0F;
/*  26 */     Optional<Item> item = ItemFactory.createItemOptional(lootItem.getItemTemplateId(), quality, rarity, material, victim.getName());
/*  27 */     item.ifPresent(i -> {
/*     */           i.setMaterial(material);
/*     */           i.setRarity(rarity);
/*     */         });
/*  31 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean lootPoolChance(Creature victim, Creature receiver, LootPool pool) {
/*  36 */     int r = pool.getRandom().nextInt((int)(650.0F / victim.getBaseCombatRating()));
/*  37 */     boolean success = (victim.isUnique() || r == 0);
/*  38 */     if (!success)
/*  39 */       logger.info("Loot Pool Chance failed '" + pool.getName() + "' for " + receiver.getName() + ": r = " + r); 
/*  40 */     return success;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootPool[] createPools() {
/*  48 */     LootItem boneSkullMask = (new LootItem()).setItemTemplateId(1428).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)35));
/*     */     
/*  50 */     LootItem goldSkullMask = (new LootItem()).setItemTemplateId(1428).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)7));
/*     */     
/*  52 */     LootItem silverSkullMask = (new LootItem()).setItemTemplateId(1428).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)8));
/*     */     
/*  54 */     LootItem oleanderSkullMask = (new LootItem()).setItemTemplateId(1428).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)51));
/*     */     
/*  56 */     LootItem trollMask = (new LootItem()).setItemTemplateId(1321).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)0));
/*     */ 
/*     */     
/*  59 */     LootItem witchHat = (new LootItem()).setItemTemplateId(1429).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)0)).setItemChance(1.0D);
/*     */ 
/*     */     
/*  62 */     LootItem pumpkinShoulders = (new LootItem()).setItemTemplateId(1322).setItemCreateFunc((victim, receiver, lootItem) -> itemCreator(victim, receiver, lootItem, (byte)0)).setItemChance(0.25D);
/*     */ 
/*     */ 
/*     */     
/*  66 */     return new LootPool[] { (new LootPool())
/*     */ 
/*     */         
/*  69 */         .setName("halloween: shoulders & hat")
/*  70 */         .addExcludeIds(new Integer[] { Integer.valueOf(11), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(23)
/*  71 */           }).setActiveFunc((victim, receiver) -> ((WurmCalendar.isHalloween() || Servers.localServer.testServer) && victim.isHunter()))
/*  72 */         .setLootPoolChanceFunc(HalloweenLootTableCreator::lootPoolChance)
/*  73 */         .setLootItems(new LootItem[] { pumpkinShoulders, witchHat
/*  74 */           }).setItemFunc(new PercentChanceLootItemFunc()), (new LootPool())
/*     */ 
/*     */         
/*  77 */         .setName("halloween: troll mask & skull mask")
/*  78 */         .addIncludeIds(new Integer[] { Integer.valueOf(11), Integer.valueOf(27)
/*  79 */           }).setActiveFunc((victim, receiver) -> (WurmCalendar.isHalloween() || Servers.localServer.testServer))
/*  80 */         .setLootPoolChance(0.04D)
/*  81 */         .setLootPoolChanceFunc((new IncreasingChanceLootPoolChanceFunc()).setPercentIncrease(0.001D))
/*  82 */         .setLootItems(new LootItem[] { boneSkullMask, goldSkullMask, silverSkullMask, oleanderSkullMask, trollMask
/*  83 */           }).setItemFunc(new RandomIndexLootItemFunc()), (new LootPool())
/*     */ 
/*     */ 
/*     */         
/*  87 */         .setName("halloween: skull mask only")
/*  88 */         .addIncludeIds(new Integer[] { Integer.valueOf(26), Integer.valueOf(23)
/*  89 */           }).setActiveFunc((victim, receiver) -> (WurmCalendar.isHalloween() || Servers.localServer.testServer))
/*  90 */         .setLootPoolChance(0.04D)
/*  91 */         .setLootPoolChanceFunc((new IncreasingChanceLootPoolChanceFunc()).setPercentIncrease(0.001D))
/*  92 */         .setLootItems(new LootItem[] { boneSkullMask, goldSkullMask, silverSkullMask, oleanderSkullMask
/*  93 */           }).setItemFunc(new RandomIndexLootItemFunc()) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initialize() {
/* 101 */     LootPool[] halloweenPools = createPools();
/* 102 */     for (CreatureTemplate template : CreatureTemplateFactory.getInstance().getTemplates()) {
/* 103 */       if (template.isHunter())
/* 104 */         template.addLootPool(halloweenPools); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\HalloweenLootTableCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */