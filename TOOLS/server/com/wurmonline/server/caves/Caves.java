/*     */ package com.wurmonline.server.caves;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Caves
/*     */ {
/*     */   private static final float MAX_CAVE_SLOPE = 8.0F;
/*     */   
/*     */   public void digHoleAt(int xFrom, int yFrom, int xTarget, int yTarget, int slope) {
/*  33 */     if (!isRockExposed(xTarget, yTarget)) {
/*     */       
/*  35 */       System.out.println("You can't mine an entrance here.. There's too much dirt on the tile.");
/*     */       
/*     */       return;
/*     */     } 
/*  39 */     if (!isCaveWall(xFrom, yFrom)) {
/*     */       
/*  41 */       System.out.println("You can't mine an entrance here.. There's a tunnel in the way.");
/*     */       return;
/*     */     } 
/*     */     int x;
/*  45 */     for (x = xTarget - 1; x <= xTarget + 1; x++) {
/*  46 */       for (int y = yTarget - 1; y <= yTarget + 1; y++) {
/*  47 */         if (isTerrainHole(x, y)) {
/*     */           
/*  49 */           System.out.println("You can't mine an entrance here.. Too close to an existing entrance.");
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*  54 */     if (isMinable(xTarget, yTarget)) {
/*     */       
/*  56 */       for (x = xFrom; x <= xFrom + 1; x++) {
/*  57 */         for (int y = yFrom; y <= yFrom + 1; y++);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  66 */       mine(xFrom, yFrom, xTarget, yTarget, slope);
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
/*     */   public void mineAt(int xFrom, int yFrom, int xTarget, int yTarget, int slope) {
/*  86 */     if (!isMinable(xTarget, yTarget)) {
/*     */       
/*  88 */       System.out.println("You can't mine here.. There's a tunnel in the way.");
/*     */       
/*     */       return;
/*     */     } 
/*  92 */     if (!isCaveWall(xTarget, xTarget))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       mine(xFrom, yFrom, xTarget, yTarget, slope);
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
/*     */   private void mine(int xFrom, int yFrom, int xTarget, int yTarget, int slope) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isMinable(int xTarget, int yTarget) {
/* 142 */     float lowestFloor = 100000.0F;
/* 143 */     float highestFloor = -100000.0F;
/* 144 */     int tunnels = 0;
/*     */     
/* 146 */     for (int x = xTarget; x <= xTarget + 1; x++) {
/* 147 */       for (int y = yTarget; y <= yTarget + 1; y++) {
/*     */         
/* 149 */         if (isExitCorner(x, y)) {
/*     */           
/* 151 */           tunnels++;
/* 152 */           float h = getTerrainHeight(x, y);
/* 153 */           if (h < lowestFloor)
/* 154 */             lowestFloor = h; 
/* 155 */           if (h > highestFloor) {
/* 156 */             highestFloor = h;
/*     */           }
/* 158 */         } else if (isTunnelCorner(x, y)) {
/*     */           
/* 160 */           tunnels++;
/* 161 */           float h = getCaveFloorHeight(x, y);
/* 162 */           if (h < lowestFloor)
/* 163 */             lowestFloor = h; 
/* 164 */           if (h > highestFloor)
/* 165 */             highestFloor = h; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 169 */     if (tunnels == 0) {
/* 170 */       return true;
/*     */     }
/* 172 */     float diff = highestFloor - lowestFloor;
/*     */     
/* 174 */     return (diff < 8.0F);
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
/*     */   private boolean isTunnelCorner(int x, int y) {
/* 186 */     if (isCaveTunnel(x, y))
/* 187 */       return true; 
/* 188 */     if (isCaveTunnel(x - 1, y))
/* 189 */       return true; 
/* 190 */     if (isCaveTunnel(x - 1, y - 1))
/* 191 */       return true; 
/* 192 */     if (isCaveTunnel(x, y - 1))
/* 193 */       return true; 
/* 194 */     return false;
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
/*     */   private boolean isExitCorner(int x, int y) {
/* 206 */     if (isCaveExit(x, y))
/* 207 */       return true; 
/* 208 */     if (isCaveExit(x - 1, y))
/* 209 */       return true; 
/* 210 */     if (isCaveExit(x - 1, y - 1))
/* 211 */       return true; 
/* 212 */     if (isCaveExit(x, y - 1))
/* 213 */       return true; 
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getTerrainHeight(int x, int y) {
/* 220 */     return 10.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getCaveFloorHeight(int x, int y) {
/* 226 */     return 10.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCaveExit(int x, int y) {
/* 232 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCaveTunnel(int x, int y) {
/* 238 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCaveWall(int x, int y) {
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTerrainHole(int x, int y) {
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRockExposed(int x, int y) {
/* 256 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\caves\Caves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */