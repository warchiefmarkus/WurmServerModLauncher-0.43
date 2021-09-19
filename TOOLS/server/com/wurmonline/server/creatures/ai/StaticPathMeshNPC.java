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
/*     */ final class StaticPathMeshNPC
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
/*     */   StaticPathMeshNPC(int startx, int starty, int endx, int endy, boolean surf, int borderSize, int[] aMesh) {
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
/*  97 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/*  99 */           logger.finest("pathables: " + pathables[x][y].toString());
/*     */         }
/* 101 */         if (pathables[x][y].getTileX() == startx && pathables[x][y].getTileY() == starty)
/* 102 */           lStart = pathables[x][y]; 
/* 103 */         if (pathables[x][y].getTileX() == endx && pathables[x][y].getTileY() == endy)
/* 104 */           lFinish = pathables[x][y]; 
/*     */       } 
/*     */     } 
/* 107 */     this.start = lStart;
/* 108 */     this.finish = lFinish;
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
/*     */   StaticPathMeshNPC(int startx, int starty, int endx, int endy, boolean surf, int borderSize) {
/* 129 */     this(startx, starty, endx, endy, surf, borderSize, surf ? Server.surfaceMesh.data : Server.caveMesh.data);
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
/* 141 */     int diffX = realx - this.borderStartX;
/* 142 */     int diffY = realy - this.borderStartY;
/* 143 */     return pathables[diffX][diffY];
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(int realx, int realy) {
/* 148 */     return (realx >= this.borderStartX && realx <= this.borderEndX && realy >= this.borderStartY && realy <= this.borderEndY);
/*     */   }
/*     */ 
/*     */   
/*     */   PathTile[] getAdjacent(PathTile p) {
/* 153 */     PathTile[] surrPathables = new PathTile[4];
/*     */     
/* 155 */     int x = p.getTileX();
/* 156 */     int y = p.getTileY();
/*     */     
/* 158 */     int minOneX = x - 1;
/* 159 */     int plusOneX = x + 1;
/*     */     
/* 161 */     int minOneY = y - 1;
/* 162 */     int plusOneY = y + 1;
/*     */ 
/*     */     
/* 165 */     if (minOneX >= this.borderStartX)
/*     */     {
/* 167 */       surrPathables[3] = getPathTile(minOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     if (plusOneX < this.borderEndX)
/*     */     {
/* 176 */       surrPathables[1] = getPathTile(plusOneX, y);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     if (minOneY >= this.borderStartY)
/* 183 */       surrPathables[0] = getPathTile(x, minOneY); 
/* 184 */     if (plusOneY < this.borderEndY) {
/* 185 */       surrPathables[2] = getPathTile(x, plusOneY);
/*     */     }
/* 187 */     return surrPathables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getStart() {
/* 197 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathTile getFinish() {
/* 207 */     return this.finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clearPathables() {
/* 215 */     pathables = (PathTile[][])null;
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
/* 234 */     return this.sizex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSizey() {
/* 244 */     return this.sizey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartX() {
/* 254 */     return this.borderStartX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndX() {
/* 264 */     return this.borderEndX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderStartY() {
/* 274 */     return this.borderStartY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getBorderEndY() {
/* 284 */     return this.borderEndY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSurfaced() {
/* 294 */     return this.surfaced;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\StaticPathMeshNPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */