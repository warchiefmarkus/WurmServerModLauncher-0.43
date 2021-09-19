/*     */ package org.flywaydb.core.internal.command;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.metadatatable.AppliedMigration;
/*     */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
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
/*     */ public class DbBaseline
/*     */ {
/*  37 */   private static final Log LOG = LogFactory.getLog(DbBaseline.class);
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
/*     */   private final MetaDataTable metaDataTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MigrationVersion baselineVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String baselineDescription;
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
/*     */   private final Schema schema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DbBaseline(Connection connection, DbSupport dbSupport, MetaDataTable metaDataTable, Schema schema, MigrationVersion baselineVersion, String baselineDescription, FlywayCallback[] callbacks) {
/*  87 */     this.connection = connection;
/*  88 */     this.dbSupport = dbSupport;
/*  89 */     this.metaDataTable = metaDataTable;
/*  90 */     this.schema = schema;
/*  91 */     this.baselineVersion = baselineVersion;
/*  92 */     this.baselineDescription = baselineDescription;
/*  93 */     this.callbacks = callbacks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void baseline() {
/*     */     try {
/* 101 */       for (FlywayCallback callback : this.callbacks) {
/* 102 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 105 */                 DbBaseline.this.dbSupport.changeCurrentSchemaTo(DbBaseline.this.schema);
/* 106 */                 callback.beforeBaseline(DbBaseline.this.connection);
/* 107 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 112 */       (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Void>() {
/*     */             public Void doInTransaction() {
/* 114 */               DbBaseline.this.dbSupport.changeCurrentSchemaTo(DbBaseline.this.schema);
/* 115 */               if (DbBaseline.this.metaDataTable.hasAppliedMigrations()) {
/* 116 */                 throw new FlywayException("Unable to baseline metadata table " + DbBaseline.this.metaDataTable + " as it already contains migrations");
/*     */               }
/* 118 */               if (DbBaseline.this.metaDataTable.hasBaselineMarker()) {
/* 119 */                 AppliedMigration baselineMarker = DbBaseline.this.metaDataTable.getBaselineMarker();
/* 120 */                 if (DbBaseline.this.baselineVersion.equals(baselineMarker.getVersion()) && DbBaseline.this
/* 121 */                   .baselineDescription.equals(baselineMarker.getDescription())) {
/* 122 */                   DbBaseline.LOG.info("Metadata table " + DbBaseline.this.metaDataTable + " already initialized with (" + DbBaseline.this
/* 123 */                       .baselineVersion + "," + DbBaseline.this.baselineDescription + "). Skipping.");
/* 124 */                   return null;
/*     */                 } 
/* 126 */                 throw new FlywayException("Unable to baseline metadata table " + DbBaseline.this.metaDataTable + " with (" + DbBaseline.this
/* 127 */                     .baselineVersion + "," + DbBaseline.this.baselineDescription + ") as it has already been initialized with (" + baselineMarker
/*     */                     
/* 129 */                     .getVersion() + "," + baselineMarker.getDescription() + ")");
/*     */               } 
/* 131 */               if (DbBaseline.this.metaDataTable.hasSchemasMarker() && DbBaseline.this.baselineVersion.equals(MigrationVersion.fromVersion("0"))) {
/* 132 */                 throw new FlywayException("Unable to baseline metadata table " + DbBaseline.this.metaDataTable + " with version 0 as this version was used for schema creation");
/*     */               }
/* 134 */               DbBaseline.this.metaDataTable.addBaselineMarker(DbBaseline.this.baselineVersion, DbBaseline.this.baselineDescription);
/*     */               
/* 136 */               return null;
/*     */             }
/*     */           });
/*     */       
/* 140 */       LOG.info("Successfully baselined schema with version: " + this.baselineVersion);
/*     */       
/* 142 */       for (FlywayCallback callback : this.callbacks) {
/* 143 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 146 */                 DbBaseline.this.dbSupport.changeCurrentSchemaTo(DbBaseline.this.schema);
/* 147 */                 callback.afterBaseline(DbBaseline.this.connection);
/* 148 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */     } finally {
/* 153 */       this.dbSupport.restoreCurrentSchema();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbBaseline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */