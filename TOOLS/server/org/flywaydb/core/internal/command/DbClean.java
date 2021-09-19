/*     */ package org.flywaydb.core.internal.command;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
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
/*     */ public class DbClean
/*     */ {
/*  37 */   private static final Log LOG = LogFactory.getLog(DbClean.class);
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
/*     */   
/*     */   private final MetaDataTable metaDataTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Schema[] schemas;
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
/*     */   private boolean cleanDisabled;
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
/*     */   public DbClean(Connection connection, DbSupport dbSupport, MetaDataTable metaDataTable, Schema[] schemas, FlywayCallback[] callbacks, boolean cleanDisabled) {
/*  84 */     this.connection = connection;
/*  85 */     this.dbSupport = dbSupport;
/*  86 */     this.metaDataTable = metaDataTable;
/*  87 */     this.schemas = schemas;
/*  88 */     this.callbacks = callbacks;
/*  89 */     this.cleanDisabled = cleanDisabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clean() throws FlywayException {
/*  98 */     if (this.cleanDisabled) {
/*  99 */       throw new FlywayException("Unable to execute clean as it has been disabled with the \"flyway.cleanDisabled\" property.");
/*     */     }
/*     */     try {
/* 102 */       for (FlywayCallback callback : this.callbacks) {
/* 103 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 106 */                 DbClean.this.dbSupport.changeCurrentSchemaTo(DbClean.this.schemas[0]);
/* 107 */                 callback.beforeClean(DbClean.this.connection);
/* 108 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 113 */       this.dbSupport.changeCurrentSchemaTo(this.schemas[0]);
/* 114 */       boolean dropSchemas = false;
/*     */       try {
/* 116 */         dropSchemas = this.metaDataTable.hasSchemasMarker();
/* 117 */       } catch (Exception e) {
/* 118 */         LOG.error("Error while checking whether the schemas should be dropped", e);
/*     */       } 
/*     */       
/* 121 */       for (Schema schema : this.schemas) {
/* 122 */         if (!schema.exists()) {
/* 123 */           LOG.warn("Unable to clean unknown schema: " + schema);
/*     */ 
/*     */         
/*     */         }
/* 127 */         else if (dropSchemas) {
/* 128 */           dropSchema(schema);
/*     */         } else {
/* 130 */           cleanSchema(schema);
/*     */         } 
/*     */       } 
/*     */       
/* 134 */       for (FlywayCallback callback : this.callbacks) {
/* 135 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 138 */                 DbClean.this.dbSupport.changeCurrentSchemaTo(DbClean.this.schemas[0]);
/* 139 */                 callback.afterClean(DbClean.this.connection);
/* 140 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */     } finally {
/* 145 */       this.dbSupport.restoreCurrentSchema();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dropSchema(final Schema schema) {
/* 156 */     LOG.debug("Dropping schema " + schema + " ...");
/* 157 */     StopWatch stopWatch = new StopWatch();
/* 158 */     stopWatch.start();
/* 159 */     (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Void>() {
/*     */           public Void doInTransaction() {
/* 161 */             schema.drop();
/* 162 */             return null;
/*     */           }
/*     */         });
/* 165 */     stopWatch.stop();
/* 166 */     LOG.info(String.format("Successfully dropped schema %s (execution time %s)", new Object[] { schema, 
/* 167 */             TimeFormat.format(stopWatch.getTotalTimeMillis()) }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanSchema(final Schema schema) {
/* 177 */     LOG.debug("Cleaning schema " + schema + " ...");
/* 178 */     StopWatch stopWatch = new StopWatch();
/* 179 */     stopWatch.start();
/* 180 */     (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Void>() {
/*     */           public Void doInTransaction() {
/* 182 */             schema.clean();
/* 183 */             return null;
/*     */           }
/*     */         });
/* 186 */     stopWatch.stop();
/* 187 */     LOG.info(String.format("Successfully cleaned schema %s (execution time %s)", new Object[] { schema, 
/* 188 */             TimeFormat.format(stopWatch.getTotalTimeMillis()) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbClean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */