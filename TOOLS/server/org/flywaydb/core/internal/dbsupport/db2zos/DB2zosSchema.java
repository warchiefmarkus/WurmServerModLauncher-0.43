/*     */ package org.flywaydb.core.internal.dbsupport.db2zos;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.internal.dbsupport.Function;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.dbsupport.Type;
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
/*     */ public class DB2zosSchema
/*     */   extends Schema<DB2zosDbSupport>
/*     */ {
/*     */   public DB2zosSchema(JdbcTemplate jdbcTemplate, DB2zosDbSupport dbSupport, String name) {
/*  40 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  47 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sysibm.sysdatabase WHERE name=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  52 */     int objectCount = this.jdbcTemplate.queryForInt("select count(*) from sysibm.systables where dbname = ?", new String[] { this.name });
/*  53 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sysibm.systables where creator = ?", new String[] { this.name });
/*  54 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sysibm.syssequences where schema = ?", new String[] { this.name });
/*  55 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sysibm.sysindexes where dbname = ?", new String[] { this.name });
/*  56 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from sysibm.sysroutines where schema = ?", new String[] { this.name });
/*  57 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  62 */     throw new UnsupportedOperationException("Create Schema - is not supported in db2 on zOS");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  67 */     throw new UnsupportedOperationException("Drop Schema - is not supported in db2 on zOS");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  76 */     for (String dropStatement : generateDropStatements(this.name, "V", "VIEW")) {
/*  77 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  81 */     for (String dropStatement : generateDropStatements(this.name, "A", "ALIAS")) {
/*  82 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */     
/*  85 */     for (Table table : allTables()) {
/*  86 */       table.drop();
/*     */     }
/*     */ 
/*     */     
/*  90 */     for (String dropStatement : generateDropStatementsForTestTable(this.name, "T", "TABLE")) {
/*  91 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  96 */     for (String dropStatement : generateDropStatementsForTablespace(this.name)) {
/*  97 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 101 */     for (String dropStatement : generateDropStatementsForSequences(this.name)) {
/* 102 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 106 */     for (String dropStatement : generateDropStatementsForProcedures(this.name)) {
/* 107 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 111 */     for (String dropStatement : generateDropStatementsForFunctions(this.name)) {
/* 112 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 116 */     for (String dropStatement : generateDropStatementsForUserTypes(this.name)) {
/* 117 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
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
/*     */   private List<String> generateDropStatementsForProcedures(String schema) throws SQLException {
/* 129 */     String dropProcGenQuery = "select rtrim(NAME) from SYSIBM.SYSROUTINES where CAST_FUNCTION = 'N'  and ROUTINETYPE  = 'P' and SCHEMA = '" + schema + "'";
/*     */     
/* 131 */     return buildDropStatements("DROP PROCEDURE", dropProcGenQuery, schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForFunctions(String schema) throws SQLException {
/* 142 */     String dropProcGenQuery = "select rtrim(NAME) from SYSIBM.SYSROUTINES where CAST_FUNCTION = 'N'  and ROUTINETYPE  = 'F' and SCHEMA = '" + schema + "'";
/*     */     
/* 144 */     return buildDropStatements("DROP FUNCTION", dropProcGenQuery, schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForSequences(String schema) throws SQLException {
/* 155 */     String dropSeqGenQuery = "select rtrim(NAME) from SYSIBM.SYSSEQUENCES where SCHEMA = '" + schema + "' and SEQTYPE='S'";
/*     */     
/* 157 */     return buildDropStatements("DROP SEQUENCE", dropSeqGenQuery, schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForTablespace(String schema) throws SQLException {
/* 168 */     String dropTablespaceGenQuery = "select rtrim(NAME) FROM SYSIBM.SYSTABLESPACE where DBNAME = '" + schema + "'";
/* 169 */     return buildDropStatements("DROP TABLESPACE", dropTablespaceGenQuery, schema);
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
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForTestTable(String schema, String tableType, String objectType) throws SQLException {
/* 183 */     String dropTablesGenQuery = "select rtrim(NAME) from SYSIBM.SYSTABLES where TYPE='" + tableType + "' and creator = '" + schema + "'";
/*     */     
/* 185 */     return buildDropStatements("DROP " + objectType, dropTablesGenQuery, schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForUserTypes(String schema) throws SQLException {
/* 196 */     String dropTablespaceGenQuery = "select rtrim(NAME) from SYSIBM.SYSDATATYPES where schema = '" + schema + "'";
/* 197 */     return buildDropStatements("DROP TYPE", dropTablespaceGenQuery, schema);
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
/*     */   
/*     */   private List<String> generateDropStatements(String schema, String tableType, String objectType) throws SQLException {
/* 210 */     String dropTablesGenQuery = "select rtrim(NAME) from SYSIBM.SYSTABLES where TYPE='" + tableType + "' and (DBNAME = '" + schema + "' OR creator = '" + schema + "')";
/*     */     
/* 212 */     return buildDropStatements("DROP " + objectType, dropTablesGenQuery, schema);
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
/*     */   
/*     */   private List<String> buildDropStatements(String dropPrefix, String query, String schema) throws SQLException {
/* 225 */     List<String> dropStatements = new ArrayList<String>();
/* 226 */     List<String> dbObjects = this.jdbcTemplate.queryForStringList(query, new String[0]);
/* 227 */     for (String dbObject : dbObjects) {
/* 228 */       dropStatements.add(dropPrefix + " " + ((DB2zosDbSupport)this.dbSupport).quote(new String[] { schema, dbObject }));
/*     */     } 
/* 230 */     return dropStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 235 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("select rtrim(NAME) from SYSIBM.SYSTABLES where TYPE='T' and DBNAME = ?", new String[] { this.name });
/*     */     
/* 237 */     Table[] tables = new Table[tableNames.size()];
/* 238 */     for (int i = 0; i < tableNames.size(); i++) {
/* 239 */       tables[i] = new DB2zosTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 241 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 246 */     return new DB2zosTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Type getType(String typeName) {
/* 251 */     return new DB2zosType(this.jdbcTemplate, this.dbSupport, this, typeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Function getFunction(String functionName, String... args) {
/* 256 */     return new DB2zosFunction(this.jdbcTemplate, this.dbSupport, this, functionName, args);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2zos\DB2zosSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */