/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DatabaseMetaData
/*      */   implements DatabaseMetaData
/*      */ {
/*      */   private static String mysqlKeywordsThatArentSQL92;
/*      */   protected static final int MAX_IDENTIFIER_LENGTH = 64;
/*      */   private static final int DEFERRABILITY = 13;
/*      */   private static final int DELETE_RULE = 10;
/*      */   private static final int FK_NAME = 11;
/*      */   private static final int FKCOLUMN_NAME = 7;
/*      */   private static final int FKTABLE_CAT = 4;
/*      */   private static final int FKTABLE_NAME = 6;
/*      */   private static final int FKTABLE_SCHEM = 5;
/*      */   private static final int KEY_SEQ = 8;
/*      */   private static final int PK_NAME = 12;
/*      */   private static final int PKCOLUMN_NAME = 3;
/*      */   private static final int PKTABLE_CAT = 0;
/*      */   private static final int PKTABLE_NAME = 2;
/*      */   private static final int PKTABLE_SCHEM = 1;
/*      */   private static final String SUPPORTS_FK = "SUPPORTS_FK";
/*      */   
/*      */   protected abstract class IteratorWithCleanup
/*      */   {
/*      */     private final DatabaseMetaData this$0;
/*      */     
/*      */     protected IteratorWithCleanup(DatabaseMetaData this$0) {
/*   66 */       this.this$0 = this$0;
/*      */     }
/*      */     
/*      */     abstract void close() throws SQLException;
/*      */     
/*      */     abstract boolean hasNext() throws SQLException;
/*      */     
/*      */     abstract Object next() throws SQLException;
/*      */   }
/*      */   
/*      */   class LocalAndReferencedColumns
/*      */   {
/*      */     String constraintName;
/*      */     List localColumnsList;
/*      */     String referencedCatalog;
/*      */     List referencedColumnsList;
/*      */     String referencedTable;
/*      */     private final DatabaseMetaData this$0;
/*      */     
/*      */     LocalAndReferencedColumns(DatabaseMetaData this$0, List localColumns, List refColumns, String constName, String refCatalog, String refTable) {
/*   86 */       this.this$0 = this$0;
/*   87 */       this.localColumnsList = localColumns;
/*   88 */       this.referencedColumnsList = refColumns;
/*   89 */       this.constraintName = constName;
/*   90 */       this.referencedTable = refTable;
/*   91 */       this.referencedCatalog = refCatalog;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ResultSetIterator extends IteratorWithCleanup { int colIndex;
/*      */     ResultSet resultSet;
/*      */     private final DatabaseMetaData this$0;
/*      */     
/*      */     ResultSetIterator(DatabaseMetaData this$0, ResultSet rs, int index) {
/*  100 */       super(this$0); this.this$0 = this$0;
/*  101 */       this.resultSet = rs;
/*  102 */       this.colIndex = index;
/*      */     }
/*      */     
/*      */     void close() throws SQLException {
/*  106 */       this.resultSet.close();
/*      */     }
/*      */     
/*      */     boolean hasNext() throws SQLException {
/*  110 */       return this.resultSet.next();
/*      */     }
/*      */     
/*      */     Object next() throws SQLException {
/*  114 */       return this.resultSet.getObject(this.colIndex);
/*      */     } }
/*      */   
/*      */   protected class SingleStringIterator extends IteratorWithCleanup {
/*      */     boolean onFirst;
/*      */     String value;
/*      */     private final DatabaseMetaData this$0;
/*      */     
/*      */     SingleStringIterator(DatabaseMetaData this$0, String s) {
/*  123 */       super(this$0); this.this$0 = this$0; this.onFirst = true;
/*  124 */       this.value = s;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void close() throws SQLException {}
/*      */ 
/*      */     
/*      */     boolean hasNext() throws SQLException {
/*  133 */       return this.onFirst;
/*      */     }
/*      */     
/*      */     Object next() throws SQLException {
/*  137 */       this.onFirst = false;
/*  138 */       return this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class TypeDescriptor
/*      */   {
/*      */     int bufferLength;
/*      */     
/*      */     int charOctetLength;
/*      */     
/*      */     Integer columnSize;
/*      */     
/*      */     short dataType;
/*      */     
/*      */     Integer decimalDigits;
/*      */     
/*      */     String isNullable;
/*      */     
/*      */     int nullability;
/*      */     
/*      */     int numPrecRadix;
/*      */     
/*      */     String typeName;
/*      */     
/*      */     private final DatabaseMetaData this$0;
/*      */     
/*      */     TypeDescriptor(DatabaseMetaData this$0, String typeInfo, String nullabilityInfo) throws SQLException {
/*  166 */       this.this$0 = this$0; this.numPrecRadix = 10;
/*  167 */       if (typeInfo == null) {
/*  168 */         throw SQLError.createSQLException("NULL typeinfo not supported.", "S1009", this$0.getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */       
/*  172 */       String mysqlType = "";
/*  173 */       String fullMysqlType = null;
/*      */       
/*  175 */       if (typeInfo.indexOf("(") != -1) {
/*  176 */         mysqlType = typeInfo.substring(0, typeInfo.indexOf("("));
/*      */       } else {
/*  178 */         mysqlType = typeInfo;
/*      */       } 
/*      */       
/*  181 */       int indexOfUnsignedInMysqlType = StringUtils.indexOfIgnoreCase(mysqlType, "unsigned");
/*      */ 
/*      */       
/*  184 */       if (indexOfUnsignedInMysqlType != -1) {
/*  185 */         mysqlType = mysqlType.substring(0, indexOfUnsignedInMysqlType - 1);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  192 */       boolean isUnsigned = false;
/*      */       
/*  194 */       if (StringUtils.indexOfIgnoreCase(typeInfo, "unsigned") != -1) {
/*  195 */         fullMysqlType = mysqlType + " unsigned";
/*  196 */         isUnsigned = true;
/*      */       } else {
/*  198 */         fullMysqlType = mysqlType;
/*      */       } 
/*      */       
/*  201 */       if (this$0.conn.getCapitalizeTypeNames()) {
/*  202 */         fullMysqlType = fullMysqlType.toUpperCase(Locale.ENGLISH);
/*      */       }
/*      */       
/*  205 */       this.dataType = (short)MysqlDefs.mysqlToJavaType(mysqlType);
/*      */       
/*  207 */       this.typeName = fullMysqlType;
/*      */ 
/*      */ 
/*      */       
/*  211 */       if (StringUtils.startsWithIgnoreCase(typeInfo, "enum")) {
/*  212 */         String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
/*      */         
/*  214 */         StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */         
/*  216 */         int maxLength = 0;
/*      */         
/*  218 */         while (tokenizer.hasMoreTokens()) {
/*  219 */           maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */         }
/*      */ 
/*      */         
/*  223 */         this.columnSize = Constants.integerValueOf(maxLength);
/*  224 */         this.decimalDigits = null;
/*  225 */       } else if (StringUtils.startsWithIgnoreCase(typeInfo, "set")) {
/*  226 */         String temp = typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
/*      */         
/*  228 */         StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */         
/*  230 */         int maxLength = 0;
/*      */         
/*  232 */         int numElements = tokenizer.countTokens();
/*      */         
/*  234 */         if (numElements > 0) {
/*  235 */           maxLength += numElements - 1;
/*      */         }
/*      */         
/*  238 */         while (tokenizer.hasMoreTokens()) {
/*  239 */           String setMember = tokenizer.nextToken().trim();
/*      */           
/*  241 */           if (setMember.startsWith("'") && setMember.endsWith("'")) {
/*      */             
/*  243 */             maxLength += setMember.length() - 2; continue;
/*      */           } 
/*  245 */           maxLength += setMember.length();
/*      */         } 
/*      */ 
/*      */         
/*  249 */         this.columnSize = Constants.integerValueOf(maxLength);
/*  250 */         this.decimalDigits = null;
/*  251 */       } else if (typeInfo.indexOf(",") != -1) {
/*      */         
/*  253 */         this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim());
/*      */         
/*  255 */         this.decimalDigits = Integer.valueOf(typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim());
/*      */       }
/*      */       else {
/*      */         
/*  259 */         this.columnSize = null;
/*  260 */         this.decimalDigits = null;
/*      */ 
/*      */         
/*  263 */         if ((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "text") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "bit") != -1) && typeInfo.indexOf("(") != -1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  270 */           int endParenIndex = typeInfo.indexOf(")");
/*      */           
/*  272 */           if (endParenIndex == -1) {
/*  273 */             endParenIndex = typeInfo.length();
/*      */           }
/*      */           
/*  276 */           this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, endParenIndex).trim());
/*      */ 
/*      */ 
/*      */           
/*  280 */           if (this$0.conn.getTinyInt1isBit() && this.columnSize.intValue() == 1 && StringUtils.startsWithIgnoreCase(typeInfo, 0, "tinyint"))
/*      */           {
/*      */ 
/*      */             
/*  284 */             if (this$0.conn.getTransformedBitIsBoolean()) {
/*  285 */               this.dataType = 16;
/*  286 */               this.typeName = "BOOLEAN";
/*      */             } else {
/*  288 */               this.dataType = -7;
/*  289 */               this.typeName = "BIT";
/*      */             } 
/*      */           }
/*  292 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint")) {
/*      */           
/*  294 */           if (this$0.conn.getTinyInt1isBit() && typeInfo.indexOf("(1)") != -1) {
/*  295 */             if (this$0.conn.getTransformedBitIsBoolean()) {
/*  296 */               this.dataType = 16;
/*  297 */               this.typeName = "BOOLEAN";
/*      */             } else {
/*  299 */               this.dataType = -7;
/*  300 */               this.typeName = "BIT";
/*      */             } 
/*      */           } else {
/*  303 */             this.columnSize = Constants.integerValueOf(3);
/*  304 */             this.decimalDigits = Constants.integerValueOf(0);
/*      */           } 
/*  306 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint")) {
/*      */           
/*  308 */           this.columnSize = Constants.integerValueOf(5);
/*  309 */           this.decimalDigits = Constants.integerValueOf(0);
/*  310 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint")) {
/*      */           
/*  312 */           this.columnSize = Constants.integerValueOf(isUnsigned ? 8 : 7);
/*  313 */           this.decimalDigits = Constants.integerValueOf(0);
/*  314 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int")) {
/*      */           
/*  316 */           this.columnSize = Constants.integerValueOf(10);
/*  317 */           this.decimalDigits = Constants.integerValueOf(0);
/*  318 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer")) {
/*      */           
/*  320 */           this.columnSize = Constants.integerValueOf(10);
/*  321 */           this.decimalDigits = Constants.integerValueOf(0);
/*  322 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint")) {
/*      */           
/*  324 */           this.columnSize = Constants.integerValueOf(isUnsigned ? 20 : 19);
/*  325 */           this.decimalDigits = Constants.integerValueOf(0);
/*  326 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24")) {
/*      */           
/*  328 */           this.columnSize = Constants.integerValueOf(19);
/*  329 */           this.decimalDigits = Constants.integerValueOf(0);
/*  330 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real")) {
/*      */           
/*  332 */           this.columnSize = Constants.integerValueOf(12);
/*  333 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "float")) {
/*      */           
/*  335 */           this.columnSize = Constants.integerValueOf(12);
/*  336 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "decimal")) {
/*      */           
/*  338 */           this.columnSize = Constants.integerValueOf(12);
/*  339 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric")) {
/*      */           
/*  341 */           this.columnSize = Constants.integerValueOf(12);
/*  342 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "double")) {
/*      */           
/*  344 */           this.columnSize = Constants.integerValueOf(22);
/*  345 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char")) {
/*      */           
/*  347 */           this.columnSize = Constants.integerValueOf(1);
/*  348 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar")) {
/*      */           
/*  350 */           this.columnSize = Constants.integerValueOf(255);
/*  351 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp")) {
/*      */           
/*  353 */           this.columnSize = Constants.integerValueOf(19);
/*  354 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime")) {
/*      */           
/*  356 */           this.columnSize = Constants.integerValueOf(19);
/*  357 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date")) {
/*      */           
/*  359 */           this.columnSize = Constants.integerValueOf(10);
/*  360 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "time")) {
/*      */           
/*  362 */           this.columnSize = Constants.integerValueOf(8);
/*      */         }
/*  364 */         else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob")) {
/*      */           
/*  366 */           this.columnSize = Constants.integerValueOf(255);
/*  367 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob")) {
/*      */           
/*  369 */           this.columnSize = Constants.integerValueOf(65535);
/*  370 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob")) {
/*      */           
/*  372 */           this.columnSize = Constants.integerValueOf(16777215);
/*  373 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob")) {
/*      */           
/*  375 */           this.columnSize = Constants.integerValueOf(2147483647);
/*  376 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext")) {
/*      */           
/*  378 */           this.columnSize = Constants.integerValueOf(255);
/*  379 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "text")) {
/*      */           
/*  381 */           this.columnSize = Constants.integerValueOf(65535);
/*  382 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext")) {
/*      */           
/*  384 */           this.columnSize = Constants.integerValueOf(16777215);
/*  385 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext")) {
/*      */           
/*  387 */           this.columnSize = Constants.integerValueOf(2147483647);
/*  388 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum")) {
/*      */           
/*  390 */           this.columnSize = Constants.integerValueOf(255);
/*  391 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set")) {
/*      */           
/*  393 */           this.columnSize = Constants.integerValueOf(255);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  399 */       this.bufferLength = MysqlIO.getMaxBuf();
/*      */ 
/*      */       
/*  402 */       this.numPrecRadix = 10;
/*      */ 
/*      */       
/*  405 */       if (nullabilityInfo != null) {
/*  406 */         if (nullabilityInfo.equals("YES")) {
/*  407 */           this.nullability = 1;
/*  408 */           this.isNullable = "YES";
/*      */         }
/*      */         else {
/*      */           
/*  412 */           this.nullability = 0;
/*  413 */           this.isNullable = "NO";
/*      */         } 
/*      */       } else {
/*  416 */         this.nullability = 0;
/*  417 */         this.isNullable = "NO";
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
/*  459 */   private static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
/*      */   
/*  461 */   private static final byte[] SYSTEM_TABLE_AS_BYTES = "SYSTEM TABLE".getBytes();
/*      */   
/*      */   private static final int UPDATE_RULE = 9;
/*      */   
/*  465 */   private static final byte[] VIEW_AS_BYTES = "VIEW".getBytes();
/*      */   
/*      */   private static final Constructor JDBC_4_DBMD_SHOW_CTOR;
/*      */   private static final Constructor JDBC_4_DBMD_IS_CTOR;
/*      */   protected ConnectionImpl conn;
/*      */   
/*      */   static {
/*  472 */     if (Util.isJdbc4()) {
/*      */       try {
/*  474 */         JDBC_4_DBMD_SHOW_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaData").getConstructor(new Class[] { ConnectionImpl.class, String.class });
/*      */ 
/*      */ 
/*      */         
/*  478 */         JDBC_4_DBMD_IS_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema").getConstructor(new Class[] { ConnectionImpl.class, String.class });
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  483 */       catch (SecurityException e) {
/*  484 */         throw new RuntimeException(e);
/*  485 */       } catch (NoSuchMethodException e) {
/*  486 */         throw new RuntimeException(e);
/*  487 */       } catch (ClassNotFoundException e) {
/*  488 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*  491 */       JDBC_4_DBMD_IS_CTOR = null;
/*  492 */       JDBC_4_DBMD_SHOW_CTOR = null;
/*      */     } 
/*      */ 
/*      */     
/*  496 */     String[] allMySQLKeywords = { "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONNECTION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_ONLY", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "X509", "XOR", "YEAR_MONTH", "ZEROFILL" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  539 */     String[] sql92Keywords = { "ABSOLUTE", "EXEC", "OVERLAPS", "ACTION", "EXECUTE", "PAD", "ADA", "EXISTS", "PARTIAL", "ADD", "EXTERNAL", "PASCAL", "ALL", "EXTRACT", "POSITION", "ALLOCATE", "FALSE", "PRECISION", "ALTER", "FETCH", "PREPARE", "AND", "FIRST", "PRESERVE", "ANY", "FLOAT", "PRIMARY", "ARE", "FOR", "PRIOR", "AS", "FOREIGN", "PRIVILEGES", "ASC", "FORTRAN", "PROCEDURE", "ASSERTION", "FOUND", "PUBLIC", "AT", "FROM", "READ", "AUTHORIZATION", "FULL", "REAL", "AVG", "GET", "REFERENCES", "BEGIN", "GLOBAL", "RELATIVE", "BETWEEN", "GO", "RESTRICT", "BIT", "GOTO", "REVOKE", "BIT_LENGTH", "GRANT", "RIGHT", "BOTH", "GROUP", "ROLLBACK", "BY", "HAVING", "ROWS", "CASCADE", "HOUR", "SCHEMA", "CASCADED", "IDENTITY", "SCROLL", "CASE", "IMMEDIATE", "SECOND", "CAST", "IN", "SECTION", "CATALOG", "INCLUDE", "SELECT", "CHAR", "INDEX", "SESSION", "CHAR_LENGTH", "INDICATOR", "SESSION_USER", "CHARACTER", "INITIALLY", "SET", "CHARACTER_LENGTH", "INNER", "SIZE", "CHECK", "INPUT", "SMALLINT", "CLOSE", "INSENSITIVE", "SOME", "COALESCE", "INSERT", "SPACE", "COLLATE", "INT", "SQL", "COLLATION", "INTEGER", "SQLCA", "COLUMN", "INTERSECT", "SQLCODE", "COMMIT", "INTERVAL", "SQLERROR", "CONNECT", "INTO", "SQLSTATE", "CONNECTION", "IS", "SQLWARNING", "CONSTRAINT", "ISOLATION", "SUBSTRING", "CONSTRAINTS", "JOIN", "SUM", "CONTINUE", "KEY", "SYSTEM_USER", "CONVERT", "LANGUAGE", "TABLE", "CORRESPONDING", "LAST", "TEMPORARY", "COUNT", "LEADING", "THEN", "CREATE", "LEFT", "TIME", "CROSS", "LEVEL", "TIMESTAMP", "CURRENT", "LIKE", "TIMEZONE_HOUR", "CURRENT_DATE", "LOCAL", "TIMEZONE_MINUTE", "CURRENT_TIME", "LOWER", "TO", "CURRENT_TIMESTAMP", "MATCH", "TRAILING", "CURRENT_USER", "MAX", "TRANSACTION", "CURSOR", "MIN", "TRANSLATE", "DATE", "MINUTE", "TRANSLATION", "DAY", "MODULE", "TRIM", "DEALLOCATE", "MONTH", "TRUE", "DEC", "NAMES", "UNION", "DECIMAL", "NATIONAL", "UNIQUE", "DECLARE", "NATURAL", "UNKNOWN", "DEFAULT", "NCHAR", "UPDATE", "DEFERRABLE", "NEXT", "UPPER", "DEFERRED", "NO", "USAGE", "DELETE", "NONE", "USER", "DESC", "NOT", "USING", "DESCRIBE", "NULL", "VALUE", "DESCRIPTOR", "NULLIF", "VALUES", "DIAGNOSTICS", "NUMERIC", "VARCHAR", "DISCONNECT", "OCTET_LENGTH", "VARYING", "DISTINCT", "OF", "VIEW", "DOMAIN", "ON", "WHEN", "DOUBLE", "ONLY", "WHENEVER", "DROP", "OPEN", "WHERE", "ELSE", "OPTION", "WITH", "END", "OR", "WORK", "END-EXEC", "ORDER", "WRITE", "ESCAPE", "OUTER", "YEAR", "EXCEPT", "OUTPUT", "ZONE", "EXCEPTION" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  581 */     TreeMap mySQLKeywordMap = new TreeMap();
/*      */     
/*  583 */     for (int i = 0; i < allMySQLKeywords.length; i++) {
/*  584 */       mySQLKeywordMap.put(allMySQLKeywords[i], null);
/*      */     }
/*      */     
/*  587 */     HashMap sql92KeywordMap = new HashMap(sql92Keywords.length);
/*      */     
/*  589 */     for (int j = 0; j < sql92Keywords.length; j++) {
/*  590 */       sql92KeywordMap.put(sql92Keywords[j], null);
/*      */     }
/*      */     
/*  593 */     Iterator it = sql92KeywordMap.keySet().iterator();
/*      */     
/*  595 */     while (it.hasNext()) {
/*  596 */       mySQLKeywordMap.remove(it.next());
/*      */     }
/*      */     
/*  599 */     StringBuffer keywordBuf = new StringBuffer();
/*      */     
/*  601 */     it = mySQLKeywordMap.keySet().iterator();
/*      */     
/*  603 */     if (it.hasNext()) {
/*  604 */       keywordBuf.append(it.next().toString());
/*      */     }
/*      */     
/*  607 */     while (it.hasNext()) {
/*  608 */       keywordBuf.append(",");
/*  609 */       keywordBuf.append(it.next().toString());
/*      */     } 
/*      */     
/*  612 */     mysqlKeywordsThatArentSQL92 = keywordBuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  619 */   protected String database = null;
/*      */ 
/*      */   
/*  622 */   protected String quotedId = null;
/*      */ 
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */ 
/*      */ 
/*      */   
/*      */   protected static DatabaseMetaData getInstance(ConnectionImpl connToSet, String databaseToSet, boolean checkForInfoSchema) throws SQLException {
/*  630 */     if (!Util.isJdbc4()) {
/*  631 */       if (checkForInfoSchema && connToSet != null && connToSet.getUseInformationSchema() && connToSet.versionMeetsMinimum(5, 0, 7))
/*      */       {
/*      */         
/*  634 */         return new DatabaseMetaDataUsingInfoSchema(connToSet, databaseToSet);
/*      */       }
/*      */ 
/*      */       
/*  638 */       return new DatabaseMetaData(connToSet, databaseToSet);
/*      */     } 
/*      */     
/*  641 */     if (checkForInfoSchema && connToSet != null && connToSet.getUseInformationSchema() && connToSet.versionMeetsMinimum(5, 0, 7))
/*      */     {
/*      */ 
/*      */       
/*  645 */       return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_IS_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  650 */     return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_SHOW_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
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
/*      */   protected DatabaseMetaData(ConnectionImpl connToSet, String databaseToSet) {
/*  663 */     this.conn = connToSet;
/*  664 */     this.database = databaseToSet;
/*  665 */     this.exceptionInterceptor = this.conn.getExceptionInterceptor();
/*      */     
/*      */     try {
/*  668 */       this.quotedId = this.conn.supportsQuotedIdentifiers() ? getIdentifierQuoteString() : "";
/*      */     }
/*  670 */     catch (SQLException sqlEx) {
/*      */ 
/*      */ 
/*      */       
/*  674 */       AssertionFailedException.shouldNotHappen(sqlEx);
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
/*      */   public boolean allProceduresAreCallable() throws SQLException {
/*  687 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean allTablesAreSelectable() throws SQLException {
/*  698 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private ResultSet buildResultSet(Field[] fields, ArrayList rows) throws SQLException {
/*  703 */     return buildResultSet(fields, rows, this.conn);
/*      */   }
/*      */ 
/*      */   
/*      */   static ResultSet buildResultSet(Field[] fields, ArrayList rows, ConnectionImpl c) throws SQLException {
/*  708 */     int fieldsLength = fields.length;
/*      */     
/*  710 */     for (int i = 0; i < fieldsLength; i++) {
/*  711 */       int jdbcType = fields[i].getSQLType();
/*      */       
/*  713 */       switch (jdbcType) {
/*      */         case -1:
/*      */         case 1:
/*      */         case 12:
/*  717 */           fields[i].setCharacterSet(c.getCharacterSetMetadata());
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  723 */       fields[i].setConnection(c);
/*  724 */       fields[i].setUseOldNameMetadata(true);
/*      */     } 
/*      */     
/*  727 */     return ResultSetImpl.getInstance(c.getCatalog(), fields, new RowDataStatic(rows), c, null, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void convertToJdbcFunctionList(String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex, Field[] fields) throws SQLException {
/*  735 */     while (proceduresRs.next()) {
/*  736 */       boolean shouldAdd = true;
/*      */       
/*  738 */       if (needsClientFiltering) {
/*  739 */         shouldAdd = false;
/*      */         
/*  741 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  743 */         if (db == null && procDb == null) {
/*  744 */           shouldAdd = true;
/*  745 */         } else if (db != null && db.equals(procDb)) {
/*  746 */           shouldAdd = true;
/*      */         } 
/*      */       } 
/*      */       
/*  750 */       if (shouldAdd) {
/*  751 */         String functionName = proceduresRs.getString(nameIndex);
/*      */         
/*  753 */         byte[][] rowData = (byte[][])null;
/*      */         
/*  755 */         if (fields != null && fields.length == 9) {
/*      */           
/*  757 */           rowData = new byte[9][];
/*  758 */           rowData[0] = (catalog == null) ? null : s2b(catalog);
/*  759 */           rowData[1] = null;
/*  760 */           rowData[2] = s2b(functionName);
/*  761 */           rowData[3] = null;
/*  762 */           rowData[4] = null;
/*  763 */           rowData[5] = null;
/*  764 */           rowData[6] = s2b(proceduresRs.getString("comment"));
/*  765 */           rowData[7] = s2b(Integer.toString(2));
/*  766 */           rowData[8] = s2b(functionName);
/*      */         } else {
/*      */           
/*  769 */           rowData = new byte[6][];
/*      */           
/*  771 */           rowData[0] = (catalog == null) ? null : s2b(catalog);
/*  772 */           rowData[1] = null;
/*  773 */           rowData[2] = s2b(functionName);
/*  774 */           rowData[3] = s2b(proceduresRs.getString("comment"));
/*  775 */           rowData[4] = s2b(Integer.toString(getJDBC4FunctionNoTableConstant()));
/*  776 */           rowData[5] = s2b(functionName);
/*      */         } 
/*      */         
/*  779 */         procedureRowsOrderedByName.put(functionName, new ByteArrayRow(rowData, getExceptionInterceptor()));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int getJDBC4FunctionNoTableConstant() {
/*  785 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void convertToJdbcProcedureList(boolean fromSelect, String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex) throws SQLException {
/*  791 */     while (proceduresRs.next()) {
/*  792 */       boolean shouldAdd = true;
/*      */       
/*  794 */       if (needsClientFiltering) {
/*  795 */         shouldAdd = false;
/*      */         
/*  797 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  799 */         if (db == null && procDb == null) {
/*  800 */           shouldAdd = true;
/*  801 */         } else if (db != null && db.equals(procDb)) {
/*  802 */           shouldAdd = true;
/*      */         } 
/*      */       } 
/*      */       
/*  806 */       if (shouldAdd) {
/*  807 */         String procedureName = proceduresRs.getString(nameIndex);
/*  808 */         byte[][] rowData = new byte[9][];
/*  809 */         rowData[0] = (catalog == null) ? null : s2b(catalog);
/*  810 */         rowData[1] = null;
/*  811 */         rowData[2] = s2b(procedureName);
/*  812 */         rowData[3] = null;
/*  813 */         rowData[4] = null;
/*  814 */         rowData[5] = null;
/*  815 */         rowData[6] = null;
/*      */         
/*  817 */         boolean isFunction = fromSelect ? "FUNCTION".equalsIgnoreCase(proceduresRs.getString("type")) : false;
/*      */ 
/*      */         
/*  820 */         rowData[7] = s2b(isFunction ? Integer.toString(2) : Integer.toString(0));
/*      */ 
/*      */ 
/*      */         
/*  824 */         rowData[8] = s2b(procedureName);
/*      */         
/*  826 */         procedureRowsOrderedByName.put(procedureName, new ByteArrayRow(rowData, getExceptionInterceptor()));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResultSetRow convertTypeDescriptorToProcedureRow(byte[] procNameAsBytes, String paramName, boolean isOutParam, boolean isInParam, boolean isReturnParam, TypeDescriptor typeDesc, boolean forGetFunctionColumns, int ordinal) throws SQLException {
/*  837 */     byte[][] row = forGetFunctionColumns ? new byte[17][] : new byte[14][];
/*  838 */     row[0] = null;
/*  839 */     row[1] = null;
/*  840 */     row[2] = procNameAsBytes;
/*  841 */     row[3] = s2b(paramName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  848 */     if (isInParam && isOutParam) {
/*  849 */       row[4] = s2b(String.valueOf(2));
/*  850 */     } else if (isInParam) {
/*  851 */       row[4] = s2b(String.valueOf(1));
/*  852 */     } else if (isOutParam) {
/*  853 */       row[4] = s2b(String.valueOf(4));
/*  854 */     } else if (isReturnParam) {
/*  855 */       row[4] = s2b(String.valueOf(5));
/*      */     } else {
/*  857 */       row[4] = s2b(String.valueOf(0));
/*      */     } 
/*  859 */     row[5] = s2b(Short.toString(typeDesc.dataType));
/*  860 */     row[6] = s2b(typeDesc.typeName);
/*  861 */     row[7] = (typeDesc.columnSize == null) ? null : s2b(typeDesc.columnSize.toString());
/*  862 */     row[8] = row[7];
/*  863 */     row[9] = (typeDesc.decimalDigits == null) ? null : s2b(typeDesc.decimalDigits.toString());
/*  864 */     row[10] = s2b(Integer.toString(typeDesc.numPrecRadix));
/*      */     
/*  866 */     switch (typeDesc.nullability) {
/*      */       case 0:
/*  868 */         row[11] = s2b(String.valueOf(0));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/*  873 */         row[11] = s2b(String.valueOf(1));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 2:
/*  878 */         row[11] = s2b(String.valueOf(2));
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  883 */         throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", "S1000", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  888 */     row[12] = null;
/*      */     
/*  890 */     if (forGetFunctionColumns) {
/*      */       
/*  892 */       row[13] = null;
/*      */ 
/*      */       
/*  895 */       row[14] = s2b(String.valueOf(ordinal));
/*      */ 
/*      */       
/*  898 */       row[15] = Constants.EMPTY_BYTE_ARRAY;
/*      */       
/*  900 */       row[16] = s2b(paramName);
/*      */     } 
/*      */     
/*  903 */     return new ByteArrayRow(row, getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected ExceptionInterceptor getExceptionInterceptor() {
/*  909 */     return this.exceptionInterceptor;
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
/*      */   public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
/*  921 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
/*  932 */     return false;
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
/*      */   public boolean deletesAreDetected(int type) throws SQLException {
/*  947 */     return false;
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
/*      */   public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
/*  960 */     return true;
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
/*      */   public List extractForeignKeyForTable(ArrayList rows, ResultSet rs, String catalog) throws SQLException {
/*  978 */     byte[][] row = new byte[3][];
/*  979 */     row[0] = rs.getBytes(1);
/*  980 */     row[1] = s2b("SUPPORTS_FK");
/*      */     
/*  982 */     String createTableString = rs.getString(2);
/*  983 */     StringTokenizer lineTokenizer = new StringTokenizer(createTableString, "\n");
/*      */     
/*  985 */     StringBuffer commentBuf = new StringBuffer("comment; ");
/*  986 */     boolean firstTime = true;
/*      */     
/*  988 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/*  990 */     if (quoteChar == null) {
/*  991 */       quoteChar = "`";
/*      */     }
/*      */     
/*  994 */     while (lineTokenizer.hasMoreTokens()) {
/*  995 */       String line = lineTokenizer.nextToken().trim();
/*      */       
/*  997 */       String constraintName = null;
/*      */       
/*  999 */       if (StringUtils.startsWithIgnoreCase(line, "CONSTRAINT")) {
/* 1000 */         boolean usingBackTicks = true;
/* 1001 */         int beginPos = line.indexOf(quoteChar);
/*      */         
/* 1003 */         if (beginPos == -1) {
/* 1004 */           beginPos = line.indexOf("\"");
/* 1005 */           usingBackTicks = false;
/*      */         } 
/*      */         
/* 1008 */         if (beginPos != -1) {
/* 1009 */           int endPos = -1;
/*      */           
/* 1011 */           if (usingBackTicks) {
/* 1012 */             endPos = line.indexOf(quoteChar, beginPos + 1);
/*      */           } else {
/* 1014 */             endPos = line.indexOf("\"", beginPos + 1);
/*      */           } 
/*      */           
/* 1017 */           if (endPos != -1) {
/* 1018 */             constraintName = line.substring(beginPos + 1, endPos);
/* 1019 */             line = line.substring(endPos + 1, line.length()).trim();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1025 */       if (line.startsWith("FOREIGN KEY")) {
/* 1026 */         if (line.endsWith(",")) {
/* 1027 */           line = line.substring(0, line.length() - 1);
/*      */         }
/*      */         
/* 1030 */         char quote = this.quotedId.charAt(0);
/*      */         
/* 1032 */         int indexOfFK = line.indexOf("FOREIGN KEY");
/*      */         
/* 1034 */         String localColumnName = null;
/* 1035 */         String referencedCatalogName = this.quotedId + catalog + this.quotedId;
/* 1036 */         String referencedTableName = null;
/* 1037 */         String referencedColumnName = null;
/*      */ 
/*      */         
/* 1040 */         if (indexOfFK != -1) {
/* 1041 */           int afterFk = indexOfFK + "FOREIGN KEY".length();
/*      */           
/* 1043 */           int indexOfRef = StringUtils.indexOfIgnoreCaseRespectQuotes(afterFk, line, "REFERENCES", quote, true);
/*      */           
/* 1045 */           if (indexOfRef != -1) {
/*      */             
/* 1047 */             int indexOfParenOpen = line.indexOf('(', afterFk);
/* 1048 */             int indexOfParenClose = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfParenOpen, line, ")", quote, true);
/*      */             
/* 1050 */             if (indexOfParenOpen == -1 || indexOfParenClose == -1);
/*      */ 
/*      */ 
/*      */             
/* 1054 */             localColumnName = line.substring(indexOfParenOpen + 1, indexOfParenClose);
/*      */             
/* 1056 */             int afterRef = indexOfRef + "REFERENCES".length();
/*      */             
/* 1058 */             int referencedColumnBegin = StringUtils.indexOfIgnoreCaseRespectQuotes(afterRef, line, "(", quote, true);
/*      */             
/* 1060 */             if (referencedColumnBegin != -1) {
/* 1061 */               referencedTableName = line.substring(afterRef, referencedColumnBegin);
/*      */               
/* 1063 */               int referencedColumnEnd = StringUtils.indexOfIgnoreCaseRespectQuotes(referencedColumnBegin + 1, line, ")", quote, true);
/*      */               
/* 1065 */               if (referencedColumnEnd != -1) {
/* 1066 */                 referencedColumnName = line.substring(referencedColumnBegin + 1, referencedColumnEnd);
/*      */               }
/*      */               
/* 1069 */               int indexOfCatalogSep = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referencedTableName, ".", quote, true);
/*      */               
/* 1071 */               if (indexOfCatalogSep != -1) {
/* 1072 */                 referencedCatalogName = referencedTableName.substring(0, indexOfCatalogSep);
/* 1073 */                 referencedTableName = referencedTableName.substring(indexOfCatalogSep + 1);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1080 */         if (!firstTime) {
/* 1081 */           commentBuf.append("; ");
/*      */         } else {
/* 1083 */           firstTime = false;
/*      */         } 
/*      */         
/* 1086 */         if (constraintName != null) {
/* 1087 */           commentBuf.append(constraintName);
/*      */         } else {
/* 1089 */           commentBuf.append("not_available");
/*      */         } 
/*      */         
/* 1092 */         commentBuf.append("(");
/* 1093 */         commentBuf.append(localColumnName);
/* 1094 */         commentBuf.append(") REFER ");
/* 1095 */         commentBuf.append(referencedCatalogName);
/* 1096 */         commentBuf.append("/");
/* 1097 */         commentBuf.append(referencedTableName);
/* 1098 */         commentBuf.append("(");
/* 1099 */         commentBuf.append(referencedColumnName);
/* 1100 */         commentBuf.append(")");
/*      */         
/* 1102 */         int lastParenIndex = line.lastIndexOf(")");
/*      */         
/* 1104 */         if (lastParenIndex != line.length() - 1) {
/* 1105 */           String cascadeOptions = line.substring(lastParenIndex + 1);
/*      */           
/* 1107 */           commentBuf.append(" ");
/* 1108 */           commentBuf.append(cascadeOptions);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1113 */     row[2] = s2b(commentBuf.toString());
/* 1114 */     rows.add(new ByteArrayRow(row, getExceptionInterceptor()));
/*      */     
/* 1116 */     return rows;
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
/*      */   public ResultSet extractForeignKeyFromCreateTable(String catalog, String tableName) throws SQLException {
/* 1137 */     ArrayList tableList = new ArrayList();
/* 1138 */     ResultSet rs = null;
/* 1139 */     Statement stmt = null;
/*      */     
/* 1141 */     if (tableName != null) {
/* 1142 */       tableList.add(tableName);
/*      */     } else {
/*      */       try {
/* 1145 */         rs = getTables(catalog, "", "%", new String[] { "TABLE" });
/*      */         
/* 1147 */         while (rs.next()) {
/* 1148 */           tableList.add(rs.getString("TABLE_NAME"));
/*      */         }
/*      */       } finally {
/* 1151 */         if (rs != null) {
/* 1152 */           rs.close();
/*      */         }
/*      */         
/* 1155 */         rs = null;
/*      */       } 
/*      */     } 
/*      */     
/* 1159 */     ArrayList rows = new ArrayList();
/* 1160 */     Field[] fields = new Field[3];
/* 1161 */     fields[0] = new Field("", "Name", 1, 2147483647);
/* 1162 */     fields[1] = new Field("", "Type", 1, 255);
/* 1163 */     fields[2] = new Field("", "Comment", 1, 2147483647);
/*      */     
/* 1165 */     int numTables = tableList.size();
/* 1166 */     stmt = this.conn.getMetadataSafeStatement();
/*      */     
/* 1168 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1170 */     if (quoteChar == null) {
/* 1171 */       quoteChar = "`";
/*      */     }
/*      */     
/*      */     try {
/* 1175 */       for (int i = 0; i < numTables; i++) {
/* 1176 */         String tableToExtract = tableList.get(i);
/*      */         
/* 1178 */         String query = "SHOW CREATE TABLE " + quoteChar + catalog + quoteChar + "." + quoteChar + tableToExtract + quoteChar;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1184 */           rs = stmt.executeQuery(query);
/* 1185 */         } catch (SQLException sqlEx) {
/*      */           
/* 1187 */           String sqlState = sqlEx.getSQLState();
/*      */           
/* 1189 */           if (!"42S02".equals(sqlState) && sqlEx.getErrorCode() != 1146)
/*      */           {
/* 1191 */             throw sqlEx;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1197 */         while (rs.next()) {
/* 1198 */           extractForeignKeyForTable(rows, rs, catalog);
/*      */         }
/*      */       } 
/*      */     } finally {
/* 1202 */       if (rs != null) {
/* 1203 */         rs.close();
/*      */       }
/*      */       
/* 1206 */       rs = null;
/*      */       
/* 1208 */       if (stmt != null) {
/* 1209 */         stmt.close();
/*      */       }
/*      */       
/* 1212 */       stmt = null;
/*      */     } 
/*      */     
/* 1215 */     return buildResultSet(fields, rows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3) throws SQLException {
/* 1223 */     Field[] fields = new Field[21];
/* 1224 */     fields[0] = new Field("", "TYPE_CAT", 1, 32);
/* 1225 */     fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
/* 1226 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 1227 */     fields[3] = new Field("", "ATTR_NAME", 1, 32);
/* 1228 */     fields[4] = new Field("", "DATA_TYPE", 5, 32);
/* 1229 */     fields[5] = new Field("", "ATTR_TYPE_NAME", 1, 32);
/* 1230 */     fields[6] = new Field("", "ATTR_SIZE", 4, 32);
/* 1231 */     fields[7] = new Field("", "DECIMAL_DIGITS", 4, 32);
/* 1232 */     fields[8] = new Field("", "NUM_PREC_RADIX", 4, 32);
/* 1233 */     fields[9] = new Field("", "NULLABLE ", 4, 32);
/* 1234 */     fields[10] = new Field("", "REMARKS", 1, 32);
/* 1235 */     fields[11] = new Field("", "ATTR_DEF", 1, 32);
/* 1236 */     fields[12] = new Field("", "SQL_DATA_TYPE", 4, 32);
/* 1237 */     fields[13] = new Field("", "SQL_DATETIME_SUB", 4, 32);
/* 1238 */     fields[14] = new Field("", "CHAR_OCTET_LENGTH", 4, 32);
/* 1239 */     fields[15] = new Field("", "ORDINAL_POSITION", 4, 32);
/* 1240 */     fields[16] = new Field("", "IS_NULLABLE", 1, 32);
/* 1241 */     fields[17] = new Field("", "SCOPE_CATALOG", 1, 32);
/* 1242 */     fields[18] = new Field("", "SCOPE_SCHEMA", 1, 32);
/* 1243 */     fields[19] = new Field("", "SCOPE_TABLE", 1, 32);
/* 1244 */     fields[20] = new Field("", "SOURCE_DATA_TYPE", 5, 32);
/*      */     
/* 1246 */     return buildResultSet(fields, new ArrayList());
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
/*      */   public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
/* 1297 */     if (table == null) {
/* 1298 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 1302 */     Field[] fields = new Field[8];
/* 1303 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 1304 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 1305 */     fields[2] = new Field("", "DATA_TYPE", 4, 32);
/* 1306 */     fields[3] = new Field("", "TYPE_NAME", 1, 32);
/* 1307 */     fields[4] = new Field("", "COLUMN_SIZE", 4, 10);
/* 1308 */     fields[5] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 1309 */     fields[6] = new Field("", "DECIMAL_DIGITS", 5, 10);
/* 1310 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 1312 */     ArrayList rows = new ArrayList();
/* 1313 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */     
/*      */     try {
/* 1317 */       (new IterateBlock(this, getCatalogIterator(catalog), table, stmt, rows) { private final String val$table; private final Statement val$stmt; private final ArrayList val$rows; private final DatabaseMetaData this$0;
/*      */           void forEach(Object catalogStr) throws SQLException {
/* 1319 */             ResultSet results = null;
/*      */             
/*      */             try {
/* 1322 */               StringBuffer queryBuf = new StringBuffer("SHOW COLUMNS FROM ");
/*      */               
/* 1324 */               queryBuf.append(this.this$0.quotedId);
/* 1325 */               queryBuf.append(this.val$table);
/* 1326 */               queryBuf.append(this.this$0.quotedId);
/* 1327 */               queryBuf.append(" FROM ");
/* 1328 */               queryBuf.append(this.this$0.quotedId);
/* 1329 */               queryBuf.append(catalogStr.toString());
/* 1330 */               queryBuf.append(this.this$0.quotedId);
/*      */               
/* 1332 */               results = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               
/* 1334 */               while (results.next()) {
/* 1335 */                 String keyType = results.getString("Key");
/*      */                 
/* 1337 */                 if (keyType != null && 
/* 1338 */                   StringUtils.startsWithIgnoreCase(keyType, "PRI"))
/*      */                 {
/* 1340 */                   byte[][] rowVal = new byte[8][];
/* 1341 */                   rowVal[0] = Integer.toString(2).getBytes();
/*      */ 
/*      */ 
/*      */                   
/* 1345 */                   rowVal[1] = results.getBytes("Field");
/*      */                   
/* 1347 */                   String type = results.getString("Type");
/* 1348 */                   int size = MysqlIO.getMaxBuf();
/* 1349 */                   int decimals = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1354 */                   if (type.indexOf("enum") != -1) {
/* 1355 */                     String temp = type.substring(type.indexOf("("), type.indexOf(")"));
/*      */ 
/*      */                     
/* 1358 */                     StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */                     
/* 1360 */                     int maxLength = 0;
/*      */                     
/* 1362 */                     while (tokenizer.hasMoreTokens()) {
/* 1363 */                       maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */                     }
/*      */ 
/*      */ 
/*      */                     
/* 1368 */                     size = maxLength;
/* 1369 */                     decimals = 0;
/* 1370 */                     type = "enum";
/* 1371 */                   } else if (type.indexOf("(") != -1) {
/* 1372 */                     if (type.indexOf(",") != -1) {
/* 1373 */                       size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(",")));
/*      */ 
/*      */ 
/*      */                       
/* 1377 */                       decimals = Integer.parseInt(type.substring(type.indexOf(",") + 1, type.indexOf(")")));
/*      */                     
/*      */                     }
/*      */                     else {
/*      */                       
/* 1382 */                       size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
/*      */                     } 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1388 */                     type = type.substring(0, type.indexOf("("));
/*      */                   } 
/*      */ 
/*      */                   
/* 1392 */                   rowVal[2] = this.this$0.s2b(String.valueOf(MysqlDefs.mysqlToJavaType(type)));
/*      */                   
/* 1394 */                   rowVal[3] = this.this$0.s2b(type);
/* 1395 */                   rowVal[4] = Integer.toString(size + decimals).getBytes();
/*      */                   
/* 1397 */                   rowVal[5] = Integer.toString(size + decimals).getBytes();
/*      */                   
/* 1399 */                   rowVal[6] = Integer.toString(decimals).getBytes();
/*      */                   
/* 1401 */                   rowVal[7] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1406 */                   this.val$rows.add(new ByteArrayRow(rowVal, this.this$0.getExceptionInterceptor()));
/*      */                 }
/*      */               
/*      */               } 
/* 1410 */             } catch (SQLException sqlEx) {
/* 1411 */               if (!"42S02".equals(sqlEx.getSQLState())) {
/* 1412 */                 throw sqlEx;
/*      */               }
/*      */             } finally {
/* 1415 */               if (results != null) {
/*      */                 try {
/* 1417 */                   results.close();
/* 1418 */                 } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */                 
/* 1422 */                 results = null;
/*      */               } 
/*      */             } 
/*      */           } }
/*      */         ).doForAll();
/*      */     } finally {
/* 1428 */       if (stmt != null) {
/* 1429 */         stmt.close();
/*      */       }
/*      */     } 
/*      */     
/* 1433 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 1435 */     return results;
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
/*      */   private void getCallStmtParameterTypes(String catalog, String procName, String parameterNamePattern, List resultRows) throws SQLException {
/* 1473 */     getCallStmtParameterTypes(catalog, procName, parameterNamePattern, resultRows, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getCallStmtParameterTypes(String catalog, String procName, String parameterNamePattern, List resultRows, boolean forGetFunctionColumns) throws SQLException {
/* 1480 */     Statement paramRetrievalStmt = null;
/* 1481 */     ResultSet paramRetrievalRs = null;
/*      */     
/* 1483 */     if (parameterNamePattern == null) {
/* 1484 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1485 */         parameterNamePattern = "%";
/*      */       } else {
/* 1487 */         throw SQLError.createSQLException("Parameter/Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1493 */     byte[] procNameAsBytes = null;
/*      */     
/*      */     try {
/* 1496 */       procNameAsBytes = procName.getBytes("UTF-8");
/* 1497 */     } catch (UnsupportedEncodingException ueEx) {
/* 1498 */       procNameAsBytes = s2b(procName);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1503 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1505 */     String parameterDef = null;
/*      */     
/* 1507 */     boolean isProcedureInAnsiMode = false;
/* 1508 */     String storageDefnDelims = null;
/* 1509 */     String storageDefnClosures = null;
/*      */     
/*      */     try {
/* 1512 */       paramRetrievalStmt = this.conn.getMetadataSafeStatement();
/*      */       
/* 1514 */       if (this.conn.lowerCaseTableNames() && catalog != null && catalog.length() != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1520 */         String oldCatalog = this.conn.getCatalog();
/* 1521 */         ResultSet rs = null;
/*      */         
/*      */         try {
/* 1524 */           this.conn.setCatalog(catalog);
/* 1525 */           rs = paramRetrievalStmt.executeQuery("SELECT DATABASE()");
/* 1526 */           rs.next();
/*      */           
/* 1528 */           catalog = rs.getString(1);
/*      */         }
/*      */         finally {
/*      */           
/* 1532 */           this.conn.setCatalog(oldCatalog);
/*      */           
/* 1534 */           if (rs != null) {
/* 1535 */             rs.close();
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1540 */       if (paramRetrievalStmt.getMaxRows() != 0) {
/* 1541 */         paramRetrievalStmt.setMaxRows(0);
/*      */       }
/*      */       
/* 1544 */       int dotIndex = -1;
/*      */       
/* 1546 */       if (!" ".equals(quoteChar)) {
/* 1547 */         dotIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       }
/*      */       else {
/*      */         
/* 1551 */         dotIndex = procName.indexOf(".");
/*      */       } 
/*      */       
/* 1554 */       String dbName = null;
/*      */       
/* 1556 */       if (dotIndex != -1 && dotIndex + 1 < procName.length()) {
/* 1557 */         dbName = procName.substring(0, dotIndex);
/* 1558 */         procName = procName.substring(dotIndex + 1);
/*      */       } else {
/* 1560 */         dbName = catalog;
/*      */       } 
/*      */       
/* 1563 */       StringBuffer procNameBuf = new StringBuffer();
/*      */       
/* 1565 */       if (dbName != null) {
/* 1566 */         if (!" ".equals(quoteChar) && !dbName.startsWith(quoteChar)) {
/* 1567 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1570 */         procNameBuf.append(dbName);
/*      */         
/* 1572 */         if (!" ".equals(quoteChar) && !dbName.startsWith(quoteChar)) {
/* 1573 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1576 */         procNameBuf.append(".");
/*      */       } 
/*      */       
/* 1579 */       boolean procNameIsNotQuoted = !procName.startsWith(quoteChar);
/*      */       
/* 1581 */       if (!" ".equals(quoteChar) && procNameIsNotQuoted) {
/* 1582 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1585 */       procNameBuf.append(procName);
/*      */       
/* 1587 */       if (!" ".equals(quoteChar) && procNameIsNotQuoted) {
/* 1588 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1591 */       boolean parsingFunction = false;
/*      */       
/*      */       try {
/* 1594 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE PROCEDURE " + procNameBuf.toString());
/*      */ 
/*      */         
/* 1597 */         parsingFunction = false;
/* 1598 */       } catch (SQLException sqlEx) {
/* 1599 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE FUNCTION " + procNameBuf.toString());
/*      */ 
/*      */         
/* 1602 */         parsingFunction = true;
/*      */       } 
/*      */       
/* 1605 */       if (paramRetrievalRs.next()) {
/* 1606 */         String procedureDef = parsingFunction ? paramRetrievalRs.getString("Create Function") : paramRetrievalRs.getString("Create Procedure");
/*      */ 
/*      */ 
/*      */         
/* 1610 */         if (procedureDef == null || procedureDef.length() == 0) {
/* 1611 */           throw SQLError.createSQLException("User does not have access to metadata required to determine stored procedure parameter types. If rights can not be granted, configure connection with \"noAccessToProcedureBodies=true\" to have driver generate parameters that represent INOUT strings irregardless of actual parameter types.", "S1000", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1618 */           String sqlMode = paramRetrievalRs.getString("sql_mode");
/*      */           
/* 1620 */           if (StringUtils.indexOfIgnoreCase(sqlMode, "ANSI") != -1) {
/* 1621 */             isProcedureInAnsiMode = true;
/*      */           }
/* 1623 */         } catch (SQLException sqlEx) {}
/*      */ 
/*      */ 
/*      */         
/* 1627 */         String identifierMarkers = isProcedureInAnsiMode ? "`\"" : "`";
/* 1628 */         String identifierAndStringMarkers = "'" + identifierMarkers;
/* 1629 */         storageDefnDelims = "(" + identifierMarkers;
/* 1630 */         storageDefnClosures = ")" + identifierMarkers;
/*      */ 
/*      */         
/* 1633 */         procedureDef = StringUtils.stripComments(procedureDef, identifierAndStringMarkers, identifierAndStringMarkers, true, false, true, true);
/*      */ 
/*      */         
/* 1636 */         int openParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */         
/* 1640 */         int endOfParamDeclarationIndex = 0;
/*      */         
/* 1642 */         endOfParamDeclarationIndex = endPositionOfParameterDeclaration(openParenIndex, procedureDef, quoteChar);
/*      */ 
/*      */         
/* 1645 */         if (parsingFunction) {
/*      */ 
/*      */ 
/*      */           
/* 1649 */           int returnsIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, " RETURNS ", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1654 */           int endReturnsDef = findEndOfReturnsClause(procedureDef, quoteChar, returnsIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1659 */           int declarationStart = returnsIndex + "RETURNS ".length();
/*      */           
/* 1661 */           while (declarationStart < procedureDef.length() && 
/* 1662 */             Character.isWhitespace(procedureDef.charAt(declarationStart))) {
/* 1663 */             declarationStart++;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1669 */           String returnsDefn = procedureDef.substring(declarationStart, endReturnsDef).trim();
/* 1670 */           TypeDescriptor returnDescriptor = new TypeDescriptor(this, returnsDefn, null);
/*      */ 
/*      */           
/* 1673 */           resultRows.add(convertTypeDescriptorToProcedureRow(procNameAsBytes, "", false, false, true, returnDescriptor, forGetFunctionColumns, 0));
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1678 */         if (openParenIndex == -1 || endOfParamDeclarationIndex == -1)
/*      */         {
/*      */           
/* 1681 */           throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1687 */         parameterDef = procedureDef.substring(openParenIndex + 1, endOfParamDeclarationIndex);
/*      */       } 
/*      */     } finally {
/*      */       
/* 1691 */       SQLException sqlExRethrow = null;
/*      */       
/* 1693 */       if (paramRetrievalRs != null) {
/*      */         try {
/* 1695 */           paramRetrievalRs.close();
/* 1696 */         } catch (SQLException sqlEx) {
/* 1697 */           sqlExRethrow = sqlEx;
/*      */         } 
/*      */         
/* 1700 */         paramRetrievalRs = null;
/*      */       } 
/*      */       
/* 1703 */       if (paramRetrievalStmt != null) {
/*      */         try {
/* 1705 */           paramRetrievalStmt.close();
/* 1706 */         } catch (SQLException sqlEx) {
/* 1707 */           sqlExRethrow = sqlEx;
/*      */         } 
/*      */         
/* 1710 */         paramRetrievalStmt = null;
/*      */       } 
/*      */       
/* 1713 */       if (sqlExRethrow != null) {
/* 1714 */         throw sqlExRethrow;
/*      */       }
/*      */     } 
/*      */     
/* 1718 */     if (parameterDef != null) {
/* 1719 */       int ordinal = 1;
/*      */       
/* 1721 */       List parseList = StringUtils.split(parameterDef, ",", storageDefnDelims, storageDefnClosures, true);
/*      */ 
/*      */       
/* 1724 */       int parseListLen = parseList.size();
/*      */       
/* 1726 */       for (int i = 0; i < parseListLen; i++) {
/* 1727 */         String declaration = parseList.get(i);
/*      */         
/* 1729 */         if (declaration.trim().length() == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1733 */         StringTokenizer declarationTok = new StringTokenizer(declaration, " \t");
/*      */ 
/*      */         
/* 1736 */         String paramName = null;
/* 1737 */         boolean isOutParam = false;
/* 1738 */         boolean isInParam = false;
/*      */         
/* 1740 */         if (declarationTok.hasMoreTokens()) {
/* 1741 */           String possibleParamName = declarationTok.nextToken();
/*      */           
/* 1743 */           if (possibleParamName.equalsIgnoreCase("OUT")) {
/* 1744 */             isOutParam = true;
/*      */             
/* 1746 */             if (declarationTok.hasMoreTokens()) {
/* 1747 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1749 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */           
/*      */           }
/* 1753 */           else if (possibleParamName.equalsIgnoreCase("INOUT")) {
/* 1754 */             isOutParam = true;
/* 1755 */             isInParam = true;
/*      */             
/* 1757 */             if (declarationTok.hasMoreTokens()) {
/* 1758 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1760 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */           
/*      */           }
/* 1764 */           else if (possibleParamName.equalsIgnoreCase("IN")) {
/* 1765 */             isOutParam = false;
/* 1766 */             isInParam = true;
/*      */             
/* 1768 */             if (declarationTok.hasMoreTokens()) {
/* 1769 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1771 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 1776 */             isOutParam = false;
/* 1777 */             isInParam = true;
/*      */             
/* 1779 */             paramName = possibleParamName;
/*      */           } 
/*      */           
/* 1782 */           TypeDescriptor typeDesc = null;
/*      */           
/* 1784 */           if (declarationTok.hasMoreTokens()) {
/* 1785 */             StringBuffer typeInfoBuf = new StringBuffer(declarationTok.nextToken());
/*      */ 
/*      */             
/* 1788 */             while (declarationTok.hasMoreTokens()) {
/* 1789 */               typeInfoBuf.append(" ");
/* 1790 */               typeInfoBuf.append(declarationTok.nextToken());
/*      */             } 
/*      */             
/* 1793 */             String typeInfo = typeInfoBuf.toString();
/*      */             
/* 1795 */             typeDesc = new TypeDescriptor(this, typeInfo, null);
/*      */           } else {
/* 1797 */             throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter type)", "S1000", getExceptionInterceptor());
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1802 */           if ((paramName.startsWith("`") && paramName.endsWith("`")) || (isProcedureInAnsiMode && paramName.startsWith("\"") && paramName.endsWith("\"")))
/*      */           {
/* 1804 */             paramName = paramName.substring(1, paramName.length() - 1);
/*      */           }
/*      */           
/* 1807 */           int wildCompareRes = StringUtils.wildCompare(paramName, parameterNamePattern);
/*      */ 
/*      */           
/* 1810 */           if (wildCompareRes != -1) {
/* 1811 */             ResultSetRow row = convertTypeDescriptorToProcedureRow(procNameAsBytes, paramName, isOutParam, isInParam, false, typeDesc, forGetFunctionColumns, ordinal++);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1816 */             resultRows.add(row);
/*      */           } 
/*      */         } else {
/* 1819 */           throw SQLError.createSQLException("Internal error when parsing callable statement metadata (unknown output from 'SHOW CREATE PROCEDURE')", "S1000", getExceptionInterceptor());
/*      */         } 
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
/*      */   private int endPositionOfParameterDeclaration(int beginIndex, String procedureDef, String quoteChar) throws SQLException {
/* 1848 */     int currentPos = beginIndex + 1;
/* 1849 */     int parenDepth = 1;
/*      */     
/* 1851 */     while (parenDepth > 0 && currentPos < procedureDef.length()) {
/* 1852 */       int closedParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, ")", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */       
/* 1856 */       if (closedParenIndex != -1) {
/* 1857 */         int nextOpenParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1862 */         if (nextOpenParenIndex != -1 && nextOpenParenIndex < closedParenIndex) {
/*      */           
/* 1864 */           parenDepth++;
/* 1865 */           currentPos = closedParenIndex + 1;
/*      */           
/*      */           continue;
/*      */         } 
/* 1869 */         parenDepth--;
/* 1870 */         currentPos = closedParenIndex;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1875 */       throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1882 */     return currentPos;
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
/*      */   private int findEndOfReturnsClause(String procedureDefn, String quoteChar, int positionOfReturnKeyword) throws SQLException {
/* 1907 */     String[] tokens = { "LANGUAGE", "NOT", "DETERMINISTIC", "CONTAINS", "NO", "READ", "MODIFIES", "SQL", "COMMENT", "BEGIN", "RETURN" };
/*      */ 
/*      */ 
/*      */     
/* 1911 */     int startLookingAt = positionOfReturnKeyword + "RETURNS".length() + 1;
/*      */     
/* 1913 */     int endOfReturn = -1;
/*      */     int i;
/* 1915 */     for (i = 0; i < tokens.length; i++) {
/* 1916 */       int nextEndOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, tokens[i], quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */       
/* 1920 */       if (nextEndOfReturn != -1 && (
/* 1921 */         endOfReturn == -1 || nextEndOfReturn < endOfReturn)) {
/* 1922 */         endOfReturn = nextEndOfReturn;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1927 */     if (endOfReturn != -1) {
/* 1928 */       return endOfReturn;
/*      */     }
/*      */ 
/*      */     
/* 1932 */     endOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, ":", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */ 
/*      */ 
/*      */     
/* 1936 */     if (endOfReturn != -1)
/*      */     {
/* 1938 */       for (i = endOfReturn; i > 0; i--) {
/* 1939 */         if (Character.isWhitespace(procedureDefn.charAt(i))) {
/* 1940 */           return i;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1947 */     throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
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
/*      */   private int getCascadeDeleteOption(String cascadeOptions) {
/* 1961 */     int onDeletePos = cascadeOptions.indexOf("ON DELETE");
/*      */     
/* 1963 */     if (onDeletePos != -1) {
/* 1964 */       String deleteOptions = cascadeOptions.substring(onDeletePos, cascadeOptions.length());
/*      */ 
/*      */       
/* 1967 */       if (deleteOptions.startsWith("ON DELETE CASCADE"))
/* 1968 */         return 0; 
/* 1969 */       if (deleteOptions.startsWith("ON DELETE SET NULL"))
/* 1970 */         return 2; 
/* 1971 */       if (deleteOptions.startsWith("ON DELETE RESTRICT"))
/* 1972 */         return 1; 
/* 1973 */       if (deleteOptions.startsWith("ON DELETE NO ACTION")) {
/* 1974 */         return 3;
/*      */       }
/*      */     } 
/*      */     
/* 1978 */     return 3;
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
/*      */   private int getCascadeUpdateOption(String cascadeOptions) {
/* 1990 */     int onUpdatePos = cascadeOptions.indexOf("ON UPDATE");
/*      */     
/* 1992 */     if (onUpdatePos != -1) {
/* 1993 */       String updateOptions = cascadeOptions.substring(onUpdatePos, cascadeOptions.length());
/*      */ 
/*      */       
/* 1996 */       if (updateOptions.startsWith("ON UPDATE CASCADE"))
/* 1997 */         return 0; 
/* 1998 */       if (updateOptions.startsWith("ON UPDATE SET NULL"))
/* 1999 */         return 2; 
/* 2000 */       if (updateOptions.startsWith("ON UPDATE RESTRICT"))
/* 2001 */         return 1; 
/* 2002 */       if (updateOptions.startsWith("ON UPDATE NO ACTION")) {
/* 2003 */         return 3;
/*      */       }
/*      */     } 
/*      */     
/* 2007 */     return 3;
/*      */   }
/*      */ 
/*      */   
/*      */   protected IteratorWithCleanup getCatalogIterator(String catalogSpec) throws SQLException {
/*      */     IteratorWithCleanup allCatalogsIter;
/* 2013 */     if (catalogSpec != null) {
/* 2014 */       if (!catalogSpec.equals("")) {
/* 2015 */         allCatalogsIter = new SingleStringIterator(this, catalogSpec);
/*      */       } else {
/*      */         
/* 2018 */         allCatalogsIter = new SingleStringIterator(this, this.database);
/*      */       } 
/* 2020 */     } else if (this.conn.getNullCatalogMeansCurrent()) {
/* 2021 */       allCatalogsIter = new SingleStringIterator(this, this.database);
/*      */     } else {
/* 2023 */       allCatalogsIter = new ResultSetIterator(this, getCatalogs(), 1);
/*      */     } 
/*      */     
/* 2026 */     return allCatalogsIter;
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
/*      */   public ResultSet getCatalogs() throws SQLException {
/* 2045 */     ResultSet results = null;
/* 2046 */     Statement stmt = null;
/*      */     
/*      */     try {
/* 2049 */       stmt = this.conn.createStatement();
/* 2050 */       stmt.setEscapeProcessing(false);
/* 2051 */       results = stmt.executeQuery("SHOW DATABASES");
/*      */       
/* 2053 */       ResultSetMetaData resultsMD = results.getMetaData();
/* 2054 */       Field[] fields = new Field[1];
/* 2055 */       fields[0] = new Field("", "TABLE_CAT", 12, resultsMD.getColumnDisplaySize(1));
/*      */ 
/*      */       
/* 2058 */       ArrayList tuples = new ArrayList();
/*      */       
/* 2060 */       while (results.next()) {
/* 2061 */         byte[][] rowVal = new byte[1][];
/* 2062 */         rowVal[0] = results.getBytes(1);
/* 2063 */         tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */       } 
/*      */       
/* 2066 */       return buildResultSet(fields, tuples);
/*      */     } finally {
/* 2068 */       if (results != null) {
/*      */         try {
/* 2070 */           results.close();
/* 2071 */         } catch (SQLException sqlEx) {
/* 2072 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         } 
/*      */         
/* 2075 */         results = null;
/*      */       } 
/*      */       
/* 2078 */       if (stmt != null) {
/*      */         try {
/* 2080 */           stmt.close();
/* 2081 */         } catch (SQLException sqlEx) {
/* 2082 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         } 
/*      */         
/* 2085 */         stmt = null;
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
/*      */   public String getCatalogSeparator() throws SQLException {
/* 2098 */     return ".";
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
/*      */   public String getCatalogTerm() throws SQLException {
/* 2115 */     return "database";
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
/* 2156 */     Field[] fields = new Field[8];
/* 2157 */     fields[0] = new Field("", "TABLE_CAT", 1, 64);
/* 2158 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
/* 2159 */     fields[2] = new Field("", "TABLE_NAME", 1, 64);
/* 2160 */     fields[3] = new Field("", "COLUMN_NAME", 1, 64);
/* 2161 */     fields[4] = new Field("", "GRANTOR", 1, 77);
/* 2162 */     fields[5] = new Field("", "GRANTEE", 1, 77);
/* 2163 */     fields[6] = new Field("", "PRIVILEGE", 1, 64);
/* 2164 */     fields[7] = new Field("", "IS_GRANTABLE", 1, 3);
/*      */     
/* 2166 */     StringBuffer grantQuery = new StringBuffer("SELECT c.host, c.db, t.grantor, c.user, c.table_name, c.column_name, c.column_priv from mysql.columns_priv c, mysql.tables_priv t where c.host = t.host and c.db = t.db and c.table_name = t.table_name ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2173 */     if (catalog != null && catalog.length() != 0) {
/* 2174 */       grantQuery.append(" AND c.db='");
/* 2175 */       grantQuery.append(catalog);
/* 2176 */       grantQuery.append("' ");
/*      */     } 
/*      */ 
/*      */     
/* 2180 */     grantQuery.append(" AND c.table_name ='");
/* 2181 */     grantQuery.append(table);
/* 2182 */     grantQuery.append("' AND c.column_name like '");
/* 2183 */     grantQuery.append(columnNamePattern);
/* 2184 */     grantQuery.append("'");
/*      */     
/* 2186 */     Statement stmt = null;
/* 2187 */     ResultSet results = null;
/* 2188 */     ArrayList grantRows = new ArrayList();
/*      */     
/*      */     try {
/* 2191 */       stmt = this.conn.createStatement();
/* 2192 */       stmt.setEscapeProcessing(false);
/* 2193 */       results = stmt.executeQuery(grantQuery.toString());
/*      */       
/* 2195 */       while (results.next()) {
/* 2196 */         String host = results.getString(1);
/* 2197 */         String db = results.getString(2);
/* 2198 */         String grantor = results.getString(3);
/* 2199 */         String user = results.getString(4);
/*      */         
/* 2201 */         if (user == null || user.length() == 0) {
/* 2202 */           user = "%";
/*      */         }
/*      */         
/* 2205 */         StringBuffer fullUser = new StringBuffer(user);
/*      */         
/* 2207 */         if (host != null && this.conn.getUseHostsInPrivileges()) {
/* 2208 */           fullUser.append("@");
/* 2209 */           fullUser.append(host);
/*      */         } 
/*      */         
/* 2212 */         String columnName = results.getString(6);
/* 2213 */         String allPrivileges = results.getString(7);
/*      */         
/* 2215 */         if (allPrivileges != null) {
/* 2216 */           allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
/*      */           
/* 2218 */           StringTokenizer st = new StringTokenizer(allPrivileges, ",");
/*      */           
/* 2220 */           while (st.hasMoreTokens()) {
/* 2221 */             String privilege = st.nextToken().trim();
/* 2222 */             byte[][] tuple = new byte[8][];
/* 2223 */             tuple[0] = s2b(db);
/* 2224 */             tuple[1] = null;
/* 2225 */             tuple[2] = s2b(table);
/* 2226 */             tuple[3] = s2b(columnName);
/*      */             
/* 2228 */             if (grantor != null) {
/* 2229 */               tuple[4] = s2b(grantor);
/*      */             } else {
/* 2231 */               tuple[4] = null;
/*      */             } 
/*      */             
/* 2234 */             tuple[5] = s2b(fullUser.toString());
/* 2235 */             tuple[6] = s2b(privilege);
/* 2236 */             tuple[7] = null;
/* 2237 */             grantRows.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 2242 */       if (results != null) {
/*      */         try {
/* 2244 */           results.close();
/* 2245 */         } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */         
/* 2249 */         results = null;
/*      */       } 
/*      */       
/* 2252 */       if (stmt != null) {
/*      */         try {
/* 2254 */           stmt.close();
/* 2255 */         } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */         
/* 2259 */         stmt = null;
/*      */       } 
/*      */     } 
/*      */     
/* 2263 */     return buildResultSet(fields, grantRows);
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
/*      */   public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 2327 */     if (columnNamePattern == null) {
/* 2328 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 2329 */         columnNamePattern = "%";
/*      */       } else {
/* 2331 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2337 */     String colPattern = columnNamePattern;
/*      */     
/* 2339 */     Field[] fields = createColumnsFields();
/*      */     
/* 2341 */     ArrayList rows = new ArrayList();
/* 2342 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */     
/*      */     try {
/* 2346 */       (new IterateBlock(this, getCatalogIterator(catalog), tableNamePattern, schemaPattern, colPattern, stmt, rows) { private final String val$tableNamePattern; private final String val$schemaPattern; private final String val$colPattern; private final Statement val$stmt; private final ArrayList val$rows; private final DatabaseMetaData this$0; void forEach(Object catalogStr) throws SQLException { // Byte code:
/*      */             //   0: new java/util/ArrayList
/*      */             //   3: dup
/*      */             //   4: invokespecial <init> : ()V
/*      */             //   7: astore_2
/*      */             //   8: aload_0
/*      */             //   9: getfield val$tableNamePattern : Ljava/lang/String;
/*      */             //   12: ifnonnull -> 111
/*      */             //   15: aconst_null
/*      */             //   16: astore_3
/*      */             //   17: aload_0
/*      */             //   18: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   21: aload_1
/*      */             //   22: checkcast java/lang/String
/*      */             //   25: aload_0
/*      */             //   26: getfield val$schemaPattern : Ljava/lang/String;
/*      */             //   29: ldc '%'
/*      */             //   31: iconst_0
/*      */             //   32: anewarray java/lang/String
/*      */             //   35: invokevirtual getTables : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */             //   38: astore_3
/*      */             //   39: aload_3
/*      */             //   40: invokeinterface next : ()Z
/*      */             //   45: ifeq -> 68
/*      */             //   48: aload_3
/*      */             //   49: ldc 'TABLE_NAME'
/*      */             //   51: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   56: astore #4
/*      */             //   58: aload_2
/*      */             //   59: aload #4
/*      */             //   61: invokevirtual add : (Ljava/lang/Object;)Z
/*      */             //   64: pop
/*      */             //   65: goto -> 39
/*      */             //   68: jsr -> 82
/*      */             //   71: goto -> 108
/*      */             //   74: astore #5
/*      */             //   76: jsr -> 82
/*      */             //   79: aload #5
/*      */             //   81: athrow
/*      */             //   82: astore #6
/*      */             //   84: aload_3
/*      */             //   85: ifnull -> 106
/*      */             //   88: aload_3
/*      */             //   89: invokeinterface close : ()V
/*      */             //   94: goto -> 104
/*      */             //   97: astore #7
/*      */             //   99: aload #7
/*      */             //   101: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
/*      */             //   104: aconst_null
/*      */             //   105: astore_3
/*      */             //   106: ret #6
/*      */             //   108: goto -> 206
/*      */             //   111: aconst_null
/*      */             //   112: astore_3
/*      */             //   113: aload_0
/*      */             //   114: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   117: aload_1
/*      */             //   118: checkcast java/lang/String
/*      */             //   121: aload_0
/*      */             //   122: getfield val$schemaPattern : Ljava/lang/String;
/*      */             //   125: aload_0
/*      */             //   126: getfield val$tableNamePattern : Ljava/lang/String;
/*      */             //   129: iconst_0
/*      */             //   130: anewarray java/lang/String
/*      */             //   133: invokevirtual getTables : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */             //   136: astore_3
/*      */             //   137: aload_3
/*      */             //   138: invokeinterface next : ()Z
/*      */             //   143: ifeq -> 166
/*      */             //   146: aload_3
/*      */             //   147: ldc 'TABLE_NAME'
/*      */             //   149: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   154: astore #4
/*      */             //   156: aload_2
/*      */             //   157: aload #4
/*      */             //   159: invokevirtual add : (Ljava/lang/Object;)Z
/*      */             //   162: pop
/*      */             //   163: goto -> 137
/*      */             //   166: jsr -> 180
/*      */             //   169: goto -> 206
/*      */             //   172: astore #8
/*      */             //   174: jsr -> 180
/*      */             //   177: aload #8
/*      */             //   179: athrow
/*      */             //   180: astore #9
/*      */             //   182: aload_3
/*      */             //   183: ifnull -> 204
/*      */             //   186: aload_3
/*      */             //   187: invokeinterface close : ()V
/*      */             //   192: goto -> 202
/*      */             //   195: astore #10
/*      */             //   197: aload #10
/*      */             //   199: invokestatic shouldNotHappen : (Ljava/lang/Exception;)V
/*      */             //   202: aconst_null
/*      */             //   203: astore_3
/*      */             //   204: ret #9
/*      */             //   206: aload_2
/*      */             //   207: invokevirtual iterator : ()Ljava/util/Iterator;
/*      */             //   210: astore_3
/*      */             //   211: aload_3
/*      */             //   212: invokeinterface hasNext : ()Z
/*      */             //   217: ifeq -> 1290
/*      */             //   220: aload_3
/*      */             //   221: invokeinterface next : ()Ljava/lang/Object;
/*      */             //   226: checkcast java/lang/String
/*      */             //   229: astore #4
/*      */             //   231: aconst_null
/*      */             //   232: astore #5
/*      */             //   234: new java/lang/StringBuffer
/*      */             //   237: dup
/*      */             //   238: ldc 'SHOW '
/*      */             //   240: invokespecial <init> : (Ljava/lang/String;)V
/*      */             //   243: astore #6
/*      */             //   245: aload_0
/*      */             //   246: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   249: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */             //   252: iconst_4
/*      */             //   253: iconst_1
/*      */             //   254: iconst_0
/*      */             //   255: invokevirtual versionMeetsMinimum : (III)Z
/*      */             //   258: ifeq -> 269
/*      */             //   261: aload #6
/*      */             //   263: ldc 'FULL '
/*      */             //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   268: pop
/*      */             //   269: aload #6
/*      */             //   271: ldc 'COLUMNS FROM '
/*      */             //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   276: pop
/*      */             //   277: aload #6
/*      */             //   279: aload_0
/*      */             //   280: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   283: getfield quotedId : Ljava/lang/String;
/*      */             //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   289: pop
/*      */             //   290: aload #6
/*      */             //   292: aload #4
/*      */             //   294: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   297: pop
/*      */             //   298: aload #6
/*      */             //   300: aload_0
/*      */             //   301: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   304: getfield quotedId : Ljava/lang/String;
/*      */             //   307: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   310: pop
/*      */             //   311: aload #6
/*      */             //   313: ldc ' FROM '
/*      */             //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   318: pop
/*      */             //   319: aload #6
/*      */             //   321: aload_0
/*      */             //   322: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   325: getfield quotedId : Ljava/lang/String;
/*      */             //   328: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   331: pop
/*      */             //   332: aload #6
/*      */             //   334: aload_1
/*      */             //   335: checkcast java/lang/String
/*      */             //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   341: pop
/*      */             //   342: aload #6
/*      */             //   344: aload_0
/*      */             //   345: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   348: getfield quotedId : Ljava/lang/String;
/*      */             //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   354: pop
/*      */             //   355: aload #6
/*      */             //   357: ldc ' LIKE ''
/*      */             //   359: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   362: pop
/*      */             //   363: aload #6
/*      */             //   365: aload_0
/*      */             //   366: getfield val$colPattern : Ljava/lang/String;
/*      */             //   369: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   372: pop
/*      */             //   373: aload #6
/*      */             //   375: ldc '''
/*      */             //   377: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   380: pop
/*      */             //   381: iconst_0
/*      */             //   382: istore #7
/*      */             //   384: aconst_null
/*      */             //   385: astore #8
/*      */             //   387: aload_0
/*      */             //   388: getfield val$colPattern : Ljava/lang/String;
/*      */             //   391: ldc '%'
/*      */             //   393: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */             //   396: ifne -> 593
/*      */             //   399: iconst_1
/*      */             //   400: istore #7
/*      */             //   402: new java/lang/StringBuffer
/*      */             //   405: dup
/*      */             //   406: ldc 'SHOW '
/*      */             //   408: invokespecial <init> : (Ljava/lang/String;)V
/*      */             //   411: astore #9
/*      */             //   413: aload_0
/*      */             //   414: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   417: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */             //   420: iconst_4
/*      */             //   421: iconst_1
/*      */             //   422: iconst_0
/*      */             //   423: invokevirtual versionMeetsMinimum : (III)Z
/*      */             //   426: ifeq -> 437
/*      */             //   429: aload #9
/*      */             //   431: ldc 'FULL '
/*      */             //   433: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   436: pop
/*      */             //   437: aload #9
/*      */             //   439: ldc 'COLUMNS FROM '
/*      */             //   441: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   444: pop
/*      */             //   445: aload #9
/*      */             //   447: aload_0
/*      */             //   448: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   451: getfield quotedId : Ljava/lang/String;
/*      */             //   454: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   457: pop
/*      */             //   458: aload #9
/*      */             //   460: aload #4
/*      */             //   462: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   465: pop
/*      */             //   466: aload #9
/*      */             //   468: aload_0
/*      */             //   469: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   472: getfield quotedId : Ljava/lang/String;
/*      */             //   475: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   478: pop
/*      */             //   479: aload #9
/*      */             //   481: ldc ' FROM '
/*      */             //   483: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   486: pop
/*      */             //   487: aload #9
/*      */             //   489: aload_0
/*      */             //   490: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   493: getfield quotedId : Ljava/lang/String;
/*      */             //   496: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   499: pop
/*      */             //   500: aload #9
/*      */             //   502: aload_1
/*      */             //   503: checkcast java/lang/String
/*      */             //   506: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   509: pop
/*      */             //   510: aload #9
/*      */             //   512: aload_0
/*      */             //   513: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   516: getfield quotedId : Ljava/lang/String;
/*      */             //   519: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */             //   522: pop
/*      */             //   523: aload_0
/*      */             //   524: getfield val$stmt : Ljava/sql/Statement;
/*      */             //   527: aload #9
/*      */             //   529: invokevirtual toString : ()Ljava/lang/String;
/*      */             //   532: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */             //   537: astore #5
/*      */             //   539: new java/util/HashMap
/*      */             //   542: dup
/*      */             //   543: invokespecial <init> : ()V
/*      */             //   546: astore #8
/*      */             //   548: iconst_1
/*      */             //   549: istore #10
/*      */             //   551: aload #5
/*      */             //   553: invokeinterface next : ()Z
/*      */             //   558: ifeq -> 593
/*      */             //   561: aload #5
/*      */             //   563: ldc 'Field'
/*      */             //   565: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   570: astore #11
/*      */             //   572: aload #8
/*      */             //   574: aload #11
/*      */             //   576: iload #10
/*      */             //   578: iinc #10, 1
/*      */             //   581: invokestatic integerValueOf : (I)Ljava/lang/Integer;
/*      */             //   584: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*      */             //   589: pop
/*      */             //   590: goto -> 551
/*      */             //   593: aload_0
/*      */             //   594: getfield val$stmt : Ljava/sql/Statement;
/*      */             //   597: aload #6
/*      */             //   599: invokevirtual toString : ()Ljava/lang/String;
/*      */             //   602: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */             //   607: astore #5
/*      */             //   609: iconst_1
/*      */             //   610: istore #9
/*      */             //   612: aload #5
/*      */             //   614: invokeinterface next : ()Z
/*      */             //   619: ifeq -> 1249
/*      */             //   622: bipush #23
/*      */             //   624: anewarray [B
/*      */             //   627: astore #10
/*      */             //   629: aload #10
/*      */             //   631: iconst_0
/*      */             //   632: aload_0
/*      */             //   633: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   636: aload_1
/*      */             //   637: checkcast java/lang/String
/*      */             //   640: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   643: aastore
/*      */             //   644: aload #10
/*      */             //   646: iconst_1
/*      */             //   647: aconst_null
/*      */             //   648: aastore
/*      */             //   649: aload #10
/*      */             //   651: iconst_2
/*      */             //   652: aload_0
/*      */             //   653: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   656: aload #4
/*      */             //   658: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   661: aastore
/*      */             //   662: aload #10
/*      */             //   664: iconst_3
/*      */             //   665: aload #5
/*      */             //   667: ldc 'Field'
/*      */             //   669: invokeinterface getBytes : (Ljava/lang/String;)[B
/*      */             //   674: aastore
/*      */             //   675: new com/mysql/jdbc/DatabaseMetaData$TypeDescriptor
/*      */             //   678: dup
/*      */             //   679: aload_0
/*      */             //   680: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   683: aload #5
/*      */             //   685: ldc 'Type'
/*      */             //   687: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   692: aload #5
/*      */             //   694: ldc 'Null'
/*      */             //   696: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   701: invokespecial <init> : (Lcom/mysql/jdbc/DatabaseMetaData;Ljava/lang/String;Ljava/lang/String;)V
/*      */             //   704: astore #11
/*      */             //   706: aload #10
/*      */             //   708: iconst_4
/*      */             //   709: aload #11
/*      */             //   711: getfield dataType : S
/*      */             //   714: invokestatic toString : (S)Ljava/lang/String;
/*      */             //   717: invokevirtual getBytes : ()[B
/*      */             //   720: aastore
/*      */             //   721: aload #10
/*      */             //   723: iconst_5
/*      */             //   724: aload_0
/*      */             //   725: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   728: aload #11
/*      */             //   730: getfield typeName : Ljava/lang/String;
/*      */             //   733: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   736: aastore
/*      */             //   737: aload #10
/*      */             //   739: bipush #6
/*      */             //   741: aload #11
/*      */             //   743: getfield columnSize : Ljava/lang/Integer;
/*      */             //   746: ifnonnull -> 753
/*      */             //   749: aconst_null
/*      */             //   750: goto -> 768
/*      */             //   753: aload_0
/*      */             //   754: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   757: aload #11
/*      */             //   759: getfield columnSize : Ljava/lang/Integer;
/*      */             //   762: invokevirtual toString : ()Ljava/lang/String;
/*      */             //   765: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   768: aastore
/*      */             //   769: aload #10
/*      */             //   771: bipush #7
/*      */             //   773: aload_0
/*      */             //   774: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   777: aload #11
/*      */             //   779: getfield bufferLength : I
/*      */             //   782: invokestatic toString : (I)Ljava/lang/String;
/*      */             //   785: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   788: aastore
/*      */             //   789: aload #10
/*      */             //   791: bipush #8
/*      */             //   793: aload #11
/*      */             //   795: getfield decimalDigits : Ljava/lang/Integer;
/*      */             //   798: ifnonnull -> 805
/*      */             //   801: aconst_null
/*      */             //   802: goto -> 820
/*      */             //   805: aload_0
/*      */             //   806: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   809: aload #11
/*      */             //   811: getfield decimalDigits : Ljava/lang/Integer;
/*      */             //   814: invokevirtual toString : ()Ljava/lang/String;
/*      */             //   817: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   820: aastore
/*      */             //   821: aload #10
/*      */             //   823: bipush #9
/*      */             //   825: aload_0
/*      */             //   826: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   829: aload #11
/*      */             //   831: getfield numPrecRadix : I
/*      */             //   834: invokestatic toString : (I)Ljava/lang/String;
/*      */             //   837: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   840: aastore
/*      */             //   841: aload #10
/*      */             //   843: bipush #10
/*      */             //   845: aload_0
/*      */             //   846: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   849: aload #11
/*      */             //   851: getfield nullability : I
/*      */             //   854: invokestatic toString : (I)Ljava/lang/String;
/*      */             //   857: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   860: aastore
/*      */             //   861: aload_0
/*      */             //   862: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   865: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */             //   868: iconst_4
/*      */             //   869: iconst_1
/*      */             //   870: iconst_0
/*      */             //   871: invokevirtual versionMeetsMinimum : (III)Z
/*      */             //   874: ifeq -> 894
/*      */             //   877: aload #10
/*      */             //   879: bipush #11
/*      */             //   881: aload #5
/*      */             //   883: ldc 'Comment'
/*      */             //   885: invokeinterface getBytes : (Ljava/lang/String;)[B
/*      */             //   890: aastore
/*      */             //   891: goto -> 908
/*      */             //   894: aload #10
/*      */             //   896: bipush #11
/*      */             //   898: aload #5
/*      */             //   900: ldc 'Extra'
/*      */             //   902: invokeinterface getBytes : (Ljava/lang/String;)[B
/*      */             //   907: aastore
/*      */             //   908: goto -> 921
/*      */             //   911: astore #12
/*      */             //   913: aload #10
/*      */             //   915: bipush #11
/*      */             //   917: iconst_0
/*      */             //   918: newarray byte
/*      */             //   920: aastore
/*      */             //   921: aload #10
/*      */             //   923: bipush #12
/*      */             //   925: aload #5
/*      */             //   927: ldc 'Default'
/*      */             //   929: invokeinterface getBytes : (Ljava/lang/String;)[B
/*      */             //   934: aastore
/*      */             //   935: aload #10
/*      */             //   937: bipush #13
/*      */             //   939: iconst_1
/*      */             //   940: newarray byte
/*      */             //   942: dup
/*      */             //   943: iconst_0
/*      */             //   944: bipush #48
/*      */             //   946: bastore
/*      */             //   947: aastore
/*      */             //   948: aload #10
/*      */             //   950: bipush #14
/*      */             //   952: iconst_1
/*      */             //   953: newarray byte
/*      */             //   955: dup
/*      */             //   956: iconst_0
/*      */             //   957: bipush #48
/*      */             //   959: bastore
/*      */             //   960: aastore
/*      */             //   961: aload #11
/*      */             //   963: getfield typeName : Ljava/lang/String;
/*      */             //   966: ldc 'CHAR'
/*      */             //   968: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
/*      */             //   971: iconst_m1
/*      */             //   972: if_icmpne -> 1017
/*      */             //   975: aload #11
/*      */             //   977: getfield typeName : Ljava/lang/String;
/*      */             //   980: ldc 'BLOB'
/*      */             //   982: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
/*      */             //   985: iconst_m1
/*      */             //   986: if_icmpne -> 1017
/*      */             //   989: aload #11
/*      */             //   991: getfield typeName : Ljava/lang/String;
/*      */             //   994: ldc 'TEXT'
/*      */             //   996: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
/*      */             //   999: iconst_m1
/*      */             //   1000: if_icmpne -> 1017
/*      */             //   1003: aload #11
/*      */             //   1005: getfield typeName : Ljava/lang/String;
/*      */             //   1008: ldc 'BINARY'
/*      */             //   1010: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
/*      */             //   1013: iconst_m1
/*      */             //   1014: if_icmpeq -> 1030
/*      */             //   1017: aload #10
/*      */             //   1019: bipush #15
/*      */             //   1021: aload #10
/*      */             //   1023: bipush #6
/*      */             //   1025: aaload
/*      */             //   1026: aastore
/*      */             //   1027: goto -> 1036
/*      */             //   1030: aload #10
/*      */             //   1032: bipush #15
/*      */             //   1034: aconst_null
/*      */             //   1035: aastore
/*      */             //   1036: iload #7
/*      */             //   1038: ifne -> 1060
/*      */             //   1041: aload #10
/*      */             //   1043: bipush #16
/*      */             //   1045: iload #9
/*      */             //   1047: iinc #9, 1
/*      */             //   1050: invokestatic toString : (I)Ljava/lang/String;
/*      */             //   1053: invokevirtual getBytes : ()[B
/*      */             //   1056: aastore
/*      */             //   1057: goto -> 1121
/*      */             //   1060: aload #5
/*      */             //   1062: ldc 'Field'
/*      */             //   1064: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   1069: astore #12
/*      */             //   1071: aload #8
/*      */             //   1073: aload #12
/*      */             //   1075: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */             //   1080: checkcast java/lang/Integer
/*      */             //   1083: astore #13
/*      */             //   1085: aload #13
/*      */             //   1087: ifnull -> 1106
/*      */             //   1090: aload #10
/*      */             //   1092: bipush #16
/*      */             //   1094: aload #13
/*      */             //   1096: invokevirtual toString : ()Ljava/lang/String;
/*      */             //   1099: invokevirtual getBytes : ()[B
/*      */             //   1102: aastore
/*      */             //   1103: goto -> 1121
/*      */             //   1106: ldc 'Can not find column in full column list to determine true ordinal position.'
/*      */             //   1108: ldc 'S1000'
/*      */             //   1110: aload_0
/*      */             //   1111: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   1114: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */             //   1117: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */             //   1120: athrow
/*      */             //   1121: aload #10
/*      */             //   1123: bipush #17
/*      */             //   1125: aload_0
/*      */             //   1126: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   1129: aload #11
/*      */             //   1131: getfield isNullable : Ljava/lang/String;
/*      */             //   1134: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   1137: aastore
/*      */             //   1138: aload #10
/*      */             //   1140: bipush #18
/*      */             //   1142: aconst_null
/*      */             //   1143: aastore
/*      */             //   1144: aload #10
/*      */             //   1146: bipush #19
/*      */             //   1148: aconst_null
/*      */             //   1149: aastore
/*      */             //   1150: aload #10
/*      */             //   1152: bipush #20
/*      */             //   1154: aconst_null
/*      */             //   1155: aastore
/*      */             //   1156: aload #10
/*      */             //   1158: bipush #21
/*      */             //   1160: aconst_null
/*      */             //   1161: aastore
/*      */             //   1162: aload #10
/*      */             //   1164: bipush #22
/*      */             //   1166: aload_0
/*      */             //   1167: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   1170: ldc ''
/*      */             //   1172: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   1175: aastore
/*      */             //   1176: aload #5
/*      */             //   1178: ldc 'Extra'
/*      */             //   1180: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */             //   1185: astore #12
/*      */             //   1187: aload #12
/*      */             //   1189: ifnull -> 1222
/*      */             //   1192: aload #10
/*      */             //   1194: bipush #22
/*      */             //   1196: aload_0
/*      */             //   1197: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   1200: aload #12
/*      */             //   1202: ldc 'auto_increment'
/*      */             //   1204: invokestatic indexOfIgnoreCase : (Ljava/lang/String;Ljava/lang/String;)I
/*      */             //   1207: iconst_m1
/*      */             //   1208: if_icmpeq -> 1216
/*      */             //   1211: ldc 'YES'
/*      */             //   1213: goto -> 1218
/*      */             //   1216: ldc 'NO'
/*      */             //   1218: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */             //   1221: aastore
/*      */             //   1222: aload_0
/*      */             //   1223: getfield val$rows : Ljava/util/ArrayList;
/*      */             //   1226: new com/mysql/jdbc/ByteArrayRow
/*      */             //   1229: dup
/*      */             //   1230: aload #10
/*      */             //   1232: aload_0
/*      */             //   1233: getfield this$0 : Lcom/mysql/jdbc/DatabaseMetaData;
/*      */             //   1236: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */             //   1239: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
/*      */             //   1242: invokevirtual add : (Ljava/lang/Object;)Z
/*      */             //   1245: pop
/*      */             //   1246: goto -> 612
/*      */             //   1249: jsr -> 1263
/*      */             //   1252: goto -> 1287
/*      */             //   1255: astore #14
/*      */             //   1257: jsr -> 1263
/*      */             //   1260: aload #14
/*      */             //   1262: athrow
/*      */             //   1263: astore #15
/*      */             //   1265: aload #5
/*      */             //   1267: ifnull -> 1285
/*      */             //   1270: aload #5
/*      */             //   1272: invokeinterface close : ()V
/*      */             //   1277: goto -> 1282
/*      */             //   1280: astore #16
/*      */             //   1282: aconst_null
/*      */             //   1283: astore #5
/*      */             //   1285: ret #15
/*      */             //   1287: goto -> 211
/*      */             //   1290: return
/*      */             // Line number table:
/*      */             //   Java source line number -> byte code offset
/*      */             //   #2349	-> 0
/*      */             //   #2351	-> 8
/*      */             //   #2353	-> 15
/*      */             //   #2356	-> 17
/*      */             //   #2359	-> 39
/*      */             //   #2360	-> 48
/*      */             //   #2362	-> 58
/*      */             //   #2364	-> 68
/*      */             //   #2375	-> 71
/*      */             //   #2365	-> 74
/*      */             //   #2367	-> 88
/*      */             //   #2371	-> 94
/*      */             //   #2368	-> 97
/*      */             //   #2369	-> 99
/*      */             //   #2373	-> 104
/*      */             //   #2377	-> 111
/*      */             //   #2380	-> 113
/*      */             //   #2383	-> 137
/*      */             //   #2384	-> 146
/*      */             //   #2386	-> 156
/*      */             //   #2388	-> 166
/*      */             //   #2399	-> 169
/*      */             //   #2389	-> 172
/*      */             //   #2391	-> 186
/*      */             //   #2395	-> 192
/*      */             //   #2392	-> 195
/*      */             //   #2393	-> 197
/*      */             //   #2397	-> 202
/*      */             //   #2402	-> 206
/*      */             //   #2404	-> 211
/*      */             //   #2405	-> 220
/*      */             //   #2407	-> 231
/*      */             //   #2410	-> 234
/*      */             //   #2412	-> 245
/*      */             //   #2413	-> 261
/*      */             //   #2416	-> 269
/*      */             //   #2417	-> 277
/*      */             //   #2418	-> 290
/*      */             //   #2419	-> 298
/*      */             //   #2420	-> 311
/*      */             //   #2421	-> 319
/*      */             //   #2422	-> 332
/*      */             //   #2423	-> 342
/*      */             //   #2424	-> 355
/*      */             //   #2425	-> 363
/*      */             //   #2426	-> 373
/*      */             //   #2433	-> 381
/*      */             //   #2434	-> 384
/*      */             //   #2436	-> 387
/*      */             //   #2437	-> 399
/*      */             //   #2439	-> 402
/*      */             //   #2442	-> 413
/*      */             //   #2443	-> 429
/*      */             //   #2446	-> 437
/*      */             //   #2447	-> 445
/*      */             //   #2448	-> 458
/*      */             //   #2449	-> 466
/*      */             //   #2450	-> 479
/*      */             //   #2451	-> 487
/*      */             //   #2452	-> 500
/*      */             //   #2454	-> 510
/*      */             //   #2456	-> 523
/*      */             //   #2459	-> 539
/*      */             //   #2461	-> 548
/*      */             //   #2463	-> 551
/*      */             //   #2464	-> 561
/*      */             //   #2467	-> 572
/*      */             //   #2472	-> 593
/*      */             //   #2474	-> 609
/*      */             //   #2476	-> 612
/*      */             //   #2477	-> 622
/*      */             //   #2478	-> 629
/*      */             //   #2479	-> 644
/*      */             //   #2482	-> 649
/*      */             //   #2483	-> 662
/*      */             //   #2485	-> 675
/*      */             //   #2489	-> 706
/*      */             //   #2493	-> 721
/*      */             //   #2495	-> 737
/*      */             //   #2496	-> 769
/*      */             //   #2497	-> 789
/*      */             //   #2498	-> 821
/*      */             //   #2500	-> 841
/*      */             //   #2511	-> 861
/*      */             //   #2512	-> 877
/*      */             //   #2515	-> 894
/*      */             //   #2519	-> 908
/*      */             //   #2517	-> 911
/*      */             //   #2518	-> 913
/*      */             //   #2522	-> 921
/*      */             //   #2524	-> 935
/*      */             //   #2525	-> 948
/*      */             //   #2527	-> 961
/*      */             //   #2531	-> 1017
/*      */             //   #2533	-> 1030
/*      */             //   #2537	-> 1036
/*      */             //   #2538	-> 1041
/*      */             //   #2541	-> 1060
/*      */             //   #2543	-> 1071
/*      */             //   #2546	-> 1085
/*      */             //   #2547	-> 1090
/*      */             //   #2550	-> 1106
/*      */             //   #2556	-> 1121
/*      */             //   #2559	-> 1138
/*      */             //   #2560	-> 1144
/*      */             //   #2561	-> 1150
/*      */             //   #2562	-> 1156
/*      */             //   #2564	-> 1162
/*      */             //   #2566	-> 1176
/*      */             //   #2568	-> 1187
/*      */             //   #2569	-> 1192
/*      */             //   #2575	-> 1222
/*      */             //   #2577	-> 1249
/*      */             //   #2587	-> 1252
/*      */             //   #2578	-> 1255
/*      */             //   #2580	-> 1270
/*      */             //   #2583	-> 1277
/*      */             //   #2581	-> 1280
/*      */             //   #2585	-> 1282
/*      */             //   #2589	-> 1290
/*      */             // Local variable table:
/*      */             //   start	length	slot	name	descriptor
/*      */             //   58	7	4	tableNameFromList	Ljava/lang/String;
/*      */             //   99	5	7	sqlEx	Ljava/lang/Exception;
/*      */             //   17	91	3	tables	Ljava/sql/ResultSet;
/*      */             //   156	7	4	tableNameFromList	Ljava/lang/String;
/*      */             //   197	5	10	sqlEx	Ljava/sql/SQLException;
/*      */             //   113	93	3	tables	Ljava/sql/ResultSet;
/*      */             //   572	18	11	fullOrdColName	Ljava/lang/String;
/*      */             //   413	180	9	fullColumnQueryBuf	Ljava/lang/StringBuffer;
/*      */             //   551	42	10	fullOrdinalPos	I
/*      */             //   913	8	12	E	Ljava/lang/Exception;
/*      */             //   1071	50	12	origColName	Ljava/lang/String;
/*      */             //   1085	36	13	realOrdinal	Ljava/lang/Integer;
/*      */             //   629	617	10	rowVal	[[B
/*      */             //   706	540	11	typeDesc	Lcom/mysql/jdbc/DatabaseMetaData$TypeDescriptor;
/*      */             //   1187	59	12	extra	Ljava/lang/String;
/*      */             //   245	1004	6	queryBuf	Ljava/lang/StringBuffer;
/*      */             //   384	865	7	fixUpOrdinalsRequired	Z
/*      */             //   387	862	8	ordinalFixUpMap	Ljava/util/Map;
/*      */             //   612	637	9	ordPos	I
/*      */             //   1282	0	16	ex	Ljava/lang/Exception;
/*      */             //   231	1056	4	tableName	Ljava/lang/String;
/*      */             //   234	1053	5	results	Ljava/sql/ResultSet;
/*      */             //   0	1291	0	this	Lcom/mysql/jdbc/DatabaseMetaData$2;
/*      */             //   0	1291	1	catalogStr	Ljava/lang/Object;
/*      */             //   8	1283	2	tableNameList	Ljava/util/ArrayList;
/*      */             //   211	1080	3	tableNames	Ljava/util/Iterator;
/*      */             // Exception table:
/*      */             //   from	to	target	type
/*      */             //   17	71	74	finally
/*      */             //   74	79	74	finally
/*      */             //   88	94	97	java/lang/Exception
/*      */             //   113	169	172	finally
/*      */             //   172	177	172	finally
/*      */             //   186	192	195	java/sql/SQLException
/*      */             //   234	1252	1255	finally
/*      */             //   861	908	911	java/lang/Exception
/*      */             //   1255	1260	1255	finally
/* 2346 */             //   1270	1277	1280	java/lang/Exception } }).doForAll();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2592 */       if (stmt != null) {
/* 2593 */         stmt.close();
/*      */       }
/*      */     } 
/*      */     
/* 2597 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 2599 */     return results;
/*      */   }
/*      */   
/*      */   protected Field[] createColumnsFields() {
/* 2603 */     Field[] fields = new Field[23];
/* 2604 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 2605 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 2606 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 2607 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 2608 */     fields[4] = new Field("", "DATA_TYPE", 4, 5);
/* 2609 */     fields[5] = new Field("", "TYPE_NAME", 1, 16);
/* 2610 */     fields[6] = new Field("", "COLUMN_SIZE", 4, Integer.toString(2147483647).length());
/*      */     
/* 2612 */     fields[7] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 2613 */     fields[8] = new Field("", "DECIMAL_DIGITS", 4, 10);
/* 2614 */     fields[9] = new Field("", "NUM_PREC_RADIX", 4, 10);
/* 2615 */     fields[10] = new Field("", "NULLABLE", 4, 10);
/* 2616 */     fields[11] = new Field("", "REMARKS", 1, 0);
/* 2617 */     fields[12] = new Field("", "COLUMN_DEF", 1, 0);
/* 2618 */     fields[13] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 2619 */     fields[14] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 2620 */     fields[15] = new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(2147483647).length());
/*      */     
/* 2622 */     fields[16] = new Field("", "ORDINAL_POSITION", 4, 10);
/* 2623 */     fields[17] = new Field("", "IS_NULLABLE", 1, 3);
/* 2624 */     fields[18] = new Field("", "SCOPE_CATALOG", 1, 255);
/* 2625 */     fields[19] = new Field("", "SCOPE_SCHEMA", 1, 255);
/* 2626 */     fields[20] = new Field("", "SCOPE_TABLE", 1, 255);
/* 2627 */     fields[21] = new Field("", "SOURCE_DATA_TYPE", 5, 10);
/* 2628 */     fields[22] = new Field("", "IS_AUTOINCREMENT", 1, 3);
/* 2629 */     return fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection getConnection() throws SQLException {
/* 2640 */     return this.conn;
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
/*      */   public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
/* 2714 */     if (primaryTable == null) {
/* 2715 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 2719 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 2721 */     ArrayList tuples = new ArrayList();
/*      */     
/* 2723 */     if (this.conn.versionMeetsMinimum(3, 23, 0)) {
/*      */       
/* 2725 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */       
/*      */       try {
/* 2729 */         (new IterateBlock(this, getCatalogIterator(foreignCatalog), stmt, foreignTable, primaryTable, foreignCatalog, foreignSchema, primaryCatalog, primarySchema, tuples) { private final Statement val$stmt; private final String val$foreignTable; private final String val$primaryTable; private final String val$foreignCatalog; private final String val$foreignSchema; private final String val$primaryCatalog; private final String val$primarySchema; private final ArrayList val$tuples; private final DatabaseMetaData this$0;
/*      */             
/*      */             void forEach(Object catalogStr) throws SQLException {
/* 2732 */               ResultSet fkresults = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 2739 */                 if (this.this$0.conn.versionMeetsMinimum(3, 23, 50)) {
/* 2740 */                   fkresults = this.this$0.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
/*      */                 } else {
/*      */                   
/* 2743 */                   StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                   
/* 2745 */                   queryBuf.append(this.this$0.quotedId);
/* 2746 */                   queryBuf.append(catalogStr.toString());
/* 2747 */                   queryBuf.append(this.this$0.quotedId);
/*      */                   
/* 2749 */                   fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */                 } 
/*      */ 
/*      */                 
/* 2753 */                 String foreignTableWithCase = this.this$0.getTableNameWithCase(this.val$foreignTable);
/* 2754 */                 String primaryTableWithCase = this.this$0.getTableNameWithCase(this.val$primaryTable);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2762 */                 while (fkresults.next()) {
/* 2763 */                   String tableType = fkresults.getString("Type");
/*      */                   
/* 2765 */                   if (tableType != null && (tableType.equalsIgnoreCase("innodb") || tableType.equalsIgnoreCase("SUPPORTS_FK"))) {
/*      */ 
/*      */ 
/*      */                     
/* 2769 */                     String comment = fkresults.getString("Comment").trim();
/*      */ 
/*      */                     
/* 2772 */                     if (comment != null) {
/* 2773 */                       StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */ 
/*      */                       
/* 2776 */                       if (commentTokens.hasMoreTokens()) {
/* 2777 */                         String dummy = commentTokens.nextToken();
/*      */                       }
/*      */ 
/*      */ 
/*      */                       
/* 2782 */                       while (commentTokens.hasMoreTokens()) {
/* 2783 */                         String keys = commentTokens.nextToken();
/*      */                         
/* 2785 */                         DatabaseMetaData.LocalAndReferencedColumns parsedInfo = this.this$0.parseTableStatusIntoLocalAndReferencedColumns(keys);
/*      */                         
/* 2787 */                         int keySeq = 0;
/*      */                         
/* 2789 */                         Iterator referencingColumns = parsedInfo.localColumnsList.iterator();
/*      */                         
/* 2791 */                         Iterator referencedColumns = parsedInfo.referencedColumnsList.iterator();
/*      */ 
/*      */                         
/* 2794 */                         while (referencingColumns.hasNext()) {
/* 2795 */                           String referencingColumn = this.this$0.removeQuotedId(referencingColumns.next().toString());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                           
/* 2801 */                           byte[][] tuple = new byte[14][];
/* 2802 */                           tuple[4] = (this.val$foreignCatalog == null) ? null : this.this$0.s2b(this.val$foreignCatalog);
/*      */                           
/* 2804 */                           tuple[5] = (this.val$foreignSchema == null) ? null : this.this$0.s2b(this.val$foreignSchema);
/*      */                           
/* 2806 */                           String dummy = fkresults.getString("Name");
/*      */ 
/*      */                           
/* 2809 */                           if (dummy.compareTo(foreignTableWithCase) != 0) {
/*      */                             continue;
/*      */                           }
/*      */ 
/*      */                           
/* 2814 */                           tuple[6] = this.this$0.s2b(dummy);
/*      */                           
/* 2816 */                           tuple[7] = this.this$0.s2b(referencingColumn);
/* 2817 */                           tuple[0] = (this.val$primaryCatalog == null) ? null : this.this$0.s2b(this.val$primaryCatalog);
/*      */                           
/* 2819 */                           tuple[1] = (this.val$primarySchema == null) ? null : this.this$0.s2b(this.val$primarySchema);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                           
/* 2825 */                           if (parsedInfo.referencedTable.compareTo(primaryTableWithCase) != 0) {
/*      */                             continue;
/*      */                           }
/*      */ 
/*      */                           
/* 2830 */                           tuple[2] = this.this$0.s2b(parsedInfo.referencedTable);
/* 2831 */                           tuple[3] = this.this$0.s2b(this.this$0.removeQuotedId(referencedColumns.next().toString()));
/*      */                           
/* 2833 */                           tuple[8] = Integer.toString(keySeq).getBytes();
/*      */ 
/*      */                           
/* 2836 */                           int[] actions = this.this$0.getForeignKeyActions(keys);
/*      */                           
/* 2838 */                           tuple[9] = Integer.toString(actions[1]).getBytes();
/*      */                           
/* 2840 */                           tuple[10] = Integer.toString(actions[0]).getBytes();
/*      */                           
/* 2842 */                           tuple[11] = null;
/* 2843 */                           tuple[12] = null;
/* 2844 */                           tuple[13] = Integer.toString(7).getBytes();
/*      */ 
/*      */ 
/*      */                           
/* 2848 */                           this.val$tuples.add(new ByteArrayRow(tuple, this.this$0.getExceptionInterceptor()));
/* 2849 */                           keySeq++;
/*      */                         } 
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } finally {
/*      */                 
/* 2857 */                 if (fkresults != null) {
/*      */                   try {
/* 2859 */                     fkresults.close();
/* 2860 */                   } catch (Exception sqlEx) {
/* 2861 */                     AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                   } 
/*      */ 
/*      */                   
/* 2865 */                   fkresults = null;
/*      */                 } 
/*      */               } 
/*      */             } }
/*      */           ).doForAll();
/*      */       } finally {
/* 2871 */         if (stmt != null) {
/* 2872 */           stmt.close();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2877 */     ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 2879 */     return results;
/*      */   }
/*      */   
/*      */   protected Field[] createFkMetadataFields() {
/* 2883 */     Field[] fields = new Field[14];
/* 2884 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/* 2885 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/* 2886 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/* 2887 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/* 2888 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/* 2889 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/* 2890 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/* 2891 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/* 2892 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/* 2893 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/* 2894 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/* 2895 */     fields[11] = new Field("", "FK_NAME", 1, 0);
/* 2896 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/* 2897 */     fields[13] = new Field("", "DEFERRABILITY", 5, 2);
/* 2898 */     return fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDatabaseMajorVersion() throws SQLException {
/* 2905 */     return this.conn.getServerMajorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDatabaseMinorVersion() throws SQLException {
/* 2912 */     return this.conn.getServerMinorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDatabaseProductName() throws SQLException {
/* 2923 */     return "MySQL";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDatabaseProductVersion() throws SQLException {
/* 2934 */     return this.conn.getServerVersion();
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
/*      */   public int getDefaultTransactionIsolation() throws SQLException {
/* 2947 */     if (this.conn.supportsIsolationLevel()) {
/* 2948 */       return 2;
/*      */     }
/*      */     
/* 2951 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDriverMajorVersion() {
/* 2960 */     return NonRegisteringDriver.getMajorVersionInternal();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDriverMinorVersion() {
/* 2969 */     return NonRegisteringDriver.getMinorVersionInternal();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDriverName() throws SQLException {
/* 2980 */     return "MySQL-AB JDBC Driver";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDriverVersion() throws SQLException {
/* 2991 */     return "mysql-connector-java-5.1.10 ( Revision: ${svn.Revision} )";
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
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/* 3055 */     if (table == null) {
/* 3056 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 3060 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 3062 */     ArrayList rows = new ArrayList();
/*      */     
/* 3064 */     if (this.conn.versionMeetsMinimum(3, 23, 0)) {
/*      */       
/* 3066 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */       
/*      */       try {
/* 3070 */         (new IterateBlock(this, getCatalogIterator(catalog), stmt, table, rows) { private final Statement val$stmt; private final String val$table; private final ArrayList val$rows; private final DatabaseMetaData this$0;
/*      */             void forEach(Object catalogStr) throws SQLException {
/* 3072 */               ResultSet fkresults = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 3079 */                 if (this.this$0.conn.versionMeetsMinimum(3, 23, 50)) {
/*      */ 
/*      */                   
/* 3082 */                   fkresults = this.this$0.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
/*      */                 } else {
/*      */                   
/* 3085 */                   StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                   
/* 3087 */                   queryBuf.append(this.this$0.quotedId);
/* 3088 */                   queryBuf.append(catalogStr.toString());
/* 3089 */                   queryBuf.append(this.this$0.quotedId);
/*      */                   
/* 3091 */                   fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */                 } 
/*      */ 
/*      */ 
/*      */                 
/* 3096 */                 String tableNameWithCase = this.this$0.getTableNameWithCase(this.val$table);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3102 */                 while (fkresults.next()) {
/* 3103 */                   String tableType = fkresults.getString("Type");
/*      */                   
/* 3105 */                   if (tableType != null && (tableType.equalsIgnoreCase("innodb") || tableType.equalsIgnoreCase("SUPPORTS_FK")))
/*      */                   {
/*      */ 
/*      */                     
/* 3109 */                     String comment = fkresults.getString("Comment").trim();
/*      */ 
/*      */                     
/* 3112 */                     if (comment != null) {
/* 3113 */                       StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */ 
/*      */                       
/* 3116 */                       if (commentTokens.hasMoreTokens()) {
/* 3117 */                         commentTokens.nextToken();
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 3122 */                         while (commentTokens.hasMoreTokens()) {
/* 3123 */                           String keys = commentTokens.nextToken();
/*      */                           
/* 3125 */                           this.this$0.getExportKeyResults(catalogStr.toString(), tableNameWithCase, keys, this.val$rows, fkresults.getString("Name"));
/*      */                         }
/*      */                       
/*      */                       }
/*      */                     
/*      */                     }
/*      */                   
/*      */                   }
/*      */                 
/*      */                 }
/*      */               
/*      */               }
/*      */               finally {
/*      */                 
/* 3139 */                 if (fkresults != null) {
/*      */                   try {
/* 3141 */                     fkresults.close();
/* 3142 */                   } catch (SQLException sqlEx) {
/* 3143 */                     AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                   } 
/*      */ 
/*      */                   
/* 3147 */                   fkresults = null;
/*      */                 } 
/*      */               } 
/*      */             } }
/*      */           ).doForAll();
/*      */       } finally {
/* 3153 */         if (stmt != null) {
/* 3154 */           stmt.close();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3159 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3161 */     return results;
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
/*      */   private void getExportKeyResults(String catalog, String exportingTable, String keysComment, List tuples, String fkTableName) throws SQLException {
/* 3185 */     getResultsImpl(catalog, exportingTable, keysComment, tuples, fkTableName, true);
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
/*      */   public String getExtraNameCharacters() throws SQLException {
/* 3198 */     return "#@";
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
/*      */   private int[] getForeignKeyActions(String commentString) {
/* 3211 */     int[] actions = { 3, 3 };
/*      */ 
/*      */ 
/*      */     
/* 3215 */     int lastParenIndex = commentString.lastIndexOf(")");
/*      */     
/* 3217 */     if (lastParenIndex != commentString.length() - 1) {
/* 3218 */       String cascadeOptions = commentString.substring(lastParenIndex + 1).trim().toUpperCase(Locale.ENGLISH);
/*      */ 
/*      */       
/* 3221 */       actions[0] = getCascadeDeleteOption(cascadeOptions);
/* 3222 */       actions[1] = getCascadeUpdateOption(cascadeOptions);
/*      */     } 
/*      */     
/* 3225 */     return actions;
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
/*      */   public String getIdentifierQuoteString() throws SQLException {
/* 3238 */     if (this.conn.supportsQuotedIdentifiers()) {
/* 3239 */       if (!this.conn.useAnsiQuotedIdentifiers()) {
/* 3240 */         return "`";
/*      */       }
/*      */       
/* 3243 */       return "\"";
/*      */     } 
/*      */     
/* 3246 */     return " ";
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
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/* 3310 */     if (table == null) {
/* 3311 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 3315 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 3317 */     ArrayList rows = new ArrayList();
/*      */     
/* 3319 */     if (this.conn.versionMeetsMinimum(3, 23, 0)) {
/*      */       
/* 3321 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */       
/*      */       try {
/* 3325 */         (new IterateBlock(this, getCatalogIterator(catalog), table, stmt, rows) { private final String val$table; private final Statement val$stmt; private final ArrayList val$rows; private final DatabaseMetaData this$0;
/*      */             void forEach(Object catalogStr) throws SQLException {
/* 3327 */               ResultSet fkresults = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/* 3334 */                 if (this.this$0.conn.versionMeetsMinimum(3, 23, 50)) {
/*      */ 
/*      */                   
/* 3337 */                   fkresults = this.this$0.extractForeignKeyFromCreateTable(catalogStr.toString(), this.val$table);
/*      */                 } else {
/*      */                   
/* 3340 */                   StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS ");
/*      */                   
/* 3342 */                   queryBuf.append(" FROM ");
/* 3343 */                   queryBuf.append(this.this$0.quotedId);
/* 3344 */                   queryBuf.append(catalogStr.toString());
/* 3345 */                   queryBuf.append(this.this$0.quotedId);
/* 3346 */                   queryBuf.append(" LIKE '");
/* 3347 */                   queryBuf.append(this.val$table);
/* 3348 */                   queryBuf.append("'");
/*      */                   
/* 3350 */                   fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3358 */                 while (fkresults.next()) {
/* 3359 */                   String tableType = fkresults.getString("Type");
/*      */                   
/* 3361 */                   if (tableType != null && (tableType.equalsIgnoreCase("innodb") || tableType.equalsIgnoreCase("SUPPORTS_FK"))) {
/*      */ 
/*      */ 
/*      */                     
/* 3365 */                     String comment = fkresults.getString("Comment").trim();
/*      */ 
/*      */                     
/* 3368 */                     if (comment != null) {
/* 3369 */                       StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */ 
/*      */                       
/* 3372 */                       if (commentTokens.hasMoreTokens()) {
/* 3373 */                         commentTokens.nextToken();
/*      */ 
/*      */ 
/*      */ 
/*      */                         
/* 3378 */                         while (commentTokens.hasMoreTokens()) {
/* 3379 */                           String keys = commentTokens.nextToken();
/*      */                           
/* 3381 */                           this.this$0.getImportKeyResults(catalogStr.toString(), this.val$table, keys, this.val$rows);
/*      */                         }
/*      */                       
/*      */                       } 
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } finally {
/*      */                 
/* 3390 */                 if (fkresults != null) {
/*      */                   try {
/* 3392 */                     fkresults.close();
/* 3393 */                   } catch (SQLException sqlEx) {
/* 3394 */                     AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                   } 
/*      */ 
/*      */                   
/* 3398 */                   fkresults = null;
/*      */                 } 
/*      */               } 
/*      */             } }
/*      */           ).doForAll();
/*      */       } finally {
/* 3404 */         if (stmt != null) {
/* 3405 */           stmt.close();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3410 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3412 */     return results;
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
/*      */   private void getImportKeyResults(String catalog, String importingTable, String keysComment, List tuples) throws SQLException {
/* 3434 */     getResultsImpl(catalog, importingTable, keysComment, tuples, null, false);
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
/*      */   public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
/* 3505 */     Field[] fields = createIndexInfoFields();
/*      */     
/* 3507 */     ArrayList rows = new ArrayList();
/* 3508 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */     
/*      */     try {
/* 3512 */       (new IterateBlock(this, getCatalogIterator(catalog), table, stmt, unique, rows) { private final String val$table; private final Statement val$stmt; private final boolean val$unique; private final ArrayList val$rows; private final DatabaseMetaData this$0;
/*      */           
/*      */           void forEach(Object catalogStr) throws SQLException {
/* 3515 */             ResultSet results = null;
/*      */             
/*      */             try {
/* 3518 */               StringBuffer queryBuf = new StringBuffer("SHOW INDEX FROM ");
/*      */               
/* 3520 */               queryBuf.append(this.this$0.quotedId);
/* 3521 */               queryBuf.append(this.val$table);
/* 3522 */               queryBuf.append(this.this$0.quotedId);
/* 3523 */               queryBuf.append(" FROM ");
/* 3524 */               queryBuf.append(this.this$0.quotedId);
/* 3525 */               queryBuf.append(catalogStr.toString());
/* 3526 */               queryBuf.append(this.this$0.quotedId);
/*      */               
/*      */               try {
/* 3529 */                 results = this.val$stmt.executeQuery(queryBuf.toString());
/* 3530 */               } catch (SQLException sqlEx) {
/* 3531 */                 int errorCode = sqlEx.getErrorCode();
/*      */ 
/*      */ 
/*      */                 
/* 3535 */                 if (!"42S02".equals(sqlEx.getSQLState()))
/*      */                 {
/*      */                   
/* 3538 */                   if (errorCode != 1146) {
/* 3539 */                     throw sqlEx;
/*      */                   }
/*      */                 }
/*      */               } 
/*      */               
/* 3544 */               while (results != null && results.next()) {
/* 3545 */                 byte[][] row = new byte[14][];
/* 3546 */                 row[0] = (catalogStr.toString() == null) ? new byte[0] : this.this$0.s2b(catalogStr.toString());
/*      */ 
/*      */                 
/* 3549 */                 row[1] = null;
/* 3550 */                 row[2] = results.getBytes("Table");
/*      */                 
/* 3552 */                 boolean indexIsUnique = (results.getInt("Non_unique") == 0);
/*      */ 
/*      */                 
/* 3555 */                 row[3] = !indexIsUnique ? this.this$0.s2b("true") : this.this$0.s2b("false");
/*      */                 
/* 3557 */                 row[4] = new byte[0];
/* 3558 */                 row[5] = results.getBytes("Key_name");
/* 3559 */                 row[6] = Integer.toString(3).getBytes();
/*      */ 
/*      */                 
/* 3562 */                 row[7] = results.getBytes("Seq_in_index");
/* 3563 */                 row[8] = results.getBytes("Column_name");
/* 3564 */                 row[9] = results.getBytes("Collation");
/* 3565 */                 row[10] = results.getBytes("Cardinality");
/* 3566 */                 row[11] = this.this$0.s2b("0");
/* 3567 */                 row[12] = null;
/*      */                 
/* 3569 */                 if (this.val$unique) {
/* 3570 */                   if (indexIsUnique) {
/* 3571 */                     this.val$rows.add(new ByteArrayRow(row, this.this$0.getExceptionInterceptor()));
/*      */                   }
/*      */                   continue;
/*      */                 } 
/* 3575 */                 this.val$rows.add(new ByteArrayRow(row, this.this$0.getExceptionInterceptor()));
/*      */               } 
/*      */             } finally {
/*      */               
/* 3579 */               if (results != null) {
/*      */                 try {
/* 3581 */                   results.close();
/* 3582 */                 } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */                 
/* 3586 */                 results = null;
/*      */               } 
/*      */             } 
/*      */           } }
/*      */         ).doForAll();
/*      */       
/* 3592 */       ResultSet indexInfo = buildResultSet(fields, rows);
/*      */       
/* 3594 */       return indexInfo;
/*      */     } finally {
/* 3596 */       if (stmt != null) {
/* 3597 */         stmt.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Field[] createIndexInfoFields() {
/* 3603 */     Field[] fields = new Field[13];
/* 3604 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 3605 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 3606 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 3607 */     fields[3] = new Field("", "NON_UNIQUE", 16, 4);
/* 3608 */     fields[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
/* 3609 */     fields[5] = new Field("", "INDEX_NAME", 1, 32);
/* 3610 */     fields[6] = new Field("", "TYPE", 5, 32);
/* 3611 */     fields[7] = new Field("", "ORDINAL_POSITION", 5, 5);
/* 3612 */     fields[8] = new Field("", "COLUMN_NAME", 1, 32);
/* 3613 */     fields[9] = new Field("", "ASC_OR_DESC", 1, 1);
/* 3614 */     fields[10] = new Field("", "CARDINALITY", 4, 10);
/* 3615 */     fields[11] = new Field("", "PAGES", 4, 10);
/* 3616 */     fields[12] = new Field("", "FILTER_CONDITION", 1, 32);
/* 3617 */     return fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJDBCMajorVersion() throws SQLException {
/* 3624 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJDBCMinorVersion() throws SQLException {
/* 3631 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxBinaryLiteralLength() throws SQLException {
/* 3642 */     return 16777208;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCatalogNameLength() throws SQLException {
/* 3653 */     return 32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCharLiteralLength() throws SQLException {
/* 3664 */     return 16777208;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnNameLength() throws SQLException {
/* 3675 */     return 64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInGroupBy() throws SQLException {
/* 3686 */     return 64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInIndex() throws SQLException {
/* 3697 */     return 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInOrderBy() throws SQLException {
/* 3708 */     return 64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInSelect() throws SQLException {
/* 3719 */     return 256;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInTable() throws SQLException {
/* 3730 */     return 512;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxConnections() throws SQLException {
/* 3741 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxCursorNameLength() throws SQLException {
/* 3752 */     return 64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxIndexLength() throws SQLException {
/* 3763 */     return 256;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxProcedureNameLength() throws SQLException {
/* 3774 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxRowSize() throws SQLException {
/* 3785 */     return 2147483639;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxSchemaNameLength() throws SQLException {
/* 3796 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStatementLength() throws SQLException {
/* 3807 */     return MysqlIO.getMaxBuf() - 4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStatements() throws SQLException {
/* 3818 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxTableNameLength() throws SQLException {
/* 3829 */     return 64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxTablesInSelect() throws SQLException {
/* 3840 */     return 256;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxUserNameLength() throws SQLException {
/* 3851 */     return 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNumericFunctions() throws SQLException {
/* 3862 */     return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
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
/*      */   public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
/* 3894 */     Field[] fields = new Field[6];
/* 3895 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 3896 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 3897 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 3898 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 3899 */     fields[4] = new Field("", "KEY_SEQ", 5, 5);
/* 3900 */     fields[5] = new Field("", "PK_NAME", 1, 32);
/*      */     
/* 3902 */     if (table == null) {
/* 3903 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 3907 */     ArrayList rows = new ArrayList();
/* 3908 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */ 
/*      */     
/*      */     try {
/* 3912 */       (new IterateBlock(this, getCatalogIterator(catalog), table, stmt, rows) { private final String val$table; private final Statement val$stmt; private final ArrayList val$rows; private final DatabaseMetaData this$0;
/*      */           void forEach(Object catalogStr) throws SQLException {
/* 3914 */             ResultSet rs = null;
/*      */ 
/*      */             
/*      */             try {
/* 3918 */               StringBuffer queryBuf = new StringBuffer("SHOW KEYS FROM ");
/*      */               
/* 3920 */               queryBuf.append(this.this$0.quotedId);
/* 3921 */               queryBuf.append(this.val$table);
/* 3922 */               queryBuf.append(this.this$0.quotedId);
/* 3923 */               queryBuf.append(" FROM ");
/* 3924 */               queryBuf.append(this.this$0.quotedId);
/* 3925 */               queryBuf.append(catalogStr.toString());
/* 3926 */               queryBuf.append(this.this$0.quotedId);
/*      */               
/* 3928 */               rs = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               
/* 3930 */               TreeMap sortMap = new TreeMap();
/*      */               
/* 3932 */               while (rs.next()) {
/* 3933 */                 String keyType = rs.getString("Key_name");
/*      */                 
/* 3935 */                 if (keyType != null && (
/* 3936 */                   keyType.equalsIgnoreCase("PRIMARY") || keyType.equalsIgnoreCase("PRI"))) {
/*      */                   
/* 3938 */                   byte[][] tuple = new byte[6][];
/* 3939 */                   tuple[0] = (catalogStr.toString() == null) ? new byte[0] : this.this$0.s2b(catalogStr.toString());
/*      */                   
/* 3941 */                   tuple[1] = null;
/* 3942 */                   tuple[2] = this.this$0.s2b(this.val$table);
/*      */                   
/* 3944 */                   String columnName = rs.getString("Column_name");
/*      */                   
/* 3946 */                   tuple[3] = this.this$0.s2b(columnName);
/* 3947 */                   tuple[4] = this.this$0.s2b(rs.getString("Seq_in_index"));
/* 3948 */                   tuple[5] = this.this$0.s2b(keyType);
/* 3949 */                   sortMap.put(columnName, tuple);
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/* 3955 */               Iterator sortedIterator = sortMap.values().iterator();
/*      */               
/* 3957 */               while (sortedIterator.hasNext()) {
/* 3958 */                 this.val$rows.add(new ByteArrayRow(sortedIterator.next(), this.this$0.getExceptionInterceptor()));
/*      */               }
/*      */             } finally {
/*      */               
/* 3962 */               if (rs != null) {
/*      */                 try {
/* 3964 */                   rs.close();
/* 3965 */                 } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */                 
/* 3969 */                 rs = null;
/*      */               } 
/*      */             } 
/*      */           } }
/*      */         ).doForAll();
/*      */     } finally {
/* 3975 */       if (stmt != null) {
/* 3976 */         stmt.close();
/*      */       }
/*      */     } 
/*      */     
/* 3980 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3982 */     return results;
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
/*      */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
/* 4054 */     Field[] fields = createProcedureColumnsFields();
/*      */     
/* 4056 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Field[] createProcedureColumnsFields() {
/* 4063 */     Field[] fields = new Field[13];
/*      */     
/* 4065 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 0);
/* 4066 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 0);
/* 4067 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 0);
/* 4068 */     fields[3] = new Field("", "COLUMN_NAME", 1, 0);
/* 4069 */     fields[4] = new Field("", "COLUMN_TYPE", 1, 0);
/* 4070 */     fields[5] = new Field("", "DATA_TYPE", 5, 0);
/* 4071 */     fields[6] = new Field("", "TYPE_NAME", 1, 0);
/* 4072 */     fields[7] = new Field("", "PRECISION", 4, 0);
/* 4073 */     fields[8] = new Field("", "LENGTH", 4, 0);
/* 4074 */     fields[9] = new Field("", "SCALE", 5, 0);
/* 4075 */     fields[10] = new Field("", "RADIX", 5, 0);
/* 4076 */     fields[11] = new Field("", "NULLABLE", 5, 0);
/* 4077 */     fields[12] = new Field("", "REMARKS", 1, 0);
/* 4078 */     return fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResultSet getProcedureOrFunctionColumns(Field[] fields, String catalog, String schemaPattern, String procedureOrFunctionNamePattern, String columnNamePattern, boolean returnProcedures, boolean returnFunctions) throws SQLException {
/* 4087 */     List proceduresToExtractList = new ArrayList();
/*      */     
/* 4089 */     if (supportsStoredProcedures()) {
/* 4090 */       if (procedureOrFunctionNamePattern.indexOf("%") == -1 && procedureOrFunctionNamePattern.indexOf("?") == -1) {
/*      */         
/* 4092 */         proceduresToExtractList.add(procedureOrFunctionNamePattern);
/*      */       } else {
/*      */         
/* 4095 */         ResultSet procedureNameRs = null;
/*      */ 
/*      */         
/*      */         try {
/* 4099 */           procedureNameRs = getProceduresAndOrFunctions(createFieldMetadataForGetProcedures(), catalog, schemaPattern, procedureOrFunctionNamePattern, returnProcedures, returnFunctions);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4105 */           while (procedureNameRs.next()) {
/* 4106 */             proceduresToExtractList.add(procedureNameRs.getString(3));
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4114 */           Collections.sort(proceduresToExtractList);
/*      */         } finally {
/* 4116 */           SQLException rethrowSqlEx = null;
/*      */           
/* 4118 */           if (procedureNameRs != null) {
/*      */             try {
/* 4120 */               procedureNameRs.close();
/* 4121 */             } catch (SQLException sqlEx) {
/* 4122 */               rethrowSqlEx = sqlEx;
/*      */             } 
/*      */           }
/*      */           
/* 4126 */           if (rethrowSqlEx != null) {
/* 4127 */             throw rethrowSqlEx;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 4133 */     ArrayList resultRows = new ArrayList();
/*      */     
/* 4135 */     for (Iterator iter = proceduresToExtractList.iterator(); iter.hasNext(); ) {
/* 4136 */       String procName = iter.next();
/*      */       
/* 4138 */       getCallStmtParameterTypes(catalog, procName, columnNamePattern, resultRows, (fields.length == 17));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4143 */     return buildResultSet(fields, resultRows);
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
/* 4189 */     Field[] fields = createFieldMetadataForGetProcedures();
/*      */     
/* 4191 */     return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private Field[] createFieldMetadataForGetProcedures() {
/* 4196 */     Field[] fields = new Field[9];
/* 4197 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 255);
/* 4198 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 255);
/* 4199 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 255);
/* 4200 */     fields[3] = new Field("", "reserved1", 1, 0);
/* 4201 */     fields[4] = new Field("", "reserved2", 1, 0);
/* 4202 */     fields[5] = new Field("", "reserved3", 1, 0);
/* 4203 */     fields[6] = new Field("", "REMARKS", 1, 255);
/* 4204 */     fields[7] = new Field("", "PROCEDURE_TYPE", 5, 6);
/* 4205 */     fields[8] = new Field("", "SPECIFIC_NAME", 1, 255);
/*      */     
/* 4207 */     return fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResultSet getProceduresAndOrFunctions(Field[] fields, String catalog, String schemaPattern, String procedureNamePattern, boolean returnProcedures, boolean returnFunctions) throws SQLException {
/* 4217 */     if (procedureNamePattern == null || procedureNamePattern.length() == 0)
/*      */     {
/* 4219 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4220 */         procedureNamePattern = "%";
/*      */       } else {
/* 4222 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4228 */     ArrayList procedureRows = new ArrayList();
/*      */     
/* 4230 */     if (supportsStoredProcedures()) {
/* 4231 */       String procNamePattern = procedureNamePattern;
/*      */       
/* 4233 */       Map procedureRowsOrderedByName = new TreeMap();
/*      */       
/* 4235 */       (new IterateBlock(this, getCatalogIterator(catalog), procNamePattern, returnProcedures, procedureRowsOrderedByName, returnFunctions, fields, procedureRows) { private final String val$procNamePattern; private final boolean val$returnProcedures; private final Map val$procedureRowsOrderedByName; private final boolean val$returnFunctions; private final Field[] val$fields; private final ArrayList val$procedureRows; private final DatabaseMetaData this$0;
/*      */           void forEach(Object catalogStr) throws SQLException {
/* 4237 */             String db = catalogStr.toString();
/*      */             
/* 4239 */             boolean fromSelect = false;
/* 4240 */             ResultSet proceduresRs = null;
/* 4241 */             boolean needsClientFiltering = true;
/* 4242 */             PreparedStatement proceduresStmt = (PreparedStatement)this.this$0.conn.clientPrepareStatement("SELECT name, type, comment FROM mysql.proc WHERE name like ? and db <=> ? ORDER BY name");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/* 4251 */               boolean hasTypeColumn = false;
/*      */               
/* 4253 */               if (db != null) {
/* 4254 */                 proceduresStmt.setString(2, db);
/*      */               } else {
/* 4256 */                 proceduresStmt.setNull(2, 12);
/*      */               } 
/*      */               
/* 4259 */               int nameIndex = 1;
/*      */               
/* 4261 */               if (proceduresStmt.getMaxRows() != 0) {
/* 4262 */                 proceduresStmt.setMaxRows(0);
/*      */               }
/*      */               
/* 4265 */               proceduresStmt.setString(1, this.val$procNamePattern);
/*      */               
/*      */               try {
/* 4268 */                 proceduresRs = proceduresStmt.executeQuery();
/* 4269 */                 fromSelect = true;
/* 4270 */                 needsClientFiltering = false;
/* 4271 */                 hasTypeColumn = true;
/* 4272 */               } catch (SQLException sqlEx) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 4279 */                 proceduresStmt.close();
/*      */                 
/* 4281 */                 fromSelect = false;
/*      */                 
/* 4283 */                 if (this.this$0.conn.versionMeetsMinimum(5, 0, 1)) {
/* 4284 */                   nameIndex = 2;
/*      */                 } else {
/* 4286 */                   nameIndex = 1;
/*      */                 } 
/*      */                 
/* 4289 */                 proceduresStmt = (PreparedStatement)this.this$0.conn.clientPrepareStatement("SHOW PROCEDURE STATUS LIKE ?");
/*      */ 
/*      */                 
/* 4292 */                 if (proceduresStmt.getMaxRows() != 0) {
/* 4293 */                   proceduresStmt.setMaxRows(0);
/*      */                 }
/*      */                 
/* 4296 */                 proceduresStmt.setString(1, this.val$procNamePattern);
/*      */                 
/* 4298 */                 proceduresRs = proceduresStmt.executeQuery();
/*      */               } 
/*      */               
/* 4301 */               if (this.val$returnProcedures) {
/* 4302 */                 this.this$0.convertToJdbcProcedureList(fromSelect, db, proceduresRs, needsClientFiltering, db, this.val$procedureRowsOrderedByName, nameIndex);
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 4307 */               if (!hasTypeColumn) {
/*      */                 
/* 4309 */                 if (proceduresStmt != null) {
/* 4310 */                   proceduresStmt.close();
/*      */                 }
/*      */                 
/* 4313 */                 proceduresStmt = (PreparedStatement)this.this$0.conn.clientPrepareStatement("SHOW FUNCTION STATUS LIKE ?");
/*      */ 
/*      */                 
/* 4316 */                 if (proceduresStmt.getMaxRows() != 0) {
/* 4317 */                   proceduresStmt.setMaxRows(0);
/*      */                 }
/*      */                 
/* 4320 */                 proceduresStmt.setString(1, this.val$procNamePattern);
/*      */                 
/* 4322 */                 proceduresRs = proceduresStmt.executeQuery();
/*      */                 
/* 4324 */                 if (this.val$returnFunctions) {
/* 4325 */                   this.this$0.convertToJdbcFunctionList(db, proceduresRs, needsClientFiltering, db, this.val$procedureRowsOrderedByName, nameIndex, this.val$fields);
/*      */                 }
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 4334 */               Iterator proceduresIter = this.val$procedureRowsOrderedByName.values().iterator();
/*      */ 
/*      */               
/* 4337 */               while (proceduresIter.hasNext()) {
/* 4338 */                 this.val$procedureRows.add(proceduresIter.next());
/*      */               }
/*      */             } finally {
/* 4341 */               SQLException rethrowSqlEx = null;
/*      */               
/* 4343 */               if (proceduresRs != null) {
/*      */                 try {
/* 4345 */                   proceduresRs.close();
/* 4346 */                 } catch (SQLException sqlEx) {
/* 4347 */                   rethrowSqlEx = sqlEx;
/*      */                 } 
/*      */               }
/*      */               
/* 4351 */               if (proceduresStmt != null) {
/*      */                 try {
/* 4353 */                   proceduresStmt.close();
/* 4354 */                 } catch (SQLException sqlEx) {
/* 4355 */                   rethrowSqlEx = sqlEx;
/*      */                 } 
/*      */               }
/*      */               
/* 4359 */               if (rethrowSqlEx != null) {
/* 4360 */                 throw rethrowSqlEx;
/*      */               }
/*      */             } 
/*      */           } }
/*      */         ).doForAll();
/*      */     } 
/*      */     
/* 4367 */     return buildResultSet(fields, procedureRows);
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
/*      */   public String getProcedureTerm() throws SQLException {
/* 4379 */     return "PROCEDURE";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getResultSetHoldability() throws SQLException {
/* 4386 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getResultsImpl(String catalog, String table, String keysComment, List tuples, String fkTableName, boolean isExport) throws SQLException {
/* 4393 */     LocalAndReferencedColumns parsedInfo = parseTableStatusIntoLocalAndReferencedColumns(keysComment);
/*      */     
/* 4395 */     if (isExport && !parsedInfo.referencedTable.equals(table)) {
/*      */       return;
/*      */     }
/*      */     
/* 4399 */     if (parsedInfo.localColumnsList.size() != parsedInfo.referencedColumnsList.size())
/*      */     {
/* 4401 */       throw SQLError.createSQLException("Error parsing foreign keys definition,number of local and referenced columns is not the same.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4407 */     Iterator localColumnNames = parsedInfo.localColumnsList.iterator();
/* 4408 */     Iterator referColumnNames = parsedInfo.referencedColumnsList.iterator();
/*      */     
/* 4410 */     int keySeqIndex = 1;
/*      */     
/* 4412 */     while (localColumnNames.hasNext()) {
/* 4413 */       byte[][] tuple = new byte[14][];
/* 4414 */       String lColumnName = removeQuotedId(localColumnNames.next().toString());
/*      */       
/* 4416 */       String rColumnName = removeQuotedId(referColumnNames.next().toString());
/*      */       
/* 4418 */       tuple[4] = (catalog == null) ? new byte[0] : s2b(catalog);
/*      */       
/* 4420 */       tuple[5] = null;
/* 4421 */       tuple[6] = s2b(isExport ? fkTableName : table);
/* 4422 */       tuple[7] = s2b(lColumnName);
/* 4423 */       tuple[0] = s2b(parsedInfo.referencedCatalog);
/* 4424 */       tuple[1] = null;
/* 4425 */       tuple[2] = s2b(isExport ? table : parsedInfo.referencedTable);
/*      */       
/* 4427 */       tuple[3] = s2b(rColumnName);
/* 4428 */       tuple[8] = s2b(Integer.toString(keySeqIndex++));
/*      */       
/* 4430 */       int[] actions = getForeignKeyActions(keysComment);
/*      */       
/* 4432 */       tuple[9] = s2b(Integer.toString(actions[1]));
/* 4433 */       tuple[10] = s2b(Integer.toString(actions[0]));
/* 4434 */       tuple[11] = s2b(parsedInfo.constraintName);
/* 4435 */       tuple[12] = null;
/* 4436 */       tuple[13] = s2b(Integer.toString(7));
/*      */       
/* 4438 */       tuples.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
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
/*      */   public ResultSet getSchemas() throws SQLException {
/* 4458 */     Field[] fields = new Field[2];
/* 4459 */     fields[0] = new Field("", "TABLE_SCHEM", 1, 0);
/* 4460 */     fields[1] = new Field("", "TABLE_CATALOG", 1, 0);
/*      */     
/* 4462 */     ArrayList tuples = new ArrayList();
/* 4463 */     ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 4465 */     return results;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchemaTerm() throws SQLException {
/* 4476 */     return "";
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
/*      */   public String getSearchStringEscape() throws SQLException {
/* 4494 */     return "\\";
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
/*      */   public String getSQLKeywords() throws SQLException {
/* 4506 */     return mysqlKeywordsThatArentSQL92;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSQLStateType() throws SQLException {
/* 4513 */     if (this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 4514 */       return 2;
/*      */     }
/*      */     
/* 4517 */     if (this.conn.getUseSqlStateCodes()) {
/* 4518 */       return 2;
/*      */     }
/*      */     
/* 4521 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringFunctions() throws SQLException {
/* 4532 */     return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
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
/*      */   public ResultSet getSuperTables(String arg0, String arg1, String arg2) throws SQLException {
/* 4546 */     Field[] fields = new Field[4];
/* 4547 */     fields[0] = new Field("", "TABLE_CAT", 1, 32);
/* 4548 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 32);
/* 4549 */     fields[2] = new Field("", "TABLE_NAME", 1, 32);
/* 4550 */     fields[3] = new Field("", "SUPERTABLE_NAME", 1, 32);
/*      */     
/* 4552 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSuperTypes(String arg0, String arg1, String arg2) throws SQLException {
/* 4560 */     Field[] fields = new Field[6];
/* 4561 */     fields[0] = new Field("", "TYPE_CAT", 1, 32);
/* 4562 */     fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
/* 4563 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 4564 */     fields[3] = new Field("", "SUPERTYPE_CAT", 1, 32);
/* 4565 */     fields[4] = new Field("", "SUPERTYPE_SCHEM", 1, 32);
/* 4566 */     fields[5] = new Field("", "SUPERTYPE_NAME", 1, 32);
/*      */     
/* 4568 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSystemFunctions() throws SQLException {
/* 4579 */     return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
/*      */   }
/*      */   
/*      */   private String getTableNameWithCase(String table) {
/* 4583 */     String tableNameWithCase = this.conn.lowerCaseTableNames() ? table.toLowerCase() : table;
/*      */ 
/*      */     
/* 4586 */     return tableNameWithCase;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
/*      */     // Byte code:
/*      */     //   0: aload_3
/*      */     //   1: ifnonnull -> 33
/*      */     //   4: aload_0
/*      */     //   5: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   8: invokevirtual getNullNamePatternMatchesAll : ()Z
/*      */     //   11: ifeq -> 20
/*      */     //   14: ldc '%'
/*      */     //   16: astore_3
/*      */     //   17: goto -> 33
/*      */     //   20: ldc_w 'Table name pattern can not be NULL or empty.'
/*      */     //   23: ldc 'S1009'
/*      */     //   25: aload_0
/*      */     //   26: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   29: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   32: athrow
/*      */     //   33: bipush #7
/*      */     //   35: anewarray com/mysql/jdbc/Field
/*      */     //   38: astore #4
/*      */     //   40: aload #4
/*      */     //   42: iconst_0
/*      */     //   43: new com/mysql/jdbc/Field
/*      */     //   46: dup
/*      */     //   47: ldc ''
/*      */     //   49: ldc 'TABLE_CAT'
/*      */     //   51: iconst_1
/*      */     //   52: bipush #64
/*      */     //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   57: aastore
/*      */     //   58: aload #4
/*      */     //   60: iconst_1
/*      */     //   61: new com/mysql/jdbc/Field
/*      */     //   64: dup
/*      */     //   65: ldc ''
/*      */     //   67: ldc_w 'TABLE_SCHEM'
/*      */     //   70: iconst_1
/*      */     //   71: iconst_1
/*      */     //   72: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   75: aastore
/*      */     //   76: aload #4
/*      */     //   78: iconst_2
/*      */     //   79: new com/mysql/jdbc/Field
/*      */     //   82: dup
/*      */     //   83: ldc ''
/*      */     //   85: ldc 'TABLE_NAME'
/*      */     //   87: iconst_1
/*      */     //   88: bipush #64
/*      */     //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   93: aastore
/*      */     //   94: aload #4
/*      */     //   96: iconst_3
/*      */     //   97: new com/mysql/jdbc/Field
/*      */     //   100: dup
/*      */     //   101: ldc ''
/*      */     //   103: ldc_w 'GRANTOR'
/*      */     //   106: iconst_1
/*      */     //   107: bipush #77
/*      */     //   109: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   112: aastore
/*      */     //   113: aload #4
/*      */     //   115: iconst_4
/*      */     //   116: new com/mysql/jdbc/Field
/*      */     //   119: dup
/*      */     //   120: ldc ''
/*      */     //   122: ldc_w 'GRANTEE'
/*      */     //   125: iconst_1
/*      */     //   126: bipush #77
/*      */     //   128: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   131: aastore
/*      */     //   132: aload #4
/*      */     //   134: iconst_5
/*      */     //   135: new com/mysql/jdbc/Field
/*      */     //   138: dup
/*      */     //   139: ldc ''
/*      */     //   141: ldc_w 'PRIVILEGE'
/*      */     //   144: iconst_1
/*      */     //   145: bipush #64
/*      */     //   147: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   150: aastore
/*      */     //   151: aload #4
/*      */     //   153: bipush #6
/*      */     //   155: new com/mysql/jdbc/Field
/*      */     //   158: dup
/*      */     //   159: ldc ''
/*      */     //   161: ldc_w 'IS_GRANTABLE'
/*      */     //   164: iconst_1
/*      */     //   165: iconst_3
/*      */     //   166: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;II)V
/*      */     //   169: aastore
/*      */     //   170: new java/lang/StringBuffer
/*      */     //   173: dup
/*      */     //   174: ldc_w 'SELECT host,db,table_name,grantor,user,table_priv from mysql.tables_priv '
/*      */     //   177: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   180: astore #5
/*      */     //   182: aload #5
/*      */     //   184: ldc_w ' WHERE '
/*      */     //   187: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   190: pop
/*      */     //   191: aload_1
/*      */     //   192: ifnull -> 227
/*      */     //   195: aload_1
/*      */     //   196: invokevirtual length : ()I
/*      */     //   199: ifeq -> 227
/*      */     //   202: aload #5
/*      */     //   204: ldc_w ' db=''
/*      */     //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   210: pop
/*      */     //   211: aload #5
/*      */     //   213: aload_1
/*      */     //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   217: pop
/*      */     //   218: aload #5
/*      */     //   220: ldc_w '' AND '
/*      */     //   223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   226: pop
/*      */     //   227: aload #5
/*      */     //   229: ldc_w 'table_name like ''
/*      */     //   232: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   235: pop
/*      */     //   236: aload #5
/*      */     //   238: aload_3
/*      */     //   239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   242: pop
/*      */     //   243: aload #5
/*      */     //   245: ldc '''
/*      */     //   247: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   250: pop
/*      */     //   251: aconst_null
/*      */     //   252: astore #6
/*      */     //   254: new java/util/ArrayList
/*      */     //   257: dup
/*      */     //   258: invokespecial <init> : ()V
/*      */     //   261: astore #7
/*      */     //   263: aconst_null
/*      */     //   264: astore #8
/*      */     //   266: aload_0
/*      */     //   267: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   270: invokevirtual createStatement : ()Ljava/sql/Statement;
/*      */     //   273: astore #8
/*      */     //   275: aload #8
/*      */     //   277: iconst_0
/*      */     //   278: invokeinterface setEscapeProcessing : (Z)V
/*      */     //   283: aload #8
/*      */     //   285: aload #5
/*      */     //   287: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   290: invokeinterface executeQuery : (Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */     //   295: astore #6
/*      */     //   297: aload #6
/*      */     //   299: invokeinterface next : ()Z
/*      */     //   304: ifeq -> 646
/*      */     //   307: aload #6
/*      */     //   309: iconst_1
/*      */     //   310: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   315: astore #9
/*      */     //   317: aload #6
/*      */     //   319: iconst_2
/*      */     //   320: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   325: astore #10
/*      */     //   327: aload #6
/*      */     //   329: iconst_3
/*      */     //   330: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   335: astore #11
/*      */     //   337: aload #6
/*      */     //   339: iconst_4
/*      */     //   340: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   345: astore #12
/*      */     //   347: aload #6
/*      */     //   349: iconst_5
/*      */     //   350: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   355: astore #13
/*      */     //   357: aload #13
/*      */     //   359: ifnull -> 370
/*      */     //   362: aload #13
/*      */     //   364: invokevirtual length : ()I
/*      */     //   367: ifne -> 374
/*      */     //   370: ldc '%'
/*      */     //   372: astore #13
/*      */     //   374: new java/lang/StringBuffer
/*      */     //   377: dup
/*      */     //   378: aload #13
/*      */     //   380: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   383: astore #14
/*      */     //   385: aload #9
/*      */     //   387: ifnull -> 417
/*      */     //   390: aload_0
/*      */     //   391: getfield conn : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   394: invokevirtual getUseHostsInPrivileges : ()Z
/*      */     //   397: ifeq -> 417
/*      */     //   400: aload #14
/*      */     //   402: ldc_w '@'
/*      */     //   405: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   408: pop
/*      */     //   409: aload #14
/*      */     //   411: aload #9
/*      */     //   413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   416: pop
/*      */     //   417: aload #6
/*      */     //   419: bipush #6
/*      */     //   421: invokeinterface getString : (I)Ljava/lang/String;
/*      */     //   426: astore #15
/*      */     //   428: aload #15
/*      */     //   430: ifnull -> 643
/*      */     //   433: aload #15
/*      */     //   435: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
/*      */     //   438: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
/*      */     //   441: astore #15
/*      */     //   443: new java/util/StringTokenizer
/*      */     //   446: dup
/*      */     //   447: aload #15
/*      */     //   449: ldc ','
/*      */     //   451: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   454: astore #16
/*      */     //   456: aload #16
/*      */     //   458: invokevirtual hasMoreTokens : ()Z
/*      */     //   461: ifeq -> 643
/*      */     //   464: aload #16
/*      */     //   466: invokevirtual nextToken : ()Ljava/lang/String;
/*      */     //   469: invokevirtual trim : ()Ljava/lang/String;
/*      */     //   472: astore #17
/*      */     //   474: aconst_null
/*      */     //   475: astore #18
/*      */     //   477: aload_0
/*      */     //   478: aload_1
/*      */     //   479: aload_2
/*      */     //   480: aload #11
/*      */     //   482: ldc '%'
/*      */     //   484: invokevirtual getColumns : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */     //   487: astore #18
/*      */     //   489: aload #18
/*      */     //   491: invokeinterface next : ()Z
/*      */     //   496: ifeq -> 605
/*      */     //   499: bipush #8
/*      */     //   501: anewarray [B
/*      */     //   504: astore #19
/*      */     //   506: aload #19
/*      */     //   508: iconst_0
/*      */     //   509: aload_0
/*      */     //   510: aload #10
/*      */     //   512: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */     //   515: aastore
/*      */     //   516: aload #19
/*      */     //   518: iconst_1
/*      */     //   519: aconst_null
/*      */     //   520: aastore
/*      */     //   521: aload #19
/*      */     //   523: iconst_2
/*      */     //   524: aload_0
/*      */     //   525: aload #11
/*      */     //   527: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */     //   530: aastore
/*      */     //   531: aload #12
/*      */     //   533: ifnull -> 549
/*      */     //   536: aload #19
/*      */     //   538: iconst_3
/*      */     //   539: aload_0
/*      */     //   540: aload #12
/*      */     //   542: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */     //   545: aastore
/*      */     //   546: goto -> 554
/*      */     //   549: aload #19
/*      */     //   551: iconst_3
/*      */     //   552: aconst_null
/*      */     //   553: aastore
/*      */     //   554: aload #19
/*      */     //   556: iconst_4
/*      */     //   557: aload_0
/*      */     //   558: aload #14
/*      */     //   560: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   563: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */     //   566: aastore
/*      */     //   567: aload #19
/*      */     //   569: iconst_5
/*      */     //   570: aload_0
/*      */     //   571: aload #17
/*      */     //   573: invokevirtual s2b : (Ljava/lang/String;)[B
/*      */     //   576: aastore
/*      */     //   577: aload #19
/*      */     //   579: bipush #6
/*      */     //   581: aconst_null
/*      */     //   582: aastore
/*      */     //   583: aload #7
/*      */     //   585: new com/mysql/jdbc/ByteArrayRow
/*      */     //   588: dup
/*      */     //   589: aload #19
/*      */     //   591: aload_0
/*      */     //   592: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   595: invokespecial <init> : ([[BLcom/mysql/jdbc/ExceptionInterceptor;)V
/*      */     //   598: invokevirtual add : (Ljava/lang/Object;)Z
/*      */     //   601: pop
/*      */     //   602: goto -> 489
/*      */     //   605: jsr -> 619
/*      */     //   608: goto -> 640
/*      */     //   611: astore #20
/*      */     //   613: jsr -> 619
/*      */     //   616: aload #20
/*      */     //   618: athrow
/*      */     //   619: astore #21
/*      */     //   621: aload #18
/*      */     //   623: ifnull -> 638
/*      */     //   626: aload #18
/*      */     //   628: invokeinterface close : ()V
/*      */     //   633: goto -> 638
/*      */     //   636: astore #22
/*      */     //   638: ret #21
/*      */     //   640: goto -> 456
/*      */     //   643: goto -> 297
/*      */     //   646: jsr -> 660
/*      */     //   649: goto -> 704
/*      */     //   652: astore #23
/*      */     //   654: jsr -> 660
/*      */     //   657: aload #23
/*      */     //   659: athrow
/*      */     //   660: astore #24
/*      */     //   662: aload #6
/*      */     //   664: ifnull -> 682
/*      */     //   667: aload #6
/*      */     //   669: invokeinterface close : ()V
/*      */     //   674: goto -> 679
/*      */     //   677: astore #25
/*      */     //   679: aconst_null
/*      */     //   680: astore #6
/*      */     //   682: aload #8
/*      */     //   684: ifnull -> 702
/*      */     //   687: aload #8
/*      */     //   689: invokeinterface close : ()V
/*      */     //   694: goto -> 699
/*      */     //   697: astore #25
/*      */     //   699: aconst_null
/*      */     //   700: astore #8
/*      */     //   702: ret #24
/*      */     //   704: aload_0
/*      */     //   705: aload #4
/*      */     //   707: aload #7
/*      */     //   709: invokespecial buildResultSet : ([Lcom/mysql/jdbc/Field;Ljava/util/ArrayList;)Ljava/sql/ResultSet;
/*      */     //   712: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #4626	-> 0
/*      */     //   #4627	-> 4
/*      */     //   #4628	-> 14
/*      */     //   #4630	-> 20
/*      */     //   #4636	-> 33
/*      */     //   #4637	-> 40
/*      */     //   #4638	-> 58
/*      */     //   #4639	-> 76
/*      */     //   #4640	-> 94
/*      */     //   #4641	-> 113
/*      */     //   #4642	-> 132
/*      */     //   #4643	-> 151
/*      */     //   #4645	-> 170
/*      */     //   #4647	-> 182
/*      */     //   #4649	-> 191
/*      */     //   #4650	-> 202
/*      */     //   #4651	-> 211
/*      */     //   #4652	-> 218
/*      */     //   #4655	-> 227
/*      */     //   #4656	-> 236
/*      */     //   #4657	-> 243
/*      */     //   #4659	-> 251
/*      */     //   #4660	-> 254
/*      */     //   #4661	-> 263
/*      */     //   #4664	-> 266
/*      */     //   #4665	-> 275
/*      */     //   #4667	-> 283
/*      */     //   #4669	-> 297
/*      */     //   #4670	-> 307
/*      */     //   #4671	-> 317
/*      */     //   #4672	-> 327
/*      */     //   #4673	-> 337
/*      */     //   #4674	-> 347
/*      */     //   #4676	-> 357
/*      */     //   #4677	-> 370
/*      */     //   #4680	-> 374
/*      */     //   #4682	-> 385
/*      */     //   #4683	-> 400
/*      */     //   #4684	-> 409
/*      */     //   #4687	-> 417
/*      */     //   #4689	-> 428
/*      */     //   #4690	-> 433
/*      */     //   #4692	-> 443
/*      */     //   #4694	-> 456
/*      */     //   #4695	-> 464
/*      */     //   #4698	-> 474
/*      */     //   #4701	-> 477
/*      */     //   #4704	-> 489
/*      */     //   #4705	-> 499
/*      */     //   #4706	-> 506
/*      */     //   #4707	-> 516
/*      */     //   #4708	-> 521
/*      */     //   #4710	-> 531
/*      */     //   #4711	-> 536
/*      */     //   #4713	-> 549
/*      */     //   #4716	-> 554
/*      */     //   #4717	-> 567
/*      */     //   #4718	-> 577
/*      */     //   #4719	-> 583
/*      */     //   #4721	-> 605
/*      */     //   #4729	-> 608
/*      */     //   #4722	-> 611
/*      */     //   #4724	-> 626
/*      */     //   #4727	-> 633
/*      */     //   #4725	-> 636
/*      */     //   #4727	-> 638
/*      */     //   #4733	-> 646
/*      */     //   #4753	-> 649
/*      */     //   #4734	-> 652
/*      */     //   #4736	-> 667
/*      */     //   #4739	-> 674
/*      */     //   #4737	-> 677
/*      */     //   #4741	-> 679
/*      */     //   #4744	-> 682
/*      */     //   #4746	-> 687
/*      */     //   #4749	-> 694
/*      */     //   #4747	-> 697
/*      */     //   #4751	-> 699
/*      */     //   #4755	-> 704
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   506	96	19	tuple	[[B
/*      */     //   638	0	22	ex	Ljava/lang/Exception;
/*      */     //   474	166	17	privilege	Ljava/lang/String;
/*      */     //   477	163	18	columnResults	Ljava/sql/ResultSet;
/*      */     //   456	187	16	st	Ljava/util/StringTokenizer;
/*      */     //   317	326	9	host	Ljava/lang/String;
/*      */     //   327	316	10	db	Ljava/lang/String;
/*      */     //   337	306	11	table	Ljava/lang/String;
/*      */     //   347	296	12	grantor	Ljava/lang/String;
/*      */     //   357	286	13	user	Ljava/lang/String;
/*      */     //   385	258	14	fullUser	Ljava/lang/StringBuffer;
/*      */     //   428	215	15	allPrivileges	Ljava/lang/String;
/*      */     //   679	0	25	ex	Ljava/lang/Exception;
/*      */     //   699	0	25	ex	Ljava/lang/Exception;
/*      */     //   0	713	0	this	Lcom/mysql/jdbc/DatabaseMetaData;
/*      */     //   0	713	1	catalog	Ljava/lang/String;
/*      */     //   0	713	2	schemaPattern	Ljava/lang/String;
/*      */     //   0	713	3	tableNamePattern	Ljava/lang/String;
/*      */     //   40	673	4	fields	[Lcom/mysql/jdbc/Field;
/*      */     //   182	531	5	grantQuery	Ljava/lang/StringBuffer;
/*      */     //   254	459	6	results	Ljava/sql/ResultSet;
/*      */     //   263	450	7	grantRows	Ljava/util/ArrayList;
/*      */     //   266	447	8	stmt	Ljava/sql/Statement;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   266	649	652	finally
/*      */     //   477	608	611	finally
/*      */     //   611	616	611	finally
/*      */     //   626	633	636	java/lang/Exception
/*      */     //   652	657	652	finally
/*      */     //   667	674	677	java/lang/Exception
/*      */     //   687	694	697	java/lang/Exception
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
/* 4797 */     if (tableNamePattern == null) {
/* 4798 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4799 */         tableNamePattern = "%";
/*      */       } else {
/* 4801 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4807 */     Field[] fields = new Field[5];
/* 4808 */     fields[0] = new Field("", "TABLE_CAT", 12, 255);
/* 4809 */     fields[1] = new Field("", "TABLE_SCHEM", 12, 0);
/* 4810 */     fields[2] = new Field("", "TABLE_NAME", 12, 255);
/* 4811 */     fields[3] = new Field("", "TABLE_TYPE", 12, 5);
/* 4812 */     fields[4] = new Field("", "REMARKS", 12, 0);
/*      */     
/* 4814 */     ArrayList tuples = new ArrayList();
/*      */     
/* 4816 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/* 4818 */     String tableNamePat = tableNamePattern;
/*      */     
/* 4820 */     boolean operatingOnInformationSchema = "information_schema".equalsIgnoreCase(catalog);
/*      */ 
/*      */     
/*      */     try {
/* 4824 */       (new IterateBlock(this, getCatalogIterator(catalog), stmt, tableNamePat, types, operatingOnInformationSchema, tuples) { private final Statement val$stmt; private final String val$tableNamePat; private final String[] val$types; private final boolean val$operatingOnInformationSchema; private final ArrayList val$tuples; private final DatabaseMetaData this$0;
/*      */           void forEach(Object catalogStr) throws SQLException {
/* 4826 */             ResultSet results = null;
/*      */ 
/*      */             
/*      */             try {
/* 4830 */               if (!this.this$0.conn.versionMeetsMinimum(5, 0, 2)) {
/*      */                 try {
/* 4832 */                   results = this.val$stmt.executeQuery("SHOW TABLES FROM " + this.this$0.quotedId + catalogStr.toString() + this.this$0.quotedId + " LIKE '" + this.val$tableNamePat + "'");
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 4837 */                 catch (SQLException sqlEx) {
/* 4838 */                   if ("08S01".equals(sqlEx.getSQLState())) {
/* 4839 */                     throw sqlEx;
/*      */                   }
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               } else {
/*      */                 try {
/* 4846 */                   results = this.val$stmt.executeQuery("SHOW FULL TABLES FROM " + this.this$0.quotedId + catalogStr.toString() + this.this$0.quotedId + " LIKE '" + this.val$tableNamePat + "'");
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 4851 */                 catch (SQLException sqlEx) {
/* 4852 */                   if ("08S01".equals(sqlEx.getSQLState())) {
/* 4853 */                     throw sqlEx;
/*      */                   }
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */               } 
/*      */               
/* 4860 */               boolean shouldReportTables = false;
/* 4861 */               boolean shouldReportViews = false;
/* 4862 */               boolean shouldReportSystemTables = false;
/*      */               
/* 4864 */               if (this.val$types == null || this.val$types.length == 0) {
/* 4865 */                 shouldReportTables = true;
/* 4866 */                 shouldReportViews = true;
/* 4867 */                 shouldReportSystemTables = true;
/*      */               } else {
/* 4869 */                 for (int i = 0; i < this.val$types.length; i++) {
/* 4870 */                   if ("TABLE".equalsIgnoreCase(this.val$types[i])) {
/* 4871 */                     shouldReportTables = true;
/*      */                   }
/*      */                   
/* 4874 */                   if ("VIEW".equalsIgnoreCase(this.val$types[i])) {
/* 4875 */                     shouldReportViews = true;
/*      */                   }
/*      */                   
/* 4878 */                   if ("SYSTEM TABLE".equalsIgnoreCase(this.val$types[i])) {
/* 4879 */                     shouldReportSystemTables = true;
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */               
/* 4884 */               int typeColumnIndex = 0;
/* 4885 */               boolean hasTableTypes = false;
/*      */               
/* 4887 */               if (this.this$0.conn.versionMeetsMinimum(5, 0, 2)) {
/*      */                 
/*      */                 try {
/*      */ 
/*      */                   
/* 4892 */                   typeColumnIndex = results.findColumn("table_type");
/*      */                   
/* 4894 */                   hasTableTypes = true;
/* 4895 */                 } catch (SQLException sqlEx) {
/*      */ 
/*      */                   
/*      */                   try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 4906 */                     typeColumnIndex = results.findColumn("Type");
/*      */                     
/* 4908 */                     hasTableTypes = true;
/* 4909 */                   } catch (SQLException sqlEx2) {
/* 4910 */                     hasTableTypes = false;
/*      */                   } 
/*      */                 } 
/*      */               }
/*      */               
/* 4915 */               TreeMap tablesOrderedByName = null;
/* 4916 */               TreeMap viewsOrderedByName = null;
/*      */               
/* 4918 */               while (results.next()) {
/* 4919 */                 byte[][] row = new byte[5][];
/* 4920 */                 row[0] = (catalogStr.toString() == null) ? null : this.this$0.s2b(catalogStr.toString());
/*      */                 
/* 4922 */                 row[1] = null;
/* 4923 */                 row[2] = results.getBytes(1);
/* 4924 */                 row[4] = new byte[0];
/*      */                 
/* 4926 */                 if (hasTableTypes) {
/* 4927 */                   String tableType = results.getString(typeColumnIndex);
/*      */ 
/*      */                   
/* 4930 */                   if (("table".equalsIgnoreCase(tableType) || "base table".equalsIgnoreCase(tableType)) && shouldReportTables) {
/*      */ 
/*      */                     
/* 4933 */                     boolean reportTable = false;
/*      */                     
/* 4935 */                     if (!this.val$operatingOnInformationSchema && shouldReportTables) {
/* 4936 */                       row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/* 4937 */                       reportTable = true;
/* 4938 */                     } else if (this.val$operatingOnInformationSchema && shouldReportSystemTables) {
/* 4939 */                       row[3] = DatabaseMetaData.SYSTEM_TABLE_AS_BYTES;
/* 4940 */                       reportTable = true;
/*      */                     } 
/*      */                     
/* 4943 */                     if (reportTable) {
/* 4944 */                       if (tablesOrderedByName == null) {
/* 4945 */                         tablesOrderedByName = new TreeMap();
/*      */                       }
/*      */                       
/* 4948 */                       tablesOrderedByName.put(results.getString(1), row);
/*      */                     }  continue;
/*      */                   } 
/* 4951 */                   if ("system view".equalsIgnoreCase(tableType) && shouldReportSystemTables) {
/* 4952 */                     row[3] = DatabaseMetaData.SYSTEM_TABLE_AS_BYTES;
/*      */                     
/* 4954 */                     if (tablesOrderedByName == null) {
/* 4955 */                       tablesOrderedByName = new TreeMap();
/*      */                     }
/*      */                     
/* 4958 */                     tablesOrderedByName.put(results.getString(1), row); continue;
/*      */                   } 
/* 4960 */                   if ("view".equalsIgnoreCase(tableType) && shouldReportViews) {
/*      */                     
/* 4962 */                     row[3] = DatabaseMetaData.VIEW_AS_BYTES;
/*      */                     
/* 4964 */                     if (viewsOrderedByName == null) {
/* 4965 */                       viewsOrderedByName = new TreeMap();
/*      */                     }
/*      */                     
/* 4968 */                     viewsOrderedByName.put(results.getString(1), row); continue;
/*      */                   } 
/* 4970 */                   if (!hasTableTypes) {
/*      */                     
/* 4972 */                     row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/*      */                     
/* 4974 */                     if (tablesOrderedByName == null) {
/* 4975 */                       tablesOrderedByName = new TreeMap();
/*      */                     }
/*      */                     
/* 4978 */                     tablesOrderedByName.put(results.getString(1), row);
/*      */                   } 
/*      */                   continue;
/*      */                 } 
/* 4982 */                 if (shouldReportTables) {
/*      */                   
/* 4984 */                   row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/*      */                   
/* 4986 */                   if (tablesOrderedByName == null) {
/* 4987 */                     tablesOrderedByName = new TreeMap();
/*      */                   }
/*      */                   
/* 4990 */                   tablesOrderedByName.put(results.getString(1), row);
/*      */                 } 
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 4999 */               if (tablesOrderedByName != null) {
/* 5000 */                 Iterator tablesIter = tablesOrderedByName.values().iterator();
/*      */ 
/*      */                 
/* 5003 */                 while (tablesIter.hasNext()) {
/* 5004 */                   this.val$tuples.add(new ByteArrayRow(tablesIter.next(), this.this$0.getExceptionInterceptor()));
/*      */                 }
/*      */               } 
/*      */               
/* 5008 */               if (viewsOrderedByName != null) {
/* 5009 */                 Iterator viewsIter = viewsOrderedByName.values().iterator();
/*      */ 
/*      */                 
/* 5012 */                 while (viewsIter.hasNext()) {
/* 5013 */                   this.val$tuples.add(new ByteArrayRow(viewsIter.next(), this.this$0.getExceptionInterceptor()));
/*      */                 }
/*      */               } 
/*      */             } finally {
/*      */               
/* 5018 */               if (results != null) {
/*      */                 try {
/* 5020 */                   results.close();
/* 5021 */                 } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */                 
/* 5025 */                 results = null;
/*      */               } 
/*      */             } 
/*      */           } }
/*      */         ).doForAll();
/*      */     } finally {
/*      */       
/* 5032 */       if (stmt != null) {
/* 5033 */         stmt.close();
/*      */       }
/*      */     } 
/*      */     
/* 5037 */     ResultSet tables = buildResultSet(fields, tuples);
/*      */     
/* 5039 */     return tables;
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
/*      */   public ResultSet getTableTypes() throws SQLException {
/* 5060 */     ArrayList tuples = new ArrayList();
/* 5061 */     Field[] fields = new Field[1];
/* 5062 */     fields[0] = new Field("", "TABLE_TYPE", 12, 5);
/*      */     
/* 5064 */     byte[][] tableTypeRow = new byte[1][];
/* 5065 */     tableTypeRow[0] = TABLE_AS_BYTES;
/* 5066 */     tuples.add(new ByteArrayRow(tableTypeRow, getExceptionInterceptor()));
/*      */     
/* 5068 */     if (this.conn.versionMeetsMinimum(5, 0, 1)) {
/* 5069 */       byte[][] viewTypeRow = new byte[1][];
/* 5070 */       viewTypeRow[0] = VIEW_AS_BYTES;
/* 5071 */       tuples.add(new ByteArrayRow(viewTypeRow, getExceptionInterceptor()));
/*      */     } 
/*      */     
/* 5074 */     byte[][] tempTypeRow = new byte[1][];
/* 5075 */     tempTypeRow[0] = s2b("LOCAL TEMPORARY");
/* 5076 */     tuples.add(new ByteArrayRow(tempTypeRow, getExceptionInterceptor()));
/*      */     
/* 5078 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTimeDateFunctions() throws SQLException {
/* 5089 */     return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTypeInfo() throws SQLException {
/* 5198 */     Field[] fields = new Field[18];
/* 5199 */     fields[0] = new Field("", "TYPE_NAME", 1, 32);
/* 5200 */     fields[1] = new Field("", "DATA_TYPE", 4, 5);
/* 5201 */     fields[2] = new Field("", "PRECISION", 4, 10);
/* 5202 */     fields[3] = new Field("", "LITERAL_PREFIX", 1, 4);
/* 5203 */     fields[4] = new Field("", "LITERAL_SUFFIX", 1, 4);
/* 5204 */     fields[5] = new Field("", "CREATE_PARAMS", 1, 32);
/* 5205 */     fields[6] = new Field("", "NULLABLE", 5, 5);
/* 5206 */     fields[7] = new Field("", "CASE_SENSITIVE", 16, 3);
/* 5207 */     fields[8] = new Field("", "SEARCHABLE", 5, 3);
/* 5208 */     fields[9] = new Field("", "UNSIGNED_ATTRIBUTE", 16, 3);
/* 5209 */     fields[10] = new Field("", "FIXED_PREC_SCALE", 16, 3);
/* 5210 */     fields[11] = new Field("", "AUTO_INCREMENT", 16, 3);
/* 5211 */     fields[12] = new Field("", "LOCAL_TYPE_NAME", 1, 32);
/* 5212 */     fields[13] = new Field("", "MINIMUM_SCALE", 5, 5);
/* 5213 */     fields[14] = new Field("", "MAXIMUM_SCALE", 5, 5);
/* 5214 */     fields[15] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 5215 */     fields[16] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 5216 */     fields[17] = new Field("", "NUM_PREC_RADIX", 4, 10);
/*      */     
/* 5218 */     byte[][] rowVal = (byte[][])null;
/* 5219 */     ArrayList tuples = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5228 */     rowVal = new byte[18][];
/* 5229 */     rowVal[0] = s2b("BIT");
/* 5230 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */ 
/*      */     
/* 5233 */     rowVal[2] = s2b("1");
/* 5234 */     rowVal[3] = s2b("");
/* 5235 */     rowVal[4] = s2b("");
/* 5236 */     rowVal[5] = s2b("");
/* 5237 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5241 */     rowVal[7] = s2b("true");
/* 5242 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5246 */     rowVal[9] = s2b("false");
/* 5247 */     rowVal[10] = s2b("false");
/* 5248 */     rowVal[11] = s2b("false");
/* 5249 */     rowVal[12] = s2b("BIT");
/* 5250 */     rowVal[13] = s2b("0");
/* 5251 */     rowVal[14] = s2b("0");
/* 5252 */     rowVal[15] = s2b("0");
/* 5253 */     rowVal[16] = s2b("0");
/* 5254 */     rowVal[17] = s2b("10");
/* 5255 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5260 */     rowVal = new byte[18][];
/* 5261 */     rowVal[0] = s2b("BOOL");
/* 5262 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */ 
/*      */     
/* 5265 */     rowVal[2] = s2b("1");
/* 5266 */     rowVal[3] = s2b("");
/* 5267 */     rowVal[4] = s2b("");
/* 5268 */     rowVal[5] = s2b("");
/* 5269 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5273 */     rowVal[7] = s2b("true");
/* 5274 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5278 */     rowVal[9] = s2b("false");
/* 5279 */     rowVal[10] = s2b("false");
/* 5280 */     rowVal[11] = s2b("false");
/* 5281 */     rowVal[12] = s2b("BOOL");
/* 5282 */     rowVal[13] = s2b("0");
/* 5283 */     rowVal[14] = s2b("0");
/* 5284 */     rowVal[15] = s2b("0");
/* 5285 */     rowVal[16] = s2b("0");
/* 5286 */     rowVal[17] = s2b("10");
/* 5287 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5292 */     rowVal = new byte[18][];
/* 5293 */     rowVal[0] = s2b("TINYINT");
/* 5294 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */ 
/*      */     
/* 5297 */     rowVal[2] = s2b("3");
/* 5298 */     rowVal[3] = s2b("");
/* 5299 */     rowVal[4] = s2b("");
/* 5300 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5301 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5305 */     rowVal[7] = s2b("false");
/* 5306 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5310 */     rowVal[9] = s2b("true");
/* 5311 */     rowVal[10] = s2b("false");
/* 5312 */     rowVal[11] = s2b("true");
/* 5313 */     rowVal[12] = s2b("TINYINT");
/* 5314 */     rowVal[13] = s2b("0");
/* 5315 */     rowVal[14] = s2b("0");
/* 5316 */     rowVal[15] = s2b("0");
/* 5317 */     rowVal[16] = s2b("0");
/* 5318 */     rowVal[17] = s2b("10");
/* 5319 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 5321 */     rowVal = new byte[18][];
/* 5322 */     rowVal[0] = s2b("TINYINT UNSIGNED");
/* 5323 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */ 
/*      */     
/* 5326 */     rowVal[2] = s2b("3");
/* 5327 */     rowVal[3] = s2b("");
/* 5328 */     rowVal[4] = s2b("");
/* 5329 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5330 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5334 */     rowVal[7] = s2b("false");
/* 5335 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5339 */     rowVal[9] = s2b("true");
/* 5340 */     rowVal[10] = s2b("false");
/* 5341 */     rowVal[11] = s2b("true");
/* 5342 */     rowVal[12] = s2b("TINYINT UNSIGNED");
/* 5343 */     rowVal[13] = s2b("0");
/* 5344 */     rowVal[14] = s2b("0");
/* 5345 */     rowVal[15] = s2b("0");
/* 5346 */     rowVal[16] = s2b("0");
/* 5347 */     rowVal[17] = s2b("10");
/* 5348 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5353 */     rowVal = new byte[18][];
/* 5354 */     rowVal[0] = s2b("BIGINT");
/* 5355 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */ 
/*      */     
/* 5358 */     rowVal[2] = s2b("19");
/* 5359 */     rowVal[3] = s2b("");
/* 5360 */     rowVal[4] = s2b("");
/* 5361 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5362 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5366 */     rowVal[7] = s2b("false");
/* 5367 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5371 */     rowVal[9] = s2b("true");
/* 5372 */     rowVal[10] = s2b("false");
/* 5373 */     rowVal[11] = s2b("true");
/* 5374 */     rowVal[12] = s2b("BIGINT");
/* 5375 */     rowVal[13] = s2b("0");
/* 5376 */     rowVal[14] = s2b("0");
/* 5377 */     rowVal[15] = s2b("0");
/* 5378 */     rowVal[16] = s2b("0");
/* 5379 */     rowVal[17] = s2b("10");
/* 5380 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 5382 */     rowVal = new byte[18][];
/* 5383 */     rowVal[0] = s2b("BIGINT UNSIGNED");
/* 5384 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */ 
/*      */     
/* 5387 */     rowVal[2] = s2b("20");
/* 5388 */     rowVal[3] = s2b("");
/* 5389 */     rowVal[4] = s2b("");
/* 5390 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5391 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5395 */     rowVal[7] = s2b("false");
/* 5396 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5400 */     rowVal[9] = s2b("true");
/* 5401 */     rowVal[10] = s2b("false");
/* 5402 */     rowVal[11] = s2b("true");
/* 5403 */     rowVal[12] = s2b("BIGINT UNSIGNED");
/* 5404 */     rowVal[13] = s2b("0");
/* 5405 */     rowVal[14] = s2b("0");
/* 5406 */     rowVal[15] = s2b("0");
/* 5407 */     rowVal[16] = s2b("0");
/* 5408 */     rowVal[17] = s2b("10");
/* 5409 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5414 */     rowVal = new byte[18][];
/* 5415 */     rowVal[0] = s2b("LONG VARBINARY");
/* 5416 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */ 
/*      */     
/* 5419 */     rowVal[2] = s2b("16777215");
/* 5420 */     rowVal[3] = s2b("'");
/* 5421 */     rowVal[4] = s2b("'");
/* 5422 */     rowVal[5] = s2b("");
/* 5423 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5427 */     rowVal[7] = s2b("true");
/* 5428 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5432 */     rowVal[9] = s2b("false");
/* 5433 */     rowVal[10] = s2b("false");
/* 5434 */     rowVal[11] = s2b("false");
/* 5435 */     rowVal[12] = s2b("LONG VARBINARY");
/* 5436 */     rowVal[13] = s2b("0");
/* 5437 */     rowVal[14] = s2b("0");
/* 5438 */     rowVal[15] = s2b("0");
/* 5439 */     rowVal[16] = s2b("0");
/* 5440 */     rowVal[17] = s2b("10");
/* 5441 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5446 */     rowVal = new byte[18][];
/* 5447 */     rowVal[0] = s2b("MEDIUMBLOB");
/* 5448 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */ 
/*      */     
/* 5451 */     rowVal[2] = s2b("16777215");
/* 5452 */     rowVal[3] = s2b("'");
/* 5453 */     rowVal[4] = s2b("'");
/* 5454 */     rowVal[5] = s2b("");
/* 5455 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5459 */     rowVal[7] = s2b("true");
/* 5460 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5464 */     rowVal[9] = s2b("false");
/* 5465 */     rowVal[10] = s2b("false");
/* 5466 */     rowVal[11] = s2b("false");
/* 5467 */     rowVal[12] = s2b("MEDIUMBLOB");
/* 5468 */     rowVal[13] = s2b("0");
/* 5469 */     rowVal[14] = s2b("0");
/* 5470 */     rowVal[15] = s2b("0");
/* 5471 */     rowVal[16] = s2b("0");
/* 5472 */     rowVal[17] = s2b("10");
/* 5473 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5478 */     rowVal = new byte[18][];
/* 5479 */     rowVal[0] = s2b("LONGBLOB");
/* 5480 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */ 
/*      */     
/* 5483 */     rowVal[2] = Integer.toString(2147483647).getBytes();
/*      */ 
/*      */     
/* 5486 */     rowVal[3] = s2b("'");
/* 5487 */     rowVal[4] = s2b("'");
/* 5488 */     rowVal[5] = s2b("");
/* 5489 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5493 */     rowVal[7] = s2b("true");
/* 5494 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5498 */     rowVal[9] = s2b("false");
/* 5499 */     rowVal[10] = s2b("false");
/* 5500 */     rowVal[11] = s2b("false");
/* 5501 */     rowVal[12] = s2b("LONGBLOB");
/* 5502 */     rowVal[13] = s2b("0");
/* 5503 */     rowVal[14] = s2b("0");
/* 5504 */     rowVal[15] = s2b("0");
/* 5505 */     rowVal[16] = s2b("0");
/* 5506 */     rowVal[17] = s2b("10");
/* 5507 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5512 */     rowVal = new byte[18][];
/* 5513 */     rowVal[0] = s2b("BLOB");
/* 5514 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */ 
/*      */     
/* 5517 */     rowVal[2] = s2b("65535");
/* 5518 */     rowVal[3] = s2b("'");
/* 5519 */     rowVal[4] = s2b("'");
/* 5520 */     rowVal[5] = s2b("");
/* 5521 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5525 */     rowVal[7] = s2b("true");
/* 5526 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5530 */     rowVal[9] = s2b("false");
/* 5531 */     rowVal[10] = s2b("false");
/* 5532 */     rowVal[11] = s2b("false");
/* 5533 */     rowVal[12] = s2b("BLOB");
/* 5534 */     rowVal[13] = s2b("0");
/* 5535 */     rowVal[14] = s2b("0");
/* 5536 */     rowVal[15] = s2b("0");
/* 5537 */     rowVal[16] = s2b("0");
/* 5538 */     rowVal[17] = s2b("10");
/* 5539 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5544 */     rowVal = new byte[18][];
/* 5545 */     rowVal[0] = s2b("TINYBLOB");
/* 5546 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */ 
/*      */     
/* 5549 */     rowVal[2] = s2b("255");
/* 5550 */     rowVal[3] = s2b("'");
/* 5551 */     rowVal[4] = s2b("'");
/* 5552 */     rowVal[5] = s2b("");
/* 5553 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5557 */     rowVal[7] = s2b("true");
/* 5558 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5562 */     rowVal[9] = s2b("false");
/* 5563 */     rowVal[10] = s2b("false");
/* 5564 */     rowVal[11] = s2b("false");
/* 5565 */     rowVal[12] = s2b("TINYBLOB");
/* 5566 */     rowVal[13] = s2b("0");
/* 5567 */     rowVal[14] = s2b("0");
/* 5568 */     rowVal[15] = s2b("0");
/* 5569 */     rowVal[16] = s2b("0");
/* 5570 */     rowVal[17] = s2b("10");
/* 5571 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5577 */     rowVal = new byte[18][];
/* 5578 */     rowVal[0] = s2b("VARBINARY");
/* 5579 */     rowVal[1] = Integer.toString(-3).getBytes();
/*      */ 
/*      */     
/* 5582 */     rowVal[2] = s2b("255");
/* 5583 */     rowVal[3] = s2b("'");
/* 5584 */     rowVal[4] = s2b("'");
/* 5585 */     rowVal[5] = s2b("(M)");
/* 5586 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5590 */     rowVal[7] = s2b("true");
/* 5591 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5595 */     rowVal[9] = s2b("false");
/* 5596 */     rowVal[10] = s2b("false");
/* 5597 */     rowVal[11] = s2b("false");
/* 5598 */     rowVal[12] = s2b("VARBINARY");
/* 5599 */     rowVal[13] = s2b("0");
/* 5600 */     rowVal[14] = s2b("0");
/* 5601 */     rowVal[15] = s2b("0");
/* 5602 */     rowVal[16] = s2b("0");
/* 5603 */     rowVal[17] = s2b("10");
/* 5604 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5610 */     rowVal = new byte[18][];
/* 5611 */     rowVal[0] = s2b("BINARY");
/* 5612 */     rowVal[1] = Integer.toString(-2).getBytes();
/*      */ 
/*      */     
/* 5615 */     rowVal[2] = s2b("255");
/* 5616 */     rowVal[3] = s2b("'");
/* 5617 */     rowVal[4] = s2b("'");
/* 5618 */     rowVal[5] = s2b("(M)");
/* 5619 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5623 */     rowVal[7] = s2b("true");
/* 5624 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5628 */     rowVal[9] = s2b("false");
/* 5629 */     rowVal[10] = s2b("false");
/* 5630 */     rowVal[11] = s2b("false");
/* 5631 */     rowVal[12] = s2b("BINARY");
/* 5632 */     rowVal[13] = s2b("0");
/* 5633 */     rowVal[14] = s2b("0");
/* 5634 */     rowVal[15] = s2b("0");
/* 5635 */     rowVal[16] = s2b("0");
/* 5636 */     rowVal[17] = s2b("10");
/* 5637 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5642 */     rowVal = new byte[18][];
/* 5643 */     rowVal[0] = s2b("LONG VARCHAR");
/* 5644 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */ 
/*      */     
/* 5647 */     rowVal[2] = s2b("16777215");
/* 5648 */     rowVal[3] = s2b("'");
/* 5649 */     rowVal[4] = s2b("'");
/* 5650 */     rowVal[5] = s2b("");
/* 5651 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5655 */     rowVal[7] = s2b("false");
/* 5656 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5660 */     rowVal[9] = s2b("false");
/* 5661 */     rowVal[10] = s2b("false");
/* 5662 */     rowVal[11] = s2b("false");
/* 5663 */     rowVal[12] = s2b("LONG VARCHAR");
/* 5664 */     rowVal[13] = s2b("0");
/* 5665 */     rowVal[14] = s2b("0");
/* 5666 */     rowVal[15] = s2b("0");
/* 5667 */     rowVal[16] = s2b("0");
/* 5668 */     rowVal[17] = s2b("10");
/* 5669 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5674 */     rowVal = new byte[18][];
/* 5675 */     rowVal[0] = s2b("MEDIUMTEXT");
/* 5676 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */ 
/*      */     
/* 5679 */     rowVal[2] = s2b("16777215");
/* 5680 */     rowVal[3] = s2b("'");
/* 5681 */     rowVal[4] = s2b("'");
/* 5682 */     rowVal[5] = s2b("");
/* 5683 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5687 */     rowVal[7] = s2b("false");
/* 5688 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5692 */     rowVal[9] = s2b("false");
/* 5693 */     rowVal[10] = s2b("false");
/* 5694 */     rowVal[11] = s2b("false");
/* 5695 */     rowVal[12] = s2b("MEDIUMTEXT");
/* 5696 */     rowVal[13] = s2b("0");
/* 5697 */     rowVal[14] = s2b("0");
/* 5698 */     rowVal[15] = s2b("0");
/* 5699 */     rowVal[16] = s2b("0");
/* 5700 */     rowVal[17] = s2b("10");
/* 5701 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5706 */     rowVal = new byte[18][];
/* 5707 */     rowVal[0] = s2b("LONGTEXT");
/* 5708 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */ 
/*      */     
/* 5711 */     rowVal[2] = Integer.toString(2147483647).getBytes();
/*      */ 
/*      */     
/* 5714 */     rowVal[3] = s2b("'");
/* 5715 */     rowVal[4] = s2b("'");
/* 5716 */     rowVal[5] = s2b("");
/* 5717 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5721 */     rowVal[7] = s2b("false");
/* 5722 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5726 */     rowVal[9] = s2b("false");
/* 5727 */     rowVal[10] = s2b("false");
/* 5728 */     rowVal[11] = s2b("false");
/* 5729 */     rowVal[12] = s2b("LONGTEXT");
/* 5730 */     rowVal[13] = s2b("0");
/* 5731 */     rowVal[14] = s2b("0");
/* 5732 */     rowVal[15] = s2b("0");
/* 5733 */     rowVal[16] = s2b("0");
/* 5734 */     rowVal[17] = s2b("10");
/* 5735 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5740 */     rowVal = new byte[18][];
/* 5741 */     rowVal[0] = s2b("TEXT");
/* 5742 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */ 
/*      */     
/* 5745 */     rowVal[2] = s2b("65535");
/* 5746 */     rowVal[3] = s2b("'");
/* 5747 */     rowVal[4] = s2b("'");
/* 5748 */     rowVal[5] = s2b("");
/* 5749 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5753 */     rowVal[7] = s2b("false");
/* 5754 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5758 */     rowVal[9] = s2b("false");
/* 5759 */     rowVal[10] = s2b("false");
/* 5760 */     rowVal[11] = s2b("false");
/* 5761 */     rowVal[12] = s2b("TEXT");
/* 5762 */     rowVal[13] = s2b("0");
/* 5763 */     rowVal[14] = s2b("0");
/* 5764 */     rowVal[15] = s2b("0");
/* 5765 */     rowVal[16] = s2b("0");
/* 5766 */     rowVal[17] = s2b("10");
/* 5767 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5772 */     rowVal = new byte[18][];
/* 5773 */     rowVal[0] = s2b("TINYTEXT");
/* 5774 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */ 
/*      */     
/* 5777 */     rowVal[2] = s2b("255");
/* 5778 */     rowVal[3] = s2b("'");
/* 5779 */     rowVal[4] = s2b("'");
/* 5780 */     rowVal[5] = s2b("");
/* 5781 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5785 */     rowVal[7] = s2b("false");
/* 5786 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5790 */     rowVal[9] = s2b("false");
/* 5791 */     rowVal[10] = s2b("false");
/* 5792 */     rowVal[11] = s2b("false");
/* 5793 */     rowVal[12] = s2b("TINYTEXT");
/* 5794 */     rowVal[13] = s2b("0");
/* 5795 */     rowVal[14] = s2b("0");
/* 5796 */     rowVal[15] = s2b("0");
/* 5797 */     rowVal[16] = s2b("0");
/* 5798 */     rowVal[17] = s2b("10");
/* 5799 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5804 */     rowVal = new byte[18][];
/* 5805 */     rowVal[0] = s2b("CHAR");
/* 5806 */     rowVal[1] = Integer.toString(1).getBytes();
/*      */ 
/*      */     
/* 5809 */     rowVal[2] = s2b("255");
/* 5810 */     rowVal[3] = s2b("'");
/* 5811 */     rowVal[4] = s2b("'");
/* 5812 */     rowVal[5] = s2b("(M)");
/* 5813 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5817 */     rowVal[7] = s2b("false");
/* 5818 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5822 */     rowVal[9] = s2b("false");
/* 5823 */     rowVal[10] = s2b("false");
/* 5824 */     rowVal[11] = s2b("false");
/* 5825 */     rowVal[12] = s2b("CHAR");
/* 5826 */     rowVal[13] = s2b("0");
/* 5827 */     rowVal[14] = s2b("0");
/* 5828 */     rowVal[15] = s2b("0");
/* 5829 */     rowVal[16] = s2b("0");
/* 5830 */     rowVal[17] = s2b("10");
/* 5831 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */     
/* 5835 */     int decimalPrecision = 254;
/*      */     
/* 5837 */     if (this.conn.versionMeetsMinimum(5, 0, 3)) {
/* 5838 */       if (this.conn.versionMeetsMinimum(5, 0, 6)) {
/* 5839 */         decimalPrecision = 65;
/*      */       } else {
/* 5841 */         decimalPrecision = 64;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5849 */     rowVal = new byte[18][];
/* 5850 */     rowVal[0] = s2b("NUMERIC");
/* 5851 */     rowVal[1] = Integer.toString(2).getBytes();
/*      */ 
/*      */     
/* 5854 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 5855 */     rowVal[3] = s2b("");
/* 5856 */     rowVal[4] = s2b("");
/* 5857 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 5858 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5862 */     rowVal[7] = s2b("false");
/* 5863 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5867 */     rowVal[9] = s2b("false");
/* 5868 */     rowVal[10] = s2b("false");
/* 5869 */     rowVal[11] = s2b("true");
/* 5870 */     rowVal[12] = s2b("NUMERIC");
/* 5871 */     rowVal[13] = s2b("-308");
/* 5872 */     rowVal[14] = s2b("308");
/* 5873 */     rowVal[15] = s2b("0");
/* 5874 */     rowVal[16] = s2b("0");
/* 5875 */     rowVal[17] = s2b("10");
/* 5876 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5881 */     rowVal = new byte[18][];
/* 5882 */     rowVal[0] = s2b("DECIMAL");
/* 5883 */     rowVal[1] = Integer.toString(3).getBytes();
/*      */ 
/*      */     
/* 5886 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 5887 */     rowVal[3] = s2b("");
/* 5888 */     rowVal[4] = s2b("");
/* 5889 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 5890 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5894 */     rowVal[7] = s2b("false");
/* 5895 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5899 */     rowVal[9] = s2b("false");
/* 5900 */     rowVal[10] = s2b("false");
/* 5901 */     rowVal[11] = s2b("true");
/* 5902 */     rowVal[12] = s2b("DECIMAL");
/* 5903 */     rowVal[13] = s2b("-308");
/* 5904 */     rowVal[14] = s2b("308");
/* 5905 */     rowVal[15] = s2b("0");
/* 5906 */     rowVal[16] = s2b("0");
/* 5907 */     rowVal[17] = s2b("10");
/* 5908 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5913 */     rowVal = new byte[18][];
/* 5914 */     rowVal[0] = s2b("INTEGER");
/* 5915 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 5918 */     rowVal[2] = s2b("10");
/* 5919 */     rowVal[3] = s2b("");
/* 5920 */     rowVal[4] = s2b("");
/* 5921 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5922 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5926 */     rowVal[7] = s2b("false");
/* 5927 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5931 */     rowVal[9] = s2b("true");
/* 5932 */     rowVal[10] = s2b("false");
/* 5933 */     rowVal[11] = s2b("true");
/* 5934 */     rowVal[12] = s2b("INTEGER");
/* 5935 */     rowVal[13] = s2b("0");
/* 5936 */     rowVal[14] = s2b("0");
/* 5937 */     rowVal[15] = s2b("0");
/* 5938 */     rowVal[16] = s2b("0");
/* 5939 */     rowVal[17] = s2b("10");
/* 5940 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 5942 */     rowVal = new byte[18][];
/* 5943 */     rowVal[0] = s2b("INTEGER UNSIGNED");
/* 5944 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 5947 */     rowVal[2] = s2b("10");
/* 5948 */     rowVal[3] = s2b("");
/* 5949 */     rowVal[4] = s2b("");
/* 5950 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5951 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5955 */     rowVal[7] = s2b("false");
/* 5956 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5960 */     rowVal[9] = s2b("true");
/* 5961 */     rowVal[10] = s2b("false");
/* 5962 */     rowVal[11] = s2b("true");
/* 5963 */     rowVal[12] = s2b("INTEGER UNSIGNED");
/* 5964 */     rowVal[13] = s2b("0");
/* 5965 */     rowVal[14] = s2b("0");
/* 5966 */     rowVal[15] = s2b("0");
/* 5967 */     rowVal[16] = s2b("0");
/* 5968 */     rowVal[17] = s2b("10");
/* 5969 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5974 */     rowVal = new byte[18][];
/* 5975 */     rowVal[0] = s2b("INT");
/* 5976 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 5979 */     rowVal[2] = s2b("10");
/* 5980 */     rowVal[3] = s2b("");
/* 5981 */     rowVal[4] = s2b("");
/* 5982 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5983 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5987 */     rowVal[7] = s2b("false");
/* 5988 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 5992 */     rowVal[9] = s2b("true");
/* 5993 */     rowVal[10] = s2b("false");
/* 5994 */     rowVal[11] = s2b("true");
/* 5995 */     rowVal[12] = s2b("INT");
/* 5996 */     rowVal[13] = s2b("0");
/* 5997 */     rowVal[14] = s2b("0");
/* 5998 */     rowVal[15] = s2b("0");
/* 5999 */     rowVal[16] = s2b("0");
/* 6000 */     rowVal[17] = s2b("10");
/* 6001 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6003 */     rowVal = new byte[18][];
/* 6004 */     rowVal[0] = s2b("INT UNSIGNED");
/* 6005 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 6008 */     rowVal[2] = s2b("10");
/* 6009 */     rowVal[3] = s2b("");
/* 6010 */     rowVal[4] = s2b("");
/* 6011 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6012 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6016 */     rowVal[7] = s2b("false");
/* 6017 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6021 */     rowVal[9] = s2b("true");
/* 6022 */     rowVal[10] = s2b("false");
/* 6023 */     rowVal[11] = s2b("true");
/* 6024 */     rowVal[12] = s2b("INT UNSIGNED");
/* 6025 */     rowVal[13] = s2b("0");
/* 6026 */     rowVal[14] = s2b("0");
/* 6027 */     rowVal[15] = s2b("0");
/* 6028 */     rowVal[16] = s2b("0");
/* 6029 */     rowVal[17] = s2b("10");
/* 6030 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6035 */     rowVal = new byte[18][];
/* 6036 */     rowVal[0] = s2b("MEDIUMINT");
/* 6037 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 6040 */     rowVal[2] = s2b("7");
/* 6041 */     rowVal[3] = s2b("");
/* 6042 */     rowVal[4] = s2b("");
/* 6043 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6044 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6048 */     rowVal[7] = s2b("false");
/* 6049 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6053 */     rowVal[9] = s2b("true");
/* 6054 */     rowVal[10] = s2b("false");
/* 6055 */     rowVal[11] = s2b("true");
/* 6056 */     rowVal[12] = s2b("MEDIUMINT");
/* 6057 */     rowVal[13] = s2b("0");
/* 6058 */     rowVal[14] = s2b("0");
/* 6059 */     rowVal[15] = s2b("0");
/* 6060 */     rowVal[16] = s2b("0");
/* 6061 */     rowVal[17] = s2b("10");
/* 6062 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6064 */     rowVal = new byte[18][];
/* 6065 */     rowVal[0] = s2b("MEDIUMINT UNSIGNED");
/* 6066 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */ 
/*      */     
/* 6069 */     rowVal[2] = s2b("8");
/* 6070 */     rowVal[3] = s2b("");
/* 6071 */     rowVal[4] = s2b("");
/* 6072 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6073 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6077 */     rowVal[7] = s2b("false");
/* 6078 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6082 */     rowVal[9] = s2b("true");
/* 6083 */     rowVal[10] = s2b("false");
/* 6084 */     rowVal[11] = s2b("true");
/* 6085 */     rowVal[12] = s2b("MEDIUMINT UNSIGNED");
/* 6086 */     rowVal[13] = s2b("0");
/* 6087 */     rowVal[14] = s2b("0");
/* 6088 */     rowVal[15] = s2b("0");
/* 6089 */     rowVal[16] = s2b("0");
/* 6090 */     rowVal[17] = s2b("10");
/* 6091 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6096 */     rowVal = new byte[18][];
/* 6097 */     rowVal[0] = s2b("SMALLINT");
/* 6098 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */ 
/*      */     
/* 6101 */     rowVal[2] = s2b("5");
/* 6102 */     rowVal[3] = s2b("");
/* 6103 */     rowVal[4] = s2b("");
/* 6104 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6105 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6109 */     rowVal[7] = s2b("false");
/* 6110 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6114 */     rowVal[9] = s2b("true");
/* 6115 */     rowVal[10] = s2b("false");
/* 6116 */     rowVal[11] = s2b("true");
/* 6117 */     rowVal[12] = s2b("SMALLINT");
/* 6118 */     rowVal[13] = s2b("0");
/* 6119 */     rowVal[14] = s2b("0");
/* 6120 */     rowVal[15] = s2b("0");
/* 6121 */     rowVal[16] = s2b("0");
/* 6122 */     rowVal[17] = s2b("10");
/* 6123 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6125 */     rowVal = new byte[18][];
/* 6126 */     rowVal[0] = s2b("SMALLINT UNSIGNED");
/* 6127 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */ 
/*      */     
/* 6130 */     rowVal[2] = s2b("5");
/* 6131 */     rowVal[3] = s2b("");
/* 6132 */     rowVal[4] = s2b("");
/* 6133 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6134 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6138 */     rowVal[7] = s2b("false");
/* 6139 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6143 */     rowVal[9] = s2b("true");
/* 6144 */     rowVal[10] = s2b("false");
/* 6145 */     rowVal[11] = s2b("true");
/* 6146 */     rowVal[12] = s2b("SMALLINT UNSIGNED");
/* 6147 */     rowVal[13] = s2b("0");
/* 6148 */     rowVal[14] = s2b("0");
/* 6149 */     rowVal[15] = s2b("0");
/* 6150 */     rowVal[16] = s2b("0");
/* 6151 */     rowVal[17] = s2b("10");
/* 6152 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6158 */     rowVal = new byte[18][];
/* 6159 */     rowVal[0] = s2b("FLOAT");
/* 6160 */     rowVal[1] = Integer.toString(7).getBytes();
/*      */ 
/*      */     
/* 6163 */     rowVal[2] = s2b("10");
/* 6164 */     rowVal[3] = s2b("");
/* 6165 */     rowVal[4] = s2b("");
/* 6166 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6167 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6171 */     rowVal[7] = s2b("false");
/* 6172 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6176 */     rowVal[9] = s2b("false");
/* 6177 */     rowVal[10] = s2b("false");
/* 6178 */     rowVal[11] = s2b("true");
/* 6179 */     rowVal[12] = s2b("FLOAT");
/* 6180 */     rowVal[13] = s2b("-38");
/* 6181 */     rowVal[14] = s2b("38");
/* 6182 */     rowVal[15] = s2b("0");
/* 6183 */     rowVal[16] = s2b("0");
/* 6184 */     rowVal[17] = s2b("10");
/* 6185 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6190 */     rowVal = new byte[18][];
/* 6191 */     rowVal[0] = s2b("DOUBLE");
/* 6192 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */ 
/*      */     
/* 6195 */     rowVal[2] = s2b("17");
/* 6196 */     rowVal[3] = s2b("");
/* 6197 */     rowVal[4] = s2b("");
/* 6198 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6199 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6203 */     rowVal[7] = s2b("false");
/* 6204 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6208 */     rowVal[9] = s2b("false");
/* 6209 */     rowVal[10] = s2b("false");
/* 6210 */     rowVal[11] = s2b("true");
/* 6211 */     rowVal[12] = s2b("DOUBLE");
/* 6212 */     rowVal[13] = s2b("-308");
/* 6213 */     rowVal[14] = s2b("308");
/* 6214 */     rowVal[15] = s2b("0");
/* 6215 */     rowVal[16] = s2b("0");
/* 6216 */     rowVal[17] = s2b("10");
/* 6217 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6222 */     rowVal = new byte[18][];
/* 6223 */     rowVal[0] = s2b("DOUBLE PRECISION");
/* 6224 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */ 
/*      */     
/* 6227 */     rowVal[2] = s2b("17");
/* 6228 */     rowVal[3] = s2b("");
/* 6229 */     rowVal[4] = s2b("");
/* 6230 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6231 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6235 */     rowVal[7] = s2b("false");
/* 6236 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6240 */     rowVal[9] = s2b("false");
/* 6241 */     rowVal[10] = s2b("false");
/* 6242 */     rowVal[11] = s2b("true");
/* 6243 */     rowVal[12] = s2b("DOUBLE PRECISION");
/* 6244 */     rowVal[13] = s2b("-308");
/* 6245 */     rowVal[14] = s2b("308");
/* 6246 */     rowVal[15] = s2b("0");
/* 6247 */     rowVal[16] = s2b("0");
/* 6248 */     rowVal[17] = s2b("10");
/* 6249 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6254 */     rowVal = new byte[18][];
/* 6255 */     rowVal[0] = s2b("REAL");
/* 6256 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */ 
/*      */     
/* 6259 */     rowVal[2] = s2b("17");
/* 6260 */     rowVal[3] = s2b("");
/* 6261 */     rowVal[4] = s2b("");
/* 6262 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6263 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6267 */     rowVal[7] = s2b("false");
/* 6268 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6272 */     rowVal[9] = s2b("false");
/* 6273 */     rowVal[10] = s2b("false");
/* 6274 */     rowVal[11] = s2b("true");
/* 6275 */     rowVal[12] = s2b("REAL");
/* 6276 */     rowVal[13] = s2b("-308");
/* 6277 */     rowVal[14] = s2b("308");
/* 6278 */     rowVal[15] = s2b("0");
/* 6279 */     rowVal[16] = s2b("0");
/* 6280 */     rowVal[17] = s2b("10");
/* 6281 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6286 */     rowVal = new byte[18][];
/* 6287 */     rowVal[0] = s2b("VARCHAR");
/* 6288 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */ 
/*      */     
/* 6291 */     rowVal[2] = s2b("255");
/* 6292 */     rowVal[3] = s2b("'");
/* 6293 */     rowVal[4] = s2b("'");
/* 6294 */     rowVal[5] = s2b("(M)");
/* 6295 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6299 */     rowVal[7] = s2b("false");
/* 6300 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6304 */     rowVal[9] = s2b("false");
/* 6305 */     rowVal[10] = s2b("false");
/* 6306 */     rowVal[11] = s2b("false");
/* 6307 */     rowVal[12] = s2b("VARCHAR");
/* 6308 */     rowVal[13] = s2b("0");
/* 6309 */     rowVal[14] = s2b("0");
/* 6310 */     rowVal[15] = s2b("0");
/* 6311 */     rowVal[16] = s2b("0");
/* 6312 */     rowVal[17] = s2b("10");
/* 6313 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6318 */     rowVal = new byte[18][];
/* 6319 */     rowVal[0] = s2b("ENUM");
/* 6320 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */ 
/*      */     
/* 6323 */     rowVal[2] = s2b("65535");
/* 6324 */     rowVal[3] = s2b("'");
/* 6325 */     rowVal[4] = s2b("'");
/* 6326 */     rowVal[5] = s2b("");
/* 6327 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6331 */     rowVal[7] = s2b("false");
/* 6332 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6336 */     rowVal[9] = s2b("false");
/* 6337 */     rowVal[10] = s2b("false");
/* 6338 */     rowVal[11] = s2b("false");
/* 6339 */     rowVal[12] = s2b("ENUM");
/* 6340 */     rowVal[13] = s2b("0");
/* 6341 */     rowVal[14] = s2b("0");
/* 6342 */     rowVal[15] = s2b("0");
/* 6343 */     rowVal[16] = s2b("0");
/* 6344 */     rowVal[17] = s2b("10");
/* 6345 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6350 */     rowVal = new byte[18][];
/* 6351 */     rowVal[0] = s2b("SET");
/* 6352 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */ 
/*      */     
/* 6355 */     rowVal[2] = s2b("64");
/* 6356 */     rowVal[3] = s2b("'");
/* 6357 */     rowVal[4] = s2b("'");
/* 6358 */     rowVal[5] = s2b("");
/* 6359 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6363 */     rowVal[7] = s2b("false");
/* 6364 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6368 */     rowVal[9] = s2b("false");
/* 6369 */     rowVal[10] = s2b("false");
/* 6370 */     rowVal[11] = s2b("false");
/* 6371 */     rowVal[12] = s2b("SET");
/* 6372 */     rowVal[13] = s2b("0");
/* 6373 */     rowVal[14] = s2b("0");
/* 6374 */     rowVal[15] = s2b("0");
/* 6375 */     rowVal[16] = s2b("0");
/* 6376 */     rowVal[17] = s2b("10");
/* 6377 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6382 */     rowVal = new byte[18][];
/* 6383 */     rowVal[0] = s2b("DATE");
/* 6384 */     rowVal[1] = Integer.toString(91).getBytes();
/*      */ 
/*      */     
/* 6387 */     rowVal[2] = s2b("0");
/* 6388 */     rowVal[3] = s2b("'");
/* 6389 */     rowVal[4] = s2b("'");
/* 6390 */     rowVal[5] = s2b("");
/* 6391 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6395 */     rowVal[7] = s2b("false");
/* 6396 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6400 */     rowVal[9] = s2b("false");
/* 6401 */     rowVal[10] = s2b("false");
/* 6402 */     rowVal[11] = s2b("false");
/* 6403 */     rowVal[12] = s2b("DATE");
/* 6404 */     rowVal[13] = s2b("0");
/* 6405 */     rowVal[14] = s2b("0");
/* 6406 */     rowVal[15] = s2b("0");
/* 6407 */     rowVal[16] = s2b("0");
/* 6408 */     rowVal[17] = s2b("10");
/* 6409 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6414 */     rowVal = new byte[18][];
/* 6415 */     rowVal[0] = s2b("TIME");
/* 6416 */     rowVal[1] = Integer.toString(92).getBytes();
/*      */ 
/*      */     
/* 6419 */     rowVal[2] = s2b("0");
/* 6420 */     rowVal[3] = s2b("'");
/* 6421 */     rowVal[4] = s2b("'");
/* 6422 */     rowVal[5] = s2b("");
/* 6423 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6427 */     rowVal[7] = s2b("false");
/* 6428 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6432 */     rowVal[9] = s2b("false");
/* 6433 */     rowVal[10] = s2b("false");
/* 6434 */     rowVal[11] = s2b("false");
/* 6435 */     rowVal[12] = s2b("TIME");
/* 6436 */     rowVal[13] = s2b("0");
/* 6437 */     rowVal[14] = s2b("0");
/* 6438 */     rowVal[15] = s2b("0");
/* 6439 */     rowVal[16] = s2b("0");
/* 6440 */     rowVal[17] = s2b("10");
/* 6441 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6446 */     rowVal = new byte[18][];
/* 6447 */     rowVal[0] = s2b("DATETIME");
/* 6448 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */ 
/*      */     
/* 6451 */     rowVal[2] = s2b("0");
/* 6452 */     rowVal[3] = s2b("'");
/* 6453 */     rowVal[4] = s2b("'");
/* 6454 */     rowVal[5] = s2b("");
/* 6455 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6459 */     rowVal[7] = s2b("false");
/* 6460 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6464 */     rowVal[9] = s2b("false");
/* 6465 */     rowVal[10] = s2b("false");
/* 6466 */     rowVal[11] = s2b("false");
/* 6467 */     rowVal[12] = s2b("DATETIME");
/* 6468 */     rowVal[13] = s2b("0");
/* 6469 */     rowVal[14] = s2b("0");
/* 6470 */     rowVal[15] = s2b("0");
/* 6471 */     rowVal[16] = s2b("0");
/* 6472 */     rowVal[17] = s2b("10");
/* 6473 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6478 */     rowVal = new byte[18][];
/* 6479 */     rowVal[0] = s2b("TIMESTAMP");
/* 6480 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */ 
/*      */     
/* 6483 */     rowVal[2] = s2b("0");
/* 6484 */     rowVal[3] = s2b("'");
/* 6485 */     rowVal[4] = s2b("'");
/* 6486 */     rowVal[5] = s2b("[(M)]");
/* 6487 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6491 */     rowVal[7] = s2b("false");
/* 6492 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */ 
/*      */ 
/*      */     
/* 6496 */     rowVal[9] = s2b("false");
/* 6497 */     rowVal[10] = s2b("false");
/* 6498 */     rowVal[11] = s2b("false");
/* 6499 */     rowVal[12] = s2b("TIMESTAMP");
/* 6500 */     rowVal[13] = s2b("0");
/* 6501 */     rowVal[14] = s2b("0");
/* 6502 */     rowVal[15] = s2b("0");
/* 6503 */     rowVal[16] = s2b("0");
/* 6504 */     rowVal[17] = s2b("10");
/* 6505 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6507 */     return buildResultSet(fields, tuples);
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
/*      */   public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
/* 6553 */     Field[] fields = new Field[6];
/* 6554 */     fields[0] = new Field("", "TYPE_CAT", 12, 32);
/* 6555 */     fields[1] = new Field("", "TYPE_SCHEM", 12, 32);
/* 6556 */     fields[2] = new Field("", "TYPE_NAME", 12, 32);
/* 6557 */     fields[3] = new Field("", "CLASS_NAME", 12, 32);
/* 6558 */     fields[4] = new Field("", "DATA_TYPE", 12, 32);
/* 6559 */     fields[5] = new Field("", "REMARKS", 12, 32);
/*      */     
/* 6561 */     ArrayList tuples = new ArrayList();
/*      */     
/* 6563 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getURL() throws SQLException {
/* 6574 */     return this.conn.getURL();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserName() throws SQLException {
/* 6585 */     if (this.conn.getUseHostsInPrivileges()) {
/* 6586 */       Statement stmt = null;
/* 6587 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 6590 */         stmt = this.conn.createStatement();
/* 6591 */         stmt.setEscapeProcessing(false);
/*      */         
/* 6593 */         rs = stmt.executeQuery("SELECT USER()");
/* 6594 */         rs.next();
/*      */         
/* 6596 */         return rs.getString(1);
/*      */       } finally {
/* 6598 */         if (rs != null) {
/*      */           try {
/* 6600 */             rs.close();
/* 6601 */           } catch (Exception ex) {
/* 6602 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           } 
/*      */           
/* 6605 */           rs = null;
/*      */         } 
/*      */         
/* 6608 */         if (stmt != null) {
/*      */           try {
/* 6610 */             stmt.close();
/* 6611 */           } catch (Exception ex) {
/* 6612 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           } 
/*      */           
/* 6615 */           stmt = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 6620 */     return this.conn.getUser();
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
/*      */   public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
/* 6659 */     Field[] fields = new Field[8];
/* 6660 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 6661 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 6662 */     fields[2] = new Field("", "DATA_TYPE", 4, 5);
/* 6663 */     fields[3] = new Field("", "TYPE_NAME", 1, 16);
/* 6664 */     fields[4] = new Field("", "COLUMN_SIZE", 4, 16);
/* 6665 */     fields[5] = new Field("", "BUFFER_LENGTH", 4, 16);
/* 6666 */     fields[6] = new Field("", "DECIMAL_DIGITS", 5, 16);
/* 6667 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 6669 */     return buildResultSet(fields, new ArrayList());
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
/*      */   public boolean insertsAreDetected(int type) throws SQLException {
/* 6685 */     return false;
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
/*      */   public boolean isCatalogAtStart() throws SQLException {
/* 6697 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/* 6708 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean locatorsUpdateCopy() throws SQLException {
/* 6715 */     return !this.conn.getEmulateLocators();
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
/*      */   public boolean nullPlusNonNullIsNull() throws SQLException {
/* 6727 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtEnd() throws SQLException {
/* 6738 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtStart() throws SQLException {
/* 6749 */     return (this.conn.versionMeetsMinimum(4, 0, 2) && !this.conn.versionMeetsMinimum(4, 0, 11));
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
/*      */   public boolean nullsAreSortedHigh() throws SQLException {
/* 6761 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedLow() throws SQLException {
/* 6772 */     return !nullsAreSortedHigh();
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
/*      */   public boolean othersDeletesAreVisible(int type) throws SQLException {
/* 6785 */     return false;
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
/*      */   public boolean othersInsertsAreVisible(int type) throws SQLException {
/* 6798 */     return false;
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
/*      */   public boolean othersUpdatesAreVisible(int type) throws SQLException {
/* 6811 */     return false;
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
/*      */   public boolean ownDeletesAreVisible(int type) throws SQLException {
/* 6824 */     return false;
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
/*      */   public boolean ownInsertsAreVisible(int type) throws SQLException {
/* 6837 */     return false;
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
/*      */   public boolean ownUpdatesAreVisible(int type) throws SQLException {
/* 6850 */     return false;
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
/*      */   private LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String keysComment) throws SQLException {
/* 6871 */     String columnsDelimitter = ",";
/*      */     
/* 6873 */     char quoteChar = (this.quotedId.length() == 0) ? Character.MIN_VALUE : this.quotedId.charAt(0);
/*      */ 
/*      */     
/* 6876 */     int indexOfOpenParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysComment, "(", quoteChar, true);
/*      */ 
/*      */ 
/*      */     
/* 6880 */     if (indexOfOpenParenLocalColumns == -1) {
/* 6881 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6886 */     String constraintName = removeQuotedId(keysComment.substring(0, indexOfOpenParenLocalColumns).trim());
/*      */     
/* 6888 */     keysComment = keysComment.substring(indexOfOpenParenLocalColumns, keysComment.length());
/*      */ 
/*      */     
/* 6891 */     String keysCommentTrimmed = keysComment.trim();
/*      */     
/* 6893 */     int indexOfCloseParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, ")", quoteChar, true);
/*      */ 
/*      */ 
/*      */     
/* 6897 */     if (indexOfCloseParenLocalColumns == -1) {
/* 6898 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6903 */     String localColumnNamesString = keysCommentTrimmed.substring(1, indexOfCloseParenLocalColumns);
/*      */ 
/*      */     
/* 6906 */     int indexOfRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, "REFER ", this.quotedId.charAt(0), true);
/*      */ 
/*      */     
/* 6909 */     if (indexOfRefer == -1) {
/* 6910 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6915 */     int indexOfOpenParenReferCol = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfRefer, keysCommentTrimmed, "(", quoteChar, false);
/*      */ 
/*      */ 
/*      */     
/* 6919 */     if (indexOfOpenParenReferCol == -1) {
/* 6920 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6925 */     String referCatalogTableString = keysCommentTrimmed.substring(indexOfRefer + "REFER ".length(), indexOfOpenParenReferCol);
/*      */ 
/*      */     
/* 6928 */     int indexOfSlash = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referCatalogTableString, "/", this.quotedId.charAt(0), false);
/*      */ 
/*      */     
/* 6931 */     if (indexOfSlash == -1) {
/* 6932 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6937 */     String referCatalog = removeQuotedId(referCatalogTableString.substring(0, indexOfSlash));
/*      */     
/* 6939 */     String referTable = removeQuotedId(referCatalogTableString.substring(indexOfSlash + 1).trim());
/*      */ 
/*      */     
/* 6942 */     int indexOfCloseParenRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfOpenParenReferCol, keysCommentTrimmed, ")", quoteChar, true);
/*      */ 
/*      */ 
/*      */     
/* 6946 */     if (indexOfCloseParenRefer == -1) {
/* 6947 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 6952 */     String referColumnNamesString = keysCommentTrimmed.substring(indexOfOpenParenReferCol + 1, indexOfCloseParenRefer);
/*      */ 
/*      */     
/* 6955 */     List referColumnsList = StringUtils.split(referColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */     
/* 6957 */     List localColumnsList = StringUtils.split(localColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */ 
/*      */     
/* 6960 */     return new LocalAndReferencedColumns(this, localColumnsList, referColumnsList, constraintName, referCatalog, referTable);
/*      */   }
/*      */ 
/*      */   
/*      */   private String removeQuotedId(String s) {
/* 6965 */     if (s == null) {
/* 6966 */       return null;
/*      */     }
/*      */     
/* 6969 */     if (this.quotedId.equals("")) {
/* 6970 */       return s;
/*      */     }
/*      */     
/* 6973 */     s = s.trim();
/*      */     
/* 6975 */     int frontOffset = 0;
/* 6976 */     int backOffset = s.length();
/* 6977 */     int quoteLength = this.quotedId.length();
/*      */     
/* 6979 */     if (s.startsWith(this.quotedId)) {
/* 6980 */       frontOffset = quoteLength;
/*      */     }
/*      */     
/* 6983 */     if (s.endsWith(this.quotedId)) {
/* 6984 */       backOffset -= quoteLength;
/*      */     }
/*      */     
/* 6987 */     return s.substring(frontOffset, backOffset);
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
/*      */   protected byte[] s2b(String s) throws SQLException {
/* 6999 */     if (s == null) {
/* 7000 */       return null;
/*      */     }
/*      */     
/* 7003 */     return StringUtils.getBytes(s, this.conn.getCharacterSetMetadata(), this.conn.getServerCharacterEncoding(), this.conn.parserKnowsUnicode(), this.conn, getExceptionInterceptor());
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
/*      */   public boolean storesLowerCaseIdentifiers() throws SQLException {
/* 7017 */     return this.conn.storesLowerCaseTableName();
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
/*      */   public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
/* 7029 */     return this.conn.storesLowerCaseTableName();
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
/*      */   public boolean storesMixedCaseIdentifiers() throws SQLException {
/* 7041 */     return !this.conn.storesLowerCaseTableName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
/* 7052 */     return !this.conn.storesLowerCaseTableName();
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
/*      */   public boolean storesUpperCaseIdentifiers() throws SQLException {
/* 7064 */     return false;
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
/*      */   public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
/* 7076 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithAddColumn() throws SQLException {
/* 7087 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithDropColumn() throws SQLException {
/* 7098 */     return true;
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
/*      */   public boolean supportsANSI92EntryLevelSQL() throws SQLException {
/* 7110 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92FullSQL() throws SQLException {
/* 7121 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92IntermediateSQL() throws SQLException {
/* 7132 */     return false;
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
/*      */   public boolean supportsBatchUpdates() throws SQLException {
/* 7144 */     return true;
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
/*      */   public boolean supportsCatalogsInDataManipulation() throws SQLException {
/* 7156 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
/* 7168 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
/* 7180 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsCatalogsInProcedureCalls() throws SQLException {
/* 7192 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsCatalogsInTableDefinitions() throws SQLException {
/* 7204 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsColumnAliasing() throws SQLException {
/* 7220 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsConvert() throws SQLException {
/* 7231 */     return false;
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
/*      */   public boolean supportsConvert(int fromType, int toType) throws SQLException {
/* 7248 */     switch (fromType) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/* 7259 */         switch (toType) {
/*      */           case -6:
/*      */           case -5:
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/*      */           case 12:
/*      */           case 91:
/*      */           case 92:
/*      */           case 93:
/*      */           case 1111:
/* 7279 */             return true;
/*      */         } 
/*      */         
/* 7282 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case -7:
/* 7289 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case -6:
/*      */       case -5:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/* 7305 */         switch (toType) {
/*      */           case -6:
/*      */           case -5:
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/*      */           case 12:
/* 7321 */             return true;
/*      */         } 
/*      */         
/* 7324 */         return false;
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/* 7329 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 1111:
/* 7337 */         switch (toType) {
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 12:
/* 7344 */             return true;
/*      */         } 
/*      */         
/* 7347 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 91:
/* 7353 */         switch (toType) {
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 12:
/* 7360 */             return true;
/*      */         } 
/*      */         
/* 7363 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 92:
/* 7369 */         switch (toType) {
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 12:
/* 7376 */             return true;
/*      */         } 
/*      */         
/* 7379 */         return false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 93:
/* 7388 */         switch (toType) {
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case -1:
/*      */           case 1:
/*      */           case 12:
/*      */           case 91:
/*      */           case 92:
/* 7397 */             return true;
/*      */         } 
/*      */         
/* 7400 */         return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 7405 */     return false;
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
/*      */   public boolean supportsCoreSQLGrammar() throws SQLException {
/* 7417 */     return true;
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
/*      */   public boolean supportsCorrelatedSubqueries() throws SQLException {
/* 7429 */     return this.conn.versionMeetsMinimum(4, 1, 0);
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
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
/* 7442 */     return false;
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
/*      */   public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
/* 7454 */     return false;
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
/*      */   public boolean supportsDifferentTableCorrelationNames() throws SQLException {
/* 7467 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsExpressionsInOrderBy() throws SQLException {
/* 7478 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsExtendedSQLGrammar() throws SQLException {
/* 7489 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsFullOuterJoins() throws SQLException {
/* 7500 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGetGeneratedKeys() {
/* 7509 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGroupBy() throws SQLException {
/* 7520 */     return true;
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
/*      */   public boolean supportsGroupByBeyondSelect() throws SQLException {
/* 7532 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsGroupByUnrelated() throws SQLException {
/* 7543 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsIntegrityEnhancementFacility() throws SQLException {
/* 7554 */     if (!this.conn.getOverrideSupportsIntegrityEnhancementFacility()) {
/* 7555 */       return false;
/*      */     }
/*      */     
/* 7558 */     return true;
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
/*      */   public boolean supportsLikeEscapeClause() throws SQLException {
/* 7570 */     return true;
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
/*      */   public boolean supportsLimitedOuterJoins() throws SQLException {
/* 7582 */     return true;
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
/*      */   public boolean supportsMinimumSQLGrammar() throws SQLException {
/* 7594 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMixedCaseIdentifiers() throws SQLException {
/* 7605 */     return !this.conn.lowerCaseTableNames();
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
/*      */   public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
/* 7617 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleOpenResults() throws SQLException {
/* 7624 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleResultSets() throws SQLException {
/* 7635 */     return false;
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
/*      */   public boolean supportsMultipleTransactions() throws SQLException {
/* 7647 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsNamedParameters() throws SQLException {
/* 7654 */     return false;
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
/*      */   public boolean supportsNonNullableColumns() throws SQLException {
/* 7666 */     return true;
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
/*      */   public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
/* 7678 */     return false;
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
/*      */   public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
/* 7690 */     return false;
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
/*      */   public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
/* 7702 */     return false;
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
/*      */   public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
/* 7714 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOrderByUnrelated() throws SQLException {
/* 7725 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsOuterJoins() throws SQLException {
/* 7736 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedDelete() throws SQLException {
/* 7747 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedUpdate() throws SQLException {
/* 7758 */     return false;
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
/*      */   public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
/* 7776 */     switch (type) {
/*      */       case 1004:
/* 7778 */         if (concurrency == 1007 || concurrency == 1008)
/*      */         {
/* 7780 */           return true;
/*      */         }
/* 7782 */         throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */       
/*      */       case 1003:
/* 7787 */         if (concurrency == 1007 || concurrency == 1008)
/*      */         {
/* 7789 */           return true;
/*      */         }
/* 7791 */         throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */       
/*      */       case 1005:
/* 7796 */         return false;
/*      */     } 
/* 7798 */     throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
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
/*      */   public boolean supportsResultSetHoldability(int holdability) throws SQLException {
/* 7810 */     return (holdability == 1);
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
/*      */   public boolean supportsResultSetType(int type) throws SQLException {
/* 7824 */     return (type == 1004);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSavepoints() throws SQLException {
/* 7832 */     return (this.conn.versionMeetsMinimum(4, 0, 14) || this.conn.versionMeetsMinimum(4, 1, 1));
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
/*      */   public boolean supportsSchemasInDataManipulation() throws SQLException {
/* 7844 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInIndexDefinitions() throws SQLException {
/* 7855 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
/* 7866 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInProcedureCalls() throws SQLException {
/* 7877 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInTableDefinitions() throws SQLException {
/* 7888 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsSelectForUpdate() throws SQLException {
/* 7899 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsStatementPooling() throws SQLException {
/* 7906 */     return false;
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
/*      */   public boolean supportsStoredProcedures() throws SQLException {
/* 7918 */     return this.conn.versionMeetsMinimum(5, 0, 0);
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
/*      */   public boolean supportsSubqueriesInComparisons() throws SQLException {
/* 7930 */     return this.conn.versionMeetsMinimum(4, 1, 0);
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
/*      */   public boolean supportsSubqueriesInExists() throws SQLException {
/* 7942 */     return this.conn.versionMeetsMinimum(4, 1, 0);
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
/*      */   public boolean supportsSubqueriesInIns() throws SQLException {
/* 7954 */     return this.conn.versionMeetsMinimum(4, 1, 0);
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
/*      */   public boolean supportsSubqueriesInQuantifieds() throws SQLException {
/* 7966 */     return this.conn.versionMeetsMinimum(4, 1, 0);
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
/*      */   public boolean supportsTableCorrelationNames() throws SQLException {
/* 7978 */     return true;
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
/*      */   public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
/* 7993 */     if (this.conn.supportsIsolationLevel()) {
/* 7994 */       switch (level) {
/*      */         case 1:
/*      */         case 2:
/*      */         case 4:
/*      */         case 8:
/* 7999 */           return true;
/*      */       } 
/*      */       
/* 8002 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 8006 */     return false;
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
/*      */   public boolean supportsTransactions() throws SQLException {
/* 8018 */     return this.conn.supportsTransactions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsUnion() throws SQLException {
/* 8029 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsUnionAll() throws SQLException {
/* 8040 */     return this.conn.versionMeetsMinimum(4, 0, 0);
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
/*      */   public boolean updatesAreDetected(int type) throws SQLException {
/* 8054 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesLocalFilePerTable() throws SQLException {
/* 8065 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesLocalFiles() throws SQLException {
/* 8076 */     return false;
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
/*      */   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
/* 8092 */     Field[] fields = createFunctionColumnsFields();
/*      */     
/* 8094 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, functionNamePattern, columnNamePattern, false, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Field[] createFunctionColumnsFields() {
/* 8101 */     Field[] fields = { new Field("", "FUNCTION_CAT", 12, 0), new Field("", "FUNCTION_SCHEM", 12, 0), new Field("", "FUNCTION_NAME", 12, 0), new Field("", "COLUMN_NAME", 12, 0), new Field("", "COLUMN_TYPE", 12, 0), new Field("", "DATA_TYPE", 5, 0), new Field("", "TYPE_NAME", 12, 0), new Field("", "PRECISION", 4, 0), new Field("", "LENGTH", 4, 0), new Field("", "SCALE", 5, 0), new Field("", "RADIX", 5, 0), new Field("", "NULLABLE", 5, 0), new Field("", "REMARKS", 12, 0), new Field("", "CHAR_OCTET_LENGTH", 4, 0), new Field("", "ORDINAL_POSITION", 4, 0), new Field("", "IS_NULLABLE", 12, 3), new Field("", "SPECIFIC_NAME", 12, 0) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8119 */     return fields;
/*      */   }
/*      */   
/*      */   public boolean providesQueryObjectGenerator() throws SQLException {
/* 8123 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
/* 8128 */     Field[] fields = { new Field("", "TABLE_SCHEM", 12, 255), new Field("", "TABLE_CATALOG", 12, 255) };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8133 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 8137 */     return true;
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
/*      */   protected PreparedStatement prepareMetaDataSafeStatement(String sql) throws SQLException {
/* 8150 */     PreparedStatement pStmt = (PreparedStatement)this.conn.clientPrepareStatement(sql);
/*      */ 
/*      */     
/* 8153 */     if (pStmt.getMaxRows() != 0) {
/* 8154 */       pStmt.setMaxRows(0);
/*      */     }
/*      */     
/* 8157 */     pStmt.setHoldResultsOpenOverClose(true);
/*      */     
/* 8159 */     return pStmt;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\DatabaseMetaData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */