/*     */ package com.wurmonline.server.database.migrations;
/*     */ 
/*     */ import com.wurmonline.server.database.ConnectionFactory;
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nonnull;
/*     */ import org.flywaydb.core.Flyway;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.MigrationInfoService;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Migrator<B extends ConnectionFactory>
/*     */ {
/*  24 */   private static final Logger logger = Logger.getLogger(Migrator.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FILESYSTEM_PREFIX = "filesystem:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VERSION_TABLE = "SCHEMA_VERSION";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String MIGRATION_PREFIX = "v";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REPEATABLE_MIGRATION_PREFIX = "r";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String BASELINE_VERSION = "1";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Flyway flyway;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<Path> sqlDirectories;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Migrator(B connectionFactory, List<Path> sqlDirectories, FlywayConfigurer configurer) {
/*  67 */     this(new Flyway(), connectionFactory, sqlDirectories, configurer);
/*     */   }
/*     */ 
/*     */   
/*     */   Migrator(Flyway flyway, B connectionFactory, List<Path> sqlDirectories, FlywayConfigurer configurer) {
/*  72 */     this.flyway = flyway;
/*  73 */     this.sqlDirectories = Collections.unmodifiableList(new ArrayList<>(sqlDirectories));
/*  74 */     flyway.setLocations(new String[] { asFlywayLocations(sqlDirectories) });
/*  75 */     flyway.setTable("SCHEMA_VERSION");
/*  76 */     flyway.setSqlMigrationPrefix("v");
/*  77 */     flyway.setRepeatableSqlMigrationPrefix("r");
/*  78 */     configurer.configureMigrations(flyway);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCurrent() {
/*  83 */     return ((this.flyway.info().pending()).length == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MigrationInfoService baseline() {
/*  93 */     logger.info("No database migrations metadata found, creating baseline at version 1");
/*  94 */     this.flyway.setBaselineVersion(MigrationVersion.fromVersion("1"));
/*  95 */     this.flyway.baseline();
/*     */     
/*  97 */     return this.flyway.info();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<String> ensureDirsExist() {
/* 107 */     for (Path path : this.sqlDirectories) {
/*     */       
/* 109 */       File dir = path.toFile();
/* 110 */       if (!dir.exists())
/*     */       {
/* 112 */         if (!dir.mkdirs()) {
/*     */           
/* 114 */           String errorMessage = "Could not find or create migrations directory at " + dir.getAbsolutePath();
/* 115 */           logger.warning(errorMessage);
/* 116 */           Optional.of(errorMessage);
/*     */         } 
/*     */       }
/*     */     } 
/* 120 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPendingMigrations() {
/* 128 */     MigrationInfoService migrationInfoService = this.flyway.info();
/* 129 */     MigrationInfo currentInfo = migrationInfoService.current();
/* 130 */     return (currentInfo == null || (migrationInfoService.pending()).length > 0);
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
/*     */   @Nonnull
/*     */   public MigrationResult migrate() {
/*     */     MigrationVersion beforeVersion;
/* 145 */     Optional<String> optionalError = ensureDirsExist();
/* 146 */     if (optionalError.isPresent())
/*     */     {
/* 148 */       return MigrationResult.newError(optionalError.get());
/*     */     }
/* 150 */     MigrationInfoService migrationInfoService = this.flyway.info();
/* 151 */     MigrationInfo currentInfo = migrationInfoService.current();
/*     */     
/* 153 */     if (currentInfo == null) {
/*     */       
/* 155 */       beforeVersion = MigrationVersion.EMPTY;
/*     */       
/* 157 */       migrationInfoService = baseline();
/* 158 */       currentInfo = migrationInfoService.current();
/* 159 */       if (currentInfo == null) {
/*     */         
/* 161 */         String errorMessage = "No database versioning information found after creating baseline";
/*     */         
/* 163 */         logger.warning(errorMessage);
/* 164 */         return MigrationResult.newError(errorMessage);
/*     */       } 
/* 166 */       int numMigrationsPending = (migrationInfoService.pending()).length;
/* 167 */       if (numMigrationsPending == 0)
/*     */       {
/* 169 */         logger.info("Database baselined to version 1. No migrations pending.");
/* 170 */         return MigrationResult.newSuccess(beforeVersion, currentInfo.getVersion(), 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 175 */       beforeVersion = currentInfo.getVersion();
/* 176 */       if (isCurrent()) {
/*     */         
/* 178 */         logger.info("No pending migrations, database is current");
/* 179 */         return MigrationResult.newSuccess(beforeVersion, beforeVersion, 0);
/*     */       } 
/*     */     } 
/* 182 */     logger.info("Found " + (migrationInfoService.pending()).length + " pending database migrations, initiating now.");
/* 183 */     int numMigrations = this.flyway.migrate();
/* 184 */     migrationInfoService = this.flyway.info();
/* 185 */     currentInfo = migrationInfoService.current();
/* 186 */     if (numMigrations == 0) {
/*     */       
/* 188 */       logger.warning("Pending migrations found but none performed.");
/*     */     } else {
/* 190 */       if (currentInfo == null) {
/*     */         
/* 192 */         String errorMessage = "Performed " + numMigrations + " migrations but no migrations metadata found afterwards.";
/*     */         
/* 194 */         logger.warning(errorMessage);
/* 195 */         return MigrationResult.newError(errorMessage);
/*     */       } 
/*     */ 
/*     */       
/* 199 */       logger.info("Performed " + numMigrations + " database migrations. Current version is " + currentInfo
/*     */ 
/*     */           
/* 202 */           .getVersion());
/*     */     } 
/* 204 */     return MigrationResult.newSuccess(beforeVersion, currentInfo.getVersion(), numMigrations);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Flyway getFlyway() {
/* 209 */     return this.flyway;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String asFlywayLocation(Path dir) {
/* 214 */     return "filesystem:" + dir.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String asFlywayLocations(List<Path> paths) {
/* 219 */     StringBuilder builder = new StringBuilder();
/* 220 */     if (paths.size() == 0) return "";
/*     */     
/* 222 */     builder.append("filesystem:");
/* 223 */     builder.append(((Path)paths.get(0)).toString());
/* 224 */     for (Path path : paths.subList(1, paths.size())) {
/*     */       
/* 226 */       builder.append(',');
/* 227 */       builder.append("filesystem:");
/* 228 */       builder.append(path.toString());
/*     */     } 
/* 230 */     return builder.toString();
/*     */   }
/*     */   
/*     */   static interface FlywayConfigurer {
/*     */     void configureMigrations(Flyway param1Flyway);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\Migrator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */