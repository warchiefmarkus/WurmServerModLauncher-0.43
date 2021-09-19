/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.CreatureMove;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MovementEntity
/*     */ {
/*     */   private long wurmid;
/*     */   private long creatorId;
/*     */   private CreatureMove movePosition;
/*     */   private final long expireTime;
/*     */   
/*     */   public MovementEntity(long creatorWurmId, long _expireTime) {
/*  40 */     setWurmid(WurmId.getNextIllusionId());
/*  41 */     setCreatorId(creatorWurmId);
/*  42 */     this.expireTime = _expireTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExpire() {
/*  47 */     return (System.currentTimeMillis() > this.expireTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getWurmid() {
/*  57 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void setWurmid(long aWurmid) {
/*  68 */     this.wurmid = aWurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureMove getMovePosition() {
/*  78 */     return this.movePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMovePosition(CreatureMove aMovePosition) {
/*  89 */     this.movePosition = aMovePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getCreatorId() {
/*  99 */     return this.creatorId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void setCreatorId(long aCreatorId) {
/* 110 */     this.creatorId = aCreatorId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void checkIfChangeDirection() {
/* 115 */     if (Server.rand.nextInt(10) == 0) {
/*     */       
/* 117 */       this.movePosition.diffX = (byte)(-3 + Server.rand.nextInt(7));
/* 118 */       this.movePosition.diffY = (byte)(-3 + Server.rand.nextInt(7));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\MovementEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */