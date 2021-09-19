/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.math.TilePos;
/*     */ import com.wurmonline.math.Vector2f;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CoordUtils
/*     */ {
/*     */   public static int WorldToTile(float woldPos) {
/*  33 */     return (int)woldPos >> 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static TilePos WorldToTile(float woldPosX, float woldPosY) {
/*  44 */     return TilePos.fromXY(WorldToTile(woldPosX), WorldToTile(woldPosY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static TilePos WorldToTile(Vector2f woldPos) {
/*  55 */     return TilePos.fromXY(WorldToTile(woldPos.x), WorldToTile(woldPos.y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float TileToWorld(int tilePos) {
/*  64 */     return (tilePos << 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Vector2f TileToWorld(@Nonnull TilePos tilePos) {
/*  72 */     return new Vector2f(TileToWorld(tilePos.x), TileToWorld(tilePos.y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float TileToWorldTileCenter(int tilePos) {
/*  81 */     return ((tilePos << 2) + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Vector2f TileToWorldTileCenter(@Nonnull TilePos tilePos) {
/*  90 */     return new Vector2f(TileToWorldTileCenter(tilePos.x), 
/*  91 */         TileToWorldTileCenter(tilePos.y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int TileToWorldInt(int tilePos) {
/* 100 */     return tilePos << 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\CoordUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */