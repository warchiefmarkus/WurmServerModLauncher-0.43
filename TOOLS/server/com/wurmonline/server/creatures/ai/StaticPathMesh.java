/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
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
/*     */ final class StaticPathMesh
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(PathMesh.class.getName());
/*     */ 
/*     */   
/*     */   private final PathTile start;
/*     */   
/*     */   private final PathTile finish;
/*     */   
/*     */   private static PathTile[][] pathables;
/*     */   
/*  42 */   private static final int WORLD_SIZE = 1 << Constants.meshSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int sizex;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int sizey;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int borderStartX;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int borderEndX;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int borderStartY;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int borderEndY;
/*     */ 
/*     */   
/*     */   private final boolean surfaced;
/*     */ 
/*     */ 
/*     */   
/*     */   StaticPathMesh(int startx, int starty, int endx, int endy, boolean surf, int borderSize, int[] aMesh) {
/*  74 */     this.surfaced = surf;
/*  75 */     int[] lMesh = aMesh;
/*  76 */     this.borderStartX = Math.max(0, Math.min(startx, endx) - borderSize);
/*  77 */     this.borderEndX = Math.min(WORLD_SIZE - 1, Math.max(startx, endx) + borderSize);
/*  78 */     this.borderStartY = Math.max(0, Math.min(starty, endy) - borderSize);
/*  79 */     this.borderEndY = Math.min(WORLD_SIZE - 1, Math.max(starty, endy) + borderSize);
/*  80 */     this.sizex = this.borderEndX - this.borderStartX + 1;
/*  81 */     this.sizey = this.borderEndY - this.borderStartY + 1;
/*     */ 
/*     */ 
/*     */     
/*  85 */     if (logger.isLoggable(Level.FINEST)) {
/*     */       
/*  87 */       logger.finest("PathMesh start-end: " + startx + "," + starty + "-" + endx + "," + endy);
/*  88 */       logger.finest("PathMesh border start-end: " + this.borderStartX + ", " + this.borderStartY + " - " + this.borderEndX + ", " + this.borderEndY + ", sz=" + this.sizex + "," + this.sizey);
/*     */     } 
/*     */     
/*  91 */     pathables = new PathTile[this.sizex][this.sizey];
/*  92 */     PathTile lStart = null;
/*  93 */     PathTile lFinish = null;
/*     */     
/*  95 */     for (int y = 0; y < this.sizey; y++) {
/*     */       
/*  97 */       for (int x = 0; x < this.sizex; x++) {
/*     */         
/*  99 */         pathables[x][y] = new PathTile(this.borderStartX + x, this.borderStartY + y, lMesh[this.borderStartX + x | this.borderStartY + y << Constants.meshSize], this.surfaced, (byte)(this.surfaced ? 0 : -1));
/*     */ 
/*     */         
/* 102 */         if (pathables[x][y].getTileX() == startx && pathables[x][y].getTileY() == starty)
/*     */         {
/* 104 */           lStart = pathables[x][y];
/*     */         }
/*     */         
/* 107 */         if (pathables[x][y].getTileX() == endx && pathables[x][y].getTileY() == endy)
/*     */         {
/* 109 */           lFinish = pathables[x][y];
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.start = lStart;
/* 119 */     this.finish = lFinish;
/* 120 */     if (this.start == this.finish) {
/* 121 */       logger.log(Level.INFO, "START=FINISH!");
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
/*     */   StaticPathMesh(int startx, int starty, int endx, int endy, boolean surf, int borderSize) {
/* 141 */     this(startx, starty, endx, endy, surf, borderSize, surf ? Server.surfaceMesh.data : Server.caveMesh.data);
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
/*     */   PathTile getPathTile(int realx, int realy) {
/* 153 */     int diffX = realx - this.borderStartX;
/* 154 */     int diffY = realy - this.borderStartY;
/* 155 */     return pathables[diffX][diffY];
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(int realx, int realy) {
/* 160 */     return (realx >= this.borderStartX && realx <= this.borderEndX && realy >= this.borderStartY && realy <= this.borderEndY);
/*     */   }
/*     */ 
/*     */   
/*     */   PathTile[] getAdjacent(PathTile p) {
/* 165 */     PathTile[] surrPathables = new PathTile[4];
/*     */     
/* 167 */     int x = p.getTileX();
/* 168 */     int y = p.getTileY();
/*     */     
/* 170 */     int minOneX = x - 1;
/* 171 */     int plusOneX = x + 1;
/*     */     
/* 173 */     int minOneY = y - 1;
/* 174 */     int plusOneY = y + 1;
/*     */ 
/*     */     
/* 177 */     if (minOneX >= this.borderStartX)
/*     */     {
/* 179 */       surrPathables[3] = getPathTile(minOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     if (plusOneX < this.borderEndX)
/*     */     {
/* 188 */       surrPathables[1] = getPathTile(plusOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (minOneY >= this.borderStartY)
/* 195 */       surrPathables[0] = getPathTile(x, minOneY); 
/* 196 */     if (plusOneY < this.borderEndY) {
/* 197 */       surrPathables[2] = getPathTile(x, plusOneY);
/*     */     }
/* 199 */     return surrPathables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getStart() {
/* 209 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getFinish() {
/* 219 */     return this.finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clearPathables() {
/* 227 */     pathables = (PathTile[][])null;
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
/*     */   int getSizex() {
/* 246 */     return this.sizex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSizey() {
/* 256 */     return this.sizey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartX() {
/* 266 */     return this.borderStartX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndX() {
/* 276 */     return this.borderEndX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartY() {
/* 286 */     return this.borderStartY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndY() {
/* 296 */     return this.borderEndY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSurfaced() {
/* 306 */     return this.surfaced;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\StaticPathMesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */