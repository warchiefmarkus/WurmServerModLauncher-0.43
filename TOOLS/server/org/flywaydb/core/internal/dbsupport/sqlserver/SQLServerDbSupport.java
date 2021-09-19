/*     */ package org.flywaydb.core.internal.dbsupport.sqlserver;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
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
/*     */ public class SQLServerDbSupport
/*     */   extends DbSupport
/*     */ {
/*  34 */   private static final Log LOG = LogFactory.getLog(SQLServerDbSupport.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean schemaMessagePrinted;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLServerDbSupport(Connection connection) {
/*  47 */     super(new JdbcTemplate(connection, 12));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  51 */     return "sqlserver";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  55 */     return "SUSER_SNAME()";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  60 */     return this.jdbcTemplate.queryForString("SELECT SCHEMA_NAME()", new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  65 */     if (!schemaMessagePrinted) {
/*  66 */       LOG.info("SQLServer does not support setting the schema for the current session. Default schema NOT changed to " + schema);
/*     */ 
/*     */       
/*  69 */       schemaMessagePrinted = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/*  78 */     return "1";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/*  82 */     return "0";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/*  86 */     return new SQLServerSqlStatementBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String escapeIdentifier(String identifier) {
/*  96 */     return StringUtils.replaceAll(identifier, "]", "]]");
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 101 */     return "[" + escapeIdentifier(identifier) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 106 */     return new SQLServerSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 111 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlserver\SQLServerDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */