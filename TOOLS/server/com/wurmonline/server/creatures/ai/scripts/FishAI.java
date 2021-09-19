/*     */ package com.wurmonline.server.creatures.ai.scripts;
/*     */ 
/*     */ import com.wurmonline.server.behaviours.FishEnums;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*     */ import com.wurmonline.server.creatures.ai.CreatureAIData;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FishAI
/*     */   extends CreatureAI
/*     */ {
/*     */   protected boolean pollMovement(Creature c, long delta) {
/*  23 */     FishAIData aiData = (FishAIData)c.getCreatureAIData();
/*  24 */     float targetX = aiData.getTargetPosX();
/*  25 */     float targetY = aiData.getTargetPosY();
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
/*  45 */     if (targetX < 0.0F || targetY < 0.0F) {
/*  46 */       return false;
/*     */     }
/*  48 */     if (c.getPosX() != targetX || c.getPosY() != targetY) {
/*     */       
/*  50 */       float diffX = c.getPosX() - targetX;
/*  51 */       float diffY = c.getPosY() - targetY;
/*  52 */       float totalDiff = (float)Math.sqrt((diffX * diffX + diffY * diffY));
/*  53 */       float movementSpeed = aiData.getSpeed() * aiData.getMovementSpeedModifier();
/*     */       
/*  55 */       if (totalDiff < movementSpeed) {
/*  56 */         movementSpeed = totalDiff;
/*     */       }
/*  58 */       double lRotation = Math.atan2((targetY - c.getPosY()), (targetX - c.getPosX())) * 57.29577951308232D + 90.0D;
/*  59 */       float lXPosMod = (float)Math.sin(lRotation * 0.01745329238474369D) * movementSpeed;
/*  60 */       float lYPosMod = -((float)Math.cos(lRotation * 0.01745329238474369D)) * movementSpeed;
/*  61 */       int lNewTileX = (int)(c.getPosX() + lXPosMod) >> 2;
/*  62 */       int lNewTileY = (int)(c.getPosY() + lYPosMod) >> 2;
/*  63 */       int lDiffTileX = lNewTileX - c.getTileX();
/*  64 */       int lDiffTileY = lNewTileY - c.getTileY();
/*     */       
/*  66 */       c.setPositionX(c.getPosX() + lXPosMod);
/*  67 */       c.setPositionY(c.getPosY() + lYPosMod);
/*  68 */       c.setRotation((float)lRotation);
/*     */ 
/*     */       
/*     */       try {
/*  72 */         float minZ = Math.min(-0.1F, Zones.calculateHeight(c.getPosX(), c.getPosY(), c.isOnSurface()));
/*     */         
/*  74 */         if (c.getPositionZ() < minZ) {
/*  75 */           c.setPositionZ(minZ + Math.abs(minZ * 0.2F));
/*  76 */         } else if (c.getPositionZ() < minZ * 0.15F) {
/*  77 */           c.setPositionZ(minZ * 0.15F);
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
/*     */         }
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
/*     */       }
/* 104 */       catch (NoSuchZoneException noSuchZoneException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       c.moved(lXPosMod, lYPosMod, 0.0F, lDiffTileX, lDiffTileY);
/*     */     } 
/*     */     
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollAttack(Creature c, long delta) {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollBreeding(Creature c, long delta) {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureAIData createCreatureAIData() {
/* 132 */     return new FishAIData();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void creatureCreated(Creature c) {}
/*     */ 
/*     */   
/*     */   public class FishAIData
/*     */     extends CreatureAIData
/*     */   {
/* 143 */     private byte fishTypeId = 0;
/* 144 */     private double ql = 10.0D;
/* 145 */     private float qlperc = 1.0F;
/* 146 */     private int weight = 0;
/* 147 */     private float targetPosX = -1.0F;
/* 148 */     private float targetPosY = -1.0F;
/* 149 */     private float timeToTarget = 0.0F;
/* 150 */     private float bodyStrength = 1.0F;
/* 151 */     private float bodyStamina = 1.0F;
/* 152 */     private float bodyControl = 1.0F;
/* 153 */     private float mindSpeed = 1.0F;
/* 154 */     private float difficulty = -10.0F;
/*     */ 
/*     */     
/*     */     private boolean racingAway = false;
/*     */     
/*     */     private static final int PERC_OFFSET = 25;
/*     */     
/*     */     private static final int SPEED_OFFSET = 75;
/*     */ 
/*     */     
/*     */     public byte getFishTypeId() {
/* 165 */       return this.fishTypeId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFishTypeId(byte fishTypeId) {
/* 174 */       this.fishTypeId = fishTypeId;
/*     */     }
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
/*     */     public float getSpeed() {
/*     */       float mod;
/* 188 */       if (this.racingAway) {
/* 189 */         mod = 2.5F;
/*     */       } else {
/* 191 */         mod = (75.0F + (float)this.ql) / 175.0F;
/* 192 */       }  return getFishData().getBaseSpeed() * mod;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FishEnums.FishData getFishData() {
/* 200 */       return FishEnums.FishData.fromInt(this.fishTypeId);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setQL(double ql) {
/* 210 */       this.ql = ql;
/* 211 */       this.qlperc = (25.0F + (float)this.ql) / 125.0F;
/*     */       
/* 213 */       this.bodyStrength = Math.max(getFishData().getBodyStrength() * this.qlperc, 1.0F);
/* 214 */       this.bodyStamina = Math.max(getFishData().getBodyStamina() * this.qlperc, 1.0F);
/* 215 */       this.bodyControl = Math.max(getFishData().getBodyControl() * this.qlperc, 1.0F);
/* 216 */       this.mindSpeed = Math.max(getFishData().getMindSpeed() * this.qlperc, 1.0F);
/*     */       
/* 218 */       setSizeModifier(this.qlperc * getFishData().getScaleMod());
/*     */       
/* 220 */       ItemTemplate it = getFishData().getTemplate();
/* 221 */       if (it != null) {
/* 222 */         this.weight = (int)(it.getWeightGrams() * ql / 100.0D);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTargetPos(float targetPosX, float targetPosY) {
/* 233 */       this.targetPosX = targetPosX;
/* 234 */       this.targetPosY = targetPosY;
/* 235 */       calcTimeToTarget();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRaceAway(boolean raceAway) {
/* 243 */       this.racingAway = raceAway;
/* 244 */       calcTimeToTarget();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void calcTimeToTarget() {
/* 253 */       float diffX = this.targetPosX - getCreature().getPosX();
/* 254 */       float diffY = this.targetPosY - getCreature().getPosY();
/* 255 */       float dist = (float)Math.sqrt((diffX * diffX + diffY * diffY));
/*     */       
/* 257 */       float movementSpeed = getSpeed() * getMovementSpeedModifier();
/* 258 */       this.timeToTarget = dist / movementSpeed * 10.0F + 2.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getTargetPosX() {
/* 266 */       return this.targetPosX;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getTargetPosY() {
/* 274 */       return this.targetPosY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getTimeToTarget() {
/* 282 */       return this.timeToTarget;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getQL() {
/* 290 */       return this.ql;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNameWithGenusAndSize() {
/* 298 */       return StringUtilities.addGenus(getNameWithSize(), false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNameWithSize() {
/* 306 */       StringBuilder buf = new StringBuilder();
/* 307 */       if (this.ql >= 99.0D) {
/* 308 */         buf.append("stupendous ");
/* 309 */       } else if (this.ql >= 95.0D) {
/* 310 */         buf.append("massive ");
/* 311 */       } else if (this.ql >= 85.0D) {
/* 312 */         buf.append("huge ");
/* 313 */       } else if (this.ql >= 75.0D) {
/* 314 */         buf.append("impressive ");
/* 315 */       } else if (this.ql >= 65.0D) {
/* 316 */         buf.append("large ");
/* 317 */       }  if (this.ql < 15.0D)
/* 318 */         buf.append("small "); 
/* 319 */       buf.append(getFishData().getName());
/* 320 */       return buf.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getWeight() {
/* 328 */       return this.weight;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getBodyStrength() {
/* 336 */       return this.bodyStrength;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getBodyStamina() {
/* 344 */       return this.bodyStamina;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void decBodyStamina(float bodyStamina) {
/* 353 */       this.bodyStamina = Math.max(this.bodyStamina - bodyStamina, 0.0F);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getBodyControl() {
/* 361 */       return this.bodyControl;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getMindSpeed() {
/* 369 */       return this.mindSpeed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDifficulty(float difficulty) {
/* 378 */       this.difficulty = difficulty;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getDifficulty() {
/* 386 */       return this.difficulty;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\FishAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */