/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DbSupport
/*     */ {
/*     */   protected final JdbcTemplate jdbcTemplate;
/*     */   protected final String originalSchema;
/*     */   
/*     */   public DbSupport(JdbcTemplate jdbcTemplate) {
/*  43 */     this.jdbcTemplate = jdbcTemplate;
/*  44 */     this.originalSchema = (jdbcTemplate.getConnection() == null) ? null : getCurrentSchemaName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbcTemplate getJdbcTemplate() {
/*  51 */     return this.jdbcTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Schema getSchema(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract SqlStatementBuilder createSqlStatementBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getDbName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema getOriginalSchema() {
/*  80 */     if (this.originalSchema == null) {
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     return getSchema(this.originalSchema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentSchemaName() {
/*     */     try {
/*  94 */       return doGetCurrentSchemaName();
/*  95 */     } catch (SQLException e) {
/*  96 */       throw new FlywayException("Unable to retrieve the current schema for the connection", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String doGetCurrentSchemaName() throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/* 114 */     if (schema.getName().equals(this.originalSchema) || !schema.exists()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 119 */       doChangeCurrentSchemaTo(schema.toString());
/* 120 */     } catch (SQLException e) {
/* 121 */       throw new FlywayException("Error setting current schema to " + schema, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restoreCurrentSchema() {
/*     */     try {
/* 130 */       doChangeCurrentSchemaTo(this.originalSchema);
/* 131 */     } catch (SQLException e) {
/* 132 */       throw new FlywayException("Error restoring current schema to its original setting", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doChangeCurrentSchemaTo(String paramString) throws SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getCurrentUserFunction();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean supportsDdlTransactions();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getBooleanTrue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getBooleanFalse();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String quote(String... identifiers) {
/* 173 */     String result = "";
/*     */     
/* 175 */     boolean first = true;
/* 176 */     for (String identifier : identifiers) {
/* 177 */       if (!first) {
/* 178 */         result = result + ".";
/*     */       }
/* 180 */       first = false;
/* 181 */       result = result + doQuote(identifier);
/*     */     } 
/*     */     
/* 184 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract String doQuote(String paramString);
/*     */   
/*     */   public abstract boolean catalogIsSchema();
/*     */   
/*     */   public void executePgCopy(Connection connection, String sql) throws SQLException {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\DbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */