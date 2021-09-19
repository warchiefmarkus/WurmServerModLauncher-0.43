/*     */ package org.flywaydb.core.internal.dbsupport.derby;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
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
/*     */ public class DerbySchema
/*     */   extends Schema<DerbyDbSupport>
/*     */ {
/*     */   public DerbySchema(JdbcTemplate jdbcTemplate, DerbyDbSupport dbSupport, String name) {
/*  40 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  45 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT (*) FROM sys.sysschemas WHERE schemaname=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  50 */     return ((allTables()).length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  55 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((DerbyDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  60 */     clean();
/*  61 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((DerbyDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " RESTRICT", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  66 */     List<String> triggerNames = listObjectNames("TRIGGER", "");
/*  67 */     for (String statement : generateDropStatements("TRIGGER", triggerNames, "")) {
/*  68 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  71 */     for (String statement : generateDropStatementsForConstraints()) {
/*  72 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  75 */     List<String> viewNames = listObjectNames("TABLE", "TABLETYPE='V'");
/*  76 */     for (String statement : generateDropStatements("VIEW", viewNames, "")) {
/*  77 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  80 */     for (Table table : allTables()) {
/*  81 */       table.drop();
/*     */     }
/*     */     
/*  84 */     List<String> sequenceNames = listObjectNames("SEQUENCE", "");
/*  85 */     for (String statement : generateDropStatements("SEQUENCE", sequenceNames, "RESTRICT")) {
/*  86 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForConstraints() throws SQLException {
/*  97 */     List<Map<String, String>> results = this.jdbcTemplate.queryForList("SELECT c.constraintname, t.tablename FROM sys.sysconstraints c INNER JOIN sys.systables t ON c.tableid = t.tableid INNER JOIN sys.sysschemas s ON c.schemaid = s.schemaid WHERE c.type = 'F' AND s.schemaname = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     List<String> statements = new ArrayList<String>();
/* 103 */     for (Map<String, String> result : results) {
/*     */       
/* 105 */       String dropStatement = "ALTER TABLE " + ((DerbyDbSupport)this.dbSupport).quote(new String[] { this.name, result.get("TABLENAME") }) + " DROP CONSTRAINT " + ((DerbyDbSupport)this.dbSupport).quote(new String[] { result.get("CONSTRAINTNAME") });
/*     */       
/* 107 */       statements.add(dropStatement);
/*     */     } 
/* 109 */     return statements;
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
/*     */   private List<String> generateDropStatements(String objectType, List<String> objectNames, String dropStatementSuffix) {
/* 121 */     List<String> statements = new ArrayList<String>();
/* 122 */     for (String objectName : objectNames) {
/*     */       
/* 124 */       String dropStatement = "DROP " + objectType + " " + ((DerbyDbSupport)this.dbSupport).quote(new String[] { this.name, objectName }) + " " + dropStatementSuffix;
/*     */       
/* 126 */       statements.add(dropStatement);
/*     */     } 
/* 128 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 133 */     List<String> tableNames = listObjectNames("TABLE", "TABLETYPE='T'");
/*     */     
/* 135 */     Table[] tables = new Table[tableNames.size()];
/* 136 */     for (int i = 0; i < tableNames.size(); i++) {
/* 137 */       tables[i] = new DerbyTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 139 */     return tables;
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
/* 151 */     String query = "SELECT " + objectType + "name FROM sys.sys" + objectType + "s WHERE schemaid in (SELECT schemaid FROM sys.sysschemas where schemaname = ?)";
/* 152 */     if (StringUtils.hasLength(querySuffix)) {
/* 153 */       query = query + " AND " + querySuffix;
/*     */     }
/*     */     
/* 156 */     return this.jdbcTemplate.queryForStringList(query, new String[] { this.name });
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 161 */     return new DerbyTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\derby\DerbySchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */