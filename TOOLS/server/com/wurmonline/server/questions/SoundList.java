/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Properties;
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
/*     */ public class SoundList
/*     */   extends Question
/*     */ {
/*  30 */   private MissionManager root = null;
/*  31 */   private String selected = "";
/*  32 */   private int sortBy = 1;
/*  33 */   private int showCat = 0;
/*     */   private soundString[] soundStrings;
/*     */   private String[] catStrings;
/*  36 */   private int nextId = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoundList(Creature _responder, String _title, String _question) {
/*  46 */     super(_responder, _title, _question, 150, -10L);
/*     */     
/*  48 */     loadSoundStrings();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadSoundStrings() {
/*  53 */     LinkedList<soundString> soundsList = new LinkedList<>();
/*  54 */     soundsList.add(new soundString("Ambient", "AMBIENT_CRICKETSDAY_SND", "sound.ambient.day.crickets"));
/*  55 */     soundsList.add(new soundString("Ambient", "AMBIENT_CRICKETSNIGHT_SND", "sound.ambient.night.crickets"));
/*  56 */     soundsList.add(new soundString("Ambient", "AMBIENT_LEAFRUSTLE_SND", "sound.forest.leafrustle"));
/*  57 */     soundsList.add(new soundString("Ambient", "AMBIENT_WINDWEAK_SND", "sound.ambient.wind.weak"));
/*  58 */     soundsList.add(new soundString("Ambient", "AMBIENT_WINDSTRONG_SND", "sound.ambient.wind.strong"));
/*  59 */     soundsList.add(new soundString("Ambient", "AMBIENT_RAINLIGHT_SND", "sound.ambient.rain.light"));
/*  60 */     soundsList.add(new soundString("Ambient", "AMBIENT_RAINHEAVY_SND", "sound.ambient.rain.heavy"));
/*  61 */     soundsList.add(new soundString("Ambient", "AMBIENT_FORESTCREAKSOFT_SND", "sound.forest.creak.soft"));
/*  62 */     soundsList.add(new soundString("Ambient", "AMBIENT_FORESTCREAKLOUD_SND", "sound.forest.creak.loud"));
/*  63 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIRE", "sound.ambient.fire"));
/*  64 */     soundsList.add(new soundString("Ambient", "FISHJUMP_SND", "sound.fish.splash"));
/*  65 */     soundsList.add(new soundString("Ambient", "AMBIENT_BUZZ_LEFT1", "sound.1.3.001.0001.001"));
/*  66 */     soundsList.add(new soundString("Ambient", "AMBIENT_BUZZ_RIGHT1", "sound.1.4.001.0001.001"));
/*  67 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_DAY_1", "sound.2.3.013.0002.001"));
/*  68 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_DAY_2", "sound.2.4.013.0002.001"));
/*  69 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_DAY_3", "sound.2.3.013.0002.002"));
/*  70 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_DAY_4", "sound.2.4.013.0002.002"));
/*  71 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_NIGHT_1", "sound.2.3.013.0003.001"));
/*  72 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_NIGHT_2", "sound.2.3.013.0003.002"));
/*  73 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_NIGHT_3", "sound.2.4.013.0003.001"));
/*  74 */     soundsList.add(new soundString("Ambient", "AMBIENT_FOREST_NIGHT_4", "sound.2.4.013.0003.002"));
/*  75 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_DAY_1", "sound.2.3.018.0002.001"));
/*  76 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_DAY_3", "sound.2.4.018.0002.001"));
/*  77 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_DAY_2", "sound.2.3.018.0002.002"));
/*  78 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_DAY_4", "sound.2.4.018.0002.002"));
/*  79 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_NIGHT_1", "sound.2.3.018.0003.001"));
/*  80 */     soundsList.add(new soundString("Ambient", "AMBIENT_FIELD_NIGHT_2", "sound.2.4.018.0003.001"));
/*  81 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_LEFT1", "sound.1.3.020.0001.001"));
/*  82 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_LEFT2", "sound.1.3.020.0001.002"));
/*  83 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_LEFT3", "sound.1.3.020.0001.003"));
/*  84 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_RIGHT1", "sound.1.4.020.0001.001"));
/*  85 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_RIGHT2", "sound.1.4.020.0001.002"));
/*  86 */     soundsList.add(new soundString("Ambient", "AMBIENT_MYCELIUM_BUZZ_RIGHT3", "sound.1.4.020.0001.003"));
/*  87 */     soundsList.add(new soundString("Ambient", "AMBIENT_GRASS_DAY_1", "sound.2.3.012.0002.001"));
/*  88 */     soundsList.add(new soundString("Ambient", "AMBIENT_GRASS_DAY_2", "sound.2.4.012.0002.001"));
/*  89 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_HIGH_DAY_3", "sound.2.3.015.0002.001"));
/*  90 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_HIGH_DAY_4", "sound.2.4.015.0002.001"));
/*  91 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_LOW_DAY_3", "sound.2.3.022.0002.001"));
/*  92 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_LOW_DAY_4", "sound.2.4.022.0002.001"));
/*  93 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_HIGH_NIGHT1", "sound.2.3.015.0003.001"));
/*  94 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_HIGH_NIGHT2", "sound.2.4.015.0003.001"));
/*  95 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_LOW_NIGHT1", "sound.2.3.022.0003.001"));
/*  96 */     soundsList.add(new soundString("Ambient", "AMBIENT_MOUNTAIN_LOW_NIGHT2", "sound.2.4.022.0003.001"));
/*  97 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_1", "sound.2.3.021.0002.001"));
/*  98 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_2", "sound.2.4.021.0002.001"));
/*  99 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_3", "sound.2.3.021.0004.001"));
/* 100 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_4", "sound.2.4.021.0004.001"));
/* 101 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_WATER_1", "sound.2.3.021.0003.001"));
/* 102 */     soundsList.add(new soundString("Ambient", "AMBIENT_CAVE_WATER_2", "sound.2.4.021.0003.001"));
/* 103 */     soundsList.add(new soundString("Ambient", "AMBIENT_STEPPE_DAY_1", "sound.2.3.017.0002.001"));
/* 104 */     soundsList.add(new soundString("Ambient", "AMBIENT_STEPPE_DAY_2", "sound.2.4.017.0002.001"));
/* 105 */     soundsList.add(new soundString("Ambient", "AMBIENT_STEPPE_NIGHT_1", "sound.2.3.017.0003.001"));
/* 106 */     soundsList.add(new soundString("Ambient", "AMBIENT_STEPPE_NIGHT_2", "sound.2.4.017.0003.001"));
/* 107 */     soundsList.add(new soundString("Ambient", "AMBIENT_DESERT_DAY_1", "sound.2.3.019.0002.001"));
/* 108 */     soundsList.add(new soundString("Ambient", "AMBIENT_DESERT_DAY_2", "sound.2.4.019.0002.001"));
/* 109 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_1", "sound.2.3.014.0003.002"));
/* 110 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_2", "sound.2.4.014.0002.003"));
/* 111 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_3", "sound.2.3.014.0003.003"));
/* 112 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_4", "sound.2.4.014.0003.002"));
/* 113 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_5", "sound.2.3.014.0002.001"));
/* 114 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_6", "sound.2.4.014.0002.001"));
/* 115 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_7", "sound.2.3.014.0002.002"));
/* 116 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_DAY_8", "sound.2.4.014.0002.002"));
/* 117 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_NIGHT_1", "sound.2.3.014.0003.001"));
/* 118 */     soundsList.add(new soundString("Ambient", "AMBIENT_LAKE_NIGHT_2", "sound.2.4.014.0003.001"));
/*     */     
/* 120 */     soundsList.add(new soundString("Arrow", "HIT_ARROW_WOOD_SND", "sound.arrow.hit.wood"));
/* 121 */     soundsList.add(new soundString("Arrow", "HIT_ARROW_METAL_SND", "sound.arrow.hit.metal"));
/* 122 */     soundsList.add(new soundString("Arrow", "ARROW_FLY_SND", "sound.arrow.shot"));
/* 123 */     soundsList.add(new soundString("Arrow", "ARROW_MISS_SND", "sound.arrow.miss"));
/* 124 */     soundsList.add(new soundString("Arrow", "ARROW_AIM_SND", "sound.arrow.aim"));
/* 125 */     soundsList.add(new soundString("Arrow", "HIT_ARROW_TREE_SND", "sound.arrow.stuck.wood"));
/* 126 */     soundsList.add(new soundString("Arrow", "ARROW_HITGROUND_SND", "sound.arrow.stuck.ground"));
/*     */     
/* 128 */     soundsList.add(new soundString("Bell", "BELL_TING_SND", "sound.bell.handbell"));
/* 129 */     soundsList.add(new soundString("Bell", "BELL_CRAZYTING_SND", "sound.bell.handbell.long"));
/* 130 */     soundsList.add(new soundString("Bell", "BELL_DONG1_SND", "sound.bell.dong.1"));
/* 131 */     soundsList.add(new soundString("Bell", "BELL_DONG2_SND", "sound.bell.dong.2"));
/* 132 */     soundsList.add(new soundString("Bell", "BELL_DONG3_SND", "sound.bell.dong.3"));
/* 133 */     soundsList.add(new soundString("Bell", "BELL_DONG4_SND", "sound.bell.dong.4"));
/* 134 */     soundsList.add(new soundString("Bell", "BELL_DONG5_SND", "sound.bell.dong.5"));
/*     */     
/* 136 */     soundsList.add(new soundString("Bird", "LARCHSONG_SND", "sound.birdsong.bird4"));
/* 137 */     soundsList.add(new soundString("Bird", "FINCHSONG_SND", "sound.birdsong.bird3"));
/* 138 */     soundsList.add(new soundString("Bird", "THRUSHSONG_SND", "sound.birdsong.bird2"));
/* 139 */     soundsList.add(new soundString("Bird", "OWLSONG_SND", "sound.birdsong.owl.short"));
/* 140 */     soundsList.add(new soundString("Bird", "NIGHTJARSONG_SND", "sound.birdsong.bird1"));
/* 141 */     soundsList.add(new soundString("Bird", "HAWKSONG_SND", "sound.birdsong.hawk"));
/* 142 */     soundsList.add(new soundString("Bird", "CROWSONG_SND", "sound.birdsong.crows"));
/* 143 */     soundsList.add(new soundString("Bird", "BIRD5SONG_SND", "sound.birdsong.bird5"));
/* 144 */     soundsList.add(new soundString("Bird", "BIRD6SONG_SND", "sound.birdsong.bird6"));
/* 145 */     soundsList.add(new soundString("Bird", "BIRD7SONG_SND", "sound.birdsong.bird7"));
/* 146 */     soundsList.add(new soundString("Bird", "BIRD8SONG_SND", "sound.birdsong.bird8"));
/* 147 */     soundsList.add(new soundString("Bird", "BIRD9SONG_SND", "sound.birdsong.bird9"));
/*     */     
/* 149 */     soundsList.add(new soundString("Combat", "SCORP_MANDIBLES_1_SND", "sound.6.1.003.0001.001"));
/* 150 */     soundsList.add(new soundString("Combat", "SCORP_MANDIBLES_2_SND", "sound.6.1.003.0001.002"));
/* 151 */     soundsList.add(new soundString("Combat", "SCORP_CALL_1_SND", "sound.6.1.003.0002.001"));
/* 152 */     soundsList.add(new soundString("Combat", "SCORP_CALL_2_SND", "sound.6.1.003.0002.002"));
/* 153 */     soundsList.add(new soundString("Combat", "SCORP_HIT_1_SND", "sound.6.1.003.0003.001"));
/* 154 */     soundsList.add(new soundString("Combat", "SCORP_HIT_2_SND", "sound.6.1.003.0003.002"));
/* 155 */     soundsList.add(new soundString("Combat", "SCORP_SCREECH_SND", "sound.6.1.003.0003.003"));
/* 156 */     soundsList.add(new soundString("Combat", "SHIELD_BASH_SND", "sound.combat.shield.bash"));
/* 157 */     soundsList.add(new soundString("Combat", "SHIELD_WOOD_SND", "sound.combat.shield.wood"));
/* 158 */     soundsList.add(new soundString("Combat", "SHIELD_METAL_SND", "sound.combat.shield.metal"));
/* 159 */     soundsList.add(new soundString("Combat", "PARRY1_SND", "sound.combat.parry1"));
/* 160 */     soundsList.add(new soundString("Combat", "PARRY2_SND", "sound.combat.parry2"));
/* 161 */     soundsList.add(new soundString("Combat", "PARRY3_SND", "sound.combat.parry3"));
/* 162 */     soundsList.add(new soundString("Combat", "FLESH1_SND", "sound.combat.fleshhit1"));
/* 163 */     soundsList.add(new soundString("Combat", "FLESH2_SND", "sound.combat.fleshhit2"));
/* 164 */     soundsList.add(new soundString("Combat", "FLESH3_SND", "sound.combat.fleshhit3"));
/* 165 */     soundsList.add(new soundString("Combat", "FLESHMETAL1_SND", "sound.combat.fleshmetal1"));
/* 166 */     soundsList.add(new soundString("Combat", "FLESHMETAL2_SND", "sound.combat.fleshmetal2"));
/* 167 */     soundsList.add(new soundString("Combat", "FLESHMETAL3_SND", "sound.combat.fleshmetal3"));
/* 168 */     soundsList.add(new soundString("Combat", "FLESHBONE1_SND", "sound.combat.fleshbone1"));
/* 169 */     soundsList.add(new soundString("Combat", "FLESHBONE2_SND", "sound.combat.fleshbone2"));
/* 170 */     soundsList.add(new soundString("Combat", "FLESHBONE3_SND", "sound.combat.fleshbone3"));
/* 171 */     soundsList.add(new soundString("Combat", "MISS_LIGHT_SND", "sound.combat.miss.light"));
/* 172 */     soundsList.add(new soundString("Combat", "MISS_MED_SND", "sound.combat.miss.med"));
/* 173 */     soundsList.add(new soundString("Combat", "MISS_HEAVY_SND", "sound.combat.miss.heavy"));
/* 174 */     soundsList.add(new soundString("Combat", "HIT_MALE_SND", "sound.combat.hit.male"));
/* 175 */     soundsList.add(new soundString("Combat", "HIT_MALE_KID_SND", "sound.combat.hit.male.child"));
/* 176 */     soundsList.add(new soundString("Combat", "HIT_FEMALE_SND", "sound.combat.hit.female"));
/* 177 */     soundsList.add(new soundString("Combat", "HIT_FEMALE_KID_SND", "sound.combat.hit.female.child"));
/* 178 */     soundsList.add(new soundString("Combat", "HIT_ZOMBIE_SND", "sound.combat.hit.zombie"));
/* 179 */     soundsList.add(new soundString("Combat", "HIT_SKELETON_SND", "sound.combat.hit.skeleton"));
/* 180 */     soundsList.add(new soundString("Combat", "HIT_SPIRIT_MALE_SND", "sound.combat.hit.spirit.male"));
/* 181 */     soundsList.add(new soundString("Combat", "HIT_SPIRIT_FEMALE_SND", "sound.combat.hit.spirit.female"));
/* 182 */     soundsList.add(new soundString("Combat", "HIT_COWBROWN_SND", "sound.combat.hit.cow.brown"));
/* 183 */     soundsList.add(new soundString("Combat", "HIT_DEER_SND", "sound.combat.hit.deer"));
/* 184 */     soundsList.add(new soundString("Combat", "HIT_HEN_SND", "sound.combat.hit.hen"));
/* 185 */     soundsList.add(new soundString("Combat", "HIT_WOLF_SND", "sound.combat.hit.wolf"));
/* 186 */     soundsList.add(new soundString("Combat", "HIT_LIZARD_SND", "sound.combat.hit.lizard"));
/* 187 */     soundsList.add(new soundString("Combat", "HIT_DEMON_SND", "sound.combat.hit.demon"));
/* 188 */     soundsList.add(new soundString("Combat", "HIT_DEATHCRAWLER_SND", "sound.combat.hit.deathcrawler"));
/* 189 */     soundsList.add(new soundString("Combat", "HIT_SPAWN_UTTACHA_SND", "sound.combat.hit.uttacha.spawn"));
/* 190 */     soundsList.add(new soundString("Combat", "HIT_SON_NOGUMP_SND", "sound.combat.hit.nogump.son"));
/* 191 */     soundsList.add(new soundString("Combat", "HIT_DRAKESPIRIT_SND", "sound.combat.hit.drakespirit"));
/* 192 */     soundsList.add(new soundString("Combat", "HIT_EAGLESPIRIT_SND", "sound.combat.hit.eaglespirit"));
/* 193 */     soundsList.add(new soundString("Combat", "HIT_EPIPHANY_VYNORA_SND", "sound.combat.hit.vynora.epiphany"));
/* 194 */     soundsList.add(new soundString("Combat", "HIT_JUGGERNAUT_MAGRANON_SND", "sound.combat.hit.magranon.juggernaut"));
/* 195 */     soundsList.add(new soundString("Combat", "HIT_MANIFESTATION_FO_SND", "sound.combat.hit.fo.manifestation"));
/* 196 */     soundsList.add(new soundString("Combat", "HIT_INCARNATION_LIBILA_SND", "sound.combat.hit.libila.incarnation"));
/* 197 */     soundsList.add(new soundString("Combat", "HIT_CROC_SND", "sound.combat.hit.croc"));
/* 198 */     soundsList.add(new soundString("Combat", "HIT_TROLL_SND", "sound.combat.hit.troll"));
/* 199 */     soundsList.add(new soundString("Combat", "HIT_PHEASANT_SND", "sound.combat.hit.pheasant"));
/* 200 */     soundsList.add(new soundString("Combat", "HIT_BEAR_SND", "sound.combat.hit.bear"));
/* 201 */     soundsList.add(new soundString("Combat", "HIT_INSECT_SND", "sound.combat.hit.insect"));
/* 202 */     soundsList.add(new soundString("Combat", "HIT_LION_SND", "sound.combat.hit.lion"));
/* 203 */     soundsList.add(new soundString("Combat", "HIT_RAT_SND", "sound.combat.hit.rat"));
/* 204 */     soundsList.add(new soundString("Combat", "HIT_CAT_SND", "sound.combat.hit.cat"));
/* 205 */     soundsList.add(new soundString("Combat", "HIT_DRAGON_SND", "sound.combat.hit.dragon"));
/* 206 */     soundsList.add(new soundString("Combat", "HIT_GIANT_SND", "sound.combat.hit.giant"));
/* 207 */     soundsList.add(new soundString("Combat", "HIT_SPIDER_SND", "sound.combat.hit.spider"));
/* 208 */     soundsList.add(new soundString("Combat", "HIT_GOBLIN_SND", "sound.combat.hit.goblin"));
/* 209 */     soundsList.add(new soundString("Combat", "HIT_HORSE_SND", "sound.combat.hit.horse"));
/* 210 */     soundsList.add(new soundString("Combat", "HIT_OOZE_SND", "sound.combat.hit.ooze"));
/* 211 */     soundsList.add(new soundString("Combat", "HIT_GORILLA_SND", "sound.combat.hit.gorilla"));
/* 212 */     soundsList.add(new soundString("Combat", "HIT_PIG_SND", "sound.combat.hit.pig"));
/* 213 */     soundsList.add(new soundString("Combat", "HIT_SNAKE_SND", "sound.combat.hit.snake"));
/* 214 */     soundsList.add(new soundString("Combat", "HIT_DOG_SND", "sound.combat.hit.dog"));
/* 215 */     soundsList.add(new soundString("Combat", "HIT_BISON_SND", "sound.combat.hit.bison"));
/*     */     
/* 217 */     soundsList.add(new soundString("Death", "DEATH_MALE_SND", "sound.death.male"));
/* 218 */     soundsList.add(new soundString("Death", "DEATH_MALE_KID_SND", "sound.death.male.child"));
/* 219 */     soundsList.add(new soundString("Death", "DEATH_FEMALE_SND", "sound.death.female"));
/* 220 */     soundsList.add(new soundString("Death", "DEATH_FEMALE_KID_SND", "sound.death.female.child"));
/* 221 */     soundsList.add(new soundString("Death", "DEATH_ZOMBIE_SND", "sound.combat.death.zombie"));
/* 222 */     soundsList.add(new soundString("Death", "DEATH_SKELETON_SND", "sound.combat.death.skeleton"));
/* 223 */     soundsList.add(new soundString("Death", "DEATH_SPIRIT_MALE_SND", "sound.death.spirit.male"));
/* 224 */     soundsList.add(new soundString("Death", "DEATH_SPIRIT_FEMALE_SND", "sound.death.spirit.female"));
/* 225 */     soundsList.add(new soundString("Death", "DEATH_COWBROWN_SND", "sound.death.cow.brown"));
/* 226 */     soundsList.add(new soundString("Death", "DEATH_DEER_SND", "sound.death.deer"));
/* 227 */     soundsList.add(new soundString("Death", "DEATH_HEN_SND", "sound.death.hen"));
/* 228 */     soundsList.add(new soundString("Death", "DEATH_WOLF_SND", "sound.death.wolf"));
/* 229 */     soundsList.add(new soundString("Death", "DEATH_LIZARD_SND", "sound.death.lizard"));
/* 230 */     soundsList.add(new soundString("Death", "DEATH_DEMON_SND", "sound.death.demon"));
/* 231 */     soundsList.add(new soundString("Death", "DEATH_DEATHCRAWLER_SND", "sound.death.deathcrawler"));
/* 232 */     soundsList.add(new soundString("Death", "DEATH_SPAWN_UTTACHA_SND", "sound.death.uttacha.spawn"));
/* 233 */     soundsList.add(new soundString("Death", "DEATH_SON_NOGUMP_SND", "sound.death.nogump.son"));
/* 234 */     soundsList.add(new soundString("Death", "DEATH_DRAKESPIRIT_SND", "sound.death.drakespirit"));
/* 235 */     soundsList.add(new soundString("Death", "DEATH_EAGLESPIRIT_SND", "sound.death.eaglespirit"));
/* 236 */     soundsList.add(new soundString("Death", "DEATH_EPIPHANY_VYNORA_SND", "sound.death.vynora.epiphany"));
/* 237 */     soundsList.add(new soundString("Death", "DEATH_JUGGERNAUT_MAGRANON_SND", "sound.death.magranon.juggernaut"));
/* 238 */     soundsList.add(new soundString("Death", "DEATH_MANIFESTATION_FO_SND", "sound.death.fo.manifestation"));
/* 239 */     soundsList.add(new soundString("Death", "DEATH_INCARNATION_LIBILA_SND", "sound.death.libila.incarnation"));
/* 240 */     soundsList.add(new soundString("Death", "DEATH_CROC_SND", "sound.death.croc"));
/* 241 */     soundsList.add(new soundString("Death", "DEATH_PHEASANT_SND", "sound.death.pheasant"));
/* 242 */     soundsList.add(new soundString("Death", "DEATH_TROLL_SND", "sound.death.troll"));
/* 243 */     soundsList.add(new soundString("Death", "DEATH_BEAR_SND", "sound.death.bear"));
/* 244 */     soundsList.add(new soundString("Death", "DEATH_INSECT_SND", "sound.death.insect"));
/* 245 */     soundsList.add(new soundString("Death", "DEATH_LION_SND", "sound.death.lion"));
/* 246 */     soundsList.add(new soundString("Death", "DEATH_RAT_SND", "sound.death.rat"));
/* 247 */     soundsList.add(new soundString("Death", "DEATH_CAT_SND", "sound.death.cat"));
/* 248 */     soundsList.add(new soundString("Death", "DEATH_DRAGON_SND", "sound.death.dragon"));
/* 249 */     soundsList.add(new soundString("Death", "DEATH_GIANT_SND", "sound.death.giant"));
/* 250 */     soundsList.add(new soundString("Death", "DEATH_SPIDER_SND", "sound.death.spider"));
/* 251 */     soundsList.add(new soundString("Death", "DEATH_GOBLIN_SND", "sound.death.goblin"));
/* 252 */     soundsList.add(new soundString("Death", "DEATH_HORSE_SND", "sound.death.horse"));
/* 253 */     soundsList.add(new soundString("Death", "DEATH_OOZE_SND", "sound.death.ooze"));
/* 254 */     soundsList.add(new soundString("Death", "DEATH_GORILLA_SND", "sound.death.gorilla"));
/* 255 */     soundsList.add(new soundString("Death", "DEATH_PIG_SND", "sound.death.pig"));
/* 256 */     soundsList.add(new soundString("Death", "DEATH_SNAKE_SND", "sound.death.snake"));
/* 257 */     soundsList.add(new soundString("Death", "DEATH_DOG_SND", "sound.death.dog"));
/* 258 */     soundsList.add(new soundString("Death", "DEATH_BISON_SND", "sound.death.bison"));
/*     */     
/* 260 */     soundsList.add(new soundString("Destroy", "DESTROYWALLWOOD_AXE_SND", "sound.destroywall.wood.axe"));
/* 261 */     soundsList.add(new soundString("Destroy", "DESTROYWALLWOOD_MAUL_SND", "sound.destroywall.wood.maul"));
/* 262 */     soundsList.add(new soundString("Destroy", "DESTROYWALLSTONE_MAUL_SND", "sound.destroywall.stone.maul"));
/* 263 */     soundsList.add(new soundString("Destroy", "DESTROYWALLSTONE_AXE_SND", "sound.destroywall.stone.axe"));
/* 264 */     soundsList.add(new soundString("Destroy", "DESTROYITEMSTONE_AXE_SND", "sound.destroyobject.stone.axe"));
/* 265 */     soundsList.add(new soundString("Destroy", "DESTROYITEMSTONE_MAUL_SND", "sound.destroyobject.stone.maul"));
/* 266 */     soundsList.add(new soundString("Destroy", "DESTROYITEMWOOD_AXE_SND", "sound.destroyobject.wood.axe"));
/* 267 */     soundsList.add(new soundString("Destroy", "DESTROYITEMWOOD_MAUL_SND", "sound.destroyobject.wood.maul"));
/* 268 */     soundsList.add(new soundString("Destroy", "DESTROYITEMMETAL_AXE_SND", "sound.destroyobject.metal.axe"));
/* 269 */     soundsList.add(new soundString("Destroy", "DESTROYITEMMETAL_MAUL_SND", "sound.destroyobject.metal.maul"));
/*     */     
/* 271 */     soundsList.add(new soundString("Emote", "EMOTE_CHUCKLE_SND", "sound.emote.chuckle"));
/* 272 */     soundsList.add(new soundString("Emote", "EMOTE_APPLAUD_SND", "sound.emote.applaud"));
/* 273 */     soundsList.add(new soundString("Emote", "EMOTE_KISS_SND", "sound.emote.kiss"));
/* 274 */     soundsList.add(new soundString("Emote", "EMOTE_COMFORT_SND", "sound.emote.comfort"));
/* 275 */     soundsList.add(new soundString("Emote", "EMOTE_WAVE_SND", "sound.emote.wave"));
/* 276 */     soundsList.add(new soundString("Emote", "EMOTE_CALL_SND", "sound.emote.call"));
/* 277 */     soundsList.add(new soundString("Emote", "EMOTE_DISAGREE_SND", "sound.emote.disagree"));
/* 278 */     soundsList.add(new soundString("Emote", "EMOTE_WORRY_SND", "sound.emote.worry"));
/* 279 */     soundsList.add(new soundString("Emote", "EMOTE_TEASE_SND", "sound.emote.tease"));
/* 280 */     soundsList.add(new soundString("Emote", "EMOTE_LAUGH_SND", "sound.emote.laugh"));
/* 281 */     soundsList.add(new soundString("Emote", "EMOTE_CRY_SND", "sound.emote.cry"));
/* 282 */     soundsList.add(new soundString("Emote", "EMOTE_SPIT_SND", "sound.emote.spit"));
/* 283 */     soundsList.add(new soundString("Emote", "EMOTE_FART_SND", "sound.emote.fart"));
/* 284 */     soundsList.add(new soundString("Emote", "EMOTE_INSULT_SND", "sound.emote.insult"));
/* 285 */     soundsList.add(new soundString("Emote", "EMOTE_CURSE_SND", "sound.emote.curse"));
/* 286 */     soundsList.add(new soundString("Emote", "EMOTE_SLAP_SND", "sound.emote.slap"));
/* 287 */     soundsList.add(new soundString("Emote", "EMOTE_WRONG_WAY_SND", "sound.emote.wrong.way"));
/* 288 */     soundsList.add(new soundString("Emote", "EMOTE_THAT_WAY_SND", "sound.emote.that.way"));
/* 289 */     soundsList.add(new soundString("Emote", "EMOTE_LEAD_SND", "sound.emote.lead"));
/* 290 */     soundsList.add(new soundString("Emote", "EMOTE_GOODBYE_SND", "sound.emote.goodbye"));
/* 291 */     soundsList.add(new soundString("Emote", "EMOTE_FOLLOW_SND", "sound.emote.follow"));
/*     */     
/* 293 */     soundsList.add(new soundString("Fx", "CONCH", "sound.fx.conch"));
/* 294 */     soundsList.add(new soundString("Fx", "DRUMROLL", "sound.fx.drumroll"));
/* 295 */     soundsList.add(new soundString("Fx", "ACHIEVEMENT", "sound.achievement"));
/* 296 */     soundsList.add(new soundString("Fx", "ACHIEVEMENT_UPDATE", "sound.achievement.update"));
/* 297 */     soundsList.add(new soundString("Fx", "FALLING_TREE", "sound.tree.falling"));
/* 298 */     soundsList.add(new soundString("Fx", "FALLING_TREE2", "sound.tree.fall"));
/* 299 */     soundsList.add(new soundString("Fx", "CHEST_OPENING", "sound.chest.open"));
/* 300 */     soundsList.add(new soundString("Fx", "NOTIFICATION", "sound.notification"));
/* 301 */     soundsList.add(new soundString("Fx", "ITEM_SPAWN_CENTRAL", "sound.spawn.item.central"));
/* 302 */     soundsList.add(new soundString("Fx", "ITEM_SPAWN_PERIMETER", "sound.spawn.item.perimeter"));
/* 303 */     soundsList.add(new soundString("Fx", "RIFTSPAWNCREATURES", "sound.rift.spawn"));
/* 304 */     soundsList.add(new soundString("Fx", "CREATURELAND", "sound.creature.land.1"));
/* 305 */     soundsList.add(new soundString("Fx", "RIFTCRYSTAL1", "sound.rift.crystal.1"));
/* 306 */     soundsList.add(new soundString("Fx", "RIFTCRYSTAL2", "sound.rift.crystal.2"));
/* 307 */     soundsList.add(new soundString("Fx", "RIFTCRYSTAL3", "sound.rift.crystal.3"));
/* 308 */     soundsList.add(new soundString("Fx", "RIFTSHUT", "sound.rift.shut"));
/* 309 */     soundsList.add(new soundString("Fx", "AMBIENT_BEES", "sound.bees"));
/* 310 */     soundsList.add(new soundString("Fx", "HUMM_SND", "sound.fx.humm"));
/* 311 */     soundsList.add(new soundString("Fx", "OOH_MALE_SND", "sound.fx.ooh.male"));
/* 312 */     soundsList.add(new soundString("Fx", "OOH_FEMALE_SND", "sound.fx.ooh.female"));
/*     */     
/* 314 */     soundsList.add(new soundString("Gnome", "GNOME_NICE_1", "sound.5.2.009.0084.001"));
/* 315 */     soundsList.add(new soundString("Gnome", "GNOME_NICE_2", "sound.5.2.009.0085.001"));
/* 316 */     soundsList.add(new soundString("Gnome", "GNOME_NICE_3", "sound.5.2.009.0086.001"));
/* 317 */     soundsList.add(new soundString("Gnome", "GNOME_NICE_4", "sound.5.2.009.0087.001"));
/* 318 */     soundsList.add(new soundString("Gnome", "GNOME_ANGRY_1", "sound.5.2.009.0088.001"));
/* 319 */     soundsList.add(new soundString("Gnome", "GNOME_ANGRY_2", "sound.5.2.009.0089.001"));
/* 320 */     soundsList.add(new soundString("Gnome", "GNOME_ANGRY_3", "sound.5.2.009.0090.001"));
/* 321 */     soundsList.add(new soundString("Gnome", "GNOME_ANGRY_4", "sound.2.4.021.0091.001"));
/*     */     
/* 323 */     soundsList.add(new soundString("Liquid", "WATER_FIZZLE_SND", "sound.liquid.fzz"));
/* 324 */     soundsList.add(new soundString("Liquid", "FILLCONTAINER_BARREL_SND", "sound.liquid.fillcontainer.barrel"));
/* 325 */     soundsList.add(new soundString("Liquid", "FILLCONTAINER_BUCKET_SND", "sound.liquid.fillcontainer.bucket"));
/* 326 */     soundsList.add(new soundString("Liquid", "FILLCONTAINER_JAR_SND", "sound.liquid.fillcontainer.jar"));
/* 327 */     soundsList.add(new soundString("Liquid", "FILLCONTAINER_SND", "sound.liquid.fillcontainer"));
/* 328 */     soundsList.add(new soundString("Liquid", "DRINKWATER_SND", "sound.liquid.drink"));
/*     */     
/* 330 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_1", "sound.7.1.006.0001.001"));
/* 331 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_2", "sound.7.1.006.0001.002"));
/* 332 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_3", "sound.7.1.006.0001.003"));
/* 333 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_4", "sound.7.1.006.0001.004"));
/* 334 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_5", "sound.7.1.006.0001.005"));
/* 335 */     soundsList.add(new soundString("Movement", "MOVE_MOUNTAIN_6", "sound.7.1.006.0001.006"));
/* 336 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_1", "sound.7.1.006.0002.001"));
/* 337 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_2", "sound.7.1.006.0002.002"));
/* 338 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_3", "sound.7.1.006.0002.003"));
/* 339 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_4", "sound.7.1.006.0002.004"));
/* 340 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_5", "sound.7.1.006.0002.005"));
/* 341 */     soundsList.add(new soundString("Movement", "MOVE_GRASS_6", "sound.7.1.006.0002.006"));
/* 342 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_1", "sound.7.1.006.0003.001"));
/* 343 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_2", "sound.7.1.006.0003.002"));
/* 344 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_3", "sound.7.1.006.0003.003"));
/* 345 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_4", "sound.7.1.006.0003.004"));
/* 346 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_5", "sound.7.1.006.0003.005"));
/* 347 */     soundsList.add(new soundString("Movement", "MOVE_COBBLE_6", "sound.7.1.006.0003.006"));
/* 348 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_1", "sound.7.1.006.0004.001"));
/* 349 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_2", "sound.7.1.006.0004.002"));
/* 350 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_3", "sound.7.1.006.0004.003"));
/* 351 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_4", "sound.7.1.006.0004.004"));
/* 352 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_5", "sound.7.1.006.0004.005"));
/* 353 */     soundsList.add(new soundString("Movement", "MOVE_DIRT_6", "sound.7.1.006.0004.006"));
/* 354 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_1", "sound.7.1.006.0005.001"));
/* 355 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_2", "sound.7.1.006.0005.002"));
/* 356 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_3", "sound.7.1.006.0005.003"));
/* 357 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_4", "sound.7.1.006.0005.004"));
/* 358 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_5", "sound.7.1.006.0005.005"));
/* 359 */     soundsList.add(new soundString("Movement", "MOVE_GRAVEL_6", "sound.7.1.006.0005.006"));
/* 360 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_1", "sound.7.1.006.0006.001"));
/* 361 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_2", "sound.7.1.006.0006.002"));
/* 362 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_3", "sound.7.1.006.0006.003"));
/* 363 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_4", "sound.7.1.006.0006.004"));
/* 364 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_5", "sound.7.1.006.0006.005"));
/* 365 */     soundsList.add(new soundString("Movement", "MOVE_WOOD_6", "sound.7.1.006.0006.006"));
/* 366 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_1", "sound.7.1.006.0007.001"));
/* 367 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_2", "sound.7.1.006.0007.002"));
/* 368 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_3", "sound.7.1.006.0007.003"));
/* 369 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_4", "sound.7.1.006.0007.004"));
/* 370 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_5", "sound.7.1.006.0007.005"));
/* 371 */     soundsList.add(new soundString("Movement", "MOVE_SLAB_6", "sound.7.1.006.0007.006"));
/* 372 */     soundsList.add(new soundString("Movement", "MOVE_SAND_1", "sound.7.1.006.0008.001"));
/* 373 */     soundsList.add(new soundString("Movement", "MOVE_SAND_2", "sound.7.1.006.0008.002"));
/* 374 */     soundsList.add(new soundString("Movement", "MOVE_SAND_3", "sound.7.1.006.0008.003"));
/* 375 */     soundsList.add(new soundString("Movement", "MOVE_SAND_4", "sound.7.1.006.0008.004"));
/* 376 */     soundsList.add(new soundString("Movement", "MOVE_SAND_5", "sound.7.1.006.0008.005"));
/* 377 */     soundsList.add(new soundString("Movement", "MOVE_SAND_6", "sound.7.1.006.0008.006"));
/* 378 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_1", "sound.7.1.006.0009.001"));
/* 379 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_2", "sound.7.1.006.0009.002"));
/* 380 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_3", "sound.7.1.006.0009.003"));
/* 381 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_4", "sound.7.1.006.0009.004"));
/* 382 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_5", "sound.7.1.006.0009.005"));
/* 383 */     soundsList.add(new soundString("Movement", "MOVE_STEPPE_6", "sound.7.1.006.0009.006"));
/* 384 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_1", "sound.7.1.006.0010.001"));
/* 385 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_2", "sound.7.1.006.0010.002"));
/* 386 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_3", "sound.7.1.006.0010.003"));
/* 387 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_4", "sound.7.1.006.0010.004"));
/* 388 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_5", "sound.7.1.006.0010.005"));
/* 389 */     soundsList.add(new soundString("Movement", "MOVE_CAVE_6", "sound.7.1.006.0010.006"));
/* 390 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_1", "sound.7.1.006.0011.001"));
/* 391 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_2", "sound.7.1.006.0011.002"));
/* 392 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_3", "sound.7.1.006.0011.003"));
/* 393 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_4", "sound.7.1.006.0011.004"));
/* 394 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_5", "sound.7.1.006.0011.005"));
/* 395 */     soundsList.add(new soundString("Movement", "MOVE_SHORE_6", "sound.7.1.006.0011.006"));
/* 396 */     soundsList.add(new soundString("Movement", "MOVE_CONSTANTLOOP", "sound.7.1.006.0018.001"));
/* 397 */     soundsList.add(new soundString("Movement", "MOVE_QUICK_RUSTLE", "sound.7.1.006.0019.001"));
/* 398 */     soundsList.add(new soundString("Movement", "MOVE_SWIMFAST_LOOP", "sound.7.1.006.0020.001"));
/* 399 */     soundsList.add(new soundString("Movement", "MOVE_SWIM_SLOW_LOOP", "sound.7.1.006.0021.001"));
/* 400 */     soundsList.add(new soundString("Movement", "MOVE_SWIM_BOBBING", "sound.7.1.006.0022.001"));
/* 401 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_1", "sound.7.1.006.0023.001"));
/* 402 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_2", "sound.7.1.006.0023.002"));
/* 403 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_3", "sound.7.1.006.0023.003"));
/* 404 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_4", "sound.7.1.006.0023.004"));
/* 405 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_5", "sound.7.1.006.0023.005"));
/* 406 */     soundsList.add(new soundString("Movement", "MOVE_ROLL_6", "sound.7.1.006.0023.006"));
/* 407 */     soundsList.add(new soundString("Movement", "MOVEITEM_SND", "sound.object.move.pushpull"));
/*     */     
/* 409 */     soundsList.add(new soundString("Music", "MUSIC_BLACKLIGHT_SND", "sound.music.song.blacklight"));
/* 410 */     soundsList.add(new soundString("Music", "MUSIC_CAVEHALL1_SND", "sound.music.song.cavehall1"));
/* 411 */     soundsList.add(new soundString("Music", "MUSIC_CAVEHALL2_SND", "sound.music.song.cavehall2"));
/* 412 */     soundsList.add(new soundString("Music", "MUSIC_COLOSSUS_SND", "sound.music.song.colossus"));
/* 413 */     soundsList.add(new soundString("Music", "MUSIC_DISBAND_SND", "sound.music.song.disbandvillage"));
/* 414 */     soundsList.add(new soundString("Music", "MUSIC_DYING1_SND", "sound.music.song.dying1"));
/* 415 */     soundsList.add(new soundString("Music", "MUSIC_DYING2_SND", "sound.music.song.dying2"));
/* 416 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES1_SND", "sound.music.song.echoes1"));
/* 417 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES2_SND", "sound.music.song.echoes2"));
/* 418 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES3_SND", "sound.music.song.echoes3"));
/* 419 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES4_SND", "sound.music.song.echoes4"));
/* 420 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES5_SND", "sound.music.song.echoes5"));
/* 421 */     soundsList.add(new soundString("Music", "MUSIC_ECHOES6_SND", "sound.music.song.echoes6"));
/* 422 */     soundsList.add(new soundString("Music", "MUSIC_FOUNDSETTLEMENT_SND", "sound.music.song.foundsettlement"));
/* 423 */     soundsList.add(new soundString("Music", "MUSIC_MOUNTAINTOP_SND", "sound.music.song.mountaintop"));
/* 424 */     soundsList.add(new soundString("Music", "MUSIC_PRAYINGLIBILA_SND", "sound.music.song.prayinglibila"));
/* 425 */     soundsList.add(new soundString("Music", "MUSIC_PRAYINGMAGRANON_SND", "sound.music.song.prayingmagranon"));
/* 426 */     soundsList.add(new soundString("Music", "MUSIC_PRAYINGVYNORA_SND", "sound.music.song.prayingvynora"));
/* 427 */     soundsList.add(new soundString("Music", "MUSIC_PRAYINGFO_SND", "sound.music.song.prayingfo"));
/* 428 */     soundsList.add(new soundString("Music", "MUSIC_SHANTY1_SND", "sound.music.song.shanty1"));
/* 429 */     soundsList.add(new soundString("Music", "MUSIC_SUNRISEPASS_SND", "sound.music.song.sunrisepass"));
/* 430 */     soundsList.add(new soundString("Music", "MUSIC_SUNRISE1_SND", "sound.music.song.sunrise1"));
/* 431 */     soundsList.add(new soundString("Music", "MUSIC_TERRITORYHOTS_SND", "sound.music.song.territoryhots"));
/* 432 */     soundsList.add(new soundString("Music", "MUSIC_TERRITORYWL_SND", "sound.music.song.territorywl"));
/* 433 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELLING1_SND", "sound.music.song.travelling1"));
/* 434 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELLING2_SND", "sound.music.song.travelling2"));
/* 435 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELLING3_SND", "sound.music.song.travelling3"));
/* 436 */     soundsList.add(new soundString("Music", "MUSIC_WHITELIGHT_SND", "sound.music.song.whitelight"));
/* 437 */     soundsList.add(new soundString("Music", "MUSIC_VILLAGERAIN_SND", "sound.music.song.villagerain"));
/* 438 */     soundsList.add(new soundString("Music", "MUSIC_VILLAGESUN_SND", "sound.music.song.villagesun"));
/* 439 */     soundsList.add(new soundString("Music", "MUSIC_VILLAGEWORK_SND", "sound.music.song.villagework"));
/* 440 */     soundsList.add(new soundString("Music", "MUSIC_WURMISWAITING_SND", "sound.music.song.wurmiswaiting"));
/* 441 */     soundsList.add(new soundString("Music", "MUSIC_ANTHEMHOTS_SND", "sound.music.song.anthemhots"));
/* 442 */     soundsList.add(new soundString("Music", "MUSIC_ANTHEMMOLREHAN_SND", "sound.music.song.anthemmolrehan"));
/* 443 */     soundsList.add(new soundString("Music", "MUSIC_ANTHEMJENN_SND", "sound.music.song.anthemjenn"));
/* 444 */     soundsList.add(new soundString("Music", "MUSIC_LOADING1_SND", "sound.music.song.loading1"));
/* 445 */     soundsList.add(new soundString("Music", "MUSIC_LOADING1A_SND", "sound.music.song.loading1a"));
/* 446 */     soundsList.add(new soundString("Music", "MUSIC_LOADING2_SND", "sound.music.song.loading2"));
/* 447 */     soundsList.add(new soundString("Music", "MUSIC_LOADING2A_SND", "sound.music.song.loading2a"));
/* 448 */     soundsList.add(new soundString("Music", "MUSIC_QUIT_F12_SND", "sound.music.song.quit-f12"));
/* 449 */     soundsList.add(new soundString("Music", "MUSIC_QUIT_NO_SND", "sound.music.song.quit-no"));
/* 450 */     soundsList.add(new soundString("Music", "MUSIC_QUIT_YES_SND", "sound.music.song.quit-yes"));
/* 451 */     soundsList.add(new soundString("Music", "MUSIC_SPAWN_SND", "sound.music.song.spawn1"));
/* 452 */     soundsList.add(new soundString("Music", "MUSIC_OPENCHRISTMAS_SND", "sound.music.song.christmas"));
/* 453 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_WAKING_SND", "sound.music.song.wakingup"));
/* 454 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_FINGER_SND", "sound.music.song.fingerfo"));
/* 455 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_INEYES_SND", "sound.music.song.inyoureyes"));
/* 456 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_BEATING_SND", "sound.music.song.beatinganvil"));
/* 457 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_PROMISING_SND", "sound.music.song.promisingfoal"));
/* 458 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_SUMMER_SND", "sound.music.song.longsummer"));
/* 459 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_WHYDIVE_SND", "sound.music.song.whyyoudive"));
/* 460 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_NORTH_SND", "sound.music.song.north"));
/* 461 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_THROUGH_SND", "sound.music.song.through"));
/* 462 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_SHORES_SND", "sound.music.song.shores"));
/* 463 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_STRIDE_SND", "sound.music.song.stride"));
/* 464 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_RIDGE_SND", "sound.music.song.ridge"));
/* 465 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_SKYFIRE_SND", "sound.music.song.skyfire"));
/* 466 */     soundsList.add(new soundString("Music", "MUSIC_TRAVELEXP_FAMILIAR_SND", "sound.music.song.familiar"));
/* 467 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_FINGER_TONING_SND", "sound.music.song.fingertone"));
/* 468 */     soundsList.add(new soundString("Music", "MUSIC_WORKMED_PROMISTONE_SND", "sound.music.song.promisetone"));
/* 469 */     soundsList.add(new soundString("Music", "MUSIC_GNO_SND", "sound.5.2.009.0083.001"));
/* 470 */     soundsList.add(new soundString("Music", "SONG_ABANDON", "sound.music.song.abandon"));
/* 471 */     soundsList.add(new soundString("Music", "SONG_BACKHOME", "sound.music.song.backhome"));
/* 472 */     soundsList.add(new soundString("Music", "SONG_DEADWATER", "sound.music.song.deadwater"));
/* 473 */     soundsList.add(new soundString("Music", "SONG_CONTACT", "sound.music.song.contact"));
/* 474 */     soundsList.add(new soundString("Music", "SONG_SUNGLOW", "sound.music.song.sunglow"));
/* 475 */     soundsList.add(new soundString("Music", "SONG_TURMOILED", "sound.music.song.turmoiled"));
/* 476 */     soundsList.add(new soundString("Music", "SONG_DANCEHORDE", "sound.music.song.dancehorde"));
/* 477 */     soundsList.add(new soundString("Music", "SONG_UNLIMITED", "sound.music.song.unlimited"));
/*     */     
/* 479 */     soundsList.add(new soundString("Other", "LOCKUNLOCK_SND", "sound.object.lockunlock"));
/* 480 */     soundsList.add(new soundString("Other", "EATFOOD_SND", "sound.food.eat"));
/* 481 */     soundsList.add(new soundString("Other", "BRANCHSNAP_SND", "sound.forest.branchsnap"));
/* 482 */     soundsList.add(new soundString("Other", "FIREWORKS_SND", "sound.object.fzz"));
/* 483 */     soundsList.add(new soundString("Other", "PICK_BREAK_SND", "sound.object.lockpick.break.ogg"));
/* 484 */     soundsList.add(new soundString("Other", "DOOR_OPEN_SND", "sound.door.open"));
/* 485 */     soundsList.add(new soundString("Other", "DOOR_CLOSE_SND", "sound.door.close"));
/* 486 */     soundsList.add(new soundString("Other", "SPOT_SBOAT_MOVING_LEFT1", "sound.7.3.007.0001.001"));
/* 487 */     soundsList.add(new soundString("Other", "SPOT_SBOAT_STILL_LEFT1", "sound.7.3.007.0002.001"));
/* 488 */     soundsList.add(new soundString("Other", "SPOT_LBOAT_STILL_LEFT1", "sound.7.3.007.0003.001"));
/* 489 */     soundsList.add(new soundString("Other", "SPOT_LBOAT_MOVING_LEFT1", "sound.7.3.007.0004.001"));
/* 490 */     soundsList.add(new soundString("Other", "SPOT_ROWBOAT_STILL_LEFT1", "sound.7.3.007.0006.001"));
/* 491 */     soundsList.add(new soundString("Other", "SPOT_ROWBOAT_MOVING_LEFT1", "sound.7.3.007.0005.001"));
/* 492 */     soundsList.add(new soundString("Other", "SPOT_SBOAT_MOVING_RIGHT1", "sound.7.4.007.0001.001"));
/* 493 */     soundsList.add(new soundString("Other", "SPOT_SBOAT_STILL_RIGHT1", "sound.7.4.007.0002.001"));
/* 494 */     soundsList.add(new soundString("Other", "SPOT_LBOAT_STILL_RIGHT1", "sound.7.4.007.0003.001"));
/* 495 */     soundsList.add(new soundString("Other", "SPOT_LBOAT_MOVING_RIGHT1", "sound.7.4.007.0004.001"));
/* 496 */     soundsList.add(new soundString("Other", "SPOT_ROWBOAT_STILL_RIGHT1", "sound.7.4.007.0006.001"));
/* 497 */     soundsList.add(new soundString("Other", "SPOT_ROWBOAT_MOVING_RIGHT1", "sound.7.4.007.0005.001"));
/*     */     
/* 499 */     soundsList.add(new soundString("Religion", "RELIGION_CHANNEL_SND", "sound.religion.channel"));
/* 500 */     soundsList.add(new soundString("Religion", "RELIGION_PRAYER_SND", "sound.religion.prayer"));
/* 501 */     soundsList.add(new soundString("Religion", "RELIGION_DESECRATE_SND", "sound.religion.desecrate"));
/* 502 */     soundsList.add(new soundString("Religion", "RELIGION_PREACH_SND", "sound.religion.preach"));
/*     */     
/* 504 */     soundsList.add(new soundString("Trap", "TRAP_SET_SND", "sound.trap.set"));
/* 505 */     soundsList.add(new soundString("Trap", "TRAP_DISARM_SND", "sound.trap.disarm"));
/* 506 */     soundsList.add(new soundString("Trap", "TRAP_THUK_SND", "sound.trap.thuk"));
/* 507 */     soundsList.add(new soundString("Trap", "TRAP_SWISH_SND", "sound.trap.swish"));
/* 508 */     soundsList.add(new soundString("Trap", "TRAP_WHAM_SND", "sound.trap.wham"));
/* 509 */     soundsList.add(new soundString("Trap", "TRAP_SCITH_SND", "sound.trap.scith"));
/* 510 */     soundsList.add(new soundString("Trap", "TRAP_CHAK_SND", "sound.trap.chak"));
/* 511 */     soundsList.add(new soundString("Trap", "TRAP_SPLASH_SND", "sound.trap.splash"));
/*     */     
/* 513 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_RAIN_1_LEFT", "sound.3.3.013.0004.001"));
/* 514 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_WIND_1_LEFT", "sound.3.3.013.0007.001"));
/* 515 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_THUNDER_1_LEFT", "sound.3.3.013.0006.001"));
/* 516 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_RAIN_1_RIGHT", "sound.3.4.013.0004.001"));
/* 517 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_WIND_1_RIGHT", "sound.3.4.013.0007.001"));
/* 518 */     soundsList.add(new soundString("Weather", "WEATHER_FOREST_THUNDER_1_RIGHT", "sound.3.4.013.0006.001"));
/* 519 */     soundsList.add(new soundString("Weather", "WEATHER_LAKE_WIND_1_LEFT", "sound.3.3.014.0004.001"));
/* 520 */     soundsList.add(new soundString("Weather", "WEATHER_LAKE_WIND_1_RIGHT", "sound.3.4.014.0004.001"));
/* 521 */     soundsList.add(new soundString("Weather", "WEATHER_LAKE_RAIN_1_LEFT", "sound.3.3.014.0005.001"));
/* 522 */     soundsList.add(new soundString("Weather", "WEATHER_LAKE_RAIN_1_RIGHT", "sound.3.4.014.0005.001"));
/* 523 */     soundsList.add(new soundString("Weather", "WEATHER_MID_DIST_THUNDER_LEFT", "sound.3.3.002.0005.001"));
/* 524 */     soundsList.add(new soundString("Weather", "WEATHER_LIGHT_BREEZE_LEFT", "sound.3.3.002.0002.001"));
/* 525 */     soundsList.add(new soundString("Weather", "WEATHER_DISTANT_THUNDER_1_LEFT", "sound.3.3.002.0001.001"));
/* 526 */     soundsList.add(new soundString("Weather", "WEATHER_DISTANT_THUNDER_1_RIGHT", "sound.3.4.002.0001.001"));
/* 527 */     soundsList.add(new soundString("Weather", "WEATHER_DISTANT_THUNDER_2_LEFT", "sound.3.3.002.0001.002"));
/* 528 */     soundsList.add(new soundString("Weather", "WEATHER_DISTANT_THUNDER_2_RIGHT", "sound.3.4.002.0001.002"));
/* 529 */     soundsList.add(new soundString("Weather", "WEATHER_MID_DIST_THUNDER_RIGHT", "sound.3.4.002.0005.001"));
/* 530 */     soundsList.add(new soundString("Weather", "WEATHER_LIGHT_BREEZE_RIGHT", "sound.3.4.002.0002.001"));
/*     */     
/* 532 */     soundsList.add(new soundString("Work", "HAMMERONWOOD1_SND", "sound.work.carpentry.mallet1"));
/* 533 */     soundsList.add(new soundString("Work", "HAMMERONWOOD2_SND", "sound.work.carpentry.mallet2"));
/* 534 */     soundsList.add(new soundString("Work", "CARPENTRY_SAW_SND", "sound.work.carpentry.saw"));
/* 535 */     soundsList.add(new soundString("Work", "CARPENTRY_RASP_SND", "sound.work.carpentry.rasp"));
/* 536 */     soundsList.add(new soundString("Work", "CARPENTRY_KNIFE_SND", "sound.work.carpentry.carvingknife"));
/* 537 */     soundsList.add(new soundString("Work", "CARPENTRY_POLISH_SND", "sound.work.carpentry.polish"));
/* 538 */     soundsList.add(new soundString("Work", "SMITHING_HAMMER_SND", "sound.work.smithing.hammer"));
/* 539 */     soundsList.add(new soundString("Work", "SMITHING_WHET_SND", "sound.work.smithing.whetstone"));
/* 540 */     soundsList.add(new soundString("Work", "SMITHING_TEMPER_SND", "sound.work.smithing.temper"));
/* 541 */     soundsList.add(new soundString("Work", "SMITHING_POLISH_SND", "sound.work.smithing.polish"));
/* 542 */     soundsList.add(new soundString("Work", "TAILORING_LOOM_SND", "sound.work.tailoring.loom"));
/* 543 */     soundsList.add(new soundString("Work", "TAILORING_SPINDLE_SND", "sound.work.tailoring.spindle"));
/* 544 */     soundsList.add(new soundString("Work", "FARMING_HARVEST_SND", "sound.work.farming.harvest"));
/* 545 */     soundsList.add(new soundString("Work", "FARMING_SCYTHE_SND", "sound.work.farming.scythe"));
/* 546 */     soundsList.add(new soundString("Work", "FARMING_RAKE_SND", "sound.work.farming.rake"));
/* 547 */     soundsList.add(new soundString("Work", "FIRSTAID_BANDAGE_SND", "sound.work.firstaid.bandage"));
/* 548 */     soundsList.add(new soundString("Work", "PAVING_SND", "sound.work.paving"));
/* 549 */     soundsList.add(new soundString("Work", "PACKING_SND", "sound.work.digging.pack"));
/* 550 */     soundsList.add(new soundString("Work", "WOODCUTTING_KINDLING_SND", "sound.work.woodcutting.kindling"));
/* 551 */     soundsList.add(new soundString("Work", "HAMMERONMETAL_SND", "sound.work.smithing.metal"));
/* 552 */     soundsList.add(new soundString("Work", "HAMMERONSTONE_SND", "sound.work.masonry"));
/* 553 */     soundsList.add(new soundString("Work", "DIGGING1_SND", "sound.work.digging1"));
/* 554 */     soundsList.add(new soundString("Work", "DIGGING2_SND", "sound.work.digging2"));
/* 555 */     soundsList.add(new soundString("Work", "DIGGING3_SND", "sound.work.digging3"));
/* 556 */     soundsList.add(new soundString("Work", "MINING1_SND", "sound.work.mining1"));
/* 557 */     soundsList.add(new soundString("Work", "MINING2_SND", "sound.work.mining2"));
/* 558 */     soundsList.add(new soundString("Work", "MINING3_SND", "sound.work.mining3"));
/* 559 */     soundsList.add(new soundString("Work", "FORAGEBOT_SND", "sound.work.foragebotanize"));
/* 560 */     soundsList.add(new soundString("Work", "PROSPECTING1_SND", "sound.work.prospecting1"));
/* 561 */     soundsList.add(new soundString("Work", "PROSPECTING2_SND", "sound.work.prospecting2"));
/* 562 */     soundsList.add(new soundString("Work", "PROSPECTING3_SND", "sound.work.prospecting3"));
/* 563 */     soundsList.add(new soundString("Work", "STONECUTTING_SND", "sound.work.stonecutting"));
/* 564 */     soundsList.add(new soundString("Work", "GROOMING_SND", "sound.work.horse.groom"));
/* 565 */     soundsList.add(new soundString("Work", "MILKING_SND", "sound.work.milking"));
/* 566 */     soundsList.add(new soundString("Work", "FARMING_SND", "sound.work.farming"));
/* 567 */     soundsList.add(new soundString("Work", "WOODCUTTING1_SND", "sound.work.woodcutting1"));
/* 568 */     soundsList.add(new soundString("Work", "WOODCUTTING2_SND", "sound.work.woodcutting2"));
/* 569 */     soundsList.add(new soundString("Work", "WOODCUTTING3_SND", "sound.work.woodcutting3"));
/* 570 */     soundsList.add(new soundString("Work", "FLINTSTEEL_SND", "sound.fire.lighting.flintsteel"));
/* 571 */     soundsList.add(new soundString("Work", "TOOL_BUTCHERS_KNIFE", "sound.butcherKnife"));
/* 572 */     soundsList.add(new soundString("Work", "TOOL_FORK", "sound.forkMix"));
/* 573 */     soundsList.add(new soundString("Work", "TOOL_MORTAR_AND_PESTLE", "sound.grindSpice"));
/* 574 */     soundsList.add(new soundString("Work", "TOOL_GRINDSTONE", "sound.grindstone"));
/* 575 */     soundsList.add(new soundString("Work", "TOOL_KNIFE", "sound.knifeChop"));
/* 576 */     soundsList.add(new soundString("Work", "TOOL_PRESS", "sound.press"));
/*     */ 
/*     */     
/* 579 */     this.soundStrings = soundsList.<soundString>toArray(new soundString[soundsList.size()]);
/* 580 */     this.catStrings = new String[] { "None", "Ambient", "Arrow", "Bell", "Bird", "Combat", "Death", "Destroy", "Emote", "Fx", "Gnome", "Liquid", "Movement", "Music", "Other", "Religion", "Trap", "Weather", "Work" };
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
/*     */   public void answer(Properties aAnswers) {
/* 592 */     setAnswer(aAnswers);
/*     */     
/* 594 */     boolean back = getBooleanProp("back");
/* 595 */     boolean close = getBooleanProp("close");
/* 596 */     if (back || close) {
/*     */       
/* 598 */       if (this.root != null)
/*     */       {
/* 600 */         this.root.cloneAndSendManageEffect(null);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 605 */     boolean filter = getBooleanProp("filter");
/* 606 */     if (filter) {
/*     */       
/* 608 */       this.showCat = getIntProp("cat");
/* 609 */       reshow();
/*     */       
/*     */       return;
/*     */     } 
/* 613 */     boolean playSound = getBooleanProp("playSound");
/* 614 */     if (playSound) {
/*     */ 
/*     */       
/* 617 */       int sel = getIntProp("sel");
/* 618 */       for (soundString ss : this.soundStrings) {
/*     */         
/* 620 */         if (ss.getId() == sel) {
/*     */           
/* 622 */           this.selected = ss.getSoundName();
/* 623 */           SoundPlayer.playSound(this.selected, getResponder(), 1.5F);
/* 624 */           reshow();
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 630 */     boolean select = getBooleanProp("select");
/* 631 */     if (select)
/*     */     {
/*     */       
/* 634 */       if (this.root != null) {
/*     */         
/* 636 */         int sel = getIntProp("sel");
/* 637 */         if (sel == 0) {
/*     */           
/* 639 */           this.root.cloneAndSendManageEffect(null);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 645 */         for (soundString ss : this.soundStrings) {
/*     */           
/* 647 */           if (ss.getId() == sel) {
/*     */             
/* 649 */             this.root.cloneAndSendManageEffect(ss.getSoundName());
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 657 */     for (String key : getAnswer().stringPropertyNames()) {
/*     */       
/* 659 */       if (key.startsWith("sort")) {
/*     */ 
/*     */         
/* 662 */         String sid = key.substring(4);
/* 663 */         this.sortBy = Integer.parseInt(sid);
/*     */         break;
/*     */       } 
/*     */     } 
/* 667 */     reshow();
/*     */   }
/*     */ 
/*     */   
/*     */   void reshow() {
/* 672 */     SoundList sl = new SoundList(getResponder(), getTitle(), getQuestion());
/* 673 */     sl.root = this.root;
/* 674 */     sl.selected = this.selected;
/* 675 */     sl.showCat = this.showCat;
/* 676 */     sl.sortBy = this.sortBy;
/* 677 */     sl.sendQuestion();
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
/*     */   public void sendQuestion() {
/* 689 */     StringBuilder buf = new StringBuilder();
/* 690 */     buf.append("border{border{size=\"20,40\";null;null;varray{rescale=\"true\";harray{label{type=\"bold\";text=\"" + this.question + "\"};}harray{label{text=\"show Category \"};dropdown{id=\"cat\";options=\"None,Ambient,Arrow,Bell,Bird,Combat,Death,Destroy,Emote,Fx,Gnome,Liquid,Movement,Music,Other,Religion,Trap,Weather,Work\"default=\"" + this.showCat + "\"}}}varray{harray{label{text=\"           \"};button{text=\"Back\";id=\"back\"};label{text=\" \"}}harray{label{text=\" \"};button{text=\"Apply Filter\";id=\"filter\"};label{text=\" \"}}}null;}null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + 
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
/* 722 */         getId() + "\"}");
/* 723 */     buf.append("closebutton{id=\"close\"};");
/* 724 */     int absSortBy = Math.abs(this.sortBy);
/* 725 */     final int upDown = Integer.signum(this.sortBy);
/*     */     
/* 727 */     switch (absSortBy) {
/*     */ 
/*     */       
/*     */       case 1:
/* 731 */         Arrays.sort(this.soundStrings, new Comparator<soundString>()
/*     */             {
/*     */               
/*     */               public int compare(SoundList.soundString param1, SoundList.soundString param2)
/*     */               {
/* 736 */                 return param1.getCategory().compareTo(param2.getCategory()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 743 */         Arrays.sort(this.soundStrings, new Comparator<soundString>()
/*     */             {
/*     */               
/*     */               public int compare(SoundList.soundString param1, SoundList.soundString param2)
/*     */               {
/* 748 */                 return param1.getName().compareTo(param2.getName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 755 */         Arrays.sort(this.soundStrings, new Comparator<soundString>()
/*     */             {
/*     */               
/*     */               public int compare(SoundList.soundString param1, SoundList.soundString param2)
/*     */               {
/* 760 */                 return param1.getSoundName().compareTo(param2.getSoundName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/*     */     
/* 766 */     buf.append("table{rows=\"1\";cols=\"4\";");
/* 767 */     buf.append("label{text=\"\"};" + 
/* 768 */         colHeader("Category", 1, this.sortBy) + 
/* 769 */         colHeader("Name", 2, this.sortBy) + 
/* 770 */         colHeader("Sound Name", 3, this.sortBy));
/*     */     
/* 772 */     boolean noneSel = true;
/* 773 */     for (soundString ss : this.soundStrings) {
/*     */       
/* 775 */       boolean show = (this.showCat == 0);
/* 776 */       boolean sel = this.selected.equals(ss.getSoundName());
/* 777 */       if (sel) {
/*     */         
/* 779 */         noneSel = false;
/*     */         
/* 781 */         show = true;
/*     */       } 
/* 783 */       if (!show && ss.getCategory().equals(this.catStrings[this.showCat]))
/*     */       {
/* 785 */         show = true;
/*     */       }
/* 787 */       if (show)
/*     */       {
/* 789 */         buf.append("radio{group=\"sel\";id=\"" + ss.getId() + "\";selected=\"" + sel + "\";text=\"\"}label{text=\"" + ss
/* 790 */             .getCategory() + "\"};label{text=\"" + ss
/* 791 */             .getName() + "\"};label{text=\"" + ss
/* 792 */             .getSoundName() + "\"};");
/*     */       }
/*     */     } 
/*     */     
/* 796 */     buf.append("}");
/* 797 */     buf.append("radio{group=\"sel\";id=\"0\";selected=\"" + noneSel + "\";text=\"None\"}");
/* 798 */     buf.append("}};null;");
/*     */     
/* 800 */     buf.append("varray{rescale=\"true\";");
/*     */     
/* 802 */     buf.append("text{text=\"Select sound and choose what to do\"}");
/* 803 */     buf.append("harray{button{id=\"select\";text=\"Select\"};label{text=\"  \"};button{id=\"playSound\";text=\"Play sound\"};}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 808 */     buf.append("}");
/* 809 */     buf.append("}");
/* 810 */     getResponder().getCommunicator().sendBml(500, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRoot(MissionManager aRoot) {
/* 820 */     this.root = aRoot;
/*     */   }
/*     */ 
/*     */   
/*     */   void setSelected(String soundName) {
/* 825 */     this.selected = soundName;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getIntProp(String key) {
/* 830 */     String svalue = getStringProp(key);
/* 831 */     int value = 0;
/*     */     
/*     */     try {
/* 834 */       value = Integer.parseInt(svalue);
/*     */     }
/* 836 */     catch (NumberFormatException nfe) {
/*     */       
/* 838 */       return 0;
/*     */     } 
/* 840 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   class soundString
/*     */   {
/*     */     private final int id;
/*     */     private final String category;
/*     */     private final String name;
/*     */     private final String soundName;
/*     */     
/*     */     soundString(String category, String name, String soundName) {
/* 852 */       this.id = SoundList.this.nextId++;
/* 853 */       this.category = category;
/* 854 */       this.name = name;
/* 855 */       this.soundName = soundName;
/*     */     }
/*     */ 
/*     */     
/*     */     int getId() {
/* 860 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     String getCategory() {
/* 865 */       return this.category;
/*     */     }
/*     */ 
/*     */     
/*     */     String getName() {
/* 870 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     String getSoundName() {
/* 875 */       return this.soundName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */