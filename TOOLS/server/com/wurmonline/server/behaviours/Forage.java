/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.GrassData;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
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
/*     */ public enum Forage
/*     */ {
/*  36 */   GSHORT_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/*  37 */   GSHORT_CORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  38 */   GSHORT_COTTON(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 15),
/*  39 */   GSHORT_LINGONBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/*  40 */   GSHORT_ONION(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/*  41 */   GSHORT_POTATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  42 */   GSHORT_PUMPKIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/*  43 */   GSHORT_STRAWBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  44 */   GSHORT_WEMP_PLANT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/*  45 */   GSHORT_EASTER_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 466, (byte)0, 0, 0, -5, -5, ModifiedBy.EASTER, 20),
/*  46 */   GSHORT_RICE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 40),
/*  47 */   GSHORT_IVY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 917, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  48 */   GSHORT_HOPS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 1275, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  49 */   GSHORT_ROCK(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 684, (byte)0, 4, 0, 0, 4, ModifiedBy.NOTHING, 0),
/*     */   
/*  51 */   GSHORT_SPROUT_HAZELNUT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 266, (byte)71, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  52 */   GSHORT_SPROUT_ORANGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 266, (byte)88, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  53 */   GSHORT_SPROUT_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 266, (byte)90, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  54 */   GSHORT_SPROUT_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 266, (byte)91, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*     */   
/*  56 */   GSHORT_CAROT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1133, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  57 */   GSHORT_CABBAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1134, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/*  58 */   GSHORT_TOMATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1135, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  59 */   GSHORT_SUGARBEET(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1136, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  60 */   GSHORT_LETTUCE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1137, (byte)0, 2, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  61 */   GSHORT_PEAPOD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1138, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  62 */   GSHORT_COCOABEAN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  63 */   GSHORT_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  64 */   GSHORT_CUCUMBER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  65 */   GSHORT_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)570, 464, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/*  67 */   GMED_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/*  68 */   GMED_CORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  69 */   GMED_COTTON(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/*  70 */   GMED_LINGONBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/*  71 */   GMED_ONION(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/*  72 */   GMED_POTATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  73 */   GMED_PUMPKIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/*  74 */   GMED_STRAWBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  75 */   GMED_WEMP_PLANT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/*  76 */   GMED_EASTER_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)571, 466, (byte)0, 0, 0, -5, -5, ModifiedBy.EASTER, 20),
/*  77 */   GMED_RICE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 30),
/*  78 */   GMED_IVY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 917, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  79 */   GMED_HOPS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 1275, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*  80 */   GMED_ROCK(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 684, (byte)0, 4, 0, 0, 4, ModifiedBy.NOTHING, 0),
/*     */   
/*  82 */   GMED_CAROT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1133, (byte)0, 3, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  83 */   GMED_CABBAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1134, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  84 */   GMED_TOMATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1135, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/*  85 */   GMED_SUGARBEET(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1136, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  86 */   GMED_LETTUCE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1137, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  87 */   GMED_PEAPOD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1138, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  88 */   GMAD_COCOABEAN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/*  89 */   GMED_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  90 */   GMED_CUCUMBER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*  91 */   GMED_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 464, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/*  93 */   GMED_SPROUT_HAZELNUT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 266, (byte)71, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  94 */   GMED_SPROUT_ORANGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 266, (byte)88, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  95 */   GMED_SPROUT_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 266, (byte)90, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*  96 */   GMED_SPROUT_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)570, 266, (byte)91, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*     */   
/*  98 */   GTALL_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/*  99 */   GTALL_CORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 100 */   GTALL_COTTON(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/*     */   
/* 102 */   GTALL_LINGONBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 103 */   GTALL_ONION(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 104 */   GTALL_POTATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 105 */   GTALL_PUMPKIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 106 */   GTALL_STRAWBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 107 */   GTALL_WEMP_PLANT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/* 108 */   GTALL_EASTER_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)571, 466, (byte)0, 0, 0, -5, -5, ModifiedBy.EASTER, 20),
/* 109 */   GTALL_RICE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/* 110 */   GTALL_IVY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 917, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 111 */   GTALL_HOPS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 1275, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 112 */   GTALL_ROCK(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 684, (byte)0, 4, 0, 0, 4, ModifiedBy.NOTHING, 0),
/*     */   
/* 114 */   GTALL_CAROT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1133, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 115 */   GTALL_CABBAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1134, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 116 */   GTALL_TOMATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1135, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 117 */   GTALL_SUGARBEET(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1136, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 118 */   GTALL_LETTUCE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1137, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 119 */   GTALL_PEAPOD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1138, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 120 */   GTALL_COCOABEAN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 121 */   GTALL_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 122 */   GTALL_CUCUMBER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 123 */   GTALL_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 464, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 125 */   GTALL_SPROUT_HAZELNUT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 266, (byte)71, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 126 */   GTALL_SPROUT_ORANGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 266, (byte)88, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 127 */   GTALL_SPROUT_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 266, (byte)90, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 128 */   GTALL_SPROUT_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)570, 266, (byte)91, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*     */   
/* 130 */   GWILD_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 131 */   GWILD_CORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 132 */   GWILD_COTTON(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/*     */   
/* 134 */   GWILD_LINGONBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 135 */   GWILD_ONION(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 136 */   GWILD_POTATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 137 */   GWILD_PUMPKIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 138 */   GWILD_STRAWBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 139 */   GWILD_WEMP_PLANT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/* 140 */   GWILD_EASTER_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)571, 466, (byte)0, 0, 0, -5, -5, ModifiedBy.EASTER, 20),
/* 141 */   GWILD_RICE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 10),
/* 142 */   GWILD_IVY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 917, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 143 */   GWILD_HOPS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 1275, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 144 */   GWILD_ROCK(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 684, (byte)0, 4, 0, 0, 4, ModifiedBy.NOTHING, 0),
/*     */   
/* 146 */   GWILD_CAROT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1133, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 147 */   GWILD_CABBAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1134, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 148 */   GWILD_TOMATO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1135, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 149 */   GWILD_SUGARBEET(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1136, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 150 */   GWILD_LETTUCE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1137, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 151 */   GWILD_PEAPOD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1138, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 152 */   GWILD_COCOABEAN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 153 */   GWILD_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 154 */   GWILD_CUCUMBER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 155 */   GWILD_EGG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 464, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 157 */   GWILD_SPROUT_HAZELNUT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 266, (byte)71, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 158 */   GWILD_SPROUT_ORANGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 266, (byte)88, 5, 1, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 159 */   GWILD_SPROUT_RASPBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 266, (byte)90, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/* 160 */   GWILD_SPROUT_BLUEBERRY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)570, 266, (byte)91, 10, 10, -10, -10, ModifiedBy.NEAR_BUSH, 10),
/*     */   
/* 162 */   STEPPE_BLUEBERRY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 163 */   STEPPE_CORN(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 164 */   STEPPE_COTTON(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 165 */   STEPPE_LINGONBERRY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 166 */   STEPPE_ONION(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 167 */   STEPPE_POTATO(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 168 */   STEPPE_PUMPKIN(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 169 */   STEPPE_STRAWBERRY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 170 */   STEPPE_WEMP_PLANT(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/* 171 */   STEPPE_RICE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/*     */   
/* 173 */   STEPPE_CAROT(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1133, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 174 */   STEPPE_CABBAGE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1134, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 175 */   STEPPE_TOMATO(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1135, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 176 */   STEPPE_SUGARBEET(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1136, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 177 */   STEPPE_LETTUCE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1137, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 178 */   STEPPE_PEAPOD(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1138, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 179 */   STEPPE_COCOABEAN(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 180 */   STEPPE_RASPBERRY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 181 */   STEPPE_CUCUMBER(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 183 */   TUNDRA_BLUEBERRY(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 25, 25, 10, 10, ModifiedBy.NO_TREES, 10),
/* 184 */   TUNDRA_COTTON(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 185 */   TUNDRA_LINGONBERRY(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)571, 367, (byte)0, 20, 20, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 186 */   TUNDRA_STRAWBERRY(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)571, 362, (byte)0, 15, 15, -5, -5, ModifiedBy.HUNGER, 10),
/* 187 */   TUNDRA_COCOABEAN(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 188 */   TUNDRA_RASPBERRY(Tiles.Tile.TILE_TUNDRA.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 190 */   MARSH_BLUEBERRY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 191 */   MARSH_CORN(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 32, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 192 */   MARSH_COTTON(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 193 */   MARSH_MUSHROOM_RED(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 194 */   MARSH_ONION(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 195 */   MARSH_POTATO(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 196 */   MARSH_PUMPKIN(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 33, (byte)0, 15, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 197 */   MARSH_STRAWBERRY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)571, 362, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 20),
/* 198 */   MARSH_WEMP_PLANT(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)570, 316, (byte)0, 10, 10, -5, -5, ModifiedBy.NO_TREES, 10),
/* 199 */   MARSH_RICE(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 50),
/* 200 */   MARSH_SUGARBEET(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 1136, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 201 */   MARSH_COCOABEAN(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 202 */   MARSH_RASPBERRY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/* 203 */   MARSH_CUCUMBER(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)569, 1247, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 205 */   TSHORT_BLUEBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 206 */   TSHORT_BRANCH(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)570, 688, (byte)0, 18, 0, 0, 18, ModifiedBy.NOTHING, 0),
/* 207 */   TSHORT_CORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 208 */   TSHORT_COTTON(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 209 */   TSHORT_LINGONBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 210 */   TSHORT_MUSHROOM_BLACK(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 211 */   TSHORT_MUSHROOM_BLUE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 212 */   TSHORT_MUSHROOM_BROWN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 213 */   TSHORT_MUSHROOM_GREEN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 214 */   TSHORT_MUSHROOM_RED(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 215 */   TSHORT_MUSHROOM_YELLOW(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 216 */   TSHORT_ONION(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 217 */   TSHORT_POTATO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 218 */   TSHORT_RICE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/* 219 */   TSHORT_COCOABEAN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 220 */   TSHORT_RASPBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 222 */   TMED_BLUEBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 223 */   TMED_BRANCH(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)570, 688, (byte)0, 18, 0, 0, 18, ModifiedBy.NOTHING, 0),
/* 224 */   TMED_CORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 225 */   TMED_COTTON(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 226 */   TMED_LINGONBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 227 */   TMED_MUSHROOM_BLACK(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 228 */   TMED_MUSHROOM_BLUE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 229 */   TMED_MUSHROOM_BROWN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 230 */   TMED_MUSHROOM_GREEN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 231 */   TMED_MUSHROOM_RED(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 232 */   TMED_MUSHROOM_YELLOW(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 233 */   TMED_ONION(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 234 */   TMED_POTATO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 235 */   TMED_RICE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/* 236 */   TMED_COCOABEAN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)570, 1155, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 237 */   TMED_RASPBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 239 */   TTALL_BLUEBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 240 */   TTALL_BRANCH(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)570, 688, (byte)0, 18, 0, 0, 18, ModifiedBy.NOTHING, 0),
/* 241 */   TTALL_CORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 242 */   TTALL_COTTON(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 243 */   TTALL_LINGONBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 244 */   TTALL_MUSHROOM_BLACK(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 245 */   TTALL_MUSHROOM_BLUE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 246 */   TTALL_MUSHROOM_BROWN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 247 */   TTALL_MUSHROOM_GREEN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 248 */   TTALL_MUSHROOM_RED(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 249 */   TTALL_MUSHROOM_YELLOW(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 250 */   TTALL_ONION(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 251 */   TTALL_POTATO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 252 */   TTALL_RICE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/* 253 */   TTALL_COCOABEAN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)570, 1155, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 254 */   TTALL_RASPBERRY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 256 */   BSHORT_BLUEBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 257 */   BSHORT_CORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 258 */   BSHORT_COTTON(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 259 */   BSHORT_LINGONBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 260 */   BSHORT_MUSHROOM_BLACK(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 261 */   BSHORT_MUSHROOM_BLUE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 262 */   BSHORT_MUSHROOM_BROWN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 263 */   BSHORT_MUSHROOM_GREEN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 264 */   BSHORT_MUSHROOM_RED(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 265 */   BSHORT_MUSHROOM_YELLOW(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 266 */   BSHORT_ONION(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 267 */   BSHORT_POTATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 268 */   BSHORT_RICE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/*     */   
/* 270 */   BSHORT_TOMATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 1135, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 271 */   BSHORT_SUGARBEET(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 1136, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 272 */   BSHORT_PEAPOD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)569, 1138, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 273 */   BSHORT_COCOABEAN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)570, 1155, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 274 */   BSHORT_RASPBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 276 */   BMED_BLUEBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 277 */   BMED_CORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 278 */   BMED_COTTON(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 279 */   BMED_LINGONBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 280 */   BMED_MUSHROOM_BLACK(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 281 */   BMED_MUSHROOM_BLUE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 282 */   BMED_MUSHROOM_BROWN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 283 */   BMED_MUSHROOM_GREEN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 284 */   BMED_MUSHROOM_RED(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 285 */   BMED_MUSHROOM_YELLOW(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 286 */   BMED_ONION(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 287 */   BMED_POTATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 288 */   BMED_RICE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/*     */   
/* 290 */   BMED_TOMATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 1135, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 291 */   BMED_SUGARBEET(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 1136, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 292 */   BMED_PEAPOD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)569, 1138, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 293 */   BMED_COCOABEAN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 294 */   BMED_RASPBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24),
/*     */   
/* 296 */   BTALL_BLUEBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)571, 364, (byte)0, 5, 5, 10, 10, ModifiedBy.NOTHING, 0),
/* 297 */   BTALL_CORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 32, (byte)0, 12, 12, -5, -5, ModifiedBy.NOTHING, 0),
/* 298 */   BTALL_COTTON(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)570, 144, (byte)0, 5, 5, -5, -5, ModifiedBy.WOUNDED, 12),
/* 299 */   BTALL_LINGONBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)571, 367, (byte)0, 6, 6, -5, -5, ModifiedBy.NEAR_BUSH, 12),
/* 300 */   BTALL_MUSHROOM_BLACK(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 247, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 301 */   BTALL_MUSHROOM_BLUE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 250, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 302 */   BTALL_MUSHROOM_BROWN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 248, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 303 */   BTALL_MUSHROOM_GREEN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 246, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 304 */   BTALL_MUSHROOM_RED(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 251, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 305 */   BTALL_MUSHROOM_YELLOW(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 249, (byte)0, 0, 0, -5, -5, ModifiedBy.NEAR_TREE, 12),
/* 306 */   BTALL_ONION(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 355, (byte)0, 1, 20, -5, -5, ModifiedBy.NOTHING, 0),
/* 307 */   BTALL_POTATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 35, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 308 */   BTALL_RICE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 746, (byte)0, 1, 1, -10, -10, ModifiedBy.NEAR_WATER, 20),
/*     */   
/* 310 */   BTALL_TOMATO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 1135, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 311 */   BTALL_SUGARBEET(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 1136, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 312 */   BTALL_PEAPOD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)569, 1138, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 313 */   BTALL_COCOABEAN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)570, 1155, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 314 */   BTALL_RASPBERRY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)571, 1196, (byte)0, 5, 5, -5, -5, ModifiedBy.HUNGER, 24);
/*     */   
/*     */   private final byte tileType;
/*     */   
/*     */   private final GrassData.GrowthStage grassLength;
/*     */   
/*     */   private final short category;
/*     */   
/*     */   private final int itemType;
/*     */   
/*     */   private final byte material;
/*     */   
/*     */   private final int chanceAt1;
/*     */   private final int chanceAt100;
/*     */   private final int difficultyAt1;
/*     */   private final int difficultyAt100;
/*     */   private final ModifiedBy modifiedBy;
/*     */   private final int chanceModifier;
/*     */   
/*     */   Forage(byte aTileType, GrassData.GrowthStage aGrassLength, short aCategory, int aItemType, byte aMaterial, int aChanceAt1, int aChanceAt100, int aDifficultyAt1, int aDifficultyAt100, ModifiedBy aModifiedBy, int aChanceModifier) {
/* 334 */     this.tileType = aTileType;
/* 335 */     this.grassLength = aGrassLength;
/* 336 */     this.category = aCategory;
/* 337 */     this.itemType = aItemType;
/* 338 */     this.material = aMaterial;
/* 339 */     this.chanceAt1 = aChanceAt1;
/* 340 */     this.chanceAt100 = aChanceAt100;
/* 341 */     this.difficultyAt1 = aDifficultyAt1;
/* 342 */     this.difficultyAt100 = aDifficultyAt100;
/* 343 */     this.modifiedBy = aModifiedBy;
/* 344 */     this.chanceModifier = aChanceModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItem() {
/* 353 */     return this.itemType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDifficultyAt(int knowledge) {
/* 363 */     float diff = (this.difficultyAt1 + (this.difficultyAt100 - this.difficultyAt1) / 100 * knowledge);
/*     */ 
/*     */     
/* 366 */     if (diff < 0.0F) {
/* 367 */       return knowledge + diff;
/*     */     }
/* 369 */     return diff;
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
/*     */   public static float getQL(double power, int knowledge) {
/* 383 */     return Math.min(100.0F, knowledge + (100 - knowledge) * (float)power / 500.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getChanceAt(Creature performer, int knowledge, int tilex, int tiley) {
/* 389 */     float chance = (this.chanceAt1 + (this.chanceAt100 - this.chanceAt1) / 100 * knowledge);
/* 390 */     return chance + this.modifiedBy.chanceModifier(performer, this.chanceModifier, tilex, tiley);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMaterial() {
/* 395 */     return this.material;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Forage getRandomForage(Creature performer, byte aTileType, GrassData.GrowthStage aGrassLength, short aCategory, int knowledge, int tilex, int tiley) {
/* 405 */     byte checkType = aTileType;
/* 406 */     Tiles.Tile theTile = Tiles.getTile(aTileType);
/* 407 */     if (theTile.isMycelium())
/* 408 */       checkType = Tiles.Tile.TILE_GRASS.id; 
/* 409 */     if (theTile.isNormalTree() || theTile.isMyceliumTree())
/* 410 */       checkType = Tiles.Tile.TILE_TREE.id; 
/* 411 */     if (theTile.isNormalBush() || theTile.isMyceliumBush()) {
/* 412 */       checkType = Tiles.Tile.TILE_BUSH.id;
/*     */     }
/* 414 */     float totalChance = 0.0F;
/* 415 */     for (Forage f : values()) {
/*     */       
/* 417 */       if (f.tileType == checkType)
/*     */       {
/* 419 */         if (f.grassLength == aGrassLength) {
/*     */           
/* 421 */           float chance = f.getChanceAt(performer, knowledge, tilex, tiley);
/* 422 */           if (chance >= 0.0F) {
/* 423 */             totalChance += chance;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 428 */     if (totalChance == 0.0F) {
/* 429 */       return null;
/*     */     }
/* 431 */     int rndChance = Server.rand.nextInt((int)totalChance);
/*     */ 
/*     */     
/* 434 */     float runningChance = 0.0F;
/* 435 */     for (Forage f : values()) {
/*     */       
/* 437 */       if (f.tileType == checkType)
/*     */       {
/* 439 */         if (checkType != Tiles.Tile.TILE_GRASS.id || f.grassLength == aGrassLength) {
/*     */           
/* 441 */           float chance = f.getChanceAt(performer, knowledge, tilex, tiley);
/* 442 */           if (chance >= 0.0F) {
/*     */             
/* 444 */             runningChance += chance;
/* 445 */             if (rndChance < runningChance) {
/*     */               
/* 447 */               if (aCategory == 223 || aCategory == f.category) {
/* 448 */                 return f;
/*     */               }
/* 450 */               return null;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 456 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getDifficulty(int templateId, int knowledge) {
/* 466 */     for (Forage f : values()) {
/*     */       
/* 468 */       if (f.tileType == Tiles.Tile.TILE_GRASS.id && f.grassLength == GrassData.GrowthStage.SHORT && f.itemType == templateId)
/*     */       {
/* 470 */         return f.getDifficultyAt(knowledge);
/*     */       }
/*     */     } 
/* 473 */     return -1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Forage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */