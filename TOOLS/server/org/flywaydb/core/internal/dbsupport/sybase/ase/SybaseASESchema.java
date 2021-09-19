/*     */ package org.flywaydb.core.internal.dbsupport.sybase.ase;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ public class SybaseASESchema
/*     */   extends Schema<SybaseASEDbSupport>
/*     */ {
/*     */   public SybaseASESchema(JdbcTemplate jdbcTemplate, SybaseASEDbSupport dbSupport, String name) {
/*  33 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  39 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  46 */     return (this.jdbcTemplate.queryForInt("select count(*) from sysobjects ob where (ob.type='U' or ob.type = 'V' or ob.type = 'P' or ob.type = 'TR') and ob.name != 'sysquerymetrics'", new String[0]) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  66 */     dropObjects("U");
/*     */     
/*  68 */     dropObjects("V");
/*     */     
/*  70 */     dropObjects("P");
/*     */     
/*  72 */     dropObjects("TR");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/*  79 */     List<String> tableNames = retrieveAllTableNames();
/*     */     
/*  81 */     Table[] result = new Table[tableNames.size()];
/*     */     
/*  83 */     for (int i = 0; i < tableNames.size(); i++) {
/*  84 */       String tableName = tableNames.get(i);
/*  85 */       result[i] = new SybaseASETable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */     } 
/*     */     
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/*  93 */     return new SybaseASETable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> retrieveAllTableNames() throws SQLException {
/* 102 */     List<String> objNames = this.jdbcTemplate.queryForStringList("select ob.name from sysobjects ob where ob.type=? order by ob.name", new String[] { "U" });
/*     */     
/* 104 */     return objNames;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dropObjects(String sybaseObjType) throws SQLException {
/* 110 */     List<String> objNames = this.jdbcTemplate.queryForStringList("select ob.name from sysobjects ob where ob.type=? order by ob.name", new String[] { sybaseObjType });
/*     */ 
/*     */     
/* 113 */     for (String name : objNames) {
/* 114 */       String sql = "";
/*     */       
/* 116 */       if ("U".equals(sybaseObjType)) {
/* 117 */         sql = "drop table ";
/* 118 */       } else if ("V".equals(sybaseObjType)) {
/* 119 */         sql = "drop view ";
/* 120 */       } else if ("P".equals(sybaseObjType)) {
/*     */         
/* 122 */         sql = "drop procedure ";
/* 123 */       } else if ("TR".equals(sybaseObjType)) {
/* 124 */         sql = "drop trigger ";
/*     */       } else {
/* 126 */         throw new IllegalArgumentException("Unknown database object type " + sybaseObjType);
/*     */       } 
/*     */       
/* 129 */       this.jdbcTemplate.execute(sql + name, new Object[0]);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\sybase\ase\SybaseASESchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */