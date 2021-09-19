/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.util.HashMap;
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
/*     */ public enum StructureConstantsEnum
/*     */ {
/*  30 */   NO_WALL((short)14, BuildingTypesEnum.HOUSE, StructureTypeEnum.NO_WALL, StructureMaterialEnum.WOOD),
/*     */   
/*  32 */   WALL_LEFT_ARCH_WOODEN((short)15, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.WOOD),
/*  33 */   WALL_RIGHT_ARCH_WOODEN((short)16, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.WOOD),
/*  34 */   WALL_T_ARCH_WOODEN((short)17, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.WOOD),
/*  35 */   WALL_LEFT_ARCH_STONE((short)18, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.STONE),
/*  36 */   WALL_RIGHT_ARCH_STONE((short)19, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.STONE),
/*     */   
/*  38 */   WALL_SOLID_WOODEN((short)20, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.WOOD),
/*  39 */   WALL_WINDOW_WOODEN((short)21, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.WOOD),
/*  40 */   WALL_DOOR_WOODEN((short)22, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.WOOD),
/*  41 */   WALL_DOUBLE_DOOR_WOODEN((short)23, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.WOOD),
/*  42 */   WALL_DOOR_ARCHED_WOODEN((short)24, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.WOOD),
/*     */   
/*  44 */   WALL_SOLID_SLATE((short)25, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.SLATE),
/*  45 */   WALL_WINDOW_SLATE((short)26, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.SLATE),
/*  46 */   WALL_NARROW_WINDOW_SLATE((short)27, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.SLATE),
/*  47 */   WALL_DOOR_SLATE((short)28, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.SLATE),
/*  48 */   WALL_DOUBLE_DOOR_SLATE((short)29, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.SLATE),
/*     */   
/*  50 */   WALL_SOLID_STONE((short)30, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.PLAIN_STONE),
/*  51 */   WALL_WINDOW_STONE((short)31, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.PLAIN_STONE),
/*  52 */   WALL_DOOR_STONE((short)32, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.PLAIN_STONE),
/*  53 */   WALL_DOUBLE_DOOR_STONE((short)33, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.PLAIN_STONE),
/*  54 */   WALL_DOOR_ARCHED_STONE((short)34, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.PLAIN_STONE),
/*  55 */   WALL_SOLID_STONE_DECORATED((short)35, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.STONE),
/*  56 */   WALL_WINDOW_STONE_DECORATED((short)36, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.STONE),
/*  57 */   WALL_DOOR_STONE_DECORATED((short)37, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.STONE),
/*  58 */   WALL_DOUBLE_DOOR_STONE_DECORATED((short)38, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.STONE),
/*  59 */   WALL_DOOR_ARCHED_STONE_DECORATED((short)39, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.STONE),
/*  60 */   WALL_SOLID_TIMBER_FRAMED((short)40, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.TIMBER_FRAMED),
/*  61 */   WALL_WINDOW_TIMBER_FRAMED((short)41, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.TIMBER_FRAMED),
/*  62 */   WALL_DOOR_TIMBER_FRAMED((short)42, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.TIMBER_FRAMED),
/*  63 */   WALL_DOUBLE_DOOR_TIMBER_FRAMED((short)43, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.TIMBER_FRAMED),
/*  64 */   WALL_DOOR_ARCHED_TIMBER_FRAMED((short)44, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.TIMBER_FRAMED),
/*     */   
/*  66 */   WALL_ARCHED_SLATE((short)45, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.SLATE),
/*  67 */   WALL_PORTCULLIS_SLATE((short)46, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SLATE),
/*  68 */   WALL_BARRED_SLATE((short)47, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.SLATE),
/*  69 */   WALL_ORIEL_SLATE((short)48, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.SLATE),
/*  70 */   WALL_LEFT_ARCH_SLATE((short)49, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.SLATE),
/*  71 */   WALL_RIGHT_ARCH_SLATE((short)50, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.SLATE),
/*  72 */   WALL_T_ARCH_SLATE((short)51, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.SLATE),
/*  73 */   WALL_SOLID_ROUNDED_STONE((short)52, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.ROUNDED_STONE),
/*  74 */   WALL_WINDOW_ROUNDED_STONE((short)53, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.ROUNDED_STONE),
/*  75 */   WALL_NARROW_WINDOW_ROUNDED_STONE((short)54, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.ROUNDED_STONE),
/*  76 */   WALL_DOOR_ROUNDED_STONE((short)55, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.ROUNDED_STONE),
/*  77 */   WALL_DOUBLE_DOOR_ROUNDED_STONE((short)56, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.ROUNDED_STONE),
/*  78 */   WALL_ARCHED_ROUNDED_STONE((short)57, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.ROUNDED_STONE),
/*  79 */   WALL_PORTCULLIS_ROUNDED_STONE((short)58, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.ROUNDED_STONE),
/*  80 */   WALL_BARRED_ROUNDED_STONE((short)59, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.ROUNDED_STONE),
/*  81 */   WALL_ORIEL_ROUNDED_STONE((short)60, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.ROUNDED_STONE),
/*  82 */   WALL_LEFT_ARCH_ROUNDED_STONE((short)61, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.ROUNDED_STONE),
/*  83 */   WALL_RIGHT_ARCH_ROUNDED_STONE((short)62, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.ROUNDED_STONE),
/*  84 */   WALL_T_ARCH_ROUNDED_STONE((short)63, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.ROUNDED_STONE),
/*  85 */   WALL_SOLID_POTTERY((short)64, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.POTTERY),
/*  86 */   WALL_WINDOW_POTTERY((short)65, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.POTTERY),
/*  87 */   WALL_NARROW_WINDOW_POTTERY((short)66, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.POTTERY),
/*  88 */   WALL_DOOR_POTTERY((short)67, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.POTTERY),
/*  89 */   WALL_DOUBLE_DOOR_POTTERY((short)68, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.POTTERY),
/*  90 */   WALL_ARCHED_POTTERY((short)69, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.POTTERY),
/*  91 */   WALL_PORTCULLIS_POTTERY((short)70, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.POTTERY),
/*  92 */   WALL_BARRED_POTTERY((short)71, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.POTTERY),
/*  93 */   WALL_ORIEL_POTTERY((short)72, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.POTTERY),
/*  94 */   WALL_LEFT_ARCH_POTTERY((short)73, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.POTTERY),
/*  95 */   WALL_RIGHT_ARCH_POTTERY((short)74, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.POTTERY),
/*  96 */   WALL_T_ARCH_POTTERY((short)75, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.POTTERY),
/*  97 */   WALL_PORTCULLIS_MARBLE((short)76, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.MARBLE),
/*  98 */   WALL_BARRED_MARBLE((short)77, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.MARBLE),
/*  99 */   WALL_ORIEL_MARBLE((short)78, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.MARBLE),
/* 100 */   WALL_LEFT_ARCH_MARBLE((short)79, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.MARBLE),
/* 101 */   WALL_RIGHT_ARCH_MARBLE((short)80, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.MARBLE),
/* 102 */   WALL_T_ARCH_MARBLE((short)81, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.MARBLE),
/*     */   
/* 104 */   FENCE_ROPE_HIGH((short)104, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.ROPE_HIGH, StructureMaterialEnum.WOOD),
/* 105 */   HEDGE_FLOWER1_LOW((short)105, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER1),
/* 106 */   HEDGE_FLOWER1_MEDIUM((short)106, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER1),
/* 107 */   HEDGE_FLOWER1_HIGH((short)107, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER1),
/* 108 */   HEDGE_FLOWER2_LOW((short)108, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER2),
/* 109 */   HEDGE_FLOWER2_MEDIUM((short)109, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER2),
/* 110 */   HEDGE_FLOWER2_HIGH((short)110, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER2),
/* 111 */   HEDGE_FLOWER3_LOW((short)111, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER3),
/* 112 */   HEDGE_FLOWER3_MEDIUM((short)112, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER3),
/* 113 */   HEDGE_FLOWER3_HIGH((short)113, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER3),
/* 114 */   HEDGE_FLOWER4_LOW((short)114, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER4),
/* 115 */   HEDGE_FLOWER4_MEDIUM((short)115, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER4),
/* 116 */   HEDGE_FLOWER4_HIGH((short)116, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER4),
/* 117 */   HEDGE_FLOWER5_LOW((short)117, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER5),
/* 118 */   HEDGE_FLOWER5_MEDIUM((short)118, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER5),
/* 119 */   HEDGE_FLOWER5_HIGH((short)119, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER5),
/* 120 */   HEDGE_FLOWER6_LOW((short)120, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER6),
/* 121 */   HEDGE_FLOWER6_MEDIUM((short)121, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER6),
/* 122 */   HEDGE_FLOWER6_HIGH((short)122, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER6),
/* 123 */   HEDGE_FLOWER7_LOW((short)123, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_LOW, StructureMaterialEnum.FLOWER7),
/* 124 */   HEDGE_FLOWER7_MEDIUM((short)124, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_MEDIUM, StructureMaterialEnum.FLOWER7),
/* 125 */   HEDGE_FLOWER7_HIGH((short)125, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.HEDGE_HIGH, StructureMaterialEnum.FLOWER7),
/* 126 */   FENCE_MAGIC_STONE((short)126, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MAGIC_FENCE, StructureMaterialEnum.STONE),
/* 127 */   FENCE_GARDESGARD_GATE((short)127, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GARDESGARD_GATE, StructureMaterialEnum.WOOD),
/*     */ 
/*     */ 
/*     */   
/* 131 */   WALL_LEFT_ARCH_TIMBER_FRAMED((short)-15, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.TIMBER_FRAMED),
/* 132 */   WALL_RIGHT_ARCH_TIMBER_FRAMED((short)-16, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.TIMBER_FRAMED),
/* 133 */   WALL_T_ARCH_TIMBER_FRAMED((short)-17, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.TIMBER_FRAMED),
/* 134 */   WALL_LEFT_ARCH_STONE_DECORATED((short)-18, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.STONE),
/* 135 */   WALL_RIGHT_ARCH_STONE_DECORATED((short)-19, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.STONE),
/*     */   
/* 137 */   WALL_SOLID_WOODEN_PLAN((short)-20, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_SOLID, StructureMaterialEnum.WOOD),
/* 138 */   WALL_WINDOW_WOODEN_PLAN((short)-21, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_WINDOW, StructureMaterialEnum.WOOD),
/* 139 */   WALL_DOOR_WOODEN_PLAN((short)-22, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOOR, StructureMaterialEnum.WOOD),
/* 140 */   WALL_DOUBLE_DOOR_WOODEN_PLAN((short)-23, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOUBLE_DOOR, StructureMaterialEnum.WOOD),
/* 141 */   WALL_DOOR_ARCHED_WOODEN_PLAN((short)-24, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.WOOD),
/*     */   
/* 143 */   WALL_SOLID_SANDSTONE((short)-25, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.SANDSTONE),
/* 144 */   WALL_WINDOW_SANDSTONE((short)-26, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.SANDSTONE),
/* 145 */   WALL_NARROW_WINDOW_SANDSTONE((short)-27, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.SANDSTONE),
/* 146 */   WALL_DOOR_SANDSTONE((short)-28, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.SANDSTONE),
/* 147 */   WALL_DOUBLE_DOOR_SANDSTONE((short)-29, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.SANDSTONE),
/*     */   
/* 149 */   WALL_SOLID_STONE_PLAN((short)-30, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_SOLID, StructureMaterialEnum.STONE),
/* 150 */   WALL_WINDOW_STONE_PLAN((short)-31, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_WINDOW, StructureMaterialEnum.STONE),
/* 151 */   WALL_DOOR_STONE_PLAN((short)-32, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOOR, StructureMaterialEnum.STONE),
/* 152 */   WALL_DOUBLE_DOOR_STONE_PLAN((short)-33, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOUBLE_DOOR, StructureMaterialEnum.STONE),
/* 153 */   WALL_DOOR_ARCHED_PLAN((short)-34, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_ARCHED, StructureMaterialEnum.WOOD),
/* 154 */   WALL_SOLID_TIMBER_FRAMED_PLAN((short)-35, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_SOLID, StructureMaterialEnum.WOOD),
/* 155 */   WALL_WINDOW_TIMBER_FRAMED_PLAN((short)-36, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_WINDOW, StructureMaterialEnum.WOOD),
/* 156 */   WALL_DOOR_TIMBER_FRAMED_PLAN((short)-37, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOOR, StructureMaterialEnum.WOOD),
/* 157 */   WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN((short)-38, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_DOUBLE_DOOR, StructureMaterialEnum.WOOD),
/* 158 */   WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN((short)-39, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_ARCHED, StructureMaterialEnum.WOOD),
/* 159 */   WALL_PLAIN_NARROW_WINDOW((short)-40, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.PLAIN_STONE),
/* 160 */   WALL_PLAIN_NARROW_WINDOW_PLAN((short)-41, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_NARROW_WINDOW, StructureMaterialEnum.STONE),
/* 161 */   WALL_PORTCULLIS_STONE((short)-42, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.STONE),
/* 162 */   WALL_BARRED_STONE((short)-43, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.STONE),
/* 163 */   WALL_PORTCULLIS_STONE_DECORATED((short)-44, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.STONE),
/* 164 */   WALL_PORTCULLIS_WOOD((short)-45, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.WOOD),
/* 165 */   WALL_RUBBLE((short)-46, BuildingTypesEnum.HOUSE, StructureTypeEnum.RUBBLE, StructureMaterialEnum.STONE),
/* 166 */   WALL_BALCONY_TIMBER_FRAMED_PLAN((short)-47, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_BALCONY, StructureMaterialEnum.TIMBER_FRAMED),
/* 167 */   WALL_BALCONY_TIMBER_FRAMED((short)-48, BuildingTypesEnum.HOUSE, StructureTypeEnum.BALCONY, StructureMaterialEnum.TIMBER_FRAMED),
/* 168 */   WALL_JETTY_TIMBER_FRAMED_PLAN((short)-49, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_JETTY, StructureMaterialEnum.TIMBER_FRAMED),
/* 169 */   WALL_JETTY_TIMBER_FRAMED((short)-50, BuildingTypesEnum.HOUSE, StructureTypeEnum.JETTY, StructureMaterialEnum.TIMBER_FRAMED),
/* 170 */   WALL_ORIEL_STONE_DECORATED_PLAN((short)-51, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_ORIEL, StructureMaterialEnum.STONE),
/* 171 */   WALL_ORIEL_STONE_DECORATED((short)-52, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.STONE),
/* 172 */   WALL_CANOPY_WOODEN((short)-53, BuildingTypesEnum.HOUSE, StructureTypeEnum.CANOPY_DOOR, StructureMaterialEnum.WOOD),
/* 173 */   WALL_CANOPY_WOODEN_PLAN((short)-54, BuildingTypesEnum.HOUSE, StructureTypeEnum.HOUSE_PLAN_CANOPY, StructureMaterialEnum.WOOD),
/* 174 */   WALL_WINDOW_WIDE_WOODEN((short)-55, BuildingTypesEnum.HOUSE, StructureTypeEnum.WIDE_WINDOW, StructureMaterialEnum.WOOD),
/* 175 */   WALL_ORIEL_STONE_PLAIN((short)-56, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.PLAIN_STONE),
/*     */   
/* 177 */   WALL_ARCHED_SANDSTONE((short)-57, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.SANDSTONE),
/* 178 */   WALL_PORTCULLIS_SANDSTONE((short)-58, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SANDSTONE),
/* 179 */   WALL_BARRED_SANDSTONE((short)-59, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.SANDSTONE),
/* 180 */   WALL_ORIEL_SANDSTONE((short)-60, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.SANDSTONE),
/* 181 */   WALL_LEFT_ARCH_SANDSTONE((short)-61, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.SANDSTONE),
/* 182 */   WALL_RIGHT_ARCH_SANDSTONE((short)-62, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.SANDSTONE),
/* 183 */   WALL_T_ARCH_SANDSTONE((short)-63, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.SANDSTONE),
/* 184 */   WALL_SOLID_RENDERED((short)-64, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.RENDERED),
/* 185 */   WALL_WINDOW_RENDERED((short)-65, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.RENDERED),
/* 186 */   WALL_NARROW_WINDOW_RENDERED((short)-66, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.RENDERED),
/* 187 */   WALL_DOOR_RENDERED((short)-67, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.RENDERED),
/* 188 */   WALL_DOUBLE_DOOR_RENDERED((short)-68, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.RENDERED),
/* 189 */   WALL_ARCHED_RENDERED((short)-69, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.RENDERED),
/* 190 */   WALL_PORTCULLIS_RENDERED((short)-70, BuildingTypesEnum.HOUSE, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.RENDERED),
/* 191 */   WALL_BARRED_RENDERED((short)-71, BuildingTypesEnum.HOUSE, StructureTypeEnum.BARRED, StructureMaterialEnum.RENDERED),
/* 192 */   WALL_ORIEL_RENDERED((short)-72, BuildingTypesEnum.HOUSE, StructureTypeEnum.ORIEL, StructureMaterialEnum.RENDERED),
/* 193 */   WALL_LEFT_ARCH_RENDERED((short)-73, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.RENDERED),
/* 194 */   WALL_RIGHT_ARCH_RENDERED((short)-74, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.RENDERED),
/* 195 */   WALL_T_ARCH_RENDERED((short)-75, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.RENDERED),
/* 196 */   WALL_SOLID_MARBLE((short)-76, BuildingTypesEnum.HOUSE, StructureTypeEnum.SOLID, StructureMaterialEnum.MARBLE),
/* 197 */   WALL_WINDOW_MARBLE((short)-77, BuildingTypesEnum.HOUSE, StructureTypeEnum.WINDOW, StructureMaterialEnum.MARBLE),
/* 198 */   WALL_NARROW_WINDOW_MARBLE((short)-78, BuildingTypesEnum.HOUSE, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.MARBLE),
/* 199 */   WALL_DOOR_MARBLE((short)-79, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOOR, StructureMaterialEnum.MARBLE),
/* 200 */   WALL_DOUBLE_DOOR_MARBLE((short)-80, BuildingTypesEnum.HOUSE, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.MARBLE),
/* 201 */   WALL_ARCHED_MARBLE((short)-81, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED, StructureMaterialEnum.MARBLE),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   WALL_T_ARCH_STONE((short)-127, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.PLAIN_STONE),
/* 207 */   WALL_T_ARCH_STONE_DECORATED((short)-128, BuildingTypesEnum.HOUSE, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.STONE),
/*     */ 
/*     */   
/* 210 */   WALL_SCAFFOLDING((short)128, BuildingTypesEnum.HOUSE, StructureTypeEnum.SCAFFOLDING, StructureMaterialEnum.CRUDE_WOOD),
/*     */ 
/*     */ 
/*     */   
/* 214 */   FENCE_WOODEN((short)0, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.WOOD),
/* 215 */   FENCE_PALISADE((short)1, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PALISADE, StructureMaterialEnum.LOG),
/* 216 */   FENCE_STONEWALL((short)2, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_WALL, StructureMaterialEnum.STONE),
/* 217 */   FENCE_WOODEN_GATE((short)3, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GATE, StructureMaterialEnum.WOOD),
/* 218 */   FENCE_PALISADE_GATE((short)4, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GATE, StructureMaterialEnum.LOG),
/* 219 */   FENCE_STONEWALL_HIGH((short)5, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.STONE),
/* 220 */   FENCE_IRON((short)6, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.STONE),
/* 221 */   FENCE_IRON_GATE((short)7, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.STONE),
/* 222 */   FENCE_WOVEN((short)8, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.WOVEN, StructureMaterialEnum.WOOD),
/* 223 */   FENCE_WOODEN_PARAPET((short)9, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.WOOD),
/* 224 */   FENCE_STONE_PARAPET((short)10, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.STONE),
/* 225 */   FENCE_STONE_IRON_PARAPET((short)11, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.IRON),
/* 226 */   FENCE_WOODEN_CRUDE((short)12, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.CRUDE_WOOD),
/* 227 */   FENCE_WOODEN_CRUDE_GATE((short)13, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GATE, StructureMaterialEnum.CRUDE_WOOD),
/*     */ 
/*     */ 
/*     */   
/* 231 */   FENCE_SLATE((short)82, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.SLATE),
/* 232 */   FENCE_SLATE_IRON((short)83, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.SLATE),
/* 233 */   FENCE_SLATE_IRON_GATE((short)84, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.SLATE),
/* 234 */   FENCE_ROUNDED_STONE((short)85, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.ROUNDED_STONE),
/* 235 */   FENCE_ROUNDED_STONE_IRON((short)86, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.ROUNDED_STONE),
/* 236 */   FENCE_ROUNDED_STONE_IRON_GATE((short)87, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.ROUNDED_STONE),
/* 237 */   FENCE_POTTERY((short)88, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.POTTERY),
/* 238 */   FENCE_POTTERY_IRON((short)89, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.POTTERY),
/* 239 */   FENCE_POTTERY_IRON_GATE((short)90, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.POTTERY),
/* 240 */   FENCE_SANDSTONE((short)91, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.SANDSTONE),
/* 241 */   FENCE_SANDSTONE_IRON((short)92, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.SANDSTONE),
/* 242 */   FENCE_SANDSTONE_IRON_GATE((short)93, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.SANDSTONE),
/* 243 */   FENCE_RENDERED((short)94, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.RENDERED),
/* 244 */   FENCE_RENDERED_IRON((short)95, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.RENDERED),
/* 245 */   FENCE_RENDERED_IRON_GATE((short)96, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.RENDERED),
/* 246 */   FENCE_MARBLE((short)97, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.MARBLE),
/* 247 */   FENCE_MARBLE_IRON((short)98, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS, StructureMaterialEnum.MARBLE),
/* 248 */   FENCE_MARBLE_IRON_GATE((short)99, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_GATE, StructureMaterialEnum.MARBLE),
/* 249 */   FENCE_ROPE_LOW((short)100, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.ROPE_LOW, StructureMaterialEnum.WOOD),
/* 250 */   FENCE_GARDESGARD_LOW((short)101, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GARDESGARD_LOW, StructureMaterialEnum.WOOD),
/* 251 */   FENCE_GARDESGARD_HIGH((short)102, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.GARDESGARD_HIGH, StructureMaterialEnum.WOOD),
/* 252 */   FENCE_CURB((short)103, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.CURB, StructureMaterialEnum.STONE),
/*     */ 
/*     */ 
/*     */   
/* 256 */   FENCE_PLAN_WOODEN((short)-1, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_WOODEN, StructureMaterialEnum.WOOD),
/* 257 */   FENCE_PLAN_PALISADE((short)-2, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_PALISADE, StructureMaterialEnum.LOG),
/* 258 */   FENCE_PLAN_STONEWALL((short)-3, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.STONE),
/* 259 */   FENCE_PLAN_PALISADE_GATE((short)-4, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_PALISADE_GATE, StructureMaterialEnum.LOG),
/* 260 */   FENCE_PLAN_WOODEN_GATE((short)-5, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_WOODEN_GATE, StructureMaterialEnum.WOOD),
/* 261 */   FENCE_PLAN_STONEWALL_HIGH((short)-6, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.STONE),
/* 262 */   FENCE_PLAN_IRON((short)-7, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.STONE),
/* 263 */   FENCE_PLAN_IRON_GATE((short)-8, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.STONE),
/* 264 */   FENCE_PLAN_WOVEN((short)-9, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.WOVEN, StructureMaterialEnum.WOOD),
/* 265 */   FENCE_PLAN_WOODEN_PARAPET((short)-10, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_WOODEN_PARAPET, StructureMaterialEnum.WOOD),
/* 266 */   FENCE_PLAN_STONE_PARAPET((short)-11, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONE_PARAPET, StructureMaterialEnum.STONE),
/* 267 */   FENCE_PLAN_STONE_IRON_PARAPET((short)-12, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_PARAPET, StructureMaterialEnum.STONE),
/* 268 */   FENCE_PLAN_WOODEN_CRUDE((short)-13, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_CRUDE, StructureMaterialEnum.WOOD),
/* 269 */   FENCE_PLAN_WOODEN_GATE_CRUDE((short)-14, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_CRUDE_GATE, StructureMaterialEnum.WOOD),
/*     */ 
/*     */ 
/*     */   
/* 273 */   FENCE_PLAN_SLATE((short)-82, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.SLATE),
/* 274 */   FENCE_PLAN_SLATE_IRON((short)-83, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.SLATE),
/* 275 */   FENCE_PLAN_SLATE_IRON_GATE((short)-84, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.SLATE),
/* 276 */   FENCE_PLAN_ROUNDED_STONE((short)-85, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.ROUNDED_STONE),
/* 277 */   FENCE_PLAN_ROUNDED_STONE_IRON((short)-86, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.ROUNDED_STONE),
/* 278 */   FENCE_PLAN_ROUNDED_STONE_IRON_GATE((short)-87, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.ROUNDED_STONE),
/* 279 */   FENCE_PLAN_POTTERY((short)-88, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.POTTERY),
/* 280 */   FENCE_PLAN_POTTERY_IRON((short)-89, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.POTTERY),
/* 281 */   FENCE_PLAN_POTTERY_IRON_GATE((short)-90, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.POTTERY),
/* 282 */   FENCE_PLAN_SANDSTONE((short)-91, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.SANDSTONE),
/* 283 */   FENCE_PLAN_SANDSTONE_IRON((short)-92, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.SANDSTONE),
/* 284 */   FENCE_PLAN_SANDSTONE_IRON_GATE((short)-93, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.SANDSTONE),
/* 285 */   FENCE_PLAN_RENDERED((short)-94, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.RENDERED),
/* 286 */   FENCE_PLAN_RENDERED_IRON((short)-95, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.RENDERED),
/* 287 */   FENCE_PLAN_RENDERED_IRON_GATE((short)-96, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.RENDERED),
/* 288 */   FENCE_PLAN_MARBLE((short)-97, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL, StructureMaterialEnum.MARBLE),
/* 289 */   FENCE_PLAN_MARBLE_IRON((short)-98, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS, StructureMaterialEnum.MARBLE),
/* 290 */   FENCE_PLAN_MARBLE_IRON_GATE((short)-99, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_GATE, StructureMaterialEnum.MARBLE),
/* 291 */   FENCE_PLAN_ROPE_LOW((short)-100, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_ROPE_LOW, StructureMaterialEnum.WOOD),
/* 292 */   FENCE_PLAN_GARDESGARD_LOW((short)-101, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_GARDESGARD_LOW, StructureMaterialEnum.WOOD),
/* 293 */   FENCE_PLAN_GARDESGARD_HIGH((short)-102, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_GARDESGARD_HIGH, StructureMaterialEnum.WOOD),
/* 294 */   FENCE_PLAN_CURB((short)-103, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_CURB, StructureMaterialEnum.STONE),
/* 295 */   FENCE_PLAN_ROPE_HIGH((short)-104, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_ROPE_HIGH, StructureMaterialEnum.WOOD),
/* 296 */   FENCE_PLAN_GARDESGARD_GATE((short)-105, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_GARDESGARD_GATE, StructureMaterialEnum.WOOD),
/* 297 */   FENCE_STONE((short)-106, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE, StructureMaterialEnum.STONE),
/* 298 */   FENCE_PLAN_STONE((short)-107, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONE_FENCE, StructureMaterialEnum.STONE),
/* 299 */   FENCE_IRON_HIGH((short)-108, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.STONE),
/* 300 */   FENCE_PLAN_IRON_HIGH((short)-109, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.STONE),
/* 301 */   FENCE_IRON_GATE_HIGH((short)-110, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.STONE),
/* 302 */   FENCE_PLAN_IRON_GATE_HIGH((short)-111, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.STONE),
/*     */ 
/*     */ 
/*     */   
/* 306 */   FENCE_SLATE_TALL_STONE_WALL((short)129, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.SLATE),
/* 307 */   FENCE_SLATE_PORTCULLIS((short)130, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SLATE),
/* 308 */   FENCE_SLATE_HIGH_IRON_FENCE((short)131, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.SLATE),
/* 309 */   FENCE_SLATE_HIGH_IRON_FENCE_GATE((short)132, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.SLATE),
/* 310 */   FENCE_SLATE_STONE_PARAPET((short)133, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.SLATE),
/* 311 */   FENCE_SLATE_CHAIN_FENCE((short)134, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.SLATE),
/*     */   
/* 313 */   FENCE_ROUNDED_STONE_TALL_STONE_WALL((short)135, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.ROUNDED_STONE),
/* 314 */   FENCE_ROUNDED_STONE_PORTCULLIS((short)136, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.ROUNDED_STONE),
/* 315 */   FENCE_ROUNDED_STONE_HIGH_IRON_FENCE((short)137, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.ROUNDED_STONE),
/* 316 */   FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE((short)138, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.ROUNDED_STONE),
/* 317 */   FENCE_ROUNDED_STONE_STONE_PARAPET((short)139, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.ROUNDED_STONE),
/* 318 */   FENCE_ROUNDED_STONE_CHAIN_FENCE((short)140, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.ROUNDED_STONE),
/*     */   
/* 320 */   FENCE_SANDSTONE_TALL_STONE_WALL((short)141, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.SANDSTONE),
/* 321 */   FENCE_SANDSTONE_PORTCULLIS((short)142, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SANDSTONE),
/* 322 */   FENCE_SANDSTONE_HIGH_IRON_FENCE((short)143, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.SANDSTONE),
/* 323 */   FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE((short)144, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.SANDSTONE),
/* 324 */   FENCE_SANDSTONE_STONE_PARAPET((short)145, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.SANDSTONE),
/* 325 */   FENCE_SANDSTONE_CHAIN_FENCE((short)146, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.SANDSTONE),
/*     */   
/* 327 */   FENCE_RENDERED_TALL_STONE_WALL((short)147, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.RENDERED),
/* 328 */   FENCE_RENDERED_PORTCULLIS((short)148, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.RENDERED),
/* 329 */   FENCE_RENDERED_HIGH_IRON_FENCE((short)149, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.RENDERED),
/* 330 */   FENCE_RENDERED_HIGH_IRON_FENCE_GATE((short)150, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.RENDERED),
/* 331 */   FENCE_RENDERED_STONE_PARAPET((short)151, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.RENDERED),
/* 332 */   FENCE_RENDERED_CHAIN_FENCE((short)152, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.RENDERED),
/*     */   
/* 334 */   FENCE_POTTERY_TALL_STONE_WALL((short)153, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.POTTERY),
/* 335 */   FENCE_POTTERY_PORTCULLIS((short)154, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.POTTERY),
/* 336 */   FENCE_POTTERY_HIGH_IRON_FENCE((short)155, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.POTTERY),
/* 337 */   FENCE_POTTERY_HIGH_IRON_FENCE_GATE((short)156, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.POTTERY),
/* 338 */   FENCE_POTTERY_STONE_PARAPET((short)157, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.POTTERY),
/* 339 */   FENCE_POTTERY_CHAIN_FENCE((short)158, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.POTTERY),
/*     */   
/* 341 */   FENCE_MARBLE_TALL_STONE_WALL((short)159, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_TALL, StructureMaterialEnum.MARBLE),
/* 342 */   FENCE_MARBLE_PORTCULLIS((short)160, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.MARBLE),
/* 343 */   FENCE_MARBLE_HIGH_IRON_FENCE((short)161, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL, StructureMaterialEnum.MARBLE),
/* 344 */   FENCE_MARBLE_HIGH_IRON_FENCE_GATE((short)162, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_IRON_BARS_TALL_GATE, StructureMaterialEnum.MARBLE),
/* 345 */   FENCE_MARBLE_STONE_PARAPET((short)163, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.MARBLE),
/* 346 */   FENCE_MARBLE_CHAIN_FENCE((short)164, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.MARBLE),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 351 */   FENCE_PLAN_SLATE_TALL_STONE_WALL((short)165, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.SLATE),
/* 352 */   FENCE_PLAN_SLATE_PORTCULLIS((short)166, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SLATE),
/* 353 */   FENCE_PLAN_SLATE_HIGH_IRON_FENCE((short)167, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.SLATE),
/* 354 */   FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE((short)168, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.SLATE),
/* 355 */   FENCE_PLAN_SLATE_STONE_PARAPET((short)169, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.SLATE),
/* 356 */   FENCE_PLAN_SLATE_CHAIN_FENCE((short)170, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.SLATE),
/*     */   
/* 358 */   FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL((short)171, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.ROUNDED_STONE),
/* 359 */   FENCE_PLAN_ROUNDED_STONE_PORTCULLIS((short)172, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.ROUNDED_STONE),
/* 360 */   FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE((short)173, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.ROUNDED_STONE),
/* 361 */   FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE((short)174, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.ROUNDED_STONE),
/* 362 */   FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET((short)175, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.ROUNDED_STONE),
/* 363 */   FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE((short)176, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.ROUNDED_STONE),
/*     */   
/* 365 */   FENCE_PLAN_SANDSTONE_TALL_STONE_WALL((short)177, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.SANDSTONE),
/* 366 */   FENCE_PLAN_SANDSTONE_PORTCULLIS((short)178, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SANDSTONE),
/* 367 */   FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE((short)179, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.SANDSTONE),
/* 368 */   FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE((short)180, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.SANDSTONE),
/* 369 */   FENCE_PLAN_SANDSTONE_STONE_PARAPET((short)181, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.SANDSTONE),
/* 370 */   FENCE_PLAN_SANDSTONE_CHAIN_FENCE((short)182, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.SANDSTONE),
/*     */   
/* 372 */   FENCE_PLAN_RENDERED_TALL_STONE_WALL((short)183, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.RENDERED),
/* 373 */   FENCE_PLAN_RENDERED_PORTCULLIS((short)184, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.RENDERED),
/* 374 */   FENCE_PLAN_RENDERED_HIGH_IRON_FENCE((short)185, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.RENDERED),
/* 375 */   FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE((short)186, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.RENDERED),
/* 376 */   FENCE_PLAN_RENDERED_STONE_PARAPET((short)187, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.RENDERED),
/* 377 */   FENCE_PLAN_RENDERED_CHAIN_FENCE((short)188, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.RENDERED),
/*     */   
/* 379 */   FENCE_PLAN_POTTERY_TALL_STONE_WALL((short)189, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.POTTERY),
/* 380 */   FENCE_PLAN_POTTERY_PORTCULLIS((short)190, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.POTTERY),
/* 381 */   FENCE_PLAN_POTTERY_HIGH_IRON_FENCE((short)191, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.POTTERY),
/* 382 */   FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE((short)192, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.POTTERY),
/* 383 */   FENCE_PLAN_POTTERY_STONE_PARAPET((short)193, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.POTTERY),
/* 384 */   FENCE_PLAN_POTTERY_CHAIN_FENCE((short)194, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.POTTERY),
/*     */   
/* 386 */   FENCE_PLAN_MARBLE_TALL_STONE_WALL((short)195, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_STONEWALL_HIGH, StructureMaterialEnum.MARBLE),
/* 387 */   FENCE_PLAN_MARBLE_PORTCULLIS((short)196, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.MARBLE),
/* 388 */   FENCE_PLAN_MARBLE_HIGH_IRON_FENCE((short)197, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL, StructureMaterialEnum.MARBLE),
/* 389 */   FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE((short)198, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_IRON_BARS_TALL_GATE, StructureMaterialEnum.MARBLE),
/* 390 */   FENCE_PLAN_MARBLE_STONE_PARAPET((short)199, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PARAPET, StructureMaterialEnum.MARBLE),
/* 391 */   FENCE_PLAN_MARBLE_CHAIN_FENCE((short)200, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.MARBLE),
/*     */ 
/*     */ 
/*     */   
/* 395 */   FLOWERBED_YELLOW((short)-112, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 396 */   FLOWERBED_ORANGE_RED((short)-113, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 397 */   FLOWERBED_PURPLE((short)-114, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 398 */   FLOWERBED_WHITE((short)-115, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 399 */   FLOWERBED_BLUE((short)-116, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 400 */   FLOWERBED_GREENISH_YELLOW((short)-117, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 401 */   FLOWERBED_WHITE_DOTTED((short)-118, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FLOWERBED, StructureMaterialEnum.FLOWER1),
/* 402 */   FENCE_MAGIC_FIRE((short)-119, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MAGIC_FENCE, StructureMaterialEnum.FIRE),
/* 403 */   FENCE_MAGIC_ICE((short)-120, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MAGIC_FENCE, StructureMaterialEnum.ICE),
/* 404 */   FENCE_PLAN_MEDIUM_CHAIN((short)-121, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_MEDIUM_CHAIN, StructureMaterialEnum.STONE),
/* 405 */   FENCE_MEDIUM_CHAIN((short)-122, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.MEDIUM_CHAIN, StructureMaterialEnum.STONE),
/* 406 */   FENCE_PLAN_PORTCULLIS((short)-123, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.FENCE_PLAN_PORTCULLIS, StructureMaterialEnum.STONE),
/* 407 */   FENCE_PORTCULLIS((short)-124, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.STONE),
/* 408 */   FENCE_RUBBLE((short)-125, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.RUBBLE, StructureMaterialEnum.STONE),
/* 409 */   FENCE_SIEGEWALL((short)-126, BuildingTypesEnum.ALLFENCES, StructureTypeEnum.SIEGWALL, StructureMaterialEnum.WOOD);
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
/* 421 */   private String modelPath = "";
/* 422 */   private String texturePath = "";
/* 423 */   private int icon = -1;
/*     */   
/*     */   public final boolean useNewStringBuilder = false;
/* 426 */   private final HashMap<String, StructureConstantsEnum> lookupMap = new HashMap<>();
/*     */   
/*     */   public final short value;
/*     */   public final BuildingTypesEnum structureType;
/*     */   
/*     */   StructureConstantsEnum(short _value, BuildingTypesEnum _structureType, StructureTypeEnum _type, StructureMaterialEnum _material) {
/* 432 */     this.value = _value;
/* 433 */     this.structureType = _structureType;
/* 434 */     this.type = _type;
/* 435 */     this.material = _material;
/*     */   }
/*     */   public final StructureTypeEnum type;
/*     */   public final StructureMaterialEnum material;
/*     */   
/*     */   public String getModelPath() {
/* 441 */     if (this.modelPath.equals(""))
/*     */     {
/* 443 */       this.modelPath = WallConstants.getModelName(this, (byte)0, 0, true);
/*     */     }
/*     */     
/* 446 */     return this.modelPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTexturePath() {
/* 451 */     if (this.texturePath.equals(""))
/*     */     {
/* 453 */       this.texturePath = WallConstants.getTextureName(this, true);
/*     */     }
/*     */     
/* 456 */     return this.texturePath;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconId() {
/* 461 */     if (this.icon == -1)
/*     */     {
/* 463 */       this.icon = WallConstants.getIconId(this, true);
/*     */     }
/*     */     
/* 466 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   private String buildModelPathString() {
/* 471 */     StringBuilder sb = new StringBuilder();
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
/* 484 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildTexturePathString() {
/* 490 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpperModelString() {
/* 495 */     return this.modelPath + ".upper";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDecayedTextureString() {
/* 500 */     return this.texturePath + ".decayed";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static StructureConstantsEnum getEnumByINDEX(short id) {
/* 506 */     if (id <= (values()).length && id >= 0)
/*     */     {
/* 508 */       return values()[id];
/*     */     }
/*     */     
/* 511 */     Logger.getGlobal().warning("Value not a valid id: " + id + " RETURNING WALL_WINDOW_STONE_PLAN(VAL=-31)!");
/*     */     
/* 513 */     return WALL_WINDOW_STONE_PLAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureConstantsEnum getEnumByValue(short value) {
/* 518 */     for (StructureConstantsEnum e : values()) {
/*     */       
/* 520 */       if (e.value == value) return e;
/*     */     
/*     */     } 
/* 523 */     Logger.getGlobal().warning("Reached default return value for value=" + value + " RETURNING WALL_WINDOW_STONE_PLAN(VAL=-31)!");
/* 524 */     return WALL_WINDOW_STONE_PLAN;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\StructureConstantsEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */