/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.behaviours.ActionEntry;
/*     */ import com.wurmonline.server.behaviours.FloorBehaviour;
/*     */ import com.wurmonline.server.behaviours.WurmPermissions;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.shared.constants.StructureMaterialEnum;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum WallEnum
/*     */ {
/*  25 */   WALL_WOOD((short)612, StructureTypeEnum.SOLID, StructureMaterialEnum.WOOD, "Wooden wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  28 */   WALL_STONE((short)617, StructureTypeEnum.SOLID, StructureMaterialEnum.STONE, "Stone wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  31 */   WALL_PLAIN_STONE((short)648, StructureTypeEnum.SOLID, StructureMaterialEnum.PLAIN_STONE, "Plain stone wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  34 */   WALL_TIMBER_FRAMED((short)622, StructureTypeEnum.SOLID, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  37 */   WALL_SLATE((short)772, StructureTypeEnum.SOLID, StructureMaterialEnum.SLATE, "Slate wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  40 */   WALL_ROUNDED_STONE((short)784, StructureTypeEnum.SOLID, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  43 */   WALL_POTTERY((short)796, StructureTypeEnum.SOLID, StructureMaterialEnum.POTTERY, "Pottery wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  46 */   WALL_SANDSTONE((short)808, StructureTypeEnum.SOLID, StructureMaterialEnum.SANDSTONE, "Sandstone wall", "building wall", (short)60, false),
/*     */ 
/*     */   
/*  49 */   WALL_MARBLE((short)820, StructureTypeEnum.SOLID, StructureMaterialEnum.MARBLE, "Marble wall", "building wall", (short)60, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   WINDOW_WOOD((short)613, StructureTypeEnum.WINDOW, StructureMaterialEnum.WOOD, "Wooden window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  57 */   WINDOW_STONE((short)618, StructureTypeEnum.WINDOW, StructureMaterialEnum.STONE, "Stone window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  60 */   WINDOW_PLAIN_STONE((short)649, StructureTypeEnum.WINDOW, StructureMaterialEnum.PLAIN_STONE, "Plain stone window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  63 */   WINDOW_TIMBER_FRAMED((short)623, StructureTypeEnum.WINDOW, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  66 */   WINDOW_SLATE((short)773, StructureTypeEnum.WINDOW, StructureMaterialEnum.SLATE, "Slate window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  69 */   WINDOW_ROUNDED_STONE((short)785, StructureTypeEnum.WINDOW, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  72 */   WINDOW_POTTERY((short)797, StructureTypeEnum.WINDOW, StructureMaterialEnum.POTTERY, "Pottery window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  75 */   WINDOW_SANDSTONE((short)809, StructureTypeEnum.WINDOW, StructureMaterialEnum.SANDSTONE, "Sandstone window", "building window", (short)60, false),
/*     */ 
/*     */   
/*  78 */   WINDOW_MARBLE((short)821, StructureTypeEnum.WINDOW, StructureMaterialEnum.MARBLE, "Marble window", "building window", (short)60, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   NARROW_WINDOW_PLAIN_STONE((short)650, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.PLAIN_STONE, "Plain stone window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */   
/*  86 */   NARROW_WINDOW_SLATE((short)774, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.SLATE, "Slate window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */   
/*  89 */   NARROW_WINDOW_ROUNDED_STONE((short)786, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */   
/*  92 */   NARROW_WINDOW_POTTERY((short)798, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.POTTERY, "Pottery window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */   
/*  95 */   NARROW_WINDOW_SANDSTONE((short)810, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.SANDSTONE, "Sandstone window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */   
/*  98 */   NARROW_WINDOW_MARBLE((short)822, StructureTypeEnum.NARROW_WINDOW, StructureMaterialEnum.MARBLE, "Marble window narrow", "building narrow window", (short)60, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   WIDE_WINDOW_WOOD((short)680, StructureTypeEnum.WIDE_WINDOW, StructureMaterialEnum.WOOD, "Wooden window wide", "building wide window", (short)60, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   DOOR_WOOD((short)614, StructureTypeEnum.DOOR, StructureMaterialEnum.WOOD, "Wooden door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 111 */   DOOR_STONE((short)619, StructureTypeEnum.DOOR, StructureMaterialEnum.STONE, "Stone door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 114 */   DOOR_PLAIN_STONE((short)651, StructureTypeEnum.DOOR, StructureMaterialEnum.PLAIN_STONE, "Plain stone door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 117 */   DOOR_TIMBER_FRAMED((short)624, StructureTypeEnum.DOOR, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 120 */   DOOR_SLATE((short)775, StructureTypeEnum.DOOR, StructureMaterialEnum.SLATE, "Slate door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 123 */   DOOR_ROUNDED_STONE((short)787, StructureTypeEnum.DOOR, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 126 */   DOOR_POTTERY((short)799, StructureTypeEnum.DOOR, StructureMaterialEnum.POTTERY, "Pottery door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 129 */   DOOR_SANDSTONE((short)811, StructureTypeEnum.DOOR, StructureMaterialEnum.SANDSTONE, "Sandstone door", "building door", (short)60, true),
/*     */ 
/*     */   
/* 132 */   DOOR_MARBLE((short)823, StructureTypeEnum.DOOR, StructureMaterialEnum.MARBLE, "Marble door", "building door", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   DOUBLE_DOOR_WOOD((short)615, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.WOOD, "Wooden door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 140 */   DOUBLE_DOOR_STONE((short)620, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.STONE, "Stone door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 143 */   DOUBLE_DOOR_PLAIN_STONE((short)652, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.PLAIN_STONE, "Plain stone door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 146 */   DOUBLE_DOOR_TIMBER_FRAMED((short)625, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 149 */   DOUBLE_DOOR_SLATE((short)776, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.SLATE, "Slate door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 152 */   DOUBLE_DOOR_ROUNDED_STONE((short)788, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 155 */   DOUBLE_DOOR_POTTERY((short)800, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.POTTERY, "Pottery door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 158 */   DOUBLE_DOOR_SANDSTONE((short)812, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.SANDSTONE, "Sandstone door double", "building double door", (short)60, true),
/*     */ 
/*     */   
/* 161 */   DOUBLE_DOOR_MARBLE((short)824, StructureTypeEnum.DOUBLE_DOOR, StructureMaterialEnum.MARBLE, "Marble door double", "building arch", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   ARCHED_WOOD((short)616, StructureTypeEnum.ARCHED, StructureMaterialEnum.WOOD, "Wooden arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 169 */   ARCHED_STONE((short)621, StructureTypeEnum.ARCHED, StructureMaterialEnum.STONE, "Stone arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 172 */   ARCHED_PLAIN_STONE((short)653, StructureTypeEnum.ARCHED, StructureMaterialEnum.PLAIN_STONE, "Plain stone arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 175 */   ARCHED_TIMBER_FRAMED((short)626, StructureTypeEnum.ARCHED, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 178 */   ARCHED_SLATE((short)777, StructureTypeEnum.ARCHED, StructureMaterialEnum.SLATE, "Slate arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 181 */   ARCHED_ROUNDED_STONE((short)789, StructureTypeEnum.ARCHED, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 184 */   ARCHED_POTTERY((short)801, StructureTypeEnum.ARCHED, StructureMaterialEnum.POTTERY, "Pottery arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 187 */   ARCHED_SANDSTONE((short)813, StructureTypeEnum.ARCHED, StructureMaterialEnum.SANDSTONE, "Sandstone arch", "building arch", (short)60, true),
/*     */ 
/*     */   
/* 190 */   ARCHED_MARBLE((short)825, StructureTypeEnum.ARCHED, StructureMaterialEnum.MARBLE, "Marble arch", "building arch", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   LEFT_ARCHED_WOOD((short)760, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.WOOD, "Wooden arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 198 */   LEFT_ARCHED_STONE((short)763, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.STONE, "Stone arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 201 */   LEFT_ARCHED_PLAIN_STONE((short)769, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.PLAIN_STONE, "Plain stone arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 204 */   LEFT_ARCHED_TIMBER_FRAMED((short)766, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 207 */   LEFT_ARCHED_SLATE((short)781, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.SLATE, "Slate arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 210 */   LEFT_ARCHED_ROUNDED_STONE((short)793, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 213 */   LEFT_ARCHED_POTTERY((short)805, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.POTTERY, "Pottery arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 216 */   LEFT_ARCHED_SANDSTONE((short)817, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.SANDSTONE, "Sandstone arch left", "building arch left", (short)60, true),
/*     */ 
/*     */   
/* 219 */   LEFT_ARCHED_MARBLE((short)829, StructureTypeEnum.ARCHED_LEFT, StructureMaterialEnum.MARBLE, "Marble arch left", "building arch left", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   RIGHT_ARCHED_WOOD((short)761, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.WOOD, "Wooden arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 227 */   RIGHT_ARCHED_STONE((short)764, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.STONE, "Stone arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 230 */   RIGHT_ARCHED_PLAIN_STONE((short)770, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.PLAIN_STONE, "Plain stone arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 233 */   RIGHT_ARCHED_TIMBER_FRAMED((short)767, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 236 */   RIGHT_ARCHED_SLATE((short)782, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.SLATE, "Slate arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 239 */   RIGHT_ARCHED_ROUNDED_STONE((short)794, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 242 */   RIGHT_ARCHED_POTTERY((short)806, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.POTTERY, "Pottery arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 245 */   RIGHT_ARCHED_SANDSTONE((short)818, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.SANDSTONE, "Sandstone arch right", "building arch right", (short)60, true),
/*     */ 
/*     */   
/* 248 */   RIGHT_ARCHED_MARBLE((short)830, StructureTypeEnum.ARCHED_RIGHT, StructureMaterialEnum.MARBLE, "Marble arch right", "building arch right", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   T_ARCHED_WOOD((short)762, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.WOOD, "Wooden arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 256 */   T_ARCHED_STONE((short)765, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.STONE, "Stone arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 259 */   T_ARCHED_PLAIN_STONE((short)771, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.PLAIN_STONE, "Plain stone arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 262 */   T_ARCHED_TIMBER_FRAMED((short)768, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 265 */   T_ARCHED_SLATE((short)783, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.SLATE, "Slate arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 268 */   T_ARCHED_ROUNDED_STONE((short)795, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 271 */   T_ARCHED_POTTERY((short)807, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.POTTERY, "Pottery arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 274 */   T_ARCHED_SANDSTONE((short)819, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.SANDSTONE, "Sandstone arch T", "building arch T", (short)60, true),
/*     */ 
/*     */   
/* 277 */   T_ARCHED_MARBLE((short)831, StructureTypeEnum.ARCHED_T, StructureMaterialEnum.MARBLE, "Marble arch T", "building arch T", (short)60, true),
/*     */ 
/*     */ 
/*     */   
/* 281 */   WALL_PLAN((short)0, StructureTypeEnum.PLAN, StructureMaterialEnum.WOOD, "Wall plan", "planning", (short)60, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 286 */   PORTCULLIS_PLAIN_STONE((short)655, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.PLAIN_STONE, "Plain stone portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 289 */   PORTCULLIS_STONE((short)657, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.STONE, "Stone portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 292 */   PORTCULLIS_WOOD((short)658, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.WOOD, "Wooden portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 295 */   PORTCULLIS_SLATE((short)778, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SLATE, "Slate portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 298 */   PORTCULLIS_ROUNDED_STONE((short)790, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 301 */   PORTCULLIS_POTTERY((short)802, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.POTTERY, "Pottery portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 304 */   PORTCULLIS_SANDSTONE((short)814, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.SANDSTONE, "Sandstone portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */   
/* 307 */   PORTCULLIS_MARBLE((short)826, StructureTypeEnum.PORTCULLIS, StructureMaterialEnum.MARBLE, "Marble portcullis", "building portcullis", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 312 */   BARRED_PLAIN_STONE((short)656, StructureTypeEnum.BARRED, StructureMaterialEnum.PLAIN_STONE, "Plain stone wall barred", "building wall barred", (short)60, false),
/*     */ 
/*     */   
/* 315 */   BARRED_SLATE((short)779, StructureTypeEnum.BARRED, StructureMaterialEnum.SLATE, "Slate wall barred", "building wall barred", (short)60, true),
/*     */ 
/*     */   
/* 318 */   BARRED_ROUNDED_STONE((short)791, StructureTypeEnum.BARRED, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone wall barred", "building wall barred", (short)60, true),
/*     */ 
/*     */   
/* 321 */   BARRED_POTTERY((short)803, StructureTypeEnum.BARRED, StructureMaterialEnum.POTTERY, "Pottery wall barred", "building wall barred", (short)60, true),
/*     */ 
/*     */   
/* 324 */   BARRED_SANDSTONE((short)815, StructureTypeEnum.BARRED, StructureMaterialEnum.SANDSTONE, "Sandstone wall barred", "building wall barred", (short)60, true),
/*     */ 
/*     */   
/* 327 */   BARRED_MARBLE((short)827, StructureTypeEnum.BARRED, StructureMaterialEnum.MARBLE, "Marble wall barred", "building wall barred", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   TIMBER_FRAMED_JETTY((short)677, StructureTypeEnum.JETTY, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed jetty", "building jetty", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   ORIEL_DECORATED_STONE((short)678, StructureTypeEnum.ORIEL, StructureMaterialEnum.STONE, "Stone oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 340 */   ORIEL_PLAIN_STONE((short)681, StructureTypeEnum.ORIEL, StructureMaterialEnum.PLAIN_STONE, "Plain stone oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 343 */   ORIEL_SLATE((short)780, StructureTypeEnum.ORIEL, StructureMaterialEnum.SLATE, "Slate oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 346 */   ORIEL_ROUNDED_STONE((short)792, StructureTypeEnum.ORIEL, StructureMaterialEnum.ROUNDED_STONE, "Rounded stone oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 349 */   ORIEL_POTTERY((short)804, StructureTypeEnum.ORIEL, StructureMaterialEnum.POTTERY, "Pottery oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 352 */   ORIEL_SANDSTONE((short)816, StructureTypeEnum.ORIEL, StructureMaterialEnum.SANDSTONE, "Sandstone oriel", "building oriel", (short)60, true),
/*     */ 
/*     */   
/* 355 */   ORIEL_MARBLE((short)828, StructureTypeEnum.ORIEL, StructureMaterialEnum.MARBLE, "Marble oriel", "building oriel", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 360 */   CANOPY_WOOD((short)679, StructureTypeEnum.CANOPY_DOOR, StructureMaterialEnum.WOOD, "Wooden canopy", "building canopy", (short)60, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   TIMBER_FRAMED_BALCONY((short)676, StructureTypeEnum.BALCONY, StructureMaterialEnum.TIMBER_FRAMED, "Timber framed balcony", "building balcony", (short)60, true);
/*     */   
/*     */   private final short actionId;
/*     */   
/*     */   private final short icon;
/*     */   
/*     */   private final StructureTypeEnum type;
/*     */   
/*     */   private final StructureMaterialEnum material;
/*     */   private final String name;
/*     */   private final String actionString;
/*     */   private final boolean isDoor;
/*     */   private static int[] emptyArr;
/*     */   
/*     */   static {
/* 380 */     emptyArr = new int[0];
/*     */   }
/*     */ 
/*     */   
/*     */   WallEnum(short actionId, StructureTypeEnum type, StructureMaterialEnum material, String name, String actionString, short icon, boolean isDoor) {
/* 385 */     this.type = type;
/* 386 */     this.icon = icon;
/* 387 */     this.material = material;
/* 388 */     this.name = name;
/* 389 */     this.actionString = actionString;
/* 390 */     this.isDoor = isDoor;
/* 391 */     this.actionId = actionId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WallEnum> getWallsByTool(Creature performer, Item tool, boolean needsDoor, boolean hasAFence) {
/* 397 */     StructureMaterialEnum[] mats = getMaterialsFromToolType(tool, performer);
/* 398 */     List<WallEnum> list = new ArrayList<>();
/* 399 */     for (WallEnum en : values()) {
/*     */       
/* 401 */       if (en.getType() != StructureTypeEnum.PLAN)
/*     */       {
/*     */         
/* 404 */         if (!hasAFence || Wall.isArched(en.getType()))
/*     */         {
/* 406 */           for (StructureMaterialEnum mat : mats) {
/*     */             
/* 408 */             if (needsDoor) {
/*     */               
/* 410 */               if (en.getMaterial() == mat && en.isDoor()) {
/* 411 */                 list.add(en);
/*     */               
/*     */               }
/*     */             }
/* 415 */             else if (en.getMaterial() == mat) {
/* 416 */               list.add(en);
/*     */             } 
/*     */           }  }  } 
/*     */     } 
/* 420 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<WallEnum> getWallsByToolAndMaterial(Creature performer, Item tool, boolean needsDoor, boolean hasAFence, StructureMaterialEnum material) {
/* 426 */     StructureMaterialEnum[] mats = getMaterialsFromToolType(tool, performer);
/* 427 */     List<WallEnum> list = new ArrayList<>();
/* 428 */     for (WallEnum en : values()) {
/*     */       
/* 430 */       if (en.getType() != StructureTypeEnum.PLAN)
/*     */       {
/* 432 */         if (en.getMaterial() == material)
/*     */         {
/*     */           
/* 435 */           if (!hasAFence || Wall.isArched(en.getType()))
/*     */           {
/* 437 */             for (StructureMaterialEnum mat : mats) {
/*     */               
/* 439 */               if (needsDoor) {
/*     */                 
/* 441 */                 if (en.getMaterial() == mat && en.isDoor()) {
/* 442 */                   list.add(en);
/*     */                 
/*     */                 }
/*     */               }
/* 446 */               else if (en.getMaterial() == mat) {
/* 447 */                 list.add(en);
/*     */               } 
/*     */             }  }  }  } 
/*     */     } 
/* 451 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canBuildWall(Wall wall, StructureMaterialEnum material, Creature performer) {
/* 456 */     int skillNumber = getSkillNumber(material);
/* 457 */     Skill skill = performer.getSkills().getSkillOrLearn(skillNumber);
/* 458 */     if (skill == null) {
/* 459 */       return false;
/*     */     }
/* 461 */     if (skillNumber == 1013)
/*     */     {
/* 463 */       if (skill.getKnowledge(0.0D) < 30.0D) {
/* 464 */         return false;
/*     */       }
/*     */     }
/* 467 */     if (FloorBehaviour.getRequiredBuildSkillForFloorLevel(wall.getFloorLevel(), false) > skill.getKnowledge(0.0D)) {
/* 468 */       return false;
/*     */     }
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canBuildWall(Wall wall, Creature performer) {
/* 475 */     return canBuildWall(wall, wall.getMaterial(), performer);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getSkillNumber() {
/* 480 */     return getSkillNumber(this.material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getSkillNumber(StructureMaterialEnum material) {
/* 485 */     if (material == StructureMaterialEnum.STONE || material == StructureMaterialEnum.PLAIN_STONE || material == StructureMaterialEnum.SLATE || material == StructureMaterialEnum.ROUNDED_STONE || material == StructureMaterialEnum.POTTERY || material == StructureMaterialEnum.SANDSTONE || material == StructureMaterialEnum.RENDERED || material == StructureMaterialEnum.MARBLE)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 493 */       return 1013;
/*     */     }
/* 495 */     return 1005;
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
/*     */   public static WallEnum getWall(StructureTypeEnum type, StructureMaterialEnum material) {
/* 508 */     for (WallEnum en : values()) {
/*     */       
/* 510 */       if (en.getType() == type && en.getMaterial() == material)
/* 511 */         return en; 
/*     */     } 
/* 513 */     return WALL_PLAN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WallEnum getWallByActionId(short actionId) {
/* 523 */     for (WallEnum en : values()) {
/*     */       
/* 525 */       if (en.getActionId() == actionId)
/* 526 */         return en; 
/*     */     } 
/* 528 */     return WALL_PLAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] getTotalMaterialsNeeded(WallEnum en) {
/* 533 */     return new int[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getMaterialsNeeded(Wall wall) {
/* 539 */     int needed = (wall.getFinalState()).state - (wall.getState()).state;
/* 540 */     if (wall.isHalfArch())
/*     */     {
/*     */ 
/*     */       
/* 544 */       if (wall.isWood())
/*     */       {
/* 546 */         if ((wall.getState()).state <= 1)
/* 547 */           return new int[] { 217, 1, 22, needed - 1 }; 
/*     */       }
/*     */     }
/* 550 */     if (wall.isWood())
/*     */     {
/* 552 */       return new int[] { 22, needed };
/*     */     }
/*     */     
/* 555 */     if (wall.isTimberFramed()) {
/*     */       
/* 557 */       if (needed > 20)
/*     */       {
/* 559 */         return new int[] { 860, needed - 20, 620, 10, 130, 20 };
/*     */       }
/*     */       
/* 562 */       if (needed > 10)
/*     */       {
/* 564 */         return new int[] { 620, needed - 10, 130, needed - 10 + 10 };
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 569 */       return new int[] { 130, needed };
/*     */     } 
/*     */     
/* 572 */     return new int[] { wall.getBrickFromType(), needed, 492, needed };
/*     */   }
/*     */ 
/*     */   
/*     */   public final int[] getTotalMaterialsNeeded() {
/* 577 */     if (Wall.isHalfArch(this.type)) {
/*     */ 
/*     */       
/* 580 */       if (this.material == StructureMaterialEnum.WOOD)
/* 581 */         return new int[] { 860, 1, 22, 19, 217, 1 }; 
/* 582 */       if (this.material == StructureMaterialEnum.TIMBER_FRAMED) {
/* 583 */         return new int[] { 860, 6, 620, 10, 130, 20 };
/*     */       }
/* 585 */       return new int[] { 681, 1, Wall.getBrickFromType(this.material), 20, 492, 20 };
/*     */     } 
/* 587 */     if (this.material == StructureMaterialEnum.WOOD)
/* 588 */       return new int[] { 22, 20, 217, 1 }; 
/* 589 */     if (this.material == StructureMaterialEnum.TIMBER_FRAMED) {
/* 590 */       return new int[] { 860, 5, 620, 10, 130, 20 };
/*     */     }
/* 592 */     return new int[] { Wall.getBrickFromType(this.material), 20, 492, 20 };
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCorrectTool(WallEnum wall, Creature performer, Item tool) {
/* 597 */     if (tool == null)
/* 598 */       return false; 
/* 599 */     List<Integer> tools = getToolsForWall(wall, performer);
/* 600 */     for (Integer t : tools) {
/*     */       
/* 602 */       if (t.intValue() == tool.getTemplateId()) {
/* 603 */         return true;
/*     */       }
/*     */     } 
/* 606 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCorrectToolForType(Item tool, Creature performer) {
/* 611 */     List<Integer> list = getToolsForWall(this, performer);
/* 612 */     if (list.contains(Integer.valueOf(tool.getTemplateId())))
/* 613 */       return true; 
/* 614 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<Integer> getToolsForWall(WallEnum wall, Creature performer) {
/* 619 */     List<Integer> list = new ArrayList<>();
/* 620 */     if (wall.getType() == StructureTypeEnum.PLAN) {
/*     */       
/* 622 */       list.add(Integer.valueOf(493));
/* 623 */       list.add(Integer.valueOf(62));
/* 624 */       list.add(Integer.valueOf(63));
/*     */       
/* 626 */       if (performer != null)
/*     */       {
/* 628 */         if (performer.getPower() >= 2 && Servers.isThisATestServer())
/*     */         {
/* 630 */           list.add(Integer.valueOf(315));
/*     */         }
/*     */         
/* 633 */         if (WurmPermissions.mayUseGMWand(performer))
/*     */         {
/* 635 */           list.add(Integer.valueOf(176));
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 641 */     else if (wall.getMaterial() == StructureMaterialEnum.WOOD || wall
/* 642 */       .getMaterial() == StructureMaterialEnum.TIMBER_FRAMED) {
/*     */       
/* 644 */       list.add(Integer.valueOf(62));
/* 645 */       list.add(Integer.valueOf(63));
/*     */       
/* 647 */       if (performer != null)
/*     */       {
/* 649 */         if (performer.getPower() >= 2 && Servers.isThisATestServer())
/*     */         {
/* 651 */           list.add(Integer.valueOf(315));
/*     */         }
/*     */         
/* 654 */         if (WurmPermissions.mayUseGMWand(performer))
/*     */         {
/* 656 */           list.add(Integer.valueOf(176));
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 662 */       list.add(Integer.valueOf(493));
/* 663 */       if (performer != null)
/*     */       {
/* 665 */         if (WurmPermissions.mayUseGMWand(performer))
/*     */         {
/* 667 */           list.add(Integer.valueOf(176));
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 672 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureMaterialEnum[] getMaterialsFromToolType(Item tool, Creature performer) {
/* 677 */     switch (tool.getTemplateId()) {
/*     */       
/*     */       case 62:
/*     */       case 63:
/* 681 */         return new StructureMaterialEnum[] { StructureMaterialEnum.TIMBER_FRAMED, StructureMaterialEnum.WOOD };
/*     */       
/*     */       case 493:
/* 684 */         return new StructureMaterialEnum[] { StructureMaterialEnum.MARBLE, StructureMaterialEnum.PLAIN_STONE, StructureMaterialEnum.RENDERED, StructureMaterialEnum.POTTERY, StructureMaterialEnum.ROUNDED_STONE, StructureMaterialEnum.SANDSTONE, StructureMaterialEnum.SLATE, StructureMaterialEnum.STONE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 176:
/* 695 */         if (WurmPermissions.mayUseGMWand(performer))
/*     */         {
/* 697 */           return new StructureMaterialEnum[] { StructureMaterialEnum.MARBLE, StructureMaterialEnum.PLAIN_STONE, StructureMaterialEnum.RENDERED, StructureMaterialEnum.POTTERY, StructureMaterialEnum.ROUNDED_STONE, StructureMaterialEnum.SANDSTONE, StructureMaterialEnum.SLATE, StructureMaterialEnum.STONE, StructureMaterialEnum.TIMBER_FRAMED, StructureMaterialEnum.WOOD };
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
/* 718 */         return new StructureMaterialEnum[0];
/*     */ 
/*     */       
/*     */       case 315:
/* 722 */         if (performer.getPower() >= 2 && Servers.isThisATestServer())
/*     */         {
/* 724 */           return new StructureMaterialEnum[] { StructureMaterialEnum.WOOD };
/*     */         }
/* 726 */         return new StructureMaterialEnum[0];
/*     */     } 
/*     */     
/* 729 */     return new StructureMaterialEnum[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isDoor() {
/* 735 */     return this.isDoor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTimber() {
/* 740 */     return (this.material == StructureMaterialEnum.TIMBER_FRAMED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isUndecoratedStone() {
/* 745 */     return (this.material == StructureMaterialEnum.PLAIN_STONE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final StructureTypeEnum getType() {
/* 750 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final StructureMaterialEnum getMaterial() {
/* 755 */     return this.material;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 760 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getIcon() {
/* 765 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getActionId() {
/* 770 */     return this.actionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ActionEntry createActionEntry() {
/* 775 */     return ActionEntry.createEntry(this.actionId, this.name, this.actionString, emptyArr);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\WallEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */