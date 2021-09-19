/*    */ package org.flywaydb.core.internal.dbsupport.sqlite;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.List;
/*    */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*    */ import org.flywaydb.core.internal.dbsupport.Schema;
/*    */ import org.flywaydb.core.internal.dbsupport.Table;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SQLiteSchema
/*    */   extends Schema<SQLiteDbSupport>
/*    */ {
/* 31 */   private static final Log LOG = LogFactory.getLog(SQLiteSchema.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SQLiteSchema(JdbcTemplate jdbcTemplate, SQLiteDbSupport dbSupport, String name) {
/* 41 */     super(jdbcTemplate, dbSupport, name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doExists() throws SQLException {
/*    */     try {
/* 47 */       doAllTables();
/* 48 */       return true;
/* 49 */     } catch (SQLException e) {
/* 50 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doEmpty() throws SQLException {
/* 56 */     Table[] tables = allTables();
/* 57 */     return (tables.length == 0 || (tables.length == 1 && "android_metadata".equals(tables[0].getName())));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doCreate() throws SQLException {
/* 62 */     LOG.info("SQLite does not support creating schemas. Schema not created: " + this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDrop() throws SQLException {
/* 67 */     LOG.info("SQLite does not support dropping schemas. Schema not dropped: " + this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doClean() throws SQLException {
/* 72 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT tbl_name FROM " + ((SQLiteDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + ".sqlite_master WHERE type='view'", new String[0]);
/* 73 */     for (String viewName : viewNames) {
/* 74 */       this.jdbcTemplate.executeStatement("DROP VIEW " + ((SQLiteDbSupport)this.dbSupport).quote(new String[] { this.name, viewName }));
/*    */     } 
/*    */     
/* 77 */     for (Table table : allTables()) {
/* 78 */       table.drop();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected Table[] doAllTables() throws SQLException {
/* 84 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT tbl_name FROM " + ((SQLiteDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + ".sqlite_master WHERE type='table'", new String[0]);
/*    */     
/* 86 */     Table[] tables = new Table[tableNames.size()];
/* 87 */     for (int i = 0; i < tableNames.size(); i++) {
/* 88 */       tables[i] = new SQLiteTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*    */     }
/* 90 */     return tables;
/*    */   }
/*    */ 
/*    */   
/*    */   public Table getTable(String tableName) {
/* 95 */     return new SQLiteTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sqlite\SQLiteSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */