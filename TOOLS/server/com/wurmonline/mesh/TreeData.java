/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ import com.wurmonline.shared.util.StringUtilities;
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
/*     */ public final class TreeData
/*     */ {
/*     */   public enum TreeType
/*     */   {
/*  33 */     BIRCH(0, (byte)14, false, 2, 100, 114, 128, 0.7887417F, 0.6493875F, 0.03F, "model.tree.birch", 0, 0, false),
/*     */ 
/*     */     
/*  36 */     PINE(1, (byte)37, false, 2, 101, 115, 129, 0.7200847F, 0.4F, 0.04F, "model.tree.pine", 1, 0, true),
/*     */ 
/*     */     
/*  39 */     OAK(2, (byte)38, false, 20, 102, 116, 130, 0.63670415F, 0.7F, 0.135F, "model.tree.oak", 2, 0, true),
/*     */ 
/*     */     
/*  42 */     CEDAR(3, (byte)39, false, 5, 103, 117, 131, 0.614782F, 0.37F, 0.05F, "model.tree.cedar", 3, 0, false),
/*     */ 
/*     */     
/*  45 */     WILLOW(4, (byte)40, false, 18, 104, 118, 132, 0.8156737F, 0.9655433F, 0.05F, "model.tree.willow", 0, 1, false),
/*     */ 
/*     */     
/*  48 */     MAPLE(5, (byte)41, false, 4, 105, 119, 133, 0.6439394F, 0.52989763F, 0.08F, "model.tree.maple", 1, 1, true),
/*     */ 
/*     */     
/*  51 */     APPLE(6, (byte)42, true, 2, 106, 120, 134, 1.4137214F, 1.1328298F, 0.03F, "model.tree.apple", 2, 1, true),
/*     */ 
/*     */     
/*  54 */     LEMON(7, (byte)43, true, 2, 107, 121, 135, 1.4890511F, 1.4594362F, 0.02F, "model.tree.lemon", 3, 1, true),
/*     */ 
/*     */     
/*  57 */     OLIVE(8, (byte)44, true, 2, 108, 122, 136, 0.84542066F, 1.0500308F, 0.07F, "model.tree.olive", 0, 2, true),
/*     */ 
/*     */     
/*  60 */     CHERRY(9, (byte)45, true, 2, 109, 123, 137, 1.1129296F, 1.1271963F, 0.025F, "model.tree.cherry", 1, 2, true),
/*     */ 
/*     */     
/*  63 */     CHESTNUT(10, (byte)63, false, 12, 110, 124, 138, 0.792233F, 0.68F, 0.07F, "model.tree.chestnut", 2, 2, true),
/*     */ 
/*     */     
/*  66 */     WALNUT(11, (byte)64, false, 15, 111, 125, 139, 0.7F, 0.65028346F, 0.07F, "model.tree.walnut", 3, 2, true),
/*     */ 
/*     */     
/*  69 */     FIR(12, (byte)65, false, 5, 112, 126, 140, 0.77708626F, 0.77F, 0.05F, "model.tree.fir", 0, 3, false),
/*     */ 
/*     */     
/*  72 */     LINDEN(13, (byte)66, false, 12, 113, 127, 141, 0.7157274F, 0.69F, 0.05F, "model.tree.linden", 1, 3, false),
/*     */ 
/*     */     
/*  75 */     ORANGE(14, (byte)88, true, 2, 163, 164, 165, 1.4890511F, 1.4594362F, 0.02F, "model.tree.orange", 2, 3, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte materialId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean fruitTree;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int woodDifficulty;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte normalTree;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte myceliumTree;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte enchantedTree;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final float width;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final float height;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final float radius;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int posX;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int posY;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean canBearFruit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TreeType(int type, byte material, boolean isFruitTree, int woodDifficulty, int normalTree, int myceliumTree, int enchantedTree, float width, float height, float radius, String modelName, int posX, int posY, boolean canBearFruit) {
/*     */       this.typeId = type;
/*     */       this.materialId = material;
/*     */       this.fruitTree = isFruitTree;
/*     */       this.woodDifficulty = woodDifficulty;
/*     */       this.normalTree = (byte)normalTree;
/*     */       this.myceliumTree = (byte)myceliumTree;
/*     */       this.enchantedTree = (byte)enchantedTree;
/*     */       this.width = width;
/*     */       this.height = height;
/*     */       this.radius = radius;
/*     */       this.modelName = modelName;
/*     */       this.posX = posX;
/*     */       this.posY = posY;
/*     */       this.canBearFruit = canBearFruit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/*     */       String name = fromInt(this.typeId).toString() + " tree";
/*     */       return StringUtilities.raiseFirstLetter(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getMaterial() {
/*     */       return this.materialId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFruitTree() {
/*     */       return this.fruitTree;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     private static final TreeType[] types = values();
/*     */     public byte asNormalTree() { return this.normalTree; }
/*     */     public byte asMyceliumTree() { return this.myceliumTree; }
/*     */     public byte asEnchantedTree() { return this.enchantedTree; }
/*     */     public int getDifficulty() { return this.woodDifficulty; }
/* 209 */     public static final int getLength() { return types.length; }
/*     */     public float getWidth() {
/*     */       return this.width;
/*     */     } public float getHeight() {
/*     */       return this.height;
/* 214 */     } public static TreeType fromTileData(int tileData) { return fromInt(tileData & 0xF); }
/*     */     public float getRadius() { return this.radius; }
/*     */     String getModelName() { return this.modelName; }
/*     */     public String getModelResourceName(int treeAge) { if (treeAge < 4) return getModelName() + ".young";  if (treeAge == 15)
/*     */         return getModelName() + ".shrivelled";  return getModelName(); }
/* 219 */     public int getTexturPosX() { return this.posX; } public static TreeType fromInt(int typeId) { if (typeId >= getLength())
/* 220 */         return types[0]; 
/* 221 */       return types[typeId & 0xFF]; }
/*     */     
/*     */     public int getTexturPosY() {
/*     */       return this.posY;
/*     */     }
/*     */     public boolean canBearFruit() {
/*     */       return this.canBearFruit;
/*     */     }
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public static int encodeTileData(int tage, int ttype) {
/* 234 */       ttype = Math.min(ttype, types.length - 1);
/* 235 */       ttype = Math.max(ttype, 0);
/* 236 */       return (tage & 0xF) << 4 | ttype & 0xF;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHelpSubject(byte type, boolean infected) {
/* 247 */     return "Terrain:" + getTypeName(type).replace(' ', '_');
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidTree(int treeId) {
/* 252 */     return (treeId < TreeType.getLength());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getType(byte data) {
/* 263 */     return data & 0xF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasFruit(int treeData) {
/* 268 */     return ((treeData >> 3 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCentre(int treeData) {
/* 273 */     return ((treeData >> 2 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GrassData.GrowthTreeStage getGrassLength(int treeData) {
/* 278 */     return GrassData.GrowthTreeStage.fromInt(treeData & 0x3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTypeName(byte data) {
/* 289 */     return TreeType.fromTileData(data).getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\TreeData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */