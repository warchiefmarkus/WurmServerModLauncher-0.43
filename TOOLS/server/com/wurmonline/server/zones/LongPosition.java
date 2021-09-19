/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.shared.constants.EffectConstants;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LongPosition
/*     */   implements EffectConstants
/*     */ {
/*     */   private final long id;
/*     */   private final int tilex;
/*     */   private final int tiley;
/*     */   private final short effectType;
/*     */   
/*     */   LongPosition(long _id, int _tilex, int _tiley) {
/*  47 */     this.id = _id;
/*  48 */     this.tilex = _tilex;
/*  49 */     this.tiley = _tiley;
/*  50 */     this.effectType = getRandomEffectType(Server.rand);
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
/*     */   static short getRandomEffectType(Random randomSource) {
/*  62 */     return (short)(5 + randomSource.nextInt(5));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getId() {
/*  72 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTilex() {
/*  82 */     return this.tilex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTiley() {
/*  92 */     return this.tiley;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   short getEffectType() {
/* 102 */     return this.effectType;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\LongPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */