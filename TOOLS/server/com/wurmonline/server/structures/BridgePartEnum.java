/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.behaviours.ActionEntry;
/*     */ import com.wurmonline.server.behaviours.BridgePartBehaviour;
/*     */ import com.wurmonline.server.behaviours.BuildAllMaterials;
/*     */ import com.wurmonline.server.behaviours.BuildMaterial;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.BridgeConstants;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum BridgePartEnum
/*     */ {
/*  24 */   WOOD_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Wood abutment with two walls", false, BridgeConstants.BridgeMaterial.WOOD, (short)440),
/*  25 */   WOOD_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Wood abutment with wall on left", false, BridgeConstants.BridgeMaterial.WOOD, (short)440),
/*  26 */   WOOD_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Wood abutment with wall on right", false, BridgeConstants.BridgeMaterial.WOOD, (short)440),
/*  27 */   WOOD_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Wood crown with two walls", false, BridgeConstants.BridgeMaterial.WOOD, (short)441),
/*  28 */   WOOD_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Wood crown with one wall", false, BridgeConstants.BridgeMaterial.WOOD, (short)441),
/*  29 */   WOOD_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Wood support with two walls", false, BridgeConstants.BridgeMaterial.WOOD, (short)442),
/*  30 */   WOOD_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Wood support with one wall", false, BridgeConstants.BridgeMaterial.WOOD, (short)442),
/*     */   
/*  32 */   BRICK_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Brick abutment with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)443),
/*  33 */   BRICK_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Brick abutment with wall on left", false, BridgeConstants.BridgeMaterial.BRICK, (short)443),
/*  34 */   BRICK_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Brick abutment with wall on right", false, BridgeConstants.BridgeMaterial.BRICK, (short)443),
/*  35 */   BRICK_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Brick abutment with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)443),
/*  36 */   BRICK_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Brick bracing with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)444),
/*  37 */   BRICK_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Brick bracing with wall on left", false, BridgeConstants.BridgeMaterial.BRICK, (short)444),
/*  38 */   BRICK_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Brick bracing with wall on right", false, BridgeConstants.BridgeMaterial.BRICK, (short)444),
/*  39 */   BRICK_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Brick bracing with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)444),
/*  40 */   BRICK_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Brick crown with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)445),
/*  41 */   BRICK_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Brick crown with one wall", false, BridgeConstants.BridgeMaterial.BRICK, (short)445),
/*  42 */   BRICK_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Brick crown with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)445),
/*  43 */   BRICK_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Brick double bracing with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)446),
/*  44 */   BRICK_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Brick double bracing with one wall", false, BridgeConstants.BridgeMaterial.BRICK, (short)446),
/*  45 */   BRICK_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Brick double bracing with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)446),
/*  46 */   BRICK_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Brick double abutment with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)447),
/*  47 */   BRICK_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Brick double abutment with one wall", false, BridgeConstants.BridgeMaterial.BRICK, (short)447),
/*  48 */   BRICK_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Brick double abutment with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)447),
/*  49 */   BRICK_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Brick floating with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)448),
/*  50 */   BRICK_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Brick floating with one wall", false, BridgeConstants.BridgeMaterial.BRICK, (short)448),
/*  51 */   BRICK_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Brick floating with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)448),
/*  52 */   BRICK_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Brick support with two walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)449),
/*  53 */   BRICK_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Brick support with one wall", false, BridgeConstants.BridgeMaterial.BRICK, (short)449),
/*  54 */   BRICK_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Brick support with no walls", false, BridgeConstants.BridgeMaterial.BRICK, (short)449),
/*     */   
/*  56 */   MARBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Marble abutment with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)450),
/*  57 */   MARBLE_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Marble abutment with wall on left", false, BridgeConstants.BridgeMaterial.MARBLE, (short)450),
/*  58 */   MARBLE_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Marble abutment with wall on right", false, BridgeConstants.BridgeMaterial.MARBLE, (short)450),
/*  59 */   MARBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Marble abutment with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)450),
/*  60 */   MARBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Marble bracing with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)451),
/*  61 */   MARBLE_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Marble bracing with wall on left", false, BridgeConstants.BridgeMaterial.MARBLE, (short)451),
/*  62 */   MARBLE_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Marble bracing with wall on right", false, BridgeConstants.BridgeMaterial.MARBLE, (short)451),
/*  63 */   MARBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Marble bracing with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)451),
/*  64 */   MARBLE_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Marble crown with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)452),
/*  65 */   MARBLE_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Marble crown with one wall", false, BridgeConstants.BridgeMaterial.MARBLE, (short)452),
/*  66 */   MARBLE_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Marble crown with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)452),
/*  67 */   MARBLE_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Marble double bracing with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)453),
/*  68 */   MARBLE_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Marble double bracing with one wall", false, BridgeConstants.BridgeMaterial.MARBLE, (short)453),
/*  69 */   MARBLE_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Marble double bracing with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)453),
/*  70 */   MARBLE_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Marble double abutment with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)454),
/*  71 */   MARBLE_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Marble double abutment with one wall", false, BridgeConstants.BridgeMaterial.MARBLE, (short)454),
/*  72 */   MARBLE_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Marble double abutment with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)454),
/*  73 */   MARBLE_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Marble floating with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)455),
/*  74 */   MARBLE_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Marble floating with one wall", false, BridgeConstants.BridgeMaterial.MARBLE, (short)455),
/*  75 */   MARBLE_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Marble floating with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)455),
/*  76 */   MARBLE_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Marble support with two walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)456),
/*  77 */   MARBLE_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Marble support with one wall", false, BridgeConstants.BridgeMaterial.MARBLE, (short)456),
/*  78 */   MARBLE_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Marble support with no walls", false, BridgeConstants.BridgeMaterial.MARBLE, (short)456),
/*     */   
/*  80 */   SLATE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Slate abutment with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)430),
/*  81 */   SLATE_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Slate abutment with wall on left", false, BridgeConstants.BridgeMaterial.SLATE, (short)430),
/*  82 */   SLATE_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Slate abutment with wall on right", false, BridgeConstants.BridgeMaterial.SLATE, (short)430),
/*  83 */   SLATE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Slate abutment with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)430),
/*  84 */   SLATE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Slate bracing with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)431),
/*  85 */   SLATE_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Slate bracing with wall on left", false, BridgeConstants.BridgeMaterial.SLATE, (short)431),
/*  86 */   SLATE_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Slate bracing with wall on right", false, BridgeConstants.BridgeMaterial.SLATE, (short)431),
/*  87 */   SLATE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Slate bracing with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)431),
/*  88 */   SLATE_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Slate crown with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)432),
/*  89 */   SLATE_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Slate crown with one wall", false, BridgeConstants.BridgeMaterial.SLATE, (short)432),
/*  90 */   SLATE_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Slate crown with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)432),
/*  91 */   SLATE_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Slate double bracing with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)433),
/*  92 */   SLATE_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Slate double bracing with one wall", false, BridgeConstants.BridgeMaterial.SLATE, (short)433),
/*  93 */   SLATE_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Slate double bracing with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)433),
/*  94 */   SLATE_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Slate double abutment with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)434),
/*  95 */   SLATE_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Slate double abutment with one wall", false, BridgeConstants.BridgeMaterial.SLATE, (short)434),
/*  96 */   SLATE_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Slate double abutment with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)434),
/*  97 */   SLATE_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Slate floating with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)435),
/*  98 */   SLATE_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Slate floating with one wall", false, BridgeConstants.BridgeMaterial.SLATE, (short)435),
/*  99 */   SLATE_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Slate floating with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)435),
/* 100 */   SLATE_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Slate support with two walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)436),
/* 101 */   SLATE_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Slate support with one wall", false, BridgeConstants.BridgeMaterial.SLATE, (short)436),
/* 102 */   SLATE_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Slate support with no walls", false, BridgeConstants.BridgeMaterial.SLATE, (short)436),
/*     */   
/* 104 */   ROUNDED_STONE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Rounded stone abutment with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)410),
/* 105 */   ROUNDED_STONE_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Rounded stone abutment with wall on left", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)410),
/* 106 */   ROUNDED_STONE_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Rounded stone abutment with wall on right", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)410),
/* 107 */   ROUNDED_STONE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Rounded stone abutment with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)410),
/* 108 */   ROUNDED_STONE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Rounded stone bracing with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)411),
/* 109 */   ROUNDED_STONE_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Rounded stone bracing with wall on left", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)411),
/* 110 */   ROUNDED_STONE_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Rounded stone bracing with wall on right", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)411),
/* 111 */   ROUNDED_STONE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Rounded stone bracing with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)411),
/* 112 */   ROUNDED_STONE_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Rounded stone crown with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)412),
/* 113 */   ROUNDED_STONE_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Rounded stone crown with one wall", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)412),
/* 114 */   ROUNDED_STONE_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Rounded stone crown with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)412),
/* 115 */   ROUNDED_STONE_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Rounded stone double bracing with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)413),
/* 116 */   ROUNDED_STONE_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Rounded stone double bracing with one wall", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)413),
/* 117 */   ROUNDED_STONE_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Rounded stone double bracing with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)413),
/* 118 */   ROUNDED_STONE_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Rounded stone double abutment with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)414),
/* 119 */   ROUNDED_STONE_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Rounded stone double abutment with one wall", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)414),
/* 120 */   ROUNDED_STONE_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Rounded stone double abutment with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)414),
/* 121 */   ROUNDED_STONE_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Rounded stone floating with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)415),
/* 122 */   ROUNDED_STONE_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Rounded stone floating with one wall", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)415),
/* 123 */   ROUNDED_STONE_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Rounded stone floating with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)415),
/* 124 */   ROUNDED_STONE_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Rounded stone support with two walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)416),
/* 125 */   ROUNDED_STONE_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Rounded stone support with one wall", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)416),
/* 126 */   ROUNDED_STONE_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Rounded stone support with no walls", false, BridgeConstants.BridgeMaterial.ROUNDED_STONE, (short)416),
/*     */   
/* 128 */   POTTERY_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Pottery abutment with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)390),
/* 129 */   POTTERY_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Pottery abutment with wall on left", false, BridgeConstants.BridgeMaterial.POTTERY, (short)390),
/* 130 */   POTTERY_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Pottery abutment with wall on right", false, BridgeConstants.BridgeMaterial.POTTERY, (short)390),
/* 131 */   POTTERY_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Pottery abutment with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)390),
/* 132 */   POTTERY_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Pottery bracing with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)391),
/* 133 */   POTTERY_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Pottery bracing with wall on left", false, BridgeConstants.BridgeMaterial.POTTERY, (short)391),
/* 134 */   POTTERY_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Pottery bracing with wall on right", false, BridgeConstants.BridgeMaterial.POTTERY, (short)391),
/* 135 */   POTTERY_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Pottery bracing with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)391),
/* 136 */   POTTERY_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Pottery crown with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)392),
/* 137 */   POTTERY_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Pottery crown with one wall", false, BridgeConstants.BridgeMaterial.POTTERY, (short)392),
/* 138 */   POTTERY_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Pottery crown with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)392),
/* 139 */   POTTERY_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Pottery double bracing with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)393),
/* 140 */   POTTERY_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Pottery double bracing with one wall", false, BridgeConstants.BridgeMaterial.POTTERY, (short)393),
/* 141 */   POTTERY_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Pottery double bracing with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)393),
/* 142 */   POTTERY_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Pottery double abutment with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)394),
/* 143 */   POTTERY_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Pottery double abutment with one wall", false, BridgeConstants.BridgeMaterial.POTTERY, (short)394),
/* 144 */   POTTERY_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Pottery double abutment with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)394),
/* 145 */   POTTERY_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Pottery floating with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)395),
/* 146 */   POTTERY_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Pottery floating with one wall", false, BridgeConstants.BridgeMaterial.POTTERY, (short)395),
/* 147 */   POTTERY_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Pottery floating with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)395),
/* 148 */   POTTERY_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Pottery support with two walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)396),
/* 149 */   POTTERY_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Pottery support with one wall", false, BridgeConstants.BridgeMaterial.POTTERY, (short)396),
/* 150 */   POTTERY_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Pottery support with no walls", false, BridgeConstants.BridgeMaterial.POTTERY, (short)396),
/*     */   
/* 152 */   SANDSTONE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Sandstone abutment with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)370),
/* 153 */   SANDSTONE_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Sandstone abutment with wall on left", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)370),
/* 154 */   SANDSTONE_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Sandstone abutment with wall on right", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)370),
/* 155 */   SANDSTONE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Sandstone abutment with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)370),
/* 156 */   SANDSTONE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Sandstone bracing with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)371),
/* 157 */   SANDSTONE_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Sandstone bracing with wall on left", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)371),
/* 158 */   SANDSTONE_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Sandstone bracing with wall on right", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)371),
/* 159 */   SANDSTONE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Sandstone bracing with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)371),
/* 160 */   SANDSTONE_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Sandstone crown with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)372),
/* 161 */   SANDSTONE_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Sandstone crown with one wall", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)372),
/* 162 */   SANDSTONE_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Sandstone crown with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)372),
/* 163 */   SANDSTONE_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Sandstone double bracing with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)373),
/* 164 */   SANDSTONE_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Sandstone double bracing with one wall", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)373),
/* 165 */   SANDSTONE_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Sandstone double bracing with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)373),
/* 166 */   SANDSTONE_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Sandstone double abutment with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)374),
/* 167 */   SANDSTONE_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Sandstone double abutment with one wall", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)374),
/* 168 */   SANDSTONE_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Sandstone double abutment with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)374),
/* 169 */   SANDSTONE_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Sandstone floating with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)375),
/* 170 */   SANDSTONE_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Sandstone floating with one wall", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)375),
/* 171 */   SANDSTONE_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Sandstone floating with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)375),
/* 172 */   SANDSTONE_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Sandstone support with two walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)376),
/* 173 */   SANDSTONE_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Sandstone support with one wall", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)376),
/* 174 */   SANDSTONE_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Sandstone support with no walls", false, BridgeConstants.BridgeMaterial.SANDSTONE, (short)376),
/*     */   
/* 176 */   RENDERED_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Rendered abutment with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)350),
/* 177 */   RENDERED_ABUTMENT_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.ABUTMENT_LEFT, "Rendered abutment with wall on left", false, BridgeConstants.BridgeMaterial.RENDERED, (short)350),
/* 178 */   RENDERED_ABUTMENT_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.ABUTMENT_RIGHT, "Rendered abutment with wall on right", false, BridgeConstants.BridgeMaterial.RENDERED, (short)350),
/* 179 */   RENDERED_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Rendered abutment with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)350),
/* 180 */   RENDERED_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.BRACING_NARROW, "Rendered bracing with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)351),
/* 181 */   RENDERED_BRACING_WITH_WALL_ON_LEFT(BridgeConstants.BridgeType.BRACING_LEFT, "Rendered bracing with wall on left", false, BridgeConstants.BridgeMaterial.RENDERED, (short)351),
/* 182 */   RENDERED_BRACING_WITH_WALL_ON_RIGHT(BridgeConstants.BridgeType.BRACING_RIGHT, "Rendered bracing with wall on right", false, BridgeConstants.BridgeMaterial.RENDERED, (short)351),
/* 183 */   RENDERED_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.BRACING_CENTER, "Rendered bracing with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)351),
/* 184 */   RENDERED_CROWN_WITH_2_WALLS(BridgeConstants.BridgeType.CROWN_NARROW, "Rendered crown with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)352),
/* 185 */   RENDERED_CROWN_WITH_1_WALL(BridgeConstants.BridgeType.CROWN_SIDE, "Rendered crown with one wall", false, BridgeConstants.BridgeMaterial.RENDERED, (short)352),
/* 186 */   RENDERED_CROWN_WITH_NO_WALLS(BridgeConstants.BridgeType.CROWN_CENTER, "Rendered crown with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)352),
/* 187 */   RENDERED_DOUBLE_BRACING_WITH_2_WALLS(BridgeConstants.BridgeType.DOUBLE_NARROW, "Rendered double bracing with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)353),
/* 188 */   RENDERED_DOUBLE_BRACING_WITH_1_WALL(BridgeConstants.BridgeType.DOUBLE_SIDE, "Rendered double bracing with one wall", false, BridgeConstants.BridgeMaterial.RENDERED, (short)353),
/* 189 */   RENDERED_DOUBLE_BRACING_WITH_NO_WALLS(BridgeConstants.BridgeType.DOUBLE_CENTER, "Rendered double bracing with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)353),
/* 190 */   RENDERED_DOUBLE_ABUTMENT_WITH_2_WALLS(BridgeConstants.BridgeType.END_NARROW, "Rendered double abutment with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)354),
/* 191 */   RENDERED_DOUBLE_ABUTMENT_WITH_1_WALL(BridgeConstants.BridgeType.END_SIDE, "Rendered double abutment with one wall", false, BridgeConstants.BridgeMaterial.RENDERED, (short)354),
/* 192 */   RENDERED_DOUBLE_ABUTMENT_WITH_NO_WALLS(BridgeConstants.BridgeType.END_CENTER, "Rendered double abutment with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)354),
/* 193 */   RENDERED_FLOATING_WITH_2_WALLS(BridgeConstants.BridgeType.FLOATING_NARROW, "Rendered floating with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)355),
/* 194 */   RENDERED_FLOATING_WITH_1_WALL(BridgeConstants.BridgeType.FLOATING_SIDE, "Rendered floating with one wall", false, BridgeConstants.BridgeMaterial.RENDERED, (short)355),
/* 195 */   RENDERED_FLOATING_WITH_NO_WALLS(BridgeConstants.BridgeType.FLOATING_CENTER, "Rendered floating with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)355),
/* 196 */   RENDERED_SUPPORT_WITH_2_WALLS(BridgeConstants.BridgeType.SUPPORT_NARROW, "Rendered support with two walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)356),
/* 197 */   RENDERED_SUPPORT_WITH_1_WALL(BridgeConstants.BridgeType.SUPPORT_SIDE, "Rendered support with one wall", false, BridgeConstants.BridgeMaterial.RENDERED, (short)356),
/* 198 */   RENDERED_SUPPORT_WITH_NO_WALLS(BridgeConstants.BridgeType.SUPPORT_CENTER, "Rendered support with no walls", false, BridgeConstants.BridgeMaterial.RENDERED, (short)356),
/*     */   
/* 200 */   ROPE_ABUTMENT(BridgeConstants.BridgeType.ABUTMENT_NARROW, "Rope abutment", false, BridgeConstants.BridgeMaterial.ROPE, (short)457),
/* 201 */   ROPE_CROWN(BridgeConstants.BridgeType.CROWN_NARROW, "Rope crown", false, BridgeConstants.BridgeMaterial.ROPE, (short)458),
/* 202 */   ROPE_DOUBLE_ABUTMENT(BridgeConstants.BridgeType.END_NARROW, "Rope double abutment", false, BridgeConstants.BridgeMaterial.ROPE, (short)459),
/*     */   
/* 204 */   UNKNOWN(BridgeConstants.BridgeType.ABUTMENT_CENTER, "Unkown", true, BridgeConstants.BridgeMaterial.WOOD, (short)60);
/*     */   
/*     */   private final BridgeConstants.BridgeType type;
/*     */   
/*     */   private final boolean isFloor;
/*     */   
/*     */   private final BridgeConstants.BridgeMaterial material;
/*     */   private final short actionId;
/*     */   
/*     */   static {
/* 214 */     emptyArr = new int[0];
/*     */   }
/*     */   private final String name; private final String actionString; private final short icon; private static int[] emptyArr;
/*     */   
/*     */   BridgePartEnum(BridgeConstants.BridgeType type, String name, boolean isFloor, BridgeConstants.BridgeMaterial material, short icon) {
/* 219 */     this.type = type;
/* 220 */     this.isFloor = isFloor;
/* 221 */     this.material = material;
/* 222 */     this.actionId = (short)(20000 + this.material.getCode());
/* 223 */     this.name = name;
/* 224 */     this.actionString = StringUtil.format("%s %s", new Object[] { "Building", StringUtil.toLowerCase(this.name) });
/* 225 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final ActionEntry createActionEntry() {
/* 231 */     return ActionEntry.createEntry(this.actionId, this.name, this.actionString, emptyArr);
/*     */   }
/*     */ 
/*     */   
/*     */   public final BridgeConstants.BridgeType getType() {
/* 236 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final BridgeConstants.BridgeMaterial getMaterial() {
/* 241 */     return this.material;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 246 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isFloor() {
/* 251 */     return this.isFloor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getIcon() {
/* 256 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getActionId() {
/* 261 */     return this.actionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isValidTool(Item tool) {
/* 266 */     if (tool == null)
/* 267 */       return false; 
/* 268 */     int[] valid = getValidToolsForMaterial(this.material);
/* 269 */     for (int v : valid) {
/*     */       
/* 271 */       if (v == tool.getTemplateId()) {
/* 272 */         return true;
/*     */       }
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final List<BridgePartEnum> getRoofsByTool(Item tool) {
/* 280 */     List<BridgePartEnum> list = new ArrayList<>();
/* 281 */     if (tool == null) {
/* 282 */       return list;
/*     */     }
/* 284 */     for (BridgePartEnum en : values()) {
/*     */       
/* 286 */       if (!en.isFloor() && en.isValidTool(tool) && en != UNKNOWN)
/* 287 */         list.add(en); 
/*     */     } 
/* 289 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BridgePartEnum getByBridgePartType(BridgePart bridgePart) {
/* 294 */     for (BridgePartEnum en : values()) {
/*     */       
/* 296 */       if (en.getType() == bridgePart.getType() && en.getMaterial() == bridgePart.getMaterial())
/* 297 */         return en; 
/*     */     } 
/* 299 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BuildAllMaterials getMaterialsNeeded(BridgePart bridgePart) {
/* 304 */     BuildAllMaterials billOfMaterial = BridgePartBehaviour.getRequiredMaterials(bridgePart);
/* 305 */     return billOfMaterial.getRemainingMaterialsNeeded();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildMaterial> getTotalMaterialsNeeded() {
/* 310 */     BuildAllMaterials billOfMaterial = BridgePartBehaviour.getRequiredMaterials(this.type, this.material, 0);
/* 311 */     return billOfMaterial.getTotalMaterialsNeeded();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canBuildBridgePlan(BridgePart bridgePart, BridgePartEnum en, Creature performer) {
/* 316 */     Skill skill = BridgePartBehaviour.getBuildSkill(en.getType(), en.getMaterial(), performer);
/* 317 */     if (skill == null) {
/* 318 */       return false;
/*     */     }
/* 320 */     if (skill.getKnowledge(0.0D) < 40.0D) {
/* 321 */       return false;
/*     */     }
/* 323 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNeededSkillNumber() {
/* 328 */     return BridgePartBehaviour.getRequiredSkill(this.material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BridgePart getBridgePartFromId(long bridgePartId) {
/* 333 */     int x = Tiles.decodeTileX(bridgePartId);
/* 334 */     int y = Tiles.decodeTileY(bridgePartId);
/*     */     
/* 336 */     int layer = Tiles.decodeLayer(bridgePartId);
/*     */     
/* 338 */     BridgePart[] bridgeParts = Zones.getBridgePartsAtTile(x, y, (layer == 0));
/* 339 */     if (bridgeParts.length > 0)
/* 340 */       return bridgeParts[0]; 
/* 341 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final List<BridgePartEnum> getBridgeByToolAndType(Item tool, BridgeConstants.BridgeType fType) {
/* 346 */     List<BridgePartEnum> list = new ArrayList<>();
/* 347 */     if (tool == null) {
/* 348 */       return list;
/*     */     }
/* 350 */     for (BridgePartEnum en : values()) {
/*     */       
/* 352 */       if (en != UNKNOWN)
/*     */       {
/* 354 */         if (en.getType() == fType && en.isValidTool(tool))
/* 355 */           list.add(en); 
/*     */       }
/*     */     } 
/* 358 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BridgePartEnum getBridgePartByTypeAndMaterial(BridgeConstants.BridgeType type, BridgeConstants.BridgeMaterial material) {
/* 363 */     for (BridgePartEnum en : values()) {
/*     */       
/* 365 */       if (en.getType() == type && en.getMaterial() == material)
/* 366 */         return en; 
/*     */     } 
/* 368 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int[] getValidToolsForMaterial(BridgeConstants.BridgeMaterial material) {
/* 373 */     switch (material) {
/*     */       
/*     */       case ROPE:
/* 376 */         return new int[] { 14 };
/*     */       case BRICK:
/*     */       case MARBLE:
/*     */       case SLATE:
/*     */       case ROUNDED_STONE:
/*     */       case POTTERY:
/*     */       case SANDSTONE:
/*     */       case RENDERED:
/* 384 */         return new int[] { 493 };
/*     */       case WOOD:
/* 386 */         return new int[] { 62, 63 };
/*     */     } 
/* 388 */     return new int[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\BridgePartEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */