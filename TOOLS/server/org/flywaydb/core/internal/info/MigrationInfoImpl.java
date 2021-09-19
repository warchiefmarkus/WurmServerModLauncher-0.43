/*     */ package org.flywaydb.core.internal.info;
/*     */ 
/*     */ import java.util.Date;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.MigrationState;
/*     */ import org.flywaydb.core.api.MigrationType;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.AppliedMigration;
/*     */ import org.flywaydb.core.internal.util.ObjectUtils;
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
/*     */ public class MigrationInfoImpl
/*     */   implements MigrationInfo
/*     */ {
/*     */   private final ResolvedMigration resolvedMigration;
/*     */   private final AppliedMigration appliedMigration;
/*     */   private final MigrationInfoContext context;
/*     */   private final boolean outOfOrder;
/*     */   
/*     */   public MigrationInfoImpl(ResolvedMigration resolvedMigration, AppliedMigration appliedMigration, MigrationInfoContext context, boolean outOfOrder) {
/*  63 */     this.resolvedMigration = resolvedMigration;
/*  64 */     this.appliedMigration = appliedMigration;
/*  65 */     this.context = context;
/*  66 */     this.outOfOrder = outOfOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvedMigration getResolvedMigration() {
/*  73 */     return this.resolvedMigration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppliedMigration getAppliedMigration() {
/*  80 */     return this.appliedMigration;
/*     */   }
/*     */   
/*     */   public MigrationType getType() {
/*  84 */     if (this.appliedMigration != null) {
/*  85 */       return this.appliedMigration.getType();
/*     */     }
/*  87 */     return this.resolvedMigration.getType();
/*     */   }
/*     */   
/*     */   public Integer getChecksum() {
/*  91 */     if (this.appliedMigration != null) {
/*  92 */       return this.appliedMigration.getChecksum();
/*     */     }
/*  94 */     return this.resolvedMigration.getChecksum();
/*     */   }
/*     */   
/*     */   public MigrationVersion getVersion() {
/*  98 */     if (this.appliedMigration != null) {
/*  99 */       return this.appliedMigration.getVersion();
/*     */     }
/* 101 */     return this.resolvedMigration.getVersion();
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 105 */     if (this.appliedMigration != null) {
/* 106 */       return this.appliedMigration.getDescription();
/*     */     }
/* 108 */     return this.resolvedMigration.getDescription();
/*     */   }
/*     */   
/*     */   public String getScript() {
/* 112 */     if (this.appliedMigration != null) {
/* 113 */       return this.appliedMigration.getScript();
/*     */     }
/* 115 */     return this.resolvedMigration.getScript();
/*     */   }
/*     */   
/*     */   public MigrationState getState() {
/* 119 */     if (this.appliedMigration == null) {
/* 120 */       if (this.resolvedMigration.getVersion() != null) {
/* 121 */         if (this.resolvedMigration.getVersion().compareTo(this.context.baseline) < 0) {
/* 122 */           return MigrationState.BELOW_BASELINE;
/*     */         }
/* 124 */         if (this.resolvedMigration.getVersion().compareTo(this.context.target) > 0) {
/* 125 */           return MigrationState.ABOVE_TARGET;
/*     */         }
/* 127 */         if (this.resolvedMigration.getVersion().compareTo(this.context.lastApplied) < 0 && !this.context.outOfOrder) {
/* 128 */           return MigrationState.IGNORED;
/*     */         }
/*     */       } 
/* 131 */       return MigrationState.PENDING;
/*     */     } 
/*     */     
/* 134 */     if (this.resolvedMigration == null) {
/* 135 */       if (MigrationType.SCHEMA == this.appliedMigration.getType()) {
/* 136 */         return MigrationState.SUCCESS;
/*     */       }
/*     */       
/* 139 */       if (MigrationType.BASELINE == this.appliedMigration.getType()) {
/* 140 */         return MigrationState.BASELINE;
/*     */       }
/*     */       
/* 143 */       if (this.appliedMigration.getVersion() == null || getVersion().compareTo(this.context.lastResolved) < 0) {
/* 144 */         if (this.appliedMigration.isSuccess()) {
/* 145 */           return MigrationState.MISSING_SUCCESS;
/*     */         }
/* 147 */         return MigrationState.MISSING_FAILED;
/*     */       } 
/* 149 */       if (this.appliedMigration.isSuccess()) {
/* 150 */         return MigrationState.FUTURE_SUCCESS;
/*     */       }
/* 152 */       return MigrationState.FUTURE_FAILED;
/*     */     } 
/*     */ 
/*     */     
/* 156 */     if (!this.appliedMigration.isSuccess()) {
/* 157 */       return MigrationState.FAILED;
/*     */     }
/*     */     
/* 160 */     if (this.appliedMigration.getVersion() == null) {
/* 161 */       if (ObjectUtils.nullSafeEquals(this.appliedMigration.getChecksum(), this.resolvedMigration.getChecksum())) {
/* 162 */         return MigrationState.SUCCESS;
/*     */       }
/* 164 */       if (this.appliedMigration.getInstalledRank() == ((Integer)this.context.latestRepeatableRuns.get(this.appliedMigration.getDescription())).intValue()) {
/* 165 */         return MigrationState.OUTDATED;
/*     */       }
/* 167 */       return MigrationState.SUPERSEEDED;
/*     */     } 
/*     */     
/* 170 */     if (this.outOfOrder) {
/* 171 */       return MigrationState.OUT_OF_ORDER;
/*     */     }
/* 173 */     return MigrationState.SUCCESS;
/*     */   }
/*     */   
/*     */   public Date getInstalledOn() {
/* 177 */     if (this.appliedMigration != null) {
/* 178 */       return this.appliedMigration.getInstalledOn();
/*     */     }
/* 180 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInstalledBy() {
/* 185 */     if (this.appliedMigration != null) {
/* 186 */       return this.appliedMigration.getInstalledBy();
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getInstalledRank() {
/* 193 */     if (this.appliedMigration != null) {
/* 194 */       return Integer.valueOf(this.appliedMigration.getInstalledRank());
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */   
/*     */   public Integer getExecutionTime() {
/* 200 */     if (this.appliedMigration != null) {
/* 201 */       return Integer.valueOf(this.appliedMigration.getExecutionTime());
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String validate() {
/* 212 */     if (this.resolvedMigration == null && this.appliedMigration
/* 213 */       .getType() != MigrationType.SCHEMA && this.appliedMigration
/* 214 */       .getType() != MigrationType.BASELINE && this.appliedMigration
/* 215 */       .getVersion() != null && (!this.context.future || (MigrationState.FUTURE_SUCCESS != 
/*     */       
/* 217 */       getState() && MigrationState.FUTURE_FAILED != getState()))) {
/* 218 */       return "Detected applied migration not resolved locally: " + getVersion();
/*     */     }
/*     */     
/* 221 */     if (!this.context.pending) {
/* 222 */       if (MigrationState.PENDING == getState() || MigrationState.IGNORED == getState()) {
/* 223 */         if (getVersion() != null) {
/* 224 */           return "Detected resolved migration not applied to database: " + getVersion();
/*     */         }
/* 226 */         return "Detected resolved repeatable migration not applied to database: " + getDescription();
/*     */       } 
/*     */       
/* 229 */       if (MigrationState.OUTDATED == getState()) {
/* 230 */         return "Detected outdated resolved repeatable migration that should be re-applied to database: " + getDescription();
/*     */       }
/*     */     } 
/*     */     
/* 234 */     if (this.resolvedMigration != null && this.appliedMigration != null) {
/* 235 */       Object migrationIdentifier = this.appliedMigration.getVersion();
/* 236 */       if (migrationIdentifier == null)
/*     */       {
/* 238 */         migrationIdentifier = this.appliedMigration.getScript();
/*     */       }
/* 240 */       if (getVersion() == null || getVersion().compareTo(this.context.baseline) > 0) {
/* 241 */         if (this.resolvedMigration.getType() != this.appliedMigration.getType()) {
/* 242 */           return createMismatchMessage("type", migrationIdentifier, this.appliedMigration
/* 243 */               .getType(), this.resolvedMigration.getType());
/*     */         }
/* 245 */         if ((this.resolvedMigration.getVersion() != null || (this.context.pending && MigrationState.OUTDATED != 
/*     */           
/* 247 */           getState() && MigrationState.SUPERSEEDED != getState())) && 
/* 248 */           !ObjectUtils.nullSafeEquals(this.resolvedMigration.getChecksum(), this.appliedMigration.getChecksum())) {
/* 249 */           return createMismatchMessage("checksum", migrationIdentifier, this.appliedMigration
/* 250 */               .getChecksum(), this.resolvedMigration.getChecksum());
/*     */         }
/*     */         
/* 253 */         if (!this.resolvedMigration.getDescription().equals(this.appliedMigration.getDescription())) {
/* 254 */           return createMismatchMessage("description", migrationIdentifier, this.appliedMigration
/* 255 */               .getDescription(), this.resolvedMigration.getDescription());
/*     */         }
/*     */       } 
/*     */     } 
/* 259 */     return null;
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
/*     */   private String createMismatchMessage(String mismatch, Object migrationIdentifier, Object applied, Object resolved) {
/* 272 */     return String.format("Migration " + mismatch + " mismatch for migration %s\n" + "-> Applied to database : %s\n" + "-> Resolved locally    : %s", new Object[] { migrationIdentifier, applied, resolved });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MigrationInfo o) {
/* 280 */     if (getInstalledRank() != null && o.getInstalledRank() != null) {
/* 281 */       return getInstalledRank().intValue() - o.getInstalledRank().intValue();
/*     */     }
/*     */     
/* 284 */     MigrationState state = getState();
/* 285 */     MigrationState oState = o.getState();
/*     */     
/* 287 */     if ((getInstalledRank() != null || o.getInstalledRank() != null) && state != MigrationState.BELOW_BASELINE && oState != MigrationState.BELOW_BASELINE && state != MigrationState.IGNORED && oState != MigrationState.IGNORED) {
/*     */ 
/*     */       
/* 290 */       if (getInstalledRank() != null) {
/* 291 */         return Integer.MIN_VALUE;
/*     */       }
/* 293 */       if (o.getInstalledRank() != null) {
/* 294 */         return Integer.MAX_VALUE;
/*     */       }
/*     */     } 
/*     */     
/* 298 */     if (getVersion() != null && o.getVersion() != null) {
/* 299 */       return getVersion().compareTo(o.getVersion());
/*     */     }
/*     */ 
/*     */     
/* 303 */     if (getVersion() != null) {
/* 304 */       return Integer.MIN_VALUE;
/*     */     }
/* 306 */     if (o.getVersion() != null) {
/* 307 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 310 */     return getDescription().compareTo(o.getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 316 */     if (this == o) return true; 
/* 317 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 319 */     MigrationInfoImpl that = (MigrationInfoImpl)o;
/*     */     
/* 321 */     if ((this.appliedMigration != null) ? !this.appliedMigration.equals(that.appliedMigration) : (that.appliedMigration != null))
/* 322 */       return false; 
/* 323 */     if (!this.context.equals(that.context)) return false; 
/* 324 */     if ((this.resolvedMigration != null) ? !this.resolvedMigration.equals(that.resolvedMigration) : (that.resolvedMigration != null)) return false;
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 329 */     int result = (this.resolvedMigration != null) ? this.resolvedMigration.hashCode() : 0;
/* 330 */     result = 31 * result + ((this.appliedMigration != null) ? this.appliedMigration.hashCode() : 0);
/* 331 */     result = 31 * result + this.context.hashCode();
/* 332 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\info\MigrationInfoImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */