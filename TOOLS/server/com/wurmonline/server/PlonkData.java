/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum PlonkData
/*     */ {
/*  29 */   FIRST_DAMAGE((short)1, 12),
/*  30 */   LOW_STAMINA((short)2, 13),
/*  31 */   THIRSTY((short)3, 15),
/*  32 */   HUNGRY((short)4, 14),
/*  33 */   FALL_DAMAGE((short)9, 16),
/*  34 */   DEATH((short)11, 18),
/*  35 */   SWIMMING((short)12, 17),
/*  36 */   ENCUMBERED((short)13, 20),
/*  37 */   ON_A_BOAT((short)14, 19),
/*  38 */   TREE_ACTIONS((short)16, 22),
/*     */   
/*  40 */   BOAT_SECURITY((short)48, 23);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final short plonkId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int flagBit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int flagColumn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PlonkData(short _plonkId, int _flagBit, int _flagColumn) {
/*  81 */     this.plonkId = _plonkId;
/*  82 */     this.flagBit = _flagBit;
/*  83 */     this.flagColumn = _flagColumn;
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
/*     */   public final short getPlonkId() {
/*  98 */     return this.plonkId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getFlagBit() {
/* 103 */     return this.flagBit;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getFlagColumn() {
/* 108 */     return this.flagColumn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void trigger(Creature player) {
/* 113 */     if (player.isPlayer() && !player.hasFlag(getFlagBit())) {
/*     */       
/* 115 */       player.getCommunicator().sendPlonk(getPlonkId());
/* 116 */       player.setFlag(getFlagBit(), true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasSeenThis(Creature player) {
/* 122 */     if (player.isPlayer())
/* 123 */       return player.hasFlag(getFlagBit()); 
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\PlonkData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */