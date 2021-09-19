/*     */ package org.flywaydb.core.internal.dbsupport.oracle;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
/*     */ import org.flywaydb.core.internal.dbsupport.Schema;
/*     */ import org.flywaydb.core.internal.dbsupport.Table;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*     */ public class OracleSchema
/*     */   extends Schema<OracleDbSupport>
/*     */ {
/*  33 */   private static final Log LOG = LogFactory.getLog(OracleSchema.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OracleSchema(JdbcTemplate jdbcTemplate, OracleDbSupport dbSupport, String name) {
/*  43 */     super(jdbcTemplate, dbSupport, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doExists() throws SQLException {
/*  48 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM all_users WHERE username=?", new String[] { this.name }) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doEmpty() throws SQLException {
/*  53 */     return (this.jdbcTemplate.queryForInt("SELECT count(*) FROM all_objects WHERE owner = ?", new String[] { this.name }) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doCreate() throws SQLException {
/*  58 */     this.jdbcTemplate.execute("CREATE USER " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " IDENTIFIED BY flyway", new Object[0]);
/*  59 */     this.jdbcTemplate.execute("GRANT RESOURCE TO " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*  60 */     this.jdbcTemplate.execute("GRANT UNLIMITED TABLESPACE TO " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name }, ), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDrop() throws SQLException {
/*  65 */     this.jdbcTemplate.execute("DROP USER " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name }, ) + " CASCADE", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClean() throws SQLException {
/*  70 */     if ("SYSTEM".equals(this.name.toUpperCase())) {
/*  71 */       throw new FlywayException("Clean not supported on Oracle for user 'SYSTEM'! You should NEVER add your own objects to the SYSTEM schema!");
/*     */     }
/*     */     
/*  74 */     String user = ((OracleDbSupport)this.dbSupport).doGetCurrentSchemaName();
/*  75 */     boolean defaultSchemaForUser = user.equalsIgnoreCase(this.name);
/*     */     
/*  77 */     if (!defaultSchemaForUser) {
/*  78 */       LOG.warn("Cleaning schema " + this.name + " by a different user (" + user + "): " + "spatial extensions, queue tables, flashback tables and scheduled jobs will not be cleaned due to Oracle limitations");
/*     */     }
/*     */ 
/*     */     
/*  82 */     for (String statement : generateDropStatementsForSpatialExtensions(defaultSchemaForUser)) {
/*  83 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/*  86 */     if (defaultSchemaForUser) {
/*  87 */       for (String statement : generateDropStatementsForQueueTables()) {
/*     */         try {
/*  89 */           this.jdbcTemplate.execute(statement, new Object[0]);
/*  90 */         } catch (SQLException e) {
/*  91 */           if (e.getErrorCode() == 65040)
/*     */           {
/*     */             
/*  94 */             LOG.error("Missing required grant to clean queue tables: GRANT EXECUTE ON DBMS_AQADM");
/*     */           }
/*  96 */           throw e;
/*     */         } 
/*     */       } 
/*     */       
/* 100 */       if (flashbackAvailable()) {
/* 101 */         executeAlterStatementsForFlashbackTables();
/*     */       }
/*     */     } 
/*     */     
/* 105 */     for (String statement : generateDropStatementsForScheduledJobs()) {
/* 106 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 109 */     for (String statement : generateDropStatementsForObjectType("TRIGGER", "")) {
/* 110 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 113 */     for (String statement : generateDropStatementsForObjectType("SEQUENCE", "")) {
/* 114 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 117 */     for (String statement : generateDropStatementsForObjectType("FUNCTION", "")) {
/* 118 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 121 */     for (String statement : generateDropStatementsForObjectType("MATERIALIZED VIEW", "PRESERVE TABLE")) {
/* 122 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 125 */     for (String statement : generateDropStatementsForObjectType("PACKAGE", "")) {
/* 126 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 129 */     for (String statement : generateDropStatementsForObjectType("PROCEDURE", "")) {
/* 130 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 133 */     for (String statement : generateDropStatementsForObjectType("SYNONYM", "")) {
/* 134 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 137 */     for (String statement : generateDropStatementsForObjectType("VIEW", "CASCADE CONSTRAINTS")) {
/* 138 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 141 */     for (Table table : allTables()) {
/* 142 */       table.drop();
/*     */     }
/*     */     
/* 145 */     for (String statement : generateDropStatementsForXmlTables()) {
/* 146 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 149 */     for (String statement : generateDropStatementsForObjectType("CLUSTER", "")) {
/* 150 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 153 */     for (String statement : generateDropStatementsForObjectType("TYPE", "FORCE")) {
/* 154 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 157 */     for (String statement : generateDropStatementsForObjectType("JAVA SOURCE", "")) {
/* 158 */       this.jdbcTemplate.execute(statement, new Object[0]);
/*     */     }
/*     */     
/* 161 */     this.jdbcTemplate.execute("PURGE RECYCLEBIN", new Object[0]);
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
/*     */   private void executeAlterStatementsForFlashbackTables() throws SQLException {
/* 173 */     List<String> tableNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM DBA_FLASHBACK_ARCHIVE_TABLES WHERE owner_name = ?", new String[] { this.name });
/*     */     
/* 175 */     for (String tableName : tableNames) {
/* 176 */       this.jdbcTemplate.execute("ALTER TABLE " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name, tableName }, ) + " NO FLASHBACK ARCHIVE", new Object[0]);
/* 177 */       String queryForOracleTechnicalTables = "SELECT count(archive_table_name) FROM user_flashback_archive_tables WHERE table_name = ?";
/*     */ 
/*     */ 
/*     */       
/* 181 */       while (this.jdbcTemplate.queryForInt(queryForOracleTechnicalTables, new String[] { tableName }) > 0) {
/*     */         try {
/* 183 */           LOG.debug("Actively waiting for Flashback cleanup on table: " + tableName);
/* 184 */           Thread.sleep(1000L);
/* 185 */         } catch (InterruptedException e) {
/* 186 */           throw new FlywayException("Waiting for Flashback cleanup interrupted", e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean flashbackAvailable() throws SQLException {
/* 199 */     return (this.jdbcTemplate.queryForInt("select count(*) from all_objects where object_name like 'DBA_FLASHBACK_ARCHIVE_TABLES'", new String[0]) > 0);
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
/*     */   private List<String> generateDropStatementsForXmlTables() throws SQLException {
/* 212 */     List<String> dropStatements = new ArrayList<String>();
/*     */     
/* 214 */     if (!xmlDBExtensionsAvailable()) {
/* 215 */       LOG.debug("Oracle XML DB Extensions are not available. No cleaning of XML tables.");
/* 216 */       return dropStatements;
/*     */     } 
/*     */ 
/*     */     
/* 220 */     List<String> objectNames = this.jdbcTemplate.queryForStringList("SELECT table_name FROM all_xml_tables WHERE owner = ?", new String[] { this.name });
/* 221 */     for (String objectName : objectNames) {
/* 222 */       dropStatements.add("DROP TABLE " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name, objectName }) + " PURGE");
/*     */     } 
/* 224 */     return dropStatements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean xmlDBExtensionsAvailable() throws SQLException {
/* 234 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM all_users WHERE username = 'XDB'", new String[0]) > 0 && this.jdbcTemplate
/* 235 */       .queryForInt("SELECT COUNT(*) FROM all_views WHERE view_name = 'RESOURCE_VIEW'", new String[0]) > 0);
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
/*     */   private List<String> generateDropStatementsForObjectType(String objectType, String extraArguments) throws SQLException {
/* 247 */     String query = "SELECT object_name FROM all_objects WHERE object_type = ? AND owner = ? AND object_name NOT LIKE 'MDRS_%$' AND object_name NOT LIKE 'ISEQ$$_%'";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     List<String> objectNames = this.jdbcTemplate.queryForStringList(query, new String[] { objectType, this.name });
/* 254 */     List<String> dropStatements = new ArrayList<String>();
/* 255 */     for (String objectName : objectNames) {
/* 256 */       dropStatements.add("DROP " + objectType + " " + ((OracleDbSupport)this.dbSupport).quote(new String[] { this.name, objectName }) + " " + extraArguments);
/*     */     } 
/* 258 */     return dropStatements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForSpatialExtensions(boolean defaultSchemaForUser) throws SQLException {
/* 269 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 271 */     if (!spatialExtensionsAvailable()) {
/* 272 */       LOG.debug("Oracle Spatial Extensions are not available. No cleaning of MDSYS tables and views.");
/* 273 */       return statements;
/*     */     } 
/* 275 */     if (!((OracleDbSupport)this.dbSupport).getCurrentSchemaName().equalsIgnoreCase(this.name)) {
/* 276 */       int count = this.jdbcTemplate.queryForInt("SELECT COUNT (*) FROM all_sdo_geom_metadata WHERE owner=?", new String[] { this.name });
/* 277 */       count += this.jdbcTemplate.queryForInt("SELECT COUNT (*) FROM all_sdo_index_info WHERE sdo_index_owner=?", new String[] { this.name });
/* 278 */       if (count > 0) {
/* 279 */         LOG.warn("Unable to clean Oracle Spatial objects for schema '" + this.name + "' as they do not belong to the default schema for this connection!");
/*     */       }
/* 281 */       return statements;
/*     */     } 
/*     */     
/* 284 */     if (defaultSchemaForUser) {
/* 285 */       statements.add("DELETE FROM mdsys.user_sdo_geom_metadata");
/*     */       
/* 287 */       List<String> indexNames = this.jdbcTemplate.queryForStringList("select INDEX_NAME from USER_SDO_INDEX_INFO", new String[0]);
/* 288 */       for (String indexName : indexNames) {
/* 289 */         statements.add("DROP INDEX \"" + indexName + "\"");
/*     */       }
/*     */     } 
/*     */     
/* 293 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForScheduledJobs() throws SQLException {
/* 303 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 305 */     List<String> jobNames = this.jdbcTemplate.queryForStringList("select JOB_NAME from ALL_SCHEDULER_JOBS WHERE owner=?", new String[] { this.name });
/* 306 */     for (String jobName : jobNames) {
/* 307 */       statements.add("begin DBMS_SCHEDULER.DROP_JOB(job_name => '" + jobName + "', defer => false, force => true); end;");
/*     */     }
/*     */     
/* 310 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> generateDropStatementsForQueueTables() throws SQLException {
/* 320 */     List<String> statements = new ArrayList<String>();
/*     */     
/* 322 */     List<String> queueTblNames = this.jdbcTemplate.queryForStringList("select QUEUE_TABLE from USER_QUEUE_TABLES", new String[0]);
/* 323 */     for (String queueTblName : queueTblNames) {
/* 324 */       statements.add("begin DBMS_AQADM.drop_queue_table (queue_table=> '" + queueTblName + "', FORCE => TRUE); end;");
/*     */     }
/*     */     
/* 327 */     return statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean spatialExtensionsAvailable() throws SQLException {
/* 337 */     return (this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM all_views WHERE owner = 'MDSYS' AND view_name = 'USER_SDO_GEOM_METADATA'", new String[0]) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Table[] doAllTables() throws SQLException {
/* 342 */     List<String> tableNames = this.jdbcTemplate.queryForStringList(" SELECT r FROM   (SELECT CONNECT_BY_ROOT t r FROM     (SELECT DISTINCT c1.table_name f, NVL(c2.table_name, at.table_name) t     FROM all_constraints c1       RIGHT JOIN all_constraints c2 ON c2.constraint_name = c1.r_constraint_name       RIGHT JOIN all_tables at ON at.table_name = c2.table_name     WHERE at.owner = ?       AND at.table_name NOT LIKE 'BIN$%'       AND at.table_name NOT LIKE 'MDRT_%$'       AND at.table_name NOT LIKE 'MLOG$%' AND at.table_name NOT LIKE 'RUPD$%'       AND at.table_name NOT LIKE 'DR$%'       AND at.table_name NOT LIKE 'SYS_IOT_OVER_%'       AND at.nested != 'YES'       AND at.secondary != 'Y')   CONNECT BY NOCYCLE PRIOR f = t) GROUP BY r ORDER BY COUNT(*)", new String[] { this.name });
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
/*     */ 
/*     */     
/* 370 */     Table[] tables = new Table[tableNames.size()];
/* 371 */     for (int i = 0; i < tableNames.size(); i++) {
/* 372 */       tables[i] = new OracleTable(this.jdbcTemplate, this.dbSupport, this, tableNames.get(i));
/*     */     }
/* 374 */     return tables;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table getTable(String tableName) {
/* 379 */     return new OracleTable(this.jdbcTemplate, this.dbSupport, this, tableName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\oracle\OracleSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */