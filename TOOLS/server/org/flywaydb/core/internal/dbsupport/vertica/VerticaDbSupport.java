/*     */ package org.flywaydb.core.internal.dbsupport.vertica;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ import org.flywaydb.core.internal.util.jdbc.RowMapper;
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
/*     */ public class VerticaDbSupport
/*     */   extends DbSupport
/*     */ {
/*     */   public VerticaDbSupport(Connection connection) {
/*  41 */     super(new JdbcTemplate(connection, 0));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  45 */     return "vertica";
/*     */   }
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  49 */     return "current_user";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getOriginalSchema() {
/*  54 */     if (this.originalSchema == null) {
/*  55 */       return null;
/*     */     }
/*     */     
/*  58 */     String result = this.originalSchema.replace(doQuote("$user"), "").trim();
/*  59 */     if (result.startsWith(",")) {
/*  60 */       result = result.substring(1);
/*     */     }
/*  62 */     if (result.contains(",")) {
/*  63 */       return getSchema(result.substring(0, result.indexOf(",")));
/*     */     }
/*  65 */     return getSchema(result);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  70 */     return this.jdbcTemplate.query("SHOW search_path", new RowMapper<String>()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public String mapRow(ResultSet rs) throws SQLException
/*     */           {
/*  79 */             return rs.getString("setting");
/*     */           }
/*  82 */         }).get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/*  87 */     if (schema.getName().equals(this.originalSchema) || this.originalSchema.startsWith(schema.getName() + ",") || !schema.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  92 */       if (StringUtils.hasText(this.originalSchema)) {
/*  93 */         doChangeCurrentSchemaTo(schema.toString() + "," + this.originalSchema);
/*     */       } else {
/*  95 */         doChangeCurrentSchemaTo(schema.toString());
/*     */       } 
/*  97 */     } catch (SQLException e) {
/*  98 */       throw new FlywayException("Error setting current schema to " + schema, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/* 104 */     if (!StringUtils.hasLength(schema)) {
/* 105 */       this.jdbcTemplate.execute("SET search_path = v_catalog", new Object[0]);
/*     */       return;
/*     */     } 
/* 108 */     this.jdbcTemplate.execute("SET search_path = " + schema, new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public String getBooleanTrue() {
/* 116 */     return "TRUE";
/*     */   }
/*     */   
/*     */   public String getBooleanFalse() {
/* 120 */     return "FALSE";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 124 */     return (SqlStatementBuilder)new VerticaStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 129 */     return "\"" + StringUtils.replaceAll(identifier, "\"", "\"\"") + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 134 */     return new VerticaSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 139 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\vertica\VerticaDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */