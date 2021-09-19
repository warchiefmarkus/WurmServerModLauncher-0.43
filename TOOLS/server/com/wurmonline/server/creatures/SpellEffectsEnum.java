/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum SpellEffectsEnum
/*     */ {
/*  11 */   NONE(0L, "", -1, (byte)0, (byte)0),
/*  12 */   CR_BONUS(-12335499L, "Potion", 0, (byte)9, (byte)0),
/*  13 */   FARWALKER(-12335498L, "Farwalker", 1, (byte)9, (byte)0),
/*  14 */   DISEASE(-12335497L, "Disease", 2, (byte)1, (byte)1),
/*  15 */   CHAMP_POINTS(-12335496L, "Champion points", 3, false, (byte)1, (byte)0),
/*     */   
/*  17 */   ENEMY(-12335495L, "Enemy", 4, (byte)5, (byte)1, false),
/*  18 */   LINKS(-12335494L, "Links", 5, (byte)5, (byte)0, false),
/*  19 */   FAITHBONUS(-12335493L, "Faith bonus", 6, (byte)5, (byte)0, false),
/*     */   
/*  21 */   WOUNDMOVE(-12335492L, "Hurting", 7, (byte)1, (byte)1, false),
/*  22 */   DEITY_FAVORGAIN(-12335491L, "Spirit Favor", 8, (byte)6, (byte)0, false),
/*     */   
/*  24 */   DEITY_STAMINAGAIN(-12335490L, "Spirit Stamina", 9, (byte)6, (byte)0, false),
/*     */   
/*  26 */   DEITY_CRBONUS(-12335489L, "Spirit Fervor", 10, (byte)6, (byte)0, false),
/*     */   
/*  28 */   DEITY_MOVEBONUS(-12335488L, "Spirit Speed", 11, (byte)6, (byte)0, false),
/*     */   
/*  30 */   LOVE_EFFECT(-12335487L, "Love effect", 12, (byte)1, (byte)0),
/*  31 */   ROD_BEGUILING_EFFECT(-12335486L, "Beguiling", 13, (byte)6, (byte)0, false),
/*     */   
/*  33 */   FINGER_FO_EFFECT(-12335485L, "Finger of Fo", 14, (byte)6, (byte)0, false),
/*     */   
/*  35 */   CROWN_MAGRANON_EFFECT(-12335484L, "Crown of Might", 15, (byte)6, (byte)0, false),
/*     */   
/*  37 */   ORB_DOOM_EFFECT(-12335483L, "Orb Of Doom", 16, (byte)6, (byte)1),
/*  38 */   KARMA(-12335481L, "Karma", 18, false, (byte)1, (byte)0),
/*  39 */   SCENARIOKARMA(-12335480L, "Scenario Points", 19, false, (byte)1, (byte)0),
/*     */   
/*  41 */   FIRE_RESIST(-12335479L, "Fire Toughness", 20, (byte)1, (byte)0, false),
/*     */   
/*  43 */   COLD_RESIST(-12335478L, "Cold Toughness", 21, (byte)1, (byte)0, false),
/*     */   
/*  45 */   DISEASE_RESIST(-12335477L, "Disease Toughness", 22, (byte)1, (byte)0, false),
/*     */   
/*  47 */   PHYSICAL_RESIST(-12335476L, "Physical Damage Toughness", 23, (byte)1, (byte)0, false),
/*     */   
/*  49 */   PIERCE_RESIST(-12335475L, "Pierce Insensitive Skin", 24, (byte)1, (byte)0, false),
/*     */   
/*  51 */   SLASH_RESIST(-12335474L, "Slash Damage Toughness", 25, (byte)1, (byte)0, false),
/*     */   
/*  53 */   CRUSH_RESIST(-12335473L, "Crush Damage Toughness", 26, (byte)1, (byte)0, false),
/*     */   
/*  55 */   BITE_RESIST(-12335472L, "Bite Damage Toughness", 27, (byte)1, (byte)0, false),
/*     */   
/*  57 */   POISON_RESIST(-12335471L, "Poison Resistance", 28, (byte)1, (byte)0, false),
/*     */   
/*  59 */   WATER_RESIST(-12335470L, "Water Damage Resistance", 29, (byte)1, (byte)0, false),
/*     */   
/*  61 */   ACID_RESIST(-12335469L, "Acid Resistant Skin", 30, (byte)1, (byte)0, false),
/*     */   
/*  63 */   INTERNAL_RESIST(-12335468L, "Internal Damage Toughness", 31, (byte)1, (byte)0, false),
/*     */   
/*  65 */   FIRE_VULNERABILITY(-12335467L, "Fire Vulnerability", 32, (byte)1, (byte)1, false),
/*     */   
/*  67 */   COLD_VULNERABILITY(-12335466L, "Cold Vulnerability", 33, (byte)1, (byte)1, false),
/*     */   
/*  69 */   DISEASE_VULNERABILITY(-12335465L, "Disease Vulnerability", 34, (byte)1, (byte)1, false),
/*     */   
/*  71 */   PHYSICAL_VULNERABILITY(-12335464L, "Physical Damage Sensitivity", 35, (byte)1, (byte)1, false),
/*     */   
/*  73 */   PIERCE_VULNERABILITY(-12335463L, "Pierce Damage Sensitivity", 36, (byte)1, (byte)1, false),
/*     */   
/*  75 */   SLASH_VULNERABILITY(-12335462L, "Slash Damage Sensitivity", 37, (byte)1, (byte)1, false),
/*     */   
/*  77 */   CRUSH_VULNERABILITY(-12335461L, "Crush Damage Sensitivity", 38, (byte)1, (byte)1, false),
/*     */   
/*  79 */   BITE_VULNERABILITY(-12335460L, "Bite Damage Sensitivity", 39, (byte)1, (byte)1, false),
/*     */   
/*  81 */   POISON_VULNERABILITY(-12335459L, "Poison Sensitivity", 40, (byte)1, (byte)1, false),
/*     */   
/*  83 */   WATER_VULNERABILITY(-12335458L, "Water Vulnerability", 41, (byte)1, (byte)1, false),
/*     */   
/*  85 */   ACID_VULNERABILITY(-12335457L, "Acid Sensitivity", 42, (byte)1, (byte)1, false),
/*     */   
/*  87 */   INTERNAL_VULNERABILITY(-12335456L, "Internal Damage Sensitivity", 43, (byte)1, (byte)1, false),
/*     */   
/*  89 */   ILLUSION(0L, "Illusion", 44, (byte)9, (byte)0),
/*  90 */   FAVOR_OVERHEATED(52L, "Overheated", 44, (byte)52, (byte)1),
/*     */   
/*  92 */   POISON(0L, "Poison", 45, (byte)0, (byte)1),
/*  93 */   DETECT_INVIS(-12335454L, "Detect Invisible", 46, (byte)1, (byte)0),
/*     */   
/*  95 */   STUNNED(-12335453L, "Stunned", 47, (byte)1, (byte)1),
/*  96 */   HUNTED(0L, "Hunted", 48, (byte)1, (byte)1),
/*  97 */   NEWBIE_HEALTH(0L, "Newbie healing buff", 49, (byte)1, (byte)0),
/*  98 */   NEWBIE_AGGRO_RANGE(0L, "Newbie agg range buff", 50, (byte)1, (byte)0),
/*     */   
/* 100 */   NEWBIE_FOOD(0L, "Newbie food and drink buff", 51, (byte)1, (byte)0),
/*     */   
/* 102 */   HATE_FEAR_EFFECT(-12335452L, "Fear effect", 52, (byte)1, (byte)0),
/* 103 */   HATE_DOUBLE_WAR(-12335451L, "Rage", 53, (byte)1, (byte)0),
/* 104 */   HATE_DOUBLE_STRUCT(-12335450L, "Double structure damage", 54, (byte)1, (byte)0),
/*     */   
/* 106 */   POWER_NO_ELEMENTAL(-12335449L, "Elemental immunity", 55, (byte)1, (byte)0),
/*     */   
/* 108 */   POWER_IGNORE_TRAPS(-12335448L, "Trap immunity", 56, (byte)1, (byte)0),
/*     */   
/* 110 */   LOVE_HEALING_HANDS(-12335447L, "Healing hands", 57, (byte)1, (byte)0, false),
/*     */   
/* 112 */   HATE_SPELL_IMMUNITY(-12335446L, "Spell immunity", 58, (byte)1, (byte)0),
/*     */   
/* 114 */   POWER_USES_LESS_STAMINA(-12335445L, "Stamina of the Vibrant Light", 59, (byte)1, (byte)0),
/*     */   
/* 116 */   KNOWLEDGE_NO_DECAY(-12335444L, "No skill loss", 60, (byte)1, (byte)0, false),
/*     */   
/* 118 */   KNOWLEDGE_INCREASED_SKILL_GAIN(-12335443L, "Increased skill gain", 61, (byte)1, (byte)0),
/*     */   
/* 120 */   INSANITY_SHIELD_GONE(-12335442L, "Shield of the Gone", 62, (byte)1, (byte)0),
/*     */   
/* 122 */   SPELL_FOREST_GIANT(0L, "Forest Giant Strength", 63, (byte)1, (byte)0),
/*     */   
/* 124 */   SPELL_BEARPAWS(0L, "Bearpaws", 64, (byte)1, (byte)0),
/* 125 */   SPELL_MORNING_FOG(0L, "Morning Fog", 65, (byte)1, (byte)0),
/* 126 */   SPELL_NOLOCATE(0L, "Nolocate", 66, (byte)1, (byte)0),
/* 127 */   SPELL_OAKSHELL(0L, "Oakshell", 67, (byte)1, (byte)0),
/* 128 */   SPELL_WILLOWSPINE(0L, "Willowspine", 68, (byte)1, (byte)0),
/* 129 */   SPELL_SIXTH_SENSE(0L, "Sixth sense", 69, (byte)1, (byte)0),
/* 130 */   SPELL_HELL_STRENGTH(0L, "Hell strength", 70, (byte)1, (byte)0),
/* 131 */   SPELL_FRANTIC_CHARGE(0L, "Frantic charge", 71, (byte)1, (byte)0),
/* 132 */   SPELL_GOAT_SHAPE(0L, "Goat shape", 72, (byte)1, (byte)0),
/* 133 */   SPELL_TRUE_HIT(0L, "Truehit", 73, (byte)1, (byte)0),
/* 134 */   SPELL_THORNSHELL(0L, "Thornshell", 74, (byte)1, (byte)0),
/* 135 */   SPELL_KARMA_STONESKIN(0L, "Stoneskin", 75, (byte)1, (byte)0),
/* 136 */   SPELL_KARMA_RUST_MONSTER(0L, "Rust Monster", 76, (byte)1, (byte)0),
/* 137 */   SPELL_KARMA_RIVER(0L, "Karma Drain", 77, (byte)1, (byte)1),
/* 138 */   SPELL_KARMA_CONTINUUM(0L, "Continuum", 78, (byte)1, (byte)0),
/* 139 */   SPELL_EXCEL(0L, "Excel", 79, (byte)1, (byte)0),
/* 140 */   SPELL_TRUE_STRIKE(0L, "True Strike", 80, (byte)1, (byte)0),
/* 141 */   RES_TENTACLES(-12335441L, "Res Tentacles", 81, (byte)1, (byte)0),
/* 142 */   RES_DISPEL(-12335440L, "Res Dispel", 82, (byte)1, (byte)0),
/* 143 */   RES_DRAINHEALTH(-12335439L, "Res Drain Health", 83, (byte)1, (byte)0),
/*     */   
/* 145 */   RES_DRAINSTAMINA(-12335438L, "Res Drain Stamina", 84, (byte)1, (byte)0),
/*     */   
/* 147 */   RES_FIREHEART(-12335437L, "Res Fireheart", 85, (byte)1, (byte)0),
/* 148 */   RES_FIREPILLAR(-12335436L, "Res Firepillar", 86, (byte)1, (byte)0),
/*     */   
/* 150 */   RES_FUNGUSTRAP(-12335435L, "Res Fungus Trap", 87, (byte)1, (byte)0),
/*     */   
/* 152 */   RES_HEAL(-12335434L, "Res Heal", 88, (byte)1, (byte)0),
/* 153 */   RES_ICEPILLAR(-12335433L, "Res Icepillar", 89, (byte)1, (byte)0),
/* 154 */   RES_LIGHTOFFO(-12335432L, "Res Light Of Fo", 90, (byte)1, (byte)0),
/*     */   
/* 156 */   RES_PAINRAIN(-12335431L, "Res Pain Rain", 91, (byte)1, (byte)0),
/* 157 */   RES_ROTTINGGUT(-12335430L, "Res Rotting Gut", 92, (byte)1, (byte)0),
/*     */   
/* 159 */   RES_SCORNLIBILA(-12335429L, "Res Scorn Of Libila", 93, (byte)1, (byte)0),
/*     */   
/* 161 */   RES_SHARDOFICE(-12335428L, "Res Shard Of Ice", 94, (byte)1, (byte)1),
/* 162 */   RES_SMITE(-12335427L, "Res Smite", 95, (byte)1, (byte)0),
/* 163 */   RES_WEAKNESS(-12335426L, "Res Weakness", 96, (byte)1, (byte)0),
/* 164 */   RES_GENERIC(-12335425L, "Res Generic", 97, (byte)1, (byte)0),
/* 165 */   ARMOUR_LIMIT_HEAVY(-12335424L, "Armour Limit -30.000002%", 98, (byte)1, (byte)1),
/*     */   
/* 167 */   ARMOUR_LIMIT_MEDIUM(-12335230L, "Armour Limit -15.000001%", 99, (byte)1, (byte)0),
/*     */   
/* 169 */   ARMOUR_LIMIT_LIGHT(-12335422L, "Armour Bonus 0.0%", 100, (byte)1, (byte)0),
/*     */   
/* 171 */   ARMOUR_LIMIT_NONE(-12335421L, "Armour Bonus 30.000002%", 101, (byte)1, (byte)0),
/*     */   
/* 173 */   SPELL_TANGLEWEAVE(-12335420L, "Tangle Weave", 102, (byte)1, (byte)1),
/* 174 */   SPELL_KARMA_INCINERATION(-12335419L, "Incineration", 103, (byte)1, (byte)1),
/*     */   
/* 176 */   ITEM_COTTON_CRUSHING(-12335418L, "Cloth armour pieces glance bonus versus crushing", 104, (byte)10, (byte)0, false),
/*     */   
/* 178 */   ITEM_COTTON_SLASHING(-12335417L, "Cloth armour pieces glance bonus versus slashing", 105, (byte)10, (byte)0, false),
/*     */   
/* 180 */   ITEM_NONE_BASHING(-12335416L, "Dodge bonus vs bashing wearing no armour", 106, (byte)10, (byte)0, false),
/*     */   
/* 182 */   ITEM_LEATHER_TWOHANDED(-12335415L, "Leather armour glance bonus vs twohanded.", 107, (byte)10, (byte)0, false),
/*     */   
/* 184 */   ITEM_STUDDED_TWOHANDED(-12335414L, "Studded leather armour glance bonus vs twohanded.", 108, (byte)10, (byte)0, false),
/*     */   
/* 186 */   ITEM_LIGHT_BASHING(-12335413L, "Dodge bonus vs bashing wearing light armour", 109, (byte)10, (byte)0, false),
/*     */   
/* 188 */   ITEM_MEDIUM_BASHING(-12335412L, "Dodge bonus vs bashing wearing medium armour", 110, (byte)10, (byte)0, false),
/*     */   
/* 190 */   ITEM_COTTON_SLASHDAM(-12335411L, "Cloth armour piece damage reduction vs slash combat damage", 111, (byte)10, (byte)0, false),
/*     */   
/* 192 */   ITEM_FACEDAM(-12335410L, "Face damage protection", 112, (byte)10, (byte)0, false),
/*     */   
/* 194 */   ITEM_LEATHER_CRUSHDAM(-12335409L, "Leather armour combat damage reduction vs crush damage", 113, (byte)10, (byte)0, false),
/*     */   
/* 196 */   ITEM_CHAIN_SLASHDAM(-12335408L, "Chain armour combat damage reduction vs slash damage", 114, (byte)10, (byte)0, false),
/*     */   
/* 198 */   ITEM_CHAIN_PIERCEDAM(-12335407L, "Chain armour combat damage reduction vs pierce damage", 115, (byte)10, (byte)0, false),
/*     */   
/* 200 */   ITEM_AREA_SPELL(-12335406L, "Area spell damage increase", 116, (byte)10, (byte)0, false),
/*     */   
/* 202 */   ITEM_ENCHANT_DAMREDUCT(-12335405L, "Enchant damage combat damage reduction reduction", 117, (byte)10, (byte)0, false),
/*     */   
/* 204 */   ITEM_AREASPELL_DAMREDUCT(-12335404L, "Area spell damage reduction", 118, (byte)10, (byte)0, false),
/*     */   
/* 206 */   ITEM_MEDLIGHT_DAMINCREASE(-12335403L, "Weapon damage increase while wearing medium armour or lighter.", 119, (byte)10, (byte)0, false),
/*     */   
/* 208 */   ITEM_HEAVY_ARCHERY(-12335402L, "Archery fail reduction when wearing heavy or medium armour.", 120, (byte)10, (byte)0, false),
/*     */   
/* 210 */   ITEM_RING_STAMINA(-12335401L, "Stamina reduction bonus", 121, (byte)10, (byte)0, false),
/*     */   
/* 212 */   ITEM_RING_DODGE(-12335400L, "Dodge bonus", 122, (byte)10, (byte)0, false),
/*     */   
/* 214 */   ITEM_RING_CR(-12335399L, "Combat rating bonus", 123, (byte)10, (byte)0, false),
/*     */   
/* 216 */   ITEM_RING_SPELLRESIST(-12335398L, "Spell resist bonus", 124, (byte)10, (byte)0, false),
/*     */   
/* 218 */   ITEM_RING_HEALING(-12335397L, "Healing bonus", 125, (byte)10, (byte)0, false),
/*     */ 
/*     */   
/* 221 */   ITEM_RING_SKILLGAIN(-12335396L, "Skillgain bonus", 126, (byte)10, (byte)0, false),
/*     */   
/* 223 */   ITEM_RING_SWIMMING(-12335395L, "Drown damage reduction.", 127, (byte)10, (byte)0, false),
/*     */   
/* 225 */   ITEM_RING_STEALTH(-12335394L, "Stealth bonus", 128, (byte)10, (byte)0, false),
/*     */   
/* 227 */   ITEM_RING_DETECTION(-12335393L, "Stealth detection", 129, (byte)10, (byte)0, false),
/*     */   
/* 229 */   ITEM_BRACELET_CRUSH(-12335392L, "Parry wielding crushing", 130, (byte)10, (byte)0, false),
/*     */   
/* 231 */   ITEM_BRACELET_TWOHANDED(-12335391L, "Parry wielding twohanded", 131, (byte)10, (byte)0, false),
/*     */   
/* 233 */   ITEM_BRACELET_PIERCEDAM(-12335390L, "Pierce damage", 132, (byte)10, (byte)0, false),
/*     */   
/* 235 */   ITEM_BRACELET_POLEARMDAM(-12335389L, "Polearm damage", 133, (byte)10, (byte)0, false),
/*     */   
/* 237 */   ITEM_BRACELET_ENCHANTDAM(-12335388L, "Enchant damage", 134, (byte)10, (byte)0, false),
/*     */ 
/*     */   
/* 240 */   ITEM_NECKLACE_SKILLEFF(-12335387L, "Skill efficiency", 135, (byte)10, (byte)0, false),
/*     */   
/* 242 */   ITEM_NECKLACE_SKILLMAX(-12335386L, "Item improvement skill max", 136, (byte)10, (byte)0, false),
/*     */   
/* 244 */   ITEM_NECKLACE_HURTING(-12335385L, "Hurting time reduction", 137, (byte)10, (byte)0, false),
/*     */   
/* 246 */   ITEM_NECKLACE_FOCUS(-12335384L, "Focus chance", 138, (byte)10, (byte)0, false),
/*     */   
/* 248 */   ITEM_NECKLACE_REPLENISH(-12335383L, "Replenishment", 139, (byte)10, (byte)0, false),
/*     */   
/* 250 */   ITEM_DEBUFF_EXHAUSTION(-12335382L, "Exhaustion", 140, (byte)10, (byte)1),
/*     */   
/* 252 */   ITEM_DEBUFF_CLUMSINESS(-12335381L, "Clumsiness", 141, (byte)10, (byte)1),
/*     */   
/* 254 */   ITEM_DEBUFF_VULNERABILITY(-12335380L, "Vulnerability", 142, (byte)10, (byte)1),
/*     */ 
/*     */   
/* 257 */   DEATH_PROTECTION(-12335454L, "Death protection", 143, (byte)1, (byte)0),
/* 258 */   SKILL_TIMED_AFFINITY(0L, "Timed Affinity", 144, (byte)1, (byte)0),
/* 259 */   RES_STUNNED(-12335379L, "Res Stunned", 145, (byte)1, (byte)0),
/*     */   
/* 261 */   RES_TORNADO(-12335378L, "Res Tornado", 146, (byte)1, (byte)0),
/*     */   
/* 263 */   RES_WORMBRAINS(-12335377L, "Res Worm Brains", 147, (byte)1, (byte)0),
/*     */   
/* 265 */   RES_SHARED(-12335376L, "Res Shared", 148, (byte)1, (byte)0),
/*     */   
/* 267 */   RES_WRATH_OF_MAGRANON(-12335375L, "Res Wrath of Magranon", 149, (byte)1, (byte)0);
/*     */   private final long id;
/*     */   private final int typeId;
/*     */   private final byte effectType;
/*     */   private final byte influence;
/*     */   private final String name;
/*     */   private final boolean sendToBuffBar;
/*     */   private final boolean sendDuration;
/*     */   private static SpellEffectsEnum[] effects;
/*     */   
/*     */   static {
/* 278 */     effects = values();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SpellEffectsEnum(long id, String name, int typeId, byte effectType, byte influence, boolean sendDuration) {
/* 284 */     this.id = id;
/* 285 */     this.name = name;
/* 286 */     this.typeId = typeId;
/* 287 */     this.effectType = effectType;
/* 288 */     this.influence = influence;
/* 289 */     this.sendDuration = sendDuration;
/* 290 */     this.sendToBuffBar = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   SpellEffectsEnum(long id, String name, int typeId, byte effectType, byte influence) {
/* 296 */     this.id = id;
/* 297 */     this.name = name;
/* 298 */     this.typeId = typeId;
/* 299 */     this.effectType = effectType;
/* 300 */     this.influence = influence;
/* 301 */     this.sendDuration = true;
/* 302 */     this.sendToBuffBar = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SpellEffectsEnum(long id, String name, int typeId, boolean sendToBuffBar, byte effectType, byte influence) {
/* 309 */     this.id = id;
/* 310 */     this.name = name;
/* 311 */     this.typeId = typeId;
/* 312 */     this.sendToBuffBar = sendToBuffBar;
/* 313 */     this.effectType = effectType;
/* 314 */     this.influence = influence;
/* 315 */     this.sendDuration = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SpellEffectsEnum getResistanceForSpell(short spellActionId) {
/* 320 */     switch (spellActionId) {
/*     */       
/*     */       case 418:
/* 323 */         return RES_TENTACLES;
/*     */       case 450:
/* 325 */         return RES_DISPEL;
/*     */       case 255:
/* 327 */         return RES_DRAINHEALTH;
/*     */       case 254:
/* 329 */         return RES_DRAINSTAMINA;
/*     */       case 424:
/* 331 */         return RES_FIREHEART;
/*     */       case 420:
/* 333 */         return RES_FIREPILLAR;
/*     */       case 433:
/* 335 */         return RES_FUNGUSTRAP;
/*     */       case 249:
/* 337 */         return RES_HEAL;
/*     */       case 414:
/* 339 */         return RES_ICEPILLAR;
/*     */       case 438:
/* 341 */         return RES_LIGHTOFFO;
/*     */       case 432:
/* 343 */         return RES_PAINRAIN;
/*     */       case 428:
/* 345 */         return RES_ROTTINGGUT;
/*     */       case 448:
/* 347 */         return RES_SCORNLIBILA;
/*     */       case 485:
/* 349 */         return RES_SHARDOFICE;
/*     */       case 252:
/* 351 */         return RES_SMITE;
/*     */       case 429:
/* 353 */         return RES_WEAKNESS;
/*     */     } 
/* 355 */     if (spellActionId == STUNNED.getTypeId())
/* 356 */       return RES_STUNNED; 
/* 357 */     return RES_GENERIC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte getDebuffForEnum(SpellEffectsEnum checkedEnum) {
/* 363 */     switch (checkedEnum) {
/*     */       
/*     */       case ITEM_DEBUFF_EXHAUSTION:
/* 366 */         return 95;
/*     */       case ITEM_DEBUFF_VULNERABILITY:
/* 368 */         return 96;
/*     */       case ITEM_DEBUFF_CLUMSINESS:
/* 370 */         return 97;
/*     */     } 
/* 372 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long createId(long modifier) {
/* 378 */     return this.id + modifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getId() {
/* 383 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getTypeId() {
/* 388 */     return this.typeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 393 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSendToBuffBar() {
/* 398 */     return this.sendToBuffBar;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getEffectType() {
/* 403 */     return this.effectType;
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte getInfluence() {
/* 408 */     return this.influence;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSendDuration() {
/* 413 */     return this.sendDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SpellEffectsEnum getEnumByName(String name) {
/* 418 */     for (int i = 0; i < effects.length; i++) {
/*     */       
/* 420 */       if (effects[i].getName().equalsIgnoreCase(name))
/* 421 */         return effects[i]; 
/*     */     } 
/* 423 */     return NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SpellEffectsEnum getEnumById(long id) {
/* 428 */     for (int i = 0; i < effects.length; i++) {
/*     */       
/* 430 */       if (effects[i].getId() == id)
/* 431 */         return effects[i]; 
/*     */     } 
/* 433 */     return NONE;
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
/*     */   public static final SpellEffectsEnum getEnumForItemTemplateId(int templateId, int extraInfo) {
/* 446 */     switch (templateId) {
/*     */       
/*     */       case 1049:
/* 449 */         return ITEM_COTTON_CRUSHING;
/*     */       case 1050:
/* 451 */         return ITEM_COTTON_SLASHING;
/*     */       case 1051:
/* 453 */         return ITEM_NONE_BASHING;
/*     */       case 1052:
/* 455 */         return ITEM_LEATHER_TWOHANDED;
/*     */       case 1053:
/*     */       case 1066:
/* 458 */         return ITEM_STUDDED_TWOHANDED;
/*     */       case 1054:
/* 460 */         return ITEM_LIGHT_BASHING;
/*     */       case 1055:
/*     */       case 1092:
/* 463 */         return ITEM_MEDIUM_BASHING;
/*     */       case 1056:
/*     */       case 1093:
/* 466 */         return ITEM_COTTON_SLASHDAM;
/*     */       case 1057:
/*     */       case 1094:
/* 469 */         return ITEM_FACEDAM;
/*     */       case 1058:
/*     */       case 1095:
/* 472 */         return ITEM_LEATHER_CRUSHDAM;
/*     */       case 1059:
/* 474 */         return ITEM_CHAIN_SLASHDAM;
/*     */       case 1060:
/* 476 */         return ITEM_CHAIN_PIERCEDAM;
/*     */       case 1061:
/* 478 */         return ITEM_AREA_SPELL;
/*     */       case 1062:
/* 480 */         return ITEM_ENCHANT_DAMREDUCT;
/*     */       case 1063:
/* 482 */         return ITEM_AREASPELL_DAMREDUCT;
/*     */       case 1064:
/* 484 */         return ITEM_MEDLIGHT_DAMINCREASE;
/*     */       case 1065:
/* 486 */         return ITEM_HEAVY_ARCHERY;
/*     */       
/*     */       case 1076:
/* 489 */         switch (extraInfo) {
/*     */           
/*     */           case 1:
/* 492 */             return ITEM_RING_STAMINA;
/*     */           case 2:
/* 494 */             return ITEM_RING_DODGE;
/*     */           case 3:
/* 496 */             return ITEM_RING_CR;
/*     */           case 4:
/* 498 */             return ITEM_RING_SPELLRESIST;
/*     */           case 5:
/* 500 */             return ITEM_RING_HEALING;
/*     */         } 
/*     */       
/*     */       case 1077:
/* 504 */         return ITEM_RING_SKILLGAIN;
/*     */       case 1078:
/* 506 */         return ITEM_RING_SWIMMING;
/*     */       case 1079:
/* 508 */         return ITEM_RING_STEALTH;
/*     */       case 1080:
/* 510 */         return ITEM_RING_DETECTION;
/*     */       
/*     */       case 1081:
/* 513 */         return ITEM_BRACELET_CRUSH;
/*     */       case 1082:
/* 515 */         return ITEM_BRACELET_TWOHANDED;
/*     */       case 1083:
/* 517 */         return ITEM_BRACELET_PIERCEDAM;
/*     */       case 1084:
/* 519 */         return ITEM_BRACELET_POLEARMDAM;
/*     */       case 1085:
/* 521 */         return ITEM_BRACELET_ENCHANTDAM;
/*     */       
/*     */       case 1086:
/* 524 */         return ITEM_NECKLACE_SKILLEFF;
/*     */       case 1087:
/* 526 */         return ITEM_NECKLACE_SKILLMAX;
/*     */       case 1088:
/* 528 */         return ITEM_NECKLACE_HURTING;
/*     */       case 1089:
/* 530 */         return ITEM_NECKLACE_FOCUS;
/*     */       case 1090:
/* 532 */         return ITEM_NECKLACE_REPLENISH;
/*     */     } 
/* 534 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final SpellEffectsEnum getDebuffEnumForItemTemplateId(int templateId, int extraInfo) {
/* 541 */     switch (templateId) {
/*     */       
/*     */       case 1076:
/* 544 */         switch (extraInfo) {
/*     */           
/*     */           case 1:
/* 547 */             return ITEM_DEBUFF_VULNERABILITY;
/*     */           case 2:
/* 549 */             return ITEM_DEBUFF_CLUMSINESS;
/*     */           case 3:
/* 551 */             return ITEM_DEBUFF_CLUMSINESS;
/*     */           case 4:
/* 553 */             return ITEM_DEBUFF_EXHAUSTION;
/*     */           case 5:
/* 555 */             return ITEM_DEBUFF_VULNERABILITY;
/*     */         } 
/*     */       
/*     */       case 1077:
/* 559 */         return ITEM_DEBUFF_CLUMSINESS;
/*     */       case 1078:
/* 561 */         return ITEM_DEBUFF_EXHAUSTION;
/*     */       case 1079:
/* 563 */         return ITEM_DEBUFF_VULNERABILITY;
/*     */       case 1080:
/* 565 */         return ITEM_DEBUFF_VULNERABILITY;
/*     */       
/*     */       case 1081:
/* 568 */         return ITEM_DEBUFF_EXHAUSTION;
/*     */       case 1082:
/* 570 */         return ITEM_DEBUFF_EXHAUSTION;
/*     */       case 1083:
/* 572 */         return ITEM_DEBUFF_CLUMSINESS;
/*     */       case 1084:
/* 574 */         return ITEM_DEBUFF_VULNERABILITY;
/*     */       case 1085:
/* 576 */         return ITEM_DEBUFF_VULNERABILITY;
/*     */       
/*     */       case 1086:
/* 579 */         return ITEM_DEBUFF_CLUMSINESS;
/*     */       case 1087:
/* 581 */         return ITEM_DEBUFF_CLUMSINESS;
/*     */       case 1088:
/* 583 */         return ITEM_DEBUFF_VULNERABILITY;
/*     */       case 1089:
/* 585 */         return ITEM_DEBUFF_EXHAUSTION;
/*     */       case 1090:
/* 587 */         return ITEM_DEBUFF_EXHAUSTION;
/*     */     } 
/* 589 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\SpellEffectsEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */