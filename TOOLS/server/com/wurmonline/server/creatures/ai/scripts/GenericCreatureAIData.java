/*     */ package com.wurmonline.server.creatures.ai.scripts;
/*     */ 
/*     */ import com.wurmonline.math.Vector2f;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAIData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericCreatureAIData
/*     */   extends CreatureAIData
/*     */ {
/*     */   private boolean freezeMovement = false;
/*  16 */   private float randomMovementChance = 0.01F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prefersPlayers = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   private float prefersPlayersModifier = 2.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasTether = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private int tetherX = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private int tetherY = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private int tetherDistance = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericCreatureAIData() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericCreatureAIData(boolean prefersPlayers, float movementChance) {
/*  66 */     this.prefersPlayers = prefersPlayers;
/*  67 */     this.randomMovementChance = movementChance;
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
/*     */   public GenericCreatureAIData(boolean prefersPlayers, float movementChance, int tetherDistance) {
/*  80 */     this(prefersPlayers, movementChance);
/*     */     
/*  82 */     this.tetherDistance = tetherDistance;
/*  83 */     if (tetherDistance > 0) {
/*  84 */       this.hasTether = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMovementFrozen() {
/*  95 */     return this.freezeMovement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMovementFrozen(boolean frozen) {
/* 105 */     this.freezeMovement = frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRandomMovementChance() {
/* 115 */     return this.randomMovementChance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRandomMovementChance(float newChance) {
/* 125 */     if (newChance > 1.0F || newChance < 0.0F) {
/* 126 */       newChance = Math.max(0.0F, Math.min(1.0F, newChance));
/*     */     }
/* 128 */     this.randomMovementChance = newChance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doesPreferPlayers() {
/* 138 */     return this.prefersPlayers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefersPlayers(boolean doesPreferPlayers) {
/* 148 */     this.prefersPlayers = doesPreferPlayers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPrefersPlayersModifier() {
/* 157 */     return this.prefersPlayersModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefersPlayersModifier(float prefersPlayersModifier) {
/* 166 */     this.prefersPlayersModifier = prefersPlayersModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTether() {
/* 176 */     return this.hasTether;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTether(boolean shouldHaveTether) {
/* 186 */     this.hasTether = shouldHaveTether;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTether(int tileX, int tileY) {
/* 197 */     if (tileX > 0 && tileY > 0) {
/* 198 */       this.hasTether = true;
/*     */     }
/* 200 */     this.tetherX = tileX;
/* 201 */     this.tetherY = tileY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTetherX() {
/* 211 */     return this.tetherX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTetherY() {
/* 221 */     return this.tetherY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f getTetherPos() {
/* 231 */     return new Vector2f(this.tetherX, this.tetherY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTetherDistance(int newDistance) {
/* 241 */     this.tetherDistance = newDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTetherDistance() {
/* 251 */     return this.tetherDistance;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\GenericCreatureAIData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */