/*     */ package org.flywaydb.core.internal.dbsupport.db2;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.flywaydb.core.internal.dbsupport.Function;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.dbsupport.Type;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
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
/*     */ public class DB2Schema
/*     */   extends Schema<DB2DbSupport>
/*     */ {
/*     */   public DB2Schema(JdbcTemplate jdbcTemplate, DB2DbSupport dbSupport, String name) {
/*  42 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  47 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM syscat.schemata WHERE schemaname=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  52 */     int objectCount = this.jdbcTemplate.queryForInt("select count(*) from syscat.tables where tabschema = ?", new String[] { this.name });
/*  53 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.views where viewschema = ?", new String[] { this.name });
/*  54 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.sequences where seqschema = ?", new String[] { this.name });
/*  55 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.indexes where indschema = ?", new String[] { this.name });
/*  56 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.procedures where procschema = ?", new String[] { this.name });
/*  57 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.functions where funcschema = ?", new String[] { this.name });
/*  58 */     objectCount += this.jdbcTemplate.queryForInt("select count(*) from syscat.triggers where trigschema = ?", new String[] { this.name });
/*  59 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  64 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((DB2DbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  69 */     clean();
/*  70 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((DB2DbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " RESTRICT", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  78 */     if (((DB2DbSupport)this.dbSupport).getDb2MajorVersion() >= 10)
/*     */     {
/*  80 */       for (String dropVersioningStatement : generateDropVersioningStatement()) {
/*  81 */         this.jdbcTemplate.execute(dropVersioningStatement, new Object[0]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  86 */     for (String dropStatement : generateDropStatementsForViews()) {
/*  87 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  91 */     for (String dropStatement : generateDropStatements("A", "ALIAS")) {
/*  92 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */     
/*  95 */     for (Table table : allTables()) {
/*  96 */       table.drop();
/*     */     }
/*     */ 
/*     */     
/* 100 */     for (String dropStatement : generateDropStatementsForSequences()) {
/* 101 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 105 */     for (String dropStatement : generateDropStatementsForProcedures()) {
/* 106 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 110 */     for (String dropStatement : generateDropStatementsForTriggers()) {
/* 111 */       this.jdbcTemplate.execute(dropStatement, new Object[0]);
/*     */     }
/*     */     
/* 114 */     for (Function function : allFunctions()) {
/* 115 */       function.drop();
/*     */     }
/*     */     
/* 118 */     for (Type type : allTypes()) {
/* 119 */       type.drop();
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
/*     */   private List<String> generateDropStatementsForProcedures() throws SQLException {
/* 131 */     String dropProcGenQuery = "select PROCNAME from SYSCAT.PROCEDURES where PROCSCHEMA = '" + this.name + "'";
/* 132 */     return buildDropStatements("DROP PROCEDURE", dropProcGenQuery);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForTriggers() throws SQLException {
/* 142 */     String dropTrigGenQuery = "select TRIGNAME from SYSCAT.TRIGGERS where TRIGSCHEMA = '" + this.name + "'";
/* 143 */     return buildDropStatements("DROP TRIGGER", dropTrigGenQuery);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForSequences() throws SQLException {
/* 153 */     String dropSeqGenQuery = "select SEQNAME from SYSCAT.SEQUENCES where SEQSCHEMA = '" + this.name + "' and SEQTYPE='S'";
/*     */     
/* 155 */     return buildDropStatements("DROP SEQUENCE", dropSeqGenQuery);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForViews() throws SQLException {
/* 165 */     String dropSeqGenQuery = "select TABNAME from SYSCAT.TABLES where TABSCHEMA = '" + this.name + "' and TABNAME NOT LIKE '%_V' and TYPE='V'";
/*     */     
/* 167 */     return buildDropStatements("DROP VIEW", dropSeqGenQuery);
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
/*     */   private List<String> generateDropStatements(String tableType, String objectType) throws SQLException {
/* 179 */     String dropTablesGenQuery = "select TABNAME from SYSCAT.TABLES where TYPE='" + tableType + "' and TABSCHEMA = '" + this.name + "'";
/*     */     
/* 181 */     return buildDropStatements("DROP " + objectType, dropTablesGenQuery);
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
/*     */   private List<String> buildDropStatements(String dropPrefix, String query) throws SQLException {
/* 193 */     List<String> dropStatements = new ArrayList<String>();
/* 194 */     List<String> dbObjects = this.jdbcTemplate.queryForStringList(query, new String[0]);
/* 195 */     for (String dbObject : dbObjects) {
/* 196 */       dropStatements.add(dropPrefix + " " + ((DB2DbSupport)this.dbSupport).quote(new String[] { this.name, dbObject }));
/*     */     } 
/* 198 */     return dropStatements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropVersioningStatement() throws SQLException {
/* 205 */     List<String> dropVersioningStatements = new ArrayList<String>();
/* 206 */     Table[] versioningTables = findTables("select TABNAME from SYSCAT.TABLES where TEMPORALTYPE <> 'N' and TABSCHEMA = ?", new String[] { this.name });
/* 207 */     for (Table table : versioningTables) {
/* 208 */       dropVersioningStatements.add("ALTER TABLE " + table.toString() + " DROP VERSIONING");
/*     */     }
/*     */     
/* 211 */     return dropVersioningStatements;
/*     */   }
/*     */   
/*     */   private Table[] findTables(String sqlQuery, String... params) throws SQLException {
/* 215 */     List<String> tableNames = this.jdbcTemplate.queryForStringList(sqlQuery, params);
/* 216 */     Table[] tables = new Table[tableNames.size()];
/* 217 */     for (int i = 0; i < tableNames.size(); i++) {
/* 218 */       tables[i] = new DB2Table(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 220 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 225 */     return findTables("select TABNAME from SYSCAT.TABLES where TYPE='T' and TABSCHEMA = ?", new String[] { this.name });
/*     */   }
/*     */ 
/*     */   
/*     */   protected Function[] doAllFunctions() throws SQLException {
/* 230 */     List<Map<String, String>> rows = this.jdbcTemplate.queryForList("select p.SPECIFICNAME, p.FUNCNAME, substr( xmlserialize( xmlagg( xmltext( concat( ', ', TYPENAME ) ) ) as varchar( 1024 ) ), 3 ) as PARAMS from SYSCAT.FUNCTIONS f inner join SYSCAT.FUNCPARMS p on f.SPECIFICNAME = p.SPECIFICNAME where f.ORIGIN = 'Q' and p.FUNCSCHEMA = ? and p.ROWTYPE = 'P' group by p.SPECIFICNAME, p.FUNCNAME order by p.SPECIFICNAME", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     List<Function> functions = new ArrayList<Function>();
/* 240 */     for (Map<String, String> row : rows) {
/* 241 */       functions.add(getFunction(row
/* 242 */             .get("FUNCNAME"), 
/* 243 */             StringUtils.tokenizeToStringArray(row.get("PARAMS"), ",")));
/*     */     }
/*     */     
/* 246 */     return functions.<Function>toArray(new Function[functions.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 251 */     return new DB2Table(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Type getType(String typeName) {
/* 256 */     return new DB2Type(this.jdbcTemplate, this.dbSupport, this, typeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Function getFunction(String functionName, String... args) {
/* 261 */     return new DB2Function(this.jdbcTemplate, this.dbSupport, this, functionName, args);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\db2\DB2Schema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */