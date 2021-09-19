/*     */ package org.flywaydb.core.internal.dbsupport.phoenix;
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
/*     */ 
/*     */ public class PhoenixDbSupport
/*     */   extends DbSupport
/*     */ {
/*  36 */   private static final Log LOG = LogFactory.getLog(PhoenixDbSupport.class);
/*     */   
/*     */   public PhoenixDbSupport(Connection connection) {
/*  39 */     super(new JdbcTemplate(connection, 12));
/*     */   }
/*     */   
/*     */   public String getDbName() {
/*  43 */     return "phoenix";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema getOriginalSchema() {
/*  49 */     return getSchema(this.originalSchema);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String quote(String... identifiers) {
/*  55 */     String result = "";
/*     */     
/*  57 */     boolean first = true;
/*  58 */     boolean lastNull = false;
/*  59 */     for (String identifier : identifiers) {
/*  60 */       if (!first && !lastNull) {
/*  61 */         result = result + ".";
/*     */       }
/*  63 */       first = false;
/*  64 */       if (identifier == null) {
/*  65 */         lastNull = true;
/*     */       } else {
/*     */         
/*  68 */         result = result + doQuote(identifier);
/*  69 */         lastNull = false;
/*     */       } 
/*     */     } 
/*     */     
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String doGetCurrentSchemaName() throws SQLException {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void changeCurrentSchemaTo(Schema schema) {
/*  83 */     LOG.info("Phoenix does not support setting the schema. Default schema NOT changed to " + schema);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
/*  88 */     LOG.info("Phoenix does not support setting the schema. Default schema NOT changed to " + schema);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentUserFunction() {
/*  93 */     String userName = null;
/*     */     try {
/*  95 */       userName = this.jdbcTemplate.getMetaData().getUserName();
/*  96 */     } catch (SQLException sQLException) {}
/*     */     
/*  98 */     return userName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDdlTransactions() {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBooleanTrue() {
/* 108 */     return "TRUE";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBooleanFalse() {
/* 113 */     return "FALSE";
/*     */   }
/*     */   
/*     */   public SqlStatementBuilder createSqlStatementBuilder() {
/* 117 */     return new SqlStatementBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String doQuote(String identifier) {
/* 122 */     return "\"" + identifier + "\"";
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(String name) {
/* 127 */     return new PhoenixSchema(this.jdbcTemplate, this, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean catalogIsSchema() {
/* 132 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\phoenix\PhoenixDbSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */