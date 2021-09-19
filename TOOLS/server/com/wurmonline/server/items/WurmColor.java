/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WurmColor
/*     */ {
/*     */   public static final int createColor(int r, int g, int b) {
/*  33 */     return ((b & 0xFF) << 16) + ((g & 0xFF) << 8) + (r & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getColorRed(int color) {
/*  38 */     return color & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getColorGreen(int color) {
/*  43 */     return color >> 8 & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getColorBlue(int color) {
/*  48 */     return color >> 16 & 0xFF;
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
/*     */   
/*  65 */   private static final Random mixRand = new Random();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int mixColors(int color1, int weight1, int color2, int weight2, float avgQl) {
/*  70 */     float modifier = 0.0F;
/*     */     
/*  72 */     if (avgQl < 100.0F && mixRand.nextInt(3) == 0) {
/*  73 */       modifier = 0.01F * (100.0F - avgQl) / 100.0F;
/*     */     }
/*  75 */     int r = (getColorRed(color1) * weight1 + getColorRed(color2) * weight2) / (weight1 + weight2);
/*  76 */     if (r > 128) {
/*  77 */       r = (int)(128.0F + (r - 128) * (1.0F - modifier));
/*     */     } else {
/*  79 */       r = (int)(r + (128 - r) * modifier);
/*  80 */     }  int g = (getColorGreen(color1) * weight1 + getColorGreen(color2) * weight2) / (weight1 + weight2);
/*  81 */     if (g > 128) {
/*  82 */       g = (int)(128.0F + (g - 128) * (1.0F - modifier));
/*     */     } else {
/*  84 */       g = (int)(g + (128 - g) * modifier);
/*  85 */     }  int b = (getColorBlue(color1) * weight1 + getColorBlue(color2) * weight2) / (weight1 + weight2);
/*  86 */     if (b > 128) {
/*  87 */       b = (int)(128.0F + (b - 128) * (1.0F - modifier));
/*     */     } else {
/*  89 */       b = (int)(b + (128 - b) * modifier);
/*  90 */     }  return createColor(r, g, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getInitialColor(int itemTemplateId, float qualityLevel) {
/*  95 */     if (itemTemplateId == 431)
/*  96 */       return getBaseBlack(qualityLevel); 
/*  97 */     if (itemTemplateId == 432)
/*  98 */       return getBaseWhite(qualityLevel); 
/*  99 */     if (itemTemplateId == 433)
/* 100 */       return getBaseRed(qualityLevel); 
/* 101 */     if (itemTemplateId == 435)
/* 102 */       return getBaseGreen(qualityLevel); 
/* 103 */     if (itemTemplateId == 434)
/* 104 */       return getBaseBlue(qualityLevel); 
/* 105 */     return -1;
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
/*     */   public static int getCompositeColor(int color, int weight, int itemTemplateId, float qualityLevel) {
/* 121 */     int componentWeight = 1000;
/*     */     
/* 123 */     if (itemTemplateId == 439) {
/*     */       
/* 125 */       int r = (getColorRed(color) * weight + getColorRed(getInitialColor(433, qualityLevel)) * 1000) / (weight + 1000);
/*     */ 
/*     */       
/* 128 */       int g = getColorGreen(color);
/* 129 */       int b = getColorBlue(color);
/*     */       
/* 131 */       return createColor(r, g, b);
/*     */     } 
/* 133 */     if (itemTemplateId == 47 || itemTemplateId == 195) {
/*     */       
/* 135 */       int r = getColorRed(color);
/* 136 */       int g = (getColorGreen(color) * weight + getColorGreen(getInitialColor(435, qualityLevel)) * 1000) / (weight + 1000);
/*     */ 
/*     */       
/* 139 */       int b = getColorBlue(color);
/*     */       
/* 141 */       return createColor(r, g, b);
/*     */     } 
/* 143 */     if (itemTemplateId == 440) {
/*     */       
/* 145 */       int r = getColorRed(color);
/* 146 */       int g = getColorGreen(color);
/* 147 */       int b = (getColorBlue(color) * weight + getColorBlue(getInitialColor(434, qualityLevel)) * 1000) / (weight + 1000);
/*     */ 
/*     */ 
/*     */       
/* 151 */       return createColor(r, g, b);
/*     */     } 
/*     */     
/* 154 */     return color;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getCompositeColor(int color, int itemTemplateId, float qualityLevel) {
/* 159 */     if (itemTemplateId == 433) {
/*     */       
/* 161 */       int r = getColorRed(color);
/* 162 */       int g = getColorGreen(color);
/* 163 */       int b = getColorBlue(color);
/* 164 */       int newR = getColorRed(getBaseRed(qualityLevel));
/*     */       
/* 166 */       if (newR > r) {
/* 167 */         r = newR;
/*     */       }
/* 169 */       return createColor(r, g, b);
/*     */     } 
/* 171 */     if (itemTemplateId == 435) {
/*     */       
/* 173 */       int r = getColorRed(color);
/* 174 */       int g = getColorGreen(color);
/* 175 */       int b = getColorBlue(color);
/* 176 */       int newG = getColorGreen(getBaseGreen(qualityLevel));
/*     */       
/* 178 */       if (newG > g) {
/* 179 */         g = newG;
/*     */       }
/* 181 */       return createColor(r, g, b);
/*     */     } 
/* 183 */     if (itemTemplateId == 434) {
/*     */       
/* 185 */       int r = getColorRed(color);
/* 186 */       int g = getColorGreen(color);
/* 187 */       int b = getColorBlue(color);
/* 188 */       int newB = getColorBlue(getBaseBlue(qualityLevel));
/*     */       
/* 190 */       if (newB > b) {
/* 191 */         b = newB;
/*     */       }
/* 193 */       return createColor(r, g, b);
/*     */     } 
/*     */     
/* 196 */     return color;
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getBaseRed(float ql) {
/* 201 */     return createColor(155 + (int)ql, 100 - (int)ql, 100 - (int)ql);
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getBaseGreen(float ql) {
/* 206 */     return createColor(100 - (int)ql, 155 + (int)ql, 100 - (int)ql);
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getBaseBlue(float ql) {
/* 211 */     return createColor(100 - (int)ql, 100 - (int)ql, 155 + (int)ql);
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getBaseWhite(float ql) {
/* 216 */     return createColor(155 + (int)ql, 155 + (int)ql, 155 + (int)ql);
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getBaseBlack(float ql) {
/* 221 */     return createColor(100 - (int)ql, 100 - (int)ql, 100 - (int)ql);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getRGBDescription(int aWurmColor) {
/* 232 */     return "R=" + getColorRed(aWurmColor) + ", G=" + getColorGreen(aWurmColor) + ", B=" + 
/* 233 */       getColorBlue(aWurmColor);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\WurmColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */