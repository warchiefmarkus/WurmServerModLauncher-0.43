/*    */ package org.flywaydb.core.internal.dbsupport.h2;
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
/*    */ 
/*    */ public class H2DbSupport
/*    */   extends DbSupport
/*    */ {
/*    */   public H2DbSupport(Connection connection) {
/* 39 */     super(new JdbcTemplate(connection, 12));
/*    */   }
/*    */   
/*    */   public String getDbName() {
/* 43 */     return "h2";
/*    */   }
/*    */   
/*    */   public String getCurrentUserFunction() {
/* 47 */     return "USER()";
/*    */   }
/*    */   
/*    */   protected String doGetCurrentSchemaName() throws SQLException {
/* 51 */     return this.jdbcTemplate.queryForString("CALL SCHEMA()", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 56 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*    */   }
/*    */   
/*    */   public boolean supportsDdlTransactions() {
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   public String getBooleanTrue() {
/* 64 */     return "1";
/*    */   }
/*    */   
/*    */   public String getBooleanFalse() {
/* 68 */     return "0";
/*    */   }
/*    */   
/*    */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 72 */     return new H2SqlStatementBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public String doQuote(String identifier) {
/* 77 */     return "\"" + identifier + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public Schema getSchema(String name) {
/* 82 */     return new H2Schema(this.jdbcTemplate, this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean catalogIsSchema() {
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\h2\H2DbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */