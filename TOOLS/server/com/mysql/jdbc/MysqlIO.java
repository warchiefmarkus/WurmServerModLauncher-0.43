/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandlerFactory;
/*      */ import com.mysql.jdbc.util.ReadAheadInputStream;
/*      */ import com.mysql.jdbc.util.ResultSetUtil;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.URL;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.zip.Deflater;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MysqlIO
/*      */ {
/*      */   private static final int UTF8_CHARSET_INDEX = 33;
/*      */   private static final String CODE_PAGE_1252 = "Cp1252";
/*      */   protected static final int NULL_LENGTH = -1;
/*      */   protected static final int COMP_HEADER_LENGTH = 3;
/*      */   protected static final int MIN_COMPRESS_LEN = 50;
/*      */   protected static final int HEADER_LENGTH = 4;
/*      */   protected static final int AUTH_411_OVERHEAD = 33;
/*   80 */   private static int maxBufferSize = 65535;
/*      */   
/*      */   private static final int CLIENT_COMPRESS = 32;
/*      */   
/*      */   protected static final int CLIENT_CONNECT_WITH_DB = 8;
/*      */   
/*      */   private static final int CLIENT_FOUND_ROWS = 2;
/*      */   
/*      */   private static final int CLIENT_LOCAL_FILES = 128;
/*      */   
/*      */   private static final int CLIENT_LONG_FLAG = 4;
/*      */   
/*      */   private static final int CLIENT_LONG_PASSWORD = 1;
/*      */   
/*      */   private static final int CLIENT_PROTOCOL_41 = 512;
/*      */   
/*      */   private static final int CLIENT_INTERACTIVE = 1024;
/*      */   
/*      */   protected static final int CLIENT_SSL = 2048;
/*      */   
/*      */   private static final int CLIENT_TRANSACTIONS = 8192;
/*      */   protected static final int CLIENT_RESERVED = 16384;
/*      */   protected static final int CLIENT_SECURE_CONNECTION = 32768;
/*      */   private static final int CLIENT_MULTI_QUERIES = 65536;
/*      */   private static final int CLIENT_MULTI_RESULTS = 131072;
/*      */   private static final int SERVER_STATUS_IN_TRANS = 1;
/*      */   private static final int SERVER_STATUS_AUTOCOMMIT = 2;
/*      */   static final int SERVER_MORE_RESULTS_EXISTS = 8;
/*      */   private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
/*      */   private static final int SERVER_QUERY_NO_INDEX_USED = 32;
/*      */   private static final int SERVER_QUERY_WAS_SLOW = 2048;
/*      */   private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
/*      */   private static final String FALSE_SCRAMBLE = "xxxxxxxx";
/*      */   protected static final int MAX_QUERY_SIZE_TO_LOG = 1024;
/*      */   protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
/*      */   protected static final int INITIAL_PACKET_SIZE = 1024;
/*  116 */   private static String jvmPlatformCharset = null;
/*      */   
/*      */   protected static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
/*      */   
/*      */   protected static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
/*      */   
/*      */   private static final int MAX_PACKET_DUMP_LENGTH = 1024;
/*      */ 
/*      */   
/*      */   static {
/*  126 */     OutputStreamWriter outWriter = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  134 */       outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
/*  135 */       jvmPlatformCharset = outWriter.getEncoding();
/*      */     } finally {
/*      */       try {
/*  138 */         if (outWriter != null) {
/*  139 */           outWriter.close();
/*      */         }
/*  141 */       } catch (IOException ioEx) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean packetSequenceReset = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int serverCharsetIndex;
/*      */ 
/*      */ 
/*      */   
/*  157 */   private Buffer reusablePacket = null;
/*  158 */   private Buffer sendPacket = null;
/*  159 */   private Buffer sharedSendPacket = null;
/*      */ 
/*      */   
/*  162 */   protected BufferedOutputStream mysqlOutput = null;
/*      */   protected ConnectionImpl connection;
/*  164 */   private Deflater deflater = null;
/*  165 */   protected InputStream mysqlInput = null;
/*  166 */   private LinkedList packetDebugRingBuffer = null;
/*  167 */   private RowData streamingData = null;
/*      */ 
/*      */   
/*  170 */   protected Socket mysqlConnection = null;
/*  171 */   private SocketFactory socketFactory = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SoftReference loadFileBufRef;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SoftReference splitBufRef;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  187 */   protected String host = null;
/*      */   protected String seed;
/*  189 */   private String serverVersion = null;
/*  190 */   private String socketFactoryClassName = null;
/*  191 */   private byte[] packetHeaderBuf = new byte[4];
/*      */   
/*      */   private boolean colDecimalNeedsBump = false;
/*      */   
/*      */   private boolean hadWarnings = false;
/*      */   
/*      */   private boolean has41NewNewProt = false;
/*      */   
/*      */   private boolean hasLongColumnInfo = false;
/*      */   
/*      */   private boolean isInteractiveClient = false;
/*      */   
/*      */   private boolean logSlowQueries = false;
/*      */   
/*      */   private boolean platformDbCharsetMatches = true;
/*      */   
/*      */   private boolean profileSql = false;
/*      */   
/*      */   private boolean queryBadIndexUsed = false;
/*      */   private boolean queryNoIndexUsed = false;
/*      */   private boolean serverQueryWasSlow = false;
/*      */   private boolean use41Extensions = false;
/*      */   private boolean useCompression = false;
/*      */   private boolean useNewLargePackets = false;
/*      */   private boolean useNewUpdateCounts = false;
/*  216 */   private byte packetSequence = 0;
/*  217 */   private byte readPacketSequence = -1;
/*      */   private boolean checkPacketSequence = false;
/*  219 */   private byte protocolVersion = 0;
/*  220 */   private int maxAllowedPacket = 1048576;
/*  221 */   protected int maxThreeBytes = 16581375;
/*  222 */   protected int port = 3306;
/*      */   protected int serverCapabilities;
/*  224 */   private int serverMajorVersion = 0;
/*  225 */   private int serverMinorVersion = 0;
/*  226 */   private int oldServerStatus = 0;
/*  227 */   private int serverStatus = 0;
/*  228 */   private int serverSubMinorVersion = 0;
/*  229 */   private int warningCount = 0;
/*  230 */   protected long clientParam = 0L;
/*  231 */   protected long lastPacketSentTimeMs = 0L;
/*  232 */   protected long lastPacketReceivedTimeMs = 0L;
/*      */   private boolean traceProtocol = false;
/*      */   private boolean enablePacketDebug = false;
/*      */   private Calendar sessionCalendar;
/*      */   private boolean useConnectWithDb;
/*      */   private boolean needToGrabQueryFromPacket;
/*      */   private boolean autoGenerateTestcaseScript;
/*      */   private long threadId;
/*      */   private boolean useNanosForElapsedTime;
/*      */   private long slowQueryThreshold;
/*      */   private String queryTimingUnits;
/*      */   private boolean useDirectRowUnpack = true;
/*      */   private int useBufferRowSizeThreshold;
/*  245 */   private int commandCount = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   private int statementExecutionDepth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useAutoSlowLog;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasLongColumnInfo() {
/*  353 */     return this.hasLongColumnInfo;
/*      */   }
/*      */   
/*      */   protected boolean isDataAvailable() throws SQLException {
/*      */     try {
/*  358 */       return (this.mysqlInput.available() > 0);
/*  359 */     } catch (IOException ioEx) {
/*  360 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long getLastPacketSentTimeMs() {
/*  371 */     return this.lastPacketSentTimeMs;
/*      */   }
/*      */   
/*      */   protected long getLastPacketReceivedTimeMs() {
/*  375 */     return this.lastPacketReceivedTimeMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResultSetImpl getResultSet(StatementImpl callingStatement, long columnCount, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean isBinaryEncoded, Field[] metadataFromCache) throws SQLException {
/*  405 */     Field[] fields = null;
/*      */ 
/*      */ 
/*      */     
/*  409 */     if (metadataFromCache == null) {
/*  410 */       fields = new Field[(int)columnCount];
/*      */       
/*  412 */       for (int i = 0; i < columnCount; i++) {
/*  413 */         Buffer fieldPacket = null;
/*      */         
/*  415 */         fieldPacket = readPacket();
/*  416 */         fields[i] = unpackField(fieldPacket, false);
/*      */       } 
/*      */     } else {
/*  419 */       for (int i = 0; i < columnCount; i++) {
/*  420 */         skipPacket();
/*      */       }
/*      */     } 
/*      */     
/*  424 */     Buffer packet = reuseAndReadPacket(this.reusablePacket);
/*      */     
/*  426 */     readServerStatusForResultSets(packet);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  432 */     if (this.connection.versionMeetsMinimum(5, 0, 2) && this.connection.getUseCursorFetch() && isBinaryEncoded && callingStatement != null && callingStatement.getFetchSize() != 0 && callingStatement.getResultSetType() == 1003) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  438 */       ServerPreparedStatement prepStmt = (ServerPreparedStatement)callingStatement;
/*      */       
/*  440 */       boolean usingCursor = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  448 */       if (this.connection.versionMeetsMinimum(5, 0, 5)) {
/*  449 */         usingCursor = ((this.serverStatus & 0x40) != 0);
/*      */       }
/*      */ 
/*      */       
/*  453 */       if (usingCursor) {
/*  454 */         RowData rows = new RowDataCursor(this, prepStmt, fields);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  459 */         ResultSetImpl resultSetImpl = buildResultSetWithRows(callingStatement, catalog, fields, rows, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  465 */         if (usingCursor) {
/*  466 */           resultSetImpl.setFetchSize(callingStatement.getFetchSize());
/*      */         }
/*      */         
/*  469 */         return resultSetImpl;
/*      */       } 
/*      */     } 
/*      */     
/*  473 */     RowData rowData = null;
/*      */     
/*  475 */     if (!streamResults) {
/*  476 */       rowData = readSingleRowSet(columnCount, maxRows, resultSetConcurrency, isBinaryEncoded, (metadataFromCache == null) ? fields : metadataFromCache);
/*      */     }
/*      */     else {
/*      */       
/*  480 */       rowData = new RowDataDynamic(this, (int)columnCount, (metadataFromCache == null) ? fields : metadataFromCache, isBinaryEncoded);
/*      */ 
/*      */       
/*  483 */       this.streamingData = rowData;
/*      */     } 
/*      */     
/*  486 */     ResultSetImpl rs = buildResultSetWithRows(callingStatement, catalog, (metadataFromCache == null) ? fields : metadataFromCache, rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  492 */     return rs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void forceClose() {
/*      */     try {
/*  500 */       if (this.mysqlInput != null) {
/*  501 */         this.mysqlInput.close();
/*      */       }
/*  503 */     } catch (IOException ioEx) {
/*      */ 
/*      */       
/*  506 */       this.mysqlInput = null;
/*      */     } 
/*      */     
/*      */     try {
/*  510 */       if (this.mysqlOutput != null) {
/*  511 */         this.mysqlOutput.close();
/*      */       }
/*  513 */     } catch (IOException ioEx) {
/*      */ 
/*      */       
/*  516 */       this.mysqlOutput = null;
/*      */     } 
/*      */     
/*      */     try {
/*  520 */       if (this.mysqlConnection != null) {
/*  521 */         this.mysqlConnection.close();
/*      */       }
/*  523 */     } catch (IOException ioEx) {
/*      */ 
/*      */       
/*  526 */       this.mysqlConnection = null;
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
/*      */   protected final void skipPacket() throws SQLException {
/*      */     try {
/*  539 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */ 
/*      */       
/*  542 */       if (lengthRead < 4) {
/*  543 */         forceClose();
/*  544 */         throw new IOException(Messages.getString("MysqlIO.1"));
/*      */       } 
/*      */       
/*  547 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */ 
/*      */ 
/*      */       
/*  551 */       if (this.traceProtocol) {
/*  552 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  554 */         traceMessageBuf.append(Messages.getString("MysqlIO.2"));
/*  555 */         traceMessageBuf.append(packetLength);
/*  556 */         traceMessageBuf.append(Messages.getString("MysqlIO.3"));
/*  557 */         traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
/*      */ 
/*      */         
/*  560 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       } 
/*      */       
/*  563 */       byte multiPacketSeq = this.packetHeaderBuf[3];
/*      */       
/*  565 */       if (!this.packetSequenceReset) {
/*  566 */         if (this.enablePacketDebug && this.checkPacketSequence) {
/*  567 */           checkPacketSequencing(multiPacketSeq);
/*      */         }
/*      */       } else {
/*  570 */         this.packetSequenceReset = false;
/*      */       } 
/*      */       
/*  573 */       this.readPacketSequence = multiPacketSeq;
/*      */       
/*  575 */       skipFully(this.mysqlInput, packetLength);
/*  576 */     } catch (IOException ioEx) {
/*  577 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*  579 */     catch (OutOfMemoryError oom) {
/*      */       
/*  581 */       try { this.connection.realClose(false, false, true, oom);
/*      */         
/*  583 */         throw oom; } finally { Exception exception = null; }
/*      */     
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
/*      */   protected final Buffer readPacket() throws SQLException {
/*      */     try {
/*  599 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */ 
/*      */       
/*  602 */       if (lengthRead < 4) {
/*  603 */         forceClose();
/*  604 */         throw new IOException(Messages.getString("MysqlIO.1"));
/*      */       } 
/*      */       
/*  607 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */ 
/*      */ 
/*      */       
/*  611 */       if (packetLength > this.maxAllowedPacket) {
/*  612 */         throw new PacketTooBigException(packetLength, this.maxAllowedPacket);
/*      */       }
/*      */       
/*  615 */       if (this.traceProtocol) {
/*  616 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  618 */         traceMessageBuf.append(Messages.getString("MysqlIO.2"));
/*  619 */         traceMessageBuf.append(packetLength);
/*  620 */         traceMessageBuf.append(Messages.getString("MysqlIO.3"));
/*  621 */         traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
/*      */ 
/*      */         
/*  624 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       } 
/*      */       
/*  627 */       byte multiPacketSeq = this.packetHeaderBuf[3];
/*      */       
/*  629 */       if (!this.packetSequenceReset) {
/*  630 */         if (this.enablePacketDebug && this.checkPacketSequence) {
/*  631 */           checkPacketSequencing(multiPacketSeq);
/*      */         }
/*      */       } else {
/*  634 */         this.packetSequenceReset = false;
/*      */       } 
/*      */       
/*  637 */       this.readPacketSequence = multiPacketSeq;
/*      */ 
/*      */       
/*  640 */       byte[] buffer = new byte[packetLength + 1];
/*  641 */       int numBytesRead = readFully(this.mysqlInput, buffer, 0, packetLength);
/*      */ 
/*      */       
/*  644 */       if (numBytesRead != packetLength) {
/*  645 */         throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
/*      */       }
/*      */ 
/*      */       
/*  649 */       buffer[packetLength] = 0;
/*      */       
/*  651 */       Buffer packet = new Buffer(buffer);
/*  652 */       packet.setBufLength(packetLength + 1);
/*      */       
/*  654 */       if (this.traceProtocol) {
/*  655 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  657 */         traceMessageBuf.append(Messages.getString("MysqlIO.4"));
/*  658 */         traceMessageBuf.append(getPacketDumpToLog(packet, packetLength));
/*      */ 
/*      */         
/*  661 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       } 
/*      */       
/*  664 */       if (this.enablePacketDebug) {
/*  665 */         enqueuePacketForDebugging(false, false, 0, this.packetHeaderBuf, packet);
/*      */       }
/*      */ 
/*      */       
/*  669 */       if (this.connection.getMaintainTimeStats()) {
/*  670 */         this.lastPacketReceivedTimeMs = System.currentTimeMillis();
/*      */       }
/*      */       
/*  673 */       return packet;
/*  674 */     } catch (IOException ioEx) {
/*  675 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*  677 */     catch (OutOfMemoryError oom) {
/*      */       
/*  679 */       try { this.connection.realClose(false, false, true, oom);
/*      */         
/*  681 */         throw oom; } finally { Exception exception = null; }
/*      */     
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
/*      */   protected final Field unpackField(Buffer packet, boolean extractDefaultValues) throws SQLException {
/*  699 */     if (this.use41Extensions) {
/*      */ 
/*      */       
/*  702 */       if (this.has41NewNewProt) {
/*      */         
/*  704 */         int catalogNameStart = packet.getPosition() + 1;
/*  705 */         int catalogNameLength = packet.fastSkipLenString();
/*  706 */         catalogNameStart = adjustStartForFieldLength(catalogNameStart, catalogNameLength);
/*      */       } 
/*      */       
/*  709 */       int databaseNameStart = packet.getPosition() + 1;
/*  710 */       int databaseNameLength = packet.fastSkipLenString();
/*  711 */       databaseNameStart = adjustStartForFieldLength(databaseNameStart, databaseNameLength);
/*      */       
/*  713 */       int i = packet.getPosition() + 1;
/*  714 */       int j = packet.fastSkipLenString();
/*  715 */       i = adjustStartForFieldLength(i, j);
/*      */ 
/*      */       
/*  718 */       int originalTableNameStart = packet.getPosition() + 1;
/*  719 */       int originalTableNameLength = packet.fastSkipLenString();
/*  720 */       originalTableNameStart = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);
/*      */ 
/*      */       
/*  723 */       int k = packet.getPosition() + 1;
/*  724 */       int m = packet.fastSkipLenString();
/*      */       
/*  726 */       k = adjustStartForFieldLength(k, m);
/*      */ 
/*      */       
/*  729 */       int originalColumnNameStart = packet.getPosition() + 1;
/*  730 */       int originalColumnNameLength = packet.fastSkipLenString();
/*  731 */       originalColumnNameStart = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);
/*      */       
/*  733 */       packet.readByte();
/*      */       
/*  735 */       short charSetNumber = (short)packet.readInt();
/*      */       
/*  737 */       long l = 0L;
/*      */       
/*  739 */       if (this.has41NewNewProt) {
/*  740 */         l = packet.readLong();
/*      */       } else {
/*  742 */         l = packet.readLongInt();
/*      */       } 
/*      */       
/*  745 */       int n = packet.readByte() & 0xFF;
/*      */       
/*  747 */       short s1 = 0;
/*      */       
/*  749 */       if (this.hasLongColumnInfo) {
/*  750 */         s1 = (short)packet.readInt();
/*      */       } else {
/*  752 */         s1 = (short)(packet.readByte() & 0xFF);
/*      */       } 
/*      */       
/*  755 */       int i1 = packet.readByte() & 0xFF;
/*      */       
/*  757 */       int defaultValueStart = -1;
/*  758 */       int defaultValueLength = -1;
/*      */       
/*  760 */       if (extractDefaultValues) {
/*  761 */         defaultValueStart = packet.getPosition() + 1;
/*  762 */         defaultValueLength = packet.fastSkipLenString();
/*      */       } 
/*      */       
/*  765 */       Field field1 = new Field(this.connection, packet.getByteBuffer(), databaseNameStart, databaseNameLength, i, j, originalTableNameStart, originalTableNameLength, k, m, originalColumnNameStart, originalColumnNameLength, l, n, s1, i1, defaultValueStart, defaultValueLength, charSetNumber);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  773 */       return field1;
/*      */     } 
/*      */     
/*  776 */     int tableNameStart = packet.getPosition() + 1;
/*  777 */     int tableNameLength = packet.fastSkipLenString();
/*  778 */     tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
/*      */     
/*  780 */     int nameStart = packet.getPosition() + 1;
/*  781 */     int nameLength = packet.fastSkipLenString();
/*  782 */     nameStart = adjustStartForFieldLength(nameStart, nameLength);
/*      */     
/*  784 */     int colLength = packet.readnBytes();
/*  785 */     int colType = packet.readnBytes();
/*  786 */     packet.readByte();
/*      */     
/*  788 */     short colFlag = 0;
/*      */     
/*  790 */     if (this.hasLongColumnInfo) {
/*  791 */       colFlag = (short)packet.readInt();
/*      */     } else {
/*  793 */       colFlag = (short)(packet.readByte() & 0xFF);
/*      */     } 
/*      */     
/*  796 */     int colDecimals = packet.readByte() & 0xFF;
/*      */     
/*  798 */     if (this.colDecimalNeedsBump) {
/*  799 */       colDecimals++;
/*      */     }
/*      */     
/*  802 */     Field field = new Field(this.connection, packet.getByteBuffer(), nameStart, nameLength, tableNameStart, tableNameLength, colLength, colType, colFlag, colDecimals);
/*      */ 
/*      */ 
/*      */     
/*  806 */     return field;
/*      */   }
/*      */   
/*      */   private int adjustStartForFieldLength(int nameStart, int nameLength) {
/*  810 */     if (nameLength < 251) {
/*  811 */       return nameStart;
/*      */     }
/*      */     
/*  814 */     if (nameLength >= 251 && nameLength < 65536) {
/*  815 */       return nameStart + 2;
/*      */     }
/*      */     
/*  818 */     if (nameLength >= 65536 && nameLength < 16777216) {
/*  819 */       return nameStart + 3;
/*      */     }
/*      */     
/*  822 */     return nameStart + 8;
/*      */   }
/*      */   
/*      */   protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
/*  826 */     if (this.use41Extensions && this.connection.getElideSetAutoCommits()) {
/*  827 */       boolean autoCommitModeOnServer = ((this.serverStatus & 0x2) != 0);
/*      */ 
/*      */       
/*  830 */       if (!autoCommitFlag && versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */ 
/*      */         
/*  834 */         boolean inTransactionOnServer = ((this.serverStatus & 0x1) != 0);
/*      */ 
/*      */         
/*  837 */         return !inTransactionOnServer;
/*      */       } 
/*      */       
/*  840 */       return (autoCommitModeOnServer != autoCommitFlag);
/*      */     } 
/*      */     
/*  843 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean inTransactionOnServer() {
/*  847 */     return ((this.serverStatus & 0x1) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void changeUser(String userName, String password, String database) throws SQLException {
/*  861 */     this.packetSequence = -1;
/*      */     
/*  863 */     int passwordLength = 16;
/*  864 */     int userLength = (userName != null) ? userName.length() : 0;
/*  865 */     int databaseLength = (database != null) ? database.length() : 0;
/*      */     
/*  867 */     int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;
/*      */     
/*  869 */     if ((this.serverCapabilities & 0x8000) != 0) {
/*  870 */       Buffer changeUserPacket = new Buffer(packLength + 1);
/*  871 */       changeUserPacket.writeByte((byte)17);
/*      */       
/*  873 */       if (versionMeetsMinimum(4, 1, 1)) {
/*  874 */         secureAuth411(changeUserPacket, packLength, userName, password, database, false);
/*      */       } else {
/*      */         
/*  877 */         secureAuth(changeUserPacket, packLength, userName, password, database, false);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  882 */       Buffer packet = new Buffer(packLength);
/*  883 */       packet.writeByte((byte)17);
/*      */ 
/*      */       
/*  886 */       packet.writeString(userName);
/*      */       
/*  888 */       if (this.protocolVersion > 9) {
/*  889 */         packet.writeString(Util.newCrypt(password, this.seed));
/*      */       } else {
/*  891 */         packet.writeString(Util.oldCrypt(password, this.seed));
/*      */       } 
/*      */       
/*  894 */       boolean localUseConnectWithDb = (this.useConnectWithDb && database != null && database.length() > 0);
/*      */ 
/*      */       
/*  897 */       if (localUseConnectWithDb) {
/*  898 */         packet.writeString(database);
/*      */       }
/*      */       
/*  901 */       send(packet, packet.getPosition());
/*  902 */       checkErrorPacket();
/*      */       
/*  904 */       if (!localUseConnectWithDb) {
/*  905 */         changeDatabaseTo(database);
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
/*      */   protected Buffer checkErrorPacket() throws SQLException {
/*  919 */     return checkErrorPacket(-1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkForCharsetMismatch() {
/*  926 */     if (this.connection.getUseUnicode() && this.connection.getEncoding() != null) {
/*      */       
/*  928 */       String encodingToCheck = jvmPlatformCharset;
/*      */       
/*  930 */       if (encodingToCheck == null) {
/*  931 */         encodingToCheck = System.getProperty("file.encoding");
/*      */       }
/*      */       
/*  934 */       if (encodingToCheck == null) {
/*  935 */         this.platformDbCharsetMatches = false;
/*      */       } else {
/*  937 */         this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void clearInputStream() throws SQLException {
/*      */     try {
/*  945 */       int len = this.mysqlInput.available();
/*      */       
/*  947 */       while (len > 0) {
/*  948 */         this.mysqlInput.skip(len);
/*  949 */         len = this.mysqlInput.available();
/*      */       } 
/*  951 */     } catch (IOException ioEx) {
/*  952 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void resetReadPacketSequence() {
/*  958 */     this.readPacketSequence = 0;
/*      */   }
/*      */   
/*      */   protected void dumpPacketRingBuffer() throws SQLException {
/*  962 */     if (this.packetDebugRingBuffer != null && this.connection.getEnablePacketDebug()) {
/*      */       
/*  964 */       StringBuffer dumpBuffer = new StringBuffer();
/*      */       
/*  966 */       dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");
/*      */       
/*  968 */       dumpBuffer.append("\n");
/*      */       
/*  970 */       Iterator ringBufIter = this.packetDebugRingBuffer.iterator();
/*  971 */       while (ringBufIter.hasNext()) {
/*  972 */         dumpBuffer.append(ringBufIter.next());
/*  973 */         dumpBuffer.append("\n");
/*      */       } 
/*      */       
/*  976 */       this.connection.getLog().logTrace(dumpBuffer.toString());
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
/*      */   protected void explainSlowQuery(byte[] querySQL, String truncatedQuery) throws SQLException {
/*  990 */     if (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT")) {
/*      */       
/*  992 */       PreparedStatement stmt = null;
/*  993 */       ResultSet rs = null;
/*      */ 
/*      */       
/*  996 */       try { stmt = (PreparedStatement)this.connection.clientPrepareStatement("EXPLAIN ?");
/*  997 */         stmt.setBytesNoEscapeNoQuotes(1, querySQL);
/*  998 */         rs = stmt.executeQuery();
/*      */         
/* 1000 */         StringBuffer explainResults = new StringBuffer(Messages.getString("MysqlIO.8") + truncatedQuery + Messages.getString("MysqlIO.9"));
/*      */ 
/*      */ 
/*      */         
/* 1004 */         ResultSetUtil.appendResultSetSlashGStyle(explainResults, rs);
/*      */         
/* 1006 */         this.connection.getLog().logWarn(explainResults.toString()); }
/* 1007 */       catch (SQLException sqlEx) {  }
/*      */       finally
/* 1009 */       { if (rs != null) {
/* 1010 */           rs.close();
/*      */         }
/*      */         
/* 1013 */         if (stmt != null) {
/* 1014 */           stmt.close();
/*      */         } }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static int getMaxBuf() {
/* 1022 */     return maxBufferSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getServerMajorVersion() {
/* 1031 */     return this.serverMajorVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getServerMinorVersion() {
/* 1040 */     return this.serverMinorVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final int getServerSubMinorVersion() {
/* 1049 */     return this.serverSubMinorVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getServerVersion() {
/* 1058 */     return this.serverVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void doHandshake(String user, String password, String database) throws SQLException {
/* 1075 */     this.checkPacketSequence = false;
/* 1076 */     this.readPacketSequence = 0;
/*      */     
/* 1078 */     Buffer buf = readPacket();
/*      */ 
/*      */     
/* 1081 */     this.protocolVersion = buf.readByte();
/*      */     
/* 1083 */     if (this.protocolVersion == -1) {
/*      */       try {
/* 1085 */         this.mysqlConnection.close();
/* 1086 */       } catch (Exception e) {}
/*      */ 
/*      */ 
/*      */       
/* 1090 */       int errno = 2000;
/*      */       
/* 1092 */       errno = buf.readInt();
/*      */       
/* 1094 */       String serverErrorMessage = buf.readString("ASCII", getExceptionInterceptor());
/*      */       
/* 1096 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.10"));
/*      */       
/* 1098 */       errorBuf.append(serverErrorMessage);
/* 1099 */       errorBuf.append("\"");
/*      */       
/* 1101 */       String xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */ 
/*      */       
/* 1104 */       throw SQLError.createSQLException(SQLError.get(xOpen) + ", " + errorBuf.toString(), xOpen, errno, getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */     
/* 1108 */     this.serverVersion = buf.readString("ASCII", getExceptionInterceptor());
/*      */ 
/*      */     
/* 1111 */     int point = this.serverVersion.indexOf('.');
/*      */     
/* 1113 */     if (point != -1) {
/*      */       try {
/* 1115 */         int n = Integer.parseInt(this.serverVersion.substring(0, point));
/* 1116 */         this.serverMajorVersion = n;
/* 1117 */       } catch (NumberFormatException NFE1) {}
/*      */ 
/*      */ 
/*      */       
/* 1121 */       String remaining = this.serverVersion.substring(point + 1, this.serverVersion.length());
/*      */       
/* 1123 */       point = remaining.indexOf('.');
/*      */       
/* 1125 */       if (point != -1) {
/*      */         try {
/* 1127 */           int n = Integer.parseInt(remaining.substring(0, point));
/* 1128 */           this.serverMinorVersion = n;
/* 1129 */         } catch (NumberFormatException nfe) {}
/*      */ 
/*      */ 
/*      */         
/* 1133 */         remaining = remaining.substring(point + 1, remaining.length());
/*      */         
/* 1135 */         int pos = 0;
/*      */         
/* 1137 */         while (pos < remaining.length() && 
/* 1138 */           remaining.charAt(pos) >= '0' && remaining.charAt(pos) <= '9')
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 1143 */           pos++;
/*      */         }
/*      */         
/*      */         try {
/* 1147 */           int n = Integer.parseInt(remaining.substring(0, pos));
/* 1148 */           this.serverSubMinorVersion = n;
/* 1149 */         } catch (NumberFormatException nfe) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1155 */     if (versionMeetsMinimum(4, 0, 8)) {
/* 1156 */       this.maxThreeBytes = 16777215;
/* 1157 */       this.useNewLargePackets = true;
/*      */     } else {
/* 1159 */       this.maxThreeBytes = 16581375;
/* 1160 */       this.useNewLargePackets = false;
/*      */     } 
/*      */     
/* 1163 */     this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
/* 1164 */     this.colDecimalNeedsBump = !versionMeetsMinimum(3, 23, 15);
/* 1165 */     this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);
/*      */     
/* 1167 */     this.threadId = buf.readLong();
/* 1168 */     this.seed = buf.readString("ASCII", getExceptionInterceptor());
/*      */     
/* 1170 */     this.serverCapabilities = 0;
/*      */     
/* 1172 */     if (buf.getPosition() < buf.getBufLength()) {
/* 1173 */       this.serverCapabilities = buf.readInt();
/*      */     }
/*      */     
/* 1176 */     if (versionMeetsMinimum(4, 1, 1)) {
/* 1177 */       int position = buf.getPosition();
/*      */ 
/*      */       
/* 1180 */       this.serverCharsetIndex = buf.readByte() & 0xFF;
/* 1181 */       this.serverStatus = buf.readInt();
/* 1182 */       checkTransactionState(0);
/* 1183 */       buf.setPosition(position + 16);
/*      */       
/* 1185 */       String seedPart2 = buf.readString("ASCII", getExceptionInterceptor());
/* 1186 */       StringBuffer newSeed = new StringBuffer(20);
/* 1187 */       newSeed.append(this.seed);
/* 1188 */       newSeed.append(seedPart2);
/* 1189 */       this.seed = newSeed.toString();
/*      */     } 
/*      */     
/* 1192 */     if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression())
/*      */     {
/* 1194 */       this.clientParam |= 0x20L;
/*      */     }
/*      */     
/* 1197 */     this.useConnectWithDb = (database != null && database.length() > 0 && !this.connection.getCreateDatabaseIfNotExist());
/*      */ 
/*      */ 
/*      */     
/* 1201 */     if (this.useConnectWithDb) {
/* 1202 */       this.clientParam |= 0x8L;
/*      */     }
/*      */     
/* 1205 */     if ((this.serverCapabilities & 0x800) == 0 && this.connection.getUseSSL()) {
/*      */       
/* 1207 */       if (this.connection.getRequireSSL()) {
/* 1208 */         this.connection.close();
/* 1209 */         forceClose();
/* 1210 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), "08001", getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */       
/* 1214 */       this.connection.setUseSSL(false);
/*      */     } 
/*      */     
/* 1217 */     if ((this.serverCapabilities & 0x4) != 0) {
/*      */       
/* 1219 */       this.clientParam |= 0x4L;
/* 1220 */       this.hasLongColumnInfo = true;
/*      */     } 
/*      */ 
/*      */     
/* 1224 */     if (!this.connection.getUseAffectedRows()) {
/* 1225 */       this.clientParam |= 0x2L;
/*      */     }
/*      */     
/* 1228 */     if (this.connection.getAllowLoadLocalInfile()) {
/* 1229 */       this.clientParam |= 0x80L;
/*      */     }
/*      */     
/* 1232 */     if (this.isInteractiveClient) {
/* 1233 */       this.clientParam |= 0x400L;
/*      */     }
/*      */ 
/*      */     
/* 1237 */     if (this.protocolVersion > 9) {
/* 1238 */       this.clientParam |= 0x1L;
/*      */     } else {
/* 1240 */       this.clientParam &= 0xFFFFFFFFFFFFFFFEL;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1246 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 1247 */       if (versionMeetsMinimum(4, 1, 1)) {
/* 1248 */         this.clientParam |= 0x200L;
/* 1249 */         this.has41NewNewProt = true;
/*      */ 
/*      */         
/* 1252 */         this.clientParam |= 0x2000L;
/*      */ 
/*      */         
/* 1255 */         this.clientParam |= 0x20000L;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1260 */         if (this.connection.getAllowMultiQueries()) {
/* 1261 */           this.clientParam |= 0x10000L;
/*      */         }
/*      */       } else {
/* 1264 */         this.clientParam |= 0x4000L;
/* 1265 */         this.has41NewNewProt = false;
/*      */       } 
/*      */       
/* 1268 */       this.use41Extensions = true;
/*      */     } 
/*      */     
/* 1271 */     int passwordLength = 16;
/* 1272 */     int userLength = (user != null) ? user.length() : 0;
/* 1273 */     int databaseLength = (database != null) ? database.length() : 0;
/*      */     
/* 1275 */     int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;
/*      */     
/* 1277 */     Buffer packet = null;
/*      */     
/* 1279 */     if (!this.connection.getUseSSL()) {
/* 1280 */       if ((this.serverCapabilities & 0x8000) != 0) {
/* 1281 */         this.clientParam |= 0x8000L;
/*      */         
/* 1283 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 1284 */           secureAuth411(null, packLength, user, password, database, true);
/*      */         } else {
/*      */           
/* 1287 */           secureAuth(null, packLength, user, password, database, true);
/*      */         } 
/*      */       } else {
/*      */         
/* 1291 */         packet = new Buffer(packLength);
/*      */         
/* 1293 */         if ((this.clientParam & 0x4000L) != 0L) {
/* 1294 */           if (versionMeetsMinimum(4, 1, 1)) {
/* 1295 */             packet.writeLong(this.clientParam);
/* 1296 */             packet.writeLong(this.maxThreeBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1301 */             packet.writeByte((byte)8);
/*      */ 
/*      */             
/* 1304 */             packet.writeBytesNoNull(new byte[23]);
/*      */           } else {
/* 1306 */             packet.writeLong(this.clientParam);
/* 1307 */             packet.writeLong(this.maxThreeBytes);
/*      */           } 
/*      */         } else {
/* 1310 */           packet.writeInt((int)this.clientParam);
/* 1311 */           packet.writeLongInt(this.maxThreeBytes);
/*      */         } 
/*      */ 
/*      */         
/* 1315 */         packet.writeString(user, "Cp1252", this.connection);
/*      */         
/* 1317 */         if (this.protocolVersion > 9) {
/* 1318 */           packet.writeString(Util.newCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         } else {
/* 1320 */           packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         } 
/*      */         
/* 1323 */         if (this.useConnectWithDb) {
/* 1324 */           packet.writeString(database, "Cp1252", this.connection);
/*      */         }
/*      */         
/* 1327 */         send(packet, packet.getPosition());
/*      */       } 
/*      */     } else {
/* 1330 */       negotiateSSLConnection(user, password, database, packLength);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1336 */     if (!versionMeetsMinimum(4, 1, 1)) {
/* 1337 */       checkErrorPacket();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1343 */     if ((this.serverCapabilities & 0x20) != 0 && this.connection.getUseCompression()) {
/*      */ 
/*      */ 
/*      */       
/* 1347 */       this.deflater = new Deflater();
/* 1348 */       this.useCompression = true;
/* 1349 */       this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
/*      */     } 
/*      */ 
/*      */     
/* 1353 */     if (!this.useConnectWithDb) {
/* 1354 */       changeDatabaseTo(database);
/*      */     }
/*      */     
/*      */     try {
/* 1358 */       this.mysqlConnection = this.socketFactory.afterHandshake();
/* 1359 */     } catch (IOException ioEx) {
/* 1360 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void changeDatabaseTo(String database) throws SQLException {
/* 1365 */     if (database == null || database.length() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/* 1370 */       sendCommand(2, database, null, false, null, 0);
/* 1371 */     } catch (Exception ex) {
/* 1372 */       if (this.connection.getCreateDatabaseIfNotExist()) {
/* 1373 */         sendCommand(3, "CREATE DATABASE IF NOT EXISTS " + database, null, false, null, 0);
/*      */ 
/*      */         
/* 1376 */         sendCommand(2, database, null, false, null, 0);
/*      */       } else {
/* 1378 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
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
/*      */   final ResultSetRow nextRow(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacketForBufferRow, Buffer existingRowPacket) throws SQLException {
/* 1406 */     if (this.useDirectRowUnpack && existingRowPacket == null && !isBinaryEncoded && !useBufferRowIfPossible && !useBufferRowExplicit)
/*      */     {
/*      */       
/* 1409 */       return nextRowFast(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacketForBufferRow);
/*      */     }
/*      */ 
/*      */     
/* 1413 */     Buffer rowPacket = null;
/*      */     
/* 1415 */     if (existingRowPacket == null) {
/* 1416 */       rowPacket = checkErrorPacket();
/*      */       
/* 1418 */       if (!useBufferRowExplicit && useBufferRowIfPossible && 
/* 1419 */         rowPacket.getBufLength() > this.useBufferRowSizeThreshold) {
/* 1420 */         useBufferRowExplicit = true;
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1426 */       rowPacket = existingRowPacket;
/* 1427 */       checkErrorPacket(existingRowPacket);
/*      */     } 
/*      */ 
/*      */     
/* 1431 */     if (!isBinaryEncoded) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1436 */       rowPacket.setPosition(rowPacket.getPosition() - 1);
/*      */       
/* 1438 */       if (!rowPacket.isLastDataPacket()) {
/* 1439 */         if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit)) {
/*      */ 
/*      */           
/* 1442 */           byte[][] rowData = new byte[columnCount][];
/*      */           
/* 1444 */           for (int i = 0; i < columnCount; i++) {
/* 1445 */             rowData[i] = rowPacket.readLenByteArray(0);
/*      */           }
/*      */           
/* 1448 */           return new ByteArrayRow(rowData, getExceptionInterceptor());
/*      */         } 
/*      */         
/* 1451 */         if (!canReuseRowPacketForBufferRow) {
/* 1452 */           this.reusablePacket = new Buffer(rowPacket.getBufLength());
/*      */         }
/*      */         
/* 1455 */         return new BufferRow(rowPacket, fields, false, getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */       
/* 1459 */       readServerStatusForResultSets(rowPacket);
/*      */       
/* 1461 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1468 */     if (!rowPacket.isLastDataPacket()) {
/* 1469 */       if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit))
/*      */       {
/* 1471 */         return unpackBinaryResultSetRow(fields, rowPacket, resultSetConcurrency);
/*      */       }
/*      */ 
/*      */       
/* 1475 */       if (!canReuseRowPacketForBufferRow) {
/* 1476 */         this.reusablePacket = new Buffer(rowPacket.getBufLength());
/*      */       }
/*      */       
/* 1479 */       return new BufferRow(rowPacket, fields, true, getExceptionInterceptor());
/*      */     } 
/*      */     
/* 1482 */     rowPacket.setPosition(rowPacket.getPosition() - 1);
/* 1483 */     readServerStatusForResultSets(rowPacket);
/*      */     
/* 1485 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final ResultSetRow nextRowFast(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacket) throws SQLException {
/*      */     try {
/* 1494 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */ 
/*      */       
/* 1497 */       if (lengthRead < 4) {
/* 1498 */         forceClose();
/* 1499 */         throw new RuntimeException(Messages.getString("MysqlIO.43"));
/*      */       } 
/*      */       
/* 1502 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1507 */       if (packetLength == this.maxThreeBytes) {
/* 1508 */         reuseAndReadPacket(this.reusablePacket, packetLength);
/*      */ 
/*      */         
/* 1511 */         return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacket, this.reusablePacket);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1518 */       if (packetLength > this.useBufferRowSizeThreshold) {
/* 1519 */         reuseAndReadPacket(this.reusablePacket, packetLength);
/*      */ 
/*      */         
/* 1522 */         return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, true, true, false, this.reusablePacket);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1527 */       int remaining = packetLength;
/*      */       
/* 1529 */       boolean firstTime = true;
/*      */       
/* 1531 */       byte[][] rowData = (byte[][])null;
/*      */       
/* 1533 */       for (int i = 0; i < columnCount; i++) {
/*      */         
/* 1535 */         int sw = this.mysqlInput.read() & 0xFF;
/* 1536 */         remaining--;
/*      */         
/* 1538 */         if (firstTime) {
/* 1539 */           if (sw == 255) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1544 */             Buffer errorPacket = new Buffer(packetLength + 4);
/* 1545 */             errorPacket.setPosition(0);
/* 1546 */             errorPacket.writeByte(this.packetHeaderBuf[0]);
/* 1547 */             errorPacket.writeByte(this.packetHeaderBuf[1]);
/* 1548 */             errorPacket.writeByte(this.packetHeaderBuf[2]);
/* 1549 */             errorPacket.writeByte((byte)1);
/* 1550 */             errorPacket.writeByte((byte)sw);
/* 1551 */             readFully(this.mysqlInput, errorPacket.getByteBuffer(), 5, packetLength - 1);
/* 1552 */             errorPacket.setPosition(4);
/* 1553 */             checkErrorPacket(errorPacket);
/*      */           } 
/*      */           
/* 1556 */           if (sw == 254 && packetLength < 9) {
/* 1557 */             if (this.use41Extensions) {
/* 1558 */               this.warningCount = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
/*      */               
/* 1560 */               remaining -= 2;
/*      */               
/* 1562 */               if (this.warningCount > 0) {
/* 1563 */                 this.hadWarnings = true;
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1569 */               this.oldServerStatus = this.serverStatus;
/*      */               
/* 1571 */               this.serverStatus = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
/*      */               
/* 1573 */               checkTransactionState(this.oldServerStatus);
/*      */               
/* 1575 */               remaining -= 2;
/*      */               
/* 1577 */               if (remaining > 0) {
/* 1578 */                 skipFully(this.mysqlInput, remaining);
/*      */               }
/*      */             } 
/*      */             
/* 1582 */             return null;
/*      */           } 
/*      */           
/* 1585 */           rowData = new byte[columnCount][];
/*      */           
/* 1587 */           firstTime = false;
/*      */         } 
/*      */         
/* 1590 */         int len = 0;
/*      */         
/* 1592 */         switch (sw) {
/*      */           case 251:
/* 1594 */             len = -1;
/*      */             break;
/*      */           
/*      */           case 252:
/* 1598 */             len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
/*      */             
/* 1600 */             remaining -= 2;
/*      */             break;
/*      */           
/*      */           case 253:
/* 1604 */             len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8 | (this.mysqlInput.read() & 0xFF) << 16;
/*      */ 
/*      */ 
/*      */             
/* 1608 */             remaining -= 3;
/*      */             break;
/*      */           
/*      */           case 254:
/* 1612 */             len = (int)((this.mysqlInput.read() & 0xFF) | (this.mysqlInput.read() & 0xFF) << 8L | (this.mysqlInput.read() & 0xFF) << 16L | (this.mysqlInput.read() & 0xFF) << 24L | (this.mysqlInput.read() & 0xFF) << 32L | (this.mysqlInput.read() & 0xFF) << 40L | (this.mysqlInput.read() & 0xFF) << 48L | (this.mysqlInput.read() & 0xFF) << 56L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1620 */             remaining -= 8;
/*      */             break;
/*      */           
/*      */           default:
/* 1624 */             len = sw;
/*      */             break;
/*      */         } 
/* 1627 */         if (len == -1) {
/* 1628 */           rowData[i] = null;
/* 1629 */         } else if (len == 0) {
/* 1630 */           rowData[i] = Constants.EMPTY_BYTE_ARRAY;
/*      */         } else {
/* 1632 */           rowData[i] = new byte[len];
/*      */           
/* 1634 */           int bytesRead = readFully(this.mysqlInput, rowData[i], 0, len);
/*      */ 
/*      */           
/* 1637 */           if (bytesRead != len) {
/* 1638 */             throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException(Messages.getString("MysqlIO.43")), getExceptionInterceptor());
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1643 */           remaining -= bytesRead;
/*      */         } 
/*      */       } 
/*      */       
/* 1647 */       if (remaining > 0) {
/* 1648 */         skipFully(this.mysqlInput, remaining);
/*      */       }
/*      */       
/* 1651 */       return new ByteArrayRow(rowData, getExceptionInterceptor());
/* 1652 */     } catch (IOException ioEx) {
/* 1653 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void quit() throws SQLException {
/* 1664 */     Buffer packet = new Buffer(6);
/* 1665 */     this.packetSequence = -1;
/* 1666 */     packet.writeByte((byte)1);
/* 1667 */     send(packet, packet.getPosition());
/* 1668 */     forceClose();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Buffer getSharedSendPacket() {
/* 1678 */     if (this.sharedSendPacket == null) {
/* 1679 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */     
/* 1682 */     return this.sharedSendPacket;
/*      */   }
/*      */   
/*      */   void closeStreamer(RowData streamer) throws SQLException {
/* 1686 */     if (this.streamingData == null) {
/* 1687 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"), getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 1691 */     if (streamer != this.streamingData) {
/* 1692 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.19") + streamer + Messages.getString("MysqlIO.20") + Messages.getString("MysqlIO.21") + Messages.getString("MysqlIO.22"), getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1698 */     this.streamingData = null;
/*      */   }
/*      */   
/*      */   boolean tackOnMoreStreamingResults(ResultSetImpl addingTo) throws SQLException {
/* 1702 */     if ((this.serverStatus & 0x8) != 0) {
/*      */       
/* 1704 */       boolean moreRowSetsExist = true;
/* 1705 */       ResultSetImpl currentResultSet = addingTo;
/* 1706 */       boolean firstTime = true;
/*      */       
/* 1708 */       while (moreRowSetsExist && (
/* 1709 */         firstTime || !currentResultSet.reallyResult())) {
/*      */ 
/*      */ 
/*      */         
/* 1713 */         firstTime = false;
/*      */         
/* 1715 */         Buffer fieldPacket = checkErrorPacket();
/* 1716 */         fieldPacket.setPosition(0);
/*      */         
/* 1718 */         Statement owningStatement = addingTo.getStatement();
/*      */         
/* 1720 */         int maxRows = owningStatement.getMaxRows();
/*      */ 
/*      */ 
/*      */         
/* 1724 */         ResultSetImpl newResultSet = readResultsForQueryOrUpdate((StatementImpl)owningStatement, maxRows, owningStatement.getResultSetType(), owningStatement.getResultSetConcurrency(), true, owningStatement.getConnection().getCatalog(), fieldPacket, addingTo.isBinaryEncoded, -1L, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1732 */         currentResultSet.setNextResultSet(newResultSet);
/*      */         
/* 1734 */         currentResultSet = newResultSet;
/*      */         
/* 1736 */         moreRowSetsExist = ((this.serverStatus & 0x8) != 0);
/*      */         
/* 1738 */         if (!currentResultSet.reallyResult() && !moreRowSetsExist)
/*      */         {
/* 1740 */           return false;
/*      */         }
/*      */       } 
/*      */       
/* 1744 */       return true;
/*      */     } 
/*      */     
/* 1747 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResultSetImpl readAllResults(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache) throws SQLException {
/* 1755 */     resultPacket.setPosition(resultPacket.getPosition() - 1);
/*      */     
/* 1757 */     ResultSetImpl topLevelResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1762 */     ResultSetImpl currentResultSet = topLevelResultSet;
/*      */     
/* 1764 */     boolean checkForMoreResults = ((this.clientParam & 0x20000L) != 0L);
/*      */ 
/*      */     
/* 1767 */     boolean serverHasMoreResults = ((this.serverStatus & 0x8) != 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1773 */     if (serverHasMoreResults && streamResults) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1778 */       if (topLevelResultSet.getUpdateCount() != -1L) {
/* 1779 */         tackOnMoreStreamingResults(topLevelResultSet);
/*      */       }
/*      */       
/* 1782 */       reclaimLargeReusablePacket();
/*      */       
/* 1784 */       return topLevelResultSet;
/*      */     } 
/*      */     
/* 1787 */     boolean moreRowSetsExist = checkForMoreResults & serverHasMoreResults;
/*      */     
/* 1789 */     while (moreRowSetsExist) {
/* 1790 */       Buffer fieldPacket = checkErrorPacket();
/* 1791 */       fieldPacket.setPosition(0);
/*      */       
/* 1793 */       ResultSetImpl newResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, fieldPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1798 */       currentResultSet.setNextResultSet(newResultSet);
/*      */       
/* 1800 */       currentResultSet = newResultSet;
/*      */       
/* 1802 */       moreRowSetsExist = ((this.serverStatus & 0x8) != 0);
/*      */     } 
/*      */     
/* 1805 */     if (!streamResults) {
/* 1806 */       clearInputStream();
/*      */     }
/*      */     
/* 1809 */     reclaimLargeReusablePacket();
/*      */     
/* 1811 */     return topLevelResultSet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void resetMaxBuf() {
/* 1818 */     this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Buffer sendCommand(int command, String extraData, Buffer queryPacket, boolean skipCheck, String extraDataCharEncoding, int timeoutMillis) throws SQLException {
/* 1844 */     this.commandCount++;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1851 */     this.enablePacketDebug = this.connection.getEnablePacketDebug();
/* 1852 */     this.readPacketSequence = 0;
/*      */     
/* 1854 */     int oldTimeout = 0;
/*      */     
/* 1856 */     if (timeoutMillis != 0) {
/*      */       try {
/* 1858 */         oldTimeout = this.mysqlConnection.getSoTimeout();
/* 1859 */         this.mysqlConnection.setSoTimeout(timeoutMillis);
/* 1860 */       } catch (SocketException e) {
/* 1861 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1868 */       checkForOutstandingStreamingData();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1873 */       this.oldServerStatus = this.serverStatus;
/* 1874 */       this.serverStatus = 0;
/* 1875 */       this.hadWarnings = false;
/* 1876 */       this.warningCount = 0;
/*      */       
/* 1878 */       this.queryNoIndexUsed = false;
/* 1879 */       this.queryBadIndexUsed = false;
/* 1880 */       this.serverQueryWasSlow = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1886 */       if (this.useCompression) {
/* 1887 */         int bytesLeft = this.mysqlInput.available();
/*      */         
/* 1889 */         if (bytesLeft > 0) {
/* 1890 */           this.mysqlInput.skip(bytesLeft);
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/* 1895 */         clearInputStream();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1904 */         if (queryPacket == null) {
/* 1905 */           int packLength = 8 + ((extraData != null) ? extraData.length() : 0) + 2;
/*      */ 
/*      */           
/* 1908 */           if (this.sendPacket == null) {
/* 1909 */             this.sendPacket = new Buffer(packLength);
/*      */           }
/*      */           
/* 1912 */           this.packetSequence = -1;
/* 1913 */           this.readPacketSequence = 0;
/* 1914 */           this.checkPacketSequence = true;
/* 1915 */           this.sendPacket.clear();
/*      */           
/* 1917 */           this.sendPacket.writeByte((byte)command);
/*      */           
/* 1919 */           if (command == 2 || command == 5 || command == 6 || command == 3 || command == 22) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1924 */             if (extraDataCharEncoding == null) {
/* 1925 */               this.sendPacket.writeStringNoNull(extraData);
/*      */             } else {
/* 1927 */               this.sendPacket.writeStringNoNull(extraData, extraDataCharEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1932 */           else if (command == 12) {
/* 1933 */             long id = Long.parseLong(extraData);
/* 1934 */             this.sendPacket.writeLong(id);
/*      */           } 
/*      */           
/* 1937 */           send(this.sendPacket, this.sendPacket.getPosition());
/*      */         } else {
/* 1939 */           this.packetSequence = -1;
/* 1940 */           send(queryPacket, queryPacket.getPosition());
/*      */         } 
/* 1942 */       } catch (SQLException sqlEx) {
/*      */         
/* 1944 */         throw sqlEx;
/* 1945 */       } catch (Exception ex) {
/* 1946 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */       
/* 1950 */       Buffer returnPacket = null;
/*      */       
/* 1952 */       if (!skipCheck) {
/* 1953 */         if (command == 23 || command == 26) {
/*      */           
/* 1955 */           this.readPacketSequence = 0;
/* 1956 */           this.packetSequenceReset = true;
/*      */         } 
/*      */         
/* 1959 */         returnPacket = checkErrorPacket(command);
/*      */       } 
/*      */       
/* 1962 */       return returnPacket;
/* 1963 */     } catch (IOException ioEx) {
/* 1964 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } finally {
/*      */       
/* 1967 */       if (timeoutMillis != 0) {
/*      */         try {
/* 1969 */           this.mysqlConnection.setSoTimeout(oldTimeout);
/* 1970 */         } catch (SocketException e) {
/* 1971 */           throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public MysqlIO(String host, int port, Properties props, String socketFactoryClassName, ConnectionImpl conn, int socketTimeout, int useBufferRowSizeThreshold) throws IOException, SQLException {
/* 1978 */     this.statementExecutionDepth = 0; this.connection = conn; if (this.connection.getEnablePacketDebug())
/*      */       this.packetDebugRingBuffer = new LinkedList();  this.traceProtocol = this.connection.getTraceProtocol(); this.useAutoSlowLog = this.connection.getAutoSlowLog(); this.useBufferRowSizeThreshold = useBufferRowSizeThreshold; this.useDirectRowUnpack = this.connection.getUseDirectRowUnpack(); this.logSlowQueries = this.connection.getLogSlowQueries(); this.reusablePacket = new Buffer(1024); this.sendPacket = new Buffer(1024); this.port = port; this.host = host; this.socketFactoryClassName = socketFactoryClassName; this.socketFactory = createSocketFactory(); this.exceptionInterceptor = this.connection.getExceptionInterceptor(); try {
/*      */       this.mysqlConnection = this.socketFactory.connect(this.host, this.port, props); if (socketTimeout != 0)
/*      */         try {
/*      */           this.mysqlConnection.setSoTimeout(socketTimeout);
/*      */         } catch (Exception ex) {}  this.mysqlConnection = this.socketFactory.beforeHandshake(); if (this.connection.getUseReadAheadInput()) {
/*      */         this.mysqlInput = (InputStream)new ReadAheadInputStream(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
/*      */       } else if (this.connection.useUnbufferedInput()) {
/*      */         this.mysqlInput = this.mysqlConnection.getInputStream();
/*      */       } else {
/*      */         this.mysqlInput = new BufferedInputStream(this.mysqlConnection.getInputStream(), 16384);
/*      */       }  this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection.getOutputStream(), 16384);
/*      */       this.isInteractiveClient = this.connection.getInteractiveClient();
/*      */       this.profileSql = this.connection.getProfileSql();
/*      */       this.sessionCalendar = Calendar.getInstance();
/*      */       this.autoGenerateTestcaseScript = this.connection.getAutoGenerateTestcaseScript();
/*      */       this.needToGrabQueryFromPacket = (this.profileSql || this.logSlowQueries || this.autoGenerateTestcaseScript);
/*      */       if (this.connection.getUseNanosForElapsedTime() && Util.nanoTimeAvailable()) {
/*      */         this.useNanosForElapsedTime = true;
/*      */         this.queryTimingUnits = Messages.getString("Nanoseconds");
/*      */       } else {
/*      */         this.queryTimingUnits = Messages.getString("Milliseconds");
/*      */       } 
/*      */       if (this.connection.getLogSlowQueries())
/*      */         calculateSlowQueryThreshold(); 
/*      */     } catch (IOException ioEx) {
/*      */       throw SQLError.createCommunicationsException(this.connection, 0L, 0L, ioEx, getExceptionInterceptor());
/* 2005 */     }  } final ResultSetInternalMethods sqlQueryDirect(StatementImpl callingStatement, String query, String characterEncoding, Buffer queryPacket, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws Exception { this.statementExecutionDepth++;
/*      */     
/*      */     try {
/* 2008 */       if (this.statementInterceptors != null) {
/* 2009 */         ResultSetInternalMethods interceptedResults = invokeStatementInterceptorsPre(query, callingStatement);
/*      */ 
/*      */         
/* 2012 */         if (interceptedResults != null) {
/* 2013 */           return interceptedResults;
/*      */         }
/*      */       } 
/*      */       
/* 2017 */       long queryStartTime = 0L;
/* 2018 */       long queryEndTime = 0L;
/*      */       
/* 2020 */       if (query != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2025 */         int packLength = 5 + query.length() * 2 + 2;
/*      */         
/* 2027 */         String statementComment = this.connection.getStatementComment();
/*      */         
/* 2029 */         byte[] commentAsBytes = null;
/*      */         
/* 2031 */         if (statementComment != null) {
/* 2032 */           commentAsBytes = StringUtils.getBytes(statementComment, (SingleByteCharsetConverter)null, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2037 */           packLength += commentAsBytes.length;
/* 2038 */           packLength += 6;
/*      */         } 
/*      */         
/* 2041 */         if (this.sendPacket == null) {
/* 2042 */           this.sendPacket = new Buffer(packLength);
/*      */         } else {
/* 2044 */           this.sendPacket.clear();
/*      */         } 
/*      */         
/* 2047 */         this.sendPacket.writeByte((byte)3);
/*      */         
/* 2049 */         if (commentAsBytes != null) {
/* 2050 */           this.sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
/* 2051 */           this.sendPacket.writeBytesNoNull(commentAsBytes);
/* 2052 */           this.sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
/*      */         } 
/*      */         
/* 2055 */         if (characterEncoding != null) {
/* 2056 */           if (this.platformDbCharsetMatches) {
/* 2057 */             this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 2062 */           else if (StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
/* 2063 */             this.sendPacket.writeBytesNoNull(query.getBytes());
/*      */           } else {
/* 2065 */             this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2073 */           this.sendPacket.writeStringNoNull(query);
/*      */         } 
/*      */         
/* 2076 */         queryPacket = this.sendPacket;
/*      */       } 
/*      */       
/* 2079 */       byte[] queryBuf = null;
/* 2080 */       int oldPacketPosition = 0;
/*      */       
/* 2082 */       if (this.needToGrabQueryFromPacket) {
/* 2083 */         queryBuf = queryPacket.getByteBuffer();
/*      */ 
/*      */         
/* 2086 */         oldPacketPosition = queryPacket.getPosition();
/*      */         
/* 2088 */         queryStartTime = getCurrentTimeNanosOrMillis();
/*      */       } 
/*      */       
/* 2091 */       if (this.autoGenerateTestcaseScript) {
/* 2092 */         String testcaseQuery = null;
/*      */         
/* 2094 */         if (query != null) {
/* 2095 */           testcaseQuery = query;
/*      */         } else {
/* 2097 */           testcaseQuery = new String(queryBuf, 5, oldPacketPosition - 5);
/*      */         } 
/*      */ 
/*      */         
/* 2101 */         StringBuffer debugBuf = new StringBuffer(testcaseQuery.length() + 32);
/* 2102 */         this.connection.generateConnectionCommentBlock(debugBuf);
/* 2103 */         debugBuf.append(testcaseQuery);
/* 2104 */         debugBuf.append(';');
/* 2105 */         this.connection.dumpTestcaseQuery(debugBuf.toString());
/*      */       } 
/*      */ 
/*      */       
/* 2109 */       Buffer resultPacket = sendCommand(3, null, queryPacket, false, null, 0);
/*      */ 
/*      */       
/* 2112 */       long fetchBeginTime = 0L;
/* 2113 */       long fetchEndTime = 0L;
/*      */       
/* 2115 */       String profileQueryToLog = null;
/*      */       
/* 2117 */       boolean queryWasSlow = false;
/*      */       
/* 2119 */       if (this.profileSql || this.logSlowQueries) {
/* 2120 */         queryEndTime = System.currentTimeMillis();
/*      */         
/* 2122 */         boolean shouldExtractQuery = false;
/*      */         
/* 2124 */         if (this.profileSql) {
/* 2125 */           shouldExtractQuery = true;
/* 2126 */         } else if (this.logSlowQueries) {
/* 2127 */           long queryTime = queryEndTime - queryStartTime;
/*      */           
/* 2129 */           boolean logSlow = false;
/*      */           
/* 2131 */           if (this.useAutoSlowLog) {
/* 2132 */             logSlow = (queryTime > this.connection.getSlowQueryThresholdMillis());
/*      */           } else {
/* 2134 */             logSlow = this.connection.isAbonormallyLongQuery(queryTime);
/*      */             
/* 2136 */             this.connection.reportQueryTime(queryTime);
/*      */           } 
/*      */           
/* 2139 */           if (logSlow) {
/* 2140 */             shouldExtractQuery = true;
/* 2141 */             queryWasSlow = true;
/*      */           } 
/*      */         } 
/*      */         
/* 2145 */         if (shouldExtractQuery) {
/*      */           
/* 2147 */           boolean truncated = false;
/*      */           
/* 2149 */           int extractPosition = oldPacketPosition;
/*      */           
/* 2151 */           if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
/* 2152 */             extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
/* 2153 */             truncated = true;
/*      */           } 
/*      */           
/* 2156 */           profileQueryToLog = new String(queryBuf, 5, extractPosition - 5);
/*      */ 
/*      */           
/* 2159 */           if (truncated) {
/* 2160 */             profileQueryToLog = profileQueryToLog + Messages.getString("MysqlIO.25");
/*      */           }
/*      */         } 
/*      */         
/* 2164 */         fetchBeginTime = queryEndTime;
/*      */       } 
/*      */       
/* 2167 */       ResultSetInternalMethods rs = readAllResults(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, false, -1L, cachedMetadata);
/*      */ 
/*      */ 
/*      */       
/* 2171 */       if (queryWasSlow && !this.serverQueryWasSlow) {
/* 2172 */         StringBuffer mesgBuf = new StringBuffer(48 + profileQueryToLog.length());
/*      */ 
/*      */         
/* 2175 */         mesgBuf.append(Messages.getString("MysqlIO.SlowQuery", new Object[] { new Long(this.slowQueryThreshold), this.queryTimingUnits, new Long(queryEndTime - queryStartTime) }));
/*      */ 
/*      */ 
/*      */         
/* 2179 */         mesgBuf.append(profileQueryToLog);
/*      */         
/* 2181 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2183 */         eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), (int)(queryEndTime - queryStartTime), this.queryTimingUnits, null, new Throwable(), mesgBuf.toString()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2190 */         if (this.connection.getExplainSlowQueries()) {
/* 2191 */           if (oldPacketPosition < 1048576) {
/* 2192 */             explainSlowQuery(queryPacket.getBytes(5, oldPacketPosition - 5), profileQueryToLog);
/*      */           } else {
/*      */             
/* 2195 */             this.connection.getLog().logWarn(Messages.getString("MysqlIO.28") + 1048576 + Messages.getString("MysqlIO.29"));
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2203 */       if (this.logSlowQueries) {
/*      */         
/* 2205 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2207 */         if (this.queryBadIndexUsed) {
/* 2208 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), Messages.getString("MysqlIO.33") + profileQueryToLog));
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
/* 2221 */         if (this.queryNoIndexUsed) {
/* 2222 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), Messages.getString("MysqlIO.35") + profileQueryToLog));
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
/* 2235 */         if (this.serverQueryWasSlow) {
/* 2236 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), Messages.getString("MysqlIO.ServerSlowQuery") + profileQueryToLog));
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
/* 2250 */       if (this.profileSql) {
/* 2251 */         fetchEndTime = getCurrentTimeNanosOrMillis();
/*      */         
/* 2253 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2255 */         eventSink.consumeEvent(new ProfilerEvent((byte)3, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), profileQueryToLog));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2263 */         eventSink.consumeEvent(new ProfilerEvent((byte)5, "", catalog, this.connection.getId(), (callingStatement != null) ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), fetchEndTime - fetchBeginTime, this.queryTimingUnits, null, new Throwable(), null));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2272 */       if (this.hadWarnings) {
/* 2273 */         scanForAndThrowDataTruncation();
/*      */       }
/*      */       
/* 2276 */       if (this.statementInterceptors != null) {
/* 2277 */         ResultSetInternalMethods interceptedResults = invokeStatementInterceptorsPost(query, callingStatement, rs);
/*      */ 
/*      */         
/* 2280 */         if (interceptedResults != null) {
/* 2281 */           rs = interceptedResults;
/*      */         }
/*      */       } 
/*      */       
/* 2285 */       return rs;
/* 2286 */     } catch (SQLException sqlEx) {
/* 2287 */       if (callingStatement != null) {
/* 2288 */         synchronized (callingStatement.cancelTimeoutMutex) {
/* 2289 */           if (callingStatement.wasCancelled) {
/* 2290 */             MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null;
/*      */             
/* 2292 */             if (callingStatement.wasCancelledByTimeout) {
/* 2293 */               MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException();
/*      */             } else {
/* 2295 */               mySQLStatementCancelledException = new MySQLStatementCancelledException();
/*      */             } 
/*      */             
/* 2298 */             callingStatement.resetCancelledState();
/*      */             
/* 2300 */             throw mySQLStatementCancelledException;
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/* 2305 */       throw sqlEx;
/*      */     } finally {
/* 2307 */       this.statementExecutionDepth--;
/*      */     }  }
/*      */ 
/*      */ 
/*      */   
/*      */   private ResultSetInternalMethods invokeStatementInterceptorsPre(String sql, Statement interceptedStatement) throws SQLException {
/* 2313 */     ResultSetInternalMethods previousResultSet = null;
/*      */     
/* 2315 */     Iterator interceptors = this.statementInterceptors.iterator();
/*      */     
/* 2317 */     while (interceptors.hasNext()) {
/* 2318 */       StatementInterceptor interceptor = interceptors.next();
/*      */ 
/*      */       
/* 2321 */       boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
/* 2322 */       boolean shouldExecute = ((executeTopLevelOnly && this.statementExecutionDepth == 1) || !executeTopLevelOnly);
/*      */ 
/*      */       
/* 2325 */       if (shouldExecute) {
/* 2326 */         String sqlToInterceptor = sql;
/*      */         
/* 2328 */         if (interceptedStatement instanceof PreparedStatement) {
/* 2329 */           sqlToInterceptor = ((PreparedStatement)interceptedStatement).asSql();
/*      */         }
/*      */ 
/*      */         
/* 2333 */         ResultSetInternalMethods interceptedResultSet = interceptor.preProcess(sqlToInterceptor, interceptedStatement, this.connection);
/*      */ 
/*      */ 
/*      */         
/* 2337 */         if (interceptedResultSet != null) {
/* 2338 */           previousResultSet = interceptedResultSet;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2343 */     return previousResultSet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ResultSetInternalMethods invokeStatementInterceptorsPost(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet) throws SQLException {
/* 2349 */     Iterator interceptors = this.statementInterceptors.iterator();
/*      */     
/* 2351 */     while (interceptors.hasNext()) {
/* 2352 */       StatementInterceptor interceptor = interceptors.next();
/*      */ 
/*      */       
/* 2355 */       boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
/* 2356 */       boolean shouldExecute = ((executeTopLevelOnly && this.statementExecutionDepth == 1) || !executeTopLevelOnly);
/*      */ 
/*      */       
/* 2359 */       if (shouldExecute) {
/* 2360 */         String sqlToInterceptor = sql;
/*      */         
/* 2362 */         if (interceptedStatement instanceof PreparedStatement) {
/* 2363 */           sqlToInterceptor = ((PreparedStatement)interceptedStatement).asSql();
/*      */         }
/*      */ 
/*      */         
/* 2367 */         ResultSetInternalMethods interceptedResultSet = interceptor.postProcess(sqlToInterceptor, interceptedStatement, originalResultSet, this.connection);
/*      */ 
/*      */ 
/*      */         
/* 2371 */         if (interceptedResultSet != null) {
/* 2372 */           originalResultSet = interceptedResultSet;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2377 */     return originalResultSet;
/*      */   }
/*      */   
/*      */   private void calculateSlowQueryThreshold() {
/* 2381 */     this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
/*      */     
/* 2383 */     if (this.connection.getUseNanosForElapsedTime()) {
/* 2384 */       long nanosThreshold = this.connection.getSlowQueryThresholdNanos();
/*      */       
/* 2386 */       if (nanosThreshold != 0L) {
/* 2387 */         this.slowQueryThreshold = nanosThreshold;
/*      */       } else {
/* 2389 */         this.slowQueryThreshold *= 1000000L;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected long getCurrentTimeNanosOrMillis() {
/* 2395 */     if (this.useNanosForElapsedTime) {
/* 2396 */       return Util.getCurrentTimeNanosOrMillis();
/*      */     }
/*      */     
/* 2399 */     return System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getHost() {
/* 2408 */     return this.host;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isVersion(int major, int minor, int subminor) {
/* 2423 */     return (major == getServerMajorVersion() && minor == getServerMinorVersion() && subminor == getServerSubMinorVersion());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean versionMeetsMinimum(int major, int minor, int subminor) {
/* 2439 */     if (getServerMajorVersion() >= major) {
/* 2440 */       if (getServerMajorVersion() == major) {
/* 2441 */         if (getServerMinorVersion() >= minor) {
/* 2442 */           if (getServerMinorVersion() == minor) {
/* 2443 */             return (getServerSubMinorVersion() >= subminor);
/*      */           }
/*      */ 
/*      */           
/* 2447 */           return true;
/*      */         } 
/*      */ 
/*      */         
/* 2451 */         return false;
/*      */       } 
/*      */ 
/*      */       
/* 2455 */       return true;
/*      */     } 
/*      */     
/* 2458 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String getPacketDumpToLog(Buffer packetToDump, int packetLength) {
/* 2472 */     if (packetLength < 1024) {
/* 2473 */       return packetToDump.dump(packetLength);
/*      */     }
/*      */     
/* 2476 */     StringBuffer packetDumpBuf = new StringBuffer(4096);
/* 2477 */     packetDumpBuf.append(packetToDump.dump(1024));
/* 2478 */     packetDumpBuf.append(Messages.getString("MysqlIO.36"));
/* 2479 */     packetDumpBuf.append(1024);
/* 2480 */     packetDumpBuf.append(Messages.getString("MysqlIO.37"));
/*      */     
/* 2482 */     return packetDumpBuf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private final int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
/* 2487 */     if (len < 0) {
/* 2488 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/* 2491 */     int n = 0;
/*      */     
/* 2493 */     while (n < len) {
/* 2494 */       int count = in.read(b, off + n, len - n);
/*      */       
/* 2496 */       if (count < 0) {
/* 2497 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { new Integer(len), new Integer(n) }));
/*      */       }
/*      */ 
/*      */       
/* 2501 */       n += count;
/*      */     } 
/*      */     
/* 2504 */     return n;
/*      */   }
/*      */   
/*      */   private final long skipFully(InputStream in, long len) throws IOException {
/* 2508 */     if (len < 0L) {
/* 2509 */       throw new IOException("Negative skip length not allowed");
/*      */     }
/*      */     
/* 2512 */     long n = 0L;
/*      */     
/* 2514 */     while (n < len) {
/* 2515 */       long count = in.skip(len - n);
/*      */       
/* 2517 */       if (count < 0L) {
/* 2518 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { new Long(len), new Long(n) }));
/*      */       }
/*      */ 
/*      */       
/* 2522 */       n += count;
/*      */     } 
/*      */     
/* 2525 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ResultSetImpl readResultsForQueryOrUpdate(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache) throws SQLException {
/* 2553 */     long columnCount = resultPacket.readFieldLength();
/*      */     
/* 2555 */     if (columnCount == 0L)
/* 2556 */       return buildResultSetWithUpdates(callingStatement, resultPacket); 
/* 2557 */     if (columnCount == -1L) {
/* 2558 */       String charEncoding = null;
/*      */       
/* 2560 */       if (this.connection.getUseUnicode()) {
/* 2561 */         charEncoding = this.connection.getEncoding();
/*      */       }
/*      */       
/* 2564 */       String fileName = null;
/*      */       
/* 2566 */       if (this.platformDbCharsetMatches) {
/* 2567 */         fileName = (charEncoding != null) ? resultPacket.readString(charEncoding, getExceptionInterceptor()) : resultPacket.readString();
/*      */       }
/*      */       else {
/*      */         
/* 2571 */         fileName = resultPacket.readString();
/*      */       } 
/*      */       
/* 2574 */       return sendFileToServer(callingStatement, fileName);
/*      */     } 
/* 2576 */     ResultSetImpl results = getResultSet(callingStatement, columnCount, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, isBinaryEncoded, metadataFromCache);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2581 */     return results;
/*      */   }
/*      */ 
/*      */   
/*      */   private int alignPacketSize(int a, int l) {
/* 2586 */     return a + l - 1 & (l - 1 ^ 0xFFFFFFFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResultSetImpl buildResultSetWithRows(StatementImpl callingStatement, String catalog, Field[] fields, RowData rows, int resultSetType, int resultSetConcurrency, boolean isBinaryEncoded) throws SQLException {
/* 2594 */     ResultSetImpl rs = null;
/*      */     
/* 2596 */     switch (resultSetConcurrency) {
/*      */       case 1007:
/* 2598 */         rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
/*      */ 
/*      */         
/* 2601 */         if (isBinaryEncoded) {
/* 2602 */           rs.setBinaryEncoded();
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
/* 2618 */         rs.setResultSetType(resultSetType);
/* 2619 */         rs.setResultSetConcurrency(resultSetConcurrency);
/*      */         
/* 2621 */         return rs;case 1008: rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, true); rs.setResultSetType(resultSetType); rs.setResultSetConcurrency(resultSetConcurrency); return rs;
/*      */     } 
/*      */     return ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
/*      */   }
/*      */   
/*      */   private ResultSetImpl buildResultSetWithUpdates(StatementImpl callingStatement, Buffer resultPacket) throws SQLException {
/* 2627 */     long updateCount = -1L;
/* 2628 */     long updateID = -1L;
/* 2629 */     String info = null;
/*      */     
/*      */     try {
/* 2632 */       if (this.useNewUpdateCounts) {
/* 2633 */         updateCount = resultPacket.newReadLength();
/* 2634 */         updateID = resultPacket.newReadLength();
/*      */       } else {
/* 2636 */         updateCount = resultPacket.readLength();
/* 2637 */         updateID = resultPacket.readLength();
/*      */       } 
/*      */       
/* 2640 */       if (this.use41Extensions) {
/*      */         
/* 2642 */         this.serverStatus = resultPacket.readInt();
/*      */         
/* 2644 */         checkTransactionState(this.oldServerStatus);
/*      */         
/* 2646 */         this.warningCount = resultPacket.readInt();
/*      */         
/* 2648 */         if (this.warningCount > 0) {
/* 2649 */           this.hadWarnings = true;
/*      */         }
/*      */         
/* 2652 */         resultPacket.readByte();
/*      */         
/* 2654 */         setServerSlowQueryFlags();
/*      */       } 
/*      */       
/* 2657 */       if (this.connection.isReadInfoMsgEnabled()) {
/* 2658 */         info = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */       }
/* 2660 */     } catch (Exception ex) {
/* 2661 */       SQLException sqlEx = SQLError.createSQLException(SQLError.get("S1000"), "S1000", -1, getExceptionInterceptor());
/*      */       
/* 2663 */       sqlEx.initCause(ex);
/*      */       
/* 2665 */       throw sqlEx;
/*      */     } 
/*      */     
/* 2668 */     ResultSetInternalMethods updateRs = ResultSetImpl.getInstance(updateCount, updateID, this.connection, callingStatement);
/*      */ 
/*      */     
/* 2671 */     if (info != null) {
/* 2672 */       ((ResultSetImpl)updateRs).setServerInfo(info);
/*      */     }
/*      */     
/* 2675 */     return (ResultSetImpl)updateRs;
/*      */   }
/*      */   
/*      */   private void setServerSlowQueryFlags() {
/* 2679 */     if (this.profileSql) {
/* 2680 */       this.queryNoIndexUsed = ((this.serverStatus & 0x10) != 0);
/*      */       
/* 2682 */       this.queryBadIndexUsed = ((this.serverStatus & 0x20) != 0);
/*      */       
/* 2684 */       this.serverQueryWasSlow = ((this.serverStatus & 0x800) != 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkForOutstandingStreamingData() throws SQLException {
/* 2690 */     if (this.streamingData != null) {
/* 2691 */       boolean shouldClobber = this.connection.getClobberStreamingResults();
/*      */       
/* 2693 */       if (!shouldClobber) {
/* 2694 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"), getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2702 */       this.streamingData.getOwner().realClose(false);
/*      */ 
/*      */       
/* 2705 */       clearInputStream();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Buffer compressPacket(Buffer packet, int offset, int packetLen, int headerLength) throws SQLException {
/* 2711 */     packet.writeLongInt(packetLen - headerLength);
/* 2712 */     packet.writeByte((byte)0);
/*      */     
/* 2714 */     int lengthToWrite = 0;
/* 2715 */     int compressedLength = 0;
/* 2716 */     byte[] bytesToCompress = packet.getByteBuffer();
/* 2717 */     byte[] compressedBytes = null;
/* 2718 */     int offsetWrite = 0;
/*      */     
/* 2720 */     if (packetLen < 50) {
/* 2721 */       lengthToWrite = packetLen;
/* 2722 */       compressedBytes = packet.getByteBuffer();
/* 2723 */       compressedLength = 0;
/* 2724 */       offsetWrite = offset;
/*      */     } else {
/* 2726 */       compressedBytes = new byte[bytesToCompress.length * 2];
/*      */       
/* 2728 */       this.deflater.reset();
/* 2729 */       this.deflater.setInput(bytesToCompress, offset, packetLen);
/* 2730 */       this.deflater.finish();
/*      */       
/* 2732 */       int compLen = this.deflater.deflate(compressedBytes);
/*      */       
/* 2734 */       if (compLen > packetLen) {
/* 2735 */         lengthToWrite = packetLen;
/* 2736 */         compressedBytes = packet.getByteBuffer();
/* 2737 */         compressedLength = 0;
/* 2738 */         offsetWrite = offset;
/*      */       } else {
/* 2740 */         lengthToWrite = compLen;
/* 2741 */         headerLength += 3;
/* 2742 */         compressedLength = packetLen;
/*      */       } 
/*      */     } 
/*      */     
/* 2746 */     Buffer compressedPacket = new Buffer(packetLen + headerLength);
/*      */     
/* 2748 */     compressedPacket.setPosition(0);
/* 2749 */     compressedPacket.writeLongInt(lengthToWrite);
/* 2750 */     compressedPacket.writeByte(this.packetSequence);
/* 2751 */     compressedPacket.writeLongInt(compressedLength);
/* 2752 */     compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite, lengthToWrite);
/*      */ 
/*      */     
/* 2755 */     return compressedPacket;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void readServerStatusForResultSets(Buffer rowPacket) throws SQLException {
/* 2760 */     if (this.use41Extensions) {
/* 2761 */       rowPacket.readByte();
/*      */       
/* 2763 */       this.warningCount = rowPacket.readInt();
/*      */       
/* 2765 */       if (this.warningCount > 0) {
/* 2766 */         this.hadWarnings = true;
/*      */       }
/*      */       
/* 2769 */       this.oldServerStatus = this.serverStatus;
/* 2770 */       this.serverStatus = rowPacket.readInt();
/* 2771 */       checkTransactionState(this.oldServerStatus);
/*      */       
/* 2773 */       setServerSlowQueryFlags();
/*      */     } 
/*      */   }
/*      */   
/*      */   private SocketFactory createSocketFactory() throws SQLException {
/*      */     try {
/* 2779 */       if (this.socketFactoryClassName == null) {
/* 2780 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), "08001", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */       
/* 2784 */       return (SocketFactory)Class.forName(this.socketFactoryClassName).newInstance();
/*      */     }
/* 2786 */     catch (Exception ex) {
/* 2787 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.76") + this.socketFactoryClassName + Messages.getString("MysqlIO.77"), "08001", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2792 */       sqlEx.initCause(ex);
/*      */       
/* 2794 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void enqueuePacketForDebugging(boolean isPacketBeingSent, boolean isPacketReused, int sendLength, byte[] header, Buffer packet) throws SQLException {
/* 2801 */     if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize()) {
/* 2802 */       this.packetDebugRingBuffer.removeFirst();
/*      */     }
/*      */     
/* 2805 */     StringBuffer packetDump = null;
/*      */     
/* 2807 */     if (!isPacketBeingSent) {
/* 2808 */       int bytesToDump = Math.min(1024, packet.getBufLength());
/*      */ 
/*      */       
/* 2811 */       Buffer packetToDump = new Buffer(4 + bytesToDump);
/*      */       
/* 2813 */       packetToDump.setPosition(0);
/* 2814 */       packetToDump.writeBytesNoNull(header);
/* 2815 */       packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));
/*      */       
/* 2817 */       String packetPayload = packetToDump.dump(bytesToDump);
/*      */       
/* 2819 */       packetDump = new StringBuffer(96 + packetPayload.length());
/*      */       
/* 2821 */       packetDump.append("Server ");
/*      */       
/* 2823 */       if (isPacketReused) {
/* 2824 */         packetDump.append("(re-used)");
/*      */       } else {
/* 2826 */         packetDump.append("(new)");
/*      */       } 
/*      */       
/* 2829 */       packetDump.append(" ");
/* 2830 */       packetDump.append(packet.toSuperString());
/* 2831 */       packetDump.append(" --------------------> Client\n");
/* 2832 */       packetDump.append("\nPacket payload:\n\n");
/* 2833 */       packetDump.append(packetPayload);
/*      */       
/* 2835 */       if (bytesToDump == 1024) {
/* 2836 */         packetDump.append("\nNote: Packet of " + packet.getBufLength() + " bytes truncated to " + 'Ѐ' + " bytes.\n");
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 2841 */       int bytesToDump = Math.min(1024, sendLength);
/*      */       
/* 2843 */       String packetPayload = packet.dump(bytesToDump);
/*      */       
/* 2845 */       packetDump = new StringBuffer(68 + packetPayload.length());
/*      */       
/* 2847 */       packetDump.append("Client ");
/* 2848 */       packetDump.append(packet.toSuperString());
/* 2849 */       packetDump.append("--------------------> Server\n");
/* 2850 */       packetDump.append("\nPacket payload:\n\n");
/* 2851 */       packetDump.append(packetPayload);
/*      */       
/* 2853 */       if (bytesToDump == 1024) {
/* 2854 */         packetDump.append("\nNote: Packet of " + sendLength + " bytes truncated to " + 'Ѐ' + " bytes.\n");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2860 */     this.packetDebugRingBuffer.addLast(packetDump);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private RowData readSingleRowSet(long columnCount, int maxRows, int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields) throws SQLException {
/* 2867 */     ArrayList rows = new ArrayList();
/*      */     
/* 2869 */     boolean useBufferRowExplicit = useBufferRowExplicit(fields);
/*      */ 
/*      */     
/* 2872 */     ResultSetRow row = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
/*      */ 
/*      */     
/* 2875 */     int rowCount = 0;
/*      */     
/* 2877 */     if (row != null) {
/* 2878 */       rows.add(row);
/* 2879 */       rowCount = 1;
/*      */     } 
/*      */     
/* 2882 */     while (row != null) {
/* 2883 */       row = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
/*      */ 
/*      */       
/* 2886 */       if (row != null && (
/* 2887 */         maxRows == -1 || rowCount < maxRows)) {
/* 2888 */         rows.add(row);
/* 2889 */         rowCount++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2894 */     RowData rowData = new RowDataStatic(rows);
/*      */     
/* 2896 */     return rowData;
/*      */   }
/*      */   
/*      */   public static boolean useBufferRowExplicit(Field[] fields) {
/* 2900 */     if (fields == null) {
/* 2901 */       return false;
/*      */     }
/*      */     
/* 2904 */     for (int i = 0; i < fields.length; i++) {
/* 2905 */       switch (fields[i].getSQLType()) {
/*      */         case -4:
/*      */         case -1:
/*      */         case 2004:
/*      */         case 2005:
/* 2910 */           return true;
/*      */       } 
/*      */     
/*      */     } 
/* 2914 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reclaimLargeReusablePacket() {
/* 2921 */     if (this.reusablePacket != null && this.reusablePacket.getCapacity() > 1048576)
/*      */     {
/* 2923 */       this.reusablePacket = new Buffer(1024);
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
/*      */   private final Buffer reuseAndReadPacket(Buffer reuse) throws SQLException {
/* 2938 */     return reuseAndReadPacket(reuse, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Buffer reuseAndReadPacket(Buffer reuse, int existingPacketLength) throws SQLException {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: iconst_0
/*      */     //   2: invokevirtual setWasMultiPacket : (Z)V
/*      */     //   5: iconst_0
/*      */     //   6: istore_3
/*      */     //   7: iload_2
/*      */     //   8: iconst_m1
/*      */     //   9: if_icmpne -> 94
/*      */     //   12: aload_0
/*      */     //   13: aload_0
/*      */     //   14: getfield mysqlInput : Ljava/io/InputStream;
/*      */     //   17: aload_0
/*      */     //   18: getfield packetHeaderBuf : [B
/*      */     //   21: iconst_0
/*      */     //   22: iconst_4
/*      */     //   23: invokespecial readFully : (Ljava/io/InputStream;[BII)I
/*      */     //   26: istore #4
/*      */     //   28: iload #4
/*      */     //   30: iconst_4
/*      */     //   31: if_icmpge -> 52
/*      */     //   34: aload_0
/*      */     //   35: invokevirtual forceClose : ()V
/*      */     //   38: new java/io/IOException
/*      */     //   41: dup
/*      */     //   42: ldc_w 'MysqlIO.43'
/*      */     //   45: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   48: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   51: athrow
/*      */     //   52: aload_0
/*      */     //   53: getfield packetHeaderBuf : [B
/*      */     //   56: iconst_0
/*      */     //   57: baload
/*      */     //   58: sipush #255
/*      */     //   61: iand
/*      */     //   62: aload_0
/*      */     //   63: getfield packetHeaderBuf : [B
/*      */     //   66: iconst_1
/*      */     //   67: baload
/*      */     //   68: sipush #255
/*      */     //   71: iand
/*      */     //   72: bipush #8
/*      */     //   74: ishl
/*      */     //   75: iadd
/*      */     //   76: aload_0
/*      */     //   77: getfield packetHeaderBuf : [B
/*      */     //   80: iconst_2
/*      */     //   81: baload
/*      */     //   82: sipush #255
/*      */     //   85: iand
/*      */     //   86: bipush #16
/*      */     //   88: ishl
/*      */     //   89: iadd
/*      */     //   90: istore_3
/*      */     //   91: goto -> 96
/*      */     //   94: iload_2
/*      */     //   95: istore_3
/*      */     //   96: aload_0
/*      */     //   97: getfield traceProtocol : Z
/*      */     //   100: ifeq -> 174
/*      */     //   103: new java/lang/StringBuffer
/*      */     //   106: dup
/*      */     //   107: invokespecial <init> : ()V
/*      */     //   110: astore #4
/*      */     //   112: aload #4
/*      */     //   114: ldc_w 'MysqlIO.44'
/*      */     //   117: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   123: pop
/*      */     //   124: aload #4
/*      */     //   126: iload_3
/*      */     //   127: invokevirtual append : (I)Ljava/lang/StringBuffer;
/*      */     //   130: pop
/*      */     //   131: aload #4
/*      */     //   133: ldc_w 'MysqlIO.45'
/*      */     //   136: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   142: pop
/*      */     //   143: aload #4
/*      */     //   145: aload_0
/*      */     //   146: getfield packetHeaderBuf : [B
/*      */     //   149: iconst_4
/*      */     //   150: invokestatic dumpAsHex : ([BI)Ljava/lang/String;
/*      */     //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   156: pop
/*      */     //   157: aload_0
/*      */     //   158: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   161: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
/*      */     //   164: aload #4
/*      */     //   166: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   169: invokeinterface logTrace : (Ljava/lang/Object;)V
/*      */     //   174: aload_0
/*      */     //   175: getfield packetHeaderBuf : [B
/*      */     //   178: iconst_3
/*      */     //   179: baload
/*      */     //   180: istore #4
/*      */     //   182: aload_0
/*      */     //   183: getfield packetSequenceReset : Z
/*      */     //   186: ifne -> 212
/*      */     //   189: aload_0
/*      */     //   190: getfield enablePacketDebug : Z
/*      */     //   193: ifeq -> 217
/*      */     //   196: aload_0
/*      */     //   197: getfield checkPacketSequence : Z
/*      */     //   200: ifeq -> 217
/*      */     //   203: aload_0
/*      */     //   204: iload #4
/*      */     //   206: invokespecial checkPacketSequencing : (B)V
/*      */     //   209: goto -> 217
/*      */     //   212: aload_0
/*      */     //   213: iconst_0
/*      */     //   214: putfield packetSequenceReset : Z
/*      */     //   217: aload_0
/*      */     //   218: iload #4
/*      */     //   220: putfield readPacketSequence : B
/*      */     //   223: aload_1
/*      */     //   224: iconst_0
/*      */     //   225: invokevirtual setPosition : (I)V
/*      */     //   228: aload_1
/*      */     //   229: invokevirtual getByteBuffer : ()[B
/*      */     //   232: arraylength
/*      */     //   233: iload_3
/*      */     //   234: if_icmpgt -> 246
/*      */     //   237: aload_1
/*      */     //   238: iload_3
/*      */     //   239: iconst_1
/*      */     //   240: iadd
/*      */     //   241: newarray byte
/*      */     //   243: invokevirtual setByteBuffer : ([B)V
/*      */     //   246: aload_1
/*      */     //   247: iload_3
/*      */     //   248: invokevirtual setBufLength : (I)V
/*      */     //   251: aload_0
/*      */     //   252: aload_0
/*      */     //   253: getfield mysqlInput : Ljava/io/InputStream;
/*      */     //   256: aload_1
/*      */     //   257: invokevirtual getByteBuffer : ()[B
/*      */     //   260: iconst_0
/*      */     //   261: iload_3
/*      */     //   262: invokespecial readFully : (Ljava/io/InputStream;[BII)I
/*      */     //   265: istore #5
/*      */     //   267: iload #5
/*      */     //   269: iload_3
/*      */     //   270: if_icmpeq -> 310
/*      */     //   273: new java/io/IOException
/*      */     //   276: dup
/*      */     //   277: new java/lang/StringBuffer
/*      */     //   280: dup
/*      */     //   281: invokespecial <init> : ()V
/*      */     //   284: ldc 'Short read, expected '
/*      */     //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   289: iload_3
/*      */     //   290: invokevirtual append : (I)Ljava/lang/StringBuffer;
/*      */     //   293: ldc ' bytes, only read '
/*      */     //   295: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   298: iload #5
/*      */     //   300: invokevirtual append : (I)Ljava/lang/StringBuffer;
/*      */     //   303: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   306: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   309: athrow
/*      */     //   310: aload_0
/*      */     //   311: getfield traceProtocol : Z
/*      */     //   314: ifeq -> 366
/*      */     //   317: new java/lang/StringBuffer
/*      */     //   320: dup
/*      */     //   321: invokespecial <init> : ()V
/*      */     //   324: astore #6
/*      */     //   326: aload #6
/*      */     //   328: ldc_w 'MysqlIO.46'
/*      */     //   331: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   334: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   337: pop
/*      */     //   338: aload #6
/*      */     //   340: aload_1
/*      */     //   341: iload_3
/*      */     //   342: invokestatic getPacketDumpToLog : (Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
/*      */     //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   348: pop
/*      */     //   349: aload_0
/*      */     //   350: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   353: invokevirtual getLog : ()Lcom/mysql/jdbc/log/Log;
/*      */     //   356: aload #6
/*      */     //   358: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   361: invokeinterface logTrace : (Ljava/lang/Object;)V
/*      */     //   366: aload_0
/*      */     //   367: getfield enablePacketDebug : Z
/*      */     //   370: ifeq -> 385
/*      */     //   373: aload_0
/*      */     //   374: iconst_0
/*      */     //   375: iconst_1
/*      */     //   376: iconst_0
/*      */     //   377: aload_0
/*      */     //   378: getfield packetHeaderBuf : [B
/*      */     //   381: aload_1
/*      */     //   382: invokespecial enqueuePacketForDebugging : (ZZI[BLcom/mysql/jdbc/Buffer;)V
/*      */     //   385: iconst_0
/*      */     //   386: istore #6
/*      */     //   388: iload_3
/*      */     //   389: aload_0
/*      */     //   390: getfield maxThreeBytes : I
/*      */     //   393: if_icmpne -> 420
/*      */     //   396: aload_1
/*      */     //   397: aload_0
/*      */     //   398: getfield maxThreeBytes : I
/*      */     //   401: invokevirtual setPosition : (I)V
/*      */     //   404: iload_3
/*      */     //   405: istore #7
/*      */     //   407: iconst_1
/*      */     //   408: istore #6
/*      */     //   410: aload_0
/*      */     //   411: aload_1
/*      */     //   412: iload #4
/*      */     //   414: iload #7
/*      */     //   416: invokespecial readRemainingMultiPackets : (Lcom/mysql/jdbc/Buffer;BI)I
/*      */     //   419: istore_3
/*      */     //   420: iload #6
/*      */     //   422: ifne -> 432
/*      */     //   425: aload_1
/*      */     //   426: invokevirtual getByteBuffer : ()[B
/*      */     //   429: iload_3
/*      */     //   430: iconst_0
/*      */     //   431: bastore
/*      */     //   432: aload_0
/*      */     //   433: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   436: invokevirtual getMaintainTimeStats : ()Z
/*      */     //   439: ifeq -> 449
/*      */     //   442: aload_0
/*      */     //   443: invokestatic currentTimeMillis : ()J
/*      */     //   446: putfield lastPacketReceivedTimeMs : J
/*      */     //   449: aload_1
/*      */     //   450: areturn
/*      */     //   451: astore_3
/*      */     //   452: aload_0
/*      */     //   453: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   456: aload_0
/*      */     //   457: getfield lastPacketSentTimeMs : J
/*      */     //   460: aload_0
/*      */     //   461: getfield lastPacketReceivedTimeMs : J
/*      */     //   464: aload_3
/*      */     //   465: aload_0
/*      */     //   466: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   469: invokestatic createCommunicationsException : (Lcom/mysql/jdbc/ConnectionImpl;JJLjava/lang/Exception;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   472: athrow
/*      */     //   473: astore_3
/*      */     //   474: aload_0
/*      */     //   475: invokevirtual clearInputStream : ()V
/*      */     //   478: jsr -> 492
/*      */     //   481: goto -> 511
/*      */     //   484: astore #8
/*      */     //   486: jsr -> 492
/*      */     //   489: aload #8
/*      */     //   491: athrow
/*      */     //   492: astore #9
/*      */     //   494: aload_0
/*      */     //   495: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */     //   498: iconst_0
/*      */     //   499: iconst_0
/*      */     //   500: iconst_1
/*      */     //   501: aload_3
/*      */     //   502: invokevirtual realClose : (ZZZLjava/lang/Throwable;)V
/*      */     //   505: aload_3
/*      */     //   506: athrow
/*      */     //   507: astore #10
/*      */     //   509: aload_3
/*      */     //   510: athrow
/*      */     //   511: goto -> 511
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2945	-> 0
/*      */     //   #2946	-> 5
/*      */     //   #2948	-> 7
/*      */     //   #2949	-> 12
/*      */     //   #2952	-> 28
/*      */     //   #2953	-> 34
/*      */     //   #2954	-> 38
/*      */     //   #2957	-> 52
/*      */     //   #2961	-> 94
/*      */     //   #2964	-> 96
/*      */     //   #2965	-> 103
/*      */     //   #2967	-> 112
/*      */     //   #2968	-> 124
/*      */     //   #2969	-> 131
/*      */     //   #2970	-> 143
/*      */     //   #2973	-> 157
/*      */     //   #2976	-> 174
/*      */     //   #2978	-> 182
/*      */     //   #2979	-> 189
/*      */     //   #2980	-> 203
/*      */     //   #2983	-> 212
/*      */     //   #2986	-> 217
/*      */     //   #2989	-> 223
/*      */     //   #2997	-> 228
/*      */     //   #2998	-> 237
/*      */     //   #3002	-> 246
/*      */     //   #3005	-> 251
/*      */     //   #3008	-> 267
/*      */     //   #3009	-> 273
/*      */     //   #3013	-> 310
/*      */     //   #3014	-> 317
/*      */     //   #3016	-> 326
/*      */     //   #3017	-> 338
/*      */     //   #3020	-> 349
/*      */     //   #3023	-> 366
/*      */     //   #3024	-> 373
/*      */     //   #3028	-> 385
/*      */     //   #3030	-> 388
/*      */     //   #3031	-> 396
/*      */     //   #3033	-> 404
/*      */     //   #3036	-> 407
/*      */     //   #3038	-> 410
/*      */     //   #3042	-> 420
/*      */     //   #3043	-> 425
/*      */     //   #3046	-> 432
/*      */     //   #3047	-> 442
/*      */     //   #3050	-> 449
/*      */     //   #3051	-> 451
/*      */     //   #3052	-> 452
/*      */     //   #3054	-> 473
/*      */     //   #3057	-> 474
/*      */     //   #3058	-> 478
/*      */     //   #3064	-> 481
/*      */     //   #3059	-> 484
/*      */     //   #3060	-> 494
/*      */     //   #3062	-> 505
/*      */     //   #3067	-> 511
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   28	63	4	lengthRead	I
/*      */     //   112	62	4	traceMessageBuf	Ljava/lang/StringBuffer;
/*      */     //   326	40	6	traceMessageBuf	Ljava/lang/StringBuffer;
/*      */     //   407	13	7	packetEndPoint	I
/*      */     //   7	444	3	packetLength	I
/*      */     //   182	269	4	multiPacketSeq	B
/*      */     //   267	184	5	numBytesRead	I
/*      */     //   388	63	6	isMultiPacket	Z
/*      */     //   452	21	3	ioEx	Ljava/io/IOException;
/*      */     //   474	37	3	oom	Ljava/lang/OutOfMemoryError;
/*      */     //   0	514	0	this	Lcom/mysql/jdbc/MysqlIO;
/*      */     //   0	514	1	reuse	Lcom/mysql/jdbc/Buffer;
/*      */     //   0	514	2	existingPacketLength	I
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	450	451	java/io/IOException
/*      */     //   0	450	473	java/lang/OutOfMemoryError
/*      */     //   474	481	484	finally
/*      */     //   484	489	484	finally
/*      */     //   494	505	507	finally
/*      */     //   507	509	507	finally
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readRemainingMultiPackets(Buffer reuse, byte multiPacketSeq, int packetEndPoint) throws IOException, SQLException {
/* 3073 */     int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */ 
/*      */     
/* 3076 */     if (lengthRead < 4) {
/* 3077 */       forceClose();
/* 3078 */       throw new IOException(Messages.getString("MysqlIO.47"));
/*      */     } 
/*      */     
/* 3081 */     int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */ 
/*      */ 
/*      */     
/* 3085 */     Buffer multiPacket = new Buffer(packetLength);
/* 3086 */     boolean firstMultiPkt = true;
/*      */     
/*      */     while (true) {
/* 3089 */       if (!firstMultiPkt) {
/* 3090 */         lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */ 
/*      */         
/* 3093 */         if (lengthRead < 4) {
/* 3094 */           forceClose();
/* 3095 */           throw new IOException(Messages.getString("MysqlIO.48"));
/*      */         } 
/*      */ 
/*      */         
/* 3099 */         packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       }
/*      */       else {
/*      */         
/* 3103 */         firstMultiPkt = false;
/*      */       } 
/*      */       
/* 3106 */       if (!this.useNewLargePackets && packetLength == 1) {
/* 3107 */         clearInputStream();
/*      */         break;
/*      */       } 
/* 3110 */       if (packetLength < this.maxThreeBytes) {
/* 3111 */         byte b = this.packetHeaderBuf[3];
/*      */         
/* 3113 */         if (b != multiPacketSeq + 1) {
/* 3114 */           throw new IOException(Messages.getString("MysqlIO.49"));
/*      */         }
/*      */ 
/*      */         
/* 3118 */         multiPacketSeq = b;
/*      */ 
/*      */         
/* 3121 */         multiPacket.setPosition(0);
/*      */ 
/*      */         
/* 3124 */         multiPacket.setBufLength(packetLength);
/*      */ 
/*      */         
/* 3127 */         byte[] arrayOfByte = multiPacket.getByteBuffer();
/* 3128 */         int i = packetLength;
/*      */         
/* 3130 */         int j = readFully(this.mysqlInput, arrayOfByte, 0, packetLength);
/*      */ 
/*      */         
/* 3133 */         if (j != i) {
/* 3134 */           throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.50") + i + Messages.getString("MysqlIO.51") + j + ".", getExceptionInterceptor()), getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3144 */         reuse.writeBytesNoNull(arrayOfByte, 0, i);
/*      */         
/* 3146 */         packetEndPoint += i;
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 3151 */       byte newPacketSeq = this.packetHeaderBuf[3];
/*      */       
/* 3153 */       if (newPacketSeq != multiPacketSeq + 1) {
/* 3154 */         throw new IOException(Messages.getString("MysqlIO.53"));
/*      */       }
/*      */ 
/*      */       
/* 3158 */       multiPacketSeq = newPacketSeq;
/*      */ 
/*      */       
/* 3161 */       multiPacket.setPosition(0);
/*      */ 
/*      */       
/* 3164 */       multiPacket.setBufLength(packetLength);
/*      */ 
/*      */       
/* 3167 */       byte[] byteBuf = multiPacket.getByteBuffer();
/* 3168 */       int lengthToWrite = packetLength;
/*      */       
/* 3170 */       int bytesRead = readFully(this.mysqlInput, byteBuf, 0, packetLength);
/*      */ 
/*      */       
/* 3173 */       if (bytesRead != lengthToWrite) {
/* 3174 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.54") + lengthToWrite + Messages.getString("MysqlIO.55") + bytesRead + ".", getExceptionInterceptor()), getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3183 */       reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
/*      */       
/* 3185 */       packetEndPoint += lengthToWrite;
/*      */     } 
/*      */     
/* 3188 */     reuse.setPosition(0);
/* 3189 */     reuse.setWasMultiPacket(true);
/* 3190 */     return packetLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkPacketSequencing(byte multiPacketSeq) throws SQLException {
/* 3199 */     if (multiPacketSeq == Byte.MIN_VALUE && this.readPacketSequence != Byte.MAX_VALUE) {
/* 3200 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -128, but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3206 */     if (this.readPacketSequence == -1 && multiPacketSeq != 0) {
/* 3207 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -1, but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3213 */     if (multiPacketSeq != Byte.MIN_VALUE && this.readPacketSequence != -1 && multiPacketSeq != this.readPacketSequence + 1)
/*      */     {
/* 3215 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # " + (this.readPacketSequence + 1) + ", but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void enableMultiQueries() throws SQLException {
/* 3224 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 3226 */     buf.clear();
/* 3227 */     buf.writeByte((byte)27);
/* 3228 */     buf.writeInt(0);
/* 3229 */     sendCommand(27, null, buf, false, null, 0);
/*      */   }
/*      */   
/*      */   void disableMultiQueries() throws SQLException {
/* 3233 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 3235 */     buf.clear();
/* 3236 */     buf.writeByte((byte)27);
/* 3237 */     buf.writeInt(1);
/* 3238 */     sendCommand(27, null, buf, false, null, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private final void send(Buffer packet, int packetLen) throws SQLException {
/*      */     try {
/* 3244 */       if (this.maxAllowedPacket > 0 && packetLen > this.maxAllowedPacket) {
/* 3245 */         throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
/*      */       }
/*      */       
/* 3248 */       if (this.serverMajorVersion >= 4 && packetLen >= this.maxThreeBytes) {
/*      */         
/* 3250 */         sendSplitPackets(packet);
/*      */       } else {
/* 3252 */         this.packetSequence = (byte)(this.packetSequence + 1);
/*      */         
/* 3254 */         Buffer packetToSend = packet;
/*      */         
/* 3256 */         packetToSend.setPosition(0);
/*      */         
/* 3258 */         if (this.useCompression) {
/* 3259 */           int originalPacketLen = packetLen;
/*      */           
/* 3261 */           packetToSend = compressPacket(packet, 0, packetLen, 4);
/*      */           
/* 3263 */           packetLen = packetToSend.getPosition();
/*      */           
/* 3265 */           if (this.traceProtocol) {
/* 3266 */             StringBuffer traceMessageBuf = new StringBuffer();
/*      */             
/* 3268 */             traceMessageBuf.append(Messages.getString("MysqlIO.57"));
/* 3269 */             traceMessageBuf.append(getPacketDumpToLog(packetToSend, packetLen));
/*      */             
/* 3271 */             traceMessageBuf.append(Messages.getString("MysqlIO.58"));
/* 3272 */             traceMessageBuf.append(getPacketDumpToLog(packet, originalPacketLen));
/*      */ 
/*      */             
/* 3275 */             this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */           } 
/*      */         } else {
/* 3278 */           packetToSend.writeLongInt(packetLen - 4);
/* 3279 */           packetToSend.writeByte(this.packetSequence);
/*      */           
/* 3281 */           if (this.traceProtocol) {
/* 3282 */             StringBuffer traceMessageBuf = new StringBuffer();
/*      */             
/* 3284 */             traceMessageBuf.append(Messages.getString("MysqlIO.59"));
/* 3285 */             traceMessageBuf.append(packetToSend.dump(packetLen));
/*      */             
/* 3287 */             this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 3292 */         this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/*      */         
/* 3294 */         this.mysqlOutput.flush();
/*      */       } 
/*      */       
/* 3297 */       if (this.enablePacketDebug) {
/* 3298 */         enqueuePacketForDebugging(true, false, packetLen + 5, this.packetHeaderBuf, packet);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3305 */       if (packet == this.sharedSendPacket) {
/* 3306 */         reclaimLargeSharedSendPacket();
/*      */       }
/*      */       
/* 3309 */       if (this.connection.getMaintainTimeStats()) {
/* 3310 */         this.lastPacketSentTimeMs = System.currentTimeMillis();
/*      */       }
/* 3312 */     } catch (IOException ioEx) {
/* 3313 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
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
/*      */   private final ResultSetImpl sendFileToServer(StatementImpl callingStatement, String fileName) throws SQLException {
/* 3331 */     Buffer filePacket = (this.loadFileBufRef == null) ? null : this.loadFileBufRef.get();
/*      */ 
/*      */     
/* 3334 */     int bigPacketLength = Math.min(this.connection.getMaxAllowedPacket() - 12, alignPacketSize(this.connection.getMaxAllowedPacket() - 16, 4096) - 12);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3339 */     int oneMeg = 1048576;
/*      */     
/* 3341 */     int smallerPacketSizeAligned = Math.min(oneMeg - 12, alignPacketSize(oneMeg - 16, 4096) - 12);
/*      */ 
/*      */     
/* 3344 */     int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);
/*      */     
/* 3346 */     if (filePacket == null) {
/*      */       try {
/* 3348 */         filePacket = new Buffer(packetLength + 4);
/* 3349 */         this.loadFileBufRef = new SoftReference(filePacket);
/* 3350 */       } catch (OutOfMemoryError oom) {
/* 3351 */         throw SQLError.createSQLException("Could not allocate packet of " + packetLength + " bytes required for LOAD DATA LOCAL INFILE operation." + " Try increasing max heap allocation for JVM or decreasing server variable " + "'max_allowed_packet'", "S1001", getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3359 */     filePacket.clear();
/* 3360 */     send(filePacket, 0);
/*      */     
/* 3362 */     byte[] fileBuf = new byte[packetLength];
/*      */     
/* 3364 */     BufferedInputStream fileIn = null;
/*      */     
/*      */     try {
/* 3367 */       if (!this.connection.getAllowLoadLocalInfile()) {
/* 3368 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3373 */       InputStream hookedStream = null;
/*      */       
/* 3375 */       if (callingStatement != null) {
/* 3376 */         hookedStream = callingStatement.getLocalInfileInputStream();
/*      */       }
/*      */       
/* 3379 */       if (hookedStream != null) {
/* 3380 */         fileIn = new BufferedInputStream(hookedStream);
/* 3381 */       } else if (!this.connection.getAllowUrlInLocalInfile()) {
/* 3382 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */       
/*      */       }
/* 3385 */       else if (fileName.indexOf(':') != -1) {
/*      */         try {
/* 3387 */           URL urlFromFileName = new URL(fileName);
/* 3388 */           fileIn = new BufferedInputStream(urlFromFileName.openStream());
/* 3389 */         } catch (MalformedURLException badUrlEx) {
/*      */           
/* 3391 */           fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */         } 
/*      */       } else {
/*      */         
/* 3395 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3400 */       int bytesRead = 0;
/*      */       
/* 3402 */       while ((bytesRead = fileIn.read(fileBuf)) != -1) {
/* 3403 */         filePacket.clear();
/* 3404 */         filePacket.writeBytesNoNull(fileBuf, 0, bytesRead);
/* 3405 */         send(filePacket, filePacket.getPosition());
/*      */       } 
/* 3407 */     } catch (IOException ioEx) {
/* 3408 */       StringBuffer messageBuf = new StringBuffer(Messages.getString("MysqlIO.60"));
/*      */ 
/*      */       
/* 3411 */       if (!this.connection.getParanoid()) {
/* 3412 */         messageBuf.append("'");
/*      */         
/* 3414 */         if (fileName != null) {
/* 3415 */           messageBuf.append(fileName);
/*      */         }
/*      */         
/* 3418 */         messageBuf.append("'");
/*      */       } 
/*      */       
/* 3421 */       messageBuf.append(Messages.getString("MysqlIO.63"));
/*      */       
/* 3423 */       if (!this.connection.getParanoid()) {
/* 3424 */         messageBuf.append(Messages.getString("MysqlIO.64"));
/* 3425 */         messageBuf.append(Util.stackTraceToString(ioEx));
/*      */       } 
/*      */       
/* 3428 */       throw SQLError.createSQLException(messageBuf.toString(), "S1009", getExceptionInterceptor());
/*      */     } finally {
/*      */       
/* 3431 */       if (fileIn != null) {
/*      */         try {
/* 3433 */           fileIn.close();
/* 3434 */         } catch (Exception ex) {
/* 3435 */           SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.65"), "S1000", getExceptionInterceptor());
/*      */           
/* 3437 */           sqlEx.initCause(ex);
/*      */           
/* 3439 */           throw sqlEx;
/*      */         } 
/*      */         
/* 3442 */         fileIn = null;
/*      */       } else {
/*      */         
/* 3445 */         filePacket.clear();
/* 3446 */         send(filePacket, filePacket.getPosition());
/* 3447 */         checkErrorPacket();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 3452 */     filePacket.clear();
/* 3453 */     send(filePacket, filePacket.getPosition());
/*      */     
/* 3455 */     Buffer resultPacket = checkErrorPacket();
/*      */     
/* 3457 */     return buildResultSetWithUpdates(callingStatement, resultPacket);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Buffer checkErrorPacket(int command) throws SQLException {
/* 3472 */     int statusCode = 0;
/* 3473 */     Buffer resultPacket = null;
/* 3474 */     this.serverStatus = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3481 */       resultPacket = reuseAndReadPacket(this.reusablePacket);
/* 3482 */     } catch (SQLException sqlEx) {
/*      */       
/* 3484 */       throw sqlEx;
/* 3485 */     } catch (Exception fallThru) {
/* 3486 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, fallThru, getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */     
/* 3490 */     checkErrorPacket(resultPacket);
/*      */     
/* 3492 */     return resultPacket;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkErrorPacket(Buffer resultPacket) throws SQLException {
/* 3497 */     int statusCode = resultPacket.readByte();
/*      */ 
/*      */     
/* 3500 */     if (statusCode == -1) {
/*      */       
/* 3502 */       int errno = 2000;
/*      */       
/* 3504 */       if (this.protocolVersion > 9) {
/* 3505 */         errno = resultPacket.readInt();
/*      */         
/* 3507 */         String xOpen = null;
/*      */         
/* 3509 */         String str1 = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */ 
/*      */         
/* 3512 */         if (str1.charAt(0) == '#') {
/*      */ 
/*      */           
/* 3515 */           if (str1.length() > 6) {
/* 3516 */             xOpen = str1.substring(1, 6);
/* 3517 */             str1 = str1.substring(6);
/*      */             
/* 3519 */             if (xOpen.equals("HY000")) {
/* 3520 */               xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */             }
/*      */           } else {
/*      */             
/* 3524 */             xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */           } 
/*      */         } else {
/*      */           
/* 3528 */           xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */         } 
/*      */ 
/*      */         
/* 3532 */         clearInputStream();
/*      */         
/* 3534 */         StringBuffer stringBuffer = new StringBuffer();
/*      */         
/* 3536 */         String xOpenErrorMessage = SQLError.get(xOpen);
/*      */         
/* 3538 */         if (!this.connection.getUseOnlyServerErrorMessages() && 
/* 3539 */           xOpenErrorMessage != null) {
/* 3540 */           stringBuffer.append(xOpenErrorMessage);
/* 3541 */           stringBuffer.append(Messages.getString("MysqlIO.68"));
/*      */         } 
/*      */ 
/*      */         
/* 3545 */         stringBuffer.append(str1);
/*      */         
/* 3547 */         if (!this.connection.getUseOnlyServerErrorMessages() && 
/* 3548 */           xOpenErrorMessage != null) {
/* 3549 */           stringBuffer.append("\"");
/*      */         }
/*      */ 
/*      */         
/* 3553 */         appendInnodbStatusInformation(xOpen, stringBuffer);
/*      */         
/* 3555 */         if (xOpen != null && xOpen.startsWith("22")) {
/* 3556 */           throw new MysqlDataTruncation(stringBuffer.toString(), 0, true, false, 0, 0, errno);
/*      */         }
/* 3558 */         throw SQLError.createSQLException(stringBuffer.toString(), xOpen, errno, getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */       
/* 3562 */       String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */       
/* 3564 */       clearInputStream();
/*      */       
/* 3566 */       if (serverErrorMessage.indexOf(Messages.getString("MysqlIO.70")) != -1) {
/* 3567 */         throw SQLError.createSQLException(SQLError.get("S0022") + ", " + serverErrorMessage, "S0022", -1, getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3574 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.72"));
/*      */       
/* 3576 */       errorBuf.append(serverErrorMessage);
/* 3577 */       errorBuf.append("\"");
/*      */       
/* 3579 */       throw SQLError.createSQLException(SQLError.get("S1000") + ", " + errorBuf.toString(), "S1000", -1, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendInnodbStatusInformation(String xOpen, StringBuffer errorBuf) throws SQLException {
/* 3587 */     if (this.connection.getIncludeInnodbStatusInDeadlockExceptions() && xOpen != null && (xOpen.startsWith("40") || xOpen.startsWith("41")) && this.streamingData == null) {
/*      */ 
/*      */ 
/*      */       
/* 3591 */       ResultSet rs = null;
/*      */       
/*      */       try {
/* 3594 */         rs = sqlQueryDirect(null, "SHOW ENGINE INNODB STATUS", this.connection.getEncoding(), null, -1, 1003, 1007, false, this.connection.getCatalog(), null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3600 */         if (rs.next()) {
/* 3601 */           errorBuf.append("\n\n");
/* 3602 */           errorBuf.append(rs.getString("Status"));
/*      */         } else {
/* 3604 */           errorBuf.append("\n\n");
/* 3605 */           errorBuf.append(Messages.getString("MysqlIO.NoInnoDBStatusFound"));
/*      */         }
/*      */       
/* 3608 */       } catch (Exception ex) {
/* 3609 */         errorBuf.append("\n\n");
/* 3610 */         errorBuf.append(Messages.getString("MysqlIO.InnoDBStatusFailed"));
/*      */         
/* 3612 */         errorBuf.append("\n\n");
/* 3613 */         errorBuf.append(Util.stackTraceToString(ex));
/*      */       } finally {
/* 3615 */         if (rs != null) {
/* 3616 */           rs.close();
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
/*      */   private final void sendSplitPackets(Buffer packet) throws SQLException {
/*      */     try {
/* 3642 */       Buffer headerPacket = (this.splitBufRef == null) ? null : this.splitBufRef.get();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3650 */       if (headerPacket == null) {
/* 3651 */         headerPacket = new Buffer(this.maxThreeBytes + 4);
/*      */         
/* 3653 */         this.splitBufRef = new SoftReference(headerPacket);
/*      */       } 
/*      */       
/* 3656 */       int len = packet.getPosition();
/* 3657 */       int splitSize = this.maxThreeBytes;
/* 3658 */       int originalPacketPos = 4;
/* 3659 */       byte[] origPacketBytes = packet.getByteBuffer();
/* 3660 */       byte[] headerPacketBytes = headerPacket.getByteBuffer();
/*      */       
/* 3662 */       while (len >= this.maxThreeBytes) {
/* 3663 */         this.packetSequence = (byte)(this.packetSequence + 1);
/*      */         
/* 3665 */         headerPacket.setPosition(0);
/* 3666 */         headerPacket.writeLongInt(splitSize);
/*      */         
/* 3668 */         headerPacket.writeByte(this.packetSequence);
/* 3669 */         System.arraycopy(origPacketBytes, originalPacketPos, headerPacketBytes, 4, splitSize);
/*      */ 
/*      */         
/* 3672 */         int i = splitSize + 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3678 */         if (!this.useCompression) {
/* 3679 */           this.mysqlOutput.write(headerPacketBytes, 0, splitSize + 4);
/*      */           
/* 3681 */           this.mysqlOutput.flush();
/*      */         }
/*      */         else {
/*      */           
/* 3685 */           headerPacket.setPosition(0);
/* 3686 */           Buffer packetToSend = compressPacket(headerPacket, 4, splitSize, 4);
/*      */           
/* 3688 */           i = packetToSend.getPosition();
/*      */           
/* 3690 */           this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, i);
/*      */           
/* 3692 */           this.mysqlOutput.flush();
/*      */         } 
/*      */         
/* 3695 */         originalPacketPos += splitSize;
/* 3696 */         len -= splitSize;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3702 */       headerPacket.clear();
/* 3703 */       headerPacket.setPosition(0);
/* 3704 */       headerPacket.writeLongInt(len - 4);
/* 3705 */       this.packetSequence = (byte)(this.packetSequence + 1);
/* 3706 */       headerPacket.writeByte(this.packetSequence);
/*      */       
/* 3708 */       if (len != 0) {
/* 3709 */         System.arraycopy(origPacketBytes, originalPacketPos, headerPacketBytes, 4, len - 4);
/*      */       }
/*      */ 
/*      */       
/* 3713 */       int packetLen = len - 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3719 */       if (!this.useCompression) {
/* 3720 */         this.mysqlOutput.write(headerPacket.getByteBuffer(), 0, len);
/* 3721 */         this.mysqlOutput.flush();
/*      */       }
/*      */       else {
/*      */         
/* 3725 */         headerPacket.setPosition(0);
/* 3726 */         Buffer packetToSend = compressPacket(headerPacket, 4, packetLen, 4);
/*      */         
/* 3728 */         packetLen = packetToSend.getPosition();
/*      */         
/* 3730 */         this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/*      */         
/* 3732 */         this.mysqlOutput.flush();
/*      */       } 
/* 3734 */     } catch (IOException ioEx) {
/* 3735 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void reclaimLargeSharedSendPacket() {
/* 3741 */     if (this.sharedSendPacket != null && this.sharedSendPacket.getCapacity() > 1048576)
/*      */     {
/* 3743 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */   }
/*      */   
/*      */   boolean hadWarnings() {
/* 3748 */     return this.hadWarnings;
/*      */   }
/*      */   
/*      */   void scanForAndThrowDataTruncation() throws SQLException {
/* 3752 */     if (this.streamingData == null && versionMeetsMinimum(4, 1, 0) && this.connection.getJdbcCompliantTruncation() && this.warningCount > 0)
/*      */     {
/* 3754 */       SQLError.convertShowWarningsToSQLWarnings(this.connection, this.warningCount, true);
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
/*      */   private void secureAuth(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams) throws SQLException {
/* 3775 */     if (packet == null) {
/* 3776 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 3779 */     if (writeClientParams) {
/* 3780 */       if (this.use41Extensions) {
/* 3781 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 3782 */           packet.writeLong(this.clientParam);
/* 3783 */           packet.writeLong(this.maxThreeBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3788 */           packet.writeByte((byte)8);
/*      */ 
/*      */           
/* 3791 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 3793 */           packet.writeLong(this.clientParam);
/* 3794 */           packet.writeLong(this.maxThreeBytes);
/*      */         } 
/*      */       } else {
/* 3797 */         packet.writeInt((int)this.clientParam);
/* 3798 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3803 */     packet.writeString(user, "Cp1252", this.connection);
/*      */     
/* 3805 */     if (password.length() != 0) {
/*      */       
/* 3807 */       packet.writeString("xxxxxxxx", "Cp1252", this.connection);
/*      */     } else {
/*      */       
/* 3810 */       packet.writeString("", "Cp1252", this.connection);
/*      */     } 
/*      */     
/* 3813 */     if (this.useConnectWithDb) {
/* 3814 */       packet.writeString(database, "Cp1252", this.connection);
/*      */     }
/*      */     
/* 3817 */     send(packet, packet.getPosition());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3822 */     if (password.length() > 0) {
/* 3823 */       Buffer b = readPacket();
/*      */       
/* 3825 */       b.setPosition(0);
/*      */       
/* 3827 */       byte[] replyAsBytes = b.getByteBuffer();
/*      */       
/* 3829 */       if (replyAsBytes.length == 25 && replyAsBytes[0] != 0)
/*      */       {
/* 3831 */         if (replyAsBytes[0] != 42) {
/*      */           
/*      */           try {
/* 3834 */             byte[] buff = Security.passwordHashStage1(password);
/*      */ 
/*      */             
/* 3837 */             byte[] passwordHash = new byte[buff.length];
/* 3838 */             System.arraycopy(buff, 0, passwordHash, 0, buff.length);
/*      */ 
/*      */             
/* 3841 */             passwordHash = Security.passwordHashStage2(passwordHash, replyAsBytes);
/*      */ 
/*      */             
/* 3844 */             byte[] packetDataAfterSalt = new byte[replyAsBytes.length - 5];
/*      */ 
/*      */             
/* 3847 */             System.arraycopy(replyAsBytes, 4, packetDataAfterSalt, 0, replyAsBytes.length - 5);
/*      */ 
/*      */             
/* 3850 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */ 
/*      */             
/* 3853 */             Security.passwordCrypt(packetDataAfterSalt, mysqlScrambleBuff, passwordHash, 20);
/*      */ 
/*      */ 
/*      */             
/* 3857 */             Security.passwordCrypt(mysqlScrambleBuff, buff, buff, 20);
/*      */             
/* 3859 */             Buffer packet2 = new Buffer(25);
/* 3860 */             packet2.writeBytesNoNull(buff);
/*      */             
/* 3862 */             this.packetSequence = (byte)(this.packetSequence + 1);
/*      */             
/* 3864 */             send(packet2, 24);
/* 3865 */           } catch (NoSuchAlgorithmException nse) {
/* 3866 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), "S1000", getExceptionInterceptor());
/*      */           } 
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */             
/* 3873 */             byte[] passwordHash = Security.createKeyFromOldPassword(password);
/*      */ 
/*      */             
/* 3876 */             byte[] netReadPos4 = new byte[replyAsBytes.length - 5];
/*      */             
/* 3878 */             System.arraycopy(replyAsBytes, 4, netReadPos4, 0, replyAsBytes.length - 5);
/*      */ 
/*      */             
/* 3881 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */ 
/*      */             
/* 3884 */             Security.passwordCrypt(netReadPos4, mysqlScrambleBuff, passwordHash, 20);
/*      */ 
/*      */ 
/*      */             
/* 3888 */             String scrambledPassword = Util.scramble(new String(mysqlScrambleBuff), password);
/*      */ 
/*      */             
/* 3891 */             Buffer packet2 = new Buffer(packLength);
/* 3892 */             packet2.writeString(scrambledPassword, "Cp1252", this.connection);
/* 3893 */             this.packetSequence = (byte)(this.packetSequence + 1);
/*      */             
/* 3895 */             send(packet2, 24);
/* 3896 */           } catch (NoSuchAlgorithmException nse) {
/* 3897 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.93") + Messages.getString("MysqlIO.94"), "S1000", getExceptionInterceptor());
/*      */           } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void secureAuth411(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams) throws SQLException {
/* 3939 */     if (packet == null) {
/* 3940 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 3943 */     if (writeClientParams) {
/* 3944 */       if (this.use41Extensions) {
/* 3945 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 3946 */           packet.writeLong(this.clientParam);
/* 3947 */           packet.writeLong(this.maxThreeBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3952 */           packet.writeByte((byte)33);
/*      */ 
/*      */           
/* 3955 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 3957 */           packet.writeLong(this.clientParam);
/* 3958 */           packet.writeLong(this.maxThreeBytes);
/*      */         } 
/*      */       } else {
/* 3961 */         packet.writeInt((int)this.clientParam);
/* 3962 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3967 */     packet.writeString(user, "utf-8", this.connection);
/*      */     
/* 3969 */     if (password.length() != 0) {
/* 3970 */       packet.writeByte((byte)20);
/*      */       
/*      */       try {
/* 3973 */         packet.writeBytesNoNull(Security.scramble411(password, this.seed, this.connection));
/* 3974 */       } catch (NoSuchAlgorithmException nse) {
/* 3975 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());
/*      */       
/*      */       }
/* 3978 */       catch (UnsupportedEncodingException e) {
/* 3979 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 3985 */       packet.writeByte((byte)0);
/*      */     } 
/*      */     
/* 3988 */     if (this.useConnectWithDb) {
/* 3989 */       packet.writeString(database, "utf-8", this.connection);
/*      */     }
/*      */     
/* 3992 */     send(packet, packet.getPosition());
/*      */     
/* 3994 */     byte savePacketSequence = this.packetSequence = (byte)(this.packetSequence + 1);
/*      */     
/* 3996 */     Buffer reply = checkErrorPacket();
/*      */     
/* 3998 */     if (reply.isLastDataPacket()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4003 */       this.packetSequence = savePacketSequence = (byte)(savePacketSequence + 1);
/* 4004 */       packet.clear();
/*      */       
/* 4006 */       String seed323 = this.seed.substring(0, 8);
/* 4007 */       packet.writeString(Util.newCrypt(password, seed323));
/* 4008 */       send(packet, packet.getPosition());
/*      */ 
/*      */       
/* 4011 */       checkErrorPacket();
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
/*      */   private final ResultSetRow unpackBinaryResultSetRow(Field[] fields, Buffer binaryData, int resultSetConcurrency) throws SQLException {
/* 4028 */     int numFields = fields.length;
/*      */     
/* 4030 */     byte[][] unpackedRowData = new byte[numFields][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4037 */     int nullCount = (numFields + 9) / 8;
/*      */     
/* 4039 */     byte[] nullBitMask = new byte[nullCount];
/*      */     
/* 4041 */     for (int i = 0; i < nullCount; i++) {
/* 4042 */       nullBitMask[i] = binaryData.readByte();
/*      */     }
/*      */     
/* 4045 */     int nullMaskPos = 0;
/* 4046 */     int bit = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4053 */     for (int j = 0; j < numFields; j++) {
/* 4054 */       if ((nullBitMask[nullMaskPos] & bit) != 0) {
/* 4055 */         unpackedRowData[j] = null;
/*      */       }
/* 4057 */       else if (resultSetConcurrency != 1008) {
/* 4058 */         extractNativeEncodedColumn(binaryData, fields, j, unpackedRowData);
/*      */       } else {
/*      */         
/* 4061 */         unpackNativeEncodedColumn(binaryData, fields, j, unpackedRowData);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 4066 */       if (((bit <<= 1) & 0xFF) == 0) {
/* 4067 */         bit = 1;
/*      */         
/* 4069 */         nullMaskPos++;
/*      */       } 
/*      */     } 
/*      */     
/* 4073 */     return new ByteArrayRow(unpackedRowData, getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */   
/*      */   private final void extractNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException {
/*      */     int length;
/* 4079 */     Field curField = fields[columnIndex];
/*      */     
/* 4081 */     switch (curField.getMysqlType()) {
/*      */       case 6:
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/* 4087 */         (new byte[1])[0] = binaryData.readByte(); unpackedRowData[columnIndex] = new byte[1];
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 13:
/* 4093 */         unpackedRowData[columnIndex] = binaryData.getBytes(2);
/*      */ 
/*      */       
/*      */       case 3:
/*      */       case 9:
/* 4098 */         unpackedRowData[columnIndex] = binaryData.getBytes(4);
/*      */ 
/*      */       
/*      */       case 8:
/* 4102 */         unpackedRowData[columnIndex] = binaryData.getBytes(8);
/*      */ 
/*      */       
/*      */       case 4:
/* 4106 */         unpackedRowData[columnIndex] = binaryData.getBytes(4);
/*      */ 
/*      */       
/*      */       case 5:
/* 4110 */         unpackedRowData[columnIndex] = binaryData.getBytes(8);
/*      */ 
/*      */       
/*      */       case 11:
/* 4114 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4116 */         unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */ 
/*      */ 
/*      */       
/*      */       case 10:
/* 4121 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4123 */         unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */ 
/*      */       
/*      */       case 7:
/*      */       case 12:
/* 4128 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4130 */         unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */       
/*      */       case 0:
/*      */       case 15:
/*      */       case 246:
/*      */       case 249:
/*      */       case 250:
/*      */       case 251:
/*      */       case 252:
/*      */       case 253:
/*      */       case 254:
/*      */       case 255:
/* 4142 */         unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */ 
/*      */       
/*      */       case 16:
/* 4146 */         unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */     } 
/*      */ 
/*      */     
/* 4150 */     throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor()); } private final void unpackNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException { byte tinyVal; short shortVal; int intVal;
/*      */     long longVal;
/*      */     float floatVal;
/*      */     double doubleVal;
/*      */     int length, hour, minute, seconds;
/*      */     byte[] timeAsBytes;
/*      */     int year, month, day;
/*      */     byte[] arrayOfByte1;
/*      */     int i, j, nanos, k;
/*      */     byte[] arrayOfByte2, arrayOfByte3;
/*      */     byte b;
/*      */     boolean bool;
/* 4162 */     Field curField = fields[columnIndex];
/*      */     
/* 4164 */     switch (curField.getMysqlType()) {
/*      */       case 6:
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/* 4170 */         tinyVal = binaryData.readByte();
/*      */         
/* 4172 */         if (!curField.isUnsigned()) {
/* 4173 */           unpackedRowData[columnIndex] = String.valueOf(tinyVal).getBytes();
/*      */         } else {
/*      */           
/* 4176 */           short unsignedTinyVal = (short)(tinyVal & 0xFF);
/*      */           
/* 4178 */           unpackedRowData[columnIndex] = String.valueOf(unsignedTinyVal).getBytes();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 13:
/* 4187 */         shortVal = (short)binaryData.readInt();
/*      */         
/* 4189 */         if (!curField.isUnsigned()) {
/* 4190 */           unpackedRowData[columnIndex] = String.valueOf(shortVal).getBytes();
/*      */         } else {
/*      */           
/* 4193 */           int unsignedShortVal = shortVal & 0xFFFF;
/*      */           
/* 4195 */           unpackedRowData[columnIndex] = String.valueOf(unsignedShortVal).getBytes();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 3:
/*      */       case 9:
/* 4204 */         intVal = (int)binaryData.readLong();
/*      */         
/* 4206 */         if (!curField.isUnsigned()) {
/* 4207 */           unpackedRowData[columnIndex] = String.valueOf(intVal).getBytes();
/*      */         } else {
/*      */           
/* 4210 */           long l = intVal & 0xFFFFFFFFL;
/*      */           
/* 4212 */           unpackedRowData[columnIndex] = String.valueOf(l).getBytes();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 8:
/* 4220 */         longVal = binaryData.readLongLong();
/*      */         
/* 4222 */         if (!curField.isUnsigned()) {
/* 4223 */           unpackedRowData[columnIndex] = String.valueOf(longVal).getBytes();
/*      */         } else {
/*      */           
/* 4226 */           BigInteger asBigInteger = ResultSetImpl.convertLongToUlong(longVal);
/*      */           
/* 4228 */           unpackedRowData[columnIndex] = asBigInteger.toString().getBytes();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/* 4236 */         floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());
/*      */         
/* 4238 */         unpackedRowData[columnIndex] = String.valueOf(floatVal).getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/* 4244 */         doubleVal = Double.longBitsToDouble(binaryData.readLongLong());
/*      */         
/* 4246 */         unpackedRowData[columnIndex] = String.valueOf(doubleVal).getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 11:
/* 4252 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4254 */         hour = 0;
/* 4255 */         minute = 0;
/* 4256 */         seconds = 0;
/*      */         
/* 4258 */         if (length != 0) {
/* 4259 */           binaryData.readByte();
/* 4260 */           binaryData.readLong();
/* 4261 */           hour = binaryData.readByte();
/* 4262 */           minute = binaryData.readByte();
/* 4263 */           seconds = binaryData.readByte();
/*      */           
/* 4265 */           if (length > 8) {
/* 4266 */             binaryData.readLong();
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 4271 */         timeAsBytes = new byte[8];
/*      */         
/* 4273 */         timeAsBytes[0] = (byte)Character.forDigit(hour / 10, 10);
/* 4274 */         timeAsBytes[1] = (byte)Character.forDigit(hour % 10, 10);
/*      */         
/* 4276 */         timeAsBytes[2] = 58;
/*      */         
/* 4278 */         timeAsBytes[3] = (byte)Character.forDigit(minute / 10, 10);
/*      */         
/* 4280 */         timeAsBytes[4] = (byte)Character.forDigit(minute % 10, 10);
/*      */ 
/*      */         
/* 4283 */         timeAsBytes[5] = 58;
/*      */         
/* 4285 */         timeAsBytes[6] = (byte)Character.forDigit(seconds / 10, 10);
/*      */         
/* 4287 */         timeAsBytes[7] = (byte)Character.forDigit(seconds % 10, 10);
/*      */ 
/*      */         
/* 4290 */         unpackedRowData[columnIndex] = timeAsBytes;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 10:
/* 4296 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4298 */         year = 0;
/* 4299 */         month = 0;
/* 4300 */         day = 0;
/*      */         
/* 4302 */         hour = 0;
/* 4303 */         minute = 0;
/* 4304 */         seconds = 0;
/*      */         
/* 4306 */         if (length != 0) {
/* 4307 */           year = binaryData.readInt();
/* 4308 */           month = binaryData.readByte();
/* 4309 */           day = binaryData.readByte();
/*      */         } 
/*      */         
/* 4312 */         if (year == 0 && month == 0 && day == 0)
/* 4313 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           
/* 4315 */           { unpackedRowData[columnIndex] = null; }
/*      */           else
/*      */           
/* 4318 */           { if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */             {
/* 4320 */               throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", getExceptionInterceptor());
/*      */             }
/*      */ 
/*      */             
/* 4324 */             year = 1;
/* 4325 */             month = 1;
/* 4326 */             day = 1;
/*      */ 
/*      */ 
/*      */             
/* 4330 */             byte[] dateAsBytes = new byte[10];
/*      */             
/* 4332 */             dateAsBytes[0] = (byte)Character.forDigit(year / 1000, 10);
/*      */ 
/*      */             
/* 4335 */             int after1000 = year % 1000;
/*      */             
/* 4337 */             dateAsBytes[1] = (byte)Character.forDigit(after1000 / 100, 10);
/*      */ 
/*      */             
/* 4340 */             int after100 = after1000 % 100;
/*      */             
/* 4342 */             dateAsBytes[2] = (byte)Character.forDigit(after100 / 10, 10);
/*      */             
/* 4344 */             dateAsBytes[3] = (byte)Character.forDigit(after100 % 10, 10);
/*      */ 
/*      */             
/* 4347 */             dateAsBytes[4] = 45;
/*      */             
/* 4349 */             dateAsBytes[5] = (byte)Character.forDigit(month / 10, 10);
/*      */             
/* 4351 */             dateAsBytes[6] = (byte)Character.forDigit(month % 10, 10);
/*      */ 
/*      */             
/* 4354 */             dateAsBytes[7] = 45;
/*      */             
/* 4356 */             dateAsBytes[8] = (byte)Character.forDigit(day / 10, 10);
/* 4357 */             dateAsBytes[9] = (byte)Character.forDigit(day % 10, 10);
/*      */             
/* 4359 */             unpackedRowData[columnIndex] = dateAsBytes; }   arrayOfByte1 = new byte[10]; arrayOfByte1[0] = (byte)Character.forDigit(year / 1000, 10); i = year % 1000; arrayOfByte1[1] = (byte)Character.forDigit(i / 100, 10); j = i % 100; arrayOfByte1[2] = (byte)Character.forDigit(j / 10, 10); arrayOfByte1[3] = (byte)Character.forDigit(j % 10, 10); arrayOfByte1[4] = 45; arrayOfByte1[5] = (byte)Character.forDigit(month / 10, 10); arrayOfByte1[6] = (byte)Character.forDigit(month % 10, 10); arrayOfByte1[7] = 45; arrayOfByte1[8] = (byte)Character.forDigit(day / 10, 10); arrayOfByte1[9] = (byte)Character.forDigit(day % 10, 10); unpackedRowData[columnIndex] = arrayOfByte1;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 7:
/*      */       case 12:
/* 4366 */         length = (int)binaryData.readFieldLength();
/*      */         
/* 4368 */         year = 0;
/* 4369 */         month = 0;
/* 4370 */         day = 0;
/*      */         
/* 4372 */         hour = 0;
/* 4373 */         minute = 0;
/* 4374 */         seconds = 0;
/*      */         
/* 4376 */         nanos = 0;
/*      */         
/* 4378 */         if (length != 0) {
/* 4379 */           year = binaryData.readInt();
/* 4380 */           month = binaryData.readByte();
/* 4381 */           day = binaryData.readByte();
/*      */           
/* 4383 */           if (length > 4) {
/* 4384 */             hour = binaryData.readByte();
/* 4385 */             minute = binaryData.readByte();
/* 4386 */             seconds = binaryData.readByte();
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4394 */         if (year == 0 && month == 0 && day == 0)
/* 4395 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           
/* 4397 */           { unpackedRowData[columnIndex] = null; }
/*      */           else
/*      */           
/* 4400 */           { if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */             {
/* 4402 */               throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", getExceptionInterceptor());
/*      */             }
/*      */ 
/*      */             
/* 4406 */             year = 1;
/* 4407 */             month = 1;
/* 4408 */             day = 1;
/*      */ 
/*      */ 
/*      */             
/* 4412 */             int stringLength = 19;
/*      */             
/* 4414 */             byte[] nanosAsBytes = Integer.toString(nanos).getBytes();
/*      */             
/* 4416 */             stringLength += 1 + nanosAsBytes.length;
/*      */             
/* 4418 */             byte[] datetimeAsBytes = new byte[stringLength];
/*      */             
/* 4420 */             datetimeAsBytes[0] = (byte)Character.forDigit(year / 1000, 10);
/*      */ 
/*      */             
/* 4423 */             int after1000 = year % 1000;
/*      */             
/* 4425 */             datetimeAsBytes[1] = (byte)Character.forDigit(after1000 / 100, 10);
/*      */ 
/*      */             
/* 4428 */             int after100 = after1000 % 100;
/*      */             
/* 4430 */             datetimeAsBytes[2] = (byte)Character.forDigit(after100 / 10, 10);
/*      */             
/* 4432 */             datetimeAsBytes[3] = (byte)Character.forDigit(after100 % 10, 10);
/*      */ 
/*      */             
/* 4435 */             datetimeAsBytes[4] = 45;
/*      */             
/* 4437 */             datetimeAsBytes[5] = (byte)Character.forDigit(month / 10, 10);
/*      */             
/* 4439 */             datetimeAsBytes[6] = (byte)Character.forDigit(month % 10, 10);
/*      */ 
/*      */             
/* 4442 */             datetimeAsBytes[7] = 45;
/*      */             
/* 4444 */             datetimeAsBytes[8] = (byte)Character.forDigit(day / 10, 10);
/*      */             
/* 4446 */             datetimeAsBytes[9] = (byte)Character.forDigit(day % 10, 10);
/*      */ 
/*      */             
/* 4449 */             datetimeAsBytes[10] = 32;
/*      */             
/* 4451 */             datetimeAsBytes[11] = (byte)Character.forDigit(hour / 10, 10);
/*      */             
/* 4453 */             datetimeAsBytes[12] = (byte)Character.forDigit(hour % 10, 10);
/*      */ 
/*      */             
/* 4456 */             datetimeAsBytes[13] = 58;
/*      */             
/* 4458 */             datetimeAsBytes[14] = (byte)Character.forDigit(minute / 10, 10);
/*      */             
/* 4460 */             datetimeAsBytes[15] = (byte)Character.forDigit(minute % 10, 10);
/*      */ 
/*      */             
/* 4463 */             datetimeAsBytes[16] = 58;
/*      */             
/* 4465 */             datetimeAsBytes[17] = (byte)Character.forDigit(seconds / 10, 10);
/*      */             
/* 4467 */             datetimeAsBytes[18] = (byte)Character.forDigit(seconds % 10, 10);
/*      */ 
/*      */             
/* 4470 */             datetimeAsBytes[19] = 46;
/*      */             
/* 4472 */             int nanosOffset = 20;
/*      */             
/* 4474 */             int m = 0; }   k = 19; arrayOfByte2 = Integer.toString(nanos).getBytes(); k += 1 + arrayOfByte2.length; arrayOfByte3 = new byte[k]; arrayOfByte3[0] = (byte)Character.forDigit(year / 1000, 10); i = year % 1000; arrayOfByte3[1] = (byte)Character.forDigit(i / 100, 10); j = i % 100; arrayOfByte3[2] = (byte)Character.forDigit(j / 10, 10); arrayOfByte3[3] = (byte)Character.forDigit(j % 10, 10); arrayOfByte3[4] = 45; arrayOfByte3[5] = (byte)Character.forDigit(month / 10, 10); arrayOfByte3[6] = (byte)Character.forDigit(month % 10, 10); arrayOfByte3[7] = 45; arrayOfByte3[8] = (byte)Character.forDigit(day / 10, 10); arrayOfByte3[9] = (byte)Character.forDigit(day % 10, 10); arrayOfByte3[10] = 32; arrayOfByte3[11] = (byte)Character.forDigit(hour / 10, 10); arrayOfByte3[12] = (byte)Character.forDigit(hour % 10, 10); arrayOfByte3[13] = 58; arrayOfByte3[14] = (byte)Character.forDigit(minute / 10, 10); arrayOfByte3[15] = (byte)Character.forDigit(minute % 10, 10); arrayOfByte3[16] = 58; arrayOfByte3[17] = (byte)Character.forDigit(seconds / 10, 10); arrayOfByte3[18] = (byte)Character.forDigit(seconds % 10, 10); arrayOfByte3[19] = 46; b = 20; bool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*      */       case 15:
/*      */       case 16:
/*      */       case 246:
/*      */       case 249:
/*      */       case 250:
/*      */       case 251:
/*      */       case 252:
/*      */       case 253:
/*      */       case 254:
/* 4493 */         unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 4498 */     throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void negotiateSSLConnection(String user, String password, String database, int packLength) throws SQLException {
/* 4521 */     if (!ExportControlled.enabled()) {
/* 4522 */       throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
/*      */     }
/*      */ 
/*      */     
/* 4526 */     boolean doSecureAuth = false;
/*      */     
/* 4528 */     if ((this.serverCapabilities & 0x8000) != 0) {
/* 4529 */       this.clientParam |= 0x8000L;
/* 4530 */       doSecureAuth = true;
/*      */     } 
/*      */     
/* 4533 */     this.clientParam |= 0x800L;
/*      */     
/* 4535 */     Buffer packet = new Buffer(packLength);
/*      */     
/* 4537 */     if (this.use41Extensions) {
/* 4538 */       packet.writeLong(this.clientParam);
/*      */     } else {
/* 4540 */       packet.writeInt((int)this.clientParam);
/*      */     } 
/*      */     
/* 4543 */     send(packet, packet.getPosition());
/*      */     
/* 4545 */     ExportControlled.transformSocketToSSLSocket(this);
/*      */     
/* 4547 */     packet.clear();
/*      */     
/* 4549 */     if (doSecureAuth) {
/* 4550 */       if (versionMeetsMinimum(4, 1, 1)) {
/* 4551 */         secureAuth411(null, packLength, user, password, database, true);
/*      */       } else {
/* 4553 */         secureAuth411(null, packLength, user, password, database, true);
/*      */       } 
/*      */     } else {
/* 4556 */       if (this.use41Extensions) {
/* 4557 */         packet.writeLong(this.clientParam);
/* 4558 */         packet.writeLong(this.maxThreeBytes);
/*      */       } else {
/* 4560 */         packet.writeInt((int)this.clientParam);
/* 4561 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       } 
/*      */ 
/*      */       
/* 4565 */       packet.writeString(user);
/*      */       
/* 4567 */       if (this.protocolVersion > 9) {
/* 4568 */         packet.writeString(Util.newCrypt(password, this.seed));
/*      */       } else {
/* 4570 */         packet.writeString(Util.oldCrypt(password, this.seed));
/*      */       } 
/*      */       
/* 4573 */       if ((this.serverCapabilities & 0x8) != 0 && database != null && database.length() > 0)
/*      */       {
/* 4575 */         packet.writeString(database);
/*      */       }
/*      */       
/* 4578 */       send(packet, packet.getPosition());
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int getServerStatus() {
/* 4583 */     return this.serverStatus;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected List fetchRowsViaCursor(List fetchedRows, long statementId, Field[] columnTypes, int fetchSize, boolean useBufferRowExplicit) throws SQLException {
/* 4589 */     if (fetchedRows == null) {
/* 4590 */       fetchedRows = new ArrayList(fetchSize);
/*      */     } else {
/* 4592 */       fetchedRows.clear();
/*      */     } 
/*      */     
/* 4595 */     this.sharedSendPacket.clear();
/*      */     
/* 4597 */     this.sharedSendPacket.writeByte((byte)28);
/* 4598 */     this.sharedSendPacket.writeLong(statementId);
/* 4599 */     this.sharedSendPacket.writeLong(fetchSize);
/*      */     
/* 4601 */     sendCommand(28, null, this.sharedSendPacket, true, null, 0);
/*      */ 
/*      */     
/* 4604 */     ResultSetRow row = null;
/*      */ 
/*      */     
/* 4607 */     while ((row = nextRow(columnTypes, columnTypes.length, true, 1007, false, useBufferRowExplicit, false, null)) != null) {
/* 4608 */       fetchedRows.add(row);
/*      */     }
/*      */     
/* 4611 */     return fetchedRows;
/*      */   }
/*      */   
/*      */   protected long getThreadId() {
/* 4615 */     return this.threadId;
/*      */   }
/*      */   
/*      */   protected boolean useNanosForElapsedTime() {
/* 4619 */     return this.useNanosForElapsedTime;
/*      */   }
/*      */   
/*      */   protected long getSlowQueryThreshold() {
/* 4623 */     return this.slowQueryThreshold;
/*      */   }
/*      */   
/*      */   protected String getQueryTimingUnits() {
/* 4627 */     return this.queryTimingUnits;
/*      */   }
/*      */   
/*      */   protected int getCommandCount() {
/* 4631 */     return this.commandCount;
/*      */   }
/*      */   
/*      */   private void checkTransactionState(int oldStatus) throws SQLException {
/* 4635 */     boolean previouslyInTrans = ((oldStatus & 0x1) != 0);
/* 4636 */     boolean currentlyInTrans = ((this.serverStatus & 0x1) != 0);
/*      */     
/* 4638 */     if (previouslyInTrans && !currentlyInTrans) {
/* 4639 */       this.connection.transactionCompleted();
/* 4640 */     } else if (!previouslyInTrans && currentlyInTrans) {
/* 4641 */       this.connection.transactionBegun();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setStatementInterceptors(List statementInterceptors) {
/* 4646 */     this.statementInterceptors = statementInterceptors;
/*      */   }
/*      */   
/*      */   protected ExceptionInterceptor getExceptionInterceptor() {
/* 4650 */     return this.exceptionInterceptor;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\MysqlIO.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */