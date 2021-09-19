/*     */ package org.flywaydb.core.internal.command;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.MigrationInfo;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.api.resolver.ResolvedMigration;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.info.MigrationInfoImpl;
/*     */ import org.flywaydb.core.internal.info.MigrationInfoServiceImpl;
/*     */ import org.flywaydb.core.internal.metadatatable.AppliedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*     */ import org.flywaydb.core.internal.util.ObjectUtils;
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
/*     */ public class DbRepair
/*     */ {
/*  44 */   private static final Log LOG = LogFactory.getLog(DbRepair.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Connection connection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MigrationInfoServiceImpl migrationInfoService;
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
/*     */   private final MetaDataTable metaDataTable;
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
/*     */   private final DbSupport dbSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbRepair(DbSupport dbSupport, Connection connection, Schema schema, MigrationResolver migrationResolver, MetaDataTable metaDataTable, FlywayCallback[] callbacks) {
/*  89 */     this.dbSupport = dbSupport;
/*  90 */     this.connection = connection;
/*  91 */     this.schema = schema;
/*  92 */     this.migrationInfoService = new MigrationInfoServiceImpl(migrationResolver, metaDataTable, MigrationVersion.LATEST, true, true, true);
/*  93 */     this.metaDataTable = metaDataTable;
/*  94 */     this.callbacks = callbacks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void repair() {
/*     */     try {
/* 102 */       for (FlywayCallback callback : this.callbacks) {
/* 103 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 106 */                 DbRepair.this.dbSupport.changeCurrentSchemaTo(DbRepair.this.schema);
/* 107 */                 callback.beforeRepair(DbRepair.this.connection);
/* 108 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 113 */       StopWatch stopWatch = new StopWatch();
/* 114 */       stopWatch.start();
/*     */       
/* 116 */       (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Void>() {
/*     */             public Void doInTransaction() {
/* 118 */               DbRepair.this.dbSupport.changeCurrentSchemaTo(DbRepair.this.schema);
/* 119 */               DbRepair.this.metaDataTable.removeFailedMigrations();
/* 120 */               DbRepair.this.repairChecksums();
/* 121 */               return null;
/*     */             }
/*     */           });
/*     */       
/* 125 */       stopWatch.stop();
/*     */       
/* 127 */       LOG.info("Successfully repaired metadata table " + this.metaDataTable + " (execution time " + 
/* 128 */           TimeFormat.format(stopWatch.getTotalTimeMillis()) + ").");
/* 129 */       if (!this.dbSupport.supportsDdlTransactions()) {
/* 130 */         LOG.info("Manual cleanup of the remaining effects the failed migration may still be required.");
/*     */       }
/*     */       
/* 133 */       for (FlywayCallback callback : this.callbacks) {
/* 134 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 137 */                 DbRepair.this.dbSupport.changeCurrentSchemaTo(DbRepair.this.schema);
/* 138 */                 callback.afterRepair(DbRepair.this.connection);
/* 139 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */     } finally {
/* 144 */       this.dbSupport.restoreCurrentSchema();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void repairChecksums() {
/* 149 */     this.migrationInfoService.refresh();
/* 150 */     for (MigrationInfo migrationInfo : this.migrationInfoService.all()) {
/* 151 */       MigrationInfoImpl migrationInfoImpl = (MigrationInfoImpl)migrationInfo;
/*     */       
/* 153 */       ResolvedMigration resolved = migrationInfoImpl.getResolvedMigration();
/* 154 */       AppliedMigration applied = migrationInfoImpl.getAppliedMigration();
/* 155 */       if (resolved != null && applied != null && 
/* 156 */         !ObjectUtils.nullSafeEquals(resolved.getChecksum(), applied.getChecksum()) && resolved
/* 157 */         .getVersion() != null)
/* 158 */         this.metaDataTable.updateChecksum(migrationInfoImpl.getVersion(), resolved.getChecksum()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbRepair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */