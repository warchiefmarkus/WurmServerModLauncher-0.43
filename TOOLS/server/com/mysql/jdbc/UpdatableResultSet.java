/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandlerFactory;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ public class UpdatableResultSet
/*      */   extends ResultSetImpl
/*      */ {
/*   46 */   protected static final byte[] STREAM_DATA_MARKER = "** STREAM DATA **".getBytes();
/*      */ 
/*      */   
/*      */   protected SingleByteCharsetConverter charConverter;
/*      */ 
/*      */   
/*      */   private String charEncoding;
/*      */ 
/*      */   
/*      */   private byte[][] defaultColumnValue;
/*      */   
/*   57 */   private PreparedStatement deleter = null;
/*      */   
/*   59 */   private String deleteSQL = null;
/*      */ 
/*      */   
/*      */   private boolean initializedCharConverter = false;
/*      */   
/*   64 */   protected PreparedStatement inserter = null;
/*      */   
/*   66 */   private String insertSQL = null;
/*      */ 
/*      */   
/*      */   private boolean isUpdatable = false;
/*      */ 
/*      */   
/*   72 */   private String notUpdatableReason = null;
/*      */ 
/*      */   
/*   75 */   private List primaryKeyIndicies = null;
/*      */   
/*      */   private String qualifiedAndQuotedTableName;
/*      */   
/*   79 */   private String quotedIdChar = null;
/*      */ 
/*      */   
/*      */   private PreparedStatement refresher;
/*      */   
/*   84 */   private String refreshSQL = null;
/*      */ 
/*      */   
/*      */   private ResultSetRow savedCurrentRow;
/*      */ 
/*      */   
/*   90 */   protected PreparedStatement updater = null;
/*      */ 
/*      */   
/*   93 */   private String updateSQL = null;
/*      */   
/*      */   private boolean populateInserterWithDefaultValues = false;
/*      */   
/*   97 */   private Map databasesUsedToTablesUsed = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected UpdatableResultSet(String catalog, Field[] fields, RowData tuples, ConnectionImpl conn, StatementImpl creatorStmt) throws SQLException {
/*  118 */     super(catalog, fields, tuples, conn, creatorStmt);
/*  119 */     checkUpdatability();
/*  120 */     this.populateInserterWithDefaultValues = this.connection.getPopulateInsertRowWithDefaultValues();
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
/*      */   public synchronized boolean absolute(int row) throws SQLException {
/*  163 */     return super.absolute(row);
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
/*      */   public synchronized void afterLast() throws SQLException {
/*  179 */     super.afterLast();
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
/*      */   public synchronized void beforeFirst() throws SQLException {
/*  195 */     super.beforeFirst();
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
/*      */   public synchronized void cancelRowUpdates() throws SQLException {
/*  209 */     checkClosed();
/*      */     
/*  211 */     if (this.doingUpdates) {
/*  212 */       this.doingUpdates = false;
/*  213 */       this.updater.clearParameters();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkRowPos() throws SQLException {
/*  223 */     checkClosed();
/*      */     
/*  225 */     if (!this.onInsertRow) {
/*  226 */       super.checkRowPos();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkUpdatability() throws SQLException {
/*      */     try {
/*  238 */       if (this.fields == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  248 */       String singleTableName = null;
/*  249 */       String catalogName = null;
/*      */       
/*  251 */       int primaryKeyCount = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  258 */       if (this.catalog == null || this.catalog.length() == 0) {
/*  259 */         this.catalog = this.fields[0].getDatabaseName();
/*      */         
/*  261 */         if (this.catalog == null || this.catalog.length() == 0) {
/*  262 */           throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.43"), "S1009", getExceptionInterceptor());
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  268 */       if (this.fields.length > 0) {
/*  269 */         singleTableName = this.fields[0].getOriginalTableName();
/*  270 */         catalogName = this.fields[0].getDatabaseName();
/*      */         
/*  272 */         if (singleTableName == null) {
/*  273 */           singleTableName = this.fields[0].getTableName();
/*  274 */           catalogName = this.catalog;
/*      */         } 
/*      */         
/*  277 */         if (singleTableName != null && singleTableName.length() == 0) {
/*  278 */           this.isUpdatable = false;
/*  279 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  284 */         if (this.fields[0].isPrimaryKey()) {
/*  285 */           primaryKeyCount++;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  291 */         for (int i = 1; i < this.fields.length; i++) {
/*  292 */           String otherTableName = this.fields[i].getOriginalTableName();
/*  293 */           String otherCatalogName = this.fields[i].getDatabaseName();
/*      */           
/*  295 */           if (otherTableName == null) {
/*  296 */             otherTableName = this.fields[i].getTableName();
/*  297 */             otherCatalogName = this.catalog;
/*      */           } 
/*      */           
/*  300 */           if (otherTableName != null && otherTableName.length() == 0) {
/*  301 */             this.isUpdatable = false;
/*  302 */             this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  307 */           if (singleTableName == null || !otherTableName.equals(singleTableName)) {
/*      */             
/*  309 */             this.isUpdatable = false;
/*  310 */             this.notUpdatableReason = Messages.getString("NotUpdatableReason.0");
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  316 */           if (catalogName == null || !otherCatalogName.equals(catalogName)) {
/*      */             
/*  318 */             this.isUpdatable = false;
/*  319 */             this.notUpdatableReason = Messages.getString("NotUpdatableReason.1");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  324 */           if (this.fields[i].isPrimaryKey()) {
/*  325 */             primaryKeyCount++;
/*      */           }
/*      */         } 
/*      */         
/*  329 */         if (singleTableName == null || singleTableName.length() == 0) {
/*  330 */           this.isUpdatable = false;
/*  331 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.2");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } else {
/*  336 */         this.isUpdatable = false;
/*  337 */         this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  342 */       if (this.connection.getStrictUpdates()) {
/*  343 */         DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */         
/*  345 */         ResultSet rs = null;
/*  346 */         HashMap primaryKeyNames = new HashMap();
/*      */         
/*      */         try {
/*  349 */           rs = dbmd.getPrimaryKeys(catalogName, null, singleTableName);
/*      */           
/*  351 */           while (rs.next()) {
/*  352 */             String keyName = rs.getString(4);
/*  353 */             keyName = keyName.toUpperCase();
/*  354 */             primaryKeyNames.put(keyName, keyName);
/*      */           } 
/*      */         } finally {
/*  357 */           if (rs != null) {
/*      */             try {
/*  359 */               rs.close();
/*  360 */             } catch (Exception ex) {
/*  361 */               AssertionFailedException.shouldNotHappen(ex);
/*      */             } 
/*      */             
/*  364 */             rs = null;
/*      */           } 
/*      */         } 
/*      */         
/*  368 */         int existingPrimaryKeysCount = primaryKeyNames.size();
/*      */         
/*  370 */         if (existingPrimaryKeysCount == 0) {
/*  371 */           this.isUpdatable = false;
/*  372 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.5");
/*      */ 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */ 
/*      */         
/*  380 */         for (int i = 0; i < this.fields.length; i++) {
/*  381 */           if (this.fields[i].isPrimaryKey()) {
/*  382 */             String columnNameUC = this.fields[i].getName().toUpperCase();
/*      */ 
/*      */             
/*  385 */             if (primaryKeyNames.remove(columnNameUC) == null) {
/*      */               
/*  387 */               String originalName = this.fields[i].getOriginalName();
/*      */               
/*  389 */               if (originalName != null && 
/*  390 */                 primaryKeyNames.remove(originalName.toUpperCase()) == null) {
/*      */ 
/*      */                 
/*  393 */                 this.isUpdatable = false;
/*  394 */                 this.notUpdatableReason = Messages.getString("NotUpdatableReason.6", new Object[] { originalName });
/*      */ 
/*      */                 
/*      */                 return;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  404 */         this.isUpdatable = primaryKeyNames.isEmpty();
/*      */         
/*  406 */         if (!this.isUpdatable) {
/*  407 */           if (existingPrimaryKeysCount > 1) {
/*  408 */             this.notUpdatableReason = Messages.getString("NotUpdatableReason.7");
/*      */           } else {
/*  410 */             this.notUpdatableReason = Messages.getString("NotUpdatableReason.4");
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  420 */       if (primaryKeyCount == 0) {
/*  421 */         this.isUpdatable = false;
/*  422 */         this.notUpdatableReason = Messages.getString("NotUpdatableReason.4");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  427 */       this.isUpdatable = true;
/*  428 */       this.notUpdatableReason = null;
/*      */       
/*      */       return;
/*  431 */     } catch (SQLException sqlEx) {
/*  432 */       this.isUpdatable = false;
/*  433 */       this.notUpdatableReason = sqlEx.getMessage();
/*      */       return;
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
/*      */   public synchronized void deleteRow() throws SQLException {
/*  448 */     checkClosed();
/*      */     
/*  450 */     if (!this.isUpdatable) {
/*  451 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  454 */     if (this.onInsertRow)
/*  455 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.1"), getExceptionInterceptor()); 
/*  456 */     if (this.rowData.size() == 0)
/*  457 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.2"), getExceptionInterceptor()); 
/*  458 */     if (isBeforeFirst())
/*  459 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.3"), getExceptionInterceptor()); 
/*  460 */     if (isAfterLast()) {
/*  461 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.4"), getExceptionInterceptor());
/*      */     }
/*      */     
/*  464 */     if (this.deleter == null) {
/*  465 */       if (this.deleteSQL == null) {
/*  466 */         generateStatements();
/*      */       }
/*      */       
/*  469 */       this.deleter = (PreparedStatement)this.connection.clientPrepareStatement(this.deleteSQL);
/*      */     } 
/*      */ 
/*      */     
/*  473 */     this.deleter.clearParameters();
/*      */     
/*  475 */     String characterEncoding = null;
/*      */     
/*  477 */     if (this.connection.getUseUnicode()) {
/*  478 */       characterEncoding = this.connection.getEncoding();
/*      */     }
/*      */     
/*  481 */     int numKeys = this.primaryKeyIndicies.size();
/*      */     
/*  483 */     if (numKeys == 1) {
/*  484 */       int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/*      */       
/*  486 */       byte[] currentVal = this.thisRow.getColumnValue(index);
/*  487 */       this.deleter.setBytes(1, currentVal);
/*      */     } else {
/*  489 */       for (int i = 0; i < numKeys; i++) {
/*  490 */         int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
/*      */         
/*  492 */         byte[] currentVal = this.thisRow.getColumnValue(index);
/*      */         
/*  494 */         this.deleter.setBytes(i + 1, currentVal);
/*      */       } 
/*      */     } 
/*      */     
/*  498 */     this.deleter.executeUpdate();
/*  499 */     this.rowData.removeRow(this.rowData.getCurrentRowNumber());
/*      */ 
/*      */     
/*  502 */     previous();
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
/*      */   private synchronized void extractDefaultValues() throws SQLException {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   4: invokevirtual getMetaData : ()Ljava/sql/DatabaseMetaData;
/*      */     //   7: astore_1
/*      */     //   8: aload_0
/*      */     //   9: aload_0
/*      */     //   10: getfield fields : [Lcom/mysql/jdbc/Field;
/*      */     //   13: arraylength
/*      */     //   14: anewarray [B
/*      */     //   17: putfield defaultColumnValue : [[B
/*      */     //   20: aconst_null
/*      */     //   21: astore_2
/*      */     //   22: aload_0
/*      */     //   23: getfield databasesUsedToTablesUsed : Ljava/util/Map;
/*      */     //   26: invokeinterface entrySet : ()Ljava/util/Set;
/*      */     //   31: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   36: astore_3
/*      */     //   37: aload_3
/*      */     //   38: invokeinterface hasNext : ()Z
/*      */     //   43: ifeq -> 259
/*      */     //   46: aload_3
/*      */     //   47: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   52: checkcast java/util/Map$Entry
/*      */     //   55: astore #4
/*      */     //   57: aload #4
/*      */     //   59: invokeinterface getKey : ()Ljava/lang/Object;
/*      */     //   64: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   67: astore #5
/*      */     //   69: aload #4
/*      */     //   71: invokeinterface getValue : ()Ljava/lang/Object;
/*      */     //   76: checkcast java/util/Map
/*      */     //   79: invokeinterface entrySet : ()Ljava/util/Set;
/*      */     //   84: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   89: astore #6
/*      */     //   91: aload #6
/*      */     //   93: invokeinterface hasNext : ()Z
/*      */     //   98: ifeq -> 256
/*      */     //   101: aload #6
/*      */     //   103: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   108: checkcast java/util/Map$Entry
/*      */     //   111: astore #7
/*      */     //   113: aload #7
/*      */     //   115: invokeinterface getKey : ()Ljava/lang/Object;
/*      */     //   120: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   123: astore #8
/*      */     //   125: aload #7
/*      */     //   127: invokeinterface getValue : ()Ljava/lang/Object;
/*      */     //   132: checkcast java/util/Map
/*      */     //   135: astore #9
/*      */     //   137: aload_1
/*      */     //   138: aload_0
/*      */     //   139: getfield catalog : Ljava/lang/String;
/*      */     //   142: aconst_null
/*      */     //   143: aload #8
/*      */     //   145: ldc '%'
/*      */     //   147: invokeinterface getColumns : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;
/*      */     //   152: astore_2
/*      */     //   153: aload_2
/*      */     //   154: invokeinterface next : ()Z
/*      */     //   159: ifeq -> 223
/*      */     //   162: aload_2
/*      */     //   163: ldc 'COLUMN_NAME'
/*      */     //   165: invokeinterface getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   170: astore #10
/*      */     //   172: aload_2
/*      */     //   173: ldc 'COLUMN_DEF'
/*      */     //   175: invokeinterface getBytes : (Ljava/lang/String;)[B
/*      */     //   180: astore #11
/*      */     //   182: aload #9
/*      */     //   184: aload #10
/*      */     //   186: invokeinterface containsKey : (Ljava/lang/Object;)Z
/*      */     //   191: ifeq -> 220
/*      */     //   194: aload #9
/*      */     //   196: aload #10
/*      */     //   198: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   203: checkcast java/lang/Integer
/*      */     //   206: invokevirtual intValue : ()I
/*      */     //   209: istore #12
/*      */     //   211: aload_0
/*      */     //   212: getfield defaultColumnValue : [[B
/*      */     //   215: iload #12
/*      */     //   217: aload #11
/*      */     //   219: aastore
/*      */     //   220: goto -> 153
/*      */     //   223: jsr -> 237
/*      */     //   226: goto -> 253
/*      */     //   229: astore #13
/*      */     //   231: jsr -> 237
/*      */     //   234: aload #13
/*      */     //   236: athrow
/*      */     //   237: astore #14
/*      */     //   239: aload_2
/*      */     //   240: ifnull -> 251
/*      */     //   243: aload_2
/*      */     //   244: invokeinterface close : ()V
/*      */     //   249: aconst_null
/*      */     //   250: astore_2
/*      */     //   251: ret #14
/*      */     //   253: goto -> 91
/*      */     //   256: goto -> 37
/*      */     //   259: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #507	-> 0
/*      */     //   #508	-> 8
/*      */     //   #510	-> 20
/*      */     //   #511	-> 22
/*      */     //   #513	-> 37
/*      */     //   #514	-> 46
/*      */     //   #515	-> 57
/*      */     //   #517	-> 69
/*      */     //   #519	-> 91
/*      */     //   #520	-> 101
/*      */     //   #521	-> 113
/*      */     //   #522	-> 125
/*      */     //   #525	-> 137
/*      */     //   #528	-> 153
/*      */     //   #529	-> 162
/*      */     //   #530	-> 172
/*      */     //   #532	-> 182
/*      */     //   #533	-> 194
/*      */     //   #535	-> 211
/*      */     //   #538	-> 223
/*      */     //   #544	-> 226
/*      */     //   #539	-> 229
/*      */     //   #540	-> 243
/*      */     //   #542	-> 249
/*      */     //   #547	-> 259
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   211	9	12	localColumnIndex	I
/*      */     //   172	48	10	columnName	Ljava/lang/String;
/*      */     //   182	38	11	defaultValue	[B
/*      */     //   113	140	7	tableEntry	Ljava/util/Map$Entry;
/*      */     //   125	128	8	tableName	Ljava/lang/String;
/*      */     //   137	116	9	columnNamesToIndices	Ljava/util/Map;
/*      */     //   57	199	4	dbEntry	Ljava/util/Map$Entry;
/*      */     //   69	187	5	databaseName	Ljava/lang/String;
/*      */     //   91	165	6	referencedTables	Ljava/util/Iterator;
/*      */     //   0	260	0	this	Lcom/mysql/jdbc/UpdatableResultSet;
/*      */     //   8	252	1	dbmd	Ljava/sql/DatabaseMetaData;
/*      */     //   22	238	2	columnsResultSet	Ljava/sql/ResultSet;
/*      */     //   37	223	3	referencedDbs	Ljava/util/Iterator;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   137	226	229	finally
/*      */     //   229	234	229	finally
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
/*      */   public synchronized boolean first() throws SQLException {
/*  563 */     return super.first();
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
/*      */   protected synchronized void generateStatements() throws SQLException {
/*  576 */     if (!this.isUpdatable) {
/*  577 */       this.doingUpdates = false;
/*  578 */       this.onInsertRow = false;
/*      */       
/*  580 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     } 
/*      */     
/*  583 */     String quotedId = getQuotedIdChar();
/*      */     
/*  585 */     Map tableNamesSoFar = null;
/*      */     
/*  587 */     if (this.connection.lowerCaseTableNames()) {
/*  588 */       tableNamesSoFar = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  589 */       this.databasesUsedToTablesUsed = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */     } else {
/*  591 */       tableNamesSoFar = new TreeMap();
/*  592 */       this.databasesUsedToTablesUsed = new TreeMap();
/*      */     } 
/*      */     
/*  595 */     this.primaryKeyIndicies = new ArrayList();
/*      */     
/*  597 */     StringBuffer fieldValues = new StringBuffer();
/*  598 */     StringBuffer keyValues = new StringBuffer();
/*  599 */     StringBuffer columnNames = new StringBuffer();
/*  600 */     StringBuffer insertPlaceHolders = new StringBuffer();
/*  601 */     StringBuffer allTablesBuf = new StringBuffer();
/*  602 */     Map columnIndicesToTable = new HashMap();
/*      */     
/*  604 */     boolean firstTime = true;
/*  605 */     boolean keysFirstTime = true;
/*      */     
/*  607 */     String equalsStr = this.connection.versionMeetsMinimum(3, 23, 0) ? "<=>" : "=";
/*      */ 
/*      */     
/*  610 */     for (int i = 0; i < this.fields.length; i++) {
/*  611 */       StringBuffer tableNameBuffer = new StringBuffer();
/*  612 */       Map updColumnNameToIndex = null;
/*      */ 
/*      */       
/*  615 */       if (this.fields[i].getOriginalTableName() != null) {
/*      */         
/*  617 */         String str1 = this.fields[i].getDatabaseName();
/*      */         
/*  619 */         if (str1 != null && str1.length() > 0) {
/*  620 */           tableNameBuffer.append(quotedId);
/*  621 */           tableNameBuffer.append(str1);
/*  622 */           tableNameBuffer.append(quotedId);
/*  623 */           tableNameBuffer.append('.');
/*      */         } 
/*      */         
/*  626 */         String tableOnlyName = this.fields[i].getOriginalTableName();
/*      */         
/*  628 */         tableNameBuffer.append(quotedId);
/*  629 */         tableNameBuffer.append(tableOnlyName);
/*  630 */         tableNameBuffer.append(quotedId);
/*      */         
/*  632 */         String fqTableName = tableNameBuffer.toString();
/*      */         
/*  634 */         if (!tableNamesSoFar.containsKey(fqTableName)) {
/*  635 */           if (!tableNamesSoFar.isEmpty()) {
/*  636 */             allTablesBuf.append(',');
/*      */           }
/*      */           
/*  639 */           allTablesBuf.append(fqTableName);
/*  640 */           tableNamesSoFar.put(fqTableName, fqTableName);
/*      */         } 
/*      */         
/*  643 */         columnIndicesToTable.put(new Integer(i), fqTableName);
/*      */         
/*  645 */         updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(str1, tableOnlyName);
/*      */       } else {
/*  647 */         String tableOnlyName = this.fields[i].getTableName();
/*      */         
/*  649 */         if (tableOnlyName != null) {
/*  650 */           tableNameBuffer.append(quotedId);
/*  651 */           tableNameBuffer.append(tableOnlyName);
/*  652 */           tableNameBuffer.append(quotedId);
/*      */           
/*  654 */           String fqTableName = tableNameBuffer.toString();
/*      */           
/*  656 */           if (!tableNamesSoFar.containsKey(fqTableName)) {
/*  657 */             if (!tableNamesSoFar.isEmpty()) {
/*  658 */               allTablesBuf.append(',');
/*      */             }
/*      */             
/*  661 */             allTablesBuf.append(fqTableName);
/*  662 */             tableNamesSoFar.put(fqTableName, fqTableName);
/*      */           } 
/*      */           
/*  665 */           columnIndicesToTable.put(new Integer(i), fqTableName);
/*      */           
/*  667 */           updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(this.catalog, tableOnlyName);
/*      */         } 
/*      */       } 
/*      */       
/*  671 */       String originalColumnName = this.fields[i].getOriginalName();
/*  672 */       String columnName = null;
/*      */       
/*  674 */       if (this.connection.getIO().hasLongColumnInfo() && originalColumnName != null && originalColumnName.length() > 0) {
/*      */ 
/*      */         
/*  677 */         columnName = originalColumnName;
/*      */       } else {
/*  679 */         columnName = this.fields[i].getName();
/*      */       } 
/*      */       
/*  682 */       if (updColumnNameToIndex != null && columnName != null) {
/*  683 */         updColumnNameToIndex.put(columnName, new Integer(i));
/*      */       }
/*      */       
/*  686 */       String originalTableName = this.fields[i].getOriginalTableName();
/*  687 */       String tableName = null;
/*      */       
/*  689 */       if (this.connection.getIO().hasLongColumnInfo() && originalTableName != null && originalTableName.length() > 0) {
/*      */ 
/*      */         
/*  692 */         tableName = originalTableName;
/*      */       } else {
/*  694 */         tableName = this.fields[i].getTableName();
/*      */       } 
/*      */       
/*  697 */       StringBuffer fqcnBuf = new StringBuffer();
/*  698 */       String databaseName = this.fields[i].getDatabaseName();
/*      */       
/*  700 */       if (databaseName != null && databaseName.length() > 0) {
/*  701 */         fqcnBuf.append(quotedId);
/*  702 */         fqcnBuf.append(databaseName);
/*  703 */         fqcnBuf.append(quotedId);
/*  704 */         fqcnBuf.append('.');
/*      */       } 
/*      */       
/*  707 */       fqcnBuf.append(quotedId);
/*  708 */       fqcnBuf.append(tableName);
/*  709 */       fqcnBuf.append(quotedId);
/*  710 */       fqcnBuf.append('.');
/*  711 */       fqcnBuf.append(quotedId);
/*  712 */       fqcnBuf.append(columnName);
/*  713 */       fqcnBuf.append(quotedId);
/*      */       
/*  715 */       String qualifiedColumnName = fqcnBuf.toString();
/*      */       
/*  717 */       if (this.fields[i].isPrimaryKey()) {
/*  718 */         this.primaryKeyIndicies.add(Constants.integerValueOf(i));
/*      */         
/*  720 */         if (!keysFirstTime) {
/*  721 */           keyValues.append(" AND ");
/*      */         } else {
/*  723 */           keysFirstTime = false;
/*      */         } 
/*      */         
/*  726 */         keyValues.append(qualifiedColumnName);
/*  727 */         keyValues.append(equalsStr);
/*  728 */         keyValues.append("?");
/*      */       } 
/*      */       
/*  731 */       if (firstTime) {
/*  732 */         firstTime = false;
/*  733 */         fieldValues.append("SET ");
/*      */       } else {
/*  735 */         fieldValues.append(",");
/*  736 */         columnNames.append(",");
/*  737 */         insertPlaceHolders.append(",");
/*      */       } 
/*      */       
/*  740 */       insertPlaceHolders.append("?");
/*      */       
/*  742 */       columnNames.append(qualifiedColumnName);
/*      */       
/*  744 */       fieldValues.append(qualifiedColumnName);
/*  745 */       fieldValues.append("=?");
/*      */     } 
/*      */     
/*  748 */     this.qualifiedAndQuotedTableName = allTablesBuf.toString();
/*      */     
/*  750 */     this.updateSQL = "UPDATE " + this.qualifiedAndQuotedTableName + " " + fieldValues.toString() + " WHERE " + keyValues.toString();
/*      */ 
/*      */     
/*  753 */     this.insertSQL = "INSERT INTO " + this.qualifiedAndQuotedTableName + " (" + columnNames.toString() + ") VALUES (" + insertPlaceHolders.toString() + ")";
/*      */ 
/*      */     
/*  756 */     this.refreshSQL = "SELECT " + columnNames.toString() + " FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
/*      */ 
/*      */     
/*  759 */     this.deleteSQL = "DELETE FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map getColumnsToIndexMapForTableAndDB(String databaseName, String tableName) {
/*  766 */     Map tablesUsedToColumnsMap = (Map)this.databasesUsedToTablesUsed.get(databaseName);
/*      */     
/*  768 */     if (tablesUsedToColumnsMap == null) {
/*  769 */       if (this.connection.lowerCaseTableNames()) {
/*  770 */         tablesUsedToColumnsMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */       } else {
/*  772 */         tablesUsedToColumnsMap = new TreeMap();
/*      */       } 
/*      */       
/*  775 */       this.databasesUsedToTablesUsed.put(databaseName, tablesUsedToColumnsMap);
/*      */     } 
/*      */     
/*  778 */     Map nameToIndex = (Map)tablesUsedToColumnsMap.get(tableName);
/*      */     
/*  780 */     if (nameToIndex == null) {
/*  781 */       nameToIndex = new HashMap();
/*  782 */       tablesUsedToColumnsMap.put(tableName, nameToIndex);
/*      */     } 
/*      */     
/*  785 */     return nameToIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized SingleByteCharsetConverter getCharConverter() throws SQLException {
/*  790 */     if (!this.initializedCharConverter) {
/*  791 */       this.initializedCharConverter = true;
/*      */       
/*  793 */       if (this.connection.getUseUnicode()) {
/*  794 */         this.charEncoding = this.connection.getEncoding();
/*  795 */         this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  800 */     return this.charConverter;
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
/*      */   public int getConcurrency() throws SQLException {
/*  813 */     return this.isUpdatable ? 1008 : 1007;
/*      */   }
/*      */   
/*      */   private synchronized String getQuotedIdChar() throws SQLException {
/*  817 */     if (this.quotedIdChar == null) {
/*  818 */       boolean useQuotedIdentifiers = this.connection.supportsQuotedIdentifiers();
/*      */ 
/*      */       
/*  821 */       if (useQuotedIdentifiers) {
/*  822 */         DatabaseMetaData dbmd = this.connection.getMetaData();
/*  823 */         this.quotedIdChar = dbmd.getIdentifierQuoteString();
/*      */       } else {
/*  825 */         this.quotedIdChar = "";
/*      */       } 
/*      */     } 
/*      */     
/*  829 */     return this.quotedIdChar;
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
/*      */   public synchronized void insertRow() throws SQLException {
/*  842 */     checkClosed();
/*      */     
/*  844 */     if (!this.onInsertRow) {
/*  845 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.7"), getExceptionInterceptor());
/*      */     }
/*      */     
/*  848 */     this.inserter.executeUpdate();
/*      */     
/*  850 */     long autoIncrementId = this.inserter.getLastInsertID();
/*  851 */     int numFields = this.fields.length;
/*  852 */     byte[][] newRow = new byte[numFields][];
/*      */     
/*  854 */     for (int i = 0; i < numFields; i++) {
/*  855 */       if (this.inserter.isNull(i)) {
/*  856 */         newRow[i] = null;
/*      */       } else {
/*  858 */         newRow[i] = this.inserter.getBytesRepresentation(i);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  865 */       if (this.fields[i].isAutoIncrement() && autoIncrementId > 0L) {
/*  866 */         newRow[i] = String.valueOf(autoIncrementId).getBytes();
/*  867 */         this.inserter.setBytesNoEscapeNoQuotes(i + 1, newRow[i]);
/*      */       } 
/*      */     } 
/*      */     
/*  871 */     ResultSetRow resultSetRow = new ByteArrayRow(newRow, getExceptionInterceptor());
/*      */     
/*  873 */     refreshRow(this.inserter, resultSetRow);
/*      */     
/*  875 */     this.rowData.addRow(resultSetRow);
/*  876 */     resetInserter();
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
/*      */   public synchronized boolean isAfterLast() throws SQLException {
/*  893 */     return super.isAfterLast();
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
/*      */   public synchronized boolean isBeforeFirst() throws SQLException {
/*  910 */     return super.isBeforeFirst();
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
/*      */   public synchronized boolean isFirst() throws SQLException {
/*  926 */     return super.isFirst();
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
/*      */   public synchronized boolean isLast() throws SQLException {
/*  945 */     return super.isLast();
/*      */   }
/*      */   
/*      */   boolean isUpdatable() {
/*  949 */     return this.isUpdatable;
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
/*      */   public synchronized boolean last() throws SQLException {
/*  966 */     return super.last();
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
/*      */   public synchronized void moveToCurrentRow() throws SQLException {
/*  980 */     checkClosed();
/*      */     
/*  982 */     if (!this.isUpdatable) {
/*  983 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  986 */     if (this.onInsertRow) {
/*  987 */       this.onInsertRow = false;
/*  988 */       this.thisRow = this.savedCurrentRow;
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
/*      */   public synchronized void moveToInsertRow() throws SQLException {
/* 1010 */     checkClosed();
/*      */     
/* 1012 */     if (!this.isUpdatable) {
/* 1013 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/* 1016 */     if (this.inserter == null) {
/* 1017 */       if (this.insertSQL == null) {
/* 1018 */         generateStatements();
/*      */       }
/*      */       
/* 1021 */       this.inserter = (PreparedStatement)this.connection.clientPrepareStatement(this.insertSQL);
/*      */       
/* 1023 */       if (this.populateInserterWithDefaultValues) {
/* 1024 */         extractDefaultValues();
/*      */       }
/*      */       
/* 1027 */       resetInserter();
/*      */     } else {
/* 1029 */       resetInserter();
/*      */     } 
/*      */     
/* 1032 */     int numFields = this.fields.length;
/*      */     
/* 1034 */     this.onInsertRow = true;
/* 1035 */     this.doingUpdates = false;
/* 1036 */     this.savedCurrentRow = this.thisRow;
/* 1037 */     byte[][] newRowData = new byte[numFields][];
/* 1038 */     this.thisRow = new ByteArrayRow(newRowData, getExceptionInterceptor());
/*      */     
/* 1040 */     for (int i = 0; i < numFields; i++) {
/* 1041 */       if (!this.populateInserterWithDefaultValues) {
/* 1042 */         this.inserter.setBytesNoEscapeNoQuotes(i + 1, "DEFAULT".getBytes());
/*      */         
/* 1044 */         newRowData = (byte[][])null;
/*      */       }
/* 1046 */       else if (this.defaultColumnValue[i] != null) {
/* 1047 */         Field f = this.fields[i];
/*      */         
/* 1049 */         switch (f.getMysqlType()) {
/*      */           
/*      */           case 7:
/*      */           case 10:
/*      */           case 11:
/*      */           case 12:
/*      */           case 14:
/* 1056 */             if ((this.defaultColumnValue[i]).length > 7 && this.defaultColumnValue[i][0] == 67 && this.defaultColumnValue[i][1] == 85 && this.defaultColumnValue[i][2] == 82 && this.defaultColumnValue[i][3] == 82 && this.defaultColumnValue[i][4] == 69 && this.defaultColumnValue[i][5] == 78 && this.defaultColumnValue[i][6] == 84 && this.defaultColumnValue[i][7] == 95) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1065 */               this.inserter.setBytesNoEscapeNoQuotes(i + 1, this.defaultColumnValue[i]);
/*      */               break;
/*      */             } 
/*      */ 
/*      */           
/*      */           default:
/* 1071 */             this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
/*      */             break;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1077 */         byte[] defaultValueCopy = new byte[(this.defaultColumnValue[i]).length];
/* 1078 */         System.arraycopy(this.defaultColumnValue[i], 0, defaultValueCopy, 0, defaultValueCopy.length);
/*      */         
/* 1080 */         newRowData[i] = defaultValueCopy;
/*      */       } else {
/* 1082 */         this.inserter.setNull(i + 1, 0);
/* 1083 */         newRowData[i] = null;
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
/*      */   public synchronized boolean next() throws SQLException {
/* 1109 */     return super.next();
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
/*      */   public synchronized boolean prev() throws SQLException {
/* 1128 */     return super.prev();
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
/*      */   public synchronized boolean previous() throws SQLException {
/* 1150 */     return super.previous();
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
/*      */   public void realClose(boolean calledExplicitly) throws SQLException {
/* 1163 */     if (this.isClosed) {
/*      */       return;
/*      */     }
/*      */     
/* 1167 */     SQLException sqlEx = null;
/*      */     
/* 1169 */     if (this.useUsageAdvisor && 
/* 1170 */       this.deleter == null && this.inserter == null && this.refresher == null && this.updater == null) {
/*      */       
/* 1172 */       this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */       
/* 1174 */       String message = Messages.getString("UpdatableResultSet.34");
/*      */       
/* 1176 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
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
/*      */     try {
/* 1190 */       if (this.deleter != null) {
/* 1191 */         this.deleter.close();
/*      */       }
/* 1193 */     } catch (SQLException ex) {
/* 1194 */       sqlEx = ex;
/*      */     } 
/*      */     
/*      */     try {
/* 1198 */       if (this.inserter != null) {
/* 1199 */         this.inserter.close();
/*      */       }
/* 1201 */     } catch (SQLException ex) {
/* 1202 */       sqlEx = ex;
/*      */     } 
/*      */     
/*      */     try {
/* 1206 */       if (this.refresher != null) {
/* 1207 */         this.refresher.close();
/*      */       }
/* 1209 */     } catch (SQLException ex) {
/* 1210 */       sqlEx = ex;
/*      */     } 
/*      */     
/*      */     try {
/* 1214 */       if (this.updater != null) {
/* 1215 */         this.updater.close();
/*      */       }
/* 1217 */     } catch (SQLException ex) {
/* 1218 */       sqlEx = ex;
/*      */     } 
/*      */     
/* 1221 */     super.realClose(calledExplicitly);
/*      */     
/* 1223 */     if (sqlEx != null) {
/* 1224 */       throw sqlEx;
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
/*      */   public synchronized void refreshRow() throws SQLException {
/* 1249 */     checkClosed();
/*      */     
/* 1251 */     if (!this.isUpdatable) {
/* 1252 */       throw new NotUpdatable();
/*      */     }
/*      */     
/* 1255 */     if (this.onInsertRow)
/* 1256 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.8"), getExceptionInterceptor()); 
/* 1257 */     if (this.rowData.size() == 0)
/* 1258 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.9"), getExceptionInterceptor()); 
/* 1259 */     if (isBeforeFirst())
/* 1260 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.10"), getExceptionInterceptor()); 
/* 1261 */     if (isAfterLast()) {
/* 1262 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.11"), getExceptionInterceptor());
/*      */     }
/*      */     
/* 1265 */     refreshRow(this.updater, this.thisRow);
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void refreshRow(PreparedStatement updateInsertStmt, ResultSetRow rowToRefresh) throws SQLException {
/* 1270 */     if (this.refresher == null) {
/* 1271 */       if (this.refreshSQL == null) {
/* 1272 */         generateStatements();
/*      */       }
/*      */       
/* 1275 */       this.refresher = (PreparedStatement)this.connection.clientPrepareStatement(this.refreshSQL);
/*      */     } 
/*      */ 
/*      */     
/* 1279 */     this.refresher.clearParameters();
/*      */     
/* 1281 */     int numKeys = this.primaryKeyIndicies.size();
/*      */     
/* 1283 */     if (numKeys == 1) {
/* 1284 */       byte[] dataFrom = null;
/* 1285 */       int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/*      */       
/* 1287 */       if (!this.doingUpdates && !this.onInsertRow) {
/* 1288 */         dataFrom = rowToRefresh.getColumnValue(index);
/*      */       } else {
/* 1290 */         dataFrom = updateInsertStmt.getBytesRepresentation(index);
/*      */ 
/*      */         
/* 1293 */         if (updateInsertStmt.isNull(index) || dataFrom.length == 0) {
/* 1294 */           dataFrom = rowToRefresh.getColumnValue(index);
/*      */         } else {
/* 1296 */           dataFrom = stripBinaryPrefix(dataFrom);
/*      */         } 
/*      */       } 
/*      */       
/* 1300 */       this.refresher.setBytesNoEscape(1, dataFrom);
/*      */     } else {
/* 1302 */       for (int i = 0; i < numKeys; i++) {
/* 1303 */         byte[] dataFrom = null;
/* 1304 */         int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
/*      */ 
/*      */         
/* 1307 */         if (!this.doingUpdates && !this.onInsertRow) {
/* 1308 */           dataFrom = rowToRefresh.getColumnValue(index);
/*      */         } else {
/* 1310 */           dataFrom = updateInsertStmt.getBytesRepresentation(index);
/*      */ 
/*      */           
/* 1313 */           if (updateInsertStmt.isNull(index) || dataFrom.length == 0) {
/* 1314 */             dataFrom = rowToRefresh.getColumnValue(index);
/*      */           } else {
/* 1316 */             dataFrom = stripBinaryPrefix(dataFrom);
/*      */           } 
/*      */         } 
/*      */         
/* 1320 */         this.refresher.setBytesNoEscape(i + 1, dataFrom);
/*      */       } 
/*      */     } 
/*      */     
/* 1324 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1327 */       rs = this.refresher.executeQuery();
/*      */       
/* 1329 */       int numCols = rs.getMetaData().getColumnCount();
/*      */       
/* 1331 */       if (rs.next()) {
/* 1332 */         for (int i = 0; i < numCols; i++) {
/* 1333 */           byte[] val = rs.getBytes(i + 1);
/*      */           
/* 1335 */           if (val == null || rs.wasNull()) {
/* 1336 */             rowToRefresh.setColumnValue(i, null);
/*      */           } else {
/* 1338 */             rowToRefresh.setColumnValue(i, rs.getBytes(i + 1));
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1342 */         throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.12"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */     
/*      */     } finally {
/*      */       
/* 1347 */       if (rs != null) {
/*      */         try {
/* 1349 */           rs.close();
/* 1350 */         } catch (SQLException ex) {}
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
/*      */   public synchronized boolean relative(int rows) throws SQLException {
/* 1384 */     return super.relative(rows);
/*      */   }
/*      */   
/*      */   private void resetInserter() throws SQLException {
/* 1388 */     this.inserter.clearParameters();
/*      */     
/* 1390 */     for (int i = 0; i < this.fields.length; i++) {
/* 1391 */       this.inserter.setNull(i + 1, 0);
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
/*      */   public synchronized boolean rowDeleted() throws SQLException {
/* 1411 */     throw SQLError.notImplemented();
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
/*      */   public synchronized boolean rowInserted() throws SQLException {
/* 1429 */     throw SQLError.notImplemented();
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
/*      */   public synchronized boolean rowUpdated() throws SQLException {
/* 1447 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setResultSetConcurrency(int concurrencyFlag) {
/* 1457 */     super.setResultSetConcurrency(concurrencyFlag);
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
/*      */   private byte[] stripBinaryPrefix(byte[] dataFrom) {
/* 1471 */     return StringUtils.stripEnclosure(dataFrom, "_binary'", "'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void syncUpdate() throws SQLException {
/* 1482 */     if (this.updater == null) {
/* 1483 */       if (this.updateSQL == null) {
/* 1484 */         generateStatements();
/*      */       }
/*      */       
/* 1487 */       this.updater = (PreparedStatement)this.connection.clientPrepareStatement(this.updateSQL);
/*      */     } 
/*      */ 
/*      */     
/* 1491 */     int numFields = this.fields.length;
/* 1492 */     this.updater.clearParameters();
/*      */     
/* 1494 */     for (int i = 0; i < numFields; i++) {
/* 1495 */       if (this.thisRow.getColumnValue(i) != null) {
/* 1496 */         this.updater.setBytes(i + 1, this.thisRow.getColumnValue(i), this.fields[i].isBinary(), false);
/*      */       } else {
/*      */         
/* 1499 */         this.updater.setNull(i + 1, 0);
/*      */       } 
/*      */     } 
/*      */     
/* 1503 */     int numKeys = this.primaryKeyIndicies.size();
/*      */     
/* 1505 */     if (numKeys == 1) {
/* 1506 */       int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/* 1507 */       byte[] keyData = this.thisRow.getColumnValue(index);
/* 1508 */       this.updater.setBytes(numFields + 1, keyData, false, false);
/*      */     } else {
/* 1510 */       for (int j = 0; j < numKeys; j++) {
/* 1511 */         byte[] currentVal = this.thisRow.getColumnValue(((Integer)this.primaryKeyIndicies.get(j)).intValue());
/*      */ 
/*      */         
/* 1514 */         if (currentVal != null) {
/* 1515 */           this.updater.setBytes(numFields + j + 1, currentVal, false, false);
/*      */         } else {
/*      */           
/* 1518 */           this.updater.setNull(numFields + j + 1, 0);
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
/*      */   public synchronized void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
/* 1543 */     if (!this.onInsertRow) {
/* 1544 */       if (!this.doingUpdates) {
/* 1545 */         this.doingUpdates = true;
/* 1546 */         syncUpdate();
/*      */       } 
/*      */       
/* 1549 */       this.updater.setAsciiStream(columnIndex, x, length);
/*      */     } else {
/* 1551 */       this.inserter.setAsciiStream(columnIndex, x, length);
/* 1552 */       this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
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
/*      */   public synchronized void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
/* 1575 */     updateAsciiStream(findColumn(columnName), x, length);
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
/*      */   public synchronized void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
/* 1594 */     if (!this.onInsertRow) {
/* 1595 */       if (!this.doingUpdates) {
/* 1596 */         this.doingUpdates = true;
/* 1597 */         syncUpdate();
/*      */       } 
/*      */       
/* 1600 */       this.updater.setBigDecimal(columnIndex, x);
/*      */     } else {
/* 1602 */       this.inserter.setBigDecimal(columnIndex, x);
/*      */       
/* 1604 */       if (x == null) {
/* 1605 */         this.thisRow.setColumnValue(columnIndex - 1, null);
/*      */       } else {
/* 1607 */         this.thisRow.setColumnValue(columnIndex - 1, x.toString().getBytes());
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
/*      */   public synchronized void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
/* 1628 */     updateBigDecimal(findColumn(columnName), x);
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
/*      */   public synchronized void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
/* 1650 */     if (!this.onInsertRow) {
/* 1651 */       if (!this.doingUpdates) {
/* 1652 */         this.doingUpdates = true;
/* 1653 */         syncUpdate();
/*      */       } 
/*      */       
/* 1656 */       this.updater.setBinaryStream(columnIndex, x, length);
/*      */     } else {
/* 1658 */       this.inserter.setBinaryStream(columnIndex, x, length);
/*      */       
/* 1660 */       if (x == null) {
/* 1661 */         this.thisRow.setColumnValue(columnIndex - 1, null);
/*      */       } else {
/* 1663 */         this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
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
/*      */   public synchronized void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
/* 1687 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(int columnIndex, Blob blob) throws SQLException {
/* 1695 */     if (!this.onInsertRow) {
/* 1696 */       if (!this.doingUpdates) {
/* 1697 */         this.doingUpdates = true;
/* 1698 */         syncUpdate();
/*      */       } 
/*      */       
/* 1701 */       this.updater.setBlob(columnIndex, blob);
/*      */     } else {
/* 1703 */       this.inserter.setBlob(columnIndex, blob);
/*      */       
/* 1705 */       if (blob == null) {
/* 1706 */         this.thisRow.setColumnValue(columnIndex - 1, null);
/*      */       } else {
/* 1708 */         this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(String columnName, Blob blob) throws SQLException {
/* 1718 */     updateBlob(findColumn(columnName), blob);
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
/*      */   public synchronized void updateBoolean(int columnIndex, boolean x) throws SQLException {
/* 1737 */     if (!this.onInsertRow) {
/* 1738 */       if (!this.doingUpdates) {
/* 1739 */         this.doingUpdates = true;
/* 1740 */         syncUpdate();
/*      */       } 
/*      */       
/* 1743 */       this.updater.setBoolean(columnIndex, x);
/*      */     } else {
/* 1745 */       this.inserter.setBoolean(columnIndex, x);
/*      */       
/* 1747 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateBoolean(String columnName, boolean x) throws SQLException {
/* 1768 */     updateBoolean(findColumn(columnName), x);
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
/*      */   public synchronized void updateByte(int columnIndex, byte x) throws SQLException {
/* 1787 */     if (!this.onInsertRow) {
/* 1788 */       if (!this.doingUpdates) {
/* 1789 */         this.doingUpdates = true;
/* 1790 */         syncUpdate();
/*      */       } 
/*      */       
/* 1793 */       this.updater.setByte(columnIndex, x);
/*      */     } else {
/* 1795 */       this.inserter.setByte(columnIndex, x);
/*      */       
/* 1797 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateByte(String columnName, byte x) throws SQLException {
/* 1818 */     updateByte(findColumn(columnName), x);
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
/*      */   public synchronized void updateBytes(int columnIndex, byte[] x) throws SQLException {
/* 1837 */     if (!this.onInsertRow) {
/* 1838 */       if (!this.doingUpdates) {
/* 1839 */         this.doingUpdates = true;
/* 1840 */         syncUpdate();
/*      */       } 
/*      */       
/* 1843 */       this.updater.setBytes(columnIndex, x);
/*      */     } else {
/* 1845 */       this.inserter.setBytes(columnIndex, x);
/*      */       
/* 1847 */       this.thisRow.setColumnValue(columnIndex - 1, x);
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
/*      */   public synchronized void updateBytes(String columnName, byte[] x) throws SQLException {
/* 1867 */     updateBytes(findColumn(columnName), x);
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
/*      */   public synchronized void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
/* 1889 */     if (!this.onInsertRow) {
/* 1890 */       if (!this.doingUpdates) {
/* 1891 */         this.doingUpdates = true;
/* 1892 */         syncUpdate();
/*      */       } 
/*      */       
/* 1895 */       this.updater.setCharacterStream(columnIndex, x, length);
/*      */     } else {
/* 1897 */       this.inserter.setCharacterStream(columnIndex, x, length);
/*      */       
/* 1899 */       if (x == null) {
/* 1900 */         this.thisRow.setColumnValue(columnIndex - 1, null);
/*      */       } else {
/* 1902 */         this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
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
/*      */   public synchronized void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
/* 1926 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateClob(int columnIndex, Clob clob) throws SQLException {
/* 1934 */     if (clob == null) {
/* 1935 */       updateNull(columnIndex);
/*      */     } else {
/* 1937 */       updateCharacterStream(columnIndex, clob.getCharacterStream(), (int)clob.length());
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
/*      */   public synchronized void updateDate(int columnIndex, Date x) throws SQLException {
/* 1958 */     if (!this.onInsertRow) {
/* 1959 */       if (!this.doingUpdates) {
/* 1960 */         this.doingUpdates = true;
/* 1961 */         syncUpdate();
/*      */       } 
/*      */       
/* 1964 */       this.updater.setDate(columnIndex, x);
/*      */     } else {
/* 1966 */       this.inserter.setDate(columnIndex, x);
/*      */       
/* 1968 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateDate(String columnName, Date x) throws SQLException {
/* 1989 */     updateDate(findColumn(columnName), x);
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
/*      */   public synchronized void updateDouble(int columnIndex, double x) throws SQLException {
/* 2008 */     if (!this.onInsertRow) {
/* 2009 */       if (!this.doingUpdates) {
/* 2010 */         this.doingUpdates = true;
/* 2011 */         syncUpdate();
/*      */       } 
/*      */       
/* 2014 */       this.updater.setDouble(columnIndex, x);
/*      */     } else {
/* 2016 */       this.inserter.setDouble(columnIndex, x);
/*      */       
/* 2018 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateDouble(String columnName, double x) throws SQLException {
/* 2039 */     updateDouble(findColumn(columnName), x);
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
/*      */   public synchronized void updateFloat(int columnIndex, float x) throws SQLException {
/* 2058 */     if (!this.onInsertRow) {
/* 2059 */       if (!this.doingUpdates) {
/* 2060 */         this.doingUpdates = true;
/* 2061 */         syncUpdate();
/*      */       } 
/*      */       
/* 2064 */       this.updater.setFloat(columnIndex, x);
/*      */     } else {
/* 2066 */       this.inserter.setFloat(columnIndex, x);
/*      */       
/* 2068 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateFloat(String columnName, float x) throws SQLException {
/* 2089 */     updateFloat(findColumn(columnName), x);
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
/*      */   public synchronized void updateInt(int columnIndex, int x) throws SQLException {
/* 2108 */     if (!this.onInsertRow) {
/* 2109 */       if (!this.doingUpdates) {
/* 2110 */         this.doingUpdates = true;
/* 2111 */         syncUpdate();
/*      */       } 
/*      */       
/* 2114 */       this.updater.setInt(columnIndex, x);
/*      */     } else {
/* 2116 */       this.inserter.setInt(columnIndex, x);
/*      */       
/* 2118 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateInt(String columnName, int x) throws SQLException {
/* 2139 */     updateInt(findColumn(columnName), x);
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
/*      */   public synchronized void updateLong(int columnIndex, long x) throws SQLException {
/* 2158 */     if (!this.onInsertRow) {
/* 2159 */       if (!this.doingUpdates) {
/* 2160 */         this.doingUpdates = true;
/* 2161 */         syncUpdate();
/*      */       } 
/*      */       
/* 2164 */       this.updater.setLong(columnIndex, x);
/*      */     } else {
/* 2166 */       this.inserter.setLong(columnIndex, x);
/*      */       
/* 2168 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateLong(String columnName, long x) throws SQLException {
/* 2189 */     updateLong(findColumn(columnName), x);
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
/*      */   public synchronized void updateNull(int columnIndex) throws SQLException {
/* 2205 */     if (!this.onInsertRow) {
/* 2206 */       if (!this.doingUpdates) {
/* 2207 */         this.doingUpdates = true;
/* 2208 */         syncUpdate();
/*      */       } 
/*      */       
/* 2211 */       this.updater.setNull(columnIndex, 0);
/*      */     } else {
/* 2213 */       this.inserter.setNull(columnIndex, 0);
/*      */       
/* 2215 */       this.thisRow.setColumnValue(columnIndex - 1, null);
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
/*      */   public synchronized void updateNull(String columnName) throws SQLException {
/* 2232 */     updateNull(findColumn(columnName));
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
/*      */   public synchronized void updateObject(int columnIndex, Object x) throws SQLException {
/* 2251 */     if (!this.onInsertRow) {
/* 2252 */       if (!this.doingUpdates) {
/* 2253 */         this.doingUpdates = true;
/* 2254 */         syncUpdate();
/*      */       } 
/*      */       
/* 2257 */       this.updater.setObject(columnIndex, x);
/*      */     } else {
/* 2259 */       this.inserter.setObject(columnIndex, x);
/*      */       
/* 2261 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateObject(int columnIndex, Object x, int scale) throws SQLException {
/* 2286 */     if (!this.onInsertRow) {
/* 2287 */       if (!this.doingUpdates) {
/* 2288 */         this.doingUpdates = true;
/* 2289 */         syncUpdate();
/*      */       } 
/*      */       
/* 2292 */       this.updater.setObject(columnIndex, x);
/*      */     } else {
/* 2294 */       this.inserter.setObject(columnIndex, x);
/*      */       
/* 2296 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateObject(String columnName, Object x) throws SQLException {
/* 2317 */     updateObject(findColumn(columnName), x);
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
/*      */   public synchronized void updateObject(String columnName, Object x, int scale) throws SQLException {
/* 2340 */     updateObject(findColumn(columnName), x);
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
/*      */   public synchronized void updateRow() throws SQLException {
/* 2354 */     if (!this.isUpdatable) {
/* 2355 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/* 2358 */     if (this.doingUpdates) {
/* 2359 */       this.updater.executeUpdate();
/* 2360 */       refreshRow();
/* 2361 */       this.doingUpdates = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2367 */     syncUpdate();
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
/*      */   public synchronized void updateShort(int columnIndex, short x) throws SQLException {
/* 2386 */     if (!this.onInsertRow) {
/* 2387 */       if (!this.doingUpdates) {
/* 2388 */         this.doingUpdates = true;
/* 2389 */         syncUpdate();
/*      */       } 
/*      */       
/* 2392 */       this.updater.setShort(columnIndex, x);
/*      */     } else {
/* 2394 */       this.inserter.setShort(columnIndex, x);
/*      */       
/* 2396 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateShort(String columnName, short x) throws SQLException {
/* 2417 */     updateShort(findColumn(columnName), x);
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
/*      */   public synchronized void updateString(int columnIndex, String x) throws SQLException {
/* 2436 */     checkClosed();
/*      */     
/* 2438 */     if (!this.onInsertRow) {
/* 2439 */       if (!this.doingUpdates) {
/* 2440 */         this.doingUpdates = true;
/* 2441 */         syncUpdate();
/*      */       } 
/*      */       
/* 2444 */       this.updater.setString(columnIndex, x);
/*      */     } else {
/* 2446 */       this.inserter.setString(columnIndex, x);
/*      */       
/* 2448 */       if (x == null) {
/* 2449 */         this.thisRow.setColumnValue(columnIndex - 1, null);
/*      */       }
/* 2451 */       else if (getCharConverter() != null) {
/* 2452 */         this.thisRow.setColumnValue(columnIndex - 1, StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2457 */         this.thisRow.setColumnValue(columnIndex - 1, x.getBytes());
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
/*      */   public synchronized void updateString(String columnName, String x) throws SQLException {
/* 2479 */     updateString(findColumn(columnName), x);
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
/*      */   public synchronized void updateTime(int columnIndex, Time x) throws SQLException {
/* 2498 */     if (!this.onInsertRow) {
/* 2499 */       if (!this.doingUpdates) {
/* 2500 */         this.doingUpdates = true;
/* 2501 */         syncUpdate();
/*      */       } 
/*      */       
/* 2504 */       this.updater.setTime(columnIndex, x);
/*      */     } else {
/* 2506 */       this.inserter.setTime(columnIndex, x);
/*      */       
/* 2508 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateTime(String columnName, Time x) throws SQLException {
/* 2529 */     updateTime(findColumn(columnName), x);
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
/*      */   public synchronized void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
/* 2548 */     if (!this.onInsertRow) {
/* 2549 */       if (!this.doingUpdates) {
/* 2550 */         this.doingUpdates = true;
/* 2551 */         syncUpdate();
/*      */       } 
/*      */       
/* 2554 */       this.updater.setTimestamp(columnIndex, x);
/*      */     } else {
/* 2556 */       this.inserter.setTimestamp(columnIndex, x);
/*      */       
/* 2558 */       this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
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
/*      */   public synchronized void updateTimestamp(String columnName, Timestamp x) throws SQLException {
/* 2579 */     updateTimestamp(findColumn(columnName), x);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\UpdatableResultSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */