/*      */ package com.wurmonline.server.meshgen;
/*      */ 
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class IslandAdder
/*      */ {
/*   32 */   private static final Logger logger = Logger.getLogger(IslandAdder.class.getName());
/*      */   
/*      */   private final MeshIO topLayer;
/*      */   
/*      */   private final MeshIO rockLayer;
/*      */   
/*   38 */   private final Random random = new Random();
/*      */   
/*   40 */   private final Map<Integer, Set<Integer>> specials = new HashMap<>();
/*      */   
/*      */   public static final byte north = 0;
/*      */   
/*      */   public static final byte northeast = 1;
/*      */   
/*      */   public static final byte east = 2;
/*      */   
/*      */   public static final byte southeast = 3;
/*      */   
/*      */   public static final byte south = 4;
/*      */   
/*      */   public static final byte southwest = 5;
/*      */   
/*      */   public static final byte west = 6;
/*      */   public static final byte northwest = 7;
/*      */   
/*      */   public IslandAdder() throws IOException {
/*   58 */     this(MeshIO.open("top_layer.map"), MeshIO.open("rock_layer.map"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IslandAdder(String directoryName) throws IOException {
/*   69 */     this(MeshIO.open(directoryName + File.separatorChar + "top_layer.map"), MeshIO.open(directoryName + File.separatorChar + "rock_layer.map"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IslandAdder(MeshIO aTopLayer, MeshIO aRockLayer) {
/*   82 */     this.topLayer = aTopLayer;
/*   83 */     this.rockLayer = aRockLayer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addIslands(int maxSize) {
/*   88 */     int maxw = maxSize / 4;
/*   89 */     int minw = maxSize / 8;
/*   90 */     for (int i = maxw; i >= minw; i--) {
/*      */       
/*   92 */       for (int j = 0; j < 2; j++) {
/*      */         
/*   94 */         int width = i;
/*   95 */         int height = width;
/*   96 */         int x = this.random.nextInt(this.topLayer.getSize() - width - 128) + 64;
/*      */         
/*   98 */         int y = this.random.nextInt(maxSize - width - 128) + 64;
/*   99 */         Map<Integer, Set<Integer>> changes = maybeAddIsland(x, y, x + width, y + height, false);
/*  100 */         if (changes != null)
/*      */         {
/*  102 */           logger.info("Added island size " + i + " @ " + (x + width / 2) + ", " + (y + height / 2));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> addOneIsland(int maxSizeX, int maxSizeY) {
/*  110 */     for (int i = 800; i >= 300; i--) {
/*      */       
/*  112 */       for (int j = 0; j < 2; j++) {
/*      */         
/*  114 */         int width = i;
/*  115 */         int height = width;
/*  116 */         int x = this.random.nextInt(maxSizeX - width - 128) + 64;
/*  117 */         int y = this.random.nextInt(maxSizeY - width - 128) + 64;
/*      */         
/*  119 */         Map<Integer, Set<Integer>> changes = maybeAddIsland(x, y, x + width, y + height, false);
/*  120 */         if (changes != null) {
/*      */           
/*  122 */           logger.info("Added island size " + i + " @ " + (x + width / 2) + ", " + (y + height / 2));
/*  123 */           return changes;
/*      */         } 
/*      */       } 
/*      */     } 
/*  127 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Map<Integer, Set<Integer>> forceIsland(int maxSizeX, int maxSizeY, int tilex, int tiley) {
/*  132 */     Map<Integer, Set<Integer>> changes = maybeAddIsland(tilex, tiley, tilex + maxSizeX, tiley + maxSizeY, true);
/*  133 */     if (changes != null) {
/*      */       
/*  135 */       logger.info("Added island size " + maxSizeX + "," + maxSizeY + " @ " + (tilex + maxSizeX / 2) + ", " + (tilex + maxSizeY / 2));
/*      */       
/*  137 */       return changes;
/*      */     } 
/*  139 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> addToSpecials(int x, int y) {
/*  144 */     Set<Integer> s = this.specials.get(Integer.valueOf(x));
/*  145 */     if (s == null)
/*  146 */       s = new HashSet<>(); 
/*  147 */     if (!s.contains(Integer.valueOf(y)))
/*  148 */       s.add(Integer.valueOf(y)); 
/*  149 */     this.specials.put(Integer.valueOf(x), s);
/*  150 */     return this.specials;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> addToChanges(Map<Integer, Set<Integer>> changes, int x, int y) {
/*  155 */     Set<Integer> s = changes.get(Integer.valueOf(x));
/*  156 */     if (s == null)
/*  157 */       s = new HashSet<>(); 
/*  158 */     if (!s.contains(Integer.valueOf(y)))
/*  159 */       s.add(Integer.valueOf(y)); 
/*  160 */     changes.put(Integer.valueOf(x), s);
/*  161 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createMultiPlateau(int x0, int y0, int x1, int y1, int iterations, int startHeight) {
/*  167 */     int lastx0 = x0;
/*  168 */     int lasty0 = y0;
/*  169 */     int lastx1 = x1;
/*  170 */     int lasty1 = y1;
/*  171 */     Map<Integer, Set<Integer>> changes = createPlateau(x0, y0, x1, y1, startHeight);
/*  172 */     for (int i = 0; i < iterations; i++) {
/*      */       
/*  174 */       int modx = (lastx1 - lastx0) / (1 + this.random.nextInt(4));
/*  175 */       int mody = (lasty1 - lasty0) / (1 + this.random.nextInt(4));
/*  176 */       if (this.random.nextBoolean())
/*  177 */         modx = -modx; 
/*  178 */       if (this.random.nextBoolean())
/*  179 */         mody = -mody; 
/*  180 */       Map<Integer, Set<Integer>> changes2 = createPlateau(lastx0 + modx, lasty0 + mody, lastx1 + modx, lasty1 + mody, startHeight);
/*      */       
/*  182 */       for (Integer inte : changes2.keySet()) {
/*      */         
/*  184 */         Set<Integer> vals = changes2.get(inte);
/*  185 */         if (!changes.containsKey(inte)) {
/*  186 */           changes.put(inte, vals);
/*      */           continue;
/*      */         } 
/*  189 */         Set<Integer> oldvals = changes.get(inte);
/*  190 */         for (Integer newint : vals) {
/*      */           
/*  192 */           if (!oldvals.contains(newint)) {
/*  193 */             oldvals.add(newint);
/*      */           }
/*      */         } 
/*      */       } 
/*  197 */       if (this.random.nextBoolean()) {
/*      */         
/*  199 */         lastx0 += modx;
/*  200 */         lasty0 += mody;
/*  201 */         lastx1 += modx;
/*  202 */         lasty1 += mody;
/*      */       } 
/*      */     } 
/*  205 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createPlateau(int x0, int y0, int x1, int y1, int startHeight) {
/*  211 */     int xm = (x1 + x0) / 2;
/*  212 */     int ym = (y1 + y0) / 2;
/*  213 */     double dirOffs = this.random.nextDouble() * Math.PI * 2.0D;
/*  214 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*      */     
/*  216 */     int branchCount = this.random.nextInt(7) + 3;
/*      */     
/*  218 */     float[] branches = new float[branchCount];
/*  219 */     for (int i = 0; i < branchCount; i++)
/*      */     {
/*  221 */       branches[i] = this.random.nextFloat() * 0.25F + 0.75F;
/*      */     }
/*      */     
/*  224 */     ImprovedNoise noise = new ImprovedNoise(this.random.nextLong());
/*      */     
/*  226 */     int highestHeight = -32768; int x;
/*  227 */     for (x = x0; x < x1; x++) {
/*      */       
/*  229 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  230 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  232 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  233 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  235 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  236 */         while (dir < 0.0D)
/*  237 */           dir++; 
/*  238 */         while (dir >= 1.0D) {
/*  239 */           dir--;
/*      */         }
/*  241 */         int branch = (int)(dir * branchCount);
/*  242 */         float step = (float)dir * branchCount - branch;
/*  243 */         float last = branches[branch];
/*  244 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  246 */         float pow = last + (next - last) * step;
/*  247 */         double d = od;
/*  248 */         d /= pow;
/*      */         
/*  250 */         if (d < 1.0D) {
/*      */           
/*  252 */           d *= d;
/*  253 */           d *= d;
/*  254 */           d = 1.0D - d;
/*      */           
/*  256 */           int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  257 */           int height = Tiles.decodeHeight(oldTile);
/*  258 */           float n = (float)(noise.perlinNoise(x, y) * 64.0D) + 100.0F;
/*  259 */           n *= 2.0F;
/*  260 */           int hh = (int)(height + (n - height) * d);
/*  261 */           if (hh > highestHeight) {
/*  262 */             highestHeight = hh;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  267 */     highestHeight += startHeight + this.random.nextInt(startHeight);
/*      */     
/*  269 */     for (x = x0; x < x1; x++) {
/*      */       
/*  271 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  272 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  274 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  275 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  277 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  278 */         while (dir < 0.0D)
/*  279 */           dir++; 
/*  280 */         while (dir >= 1.0D) {
/*  281 */           dir--;
/*      */         }
/*  283 */         int branch = (int)(dir * branchCount);
/*  284 */         float step = (float)dir * branchCount - branch;
/*  285 */         float last = branches[branch];
/*  286 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  288 */         float pow = last + (next - last) * step;
/*  289 */         double d = od;
/*  290 */         d /= pow;
/*      */         
/*  292 */         if (d < 1.0D)
/*      */         {
/*  294 */           if (d < 0.30000001192092896D) {
/*      */             
/*  296 */             this.topLayer.setTile(x, y, Tiles.encode((short)(int)(highestHeight * 0.7D), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  297 */             this.rockLayer.setTile(x, y, Tiles.encode((short)(int)(highestHeight * 0.7D), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  298 */             changes = addToChanges(changes, x, y);
/*      */           }
/*      */           else {
/*      */             
/*  302 */             int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  303 */             short height = Tiles.decodeHeight(oldTile);
/*  304 */             short newHeight = (short)(int)(highestHeight * (1.0D - d) * pow);
/*  305 */             if (newHeight > height) {
/*      */               
/*  307 */               this.topLayer.setTile(x, y, Tiles.encode(newHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  308 */               this.rockLayer.setTile(x, y, Tiles.encode(newHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  309 */               changes = addToChanges(changes, x, y);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  316 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createRavine(int startX, int startY, int length, int direction) {
/*      */     int modx, mody;
/*  324 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*  325 */     switch (direction) {
/*      */       
/*      */       case 0:
/*  328 */         modx = 0;
/*  329 */         mody = -1;
/*      */         break;
/*      */       case 1:
/*  332 */         mody = -1;
/*  333 */         modx = 1;
/*      */         break;
/*      */       case 2:
/*  336 */         modx = 1;
/*  337 */         mody = 0;
/*      */         break;
/*      */       case 3:
/*  340 */         mody = 1;
/*  341 */         modx = 1;
/*      */         break;
/*      */       case 4:
/*  344 */         modx = 0;
/*  345 */         mody = 1;
/*      */         break;
/*      */       case 5:
/*  348 */         mody = 1;
/*  349 */         modx = -1;
/*      */         break;
/*      */       case 6:
/*  352 */         modx = -1;
/*  353 */         mody = 0;
/*      */         break;
/*      */       case 7:
/*  356 */         modx = -1;
/*  357 */         mody = -1;
/*      */         break;
/*      */       default:
/*  360 */         modx = 0;
/*  361 */         mody = 0;
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  367 */     int width = 1;
/*  368 */     int maxWidth = Math.max(4, length / 10);
/*  369 */     float maximumLengthDepthPoint = (1 + length / 2);
/*  370 */     float maximumWidthDepthPoint = (1 + maxWidth / 2);
/*      */     
/*  372 */     float maxDepth = length / 3.0F;
/*  373 */     int currX = startX;
/*  374 */     int currY = startY;
/*  375 */     logger.log(Level.INFO, "Max depth=" + maxDepth + " length=" + length + ", " + maximumLengthDepthPoint + "," + maximumWidthDepthPoint);
/*      */     
/*  377 */     for (int dist = 0; dist < length; dist++) {
/*      */       
/*  379 */       float currLengthDepth = 1.0F - Math.abs(maximumLengthDepthPoint - dist) / maximumLengthDepthPoint;
/*  380 */       for (int w = 0; w <= width; w++) {
/*      */         
/*  382 */         int tx = currX + modx * w;
/*  383 */         int ty = currY + mody * w;
/*  384 */         Set<Integer> yset = changes.get(Integer.valueOf(tx));
/*  385 */         if (yset == null || !yset.contains(Integer.valueOf(ty))) {
/*      */           
/*      */           try {
/*      */             
/*  389 */             int oldTile = this.topLayer.data[tx | ty << this.topLayer.getSizeLevel()];
/*  390 */             float height = Tiles.decodeHeightAsFloat(oldTile);
/*      */             
/*  392 */             int nt = this.topLayer.data[tx | ty - 1 << this.topLayer.getSizeLevel()];
/*  393 */             float nth = Tiles.decodeHeightAsFloat(nt);
/*      */             
/*  395 */             int st = this.topLayer.data[tx | ty + 1 << this.topLayer.getSizeLevel()];
/*  396 */             float sth = Tiles.decodeHeightAsFloat(st);
/*      */             
/*  398 */             int et = this.topLayer.data[tx + 1 | ty << this.topLayer.getSizeLevel()];
/*  399 */             float eth = Tiles.decodeHeightAsFloat(et);
/*      */             
/*  401 */             int wt = this.topLayer.data[tx - 1 | ty << this.topLayer.getSizeLevel()];
/*  402 */             float wth = Tiles.decodeHeightAsFloat(wt);
/*      */             
/*  404 */             float minPrevHeight = Math.min(nth, Math.min(sth, Math.min(eth, wth)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  410 */             float change = currLengthDepth * maxDepth;
/*  411 */             if (change < height - minPrevHeight) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  416 */               change = Math.min(change, height - minPrevHeight - 3.0F);
/*      */             }
/*  418 */             else if (change > height - minPrevHeight) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  423 */               change = Math.min(change, height - minPrevHeight + 3.0F);
/*      */             } 
/*  425 */             if (change != 0.0F)
/*      */             {
/*  427 */               float newDepth = height - change;
/*      */               
/*  429 */               if (Tiles.decodeHeightAsFloat(this.rockLayer.data[tx | ty << this.rockLayer.getSizeLevel()]) >= newDepth) {
/*      */                 
/*  431 */                 logger.log(Level.INFO, "Setting rock at " + tx + "," + ty + " to " + newDepth);
/*  432 */                 this.topLayer.setTile(tx, ty, Tiles.encode(newDepth, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  433 */                 this.rockLayer.setTile(tx, ty, Tiles.encode(newDepth, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */               }
/*      */               else {
/*      */                 
/*  437 */                 logger.log(Level.INFO, "Rock at " + tx + "," + ty + " is " + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*  444 */                     Tiles.decodeHeightAsFloat(this.rockLayer.data[tx | ty << this.rockLayer.getSizeLevel()]) + " so setting to " + newDepth);
/*      */                 
/*  446 */                 if (this.random.nextInt(5) == 0) {
/*  447 */                   this.topLayer.setTile(tx, ty, 
/*  448 */                       Tiles.encode(newDepth, Tiles.decodeType(oldTile), Tiles.decodeData(oldTile)));
/*      */                 } else {
/*  450 */                   this.topLayer.setTile(tx, ty, Tiles.encode(newDepth, Tiles.Tile.TILE_DIRT.id, (byte)0));
/*      */                 } 
/*  452 */               }  changes = addToChanges(changes, tx, ty);
/*      */             
/*      */             }
/*      */ 
/*      */           
/*      */           }
/*  458 */           catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  464 */       int rand = this.random.nextInt(20);
/*      */ 
/*      */       
/*  467 */       if (modx <= 0 && rand == 0) {
/*  468 */         currX++;
/*  469 */       } else if (modx >= 0 && rand == 1) {
/*  470 */         currX--;
/*  471 */       }  if (mody <= 0 && rand == 2) {
/*  472 */         currY++;
/*  473 */       } else if (mody >= 0 && rand == 3) {
/*  474 */         currY--;
/*  475 */       }  int wmod = 0;
/*  476 */       if (rand == 4) {
/*  477 */         wmod = 1;
/*  478 */       } else if (rand == 5) {
/*  479 */         wmod = -1;
/*      */       } 
/*  481 */       currX += modx;
/*  482 */       currY += mody;
/*  483 */       width = (int)Math.max(4.0F, wmod + maxWidth * currLengthDepth * 2.0F);
/*      */     } 
/*  485 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Integer, Set<Integer>> createIndentationXxx(int x0, int y0, int x1, int y1, byte newTopLayerTileId, byte newTopLayerData) {
/*  491 */     int xm = (x1 + x0) / 2;
/*  492 */     int ym = (y1 + y0) / 2;
/*  493 */     double dirOffs = this.random.nextDouble() * Math.PI * 2.0D;
/*  494 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*      */     
/*  496 */     int branchCount = this.random.nextInt(7) + 3;
/*      */     
/*  498 */     float[] branches = new float[branchCount];
/*  499 */     for (int i = 0; i < branchCount; i++)
/*      */     {
/*  501 */       branches[i] = this.random.nextFloat() * 0.25F + 0.75F;
/*      */     }
/*      */     
/*  504 */     ImprovedNoise noise = new ImprovedNoise(this.random.nextLong());
/*      */     
/*  506 */     int lowestHeight = 32767; int x;
/*  507 */     for (x = x0; x < x1; x++) {
/*      */       
/*  509 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  510 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  512 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  513 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  515 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  516 */         while (dir < 0.0D)
/*  517 */           dir++; 
/*  518 */         while (dir >= 1.0D) {
/*  519 */           dir--;
/*      */         }
/*  521 */         int branch = (int)(dir * branchCount);
/*  522 */         float step = (float)dir * branchCount - branch;
/*  523 */         float last = branches[branch];
/*  524 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  526 */         float pow = last + (next - last) * step;
/*  527 */         double d = od;
/*  528 */         d /= pow;
/*      */         
/*  530 */         if (d < 1.0D) {
/*      */           
/*  532 */           d *= d;
/*  533 */           d *= d;
/*  534 */           d = 1.0D - d;
/*      */           
/*  536 */           int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  537 */           int height = Tiles.decodeHeight(oldTile);
/*  538 */           float n = (float)(noise.perlinNoise(x, y) * 64.0D) + 100.0F;
/*  539 */           n *= 2.0F;
/*  540 */           int hh = (int)(height + (n - height) * d);
/*  541 */           if (hh < lowestHeight) {
/*  542 */             lowestHeight = hh;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  548 */     for (x = x0; x < x1; x++) {
/*      */       
/*  550 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  551 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  553 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  554 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  556 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  557 */         while (dir < 0.0D)
/*  558 */           dir++; 
/*  559 */         while (dir >= 1.0D) {
/*  560 */           dir--;
/*      */         }
/*  562 */         int branch = (int)(dir * branchCount);
/*  563 */         float step = (float)dir * branchCount - branch;
/*  564 */         float last = branches[branch];
/*  565 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  567 */         float pow = last + (next - last) * step;
/*  568 */         double d = od;
/*  569 */         d /= pow;
/*      */         
/*  571 */         if (d < 1.0D) {
/*      */           
/*  573 */           this.topLayer.setTile(x, y, Tiles.encode((short)lowestHeight, newTopLayerTileId, newTopLayerData));
/*  574 */           this.rockLayer.setTile(x, y, Tiles.encode((short)lowestHeight, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  575 */           changes = addToChanges(changes, x, y);
/*      */ 
/*      */         
/*      */         }
/*  579 */         else if (this.random.nextInt(3) == 0) {
/*      */           
/*  581 */           this.topLayer.setTile(x, y, Tiles.encode(
/*  582 */                 Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */           
/*  584 */           this.rockLayer.setTile(x, y, Tiles.encode(
/*  585 */                 Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */           
/*  587 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  593 */     return changes;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createRockIndentation(int x0, int y0, int x1, int y1) {
/*  598 */     return createIndentationXxx(x0, y0, x1, y1, Tiles.Tile.TILE_ROCK.id, (byte)0);
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createVolcano(int x0, int y0, int x1, int y1) {
/*  603 */     Map<Integer, Set<Integer>> changes = createIndentationXxx(x0, y0, x1, y1, Tiles.Tile.TILE_LAVA.id, (byte)-1);
/*      */     
/*  605 */     for (int x = x0; x < x1; x++) {
/*      */       
/*  607 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  609 */         int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  610 */         byte oldType = Tiles.decodeType(oldTile);
/*  611 */         int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  612 */         if (oldType == Tiles.Tile.TILE_LAVA.id)
/*      */         {
/*  614 */           if (!isTopLayerFlat(x, y)) {
/*      */ 
/*      */ 
/*      */             
/*  618 */             this.topLayer.setTile(x, y, Tiles.encode((short)height, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */             
/*  620 */             this.rockLayer.setTile(x, y, Tiles.encode((short)height, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  621 */             changes = addToChanges(changes, x, y);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  627 */     return changes;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTopLayerFlat(int tilex, int tiley) {
/*  632 */     int heightChecked = -32768;
/*  633 */     for (int x = 0; x <= 1; x++) {
/*      */       
/*  635 */       for (int y = 0; y <= 1; y++) {
/*      */         
/*  637 */         short ch = Tiles.decodeHeight(this.topLayer.getTile(tilex + x, tiley + y));
/*  638 */         if (heightChecked == -32768)
/*  639 */           heightChecked = ch; 
/*  640 */         if (ch != heightChecked)
/*  641 */           return false; 
/*      */       } 
/*      */     } 
/*  644 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> createCrater(int x0, int y0, int x1, int y1) {
/*  649 */     int xm = (x1 + x0) / 2;
/*  650 */     int ym = (y1 + y0) / 2;
/*  651 */     double dirOffs = this.random.nextDouble() * Math.PI * 2.0D;
/*  652 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*      */     
/*  654 */     int branchCount = this.random.nextInt(7) + 3;
/*      */     
/*  656 */     float[] branches = new float[branchCount];
/*  657 */     for (int i = 0; i < branchCount; i++)
/*      */     {
/*  659 */       branches[i] = this.random.nextFloat() * 0.25F + 0.75F;
/*      */     }
/*  661 */     ImprovedNoise noise = new ImprovedNoise(this.random.nextLong());
/*      */     int x;
/*  663 */     for (x = x0; x < x1; x++) {
/*      */       
/*  665 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  666 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  668 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  669 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  671 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  672 */         while (dir < 0.0D)
/*  673 */           dir++; 
/*  674 */         while (dir >= 1.0D) {
/*  675 */           dir--;
/*      */         }
/*  677 */         int branch = (int)(dir * branchCount);
/*  678 */         float step = (float)dir * branchCount - branch;
/*  679 */         float last = branches[branch];
/*  680 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  682 */         float pow = last + (next - last) * step;
/*  683 */         double d = od;
/*  684 */         d /= pow;
/*      */         
/*  686 */         int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  687 */         byte oldType = Tiles.decodeType(oldTile);
/*  688 */         if (d < 1.0D) {
/*      */           
/*  690 */           d *= d;
/*  691 */           d *= d;
/*  692 */           d = 1.0D - d;
/*      */           
/*  694 */           int height = Tiles.decodeHeight(oldTile);
/*  695 */           float n = (float)noise.perlinNoise(x, y) * 5.0F;
/*  696 */           n *= 2.0F;
/*  697 */           int hh = (int)(height + (n - 1.0F) * d * 50.0D);
/*  698 */           byte type = Tiles.Tile.TILE_DIRT.id;
/*  699 */           int diff = hh - height;
/*  700 */           if (diff < 0 && (oldType == Tiles.Tile.TILE_ROCK.id || oldType == Tiles.Tile.TILE_CLIFF.id)) {
/*  701 */             type = oldType;
/*      */           } else {
/*      */             
/*  704 */             if (hh <= 0)
/*      */             {
/*  706 */               if (this.random.nextInt(5) == 0)
/*      */               {
/*  708 */                 type = Tiles.Tile.TILE_SAND.id;
/*      */               }
/*      */             }
/*  711 */             if (hh > 5)
/*      */             {
/*  713 */               if (this.random.nextInt(100) == 0)
/*      */               {
/*  715 */                 type = Tiles.Tile.TILE_GRASS.id;
/*      */               }
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  721 */           this.topLayer.setTile(x, y, Tiles.encode((short)hh, type, (byte)0));
/*  722 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*  726 */     for (x = x0; x < x1; x++) {
/*      */       
/*  728 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  729 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  731 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  732 */         double d = Math.sqrt(xd * xd + yd * yd);
/*  733 */         double od = d * (x1 - x0);
/*      */         
/*  735 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  736 */         while (dir < 0.0D)
/*  737 */           dir++; 
/*  738 */         while (dir >= 1.0D) {
/*  739 */           dir--;
/*      */         }
/*  741 */         int branch = (int)(dir * branchCount);
/*  742 */         float step = (float)dir * branchCount - branch;
/*  743 */         float last = branches[branch];
/*  744 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  746 */         float pow = last + (next - last) * step;
/*  747 */         d /= pow;
/*      */         
/*  749 */         int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  750 */         int dd = 0;
/*      */         
/*  752 */         float hh = height / 10.0F - 8.0F;
/*  753 */         d = 1.0D - d;
/*      */         
/*  755 */         if (d < 0.0D)
/*  756 */           d = 0.0D; 
/*  757 */         d = Math.sin(d * Math.PI) * 2.0D - 1.0D;
/*  758 */         if (d < 0.0D) {
/*  759 */           d = 0.0D;
/*      */         }
/*  761 */         float n = (float)noise.perlinNoise(x / 2.0D, y / 2.0D);
/*  762 */         if (n > 0.5F)
/*  763 */           n -= (n - 0.5F) * 2.0F; 
/*  764 */         n /= 0.5F;
/*  765 */         if (n < 0.0F)
/*  766 */           n = 0.0F; 
/*  767 */         hh = (float)(hh + (n * (x1 - x0) / 8.0F) * d);
/*      */ 
/*      */ 
/*      */         
/*  771 */         int oldTile = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  772 */         byte oldType = Tiles.decodeType(oldTile);
/*  773 */         if (oldType != Tiles.Tile.TILE_ROCK.id && oldType != Tiles.Tile.TILE_CLIFF.id) {
/*      */           
/*  775 */           float ddd = (float)od / 16.0F;
/*  776 */           if (ddd < 1.0F)
/*      */           {
/*  778 */             ddd = ddd * 2.0F - 1.0F;
/*  779 */             if (ddd > 1.0F)
/*  780 */               ddd = 1.0F; 
/*  781 */             if (ddd < 0.0F)
/*  782 */               ddd = 0.0F; 
/*  783 */             dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  784 */             float hh1 = Tiles.decodeHeightAsFloat(dd);
/*  785 */             hh = Tiles.decodeHeightAsFloat(this.rockLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  786 */             hh = hh1 - Math.min(5.0F, (hh1 - hh) * ddd);
/*      */             
/*  788 */             this.topLayer.setTile(x, y, Tiles.encode(hh, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  795 */             changes = addToChanges(changes, x, y);
/*      */           }
/*      */           else
/*      */           {
/*  799 */             dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  800 */             hh = Tiles.decodeHeightAsFloat(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  801 */             hh = hh * 0.5F + ((int)hh / 2 * 2) * 0.5F;
/*  802 */             if (hh > 0.0F) {
/*      */               
/*  804 */               hh += 0.07F;
/*      */             }
/*      */             else {
/*      */               
/*  808 */               hh -= 0.07F;
/*      */             } 
/*      */             
/*  811 */             this.topLayer.setTile(x, y, Tiles.encode(hh, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/*  812 */             if (hh < Tiles.decodeHeightAsFloat(this.rockLayer.data[x | y << this.topLayer.getSizeLevel()])) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  818 */               this.topLayer.setTile(x, y, Tiles.encode(hh, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  819 */               this.rockLayer.setTile(x, y, Tiles.encode(hh, Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  824 */               this.topLayer.setTile(x, y, Tiles.encode(hh, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/*      */             } 
/*  826 */             changes = addToChanges(changes, x, y);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  831 */           dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  832 */           this.topLayer.setTile(x, y, Tiles.encode(hh, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/*  833 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  839 */     for (x = x0; x < x1; x++) {
/*      */ 
/*      */       
/*  842 */       for (int y = y0; y < y1; y++) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  847 */         boolean rock = true;
/*      */         
/*  849 */         for (int xx = 0; xx < 2; xx++) {
/*      */           
/*  851 */           for (int yy = 0; yy < 2; yy++) {
/*      */             
/*  853 */             int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  854 */             int groundHeight = Tiles.decodeHeight(this.rockLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*  855 */             if (groundHeight < height) {
/*      */               
/*  857 */               rock = false;
/*      */             }
/*      */             else {
/*      */               
/*  861 */               int dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  862 */               this.topLayer.setTile(x, y, Tiles.encode((short)groundHeight, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/*  863 */               changes = addToChanges(changes, x, y);
/*      */             } 
/*      */           } 
/*      */         } 
/*  867 */         if (rock) {
/*      */           
/*  869 */           int dd = this.topLayer.data[x | y << this.topLayer.getSizeLevel()];
/*  870 */           this.topLayer.setTile(x, y, Tiles.encode(Tiles.decodeHeight(dd), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*  871 */           changes = addToChanges(changes, x, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  876 */     return changes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Integer, Set<Integer>> maybeAddIsland(int x0, int y0, int x1, int y1, boolean forced) {
/*  882 */     int xm = (x1 + x0) / 2;
/*  883 */     int ym = (y1 + y0) / 2;
/*  884 */     double dirOffs = this.random.nextDouble() * Math.PI * 2.0D;
/*      */     
/*  886 */     for (int x = x0; x < x1; x++) {
/*      */       
/*  888 */       double xd = (x - xm) * 2.0D / (x1 - x0);
/*  889 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  891 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  892 */         double d = Math.sqrt(xd * xd + yd * yd);
/*  893 */         if (d < 1.0D) {
/*      */           
/*  895 */           int height = Tiles.decodeHeight(this.topLayer.data[x | y << this.topLayer.getSizeLevel()]);
/*      */           
/*  897 */           if (height > -5 && !forced)
/*      */           {
/*  899 */             return null;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  904 */     Map<Integer, Set<Integer>> changes = new HashMap<>();
/*  905 */     int branchCount = this.random.nextInt(7) + 3;
/*      */     
/*  907 */     float[] branches = new float[branchCount];
/*  908 */     for (int i = 0; i < branchCount; i++)
/*      */     {
/*  910 */       branches[i] = this.random.nextFloat() * 0.25F + 0.75F;
/*      */     }
/*      */     
/*  913 */     ImprovedNoise noise = new ImprovedNoise(this.random.nextLong());
/*      */     int j;
/*  915 */     for (j = x0; j < x1; j++) {
/*      */       
/*  917 */       double xd = (j - xm) * 2.0D / (x1 - x0);
/*  918 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  920 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  921 */         double od = Math.sqrt(xd * xd + yd * yd);
/*      */         
/*  923 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  924 */         while (dir < 0.0D)
/*  925 */           dir++; 
/*  926 */         while (dir >= 1.0D) {
/*  927 */           dir--;
/*      */         }
/*  929 */         int branch = (int)(dir * branchCount);
/*  930 */         float step = (float)dir * branchCount - branch;
/*  931 */         float last = branches[branch];
/*  932 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  934 */         float pow = last + (next - last) * step;
/*  935 */         double d = od;
/*  936 */         d /= pow;
/*      */         
/*  938 */         if (d < 1.0D) {
/*      */ 
/*      */           
/*  941 */           d *= d;
/*  942 */           d *= d;
/*  943 */           d = 1.0D - d;
/*      */           
/*  945 */           int height = Tiles.decodeHeight(this.topLayer.data[j | y << this.topLayer.getSizeLevel()]);
/*  946 */           float n = (float)(noise.perlinNoise(j, y) * 64.0D) + 100.0F;
/*  947 */           n *= 2.0F;
/*  948 */           int hh = (int)(height + (n - height) * d);
/*      */           
/*  950 */           byte type = Tiles.Tile.TILE_DIRT.id;
/*  951 */           if (hh > 5)
/*      */           {
/*  953 */             if (this.random.nextInt(100) == 0)
/*      */             {
/*  955 */               type = Tiles.Tile.TILE_GRASS.id;
/*      */             }
/*      */           }
/*  958 */           if (hh > 0) {
/*      */             
/*  960 */             hh = (int)(hh + 0.07F);
/*      */           }
/*      */           else {
/*      */             
/*  964 */             hh = (int)(hh - 0.07F);
/*      */           } 
/*  966 */           this.topLayer.setTile(j, y, Tiles.encode((short)hh, type, (byte)0));
/*  967 */           changes = addToChanges(changes, j, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  972 */     for (j = x0; j < x1; j++) {
/*      */       
/*  974 */       double xd = (j - xm) * 2.0D / (x1 - x0);
/*  975 */       for (int y = y0; y < y1; y++) {
/*      */         
/*  977 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/*  978 */         double d = Math.sqrt(xd * xd + yd * yd);
/*  979 */         double od = d * (x1 - x0);
/*      */         
/*  981 */         double dir = (Math.atan2(yd, xd) + Math.PI) / 6.283185307179586D + dirOffs;
/*  982 */         while (dir < 0.0D)
/*  983 */           dir++; 
/*  984 */         while (dir >= 1.0D) {
/*  985 */           dir--;
/*      */         }
/*  987 */         int branch = (int)(dir * branchCount);
/*  988 */         float step = (float)dir * branchCount - branch;
/*  989 */         float last = branches[branch];
/*  990 */         float next = branches[(branch + 1) % branchCount];
/*      */         
/*  992 */         float pow = last + (next - last) * step;
/*  993 */         d /= pow;
/*      */         
/*  995 */         int height = Tiles.decodeHeight(this.topLayer.data[j | y << this.topLayer.getSizeLevel()]);
/*  996 */         int dd = this.rockLayer.data[j | y << this.topLayer.getSizeLevel()];
/*      */         
/*  998 */         float hh = height / 10.0F - 8.0F;
/*      */ 
/*      */         
/* 1001 */         d = 1.0D - d;
/*      */         
/* 1003 */         if (d < 0.0D)
/* 1004 */           d = 0.0D; 
/* 1005 */         d = Math.sin(d * Math.PI) * 2.0D - 1.0D;
/* 1006 */         if (d < 0.0D) {
/* 1007 */           d = 0.0D;
/*      */         }
/* 1009 */         float n = (float)noise.perlinNoise(j / 2.0D, y / 2.0D);
/* 1010 */         if (n > 0.5F)
/* 1011 */           n -= (n - 0.5F) * 2.0F; 
/* 1012 */         n /= 0.5F;
/* 1013 */         if (n < 0.0F)
/* 1014 */           n = 0.0F; 
/* 1015 */         hh = (float)(hh + (n * (x1 - x0) / 8.0F) * d);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1021 */         this.rockLayer.setTile(j, y, Tiles.encode(hh, Tiles.decodeType(dd), Tiles.decodeData(dd)));
/* 1022 */         changes = addToChanges(changes, j, y);
/* 1023 */         float ddd = (float)od / 16.0F;
/* 1024 */         if (ddd < 1.0F) {
/*      */           
/* 1026 */           ddd = ddd * 2.0F - 1.0F;
/* 1027 */           if (ddd > 1.0F)
/* 1028 */             ddd = 1.0F; 
/* 1029 */           if (ddd < 0.0F)
/* 1030 */             ddd = 0.0F; 
/* 1031 */           dd = this.topLayer.data[j | y << this.topLayer.getSizeLevel()];
/* 1032 */           float hh1 = Tiles.decodeHeightAsFloat(this.topLayer.data[j | y << this.topLayer.getSizeLevel()]);
/* 1033 */           hh = Tiles.decodeHeightAsFloat(this.rockLayer.data[j | y << this.topLayer.getSizeLevel()]);
/*      */           
/* 1035 */           hh += (hh1 - hh) * ddd;
/* 1036 */           this.topLayer.setTile(j, y, Tiles.encode(hh, Tiles.decodeType(dd), 
/* 1037 */                 Tiles.decodeData(dd)));
/* 1038 */           changes = addToChanges(changes, j, y);
/*      */         }
/*      */         else {
/*      */           
/* 1042 */           dd = this.topLayer.data[j | y << this.topLayer.getSizeLevel()];
/* 1043 */           hh = Tiles.decodeHeightAsFloat(this.topLayer.data[j | y << this.topLayer.getSizeLevel()]);
/* 1044 */           hh = hh * 0.5F + ((int)hh / 2 * 2) * 0.5F;
/* 1045 */           if (hh > 0.0F) {
/*      */             
/* 1047 */             hh += 0.07F;
/*      */           }
/*      */           else {
/*      */             
/* 1051 */             hh -= 0.07F;
/*      */           } 
/*      */           
/* 1054 */           this.topLayer.setTile(j, y, Tiles.encode(hh, Tiles.decodeType(dd), 
/* 1055 */                 Tiles.decodeData(dd)));
/* 1056 */           changes = addToChanges(changes, j, y);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1061 */     for (j = x0; j < x1; j++) {
/*      */       
/* 1063 */       double xd = (j - xm) * 2.0D / (x1 - x0);
/* 1064 */       for (int y = y0; y < y1; y++) {
/*      */         
/* 1066 */         double yd = (y - ym) * 2.0D / (y1 - y0);
/* 1067 */         double d = Math.sqrt(xd * xd + yd * yd);
/* 1068 */         double od = d * (x1 - x0);
/* 1069 */         boolean rock = true;
/*      */         
/* 1071 */         for (int xx = 0; xx < 2; xx++) {
/*      */           
/* 1073 */           for (int yy = 0; yy < 2; yy++) {
/*      */             
/* 1075 */             int height = Tiles.decodeHeight(this.topLayer.data[j | y << this.topLayer.getSizeLevel()]);
/* 1076 */             int groundHeight = Tiles.decodeHeight(this.rockLayer.data[j | y << this.topLayer.getSizeLevel()]);
/* 1077 */             if (groundHeight < height) {
/*      */               
/* 1079 */               rock = false;
/*      */             }
/*      */             else {
/*      */               
/* 1083 */               int dd = this.topLayer.data[j | y << this.topLayer.getSizeLevel()];
/* 1084 */               this.topLayer.setTile(j, y, Tiles.encode((short)groundHeight, 
/* 1085 */                     Tiles.decodeType(dd), Tiles.decodeData(dd)));
/* 1086 */               changes = addToChanges(changes, j, y);
/*      */             } 
/*      */           } 
/*      */         } 
/* 1090 */         if (rock) {
/*      */           
/* 1092 */           float ddd = (float)od / 16.0F;
/* 1093 */           if (ddd < 1.0F) {
/*      */             
/* 1095 */             int dd = this.topLayer.data[j | y << this.topLayer.getSizeLevel()];
/* 1096 */             this.topLayer.setTile(j, y, Tiles.encode(Tiles.decodeHeight(dd), Tiles.Tile.TILE_LAVA.id, (byte)0));
/*      */             
/* 1098 */             changes = addToChanges(changes, j, y);
/*      */           }
/*      */           else {
/*      */             
/* 1102 */             int dd = this.topLayer.data[j | y << this.topLayer.getSizeLevel()];
/* 1103 */             this.topLayer.setTile(j, y, Tiles.encode(Tiles.decodeHeight(dd), Tiles.Tile.TILE_ROCK.id, (byte)0));
/*      */             
/* 1105 */             changes = addToChanges(changes, j, y);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1110 */     return changes;
/*      */   }
/*      */ 
/*      */   
/*      */   public void save() throws IOException {
/* 1115 */     this.topLayer.setAllRowsDirty();
/* 1116 */     this.topLayer.saveAll();
/* 1117 */     this.rockLayer.saveAll();
/* 1118 */     this.topLayer.close();
/* 1119 */     this.rockLayer.close();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/*      */     try {
/* 1126 */       logger.info("Loading maps..");
/* 1127 */       IslandAdder islandAdder = new IslandAdder();
/* 1128 */       logger.info("Adding islands..");
/* 1129 */       islandAdder.addIslands(2096);
/* 1130 */       logger.info("Saving islands..");
/* 1131 */       islandAdder.save();
/* 1132 */       logger.info("Finished");
/*      */     }
/* 1134 */     catch (IOException e) {
/*      */       
/* 1136 */       logger.log(Level.SEVERE, "Failed to add islands!", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshIO getTopLayer() {
/* 1145 */     return this.topLayer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshIO getRockLayer() {
/* 1153 */     return this.rockLayer;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\meshgen\IslandAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */