/*     */ package org.flywaydb.core.internal.dbsupport.hsql;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HsqlDbSupport
/*     */   extends DbSupport
/*     */ {
/*     */   public HsqlDbSupport(Connection connection) {
/*  39 */     super(new JdbcTemplate(connection, 12));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  43 */     return "hsql";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  47 */     return "USER()";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  52 */     ResultSet resultSet = null;
/*  53 */     String schema = null;
/*     */     
/*     */     try {
/*  56 */       resultSet = this.jdbcTemplate.getMetaData().getSchemas();
/*  57 */       while (resultSet.next()) {
/*  58 */         if (resultSet.getBoolean("IS_DEFAULT")) {
/*  59 */           schema = resultSet.getString("TABLE_SCHEM");
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  64 */       JdbcUtils.closeResultSet(resultSet);
/*     */     } 
/*     */     
/*  67 */     return schema;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  72 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/*  80 */     return "1";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/*  84 */     return "0";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/*  88 */     return new HsqlSqlStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/*  93 */     return "\"" + identifier + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/*  98 */     return new HsqlSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\hsql\HsqlDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */