/*     */ package org.flywaydb.core.internal.dbsupport.redshift;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.dbsupport.postgresql.PostgreSQLSqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
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
/*     */ public abstract class RedshiftDbSupport
/*     */   extends DbSupport
/*     */ {
/*  34 */   private static final Log LOG = LogFactory.getLog(RedshiftDbSupport.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedshiftDbSupport(JdbcTemplate jdbcTemplate) {
/*  42 */     super(jdbcTemplate);
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  46 */     return "redshift";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  50 */     return "current_user";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getOriginalSchema() {
/*  55 */     if (this.originalSchema == null) {
/*  56 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  60 */     String result = this.originalSchema.replace(doQuote("$user"), "").trim();
/*  61 */     if (result.startsWith(",")) {
/*  62 */       result = result.substring(2);
/*     */     }
/*  64 */     if (result.contains(",")) {
/*  65 */       return getSchema(result.substring(0, result.indexOf(",")));
/*     */     }
/*  67 */     return getSchema(result);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  72 */     String searchPath = this.jdbcTemplate.queryForString("SHOW search_path", new String[0]);
/*  73 */     if (StringUtils.hasText(searchPath) && !searchPath.equals("unset"))
/*     */     {
/*  75 */       if (searchPath.contains("$user") && !searchPath.contains(doQuote("$user"))) {
/*  76 */         searchPath = searchPath.replace("$user", doQuote("$user"));
/*     */       }
/*     */     }
/*  79 */     return searchPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/*  84 */     if (schema.getName().equals(this.originalSchema) || this.originalSchema.startsWith(schema.getName() + ",") || !schema.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  89 */       if (StringUtils.hasText(this.originalSchema) && !this.originalSchema.equals("unset")) {
/*  90 */         doChangeCurrentSchemaTo(schema.toString() + "," + this.originalSchema);
/*     */       } else {
/*  92 */         doChangeCurrentSchemaTo(schema.toString());
/*     */       } 
/*  94 */     } catch (SQLException e) {
/*  95 */       throw new FlywayException("Error setting current schema to " + schema, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 101 */     if (!StringUtils.hasLength(schema) || schema.equals("unset")) {
/*     */       
/* 103 */       this.jdbcTemplate.execute("SELECT set_config('search_path', '', false)", new Object[0]);
/*     */       return;
/*     */     } 
/* 106 */     this.jdbcTemplate.execute("SET search_path = " + schema, new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/* 114 */     return "TRUE";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/* 118 */     return "FALSE";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 122 */     return (SqlStatementBuilder)new PostgreSQLSqlStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 127 */     return "\"" + StringUtils.replaceAll(identifier, "\"", "\"\"") + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 132 */     return new RedshiftSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean detect() {
/*     */     try {
/* 145 */       return (this.jdbcTemplate.queryForInt("select count(*) from information_schema.tables where table_schema = 'pg_catalog' and table_name = 'stl_s3client'", new String[0]) > 0);
/* 146 */     } catch (SQLException e) {
/* 147 */       LOG.error("Unable to check whether this is a Redshift database", e);
/* 148 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\redshift\RedshiftDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */