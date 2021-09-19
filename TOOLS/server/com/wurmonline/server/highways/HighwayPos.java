/*     */ package com.wurmonline.server.highways;
/*     */ 
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HighwayPos
/*     */ {
/*     */   private int tilex;
/*     */   private int tiley;
/*     */   private boolean onSurface;
/*     */   private BridgePart bridgePart;
/*     */   private Floor floor;
/*     */   
/*     */   public HighwayPos(int tilex, int tiley, boolean onSurface, @Nullable BridgePart bridgePart, @Nullable Floor floor) {
/*  53 */     this.tilex = tilex;
/*  54 */     this.tiley = tiley;
/*  55 */     this.onSurface = onSurface;
/*  56 */     this.bridgePart = bridgePart;
/*  57 */     this.floor = floor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTilex() {
/*  62 */     return this.tilex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTiley() {
/*  67 */     return this.tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnSurface() {
/*  72 */     return this.onSurface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSurfaceTile() {
/*  77 */     return (this.onSurface && this.bridgePart == null && this.floor == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCaveTile() {
/*  82 */     return (!this.onSurface && this.bridgePart == null && this.floor == null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BridgePart getBridgePart() {
/*  88 */     return this.bridgePart;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Floor getFloor() {
/*  94 */     return this.floor;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBridgeId() {
/*  99 */     if (this.bridgePart == null)
/* 100 */       return -10L; 
/* 101 */     return this.bridgePart.getStructureId();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFloorLevel() {
/* 106 */     if (this.floor == null)
/* 107 */       return 0; 
/* 108 */     return this.floor.getFloorLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(int tilex) {
/* 114 */     this.tilex = tilex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(int tiley) {
/* 119 */     this.tiley = tiley;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnSurface(boolean onSurface) {
/* 124 */     this.onSurface = onSurface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBridgePart(@Nullable BridgePart bridgePart) {
/* 129 */     this.bridgePart = bridgePart;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFloor(@Nullable Floor floor) {
/* 134 */     this.floor = floor;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\highways\HighwayPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */