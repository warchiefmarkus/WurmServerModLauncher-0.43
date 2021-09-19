/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.spells.EnchantUtil;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import com.wurmonline.server.villages.DeadVillage;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class FragmentUtilities
/*     */ {
/*     */   private static final int DIFFRANGE_TRASH = 0;
/*     */   private static final int DIFFRANGE_0_15 = 1;
/*     */   private static final int DIFFRANGE_15_30 = 2;
/*     */   private static final int DIFFRANGE_30_40 = 3;
/*     */   private static final int DIFFRANGE_40_50 = 4;
/*     */   private static final int DIFFRANGE_50_60 = 5;
/*     */   private static final int DIFFRANGE_60_70 = 6;
/*     */   private static final int DIFFRANGE_70_80 = 7;
/*     */   private static final int DIFFRANGE_80_90 = 8;
/*     */   private static final int DIFFRANGE_90_100 = 9;
/*  37 */   private static int[] diffTrash = new int[] { 776, 786, 1122, 1121, 1123, 132, 38, 39, 43, 41, 40, 207, 42, 785, 785, 146, 688, 23, 454, 561, 551 };
/*     */ 
/*     */ 
/*     */   
/*  41 */   private static int[] diff0_15 = new int[] { 46, 47, 49, 220, 48, 453 };
/*     */   
/*  43 */   private static int[] diff15_30 = new int[] { 1011, 685, 687, 690, 45, 44, 223, 205, 221, 1411, 784, 778, 217, 218, 188, 451, 1408, 1407, 1416 };
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static int[] diff30_40 = new int[] { 77, 813, 1161, 76, 78, 1020, 523, 127, 154, 389, 125, 126, 124, 123, 395, 270, 121, 269, 494, 452, 1406, 1421, 1418 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static int[] diff40_50 = new int[] { 1022, 1172, 1169, 1165, 1252, 1323, 1324, 1405, 708, 88, 91, 89, 293, 295, 294, 148, 147, 149, 1417, 1420, 1419, 1430 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static int[] diff50_60 = new int[] { 62, 20, 97, 388, 93, 8, 25, 7, 27, 24, 493, 394, 268, 267, 1325, 1330, 1415 };
/*     */ 
/*     */   
/*  60 */   private static int[] diff60_70 = new int[] { 21, 80, 81, 87, 90, 3, 706, 290, 292, 291, 274, 279, 278, 275, 276, 277, 1328, 1327, 1329, 1326, 710 };
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static int[] diff70_80 = new int[] { 976, 973, 978, 975, 974, 280, 284, 281, 282, 283, 83, 86, 287, 286 };
/*     */ 
/*     */ 
/*     */   
/*  68 */   private static int[] anniversaryGifts = new int[] { 791, 738, 967, 1306, 1321, 1100, 1297, 972, 1032, 844, 700, 1334, 997 };
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static int[] justStatues = new int[] { 1408, 1407, 1416, 1406, 1421, 1418, 1323, 1324, 1405, 1417, 1420, 1419, 1325, 1330, 1415, 1328, 1327, 1329, 1326, 1430 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static HashMap<Integer, ArrayList<Integer>> fragmentLists = new HashMap<>();
/*     */   static final byte CLASS_WEAPON = 1;
/*     */   
/*     */   static {
/*  81 */     for (int fragment : diffTrash)
/*  82 */       addFragment(fragment, 0); 
/*  83 */     for (int fragment : diff0_15)
/*  84 */       addFragment(fragment, 1); 
/*  85 */     for (int fragment : diff15_30)
/*  86 */       addFragment(fragment, 2); 
/*  87 */     for (int fragment : diff30_40)
/*  88 */       addFragment(fragment, 3); 
/*  89 */     for (int fragment : diff40_50)
/*  90 */       addFragment(fragment, 4); 
/*  91 */     for (int fragment : diff50_60)
/*  92 */       addFragment(fragment, 5); 
/*  93 */     for (int fragment : diff60_70)
/*  94 */       addFragment(fragment, 6); 
/*  95 */     for (int fragment : diff70_80) {
/*  96 */       addFragment(fragment, 7);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final byte CLASS_ARMOUR = 2;
/*     */   
/*     */   static final byte CLASS_TOOL = 3;
/*     */   static final byte CLASS_CONTAINER = 4;
/*     */   static final byte CLASS_VEHICLE = 5;
/*     */   static final byte CLASS_ALL = 6;
/*     */   
/*     */   public static Fragment getRandomFragmentForSkill(double skill, boolean trashPossible) {
/* 109 */     if (skill < 0.0D) {
/* 110 */       return null;
/*     */     }
/* 112 */     int maxRange = 1;
/* 113 */     if (skill >= 15.0D && skill < 30.0D) {
/* 114 */       maxRange = 2;
/* 115 */     } else if (skill >= 30.0D) {
/* 116 */       maxRange = (int)Math.min(9.0D, Math.floor(skill / 10.0D) - 1.0D);
/*     */     } 
/* 118 */     int thisRange = Server.rand.nextInt(maxRange + 1);
/* 119 */     if (trashPossible && Server.rand.nextInt(3) != 0)
/* 120 */       thisRange = Math.max(0, thisRange - 3); 
/* 121 */     boolean bumpMaterial = false;
/*     */     
/* 123 */     if (thisRange == 8) {
/*     */       
/* 125 */       thisRange = 5;
/* 126 */       bumpMaterial = true;
/*     */     }
/* 128 */     else if (thisRange == 9) {
/*     */       
/* 130 */       thisRange = 6;
/* 131 */       bumpMaterial = true;
/*     */     } 
/*     */     
/* 134 */     int itemId = -1;
/* 135 */     byte materialId = -1;
/* 136 */     ArrayList<Integer> possibleItems = fragmentLists.get(Integer.valueOf(thisRange));
/* 137 */     if (possibleItems != null) {
/* 138 */       itemId = ((Integer)possibleItems.get(Server.rand.nextInt(possibleItems.size()))).intValue();
/*     */     }
/* 140 */     if (itemId == -1) {
/* 141 */       return null;
/*     */     }
/* 143 */     ItemTemplate item = ItemTemplateFactory.getInstance().getTemplateOrNull(itemId);
/* 144 */     if (item == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     materialId = item.getMaterial();
/* 148 */     if (item.isMetal() && !item.isOre && !item.isMetalLump()) {
/* 149 */       materialId = 93;
/*     */     }
/* 151 */     if (item.isMetal() && !MaterialUtilities.isMetal(materialId)) {
/* 152 */       materialId = 93;
/* 153 */     } else if (item.isWood() && !MaterialUtilities.isWood(materialId)) {
/* 154 */       materialId = 14;
/*     */     } 
/* 156 */     if (bumpMaterial && item.isMetal() && materialId == 93) {
/* 157 */       materialId = 94;
/*     */     }
/* 159 */     return new Fragment(itemId, materialId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Item createVillageCache(Player performer, Item archReport, DeadVillage vill, Skill archSkill) {
/* 165 */     if (!archReport.getAuxBit(0) || !archReport.getAuxBit(1) || !archReport.getAuxBit(2) || !archReport.getAuxBit(3)) {
/* 166 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 170 */       Item cache = ItemFactory.createItem(1422, archReport.getCurrentQualityLevel(), vill.getFounderName());
/* 171 */       cache.setName(vill.getDeedName());
/*     */       
/* 173 */       int statueCount = (int)Math.min(6.0D, (archSkill.getKnowledge(0.0D) + archReport.getCurrentQualityLevel()) / 28.0D);
/* 174 */       int goodCount = (int)Math.min(6.0D, (archSkill.getKnowledge(0.0D) + archReport.getCurrentQualityLevel()) / 28.0D);
/* 175 */       float dvModifier = Math.min(2.0F, 0.25F + vill.getTimeSinceDisband() / 120.0F + vill.getTotalAge() / 60.0F);
/* 176 */       int totalGiven = 0;
/*     */       int i;
/* 178 */       for (i = 0; i < statueCount * dvModifier; i++) {
/*     */         
/* 180 */         double power = archSkill.skillCheck((i * 5), archReport, 0.0D, false, 1.0F);
/* 181 */         Item statueFrag = ItemFactory.createItem(1307, (float)Math.min(100.0D, Math.max(1.0D, power)), vill.getFounderName());
/* 182 */         statueFrag.setRealTemplate(justStatues[Server.rand.nextInt(justStatues.length)]);
/* 183 */         statueFrag.setLastOwnerId(performer.getWurmId());
/* 184 */         if (statueFrag.isMetal())
/*     */         {
/* 186 */           if (Server.rand.nextInt(500) == 0) {
/* 187 */             statueFrag.setMaterial((byte)95);
/* 188 */           } else if (Server.rand.nextInt(50) == 0) {
/* 189 */             statueFrag.setMaterial((byte)94);
/*     */           } else {
/* 191 */             statueFrag.setMaterial((byte)93);
/*     */           } 
/*     */         }
/* 194 */         cache.insertItem(statueFrag, true);
/* 195 */         totalGiven++;
/*     */       } 
/* 197 */       if (archSkill.getKnowledge(0.0D) > 50.0D)
/*     */       {
/*     */         
/* 200 */         for (i = 0; i < goodCount * dvModifier; i++) {
/*     */           
/* 202 */           double power = archSkill.skillCheck((i * 10), archReport, 0.0D, false, 1.0F);
/* 203 */           Item randomFrag = ItemFactory.createItem(1307, (float)Math.min(100.0D, Math.max(1.0D, power)), vill.getFounderName());
/* 204 */           int[] list = diff50_60;
/* 205 */           if (power > 50.0D) {
/* 206 */             list = diff70_80;
/* 207 */           } else if (power > 30.0D) {
/* 208 */             list = diff60_70;
/* 209 */           }  randomFrag.setRealTemplate(list[Server.rand.nextInt(list.length)]);
/* 210 */           randomFrag.setLastOwnerId(performer.getWurmId());
/* 211 */           randomFrag.setMaterial(randomFrag.getRealTemplate().getMaterial());
/* 212 */           if (randomFrag.isMetal() && !(randomFrag.getTemplate()).isOre && !randomFrag.getTemplate().isMetalLump())
/*     */           {
/* 214 */             if (Server.rand.nextInt(500) == 0) {
/* 215 */               randomFrag.setMaterial((byte)95);
/* 216 */             } else if (Server.rand.nextInt(50) == 0) {
/* 217 */               randomFrag.setMaterial((byte)94);
/*     */             } else {
/* 219 */               randomFrag.setMaterial((byte)93);
/*     */             } 
/*     */           }
/* 222 */           cache.insertItem(randomFrag, true);
/* 223 */           totalGiven++;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 228 */       for (i = totalGiven; i < 10; i++) {
/*     */         
/* 230 */         double power = archSkill.skillCheck((i * 5), archReport, 0.0D, false, 1.0F);
/* 231 */         Item randomFrag = ItemFactory.createItem(1307, (float)Math.min(100.0D, Math.max(1.0D, power)), vill.getFounderName());
/* 232 */         int[] list = diff15_30;
/* 233 */         if (power > 50.0D) {
/* 234 */           list = diff40_50;
/* 235 */         } else if (power > 20.0D) {
/* 236 */           list = diff30_40;
/* 237 */         }  randomFrag.setRealTemplate(list[Server.rand.nextInt(list.length)]);
/* 238 */         randomFrag.setLastOwnerId(performer.getWurmId());
/* 239 */         randomFrag.setMaterial(randomFrag.getRealTemplate().getMaterial());
/* 240 */         if (randomFrag.isMetal() && !(randomFrag.getTemplate()).isOre && !randomFrag.getTemplate().isMetalLump())
/*     */         {
/* 242 */           if (Server.rand.nextInt(500) == 0) {
/* 243 */             randomFrag.setMaterial((byte)95);
/* 244 */           } else if (Server.rand.nextInt(50) == 0) {
/* 245 */             randomFrag.setMaterial((byte)94);
/*     */           } else {
/* 247 */             randomFrag.setMaterial((byte)93);
/*     */           } 
/*     */         }
/* 250 */         cache.insertItem(randomFrag, true);
/*     */       } 
/*     */       
/* 253 */       Item tokenMini = ItemFactory.createItem(1423, (float)((archSkill.getKnowledge(0.0D) + archReport.getCurrentQualityLevel()) / 2.0D), vill
/* 254 */           .getFounderName());
/* 255 */       double tokenPower = archSkill.skillCheck(50.0D, archReport, 0.0D, false, 1.0F);
/* 256 */       if (tokenPower > 80.0D) {
/* 257 */         tokenMini.setMaterial(getMetalMoonMaterial(100));
/* 258 */       } else if (tokenPower > 60.0D) {
/* 259 */         tokenMini.setMaterial(getMetalAlloyMaterial(100));
/* 260 */       } else if (tokenPower > 30.0D) {
/* 261 */         tokenMini.setMaterial(getMetalBaseMaterial((int)tokenPower));
/*     */       } 
/* 263 */       tokenMini.setName(vill.getDeedName());
/* 264 */       tokenMini.setData(vill.getDeedId());
/* 265 */       tokenMini.setAuxData((byte)((archReport.getAuxData() & 0xFF) >>> 4));
/* 266 */       tokenMini.setAuxBit(7, true);
/* 267 */       tokenMini.setLastOwnerId(performer.getWurmId());
/*     */       
/* 269 */       cache.insertItem(tokenMini, true);
/*     */       
/* 271 */       return cache;
/*     */     }
/* 273 */     catch (FailedException|NoSuchTemplateException failedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 278 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getDifficultyForItem(int itemId, int materialId) {
/* 283 */     for (int fragment : diff0_15) {
/* 284 */       if (fragment == itemId)
/* 285 */         return 5; 
/* 286 */     }  for (int fragment : diff15_30) {
/* 287 */       if (fragment == itemId)
/* 288 */         return 15; 
/* 289 */     }  for (int fragment : diff30_40) {
/* 290 */       if (fragment == itemId)
/* 291 */         return 25; 
/* 292 */     }  for (int fragment : diff40_50) {
/* 293 */       if (fragment == itemId)
/* 294 */         return 35; 
/* 295 */     }  for (int fragment : diff50_60) {
/* 296 */       if (fragment == itemId)
/* 297 */       { if (materialId == 94 || materialId == 9) {
/* 298 */           return 75;
/*     */         }
/* 300 */         return 45; } 
/* 301 */     }  for (int fragment : diff60_70) {
/* 302 */       if (fragment == itemId)
/* 303 */       { if (materialId == 94 || materialId == 9) {
/* 304 */           return 85;
/*     */         }
/* 306 */         return 55; } 
/* 307 */     }  for (int fragment : diff70_80) {
/* 308 */       if (fragment == itemId)
/* 309 */         return 65; 
/*     */     } 
/* 311 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getMetalBaseMaterial(int identifyLevel) {
/* 316 */     switch (Server.rand.nextInt(Math.max(6, 75 - identifyLevel))) {
/*     */       
/*     */       case 0:
/* 319 */         return 7;
/*     */       case 1:
/* 321 */         return 8;
/*     */       case 2:
/* 323 */         return 10;
/*     */       case 3:
/* 325 */         return 13;
/*     */       case 4:
/* 327 */         return 34;
/*     */       case 5:
/* 329 */         return 12;
/*     */     } 
/* 331 */     return 11;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getMetalAlloyMaterial(int identifyLevel) {
/* 337 */     switch (Server.rand.nextInt(Math.max(4, 75 - identifyLevel))) {
/*     */       
/*     */       case 0:
/* 340 */         return 30;
/*     */       case 1:
/* 342 */         return 31;
/*     */       case 2:
/* 344 */         return 96;
/*     */     } 
/* 346 */     return 9;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getMetalMoonMaterial(int identifyLevel) {
/* 352 */     switch (Server.rand.nextInt(Math.max(10, 90 - identifyLevel))) {
/*     */       
/*     */       case 0:
/* 355 */         return 67;
/*     */       case 1:
/*     */       case 2:
/* 358 */         return 56;
/*     */     } 
/* 360 */     return 57;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte getRandomWoodMaterial(int identifyLevel) {
/* 366 */     switch (Server.rand.nextInt(Math.max(25, 75 - identifyLevel))) {
/*     */       
/*     */       case 0:
/* 369 */         return 42;
/*     */       case 1:
/* 371 */         return 14;
/*     */       case 2:
/* 373 */         return 91;
/*     */       case 3:
/* 375 */         return 50;
/*     */       case 4:
/* 377 */         return 39;
/*     */       case 5:
/* 379 */         return 45;
/*     */       case 6:
/* 381 */         return 63;
/*     */       case 7:
/* 383 */         return 65;
/*     */       case 8:
/* 385 */         return 49;
/*     */       case 9:
/* 387 */         return 71;
/*     */       case 10:
/* 389 */         return 46;
/*     */       case 11:
/* 391 */         return 43;
/*     */       case 12:
/* 393 */         return 66;
/*     */       case 13:
/* 395 */         return 92;
/*     */       case 14:
/* 397 */         return 41;
/*     */       case 15:
/* 399 */         return 38;
/*     */       case 16:
/* 401 */         return 51;
/*     */       case 17:
/* 403 */         return 44;
/*     */       case 18:
/* 405 */         return 88;
/*     */       case 19:
/* 407 */         return 37;
/*     */       case 20:
/* 409 */         return 90;
/*     */       case 21:
/* 411 */         return 47;
/*     */       case 22:
/* 413 */         return 48;
/*     */       case 23:
/* 415 */         return 64;
/*     */       case 24:
/* 417 */         return 40;
/*     */     } 
/* 419 */     return 14;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRandomAnniversaryGift() {
/* 425 */     return anniversaryGifts[Server.rand.nextInt(anniversaryGifts.length)];
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
/*     */   public static int getRandomEnchantNumber(int weight) {
/* 437 */     if (weight < 50) {
/* 438 */       return 0;
/*     */     }
/* 440 */     int[] vals = new int[8];
/* 441 */     for (int i = 0; i < 8; i++) {
/* 442 */       vals[i] = Server.rand.nextInt(1000);
/*     */     }
/* 444 */     int closest = vals[0];
/* 445 */     int weightedVal = (weight - 50) * 20;
/* 446 */     for (int j = 0; j < 8; j++) {
/* 447 */       if (Math.abs(weightedVal - vals[j]) < Math.abs(weightedVal - closest))
/* 448 */         closest = vals[j]; 
/*     */     } 
/* 450 */     return Math.min(5, Math.max(1, Math.round(closest / 200.0F)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addRandomEnchantment(Item toEnchant, int enchLevel, float power) {
/* 455 */     byte itemClass = 6;
/* 456 */     if (toEnchant.isWeapon()) {
/* 457 */       itemClass = 1;
/* 458 */     } else if (toEnchant.isArmour()) {
/* 459 */       itemClass = 2;
/* 460 */     } else if (toEnchant.isTool()) {
/* 461 */       itemClass = 3;
/* 462 */     } else if (toEnchant.isHollow()) {
/* 463 */       itemClass = 4;
/* 464 */     } else if (toEnchant.isVehicle()) {
/* 465 */       itemClass = 5;
/*     */     } 
/* 467 */     FragmentEnchantment f = FragmentEnchantment.getRandomEnchantment(itemClass, enchLevel);
/* 468 */     if (f == null) {
/*     */       return;
/*     */     }
/* 471 */     byte enchantment = f.getEnchantment();
/* 472 */     if (enchantment <= -51) {
/*     */       
/* 474 */       if (!RuneUtilities.canApplyRuneTo(enchantment, toEnchant)) {
/*     */         return;
/*     */       }
/*     */     } else {
/*     */       
/* 479 */       if (EnchantUtil.hasNegatingEffect(toEnchant, enchantment) != null)
/*     */         return; 
/* 481 */       if (!Spell.mayBeEnchanted(toEnchant)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 485 */     ItemSpellEffects effs = toEnchant.getSpellEffects();
/* 486 */     if (effs == null)
/* 487 */       effs = new ItemSpellEffects(toEnchant.getWurmId()); 
/* 488 */     SpellEffect e = effs.getSpellEffect(enchantment);
/* 489 */     if (e == null) {
/*     */       
/* 491 */       e = new SpellEffect(toEnchant.getWurmId(), enchantment, power, 20000000);
/* 492 */       effs.addSpellEffect(e);
/*     */     }
/*     */     else {
/*     */       
/* 496 */       if (power > e.getPower() + power / 5.0F) {
/* 497 */         e.setPower(power);
/*     */       } else {
/* 499 */         e.setPower(e.getPower() + power / 5.0F);
/*     */       } 
/* 501 */       if (enchantment != 45 && e.getPower() > 104.0F) {
/* 502 */         e.setPower(104.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addFragment(int itemId, int range) {
/* 508 */     ArrayList<Integer> fragments = fragmentLists.get(Integer.valueOf(range));
/* 509 */     if (fragments == null) {
/*     */       
/* 511 */       fragments = new ArrayList<>();
/* 512 */       fragmentLists.put(Integer.valueOf(range), fragments);
/*     */     } 
/*     */     
/* 515 */     fragments.add(Integer.valueOf(itemId));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Fragment
/*     */   {
/*     */     private int itemId;
/*     */     private int itemMaterial;
/*     */     
/*     */     Fragment(int itemId, int itemMaterial) {
/* 525 */       this.itemId = itemId;
/* 526 */       this.itemMaterial = itemMaterial;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getItemId() {
/* 531 */       return this.itemId;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaterial() {
/* 536 */       return this.itemMaterial;
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
/*     */   public enum FragmentEnchantment
/*     */   {
/* 550 */     FLAMEAURA((byte)14, new float[] { 0.1F, 0.2F, 0.4F, 0.4F, 0.2F }, new byte[] { 1
/*     */       }),
/* 552 */     FROSTBRAND((byte)33, new float[] { 0.1F, 0.2F, 0.5F, 0.3F, 0.2F }, new byte[] { 1
/*     */       }),
/* 554 */     BLOODTHIRST((byte)45, new float[] { 0.2F, 0.4F, 0.2F, 0.1F, 0.0F }, new byte[] { 1
/*     */       }),
/* 556 */     ROTTINGTOUCH((byte)18, new float[] { 0.0F, 0.0F, 0.2F, 0.4F, 0.4F }, new byte[] { 1
/*     */       }),
/* 558 */     NIMBLENESS((byte)32, new float[] { 0.0F, 0.0F, 0.0F, 0.1F, 0.3F }, new byte[] { 1
/*     */       }),
/* 560 */     LIFETRANSFER((byte)26, new float[] { 0.0F, 0.0F, 0.0F, 0.1F, 0.3F }, new byte[] { 1
/*     */       }),
/* 562 */     MINDSTEALER((byte)31, new float[] { 0.0F, 0.0F, 0.05F, 0.2F, 0.4F }, new byte[] { 1
/*     */       }),
/* 564 */     AURASHAREDPAIN((byte)17, new float[] { 0.0F, 0.1F, 0.3F, 0.2F, 0.1F }, new byte[] { 2
/*     */       }),
/* 566 */     WEBARMOUR((byte)46, new float[] { 0.0F, 0.0F, 0.2F, 0.3F, 0.2F }, new byte[] { 2
/*     */       }),
/* 568 */     WINDOFAGES((byte)16, new float[] { 0.1F, 0.2F, 0.4F, 0.2F, 0.1F }, new byte[] { 1, 3
/*     */       }),
/* 570 */     CIRCLEOFCUNNING((byte)13, new float[] { 0.05F, 0.15F, 0.3F, 0.4F, 0.2F }, new byte[] { 1, 3
/*     */       }),
/* 572 */     BOTD((byte)47, new float[] { 0.0F, 0.05F, 0.2F, 0.4F, 0.2F }, new byte[] { 1, 3
/*     */       
/*     */       }),
/* 575 */     MAGBRASS(-128, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */       }),
/* 577 */     MAGBRONZE((byte)-127, new float[] { 0.1F, 0.125F, 0.05F, 0.0F, 0.0F }, new byte[] { 3
/*     */ 
/*     */       
/*     */       }),
/* 581 */     MAGADAMANTINE((byte)-125, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 5
/*     */       }),
/* 583 */     MAGGLIMMERSTEEL((byte)-124, new float[] { 0.1F, 0.05F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 585 */     MAGGOLD((byte)-123, new float[] { 0.05F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 6
/*     */       }),
/* 587 */     MAGSILVER((byte)-122, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 4
/*     */       }),
/* 589 */     MAGSTEEL((byte)-121, new float[] { 0.05F, 0.025F, 0.0F, 0.0F, 0.0F }, new byte[] { 5
/*     */       }),
/* 591 */     MAGCOPPER((byte)-120, new float[] { 0.1F, 0.15F, 0.05F, 0.0F, 0.0F }, new byte[] { 6
/*     */ 
/*     */       
/*     */       }),
/* 595 */     MAGLEAD((byte)-118, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */       }),
/* 597 */     MAGZINC((byte)-117, new float[] { 0.05F, 0.025F, 0.0F, 0.0F, 0.0F }, new byte[] { 5
/*     */       }),
/* 599 */     MAGSERYLL((byte)-116, new float[] { 0.025F, 0.05F, 0.1F, 0.125F, 0.075F }, new byte[] { 6
/*     */       
/*     */       }),
/* 602 */     FOBRASS((byte)-115, new float[] { 0.05F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 604 */     FOBRONZE((byte)-114, new float[] { 0.05F, 0.1F, 0.125F, 0.05F, 0.025F }, new byte[] { 5
/*     */       }),
/* 606 */     FOTIN((byte)-113, new float[] { 0.1F, 0.125F, 0.05F, 0.0F, 0.0F }, new byte[] { 6
/*     */       }),
/* 608 */     FOADAMANTINE((byte)-112, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */       }),
/* 610 */     FOGLIMMERSTEEL((byte)-111, new float[] { 0.025F, 0.0F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 612 */     FOGOLD((byte)-110, new float[] { 0.05F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 614 */     FOSILVER((byte)-109, new float[] { 0.025F, 0.0F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 616 */     FOSTEEL((byte)-108, new float[] { 0.1F, 0.125F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 622 */     FOLEAD((byte)-105, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */ 
/*     */       
/*     */       }),
/* 626 */     FOSERYLL((byte)-103, new float[] { 0.025F, 0.05F, 0.1F, 0.125F, 0.075F }, new byte[] { 6
/*     */       
/*     */       }),
/* 629 */     VYNBRASS((byte)-102, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */       }),
/* 631 */     VYNBRONZE((byte)-101, new float[] { 0.05F, 0.1F, 0.125F, 0.05F, 0.025F }, new byte[] { 5
/*     */       }),
/* 633 */     VYNTIN((byte)-100, new float[] { 0.025F, 0.05F, 0.1F, 0.1F, 0.05F }, new byte[] { 6
/*     */       }),
/* 635 */     VYNADAMANTINE((byte)-99, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 5
/*     */       }),
/* 637 */     VYNGLIMMERSTEEL((byte)-98, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.0F }, new byte[] { 3
/*     */ 
/*     */       
/*     */       }),
/* 641 */     VYNSILVER((byte)-96, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.025F }, new byte[] { 4
/*     */       }),
/* 643 */     VYNSTEEL((byte)-95, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.0F }, new byte[] { 6
/*     */       }),
/* 645 */     VYNCOPPER((byte)-94, new float[] { 0.05F, 0.1F, 0.125F, 0.05F, 0.0F }, new byte[] { 6
/*     */ 
/*     */       
/*     */       }),
/* 649 */     VYNLEAD((byte)-92, new float[] { 0.1F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 4
/*     */ 
/*     */       
/*     */       }),
/* 653 */     VYNSERYLL((byte)-90, new float[] { 0.025F, 0.05F, 0.1F, 0.125F, 0.075F }, new byte[] { 6
/*     */       
/*     */       }),
/* 656 */     LIBBRASS((byte)-89, new float[] { 0.05F, 0.025F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 658 */     LIBBRONZE((byte)-88, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 660 */     LIBTIN((byte)-87, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.025F }, new byte[] { 6
/*     */       }),
/* 662 */     LIBADAMANTINE((byte)-86, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.0F }, new byte[] { 6
/*     */       }),
/* 664 */     LIBGLIMMERSTEEL((byte)-85, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.0F }, new byte[] { 3
/*     */       }),
/* 666 */     LIBGOLD((byte)-84, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 668 */     LIBSILVER((byte)-83, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.025F }, new byte[] { 4
/*     */       }),
/* 670 */     LIBSTEEL((byte)-82, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 5
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 676 */     LIBLEAD((byte)-79, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 678 */     LIBZINC((byte)-78, new float[] { 0.05F, 0.1F, 0.05F, 0.025F, 0.0F }, new byte[] { 6
/*     */       }),
/* 680 */     LIBSERYLL((byte)-77, new float[] { 0.025F, 0.05F, 0.1F, 0.125F, 0.075F }, new byte[] { 6
/*     */       
/*     */       }),
/* 683 */     JACKALBRASS((byte)-76, new float[] { 0.025F, 0.01F, 0.0F, 0.0F, 0.0F }, new byte[] { 6
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }),
/* 691 */     JACKALGLIMMERSTEEL((byte)-72, new float[] { 0.025F, 0.01F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 693 */     JACKALGOLD((byte)-71, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 6
/*     */       }),
/* 695 */     JACKALSILVER((byte)-70, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 4
/*     */       }),
/* 697 */     JACKALSTEEL((byte)-69, new float[] { 0.025F, 0.05F, 0.1F, 0.05F, 0.01F }, new byte[] { 6
/*     */       }),
/* 699 */     JACKALCOPPER((byte)-68, new float[] { 0.05F, 0.1F, 0.125F, 0.05F, 0.0F }, new byte[] { 4
/*     */       }),
/* 701 */     JACKALIRON((byte)-67, new float[] { 0.025F, 0.01F, 0.0F, 0.0F, 0.0F }, new byte[] { 3
/*     */       }),
/* 703 */     JACKALLEAD((byte)-66, new float[] { 0.025F, 0.0F, 0.0F, 0.0F, 0.0F }, new byte[] { 6
/*     */       }),
/* 705 */     JACKALZINC((byte)-65, new float[] { 0.025F, 0.05F, 0.025F, 0.0F, 0.0F }, new byte[] { 5
/*     */       }),
/* 707 */     JACKALSERYLL((byte)-64, new float[] { 0.025F, 0.05F, 0.1F, 0.125F, 0.075F }, new byte[] { 6
/*     */       
/*     */       }),
/* 710 */     UNKBRASS((byte)-63, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 712 */     UNKBRONZE((byte)-62, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 714 */     UNKTIN((byte)-61, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 716 */     UNKADAMANTINE((byte)-60, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 718 */     UNKGLIMMERSTEEL((byte)-59, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 3
/*     */       }),
/* 720 */     UNKGOLD((byte)-58, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 722 */     UNKSILVER((byte)-57, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 4
/*     */       }),
/* 724 */     UNKSTEEL((byte)-56, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 726 */     UNKCOPPER((byte)-55, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 728 */     UNKIRON((byte)-54, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 730 */     UNKLEAD((byte)-53, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 3
/*     */       }),
/* 732 */     UNKZINC((byte)-52, new float[] { 0.0F, 0.01F, 0.025F, 0.05F, 0.1F }, new byte[] { 6
/*     */       }),
/* 734 */     UNKSERYLL((byte)-51, new float[] { 0.0F, 0.0F, 0.025F, 0.075F, 0.15F }, new byte[] { 6 });
/*     */     
/*     */     private final byte enchantment;
/*     */     
/*     */     private final byte[] itemClass;
/*     */     
/*     */     private final float[] levelChances;
/*     */     
/*     */     FragmentEnchantment(byte enchantment, float[] levelChances, byte... itemClass) {
/* 743 */       this.enchantment = enchantment;
/* 744 */       this.itemClass = itemClass;
/* 745 */       this.levelChances = levelChances;
/*     */     }
/*     */ 
/*     */     
/*     */     byte getEnchantment() {
/* 750 */       return this.enchantment;
/*     */     }
/*     */ 
/*     */     
/*     */     static FragmentEnchantment getRandomEnchantment(byte itemClass, int level) {
/* 755 */       float totalChance = 0.0F;
/* 756 */       for (FragmentEnchantment f : values()) {
/*     */         
/* 758 */         for (byte b : f.itemClass) {
/* 759 */           if (b == itemClass || b == 6)
/* 760 */             totalChance += f.levelChances[level]; 
/*     */         } 
/*     */       } 
/* 763 */       float winningVal = Server.rand.nextFloat() * totalChance;
/* 764 */       float thisVal = 0.0F;
/* 765 */       for (FragmentEnchantment f : values()) {
/*     */         
/* 767 */         for (byte b : f.itemClass) {
/* 768 */           if (b == itemClass || b == 6) {
/*     */             
/* 770 */             thisVal += f.levelChances[level];
/* 771 */             if (winningVal < thisVal)
/* 772 */               return f; 
/*     */           } 
/*     */         } 
/*     */       } 
/* 776 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\FragmentUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */