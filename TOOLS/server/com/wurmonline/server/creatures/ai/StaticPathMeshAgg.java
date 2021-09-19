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
/*     */ final class StaticPathMeshAgg
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
/*     */   StaticPathMeshAgg(int startx, int starty, int endx, int endy, boolean surf, int borderSize, int[] aMesh) {
/*  74 */     this.surfaced = surf;
/*  75 */     int[] lMesh = aMesh;
/*  76 */     this.borderStartX = Math.max(0, Math.min(startx, endx) - borderSize);
/*  77 */     this.borderEndX = Math.min(WORLD_SIZE - 1, Math.max(startx, endx) + borderSize);
/*  78 */     this.borderStartY = Math.max(0, Math.min(starty, endy) - borderSize);
/*  79 */     this.borderEndY = Math.min(WORLD_SIZE - 1, Math.max(starty, endy) + borderSize);
/*  80 */     this.sizex = this.borderEndX - this.borderStartX + 1;
/*  81 */     this.sizey = this.borderEndY - this.borderStartY + 1;
/*  82 */     if (logger.isLoggable(Level.FINEST)) {
/*     */       
/*  84 */       logger.finest("PathMesh start-end: " + startx + "," + starty + "-" + endx + "," + endy);
/*  85 */       logger.finest("PathMesh border start-end: " + this.borderStartX + ", " + this.borderStartY + " - " + this.borderEndX + ", " + this.borderEndY + ", sz=" + this.sizex + "," + this.sizey);
/*     */     } 
/*     */     
/*  88 */     pathables = new PathTile[this.sizex][this.sizey];
/*  89 */     PathTile lStart = null;
/*  90 */     PathTile lFinish = null;
/*  91 */     for (int x = 0; x < this.sizex; x++) {
/*     */       
/*  93 */       for (int y = 0; y < this.sizey; y++) {
/*     */         
/*  95 */         pathables[x][y] = new PathTile(this.borderStartX + x, this.borderStartY + y, lMesh[this.borderStartX + x | this.borderStartY + y << Constants.meshSize], this.surfaced, (byte)(this.surfaced ? 0 : -1));
/*     */         
/*  97 */         if (pathables[x][y].getTileX() == startx && pathables[x][y].getTileY() == starty)
/*  98 */           lStart = pathables[x][y]; 
/*  99 */         if (pathables[x][y].getTileX() == endx && pathables[x][y].getTileY() == endy)
/* 100 */           lFinish = pathables[x][y]; 
/*     */       } 
/*     */     } 
/* 103 */     this.start = lStart;
/* 104 */     this.finish = lFinish;
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
/*     */   StaticPathMeshAgg(int startx, int starty, int endx, int endy, boolean surf, int borderSize) {
/* 125 */     this(startx, starty, endx, endy, surf, borderSize, surf ? Server.surfaceMesh.data : Server.caveMesh.data);
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
/* 137 */     int diffX = realx - this.borderStartX;
/* 138 */     int diffY = realy - this.borderStartY;
/* 139 */     return pathables[diffX][diffY];
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(int realx, int realy) {
/* 144 */     return (realx >= this.borderStartX && realx <= this.borderEndX && realy >= this.borderStartY && realy <= this.borderEndY);
/*     */   }
/*     */ 
/*     */   
/*     */   PathTile[] getAdjacent(PathTile p) {
/* 149 */     PathTile[] surrPathables = new PathTile[4];
/*     */     
/* 151 */     int x = p.getTileX();
/* 152 */     int y = p.getTileY();
/*     */     
/* 154 */     int minOneX = x - 1;
/* 155 */     int plusOneX = x + 1;
/*     */     
/* 157 */     int minOneY = y - 1;
/* 158 */     int plusOneY = y + 1;
/*     */ 
/*     */     
/* 161 */     if (minOneX >= this.borderStartX)
/*     */     {
/* 163 */       surrPathables[3] = getPathTile(minOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (plusOneX < this.borderEndX)
/*     */     {
/* 172 */       surrPathables[1] = getPathTile(plusOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (minOneY >= this.borderStartY)
/* 179 */       surrPathables[0] = getPathTile(x, minOneY); 
/* 180 */     if (plusOneY < this.borderEndY) {
/* 181 */       surrPathables[2] = getPathTile(x, plusOneY);
/*     */     }
/* 183 */     return surrPathables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getStart() {
/* 193 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getFinish() {
/* 203 */     return this.finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clearPathables() {
/* 211 */     pathables = (PathTile[][])null;
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
/* 230 */     return this.sizex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSizey() {
/* 240 */     return this.sizey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartX() {
/* 250 */     return this.borderStartX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndX() {
/* 260 */     return this.borderEndX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartY() {
/* 270 */     return this.borderStartY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndY() {
/* 280 */     return this.borderEndY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSurfaced() {
/* 290 */     return this.surfaced;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\StaticPathMeshAgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */