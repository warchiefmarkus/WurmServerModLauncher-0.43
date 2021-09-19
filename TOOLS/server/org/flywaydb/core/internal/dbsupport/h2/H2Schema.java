/*     */ package org.flywaydb.core.internal.dbsupport.h2;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
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
/*     */ public class H2Schema
/*     */   extends Schema<H2DbSupport>
/*     */ {
/*  33 */   private static final Log LOG = LogFactory.getLog(H2Schema.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public H2Schema(JdbcTemplate jdbcTemplate, H2DbSupport dbSupport, String name) {
/*  43 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  48 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM INFORMATION_SCHEMA.schemata WHERE schema_name=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  53 */     return ((allTables()).length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  58 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((H2DbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  63 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((H2DbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  68 */     for (Table table : allTables()) {
/*  69 */       table.drop();
/*     */     }
/*     */     
/*  72 */     List<String> sequenceNames = listObjectNames("SEQUENCE", "IS_GENERATED = false");
/*  73 */     for (String statement : generateDropStatements("SEQUENCE", sequenceNames, "")) {
/*  74 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  77 */     List<String> constantNames = listObjectNames("CONSTANT", "");
/*  78 */     for (String statement : generateDropStatements("CONSTANT", constantNames, "")) {
/*  79 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  82 */     List<String> domainNames = listObjectNames("DOMAIN", "");
/*  83 */     if (!domainNames.isEmpty()) {
/*  84 */       if (this.name.equals(((H2DbSupport)this.dbSupport).getCurrentSchemaName())) {
/*  85 */         for (String statement : generateDropStatementsForCurrentSchema("DOMAIN", domainNames, "")) {
/*  86 */           this.jdbcTemplate.execute(statement, new Object[0]);
/*     */         }
/*     */       } else {
/*  89 */         LOG.error("Unable to drop DOMAIN objects in schema " + ((H2DbSupport)this.dbSupport).quote(new String[] { this.name }) + " due to H2 bug! (More info: http://code.google.com/p/h2database/issues/detail?id=306)");
/*     */       } 
/*     */     }
/*     */   }
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
/*     */   private List<String> generateDropStatements(String objectType, List<String> objectNames, String dropStatementSuffix) {
/* 104 */     List<String> statements = new ArrayList<String>();
/* 105 */     for (String objectName : objectNames) {
/*     */       
/* 107 */       String dropStatement = "DROP " + objectType + ((H2DbSupport)this.dbSupport).quote(new String[] { this.name, objectName }) + " " + dropStatementSuffix;
/*     */       
/* 109 */       statements.add(dropStatement);
/*     */     } 
/* 111 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForCurrentSchema(String objectType, List<String> objectNames, String dropStatementSuffix) {
/* 123 */     List<String> statements = new ArrayList<String>();
/* 124 */     for (String objectName : objectNames) {
/*     */       
/* 126 */       String dropStatement = "DROP " + objectType + ((H2DbSupport)this.dbSupport).quote(new String[] { objectName }) + " " + dropStatementSuffix;
/*     */       
/* 128 */       statements.add(dropStatement);
/*     */     } 
/* 130 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 135 */     List<String> tableNames = listObjectNames("TABLE", "TABLE_TYPE = 'TABLE'");
/*     */     
/* 137 */     Table[] tables = new Table[tableNames.size()];
/* 138 */     for (int i = 0; i < tableNames.size(); i++) {
/* 139 */       tables[i] = new H2Table(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 141 */     return tables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> listObjectNames(String objectType, String querySuffix) throws SQLException {
/* 153 */     String query = "SELECT " + objectType + "_NAME FROM INFORMATION_SCHEMA." + objectType + "s WHERE " + objectType + "_schema = ?";
/* 154 */     if (StringUtils.hasLength(querySuffix)) {
/* 155 */       query = query + " AND " + querySuffix;
/*     */     }
/*     */     
/* 158 */     return this.jdbcTemplate.queryForStringList(query, new String[] { this.name });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 164 */     return new H2Table(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\h2\H2Schema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */