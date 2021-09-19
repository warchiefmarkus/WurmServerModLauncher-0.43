/*     */ package com.wurmonline.mesh;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CaveMesh
/*     */   extends Mesh
/*     */ {
/*     */   public CaveMesh(Mesh mesh, int width, int height) {
/*  24 */     super(width, height, mesh.getMeshWidth());
/*     */     
/*  26 */     for (int x = 0; x < width; x++) {
/*     */       
/*  28 */       for (int y = 0; y < height; y++) {
/*     */         
/*  30 */         this.nodes[x][y] = new CaveNode();
/*  31 */         this.nodes[x][y].setTexture(CaveTile.TILE_ROCK.id);
/*  32 */         ((CaveNode)this.nodes[x][y]).setCeilingTexture(CaveTile.TILE_ROCK.id);
/*     */         
/*  34 */         this.nodes[x][y].setHeight((float)(Math.random() * Math.random() * Math.random()) * 100.0F + 0.2F);
/*  35 */         this.nodes[x][y].setNormals(new float[3]);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     processData();
/* 119 */     calculateNormals();
/*     */   }
/*     */ 
/*     */   
/*     */   public CaveNode getCaveNode(int x, int y) {
/* 124 */     return (CaveNode)getNode(x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public CaveMesh(int width, int height, int meshWidth) {
/* 129 */     super(width, height, meshWidth);
/* 130 */     setWraparound();
/*     */     
/* 132 */     for (int x = 0; x < width; x++) {
/*     */       
/* 134 */       for (int y = 0; y < width; y++) {
/*     */         
/* 136 */         this.nodes[x][y] = new CaveNode();
/* 137 */         this.nodes[x][y].setTexture(CaveTile.TILE_ROCK.id);
/* 138 */         getCaveNode(x, y).setCeilingTexture(CaveTile.TILE_ROCK.id);
/* 139 */         getCaveNode(x, y).setHeight(Float.MAX_VALUE);
/* 140 */         getCaveNode(x, y).setData(0.0F);
/* 141 */         getCaveNode(x, y).setSpecial(1);
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     processData();
/* 146 */     calculateNormals();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTransition(float xp, float yp) {
/* 152 */     while (xp < 0.0F)
/* 153 */       xp += (getWidth() * getMeshWidth()); 
/* 154 */     while (yp < 0.0F) {
/* 155 */       yp += (getHeight() * getMeshWidth());
/*     */     }
/* 157 */     int xx = (int)(xp / getMeshWidth());
/* 158 */     int yy = (int)(yp / getMeshWidth());
/*     */     
/* 160 */     return (getCaveNode(xx, yy).getSpecial() != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void processData(int x1, int y1, int x2, int y2) {
/* 166 */     for (int x = x1; x < x2; x++) {
/*     */       
/* 168 */       for (int y = y1; y < y2; y++) {
/*     */         
/* 170 */         Node node = getNode(x, y);
/* 171 */         float b = node.getHeight();
/* 172 */         float t = node.getHeight() + getCaveNode(x, y).getData();
/*     */ 
/*     */         
/* 175 */         if (getNode(x + 1, y).getHeight() < b)
/* 176 */           b = getNode(x + 1, y).getHeight(); 
/* 177 */         if (getNode(x + 1, y).getHeight() + getCaveNode(x + 1, y).getData() > t) {
/* 178 */           t = getNode(x + 1, y).getHeight() + getCaveNode(x + 1, y).getData();
/*     */         }
/* 180 */         if (getNode(x + 1, y + 1).getHeight() < b)
/* 181 */           b = getNode(x + 1, y + 1).getHeight(); 
/* 182 */         if (getNode(x + 1, y + 1).getHeight() + getCaveNode(x + 1, y + 1).getData() > t) {
/* 183 */           t = getNode(x + 1, y + 1).getHeight() + getCaveNode(x + 1, y + 1).getData();
/*     */         }
/* 185 */         if (getNode(x, y + 1).getHeight() < b)
/* 186 */           b = getNode(x, y + 1).getHeight(); 
/* 187 */         if (getNode(x, y + 1).getHeight() + getCaveNode(x, y + 1).getData() > t) {
/* 188 */           t = getNode(x, y + 1).getHeight() + getCaveNode(x, y + 1).getData();
/*     */         }
/*     */         
/* 191 */         float h = t - b;
/*     */         
/* 193 */         node.setBbBottom(b);
/* 194 */         node.setBbHeight(h);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void calculateNormals(int x1, int y1, int x2, int y2) {
/* 202 */     for (int x = x1; x < x2; x++) {
/*     */       
/* 204 */       for (int y = y1; y < y2; y++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 209 */         CaveNode n1 = getCaveNode(x, y);
/*     */         
/* 211 */         float v1x = getMeshWidth();
/* 212 */         float v1y = getNode(x + 1, y).getHeight() - n1.getHeight();
/* 213 */         float v1z = 0.0F;
/*     */         
/* 215 */         float v2x = 0.0F;
/* 216 */         float v2y = getNode(x, y + 1).getHeight() - n1.getHeight();
/* 217 */         float v2z = getMeshWidth();
/*     */         
/* 219 */         float vx = v1y * v2z - v1z * v2y;
/* 220 */         float vy = v1z * v2x - v1x * v2z;
/* 221 */         float vz = v1x * v2y - v1y * v2x;
/*     */         
/* 223 */         v1x = -getMeshWidth();
/* 224 */         v1y = getNode(x - 1, y).getHeight() - n1.getHeight();
/* 225 */         v1z = 0.0F;
/*     */         
/* 227 */         v2x = 0.0F;
/* 228 */         v2y = getNode(x, y - 1).getHeight() - n1.getHeight();
/* 229 */         v2z = -getMeshWidth();
/*     */         
/* 231 */         vx += v1y * v2z - v1z * v2y;
/* 232 */         vy += v1z * v2x - v1x * v2z;
/* 233 */         vz += v1x * v2y - v1y * v2x;
/*     */         
/* 235 */         float dist = (float)Math.sqrt((vx * vx + vy * vy + vz * vz));
/* 236 */         vx /= dist;
/* 237 */         vy /= dist;
/* 238 */         vz /= dist;
/*     */         
/* 240 */         n1.normals[0] = -vx;
/* 241 */         n1.normals[1] = -vy;
/* 242 */         n1.normals[2] = -vz;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setTiles(int xStart, int yStart, int w, int h, int[][] tiles) {
/* 269 */     for (int x = 0; x < w; x++) {
/*     */       
/* 271 */       for (int y = 0; y < h; y++) {
/*     */         
/* 273 */         CaveNode node = getCaveNode(x + xStart, y + yStart);
/* 274 */         node.setHeight(CaveTile.decodeHeightAsFloat(tiles[x][y]));
/* 275 */         node.setTexture(CaveTile.decodeFloorTexture(tiles[x][y]));
/*     */         
/* 277 */         node.setCeilingTexture(CaveTile.decodeCeilingTexture(tiles[x][y]));
/* 278 */         node.setData(CaveTile.decodeCeilingHeightAsFloat(tiles[x][y]));
/* 279 */         node.setSpecial(0);
/* 280 */         if (node.getData() < 0.0F) {
/*     */           
/* 282 */           node.setData(-node.getData());
/* 283 */           node.setSpecial(1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 288 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\CaveMesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */