/*     */ package com.wurmonline.shared.util;
/*     */ 
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaterialUtilities
/*     */   implements ItemMaterials
/*     */ {
/*  27 */   private static final Pattern prefixPattern = Pattern.compile("^(?:small|medium|large|huge|unfinished|pile of)", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte COMMON = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte RARE = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte SUPREME = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte FANTASTIC = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String emptyString = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getPrefixEndPos(String baseName) {
/*  56 */     Matcher m = prefixPattern.matcher(baseName);
/*  57 */     if (!m.find())
/*     */     {
/*  59 */       return -1;
/*     */     }
/*  61 */     return m.end();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getRarityString(byte rarity) {
/*  66 */     switch (rarity) {
/*     */       
/*     */       case 0:
/*  69 */         return "";
/*     */       case 1:
/*  71 */         return "rare ";
/*     */       case 2:
/*  73 */         return "supreme ";
/*     */       case 3:
/*  75 */         return "fantastic ";
/*     */     } 
/*  77 */     return "";
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
/*     */   public static final void insertRarityDescription(StringBuilder sb, byte rarity) {
/*  91 */     if (rarity > 0)
/*     */     {
/*  93 */       sb.append(getRarityString(rarity));
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
/*     */   public static final void appendNameWithMaterial(StringBuilder sb, String baseName, byte materialId) {
/* 107 */     String materialName = getClientMaterialString(materialId, true);
/* 108 */     if (materialName == null || baseName.indexOf(materialName) != -1 || baseName.charAt(0) == '"') {
/*     */ 
/*     */       
/* 111 */       sb.append(baseName);
/*     */     } else {
/*     */       String baseNameBefore, baseNameAfter;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       int pos = getPrefixEndPos(baseName);
/* 119 */       if (pos != -1) {
/*     */         
/* 121 */         baseNameBefore = baseName.substring(0, pos);
/* 122 */         baseNameAfter = baseName.substring(pos + 1);
/*     */       }
/*     */       else {
/*     */         
/* 126 */         baseNameBefore = null;
/* 127 */         baseNameAfter = baseName;
/*     */       } 
/*     */       
/* 130 */       if (baseNameBefore != null)
/*     */       {
/* 132 */         sb.append(baseNameBefore).append(" ");
/*     */       }
/*     */       
/* 135 */       sb.append(materialName).append(" ");
/* 136 */       sb.append(baseNameAfter);
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
/*     */   public static final void appendNameWithMaterialSuffix(StringBuilder sb, String baseName, byte materialId) {
/* 149 */     String materialName = getClientMaterialString(materialId, true);
/* 150 */     if (materialName == null || baseName.length() == 0 || baseName.indexOf(materialName) != -1 || baseName.charAt(0) == '"') {
/*     */ 
/*     */       
/* 153 */       sb.append(baseName);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 158 */       sb.append(baseName.trim());
/* 159 */       sb.append(", ");
/*     */       
/* 161 */       sb.append(materialName);
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
/*     */   public static final String getClientMaterialString(byte material, boolean incMeat) {
/* 174 */     switch (material) {
/*     */ 
/*     */       
/*     */       case 11:
/* 178 */         return "iron";
/*     */       case 9:
/* 180 */         return "steel";
/*     */       case 12:
/* 182 */         return "lead";
/*     */       case 10:
/* 184 */         return "copper";
/*     */       case 7:
/* 186 */         return "gold";
/*     */       case 8:
/* 188 */         return "silver";
/*     */       case 30:
/* 190 */         return "brass";
/*     */       case 31:
/* 192 */         return "bronze";
/*     */       case 13:
/* 194 */         return "zinc";
/*     */       case 34:
/* 196 */         return "tin";
/*     */       case 56:
/* 198 */         return "adamantine";
/*     */       case 57:
/* 200 */         return "glimmersteel";
/*     */       
/*     */       case 14:
/* 203 */         return "birchwood";
/*     */       case 37:
/* 205 */         return "pinewood";
/*     */       case 38:
/* 207 */         return "oakenwood";
/*     */       case 63:
/* 209 */         return "chestnut";
/*     */       case 64:
/* 211 */         return "walnut";
/*     */       case 39:
/* 213 */         return "cedarwood";
/*     */       case 40:
/* 215 */         return "willow";
/*     */       case 41:
/* 217 */         return "maplewood";
/*     */       case 42:
/* 219 */         return "applewood";
/*     */       case 43:
/* 221 */         return "lemonwood";
/*     */       case 44:
/* 223 */         return "olivewood";
/*     */       case 45:
/* 225 */         return "cherrywood";
/*     */       case 46:
/* 227 */         return "lavenderwood";
/*     */       case 47:
/* 229 */         return "rosewood";
/*     */       case 48:
/* 231 */         return "thorn";
/*     */       case 49:
/* 233 */         return "grapewood";
/*     */       case 50:
/* 235 */         return "camelliawood";
/*     */       case 51:
/* 237 */         return "oleanderwood";
/*     */       case 66:
/* 239 */         return "lindenwood";
/*     */       case 65:
/* 241 */         return "firwood";
/*     */       case 71:
/* 243 */         return "hazelnutwood";
/*     */       case 88:
/* 245 */         return "orangewood";
/*     */       case 90:
/* 247 */         return "raspberrywood";
/*     */       case 91:
/* 249 */         return "blueberrywood";
/*     */       case 92:
/* 251 */         return "lingonberrywood";
/*     */       
/*     */       case 3:
/* 254 */         return "rye";
/*     */       case 4:
/* 256 */         return "oat";
/*     */       case 5:
/* 258 */         return "barley";
/*     */       case 6:
/* 260 */         return "wheat";
/*     */       
/*     */       case 2:
/* 263 */         return null;
/*     */       case 72:
/* 265 */         if (incMeat)
/* 266 */           return "bear"; 
/* 267 */         return null;
/*     */       case 73:
/* 269 */         if (incMeat)
/* 270 */           return "beef"; 
/* 271 */         return null;
/*     */       case 74:
/* 273 */         if (incMeat)
/* 274 */           return "canine"; 
/* 275 */         return null;
/*     */       case 75:
/* 277 */         if (incMeat)
/* 278 */           return "feline"; 
/* 279 */         return null;
/*     */       case 76:
/* 281 */         if (incMeat)
/* 282 */           return "dragon"; 
/* 283 */         return null;
/*     */       case 77:
/* 285 */         if (incMeat)
/* 286 */           return "fowl"; 
/* 287 */         return null;
/*     */       case 78:
/* 289 */         if (incMeat)
/* 290 */           return "game"; 
/* 291 */         return null;
/*     */       case 79:
/* 293 */         if (incMeat)
/* 294 */           return "horse"; 
/* 295 */         return null;
/*     */       case 80:
/* 297 */         if (incMeat)
/* 298 */           return "human"; 
/* 299 */         return null;
/*     */       case 81:
/* 301 */         if (incMeat)
/* 302 */           return "humanoid"; 
/* 303 */         return null;
/*     */       case 82:
/* 305 */         if (incMeat)
/* 306 */           return "insect"; 
/* 307 */         return null;
/*     */       case 83:
/* 309 */         if (incMeat)
/* 310 */           return "lamb"; 
/* 311 */         return null;
/*     */       case 84:
/* 313 */         if (incMeat)
/* 314 */           return "pork"; 
/* 315 */         return null;
/*     */       case 85:
/* 317 */         if (incMeat)
/* 318 */           return "seafood"; 
/* 319 */         return null;
/*     */       case 86:
/* 321 */         if (incMeat)
/* 322 */           return "snake"; 
/* 323 */         return null;
/*     */       case 87:
/* 325 */         if (incMeat)
/* 326 */           return "tough"; 
/* 327 */         return null;
/*     */       
/*     */       case 16:
/* 330 */         return "leather";
/*     */       case 17:
/* 332 */         return "cotton";
/*     */       case 69:
/* 334 */         return "wool";
/*     */       case 18:
/* 336 */         return "clay";
/*     */       case 19:
/* 338 */         return "pottery";
/*     */       case 58:
/* 340 */         return "tar";
/*     */       case 59:
/* 342 */         return "peat";
/*     */       
/*     */       case 61:
/* 345 */         return "slate";
/*     */       
/*     */       case 62:
/* 348 */         return "marble";
/*     */       case 89:
/* 350 */         return "sandstone";
/*     */       
/*     */       case 67:
/* 353 */         return "seryll";
/*     */       case 70:
/* 355 */         return "straw";
/*     */       
/*     */       case 93:
/* 358 */         return "metal";
/*     */       case 94:
/* 360 */         return "alloy";
/*     */       case 95:
/* 362 */         return "moonmetal";
/*     */       case 96:
/* 364 */         return "electrum";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 385 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getMaterialString(byte material) {
/* 391 */     String toReturn = "unknown";
/*     */     
/* 393 */     switch (material)
/*     */     
/*     */     { case 3:
/* 396 */         toReturn = "rye";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 678 */         return toReturn;case 4: toReturn = "oat"; return toReturn;case 5: toReturn = "barley"; return toReturn;case 6: toReturn = "wheat"; return toReturn;case 11: toReturn = "iron"; return toReturn;case 9: toReturn = "steel"; return toReturn;case 12: toReturn = "lead"; return toReturn;case 10: toReturn = "copper"; return toReturn;case 7: toReturn = "gold"; return toReturn;case 8: toReturn = "silver"; return toReturn;case 30: toReturn = "brass"; return toReturn;case 31: toReturn = "bronze"; return toReturn;case 14: toReturn = "birchwood"; return toReturn;case 37: toReturn = "pinewood"; return toReturn;case 38: toReturn = "oakenwood"; return toReturn;case 63: toReturn = "chestnut"; return toReturn;case 64: toReturn = "walnut"; return toReturn;case 39: toReturn = "cedarwood"; return toReturn;case 40: toReturn = "willow"; return toReturn;case 41: toReturn = "maplewood"; return toReturn;case 42: toReturn = "applewood"; return toReturn;case 43: toReturn = "lemonwood"; return toReturn;case 44: toReturn = "olivewood"; return toReturn;case 45: toReturn = "cherrywood"; return toReturn;case 46: toReturn = "lavenderwood"; return toReturn;case 47: toReturn = "rosewood"; return toReturn;case 48: toReturn = "thorn"; return toReturn;case 49: toReturn = "grapewood"; return toReturn;case 50: toReturn = "camelliawood"; return toReturn;case 51: toReturn = "oleanderwood"; return toReturn;case 66: toReturn = "lindenwood"; return toReturn;case 65: toReturn = "firwood"; return toReturn;case 68: toReturn = "ivy"; return toReturn;case 13: toReturn = "zinc"; return toReturn;case 1: toReturn = "flesh"; return toReturn;case 15: toReturn = "stone"; return toReturn;case 16: toReturn = "leather"; return toReturn;case 17: toReturn = "cotton"; return toReturn;case 69: toReturn = "wool"; return toReturn;case 18: toReturn = "clay"; return toReturn;case 19: toReturn = "pottery"; return toReturn;case 34: toReturn = "tin"; return toReturn;case 20: toReturn = "glass"; return toReturn;case 21: toReturn = "magic"; return toReturn;case 22: toReturn = "vegetarian"; return toReturn;case 23: toReturn = "fire"; return toReturn;case 28: toReturn = "dairy"; return toReturn;case 25: toReturn = "oil"; return toReturn;case 26: toReturn = "water"; return toReturn;case 27: toReturn = "charcoal"; return toReturn;case 29: toReturn = "honey"; return toReturn;case 32: toReturn = "fat"; return toReturn;case 33: toReturn = "paper"; return toReturn;case 35: toReturn = "bone"; return toReturn;case 36: toReturn = "salt"; return toReturn;case 52: toReturn = "crystal"; return toReturn;case 54: toReturn = "diamond"; return toReturn;case 56: toReturn = "adamantine"; return toReturn;case 57: toReturn = "glimmersteel"; return toReturn;case 58: toReturn = "tar"; return toReturn;case 59: toReturn = "peat"; return toReturn;case 60: toReturn = "reed"; return toReturn;case 62: toReturn = "marble"; return toReturn;case 89: toReturn = "sandstone"; return toReturn;case 67: toReturn = "seryll"; return toReturn;case 61: toReturn = "slate"; return toReturn;case 70: toReturn = "straw"; return toReturn;case 71: toReturn = "hazelnutwood"; return toReturn;case 72: toReturn = "bear"; return toReturn;case 73: toReturn = "beef"; return toReturn;case 74: toReturn = "canine"; return toReturn;case 75: toReturn = "feline"; return toReturn;case 76: toReturn = "dragon"; return toReturn;case 77: toReturn = "fowl"; return toReturn;case 78: toReturn = "game"; return toReturn;case 79: toReturn = "horse"; return toReturn;case 80: toReturn = "human"; return toReturn;case 81: toReturn = "humanoid"; return toReturn;case 82: toReturn = "insect"; return toReturn;case 83: toReturn = "lamb"; return toReturn;case 84: toReturn = "pork"; return toReturn;case 85: toReturn = "seafood"; return toReturn;case 86: toReturn = "snake"; return toReturn;case 87: toReturn = "tough"; return toReturn;case 88: toReturn = "orangewood"; return toReturn;case 90: toReturn = "raspberrywood"; return toReturn;case 91: toReturn = "blueberrywood"; return toReturn;case 92: toReturn = "lingonberrywood"; return toReturn;case 93: toReturn = "metal"; return toReturn;case 94: toReturn = "alloy"; return toReturn;case 95: toReturn = "moonmetal"; return toReturn;case 96: toReturn = "electrum"; return toReturn; }  toReturn = "unknown"; return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getDragonLeatherMaterialNameFromColour(float red, float green, float blue) {
/* 683 */     if (red == 10.0F && green == 210.0F && blue == 10.0F)
/*     */     {
/* 685 */       return ".green";
/*     */     }
/* 687 */     if (red == 10.0F && green == 10.0F && blue == 10.0F)
/*     */     {
/* 689 */       return ".black";
/*     */     }
/* 691 */     if (red == 255.0F && green == 255.0F && blue == 255.0F)
/*     */     {
/* 693 */       return ".white";
/*     */     }
/* 695 */     if (red == 215.0F && green == 40.0F && blue == 40.0F)
/*     */     {
/* 697 */       return ".red";
/*     */     }
/* 699 */     if (red == 40.0F && green == 40.0F && blue == 215.0F)
/*     */     {
/* 701 */       return ".blue";
/*     */     }
/*     */     
/* 704 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLeather(byte material) {
/* 709 */     switch (material) {
/*     */       
/*     */       case 16:
/* 712 */         return true;
/*     */     } 
/* 714 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCloth(byte material) {
/* 720 */     switch (material) {
/*     */       
/*     */       case 17:
/*     */       case 69:
/* 724 */         return true;
/*     */     } 
/* 726 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPaper(byte material) {
/* 732 */     switch (material) {
/*     */       
/*     */       case 33:
/* 735 */         return true;
/*     */     } 
/* 737 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStone(byte material) {
/* 743 */     switch (material) {
/*     */       
/*     */       case 15:
/*     */       case 61:
/*     */       case 62:
/*     */       case 89:
/* 749 */         return true;
/*     */     } 
/* 751 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGlass(byte material) {
/* 757 */     switch (material) {
/*     */       
/*     */       case 20:
/*     */       case 52:
/*     */       case 54:
/* 762 */         return true;
/*     */     } 
/* 764 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPottery(byte material) {
/* 770 */     switch (material) {
/*     */       
/*     */       case 19:
/* 773 */         return true;
/*     */     } 
/* 775 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClay(byte material) {
/* 781 */     switch (material) {
/*     */       
/*     */       case 18:
/*     */       case 19:
/* 785 */         return true;
/*     */     } 
/* 787 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWood(byte material) {
/* 793 */     switch (material) {
/*     */ 
/*     */       
/*     */       case 14:
/*     */       case 37:
/*     */       case 38:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 42:
/*     */       case 43:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 47:
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 63:
/*     */       case 64:
/*     */       case 65:
/*     */       case 66:
/*     */       case 71:
/*     */       case 88:
/*     */       case 90:
/*     */       case 91:
/*     */       case 92:
/* 821 */         return true;
/*     */     } 
/* 823 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMetal(byte material) {
/* 829 */     switch (material) {
/*     */       
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 30:
/*     */       case 31:
/*     */       case 34:
/*     */       case 56:
/*     */       case 57:
/*     */       case 67:
/*     */       case 93:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/* 848 */         return true;
/*     */     } 
/* 850 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGrain(byte material) {
/* 856 */     switch (material) {
/*     */       
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 862 */         return true;
/*     */     } 
/* 864 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLiquid(byte material) {
/* 870 */     switch (material) {
/*     */       
/*     */       case 25:
/*     */       case 26:
/*     */       case 28:
/*     */       case 29:
/* 876 */         return true;
/*     */     } 
/* 878 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\MaterialUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */