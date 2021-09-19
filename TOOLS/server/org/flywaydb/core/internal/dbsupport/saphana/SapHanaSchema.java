/*     */ package org.flywaydb.core.internal.dbsupport.saphana;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class SapHanaSchema
/*     */   extends Schema<SapHanaDbSupport>
/*     */ {
/*     */   public SapHanaSchema(JdbcTemplate jdbcTemplate, SapHanaDbSupport dbSupport, String name) {
/*  38 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  43 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM SYS.SCHEMAS WHERE SCHEMA_NAME=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  48 */     int objectCount = this.jdbcTemplate.queryForInt("select count(*) from sys.tables where schema_name = ?", new String[] { this.name });
/*  49 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sys.views where schema_name = ?", new String[] { this.name });
/*  50 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sys.sequences where schema_name = ?", new String[] { this.name });
/*  51 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sys.synonyms where schema_name = ?", new String[] { this.name });
/*  52 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  57 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((SapHanaDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  62 */     clean();
/*  63 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((SapHanaDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " RESTRICT", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  68 */     for (String dropStatement : generateDropStatements("SYNONYM")) {
/*  69 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */     
/*  72 */     for (String dropStatement : generateDropStatements("VIEW")) {
/*  73 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */     
/*  76 */     for (Table table : allTables()) {
/*  77 */       table.drop();
/*     */     }
/*     */     
/*  80 */     for (String dropStatement : generateDropStatements("SEQUENCE")) {
/*  81 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
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
/*     */   private List<String> generateDropStatements(String objectType) throws SQLException {
/*  93 */     List<String> dropStatements = new ArrayList<String>();
/*  94 */     List<String> dbObjects = this.jdbcTemplate.queryForStringList("select " + objectType + "_NAME from SYS." + objectType + "S where SCHEMA_NAME = '" + this.name + "'", new String[0]);
/*     */     
/*  96 */     for (String dbObject : dbObjects) {
/*  97 */       dropStatements.add("DROP " + objectType + " " + ((SapHanaDbSupport)this.dbSupport).quote(new String[] { this.name, dbObject }) + " CASCADE");
/*     */     } 
/*  99 */     return dropStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 104 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("select TABLE_NAME from SYS.TABLES where SCHEMA_NAME = ?", new String[] { this.name });
/* 105 */     Table[] tables = new Table[tableNames.size()];
/* 106 */     for (int i = 0; i < tableNames.size(); i++) {
/* 107 */       tables[i] = new SapHanaTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 109 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 114 */     return new SapHanaTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\saphana\SapHanaSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */