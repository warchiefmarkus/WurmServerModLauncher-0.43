/*      */ package com.wurmonline.server.meshgen;
/*      */ 
/*      */ import com.wurmonline.mesh.BushData;
/*      */ import com.wurmonline.mesh.FoliageAge;
/*      */ import com.wurmonline.mesh.GrassData;
/*      */ import com.wurmonline.mesh.MeshIO;
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.mesh.TreeData;
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Server;
/*      */ import java.awt.Color;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Random;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class MeshGen
/*      */ {
/*   43 */   private static final Logger logger = Logger.getLogger(MeshGen.class.getName());
/*      */   
/*      */   private float[][] groundHeight;
/*      */   
/*      */   private float[][] height;
/*      */   
/*      */   private byte[][] textures;
/*      */   
/*      */   private byte[][] textureDatas;
/*      */   
/*      */   private int level;
/*      */   
/*      */   private int width;
/*      */   
/*      */   private static final float MAP_HEIGHT = 1000.0F;
/*      */   
/*      */   private static final boolean USE_SPLIT_TREES = true;
/*      */   
/*      */   private static final boolean USE_DROP_DIRT_METHOD = false;
/*      */   
/*      */   private static final int NUMBER_OF_DIRT_TO_DROP = 40;
/*      */   
/*      */   private static final boolean CHECK_STRAIGHT_SLOPES = true;
/*      */   
/*      */   private static final int MAX_STRAIGHT_SLOPE = 20;
/*      */   
/*      */   private static final boolean CHECK_DIAGONAL_SLOPES = true;
/*      */   
/*      */   private static final int MAX_DIAGONAL_SLOPE = 20;
/*      */   private static final boolean OLD_MAP_STYLE_ROTATE_AND_FLIP = true;
/*      */   private static final float waterBias = 0.1F;
/*   74 */   private int imageLayer = 0;
/*      */   
/*   76 */   private static final byte TREEID = Tiles.Tile.TILE_TREE.id;
/*   77 */   private static final byte BUSHID = Tiles.Tile.TILE_BUSH.id;
/*   78 */   private static final byte GRASSID = Tiles.Tile.TILE_GRASS.id;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshGen(int level1, MeshGenGui.Task task) throws Exception {
/*   85 */     this.level = level1;
/*   86 */     this.width = 1 << level1;
/*      */     
/*   88 */     logger.info("Level: " + level1);
/*   89 */     logger.info("Width: " + this.width);
/*   90 */     task.setNote(0, "Allocating memory");
/*   91 */     task.setNote(1, "  heights");
/*   92 */     this.height = new float[this.width][this.width];
/*   93 */     task.setNote(25, "  textures");
/*   94 */     this.textures = new byte[this.width][this.width];
/*   95 */     task.setNote(50, "  ground heights");
/*   96 */     this.groundHeight = new float[this.width][this.width];
/*   97 */     task.setNote(75, "  texture data");
/*   98 */     this.textureDatas = new byte[this.width][this.width];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MeshGen(Random random, int aLevel, MeshGenGui.Task task) throws Exception {
/*  105 */     this(aLevel, task);
/*  106 */     PerlinNoise perlin = new PerlinNoise(random, aLevel);
/*      */     
/*  108 */     int steps = 3;
/*      */ 
/*      */     
/*  111 */     task.setMax(this.width * 3 * 2);
/*  112 */     for (int x = 0; x < this.width; x++) {
/*      */       
/*  114 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  116 */         this.textures[x][y] = -1;
/*  117 */         this.height[x][y] = 0.0F;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  122 */     perlin.setRandom(new Random(random.nextLong()));
/*  123 */     for (int i = 0; i < 3; i++) {
/*      */       
/*  125 */       perlin.setRandom(new Random(random.nextLong()));
/*  126 */       task.setNote("Calculating perlin noise..");
/*      */ 
/*      */       
/*  129 */       float[][] hs1 = perlin.generatePerlinNoise(0.3F, (i == 0) ? 0 : 1, task, this.width * i * 2, 0);
/*      */       
/*  131 */       for (int j = 0; j < this.width; j++) {
/*      */         
/*  133 */         task.setNote(j + this.width * i * 2 + this.width);
/*  134 */         for (int y = 0; y < this.width; y++) {
/*      */           
/*  136 */           float h = hs1[j][y] - this.height[j][y];
/*  137 */           if (h < 0.0F)
/*  138 */             h = -h; 
/*  139 */           h = (float)Math.pow(h, 1.2D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  147 */           this.height[j][y] = h;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getWidth() {
/*  157 */     return this.width;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setData(float[] data, MeshGenGui.Task task) throws Exception {
/*  163 */     task.setMax(this.width * 7);
/*  164 */     task.setNote(0, "Normalizing.");
/*  165 */     Random grassRand = new Random();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  171 */     float lowest = Float.MAX_VALUE;
/*  172 */     float highest = Float.MIN_VALUE;
/*  173 */     float numsover50 = 0.0F;
/*      */     
/*  175 */     task.setNote("  Pass 1");
/*  176 */     for (int x = 0; x < this.width; x++) {
/*      */       
/*  178 */       task.setNote(x);
/*  179 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  181 */         this.height[x][y] = data[y + x * this.width];
/*      */         
/*  183 */         float n1 = this.height[x][y];
/*  184 */         if (n1 < lowest) {
/*  185 */           lowest = n1;
/*      */         }
/*  187 */         if (n1 > highest)
/*  188 */           highest = n1; 
/*  189 */         if (n1 > 0.1F) {
/*  190 */           numsover50++;
/*      */         }
/*      */         
/*  193 */         for (int xx = x - 1; xx <= x + 1; xx++) {
/*      */           
/*  195 */           for (int yy = y - 1; yy <= y + 1; yy++) {
/*      */             
/*  197 */             if (xx >= 0 && yy >= 0 && xx < this.width && yy < this.width) {
/*      */               
/*  199 */               this.height[x][y] = this.height[x][y] + Math.min(data[y + x * this.width], data[yy + xx * this.width]);
/*      */             }
/*      */             else {
/*      */               
/*  203 */               this.height[x][y] = this.height[x][y] + data[y + x * this.width];
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  209 */         this.height[x][y] = this.height[x][y] / 10.0F;
/*      */       } 
/*      */     } 
/*      */     
/*  213 */     float maxHeight = highest - lowest;
/*  214 */     System.out.println("Before percent over 0.1=" + (numsover50 / (this.width * this.width)) + " highest=" + highest + " lowest=" + lowest + " maxheight=" + maxHeight);
/*      */ 
/*      */ 
/*      */     
/*  218 */     lowest = Float.MAX_VALUE;
/*  219 */     highest = Float.MIN_VALUE;
/*  220 */     numsover50 = 0.0F;
/*  221 */     task.setNote("  Pass 2"); int i;
/*  222 */     for (i = 0; i < this.width; i++) {
/*      */       
/*  224 */       task.setNote(i + this.width);
/*  225 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  227 */         float n1 = this.height[i][y];
/*  228 */         if (n1 < lowest) {
/*  229 */           lowest = n1;
/*      */         }
/*  231 */         if (n1 > highest)
/*  232 */           highest = n1; 
/*  233 */         if (n1 > 0.1F) {
/*  234 */           numsover50++;
/*      */         }
/*      */       } 
/*      */     } 
/*  238 */     maxHeight = highest - lowest;
/*  239 */     System.out.println("After percent over 0.1=" + (numsover50 / (this.width * this.width)) + " highest=" + highest + " lowest=" + lowest + " maxheight=" + maxHeight);
/*      */ 
/*      */ 
/*      */     
/*  243 */     task.setNote("  Pass 3");
/*  244 */     for (i = 0; i < this.width; i++) {
/*      */       
/*  246 */       task.setNote(i + this.width * 2);
/*  247 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  249 */         float n1 = this.height[i][y];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  256 */         float h = (n1 - 0.1F) / 0.9F;
/*      */         
/*  258 */         if (h > 0.0F) {
/*      */           
/*  260 */           h += 1.0E-4F;
/*      */         }
/*      */         else {
/*      */           
/*  264 */           h *= 0.5F;
/*  265 */           h -= 1.0E-4F;
/*      */         } 
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
/*  277 */         this.height[i][y] = h;
/*  278 */         this.groundHeight[i][y] = h;
/*      */       } 
/*      */     } 
/*  281 */     lowest = Float.MAX_VALUE;
/*  282 */     highest = Float.MIN_VALUE;
/*  283 */     numsover50 = 0.0F;
/*  284 */     task.setNote("  Pass 4");
/*  285 */     for (i = 0; i < this.width; i++) {
/*      */       
/*  287 */       task.setNote(i + this.width * 3);
/*  288 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  290 */         float n1 = this.height[i][y];
/*  291 */         if (n1 < lowest) {
/*  292 */           lowest = n1;
/*      */         }
/*  294 */         if (n1 > highest)
/*  295 */           highest = n1; 
/*  296 */         if (n1 > 0.1F) {
/*  297 */           numsover50++;
/*      */         }
/*      */       } 
/*      */     } 
/*  301 */     maxHeight = highest - lowest;
/*  302 */     System.out.println("After THIRD percent over 0.1=" + (numsover50 / (this.width * this.width)) + " highest=" + highest + " lowest=" + lowest + " maxheight=" + maxHeight);
/*      */     
/*  304 */     System.out.println("Creating rock layer.");
/*  305 */     float waterConstant = 1.1F;
/*      */ 
/*      */     
/*  308 */     float mapSizeInfluence = 0.035F;
/*      */ 
/*      */ 
/*      */     
/*  312 */     float mapSizeMod = this.width * 0.035F;
/*      */ 
/*      */ 
/*      */     
/*  316 */     float influenceMod = 0.02F;
/*  317 */     System.out.println("mapSizeMod=" + mapSizeMod + ", waterConstant=" + 1.1F + ", influenceMod=" + 0.02F);
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
/*  333 */     task.setNote("  Pass 5 - Creating rock layer.");
/*      */     int j;
/*  335 */     for (j = 0; j < this.width; j++) {
/*      */       
/*  337 */       task.setNote(j + this.width * 4);
/*  338 */       for (int y = 0; y < this.width; y++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  345 */         float hh = Math.max(0.0F, (this.height[j][y] + 0.1F) / 1.1F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  351 */         float heightModifier = 1.1F - hh;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  356 */         float subtracted = (1.0F - getDirtSlope(j, y) * mapSizeMod) * 0.02F * heightModifier / 3.0F;
/*  357 */         if (j == 3274 && y == 1425) {
/*  358 */           System.out.println("dslope=" + getDirtSlope(j, y) + ", subtracted=" + subtracted + " heightmod=" + heightModifier + " height " + j + "," + y + "=" + this.height[j][y]);
/*      */         }
/*  360 */         float h = this.height[j][y] - subtracted;
/*      */ 
/*      */         
/*  363 */         this.groundHeight[j][y] = h;
/*  364 */         if (this.groundHeight[j][y] > this.height[j][y])
/*      */         {
/*  366 */           this.groundHeight[j][y] = this.height[j][y];
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  372 */     task.setNote("  Pass 6 - Applying Cliff, Rock, and Grass tiles.");
/*  373 */     for (j = 0; j < this.width; j++) {
/*      */       
/*  375 */       task.setNote(j + this.width * 5);
/*  376 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  378 */         boolean rock = true;
/*      */         
/*  380 */         for (int xx = 0; xx < 2; xx++) {
/*      */           
/*  382 */           for (int yy = 0; yy < 2; yy++) {
/*      */             
/*  384 */             if (getGroundHeight(j + xx, y + yy) < getHeight(j + xx, y + yy)) {
/*      */               
/*  386 */               if (j == 3274 && y == 1425)
/*  387 */                 System.out.println("ggh=" + getGroundHeight(j + xx, y + yy) + ", gh=" + getHeight(j + xx, y + yy) + ": rock false"); 
/*  388 */               rock = false;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  393 */         if (rock) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  402 */           this.textures[j][y] = Tiles.Tile.TILE_ROCK.id;
/*  403 */           this.textureDatas[j][y] = 0;
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  408 */           setTile(GRASSID, j, y, grassRand);
/*      */         } 
/*      */       } 
/*      */     } 
/*  412 */     task.setNote("  Pass 7 - Check Heights of Rock and Cliff tiles");
/*  413 */     for (j = 0; j < this.width; j++) {
/*      */       
/*  415 */       task.setNote(j + this.width * 6);
/*  416 */       for (int y = 0; y < this.width; y++) {
/*      */         
/*  418 */         if (this.textures[j][y] == Tiles.Tile.TILE_CLIFF.id || this.textures[j][y] == Tiles.Tile.TILE_ROCK.id)
/*      */         {
/*  420 */           if (getGroundHeight(j, y) < getHeight(j, y)) {
/*  421 */             System.out.println("Cliff Error at " + j + ", " + y);
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float getDirtSlope(int x, int y) {
/*  443 */     float hs1 = Math.abs(getHeight(x - 1, y) - getHeight(x, y));
/*  444 */     float hs2 = Math.abs(getHeight(x + 1, y) - getHeight(x, y));
/*  445 */     float vs1 = Math.abs(getHeight(x, y - 1) - getHeight(x, y));
/*  446 */     float vs2 = Math.abs(getHeight(x, y + 1) - getHeight(x, y));
/*      */     
/*  448 */     float hs = hs1 + hs2;
/*  449 */     float vs = vs1 + vs2;
/*      */     
/*  451 */     return (float)Math.sqrt((vs * vs + hs * hs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void generateGround(Random random, MeshGenGui.Task task) {
/*  462 */     int blurSteps = 1;
/*      */ 
/*      */     
/*  465 */     task.setMax(this.width * 1);
/*  466 */     task.setNote("Flowing water..");
/*      */     
/*  468 */     for (int i = 0; i < this.width * this.width / 1000; i++)
/*      */     {
/*  470 */       task.setNote(i);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     float[][] h = new float[this.width][this.width];
/*      */     
/*  481 */     for (int j = 0; j < 1; j++) {
/*      */       int k;
/*  483 */       for (k = 0; k < this.width; k++) {
/*      */         
/*  485 */         for (int y = 0; y < this.width; y++) {
/*      */           
/*  487 */           System.out.println("Setting " + k + ", " + y + " to 0");
/*  488 */           h[k][y] = 0.0F;
/*      */         } 
/*      */       } 
/*      */       
/*  492 */       for (k = 0; k < this.width; k++) {
/*      */         
/*  494 */         task.setNote(k + j * this.width);
/*      */         
/*  496 */         for (int y = 0; y < this.width; y++);
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  518 */       for (k = 0; k < this.width; k++) {
/*      */         
/*  520 */         for (int y = 0; y < this.width; y++)
/*      */         {
/*      */           
/*  523 */           h[k][y] = 0.0F;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  528 */     for (int x = 0; x < this.width; x++)
/*      */     {
/*  530 */       for (int y = 0; y < this.width; y++);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void generateWater() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getHeight(int x, int y) {
/*  551 */     if (x < 0 || y < 0 || x > this.width - 1 || y > this.width - 1) return 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  556 */     return this.height[x & this.width - 1][y & this.width - 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getGroundHeight(int x, int y) {
/*  566 */     if (x < 0 || y < 0 || x > this.width - 1 || y > this.width - 1) return 0.0F;
/*      */ 
/*      */ 
/*      */     
/*  570 */     return this.groundHeight[x & this.width - 1][y & this.width - 1];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getHeightAndWater(int x, int y) {
/*  575 */     x &= this.width - 1;
/*  576 */     y &= this.width - 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  584 */     return this.height[x][y];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeight(int x, int y, float h) {
/*  592 */     this.height[x & this.width - 1][y & this.width - 1] = h;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dropADirt(boolean forceDrop, Random random) {
/*  597 */     for (int x = 0; x < this.width; x++) {
/*      */       
/*  599 */       for (int y = 0; y < this.width; y++) {
/*      */ 
/*      */         
/*  602 */         if (forceDrop || getHeight(x, y) > -1.0F) {
/*      */           
/*  604 */           Point p = findDropTile(x, y, random);
/*  605 */           incHeight(p.getX(), p.getY());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void incHeight(int x, int y) {
/*  617 */     setHeight(x, y, this.height[x & this.width - 1][y & this.width - 1] + 1.0E-4F);
/*      */   }
/*      */ 
/*      */   
/*      */   private Point findDropTile(int tileX, int tileY, Random random) {
/*  622 */     ArrayList<Point> slopes = new ArrayList<>();
/*      */     
/*  624 */     short h = (short)(int)(getHeight(tileX, tileY) * 1000.0F * 10.0F);
/*      */     
/*  626 */     for (int xx = 1; xx >= -1; xx--) {
/*      */       
/*  628 */       for (int yy = 1; yy >= -1; yy--) {
/*      */ 
/*      */ 
/*      */         
/*  632 */         short th = (short)(int)(getHeight(tileX + xx, tileY + yy) * 1000.0F * 10.0F);
/*  633 */         if ((xx == 0 && yy != 0) || (yy == 0 && xx != 0))
/*      */         {
/*  635 */           if (th < h - 20) {
/*  636 */             slopes.add(new Point(tileX + xx, tileY + yy));
/*      */           }
/*      */         }
/*  639 */         if (xx != 0 && yy != 0)
/*      */         {
/*  641 */           if (th < h - 20)
/*  642 */             slopes.add(new Point(tileX + xx, tileY + yy)); 
/*      */         }
/*      */       } 
/*      */     } 
/*  646 */     if (slopes.size() > 0) {
/*      */       
/*  648 */       int r = 0;
/*      */       
/*  650 */       if (slopes.size() > 1)
/*  651 */         r = random.nextInt(slopes.size()); 
/*  652 */       return findDropTile(((Point)slopes.get(r)).getX(), ((Point)slopes.get(r)).getY(), random);
/*      */     } 
/*      */ 
/*      */     
/*  656 */     return new Point(tileX, tileY);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void createReeds2() {
/*  662 */     System.out.println("Skipping reeds");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void createReeds() {
/*  668 */     logger.info("Creating reeds");
/*  669 */     Random grassRand = new Random();
/*      */     
/*  671 */     int grassSeed = grassRand.nextInt();
/*      */     
/*  673 */     int grassMod = 5;
/*      */     
/*  675 */     int grassCommonality = this.width / 20;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  681 */     for (int xk = 0; xk < this.width - 1; xk++) {
/*  682 */       for (int yk = 0; yk < this.width - 1; yk++) {
/*      */         
/*  684 */         float theight = getHeight(xk, yk) * 1000.0F;
/*      */         
/*  686 */         if (theight <= 0.0F && theight > -40.0F) {
/*      */ 
/*      */           
/*  689 */           grassRand.setSeed((grassSeed + xk / grassCommonality + yk / grassCommonality * 10000));
/*      */           
/*  691 */           int randXResult = grassRand.nextInt(5);
/*  692 */           int randYResult = grassRand.nextInt(5);
/*      */           
/*  694 */           if (randXResult == 0 && randYResult == 0) {
/*      */             
/*  696 */             grassRand.setSeed(System.nanoTime());
/*  697 */             if (grassRand.nextBoolean()) {
/*      */               
/*  699 */               byte tileType = Tiles.Tile.TILE_REED.id;
/*  700 */               if (theight < -2.0F) {
/*  701 */                 tileType = Tiles.Tile.TILE_KELP.id;
/*      */               }
/*  703 */               byte tileData = GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, GrassData.FlowerType.NONE);
/*      */               
/*  705 */               this.textures[xk][yk] = tileType;
/*      */ 
/*      */ 
/*      */               
/*  709 */               this.textureDatas[xk][yk] = tileData;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void blotch(byte id, long amount, int spread, int sizeScale, boolean underwater, boolean nearRock, boolean waterOrNoWater, boolean shallowOnly, Random random) {
/*  734 */     logger.info("Adding blotch of " + Tiles.getTile(id).getName() + ". Amount=" + amount + ", spread=" + spread + ", sizeSale=" + sizeScale + ", underwater=" + underwater + ", nearRock=" + nearRock + ", waterOrNoWater=" + waterOrNoWater + ", shallowOnly=" + shallowOnly);
/*      */ 
/*      */ 
/*      */     
/*  738 */     for (int i = 0; i < amount; i++) {
/*      */ 
/*      */       
/*  741 */       int xo = random.nextInt(this.width);
/*  742 */       int yo = random.nextInt(this.width);
/*      */       
/*  744 */       boolean ok = false;
/*      */       
/*  746 */       if (nearRock) {
/*      */         
/*  748 */         ok = isRock(xo, yo);
/*      */       }
/*  750 */       else if (underwater) {
/*      */         
/*  752 */         ok = isWater(xo, yo);
/*      */       }
/*      */       else {
/*      */         
/*  756 */         if (waterOrNoWater) {
/*      */           
/*  758 */           System.out.println("Water or no water triggered:");
/*  759 */           (new Exception()).printStackTrace();
/*      */         } 
/*  761 */         ok = ((waterOrNoWater || !isWater(xo, yo)) && !isRock(xo, yo));
/*      */       } 
/*  763 */       if (ok) {
/*      */         
/*  765 */         int size = (random.nextInt(80) + 10) * sizeScale;
/*      */         
/*  767 */         for (int j = 0; j < size / 5; j++) {
/*      */           
/*  769 */           int x = xo;
/*  770 */           int y = yo;
/*      */           
/*  772 */           for (int k = 0; k < size / 5; k++) {
/*      */             
/*  774 */             if (random.nextInt(2) == 0) {
/*  775 */               x = x + random.nextInt(spread * 2 + 1) - spread;
/*      */             } else {
/*  777 */               y = y + random.nextInt(spread * 2 + 1) - spread;
/*      */             } 
/*  779 */             x &= this.width - 1;
/*  780 */             y &= this.width - 1;
/*      */             
/*  782 */             for (int xk = x; xk < x + sizeScale / 2 + 1; xk++) {
/*      */               
/*  784 */               for (int yk = y; yk < y + sizeScale / 2 + 1; ) {
/*      */                 
/*  786 */                 boolean tileSubmerged = isWater(xk, yk);
/*  787 */                 if (underwater && !tileSubmerged)
/*      */                 {
/*  789 */                   k++;
/*      */                 }
/*  791 */                 if ((!tileSubmerged || underwater) && !isRock(xk, yk)) {
/*      */                   
/*  793 */                   float theight = getHeight(xk, yk);
/*  794 */                   if (shallowOnly) {
/*      */                     
/*  796 */                     if (theight * 1000.0F > -1.0F)
/*      */                     {
/*  798 */                       setTile(id, xk & this.width - 1, yk & this.width - 1, random);
/*      */                     }
/*      */                   } else {
/*      */                     
/*  802 */                     setTile(id, xk & this.width - 1, yk & this.width - 1, random);
/*      */                   } 
/*      */                   yk++;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setTile(byte id, int x, int y, Random random) {
/*  818 */     if (id == GRASSID) {
/*      */       
/*  820 */       GrassData.GrowthStage growthStage = GrassData.GrowthStage.fromInt(random.nextInt(4));
/*  821 */       GrassData.FlowerType flowerType = getRandomFlower(6);
/*  822 */       this.textures[x][y] = id;
/*  823 */       this.textureDatas[x][y] = GrassData.encodeGrassTileData(growthStage, flowerType);
/*      */     }
/*  825 */     else if (id == Tiles.Tile.TILE_REED.id) {
/*      */       
/*  827 */       float theight = getHeight(x, y) * 1000.0F;
/*  828 */       if (theight < -2.0F) {
/*  829 */         this.textures[x][y] = Tiles.Tile.TILE_KELP.id;
/*      */       } else {
/*  831 */         this.textures[x][y] = Tiles.Tile.TILE_REED.id;
/*  832 */       }  this.textureDatas[x][y] = GrassData.encodeGrassTileData(GrassData.GrowthStage.SHORT, GrassData.FlowerType.NONE);
/*      */     }
/*      */     else {
/*      */       
/*  836 */       this.textures[x][y] = id;
/*  837 */       this.textureDatas[x][y] = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static GrassData.FlowerType getRandomFlower(int chance) {
/*  843 */     int rnd = Server.rand.nextInt(chance * 1000);
/*  844 */     if (rnd < 1000) {
/*      */       
/*  846 */       if (rnd > 998)
/*  847 */         return GrassData.FlowerType.FLOWER_7; 
/*  848 */       if (rnd > 990)
/*  849 */         return GrassData.FlowerType.FLOWER_6; 
/*  850 */       if (rnd > 962)
/*  851 */         return GrassData.FlowerType.FLOWER_5; 
/*  852 */       if (rnd > 900)
/*  853 */         return GrassData.FlowerType.FLOWER_4; 
/*  854 */       if (rnd > 800)
/*  855 */         return GrassData.FlowerType.FLOWER_3; 
/*  856 */       if (rnd > 500) {
/*  857 */         return GrassData.FlowerType.FLOWER_2;
/*      */       }
/*  859 */       return GrassData.FlowerType.FLOWER_1;
/*      */     } 
/*  861 */     return GrassData.FlowerType.NONE;
/*      */   }
/*      */ 
/*      */   
/*      */   private void exposeClay(long amount, Random random) {
/*  866 */     logger.info("Attempting to expose " + amount + " tiles of clay");
/*  867 */     for (int i = 0; i < amount; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  873 */       int xo = random.nextInt(this.width - 10) + 1;
/*  874 */       int yo = random.nextInt(this.width - 10) + 1;
/*      */       
/*  876 */       int w = random.nextInt(2) + 2;
/*  877 */       int h = random.nextInt(2) + 2;
/*      */       
/*  879 */       boolean fail = false;
/*  880 */       boolean below = (this.height[xo][yo] * 1000.0F < -4.0F);
/*  881 */       boolean above = (this.height[xo][yo] * 1000.0F > 2.0F);
/*  882 */       if (below || above) {
/*      */ 
/*      */         
/*  885 */         fail = true;
/*      */ 
/*      */       
/*      */       }
/*  889 */       else if (this.height[xo][yo] < 0.0F) {
/*      */ 
/*      */         
/*  892 */         if (this.height[xo + w][yo + h] < 0.0F) {
/*  893 */           fail = true;
/*      */         }
/*      */       } 
/*  896 */       if (!fail)
/*      */       {
/*  898 */         for (int x = xo - 1; x < xo + w + 1; x++) {
/*      */           
/*  900 */           for (int y = yo - 1; y < yo + h + 1; y++) {
/*      */             
/*  902 */             if (this.textures[x][y] == Tiles.Tile.TILE_ROCK.id)
/*  903 */               fail = true; 
/*  904 */             if (this.textures[x][y] == Tiles.Tile.TILE_CLIFF.id)
/*  905 */               fail = true; 
/*  906 */             if (this.textures[x][y] == Tiles.Tile.TILE_TAR.id)
/*  907 */               fail = true; 
/*  908 */             if (this.textures[x][y] == Tiles.Tile.TILE_CLAY.id)
/*  909 */               fail = true; 
/*      */           } 
/*      */         } 
/*      */       }
/*  913 */       if (!fail) {
/*      */         
/*  915 */         System.out.print(".");
/*  916 */         for (int x = xo; x < xo + w + 1; x++) {
/*      */           
/*  918 */           for (int y = yo; y < yo + h + 1; y++) {
/*      */             
/*  920 */             if (this.height[x][y] > this.groundHeight[x][y])
/*      */             {
/*  922 */               this.height[x][y] = this.height[x][y] * 0.95F + this.groundHeight[x][y] * 0.05F;
/*      */             }
/*  924 */             if (x < xo + w && y < yo + h)
/*      */             {
/*  926 */               this.textures[x][y] = Tiles.Tile.TILE_CLAY.id;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } else {
/*  931 */         i--;
/*      */       } 
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void exposeTar(long amount, Random random) {
/*  951 */     logger.info("Attempting to expose " + amount + " tiles of tar");
/*  952 */     for (int i = 0; i < amount; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  958 */       int xo = random.nextInt(this.width - 10) + 1;
/*  959 */       int yo = random.nextInt(this.width - 10) + 1;
/*      */       
/*  961 */       int w = random.nextInt(2) + 2;
/*  962 */       int h = random.nextInt(2) + 2;
/*      */       
/*  964 */       boolean fail = false; int x;
/*  965 */       for (x = xo - 1; x < xo + w + 1; x++) {
/*      */         
/*  967 */         for (int y = yo - 1; y < yo + h + 1; y++) {
/*      */           
/*  969 */           if (this.textures[x][y] == Tiles.Tile.TILE_ROCK.id)
/*  970 */             fail = true; 
/*  971 */           if (this.textures[x][y] == Tiles.Tile.TILE_CLIFF.id)
/*  972 */             fail = true; 
/*  973 */           if (this.textures[x][y] == Tiles.Tile.TILE_TAR.id) {
/*  974 */             fail = true;
/*      */           }
/*      */         } 
/*      */       } 
/*  978 */       if (!fail)
/*      */       {
/*  980 */         for (x = xo; x < xo + w + 1; x++) {
/*      */           
/*  982 */           for (int y = yo; y < yo + h + 1; y++) {
/*      */             
/*  984 */             if (this.height[x][y] > this.groundHeight[x][y])
/*      */             {
/*  986 */               this.height[x][y] = this.height[x][y] * 0.95F + this.groundHeight[x][y] * 0.05F;
/*      */             }
/*  988 */             if (x < xo + w && y < yo + h)
/*      */             {
/*  990 */               this.textures[x][y] = Tiles.Tile.TILE_TAR.id;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isRock(int x, int y) {
/* 1062 */     for (int xx = 0; xx < 2; xx++) {
/*      */       
/* 1064 */       for (int yy = 0; yy < 2; yy++) {
/*      */         
/* 1066 */         if (getGroundHeight(x + xx, y + yy) < getHeight(x + xx, y + yy))
/*      */         {
/* 1068 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/* 1072 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isWater(int x, int y) {
/* 1077 */     for (int xx = 0; xx < 2; xx++) {
/*      */       
/* 1079 */       for (int yy = 0; yy < 2; yy++) {
/*      */         
/* 1081 */         if (getHeight(x + xx, y + yy) < 0.0F)
/*      */         {
/* 1083 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 1087 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void generateTextures(Random random, MeshGenGui.Task task) {
/* 1092 */     logger.info("Generating texture");
/* 1093 */     this.textureDatas = new byte[this.width][this.width];
/*      */     
/* 1095 */     task.setMax(100);
/* 1096 */     task.setNote(0, "  Convert underwater grass into dirt.");
/* 1097 */     for (int x = 0; x < this.width; x++) {
/*      */       
/* 1099 */       for (int y = 0; y < this.width; y++) {
/*      */         
/* 1101 */         if (!isRock(x, y))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1107 */           if (!isWater(x, y)) {
/* 1108 */             setTile(GRASSID, x, y, random);
/*      */           }
/*      */           else {
/*      */             
/* 1112 */             this.textures[x][y] = Tiles.Tile.TILE_DIRT.id;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1122 */     task.setNote(10, "  Adding blotches.");
/* 1123 */     int wsqd = this.width * this.width / 10240;
/* 1124 */     int sizeMod = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1133 */     task.setNote(10, "    dirt.");
/* 1134 */     blotch(Tiles.Tile.TILE_DIRT.id, (wsqd / 3), 2, 1, false, true, false, false, random);
/* 1135 */     task.setNote(11, "    peat.");
/* 1136 */     blotch(Tiles.Tile.TILE_PEAT.id, (wsqd / 4), 3, 2, false, false, false, false, random);
/* 1137 */     task.setNote(12, "    steppe.");
/* 1138 */     blotch(Tiles.Tile.TILE_STEPPE.id, (wsqd / 17 / 1), 4, 8, false, false, false, false, random);
/* 1139 */     task.setNote(13, "    desert.");
/* 1140 */     blotch(Tiles.Tile.TILE_SAND.id, (wsqd / 15 / 1), 3, 8, false, false, false, false, random);
/* 1141 */     task.setNote(14, "    tundra.");
/* 1142 */     blotch(Tiles.Tile.TILE_TUNDRA.id, (wsqd / 18 / 1), 3, 7, false, false, false, false, random);
/* 1143 */     task.setNote(15, "    moss.");
/* 1144 */     blotch(Tiles.Tile.TILE_MOSS.id, (wsqd / 5), 1, 2, false, false, false, false, random);
/* 1145 */     task.setNote(16, "    gravel.");
/* 1146 */     blotch(Tiles.Tile.TILE_GRAVEL.id, (wsqd / 3), 5, 2, false, true, false, true, random);
/*      */ 
/*      */ 
/*      */     
/* 1150 */     task.setNote(17, "    underwater sand.");
/* 1151 */     blotch(Tiles.Tile.TILE_SAND.id, (wsqd * 4), 2, 3, true, false, true, false, random);
/* 1152 */     task.setNote(18, "    marsh.");
/* 1153 */     blotch(Tiles.Tile.TILE_MARSH.id, (wsqd / 2), 4, 5, true, false, false, true, random);
/* 1154 */     task.setNote(19, "    reeds and kelp.");
/* 1155 */     blotch(Tiles.Tile.TILE_REED.id, (wsqd / 2), 2, 3, true, false, false, true, random);
/*      */ 
/*      */ 
/*      */     
/* 1159 */     logger.info("Adding random trees.");
/*      */     
/* 1161 */     task.setNote(25, "  Adding random trees.");
/*      */     
/* 1163 */     for (int i = 0; i < this.width * this.width * 1; i++) {
/*      */       
/* 1165 */       int k = random.nextInt(this.width);
/* 1166 */       int y = random.nextInt(this.width);
/*      */       
/* 1168 */       if (random.nextFloat() < 0.04F && isTreeCapable(k, y, 5)) {
/*      */         
/* 1170 */         int age = generateAge(random);
/* 1171 */         GrassData.GrowthTreeStage grassLen = GrassData.GrowthTreeStage.fromInt(random.nextInt(3) + 1);
/*      */         
/* 1173 */         if (random.nextInt(4) == 2) {
/*      */ 
/*      */           
/* 1176 */           int type = random.nextInt(BushData.BushType.getLength());
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1181 */           this.textures[k][y] = BushData.BushType.fromInt(type).asNormalBush();
/* 1182 */           this.textureDatas[k][y] = Tiles.encodeTreeData(FoliageAge.fromByte((byte)age), false, false, grassLen);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1193 */           int type = random.nextInt(TreeData.TreeType.getLength());
/*      */ 
/*      */           
/* 1196 */           if (type == TreeData.TreeType.OAK.getTypeId() && random.nextInt(3) != 0)
/* 1197 */             type = TreeData.TreeType.BIRCH.getTypeId(); 
/* 1198 */           if (type == TreeData.TreeType.WILLOW.getTypeId() && random.nextInt(2) != 0) {
/* 1199 */             type = TreeData.TreeType.PINE.getTypeId();
/*      */           }
/*      */ 
/*      */           
/* 1203 */           this.textures[k][y] = TreeData.TreeType.fromInt(type).asNormalTree();
/* 1204 */           this.textureDatas[k][y] = Tiles.encodeTreeData(FoliageAge.fromByte((byte)age), false, false, grassLen);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1214 */     logger.info("Making Forests.");
/* 1215 */     task.setNote(30, "  Making forests.");
/*      */     
/* 1217 */     int info = 31;
/* 1218 */     int infotick = this.width * 16 / 60;
/* 1219 */     for (int j = 0; j < this.width * 16; j++) {
/*      */       
/* 1221 */       if ((j + 1) % infotick == 0) {
/*      */         
/* 1223 */         task.setNote(info);
/* 1224 */         info++;
/*      */       } 
/* 1226 */       int k = random.nextInt(this.width);
/* 1227 */       int y = random.nextInt(this.width);
/* 1228 */       Tiles.Tile tex = Tiles.getTile(this.textures[k][y]);
/* 1229 */       if (tex.isBush() || tex.isTree())
/*      */       {
/*      */         
/* 1232 */         makeForest(k, y, tex, this.textureDatas[k][y], random);
/*      */       }
/*      */     } 
/*      */     
/* 1236 */     task.setNote(92, "  Adding grass plains...");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1244 */     blotch(Tiles.Tile.TILE_GRASS.id, (wsqd / 2 / 1), 3, 20, false, false, false, false, random);
/*      */ 
/*      */     
/* 1247 */     task.setNote(95, "  Exposing some tar...");
/* 1248 */     exposeTar(wsqd, random);
/* 1249 */     task.setNote(96, "  Exposing some clay...");
/* 1250 */     exposeClay(wsqd, random);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1259 */     logger.info("Finished!");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeForest(int x, int y, Tiles.Tile theTile, byte data, Random random) {
/* 1267 */     int sqrw = (int)Math.sqrt(this.width);
/* 1268 */     int maxForestSize = theTile.isBush() ? (this.width / sqrw / 2) : (this.width / sqrw);
/* 1269 */     for (int i = 0; i < sqrw * 10; i++) {
/*      */       
/* 1271 */       int count = 0;
/*      */ 
/*      */ 
/*      */       
/* 1275 */       int scarcity = (theTile.isOak(data) || theTile.isWillow(data)) ? 0 : (random.nextInt(3) * 2 + 1);
/* 1276 */       for (int j = 0; j < maxForestSize; j++) {
/*      */         
/* 1278 */         int xx = x + random.nextInt(maxForestSize * 2 + 1) - maxForestSize;
/* 1279 */         int yy = y + random.nextInt(maxForestSize * 2 + 1) - maxForestSize;
/*      */         
/* 1281 */         if (isTreeCapable(xx, yy, scarcity)) {
/*      */           
/* 1283 */           addTree(x, y, xx, yy, random);
/* 1284 */           count++;
/*      */         } 
/*      */       } 
/* 1287 */       if (count == 0) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addTree(int origx, int origy, int x, int y, Random random) {
/* 1294 */     this.textures[x][y] = this.textures[origx][origy];
/* 1295 */     int age = generateAge(random);
/*      */ 
/*      */     
/* 1298 */     GrassData.GrowthTreeStage grassLen = GrassData.GrowthTreeStage.fromInt(random.nextInt(3) + 1);
/* 1299 */     this.textureDatas[x][y] = Tiles.encodeTreeData(FoliageAge.fromByte((byte)age), false, false, grassLen);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int generateAge(Random random) {
/* 1310 */     int age = random.nextInt(13) + 1;
/* 1311 */     int age2 = random.nextInt(13) + 1;
/* 1312 */     if (age2 > age)
/* 1313 */       age = age2; 
/* 1314 */     age2 = random.nextInt(13) + 1;
/* 1315 */     if (age2 > age) {
/* 1316 */       age = age2;
/*      */     }
/* 1318 */     return age;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isTreeCapable(int x, int y, int maxNeighbours) {
/* 1323 */     if (x < 0 || y < 0 || x >= this.width || y >= this.width) {
/* 1324 */       return false;
/*      */     }
/* 1326 */     if (this.textures[x & this.width - 1][y & this.width - 1] != GRASSID) {
/* 1327 */       return false;
/*      */     }
/*      */     
/* 1330 */     if (getGroundHeight(x, y) > 0.65F) {
/* 1331 */       return false;
/*      */     }
/*      */     
/* 1334 */     int neighborTrees = 0;
/*      */     
/* 1336 */     for (int xx = x - 1; xx <= x + 1; xx++) {
/*      */       
/* 1338 */       for (int yy = y - 1; yy <= y + 1; yy++) {
/*      */         
/* 1340 */         int xxx = xx & this.width - 1;
/* 1341 */         int yyy = yy & this.width - 1;
/*      */         
/* 1343 */         Tiles.Tile theTile = Tiles.getTile(this.textures[xxx][yyy]);
/* 1344 */         if (theTile.isTree()) {
/*      */           
/* 1346 */           byte data = this.textureDatas[xxx][yyy];
/*      */           
/* 1348 */           if (theTile.isOak(data) || theTile.isWillow(data)) {
/* 1349 */             return false;
/*      */           }
/* 1351 */           neighborTrees++;
/*      */         }
/* 1353 */         else if (theTile.isBush()) {
/* 1354 */           neighborTrees++;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1358 */     return (neighborTrees <= maxNeighbours);
/*      */   }
/*      */ 
/*      */   
/*      */   public BufferedImage getImage(MeshGenGui.Task task) {
/* 1363 */     return getImage(this.imageLayer, task);
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
/*      */   
/*      */   public BufferedImage getImage(int layer, MeshGenGui.Task task) {
/* 1377 */     task.setMax(this.width + 50);
/* 1378 */     task.setNote(0, "Generating image.");
/*      */     
/* 1380 */     int lWidth = 16384;
/* 1381 */     if (lWidth > this.width)
/* 1382 */       lWidth = this.width; 
/* 1383 */     int yo = this.width - lWidth;
/* 1384 */     if (yo < 0)
/* 1385 */       yo = 0; 
/* 1386 */     int xo = this.width - lWidth;
/* 1387 */     if (xo < 0) {
/* 1388 */       xo = 0;
/*      */     }
/* 1390 */     Random random = new Random();
/* 1391 */     if (xo > 0)
/* 1392 */       xo = random.nextInt(xo); 
/* 1393 */     if (yo > 0) {
/* 1394 */       yo = random.nextInt(yo);
/*      */     }
/* 1396 */     BufferedImage bi2 = new BufferedImage(lWidth, lWidth, 1);
/* 1397 */     float[] data = new float[lWidth * lWidth * 3];
/*      */ 
/*      */     
/* 1400 */     task.setNote(1, "  Generate colours.");
/* 1401 */     for (int x = 0; x < lWidth; x++) {
/*      */       
/* 1403 */       task.setNote(x + 2);
/* 1404 */       int alt = lWidth - 1;
/* 1405 */       for (int y = lWidth - 1; y >= 0; y--) {
/*      */         
/* 1407 */         float node = 0.0F;
/* 1408 */         float node2 = 0.0F;
/*      */         
/* 1410 */         if (layer == 0) {
/*      */           
/* 1412 */           node = getHeight(x + xo, y + yo);
/*      */           
/* 1414 */           node2 = getHeight(x + 1 + xo, y + 1 + yo);
/*      */         }
/*      */         else {
/*      */           
/* 1418 */           node = getGroundHeight(x + xo, y + yo);
/*      */ 
/*      */           
/* 1421 */           node2 = getGroundHeight(x + 1 + xo, y + 1 + yo);
/*      */         } 
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
/* 1435 */         byte tex = this.textures[x + xo][y + yo];
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1440 */         float hh = node;
/*      */ 
/*      */ 
/*      */         
/* 1444 */         float h = (node2 - node) * 1500.0F / 256.0F * (1 << this.level) / 128.0F + hh / 2.0F + 1.0F;
/* 1445 */         h *= 0.4F;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1450 */         float r = h;
/* 1451 */         float g = h;
/* 1452 */         float b = h;
/*      */         
/* 1454 */         if (layer == 0) {
/*      */           
/* 1456 */           Color color = Tiles.getTile(tex).getColor();
/* 1457 */           r *= color.getRed() / 255.0F * 2.0F;
/* 1458 */           g *= color.getGreen() / 255.0F * 2.0F;
/* 1459 */           b *= color.getBlue() / 255.0F * 2.0F;
/*      */         } 
/*      */         
/* 1462 */         if (r < 0.0F)
/* 1463 */           r = 0.0F; 
/* 1464 */         if (r > 1.0F)
/* 1465 */           r = 1.0F; 
/* 1466 */         if (g < 0.0F)
/* 1467 */           g = 0.0F; 
/* 1468 */         if (g > 1.0F)
/* 1469 */           g = 1.0F; 
/* 1470 */         if (b < 0.0F)
/* 1471 */           b = 0.0F; 
/* 1472 */         if (b > 1.0F) {
/* 1473 */           b = 1.0F;
/*      */         }
/* 1475 */         if (node < 0.0F) {
/*      */           
/* 1477 */           r = r * 0.2F + 0.16000001F;
/* 1478 */           g = g * 0.2F + 0.2F;
/* 1479 */           b = b * 0.2F + 0.4F;
/*      */         } 
/*      */         
/* 1482 */         int altTarget = y - (int)(getHeight(x, y) * 1000.0F / 4.0F);
/* 1483 */         while (alt > altTarget && alt >= 0) {
/*      */           
/* 1485 */           data[(x + alt * lWidth) * 3 + 0] = r * 255.0F;
/* 1486 */           data[(x + alt * lWidth) * 3 + 1] = g * 255.0F;
/* 1487 */           data[(x + alt * lWidth) * 3 + 2] = b * 255.0F;
/* 1488 */           alt--;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1493 */     task.setNote(this.width + 10, "  Convert colours to image.");
/* 1494 */     bi2.getRaster().setPixels(0, 0, lWidth, lWidth, data);
/* 1495 */     return bi2;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int[] getData(MeshGenGui.Task task) {
/* 1500 */     logger.info("Getting data for a " + this.width + 'x' + this.width + " map. Map height is " + 1000.0F);
/* 1501 */     task.setMax(this.width);
/* 1502 */     task.setNote(0, "getting Surface Data");
/* 1503 */     int[] data = new int[this.width * this.width];
/* 1504 */     int x = 0;
/* 1505 */     int y = 0;
/*      */     
/*      */     try {
/* 1508 */       for (y = 0; y < this.width; y++)
/*      */       {
/* 1510 */         task.setNote(y);
/* 1511 */         for (x = 0; x < this.width; x++)
/*      */         {
/* 1513 */           float lHeight = getHeight(x, y) * 1000.0F;
/*      */ 
/*      */           
/* 1516 */           byte tex = this.textures[x][y];
/* 1517 */           byte texdata = this.textureDatas[x][y];
/* 1518 */           data[x + (y << this.level)] = Tiles.encode(lHeight, tex, texdata);
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 1523 */     } catch (ArrayIndexOutOfBoundsException e) {
/*      */       
/* 1525 */       logger.log(Level.WARNING, "data: " + data.length + ", x: " + x + ", y: " + y + ", x + (y << (level + 1): " + (x + (y << this.level + 1)), e);
/*      */       
/* 1527 */       throw e;
/*      */     } 
/* 1529 */     return data;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int[] getRockData(MeshGenGui.Task task) {
/* 1534 */     logger.info("Getting rock data for a " + this.width + 'x' + this.width + " map. Map height is " + 1000.0F);
/* 1535 */     task.setMax(this.width);
/* 1536 */     task.setNote(0, "getting Surface Data");
/* 1537 */     int[] data = new int[this.width * this.width];
/*      */     
/* 1539 */     for (int y = 0; y < this.width; y++) {
/*      */       
/* 1541 */       task.setNote(y);
/* 1542 */       for (int x = 0; x < this.width; x++) {
/*      */         
/* 1544 */         float lHeight = getGroundHeight(x, y);
/* 1545 */         byte tex = 0;
/* 1546 */         byte texdata = 0;
/*      */         
/* 1548 */         data[x + (y << this.level)] = Tiles.encode(lHeight * 1000.0F, (byte)0, (byte)0);
/*      */       } 
/*      */     } 
/*      */     
/* 1552 */     return data;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getLevel() {
/* 1557 */     return this.level;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setData(MeshIO meshIO, MeshIO meshIO2, MeshGenGui.Task task) {
/* 1562 */     task.setMax(this.width * 2);
/* 1563 */     task.setNote(0, "setting Data"); int x;
/* 1564 */     for (x = 0; x < this.width; x++) {
/*      */       
/* 1566 */       task.setNote(x);
/* 1567 */       for (int y = 0; y < this.width; y++) {
/*      */         
/* 1569 */         int tile = meshIO.getTile(x, y);
/* 1570 */         setHeight(x, y, Tiles.decodeHeightAsFloat(tile) / 1000.0F);
/*      */         
/* 1572 */         this.textures[x][y] = Tiles.decodeType(tile);
/*      */         
/* 1574 */         this.textureDatas[x][y] = Tiles.decodeData(tile);
/*      */       } 
/*      */     } 
/* 1577 */     for (x = 0; x < this.width; x++) {
/*      */       
/* 1579 */       task.setNote(this.width + x);
/* 1580 */       for (int y = 0; y < this.width; y++) {
/*      */         
/* 1582 */         int tile = meshIO2.getTile(x, y);
/* 1583 */         float orgHeight = Tiles.decodeHeightAsFloat(tile) / 1000.0F;
/*      */ 
/*      */ 
/*      */         
/* 1587 */         this.groundHeight[x][y] = orgHeight;
/*      */       } 
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getImageLayer() {
/* 1608 */     return this.imageLayer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setImageLayer(int aImageLayer) {
/* 1617 */     this.imageLayer = aImageLayer;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\meshgen\MeshGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */