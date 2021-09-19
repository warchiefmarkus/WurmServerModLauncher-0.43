/*     */ package org.flywaydb.core.internal.dbsupport.solid;
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
/*     */ public class SolidSchema
/*     */   extends Schema<SolidDbSupport>
/*     */ {
/*     */   public SolidSchema(JdbcTemplate jdbcTemplate, SolidDbSupport dbSupport, String name) {
/*  38 */     super(jdbcTemplate, dbSupport, name.toUpperCase());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  43 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM _SYSTEM.SYS_SCHEMAS WHERE NAME = ?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  48 */     int count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM _SYSTEM.SYS_TABLES WHERE TABLE_SCHEMA = ?", new String[] { this.name });
/*  49 */     if (count > 0)
/*     */     {
/*  51 */       return false;
/*     */     }
/*  53 */     count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM _SYSTEM.SYS_TRIGGERS WHERE TRIGGER_SCHEMA = ?", new String[] { this.name });
/*  54 */     if (count > 0) {
/*  55 */       return false;
/*     */     }
/*  57 */     count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM _SYSTEM.SYS_PROCEDURES WHERE PROCEDURE_SCHEMA = ?", new String[] { this.name });
/*     */     
/*  59 */     if (count > 0) {
/*  60 */       return false;
/*     */     }
/*  62 */     count = this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM _SYSTEM.SYS_FORKEYS WHERE KEY_SCHEMA = ?", new String[] { this.name });
/*  63 */     if (count > 0) {
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  73 */     this.jdbcTemplate.execute("CREATE SCHEMA " + this.name, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  78 */     clean();
/*  79 */     this.jdbcTemplate.execute("DROP SCHEMA " + this.name, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  84 */     for (String statement : dropTriggers()) {
/*  85 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*  87 */     for (String statement : dropProcedures()) {
/*  88 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*  90 */     for (String statement : dropConstraints()) {
/*  91 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*  93 */     for (String statement : dropViews()) {
/*  94 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  98 */     for (Table table : allTables()) {
/*  99 */       table.drop();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 105 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT TABLE_NAME FROM _SYSTEM.SYS_TABLES WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/* 108 */     Table[] tables = new Table[tableNames.size()];
/* 109 */     for (int i = 0; i < tableNames.size(); i++) {
/* 110 */       tables[i] = new SolidTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/*     */     
/* 113 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 118 */     return new SolidTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */   
/*     */   private Iterable<String> dropTriggers() throws SQLException {
/* 122 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 124 */     for (Map<String, String> item : (Iterable<Map<String, String>>)this.jdbcTemplate.queryForList("SELECT TRIGGER_NAME FROM _SYSTEM.SYS_TRIGGERS WHERE TRIGGER_SCHEMA = ?", new String[] { this.name })) {
/*     */       
/* 126 */       statements.add("DROP TRIGGER " + ((SolidDbSupport)this.dbSupport).quote(new String[] { this.name, item.get("TRIGGER_NAME") }));
/*     */     } 
/*     */     
/* 129 */     return statements;
/*     */   }
/*     */   
/*     */   private Iterable<String> dropProcedures() throws SQLException {
/* 133 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 135 */     for (Map<String, String> item : (Iterable<Map<String, String>>)this.jdbcTemplate.queryForList("SELECT PROCEDURE_NAME FROM _SYSTEM.SYS_PROCEDURES WHERE PROCEDURE_SCHEMA = ?", new String[] { this.name })) {
/*     */       
/* 137 */       statements.add("DROP PROCEDURE " + ((SolidDbSupport)this.dbSupport).quote(new String[] { this.name, item.get("PROCEDURE_NAME") }));
/*     */     } 
/*     */     
/* 140 */     return statements;
/*     */   }
/*     */   
/*     */   private Iterable<String> dropConstraints() throws SQLException {
/* 144 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 146 */     for (Map<String, String> item : (Iterable<Map<String, String>>)this.jdbcTemplate.queryForList("SELECT TABLE_NAME, KEY_NAME FROM _SYSTEM.SYS_FORKEYS, _SYSTEM.SYS_TABLES WHERE SYS_FORKEYS.KEY_SCHEMA = ? AND SYS_FORKEYS.CREATE_REL_ID = SYS_FORKEYS.REF_REL_ID AND SYS_FORKEYS.CREATE_REL_ID = SYS_TABLES.ID", new String[] { this.name })) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       statements.add("ALTER TABLE " + ((SolidDbSupport)this.dbSupport)
/* 152 */           .quote(new String[] { this.name, item.get("TABLE_NAME") }) + " DROP CONSTRAINT " + ((SolidDbSupport)this.dbSupport)
/* 153 */           .quote(new String[] { item.get("KEY_NAME") }));
/*     */     } 
/*     */     
/* 156 */     return statements;
/*     */   }
/*     */   
/*     */   private Iterable<String> dropViews() throws SQLException {
/* 160 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 162 */     for (Map<String, String> item : (Iterable<Map<String, String>>)this.jdbcTemplate.queryForList("SELECT TABLE_NAME FROM _SYSTEM.SYS_TABLES WHERE TABLE_TYPE = 'VIEW' AND TABLE_SCHEMA = ?", new String[] { this.name })) {
/*     */       
/* 164 */       statements.add("DROP VIEW " + ((SolidDbSupport)this.dbSupport).quote(new String[] { this.name, item.get("TABLE_NAME") }));
/*     */     } 
/*     */     
/* 167 */     return statements;
/*     */   }
/*     */   
/*     */   private void commitWork() throws SQLException {
/* 171 */     this.jdbcTemplate.executeStatement("COMMIT WORK");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\solid\SolidSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */