/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.shared.constants.AttitudeConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ public final class Spells
/*     */   implements AttitudeConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(Spells.class.getName());
/*     */   
/*  42 */   private static final Map<Integer, Spell> spells = new HashMap<>();
/*  43 */   private static final Map<Integer, Spell> creatureSpells = new HashMap<>();
/*  44 */   private static final Map<Integer, Spell> itemSpells = new HashMap<>();
/*  45 */   private static final Map<Integer, Spell> woundSpells = new HashMap<>();
/*  46 */   private static final Map<Byte, Spell> enchantments = new HashMap<>();
/*  47 */   private static final Map<Integer, Spell> tileSpells = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static Spell SPELL_AURA_SHARED_PAIN = new SharedPain();
/*  52 */   public static Spell SPELL_BEARPAWS = new Bearpaw();
/*  53 */   public static Spell SPELL_BLAZE = new Blaze();
/*  54 */   public static Spell SPELL_BLESS = new Bless();
/*  55 */   public static Spell SPELL_BLESSINGS_OF_THE_DARK = new BlessingDark();
/*  56 */   public static Spell SPELL_BLOODTHIRST = new Bloodthirst();
/*  57 */   public static Spell SPELL_BREAK_ALTAR = new BreakAltar();
/*  58 */   public static Spell SPELL_CHARM_ANIMAL = new CharmAnimal();
/*  59 */   public static Spell SPELL_CIRCLE_OF_CUNNING = new CircleOfCunning();
/*  60 */   public static Spell SPELL_CLEANSE = new Cleanse();
/*  61 */   public static Spell SPELL_CORROSION = new Corrosion();
/*  62 */   public static Spell SPELL_CORRUPT = new Corrupt();
/*  63 */   public static Spell SPELL_COURIER = new Courier();
/*  64 */   public static Spell SPELL_CURE_LIGHT = new CureLight();
/*  65 */   public static Spell SPELL_CURE_MEDIUM = new CureMedium();
/*  66 */   public static Spell SPELL_CURE_SERIOUS = new CureSerious();
/*  67 */   public static Spell SPELL_DARK_MESSENGER = new DarkMessenger();
/*  68 */   public static Spell SPELL_DEMISE_ANIMAL = new DemiseAnimal();
/*  69 */   public static Spell SPELL_DEMISE_HUMAN = new DemiseHuman();
/*  70 */   public static Spell SPELL_DEMISE_LEGENDARY = new DemiseLegendary();
/*  71 */   public static Spell SPELL_DEMISE_MONSTER = new DemiseMonster();
/*  72 */   public static Spell SPELL_DIRT = new Dirt();
/*  73 */   public static Spell SPELL_DISINTEGRATE = new Disintegrate();
/*  74 */   public static Spell SPELL_DISPEL = new Dispel();
/*  75 */   public static Spell SPELL_DOMINATE = new Dominate();
/*  76 */   public static Spell SPELL_DRAIN_HEALTH = new DrainHealth();
/*  77 */   public static Spell SPELL_DRAIN_STAMINA = new DrainStamina();
/*  78 */   public static Spell SPELL_ESSENCE_DRAIN = new EssenceDrain();
/*  79 */   public static Spell SPELL_EXCEL = new Excel();
/*  80 */   public static Spell SPELL_FIREHEART = new FireHeart();
/*  81 */   public static Spell SPELL_FIRE_PILLAR = new FirePillar();
/*  82 */   public static Spell SPELL_FLAMING_AURA = new FlamingAura();
/*  83 */   public static Spell SPELL_FOCUSED_WILL = new FocusedWill();
/*  84 */   public static Spell SPELL_FOREST_GIANT_STRENGTH = new ForestGiant();
/*  85 */   public static Spell SPELL_FRANTIC_CHARGE = new FranticCharge();
/*  86 */   public static Spell SPELL_FROSTBRAND = new Frostbrand();
/*  87 */   public static Spell SPELL_FUNGUS_TRAP = new FungusTrap();
/*  88 */   public static Spell SPELL_GENESIS = new Genesis();
/*  89 */   public static Spell SPELL_GLACIAL = new Glacial();
/*  90 */   public static Spell SPELL_GOAT_SHAPE = new GoatShape();
/*  91 */   public static Spell SPELL_HEAL = new Heal();
/*  92 */   public static Spell SPELL_HELL_STRENGTH = new Hellstrength();
/*  93 */   public static Spell SPELL_HOLY_CROP = new HolyCrop();
/*  94 */   public static Spell SPELL_HUMID_DRIZZLE = new HumidDrizzle();
/*  95 */   public static Spell SPELL_HYPOTHERMIA = new Hypothermia();
/*  96 */   public static Spell SPELL_ICE_PILLAR = new IcePillar();
/*  97 */   public static Spell SPELL_INFERNO = new Inferno();
/*  98 */   public static Spell SPELL_LAND_OF_THE_DEAD = new LandOfTheDead();
/*  99 */   public static Spell SPELL_LIFE_TRANSFER = new LifeTransfer();
/* 100 */   public static Spell SPELL_LIGHT_OF_FO = new LightOfFo();
/* 101 */   public static Spell SPELL_LIGHT_TOKEN = new LightToken();
/* 102 */   public static Spell SPELL_LOCATE_ARTIFACT = new LocateArtifact();
/* 103 */   public static Spell SPELL_LOCATE_SOUL = new LocatePlayer();
/* 104 */   public static Spell SPELL_LURKER_IN_THE_DARK = new LurkerDark();
/* 105 */   public static Spell SPELL_LURKER_IN_THE_DEEP = new LurkerDeep();
/* 106 */   public static Spell SPELL_LURKER_IN_THE_WOODS = new LurkerWoods();
/* 107 */   public static Spell SPELL_MASS_STAMINA = new MassStamina();
/* 108 */   public static Spell SPELL_MEND = new Mend();
/* 109 */   public static Spell SPELL_MIND_STEALER = new MindStealer();
/* 110 */   public static Spell SPELL_MOLE_SENSES = new MoleSenses();
/* 111 */   public static Spell SPELL_MORNING_FOG = new MorningFog();
/* 112 */   public static Spell SPELL_NIMBLENESS = new Nimbleness();
/* 113 */   public static Spell SPELL_NOLOCATE = new Nolocate();
/* 114 */   public static Spell SPELL_OAKSHELL = new OakShell();
/* 115 */   public static Spell SPELL_OPULENCE = new Opulence();
/* 116 */   public static Spell SPELL_PAIN_RAIN = new PainRain();
/* 117 */   public static Spell SPELL_PHANTASMS = new Phantasms();
/* 118 */   public static Spell SPELL_PROTECT_ACID = new ProtectionAcid();
/* 119 */   public static Spell SPELL_PROTECT_FIRE = new ProtectionFire();
/* 120 */   public static Spell SPELL_PROTECT_FROST = new ProtectionFrost();
/* 121 */   public static Spell SPELL_PROTECT_POISON = new ProtectionPoison();
/* 122 */   public static Spell SPELL_PURGE = new Purge();
/* 123 */   public static Spell SPELL_REBIRTH = new Rebirth();
/* 124 */   public static Spell SPELL_REFRESH = new Refresh();
/* 125 */   public static Spell SPELL_REVEAL_CREATURES = new RevealCreatures();
/* 126 */   public static Spell SPELL_REVEAL_SETTLEMENTS = new RevealSettlements();
/* 127 */   public static Spell SPELL_RITE_OF_DEATH = new RiteDeath();
/* 128 */   public static Spell SPELL_RITE_OF_SPRING = new RiteSpring();
/* 129 */   public static Spell SPELL_RITUAL_OF_THE_SUN = new RitualSun();
/* 130 */   public static Spell SPELL_ROTTING_GUT = new RottingGut();
/* 131 */   public static Spell SPELL_ROTTING_TOUCH = new RottingTouch();
/* 132 */   public static Spell SPELL_SCORN_OF_LIBILA = new ScornOfLibila();
/* 133 */   public static Spell SPELL_SHARD_OF_ICE = new ShardOfIce();
/* 134 */   public static Spell SPELL_SIXTH_SENSE = new SixthSense();
/* 135 */   public static Spell SPELL_SMITE = new Smite();
/* 136 */   public static Spell SPELL_STRONGWALL = new StrongWall();
/* 137 */   public static Spell SPELL_SUMMON_SOUL = new SummonSoul();
/* 138 */   public static Spell SPELL_SUNDER = new Sunder();
/* 139 */   public static Spell SPELL_TANGLEWEAVE = new TangleWeave();
/* 140 */   public static Spell SPELL_TENTACLES = new DeepTentacles();
/* 141 */   public static Spell SPELL_TORNADO = new Tornado();
/* 142 */   public static Spell SPELL_TOXIN = new Toxin();
/* 143 */   public static Spell SPELL_TRUEHIT = new TrueHit();
/* 144 */   public static Spell SPELL_VENOM = new Venom();
/* 145 */   public static Spell SPELL_VESSEL = new Vessle();
/* 146 */   public static Spell SPELL_WARD = new Ward();
/* 147 */   public static Spell SPELL_WEAKNESS = new Weakness();
/* 148 */   public static Spell SPELL_WEB_ARMOUR = new WebArmour();
/* 149 */   public static Spell SPELL_WILD_GROWTH = new WildGrowth();
/* 150 */   public static Spell SPELL_WILLOWSPINE = new WillowSpine();
/* 151 */   public static Spell SPELL_WIND_OF_AGES = new WindOfAges();
/* 152 */   public static Spell SPELL_WISDOM_OF_VYNORA = new WisdomVynora();
/* 153 */   public static Spell SPELL_WORM_BRAINS = new WormBrains();
/* 154 */   public static Spell SPELL_WRATH_OF_MAGRANON = new WrathMagranon();
/* 155 */   public static Spell SPELL_ZOMBIE_INFESTATION = new ZombieInfestation();
/*     */ 
/*     */   
/* 158 */   public static Spell SPELL_CONTINUUM = new Continuum();
/* 159 */   public static Spell SPELL_DISEASE = new Disease();
/* 160 */   public static Spell SPELL_FIREBALL = new Fireball();
/* 161 */   public static Spell SPELL_FORECAST = new Forecast();
/* 162 */   public static Spell SPELL_INCINERATE = new Incinerate();
/* 163 */   public static Spell SPELL_KARMA_BOLT = new KarmaBolt();
/* 164 */   public static Spell SPELL_KARMA_MISSILE = new KarmaMissile();
/* 165 */   public static Spell SPELL_KARMA_SLOW = new KarmaSlow();
/* 166 */   public static Spell SPELL_LIGHTNING = new Lightning();
/* 167 */   public static Spell SPELL_MIRRORED_SELF = new MirroredSelf();
/* 168 */   public static Spell SPELL_RUST_MONSTER = new RustMonster();
/* 169 */   public static Spell SPELL_SPROUT_TREES = new SproutTrees();
/* 170 */   public static Spell SPELL_STONESKIN = new StoneSkin();
/* 171 */   public static Spell SPELL_SUMMON = new Summon();
/* 172 */   public static Spell SPELL_SUMMON_SKELETON = new SummonSkeleton();
/* 173 */   public static Spell SPELL_SUMMON_WORG = new SummonWorg();
/* 174 */   public static Spell SPELL_SUMMON_WRAITH = new SummonWraith();
/* 175 */   public static Spell SPELL_TRUESTRIKE = new Truestrike();
/* 176 */   public static Spell SPELL_WALL_OF_FIRE = new WallOfFire();
/* 177 */   public static Spell SPELL_WALL_OF_ICE = new WallOfIce();
/* 178 */   public static Spell SPELL_WALL_OF_STONE = new WallOfStone();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 186 */     addSpell(SPELL_AURA_SHARED_PAIN);
/* 187 */     addSpell(SPELL_BEARPAWS);
/* 188 */     addSpell(SPELL_BLAZE);
/* 189 */     addSpell(SPELL_BLESS);
/* 190 */     addSpell(SPELL_BLESSINGS_OF_THE_DARK);
/* 191 */     addSpell(SPELL_BLOODTHIRST);
/* 192 */     addSpell(SPELL_BREAK_ALTAR);
/* 193 */     addSpell(SPELL_CHARM_ANIMAL);
/* 194 */     addSpell(SPELL_CIRCLE_OF_CUNNING);
/* 195 */     addSpell(SPELL_CLEANSE);
/* 196 */     addSpell(SPELL_CORROSION);
/* 197 */     addSpell(SPELL_CORRUPT);
/* 198 */     addSpell(SPELL_COURIER);
/* 199 */     addSpell(SPELL_CURE_LIGHT);
/* 200 */     addSpell(SPELL_CURE_MEDIUM);
/* 201 */     addSpell(SPELL_CURE_SERIOUS);
/* 202 */     addSpell(SPELL_DARK_MESSENGER);
/* 203 */     addSpell(SPELL_DEMISE_ANIMAL);
/* 204 */     addSpell(SPELL_DEMISE_HUMAN);
/* 205 */     addSpell(SPELL_DEMISE_LEGENDARY);
/* 206 */     addSpell(SPELL_DEMISE_MONSTER);
/* 207 */     addSpell(SPELL_DIRT);
/* 208 */     addSpell(SPELL_DISINTEGRATE);
/* 209 */     addSpell(SPELL_DISPEL);
/* 210 */     addSpell(SPELL_DOMINATE);
/* 211 */     addSpell(SPELL_DRAIN_HEALTH);
/* 212 */     addSpell(SPELL_DRAIN_STAMINA);
/* 213 */     addSpell(SPELL_ESSENCE_DRAIN);
/* 214 */     addSpell(SPELL_EXCEL);
/* 215 */     addSpell(SPELL_FIREHEART);
/* 216 */     addSpell(SPELL_FIRE_PILLAR);
/* 217 */     addSpell(SPELL_FLAMING_AURA);
/* 218 */     addSpell(SPELL_FOCUSED_WILL);
/* 219 */     addSpell(SPELL_FOREST_GIANT_STRENGTH);
/* 220 */     addSpell(SPELL_FRANTIC_CHARGE);
/* 221 */     addSpell(SPELL_FROSTBRAND);
/* 222 */     addSpell(SPELL_FUNGUS_TRAP);
/* 223 */     addSpell(SPELL_GENESIS);
/* 224 */     addSpell(SPELL_GLACIAL);
/* 225 */     addSpell(SPELL_GOAT_SHAPE);
/* 226 */     addSpell(SPELL_HEAL);
/* 227 */     addSpell(SPELL_HELL_STRENGTH);
/* 228 */     addSpell(SPELL_HOLY_CROP);
/* 229 */     addSpell(SPELL_HUMID_DRIZZLE);
/* 230 */     addSpell(SPELL_HYPOTHERMIA);
/* 231 */     addSpell(SPELL_ICE_PILLAR);
/* 232 */     addSpell(SPELL_INFERNO);
/* 233 */     addSpell(SPELL_LAND_OF_THE_DEAD);
/* 234 */     addSpell(SPELL_LIFE_TRANSFER);
/* 235 */     addSpell(SPELL_LIGHT_OF_FO);
/* 236 */     addSpell(SPELL_LIGHT_TOKEN);
/* 237 */     addSpell(SPELL_LOCATE_ARTIFACT);
/* 238 */     addSpell(SPELL_LOCATE_SOUL);
/* 239 */     addSpell(SPELL_LURKER_IN_THE_DARK);
/* 240 */     addSpell(SPELL_LURKER_IN_THE_DEEP);
/* 241 */     addSpell(SPELL_LURKER_IN_THE_WOODS);
/* 242 */     addSpell(SPELL_MASS_STAMINA);
/* 243 */     addSpell(SPELL_MEND);
/* 244 */     addSpell(SPELL_MIND_STEALER);
/* 245 */     addSpell(SPELL_MOLE_SENSES);
/* 246 */     addSpell(SPELL_MORNING_FOG);
/* 247 */     addSpell(SPELL_NIMBLENESS);
/* 248 */     addSpell(SPELL_NOLOCATE);
/* 249 */     addSpell(SPELL_OAKSHELL);
/* 250 */     addSpell(SPELL_OPULENCE);
/* 251 */     addSpell(SPELL_PAIN_RAIN);
/* 252 */     addSpell(SPELL_PHANTASMS);
/* 253 */     addSpell(SPELL_PROTECT_ACID);
/* 254 */     addSpell(SPELL_PROTECT_FIRE);
/* 255 */     addSpell(SPELL_PROTECT_FROST);
/* 256 */     addSpell(SPELL_PROTECT_POISON);
/* 257 */     addSpell(SPELL_PURGE);
/* 258 */     addSpell(SPELL_REBIRTH);
/* 259 */     addSpell(SPELL_REFRESH);
/* 260 */     addSpell(SPELL_REVEAL_CREATURES);
/* 261 */     addSpell(SPELL_REVEAL_SETTLEMENTS);
/* 262 */     addSpell(SPELL_RITE_OF_DEATH);
/* 263 */     addSpell(SPELL_RITE_OF_SPRING);
/* 264 */     addSpell(SPELL_RITUAL_OF_THE_SUN);
/* 265 */     addSpell(SPELL_ROTTING_GUT);
/* 266 */     addSpell(SPELL_ROTTING_TOUCH);
/* 267 */     addSpell(SPELL_SCORN_OF_LIBILA);
/* 268 */     addSpell(SPELL_SHARD_OF_ICE);
/* 269 */     addSpell(SPELL_SIXTH_SENSE);
/* 270 */     addSpell(SPELL_SMITE);
/* 271 */     addSpell(SPELL_STRONGWALL);
/* 272 */     addSpell(SPELL_SUMMON_SOUL);
/* 273 */     addSpell(SPELL_SUNDER);
/* 274 */     addSpell(SPELL_TANGLEWEAVE);
/* 275 */     addSpell(SPELL_TENTACLES);
/* 276 */     addSpell(SPELL_TORNADO);
/* 277 */     addSpell(SPELL_TOXIN);
/* 278 */     addSpell(SPELL_TRUEHIT);
/* 279 */     addSpell(SPELL_VENOM);
/* 280 */     addSpell(SPELL_VESSEL);
/* 281 */     addSpell(SPELL_WARD);
/* 282 */     addSpell(SPELL_WEAKNESS);
/* 283 */     addSpell(SPELL_WEB_ARMOUR);
/* 284 */     addSpell(SPELL_WILD_GROWTH);
/* 285 */     addSpell(SPELL_WILLOWSPINE);
/* 286 */     addSpell(SPELL_WIND_OF_AGES);
/* 287 */     addSpell(SPELL_WISDOM_OF_VYNORA);
/* 288 */     addSpell(SPELL_WORM_BRAINS);
/* 289 */     addSpell(SPELL_WRATH_OF_MAGRANON);
/* 290 */     addSpell(SPELL_ZOMBIE_INFESTATION);
/*     */ 
/*     */     
/* 293 */     addSpell(SPELL_CONTINUUM);
/* 294 */     addSpell(SPELL_DISEASE);
/* 295 */     addSpell(SPELL_FIREBALL);
/* 296 */     addSpell(SPELL_FORECAST);
/* 297 */     addSpell(SPELL_INCINERATE);
/* 298 */     addSpell(SPELL_KARMA_BOLT);
/* 299 */     addSpell(SPELL_KARMA_MISSILE);
/* 300 */     addSpell(SPELL_KARMA_SLOW);
/* 301 */     addSpell(SPELL_LIGHTNING);
/* 302 */     addSpell(SPELL_MIRRORED_SELF);
/* 303 */     addSpell(SPELL_RUST_MONSTER);
/* 304 */     addSpell(SPELL_SPROUT_TREES);
/* 305 */     addSpell(SPELL_STONESKIN);
/* 306 */     addSpell(SPELL_SUMMON);
/* 307 */     addSpell(SPELL_SUMMON_SKELETON);
/* 308 */     addSpell(SPELL_SUMMON_WORG);
/* 309 */     addSpell(SPELL_SUMMON_WRAITH);
/* 310 */     addSpell(SPELL_TRUESTRIKE);
/* 311 */     addSpell(SPELL_WALL_OF_FIRE);
/* 312 */     addSpell(SPELL_WALL_OF_ICE);
/* 313 */     addSpell(SPELL_WALL_OF_STONE);
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
/*     */ 
/*     */   
/*     */   public static void addSpell(Spell spell) {
/* 328 */     spells.put(Integer.valueOf(spell.getNumber()), spell);
/*     */     
/* 330 */     if (spell.isTargetCreature())
/*     */     {
/* 332 */       creatureSpells.put(Integer.valueOf(spell.getNumber()), spell);
/*     */     }
/* 334 */     if (spell.isTargetAnyItem())
/*     */     {
/* 336 */       itemSpells.put(Integer.valueOf(spell.getNumber()), spell);
/*     */     }
/* 338 */     if (spell.isTargetWound())
/*     */     {
/* 340 */       woundSpells.put(Integer.valueOf(spell.getNumber()), spell);
/*     */     }
/* 342 */     if (spell.isTargetTile())
/*     */     {
/* 344 */       tileSpells.put(Integer.valueOf(spell.getNumber()), spell);
/*     */     }
/*     */ 
/*     */     
/* 348 */     if (spell.getEnchantment() != 0)
/*     */     {
/* 350 */       enchantments.put(Byte.valueOf(spell.getEnchantment()), spell);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Spell getSpell(int number) {
/* 356 */     return spells.get(Integer.valueOf(number));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Spell[] getAllSpells() {
/* 361 */     return (Spell[])spells.values().toArray((Object[])new Spell[spells.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Spell getEnchantment(byte num) {
/* 366 */     return enchantments.get(Byte.valueOf(num));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Spell[] getSpellsTargettingItems() {
/* 371 */     Set<Spell> toReturn = new HashSet<>();
/* 372 */     for (Iterator<Integer> it = itemSpells.keySet().iterator(); it.hasNext();)
/*     */     {
/* 374 */       toReturn.add(itemSpells.get(it.next()));
/*     */     }
/* 376 */     return toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Spell[] getSpellsEnchantingItems() {
/* 381 */     Set<Spell> toReturn = new HashSet<>();
/* 382 */     for (Iterator<Integer> it = itemSpells.keySet().iterator(); it.hasNext(); ) {
/*     */       
/* 384 */       Spell spell = itemSpells.get(it.next());
/* 385 */       if (spell.getEnchantment() > 0)
/* 386 */         toReturn.add(spell); 
/*     */     } 
/* 388 */     return toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Spells.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */