/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.SpellEffectsEnum;
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
/*     */ public class SpellResistance
/*     */ {
/*     */   private static final float initialResistance = 0.5F;
/*  30 */   private float currentResistance = 0.0F;
/*  31 */   private int currentSecond = 0;
/*  32 */   private float customTickMod = 0.0F;
/*     */ 
/*     */   
/*     */   private static final int secondsBetweenTicks = 7;
/*     */   
/*     */   private static final float tickModifier = 0.0117F;
/*     */   
/*     */   protected static final byte UTYPESTART = 1;
/*     */   
/*     */   protected static final byte UTYPESTOP = 0;
/*     */   
/*     */   protected static final byte UTYPEUPDATE = 2;
/*     */   
/*     */   private final short spellType;
/*     */ 
/*     */   
/*     */   public SpellResistance(short spell) {
/*  49 */     this.spellType = spell;
/*     */   }
/*     */ 
/*     */   
/*     */   public final short getSpellType() {
/*  54 */     return this.spellType;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean tickSecond(Communicator comm) {
/*  59 */     if (this.currentSecond++ == 7) {
/*     */       
/*  61 */       this.currentResistance -= (this.customTickMod > 0.0F) ? this.customTickMod : 0.0117F;
/*  62 */       this.currentSecond = 0;
/*  63 */       if (this.currentResistance <= 0.0F) {
/*     */         
/*  65 */         this.currentResistance = 0.0F;
/*  66 */         sendUpdateToClient(comm, (byte)0);
/*  67 */         return true;
/*     */       } 
/*  69 */       sendUpdateToClient(comm, (byte)2);
/*     */     } 
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int getSecondsLeft() {
/*  76 */     return (int)(this.currentResistance * 100.0F / ((this.customTickMod > 0.0F) ? this.customTickMod : 0.0117F) * 100.0F * 7.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setResistance() {
/*  81 */     this.currentResistance = 0.5F;
/*  82 */     this.currentSecond = 0;
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
/*     */   public final void setResistance(float newRes, float tickModifier) {
/*  94 */     this.currentResistance = newRes;
/*  95 */     this.currentSecond = 0;
/*  96 */     this.customTickMod = tickModifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getResistance() {
/* 101 */     return this.currentResistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendUpdateToClient(Communicator communicator, byte updateType) {
/* 106 */     switch (updateType) {
/*     */       
/*     */       case 1:
/* 109 */         communicator.sendAddStatusEffect(SpellEffectsEnum.getResistanceForSpell(this.spellType), getSecondsLeft());
/*     */         return;
/*     */       case 0:
/* 112 */         communicator.sendRemoveSpellEffect(SpellEffectsEnum.getResistanceForSpell(this.spellType));
/*     */         return;
/*     */     } 
/*     */     
/* 116 */     communicator.sendAddStatusEffect(SpellEffectsEnum.getResistanceForSpell(this.spellType), getSecondsLeft());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\SpellResistance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */