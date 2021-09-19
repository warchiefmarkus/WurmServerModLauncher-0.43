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
/*     */ 
/*     */ 
/*     */ public final class BushData
/*     */ {
/*     */   public enum BushType
/*     */   {
/*  35 */     LAVENDER(0, (byte)46, 4, 142, 148, 154, 1.0F, 1.0F, 0.0F, "model.bush.lavendel", 0, 0, true),
/*     */ 
/*     */     
/*  38 */     ROSE(1, (byte)47, 5, 143, 149, 155, 2.0F, 1.0F, 0.0F, "model.bush.rose", 1, 0, true),
/*     */ 
/*     */     
/*  41 */     THORN(2, (byte)48, 15, 144, 150, 156, 0.5F, 0.5F, 0.0F, "model.bush.thorn", 2, 0, false),
/*     */ 
/*     */     
/*  44 */     GRAPE(3, (byte)49, 5, 145, 151, 157, 1.4F, 1.2F, 0.0F, "model.bush.grape", 3, 0, true),
/*     */ 
/*     */     
/*  47 */     CAMELLIA(4, (byte)50, 3, 146, 152, 158, 1.6F, 1.25F, 0.0F, "model.bush.camellia", 0, 1, true),
/*     */ 
/*     */     
/*  50 */     OLEANDER(5, (byte)51, 2, 147, 153, 159, 1.55F, 1.45F, 0.0F, "model.bush.oleander", 1, 1, true),
/*     */ 
/*     */     
/*  53 */     HAZELNUT(6, (byte)71, 2, 160, 161, 162, 1.7F, 1.32F, 0.0F, "model.bush.hazelnut", 2, 1, true),
/*     */ 
/*     */     
/*  56 */     RASPBERRY(7, (byte)90, 2, 166, 167, 168, 1.7F, 1.32F, 0.0F, "model.bush.raspberry", 3, 1, true),
/*     */ 
/*     */     
/*  59 */     BLUEBERRY(8, (byte)91, 2, 169, 170, 171, 1.7F, 1.32F, 0.0F, "model.bush.blueberry", 0, 2, true),
/*     */ 
/*     */     
/*  62 */     LINGONBERRY(9, (byte)92, 2, 172, 172, 172, 1.7F, 1.32F, 0.0F, "model.bush.lingonberry", 1, 2, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte materialId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int woodDifficulty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte normalBush;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte myceliumBush;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte enchantedBush;
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
/*     */     BushType(int id, byte material, int woodDifficulty, int normalBush, int myceliumBush, int enchantedBush, float width, float height, float radius, String modelName, int posX, int posY, boolean canBearFruit) {
/*     */       this.typeId = id;
/*     */       this.materialId = material;
/*     */       this.woodDifficulty = woodDifficulty;
/*     */       this.normalBush = (byte)normalBush;
/*     */       this.myceliumBush = (byte)myceliumBush;
/*     */       this.enchantedBush = (byte)enchantedBush;
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
/*     */       String name = fromInt(this.typeId).toString() + " bush";
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
/* 184 */     private static final BushType[] types = values();
/*     */     public byte asNormalBush() { return this.normalBush; }
/*     */     public byte asMyceliumBush() { return this.myceliumBush; }
/*     */     public byte asEnchantedBush() { return this.enchantedBush; }
/*     */     public int getDifficulty() { return this.woodDifficulty; }
/* 189 */     public static final int getLength() { return types.length; }
/*     */     public float getWidth() {
/*     */       return this.width;
/*     */     } public float getHeight() {
/*     */       return this.height;
/* 194 */     } public static BushType fromTileData(int tileData) { return fromInt(tileData & 0xF); }
/*     */     public float getRadius() { return this.radius; }
/*     */     String getModelName() { return this.modelName; }
/*     */     public String getModelResourceName(int treeAge) { if (treeAge < 4) return getModelName() + ".young";  if (treeAge == 15)
/*     */         return getModelName() + ".shrivelled";  return getModelName(); }
/* 199 */     public int getTexturPosX() { return this.posX; } public static BushType fromInt(int i) { if (i >= getLength())
/* 200 */         return types[0]; 
/* 201 */       return types[i & 0xFF]; }
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
/*     */     public static BushType decodeTileData(int tileData) {
/* 213 */       return fromInt(tileData & 0xF);
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
/*     */     public static int encodeTileData(int tage, int ttype) {
/* 226 */       ttype = Math.min(ttype, types.length - 1);
/* 227 */       ttype = Math.max(ttype, 0);
/* 228 */       return (tage & 0xF) << 4 | ttype & 0xF;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBush(int treeId) {
/* 239 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTree(int treeId) {
/* 244 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getHelpSubject(byte type, boolean infected) {
/* 249 */     return "Terrain:" + getTypeName(type).replace(' ', '_');
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidBush(int treeId) {
/* 254 */     return (treeId < BushType.getLength());
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
/* 265 */     return data & 0xF;
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
/* 276 */     return BushType.fromTileData(getType(data)).getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\BushData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */