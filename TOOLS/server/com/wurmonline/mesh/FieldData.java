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
/*     */ public final class FieldData
/*     */ {
/*     */   public static final int BARLEY = 0;
/*     */   public static final int WHEAT = 1;
/*     */   public static final int RYE = 2;
/*     */   public static final int OAT = 3;
/*     */   public static final int CORN = 4;
/*     */   public static final int PUMPKIN = 5;
/*     */   public static final int POTATO = 6;
/*     */   public static final int COTTON = 7;
/*     */   public static final int WEMP = 8;
/*     */   public static final int GARLIC = 9;
/*     */   public static final int ONION = 10;
/*     */   public static final int REED = 11;
/*     */   public static final int RICE = 12;
/*     */   public static final int STRAWBERRIES = 13;
/*     */   public static final int CARROTS = 14;
/*     */   public static final int CABBAGE = 15;
/*     */   public static final int TOMATOS = 16;
/*     */   public static final int SUGAR_BEET = 17;
/*     */   public static final int LETTUCE = 18;
/*     */   public static final int PEAPODS = 19;
/*     */   public static final int CUCUMBER = 20;
/*  46 */   private static final String[] fieldAges = new String[] { "freshly sown", "sprouting", "growing", "halfway", "almost ripe", "ripe", "ripe", "only weeds" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFieldName(int fieldType) {
/*  64 */     switch (fieldType) {
/*     */       
/*     */       case 0:
/*  67 */         return "Barley";
/*     */       case 1:
/*  69 */         return "Wheat";
/*     */       case 2:
/*  71 */         return "Rye";
/*     */       case 3:
/*  73 */         return "Oat";
/*     */       case 4:
/*  75 */         return "Corn";
/*     */       case 5:
/*  77 */         return "Pumpkin";
/*     */       case 6:
/*  79 */         return "Potato";
/*     */       case 7:
/*  81 */         return "Cotton";
/*     */       case 8:
/*  83 */         return "Wemp";
/*     */       case 9:
/*  85 */         return "Garlic";
/*     */       case 10:
/*  87 */         return "Onion";
/*     */       case 11:
/*  89 */         return "Reed";
/*     */       case 12:
/*  91 */         return "Rice";
/*     */       case 13:
/*  93 */         return "Strawberries";
/*     */       case 14:
/*  95 */         return "Carrots";
/*     */       case 15:
/*  97 */         return "Cabbage";
/*     */       case 16:
/*  99 */         return "Tomatoes";
/*     */       case 17:
/* 101 */         return "Sugar beet";
/*     */       case 18:
/* 103 */         return "Lettuce";
/*     */       case 19:
/* 105 */         return "Pea pods";
/*     */       case 20:
/* 107 */         return "Cucumber";
/*     */     } 
/* 109 */     return "Unknown crop";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getModelResourceName(int fieldType) {
/* 116 */     return "img.texture.crop." + getFieldName(fieldType).toLowerCase().replace(" ", "");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getHelpSubject(int fieldType) {
/* 121 */     return "Terrain:" + getFieldName(fieldType).replace(' ', '_');
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getType(Tiles.Tile fieldType, byte data) {
/* 126 */     if (fieldType == Tiles.Tile.TILE_FIELD)
/* 127 */       return data & 0xF; 
/* 128 */     return 16 + (data & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAge(byte data) {
/* 133 */     return (data & 0x70) >> 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTended(byte data) {
/* 138 */     return ((data & 0x80) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getTypeName(Tiles.Tile fieldType, byte data) {
/* 143 */     return getFieldName(getType(fieldType, data));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAgeName(byte data) {
/* 148 */     return fieldAges[getAge(data)];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\FieldData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */