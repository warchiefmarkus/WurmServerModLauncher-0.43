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
/*     */ final class PathMesh
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(PathMesh.class.getName());
/*     */ 
/*     */   
/*     */   private final PathTile start;
/*     */   
/*     */   private final PathTile finish;
/*     */   
/*     */   private PathTile[][] pathables;
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
/*     */   private final int borderEndY;
/*     */ 
/*     */   
/*     */   private final boolean surfaced;
/*     */ 
/*     */ 
/*     */   
/*     */   PathMesh(int startx, int starty, int endx, int endy, boolean surf, int borderSize, int[] aMesh) {
/*  73 */     this.surfaced = surf;
/*  74 */     int[] lMesh = aMesh;
/*  75 */     this.borderStartX = Math.max(0, Math.min(startx, endx) - borderSize);
/*  76 */     this.borderEndX = Math.min(WORLD_SIZE - 1, Math.max(startx, endx) + borderSize);
/*  77 */     this.borderStartY = Math.max(0, Math.min(starty, endy) - borderSize);
/*  78 */     this.borderEndY = Math.min(WORLD_SIZE - 1, Math.max(starty, endy) + borderSize);
/*  79 */     this.sizex = this.borderEndX - this.borderStartX + 1;
/*  80 */     this.sizey = this.borderEndY - this.borderStartY + 1;
/*  81 */     if (logger.isLoggable(Level.FINEST)) {
/*     */       
/*  83 */       logger.finest("PathMesh start-end: " + startx + "," + starty + "-" + endx + "," + endy);
/*  84 */       logger.finest("PathMesh border start-end: " + this.borderStartX + ", " + this.borderStartY + " - " + this.borderEndX + ", " + this.borderEndY + ", sz=" + this.sizex + "," + this.sizey);
/*     */     } 
/*     */     
/*  87 */     this.pathables = new PathTile[this.sizex][this.sizey];
/*  88 */     PathTile lStart = null;
/*  89 */     PathTile lFinish = null;
/*  90 */     for (int x = 0; x < this.sizex; x++) {
/*     */       
/*  92 */       for (int y = 0; y < this.sizey; y++) {
/*     */         
/*  94 */         this.pathables[x][y] = new PathTile(this.borderStartX + x, this.borderStartY + y, lMesh[this.borderStartX + x | this.borderStartY + y << Constants.meshSize], this.surfaced, (byte)(this.surfaced ? 0 : -1));
/*     */         
/*  96 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/*  98 */           logger.finest("pathables: " + this.pathables[x][y].toString());
/*     */         }
/* 100 */         if (this.pathables[x][y].getTileX() == startx && this.pathables[x][y].getTileY() == starty)
/* 101 */           lStart = this.pathables[x][y]; 
/* 102 */         if (this.pathables[x][y].getTileX() == endx && this.pathables[x][y].getTileY() == endy)
/* 103 */           lFinish = this.pathables[x][y]; 
/*     */       } 
/*     */     } 
/* 106 */     this.start = lStart;
/* 107 */     this.finish = lFinish;
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
/*     */   PathMesh(int startx, int starty, int endx, int endy, boolean surf, int borderSize) {
/* 127 */     this(startx, starty, endx, endy, surf, borderSize, surf ? Server.surfaceMesh.data : Server.caveMesh.data);
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
/* 139 */     int diffX = realx - this.borderStartX;
/* 140 */     int diffY = realy - this.borderStartY;
/* 141 */     return this.pathables[diffX][diffY];
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(int realx, int realy) {
/* 146 */     return (realx >= this.borderStartX && realx <= this.borderEndX && realy >= this.borderStartY && realy <= this.borderEndY);
/*     */   }
/*     */ 
/*     */   
/*     */   PathTile[] getAdjacent(PathTile p) {
/* 151 */     PathTile[] surrPathables = new PathTile[4];
/*     */     
/* 153 */     int x = p.getTileX();
/* 154 */     int y = p.getTileY();
/*     */     
/* 156 */     int minOneX = x - 1;
/* 157 */     int plusOneX = x + 1;
/*     */     
/* 159 */     int minOneY = y - 1;
/* 160 */     int plusOneY = y + 1;
/*     */ 
/*     */     
/* 163 */     if (minOneX >= this.borderStartX)
/*     */     {
/* 165 */       surrPathables[3] = getPathTile(minOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (plusOneX < this.borderEndX)
/*     */     {
/* 174 */       surrPathables[1] = getPathTile(plusOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     if (minOneY >= this.borderStartY)
/* 181 */       surrPathables[0] = getPathTile(x, minOneY); 
/* 182 */     if (plusOneY < this.borderEndY) {
/* 183 */       surrPathables[2] = getPathTile(x, plusOneY);
/*     */     }
/* 185 */     return surrPathables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getStart() {
/* 195 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getFinish() {
/* 205 */     return this.finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearPathables() {
/* 213 */     this.pathables = (PathTile[][])null;
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
/* 232 */     return this.sizex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSizey() {
/* 242 */     return this.sizey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartX() {
/* 252 */     return this.borderStartX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndX() {
/* 262 */     return this.borderEndX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartY() {
/* 272 */     return this.borderStartY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndY() {
/* 282 */     return this.borderEndY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSurfaced() {
/* 292 */     return this.surfaced;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\PathMesh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */