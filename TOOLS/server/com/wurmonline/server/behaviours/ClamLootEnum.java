/*     */ package com.wurmonline.server.behaviours;
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
/*     */ public enum ClamLootEnum
/*     */ {
/*  33 */   NONE(-1, false, false),
/*  34 */   PEARL(1397, false, false),
/*  35 */   COIN(51, false, false),
/*  36 */   IRON_LUMP(46, true, false),
/*  37 */   LEAD_LUMP(49, true, false),
/*  38 */   COPPER_LUMP(47, true, false),
/*  39 */   TIN_LUMP(220, true, false),
/*  40 */   ZINC_LUMP(48, true, false),
/*  41 */   SILVER_LUMP(45, true, false),
/*  42 */   GOLD_LUMP(44, true, false),
/*  43 */   SALT(349, true, false),
/*  44 */   FLINT(446, true, false),
/*  45 */   MEAT(92, false, false),
/*  46 */   LINE_LIGHT(1348, true, false),
/*  47 */   LINE_MEDIUM(1349, true, false),
/*  48 */   LINE_HEAVY(1350, true, false),
/*  49 */   LINE_BRAIDED(1351, true, false),
/*  50 */   HANDLE(99, true, true),
/*  51 */   HANDLE_LEATHER(101, true, true),
/*  52 */   HANDLE_REINFORCED(1370, true, true),
/*  53 */   HANDLE_PADDED(1371, true, true),
/*  54 */   REEL_LIGHT(1372, true, true),
/*  55 */   REEL_MEDIUM(1373, true, true),
/*  56 */   REEL_DEEP(1374, true, true),
/*  57 */   REEL_PROFESSIONAL(1375, true, true),
/*  58 */   REEL_WOOD(1367, true, true),
/*  59 */   REEL_METAL(1368, true, true),
/*  60 */   HOOK_WOOD(1356, true, true),
/*  61 */   HOOK_METAL(1357, true, true),
/*  62 */   HOOK_BONE(1358, true, false),
/*  63 */   NAILS_SMALL(218, true, true),
/*  64 */   NAILS_LARGE(217, true, true),
/*  65 */   RIVET(131, true, true),
/*  66 */   SEED_CABBAGE(1146, true, false),
/*  67 */   SEED_PUMPKIN(34, true, false),
/*  68 */   SEED_WEMP(317, true, false),
/*  69 */   SEED_REED(744, true, false),
/*  70 */   SEED_COTTON(145, true, false),
/*  71 */   SEED_STRAWBERRY(750, true, false),
/*  72 */   SEED_FENNEL(1151, true, false),
/*  73 */   SEED_CARROT(1145, true, false),
/*  74 */   SEED_TOMATO(1147, true, false),
/*  75 */   SEED_SUGARBEET(1148, true, false),
/*  76 */   SEED_LETTUCE(1149, true, false),
/*  77 */   SEED_CUCUMBER(1248, true, false),
/*  78 */   SEED_PAPRIKA(1153, true, false),
/*  79 */   SEED_TURMERIC(1154, true, false),
/*  80 */   COCOABEAN(1155, true, false),
/*  81 */   FRAGMENT(1307, false, false);
/*     */   
/*     */   private final int templateId;
/*     */   private final boolean canHaveDamage;
/*     */   private final boolean randomMaterial;
/*     */   private static final Logger logger;
/*     */   
/*     */   ClamLootEnum(int templateId, boolean canHaveDamage, boolean randomMaterial) {
/*  89 */     this.templateId = templateId;
/*  90 */     this.canHaveDamage = canHaveDamage;
/*  91 */     this.randomMaterial = randomMaterial;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTemplateId() {
/*  96 */     return this.templateId;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHaveDamage() {
/* 101 */     return this.canHaveDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean randomMaterial() {
/* 106 */     return this.randomMaterial;
/*     */   }
/*     */   static {
/* 109 */     logger = Logger.getLogger(ClamLootEnum.class.getName());
/*     */   }
/*     */   
/*     */   public static ClamLootEnum[] getLootTable() {
/* 113 */     ClamLootEnum[] loot = { COIN, IRON_LUMP, IRON_LUMP, IRON_LUMP, IRON_LUMP, IRON_LUMP, IRON_LUMP, LEAD_LUMP, COPPER_LUMP, LEAD_LUMP, TIN_LUMP, ZINC_LUMP, SILVER_LUMP, GOLD_LUMP, SALT, FLINT, MEAT, LINE_LIGHT, LINE_MEDIUM, LINE_HEAVY, LINE_BRAIDED, PEARL, PEARL, PEARL, HANDLE, HANDLE_LEATHER, HANDLE_REINFORCED, HANDLE_PADDED, REEL_LIGHT, REEL_MEDIUM, REEL_DEEP, REEL_PROFESSIONAL, REEL_WOOD, REEL_METAL, HOOK_WOOD, HOOK_METAL, HOOK_BONE, NAILS_SMALL, NAILS_LARGE, RIVET, SEED_CABBAGE, SEED_PUMPKIN, SEED_WEMP, SEED_REED, SEED_COTTON, SEED_STRAWBERRY, SEED_FENNEL, SEED_CARROT, SEED_TOMATO, SEED_SUGARBEET, SEED_LETTUCE, SEED_CUCUMBER, SEED_PAPRIKA, SEED_TURMERIC, COCOABEAN, PEARL, FRAGMENT, FRAGMENT, FRAGMENT, FRAGMENT, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, PEARL, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, MEAT, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE };
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
/* 148 */     if (loot.length != 256)
/* 149 */       logger.log(Level.SEVERE, "Wrong lenght (" + loot.length + ") loot table", new Exception("Bad loot table!")); 
/* 150 */     return loot;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ClamLootEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */