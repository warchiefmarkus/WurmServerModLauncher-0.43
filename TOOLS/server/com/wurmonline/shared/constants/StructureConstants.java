/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public interface StructureConstants
/*     */ {
/*     */   public static final byte STRUCTURE_HOUSE = 0;
/*     */   public static final byte STRUCTURE_BRIDGE = 1;
/*     */   
/*     */   public enum FloorMaterial
/*     */   {
/*  54 */     WOOD((byte)0, "Wood", "wood"),
/*  55 */     STONE_BRICK((byte)1, "Stone brick", "stone_brick"),
/*  56 */     SANDSTONE_SLAB((byte)2, "Sandstone slab", "sandstone_slab"),
/*  57 */     SLATE_SLAB((byte)3, "Slate slab", "slate_slab"),
/*  58 */     THATCH((byte)4, "Thatch", "thatch"),
/*  59 */     METAL_IRON((byte)5, "Iron", "metal_iron"),
/*  60 */     METAL_STEEL((byte)6, "Steel", "metal_steel"),
/*  61 */     METAL_COPPER((byte)7, "Copper", "metal_copper"),
/*  62 */     CLAY_BRICK((byte)8, "Clay brick", "clay_brick"),
/*  63 */     METAL_GOLD((byte)9, "Gold", "metal_gold"),
/*  64 */     METAL_SILVER((byte)10, "Silver", "metal_silver"),
/*  65 */     MARBLE_SLAB((byte)11, "Marble slab", "marble_slab"),
/*  66 */     STANDALONE((byte)12, "Standalone", "standalone"),
/*  67 */     STONE_SLAB((byte)13, "Stone slab", "stone_slab");
/*     */ 
/*     */ 
/*     */     
/*     */     private byte material;
/*     */ 
/*     */     
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private String modelName;
/*     */ 
/*     */ 
/*     */     
/*     */     FloorMaterial(byte newMaterial, String newName, String newModelName) {
/*     */       this.material = newMaterial;
/*     */       this.name = newName;
/*     */       this.modelName = newModelName;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  89 */     private static final FloorMaterial[] types = values();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static FloorMaterial fromByte(byte typeByte) {
/*  95 */       for (int i = 0; i < types.length; i++) {
/*     */         
/*  97 */         if (typeByte == types[i].getCode())
/*  98 */           return types[i]; 
/*     */       } 
/* 100 */       return null;
/*     */     }
/*     */     public byte getCode() {
/*     */       return this.material;
/*     */     }
/*     */     
/*     */     public final String getName() {
/* 107 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getModelName() {
/* 112 */       return this.modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String getTextureName(StructureConstants.FloorType type, FloorMaterial material) {
/* 128 */       return StructureConstants.FloorMappings.getMapping(type, material);
/*     */     }
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
/*     */   public enum FloorType
/*     */   {
/* 145 */     UNKNOWN((byte)100, false, "unknown"),
/* 146 */     FLOOR((byte)10, false, "floor"),
/* 147 */     DOOR((byte)11, false, "hatch"),
/*     */     
/* 149 */     OPENING((byte)12, false, "opening"),
/*     */     
/* 151 */     ROOF((byte)13, false, "roof"),
/* 152 */     SOLID((byte)14, false, "solid"),
/* 153 */     STAIRCASE((byte)15, true, "staircase"),
/* 154 */     WIDE_STAIRCASE((byte)16, true, "staircase, wide"),
/* 155 */     RIGHT_STAIRCASE((byte)17, true, "staircase, right"),
/* 156 */     LEFT_STAIRCASE((byte)18, true, "staircase, left"),
/* 157 */     WIDE_STAIRCASE_RIGHT((byte)19, true, "staircase, wide with right banisters"),
/* 158 */     WIDE_STAIRCASE_LEFT((byte)20, true, "staircase, wide with left banisters"),
/* 159 */     WIDE_STAIRCASE_BOTH((byte)21, true, "staircase, wide with both banisters"),
/* 160 */     CLOCKWISE_STAIRCASE((byte)22, true, "staircase, clockwise spiral"),
/* 161 */     ANTICLOCKWISE_STAIRCASE((byte)23, true, "staircase, counter clockwise spiral"),
/* 162 */     CLOCKWISE_STAIRCASE_WITH((byte)24, true, "staircase, clockwise spiral with banisters"),
/* 163 */     ANTICLOCKWISE_STAIRCASE_WITH((byte)25, true, "staircase, counter clockwise spiral with banisters");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private byte type;
/*     */ 
/*     */ 
/*     */     
/*     */     private String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isStair;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     FloorType(byte newType, boolean newIsStair, String newName) {
/*     */       this.type = newType;
/*     */       this.name = newName;
/*     */       this.isStair = newIsStair;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     private static final FloorType[] types = values();
/*     */     public byte getCode() {
/*     */       return this.type;
/*     */     }
/*     */     
/*     */     public static FloorType fromByte(byte typeByte) {
/* 196 */       for (int i = 0; i < types.length; i++) {
/*     */         
/* 198 */         if (typeByte == types[i].getCode())
/* 199 */           return types[i]; 
/*     */       } 
/* 201 */       return UNKNOWN;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 206 */       return this.name;
/*     */     }
/*     */     public boolean isStair() {
/*     */       return this.isStair;
/*     */     }
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public static final String getModelName(FloorType type, StructureConstants.FloorMaterial material, StructureConstants.FloorState state) {
/*     */       String str;
/* 217 */       if (type == STAIRCASE) {
/*     */         
/* 219 */         if (state == StructureConstants.FloorState.PLANNING)
/* 220 */           return "model.structure.staircase.plan"; 
/* 221 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 222 */           return "model.structure.staircase.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 224 */         return "model.structure.staircase." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 226 */       if (type == CLOCKWISE_STAIRCASE) {
/*     */         
/* 228 */         if (state == StructureConstants.FloorState.PLANNING)
/* 229 */           return "model.structure.staircase.clockwise.none.plan"; 
/* 230 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 231 */           return "model.structure.staircase.clockwise.none.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 233 */         return "model.structure.staircase.clockwise.none." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 235 */       if (type == CLOCKWISE_STAIRCASE_WITH) {
/*     */         
/* 237 */         if (state == StructureConstants.FloorState.PLANNING)
/* 238 */           return "model.structure.staircase.clockwise.with.plan"; 
/* 239 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 240 */           return "model.structure.staircase.clockwise.with.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 242 */         return "model.structure.staircase.clockwise.with." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 244 */       if (type == ANTICLOCKWISE_STAIRCASE) {
/*     */         
/* 246 */         if (state == StructureConstants.FloorState.PLANNING)
/* 247 */           return "model.structure.staircase.anticlockwise.none.plan"; 
/* 248 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 249 */           return "model.structure.staircase.anticlockwise.none.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 251 */         return "model.structure.staircase.anticlockwise.none." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 253 */       if (type == ANTICLOCKWISE_STAIRCASE_WITH) {
/*     */         
/* 255 */         if (state == StructureConstants.FloorState.PLANNING)
/* 256 */           return "model.structure.staircase.anticlockwise.with.plan"; 
/* 257 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 258 */           return "model.structure.staircase.anticlockwise.with.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 260 */         return "model.structure.staircase.anticlockwise.with." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 262 */       if (type == WIDE_STAIRCASE) {
/*     */         
/* 264 */         if (state == StructureConstants.FloorState.PLANNING)
/* 265 */           return "model.structure.staircase.wide.none.plan"; 
/* 266 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 267 */           return "model.structure.staircase.wide.none.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 269 */         return "model.structure.staircase.wide.none." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 271 */       if (type == WIDE_STAIRCASE_LEFT) {
/*     */         
/* 273 */         if (state == StructureConstants.FloorState.PLANNING)
/* 274 */           return "model.structure.staircase.wide.left.plan"; 
/* 275 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 276 */           return "model.structure.staircase.wide.left.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 278 */         return "model.structure.staircase.wide.left." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 280 */       if (type == WIDE_STAIRCASE_RIGHT) {
/*     */         
/* 282 */         if (state == StructureConstants.FloorState.PLANNING)
/* 283 */           return "model.structure.staircase.wide.right.plan"; 
/* 284 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 285 */           return "model.structure.staircase.wide.right.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 287 */         return "model.structure.staircase.wide.right." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 289 */       if (type == WIDE_STAIRCASE_BOTH) {
/*     */         
/* 291 */         if (state == StructureConstants.FloorState.PLANNING)
/* 292 */           return "model.structure.staircase.wide.both.plan"; 
/* 293 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 294 */           return "model.structure.staircase.wide.both.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 296 */         return "model.structure.staircase.wide.both." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 298 */       if (type == RIGHT_STAIRCASE) {
/*     */         
/* 300 */         if (state == StructureConstants.FloorState.PLANNING)
/* 301 */           return "model.structure.staircase.right.plan"; 
/* 302 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 303 */           return "model.structure.staircase.right.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 305 */         return "model.structure.staircase.right." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 307 */       if (type == LEFT_STAIRCASE) {
/*     */         
/* 309 */         if (state == StructureConstants.FloorState.PLANNING)
/* 310 */           return "model.structure.staircase.left.plan"; 
/* 311 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 312 */           return "model.structure.staircase.left.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 314 */         return "model.structure.staircase.left." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/* 316 */       if (type == OPENING) {
/*     */         
/* 318 */         if (state == StructureConstants.FloorState.PLANNING)
/* 319 */           return "model.structure.floor.opening.plan"; 
/* 320 */         if (state == StructureConstants.FloorState.BUILDING) {
/* 321 */           return "model.structure.floor.opening.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */         }
/* 323 */         return "model.structure.floor.opening." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/*     */       
/* 326 */       if (state == StructureConstants.FloorState.PLANNING) {
/* 327 */         str = "model.structure.floor.plan";
/* 328 */       } else if (state == StructureConstants.FloorState.BUILDING) {
/* 329 */         str = "model.structure.floor.plan." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       
/*     */       }
/* 332 */       else if (type == ROOF) {
/*     */         
/* 334 */         str = "model.structure.roof." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       }
/*     */       else {
/*     */         
/* 338 */         str = "model.structure.floor." + material.toString().toLowerCase(Locale.ENGLISH);
/*     */       } 
/*     */ 
/*     */       
/* 342 */       if (type == UNKNOWN)
/*     */       {
/* 344 */         str = "model.structure.floor.plan";
/*     */       }
/* 346 */       return str;
/*     */     }
/*     */ 
/*     */     
/*     */     public static final int getIconId(FloorType type, StructureConstants.FloorMaterial material, StructureConstants.FloorState state) {
/* 351 */       if (state == StructureConstants.FloorState.PLANNING || state == StructureConstants.FloorState.BUILDING)
/*     */       {
/* 353 */         return 60;
/*     */       }
/* 355 */       if (type == ROOF)
/*     */       {
/* 357 */         return getRoofIconId(material);
/*     */       }
/*     */       
/* 360 */       return getFloorIconId(material);
/*     */     }
/*     */ 
/*     */     
/*     */     private static int getFloorIconId(StructureConstants.FloorMaterial material) {
/* 365 */       int returnId = 60;
/* 366 */       switch (material)
/*     */       
/*     */       { case WOOD:
/* 369 */           returnId = 60;
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
/* 415 */           return returnId;case STONE_BRICK: returnId = 60; return returnId;case CLAY_BRICK: returnId = 60; return returnId;case SLATE_SLAB: returnId = 60; return returnId;case STONE_SLAB: returnId = 60; return returnId;case THATCH: returnId = 60; return returnId;case METAL_IRON: returnId = 60; return returnId;case METAL_STEEL: returnId = 60; return returnId;case METAL_COPPER: returnId = 60; return returnId;case METAL_GOLD: returnId = 60; return returnId;case METAL_SILVER: returnId = 60; return returnId;case SANDSTONE_SLAB: returnId = 60; return returnId;case MARBLE_SLAB: returnId = 60; return returnId;case STANDALONE: returnId = 60; return returnId; }  returnId = 60; return returnId;
/*     */     }
/*     */ 
/*     */     
/*     */     private static int getRoofIconId(StructureConstants.FloorMaterial material) {
/* 420 */       int returnId = 60;
/* 421 */       switch (material)
/*     */       
/*     */       { case WOOD:
/* 424 */           returnId = 60;
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
/* 467 */           return returnId;case STONE_BRICK: returnId = 60; return returnId;case CLAY_BRICK: returnId = 60; return returnId;case SLATE_SLAB: returnId = 60; return returnId;case STONE_SLAB: returnId = 60; return returnId;case THATCH: returnId = 60; return returnId;case METAL_IRON: returnId = 60; return returnId;case METAL_STEEL: returnId = 60; return returnId;case METAL_COPPER: returnId = 60; return returnId;case METAL_GOLD: returnId = 60; return returnId;case METAL_SILVER: returnId = 60; return returnId;case MARBLE_SLAB: returnId = 60; return returnId;case SANDSTONE_SLAB: returnId = 60; return returnId; }  returnId = 60; return returnId;
/*     */     }
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
/*     */   public enum FloorState
/*     */   {
/* 481 */     PLANNING((byte)-1),
/* 482 */     BUILDING((byte)0),
/* 483 */     COMPLETED(127);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private byte state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 500 */     private static final FloorState[] types = values();
/*     */     FloorState(byte newState) {
/*     */       this.state = newState;
/*     */     }
/*     */     
/*     */     public static FloorState fromByte(byte floorStateByte) {
/* 506 */       for (int i = 0; i < types.length; i++) {
/*     */         
/* 508 */         if (floorStateByte == types[i].getCode()) {
/* 509 */           return types[i];
/*     */         }
/*     */       } 
/* 512 */       return BUILDING;
/*     */     }
/*     */     
/*     */     public byte getCode() {
/*     */       return this.state;
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     } }
/*     */   
/*     */   public static class Pair<K, V> { public Pair(K key, V value) {
/* 524 */       this.key = key;
/* 525 */       this.value = value;
/*     */     }
/*     */     private final K key;
/*     */     private final V value;
/*     */     
/*     */     public final K getKey() {
/* 531 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public final V getValue() {
/* 536 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 542 */       return this.key.hashCode() ^ this.value.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 548 */       if (o == null)
/* 549 */         return false; 
/* 550 */       if (!(o instanceof Pair))
/* 551 */         return false; 
/* 552 */       Pair mapping = (Pair)o;
/* 553 */       return (this.key.equals(mapping.getKey()) && this.value.equals(mapping.getValue()));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class FloorMappings
/*     */   {
/* 559 */     public static final Map<StructureConstants.Pair<StructureConstants.FloorType, StructureConstants.FloorMaterial>, String> mappings = new HashMap<>();
/*     */ 
/*     */     
/*     */     static {
/* 563 */       for (StructureConstants.FloorType t : StructureConstants.FloorType.values()) {
/*     */         
/* 565 */         for (StructureConstants.FloorMaterial m : StructureConstants.FloorMaterial.values()) {
/*     */           
/* 567 */           String mapping = "img.texture.floor." + t.toString().toLowerCase() + "." + m.toString().toLowerCase();
/* 568 */           StructureConstants.Pair<StructureConstants.FloorType, StructureConstants.FloorMaterial> p = new StructureConstants.Pair<>(t, m);
/* 569 */           mappings.put(p, mapping);
/*     */         } 
/*     */       } 
/*     */     }
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
/*     */     public static final String getMapping(StructureConstants.FloorType t, StructureConstants.FloorMaterial m) {
/* 584 */       StructureConstants.Pair<StructureConstants.FloorType, StructureConstants.FloorMaterial> p = new StructureConstants.Pair<>(t, m);
/* 585 */       return mappings.get(p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\StructureConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */