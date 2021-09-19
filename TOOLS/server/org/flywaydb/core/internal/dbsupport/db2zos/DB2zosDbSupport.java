/*    */ package org.flywaydb.core.internal.dbsupport.db2zos;
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
/*    */ public class DB2zosDbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public DB2zosDbSupport(Connection connection) {
/* 37 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 41 */     return "db2zos";
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 45 */     return new DB2zosSqlStatementBuilder();
/*    */   }
/*    */   
/*    */   public String getScriptLocation() {
/* 49 */     return "com/googlecode/flyway/core/dbsupport/db2zos/";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 54 */     return this.jdbcTemplate.queryForString("select current_schema from sysibm.sysdummy1", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 59 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 63 */     return "USER";
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 67 */     return true;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 71 */     return "1";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 75 */     return "0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 80 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 85 */     return new DB2zosSchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 90 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2zos\DB2zosDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */