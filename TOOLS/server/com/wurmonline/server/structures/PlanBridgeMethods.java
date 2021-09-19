/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Point;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.questions.PlanBridgeQuestion;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.BridgeConstants;
/*     */ import com.wurmonline.shared.constants.StructureTypeEnum;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public final class PlanBridgeMethods
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(PlanBridgeQuestion.class.getName());
/*     */   
/*     */   private static boolean DEBUG = true;
/*     */   
/*  48 */   private static final int[] highest = new int[] { 0, 2, 5, 10, 20, 25, 30, 32, 40, 45, 57, 58, 65, 70, 80, 83, 90, 96, 105, 108, 115, 121, 129, 134, 141, 147, 155, 159, 166, 172, 180, 185, 191, 197, 205, 210, 217, 223, 230, 236, 242, 248, 256, 261, 268, 274, 281, 287, 293, 299, 306, 312, 319, 325, 332, 337, 344, 350, 357, 363, 369, 376, 383, 388, 395, 401, 408, 414, 420, 427, 434, 439, 446, 452, 459, 465, 471, 477, 484, 490, 497, 503, 510, 516, 522, 528, 535, 541, 548, 554, 561, 567, 573, 579, 586, 592, 599, 605, 612, 618, 624, 630, 637 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final int[] height0 = new int[0];
/*  61 */   private static final int[] height1 = new int[] { 20 };
/*  62 */   private static final int[] height2 = new int[] { 15, 30 };
/*  63 */   private static final int[] height3 = new int[] { 10, 30, 40 };
/*  64 */   private static final int[] height4 = new int[] { 8, 28, 48, 57 };
/*  65 */   private static final int[] height5 = new int[] { 6, 22, 42, 59, 65 };
/*  66 */   private static final int[] height6 = new int[] { 5, 20, 40, 60, 75, 80 };
/*  67 */   private static final int[] height7 = new int[] { 4, 17, 35, 55, 73, 85, 90 };
/*  68 */   private static final int[] height8 = new int[] { 4, 15, 32, 52, 72, 89, 101, 105 };
/*  69 */   private static final int[] height9 = new int[] { 3, 13, 29, 48, 68, 86, 102, 112, 115 };
/*  70 */   private static final int[] height10 = new int[] { 3, 12, 27, 45, 65, 85, 103, 117, 126, 129 };
/*  71 */   private static final int[] height11 = new int[] { 3, 11, 24, 41, 60, 80, 99, 116, 129, 138, 141 };
/*  72 */   private static final int[] height12 = new int[] { 3, 10, 23, 39, 57, 77, 97, 116, 132, 144, 152, 155 };
/*  73 */   private static final int[] height13 = new int[] { 2, 10, 21, 36, 54, 73, 93, 112, 130, 145, 156, 164, 166 };
/*  74 */   private static final int[] height14 = new int[] { 2, 9, 20, 34, 51, 70, 90, 110, 129, 146, 160, 171, 178, 180 };
/*  75 */   private static final int[] height15 = new int[] { 2, 8, 18, 32, 48, 66, 86, 106, 125, 144, 160, 173, 183, 189, 191 };
/*  76 */   private static final int[] height16 = new int[] { 2, 8, 17, 30, 46, 63, 83, 103, 123, 142, 159, 175, 188, 197, 203, 205 };
/*  77 */   private static final int[] height17 = new int[] { 2, 7, 16, 28, 43, 60, 79, 98, 118, 138, 157, 174, 188, 201, 209, 215, 217 };
/*  78 */   private static final int[] height18 = new int[] { 2, 7, 15, 27, 41, 58, 76, 95, 115, 135, 155, 173, 189, 203, 215, 223, 229, 230 };
/*  79 */   private static final int[] height19 = new int[] { 2, 7, 15, 26, 39, 55, 72, 91, 111, 131, 151, 170, 187, 203, 217, 228, 236, 241, 242 };
/*  80 */   private static final int[] height20 = new int[] { 2, 6, 14, 24, 37, 53, 70, 88, 108, 128, 148, 167, 186, 203, 218, 231, 242, 249, 254, 256 };
/*  81 */   private static final int[] height21 = new int[] { 1, 6, 13, 23, 36, 50, 67, 85, 104, 124, 144, 164, 183, 201, 217, 232, 244, 254, 262, 266, 268 };
/*  82 */   private static final int[] height22 = new int[] { 1, 6, 13, 22, 34, 49, 65, 82, 101, 121, 141, 161, 180, 199, 217, 233, 247, 259, 268, 275, 280, 281 };
/*  83 */   private static final int[] height23 = new int[] { 1, 5, 12, 21, 33, 47, 62, 79, 97, 117, 137, 157, 176, 196, 214, 231, 247, 260, 272, 281, 288, 292, 293 };
/*  84 */   private static final int[] height24 = new int[] { 1, 5, 12, 21, 32, 45, 60, 77, 95, 114, 133, 153, 173, 193, 212, 230, 247, 262, 275, 286, 295, 301, 305, 306 };
/*  85 */   private static final int[] height25 = new int[] { 1, 5, 11, 20, 30, 43, 58, 74, 91, 110, 129, 149, 169, 189, 208, 227, 245, 261, 275, 288, 299, 307, 314, 317, 319 };
/*  86 */   private static final int[] height26 = new int[] { 1, 5, 11, 19, 29, 42, 56, 72, 89, 107, 126, 146, 166, 186, 206, 225, 243, 260, 276, 290, 302, 313, 321, 327, 331, 332 };
/*  87 */   private static final int[] height27 = new int[] { 1, 5, 10, 18, 28, 40, 54, 69, 86, 104, 123, 142, 162, 182, 202, 221, 240, 258, 275, 290, 304, 316, 326, 334, 339, 343, 344 };
/*  88 */   private static final int[] height28 = new int[] { 1, 4, 10, 18, 27, 39, 52, 67, 84, 101, 120, 139, 159, 179, 199, 218, 238, 256, 274, 290, 305, 318, 330, 340, 347, 353, 356, 357 };
/*  89 */   private static final int[] height29 = new int[] { 1, 4, 10, 17, 26, 38, 51, 65, 81, 98, 116, 135, 155, 175, 195, 215, 234, 253, 271, 288, 304, 319, 332, 343, 352, 360, 365, 368, 369 };
/*  90 */   private static final int[] height30 = new int[] { 1, 4, 9, 17, 26, 37, 49, 63, 79, 96, 114, 132, 152, 171, 191, 211, 231, 250, 269, 287, 304, 319, 334, 346, 357, 366, 373, 378, 382, 383 };
/*  91 */   private static final int[] height31 = new int[] { 1, 4, 9, 16, 25, 35, 48, 61, 77, 93, 110, 129, 148, 168, 187, 207, 227, 247, 266, 284, 302, 318, 333, 347, 359, 370, 379, 386, 391, 394, 395 };
/*  92 */   private static final int[] height32 = new int[] { 1, 4, 9, 16, 24, 34, 46, 60, 75, 91, 108, 126, 145, 164, 184, 204, 224, 244, 263, 282, 300, 317, 333, 348, 362, 374, 384, 393, 399, 404, 407, 408 };
/*  93 */   private static final int[] height33 = new int[] { 1, 4, 9, 15, 23, 33, 45, 58, 73, 88, 105, 123, 141, 161, 180, 200, 220, 240, 260, 279, 297, 315, 332, 348, 362, 375, 387, 397, 405, 412, 417, 419, 420 };
/*  94 */   private static final int[] height34 = new int[] { 1, 4, 8, 15, 23, 32, 44, 57, 71, 86, 103, 120, 138, 157, 177, 197, 217, 237, 257, 276, 295, 313, 331, 347, 363, 377, 390, 401, 411, 419, 425, 430, 433, 434 };
/*  95 */   private static final int[] height35 = new int[] { 1, 4, 8, 14, 22, 32, 43, 55, 69, 84, 100, 117, 135, 154, 173, 193, 213, 233, 253, 272, 292, 310, 329, 346, 362, 377, 391, 403, 414, 424, 432, 438, 442, 445, 446 };
/*  96 */   private static final int[] height36 = new int[] { 1, 3, 8, 14, 21, 31, 41, 54, 67, 82, 98, 115, 132, 151, 170, 190, 209, 229, 249, 269, 289, 308, 326, 344, 361, 377, 392, 405, 417, 428, 437, 445, 451, 455, 458, 459 };
/*  97 */   private static final int[] height37 = new int[] { 1, 3, 8, 13, 21, 30, 40, 52, 66, 80, 96, 112, 130, 148, 167, 186, 206, 226, 246, 266, 285, 305, 323, 342, 359, 376, 391, 406, 419, 431, 441, 450, 458, 464, 468, 470, 471 };
/*  98 */   private static final int[] height38 = new int[] { 1, 3, 7, 13, 20, 29, 39, 51, 64, 78, 93, 110, 127, 145, 164, 183, 202, 222, 242, 262, 282, 302, 321, 339, 357, 375, 391, 406, 420, 433, 445, 455, 464, 471, 477, 481, 484, 484 };
/*  99 */   private static final int[] height39 = new int[] { 1, 3, 7, 13, 20, 28, 38, 50, 62, 76, 91, 107, 124, 142, 160, 179, 199, 218, 238, 258, 278, 298, 317, 336, 355, 373, 389, 405, 420, 434, 447, 458, 468, 477, 484, 489, 493, 496, 497 };
/* 100 */   private static final int[] height40 = new int[] { 1, 3, 7, 12, 19, 28, 38, 49, 61, 75, 89, 105, 122, 139, 157, 176, 195, 215, 235, 255, 275, 295, 314, 334, 352, 371, 388, 405, 420, 435, 449, 461, 472, 482, 490, 497, 503, 507, 509, 510 };
/* 101 */   private static final int[] height41 = new int[] { 1, 3, 7, 12, 19, 27, 37, 48, 60, 73, 87, 103, 119, 136, 154, 173, 192, 211, 231, 251, 271, 291, 311, 330, 349, 368, 386, 403, 419, 435, 449, 462, 475, 485, 495, 503, 510, 515, 519, 521, 522 };
/* 102 */   private static final int[] height42 = new int[] { 1, 3, 7, 12, 19, 27, 36, 47, 58, 71, 86, 101, 117, 134, 152, 170, 189, 208, 228, 248, 268, 288, 308, 327, 347, 365, 384, 401, 418, 434, 450, 464, 477, 489, 499, 509, 517, 523, 529, 532, 535, 535 };
/* 103 */   private static final int[] height43 = new int[] { 1, 3, 7, 12, 18, 26, 35, 45, 57, 70, 84, 99, 114, 131, 149, 167, 185, 205, 224, 244, 264, 284, 304, 324, 343, 362, 381, 399, 416, 433, 449, 464, 478, 491, 502, 513, 522, 530, 536, 541, 545, 547, 548 };
/* 104 */   private static final int[] height44 = new int[] { 1, 3, 6, 11, 18, 25, 34, 45, 56, 68, 82, 97, 112, 129, 146, 164, 182, 201, 221, 240, 260, 280, 300, 320, 340, 359, 378, 397, 415, 432, 448, 464, 479, 492, 505, 516, 526, 535, 543, 549, 554, 558, 560, 561 };
/* 105 */   private static final int[] height45 = new int[] { 1, 3, 6, 11, 17, 25, 34, 44, 55, 67, 80, 95, 110, 126, 143, 161, 179, 198, 217, 237, 257, 277, 297, 316, 336, 356, 375, 394, 412, 430, 447, 463, 478, 493, 506, 518, 530, 540, 548, 556, 562, 567, 570, 572, 573 };
/* 106 */   private static final int[] height46 = new int[] { 1, 3, 6, 11, 17, 24, 33, 43, 54, 66, 79, 93, 108, 124, 141, 158, 176, 195, 214, 233, 253, 273, 293, 313, 333, 353, 372, 391, 410, 428, 445, 462, 478, 493, 507, 520, 533, 543, 553, 562, 569, 575, 580, 583, 585, 586 };
/* 107 */   private static final int[] height47 = new int[] { 1, 3, 6, 11, 17, 24, 32, 42, 53, 64, 77, 91, 106, 122, 138, 155, 173, 192, 211, 230, 249, 269, 289, 309, 329, 349, 369, 388, 407, 425, 443, 460, 477, 492, 507, 521, 534, 546, 557, 566, 575, 582, 588, 593, 596, 598, 599 };
/* 108 */   private static final int[] height48 = new int[] { 1, 3, 6, 10, 16, 23, 32, 41, 52, 63, 76, 90, 104, 120, 136, 153, 171, 189, 208, 227, 246, 266, 286, 306, 326, 346, 365, 385, 404, 423, 441, 459, 476, 492, 507, 522, 536, 548, 560, 571, 580, 588, 595, 601, 606, 609, 611, 612 };
/* 109 */   private static final int[] height49 = new int[] { 1, 3, 6, 10, 16, 23, 31, 40, 51, 62, 74, 88, 102, 117, 133, 150, 168, 186, 204, 223, 243, 262, 282, 302, 322, 342, 362, 381, 401, 420, 438, 456, 474, 490, 507, 522, 536, 550, 562, 573, 584, 593, 601, 608, 614, 618, 621, 623, 624 };
/* 110 */   private static final int[] height50 = new int[] { 1, 3, 6, 10, 16, 22, 30, 39, 50, 61, 73, 86, 100, 115, 131, 148, 165, 183, 201, 220, 239, 259, 279, 299, 319, 339, 358, 378, 398, 417, 436, 454, 472, 489, 506, 522, 537, 551, 564, 576, 587, 598, 607, 615, 621, 627, 631, 635, 636, 637 };
/*     */   
/* 112 */   private static final int[][] heights = new int[][] { height0, height1, height2, height3, height4, height5, height6, height7, height8, height9, height10, height11, height12, height13, height14, height15, height16, height17, height18, height19, height20, height21, height22, height23, height24, height25, height26, height27, height28, height29, height30, height31, height32, height33, height34, height35, height36, height37, height38, height39, height40, height41, height42, height43, height44, height45, height46, height47, height48, height49, height50 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getHighest() {
/* 134 */     return highest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void planBridge(Creature performer, byte dir, byte bridgeType, boolean arched, String bridgePlan, int steepness, Point start, Point end, String bridgeName) {
/* 141 */     int layer = performer.getLayer();
/* 142 */     if (Servers.isThisATestServer()) {
/* 143 */       performer.getCommunicator().sendNormalServerMessage("(" + bridgePlan + ")");
/*     */     }
/* 145 */     byte[] parts = bridgePlan.getBytes();
/* 146 */     int[] hts = calcHeights(performer, dir, bridgeType, arched, bridgePlan, steepness, start, end);
/*     */     
/* 148 */     boolean insta = (performer.getPower() > 1);
/* 149 */     if (!PlanBridgeChecks.passChecks(performer, start, end, dir, hts, insta)) {
/*     */       return;
/*     */     }
/* 152 */     if (dir == 0) {
/*     */       int y;
/*     */       
/* 155 */       for (y = start.getY(); y <= end.getY(); y++) {
/*     */         
/* 157 */         for (int x = start.getX(); x <= end.getX(); x++) {
/*     */ 
/*     */           
/*     */           try {
/* 161 */             VolaTile t = Zones.getOrCreateTile(x, y, (layer == 0));
/* 162 */             performer.addStructureTile(t, (byte)1);
/* 163 */             t.addBridge(performer.getStructure());
/*     */           }
/* 165 */           catch (NoSuchStructureException e) {
/*     */ 
/*     */             
/* 168 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 173 */       finaliseBridge(performer, bridgeName);
/*     */ 
/*     */       
/* 176 */       for (y = start.getY(); y <= end.getY(); y++) {
/*     */ 
/*     */         
/* 179 */         int northExit = -1;
/* 180 */         int eastExit = -1;
/* 181 */         int southExit = -1;
/* 182 */         int westExit = -1;
/* 183 */         if (y == start.getY()) {
/* 184 */           northExit = start.getH() - (int)(Zones.getHeightForNode(start.getX(), y, layer) * 10.0F);
/*     */         }
/* 186 */         if (y == end.getY()) {
/* 187 */           southExit = end.getH() - (int)(Zones.getHeightForNode(end.getX(), y + 1, layer) * 10.0F);
/*     */         }
/*     */         
/* 190 */         int yy = y - start.getY();
/*     */         
/* 192 */         byte rdir = dir;
/* 193 */         if (parts[yy] == 97 || parts[yy] == 98) {
/* 194 */           rdir = (byte)((dir + 4) % 8);
/*     */         }
/* 196 */         byte slope = (byte)(hts[yy + 1] - hts[yy]);
/*     */         
/* 198 */         for (int x = start.getX(); x <= end.getX(); x++) {
/*     */           
/* 200 */           byte ndir = rdir;
/* 201 */           BridgeConstants.BridgeType bridgetype = getBridgeType(dir, parts[yy], end.getX(), start.getX(), x, ndir);
/* 202 */           if (!bridgetype.isAbutment() && !bridgetype.isBracing())
/*     */           {
/*     */             
/* 205 */             if (onLeft(dir, end.getX(), start.getX(), x, ndir)) {
/* 206 */               ndir = (byte)((ndir + 4) % 8);
/*     */             }
/*     */           }
/*     */           
/*     */           try {
/* 211 */             VolaTile t = Zones.getOrCreateTile(x, y, (layer == 0));
/*     */ 
/*     */             
/* 214 */             BridgePart bridgePart = new DbBridgePart(bridgetype, x, y, hts[yy], 1.0F, performer.getStructure().getWurmId(), BridgeConstants.BridgeMaterial.fromByte(bridgeType), ndir, slope, northExit, -1, southExit, -1, (byte)0, layer);
/*     */             
/* 216 */             t.addBridgePart(bridgePart);
/*     */           }
/* 218 */           catch (NoSuchStructureException e) {
/*     */ 
/*     */             
/* 221 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       int x;
/*     */ 
/*     */ 
/*     */       
/* 230 */       for (x = start.getX(); x <= end.getX(); x++) {
/*     */         
/* 232 */         for (int y = start.getY(); y <= end.getY(); y++) {
/*     */ 
/*     */           
/*     */           try {
/* 236 */             VolaTile t = Zones.getOrCreateTile(x, y, (layer == 0));
/* 237 */             performer.addStructureTile(t, (byte)1);
/* 238 */             t.addBridge(performer.getStructure());
/*     */           }
/* 240 */           catch (NoSuchStructureException e) {
/*     */ 
/*     */             
/* 243 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 248 */       finaliseBridge(performer, bridgeName);
/*     */ 
/*     */       
/* 251 */       for (x = start.getX(); x <= end.getX(); x++) {
/*     */ 
/*     */         
/* 254 */         int northExit = -1;
/* 255 */         int eastExit = -1;
/* 256 */         int southExit = -1;
/* 257 */         int westExit = -1;
/* 258 */         if (x == start.getX()) {
/* 259 */           westExit = start.getH() - (int)(Zones.getHeightForNode(x, start.getY(), layer) * 10.0F);
/*     */         }
/* 261 */         if (x == end.getX()) {
/* 262 */           eastExit = end.getH() - (int)(Zones.getHeightForNode(x + 1, end.getY(), layer) * 10.0F);
/*     */         }
/*     */         
/* 265 */         int xx = x - start.getX();
/*     */         
/* 267 */         byte rdir = dir;
/* 268 */         if (parts[xx] == 65 || parts[xx] == 66) {
/* 269 */           rdir = (byte)((dir + 4) % 8);
/*     */         }
/* 271 */         byte slope = (byte)(hts[xx + 1] - hts[xx]);
/*     */         
/* 273 */         for (int y = start.getY(); y <= end.getY(); y++) {
/*     */           
/* 275 */           byte ndir = rdir;
/* 276 */           BridgeConstants.BridgeType bridgetype = getBridgeType(dir, parts[xx], end.getY(), start.getY(), y, ndir);
/* 277 */           if (!bridgetype.isAbutment() && !bridgetype.isBracing())
/*     */           {
/*     */             
/* 280 */             if (onLeft(dir, end.getY(), start.getY(), y, ndir)) {
/* 281 */               ndir = (byte)((ndir + 4) % 8);
/*     */             }
/*     */           }
/*     */           
/*     */           try {
/* 286 */             VolaTile t = Zones.getOrCreateTile(x, y, (layer == 0));
/*     */ 
/*     */             
/* 289 */             BridgePart bridgePart = new DbBridgePart(bridgetype, x, y, hts[xx], 1.0F, performer.getStructure().getWurmId(), BridgeConstants.BridgeMaterial.fromByte(bridgeType), ndir, slope, -1, eastExit, -1, westExit, (byte)0, layer);
/*     */             
/* 291 */             t.addBridgePart(bridgePart);
/*     */           }
/* 293 */           catch (NoSuchStructureException e) {
/*     */ 
/*     */             
/* 296 */             logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] calcHeights(Creature performer, byte dir, byte bridgeType, boolean arched, String bridge, int steepness, Point start, Point end) {
/* 306 */     int[] hts = new int[bridge.length() + 1];
/* 307 */     if (arched) {
/*     */       
/* 309 */       if (bridgeType == BridgeConstants.BridgeMaterial.ROPE.getCode()) {
/*     */         
/* 311 */         if (bridge.length() == 1) {
/*     */ 
/*     */           
/* 314 */           hts[0] = start.getH();
/* 315 */           hts[hts.length - 1] = end.getH();
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 321 */           int odd = bridge.length() % 2;
/* 322 */           int lenp1 = bridge.length() + 1;
/* 323 */           int lowBorder = lenp1 >>> 1;
/* 324 */           int hiBorder = bridge.length() - lowBorder;
/* 325 */           float totalSag = steepness / 100.0F;
/* 326 */           float sagDistance = lenp1 * 4.0F * totalSag;
/* 327 */           double scaleCosh = 5.0E-5D;
/* 328 */           double scaleFactor = sagDistance / 5.0E-5D;
/* 329 */           int htDiff = end.getH() - start.getH();
/* 330 */           float htd = htDiff / 10.0F;
/*     */           
/* 332 */           int[] tempBorders = new int[lenp1];
/* 333 */           int[] borders = new int[lenp1];
/* 334 */           float[] scale = new float[lenp1];
/* 335 */           double[] floatSag = new double[lenp1];
/* 336 */           float[] tempSign = new float[lenp1];
/* 337 */           float[] tempAdjust = new float[lenp1];
/* 338 */           float[] adjust = new float[lenp1];
/* 339 */           float[] slopeSag = new float[lenp1];
/* 340 */           int[] dirtSag = new int[lenp1];
/* 341 */           int[] oppSag = new int[lenp1];
/*     */           
/*     */           int x;
/* 344 */           for (x = 0; x < lenp1; x++)
/* 345 */             tempBorders[x] = lowBorder - x; 
/* 346 */           for (x = 0; x < lenp1; x++) {
/*     */             
/* 348 */             if (tempBorders[x] > 0) {
/* 349 */               borders[x] = tempBorders[x];
/*     */             } else {
/* 351 */               borders[x] = tempBorders[x] - odd;
/*     */             } 
/*     */           } 
/* 354 */           for (x = 0; x < lenp1; x++) {
/*     */             
/* 356 */             if (borders[x] >= 0) {
/* 357 */               scale[x] = borders[x] / lowBorder;
/*     */             } else {
/* 359 */               scale[x] = -borders[x] / (hiBorder + odd);
/*     */             } 
/*     */           } 
/* 362 */           for (x = 0; x < lenp1; x++) {
/* 363 */             floatSag[x] = scaleFactor * Math.cosh((scale[x] / 100.0F)) - scaleFactor - sagDistance;
/*     */           }
/* 365 */           for (x = 0; x < lenp1; x++)
/* 366 */             tempSign[x] = Math.signum(borders[x]); 
/* 367 */           for (x = 0; x < lenp1; x++)
/* 368 */             tempAdjust[x] = (tempSign[x] * scale[x] * htd - htd) / 2.0F; 
/* 369 */           for (x = 0; x < lenp1; x++) {
/* 370 */             adjust[x] = Math.abs(tempAdjust[x]);
/*     */           }
/* 372 */           for (x = 0; x < lenp1; x++) {
/* 373 */             slopeSag[x] = (float)(floatSag[x] + adjust[x]);
/*     */           }
/* 375 */           for (x = 0; x < lenp1; x++) {
/* 376 */             dirtSag[x] = (int)(slopeSag[x] * 10.0F);
/*     */           }
/* 378 */           for (x = 0; x < lenp1; x++) {
/* 379 */             oppSag[x] = dirtSag[bridge.length() - x];
/*     */           }
/* 381 */           for (x = 0; x < lenp1; x++) {
/*     */             
/* 383 */             if (htDiff >= 0) {
/* 384 */               hts[x] = start.getH() + dirtSag[x];
/*     */             } else {
/* 386 */               hts[x] = end.getH() + oppSag[x];
/*     */             } 
/*     */           } 
/* 389 */           hts[0] = start.getH();
/* 390 */           hts[hts.length - 1] = end.getH();
/*     */           
/* 392 */           outputheights(performer, "tBorders", tempBorders);
/* 393 */           outputheights(performer, "borders", borders);
/* 394 */           outputheights(performer, "scale", scale);
/* 395 */           outputheights(performer, "fSag", floatSag);
/* 396 */           outputheights(performer, "tSign", tempSign);
/* 397 */           outputheights(performer, "tAdjust", tempAdjust);
/* 398 */           outputheights(performer, "adjust", adjust);
/* 399 */           outputheights(performer, "slopeSag", slopeSag);
/* 400 */           outputheights(performer, "dirtSag", dirtSag);
/* 401 */           outputheights(performer, "oppSag", oppSag);
/*     */         } 
/* 403 */         outputheights(performer, "Hts", hts);
/* 404 */         return hts;
/*     */       } 
/*     */       
/* 407 */       return calcArch(performer, steepness, bridge.length(), start, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 412 */     float slope = (start.getH() - end.getH()) / bridge.length();
/* 413 */     int ht = end.getH();
/* 414 */     float sd = 0.0F;
/* 415 */     if (dir == 0) {
/*     */       
/* 417 */       for (int y = start.getY(); y <= end.getY(); y++) {
/*     */         
/* 419 */         ht = (int)(start.getH() - sd);
/* 420 */         hts[y - start.getY()] = ht;
/* 421 */         sd += slope;
/*     */       } 
/*     */       
/* 424 */       hts[hts.length - 1] = end.getH();
/*     */     }
/*     */     else {
/*     */       
/* 428 */       for (int x = start.getX(); x <= end.getX(); x++) {
/*     */         
/* 430 */         ht = (int)(start.getH() - sd);
/* 431 */         hts[x - start.getX()] = ht;
/* 432 */         sd += slope;
/*     */       } 
/*     */       
/* 435 */       hts[hts.length - 1] = end.getH();
/*     */     } 
/*     */     
/* 438 */     return hts;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] calcArch(Creature performer, int maxSlope, int len, Point start, Point end) {
/* 444 */     if (len == 1) {
/*     */       
/* 446 */       int[] arrayOfInt = new int[2];
/* 447 */       arrayOfInt[0] = end.getH();
/* 448 */       arrayOfInt[1] = start.getH();
/* 449 */       return arrayOfInt;
/*     */     } 
/* 451 */     float factor = maxSlope / 20.0F;
/* 452 */     int odd = len % 2;
/*     */ 
/*     */     
/* 455 */     int middle = 0;
/* 456 */     int closest = 9999;
/* 457 */     int mindiff = 9999;
/* 458 */     StringBuilder bufL = new StringBuilder();
/* 459 */     StringBuilder bufR = new StringBuilder();
/* 460 */     StringBuilder bufS = new StringBuilder();
/* 461 */     StringBuilder bufE = new StringBuilder();
/* 462 */     StringBuilder bufD = new StringBuilder();
/*     */ 
/*     */     
/* 465 */     int[] ahtL = new int[len + 2];
/* 466 */     int[] ahtR = new int[len + 2];
/* 467 */     int[] ahtS = new int[len + 2];
/* 468 */     int[] ahtE = new int[len + 2]; int i;
/* 469 */     for (i = 0; i <= len; i++) {
/*     */       
/* 471 */       ahtL[i] = (int)(highest[i * 2] * factor);
/* 472 */       ahtS[i] = start.getH() + ahtL[i];
/* 473 */       ahtR[i] = (int)(highest[(len - i) * 2] * factor);
/* 474 */       ahtE[i] = end.getH() + ahtR[i];
/*     */     } 
/* 476 */     ahtL[len + 1] = (int)(highest[(len + 1) * 2] * factor);
/* 477 */     ahtS[len + 1] = start.getH() + ahtL[len + 1];
/*     */     
/* 479 */     for (i = 0; i <= len; i++) {
/*     */       
/* 481 */       int diff = ahtE[i] - ahtS[i + odd];
/*     */       
/* 483 */       if (performer.getPower() > 1 && DEBUG) {
/*     */         
/* 485 */         addTo(bufL, "Left", ahtL[i]);
/* 486 */         addTo(bufR, "Rght", ahtR[i]);
/* 487 */         addTo(bufS, "Strt", ahtS[i]);
/* 488 */         addTo(bufE, "End ", ahtE[i]);
/* 489 */         addTo(bufD, "Diff", diff);
/*     */       } 
/*     */       
/* 492 */       if (Math.abs(diff) < mindiff) {
/*     */         
/* 494 */         mindiff = Math.abs(diff);
/* 495 */         closest = diff;
/* 496 */         middle = i;
/*     */       } 
/*     */     } 
/*     */     
/* 500 */     if (performer.getPower() > 1 && DEBUG) {
/*     */       
/* 502 */       performer.getCommunicator().sendNormalServerMessage(bufL.toString() + ")");
/* 503 */       performer.getCommunicator().sendNormalServerMessage(bufR.toString() + ")");
/* 504 */       performer.getCommunicator().sendNormalServerMessage(bufS.toString() + ")");
/* 505 */       performer.getCommunicator().sendNormalServerMessage(bufE.toString() + ")");
/* 506 */       performer.getCommunicator().sendNormalServerMessage(bufD.toString() + ")");
/*     */     } 
/*     */ 
/*     */     
/* 510 */     int lenn = len - middle - odd;
/* 511 */     if (lenn < 0) lenn = 0;
/*     */     
/* 513 */     if (performer.getPower() > 1 && DEBUG) {
/* 514 */       performer.getCommunicator().sendNormalServerMessage("(len:" + len + " middle:" + middle + "," + lenn + " s:" + start.getH() + " e:" + end.getH() + ")");
/*     */     }
/*     */     
/* 517 */     float slopeLeft = 0.0F;
/* 518 */     float slopeRight = 0.0F;
/* 519 */     if (closest < 0 && middle > 0)
/* 520 */       slopeLeft = mindiff / middle; 
/* 521 */     if (closest > 0 && lenn > 0) {
/* 522 */       slopeRight = closest / lenn;
/*     */     }
/* 524 */     if (performer.getPower() > 1 && DEBUG) {
/* 525 */       performer.getCommunicator().sendNormalServerMessage("(middiff:" + closest + " L:" + Float.toString(slopeLeft) + " R:" + Float.toString(slopeRight) + ")");
/*     */     }
/*     */     
/* 528 */     int[] hts = new int[len + 1];
/*     */     
/* 530 */     float slopeLeftAdjustment = 0.0F;
/* 531 */     int[] shts = heights[middle];
/* 532 */     outputheights(performer, "shts", shts);
/*     */     
/* 534 */     for (int j = 0; j < middle; j++) {
/*     */       
/* 536 */       hts[j + 1] = (int)(start.getH() + (shts[j] - slopeLeftAdjustment) * factor);
/* 537 */       slopeLeftAdjustment += slopeLeft;
/*     */     } 
/* 539 */     int[] ehts = heights[lenn];
/* 540 */     float slopeRightAdjustment = 0.0F;
/* 541 */     outputheights(performer, "ehts", ehts);
/*     */     
/* 543 */     for (int k = 0; k < lenn; k++) {
/*     */ 
/*     */       
/* 546 */       hts[len - k - 1] = (int)(end.getH() + (ehts[k] - slopeRightAdjustment) * factor);
/* 547 */       slopeRightAdjustment += slopeRight;
/*     */     } 
/*     */     
/* 550 */     outputheights(performer, "Hts", hts);
/*     */     
/* 552 */     hts[0] = start.getH();
/* 553 */     hts[hts.length - 1] = end.getH();
/*     */ 
/*     */     
/* 556 */     outputheights(performer, "Hts", hts);
/*     */     
/* 558 */     return hts;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void outputheights(Creature performer, String name, int[] hts) {
/* 563 */     if (performer.getPower() > 1 && DEBUG) {
/*     */       
/* 565 */       StringBuilder buf = new StringBuilder();
/* 566 */       for (int ht : hts)
/*     */       {
/* 568 */         addTo(buf, name, ht);
/*     */       }
/* 570 */       performer.getCommunicator().sendNormalServerMessage(buf.toString() + ")");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(StringBuilder buf, String name, int value) {
/* 576 */     if (buf.length() > 0) {
/* 577 */       buf.append(", ");
/*     */     } else {
/* 579 */       buf.append("(" + name + ":");
/* 580 */     }  buf.append(value);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void outputheights(Creature performer, String name, double[] hts) {
/* 585 */     if (performer.getPower() > 1 && DEBUG) {
/*     */       
/* 587 */       StringBuilder buf = new StringBuilder();
/* 588 */       for (double ht : hts)
/*     */       {
/* 590 */         addTo(buf, name, ht);
/*     */       }
/* 592 */       performer.getCommunicator().sendNormalServerMessage(buf.toString() + ")");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(StringBuilder buf, String name, double value) {
/* 598 */     if (buf.length() > 0) {
/* 599 */       buf.append(", ");
/*     */     } else {
/* 601 */       buf.append("(" + name + ":");
/* 602 */     }  buf.append(value);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void outputheights(Creature performer, String name, float[] hts) {
/* 607 */     if (performer.getPower() > 1 && DEBUG) {
/*     */       
/* 609 */       StringBuilder buf = new StringBuilder();
/* 610 */       for (float ht : hts)
/*     */       {
/* 612 */         addTo(buf, name, ht);
/*     */       }
/* 614 */       performer.getCommunicator().sendNormalServerMessage(buf.toString() + ")");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(StringBuilder buf, String name, float value) {
/* 620 */     if (buf.length() > 0) {
/* 621 */       buf.append(", ");
/*     */     } else {
/* 623 */       buf.append("(" + name + ":");
/* 624 */     }  buf.append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean finaliseBridge(Creature performer, String bridgeName) {
/*     */     try {
/* 632 */       Structure structure = performer.getStructure();
/* 633 */       structure.makeFinal(performer, bridgeName);
/* 634 */       performer.getStatus().setBuildingId(structure.getWurmId());
/* 635 */       return true;
/*     */     }
/* 637 */     catch (NoSuchZoneException e1) {
/*     */ 
/*     */       
/* 640 */       logger.log(Level.WARNING, e1.getMessage(), (Throwable)e1);
/*     */     }
/* 642 */     catch (NoSuchStructureException e1) {
/*     */ 
/*     */       
/* 645 */       logger.log(Level.WARNING, e1.getMessage(), (Throwable)e1);
/*     */     }
/* 647 */     catch (IOException e1) {
/*     */ 
/*     */       
/* 650 */       logger.log(Level.WARNING, e1.getMessage(), e1);
/*     */     } 
/* 652 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BridgeConstants.BridgeType getBridgeType(byte dir, byte part, int left, int right, int pos, byte direction) {
/* 657 */     switch (part) {
/*     */ 
/*     */       
/*     */       case 65:
/*     */       case 97:
/* 662 */         if (left == right)
/* 663 */           return BridgeConstants.BridgeType.ABUTMENT_NARROW; 
/* 664 */         if (pos == left) {
/* 665 */           if (dir == direction) {
/* 666 */             return BridgeConstants.BridgeType.ABUTMENT_LEFT;
/*     */           }
/* 668 */           return BridgeConstants.BridgeType.ABUTMENT_RIGHT;
/* 669 */         }  if (pos == right) {
/* 670 */           if (dir == direction) {
/* 671 */             return BridgeConstants.BridgeType.ABUTMENT_RIGHT;
/*     */           }
/* 673 */           return BridgeConstants.BridgeType.ABUTMENT_LEFT;
/*     */         } 
/* 675 */         return BridgeConstants.BridgeType.ABUTMENT_CENTER;
/*     */ 
/*     */       
/*     */       case 66:
/*     */       case 98:
/* 680 */         if (left == right)
/* 681 */           return BridgeConstants.BridgeType.BRACING_NARROW; 
/* 682 */         if (pos == left) {
/* 683 */           if (dir == direction) {
/* 684 */             return BridgeConstants.BridgeType.BRACING_LEFT;
/*     */           }
/* 686 */           return BridgeConstants.BridgeType.BRACING_RIGHT;
/* 687 */         }  if (pos == right) {
/* 688 */           if (dir == direction) {
/* 689 */             return BridgeConstants.BridgeType.BRACING_RIGHT;
/*     */           }
/* 691 */           return BridgeConstants.BridgeType.BRACING_LEFT;
/*     */         } 
/* 693 */         return BridgeConstants.BridgeType.BRACING_CENTER;
/*     */ 
/*     */       
/*     */       case 67:
/* 697 */         if (left == right)
/* 698 */           return BridgeConstants.BridgeType.CROWN_NARROW; 
/* 699 */         if (pos == left || pos == right) {
/* 700 */           return BridgeConstants.BridgeType.CROWN_SIDE;
/*     */         }
/* 702 */         return BridgeConstants.BridgeType.CROWN_CENTER;
/*     */ 
/*     */       
/*     */       case 68:
/* 706 */         if (left == right)
/* 707 */           return BridgeConstants.BridgeType.DOUBLE_NARROW; 
/* 708 */         if (pos == left || pos == right) {
/* 709 */           return BridgeConstants.BridgeType.DOUBLE_SIDE;
/*     */         }
/* 711 */         return BridgeConstants.BridgeType.DOUBLE_CENTER;
/*     */ 
/*     */       
/*     */       case 69:
/* 715 */         if (left == right)
/* 716 */           return BridgeConstants.BridgeType.END_NARROW; 
/* 717 */         if (pos == left || pos == right) {
/* 718 */           return BridgeConstants.BridgeType.END_SIDE;
/*     */         }
/* 720 */         return BridgeConstants.BridgeType.END_CENTER;
/*     */ 
/*     */       
/*     */       case 83:
/* 724 */         if (left == right)
/* 725 */           return BridgeConstants.BridgeType.SUPPORT_NARROW; 
/* 726 */         if (pos == left || pos == right) {
/* 727 */           return BridgeConstants.BridgeType.SUPPORT_SIDE;
/*     */         }
/* 729 */         return BridgeConstants.BridgeType.SUPPORT_CENTER;
/*     */     } 
/*     */ 
/*     */     
/* 733 */     if (left == right)
/* 734 */       return BridgeConstants.BridgeType.FLOATING_NARROW; 
/* 735 */     if (pos == left || pos == right) {
/* 736 */       return BridgeConstants.BridgeType.FLOATING_SIDE;
/*     */     }
/* 738 */     return BridgeConstants.BridgeType.FLOATING_CENTER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean onLeft(byte dir, int left, int right, int pos, byte direction) {
/* 745 */     if (pos == left)
/* 746 */       return (dir == direction); 
/* 747 */     return false;
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
/*     */   
/*     */   public static String[] isBuildingOk(byte bridgeType, byte dir, boolean onSurface, Point start, int startFloorlevel, Point end, int endFloorlevel) {
/* 766 */     if (dir == 0) {
/*     */ 
/*     */       
/* 769 */       int y = start.getY(); int x;
/* 770 */       for (x = start.getX(); x <= end.getX(); x++) {
/*     */         
/* 772 */         VolaTile checkedTile = Zones.getTileOrNull(x, y - 1, onSurface);
/* 773 */         if (checkedTile != null) {
/*     */           
/* 775 */           if (hasNoDoor(bridgeType, checkedTile, startFloorlevel, x, y, x + 1, y))
/* 776 */             return new String[] { "N:No Door", "North end of bridge plan requires a door." }; 
/* 777 */           String nos = noSupport(bridgeType, checkedTile, startFloorlevel, x, y, x + 1, y);
/* 778 */           if (nos.length() > 0) {
/* 779 */             return new String[] { "N:Too Weak", "North end " + nos };
/*     */           }
/*     */         } 
/*     */       } 
/* 783 */       y = end.getY() + 1;
/* 784 */       for (x = start.getX(); x <= end.getX(); x++) {
/*     */         
/* 786 */         VolaTile checkedTile = Zones.getTileOrNull(x, y, onSurface);
/* 787 */         if (checkedTile != null)
/*     */         {
/* 789 */           if (hasNoDoor(bridgeType, checkedTile, endFloorlevel, x, y, x + 1, y))
/* 790 */             return new String[] { "S:No Door", "South end of bridge plan requires a door." }; 
/* 791 */           String nos = noSupport(bridgeType, checkedTile, endFloorlevel, x, y, x + 1, y);
/* 792 */           if (nos.length() > 0) {
/* 793 */             return new String[] { "S:Too Weak", "South end " + nos };
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 800 */       int x = start.getX(); int y;
/* 801 */       for (y = start.getY(); y <= end.getY(); y++) {
/*     */         
/* 803 */         VolaTile checkedTile = Zones.getTileOrNull(x - 1, y, onSurface);
/* 804 */         if (checkedTile != null) {
/*     */           
/* 806 */           if (hasNoDoor(bridgeType, checkedTile, startFloorlevel, x, y, x, y + 1))
/* 807 */             return new String[] { "W:No Door", "West end of bridge plan requires a door." }; 
/* 808 */           String nos = noSupport(bridgeType, checkedTile, startFloorlevel, x, y, x, y + 1);
/* 809 */           if (nos.length() > 0) {
/* 810 */             return new String[] { "W:Too Weak", "West end " + nos };
/*     */           }
/*     */         } 
/*     */       } 
/* 814 */       x = end.getX() + 1;
/* 815 */       for (y = start.getY(); y <= end.getY(); y++) {
/*     */         
/* 817 */         VolaTile checkedTile = Zones.getTileOrNull(x, y, onSurface);
/* 818 */         if (checkedTile != null) {
/*     */           
/* 820 */           if (hasNoDoor(bridgeType, checkedTile, endFloorlevel, x, y, x, y + 1))
/* 821 */             return new String[] { "E:No Door", "East end of bridge plan requires a door." }; 
/* 822 */           String nos = noSupport(bridgeType, checkedTile, endFloorlevel, x, y, x, y + 1);
/* 823 */           if (nos.length() > 0)
/* 824 */             return new String[] { "E:Too Weak", "East end " + nos }; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 828 */     return new String[] { "", "" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasNoDoor(byte bridgeType, VolaTile checkedTile, int floorlevel, int startX, int startY, int endX, int endY) {
/* 835 */     Wall wall = getWall(checkedTile, floorlevel, startX, startY, endX, endY);
/* 836 */     if (wall == null || wall.isDoor())
/* 837 */       return false; 
/* 838 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String noSupport(byte bridgeType, VolaTile checkedTile, int floorlevel, int startX, int startY, int endX, int endY) {
/* 847 */     if (bridgeType != BridgeConstants.BridgeMaterial.ROPE.getCode() && floorlevel >= 1) {
/*     */       
/* 849 */       Wall wall = getWall(checkedTile, floorlevel - 1, startX, startY, endX, endY);
/* 850 */       if (wall == null || wall.getType() != StructureTypeEnum.SOLID) {
/* 851 */         return "needs a solid wall just below planned connection";
/*     */       }
/* 853 */       if (bridgeType != BridgeConstants.BridgeMaterial.WOOD.getCode() && !wall.canSupportStoneBridges()) {
/* 854 */         return "needs a solid stone wall just below planned connection";
/*     */       }
/* 856 */       if (bridgeType != BridgeConstants.BridgeMaterial.WOOD.getCode() && floorlevel >= 2) {
/*     */         
/* 858 */         wall = getWall(checkedTile, floorlevel - 2, startX, startY, endX, endY);
/* 859 */         if (wall == null || wall.getType() != StructureTypeEnum.SOLID || !wall.canSupportStoneBridges())
/* 860 */           return "needs a solid stone wall two floors below planned connection"; 
/*     */       } 
/*     */     } 
/* 863 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Wall getWall(VolaTile checkedTile, int floorlevel, int startX, int startY, int endX, int endY) {
/* 869 */     for (Wall wall : checkedTile.getWalls()) {
/*     */       
/* 871 */       if (wall.getFloorLevel() == floorlevel)
/*     */       {
/* 873 */         if (wall.getStartX() == startX && wall.getStartY() == startY && wall
/* 874 */           .getEndX() == endX && wall.getEndY() == endY)
/* 875 */           return wall; 
/*     */       }
/*     */     } 
/* 878 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\PlanBridgeMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */