/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.Structure;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class VolaTileItems
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(VolaTileItems.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private int[] itemsPerLevel = new int[1];
/*  47 */   private int[] decoItemsPerLevel = new int[1];
/*  48 */   private final int PILECOUNT = 3;
/*     */   private boolean hasFire = false;
/*  50 */   private final int maxFloorLevel = 20;
/*  51 */   private ConcurrentHashMap<Integer, Item> pileItems = new ConcurrentHashMap<>();
/*  52 */   private ConcurrentHashMap<Integer, Item> onePerTileItems = new ConcurrentHashMap<>();
/*  53 */   private ConcurrentHashMap<Integer, Set<Item>> fourPerTileItems = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Set<Item> allItems = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<Item> alwaysPoll;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void incrementItemsOnLevelByOne(int level) {
/*  76 */     if (this.itemsPerLevel.length > level)
/*     */     {
/*  78 */       this.itemsPerLevel[level] = this.itemsPerLevel[level] + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void incrementDecoItemsOnLevelByOne(int level) {
/*  84 */     if (this.decoItemsPerLevel.length > level) {
/*  85 */       this.decoItemsPerLevel[level] = this.decoItemsPerLevel[level] + 1;
/*     */     }
/*     */   }
/*     */   
/*     */   private void decrementItemsOnLevelByOne(int level) {
/*  90 */     if (this.itemsPerLevel.length > level)
/*     */     {
/*  92 */       if (this.itemsPerLevel[level] > 0) {
/*  93 */         this.itemsPerLevel[level] = this.itemsPerLevel[level] - 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void decrementDecoItemsOnLevelByOne(int level) {
/*  99 */     if (this.decoItemsPerLevel.length > level)
/*     */     {
/* 101 */       if (this.decoItemsPerLevel[level] > 0) {
/* 102 */         this.decoItemsPerLevel[level] = this.decoItemsPerLevel[level] - 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean addItem(Item item, boolean starting) {
/* 108 */     if (!this.allItems.contains(item)) {
/*     */       
/* 110 */       if (item.isFire()) {
/*     */ 
/*     */         
/* 113 */         if (this.hasFire)
/*     */         {
/* 115 */           if (item.getTemplateId() == 37) {
/*     */             
/* 117 */             Item c = getCampfire(false);
/* 118 */             if (c != null) {
/*     */               
/* 120 */               short maxTemp = 30000;
/* 121 */               c.setTemperature((short)Math.min(30000, c.getTemperature() + item.getWeightGrams()));
/* 122 */               Items.destroyItem(item.getWurmId());
/* 123 */               return false;
/*     */             } 
/*     */           } 
/*     */         }
/* 127 */         this.hasFire = true;
/*     */       } 
/* 129 */       if (item.isOnePerTile()) {
/* 130 */         this.onePerTileItems.put(Integer.valueOf(item.getFloorLevel()), item);
/*     */       }
/* 132 */       if (item.isFourPerTile()) {
/*     */         
/* 134 */         Set<Item> itemSet = this.fourPerTileItems.get(Integer.valueOf(item.getFloorLevel()));
/* 135 */         if (itemSet == null) {
/* 136 */           itemSet = new HashSet<>();
/*     */         }
/* 138 */         int count = getFourPerTileCount(item.getFloorLevel());
/* 139 */         if (item.isPlanted() && count >= 4) {
/*     */           
/* 141 */           logger.info("Unplanted " + item.getName() + " (" + item.getWurmId() + ") as tile " + item.getTileX() + "," + item
/* 142 */               .getTileY() + " already has " + count + " items");
/* 143 */           item.setIsPlanted(false);
/*     */         } 
/* 145 */         itemSet.add(item);
/* 146 */         this.fourPerTileItems.put(Integer.valueOf(item.getFloorLevel()), itemSet);
/*     */       } 
/* 148 */       int fl = trimFloorLevel(item.getFloorLevel());
/* 149 */       if (fl + 1 > this.itemsPerLevel.length)
/*     */       {
/* 151 */         this.itemsPerLevel = createNewLevelArrayAt(fl + 1);
/*     */       }
/*     */       
/* 154 */       if (item.isDecoration()) {
/*     */         
/* 156 */         if (fl + 1 > this.decoItemsPerLevel.length)
/*     */         {
/* 158 */           this.decoItemsPerLevel = createNewDecoLevelArrayAt(fl + 1);
/*     */         }
/* 160 */         incrementDecoItemsOnLevelByOne(fl);
/*     */       } 
/*     */       
/* 163 */       this.allItems.add(item);
/* 164 */       incrementItemsOnLevelByOne(fl);
/* 165 */       if (item.isTent())
/* 166 */         Items.addTent(item); 
/* 167 */       if (item.isRoadMarker() && item.isPlanted() && Features.Feature.HIGHWAYS.isEnabled())
/* 168 */         Items.addMarker(item); 
/* 169 */       if (item.getTemplateId() == 677 && item.isPlanted())
/* 170 */         Items.addGmSign(item); 
/* 171 */       if (item.getTemplateId() == 1309 && item.isPlanted() && Features.Feature.HIGHWAYS.isEnabled())
/* 172 */         Items.addWagonerContainer(item); 
/* 173 */       if (item.isAlwaysPoll()) {
/* 174 */         addAlwaysPollItem(item);
/*     */       }
/* 176 */       if (item.isSpawnPoint())
/* 177 */         Items.addSpawn(item); 
/* 178 */       return true;
/*     */     } 
/* 180 */     return false;
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
/*     */   private final int[] createNewLevelArrayAt(int floorLevels) {
/* 193 */     int[] newArr = new int[floorLevels];
/* 194 */     for (int level = 0; level < floorLevels; level++) {
/*     */       
/* 196 */       if (level < this.itemsPerLevel.length)
/* 197 */         newArr[level] = this.itemsPerLevel[level]; 
/*     */     } 
/* 199 */     return newArr;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int[] createNewDecoLevelArrayAt(int floorLevels) {
/* 204 */     int[] newArr = new int[floorLevels];
/* 205 */     for (int level = 0; level < floorLevels; level++) {
/*     */       
/* 207 */       if (level < this.decoItemsPerLevel.length)
/* 208 */         newArr[level] = this.decoItemsPerLevel[level]; 
/*     */     } 
/* 210 */     return newArr;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean contains(Item item) {
/* 215 */     return this.allItems.contains(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean removeItem(Item item) {
/* 220 */     if (item.isAlwaysPoll())
/* 221 */       removeAlwaysPollItem(item); 
/* 222 */     int fl = trimFloorLevel(item.getFloorLevel());
/* 223 */     decrementItemsOnLevelByOne(fl);
/*     */     
/* 225 */     if (item.isDecoration())
/*     */     {
/* 227 */       decrementDecoItemsOnLevelByOne(fl);
/*     */     }
/* 229 */     this.allItems.remove(item);
/* 230 */     if (item.isFire())
/* 231 */       this.hasFire = stillHasFire(); 
/* 232 */     if (item.isOnePerTile())
/* 233 */       this.onePerTileItems.remove(Integer.valueOf(item.getFloorLevel())); 
/* 234 */     if (item.isTent())
/* 235 */       Items.removeTent(item); 
/* 236 */     if (item.isSpawnPoint())
/* 237 */       Items.removeSpawn(item); 
/* 238 */     return isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   final void destroy(VolaTile toSendTo) {
/* 243 */     for (Item i : this.allItems)
/*     */     {
/*     */ 
/*     */       
/* 247 */       toSendTo.sendRemoveItem(i);
/*     */     }
/* 249 */     for (Item pile : this.pileItems.values())
/*     */     {
/*     */ 
/*     */       
/* 253 */       toSendTo.sendRemoveItem(pile);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void moveToNewFloorLevel(Item item, int oldFloorLevel) {
/* 259 */     if (item.getFloorLevel() != oldFloorLevel) {
/*     */       
/* 261 */       int fl = trimFloorLevel(item.getFloorLevel());
/* 262 */       int old = trimFloorLevel(oldFloorLevel);
/*     */       
/* 264 */       if (fl + 1 > this.itemsPerLevel.length)
/*     */       {
/* 266 */         this.itemsPerLevel = createNewLevelArrayAt(fl + 1);
/*     */       }
/* 268 */       incrementItemsOnLevelByOne(fl);
/* 269 */       decrementItemsOnLevelByOne(old);
/*     */       
/* 271 */       if (item.isDecoration()) {
/*     */         
/* 273 */         if (fl + 1 > this.decoItemsPerLevel.length)
/*     */         {
/* 275 */           this.decoItemsPerLevel = createNewDecoLevelArrayAt(fl + 1);
/*     */         }
/* 277 */         incrementDecoItemsOnLevelByOne(fl);
/* 278 */         decrementDecoItemsOnLevelByOne(old);
/*     */       } 
/*     */     } 
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
/*     */   boolean movePileItemToNewFloorLevel(Item item, int oldFloorLevel) {
/* 292 */     if (item.getFloorLevel() != oldFloorLevel) {
/*     */       
/* 294 */       Item oldPile = this.pileItems.get(Integer.valueOf(oldFloorLevel));
/* 295 */       if (oldPile == item)
/*     */       {
/* 297 */         this.pileItems.remove(Integer.valueOf(oldFloorLevel));
/*     */       }
/* 299 */       Item newPile = this.pileItems.get(Integer.valueOf(item.getFloorLevel()));
/* 300 */       if (newPile == null) {
/*     */         
/* 302 */         this.pileItems.put(Integer.valueOf(item.getFloorLevel()), item);
/*     */       }
/* 304 */       else if (newPile != item) {
/*     */         
/* 306 */         return true;
/*     */       } 
/*     */     } 
/* 309 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final Item[] getPileItems() {
/* 314 */     return (Item[])this.pileItems.values().toArray((Object[])new Item[this.pileItems.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean stillHasFire() {
/* 319 */     if (getCampfire(true) != null)
/* 320 */       return true; 
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean hasOnePerTileItem(int floorLevel) {
/* 326 */     return (this.onePerTileItems.get(Integer.valueOf(floorLevel)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Item getOnePerTileItem(int floorLevel) {
/* 331 */     return this.onePerTileItems.get(Integer.valueOf(floorLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int getFourPerTileCount(int floorLevel) {
/* 336 */     Set<Item> itemSet = this.fourPerTileItems.get(Integer.valueOf(floorLevel));
/* 337 */     if (itemSet == null) {
/* 338 */       return 0;
/*     */     }
/* 340 */     int count = 0;
/* 341 */     for (Item item : itemSet) {
/*     */       
/* 343 */       if (item.isPlanted())
/* 344 */         count++; 
/* 345 */       if (item.getTemplateId() == 1311)
/* 346 */         count++; 
/*     */     } 
/* 348 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasFire() {
/* 358 */     return this.hasFire;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Item[] getAllItemsAsArray() {
/* 363 */     return this.allItems.<Item>toArray(new Item[this.allItems.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Set<Item> getAllItemsAsSet() {
/* 368 */     return this.allItems;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int trimFloorLevel(int floorLevel) {
/* 373 */     return Math.min(20, Math.max(0, floorLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNumberOfItems(int floorLevel) {
/* 378 */     if (this.itemsPerLevel == null)
/* 379 */       return 0; 
/* 380 */     if (this.itemsPerLevel.length - 1 < trimFloorLevel(floorLevel))
/* 381 */       return 0; 
/* 382 */     return this.itemsPerLevel[trimFloorLevel(floorLevel)];
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNumberOfDecorations(int floorLevel) {
/* 387 */     if (this.decoItemsPerLevel == null)
/* 388 */       return 0; 
/* 389 */     if (this.decoItemsPerLevel.length - 1 < trimFloorLevel(floorLevel))
/* 390 */       return 0; 
/* 391 */     return this.decoItemsPerLevel[trimFloorLevel(floorLevel)];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void poll(boolean pollItems, int seed, boolean lava, Structure structure, boolean surfaced, Village village, long now) {
/* 397 */     if (pollItems) {
/*     */       
/* 399 */       Item[] lTempItems = getAllItemsAsArray();
/* 400 */       for (int x = 0; x < lTempItems.length; x++) {
/*     */         
/* 402 */         if (lava && !lTempItems[x].isIndestructible() && !lTempItems[x].isHugeAltar()) {
/* 403 */           lTempItems[x].setDamage(lTempItems[x].getDamage() + 1.0F);
/*     */         }
/*     */         else {
/*     */           
/* 407 */           lTempItems[x].poll(((structure != null && structure.isFinished()) || !surfaced), (village != null), 1L);
/*     */         }
/*     */       
/*     */       } 
/* 411 */     } else if (this.alwaysPoll != null) {
/*     */       
/* 413 */       Item[] lTempItems = this.alwaysPoll.<Item>toArray(new Item[this.alwaysPoll.size()]);
/* 414 */       for (int x = 0; x < lTempItems.length; x++) {
/*     */         
/* 416 */         if (!lTempItems[x].poll(((structure != null && structure.isFinished()) || !surfaced), (village != null), seed) && lava)
/*     */         {
/* 418 */           lTempItems[x].setDamage(lTempItems[x].getDamage() + 0.1F);
/*     */         }
/*     */       } 
/*     */     } 
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
/*     */   private void addAlwaysPollItem(Item item) {
/* 433 */     if (this.alwaysPoll == null)
/* 434 */       this.alwaysPoll = new HashSet<>(); 
/* 435 */     this.alwaysPoll.add(item);
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAlwaysPollItem(Item item) {
/* 440 */     if (this.alwaysPoll != null) {
/*     */       
/* 442 */       this.alwaysPoll.remove(item);
/* 443 */       if (this.alwaysPoll.isEmpty()) {
/* 444 */         this.alwaysPoll = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   final void removePileItem(int floorLevel) {
/* 450 */     this.pileItems.remove(Integer.valueOf(floorLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean checkIfCreatePileItem(int floorLevel) {
/* 455 */     if (this.itemsPerLevel.length - 1 < trimFloorLevel(floorLevel))
/* 456 */       return false; 
/* 457 */     int decoItems = 0;
/* 458 */     if (this.decoItemsPerLevel.length > trimFloorLevel(floorLevel)) {
/* 459 */       decoItems = this.decoItemsPerLevel[trimFloorLevel(floorLevel)];
/*     */     }
/* 461 */     if (this.itemsPerLevel[trimFloorLevel(floorLevel)] <= decoItems + 3 - 1)
/* 462 */       return false; 
/* 463 */     if (this.itemsPerLevel[trimFloorLevel(floorLevel)] < 3)
/* 464 */       return false; 
/* 465 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean checkIfRemovePileItem(int floorLevel) {
/* 470 */     if (this.itemsPerLevel.length - 1 < trimFloorLevel(floorLevel))
/*     */     {
/* 472 */       return false;
/*     */     }
/* 474 */     int decoItems = 0;
/* 475 */     if (this.decoItemsPerLevel.length > trimFloorLevel(floorLevel)) {
/* 476 */       decoItems = this.decoItemsPerLevel[trimFloorLevel(floorLevel)];
/*     */     }
/* 478 */     if (this.itemsPerLevel[trimFloorLevel(floorLevel)] < decoItems + 3)
/* 479 */       return true; 
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   final Item getPileItem(int floorLevel) {
/* 485 */     return this.pileItems.get(Integer.valueOf(floorLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   final void addPileItem(Item pile) {
/* 490 */     this.pileItems.put(Integer.valueOf(pile.getFloorLevel()), pile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Item getCampfire(boolean requiresBurning) {
/* 501 */     for (Item i : this.allItems) {
/*     */       
/* 503 */       if (i.getTemplateId() == 37 && (!requiresBurning || i.getTemperature() >= 1000))
/* 504 */         return i; 
/*     */     } 
/* 506 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 511 */     return (this.allItems == null || this.allItems.size() == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\VolaTileItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */