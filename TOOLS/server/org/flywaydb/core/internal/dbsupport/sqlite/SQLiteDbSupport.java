/*    */ package org.flywaydb.core.internal.dbsupport.sqlite;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*    */ 
/*    */ public class SQLiteDbSupport
/*    */   extends DbSupport
/*    */ {
/* 33 */   private static final Log LOG = LogFactory.getLog(SQLiteDbSupport.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLiteDbSupport(Connection connection) {
/* 41 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 45 */     return "sqlite";
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 49 */     return "''";
/*    */   }
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 53 */     return "main";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 58 */     LOG.info("SQLite does not support setting the schema. Default schema NOT changed to " + schema);
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 62 */     return true;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 66 */     return "1";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 70 */     return "0";
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 74 */     return new SQLiteSqlStatementBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 79 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 84 */     return new SQLiteSchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 89 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlite\SQLiteDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */