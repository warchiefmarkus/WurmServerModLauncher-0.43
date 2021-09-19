/*    */ package org.flywaydb.core.internal.dbsupport.saphana;
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
/*    */ public class SapHanaDbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public SapHanaDbSupport(Connection connection) {
/* 37 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 41 */     return new SapHanaSqlStatementBuilder();
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 45 */     return "saphana";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 50 */     return this.jdbcTemplate.queryForString("SELECT CURRENT_SCHEMA FROM DUMMY", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 55 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 59 */     return "CURRENT_USER";
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 67 */     return "1";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 71 */     return "0";
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 76 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 81 */     return new SapHanaSchema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\saphana\SapHanaDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */