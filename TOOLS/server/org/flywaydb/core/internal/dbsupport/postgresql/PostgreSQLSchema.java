/*     */ package org.flywaydb.core.internal.dbsupport.postgresql;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class PostgreSQLSchema
/*     */   extends Schema<PostgreSQLDbSupport>
/*     */ {
/*     */   public PostgreSQLSchema(JdbcTemplate jdbcTemplate, PostgreSQLDbSupport dbSupport, String name) {
/*  41 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  46 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM pg_namespace WHERE nspname=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  51 */     int objectCount = this.jdbcTemplate.queryForInt("SELECT count(*) FROM information_schema.tables WHERE table_schema=? AND table_type='BASE TABLE'", new String[] { this.name });
/*     */ 
/*     */     
/*  54 */     return (objectCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  59 */     this.jdbcTemplate.execute("CREATE SCHEMA " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  64 */     this.jdbcTemplate.execute("DROP SCHEMA " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " CASCADE", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  69 */     int databaseMajorVersion = this.jdbcTemplate.getMetaData().getDatabaseMajorVersion();
/*  70 */     int databaseMinorVersion = this.jdbcTemplate.getMetaData().getDatabaseMinorVersion();
/*     */     
/*  72 */     if (databaseMajorVersion > 9 || (databaseMajorVersion == 9 && databaseMinorVersion >= 3))
/*     */     {
/*  74 */       for (String statement : generateDropStatementsForMaterializedViews()) {
/*  75 */         this.jdbcTemplate.execute(statement, new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*  79 */     for (String statement : generateDropStatementsForViews()) {
/*  80 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  83 */     for (Table table : allTables()) {
/*  84 */       table.drop();
/*     */     }
/*     */     
/*  87 */     for (String statement : generateDropStatementsForSequences()) {
/*  88 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  91 */     for (String statement : generateDropStatementsForBaseTypes(true)) {
/*  92 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  95 */     for (String statement : generateDropStatementsForAggregates()) {
/*  96 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  99 */     for (String statement : generateDropStatementsForRoutines()) {
/* 100 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 103 */     for (String statement : generateDropStatementsForEnums()) {
/* 104 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 107 */     for (String statement : generateDropStatementsForDomains()) {
/* 108 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 111 */     for (String statement : generateDropStatementsForBaseTypes(false)) {
/* 112 */       this.jdbcTemplate.execute(statement, new Object[0]);
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
/*     */   private List<String> generateDropStatementsForSequences() throws SQLException {
/* 125 */     List<String> sequenceNames = this.jdbcTemplate.queryForStringList("SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/* 128 */     List<String> statements = new ArrayList<String>();
/* 129 */     for (String sequenceName : sequenceNames) {
/* 130 */       statements.add("DROP SEQUENCE IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, sequenceName }));
/*     */     } 
/*     */     
/* 133 */     return statements;
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
/*     */   private List<String> generateDropStatementsForBaseTypes(boolean recreate) throws SQLException {
/* 146 */     List<Map<String, String>> rows = this.jdbcTemplate.queryForList("select typname, typcategory from pg_catalog.pg_type t where (t.typrelid = 0 OR (SELECT c.relkind = 'c' FROM pg_catalog.pg_class c WHERE c.oid = t.typrelid)) and NOT EXISTS(SELECT 1 FROM pg_catalog.pg_type el WHERE el.oid = t.typelem AND el.typarray = t.oid) and t.typnamespace in (select oid from pg_catalog.pg_namespace where nspname = ?)", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     List<String> statements = new ArrayList<String>();
/* 154 */     for (Map<String, String> row : rows) {
/* 155 */       statements.add("DROP TYPE IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("typname") }) + " CASCADE");
/*     */     } 
/*     */     
/* 158 */     if (recreate) {
/* 159 */       for (Map<String, String> row : rows) {
/*     */         
/* 161 */         if (Arrays.<String>asList(new String[] { "P", "U" }).contains(row.get("typcategory"))) {
/* 162 */           statements.add("CREATE TYPE " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("typname") }));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 167 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForAggregates() throws SQLException {
/* 178 */     List<Map<String, String>> rows = this.jdbcTemplate.queryForList("SELECT proname, oidvectortypes(proargtypes) AS args FROM pg_proc INNER JOIN pg_namespace ns ON (pg_proc.pronamespace = ns.oid) WHERE pg_proc.proisagg = true AND ns.nspname = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     List<String> statements = new ArrayList<String>();
/* 186 */     for (Map<String, String> row : rows) {
/* 187 */       statements.add("DROP AGGREGATE IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("proname") }) + "(" + (String)row.get("args") + ") CASCADE");
/*     */     } 
/* 189 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForRoutines() throws SQLException {
/* 200 */     List<Map<String, String>> rows = this.jdbcTemplate.queryForList("SELECT proname, oidvectortypes(proargtypes) AS args FROM pg_proc INNER JOIN pg_namespace ns ON (pg_proc.pronamespace = ns.oid) LEFT JOIN pg_depend dep ON dep.objid = pg_proc.oid AND dep.deptype = 'e' WHERE pg_proc.proisagg = false AND ns.nspname = ? AND dep.objid IS NULL", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     List<String> statements = new ArrayList<String>();
/* 211 */     for (Map<String, String> row : rows) {
/* 212 */       statements.add("DROP FUNCTION IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, row.get("proname") }) + "(" + (String)row.get("args") + ") CASCADE");
/*     */     } 
/* 214 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForEnums() throws SQLException {
/* 225 */     List<String> enumNames = this.jdbcTemplate.queryForStringList("SELECT t.typname FROM pg_catalog.pg_type t INNER JOIN pg_catalog.pg_namespace n ON n.oid = t.typnamespace WHERE n.nspname = ? and t.typtype = 'e'", new String[] { this.name });
/*     */ 
/*     */     
/* 228 */     List<String> statements = new ArrayList<String>();
/* 229 */     for (String enumName : enumNames) {
/* 230 */       statements.add("DROP TYPE " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, enumName }));
/*     */     } 
/*     */     
/* 233 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForDomains() throws SQLException {
/* 244 */     List<String> domainNames = this.jdbcTemplate.queryForStringList("SELECT domain_name FROM information_schema.domains WHERE domain_schema=?", new String[] { this.name });
/*     */ 
/*     */     
/* 247 */     List<String> statements = new ArrayList<String>();
/* 248 */     for (String domainName : domainNames) {
/* 249 */       statements.add("DROP DOMAIN " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, domainName }));
/*     */     } 
/*     */     
/* 252 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForMaterializedViews() throws SQLException {
/* 263 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT relname FROM pg_catalog.pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace WHERE c.relkind = 'm' AND n.nspname = ?", new String[] { this.name });
/*     */ 
/*     */ 
/*     */     
/* 267 */     List<String> statements = new ArrayList<String>();
/* 268 */     for (String domainName : viewNames) {
/* 269 */       statements.add("DROP MATERIALIZED VIEW IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, domainName }) + " CASCADE");
/*     */     } 
/*     */     
/* 272 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForViews() throws SQLException {
/* 283 */     List<String> viewNames = this.jdbcTemplate.queryForStringList("SELECT relname FROM pg_catalog.pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace LEFT JOIN pg_depend dep ON dep.objid = c.oid AND dep.deptype = 'e' WHERE c.relkind = 'v' AND  n.nspname = ? AND dep.objid IS NULL", new String[] { this.name });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     List<String> statements = new ArrayList<String>();
/* 291 */     for (String domainName : viewNames) {
/* 292 */       statements.add("DROP VIEW IF EXISTS " + ((PostgreSQLDbSupport)this.dbSupport).quote(new String[] { this.name, domainName }) + " CASCADE");
/*     */     } 
/*     */     
/* 295 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 301 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT t.table_name FROM information_schema.tables t WHERE table_schema=? AND table_type='BASE TABLE' AND NOT (SELECT EXISTS (SELECT inhrelid FROM pg_catalog.pg_inherits WHERE inhrelid = (quote_ident(t.table_schema)||'.'||quote_ident(t.table_name))::regclass::oid))", new String[] { this.name });
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
/* 315 */     Table[] tables = new Table[tableNames.size()];
/* 316 */     for (int i = 0; i < tableNames.size(); i++) {
/* 317 */       tables[i] = new PostgreSQLTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 319 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 324 */     return new PostgreSQLTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Type getType(String typeName) {
/* 329 */     return new PostgreSQLType(this.jdbcTemplate, this.dbSupport, this, typeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\postgresql\PostgreSQLSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */