/*     */ package org.flywaydb.core.internal.dbsupport.mysql;
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
/*     */ public class MySQLSchema
/*     */   extends Schema<MySQLDbSupport>
/*     */ {
/*     */   public MySQLSchema(JdbcTemplate jdbcTemplate, MySQLDbSupport dbSupport, String name) {
/*  39 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  44 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  49 */     int objectCount = this.jdbcTemplate.queryForInt("Select (Select count(*) from information_schema.TABLES Where TABLE_SCHEMA=?) + (Select count(*) from information_schema.VIEWS Where TABLE_SCHEMA=?) + (Select count(*) from information_schema.TABLE_CONSTRAINTS Where TABLE_SCHEMA=?) + (Select count(*) from information_schema.EVENTS Where EVENT_SCHEMA=?) + (Select count(*) from information_schema.TRIGGERS Where TRIGGER_SCHEMA=?) + (Select count(*) from information_schema.ROUTINES Where ROUTINE_SCHEMA=?)", new String[] { this.name, this.name, this.name, this.name, this.name, this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  63 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((MySQLDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  68 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((MySQLDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  73 */     for (String statement : cleanEvents()) {
/*  74 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  77 */     for (String statement : cleanRoutines()) {
/*  78 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  81 */     for (String statement : cleanViews()) {
/*  82 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  85 */     this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0", new Object[0]);
/*  86 */     for (Table table : allTables()) {
/*  87 */       table.drop();
/*     */     }
/*  89 */     this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanEvents() throws SQLException {
/* 100 */     List<Map<String, String>> eventNames = this.jdbcTemplate.queryForList("SELECT event_name FROM information_schema.events WHERE event_schema=?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */     
/* 104 */     List<String> statements = new ArrayList<String>();
/* 105 */     for (Map<String, String> row : eventNames) {
/* 106 */       statements.add("DROP EVENT " + ((MySQLDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("event_name") }));
/*     */     } 
/* 108 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> cleanRoutines() throws SQLException {
/* 119 */     List<Map<String, String>> routineNames = this.jdbcTemplate.queryForList("SELECT routine_name, routine_type FROM information_schema.routines WHERE routine_schema=?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */     
/* 123 */     List<String> statements = new ArrayList<String>();
/* 124 */     for (Map<String, String> row : routineNames) {
/* 125 */       String routineName = row.get("routine_name");
/* 126 */       String routineType = row.get("routine_type");
/* 127 */       statements.add("DROP " + routineType + " " + ((MySQLDbSupport)this.dbSupport).quote(new String[] { this.name, routineName }));
/*     */     } 
/* 129 */     return statements;
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
/* 140 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM information_schema.views WHERE table_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/* 143 */     List<String> statements = new ArrayList<String>();
/* 144 */     for (String viewName : viewNames) {
/* 145 */       statements.add("DROP VIEW " + ((MySQLDbSupport)this.dbSupport).quote(new String[] { this.name, viewName }));
/*     */     } 
/* 147 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 152 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM information_schema.tables WHERE table_schema=? AND table_type='BASE TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/* 155 */     Table[] tables = new Table[tableNames.size()];
/* 156 */     for (int i = 0; i < tableNames.size(); i++) {
/* 157 */       tables[i] = new MySQLTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 159 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 164 */     return new MySQLTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\mysql\MySQLSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */