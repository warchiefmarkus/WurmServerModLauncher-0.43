/*    */ package org.flywaydb.core.internal.dbsupport.solid;
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
/*    */ 
/*    */ public class SolidDbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public SolidDbSupport(Connection connection) {
/* 38 */     super(new JdbcTemplate(connection, 0));
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 43 */     return new SolidSchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 48 */     return new SolidSqlStatementBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDbName() {
/* 53 */     return "solid";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 58 */     return this.jdbcTemplate.queryForString("SELECT CURRENT_SCHEMA()", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 63 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 68 */     return "LOGIN_SCHEMA()";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBooleanTrue() {
/* 78 */     return "1";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBooleanFalse() {
/* 83 */     return "0";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doQuote(String identifier) {
/* 88 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 93 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\solid\SolidDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */