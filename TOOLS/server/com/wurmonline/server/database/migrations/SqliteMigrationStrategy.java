/*     */ package com.wurmonline.server.database.migrations;
/*     */ 
/*     */ import com.wurmonline.server.database.SqliteConnectionFactory;
/*     */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ 
/*     */ public class SqliteMigrationStrategy
/*     */   implements MigrationStrategy
/*     */ {
/*  16 */   private static final Logger logger = Logger.getLogger(SqliteMigrationStrategy.class.getName());
/*     */ 
/*     */   
/*     */   private final List<SqliteMigrator> migrators;
/*     */ 
/*     */ 
/*     */   
/*     */   public SqliteMigrationStrategy(List<SqliteConnectionFactory> connectionFactories, Path migrationsDir) {
/*  24 */     List<SqliteMigrator> migrators = new ArrayList<>();
/*  25 */     for (SqliteConnectionFactory connectionFactory : connectionFactories) {
/*     */       
/*  27 */       Path migrationsDirForSchema = migrationsDir.resolve(connectionFactory.getSchema().getMigration());
/*  28 */       migrators.add(new SqliteMigrator(connectionFactory, migrationsDirForSchema));
/*     */     } 
/*  30 */     this.migrators = migrators;
/*     */   }
/*     */ 
/*     */   
/*     */   private void logErrors(Map<WurmDatabaseSchema, MigrationResult.MigrationSuccess> successfulMigrations) {
/*  35 */     if (successfulMigrations.size() == 0) {
/*     */       
/*  37 */       logger.warning("Cannot perform migrations, error encounted. No migrations performed successfully.");
/*     */     }
/*     */     else {
/*     */       
/*  41 */       logger.warning("Cannot continue migrations, error encountered. Migration in a partial state.");
/*  42 */       logger.warning("The following migrations were performed successfully:");
/*     */       
/*  44 */       for (Map.Entry<WurmDatabaseSchema, MigrationResult.MigrationSuccess> entry : successfulMigrations.entrySet()) {
/*     */         
/*  46 */         MigrationResult.MigrationSuccess result = entry.getValue();
/*  47 */         String before = result.getVersionBefore().getVersion();
/*  48 */         if (before == null)
/*     */         {
/*  50 */           before = "baseline";
/*     */         }
/*  52 */         logger.warning(((WurmDatabaseSchema)entry.getKey()).name() + " : performed " + result
/*  53 */             .getNumMigrations() + " migrations from " + before + " to " + result
/*  54 */             .getVersionAfter());
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPendingMigrations() {
/*  70 */     for (SqliteMigrator migrator : this.migrators) {
/*     */       
/*  72 */       if (migrator.hasPendingMigrations())
/*     */       {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigrationResult migrate() {
/*  87 */     MigrationVersion earliestVersion = MigrationVersion.LATEST;
/*  88 */     MigrationVersion latestVersion = MigrationVersion.EMPTY;
/*  89 */     int numMigrations = 0;
/*  90 */     LinkedHashMap<WurmDatabaseSchema, MigrationResult.MigrationSuccess> schemasMigrated = new LinkedHashMap<>();
/*  91 */     for (SqliteMigrator migrator : this.migrators) {
/*     */       
/*  93 */       MigrationResult result = migrator.migrate();
/*  94 */       if (result.isError()) {
/*     */         
/*  96 */         logErrors(schemasMigrated);
/*  97 */         return result;
/*     */       } 
/*     */ 
/*     */       
/* 101 */       MigrationResult.MigrationSuccess success = result.asSuccess();
/* 102 */       schemasMigrated.put(migrator.getSchema(), success);
/* 103 */       if (latestVersion.compareTo(success.getVersionAfter()) < 0)
/*     */       {
/* 105 */         latestVersion = success.getVersionAfter();
/*     */       }
/* 107 */       MigrationVersion before = success.getVersionBefore();
/* 108 */       if (before == null) {
/*     */         
/* 110 */         earliestVersion = MigrationVersion.EMPTY;
/*     */       }
/* 112 */       else if (before.compareTo(earliestVersion) < 0) {
/*     */         
/* 114 */         earliestVersion = success.getVersionBefore();
/*     */       } 
/* 116 */       numMigrations += success.getNumMigrations();
/*     */     } 
/*     */     
/* 119 */     if (latestVersion == null) {
/*     */       
/* 121 */       String errorMessage = "Error encountered after performing migrations: could not determine latest version";
/* 122 */       logger.warning(errorMessage);
/* 123 */       return MigrationResult.newError(errorMessage);
/*     */     } 
/* 125 */     return MigrationResult.newSuccess(earliestVersion, latestVersion, numMigrations);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\SqliteMigrationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */