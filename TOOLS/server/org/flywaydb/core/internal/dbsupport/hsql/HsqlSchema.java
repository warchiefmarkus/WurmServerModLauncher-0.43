/*     */ package org.flywaydb.core.internal.dbsupport.hsql;
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
/*     */ public class HsqlSchema
/*     */   extends Schema<HsqlDbSupport>
/*     */ {
/*     */   public HsqlSchema(JdbcTemplate jdbcTemplate, HsqlDbSupport dbSupport, String name) {
/*  38 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  43 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT (*) FROM information_schema.system_schemas WHERE table_schem=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  48 */     return ((allTables()).length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  53 */     String user = this.jdbcTemplate.queryForString("SELECT USER() FROM (VALUES(0))", new String[0]);
/*  54 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((HsqlDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " AUTHORIZATION " + user, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  59 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((HsqlDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " CASCADE", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  64 */     for (Table table : allTables()) {
/*  65 */       table.drop();
/*     */     }
/*     */     
/*  68 */     for (String statement : generateDropStatementsForSequences()) {
/*  69 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForSequences() throws SQLException {
/*  80 */     List<String> sequenceNames = this.jdbcTemplate.queryForStringList("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_SCHEMA = ?", new String[] { this.name });
/*     */ 
/*     */     
/*  83 */     List<String> statements = new ArrayList<String>();
/*  84 */     for (String seqName : sequenceNames) {
/*  85 */       statements.add("DROP SEQUENCE " + ((HsqlDbSupport)this.dbSupport).quote(new String[] { this.name, seqName }));
/*     */     } 
/*     */     
/*  88 */     return statements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/*  93 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_SCHEM = ? AND TABLE_TYPE = 'TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/*  96 */     Table[] tables = new Table[tableNames.size()];
/*  97 */     for (int i = 0; i < tableNames.size(); i++) {
/*  98 */       tables[i] = new HsqlTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 100 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 105 */     return new HsqlTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\hsql\HsqlSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */