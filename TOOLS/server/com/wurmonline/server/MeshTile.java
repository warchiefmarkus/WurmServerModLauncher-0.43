/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MeshTile
/*     */   implements MiscConstants
/*     */ {
/*     */   final MeshIO mesh;
/*     */   final int tilex;
/*     */   final int tiley;
/*     */   final int currentTile;
/*     */   final short currentHeight;
/*     */   
/*     */   public MeshTile(MeshIO mesh, int tilex, int tiley) {
/*  46 */     this.mesh = mesh;
/*  47 */     this.tilex = Zones.safeTileX(tilex);
/*  48 */     this.tiley = Zones.safeTileY(tiley);
/*  49 */     this.currentTile = mesh.getTile(this.tilex, this.tiley);
/*  50 */     this.currentHeight = Tiles.decodeHeight(this.currentTile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEncodedTile() {
/*  59 */     return this.currentTile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getTileType() {
/*  68 */     return Tiles.decodeType(this.currentTile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getTileHeight() {
/*  77 */     return this.currentHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeightAsFloat() {
/*  86 */     return this.currentHeight / 10.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getTileData() {
/*  95 */     return Tiles.decodeData(this.currentTile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHole() {
/* 104 */     return (getTileType() == Tiles.Tile.TILE_HOLE.id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLowerLip() {
/* 113 */     if (getNorthSlope() == 0)
/*     */     {
/*     */       
/* 116 */       if (getWestSlope() > 0 && getEastSlope() > 0)
/* 117 */         return 0; 
/*     */     }
/* 119 */     if (getSouthSlope() == 0)
/*     */     {
/*     */       
/* 122 */       if (getWestSlope() < 0 && getEastSlope() < 0)
/* 123 */         return 4; 
/*     */     }
/* 125 */     if (getWestSlope() == 0)
/*     */     {
/*     */       
/* 128 */       if (getNorthSlope() > 0 && getSouthSlope() > 0)
/* 129 */         return 6; 
/*     */     }
/* 131 */     if (getEastSlope() == 0)
/*     */     {
/*     */       
/* 134 */       if (getNorthSlope() < 0 && getSouthSlope() < 0) {
/* 135 */         return 2;
/*     */       }
/*     */     }
/* 138 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getWestSlope() {
/* 147 */     int southTile = this.mesh.getTile(this.tilex, this.tiley + 1);
/* 148 */     short heightSW = Tiles.decodeHeight(southTile);
/* 149 */     return (short)(heightSW - this.currentHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getNorthSlope() {
/* 158 */     int eastTile = this.mesh.getTile(this.tilex + 1, this.tiley);
/* 159 */     short heightNE = Tiles.decodeHeight(eastTile);
/* 160 */     return (short)(heightNE - this.currentHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getEastSlope() {
/* 169 */     int eastTile = this.mesh.getTile(this.tilex + 1, this.tiley);
/* 170 */     short heightNE = Tiles.decodeHeight(eastTile);
/* 171 */     int southEastTile = this.mesh.getTile(this.tilex + 1, this.tiley + 1);
/* 172 */     short heightSE = Tiles.decodeHeight(southEastTile);
/* 173 */     return (short)(heightSE - heightNE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getSouthSlope() {
/* 182 */     int southEastTile = this.mesh.getTile(this.tilex + 1, this.tiley + 1);
/* 183 */     short heightSE = Tiles.decodeHeight(southEastTile);
/* 184 */     int southTile = this.mesh.getTile(this.tilex, this.tiley + 1);
/* 185 */     short heightSW = Tiles.decodeHeight(southTile);
/* 186 */     return (short)(heightSE - heightSW);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkSlopes(int maxStraight, int maxDiagonal) {
/* 195 */     if (Math.abs(getNorthSlope()) > maxStraight || 
/* 196 */       Math.abs(getSouthSlope()) > maxStraight || 
/* 197 */       Math.abs(getEastSlope()) > maxStraight || 
/* 198 */       Math.abs(getWestSlope()) > maxStraight) {
/* 199 */       return true;
/*     */     }
/* 201 */     int southEastTile = this.mesh.getTile(this.tilex + 1, this.tiley + 1);
/* 202 */     short heightSE = Tiles.decodeHeight(southEastTile);
/* 203 */     if (Math.abs(this.currentHeight - heightSE) > maxDiagonal) {
/* 204 */       return true;
/*     */     }
/* 206 */     int southTile = this.mesh.getTile(this.tilex, this.tiley + 1);
/* 207 */     short heightS = Tiles.decodeHeight(southTile);
/* 208 */     int eastTile = this.mesh.getTile(this.tilex + 1, this.tiley);
/* 209 */     short heightE = Tiles.decodeHeight(eastTile);
/* 210 */     if (Math.abs(heightE - heightS) > maxDiagonal) {
/* 211 */       return true;
/*     */     }
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFlat() {
/* 223 */     return (getNorthSlope() == 0 && getSouthSlope() == 0 && getEastSlope() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getNorthMeshTile() {
/* 232 */     return new MeshTile(this.mesh, this.tilex, this.tiley - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getNorthWestMeshTile() {
/* 241 */     return new MeshTile(this.mesh, this.tilex - 1, this.tiley - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getWestMeshTile() {
/* 250 */     return new MeshTile(this.mesh, this.tilex - 1, this.tiley);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getSouthWestMeshTile() {
/* 259 */     return new MeshTile(this.mesh, this.tilex - 1, this.tiley + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getSouthMeshTile() {
/* 268 */     return new MeshTile(this.mesh, this.tilex, this.tiley + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getSouthEastMeshTile() {
/* 277 */     return new MeshTile(this.mesh, this.tilex + 1, this.tiley + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getEastMeshTile() {
/* 286 */     return new MeshTile(this.mesh, this.tilex + 1, this.tiley);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshTile getNorthEastMeshTile() {
/* 295 */     return new MeshTile(this.mesh, this.tilex + 1, this.tiley + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnder(int height) {
/* 305 */     if (this.currentHeight <= height)
/* 306 */       return true; 
/* 307 */     if (getSouthMeshTile().getTileHeight() <= height)
/* 308 */       return true; 
/* 309 */     if (getEastMeshTile().getTileHeight() <= height)
/* 310 */       return true; 
/* 311 */     if (getSouthEastMeshTile().getTileHeight() <= height)
/* 312 */       return true; 
/* 313 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\MeshTile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */