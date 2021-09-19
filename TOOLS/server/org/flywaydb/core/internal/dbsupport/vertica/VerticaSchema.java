/*     */ package org.flywaydb.core.internal.dbsupport.vertica;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.dbsupport.Type;
/*     */ import org.flywaydb.core.internal.dbsupport.postgresql.PostgreSQLTable;
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
/*     */ public class VerticaSchema
/*     */   extends Schema<VerticaDbSupport>
/*     */ {
/*     */   public VerticaSchema(JdbcTemplate jdbcTemplate, VerticaDbSupport dbSupport, String name) {
/*  32 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  37 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM v_catalog.schemata WHERE schema_name=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  42 */     int objectCount = this.jdbcTemplate.queryForInt("SELECT count(*) FROM v_catalog.all_tables WHERE schema_name=? and table_type = 'TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/*  45 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  50 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((VerticaDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  55 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((VerticaDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " CASCADE", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  60 */     for (String statement : generateDropStatementsForViews()) {
/*  61 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  64 */     for (Table table : allTables()) {
/*  65 */       table.drop();
/*     */     }
/*     */     
/*  68 */     for (String statement : generateDropStatementsForSequences()) {
/*  69 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     for (String statement : generateDropStatementsForFunctions()) {
/*  76 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  79 */     for (Type type : allTypes()) {
/*  80 */       type.drop();
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
/*     */   private List<String> generateDropStatementsForSequences() throws SQLException {
/*  92 */     List<String> sequenceNames = this.jdbcTemplate.queryForStringList("SELECT sequence_name FROM v_catalog.sequences WHERE sequence_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/*  95 */     List<String> statements = new ArrayList<String>();
/*  96 */     for (String sequenceName : sequenceNames) {
/*  97 */       statements.add("DROP SEQUENCE IF EXISTS " + ((VerticaDbSupport)this.dbSupport).quote(new String[] { this.name, sequenceName }));
/*     */     } 
/*     */     
/* 100 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForFunctions() throws SQLException {
/* 111 */     List<Map<String, String>> rows = this.jdbcTemplate.queryForList("select * from user_functions where schema_name = ? and procedure_type = 'User Defined Function'", new String[] { this.name });
/*     */ 
/*     */ 
/*     */     
/* 115 */     List<String> statements = new ArrayList<String>();
/* 116 */     for (Map<String, String> row : rows) {
/* 117 */       statements.add("DROP FUNCTION IF EXISTS " + ((VerticaDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("function_name") }) + "(" + (String)row.get("function_argument_type") + ")");
/*     */     } 
/* 119 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForViews() throws SQLException {
/* 130 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT t.table_name FROM v_catalog.all_tables t WHERE schema_name=? and table_type = 'VIEW'", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     List<String> statements = new ArrayList<String>();
/* 140 */     for (String viewName : viewNames) {
/* 141 */       statements.add("DROP VIEW IF EXISTS " + ((VerticaDbSupport)this.dbSupport).quote(new String[] { this.name, viewName }));
/*     */     } 
/* 143 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 149 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT t.table_name FROM v_catalog.all_tables t WHERE schema_name=? and table_type =  'TABLE'", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     Table[] tables = new Table[tableNames.size()];
/* 160 */     for (int i = 0; i < tableNames.size(); i++) {
/* 161 */       tables[i] = (Table)new PostgreSQLTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 163 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 168 */     return (Table)new PostgreSQLTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\vertica\VerticaSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */