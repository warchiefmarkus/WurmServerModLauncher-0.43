/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class AwardLadder
/*     */   implements MiscConstants, ItemMaterials
/*     */ {
/*     */   private final String name;
/*     */   private final String imageUrl;
/*     */   private final int monthsRequiredSinceReset;
/*     */   private final int totalMonthsRequired;
/*     */   private final int silversRequired;
/*     */   private final int titleAwarded;
/*     */   private final int itemAwarded;
/*     */   private final int achievementAwarded;
/*     */   private final float qlOfItemAward;
/*  58 */   private static final Logger logger = Logger.getLogger(AwardLadder.class.getName());
/*  59 */   private static final Map<Integer, AwardLadder> resetLadder = new ConcurrentHashMap<>();
/*  60 */   private static final Map<Integer, AwardLadder> totalLadder = new ConcurrentHashMap<>();
/*  61 */   private static final Map<Integer, AwardLadder> silverLadder = new ConcurrentHashMap<>();
/*  62 */   private static final Map<Player, Set<Item>> itemsToAward = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  66 */     generateLadder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AwardLadder(String _name, String _imageUrl, int _totalMonthsRequired, int _monthsRequiredSinceReset, int _silversRequired, int _titleAwarded, int _itemAwarded, int _achievementAwarded, float qlAwarded) {
/*  73 */     this.name = _name;
/*  74 */     this.imageUrl = _imageUrl;
/*  75 */     this.monthsRequiredSinceReset = _monthsRequiredSinceReset;
/*  76 */     this.totalMonthsRequired = _totalMonthsRequired;
/*  77 */     this.silversRequired = _silversRequired;
/*  78 */     this.titleAwarded = _titleAwarded;
/*  79 */     this.itemAwarded = _itemAwarded;
/*  80 */     this.achievementAwarded = _achievementAwarded;
/*  81 */     this.qlOfItemAward = qlAwarded;
/*  82 */     if (this.monthsRequiredSinceReset > 0)
/*  83 */       resetLadder.put(Integer.valueOf(this.monthsRequiredSinceReset), this); 
/*  84 */     if (this.totalMonthsRequired > 0)
/*  85 */       totalLadder.put(Integer.valueOf(this.totalMonthsRequired), this); 
/*  86 */     if (this.silversRequired > 0) {
/*  87 */       silverLadder.put(Integer.valueOf(this.silversRequired), this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final AwardLadder getLadderStepForReset(int months) {
/*  92 */     return resetLadder.get(Integer.valueOf(months));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AwardLadder[] getLadderStepsForTotal(int months) {
/*  97 */     Set<AwardLadder> steps = new HashSet<>();
/*  98 */     for (AwardLadder step : totalLadder.values()) {
/*     */       
/* 100 */       if (step.getTotalMonthsRequired() <= months)
/* 101 */         steps.add(step); 
/*     */     } 
/* 103 */     return steps.<AwardLadder>toArray(new AwardLadder[steps.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AwardLadder getLadderStepForSilver(int silver) {
/* 108 */     return silverLadder.get(Integer.valueOf(silver));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 118 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImageUrl() {
/* 128 */     return this.imageUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonthsRequiredReset() {
/* 138 */     return this.monthsRequiredSinceReset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalMonthsRequired() {
/* 148 */     return this.totalMonthsRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSilversRequired() {
/* 158 */     return this.silversRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTitleNumberAwarded() {
/* 168 */     return this.titleAwarded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItemNumberAwarded() {
/* 178 */     return this.itemAwarded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAchievementNumberAwarded() {
/* 188 */     return this.achievementAwarded;
/*     */   }
/*     */ 
/*     */   
/*     */   public Titles.Title getTitleAwarded() {
/* 193 */     return Titles.Title.getTitle(this.titleAwarded);
/*     */   }
/*     */ 
/*     */   
/*     */   public AchievementTemplate getAchievementAwarded() {
/* 198 */     return Achievement.getTemplate(this.achievementAwarded);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemTemplate getItemAwarded() {
/*     */     try {
/* 205 */       return ItemTemplateFactory.getInstance().getTemplate(this.itemAwarded);
/*     */     }
/* 207 */     catch (NoSuchTemplateException nst) {
/*     */       
/* 209 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AwardLadder getNextTotalAward(int totalMonthsSinceReset) {
/* 215 */     AwardLadder next = null;
/* 216 */     for (Map.Entry<Integer, AwardLadder> entry : resetLadder.entrySet()) {
/*     */       
/* 218 */       if (next == null) {
/*     */         
/* 220 */         if (((Integer)entry.getKey()).intValue() > totalMonthsSinceReset)
/* 221 */           next = entry.getValue();  continue;
/*     */       } 
/* 223 */       if (((Integer)entry.getKey()).intValue() > totalMonthsSinceReset && ((Integer)entry
/* 224 */         .getKey()).intValue() < next.getMonthsRequiredReset())
/* 225 */         next = entry.getValue(); 
/*     */     } 
/* 227 */     return next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void clearItemAwards() {
/* 235 */     for (Map.Entry<Player, Set<Item>> entry : itemsToAward.entrySet()) {
/*     */       
/* 237 */       for (Item i : entry.getValue()) {
/*     */         
/* 239 */         ((Player)entry.getKey()).getInventory().insertItem(i, true);
/* 240 */         if (((Player)entry.getKey()).getCommunicator() != null)
/* 241 */           ((Player)entry.getKey()).getCommunicator()
/* 242 */             .sendSafeServerMessage("You receive " + i.getNameWithGenus() + " as premium bonus!"); 
/*     */       } 
/*     */     } 
/* 245 */     itemsToAward.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void awardTotalLegacy(PlayerInfo p) {
/* 250 */     if (p.awards != null) {
/*     */       
/*     */       try {
/*     */         
/* 254 */         AwardLadder[] total = getLadderStepsForTotal(p.awards.getMonthsPaidEver());
/*     */         
/* 256 */         Set<Item> itemSet = new HashSet<>();
/*     */         
/* 258 */         for (AwardLadder step : total) {
/*     */           
/* 260 */           if (step.getItemNumberAwarded() > 0)
/*     */           {
/* 262 */             if (step.getItemNumberAwarded() == 229) {
/*     */               
/* 264 */               int numawarded = 274 + Server.rand.nextInt(14);
/*     */               
/*     */               try {
/* 267 */                 Player player = Players.getInstance().getPlayer(p.wurmId);
/*     */                 
/* 269 */                 Item i = ItemFactory.createItem(numawarded, step.getQlOfItemAward(), (byte)67, (byte)0, "");
/*     */                 
/* 271 */                 itemSet.add(i);
/* 272 */                 itemsToAward.put(player, itemSet);
/*     */               }
/* 274 */               catch (NoSuchPlayerException nsp) {
/*     */                 
/* 276 */                 long inventoryId = DbCreatureStatus.getInventoryIdFor(p.wurmId);
/* 277 */                 Item i = ItemFactory.createItem(numawarded, step.getQlOfItemAward(), (byte)67, (byte)0, "");
/*     */                 
/* 279 */                 i.setParentId(inventoryId, true);
/* 280 */                 i.setOwnerId(p.wurmId);
/*     */               } 
/*     */             } else {
/*     */ 
/*     */               
/*     */               try {
/* 286 */                 Player player = Players.getInstance().getPlayer(p.wurmId);
/*     */                 
/* 288 */                 Item i = ItemFactory.createItem(step.getItemNumberAwarded(), step.getQlOfItemAward(), "");
/*     */                 
/* 290 */                 itemSet.add(i);
/* 291 */                 itemsToAward.put(player, itemSet);
/*     */               }
/* 293 */               catch (NoSuchPlayerException nsp) {
/*     */                 
/* 295 */                 long inventoryId = DbCreatureStatus.getInventoryIdFor(p.wurmId);
/* 296 */                 Item i = ItemFactory.createItem(step.getItemNumberAwarded(), step.getQlOfItemAward(), "");
/*     */                 
/* 298 */                 i.setParentId(inventoryId, true);
/* 299 */                 i.setOwnerId(p.wurmId);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 304 */       } catch (Exception ex) {
/*     */         
/* 306 */         logger.log(Level.WARNING, ex.getMessage() + " " + p.getName() + " " + p.awards.getMonthsPaidSinceReset() + ": " + p.awards
/* 307 */             .getMonthsPaidInARow(), ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float consecutiveItemQL(int consecutiveMonths) {
/* 314 */     return Math.min(100.0F, (consecutiveMonths * 16));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void award(PlayerInfo p, boolean tickedMonth) {
/* 319 */     if (p.awards != null) {
/*     */       
/*     */       try {
/*     */         
/* 323 */         if (tickedMonth) {
/*     */           
/* 325 */           float ql = consecutiveItemQL(p.awards.getMonthsPaidInARow());
/*     */           
/*     */           try {
/* 328 */             Player player = Players.getInstance().getPlayer(p.wurmId);
/*     */             
/* 330 */             Item inventory = player.getInventory();
/* 331 */             Item i = ItemFactory.createItem(834, ql, "");
/* 332 */             inventory.insertItem(i, true);
/* 333 */             player.getCommunicator().sendSafeServerMessage("You receive " + i
/* 334 */                 .getNameWithGenus() + " at ql " + ql + " for staying premium!");
/*     */           }
/* 336 */           catch (NoSuchPlayerException nsp) {
/*     */             
/* 338 */             long inventoryId = DbCreatureStatus.getInventoryIdFor(p.wurmId);
/* 339 */             Item i = ItemFactory.createItem(834, ql, "");
/* 340 */             i.setParentId(inventoryId, true);
/* 341 */             i.setOwnerId(p.wurmId);
/*     */           } 
/* 343 */           AwardLadder sinceReset = getLadderStepForReset(p.awards.getMonthsPaidSinceReset());
/* 344 */           if (sinceReset != null)
/*     */           {
/* 346 */             if (sinceReset.getTitleAwarded() != null) {
/*     */               
/* 348 */               p.addTitle(sinceReset.getTitleAwarded());
/*     */               
/*     */               try {
/* 351 */                 Player player = Players.getInstance().getPlayer(p.wurmId);
/* 352 */                 player.getCommunicator().sendSafeServerMessage("You receive the title " + sinceReset
/*     */                     
/* 354 */                     .getTitleAwarded().getName((player.getSex() == 0)) + " for staying premium!");
/*     */               
/*     */               }
/* 357 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 362 */             if (sinceReset.getAchievementNumberAwarded() > 0)
/* 363 */               Achievements.triggerAchievement(p.wurmId, sinceReset.getAchievementNumberAwarded()); 
/* 364 */             if (sinceReset.getItemNumberAwarded() > 0) {
/*     */               try
/*     */               {
/*     */                 
/* 368 */                 Player player = Players.getInstance().getPlayer(p.wurmId);
/*     */                 
/* 370 */                 Item i = ItemFactory.createItem(sinceReset.getItemNumberAwarded(), sinceReset
/* 371 */                     .getQlOfItemAward(), "");
/*     */                 
/* 373 */                 player.getInventory().insertItem(i, true);
/*     */               }
/* 375 */               catch (NoSuchPlayerException nsp)
/*     */               {
/* 377 */                 long inventoryId = DbCreatureStatus.getInventoryIdFor(p.wurmId);
/* 378 */                 Item i = ItemFactory.createItem(sinceReset.getItemNumberAwarded(), sinceReset
/* 379 */                     .getQlOfItemAward(), "");
/* 380 */                 i.setParentId(inventoryId, true);
/* 381 */                 i.setOwnerId(p.wurmId);
/*     */               }
/*     */             
/*     */             }
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 389 */           AwardLadder silvers = getLadderStepForSilver(p.awards.getSilversPaidEver());
/* 390 */           if (silvers != null) {
/*     */             
/* 392 */             if (silvers.getTitleAwarded() != null)
/* 393 */               p.addTitle(silvers.getTitleAwarded()); 
/* 394 */             if (silvers.getAchievementNumberAwarded() > 0) {
/* 395 */               Achievements.triggerAchievement(p.wurmId, silvers.getAchievementNumberAwarded());
/*     */             }
/*     */           } 
/*     */         } 
/* 399 */       } catch (Exception ex) {
/*     */         
/* 401 */         logger.log(Level.WARNING, ex.getMessage() + " " + p.getName() + " " + p.awards.getMonthsPaidSinceReset() + ": " + p.awards
/* 402 */             .getMonthsPaidInARow(), ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void generateLadder() {
/* 409 */     new AwardLadder("Title: Soldier of Lomaner", "", 0, 1, 0, 254, 0, 0, 0.0F);
/* 410 */     new AwardLadder("Achievement: Landed", "", 0, 1, 0, 0, 0, 343, 0.0F);
/* 411 */     new AwardLadder("Achievement: Survived", "", 0, 3, 0, 0, 0, 344, 0.0F);
/* 412 */     new AwardLadder("Title: Rider of Lomaner", "", 0, 4, 0, 255, 0, 0, 0.0F);
/* 413 */     new AwardLadder("Achievement: Scouted", "", 0, 6, 0, 0, 0, 345, 0.0F);
/* 414 */     new AwardLadder("Title: Chieftain of Lomaner", "", 0, 7, 0, 256, 0, 0, 0.0F);
/* 415 */     new AwardLadder("Achievement: Experienced", "", 0, 9, 0, 0, 0, 346, 0.0F);
/* 416 */     new AwardLadder("Title: Ambassador of Lomaner", "", 0, 10, 0, 257, 0, 0, 0.0F);
/* 417 */     new AwardLadder("Item: Spyglass", "", 0, 12, 0, 0, 489, 0, 70.0F);
/* 418 */     new AwardLadder("Achievement: Owning", "", 0, 13, 0, 0, 0, 347, 0.0F);
/* 419 */     new AwardLadder("Title: Baron of Lomaner", "", 0, 14, 0, 258, 0, 0, 0.0F);
/* 420 */     new AwardLadder("Achievement: Shined", "", 0, 16, 0, 0, 0, 348, 0.0F);
/* 421 */     new AwardLadder("Title: Jarl of Lomaner", "", 0, 18, 0, 259, 0, 0, 0.0F);
/* 422 */     new AwardLadder("Achievement: Glittered", "", 0, 20, 0, 0, 0, 349, 0.0F);
/* 423 */     new AwardLadder("Title: Duke of Lomaner", "", 0, 23, 0, 260, 0, 0, 0.0F);
/* 424 */     new AwardLadder("Achievement: Highly Illuminated", "", 0, 26, 0, 0, 0, 350, 0.0F);
/* 425 */     new AwardLadder("Title: Provost of Lomaner", "", 0, 30, 0, 261, 0, 0, 0.0F);
/* 426 */     new AwardLadder("Achievement: Foundation Pillar", "", 0, 36, 0, 0, 0, 351, 0.0F);
/* 427 */     new AwardLadder("Title: Marquis of Lomaner", "", 0, 40, 0, 262, 0, 0, 0.0F);
/* 428 */     new AwardLadder("Achievement: Revered One", "", 0, 48, 0, 0, 0, 352, 0.0F);
/* 429 */     new AwardLadder("Title: Grand Duke of Lomaner", "", 0, 54, 0, 263, 0, 0, 0.0F);
/* 430 */     new AwardLadder("Achievement: Patron Of The Net", "", 0, 60, 0, 0, 0, 353, 0.0F);
/* 431 */     new AwardLadder("Title: Viceroy of Lomaner", "", 0, 70, 0, 264, 0, 0, 0.0F);
/* 432 */     new AwardLadder("Achievement: Myth Or Legend?", "", 0, 80, 0, 0, 0, 354, 0.0F);
/* 433 */     new AwardLadder("Title: Prince of Lomaner", "", 0, 100, 0, 265, 0, 0, 0.0F);
/* 434 */     new AwardLadder("Achievement: Atlas Reincarnated", "", 0, 120, 0, 0, 0, 355, 0.0F);
/*     */     
/* 436 */     new AwardLadder("1 month", "", 1, 0, 0, 0, 834, 0, 50.0F);
/* 437 */     new AwardLadder("3 months", "", 3, 0, 0, 0, 700, 0, 80.0F);
/* 438 */     new AwardLadder("6 months", "", 6, 0, 0, 0, 466, 0, 99.0F);
/* 439 */     new AwardLadder("9 months", "", 9, 0, 0, 0, 837, 0, 80.0F);
/* 440 */     new AwardLadder("12 months", "", 12, 0, 0, 0, 229, 0, 30.0F);
/* 441 */     new AwardLadder("15 months", "", 15, 0, 0, 0, 837, 0, 90.0F);
/* 442 */     new AwardLadder("24 months", "", 24, 0, 0, 0, 229, 0, 90.0F);
/* 443 */     new AwardLadder("30 months", "", 30, 0, 0, 0, 837, 0, 90.0F);
/* 444 */     new AwardLadder("36 months", "", 36, 0, 0, 0, 668, 0, 40.0F);
/* 445 */     new AwardLadder("42 months", "", 42, 0, 0, 0, 837, 0, 93.0F);
/* 446 */     new AwardLadder("48 months", "", 48, 0, 0, 0, 837, 0, 94.0F);
/* 447 */     new AwardLadder("54 months", "", 54, 0, 0, 0, 837, 0, 95.0F);
/* 448 */     new AwardLadder("60 months", "", 60, 0, 0, 0, 837, 0, 96.0F);
/*     */     
/* 450 */     logger.info("Finished generating AwardLadder");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getQlOfItemAward() {
/* 460 */     return this.qlOfItemAward;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\AwardLadder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */