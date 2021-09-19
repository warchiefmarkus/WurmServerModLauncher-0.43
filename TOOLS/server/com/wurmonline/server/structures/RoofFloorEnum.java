/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.behaviours.ActionEntry;
/*     */ import com.wurmonline.server.behaviours.BuildMaterial;
/*     */ import com.wurmonline.server.behaviours.FloorBehaviour;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureConstants;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum RoofFloorEnum
/*     */ {
/*  22 */   WOOD_SHINGLE_ROOF(StructureConstants.FloorType.ROOF, "Wood shingle", false, StructureConstants.FloorMaterial.WOOD, (short)60),
/*  23 */   SLATE_SHINGLE_ROOF(StructureConstants.FloorType.ROOF, "Slate shingle", false, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/*  25 */   CLAY_SHIGLE_ROOF(StructureConstants.FloorType.ROOF, "Pottery shingle", false, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*     */   
/*  27 */   TATCHED_ROOF(StructureConstants.FloorType.ROOF, "Thatched", false, StructureConstants.FloorMaterial.THATCH, (short)60),
/*  28 */   WOODEN_PLANK_FLOOR(StructureConstants.FloorType.FLOOR, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
/*  29 */   WOODEN_PLANK_OPENING(StructureConstants.FloorType.OPENING, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
/*     */   
/*  31 */   CLAY_FLOOR(StructureConstants.FloorType.FLOOR, "Pottery brick", true, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*  32 */   CLAY_OPENING(StructureConstants.FloorType.OPENING, "Pottery brick", true, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*     */   
/*  34 */   SANDSTONE_SLAB_FLOOR(StructureConstants.FloorType.FLOOR, "Sandstone slab", true, StructureConstants.FloorMaterial.SANDSTONE_SLAB, (short)60),
/*     */   
/*  36 */   SANDSTONE_SLAB_OPENING(StructureConstants.FloorType.OPENING, "Sandstone slab", true, StructureConstants.FloorMaterial.SANDSTONE_SLAB, (short)60),
/*     */   
/*  38 */   STONE_SLAB_FLOOR(StructureConstants.FloorType.FLOOR, "Stone slab", true, StructureConstants.FloorMaterial.STONE_SLAB, (short)60),
/*     */   
/*  40 */   STONE_SLAB_OPENING(StructureConstants.FloorType.OPENING, "Stone slab", true, StructureConstants.FloorMaterial.STONE_SLAB, (short)60),
/*     */   
/*  42 */   STONE_BRICK_FLOOR(StructureConstants.FloorType.FLOOR, "Stone brick", true, StructureConstants.FloorMaterial.STONE_BRICK, (short)60),
/*     */   
/*  44 */   STONE_BRICK_OPENING(StructureConstants.FloorType.OPENING, "Stone brick", true, StructureConstants.FloorMaterial.STONE_BRICK, (short)60),
/*     */   
/*  46 */   SLATE_SLAB_FLOOR(StructureConstants.FloorType.FLOOR, "Slate slab", true, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/*  48 */   SLATE_SLAB_OPENING(StructureConstants.FloorType.OPENING, "Slate slab", true, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/*  50 */   MARBLE_FLOOR(StructureConstants.FloorType.FLOOR, "Marble slab", true, StructureConstants.FloorMaterial.MARBLE_SLAB, (short)60),
/*  51 */   MARBLE_OPENING(StructureConstants.FloorType.OPENING, "Marble slab", true, StructureConstants.FloorMaterial.MARBLE_SLAB, (short)60),
/*     */   
/*  53 */   WOODEN_PLANK_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
/*     */   
/*  55 */   CLAY_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Pottery brick", true, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*     */   
/*  57 */   SANDSTONE_SLAB_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Sandstone slab", true, StructureConstants.FloorMaterial.SANDSTONE_SLAB, (short)60),
/*     */   
/*  59 */   STONE_SLAB_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Stone slab", true, StructureConstants.FloorMaterial.STONE_SLAB, (short)60),
/*     */   
/*  61 */   STONE_BRICK_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Stone brick", true, StructureConstants.FloorMaterial.STONE_BRICK, (short)60),
/*     */   
/*  63 */   SLATE_SLAB_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Slate slab", true, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/*  65 */   MARBLE_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Marble slab", true, StructureConstants.FloorMaterial.MARBLE_SLAB, (short)60),
/*     */ 
/*     */ 
/*     */   
/*  69 */   STANDALONE_STAIRCASE(StructureConstants.FloorType.STAIRCASE, "Standalone", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */ 
/*     */   
/*  72 */   WIDE_STAIRCASE(StructureConstants.FloorType.WIDE_STAIRCASE, "No banisters", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */   
/*  74 */   WIDE_STAIRCASE_RIGHT(StructureConstants.FloorType.WIDE_STAIRCASE_RIGHT, "Right banisters", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */   
/*  76 */   WIDE_STAIRCASE_LEFT(StructureConstants.FloorType.WIDE_STAIRCASE_LEFT, "Left banisters", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */   
/*  78 */   WIDE_STAIRCASE_BOTH(StructureConstants.FloorType.WIDE_STAIRCASE_BOTH, "Both banisters", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */ 
/*     */   
/*  81 */   WOODEN_PLANK_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
/*     */   
/*  83 */   CLAY_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Pottery brick", true, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*     */   
/*  85 */   SANDSTONE_SLAB_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Sandstone slab", true, StructureConstants.FloorMaterial.SANDSTONE_SLAB, (short)60),
/*     */   
/*  87 */   STONE_SLAB_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Stone slab", true, StructureConstants.FloorMaterial.STONE_SLAB, (short)60),
/*     */   
/*  89 */   STONE_BRICK_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Stone brick", true, StructureConstants.FloorMaterial.STONE_BRICK, (short)60),
/*     */   
/*  91 */   SLATE_SLAB_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Slate slab", true, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/*  93 */   MARBLE_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Marble slab", true, StructureConstants.FloorMaterial.MARBLE_SLAB, (short)60),
/*     */   
/*  95 */   STANDALONE_RIGHT_STAIRCASE(StructureConstants.FloorType.RIGHT_STAIRCASE, "Standalone", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */ 
/*     */   
/*  98 */   WOODEN_PLANK_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
/*     */   
/* 100 */   CLAY_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Pottery brick", true, StructureConstants.FloorMaterial.CLAY_BRICK, (short)60),
/*     */   
/* 102 */   SANDSTONE_SLAB_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Sandstone slab", true, StructureConstants.FloorMaterial.SANDSTONE_SLAB, (short)60),
/*     */   
/* 104 */   STONE_SLAB_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Stone slab", true, StructureConstants.FloorMaterial.STONE_SLAB, (short)60),
/*     */   
/* 106 */   STONE_BRICK_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Stone brick", true, StructureConstants.FloorMaterial.STONE_BRICK, (short)60),
/*     */   
/* 108 */   SLATE_SLAB_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Slate slab", true, StructureConstants.FloorMaterial.SLATE_SLAB, (short)60),
/*     */   
/* 110 */   MARBLE_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Marble slab", true, StructureConstants.FloorMaterial.MARBLE_SLAB, (short)60),
/*     */   
/* 112 */   STANDALONE_LEFT_STAIRCASE(StructureConstants.FloorType.LEFT_STAIRCASE, "Standalone", true, StructureConstants.FloorMaterial.STANDALONE, (short)60),
/*     */ 
/*     */   
/* 115 */   WOODEN_PLANK_CLOCKWISE_STAIRCASE(StructureConstants.FloorType.CLOCKWISE_STAIRCASE, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
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
/* 128 */   WOODEN_PLANK_ANTICLOCKWISE_STAIRCASE(StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
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
/* 141 */   WOODEN_PLANK_CLOCKWISE_STAIRCASE_WITH(StructureConstants.FloorType.CLOCKWISE_STAIRCASE_WITH, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
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
/* 154 */   WOODEN_PLANK_ANTICLOCKWISE_STAIRCASE_WITH(StructureConstants.FloorType.ANTICLOCKWISE_STAIRCASE_WITH, "Wooden plank", true, StructureConstants.FloorMaterial.WOOD, (short)60),
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
/* 170 */   UNKNOWN(StructureConstants.FloorType.OPENING, "Unkown", true, StructureConstants.FloorMaterial.WOOD, (short)60);
/*     */   
/*     */   private final StructureConstants.FloorType type;
/*     */   private final boolean isFloor;
/*     */   private final StructureConstants.FloorMaterial material;
/*     */   private final short actionId;
/*     */   private final String name;
/*     */   private final String actionString;
/*     */   private final short icon;
/*     */   private static int[] emptyArr;
/*     */   
/*     */   static {
/* 182 */     emptyArr = new int[0];
/*     */   }
/*     */ 
/*     */   
/*     */   RoofFloorEnum(StructureConstants.FloorType type, String name, boolean isFloor, StructureConstants.FloorMaterial material, short icon) {
/* 187 */     this.type = type;
/* 188 */     this.isFloor = isFloor;
/* 189 */     this.material = material;
/* 190 */     this.actionId = (short)(20000 + this.material.getCode());
/* 191 */     this.name = StringUtil.format("%s %s", new Object[] { name, this.type.getName() });
/* 192 */     this.actionString = StringUtil.format("%s %s", new Object[] { "Building", StringUtil.toLowerCase(this.name) });
/* 193 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ActionEntry createActionEntry() {
/* 198 */     return ActionEntry.createEntry(this.actionId, this.name, this.actionString, emptyArr);
/*     */   }
/*     */ 
/*     */   
/*     */   public final StructureConstants.FloorType getType() {
/* 203 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final StructureConstants.FloorMaterial getMaterial() {
/* 208 */     return this.material;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 213 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isFloor() {
/* 218 */     return this.isFloor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getIcon() {
/* 223 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getActionId() {
/* 228 */     return this.actionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isValidTool(Item tool) {
/* 233 */     if (tool == null)
/* 234 */       return false; 
/* 235 */     if (tool.getTemplateId() == 176 || tool.getTemplateId() == 315)
/* 236 */       return true; 
/* 237 */     int[] valid = getValidToolsForMaterial(this.material);
/* 238 */     for (int v : valid) {
/*     */       
/* 240 */       if (v == tool.getTemplateId()) {
/* 241 */         return true;
/*     */       }
/*     */     } 
/* 244 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final List<RoofFloorEnum> getRoofsByTool(Item tool) {
/* 249 */     List<RoofFloorEnum> list = new ArrayList<>();
/* 250 */     if (tool == null) {
/* 251 */       return list;
/*     */     }
/* 253 */     for (RoofFloorEnum en : values()) {
/*     */       
/* 255 */       if (!en.isFloor() && en.isValidTool(tool) && en != UNKNOWN)
/* 256 */         list.add(en); 
/*     */     } 
/* 258 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RoofFloorEnum getByFloorType(Floor floor) {
/* 263 */     for (RoofFloorEnum en : values()) {
/*     */       
/* 265 */       if (en.getType() == floor.getType() && en.getMaterial() == floor.getMaterial())
/* 266 */         return en; 
/*     */     } 
/* 268 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<BuildMaterial> getMaterialsNeeded(Floor floor) {
/* 273 */     List<BuildMaterial> billOfMaterial = FloorBehaviour.getRequiredMaterialsAtState(floor);
/* 274 */     List<BuildMaterial> needed = new ArrayList<>();
/* 275 */     for (BuildMaterial mat : billOfMaterial) {
/*     */       
/* 277 */       if (mat.getNeededQuantity() > 0)
/* 278 */         needed.add(mat); 
/*     */     } 
/* 280 */     return needed;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildMaterial> getTotalMaterialsNeeded() {
/*     */     List<BuildMaterial> billOfMaterial;
/* 286 */     if (this.type == StructureConstants.FloorType.ROOF) {
/* 287 */       billOfMaterial = FloorBehaviour.getRequiredMaterialsForRoof(this.material);
/*     */     } else {
/* 289 */       billOfMaterial = FloorBehaviour.getRequiredMaterialsForFloor(this.type, this.material);
/*     */     } 
/* 291 */     return billOfMaterial;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean canBuildFloorRoof(Floor floor, RoofFloorEnum en, Creature performer) {
/* 296 */     Skill skill = FloorBehaviour.getBuildSkill(en.getType(), en.getMaterial(), performer);
/* 297 */     if (skill == null) {
/* 298 */       return false;
/*     */     }
/* 300 */     if (skill.getKnowledge(0.0D) < FloorBehaviour.getRequiredBuildSkillForFloorType(en.getMaterial())) {
/* 301 */       return false;
/*     */     }
/* 303 */     if (!FloorBehaviour.mayPlanAtLevel(performer, floor.getFloorLevel(), skill, floor.isRoof(), false))
/* 304 */       return false; 
/* 305 */     if (FloorBehaviour.getRequiredBuildSkillForFloorLevel(floor.getFloorLevel(), floor.isRoof()) > skill.getKnowledge(0.0D)) {
/* 306 */       return false;
/*     */     }
/* 308 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNeededSkillNumber() {
/* 313 */     if (this.type == StructureConstants.FloorType.ROOF)
/*     */     {
/* 315 */       return FloorBehaviour.getSkillForRoof(this.material);
/*     */     }
/*     */ 
/*     */     
/* 319 */     return FloorBehaviour.getSkillForFloor(this.material);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Floor getFloorOrRoofFromId(long floorId) {
/* 325 */     int x = Tiles.decodeTileX(floorId);
/* 326 */     int y = Tiles.decodeTileY(floorId);
/* 327 */     int heightOffset = Tiles.decodeHeightOffset(floorId);
/* 328 */     byte layer = Tiles.decodeLayer(floorId);
/*     */     
/* 330 */     Floor[] floors = Zones.getFloorsAtTile(x, y, heightOffset, heightOffset, layer);
/* 331 */     if (floors != null && floors.length > 0)
/* 332 */       return floors[0]; 
/* 333 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final List<RoofFloorEnum> getFloorByToolAndType(Item tool, StructureConstants.FloorType fType) {
/* 338 */     List<RoofFloorEnum> list = new ArrayList<>();
/* 339 */     if (tool == null) {
/* 340 */       return list;
/*     */     }
/* 342 */     for (RoofFloorEnum en : values()) {
/*     */       
/* 344 */       if (en.isFloor() && en != UNKNOWN)
/*     */       {
/* 346 */         if (en.getType() == fType && en.isValidTool(tool))
/* 347 */           list.add(en); 
/*     */       }
/*     */     } 
/* 350 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RoofFloorEnum getFloorRoofByTypeAndMaterial(StructureConstants.FloorType type, StructureConstants.FloorMaterial material) {
/* 355 */     for (RoofFloorEnum en : values()) {
/*     */       
/* 357 */       if (en.getType() == type && en.getMaterial() == material)
/* 358 */         return en; 
/*     */     } 
/* 360 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int[] getValidToolsForMaterial(StructureConstants.FloorMaterial material) {
/* 365 */     switch (material) {
/*     */       
/*     */       case METAL_STEEL:
/* 368 */         return new int[] { 62, 63 };
/*     */       case CLAY_BRICK:
/* 370 */         return new int[] { 493 };
/*     */       case SLATE_SLAB:
/* 372 */         return new int[] { 493 };
/*     */       case STONE_BRICK:
/* 374 */         return new int[] { 493 };
/*     */       case THATCH:
/* 376 */         return new int[] { 62, 63 };
/*     */       case WOOD:
/* 378 */         return new int[] { 62, 63 };
/*     */       case STONE_SLAB:
/* 380 */         return new int[] { 493 };
/*     */       case METAL_COPPER:
/* 382 */         return new int[] { 62, 63 };
/*     */       case METAL_IRON:
/* 384 */         return new int[] { 62, 63 };
/*     */       case SANDSTONE_SLAB:
/* 386 */         return new int[] { 493 };
/*     */       case MARBLE_SLAB:
/* 388 */         return new int[] { 493 };
/*     */       case METAL_GOLD:
/* 390 */         return new int[] { 62, 63 };
/*     */       case METAL_SILVER:
/* 392 */         return new int[] { 62, 63 };
/*     */       case STANDALONE:
/* 394 */         return new int[] { 62, 63 };
/*     */     } 
/* 396 */     return new int[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\RoofFloorEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */