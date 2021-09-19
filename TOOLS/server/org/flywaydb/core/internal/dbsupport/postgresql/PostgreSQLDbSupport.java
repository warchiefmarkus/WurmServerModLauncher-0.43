/*     */ package org.flywaydb.core.internal.dbsupport.postgresql;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ import org.postgresql.copy.CopyManager;
/*     */ import org.postgresql.core.BaseConnection;
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
/*     */ public class PostgreSQLDbSupport
/*     */   extends DbSupport
/*     */ {
/*     */   public PostgreSQLDbSupport(Connection connection) {
/*  43 */     super(new JdbcTemplate(connection, 0));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  47 */     return "postgresql";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  51 */     return "current_user";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getOriginalSchema() {
/*  56 */     if (this.originalSchema == null) {
/*  57 */       return null;
/*     */     }
/*     */     
/*  60 */     return getSchema(getFirstSchemaFromSearchPath(this.originalSchema));
/*     */   }
/*     */   
/*     */   String getFirstSchemaFromSearchPath(String searchPath) {
/*  64 */     String result = searchPath.replace(doQuote("$user"), "").trim();
/*  65 */     if (result.startsWith(",")) {
/*  66 */       result = result.substring(1);
/*     */     }
/*  68 */     if (result.contains(",")) {
/*  69 */       result = result.substring(0, result.indexOf(","));
/*     */     }
/*  71 */     result = result.trim();
/*     */     
/*  73 */     if (result.startsWith("\"") && result.endsWith("\"") && !result.endsWith("\\\"") && result.length() > 1) {
/*  74 */       result = result.substring(1, result.length() - 1);
/*     */     }
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  81 */     return this.jdbcTemplate.queryForString("SHOW search_path", new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/*  86 */     if (schema.getName().equals(this.originalSchema) || this.originalSchema.startsWith(schema.getName() + ",") || !schema.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  91 */       if (StringUtils.hasText(this.originalSchema)) {
/*  92 */         doChangeCurrentSchemaTo(schema.toString() + "," + this.originalSchema);
/*     */       } else {
/*  94 */         doChangeCurrentSchemaTo(schema.toString());
/*     */       } 
/*  96 */     } catch (SQLException e) {
/*  97 */       throw new FlywayException("Error setting current schema to " + schema, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 103 */     if (!StringUtils.hasLength(schema)) {
/* 104 */       this.jdbcTemplate.execute("SELECT set_config('search_path', '', false)", new Object[0]);
/*     */       return;
/*     */     } 
/* 107 */     this.jdbcTemplate.execute("SET search_path = " + schema, new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/* 115 */     return "TRUE";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/* 119 */     return "FALSE";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 123 */     return new PostgreSQLSqlStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 128 */     return "\"" + StringUtils.replaceAll(identifier, "\"", "\"\"") + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 133 */     return new PostgreSQLSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void executePgCopy(Connection connection, String sql) throws SQLException {
/* 143 */     int split = sql.indexOf(";");
/* 144 */     String statement = sql.substring(0, split);
/* 145 */     String data = sql.substring(split + 1).trim();
/*     */     
/* 147 */     CopyManager copyManager = new CopyManager(connection.<BaseConnection>unwrap(BaseConnection.class));
/*     */     try {
/* 149 */       copyManager.copyIn(statement, new StringReader(data));
/* 150 */     } catch (IOException e) {
/* 151 */       throw new SQLException("Unable to execute COPY operation", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\postgresql\PostgreSQLDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */