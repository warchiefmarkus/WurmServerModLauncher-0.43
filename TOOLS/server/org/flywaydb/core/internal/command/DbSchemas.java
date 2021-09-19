/*    */ package org.flywaydb.core.internal.command;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.metadatatable.MetaDataTable;
/*    */ import org.flywaydb.core.internal.util.jdbc.TransactionCallback;
/*    */ import org.flywaydb.core.internal.util.jdbc.TransactionTemplate;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DbSchemas
/*    */ {
/* 31 */   private static final Log LOG = LogFactory.getLog(DbSchemas.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Connection connection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Schema[] schemas;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final MetaDataTable metaDataTable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DbSchemas(Connection connection, Schema[] schemas, MetaDataTable metaDataTable) {
/* 56 */     this.connection = connection;
/* 57 */     this.schemas = schemas;
/* 58 */     this.metaDataTable = metaDataTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void create() {
/* 65 */     (new TransactionTemplate(this.connection)).execute(new TransactionCallback<Void>() {
/*    */           public Void doInTransaction() {
/* 67 */             for (Schema schema : DbSchemas.this.schemas) {
/* 68 */               if (schema.exists()) {
/* 69 */                 DbSchemas.LOG.debug("Schema " + schema + " already exists. Skipping schema creation.");
/* 70 */                 return null;
/*    */               } 
/*    */             } 
/*    */             
/* 74 */             for (Schema schema : DbSchemas.this.schemas) {
/* 75 */               DbSchemas.LOG.info("Creating schema " + schema + " ...");
/* 76 */               schema.create();
/*    */             } 
/*    */             
/* 79 */             DbSchemas.this.metaDataTable.addSchemasMarker(DbSchemas.this.schemas);
/*    */             
/* 81 */             return null;
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\command\DbSchemas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */