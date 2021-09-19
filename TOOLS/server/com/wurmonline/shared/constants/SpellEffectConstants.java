/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpellEffectConstants
/*     */ {
/*     */   public static final int LONGTIMEEFFECT = 100000;
/*     */   public static final byte TYPE_GENERAL = 0;
/*     */   public static final byte TYPE_PLAYER = 1;
/*     */   public static final byte TYPE_KINGDOM = 2;
/*     */   public static final byte TYPE_FIEFDOM = 3;
/*     */   public static final byte TYPE_VILLAGE = 4;
/*     */   public static final byte TYPE_PROXIMITY = 5;
/*     */   public static final byte TYPE_RELIGION = 6;
/*     */   public static final byte TYPE_CLASS = 7;
/*     */   public static final byte TYPE_RACE = 8;
/*     */   public static final byte TYPE_ENCHANTMENT = 9;
/*     */   public static final byte TYPE_ITEM = 10;
/*     */   public static final byte INFLUENCE_BENEFICIAL = 0;
/*     */   public static final byte INFLUENCE_HARMFUL = 1;
/*     */   public static final byte INFLUENCE_NEUTRAL = 2;
/*     */   
/*     */   public static final String getTypeName(byte type) {
/*  50 */     switch (type) {
/*     */       
/*     */       case 0:
/*  53 */         return "General";
/*     */       case 1:
/*  55 */         return "Player";
/*     */       case 2:
/*  57 */         return "Kingdom";
/*     */       case 3:
/*  59 */         return "Fiefdom";
/*     */       case 4:
/*  61 */         return "Village";
/*     */       case 5:
/*  63 */         return "Proximity";
/*     */       case 6:
/*  65 */         return "Religion";
/*     */       case 9:
/*  67 */         return "Enchantment";
/*     */       case 7:
/*  69 */         return "Class";
/*     */       case 8:
/*  71 */         return "Race";
/*     */       case 10:
/*  73 */         return "Item";
/*     */     } 
/*  75 */     return "Unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getInfluenceName(byte influence) {
/*  81 */     switch (influence) {
/*     */       
/*     */       case 0:
/*  84 */         return "Beneficial";
/*     */       case 1:
/*  86 */         return "Harmful";
/*     */       case 2:
/*  88 */         return "Neutral";
/*     */     } 
/*  90 */     return "Unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getInfluenceSymbol(byte influence) {
/*  96 */     switch (influence) {
/*     */       
/*     */       case 0:
/*  99 */         return "+";
/*     */       case 1:
/* 101 */         return "-";
/*     */       case 2:
/* 103 */         return " ";
/*     */     } 
/* 105 */     return "?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isTimed(int duration) {
/* 111 */     if (duration == 0)
/*     */     {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\SpellEffectConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */