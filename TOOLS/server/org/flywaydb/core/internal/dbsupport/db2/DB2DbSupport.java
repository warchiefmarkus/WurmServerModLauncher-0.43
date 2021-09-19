/*     */ package org.flywaydb.core.internal.dbsupport.db2;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DB2DbSupport
/*     */   extends DbSupport
/*     */ {
/*     */   private final int majorVersion;
/*     */   
/*     */   public DB2DbSupport(Connection connection) {
/*  43 */     super(new JdbcTemplate(connection, 12));
/*     */     try {
/*  45 */       this.majorVersion = connection.getMetaData().getDatabaseMajorVersion();
/*  46 */     } catch (SQLException e) {
/*  47 */       throw new FlywayException("Unable to determine DB2 major version", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/*  52 */     return new DB2SqlStatementBuilder();
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  56 */     return "db2";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  61 */     return this.jdbcTemplate.queryForString("select current_schema from sysibm.sysdummy1", new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  66 */     this.jdbcTemplate.execute("SET SCHEMA " + schema, new Object[0]);
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  70 */     return "CURRENT_USER";
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
/*     */   
/*     */   public String doQuote(String identifier) {
/*  87 */     return "\"" + identifier + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/*  92 */     return new DB2Schema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDb2MajorVersion() {
/* 104 */     return this.majorVersion;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2\DB2DbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */