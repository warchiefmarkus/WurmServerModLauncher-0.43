/*    */ package org.flywaydb.core.internal.dbsupport.derby;
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
/*    */ public class DerbyDbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public DerbyDbSupport(Connection connection) {
/* 37 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 41 */     return "derby";
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 45 */     return "CURRENT_USER";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 50 */     return this.jdbcTemplate.queryForString("SELECT CURRENT SCHEMA FROM SYSIBM.SYSDUMMY1", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 55 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 59 */     return true;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 63 */     return "true";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 67 */     return "false";
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 71 */     return new DerbySqlStatementBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 76 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 81 */     return new DerbySchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\derby\DerbyDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */