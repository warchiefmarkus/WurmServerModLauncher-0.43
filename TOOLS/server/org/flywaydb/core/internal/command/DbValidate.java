/*     */ package org.flywaydb.core.internal.command;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.MigrationVersion;
/*     */ import org.flywaydb.core.api.callback.FlywayCallback;
/*     */ import org.flywaydb.core.api.resolver.MigrationResolver;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.info.MigrationInfoServiceImpl;
/*     */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*     */ import org.flywaydb.core.internal.util.Pair;
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
/*     */ public class DbValidate
/*     */ {
/*  42 */   private static final Log LOG = LogFactory.getLog(DbValidate.class);
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
/*     */   
/*     */   private final MigrationResolver migrationResolver;
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
/*     */   private final boolean outOfOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean pending;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean future;
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
/*     */   public DbValidate(Connection connection, DbSupport dbSupport, MetaDataTable metaDataTable, Schema schema, MigrationResolver migrationResolver, MigrationVersion target, boolean outOfOrder, boolean pending, boolean future, FlywayCallback[] callbacks) {
/* 116 */     this.connection = connection;
/* 117 */     this.dbSupport = dbSupport;
/* 118 */     this.metaDataTable = metaDataTable;
/* 119 */     this.schema = schema;
/* 120 */     this.migrationResolver = migrationResolver;
/* 121 */     this.target = target;
/* 122 */     this.outOfOrder = outOfOrder;
/* 123 */     this.pending = pending;
/* 124 */     this.future = future;
/* 125 */     this.callbacks = callbacks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String validate() {
/*     */     try {
/* 135 */       for (FlywayCallback callback : this.callbacks) {
/* 136 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 139 */                 DbValidate.this.dbSupport.changeCurrentSchemaTo(DbValidate.this.schema);
/* 140 */                 callback.beforeValidate(DbValidate.this.connection);
/* 141 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 146 */       LOG.debug("Validating migrations ...");
/* 147 */       StopWatch stopWatch = new StopWatch();
/* 148 */       stopWatch.start();
/*     */       
/* 150 */       Pair<Integer, String> result = (Pair<Integer, String>)(new TransactionTemplate(this.connection)).execute(new TransactionCallback<Pair<Integer, String>>() {
/*     */             public Pair<Integer, String> doInTransaction() {
/* 152 */               DbValidate.this.dbSupport.changeCurrentSchemaTo(DbValidate.this.schema);
/*     */               
/* 154 */               MigrationInfoServiceImpl migrationInfoService = new MigrationInfoServiceImpl(DbValidate.this.migrationResolver, DbValidate.this.metaDataTable, DbValidate.this.target, DbValidate.this.outOfOrder, DbValidate.this.pending, DbValidate.this.future);
/*     */               
/* 156 */               migrationInfoService.refresh();
/*     */               
/* 158 */               int count = (migrationInfoService.all()).length;
/* 159 */               String validationError = migrationInfoService.validate();
/* 160 */               return Pair.of(Integer.valueOf(count), validationError);
/*     */             }
/*     */           });
/*     */       
/* 164 */       stopWatch.stop();
/*     */       
/* 166 */       String error = (String)result.getRight();
/* 167 */       if (error == null) {
/* 168 */         int count = ((Integer)result.getLeft()).intValue();
/* 169 */         if (count == 1) {
/* 170 */           LOG.info(String.format("Successfully validated 1 migration (execution time %s)", new Object[] {
/* 171 */                   TimeFormat.format(stopWatch.getTotalTimeMillis()) }));
/*     */         } else {
/* 173 */           LOG.info(String.format("Successfully validated %d migrations (execution time %s)", new Object[] {
/* 174 */                   Integer.valueOf(count), TimeFormat.format(stopWatch.getTotalTimeMillis())
/*     */                 }));
/*     */         } 
/*     */       } 
/* 178 */       for (FlywayCallback callback : this.callbacks) {
/* 179 */         (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Object>()
/*     */             {
/*     */               public Object doInTransaction() throws SQLException {
/* 182 */                 DbValidate.this.dbSupport.changeCurrentSchemaTo(DbValidate.this.schema);
/* 183 */                 callback.afterValidate(DbValidate.this.connection);
/* 184 */                 return null;
/*     */               }
/*     */             });
/*     */       } 
/*     */       
/* 189 */       return error;
/*     */     } finally {
/* 191 */       this.dbSupport.restoreCurrentSchema();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbValidate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */