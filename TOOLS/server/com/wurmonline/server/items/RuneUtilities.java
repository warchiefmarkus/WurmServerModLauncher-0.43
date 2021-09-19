/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import com.wurmonline.shared.constants.Enchants;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
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
/*     */ public abstract class RuneUtilities
/*     */   implements MiscConstants, Enchants, ItemMaterials
/*     */ {
/*     */   static {
/*  25 */     byte[] metalOffset = { 30, 31, 34, 56, 57, 7, 8, 9, 10, 11, 12, 13, 67 };
/*     */   }
/*     */   
/*  28 */   private static final ArrayList<Byte> metalList = new ArrayList<>(); static {
/*  29 */     for (byte b : metalOffset)
/*  30 */       metalList.add(Byte.valueOf(b)); 
/*     */   }
/*  32 */   private static final HashMap<Byte, RuneData> runeDataMap = new HashMap<>(); static {
/*  33 */     createRuneDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isSingleUseRune(Item rune) {
/*  38 */     if (!runeDataMap.containsKey(Byte.valueOf(getEnchantForRune(rune)))) {
/*  39 */       return false;
/*     */     }
/*  41 */     return ((RuneData)runeDataMap.get(Byte.valueOf(getEnchantForRune(rune)))).isSingleUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isEnchantRune(Item rune) {
/*  46 */     if (!runeDataMap.containsKey(Byte.valueOf(getEnchantForRune(rune)))) {
/*  47 */       return false;
/*     */     }
/*  49 */     return ((RuneData)runeDataMap.get(Byte.valueOf(getEnchantForRune(rune)))).isEnchantment();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getAttachmentTargets(Item rune) {
/*  54 */     int riftTemplate = rune.getRealTemplateId();
/*  55 */     RuneData runeData = runeDataMap.get(Byte.valueOf(getEnchantForRune(rune)));
/*     */     
/*  57 */     if (runeData.isAnyTarget()) {
/*  58 */       return "any item";
/*     */     }
/*  60 */     switch (riftTemplate) {
/*     */       
/*     */       case 1104:
/*  63 */         return "wooden items";
/*     */       case 1103:
/*  65 */         return "metal items";
/*     */       case 1102:
/*  67 */         return "stone, leather, cloth and pottery items";
/*     */     } 
/*     */     
/*  70 */     return "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isCorrectTarget(Item rune, Item target) {
/*  76 */     int riftTemplate = rune.getRealTemplateId();
/*  77 */     RuneData runeData = runeDataMap.get(Byte.valueOf(getEnchantForRune(rune)));
/*     */     
/*  79 */     if (runeData.getModifierPercentage(ModifierEffect.ENCH_GLOW) > 0.0F && (target
/*  80 */       .getTemplate().isLight() || target.getTemplate().isCooker() || target
/*  81 */       .isAlwaysPoll())) {
/*  82 */       return false;
/*     */     }
/*  84 */     switch (riftTemplate) {
/*     */       
/*     */       case 1104:
/*  87 */         if (target.isWood() || runeData.isAnyTarget())
/*     */         {
/*     */           
/*  90 */           return true;
/*     */         }
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
/* 124 */         return false;case 1103: if (target.isMetal() || runeData.isAnyTarget()) return true;  return false;case 1102: if (target.isStone() || runeData.isAnyTarget()) return true;  if (target.isLeather() || target.isCloth()) return true;  if (target.isPottery()) return true;  return false;
/*     */     } 
/*     */     if (Servers.isThisATestServer())
/*     */       return true; 
/*     */     return false; } public static final int getNumberOfRuneEffects(Item target) {
/* 129 */     ItemSpellEffects effs = target.getSpellEffects();
/* 130 */     if (effs != null) {
/* 131 */       return effs.getNumberOfRuneEffects();
/*     */     }
/* 133 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canApplyRuneTo(Item rune, Item target) {
/* 138 */     if (canApplyRuneTo(getEnchantForRune(rune), target)) {
/* 139 */       return isCorrectTarget(rune, target);
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canApplyRuneTo(byte runeEnchant, Item target) {
/* 146 */     if (target.isNotRuneable()) {
/* 147 */       return false;
/*     */     }
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
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte getEnchantForRune(Item rune) {
/* 173 */     int metalOffset = metalList.indexOf(Byte.valueOf(rune.getMaterial()));
/*     */     
/* 175 */     if (rune.getMaterial() == 96) {
/*     */       
/* 177 */       switch (rune.getTemplateId()) {
/*     */         
/*     */         case 1290:
/* 180 */           return -50;
/*     */       } 
/*     */ 
/*     */       
/* 184 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 188 */     switch (rune.getTemplateId())
/*     */     
/*     */     { case 1289:
/* 191 */         startId = Byte.MIN_VALUE;
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
/* 210 */         return (byte)(startId + metalOffset);case 1290: startId = -115; return (byte)(startId + metalOffset);case 1291: startId = -102; return (byte)(startId + metalOffset);case 1292: startId = -89; return (byte)(startId + metalOffset); }  byte startId = -76; return (byte)(startId + metalOffset);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Spell getSpellForRune(Item source) {
/* 215 */     if (runeDataMap.get(Byte.valueOf(getEnchantForRune(source))) != null) {
/* 216 */       return ((RuneData)runeDataMap.get(Byte.valueOf(getEnchantForRune(source)))).getSingleUseSpell();
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte getMetalForEnchant(byte enchantType) {
/* 224 */     enchantType = (byte)(enchantType + 128);
/*     */ 
/*     */     
/* 227 */     while (enchantType > 12) {
/* 228 */       enchantType = (byte)(enchantType - 13);
/*     */     }
/* 230 */     return ((Byte)metalList.get(enchantType)).byteValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getRuneName(byte type) {
/* 236 */     switch (type) {
/*     */       
/*     */       case -50:
/* 239 */         return MaterialUtilities.getMaterialString((byte)96) + " rune of " + Deities.getDeityName(1);
/*     */     } 
/*     */     
/* 242 */     String toReturn = MaterialUtilities.getMaterialString(getMetalForEnchant(type));
/* 243 */     toReturn = toReturn + " rune of ";
/*     */     
/* 245 */     if (type < -115) {
/* 246 */       toReturn = toReturn + Deities.getDeityName(2);
/* 247 */     } else if (type < -102) {
/* 248 */       toReturn = toReturn + Deities.getDeityName(1);
/* 249 */     } else if (type < -89) {
/* 250 */       toReturn = toReturn + Deities.getDeityName(3);
/* 251 */     } else if (type < -76) {
/* 252 */       toReturn = toReturn + Deities.getDeityName(4);
/* 253 */     } else if (type < -63) {
/* 254 */       toReturn = toReturn + Deities.getDeityName(9);
/* 255 */     } else if (type < -50) {
/* 256 */       toReturn = toReturn + Deities.getDeityName(11);
/*     */     } 
/* 258 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getRuneLongDesc(byte type) {
/* 263 */     if (!runeDataMap.containsKey(Byte.valueOf(type))) {
/* 264 */       return "";
/*     */     }
/* 266 */     return ((RuneData)runeDataMap.get(Byte.valueOf(type))).getDescription();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final float getModifier(byte type, ModifierEffect e) {
/* 271 */     if (!runeDataMap.containsKey(Byte.valueOf(type))) {
/* 272 */       return 0.0F;
/*     */     }
/* 274 */     return ((RuneData)runeDataMap.get(Byte.valueOf(type))).getModifierPercentage(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createRuneDefinitions() {
/* 282 */     RuneData tempRune = new RuneData(-128, true, "increase quality at a faster rate when being improved (10%)");
/* 283 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.1F);
/* 284 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 286 */     tempRune = new RuneData((byte)-127, true, "gather resources at a higher quality level (5%) and increase the time an enchant holds its power on the item (5%)");
/*     */     
/* 288 */     tempRune.addModifier(ModifierEffect.ENCH_RESGATHERED, 0.05F);
/* 289 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHRETENTION, 0.05F);
/* 290 */     tempRune.setAnyTarget(true);
/* 291 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 293 */     tempRune = new RuneData((byte)-126, false, "activate the mole senses effect one time");
/* 294 */     tempRune.setSingleUseSpell(Spells.getSpell(439));
/* 295 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 297 */     tempRune = new RuneData((byte)-125, true, "increase vehicle speed (10%)");
/* 298 */     tempRune.addModifier(ModifierEffect.ENCH_VEHCSPEED, 0.1F);
/* 299 */     tempRune.setAnyTarget(true);
/* 300 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 302 */     tempRune = new RuneData((byte)-124, true, "increase usage speed (5%) and increase skill level on skill checks (5%)");
/* 303 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.05F);
/* 304 */     tempRune.addModifier(ModifierEffect.ENCH_SKILLCHECKBONUS, 0.05F);
/* 305 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 307 */     tempRune = new RuneData((byte)-123, true, "reduce size (5%) and reduce weight (5%)");
/* 308 */     tempRune.addModifier(ModifierEffect.ENCH_SIZE, -0.05F);
/* 309 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.05F);
/* 310 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 312 */     tempRune = new RuneData((byte)-122, true, "increase volume (10%)");
/* 313 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, 0.1F);
/* 314 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 316 */     tempRune = new RuneData((byte)-121, true, "reduce decay taken (5%) and increase the effect on speed by wind (5%)");
/* 317 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.05F);
/* 318 */     tempRune.addModifier(ModifierEffect.ENCH_WIND, 0.05F);
/* 319 */     tempRune.setAnyTarget(true);
/* 320 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 322 */     tempRune = new RuneData((byte)-120, true, "have a higher chance to be successfully improved (5%) and increase the chance of increasing rarity when improved (5%)");
/*     */     
/* 324 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.05F);
/* 325 */     tempRune.addModifier(ModifierEffect.ENCH_RARITYIMP, 0.05F);
/* 326 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 328 */     tempRune = new RuneData((byte)-119, false, "activate the sunder effect one time");
/* 329 */     tempRune.setSingleUseSpell(Spells.getSpell(253));
/* 330 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 332 */     tempRune = new RuneData((byte)-118, true, "reduce the quality change when repairing damage (10%)");
/* 333 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.1F);
/* 334 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 336 */     tempRune = new RuneData((byte)-117, true, "increase size (5%) and increase vehicle speed (5%)");
/* 337 */     tempRune.addModifier(ModifierEffect.ENCH_SIZE, 0.05F);
/* 338 */     tempRune.addModifier(ModifierEffect.ENCH_VEHCSPEED, 0.05F);
/* 339 */     tempRune.setAnyTarget(true);
/* 340 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 342 */     tempRune = new RuneData((byte)-116, true, "increase chance to resist shattering when being enchanted (10%)");
/* 343 */     tempRune.addModifier(ModifierEffect.ENCH_SHATTERRES, 0.1F);
/* 344 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     tempRune = new RuneData((byte)-50, false, "decrease the age of a single creature");
/* 350 */     tempRune.addModifier(ModifierEffect.SINGLE_CHANGE_AGE, 1.0F);
/* 351 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 353 */     tempRune = new RuneData((byte)-115, true, "increase quality at a faster rate when being improved (5%) and gather resources at a higher quality level (5%)");
/*     */     
/* 355 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.05F);
/* 356 */     tempRune.addModifier(ModifierEffect.ENCH_RESGATHERED, 0.05F);
/* 357 */     tempRune.setAnyTarget(true);
/* 358 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 360 */     tempRune = new RuneData((byte)-114, true, "increase the effect on speed by wind (10%)");
/* 361 */     tempRune.addModifier(ModifierEffect.ENCH_WIND, 0.1F);
/* 362 */     tempRune.setAnyTarget(true);
/* 363 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 365 */     tempRune = new RuneData((byte)-113, true, "increase the chance of increasing rarity when improved (5%) and reduce damage taken (5%)");
/*     */     
/* 367 */     tempRune.addModifier(ModifierEffect.ENCH_RARITYIMP, 0.05F);
/* 368 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.05F);
/* 369 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 371 */     tempRune = new RuneData((byte)-112, true, "increase the time an enchant holds its power on the item (5%) and increase the chance of successfully enchanting the item (5%)");
/*     */     
/* 373 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHRETENTION, 0.05F);
/* 374 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.05F);
/* 375 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 377 */     tempRune = new RuneData((byte)-111, true, "increase skill level on skill checks (10%)");
/* 378 */     tempRune.addModifier(ModifierEffect.ENCH_SKILLCHECKBONUS, 0.1F);
/* 379 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 381 */     tempRune = new RuneData((byte)-110, true, "have a chance to increase the effect of tending a field or harvesting a tree or bush (10%)");
/* 382 */     tempRune.addModifier(ModifierEffect.ENCH_FARMYIELD, 0.1F);
/* 383 */     tempRune.setAnyTarget(true);
/* 384 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 386 */     tempRune = new RuneData((byte)-109, true, "have an okay glow and increase skill level on skill checks (5%)");
/*     */     
/* 388 */     tempRune.addModifier(ModifierEffect.ENCH_GLOW, 0.25F);
/* 389 */     tempRune.addModifier(ModifierEffect.ENCH_SKILLCHECKBONUS, 0.05F);
/* 390 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 392 */     tempRune = new RuneData((byte)-108, true, "reduce decay taken (5%) and reduce damage taken (5%)");
/* 393 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.05F);
/* 394 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.05F);
/* 395 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 397 */     tempRune = new RuneData((byte)-107, false, "activate the charm animal effect one time");
/* 398 */     tempRune.setSingleUseSpell(Spells.getSpell(275));
/* 399 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 401 */     tempRune = new RuneData((byte)-106, true, "reduce fuel usage rate (10%)");
/* 402 */     tempRune.addModifier(ModifierEffect.ENCH_FUELUSE, 0.1F);
/* 403 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 405 */     tempRune = new RuneData((byte)-105, true, "reduce the quality change when repairing damage (5%) and increase usage speed (5%)");
/*     */     
/* 407 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.05F);
/* 408 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.05F);
/* 409 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 411 */     tempRune = new RuneData((byte)-104, false, "activate the morning fog effect one time");
/* 412 */     tempRune.setSingleUseSpell(Spells.getSpell(282));
/* 413 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 415 */     tempRune = new RuneData((byte)-103, true, "increase the chance of successfully enchanting the item (10%)");
/* 416 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.1F);
/* 417 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 422 */     tempRune = new RuneData((byte)-102, true, "increase quality at a faster rate when being improved (7.5%) and have a slight glow");
/*     */     
/* 424 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.075F);
/* 425 */     tempRune.addModifier(ModifierEffect.ENCH_GLOW, 0.1F);
/* 426 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 428 */     tempRune = new RuneData((byte)-101, true, "increase the effect on speed by wind (7.5%) and increase vehicle speed (5%)");
/*     */     
/* 430 */     tempRune.addModifier(ModifierEffect.ENCH_WIND, 0.075F);
/* 431 */     tempRune.addModifier(ModifierEffect.ENCH_VEHCSPEED, 0.05F);
/* 432 */     tempRune.setAnyTarget(true);
/* 433 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 435 */     tempRune = new RuneData((byte)-100, true, "increase the chance of increasing rarity when improved (10%)");
/* 436 */     tempRune.addModifier(ModifierEffect.ENCH_RARITYIMP, 0.1F);
/* 437 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 439 */     tempRune = new RuneData((byte)-99, true, "increase vehicle speed (7.5%) and reduce decay taken (5%)");
/*     */     
/* 441 */     tempRune.addModifier(ModifierEffect.ENCH_VEHCSPEED, 0.075F);
/* 442 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.05F);
/* 443 */     tempRune.setAnyTarget(true);
/* 444 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 446 */     tempRune = new RuneData((byte)-98, true, "increase usage speed (5%) and increase quality at a faster rate when being improved (5%)");
/*     */     
/* 448 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.05F);
/* 449 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.05F);
/* 450 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 452 */     tempRune = new RuneData((byte)-97, false, "activate the reveal creatures effect one time");
/* 453 */     tempRune.setSingleUseSpell(Spells.getSpell(444));
/* 454 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 456 */     tempRune = new RuneData((byte)-96, true, "reduce the decay taken of items inside (10%)");
/* 457 */     tempRune.addModifier(ModifierEffect.ENCH_INTERNAL_DECAY, 0.1F);
/* 458 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 460 */     tempRune = new RuneData((byte)-95, true, "reduce damage taken (10%)");
/* 461 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.1F);
/* 462 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 464 */     tempRune = new RuneData((byte)-94, true, "have a higher chance to be successfully improved (7.5%)");
/* 465 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.075F);
/* 466 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 468 */     tempRune = new RuneData((byte)-93, true, "reduce fuel usage rate (5%) and reduce decay taken (5%)");
/*     */     
/* 470 */     tempRune.addModifier(ModifierEffect.ENCH_FUELUSE, 0.05F);
/* 471 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.05F);
/* 472 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 474 */     tempRune = new RuneData((byte)-92, true, "reduce the quality change when repairing damage (5%) and increase volume (5%)");
/*     */     
/* 476 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.05F);
/* 477 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, 0.05F);
/* 478 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 480 */     tempRune = new RuneData((byte)-91, false, "activate the mend effect one time");
/* 481 */     tempRune.setSingleUseSpell(Spells.getSpell(251));
/* 482 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 484 */     tempRune = new RuneData((byte)-90, true, "increase chance to resist shattering when being enchanted (5%) and increase the chance of successfully enchanting the item (5%)");
/*     */     
/* 486 */     tempRune.addModifier(ModifierEffect.ENCH_SHATTERRES, 0.05F);
/* 487 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.05F);
/* 488 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     tempRune = new RuneData((byte)-89, true, "have an okay glow and gather resources at a higher quality level (5%)");
/* 494 */     tempRune.addModifier(ModifierEffect.ENCH_GLOW, 0.25F);
/* 495 */     tempRune.addModifier(ModifierEffect.ENCH_RESGATHERED, 0.05F);
/* 496 */     tempRune.setAnyTarget(true);
/* 497 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 499 */     tempRune = new RuneData((byte)-88, true, "gather resources at a higher quality level (10%)");
/* 500 */     tempRune.addModifier(ModifierEffect.ENCH_RESGATHERED, 0.1F);
/* 501 */     tempRune.setAnyTarget(true);
/* 502 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 504 */     tempRune = new RuneData((byte)-87, true, "increase the chance of increasing rarity when improved (5%) and have a higher chance to be successfully improved (5%)");
/*     */     
/* 506 */     tempRune.addModifier(ModifierEffect.ENCH_RARITYIMP, 0.05F);
/* 507 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.05F);
/* 508 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 510 */     tempRune = new RuneData((byte)-86, true, "increase the time an enchant holds its power on the item (10%)");
/* 511 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHRETENTION, 0.1F);
/* 512 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 514 */     tempRune = new RuneData((byte)-85, true, "increase usage speed (10%)");
/* 515 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.1F);
/* 516 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 518 */     tempRune = new RuneData((byte)-84, true, "have a chance to increase the effect of tending a field or harvesting a tree or bush (5%) and reduce weight (5%)");
/*     */     
/* 520 */     tempRune.addModifier(ModifierEffect.ENCH_FARMYIELD, 0.05F);
/* 521 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.05F);
/* 522 */     tempRune.setAnyTarget(true);
/* 523 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 525 */     tempRune = new RuneData((byte)-83, true, "increase volume (5%) and reduce the decay taken of items inside (5%)");
/*     */     
/* 527 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, 0.05F);
/* 528 */     tempRune.addModifier(ModifierEffect.ENCH_INTERNAL_DECAY, 0.05F);
/* 529 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 531 */     tempRune = new RuneData((byte)-82, true, "reduce damage taken (5%) and increase the effect on speed by wind (5%)");
/*     */     
/* 533 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.05F);
/* 534 */     tempRune.addModifier(ModifierEffect.ENCH_WIND, 0.05F);
/* 535 */     tempRune.setAnyTarget(true);
/* 536 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 538 */     tempRune = new RuneData((byte)-81, false, "activate the locate soul effect one time");
/* 539 */     tempRune.setSingleUseSpell(Spells.getSpell(419));
/* 540 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 542 */     tempRune = new RuneData((byte)-80, false, "activate the light token effect one time");
/* 543 */     tempRune.setSingleUseSpell(Spells.getSpell(421));
/* 544 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 546 */     tempRune = new RuneData((byte)-79, true, "reduce volume (5%) and increase usage speed (5%)");
/* 547 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, -0.05F);
/* 548 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.05F);
/* 549 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 551 */     tempRune = new RuneData((byte)-78, true, "reduce weight (10%)");
/* 552 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.1F);
/* 553 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 555 */     tempRune = new RuneData((byte)-77, true, "increase chance to resist shattering when being enchanted (5%) and reduce the quality change when repairing damage (5%)");
/*     */     
/* 557 */     tempRune.addModifier(ModifierEffect.ENCH_SHATTERRES, 0.05F);
/* 558 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.05F);
/* 559 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 564 */     tempRune = new RuneData((byte)-76, true, "have a decent glow");
/* 565 */     tempRune.addModifier(ModifierEffect.ENCH_GLOW, 0.4F);
/* 566 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */     
/* 569 */     tempRune = new RuneData((byte)-75, false, "activate the goat shape effect one time");
/* 570 */     tempRune.setSingleUseSpell(Spells.getSpell(422));
/* 571 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 573 */     tempRune = new RuneData((byte)-74, false, "activate the refresh effect one time");
/* 574 */     tempRune.setSingleUseSpell(Spells.getSpell(250));
/*     */     
/* 576 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 578 */     tempRune = new RuneData((byte)-73, false, "give an item a random color one time");
/* 579 */     tempRune.addModifier(ModifierEffect.SINGLE_COLOR, 1.0F);
/* 580 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 582 */     tempRune = new RuneData((byte)-72, true, "increase skill level bonus on skill checks (5%) and increase quality at a faster rate when being improved (5%)");
/*     */     
/* 584 */     tempRune.addModifier(ModifierEffect.ENCH_SKILLCHECKBONUS, 0.05F);
/* 585 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.05F);
/* 586 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 588 */     tempRune = new RuneData((byte)-71, true, "reduce size (10%)");
/* 589 */     tempRune.addModifier(ModifierEffect.ENCH_SIZE, -0.1F);
/* 590 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 592 */     tempRune = new RuneData((byte)-70, true, "have an okay glow and increase volume (7.5%)");
/* 593 */     tempRune.addModifier(ModifierEffect.ENCH_GLOW, 0.25F);
/* 594 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, 0.075F);
/* 595 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 597 */     tempRune = new RuneData((byte)-69, true, "reduce decay taken (10%)");
/* 598 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.1F);
/* 599 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 601 */     tempRune = new RuneData((byte)-68, true, "have a higher chance to be successfully improved (5%) and reduce the decay taken of items inside (5%)");
/*     */     
/* 603 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.05F);
/* 604 */     tempRune.addModifier(ModifierEffect.ENCH_INTERNAL_DECAY, 0.05F);
/* 605 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 607 */     tempRune = new RuneData((byte)-67, true, "have a chance to increase the effect of tending a field or harvesting a tree or bush (5%) and reduce decay taken (5%)");
/*     */     
/* 609 */     tempRune.addModifier(ModifierEffect.ENCH_FARMYIELD, 0.05F);
/* 610 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.05F);
/* 611 */     tempRune.setAnyTarget(true);
/* 612 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 614 */     tempRune = new RuneData((byte)-66, true, "reduce volume (10%)");
/* 615 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, -0.1F);
/* 616 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 618 */     tempRune = new RuneData((byte)-65, true, "reduce weight (5%) and increase vehicle speed (5%)");
/* 619 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.05F);
/* 620 */     tempRune.addModifier(ModifierEffect.ENCH_VEHCSPEED, 0.05F);
/* 621 */     tempRune.setAnyTarget(true);
/* 622 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 624 */     tempRune = new RuneData((byte)-64, true, "increase the chance of successfully enchanting the item (5%) and reduce the quality change when repairing damage (5%)");
/*     */     
/* 626 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.05F);
/* 627 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.05F);
/* 628 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 634 */     tempRune = new RuneData((byte)-63, true, "reduce damage taken (10%) and reduce weight (10%)");
/*     */     
/* 636 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.1F);
/* 637 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.1F);
/* 638 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 640 */     tempRune = new RuneData((byte)-62, true, "increase quality at a faster rate when being improved (10%) and increase the time an enchant holds its power on the item (10%)");
/*     */     
/* 642 */     tempRune.addModifier(ModifierEffect.ENCH_IMPQL, 0.1F);
/* 643 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHRETENTION, 0.1F);
/* 644 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 646 */     tempRune = new RuneData((byte)-61, true, "increase the chance of increasing rarity when improved (10%) and have a higher chance to be successfully improved (10%)");
/*     */     
/* 648 */     tempRune.addModifier(ModifierEffect.ENCH_RARITYIMP, 0.1F);
/* 649 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.1F);
/* 650 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 652 */     tempRune = new RuneData((byte)-60, true, "increase the chance of successfully enchanting the item (10%) and reduce decay taken (10%)");
/*     */     
/* 654 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.1F);
/* 655 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.1F);
/* 656 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 658 */     tempRune = new RuneData((byte)-59, true, "increase usage speed (10%) and increase chance to resist shattering when being enchanted (10%)");
/*     */     
/* 660 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.1F);
/* 661 */     tempRune.addModifier(ModifierEffect.ENCH_SHATTERRES, 0.1F);
/* 662 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 664 */     tempRune = new RuneData((byte)-58, true, "reduce the quality change when repairing damage (10%) and reduce damage taken (10%)");
/*     */     
/* 666 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.1F);
/* 667 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.1F);
/* 668 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 670 */     tempRune = new RuneData((byte)-57, true, "increase volume (10%) and reduce the decay taken of items inside (10%)");
/*     */     
/* 672 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, 0.1F);
/* 673 */     tempRune.addModifier(ModifierEffect.ENCH_INTERNAL_DECAY, 0.1F);
/* 674 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 676 */     tempRune = new RuneData((byte)-56, true, "reduce damage taken (10%) and reduce decay taken (10%)");
/*     */     
/* 678 */     tempRune.addModifier(ModifierEffect.ENCH_DAMAGETAKEN, -0.1F);
/* 679 */     tempRune.addModifier(ModifierEffect.ENCH_DECAY, 0.1F);
/* 680 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 682 */     tempRune = new RuneData((byte)-55, true, "have a higher chance to be successfully improved (10%) and reduce the quality change when repairing damage (10%)");
/*     */     
/* 684 */     tempRune.addModifier(ModifierEffect.ENCH_IMPPERCENT, 0.1F);
/* 685 */     tempRune.addModifier(ModifierEffect.ENCH_REPAIRQL, -0.1F);
/* 686 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 688 */     tempRune = new RuneData((byte)-54, true, "increase the time an enchant holds its power on the item (10%) and reduce size (10%)");
/*     */     
/* 690 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHRETENTION, 0.1F);
/* 691 */     tempRune.addModifier(ModifierEffect.ENCH_SIZE, -0.1F);
/* 692 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 694 */     tempRune = new RuneData((byte)-53, true, "reduce volume (10%) and increase usage speed (10%)");
/*     */     
/* 696 */     tempRune.addModifier(ModifierEffect.ENCH_VOLUME, -0.1F);
/* 697 */     tempRune.addModifier(ModifierEffect.ENCH_USESPEED, 0.1F);
/* 698 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 700 */     tempRune = new RuneData((byte)-52, true, "reduce weight (10%) and increase size (10%)");
/*     */     
/* 702 */     tempRune.addModifier(ModifierEffect.ENCH_WEIGHT, -0.1F);
/* 703 */     tempRune.addModifier(ModifierEffect.ENCH_SIZE, 0.1F);
/* 704 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */     
/* 706 */     tempRune = new RuneData((byte)-51, true, "increase chance to resist shattering when being enchanted (10%) and increase the chance of successfully enchanting the item (10%)");
/*     */     
/* 708 */     tempRune.addModifier(ModifierEffect.ENCH_SHATTERRES, -0.1F);
/* 709 */     tempRune.addModifier(ModifierEffect.ENCH_ENCHANTABILITY, 0.1F);
/* 710 */     runeDataMap.put(Byte.valueOf(tempRune.getEnchantType()), tempRune);
/*     */   }
/*     */   
/*     */   public enum ModifierEffect
/*     */   {
/* 715 */     ENCH_WEIGHT, ENCH_VOLUME, ENCH_DAMAGETAKEN, ENCH_USESPEED, ENCH_SIZE, ENCH_SKILLCHECKBONUS,
/* 716 */     ENCH_SHATTERRES, ENCH_DECAY, ENCH_INTERNAL_DECAY, ENCH_VEHCSPEED, ENCH_WIND, ENCH_IMPQL,
/* 717 */     ENCH_REPAIRQL, ENCH_FUELUSE, ENCH_ENCHANTABILITY, ENCH_ENCHRETENTION, ENCH_IMPPERCENT,
/* 718 */     ENCH_RESGATHERED, ENCH_FARMYIELD, ENCH_RARITYIMP, ENCH_GLOW, SINGLE_COLOR, SINGLE_REFRESH, SINGLE_CHANGE_AGE;
/*     */   }
/*     */   
/*     */   static class RuneData {
/*     */     private byte enchantType;
/*     */     private boolean isSingleUse;
/*     */     private boolean isEnchantment;
/*     */     private boolean anyTarget;
/*     */     private String description;
/*     */     private HashMap<RuneUtilities.ModifierEffect, Float> modifierMap;
/* 728 */     private Spell singleUse = null;
/*     */ 
/*     */     
/*     */     RuneData(byte enchantType, boolean isEnchantment, String description) {
/* 732 */       this.enchantType = enchantType;
/* 733 */       this.isEnchantment = isEnchantment;
/* 734 */       this.isSingleUse = !isEnchantment;
/* 735 */       this.description = description;
/* 736 */       this.anyTarget = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAnyTarget() {
/* 741 */       return this.anyTarget;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAnyTarget(boolean anyTarget) {
/* 746 */       this.anyTarget = anyTarget;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getEnchantType() {
/* 751 */       return this.enchantType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnchantment() {
/* 756 */       return this.isEnchantment;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setEnchantment(boolean isEnchantment) {
/* 761 */       this.isEnchantment = isEnchantment;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSingleUse() {
/* 766 */       return this.isSingleUse;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSingleUse(boolean isSingleUse) {
/* 771 */       this.isSingleUse = isSingleUse;
/*     */     }
/*     */ 
/*     */     
/*     */     public Spell getSingleUseSpell() {
/* 776 */       return this.singleUse;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSingleUseSpell(Spell singleUseSpell) {
/* 781 */       this.singleUse = singleUseSpell;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 786 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void addModifier(RuneUtilities.ModifierEffect mod, float percentage) {
/* 797 */       if (percentage < -1.0F || percentage > 1.0F) {
/*     */         return;
/*     */       }
/* 800 */       if (this.modifierMap == null) {
/* 801 */         this.modifierMap = new HashMap<>();
/*     */       }
/* 803 */       if (this.modifierMap.containsKey(mod)) {
/* 804 */         this.modifierMap.replace(mod, Float.valueOf(percentage));
/*     */       }
/* 806 */       this.modifierMap.put(mod, Float.valueOf(percentage));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     float getModifierPercentage(RuneUtilities.ModifierEffect mod) {
/* 816 */       if (this.modifierMap == null) {
/* 817 */         return 0.0F;
/*     */       }
/* 819 */       if (this.modifierMap.containsKey(mod)) {
/* 820 */         return ((Float)this.modifierMap.get(mod)).floatValue();
/*     */       }
/* 822 */       return 0.0F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\RuneUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */