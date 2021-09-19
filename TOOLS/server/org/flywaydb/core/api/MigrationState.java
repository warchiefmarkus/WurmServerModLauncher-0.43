/*     */ package org.flywaydb.core.api;
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
/*     */ public enum MigrationState
/*     */ {
/*  25 */   PENDING("Pending", true, false, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   ABOVE_TARGET(">Target", true, false, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   BELOW_BASELINE("<Baseln", true, false, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   BASELINE("Baselin", true, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   IGNORED("Ignored", true, false, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   MISSING_SUCCESS("Missing", false, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   MISSING_FAILED("MisFail", false, true, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   SUCCESS("Success", true, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   FAILED("Failed", true, true, true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   OUT_OF_ORDER("OutOrdr", true, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   FUTURE_SUCCESS("Future", false, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   FUTURE_FAILED("FutFail", false, true, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   OUTDATED("Outdate", true, true, false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   SUPERSEEDED("Superse", true, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String displayName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean resolved;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean applied;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean failed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MigrationState(String displayName, boolean resolved, boolean applied, boolean failed) {
/* 149 */     this.displayName = displayName;
/* 150 */     this.resolved = resolved;
/* 151 */     this.applied = applied;
/* 152 */     this.failed = failed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/* 159 */     return this.displayName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isApplied() {
/* 166 */     return this.applied;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResolved() {
/* 173 */     return this.resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFailed() {
/* 180 */     return this.failed;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\MigrationState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */