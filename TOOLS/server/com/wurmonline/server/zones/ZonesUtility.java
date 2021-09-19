/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.Point;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.MethodsFishing;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.highways.Routes;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZonesUtility
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(ZonesUtility.class.getName());
/*  53 */   static final Color waystoneSurface = Color.CYAN;
/*  54 */   static final Color waystoneCave = new Color(0, 153, 153);
/*     */   
/*  56 */   static final Color catseye0Surface = new Color(255, 255, 255);
/*  57 */   static final Color catseye0Cave = new Color(224, 224, 224);
/*     */   
/*  59 */   static final Color catseye1Surface = new Color(255, 0, 0);
/*  60 */   static final Color catseye1Cave = new Color(204, 0, 0);
/*     */   
/*  62 */   static final Color catseye2Surface = new Color(0, 0, 255);
/*  63 */   static final Color catseye2Cave = new Color(0, 0, 153);
/*     */   
/*  65 */   static final Color catseye3Surface = new Color(0, 255, 0);
/*  66 */   static final Color catseye3Cave = new Color(0, 153, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveFishSpots(MeshIO mesh) {
/*  79 */     logger.log(Level.INFO, "[FISH_SPOTS]: Started");
/*  80 */     int size = 256;
/*  81 */     Color[][] colours = new Color[256][256];
/*  82 */     for (int season = 0; season < 4; season++) {
/*     */       
/*  84 */       Point offset = MethodsFishing.getSeasonOffset(season);
/*     */       
/*  86 */       Color bgColour = MethodsFishing.getBgColour(season);
/*  87 */       for (int x = 0; x < 128; x++) {
/*     */         
/*  89 */         for (int y = 0; y < 128; y++)
/*     */         {
/*  91 */           colours[offset.getX() + x][offset.getY() + y] = bgColour;
/*     */         }
/*     */       } 
/*     */       
/*  95 */       Point[] spots = MethodsFishing.getSpecialSpots(0, 0, season);
/*  96 */       for (Point spot : spots) {
/*     */ 
/*     */         
/*  99 */         Color fishColour = MethodsFishing.getFishColour(spot.getH());
/* 100 */         for (int i = spot.getX() - 5; i <= spot.getX() + 5; i++) {
/*     */           
/* 102 */           for (int y = spot.getY() - 5; y <= spot.getY() + 5; y++)
/*     */           {
/* 104 */             colours[offset.getX() + i][offset.getY() + y] = fishColour;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     BufferedImage bmi = makeBufferedImage(colours, 256);
/* 111 */     String filename = "fishSpots.png";
/* 112 */     File f = new File("fishSpots.png");
/*     */     
/*     */     try {
/* 115 */       ImageIO.write(bmi, "png", f);
/* 116 */       logger.log(Level.INFO, "[FISH_SPOTS]: Finished");
/*     */     }
/* 118 */     catch (IOException e) {
/*     */       
/* 120 */       logger.log(Level.WARNING, "[FISH_SPOTS]: Failed to produce fishSpots.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveMapDump(MeshIO mesh) {
/* 126 */     int size = mesh.getSize();
/* 127 */     Color[][] colors = new Color[size][size];
/* 128 */     int maxH = 0;
/* 129 */     int minH = 0;
/*     */     
/* 131 */     float divider = 10.0F; int x;
/* 132 */     for (x = 0; x < size; x++) {
/*     */       
/* 134 */       for (int y = 0; y < size; y++) {
/*     */         
/* 136 */         int tileId = mesh.getTile(x, y);
/* 137 */         int height = Tiles.decodeHeight(tileId);
/* 138 */         if (height > maxH)
/* 139 */           maxH = height; 
/* 140 */         if (height < minH) {
/* 141 */           minH = height;
/*     */         }
/*     */       } 
/*     */     } 
/* 145 */     maxH = (int)(maxH / 10.0F);
/* 146 */     minH = (int)(minH / 10.0F);
/*     */     
/* 148 */     for (x = 0; x < size; x++) {
/*     */       
/* 150 */       for (int y = 0; y < size; y++) {
/*     */         
/* 152 */         int tileId = mesh.getTile(x, y);
/* 153 */         byte type = Tiles.decodeType(tileId);
/* 154 */         Tiles.Tile tile = Tiles.getTile(type);
/* 155 */         int height = Tiles.decodeHeight(tileId);
/* 156 */         if (height > 0) {
/*     */           
/* 158 */           if (type == 4)
/*     */           {
/* 160 */             float tenth = height / 10.0F;
/* 161 */             float percent = tenth / maxH;
/* 162 */             int step = (int)(150.0F * percent);
/* 163 */             colors[x][y] = new Color(Math.min(200, 10 + step), Math.min(200, 10 + step), Math.min(200, 10 + step));
/*     */           }
/* 165 */           else if (type == 12)
/*     */           {
/* 167 */             colors[x][y] = Color.MAGENTA;
/*     */           }
/*     */           else
/*     */           {
/* 171 */             float tenth = height / 10.0F;
/* 172 */             float percent = tenth / maxH;
/* 173 */             int step = (int)(190.0F * percent);
/* 174 */             Color c = tile.getColor();
/*     */ 
/*     */             
/* 177 */             colors[x][y] = new Color(c.getRed(), Math.min(255, c.getGreen() + step), c.getBlue());
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 184 */           float tenth = height / 10.0F;
/* 185 */           float percent = tenth / minH;
/* 186 */           int step = (int)(255.0F * percent);
/* 187 */           colors[x][y] = new Color(0, 0, Math.max(20, 255 - Math.abs(step)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 192 */     BufferedImage bmi = new BufferedImage(size * 4, size * 4, 2);
/* 193 */     Graphics g = bmi.createGraphics();
/*     */     
/* 195 */     for (int i = 0; i < size; i++) {
/*     */       
/* 197 */       for (int y = 0; y < size; y++) {
/*     */         
/* 199 */         g.setColor(colors[i][y]);
/*     */         
/* 201 */         g.fillRect(i * 4, y * 4, 4, 4);
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     File f = new File("mapdump.png");
/*     */     
/*     */     try {
/* 208 */       ImageIO.write(bmi, "png", f);
/*     */     }
/* 210 */     catch (IOException e) {
/*     */       
/* 212 */       logger.log(Level.WARNING, "Failed to produce mapdump.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveCreatureDistributionAsImg(MeshIO mesh) {
/* 218 */     logger.log(Level.INFO, "[CREATURE_DUMP]: Started");
/* 219 */     int size = mesh.getSize();
/* 220 */     Color[][] colors = getHeightMap(size);
/* 221 */     Creature[] crets = Creatures.getInstance().getCreatures();
/*     */     
/* 223 */     for (Creature c : crets) {
/*     */       
/* 225 */       if (c.isGuard()) {
/* 226 */         colors[c.getTileX()][c.getTileY()] = Color.decode("#006000");
/* 227 */       } else if (c.isBred()) {
/* 228 */         colors[c.getTileX()][c.getTileY()] = Color.CYAN;
/* 229 */       } else if (c.isDomestic()) {
/* 230 */         colors[c.getTileX()][c.getTileY()] = Color.YELLOW;
/*     */       } else {
/* 232 */         colors[c.getTileX()][c.getTileY()] = Color.RED;
/*     */       } 
/*     */     } 
/* 235 */     BufferedImage bmi = makeBufferedImage(colors, size);
/* 236 */     File f = new File("creatures.png");
/*     */     
/*     */     try {
/* 239 */       ImageIO.write(bmi, "png", f);
/* 240 */       logger.log(Level.INFO, "[CREATURE_DUMP]: Finished");
/*     */     }
/* 242 */     catch (IOException e) {
/*     */       
/* 244 */       logger.log(Level.WARNING, "[CREATURE_DUMP]: Failed to produce creatures.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveMapWithMarkersAsImg() {
/* 253 */     logger.log(Level.INFO, "[MAP WITH MARKERS DUMP]: Started");
/* 254 */     int size = Server.surfaceMesh.getSize();
/* 255 */     Color[][] colours = getHeightMap(size);
/*     */     
/* 257 */     AddOptedInVillages(size, colours);
/*     */     
/* 259 */     AddMarkers(Items.getMarkers(), colours);
/*     */     
/* 261 */     BufferedImage bmi = makeBufferedImage(colours, size);
/* 262 */     File f = new File("markersdump.png");
/*     */     
/*     */     try {
/* 265 */       ImageIO.write(bmi, "png", f);
/* 266 */       logger.log(Level.INFO, "[MAP WITH MARKERS DUMP]: Finished");
/*     */     }
/* 268 */     catch (IOException e) {
/*     */       
/* 270 */       logger.log(Level.WARNING, "[MAP WITH MARKERS DUMP]: Failed to produce markers.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveRoutesAsImg() {
/* 279 */     logger.log(Level.INFO, "[ROUTES_DUMP]: Started");
/* 280 */     int size = Server.surfaceMesh.getSize();
/* 281 */     Color[][] colours = getHeightMap(size);
/*     */     
/* 283 */     AddMarkers(Routes.getMarkers(), colours);
/*     */     
/* 285 */     BufferedImage bmi = makeBufferedImage(colours, size);
/* 286 */     File f = new File("routes.png");
/*     */     
/*     */     try {
/* 289 */       ImageIO.write(bmi, "png", f);
/* 290 */       logger.log(Level.INFO, "[ROUTES_DUMP]: Finished");
/*     */     }
/* 292 */     catch (IOException e) {
/*     */       
/* 294 */       logger.log(Level.WARNING, "[ROUTES_DUMP]: Failed to produce routes.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveWaterTypesAsImg(boolean onSurface) {
/* 300 */     logger.log(Level.INFO, "[WATER_TYPES_DUMP]: Started");
/* 301 */     int size = Server.surfaceMesh.getSize();
/* 302 */     Color[][] colours = getHeightMap(size);
/*     */     
/* 304 */     for (int x = 0; x < size; x++) {
/*     */       
/* 306 */       for (int y = 0; y < size; y++) {
/*     */         
/* 308 */         switch (WaterType.getWaterType(x, y, onSurface)) {
/*     */           
/*     */           case 1:
/* 311 */             colours[x][y] = new Color(174, 168, 253);
/*     */             break;
/*     */           case 2:
/* 314 */             colours[x][y] = new Color(137, 128, 252);
/*     */             break;
/*     */           case 3:
/* 317 */             colours[x][y] = new Color(115, 150, 165);
/*     */             break;
/*     */           case 4:
/* 320 */             colours[x][y] = new Color(163, 200, 176);
/*     */             break;
/*     */           case 5:
/* 323 */             colours[x][y] = new Color(235, 200, 176);
/*     */             break;
/*     */           case 6:
/* 326 */             colours[x][y] = new Color(185, 150, 165);
/*     */             break;
/*     */         } 
/*     */       
/*     */       } 
/*     */     } 
/* 332 */     BufferedImage bmi = makeBufferedImage(colours, size);
/* 333 */     String filename = onSurface ? "waterSurfaceTypes.png" : "waterCaveTypes.png";
/* 334 */     File f = new File(filename);
/*     */     
/*     */     try {
/* 337 */       ImageIO.write(bmi, "png", f);
/* 338 */       logger.log(Level.INFO, "[WATER_TYPES_DUMP]: Finished");
/*     */     }
/* 340 */     catch (IOException e) {
/*     */       
/* 342 */       logger.log(Level.WARNING, "[WATER_TYPES_DUMP]: Failed to produce (" + onSurface + ") waterTypes.png", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Color[][] getHeightMap(int size) {
/* 348 */     Color[][] colours = new Color[size][size];
/*     */     
/* 350 */     int maxH = 0;
/* 351 */     int minH = 0;
/* 352 */     float divider = 10.0F; int x;
/* 353 */     for (x = 0; x < size; x++) {
/*     */       
/* 355 */       for (int y = 0; y < size; y++) {
/*     */         
/* 357 */         int tileId = Server.surfaceMesh.getTile(x, y);
/* 358 */         int height = Tiles.decodeHeight(tileId);
/* 359 */         if (height > maxH)
/* 360 */           maxH = height; 
/* 361 */         if (height < minH)
/* 362 */           minH = height; 
/*     */       } 
/*     */     } 
/* 365 */     maxH = (int)(maxH / 10.0F);
/* 366 */     minH = (int)(minH / 10.0F);
/*     */     
/* 368 */     for (x = 0; x < size; x++) {
/*     */       
/* 370 */       for (int y = 0; y < size; y++) {
/*     */         
/* 372 */         int tileId = Server.surfaceMesh.getTile(x, y);
/* 373 */         byte type = Tiles.decodeType(tileId);
/* 374 */         Tiles.Tile tile = Tiles.getTile(type);
/* 375 */         int height = Tiles.decodeHeight(tileId);
/* 376 */         if (height > 0) {
/*     */           
/* 378 */           if (type == 4)
/*     */           {
/* 380 */             float tenth = height / 10.0F;
/* 381 */             float percent = tenth / maxH;
/* 382 */             int step = (int)(150.0F * percent);
/* 383 */             colours[x][y] = new Color(Math.min(200, 10 + step), Math.min(200, 10 + step), Math.min(200, 10 + step));
/*     */           }
/* 385 */           else if (type == 12)
/*     */           {
/* 387 */             colours[x][y] = Color.MAGENTA;
/*     */           }
/*     */           else
/*     */           {
/* 391 */             float tenth = height / 10.0F;
/* 392 */             float percent = tenth / maxH;
/* 393 */             int step = (int)(190.0F * percent);
/* 394 */             Color c = tile.getColor();
/* 395 */             colours[x][y] = new Color(c.getRed(), Math.min(255, c.getGreen() + step), c.getBlue());
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 400 */           float tenth = height / 10.0F;
/* 401 */           float percent = tenth / minH;
/* 402 */           int step = (int)(255.0F * percent);
/* 403 */           colours[x][y] = new Color(0, 0, Math.max(20, 255 - Math.abs(step)));
/*     */         } 
/*     */       } 
/*     */     } 
/* 407 */     return colours;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void AddOptedInVillages(int size, Color[][] colours) {
/* 412 */     for (int x = 0; x < size; x++) {
/*     */       
/* 414 */       for (int y = 0; y < size; y++) {
/*     */ 
/*     */         
/* 417 */         VolaTile tt = Zones.getOrCreateTile(x, y, true);
/* 418 */         Village vill = (tt == null) ? null : tt.getVillage();
/* 419 */         if (vill != null && vill.isHighwayFound())
/*     */         {
/*     */           
/* 422 */           if (x == vill.getStartX() || x == vill.getEndX() || y == vill.getStartY() || y == vill.getEndY()) {
/* 423 */             colours[x][y] = new Color(255, 127, 39);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void AddMarkers(Item[] markers, Color[][] colours) {
/* 431 */     for (Item marker : markers) {
/*     */       Color colour;
/* 433 */       int id = marker.getTemplateId();
/* 434 */       switch (id) {
/*     */ 
/*     */         
/*     */         case 1112:
/* 438 */           if (marker.isOnSurface()) {
/*     */             
/* 440 */             addMarker(colours, marker, waystoneSurface);
/*     */             
/*     */             break;
/*     */           } 
/* 444 */           colour = colours[marker.getTileX()][marker.getTileY()];
/* 445 */           if (colour != waystoneSurface)
/*     */           {
/* 447 */             addMarker(colours, marker, waystoneCave);
/*     */           }
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 1114:
/* 454 */           colour = colours[marker.getTileX()][marker.getTileY()];
/* 455 */           if (colour == waystoneSurface || colour == waystoneCave);
/*     */ 
/*     */ 
/*     */           
/* 459 */           if (marker.isOnSurface()) {
/*     */ 
/*     */             
/* 462 */             switch (MethodsHighways.numberOfSetBits(marker.getAuxData())) {
/*     */               
/*     */               case 0:
/* 465 */                 addMarker(colours, marker, catseye0Surface);
/*     */                 break;
/*     */               case 1:
/* 468 */                 addMarker(colours, marker, catseye1Surface);
/*     */                 break;
/*     */             } 
/*     */             
/* 472 */             if (Routes.isCatseyeUsed(marker)) {
/* 473 */               addMarker(colours, marker, catseye3Surface); break;
/*     */             } 
/* 475 */             addMarker(colours, marker, catseye2Surface);
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */ 
/*     */           
/* 482 */           if (colour != catseye0Surface && colour != catseye2Surface && colour != catseye2Surface && colour != catseye3Surface) {
/*     */ 
/*     */             
/* 485 */             switch (MethodsHighways.numberOfSetBits(marker.getAuxData())) {
/*     */               
/*     */               case 0:
/* 488 */                 addMarker(colours, marker, catseye0Cave);
/*     */                 break;
/*     */               case 1:
/* 491 */                 addMarker(colours, marker, catseye1Cave);
/*     */                 break;
/*     */             } 
/*     */             
/* 495 */             if (Routes.isCatseyeUsed(marker)) {
/* 496 */               addMarker(colours, marker, catseye3Cave); break;
/*     */             } 
/* 498 */             addMarker(colours, marker, catseye2Cave);
/*     */           } 
/*     */           break;
/*     */       } 
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
/*     */   private static void addMarker(Color[][] colours, Item marker, Color colour) {
/* 514 */     int x = marker.getTileX();
/* 515 */     int y = marker.getTileY();
/* 516 */     colours[x][y] = colour;
/* 517 */     addColour(colours, x - 1, y, colour);
/* 518 */     addColour(colours, x, y - 1, colour);
/* 519 */     addColour(colours, x - 1, y - 1, colour);
/*     */ 
/*     */     
/* 522 */     MeshIO mesh = marker.isOnSurface() ? Server.surfaceMesh : Server.caveMesh;
/* 523 */     for (int xx = 0; xx <= 1; xx++) {
/*     */       
/* 525 */       for (int yy = 0; yy <= 1; yy++) {
/*     */ 
/*     */         
/* 528 */         if (xx != 0 && yy != 0) {
/*     */           
/* 530 */           int tileId = mesh.getTile(x + xx, y + yy);
/* 531 */           byte type = Tiles.decodeType(tileId);
/* 532 */           if (Tiles.isRoadType(type) || type == 201)
/*     */           {
/* 534 */             addColour(colours, x + xx, y + yy, colour);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addColour(Color[][] colours, int x, int y, Color colour) {
/* 544 */     if (colours[x][y] != waystoneSurface && colours[x][y] != waystoneCave) {
/* 545 */       colours[x][y] = colour;
/*     */     }
/*     */   }
/*     */   
/*     */   private static BufferedImage makeBufferedImage(Color[][] colors, int size) {
/* 550 */     BufferedImage bmi = new BufferedImage(size, size, 2);
/* 551 */     Graphics g = bmi.createGraphics();
/*     */     
/* 553 */     for (int x = 0; x < size; x++) {
/*     */       
/* 555 */       for (int y = 0; y < size; y++) {
/*     */         
/* 557 */         g.setColor(colors[x][y]);
/* 558 */         g.fillRect(x, y, 1, 1);
/*     */       } 
/*     */     } 
/* 561 */     return bmi;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveAsImg(MeshIO mesh) {
/* 566 */     long lStart = System.nanoTime();
/* 567 */     int size = mesh.getSize();
/* 568 */     logger.info("Size:" + size);
/* 569 */     logger.info("Data Size: " + mesh.data.length);
/* 570 */     byte[][] tiles = new byte[size][size];
/* 571 */     for (int x = 0; x < size; x++) {
/*     */       
/* 573 */       for (int y = 0; y < size; y++) {
/*     */         
/* 575 */         int tileId = mesh.getTile(x, y);
/* 576 */         byte type = Tiles.decodeType(tileId);
/* 577 */         tiles[x][y] = type;
/*     */       } 
/*     */     } 
/*     */     
/* 581 */     BufferedImage bmi = new BufferedImage(size, size, 2);
/* 582 */     Graphics g = bmi.createGraphics();
/*     */     
/* 584 */     for (int i = 0; i < size; i++) {
/*     */       
/* 586 */       for (int y = 0; y < size; y++) {
/*     */         
/* 588 */         boolean paint = true;
/* 589 */         if (tiles[i][y] == -34) {
/* 590 */           g.setColor(Color.darkGray);
/* 591 */         } else if (tiles[i][y] == -50) {
/* 592 */           g.setColor(Color.white);
/* 593 */         } else if (tiles[i][y] == -52) {
/* 594 */           g.setColor(Color.ORANGE);
/* 595 */         } else if (tiles[i][y] == -36) {
/* 596 */           g.setColor(Color.YELLOW);
/* 597 */         } else if (tiles[i][y] == -35) {
/* 598 */           g.setColor(Color.GREEN);
/* 599 */         } else if (tiles[i][y] == -32) {
/* 600 */           g.setColor(Color.CYAN);
/* 601 */         } else if (tiles[i][y] == -51) {
/* 602 */           g.setColor(Color.MAGENTA);
/* 603 */         } else if (tiles[i][y] == -33) {
/* 604 */           g.setColor(Color.PINK);
/* 605 */         } else if (tiles[i][y] == -30) {
/* 606 */           g.setColor(Color.BLUE);
/* 607 */         } else if (tiles[i][y] == -31) {
/* 608 */           g.setColor(Color.RED);
/*     */         } else {
/* 610 */           paint = false;
/*     */         } 
/* 612 */         if (paint) {
/* 613 */           g.fillRect(i, y, 1, 1);
/*     */         }
/*     */       } 
/*     */     } 
/* 617 */     File f = new File("Ore.png");
/*     */     
/*     */     try {
/* 620 */       ImageIO.write(bmi, "png", f);
/*     */     }
/* 622 */     catch (IOException e) {
/*     */       
/* 624 */       logger.log(Level.WARNING, "Failed to produce ore.png", e);
/*     */     }
/*     */     finally {
/*     */       
/* 628 */       long lElapsedTime = System.nanoTime() - lStart;
/* 629 */       logger.info("Saved Mesh to '" + f.getAbsoluteFile() + "', that took " + ((float)lElapsedTime / 1000000.0F) + ", millis.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\ZonesUtility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */