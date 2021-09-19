/*     */ package org.flywaydb.core.internal.dbsupport.mysql;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.UUID;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
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
/*     */ public class MySQLDbSupport
/*     */   extends DbSupport
/*     */ {
/*  36 */   private static final Log LOG = LogFactory.getLog(MySQLDbSupport.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MySQLDbSupport(Connection connection) {
/*  44 */     super(new JdbcTemplate(connection, 12));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  48 */     return "mysql";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  52 */     return "SUBSTRING_INDEX(USER(),'@',1)";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  57 */     return this.jdbcTemplate.getConnection().getCatalog();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/*  66 */     if (schema.getName().equals(this.originalSchema) || !schema.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  71 */       doChangeCurrentSchemaTo(schema.getName());
/*  72 */     } catch (SQLException e) {
/*  73 */       throw new FlywayException("Error setting current schema to " + schema, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  79 */     if (!StringUtils.hasLength(schema)) {
/*     */       
/*     */       try {
/*  82 */         String newDb = quote(new String[] { UUID.randomUUID().toString() });
/*  83 */         this.jdbcTemplate.execute("CREATE SCHEMA " + newDb, new Object[0]);
/*  84 */         this.jdbcTemplate.execute("USE " + newDb, new Object[0]);
/*  85 */         this.jdbcTemplate.execute("DROP SCHEMA " + newDb, new Object[0]);
/*  86 */       } catch (Exception e) {
/*  87 */         LOG.warn("Unable to restore connection to having no default schema: " + e.getMessage());
/*     */       } 
/*     */     } else {
/*  90 */       this.jdbcTemplate.getConnection().setCatalog(schema);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/*  99 */     return "1";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/* 103 */     return "0";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 107 */     return new MySQLSqlStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 112 */     return "`" + identifier + "`";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 117 */     return new MySQLSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 122 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\mysql\MySQLDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */