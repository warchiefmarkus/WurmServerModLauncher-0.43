/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.mesh.BushData;
/*     */ import com.wurmonline.mesh.TreeData;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Materials
/*     */   implements ItemMaterials, MiscConstants
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(Materials.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTemplateIdForMaterial(byte material) {
/*  49 */     if (material == 0) {
/*  50 */       return -10;
/*     */     }
/*     */     
/*  53 */     switch (material) {
/*     */       
/*     */       case 2:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 83:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*  72 */         return 92;
/*     */       case 3:
/*  74 */         return 30;
/*     */       case 4:
/*  76 */         return 31;
/*     */       case 5:
/*  78 */         return 28;
/*     */       case 6:
/*  80 */         return 29;
/*     */       case 7:
/*  82 */         return 44;
/*     */       case 8:
/*  84 */         return 45;
/*     */       case 9:
/*  86 */         return 205;
/*     */       case 10:
/*  88 */         return 47;
/*     */       case 11:
/*  90 */         return 46;
/*     */       case 12:
/*  92 */         return 49;
/*     */       case 13:
/*  94 */         return 48;
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
/* 120 */         return 9;
/*     */       case 15:
/* 122 */         return 146;
/*     */       case 16:
/* 124 */         return 72;
/*     */       case 17:
/* 126 */         return 214;
/*     */       case 69:
/* 128 */         return 925;
/*     */       case 18:
/* 130 */         return 130;
/*     */       case 19:
/* 132 */         return 130;
/*     */       case 20:
/* 134 */         return -10;
/*     */       case 21:
/* 136 */         return -10;
/*     */       case 22:
/* 138 */         return -10;
/*     */       case 23:
/* 140 */         return -10;
/*     */       case 25:
/* 142 */         return -10;
/*     */       case 26:
/* 144 */         return 128;
/*     */       case 27:
/* 146 */         return 204;
/*     */       case 28:
/* 148 */         return 142;
/*     */       case 29:
/* 150 */         return 70;
/*     */       case 30:
/* 152 */         return 221;
/*     */       case 31:
/* 154 */         return 223;
/*     */       case 32:
/* 156 */         return -10;
/*     */       case 33:
/* 158 */         return -10;
/*     */       case 34:
/* 160 */         return 220;
/*     */       case 35:
/* 162 */         return -10;
/*     */       case 36:
/* 164 */         return 349;
/*     */       case 52:
/* 166 */         return -10;
/*     */       case 54:
/* 168 */         return 380;
/*     */       case 53:
/* 170 */         return 318;
/*     */       case 56:
/* 172 */         return 694;
/*     */       case 57:
/* 174 */         return 698;
/*     */       case 62:
/* 176 */         return 785;
/*     */       case 61:
/* 178 */         return 770;
/*     */       case 89:
/* 180 */         return 1116;
/*     */       case 67:
/* 182 */         return 837;
/*     */       case 96:
/* 184 */         return 1411;
/*     */     } 
/* 186 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 188 */       logger.fine("Returning Template NOID for unexpected material: " + material);
/*     */     }
/* 190 */     return -10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTreeName(byte data) {
/* 197 */     return TreeData.TreeType.fromInt(data).toString() + " tree";
/*     */   }
/*     */ 
/*     */   
/*     */   public static BushData.BushType getBushTypeForWood(byte wood) {
/* 202 */     switch (wood) {
/*     */       
/*     */       case 46:
/* 205 */         return BushData.BushType.LAVENDER;
/*     */       case 47:
/* 207 */         return BushData.BushType.ROSE;
/*     */       case 48:
/* 209 */         return BushData.BushType.THORN;
/*     */       case 49:
/* 211 */         return BushData.BushType.GRAPE;
/*     */       case 50:
/* 213 */         return BushData.BushType.CAMELLIA;
/*     */       case 51:
/* 215 */         return BushData.BushType.OLEANDER;
/*     */       case 71:
/* 217 */         return BushData.BushType.HAZELNUT;
/*     */       case 90:
/* 219 */         return BushData.BushType.RASPBERRY;
/*     */       case 91:
/* 221 */         return BushData.BushType.BLUEBERRY;
/*     */       case 92:
/* 223 */         return BushData.BushType.LINGONBERRY;
/*     */     } 
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TreeData.TreeType getTreeTypeForWood(byte wood) {
/* 232 */     switch (wood) {
/*     */       
/*     */       case 14:
/* 235 */         return TreeData.TreeType.BIRCH;
/*     */       case 37:
/* 237 */         return TreeData.TreeType.PINE;
/*     */       case 38:
/* 239 */         return TreeData.TreeType.OAK;
/*     */       case 39:
/* 241 */         return TreeData.TreeType.CEDAR;
/*     */       case 40:
/* 243 */         return TreeData.TreeType.WILLOW;
/*     */       case 41:
/* 245 */         return TreeData.TreeType.MAPLE;
/*     */       case 42:
/* 247 */         return TreeData.TreeType.APPLE;
/*     */       case 43:
/* 249 */         return TreeData.TreeType.LEMON;
/*     */       case 44:
/* 251 */         return TreeData.TreeType.OLIVE;
/*     */       case 45:
/* 253 */         return TreeData.TreeType.CHERRY;
/*     */       case 63:
/* 255 */         return TreeData.TreeType.CHESTNUT;
/*     */       case 64:
/* 257 */         return TreeData.TreeType.WALNUT;
/*     */       case 65:
/* 259 */         return TreeData.TreeType.FIR;
/*     */       case 66:
/* 261 */         return TreeData.TreeType.LINDEN;
/*     */       case 88:
/* 263 */         return TreeData.TreeType.ORANGE;
/*     */     } 
/* 265 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 267 */       logger.fine("Returning Birch for unexpected material type: " + wood);
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWood(byte material) {
/* 276 */     return MaterialUtilities.isWood(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLeather(byte material) {
/* 281 */     return MaterialUtilities.isLeather(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMetal(byte material) {
/* 286 */     return MaterialUtilities.isMetal(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCloth(byte material) {
/* 291 */     return MaterialUtilities.isCloth(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPaper(byte material) {
/* 296 */     return MaterialUtilities.isPaper(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStone(byte material) {
/* 301 */     return MaterialUtilities.isStone(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isGlass(byte material) {
/* 306 */     return MaterialUtilities.isGlass(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPottery(byte material) {
/* 311 */     return MaterialUtilities.isPottery(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isClay(byte material) {
/* 316 */     return MaterialUtilities.isClay(material);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int getTransmutedTemplate(int templateId) {
/* 321 */     int toReturn = 26;
/* 322 */     switch (templateId)
/*     */     
/*     */     { case 479:
/* 325 */         toReturn = 467;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 392 */         return toReturn;case 467: toReturn = 204; return toReturn;case 204: toReturn = 26; return toReturn;case 26: toReturn = 298; return toReturn;case 298: toReturn = 130; return toReturn;case 130: toReturn = 146; return toReturn;case 146: toReturn = 38; return toReturn;case 38: toReturn = 43; return toReturn;case 43: toReturn = 42; return toReturn;case 42: toReturn = 40; return toReturn;case 40: toReturn = 207; return toReturn;case 207: toReturn = 39; return toReturn;case 39: toReturn = 41; return toReturn;case 46: toReturn = 47; return toReturn;case 47: toReturn = 221; return toReturn;case 221: toReturn = 223; return toReturn;case 223: toReturn = 48; return toReturn;case 48: toReturn = 45; return toReturn;case 45: toReturn = 220; return toReturn;case 220: toReturn = 44; return toReturn;case 44: toReturn = 49; return toReturn; }  toReturn = 26; return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte convertMaterialStringIntoByte(String material) {
/* 397 */     switch (material) {
/*     */       
/*     */       case "flesh":
/* 400 */         return 1;
/*     */       case "meat":
/* 402 */         return 2;
/*     */       case "rye":
/* 404 */         return 3;
/*     */       case "oat":
/* 406 */         return 4;
/*     */       case "barley":
/* 408 */         return 5;
/*     */       case "wheat":
/* 410 */         return 6;
/*     */       case "gold":
/* 412 */         return 7;
/*     */       case "silver":
/* 414 */         return 8;
/*     */       case "steel":
/* 416 */         return 9;
/*     */       case "copper":
/* 418 */         return 10;
/*     */       case "iron":
/* 420 */         return 11;
/*     */       case "lead":
/* 422 */         return 12;
/*     */       case "zinc":
/* 424 */         return 13;
/*     */       case "birchwood":
/* 426 */         return 14;
/*     */       case "stone":
/* 428 */         return 15;
/*     */       case "leather":
/* 430 */         return 16;
/*     */       case "cotton":
/* 432 */         return 17;
/*     */       case "clay":
/* 434 */         return 18;
/*     */       case "pottery":
/* 436 */         return 19;
/*     */       case "glass":
/* 438 */         return 20;
/*     */       case "magic":
/* 440 */         return 21;
/*     */       case "vegetarian":
/* 442 */         return 22;
/*     */       case "fire":
/* 444 */         return 23;
/*     */       case "oil":
/* 446 */         return 25;
/*     */       case "water":
/* 448 */         return 26;
/*     */       case "charcoal":
/* 450 */         return 27;
/*     */       case "dairy":
/* 452 */         return 28;
/*     */       case "honey":
/* 454 */         return 29;
/*     */       case "brass":
/* 456 */         return 30;
/*     */       case "bronze":
/* 458 */         return 31;
/*     */       case "fat":
/* 460 */         return 32;
/*     */       case "paper":
/* 462 */         return 33;
/*     */       case "tin":
/* 464 */         return 34;
/*     */       case "bone":
/* 466 */         return 35;
/*     */       case "salt":
/* 468 */         return 36;
/*     */       case "pinewood":
/* 470 */         return 37;
/*     */       case "oakenwood":
/* 472 */         return 38;
/*     */       case "cedarwood":
/* 474 */         return 39;
/*     */       case "willow":
/* 476 */         return 40;
/*     */       case "maplewood":
/* 478 */         return 41;
/*     */       case "applewood":
/* 480 */         return 42;
/*     */       case "lemonwood":
/* 482 */         return 43;
/*     */       case "olivewood":
/* 484 */         return 44;
/*     */       case "cherrywood":
/* 486 */         return 45;
/*     */       case "lavenderwood":
/* 488 */         return 46;
/*     */       case "rosewood":
/* 490 */         return 47;
/*     */       case "thorn":
/* 492 */         return 48;
/*     */       case "grapewood":
/* 494 */         return 49;
/*     */       case "camelliawood":
/* 496 */         return 50;
/*     */       case "oleanderwood":
/* 498 */         return 51;
/*     */       case "crystal":
/* 500 */         return 52;
/*     */       case "wemp":
/* 502 */         return 53;
/*     */       case "diamond":
/* 504 */         return 54;
/*     */       case "animal":
/* 506 */         return 55;
/*     */       case "adamantine":
/* 508 */         return 56;
/*     */       case "glimmersteel":
/* 510 */         return 57;
/*     */       case "tar":
/* 512 */         return 58;
/*     */       case "peat":
/* 514 */         return 59;
/*     */       case "reed":
/* 516 */         return 60;
/*     */       case "slate":
/* 518 */         return 61;
/*     */       case "marble":
/* 520 */         return 62;
/*     */       case "chestnut":
/* 522 */         return 63;
/*     */       case "walnut":
/* 524 */         return 64;
/*     */       case "firwood":
/* 526 */         return 65;
/*     */       case "lindenwood":
/* 528 */         return 66;
/*     */       case "seryll":
/* 530 */         return 67;
/*     */       case "ivy":
/* 532 */         return 68;
/*     */       case "wool":
/* 534 */         return 69;
/*     */       case "straw":
/* 536 */         return 70;
/*     */       case "hazelnutwood":
/* 538 */         return 71;
/*     */       case "bear":
/* 540 */         return 72;
/*     */       case "beef":
/* 542 */         return 73;
/*     */       case "canine":
/* 544 */         return 74;
/*     */       case "feline":
/* 546 */         return 75;
/*     */       case "dragon":
/* 548 */         return 76;
/*     */       case "fowl":
/* 550 */         return 77;
/*     */       case "game":
/* 552 */         return 78;
/*     */       case "horse":
/* 554 */         return 79;
/*     */       case "human":
/* 556 */         return 80;
/*     */       case "humanoid":
/* 558 */         return 81;
/*     */       case "insect":
/* 560 */         return 82;
/*     */       case "lamb":
/* 562 */         return 83;
/*     */       case "pork":
/* 564 */         return 84;
/*     */       case "seafood":
/* 566 */         return 85;
/*     */       case "snake":
/* 568 */         return 86;
/*     */       case "tough":
/* 570 */         return 87;
/*     */       case "orangewood":
/* 572 */         return 88;
/*     */       case "raspberrywood":
/* 574 */         return 90;
/*     */       case "blueberrywood":
/* 576 */         return 91;
/*     */       case "lingonberrywood":
/* 578 */         return 92;
/*     */       case "metal":
/* 580 */         return 93;
/*     */       case "alloy":
/* 582 */         return 94;
/*     */       case "moonmetal":
/* 584 */         return 95;
/*     */       case "electrum":
/* 586 */         return 96;
/*     */     } 
/* 588 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertMaterialByteIntoString(byte material) {
/* 593 */     switch (material) {
/*     */       
/*     */       case 1:
/* 596 */         return "flesh";
/*     */       case 2:
/* 598 */         return "meat";
/*     */       case 3:
/* 600 */         return "rye";
/*     */       case 4:
/* 602 */         return "oat";
/*     */       case 5:
/* 604 */         return "barley";
/*     */       case 6:
/* 606 */         return "wheat";
/*     */       case 7:
/* 608 */         return "gold";
/*     */       case 8:
/* 610 */         return "silver";
/*     */       case 9:
/* 612 */         return "steel";
/*     */       case 10:
/* 614 */         return "copper";
/*     */       case 11:
/* 616 */         return "iron";
/*     */       case 12:
/* 618 */         return "lead";
/*     */       case 13:
/* 620 */         return "zinc";
/*     */       case 14:
/* 622 */         return "birchwood";
/*     */       case 15:
/* 624 */         return "stone";
/*     */       case 16:
/* 626 */         return "leather";
/*     */       case 17:
/* 628 */         return "cotton";
/*     */       case 18:
/* 630 */         return "clay";
/*     */       case 19:
/* 632 */         return "pottery";
/*     */       case 20:
/* 634 */         return "glass";
/*     */       case 21:
/* 636 */         return "magic";
/*     */       case 22:
/* 638 */         return "vegetarian";
/*     */       case 23:
/* 640 */         return "fire";
/*     */       case 25:
/* 642 */         return "oil";
/*     */       case 26:
/* 644 */         return "water";
/*     */       case 27:
/* 646 */         return "charcoal";
/*     */       case 28:
/* 648 */         return "dairy";
/*     */       case 29:
/* 650 */         return "honey";
/*     */       case 30:
/* 652 */         return "brass";
/*     */       case 31:
/* 654 */         return "bronze";
/*     */       case 32:
/* 656 */         return "fat";
/*     */       case 33:
/* 658 */         return "paper";
/*     */       case 34:
/* 660 */         return "tin";
/*     */       case 35:
/* 662 */         return "bone";
/*     */       case 36:
/* 664 */         return "salt";
/*     */       case 37:
/* 666 */         return "pinewood";
/*     */       case 38:
/* 668 */         return "oakenwood";
/*     */       case 39:
/* 670 */         return "cedarwood";
/*     */       case 40:
/* 672 */         return "willow";
/*     */       case 41:
/* 674 */         return "maplewood";
/*     */       case 42:
/* 676 */         return "applewood";
/*     */       case 43:
/* 678 */         return "lemonwood";
/*     */       case 44:
/* 680 */         return "olivewood";
/*     */       case 45:
/* 682 */         return "cherrywood";
/*     */       case 46:
/* 684 */         return "lavenderwood";
/*     */       case 47:
/* 686 */         return "rosewood";
/*     */       case 48:
/* 688 */         return "thorn";
/*     */       case 49:
/* 690 */         return "grapewood";
/*     */       case 50:
/* 692 */         return "camelliawood";
/*     */       case 51:
/* 694 */         return "oleanderwood";
/*     */       case 52:
/* 696 */         return "crystal";
/*     */       case 53:
/* 698 */         return "wemp";
/*     */       case 54:
/* 700 */         return "diamond";
/*     */       case 55:
/* 702 */         return "animal";
/*     */       case 56:
/* 704 */         return "adamantine";
/*     */       case 57:
/* 706 */         return "glimmersteel";
/*     */       case 58:
/* 708 */         return "tar";
/*     */       case 59:
/* 710 */         return "peat";
/*     */       case 60:
/* 712 */         return "reed";
/*     */       case 61:
/* 714 */         return "slate";
/*     */       case 62:
/* 716 */         return "marble";
/*     */       case 63:
/* 718 */         return "chestnut";
/*     */       case 64:
/* 720 */         return "walnut";
/*     */       case 65:
/* 722 */         return "firwood";
/*     */       case 66:
/* 724 */         return "lindenwood";
/*     */       case 67:
/* 726 */         return "seryll";
/*     */       case 68:
/* 728 */         return "ivy";
/*     */       case 69:
/* 730 */         return "wool";
/*     */       case 70:
/* 732 */         return "straw";
/*     */       case 71:
/* 734 */         return "hazelnutwood";
/*     */       case 72:
/* 736 */         return "bear";
/*     */       case 73:
/* 738 */         return "beef";
/*     */       case 74:
/* 740 */         return "canine";
/*     */       case 75:
/* 742 */         return "feline";
/*     */       case 76:
/* 744 */         return "dragon";
/*     */       case 77:
/* 746 */         return "fowl";
/*     */       case 78:
/* 748 */         return "game";
/*     */       case 79:
/* 750 */         return "horse";
/*     */       case 80:
/* 752 */         return "human";
/*     */       case 81:
/* 754 */         return "humanoid";
/*     */       case 82:
/* 756 */         return "insect";
/*     */       case 83:
/* 758 */         return "lamb";
/*     */       case 84:
/* 760 */         return "pork";
/*     */       case 85:
/* 762 */         return "seafood";
/*     */       case 86:
/* 764 */         return "snake";
/*     */       case 87:
/* 766 */         return "tough";
/*     */       case 88:
/* 768 */         return "orangewood";
/*     */       case 90:
/* 770 */         return "raspberrywood";
/*     */       case 91:
/* 772 */         return "blueberrywood";
/*     */       case 92:
/* 774 */         return "lingonberrywood";
/*     */       case 93:
/* 776 */         return "metal";
/*     */       case 94:
/* 778 */         return "alloy";
/*     */       case 95:
/* 780 */         return "moonmetal";
/*     */       case 96:
/* 782 */         return "electrum";
/*     */     } 
/* 784 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Materials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */