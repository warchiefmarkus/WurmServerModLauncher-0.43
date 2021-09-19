/*    */ package com.wurmonline.shared.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ColorDefinitions
/*    */ {
/* 24 */   public static final float[] COLOR_SYSTEM = new float[] { 0.5F, 1.0F, 0.5F };
/* 25 */   public static final float[] COLOR_ERROR = new float[] { 1.0F, 0.3F, 0.3F };
/*    */   
/* 27 */   public static final float[] COLOR_WHITE = new float[] { 1.0F, 1.0F, 1.0F };
/* 28 */   public static final float[] COLOR_BLACK = new float[] { 0.0F, 0.0F, 0.0F };
/* 29 */   public static final float[] COLOR_NAVY_BLUE = new float[] { 0.23F, 0.39F, 1.0F };
/* 30 */   public static final float[] COLOR_GREEN = new float[] { 0.08F, 1.0F, 0.08F };
/* 31 */   public static final float[] COLOR_RED = new float[] { 1.0F, 0.0F, 0.0F };
/* 32 */   public static final float[] COLOR_MAROON = new float[] { 0.5F, 0.0F, 0.0F };
/* 33 */   public static final float[] COLOR_PURPLE = new float[] { 0.5F, 0.0F, 0.5F };
/* 34 */   public static final float[] COLOR_ORANGE = new float[] { 1.0F, 0.85F, 0.24F };
/* 35 */   public static final float[] COLOR_YELLOW = new float[] { 1.0F, 1.0F, 0.0F };
/* 36 */   public static final float[] COLOR_LIME = new float[] { 0.0F, 1.0F, 0.0F };
/* 37 */   public static final float[] COLOR_TEAL = new float[] { 0.0F, 0.5F, 0.5F };
/* 38 */   public static final float[] COLOR_CYAN = new float[] { 0.0F, 1.0F, 1.0F };
/* 39 */   public static final float[] COLOR_ROYAL_BLUE = new float[] { 0.23F, 0.39F, 1.0F };
/* 40 */   public static final float[] COLOR_FUCHSIA = new float[] { 1.0F, 0.0F, 1.0F };
/* 41 */   public static final float[] COLOR_GREY = new float[] { 0.5F, 0.5F, 0.5F };
/* 42 */   public static final float[] COLOR_SILVER = new float[] { 0.75F, 0.75F, 0.75F };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static float[] getColor(byte colorCode) {
/* 54 */     switch (colorCode) {
/*    */       
/*    */       case 0:
/* 57 */         return COLOR_WHITE;
/*    */       case 1:
/* 59 */         return COLOR_BLACK;
/*    */       case 2:
/* 61 */         return COLOR_NAVY_BLUE;
/*    */       case 3:
/* 63 */         return COLOR_GREEN;
/*    */       case 4:
/* 65 */         return COLOR_RED;
/*    */       case 5:
/* 67 */         return COLOR_MAROON;
/*    */       case 6:
/* 69 */         return COLOR_PURPLE;
/*    */       case 7:
/* 71 */         return COLOR_ORANGE;
/*    */       case 8:
/* 73 */         return COLOR_YELLOW;
/*    */       case 9:
/* 75 */         return COLOR_LIME;
/*    */       case 10:
/* 77 */         return COLOR_TEAL;
/*    */       case 11:
/* 79 */         return COLOR_CYAN;
/*    */       case 12:
/* 81 */         return COLOR_ROYAL_BLUE;
/*    */       case 13:
/* 83 */         return COLOR_FUCHSIA;
/*    */       case 14:
/* 85 */         return COLOR_GREY;
/*    */       case 15:
/* 87 */         return COLOR_SILVER;
/*    */       case 100:
/* 89 */         return COLOR_SYSTEM;
/*    */       case 101:
/* 91 */         return COLOR_ERROR;
/*    */     } 
/* 93 */     return COLOR_BLACK;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\ColorDefinitions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */