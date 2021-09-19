/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FaithZone
/*     */ {
/*     */   private final Deity[] activeGods;
/*     */   private final byte[] newFaiths;
/*     */   private short startX;
/*     */   private short startY;
/*     */   private short endX;
/*     */   private short endY;
/*  49 */   public Deity currentRuler = null;
/*  50 */   private int strength = 0;
/*     */   private static int nums;
/*  52 */   private int num = 0;
/*     */ 
/*     */   
/*  55 */   private static final Logger logger = Logger.getLogger(FaithZone.class.getName());
/*     */   
/*     */   private Item altar;
/*     */   
/*     */   private int areaQL;
/*     */   
/*     */   public FaithZone(Item altar) {
/*  62 */     this.altar = altar;
/*  63 */     updateArea();
/*     */     
/*  65 */     this.activeGods = null;
/*  66 */     this.newFaiths = null;
/*     */     
/*  68 */     this.currentRuler = altar.getBless();
/*     */   }
/*     */ 
/*     */   
/*     */   public FaithZone(short startx, short starty, short endx, short endy) {
/*  73 */     this.num = nums++;
/*  74 */     this.startX = startx;
/*  75 */     this.startY = starty;
/*  76 */     this.endX = endx;
/*  77 */     this.endY = endy;
/*     */     
/*  79 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*     */       
/*  81 */       this.activeGods = null;
/*  82 */       this.newFaiths = null;
/*     */     }
/*     */     else {
/*     */       
/*  86 */       this.activeGods = new Deity[4];
/*  87 */       this.newFaiths = new byte[4];
/*  88 */       for (int f = 0; f < 4; f++) {
/*     */         
/*  90 */         this.activeGods[f] = null;
/*  91 */         this.newFaiths[f] = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateArea() {
/*  99 */     this.areaQL = (int)this.altar.getCurrentQualityLevel();
/* 100 */     this.startX = (short)(this.altar.getTileX() - this.areaQL);
/* 101 */     this.startY = (short)(this.altar.getTileY() - this.areaQL);
/* 102 */     this.endX = (short)(this.altar.getTileX() + this.areaQL);
/* 103 */     this.endY = (short)(this.altar.getTileY() + this.areaQL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStrength() {
/* 113 */     return this.strength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrengthForTile(int tileX, int tileY, boolean surfaced) {
/* 118 */     return 
/* 119 */       (int)(this.altar.getCurrentQualityLevel() / ((surfaced == this.altar.isOnSurface()) ? true : 2) - Math.max(Math.abs(this.altar.getTileX() - tileX), Math.abs(this.altar.getTileY() - tileY)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsTile(int tileX, int tileY) {
/* 124 */     if (this.areaQL != (int)this.altar.getCurrentQualityLevel()) {
/* 125 */       updateArea();
/*     */     }
/* 127 */     return (tileX > this.startX && tileX < this.endX && tileY > this.startY && tileY < this.endY);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartX() {
/* 132 */     return this.startX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartY() {
/* 137 */     return this.startY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndX() {
/* 142 */     return this.endX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndY() {
/* 147 */     return this.endY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCenterX() {
/* 152 */     return this.startX + (this.endX - this.startX) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCenterY() {
/* 157 */     return this.startY + (this.endY - this.startY) / 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Deity getCurrentRuler() {
/* 167 */     if (Features.Feature.NEWDOMAINS.isEnabled()) {
/* 168 */       return this.altar.getBless();
/*     */     }
/* 170 */     return this.currentRuler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean poll() {
/* 178 */     if (this.num == 1)
/* 179 */       Zones.checkAltars(); 
/* 180 */     int max = 0;
/* 181 */     Deity newRuler = null;
/* 182 */     for (int f = 0; f < 4; f++) {
/*     */       
/* 184 */       if (this.activeGods[f] != null)
/*     */       {
/* 186 */         if (this.newFaiths[f] > 0 && this.newFaiths[f] >= max) {
/*     */           
/* 188 */           max = this.newFaiths[f];
/* 189 */           newRuler = this.activeGods[f];
/*     */         }
/* 191 */         else if (this.activeGods[f] == getCurrentRuler()) {
/* 192 */           this.currentRuler = null;
/*     */         }  } 
/* 194 */       this.newFaiths[f] = 0;
/* 195 */       this.activeGods[f] = null;
/*     */     } 
/*     */     
/* 198 */     boolean toReturn = false;
/* 199 */     if (getCurrentRuler() != newRuler) {
/*     */       
/* 201 */       this.currentRuler = newRuler;
/* 202 */       toReturn = true;
/*     */     } 
/* 204 */     if (getCurrentRuler() == null) {
/*     */       
/* 206 */       this.strength = 0;
/*     */     }
/*     */     else {
/*     */       
/* 210 */       this.strength = max;
/* 211 */       this.strength = Math.max(-100, this.strength);
/* 212 */       this.strength = Math.min(100, this.strength);
/*     */ 
/*     */       
/* 215 */       pollMycelium();
/*     */     } 
/* 217 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pollMycelium() {
/* 222 */     if (getCurrentRuler() == null) {
/*     */       return;
/*     */     }
/* 225 */     if ((getCurrentRuler()).number == 4)
/*     */     {
/* 227 */       if (Server.rand.nextInt(50) == 0) {
/*     */         
/* 229 */         int x = this.startX + Server.rand.nextInt(this.endX - this.startX);
/* 230 */         int y = this.startY + Server.rand.nextInt(this.endY - this.startY);
/* 231 */         if (Kingdoms.getKingdomTemplateFor(Zones.getKingdom(x, y)) == 3) {
/* 232 */           spawnMycelium(x, y);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static void spawnMycelium(int tilex, int tiley) {
/* 239 */     int tile = Server.surfaceMesh.getTile(tilex, tiley);
/* 240 */     if (Tiles.decodeHeight(tile) > 0) {
/*     */       
/* 242 */       byte type = Tiles.decodeType(tile);
/* 243 */       byte data = Tiles.decodeData(tile);
/* 244 */       Tiles.Tile theTile = Tiles.getTile(type);
/* 245 */       if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_KELP.id || type == Tiles.Tile.TILE_DIRT.id || type == Tiles.Tile.TILE_DIRT_PACKED.id) {
/*     */ 
/*     */         
/* 248 */         Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/* 249 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*     */       }
/* 251 */       else if (theTile.isNormalTree()) {
/*     */         
/* 253 */         byte newType = theTile.getTreeType(data).asMyceliumTree();
/* 254 */         Server.setSurfaceTile(tilex, tiley, 
/* 255 */             Tiles.decodeHeight(tile), newType, data);
/* 256 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
/*     */       }
/* 258 */       else if (theTile.isNormalBush()) {
/*     */         
/* 260 */         byte newType = theTile.getBushType(data).asMyceliumBush();
/* 261 */         Server.setSurfaceTile(tilex, tiley, 
/* 262 */             Tiles.decodeHeight(tile), newType, data);
/* 263 */         Players.getInstance().sendChangedTile(tilex, tiley, true, false);
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
/*     */   void addToFaith(Deity aDeity, int aStrength) {
/* 277 */     for (int f = 0; f < 4; f++) {
/*     */       
/* 279 */       if (this.activeGods[f] == null || this.activeGods[f] == aDeity) {
/*     */         
/* 281 */         this.activeGods[f] = aDeity;
/* 282 */         if (this.newFaiths[f] < aStrength)
/* 283 */           this.newFaiths[f] = (byte)Math.min(100, aStrength); 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\FaithZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */