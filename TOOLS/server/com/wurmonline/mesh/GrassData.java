/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GrassData
/*     */ {
/*     */   public enum GrowthSeason
/*     */   {
/*  46 */     WINTER, SPRING, SUMMER, AUTUMN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum GrowthStage
/*     */   {
/*  56 */     SHORT((byte)0), MEDIUM((byte)1), TALL((byte)2), WILD((byte)3);
/*     */     private static final GrowthStage[] stages;
/*  58 */     private static final int NUMBER_OF_STAGES = (values()).length;
/*     */     
/*     */     private byte code;
/*     */     
/*     */     static {
/*  63 */       stages = values();
/*     */     }
/*     */ 
/*     */     
/*     */     GrowthStage(byte code) {
/*  68 */       this.code = code;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getCode() {
/*  73 */       return this.code;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getEncodedData() {
/*  84 */       return (byte)(this.code << 6 & 0xC0);
/*     */     }
/*     */ 
/*     */     
/*     */     public static GrowthStage fromInt(int i) {
/*  89 */       return stages[i];
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
/*     */     public static GrowthStage decodeTileData(int tileData) {
/* 101 */       return fromInt(tileData >> 6 & 0x3);
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
/*     */     public static GrowthStage decodeTreeData(int tileData) {
/* 114 */       int len = Math.max((tileData & 0x3) - 1, 0);
/* 115 */       return fromInt(len);
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
/*     */ 
/*     */     
/*     */     public static short getYield(GrowthStage growthStage) {
/* 130 */       switch (growthStage)
/*     */       
/*     */       { case NONE:
/* 133 */           yield = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 147 */           return yield;case FLOWER_1: yield = 1; return yield;case FLOWER_2: yield = 2; return yield;case FLOWER_3: yield = 3; return yield; }  short yield = 0; return yield;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GrowthStage getNextStage() {
/* 157 */       int num = ordinal();
/* 158 */       num = Math.min(num + 1, NUMBER_OF_STAGES - 1);
/* 159 */       return fromInt(num);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isMax() {
/* 164 */       return (ordinal() >= NUMBER_OF_STAGES - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GrowthStage getPreviousStage() {
/* 174 */       int num = ordinal();
/* 175 */       num = Math.max(num - 1, 0);
/* 176 */       return fromInt(num);
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
/*     */   public enum GrowthTreeStage
/*     */   {
/* 191 */     LAWN((byte)0), SHORT((byte)1), MEDIUM((byte)2), TALL((byte)3);
/*     */     private static final GrowthTreeStage[] stages;
/* 193 */     private static final int NUMBER_OF_STAGES = (values()).length;
/*     */     
/*     */     private byte code;
/*     */     
/*     */     static {
/* 198 */       stages = values();
/*     */     }
/*     */ 
/*     */     
/*     */     GrowthTreeStage(byte code) {
/* 203 */       this.code = code;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getCode() {
/* 208 */       return this.code;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getEncodedData() {
/* 219 */       return (byte)(this.code & 0x3);
/*     */     }
/*     */ 
/*     */     
/*     */     public static GrowthTreeStage fromInt(int i) {
/* 224 */       return stages[i];
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
/*     */     public static GrowthTreeStage decodeTileData(int tileData) {
/* 236 */       return fromInt(tileData & 0x3);
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
/*     */ 
/*     */     
/*     */     public static short getYield(GrowthTreeStage growthStage) {
/* 251 */       switch (growthStage)
/*     */       
/*     */       { case NONE:
/* 254 */           yield = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 265 */           return yield;case FLOWER_1: yield = 1; return yield;case FLOWER_2: yield = 2; return yield; }  short yield = 0; return yield;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GrowthTreeStage getNextStage() {
/* 275 */       int num = ordinal();
/* 276 */       num = Math.min(num + 1, NUMBER_OF_STAGES - 1);
/* 277 */       return fromInt(num);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isMax() {
/* 282 */       return (ordinal() >= NUMBER_OF_STAGES - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GrowthTreeStage getPreviousStage() {
/* 292 */       int num = ordinal();
/* 293 */       num = Math.max(num - 1, 1);
/* 294 */       return fromInt(num);
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
/*     */   public enum GrassType
/*     */   {
/* 310 */     GRASS((byte)0), REED((byte)1), KELP((byte)2), UNUSED((byte)3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     private static final GrassType[] types = values(); private byte type;
/*     */     static {
/*     */     
/*     */     }
/*     */     GrassType(byte type) {
/* 323 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getType() {
/* 333 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getEncodedData() {
/* 344 */       return (byte)(this.type << 4 & 0x30);
/*     */     }
/*     */ 
/*     */     
/*     */     public static GrassType fromInt(int i) {
/* 349 */       return types[i];
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
/*     */     public static GrassType decodeTileData(int tile) {
/* 361 */       return fromInt(tile >> 4 & 0x3);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 371 */       switch (this) {
/*     */         
/*     */         case NONE:
/* 374 */           return "Grass";
/*     */         case FLOWER_1:
/* 376 */           return "Kelp";
/*     */         case FLOWER_2:
/* 378 */           return "Reed";
/*     */       } 
/* 380 */       return "Unknown";
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
/*     */     public int getGrowthRateInSeason(GrassData.GrowthSeason season) {
/* 393 */       switch (season) {
/*     */         
/*     */         case NONE:
/* 396 */           return 15;
/*     */         case FLOWER_1:
/* 398 */           return 40;
/*     */         case FLOWER_2:
/* 400 */           return 30;
/*     */         case FLOWER_3:
/* 402 */           return 20;
/*     */       } 
/* 404 */       return 5;
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
/*     */   public enum FlowerType
/*     */   {
/* 417 */     NONE((byte)0), FLOWER_1((byte)1), FLOWER_2((byte)2), FLOWER_3((byte)3), FLOWER_4((byte)4), FLOWER_5((byte)5), FLOWER_6((byte)6),
/* 418 */     FLOWER_7((byte)7), FLOWER_8((byte)8),
/* 419 */     FLOWER_9((byte)9), FLOWER_10((byte)10), FLOWER_11((byte)11), FLOWER_12((byte)12), FLOWER_13((byte)13), FLOWER_14((byte)14),
/* 420 */     FLOWER_15((byte)15);
/*     */ 
/*     */ 
/*     */     
/*     */     private byte type;
/*     */ 
/*     */     
/* 427 */     private static final FlowerType[] types = values();
/*     */     static {
/*     */     
/*     */     }
/*     */     FlowerType(byte type) {
/* 432 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getType() {
/* 437 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getEncodedData() {
/* 448 */       return (byte)(this.type & 0xFF);
/*     */     }
/*     */ 
/*     */     
/*     */     public static FlowerType fromInt(int i) {
/* 453 */       return types[i];
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
/*     */     public static FlowerType decodeTileData(int tileData) {
/* 465 */       return fromInt(tileData & 0xF);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 470 */       return GrassData.getFlowerName(this);
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
/*     */   private static String getFlowerName(FlowerType flowerType) {
/* 484 */     switch (flowerType) {
/*     */       
/*     */       case NONE:
/* 487 */         return "";
/*     */       case FLOWER_1:
/* 489 */         return "Yellow flowers";
/*     */       case FLOWER_2:
/* 491 */         return "Orange-red flowers";
/*     */       case FLOWER_3:
/* 493 */         return "Purple flowers";
/*     */       case FLOWER_4:
/* 495 */         return "White flowers";
/*     */       case FLOWER_5:
/* 497 */         return "Blue flowers";
/*     */       case FLOWER_6:
/* 499 */         return "Greenish-yellow flowers";
/*     */       case FLOWER_7:
/* 501 */         return "White-dotted flowers";
/*     */     } 
/* 503 */     return "Unknown grass";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getModelResourceName(FlowerType flowerType) {
/* 510 */     switch (flowerType) {
/*     */     
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 521 */     return "model.flower.unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHelpSubject(int type) {
/* 528 */     return "Terrain:" + GrassType.values()[type].name().replace(' ', '_');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFlowerType(byte data) {
/* 534 */     return FlowerType.decodeTileData(data).getType() & 0xFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFlowerTypeName(byte data) {
/* 540 */     return getFlowerName(FlowerType.decodeTileData(data));
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
/*     */ 
/*     */   
/*     */   public static byte encodeGrassTileData(GrowthStage growthStage, GrassType grassType, FlowerType flowerType) {
/* 557 */     return (byte)(growthStage.getEncodedData() | grassType.getEncodedData() | flowerType.getEncodedData());
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
/*     */   public static byte encodeGrassTileData(GrowthStage growthStage, FlowerType flowerType) {
/* 572 */     return (byte)(growthStage.getEncodedData() | flowerType.getEncodedData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHover(byte data) {
/* 583 */     return GrassType.decodeTileData(data).getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getGrowthRateFor(GrassType grassType, GrowthSeason season) {
/* 588 */     return grassType.getGrowthRateInSeason(season);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\GrassData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */