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
/*     */ public enum Herb
/*     */ {
/*  36 */   GSHORT_ACORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 20, 15, ModifiedBy.NEAR_OAK, 50),
/*  37 */   GSHORT_BARLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 10),
/*  38 */   GSHORT_BASIL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 3, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  39 */   GSHORT_BELLADONNA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 3, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  40 */   GSHORT_GALIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 2, 4, -5, -5, ModifiedBy.NOTHING, 0),
/*  41 */   GSHORT_LOVAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  42 */   GSHORT_NETTLES(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 2, 5, -5, -5, ModifiedBy.WOUNDED, 5),
/*  43 */   GSHORT_OAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*  44 */   GSHORT_OREGANO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 6, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  45 */   GSHORT_PARSLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 5),
/*  46 */   GSHORT_ROSMARY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  47 */   GSHORT_RYE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*  48 */   GSHORT_SAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 15),
/*  49 */   GSHORT_SASSAFRAS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  50 */   GSHORT_THYME(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 6, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  51 */   GSHORT_WHEAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 5),
/*  52 */   GSHORT_WOAD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 1, 6, 20, 15, ModifiedBy.NO_TREES, 10),
/*     */   
/*  54 */   GSHORT_CUMIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)720, 1140, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  55 */   GSHORT_GINGER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)720, 1141, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  56 */   GSHORT_NUTMEG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 2, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  57 */   GSHORT_PAPRIKA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)720, 1143, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  58 */   GSHORT_TURMERIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)720, 1144, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  59 */   GSHORT_MINT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)573, 1130, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  60 */   GSHORT_FENNEL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.SHORT, (short)575, 1132, (byte)0, 5, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/*  62 */   GMED_ACORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)575, 436, (byte)0, 10, 10, 20, 10, ModifiedBy.NEAR_OAK, 50),
/*  63 */   GMED_BARLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 10),
/*  64 */   GMED_BASIL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 359, (byte)0, 3, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  65 */   GMED_BELLADONNA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 361, (byte)0, 3, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  66 */   GMED_GALIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)575, 356, (byte)0, 3, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  67 */   GMED_LOVAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 353, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 20),
/*  68 */   GMED_NETTLES(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)575, 365, (byte)0, 2, 5, -5, -5, ModifiedBy.WOUNDED, 10),
/*  69 */   GMED_OAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*  70 */   GMED_OREGANO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 357, (byte)0, 6, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  71 */   GMED_PARSLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 358, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 8),
/*  72 */   GMED_ROSMARY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 363, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 15),
/*  73 */   GMED_RYE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*  74 */   GMED_SAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 354, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  75 */   GMED_SASSAFRAS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 366, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  76 */   GMED_THYME(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 360, (byte)0, 6, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  77 */   GMED_WHEAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 8),
/*  78 */   GMED_WOAD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)575, 440, (byte)0, 1, 10, 20, 10, ModifiedBy.NO_TREES, 20),
/*     */   
/*  80 */   GMED_CUMIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)720, 1140, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  81 */   GMED_GINGER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)720, 1141, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  82 */   GMED_NUTMEG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)720, 1142, (byte)0, 2, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  83 */   GMED_PAPRIKA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)720, 1143, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  84 */   GMED_TURMERIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)720, 1144, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  85 */   GMED_MINT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)573, 1130, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  86 */   GMED_FENNEL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.MEDIUM, (short)575, 1132, (byte)0, 5, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/*  88 */   GTALL_ACORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)575, 436, (byte)0, 15, 15, 20, 5, ModifiedBy.NEAR_OAK, 50),
/*  89 */   GTALL_BARLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 10),
/*  90 */   GTALL_BASIL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 359, (byte)0, 3, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*  91 */   GTALL_BELLADONNA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 361, (byte)0, 3, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*  92 */   GTALL_GALIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)575, 356, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*  93 */   GTALL_LOVAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 353, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 15),
/*  94 */   GTALL_NETTLES(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)575, 365, (byte)0, 2, 5, -5, -5, ModifiedBy.WOUNDED, 10),
/*  95 */   GTALL_OAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*  96 */   GTALL_OREGANO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 357, (byte)0, 6, 3, -5, -5, ModifiedBy.NOTHING, 0),
/*  97 */   GTALL_PARSLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 358, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/*  98 */   GTALL_ROSMARY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 363, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 20),
/*  99 */   GTALL_RYE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 100 */   GTALL_SAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 354, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 12),
/* 101 */   GTALL_SASSAFRAS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 366, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 12),
/* 102 */   GTALL_THYME(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 360, (byte)0, 7, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 103 */   GTALL_WHEAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 10),
/* 104 */   GTALL_WOAD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)575, 440, (byte)0, 1, 20, 20, 5, ModifiedBy.NO_TREES, 30),
/*     */   
/* 106 */   GTALL_CUMIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)720, 1140, (byte)0, 4, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 107 */   GTALL_GINGER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)720, 1141, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 108 */   GTALL_NUTMEG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)720, 1142, (byte)0, 2, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 109 */   GTALL_PAPRIKA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)720, 1143, (byte)0, 4, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 110 */   GTALL_TURMERIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)720, 1144, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 111 */   GTALL_MINT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)573, 1130, (byte)0, 6, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 112 */   GTALL_FENNEL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.TALL, (short)575, 1132, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 114 */   GWILD_ACORN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)575, 436, (byte)0, 20, 20, 20, 1, ModifiedBy.NEAR_OAK, 50),
/* 115 */   GWILD_BARLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 5),
/* 116 */   GWILD_BASIL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 359, (byte)0, 3, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 117 */   GWILD_BELLADONNA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 118 */   GWILD_GALIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)575, 356, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 119 */   GWILD_LOVAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 353, (byte)0, 2, 6, -5, -5, ModifiedBy.WOUNDED, 15),
/* 120 */   GWILD_NETTLES(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)575, 365, (byte)0, 3, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 121 */   GWILD_OAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 122 */   GWILD_OREGANO(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 357, (byte)0, 5, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 123 */   GWILD_PARSLEY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 358, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 7),
/* 124 */   GWILD_ROSMARY(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 363, (byte)0, 6, 5, -5, -5, ModifiedBy.WOUNDED, 10),
/* 125 */   GWILD_RYE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 126 */   GWILD_SAGE(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 354, (byte)0, 6, 10, -5, -5, ModifiedBy.WOUNDED, 15),
/* 127 */   GWILD_SASSAFRAS(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 366, (byte)0, 4, 2, -5, -5, ModifiedBy.WOUNDED, 9),
/* 128 */   GWILD_THYME(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 129 */   GWILD_WHEAT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 6),
/* 130 */   GWILD_WOAD(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)575, 440, (byte)0, 1, 40, 20, 1, ModifiedBy.NO_TREES, 40),
/*     */   
/* 132 */   GWILD_CUMIN(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)720, 1140, (byte)0, 2, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 133 */   GWILD_GINGER(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)720, 1141, (byte)0, 2, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 134 */   GWILD_NUTMEG(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)720, 1142, (byte)0, 2, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 135 */   GWILD_PAPRIKA(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)720, 1143, (byte)0, 2, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 136 */   GWILD_TURMERIC(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)720, 1144, (byte)0, 3, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 137 */   GWILD_MINT(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)573, 1130, (byte)0, 6, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 138 */   GWILD_FENNEL(Tiles.Tile.TILE_GRASS.id, GrassData.GrowthStage.WILD, (short)575, 1132, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 140 */   STEPPE_ACORN(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 20, 10, ModifiedBy.NEAR_OAK, 50),
/* 141 */   STEPPE_BARLEY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 7, -5, -5, ModifiedBy.WOUNDED, 7),
/* 142 */   STEPPE_BASIL(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 8, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 143 */   STEPPE_BELLADONNA(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 8, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 144 */   STEPPE_GALIC(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 8, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/* 145 */   STEPPE_LOVAGE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 3, 7, -5, -5, ModifiedBy.WOUNDED, 5),
/* 146 */   STEPPE_NETTLES(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 2, 2, -5, -5, ModifiedBy.WOUNDED, 10),
/* 147 */   STEPPE_OAT(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 148 */   STEPPE_OREGANO(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 3, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 149 */   STEPPE_PARSLEY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 6, 3, -5, -5, ModifiedBy.WOUNDED, 10),
/* 150 */   STEPPE_ROSMARY(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 8, 5, -5, -5, ModifiedBy.WOUNDED, 5),
/* 151 */   STEPPE_RYE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 152 */   STEPPE_SAGE(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 6, 8, -5, -5, ModifiedBy.WOUNDED, 5),
/* 153 */   STEPPE_SASSAFRAS(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 8, 5, -5, -5, ModifiedBy.WOUNDED, 8),
/* 154 */   STEPPE_THYME(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 155 */   STEPPE_WHEAT(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 9),
/* 156 */   STEPPE_WOAD(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 10, 46, 20, 1, ModifiedBy.NO_TREES, 50),
/*     */   
/* 158 */   STEPPE_CUMIN(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)720, 1140, (byte)0, 5, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 159 */   STEPPE_NUTMEG(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 2, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 160 */   STEPPE_PAPRIKA(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)720, 1143, (byte)0, 3, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 161 */   STEPPE_MINT(Tiles.Tile.TILE_STEPPE.id, GrassData.GrowthStage.SHORT, (short)573, 1130, (byte)0, 7, 6, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 163 */   MARSH_ACORN(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 10, 10, ModifiedBy.NEAR_OAK, 50),
/* 164 */   MARSH_BARLEY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 3),
/* 165 */   MARSH_BASIL(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 166 */   MARSH_BELLADONNA(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 167 */   MARSH_GALIC(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 168 */   MARSH_LOVAGE(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.WOUNDED, 4),
/* 169 */   MARSH_NETTLES(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 5, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 170 */   MARSH_OAT(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 171 */   MARSH_OREGANO(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 5, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 172 */   MARSH_PARSLEY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 4, 5, -5, -5, ModifiedBy.WOUNDED, 8),
/* 173 */   MARSH_ROSMARY(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 10, 15, -5, -5, ModifiedBy.WOUNDED, 17),
/* 174 */   MARSH_RYE(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 175 */   MARSH_SAGE(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 3, 10, -5, -5, ModifiedBy.WOUNDED, 10),
/* 176 */   MARSH_SASSAFRAS(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 3, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 177 */   MARSH_THYME(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 178 */   MARSH_WHEAT(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.WOUNDED, 5),
/* 179 */   MARSH_WOAD(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 6, 26, 20, 20, ModifiedBy.NO_TREES, 20),
/*     */   
/* 181 */   MARSH_GINGER(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)720, 1141, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 182 */   MARSH_NUTMEG(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 6, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 183 */   MARSH_TURMERIC(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)720, 1144, (byte)0, 10, 15, -5, -5, ModifiedBy.NOTHING, 0),
/* 184 */   MARSH_FENNEL(Tiles.Tile.TILE_MARSH.id, GrassData.GrowthStage.SHORT, (short)575, 1132, (byte)0, 6, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 186 */   MOSS_ACORN(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 20, 20, ModifiedBy.NEAR_OAK, 50),
/* 187 */   MOSS_BARLEY(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 188 */   MOSS_BASIL(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 189 */   MOSS_BELLADONNA(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 190 */   MOSS_GALIC(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 191 */   MOSS_LOVAGE(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 16, 16, -5, -5, ModifiedBy.WOUNDED, 20),
/* 192 */   MOSS_NETTLES(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 193 */   MOSS_OAT(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 194 */   MOSS_OREGANO(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 195 */   MOSS_PARSLEY(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 196 */   MOSS_ROSMARY(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 9, 9, -5, -5, ModifiedBy.WOUNDED, 5),
/* 197 */   MOSS_RYE(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 198 */   MOSS_SAGE(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 199 */   MOSS_SASSAFRAS(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 200 */   MOSS_THYME(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 201 */   MOSS_WHEAT(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 202 */   MOSS_WOAD(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 6, 26, 20, 20, ModifiedBy.NO_TREES, 32),
/*     */   
/* 204 */   MOSS_CUMIN(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)720, 1140, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 205 */   MOSS_GINGER(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)720, 1141, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 206 */   MOSS_NUTMEG(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 9, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 207 */   MOSS_PAPRIKA(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)720, 1143, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 208 */   MOSS_TURMERIC(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)720, 1144, (byte)0, 3, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 209 */   MOSS_MINT(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)573, 1130, (byte)0, 1, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 210 */   MOSS_FENNEL(Tiles.Tile.TILE_MOSS.id, GrassData.GrowthStage.SHORT, (short)575, 1132, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 212 */   PEAT_ACORN(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 15, 20, 20, ModifiedBy.NEAR_OAK, 40),
/* 213 */   PEAT_BARLEY(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 214 */   PEAT_BASIL(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 215 */   PEAT_BELLADONNA(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 216 */   PEAT_GALIC(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 217 */   PEAT_LOVAGE(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 16, 16, -5, -5, ModifiedBy.WOUNDED, 10),
/* 218 */   PEAT_NETTLES(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 219 */   PEAT_OAT(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 220 */   PEAT_OREGANO(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 221 */   PEAT_PARSLEY(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 222 */   PEAT_ROSMARY(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 15, 15, -5, -5, ModifiedBy.WOUNDED, 10),
/* 223 */   PEAT_RYE(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 224 */   PEAT_SAGE(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 225 */   PEAT_SASSAFRAS(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 226 */   PEAT_THYME(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 227 */   PEAT_WHEAT(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 228 */   PEAT_WOAD(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 6, 6, 20, 20, ModifiedBy.NO_TREES, 40),
/*     */   
/* 230 */   PEAT_CUMIN(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)720, 1140, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 231 */   PEAT_NUTMEG(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 232 */   PEAT_PAPRIKA(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)720, 1143, (byte)0, 6, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 233 */   PEAT_MINT(Tiles.Tile.TILE_PEAT.id, GrassData.GrowthStage.SHORT, (short)573, 1130, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 235 */   TSHORT_ACORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 20, 10, ModifiedBy.NEAR_OAK, 50),
/* 236 */   TSHORT_BARLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 237 */   TSHORT_BASIL(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 238 */   TSHORT_BELLADONNA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 239 */   TSHORT_GALIC(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 240 */   TSHORT_LOVAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 241 */   TSHORT_NETTLES(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 242 */   TSHORT_OAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 243 */   TSHORT_OREGANO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 244 */   TSHORT_PARSLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 245 */   TSHORT_ROSMARY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 246 */   TSHORT_RYE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 247 */   TSHORT_SAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 248 */   TSHORT_SASSAFRAS(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 249 */   TSHORT_THYME(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 250 */   TSHORT_WHEAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 251 */   TSHORT_WOAD(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 6, 16, 30, 20, ModifiedBy.NOTHING, 0),
/*     */   
/* 253 */   TSHORT_CUMIN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)720, 1140, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 254 */   TSHORT_NUTMEG(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 255 */   TSHORT_PAPRIKA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)720, 1142, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 256 */   TSHORT_MINT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.SHORT, (short)573, 1130, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 258 */   TMED_ACORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)575, 436, (byte)0, 5, 10, 20, 5, ModifiedBy.NEAR_OAK, 50),
/* 259 */   TMED_BARLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 260 */   TMED_BASIL(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 261 */   TMED_BELLADONNA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 262 */   TMED_GALIC(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 263 */   TMED_LOVAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 264 */   TMED_NETTLES(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 265 */   TMED_OAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 266 */   TMED_OREGANO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 267 */   TMED_PARSLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 268 */   TMED_ROSMARY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 269 */   TMED_RYE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 270 */   TMED_SAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 271 */   TMED_SASSAFRAS(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 272 */   TMED_THYME(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 273 */   TMED_WHEAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 274 */   TMED_WOAD(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)575, 440, (byte)0, 6, 16, 30, 10, ModifiedBy.NOTHING, 0),
/*     */   
/* 276 */   TMED_CUMIN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)720, 1140, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 277 */   TMED_NUTMEG(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)720, 1142, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 278 */   TMED_PAPRIKA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)720, 1143, (byte)0, 2, 2, -5, -5, ModifiedBy.NOTHING, 0),
/* 279 */   TMED_MINT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.MEDIUM, (short)573, 1130, (byte)0, 5, 8, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 281 */   TTALL_ACORN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)575, 436, (byte)0, 5, 15, 20, 1, ModifiedBy.NEAR_OAK, 50),
/* 282 */   TTALL_BARLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 283 */   TTALL_BASIL(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 284 */   TTALL_BELLADONNA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 285 */   TTALL_GALIC(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 286 */   TTALL_LOVAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 287 */   TTALL_NETTLES(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 288 */   TTALL_OAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 289 */   TTALL_OREGANO(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 290 */   TTALL_PARSLEY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 291 */   TTALL_ROSMARY(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 292 */   TTALL_RYE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 293 */   TTALL_SAGE(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 294 */   TTALL_SASSAFRAS(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 295 */   TTALL_THYME(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 296 */   TTALL_WHEAT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 297 */   TTALL_WOAD(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)575, 440, (byte)0, 6, 16, 30, 1, ModifiedBy.NOTHING, 0),
/*     */   
/* 299 */   TTALL_CUMIN(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)720, 1140, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 300 */   TTALL_NUTMEG(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)720, 1142, (byte)0, 4, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 301 */   TTALL_PAPRIKA(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)720, 1143, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 302 */   TTALL_MINT(Tiles.Tile.TILE_TREE.id, GrassData.GrowthStage.TALL, (short)573, 1130, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 304 */   BSHORT_ACORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)575, 436, (byte)0, 5, 5, 20, 15, ModifiedBy.NEAR_OAK, 50),
/* 305 */   BSHORT_BARLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 306 */   BSHORT_BASIL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 307 */   BSHORT_BELLADONNA(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 308 */   BSHORT_GALIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 309 */   BSHORT_LOVAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 310 */   BSHORT_NETTLES(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 311 */   BSHORT_OAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 312 */   BSHORT_OREGANO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 313 */   BSHORT_PARSLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 314 */   BSHORT_ROSMARY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 315 */   BSHORT_RYE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 316 */   BSHORT_SAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 317 */   BSHORT_SASSAFRAS(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 318 */   BSHORT_THYME(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 319 */   BSHORT_WHEAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 320 */   BSHORT_WOAD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.SHORT, (short)575, 440, (byte)0, 6, 16, 20, 10, ModifiedBy.NOTHING, 0),
/*     */   
/* 322 */   BSHORT_GINGER(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1141, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 323 */   BSHORT_NUTMEG(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1142, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 324 */   BSHORT_TURMERIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1144, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 325 */   BSHORT_MINT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 1130, (byte)0, 5, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 326 */   BSHORT_FENNEL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 1132, (byte)0, 10, 5, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 328 */   BMED_ACORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)575, 436, (byte)0, 5, 10, 20, 10, ModifiedBy.NEAR_OAK, 50),
/* 329 */   BMED_BARLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 330 */   BMED_BASIL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 331 */   BMED_BELLADONNA(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 332 */   BMED_GALIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 333 */   BMED_LOVAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 334 */   BMED_NETTLES(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 335 */   BMED_OAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 336 */   BMED_OREGANO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 337 */   BMED_PARSLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 338 */   BMED_ROSMARY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 339 */   BMED_RYE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 340 */   BMED_SAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 341 */   BMED_SASSAFRAS(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 342 */   BMED_THYME(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 343 */   BMED_WHEAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 344 */   BMED_WOAD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)575, 440, (byte)0, 6, 16, 20, 5, ModifiedBy.NOTHING, 0),
/*     */   
/* 346 */   BMED_GINGER(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)720, 1141, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 347 */   BMED_NUTMEG(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)720, 1142, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 348 */   BMED_TURMERIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)720, 1144, (byte)0, 4, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 349 */   BMED_MINT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)573, 1130, (byte)0, 8, 4, -5, -5, ModifiedBy.NOTHING, 0),
/* 350 */   BMED_FENNEL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.MEDIUM, (short)575, 1132, (byte)0, 4, 4, -5, -5, ModifiedBy.NOTHING, 0),
/*     */   
/* 352 */   BTALL_ACORN(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 436, (byte)0, 5, 15, 20, 1, ModifiedBy.NEAR_OAK, 50),
/* 353 */   BTALL_BARLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)572, 28, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 354 */   BTALL_BASIL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 359, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 355 */   BTALL_BELLADONNA(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 361, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 356 */   BTALL_GALIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 356, (byte)0, 8, 8, -5, -5, ModifiedBy.NOTHING, 0),
/* 357 */   BTALL_LOVAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 353, (byte)0, 6, 6, -5, -5, ModifiedBy.NOTHING, 0),
/* 358 */   BTALL_NETTLES(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 365, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 359 */   BTALL_OAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)572, 31, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 360 */   BTALL_OREGANO(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 357, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 361 */   BTALL_PARSLEY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 358, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 362 */   BTALL_ROSMARY(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 363, (byte)0, 5, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 363 */   BTALL_RYE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)572, 30, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 364 */   BTALL_SAGE(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 354, (byte)0, 10, 10, -5, -5, ModifiedBy.WOUNDED, 20),
/* 365 */   BTALL_SASSAFRAS(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 366, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 366 */   BTALL_THYME(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 360, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 367 */   BTALL_WHEAT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)572, 29, (byte)0, 1, 1, -5, -5, ModifiedBy.NOTHING, 0),
/* 368 */   BTALL_WOAD(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 440, (byte)0, 6, 16, 20, 1, ModifiedBy.NOTHING, 0),
/*     */   
/* 370 */   BTALL_GINGER(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1141, (byte)0, 10, 10, -5, -5, ModifiedBy.NOTHING, 0),
/* 371 */   BTALL_NUTMEG(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1142, (byte)0, 7, 7, -5, -5, ModifiedBy.NOTHING, 0),
/* 372 */   BTALL_TURMERIC(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)720, 1144, (byte)0, 7, 5, -5, -5, ModifiedBy.NOTHING, 0),
/* 373 */   BTALL_MINT(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)573, 1130, (byte)0, 5, 3, -5, -5, ModifiedBy.NOTHING, 0),
/* 374 */   BTALL_FENNEL(Tiles.Tile.TILE_BUSH.id, GrassData.GrowthStage.TALL, (short)575, 1132, (byte)0, 6, 8, -5, -5, ModifiedBy.NOTHING, 0);
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
/*     */   Herb(byte aTileType, GrassData.GrowthStage aGrassLength, short aCategory, int aItemType, byte aMaterial, int aChanceAt1, int aChanceAt100, int aDifficultyAt1, int aDifficultyAt100, ModifiedBy aModifiedBy, int aChanceModifier) {
/* 394 */     this.tileType = aTileType;
/* 395 */     this.grassLength = aGrassLength;
/* 396 */     this.category = aCategory;
/* 397 */     this.itemType = aItemType;
/* 398 */     this.material = aMaterial;
/* 399 */     this.chanceAt1 = aChanceAt1;
/* 400 */     this.chanceAt100 = aChanceAt100;
/* 401 */     this.difficultyAt1 = aDifficultyAt1;
/* 402 */     this.difficultyAt100 = aDifficultyAt100;
/* 403 */     this.modifiedBy = aModifiedBy;
/* 404 */     this.chanceModifier = aChanceModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItem() {
/* 413 */     return this.itemType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDifficultyAt(int knowledge) {
/* 423 */     float diff = (this.difficultyAt1 + (this.difficultyAt100 - this.difficultyAt1) / 100 * knowledge);
/*     */ 
/*     */     
/* 426 */     if (diff < 0.0F) {
/* 427 */       return knowledge + diff;
/*     */     }
/* 429 */     return diff;
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
/* 443 */     return Math.min(100.0F, knowledge + (100 - knowledge) * (float)power / 500.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getChanceAt(Creature performer, int knowledge, int tilex, int tiley) {
/* 449 */     float chance = (this.chanceAt1 + (this.chanceAt100 - this.chanceAt1) / 100 * knowledge);
/* 450 */     return chance + this.modifiedBy.chanceModifier(performer, this.chanceModifier, tilex, tiley);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getMaterial() {
/* 455 */     return this.material;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Herb getRandomHerb(Creature performer, byte aTileType, GrassData.GrowthStage aGrassLength, short aCategory, int knowledge, int tilex, int tiley) {
/* 465 */     byte checkType = aTileType;
/* 466 */     Tiles.Tile theTile = Tiles.getTile(aTileType);
/* 467 */     if (theTile.isMycelium())
/* 468 */       checkType = Tiles.Tile.TILE_GRASS.id; 
/* 469 */     if (theTile.isNormalTree() || theTile.isMyceliumTree())
/* 470 */       checkType = Tiles.Tile.TILE_TREE.id; 
/* 471 */     if (theTile.isNormalBush() || theTile.isMyceliumBush()) {
/* 472 */       checkType = Tiles.Tile.TILE_BUSH.id;
/*     */     }
/* 474 */     float totalChance = 0.0F;
/* 475 */     for (Herb h : values()) {
/*     */       
/* 477 */       if (h.tileType == checkType)
/*     */       {
/* 479 */         if (h.grassLength == aGrassLength) {
/*     */           
/* 481 */           float chance = h.getChanceAt(performer, knowledge, tilex, tiley);
/* 482 */           if (chance >= 0.0F) {
/* 483 */             totalChance += chance;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 488 */     if (totalChance == 0.0F) {
/* 489 */       return null;
/*     */     }
/* 491 */     int rndChance = Server.rand.nextInt((int)totalChance);
/*     */ 
/*     */     
/* 494 */     float runningChance = 0.0F;
/* 495 */     for (Herb h : values()) {
/*     */       
/* 497 */       if (h.tileType == checkType)
/*     */       {
/* 499 */         if (checkType != Tiles.Tile.TILE_GRASS.id || h.grassLength == aGrassLength) {
/*     */           
/* 501 */           float chance = h.getChanceAt(performer, knowledge, tilex, tiley);
/* 502 */           if (chance >= 0.0F) {
/*     */             
/* 504 */             runningChance += chance;
/* 505 */             if (rndChance < runningChance) {
/*     */               
/* 507 */               if (aCategory == 224 || aCategory == h.category) {
/* 508 */                 return h;
/*     */               }
/* 510 */               return null;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 516 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getDifficulty(int templateId, int knowledge) {
/* 526 */     for (Herb h : values()) {
/*     */       
/* 528 */       if (h.tileType == Tiles.Tile.TILE_GRASS.id && h.grassLength == GrassData.GrowthStage.SHORT && h.itemType == templateId)
/*     */       {
/* 530 */         return h.getDifficultyAt(knowledge);
/*     */       }
/*     */     } 
/* 533 */     return -1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Herb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */