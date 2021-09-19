/*     */ package org.flywaydb.core.internal.command;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.MigrationState;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.api.resolver.MigrationExecutor;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupportFactory;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.info.MigrationInfoImpl;
/*     */ import org.flywaydb.core.internal.info.MigrationInfoServiceImpl;
/*     */ import org.flywaydb.core.internal.metadatatable.AppliedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*     */ import org.flywaydb.core.internal.util.StopWatch;
/*     */ import org.flywaydb.core.internal.util.TimeFormat;
/*     */ import org.flywaydb.core.internal.util.jdbc.TransactionCallback;
/*     */ import org.flywaydb.core.internal.util.jdbc.TransactionTemplate;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*     */ public class DbMigrate
/*     */ {
/*  49 */   private static final Log LOG = LogFactory.getLog(DbMigrate.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MigrationVersion target;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final DbSupport dbSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MetaDataTable metaDataTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Schema schema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MigrationResolver migrationResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Connection connectionMetaDataTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Connection connectionUserObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean ignoreFutureMigrations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean ignoreFailedFutureMigration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean outOfOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final FlywayCallback[] callbacks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final DbSupport dbSupportUserObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbMigrate(Connection connectionMetaDataTable, Connection connectionUserObjects, DbSupport dbSupport, MetaDataTable metaDataTable, Schema schema, MigrationResolver migrationResolver, MigrationVersion target, boolean ignoreFutureMigrations, boolean ignoreFailedFutureMigration, boolean outOfOrder, FlywayCallback[] callbacks) {
/* 133 */     this.connectionMetaDataTable = connectionMetaDataTable;
/* 134 */     this.connectionUserObjects = connectionUserObjects;
/* 135 */     this.dbSupport = dbSupport;
/* 136 */     this.metaDataTable = metaDataTable;
/* 137 */     this.schema = schema;
/* 138 */     this.migrationResolver = migrationResolver;
/* 139 */     this.target = target;
/* 140 */     this.ignoreFutureMigrations = ignoreFutureMigrations;
/* 141 */     this.ignoreFailedFutureMigration = ignoreFailedFutureMigration;
/* 142 */     this.outOfOrder = outOfOrder;
/* 143 */     this.callbacks = callbacks;
/*     */     
/* 145 */     this.dbSupportUserObjects = DbSupportFactory.createDbSupport(connectionUserObjects, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int migrate() throws FlywayException {
/*     */     try {
/* 156 */       for (FlywayCallback callback : this.callbacks) {
/* 157 */         (new TransactionTemplate(this.connectionUserObjects)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 160 */                 DbMigrate.this.dbSupportUserObjects.changeCurrentSchemaTo(DbMigrate.this.schema);
/* 161 */                 callback.beforeMigrate(DbMigrate.this.connectionUserObjects);
/* 162 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 167 */       StopWatch stopWatch = new StopWatch();
/* 168 */       stopWatch.start();
/*     */       
/* 170 */       int migrationSuccessCount = 0;
/*     */       while (true) {
/* 172 */         final boolean firstRun = (migrationSuccessCount == 0);
/* 173 */         boolean done = ((Boolean)(new TransactionTemplate(this.connectionMetaDataTable, false)).execute(new TransactionCallback<Boolean>() {
/*     */               public Boolean doInTransaction() {
/* 175 */                 DbMigrate.this.metaDataTable.lock();
/*     */ 
/*     */                 
/* 178 */                 MigrationInfoServiceImpl infoService = new MigrationInfoServiceImpl(DbMigrate.this.migrationResolver, DbMigrate.this.metaDataTable, DbMigrate.this.target, DbMigrate.this.outOfOrder, true, true);
/* 179 */                 infoService.refresh();
/*     */                 
/* 181 */                 MigrationVersion currentSchemaVersion = MigrationVersion.EMPTY;
/* 182 */                 if (infoService.current() != null) {
/* 183 */                   currentSchemaVersion = infoService.current().getVersion();
/*     */                 }
/* 185 */                 if (firstRun) {
/* 186 */                   DbMigrate.LOG.info("Current version of schema " + DbMigrate.this.schema + ": " + currentSchemaVersion);
/*     */                   
/* 188 */                   if (DbMigrate.this.outOfOrder) {
/* 189 */                     DbMigrate.LOG.warn("outOfOrder mode is active. Migration of schema " + DbMigrate.this.schema + " may not be reproducible.");
/*     */                   }
/*     */                 } 
/*     */                 
/* 193 */                 MigrationInfo[] future = infoService.future();
/* 194 */                 if (future.length > 0) {
/* 195 */                   MigrationInfo[] resolved = infoService.resolved();
/* 196 */                   if (resolved.length == 0) {
/* 197 */                     DbMigrate.LOG.warn("Schema " + DbMigrate.this.schema + " has version " + currentSchemaVersion + ", but no migration could be resolved in the configured locations !");
/*     */                   } else {
/*     */                     
/* 200 */                     int offset = resolved.length - 1;
/* 201 */                     while (resolved[offset].getVersion() == null)
/*     */                     {
/* 203 */                       offset--;
/*     */                     }
/* 205 */                     DbMigrate.LOG.warn("Schema " + DbMigrate.this.schema + " has a version (" + currentSchemaVersion + ") that is newer than the latest available migration (" + resolved[offset]
/*     */                         
/* 207 */                         .getVersion() + ") !");
/*     */                   } 
/*     */                 } 
/*     */                 
/* 211 */                 MigrationInfo[] failed = infoService.failed();
/* 212 */                 if (failed.length > 0) {
/* 213 */                   if (failed.length == 1 && failed[0]
/* 214 */                     .getState() == MigrationState.FUTURE_FAILED && (DbMigrate.this
/* 215 */                     .ignoreFutureMigrations || DbMigrate.this.ignoreFailedFutureMigration)) {
/* 216 */                     DbMigrate.LOG.warn("Schema " + DbMigrate.this.schema + " contains a failed future migration to version " + failed[0].getVersion() + " !");
/*     */                   } else {
/* 218 */                     throw new FlywayException("Schema " + DbMigrate.this.schema + " contains a failed migration to version " + failed[0].getVersion() + " !");
/*     */                   } 
/*     */                 }
/*     */                 
/* 222 */                 MigrationInfoImpl[] pendingMigrations = infoService.pending();
/*     */                 
/* 224 */                 if (pendingMigrations.length == 0) {
/* 225 */                   return Boolean.valueOf(true);
/*     */                 }
/*     */ 
/*     */                 
/* 229 */                 boolean isOutOfOrder = (pendingMigrations[0].getVersion() != null && pendingMigrations[0].getVersion().compareTo(currentSchemaVersion) < 0);
/* 230 */                 return DbMigrate.this.applyMigration(pendingMigrations[0], isOutOfOrder);
/*     */               }
/*     */             })).booleanValue();
/* 233 */         if (done) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 238 */         migrationSuccessCount++;
/*     */       } 
/*     */       
/* 241 */       stopWatch.stop();
/*     */       
/* 243 */       logSummary(migrationSuccessCount, stopWatch.getTotalTimeMillis());
/*     */       
/* 245 */       for (FlywayCallback callback : this.callbacks) {
/* 246 */         (new TransactionTemplate(this.connectionUserObjects)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 249 */                 DbMigrate.this.dbSupportUserObjects.changeCurrentSchemaTo(DbMigrate.this.schema);
/* 250 */                 callback.afterMigrate(DbMigrate.this.connectionUserObjects);
/* 251 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 256 */       return migrationSuccessCount;
/*     */     } finally {
/* 258 */       this.dbSupportUserObjects.restoreCurrentSchema();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logSummary(int migrationSuccessCount, long executionTime) {
/* 269 */     if (migrationSuccessCount == 0) {
/* 270 */       LOG.info("Schema " + this.schema + " is up to date. No migration necessary.");
/*     */       
/*     */       return;
/*     */     } 
/* 274 */     if (migrationSuccessCount == 1) {
/* 275 */       LOG.info("Successfully applied 1 migration to schema " + this.schema + " (execution time " + TimeFormat.format(executionTime) + ").");
/*     */     } else {
/* 277 */       LOG.info("Successfully applied " + migrationSuccessCount + " migrations to schema " + this.schema + " (execution time " + TimeFormat.format(executionTime) + ").");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean applyMigration(final MigrationInfoImpl migration, boolean isOutOfOrder) {
/*     */     final String migrationText;
/* 289 */     MigrationVersion version = migration.getVersion();
/*     */     
/* 291 */     if (version != null) {
/* 292 */       migrationText = "schema " + this.schema + " to version " + version + " - " + migration.getDescription() + (isOutOfOrder ? " (out of order)" : "");
/*     */     } else {
/*     */       
/* 295 */       migrationText = "schema " + this.schema + " with repeatable migration " + migration.getDescription();
/*     */     } 
/* 297 */     LOG.info("Migrating " + migrationText);
/*     */     
/* 299 */     StopWatch stopWatch = new StopWatch();
/* 300 */     stopWatch.start();
/*     */     
/*     */     try {
/* 303 */       final MigrationExecutor migrationExecutor = migration.getResolvedMigration().getExecutor();
/* 304 */       if (migrationExecutor.executeInTransaction()) {
/* 305 */         (new TransactionTemplate(this.connectionUserObjects)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 308 */                 DbMigrate.this.doMigrate(migration, migrationExecutor, migrationText);
/* 309 */                 return null;
/*     */               }
/*     */             });
/*     */       } else {
/*     */         try {
/* 314 */           doMigrate(migration, migrationExecutor, migrationText);
/* 315 */         } catch (SQLException e) {
/* 316 */           throw new FlywayException("Unable to apply migration", e);
/*     */         } 
/*     */       } 
/* 319 */     } catch (FlywayException e) {
/* 320 */       String failedMsg = "Migration of " + migrationText + " failed!";
/* 321 */       if (this.dbSupport.supportsDdlTransactions()) {
/* 322 */         LOG.error(failedMsg + " Changes successfully rolled back.");
/*     */       } else {
/* 324 */         LOG.error(failedMsg + " Please restore backups and roll back database and code!");
/*     */         
/* 326 */         stopWatch.stop();
/* 327 */         int i = (int)stopWatch.getTotalTimeMillis();
/*     */         
/* 329 */         AppliedMigration appliedMigration1 = new AppliedMigration(version, migration.getDescription(), migration.getType(), migration.getScript(), migration.getResolvedMigration().getChecksum(), i, false);
/* 330 */         this.metaDataTable.addAppliedMigration(appliedMigration1);
/*     */       } 
/* 332 */       throw e;
/*     */     } 
/*     */     
/* 335 */     stopWatch.stop();
/* 336 */     int executionTime = (int)stopWatch.getTotalTimeMillis();
/*     */ 
/*     */     
/* 339 */     AppliedMigration appliedMigration = new AppliedMigration(version, migration.getDescription(), migration.getType(), migration.getScript(), migration.getResolvedMigration().getChecksum(), executionTime, true);
/* 340 */     this.metaDataTable.addAppliedMigration(appliedMigration);
/*     */     
/* 342 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   private void doMigrate(MigrationInfoImpl migration, MigrationExecutor migrationExecutor, String migrationText) throws SQLException {
/* 346 */     this.dbSupportUserObjects.changeCurrentSchemaTo(this.schema);
/*     */     
/* 348 */     for (FlywayCallback callback : this.callbacks) {
/* 349 */       callback.beforeEachMigrate(this.connectionUserObjects, (MigrationInfo)migration);
/*     */     }
/*     */     
/* 352 */     migrationExecutor.execute(this.connectionUserObjects);
/* 353 */     LOG.debug("Successfully completed migration of " + migrationText);
/*     */     
/* 355 */     for (FlywayCallback callback : this.callbacks)
/* 356 */       callback.afterEachMigrate(this.connectionUserObjects, (MigrationInfo)migration); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbMigrate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */