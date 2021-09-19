/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandlerFactory;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ResultSetImpl
/*      */   implements ResultSetInternalMethods
/*      */ {
/*      */   private static final Constructor JDBC_4_RS_4_ARG_CTOR;
/*      */   private static final Constructor JDBC_4_RS_6_ARG_CTOR;
/*      */   private static final Constructor JDBC_4_UPD_RS_6_ARG_CTOR;
/*      */   
/*      */   static {
/*  123 */     if (Util.isJdbc4()) {
/*      */       try {
/*  125 */         JDBC_4_RS_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4ResultSet").getConstructor(new Class[] { long.class, long.class, ConnectionImpl.class, StatementImpl.class });
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  130 */         JDBC_4_RS_6_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4ResultSet").getConstructor(new Class[] { String.class, (array$Lcom$mysql$jdbc$Field == null) ? (array$Lcom$mysql$jdbc$Field = class$("[Lcom.mysql.jdbc.Field;")) : array$Lcom$mysql$jdbc$Field, RowData.class, ConnectionImpl.class, StatementImpl.class });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  136 */         JDBC_4_UPD_RS_6_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4UpdatableResultSet").getConstructor(new Class[] { String.class, (array$Lcom$mysql$jdbc$Field == null) ? (array$Lcom$mysql$jdbc$Field = class$("[Lcom.mysql.jdbc.Field;")) : array$Lcom$mysql$jdbc$Field, RowData.class, ConnectionImpl.class, StatementImpl.class });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  143 */       catch (SecurityException e) {
/*  144 */         throw new RuntimeException(e);
/*  145 */       } catch (NoSuchMethodException e) {
/*  146 */         throw new RuntimeException(e);
/*  147 */       } catch (ClassNotFoundException e) {
/*  148 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*  151 */       JDBC_4_RS_4_ARG_CTOR = null;
/*  152 */       JDBC_4_RS_6_ARG_CTOR = null;
/*  153 */       JDBC_4_UPD_RS_6_ARG_CTOR = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  160 */   protected static final double MIN_DIFF_PREC = Float.parseFloat(Float.toString(Float.MIN_VALUE)) - Double.parseDouble(Float.toString(Float.MIN_VALUE));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  166 */   protected static final double MAX_DIFF_PREC = Float.parseFloat(Float.toString(Float.MAX_VALUE)) - Double.parseDouble(Float.toString(Float.MAX_VALUE));
/*      */ 
/*      */ 
/*      */   
/*  170 */   protected static int resultCounter = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static BigInteger convertLongToUlong(long longVal) {
/*  177 */     byte[] asBytes = new byte[8];
/*  178 */     asBytes[7] = (byte)(int)(longVal & 0xFFL);
/*  179 */     asBytes[6] = (byte)(int)(longVal >>> 8L);
/*  180 */     asBytes[5] = (byte)(int)(longVal >>> 16L);
/*  181 */     asBytes[4] = (byte)(int)(longVal >>> 24L);
/*  182 */     asBytes[3] = (byte)(int)(longVal >>> 32L);
/*  183 */     asBytes[2] = (byte)(int)(longVal >>> 40L);
/*  184 */     asBytes[1] = (byte)(int)(longVal >>> 48L);
/*  185 */     asBytes[0] = (byte)(int)(longVal >>> 56L);
/*      */     
/*  187 */     return new BigInteger(1, asBytes);
/*      */   }
/*      */ 
/*      */   
/*  191 */   protected String catalog = null;
/*      */ 
/*      */   
/*  194 */   protected Map columnLabelToIndex = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  200 */   protected Map columnToIndexCache = null;
/*      */ 
/*      */   
/*  203 */   protected boolean[] columnUsed = null;
/*      */ 
/*      */   
/*      */   protected ConnectionImpl connection;
/*      */ 
/*      */   
/*  209 */   protected long connectionId = 0L;
/*      */ 
/*      */   
/*  212 */   protected int currentRow = -1;
/*      */ 
/*      */   
/*      */   TimeZone defaultTimeZone;
/*      */   
/*      */   protected boolean doingUpdates = false;
/*      */   
/*  219 */   protected ProfilerEventHandler eventSink = null;
/*      */   
/*  221 */   Calendar fastDateCal = null;
/*      */ 
/*      */   
/*  224 */   protected int fetchDirection = 1000;
/*      */ 
/*      */   
/*  227 */   protected int fetchSize = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Field[] fields;
/*      */ 
/*      */ 
/*      */   
/*      */   protected char firstCharOfQuery;
/*      */ 
/*      */ 
/*      */   
/*  240 */   protected Map fullColumnNameToIndex = null;
/*      */   
/*  242 */   protected Map columnNameToIndex = null;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasBuiltIndexMapping = false;
/*      */ 
/*      */   
/*      */   protected boolean isBinaryEncoded = false;
/*      */ 
/*      */   
/*      */   protected boolean isClosed = false;
/*      */ 
/*      */   
/*  255 */   protected ResultSetInternalMethods nextResultSet = null;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean onInsertRow = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected StatementImpl owningStatement;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Throwable pointOfOrigin;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean profileSql = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean reallyResult = false;
/*      */ 
/*      */   
/*      */   protected int resultId;
/*      */ 
/*      */   
/*  281 */   protected int resultSetConcurrency = 0;
/*      */ 
/*      */   
/*  284 */   protected int resultSetType = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RowData rowData;
/*      */ 
/*      */ 
/*      */   
/*  293 */   protected String serverInfo = null;
/*      */ 
/*      */   
/*      */   PreparedStatement statementUsedForFetchingRows;
/*      */   
/*  298 */   protected ResultSetRow thisRow = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long updateCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  312 */   protected long updateId = -1L;
/*      */ 
/*      */   
/*      */   private boolean useStrictFloatingPoint = false;
/*      */   
/*      */   protected boolean useUsageAdvisor = false;
/*      */   
/*  319 */   protected SQLWarning warningChain = null;
/*      */ 
/*      */   
/*      */   protected boolean wasNullFlag = false;
/*      */   
/*      */   protected Statement wrapperStatement;
/*      */   
/*      */   protected boolean retainOwningStatement;
/*      */   
/*  328 */   protected Calendar gmtCalendar = null;
/*      */   
/*      */   protected boolean useFastDateParsing = false;
/*      */   
/*      */   private boolean padCharsWithSpace = false;
/*      */   
/*      */   private boolean jdbcCompliantTruncationForReads;
/*      */   
/*      */   private boolean useFastIntParsing = true;
/*      */   
/*      */   private boolean useColumnNamesInFindColumn;
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*  341 */   protected static final char[] EMPTY_SPACE = new char[255]; private boolean onValidRow; private String invalidRowReason; protected boolean useLegacyDatetimeCode; private TimeZone serverTimeZoneTz; static Class array$Lcom$mysql$jdbc$Field;
/*      */   
/*      */   static {
/*  344 */     for (int i = 0; i < EMPTY_SPACE.length; i++) {
/*  345 */       EMPTY_SPACE[i] = ' ';
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static ResultSetImpl getInstance(long updateCount, long updateID, ConnectionImpl conn, StatementImpl creatorStmt) throws SQLException {
/*  351 */     if (!Util.isJdbc4()) {
/*  352 */       return new ResultSetImpl(updateCount, updateID, conn, creatorStmt);
/*      */     }
/*      */     
/*  355 */     return (ResultSetImpl)Util.handleNewInstance(JDBC_4_RS_4_ARG_CTOR, new Object[] { Constants.longValueOf(updateCount), Constants.longValueOf(updateID), conn, creatorStmt }, conn.getExceptionInterceptor());
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
/*      */   protected static ResultSetImpl getInstance(String catalog, Field[] fields, RowData tuples, ConnectionImpl conn, StatementImpl creatorStmt, boolean isUpdatable) throws SQLException {
/*  371 */     if (!Util.isJdbc4()) {
/*  372 */       if (!isUpdatable) {
/*  373 */         return new ResultSetImpl(catalog, fields, tuples, conn, creatorStmt);
/*      */       }
/*      */       
/*  376 */       return new UpdatableResultSet(catalog, fields, tuples, conn, creatorStmt);
/*      */     } 
/*      */ 
/*      */     
/*  380 */     if (!isUpdatable) {
/*  381 */       return (ResultSetImpl)Util.handleNewInstance(JDBC_4_RS_6_ARG_CTOR, new Object[] { catalog, fields, tuples, conn, creatorStmt }, conn.getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  386 */     return (ResultSetImpl)Util.handleNewInstance(JDBC_4_UPD_RS_6_ARG_CTOR, new Object[] { catalog, fields, tuples, conn, creatorStmt }, conn.getExceptionInterceptor());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initializeWithMetadata() throws SQLException {
/*  506 */     this.rowData.setMetadata(this.fields);
/*      */     
/*  508 */     this.columnToIndexCache = new HashMap();
/*      */     
/*  510 */     if (this.profileSql || this.connection.getUseUsageAdvisor()) {
/*  511 */       this.columnUsed = new boolean[this.fields.length];
/*  512 */       this.pointOfOrigin = new Throwable();
/*  513 */       this.resultId = resultCounter++;
/*  514 */       this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
/*  515 */       this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */     } 
/*      */     
/*  518 */     if (this.connection.getGatherPerformanceMetrics()) {
/*  519 */       this.connection.incrementNumberOfResultSetsCreated();
/*      */       
/*  521 */       Map tableNamesMap = new HashMap();
/*      */       
/*  523 */       for (int i = 0; i < this.fields.length; i++) {
/*  524 */         Field f = this.fields[i];
/*      */         
/*  526 */         String tableName = f.getOriginalTableName();
/*      */         
/*  528 */         if (tableName == null) {
/*  529 */           tableName = f.getTableName();
/*      */         }
/*      */         
/*  532 */         if (tableName != null) {
/*  533 */           if (this.connection.lowerCaseTableNames()) {
/*  534 */             tableName = tableName.toLowerCase();
/*      */           }
/*      */ 
/*      */           
/*  538 */           tableNamesMap.put(tableName, null);
/*      */         } 
/*      */       } 
/*      */       
/*  542 */       this.connection.reportNumberOfTablesAccessed(tableNamesMap.size());
/*      */     } 
/*      */   }
/*      */   
/*      */   private synchronized void createCalendarIfNeeded() {
/*  547 */     if (this.fastDateCal == null) {
/*  548 */       this.fastDateCal = new GregorianCalendar(Locale.US);
/*  549 */       this.fastDateCal.setTimeZone(getDefaultTimeZone());
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
/*      */   public boolean absolute(int row) throws SQLException {
/*      */     boolean b;
/*  592 */     checkClosed();
/*      */ 
/*      */ 
/*      */     
/*  596 */     if (this.rowData.size() == 0) {
/*  597 */       b = false;
/*      */     } else {
/*  599 */       if (row == 0) {
/*  600 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Cannot_absolute_position_to_row_0_110"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  606 */       if (this.onInsertRow) {
/*  607 */         this.onInsertRow = false;
/*      */       }
/*      */       
/*  610 */       if (this.doingUpdates) {
/*  611 */         this.doingUpdates = false;
/*      */       }
/*      */       
/*  614 */       if (this.thisRow != null) {
/*  615 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/*  618 */       if (row == 1) {
/*  619 */         b = first();
/*  620 */       } else if (row == -1) {
/*  621 */         b = last();
/*  622 */       } else if (row > this.rowData.size()) {
/*  623 */         afterLast();
/*  624 */         b = false;
/*      */       }
/*  626 */       else if (row < 0) {
/*      */         
/*  628 */         int newRowPosition = this.rowData.size() + row + 1;
/*      */         
/*  630 */         if (newRowPosition <= 0) {
/*  631 */           beforeFirst();
/*  632 */           b = false;
/*      */         } else {
/*  634 */           b = absolute(newRowPosition);
/*      */         } 
/*      */       } else {
/*  637 */         row--;
/*  638 */         this.rowData.setCurrentRow(row);
/*  639 */         this.thisRow = this.rowData.getAt(row);
/*  640 */         b = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  645 */     setRowPositionValidity();
/*      */     
/*  647 */     return b;
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
/*      */   public void afterLast() throws SQLException {
/*  663 */     checkClosed();
/*      */     
/*  665 */     if (this.onInsertRow) {
/*  666 */       this.onInsertRow = false;
/*      */     }
/*      */     
/*  669 */     if (this.doingUpdates) {
/*  670 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*  673 */     if (this.thisRow != null) {
/*  674 */       this.thisRow.closeOpenStreams();
/*      */     }
/*      */     
/*  677 */     if (this.rowData.size() != 0) {
/*  678 */       this.rowData.afterLast();
/*  679 */       this.thisRow = null;
/*      */     } 
/*      */     
/*  682 */     setRowPositionValidity();
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
/*      */   public void beforeFirst() throws SQLException {
/*  698 */     checkClosed();
/*      */     
/*  700 */     if (this.onInsertRow) {
/*  701 */       this.onInsertRow = false;
/*      */     }
/*      */     
/*  704 */     if (this.doingUpdates) {
/*  705 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*  708 */     if (this.rowData.size() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  712 */     if (this.thisRow != null) {
/*  713 */       this.thisRow.closeOpenStreams();
/*      */     }
/*      */     
/*  716 */     this.rowData.beforeFirst();
/*  717 */     this.thisRow = null;
/*      */     
/*  719 */     setRowPositionValidity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void buildIndexMapping() throws SQLException {
/*  730 */     int numFields = this.fields.length;
/*  731 */     this.columnLabelToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  732 */     this.fullColumnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  733 */     this.columnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  747 */     for (int i = numFields - 1; i >= 0; i--) {
/*  748 */       Integer index = Constants.integerValueOf(i);
/*  749 */       String columnName = this.fields[i].getOriginalName();
/*  750 */       String columnLabel = this.fields[i].getName();
/*  751 */       String fullColumnName = this.fields[i].getFullName();
/*      */       
/*  753 */       if (columnLabel != null) {
/*  754 */         this.columnLabelToIndex.put(columnLabel, index);
/*      */       }
/*      */       
/*  757 */       if (fullColumnName != null) {
/*  758 */         this.fullColumnNameToIndex.put(fullColumnName, index);
/*      */       }
/*      */       
/*  761 */       if (columnName != null) {
/*  762 */         this.columnNameToIndex.put(columnName, index);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  767 */     this.hasBuiltIndexMapping = true;
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
/*      */   public void cancelRowUpdates() throws SQLException {
/*  783 */     throw new NotUpdatable();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void checkClosed() throws SQLException {
/*  793 */     if (this.isClosed) {
/*  794 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), "S1000", getExceptionInterceptor());
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
/*      */   protected final void checkColumnBounds(int columnIndex) throws SQLException {
/*  811 */     if (columnIndex < 1) {
/*  812 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_low", new Object[] { Constants.integerValueOf(columnIndex), Constants.integerValueOf(this.fields.length) }), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  817 */     if (columnIndex > this.fields.length) {
/*  818 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_high", new Object[] { Constants.integerValueOf(columnIndex), Constants.integerValueOf(this.fields.length) }), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  825 */     if (this.profileSql || this.useUsageAdvisor) {
/*  826 */       this.columnUsed[columnIndex - 1] = true;
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
/*      */   protected void checkRowPos() throws SQLException {
/*  838 */     checkClosed();
/*      */     
/*  840 */     if (!this.onValidRow) {
/*  841 */       throw SQLError.createSQLException(this.invalidRowReason, "S1000", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   public ResultSetImpl(long updateCount, long updateID, ConnectionImpl conn, StatementImpl creatorStmt) {
/*  846 */     this.onValidRow = false;
/*  847 */     this.invalidRowReason = null; this.updateCount = updateCount; this.updateId = updateID; this.reallyResult = false; this.fields = new Field[0]; this.connection = conn; this.owningStatement = creatorStmt; this.exceptionInterceptor = this.connection.getExceptionInterceptor(); this.retainOwningStatement = false; if (this.connection != null) { this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose(); this.connectionId = this.connection.getId(); this.serverTimeZoneTz = this.connection.getServerTimezoneTZ(); this.padCharsWithSpace = this.connection.getPadCharsWithSpace(); }  this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode(); } public ResultSetImpl(String catalog, Field[] fields, RowData tuples, ConnectionImpl conn, StatementImpl creatorStmt) throws SQLException { this.onValidRow = false; this.invalidRowReason = null; this.connection = conn; this.retainOwningStatement = false; if (this.connection != null) { this.useStrictFloatingPoint = this.connection.getStrictFloatingPoint(); setDefaultTimeZone(this.connection.getDefaultTimeZone()); this.connectionId = this.connection.getId(); this.useFastDateParsing = this.connection.getUseFastDateParsing(); this.profileSql = this.connection.getProfileSql(); this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose(); this.jdbcCompliantTruncationForReads = this.connection.getJdbcCompliantTruncationForReads(); this.useFastIntParsing = this.connection.getUseFastIntParsing(); this.serverTimeZoneTz = this.connection.getServerTimezoneTZ(); this.padCharsWithSpace = this.connection.getPadCharsWithSpace(); }
/*      */      this.owningStatement = creatorStmt; this.catalog = catalog; this.fields = fields; this.rowData = tuples; this.updateCount = this.rowData.size(); this.reallyResult = true; if (this.rowData.size() > 0) { if (this.updateCount == 1L && this.thisRow == null) { this.rowData.close(); this.updateCount = -1L; }
/*      */        }
/*      */     else { this.thisRow = null; }
/*      */      this.rowData.setOwner(this); if (this.fields != null)
/*  852 */       initializeWithMetadata();  this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode(); this.useColumnNamesInFindColumn = this.connection.getUseColumnNamesInFindColumn(); setRowPositionValidity(); } private void setRowPositionValidity() throws SQLException { if (!this.rowData.isDynamic() && this.rowData.size() == 0) {
/*  853 */       this.invalidRowReason = Messages.getString("ResultSet.Illegal_operation_on_empty_result_set");
/*      */       
/*  855 */       this.onValidRow = false;
/*  856 */     } else if (this.rowData.isBeforeFirst()) {
/*  857 */       this.invalidRowReason = Messages.getString("ResultSet.Before_start_of_result_set_146");
/*      */       
/*  859 */       this.onValidRow = false;
/*  860 */     } else if (this.rowData.isAfterLast()) {
/*  861 */       this.invalidRowReason = Messages.getString("ResultSet.After_end_of_result_set_148");
/*      */       
/*  863 */       this.onValidRow = false;
/*      */     } else {
/*  865 */       this.onValidRow = true;
/*  866 */       this.invalidRowReason = null;
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearNextResult() {
/*  875 */     this.nextResultSet = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*  886 */     this.warningChain = null;
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
/*      */   public void close() throws SQLException {
/*  907 */     realClose(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int convertToZeroWithEmptyCheck() throws SQLException {
/*  914 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  915 */       return 0;
/*      */     }
/*      */     
/*  918 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String convertToZeroLiteralStringWithEmptyCheck() throws SQLException {
/*  925 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  926 */       return "0";
/*      */     }
/*      */     
/*  929 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSetInternalMethods copy() throws SQLException {
/*  937 */     ResultSetInternalMethods rs = getInstance(this.catalog, this.fields, this.rowData, this.connection, this.owningStatement, false);
/*      */ 
/*      */     
/*  940 */     return rs;
/*      */   }
/*      */   
/*      */   public void redefineFieldsForDBMD(Field[] f) {
/*  944 */     this.fields = f;
/*      */     
/*  946 */     for (int i = 0; i < this.fields.length; i++) {
/*  947 */       this.fields[i].setUseOldNameMetadata(true);
/*  948 */       this.fields[i].setConnection(this.connection);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void populateCachedMetaData(CachedResultSetMetaData cachedMetaData) throws SQLException {
/*  954 */     cachedMetaData.fields = this.fields;
/*  955 */     cachedMetaData.columnNameToIndex = this.columnLabelToIndex;
/*  956 */     cachedMetaData.fullColumnNameToIndex = this.fullColumnNameToIndex;
/*  957 */     cachedMetaData.metadata = getMetaData();
/*      */   }
/*      */   
/*      */   public void initializeFromCachedMetaData(CachedResultSetMetaData cachedMetaData) {
/*  961 */     this.fields = cachedMetaData.fields;
/*  962 */     this.columnLabelToIndex = cachedMetaData.columnNameToIndex;
/*  963 */     this.fullColumnNameToIndex = cachedMetaData.fullColumnNameToIndex;
/*  964 */     this.hasBuiltIndexMapping = true;
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
/*      */   public void deleteRow() throws SQLException {
/*  979 */     throw new NotUpdatable();
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
/*      */   private String extractStringFromNativeColumn(int columnIndex, int mysqlType) throws SQLException {
/*  991 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/*  993 */     this.wasNullFlag = false;
/*      */     
/*  995 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/*  996 */       this.wasNullFlag = true;
/*      */       
/*  998 */       return null;
/*      */     } 
/*      */     
/* 1001 */     this.wasNullFlag = false;
/*      */     
/* 1003 */     String encoding = this.fields[columnIndexMinusOne].getCharacterSet();
/*      */ 
/*      */     
/* 1006 */     return this.thisRow.getString(columnIndex - 1, encoding, this.connection);
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized Date fastDateCreate(Calendar cal, int year, int month, int day) {
/* 1011 */     if (this.useLegacyDatetimeCode) {
/* 1012 */       return TimeUtil.fastDateCreate(year, month, day, cal);
/*      */     }
/*      */     
/* 1015 */     if (cal == null) {
/* 1016 */       createCalendarIfNeeded();
/* 1017 */       cal = this.fastDateCal;
/*      */     } 
/*      */     
/* 1020 */     boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */     
/* 1022 */     return TimeUtil.fastDateCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : cal, cal, year, month, day);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized Time fastTimeCreate(Calendar cal, int hour, int minute, int second) throws SQLException {
/* 1029 */     if (!this.useLegacyDatetimeCode) {
/* 1030 */       return TimeUtil.fastTimeCreate(hour, minute, second, cal, getExceptionInterceptor());
/*      */     }
/*      */     
/* 1033 */     if (cal == null) {
/* 1034 */       createCalendarIfNeeded();
/* 1035 */       cal = this.fastDateCal;
/*      */     } 
/*      */     
/* 1038 */     return TimeUtil.fastTimeCreate(cal, hour, minute, second, getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized Timestamp fastTimestampCreate(Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart) {
/* 1044 */     if (!this.useLegacyDatetimeCode) {
/* 1045 */       return TimeUtil.fastTimestampCreate(cal.getTimeZone(), year, month, day, hour, minute, seconds, secondsPart);
/*      */     }
/*      */ 
/*      */     
/* 1049 */     if (cal == null) {
/* 1050 */       createCalendarIfNeeded();
/* 1051 */       cal = this.fastDateCal;
/*      */     } 
/*      */     
/* 1054 */     boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */     
/* 1056 */     return TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : null, cal, year, month, day, hour, minute, seconds, secondsPart);
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
/*      */   public synchronized int findColumn(String columnName) throws SQLException {
/* 1105 */     checkClosed();
/*      */     
/* 1107 */     if (!this.hasBuiltIndexMapping) {
/* 1108 */       buildIndexMapping();
/*      */     }
/*      */     
/* 1111 */     Integer index = (Integer)this.columnToIndexCache.get(columnName);
/*      */     
/* 1113 */     if (index != null) {
/* 1114 */       return index.intValue() + 1;
/*      */     }
/*      */     
/* 1117 */     index = (Integer)this.columnLabelToIndex.get(columnName);
/*      */     
/* 1119 */     if (index == null && this.useColumnNamesInFindColumn) {
/* 1120 */       index = (Integer)this.columnNameToIndex.get(columnName);
/*      */     }
/*      */     
/* 1123 */     if (index == null) {
/* 1124 */       index = (Integer)this.fullColumnNameToIndex.get(columnName);
/*      */     }
/*      */     
/* 1127 */     if (index != null) {
/* 1128 */       this.columnToIndexCache.put(columnName, index);
/*      */       
/* 1130 */       return index.intValue() + 1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1135 */     for (int i = 0; i < this.fields.length; i++) {
/* 1136 */       if (this.fields[i].getName().equalsIgnoreCase(columnName))
/* 1137 */         return i + 1; 
/* 1138 */       if (this.fields[i].getFullName().equalsIgnoreCase(columnName))
/*      */       {
/* 1140 */         return i + 1;
/*      */       }
/*      */     } 
/*      */     
/* 1144 */     throw SQLError.createSQLException(Messages.getString("ResultSet.Column____112") + columnName + Messages.getString("ResultSet.___not_found._113"), "S0022", getExceptionInterceptor());
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
/*      */   public boolean first() throws SQLException {
/* 1164 */     checkClosed();
/*      */     
/* 1166 */     boolean b = true;
/*      */     
/* 1168 */     if (this.rowData.isEmpty()) {
/* 1169 */       b = false;
/*      */     } else {
/*      */       
/* 1172 */       if (this.onInsertRow) {
/* 1173 */         this.onInsertRow = false;
/*      */       }
/*      */       
/* 1176 */       if (this.doingUpdates) {
/* 1177 */         this.doingUpdates = false;
/*      */       }
/*      */       
/* 1180 */       this.rowData.beforeFirst();
/* 1181 */       this.thisRow = this.rowData.next();
/*      */     } 
/*      */     
/* 1184 */     setRowPositionValidity();
/*      */     
/* 1186 */     return b;
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
/*      */   public Array getArray(int i) throws SQLException {
/* 1203 */     checkColumnBounds(i);
/*      */     
/* 1205 */     throw SQLError.notImplemented();
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
/*      */   public Array getArray(String colName) throws SQLException {
/* 1222 */     return getArray(findColumn(colName));
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
/*      */   public InputStream getAsciiStream(int columnIndex) throws SQLException {
/* 1251 */     checkRowPos();
/*      */     
/* 1253 */     if (!this.isBinaryEncoded) {
/* 1254 */       return getBinaryStream(columnIndex);
/*      */     }
/*      */     
/* 1257 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getAsciiStream(String columnName) throws SQLException {
/* 1272 */     return getAsciiStream(findColumn(columnName));
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
/*      */   public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
/* 1289 */     if (!this.isBinaryEncoded) {
/* 1290 */       String stringVal = getString(columnIndex);
/*      */ 
/*      */       
/* 1293 */       if (stringVal != null) {
/* 1294 */         if (stringVal.length() == 0) {
/*      */           
/* 1296 */           BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */ 
/*      */           
/* 1299 */           return val;
/*      */         } 
/*      */         
/*      */         try {
/* 1303 */           BigDecimal val = new BigDecimal(stringVal);
/*      */           
/* 1305 */           return val;
/* 1306 */         } catch (NumberFormatException ex) {
/* 1307 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1315 */       return null;
/*      */     } 
/*      */     
/* 1318 */     return getNativeBigDecimal(columnIndex);
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
/*      */   public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
/* 1339 */     if (!this.isBinaryEncoded) {
/* 1340 */       String stringVal = getString(columnIndex);
/*      */ 
/*      */       
/* 1343 */       if (stringVal != null) {
/* 1344 */         BigDecimal val; if (stringVal.length() == 0) {
/* 1345 */           val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */ 
/*      */           
/*      */           try {
/* 1349 */             return val.setScale(scale);
/* 1350 */           } catch (ArithmeticException ex) {
/*      */             try {
/* 1352 */               return val.setScale(scale, 4);
/*      */             }
/* 1354 */             catch (ArithmeticException arEx) {
/* 1355 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1365 */           val = new BigDecimal(stringVal);
/* 1366 */         } catch (NumberFormatException ex) {
/* 1367 */           if (this.fields[columnIndex - 1].getMysqlType() == 16) {
/* 1368 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 1370 */             val = new BigDecimal(valueAsLong);
/*      */           } else {
/* 1372 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Constants.integerValueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1381 */           return val.setScale(scale);
/* 1382 */         } catch (ArithmeticException ex) {
/*      */           try {
/* 1384 */             return val.setScale(scale, 4);
/* 1385 */           } catch (ArithmeticException arithEx) {
/* 1386 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Constants.integerValueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1395 */       return null;
/*      */     } 
/*      */     
/* 1398 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   public BigDecimal getBigDecimal(String columnName) throws SQLException {
/* 1414 */     return getBigDecimal(findColumn(columnName));
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
/*      */   public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
/* 1434 */     return getBigDecimal(findColumn(columnName), scale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final BigDecimal getBigDecimalFromString(String stringVal, int columnIndex, int scale) throws SQLException {
/* 1441 */     if (stringVal != null) {
/* 1442 */       if (stringVal.length() == 0) {
/* 1443 */         BigDecimal bdVal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */         
/*      */         try {
/* 1446 */           return bdVal.setScale(scale);
/* 1447 */         } catch (ArithmeticException ex) {
/*      */           try {
/* 1449 */             return bdVal.setScale(scale, 4);
/* 1450 */           } catch (ArithmeticException arEx) {
/* 1451 */             throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1462 */         return (new BigDecimal(stringVal)).setScale(scale);
/* 1463 */       } catch (ArithmeticException ex) {
/*      */         try {
/* 1465 */           return (new BigDecimal(stringVal)).setScale(scale, 4);
/*      */         }
/* 1467 */         catch (ArithmeticException arEx) {
/* 1468 */           throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009");
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1475 */       catch (NumberFormatException ex) {
/* 1476 */         if (this.fields[columnIndex - 1].getMysqlType() == 16) {
/* 1477 */           long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */           
/*      */           try {
/* 1480 */             return (new BigDecimal(valueAsLong)).setScale(scale);
/* 1481 */           } catch (ArithmeticException arEx1) {
/*      */             try {
/* 1483 */               return (new BigDecimal(valueAsLong)).setScale(scale, 4);
/*      */             }
/* 1485 */             catch (ArithmeticException arEx2) {
/* 1486 */               throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1495 */         if (this.fields[columnIndex - 1].getMysqlType() == 1 && this.connection.getTinyInt1isBit() && this.fields[columnIndex - 1].getLength() == 1L)
/*      */         {
/* 1497 */           return (new BigDecimal(stringVal.equalsIgnoreCase("true") ? 1.0D : 0.0D)).setScale(scale);
/*      */         }
/*      */         
/* 1500 */         throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1508 */     return null;
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
/*      */   public InputStream getBinaryStream(int columnIndex) throws SQLException {
/* 1529 */     checkRowPos();
/*      */     
/* 1531 */     if (!this.isBinaryEncoded) {
/* 1532 */       checkColumnBounds(columnIndex);
/*      */       
/* 1534 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1536 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1537 */         this.wasNullFlag = true;
/*      */         
/* 1539 */         return null;
/*      */       } 
/*      */       
/* 1542 */       this.wasNullFlag = false;
/*      */       
/* 1544 */       return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
/*      */     } 
/*      */     
/* 1547 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getBinaryStream(String columnName) throws SQLException {
/* 1562 */     return getBinaryStream(findColumn(columnName));
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
/*      */   public Blob getBlob(int columnIndex) throws SQLException {
/* 1577 */     if (!this.isBinaryEncoded) {
/* 1578 */       checkRowPos();
/*      */       
/* 1580 */       checkColumnBounds(columnIndex);
/*      */       
/* 1582 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1584 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1585 */         this.wasNullFlag = true;
/*      */       } else {
/* 1587 */         this.wasNullFlag = false;
/*      */       } 
/*      */       
/* 1590 */       if (this.wasNullFlag) {
/* 1591 */         return null;
/*      */       }
/*      */       
/* 1594 */       if (!this.connection.getEmulateLocators()) {
/* 1595 */         return new Blob(this.thisRow.getColumnValue(columnIndexMinusOne), getExceptionInterceptor());
/*      */       }
/*      */       
/* 1598 */       return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
/*      */     } 
/*      */     
/* 1601 */     return getNativeBlob(columnIndex);
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
/*      */   public Blob getBlob(String colName) throws SQLException {
/* 1616 */     return getBlob(findColumn(colName));
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
/*      */   public boolean getBoolean(int columnIndex) throws SQLException {
/*      */     long boolVal;
/* 1632 */     checkColumnBounds(columnIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1639 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 1641 */     Field field = this.fields[columnIndexMinusOne];
/*      */     
/* 1643 */     if (field.getMysqlType() == 16) {
/* 1644 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1647 */     this.wasNullFlag = false;
/*      */     
/* 1649 */     int sqlType = field.getSQLType();
/*      */     
/* 1651 */     switch (sqlType) {
/*      */       case 16:
/* 1653 */         if (field.getMysqlType() == -1) {
/* 1654 */           String str = getString(columnIndex);
/*      */           
/* 1656 */           return getBooleanFromString(str, columnIndex);
/*      */         } 
/*      */         
/* 1659 */         boolVal = getLong(columnIndex, false);
/*      */         
/* 1661 */         return (boolVal == -1L || boolVal > 0L);
/*      */       case -7:
/*      */       case -6:
/*      */       case -5:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/* 1672 */         boolVal = getLong(columnIndex, false);
/*      */         
/* 1674 */         return (boolVal == -1L || boolVal > 0L);
/*      */     } 
/* 1676 */     if (this.connection.getPedantic())
/*      */     {
/* 1678 */       switch (sqlType) {
/*      */         case -4:
/*      */         case -3:
/*      */         case -2:
/*      */         case 70:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 2000:
/*      */         case 2002:
/*      */         case 2003:
/*      */         case 2004:
/*      */         case 2005:
/*      */         case 2006:
/* 1692 */           throw SQLError.createSQLException("Required type conversion not allowed", "22018", getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */     
/*      */     }
/* 1697 */     if (sqlType == -2 || sqlType == -3 || sqlType == -4 || sqlType == 2004)
/*      */     {
/*      */ 
/*      */       
/* 1701 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1704 */     if (this.useUsageAdvisor) {
/* 1705 */       issueConversionViaParsingWarning("getBoolean()", columnIndex, this.thisRow.getColumnValue(columnIndexMinusOne), this.fields[columnIndex], new int[] { 16, 5, 1, 2, 3, 8, 4 });
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
/* 1717 */     String stringVal = getString(columnIndex);
/*      */     
/* 1719 */     return getBooleanFromString(stringVal, columnIndex);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean byteArrayToBoolean(int columnIndexMinusOne) throws SQLException {
/* 1724 */     Object value = this.thisRow.getColumnValue(columnIndexMinusOne);
/*      */     
/* 1726 */     if (value == null) {
/* 1727 */       this.wasNullFlag = true;
/*      */       
/* 1729 */       return false;
/*      */     } 
/*      */     
/* 1732 */     this.wasNullFlag = false;
/*      */     
/* 1734 */     if (((byte[])value).length == 0) {
/* 1735 */       return false;
/*      */     }
/*      */     
/* 1738 */     byte boolVal = ((byte[])value)[0];
/*      */     
/* 1740 */     if (boolVal == 49)
/* 1741 */       return true; 
/* 1742 */     if (boolVal == 48) {
/* 1743 */       return false;
/*      */     }
/*      */     
/* 1746 */     return (boolVal == -1 || boolVal > 0);
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
/*      */   public boolean getBoolean(String columnName) throws SQLException {
/* 1761 */     return getBoolean(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean getBooleanFromString(String stringVal, int columnIndex) throws SQLException {
/* 1766 */     if (stringVal != null && stringVal.length() > 0) {
/* 1767 */       int c = Character.toLowerCase(stringVal.charAt(0));
/*      */       
/* 1769 */       return (c == 116 || c == 121 || c == 49 || stringVal.equals("-1"));
/*      */     } 
/*      */ 
/*      */     
/* 1773 */     return false;
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
/*      */   public byte getByte(int columnIndex) throws SQLException {
/* 1788 */     if (!this.isBinaryEncoded) {
/* 1789 */       String stringVal = getString(columnIndex);
/*      */       
/* 1791 */       if (this.wasNullFlag || stringVal == null) {
/* 1792 */         return 0;
/*      */       }
/*      */       
/* 1795 */       return getByteFromString(stringVal, columnIndex);
/*      */     } 
/*      */     
/* 1798 */     return getNativeByte(columnIndex);
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
/*      */   public byte getByte(String columnName) throws SQLException {
/* 1813 */     return getByte(findColumn(columnName));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte getByteFromString(String stringVal, int columnIndex) throws SQLException {
/* 1819 */     if (stringVal != null && stringVal.length() == 0) {
/* 1820 */       return (byte)convertToZeroWithEmptyCheck();
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
/* 1831 */     if (stringVal == null) {
/* 1832 */       return 0;
/*      */     }
/*      */     
/* 1835 */     stringVal = stringVal.trim();
/*      */     
/*      */     try {
/* 1838 */       int decimalIndex = stringVal.indexOf(".");
/*      */ 
/*      */       
/* 1841 */       if (decimalIndex != -1) {
/* 1842 */         double valueAsDouble = Double.parseDouble(stringVal);
/*      */         
/* 1844 */         if (this.jdbcCompliantTruncationForReads && (
/* 1845 */           valueAsDouble < -128.0D || valueAsDouble > 127.0D))
/*      */         {
/* 1847 */           throwRangeException(stringVal, columnIndex, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1852 */         return (byte)(int)valueAsDouble;
/*      */       } 
/*      */       
/* 1855 */       long valueAsLong = Long.parseLong(stringVal);
/*      */       
/* 1857 */       if (this.jdbcCompliantTruncationForReads && (
/* 1858 */         valueAsLong < -128L || valueAsLong > 127L))
/*      */       {
/* 1860 */         throwRangeException(String.valueOf(valueAsLong), columnIndex, -6);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1865 */       return (byte)(int)valueAsLong;
/* 1866 */     } catch (NumberFormatException NFE) {
/* 1867 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Value____173") + stringVal + Messages.getString("ResultSet.___is_out_of_range_[-127,127]_174"), "S1009", getExceptionInterceptor());
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
/*      */   public byte[] getBytes(int columnIndex) throws SQLException {
/* 1892 */     return getBytes(columnIndex, false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected byte[] getBytes(int columnIndex, boolean noConversion) throws SQLException {
/* 1897 */     if (!this.isBinaryEncoded) {
/* 1898 */       checkRowPos();
/*      */       
/* 1900 */       checkColumnBounds(columnIndex);
/*      */       
/* 1902 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1904 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1905 */         this.wasNullFlag = true;
/*      */       } else {
/* 1907 */         this.wasNullFlag = false;
/*      */       } 
/*      */       
/* 1910 */       if (this.wasNullFlag) {
/* 1911 */         return null;
/*      */       }
/*      */       
/* 1914 */       return this.thisRow.getColumnValue(columnIndexMinusOne);
/*      */     } 
/*      */     
/* 1917 */     return getNativeBytes(columnIndex, noConversion);
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
/*      */   public byte[] getBytes(String columnName) throws SQLException {
/* 1932 */     return getBytes(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final byte[] getBytesFromString(String stringVal, int columnIndex) throws SQLException {
/* 1937 */     if (stringVal != null) {
/* 1938 */       return StringUtils.getBytes(stringVal, this.connection.getEncoding(), this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection, getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1945 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Calendar getCalendarInstanceForSessionOrNew() {
/* 1953 */     if (this.connection != null) {
/* 1954 */       return this.connection.getCalendarInstanceForSessionOrNew();
/*      */     }
/*      */     
/* 1957 */     return new GregorianCalendar();
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
/*      */   public Reader getCharacterStream(int columnIndex) throws SQLException {
/* 1978 */     if (!this.isBinaryEncoded) {
/* 1979 */       checkColumnBounds(columnIndex);
/*      */       
/* 1981 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1983 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1984 */         this.wasNullFlag = true;
/*      */         
/* 1986 */         return null;
/*      */       } 
/*      */       
/* 1989 */       this.wasNullFlag = false;
/*      */       
/* 1991 */       return this.thisRow.getReader(columnIndexMinusOne);
/*      */     } 
/*      */     
/* 1994 */     return getNativeCharacterStream(columnIndex);
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
/*      */   public Reader getCharacterStream(String columnName) throws SQLException {
/* 2014 */     return getCharacterStream(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final Reader getCharacterStreamFromString(String stringVal, int columnIndex) throws SQLException {
/* 2019 */     if (stringVal != null) {
/* 2020 */       return new StringReader(stringVal);
/*      */     }
/*      */     
/* 2023 */     return null;
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
/*      */   public Clob getClob(int i) throws SQLException {
/* 2038 */     if (!this.isBinaryEncoded) {
/* 2039 */       String asString = getStringForClob(i);
/*      */       
/* 2041 */       if (asString == null) {
/* 2042 */         return null;
/*      */       }
/*      */       
/* 2045 */       return new Clob(asString, getExceptionInterceptor());
/*      */     } 
/*      */     
/* 2048 */     return getNativeClob(i);
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
/*      */   public Clob getClob(String colName) throws SQLException {
/* 2063 */     return getClob(findColumn(colName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final Clob getClobFromString(String stringVal, int columnIndex) throws SQLException {
/* 2068 */     return new Clob(stringVal, getExceptionInterceptor());
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
/* 2081 */     return 1007;
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
/*      */   public String getCursorName() throws SQLException {
/* 2110 */     throw SQLError.createSQLException(Messages.getString("ResultSet.Positioned_Update_not_supported"), "S1C00", getExceptionInterceptor());
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
/*      */   public Date getDate(int columnIndex) throws SQLException {
/* 2127 */     return getDate(columnIndex, (Calendar)null);
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
/*      */   public Date getDate(int columnIndex, Calendar cal) throws SQLException {
/* 2148 */     if (this.isBinaryEncoded) {
/* 2149 */       return getNativeDate(columnIndex, cal);
/*      */     }
/*      */     
/* 2152 */     if (!this.useFastDateParsing) {
/* 2153 */       String stringVal = getStringInternal(columnIndex, false);
/*      */       
/* 2155 */       if (stringVal == null) {
/* 2156 */         return null;
/*      */       }
/*      */       
/* 2159 */       return getDateFromString(stringVal, columnIndex, cal);
/*      */     } 
/*      */     
/* 2162 */     checkColumnBounds(columnIndex);
/*      */     
/* 2164 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 2166 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2167 */       this.wasNullFlag = true;
/*      */       
/* 2169 */       return null;
/*      */     } 
/*      */     
/* 2172 */     this.wasNullFlag = false;
/*      */     
/* 2174 */     return this.thisRow.getDateFast(columnIndexMinusOne, this.connection, this, cal);
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
/*      */   public Date getDate(String columnName) throws SQLException {
/* 2190 */     return getDate(findColumn(columnName));
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
/*      */   public Date getDate(String columnName, Calendar cal) throws SQLException {
/* 2210 */     return getDate(findColumn(columnName), cal);
/*      */   }
/*      */ 
/*      */   
/*      */   private final Date getDateFromString(String stringVal, int columnIndex, Calendar targetCalendar) throws SQLException {
/* 2215 */     int year = 0;
/* 2216 */     int month = 0;
/* 2217 */     int day = 0;
/*      */     
/*      */     try {
/* 2220 */       this.wasNullFlag = false;
/*      */       
/* 2222 */       if (stringVal == null) {
/* 2223 */         this.wasNullFlag = true;
/*      */         
/* 2225 */         return null;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2236 */       stringVal = stringVal.trim();
/*      */       
/* 2238 */       if (stringVal.equals("0") || stringVal.equals("0000-00-00") || stringVal.equals("0000-00-00 00:00:00") || stringVal.equals("00000000000000") || stringVal.equals("0")) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2243 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
/*      */           
/* 2245 */           this.wasNullFlag = true;
/*      */           
/* 2247 */           return null;
/* 2248 */         }  if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2250 */           throw SQLError.createSQLException("Value '" + stringVal + "' can not be represented as java.sql.Date", "S1009", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2257 */         return fastDateCreate(targetCalendar, 1, 1, 1);
/*      */       } 
/* 2259 */       if (this.fields[columnIndex - 1].getMysqlType() == 7) {
/*      */         
/* 2261 */         switch (stringVal.length()) {
/*      */           case 19:
/*      */           case 21:
/* 2264 */             year = Integer.parseInt(stringVal.substring(0, 4));
/* 2265 */             month = Integer.parseInt(stringVal.substring(5, 7));
/* 2266 */             day = Integer.parseInt(stringVal.substring(8, 10));
/*      */             
/* 2268 */             return fastDateCreate(targetCalendar, year, month, day);
/*      */ 
/*      */           
/*      */           case 8:
/*      */           case 14:
/* 2273 */             year = Integer.parseInt(stringVal.substring(0, 4));
/* 2274 */             month = Integer.parseInt(stringVal.substring(4, 6));
/* 2275 */             day = Integer.parseInt(stringVal.substring(6, 8));
/*      */             
/* 2277 */             return fastDateCreate(targetCalendar, year, month, day);
/*      */ 
/*      */           
/*      */           case 6:
/*      */           case 10:
/*      */           case 12:
/* 2283 */             year = Integer.parseInt(stringVal.substring(0, 2));
/*      */             
/* 2285 */             if (year <= 69) {
/* 2286 */               year += 100;
/*      */             }
/*      */             
/* 2289 */             month = Integer.parseInt(stringVal.substring(2, 4));
/* 2290 */             day = Integer.parseInt(stringVal.substring(4, 6));
/*      */             
/* 2292 */             return fastDateCreate(targetCalendar, year + 1900, month, day);
/*      */ 
/*      */           
/*      */           case 4:
/* 2296 */             year = Integer.parseInt(stringVal.substring(0, 4));
/*      */             
/* 2298 */             if (year <= 69) {
/* 2299 */               year += 100;
/*      */             }
/*      */             
/* 2302 */             month = Integer.parseInt(stringVal.substring(2, 4));
/*      */             
/* 2304 */             return fastDateCreate(targetCalendar, year + 1900, month, 1);
/*      */ 
/*      */           
/*      */           case 2:
/* 2308 */             year = Integer.parseInt(stringVal.substring(0, 2));
/*      */             
/* 2310 */             if (year <= 69) {
/* 2311 */               year += 100;
/*      */             }
/*      */             
/* 2314 */             return fastDateCreate(targetCalendar, year + 1900, 1, 1);
/*      */         } 
/*      */ 
/*      */         
/* 2318 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2323 */       if (this.fields[columnIndex - 1].getMysqlType() == 13) {
/*      */         
/* 2325 */         if (stringVal.length() == 2 || stringVal.length() == 1) {
/* 2326 */           year = Integer.parseInt(stringVal);
/*      */           
/* 2328 */           if (year <= 69) {
/* 2329 */             year += 100;
/*      */           }
/*      */           
/* 2332 */           year += 1900;
/*      */         } else {
/* 2334 */           year = Integer.parseInt(stringVal.substring(0, 4));
/*      */         } 
/*      */         
/* 2337 */         return fastDateCreate(targetCalendar, year, 1, 1);
/* 2338 */       }  if (this.fields[columnIndex - 1].getMysqlType() == 11) {
/* 2339 */         return fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */       }
/* 2341 */       if (stringVal.length() < 10) {
/* 2342 */         if (stringVal.length() == 8) {
/* 2343 */           return fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */         }
/*      */         
/* 2346 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2352 */       if (stringVal.length() != 18) {
/* 2353 */         year = Integer.parseInt(stringVal.substring(0, 4));
/* 2354 */         month = Integer.parseInt(stringVal.substring(5, 7));
/* 2355 */         day = Integer.parseInt(stringVal.substring(8, 10));
/*      */       } else {
/*      */         
/* 2358 */         StringTokenizer st = new StringTokenizer(stringVal, "- ");
/*      */         
/* 2360 */         year = Integer.parseInt(st.nextToken());
/* 2361 */         month = Integer.parseInt(st.nextToken());
/* 2362 */         day = Integer.parseInt(st.nextToken());
/*      */       } 
/*      */ 
/*      */       
/* 2366 */       return fastDateCreate(targetCalendar, year, month, day);
/* 2367 */     } catch (SQLException sqlEx) {
/* 2368 */       throw sqlEx;
/* 2369 */     } catch (Exception e) {
/* 2370 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2375 */       sqlEx.initCause(e);
/*      */       
/* 2377 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */   
/*      */   private TimeZone getDefaultTimeZone() {
/* 2382 */     if (!this.useLegacyDatetimeCode && this.connection != null) {
/* 2383 */       return this.serverTimeZoneTz;
/*      */     }
/*      */     
/* 2386 */     return this.connection.getDefaultTimeZone();
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
/*      */   public double getDouble(int columnIndex) throws SQLException {
/* 2401 */     if (!this.isBinaryEncoded) {
/* 2402 */       return getDoubleInternal(columnIndex);
/*      */     }
/*      */     
/* 2405 */     return getNativeDouble(columnIndex);
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
/*      */   public double getDouble(String columnName) throws SQLException {
/* 2420 */     return getDouble(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final double getDoubleFromString(String stringVal, int columnIndex) throws SQLException {
/* 2425 */     return getDoubleInternal(stringVal, columnIndex);
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
/*      */   protected double getDoubleInternal(int colIndex) throws SQLException {
/* 2441 */     return getDoubleInternal(getString(colIndex), colIndex);
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
/*      */   protected double getDoubleInternal(String stringVal, int colIndex) throws SQLException {
/*      */     try {
/* 2461 */       if (stringVal == null) {
/* 2462 */         return 0.0D;
/*      */       }
/*      */       
/* 2465 */       if (stringVal.length() == 0) {
/* 2466 */         return convertToZeroWithEmptyCheck();
/*      */       }
/*      */       
/* 2469 */       double d = Double.parseDouble(stringVal);
/*      */       
/* 2471 */       if (this.useStrictFloatingPoint)
/*      */       {
/* 2473 */         if (d == 2.147483648E9D) {
/*      */           
/* 2475 */           d = 2.147483647E9D;
/* 2476 */         } else if (d == 1.0000000036275E-15D) {
/*      */           
/* 2478 */           d = 1.0E-15D;
/* 2479 */         } else if (d == 9.999999869911E14D) {
/* 2480 */           d = 9.99999999999999E14D;
/* 2481 */         } else if (d == 1.4012984643248E-45D) {
/* 2482 */           d = 1.4E-45D;
/* 2483 */         } else if (d == 1.4013E-45D) {
/* 2484 */           d = 1.4E-45D;
/* 2485 */         } else if (d == 3.4028234663853E37D) {
/* 2486 */           d = 3.4028235E37D;
/* 2487 */         } else if (d == -2.14748E9D) {
/* 2488 */           d = -2.147483648E9D;
/* 2489 */         } else if (d == 3.40282E37D) {
/* 2490 */           d = 3.4028235E37D;
/*      */         } 
/*      */       }
/*      */       
/* 2494 */       return d;
/* 2495 */     } catch (NumberFormatException e) {
/* 2496 */       if (this.fields[colIndex - 1].getMysqlType() == 16) {
/* 2497 */         long valueAsLong = getNumericRepresentationOfSQLBitType(colIndex);
/*      */         
/* 2499 */         return valueAsLong;
/*      */       } 
/*      */       
/* 2502 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_number", new Object[] { stringVal, Constants.integerValueOf(colIndex) }), "S1009", getExceptionInterceptor());
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
/*      */   public int getFetchDirection() throws SQLException {
/* 2518 */     return this.fetchDirection;
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
/*      */   public int getFetchSize() throws SQLException {
/* 2530 */     return this.fetchSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getFirstCharOfQuery() {
/* 2540 */     return this.firstCharOfQuery;
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
/*      */   public float getFloat(int columnIndex) throws SQLException {
/* 2555 */     if (!this.isBinaryEncoded) {
/* 2556 */       String val = null;
/*      */       
/* 2558 */       val = getString(columnIndex);
/*      */       
/* 2560 */       return getFloatFromString(val, columnIndex);
/*      */     } 
/*      */     
/* 2563 */     return getNativeFloat(columnIndex);
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
/*      */   public float getFloat(String columnName) throws SQLException {
/* 2578 */     return getFloat(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final float getFloatFromString(String val, int columnIndex) throws SQLException {
/*      */     try {
/* 2584 */       if (val != null) {
/* 2585 */         if (val.length() == 0) {
/* 2586 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2589 */         float f = Float.parseFloat(val);
/*      */         
/* 2591 */         if (this.jdbcCompliantTruncationForReads && (
/* 2592 */           f == Float.MIN_VALUE || f == Float.MAX_VALUE)) {
/* 2593 */           double valAsDouble = Double.parseDouble(val);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2599 */           if (valAsDouble < 1.401298464324817E-45D - MIN_DIFF_PREC || valAsDouble > 3.4028234663852886E38D - MAX_DIFF_PREC)
/*      */           {
/* 2601 */             throwRangeException(String.valueOf(valAsDouble), columnIndex, 6);
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2607 */         return f;
/*      */       } 
/*      */       
/* 2610 */       return 0.0F;
/* 2611 */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2613 */         Double valueAsDouble = new Double(val);
/* 2614 */         float valueAsFloat = valueAsDouble.floatValue();
/*      */         
/* 2616 */         if (this.jdbcCompliantTruncationForReads)
/*      */         {
/* 2618 */           if ((this.jdbcCompliantTruncationForReads && valueAsFloat == Float.NEGATIVE_INFINITY) || valueAsFloat == Float.POSITIVE_INFINITY)
/*      */           {
/*      */             
/* 2621 */             throwRangeException(valueAsDouble.toString(), columnIndex, 6);
/*      */           }
/*      */         }
/*      */ 
/*      */         
/* 2626 */         return valueAsFloat;
/* 2627 */       } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */         
/* 2631 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getFloat()_-____200") + val + Messages.getString("ResultSet.___in_column__201") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public int getInt(int columnIndex) throws SQLException {
/* 2652 */     checkRowPos();
/*      */     
/* 2654 */     if (!this.isBinaryEncoded) {
/* 2655 */       int columnIndexMinusOne = columnIndex - 1;
/* 2656 */       if (this.useFastIntParsing) {
/* 2657 */         checkColumnBounds(columnIndex);
/*      */         
/* 2659 */         if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2660 */           this.wasNullFlag = true;
/*      */         } else {
/* 2662 */           this.wasNullFlag = false;
/*      */         } 
/*      */         
/* 2665 */         if (this.wasNullFlag) {
/* 2666 */           return 0;
/*      */         }
/*      */         
/* 2669 */         if (this.thisRow.length(columnIndexMinusOne) == 0L) {
/* 2670 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2673 */         boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
/*      */ 
/*      */         
/* 2676 */         if (!needsFullParse) {
/*      */           try {
/* 2678 */             return getIntWithOverflowCheck(columnIndexMinusOne);
/* 2679 */           } catch (NumberFormatException nfe) {
/*      */             
/*      */             try {
/* 2682 */               return parseIntAsDouble(columnIndex, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection));
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 2687 */             catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */               
/* 2691 */               if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2692 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 2694 */                 if (this.connection.getJdbcCompliantTruncationForReads() && (valueAsLong < -2147483648L || valueAsLong > 2147483647L))
/*      */                 {
/*      */                   
/* 2697 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/* 2702 */                 return (int)valueAsLong;
/*      */               } 
/*      */               
/* 2705 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection) + "'", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2721 */       String val = null;
/*      */       
/*      */       try {
/* 2724 */         val = getString(columnIndex);
/*      */         
/* 2726 */         if (val != null) {
/* 2727 */           if (val.length() == 0) {
/* 2728 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 2731 */           if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
/*      */             
/* 2733 */             int i = Integer.parseInt(val);
/*      */             
/* 2735 */             checkForIntegerTruncation(columnIndexMinusOne, null, i);
/*      */             
/* 2737 */             return i;
/*      */           } 
/*      */ 
/*      */           
/* 2741 */           int intVal = parseIntAsDouble(columnIndex, val);
/*      */           
/* 2743 */           checkForIntegerTruncation(columnIndex, null, intVal);
/*      */           
/* 2745 */           return intVal;
/*      */         } 
/*      */         
/* 2748 */         return 0;
/* 2749 */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 2751 */           return parseIntAsDouble(columnIndex, val);
/* 2752 */         } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */           
/* 2756 */           if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2757 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 2759 */             if (this.jdbcCompliantTruncationForReads && (valueAsLong < -2147483648L || valueAsLong > 2147483647L))
/*      */             {
/* 2761 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */ 
/*      */             
/* 2765 */             return (int)valueAsLong;
/*      */           } 
/*      */           
/* 2768 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + val + "'", "S1009", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2778 */     return getNativeInt(columnIndex);
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
/*      */   public int getInt(String columnName) throws SQLException {
/* 2793 */     return getInt(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final int getIntFromString(String val, int columnIndex) throws SQLException {
/*      */     try {
/* 2799 */       if (val != null) {
/*      */         
/* 2801 */         if (val.length() == 0) {
/* 2802 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2805 */         if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2815 */           val = val.trim();
/*      */           
/* 2817 */           int valueAsInt = Integer.parseInt(val);
/*      */           
/* 2819 */           if (this.jdbcCompliantTruncationForReads && (
/* 2820 */             valueAsInt == Integer.MIN_VALUE || valueAsInt == Integer.MAX_VALUE)) {
/*      */             
/* 2822 */             long valueAsLong = Long.parseLong(val);
/*      */             
/* 2824 */             if (valueAsLong < -2147483648L || valueAsLong > 2147483647L)
/*      */             {
/* 2826 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2833 */           return valueAsInt;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2838 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2840 */         if (this.jdbcCompliantTruncationForReads && (
/* 2841 */           valueAsDouble < -2.147483648E9D || valueAsDouble > 2.147483647E9D))
/*      */         {
/* 2843 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2848 */         return (int)valueAsDouble;
/*      */       } 
/*      */       
/* 2851 */       return 0;
/* 2852 */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2854 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2856 */         if (this.jdbcCompliantTruncationForReads && (
/* 2857 */           valueAsDouble < -2.147483648E9D || valueAsDouble > 2.147483647E9D))
/*      */         {
/* 2859 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2864 */         return (int)valueAsDouble;
/* 2865 */       } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */         
/* 2869 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____206") + val + Messages.getString("ResultSet.___in_column__207") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public long getLong(int columnIndex) throws SQLException {
/* 2889 */     return getLong(columnIndex, true);
/*      */   }
/*      */   
/*      */   private long getLong(int columnIndex, boolean overflowCheck) throws SQLException {
/* 2893 */     if (!this.isBinaryEncoded) {
/* 2894 */       checkRowPos();
/*      */       
/* 2896 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 2898 */       if (this.useFastIntParsing) {
/*      */         
/* 2900 */         checkColumnBounds(columnIndex);
/*      */         
/* 2902 */         if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2903 */           this.wasNullFlag = true;
/*      */         } else {
/* 2905 */           this.wasNullFlag = false;
/*      */         } 
/*      */         
/* 2908 */         if (this.wasNullFlag) {
/* 2909 */           return 0L;
/*      */         }
/*      */         
/* 2912 */         if (this.thisRow.length(columnIndexMinusOne) == 0L) {
/* 2913 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2916 */         boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
/*      */         
/* 2918 */         if (!needsFullParse) {
/*      */           try {
/* 2920 */             return getLongWithOverflowCheck(columnIndexMinusOne, overflowCheck);
/* 2921 */           } catch (NumberFormatException nfe) {
/*      */             
/*      */             try {
/* 2924 */               return parseLongAsDouble(columnIndexMinusOne, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection));
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 2929 */             catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */               
/* 2933 */               if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2934 */                 return getNumericRepresentationOfSQLBitType(columnIndex);
/*      */               }
/*      */               
/* 2937 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection) + "'", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2951 */       String val = null;
/*      */       
/*      */       try {
/* 2954 */         val = getString(columnIndex);
/*      */         
/* 2956 */         if (val != null) {
/* 2957 */           if (val.length() == 0) {
/* 2958 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 2961 */           if (val.indexOf("e") == -1 && val.indexOf("E") == -1) {
/* 2962 */             return parseLongWithOverflowCheck(columnIndexMinusOne, null, val, overflowCheck);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 2967 */           return parseLongAsDouble(columnIndexMinusOne, val);
/*      */         } 
/*      */         
/* 2970 */         return 0L;
/* 2971 */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 2973 */           return parseLongAsDouble(columnIndexMinusOne, val);
/* 2974 */         } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */           
/* 2978 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + val + "'", "S1009", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2986 */     return getNativeLong(columnIndex, overflowCheck, true);
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
/*      */   public long getLong(String columnName) throws SQLException {
/* 3001 */     return getLong(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final long getLongFromString(String val, int columnIndexZeroBased) throws SQLException {
/*      */     try {
/* 3007 */       if (val != null) {
/*      */         
/* 3009 */         if (val.length() == 0) {
/* 3010 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 3013 */         if (val.indexOf("e") == -1 && val.indexOf("E") == -1) {
/* 3014 */           return parseLongWithOverflowCheck(columnIndexZeroBased, null, val, true);
/*      */         }
/*      */ 
/*      */         
/* 3018 */         return parseLongAsDouble(columnIndexZeroBased, val);
/*      */       } 
/*      */       
/* 3021 */       return 0L;
/* 3022 */     } catch (NumberFormatException nfe) {
/*      */       
/*      */       try {
/* 3025 */         return parseLongAsDouble(columnIndexZeroBased, val);
/* 3026 */       } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */         
/* 3030 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____211") + val + Messages.getString("ResultSet.___in_column__212") + (columnIndexZeroBased + 1), "S1009", getExceptionInterceptor());
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
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/* 3049 */     checkClosed();
/*      */     
/* 3051 */     return new ResultSetMetaData(this.fields, this.connection.getUseOldAliasMetadataBehavior(), getExceptionInterceptor());
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
/*      */   protected Array getNativeArray(int i) throws SQLException {
/* 3069 */     throw SQLError.notImplemented();
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
/*      */   protected InputStream getNativeAsciiStream(int columnIndex) throws SQLException {
/* 3099 */     checkRowPos();
/*      */     
/* 3101 */     return getNativeBinaryStream(columnIndex);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex) throws SQLException {
/* 3120 */     checkColumnBounds(columnIndex);
/*      */     
/* 3122 */     int scale = this.fields[columnIndex - 1].getDecimals();
/*      */     
/* 3124 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex, int scale) throws SQLException {
/* 3143 */     checkColumnBounds(columnIndex);
/*      */     
/* 3145 */     String stringVal = null;
/*      */     
/* 3147 */     Field f = this.fields[columnIndex - 1];
/*      */     
/* 3149 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3151 */     if (value == null) {
/* 3152 */       this.wasNullFlag = true;
/*      */       
/* 3154 */       return null;
/*      */     } 
/*      */     
/* 3157 */     this.wasNullFlag = false;
/*      */     
/* 3159 */     switch (f.getSQLType())
/*      */     { case 2:
/*      */       case 3:
/* 3162 */         stringVal = StringUtils.toAsciiString((byte[])value);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3169 */         return getBigDecimalFromString(stringVal, columnIndex, scale); }  stringVal = getNativeString(columnIndex); return getBigDecimalFromString(stringVal, columnIndex, scale);
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
/*      */   protected InputStream getNativeBinaryStream(int columnIndex) throws SQLException {
/* 3191 */     checkRowPos();
/*      */     
/* 3193 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3195 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 3196 */       this.wasNullFlag = true;
/*      */       
/* 3198 */       return null;
/*      */     } 
/*      */     
/* 3201 */     this.wasNullFlag = false;
/*      */     
/* 3203 */     switch (this.fields[columnIndexMinusOne].getSQLType()) {
/*      */       case -7:
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/*      */       case 2004:
/* 3209 */         return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
/*      */     } 
/*      */     
/* 3212 */     byte[] b = getNativeBytes(columnIndex, false);
/*      */     
/* 3214 */     if (b != null) {
/* 3215 */       return new ByteArrayInputStream(b);
/*      */     }
/*      */     
/* 3218 */     return null;
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
/*      */   protected Blob getNativeBlob(int columnIndex) throws SQLException {
/* 3233 */     checkRowPos();
/*      */     
/* 3235 */     checkColumnBounds(columnIndex);
/*      */     
/* 3237 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3239 */     if (value == null) {
/* 3240 */       this.wasNullFlag = true;
/*      */     } else {
/* 3242 */       this.wasNullFlag = false;
/*      */     } 
/*      */     
/* 3245 */     if (this.wasNullFlag) {
/* 3246 */       return null;
/*      */     }
/*      */     
/* 3249 */     int mysqlType = this.fields[columnIndex - 1].getMysqlType();
/*      */     
/* 3251 */     byte[] dataAsBytes = null;
/*      */     
/* 3253 */     switch (mysqlType) {
/*      */       case 249:
/*      */       case 250:
/*      */       case 251:
/*      */       case 252:
/* 3258 */         dataAsBytes = (byte[])value;
/*      */         break;
/*      */       
/*      */       default:
/* 3262 */         dataAsBytes = getNativeBytes(columnIndex, false);
/*      */         break;
/*      */     } 
/* 3265 */     if (!this.connection.getEmulateLocators()) {
/* 3266 */       return new Blob(dataAsBytes, getExceptionInterceptor());
/*      */     }
/*      */     
/* 3269 */     return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public static boolean arraysEqual(byte[] left, byte[] right) {
/* 3273 */     if (left == null) {
/* 3274 */       return (right == null);
/*      */     }
/* 3276 */     if (right == null) {
/* 3277 */       return false;
/*      */     }
/* 3279 */     if (left.length != right.length) {
/* 3280 */       return false;
/*      */     }
/* 3282 */     for (int i = 0; i < left.length; i++) {
/* 3283 */       if (left[i] != right[i]) {
/* 3284 */         return false;
/*      */       }
/*      */     } 
/* 3287 */     return true;
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
/*      */   protected byte getNativeByte(int columnIndex) throws SQLException {
/* 3302 */     return getNativeByte(columnIndex, true); } protected byte getNativeByte(int columnIndex, boolean overflowCheck) throws SQLException { long valueAsLong; byte valueAsByte; short valueAsShort;
/*      */     int valueAsInt;
/*      */     float valueAsFloat;
/*      */     double valueAsDouble;
/* 3306 */     checkRowPos();
/*      */     
/* 3308 */     checkColumnBounds(columnIndex);
/*      */     
/* 3310 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3312 */     if (value == null) {
/* 3313 */       this.wasNullFlag = true;
/*      */       
/* 3315 */       return 0;
/*      */     } 
/*      */     
/* 3318 */     if (value == null) {
/* 3319 */       this.wasNullFlag = true;
/*      */     } else {
/* 3321 */       this.wasNullFlag = false;
/*      */     } 
/*      */     
/* 3324 */     if (this.wasNullFlag) {
/* 3325 */       return 0;
/*      */     }
/*      */     
/* 3328 */     columnIndex--;
/*      */     
/* 3330 */     Field field = this.fields[columnIndex];
/*      */     
/* 3332 */     switch (field.getMysqlType()) {
/*      */       case 16:
/* 3334 */         valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */         
/* 3336 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong < -128L || valueAsLong > 127L))
/*      */         {
/*      */           
/* 3339 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */         
/* 3343 */         return (byte)(int)valueAsLong;
/*      */       case 1:
/* 3345 */         valueAsByte = ((byte[])value)[0];
/*      */         
/* 3347 */         if (!field.isUnsigned()) {
/* 3348 */           return valueAsByte;
/*      */         }
/*      */         
/* 3351 */         valueAsShort = (valueAsByte >= 0) ? (short)valueAsByte : (short)(valueAsByte + 256);
/*      */ 
/*      */         
/* 3354 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && 
/* 3355 */           valueAsShort > 127) {
/* 3356 */           throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3361 */         return (byte)valueAsShort;
/*      */       
/*      */       case 2:
/*      */       case 13:
/* 3365 */         valueAsShort = getNativeShort(columnIndex + 1);
/*      */         
/* 3367 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 3368 */           valueAsShort < -128 || valueAsShort > 127))
/*      */         {
/* 3370 */           throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3375 */         return (byte)valueAsShort;
/*      */       case 3:
/*      */       case 9:
/* 3378 */         valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */         
/* 3380 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 3381 */           valueAsInt < -128 || valueAsInt > 127)) {
/* 3382 */           throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3387 */         return (byte)valueAsInt;
/*      */       
/*      */       case 4:
/* 3390 */         valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */         
/* 3392 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 3393 */           valueAsFloat < -128.0F || valueAsFloat > 127.0F))
/*      */         {
/*      */           
/* 3396 */           throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3401 */         return (byte)(int)valueAsFloat;
/*      */       
/*      */       case 5:
/* 3404 */         valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */         
/* 3406 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 3407 */           valueAsDouble < -128.0D || valueAsDouble > 127.0D))
/*      */         {
/* 3409 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3414 */         return (byte)(int)valueAsDouble;
/*      */       
/*      */       case 8:
/* 3417 */         valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */         
/* 3419 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 3420 */           valueAsLong < -128L || valueAsLong > 127L))
/*      */         {
/* 3422 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3427 */         return (byte)(int)valueAsLong;
/*      */     } 
/*      */     
/* 3430 */     if (this.useUsageAdvisor) {
/* 3431 */       issueConversionViaParsingWarning("getByte()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3441 */     return getByteFromString(getNativeString(columnIndex + 1), columnIndex + 1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected byte[] getNativeBytes(int columnIndex, boolean noConversion) throws SQLException {
/* 3463 */     checkRowPos();
/*      */     
/* 3465 */     checkColumnBounds(columnIndex);
/*      */     
/* 3467 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3469 */     if (value == null) {
/* 3470 */       this.wasNullFlag = true;
/*      */     } else {
/* 3472 */       this.wasNullFlag = false;
/*      */     } 
/*      */     
/* 3475 */     if (this.wasNullFlag) {
/* 3476 */       return null;
/*      */     }
/*      */     
/* 3479 */     Field field = this.fields[columnIndex - 1];
/*      */     
/* 3481 */     int mysqlType = field.getMysqlType();
/*      */ 
/*      */ 
/*      */     
/* 3485 */     if (noConversion) {
/* 3486 */       mysqlType = 252;
/*      */     }
/*      */     
/* 3489 */     switch (mysqlType) {
/*      */       case 16:
/*      */       case 249:
/*      */       case 250:
/*      */       case 251:
/*      */       case 252:
/* 3495 */         return (byte[])value;
/*      */       
/*      */       case 15:
/*      */       case 253:
/*      */       case 254:
/* 3500 */         if (value instanceof byte[]) {
/* 3501 */           return (byte[])value;
/*      */         }
/*      */         break;
/*      */     } 
/* 3505 */     int sqlType = field.getSQLType();
/*      */     
/* 3507 */     if (sqlType == -3 || sqlType == -2) {
/* 3508 */       return (byte[])value;
/*      */     }
/*      */     
/* 3511 */     return getBytesFromString(getNativeString(columnIndex), columnIndex);
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
/*      */   protected Reader getNativeCharacterStream(int columnIndex) throws SQLException {
/* 3532 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3534 */     switch (this.fields[columnIndexMinusOne].getSQLType()) {
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/*      */       case 2005:
/* 3539 */         if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 3540 */           this.wasNullFlag = true;
/*      */           
/* 3542 */           return null;
/*      */         } 
/*      */         
/* 3545 */         this.wasNullFlag = false;
/*      */         
/* 3547 */         return this.thisRow.getReader(columnIndexMinusOne);
/*      */     } 
/*      */     
/* 3550 */     String asString = null;
/*      */     
/* 3552 */     asString = getStringForClob(columnIndex);
/*      */     
/* 3554 */     if (asString == null) {
/* 3555 */       return null;
/*      */     }
/*      */     
/* 3558 */     return getCharacterStreamFromString(asString, columnIndex);
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
/*      */   protected Clob getNativeClob(int columnIndex) throws SQLException {
/* 3573 */     String stringVal = getStringForClob(columnIndex);
/*      */     
/* 3575 */     if (stringVal == null) {
/* 3576 */       return null;
/*      */     }
/*      */     
/* 3579 */     return getClobFromString(stringVal, columnIndex); } private String getNativeConvertToString(int columnIndex, Field field) throws SQLException { boolean booleanVal; byte tinyintVal; short unsignedTinyVal; int intVal; long longVal; float floatVal; double doubleVal;
/*      */     String stringVal;
/*      */     byte[] data;
/*      */     Date dt;
/*      */     Object obj;
/*      */     Time tm;
/*      */     Timestamp tstamp;
/*      */     String result;
/* 3587 */     int sqlType = field.getSQLType();
/* 3588 */     int mysqlType = field.getMysqlType();
/*      */     
/* 3590 */     switch (sqlType) {
/*      */       case -7:
/* 3592 */         return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
/*      */       case 16:
/* 3594 */         booleanVal = getBoolean(columnIndex);
/*      */         
/* 3596 */         if (this.wasNullFlag) {
/* 3597 */           return null;
/*      */         }
/*      */         
/* 3600 */         return String.valueOf(booleanVal);
/*      */       
/*      */       case -6:
/* 3603 */         tinyintVal = getNativeByte(columnIndex, false);
/*      */         
/* 3605 */         if (this.wasNullFlag) {
/* 3606 */           return null;
/*      */         }
/*      */         
/* 3609 */         if (!field.isUnsigned() || tinyintVal >= 0) {
/* 3610 */           return String.valueOf(tinyintVal);
/*      */         }
/*      */         
/* 3613 */         unsignedTinyVal = (short)(tinyintVal & 0xFF);
/*      */         
/* 3615 */         return String.valueOf(unsignedTinyVal);
/*      */ 
/*      */       
/*      */       case 5:
/* 3619 */         intVal = getNativeInt(columnIndex, false);
/*      */         
/* 3621 */         if (this.wasNullFlag) {
/* 3622 */           return null;
/*      */         }
/*      */         
/* 3625 */         if (!field.isUnsigned() || intVal >= 0) {
/* 3626 */           return String.valueOf(intVal);
/*      */         }
/*      */         
/* 3629 */         intVal &= 0xFFFF;
/*      */         
/* 3631 */         return String.valueOf(intVal);
/*      */       
/*      */       case 4:
/* 3634 */         intVal = getNativeInt(columnIndex, false);
/*      */         
/* 3636 */         if (this.wasNullFlag) {
/* 3637 */           return null;
/*      */         }
/*      */         
/* 3640 */         if (!field.isUnsigned() || intVal >= 0 || field.getMysqlType() == 9)
/*      */         {
/*      */           
/* 3643 */           return String.valueOf(intVal);
/*      */         }
/*      */         
/* 3646 */         longVal = intVal & 0xFFFFFFFFL;
/*      */         
/* 3648 */         return String.valueOf(longVal);
/*      */ 
/*      */       
/*      */       case -5:
/* 3652 */         if (!field.isUnsigned()) {
/* 3653 */           longVal = getNativeLong(columnIndex, false, true);
/*      */           
/* 3655 */           if (this.wasNullFlag) {
/* 3656 */             return null;
/*      */           }
/*      */           
/* 3659 */           return String.valueOf(longVal);
/*      */         } 
/*      */         
/* 3662 */         longVal = getNativeLong(columnIndex, false, false);
/*      */         
/* 3664 */         if (this.wasNullFlag) {
/* 3665 */           return null;
/*      */         }
/*      */         
/* 3668 */         return String.valueOf(convertLongToUlong(longVal));
/*      */       case 7:
/* 3670 */         floatVal = getNativeFloat(columnIndex);
/*      */         
/* 3672 */         if (this.wasNullFlag) {
/* 3673 */           return null;
/*      */         }
/*      */         
/* 3676 */         return String.valueOf(floatVal);
/*      */       
/*      */       case 6:
/*      */       case 8:
/* 3680 */         doubleVal = getNativeDouble(columnIndex);
/*      */         
/* 3682 */         if (this.wasNullFlag) {
/* 3683 */           return null;
/*      */         }
/*      */         
/* 3686 */         return String.valueOf(doubleVal);
/*      */       
/*      */       case 2:
/*      */       case 3:
/* 3690 */         stringVal = StringUtils.toAsciiString(this.thisRow.getColumnValue(columnIndex - 1));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3695 */         if (stringVal != null) {
/* 3696 */           BigDecimal val; this.wasNullFlag = false;
/*      */           
/* 3698 */           if (stringVal.length() == 0) {
/* 3699 */             val = new BigDecimal(0.0D);
/*      */             
/* 3701 */             return val.toString();
/*      */           } 
/*      */           
/*      */           try {
/* 3705 */             val = new BigDecimal(stringVal);
/* 3706 */           } catch (NumberFormatException ex) {
/* 3707 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Constants.integerValueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3714 */           return val.toString();
/*      */         } 
/*      */         
/* 3717 */         this.wasNullFlag = true;
/*      */         
/* 3719 */         return null;
/*      */ 
/*      */       
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/* 3725 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */       
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/* 3730 */         if (!field.isBlob())
/* 3731 */           return extractStringFromNativeColumn(columnIndex, mysqlType); 
/* 3732 */         if (!field.isBinary()) {
/* 3733 */           return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */         }
/* 3735 */         data = getBytes(columnIndex);
/* 3736 */         obj = data;
/*      */         
/* 3738 */         if (data != null && data.length >= 2) {
/* 3739 */           if (data[0] == -84 && data[1] == -19) {
/*      */             
/*      */             try {
/* 3742 */               ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */               
/* 3744 */               ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */               
/* 3746 */               obj = objIn.readObject();
/* 3747 */               objIn.close();
/* 3748 */               bytesIn.close();
/* 3749 */             } catch (ClassNotFoundException cnfe) {
/* 3750 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 3756 */             catch (IOException ex) {
/* 3757 */               obj = data;
/*      */             } 
/*      */           }
/*      */           
/* 3761 */           return obj.toString();
/*      */         } 
/*      */         
/* 3764 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 91:
/* 3770 */         if (mysqlType == 13) {
/* 3771 */           short shortVal = getNativeShort(columnIndex);
/*      */           
/* 3773 */           if (!this.connection.getYearIsDateType()) {
/*      */             
/* 3775 */             if (this.wasNullFlag) {
/* 3776 */               return null;
/*      */             }
/*      */             
/* 3779 */             return String.valueOf(shortVal);
/*      */           } 
/*      */           
/* 3782 */           if (field.getLength() == 2L) {
/*      */             
/* 3784 */             if (shortVal <= 69) {
/* 3785 */               shortVal = (short)(shortVal + 100);
/*      */             }
/*      */             
/* 3788 */             shortVal = (short)(shortVal + 1900);
/*      */           } 
/*      */           
/* 3791 */           return fastDateCreate(null, shortVal, 1, 1).toString();
/*      */         } 
/*      */ 
/*      */         
/* 3795 */         dt = getNativeDate(columnIndex);
/*      */         
/* 3797 */         if (dt == null) {
/* 3798 */           return null;
/*      */         }
/*      */         
/* 3801 */         return String.valueOf(dt);
/*      */       
/*      */       case 92:
/* 3804 */         tm = getNativeTime(columnIndex, null, this.defaultTimeZone, false);
/*      */         
/* 3806 */         if (tm == null) {
/* 3807 */           return null;
/*      */         }
/*      */         
/* 3810 */         return String.valueOf(tm);
/*      */       
/*      */       case 93:
/* 3813 */         tstamp = getNativeTimestamp(columnIndex, null, this.defaultTimeZone, false);
/*      */ 
/*      */         
/* 3816 */         if (tstamp == null) {
/* 3817 */           return null;
/*      */         }
/*      */         
/* 3820 */         result = String.valueOf(tstamp);
/*      */         
/* 3822 */         if (!this.connection.getNoDatetimeStringSync()) {
/* 3823 */           return result;
/*      */         }
/*      */         
/* 3826 */         if (result.endsWith(".0")) {
/* 3827 */           return result.substring(0, result.length() - 2);
/*      */         }
/*      */         break;
/*      */     } 
/* 3831 */     return extractStringFromNativeColumn(columnIndex, mysqlType); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date getNativeDate(int columnIndex) throws SQLException {
/* 3847 */     return getNativeDate(columnIndex, null);
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
/*      */   protected Date getNativeDate(int columnIndex, Calendar cal) throws SQLException {
/* 3868 */     checkRowPos();
/* 3869 */     checkColumnBounds(columnIndex);
/*      */     
/* 3871 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3873 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 3875 */     Date dateToReturn = null;
/*      */     
/* 3877 */     if (mysqlType == 10) {
/*      */       
/* 3879 */       dateToReturn = this.thisRow.getNativeDate(columnIndexMinusOne, this.connection, this, cal);
/*      */     } else {
/*      */       
/* 3882 */       TimeZone tz = (cal != null) ? cal.getTimeZone() : getDefaultTimeZone();
/*      */ 
/*      */       
/* 3885 */       boolean rollForward = (tz != null && !tz.equals(getDefaultTimeZone()));
/*      */       
/* 3887 */       dateToReturn = (Date)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 91, mysqlType, tz, rollForward, this.connection, this);
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
/* 3899 */     if (dateToReturn == null) {
/*      */       
/* 3901 */       this.wasNullFlag = true;
/*      */       
/* 3903 */       return null;
/*      */     } 
/*      */     
/* 3906 */     this.wasNullFlag = false;
/*      */     
/* 3908 */     return dateToReturn;
/*      */   }
/*      */   
/*      */   Date getNativeDateViaParseConversion(int columnIndex) throws SQLException {
/* 3912 */     if (this.useUsageAdvisor) {
/* 3913 */       issueConversionViaParsingWarning("getDate()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[] { 10 });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3918 */     String stringVal = getNativeString(columnIndex);
/*      */     
/* 3920 */     return getDateFromString(stringVal, columnIndex, null);
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
/*      */   protected double getNativeDouble(int columnIndex) throws SQLException {
/*      */     long valueAsLong;
/*      */     BigInteger asBigInt;
/* 3935 */     checkRowPos();
/* 3936 */     checkColumnBounds(columnIndex);
/*      */     
/* 3938 */     columnIndex--;
/*      */     
/* 3940 */     if (this.thisRow.isNull(columnIndex)) {
/* 3941 */       this.wasNullFlag = true;
/*      */       
/* 3943 */       return 0.0D;
/*      */     } 
/*      */     
/* 3946 */     this.wasNullFlag = false;
/*      */     
/* 3948 */     Field f = this.fields[columnIndex];
/*      */     
/* 3950 */     switch (f.getMysqlType()) {
/*      */       case 5:
/* 3952 */         return this.thisRow.getNativeDouble(columnIndex);
/*      */       case 1:
/* 3954 */         if (!f.isUnsigned()) {
/* 3955 */           return getNativeByte(columnIndex + 1);
/*      */         }
/*      */         
/* 3958 */         return getNativeShort(columnIndex + 1);
/*      */       case 2:
/*      */       case 13:
/* 3961 */         if (!f.isUnsigned()) {
/* 3962 */           return getNativeShort(columnIndex + 1);
/*      */         }
/*      */         
/* 3965 */         return getNativeInt(columnIndex + 1);
/*      */       case 3:
/*      */       case 9:
/* 3968 */         if (!f.isUnsigned()) {
/* 3969 */           return getNativeInt(columnIndex + 1);
/*      */         }
/*      */         
/* 3972 */         return getNativeLong(columnIndex + 1);
/*      */       case 8:
/* 3974 */         valueAsLong = getNativeLong(columnIndex + 1);
/*      */         
/* 3976 */         if (!f.isUnsigned()) {
/* 3977 */           return valueAsLong;
/*      */         }
/*      */         
/* 3980 */         asBigInt = convertLongToUlong(valueAsLong);
/*      */ 
/*      */ 
/*      */         
/* 3984 */         return asBigInt.doubleValue();
/*      */       case 4:
/* 3986 */         return getNativeFloat(columnIndex + 1);
/*      */       case 16:
/* 3988 */         return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */     } 
/* 3990 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 3992 */     if (this.useUsageAdvisor) {
/* 3993 */       issueConversionViaParsingWarning("getDouble()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4003 */     return getDoubleFromString(stringVal, columnIndex + 1);
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
/*      */   protected float getNativeFloat(int columnIndex) throws SQLException {
/*      */     long valueAsLong;
/*      */     Double valueAsDouble;
/*      */     float valueAsFloat;
/*      */     BigInteger asBigInt;
/* 4019 */     checkRowPos();
/* 4020 */     checkColumnBounds(columnIndex);
/*      */     
/* 4022 */     columnIndex--;
/*      */     
/* 4024 */     if (this.thisRow.isNull(columnIndex)) {
/* 4025 */       this.wasNullFlag = true;
/*      */       
/* 4027 */       return 0.0F;
/*      */     } 
/*      */     
/* 4030 */     this.wasNullFlag = false;
/*      */     
/* 4032 */     Field f = this.fields[columnIndex];
/*      */     
/* 4034 */     switch (f.getMysqlType()) {
/*      */       case 16:
/* 4036 */         valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */         
/* 4038 */         return (float)valueAsLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/* 4045 */         valueAsDouble = new Double(getNativeDouble(columnIndex + 1));
/*      */         
/* 4047 */         valueAsFloat = valueAsDouble.floatValue();
/*      */         
/* 4049 */         if ((this.jdbcCompliantTruncationForReads && valueAsFloat == Float.NEGATIVE_INFINITY) || valueAsFloat == Float.POSITIVE_INFINITY)
/*      */         {
/*      */           
/* 4052 */           throwRangeException(valueAsDouble.toString(), columnIndex + 1, 6);
/*      */         }
/*      */ 
/*      */         
/* 4056 */         return (float)getNativeDouble(columnIndex + 1);
/*      */       case 1:
/* 4058 */         if (!f.isUnsigned()) {
/* 4059 */           return getNativeByte(columnIndex + 1);
/*      */         }
/*      */         
/* 4062 */         return getNativeShort(columnIndex + 1);
/*      */       case 2:
/*      */       case 13:
/* 4065 */         if (!f.isUnsigned()) {
/* 4066 */           return getNativeShort(columnIndex + 1);
/*      */         }
/*      */         
/* 4069 */         return getNativeInt(columnIndex + 1);
/*      */       case 3:
/*      */       case 9:
/* 4072 */         if (!f.isUnsigned()) {
/* 4073 */           return getNativeInt(columnIndex + 1);
/*      */         }
/*      */         
/* 4076 */         return (float)getNativeLong(columnIndex + 1);
/*      */       case 8:
/* 4078 */         valueAsLong = getNativeLong(columnIndex + 1);
/*      */         
/* 4080 */         if (!f.isUnsigned()) {
/* 4081 */           return (float)valueAsLong;
/*      */         }
/*      */         
/* 4084 */         asBigInt = convertLongToUlong(valueAsLong);
/*      */ 
/*      */ 
/*      */         
/* 4088 */         return asBigInt.floatValue();
/*      */       
/*      */       case 4:
/* 4091 */         return this.thisRow.getNativeFloat(columnIndex);
/*      */     } 
/*      */     
/* 4094 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4096 */     if (this.useUsageAdvisor) {
/* 4097 */       issueConversionViaParsingWarning("getFloat()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4107 */     return getFloatFromString(stringVal, columnIndex + 1);
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
/*      */   protected int getNativeInt(int columnIndex) throws SQLException {
/* 4123 */     return getNativeInt(columnIndex, true); } protected int getNativeInt(int columnIndex, boolean overflowCheck) throws SQLException { long valueAsLong; byte tinyintVal;
/*      */     short asShort;
/*      */     int valueAsInt;
/*      */     double valueAsDouble;
/* 4127 */     checkRowPos();
/* 4128 */     checkColumnBounds(columnIndex);
/*      */     
/* 4130 */     columnIndex--;
/*      */     
/* 4132 */     if (this.thisRow.isNull(columnIndex)) {
/* 4133 */       this.wasNullFlag = true;
/*      */       
/* 4135 */       return 0;
/*      */     } 
/*      */     
/* 4138 */     this.wasNullFlag = false;
/*      */     
/* 4140 */     Field f = this.fields[columnIndex];
/*      */     
/* 4142 */     switch (f.getMysqlType()) {
/*      */       case 16:
/* 4144 */         valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */         
/* 4146 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong < -2147483648L || valueAsLong > 2147483647L))
/*      */         {
/*      */           
/* 4149 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */         }
/*      */ 
/*      */         
/* 4153 */         return (short)(int)valueAsLong;
/*      */       case 1:
/* 4155 */         tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */         
/* 4157 */         if (!f.isUnsigned() || tinyintVal >= 0) {
/* 4158 */           return tinyintVal;
/*      */         }
/*      */         
/* 4161 */         return tinyintVal + 256;
/*      */       case 2:
/*      */       case 13:
/* 4164 */         asShort = getNativeShort(columnIndex + 1, false);
/*      */         
/* 4166 */         if (!f.isUnsigned() || asShort >= 0) {
/* 4167 */           return asShort;
/*      */         }
/*      */         
/* 4170 */         return asShort + 65536;
/*      */       
/*      */       case 3:
/*      */       case 9:
/* 4174 */         valueAsInt = this.thisRow.getNativeInt(columnIndex);
/*      */         
/* 4176 */         if (!f.isUnsigned()) {
/* 4177 */           return valueAsInt;
/*      */         }
/*      */         
/* 4180 */         valueAsLong = (valueAsInt >= 0) ? valueAsInt : (valueAsInt + 4294967296L);
/*      */ 
/*      */         
/* 4183 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsLong > 2147483647L)
/*      */         {
/* 4185 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */         }
/*      */ 
/*      */         
/* 4189 */         return (int)valueAsLong;
/*      */       case 8:
/* 4191 */         valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */         
/* 4193 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4194 */           valueAsLong < -2147483648L || valueAsLong > 2147483647L))
/*      */         {
/* 4196 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4201 */         return (int)valueAsLong;
/*      */       case 5:
/* 4203 */         valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */         
/* 4205 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4206 */           valueAsDouble < -2.147483648E9D || valueAsDouble > 2.147483647E9D))
/*      */         {
/* 4208 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4213 */         return (int)valueAsDouble;
/*      */       case 4:
/* 4215 */         valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */         
/* 4217 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4218 */           valueAsDouble < -2.147483648E9D || valueAsDouble > 2.147483647E9D))
/*      */         {
/* 4220 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4225 */         return (int)valueAsDouble;
/*      */     } 
/*      */     
/* 4228 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4230 */     if (this.useUsageAdvisor) {
/* 4231 */       issueConversionViaParsingWarning("getInt()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4241 */     return getIntFromString(stringVal, columnIndex + 1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long getNativeLong(int columnIndex) throws SQLException {
/* 4257 */     return getNativeLong(columnIndex, true, true); } protected long getNativeLong(int columnIndex, boolean overflowCheck, boolean expandUnsignedLong) throws SQLException {
/*      */     int asInt;
/*      */     long valueAsLong;
/*      */     BigInteger asBigInt;
/*      */     double valueAsDouble;
/* 4262 */     checkRowPos();
/* 4263 */     checkColumnBounds(columnIndex);
/*      */     
/* 4265 */     columnIndex--;
/*      */     
/* 4267 */     if (this.thisRow.isNull(columnIndex)) {
/* 4268 */       this.wasNullFlag = true;
/*      */       
/* 4270 */       return 0L;
/*      */     } 
/*      */     
/* 4273 */     this.wasNullFlag = false;
/*      */     
/* 4275 */     Field f = this.fields[columnIndex];
/*      */     
/* 4277 */     switch (f.getMysqlType()) {
/*      */       case 16:
/* 4279 */         return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       case 1:
/* 4281 */         if (!f.isUnsigned()) {
/* 4282 */           return getNativeByte(columnIndex + 1);
/*      */         }
/*      */         
/* 4285 */         return getNativeInt(columnIndex + 1);
/*      */       case 2:
/* 4287 */         if (!f.isUnsigned()) {
/* 4288 */           return getNativeShort(columnIndex + 1);
/*      */         }
/*      */         
/* 4291 */         return getNativeInt(columnIndex + 1, false);
/*      */       
/*      */       case 13:
/* 4294 */         return getNativeShort(columnIndex + 1);
/*      */       case 3:
/*      */       case 9:
/* 4297 */         asInt = getNativeInt(columnIndex + 1, false);
/*      */         
/* 4299 */         if (!f.isUnsigned() || asInt >= 0) {
/* 4300 */           return asInt;
/*      */         }
/*      */         
/* 4303 */         return asInt + 4294967296L;
/*      */       case 8:
/* 4305 */         valueAsLong = this.thisRow.getNativeLong(columnIndex);
/*      */         
/* 4307 */         if (!f.isUnsigned() || !expandUnsignedLong) {
/* 4308 */           return valueAsLong;
/*      */         }
/*      */         
/* 4311 */         asBigInt = convertLongToUlong(valueAsLong);
/*      */         
/* 4313 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (asBigInt.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0 || asBigInt.compareTo(new BigInteger(String.valueOf(Long.MIN_VALUE))) < 0))
/*      */         {
/*      */           
/* 4316 */           throwRangeException(asBigInt.toString(), columnIndex + 1, -5);
/*      */         }
/*      */ 
/*      */         
/* 4320 */         return getLongFromString(asBigInt.toString(), columnIndex);
/*      */       
/*      */       case 5:
/* 4323 */         valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */         
/* 4325 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4326 */           valueAsDouble < -9.223372036854776E18D || valueAsDouble > 9.223372036854776E18D))
/*      */         {
/* 4328 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4333 */         return (long)valueAsDouble;
/*      */       case 4:
/* 4335 */         valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */         
/* 4337 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4338 */           valueAsDouble < -9.223372036854776E18D || valueAsDouble > 9.223372036854776E18D))
/*      */         {
/* 4340 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4345 */         return (long)valueAsDouble;
/*      */     } 
/* 4347 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4349 */     if (this.useUsageAdvisor) {
/* 4350 */       issueConversionViaParsingWarning("getLong()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4360 */     return getLongFromString(stringVal, columnIndex + 1);
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
/*      */   protected Ref getNativeRef(int i) throws SQLException {
/* 4378 */     throw SQLError.notImplemented();
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
/*      */   protected short getNativeShort(int columnIndex) throws SQLException {
/* 4393 */     return getNativeShort(columnIndex, true); } protected short getNativeShort(int columnIndex, boolean overflowCheck) throws SQLException { byte tinyintVal; short asShort; int valueAsInt; long valueAsLong;
/*      */     BigInteger asBigInt;
/*      */     double valueAsDouble;
/*      */     float valueAsFloat;
/* 4397 */     checkRowPos();
/* 4398 */     checkColumnBounds(columnIndex);
/*      */     
/* 4400 */     columnIndex--;
/*      */ 
/*      */     
/* 4403 */     if (this.thisRow.isNull(columnIndex)) {
/* 4404 */       this.wasNullFlag = true;
/*      */       
/* 4406 */       return 0;
/*      */     } 
/*      */     
/* 4409 */     this.wasNullFlag = false;
/*      */     
/* 4411 */     Field f = this.fields[columnIndex];
/*      */     
/* 4413 */     switch (f.getMysqlType()) {
/*      */       
/*      */       case 1:
/* 4416 */         tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */         
/* 4418 */         if (!f.isUnsigned() || tinyintVal >= 0) {
/* 4419 */           return (short)tinyintVal;
/*      */         }
/*      */         
/* 4422 */         return (short)(tinyintVal + 256);
/*      */       
/*      */       case 2:
/*      */       case 13:
/* 4426 */         asShort = this.thisRow.getNativeShort(columnIndex);
/*      */         
/* 4428 */         if (!f.isUnsigned()) {
/* 4429 */           return asShort;
/*      */         }
/*      */         
/* 4432 */         valueAsInt = asShort & 0xFFFF;
/*      */         
/* 4434 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsInt > 32767)
/*      */         {
/* 4436 */           throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */         }
/*      */ 
/*      */         
/* 4440 */         return (short)valueAsInt;
/*      */       case 3:
/*      */       case 9:
/* 4443 */         if (!f.isUnsigned()) {
/* 4444 */           valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */           
/* 4446 */           if ((overflowCheck && this.jdbcCompliantTruncationForReads && valueAsInt > 32767) || valueAsInt < -32768)
/*      */           {
/*      */             
/* 4449 */             throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */           }
/*      */ 
/*      */           
/* 4453 */           return (short)valueAsInt;
/*      */         } 
/*      */         
/* 4456 */         valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */         
/* 4458 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsLong > 32767L)
/*      */         {
/* 4460 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */         }
/*      */ 
/*      */         
/* 4464 */         return (short)(int)valueAsLong;
/*      */       
/*      */       case 8:
/* 4467 */         valueAsLong = getNativeLong(columnIndex + 1, false, false);
/*      */         
/* 4469 */         if (!f.isUnsigned()) {
/* 4470 */           if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4471 */             valueAsLong < -32768L || valueAsLong > 32767L))
/*      */           {
/* 4473 */             throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 4478 */           return (short)(int)valueAsLong;
/*      */         } 
/*      */         
/* 4481 */         asBigInt = convertLongToUlong(valueAsLong);
/*      */         
/* 4483 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (asBigInt.compareTo(new BigInteger(String.valueOf(32767))) > 0 || asBigInt.compareTo(new BigInteger(String.valueOf(-32768))) < 0))
/*      */         {
/*      */           
/* 4486 */           throwRangeException(asBigInt.toString(), columnIndex + 1, 5);
/*      */         }
/*      */ 
/*      */         
/* 4490 */         return (short)getIntFromString(asBigInt.toString(), columnIndex + 1);
/*      */       
/*      */       case 5:
/* 4493 */         valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */         
/* 4495 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4496 */           valueAsDouble < -32768.0D || valueAsDouble > 32767.0D))
/*      */         {
/* 4498 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 5);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4503 */         return (short)(int)valueAsDouble;
/*      */       case 4:
/* 4505 */         valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */         
/* 4507 */         if (overflowCheck && this.jdbcCompliantTruncationForReads && (
/* 4508 */           valueAsFloat < -32768.0F || valueAsFloat > 32767.0F))
/*      */         {
/* 4510 */           throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, 5);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4515 */         return (short)(int)valueAsFloat;
/*      */     } 
/* 4517 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4519 */     if (this.useUsageAdvisor) {
/* 4520 */       issueConversionViaParsingWarning("getShort()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4530 */     return getShortFromString(stringVal, columnIndex + 1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getNativeString(int columnIndex) throws SQLException {
/* 4546 */     checkRowPos();
/* 4547 */     checkColumnBounds(columnIndex);
/*      */     
/* 4549 */     if (this.fields == null) {
/* 4550 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_133"), "S1002", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4556 */     if (this.thisRow.isNull(columnIndex - 1)) {
/* 4557 */       this.wasNullFlag = true;
/*      */       
/* 4559 */       return null;
/*      */     } 
/*      */     
/* 4562 */     this.wasNullFlag = false;
/*      */     
/* 4564 */     String stringVal = null;
/*      */     
/* 4566 */     Field field = this.fields[columnIndex - 1];
/*      */ 
/*      */     
/* 4569 */     stringVal = getNativeConvertToString(columnIndex, field);
/*      */     
/* 4571 */     if (field.isZeroFill() && stringVal != null) {
/* 4572 */       int origLength = stringVal.length();
/*      */       
/* 4574 */       StringBuffer zeroFillBuf = new StringBuffer(origLength);
/*      */       
/* 4576 */       long numZeros = field.getLength() - origLength;
/*      */       long i;
/* 4578 */       for (i = 0L; i < numZeros; i++) {
/* 4579 */         zeroFillBuf.append('0');
/*      */       }
/*      */       
/* 4582 */       zeroFillBuf.append(stringVal);
/*      */       
/* 4584 */       stringVal = zeroFillBuf.toString();
/*      */     } 
/*      */     
/* 4587 */     return stringVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4593 */     checkRowPos();
/* 4594 */     checkColumnBounds(columnIndex);
/*      */     
/* 4596 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4598 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 4600 */     Time timeVal = null;
/*      */     
/* 4602 */     if (mysqlType == 11) {
/* 4603 */       timeVal = this.thisRow.getNativeTime(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
/*      */     }
/*      */     else {
/*      */       
/* 4607 */       timeVal = (Time)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 92, mysqlType, tz, rollForward, this.connection, this);
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
/* 4619 */     if (timeVal == null) {
/*      */       
/* 4621 */       this.wasNullFlag = true;
/*      */       
/* 4623 */       return null;
/*      */     } 
/*      */     
/* 4626 */     this.wasNullFlag = false;
/*      */     
/* 4628 */     return timeVal;
/*      */   }
/*      */ 
/*      */   
/*      */   Time getNativeTimeViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4633 */     if (this.useUsageAdvisor) {
/* 4634 */       issueConversionViaParsingWarning("getTime()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[] { 11 });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4639 */     String strTime = getNativeString(columnIndex);
/*      */     
/* 4641 */     return getTimeFromString(strTime, targetCalendar, columnIndex, tz, rollForward);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4648 */     checkRowPos();
/* 4649 */     checkColumnBounds(columnIndex);
/*      */     
/* 4651 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4653 */     Timestamp tsVal = null;
/*      */     
/* 4655 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 4657 */     switch (mysqlType) {
/*      */       case 7:
/*      */       case 12:
/* 4660 */         tsVal = this.thisRow.getNativeTimestamp(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/* 4667 */         tsVal = (Timestamp)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 93, mysqlType, tz, rollForward, this.connection, this);
/*      */         break;
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
/* 4679 */     if (tsVal == null) {
/*      */       
/* 4681 */       this.wasNullFlag = true;
/*      */       
/* 4683 */       return null;
/*      */     } 
/*      */     
/* 4686 */     this.wasNullFlag = false;
/*      */     
/* 4688 */     return tsVal;
/*      */   }
/*      */ 
/*      */   
/*      */   Timestamp getNativeTimestampViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4693 */     if (this.useUsageAdvisor) {
/* 4694 */       issueConversionViaParsingWarning("getTimestamp()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[] { 7, 12 });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4700 */     String strTimestamp = getNativeString(columnIndex);
/*      */     
/* 4702 */     return getTimestampFromString(columnIndex, targetCalendar, strTimestamp, tz, rollForward);
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
/*      */   protected InputStream getNativeUnicodeStream(int columnIndex) throws SQLException {
/* 4729 */     checkRowPos();
/*      */     
/* 4731 */     return getBinaryStream(columnIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected URL getNativeURL(int colIndex) throws SQLException {
/* 4738 */     String val = getString(colIndex);
/*      */     
/* 4740 */     if (val == null) {
/* 4741 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 4745 */       return new URL(val);
/* 4746 */     } catch (MalformedURLException mfe) {
/* 4747 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____141") + val + "'", "S1009", getExceptionInterceptor());
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
/*      */   public ResultSetInternalMethods getNextResultSet() {
/* 4759 */     return this.nextResultSet;
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
/*      */   public Object getObject(int columnIndex) throws SQLException {
/*      */     String stringVal;
/* 4786 */     checkRowPos();
/* 4787 */     checkColumnBounds(columnIndex);
/*      */     
/* 4789 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4791 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 4792 */       this.wasNullFlag = true;
/*      */       
/* 4794 */       return null;
/*      */     } 
/*      */     
/* 4797 */     this.wasNullFlag = false;
/*      */ 
/*      */     
/* 4800 */     Field field = this.fields[columnIndexMinusOne];
/*      */     
/* 4802 */     switch (field.getSQLType()) {
/*      */       case -7:
/*      */       case 16:
/* 4805 */         if (field.getMysqlType() == 16 && !field.isSingleBit())
/*      */         {
/* 4807 */           return getBytes(columnIndex);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4813 */         return Boolean.valueOf(getBoolean(columnIndex));
/*      */       
/*      */       case -6:
/* 4816 */         if (!field.isUnsigned()) {
/* 4817 */           return Constants.integerValueOf(getByte(columnIndex));
/*      */         }
/*      */         
/* 4820 */         return Constants.integerValueOf(getInt(columnIndex));
/*      */ 
/*      */       
/*      */       case 5:
/* 4824 */         return Constants.integerValueOf(getInt(columnIndex));
/*      */ 
/*      */       
/*      */       case 4:
/* 4828 */         if (!field.isUnsigned() || field.getMysqlType() == 9)
/*      */         {
/* 4830 */           return Constants.integerValueOf(getInt(columnIndex));
/*      */         }
/*      */         
/* 4833 */         return Constants.longValueOf(getLong(columnIndex));
/*      */ 
/*      */       
/*      */       case -5:
/* 4837 */         if (!field.isUnsigned()) {
/* 4838 */           return Constants.longValueOf(getLong(columnIndex));
/*      */         }
/*      */         
/* 4841 */         stringVal = getString(columnIndex);
/*      */         
/* 4843 */         if (stringVal == null) {
/* 4844 */           return null;
/*      */         }
/*      */         
/*      */         try {
/* 4848 */           return new BigInteger(stringVal);
/* 4849 */         } catch (NumberFormatException nfe) {
/* 4850 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigInteger", new Object[] { Constants.integerValueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 3:
/* 4858 */         stringVal = getString(columnIndex);
/*      */ 
/*      */ 
/*      */         
/* 4862 */         if (stringVal != null) {
/* 4863 */           BigDecimal val; if (stringVal.length() == 0) {
/* 4864 */             val = new BigDecimal(0.0D);
/*      */             
/* 4866 */             return val;
/*      */           } 
/*      */           
/*      */           try {
/* 4870 */             val = new BigDecimal(stringVal);
/* 4871 */           } catch (NumberFormatException ex) {
/* 4872 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4879 */           return val;
/*      */         } 
/*      */         
/* 4882 */         return null;
/*      */       
/*      */       case 7:
/* 4885 */         return new Float(getFloat(columnIndex));
/*      */       
/*      */       case 6:
/*      */       case 8:
/* 4889 */         return new Double(getDouble(columnIndex));
/*      */       
/*      */       case 1:
/*      */       case 12:
/* 4893 */         if (!field.isOpaqueBinary()) {
/* 4894 */           return getString(columnIndex);
/*      */         }
/*      */         
/* 4897 */         return getBytes(columnIndex);
/*      */       case -1:
/* 4899 */         if (!field.isOpaqueBinary()) {
/* 4900 */           return getStringForClob(columnIndex);
/*      */         }
/*      */         
/* 4903 */         return getBytes(columnIndex);
/*      */       
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/* 4908 */         if (field.getMysqlType() == 255)
/* 4909 */           return getBytes(columnIndex); 
/* 4910 */         if (field.isBinary() || field.isBlob()) {
/* 4911 */           byte[] data = getBytes(columnIndex);
/*      */           
/* 4913 */           if (this.connection.getAutoDeserialize()) {
/* 4914 */             Object obj = data;
/*      */             
/* 4916 */             if (data != null && data.length >= 2) {
/* 4917 */               if (data[0] == -84 && data[1] == -19) {
/*      */                 
/*      */                 try {
/* 4920 */                   ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */                   
/* 4922 */                   ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */                   
/* 4924 */                   obj = objIn.readObject();
/* 4925 */                   objIn.close();
/* 4926 */                   bytesIn.close();
/* 4927 */                 } catch (ClassNotFoundException cnfe) {
/* 4928 */                   throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/* 4934 */                 catch (IOException ex) {
/* 4935 */                   obj = data;
/*      */                 } 
/*      */               } else {
/* 4938 */                 return getString(columnIndex);
/*      */               } 
/*      */             }
/*      */             
/* 4942 */             return obj;
/*      */           } 
/*      */           
/* 4945 */           return data;
/*      */         } 
/*      */         
/* 4948 */         return getBytes(columnIndex);
/*      */       
/*      */       case 91:
/* 4951 */         if (field.getMysqlType() == 13 && !this.connection.getYearIsDateType())
/*      */         {
/* 4953 */           return Constants.shortValueOf(getShort(columnIndex));
/*      */         }
/*      */         
/* 4956 */         return getDate(columnIndex);
/*      */       
/*      */       case 92:
/* 4959 */         return getTime(columnIndex);
/*      */       
/*      */       case 93:
/* 4962 */         return getTimestamp(columnIndex);
/*      */     } 
/*      */     
/* 4965 */     return getString(columnIndex);
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
/*      */   public Object getObject(int i, Map map) throws SQLException {
/* 4985 */     return getObject(i);
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
/*      */   public Object getObject(String columnName) throws SQLException {
/* 5012 */     return getObject(findColumn(columnName));
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
/*      */   public Object getObject(String colName, Map map) throws SQLException {
/* 5032 */     return getObject(findColumn(colName), map);
/*      */   }
/*      */   
/*      */   public Object getObjectStoredProc(int columnIndex, int desiredSqlType) throws SQLException {
/*      */     String stringVal;
/* 5037 */     checkRowPos();
/* 5038 */     checkColumnBounds(columnIndex);
/*      */     
/* 5040 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 5042 */     if (value == null) {
/* 5043 */       this.wasNullFlag = true;
/*      */       
/* 5045 */       return null;
/*      */     } 
/*      */     
/* 5048 */     this.wasNullFlag = false;
/*      */ 
/*      */     
/* 5051 */     Field field = this.fields[columnIndex - 1];
/*      */     
/* 5053 */     switch (desiredSqlType) {
/*      */ 
/*      */ 
/*      */       
/*      */       case -7:
/*      */       case 16:
/* 5059 */         return Boolean.valueOf(getBoolean(columnIndex));
/*      */       
/*      */       case -6:
/* 5062 */         return Constants.integerValueOf(getInt(columnIndex));
/*      */       
/*      */       case 5:
/* 5065 */         return Constants.integerValueOf(getInt(columnIndex));
/*      */ 
/*      */       
/*      */       case 4:
/* 5069 */         if (!field.isUnsigned() || field.getMysqlType() == 9)
/*      */         {
/* 5071 */           return Constants.integerValueOf(getInt(columnIndex));
/*      */         }
/*      */         
/* 5074 */         return Constants.longValueOf(getLong(columnIndex));
/*      */ 
/*      */       
/*      */       case -5:
/* 5078 */         if (field.isUnsigned()) {
/* 5079 */           return getBigDecimal(columnIndex);
/*      */         }
/*      */         
/* 5082 */         return Constants.longValueOf(getLong(columnIndex));
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 3:
/* 5087 */         stringVal = getString(columnIndex);
/*      */ 
/*      */         
/* 5090 */         if (stringVal != null) {
/* 5091 */           BigDecimal val; if (stringVal.length() == 0) {
/* 5092 */             val = new BigDecimal(0.0D);
/*      */             
/* 5094 */             return val;
/*      */           } 
/*      */           
/*      */           try {
/* 5098 */             val = new BigDecimal(stringVal);
/* 5099 */           } catch (NumberFormatException ex) {
/* 5100 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 5107 */           return val;
/*      */         } 
/*      */         
/* 5110 */         return null;
/*      */       
/*      */       case 7:
/* 5113 */         return new Float(getFloat(columnIndex));
/*      */ 
/*      */       
/*      */       case 6:
/* 5117 */         if (!this.connection.getRunningCTS13()) {
/* 5118 */           return new Double(getFloat(columnIndex));
/*      */         }
/* 5120 */         return new Float(getFloat(columnIndex));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 8:
/* 5127 */         return new Double(getDouble(columnIndex));
/*      */       
/*      */       case 1:
/*      */       case 12:
/* 5131 */         return getString(columnIndex);
/*      */       case -1:
/* 5133 */         return getStringForClob(columnIndex);
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/* 5137 */         return getBytes(columnIndex);
/*      */       
/*      */       case 91:
/* 5140 */         if (field.getMysqlType() == 13 && !this.connection.getYearIsDateType())
/*      */         {
/* 5142 */           return Constants.shortValueOf(getShort(columnIndex));
/*      */         }
/*      */         
/* 5145 */         return getDate(columnIndex);
/*      */       
/*      */       case 92:
/* 5148 */         return getTime(columnIndex);
/*      */       
/*      */       case 93:
/* 5151 */         return getTimestamp(columnIndex);
/*      */     } 
/*      */     
/* 5154 */     return getString(columnIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObjectStoredProc(int i, Map map, int desiredSqlType) throws SQLException {
/* 5160 */     return getObjectStoredProc(i, desiredSqlType);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObjectStoredProc(String columnName, int desiredSqlType) throws SQLException {
/* 5165 */     return getObjectStoredProc(findColumn(columnName), desiredSqlType);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObjectStoredProc(String colName, Map map, int desiredSqlType) throws SQLException {
/* 5170 */     return getObjectStoredProc(findColumn(colName), map, desiredSqlType);
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
/*      */   public Ref getRef(int i) throws SQLException {
/* 5187 */     checkColumnBounds(i);
/* 5188 */     throw SQLError.notImplemented();
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
/*      */   public Ref getRef(String colName) throws SQLException {
/* 5205 */     return getRef(findColumn(colName));
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
/*      */   public int getRow() throws SQLException {
/* 5222 */     checkClosed();
/*      */     
/* 5224 */     int currentRowNumber = this.rowData.getCurrentRowNumber();
/* 5225 */     int row = 0;
/*      */ 
/*      */ 
/*      */     
/* 5229 */     if (!this.rowData.isDynamic()) {
/* 5230 */       if (currentRowNumber < 0 || this.rowData.isAfterLast() || this.rowData.isEmpty()) {
/*      */         
/* 5232 */         row = 0;
/*      */       } else {
/* 5234 */         row = currentRowNumber + 1;
/*      */       } 
/*      */     } else {
/*      */       
/* 5238 */       row = currentRowNumber + 1;
/*      */     } 
/*      */     
/* 5241 */     return row;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServerInfo() {
/* 5250 */     return this.serverInfo;
/*      */   }
/*      */ 
/*      */   
/*      */   private long getNumericRepresentationOfSQLBitType(int columnIndex) throws SQLException {
/* 5255 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 5257 */     if (this.fields[columnIndex - 1].isSingleBit() || ((byte[])value).length == 1)
/*      */     {
/* 5259 */       return ((byte[])value)[0];
/*      */     }
/*      */ 
/*      */     
/* 5263 */     byte[] asBytes = (byte[])value;
/*      */ 
/*      */     
/* 5266 */     int shift = 0;
/*      */     
/* 5268 */     long[] steps = new long[asBytes.length];
/*      */     
/* 5270 */     for (int i = asBytes.length - 1; i >= 0; i--) {
/* 5271 */       steps[i] = (asBytes[i] & 0xFF) << shift;
/* 5272 */       shift += 8;
/*      */     } 
/*      */     
/* 5275 */     long valueAsLong = 0L;
/*      */     
/* 5277 */     for (int j = 0; j < asBytes.length; j++) {
/* 5278 */       valueAsLong |= steps[j];
/*      */     }
/*      */     
/* 5281 */     return valueAsLong;
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
/*      */   public short getShort(int columnIndex) throws SQLException {
/* 5296 */     if (!this.isBinaryEncoded) {
/* 5297 */       checkRowPos();
/*      */       
/* 5299 */       if (this.useFastIntParsing) {
/*      */         
/* 5301 */         checkColumnBounds(columnIndex);
/*      */         
/* 5303 */         Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */         
/* 5305 */         if (value == null) {
/* 5306 */           this.wasNullFlag = true;
/*      */         } else {
/* 5308 */           this.wasNullFlag = false;
/*      */         } 
/*      */         
/* 5311 */         if (this.wasNullFlag) {
/* 5312 */           return 0;
/*      */         }
/*      */         
/* 5315 */         byte[] shortAsBytes = (byte[])value;
/*      */         
/* 5317 */         if (shortAsBytes.length == 0) {
/* 5318 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5321 */         boolean needsFullParse = false;
/*      */         
/* 5323 */         for (int i = 0; i < shortAsBytes.length; i++) {
/* 5324 */           if ((char)shortAsBytes[i] == 'e' || (char)shortAsBytes[i] == 'E') {
/*      */             
/* 5326 */             needsFullParse = true;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 5332 */         if (!needsFullParse) {
/*      */           try {
/* 5334 */             return parseShortWithOverflowCheck(columnIndex, shortAsBytes, null);
/*      */           }
/* 5336 */           catch (NumberFormatException nfe) {
/*      */             
/*      */             try {
/* 5339 */               return parseShortAsDouble(columnIndex, new String(shortAsBytes));
/*      */             }
/* 5341 */             catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */               
/* 5345 */               if (this.fields[columnIndex - 1].getMysqlType() == 16) {
/* 5346 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 5348 */                 if (this.jdbcCompliantTruncationForReads && (valueAsLong < -32768L || valueAsLong > 32767L))
/*      */                 {
/*      */                   
/* 5351 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */                 }
/*      */ 
/*      */                 
/* 5355 */                 return (short)(int)valueAsLong;
/*      */               } 
/*      */               
/* 5358 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + new String(shortAsBytes) + "'", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5368 */       String val = null;
/*      */       
/*      */       try {
/* 5371 */         val = getString(columnIndex);
/*      */         
/* 5373 */         if (val != null) {
/*      */           
/* 5375 */           if (val.length() == 0) {
/* 5376 */             return (short)convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 5379 */           if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1)
/*      */           {
/* 5381 */             return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 5386 */           return parseShortAsDouble(columnIndex, val);
/*      */         } 
/*      */         
/* 5389 */         return 0;
/* 5390 */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 5392 */           return parseShortAsDouble(columnIndex, val);
/* 5393 */         } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */           
/* 5397 */           if (this.fields[columnIndex - 1].getMysqlType() == 16) {
/* 5398 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 5400 */             if (this.jdbcCompliantTruncationForReads && (valueAsLong < -32768L || valueAsLong > 32767L))
/*      */             {
/*      */               
/* 5403 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */             }
/*      */ 
/*      */             
/* 5407 */             return (short)(int)valueAsLong;
/*      */           } 
/*      */           
/* 5410 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + val + "'", "S1009", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5418 */     return getNativeShort(columnIndex);
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
/*      */   public short getShort(String columnName) throws SQLException {
/* 5433 */     return getShort(findColumn(columnName));
/*      */   }
/*      */ 
/*      */   
/*      */   private final short getShortFromString(String val, int columnIndex) throws SQLException {
/*      */     try {
/* 5439 */       if (val != null) {
/*      */         
/* 5441 */         if (val.length() == 0) {
/* 5442 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5445 */         if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1)
/*      */         {
/* 5447 */           return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */         }
/*      */ 
/*      */         
/* 5451 */         return parseShortAsDouble(columnIndex, val);
/*      */       } 
/*      */       
/* 5454 */       return 0;
/* 5455 */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 5457 */         return parseShortAsDouble(columnIndex, val);
/* 5458 */       } catch (NumberFormatException newNfe) {
/*      */ 
/*      */ 
/*      */         
/* 5462 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____217") + val + Messages.getString("ResultSet.___in_column__218") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public Statement getStatement() throws SQLException {
/* 5481 */     if (this.isClosed && !this.retainOwningStatement) {
/* 5482 */       throw SQLError.createSQLException("Operation not allowed on closed ResultSet. Statements can be retained over result set closure by setting the connection property \"retainStatementAfterResultSetClose\" to \"true\".", "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5490 */     if (this.wrapperStatement != null) {
/* 5491 */       return this.wrapperStatement;
/*      */     }
/*      */     
/* 5494 */     return this.owningStatement;
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
/*      */   public String getString(int columnIndex) throws SQLException {
/* 5509 */     String stringVal = getStringInternal(columnIndex, true);
/*      */     
/* 5511 */     if (this.padCharsWithSpace && stringVal != null) {
/* 5512 */       Field f = this.fields[columnIndex - 1];
/*      */       
/* 5514 */       if (f.getMysqlType() == 254) {
/* 5515 */         int fieldLength = (int)f.getLength() / f.getMaxBytesPerCharacter();
/*      */ 
/*      */         
/* 5518 */         int currentLength = stringVal.length();
/*      */         
/* 5520 */         if (currentLength < fieldLength) {
/* 5521 */           StringBuffer paddedBuf = new StringBuffer(fieldLength);
/* 5522 */           paddedBuf.append(stringVal);
/*      */           
/* 5524 */           int difference = fieldLength - currentLength;
/*      */           
/* 5526 */           paddedBuf.append(EMPTY_SPACE, 0, difference);
/*      */           
/* 5528 */           stringVal = paddedBuf.toString();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 5533 */     return stringVal;
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
/*      */   public String getString(String columnName) throws SQLException {
/* 5549 */     return getString(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private String getStringForClob(int columnIndex) throws SQLException {
/* 5553 */     String asString = null;
/*      */     
/* 5555 */     String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */ 
/*      */     
/* 5558 */     if (forcedEncoding == null) {
/* 5559 */       if (!this.isBinaryEncoded) {
/* 5560 */         asString = getString(columnIndex);
/*      */       } else {
/* 5562 */         asString = getNativeString(columnIndex);
/*      */       } 
/*      */     } else {
/*      */       try {
/* 5566 */         byte[] asBytes = null;
/*      */         
/* 5568 */         if (!this.isBinaryEncoded) {
/* 5569 */           asBytes = getBytes(columnIndex);
/*      */         } else {
/* 5571 */           asBytes = getNativeBytes(columnIndex, true);
/*      */         } 
/*      */         
/* 5574 */         if (asBytes != null) {
/* 5575 */           asString = new String(asBytes, forcedEncoding);
/*      */         }
/* 5577 */       } catch (UnsupportedEncodingException uee) {
/* 5578 */         throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5583 */     return asString;
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getStringInternal(int columnIndex, boolean checkDateTypes) throws SQLException {
/* 5588 */     if (!this.isBinaryEncoded) {
/* 5589 */       checkRowPos();
/* 5590 */       checkColumnBounds(columnIndex);
/*      */       
/* 5592 */       if (this.fields == null) {
/* 5593 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_99"), "S1002", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5601 */       int internalColumnIndex = columnIndex - 1;
/*      */       
/* 5603 */       if (this.thisRow.isNull(internalColumnIndex)) {
/* 5604 */         this.wasNullFlag = true;
/*      */         
/* 5606 */         return null;
/*      */       } 
/*      */       
/* 5609 */       this.wasNullFlag = false;
/*      */ 
/*      */       
/* 5612 */       Field metadata = this.fields[internalColumnIndex];
/*      */       
/* 5614 */       String stringVal = null;
/*      */       
/* 5616 */       if (metadata.getMysqlType() == 16) {
/* 5617 */         if (metadata.isSingleBit()) {
/* 5618 */           byte[] value = this.thisRow.getColumnValue(internalColumnIndex);
/*      */           
/* 5620 */           if (value.length == 0) {
/* 5621 */             return String.valueOf(convertToZeroWithEmptyCheck());
/*      */           }
/*      */           
/* 5624 */           return String.valueOf(value[0]);
/*      */         } 
/*      */         
/* 5627 */         return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
/*      */       } 
/*      */       
/* 5630 */       String encoding = metadata.getCharacterSet();
/*      */       
/* 5632 */       stringVal = this.thisRow.getString(internalColumnIndex, encoding, this.connection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5639 */       if (metadata.getMysqlType() == 13) {
/* 5640 */         if (!this.connection.getYearIsDateType()) {
/* 5641 */           return stringVal;
/*      */         }
/*      */         
/* 5644 */         Date dt = getDateFromString(stringVal, columnIndex, null);
/*      */         
/* 5646 */         if (dt == null) {
/* 5647 */           this.wasNullFlag = true;
/*      */           
/* 5649 */           return null;
/*      */         } 
/*      */         
/* 5652 */         this.wasNullFlag = false;
/*      */         
/* 5654 */         return dt.toString();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 5659 */       if (checkDateTypes && !this.connection.getNoDatetimeStringSync()) {
/* 5660 */         Time tm; Date dt; Timestamp ts; switch (metadata.getSQLType()) {
/*      */           case 92:
/* 5662 */             tm = getTimeFromString(stringVal, null, columnIndex, getDefaultTimeZone(), false);
/*      */ 
/*      */             
/* 5665 */             if (tm == null) {
/* 5666 */               this.wasNullFlag = true;
/*      */               
/* 5668 */               return null;
/*      */             } 
/*      */             
/* 5671 */             this.wasNullFlag = false;
/*      */             
/* 5673 */             return tm.toString();
/*      */           
/*      */           case 91:
/* 5676 */             dt = getDateFromString(stringVal, columnIndex, null);
/*      */             
/* 5678 */             if (dt == null) {
/* 5679 */               this.wasNullFlag = true;
/*      */               
/* 5681 */               return null;
/*      */             } 
/*      */             
/* 5684 */             this.wasNullFlag = false;
/*      */             
/* 5686 */             return dt.toString();
/*      */           case 93:
/* 5688 */             ts = getTimestampFromString(columnIndex, null, stringVal, getDefaultTimeZone(), false);
/*      */ 
/*      */             
/* 5691 */             if (ts == null) {
/* 5692 */               this.wasNullFlag = true;
/*      */               
/* 5694 */               return null;
/*      */             } 
/*      */             
/* 5697 */             this.wasNullFlag = false;
/*      */             
/* 5699 */             return ts.toString();
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       } 
/* 5705 */       return stringVal;
/*      */     } 
/*      */     
/* 5708 */     return getNativeString(columnIndex);
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
/*      */   public Time getTime(int columnIndex) throws SQLException {
/* 5723 */     return getTimeInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Time getTime(int columnIndex, Calendar cal) throws SQLException {
/* 5743 */     return getTimeInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Time getTime(String columnName) throws SQLException {
/* 5758 */     return getTime(findColumn(columnName));
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
/*      */   public Time getTime(String columnName, Calendar cal) throws SQLException {
/* 5778 */     return getTime(findColumn(columnName), cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Time getTimeFromString(String timeAsString, Calendar targetCalendar, int columnIndex, TimeZone tz, boolean rollForward) throws SQLException {
/* 5785 */     int hr = 0;
/* 5786 */     int min = 0;
/* 5787 */     int sec = 0;
/*      */ 
/*      */     
/*      */     try {
/* 5791 */       if (timeAsString == null) {
/* 5792 */         this.wasNullFlag = true;
/*      */         
/* 5794 */         return null;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5805 */       timeAsString = timeAsString.trim();
/*      */       
/* 5807 */       if (timeAsString.equals("0") || timeAsString.equals("0000-00-00") || timeAsString.equals("0000-00-00 00:00:00") || timeAsString.equals("00000000000000")) {
/*      */ 
/*      */ 
/*      */         
/* 5811 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
/*      */           
/* 5813 */           this.wasNullFlag = true;
/*      */           
/* 5815 */           return null;
/* 5816 */         }  if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 5818 */           throw SQLError.createSQLException("Value '" + timeAsString + "' can not be represented as java.sql.Time", "S1009", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5825 */         return fastTimeCreate(targetCalendar, 0, 0, 0);
/*      */       } 
/*      */       
/* 5828 */       this.wasNullFlag = false;
/*      */       
/* 5830 */       Field timeColField = this.fields[columnIndex - 1];
/*      */       
/* 5832 */       if (timeColField.getMysqlType() == 7)
/*      */       
/* 5834 */       { int length = timeAsString.length();
/*      */         
/* 5836 */         switch (length) {
/*      */           
/*      */           case 19:
/* 5839 */             hr = Integer.parseInt(timeAsString.substring(length - 8, length - 6));
/*      */             
/* 5841 */             min = Integer.parseInt(timeAsString.substring(length - 5, length - 3));
/*      */             
/* 5843 */             sec = Integer.parseInt(timeAsString.substring(length - 2, length));
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/*      */           case 14:
/* 5850 */             hr = Integer.parseInt(timeAsString.substring(length - 6, length - 4));
/*      */             
/* 5852 */             min = Integer.parseInt(timeAsString.substring(length - 4, length - 2));
/*      */             
/* 5854 */             sec = Integer.parseInt(timeAsString.substring(length - 2, length));
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/* 5861 */             hr = Integer.parseInt(timeAsString.substring(6, 8));
/* 5862 */             min = Integer.parseInt(timeAsString.substring(8, 10));
/* 5863 */             sec = 0;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           default:
/* 5869 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257") + columnIndex + "(" + this.fields[columnIndex - 1] + ").", "S1009", getExceptionInterceptor());
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5878 */         SQLWarning precisionLost = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261") + columnIndex + "(" + this.fields[columnIndex - 1] + ").");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5885 */         if (this.warningChain == null) {
/* 5886 */           this.warningChain = precisionLost;
/*      */         } else {
/* 5888 */           this.warningChain.setNextWarning(precisionLost);
/*      */         }  }
/* 5890 */       else if (timeColField.getMysqlType() == 12)
/* 5891 */       { hr = Integer.parseInt(timeAsString.substring(11, 13));
/* 5892 */         min = Integer.parseInt(timeAsString.substring(14, 16));
/* 5893 */         sec = Integer.parseInt(timeAsString.substring(17, 19));
/*      */         
/* 5895 */         SQLWarning precisionLost = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264") + columnIndex + "(" + this.fields[columnIndex - 1] + ").");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5902 */         if (this.warningChain == null) {
/* 5903 */           this.warningChain = precisionLost;
/*      */         } else {
/* 5905 */           this.warningChain.setNextWarning(precisionLost);
/*      */         }  }
/* 5907 */       else { if (timeColField.getMysqlType() == 10) {
/* 5908 */           return fastTimeCreate(targetCalendar, 0, 0, 0);
/*      */         }
/*      */ 
/*      */         
/* 5912 */         if (timeAsString.length() != 5 && timeAsString.length() != 8)
/*      */         {
/* 5914 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Time____267") + timeAsString + Messages.getString("ResultSet.___in_column__268") + columnIndex, "S1009", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5921 */         hr = Integer.parseInt(timeAsString.substring(0, 2));
/* 5922 */         min = Integer.parseInt(timeAsString.substring(3, 5));
/* 5923 */         sec = (timeAsString.length() == 5) ? 0 : Integer.parseInt(timeAsString.substring(6)); }
/*      */ 
/*      */ 
/*      */       
/* 5927 */       Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */       
/* 5929 */       synchronized (sessionCalendar) {
/* 5930 */         return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimeCreate(sessionCalendar, hr, min, sec), this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 5938 */     catch (Exception ex) {
/* 5939 */       SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", getExceptionInterceptor());
/*      */       
/* 5941 */       sqlEx.initCause(ex);
/*      */       
/* 5943 */       throw sqlEx;
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
/*      */   private Time getTimeInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 5964 */     checkRowPos();
/*      */     
/* 5966 */     if (this.isBinaryEncoded) {
/* 5967 */       return getNativeTime(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 5970 */     if (!this.useFastDateParsing) {
/* 5971 */       String timeAsString = getStringInternal(columnIndex, false);
/*      */       
/* 5973 */       return getTimeFromString(timeAsString, targetCalendar, columnIndex, tz, rollForward);
/*      */     } 
/*      */ 
/*      */     
/* 5977 */     checkColumnBounds(columnIndex);
/*      */     
/* 5979 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 5981 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 5982 */       this.wasNullFlag = true;
/*      */       
/* 5984 */       return null;
/*      */     } 
/*      */     
/* 5987 */     this.wasNullFlag = false;
/*      */     
/* 5989 */     return this.thisRow.getTimeFast(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
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
/*      */   public Timestamp getTimestamp(int columnIndex) throws SQLException {
/* 6006 */     return getTimestampInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
/* 6028 */     return getTimestampInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Timestamp getTimestamp(String columnName) throws SQLException {
/* 6044 */     return getTimestamp(findColumn(columnName));
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
/*      */   public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
/* 6065 */     return getTimestamp(findColumn(columnName), cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Timestamp getTimestampFromString(int columnIndex, Calendar targetCalendar, String timestampValue, TimeZone tz, boolean rollForward) throws SQLException {
/*      */     try {
/* 6073 */       this.wasNullFlag = false;
/*      */       
/* 6075 */       if (timestampValue == null) {
/* 6076 */         this.wasNullFlag = true;
/*      */         
/* 6078 */         return null;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 6089 */       timestampValue = timestampValue.trim();
/*      */       
/* 6091 */       int length = timestampValue.length();
/*      */       
/* 6093 */       Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */       
/* 6097 */       synchronized (sessionCalendar) {
/* 6098 */         if (length > 0 && timestampValue.charAt(0) == '0' && (timestampValue.equals("0000-00-00") || timestampValue.equals("0000-00-00 00:00:00") || timestampValue.equals("00000000000000") || timestampValue.equals("0"))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 6105 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
/*      */             
/* 6107 */             this.wasNullFlag = true;
/*      */             
/* 6109 */             return null;
/* 6110 */           }  if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 6112 */             throw SQLError.createSQLException("Value '" + timestampValue + "' can not be represented as java.sql.Timestamp", "S1009", getExceptionInterceptor());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 6119 */           return fastTimestampCreate(null, 1, 1, 1, 0, 0, 0, 0);
/*      */         } 
/* 6121 */         if (this.fields[columnIndex - 1].getMysqlType() == 13) {
/*      */           
/* 6123 */           if (!this.useLegacyDatetimeCode) {
/* 6124 */             return TimeUtil.fastTimestampCreate(tz, Integer.parseInt(timestampValue.substring(0, 4)), 1, 1, 0, 0, 0, 0);
/*      */           }
/*      */ 
/*      */           
/* 6128 */           return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, Integer.parseInt(timestampValue.substring(0, 4)), 1, 1, 0, 0, 0, 0), this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6138 */         if (timestampValue.endsWith(".")) {
/* 6139 */           timestampValue = timestampValue.substring(0, timestampValue.length() - 1);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6145 */         int year = 0;
/* 6146 */         int month = 0;
/* 6147 */         int day = 0;
/* 6148 */         int hour = 0;
/* 6149 */         int minutes = 0;
/* 6150 */         int seconds = 0;
/* 6151 */         int nanos = 0;
/*      */         
/* 6153 */         switch (length) {
/*      */           case 19:
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/*      */           case 26:
/* 6162 */             year = Integer.parseInt(timestampValue.substring(0, 4));
/* 6163 */             month = Integer.parseInt(timestampValue.substring(5, 7));
/*      */             
/* 6165 */             day = Integer.parseInt(timestampValue.substring(8, 10));
/* 6166 */             hour = Integer.parseInt(timestampValue.substring(11, 13));
/*      */             
/* 6168 */             minutes = Integer.parseInt(timestampValue.substring(14, 16));
/*      */             
/* 6170 */             seconds = Integer.parseInt(timestampValue.substring(17, 19));
/*      */ 
/*      */             
/* 6173 */             nanos = 0;
/*      */             
/* 6175 */             if (length > 19) {
/* 6176 */               int decimalIndex = timestampValue.lastIndexOf('.');
/*      */               
/* 6178 */               if (decimalIndex != -1) {
/* 6179 */                 if (decimalIndex + 2 <= length) {
/* 6180 */                   nanos = Integer.parseInt(timestampValue.substring(decimalIndex + 1));
/*      */ 
/*      */                   
/* 6183 */                   int numDigits = length - decimalIndex + 1;
/*      */                   
/* 6185 */                   if (numDigits < 9) {
/* 6186 */                     int factor = (int)Math.pow(10.0D, (9 - numDigits));
/* 6187 */                     nanos *= factor;
/*      */                   }  break;
/*      */                 } 
/* 6190 */                 throw new IllegalArgumentException();
/*      */               } 
/*      */             } 
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 14:
/* 6204 */             year = Integer.parseInt(timestampValue.substring(0, 4));
/* 6205 */             month = Integer.parseInt(timestampValue.substring(4, 6));
/*      */             
/* 6207 */             day = Integer.parseInt(timestampValue.substring(6, 8));
/* 6208 */             hour = Integer.parseInt(timestampValue.substring(8, 10));
/*      */             
/* 6210 */             minutes = Integer.parseInt(timestampValue.substring(10, 12));
/*      */             
/* 6212 */             seconds = Integer.parseInt(timestampValue.substring(12, 14));
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/* 6219 */             year = Integer.parseInt(timestampValue.substring(0, 2));
/*      */             
/* 6221 */             if (year <= 69) {
/* 6222 */               year += 100;
/*      */             }
/*      */             
/* 6225 */             year += 1900;
/*      */             
/* 6227 */             month = Integer.parseInt(timestampValue.substring(2, 4));
/*      */             
/* 6229 */             day = Integer.parseInt(timestampValue.substring(4, 6));
/* 6230 */             hour = Integer.parseInt(timestampValue.substring(6, 8));
/* 6231 */             minutes = Integer.parseInt(timestampValue.substring(8, 10));
/*      */             
/* 6233 */             seconds = Integer.parseInt(timestampValue.substring(10, 12));
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/* 6240 */             if (this.fields[columnIndex - 1].getMysqlType() == 10 || timestampValue.indexOf("-") != -1) {
/*      */               
/* 6242 */               year = Integer.parseInt(timestampValue.substring(0, 4));
/* 6243 */               month = Integer.parseInt(timestampValue.substring(5, 7));
/*      */               
/* 6245 */               day = Integer.parseInt(timestampValue.substring(8, 10));
/* 6246 */               hour = 0;
/* 6247 */               minutes = 0; break;
/*      */             } 
/* 6249 */             year = Integer.parseInt(timestampValue.substring(0, 2));
/*      */             
/* 6251 */             if (year <= 69) {
/* 6252 */               year += 100;
/*      */             }
/*      */             
/* 6255 */             month = Integer.parseInt(timestampValue.substring(2, 4));
/*      */             
/* 6257 */             day = Integer.parseInt(timestampValue.substring(4, 6));
/* 6258 */             hour = Integer.parseInt(timestampValue.substring(6, 8));
/* 6259 */             minutes = Integer.parseInt(timestampValue.substring(8, 10));
/*      */ 
/*      */             
/* 6262 */             year += 1900;
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 8:
/* 6269 */             if (timestampValue.indexOf(":") != -1) {
/* 6270 */               hour = Integer.parseInt(timestampValue.substring(0, 2));
/*      */               
/* 6272 */               minutes = Integer.parseInt(timestampValue.substring(3, 5));
/*      */               
/* 6274 */               seconds = Integer.parseInt(timestampValue.substring(6, 8));
/*      */               
/* 6276 */               year = 1970;
/* 6277 */               month = 1;
/* 6278 */               day = 1;
/*      */               
/*      */               break;
/*      */             } 
/* 6282 */             year = Integer.parseInt(timestampValue.substring(0, 4));
/* 6283 */             month = Integer.parseInt(timestampValue.substring(4, 6));
/*      */             
/* 6285 */             day = Integer.parseInt(timestampValue.substring(6, 8));
/*      */             
/* 6287 */             year -= 1900;
/* 6288 */             month--;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 6:
/* 6294 */             year = Integer.parseInt(timestampValue.substring(0, 2));
/*      */             
/* 6296 */             if (year <= 69) {
/* 6297 */               year += 100;
/*      */             }
/*      */             
/* 6300 */             year += 1900;
/*      */             
/* 6302 */             month = Integer.parseInt(timestampValue.substring(2, 4));
/*      */             
/* 6304 */             day = Integer.parseInt(timestampValue.substring(4, 6));
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 4:
/* 6310 */             year = Integer.parseInt(timestampValue.substring(0, 2));
/*      */             
/* 6312 */             if (year <= 69) {
/* 6313 */               year += 100;
/*      */             }
/*      */             
/* 6316 */             year += 1900;
/*      */             
/* 6318 */             month = Integer.parseInt(timestampValue.substring(2, 4));
/*      */ 
/*      */             
/* 6321 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/* 6327 */             year = Integer.parseInt(timestampValue.substring(0, 2));
/*      */             
/* 6329 */             if (year <= 69) {
/* 6330 */               year += 100;
/*      */             }
/*      */             
/* 6333 */             year += 1900;
/* 6334 */             month = 1;
/* 6335 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           default:
/* 6341 */             throw new SQLException("Bad format for Timestamp '" + timestampValue + "' in column " + columnIndex + ".", "S1009");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6347 */         if (!this.useLegacyDatetimeCode) {
/* 6348 */           return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minutes, seconds, nanos);
/*      */         }
/*      */ 
/*      */         
/* 6352 */         return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, year, month, day, hour, minutes, seconds, nanos), this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 6360 */     catch (Exception e) {
/* 6361 */       SQLException sqlEx = SQLError.createSQLException("Cannot convert value '" + timestampValue + "' from column " + columnIndex + " to TIMESTAMP.", "S1009", getExceptionInterceptor());
/*      */ 
/*      */       
/* 6364 */       sqlEx.initCause(e);
/*      */       
/* 6366 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Timestamp getTimestampFromBytes(int columnIndex, Calendar targetCalendar, byte[] timestampAsBytes, TimeZone tz, boolean rollForward) throws SQLException {
/* 6375 */     checkColumnBounds(columnIndex);
/*      */     
/*      */     try {
/* 6378 */       this.wasNullFlag = false;
/*      */       
/* 6380 */       if (timestampAsBytes == null) {
/* 6381 */         this.wasNullFlag = true;
/*      */         
/* 6383 */         return null;
/*      */       } 
/*      */       
/* 6386 */       int length = timestampAsBytes.length;
/*      */       
/* 6388 */       Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */       
/* 6392 */       synchronized (sessionCalendar) {
/* 6393 */         boolean allZeroTimestamp = true;
/*      */         
/* 6395 */         boolean onlyTimePresent = (StringUtils.indexOf(timestampAsBytes, ':') != -1);
/*      */         
/* 6397 */         for (int i = 0; i < length; i++) {
/* 6398 */           byte b = timestampAsBytes[i];
/*      */           
/* 6400 */           if (b == 32 || b == 45 || b == 47) {
/* 6401 */             onlyTimePresent = false;
/*      */           }
/*      */           
/* 6404 */           if (b != 48 && b != 32 && b != 58 && b != 45 && b != 47 && b != 46) {
/*      */             
/* 6406 */             allZeroTimestamp = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 6412 */         if (!onlyTimePresent && allZeroTimestamp) {
/*      */           
/* 6414 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
/*      */             
/* 6416 */             this.wasNullFlag = true;
/*      */             
/* 6418 */             return null;
/* 6419 */           }  if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 6421 */             throw SQLError.createSQLException("Value '" + timestampAsBytes + "' can not be represented as java.sql.Timestamp", "S1009", getExceptionInterceptor());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 6428 */           if (!this.useLegacyDatetimeCode) {
/* 6429 */             return TimeUtil.fastTimestampCreate(tz, 1, 1, 1, 0, 0, 0, 0);
/*      */           }
/*      */ 
/*      */           
/* 6433 */           return fastTimestampCreate(null, 1, 1, 1, 0, 0, 0, 0);
/* 6434 */         }  if (this.fields[columnIndex - 1].getMysqlType() == 13) {
/*      */           
/* 6436 */           if (!this.useLegacyDatetimeCode) {
/* 6437 */             return TimeUtil.fastTimestampCreate(tz, StringUtils.getInt(timestampAsBytes, 0, 4), 1, 1, 0, 0, 0, 0);
/*      */           }
/*      */ 
/*      */           
/* 6441 */           return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, StringUtils.getInt(timestampAsBytes, 0, 4), 1, 1, 0, 0, 0, 0), this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6449 */         if (timestampAsBytes[length - 1] == 46) {
/* 6450 */           length--;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 6455 */         int year = 0;
/* 6456 */         int month = 0;
/* 6457 */         int day = 0;
/* 6458 */         int hour = 0;
/* 6459 */         int minutes = 0;
/* 6460 */         int seconds = 0;
/* 6461 */         int nanos = 0;
/*      */         
/* 6463 */         switch (length) {
/*      */           case 19:
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/*      */           case 26:
/* 6472 */             year = StringUtils.getInt(timestampAsBytes, 0, 4);
/* 6473 */             month = StringUtils.getInt(timestampAsBytes, 5, 7);
/* 6474 */             day = StringUtils.getInt(timestampAsBytes, 8, 10);
/* 6475 */             hour = StringUtils.getInt(timestampAsBytes, 11, 13);
/* 6476 */             minutes = StringUtils.getInt(timestampAsBytes, 14, 16);
/* 6477 */             seconds = StringUtils.getInt(timestampAsBytes, 17, 19);
/*      */             
/* 6479 */             nanos = 0;
/*      */             
/* 6481 */             if (length > 19) {
/* 6482 */               int decimalIndex = StringUtils.lastIndexOf(timestampAsBytes, '.');
/*      */               
/* 6484 */               if (decimalIndex != -1) {
/* 6485 */                 if (decimalIndex + 2 <= length) {
/* 6486 */                   nanos = StringUtils.getInt(timestampAsBytes, decimalIndex + 1, length); break;
/*      */                 } 
/* 6488 */                 throw new IllegalArgumentException();
/*      */               } 
/*      */             } 
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 14:
/* 6502 */             year = StringUtils.getInt(timestampAsBytes, 0, 4);
/* 6503 */             month = StringUtils.getInt(timestampAsBytes, 4, 6);
/* 6504 */             day = StringUtils.getInt(timestampAsBytes, 6, 8);
/* 6505 */             hour = StringUtils.getInt(timestampAsBytes, 8, 10);
/* 6506 */             minutes = StringUtils.getInt(timestampAsBytes, 10, 12);
/* 6507 */             seconds = StringUtils.getInt(timestampAsBytes, 12, 14);
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 12:
/* 6513 */             year = StringUtils.getInt(timestampAsBytes, 0, 2);
/*      */             
/* 6515 */             if (year <= 69) {
/* 6516 */               year += 100;
/*      */             }
/*      */             
/* 6519 */             year += 1900;
/*      */             
/* 6521 */             month = StringUtils.getInt(timestampAsBytes, 2, 4);
/* 6522 */             day = StringUtils.getInt(timestampAsBytes, 4, 6);
/* 6523 */             hour = StringUtils.getInt(timestampAsBytes, 6, 8);
/* 6524 */             minutes = StringUtils.getInt(timestampAsBytes, 8, 10);
/* 6525 */             seconds = StringUtils.getInt(timestampAsBytes, 10, 12);
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 10:
/* 6531 */             if (this.fields[columnIndex - 1].getMysqlType() == 10 || StringUtils.indexOf(timestampAsBytes, '-') != -1) {
/*      */               
/* 6533 */               year = StringUtils.getInt(timestampAsBytes, 0, 4);
/* 6534 */               month = StringUtils.getInt(timestampAsBytes, 5, 7);
/* 6535 */               day = StringUtils.getInt(timestampAsBytes, 8, 10);
/* 6536 */               hour = 0;
/* 6537 */               minutes = 0; break;
/*      */             } 
/* 6539 */             year = StringUtils.getInt(timestampAsBytes, 0, 2);
/*      */             
/* 6541 */             if (year <= 69) {
/* 6542 */               year += 100;
/*      */             }
/*      */             
/* 6545 */             month = StringUtils.getInt(timestampAsBytes, 2, 4);
/* 6546 */             day = StringUtils.getInt(timestampAsBytes, 4, 6);
/* 6547 */             hour = StringUtils.getInt(timestampAsBytes, 6, 8);
/* 6548 */             minutes = StringUtils.getInt(timestampAsBytes, 8, 10);
/*      */             
/* 6550 */             year += 1900;
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 8:
/* 6557 */             if (StringUtils.indexOf(timestampAsBytes, ':') != -1) {
/* 6558 */               hour = StringUtils.getInt(timestampAsBytes, 0, 2);
/* 6559 */               minutes = StringUtils.getInt(timestampAsBytes, 3, 5);
/* 6560 */               seconds = StringUtils.getInt(timestampAsBytes, 6, 8);
/*      */               
/* 6562 */               year = 1970;
/* 6563 */               month = 1;
/* 6564 */               day = 1;
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/* 6569 */             year = StringUtils.getInt(timestampAsBytes, 0, 4);
/* 6570 */             month = StringUtils.getInt(timestampAsBytes, 4, 6);
/* 6571 */             day = StringUtils.getInt(timestampAsBytes, 6, 8);
/*      */             
/* 6573 */             year -= 1900;
/* 6574 */             month--;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 6:
/* 6580 */             year = StringUtils.getInt(timestampAsBytes, 0, 2);
/*      */             
/* 6582 */             if (year <= 69) {
/* 6583 */               year += 100;
/*      */             }
/*      */             
/* 6586 */             year += 1900;
/*      */             
/* 6588 */             month = StringUtils.getInt(timestampAsBytes, 2, 4);
/* 6589 */             day = StringUtils.getInt(timestampAsBytes, 4, 6);
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 4:
/* 6595 */             year = StringUtils.getInt(timestampAsBytes, 0, 2);
/*      */             
/* 6597 */             if (year <= 69) {
/* 6598 */               year += 100;
/*      */             }
/*      */             
/* 6601 */             year += 1900;
/*      */             
/* 6603 */             month = StringUtils.getInt(timestampAsBytes, 2, 4);
/* 6604 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/* 6610 */             year = StringUtils.getInt(timestampAsBytes, 0, 2);
/*      */             
/* 6612 */             if (year <= 69) {
/* 6613 */               year += 100;
/*      */             }
/*      */             
/* 6616 */             year += 1900;
/* 6617 */             month = 1;
/* 6618 */             day = 1;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           default:
/* 6624 */             throw new SQLException("Bad format for Timestamp '" + new String(timestampAsBytes) + "' in column " + columnIndex + ".", "S1009");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 6630 */         if (!this.useLegacyDatetimeCode) {
/* 6631 */           return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minutes, seconds, nanos);
/*      */         }
/*      */ 
/*      */         
/* 6635 */         return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, year, month, day, hour, minutes, seconds, nanos), this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 6643 */     catch (Exception e) {
/* 6644 */       SQLException sqlEx = SQLError.createSQLException("Cannot convert value '" + new String(timestampAsBytes) + "' from column " + columnIndex + " to TIMESTAMP.", "S1009", getExceptionInterceptor());
/*      */ 
/*      */       
/* 6647 */       sqlEx.initCause(e);
/*      */       
/* 6649 */       throw sqlEx;
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
/*      */   private Timestamp getTimestampInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 6670 */     if (this.isBinaryEncoded) {
/* 6671 */       return getNativeTimestamp(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 6674 */     Timestamp tsVal = null;
/*      */     
/* 6676 */     if (!this.useFastDateParsing) {
/* 6677 */       String timestampValue = getStringInternal(columnIndex, false);
/*      */       
/* 6679 */       tsVal = getTimestampFromString(columnIndex, targetCalendar, timestampValue, tz, rollForward);
/*      */     }
/*      */     else {
/*      */       
/* 6683 */       checkClosed();
/* 6684 */       checkRowPos();
/* 6685 */       checkColumnBounds(columnIndex);
/*      */       
/* 6687 */       tsVal = this.thisRow.getTimestampFast(columnIndex - 1, targetCalendar, tz, rollForward, this.connection, this);
/*      */     } 
/*      */ 
/*      */     
/* 6691 */     if (tsVal == null) {
/* 6692 */       this.wasNullFlag = true;
/*      */     } else {
/* 6694 */       this.wasNullFlag = false;
/*      */     } 
/*      */     
/* 6697 */     return tsVal;
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
/*      */   public int getType() throws SQLException {
/* 6711 */     return this.resultSetType;
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
/*      */   public InputStream getUnicodeStream(int columnIndex) throws SQLException {
/* 6733 */     if (!this.isBinaryEncoded) {
/* 6734 */       checkRowPos();
/*      */       
/* 6736 */       return getBinaryStream(columnIndex);
/*      */     } 
/*      */     
/* 6739 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getUnicodeStream(String columnName) throws SQLException {
/* 6756 */     return getUnicodeStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public long getUpdateCount() {
/* 6760 */     return this.updateCount;
/*      */   }
/*      */   
/*      */   public long getUpdateID() {
/* 6764 */     return this.updateId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(int colIndex) throws SQLException {
/* 6771 */     String val = getString(colIndex);
/*      */     
/* 6773 */     if (val == null) {
/* 6774 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 6778 */       return new URL(val);
/* 6779 */     } catch (MalformedURLException mfe) {
/* 6780 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____104") + val + "'", "S1009", getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(String colName) throws SQLException {
/* 6790 */     String val = getString(colName);
/*      */     
/* 6792 */     if (val == null) {
/* 6793 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 6797 */       return new URL(val);
/* 6798 */     } catch (MalformedURLException mfe) {
/* 6799 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____107") + val + "'", "S1009", getExceptionInterceptor());
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
/*      */   public SQLWarning getWarnings() throws SQLException {
/* 6826 */     return this.warningChain;
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
/*      */   public void insertRow() throws SQLException {
/* 6841 */     throw new NotUpdatable();
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
/*      */   public boolean isAfterLast() throws SQLException {
/* 6858 */     checkClosed();
/*      */     
/* 6860 */     boolean b = this.rowData.isAfterLast();
/*      */     
/* 6862 */     return b;
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
/*      */   public boolean isBeforeFirst() throws SQLException {
/* 6879 */     checkClosed();
/*      */     
/* 6881 */     return this.rowData.isBeforeFirst();
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
/*      */   public boolean isFirst() throws SQLException {
/* 6897 */     checkClosed();
/*      */     
/* 6899 */     return this.rowData.isFirst();
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
/*      */   public boolean isLast() throws SQLException {
/* 6918 */     checkClosed();
/*      */     
/* 6920 */     return this.rowData.isLast();
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
/*      */   private void issueConversionViaParsingWarning(String methodName, int columnIndex, Object value, Field fieldInfo, int[] typesWithNoParseConversion) throws SQLException {
/* 6932 */     StringBuffer originalQueryBuf = new StringBuffer();
/*      */     
/* 6934 */     if (this.owningStatement != null && this.owningStatement instanceof PreparedStatement) {
/*      */       
/* 6936 */       originalQueryBuf.append(Messages.getString("ResultSet.CostlyConversionCreatedFromQuery"));
/* 6937 */       originalQueryBuf.append(((PreparedStatement)this.owningStatement).originalSql);
/*      */       
/* 6939 */       originalQueryBuf.append("\n\n");
/*      */     } else {
/* 6941 */       originalQueryBuf.append(".");
/*      */     } 
/*      */     
/* 6944 */     StringBuffer convertibleTypesBuf = new StringBuffer();
/*      */     
/* 6946 */     for (int i = 0; i < typesWithNoParseConversion.length; i++) {
/* 6947 */       convertibleTypesBuf.append(MysqlDefs.typeToName(typesWithNoParseConversion[i]));
/* 6948 */       convertibleTypesBuf.append("\n");
/*      */     } 
/*      */     
/* 6951 */     String message = Messages.getString("ResultSet.CostlyConversion", new Object[] { methodName, new Integer(columnIndex + 1), fieldInfo.getOriginalName(), fieldInfo.getOriginalTableName(), originalQueryBuf.toString(), (value != null) ? value.getClass().getName() : ResultSetMetaData.getClassNameForJavaType(fieldInfo.getSQLType(), fieldInfo.isUnsigned(), fieldInfo.getMysqlType(), (fieldInfo.isBinary() || fieldInfo.isBlob()), fieldInfo.isOpaqueBinary()), MysqlDefs.typeToName(fieldInfo.getMysqlType()), convertibleTypesBuf.toString() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 6966 */     this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
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
/*      */   public boolean last() throws SQLException {
/* 6990 */     checkClosed();
/*      */     
/* 6992 */     boolean b = true;
/*      */     
/* 6994 */     if (this.rowData.size() == 0) {
/* 6995 */       b = false;
/*      */     } else {
/*      */       
/* 6998 */       if (this.onInsertRow) {
/* 6999 */         this.onInsertRow = false;
/*      */       }
/*      */       
/* 7002 */       if (this.doingUpdates) {
/* 7003 */         this.doingUpdates = false;
/*      */       }
/*      */       
/* 7006 */       if (this.thisRow != null) {
/* 7007 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/* 7010 */       this.rowData.beforeLast();
/* 7011 */       this.thisRow = this.rowData.next();
/*      */     } 
/*      */     
/* 7014 */     setRowPositionValidity();
/*      */     
/* 7016 */     return b;
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
/*      */   public void moveToCurrentRow() throws SQLException {
/* 7038 */     throw new NotUpdatable();
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
/*      */   public void moveToInsertRow() throws SQLException {
/* 7059 */     throw new NotUpdatable();
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
/*      */   public boolean next() throws SQLException {
/*      */     boolean b;
/* 7078 */     checkClosed();
/*      */     
/* 7080 */     if (this.onInsertRow) {
/* 7081 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 7084 */     if (this.doingUpdates) {
/* 7085 */       this.doingUpdates = false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 7090 */     if (!reallyResult()) {
/* 7091 */       throw SQLError.createSQLException(Messages.getString("ResultSet.ResultSet_is_from_UPDATE._No_Data_115"), "S1000", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7097 */     if (this.thisRow != null) {
/* 7098 */       this.thisRow.closeOpenStreams();
/*      */     }
/*      */     
/* 7101 */     if (this.rowData.size() == 0) {
/* 7102 */       b = false;
/*      */     } else {
/* 7104 */       this.thisRow = this.rowData.next();
/*      */       
/* 7106 */       if (this.thisRow == null) {
/* 7107 */         b = false;
/*      */       } else {
/* 7109 */         clearWarnings();
/*      */         
/* 7111 */         b = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 7116 */     setRowPositionValidity();
/*      */     
/* 7118 */     return b;
/*      */   }
/*      */ 
/*      */   
/*      */   private int parseIntAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException {
/* 7123 */     if (val == null) {
/* 7124 */       return 0;
/*      */     }
/*      */     
/* 7127 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7129 */     if (this.jdbcCompliantTruncationForReads && (
/* 7130 */       valueAsDouble < -2.147483648E9D || valueAsDouble > 2.147483647E9D))
/*      */     {
/* 7132 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 7137 */     return (int)valueAsDouble;
/*      */   }
/*      */   
/*      */   private int getIntWithOverflowCheck(int columnIndex) throws SQLException {
/* 7141 */     int intValue = this.thisRow.getInt(columnIndex);
/*      */     
/* 7143 */     checkForIntegerTruncation(columnIndex, null, intValue);
/*      */ 
/*      */     
/* 7146 */     return intValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkForIntegerTruncation(int columnIndex, byte[] valueAsBytes, int intValue) throws SQLException {
/* 7152 */     if (this.jdbcCompliantTruncationForReads && (
/* 7153 */       intValue == Integer.MIN_VALUE || intValue == Integer.MAX_VALUE)) {
/* 7154 */       String valueAsString = null;
/*      */       
/* 7156 */       if (valueAsBytes == null) {
/* 7157 */         valueAsString = this.thisRow.getString(columnIndex, this.fields[columnIndex].getCharacterSet(), this.connection);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 7162 */       long valueAsLong = Long.parseLong((valueAsString == null) ? new String(valueAsBytes) : valueAsString);
/*      */ 
/*      */ 
/*      */       
/* 7166 */       if (valueAsLong < -2147483648L || valueAsLong > 2147483647L)
/*      */       {
/* 7168 */         throwRangeException((valueAsString == null) ? new String(valueAsBytes) : valueAsString, columnIndex + 1, 4);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long parseLongAsDouble(int columnIndexZeroBased, String val) throws NumberFormatException, SQLException {
/* 7178 */     if (val == null) {
/* 7179 */       return 0L;
/*      */     }
/*      */     
/* 7182 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7184 */     if (this.jdbcCompliantTruncationForReads && (
/* 7185 */       valueAsDouble < -9.223372036854776E18D || valueAsDouble > 9.223372036854776E18D))
/*      */     {
/* 7187 */       throwRangeException(val, columnIndexZeroBased + 1, -5);
/*      */     }
/*      */ 
/*      */     
/* 7191 */     return (long)valueAsDouble;
/*      */   }
/*      */   
/*      */   private long getLongWithOverflowCheck(int columnIndexZeroBased, boolean doOverflowCheck) throws SQLException {
/* 7195 */     long longValue = this.thisRow.getLong(columnIndexZeroBased);
/*      */     
/* 7197 */     if (doOverflowCheck) {
/* 7198 */       checkForLongTruncation(columnIndexZeroBased, null, longValue);
/*      */     }
/*      */     
/* 7201 */     return longValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long parseLongWithOverflowCheck(int columnIndexZeroBased, byte[] valueAsBytes, String valueAsString, boolean doCheck) throws NumberFormatException, SQLException {
/* 7208 */     long longValue = 0L;
/*      */     
/* 7210 */     if (valueAsBytes == null && valueAsString == null) {
/* 7211 */       return 0L;
/*      */     }
/*      */     
/* 7214 */     if (valueAsBytes != null) {
/* 7215 */       longValue = StringUtils.getLong(valueAsBytes);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7225 */       valueAsString = valueAsString.trim();
/*      */       
/* 7227 */       longValue = Long.parseLong(valueAsString);
/*      */     } 
/*      */     
/* 7230 */     if (doCheck && this.jdbcCompliantTruncationForReads) {
/* 7231 */       checkForLongTruncation(columnIndexZeroBased, valueAsBytes, longValue);
/*      */     }
/*      */     
/* 7234 */     return longValue;
/*      */   }
/*      */   
/*      */   private void checkForLongTruncation(int columnIndexZeroBased, byte[] valueAsBytes, long longValue) throws SQLException {
/* 7238 */     if (longValue == Long.MIN_VALUE || longValue == Long.MAX_VALUE) {
/*      */       
/* 7240 */       String valueAsString = null;
/*      */       
/* 7242 */       if (valueAsBytes == null) {
/* 7243 */         valueAsString = this.thisRow.getString(columnIndexZeroBased, this.fields[columnIndexZeroBased].getCharacterSet(), this.connection);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 7248 */       double valueAsDouble = Double.parseDouble((valueAsString == null) ? new String(valueAsBytes) : valueAsString);
/*      */ 
/*      */ 
/*      */       
/* 7252 */       if (valueAsDouble < -9.223372036854776E18D || valueAsDouble > 9.223372036854776E18D)
/*      */       {
/* 7254 */         throwRangeException((valueAsString == null) ? new String(valueAsBytes) : valueAsString, columnIndexZeroBased + 1, -5);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private short parseShortAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException {
/* 7263 */     if (val == null) {
/* 7264 */       return 0;
/*      */     }
/*      */     
/* 7267 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7269 */     if (this.jdbcCompliantTruncationForReads && (
/* 7270 */       valueAsDouble < -32768.0D || valueAsDouble > 32767.0D))
/*      */     {
/* 7272 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 5);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 7277 */     return (short)(int)valueAsDouble;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private short parseShortWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString) throws NumberFormatException, SQLException {
/* 7284 */     short shortValue = 0;
/*      */     
/* 7286 */     if (valueAsBytes == null && valueAsString == null) {
/* 7287 */       return 0;
/*      */     }
/*      */     
/* 7290 */     if (valueAsBytes != null) {
/* 7291 */       shortValue = StringUtils.getShort(valueAsBytes);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 7301 */       valueAsString = valueAsString.trim();
/*      */       
/* 7303 */       shortValue = Short.parseShort(valueAsString);
/*      */     } 
/*      */     
/* 7306 */     if (this.jdbcCompliantTruncationForReads && (
/* 7307 */       shortValue == Short.MIN_VALUE || shortValue == Short.MAX_VALUE)) {
/* 7308 */       long valueAsLong = Long.parseLong((valueAsString == null) ? new String(valueAsBytes) : valueAsString);
/*      */ 
/*      */ 
/*      */       
/* 7312 */       if (valueAsLong < -32768L || valueAsLong > 32767L)
/*      */       {
/* 7314 */         throwRangeException((valueAsString == null) ? new String(valueAsBytes) : valueAsString, columnIndex, 5);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7321 */     return shortValue;
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
/*      */   public boolean prev() throws SQLException {
/* 7345 */     checkClosed();
/*      */     
/* 7347 */     int rowIndex = this.rowData.getCurrentRowNumber();
/*      */     
/* 7349 */     if (this.thisRow != null) {
/* 7350 */       this.thisRow.closeOpenStreams();
/*      */     }
/*      */     
/* 7353 */     boolean b = true;
/*      */     
/* 7355 */     if (rowIndex - 1 >= 0) {
/* 7356 */       rowIndex--;
/* 7357 */       this.rowData.setCurrentRow(rowIndex);
/* 7358 */       this.thisRow = this.rowData.getAt(rowIndex);
/*      */       
/* 7360 */       b = true;
/* 7361 */     } else if (rowIndex - 1 == -1) {
/* 7362 */       rowIndex--;
/* 7363 */       this.rowData.setCurrentRow(rowIndex);
/* 7364 */       this.thisRow = null;
/*      */       
/* 7366 */       b = false;
/*      */     } else {
/* 7368 */       b = false;
/*      */     } 
/*      */     
/* 7371 */     setRowPositionValidity();
/*      */     
/* 7373 */     return b;
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
/*      */   public boolean previous() throws SQLException {
/* 7395 */     if (this.onInsertRow) {
/* 7396 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 7399 */     if (this.doingUpdates) {
/* 7400 */       this.doingUpdates = false;
/*      */     }
/*      */     
/* 7403 */     return prev();
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
/* 7416 */     if (this.isClosed) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/* 7421 */       if (this.useUsageAdvisor)
/*      */       {
/*      */ 
/*      */         
/* 7425 */         if (!calledExplicitly) {
/* 7426 */           this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.ResultSet_implicitly_closed_by_driver")));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7445 */         if (this.rowData instanceof RowDataStatic) {
/*      */ 
/*      */ 
/*      */           
/* 7449 */           if (this.rowData.size() > this.connection.getResultSetSizeThreshold())
/*      */           {
/* 7451 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Too_Large_Result_Set", new Object[] { new Integer(this.rowData.size()), new Integer(this.connection.getResultSetSizeThreshold()) })));
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 7479 */           if (!isLast() && !isAfterLast() && this.rowData.size() != 0)
/*      */           {
/* 7481 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Possible_incomplete_traversal_of_result_set", new Object[] { new Integer(getRow()), new Integer(this.rowData.size()) })));
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7514 */         if (this.columnUsed.length > 0 && !this.rowData.wasEmpty()) {
/* 7515 */           StringBuffer buf = new StringBuffer(Messages.getString("ResultSet.The_following_columns_were_never_referenced"));
/*      */ 
/*      */ 
/*      */           
/* 7519 */           boolean issueWarn = false;
/*      */           
/* 7521 */           for (int i = 0; i < this.columnUsed.length; i++) {
/* 7522 */             if (!this.columnUsed[i]) {
/* 7523 */               if (!issueWarn) {
/* 7524 */                 issueWarn = true;
/*      */               } else {
/* 7526 */                 buf.append(", ");
/*      */               } 
/*      */               
/* 7529 */               buf.append(this.fields[i].getFullName());
/*      */             } 
/*      */           } 
/*      */           
/* 7533 */           if (issueWarn) {
/* 7534 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", (this.owningStatement == null) ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, (this.owningStatement == null) ? -1 : this.owningStatement.getId(), 0, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, buf.toString()));
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/* 7548 */       if (this.owningStatement != null && calledExplicitly) {
/* 7549 */         this.owningStatement.removeOpenResultSet(this);
/*      */       }
/*      */       
/* 7552 */       SQLException exceptionDuringClose = null;
/*      */       
/* 7554 */       if (this.rowData != null) {
/*      */         try {
/* 7556 */           this.rowData.close();
/* 7557 */         } catch (SQLException sqlEx) {
/* 7558 */           exceptionDuringClose = sqlEx;
/*      */         } 
/*      */       }
/*      */       
/* 7562 */       if (this.statementUsedForFetchingRows != null) {
/*      */         try {
/* 7564 */           this.statementUsedForFetchingRows.realClose(true, false);
/* 7565 */         } catch (SQLException sqlEx) {
/* 7566 */           if (exceptionDuringClose != null) {
/* 7567 */             exceptionDuringClose.setNextException(sqlEx);
/*      */           } else {
/* 7569 */             exceptionDuringClose = sqlEx;
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/* 7574 */       this.rowData = null;
/* 7575 */       this.defaultTimeZone = null;
/* 7576 */       this.fields = null;
/* 7577 */       this.columnLabelToIndex = null;
/* 7578 */       this.fullColumnNameToIndex = null;
/* 7579 */       this.columnToIndexCache = null;
/* 7580 */       this.eventSink = null;
/* 7581 */       this.warningChain = null;
/*      */       
/* 7583 */       if (!this.retainOwningStatement) {
/* 7584 */         this.owningStatement = null;
/*      */       }
/*      */       
/* 7587 */       this.catalog = null;
/* 7588 */       this.serverInfo = null;
/* 7589 */       this.thisRow = null;
/* 7590 */       this.fastDateCal = null;
/* 7591 */       this.connection = null;
/*      */       
/* 7593 */       this.isClosed = true;
/*      */       
/* 7595 */       if (exceptionDuringClose != null) {
/* 7596 */         throw exceptionDuringClose;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean reallyResult() {
/* 7602 */     if (this.rowData != null) {
/* 7603 */       return true;
/*      */     }
/*      */     
/* 7606 */     return this.reallyResult;
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
/*      */   public void refreshRow() throws SQLException {
/* 7630 */     throw new NotUpdatable();
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
/*      */   public boolean relative(int rows) throws SQLException {
/* 7660 */     checkClosed();
/*      */     
/* 7662 */     if (this.rowData.size() == 0) {
/* 7663 */       setRowPositionValidity();
/*      */       
/* 7665 */       return false;
/*      */     } 
/*      */     
/* 7668 */     if (this.thisRow != null) {
/* 7669 */       this.thisRow.closeOpenStreams();
/*      */     }
/*      */     
/* 7672 */     this.rowData.moveRowRelative(rows);
/* 7673 */     this.thisRow = this.rowData.getAt(this.rowData.getCurrentRowNumber());
/*      */     
/* 7675 */     setRowPositionValidity();
/*      */     
/* 7677 */     return (!this.rowData.isAfterLast() && !this.rowData.isBeforeFirst());
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
/*      */   public boolean rowDeleted() throws SQLException {
/* 7696 */     throw SQLError.notImplemented();
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
/*      */   public boolean rowInserted() throws SQLException {
/* 7714 */     throw SQLError.notImplemented();
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
/*      */   public boolean rowUpdated() throws SQLException {
/* 7732 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setBinaryEncoded() {
/* 7740 */     this.isBinaryEncoded = true;
/*      */   }
/*      */   
/*      */   private void setDefaultTimeZone(TimeZone defaultTimeZone) {
/* 7744 */     this.defaultTimeZone = defaultTimeZone;
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
/*      */   public void setFetchDirection(int direction) throws SQLException {
/* 7763 */     if (direction != 1000 && direction != 1001 && direction != 1002)
/*      */     {
/* 7765 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Illegal_value_for_fetch_direction_64"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7771 */     this.fetchDirection = direction;
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
/*      */   public void setFetchSize(int rows) throws SQLException {
/* 7791 */     if (rows < 0) {
/* 7792 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Value_must_be_between_0_and_getMaxRows()_66"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7798 */     this.fetchSize = rows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFirstCharOfQuery(char c) {
/* 7809 */     this.firstCharOfQuery = c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setNextResultSet(ResultSetInternalMethods nextResultSet) {
/* 7820 */     this.nextResultSet = nextResultSet;
/*      */   }
/*      */   
/*      */   public void setOwningStatement(StatementImpl owningStatement) {
/* 7824 */     this.owningStatement = owningStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setResultSetConcurrency(int concurrencyFlag) {
/* 7834 */     this.resultSetConcurrency = concurrencyFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setResultSetType(int typeFlag) {
/* 7845 */     this.resultSetType = typeFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setServerInfo(String info) {
/* 7855 */     this.serverInfo = info;
/*      */   }
/*      */   
/*      */   public void setStatementUsedForFetchingRows(PreparedStatement stmt) {
/* 7859 */     this.statementUsedForFetchingRows = stmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrapperStatement(Statement wrapperStatement) {
/* 7867 */     this.wrapperStatement = wrapperStatement;
/*      */   }
/*      */ 
/*      */   
/*      */   private void throwRangeException(String valueAsString, int columnIndex, int jdbcType) throws SQLException {
/* 7872 */     String datatype = null;
/*      */     
/* 7874 */     switch (jdbcType)
/*      */     { case -6:
/* 7876 */         datatype = "TINYINT";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 7903 */         throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 5: datatype = "SMALLINT"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 4: datatype = "INTEGER"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case -5: datatype = "BIGINT"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 7: datatype = "REAL"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 6: datatype = "FLOAT"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 8: datatype = "DOUBLE"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());case 3: datatype = "DECIMAL"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor()); }  datatype = " (JDBC type '" + jdbcType + "')"; throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 7914 */     if (this.reallyResult) {
/* 7915 */       return super.toString();
/*      */     }
/*      */     
/* 7918 */     return "Result set representing update count of " + this.updateCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateArray(int arg0, Array arg1) throws SQLException {
/* 7925 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateArray(String arg0, Array arg1) throws SQLException {
/* 7932 */     throw SQLError.notImplemented();
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
/*      */   public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
/* 7956 */     throw new NotUpdatable();
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
/*      */   public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
/* 7978 */     updateAsciiStream(findColumn(columnName), x, length);
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
/*      */   public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
/* 7999 */     throw new NotUpdatable();
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
/*      */   public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
/* 8018 */     updateBigDecimal(findColumn(columnName), x);
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
/*      */   public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
/* 8042 */     throw new NotUpdatable();
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
/*      */   public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
/* 8064 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBlob(int arg0, Blob arg1) throws SQLException {
/* 8071 */     throw new NotUpdatable();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBlob(String arg0, Blob arg1) throws SQLException {
/* 8078 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(int columnIndex, boolean x) throws SQLException {
/* 8098 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(String columnName, boolean x) throws SQLException {
/* 8116 */     updateBoolean(findColumn(columnName), x);
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
/*      */   public void updateByte(int columnIndex, byte x) throws SQLException {
/* 8136 */     throw new NotUpdatable();
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
/*      */   public void updateByte(String columnName, byte x) throws SQLException {
/* 8154 */     updateByte(findColumn(columnName), x);
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
/*      */   public void updateBytes(int columnIndex, byte[] x) throws SQLException {
/* 8174 */     throw new NotUpdatable();
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
/*      */   public void updateBytes(String columnName, byte[] x) throws SQLException {
/* 8192 */     updateBytes(findColumn(columnName), x);
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
/*      */   public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
/* 8216 */     throw new NotUpdatable();
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
/*      */   public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
/* 8238 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateClob(int arg0, Clob arg1) throws SQLException {
/* 8245 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateClob(String columnName, Clob clob) throws SQLException {
/* 8253 */     updateClob(findColumn(columnName), clob);
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
/*      */   public void updateDate(int columnIndex, Date x) throws SQLException {
/* 8274 */     throw new NotUpdatable();
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
/*      */   public void updateDate(String columnName, Date x) throws SQLException {
/* 8293 */     updateDate(findColumn(columnName), x);
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
/*      */   public void updateDouble(int columnIndex, double x) throws SQLException {
/* 8313 */     throw new NotUpdatable();
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
/*      */   public void updateDouble(String columnName, double x) throws SQLException {
/* 8331 */     updateDouble(findColumn(columnName), x);
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
/*      */   public void updateFloat(int columnIndex, float x) throws SQLException {
/* 8351 */     throw new NotUpdatable();
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
/*      */   public void updateFloat(String columnName, float x) throws SQLException {
/* 8369 */     updateFloat(findColumn(columnName), x);
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
/*      */   public void updateInt(int columnIndex, int x) throws SQLException {
/* 8389 */     throw new NotUpdatable();
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
/*      */   public void updateInt(String columnName, int x) throws SQLException {
/* 8407 */     updateInt(findColumn(columnName), x);
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
/*      */   public void updateLong(int columnIndex, long x) throws SQLException {
/* 8427 */     throw new NotUpdatable();
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
/*      */   public void updateLong(String columnName, long x) throws SQLException {
/* 8445 */     updateLong(findColumn(columnName), x);
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
/*      */   public void updateNull(int columnIndex) throws SQLException {
/* 8463 */     throw new NotUpdatable();
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
/*      */   public void updateNull(String columnName) throws SQLException {
/* 8479 */     updateNull(findColumn(columnName));
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
/*      */   public void updateObject(int columnIndex, Object x) throws SQLException {
/* 8499 */     throw new NotUpdatable();
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
/*      */   public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
/* 8524 */     throw new NotUpdatable();
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
/*      */   public void updateObject(String columnName, Object x) throws SQLException {
/* 8542 */     updateObject(findColumn(columnName), x);
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
/*      */   public void updateObject(String columnName, Object x, int scale) throws SQLException {
/* 8565 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateRef(int arg0, Ref arg1) throws SQLException {
/* 8572 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateRef(String arg0, Ref arg1) throws SQLException {
/* 8579 */     throw SQLError.notImplemented();
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
/*      */   public void updateRow() throws SQLException {
/* 8593 */     throw new NotUpdatable();
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
/*      */   public void updateShort(int columnIndex, short x) throws SQLException {
/* 8613 */     throw new NotUpdatable();
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
/*      */   public void updateShort(String columnName, short x) throws SQLException {
/* 8631 */     updateShort(findColumn(columnName), x);
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
/*      */   public void updateString(int columnIndex, String x) throws SQLException {
/* 8651 */     throw new NotUpdatable();
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
/*      */   public void updateString(String columnName, String x) throws SQLException {
/* 8669 */     updateString(findColumn(columnName), x);
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
/*      */   public void updateTime(int columnIndex, Time x) throws SQLException {
/* 8690 */     throw new NotUpdatable();
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
/*      */   public void updateTime(String columnName, Time x) throws SQLException {
/* 8709 */     updateTime(findColumn(columnName), x);
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
/*      */   public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
/* 8731 */     throw new NotUpdatable();
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
/*      */   public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
/* 8750 */     updateTimestamp(findColumn(columnName), x);
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
/*      */   public boolean wasNull() throws SQLException {
/* 8765 */     return this.wasNullFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Calendar getGmtCalendar() {
/* 8772 */     if (this.gmtCalendar == null) {
/* 8773 */       this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */     
/* 8776 */     return this.gmtCalendar;
/*      */   }
/*      */   
/*      */   protected ExceptionInterceptor getExceptionInterceptor() {
/* 8780 */     return this.exceptionInterceptor;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ResultSetImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */