/*     */ package com.wurmonline.server.structures;
/*     */ 
/*     */ import com.wurmonline.math.Vector3f;
/*     */ import java.util.LinkedList;
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
/*     */ public final class BlockingResult
/*     */ {
/*     */   private LinkedList<Blocker> blockers;
/*     */   private LinkedList<Vector3f> intersections;
/*     */   private float totalCover;
/*  36 */   private static final Blocker[] emptyBlockers = new Blocker[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float estimatedBlockingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float actualBlockingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float addBlocker(Blocker blockerToAdd, Vector3f intersection, float factorToAdd) {
/*  57 */     if (this.blockers == null) {
/*     */       
/*  59 */       this.blockers = new LinkedList<>();
/*  60 */       this.intersections = new LinkedList<>();
/*     */     } 
/*  62 */     this.blockers.add(blockerToAdd);
/*  63 */     this.intersections.add(intersection);
/*  64 */     addBlockingFactor(factorToAdd);
/*  65 */     return this.totalCover;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addBlockingFactor(float factorToAdd) {
/*  70 */     this.totalCover += factorToAdd;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Blocker getFirstBlocker() {
/*  75 */     if (this.blockers != null && !this.blockers.isEmpty())
/*  76 */       return this.blockers.getFirst(); 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Blocker getLastBlocker() {
/*  82 */     if (this.blockers != null && !this.blockers.isEmpty())
/*  83 */       return this.blockers.getLast(); 
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f getFirstIntersection() {
/*  89 */     if (this.intersections != null && !this.intersections.isEmpty())
/*  90 */       return this.intersections.getFirst(); 
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Vector3f getLastIntersection() {
/*  96 */     if (this.intersections != null && !this.intersections.isEmpty())
/*  97 */       return this.intersections.getLast(); 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getTotalCover() {
/* 103 */     return this.totalCover;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeBlocker(Blocker blocker) {
/* 108 */     if (this.blockers != null) {
/* 109 */       this.blockers.remove(blocker);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Blocker[] getBlockerArray() {
/* 114 */     if (this.blockers == null || this.blockers.isEmpty())
/* 115 */       return emptyBlockers; 
/* 116 */     return this.blockers.<Blocker>toArray(new Blocker[this.blockers.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getActualBlockingTime() {
/* 126 */     return this.actualBlockingTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActualBlockingTime(float aActualBlockingTime) {
/* 137 */     this.actualBlockingTime = aActualBlockingTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEstimatedBlockingTime() {
/* 147 */     return this.estimatedBlockingTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEstimatedBlockingTime(float aEstimatedBlockingTime) {
/* 158 */     this.estimatedBlockingTime = aEstimatedBlockingTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\structures\BlockingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */