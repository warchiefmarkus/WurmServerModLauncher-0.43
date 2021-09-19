/*     */ package org.flywaydb.core.internal.dbsupport.sybase.ase;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
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
/*     */ 
/*     */ 
/*     */ public class SybaseASEDbSupport
/*     */   extends DbSupport
/*     */ {
/*  35 */   private static final Log LOG = LogFactory.getLog(SybaseASEDbSupport.class);
/*     */   
/*     */   public SybaseASEDbSupport(Connection connection) {
/*  38 */     super(new JdbcTemplate(connection, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/*  47 */     Schema schema = new SybaseASESchema(this.jdbcTemplate, this, name)
/*     */       {
/*     */         protected boolean doExists() throws SQLException {
/*  50 */           return false;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*     */     try {
/*  56 */       String currentName = doGetCurrentSchemaName();
/*  57 */       if (currentName.equals(name)) {
/*  58 */         schema = new SybaseASESchema(this.jdbcTemplate, this, name);
/*     */       }
/*  60 */     } catch (SQLException e) {
/*  61 */       LOG.error("Unable to obtain current schema, return non-existing schema", e);
/*     */     } 
/*  63 */     return schema;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/*  71 */     return new SybaseASESqlStatementBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDbName() {
/*  79 */     return "sybaseASE";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  87 */     return this.jdbcTemplate.queryForString("select USER_NAME()", new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  95 */     LOG.info("Sybase does not support setting the schema for the current session. Default schema NOT changed to " + schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentUserFunction() {
/* 104 */     return "user_name()";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBooleanTrue() {
/* 120 */     return "1";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBooleanFalse() {
/* 128 */     return "0";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String doQuote(String identifier) {
/* 137 */     return identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sybase\ase\SybaseASEDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */