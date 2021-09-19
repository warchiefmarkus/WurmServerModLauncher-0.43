/*     */ package com.wurmonline.server.database.migrations;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.ParametersAreNonnullByDefault;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import org.flywaydb.core.api.MigrationVersion;
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
/*     */ @ParametersAreNonnullByDefault
/*     */ @Immutable
/*     */ public abstract class MigrationResult
/*     */ {
/*     */   private MigrationResult() {}
/*     */   
/*     */   public boolean isError() {
/*  35 */     return !isSuccess();
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
/*     */   public MigrationError asError() {
/*  47 */     throw new IllegalArgumentException("This migration is not in error");
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
/*     */   public MigrationSuccess asSuccess() {
/*  59 */     throw new IllegalArgumentException("This migration is not a success");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   @Immutable
/*     */   public static final class MigrationError
/*     */     extends MigrationResult
/*     */   {
/*     */     private final String message;
/*     */ 
/*     */     
/*     */     private MigrationError(String message) {
/*  73 */       this.message = message;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSuccess() {
/*  79 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public final String getMessage() {
/*  85 */       return this.message;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MigrationError asError() {
/*  91 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   @Immutable
/*     */   public static final class MigrationSuccess
/*     */     extends MigrationResult
/*     */   {
/*     */     private final MigrationVersion versionBeforeMigration;
/*     */     
/*     */     private final MigrationVersion versionAfterMigration;
/*     */     
/*     */     private final int numMigrations;
/*     */ 
/*     */     
/*     */     private MigrationSuccess(MigrationVersion versionBeforeMigration, MigrationVersion versionAfterMigration, int numMigrations) {
/* 110 */       this.versionBeforeMigration = versionBeforeMigration;
/* 111 */       this.versionAfterMigration = versionAfterMigration;
/* 112 */       this.numMigrations = numMigrations;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSuccess() {
/* 117 */       return true;
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
/*     */     public MigrationVersion getVersionBefore() {
/* 130 */       return this.versionBeforeMigration;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MigrationVersion getVersionAfter() {
/* 139 */       return this.versionAfterMigration;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getNumMigrations() {
/* 147 */       return this.numMigrations;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MigrationSuccess asSuccess() {
/* 153 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MigrationError newError(String message) {
/* 165 */     return new MigrationError(message);
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
/*     */   
/*     */   static MigrationSuccess newSuccess(MigrationVersion versionBeforeMigration, MigrationVersion versionAfterMigration, int numMigrations) {
/* 181 */     return new MigrationSuccess(versionBeforeMigration, versionAfterMigration, numMigrations);
/*     */   }
/*     */   
/*     */   public abstract boolean isSuccess();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\MigrationResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */