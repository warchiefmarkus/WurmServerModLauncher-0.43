/*     */ package org.flywaydb.core.internal.dbsupport.sqlserver;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
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
/*     */ public class SQLServerSchema
/*     */   extends Schema<SQLServerDbSupport>
/*     */ {
/*     */   public SQLServerSchema(JdbcTemplate jdbcTemplate, SQLServerDbSupport dbSupport, String name) {
/*  39 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  44 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  49 */     int objectCount = this.jdbcTemplate.queryForInt("Select count(*) FROM ( Select TABLE_NAME as OBJECT_NAME, TABLE_SCHEMA as OBJECT_SCHEMA from INFORMATION_SCHEMA.TABLES Union Select TABLE_NAME as OBJECT_NAME, TABLE_SCHEMA as OBJECT_SCHEMA from INFORMATION_SCHEMA.VIEWS Union Select CONSTRAINT_NAME as OBJECT_NAME, TABLE_SCHEMA as OBJECT_SCHEMA from INFORMATION_SCHEMA.TABLE_CONSTRAINTS Union Select ROUTINE_NAME as OBJECT_NAME, ROUTINE_SCHEMA as OBJECT_SCHEMA from INFORMATION_SCHEMA.ROUTINES ) R where OBJECT_SCHEMA = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  64 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  69 */     clean();
/*  70 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  75 */     for (String statement : cleanForeignKeys()) {
/*  76 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  79 */     for (String statement : cleanDefaultConstraints()) {
/*  80 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  83 */     for (String statement : cleanRoutines("PROCEDURE")) {
/*  84 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  87 */     for (String statement : cleanViews()) {
/*  88 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  91 */     for (Table table : allTables()) {
/*  92 */       table.drop();
/*     */     }
/*     */     
/*  95 */     for (String statement : cleanRoutines("FUNCTION")) {
/*  96 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  99 */     for (String statement : cleanTypes()) {
/* 100 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 103 */     for (String statement : cleanSynonyms()) {
/* 104 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 107 */     if (this.jdbcTemplate.getMetaData().getDatabaseMajorVersion() >= 11) {
/* 108 */       for (String statement : cleanSequences()) {
/* 109 */         this.jdbcTemplate.execute(statement, new Object[0]);
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
/*     */   private List<String> cleanForeignKeys() throws SQLException {
/* 123 */     List<Map<String, String>> constraintNames = this.jdbcTemplate.queryForList("SELECT table_name, constraint_name FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE constraint_type in ('FOREIGN KEY','CHECK') and table_schema=?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     List<String> statements = new ArrayList<String>();
/* 130 */     for (Map<String, String> row : constraintNames) {
/* 131 */       String tableName = row.get("table_name");
/* 132 */       String constraintName = row.get("constraint_name");
/* 133 */       statements.add("ALTER TABLE " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, tableName }) + " DROP CONSTRAINT " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { constraintName }));
/*     */     } 
/* 135 */     return statements;
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
/*     */   private List<String> cleanDefaultConstraints() throws SQLException {
/* 147 */     List<Map<String, String>> constraintNames = this.jdbcTemplate.queryForList("select t.name as table_name, d.name as constraint_name from sys.tables t inner join sys.default_constraints d on d.parent_object_id = t.object_id\n inner join sys.schemas s on s.schema_id = t.schema_id\n where s.name = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     List<String> statements = new ArrayList<String>();
/* 157 */     for (Map<String, String> row : constraintNames) {
/* 158 */       String tableName = row.get("table_name");
/* 159 */       String constraintName = row.get("constraint_name");
/* 160 */       statements.add("ALTER TABLE " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, tableName }) + " DROP CONSTRAINT " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { constraintName }));
/*     */     } 
/* 162 */     return statements;
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
/*     */   private List<String> cleanRoutines(String routineType) throws SQLException {
/* 174 */     List<Map<String, String>> routineNames = this.jdbcTemplate.queryForList("SELECT routine_name FROM INFORMATION_SCHEMA.ROUTINES WHERE routine_schema=? AND routine_type=?", new String[] { this.name, routineType });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     List<String> statements = new ArrayList<String>();
/* 180 */     for (Map<String, String> row : routineNames) {
/* 181 */       String routineName = row.get("routine_name");
/* 182 */       statements.add("DROP " + routineType + " " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, routineName }));
/*     */     } 
/* 184 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanViews() throws SQLException {
/* 195 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM INFORMATION_SCHEMA.VIEWS WHERE table_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/* 198 */     List<String> statements = new ArrayList<String>();
/* 199 */     for (String viewName : viewNames) {
/* 200 */       statements.add("DROP VIEW " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, viewName }));
/*     */     } 
/* 202 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanTypes() throws SQLException {
/* 213 */     List<String> typeNames = this.jdbcTemplate.queryForStringList("SELECT t.name FROM sys.types t INNER JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE t.is_user_defined = 1 AND s.name = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     List<String> statements = new ArrayList<String>();
/* 220 */     for (String typeName : typeNames) {
/* 221 */       statements.add("DROP TYPE " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, typeName }));
/*     */     } 
/* 223 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanSynonyms() throws SQLException {
/* 234 */     List<String> synonymNames = this.jdbcTemplate.queryForStringList("SELECT sn.name FROM sys.synonyms sn INNER JOIN sys.schemas s ON sn.schema_id = s.schema_id WHERE s.name = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     List<String> statements = new ArrayList<String>();
/* 241 */     for (String synonymName : synonymNames) {
/* 242 */       statements.add("DROP SYNONYM " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, synonymName }));
/*     */     } 
/* 244 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanSequences() throws SQLException {
/* 255 */     List<String> names = this.jdbcTemplate.queryForStringList("SELECT sequence_name FROM INFORMATION_SCHEMA.SEQUENCES WHERE sequence_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/* 258 */     List<String> statements = new ArrayList<String>();
/* 259 */     for (String sequenceName : names) {
/* 260 */       statements.add("DROP SEQUENCE " + ((SQLServerDbSupport)this.dbSupport).quote(new String[] { this.name, sequenceName }));
/*     */     } 
/* 262 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 267 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_type='BASE TABLE' and table_schema=?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     Table[] tables = new Table[tableNames.size()];
/* 273 */     for (int i = 0; i < tableNames.size(); i++) {
/* 274 */       tables[i] = new SQLServerTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 276 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 281 */     return new SQLServerTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlserver\SQLServerSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */