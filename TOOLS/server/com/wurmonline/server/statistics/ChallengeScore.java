/*     */ package com.wurmonline.server.statistics;
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
/*     */ public class ChallengeScore
/*     */ {
/*     */   private final int type;
/*     */   private float points;
/*     */   private float lastPoints;
/*     */   private long lastUpdated;
/*     */   
/*     */   public ChallengeScore(int scoreType, float numPoints, long aLastUpdated, float aLastPoints) {
/*  36 */     this.type = scoreType;
/*  37 */     setPoints(numPoints);
/*  38 */     setLastPoints(aLastPoints);
/*  39 */     setLastUpdated(aLastUpdated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/*  49 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPoints() {
/*  59 */     return this.points;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPoints(float aPoints) {
/*  70 */     this.points = aPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastUpdated() {
/*  80 */     return this.lastUpdated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastUpdated(long aLastUpdated) {
/*  91 */     this.lastUpdated = aLastUpdated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLastPoints() {
/* 101 */     return this.lastPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastPoints(float aLastPoints) {
/* 112 */     this.lastPoints = aLastPoints;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\ChallengeScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */