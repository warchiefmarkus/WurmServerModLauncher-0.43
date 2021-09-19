/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
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
/*     */ public class Mesh
/*     */ {
/*     */   public static final float MAX_NOISE = 200.0F;
/*     */   private final int width;
/*     */   private final int height;
/*     */   Node[][] nodes;
/*     */   private final int meshWidth;
/*     */   private final float textureScale;
/*     */   private final int heightMinusOne;
/*     */   private final int widthMinusOne;
/*     */   private boolean wrap = false;
/*     */   
/*     */   public Mesh(int width, int height, int meshWidth) {
/*  49 */     this.width = width;
/*  50 */     this.height = height;
/*  51 */     this.widthMinusOne = width - 1;
/*  52 */     this.heightMinusOne = height - 1;
/*  53 */     this.meshWidth = meshWidth;
/*  54 */     this.textureScale = 1.0F;
/*     */     
/*  56 */     this.nodes = new Node[width][height];
/*  57 */     for (int x = 0; x < width; x++) {
/*     */       
/*  59 */       for (int y = 0; y < width; y++)
/*     */       {
/*  61 */         this.nodes[x][y] = new Node();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTextureScale() {
/*  68 */     return this.textureScale;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getMeshWidth() {
/*  73 */     return this.meshWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateMesh(InputStream in) throws IOException, ClassNotFoundException {
/*  78 */     ObjectInputStream ois = new ObjectInputStream(in);
/*  79 */     int[][] tileDatas = (int[][])ois.readObject();
/*     */     
/*  81 */     ois.close();
/*     */     
/*  83 */     for (int x = 0; x < this.width; x++) {
/*     */       
/*  85 */       for (int y = 0; y < this.width; y++) {
/*     */         
/*  87 */         this.nodes[x][y] = new Node();
/*     */         
/*  89 */         this.nodes[x][y].setHeight(Tiles.decodeHeightAsFloat(tileDatas[x][y]));
/*  90 */         this.nodes[x][y].setTexture(Tiles.decodeType(tileDatas[x][y]));
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     processData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateEmpty(boolean createObjects) {
/*  99 */     for (int x = 0; x < this.width; x++) {
/*     */       
/* 101 */       for (int y = 0; y < this.width; y++) {
/*     */         
/* 103 */         if (createObjects)
/* 104 */           this.nodes[x][y] = new Node(); 
/* 105 */         this.nodes[x][y].setHeight(-100.0F);
/* 106 */         this.nodes[x][y].setTexture(Tiles.Tile.TILE_SAND.getId());
/*     */         
/* 108 */         if (createObjects)
/* 109 */           this.nodes[x][y].setNormals(new float[3]); 
/* 110 */         (this.nodes[x][y]).normals[0] = 0.0F;
/* 111 */         (this.nodes[x][y]).normals[1] = 1.0F;
/* 112 */         (this.nodes[x][y]).normals[2] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     processData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void processData() {
/* 121 */     processData(0, 0, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processData(int x1, int y1, int x2, int y2) {
/* 126 */     for (int x = x1; x < x2; x++) {
/*     */       
/* 128 */       for (int y = y1; y < y2; y++) {
/*     */         
/* 130 */         Node node = getNode(x, y);
/* 131 */         float b = node.getHeight();
/* 132 */         float t = node.getHeight();
/*     */ 
/*     */         
/* 135 */         if (getNode(x + 1, y).getHeight() < b)
/* 136 */           b = getNode(x + 1, y).getHeight(); 
/* 137 */         if (getNode(x + 1, y).getHeight() > t) {
/* 138 */           t = getNode(x + 1, y).getHeight();
/*     */         }
/* 140 */         if (getNode(x + 1, y + 1).getHeight() < b)
/* 141 */           b = getNode(x + 1, y + 1).getHeight(); 
/* 142 */         if (getNode(x + 1, y + 1).getHeight() > t) {
/* 143 */           t = getNode(x + 1, y + 1).getHeight();
/*     */         }
/* 145 */         if (getNode(x, y + 1).getHeight() < b)
/* 146 */           b = getNode(x, y + 1).getHeight(); 
/* 147 */         if (getNode(x, y + 1).getHeight() > t) {
/* 148 */           t = getNode(x, y + 1).getHeight();
/*     */         }
/*     */         
/* 151 */         float h = t - b;
/*     */         
/* 153 */         node.setBbBottom(b);
/* 154 */         node.setBbHeight(h);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateNormals() {
/* 161 */     calculateNormals(0, 0, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateNormals(int x1, int y1, int x2, int y2) {
/* 166 */     for (int x = x1; x < x2; x++) {
/*     */       
/* 168 */       for (int y = y1; y < y2; y++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 173 */         Node n1 = getNode(x, y);
/*     */         
/* 175 */         float v1x = getMeshWidth();
/* 176 */         float v1y = getNode(x + 1, y).getHeight() - n1.getHeight();
/* 177 */         float v1z = 0.0F;
/*     */         
/* 179 */         float v2x = 0.0F;
/* 180 */         float v2y = getNode(x, y + 1).getHeight() - n1.getHeight();
/* 181 */         float v2z = getMeshWidth();
/*     */         
/* 183 */         float vx = v1y * v2z - v1z * v2y;
/* 184 */         float vy = v1z * v2x - v1x * v2z;
/* 185 */         float vz = v1x * v2y - v1y * v2x;
/*     */         
/* 187 */         v1x = -getMeshWidth();
/* 188 */         v1y = getNode(x - 1, y).getHeight() - n1.getHeight();
/* 189 */         v1z = 0.0F;
/*     */         
/* 191 */         v2x = 0.0F;
/* 192 */         v2y = getNode(x, y - 1).getHeight() - n1.getHeight();
/* 193 */         v2z = -getMeshWidth();
/*     */         
/* 195 */         vx += v1y * v2z - v1z * v2y;
/* 196 */         vy += v1z * v2x - v1x * v2z;
/* 197 */         vz += v1x * v2y - v1y * v2x;
/*     */         
/* 199 */         float dist = (float)Math.sqrt((vx * vx + vy * vy + vz * vz));
/* 200 */         vx /= dist;
/* 201 */         vy /= dist;
/* 202 */         vz /= dist;
/*     */         
/* 204 */         n1.normals[0] = -vx;
/* 205 */         n1.normals[1] = -vy;
/* 206 */         n1.normals[2] = -vz;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatBuffer createFloatBuffer(int size) {
/* 213 */     ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
/* 214 */     temp.order(ByteOrder.nativeOrder());
/*     */     
/* 216 */     return temp.asFloatBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWraparound() {
/* 221 */     this.wrap = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getNode(int x, int y) {
/* 226 */     if (!this.wrap && (x < 0 || y < 0 || x >= this.width || y >= this.height)) {
/* 227 */       return this.nodes[0][0];
/*     */     }
/* 229 */     return this.nodes[x & this.widthMinusOne][y & this.heightMinusOne];
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateHills() {
/* 234 */     float[][] heights = new float[this.width][this.height];
/*     */     int x;
/* 236 */     for (x = 0; x < this.width; x++) {
/*     */       
/* 238 */       for (int y = 0; y < this.height; y++) {
/*     */         
/* 240 */         Node node = this.nodes[x][y];
/*     */         
/* 242 */         heights[x][y] = node.getHeight();
/*     */         
/* 244 */         boolean lower = (heights[x][y] < -64.0F);
/*     */         
/* 246 */         if (heights[x][y] < 0.0F)
/*     */         {
/* 248 */           heights[x][y] = heights[x][y] * 0.1F;
/*     */         }
/*     */         
/* 251 */         if (lower)
/*     */         {
/* 253 */           heights[x][y] = heights[x][y] * 2.0F - 3.0F;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 258 */     for (x = 0; x < this.width; x++) {
/*     */       
/* 260 */       for (int y = 0; y < this.height; y++)
/*     */       {
/* 262 */         this.nodes[x][y].setHeight(heights[x][y]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void smooth(float bias) {
/* 269 */     float[][] heights = new float[this.width][this.height];
/*     */     int x;
/* 271 */     for (x = 0; x < this.width; x++) {
/*     */       
/* 273 */       for (int y = 0; y < this.height; y++) {
/*     */         
/* 275 */         Node node = this.nodes[x][y];
/*     */         
/* 277 */         float surroundingHeight = 0.0F;
/* 278 */         surroundingHeight += getNode(x - 1, y).getHeight();
/* 279 */         surroundingHeight += getNode(x + 1, y).getHeight();
/* 280 */         surroundingHeight += getNode(x, y - 1).getHeight();
/* 281 */         surroundingHeight += getNode(x, y + 1).getHeight();
/*     */         
/* 283 */         heights[x][y] = node.getHeight() * bias + surroundingHeight / 4.0F * (1.0F - bias);
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     for (x = 0; x < this.width; x++) {
/*     */       
/* 289 */       for (int y = 0; y < this.height; y++)
/*     */       {
/* 291 */         this.nodes[x][y].setHeight(heights[x][y]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void noise(Random random, float noiseLevel) {
/* 298 */     for (int x = 0; x < this.width; x++) {
/*     */       
/* 300 */       for (int y = 0; y < this.height; y++)
/*     */       {
/* 302 */         this.nodes[x][y].setHeight(this.nodes[x][y].getHeight() + (random.nextFloat() - 0.5F) * noiseLevel);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 309 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 314 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTransition(float xp, float yp) {
/* 319 */     while (xp < 0.0F)
/* 320 */       xp += (getWidth() * getMeshWidth()); 
/* 321 */     while (yp < 0.0F) {
/* 322 */       yp += (getHeight() * getMeshWidth());
/*     */     }
/* 324 */     int xx = (int)(xp / getMeshWidth());
/* 325 */     int yy = (int)(yp / getMeshWidth());
/*     */     
/* 327 */     return (getNode(xx, yy).getTexture() == Tiles.Tile.TILE_HOLE.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight(float xp, float yp) {
/* 332 */     while (xp < 0.0F)
/* 333 */       xp += (getWidth() * getMeshWidth()); 
/* 334 */     while (yp < 0.0F) {
/* 335 */       yp += (getHeight() * getMeshWidth());
/*     */     }
/* 337 */     int xx = (int)(xp / getMeshWidth());
/* 338 */     int yy = (int)(yp / getMeshWidth());
/*     */     
/* 340 */     if (getNode(xx, yy).getTexture() == Tiles.Tile.TILE_HOLE.getId()) {
/* 341 */       return 1.0F;
/*     */     }
/* 343 */     float xa = xp / getMeshWidth() - xx;
/* 344 */     float ya = yp / getMeshWidth() - yy;
/* 345 */     if (xa < 0.0F)
/* 346 */       xa = -xa; 
/* 347 */     if (ya < 0.0F) {
/* 348 */       ya = -ya;
/*     */     }
/* 350 */     float height = 0.0F;
/*     */     
/* 352 */     if (xa > ya) {
/*     */       
/* 354 */       xa -= ya;
/* 355 */       xa /= 1.0F - ya;
/* 356 */       float xheight1 = getNode(xx, yy).getHeight() * (1.0F - xa) + getNode(xx + 1, yy).getHeight() * xa;
/*     */       
/* 358 */       float xheight2 = getNode(xx + 1, yy + 1).getHeight();
/* 359 */       height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*     */     }
/*     */     else {
/*     */       
/* 363 */       if (ya <= 0.001F)
/* 364 */         ya = 0.001F; 
/* 365 */       xa /= ya;
/*     */       
/* 367 */       float xheight1 = getNode(xx, yy).getHeight();
/* 368 */       float xheight2 = getNode(xx, yy + 1).getHeight() * (1.0F - xa) + getNode(xx + 1, yy + 1).getHeight() * xa;
/*     */       
/* 370 */       height = xheight1 * (1.0F - ya) + xheight2 * ya;
/*     */     } 
/*     */ 
/*     */     
/* 374 */     return height;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTiles(int xStart, int yStart, int w, int h, int[][] tiles) {
/* 379 */     boolean changed = false;
/*     */     
/* 381 */     for (int x = 0; x < w; x++) {
/*     */       
/* 383 */       for (int y = 0; y < h; y++) {
/*     */         
/* 385 */         Node node = getNode(x + xStart, y + yStart);
/* 386 */         node.setHeight(Tiles.decodeHeightAsFloat(tiles[x][y]));
/*     */         
/* 388 */         short t = (short)Tiles.decodeType(tiles[x][y]);
/* 389 */         short d = (short)Tiles.decodeData(tiles[x][y]);
/*     */         
/* 391 */         if (t != node.getTexture() || d != node.data) {
/*     */           
/* 393 */           node.setTexture((byte)t);
/* 394 */           node.setData((byte)d);
/* 395 */           changed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 400 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void refresh(int xStart, int yStart, int w, int h) {
/* 405 */     calculateNormals(xStart - 1, yStart - 1, xStart + w + 2, yStart + h + 2);
/* 406 */     processData(xStart - 1, yStart - 1, xStart + w + 2, yStart + h + 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 411 */     generateEmpty(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\Mesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */