/*     */ package org.flywaydb.core.internal.dbsupport.redshift;
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
/*     */ public class RedshiftSchema
/*     */   extends Schema<RedshiftDbSupport>
/*     */ {
/*     */   public RedshiftSchema(JdbcTemplate jdbcTemplate, RedshiftDbSupport dbSupport, String name) {
/*  38 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  43 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM pg_namespace WHERE nspname=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  48 */     int objectCount = this.jdbcTemplate.queryForInt("SELECT count(*) FROM information_schema.tables WHERE table_schema=? AND table_type='BASE TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/*  51 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  56 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((RedshiftDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  61 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((RedshiftDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " CASCADE", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  66 */     for (Table table : allTables()) {
/*  67 */       table.drop();
/*     */     }
/*     */ 
/*     */     
/*  71 */     for (String statement : generateDropStatementsForViews()) {
/*  72 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/*  81 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT t.table_name FROM information_schema.tables t WHERE table_schema=? AND table_type = 'BASE TABLE'", new String[] { this.name });
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
/*  92 */     Table[] tables = new Table[tableNames.size()];
/*  93 */     for (int i = 0; i < tableNames.size(); i++) {
/*  94 */       tables[i] = new RedshiftTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/*  96 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> generateDropStatementsForViews() throws SQLException {
/* 101 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT t.table_name FROM information_schema.tables t WHERE table_schema=? AND table_type = 'VIEW'", new String[] { this.name });
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
/* 112 */     List<String> statements = new ArrayList<String>();
/* 113 */     for (String viewName : viewNames) {
/* 114 */       statements.add("DROP VIEW " + ((RedshiftDbSupport)this.dbSupport).quote(new String[] { this.name, viewName }) + " CASCADE");
/*     */     } 
/* 116 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 121 */     return new RedshiftTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\redshift\RedshiftSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */