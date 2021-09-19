/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.MiscConstants;
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
/*     */ public final class Crops
/*     */   implements MiscConstants
/*     */ {
/*     */   private final int number;
/*     */   private final String cropName;
/*     */   private final int templateId;
/*     */   private final String measure;
/*     */   private final int productId;
/*     */   private final double difficulty;
/*     */   static final int BARLEY = 0;
/*     */   static final int WHEAT = 1;
/*     */   static final int RYE = 2;
/*     */   static final int OAT = 3;
/*     */   static final int CORN = 4;
/*     */   static final int PUMPKIN = 5;
/*     */   static final int POTATO = 6;
/*     */   static final int COTTON = 7;
/*     */   static final int WEMP = 8;
/*     */   static final int GARLIC = 9;
/*     */   static final int ONION = 10;
/*     */   static final int REED = 11;
/*     */   static final int RICE = 12;
/*     */   static final int STRAWBERRIES = 13;
/*     */   static final int CARROTS = 14;
/*     */   static final int CABBAGE = 15;
/*     */   static final int TOMATOS = 16;
/*     */   static final int SUGAR_BEET = 17;
/*     */   static final int LETTUCE = 18;
/*     */   static final int PEAS = 19;
/*     */   static final int CUCUMBER = 20;
/*  99 */   private static final Crops[] cropTypes = new Crops[] { new Crops(0, "barley", 28, 28, "handfuls", 20.0D), new Crops(1, "wheat", 29, 29, "handfuls", 30.0D), new Crops(2, "rye", 30, 30, "handfuls", 10.0D), new Crops(3, "oat", 31, 31, "handfuls", 15.0D), new Crops(4, "corn", 32, 32, "stalks", 40.0D), new Crops(5, "pumpkin", 34, 33, "", 15.0D), new Crops(6, "potato", 35, 35, "", 4.0D), new Crops(7, "cotton", 145, 144, "bales", 7.0D), new Crops(8, "wemp", 317, 316, "bales", 10.0D), new Crops(9, "garlic", 356, 356, "bunch", 70.0D), new Crops(10, "onion", 355, 355, "bunch", 60.0D), new Crops(11, "reed", 744, 743, "bales", 20.0D), new Crops(12, "rice", 746, 746, "handfuls", 80.0D), new Crops(13, "strawberries", 750, 362, "handfuls", 60.0D), new Crops(14, "carrots", 1145, 1133, "handfuls", 25.0D), new Crops(15, "cabbage", 1146, 1134, "", 35.0D), new Crops(16, "tomatoes", 1147, 1135, "handfuls", 45.0D), new Crops(17, "sugar beet", 1148, 1136, "", 85.0D), new Crops(18, "lettuce", 1149, 1137, "", 55.0D), new Crops(19, "peas", 1150, 1138, "handfuls", 65.0D), new Crops(20, "cucumber", 1248, 1247, "", 15.0D) };
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
/*     */   private Crops(int aNumber, String aCropName, int aTemplateId, int aProductId, String aMeasure, double aDifficulty) {
/* 129 */     this.number = aNumber;
/* 130 */     this.cropName = aCropName;
/* 131 */     this.templateId = aTemplateId;
/* 132 */     this.productId = aProductId;
/* 133 */     this.measure = aMeasure;
/* 134 */     this.difficulty = aDifficulty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getCropName() {
/* 143 */     return this.cropName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNumber() {
/* 152 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getTemplateId() {
/* 161 */     return this.templateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getMeasure() {
/* 170 */     return this.measure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getProductId() {
/* 179 */     return this.productId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double getDifficulty() {
/* 188 */     return this.difficulty;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getCropName(int cropNumber) {
/* 193 */     String cropString = "Unknown crop";
/* 194 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 196 */       if (cropTypes[x].getNumber() == cropNumber) {
/*     */         
/* 198 */         cropString = cropTypes[x].getCropName();
/* 199 */         return cropString;
/*     */       } 
/*     */     } 
/* 202 */     return cropString;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte getTileType(int cropNumber) {
/* 207 */     if (cropNumber < 16)
/* 208 */       return Tiles.Tile.TILE_FIELD.id; 
/* 209 */     return Tiles.Tile.TILE_FIELD2.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final int getItemTemplate(int cropNumber) {
/* 215 */     int itemTemplate = -10;
/* 216 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 218 */       if (cropTypes[x].getNumber() == cropNumber) {
/*     */         
/* 220 */         itemTemplate = cropTypes[x].getTemplateId();
/* 221 */         return itemTemplate;
/*     */       } 
/*     */     } 
/* 224 */     return itemTemplate;
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getProductTemplate(int cropNumber) {
/* 229 */     int productTemplate = -10;
/* 230 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 232 */       if (cropTypes[x].getNumber() == cropNumber) {
/*     */         
/* 234 */         productTemplate = cropTypes[x].getProductId();
/* 235 */         return productTemplate;
/*     */       } 
/*     */     } 
/* 238 */     return productTemplate;
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getNumber(int templateId) {
/* 243 */     int cropNumber = -10;
/* 244 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 246 */       if (cropTypes[x].getTemplateId() == templateId) {
/*     */         
/* 248 */         cropNumber = cropTypes[x].getNumber();
/* 249 */         return cropNumber;
/*     */       } 
/*     */     } 
/* 252 */     return cropNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getCropNumber(byte tileType, byte tileData) {
/* 257 */     if (tileType == 7) {
/* 258 */       return tileData & 0xF;
/*     */     }
/* 260 */     return 16 + (tileData & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte encodeFieldData(boolean tended, int fieldAge, int cropNumber) {
/* 265 */     return (byte)((tended ? 128 : 0) + (fieldAge << 4) + (cropNumber & 0xF));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final byte decodeFieldAge(byte data) {
/* 270 */     return (byte)(data >> 4 & 0x7);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean decodeFieldState(byte data) {
/* 275 */     return ((data & 0x80) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   static final String getMeasure(int cropNumber) {
/* 280 */     String cropString = "";
/* 281 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 283 */       if (cropTypes[x].getNumber() == cropNumber) {
/*     */         
/* 285 */         cropString = cropTypes[x].getMeasure();
/* 286 */         return cropString;
/*     */       } 
/*     */     } 
/* 289 */     return cropString;
/*     */   }
/*     */ 
/*     */   
/*     */   static final double getDifficultyFor(int cropNumber) {
/* 294 */     double diff = 10.0D;
/* 295 */     for (int x = 0; x < cropTypes.length; x++) {
/*     */       
/* 297 */       if (cropTypes[x].getNumber() == cropNumber) {
/*     */         
/* 299 */         diff = cropTypes[x].getDifficulty();
/* 300 */         return diff;
/*     */       } 
/*     */     } 
/* 303 */     return diff;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Crops.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */