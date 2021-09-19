/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Random;
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
/*     */ public final class SpellGenerator
/*     */   implements SpellTypes
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(SpellGenerator.class.getName());
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
/*     */   public static void createSpells() {
/*  50 */     logger.info("Starting to create spells");
/*  51 */     long start = System.nanoTime();
/*     */ 
/*     */     
/*  54 */     EnchantUtil.initializeEnchantGroups();
/*     */     
/*  56 */     Deity fo = Deities.getDeity(1);
/*  57 */     Deity magranon = Deities.getDeity(2);
/*  58 */     Deity libila = Deities.getDeity(4);
/*  59 */     Deity vynora = Deities.getDeity(3);
/*     */ 
/*     */     
/*  62 */     Spell[] SPELLS_COMMON = { Spells.SPELL_BLESS, Spells.SPELL_BREAK_ALTAR, Spells.SPELL_DISPEL, Spells.SPELL_LOCATE_ARTIFACT, Spells.SPELL_LOCATE_SOUL, Spells.SPELL_NOLOCATE, Spells.SPELL_PURGE, Spells.SPELL_TANGLEWEAVE, Spells.SPELL_VESSEL };
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
/*  75 */     for (Deity deity : Deities.getDeities()) {
/*     */       
/*  77 */       for (Spell spell : SPELLS_COMMON)
/*     */       {
/*  79 */         deity.addSpell(spell);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  84 */     ignoreSpells.addAll(Arrays.asList(SPELLS_COMMON));
/*     */ 
/*     */     
/*  87 */     Spell[] SPELLS_COMMON_PVE = { Spells.SPELL_SUMMON_SOUL };
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (!Servers.isThisAPvpServer() || Servers.isThisATestServer())
/*     */     {
/*  93 */       for (Deity deity : Deities.getDeities()) {
/*     */         
/*  95 */         for (Spell spell : SPELLS_COMMON_PVE)
/*     */         {
/*  97 */           deity.addSpell(spell);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 103 */     ignoreSpells.addAll(Arrays.asList(SPELLS_COMMON_PVE));
/*     */ 
/*     */     
/* 106 */     fo.addSpell(Spells.SPELL_BEARPAWS);
/* 107 */     fo.addSpell(Spells.SPELL_CHARM_ANIMAL);
/* 108 */     fo.addSpell(Spells.SPELL_CLEANSE);
/* 109 */     fo.addSpell(Spells.SPELL_COURIER);
/* 110 */     fo.addSpell(Spells.SPELL_CURE_LIGHT);
/* 111 */     fo.addSpell(Spells.SPELL_CURE_MEDIUM);
/* 112 */     fo.addSpell(Spells.SPELL_CURE_SERIOUS);
/* 113 */     fo.addSpell(Spells.SPELL_DIRT);
/* 114 */     fo.addSpell(Spells.SPELL_FOREST_GIANT_STRENGTH);
/* 115 */     fo.addSpell(Spells.SPELL_GENESIS);
/* 116 */     fo.addSpell(Spells.SPELL_HEAL);
/* 117 */     fo.addSpell(Spells.SPELL_HOLY_CROP);
/* 118 */     fo.addSpell(Spells.SPELL_HUMID_DRIZZLE);
/* 119 */     fo.addSpell(Spells.SPELL_LIFE_TRANSFER);
/* 120 */     fo.addSpell(Spells.SPELL_LIGHT_OF_FO);
/* 121 */     fo.addSpell(Spells.SPELL_LURKER_IN_THE_WOODS);
/* 122 */     fo.addSpell(Spells.SPELL_MORNING_FOG);
/* 123 */     fo.addSpell(Spells.SPELL_OAKSHELL);
/* 124 */     fo.addSpell(Spells.SPELL_PROTECT_ACID);
/* 125 */     fo.addSpell(Spells.SPELL_REFRESH);
/* 126 */     fo.addSpell(Spells.SPELL_SIXTH_SENSE);
/* 127 */     fo.addSpell(Spells.SPELL_TOXIN);
/* 128 */     fo.addSpell(Spells.SPELL_VENOM);
/* 129 */     fo.addSpell(Spells.SPELL_WARD);
/* 130 */     fo.addSpell(Spells.SPELL_WILD_GROWTH);
/* 131 */     fo.addSpell(Spells.SPELL_WILLOWSPINE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     magranon.addSpell(Spells.SPELL_BLAZE);
/* 137 */     magranon.addSpell(Spells.SPELL_DEMISE_ANIMAL);
/* 138 */     magranon.addSpell(Spells.SPELL_DEMISE_LEGENDARY);
/* 139 */     magranon.addSpell(Spells.SPELL_DEMISE_MONSTER);
/* 140 */     magranon.addSpell(Spells.SPELL_DISINTEGRATE);
/* 141 */     magranon.addSpell(Spells.SPELL_DOMINATE);
/* 142 */     magranon.addSpell(Spells.SPELL_FIREHEART);
/* 143 */     magranon.addSpell(Spells.SPELL_FIRE_PILLAR);
/* 144 */     magranon.addSpell(Spells.SPELL_FLAMING_AURA);
/* 145 */     magranon.addSpell(Spells.SPELL_FOCUSED_WILL);
/* 146 */     magranon.addSpell(Spells.SPELL_FRANTIC_CHARGE);
/* 147 */     magranon.addSpell(Spells.SPELL_GOAT_SHAPE);
/* 148 */     magranon.addSpell(Spells.SPELL_INFERNO);
/* 149 */     magranon.addSpell(Spells.SPELL_LIGHT_TOKEN);
/* 150 */     magranon.addSpell(Spells.SPELL_LURKER_IN_THE_DARK);
/* 151 */     magranon.addSpell(Spells.SPELL_LURKER_IN_THE_WOODS);
/* 152 */     magranon.addSpell(Spells.SPELL_MASS_STAMINA);
/* 153 */     magranon.addSpell(Spells.SPELL_MOLE_SENSES);
/* 154 */     magranon.addSpell(Spells.SPELL_PROTECT_FROST);
/* 155 */     magranon.addSpell(Spells.SPELL_RITUAL_OF_THE_SUN);
/* 156 */     magranon.addSpell(Spells.SPELL_SIXTH_SENSE);
/* 157 */     magranon.addSpell(Spells.SPELL_SMITE);
/* 158 */     magranon.addSpell(Spells.SPELL_STRONGWALL);
/* 159 */     magranon.addSpell(Spells.SPELL_SUNDER);
/* 160 */     magranon.addSpell(Spells.SPELL_WEB_ARMOUR);
/* 161 */     magranon.addSpell(Spells.SPELL_WRATH_OF_MAGRANON);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     libila.addSpell(Spells.SPELL_AURA_SHARED_PAIN);
/* 167 */     libila.addSpell(Spells.SPELL_BLESSINGS_OF_THE_DARK);
/* 168 */     libila.addSpell(Spells.SPELL_BLOODTHIRST);
/* 169 */     libila.addSpell(Spells.SPELL_CORROSION);
/* 170 */     libila.addSpell(Spells.SPELL_CORRUPT);
/* 171 */     libila.addSpell(Spells.SPELL_DARK_MESSENGER);
/* 172 */     libila.addSpell(Spells.SPELL_DEMISE_ANIMAL);
/* 173 */     libila.addSpell(Spells.SPELL_DEMISE_HUMAN);
/* 174 */     libila.addSpell(Spells.SPELL_DEMISE_LEGENDARY);
/* 175 */     libila.addSpell(Spells.SPELL_DEMISE_MONSTER);
/* 176 */     libila.addSpell(Spells.SPELL_DRAIN_HEALTH);
/* 177 */     libila.addSpell(Spells.SPELL_DRAIN_STAMINA);
/* 178 */     libila.addSpell(Spells.SPELL_ESSENCE_DRAIN);
/* 179 */     libila.addSpell(Spells.SPELL_FUNGUS_TRAP);
/* 180 */     libila.addSpell(Spells.SPELL_HELL_STRENGTH);
/* 181 */     libila.addSpell(Spells.SPELL_LAND_OF_THE_DEAD);
/* 182 */     libila.addSpell(Spells.SPELL_LURKER_IN_THE_DARK);
/* 183 */     libila.addSpell(Spells.SPELL_PAIN_RAIN);
/* 184 */     libila.addSpell(Spells.SPELL_PHANTASMS);
/* 185 */     libila.addSpell(Spells.SPELL_PROTECT_POISON);
/* 186 */     libila.addSpell(Spells.SPELL_REBIRTH);
/* 187 */     libila.addSpell(Spells.SPELL_RITE_OF_DEATH);
/* 188 */     libila.addSpell(Spells.SPELL_ROTTING_GUT);
/* 189 */     libila.addSpell(Spells.SPELL_ROTTING_TOUCH);
/* 190 */     libila.addSpell(Spells.SPELL_SCORN_OF_LIBILA);
/* 191 */     libila.addSpell(Spells.SPELL_TRUEHIT);
/* 192 */     libila.addSpell(Spells.SPELL_WEAKNESS);
/* 193 */     libila.addSpell(Spells.SPELL_WEB_ARMOUR);
/* 194 */     libila.addSpell(Spells.SPELL_WORM_BRAINS);
/* 195 */     libila.addSpell(Spells.SPELL_ZOMBIE_INFESTATION);
/* 196 */     if (Servers.localServer.PVPSERVER)
/*     */     {
/* 198 */       libila.addSpell(Spells.SPELL_STRONGWALL);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     vynora.addSpell(Spells.SPELL_AURA_SHARED_PAIN);
/* 205 */     vynora.addSpell(Spells.SPELL_CIRCLE_OF_CUNNING);
/* 206 */     vynora.addSpell(Spells.SPELL_DEMISE_ANIMAL);
/* 207 */     vynora.addSpell(Spells.SPELL_DEMISE_HUMAN);
/* 208 */     vynora.addSpell(Spells.SPELL_DEMISE_LEGENDARY);
/* 209 */     vynora.addSpell(Spells.SPELL_DEMISE_MONSTER);
/* 210 */     vynora.addSpell(Spells.SPELL_EXCEL);
/* 211 */     vynora.addSpell(Spells.SPELL_FROSTBRAND);
/* 212 */     vynora.addSpell(Spells.SPELL_GLACIAL);
/* 213 */     vynora.addSpell(Spells.SPELL_HYPOTHERMIA);
/* 214 */     vynora.addSpell(Spells.SPELL_ICE_PILLAR);
/* 215 */     vynora.addSpell(Spells.SPELL_LURKER_IN_THE_DEEP);
/* 216 */     vynora.addSpell(Spells.SPELL_MEND);
/* 217 */     vynora.addSpell(Spells.SPELL_MIND_STEALER);
/* 218 */     vynora.addSpell(Spells.SPELL_NIMBLENESS);
/* 219 */     vynora.addSpell(Spells.SPELL_OPULENCE);
/* 220 */     vynora.addSpell(Spells.SPELL_PROTECT_FIRE);
/* 221 */     vynora.addSpell(Spells.SPELL_REVEAL_CREATURES);
/* 222 */     vynora.addSpell(Spells.SPELL_REVEAL_SETTLEMENTS);
/* 223 */     vynora.addSpell(Spells.SPELL_RITE_OF_SPRING);
/* 224 */     vynora.addSpell(Spells.SPELL_SHARD_OF_ICE);
/* 225 */     vynora.addSpell(Spells.SPELL_SIXTH_SENSE);
/* 226 */     vynora.addSpell(Spells.SPELL_TENTACLES);
/* 227 */     vynora.addSpell(Spells.SPELL_TORNADO);
/* 228 */     vynora.addSpell(Spells.SPELL_WIND_OF_AGES);
/* 229 */     vynora.addSpell(Spells.SPELL_WISDOM_OF_VYNORA);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     generateDemigodSpells();
/*     */     
/* 236 */     long end = System.nanoTime();
/* 237 */     logger.info("Generating spells took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */   }
/*     */   
/* 240 */   protected static HashSet<Spell> ignoreSpells = new HashSet<>();
/*     */   
/*     */   private static class SpellGroup {
/* 243 */     protected ArrayList<Integer> spells = new ArrayList<>(); protected Deity deity;
/*     */     
/*     */     SpellGroup(Deity deity) {
/* 246 */       this.deity = deity;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addSpellChance(Spell spell, int amount) {
/* 256 */       for (int i = 0; i < amount; i++)
/*     */       {
/* 258 */         this.spells.add(Integer.valueOf(spell.getNumber()));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addEmptyChance(int amount) {
/* 268 */       for (int i = 0; i < amount; i++)
/*     */       {
/* 270 */         this.spells.add(Integer.valueOf(-10));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getRandomSpell(Random rand) {
/* 280 */       if (this.spells.size() > 0) {
/* 281 */         int index = rand.nextInt(this.spells.size());
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
/* 293 */         return ((Integer)this.spells.get(index)).intValue();
/*     */       } 
/* 295 */       SpellGenerator.logger.warning(String.format("Attempting to obtain a random spell for deity %s failed because no spells available.", new Object[] { this.deity
/* 296 */               .getName() }));
/* 297 */       return -10;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollTamingGroup(Deity deity, Random rand) {
/* 304 */     SpellGroup taming = new SpellGroup(deity);
/*     */ 
/*     */     
/* 307 */     if (deity.isBefriendCreature()) {
/* 308 */       taming.addSpellChance(Spells.SPELL_CHARM_ANIMAL, 3);
/*     */     } else {
/* 310 */       taming.addSpellChance(Spells.SPELL_CHARM_ANIMAL, 2);
/*     */     } 
/*     */     
/* 313 */     if (deity.isHateGod()) {
/* 314 */       taming.addSpellChance(Spells.SPELL_REBIRTH, 2);
/*     */     }
/*     */     
/* 317 */     if (deity.isWarrior()) {
/* 318 */       taming.addSpellChance(Spells.SPELL_DOMINATE, 3);
/*     */     } else {
/* 320 */       taming.addSpellChance(Spells.SPELL_DOMINATE, 2);
/*     */     } 
/* 322 */     taming.addEmptyChance(2);
/*     */     
/* 324 */     return taming.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollAreaHealingGroup(Deity deity, Random rand) {
/* 330 */     SpellGroup areaHealing = new SpellGroup(deity);
/*     */ 
/*     */     
/* 333 */     if (!deity.isHateGod())
/*     */     {
/*     */       
/* 336 */       if (deity.isWarrior()) {
/* 337 */         areaHealing.addSpellChance(Spells.SPELL_LIGHT_OF_FO, 1);
/* 338 */       } else if (deity.isHealer()) {
/* 339 */         areaHealing.addSpellChance(Spells.SPELL_LIGHT_OF_FO, 4);
/*     */       } else {
/* 341 */         areaHealing.addSpellChance(Spells.SPELL_LIGHT_OF_FO, 3);
/*     */       } 
/*     */     }
/*     */     
/* 345 */     if (deity.isHateGod())
/*     */     {
/*     */       
/* 348 */       if (deity.isWarrior()) {
/* 349 */         areaHealing.addSpellChance(Spells.SPELL_SCORN_OF_LIBILA, 1);
/* 350 */       } else if (deity.isHealer()) {
/* 351 */         areaHealing.addSpellChance(Spells.SPELL_SCORN_OF_LIBILA, 4);
/*     */       } else {
/* 353 */         areaHealing.addSpellChance(Spells.SPELL_SCORN_OF_LIBILA, 3);
/*     */       } 
/*     */     }
/*     */     
/* 357 */     if (deity.isMountainGod()) {
/* 358 */       areaHealing.addSpellChance(Spells.SPELL_MASS_STAMINA, 3);
/*     */     } else {
/* 360 */       areaHealing.addSpellChance(Spells.SPELL_MASS_STAMINA, 1);
/*     */     } 
/* 362 */     areaHealing.addEmptyChance(2);
/*     */     
/* 364 */     return areaHealing.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollAreaCombatGroup(Deity deity, Random rand) {
/* 370 */     SpellGroup areaCombat = new SpellGroup(deity);
/*     */     
/* 372 */     if (deity.hasSpell(Spells.SPELL_LIGHT_OF_FO) || deity.hasSpell(Spells.SPELL_SCORN_OF_LIBILA)) {
/*     */ 
/*     */       
/* 375 */       if (deity.isWarrior())
/*     */       {
/*     */         
/* 378 */         return -10;
/*     */       }
/*     */       
/* 381 */       areaCombat.addSpellChance(Spells.SPELL_FIRE_PILLAR, 1);
/* 382 */       areaCombat.addSpellChance(Spells.SPELL_ICE_PILLAR, 1);
/* 383 */       if (deity.isHateGod())
/*     */       {
/* 385 */         areaCombat.addSpellChance(Spells.SPELL_FUNGUS_TRAP, 1);
/* 386 */         areaCombat.addSpellChance(Spells.SPELL_PAIN_RAIN, 1);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 393 */       if (deity.isWarrior()) {
/* 394 */         areaCombat.addSpellChance(Spells.SPELL_FIRE_PILLAR, 4);
/*     */       } else {
/* 396 */         areaCombat.addSpellChance(Spells.SPELL_FIRE_PILLAR, 2);
/*     */       } 
/* 398 */       if (deity.isWarrior()) {
/* 399 */         areaCombat.addSpellChance(Spells.SPELL_ICE_PILLAR, 4);
/*     */       } else {
/* 401 */         areaCombat.addSpellChance(Spells.SPELL_ICE_PILLAR, 2);
/*     */       } 
/* 403 */       if (deity.isHateGod()) {
/*     */         
/* 405 */         if (deity.isWarrior()) {
/* 406 */           areaCombat.addSpellChance(Spells.SPELL_FUNGUS_TRAP, 3);
/*     */         } else {
/* 408 */           areaCombat.addSpellChance(Spells.SPELL_FUNGUS_TRAP, 2);
/*     */         } 
/* 410 */         if (deity.isWarrior()) {
/* 411 */           areaCombat.addSpellChance(Spells.SPELL_PAIN_RAIN, 3);
/*     */         } else {
/* 413 */           areaCombat.addSpellChance(Spells.SPELL_PAIN_RAIN, 2);
/*     */         } 
/*     */       } 
/* 416 */     }  areaCombat.addEmptyChance(2);
/*     */     
/* 418 */     return areaCombat.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollWeaponEnchantGroup(Deity deity, Random rand) {
/* 424 */     SpellGroup weaponEnchant = new SpellGroup(deity);
/*     */     
/* 426 */     if (deity.isForestGod()) {
/* 427 */       weaponEnchant.addSpellChance(Spells.SPELL_VENOM, 4);
/*     */     } else {
/* 429 */       weaponEnchant.addSpellChance(Spells.SPELL_VENOM, 1);
/*     */     } 
/* 431 */     if (deity.isHateGod()) {
/* 432 */       weaponEnchant.addSpellChance(Spells.SPELL_BLOODTHIRST, 4);
/*     */     } else {
/* 434 */       weaponEnchant.addSpellChance(Spells.SPELL_BLOODTHIRST, 2);
/*     */     } 
/* 436 */     weaponEnchant.addSpellChance(Spells.SPELL_ROTTING_TOUCH, 2);
/* 437 */     weaponEnchant.addSpellChance(Spells.SPELL_FROSTBRAND, 2);
/* 438 */     weaponEnchant.addSpellChance(Spells.SPELL_FLAMING_AURA, 2);
/*     */     
/* 440 */     return weaponEnchant.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollWeaponAugmentGroup(Deity deity, Random rand) {
/* 446 */     SpellGroup weaponAugment = new SpellGroup(deity);
/*     */     
/* 448 */     if (deity.isHealer()) {
/* 449 */       weaponAugment.addSpellChance(Spells.SPELL_LIFE_TRANSFER, 4);
/* 450 */     } else if (deity.isForestGod()) {
/* 451 */       weaponAugment.addSpellChance(Spells.SPELL_LIFE_TRANSFER, 3);
/*     */     } else {
/* 453 */       weaponAugment.addSpellChance(Spells.SPELL_LIFE_TRANSFER, 2);
/*     */     } 
/* 455 */     if (deity.isHateGod()) {
/* 456 */       weaponAugment.addSpellChance(Spells.SPELL_ESSENCE_DRAIN, 4);
/*     */     } else {
/* 458 */       weaponAugment.addSpellChance(Spells.SPELL_ESSENCE_DRAIN, 2);
/*     */     } 
/* 460 */     if (deity.isWaterGod()) {
/* 461 */       weaponAugment.addSpellChance(Spells.SPELL_NIMBLENESS, 5);
/*     */     } else {
/* 463 */       weaponAugment.addSpellChance(Spells.SPELL_NIMBLENESS, 2);
/*     */     } 
/* 465 */     weaponAugment.addEmptyChance(1);
/*     */     
/* 467 */     return weaponAugment.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollIndustrialEnchantGroup(Deity deity, Random rand) {
/* 473 */     SpellGroup industrialEnchant = new SpellGroup(deity);
/*     */     
/* 475 */     if (deity.isLearner()) {
/* 476 */       industrialEnchant.addSpellChance(Spells.SPELL_CIRCLE_OF_CUNNING, 4);
/* 477 */     } else if (deity.isHateGod()) {
/* 478 */       industrialEnchant.addSpellChance(Spells.SPELL_CIRCLE_OF_CUNNING, 1);
/*     */     } else {
/* 480 */       industrialEnchant.addSpellChance(Spells.SPELL_CIRCLE_OF_CUNNING, 2);
/*     */     } 
/* 482 */     if (deity.isHateGod()) {
/* 483 */       industrialEnchant.addSpellChance(Spells.SPELL_BLESSINGS_OF_THE_DARK, 4);
/*     */     }
/* 485 */     if (deity.isLearner() || deity.isRoadProtector()) {
/* 486 */       industrialEnchant.addSpellChance(Spells.SPELL_BLESSINGS_OF_THE_DARK, 4);
/*     */     } else {
/* 488 */       industrialEnchant.addSpellChance(Spells.SPELL_BLESSINGS_OF_THE_DARK, 2);
/*     */     } 
/* 490 */     if (deity.isRoadProtector()) {
/* 491 */       industrialEnchant.addSpellChance(Spells.SPELL_WIND_OF_AGES, 4);
/*     */     } else {
/* 493 */       industrialEnchant.addSpellChance(Spells.SPELL_WIND_OF_AGES, 2);
/*     */     } 
/* 495 */     industrialEnchant.addEmptyChance(1);
/*     */     
/* 497 */     return industrialEnchant.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollCreatureEnchantGroup(Deity deity, Random rand) {
/* 503 */     SpellGroup creatureEnchant = new SpellGroup(deity);
/*     */ 
/*     */     
/* 506 */     if (deity.isForestGod()) {
/* 507 */       creatureEnchant.addSpellChance(Spells.SPELL_OAKSHELL, 1);
/*     */     }
/* 509 */     if (deity.isWarrior()) {
/* 510 */       creatureEnchant.addSpellChance(Spells.SPELL_OAKSHELL, 1);
/* 511 */     } else if (deity.isBefriendCreature()) {
/* 512 */       creatureEnchant.addSpellChance(Spells.SPELL_OAKSHELL, 4);
/*     */     } else {
/* 514 */       creatureEnchant.addSpellChance(Spells.SPELL_OAKSHELL, 2);
/*     */     } 
/* 516 */     if (deity.isWaterGod()) {
/* 517 */       creatureEnchant.addSpellChance(Spells.SPELL_EXCEL, 1);
/*     */     }
/*     */     
/* 520 */     if (deity.hasSpell(Spells.SPELL_NIMBLENESS)) {
/* 521 */       creatureEnchant.addSpellChance(Spells.SPELL_EXCEL, 3);
/*     */     } else {
/* 523 */       creatureEnchant.addSpellChance(Spells.SPELL_EXCEL, 1);
/*     */     } 
/*     */     
/* 526 */     if (deity.hasSpell(Spells.SPELL_BLESSINGS_OF_THE_DARK)) {
/* 527 */       creatureEnchant.addSpellChance(Spells.SPELL_TRUEHIT, 3);
/* 528 */     } else if (deity.isHateGod()) {
/* 529 */       creatureEnchant.addSpellChance(Spells.SPELL_TRUEHIT, 2);
/*     */     } else {
/* 531 */       creatureEnchant.addSpellChance(Spells.SPELL_TRUEHIT, 1);
/*     */     } 
/* 533 */     creatureEnchant.addEmptyChance(1);
/*     */     
/* 535 */     return creatureEnchant.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollJewelryOffenseGroup(Deity deity, Random rand) {
/* 541 */     SpellGroup jewelryOffense = new SpellGroup(deity);
/*     */     
/* 543 */     if (deity.isForestGod()) {
/* 544 */       jewelryOffense.addSpellChance(Spells.SPELL_TOXIN, 3);
/*     */     } else {
/* 546 */       jewelryOffense.addSpellChance(Spells.SPELL_TOXIN, 1);
/*     */     } 
/* 548 */     if (deity.isMountainGod()) {
/* 549 */       jewelryOffense.addSpellChance(Spells.SPELL_BLAZE, 3);
/*     */     } else {
/* 551 */       jewelryOffense.addSpellChance(Spells.SPELL_BLAZE, 1);
/*     */     } 
/* 553 */     if (deity.isWaterGod()) {
/* 554 */       jewelryOffense.addSpellChance(Spells.SPELL_GLACIAL, 3);
/*     */     } else {
/* 556 */       jewelryOffense.addSpellChance(Spells.SPELL_GLACIAL, 1);
/*     */     } 
/* 558 */     if (deity.isHateGod()) {
/* 559 */       jewelryOffense.addSpellChance(Spells.SPELL_CORROSION, 3);
/*     */     } else {
/* 561 */       jewelryOffense.addSpellChance(Spells.SPELL_CORROSION, 1);
/*     */     } 
/* 563 */     return jewelryOffense.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollJewelryDefenseGroup(Deity deity, Random rand) {
/* 569 */     SpellGroup jewelryDefense = new SpellGroup(deity);
/*     */     
/* 571 */     jewelryDefense.addSpellChance(Spells.SPELL_PROTECT_ACID, 1);
/* 572 */     jewelryDefense.addSpellChance(Spells.SPELL_PROTECT_FIRE, 1);
/* 573 */     jewelryDefense.addSpellChance(Spells.SPELL_PROTECT_FROST, 1);
/* 574 */     jewelryDefense.addSpellChance(Spells.SPELL_PROTECT_POISON, 1);
/*     */     
/* 576 */     return jewelryDefense.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollCombatDamageGroup(Deity deity, Random rand) {
/* 582 */     SpellGroup combatDamage = new SpellGroup(deity);
/*     */     
/* 584 */     if (deity.isMountainGod()) {
/* 585 */       combatDamage.addSpellChance(Spells.SPELL_FIREHEART, 3);
/*     */     } else {
/* 587 */       combatDamage.addSpellChance(Spells.SPELL_FIREHEART, 1);
/*     */     } 
/* 589 */     if (deity.isHateGod()) {
/* 590 */       combatDamage.addSpellChance(Spells.SPELL_ROTTING_GUT, 3);
/*     */     } else {
/* 592 */       combatDamage.addSpellChance(Spells.SPELL_ROTTING_GUT, 1);
/*     */     } 
/* 594 */     if (deity.isWaterGod()) {
/* 595 */       combatDamage.addSpellChance(Spells.SPELL_SHARD_OF_ICE, 3);
/*     */     } else {
/* 597 */       combatDamage.addSpellChance(Spells.SPELL_SHARD_OF_ICE, 1);
/*     */     } 
/* 599 */     if (!deity.isWarrior() && (deity.isForestGod() || deity.isWaterGod())) {
/* 600 */       combatDamage.addEmptyChance(2);
/*     */     }
/* 602 */     return combatDamage.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollMajorDamageGroup(Deity deity, Random rand) {
/* 608 */     SpellGroup majorDamage = new SpellGroup(deity);
/*     */     
/* 610 */     if (deity.isMountainGod()) {
/* 611 */       majorDamage.addSpellChance(Spells.SPELL_INFERNO, 3);
/*     */     } else {
/* 613 */       majorDamage.addSpellChance(Spells.SPELL_INFERNO, 1);
/*     */     } 
/* 615 */     if (deity.isHateGod()) {
/* 616 */       majorDamage.addSpellChance(Spells.SPELL_WORM_BRAINS, 3);
/*     */     } else {
/* 618 */       majorDamage.addSpellChance(Spells.SPELL_WORM_BRAINS, 1);
/*     */     } 
/* 620 */     if (deity.isWaterGod()) {
/* 621 */       majorDamage.addSpellChance(Spells.SPELL_HYPOTHERMIA, 3);
/*     */     } else {
/* 623 */       majorDamage.addSpellChance(Spells.SPELL_HYPOTHERMIA, 1);
/*     */     } 
/* 625 */     if (!deity.isWarrior() && (deity.isForestGod() || deity.isWaterGod())) {
/* 626 */       majorDamage.addEmptyChance(2);
/*     */     }
/* 628 */     return majorDamage.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollTileChangeGroup(Deity deity, Random rand) {
/* 634 */     SpellGroup tileChange = new SpellGroup(deity);
/*     */     
/* 636 */     if (deity.isHateGod()) {
/* 637 */       tileChange.addSpellChance(Spells.SPELL_CORRUPT, 1);
/* 638 */     } else if (deity.isForestGod()) {
/* 639 */       tileChange.addSpellChance(Spells.SPELL_CLEANSE, 1);
/*     */     }
/*     */     else {
/*     */       
/* 643 */       tileChange.addSpellChance(Spells.SPELL_CLEANSE, 1);
/* 644 */       tileChange.addEmptyChance(2);
/*     */     } 
/*     */     
/* 647 */     return tileChange.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollRiteGroup(Deity deity, Random rand) {
/* 653 */     SpellGroup rite = new SpellGroup(deity);
/*     */     
/* 655 */     if (deity.isHateGod()) {
/* 656 */       rite.addSpellChance(Spells.SPELL_RITE_OF_DEATH, 1);
/* 657 */     } else if (deity.isForestGod()) {
/* 658 */       rite.addSpellChance(Spells.SPELL_HOLY_CROP, 1);
/* 659 */     } else if (deity.isMountainGod()) {
/* 660 */       rite.addSpellChance(Spells.SPELL_RITUAL_OF_THE_SUN, 1);
/* 661 */     } else if (deity.isWaterGod()) {
/* 662 */       rite.addSpellChance(Spells.SPELL_RITE_OF_SPRING, 1);
/*     */     } else {
/* 664 */       logger.warning(String.format("Deity %s has no template deity.", new Object[] { deity.getName() }));
/*     */     } 
/* 666 */     return rite.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollDisintegrate(Deity deity, Random rand) {
/* 673 */     SpellGroup disintegrate = new SpellGroup(deity);
/*     */     
/* 675 */     if (deity.isWarrior()) {
/* 676 */       disintegrate.addSpellChance(Spells.SPELL_DISINTEGRATE, 4);
/* 677 */     } else if (deity.isMountainGod()) {
/* 678 */       disintegrate.addSpellChance(Spells.SPELL_DISINTEGRATE, 3);
/*     */     } else {
/* 680 */       disintegrate.addSpellChance(Spells.SPELL_DISINTEGRATE, 2);
/*     */     } 
/* 682 */     disintegrate.addEmptyChance(3);
/*     */     
/* 684 */     return disintegrate.getRandomSpell(rand);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int rollStrongwall(Deity deity, Random rand) {
/* 692 */     SpellGroup strongwall = new SpellGroup(deity);
/*     */     
/* 694 */     if (deity.hasSpell(Spells.SPELL_DISINTEGRATE))
/* 695 */       return -10; 
/* 696 */     if (deity.isMountainGod()) {
/* 697 */       strongwall.addSpellChance(Spells.SPELL_STRONGWALL, 8);
/*     */     } else {
/* 699 */       strongwall.addSpellChance(Spells.SPELL_STRONGWALL, 2);
/*     */     } 
/* 701 */     strongwall.addEmptyChance(2);
/*     */     
/* 703 */     return strongwall.getRandomSpell(rand);
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
/*     */   private static void addDeitySpell(String group, Deity deity, int spellId) {
/* 715 */     if (spellId > 0) {
/*     */       
/* 717 */       deity.addSpell(Spells.getSpell(spellId));
/* 718 */       logger.info(String.format("%s obtains spell %s from %s group.", new Object[] { deity
/* 719 */               .getName(), Spells.getSpell(spellId).getName(), group }));
/*     */     }
/*     */     else {
/*     */       
/* 723 */       logger.info(String.format("%s obtains no spell from %s group.", new Object[] { deity
/* 724 */               .getName(), group }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void generateDemigodSpells() {
/* 735 */     ignoreSpells.add(Spells.SPELL_CHARM_ANIMAL);
/* 736 */     ignoreSpells.add(Spells.SPELL_REBIRTH);
/* 737 */     ignoreSpells.add(Spells.SPELL_DOMINATE);
/*     */     
/* 739 */     ignoreSpells.add(Spells.SPELL_LIGHT_OF_FO);
/* 740 */     ignoreSpells.add(Spells.SPELL_SCORN_OF_LIBILA);
/* 741 */     ignoreSpells.add(Spells.SPELL_MASS_STAMINA);
/*     */     
/* 743 */     ignoreSpells.add(Spells.SPELL_FIRE_PILLAR);
/* 744 */     ignoreSpells.add(Spells.SPELL_ICE_PILLAR);
/* 745 */     ignoreSpells.add(Spells.SPELL_FUNGUS_TRAP);
/* 746 */     ignoreSpells.add(Spells.SPELL_PAIN_RAIN);
/*     */     
/* 748 */     ignoreSpells.add(Spells.SPELL_BLOODTHIRST);
/* 749 */     ignoreSpells.add(Spells.SPELL_FLAMING_AURA);
/* 750 */     ignoreSpells.add(Spells.SPELL_FROSTBRAND);
/* 751 */     ignoreSpells.add(Spells.SPELL_ROTTING_TOUCH);
/* 752 */     ignoreSpells.add(Spells.SPELL_VENOM);
/*     */     
/* 754 */     ignoreSpells.add(Spells.SPELL_LIFE_TRANSFER);
/* 755 */     ignoreSpells.add(Spells.SPELL_NIMBLENESS);
/*     */     
/* 757 */     ignoreSpells.add(Spells.SPELL_BLESSINGS_OF_THE_DARK);
/* 758 */     ignoreSpells.add(Spells.SPELL_CIRCLE_OF_CUNNING);
/* 759 */     ignoreSpells.add(Spells.SPELL_WIND_OF_AGES);
/*     */     
/* 761 */     ignoreSpells.add(Spells.SPELL_EXCEL);
/* 762 */     ignoreSpells.add(Spells.SPELL_OAKSHELL);
/* 763 */     ignoreSpells.add(Spells.SPELL_TRUEHIT);
/*     */     
/* 765 */     ignoreSpells.add(Spells.SPELL_BLAZE);
/* 766 */     ignoreSpells.add(Spells.SPELL_CORROSION);
/* 767 */     ignoreSpells.add(Spells.SPELL_GLACIAL);
/* 768 */     ignoreSpells.add(Spells.SPELL_TOXIN);
/*     */     
/* 770 */     ignoreSpells.add(Spells.SPELL_PROTECT_ACID);
/* 771 */     ignoreSpells.add(Spells.SPELL_PROTECT_FIRE);
/* 772 */     ignoreSpells.add(Spells.SPELL_PROTECT_FROST);
/* 773 */     ignoreSpells.add(Spells.SPELL_PROTECT_POISON);
/*     */     
/* 775 */     ignoreSpells.add(Spells.SPELL_FIREHEART);
/* 776 */     ignoreSpells.add(Spells.SPELL_ROTTING_GUT);
/* 777 */     ignoreSpells.add(Spells.SPELL_SHARD_OF_ICE);
/*     */     
/* 779 */     ignoreSpells.add(Spells.SPELL_HYPOTHERMIA);
/* 780 */     ignoreSpells.add(Spells.SPELL_INFERNO);
/* 781 */     ignoreSpells.add(Spells.SPELL_WORM_BRAINS);
/*     */     
/* 783 */     ignoreSpells.add(Spells.SPELL_HOLY_CROP);
/* 784 */     ignoreSpells.add(Spells.SPELL_RITE_OF_DEATH);
/* 785 */     ignoreSpells.add(Spells.SPELL_RITE_OF_SPRING);
/* 786 */     ignoreSpells.add(Spells.SPELL_RITUAL_OF_THE_SUN);
/*     */ 
/*     */     
/* 789 */     ignoreSpells.add(Spells.SPELL_DISINTEGRATE);
/* 790 */     ignoreSpells.add(Spells.SPELL_STRONGWALL);
/* 791 */     ignoreSpells.add(Spells.SPELL_CORRUPT);
/* 792 */     ignoreSpells.add(Spells.SPELL_CLEANSE);
/*     */ 
/*     */     
/* 795 */     for (Deity deity : Deities.getDeities()) {
/*     */ 
/*     */       
/* 798 */       if (deity.isCustomDeity()) {
/*     */ 
/*     */         
/* 801 */         Random rand = deity.initializeAndGetRand();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 806 */         addDeitySpell("Taming", deity, rollTamingGroup(deity, rand));
/*     */ 
/*     */         
/* 809 */         addDeitySpell("Area Healing", deity, rollAreaHealingGroup(deity, rand));
/*     */ 
/*     */         
/* 812 */         addDeitySpell("Area Combat", deity, rollAreaCombatGroup(deity, rand));
/*     */ 
/*     */         
/* 815 */         addDeitySpell("Weapon Enchant", deity, rollWeaponEnchantGroup(deity, rand));
/*     */ 
/*     */         
/* 818 */         addDeitySpell("Weapon Augment", deity, rollWeaponAugmentGroup(deity, rand));
/*     */ 
/*     */         
/* 821 */         addDeitySpell("Industrial Enchant", deity, rollIndustrialEnchantGroup(deity, rand));
/*     */ 
/*     */         
/* 824 */         addDeitySpell("Creature Enchant", deity, rollCreatureEnchantGroup(deity, rand));
/*     */ 
/*     */         
/* 827 */         addDeitySpell("Jewelry Offense", deity, rollJewelryOffenseGroup(deity, rand));
/*     */ 
/*     */         
/* 830 */         addDeitySpell("Jewelry Defense", deity, rollJewelryDefenseGroup(deity, rand));
/*     */ 
/*     */         
/* 833 */         addDeitySpell("Combat Damage", deity, rollCombatDamageGroup(deity, rand));
/*     */ 
/*     */         
/* 836 */         addDeitySpell("Major Damage", deity, rollMajorDamageGroup(deity, rand));
/*     */ 
/*     */         
/* 839 */         addDeitySpell("Tile Change", deity, rollTileChangeGroup(deity, rand));
/*     */ 
/*     */         
/* 842 */         addDeitySpell("Rite", deity, rollRiteGroup(deity, rand));
/*     */ 
/*     */         
/* 845 */         addDeitySpell("Disintegrate", deity, rollDisintegrate(deity, rand));
/* 846 */         addDeitySpell("Strongwall", deity, rollStrongwall(deity, rand));
/*     */ 
/*     */         
/* 849 */         for (Spell spell : Spells.getAllSpells()) {
/*     */           
/* 851 */           if (!spell.isKarmaSpell()) {
/*     */ 
/*     */ 
/*     */             
/* 855 */             if (!ignoreSpells.contains(spell) && spell.deityCanHaveSpell(deity.getNumber())) {
/* 856 */               deity.addSpell(spell);
/*     */             }
/*     */             
/* 859 */             if (Servers.isThisAPvpServer() && spell.getNumber() == Spells.SPELL_DISINTEGRATE.getNumber() && !deity.hasSpell(spell))
/*     */             {
/* 861 */               deity.addSpell(spell); } 
/*     */           } 
/*     */         } 
/* 864 */         if (deity.getNumber() == 31) {
/*     */           
/* 866 */           deity.removeSpell(Spells.SPELL_LIFE_TRANSFER);
/* 867 */           deity.addSpell(Spells.SPELL_BLOODTHIRST);
/* 868 */           deity.addSpell(Spells.SPELL_WEB_ARMOUR);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void generateLegacyDemigodSpells() {
/* 877 */     for (Deity deity : Deities.getDeities()) {
/*     */       
/* 879 */       if (deity.getNumber() > 4) {
/*     */         
/* 881 */         Random rand = deity.initializeAndGetRand();
/* 882 */         for (Spell spell : Spells.getAllSpells()) {
/*     */           
/* 884 */           if (!spell.isKarmaSpell()) {
/*     */             
/* 886 */             boolean blocked = false;
/* 887 */             if (spell == Spells.SPELL_REBIRTH)
/*     */             {
/* 889 */               if (deity.hasSpell(Spells.SPELL_OAKSHELL)) {
/* 890 */                 blocked = true;
/* 891 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_OAKSHELL.getNumber())) {
/* 892 */                 blocked = true;
/*     */               }  } 
/* 894 */             if (spell == Spells.SPELL_SCORN_OF_LIBILA)
/*     */             {
/* 896 */               if (deity.hasSpell(Spells.SPELL_SCORN_OF_LIBILA)) {
/* 897 */                 blocked = true;
/* 898 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_LIGHT_OF_FO.getNumber())) {
/* 899 */                 blocked = true;
/*     */               }  } 
/* 901 */             if (spell == Spells.SPELL_FIRE_PILLAR)
/*     */             {
/* 903 */               if (deity.hasSpell(Spells.SPELL_ICE_PILLAR) || deity.hasSpell(Spells.SPELL_PAIN_RAIN)) {
/* 904 */                 blocked = true;
/* 905 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_ICE_PILLAR.getNumber())) {
/* 906 */                 blocked = true;
/* 907 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_PAIN_RAIN.getNumber())) {
/* 908 */                 blocked = true;
/*     */               }  } 
/* 910 */             if (spell == Spells.SPELL_ICE_PILLAR)
/*     */             {
/* 912 */               if (deity.hasSpell(Spells.SPELL_PAIN_RAIN)) {
/* 913 */                 blocked = true;
/* 914 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_PAIN_RAIN.getNumber())) {
/* 915 */                 blocked = true;
/*     */               }  } 
/* 917 */             if (spell == Spells.SPELL_ROTTING_GUT)
/*     */             {
/* 919 */               if (deity.hasSpell(Spells.SPELL_HEAL)) {
/* 920 */                 blocked = true;
/* 921 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_HEAL.getNumber())) {
/* 922 */                 blocked = true;
/*     */               }  } 
/* 924 */             if (spell == Spells.SPELL_WORM_BRAINS)
/*     */             {
/* 926 */               if (deity.hasSpell(Spells.SPELL_HEAL)) {
/* 927 */                 blocked = true;
/* 928 */               } else if (spell.isSpellBlocked(deity.getNumber(), Spells.SPELL_HEAL.getNumber())) {
/* 929 */                 blocked = true;
/*     */               } 
/*     */             }
/*     */             
/* 933 */             if (!blocked && spell.deityCanHaveSpell(deity.getNumber()))
/*     */             {
/* 935 */               if (!deity.hasSpell(spell))
/*     */               {
/*     */                 
/* 938 */                 deity.addSpell(spell);
/*     */               }
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SpellGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */