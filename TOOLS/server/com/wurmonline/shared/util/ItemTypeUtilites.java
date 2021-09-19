/*     */ package com.wurmonline.shared.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemTypeUtilites
/*     */ {
/*     */   public static final int ITEMTYPE_ITEM = 0;
/*     */   public static final int ITEMTYPE_WOUND = 1;
/*     */   public static final int ITEMTYPE_BODYPART = 2;
/*     */   public static final int ITEMTYPE_CONTAINER = 3;
/*     */   public static final int ITEMTYPE_NODROP = 4;
/*     */   public static final int ITEMTYPE_IS_TWO_HANDER = 5;
/*     */   public static final int ITEMTYPE_INVENTORY_GROUP = 6;
/*     */   public static final int ITEMTYPE_SHOWSLOPES = 7;
/*     */   public static final int ITEMTYPE_TOOLBELT_IGNORE_CONTENTS = 8;
/*     */   
/*     */   public static short calcProfile(boolean wound, boolean bodypart, boolean container, boolean nodrop, boolean twoHanded, boolean inventoryGroup, boolean showSlopes, boolean toolbeltIgnoreContents) {
/*  44 */     short toReturn = 0;
/*  45 */     if (wound)
/*  46 */       toReturn = (short)(toReturn + 2); 
/*  47 */     if (bodypart)
/*  48 */       toReturn = (short)(toReturn + 4); 
/*  49 */     if (container)
/*  50 */       toReturn = (short)(toReturn + 8); 
/*  51 */     if (nodrop)
/*  52 */       toReturn = (short)(toReturn + 16); 
/*  53 */     if (twoHanded)
/*  54 */       toReturn = (short)(toReturn + 32); 
/*  55 */     if (inventoryGroup)
/*  56 */       toReturn = (short)(toReturn + 64); 
/*  57 */     if (showSlopes)
/*  58 */       toReturn = (short)(toReturn + 128); 
/*  59 */     if (toolbeltIgnoreContents)
/*  60 */       toReturn = (short)(toReturn + 256); 
/*  61 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWound(short profile) {
/*  69 */     return ((profile >> 1 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBodypart(short profile) {
/*  77 */     return ((profile >> 2 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isContainer(short profile) {
/*  85 */     return ((profile >> 3 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNodrop(short profile) {
/*  93 */     return ((profile >> 4 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTwoHanded(short profile) {
/* 101 */     return ((profile >> 5 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInventoryGroup(short profile) {
/* 109 */     return ((profile >> 6 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean doesShowSlopes(short profile) {
/* 117 */     return ((profile >> 7 & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toolbeltIgnoreContents(short profile) {
/* 125 */     return ((profile >> 8 & 0x1) == 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\ItemTypeUtilites.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */