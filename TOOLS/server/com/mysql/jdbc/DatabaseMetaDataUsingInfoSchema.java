/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DatabaseMetaDataUsingInfoSchema
/*      */   extends DatabaseMetaData
/*      */ {
/*      */   private boolean hasReferentialConstraintsView;
/*      */   private boolean hasParametersView;
/*      */   
/*      */   protected DatabaseMetaDataUsingInfoSchema(ConnectionImpl connToSet, String databaseToSet) throws SQLException {
/*   43 */     super(connToSet, databaseToSet);
/*      */     
/*   45 */     this.hasReferentialConstraintsView = this.conn.versionMeetsMinimum(5, 1, 10);
/*      */ 
/*      */     
/*   48 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*   51 */       rs = super.getTables("INFORMATION_SCHEMA", null, "PARAMETERS", new String[0]);
/*      */       
/*   53 */       this.hasParametersView = rs.next();
/*      */     } finally {
/*   55 */       if (rs != null) {
/*   56 */         rs.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private ResultSet executeMetadataQuery(PreparedStatement pStmt) throws SQLException {
/*   63 */     ResultSet rs = pStmt.executeQuery();
/*   64 */     ((ResultSetInternalMethods)rs).setOwningStatement(null);
/*      */     
/*   66 */     return rs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
/*  107 */     if (columnNamePattern == null) {
/*  108 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  109 */         columnNamePattern = "%";
/*      */       } else {
/*  111 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  117 */     if (catalog == null && 
/*  118 */       this.conn.getNullCatalogMeansCurrent()) {
/*  119 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  123 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME,COLUMN_NAME, NULL AS GRANTOR, GRANTEE, PRIVILEGE_TYPE AS PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME =? AND COLUMN_NAME LIKE ? ORDER BY COLUMN_NAME, PRIVILEGE_TYPE";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  130 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  133 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  135 */       if (catalog != null) {
/*  136 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  138 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  141 */       pStmt.setString(2, table);
/*  142 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  144 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  145 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 64), new Field("", "TABLE_SCHEM", 1, 1), new Field("", "TABLE_NAME", 1, 64), new Field("", "COLUMN_NAME", 1, 64), new Field("", "GRANTOR", 1, 77), new Field("", "GRANTEE", 1, 77), new Field("", "PRIVILEGE", 1, 64), new Field("", "IS_GRANTABLE", 1, 3) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  155 */       return rs;
/*      */     } finally {
/*  157 */       if (pStmt != null) {
/*  158 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumns(String catalog, String schemaPattern, String tableName, String columnNamePattern) throws SQLException {
/*  209 */     if (columnNamePattern == null) {
/*  210 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  211 */         columnNamePattern = "%";
/*      */       } else {
/*  213 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  219 */     if (catalog == null && 
/*  220 */       this.conn.getNullCatalogMeansCurrent()) {
/*  221 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  225 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,COLUMN_NAME,");
/*      */ 
/*      */     
/*  228 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/*  230 */     sqlBuf.append(" AS DATA_TYPE, ");
/*      */     
/*  232 */     if (this.conn.getCapitalizeTypeNames()) {
/*  233 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS TYPE_NAME,");
/*      */     } else {
/*  235 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS TYPE_NAME,");
/*      */     } 
/*      */     
/*  238 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS COLUMN_SIZE, " + MysqlIO.getMaxBuf() + " AS BUFFER_LENGTH," + "NUMERIC_SCALE AS DECIMAL_DIGITS," + "10 AS NUM_PREC_RADIX," + "CASE WHEN IS_NULLABLE='NO' THEN " + Character.MIN_VALUE + " ELSE CASE WHEN IS_NULLABLE='YES' THEN " + '\001' + " ELSE " + '\002' + " END END AS NULLABLE," + "COLUMN_COMMENT AS REMARKS," + "COLUMN_DEFAULT AS COLUMN_DEF," + "0 AS SQL_DATA_TYPE," + "0 AS SQL_DATETIME_SUB," + "CASE WHEN CHARACTER_OCTET_LENGTH > " + 2147483647 + " THEN " + 2147483647 + " ELSE CHARACTER_OCTET_LENGTH END AS CHAR_OCTET_LENGTH," + "ORDINAL_POSITION," + "IS_NULLABLE," + "NULL AS SCOPE_CATALOG," + "NULL AS SCOPE_SCHEMA," + "NULL AS SCOPE_TABLE," + "NULL AS SOURCE_DATA_TYPE," + "IF (EXTRA LIKE '%auto_increment%','YES','NO') AS IS_AUTOINCREMENT " + "FROM INFORMATION_SCHEMA.COLUMNS WHERE " + "TABLE_SCHEMA LIKE ? AND " + "TABLE_NAME LIKE ? AND COLUMN_NAME LIKE ? " + "ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  262 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  265 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  267 */       if (catalog != null) {
/*  268 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  270 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  273 */       pStmt.setString(2, tableName);
/*  274 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  276 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  278 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createColumnsFields());
/*  279 */       return rs;
/*      */     } finally {
/*  281 */       if (pStmt != null) {
/*  282 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
/*  357 */     if (primaryTable == null) {
/*  358 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  362 */     if (primaryCatalog == null && 
/*  363 */       this.conn.getNullCatalogMeansCurrent()) {
/*  364 */       primaryCatalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  368 */     if (foreignCatalog == null && 
/*  369 */       this.conn.getNullCatalogMeansCurrent()) {
/*  370 */       foreignCatalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  374 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + '\007' + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN " + "INFORMATION_SCHEMA.TABLE_CONSTRAINTS B " + "USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? " + "AND A.TABLE_SCHEMA LIKE ? AND A.TABLE_NAME=? " + "ORDER BY " + "A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  408 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  411 */       pStmt = prepareMetaDataSafeStatement(sql);
/*  412 */       if (primaryCatalog != null) {
/*  413 */         pStmt.setString(1, primaryCatalog);
/*      */       } else {
/*  415 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  418 */       pStmt.setString(2, primaryTable);
/*      */       
/*  420 */       if (foreignCatalog != null) {
/*  421 */         pStmt.setString(3, foreignCatalog);
/*      */       } else {
/*  423 */         pStmt.setString(3, "%");
/*      */       } 
/*      */       
/*  426 */       pStmt.setString(4, foreignTable);
/*      */       
/*  428 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  429 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  431 */       return rs;
/*      */     } finally {
/*  433 */       if (pStmt != null) {
/*  434 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/*  502 */     if (table == null) {
/*  503 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  507 */     if (catalog == null && 
/*  508 */       this.conn.getNullCatalogMeansCurrent()) {
/*  509 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  515 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME,A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + '\007' + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN " + "INFORMATION_SCHEMA.TABLE_CONSTRAINTS B " + "USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? " + "ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  548 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  551 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  553 */       if (catalog != null) {
/*  554 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  556 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  559 */       pStmt.setString(2, table);
/*      */       
/*  561 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  563 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  565 */       return rs;
/*      */     } finally {
/*  567 */       if (pStmt != null) {
/*  568 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private String generateOptionalRefContraintsJoin() {
/*  575 */     return this.hasReferentialConstraintsView ? "JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON (R.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND R.TABLE_NAME = B.TABLE_NAME AND R.CONSTRAINT_SCHEMA = B.TABLE_SCHEMA) " : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String generateDeleteRuleClause() {
/*  583 */     return this.hasReferentialConstraintsView ? ("CASE WHEN R.DELETE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.DELETE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.DELETE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.DELETE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.DELETE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END ") : String.valueOf(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String generateUpdateRuleClause() {
/*  593 */     return this.hasReferentialConstraintsView ? ("CASE WHEN R.UPDATE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.UPDATE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.UPDATE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.UPDATE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.UPDATE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END ") : String.valueOf(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/*  663 */     if (table == null) {
/*  664 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  668 */     if (catalog == null && 
/*  669 */       this.conn.getNullCatalogMeansCurrent()) {
/*  670 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  674 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + '\007' + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A " + "JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS B USING " + "(CONSTRAINT_NAME, TABLE_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.TABLE_SCHEMA LIKE ? " + "AND A.TABLE_NAME=? " + "AND A.REFERENCED_TABLE_SCHEMA IS NOT NULL " + "ORDER BY " + "A.REFERENCED_TABLE_SCHEMA, A.REFERENCED_TABLE_NAME, " + "A.ORDINAL_POSITION";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  711 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  714 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  716 */       if (catalog != null) {
/*  717 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  719 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  722 */       pStmt.setString(2, table);
/*      */       
/*  724 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  726 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  728 */       return rs;
/*      */     } finally {
/*  730 */       if (pStmt != null) {
/*  731 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
/*  796 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,NON_UNIQUE,TABLE_SCHEMA AS INDEX_QUALIFIER,INDEX_NAME,3 AS TYPE,SEQ_IN_INDEX AS ORDINAL_POSITION,COLUMN_NAME,COLLATION AS ASC_OR_DESC,CARDINALITY,NULL AS PAGES,NULL AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  807 */     if (unique) {
/*  808 */       sqlBuf.append(" AND NON_UNIQUE=0 ");
/*      */     }
/*      */     
/*  811 */     sqlBuf.append("ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX");
/*      */     
/*  813 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  816 */       if (catalog == null && 
/*  817 */         this.conn.getNullCatalogMeansCurrent()) {
/*  818 */         catalog = this.database;
/*      */       }
/*      */ 
/*      */       
/*  822 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  824 */       if (catalog != null) {
/*  825 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  827 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  830 */       pStmt.setString(2, table);
/*      */       
/*  832 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  834 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createIndexInfoFields());
/*      */       
/*  836 */       return rs;
/*      */     } finally {
/*  838 */       if (pStmt != null) {
/*  839 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
/*  872 */     if (catalog == null && 
/*  873 */       this.conn.getNullCatalogMeansCurrent()) {
/*  874 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */     
/*  878 */     if (table == null) {
/*  879 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  883 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX AS KEY_SEQ, 'PRIMARY' AS PK_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND INDEX_NAME='PRIMARY' ORDER BY TABLE_SCHEMA, TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  888 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  891 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  893 */       if (catalog != null) {
/*  894 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  896 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/*  899 */       pStmt.setString(2, table);
/*      */       
/*  901 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  902 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 5), new Field("", "PK_NAME", 1, 32) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  910 */       return rs;
/*      */     } finally {
/*  912 */       if (pStmt != null) {
/*  913 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
/*  961 */     if (procedureNamePattern == null || procedureNamePattern.length() == 0)
/*      */     {
/*  963 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  964 */         procedureNamePattern = "%";
/*      */       } else {
/*  966 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  972 */     String db = null;
/*      */     
/*  974 */     if (catalog == null && 
/*  975 */       this.conn.getNullCatalogMeansCurrent()) {
/*  976 */       db = this.database;
/*      */     }
/*      */ 
/*      */     
/*  980 */     String sql = "SELECT ROUTINE_SCHEMA AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, ROUTINE_NAME AS PROCEDURE_NAME, NULL AS RESERVED_1, NULL AS RESERVED_2, NULL AS RESERVED_3, ROUTINE_COMMENT AS REMARKS, CASE WHEN ROUTINE_TYPE = 'PROCEDURE' THEN 1 WHEN ROUTINE_TYPE='FUNCTION' THEN 2 ELSE 0 END AS PROCEDURE_TYPE FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_SCHEMA LIKE ? AND ROUTINE_NAME LIKE ? ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  993 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/*  996 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  998 */       if (db != null) {
/*  999 */         pStmt.setString(1, db);
/*      */       } else {
/* 1001 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/* 1004 */       pStmt.setString(2, procedureNamePattern);
/*      */       
/* 1006 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1007 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "PROCEDURE_CAT", 1, 0), new Field("", "PROCEDURE_SCHEM", 1, 0), new Field("", "PROCEDURE_NAME", 1, 0), new Field("", "reserved1", 1, 0), new Field("", "reserved2", 1, 0), new Field("", "reserved3", 1, 0), new Field("", "REMARKS", 1, 0), new Field("", "PROCEDURE_TYPE", 5, 0) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1017 */       return rs;
/*      */     } finally {
/* 1019 */       if (pStmt != null) {
/* 1020 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
/* 1123 */     if (!this.conn.versionMeetsMinimum(5, 4, 0)) {
/* 1124 */       return super.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
/*      */     }
/*      */ 
/*      */     
/* 1128 */     if (!this.hasParametersView) {
/* 1129 */       return super.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
/*      */     }
/*      */     
/* 1132 */     if (functionNamePattern == null || functionNamePattern.length() == 0)
/*      */     {
/* 1134 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1135 */         functionNamePattern = "%";
/*      */       } else {
/* 1137 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1143 */     String db = null;
/*      */     
/* 1145 */     if (catalog == null && 
/* 1146 */       this.conn.getNullCatalogMeansCurrent()) {
/* 1147 */       db = this.database;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1157 */     StringBuffer sqlBuf = new StringBuffer("SELECT SPECIFIC_SCHEMA AS FUNCTION_CAT, NULL AS `FUNCTION_SCHEM`, SPECIFIC_NAME AS `FUNCTION_NAME`, PARAMETER_NAME AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN 1 WHEN PARAMETER_MODE='OUT' THEN 3 WHEN PARAMETER_MODE='INOUT' THEN 2 WHEN ORDINAL_POSITION=0 THEN 4 ELSE 0 END AS `COLUMN_TYPE`, ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1169 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/* 1171 */     sqlBuf.append(" AS `DATA_TYPE`, ");
/*      */ 
/*      */     
/* 1174 */     if (this.conn.getCapitalizeTypeNames()) {
/* 1175 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,");
/*      */     } else {
/* 1177 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,");
/*      */     } 
/*      */ 
/*      */     
/* 1181 */     sqlBuf.append("NUMERIC_PRECISION AS `PRECISION`, ");
/*      */     
/* 1183 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, ");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1188 */     sqlBuf.append("NUMERIC_SCALE AS `SCALE`, ");
/*      */     
/* 1190 */     sqlBuf.append("10 AS RADIX,");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1197 */     sqlBuf.append("2 AS `NULLABLE`,  NULL AS `REMARKS`, CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`,  ORDINAL_POSITION, '' AS `IS_NULLABLE`, SPECIFIC_NAME FROM INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) AND ROUTINE_TYPE='FUNCTION' ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1207 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/* 1210 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/* 1212 */       if (db != null) {
/* 1213 */         pStmt.setString(1, db);
/*      */       } else {
/* 1215 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/* 1218 */       pStmt.setString(2, functionNamePattern);
/* 1219 */       pStmt.setString(3, columnNamePattern);
/*      */       
/* 1221 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1222 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFunctionColumnsFields());
/*      */       
/* 1224 */       return rs;
/*      */     } finally {
/* 1226 */       if (pStmt != null) {
/* 1227 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
/* 1298 */     if (!this.conn.versionMeetsMinimum(5, 4, 0)) {
/* 1299 */       return super.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
/*      */     }
/*      */ 
/*      */     
/* 1303 */     if (!this.hasParametersView) {
/* 1304 */       return super.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
/*      */     }
/*      */     
/* 1307 */     if (procedureNamePattern == null || procedureNamePattern.length() == 0)
/*      */     {
/* 1309 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1310 */         procedureNamePattern = "%";
/*      */       } else {
/* 1312 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1318 */     String db = null;
/*      */     
/* 1320 */     if (catalog == null && 
/* 1321 */       this.conn.getNullCatalogMeansCurrent()) {
/* 1322 */       db = this.database;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1342 */     StringBuffer sqlBuf = new StringBuffer("SELECT SPECIFIC_SCHEMA AS PROCEDURE_CAT, NULL AS `PROCEDURE_SCHEM`, SPECIFIC_NAME AS `PROCEDURE_NAME`, PARAMETER_NAME AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN 1 WHEN PARAMETER_MODE='OUT' THEN 4 WHEN PARAMETER_MODE='INOUT' THEN 2 WHEN ORDINAL_POSITION=0 THEN 5 ELSE 0 END AS `COLUMN_TYPE`, ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1354 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/* 1356 */     sqlBuf.append(" AS `DATA_TYPE`, ");
/*      */ 
/*      */     
/* 1359 */     if (this.conn.getCapitalizeTypeNames()) {
/* 1360 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,");
/*      */     } else {
/* 1362 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,");
/*      */     } 
/*      */ 
/*      */     
/* 1366 */     sqlBuf.append("NUMERIC_PRECISION AS `PRECISION`, ");
/*      */     
/* 1368 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, ");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1373 */     sqlBuf.append("NUMERIC_SCALE AS `SCALE`, ");
/*      */     
/* 1375 */     sqlBuf.append("10 AS RADIX,");
/* 1376 */     sqlBuf.append("2 AS `NULLABLE`,  NULL AS `REMARKS` FROM INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1382 */     PreparedStatement pStmt = null;
/*      */     
/*      */     try {
/* 1385 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/* 1387 */       if (db != null) {
/* 1388 */         pStmt.setString(1, db);
/*      */       } else {
/* 1390 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/* 1393 */       pStmt.setString(2, procedureNamePattern);
/* 1394 */       pStmt.setString(3, columnNamePattern);
/*      */       
/* 1396 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1397 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createProcedureColumnsFields());
/*      */       
/* 1399 */       return rs;
/*      */     } finally {
/* 1401 */       if (pStmt != null) {
/* 1402 */         pStmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
/* 1445 */     if (catalog == null && 
/* 1446 */       this.conn.getNullCatalogMeansCurrent()) {
/* 1447 */       catalog = this.database;
/*      */     }
/*      */ 
/*      */     
/* 1451 */     if (tableNamePattern == null) {
/* 1452 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1453 */         tableNamePattern = "%";
/*      */       } else {
/* 1455 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1461 */     PreparedStatement pStmt = null;
/*      */     
/* 1463 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN 'TABLE' WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND TABLE_TYPE IN (?,?,?) ORDER BY TABLE_TYPE, TABLE_SCHEMA, TABLE_NAME";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1471 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1473 */       if (catalog != null) {
/* 1474 */         pStmt.setString(1, catalog);
/*      */       } else {
/* 1476 */         pStmt.setString(1, "%");
/*      */       } 
/*      */       
/* 1479 */       pStmt.setString(2, tableNamePattern);
/*      */ 
/*      */ 
/*      */       
/* 1483 */       if (types == null || types.length == 0) {
/* 1484 */         pStmt.setString(3, "BASE TABLE");
/* 1485 */         pStmt.setString(4, "VIEW");
/* 1486 */         pStmt.setString(5, "TEMPORARY");
/*      */       } else {
/* 1488 */         pStmt.setNull(3, 12);
/* 1489 */         pStmt.setNull(4, 12);
/* 1490 */         pStmt.setNull(5, 12);
/*      */         
/* 1492 */         for (int i = 0; i < types.length; i++) {
/* 1493 */           if ("TABLE".equalsIgnoreCase(types[i])) {
/* 1494 */             pStmt.setString(3, "BASE TABLE");
/*      */           }
/*      */           
/* 1497 */           if ("VIEW".equalsIgnoreCase(types[i])) {
/* 1498 */             pStmt.setString(4, "VIEW");
/*      */           }
/*      */           
/* 1501 */           if ("LOCAL TEMPORARY".equalsIgnoreCase(types[i])) {
/* 1502 */             pStmt.setString(5, "TEMPORARY");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1507 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/* 1509 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 12, (catalog == null) ? 0 : catalog.length()), new Field("", "TABLE_SCHEM", 12, 0), new Field("", "TABLE_NAME", 12, 255), new Field("", "TABLE_TYPE", 12, 5), new Field("", "REMARKS", 12, 0) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1517 */       return rs;
/*      */     } finally {
/* 1519 */       if (pStmt != null)
/* 1520 */         pStmt.close(); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\DatabaseMetaDataUsingInfoSchema.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */