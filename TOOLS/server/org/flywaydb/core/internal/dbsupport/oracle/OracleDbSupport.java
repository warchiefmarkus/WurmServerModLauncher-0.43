/*    */ package org.flywaydb.core.internal.dbsupport.oracle;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OracleDbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public OracleDbSupport(Connection connection) {
/* 37 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 41 */     return "oracle";
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 45 */     return "USER";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 50 */     return this.jdbcTemplate.queryForString("SELECT USER FROM dual", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 55 */     this.jdbcTemplate.execute("ALTER SESSION SET CURRENT_SCHEMA=" + schema, new Object[0]);
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 63 */     return "1";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 67 */     return "0";
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 71 */     return new OracleSqlStatementBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 76 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 81 */     return new OracleSchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\oracle\OracleDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */