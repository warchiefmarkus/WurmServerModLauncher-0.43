/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.LogFactory;
/*      */ import com.mysql.jdbc.log.NullLogger;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandlerFactory;
/*      */ import com.mysql.jdbc.util.LRUCache;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Stack;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
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
/*      */ public class ConnectionImpl
/*      */   extends ConnectionPropertiesImpl
/*      */   implements Connection
/*      */ {
/*      */   private static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
/*      */   
/*      */   class ExceptionInterceptorChain
/*      */     implements ExceptionInterceptor
/*      */   {
/*      */     List interceptors;
/*      */     private final ConnectionImpl this$0;
/*      */     
/*      */     ExceptionInterceptorChain(ConnectionImpl this$0, String interceptorClasses) throws SQLException {
/*   86 */       this.this$0 = this$0;
/*   87 */       this.interceptors = Util.loadExtensions(this$0, this$0.props, interceptorClasses, "Connection.BadExceptionInterceptor", this);
/*      */     }
/*      */     
/*      */     public SQLException interceptException(SQLException sqlEx) {
/*   91 */       if (this.interceptors != null) {
/*   92 */         Iterator iter = this.interceptors.iterator();
/*      */         
/*   94 */         while (iter.hasNext()) {
/*   95 */           sqlEx = ((ExceptionInterceptor)iter.next()).interceptException(sqlEx);
/*      */         }
/*      */       } 
/*      */       
/*   99 */       return sqlEx;
/*      */     }
/*      */     
/*      */     public void destroy() {
/*  103 */       if (this.interceptors != null) {
/*  104 */         Iterator iter = this.interceptors.iterator();
/*      */         
/*  106 */         while (iter.hasNext()) {
/*  107 */           ((ExceptionInterceptor)iter.next()).destroy();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void init(Connection conn, Properties props) throws SQLException {
/*  114 */       if (this.interceptors != null) {
/*  115 */         Iterator iter = this.interceptors.iterator();
/*      */         
/*  117 */         while (iter.hasNext()) {
/*  118 */           ((ExceptionInterceptor)iter.next()).init(conn, props);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class CompoundCacheKey
/*      */   {
/*      */     String componentOne;
/*      */     
/*      */     String componentTwo;
/*      */     
/*      */     int hashCode;
/*      */     
/*      */     private final ConnectionImpl this$0;
/*      */     
/*      */     CompoundCacheKey(ConnectionImpl this$0, String partOne, String partTwo) {
/*  136 */       this.this$0 = this$0;
/*  137 */       this.componentOne = partOne;
/*  138 */       this.componentTwo = partTwo;
/*      */ 
/*      */ 
/*      */       
/*  142 */       this.hashCode = (((this.componentOne != null) ? this.componentOne : "") + this.componentTwo).hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  152 */       if (obj instanceof CompoundCacheKey) {
/*  153 */         CompoundCacheKey another = (CompoundCacheKey)obj;
/*      */         
/*  155 */         boolean firstPartEqual = false;
/*      */         
/*  157 */         if (this.componentOne == null) {
/*  158 */           firstPartEqual = (another.componentOne == null);
/*      */         } else {
/*  160 */           firstPartEqual = this.componentOne.equals(another.componentOne);
/*      */         } 
/*      */ 
/*      */         
/*  164 */         return (firstPartEqual && this.componentTwo.equals(another.componentTwo));
/*      */       } 
/*      */ 
/*      */       
/*  168 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  177 */       return this.hashCode;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  185 */   private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map charsetMap;
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int HISTOGRAM_BUCKETS = 20;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOGGER_INSTANCE_NAME = "MySQL";
/*      */ 
/*      */ 
/*      */   
/*  205 */   private static Map mapTransIsolationNameToValue = null;
/*      */ 
/*      */   
/*  208 */   private static final Log NULL_LOGGER = (Log)new NullLogger("MySQL");
/*      */   
/*      */   private static Map roundRobinStatsMap;
/*      */   
/*  212 */   private static final Map serverCollationByUrl = new HashMap();
/*      */   
/*  214 */   private static final Map serverConfigByUrl = new HashMap();
/*      */   
/*      */   private long queryTimeCount;
/*      */   
/*      */   private double queryTimeSum;
/*      */   
/*      */   private double queryTimeSumSquares;
/*      */   
/*      */   private double queryTimeMean;
/*      */   
/*      */   private static Timer cancelTimer;
/*      */   
/*      */   private List connectionLifecycleInterceptors;
/*      */   private static final Constructor JDBC_4_CONNECTION_CTOR;
/*      */   private static final int DEFAULT_RESULT_SET_TYPE = 1003;
/*      */   private static final int DEFAULT_RESULT_SET_CONCURRENCY = 1007;
/*      */   
/*      */   static {
/*  232 */     mapTransIsolationNameToValue = new HashMap(8);
/*  233 */     mapTransIsolationNameToValue.put("READ-UNCOMMITED", Constants.integerValueOf(1));
/*      */     
/*  235 */     mapTransIsolationNameToValue.put("READ-UNCOMMITTED", Constants.integerValueOf(1));
/*      */     
/*  237 */     mapTransIsolationNameToValue.put("READ-COMMITTED", Constants.integerValueOf(2));
/*      */     
/*  239 */     mapTransIsolationNameToValue.put("REPEATABLE-READ", Constants.integerValueOf(4));
/*      */     
/*  241 */     mapTransIsolationNameToValue.put("SERIALIZABLE", Constants.integerValueOf(8));
/*      */ 
/*      */     
/*  244 */     boolean createdNamedTimer = false;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  249 */       Constructor ctr = Timer.class.getConstructor(new Class[] { String.class, boolean.class });
/*      */       
/*  251 */       cancelTimer = ctr.newInstance(new Object[] { "MySQL Statement Cancellation Timer", Boolean.TRUE });
/*  252 */       createdNamedTimer = true;
/*  253 */     } catch (Throwable t) {
/*  254 */       createdNamedTimer = false;
/*      */     } 
/*      */     
/*  257 */     if (!createdNamedTimer) {
/*  258 */       cancelTimer = new Timer(true);
/*      */     }
/*      */     
/*  261 */     if (Util.isJdbc4()) {
/*      */       try {
/*  263 */         JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4Connection").getConstructor(new Class[] { String.class, int.class, Properties.class, String.class, String.class });
/*      */ 
/*      */       
/*      */       }
/*  267 */       catch (SecurityException e) {
/*  268 */         throw new RuntimeException(e);
/*  269 */       } catch (NoSuchMethodException e) {
/*  270 */         throw new RuntimeException(e);
/*  271 */       } catch (ClassNotFoundException e) {
/*  272 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*  275 */       JDBC_4_CONNECTION_CTOR = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend, ExceptionInterceptor interceptor) {
/*  281 */     String origMessage = sqlEx.getMessage();
/*  282 */     String sqlState = sqlEx.getSQLState();
/*  283 */     int vendorErrorCode = sqlEx.getErrorCode();
/*      */     
/*  285 */     StringBuffer messageBuf = new StringBuffer(origMessage.length() + messageToAppend.length());
/*      */     
/*  287 */     messageBuf.append(origMessage);
/*  288 */     messageBuf.append(messageToAppend);
/*      */     
/*  290 */     SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf.toString(), sqlState, vendorErrorCode, interceptor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  300 */       Method getStackTraceMethod = null;
/*  301 */       Method setStackTraceMethod = null;
/*  302 */       Object theStackTraceAsObject = null;
/*      */       
/*  304 */       Class stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
/*      */       
/*  306 */       Class stackTraceElementArrayClass = Array.newInstance(stackTraceElementClass, new int[] { 0 }).getClass();
/*      */ 
/*      */       
/*  309 */       getStackTraceMethod = Throwable.class.getMethod("getStackTrace", new Class[0]);
/*      */ 
/*      */       
/*  312 */       setStackTraceMethod = Throwable.class.getMethod("setStackTrace", new Class[] { stackTraceElementArrayClass });
/*      */ 
/*      */       
/*  315 */       if (getStackTraceMethod != null && setStackTraceMethod != null) {
/*  316 */         theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx, new Object[0]);
/*      */         
/*  318 */         setStackTraceMethod.invoke(sqlExceptionWithNewMessage, new Object[] { theStackTraceAsObject });
/*      */       }
/*      */     
/*  321 */     } catch (NoClassDefFoundError noClassDefFound) {
/*      */     
/*  323 */     } catch (NoSuchMethodException noSuchMethodEx) {
/*      */     
/*  325 */     } catch (Throwable catchAll) {}
/*      */ 
/*      */ 
/*      */     
/*  329 */     return sqlExceptionWithNewMessage;
/*      */   }
/*      */   
/*      */   protected static Timer getCancelTimer() {
/*  333 */     return cancelTimer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Connection getInstance(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
/*  347 */     if (!Util.isJdbc4()) {
/*  348 */       return new ConnectionImpl(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
/*      */     }
/*      */ 
/*      */     
/*  352 */     return (Connection)Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[] { hostToConnectTo, Constants.integerValueOf(portToConnectTo), info, databaseToConnectTo, url }, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static synchronized int getNextRoundRobinHostIndex(String url, List hostList) {
/*  363 */     int indexRange = hostList.size();
/*      */     
/*  365 */     int index = (int)(Math.random() * indexRange);
/*      */     
/*  367 */     return index;
/*      */   }
/*      */   
/*      */   private static boolean nullSafeCompare(String s1, String s2) {
/*  371 */     if (s1 == null && s2 == null) {
/*  372 */       return true;
/*      */     }
/*      */     
/*  375 */     if (s1 == null && s2 != null) {
/*  376 */       return false;
/*      */     }
/*      */     
/*  379 */     return s1.equals(s2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean autoCommit = true;
/*      */ 
/*      */   
/*      */   private Map cachedPreparedStatementParams;
/*      */ 
/*      */   
/*  391 */   private String characterSetMetadata = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  397 */   private String characterSetResultsOnServer = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  404 */   private Map charsetConverterMap = new HashMap(CharsetMapping.getNumberOfCharsetsConfigured());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map charsetToNumBytesMap;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  414 */   private long connectionCreationTimeMillis = 0L;
/*      */ 
/*      */   
/*      */   private long connectionId;
/*      */ 
/*      */   
/*  420 */   private String database = null;
/*      */ 
/*      */   
/*  423 */   private DatabaseMetaData dbmd = null;
/*      */ 
/*      */   
/*      */   private TimeZone defaultTimeZone;
/*      */ 
/*      */   
/*      */   private ProfilerEventHandler eventSink;
/*      */ 
/*      */   
/*      */   private boolean executingFailoverReconnect = false;
/*      */ 
/*      */   
/*      */   private boolean failedOver = false;
/*      */ 
/*      */   
/*      */   private Throwable forceClosedReason;
/*      */ 
/*      */   
/*      */   private Throwable forcedClosedLocation;
/*      */ 
/*      */   
/*      */   private boolean hasIsolationLevels = false;
/*      */   
/*      */   private boolean hasQuotedIdentifiers = false;
/*      */   
/*  448 */   private String host = null;
/*      */ 
/*      */   
/*  451 */   private List hostList = null;
/*      */ 
/*      */   
/*  454 */   private int hostListSize = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  460 */   private String[] indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;
/*      */ 
/*      */   
/*  463 */   private MysqlIO io = null;
/*      */ 
/*      */   
/*      */   private boolean isClientTzUTC = false;
/*      */ 
/*      */   
/*      */   private boolean isClosed = true;
/*      */ 
/*      */   
/*      */   private boolean isInGlobalTx = false;
/*      */ 
/*      */   
/*      */   private boolean isRunningOnJDK13 = false;
/*      */   
/*  477 */   private int isolationLevel = 2;
/*      */ 
/*      */   
/*      */   private boolean isServerTzUTC = false;
/*      */   
/*  482 */   private long lastQueryFinishedTime = 0L;
/*      */ 
/*      */   
/*  485 */   private Log log = NULL_LOGGER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  491 */   private long longestQueryTimeMs = 0L;
/*      */ 
/*      */   
/*      */   private boolean lowerCaseTableNames = false;
/*      */ 
/*      */   
/*  497 */   private long masterFailTimeMillis = 0L;
/*      */   
/*  499 */   private long maximumNumberTablesAccessed = 0L;
/*      */ 
/*      */   
/*      */   private boolean maxRowsChanged = false;
/*      */ 
/*      */   
/*      */   private long metricsLastReportedMs;
/*      */   
/*  507 */   private long minimumNumberTablesAccessed = Long.MAX_VALUE;
/*      */ 
/*      */   
/*  510 */   private final Object mutex = new Object();
/*      */ 
/*      */   
/*  513 */   private String myURL = null;
/*      */ 
/*      */   
/*      */   private boolean needsPing = false;
/*      */   
/*  518 */   private int netBufferLength = 16384;
/*      */   
/*      */   private boolean noBackslashEscapes = false;
/*      */   
/*  522 */   private long numberOfPreparedExecutes = 0L;
/*      */   
/*  524 */   private long numberOfPrepares = 0L;
/*      */   
/*  526 */   private long numberOfQueriesIssued = 0L;
/*      */   
/*  528 */   private long numberOfResultSetsCreated = 0L;
/*      */   
/*      */   private long[] numTablesMetricsHistBreakpoints;
/*      */   
/*      */   private int[] numTablesMetricsHistCounts;
/*      */   
/*  534 */   private long[] oldHistBreakpoints = null;
/*      */   
/*  536 */   private int[] oldHistCounts = null;
/*      */ 
/*      */   
/*      */   private Map openStatements;
/*      */ 
/*      */   
/*      */   private LRUCache parsedCallableStatementCache;
/*      */   
/*      */   private boolean parserKnowsUnicode = false;
/*      */   
/*  546 */   private String password = null;
/*      */ 
/*      */   
/*      */   private long[] perfMetricsHistBreakpoints;
/*      */ 
/*      */   
/*      */   private int[] perfMetricsHistCounts;
/*      */   
/*      */   private Throwable pointOfOrigin;
/*      */   
/*  556 */   private int port = 3306;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preferSlaveDuringFailover = false;
/*      */ 
/*      */ 
/*      */   
/*  565 */   protected Properties props = null;
/*      */ 
/*      */   
/*  568 */   private long queriesIssuedFailedOver = 0L;
/*      */ 
/*      */   
/*      */   private boolean readInfoMsg = false;
/*      */ 
/*      */   
/*      */   private boolean readOnly = false;
/*      */ 
/*      */   
/*      */   protected LRUCache resultSetMetadataCache;
/*      */ 
/*      */   
/*  580 */   private TimeZone serverTimezoneTZ = null;
/*      */ 
/*      */   
/*  583 */   private Map serverVariables = null;
/*      */   
/*  585 */   private long shortestQueryTimeMs = Long.MAX_VALUE;
/*      */ 
/*      */   
/*      */   private Map statementsUsingMaxRows;
/*      */   
/*  590 */   private double totalQueryTimeMs = 0.0D;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean transactionsSupported = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private Map typeMap;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useAnsiQuotes = false;
/*      */ 
/*      */   
/*  605 */   private String user = null;
/*      */ 
/*      */   
/*      */   private boolean useServerPreparedStmts = false;
/*      */ 
/*      */   
/*      */   private LRUCache serverSideStatementCheckCache;
/*      */ 
/*      */   
/*      */   private LRUCache serverSideStatementCache;
/*      */ 
/*      */   
/*      */   private Calendar sessionCalendar;
/*      */   
/*      */   private Calendar utcCalendar;
/*      */   
/*      */   private String origHostToConnectTo;
/*      */   
/*      */   private int origPortToConnectTo;
/*      */   
/*      */   private String origDatabaseToConnectTo;
/*      */   
/*  627 */   private String errorMessageEncoding = "Cp1252";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean usePlatformCharsetConverters;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasTriedMasterFlag = false;
/*      */ 
/*      */ 
/*      */   
/*  640 */   private String statementComment = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean storesLowerCaseTableName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List statementInterceptors;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean requiresEscapingEncoder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean usingCachedConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int autoIncrementIncrement;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeStatementInterceptors() throws SQLException {
/*  825 */     this.statementInterceptors = Util.loadExtensions(this, this.props, getStatementInterceptors(), "MysqlIo.BadStatementInterceptor", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected List getStatementInterceptorsInstances() {
/*  831 */     return this.statementInterceptors;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToHistogram(int[] histogramCounts, long[] histogramBreakpoints, long value, int numberOfTimes, long currentLowerBound, long currentUpperBound) {
/*  837 */     if (histogramCounts == null) {
/*  838 */       createInitialHistogram(histogramBreakpoints, currentLowerBound, currentUpperBound);
/*      */     } else {
/*      */       
/*  841 */       for (int i = 0; i < 20; i++) {
/*  842 */         if (histogramBreakpoints[i] >= value) {
/*  843 */           histogramCounts[i] = histogramCounts[i] + numberOfTimes;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addToPerformanceHistogram(long value, int numberOfTimes) {
/*  852 */     checkAndCreatePerformanceHistogram();
/*      */     
/*  854 */     addToHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, value, numberOfTimes, (this.shortestQueryTimeMs == Long.MAX_VALUE) ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToTablesAccessedHistogram(long value, int numberOfTimes) {
/*  861 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/*  863 */     addToHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, value, numberOfTimes, (this.minimumNumberTablesAccessed == Long.MAX_VALUE) ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildCollationMapping() throws SQLException {
/*  878 */     if (versionMeetsMinimum(4, 1, 0)) {
/*      */       
/*  880 */       TreeMap sortedCollationMap = null;
/*      */       
/*  882 */       if (getCacheServerConfiguration()) {
/*  883 */         synchronized (serverConfigByUrl) {
/*  884 */           sortedCollationMap = (TreeMap)serverCollationByUrl.get(getURL());
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  889 */       Statement stmt = null;
/*  890 */       ResultSet results = null;
/*      */       
/*      */       try {
/*  893 */         if (sortedCollationMap == null) {
/*  894 */           sortedCollationMap = new TreeMap();
/*      */           
/*  896 */           stmt = getMetadataSafeStatement();
/*      */           
/*  898 */           results = stmt.executeQuery("SHOW COLLATION");
/*      */ 
/*      */           
/*  901 */           while (results.next()) {
/*  902 */             String charsetName = results.getString(2);
/*  903 */             Integer charsetIndex = Constants.integerValueOf(results.getInt(3));
/*      */             
/*  905 */             sortedCollationMap.put(charsetIndex, charsetName);
/*      */           } 
/*      */           
/*  908 */           if (getCacheServerConfiguration()) {
/*  909 */             synchronized (serverConfigByUrl) {
/*  910 */               serverCollationByUrl.put(getURL(), sortedCollationMap);
/*      */             } 
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  918 */         int highestIndex = ((Integer)sortedCollationMap.lastKey()).intValue();
/*      */ 
/*      */         
/*  921 */         if (CharsetMapping.INDEX_TO_CHARSET.length > highestIndex) {
/*  922 */           highestIndex = CharsetMapping.INDEX_TO_CHARSET.length;
/*      */         }
/*      */         
/*  925 */         this.indexToCharsetMapping = new String[highestIndex + 1];
/*      */         
/*  927 */         for (int i = 0; i < CharsetMapping.INDEX_TO_CHARSET.length; i++) {
/*  928 */           this.indexToCharsetMapping[i] = CharsetMapping.INDEX_TO_CHARSET[i];
/*      */         }
/*      */         
/*  931 */         Iterator indexIter = sortedCollationMap.entrySet().iterator();
/*  932 */         while (indexIter.hasNext()) {
/*  933 */           Map.Entry indexEntry = indexIter.next();
/*      */           
/*  935 */           String mysqlCharsetName = (String)indexEntry.getValue();
/*      */           
/*  937 */           this.indexToCharsetMapping[((Integer)indexEntry.getKey()).intValue()] = CharsetMapping.getJavaEncodingForMysqlEncoding(mysqlCharsetName, this);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  942 */       catch (SQLException e) {
/*  943 */         throw e;
/*      */       } finally {
/*  945 */         if (results != null) {
/*      */           try {
/*  947 */             results.close();
/*  948 */           } catch (SQLException sqlE) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  953 */         if (stmt != null) {
/*      */           try {
/*  955 */             stmt.close();
/*  956 */           } catch (SQLException sqlE) {}
/*      */         
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  964 */       this.indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canHandleAsServerPreparedStatement(String sql) throws SQLException {
/*  970 */     if (sql == null || sql.length() == 0) {
/*  971 */       return true;
/*      */     }
/*      */     
/*  974 */     if (!this.useServerPreparedStmts) {
/*  975 */       return false;
/*      */     }
/*      */     
/*  978 */     if (getCachePreparedStatements()) {
/*  979 */       synchronized (this.serverSideStatementCheckCache) {
/*  980 */         Boolean flag = (Boolean)this.serverSideStatementCheckCache.get(sql);
/*      */         
/*  982 */         if (flag != null) {
/*  983 */           return flag.booleanValue();
/*      */         }
/*      */         
/*  986 */         boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);
/*      */         
/*  988 */         if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/*  989 */           this.serverSideStatementCheckCache.put(sql, canHandle ? Boolean.TRUE : Boolean.FALSE);
/*      */         }
/*      */ 
/*      */         
/*  993 */         return canHandle;
/*      */       } 
/*      */     }
/*      */     
/*  997 */     return canHandleAsServerPreparedStatementNoCache(sql);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean canHandleAsServerPreparedStatementNoCache(String sql) throws SQLException {
/* 1004 */     if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
/* 1005 */       return false;
/*      */     }
/*      */     
/* 1008 */     boolean canHandleAsStatement = true;
/*      */     
/* 1010 */     if (!versionMeetsMinimum(5, 0, 7) && (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "DELETE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "INSERT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "UPDATE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "REPLACE"))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1028 */       int currentPos = 0;
/* 1029 */       int statementLength = sql.length();
/* 1030 */       int lastPosToLook = statementLength - 7;
/* 1031 */       boolean allowBackslashEscapes = !this.noBackslashEscapes;
/* 1032 */       char quoteChar = this.useAnsiQuotes ? '"' : '\'';
/* 1033 */       boolean foundLimitWithPlaceholder = false;
/*      */       
/* 1035 */       while (currentPos < lastPosToLook) {
/* 1036 */         int limitStart = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, sql, "LIMIT ", quoteChar, allowBackslashEscapes);
/*      */ 
/*      */ 
/*      */         
/* 1040 */         if (limitStart == -1) {
/*      */           break;
/*      */         }
/*      */         
/* 1044 */         currentPos = limitStart + 7;
/*      */         
/* 1046 */         while (currentPos < statementLength) {
/* 1047 */           char c = sql.charAt(currentPos);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1054 */           if (!Character.isDigit(c) && !Character.isWhitespace(c) && c != ',' && c != '?') {
/*      */             break;
/*      */           }
/*      */ 
/*      */           
/* 1059 */           if (c == '?') {
/* 1060 */             foundLimitWithPlaceholder = true;
/*      */             
/*      */             break;
/*      */           } 
/* 1064 */           currentPos++;
/*      */         } 
/*      */       } 
/*      */       
/* 1068 */       canHandleAsStatement = !foundLimitWithPlaceholder;
/* 1069 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
/* 1070 */       canHandleAsStatement = false;
/* 1071 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "DO")) {
/* 1072 */       canHandleAsStatement = false;
/* 1073 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
/* 1074 */       canHandleAsStatement = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1079 */     return canHandleAsStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void changeUser(String userName, String newPassword) throws SQLException {
/* 1097 */     if (userName == null || userName.equals("")) {
/* 1098 */       userName = "";
/*      */     }
/*      */     
/* 1101 */     if (newPassword == null) {
/* 1102 */       newPassword = "";
/*      */     }
/*      */     
/* 1105 */     this.io.changeUser(userName, newPassword, this.database);
/* 1106 */     this.user = userName;
/* 1107 */     this.password = newPassword;
/*      */     
/* 1109 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 1110 */       configureClientCharacterSet(true);
/*      */     }
/*      */     
/* 1113 */     setupServerForTruncationChecks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean characterSetNamesMatches(String mysqlEncodingName) {
/* 1120 */     return (mysqlEncodingName != null && mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_client")) && mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_connection")));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkAndCreatePerformanceHistogram() {
/* 1126 */     if (this.perfMetricsHistCounts == null) {
/* 1127 */       this.perfMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1130 */     if (this.perfMetricsHistBreakpoints == null) {
/* 1131 */       this.perfMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkAndCreateTablesAccessedHistogram() {
/* 1136 */     if (this.numTablesMetricsHistCounts == null) {
/* 1137 */       this.numTablesMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1140 */     if (this.numTablesMetricsHistBreakpoints == null) {
/* 1141 */       this.numTablesMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkClosed() throws SQLException {
/* 1146 */     if (this.isClosed) {
/* 1147 */       throwConnectionClosedException();
/*      */     }
/*      */   }
/*      */   
/*      */   private void throwConnectionClosedException() throws SQLException {
/* 1152 */     StringBuffer messageBuf = new StringBuffer("No operations allowed after connection closed.");
/*      */ 
/*      */     
/* 1155 */     if (this.forcedClosedLocation != null || this.forceClosedReason != null) {
/* 1156 */       messageBuf.append("Connection was implicitly closed by the driver.");
/*      */     }
/*      */ 
/*      */     
/* 1160 */     SQLException ex = SQLError.createSQLException(messageBuf.toString(), "08003", getExceptionInterceptor());
/*      */ 
/*      */     
/* 1163 */     if (this.forceClosedReason != null) {
/* 1164 */       ex.initCause(this.forceClosedReason);
/*      */     }
/*      */     
/* 1167 */     throw ex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkServerEncoding() throws SQLException {
/* 1178 */     if (getUseUnicode() && getEncoding() != null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1183 */     String serverEncoding = (String)this.serverVariables.get("character_set");
/*      */ 
/*      */     
/* 1186 */     if (serverEncoding == null)
/*      */     {
/* 1188 */       serverEncoding = (String)this.serverVariables.get("character_set_server");
/*      */     }
/*      */ 
/*      */     
/* 1192 */     String mappedServerEncoding = null;
/*      */     
/* 1194 */     if (serverEncoding != null) {
/* 1195 */       mappedServerEncoding = CharsetMapping.getJavaEncodingForMysqlEncoding(serverEncoding.toUpperCase(Locale.ENGLISH), this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1203 */     if (!getUseUnicode() && mappedServerEncoding != null) {
/* 1204 */       SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);
/*      */       
/* 1206 */       if (converter != null) {
/* 1207 */         setUseUnicode(true);
/* 1208 */         setEncoding(mappedServerEncoding);
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1218 */     if (serverEncoding != null) {
/* 1219 */       if (mappedServerEncoding == null)
/*      */       {
/*      */         
/* 1222 */         if (Character.isLowerCase(serverEncoding.charAt(0))) {
/* 1223 */           char[] ach = serverEncoding.toCharArray();
/* 1224 */           ach[0] = Character.toUpperCase(serverEncoding.charAt(0));
/* 1225 */           setEncoding(new String(ach));
/*      */         } 
/*      */       }
/*      */       
/* 1229 */       if (mappedServerEncoding == null) {
/* 1230 */         throw SQLError.createSQLException("Unknown character encoding on server '" + serverEncoding + "', use 'characterEncoding=' property " + " to provide correct mapping", "01S00", getExceptionInterceptor());
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
/*      */       try {
/* 1242 */         "abc".getBytes(mappedServerEncoding);
/* 1243 */         setEncoding(mappedServerEncoding);
/* 1244 */         setUseUnicode(true);
/* 1245 */       } catch (UnsupportedEncodingException UE) {
/* 1246 */         throw SQLError.createSQLException("The driver can not map the character encoding '" + getEncoding() + "' that your server is using " + "to a character encoding your JVM understands. You " + "can specify this mapping manually by adding \"useUnicode=true\" " + "as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" " + "to your JDBC URL.", "0S100", getExceptionInterceptor());
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
/*      */   private void checkTransactionIsolationLevel() throws SQLException {
/* 1266 */     String txIsolationName = null;
/*      */     
/* 1268 */     if (versionMeetsMinimum(4, 0, 3)) {
/* 1269 */       txIsolationName = "tx_isolation";
/*      */     } else {
/* 1271 */       txIsolationName = "transaction_isolation";
/*      */     } 
/*      */     
/* 1274 */     String s = (String)this.serverVariables.get(txIsolationName);
/*      */     
/* 1276 */     if (s != null) {
/* 1277 */       Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */       
/* 1279 */       if (intTI != null) {
/* 1280 */         this.isolationLevel = intTI.intValue();
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
/*      */   protected void abortInternal() throws SQLException {
/* 1292 */     if (this.io != null) {
/*      */       try {
/* 1294 */         this.io.forceClose();
/* 1295 */       } catch (Throwable t) {}
/*      */ 
/*      */       
/* 1298 */       this.io = null;
/*      */     } 
/*      */     
/* 1301 */     this.isClosed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void cleanup(Throwable whyCleanedUp) {
/*      */     try {
/* 1314 */       if (this.io != null && !isClosed()) {
/* 1315 */         realClose(false, false, false, whyCleanedUp);
/* 1316 */       } else if (this.io != null) {
/* 1317 */         this.io.forceClose();
/*      */       } 
/* 1319 */     } catch (SQLException sqlEx) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1324 */     this.isClosed = true;
/*      */   }
/*      */   
/*      */   public void clearHasTriedMaster() {
/* 1328 */     this.hasTriedMasterFlag = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
/* 1353 */     return clientPrepareStatement(sql, 1003, 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
/* 1363 */     PreparedStatement pStmt = clientPrepareStatement(sql);
/*      */     
/* 1365 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyIndex == 1));
/*      */ 
/*      */     
/* 1368 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 1386 */     return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, boolean processEscapeCodesIfNeeded) throws SQLException {
/* 1394 */     checkClosed();
/*      */     
/* 1396 */     String nativeSql = (processEscapeCodesIfNeeded && getProcessEscapeCodesForPrepStmts()) ? nativeSQL(sql) : sql;
/*      */     
/* 1398 */     PreparedStatement pStmt = null;
/*      */     
/* 1400 */     if (getCachePreparedStatements()) {
/* 1401 */       synchronized (this.cachedPreparedStatementParams) {
/* 1402 */         PreparedStatement.ParseInfo pStmtInfo = (PreparedStatement.ParseInfo)this.cachedPreparedStatementParams.get(nativeSql);
/*      */ 
/*      */         
/* 1405 */         if (pStmtInfo == null) {
/* 1406 */           pStmt = PreparedStatement.getInstance(this, nativeSql, this.database);
/*      */ 
/*      */           
/* 1409 */           PreparedStatement.ParseInfo parseInfo = pStmt.getParseInfo();
/*      */           
/* 1411 */           if (parseInfo.statementLength < getPreparedStatementCacheSqlLimit()) {
/* 1412 */             if (this.cachedPreparedStatementParams.size() >= getPreparedStatementCacheSize()) {
/* 1413 */               Iterator oldestIter = this.cachedPreparedStatementParams.keySet().iterator();
/*      */               
/* 1415 */               long lruTime = Long.MAX_VALUE;
/* 1416 */               String oldestSql = null;
/*      */               
/* 1418 */               while (oldestIter.hasNext()) {
/* 1419 */                 String sqlKey = oldestIter.next();
/* 1420 */                 PreparedStatement.ParseInfo lruInfo = (PreparedStatement.ParseInfo)this.cachedPreparedStatementParams.get(sqlKey);
/*      */ 
/*      */                 
/* 1423 */                 if (lruInfo.lastUsed < lruTime) {
/* 1424 */                   lruTime = lruInfo.lastUsed;
/* 1425 */                   oldestSql = sqlKey;
/*      */                 } 
/*      */               } 
/*      */               
/* 1429 */               if (oldestSql != null) {
/* 1430 */                 this.cachedPreparedStatementParams.remove(oldestSql);
/*      */               }
/*      */             } 
/*      */ 
/*      */             
/* 1435 */             this.cachedPreparedStatementParams.put(nativeSql, pStmt.getParseInfo());
/*      */           } 
/*      */         } else {
/*      */           
/* 1439 */           pStmtInfo.lastUsed = System.currentTimeMillis();
/* 1440 */           pStmt = new PreparedStatement(this, nativeSql, this.database, pStmtInfo);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1445 */       pStmt = PreparedStatement.getInstance(this, nativeSql, this.database);
/*      */     } 
/*      */ 
/*      */     
/* 1449 */     pStmt.setResultSetType(resultSetType);
/* 1450 */     pStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 1452 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
/* 1461 */     PreparedStatement pStmt = (PreparedStatement)clientPrepareStatement(sql);
/*      */     
/* 1463 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 1467 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
/* 1475 */     PreparedStatement pStmt = (PreparedStatement)clientPrepareStatement(sql);
/*      */     
/* 1477 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 1481 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 1487 */     return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/* 1503 */     if (this.connectionLifecycleInterceptors != null) {
/* 1504 */       (new IterateBlock(this, this.connectionLifecycleInterceptors.iterator()) { private final ConnectionImpl this$0;
/*      */           void forEach(Object each) throws SQLException {
/* 1506 */             ((ConnectionLifecycleInterceptor)each).close();
/*      */           } }
/*      */         ).doForAll();
/*      */     }
/*      */     
/* 1511 */     realClose(true, true, false, (Throwable)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeAllOpenStatements() throws SQLException {
/* 1521 */     SQLException postponedException = null;
/*      */     
/* 1523 */     if (this.openStatements != null) {
/* 1524 */       List currentlyOpenStatements = new ArrayList();
/*      */ 
/*      */ 
/*      */       
/* 1528 */       Iterator iter = this.openStatements.keySet().iterator();
/* 1529 */       while (iter.hasNext()) {
/* 1530 */         currentlyOpenStatements.add(iter.next());
/*      */       }
/*      */       
/* 1533 */       int numStmts = currentlyOpenStatements.size();
/*      */       
/* 1535 */       for (int i = 0; i < numStmts; i++) {
/* 1536 */         StatementImpl stmt = currentlyOpenStatements.get(i);
/*      */         
/*      */         try {
/* 1539 */           stmt.realClose(false, true);
/* 1540 */         } catch (SQLException sqlEx) {
/* 1541 */           postponedException = sqlEx;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1546 */       if (postponedException != null) {
/* 1547 */         throw postponedException;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void closeStatement(Statement stmt) {
/* 1553 */     if (stmt != null) {
/*      */       try {
/* 1555 */         stmt.close();
/* 1556 */       } catch (SQLException sqlEx) {}
/*      */ 
/*      */ 
/*      */       
/* 1560 */       stmt = null;
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
/*      */   public void commit() throws SQLException {
/* 1579 */     synchronized (getMutex()) {
/* 1580 */       checkClosed();
/*      */       
/*      */       try {
/* 1583 */         if (this.connectionLifecycleInterceptors != null) {
/* 1584 */           IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator()) { private final ConnectionImpl this$0;
/*      */               
/*      */               void forEach(Object each) throws SQLException {
/* 1587 */                 if (!((ConnectionLifecycleInterceptor)each).commit()) {
/* 1588 */                   this.stopIterating = true;
/*      */                 }
/*      */               } }
/*      */             ;
/*      */           
/* 1593 */           iter.doForAll();
/*      */           
/* 1595 */           if (!iter.fullIteration()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1601 */         if (this.autoCommit && !getRelaxAutoCommit())
/* 1602 */           throw SQLError.createSQLException("Can't call commit when autocommit=true", getExceptionInterceptor()); 
/* 1603 */         if (this.transactionsSupported) {
/* 1604 */           if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) && 
/* 1605 */             !this.io.inTransactionOnServer()) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/* 1610 */           execSQL((StatementImpl)null, "commit", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1616 */       catch (SQLException sqlException) {
/* 1617 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 1619 */           throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1624 */         throw sqlException;
/*      */       } finally {
/* 1626 */         this.needsPing = getReconnectAtTxEnd();
/*      */       } 
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
/*      */   private void configureCharsetProperties() throws SQLException {
/* 1640 */     if (getEncoding() != null) {
/*      */       
/*      */       try {
/*      */         
/* 1644 */         String testString = "abc";
/* 1645 */         testString.getBytes(getEncoding());
/* 1646 */       } catch (UnsupportedEncodingException UE) {
/*      */         
/* 1648 */         String oldEncoding = getEncoding();
/*      */         
/* 1650 */         setEncoding(CharsetMapping.getJavaEncodingForMysqlEncoding(oldEncoding, this));
/*      */ 
/*      */         
/* 1653 */         if (getEncoding() == null) {
/* 1654 */           throw SQLError.createSQLException("Java does not support the MySQL character encoding  encoding '" + oldEncoding + "'.", "01S00", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1661 */           String testString = "abc";
/* 1662 */           testString.getBytes(getEncoding());
/* 1663 */         } catch (UnsupportedEncodingException encodingEx) {
/* 1664 */           throw SQLError.createSQLException("Unsupported character encoding '" + getEncoding() + "'.", "01S00", getExceptionInterceptor());
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
/*      */   private boolean configureClientCharacterSet(boolean dontCheckServerMatch) throws SQLException {
/* 1686 */     String realJavaEncoding = getEncoding();
/* 1687 */     boolean characterSetAlreadyConfigured = false;
/*      */     
/*      */     try {
/* 1690 */       if (versionMeetsMinimum(4, 1, 0)) {
/* 1691 */         characterSetAlreadyConfigured = true;
/*      */         
/* 1693 */         setUseUnicode(true);
/*      */         
/* 1695 */         configureCharsetProperties();
/* 1696 */         realJavaEncoding = getEncoding();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1704 */           if (this.props != null && this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null) {
/* 1705 */             this.io.serverCharsetIndex = Integer.parseInt(this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex"));
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1710 */           String serverEncodingToSet = CharsetMapping.INDEX_TO_CHARSET[this.io.serverCharsetIndex];
/*      */ 
/*      */           
/* 1713 */           if (serverEncodingToSet == null || serverEncodingToSet.length() == 0) {
/* 1714 */             if (realJavaEncoding != null) {
/*      */               
/* 1716 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 1718 */               throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1727 */           if (versionMeetsMinimum(4, 1, 0) && "ISO8859_1".equalsIgnoreCase(serverEncodingToSet))
/*      */           {
/* 1729 */             serverEncodingToSet = "Cp1252";
/*      */           }
/*      */           
/* 1732 */           setEncoding(serverEncodingToSet);
/*      */         }
/* 1734 */         catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
/* 1735 */           if (realJavaEncoding != null) {
/*      */             
/* 1737 */             setEncoding(realJavaEncoding);
/*      */           } else {
/* 1739 */             throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1747 */         if (getEncoding() == null)
/*      */         {
/* 1749 */           setEncoding("ISO8859_1");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1756 */         if (getUseUnicode()) {
/* 1757 */           if (realJavaEncoding != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1763 */             if (realJavaEncoding.equalsIgnoreCase("UTF-8") || realJavaEncoding.equalsIgnoreCase("UTF8")) {
/*      */ 
/*      */ 
/*      */               
/* 1767 */               if (!getUseOldUTF8Behavior() && (
/* 1768 */                 dontCheckServerMatch || !characterSetNamesMatches("utf8"))) {
/* 1769 */                 execSQL((StatementImpl)null, "SET NAMES utf8", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1776 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 1778 */               String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1793 */               if (mysqlEncodingName != null)
/*      */               {
/* 1795 */                 if (dontCheckServerMatch || !characterSetNamesMatches(mysqlEncodingName)) {
/* 1796 */                   execSQL((StatementImpl)null, "SET NAMES " + mysqlEncodingName, -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */                 }
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1807 */               setEncoding(realJavaEncoding);
/*      */             } 
/* 1809 */           } else if (getEncoding() != null) {
/*      */ 
/*      */ 
/*      */             
/* 1813 */             String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(getEncoding().toUpperCase(Locale.ENGLISH), this);
/*      */ 
/*      */ 
/*      */             
/* 1817 */             if (dontCheckServerMatch || !characterSetNamesMatches(mysqlEncodingName)) {
/* 1818 */               execSQL((StatementImpl)null, "SET NAMES " + mysqlEncodingName, -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1824 */             realJavaEncoding = getEncoding();
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
/* 1835 */         String onServer = null;
/* 1836 */         boolean isNullOnServer = false;
/*      */         
/* 1838 */         if (this.serverVariables != null) {
/* 1839 */           onServer = (String)this.serverVariables.get("character_set_results");
/*      */           
/* 1841 */           isNullOnServer = (onServer == null || "NULL".equalsIgnoreCase(onServer) || onServer.length() == 0);
/*      */         } 
/*      */         
/* 1844 */         if (getCharacterSetResults() == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1851 */           if (!isNullOnServer) {
/* 1852 */             execSQL((StatementImpl)null, "SET character_set_results = NULL", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1857 */             if (!this.usingCachedConfig) {
/* 1858 */               this.serverVariables.put("jdbc.local.character_set_results", null);
/*      */             }
/*      */           }
/* 1861 */           else if (!this.usingCachedConfig) {
/* 1862 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           } 
/*      */         } else {
/*      */           
/* 1866 */           String charsetResults = getCharacterSetResults();
/* 1867 */           String mysqlEncodingName = null;
/*      */           
/* 1869 */           if ("UTF-8".equalsIgnoreCase(charsetResults) || "UTF8".equalsIgnoreCase(charsetResults)) {
/*      */             
/* 1871 */             mysqlEncodingName = "utf8";
/*      */           } else {
/* 1873 */             mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), this);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1882 */           if (!mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_results"))) {
/*      */             
/* 1884 */             StringBuffer setBuf = new StringBuffer("SET character_set_results = ".length() + mysqlEncodingName.length());
/*      */ 
/*      */             
/* 1887 */             setBuf.append("SET character_set_results = ").append(mysqlEncodingName);
/*      */ 
/*      */             
/* 1890 */             execSQL((StatementImpl)null, setBuf.toString(), -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1895 */             if (!this.usingCachedConfig) {
/* 1896 */               this.serverVariables.put("jdbc.local.character_set_results", mysqlEncodingName);
/*      */             
/*      */             }
/*      */           }
/* 1900 */           else if (!this.usingCachedConfig) {
/* 1901 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1906 */         if (getConnectionCollation() != null) {
/* 1907 */           StringBuffer setBuf = new StringBuffer("SET collation_connection = ".length() + getConnectionCollation().length());
/*      */ 
/*      */           
/* 1910 */           setBuf.append("SET collation_connection = ").append(getConnectionCollation());
/*      */ 
/*      */           
/* 1913 */           execSQL((StatementImpl)null, setBuf.toString(), -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1920 */         realJavaEncoding = getEncoding();
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1928 */       setEncoding(realJavaEncoding);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1936 */       CharsetEncoder enc = Charset.forName(getEncoding()).newEncoder();
/* 1937 */       CharBuffer cbuf = CharBuffer.allocate(1);
/* 1938 */       ByteBuffer bbuf = ByteBuffer.allocate(1);
/*      */       
/* 1940 */       cbuf.put("¥");
/* 1941 */       cbuf.position(0);
/* 1942 */       enc.encode(cbuf, bbuf, true);
/* 1943 */       if (bbuf.get(0) == 92) {
/* 1944 */         this.requiresEscapingEncoder = true;
/*      */       } else {
/* 1946 */         cbuf.clear();
/* 1947 */         bbuf.clear();
/*      */         
/* 1949 */         cbuf.put("₩");
/* 1950 */         cbuf.position(0);
/* 1951 */         enc.encode(cbuf, bbuf, true);
/* 1952 */         if (bbuf.get(0) == 92) {
/* 1953 */           this.requiresEscapingEncoder = true;
/*      */         }
/*      */       } 
/* 1956 */     } catch (UnsupportedCharsetException ucex) {
/*      */       
/*      */       try {
/* 1959 */         byte[] bbuf = (new String("¥")).getBytes(getEncoding());
/* 1960 */         if (bbuf[0] == 92) {
/* 1961 */           this.requiresEscapingEncoder = true;
/*      */         } else {
/* 1963 */           bbuf = (new String("₩")).getBytes(getEncoding());
/* 1964 */           if (bbuf[0] == 92) {
/* 1965 */             this.requiresEscapingEncoder = true;
/*      */           }
/*      */         } 
/* 1968 */       } catch (UnsupportedEncodingException ueex) {
/* 1969 */         throw SQLError.createSQLException("Unable to use encoding: " + getEncoding(), "S1000", ueex, getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1975 */     return characterSetAlreadyConfigured;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void configureTimezone() throws SQLException {
/* 1986 */     String configuredTimeZoneOnServer = (String)this.serverVariables.get("timezone");
/*      */ 
/*      */     
/* 1989 */     if (configuredTimeZoneOnServer == null) {
/* 1990 */       configuredTimeZoneOnServer = (String)this.serverVariables.get("time_zone");
/*      */ 
/*      */       
/* 1993 */       if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
/* 1994 */         configuredTimeZoneOnServer = (String)this.serverVariables.get("system_time_zone");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1999 */     String canoncicalTimezone = getServerTimezone();
/*      */     
/* 2001 */     if ((getUseTimezone() || !getUseLegacyDatetimeCode()) && configuredTimeZoneOnServer != null) {
/*      */       
/* 2003 */       if (canoncicalTimezone == null || StringUtils.isEmptyOrWhitespaceOnly(canoncicalTimezone)) {
/*      */         try {
/* 2005 */           canoncicalTimezone = TimeUtil.getCanoncialTimezone(configuredTimeZoneOnServer, getExceptionInterceptor());
/*      */ 
/*      */           
/* 2008 */           if (canoncicalTimezone == null) {
/* 2009 */             throw SQLError.createSQLException("Can't map timezone '" + configuredTimeZoneOnServer + "' to " + " canonical timezone.", "S1009", getExceptionInterceptor());
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 2014 */         catch (IllegalArgumentException iae) {
/* 2015 */           throw SQLError.createSQLException(iae.getMessage(), "S1000", getExceptionInterceptor());
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/* 2020 */       canoncicalTimezone = getServerTimezone();
/*      */     } 
/*      */     
/* 2023 */     if (canoncicalTimezone != null && canoncicalTimezone.length() > 0) {
/* 2024 */       this.serverTimezoneTZ = TimeZone.getTimeZone(canoncicalTimezone);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2031 */       if (!canoncicalTimezone.equalsIgnoreCase("GMT") && this.serverTimezoneTZ.getID().equals("GMT"))
/*      */       {
/* 2033 */         throw SQLError.createSQLException("No timezone mapping entry for '" + canoncicalTimezone + "'", "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2038 */       if ("GMT".equalsIgnoreCase(this.serverTimezoneTZ.getID())) {
/* 2039 */         this.isServerTzUTC = true;
/*      */       } else {
/* 2041 */         this.isServerTzUTC = false;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createInitialHistogram(long[] breakpoints, long lowerBound, long upperBound) {
/* 2049 */     double bucketSize = (upperBound - lowerBound) / 20.0D * 1.25D;
/*      */     
/* 2051 */     if (bucketSize < 1.0D) {
/* 2052 */       bucketSize = 1.0D;
/*      */     }
/*      */     
/* 2055 */     for (int i = 0; i < 20; i++) {
/* 2056 */       breakpoints[i] = lowerBound;
/* 2057 */       lowerBound = (long)(lowerBound + bucketSize);
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
/*      */   protected void createNewIO(boolean isForReconnect) throws SQLException {
/* 2080 */     synchronized (this.mutex) {
/* 2081 */       Properties mergedProps = exposeAsProperties(this.props);
/*      */       
/* 2083 */       long queriesIssuedFailedOverCopy = this.queriesIssuedFailedOver;
/* 2084 */       this.queriesIssuedFailedOver = 0L;
/*      */       
/*      */       try {
/* 2087 */         if (!getHighAvailability() && !this.failedOver) {
/* 2088 */           boolean connectionGood = false;
/* 2089 */           Exception connectionNotEstablishedBecause = null;
/*      */           
/* 2091 */           int hostIndex = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2099 */           if (getRoundRobinLoadBalance()) {
/* 2100 */             hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList);
/*      */           }
/*      */ 
/*      */           
/* 2104 */           for (; hostIndex < this.hostListSize; hostIndex++) {
/*      */             
/* 2106 */             if (hostIndex == 0) {
/* 2107 */               this.hasTriedMasterFlag = true;
/*      */             }
/*      */             
/*      */             try {
/* 2111 */               String newHostPortPair = this.hostList.get(hostIndex);
/*      */ 
/*      */               
/* 2114 */               int newPort = 3306;
/*      */               
/* 2116 */               String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(newHostPortPair);
/*      */               
/* 2118 */               String newHost = hostPortPair[0];
/*      */               
/* 2120 */               if (newHost == null || StringUtils.isEmptyOrWhitespaceOnly(newHost)) {
/* 2121 */                 newHost = "localhost";
/*      */               }
/*      */               
/* 2124 */               if (hostPortPair[1] != null) {
/*      */                 try {
/* 2126 */                   newPort = Integer.parseInt(hostPortPair[1]);
/*      */                 }
/* 2128 */                 catch (NumberFormatException nfe) {
/* 2129 */                   throw SQLError.createSQLException("Illegal connection port value '" + hostPortPair[1] + "'", "01S00", getExceptionInterceptor());
/*      */                 } 
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2137 */               this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), this, getSocketTimeout(), this.largeRowSizeThreshold.getValueAsInt());
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2142 */               this.io.doHandshake(this.user, this.password, this.database);
/*      */               
/* 2144 */               this.connectionId = this.io.getThreadId();
/* 2145 */               this.isClosed = false;
/*      */ 
/*      */               
/* 2148 */               boolean oldAutoCommit = getAutoCommit();
/* 2149 */               int oldIsolationLevel = this.isolationLevel;
/* 2150 */               boolean oldReadOnly = isReadOnly();
/* 2151 */               String oldCatalog = getCatalog();
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2156 */               initializePropsFromServer();
/*      */               
/* 2158 */               if (isForReconnect) {
/*      */                 
/* 2160 */                 setAutoCommit(oldAutoCommit);
/*      */                 
/* 2162 */                 if (this.hasIsolationLevels) {
/* 2163 */                   setTransactionIsolation(oldIsolationLevel);
/*      */                 }
/*      */                 
/* 2166 */                 setCatalog(oldCatalog);
/*      */               } 
/*      */               
/* 2169 */               if (hostIndex != 0) {
/* 2170 */                 setFailedOverState();
/* 2171 */                 queriesIssuedFailedOverCopy = 0L;
/*      */               } else {
/* 2173 */                 this.failedOver = false;
/* 2174 */                 queriesIssuedFailedOverCopy = 0L;
/*      */                 
/* 2176 */                 if (this.hostListSize > 1) {
/* 2177 */                   setReadOnlyInternal(false);
/*      */                 } else {
/* 2179 */                   setReadOnlyInternal(oldReadOnly);
/*      */                 } 
/*      */               } 
/*      */               
/* 2183 */               connectionGood = true;
/*      */               
/*      */               break;
/* 2186 */             } catch (Exception EEE) {
/* 2187 */               if (this.io != null) {
/* 2188 */                 this.io.forceClose();
/*      */               }
/*      */               
/* 2191 */               connectionNotEstablishedBecause = EEE;
/*      */               
/* 2193 */               connectionGood = false;
/*      */               
/* 2195 */               if (EEE instanceof SQLException) {
/* 2196 */                 SQLException sqlEx = (SQLException)EEE;
/*      */                 
/* 2198 */                 String sqlState = sqlEx.getSQLState();
/*      */ 
/*      */ 
/*      */                 
/* 2202 */                 if (sqlState == null || !sqlState.equals("08S01"))
/*      */                 {
/*      */                   
/* 2205 */                   throw sqlEx;
/*      */                 }
/*      */               } 
/*      */ 
/*      */               
/* 2210 */               if (getRoundRobinLoadBalance()) {
/* 2211 */                 hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList) - 1;
/*      */               }
/* 2213 */               else if (this.hostListSize - 1 == hostIndex) {
/* 2214 */                 throw SQLError.createCommunicationsException(this, (this.io != null) ? this.io.getLastPacketSentTimeMs() : 0L, (this.io != null) ? this.io.getLastPacketReceivedTimeMs() : 0L, EEE, getExceptionInterceptor());
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2224 */           if (!connectionGood) {
/*      */             
/* 2226 */             SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnect"), "08001", getExceptionInterceptor());
/*      */ 
/*      */             
/* 2229 */             chainedEx.initCause(connectionNotEstablishedBecause);
/*      */             
/* 2231 */             throw chainedEx;
/*      */           } 
/*      */         } else {
/* 2234 */           double timeout = getInitialTimeout();
/* 2235 */           boolean connectionGood = false;
/*      */           
/* 2237 */           Exception connectionException = null;
/*      */           
/* 2239 */           int hostIndex = 0;
/*      */           
/* 2241 */           if (getRoundRobinLoadBalance()) {
/* 2242 */             hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList);
/*      */           }
/*      */ 
/*      */           
/* 2246 */           for (; hostIndex < this.hostListSize && !connectionGood; hostIndex++) {
/* 2247 */             if (hostIndex == 0) {
/* 2248 */               this.hasTriedMasterFlag = true;
/*      */             }
/*      */             
/* 2251 */             if (this.preferSlaveDuringFailover && hostIndex == 0) {
/* 2252 */               hostIndex++;
/*      */             }
/*      */             
/* 2255 */             int attemptCount = 0;
/* 2256 */             for (; attemptCount < getMaxReconnects() && !connectionGood; attemptCount++) {
/*      */               try {
/* 2258 */                 if (this.io != null) {
/* 2259 */                   this.io.forceClose();
/*      */                 }
/*      */                 
/* 2262 */                 String newHostPortPair = this.hostList.get(hostIndex);
/*      */ 
/*      */                 
/* 2265 */                 int newPort = 3306;
/*      */                 
/* 2267 */                 String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(newHostPortPair);
/*      */                 
/* 2269 */                 String newHost = hostPortPair[0];
/*      */                 
/* 2271 */                 if (newHost == null || StringUtils.isEmptyOrWhitespaceOnly(newHost)) {
/* 2272 */                   newHost = "localhost";
/*      */                 }
/*      */                 
/* 2275 */                 if (hostPortPair[1] != null) {
/*      */                   try {
/* 2277 */                     newPort = Integer.parseInt(hostPortPair[1]);
/*      */                   }
/* 2279 */                   catch (NumberFormatException nfe) {
/* 2280 */                     throw SQLError.createSQLException("Illegal connection port value '" + hostPortPair[1] + "'", "01S00", getExceptionInterceptor());
/*      */                   } 
/*      */                 }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2288 */                 this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), this, getSocketTimeout(), this.largeRowSizeThreshold.getValueAsInt());
/*      */ 
/*      */ 
/*      */                 
/* 2292 */                 this.io.doHandshake(this.user, this.password, this.database);
/*      */                 
/* 2294 */                 pingInternal(false, 0);
/* 2295 */                 this.connectionId = this.io.getThreadId();
/* 2296 */                 this.isClosed = false;
/*      */ 
/*      */                 
/* 2299 */                 boolean oldAutoCommit = getAutoCommit();
/* 2300 */                 int oldIsolationLevel = this.isolationLevel;
/* 2301 */                 boolean oldReadOnly = isReadOnly();
/* 2302 */                 String oldCatalog = getCatalog();
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2307 */                 initializePropsFromServer();
/*      */                 
/* 2309 */                 if (isForReconnect) {
/*      */                   
/* 2311 */                   setAutoCommit(oldAutoCommit);
/*      */                   
/* 2313 */                   if (this.hasIsolationLevels) {
/* 2314 */                     setTransactionIsolation(oldIsolationLevel);
/*      */                   }
/*      */                   
/* 2317 */                   setCatalog(oldCatalog);
/*      */                 } 
/*      */                 
/* 2320 */                 connectionGood = true;
/*      */                 
/* 2322 */                 if (hostIndex != 0) {
/* 2323 */                   setFailedOverState();
/* 2324 */                   queriesIssuedFailedOverCopy = 0L; break;
/*      */                 } 
/* 2326 */                 this.failedOver = false;
/* 2327 */                 queriesIssuedFailedOverCopy = 0L;
/*      */                 
/* 2329 */                 if (this.hostListSize > 1) {
/* 2330 */                   setReadOnlyInternal(false); break;
/*      */                 } 
/* 2332 */                 setReadOnlyInternal(oldReadOnly);
/*      */ 
/*      */ 
/*      */                 
/*      */                 break;
/* 2337 */               } catch (Exception EEE) {
/* 2338 */                 connectionException = EEE;
/* 2339 */                 connectionGood = false;
/*      */ 
/*      */                 
/* 2342 */                 if (getRoundRobinLoadBalance()) {
/* 2343 */                   hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList) - 1;
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/* 2348 */                 if (connectionGood) {
/*      */                   break;
/*      */                 }
/*      */                 
/* 2352 */                 if (attemptCount > 0) {
/*      */                   try {
/* 2354 */                     Thread.sleep((long)timeout * 1000L);
/* 2355 */                   } catch (InterruptedException IE) {}
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 2362 */           if (!connectionGood) {
/*      */             
/* 2364 */             SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnectWithRetries", new Object[] { new Integer(getMaxReconnects()) }), "08001", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */             
/* 2368 */             chainedEx.initCause(connectionException);
/*      */             
/* 2370 */             throw chainedEx;
/*      */           } 
/*      */         } 
/*      */         
/* 2374 */         if (getParanoid() && !getHighAvailability() && this.hostListSize <= 1) {
/*      */           
/* 2376 */           this.password = null;
/* 2377 */           this.user = null;
/*      */         } 
/*      */         
/* 2380 */         if (isForReconnect) {
/*      */ 
/*      */ 
/*      */           
/* 2384 */           Iterator statementIter = this.openStatements.values().iterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2396 */           Stack serverPreparedStatements = null;
/*      */           
/* 2398 */           while (statementIter.hasNext()) {
/* 2399 */             Object statementObj = statementIter.next();
/*      */             
/* 2401 */             if (statementObj instanceof ServerPreparedStatement) {
/* 2402 */               if (serverPreparedStatements == null) {
/* 2403 */                 serverPreparedStatements = new Stack();
/*      */               }
/*      */               
/* 2406 */               serverPreparedStatements.add(statementObj);
/*      */             } 
/*      */           } 
/*      */           
/* 2410 */           if (serverPreparedStatements != null) {
/* 2411 */             while (!serverPreparedStatements.isEmpty()) {
/* 2412 */               ((ServerPreparedStatement)serverPreparedStatements.pop()).rePrepare();
/*      */             }
/*      */           }
/*      */         } 
/*      */       } finally {
/*      */         
/* 2418 */         this.queriesIssuedFailedOver = queriesIssuedFailedOverCopy;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createPreparedStatementCaches() {
/* 2424 */     int cacheSize = getPreparedStatementCacheSize();
/*      */     
/* 2426 */     this.cachedPreparedStatementParams = new HashMap(cacheSize);
/*      */     
/* 2428 */     if (getUseServerPreparedStmts()) {
/* 2429 */       this.serverSideStatementCheckCache = new LRUCache(cacheSize);
/*      */       
/* 2431 */       this.serverSideStatementCache = new LRUCache(this, cacheSize) { private final ConnectionImpl this$0;
/*      */           protected boolean removeEldestEntry(Map.Entry eldest) {
/* 2433 */             if (this.maxElements <= 1) {
/* 2434 */               return false;
/*      */             }
/*      */             
/* 2437 */             boolean removeIt = super.removeEldestEntry(eldest);
/*      */             
/* 2439 */             if (removeIt) {
/* 2440 */               ServerPreparedStatement ps = (ServerPreparedStatement)eldest.getValue();
/*      */               
/* 2442 */               ps.isCached = false;
/* 2443 */               ps.setClosed(false);
/*      */               
/*      */               try {
/* 2446 */                 ps.close();
/* 2447 */               } catch (SQLException sqlEx) {}
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 2452 */             return removeIt;
/*      */           } }
/*      */         ;
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
/*      */   public Statement createStatement() throws SQLException {
/* 2468 */     return createStatement(1003, 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
/* 2486 */     checkClosed();
/*      */     
/* 2488 */     StatementImpl stmt = new StatementImpl(this, this.database);
/* 2489 */     stmt.setResultSetType(resultSetType);
/* 2490 */     stmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 2492 */     return stmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 2501 */     if (getPedantic() && 
/* 2502 */       resultSetHoldability != 1) {
/* 2503 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2509 */     return createStatement(resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   protected void dumpTestcaseQuery(String query) {
/* 2513 */     System.err.println(query);
/*      */   }
/*      */   
/*      */   protected Connection duplicate() throws SQLException {
/* 2517 */     return new ConnectionImpl(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
/* 2571 */     return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
/* 2586 */     synchronized (this.mutex) {
/* 2587 */       long queryStartTime = 0L;
/*      */       
/* 2589 */       int endOfQueryPacketPosition = 0;
/*      */       
/* 2591 */       if (packet != null) {
/* 2592 */         endOfQueryPacketPosition = packet.getPosition();
/*      */       }
/*      */       
/* 2595 */       if (getGatherPerformanceMetrics()) {
/* 2596 */         queryStartTime = System.currentTimeMillis();
/*      */       }
/*      */       
/* 2599 */       this.lastQueryFinishedTime = 0L;
/*      */       
/* 2601 */       if (this.failedOver && this.autoCommit && !isBatch && 
/* 2602 */         shouldFallBack() && !this.executingFailoverReconnect) {
/*      */         try {
/* 2604 */           this.executingFailoverReconnect = true;
/*      */           
/* 2606 */           createNewIO(true);
/*      */           
/* 2608 */           String connectedHost = this.io.getHost();
/*      */           
/* 2610 */           if (connectedHost != null && this.hostList.get(0).equals(connectedHost)) {
/*      */             
/* 2612 */             this.failedOver = false;
/* 2613 */             this.queriesIssuedFailedOver = 0L;
/* 2614 */             setReadOnlyInternal(false);
/*      */           } 
/*      */         } finally {
/* 2617 */           this.executingFailoverReconnect = false;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 2622 */       if ((getHighAvailability() || this.failedOver) && (this.autoCommit || getAutoReconnectForPools()) && this.needsPing && !isBatch) {
/*      */         
/*      */         try {
/*      */           
/* 2626 */           pingInternal(false, 0);
/*      */           
/* 2628 */           this.needsPing = false;
/* 2629 */         } catch (Exception Ex) {
/* 2630 */           createNewIO(true);
/*      */         } 
/*      */       }
/*      */       
/*      */       try {
/* 2635 */         if (packet == null) {
/* 2636 */           String encoding = null;
/*      */           
/* 2638 */           if (getUseUnicode()) {
/* 2639 */             encoding = getEncoding();
/*      */           }
/*      */           
/* 2642 */           return this.io.sqlQueryDirect(callingStatement, sql, encoding, null, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2648 */         return this.io.sqlQueryDirect(callingStatement, null, null, packet, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
/*      */ 
/*      */       
/*      */       }
/* 2652 */       catch (SQLException sqlE) {
/*      */ 
/*      */         
/* 2655 */         if (getDumpQueriesOnException()) {
/* 2656 */           String extractedSql = extractSqlFromPacket(sql, packet, endOfQueryPacketPosition);
/*      */           
/* 2658 */           StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);
/*      */           
/* 2660 */           messageBuf.append("\n\nQuery being executed when exception was thrown:\n\n");
/*      */           
/* 2662 */           messageBuf.append(extractedSql);
/*      */           
/* 2664 */           sqlE = appendMessageToException(sqlE, messageBuf.toString(), getExceptionInterceptor());
/*      */         } 
/*      */         
/* 2667 */         if (getHighAvailability() || this.failedOver) {
/* 2668 */           this.needsPing = true;
/*      */         } else {
/* 2670 */           String sqlState = sqlE.getSQLState();
/*      */           
/* 2672 */           if (sqlState != null && sqlState.equals("08S01"))
/*      */           {
/*      */             
/* 2675 */             cleanup(sqlE);
/*      */           }
/*      */         } 
/*      */         
/* 2679 */         throw sqlE;
/* 2680 */       } catch (Exception ex) {
/* 2681 */         if (getHighAvailability() || this.failedOver) {
/* 2682 */           this.needsPing = true;
/* 2683 */         } else if (ex instanceof java.io.IOException) {
/* 2684 */           cleanup(ex);
/*      */         } 
/*      */         
/* 2687 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnexpectedException"), "S1000", getExceptionInterceptor());
/*      */ 
/*      */         
/* 2690 */         sqlEx.initCause(ex);
/*      */         
/* 2692 */         throw sqlEx;
/*      */       } finally {
/* 2694 */         if (getMaintainTimeStats()) {
/* 2695 */           this.lastQueryFinishedTime = System.currentTimeMillis();
/*      */         }
/*      */         
/* 2698 */         if (this.failedOver) {
/* 2699 */           this.queriesIssuedFailedOver++;
/*      */         }
/*      */         
/* 2702 */         if (getGatherPerformanceMetrics()) {
/* 2703 */           long queryTime = System.currentTimeMillis() - queryStartTime;
/*      */ 
/*      */           
/* 2706 */           registerQueryExecutionTime(queryTime);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
/* 2716 */     String extractedSql = null;
/*      */     
/* 2718 */     if (possibleSqlQuery != null) {
/* 2719 */       if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
/* 2720 */         StringBuffer truncatedQueryBuf = new StringBuffer(possibleSqlQuery.substring(0, getMaxQuerySizeToLog()));
/*      */         
/* 2722 */         truncatedQueryBuf.append(Messages.getString("MysqlIO.25"));
/* 2723 */         extractedSql = truncatedQueryBuf.toString();
/*      */       } else {
/* 2725 */         extractedSql = possibleSqlQuery;
/*      */       } 
/*      */     }
/*      */     
/* 2729 */     if (extractedSql == null) {
/*      */ 
/*      */ 
/*      */       
/* 2733 */       int extractPosition = endOfQueryPacketPosition;
/*      */       
/* 2735 */       boolean truncated = false;
/*      */       
/* 2737 */       if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
/* 2738 */         extractPosition = getMaxQuerySizeToLog();
/* 2739 */         truncated = true;
/*      */       } 
/*      */       
/* 2742 */       extractedSql = new String(queryPacket.getByteBuffer(), 5, extractPosition - 5);
/*      */ 
/*      */       
/* 2745 */       if (truncated) {
/* 2746 */         extractedSql = extractedSql + Messages.getString("MysqlIO.25");
/*      */       }
/*      */     } 
/*      */     
/* 2750 */     return extractedSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/* 2761 */     cleanup((Throwable)null);
/*      */     
/* 2763 */     super.finalize();
/*      */   }
/*      */   
/*      */   protected StringBuffer generateConnectionCommentBlock(StringBuffer buf) {
/* 2767 */     buf.append("/* conn id ");
/* 2768 */     buf.append(getId());
/* 2769 */     buf.append(" clock: ");
/* 2770 */     buf.append(System.currentTimeMillis());
/* 2771 */     buf.append(" */ ");
/*      */     
/* 2773 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getActiveStatementCount() {
/* 2779 */     if (this.openStatements != null) {
/* 2780 */       synchronized (this.openStatements) {
/* 2781 */         return this.openStatements.size();
/*      */       } 
/*      */     }
/*      */     
/* 2785 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoCommit() throws SQLException {
/* 2797 */     return this.autoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Calendar getCalendarInstanceForSessionOrNew() {
/* 2805 */     if (getDynamicCalendars()) {
/* 2806 */       return Calendar.getInstance();
/*      */     }
/*      */     
/* 2809 */     return getSessionLockedCalendar();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalog() throws SQLException {
/* 2824 */     return this.database;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getCharacterSetMetadata() {
/* 2831 */     return this.characterSetMetadata;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
/* 2844 */     if (javaEncodingName == null) {
/* 2845 */       return null;
/*      */     }
/*      */     
/* 2848 */     if (this.usePlatformCharsetConverters) {
/* 2849 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2853 */     SingleByteCharsetConverter converter = null;
/*      */     
/* 2855 */     synchronized (this.charsetConverterMap) {
/* 2856 */       Object asObject = this.charsetConverterMap.get(javaEncodingName);
/*      */ 
/*      */       
/* 2859 */       if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
/* 2860 */         return null;
/*      */       }
/*      */       
/* 2863 */       converter = (SingleByteCharsetConverter)asObject;
/*      */       
/* 2865 */       if (converter == null) {
/*      */         try {
/* 2867 */           converter = SingleByteCharsetConverter.getInstance(javaEncodingName, this);
/*      */ 
/*      */           
/* 2870 */           if (converter == null) {
/* 2871 */             this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */           } else {
/*      */             
/* 2874 */             this.charsetConverterMap.put(javaEncodingName, converter);
/*      */           } 
/* 2876 */         } catch (UnsupportedEncodingException unsupEncEx) {
/* 2877 */           this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */ 
/*      */           
/* 2880 */           converter = null;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2885 */     return converter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getCharsetNameForIndex(int charsetIndex) throws SQLException {
/* 2900 */     String charsetName = null;
/*      */     
/* 2902 */     if (getUseOldUTF8Behavior()) {
/* 2903 */       return getEncoding();
/*      */     }
/*      */     
/* 2906 */     if (charsetIndex != -1) {
/*      */       try {
/* 2908 */         charsetName = this.indexToCharsetMapping[charsetIndex];
/*      */         
/* 2910 */         if ("sjis".equalsIgnoreCase(charsetName) || "MS932".equalsIgnoreCase(charsetName))
/*      */         {
/*      */           
/* 2913 */           if (CharsetMapping.isAliasForSjis(getEncoding())) {
/* 2914 */             charsetName = getEncoding();
/*      */           }
/*      */         }
/* 2917 */       } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
/* 2918 */         throw SQLError.createSQLException("Unknown character set index for field '" + charsetIndex + "' received from server.", "S1000", getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2925 */       if (charsetName == null) {
/* 2926 */         charsetName = getEncoding();
/*      */       }
/*      */     } else {
/* 2929 */       charsetName = getEncoding();
/*      */     } 
/*      */     
/* 2932 */     return charsetName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TimeZone getDefaultTimeZone() {
/* 2941 */     return this.defaultTimeZone;
/*      */   }
/*      */   
/*      */   protected String getErrorMessageEncoding() {
/* 2945 */     return this.errorMessageEncoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHoldability() throws SQLException {
/* 2952 */     return 2;
/*      */   }
/*      */   
/*      */   long getId() {
/* 2956 */     return this.connectionId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleFor() {
/* 2968 */     if (this.lastQueryFinishedTime == 0L) {
/* 2969 */       return 0L;
/*      */     }
/*      */     
/* 2972 */     long now = System.currentTimeMillis();
/* 2973 */     long idleTime = now - this.lastQueryFinishedTime;
/*      */     
/* 2975 */     return idleTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MysqlIO getIO() throws SQLException {
/* 2986 */     if (this.io == null || this.isClosed) {
/* 2987 */       throw SQLError.createSQLException("Operation not allowed on closed connection", "08003", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2992 */     return this.io;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Log getLog() throws SQLException {
/* 3004 */     return this.log;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
/* 3010 */     String charset = CharsetMapping.getMysqlEncodingForJavaEncoding(javaCharsetName, this);
/*      */ 
/*      */     
/* 3013 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 3014 */       Map mapToCheck = null;
/*      */       
/* 3016 */       if (!getUseDynamicCharsetInfo()) {
/* 3017 */         mapToCheck = CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP;
/*      */       } else {
/* 3019 */         mapToCheck = this.charsetToNumBytesMap;
/*      */         
/* 3021 */         synchronized (this.charsetToNumBytesMap) {
/* 3022 */           if (this.charsetToNumBytesMap.isEmpty()) {
/*      */             
/* 3024 */             Statement stmt = null;
/* 3025 */             ResultSet rs = null;
/*      */             
/*      */             try {
/* 3028 */               stmt = getMetadataSafeStatement();
/*      */               
/* 3030 */               rs = stmt.executeQuery("SHOW CHARACTER SET");
/*      */               
/* 3032 */               while (rs.next()) {
/* 3033 */                 this.charsetToNumBytesMap.put(rs.getString("Charset"), Constants.integerValueOf(rs.getInt("Maxlen")));
/*      */               }
/*      */ 
/*      */               
/* 3037 */               rs.close();
/* 3038 */               rs = null;
/*      */               
/* 3040 */               stmt.close();
/*      */               
/* 3042 */               stmt = null;
/*      */             } finally {
/* 3044 */               if (rs != null) {
/* 3045 */                 rs.close();
/* 3046 */                 rs = null;
/*      */               } 
/*      */               
/* 3049 */               if (stmt != null) {
/* 3050 */                 stmt.close();
/* 3051 */                 stmt = null;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 3058 */       Integer mbPerChar = (Integer)mapToCheck.get(charset);
/*      */       
/* 3060 */       if (mbPerChar != null) {
/* 3061 */         return mbPerChar.intValue();
/*      */       }
/*      */       
/* 3064 */       return 1;
/*      */     } 
/*      */     
/* 3067 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DatabaseMetaData getMetaData() throws SQLException {
/* 3081 */     return getMetaData(true, true);
/*      */   }
/*      */   
/*      */   private DatabaseMetaData getMetaData(boolean checkClosed, boolean checkForInfoSchema) throws SQLException {
/* 3085 */     if (checkClosed) {
/* 3086 */       checkClosed();
/*      */     }
/*      */     
/* 3089 */     return DatabaseMetaData.getInstance(this, this.database, checkForInfoSchema);
/*      */   }
/*      */   
/*      */   protected Statement getMetadataSafeStatement() throws SQLException {
/* 3093 */     Statement stmt = createStatement();
/*      */     
/* 3095 */     if (stmt.getMaxRows() != 0) {
/* 3096 */       stmt.setMaxRows(0);
/*      */     }
/*      */     
/* 3099 */     stmt.setEscapeProcessing(false);
/*      */     
/* 3101 */     if (stmt.getFetchSize() != 0) {
/* 3102 */       stmt.setFetchSize(0);
/*      */     }
/*      */     
/* 3105 */     return stmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object getMutex() throws SQLException {
/* 3116 */     if (this.io == null) {
/* 3117 */       throwConnectionClosedException();
/*      */     }
/*      */     
/* 3120 */     reportMetricsIfNeeded();
/*      */     
/* 3122 */     return this.mutex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int getNetBufferLength() {
/* 3131 */     return this.netBufferLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServerCharacterEncoding() {
/* 3140 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 3141 */       return (String)this.serverVariables.get("character_set_server");
/*      */     }
/* 3143 */     return (String)this.serverVariables.get("character_set");
/*      */   }
/*      */ 
/*      */   
/*      */   int getServerMajorVersion() {
/* 3148 */     return this.io.getServerMajorVersion();
/*      */   }
/*      */   
/*      */   int getServerMinorVersion() {
/* 3152 */     return this.io.getServerMinorVersion();
/*      */   }
/*      */   
/*      */   int getServerSubMinorVersion() {
/* 3156 */     return this.io.getServerSubMinorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getServerTimezoneTZ() {
/* 3165 */     return this.serverTimezoneTZ;
/*      */   }
/*      */ 
/*      */   
/*      */   String getServerVariable(String variableName) {
/* 3170 */     if (this.serverVariables != null) {
/* 3171 */       return (String)this.serverVariables.get(variableName);
/*      */     }
/*      */     
/* 3174 */     return null;
/*      */   }
/*      */   
/*      */   String getServerVersion() {
/* 3178 */     return this.io.getServerVersion();
/*      */   }
/*      */ 
/*      */   
/*      */   protected Calendar getSessionLockedCalendar() {
/* 3183 */     return this.sessionCalendar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTransactionIsolation() throws SQLException {
/* 3195 */     if (this.hasIsolationLevels && !getUseLocalSessionState()) {
/* 3196 */       Statement stmt = null;
/* 3197 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 3200 */         stmt = getMetadataSafeStatement();
/*      */         
/* 3202 */         String query = null;
/*      */         
/* 3204 */         int offset = 0;
/*      */         
/* 3206 */         if (versionMeetsMinimum(4, 0, 3)) {
/* 3207 */           query = "SELECT @@session.tx_isolation";
/* 3208 */           offset = 1;
/*      */         } else {
/* 3210 */           query = "SHOW VARIABLES LIKE 'transaction_isolation'";
/* 3211 */           offset = 2;
/*      */         } 
/*      */         
/* 3214 */         rs = stmt.executeQuery(query);
/*      */         
/* 3216 */         if (rs.next()) {
/* 3217 */           String s = rs.getString(offset);
/*      */           
/* 3219 */           if (s != null) {
/* 3220 */             Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */ 
/*      */             
/* 3223 */             if (intTI != null) {
/* 3224 */               return intTI.intValue();
/*      */             }
/*      */           } 
/*      */           
/* 3228 */           throw SQLError.createSQLException("Could not map transaction isolation '" + s + " to a valid JDBC level.", "S1000", getExceptionInterceptor());
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3234 */         throw SQLError.createSQLException("Could not retrieve transaction isolation level from server", "S1000", getExceptionInterceptor());
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/* 3239 */         if (rs != null) {
/*      */           try {
/* 3241 */             rs.close();
/* 3242 */           } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3247 */           rs = null;
/*      */         } 
/*      */         
/* 3250 */         if (stmt != null) {
/*      */           try {
/* 3252 */             stmt.close();
/* 3253 */           } catch (Exception ex) {}
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3258 */           stmt = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3263 */     return this.isolationLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Map getTypeMap() throws SQLException {
/* 3275 */     if (this.typeMap == null) {
/* 3276 */       this.typeMap = new HashMap();
/*      */     }
/*      */     
/* 3279 */     return this.typeMap;
/*      */   }
/*      */   
/*      */   String getURL() {
/* 3283 */     return this.myURL;
/*      */   }
/*      */   
/*      */   String getUser() {
/* 3287 */     return this.user;
/*      */   }
/*      */   
/*      */   protected Calendar getUtcCalendar() {
/* 3291 */     return this.utcCalendar;
/*      */   }
/*      */ 
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
/* 3304 */     return null;
/*      */   }
/*      */   
/*      */   public boolean hasSameProperties(Connection c) {
/* 3308 */     return this.props.equals(((ConnectionImpl)c).props);
/*      */   }
/*      */   
/*      */   public boolean hasTriedMaster() {
/* 3312 */     return this.hasTriedMasterFlag;
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfPreparedExecutes() {
/* 3316 */     if (getGatherPerformanceMetrics()) {
/* 3317 */       this.numberOfPreparedExecutes++;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3322 */       this.numberOfQueriesIssued++;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfPrepares() {
/* 3327 */     if (getGatherPerformanceMetrics()) {
/* 3328 */       this.numberOfPrepares++;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfResultSetsCreated() {
/* 3333 */     if (getGatherPerformanceMetrics()) {
/* 3334 */       this.numberOfResultSetsCreated++;
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
/*      */   private void initializeDriverProperties(Properties info) throws SQLException {
/* 3349 */     initializeProperties(info);
/*      */     
/* 3351 */     String exceptionInterceptorClasses = getExceptionInterceptors();
/*      */     
/* 3353 */     if (exceptionInterceptorClasses != null && !"".equals(exceptionInterceptorClasses)) {
/* 3354 */       this.exceptionInterceptor = new ExceptionInterceptorChain(this, exceptionInterceptorClasses);
/* 3355 */       this.exceptionInterceptor.init(this, info);
/*      */     } 
/*      */     
/* 3358 */     this.usePlatformCharsetConverters = getUseJvmCharsetConverters();
/*      */     
/* 3360 */     this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
/*      */     
/* 3362 */     if (getProfileSql() || getUseUsageAdvisor()) {
/* 3363 */       this.eventSink = ProfilerEventHandlerFactory.getInstance(this);
/*      */     }
/*      */     
/* 3366 */     if (getCachePreparedStatements()) {
/* 3367 */       createPreparedStatementCaches();
/*      */     }
/*      */     
/* 3370 */     if (getNoDatetimeStringSync() && getUseTimezone()) {
/* 3371 */       throw SQLError.createSQLException("Can't enable noDatetimeSync and useTimezone configuration properties at the same time", "01S00", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3377 */     if (getCacheCallableStatements()) {
/* 3378 */       this.parsedCallableStatementCache = new LRUCache(getCallableStatementCacheSize());
/*      */     }
/*      */ 
/*      */     
/* 3382 */     if (getAllowMultiQueries()) {
/* 3383 */       setCacheResultSetMetadata(false);
/*      */     }
/*      */     
/* 3386 */     if (getCacheResultSetMetadata()) {
/* 3387 */       this.resultSetMetadataCache = new LRUCache(getMetadataCacheSize());
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
/*      */   private void initializePropsFromServer() throws SQLException {
/* 3402 */     String connectionInterceptorClasses = getConnectionLifecycleInterceptors();
/*      */     
/* 3404 */     this.connectionLifecycleInterceptors = null;
/*      */     
/* 3406 */     if (connectionInterceptorClasses != null) {
/* 3407 */       this.connectionLifecycleInterceptors = Util.loadExtensions(this, this.props, connectionInterceptorClasses, "Connection.badLifecycleInterceptor", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */       
/* 3411 */       Iterator iter = this.connectionLifecycleInterceptors.iterator();
/*      */       
/* 3413 */       (new IterateBlock(this, iter) { private final ConnectionImpl this$0;
/*      */           
/*      */           void forEach(Object each) throws SQLException {
/* 3416 */             ((ConnectionLifecycleInterceptor)each).init(this.this$0, this.this$0.props);
/*      */           } }
/*      */         ).doForAll();
/*      */     } 
/*      */     
/* 3421 */     setSessionVariables();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3427 */     if (!versionMeetsMinimum(4, 1, 0)) {
/* 3428 */       setTransformedBitIsBoolean(false);
/*      */     }
/*      */     
/* 3431 */     this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3436 */     if (getUseServerPreparedStmts() && versionMeetsMinimum(4, 1, 0)) {
/* 3437 */       this.useServerPreparedStmts = true;
/*      */       
/* 3439 */       if (versionMeetsMinimum(5, 0, 0) && !versionMeetsMinimum(5, 0, 3)) {
/* 3440 */         this.useServerPreparedStmts = false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3446 */     this.serverVariables.clear();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3451 */     if (versionMeetsMinimum(3, 21, 22)) {
/* 3452 */       loadServerVariables();
/*      */       
/* 3454 */       if (versionMeetsMinimum(5, 0, 2)) {
/* 3455 */         this.autoIncrementIncrement = getServerVariableAsInt("auto_increment_increment", 1);
/*      */       } else {
/* 3457 */         this.autoIncrementIncrement = 1;
/*      */       } 
/*      */       
/* 3460 */       buildCollationMapping();
/*      */       
/* 3462 */       LicenseConfiguration.checkLicenseType(this.serverVariables);
/*      */       
/* 3464 */       String lowerCaseTables = (String)this.serverVariables.get("lower_case_table_names");
/*      */ 
/*      */       
/* 3467 */       this.lowerCaseTableNames = ("on".equalsIgnoreCase(lowerCaseTables) || "1".equalsIgnoreCase(lowerCaseTables) || "2".equalsIgnoreCase(lowerCaseTables));
/*      */ 
/*      */ 
/*      */       
/* 3471 */       this.storesLowerCaseTableName = ("1".equalsIgnoreCase(lowerCaseTables) || "on".equalsIgnoreCase(lowerCaseTables));
/*      */ 
/*      */       
/* 3474 */       configureTimezone();
/*      */       
/* 3476 */       if (this.serverVariables.containsKey("max_allowed_packet")) {
/* 3477 */         int serverMaxAllowedPacket = getServerVariableAsInt("max_allowed_packet", -1);
/*      */         
/* 3479 */         if (serverMaxAllowedPacket != -1 && (serverMaxAllowedPacket < getMaxAllowedPacket() || getMaxAllowedPacket() <= 0)) {
/*      */           
/* 3481 */           setMaxAllowedPacket(serverMaxAllowedPacket);
/* 3482 */         } else if (serverMaxAllowedPacket == -1 && getMaxAllowedPacket() == -1) {
/* 3483 */           setMaxAllowedPacket(65535);
/*      */         } 
/* 3485 */         int preferredBlobSendChunkSize = getBlobSendChunkSize();
/*      */         
/* 3487 */         int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, getMaxAllowedPacket()) - 8192 - 11;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3492 */         setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
/*      */       } 
/*      */       
/* 3495 */       if (this.serverVariables.containsKey("net_buffer_length")) {
/* 3496 */         this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384);
/*      */       }
/*      */       
/* 3499 */       checkTransactionIsolationLevel();
/*      */       
/* 3501 */       if (!versionMeetsMinimum(4, 1, 0)) {
/* 3502 */         checkServerEncoding();
/*      */       }
/*      */       
/* 3505 */       this.io.checkForCharsetMismatch();
/*      */       
/* 3507 */       if (this.serverVariables.containsKey("sql_mode")) {
/* 3508 */         int sqlMode = 0;
/*      */         
/* 3510 */         String sqlModeAsString = (String)this.serverVariables.get("sql_mode");
/*      */         
/*      */         try {
/* 3513 */           sqlMode = Integer.parseInt(sqlModeAsString);
/* 3514 */         } catch (NumberFormatException nfe) {
/*      */ 
/*      */           
/* 3517 */           sqlMode = 0;
/*      */           
/* 3519 */           if (sqlModeAsString != null) {
/* 3520 */             if (sqlModeAsString.indexOf("ANSI_QUOTES") != -1) {
/* 3521 */               sqlMode |= 0x4;
/*      */             }
/*      */             
/* 3524 */             if (sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
/* 3525 */               this.noBackslashEscapes = true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/* 3530 */         if ((sqlMode & 0x4) > 0) {
/* 3531 */           this.useAnsiQuotes = true;
/*      */         } else {
/* 3533 */           this.useAnsiQuotes = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3538 */     this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
/*      */ 
/*      */ 
/*      */     
/* 3542 */     boolean overrideDefaultAutocommit = isAutoCommitNonDefaultOnServer();
/*      */     
/* 3544 */     configureClientCharacterSet(false);
/*      */     
/* 3546 */     if (versionMeetsMinimum(3, 23, 15)) {
/* 3547 */       this.transactionsSupported = true;
/*      */       
/* 3549 */       if (!overrideDefaultAutocommit) {
/* 3550 */         setAutoCommit(true);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 3555 */       this.transactionsSupported = false;
/*      */     } 
/*      */ 
/*      */     
/* 3559 */     if (versionMeetsMinimum(3, 23, 36)) {
/* 3560 */       this.hasIsolationLevels = true;
/*      */     } else {
/* 3562 */       this.hasIsolationLevels = false;
/*      */     } 
/*      */     
/* 3565 */     this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);
/*      */     
/* 3567 */     this.io.resetMaxBuf();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3577 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 3578 */       String characterSetResultsOnServerMysql = (String)this.serverVariables.get("jdbc.local.character_set_results");
/*      */ 
/*      */       
/* 3581 */       if (characterSetResultsOnServerMysql == null || StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, "NULL") || characterSetResultsOnServerMysql.length() == 0) {
/*      */ 
/*      */ 
/*      */         
/* 3585 */         String defaultMetadataCharsetMysql = (String)this.serverVariables.get("character_set_system");
/*      */         
/* 3587 */         String defaultMetadataCharset = null;
/*      */         
/* 3589 */         if (defaultMetadataCharsetMysql != null) {
/* 3590 */           defaultMetadataCharset = CharsetMapping.getJavaEncodingForMysqlEncoding(defaultMetadataCharsetMysql, this);
/*      */         }
/*      */         else {
/*      */           
/* 3594 */           defaultMetadataCharset = "UTF-8";
/*      */         } 
/*      */         
/* 3597 */         this.characterSetMetadata = defaultMetadataCharset;
/*      */       } else {
/* 3599 */         this.characterSetResultsOnServer = CharsetMapping.getJavaEncodingForMysqlEncoding(characterSetResultsOnServerMysql, this);
/*      */ 
/*      */         
/* 3602 */         this.characterSetMetadata = this.characterSetResultsOnServer;
/*      */       } 
/*      */     } else {
/* 3605 */       this.characterSetMetadata = getEncoding();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3612 */     if (versionMeetsMinimum(4, 1, 0) && !versionMeetsMinimum(4, 1, 10) && getAllowMultiQueries())
/*      */     {
/*      */       
/* 3615 */       if (isQueryCacheEnabled()) {
/* 3616 */         setAllowMultiQueries(false);
/*      */       }
/*      */     }
/*      */     
/* 3620 */     if (versionMeetsMinimum(5, 0, 0) && (getUseLocalTransactionState() || getElideSetAutoCommits()) && isQueryCacheEnabled() && !versionMeetsMinimum(6, 0, 10)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3625 */       setUseLocalTransactionState(false);
/* 3626 */       setElideSetAutoCommits(false);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3633 */     setupServerForTruncationChecks();
/*      */   }
/*      */   
/*      */   private boolean isQueryCacheEnabled() {
/* 3637 */     return ("ON".equalsIgnoreCase((String)this.serverVariables.get("query_cache_type")) && !"0".equalsIgnoreCase((String)this.serverVariables.get("query_cache_size")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getServerVariableAsInt(String variableName, int fallbackValue) throws SQLException {
/*      */     try {
/* 3646 */       return Integer.parseInt((String)this.serverVariables.get(variableName));
/*      */     }
/* 3648 */     catch (NumberFormatException nfe) {
/* 3649 */       getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[] { variableName, this.serverVariables.get(variableName), new Integer(fallbackValue) }));
/*      */ 
/*      */       
/* 3652 */       return fallbackValue;
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
/*      */   private boolean isAutoCommitNonDefaultOnServer() throws SQLException {
/* 3665 */     boolean overrideDefaultAutocommit = false;
/*      */     
/* 3667 */     String initConnectValue = (String)this.serverVariables.get("init_connect");
/*      */ 
/*      */     
/* 3670 */     if (versionMeetsMinimum(4, 1, 2) && initConnectValue != null && initConnectValue.length() > 0)
/*      */     {
/* 3672 */       if (!getElideSetAutoCommits()) {
/*      */         
/* 3674 */         ResultSet rs = null;
/* 3675 */         Statement stmt = null;
/*      */         
/*      */         try {
/* 3678 */           stmt = getMetadataSafeStatement();
/*      */           
/* 3680 */           rs = stmt.executeQuery("SELECT @@session.autocommit");
/*      */           
/* 3682 */           if (rs.next()) {
/* 3683 */             this.autoCommit = rs.getBoolean(1);
/* 3684 */             if (this.autoCommit != true) {
/* 3685 */               overrideDefaultAutocommit = true;
/*      */             }
/*      */           } 
/*      */         } finally {
/*      */           
/* 3690 */           if (rs != null) {
/*      */             try {
/* 3692 */               rs.close();
/* 3693 */             } catch (SQLException sqlEx) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 3698 */           if (stmt != null) {
/*      */             try {
/* 3700 */               stmt.close();
/* 3701 */             } catch (SQLException sqlEx) {}
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/* 3707 */       else if (getIO().isSetNeededForAutoCommitMode(true)) {
/*      */         
/* 3709 */         this.autoCommit = false;
/* 3710 */         overrideDefaultAutocommit = true;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3715 */     return overrideDefaultAutocommit;
/*      */   }
/*      */   
/*      */   protected boolean isClientTzUTC() {
/* 3719 */     return this.isClientTzUTC;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosed() {
/* 3728 */     return this.isClosed;
/*      */   }
/*      */   
/*      */   protected boolean isCursorFetchEnabled() throws SQLException {
/* 3732 */     return (versionMeetsMinimum(5, 0, 2) && getUseCursorFetch());
/*      */   }
/*      */   
/*      */   public boolean isInGlobalTx() {
/* 3736 */     return this.isInGlobalTx;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isMasterConnection() {
/* 3747 */     return !this.failedOver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoBackslashEscapesSet() {
/* 3757 */     return this.noBackslashEscapes;
/*      */   }
/*      */   
/*      */   boolean isReadInfoMsgEnabled() {
/* 3761 */     return this.readInfoMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/* 3774 */     return this.readOnly;
/*      */   }
/*      */   
/*      */   protected boolean isRunningOnJDK13() {
/* 3778 */     return this.isRunningOnJDK13;
/*      */   }
/*      */   
/*      */   public synchronized boolean isSameResource(Connection otherConnection) {
/* 3782 */     if (otherConnection == null) {
/* 3783 */       return false;
/*      */     }
/*      */     
/* 3786 */     boolean directCompare = true;
/*      */     
/* 3788 */     String otherHost = ((ConnectionImpl)otherConnection).origHostToConnectTo;
/* 3789 */     String otherOrigDatabase = ((ConnectionImpl)otherConnection).origDatabaseToConnectTo;
/* 3790 */     String otherCurrentCatalog = ((ConnectionImpl)otherConnection).database;
/*      */     
/* 3792 */     if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
/* 3793 */       directCompare = false;
/* 3794 */     } else if (otherHost != null && otherHost.indexOf(',') == -1 && otherHost.indexOf(':') == -1) {
/*      */ 
/*      */       
/* 3797 */       directCompare = (((ConnectionImpl)otherConnection).origPortToConnectTo == this.origPortToConnectTo);
/*      */     } 
/*      */ 
/*      */     
/* 3801 */     if (directCompare) {
/* 3802 */       if (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo)) { directCompare = false;
/* 3803 */         directCompare = false; }
/* 3804 */       else if (!nullSafeCompare(otherCurrentCatalog, this.database))
/* 3805 */       { directCompare = false; }
/*      */     
/*      */     }
/*      */     
/* 3809 */     if (directCompare) {
/* 3810 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 3814 */     String otherResourceId = ((ConnectionImpl)otherConnection).getResourceId();
/* 3815 */     String myResourceId = getResourceId();
/*      */     
/* 3817 */     if (otherResourceId != null || myResourceId != null) {
/* 3818 */       directCompare = nullSafeCompare(otherResourceId, myResourceId);
/*      */       
/* 3820 */       if (directCompare) {
/* 3821 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 3825 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean isServerTzUTC() {
/* 3829 */     return this.isServerTzUTC;
/*      */   }
/*      */   
/* 3832 */   protected ConnectionImpl() { this.usingCachedConfig = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3952 */     this.autoIncrementIncrement = 0; } protected ConnectionImpl(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException { this.usingCachedConfig = false; this.autoIncrementIncrement = 0; this.charsetToNumBytesMap = new HashMap(); this.connectionCreationTimeMillis = System.currentTimeMillis(); this.pointOfOrigin = new Throwable(); this.origHostToConnectTo = hostToConnectTo; this.origPortToConnectTo = portToConnectTo; this.origDatabaseToConnectTo = databaseToConnectTo; try { Blob.class.getMethod("truncate", new Class[] { long.class }); this.isRunningOnJDK13 = false; } catch (NoSuchMethodException nsme) { this.isRunningOnJDK13 = true; }  this.sessionCalendar = new GregorianCalendar(); this.utcCalendar = new GregorianCalendar(); this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT")); this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor()); this.defaultTimeZone = Util.getDefaultTimeZone(); if ("GMT".equalsIgnoreCase(this.defaultTimeZone.getID())) { this.isClientTzUTC = true; } else { this.isClientTzUTC = false; }  this.openStatements = new HashMap(); this.serverVariables = new HashMap(); this.hostList = new ArrayList(); int numHosts = Integer.parseInt(info.getProperty("NUM_HOSTS")); if (hostToConnectTo == null) { this.host = "localhost"; this.hostList.add(this.host + ":" + portToConnectTo); } else if (numHosts > 1) { for (int i = 0; i < numHosts; i++) { int index = i + 1; this.hostList.add(info.getProperty("HOST." + index) + ":" + info.getProperty("PORT." + index)); }  } else { this.host = hostToConnectTo; if (hostToConnectTo.indexOf(":") == -1) { this.hostList.add(this.host + ":" + portToConnectTo); } else { this.hostList.add(this.host); }  }  this.hostListSize = this.hostList.size(); this.port = portToConnectTo; if (databaseToConnectTo == null)
/*      */       databaseToConnectTo = "";  this.database = databaseToConnectTo; this.myURL = url; this.user = info.getProperty("user"); this.password = info.getProperty("password"); if (this.user == null || this.user.equals(""))
/*      */       this.user = "";  if (this.password == null)
/* 3955 */       this.password = "";  this.props = info; initializeDriverProperties(info); try { this.dbmd = getMetaData(false, false); createNewIO(false); initializeStatementInterceptors(); this.io.setStatementInterceptors(this.statementInterceptors); } catch (SQLException ex) { cleanup(ex); throw ex; } catch (Exception ex) { cleanup(ex); StringBuffer mesg = new StringBuffer(128); if (!getParanoid()) { mesg.append("Cannot connect to MySQL server on "); mesg.append(this.host); mesg.append(":"); mesg.append(this.port); mesg.append(".\n\n"); mesg.append("Make sure that there is a MySQL server "); mesg.append("running on the machine/port you are trying "); mesg.append("to connect to and that the machine this software is running on "); mesg.append("is able to connect to this host/port (i.e. not firewalled). "); mesg.append("Also make sure that the server has not been started with the --skip-networking "); mesg.append("flag.\n\n"); } else { mesg.append("Unable to connect to database."); }  SQLException sqlEx = SQLError.createSQLException(mesg.toString(), "08S01", getExceptionInterceptor()); sqlEx.initCause(ex); throw sqlEx; }  } public int getAutoIncrementIncrement() { return this.autoIncrementIncrement; }
/*      */   private void loadServerVariables() throws SQLException { if (getCacheServerConfiguration())
/*      */       synchronized (serverConfigByUrl) { Map cachedVariableMap = (Map)serverConfigByUrl.get(getURL()); if (cachedVariableMap != null) { this.serverVariables = cachedVariableMap; this.usingCachedConfig = true; return; }  }   Statement stmt = null; ResultSet results = null; try { stmt = getMetadataSafeStatement(); String version = this.dbmd.getDriverVersion(); if (version != null && version.indexOf('*') != -1) { StringBuffer buf = new StringBuffer(version.length() + 10); for (int i = 0; i < version.length(); i++) { char c = version.charAt(i); if (c == '*') { buf.append("[star]"); } else { buf.append(c); }  }  version = buf.toString(); }  String versionComment = (getParanoid() || version == null) ? "" : ("/* " + version + " */"); String query = versionComment + "SHOW VARIABLES"; if (versionMeetsMinimum(5, 0, 3))
/*      */         query = versionComment + "SHOW VARIABLES WHERE Variable_name ='language'" + " OR Variable_name = 'net_write_timeout'" + " OR Variable_name = 'interactive_timeout'" + " OR Variable_name = 'wait_timeout'" + " OR Variable_name = 'character_set_client'" + " OR Variable_name = 'character_set_connection'" + " OR Variable_name = 'character_set'" + " OR Variable_name = 'character_set_server'" + " OR Variable_name = 'tx_isolation'" + " OR Variable_name = 'transaction_isolation'" + " OR Variable_name = 'character_set_results'" + " OR Variable_name = 'timezone'" + " OR Variable_name = 'time_zone'" + " OR Variable_name = 'system_time_zone'" + " OR Variable_name = 'lower_case_table_names'" + " OR Variable_name = 'max_allowed_packet'" + " OR Variable_name = 'net_buffer_length'" + " OR Variable_name = 'sql_mode'" + " OR Variable_name = 'query_cache_type'" + " OR Variable_name = 'query_cache_size'" + " OR Variable_name = 'init_connect'";  results = stmt.executeQuery(query); while (results.next())
/*      */         this.serverVariables.put(results.getString(1), results.getString(2));  if (versionMeetsMinimum(5, 0, 2)) { results = stmt.executeQuery(versionComment + "SELECT @@session.auto_increment_increment"); if (results.next())
/*      */           this.serverVariables.put("auto_increment_increment", results.getString(1));  }  if (getCacheServerConfiguration())
/*      */         synchronized (serverConfigByUrl) { serverConfigByUrl.put(getURL(), this.serverVariables); }   } catch (SQLException e) { throw e; } finally { if (results != null)
/*      */         try { results.close(); } catch (SQLException sqlE) {}  if (stmt != null)
/*      */         try { stmt.close(); } catch (SQLException sqlE) {}  }
/* 3964 */      } public boolean lowerCaseTableNames() { return this.lowerCaseTableNames; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void maxRowsChanged(Statement stmt) {
/* 3974 */     synchronized (this.mutex) {
/* 3975 */       if (this.statementsUsingMaxRows == null) {
/* 3976 */         this.statementsUsingMaxRows = new HashMap();
/*      */       }
/*      */       
/* 3979 */       this.statementsUsingMaxRows.put(stmt, stmt);
/*      */       
/* 3981 */       this.maxRowsChanged = true;
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
/*      */   public String nativeSQL(String sql) throws SQLException {
/* 3998 */     if (sql == null) {
/* 3999 */       return null;
/*      */     }
/*      */     
/* 4002 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), this);
/*      */ 
/*      */ 
/*      */     
/* 4006 */     if (escapedSqlResult instanceof String) {
/* 4007 */       return (String)escapedSqlResult;
/*      */     }
/*      */     
/* 4010 */     return ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */   }
/*      */ 
/*      */   
/*      */   private CallableStatement parseCallableStatement(String sql) throws SQLException {
/* 4015 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), this);
/*      */ 
/*      */     
/* 4018 */     boolean isFunctionCall = false;
/* 4019 */     String parsedSql = null;
/*      */     
/* 4021 */     if (escapedSqlResult instanceof EscapeProcessorResult) {
/* 4022 */       parsedSql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/* 4023 */       isFunctionCall = ((EscapeProcessorResult)escapedSqlResult).callingStoredFunction;
/*      */     } else {
/* 4025 */       parsedSql = (String)escapedSqlResult;
/* 4026 */       isFunctionCall = false;
/*      */     } 
/*      */     
/* 4029 */     return CallableStatement.getInstance(this, parsedSql, this.database, isFunctionCall);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean parserKnowsUnicode() {
/* 4039 */     return this.parserKnowsUnicode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ping() throws SQLException {
/* 4049 */     pingInternal(true, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
/* 4054 */     if (checkForClosedConnection) {
/* 4055 */       checkClosed();
/*      */     }
/*      */     
/* 4058 */     long pingMillisLifetime = getSelfDestructOnPingSecondsLifetime();
/* 4059 */     int pingMaxOperations = getSelfDestructOnPingMaxOperations();
/*      */     
/* 4061 */     if ((pingMillisLifetime > 0L && System.currentTimeMillis() - this.connectionCreationTimeMillis > pingMillisLifetime) || (pingMaxOperations > 0 && pingMaxOperations <= this.io.getCommandCount())) {
/*      */ 
/*      */ 
/*      */       
/* 4065 */       close();
/*      */       
/* 4067 */       throw SQLError.createSQLException(Messages.getString("Connection.exceededConnectionLifetime"), "08S01", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4072 */     this.io.sendCommand(14, null, null, false, null, timeoutMillis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement prepareCall(String sql) throws SQLException {
/* 4087 */     return prepareCall(sql, 1003, 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 4108 */     if (versionMeetsMinimum(5, 0, 0)) {
/* 4109 */       CallableStatement cStmt = null;
/*      */       
/* 4111 */       if (!getCacheCallableStatements()) {
/*      */         
/* 4113 */         cStmt = parseCallableStatement(sql);
/*      */       } else {
/* 4115 */         synchronized (this.parsedCallableStatementCache) {
/* 4116 */           CompoundCacheKey key = new CompoundCacheKey(this, getCatalog(), sql);
/*      */           
/* 4118 */           CallableStatement.CallableStatementParamInfo cachedParamInfo = (CallableStatement.CallableStatementParamInfo)this.parsedCallableStatementCache.get(key);
/*      */ 
/*      */           
/* 4121 */           if (cachedParamInfo != null) {
/* 4122 */             cStmt = CallableStatement.getInstance(this, cachedParamInfo);
/*      */           } else {
/* 4124 */             cStmt = parseCallableStatement(sql);
/*      */             
/* 4126 */             cachedParamInfo = cStmt.paramInfo;
/*      */             
/* 4128 */             this.parsedCallableStatementCache.put(key, cachedParamInfo);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 4133 */       cStmt.setResultSetType(resultSetType);
/* 4134 */       cStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */       
/* 4136 */       return cStmt;
/*      */     } 
/*      */     
/* 4139 */     throw SQLError.createSQLException("Callable statements not supported.", "S1C00", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 4149 */     if (getPedantic() && 
/* 4150 */       resultSetHoldability != 1) {
/* 4151 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4157 */     CallableStatement cStmt = (CallableStatement)prepareCall(sql, resultSetType, resultSetConcurrency);
/*      */ 
/*      */     
/* 4160 */     return cStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql) throws SQLException {
/* 4190 */     return prepareStatement(sql, 1003, 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
/* 4199 */     PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4201 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyIndex == 1));
/*      */ 
/*      */     
/* 4204 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 4224 */     checkClosed();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4230 */     PreparedStatement pStmt = null;
/*      */     
/* 4232 */     boolean canServerPrepare = true;
/*      */     
/* 4234 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 4236 */     if (this.useServerPreparedStmts && getEmulateUnsupportedPstmts()) {
/* 4237 */       canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
/*      */     }
/*      */     
/* 4240 */     if (this.useServerPreparedStmts && canServerPrepare) {
/* 4241 */       if (getCachePreparedStatements()) {
/* 4242 */         synchronized (this.serverSideStatementCache) {
/* 4243 */           pStmt = (ServerPreparedStatement)this.serverSideStatementCache.remove(sql);
/*      */           
/* 4245 */           if (pStmt != null) {
/* 4246 */             ((ServerPreparedStatement)pStmt).setClosed(false);
/* 4247 */             pStmt.clearParameters();
/*      */           } 
/*      */           
/* 4250 */           if (pStmt == null) {
/*      */             try {
/* 4252 */               pStmt = ServerPreparedStatement.getInstance(this, nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */               
/* 4254 */               if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4255 */                 ((ServerPreparedStatement)pStmt).isCached = true;
/*      */               }
/*      */               
/* 4258 */               pStmt.setResultSetType(resultSetType);
/* 4259 */               pStmt.setResultSetConcurrency(resultSetConcurrency);
/* 4260 */             } catch (SQLException sqlEx) {
/*      */               
/* 4262 */               if (getEmulateUnsupportedPstmts()) {
/* 4263 */                 pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */                 
/* 4265 */                 if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4266 */                   this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
/*      */                 }
/*      */               } else {
/* 4269 */                 throw sqlEx;
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         try {
/* 4276 */           pStmt = ServerPreparedStatement.getInstance(this, nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */ 
/*      */           
/* 4279 */           pStmt.setResultSetType(resultSetType);
/* 4280 */           pStmt.setResultSetConcurrency(resultSetConcurrency);
/* 4281 */         } catch (SQLException sqlEx) {
/*      */           
/* 4283 */           if (getEmulateUnsupportedPstmts()) {
/* 4284 */             pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */           } else {
/* 4286 */             throw sqlEx;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } else {
/* 4291 */       pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */     } 
/*      */     
/* 4294 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 4303 */     if (getPedantic() && 
/* 4304 */       resultSetHoldability != 1) {
/* 4305 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4311 */     return prepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
/* 4319 */     PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4321 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 4325 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
/* 4333 */     PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4335 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 4339 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
/* 4354 */     SQLException sqlEx = null;
/*      */     
/* 4356 */     if (isClosed()) {
/*      */       return;
/*      */     }
/*      */     
/* 4360 */     this.forceClosedReason = reason;
/*      */     
/*      */     try {
/* 4363 */       if (!skipLocalTeardown) {
/* 4364 */         if (!getAutoCommit() && issueRollback) {
/*      */           try {
/* 4366 */             rollback();
/* 4367 */           } catch (SQLException ex) {
/* 4368 */             sqlEx = ex;
/*      */           } 
/*      */         }
/*      */         
/* 4372 */         reportMetrics();
/*      */         
/* 4374 */         if (getUseUsageAdvisor()) {
/* 4375 */           if (!calledExplicitly) {
/* 4376 */             String message = "Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.";
/*      */             
/* 4378 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4386 */           long connectionLifeTime = System.currentTimeMillis() - this.connectionCreationTimeMillis;
/*      */ 
/*      */           
/* 4389 */           if (connectionLifeTime < 500L) {
/* 4390 */             String message = "Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.";
/*      */             
/* 4392 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 4402 */           closeAllOpenStatements();
/* 4403 */         } catch (SQLException ex) {
/* 4404 */           sqlEx = ex;
/*      */         } 
/*      */         
/* 4407 */         if (this.io != null) {
/*      */           try {
/* 4409 */             this.io.quit();
/* 4410 */           } catch (Exception e) {}
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 4416 */         this.io.forceClose();
/*      */       } 
/*      */       
/* 4419 */       if (this.statementInterceptors != null) {
/* 4420 */         for (int i = 0; i < this.statementInterceptors.size(); i++) {
/* 4421 */           ((StatementInterceptor)this.statementInterceptors.get(i)).destroy();
/*      */         }
/*      */       }
/*      */       
/* 4425 */       if (this.exceptionInterceptor != null) {
/* 4426 */         this.exceptionInterceptor.destroy();
/*      */       }
/*      */     } finally {
/* 4429 */       this.openStatements = null;
/* 4430 */       this.io = null;
/* 4431 */       this.statementInterceptors = null;
/* 4432 */       this.exceptionInterceptor = null;
/* 4433 */       ProfilerEventHandlerFactory.removeInstance(this);
/* 4434 */       this.isClosed = true;
/*      */     } 
/*      */     
/* 4437 */     if (sqlEx != null) {
/* 4438 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
/* 4444 */     if (pstmt.isPoolable()) {
/* 4445 */       synchronized (this.serverSideStatementCache) {
/* 4446 */         this.serverSideStatementCache.put(pstmt.originalSql, pstmt);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void registerQueryExecutionTime(long queryTimeMs) {
/* 4457 */     if (queryTimeMs > this.longestQueryTimeMs) {
/* 4458 */       this.longestQueryTimeMs = queryTimeMs;
/*      */       
/* 4460 */       repartitionPerformanceHistogram();
/*      */     } 
/*      */     
/* 4463 */     addToPerformanceHistogram(queryTimeMs, 1);
/*      */     
/* 4465 */     if (queryTimeMs < this.shortestQueryTimeMs) {
/* 4466 */       this.shortestQueryTimeMs = (queryTimeMs == 0L) ? 1L : queryTimeMs;
/*      */     }
/*      */     
/* 4469 */     this.numberOfQueriesIssued++;
/*      */     
/* 4471 */     this.totalQueryTimeMs += queryTimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void registerStatement(Statement stmt) {
/* 4481 */     synchronized (this.openStatements) {
/* 4482 */       this.openStatements.put(stmt, stmt);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void releaseSavepoint(Savepoint arg0) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void repartitionHistogram(int[] histCounts, long[] histBreakpoints, long currentLowerBound, long currentUpperBound) {
/* 4496 */     if (this.oldHistCounts == null) {
/* 4497 */       this.oldHistCounts = new int[histCounts.length];
/* 4498 */       this.oldHistBreakpoints = new long[histBreakpoints.length];
/*      */     } 
/*      */     
/* 4501 */     System.arraycopy(histCounts, 0, this.oldHistCounts, 0, histCounts.length);
/*      */     
/* 4503 */     System.arraycopy(histBreakpoints, 0, this.oldHistBreakpoints, 0, histBreakpoints.length);
/*      */ 
/*      */     
/* 4506 */     createInitialHistogram(histBreakpoints, currentLowerBound, currentUpperBound);
/*      */ 
/*      */     
/* 4509 */     for (int i = 0; i < 20; i++) {
/* 4510 */       addToHistogram(histCounts, histBreakpoints, this.oldHistBreakpoints[i], this.oldHistCounts[i], currentLowerBound, currentUpperBound);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void repartitionPerformanceHistogram() {
/* 4516 */     checkAndCreatePerformanceHistogram();
/*      */     
/* 4518 */     repartitionHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, (this.shortestQueryTimeMs == Long.MAX_VALUE) ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void repartitionTablesAccessedHistogram() {
/* 4525 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/* 4527 */     repartitionHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, (this.minimumNumberTablesAccessed == Long.MAX_VALUE) ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reportMetrics() {
/* 4535 */     if (getGatherPerformanceMetrics()) {
/* 4536 */       StringBuffer logMessage = new StringBuffer(256);
/*      */       
/* 4538 */       logMessage.append("** Performance Metrics Report **\n");
/* 4539 */       logMessage.append("\nLongest reported query: " + this.longestQueryTimeMs + " ms");
/*      */       
/* 4541 */       logMessage.append("\nShortest reported query: " + this.shortestQueryTimeMs + " ms");
/*      */       
/* 4543 */       logMessage.append("\nAverage query execution time: " + (this.totalQueryTimeMs / this.numberOfQueriesIssued) + " ms");
/*      */ 
/*      */ 
/*      */       
/* 4547 */       logMessage.append("\nNumber of statements executed: " + this.numberOfQueriesIssued);
/*      */       
/* 4549 */       logMessage.append("\nNumber of result sets created: " + this.numberOfResultSetsCreated);
/*      */       
/* 4551 */       logMessage.append("\nNumber of statements prepared: " + this.numberOfPrepares);
/*      */       
/* 4553 */       logMessage.append("\nNumber of prepared statement executions: " + this.numberOfPreparedExecutes);
/*      */ 
/*      */       
/* 4556 */       if (this.perfMetricsHistBreakpoints != null) {
/* 4557 */         logMessage.append("\n\n\tTiming Histogram:\n");
/* 4558 */         int maxNumPoints = 20;
/* 4559 */         int highestCount = Integer.MIN_VALUE;
/*      */         int i;
/* 4561 */         for (i = 0; i < 20; i++) {
/* 4562 */           if (this.perfMetricsHistCounts[i] > highestCount) {
/* 4563 */             highestCount = this.perfMetricsHistCounts[i];
/*      */           }
/*      */         } 
/*      */         
/* 4567 */         if (highestCount == 0) {
/* 4568 */           highestCount = 1;
/*      */         }
/*      */         
/* 4571 */         for (i = 0; i < 19; i++) {
/*      */           
/* 4573 */           if (i == 0) {
/* 4574 */             logMessage.append("\n\tless than " + this.perfMetricsHistBreakpoints[i + 1] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           }
/*      */           else {
/*      */             
/* 4578 */             logMessage.append("\n\tbetween " + this.perfMetricsHistBreakpoints[i] + " and " + this.perfMetricsHistBreakpoints[i + 1] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4584 */           logMessage.append("\t");
/*      */           
/* 4586 */           int numPointsToGraph = (int)(maxNumPoints * this.perfMetricsHistCounts[i] / highestCount);
/*      */           
/* 4588 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 4589 */             logMessage.append("*");
/*      */           }
/*      */           
/* 4592 */           if (this.longestQueryTimeMs < this.perfMetricsHistCounts[i + 1]) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */         
/* 4597 */         if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
/* 4598 */           logMessage.append("\n\tbetween ");
/* 4599 */           logMessage.append(this.perfMetricsHistBreakpoints[18]);
/*      */           
/* 4601 */           logMessage.append(" and ");
/* 4602 */           logMessage.append(this.perfMetricsHistBreakpoints[19]);
/*      */           
/* 4604 */           logMessage.append(" ms: \t");
/* 4605 */           logMessage.append(this.perfMetricsHistCounts[19]);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 4610 */       if (this.numTablesMetricsHistBreakpoints != null) {
/* 4611 */         logMessage.append("\n\n\tTable Join Histogram:\n");
/* 4612 */         int maxNumPoints = 20;
/* 4613 */         int highestCount = Integer.MIN_VALUE;
/*      */         int i;
/* 4615 */         for (i = 0; i < 20; i++) {
/* 4616 */           if (this.numTablesMetricsHistCounts[i] > highestCount) {
/* 4617 */             highestCount = this.numTablesMetricsHistCounts[i];
/*      */           }
/*      */         } 
/*      */         
/* 4621 */         if (highestCount == 0) {
/* 4622 */           highestCount = 1;
/*      */         }
/*      */         
/* 4625 */         for (i = 0; i < 19; i++) {
/*      */           
/* 4627 */           if (i == 0) {
/* 4628 */             logMessage.append("\n\t" + this.numTablesMetricsHistBreakpoints[i + 1] + " tables or less: \t\t" + this.numTablesMetricsHistCounts[i]);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 4633 */             logMessage.append("\n\tbetween " + this.numTablesMetricsHistBreakpoints[i] + " and " + this.numTablesMetricsHistBreakpoints[i + 1] + " tables: \t" + this.numTablesMetricsHistCounts[i]);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4641 */           logMessage.append("\t");
/*      */           
/* 4643 */           int numPointsToGraph = (int)(maxNumPoints * this.numTablesMetricsHistCounts[i] / highestCount);
/*      */           
/* 4645 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 4646 */             logMessage.append("*");
/*      */           }
/*      */           
/* 4649 */           if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[i + 1]) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */         
/* 4654 */         if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
/* 4655 */           logMessage.append("\n\tbetween ");
/* 4656 */           logMessage.append(this.numTablesMetricsHistBreakpoints[18]);
/*      */           
/* 4658 */           logMessage.append(" and ");
/* 4659 */           logMessage.append(this.numTablesMetricsHistBreakpoints[19]);
/*      */           
/* 4661 */           logMessage.append(" tables: ");
/* 4662 */           logMessage.append(this.numTablesMetricsHistCounts[19]);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 4667 */       this.log.logInfo(logMessage);
/*      */       
/* 4669 */       this.metricsLastReportedMs = System.currentTimeMillis();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reportMetricsIfNeeded() {
/* 4678 */     if (getGatherPerformanceMetrics() && 
/* 4679 */       System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis()) {
/* 4680 */       reportMetrics();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void reportNumberOfTablesAccessed(int numTablesAccessed) {
/* 4686 */     if (numTablesAccessed < this.minimumNumberTablesAccessed) {
/* 4687 */       this.minimumNumberTablesAccessed = numTablesAccessed;
/*      */     }
/*      */     
/* 4690 */     if (numTablesAccessed > this.maximumNumberTablesAccessed) {
/* 4691 */       this.maximumNumberTablesAccessed = numTablesAccessed;
/*      */       
/* 4693 */       repartitionTablesAccessedHistogram();
/*      */     } 
/*      */     
/* 4696 */     addToTablesAccessedHistogram(numTablesAccessed, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetServerState() throws SQLException {
/* 4708 */     if (!getParanoid() && this.io != null && versionMeetsMinimum(4, 0, 6))
/*      */     {
/* 4710 */       changeUser(this.user, this.password);
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
/*      */   public void rollback() throws SQLException {
/* 4724 */     synchronized (getMutex()) {
/* 4725 */       checkClosed();
/*      */       
/*      */       try {
/* 4728 */         if (this.connectionLifecycleInterceptors != null) {
/* 4729 */           IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator()) { private final ConnectionImpl this$0;
/*      */               
/*      */               void forEach(Object each) throws SQLException {
/* 4732 */                 if (!((ConnectionLifecycleInterceptor)each).rollback()) {
/* 4733 */                   this.stopIterating = true;
/*      */                 }
/*      */               } }
/*      */             ;
/*      */           
/* 4738 */           iter.doForAll();
/*      */           
/* 4740 */           if (!iter.fullIteration()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */         
/* 4745 */         if (this.autoCommit && !getRelaxAutoCommit()) {
/* 4746 */           throw SQLError.createSQLException("Can't call rollback when autocommit=true", "08003", getExceptionInterceptor());
/*      */         }
/*      */         
/* 4749 */         if (this.transactionsSupported) {
/*      */           try {
/* 4751 */             rollbackNoChecks();
/* 4752 */           } catch (SQLException sqlEx) {
/*      */             
/* 4754 */             if (getIgnoreNonTxTables() && sqlEx.getErrorCode() != 1196)
/*      */             {
/* 4756 */               throw sqlEx;
/*      */             }
/*      */           } 
/*      */         }
/* 4760 */       } catch (SQLException sqlException) {
/* 4761 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 4763 */           throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 4768 */         throw sqlException;
/*      */       } finally {
/* 4770 */         this.needsPing = getReconnectAtTxEnd();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rollback(Savepoint savepoint) throws SQLException {
/* 4780 */     if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
/* 4781 */       synchronized (getMutex()) {
/* 4782 */         checkClosed();
/*      */         
/*      */         try {
/* 4785 */           if (this.connectionLifecycleInterceptors != null) {
/* 4786 */             IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator(), savepoint) { private final Savepoint val$savepoint; private final ConnectionImpl this$0;
/*      */                 
/*      */                 void forEach(Object each) throws SQLException {
/* 4789 */                   if (!((ConnectionLifecycleInterceptor)each).rollback(this.val$savepoint)) {
/* 4790 */                     this.stopIterating = true;
/*      */                   }
/*      */                 } }
/*      */               ;
/*      */             
/* 4795 */             iter.doForAll();
/*      */             
/* 4797 */             if (!iter.fullIteration()) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */           
/* 4802 */           StringBuffer rollbackQuery = new StringBuffer("ROLLBACK TO SAVEPOINT ");
/*      */           
/* 4804 */           rollbackQuery.append('`');
/* 4805 */           rollbackQuery.append(savepoint.getSavepointName());
/* 4806 */           rollbackQuery.append('`');
/*      */           
/* 4808 */           Statement stmt = null;
/*      */           
/*      */           try {
/* 4811 */             stmt = getMetadataSafeStatement();
/*      */             
/* 4813 */             stmt.executeUpdate(rollbackQuery.toString());
/* 4814 */           } catch (SQLException sqlEx) {
/* 4815 */             int errno = sqlEx.getErrorCode();
/*      */             
/* 4817 */             if (errno == 1181) {
/* 4818 */               String msg = sqlEx.getMessage();
/*      */               
/* 4820 */               if (msg != null) {
/* 4821 */                 int indexOfError153 = msg.indexOf("153");
/*      */                 
/* 4823 */                 if (indexOfError153 != -1) {
/* 4824 */                   throw SQLError.createSQLException("Savepoint '" + savepoint.getSavepointName() + "' does not exist", "S1009", errno, getExceptionInterceptor());
/*      */                 }
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 4834 */             if (getIgnoreNonTxTables() && sqlEx.getErrorCode() != 1196)
/*      */             {
/* 4836 */               throw sqlEx;
/*      */             }
/*      */             
/* 4839 */             if ("08S01".equals(sqlEx.getSQLState()))
/*      */             {
/* 4841 */               throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 4846 */             throw sqlEx;
/*      */           } finally {
/* 4848 */             closeStatement(stmt);
/*      */           } 
/*      */         } finally {
/* 4851 */           this.needsPing = getReconnectAtTxEnd();
/*      */         } 
/*      */       } 
/*      */     } else {
/* 4855 */       throw SQLError.notImplemented();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void rollbackNoChecks() throws SQLException {
/* 4860 */     if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) && 
/* 4861 */       !this.io.inTransactionOnServer()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 4866 */     execSQL((StatementImpl)null, "rollback", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
/* 4878 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 4880 */     return ServerPreparedStatement.getInstance(this, nativeSql, getCatalog(), 1003, 1007);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
/* 4890 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 4892 */     PreparedStatement pStmt = ServerPreparedStatement.getInstance(this, nativeSql, getCatalog(), 1003, 1007);
/*      */ 
/*      */ 
/*      */     
/* 4896 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyIndex == 1));
/*      */ 
/*      */     
/* 4899 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
/* 4907 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 4909 */     return ServerPreparedStatement.getInstance(this, nativeSql, getCatalog(), resultSetType, resultSetConcurrency);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
/* 4920 */     if (getPedantic() && 
/* 4921 */       resultSetHoldability != 1) {
/* 4922 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4928 */     return serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
/* 4937 */     PreparedStatement pStmt = (PreparedStatement)serverPrepareStatement(sql);
/*      */     
/* 4939 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 4943 */     return pStmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
/* 4951 */     PreparedStatement pStmt = (PreparedStatement)serverPrepareStatement(sql);
/*      */     
/* 4953 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null && autoGenKeyColNames.length > 0));
/*      */ 
/*      */ 
/*      */     
/* 4957 */     return pStmt;
/*      */   }
/*      */   
/*      */   protected boolean serverSupportsConvertFn() throws SQLException {
/* 4961 */     return versionMeetsMinimum(4, 0, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoCommit(boolean autoCommitFlag) throws SQLException {
/* 4987 */     synchronized (getMutex()) {
/* 4988 */       checkClosed();
/*      */       
/* 4990 */       if (this.connectionLifecycleInterceptors != null) {
/* 4991 */         IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator(), autoCommitFlag) { private final boolean val$autoCommitFlag; private final ConnectionImpl this$0;
/*      */             
/*      */             void forEach(Object each) throws SQLException {
/* 4994 */               if (!((ConnectionLifecycleInterceptor)each).setAutoCommit(this.val$autoCommitFlag)) {
/* 4995 */                 this.stopIterating = true;
/*      */               }
/*      */             } }
/*      */           ;
/*      */         
/* 5000 */         iter.doForAll();
/*      */         
/* 5002 */         if (!iter.fullIteration()) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */       
/* 5007 */       if (getAutoReconnectForPools()) {
/* 5008 */         setHighAvailability(true);
/*      */       }
/*      */       
/*      */       try {
/* 5012 */         if (this.transactionsSupported) {
/*      */           
/* 5014 */           boolean needsSetOnServer = true;
/*      */           
/* 5016 */           if (getUseLocalSessionState() && this.autoCommit == autoCommitFlag) {
/*      */             
/* 5018 */             needsSetOnServer = false;
/* 5019 */           } else if (!getHighAvailability()) {
/* 5020 */             needsSetOnServer = getIO().isSetNeededForAutoCommitMode(autoCommitFlag);
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
/* 5031 */           this.autoCommit = autoCommitFlag;
/*      */           
/* 5033 */           if (needsSetOnServer) {
/* 5034 */             execSQL((StatementImpl)null, autoCommitFlag ? "SET autocommit=1" : "SET autocommit=0", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 5042 */           if (!autoCommitFlag && !getRelaxAutoCommit()) {
/* 5043 */             throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 do not support transactions", "08003", getExceptionInterceptor());
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 5048 */           this.autoCommit = autoCommitFlag;
/*      */         } 
/*      */       } finally {
/* 5051 */         if (getAutoReconnectForPools()) {
/* 5052 */           setHighAvailability(false);
/*      */         }
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatalog(String catalog) throws SQLException {
/* 5080 */     synchronized (getMutex()) {
/* 5081 */       checkClosed();
/*      */       
/* 5083 */       if (catalog == null) {
/* 5084 */         throw SQLError.createSQLException("Catalog can not be null", "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */       
/* 5088 */       if (this.connectionLifecycleInterceptors != null) {
/* 5089 */         IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator(), catalog) { private final String val$catalog; private final ConnectionImpl this$0;
/*      */             
/*      */             void forEach(Object each) throws SQLException {
/* 5092 */               if (!((ConnectionLifecycleInterceptor)each).setCatalog(this.val$catalog)) {
/* 5093 */                 this.stopIterating = true;
/*      */               }
/*      */             } }
/*      */           ;
/*      */         
/* 5098 */         iter.doForAll();
/*      */         
/* 5100 */         if (!iter.fullIteration()) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */       
/* 5105 */       if (getUseLocalSessionState()) {
/* 5106 */         if (this.lowerCaseTableNames) {
/* 5107 */           if (this.database.equalsIgnoreCase(catalog)) {
/*      */             return;
/*      */           }
/*      */         }
/* 5111 */         else if (this.database.equals(catalog)) {
/*      */           return;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 5117 */       String quotedId = this.dbmd.getIdentifierQuoteString();
/*      */       
/* 5119 */       if (quotedId == null || quotedId.equals(" ")) {
/* 5120 */         quotedId = "";
/*      */       }
/*      */       
/* 5123 */       StringBuffer query = new StringBuffer("USE ");
/* 5124 */       query.append(quotedId);
/* 5125 */       query.append(catalog);
/* 5126 */       query.append(quotedId);
/*      */       
/* 5128 */       execSQL((StatementImpl)null, query.toString(), -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5133 */       this.database = catalog;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setFailedOver(boolean flag) {
/* 5142 */     if (flag && getRoundRobinLoadBalance()) {
/*      */       return;
/*      */     }
/*      */     
/* 5146 */     this.failedOver = flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setFailedOverState() throws SQLException {
/* 5156 */     if (getRoundRobinLoadBalance()) {
/*      */       return;
/*      */     }
/*      */     
/* 5160 */     if (getFailOverReadOnly()) {
/* 5161 */       setReadOnlyInternal(true);
/*      */     }
/*      */     
/* 5164 */     this.queriesIssuedFailedOver = 0L;
/* 5165 */     this.failedOver = true;
/* 5166 */     this.masterFailTimeMillis = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHoldability(int arg0) throws SQLException {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInGlobalTx(boolean flag) {
/* 5177 */     this.isInGlobalTx = flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreferSlaveDuringFailover(boolean flag) {
/* 5186 */     this.preferSlaveDuringFailover = flag;
/*      */   }
/*      */   
/*      */   void setReadInfoMsgEnabled(boolean flag) {
/* 5190 */     this.readInfoMsg = flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean readOnlyFlag) throws SQLException {
/* 5204 */     checkClosed();
/*      */ 
/*      */ 
/*      */     
/* 5208 */     if (this.failedOver && getFailOverReadOnly() && !readOnlyFlag) {
/*      */       return;
/*      */     }
/*      */     
/* 5212 */     setReadOnlyInternal(readOnlyFlag);
/*      */   }
/*      */   
/*      */   protected void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
/* 5216 */     this.readOnly = readOnlyFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Savepoint setSavepoint() throws SQLException {
/* 5223 */     MysqlSavepoint savepoint = new MysqlSavepoint(getExceptionInterceptor());
/*      */     
/* 5225 */     setSavepoint(savepoint);
/*      */     
/* 5227 */     return savepoint;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setSavepoint(MysqlSavepoint savepoint) throws SQLException {
/* 5232 */     if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
/* 5233 */       synchronized (getMutex()) {
/* 5234 */         checkClosed();
/*      */         
/* 5236 */         StringBuffer savePointQuery = new StringBuffer("SAVEPOINT ");
/* 5237 */         savePointQuery.append('`');
/* 5238 */         savePointQuery.append(savepoint.getSavepointName());
/* 5239 */         savePointQuery.append('`');
/*      */         
/* 5241 */         Statement stmt = null;
/*      */         
/*      */         try {
/* 5244 */           stmt = getMetadataSafeStatement();
/*      */           
/* 5246 */           stmt.executeUpdate(savePointQuery.toString());
/*      */         } finally {
/* 5248 */           closeStatement(stmt);
/*      */         } 
/*      */       } 
/*      */     } else {
/* 5252 */       throw SQLError.notImplemented();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Savepoint setSavepoint(String name) throws SQLException {
/* 5260 */     MysqlSavepoint savepoint = new MysqlSavepoint(name, getExceptionInterceptor());
/*      */     
/* 5262 */     setSavepoint(savepoint);
/*      */     
/* 5264 */     return savepoint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setSessionVariables() throws SQLException {
/* 5271 */     if (versionMeetsMinimum(4, 0, 0) && getSessionVariables() != null) {
/* 5272 */       List variablesToSet = StringUtils.split(getSessionVariables(), ",", "\"'", "\"'", false);
/*      */ 
/*      */       
/* 5275 */       int numVariablesToSet = variablesToSet.size();
/*      */       
/* 5277 */       Statement stmt = null;
/*      */       
/*      */       try {
/* 5280 */         stmt = getMetadataSafeStatement();
/*      */         
/* 5282 */         for (int i = 0; i < numVariablesToSet; i++) {
/* 5283 */           String variableValuePair = variablesToSet.get(i);
/*      */           
/* 5285 */           if (variableValuePair.startsWith("@")) {
/* 5286 */             stmt.executeUpdate("SET " + variableValuePair);
/*      */           } else {
/* 5288 */             stmt.executeUpdate("SET SESSION " + variableValuePair);
/*      */           } 
/*      */         } 
/*      */       } finally {
/* 5292 */         if (stmt != null) {
/* 5293 */           stmt.close();
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
/*      */   public synchronized void setTransactionIsolation(int level) throws SQLException {
/* 5309 */     checkClosed();
/*      */     
/* 5311 */     if (this.hasIsolationLevels) {
/* 5312 */       String sql = null;
/*      */       
/* 5314 */       boolean shouldSendSet = false;
/*      */       
/* 5316 */       if (getAlwaysSendSetIsolation()) {
/* 5317 */         shouldSendSet = true;
/*      */       }
/* 5319 */       else if (level != this.isolationLevel) {
/* 5320 */         shouldSendSet = true;
/*      */       } 
/*      */ 
/*      */       
/* 5324 */       if (getUseLocalSessionState()) {
/* 5325 */         shouldSendSet = (this.isolationLevel != level);
/*      */       }
/*      */       
/* 5328 */       if (shouldSendSet) {
/* 5329 */         switch (level) {
/*      */           case 0:
/* 5331 */             throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL", getExceptionInterceptor());
/*      */ 
/*      */           
/*      */           case 2:
/* 5335 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
/*      */             break;
/*      */ 
/*      */           
/*      */           case 1:
/* 5340 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
/*      */             break;
/*      */ 
/*      */           
/*      */           case 4:
/* 5345 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
/*      */             break;
/*      */ 
/*      */           
/*      */           case 8:
/* 5350 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
/*      */             break;
/*      */ 
/*      */           
/*      */           default:
/* 5355 */             throw SQLError.createSQLException("Unsupported transaction isolation level '" + level + "'", "S1C00", getExceptionInterceptor());
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 5360 */         execSQL((StatementImpl)null, sql, -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5365 */         this.isolationLevel = level;
/*      */       } 
/*      */     } else {
/* 5368 */       throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", "S1C00", getExceptionInterceptor());
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
/*      */   public synchronized void setTypeMap(Map map) throws SQLException {
/* 5384 */     this.typeMap = map;
/*      */   }
/*      */   
/*      */   private void setupServerForTruncationChecks() throws SQLException {
/* 5388 */     if (getJdbcCompliantTruncation() && 
/* 5389 */       versionMeetsMinimum(5, 0, 2)) {
/* 5390 */       String currentSqlMode = (String)this.serverVariables.get("sql_mode");
/*      */ 
/*      */       
/* 5393 */       boolean strictTransTablesIsSet = (StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1);
/*      */       
/* 5395 */       if (currentSqlMode == null || currentSqlMode.length() == 0 || !strictTransTablesIsSet) {
/*      */         
/* 5397 */         StringBuffer commandBuf = new StringBuffer("SET sql_mode='");
/*      */         
/* 5399 */         if (currentSqlMode != null && currentSqlMode.length() > 0) {
/* 5400 */           commandBuf.append(currentSqlMode);
/* 5401 */           commandBuf.append(",");
/*      */         } 
/*      */         
/* 5404 */         commandBuf.append("STRICT_TRANS_TABLES'");
/*      */         
/* 5406 */         execSQL((StatementImpl)null, commandBuf.toString(), -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5411 */         setJdbcCompliantTruncation(false);
/* 5412 */       } else if (strictTransTablesIsSet) {
/*      */         
/* 5414 */         setJdbcCompliantTruncation(false);
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
/*      */   private boolean shouldFallBack() {
/* 5429 */     long secondsSinceFailedOver = (System.currentTimeMillis() - this.masterFailTimeMillis) / 1000L;
/*      */ 
/*      */     
/* 5432 */     boolean tryFallback = (secondsSinceFailedOver >= getSecondsBeforeRetryMaster() || this.queriesIssuedFailedOver >= getQueriesBeforeRetryMaster());
/*      */     
/* 5434 */     return tryFallback;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void shutdownServer() throws SQLException {
/*      */     try {
/* 5445 */       this.io.sendCommand(8, null, null, false, null, 0);
/* 5446 */     } catch (Exception ex) {
/* 5447 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnhandledExceptionDuringShutdown"), "S1000", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */       
/* 5451 */       sqlEx.initCause(ex);
/*      */       
/* 5453 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsIsolationLevel() {
/* 5463 */     return this.hasIsolationLevels;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsQuotedIdentifiers() {
/* 5472 */     return this.hasQuotedIdentifiers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsTransactions() {
/* 5481 */     return this.transactionsSupported;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void unregisterStatement(Statement stmt) {
/* 5491 */     if (this.openStatements != null) {
/* 5492 */       synchronized (this.openStatements) {
/* 5493 */         this.openStatements.remove(stmt);
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
/*      */   void unsetMaxRows(Statement stmt) throws SQLException {
/* 5509 */     synchronized (this.mutex) {
/* 5510 */       if (this.statementsUsingMaxRows != null) {
/* 5511 */         Object found = this.statementsUsingMaxRows.remove(stmt);
/*      */         
/* 5513 */         if (found != null && this.statementsUsingMaxRows.size() == 0) {
/*      */           
/* 5515 */           execSQL((StatementImpl)null, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, (Buffer)null, 1003, 1007, false, this.database, (Field[])null, false);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 5520 */           this.maxRowsChanged = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean useAnsiQuotedIdentifiers() {
/* 5527 */     return this.useAnsiQuotes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean useMaxRows() {
/* 5536 */     synchronized (this.mutex) {
/* 5537 */       return this.maxRowsChanged;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
/* 5543 */     checkClosed();
/*      */     
/* 5545 */     return this.io.versionMeetsMinimum(major, minor, subminor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CachedResultSetMetaData getCachedMetaData(String sql) {
/* 5563 */     if (this.resultSetMetadataCache != null) {
/* 5564 */       synchronized (this.resultSetMetadataCache) {
/* 5565 */         return (CachedResultSetMetaData)this.resultSetMetadataCache.get(sql);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 5570 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
/* 5591 */     if (cachedMetaData == null) {
/*      */ 
/*      */       
/* 5594 */       cachedMetaData = new CachedResultSetMetaData();
/*      */ 
/*      */ 
/*      */       
/* 5598 */       resultSet.buildIndexMapping();
/* 5599 */       resultSet.initializeWithMetadata();
/*      */       
/* 5601 */       if (resultSet instanceof UpdatableResultSet) {
/* 5602 */         ((UpdatableResultSet)resultSet).checkUpdatability();
/*      */       }
/*      */       
/* 5605 */       resultSet.populateCachedMetaData(cachedMetaData);
/*      */       
/* 5607 */       this.resultSetMetadataCache.put(sql, cachedMetaData);
/*      */     } else {
/* 5609 */       resultSet.initializeFromCachedMetaData(cachedMetaData);
/* 5610 */       resultSet.initializeWithMetadata();
/*      */       
/* 5612 */       if (resultSet instanceof UpdatableResultSet) {
/* 5613 */         ((UpdatableResultSet)resultSet).checkUpdatability();
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
/*      */   public String getStatementComment() {
/* 5626 */     return this.statementComment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatementComment(String comment) {
/* 5638 */     this.statementComment = comment;
/*      */   }
/*      */   
/*      */   public synchronized void reportQueryTime(long millisOrNanos) {
/* 5642 */     this.queryTimeCount++;
/* 5643 */     this.queryTimeSum += millisOrNanos;
/* 5644 */     this.queryTimeSumSquares += (millisOrNanos * millisOrNanos);
/* 5645 */     this.queryTimeMean = (this.queryTimeMean * (this.queryTimeCount - 1L) + millisOrNanos) / this.queryTimeCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isAbonormallyLongQuery(long millisOrNanos) {
/* 5650 */     if (this.queryTimeCount < 15L) {
/* 5651 */       return false;
/*      */     }
/*      */     
/* 5654 */     double stddev = Math.sqrt((this.queryTimeSumSquares - this.queryTimeSum * this.queryTimeSum / this.queryTimeCount) / (this.queryTimeCount - 1L));
/*      */     
/* 5656 */     return (millisOrNanos > this.queryTimeMean + 5.0D * stddev);
/*      */   }
/*      */   
/*      */   public void initializeExtension(Extension ex) throws SQLException {
/* 5660 */     ex.init(this, this.props);
/*      */   }
/*      */   
/*      */   protected void transactionBegun() throws SQLException {
/* 5664 */     if (this.connectionLifecycleInterceptors != null) {
/* 5665 */       IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator()) { private final ConnectionImpl this$0;
/*      */           
/*      */           void forEach(Object each) throws SQLException {
/* 5668 */             ((ConnectionLifecycleInterceptor)each).transactionBegun();
/*      */           } }
/*      */         ;
/*      */       
/* 5672 */       iter.doForAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void transactionCompleted() throws SQLException {
/* 5677 */     if (this.connectionLifecycleInterceptors != null) {
/* 5678 */       IterateBlock iter = new IterateBlock(this, this.connectionLifecycleInterceptors.iterator()) { private final ConnectionImpl this$0;
/*      */           
/*      */           void forEach(Object each) throws SQLException {
/* 5681 */             ((ConnectionLifecycleInterceptor)each).transactionCompleted();
/*      */           } }
/*      */         ;
/*      */       
/* 5685 */       iter.doForAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean storesLowerCaseTableName() {
/* 5690 */     return this.storesLowerCaseTableName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor() {
/* 5696 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   public boolean getRequiresEscapingEncoder() {
/* 5700 */     return this.requiresEscapingEncoder;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ConnectionImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */